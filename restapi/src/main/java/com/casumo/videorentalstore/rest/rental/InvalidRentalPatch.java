package com.casumo.videorentalstore.rest.rental;

public class InvalidRentalPatch extends RuntimeException {

	private static final long serialVersionUID = 9041523715551924322L;

	public InvalidRentalPatch(String string) {
		super(string);
	}

}
