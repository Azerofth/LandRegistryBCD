package landregistry;

//includes main menu, login, register, logout

import java.util.Scanner;

import handler.LandInfoHandler;
import handler.LandRecHandler;
import handler.LoginHandler;

public class Main {
	static boolean isRunning = true;
	static LoginHandler login = LoginHandler.getInstance();
    public static void main(String[] args) {

        while (isRunning) {
            welcomeMenu();
        }
    }

    private static void welcomeMenu() {
    	Scanner scanner = new Scanner(System.in);
    	
        System.out.println("Land Registration System");
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
                login.login();
                break;
            case 2:
                login.register();
                break;
            case 3:
                isRunning = false;
                System.out.println("\n** Exiting Land Registration System. **");
                break;
            default:
                System.out.println("** Invalid choice. Please enter a valid option. **");
        }

    }
}