package com.casumo.videorentalstore.rental.core.domain.command;

import java.util.UUID;

public class CreateRentalCommand extends RentalCommand {

	private final UUID userId;

	public CreateRentalCommand(UUID rentalId, UUID userId) {
		super(rentalId);
		this.userId = userId;
	}

	public UUID getUserId() {
		return userId;
	}
}
