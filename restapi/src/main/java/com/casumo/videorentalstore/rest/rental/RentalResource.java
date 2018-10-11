package com.casumo.videorentalstore.rest.rental;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;

import com.casumo.videorentalstore.rental.core.application.dto.Rental;
import com.casumo.videorentalstore.rental.core.application.dto.RentalItem;
import com.casumo.videorentalstore.rental.core.application.dto.ReturnedItem;
import com.casumo.videorentalstore.rental.core.domain.RentalStatus;

public class RentalResource extends ResourceSupport {

	private final UUID rentalId;
	private final UUID userId;
	private final RentalStatus status;
	private final LocalDate startDate;
	private final Resources<Resource<RentalItem>> rentedItems;
	private final Resources<Resource<ReturnedItem>> returns;

	private final RentalItemResourceAssembler rentalItemResourceAssembler;
	private final ReturnItemResourceAssembler returnItemResourceAssembler;
	
	public RentalResource(
				Rental rental,
				RentalItemResourceAssembler rentalItemResourceAssembler,
				ReturnItemResourceAssembler returnItemResourceAssembler) {

		this.rentalItemResourceAssembler = rentalItemResourceAssembler;
		this.returnItemResourceAssembler = returnItemResourceAssembler;
		this.rentalId = rental.getId();
		this.userId = rental.getUserId();
		this.status = rental.getStatus();
		this.startDate = rental.getStartDate();
		this.rentedItems = produceRentalItemResources(rental.getId(), rental.getRentalItems());
		this.returns = produceReturnItemResources(rental.getId(), rental.getReturnedItems());
	}
	
	public UUID getRentalId() {
		return rentalId;
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
	
	public Resources<Resource<RentalItem>> getRentedItems() {
		return rentedItems;
	}

	public Resources<Resource<ReturnedItem>> getReturns() {
		return returns;
	}

	private Resources<Resource<RentalItem>> produceRentalItemResources(UUID rentalId, Collection<RentalItem> items) {
		Resources<Resource<RentalItem>> resource = 
			new Resources<Resource<RentalItem>>(
					items.stream()
						 .map(this.rentalItemResourceAssembler::toResource)
						 .map( i -> {
					   		 	i.add(linkTo(methodOn(RentalsController.class).getRentedMovie(
					   		 													rentalId,
					   		 													i.getContent().getMovieId()))
					   		 		  .withSelfRel()); 
					   		 	return i;
					   	 }).collect(Collectors.toList()));
					
		if (items.size() > 0) {
			resource.add(linkTo(methodOn(RentalsController.class).getRentedMovies(rentalId)).withSelfRel());
		}
		
		return resource;
	}	

	private Resources<Resource<ReturnedItem>> produceReturnItemResources(UUID rentalId, Collection<ReturnedItem> items) {
		Resources<Resource<ReturnedItem>> resource = 
			new Resources<Resource<ReturnedItem>>(
				items.stream()
					 .map(this.returnItemResourceAssembler::toResource)
					 .map( i -> {
				   		 	i.add(linkTo(methodOn(RentalsController.class).getReturnedMovie(
				   		 													rentalId,
				   		 													i.getContent().getMovieId()))
				   		 		  .withSelfRel()); 
				   		 	return i;
				   	 }).collect(Collectors.toList()));
		
		if (items.size() > 0) {
			resource.add(linkTo(methodOn(RentalsController.class).getReturns(rentalId)).withSelfRel());
		}
		
		return resource;
	}
}
