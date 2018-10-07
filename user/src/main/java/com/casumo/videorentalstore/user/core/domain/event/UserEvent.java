package com.casumo.videorentalstore.user.core.domain.event;

import java.util.UUID;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public abstract class UserEvent {
	private final UUID userId;

	public UserEvent(UUID userId) {
		this.userId = userId;
	}

	@TargetAggregateIdentifier
	public UUID getUserId() {
		return userId;
	}
}
