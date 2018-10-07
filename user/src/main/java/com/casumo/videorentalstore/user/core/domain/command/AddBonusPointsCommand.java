package com.casumo.videorentalstore.user.core.domain.command;

import java.util.UUID;

public class AddBonusPointsCommand extends UserCommand {
	private final int bonusPoints;

	public AddBonusPointsCommand(UUID userId, int bonusPoints) {
		super(userId);
		this.bonusPoints = bonusPoints;
	}

	public int getBonusPoints() {
		return bonusPoints;
	}
}
