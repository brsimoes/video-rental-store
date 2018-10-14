package com.casumo.videorentalstore.rest.rental.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.junit.Test;

import com.casumo.videorentalstore.rental.core.application.dto.Rental;
import com.casumo.videorentalstore.rental.core.application.dto.RentalItem;
import com.casumo.videorentalstore.rental.core.application.dto.ReturnedItem;
import com.casumo.videorentalstore.rental.core.domain.RentalStatus;

public class RentalJsonConverterTests {

	@Test
	public void givenARental_toJson_fromJson_returnsEqualRental () {
		RentalJsonConverter converter = new RentalJsonConverter();
		
		Collection<RentalItem> rentalItems = new ArrayList<>();
		Collection<ReturnedItem> returnedItems = new ArrayList<>();
		
		rentalItems.add(new RentalItem(UUID.randomUUID(), 1, 1, "movie1"));
		rentalItems.add(new RentalItem(UUID.randomUUID(), 2, 2, "movie2"));
		
		returnedItems.add(new ReturnedItem (UUID.randomUUID(), LocalDate.now(), 1, "movie1"));
		returnedItems.add(new ReturnedItem (UUID.randomUUID(), LocalDate.now(), 2, "movie2"));
		
		Rental rental = new Rental(
							UUID.randomUUID(),
							UUID.randomUUID(),
							rentalItems,
							returnedItems,
							RentalStatus.OPEN,
							LocalDate.now());
		
		Rental mirrorRental = converter.fromJson(converter.toJson(rental));
		
		assertNotNull(mirrorRental);
		assertTrue(mirrorRental.getId().equals(rental.getId()));
		assertTrue(mirrorRental.getStartDate().equals(rental.getStartDate()));
		assertTrue(mirrorRental.getStatus().equals(rental.getStatus()));
		assertTrue(mirrorRental.getUserId().equals(rental.getUserId()));
		
		assertNotNull(mirrorRental.getRentalItems());
		assertNotNull(mirrorRental.getReturnedItems());
	}
}
