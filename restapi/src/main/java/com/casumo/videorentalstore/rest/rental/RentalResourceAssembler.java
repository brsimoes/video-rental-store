package com.casumo.videorentalstore.rest.rental;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.casumo.videorentalstore.rental.core.application.dto.Rental;

@Component
public class RentalResourceAssembler implements ResourceAssembler<Rental, Resource<RentalResource>> {

	@Autowired
	RentalItemResourceAssembler rentalItemResourceAssembler;
	@Autowired
	ReturnItemResourceAssembler returnItemResourceAssembler;
	
	@Override
	public Resource<RentalResource> toResource(Rental rental) {

		Resource<RentalResource> rentalResouce = new Resource<>(
				new RentalResource(rental, rentalItemResourceAssembler, returnItemResourceAssembler),
				linkTo(methodOn(RentalsController.class).getAllRentals()).withRel("rentals"),
				linkTo(methodOn(RentalsController.class).getRentalById(rental.getId())).withSelfRel());

		return rentalResouce;
	}
}
