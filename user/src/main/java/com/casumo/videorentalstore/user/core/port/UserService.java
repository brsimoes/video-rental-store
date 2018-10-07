package com.casumo.videorentalstore.user.core.port;

import java.util.Optional;
import java.util.UUID;

import com.casumo.videorentalstore.user.core.application.dto.User;

public interface UserService {
	
	void createUser(UUID userId, String name);
	
	public Iterable<User> getAllUsers();
	
	public Optional<User> getUserById(UUID id);
}
