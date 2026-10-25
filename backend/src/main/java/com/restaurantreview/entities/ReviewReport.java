package com.restaurantreview.entities;

import java.time.Instant;

import com.restaurantreview.constants.ReportReason;
import com.restaurantreview.constants.ReportStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

/** A user's report against a review; one report per user per review. */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "review_reports")
public class ReviewReport extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "review_id", nullable = false)
	private Review review;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "reporter_id", nullable = false)
	private User reporter;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReportReason reason;

	@Column(columnDefinition = "TEXT")
	private String details;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReportStatus status = ReportStatus.OPEN;

	/** The admin who resolved or dismissed the report. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resolved_by")
	private User resolvedBy;

	@Column(name = "resolved_at")
	private Instant resolvedAt;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	public ReviewReport(Review review, User reporter, ReportReason reason, String details) {
		this.review = review;
		this.reporter = reporter;
		this.reason = reason;
		this.details = details;
	}

}
