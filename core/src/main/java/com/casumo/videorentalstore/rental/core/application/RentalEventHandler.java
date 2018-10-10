package com.casumo.videorentalstore.rental.core.application;

import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.casumo.videorentalstore.rental.core.domain.event.RentalCreatedEvent;
import com.casumo.videorentalstore.rental.core.domain.event.RentalStatusChangedEvent;
import com.casumo.videorentalstore.rental.core.domain.event.RentedMovieReturnedEvent;
import com.casumo.videorentalstore.rental.core.domain.event.RentedMovieAddedEvent;
import com.casumo.videorentalstore.rental.core.persistence.RentalEntity;
import com.casumo.videorentalstore.rental.core.persistence.RentalRepository;
import com.casumo.videorentalstore.rental.core.persistence.ReturnItemDetail;

@Component
public class RentalEventHandler {

	private final RentalRepository repository;

	public RentalEventHandler(RentalRepository repository) {
		this.repository = repository;
	}

	@EventHandler
	public void handle(RentalCreatedEvent evt) {
		RentalEntity rental = new RentalEntity(
										evt.getRentalId(), 
										evt.getUserId(), 
										evt.getStartDate());
		
		this.repository.save(rental);
	}

	@EventHandler
	public void handle(RentedMovieAddedEvent evt) {
		Optional<RentalEntity> rental = this.repository.findById(evt.getRentalId());
		if (rental.isPresent()) {
			rental.get().addRentalItem(
							evt.getMovieId(), 
							evt.getMovieName(), 
							evt.getHireDays(), 
							evt.getChargeAmmount());
			
			this.repository.save(rental.get());
		}
	}
	
	@EventHandler
	public void handle(RentedMovieReturnedEvent evt) {
		Optional<RentalEntity> rental = this.repository.findById(evt.getRentalId());
		if (rental.isPresent()) {
			rental.get().addReturn(
							new ReturnItemDetail(
									evt.getMovieId(), 
									evt.getReturnDate(), 
									evt.getSurchargeAmmount(),
									evt.getMovieName()));
			
			this.repository.save(rental.get());
		}
	}
	
	@EventHandler
	public void handle(RentalStatusChangedEvent evt) {
		Optional<RentalEntity> rental = this.repository.findById(evt.getRentalId());
		if (rental.isPresent()) {
			rental.get().setStatus(evt.getNewStatus());
			
			this.repository.save(rental.get());
		}
	}
}
