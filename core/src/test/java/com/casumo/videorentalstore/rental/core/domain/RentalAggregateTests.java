package com.casumo.videorentalstore.rental.core.domain;

import java.time.LocalDate;
import java.util.UUID;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import com.casumo.videorentalstore.catalog.core.domain.MovieType;
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
		LocalDate rentalDate = LocalDate.now();
		
		this.rentalFixture.givenNoPriorActivity()
						  .when(new CreateRentalCommand(rentalId, userId, rentalDate))
						  .expectEvents(new RentalCreatedEvent(rentalId, userId, rentalDate, status));
	}
	
	@Test
	public void addRentedMovieCommandShoulThrowRentedMovieAddedEvent () {
		
		UUID rentalId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		UUID movieId = UUID.randomUUID();
		String name ="Teste";
		MovieType movieType = MovieType.NEW_RELEASE;
		int hireDays = 5;
		double chargeAmmount = 10;
		RentalStatus status = RentalStatus.OPEN;
		LocalDate rentalDate = LocalDate.now();
		
		this.rentalFixture.given(new RentalCreatedEvent(rentalId, userId, rentalDate, status))
						  .when(new AddRentedMovieCommand(rentalId, movieId, name, movieType, hireDays, chargeAmmount ))
						  .expectEvents(new RentedMovieAddedEvent(rentalId, movieId, hireDays, chargeAmmount, movieType, name));
	}
	
	@Test
	public void changeRentalStatusCommandShoulThrowRentalStatusChangedEvent () {
		
		UUID rentalId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		RentalStatus oldStatus = RentalStatus.OPEN;
		RentalStatus newStatus = RentalStatus.SUBMITED;
		LocalDate rentalDate = LocalDate.now();
		
		this.rentalFixture.given(new RentalCreatedEvent(rentalId, userId, rentalDate, oldStatus))
						  .when(new ChangeRentalStatusCommand(rentalId, newStatus))
						  .expectEvents(new RentalStatusChangedEvent(rentalId, newStatus, oldStatus));
	}
	
	@Test
	public void returnRentedMovieCommandShoulThrowRentedMovieReturnedEvent () {
		
		UUID rentalId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		UUID movieId = UUID.randomUUID();
		MovieType movieType = MovieType.NEW_RELEASE;
		String name = "Teste";
		int hireDays = 5;
		double chargeAmmount = 10;
		double surchargeAmmount = 0;
		RentalStatus status = RentalStatus.OPEN;
		LocalDate returnDate = LocalDate.now();
		LocalDate rentalDate = LocalDate.now();
		
		this.rentalFixture.given(new RentalCreatedEvent(rentalId, userId, rentalDate, status),
								new RentedMovieAddedEvent(rentalId, movieId, hireDays, chargeAmmount, movieType, name))
						  .when(new ReturnRentedMovieCommand(rentalId, movieId, surchargeAmmount, returnDate))
						  .expectEvents(new RentedMovieReturnedEvent(rentalId, movieId, name, surchargeAmmount, returnDate));
	}
}
