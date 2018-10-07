package com.casumo.videorentalstore.rest.user;

import javax.validation.constraints.NotEmpty;

public class NewUser {

	@NotEmpty
	private String name;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
