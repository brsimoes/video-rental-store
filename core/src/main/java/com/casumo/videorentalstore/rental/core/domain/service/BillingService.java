package com.casumo.videorentalstore.rental.core.domain.service;

import java.time.LocalDate;

import com.casumo.videorentalstore.catalog.core.domain.MovieType;


public interface BillingService {

	public double getRentalChargeAmmountFor(MovieType movieType, int hireDays)
			throws UnknownPriceFormulaForMovieTypeException;
	
	public double getReturnSurchargeAmmountFor(
			MovieType movieType, int alreadyPaidHireDays, LocalDate rentalDate,  LocalDate returnDate)
	
			throws UnknownPriceFormulaForMovieTypeException;
	
	public int getBonusAmmountFor(MovieType movieType, LocalDate rentalDate,  LocalDate returnDate) 
			throws UnknownPriceFormulaForMovieTypeException;
}