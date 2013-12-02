package nl.fhict.intellicloud.answers;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
	private int id;
	private String userName;
	private String firstName;
	private String lastName;
	private String infix;
	private String password;
	private String userType;
	
	public User(int id, String userName, String firstName, String lastName, String infix, String password, String userType) {
		this.id = id;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.infix = infix;
		this.password = password;
		this.userType = userType;
	}
	
	public User (Parcel in){
		id = in.readInt();
		userName = in.readString();
		firstName = in.readString();
		lastName = in.readString();
		infix = in.readString();
		password = in.readString();
		userType = in.readString();
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
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
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUserType() {
		return userType;
	}
	
	public void setUserType(String userType) {
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
		dest.writeString(userName);
		dest.writeString(infix);
		dest.writeString(password);
		dest.writeString(userType);
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
