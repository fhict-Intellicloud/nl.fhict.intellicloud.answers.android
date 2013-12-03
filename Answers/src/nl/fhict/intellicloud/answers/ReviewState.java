package nl.fhict.intellicloud.answers;

import android.os.Parcel;
import android.os.Parcelable;

public enum ReviewState implements Parcelable {
	
	Open,
	Closed;
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(ordinal());
	}
	
	public static final Creator<ReviewState> CREATOR = new Creator<ReviewState>() {
		@Override
		public ReviewState createFromParcel(final Parcel source){
			return ReviewState.values()[source.readInt()];
		}
		
		@Override
		public ReviewState[] newArray(final int size){
			return new ReviewState[size];
		}
	};
}
