package com.casumo.videorentalstore.rental.core.domain.event;

import java.util.UUID;

import com.casumo.videorentalstore.rental.core.domain.RentalStatus;

public class RentalStatusChangedEvent extends RentalEvent {

	private final RentalStatus newStatus;
	private final RentalStatus oldStatus;
	
	public RentalStatusChangedEvent(UUID rentalId, RentalStatus newStatus, RentalStatus oldStatus) {
		super(rentalId);
		this.newStatus = newStatus;
		this.oldStatus = oldStatus;
	}

	public RentalStatus getNewStatus() {
		return this.newStatus;
	}
	
	public RentalStatus getOldStatus() {
		return this.oldStatus;
	}
}
