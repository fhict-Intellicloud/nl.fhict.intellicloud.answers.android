package nl.fhict.intellicloud.answers.backendcommunication;

import java.util.ArrayList;
import nl.fhict.intellicloud.answers.*;

import android.content.Context;

public class ServerAccessor {
	Context context;
	String token;
	public ServerAccessor(Context context, String accountToken)
	{
		this.context = context;
		this.token = accountToken;
	}
	
	public ArrayList<Question> getRemoteQuestions()
	{
		return null;
	}
	public void updateRemoteQuestions(ArrayList<Question> questions)
	{
		
	}
	public ArrayList<Answer> getRemoteAnswers()
	{
		return null;
	}
	public void updateRemoteAnswers(ArrayList<Answer> answers)
	{
		
	}
	public ArrayList<Review> getRemoteReviews()
	{
		return null;
	}
	public void updateRemoteReviews(ArrayList<Review> reviews)
	{
		
	}
	public ArrayList<Feedback> getRemoteFeedback()
	{
		return null;
	}
	public void updateRemoteFeedback(ArrayList<Feedback> feedback)
	{
		
	}
	public ArrayList<Question> getRemoteUsers()
	{
		return null;
	}
	
	
}
