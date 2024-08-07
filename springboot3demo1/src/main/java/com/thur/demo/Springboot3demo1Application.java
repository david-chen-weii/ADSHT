package com.thur.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = { "com.thur.demo.model.user", "com.thur.demo.model.post", "com.thur.demo.model" })
public class Springboot3demo1Application {

	public static void main(String[] args) {
		SpringApplication.run(Springboot3demo1Application.class, args);
	}

}
