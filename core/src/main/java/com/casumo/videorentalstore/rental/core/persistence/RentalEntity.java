package com.casumo.videorentalstore.rental.core.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.casumo.videorentalstore.enums.RentalStatus;

@Entity
public final class RentalEntity {

	@Id
	private UUID id;
	private UUID userId;
	@ElementCollection
	private Collection<RentalItemDetails> rentedItems;
	@ElementCollection
	private Collection<Return> returns;
	private RentalStatus status;

	protected RentalEntity() {
		this.rentedItems = Collections.emptyList();
		this.returns = Collections.emptyList();
		this.status = RentalStatus.OPEN;
	}
	
	public RentalEntity(UUID rentalId, UUID userId) {
		this.id = rentalId;
		this.userId = userId;
		this.rentedItems = new ArrayList<>();
		this.returns = new ArrayList<>();
		this.status = RentalStatus.OPEN;
	}
			
	public RentalEntity(UUID rentalId, UUID userId, ArrayList<RentalItemDetails> rentedItems, 
			ArrayList<Return> returns, RentalStatus rentalStatus) {
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

	public Collection<RentalItemDetails> getRentedItems() {
		return rentedItems;
	}

	public void addRentalItem(RentalItemDetails item) {
		this.rentedItems.add(item);
	}
	
	public void setMoviesRentalDuration(Collection<RentalItemDetails> moviesRentalDuration) {
		this.rentedItems = moviesRentalDuration;
	}
	
	public void addReturn (Return returnedItem) {
		this.returns.add(returnedItem);
	}

	public Collection<Return> getReturns() {
		return returns;
	}

	public void setReturns(Collection<Return> returns) {
		this.returns = returns;
	}

	public RentalStatus getStatus() {
		return status;
	}

	public void setStatus(RentalStatus status) {
		this.status = status;
	}

	public void addRentalItem(UUID movieId, int hireDays, double chargeAmmount) {
		this.rentedItems.add(new RentalItemDetails(movieId, hireDays, chargeAmmount));
	}
}
