package com.restaurantreview.repository;

import java.util.Optional;

import com.restaurantreview.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

	Optional<Restaurant> findBySlug(String slug);

	boolean existsBySlug(String slug);

}
