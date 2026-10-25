package com.restaurantreview.repository;

import com.restaurantreview.entities.ReviewVote;
import com.restaurantreview.entities.ReviewVoteId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewVoteRepository extends JpaRepository<ReviewVote, ReviewVoteId> {

	long countByReviewId(Long reviewId);

}
