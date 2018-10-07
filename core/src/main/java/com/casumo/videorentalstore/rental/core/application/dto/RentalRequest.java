package com.casumo.videorentalstore.rental.core.application.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import com.casumo.videorentalstore.rental.core.persistence.RentalItemDetails;

public final class RentalRequest {

	private UUID userId;
	private Collection<RentalItemDetails> moviesAndDaysToRent;

	protected RentalRequest() {
		this.moviesAndDaysToRent = Collections.emptyList();
	}
			
	public RentalRequest(UUID userId, Collection<RentalItemDetails> moviesAndDaysToRent) {
		this.userId = userId;
		this.moviesAndDaysToRent = moviesAndDaysToRent;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public Collection<RentalItemDetails> getMoviesAndDaysToRent() {
		return moviesAndDaysToRent;
	}

	public void setMoviesAndDaysToRent(Collection<RentalItemDetails> moviesAndDaysToRent) {
		this.moviesAndDaysToRent = moviesAndDaysToRent;
	}
}
