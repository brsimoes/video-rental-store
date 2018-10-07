package com.casumo.videorentalstore.rental.core.domain;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.UUID;

import org.axonframework.test.saga.SagaTestFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.casumo.videorentalstore.catalog.core.domain.event.MovieRentedEvent;
import com.casumo.videorentalstore.catalog.core.domain.event.MovieReturnedEvent;
import com.casumo.videorentalstore.enums.MovieType;
import com.casumo.videorentalstore.enums.RentalStatus;
import com.casumo.videorentalstore.rental.core.domain.RentalManagerSaga;
import com.casumo.videorentalstore.rental.core.domain.command.AddRentedMovieCommand;
import com.casumo.videorentalstore.rental.core.domain.command.ReturnRentedMovieCommand;
import com.casumo.videorentalstore.rental.core.domain.event.RentalCreatedEvent;
import com.casumo.videorentalstore.rental.core.domain.service.BillingService;
import com.casumo.videorentalstore.rental.core.domain.service.UnknownPriceFormulaForMovieTypeException;

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

		this.rentalManagerSagaFixture
				.givenNoPriorActivity().whenPublishingA(new RentalCreatedEvent(rentalId, userId, status))
				.expectActiveSagas(1).expectAssociationWith("rentalId", rentalId).expectNoDispatchedCommands();
	}

	@Test
	public void givenMovieRentedEvent_ShouldPublishRentalMovieAddedCommand()
			throws UnknownPriceFormulaForMovieTypeException {

		UUID rentalId = UUID.randomUUID();
		UUID movieId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		MovieType movieType = MovieType.NEW_RELEASE;
		int hireDays = 2;
		double chargeAmmount = 10;
		LocalDate now = LocalDate.now();
		RentalStatus status = RentalStatus.OPEN;
		int copiesLastToRent = 1;

		BillingService billingService = Mockito.mock(BillingService.class);

		this.rentalManagerSagaFixture.registerResource(billingService);

		when(billingService.getRentalChargeAmmountFor(movieType, 2)).thenReturn(chargeAmmount);

		this.rentalManagerSagaFixture.givenAPublished(new RentalCreatedEvent(rentalId, userId, status))
				.whenPublishingA(new MovieRentedEvent(movieId, movieType, rentalId, hireDays, now, copiesLastToRent))
				.expectDispatchedCommands(
						new AddRentedMovieCommand(rentalId, movieId, hireDays, chargeAmmount, movieType));
	}

	@Test
	public void givenMovieReturnedEvent_ShouldPublishReturRentedMovieCommand() throws UnknownPriceFormulaForMovieTypeException {

		UUID rentalId = UUID.randomUUID();
		UUID movieId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		MovieType movieType = MovieType.NEW_RELEASE;
		int hireDays = 2;
		LocalDate now = LocalDate.now();
		double surchargeAmmount = 0;
		RentalStatus status = RentalStatus.OPEN;
		int copiesLastToRent = 1;
		
		BillingService billingService = Mockito.mock(BillingService.class);
		when(billingService.getReturnSurchargeAmmountFor(movieType, hireDays, now, now)).thenReturn(surchargeAmmount);
		this.rentalManagerSagaFixture.registerResource(billingService);

		this.rentalManagerSagaFixture
				.givenAPublished(new RentalCreatedEvent(rentalId, userId, status))
				.andThenAPublished(new MovieRentedEvent(
											movieId, 
											movieType, 
											rentalId, 
											hireDays, 
											now,
											copiesLastToRent))
				.whenPublishingA(new MovieReturnedEvent(
											movieId, 
											rentalId, 
											movieType, 
											now,
											now,
											hireDays))
				.expectDispatchedCommands(
						new ReturnRentedMovieCommand(rentalId, movieId, surchargeAmmount, now));
	}
	
	@Test
	public void givenMovieReturnedEvent_WithReturnDaysOverdue_ShouldPublishReturRentedMovieCommandWithSurcharge() 
					throws UnknownPriceFormulaForMovieTypeException {

		UUID rentalId = UUID.randomUUID();
		UUID movieId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		MovieType movieType = MovieType.NEW_RELEASE;
		int hireDays = 2;
		double surchargeAmmount = 10;
		LocalDate rentalDate = LocalDate.parse("2018-10-05");
		LocalDate returnDate = LocalDate.parse("2018-10-08");
		RentalStatus status = RentalStatus.OPEN;
		int copiesLastToRent = 1;
		
		
		
		BillingService billingService = Mockito.mock(BillingService.class);
		when(billingService.getReturnSurchargeAmmountFor(movieType, hireDays, rentalDate, returnDate)).thenReturn(surchargeAmmount);
		this.rentalManagerSagaFixture.registerResource(billingService);
		
		this.rentalManagerSagaFixture
				.givenAPublished(new RentalCreatedEvent(rentalId, userId, status))
				.andThenAPublished(new MovieRentedEvent(
											movieId, 
											movieType, 
											rentalId, 
											hireDays, 
											rentalDate,
											copiesLastToRent))
				.whenPublishingA(new MovieReturnedEvent(
											movieId, 
											rentalId, 
											movieType, 
											rentalDate,
											returnDate,
											hireDays))
				.expectDispatchedCommands(
						new ReturnRentedMovieCommand(rentalId, movieId, surchargeAmmount, returnDate));
	}
}
