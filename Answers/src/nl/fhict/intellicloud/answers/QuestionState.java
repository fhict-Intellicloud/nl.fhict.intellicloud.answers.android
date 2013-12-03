package nl.fhict.intellicloud.answers;

import android.os.Parcel;
import android.os.Parcelable;

public enum QuestionState implements Parcelable{
	
	Open,
	UpForAnswer,
	UpForFeedback,
	Closed;
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(ordinal());
	}
	
	public static final Creator<QuestionState> CREATOR = new Creator<QuestionState>() {
		@Override
		public QuestionState createFromParcel(final Parcel source){
			return QuestionState.values()[source.readInt()];
		}
		
		@Override
		public QuestionState[] newArray(final int size){
			return new QuestionState[size];
		}
	};
}
