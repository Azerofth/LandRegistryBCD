package z.admin;

import java.util.InputMismatchException;
import java.util.Scanner;

import handler.UserHandler;

public class manageUser {
	Scanner scanner = new Scanner(System.in);
	UserHandler uh = new UserHandler();
	
	void manageUserMenu(){
	   	boolean muIsRunning = true;
		
		while(muIsRunning) {
		       System.out.println("\n\nManage User");
		        System.out.println("-".repeat(50));
		        System.out.println("1. User List");
		        System.out.println("2. Register New User");
		        System.out.println("3. Modify User");
		        System.out.println("4. Delete User");
		        System.out.println("5. Back");
		        System.out.print("Enter your choice: ");

		        try {
		            int muChoice = scanner.nextInt();
		            scanner.nextLine(); // Consume the newline character

		            // scanner.close();
		            switch (muChoice) {
		                case 1:
		                    uh.printUser();
		                    break;
		                case 2:
		                    System.out.println("Register User");
		                    System.out.println("-".repeat(50));
		                    uh.addUser(000);
		                    break;
		                case 3:
		                    System.out.println("Modify User");
		                    System.out.println("-".repeat(50));
		                    uh.modifyUser(000);
		                    break;
		                case 4:
		                    System.out.println("Delete User");
		                    System.out.println("-".repeat(50));
		                    uh.deleteUser();
		                    break;
		                case 5:
		                	muIsRunning = false;
		                    break; // back
		                default:
		                    System.out.println("Invalid choice. Please enter a valid option.");
		            }
		        } catch (InputMismatchException e) {
		            System.out.println("Invalid input. Please enter a valid integer.");
		            scanner.nextLine(); // Consume the invalid input
		        } catch (Exception e) {
		            System.out.println("An error occurred: " + e.getMessage());
		        }
		}
	}

}
