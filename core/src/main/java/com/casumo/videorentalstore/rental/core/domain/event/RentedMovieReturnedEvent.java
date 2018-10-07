package com.casumo.videorentalstore.rental.core.domain.event;

import java.time.LocalDate;
import java.util.UUID;

public class RentedMovieReturnedEvent extends RentalEvent {
	
	private final UUID movieId;
	private final double surchargeAmmount;
	private final LocalDate returnDate;

	public RentedMovieReturnedEvent(UUID rentalId, UUID movieId, double surchargeAmmount, LocalDate returnDate) {
		super(rentalId);
		this.movieId = movieId;
		this.surchargeAmmount = surchargeAmmount;
		this.returnDate = returnDate;
	}

	public UUID getMovieId() {
		return movieId;
	}

	public double getSurchargeAmmount() {
		return surchargeAmmount;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}
}
