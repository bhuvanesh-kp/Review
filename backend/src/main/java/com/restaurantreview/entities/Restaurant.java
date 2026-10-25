package com.restaurantreview.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "restaurants")
public class Restaurant extends AuditableEntity {

	@Column(nullable = false, length = 200)
	private String name;

	@Column(nullable = false, unique = true, length = 220)
	private String slug;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(length = 30)
	private String phone;

	private String website;

	@Column(name = "address_line")
	private String addressLine;

	@Column(nullable = false, length = 100)
	private String city;

	@Column(length = 100)
	private String state;

	@Column(name = "postal_code", length = 20)
	private String postalCode;

	@Column(nullable = false, length = 100)
	private String country;

	@Column(precision = 9, scale = 6)
	private BigDecimal latitude;

	@Column(precision = 9, scale = 6)
	private BigDecimal longitude;

	/** 1 (cheap) … 4 (expensive). */
	@JdbcTypeCode(SqlTypes.TINYINT)
	@Column(name = "price_level")
	private Short priceLevel;

	/** Denormalized: average of PUBLISHED review ratings, maintained by the review service. */
	@Column(name = "avg_rating", nullable = false, precision = 3, scale = 2)
	private BigDecimal avgRating = BigDecimal.ZERO;

	/** Denormalized: number of PUBLISHED reviews, maintained by the review service. */
	@Column(name = "review_count", nullable = false)
	private int reviewCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private User createdBy;

	@ManyToMany
	@JoinTable(name = "restaurant_cuisines",
			joinColumns = @JoinColumn(name = "restaurant_id"),
			inverseJoinColumns = @JoinColumn(name = "cuisine_id"))
	private Set<Cuisine> cuisines = new HashSet<>();

	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("dayOfWeek ASC")
	private List<OpeningHours> openingHours = new ArrayList<>();

	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("sortOrder ASC")
	private List<MenuCategory> menuCategories = new ArrayList<>();

	public Restaurant(String name, String slug, String city, String country) {
		this.name = name;
		this.slug = slug;
		this.city = city;
		this.country = country;
	}

	public void addOpeningHours(OpeningHours hours) {
		openingHours.add(hours);
		hours.setRestaurant(this);
	}

	public void addMenuCategory(MenuCategory category) {
		menuCategories.add(category);
		category.setRestaurant(this);
	}

}
