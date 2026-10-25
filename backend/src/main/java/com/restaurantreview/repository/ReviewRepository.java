package com.restaurantreview.repository;

import com.restaurantreview.constants.ReviewStatus;
import com.restaurantreview.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	Page<Review> findByRestaurantIdAndStatus(Long restaurantId, ReviewStatus status, Pageable pageable);

	boolean existsByRestaurantIdAndUserId(Long restaurantId, Long userId);

}
