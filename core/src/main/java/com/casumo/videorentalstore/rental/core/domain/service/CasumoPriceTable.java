package com.casumo.videorentalstore.rental.core.domain.service;

import java.time.LocalDate;

import com.casumo.videorentalstore.enums.MovieType;

public abstract class CasumoPriceTable {
	
	public final static double PREMIUM_PRICE = 40;
    public final static double BASIC_PRICE = 30;

	public static CasumoPriceTable getPriceTableFor(MovieType type) throws UnknownPriceFormulaForMovieTypeException {

		if (type.isNewRelease())
			return new NewReleasePriceTable();

		if (type.isOldFilm())
			return new OldFilmPriceTable();

		if (type.isRegularFilm())
			return new RegularFilmPriceTable();

		throw new UnknownPriceFormulaForMovieTypeException();
	}

	public abstract double getRentalChargeAmmount(int hireDays);

	public abstract double getReturnSurChargeAmmount(int paidHireDays, LocalDate rentalDate,  LocalDate returnDate);

	public abstract int getBonusAmmount();
}
