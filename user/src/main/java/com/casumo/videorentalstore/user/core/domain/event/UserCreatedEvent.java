package com.casumo.videorentalstore.user.core.domain.event;

import java.util.UUID;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class UserCreatedEvent extends UserEvent {
	private final String name;

	public UserCreatedEvent(UUID userId, String name) {
		super(userId);
		this.name = name;
	}

	@TargetAggregateIdentifier
	public String getName() {
		return name;
	}
}
