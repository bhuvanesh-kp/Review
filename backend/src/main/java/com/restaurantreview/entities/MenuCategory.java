package com.restaurantreview.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "menu_categories")
public class MenuCategory extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(name = "sort_order", nullable = false)
	private int sortOrder;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("name ASC")
	private List<MenuItem> items = new ArrayList<>();

	public MenuCategory(String name, int sortOrder) {
		this.name = name;
		this.sortOrder = sortOrder;
	}

	public void addItem(MenuItem item) {
		items.add(item);
		item.setCategory(this);
	}

}
