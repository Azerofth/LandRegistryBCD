package handler;

import model.User;
import java.util.List;

public class LoginHandler {

	private static final String USER_FILE = "user.txt";
    private static LoginHandler instance;

    // Variable to store the currently logged-in user
    private User currentUser = new User();
    
    private LoginHandler() {
        // Initialization logic, if needed
    }
    
    // Method to get the singleton instance
    public static synchronized LoginHandler getInstance() {
        if (instance == null) {
            instance = new LoginHandler();
        }
        return instance;
    }


    public void displayCurrentUser() {
        if (currentUser != null) {
        	System.out.println("\n\n");
        	System.out.println("*".repeat(50));
            System.out.println("Current User Information:");
            System.out.printf("User ID\t:"+ currentUser.getUserID()+
            		"\nUsername\t\t:"+ currentUser.getUsername()+ 
            		"\nPassword\t\t:"+ currentUser.getPassword()+ 
            		"\nAge\t\t\t:"+ currentUser.getAge()+
            		"\nEmail\t\t\t:"+ currentUser.getEmail()+
            		"\nPhone Number\t\t:"+ currentUser.getPhoneNo()+
            		"\nOccupation\t\t:"+ currentUser.getOccupation() + "\n");
            System.out.println("*".repeat(50));
            System.out.println("\n\n");
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public void setCurrentUser(User user) {
        System.out.println("Setting currentUser to: " + user);
        currentUser = user;
    }

    public User getCurrentUser() {
        System.out.println(currentUser);
        return currentUser;
    }
    
    public int getCurrentUserID() {
        System.out.println(currentUser);
        return currentUser.getUserID();
    }
}
