package com.casumo.videorentalstore.rental.core.domain.command;

import java.util.UUID;

import com.casumo.videorentalstore.enums.MovieType;

public class AddRentedMovieCommand extends RentalCommand{

	private final UUID movieId;
	private final int hireDays;
	private final double chargeAmmount;
	private final MovieType movieType;

	public AddRentedMovieCommand(UUID rentalId, UUID movieId, int hireDays, 
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
