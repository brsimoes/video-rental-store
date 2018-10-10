package com.casumo.videorentalstore.catalog.core.application.dto;

import java.util.Collections;
import java.util.UUID;

import com.casumo.videorentalstore.catalog.core.domain.MovieType;

public class Movie {

	private final UUID id;
	private final String name;
	private final MovieType type;
	private final int availableCopiesToRent;
	private final Iterable<UUID> activeRentals;

	public Movie(UUID id, String name, MovieType type, int availableCopiesToRent) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.availableCopiesToRent = availableCopiesToRent;
		this.activeRentals = Collections.emptyList();
	}
	
	public Movie(UUID id, String name, MovieType type, int availableCopiesToRent, Iterable<UUID> activeRentals) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.availableCopiesToRent = availableCopiesToRent;
		this.activeRentals = activeRentals;
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

	public Iterable<UUID> getActiveRentals() {
		return activeRentals;
	}
}
