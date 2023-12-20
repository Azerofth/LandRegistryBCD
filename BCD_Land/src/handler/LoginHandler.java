package handler;

import model.User;
import z.admin.admin;
import z.customer.customer;

import java.util.List;
import java.util.Scanner;

import enuum.userType;

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
        	System.out.println("\n");
        	System.out.println("*".repeat(50));
            System.out.println("Current User Information:");
            System.out.printf("User ID #"+ currentUser.getUserID()+
            		"\nUsername\t\t:"+ currentUser.getUsername()+ 
            		"\nPassword\t\t:"+ currentUser.getPassword()+ 
            		"\nAge\t\t\t:"+ currentUser.getAge()+
            		"\nEmail\t\t\t:"+ currentUser.getEmail()+
            		"\nPhone Number\t\t:"+ currentUser.getPhoneNo()+
            		"\nOccupation\t\t:"+ currentUser.getOccupation() + "\n");
            System.out.println("*".repeat(50));
            System.out.println("\n");
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }
    
    public int getCurrentUserID() {
        return currentUser.getUserID();
    }

    public String getCurrentUserPass() {
        return currentUser.getPassword();
    }
    
    public void login() {
        Scanner scanner = new Scanner(System.in);
        admin ad = new admin();
        customer cus = new customer();
        
        System.out.println("\n\nLogin");
        System.out.println("-".repeat(50));
        
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        //scanner.close();
        // Check if the entered credentials are valid
        User user = validateUser(username, password);
        
        setCurrentUser(user);
        
        if (user != null) {
            System.out.println("** Login successful! **\n");
            
            // Redirect to the appropriate menu based on userType
            if (user.getUserType() == userType.ADMIN) {
                ad.adminMenu();
            } else {
                cus.customerMenu();
            }
        } else {
            System.out.println("** Invalid username or password. Login failed. **\n");
        }
    }

    private static User validateUser(String username, String password) {
        List<User> users = FileHandler.readData(USER_FILE);

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user; // Valid credentials
            }
        }

        return null; // Invalid credentials
    }
    
    
    public void register() {
    	System.out.println("\n\nRegister User");
    	System.out.println("-".repeat(50));
    	
    	UserHandler userHandler = new UserHandler();

    	userHandler.addUser(1);

        System.out.println("** You can now login with your credentials. **\n");
    }
    
    public boolean logout() {
	        setCurrentUser(null);
	        if (currentUser == null) {
	        	System.out.println("** Logout successful! **\n");
	        } else {
	        	System.out.println("** Logout failed **\n");
	        }
	        
        return false;
    }
}
