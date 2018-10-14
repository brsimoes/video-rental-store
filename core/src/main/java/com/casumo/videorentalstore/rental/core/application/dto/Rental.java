package com.casumo.videorentalstore.rental.core.application.dto;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import com.casumo.videorentalstore.rental.core.domain.RentalStatus;

public final class Rental {

	private final UUID id;
	private final UUID userId;
	private final Collection<RentalItem> rentalItems;
	private final Collection<ReturnedItem> returns;
	private final RentalStatus status;
	private final LocalDate startDate;
			
	public Rental(UUID rentalId, UUID userId, LocalDate startDate) {
		this.id = rentalId;
		this.userId = userId;
		this.rentalItems = Collections.emptyList();
		this.returns = Collections.emptyList();
		this.status = RentalStatus.OPEN;
		this.startDate = startDate;
	}
	
	public Rental(UUID rentalId, UUID userId, Collection<RentalItem> rentalItems, 
			Collection<ReturnedItem> returns, RentalStatus rentalStatus, LocalDate startDate) {
		this.id = rentalId;
		this.userId = userId;
		this.rentalItems = rentalItems;
		this.returns = returns;
		this.status = rentalStatus;
		this.startDate = startDate;
	}
	
	public UUID getId() {
		return id;
	}
	
	public UUID getUserId() {
		return userId;
	}
	
	public void addReturn (ReturnedItem returnedItem) {
		this.returns.add(returnedItem);
	}

	public Collection<ReturnedItem> getReturnedItems() {
		return returns;
	}

	public RentalStatus getStatus() {
		return status;
	}

	public Collection<RentalItem> getRentalItems() {
		return rentalItems;
	}

	public LocalDate getStartDate() {
		return startDate;
	}
}
