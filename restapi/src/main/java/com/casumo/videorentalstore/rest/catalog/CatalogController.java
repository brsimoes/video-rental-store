package com.casumo.videorentalstore.rest.catalog;

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

import com.casumo.videorentalstore.catalog.core.dto.Movie;
import com.casumo.videorentalstore.catalog.core.port.CatalogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/movies")
@Api(value="movieCatalog", tags="Movie Catalog", description="Operations to manage the movies Catalog")
public class CatalogController {

	@Autowired
	private CatalogService catalogService;

	@ApiOperation(value="Create a new movie.")
	@SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity createMovie(
		 					@RequestBody @Valid NewMovie movie,
		 					UriComponentsBuilder builder) {

	 	UUID movieId = UUID.randomUUID();
		 
		this.catalogService.createMovie(movieId, movie.getName(), movie.getType(), movie.getAvailableCopies());

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("/v1/movies/{movieId}").buildAndExpand(movieId).toUri());

 		return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
 	}
	
	@ApiOperation(value="View a list of available movies.", response=Iterable.class)
	@RequestMapping(method = RequestMethod.GET )
	public Iterable<Movie> getAllMovies() {
		Iterable<Movie> movies = this.catalogService.getAllMovies();

		return movies;
	}
	
	@ApiOperation(value="Search a movie by Id.", response=Movie.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Movie> getMovieById(@PathVariable UUID id) {
		Optional<Movie> movie = this.catalogService.getMovie(id)
				.map(m -> new Movie(m.getId(), m.getName(), m.getType(), m.getAvailableCopiesToRent()));

		HttpStatus status = movie.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
				
		return new ResponseEntity<>(movie.get(), null, status);
	}
}
