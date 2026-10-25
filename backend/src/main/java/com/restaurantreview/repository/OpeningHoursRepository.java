package com.restaurantreview.repository;

import com.restaurantreview.entities.OpeningHours;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpeningHoursRepository extends JpaRepository<OpeningHours, Long> {
}
