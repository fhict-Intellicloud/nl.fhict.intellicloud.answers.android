package nl.fhict.intellicloud.answers;

import java.util.Date;

public class Question {
	
	private int id;
	private String question;
	private User asker;
	private User anwserer;
	private QuestionState questionState;
	private Date date;
	
	public Question(int id, String question, User asker, User anwserer, QuestionState questionState, Date date){
		this.id = id;
		this.question = question;
		this.asker = asker;
		this.anwserer = anwserer;
		this.questionState = questionState;
		this.date = date;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public User getAsker() {
		return asker;
	}
	
	public void setAsker(User asker) {
		this.asker = asker;
	}
	
	public User getAnwserer() {
		return anwserer;
	}
	
	public void setAnwserer(User anwserer) {
		this.anwserer = anwserer;
	}
	
	public QuestionState getQuestionState() {
		return questionState;
	}
	
	public void setQuestionState(QuestionState questionState) {
		this.questionState = questionState;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
