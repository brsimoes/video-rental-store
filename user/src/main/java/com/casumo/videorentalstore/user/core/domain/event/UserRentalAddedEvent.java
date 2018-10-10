package com.casumo.videorentalstore.user.core.domain.event;

import java.util.UUID;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class UserRentalAddedEvent extends UserEvent {
	private final UUID rentalId;

	public UserRentalAddedEvent(UUID userId, UUID rentalId) {
		super(userId);
		this.rentalId = rentalId;
	}

	@TargetAggregateIdentifier
	public UUID getRentalId() {
		return rentalId;
	}
}
