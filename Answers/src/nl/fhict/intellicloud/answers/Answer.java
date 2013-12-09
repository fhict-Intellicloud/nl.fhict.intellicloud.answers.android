package nl.fhict.intellicloud.answers;

public class Answer {
	
	private int id;
	private String answer;
	
	private User answerer;
	private AnswerState answerState;
	
	public Answer(int id, String answer, User answerer, AnswerState answerState){
		this.id = id;
		this.answer = answer;
		
		this.answerer = answerer;
		this.answerState = answerState;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	
	public User getAnswerer() {
		return answerer;
	}
	
	public void setAnswerer(User answerer) {
		this.answerer = answerer;
	}
	
	public AnswerState getAnwserState() {
		return answerState;
	}
	
	public void setAnwserState(AnswerState anwserState) {
		this.answerState = anwserState;
	}
	

	public AnswerState getAnswerState() {
		return answerState;
	}

	public void setAnswerState(AnswerState answerState) {
		this.answerState = answerState;
	}
}
