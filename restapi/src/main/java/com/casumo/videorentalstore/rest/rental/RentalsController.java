package com.casumo.videorentalstore.rest.rental;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonPatch;
import javax.validation.Valid;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.casumo.videorentalstore.rental.core.application.dto.Rental;
import com.casumo.videorentalstore.rental.core.application.dto.RentalItem;
import com.casumo.videorentalstore.rental.core.application.dto.ReturnedItem;
import com.casumo.videorentalstore.rental.core.domain.RentalAggregate.InvalidStatusException;
import com.casumo.videorentalstore.rental.core.port.RentalService;
import com.casumo.videorentalstore.rest.rental.util.RentalJsonConverter;
import com.google.common.collect.Iterables;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/v1/rentals", produces = MediaTypes.HAL_JSON_VALUE)
@Api(value="rentals", tags="Rentals", description="Operations to manage rentals")
public class RentalsController {

	private RentalService rentalService;
	private RentalResourceAssembler rentalResourceAssembler;
	private RentalItemResourceAssembler rentalItemResourceAssembler;
	private ReturnItemResourceAssembler returnItemResourceAssembler;
	private Clock clock;
	private RentalJsonConverter jsonConverter;

	public RentalsController(
				RentalService rentalService,
				Clock clock, 
				RentalResourceAssembler rentalResourceAssembler,
				RentalItemResourceAssembler rentalItemResourceAssembler,
				ReturnItemResourceAssembler returnItemResourceAssembler,
				RentalJsonConverter jsonConverter) {
		
		this.rentalService = rentalService;
		this.clock = clock;
		this.rentalResourceAssembler = rentalResourceAssembler;
		this.rentalItemResourceAssembler = rentalItemResourceAssembler;
		this.returnItemResourceAssembler = returnItemResourceAssembler;
		this.jsonConverter = jsonConverter;
	}
	
	@ApiOperation(value = "Create a new rental session.", response = ResponseEntity.class)
	@PostMapping
	public ResponseEntity<Resource<RentalResource>> createRental(@RequestBody @Valid UUID userId) 
			throws URISyntaxException {

		Rental rental = new Rental (UUID.randomUUID(), userId, LocalDate.now(clock));
		
		this.rentalService.createRental(rental);

		Resource<RentalResource> rentalResource = rentalResourceAssembler.toResource(rental);

		return created(new URI(rentalResource.getId().expand().getHref()))
				.body(rentalResource);
	}
	
	@ApiOperation(value="View a list of all rentals.", response = ResponseEntity.class)
	@GetMapping
	public ResponseEntity<Resources<Resource<RentalResource>>> getAllRentals() {
		return ok(new Resources<Resource<RentalResource>>(
						Iterables.transform(this.rentalService.getAllRentals(), rentalResourceAssembler::toResource),
						linkTo(methodOn(RentalsController.class).getAllRentals()).withSelfRel()));
	}

	@ApiOperation(
			value = "Update rental properties.", 
			notes = "Currently only rental status is allowed to change.",
			response = ResponseEntity.class)
	@PatchMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Resource<RentalResource>> updateRentalStatus(@PathVariable @Valid UUID id,
			@RequestBody Map<String, Object>[] patchOperations) throws InvalidStatusException {

		final JsonPatch patch = toJsonPatch(patchOperations);
		this.validateAllowedPatchOperations(patchOperations);

		final Optional<Rental> rental = this.rentalService.getRentalById(id);

		if (rental.isPresent()) {
			final JsonObject result = patch.apply(jsonConverter.toJson(rental.get()));

			Rental patchedRental = jsonConverter.fromJson(result);

			this.rentalService.updateRentalStatus(patchedRental.getId(), patchedRental.getStatus());

			return ok(rentalResourceAssembler.toResource(patchedRental));

		} else {
			return notFound().build();
		}
	}

	@ApiOperation(value="Search a rental by Id.", response = ResponseEntity.class)
	@GetMapping("/{id}")
	public ResponseEntity<Resource<RentalResource>> getRentalById(@PathVariable UUID id) {
		return this.rentalService.getRentalById(id)
							   	 .map(rentalResourceAssembler::toResource)
							   	 .map(ResponseEntity::ok)
							   	 .orElse(notFound().build());
	}

	@ApiOperation(value="Get rented movies of a rental session.", response = ResponseEntity.class)
	@GetMapping("/{rentalId}/movies")
	public ResponseEntity<Resources<Resource<RentalItem>>> getRentedMovies(@PathVariable UUID rentalId) {

		return this.rentalService.getRentalById(rentalId)
			   	 .map(r -> new Resources<Resource<RentalItem>>(
			   			 		Iterables.transform(r.getRentalItems(),rentalItemResourceAssembler::toResource),
			   			 		linkTo(methodOn(RentalsController.class).getRentalById(rentalId)).withSelfRel()))
			   	 .map(ResponseEntity::ok)
			   	 .orElse(notFound().build());
	}
	
