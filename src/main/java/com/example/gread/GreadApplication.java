package com.example.gread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GreadApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreadApplication.class, args);
	}

}
