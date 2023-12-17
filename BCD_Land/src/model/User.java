package model;

// User Type
// Customer 0	Admin 1

import java.io.Serializable;

import enuum.userType;

@SuppressWarnings("serial")
public class User implements Serializable{
    private int userID;
    private userType userType;
    private String username;
    private String password;
    private int age;
    private String email;
    private String phoneNo;
    private String occupation;

    public User() {
        
    }
    
    public User(int userID, userType userType, String username, String password, int age, String email, String phoneNo, String occupation) {
        this.userID = userID;
        this.setUserType(userType);
        this.username = username;
        this.password = password;
        this.age = age;
        this.email = email;
        this.phoneNo = phoneNo;
        this.occupation = occupation;
    }
    
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
	public userType getUserType() {
		return userType;
	}

	public void setUserType(userType userType) {
		this.userType = userType;
	}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
    
    @Override
    public String toString() {
        return 
        		"User{\n" + "userID=" + userID + 
                ", userType=" + userType + 
                ", username=" + username + 
                ", password=" + password + 
                ", age=" + age + 
                ", email=" + email + 
                ", phoneNo=" + phoneNo + 
                ", occupation=" + occupation + 
                '}';
    }
}
