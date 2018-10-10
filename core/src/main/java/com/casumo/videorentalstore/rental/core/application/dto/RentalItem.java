package com.casumo.videorentalstore.rental.core.application.dto;

import java.util.UUID;

public final class RentalItem {

	private final UUID movieId;
	private final String movieName;
	private final int hireDaysDuration;
	private final double chargeAmmount;
	
	public RentalItem(UUID movieId, int hireDaysDuration) {
		this.movieId = movieId;
		this.hireDaysDuration = hireDaysDuration;
		this.chargeAmmount = 0;
		this.movieName = "";
	}
	
	public RentalItem(UUID movieId, int hireDaysDuration, double chargeAmmount, String movieName) {
		this.movieId = movieId;
		this.hireDaysDuration = hireDaysDuration;
		this.chargeAmmount = chargeAmmount;
		this.movieName = movieName;
	}

	public UUID getMovieId() {
		return movieId;
	}

	public int getHireDaysDuration() {
		return hireDaysDuration;
	}

	public double getChargeAmmount() {
		return chargeAmmount;
	}

	public String getMovieName() {
		return movieName;
	}
}
