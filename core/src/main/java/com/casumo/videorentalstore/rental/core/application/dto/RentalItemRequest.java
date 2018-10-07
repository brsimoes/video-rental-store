package com.casumo.videorentalstore.rental.core.application.dto;

import java.util.UUID;

public final class RentalItemRequest {

	private UUID movieId;
	private int hireDaysDuration;
	
	protected RentalItemRequest () {
		
	}
	
	public RentalItemRequest(UUID movieId, int hireDaysDuration) {
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
