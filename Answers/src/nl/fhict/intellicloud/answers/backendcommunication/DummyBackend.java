package nl.fhict.intellicloud.answers.backendcommunication;

import java.util.ArrayList;
import java.util.Date;

import nl.fhict.intellicloud.answers.Answer;
import nl.fhict.intellicloud.answers.AnswerState;
import nl.fhict.intellicloud.answers.Question;
import nl.fhict.intellicloud.answers.QuestionState;
import nl.fhict.intellicloud.answers.Review;
import nl.fhict.intellicloud.answers.User;
import nl.fhict.intellicloud.answers.UserType;
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
	ArrayList<Question> dummyQuestions = null;
	ArrayList<Answer> dummyAnswers = null;
	ArrayList<Review> dummyReviews = null;
	
	
	public DummyBackend()
	{
		dummyQuestions = new ArrayList<Question>();
		dummyQuestions.add(new Question(1, "What does the fox say?", null, null, QuestionState.Open, new Date()));
		dummyQuestions.add(new Question(2, "What is love?", null, null, QuestionState.Open, new Date()));
		dummyQuestions.add(new Question(3, "Do you know the muffin man?", null, null, QuestionState.Open, new Date()));
		dummyQuestions.add(new Question(4, "What is your name?", null, null, QuestionState.Open, new Date()));
		dummyQuestions.add(new Question(5, "What is your quest?", null, null, QuestionState.Open, null));
		dummyQuestions.add(new Question(6, "What is the airspeed velocity of an unladen swallow?", null, null, QuestionState.Open, new Date()));
		
		dummyAnswers = new ArrayList<Answer>();
		User user1 = new User(1, "Remco", "Loeff", "", UserType.Employee);
		User user2 = new User(2, "Hans", "Grietje", "En", UserType.Customer);
		Question q = new Question(1, "HOI?", user1, user2, QuestionState.Open, new Date());
		Answer anwser = new Answer(1,"HELL YEAH",q, user1, AnswerState.UnderReview);
		
		dummyAnswers.add(anwser);
		dummyAnswers.add(anwser);
		dummyAnswers.add(anwser);
		
		dummyReviews = new ArrayList<Review>();
		
	
	}
	@Override
	public void CreateReview(Review review) {
		dummyReviews.add(review);

	}

	@Override
	public ArrayList<Review> GetReviews(Answer answer) {

		return dummyReviews;
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
	public void CreateAnswer(Answer answer) {
		dummyAnswers.add(answer);

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

}
