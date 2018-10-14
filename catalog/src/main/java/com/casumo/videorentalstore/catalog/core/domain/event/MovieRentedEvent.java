package com.casumo.videorentalstore.catalog.core.domain.event;

import java.util.UUID;

import com.casumo.videorentalstore.catalog.core.domain.MovieType;

public class MovieRentedEvent extends MovieEvent {

	private final MovieType movieType;
	private final UUID rentalId;
	private final int numberOfDaysHired;
	private final int copiesLeftToRent;
	private final String movieName;

	public MovieRentedEvent(UUID movieId, MovieType movieType, UUID rentalId, int numberOfDaysHired,
			int copiesLeftToRent, String movieName) {
		super(movieId);
		this.rentalId = rentalId;
		this.numberOfDaysHired = numberOfDaysHired;
		this.movieType = movieType;
		this.copiesLeftToRent = copiesLeftToRent;
		this.movieName = movieName;
	}

	public UUID getRentalId() {
		return rentalId;
	}

	public int getNumberOfDaysHired() {
		return numberOfDaysHired;
	}

	public MovieType getMovieType() {
		return movieType;
	}

	public int getCopiesLeftToRent() {
		return copiesLeftToRent;
	}

	public String getMovieName() {
		return movieName;
	}
}