	@ApiOperation(value="Get returned movies of a rental session.", response = ResponseEntity.class)
	@GetMapping("/{rentalId}/returns")
	public ResponseEntity<Resources<Resource<ReturnedItem>>> getReturns(@PathVariable UUID rentalId) {

		return this.rentalService.getRentalById(rentalId)
			   	 .map(r -> new Resources<Resource<ReturnedItem>>(
			   			 		Iterables.transform(r.getReturnedItems(),returnItemResourceAssembler::toResource),
			   			 		linkTo(methodOn(RentalsController.class).getRentalById(rentalId)).withSelfRel()))
			   	 .map(ResponseEntity::ok)
			   	 .orElse(notFound().build());
	}

	@ApiOperation(value="Rent a movie.", response = ResponseEntity.class)
	@PostMapping(value = "/{rentalId}/movies")
	public ResponseEntity<Resource<RentalItem>> rentMovie(
				@PathVariable UUID rentalId, 
				@RequestBody @Valid NewRentalItem newRentalItem) throws URISyntaxException {

		this.rentalService.addMovieToRental(rentalId, newRentalItem.getMovieId(), newRentalItem.getNumberOfDaysToHire());
		
		Optional<RentalItem> createdRentalItem = this.rentalService.getRentalMovie(rentalId, newRentalItem.getMovieId());

		Resource<RentalItem> rentalResource = rentalItemResourceAssembler.toResource(createdRentalItem.get());

		return created(new URI(rentalResource.getId().expand().getHref()))
				.body(rentalResource);
	}
	
	@ApiOperation(value="Return a movie.", response = ResponseEntity.class)
	@PostMapping(value = "/{rentalId}/returns")
	public ResponseEntity<Resource<ReturnedItem>> returnMovie(
				@PathVariable UUID rentalId, 
				@RequestBody UUID movieId) throws URISyntaxException {

		this.rentalService.returnMovie(rentalId, movieId, LocalDate.now(clock));
		
		Optional<ReturnedItem> returnItem = this.rentalService.getReturnMovie(rentalId, movieId);

		Resource<ReturnedItem> rentalResource = returnItemResourceAssembler.toResource(returnItem.get());

		return created(new URI(rentalResource.getId().expand().getHref()))
				.body(rentalResource);
	}
	
	@ApiOperation(value="Gets details on rented movie.", response = ResponseEntity.class)
	@GetMapping(value = "/{rentalId}/movies/{movieId}")
	public ResponseEntity<Resource<RentalItem>> getRentedMovie(
				@PathVariable UUID rentalId, 
				@PathVariable UUID movieId) {

		return this.rentalService.getRentalMovie(rentalId, movieId)
			   	 .map(rentalItemResourceAssembler::toResource)
			   	 .map( i -> {
			   		 	i.add(linkTo(methodOn(RentalsController.class).getRentedMovie(rentalId,movieId)).withSelfRel()); 
			   		 	return i;})
			   	 .map(ResponseEntity::ok)
			   	 .orElse(notFound().build());
	}
	
	@ApiOperation(value="Gets details on returned movie.", response = ResponseEntity.class)
	@GetMapping(value = "/{rentalId}/returns/{movieId}")
	public ResponseEntity<Resource<ReturnedItem>> getReturnedMovie(
				@PathVariable UUID rentalId, 
				@PathVariable UUID movieId) {

		return this.rentalService.getReturnMovie(rentalId, movieId)
			   	 .map(returnItemResourceAssembler::toResource)
			   	 .map( i -> {
		   		 	i.add(linkTo(methodOn(RentalsController.class).getReturnedMovie(rentalId,movieId)).withSelfRel()); 
		   		 	return i;})
			   	 .map(ResponseEntity::ok)
			   	 .orElse(notFound().build());
	}

	private JsonPatch toJsonPatch(Map<String, Object>[] patchOperations) {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		
		for (Map<String, Object> operation : patchOperations) {
			builder.add(Json.createObjectBuilder(operation).build());
		}
		
		return Json.createPatch(builder.build());
	}
	
	private void validateAllowedPatchOperations(Map<String, Object>[] patchOperations) {
		for (Map<String, Object> operation : patchOperations) {
			String path = (String) operation.get("path");
			String op = (String) operation.get("op");
			
			if (!path.equals("/rentalStatus"))
				throw new InvalidRentalPatch("Only replace rentalStatus is allowed to be performed.");
			
			if (!op.equals("/replace"))
				throw new InvalidRentalPatch("Only replace rentalStatus is allowed to be performed.");
		}
	}
}
