package com.restaurantreview.repository;

import com.restaurantreview.entities.ReviewPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewPhotoRepository extends JpaRepository<ReviewPhoto, Long> {
}
