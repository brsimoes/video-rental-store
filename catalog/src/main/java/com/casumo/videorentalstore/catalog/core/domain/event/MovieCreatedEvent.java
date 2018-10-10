package com.casumo.videorentalstore.catalog.core.domain.event;

import java.util.UUID;

import com.casumo.videorentalstore.catalog.core.domain.MovieType;

public class MovieCreatedEvent extends MovieEvent {

	private final String name;
	private final MovieType type;
	private final int maxAvailableCopiesToRent;

	public MovieCreatedEvent(UUID movieId, String name, MovieType type, int maxAvailableCopiesToRent) {
		super(movieId);
		this.name = name;
		this.type = type;
		this.maxAvailableCopiesToRent = maxAvailableCopiesToRent;
	}
	
	public String getName() {
		return name;
	}

	public MovieType getType() {
		return type;
	}

	public int getMaxAvailableCopiesToRent() {
		return maxAvailableCopiesToRent;
	}
}
