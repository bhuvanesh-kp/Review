package com.restaurantreview.entities;

import java.time.Instant;

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
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "restaurant_photos")
public class RestaurantPhoto extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uploaded_by")
	private User uploadedBy;

	/** Location returned by the StorageService. */
	@Column(nullable = false, length = 512)
	private String url;

	private String caption;

	/** At most one cover photo per restaurant (enforced in the photo service). */
	@Column(name = "is_cover", nullable = false)
	private boolean cover;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	public RestaurantPhoto(Restaurant restaurant, User uploadedBy, String url) {
		this.restaurant = restaurant;
		this.uploadedBy = uploadedBy;
		this.url = url;
	}

}
