package com.casumo.videorentalstore.rental.core.persistence;

import java.util.UUID;

import javax.persistence.Embeddable;

@Embeddable
public final class RentalItemDetails {

	private UUID movieId;
	private int hireDaysDuration;
	private double chargeAmmount;
	
	protected RentalItemDetails () {
		
	}
	
	public RentalItemDetails(UUID movieId, int hireDaysDuration, double chargeAmmount) {
		this.movieId = movieId;
		this.hireDaysDuration = hireDaysDuration;
		this.chargeAmmount = chargeAmmount;
	}

	public UUID getMovieId() {
		return movieId;
	}

	public void setMovieId(UUID movieId) {
		this.movieId = movieId;
	}

	public int getRentlDaysDuration() {
		return hireDaysDuration;
	}

	public void setRentlDaysDuration (int numberOfDays) {
		this.hireDaysDuration = numberOfDays;
	}

	public double getChargeAmmount() {
		return chargeAmmount;
	}

	public void setChargeAmmount(double chargeAmmount) {
		this.chargeAmmount = chargeAmmount;
	}
}
