package nl.fhict.intellicloud.answers.backendcommunication;

import java.util.ArrayList;

import nl.fhict.intellicloud.answers.*;

/**
 * IReviewService.java
 * 
 * @author Bart van Drongelen
 *
 * Interface defining communication between backend and the application for functionality related to Reviews.
 *
 */
public interface IReviewService {
	
	/**
	 * Adds a review to the database and sends it to be processed.
	 * @param review The review to add.
	 */
	void createReview(Review review);
	/**
	 * Get {@link Review} objects that belong to a specific {@link Answer}.
	 * @param answer The Answer to find reviews for.
	 * @return {@link ArrayList} with all Reviews for the given answer
	 */
	ArrayList<Review> getReviews(Answer answer);
	/**
	 * Updates a {@link Review} with the updated {@link ReviewState}.
	 * @param review The Review to update.
	 */
	void UpdateReview(Review review);
	
	

}
