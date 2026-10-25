package com.restaurantreview.repository;

import java.util.List;

import com.restaurantreview.entities.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {

	List<MenuCategory> findByRestaurantIdOrderBySortOrderAsc(Long restaurantId);

}
