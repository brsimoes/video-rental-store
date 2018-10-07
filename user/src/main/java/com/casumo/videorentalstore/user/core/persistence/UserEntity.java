package com.casumo.videorentalstore.user.core.persistence;

import java.util.HashSet;
import java.util.UUID;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserEntity {
	@Id
	private UUID id;
	private String name;
	private int bonusPoints;
	@Embedded
	private HashSet<UUID> rentals;
	
	protected UserEntity() {
		
	}
	
	public UserEntity(UUID id, String name, int bonusPoints, HashSet<UUID> rentals) {
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

	public HashSet<UUID> getRentals() {
		return rentals;
	}

	public void setRentals(HashSet<UUID> rentals) {
		this.rentals = rentals;
	}

	public int getBonusPoints() {
		return bonusPoints;
	}
	
	public void setBonusPoints(int bonusPoints) {
		this.bonusPoints = bonusPoints;
	}
}
