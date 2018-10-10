package com.casumo.videorentalstore.catalog.core.domain.event;

import java.util.UUID;

import com.casumo.videorentalstore.catalog.core.domain.MovieType;

public class MovieTypeChangedEvent extends MovieEvent {
	
	private final MovieType newType;
	private final MovieType oldType;

	public MovieTypeChangedEvent(UUID movieId, MovieType newType, MovieType oldType) {
		super(movieId);
		this.newType = newType;
		this.oldType = oldType;
	}

	public MovieType getNewType() {
		return newType;
	}

	public MovieType getOldType() {
		return oldType;
	}
}
