package com.casumo.videorentalstore.rental.core.application;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.casumo.videorentalstore.catalog.core.domain.command.RentMovieCommand;
import com.casumo.videorentalstore.catalog.core.domain.command.ReturnMovieCommand;
import com.casumo.videorentalstore.rental.core.application.dto.RentalItemRequest;
import com.casumo.videorentalstore.rental.core.domain.command.CreateRentalCommand;
import com.casumo.videorentalstore.rental.core.persistence.RentalEntity;
import com.casumo.videorentalstore.rental.core.persistence.RentalRepository;
import com.casumo.videorentalstore.rental.core.port.RentalService;

@Component
public class RentalServiceImpl implements RentalService {

	private final CommandGateway commandGateway;
	private final RentalRepository rentalRepository;
	private final Clock clock;

	@Autowired
	public RentalServiceImpl(CommandGateway commandGateway, RentalRepository rentalRepository, Clock clock) {
		this.commandGateway = commandGateway;
		this.rentalRepository = rentalRepository;
		this.clock = clock;
	}

	@Override
	public UUID createRental(UUID userId) {

		UUID rentalId = UUID.randomUUID();

		this.commandGateway.send(new CreateRentalCommand(rentalId, userId));

		return rentalId;
	}

	@Override
	public void addMovieToRental(UUID rentalId, RentalItemRequest rentalDetails) {
		if (rentalDetails != null) {
			this.commandGateway.send(
					new RentMovieCommand(
							rentalDetails.getMovieId(), 
							rentalId, 
							rentalDetails.getRentlDaysDuration(),
							LocalDate.now(clock)));
		}
	}

	@Override
	public Iterable<RentalEntity> getAllRentals() {
		return this.rentalRepository.findAll();
	}

	@Override
	public Optional<RentalEntity> getRentalById(UUID rentalId) {
		return this.rentalRepository.findById(rentalId);
	}

	@Override
	public void returnMovie(UUID rentalId, UUID movieId) {
		this.commandGateway.send(
				new ReturnMovieCommand(
						movieId, 
						rentalId, 
						LocalDate.now(clock)));
	}
}
