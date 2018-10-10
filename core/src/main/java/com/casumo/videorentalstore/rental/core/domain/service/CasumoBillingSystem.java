package com.casumo.videorentalstore.rental.core.domain.service;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.casumo.videorentalstore.catalog.core.domain.MovieType;

@Component
public class CasumoBillingSystem implements BillingService {

	@Override
	public double getRentalChargeAmmountFor(MovieType movieType, int hireDays)
			throws UnknownPriceFormulaForMovieTypeException {
		
		return PriceTable.getPriceTableFor(movieType).getRentalChargeAmmount(hireDays);
	}

	@Override
	public double getReturnSurchargeAmmountFor(
			MovieType movieType, int alreadyPaidHireDays, LocalDate rentalDate,  LocalDate returnDate)
			throws UnknownPriceFormulaForMovieTypeException {
		
		return PriceTable.getPriceTableFor(movieType)
				           .getReturnSurChargeAmmount(alreadyPaidHireDays, rentalDate, returnDate);
	}

	@Override
	public int getBonusAmmountFor(MovieType movieType, LocalDate rentalDate, LocalDate returnDate)
			throws UnknownPriceFormulaForMovieTypeException {
		return PriceTable.getPriceTableFor(movieType).getBonusAmmount();
	}
}
