package com.casumo.videorentalstore.catalog.core.domain;

import java.time.LocalDate;
import java.util.UUID;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import com.casumo.videorentalstore.catalog.core.domain.MovieAggregate;
import com.casumo.videorentalstore.catalog.core.domain.command.CancelMovieRentalCommand;
import com.casumo.videorentalstore.catalog.core.domain.command.CreateMovieCommand;
import com.casumo.videorentalstore.catalog.core.domain.command.RentMovieCommand;
import com.casumo.videorentalstore.catalog.core.domain.command.ReturnMovieCommand;
import com.casumo.videorentalstore.catalog.core.domain.event.MovieCreatedEvent;
import com.casumo.videorentalstore.catalog.core.domain.event.MovieRentalCanceledEvent;
import com.casumo.videorentalstore.catalog.core.domain.event.MovieRentedEvent;
import com.casumo.videorentalstore.catalog.core.domain.event.MovieReturnedEvent;

public class MovieAggregateTests {
	private FixtureConfiguration<MovieAggregate> rentalFixture;
	
	@Before
	public void init() throws Exception {
		this.rentalFixture = new AggregateTestFixture<MovieAggregate>(MovieAggregate.class);
	}

	@Test
	public void createMovieCommandShouldThrowMovieCreatedEvent () {
		
		UUID movieId = UUID.randomUUID();
		String name = "New Release Film";
		MovieType type = MovieType.NEW_RELEASE;
		int numberOfCopies = 5;
		
		this.rentalFixture
				.givenNoPriorActivity()
				.when(new CreateMovieCommand(movieId, name, type, numberOfCopies))
				.expectEvents(new MovieCreatedEvent(movieId, name, type, numberOfCopies));
	}
	
	@Test
	public void rentMovieCommandShouldThrowMovieRentedEvent () {
		
		UUID movieId = UUID.randomUUID();
		UUID rentalId = UUID.randomUUID();
		String name = "New Release Film";
		MovieType type = MovieType.NEW_RELEASE;
		int availableCopies = 3;
		int hireDays = 3;
		
		this.rentalFixture
				.given(new MovieCreatedEvent(movieId, name, type, availableCopies))
				.when(new RentMovieCommand(movieId, rentalId, hireDays))
				.expectEvents(new MovieRentedEvent(movieId, type, rentalId, hireDays,availableCopies-1, name));
	}
	
	@Test
	public void cannotRentMoreThanTheNumberOfCopies () {
		
		UUID movieId = UUID.randomUUID();
		String name = "New Release Film";
		MovieType type = MovieType.NEW_RELEASE;
		int availableCopies = 3;
		int hireDays = 3;
		
		this.rentalFixture
				.given(new MovieCreatedEvent(movieId, name, type, availableCopies),
						new MovieRentedEvent(movieId, type, UUID.randomUUID(), hireDays, 2, name),
						new MovieRentedEvent(movieId, type, UUID.randomUUID(), hireDays, 1, name),
						new MovieRentedEvent(movieId, type, UUID.randomUUID(), hireDays, 0, name))
				.when(new RentMovieCommand(movieId, UUID.randomUUID(), hireDays))
				.expectNoEvents()
				.expectException(MovieAggregate.UnavailableCopiesForRentException.class);
	}
	
	@Test
	public void returnMovieCommandShouldThrowMovieReturnedEvent () {
		
		UUID movieId = UUID.randomUUID();
		UUID rentalId = UUID.randomUUID();
		String name = "New Release Film";
		MovieType type = MovieType.NEW_RELEASE;
		int availableCopies = 3;
		int hireDays = 3;
		LocalDate now = LocalDate.now();
		
		this.rentalFixture
				.given(new MovieCreatedEvent(movieId, name, type, availableCopies),
						new MovieRentedEvent(movieId, type, rentalId, hireDays, 2, name))
				.when(new ReturnMovieCommand(movieId, rentalId, now))
				.expectEvents(new MovieReturnedEvent(movieId, rentalId, type, hireDays, now, availableCopies));
	}
	
	@Test
	public void returnMovieCommand_WhenNotRented_ShouldThrowMovieNotRentedException () {
		
		UUID movieId = UUID.randomUUID();
		UUID rentalId = UUID.randomUUID();
		String name = "New Release Film";
		MovieType type = MovieType.NEW_RELEASE;
		int availableCopies = 3;
		LocalDate now = LocalDate.now();
		
		this.rentalFixture
				.given(new MovieCreatedEvent(movieId, name, type, availableCopies))
				.when(new ReturnMovieCommand(movieId, rentalId, now))
				.expectNoEvents()
				.expectException(MovieAggregate.MovieNotRentedException.class);
	}
	
	@Test
	public void isAbleToRentAfterReturnAtLeastOneCopie () {
		
		UUID movieId = UUID.randomUUID();
		UUID rentalId1 = UUID.randomUUID();
		UUID rentalId2 = UUID.randomUUID();
		String name = "New Release Film";
		MovieType type = MovieType.NEW_RELEASE;
		int availableCopies = 3;
		int hireDays = 3;
		LocalDate now = LocalDate.now();
		
		this.rentalFixture
				.given(new MovieCreatedEvent(movieId, name, type, availableCopies),
						new MovieRentedEvent(movieId, type, UUID.randomUUID(), hireDays, 2, name),
						new MovieRentedEvent(movieId, type, UUID.randomUUID(), hireDays, 1, name),
						new MovieRentedEvent(movieId, type, rentalId1, hireDays, 0, name),
						new MovieReturnedEvent(movieId, rentalId1, type, hireDays, now, 1))
				.when(new RentMovieCommand(movieId, rentalId2, hireDays))
				.expectEvents(new MovieRentedEvent(movieId, type, rentalId2, hireDays, 0, name));
	}
	
	@Test
	public void cancelMovieRentalCommandShouldThrowMovieRentalCanceledEvent () {
		
		UUID movieId = UUID.randomUUID();
		UUID rentalId = UUID.randomUUID();
		String name = "New Release Film";
		MovieType type = MovieType.NEW_RELEASE;
		int availableCopies = 3;
		int hireDays = 3;
		
		this.rentalFixture
				.given(new MovieCreatedEvent(movieId, name, type, availableCopies),
						new MovieRentedEvent(movieId, type, rentalId, hireDays, availableCopies-1, name))
				.when(new CancelMovieRentalCommand(movieId, rentalId))
				.expectEvents(new MovieRentalCanceledEvent(movieId, rentalId, availableCopies));
	}
	
	@Test
	public void cancelMovieRentalCommand_WhenNotRented_ShouldNoThrowAnyEvent () {
		
		UUID movieId = UUID.randomUUID();
		UUID rentalId = UUID.randomUUID();
		String name = "New Release Film";
		MovieType type = MovieType.NEW_RELEASE;
		int availableCopies = 3;
		
		this.rentalFixture
				.given(new MovieCreatedEvent(movieId, name, type, availableCopies))
				.when(new CancelMovieRentalCommand(movieId, rentalId))
				.expectNoEvents();
	}
}
