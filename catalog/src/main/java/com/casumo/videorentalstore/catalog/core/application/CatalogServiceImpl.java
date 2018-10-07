package com.casumo.videorentalstore.catalog.core.application;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;

import com.casumo.videorentalstore.catalog.core.domain.command.CancelMovieRentalCommand;
import com.casumo.videorentalstore.catalog.core.domain.command.CreateMovieCommand;
import com.casumo.videorentalstore.catalog.core.domain.command.RentMovieCommand;
import com.casumo.videorentalstore.catalog.core.domain.command.ReturnMovieCommand;
import com.casumo.videorentalstore.catalog.core.dto.Movie;
import com.casumo.videorentalstore.catalog.core.persistence.MovieRepository;
import com.casumo.videorentalstore.catalog.core.port.CatalogService;
import com.casumo.videorentalstore.enums.MovieType;

@Component
public class CatalogServiceImpl implements CatalogService {

	private final CommandGateway commandGateway;
	private final MovieRepository movieRepository;
	
	public CatalogServiceImpl(CommandGateway commandGateway, MovieRepository movieRepository){
		this.commandGateway = commandGateway;
		this.movieRepository = movieRepository;
	}
	
	@Override
	public void createMovie(UUID movieId, String name, MovieType type, int numberOfCopiesAvailable) {
		this.commandGateway.send(
				new CreateMovieCommand(
						movieId,
						name,
						type,
						numberOfCopiesAvailable));
	}

	@Override
	public void rentMovie(UUID movieId, UUID rentalId, int hireDays, LocalDate rentalDate) {
		this.commandGateway.send(
				new RentMovieCommand(
						movieId,
						rentalId,
						hireDays,
						rentalDate));
	}
	
	@Override
	public void cancelMovieRental(UUID movieId, UUID rentalId) {
		this.commandGateway.send(
				new CancelMovieRentalCommand(
						movieId,
						rentalId));
	}

	@Override
	public void returnMovie(UUID movieId, UUID rentalId, LocalDate returnDate) {
		this.commandGateway.send(
				new ReturnMovieCommand(
						movieId,
						rentalId,
						returnDate));
	}

	@Override
	public Optional<Movie> getMovie(UUID movieId) {
		return this.movieRepository.findById(movieId).map(this::toMovieDto);
	}

	@Override
	public Iterable<Movie> getAllMovies() {
		return this.movieRepository.findAll().stream().map(this::toMovieDto).collect(Collectors.toList());
	}
	
	private Movie toMovieDto (com.casumo.videorentalstore.catalog.core.persistence.MovieEntity movie) {
		return new Movie (
					movie.getId(),
					movie.getName(),
					movie.getType(),
					movie.getAvailableCopiesToRent(),
					movie.getActiveRentalsExpectedEndDate());
	}
	
}
