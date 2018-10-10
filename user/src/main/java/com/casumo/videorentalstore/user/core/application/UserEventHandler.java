package com.casumo.videorentalstore.user.core.application;

import java.util.HashSet;
import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.casumo.videorentalstore.user.core.domain.event.BonusPointsUpdatedEvent;
import com.casumo.videorentalstore.user.core.domain.event.UserCreatedEvent;
import com.casumo.videorentalstore.user.core.domain.event.UserRentalAddedEvent;
import com.casumo.videorentalstore.user.core.persistence.UserEntity;
import com.casumo.videorentalstore.user.core.persistence.UserRepository;

@Component
public class UserEventHandler {

	private final UserRepository repository;

	public UserEventHandler(UserRepository repository) {
		this.repository = repository;
	}

	@EventHandler
	public void handle(UserCreatedEvent evt) {
		this.repository.save(new UserEntity(evt.getUserId(), evt.getName(), 0, new HashSet<>()));
	}
	
	@EventHandler
	public void handle(BonusPointsUpdatedEvent event) {
		Optional<UserEntity> user = this.repository.findById(event.getUserId());
		if (user.isPresent()) {
			user.get().setBonusPoints(event.getBonusPoints());
			this.repository.save(user.get());
		}
	}
	
	@EventHandler
	public void handle(UserRentalAddedEvent event) {
		Optional<UserEntity> user = this.repository.findById(event.getUserId());
		if (user.isPresent()) {
			user.get().addRental(event.getRentalId());
			this.repository.save(user.get());
		}
	}
}
