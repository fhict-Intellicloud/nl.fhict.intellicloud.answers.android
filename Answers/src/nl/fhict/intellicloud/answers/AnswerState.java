package nl.fhict.intellicloud.answers;

import android.os.Parcel;
import android.os.Parcelable;

public enum AnswerState implements Parcelable {
	
	Ready,
	UnderReview;
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(ordinal());
	}
	
	public static final Creator<AnswerState> CREATOR = new Creator<AnswerState>() {
		@Override
		public AnswerState createFromParcel(final Parcel source){
			return AnswerState.values()[source.readInt()];
		}
		
		@Override
		public AnswerState[] newArray(final int size){
			return new AnswerState[size];
		}
	};

}
