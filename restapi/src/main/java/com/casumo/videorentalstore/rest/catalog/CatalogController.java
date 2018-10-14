package com.casumo.videorentalstore.rest.catalog;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import java.net.URI;
import java.net.URISyntaxException;
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
import org.springframework.web.bind.annotation.RestController;

import com.casumo.videorentalstore.catalog.core.application.dto.Movie;
import com.casumo.videorentalstore.catalog.core.port.CatalogService;
import com.google.common.collect.Iterables;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/v1/movies", produces = MediaTypes.HAL_JSON_VALUE)
@Api(value = "movieCatalog", tags = "Movie Catalog", description = "Operations to manage the movies Catalog")
public class CatalogController {

	private CatalogService catalogService;
	private MovieResourceAssembler resourceAssembler;

	public CatalogController(CatalogService catalogService, MovieResourceAssembler resourceAssembler) {
		this.catalogService = catalogService;
		this.resourceAssembler = resourceAssembler;
	}

	@ApiOperation(
		value = "Creates a new movie.",
		notes="The type of the movie can be \"NEW_RELEASE\", \"REGULAR_FILM\" or \"OLD_FILM\"",
		response = Movie.class)
	@PostMapping
	public ResponseEntity<Resource<Movie>> createMovie(@RequestBody @Valid NewMovie newMovie) 
			throws URISyntaxException {

		Movie movie = new Movie(
							UUID.randomUUID(), 
							newMovie.getName(), 
							newMovie.getType(), 
							newMovie.getAvailableCopiesForRental());

		this.catalogService.createMovie(movie);
		
		Resource<Movie> movieResource = resourceAssembler.toResource(movie);

		return created(new URI(movieResource.getId().expand().getHref()))
				.body(movieResource);
	}

	@ApiOperation(value = "Gets a list of available movies.", response = ResponseEntity.class)
	@GetMapping
	public ResponseEntity<Resources<Resource<Movie>>> getAllMovies() {
		return ok(new Resources<Resource<Movie>>(
						Iterables.transform(this.catalogService.getAllMovies(), resourceAssembler::toResource),
						linkTo(methodOn(CatalogController.class).getAllMovies()).withSelfRel()));
	}

	@ApiOperation(value = "Find a movie by Id.", response = ResponseEntity.class)
	@GetMapping(value = "/{id}")
	public ResponseEntity<Resource<Movie>> getMovieById(@PathVariable UUID id) {

		return this.catalogService.getMovieById(id)
								  .map(resourceAssembler::toResource)
								  .map(ResponseEntity::ok)
								  .orElse(notFound().build());
	}
}
