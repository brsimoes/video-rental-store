package com.casumo.videorentalstore.rental.core.domain;

import java.time.LocalDate;
import java.util.UUID;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import com.casumo.videorentalstore.enums.MovieType;
import com.casumo.videorentalstore.enums.RentalStatus;
import com.casumo.videorentalstore.rental.core.domain.RentalAggregate;
import com.casumo.videorentalstore.rental.core.domain.command.AddRentedMovieCommand;
import com.casumo.videorentalstore.rental.core.domain.command.ChangeRentalStatusCommand;
import com.casumo.videorentalstore.rental.core.domain.command.CreateRentalCommand;
import com.casumo.videorentalstore.rental.core.domain.command.ReturnRentedMovieCommand;
import com.casumo.videorentalstore.rental.core.domain.event.RentalCreatedEvent;
import com.casumo.videorentalstore.rental.core.domain.event.RentalStatusChangedEvent;
import com.casumo.videorentalstore.rental.core.domain.event.RentedMovieAddedEvent;
import com.casumo.videorentalstore.rental.core.domain.event.RentedMovieReturnedEvent;

public class RentalAggregateTests {
	private FixtureConfiguration<RentalAggregate> rentalFixture;
	
	@Before
	public void init() throws Exception {
		this.rentalFixture = new AggregateTestFixture<RentalAggregate>(RentalAggregate.class);
	}

	@Test
	public void createRentalCommandShoulThrowRentalCreatedEventWithStatusOpen (){
		
		UUID rentalId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		RentalStatus status = RentalStatus.OPEN;
		
		this.rentalFixture.givenNoPriorActivity()
						  .when(new CreateRentalCommand(rentalId, userId))
						  .expectEvents(new RentalCreatedEvent(rentalId, userId, status));
	}
	
	@Test
	public void addRentedMovieCommandShoulThrowRentedMovieAddedEvent () {
		
		UUID rentalId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		UUID movieId = UUID.randomUUID();
		MovieType movieType = MovieType.NEW_RELEASE;
		int hireDays = 5;
		double chargeAmmount = 10;
		RentalStatus status = RentalStatus.OPEN;
		
		
		this.rentalFixture.given(new RentalCreatedEvent(rentalId, userId, status))
						  .when(new AddRentedMovieCommand(rentalId, movieId, hireDays, chargeAmmount, movieType))
						  .expectEvents(new RentedMovieAddedEvent(rentalId, movieId, hireDays, chargeAmmount, movieType));
	}
	
	@Test
	public void changeRentalStatusCommandShoulThrowRentalStatusChangedEvent () {
		
		UUID rentalId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		RentalStatus oldStatus = RentalStatus.OPEN;
		RentalStatus newStatus = RentalStatus.SUBMITED;
		
		this.rentalFixture.given(new RentalCreatedEvent(rentalId, userId, oldStatus))
						  .when(new ChangeRentalStatusCommand(rentalId, newStatus))
						  .expectEvents(new RentalStatusChangedEvent(rentalId, newStatus, oldStatus));
	}
	
	@Test
	public void returnRentedMovieCommandShoulThrowRentedMovieReturnedEvent () {
		
		UUID rentalId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		UUID movieId = UUID.randomUUID();
		MovieType movieType = MovieType.NEW_RELEASE;
		int hireDays = 5;
		double chargeAmmount = 10;
		double surchargeAmmount = 0;
		RentalStatus status = RentalStatus.OPEN;
		LocalDate returnDate = LocalDate.now();
		
		this.rentalFixture.given(new RentalCreatedEvent(rentalId, userId, status),
								new RentedMovieAddedEvent(rentalId, movieId, hireDays, chargeAmmount, movieType))
						  .when(new ReturnRentedMovieCommand(rentalId, movieId, surchargeAmmount, returnDate))
						  .expectEvents(new RentedMovieReturnedEvent(rentalId, movieId, surchargeAmmount, returnDate));
	}
}
