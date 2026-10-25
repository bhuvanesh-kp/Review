package com.restaurantreview.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cuisines")
public class Cuisine extends BaseEntity {

	@Column(nullable = false, unique = true, length = 50)
	private String name;

	public Cuisine(String name) {
		this.name = name;
	}

}
