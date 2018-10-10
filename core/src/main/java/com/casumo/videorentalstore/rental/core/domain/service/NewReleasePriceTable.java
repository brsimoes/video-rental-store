package com.casumo.videorentalstore.rental.core.domain.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class NewReleasePriceTable extends PriceTable {

	@Override
	public double getRentalChargeAmmount(int hireDays) {
		return hireDays * PriceTable.PREMIUM_PRICE;
	}

	@Override
	public double getReturnSurChargeAmmount(int paidHireDays, LocalDate rentalDate,  LocalDate returnDate) {

		int finalHireDays = (int) ChronoUnit.DAYS.between(rentalDate, returnDate);
		
		return finalHireDays - paidHireDays > 0 ? getRentalChargeAmmount(finalHireDays - paidHireDays) : 0;
	}

	@Override
	public int getBonusAmmount() {
		return 2;
	}

}
