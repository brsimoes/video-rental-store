package com.casumo.videorentalstore.user.core.application;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;

import com.casumo.videorentalstore.user.core.application.dto.User;
import com.casumo.videorentalstore.user.core.domain.command.CreateUserCommand;
import com.casumo.videorentalstore.user.core.persistence.UserEntity;
import com.casumo.videorentalstore.user.core.persistence.UserRepository;
import com.casumo.videorentalstore.user.core.port.UserService;

@Component
public class UserServiceImpl implements UserService {

	private final CommandGateway commandGateway;
	private final UserRepository userRepository;
	
	public UserServiceImpl(
				CommandGateway commandGateway, 
				UserRepository userRepository) {
		
		this.commandGateway = commandGateway;
		this.userRepository = userRepository;
	}
	
	@Override
	public void createUser(User user) {
		this.commandGateway.send(
				new CreateUserCommand(
						user.getId(),
						user.getName()));
	}

	@Override
	public Iterable<User> getAllUsers() {
		return this.userRepository.findAll()
								  .stream()
								  .map(this::toUserDto)
								  .collect(Collectors.toList());
	}
	
	@Override
	public Optional<User> getUserById(UUID id) {
		return this.userRepository.findById(id)
								  .map(this::toUserDto);
	}
	
	private User toUserDto (UserEntity user) {
		return new User (
					user.getId(), 
					user.getName(), 
					user.getBonusPoints(), 
					user.getRentals());
	}
}
