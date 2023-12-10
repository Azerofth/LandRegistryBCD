package z.customer;

import java.util.Scanner;

//if login success
public class customer {  	
	Scanner scanner = new Scanner(System.in);
	
	public void customerMenu() 
	{
	    System.out.println("\nMenu");
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
	            //modifyProfile();
	        			// reuse admin's (modify param)
	            break;
	        case 2:
	            //registerLand();
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
	            //logout();
	            break;
	        default:
	            System.out.println("Invalid choice. Please enter a valid option.");
	    }
	    scanner.close();
	}
	
}
