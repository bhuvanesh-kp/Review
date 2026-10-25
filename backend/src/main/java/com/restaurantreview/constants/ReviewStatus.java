package com.restaurantreview.constants;

/** Only {@link #PUBLISHED} reviews count toward a restaurant's rating aggregates. */
public enum ReviewStatus {
	PUBLISHED,
	HIDDEN,
	REMOVED
}
