package com.casumo.videorentalstore.rental.core.application;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.casumo.videorentalstore.catalog.core.domain.command.RentMovieCommand;
import com.casumo.videorentalstore.catalog.core.domain.command.ReturnMovieCommand;
import com.casumo.videorentalstore.rental.core.application.dto.Rental;
import com.casumo.videorentalstore.rental.core.application.dto.RentalItem;
import com.casumo.videorentalstore.rental.core.application.dto.ReturnedItem;
import com.casumo.videorentalstore.rental.core.domain.RentalAggregate.InvalidStatusException;
import com.casumo.videorentalstore.rental.core.domain.RentalStatus;
import com.casumo.videorentalstore.rental.core.domain.command.ChangeRentalStatusCommand;
import com.casumo.videorentalstore.rental.core.domain.command.CreateRentalCommand;
import com.casumo.videorentalstore.rental.core.persistence.RentalEntity;
import com.casumo.videorentalstore.rental.core.persistence.RentalItemDetail;
import com.casumo.videorentalstore.rental.core.persistence.ReturnItemDetail;
import com.casumo.videorentalstore.rental.core.persistence.RentalRepository;
import com.casumo.videorentalstore.rental.core.port.RentalService;

@Component
public class RentalServiceImpl implements RentalService {

	private final CommandGateway commandGateway;
	private final RentalRepository rentalRepository;

	@Autowired
	public RentalServiceImpl(
				CommandGateway commandGateway, 
				RentalRepository rentalRepository) {
		
		this.commandGateway = commandGateway;
		this.rentalRepository = rentalRepository;
	}

	@Override
	public void createRental(Rental rental) {
		this.commandGateway.send(
				new CreateRentalCommand(
						rental.getId(), 
						rental.getId(), 
						rental.getStartDate()));
	}

	@Override
	public void addMovieToRental(UUID rentalId, UUID movieId, int numberOfDaysToHire) {
		this.commandGateway.send(
				new RentMovieCommand(
						movieId, 
						rentalId, 
						numberOfDaysToHire));
	}

	@Override
	public Iterable<Rental> getAllRentals() {
		return this.rentalRepository.findAll()
									.stream()
									.map(this::toRentalDto)
									.collect(Collectors.toList());
	}

	@Override
	public Optional<Rental> getRentalById(UUID rentalId) {
		return this.rentalRepository.findById(rentalId)
									.map(this::toRentalDto);
	}

	@Override
	public void returnMovie(UUID rentalId, UUID movieId, LocalDate returnDate) {
		this.commandGateway.send(
				new ReturnMovieCommand(
						movieId, 
						rentalId, 
						returnDate));
	}
	
	@Override
	public Optional<RentalItem> getRentalMovie(UUID rentalId, UUID movieId) {
		return this.rentalRepository.findById(rentalId)
				.flatMap(i -> i.getRentedItems().stream().filter(r -> r.getMovieId().equals(movieId)).findFirst())
				.map(this::toRentalItemDto);
	}

	@Override
	public Optional<ReturnedItem> getReturnMovie(UUID rentalId, UUID movieId) {
		return this.rentalRepository.findById(rentalId)
				.flatMap(i -> i.getReturns().stream().filter(r -> r.getMovieId().equals(movieId)).findFirst())
				.map(this::toReturnItemDto);
	}


	@Override
	public void updateRentalStatus(UUID rentalId, RentalStatus rentalStatus) 
			throws InvalidStatusException {
		this.commandGateway.send(
				new ChangeRentalStatusCommand(
						rentalId, 
						rentalStatus));
	}
	
	private Rental toRentalDto (RentalEntity rental) {
		
		Collection<RentalItem> rentalItems =
				rental.getRentedItems()
					  .stream()
					  .map( item -> new RentalItem(
					  						item.getMovieId(), 
					  						item.getHireDaysDuration(),
					  						item.getChargeAmmount(),
					  						item.getMovieName()))
					  .collect(Collectors.toList());
		
		Collection<ReturnedItem> returns =
				rental.getReturns()
					  .stream()
					  .map( item -> new ReturnedItem(
					  						item.getMovieId(), 
					  						item.getReturnDate(),
					  						item.getSurchargeAmmount(),
					  						item.getMovieName()))
					  .collect(Collectors.toList());
		
		return new Rental (
					rental.getId(),
					rental.getUserId(),
					rentalItems, 
					returns, 
					rental.getStatus(), 
					rental.getStartDate());
	}
	
	private RentalItem toRentalItemDto (RentalItemDetail item) {
		
		return new RentalItem(
					item.getMovieId(), 
					item.getHireDaysDuration(),
					item.getChargeAmmount(),
					item.getMovieName());
	}
	
	private ReturnedItem toReturnItemDto (ReturnItemDetail item) {
			
		return new ReturnedItem(
					item.getMovieId(), 
					item.getReturnDate(),
					item.getSurchargeAmmount(),
					item.getMovieName());
	}
}
