package com.casumo.videorentalstore.rest.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.UUID;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.casumo.videorentalstore.rest.rental.RentalsController;
import com.casumo.videorentalstore.user.core.application.dto.User;

@Component
public class UserResourceAssembler implements ResourceAssembler<User, Resource<User>> {

	@Override
	public Resource<User> toResource(User user) {

		Resource<User> movieResouce = new Resource<>(user,
				linkTo(methodOn(UsersController.class).getAllUsers()).withRel("users"),
				linkTo(methodOn(UsersController.class).getUserById(user.getId())).withSelfRel());

		for (UUID rentalId : user.getRentals()) {
			movieResouce.add(linkTo(methodOn(RentalsController.class).getRentalById(rentalId)).withRel("rentals"));
		}

		return movieResouce;
	}
}
