package com.casumo.videorentalstore.rental.core.persistence;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Embeddable;

@Embeddable
public final class Return {
	private UUID movieId;
	private LocalDate returnDate;
	private double surchargeAmmount;

	protected Return() {
		
	}
	
	public Return(UUID movieId, LocalDate returnDate, double surchargeAmmount) {
		super();
		this.movieId = movieId;
		this.returnDate = returnDate;
		this.surchargeAmmount = surchargeAmmount;
	}

	public UUID getMovieId() {
		return movieId;
	}

	public void setMovieId(UUID movieId) {
		this.movieId = movieId;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	public double getSurchargeAmmount() {
		return surchargeAmmount;
	}

	public void setSurchargeAmmount(double surchargeAmmount) {
		this.surchargeAmmount = surchargeAmmount;
	}
}
