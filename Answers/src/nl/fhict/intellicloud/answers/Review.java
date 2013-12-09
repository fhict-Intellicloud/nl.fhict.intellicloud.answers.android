package nl.fhict.intellicloud.answers;

public class Review {
	
	private int id;
	private String review;
	private Answer answer;
	private User reviewer;
	private ReviewState reviewState;
	
	public Review(String review, Answer answer, User reviewer, ReviewState reviewState){
		this.review = review;
		this.answer = answer;
		this.reviewer = reviewer;
		this.reviewState = reviewState;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getReview() {
		return review;
	}
	
	public void setReview(String review) {
		this.review = review;
	}
	
	public Answer getAnswer() {
		return answer;
	}
	
	public void setAnswer(Answer answer) {
		this.answer = answer;
	}
	
	public User getReviewer() {
		return reviewer;
	}
	
	public void setReviewer(User reviewer) {
		this.reviewer = reviewer;
	}
	
	public ReviewState getReviewState() {
		return reviewState;
	}
	
	public void setReviewState(ReviewState reviewState) {
		this.reviewState = reviewState;
	}
}

