package com.casumo.videorentalstore.rental.core.domain.event;

import java.util.UUID;

import com.casumo.videorentalstore.enums.RentalStatus;

public class RentalCreatedEvent extends RentalEvent {
	private final UUID userId;
	private final RentalStatus status;

	public RentalCreatedEvent(UUID rentalId, UUID userId, RentalStatus status) {
		super(rentalId);
		this.userId = userId;
		this.status = status;
	}

	public UUID getUserId() {
		return userId;
	}

	public RentalStatus getStatus() {
		return status;
	}
}
