package com.casumo.videorentalstore.catalog.core.domain.command;

import java.time.LocalDate;
import java.util.UUID;

public class ReturnMovieCommand extends MovieCommand {

	private final UUID rentalId;
	private final LocalDate returnDate;

	public ReturnMovieCommand(UUID movieId, UUID rentalId, LocalDate returnDate) {
		super(movieId);
		this.rentalId = rentalId;
		this.returnDate = returnDate;
	}

	public UUID getRentalId() {
		return rentalId;
	}
	
	public LocalDate getReturnDate(){
		return returnDate;
	}
}
