package com.casumo.videorentalstore.rental.core.domain;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.saga.SagaEventHandler;
import org.axonframework.eventhandling.saga.SagaLifecycle;
import org.axonframework.eventhandling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.casumo.videorentalstore.catalog.core.domain.command.CancelMovieRentalCommand;
import com.casumo.videorentalstore.catalog.core.domain.event.MovieRentedEvent;
import com.casumo.videorentalstore.catalog.core.domain.event.MovieReturnedEvent;
import com.casumo.videorentalstore.rental.core.domain.command.AddRentedMovieCommand;
import com.casumo.videorentalstore.rental.core.domain.command.ChangeRentalStatusCommand;
import com.casumo.videorentalstore.rental.core.domain.command.ReturnRentedMovieCommand;
import com.casumo.videorentalstore.rental.core.domain.event.RentalCreatedEvent;
import com.casumo.videorentalstore.rental.core.domain.event.RentalStatusChangedEvent;
import com.casumo.videorentalstore.rental.core.domain.service.BillingService;
import com.casumo.videorentalstore.rental.core.domain.service.UnknownPriceFormulaForMovieTypeException;
import com.casumo.videorentalstore.user.core.domain.command.AddBonusPointsCommand;
import com.casumo.videorentalstore.user.core.domain.command.AddUserRentalCommand;

@Saga
public class RentalManagerSaga {
	private static final Logger logger = LoggerFactory.getLogger(RentalManagerSaga.class);
	
	private UUID userId;
	private Map<UUID,Integer> initialHireDaysByMovie;
	private LocalDate rentalStartDate;
	private Collection<UUID> moviesToBeReturned;

	@Autowired
	private transient CommandGateway commandGateway;
	@Autowired
	private transient BillingService billingService;

	@StartSaga(forceNew = true)
	@SagaEventHandler(associationProperty = "rentalId")
	public void handle(RentalCreatedEvent event) {
		this.initialHireDaysByMovie = new HashMap<>();
		this.moviesToBeReturned = new HashSet<>();
		this.userId = event.getUserId();
		this.rentalStartDate = event.getStartDate();
		
		this.commandGateway.send(new AddUserRentalCommand(event.getUserId(), event.getRentalId()));
		
		logger.debug("A new rental is in place. Id : {}, UserId: {}", event.getRentalId(), event.getUserId());
	}

	@SagaEventHandler(associationProperty = "rentalId")
	public void on(MovieRentedEvent event) {
		try {

			double movieRentalchargeAmmount = this.billingService.getRentalChargeAmmountFor(event.getMovieType(),
					event.getNumberOfDaysHired());

			this.commandGateway.send(new AddRentedMovieCommand(
											event.getRentalId(), 
											event.getMovieId(), 
											event.getMovieName(), 
											event.getMovieType(),
											event.getNumberOfDaysHired(), 
											movieRentalchargeAmmount));
			
			this.initialHireDaysByMovie.put(event.getMovieId(), event.getNumberOfDaysHired());
			
			this.moviesToBeReturned.add(event.getMovieId());

		} catch (UnknownPriceFormulaForMovieTypeException e) {

			this.commandGateway.send(new CancelMovieRentalCommand(event.getMovieId(), event.getRentalId()));
		}
	}

	@SagaEventHandler(associationProperty = "rentalId")
	public void on(MovieReturnedEvent event) {
		double surchargeAmmount = 0;
		int bonusAmmount = 0;
		
		try {
			
			int initialHireDays = this.initialHireDaysByMovie.getOrDefault(event.getMovieId(), 0);
			
			surchargeAmmount = this.billingService.getReturnSurchargeAmmountFor(
														event.getMovieType(),
														initialHireDays,
														this.rentalStartDate,
														event.getReturnDate());
			
			bonusAmmount = this.billingService.getBonusAmmountFor(
														event.getMovieType(), 
														this.rentalStartDate,
														event.getReturnDate());
			
			this.moviesToBeReturned.remove(event.getMovieId());
		
		} catch (UnknownPriceFormulaForMovieTypeException e) {
			logger.error("Unable to calculate return surcharge and bonus on rental {} for movie {}.",
							event.getRentalId(), event.getMovieId());
		}
			
		this.commandGateway.send( new ReturnRentedMovieCommand(
											event.getRentalId(),
											event.getMovieId(),
											surchargeAmmount,
											event.getReturnDate()));
		
		this.commandGateway.send( new AddBonusPointsCommand ( this.userId, bonusAmmount));
		
		if (this.moviesToBeReturned.size() == 0) {
			this.commandGateway.send( new ChangeRentalStatusCommand(event.getRentalId(), RentalStatus.CLOSED));
		}
	}
	
	@SagaEventHandler(associationProperty = "rentalId")
	public void on(RentalStatusChangedEvent  event) {
		if (event.getNewStatus().isCanceled() || event.getNewStatus().isClosed()  ) {
			SagaLifecycle.end();
		}
	}
}
