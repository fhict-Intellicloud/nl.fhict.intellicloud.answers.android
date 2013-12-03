package nl.fhict.intellicloud.answers;

import android.os.Parcel;
import android.os.Parcelable;

public enum UserType implements Parcelable {
	
	Customer,
	Employee;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(ordinal());
	}
	
	public static final Creator<UserType> CREATOR = new Creator<UserType>() {
		@Override
		public UserType createFromParcel(final Parcel source){
			return UserType.values()[source.readInt()];
		}
		
		@Override
		public UserType[] newArray(final int size){
			return new UserType[size];
		}
	};
}
