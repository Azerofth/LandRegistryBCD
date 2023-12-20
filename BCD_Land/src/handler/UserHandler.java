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
    
    private boolean isUsernameValid(List<User> users, String username) {
        // Check if the username contains at least one non-digit character and is not taken
        return username.matches(".*[a-zA-Z].*") && users.stream().noneMatch(user -> user.getUsername().equals(username));
    }
    
    public void addUser(int mode) {
        Scanner scanner = new Scanner(System.in);

        List<User> users = readUsers();

        if (mode == 1) {
            // customer use
        } else {
            printUser();
        }

        int userID = generateNewUserID(users);
        userType userType = enuum.userType.CUSTOMER; // Only 1 admin

        System.out.printf("User ID #%d", userID);
        System.out.print("\n");

        String username;
        do {
            System.out.print("Username\t: ");
            username = scanner.nextLine().toLowerCase();
            if (!isUsernameValid(users, username)) {
                System.out.println("** Username is invalid. Please choose a different username. **");
            }
        } while (!isUsernameValid(users, username));

        String password;
        do {
            System.out.print("Password\t: ");
            password = scanner.nextLine().toLowerCase();
            if (password.length() < 8) {
                System.out.println("** Password must be at least 8 characters long. **");
            }
        } while (password.length() < 8);

        int age;
        do {
            System.out.print("Age\t\t: ");
            age = scanner.nextInt();
            if (age < 18 || age > 120) {
                System.out.println("** Age must be between 18 and 120. **");
            }
        } while (age < 18 || age > 120);

        scanner.nextLine();

        String email;
        do {
            System.out.print("Email\t\t: ");
            email = scanner.nextLine().toLowerCase();
            if (!email.contains("@")) {
                System.out.println("** Invalid email. **");
            }
        } while (!email.contains("@"));

        String phoneNumber;
        do {
            System.out.print("Phone Number\t: ");
            phoneNumber = scanner.nextLine();
            if (phoneNumber.length() < 10) {
                System.out.println("** Phone number must be at least 10 characters long. **");
            }
        } while (phoneNumber.length() < 10);

        String occupation;
        do {
            System.out.print("Occupation\t: ");
            occupation = scanner.nextLine();
            if (!occupation.matches("^[a-zA-Z]+$")) {
                System.out.println("** Invalid input. Please enter only text. **");
            }
        } while (!occupation.matches("^[a-zA-Z]+$"));

        // Create a new user object
        User newUser = new User(userID, userType, username, password, age, email, phoneNumber, occupation);

        // Add the new user to the list
        FileHandler.addObject(newUser, USER_FILE);
        System.out.println("** New user registered successfully. **");
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
            // customer use
        } else {
            printUser(); // admin use

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
            System.out.print("New username\t\t: ");
            String newUsername;
            do {
                newUsername = scanner.nextLine().toLowerCase();
                if (!isUsernameValid(users, newUsername)) {
                    System.out.println("** Username is invalid or already taken. Please choose a different username. **");
                }
            } while (!isUsernameValid(users, newUsername));

            // Validate and get a valid password
            String newPassword;
            do {
                System.out.print("New password\t\t: ");
                newPassword = scanner.nextLine().toLowerCase();
                if (newPassword.length() < 8) {
                    System.out.println("** Password must be at least 8 characters long. **");
                }
            } while (newPassword.length() < 8);

            int newAge;
            do {
                System.out.print("New age\t\t\t: ");
                newAge = scanner.nextInt();
                scanner.nextLine();
                if (newAge < 18 || newAge > 120) {
                    System.out.println("** Age must be between 18 and 120. **");
                }
            } while (newAge < 18 || newAge > 120);

            String newEmail;
            do {
                System.out.print("New email\t\t: ");
                newEmail = scanner.nextLine().toLowerCase();
                if (!newEmail.contains("@")) {
                    System.out.println("** Invalid email. **");
                }
            } while (!newEmail.contains("@"));

            String newPhoneNumber;
            do {
                System.out.print("New phone number\t: ");
                newPhoneNumber = scanner.nextLine();
                if (newPhoneNumber.length() < 10) {
                    System.out.println("** Phone number must be at least 10 characters long. **");
                }
            } while (newPhoneNumber.length() < 10);

            String newOccupation;
            do {
                System.out.print("New occupation\t\t: ");
                newOccupation = scanner.nextLine();
                if (!newOccupation.matches("^[a-zA-Z]+$")) {
                    System.out.println("** Invalid input. Please enter only text. **");
                }
            } while (!newOccupation.matches("^[a-zA-Z]+$"));

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

            System.out.println("\n\n** User modified successfully. **\n");
        } else {
            System.out.println("** User not found. **\n");
        }

        if (mode == 1) {
            login.setCurrentUser(updatedUser);
            login.displayCurrentUser();
            // customer use
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
        } else {
            System.out.println("** User not found. **\n");
        }
    }
}