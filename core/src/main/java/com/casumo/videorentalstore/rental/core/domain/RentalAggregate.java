package com.casumo.videorentalstore.rental.core.domain;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.casumo.videorentalstore.enums.MovieType;
import com.casumo.videorentalstore.enums.RentalStatus;
import com.casumo.videorentalstore.rental.core.domain.command.AddRentedMovieCommand;
import com.casumo.videorentalstore.rental.core.domain.command.ChangeRentalStatusCommand;
import com.casumo.videorentalstore.rental.core.domain.command.CreateRentalCommand;
import com.casumo.videorentalstore.rental.core.domain.command.ReturnRentedMovieCommand;
import com.casumo.videorentalstore.rental.core.domain.event.RentalCreatedEvent;
import com.casumo.videorentalstore.rental.core.domain.event.RentedMovieReturnedEvent;
import com.casumo.videorentalstore.rental.core.domain.event.RentalStatusChangedEvent;
import com.casumo.videorentalstore.rental.core.domain.event.RentedMovieAddedEvent;

@Aggregate
public class RentalAggregate {

	@AggregateIdentifier
	private UUID rentalId;
	private Map<UUID,RentalItem> moviesRented;
	private Map<UUID,ReturnEntry> returns;
	private RentalStatus rentalStatus;

	protected RentalAggregate() {

	}

	@CommandHandler
	public RentalAggregate(CreateRentalCommand command) {
		apply(new RentalCreatedEvent(command.getRentalId(), command.getUserId(), RentalStatus.OPEN));
	}

	@CommandHandler
	public void handle(AddRentedMovieCommand command) {
		apply(new RentedMovieAddedEvent(command.getRentalId(), command.getMovieId(), command.getHireDays(),
				command.getChargeAmmount(), command.getMovieType()));
	}

	@CommandHandler
	public void handle(ReturnRentedMovieCommand command) {
		apply(new RentedMovieReturnedEvent(command.getRentalId(), command.getMovieId(), command.getSurchargeAmmount(),
				command.getReturnDate()));
	}
	
	@CommandHandler
	public void handle(ChangeRentalStatusCommand command) {
		apply(new RentalStatusChangedEvent(command.getRentalId(), command.getNewStatus(), this.rentalStatus));
	}

	@EventSourcingHandler
	public void on(RentalCreatedEvent event) {
		this.rentalId = event.getRentalId();
		this.moviesRented = new HashMap<>();
		this.returns = new HashMap<>();
		this.rentalStatus = event.getStatus();
	}

	@EventSourcingHandler
	public void on(RentedMovieAddedEvent event) {
		RentalItem newItem = new RentalItem(event.getMovieId(), event.getHireDays(), event.getMovieType(),
				event.getChargeAmmount());
		
		this.moviesRented.put(newItem.movieId, newItem);
	}

	@EventSourcingHandler
	public void on(RentedMovieReturnedEvent event) {
		ReturnEntry newReturn = new ReturnEntry(event.getMovieId(), event.getSurchargeAmmount(), event.getReturnDate());

		this.returns.put(newReturn.movieId, newReturn);
	}
	
	@EventSourcingHandler
	public void on(RentalStatusChangedEvent event) {
		this.rentalStatus = event.getNewStatus();
	}

	private class RentalItem {
		final UUID movieId;
		final int hireDays;
		final MovieType movieType;
		final double chargeAmmount;

		public RentalItem(UUID movieId, int hireDays, MovieType movieType, double chargeAmmount) {
			this.movieId = movieId;
			this.hireDays = hireDays;
			this.movieType = movieType;
			this.chargeAmmount = chargeAmmount;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			long temp;
			temp = Double.doubleToLongBits(chargeAmmount);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result + hireDays;
			result = prime * result + ((movieId == null) ? 0 : movieId.hashCode());
			result = prime * result + ((movieType == null) ? 0 : movieType.hashCode());
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
			if (!(obj instanceof RentalItem)) {
				return false;
			}
			RentalItem other = (RentalItem) obj;
			if (Double.doubleToLongBits(chargeAmmount) != Double.doubleToLongBits(other.chargeAmmount)) {
				return false;
			}
			if (hireDays != other.hireDays) {
				return false;
			}
			if (movieId == null) {
				if (other.movieId != null) {
					return false;
				}
			} else if (!movieId.equals(other.movieId)) {
				return false;
			}
			if (movieType != other.movieType) {
				return false;
			}
			return true;
		}
	}

	private class ReturnEntry {
		final UUID movieId;
		final double chargeAmmount;
		final LocalDate returnDate;
		
		public ReturnEntry(UUID movieId, double chargeAmmount, LocalDate returnDate) {
			super();
			this.movieId = movieId;
			this.chargeAmmount = chargeAmmount;
			this.returnDate = returnDate;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			long temp;
			temp = Double.doubleToLongBits(chargeAmmount);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result + ((movieId == null) ? 0 : movieId.hashCode());
			result = prime * result + ((returnDate == null) ? 0 : returnDate.hashCode());
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
			if (!(obj instanceof ReturnEntry)) {
				return false;
			}
			ReturnEntry other = (ReturnEntry) obj;
			if (Double.doubleToLongBits(chargeAmmount) != Double.doubleToLongBits(other.chargeAmmount)) {
				return false;
			}
			if (movieId == null) {
				if (other.movieId != null) {
					return false;
				}
			} else if (!movieId.equals(other.movieId)) {
				return false;
			}
			if (returnDate == null) {
				if (other.returnDate != null) {
					return false;
				}
			} else if (!returnDate.equals(other.returnDate)) {
				return false;
			}
			return true;
		}
	}
}
