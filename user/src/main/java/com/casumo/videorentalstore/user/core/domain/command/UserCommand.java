package com.casumo.videorentalstore.user.core.domain.command;

import java.util.UUID;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public abstract class UserCommand {
	private final UUID userId;

	public UserCommand(UUID userId) {
		this.userId = userId;
	}

	@TargetAggregateIdentifier
	public UUID getUserId() {
		return userId;
	}
}
