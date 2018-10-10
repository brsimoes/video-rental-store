package com.casumo.videorentalstore.catalog.core.domain.command;

import java.util.UUID;

import com.casumo.videorentalstore.catalog.core.domain.MovieType;

public class ChangeMovieTypeCommand extends MovieCommand {

	private final MovieType newType;

	public ChangeMovieTypeCommand(UUID movieId, MovieType newType) {
		super(movieId);
		this.newType = newType;
	}

	public MovieType getNewType() {
		return newType;
	}
}
