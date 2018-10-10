package com.casumo.videorentalstore.rental.core.domain.command;

import java.util.UUID;

import com.casumo.videorentalstore.rental.core.domain.RentalStatus;

public class ChangeRentalStatusCommand extends RentalCommand {

	private final RentalStatus newStatus;
	
	public ChangeRentalStatusCommand(UUID rentalId, RentalStatus newStatus) {
		super(rentalId);
		this.newStatus = newStatus;
	}

	public RentalStatus getNewStatus() {
		return this.newStatus;
	}
}
