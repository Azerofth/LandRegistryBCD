package landregistry;

// includes 
//		- main menu
//		- login
//		- register
//		- logout


import java.util.Scanner;

import z.admin.*;
import z.customer.*;
import misc.userType;
import model.User;

public class Main {
	static boolean isRunning = true;
	
	//static 
	
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (isRunning) {
            welcomeMenu(scanner);
        }

        scanner.close();
    }

    private static void welcomeMenu(Scanner scanner) {
        System.out.println("Welcome to the Land Registration System!");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (choice) {
            case 1:
                login(scanner);
                break;
            case 2:
                register(scanner);
                break;
            case 3:
                isRunning = false;
                System.out.println("Exiting the Land Registration System.");
                break;
            default:
                System.out.println("Invalid choice. Please enter a valid option.");
        }

    }
    
    private static void login(Scanner scanner) {
    	customer customer = new customer();
    	admin admin = new admin();
    	
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        //modify here for setting username pass, db? txt?
        if (username.equals("cust1") && password.equals("pass")) {
            String loggedInUser = username;
            System.out.println("Login successful. Welcome, " + loggedInUser + "!");
            
//            if (User.getUserType().equals(userType.CUSTOMER)) {
//            	
//            }
            
            customer.customerMenu();
            
            admin.adminMenu();
        } else {
            System.out.println("Invalid username or password. Login failed.");
        }
    }

    private static void register(Scanner scanner) {
    	//generate userID auto
    	int userID = 1;
    	//set user type
    	//fix later
    	userType userType = misc.userType.CUSTOMER;
    	
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Age: ");
        int age = scanner.nextInt();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Occupation: ");
        String occupation = scanner.nextLine();
        
        // add logic to store the new username and password
        User newUser = new User(userID, userType, username, password, age, email, phoneNumber, occupation);

        System.out.println("Registration successful. You can now login with your new credentials.");
    }

}