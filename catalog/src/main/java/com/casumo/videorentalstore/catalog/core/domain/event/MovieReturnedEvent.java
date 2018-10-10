package com.casumo.videorentalstore.catalog.core.domain.event;

import java.time.LocalDate;
import java.util.UUID;

import com.casumo.videorentalstore.catalog.core.domain.MovieType;

public class MovieReturnedEvent extends MovieEvent {

	private final UUID rentalId;
	private final MovieType movieType;
	private final int hireDaysAmmount;
	private final LocalDate returnDate;
	private final int availableCopiesToRent;

	public MovieReturnedEvent(UUID movieId, UUID rentalId, MovieType movieType, int hireDaysAmmount,
			LocalDate returnDate, int availableCopiesToRent) {
		super(movieId);
		this.rentalId = rentalId;
		this.movieType = movieType;
		this.returnDate = returnDate;
		this.hireDaysAmmount = hireDaysAmmount;
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

	public int getHireDaysAmmount() {
		return hireDaysAmmount;
	}

	public int getAvailableCopiesToRent() {
		return availableCopiesToRent;
	}
}
