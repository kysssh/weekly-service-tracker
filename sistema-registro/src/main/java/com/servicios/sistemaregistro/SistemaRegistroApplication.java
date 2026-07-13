package com.servicios.sistemaregistro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // activa el motor de tareas programadas de Spring
public class SistemaRegistroApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaRegistroApplication.class, args);
	}

}
