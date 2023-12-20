package z.customer;

import java.util.Scanner;
import java.util.InputMismatchException;

import handler.LandInfoHandler;
import handler.LoginHandler;
import handler.UserHandler;
import landregistry.Main;
import model.User;

//if login success
public class customer {  	
	
	static boolean isRunning = true;

	public void customerMenu() 
	{
		Scanner scanner = new Scanner(System.in);
		LoginHandler login = LoginHandler.getInstance();
		UserHandler uh = new UserHandler();
		LandInfoHandler lih = new LandInfoHandler();
		landConveyance lc = new landConveyance();
		
    	User currentUser = login.getCurrentUser();
    	System.out.println("Welcome, " + currentUser.getUsername() + "!");

		while (isRunning) {		
			try {
				System.out.println("\nMenu");
				System.out.println("-".repeat(50));
				System.out.println("1. Modify Profile");
				System.out.println("2. Register Land");			//seller
				System.out.println("3. User Land Record");		//seller
				System.out.println("4. Buy Land");				//buyer
				System.out.println("5. Logout");
				System.out.print("Enter your choice: ");
				
				int choice = scanner.nextInt();
				scanner.nextLine(); // Consume the newline character

				switch (choice) {
				    case 1:
				    	System.out.println("\nModify Profile");
				    	System.out.println("-".repeat(50));
				    	uh.modifyUser(1);
				        break;
				    case 2:
				    	System.out.println("\nRegister Land");
				    	System.out.println("-".repeat(50));
				    	lih.addLandInfo(1);
				        break;
				    case 3:
				    	System.out.println("\nUser Land Record");
				    	System.out.println("-".repeat(50));
				    	lc.userSellLand();
				        break;
				    case 4:
				    	System.out.println("\nBuy Land");
				    	System.out.println("-".repeat(50));
				    	lc.userBuyLand();
				        break;
				    case 5:
				        isRunning = login.logout();
				        break;
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