package com.restaurantreview;

import org.springframework.boot.SpringApplication;

public class TestRestaurantReviewApplication {

	public static void main(String[] args) {
		SpringApplication.from(RestaurantReviewApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
