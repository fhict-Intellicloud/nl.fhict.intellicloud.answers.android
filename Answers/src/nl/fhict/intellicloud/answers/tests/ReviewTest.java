package nl.fhict.intellicloud.answers.tests;

import android.test.AndroidTestCase;
import nl.fhict.intellicloud.answers.*;

public class ReviewTest extends AndroidTestCase {
	
	Review review;
	Answer answer;
	User answerer;
	User reviewer;
	
	@Override
	protected void setUp() throws Exception {
		
		answerer = new User(0, "Remco", "Loeff", "", UserType.Customer);
		reviewer = new User(1, "Bassie", "de Clown", "", UserType.Employee);
		answer = new Answer("Dit is een antwoord", answerer, AnswerState.UnderReview);
		review = new Review("Dit is een review", answer, reviewer, ReviewState.Open);
		
		super.setUp();
	}
	
	public void testGetReviewState(){
		assertEquals(ReviewState.Open, review.getReviewState());
	}
	
	public void testSetReviewState(){
		review.setReviewState(ReviewState.Closed);
		assertEquals(ReviewState.Closed, review.getReviewState());
	}
	
	public void testGetReviewer(){
		assertEquals("Bassie", reviewer.getFirstName());
		assertEquals("de Clown", reviewer.getLastName());
	}
	
	public void testSetReviewer(){
		reviewer.setFirstName("Hans");
		reviewer.setLastName("en Grietje");
		assertEquals("Hans", reviewer.getFirstName());
		assertEquals("en Grietje", reviewer.getLastName());
	}
	
	public void testGetAnswer(){
		assertEquals("Dit is een antwoord", answer.getAnswer());
	}
	
	public void testSetAnswer(){
		answer.setAnswer("Ik ben super");
		assertEquals("Ik ben super", answer.getAnswer());
	}
	
	public void testGetReview(){
		assertEquals("Dit is een review", review.getReview());
	}
	
	public void testSetReview(){
		review.setReview("Hello mister sjaak");
		assertEquals("Hello mister sjaak", review.getReview());
		
	}
	
	public void testGetID(){
		assertEquals(0, answerer.getId());
		assertEquals(1, reviewer.getId());
	}
	
	public void testGetId(){
		answerer.setId(2);
		reviewer.setId(3);
		assertEquals(2, answerer.getId());
		assertEquals(3, reviewer.getId());
	}

}
