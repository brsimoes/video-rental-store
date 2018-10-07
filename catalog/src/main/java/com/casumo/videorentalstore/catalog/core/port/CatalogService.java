package com.casumo.videorentalstore.catalog.core.port;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.casumo.videorentalstore.catalog.core.dto.Movie;
import com.casumo.videorentalstore.enums.MovieType;

public interface CatalogService {
	
	void createMovie(UUID movieId, String name, MovieType type, int numberOfCopiesAvailable);
	
	public void rentMovie(UUID movieId, UUID rentalId, int hireDays, LocalDate rentalDate);
	
	public void cancelMovieRental(UUID movieId, UUID rentalId);
	
	public void returnMovie(UUID movieId, UUID rentalId, LocalDate returnDate);
	
	public Optional<Movie> getMovie(UUID movieId);
	
	public Iterable<Movie> getAllMovies();
}
