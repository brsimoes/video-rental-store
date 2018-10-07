package com.casumo.videorentalstore.rental.core.domain.service;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.casumo.videorentalstore.enums.MovieType;

@Component
public class BillingServiceImpl implements BillingService {

	@Override
	public double getRentalChargeAmmountFor(MovieType movieType, int hireDays)
			throws UnknownPriceFormulaForMovieTypeException {
		
		return CasumoPriceTable.getPriceTableFor(movieType).getRentalChargeAmmount(hireDays);
	}

	@Override
	public double getReturnSurchargeAmmountFor(
			MovieType movieType, int alreadyPaidHireDays, LocalDate rentalDate,  LocalDate returnDate)
			throws UnknownPriceFormulaForMovieTypeException {
		
		return CasumoPriceTable.getPriceTableFor(movieType)
				           .getReturnSurChargeAmmount(alreadyPaidHireDays, rentalDate, returnDate);
	}

	@Override
	public int getBonusAmmountFor(MovieType movieType, LocalDate rentalDate, LocalDate returnDate)
			throws UnknownPriceFormulaForMovieTypeException {
		return CasumoPriceTable.getPriceTableFor(movieType).getBonusAmmount();
	}
}
