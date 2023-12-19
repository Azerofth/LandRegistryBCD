package z.admin;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import handler.LandInfoHandler;
import handler.LandRecHandler;
import model.LandInfo;

public class manageLand {
	Scanner scanner = new Scanner(System.in);
	LandInfoHandler lih = new LandInfoHandler();
	LandRecHandler lrh = new LandRecHandler();
	
	public void manageLandMenu(){
	   	boolean mlIsRunning = true;
	   	
		while(mlIsRunning) {
		       	System.out.println("\n\nManage Land");
		        System.out.println("-".repeat(50));
		        System.out.println("1. Land List");	// display all and incl count for registered n pending
		        System.out.println("2. Register New Land");
		        System.out.println("3. Approve Land Registration");
		        System.out.println("4. Back");
		        System.out.print("Enter your choice: ");

		        try {
		            int muChoice = scanner.nextInt();
		            scanner.nextLine(); // Consume the newline character

		            // scanner.close();
		            switch (muChoice) {
		                case 1:
		                    lih.printLandInfo(000);
		                    break;
		                case 2:
		                    System.out.println("Register Land");
		                    System.out.println("-".repeat(50));
		                    lih.addLandInfo(000);
		                    break;
		                case 3:
		                    System.out.println("Approve Land Registration");
		                    System.out.println("-".repeat(50));
		                    lrh.approveLandRec();
		                    break;
		                case 4:
		                	mlIsRunning = false;
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
