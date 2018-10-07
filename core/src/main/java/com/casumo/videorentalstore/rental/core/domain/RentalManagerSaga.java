package com.casumo.videorentalstore.rental.core.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.saga.SagaEventHandler;
import org.axonframework.eventhandling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.casumo.videorentalstore.catalog.core.domain.command.CancelMovieRentalCommand;
import com.casumo.videorentalstore.catalog.core.domain.event.MovieRentedEvent;
import com.casumo.videorentalstore.catalog.core.domain.event.MovieReturnedEvent;
import com.casumo.videorentalstore.rental.core.domain.command.AddRentedMovieCommand;
import com.casumo.videorentalstore.rental.core.domain.command.ReturnRentedMovieCommand;
import com.casumo.videorentalstore.rental.core.domain.event.RentalCreatedEvent;
import com.casumo.videorentalstore.rental.core.domain.service.BillingService;
import com.casumo.videorentalstore.rental.core.domain.service.UnknownPriceFormulaForMovieTypeException;
import com.casumo.videorentalstore.user.core.domain.command.AddBonusPointsCommand;

@Saga
public class RentalManagerSaga {
	private static final Logger logger = LoggerFactory.getLogger(RentalManagerSaga.class);
	
	private UUID userId;
	private Map<UUID,Integer> initialHireDaysByMovie;

	@Autowired
	private transient CommandGateway commandGateway;
	@Autowired
	private transient BillingService billingService;

	@StartSaga(forceNew = true)
	@SagaEventHandler(associationProperty = "rentalId")
	public void handle(RentalCreatedEvent event) {
		this.initialHireDaysByMovie = new HashMap<>();
		this.userId = event.getUserId();
		
		logger.debug("A new rental is in place. Id : {}, UserId: {}", event.getRentalId(), event.getUserId());
	}

	@SagaEventHandler(associationProperty = "rentalId")
	public void on(MovieRentedEvent event) {
		try {

			double movieRentalchargeAmmount = this.billingService.getRentalChargeAmmountFor(event.getMovieType(),
					event.getHireDays());

			this.commandGateway.send(new AddRentedMovieCommand(event.getRentalId(), event.getMovieId(),
					event.getHireDays(), movieRentalchargeAmmount, event.getMovieType()));
			
			this.initialHireDaysByMovie.put(event.getMovieId(), event.getHireDays());

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
														event.getRentalDate(),
														event.getReturnDate());
			
			bonusAmmount = this.billingService.getBonusAmmountFor(
														event.getMovieType(), 
														event.getRentalDate(),
														event.getReturnDate());
		
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
	}
}
