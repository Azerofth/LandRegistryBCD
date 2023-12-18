package handler;

import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

import enuum.paymentMethod;
import enuum.status;
import enuum.transType;
import model.LandInfo;
import model.TransRec;
import model.User;

public class TransHandler {
    static LoginHandler login = LoginHandler.getInstance();

	private static final String TRANSACTION_FILE = "transaction.txt";
	
	UserHandler uh = new UserHandler();
	
    private static List<TransRec> readTransRec() {
        return FileHandler.readData(TRANSACTION_FILE);
    }
    
    private static int generateNewTransRecID(List<TransRec> trans) {
        // Find the maximum userID from existing users and increment by 1
        int maxID = 0;
        for (TransRec tran : trans) {
            if (tran.getLandID() > maxID) {
            	maxID = tran.getLandID();
            }
        }
        return maxID + 1;
    }

    public boolean transaction(int mode, int landID, int buyerID, int sellerID) {
        Scanner scanner = new Scanner(System.in);
        final int MAX_ATTEMPTS = 3;
        int attempts = 0;
        String enteredPassword = null;
        
        paymentMethod paymentMethod = null;

        // 1 - REGISTRATIONOFTITLE     // reg land to gov
        // 2 - CONVEYANCE              // between buyer n seller
        // 3 - REGISTRATIONOFDEEDS     // gov record. deeds, mortgages, etc

        System.out.println("\n");
        if (mode == 1) {
            System.out.println("Registration of Title Fee: RM100");
        } else if (mode == 2) {
            System.out.println("Conveyance Fee: RM100");
        } else if (mode == 3) {
            System.out.println("Registration of Deeds Fee: RM100");
        }

        System.out.print("Select Payment Method\n");
        System.out.print("1. Cash\n");
        System.out.print("2. Debit Card\n");
        System.out.print("3. Credit Card\n");
        System.out.print("4. QR Pay\n");
        System.out.print("5. Cancel\n");
        System.out.print("Enter Payment Method: ");
        int paymentMet = scanner.nextInt();
        scanner.nextLine();

        if (paymentMet == 1) {
            paymentMethod = enuum.paymentMethod.CASH;
        } else if (paymentMet == 2) {
            paymentMethod = enuum.paymentMethod.DEBITCARD;
        } else if (paymentMet == 3) {
            paymentMethod = enuum.paymentMethod.CREDITCARD;
        } else if (paymentMet == 4) {
            paymentMethod = enuum.paymentMethod.QRPAY;
        } else if (paymentMet == 5) {
            System.out.println("Transaction canceled.");
            return false;
        }

        if (paymentMethod != null) {
            while (attempts < MAX_ATTEMPTS) {
                System.out.print("\nEnter your password for validation ('X' to cancel): ");
                enteredPassword = scanner.nextLine();  // Use nextLine() to consume the entire line

                if (enteredPassword.equalsIgnoreCase("X")) {
                    System.out.println("\nTransaction canceled.");
                    return false;
                }

                if (enteredPassword.equals(login.getCurrentUserPass())) {
                    System.out.println("** Transaction successful. **");
                    addTransaction(mode, landID, buyerID, sellerID, paymentMethod);
                    return true;
                } else {
                    attempts++;
                    System.out.println("Invalid password. Attempts remaining: " + (MAX_ATTEMPTS - attempts));
                }
            }

            System.out.println("Max attempts reached. Transaction failed.");
        } else {
            System.out.println("Invalid payment method. Transaction canceled.");
        }

        return false;
    }
    
    private static void addTransaction(int mode, int landID, int buyerID, int sellerID, paymentMethod paymentMethod) {
        List<TransRec> trans = readTransRec();

        int transID = generateNewTransRecID(trans);
        Timestamp recDate = new Timestamp(System.currentTimeMillis());
        double amount = 100.0; // Assuming a fixed amount of RM100 for the transaction

        // Set transaction type based on the mode
        transType transType = null;
        if (mode == 1) {
            transType = enuum.transType.REGISTRATIONOFTITLE;
        } else if (mode == 2) {
            transType = enuum.transType.CONVEYANCE;
        } else if (mode == 3) {
            transType = enuum.transType.REGISTRATIONOFDEEDS;
        }

        // Set transaction status to PENDING as it needs approval
        status tranStatus = enuum.status.PENDING;

        // Create a new transaction object
        TransRec newTransaction = new TransRec(transID, landID, buyerID, sellerID, recDate, amount, paymentMethod, transType, tranStatus);

        System.out.println(newTransaction);
        // Add the new transaction to the list
        FileHandler.addObject(newTransaction, TRANSACTION_FILE);
        System.out.println("Transaction recorded successfully.");
    }
}
