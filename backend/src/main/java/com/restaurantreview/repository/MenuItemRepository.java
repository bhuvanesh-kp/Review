package com.restaurantreview.repository;

import com.restaurantreview.entities.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}
