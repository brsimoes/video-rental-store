package com.casumo.videorentalstore.rental.core.application.dto;

import java.time.LocalDate;
import java.util.UUID;

public final class ReturnedItem {
	private final UUID movieId;
	private final LocalDate returnDate;
	private final double surchargeAmmount;
	private final String movieName;

	public ReturnedItem(UUID movieId, LocalDate returnDate, double surchargeAmmount, String movieName) {
		this.movieId = movieId;
		this.returnDate = returnDate;
		this.surchargeAmmount = surchargeAmmount;
		this.movieName = movieName;
	}

	public UUID getMovieId() {
		return movieId;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public double getSurchargeAmmount() {
		return surchargeAmmount;
	}

	public String getMovieName() {
		return movieName;
	}
}
