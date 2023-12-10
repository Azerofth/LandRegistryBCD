package z.admin;

import java.util.Scanner;

public class admin {

	Scanner scanner = new Scanner(System.in);
	
	public void adminMenu() 
	{
	    System.out.println("\nAdmin Menu");
	    System.out.println("1. Manage User");
	    System.out.println("2. Manage Land");
	    System.out.println("3. Land Record");
	    System.out.println("4. Logout");
	    System.out.print("Enter your choice: ");
	    
	    int choice = scanner.nextInt();
	    scanner.nextLine(); // Consume the newline character

	    switch (choice) {
	        case 1:
	            //manageUser();
	        			// USER
	        			// registerUser	: reuse Main's register (modify param)
	        			// modifyUser	: new feature in manageUser
	        			// deleteUser	: new feature in manageUser
	            break;
	        case 2:
	            //manageLand();
	        			// LANDINFO
	        			// registerLand	: new land
	        			// landApproval	: pass in all land status that is pending
	            break;
	        case 3:
	        	//landRecord();
	        			// LANDREC, TRANSREC
	        			// check transaction success? LANDINFO update
	            break;
	        case 4:
	        	//logout();
	            break;
	        default:
	            System.out.println("Invalid choice. Please enter a valid option.");
	    }
	    scanner.close();
	}	
	
}
