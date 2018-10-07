package com.casumo.videorentalstore.user.core.domain.command;

import java.util.UUID;

public class CreateUserCommand extends UserCommand {
	private final String name;

	public CreateUserCommand(UUID userId, String name) {
		super(userId);
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
