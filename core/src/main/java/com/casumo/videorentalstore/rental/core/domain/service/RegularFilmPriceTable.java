package com.casumo.videorentalstore.rental.core.domain.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RegularFilmPriceTable extends PriceTable {

	public static final int FLAT_PRICE_PERIOD = 3;
	
	@Override
	public double getRentalChargeAmmount(int hireDays) {
		int daysAfterFlatPrice = hireDays - FLAT_PRICE_PERIOD;

		return PriceTable.BASIC_PRICE + (daysAfterFlatPrice > 0 ? daysAfterFlatPrice * PriceTable.BASIC_PRICE : 0);
	}

	@Override
	public double getReturnSurChargeAmmount(int paidHireDays, LocalDate rentalDate,  LocalDate returnDate) {
		
		int finalHireDays = (int) ChronoUnit.DAYS.between(rentalDate, returnDate);
		int daysAlreadyPaid = Math.max(paidHireDays, FLAT_PRICE_PERIOD);
		
		return (finalHireDays - daysAlreadyPaid) > 0 ? getRentalChargeAmmount(finalHireDays - daysAlreadyPaid) : 0;
	}

	@Override
	public int getBonusAmmount() {
		return 1;
	}
}
