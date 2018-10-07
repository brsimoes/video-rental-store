package com.casumo.videorentalstore.rental.core.domain.event;

import java.util.UUID;

public abstract class RentalEvent {
	private final UUID rentalId;

	public RentalEvent(UUID rentalId) {
		this.rentalId = rentalId;
	}

	public UUID getRentalId() {
		return rentalId;
	}
}
