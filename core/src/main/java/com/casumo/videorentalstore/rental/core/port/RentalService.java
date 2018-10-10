package com.casumo.videorentalstore.rental.core.port;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.casumo.videorentalstore.rental.core.application.dto.Rental;
import com.casumo.videorentalstore.rental.core.application.dto.RentalItem;
import com.casumo.videorentalstore.rental.core.application.dto.Return;

public interface RentalService {

	public void createRental (Rental rental);
	
	public void addMovieToRental(UUID rentalId, UUID movieId, int hireDays);
	
	public void returnMovie(UUID rentalId, UUID movieId, LocalDate returnDate);
	
	public Iterable<Rental> getAllRentals();

	public Optional<Rental>  getRentalById(UUID rentalId);
	
	public Optional<RentalItem>  getRentalMovie(UUID rentalId, UUID movieId);
	
	public Optional<Return>  getReturnMovie(UUID rentalId, UUID movieId);
}
