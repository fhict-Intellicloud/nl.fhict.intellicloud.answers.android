package nl.fhict.intellicloud.answers.tests;

import android.test.AndroidTestCase;
import nl.fhict.intellicloud.answers.*;

public class AnswerTest extends AndroidTestCase {
	
	User answerer;
	Answer answer;
	
	@Override
	protected void setUp() throws Exception {
		
		answerer = new User(0, "Remco", "Loeff", "", UserType.Customer);
		answer = new Answer("Dit is een antwoord", answerer, AnswerState.UnderReview);
		answer.setId(2);
		
		super.setUp();
	}
	
	public void testGetId(){
		assertEquals(2, answer.getId());
	}
	
	public void testSetId(){
		answer.setId(453);
		assertEquals(453, answer.getId());	
	}
	
	public void testGetAnswer(){
		assertEquals("Dit is een antwoord", answer.getAnswer());
	}
	
	public void testSetAnswer(){
		answer.setAnswer("Dit is een grap haha");
		assertEquals("Dit is een grap haha", answer.getAnswer());
	}
	
	public void testGetAnswerState(){
		assertEquals(AnswerState.UnderReview, answer.getAnswerState());
	}
	
	public void testSetAnswerState(){
		answer.setAnswerState(AnswerState.Ready);
		assertEquals(AnswerState.Ready, answer.getAnswerState());
	}

}
