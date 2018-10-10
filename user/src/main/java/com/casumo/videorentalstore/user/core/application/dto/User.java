package com.casumo.videorentalstore.user.core.application.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class User {

	private UUID id;
	private String name;
	private int bonusPoints;
	private Collection<UUID> rentals;
	
	public User() {
		this.rentals = Collections.emptyList();
	}
	
	public User(UUID id, String name) {
		this.id = id;
		this.name = name;
		this.bonusPoints = 0;
		this.rentals = Collections.emptySet();
	}
	
	public User(UUID id, String name, int bonusPoints, Collection<UUID> activeRentals) {
		this.id = id;
		this.name = name;
		this.rentals = activeRentals;
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

	public Collection<UUID> getRentals() {
		return rentals;
	}

	public void setRentals(Collection<UUID> rentals) {
		this.rentals = rentals;
	}
}
