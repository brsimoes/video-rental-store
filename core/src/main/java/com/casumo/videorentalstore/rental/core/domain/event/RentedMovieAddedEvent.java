package com.casumo.videorentalstore.rental.core.domain.event;

import java.util.UUID;

import com.casumo.videorentalstore.enums.MovieType;

public class RentedMovieAddedEvent extends RentalEvent {

	private final UUID movieId;
	private final int hireDays;
	private final double chargeAmmount;
	private final MovieType movieType;

	public RentedMovieAddedEvent(UUID rentalId, UUID movieId, int hireDays, 
				double chargeAmmount, MovieType movieType) {
		super(rentalId);
		this.movieId = movieId;
		this.hireDays = hireDays;
		this.chargeAmmount = chargeAmmount;
		this.movieType = movieType;
	}

	public UUID getMovieId() {
		return movieId;
	}

	public int getHireDays() {
		return hireDays;
	}

	public double getChargeAmmount() {
		return chargeAmmount;
	}

	public MovieType getMovieType() {
		return movieType;
	}
}
