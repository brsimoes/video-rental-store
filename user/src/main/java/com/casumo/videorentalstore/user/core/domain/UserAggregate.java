package com.casumo.videorentalstore.user.core.domain;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.casumo.videorentalstore.user.core.domain.command.CreateUserCommand;
import com.casumo.videorentalstore.user.core.domain.command.AddBonusPointsCommand;
import com.casumo.videorentalstore.user.core.domain.command.AddUserRentalCommand;
import com.casumo.videorentalstore.user.core.domain.event.BonusPointsUpdatedEvent;
import com.casumo.videorentalstore.user.core.domain.event.UserCreatedEvent;
import com.casumo.videorentalstore.user.core.domain.event.UserRentalAddedEvent;

@Aggregate
public class UserAggregate {
	@AggregateIdentifier
	private UUID userId;
	private int bonusPoints = 0;

	protected UserAggregate() {

	}

	@CommandHandler
	public UserAggregate(CreateUserCommand command) {
		apply(new UserCreatedEvent(command.getUserId(), command.getName()));
	}
	
	@CommandHandler
	public void handle (AddBonusPointsCommand command) {
		apply(new BonusPointsUpdatedEvent(command.getUserId(), this.bonusPoints += command.getBonusPoints()));
	}
	
	@CommandHandler
	public void handle (AddUserRentalCommand command) {
		apply(new UserRentalAddedEvent(command.getUserId(), command.getRentalId()));
	}

	@EventSourcingHandler
	public void on(UserCreatedEvent event) {
		this.userId = event.getUserId();
	}
	
	@EventSourcingHandler
	public void on(BonusPointsUpdatedEvent event) {
		this.bonusPoints = event.getBonusPoints();
	}
	
	@EventSourcingHandler
	public void on(UserRentalAddedEvent event) {
		// Do nothing
	}
}
