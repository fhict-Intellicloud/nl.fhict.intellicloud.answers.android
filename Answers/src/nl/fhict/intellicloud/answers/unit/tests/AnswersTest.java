//package nl.fhict.intellicloud.answers.unit.tests;
//
//import static org.junit.Assert.*;
//
//import java.util.ArrayList;
//import java.util.Date;
//
//import nl.fhict.intellicloud.answers.FilterList;
//import nl.fhict.intellicloud.answers.Question;
//import nl.fhict.intellicloud.answers.QuestionState;
//
//import org.junit.Before;
//import org.junit.Test;
//
//public class AnswersTest {
//	ArrayList<Question> list;
//	FilterList filterList;
//	
//	@Before
//	public void setUp() throws Exception {
//		filterList = new FilterList();
//		list = new ArrayList<Question>();
//		list.add(new Question(1, "What does the fox say?", null, null, QuestionState.Open, new Date()));
//		list.add(new Question(2, "What is love?", null, null, QuestionState.Closed, new Date()));
//		list.add(new Question(3, "Do you know the muffin man?", null, null, QuestionState.UpForAnswer, new Date()));
//		list.add(new Question(4, "What is your name?", null, null, QuestionState.UpForFeedback, new Date()));
//	}
//	
//	@Test
//	public void noFilterTest()
//	{
//		ArrayList<Question> listToCheck = filterList.createListWithFilter(list, 0);
//		int idToCheck = 1;
//		assertEquals(idToCheck,listToCheck.get(0).getId());
//		idToCheck = 2;
//		assertEquals(idToCheck,listToCheck.get(1).getId());
//		idToCheck = 3;
//		assertEquals(idToCheck,listToCheck.get(2).getId());
//		idToCheck = 4;
//		assertEquals(idToCheck,listToCheck.get(3).getId());
//	}
//	
//	@Test
//	public void filterOpenTest()
//	{
//		ArrayList<Question> listToCheck = filterList.createListWithFilter(list, 1);
//		int idToCheck = 1;
//		assertEquals(idToCheck,listToCheck.get(0).getId());
//		//list has only 1 item with question state open
//		assertEquals(1, listToCheck.size());
//		//check if question state matches filter
//		QuestionState stateToCheck = QuestionState.Open;
//		assertEquals(stateToCheck, listToCheck.get(0).getQuestionState());
//	}
//	
//	@Test
//	public void filterClosedTest()
//	{
//		ArrayList<Question> listToCheck = filterList.createListWithFilter(list, 2);
//		int idToCheck = 2;
//		assertEquals(idToCheck,listToCheck.get(0).getId());
//		//list has only 1 item with question state open
//		assertEquals(1, listToCheck.size());
//		//check if question state matches filter
//		QuestionState stateToCheck = QuestionState.Closed;
//		assertEquals(stateToCheck, listToCheck.get(0).getQuestionState());
//	}
//
//	@Test
//	public void filterUpForAnswerTest()
//	{
//		ArrayList<Question> listToCheck = filterList.createListWithFilter(list, 3);
//		int idToCheck = 3;
//		assertEquals(idToCheck,listToCheck.get(0).getId());
//		//list has only 1 item with question state open
//		assertEquals(1, listToCheck.size());
//		//check if question state matches filter
//		QuestionState stateToCheck = QuestionState.UpForAnswer;
//		assertEquals(stateToCheck, listToCheck.get(0).getQuestionState());
//	}
//	
//	@Test
//	public void filterUpForFeedbackTest()
//	{
//		ArrayList<Question> listToCheck = filterList.createListWithFilter(list, 4);
//		int idToCheck = 4;
//		assertEquals(idToCheck,listToCheck.get(0).getId());
//		//list has only 1 item with question state open
//		assertEquals(1, listToCheck.size());
//		//check if question state matches filter
//		QuestionState stateToCheck = QuestionState.UpForFeedback;
//		assertEquals(stateToCheck, listToCheck.get(0).getQuestionState());
//	}
//
//}
