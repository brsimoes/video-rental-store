package com.casumo.videorentalstore.rental.core.domain.command;

import java.time.LocalDate;
import java.util.UUID;

public class ReturnRentedMovieCommand extends RentalCommand {

	private final UUID movieId;
	private final double surchargeAmmount;
	private final LocalDate returnDate;
	
	public ReturnRentedMovieCommand(UUID rentalId, UUID movieId, double surchargeAmmount, LocalDate returnDate) {
		super(rentalId);
		this.movieId = movieId;
		this.surchargeAmmount = surchargeAmmount;
		this.returnDate = returnDate;
	}

	public UUID getMovieId() {
		return this.movieId;
	}

	public double getSurchargeAmmount() {
		return surchargeAmmount;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}
}
