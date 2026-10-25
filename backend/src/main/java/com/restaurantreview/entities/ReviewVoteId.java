package com.restaurantreview.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** Composite key of {@code review_votes}: one vote per user per review. */
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class ReviewVoteId implements Serializable {

	@Column(name = "review_id")
	private Long reviewId;

	@Column(name = "user_id")
	private Long userId;

}
