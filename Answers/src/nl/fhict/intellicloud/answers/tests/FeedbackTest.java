package nl.fhict.intellicloud.answers.tests;

import java.util.Date;

import android.test.AndroidTestCase;
import nl.fhict.intellicloud.answers.*;


public class FeedbackTest extends AndroidTestCase {
	
	User asker;
	User answerer;
	Question originalQuestion;
	Question newQuestion;
	Answer originalAnswer;
	Answer newAnswer;
	Date date;
	Date newDate;
	Feedback feedback;
	
	@Override
	protected void setUp() throws Exception {
		
		asker = new User(0, "Remco", "Loeff", "", UserType.Customer);
		answerer = new User(1, "Bart", "Bastings", "", UserType.Customer);
		originalAnswer = new Answer("Dit is een antwoord", answerer, AnswerState.UnderReview);
		newAnswer = new Answer("Dit is geen antwoord", answerer, AnswerState.UnderReview);
		date = new Date();
		newDate = new Date();
		originalQuestion = new Question(1, "Hoe oud worden 1dags vliegen", asker, answerer, QuestionState.Closed, date);
		newQuestion = new Question(1, "Hoe oud worden 3dags vliegen", asker, answerer, QuestionState.Closed, date);
		
		feedback = new Feedback("Test", asker, originalQuestion, originalAnswer, date, FeedbackState.Closed, FeedbackType.Accepted);
		feedback.setId(88);
		
		super.setUp();
	}
	
	public void testGetContent(){
		assertEquals("Test", feedback.getContent());
	}
	
	public void testSetContent(){
		feedback.setContent("Content");
		assertEquals("Content", feedback.getContent());
	}
	
	public void testGetId(){
		assertEquals(88, feedback.getId());
	}
	
	public void testSetId(){
		feedback.setId(22);
		assertEquals(22, feedback.getId());
	}
	
	public void testGetUser(){
		assertEquals(asker, feedback.getUser());
	}
	
	public void testSetUser(){
		feedback.setUser(answerer);
		assertEquals(answerer, feedback.getUser());
	}
	
	public void testGetOriginalQuestion(){
		assertEquals(originalQuestion, feedback.getOriginalQuestion());
	}
	
	public void testSetOriginalQuestion(){
		feedback.setOriginalQuestion(newQuestion);
		assertEquals(newQuestion, feedback.getOriginalQuestion());
	}
	
	public void testGetOriginalAnswer(){
		assertEquals(originalAnswer, feedback.getOriginalAnswer());
	}
	
	public void testSetOriginalAnswer(){
		feedback.setOriginalAnswer(newAnswer);
		assertEquals(newAnswer, feedback.getOriginalAnswer());
	}
	
	public void testGetDate(){
		assertEquals(date, feedback.getDate());
	}
	
	public void testSetDate(){
		feedback.setDate(newDate);
		assertEquals(newDate, feedback.getDate());
	}
	
	public void testGetFeedbackState(){
		assertEquals(FeedbackState.Closed, feedback.getFeedbackState());
	}
	
	public void testSetFeedbackState(){
		feedback.setFeedbackState(FeedbackState.Open);
		assertEquals(FeedbackState.Open, feedback.getFeedbackState());
	}
	
	public void testGetFeedbackType(){
		assertEquals(FeedbackType.Accepted, feedback.getFeedbackType());
	}
	
	public void testSetFeedbackType(){
		feedback.setFeedbackType(FeedbackType.Declined);
		assertEquals(FeedbackType.Declined, feedback.getFeedbackType());
	}
	
	

}
