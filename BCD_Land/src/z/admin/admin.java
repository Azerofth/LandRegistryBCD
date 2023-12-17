package z.admin;

import java.util.InputMismatchException;
import java.util.Scanner;

import landregistry.Main;
import model.User;

public class admin {

    static boolean isRunning = true;

    Scanner scanner = new Scanner(System.in);
    manageUser mu = new manageUser();
    Main main = new Main();

    public void adminMenu(User currentUser) {
        while (isRunning) {
            System.out.println("Welcome, " + currentUser.getUsername());

            System.out.println("\nAdmin Menu");
            System.out.println("-".repeat(50));
            System.out.println("1. Manage User");
            System.out.println("2. Manage Land");
            System.out.println("3. Land Record");
            System.out.println("4. Logout");
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
                        // manageLand();
                        // LANDINFO
                        // registerLand : new land
                        // landApproval : pass in all land status that is pending
                        break;
                    case 3:
                        // landRecord();
                        // LANDREC, TRANSREC
                        // check transaction success? LANDINFO update
                        break;
                    case 4:
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
