package ru.misis;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Volunteering API",
				version = "0.0.1 beta"
		)
)
public class VolunteeringApplication {

	public static void main(String[] args) {
		SpringApplication.run(VolunteeringApplication.class, args);
	}

}
