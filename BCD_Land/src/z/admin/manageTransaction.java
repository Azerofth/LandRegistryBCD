package z.admin;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import handler.LandInfoHandler;
import handler.TransHandler;
import model.LandInfo;

public class manageTransaction {
	Scanner scanner = new Scanner(System.in);
	TransHandler th = new TransHandler();
	
	public void manageTransactionMenu(){
	   	boolean mtIsRunning = true;
	   	
		while(mtIsRunning) {
		       	System.out.println("\n\nManage Transaction");
		        System.out.println("-".repeat(50));
		        System.out.println("1. Transaction List");	// display all and incl count for registered n pending
		        System.out.println("2. Transaction Approval");
		        System.out.println("3. Back");
		        System.out.print("Enter your choice: ");

		        try {
		            int muChoice = scanner.nextInt();
		            scanner.nextLine(); // Consume the newline character

		            // scanner.close();
		            switch (muChoice) {
		                case 1:
		                    th.printTransactionInfo(000);
		                    break;
		                case 2:
		                    System.out.println("\nTransaction Approval");
		                    System.out.println("-".repeat(50));
		                    th.approveTransaction();
		                    break;
		                case 3:
		                	mtIsRunning = false;
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
