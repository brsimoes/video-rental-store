package com.casumo.videorentalstore.catalog.core.persistence;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.casumo.videorentalstore.enums.MovieType;

@Entity
public class MovieEntity {
	@Id
	private UUID id;
	private String name;
	private MovieType type;
	private int availableCopiesToRent;
	@ElementCollection
	private Map<UUID, LocalDate> activeRentalsExpectedEndDate;
	
	protected MovieEntity() {
		
	}
	
	public MovieEntity(UUID id, String name, MovieType type, int availableCopiesToRent, 
				Map<UUID, LocalDate> activeRentalsExpectedEndDate) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.availableCopiesToRent = availableCopiesToRent;
		this.activeRentalsExpectedEndDate = activeRentalsExpectedEndDate;
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
	
	public Map<UUID, LocalDate> getActiveRentalsExpectedEndDate() {
		return activeRentalsExpectedEndDate;
	}

	public void setActiveRentalsExpectedEndDate(Map<UUID, LocalDate> activeRentalsDuration) {
		this.activeRentalsExpectedEndDate = activeRentalsDuration;
	}

	public void addRental(UUID rental, LocalDate expectedEndDate) {
		this.activeRentalsExpectedEndDate.put(rental, expectedEndDate);
	}
	
	public void removeRental(UUID rental) {
		this.activeRentalsExpectedEndDate.remove(rental);
	}
}
