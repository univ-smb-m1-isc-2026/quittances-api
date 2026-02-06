package com.nobless.quittances;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) /*Usage sans postrgesql*/
public class QuittancesApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuittancesApplication.class, args);
	}

}
