package com.casumo.videorentalstore.catalog.core.domain.event;

import java.time.LocalDate;
import java.util.UUID;

import com.casumo.videorentalstore.enums.MovieType;

public class MovieRentedEvent extends MovieEvent {

	private final MovieType movieType;
	private final UUID rentalId;
	private final int hireDays;
	private final LocalDate rentalDate;
	private final int copiesLastToRent;

	public MovieRentedEvent(UUID movieId, MovieType movieType, UUID rentalId, int hireDays, LocalDate rentalDate,
			int copiesLastToRent) {
		super(movieId);
		this.rentalId = rentalId;
		this.hireDays = hireDays;
		this.movieType = movieType;
		this.rentalDate = rentalDate;
		this.copiesLastToRent = copiesLastToRent;
	}

	public UUID getRentalId() {
		return rentalId;
	}

	public int getHireDays() {
		return hireDays;
	}

	public MovieType getMovieType() {
		return movieType;
	}

	public LocalDate getRentalDate() {
		return rentalDate;
	}

	public int getCopiesLastToRent() {
		return copiesLastToRent;
	}
}
