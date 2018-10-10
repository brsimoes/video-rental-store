package com.casumo.videorentalstore.catalog.core.domain.command;

import java.util.UUID;

import com.casumo.videorentalstore.catalog.core.domain.MovieType;

public class CreateMovieCommand extends MovieCommand {

	private final String name;
	private final MovieType type;
	private final int numberCopies;

	public CreateMovieCommand(UUID movieId, String name, MovieType type, int numberCopies) {
		super(movieId);
		this.name = name;
		this.type = type;
		this.numberCopies = numberCopies;
	}

	public String getName() {
		return name;
	}

	public MovieType getType() {
		return type;
	}

	public int getNumberCopies() {
		return numberCopies;
	}
}
