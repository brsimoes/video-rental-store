package com.casumo.videorentalstore.rental.core.domain.command;

import java.util.UUID;

import com.casumo.videorentalstore.catalog.core.domain.MovieType;

public class AddRentedMovieCommand extends RentalCommand{

	private final UUID movieId;
	private final int hireDays;
	private final double chargeAmmount;
	private final MovieType movieType;
	private final String movieName;

	public AddRentedMovieCommand(
				UUID rentalId, 
				UUID movieId, 
				String movieName, 
				MovieType movieType, 
				int hireDays, 
				double chargeAmmount) {
		
		super(rentalId);
		this.movieId = movieId;
		this.hireDays = hireDays;
		this.chargeAmmount = chargeAmmount;
		this.movieType = movieType;
		this.movieName = movieName;
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

	public String getMovieName() {
		return movieName;
	}
}
