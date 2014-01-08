package nl.fhict.intellicloud.answers;

import java.util.Date;

public class Feedback {
	private String content;
	private int id;
	private User user;
	private Question originalQuestion;
	private Answer originalAnswer;
	private Date date;
	private FeedbackState feedbackState;
	private FeedbackType feedbackType;
	
	
	
	public Feedback(String content, User user, Question originalQuestion,
			Answer originalAnswer, Date date, FeedbackState feedbackState,
			FeedbackType feedbackType) {
		super();
		this.content = content;
		this.user = user;
		this.originalQuestion = originalQuestion;
		this.originalAnswer = originalAnswer;
		this.date = date;
		this.feedbackState = feedbackState;
		this.feedbackType = feedbackType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Question getOriginalQuestion() {
		return originalQuestion;
	}
	public void setOriginalQuestion(Question originalQuestion) {
		this.originalQuestion = originalQuestion;
	}
	public Answer getOriginalAnswer() {
		return originalAnswer;
	}
	public void setOriginalAnswer(Answer originalAnswer) {
		this.originalAnswer = originalAnswer;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public FeedbackState getFeedbackState() {
		return feedbackState;
	}
	public void setFeedbackState(FeedbackState feedbackState) {
		this.feedbackState = feedbackState;
	}
	public FeedbackType getFeedbackType() {
		return feedbackType;
	}
	public void setFeedbackType(FeedbackType feedbackType) {
		this.feedbackType = feedbackType;
	}

	
}
