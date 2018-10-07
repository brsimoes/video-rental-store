package com.casumo.videorentalstore.rental.core.port;

import java.util.Optional;
import java.util.UUID;

import com.casumo.videorentalstore.rental.core.application.dto.RentalItemRequest;
import com.casumo.videorentalstore.rental.core.persistence.RentalEntity;

public interface RentalService {

	public UUID createRental (UUID userId);
	
	public void addMovieToRental(UUID rentalId, RentalItemRequest moviesAndDaysToRent);
	
	public void returnMovie(UUID rentalId, UUID movieId);
	
	public Iterable<RentalEntity> getAllRentals();

	public Optional<RentalEntity>  getRentalById(UUID rentalId);
}
