package com.casumo.videorentalstore.rental.core.domain.event;

import java.time.LocalDate;
import java.util.UUID;

import com.casumo.videorentalstore.rental.core.domain.RentalStatus;

public class RentalCreatedEvent extends RentalEvent {
	private final UUID userId;
	private final RentalStatus status;
	private final LocalDate startDate;

	public RentalCreatedEvent(UUID rentalId, UUID userId, LocalDate startDate, RentalStatus status) {
		super(rentalId);
		this.userId = userId;
		this.status = status;
		this.startDate = startDate;
	}

	public UUID getUserId() {
		return userId;
	}

	public RentalStatus getStatus() {
		return status;
	}

	public LocalDate getStartDate() {
		return startDate;
	}
}
