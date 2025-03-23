package com.example.laundryproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.laundryproject")
public class LaundryprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(LaundryprojectApplication.class, args);
	}

}
