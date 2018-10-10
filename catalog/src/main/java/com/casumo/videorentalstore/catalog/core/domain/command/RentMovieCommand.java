package com.casumo.videorentalstore.catalog.core.domain.command;

import java.util.UUID;

public class RentMovieCommand extends MovieCommand {

	private final UUID rentalId;
	private final int hireDays;

	public RentMovieCommand(UUID movieId, UUID rentalId, int hireDays) {
		super(movieId);
		this.rentalId = rentalId;
		this.hireDays = hireDays;
	}
	
	public UUID getRentalId() {
		return rentalId;
	}

	public int getHireDays() {
		return hireDays;
	}
}
