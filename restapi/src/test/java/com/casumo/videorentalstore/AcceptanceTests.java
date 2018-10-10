package com.casumo.videorentalstore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.casumo.videorentalstore.init.DBInit;
import com.casumo.videorentalstore.rental.core.persistence.RentalEntity;
import com.casumo.videorentalstore.rest.rental.NewRentalItem;
import com.casumo.videorentalstore.user.core.application.dto.User;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTests {

	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private CommandGateway commandGateway;
	
	@MockBean
    private Clock fixedClock;
	
	@Before
	public void Init() {
		when(this.fixedClock.getZone()).thenReturn(ZoneId.of("Z"));
		when(this.fixedClock.instant()).thenReturn(Instant.now());
		
		DBInit dbInit = new DBInit(commandGateway);
		dbInit.loadMovies();
		dbInit.loadUsers();
	}


	@Test
	public void shouldRentSeveralFilmsAndCalculatePrice() {

		// given
		UUID userId = UUID.fromString("0bc3f79b-8a0a-40a4-8463-222703c78b13");

		// The Dark Knight Rises :: Regular film :: 5 day rental :: 30 + 2 * 30
		// SEK
		NewRentalItem item1 = new NewRentalItem(UUID.fromString("ec5cbcd9-08b3-466e-8f76-a40c29bb1738"), 5);

		// The Godfather :: Old film :: 6 days rental :: 30 + 30 SEK
		NewRentalItem item2 = new NewRentalItem(UUID.fromString("92d6e75a-ee87-47ef-9178-8ee3f797d5ca"), 6);

		// Avengers: Infinity War :: New release film :: 2 days rental :: 2 * 40
		// SEK
		NewRentalItem item3 = new NewRentalItem(UUID.fromString("9bef5979-7249-44eb-ae5d-ef6d54cba9b4"), 2);

		double totalRentalCharge = (30 + 2 * 30) + (30 + 30) + (2 * 40);

		ResponseEntity<Object> response = restTemplate.postForEntity("/rentals", userId, null);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI newRentalLocation = response.getHeaders().getLocation();

		URI moviesURI = URI.create(newRentalLocation.toString() + "/movies");

		response = restTemplate.postForEntity(moviesURI, item1, null);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		response = restTemplate.postForEntity(moviesURI, item2, null);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		response = restTemplate.postForEntity(moviesURI, item3, null);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// When

		ResponseEntity<RentalEntity> getRentalResponse = restTemplate.getForEntity(newRentalLocation, RentalEntity.class);

		// Then

		assertThat(getRentalResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getRentalResponse.getBody().getRentedItems().stream()
						.mapToDouble(rental -> rental.getChargeAmmount()).sum()).isEqualTo(totalRentalCharge);
	}

	@Test
	public void shouldAllowReturnFilmsAndCalculateSurcharges() {
		// given
		UUID userId = UUID.fromString("0bc3f79b-8a0a-40a4-8463-222703c78b13");

		// The Dark Knight Rises :: Regular film :: 5 day rental :: 30 + 2 * 30
		// SEK
		UUID movieId = UUID.fromString("ec5cbcd9-08b3-466e-8f76-a40c29bb1738");
		NewRentalItem item = new NewRentalItem(movieId, 5);
		
		double totalSurcharge = 30; // 1 day more
		
		ResponseEntity<Object> response = restTemplate.postForEntity("/rentals", userId, null);
		URI newRentalLocation = response.getHeaders().getLocation();
		
		when(this.fixedClock.instant()).thenReturn(Instant.parse("2018-10-01T00:00:00.00Z"));
		
		URI moviesURI = URI.create(newRentalLocation.toString() + "/movies");
		response = restTemplate.postForEntity(moviesURI, item, null);
		
		when(this.fixedClock.instant()).thenReturn(Instant.parse("2018-10-07T00:00:00.00Z"));
		
		URI returnsURI = URI.create(newRentalLocation.toString() + "/returns");
		response = restTemplate.postForEntity(returnsURI, movieId, null);
		
		// When

		ResponseEntity<RentalEntity> getRentalResponse = restTemplate.getForEntity(newRentalLocation, RentalEntity.class);

		// Then

		assertThat(getRentalResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getRentalResponse.getBody().getReturns().stream()
						.mapToDouble(rental -> rental.getSurchargeAmmount()).sum()).isEqualTo(totalSurcharge);
	}
	
	@Test
	public void shoulIncreaseUserBonusAfterReturnRentedMovie() {
		// given
		UUID userId = UUID.fromString("0bc3f79b-8a0a-40a4-8463-222703c78b13");

		// The Dark Knight Rises :: Regular film :: 5 day rental :: 30 + 2 * 30
		// SEK
		UUID movieId = UUID.fromString("ec5cbcd9-08b3-466e-8f76-a40c29bb1738");
		NewRentalItem item = new NewRentalItem(movieId, 5);
		
		ResponseEntity<Object> response = restTemplate.postForEntity("/rentals", userId, null);
		URI newRentalLocation = response.getHeaders().getLocation();
		
		when(this.fixedClock.instant()).thenReturn(Instant.parse("2018-10-01T00:00:00.00Z"));
		
		URI moviesURI = URI.create(newRentalLocation.toString() + "/movies");
		response = restTemplate.postForEntity(moviesURI, item, null);
		
		when(this.fixedClock.instant()).thenReturn(Instant.parse("2018-10-07T00:00:00.00Z"));
		
		URI returnsURI = URI.create(newRentalLocation.toString() + "/returns");
		response = restTemplate.postForEntity(returnsURI, movieId, null);
		
		int totalBonusPoints = 1; // regular film
		
		// When

		ResponseEntity<User> getUserResponse = restTemplate.getForEntity("/users/{id}", User.class, userId);

		// Then

		assertThat(getUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getUserResponse.getBody().getBonusPoints()).isEqualTo(totalBonusPoints);
	}
}
