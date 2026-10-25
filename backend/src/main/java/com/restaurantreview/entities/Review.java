package com.restaurantreview.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.restaurantreview.constants.ReviewStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/** One review per user per restaurant (enforced by a unique constraint). */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "reviews")
public class Review extends AuditableEntity {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	/** 1 … 5 stars. */
	@JdbcTypeCode(SqlTypes.TINYINT)
	@Column(nullable = false)
	private Short rating;

	@Column(length = 200)
	private String title;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String body;

	@Column(name = "visit_date")
	private LocalDate visitDate;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReviewStatus status = ReviewStatus.PUBLISHED;

	/** Denormalized: number of helpful votes, maintained by the vote service. */
	@Column(name = "helpful_count", nullable = false)
	private int helpfulCount;

	@OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("createdAt ASC")
	private List<ReviewPhoto> photos = new ArrayList<>();

	public Review(Restaurant restaurant, User user, Short rating, String body) {
		this.restaurant = restaurant;
		this.user = user;
		this.rating = rating;
		this.body = body;
	}

	public void addPhoto(ReviewPhoto photo) {
		photos.add(photo);
		photo.setReview(this);
	}

}
