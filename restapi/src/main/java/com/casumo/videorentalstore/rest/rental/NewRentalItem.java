package com.casumo.videorentalstore.rest.rental;

import java.util.UUID;

public final class NewRentalItem {

	private UUID movieId;
	private int numberOfDaysToHire;
	
	protected NewRentalItem () {
		
	}
	
	public NewRentalItem(UUID movieId, int numberOfDaysToHire) {
		this.movieId = movieId;
		this.numberOfDaysToHire = numberOfDaysToHire;
	}

	public UUID getMovieId() {
		return movieId;
	}

	public void setMovieId(UUID movieId) {
		this.movieId = movieId;
	}

	public int getNumberOfDaysToHire() {
		return numberOfDaysToHire;
	}

	public void setNumberOfDaysToHire (int numberOfDays) {
		this.numberOfDaysToHire = numberOfDays;
	}
}
