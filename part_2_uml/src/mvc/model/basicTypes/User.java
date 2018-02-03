/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model.basicTypes;

/**
 *
 * @author bohao
 */
public class User {
	private String id;
	private String userName;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String phoneNumber;

	public String getId() {
		return id;
	}
	
	protected void resetUser() {
		setId(null);
		setUserName(null);
		setFirstName(null);
		setLastName(null);
		setEmailAddress(null);
		setPhoneNumber(null);
	}

	public String getUserName() {
		return userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "User{" + "id=" + id + ", userName=" + userName + ", firstName=" + firstName + ", lastName=" + lastName + ", emailAddress=" + emailAddress + ", phoneNumber=" + phoneNumber + '}';
	}
	
	
}
