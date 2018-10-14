package com.casumo.videorentalstore.user.core.domain;

import java.util.UUID;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import com.casumo.videorentalstore.user.core.domain.command.AddBonusPointsCommand;
import com.casumo.videorentalstore.user.core.domain.command.AddUserRentalCommand;
import com.casumo.videorentalstore.user.core.domain.command.CreateUserCommand;
import com.casumo.videorentalstore.user.core.domain.event.BonusPointsUpdatedEvent;
import com.casumo.videorentalstore.user.core.domain.event.UserCreatedEvent;
import com.casumo.videorentalstore.user.core.domain.event.UserRentalAddedEvent;

public class UserAggregateTests {
	private FixtureConfiguration<UserAggregate> userFixture;
	
	@Before
	public void init() throws Exception {
		this.userFixture = new AggregateTestFixture<UserAggregate>(UserAggregate.class);
	}

	@Test
	public void createUserCommandShouldThrowUserCreatedEvent () {
		
		UUID userId = UUID.randomUUID();
		String name = "Bruno";
		
		this.userFixture.givenNoPriorActivity()
						  .when(new CreateUserCommand(userId, "Dias"))
						  .expectEvents(new UserCreatedEvent(userId, name));
	}
	
	@Test
	public void updateBonusPointsCommandShouldThrowBonusPointsUpdatedEvent () {
		
		UUID userId = UUID.randomUUID();
		String name = "Bruno";
		int bonusPoints = 1;
		
		this.userFixture.given(new UserCreatedEvent(userId, name))
						  .when(new AddBonusPointsCommand(userId, bonusPoints))
						  .expectEvents(new BonusPointsUpdatedEvent(userId, bonusPoints));
	}
	
	@Test
	public void addUserRentalCommandShouldThrowUserRentalAddedEvent () {
		
		UUID userId = UUID.randomUUID();
		UUID rentalId = UUID.randomUUID();
		String name = "Bruno";
		
		this.userFixture.given(new UserCreatedEvent(userId, name))
						  .when(new AddUserRentalCommand(userId, rentalId))
						  .expectEvents(new UserRentalAddedEvent(userId, rentalId));
	}
}
