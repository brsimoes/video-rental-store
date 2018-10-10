package com.casumo.videorentalstore.rest.rental;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.casumo.videorentalstore.rental.core.application.dto.Return;
import com.casumo.videorentalstore.rest.catalog.CatalogController;

@Component
public class ReturnItemResourceAssembler implements ResourceAssembler<Return, Resource<Return>> {

	@Override
	public Resource<Return> toResource(Return item) {

		return new Resource<>(item,
					linkTo(methodOn(CatalogController.class).getMovieById(item.getMovieId())).withRel("movie"));
	}
}
