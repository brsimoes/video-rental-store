package com.casumo.videorentalstore.rental.core.domain.command;

import java.util.UUID;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public abstract class RentalCommand {

	private final UUID rentalId;

	public RentalCommand(UUID rentalId) {
		this.rentalId = rentalId;
	}

	@TargetAggregateIdentifier
	public UUID getRentalId() {
		return rentalId;
	}
}
