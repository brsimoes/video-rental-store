package com.casumo.videorentalstore.rest.catalog;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.UUID;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.casumo.videorentalstore.catalog.core.application.dto.Movie;
import com.casumo.videorentalstore.rest.rental.RentalsController;

@Component
public class MovieResourceAssembler implements ResourceAssembler<Movie, Resource<Movie>> {

	@Override
	public Resource<Movie> toResource(Movie movie) {

		Resource<Movie> movieResouce = new Resource<>(movie,
				linkTo(methodOn(CatalogController.class).getAllMovies()).withRel("movies"),
				linkTo(methodOn(CatalogController.class).getMovieById(movie.getId())).withSelfRel());

		for (UUID rentalId : movie.getActiveRentals()) {
			movieResouce.add(linkTo(methodOn(RentalsController.class).getRentalById(rentalId)).withRel("rentals"));
		}

		return movieResouce;
	}
}
