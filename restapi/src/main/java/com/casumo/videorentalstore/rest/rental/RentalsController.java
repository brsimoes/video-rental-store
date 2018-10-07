package com.casumo.videorentalstore.rest.rental;

import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.casumo.videorentalstore.rental.core.application.dto.RentalItemRequest;
import com.casumo.videorentalstore.rental.core.persistence.RentalEntity;
import com.casumo.videorentalstore.rental.core.port.RentalService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/rentals")
@Api(value="rentals", tags="Rentals", description="Operations to manage rentals")
public class RentalsController {

	@Autowired
	private RentalService rentalService;

	@SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity createRental(@RequestBody @Valid UUID userId, UriComponentsBuilder builder) {

		UUID newRentalId = this.rentalService.createRental(userId);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("/rentals/{rentalId}").buildAndExpand(newRentalId).toUri());

		return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.GET)
	public Iterable<RentalEntity> getAllRentals() {
		return this.rentalService.getAllRentals();
	}

	@RequestMapping(value = "/{rentalId}", method = RequestMethod.GET)
	public ResponseEntity<RentalEntity> getRentalById(@PathVariable UUID rentalId) {
		Optional<RentalEntity> rental = this.rentalService.getRentalById(rentalId);

		if (rental.isPresent()) {
			return new ResponseEntity<>(rental.get(), null, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
		}
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{rentalId}/movies", method = RequestMethod.GET)
	public ResponseEntity getRentalMovies(@PathVariable UUID rentalId, UriComponentsBuilder builder) {

		return new ResponseEntity<>(null, null, HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{rentalId}/movies", method = RequestMethod.POST)
	public ResponseEntity create(@PathVariable UUID rentalId, @RequestBody @Valid RentalItemRequest rentalItemRequest,
			UriComponentsBuilder builder) {

		this.rentalService.addMovieToRental(rentalId, rentalItemRequest);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("/rentals/{rentalId}/movies/{movieId}")
				.buildAndExpand(rentalId, rentalItemRequest.getMovieId()).toUri());

		return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{rentalId}/returns", method = RequestMethod.POST)
	public ResponseEntity create(@PathVariable UUID rentalId, @RequestBody @Valid UUID movieId,
			UriComponentsBuilder builder) {

		this.rentalService.returnMovie(rentalId, movieId);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("/rentals/{rentalId}/returns/{movieId}")
				.buildAndExpand(rentalId, movieId).toUri());

		return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
	}
}
