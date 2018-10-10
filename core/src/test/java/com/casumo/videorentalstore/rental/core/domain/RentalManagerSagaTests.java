package com.casumo.videorentalstore.rental.core.domain;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.UUID;

import org.axonframework.test.saga.SagaTestFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.casumo.videorentalstore.catalog.core.domain.MovieType;
import com.casumo.videorentalstore.catalog.core.domain.event.MovieRentedEvent;
import com.casumo.videorentalstore.catalog.core.domain.event.MovieReturnedEvent;
import com.casumo.videorentalstore.rental.core.domain.RentalManagerSaga;
import com.casumo.videorentalstore.rental.core.domain.command.AddRentedMovieCommand;
import com.casumo.videorentalstore.rental.core.domain.command.ReturnRentedMovieCommand;
import com.casumo.videorentalstore.rental.core.domain.event.RentalCreatedEvent;
import com.casumo.videorentalstore.rental.core.domain.service.BillingService;
import com.casumo.videorentalstore.rental.core.domain.service.UnknownPriceFormulaForMovieTypeException;
import com.casumo.videorentalstore.user.core.domain.command.AddBonusPointsCommand;
import com.casumo.videorentalstore.user.core.domain.command.AddUserRentalCommand;

public class RentalManagerSagaTests {

	private SagaTestFixture<RentalManagerSaga> rentalManagerSagaFixture;

	@Before
	public void init() throws Exception {
		this.rentalManagerSagaFixture = new SagaTestFixture<>(RentalManagerSaga.class);
	}

	@Test
	public void givenRentalCreatedEvent_ShouldStartSaga() {

		UUID rentalId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		RentalStatus status = RentalStatus.OPEN;
		LocalDate now = LocalDate.now();

		this.rentalManagerSagaFixture
				.givenNoPriorActivity()
				.whenPublishingA(new RentalCreatedEvent(rentalId, userId, now, status))
				.expectActiveSagas(1)
				.expectAssociationWith("rentalId", rentalId)
				.expectDispatchedCommands(
						new AddUserRentalCommand(userId, rentalId));
	}

	@Test
	public void givenMovieRentedEvent_ShouldPublishRentalMovieAddedCommand()
			throws UnknownPriceFormulaForMovieTypeException {

		UUID rentalId = UUID.randomUUID();
		UUID movieId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		String name = "Teste";
		MovieType movieType = MovieType.NEW_RELEASE;
		int hireDays = 2;
		double chargeAmmount = 10;
		LocalDate now = LocalDate.now();
		RentalStatus status = RentalStatus.OPEN;
		int copiesLastToRent = 1;

		BillingService billingService = Mockito.mock(BillingService.class);

		this.rentalManagerSagaFixture.registerResource(billingService);

		when(billingService.getRentalChargeAmmountFor(movieType, 2)).thenReturn(chargeAmmount);

		this.rentalManagerSagaFixture.givenAPublished(new RentalCreatedEvent(rentalId, userId, now, status))
				.whenPublishingA(new MovieRentedEvent(movieId, movieType, rentalId, hireDays, copiesLastToRent, name))
				.expectDispatchedCommands(
						new AddRentedMovieCommand(rentalId, movieId, name, movieType, hireDays, chargeAmmount));
	}

	@Test
	public void givenMovieReturnedEvent_ShouldPublishReturRentedMovieCommandAndAddBonusPointsCommand() 
			throws UnknownPriceFormulaForMovieTypeException {

		UUID userId = UUID.randomUUID();
		UUID movieId = UUID.randomUUID();
		UUID rentalId = UUID.randomUUID();
		String name = "Teste";
		MovieType movieType = MovieType.NEW_RELEASE;
		int hireDays = 2;
		LocalDate now = LocalDate.now();
		double surchargeAmmount = 0;
		RentalStatus status = RentalStatus.OPEN;
		int copiesLastToRent = 1;
		int bonusAmmount = 1;
		
		BillingService billingService = Mockito.mock(BillingService.class);
		when(billingService.getReturnSurchargeAmmountFor(movieType, hireDays, now, now)).thenReturn(surchargeAmmount);
		when(billingService.getBonusAmmountFor(movieType, now, now)).thenReturn(bonusAmmount);
		this.rentalManagerSagaFixture.registerResource(billingService);

		this.rentalManagerSagaFixture
				.givenAPublished(new RentalCreatedEvent(rentalId, userId, now, status))
				.andThenAPublished(new MovieRentedEvent(
											movieId, 
											movieType, 
											rentalId, 
											hireDays,
											copiesLastToRent,
											name))
				.whenPublishingA(new MovieReturnedEvent(
											movieId, 
											rentalId, 
											movieType, 
											hireDays,
											now,
											copiesLastToRent))
				.expectDispatchedCommands(
						new ReturnRentedMovieCommand(rentalId, movieId, surchargeAmmount, now),
						new AddBonusPointsCommand (userId, bonusAmmount));
	}
	
	@Test
	public void givenMovieReturnedEvent_WithReturnDaysOverdue_ShouldPublishReturRentedMovieCommandWithSurchargeAndAddBonusPointsCommand() 
					throws UnknownPriceFormulaForMovieTypeException {

		UUID rentalId = UUID.randomUUID();
		UUID movieId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		String name = "Teste";
		MovieType movieType = MovieType.NEW_RELEASE;
		int hireDays = 2;
		double surchargeAmmount = 10;
		LocalDate rentalDate = LocalDate.parse("2018-10-05");
		LocalDate returnDate = LocalDate.parse("2018-10-08");
		RentalStatus status = RentalStatus.OPEN;
		int copiesLastToRent = 1;
		int bonusAmmount = 1;
		
		BillingService billingService = Mockito.mock(BillingService.class);
		when(billingService.getReturnSurchargeAmmountFor(movieType, hireDays, rentalDate, returnDate)).thenReturn(surchargeAmmount);
		when(billingService.getBonusAmmountFor(movieType, rentalDate, returnDate)).thenReturn(bonusAmmount);
		this.rentalManagerSagaFixture.registerResource(billingService);
		
		this.rentalManagerSagaFixture
				.givenAPublished(new RentalCreatedEvent(rentalId, userId, rentalDate, status))
				.andThenAPublished(new MovieRentedEvent(
											movieId, 
											movieType, 
											rentalId, 
											hireDays, 
											copiesLastToRent,
											name))
				.whenPublishingA(new MovieReturnedEvent(
											movieId, 
											rentalId, 
											movieType, 
											hireDays,
											returnDate,
											hireDays))
				.expectDispatchedCommands(
						new ReturnRentedMovieCommand(rentalId, movieId, surchargeAmmount, returnDate),
						new AddBonusPointsCommand (userId, bonusAmmount));
	}
}
