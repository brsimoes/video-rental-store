package com.casumo.videorentalstore.rental.core.persistence;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Embeddable;

@Embeddable
public final class ReturnItemDetail {
	private UUID movieId;
	private LocalDate returnDate;
	private double surchargeAmmount;
	private String movieName;

	protected ReturnItemDetail() {
		
	}
	
	public ReturnItemDetail(UUID movieId, LocalDate returnDate, double surchargeAmmount, String movieName) {
		this.movieId = movieId;
		this.returnDate = returnDate;
		this.surchargeAmmount = surchargeAmmount;
		this.movieName = movieName;
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

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
}
