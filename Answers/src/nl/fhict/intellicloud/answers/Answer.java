package nl.fhict.intellicloud.answers;

public class Answer {
	
	private int id;
	private String answer;
	private Question question;
	private User answerer;
	private AnswerState answerState;
	
	public Answer(int id, String answer, Question question, User answerer, AnswerState answerState){
		this.id = id;
		this.answer = answer;
		this.question = question;
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
	
	public Question getQuestion() {
		return question;
	}
	
	public void setQuestion(Question question) {
		this.question = question;
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
}
