package com.casumo.videorentalstore.rest.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.casumo.videorentalstore.user.core.application.dto.User;
import com.casumo.videorentalstore.user.core.port.UserService;
import com.google.common.collect.Iterables;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/v1/users", produces = MediaTypes.HAL_JSON_VALUE)
@Api(value="users", tags="Users", description="Operations to manage users")
public class UsersController {

	private UserService userService;
	private UserResourceAssembler resourceAssembler;

	public UsersController(UserService userService, UserResourceAssembler resourceAssembler) {
		this.userService = userService;
		this.resourceAssembler = resourceAssembler;
	}

	@ApiOperation(value="Create a new user.", response = ResponseEntity.class)
	@PostMapping
	public ResponseEntity<Resource<User>> createUser(@RequestBody @Valid NewUser newUser)
			throws URISyntaxException {
		
		User user = new User (UUID.randomUUID(), newUser.getName());
		
		this.userService.createUser(user);

		Resource<User> userResource = resourceAssembler.toResource(user);

		return created(new URI(userResource.getId().expand().getHref()))
				.body(userResource);
	}

	@ApiOperation(value="View a list of available users.", response = ResponseEntity.class)
	@GetMapping
	public ResponseEntity<Resources<Resource<User>>> getAllUsers() {
		return ok(new Resources<Resource<User>>(
						Iterables.transform(this.userService.getAllUsers(), resourceAssembler::toResource),
						linkTo(methodOn(UsersController.class).getAllUsers()).withSelfRel()));
	}

	@ApiOperation(value="Search a user by Id.", response = ResponseEntity.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Resource<User>> getUserById(@PathVariable UUID id) {
		return this.userService.getUserById(id)
							   .map(resourceAssembler::toResource)
							   .map(ResponseEntity::ok)
							   .orElse(notFound().build());
	}
}
