package nl.fhict.intellicloud.answers;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
	
	private int id;
	private String review;
	private Answer answer;
	private User reviewer;
	private ReviewState reviewState;
	
	public Review(int id, String review, Answer answer, User reviewer, ReviewState reviewState){
		this.id = id;
		this.review = review;
		this.answer = answer;
		this.reviewer = reviewer;
		this.reviewState = reviewState;
	}
	
	public Review (Parcel in){
		id = in.readInt();
		review = in.readString();
		answer = in.readParcelable(Answer.class.getClassLoader());
		reviewer = in.readParcelable(User.class.getClassLoader());
		reviewState = in.readParcelable(ReviewState.class.getClassLoader());
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(review);
		dest.writeParcelable(answer, 0);
		dest.writeParcelable(reviewer, 0);
		dest.writeParcelable(reviewState, 0);
	}
	
	public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
		public Review createFromParcel (Parcel in){
			return new Review(in);
		}

		@Override
		public Review[] newArray(int size) {
			return new Review[size];
		}
	};
}

