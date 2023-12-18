package landregistry;

//includes main menu, login, register, logout

import java.util.List;
import java.util.Scanner;

import enuum.userType;
import handler.FileHandler;
import handler.LoginHandler;
import handler.UserHandler;
import z.admin.*;
import z.customer.*;
import model.User;

public class Main {
	static boolean isRunning = true;
	private static final String USER_FILE = "user.txt";
	private static User currentUser = null;
	
    public static void main(String[] args) {

        while (isRunning) {
            welcomeMenu();
        }
    }

    private static void welcomeMenu() {
    	Scanner scanner = new Scanner(System.in);
    	
        System.out.println("Welcome to the Land Registration System!");
        System.out.println("-".repeat(50));
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        
        //scanner.close();

        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 3:
                isRunning = false;
                System.out.println("Exiting the Land Registration System.");
                break;
            default:
                System.out.println("Invalid choice. Please enter a valid option.");
        }

    }
    
    public static void login() {
    	LoginHandler login = LoginHandler.getInstance();
    	UserHandler uh = new UserHandler();
    	customer cus = new customer();
    	admin ad = new admin();
    	
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n\nLogin");
        System.out.println("-".repeat(50));
        
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        //scanner.close();
        // Check if the entered credentials are valid
        User user = validateUser(username, password);
        
        login.setCurrentUser(user);
        
        if (user != null) {
            System.out.println("Login successful!\n\n");
            // Redirect to the appropriate menu based on userType
            if (user.getUserType() == userType.ADMIN) {
                ad.adminMenu();
            } else {
                cus.customerMenu();
            }
        } else {
            System.out.println("Invalid username or password. Login failed.\n\n");
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
    
    
    private static void register() {
    	//Scanner scanner = new Scanner(System.in);
    	
    	System.out.println("\n\nRegister User");
    	System.out.println("-".repeat(50));
    	
    	UserHandler userHandler = new UserHandler();

    	userHandler.addUser(1);

        System.out.println("You can now login with your new credentials.");
        //scanner.close();
    }
    
    public boolean logout() {
	        currentUser = null;
	        System.out.println("Logout successful!\n\n\n");
        return false;
    }
}