package com.casumo.videorentalstore.catalog.core.domain.event;

import java.util.UUID;

public class MovieRentalCanceledEvent extends MovieEvent {

	private final UUID rentalId;
	private final int availableCopiesToRent;
	
	public MovieRentalCanceledEvent(UUID movieId,UUID rentalId, int availableCopiesToRent) {
		super(movieId);
		this.rentalId = rentalId;
		this.availableCopiesToRent = availableCopiesToRent;
	}
	
	public UUID getRentalId() {
		return rentalId;
	}

	public int getAvailableCopiesToRent() {
		return availableCopiesToRent;
	}
}
