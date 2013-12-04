package nl.fhict.intellicloud.answers;


public class User{
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
}
