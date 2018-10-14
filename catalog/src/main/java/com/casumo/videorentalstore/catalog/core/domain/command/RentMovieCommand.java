package com.casumo.videorentalstore.catalog.core.domain.command;

import java.util.UUID;

public class RentMovieCommand extends MovieCommand {

	private final UUID rentalId;
	private final int numberOfDaysToHire;

	public RentMovieCommand(UUID movieId, UUID rentalId, int numberOfDaysToHire) {
		super(movieId);
		this.rentalId = rentalId;
		this.numberOfDaysToHire = numberOfDaysToHire;
	}
	
	public UUID getRentalId() {
		return rentalId;
	}

	public int getNumberOfDaysToHire() {
		return numberOfDaysToHire;
	}
}
