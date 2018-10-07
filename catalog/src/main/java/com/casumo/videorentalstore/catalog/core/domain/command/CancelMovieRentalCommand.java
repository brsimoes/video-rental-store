package com.casumo.videorentalstore.catalog.core.domain.command;

import java.util.UUID;

public class CancelMovieRentalCommand extends MovieCommand {

	private final UUID rentalId;

	public CancelMovieRentalCommand(UUID movieId, UUID rentalId) {
		super(movieId);
		this.rentalId = rentalId;
	}
	
	public UUID getRentalId() {
		return rentalId;
	}
}
