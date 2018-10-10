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
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.casumo.videorentalstore.rental.core.application.dto.Rental;
import com.casumo.videorentalstore.rental.core.application.dto.RentalItem;
import com.casumo.videorentalstore.rental.core.application.dto.Return;
import com.casumo.videorentalstore.rental.core.port.RentalService;
import com.google.common.collect.Iterables;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/rentals", produces = MediaTypes.HAL_JSON_VALUE)
@Api(value="rentals", tags="Rentals", description="Operations to manage rentals")
public class RentalsController {

	private RentalService rentalService;
	private RentalResourceAssembler rentalResourceAssembler;
	private RentalItemResourceAssembler rentalItemResourceAssembler;
	private ReturnItemResourceAssembler returnItemResourceAssembler;
	private Clock clock;

	public RentalsController(
				RentalService rentalService,
				Clock clock, 
				RentalResourceAssembler rentalResourceAssembler,
				RentalItemResourceAssembler rentalItemResourceAssembler,
				ReturnItemResourceAssembler returnItemResourceAssembler) {
		
		this.rentalService = rentalService;
		this.clock = clock;
		this.rentalResourceAssembler = rentalResourceAssembler;
		this.rentalItemResourceAssembler = rentalItemResourceAssembler;
		this.returnItemResourceAssembler = returnItemResourceAssembler;
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

	@ApiOperation(value="Search a rental by Id.", response = ResponseEntity.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
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
	public ResponseEntity<Resources<Resource<Return>>> getReturns(@PathVariable UUID rentalId) {

		return this.rentalService.getRentalById(rentalId)
			   	 .map(r -> new Resources<Resource<Return>>(
			   			 		Iterables.transform(r.getReturns(),returnItemResourceAssembler::toResource),
			   			 		linkTo(methodOn(RentalsController.class).getRentalById(rentalId)).withSelfRel()))
			   	 .map(ResponseEntity::ok)
			   	 .orElse(notFound().build());
	}

	@ApiOperation(value="Rent a movie.", response = ResponseEntity.class)
	@PostMapping(value = "/{rentalId}/movies")
	public ResponseEntity<Resource<RentalItem>> rentMovie(
				@PathVariable UUID rentalId, 
				@RequestBody @Valid NewRentalItem newRentalItem) throws URISyntaxException {

		this.rentalService.addMovieToRental(rentalId, newRentalItem.getMovieId(), newRentalItem.getRentlDaysDuration());
		
		Optional<RentalItem> createdRentalItem = this.rentalService.getRentalMovie(rentalId, newRentalItem.getMovieId());

		Resource<RentalItem> rentalResource = rentalItemResourceAssembler.toResource(createdRentalItem.get());

		return created(new URI(rentalResource.getId().expand().getHref()))
				.body(rentalResource);
	}
	
	@ApiOperation(value="Return a movie.", response = ResponseEntity.class)
	@PostMapping(value = "/{rentalId}/returns")
	public ResponseEntity<Resource<Return>> returnMovie(
				@PathVariable UUID rentalId, 
				@RequestBody @Valid UUID movieId) throws URISyntaxException {

		this.rentalService.returnMovie(rentalId, movieId, LocalDate.now(clock));
		
		Optional<Return> returnItem = this.rentalService.getReturnMovie(rentalId, movieId);

		Resource<Return> rentalResource = returnItemResourceAssembler.toResource(returnItem.get());

		return created(new URI(rentalResource.getId().expand().getHref()))
				.body(rentalResource);
	}
	
	@ApiOperation(value="Get details on rented movie.", response = ResponseEntity.class)
	@PostMapping(value = "/{rentalId}/movies/{movieId}")
	public ResponseEntity<Resource<RentalItem>> getRentedMovie(
				@PathVariable UUID rentalId, 
				@RequestBody @Valid UUID movieId) {

		return this.rentalService.getRentalMovie(rentalId, movieId)
			   	 .map(rentalItemResourceAssembler::toResource)
			   	 .map( i -> {
			   		 	i.add(linkTo(methodOn(RentalsController.class).getRentedMovie(rentalId,movieId)).withSelfRel()); 
			   		 	return i;})
			   	 .map(ResponseEntity::ok)
			   	 .orElse(notFound().build());
	}
	
	@ApiOperation(value="Get details on returned movie.", response = ResponseEntity.class)
	@PostMapping(value = "/{rentalId}/returns/{movieId}")
	public ResponseEntity<Resource<Return>> getReturnedMovie(
				@PathVariable UUID rentalId, 
				@RequestBody @Valid UUID movieId) {

		return this.rentalService.getReturnMovie(rentalId, movieId)
			   	 .map(returnItemResourceAssembler::toResource)
			   	 .map( i -> {
		   		 	i.add(linkTo(methodOn(RentalsController.class).getReturnedMovie(rentalId,movieId)).withSelfRel()); 
		   		 	return i;})
			   	 .map(ResponseEntity::ok)
			   	 .orElse(notFound().build());
	}
}
