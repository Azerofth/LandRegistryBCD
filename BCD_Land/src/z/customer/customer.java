package z.customer;

import java.util.Scanner;
import java.util.InputMismatchException;

import handler.LandInfoHandler;
import handler.UserHandler;
import landregistry.Main;
import model.User;

//if login success
public class customer {  	
	
	static boolean isRunning = true;
	Scanner scanner = new Scanner(System.in);
	UserHandler uh = new UserHandler();
	LandInfoHandler lih = new LandInfoHandler();
	Main main = new Main();
	
	public void customerMenu() 
	{
    	User currentUser = uh.getCurrentUser();
		while (isRunning) {		
			try {
				System.out.println("Welcome, " + currentUser.getUsername());
				
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
				
				//scanner.close();

				switch (choice) {
				    case 1:
				    	uh.modifyUser(1);
				        break;
				    case 2:
				    	lih.addLandInfo(1);
				    			// input landInfo
				        break;
				    case 3:
				    	//userLandRecord();
				    			// read all LANDINFO of current user
				    			// user can reg as LANDREC to put onto market
				        break;
				    case 4:
				        //buyLand();
				    			// read land ONSALE
				        break;
				    case 5:
				        isRunning = main.logout();
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
