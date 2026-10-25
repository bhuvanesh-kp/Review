package com.restaurantreview.repository;

import java.util.Optional;

import com.restaurantreview.entities.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuisineRepository extends JpaRepository<Cuisine, Long> {

	Optional<Cuisine> findByName(String name);

}
