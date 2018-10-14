package com.casumo.videorentalstore.rest.catalog;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.casumo.videorentalstore.catalog.core.domain.MovieType;

public class NewMovie {

	@NotEmpty
	private String name;
	@NotNull
	private MovieType type;
	@Positive
	private int availableCopiesForRental;
	
	public void setName(String name) {
		this.name = name;
	}

	public void setType(MovieType type) {
		this.type = type;
	}

	public void setAvailableCopiesForRental(int availableCopies) {
		this.availableCopiesForRental = availableCopies;
	}

	public String getName() {
		return name;
	}

	public MovieType getType() {
		return type;
	}

	public int getAvailableCopiesForRental() {
		return availableCopiesForRental;
	}
}
