package com.casumo.videorentalstore;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

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
}
