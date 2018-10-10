package com.casumo.videorentalstore.rental.core.domain.command;

import java.time.LocalDate;
import java.util.UUID;

public class CreateRentalCommand extends RentalCommand {

	private final UUID userId;
	private final LocalDate startDate;

	public CreateRentalCommand(UUID rentalId, UUID userId, LocalDate startDate) {
		super(rentalId);
		this.userId = userId;
		this.startDate = startDate;
	}

	public UUID getUserId() {
		return userId;
	}

	public LocalDate getStartDate() {
		return startDate;
	}
}
