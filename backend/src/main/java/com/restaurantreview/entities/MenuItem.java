package com.restaurantreview.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "menu_items")
public class MenuItem extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_id", nullable = false)
	private MenuCategory category;

	@Column(nullable = false, length = 150)
	private String name;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal price;

	@Column(nullable = false)
	private boolean vegetarian;

	@Column(nullable = false)
	private boolean available = true;

	@Column(name = "image_url", length = 512)
	private String imageUrl;

	public MenuItem(String name, BigDecimal price) {
		this.name = name;
		this.price = price;
	}

}
