package nl.fhict.intellicloud.answers.backendcommunication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.util.Base64;

import nl.fhict.intellicloud.answers.Answer;
import nl.fhict.intellicloud.answers.AnswerState;
import nl.fhict.intellicloud.answers.Question;
import nl.fhict.intellicloud.answers.QuestionState;
import nl.fhict.intellicloud.answers.Review;
import nl.fhict.intellicloud.answers.User;
import nl.fhict.intellicloud.answers.UserType;
import nl.fhict.intellicloud.answers.backendcommunication.oauth.AuthenticationManager;
/**
 * DummyBackend.java
 * 
 * @author Bart van Drongelen
 * 
 * Temporary dummy class for communicating with the backend.
 *
 */
public class DummyBackend implements IAnswerService, IQuestionService,
		IReviewService {
	@Override
	public void UpdateQuestion(Question question) {
		// TODO Auto-generated method stub
		
	}
	ArrayList<Question> dummyQuestions = null;
	ArrayList<Answer> dummyAnswers = null;
	ArrayList<Review> dummyReviews = null;
	
	
	public DummyBackend()
	{
		User user1 = new User(1, "Remco", "Loeff", "", UserType.Employee);
		User user2 = new User(2, "Hans", "Grietje", "En", UserType.Customer);
		User johnCleese = new User(1, "John", "Cleese", "", UserType.Customer);
		
		dummyQuestions = new ArrayList<Question>();
		dummyQuestions.add(new Question(1, "What does the fox say?", user1, null, QuestionState.Open, new Date()));
		dummyQuestions.add(new Question(2, "What is love?", user2, null, QuestionState.Open, new Date()));
		dummyQuestions.add(new Question(3, "Do you know the muffin man?", null, null, QuestionState.Open, new Date()));
		dummyQuestions.add(new Question(4, "What is your name?", null, null, QuestionState.Open, new Date()));
		dummyQuestions.add(new Question(5, "What is your quest?", null, null, QuestionState.Open, null));
		dummyQuestions.add(new Question(6, "What is the airspeed velocity of an unladen swallow?", johnCleese, null, QuestionState.Open, setDate("2013-12-04 09:27:37")));
		dummyQuestions.add(new Question(1, "Does looking at a picture of the sun hurt your eyes?", null, null, QuestionState.Closed, setDate("2013-12-05 09:27:37")));
		dummyQuestions.add(new Question(2, "How can I lose weight without moving?", null, null, QuestionState.Closed, new Date()));
		dummyQuestions.add(new Question(3, "How do I get accepted into Hogwarts?", null, null, QuestionState.UpForAnswer, new Date()));
		dummyQuestions.add(new Question(4, "Is it posible to make 1+1=3?", null, null, QuestionState.UpForAnswer, new Date()));
		dummyQuestions.add(new Question(5, "HOW DO I TURN OF CAPSLOCK?", null, null, QuestionState.UpForFeedback, null));
		dummyQuestions.add(new Question(6, "Is this an oke question?", null, null, QuestionState.UpForFeedback, new Date()));
		
		dummyAnswers = new ArrayList<Answer>();
		Question q = new Question(21, "HOI?", user1, user2, QuestionState.Open, new Date());
		Answer anwser = new Answer("HELL YEAH", user1, AnswerState.UnderReview);
		
		dummyAnswers.add(anwser);
		dummyAnswers.add(anwser);
		dummyAnswers.add(anwser);
		
		int i = 0;
		for (Question question : dummyQuestions)
		{
			Answer answerselection = dummyAnswers.get(i % dummyAnswers.size());
			question.setAnswer(answerselection);
		}
		
		dummyReviews = new ArrayList<Review>();
		
	
	}
	@Override
	public void CreateReview(Review review) {
		dummyReviews.add(review);

	}


	@Override
	public void UpdateReview(Review review) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<Question> GetQuestions() {
		
		return GetQuestions(-1);
	}

	@Override
	public ArrayList<Question> GetQuestions(int employeeId) {

		return dummyQuestions;
	}

	@Override
	public void CreateAnswer(Answer answer, int questionId) {
		dummyAnswers.add(answer);
		dummyQuestions.get(questionId).setAnswer(answer);

	}


	@Override
	public ArrayList<Answer> GetAnswers() {
		return GetAnswers(-1, null);
	}

	@Override
	public ArrayList<Answer> GetAnswers(int employeeId, AnswerState answerState) {
		return dummyAnswers;
	}

	@Override
	public void UpdateAnswer(Answer answer) {
		// TODO Auto-generated method stub

	}

	@Override
	public Answer GetAnswer(int id) {
		for (Answer a : dummyAnswers)
		{
			if (a.getId() == id)
			{
				return a;
			}
		}
		return null;
	}

	@Override
	public Question GetQuestion(int id) {
		for (Question q : dummyQuestions)
		{
			if (q.getId() == id)
			{
				return q;
			}
		}
		return null;
	}
	
	 private Date setDate(String date) {
     	String dtStart = date;  
     	SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
     	try {
     		return format.parse(dtStart);
     	} catch (Exception e) {
     	    e.printStackTrace();  
     	}
			return null;
     }
	@Override
	public ArrayList<Review> GetReviews(int answerId) {
		// TODO Auto-generated method stub
		return dummyReviews;
	}
	@Override
	public Answer GetAnswerUsingQuestion(int questionId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Question GetQuestionUsingAnswer(int answerId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This function is used to get the header for authorizing a request in the backend. It returns a base64
	 * representation of the json containing the issuer and the access_token needed to authorize a request.
	 * @return
	 */
	private String getBase64AuthorizationHeader() {
		String json = String.format(
				"{" +
				"	\"issuer\":\"accounts.google.com\"," +
				"	\"access_token\":\"%s\"" +
				"}", AuthenticationManager.getInstance().getAccessToken());
		byte[] encodedbytes = Base64.encode(json.getBytes(), Base64.DEFAULT);
		return new String(encodedbytes);
	}
}
