package com.casumo.videorentalstore.user.core.domain.event;

import java.util.UUID;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class BonusPointsUpdatedEvent extends UserEvent {
	private final int bonusPoints;

	public BonusPointsUpdatedEvent(UUID userId, int bonusPoints) {
		super(userId);
		this.bonusPoints = bonusPoints;
	}

	@TargetAggregateIdentifier
	public int getBonusPoints() {
		return bonusPoints;
	}
}
