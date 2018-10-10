package com.casumo.videorentalstore.rental.core.persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.casumo.videorentalstore.rental.core.domain.RentalStatus;

@Entity
public final class RentalEntity {

	@Id
	private UUID id;
	private UUID userId;
	@ElementCollection
	private Collection<RentalItemDetail> rentedItems;
	@ElementCollection
	private Collection<ReturnItemDetail> returns;
	private RentalStatus status;
	private LocalDate startDate;

	protected RentalEntity() {
		this.rentedItems = Collections.emptyList();
		this.returns = Collections.emptyList();
		this.status = RentalStatus.OPEN;
	}
	
	public RentalEntity(UUID rentalId, UUID userId, LocalDate startDate) {
		this.id = rentalId;
		this.userId = userId;
		this.rentedItems = new ArrayList<>();
		this.returns = new ArrayList<>();
		this.status = RentalStatus.OPEN;
		this.startDate = startDate;
	}
			
	public RentalEntity(UUID rentalId, UUID userId, ArrayList<RentalItemDetail> rentedItems, 
			ArrayList<ReturnItemDetail> returns, RentalStatus rentalStatus) {
		this.id = rentalId;
		this.userId = userId;
		this.rentedItems = rentedItems;
		this.returns = returns;
		this.status = rentalStatus;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public Collection<RentalItemDetail> getRentedItems() {
		return rentedItems;
	}

	public void setRentedItems(Collection<RentalItemDetail> rentedItems) {
		this.rentedItems = rentedItems;
	}

	public void addRentalItem(UUID movieId, String movieName, int hireDays, double chargeAmmount) {
		this.rentedItems.add(new RentalItemDetail(movieId, movieName, hireDays, chargeAmmount));
	}
	
	public void addReturn (ReturnItemDetail returnedItem) {
		this.returns.add(returnedItem);
	}

	public Collection<ReturnItemDetail> getReturns() {
		return returns;
	}

	public void setReturns(Collection<ReturnItemDetail> returns) {
		this.returns = returns;
	}

	public RentalStatus getStatus() {
		return status;
	}

	public void setStatus(RentalStatus status) {
		this.status = status;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
}
