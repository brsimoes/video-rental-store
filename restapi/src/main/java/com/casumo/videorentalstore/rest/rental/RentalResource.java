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
import com.casumo.videorentalstore.rental.core.application.dto.Return;
import com.casumo.videorentalstore.rental.core.domain.RentalStatus;

public class RentalResource extends ResourceSupport {

	private final UUID rentalId;
	private final UUID userId;
	private final RentalStatus status;
	private final LocalDate startDate;
	private final Resources<Resource<RentalItem>> rentalItems;
	private final Resources<Resource<Return>> returns;

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
		this.rentalItems = produceRentalItemResources(rental.getId(), rental.getRentalItems());
		this.returns = produceReturnItemResources(rental.getId(), rental.getReturns());
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
	
	public Resources<Resource<RentalItem>> getRentalItems() {
		return rentalItems;
	}

	public Resources<Resource<Return>> getReturns() {
		return returns;
	}

	private Resources<Resource<RentalItem>> produceRentalItemResources(UUID rentalId, Collection<RentalItem> items) {
		return new Resources<Resource<RentalItem>>(
				items.stream()
					 .map(this.rentalItemResourceAssembler::toResource)
					 .map( i -> {
				   		 	i.add(linkTo(methodOn(RentalsController.class).getRentedMovie(
				   		 													rentalId,
				   		 													i.getContent().getMovieId()))
				   		 		  .withSelfRel()); 
				   		 	return i;
				   	 }).collect(Collectors.toList()),
				linkTo(methodOn(RentalsController.class).getReturns(rentalId)).withSelfRel());
	}

	private Resources<Resource<Return>> produceReturnItemResources(UUID rentalId, Collection<Return> items) {
		return new Resources<Resource<Return>>(
				items.stream()
					 .map(this.returnItemResourceAssembler::toResource)
					 .map( i -> {
				   		 	i.add(linkTo(methodOn(RentalsController.class).getReturnedMovie(
				   		 													rentalId,
				   		 													i.getContent().getMovieId()))
				   		 		  .withSelfRel()); 
				   		 	return i;
				   	 }).collect(Collectors.toList()),
				linkTo(methodOn(RentalsController.class).getReturns(rentalId)).withSelfRel());
	}
}
