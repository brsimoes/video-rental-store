package com.casumo.videorentalstore.catalog.core.domain;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

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

@Aggregate
public class MovieAggregate {
	@AggregateIdentifier
	private UUID movieId;
	private int availableCopies;
	private Map<UUID, Integer> hireDaysAmmountByRental;
	private MovieType type;
	private String name;

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
		if (this.availableCopies - this.hireDaysAmmountByRental.size() > 0) {
			apply(new MovieRentedEvent(
						command.getMovieId(), 
						this.type, 
						command.getRentalId(), 
						command.getNumberOfDaysToHire(),
						this.availableCopies - this.hireDaysAmmountByRental.size() - 1,
						this.name));
		} else {
			throw new UnavailableCopiesForRentException();
		}
	}

	@CommandHandler
	public void handle(ReturnMovieCommand command) {
		Integer hireDaysAmmount = this.hireDaysAmmountByRental.get(command.getRentalId());
		if (hireDaysAmmount != null) {
					
			apply(new MovieReturnedEvent(
						command.getMovieId(), 
						command.getRentalId(), 
						this.type,
						hireDaysAmmount,
						command.getReturnDate(),
						this.availableCopies - this.hireDaysAmmountByRental.size() + 1));
		} else {
			throw new MovieNotRentedException();
		}
	}

	@CommandHandler
	public void handle(CancelMovieRentalCommand command) throws MovieNotRentedException {
		if (this.hireDaysAmmountByRental.containsKey(command.getRentalId())) {
			apply(new MovieRentalCanceledEvent(
						command.getMovieId(), 
						command.getRentalId(),
						this.availableCopies - this.hireDaysAmmountByRental.size() + 1));
		}
	}

	@EventSourcingHandler
	public void on(MovieCreatedEvent event) {
		this.movieId = event.getMovieId();
		this.availableCopies = event.getMaxAvailableCopiesToRent();
		this.hireDaysAmmountByRental = new HashMap<>(event.getMaxAvailableCopiesToRent());
		this.type = event.getType();
		this.name = event.getName();
	}

	@EventSourcingHandler
	public void on(MovieRentedEvent event) {
		this.hireDaysAmmountByRental.put(event.getRentalId(), event.getNumberOfDaysHired());
	}

	@EventSourcingHandler
	public void on(MovieReturnedEvent event) {
		this.hireDaysAmmountByRental.remove(event.getRentalId());
	}

	@EventSourcingHandler
	public void on(MovieRentalCanceledEvent event) {
		this.hireDaysAmmountByRental.remove(event.getRentalId());
	}

	public class MovieNotRentedException extends RuntimeException {

		private static final long serialVersionUID = -194444633524613856L;

	}

	public class UnavailableCopiesForRentException extends RuntimeException {

		private static final long serialVersionUID = 2854276385696003476L;

	}
}
