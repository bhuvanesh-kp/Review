package com.restaurantreview.repository;

import com.restaurantreview.constants.ReviewStatus;
import com.restaurantreview.entities.Cuisine;
import com.restaurantreview.entities.Restaurant;
import com.restaurantreview.entities.Review;
import com.restaurantreview.entities.ReviewPhoto;
import com.restaurantreview.entities.ReviewVote;
import com.restaurantreview.entities.ReviewVoteId;
import com.restaurantreview.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Runs the Flyway schema on a real MySQL and checks the review-related mappings and constraints.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class ReviewRepositoryTest {

	@Container
	@ServiceConnection
	static MySQLContainer mysql = new MySQLContainer(DockerImageName.parse("mysql:8.4"));

	@Autowired
	TestEntityManager em;

	@Autowired
	ReviewRepository reviews;

	@Autowired
	ReviewVoteRepository votes;

	@Autowired
	ReviewPhotoRepository photos;

	User author;

	User voter;

	Restaurant restaurant;

	@BeforeEach
	void setUp() {
		author = em.persist(new User("author@example.com", "hash", "Author"));
		voter = em.persist(new User("voter@example.com", "hash", "Voter"));
		Cuisine italian = em.persist(new Cuisine("Italian"));
		restaurant = new Restaurant("Trattoria", "trattoria", "Chennai", "India");
		restaurant.getCuisines().add(italian);
		em.persist(restaurant);
	}

	@Test
	void savesReviewWithPhotoAndCompositeKeyVote() {
		Review review = new Review(restaurant, author, (short) 5, "Great pasta");
		review.addPhoto(new ReviewPhoto("/uploads/1.jpg", "Carbonara"));
		reviews.save(review);
		votes.save(new ReviewVote(review, voter));
		em.flush();
		em.clear();

		Review loaded = reviews.findById(review.getId()).orElseThrow();
		assertThat(loaded.getStatus()).isEqualTo(ReviewStatus.PUBLISHED);
		assertThat(loaded.getCreatedAt()).isNotNull();
		assertThat(loaded.getPhotos()).hasSize(1);
		assertThat(loaded.getRestaurant().getCuisines()).extracting(Cuisine::getName).containsExactly("Italian");
		assertThat(votes.findById(new ReviewVoteId(review.getId(), voter.getId()))).isPresent();
		assertThat(reviews.existsByRestaurantIdAndUserId(restaurant.getId(), author.getId())).isTrue();
	}

	@Test
	void rejectsSecondReviewBySameUserForSameRestaurant() {
		reviews.saveAndFlush(new Review(restaurant, author, (short) 4, "First"));

		assertThatThrownBy(() -> reviews.saveAndFlush(new Review(restaurant, author, (short) 2, "Second")))
			.isInstanceOf(DataIntegrityViolationException.class);
	}

	@Test
	void deletingReviewRemovesItsPhotosAndVotes() {
		Review review = new Review(restaurant, author, (short) 3, "Okay");
		review.addPhoto(new ReviewPhoto("/uploads/2.jpg", null));
		reviews.save(review);
		votes.save(new ReviewVote(review, voter));
		em.flush();
		em.clear();

		reviews.deleteById(review.getId());
		em.flush();

		assertThat(photos.count()).isZero();
		assertThat(votes.count()).isZero();
	}

}
