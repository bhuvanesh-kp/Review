package com.restaurantreview.entities;

import com.restaurantreview.constants.Role;
import com.restaurantreview.constants.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends AuditableEntity {

	@Column(nullable = false, unique = true)
	private String email;

	@Column(name = "password_hash", nullable = false)
	private String passwordHash;

	@Column(name = "display_name", nullable = false, length = 100)
	private String displayName;

	@Column(name = "avatar_url", length = 512)
	private String avatarUrl;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role = Role.USER;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserStatus status = UserStatus.ACTIVE;

	public User(String email, String passwordHash, String displayName) {
		this.email = email;
		this.passwordHash = passwordHash;
		this.displayName = displayName;
	}

}
