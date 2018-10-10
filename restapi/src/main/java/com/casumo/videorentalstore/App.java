package com.casumo.videorentalstore;

import java.time.Clock;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.casumo.videorentalstore.init.DBInit;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(App.class, args);

		CommandGateway commandGateway = context.getBean(CommandGateway.class);

		DBInit dbInit = new DBInit(commandGateway);
		
		dbInit.loadMovies();
		
		dbInit.loadUsers();
		
		dbInit.loadRentals();
	}

	@Bean
	public Clock clock() {
		return Clock.systemDefaultZone();
	}
}
