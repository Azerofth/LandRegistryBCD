package handler;

import model.User;
import java.util.List;
import java.util.Scanner;

import misc.userType;

public class userHandler {

    private static final String USER_FILE = "users.txt";

    public static List<User> readUsers() {
        return FileHandler.readData(USER_FILE);
    }

    public static void printUser() {
        List<User> users = readUsers();
        System.out.printf("%" + 50 + "s%s%n", "", "User");
        System.out.println("-".repeat(150));
        System.out.printf("%-6s | %-10s | %-15s | %-15s | %-3s | %-20s | %-15s | %-15s%n",
                "UserID", "UserType", "Username", "Password", "Age", "Email", "Phone Number", "Occupation");
        System.out.println("-".repeat(150));
        for (User user : users) {
            System.out.printf("%-6d | %-10s | %-15s | %-15s | %-3d | %-20s | %-15s | %-15s%n",
                    user.getUserID(), user.getUserType(), user.getUsername(), user.getPassword(), user.getAge(),
                    user.getEmail(), user.getPhoneNo(), user.getOccupation());
        }
        System.out.println("-".repeat(150));
    }

    private static int generateNewUserID(List<User> users) {
         // Find the maximum userID from existing users and increment by 1
         int maxUserID = 0;
         for (User user : users) {
             if (user.getUserID() > maxUserID) {
                 maxUserID = user.getUserID();
             }
         }
         return maxUserID + 1;
     }
    
    public static void addUser(int mode) {
    	
    	if (mode == 1) {
    		//customer use
    	} else {
    		printUser();	//admin use
    	}
    	
        Scanner scanner = new Scanner(System.in);

        // Collect user details from user input
        // Collect other user details in a similar manner
        List<User> users = readUsers();

        int userID = generateNewUserID(users);
        userType userType = misc.userType.CUSTOMER;  // Only 1 admin

        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Age: ");
        int age = scanner.nextInt();
        
        // Consume the newline character left in the buffer
        scanner.nextLine(); 

        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Occupation: ");
        String occupation = scanner.nextLine();

        // Create a new user object
        User newUser = new User(userID, userType, username, password, age, email, phoneNumber, occupation);

        // Add the new user to the list
        FileHandler.addObject(newUser, USER_FILE);
        System.out.println("User added successfully.");
    }

	public static void modifyUser(int mode) {
	    Scanner scanner = new Scanner(System.in);

	    // Display existing users for reference
	    List<User> users = readUsers();
	    
    	if (mode == 1) {
    		//customer use
    	} else {
    		printUser();	//admin use
    	}

	    // Collect user input for the user to modify
	    System.out.print("Enter the username or user ID to modify: ");
	    String userInputToModify = scanner.nextLine();

	    // Find the user with the specified username or user ID
	    User userToModify = null;
	    for (int i = 0; i < users.size(); i++) {
	        User user = users.get(i);
	        if (String.valueOf(user.getUserID()).equals(userInputToModify) || user.getUsername().equals(userInputToModify)) {
	            userToModify = user;
	            break;
	        }
	    }

	    if (userToModify != null) {
	        // Collect updated user details from user input
	        System.out.print("Enter new username: ");
	        String newUsername = scanner.nextLine();
	        System.out.print("Enter new password: ");
	        String newPassword = scanner.nextLine();
	        System.out.print("Enter new age: ");
	        int newAge = scanner.nextInt();

	        // Consume the newline character left in the buffer
	        scanner.nextLine();

	        System.out.print("Enter new email: ");
	        String newEmail = scanner.nextLine();
	        System.out.print("Enter new phone number: ");
	        String newPhoneNumber = scanner.nextLine();
	        System.out.print("Enter new occupation: ");
	        String newOccupation = scanner.nextLine();

	        // Create a new user object with updated details
	        User updatedUser = new User(
	                userToModify.getUserID(),
	                userToModify.getUserType(),
	                newUsername,
	                newPassword,
	                newAge,
	                newEmail,
	                newPhoneNumber,
	                newOccupation
	        );

	        // Replace the old user with the updated user in the list
	        users.set(users.indexOf(userToModify), updatedUser);

	        // Write the updated user list to the file
	        FileHandler.writeData(users, USER_FILE);

	        System.out.println("User modified successfully.");
	        System.out.println(updatedUser);
	        printUser();  // Display updated user list
	    } else {
	        System.out.println("User not found.");
	    }
	}
    
	public static void deleteUser() {
        Scanner scanner = new Scanner(System.in);

        List<User> users = readUsers();

        // Display existing users for reference
        printUser();

        // Collect user input for the user to delete
        System.out.print("Enter the username or user ID to delete: ");
        String userInputToDelete = scanner.nextLine();

        // Find the user with the specified username
        User userToDelete = null;
        for (User user : users) {
            if (String.valueOf(user.getUserID()).equals(userInputToDelete) || user.getUsername().equals(userInputToDelete)) {
                userToDelete = user;
                break;
            }
        }

        if (userToDelete != null) {
            // Delete the user
            users.remove(userToDelete);
            FileHandler.writeData(users, USER_FILE);
            System.out.println("User deleted successfully.");
            printUser();
        } else {
            System.out.println("User not found.");
        }
    }

//testing purpose
    public static void main(String[] args) {
////    all working now
//    	printUser();
//    	addUser(000);
//    	deleteUser();
//      modifyUser(000);
    }
}

