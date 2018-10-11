package com.casumo.videorentalstore.rest.rental.util;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import org.springframework.stereotype.Component;

import com.casumo.videorentalstore.rental.core.application.dto.Rental;
import com.casumo.videorentalstore.rental.core.application.dto.RentalItem;
import com.casumo.videorentalstore.rental.core.application.dto.ReturnedItem;
import com.casumo.videorentalstore.rental.core.domain.RentalStatus;

@Component
public class RentalJsonConverter {
	
	public Rental fromJson(JsonObject json) {

		UUID rentalId = UUID.fromString(json.getString("rentalId"));
		UUID userId = UUID.fromString(json.getString("userId"));
		
		Collection<RentalItem> rentalItems = 
				json.getJsonArray("rentalItems")
					.stream()
					.map(value -> (JsonObject)value)
					.map(this::getRentalItemFromJsonObject)
					.collect(Collectors.toList());
		
		Collection<ReturnedItem> returnItems =
				json.getJsonArray("returnItems")
					.stream()
					.map(value -> (JsonObject)value)
					.map(this::getReturnItemFromJsonObject)
					.collect(Collectors.toList());
		RentalStatus rentalStatus = RentalStatus.valueOf(json.getString("rentalStatus"));
		LocalDate startDate = LocalDate.parse(json.getString("startDate"));

		return new Rental(rentalId, userId, rentalItems, returnItems, rentalStatus, startDate);
	}

	public JsonObject toJson(Rental rental) {
		
		JsonArrayBuilder jsonRentalItems = Json.createArrayBuilder();
		JsonArrayBuilder jsonReturnedItems = Json.createArrayBuilder();
		
		for (RentalItem i : rental.getRentalItems())
		{
			JsonObject jsonObj = Json.createObjectBuilder()
										  .add("movieId", i.getMovieId().toString())
										  .add("movieName", i.getMovieName())
										  .add("hireDaysDuration", i.getHireDaysDuration())
										  .add("chargeAmmount", String.valueOf(i.getChargeAmmount()))
										  .build();

			jsonRentalItems.add(jsonObj);
		}
		
		for (ReturnedItem i : rental.getReturnedItems())
		{
			JsonObject jsonObj = Json.createObjectBuilder()
										  .add("movieId", i.getMovieId().toString())
										  .add("movieName", i.getMovieName())
										  .add("returnDate", i.getReturnDate().toString())
										  .add("surchargeAmmount", String.valueOf(i.getSurchargeAmmount()))
										  .build();

			jsonReturnedItems.add(jsonObj);
		}
		
		return Json.createObjectBuilder()
						.add("rentalId", rental.getId().toString())
						.add("userId", rental.getUserId().toString())
						.add("rentalItems", jsonRentalItems.build())
						.add("returnItems", jsonReturnedItems.build())
						.add("rentalStatus", rental.getStatus().toString())
						.add("startDate", rental.getStartDate().toString())
						.build();
	}
	
	private RentalItem getRentalItemFromJsonObject (JsonObject jsonObj) {
		return new RentalItem(
						UUID.fromString(jsonObj.getString("movieId")), 
						jsonObj.getInt("hireDaysDuration"), 
						Double.valueOf(jsonObj.getString("chargeAmmount")),
						jsonObj.getString("movieName"));
	}
	
	private ReturnedItem getReturnItemFromJsonObject (JsonObject jsonObj) {
		return new ReturnedItem(
						UUID.fromString(jsonObj.getString("movieId")), 
						LocalDate.parse(jsonObj.getString("returnDate")), 
						Double.valueOf(jsonObj.getString("surchargeAmmount")),
						jsonObj.getString("movieName"));
	}
}
