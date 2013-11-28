package nl.fhict.intellicloud.answers;

import java.util.Date;

public class Question {
	private int questionId;
	private String content;
	private User questionUser, answerUser;
	
	private QuestionState questionState;
	private Date creationTime;
	
	public Question(int questionId, String content, User questionUser,
			User answerUser, QuestionState questionState, Date creationTime) {
		super();
		this.questionId = questionId;
		this.content = content;
		this.questionUser = questionUser;
		this.answerUser = answerUser;
		this.questionState = questionState;
		this.creationTime = creationTime;
	}

	public int getQuestionId() { return questionId;	}
	public void setQuestionId(int questionId) { this.questionId = questionId; }
	
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
	
	public User getQuestionUser() { return questionUser; }
	public void setQuestionUser(User questionUser) { this.questionUser = questionUser; }
	
	public User getAnswerUser() { return answerUser; }
	public void setAnswerUser(User answerUser) { this.answerUser = answerUser; }
	
	public QuestionState getQuestionState() { return questionState; }
	public void setQuestionState(QuestionState questionState) { this.questionState = questionState; }
	
	public Date getCreationTime() { return creationTime; }
	public void setCreationTime(Date creationTime) { this.creationTime = creationTime; }
}
