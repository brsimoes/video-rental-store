package com.casumo.videorentalstore.catalog.core.domain.command;

import java.util.UUID;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public abstract class MovieCommand {
	private final UUID movieId;

	public MovieCommand(UUID movieId) {
		this.movieId = movieId;
	}

	@TargetAggregateIdentifier
	public UUID getMovieId() {
		return movieId;
	}
}
