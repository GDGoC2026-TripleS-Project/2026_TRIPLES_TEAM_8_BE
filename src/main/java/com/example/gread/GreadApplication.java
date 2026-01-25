package com.example.gread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class GreadApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreadApplication.class, args);
	}

}
