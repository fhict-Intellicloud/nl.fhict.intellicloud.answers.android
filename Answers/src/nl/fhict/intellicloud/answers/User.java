package nl.fhict.intellicloud.answers;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
	private int id;
	private String firstName;
	private String lastName;
	private String infix;
	private UserType userType;
	
	public User(int id, String firstName, String lastName, String infix, UserType userType) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.infix = infix;
		this.userType = userType;
	}
	
	public User (Parcel in){
		id = in.readInt();
		firstName = in.readString();
		lastName = in.readString();
		infix = in.readString();
		userType = in.readParcelable(UserType.class.getClassLoader());
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getInfix() {
		return infix;
	}
	
	public void setInfix(String infix) {
		this.infix = infix;
	}
	
	public UserType getUserType() {
		return userType;
	}
	
	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(firstName);
		dest.writeString(lastName);
		dest.writeString(infix);
		dest.writeParcelable(userType, 0);
	}
	
	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		public User createFromParcel (Parcel in){
			return new User(in);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};
}
