package com.casumo.videorentalstore.catalog.core.domain.command;

import java.time.LocalDate;
import java.util.UUID;

public class RentMovieCommand extends MovieCommand {

	private final UUID rentalId;
	private final int hireDays;
	private final LocalDate rentalDate;

	public RentMovieCommand(UUID movieId, UUID rentalId, int hireDays, LocalDate rentalDate) {
		super(movieId);
		this.rentalId = rentalId;
		this.hireDays = hireDays;
		this.rentalDate = rentalDate;
	}
	
	public UUID getRentalId() {
		return rentalId;
	}

	public int getHireDays() {
		return hireDays;
	}

	public LocalDate getRentalDate() {
		return rentalDate;
	}
}
