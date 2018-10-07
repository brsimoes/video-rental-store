package com.casumo.videorentalstore.catalog.core.domain;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.casumo.videorentalstore.catalog.core.domain.command.CancelMovieRentalCommand;
import com.casumo.videorentalstore.catalog.core.domain.command.CreateMovieCommand;
import com.casumo.videorentalstore.catalog.core.domain.command.RentMovieCommand;
import com.casumo.videorentalstore.catalog.core.domain.command.ReturnMovieCommand;
import com.casumo.videorentalstore.catalog.core.domain.event.MovieCreatedEvent;
import com.casumo.videorentalstore.catalog.core.domain.event.MovieRentalCanceledEvent;
import com.casumo.videorentalstore.catalog.core.domain.event.MovieRentedEvent;
import com.casumo.videorentalstore.catalog.core.domain.event.MovieReturnedEvent;
import com.casumo.videorentalstore.enums.MovieType;

@Aggregate
public class MovieAggregate {
	@AggregateIdentifier
	private UUID movieId;
	private int availableCopies;
	private Map<UUID, RentalEntry> rentals;
	private MovieType type;

	protected MovieAggregate() {

	}

	@CommandHandler
	public MovieAggregate(CreateMovieCommand command) {
		apply(new MovieCreatedEvent(
						command.getMovieId(), 
						command.getName(), 
						command.getType(),
						command.getNumberCopies()));
	}

	@CommandHandler
	public void handle(RentMovieCommand command) {
		if (this.availableCopies - this.rentals.size() > 0) {
			apply(new MovieRentedEvent(
						command.getMovieId(), 
						this.type, 
						command.getRentalId(), 
						command.getHireDays(),
						command.getRentalDate(),
						this.availableCopies - this.rentals.size() - 1));
		} else {
			throw new UnavailableCopiesForRentException();
		}
	}

	@CommandHandler
	public void handle(ReturnMovieCommand command) {
		RentalEntry rental = this.rentals.get(command.getRentalId());
		if (rental != null) {
					
			apply(new MovieReturnedEvent(
						command.getMovieId(), 
						command.getRentalId(), 
						this.type,
						rental.rentalDate,
						command.getReturnDate(),
						this.availableCopies - this.rentals.size() + 1));
		} else {
			throw new MovieNotRentedException();
		}
	}

	@CommandHandler
	public void handle(CancelMovieRentalCommand command) throws MovieNotRentedException {
		if (this.rentals.containsKey(command.getRentalId())) {
			apply(new MovieRentalCanceledEvent(
						command.getMovieId(), 
						command.getRentalId(),
						this.availableCopies - this.rentals.size() + 1));
		}
	}

	@EventSourcingHandler
	public void on(MovieCreatedEvent event) {
		this.movieId = event.getMovieId();
		this.availableCopies = event.getMaxAvailableCopiesToRent();
		this.rentals = new HashMap<>(event.getMaxAvailableCopiesToRent());
		this.type = event.getType();
	}

	@EventSourcingHandler
	public void on(MovieRentedEvent event) {
		this.rentals.put(event.getRentalId(), new RentalEntry(event.getHireDays(), event.getRentalDate()));
	}

	@EventSourcingHandler
	public void on(MovieReturnedEvent event) {
		this.rentals.remove(event.getRentalId());
	}

	@EventSourcingHandler
	public void on(MovieRentalCanceledEvent event) {
		this.rentals.remove(event.getRentalId());
	}

	public class MovieNotRentedException extends RuntimeException {

		private static final long serialVersionUID = -194444633524613856L;

	}

	public class UnavailableCopiesForRentException extends RuntimeException {

		private static final long serialVersionUID = 2854276385696003476L;

	}

	private class RentalEntry {
		final int hireDaysAmmount;
		final LocalDate rentalDate;

		public RentalEntry(int hireDaysAmmount, LocalDate rentalDate) {
			this.hireDaysAmmount = hireDaysAmmount;
			this.rentalDate = rentalDate;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + hireDaysAmmount;
			result = prime * result + ((rentalDate == null) ? 0 : rentalDate.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof RentalEntry)) {
				return false;
			}
			RentalEntry other = (RentalEntry) obj;
			if (hireDaysAmmount != other.hireDaysAmmount) {
				return false;
			}
			if (rentalDate == null) {
				if (other.rentalDate != null) {
					return false;
				}
			} else if (!rentalDate.equals(other.rentalDate)) {
				return false;
			}
			return true;
		}
	}
}
