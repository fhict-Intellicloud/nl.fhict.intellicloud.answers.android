package nl.fhict.intellicloud.answers;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
	
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
	
	public Question (Parcel in){
		id = in.readInt();
		question = in.readString();
		asker = in.readParcelable(User.class.getClassLoader());
		anwserer = in.readParcelable(User.class.getClassLoader());
		questionState = in.readParcelable(QuestionState.class.getClassLoader());
		date = in.readParcelable(User.class.getClassLoader());
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
		dest.writeParcelable(questionState, 0);
		dest.writeLong(date.getTime());
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
