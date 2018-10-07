package com.casumo.videorentalstore.catalog.core.domain.event;

import java.time.LocalDate;
import java.util.UUID;

import com.casumo.videorentalstore.enums.MovieType;

public class MovieReturnedEvent extends MovieEvent {

	private final UUID rentalId;
	private final MovieType movieType;
	private final LocalDate rentalDate;
	private final LocalDate returnDate;
	private final int availableCopiesToRent;

	public MovieReturnedEvent(UUID movieId, UUID rentalId, MovieType movieType, LocalDate rentalDate,
			LocalDate returnDate, int availableCopiesToRent) {
		super(movieId);
		this.rentalId = rentalId;
		this.movieType = movieType;
		this.returnDate = returnDate;
		this.rentalDate = rentalDate;
		this.availableCopiesToRent = availableCopiesToRent;
	}

	public UUID getRentalId() {
		return rentalId;
	}

	public MovieType getMovieType() {
		return movieType;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public LocalDate getRentalDate() {
		return rentalDate;
	}

	public int getAvailableCopiesToRent() {
		return availableCopiesToRent;
	}
}
