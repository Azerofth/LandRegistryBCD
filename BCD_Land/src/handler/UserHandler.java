package handler;

import model.User;
import java.util.List;
import java.util.Scanner;

import enuum.userType;

public class UserHandler {
    private static final String USER_FILE = "user.txt";
    
    public static List<User> readUsers() {
        return FileHandler.readData(USER_FILE);
    }
    
    public void printUser() {
        List<User> users = readUsers();				//will re-read everytime
        System.out.printf("\n\n%" + 50 + "s%s%n", "", "User");
        System.out.println("-".repeat(130));
        System.out.printf("%-4s | %-10s | %-15s | %-15s | %-3s | %-20s | %-15s | %-15s%n",
                "ID", "User Type", "Username", "Password", "Age", "Email", "Phone Number", "Occupation");
        System.out.println("-".repeat(130));
        for (User user : users) {
            System.out.printf("%-4d | %-10s | %-15s | %-15s | %-3d | %-20s | %-15s | %-15s%n",
                    user.getUserID(), user.getUserType(), user.getUsername(), user.getPassword(), user.getAge(),
                    user.getEmail(), user.getPhoneNo(), user.getOccupation());
        }
        System.out.println("-".repeat(130));
    }

    private static int generateNewUserID(List<User> users) {
         // Find the maximum userID from existing users and increment by 1
         int maxID = 0;
         for (User user : users) {
             if (user.getUserID() > maxID) {
            	 maxID = user.getUserID();
             }
         }
         return maxID + 1;
     }
    
    public void addUser(int mode) {
        Scanner scanner = new Scanner(System.in);

        List<User> users = readUsers();
    	
    	if (mode == 1) {
    		//customer use
    	} else {
    		//printUser();	//admin use
    	}
        
        int userID = generateNewUserID(users);
        userType userType = enuum.userType.CUSTOMER;  // Only 1 admin
        
        System.out.printf("User ID #%d", userID);
        
        System.out.print("\nUsername: ");
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
        System.out.println("** New user registered successfully. **\n");
    }

    public void modifyUser(int mode) {
        Scanner scanner = new Scanner(System.in);
        LoginHandler login = LoginHandler.getInstance();
        
        String userInputToModify = null;
        User userToModify = null;
        User updatedUser = null;
        
        // Display existing users for reference
        List<User> users = readUsers();

        if (mode == 1) {
        	User currentUser = login.getCurrentUser();
        	userInputToModify = currentUser.getUsername();
            login.displayCurrentUser();
            //customer use
        } else {
            printUser(); //admin use

            // Collect user input for the user to modify
            System.out.print("Enter the username or user ID to modify: ");
            userInputToModify = scanner.nextLine();
        }

        // Find the user with the specified username or user ID
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (String.valueOf(user.getUserID()).equals(userInputToModify) || user.getUsername().equals(userInputToModify)) {
                userToModify = user;
                break;
            }
        }
        
        if (userToModify != null) {
            // Collect updated user details from user input
            System.out.println("New username\t\t: ");
            String newUsername = scanner.nextLine();
            System.out.println("New password\t\t: ");
            String newPassword = scanner.nextLine();
            System.out.println("New age\t\t\t: ");
            int newAge = scanner.nextInt();
            scanner.nextLine();
            System.out.println("New email\t\t: ");
            String newEmail = scanner.nextLine();
            System.out.println("New phone number\t: ");
            String newPhoneNumber = scanner.nextLine();
            System.out.println("New occupation\t\t: ");
            String newOccupation = scanner.nextLine();

            // Create a new user object with updated details
            updatedUser = new User(
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

            System.out.println("** User modified successfully. **\n");
            System.out.println(updatedUser);
            printUser(); // Display updated user list
        } else {
            System.out.println("** User not found. **\n");
        }

        if (mode == 1) {
        	login.setCurrentUser(updatedUser);
            login.displayCurrentUser();
            //customer use
        }
    }
    
	public void deleteUser() {
        Scanner scanner = new Scanner(System.in);

        List<User> users = readUsers();

        // Display existing users for reference
        printUser();

        // Collect user input for the user to delete
        System.out.print("\nEnter the username or user ID to delete: ");
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
            System.out.println("** User deleted successfully. **\n");
            printUser();
        } else {
            System.out.println("** User not found. **\n");
        }
    }
}