package com.restaurantreview.entities;

import java.time.Instant;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

/** A "helpful" vote. Changes must update {@link Review#getHelpfulCount()} in the same transaction. */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "review_votes")
public class ReviewVote {

	@EmbeddedId
	private ReviewVoteId id;

	@MapsId("reviewId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "review_id")
	private Review review;

	@MapsId("userId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	public ReviewVote(Review review, User user) {
		this.id = new ReviewVoteId(review.getId(), user.getId());
		this.review = review;
		this.user = user;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		return id != null && Objects.equals(id, ((ReviewVote) o).getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

}
