package com.casumo.videorentalstore.catalog.core.port;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.casumo.videorentalstore.catalog.core.application.dto.Movie;

public interface CatalogService {
	
	void createMovie(Movie movie);
	
	public void rentMovie(UUID movieId, UUID rentalId, int hireDays);
	
	public void cancelMovieRental(UUID movieId, UUID rentalId);
	
	public void returnMovie(UUID movieId, UUID rentalId, LocalDate returnDate);
	
	public Optional<Movie> getMovieById(UUID movieId);
	
	public Iterable<Movie> getAllMovies();
}
