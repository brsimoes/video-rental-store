package com.casumo.videorentalstore.init;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;

import com.casumo.videorentalstore.catalog.core.domain.command.CreateMovieCommand;
import com.casumo.videorentalstore.enums.MovieType;
import com.casumo.videorentalstore.user.core.domain.command.CreateUserCommand;

public class DBInit {

	private CommandGateway commandGateway;

	public DBInit(CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
	}

	public void loadMovies() {
		this.addNewMovie(
				UUID.fromString("ec5cbcd9-08b3-466e-8f76-a40c29bb1738"), 
				"The Dark Knight Rises",
				MovieType.REGULAR_FILM, 
				3);

		this.addNewMovie(
				UUID.fromString("80ab0aeb-d59a-409f-ad29-955743aed434"), 
				"The Wolf of Wall Street",
				MovieType.REGULAR_FILM, 
				3);

		this.addNewMovie(
				UUID.fromString("917e8bed-6b7c-4cc7-9d39-06db268f29d7"), 
				"Trainspotting",
				MovieType.REGULAR_FILM, 
				3);

		this.addNewMovie(
				UUID.fromString("92d6e75a-ee87-47ef-9178-8ee3f797d5ca"), 
				"The Godfather", 
				MovieType.OLD_FILM,
				5);

		this.addNewMovie(
				UUID.fromString("29e6c30d-e506-4c19-bf93-12e755a92e0a"), 
				"American History X",
				MovieType.OLD_FILM, 
				5);

		this.addNewMovie(
				UUID.fromString("aa4bd571-7b2c-4a5f-8dc7-5f5bc41e8a36"), 
				"Pulp Fiction", 
				MovieType.OLD_FILM,
				5);

		this.addNewMovie(
				UUID.fromString("9bef5979-7249-44eb-ae5d-ef6d54cba9b4"), 
				"Avengers: Infinity War",
				MovieType.NEW_RELEASE, 
				1);

		this.addNewMovie(
				UUID.fromString("8e5751ed-c22c-42ec-96cc-ea8210e9b5ef"), 
				"Deadpool 2", 
				MovieType.NEW_RELEASE,
				1);

		this.addNewMovie(
				UUID.fromString("af4a2b9f-1631-411c-a17b-42d3093a9cf5"), 
				"Super Troopers 2",
				MovieType.NEW_RELEASE, 
				1);
	}
	
	public void loadUsers() {
		this.addNewUser(
				UUID.fromString("0bc3f79b-8a0a-40a4-8463-222703c78b13"),
				"Bruno");
		
		this.addNewUser(
				UUID.fromString("6c25ec8e-f80b-4114-a667-ebf69c08ae9e"),
				"Miguel");
		
		this.addNewUser(
				UUID.fromString("cd8a2eec-a33e-43f1-9727-b3a10523f0dd"),
				"Vasco");
	}

	private void addNewMovie(UUID movieId, String name, MovieType type, int numberCopies) {
		this.commandGateway.send(new CreateMovieCommand(movieId, name, type, numberCopies));
	}
	
	private void addNewUser(UUID userId, String name) {
		this.commandGateway.send(new CreateUserCommand(userId, name));
	}
}
