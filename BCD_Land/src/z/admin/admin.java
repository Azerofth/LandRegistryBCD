package z.admin;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import handler.LandRecHandler;
import handler.LoginHandler;
import handler.UserHandler;
import landregistry.Main;
import model.LandRec;
import model.User;
import z.customer.landConveyance;

public class admin {

    static boolean isRunning = true;

    Scanner scanner = new Scanner(System.in);
    LoginHandler login = LoginHandler.getInstance();
    UserHandler uh = new UserHandler();
    manageUser mu = new manageUser();
    manageLand ml = new manageLand();
    manageTransaction mt = new manageTransaction();
    LandRecHandler lrh = new LandRecHandler();
    landConveyance lc = new landConveyance();
    
    public void adminMenu() {
    	User currentUser = login.getCurrentUser();
    	System.out.println("Welcome, " + currentUser.getUsername() + "!");
    	
        while (isRunning) {
            System.out.println("\nAdmin Menu");
            System.out.println("-".repeat(50));
            System.out.println("1. Manage User");
            System.out.println("2. Manage Land");
            System.out.println("3. Manage Transaction");
            System.out.println("4. Land Record");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                // scanner.close();
                switch (choice) {
                    case 1:
                        mu.manageUserMenu();
                        break;
                    case 2:
                        ml.manageLandMenu();
                        break;
                    case 3:
                    	mt.manageTransactionMenu();
                        break;
                    case 4:
                    	lrh.printLandRec();
                		List<LandRec> allOnsale = lc.getLandRecList(3, 000);
                		lc.printLandInfoWLandRec(3, allOnsale, 000);
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
