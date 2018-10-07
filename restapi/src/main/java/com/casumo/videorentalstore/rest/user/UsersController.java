package com.casumo.videorentalstore.rest.user;

import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.casumo.videorentalstore.user.core.application.dto.User;
import com.casumo.videorentalstore.user.core.port.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/users")
@Api(value="users", tags="Users", description="Operations to manage users")
public class UsersController {

	@Autowired
	private UserService userService;

	@ApiOperation(value="Create a new user.")
	@SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity createUser(@RequestBody @Valid NewUser user, UriComponentsBuilder builder) {

		UUID userId = UUID.randomUUID();
		
		this.userService.createUser(userId, user.getName());

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("/v1/users/{userId}").buildAndExpand(userId).toUri());

		return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
	}

	@ApiOperation(value="View a list of available users.", response=Iterable.class)
	@RequestMapping(method = RequestMethod.GET)
	public Iterable<User> getAllRentals() {
		Iterable<User> rentals = this.userService.getAllUsers();

		return rentals;
	}

	@ApiOperation(value="Search a user by Id.", response=User.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> getUserById(@PathVariable UUID id) {
		Optional<User> user = this.userService.getUserById(id);

		HttpStatus status = user.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
				
		return new ResponseEntity<>(user.get(), null, status);
	}
}
