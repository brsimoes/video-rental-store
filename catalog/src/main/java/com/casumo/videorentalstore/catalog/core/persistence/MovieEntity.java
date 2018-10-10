package com.casumo.videorentalstore.catalog.core.persistence;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.casumo.videorentalstore.catalog.core.domain.MovieType;

@Entity
public class MovieEntity {
	@Id
	private UUID id;
	private String name;
	private MovieType type;
	private int availableCopiesToRent;
	@ElementCollection
	private Collection<UUID> activeRentals;
	
	protected MovieEntity() {
		
	}
	
	public MovieEntity(UUID id, String name, MovieType type, int availableCopiesToRent, HashSet<UUID> activeRentals) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.availableCopiesToRent = availableCopiesToRent;
		this.activeRentals = activeRentals;
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

	public MovieType getType() {
		return type;
	}

	public void setType(MovieType type) {
		this.type = type;
	}

	public int getAvailableCopiesToRent() {
		return availableCopiesToRent;
	}

	public void setAvailableCopiesToRent(int availableCopiesToRent) {
		this.availableCopiesToRent = availableCopiesToRent;
	}
	
	public Collection<UUID> getActiveRentals() {
		return activeRentals;
	}

	public void setActiveRentalsExpectedEndDate(Collection<UUID> activeRentals) {
		this.activeRentals = activeRentals;
	}

	public void addRental(UUID rental) {
		this.activeRentals.add(rental);
	}
	
	public void removeRental(UUID rental) {
		this.activeRentals.remove(rental);
	}
}
