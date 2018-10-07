package com.casumo.videorentalstore.user.core.application.dto;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class User {

	private UUID id;
	private String name;
	private int bonusPoints;
	private Set<UUID> rentals;
	
	public User() {
		this.rentals = Collections.emptySet();
	}
	
	public User(UUID id, String name) {
		this.id = id;
		this.name = name;
		this.bonusPoints = 0;
		this.rentals = Collections.emptySet();
	}
	
	public User(UUID id, String name, int bonusPoints, Set<UUID> rentals) {
		this.id = id;
		this.name = name;
		this.rentals = rentals;
		this.bonusPoints = bonusPoints;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBonusPoints() {
		return bonusPoints;
	}

	public void setBonusPoints(int bonusPoints) {
		this.bonusPoints = bonusPoints;
	}

	public Set<UUID> getRentals() {
		return rentals;
	}

	public void setRentals(Set<UUID> rentals) {
		this.rentals = rentals;
	}
}
