package com.casumo.videorentalstore.user.core.domain.command;

import java.util.UUID;

public class AddUserRentalCommand extends UserCommand {
	private final UUID rentalId;

	public AddUserRentalCommand(UUID userId, UUID rentalId) {
		super(userId);
		this.rentalId = rentalId;
	}

	public UUID getRentalId() {
		return rentalId;
	}
}
