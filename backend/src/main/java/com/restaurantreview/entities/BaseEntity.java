package com.restaurantreview.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.Hibernate;

/**
 * Surrogate {@code BIGINT AUTO_INCREMENT} key with id-based equality that is safe for Hibernate proxies.
 */
@Getter
@MappedSuperclass
public abstract class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		Long otherId = ((BaseEntity) o).getId();
		return id != null && id.equals(otherId);
	}

	@Override
	public int hashCode() {
		return Hibernate.getClass(this).hashCode();
	}

}
