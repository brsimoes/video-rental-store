package com.casumo.videorentalstore.catalog.core.dto;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import com.casumo.videorentalstore.enums.MovieType;

public class Movie {

	private final UUID id;
	private final String name;
	private final MovieType type;
	private final int availableCopiesToRent;
	private final Map<UUID, LocalDate> activeRentalsExpectedEndDate;

	public Movie(UUID id, String name, MovieType type, int availableCopiesToRent) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.availableCopiesToRent = availableCopiesToRent;
		this.activeRentalsExpectedEndDate = Collections.emptyMap();
	}
	
	public Movie(UUID id, String name, MovieType type, int availableCopiesToRent,
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

	public String getName() {
		return name;
	}

	public MovieType getType() {
		return type;
	}

	public int getAvailableCopiesToRent() {
		return availableCopiesToRent;
	}

	public Map<UUID, LocalDate> getActiveRentalsExpectedEndDate() {
		return activeRentalsExpectedEndDate;
	}
}
