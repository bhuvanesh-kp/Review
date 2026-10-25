package com.restaurantreview.repository;

import java.util.List;

import com.restaurantreview.entities.RestaurantPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantPhotoRepository extends JpaRepository<RestaurantPhoto, Long> {

	List<RestaurantPhoto> findByRestaurantId(Long restaurantId);

}
