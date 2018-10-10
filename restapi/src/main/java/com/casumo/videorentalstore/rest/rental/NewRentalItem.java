package com.casumo.videorentalstore.rest.rental;

import java.util.UUID;

public final class NewRentalItem {

	private UUID movieId;
	private int hireDaysDuration;
	
	protected NewRentalItem () {
		
	}
	
	public NewRentalItem(UUID movieId, int hireDaysDuration) {
		this.movieId = movieId;
		this.hireDaysDuration = hireDaysDuration;
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
}
