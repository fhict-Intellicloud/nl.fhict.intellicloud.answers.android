package nl.fhict.intellicloud.answers;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
	
	private int id;
	private String question;
	private User asker;
	private User anwserer;
	private String questionState;
	
	public Question(int id, String question, User asker, User anwserer, String questionState){
		this.id = id;
		this.question = question;
		this.asker = asker;
		this.anwserer = anwserer;
		this.questionState = questionState;
	}
	
	public Question (Parcel in){
		id = in.readInt();
		question = in.readString();
		asker = in.readParcelable(User.class.getClassLoader());
		anwserer = in.readParcelable(User.class.getClassLoader());
		questionState = in.readString();
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
	
	public String getQuestionState() {
		return questionState;
	}
	
	public void setQuestionState(String questionState) {
		this.questionState = questionState;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(question);
		dest.writeParcelable(asker, 0);
		dest.writeParcelable(anwserer, 0);
		dest.writeString(questionState);
	}
	
	public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
		public Question createFromParcel (Parcel in){
			return new Question(in);
		}

		@Override
		public Question[] newArray(int size) {
			return new Question[size];
		}
	};
	
	
}
