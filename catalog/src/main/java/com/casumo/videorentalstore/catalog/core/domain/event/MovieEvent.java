package com.casumo.videorentalstore.catalog.core.domain.event;

import java.util.UUID;

public abstract class MovieEvent {
	private final UUID movieId;

	public MovieEvent(UUID movieId) {
		this.movieId = movieId;
	}

	public UUID getMovieId() {
		return movieId;
	}
}
