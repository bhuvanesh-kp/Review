package com.restaurantreview.entities;

import java.time.LocalTime;

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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "opening_hours")
public class OpeningHours extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	/** 0 = Monday … 6 = Sunday. */
	@JdbcTypeCode(SqlTypes.TINYINT)
	@Column(name = "day_of_week", nullable = false)
	private Short dayOfWeek;

	@Column(name = "open_time")
	private LocalTime openTime;

	@Column(name = "close_time")
	private LocalTime closeTime;

	/** When true the open/close times are ignored. */
	@Column(nullable = false)
	private boolean closed;

	public OpeningHours(Short dayOfWeek, LocalTime openTime, LocalTime closeTime) {
		this.dayOfWeek = dayOfWeek;
		this.openTime = openTime;
		this.closeTime = closeTime;
	}

}
