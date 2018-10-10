package com.casumo.videorentalstore.rental.core.persistence;

import java.util.UUID;

import javax.persistence.Embeddable;

@Embeddable
public final class RentalItemDetail {

	private UUID movieId;
	private String movieName;
	private int hireDaysDuration;
	private double chargeAmmount;
	
	protected RentalItemDetail () {
		
	}
	
	public RentalItemDetail(UUID movieId, String movieName, int hireDaysDuration, double chargeAmmount) {
		this.movieId = movieId;
		this.hireDaysDuration = hireDaysDuration;
		this.chargeAmmount = chargeAmmount;
		this.movieName = movieName;
	}

	public UUID getMovieId() {
		return movieId;
	}

	public void setMovieId(UUID movieId) {
		this.movieId = movieId;
	}

	public int getHireDaysDuration() {
		return hireDaysDuration;
	}

	public void setHireDaysDuration (int numberOfDays) {
		this.hireDaysDuration = numberOfDays;
	}

	public double getChargeAmmount() {
		return chargeAmmount;
	}

	public void setChargeAmmount(double chargeAmmount) {
		this.chargeAmmount = chargeAmmount;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
}
