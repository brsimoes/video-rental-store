package com.casumo.videorentalstore.catalog.core.application;

import java.util.Collections;
import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.casumo.videorentalstore.catalog.core.domain.event.MovieCreatedEvent;
import com.casumo.videorentalstore.catalog.core.domain.event.MovieRentalCanceledEvent;
import com.casumo.videorentalstore.catalog.core.domain.event.MovieRentedEvent;
import com.casumo.videorentalstore.catalog.core.domain.event.MovieReturnedEvent;
import com.casumo.videorentalstore.catalog.core.domain.event.MovieTypeChangedEvent;
import com.casumo.videorentalstore.catalog.core.persistence.MovieEntity;
import com.casumo.videorentalstore.catalog.core.persistence.MovieRepository;

@Component
public class CatalogEventHandler {

	private final MovieRepository repository;

	public CatalogEventHandler(MovieRepository repository) {
		this.repository = repository;
	}

	@EventHandler
	public void handle(MovieCreatedEvent evt) {
		this.repository.save(new MovieEntity(
									evt.getMovieId(), 
									evt.getName(), 
									evt.getType(), 
									evt.getMaxAvailableCopiesToRent(), 
									Collections.emptyMap()));
	}
	
	@EventHandler
	public void handle(MovieRentedEvent evt) {
		Optional<MovieEntity> movie = this.repository.findById(evt.getMovieId());
		if (movie.isPresent()) {
			movie.get().setAvailableCopiesToRent(evt.getCopiesLastToRent());
			movie.get().addRental(evt.getRentalId(), evt.getRentalDate().plusDays(evt.getHireDays()));
		}
	}
	
	@EventHandler
	public void handle(MovieReturnedEvent evt) {
		Optional<MovieEntity> movie = this.repository.findById(evt.getMovieId());
		if (movie.isPresent()) {
			movie.get().setAvailableCopiesToRent(evt.getAvailableCopiesToRent());
			movie.get().removeRental(evt.getRentalId());
		}
	}
	
	@EventHandler
	public void handle(MovieRentalCanceledEvent evt) {
		Optional<MovieEntity> movie = this.repository.findById(evt.getMovieId());
		if (movie.isPresent()) {
			movie.get().setAvailableCopiesToRent(evt.getAvailableCopiesToRent());
			movie.get().removeRental(evt.getRentalId());
		}
	}
	
	@EventHandler
	public void handle(MovieTypeChangedEvent evt) {
		Optional<MovieEntity> movie = this.repository.findById(evt.getMovieId());
		if (movie.isPresent()) {
			movie.get().setType(evt.getNewType());
		}
	}
}
