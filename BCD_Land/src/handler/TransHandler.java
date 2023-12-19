package handler;

import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import enuum.paymentMethod;
import enuum.status;
import enuum.transType;
import model.TransRec;

public class TransHandler {
    static LoginHandler login = LoginHandler.getInstance();

	private static final String TRANSACTION_FILE = "transaction.txt";
	
	UserHandler uh = new UserHandler();
	
	
    public List<TransRec> readTransRec() {
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

    public List<TransRec> printTransactionInfo(int mode) {
        List<TransRec> transactions = readTransRec();

        if (mode == 1) {		//read pending only
    	    List<TransRec> filteredTransactions = transactions.stream()
    		        .filter(transaction -> transaction.getTranStatus() == enuum.status.PENDING)
    		        .collect(Collectors.toList());
    		transactions = filteredTransactions;
    		System.out.printf("\n\n%" + 50 + "s%s%n", "", "Pending Transaction");
    		System.out.println("-".repeat(130));
        }else {
            System.out.printf("\n\n%" + 50 + "s%s%n", "", "Transaction Information");
            System.out.println("-".repeat(130));
        }
        
	    if (transactions.isEmpty()) {
	        System.out.println("No pending transactions.");
	    } else {
	        System.out.printf("%-4s | %-7s | %-6s | %-6s | %-10s | %-12s | %-20s | %-25s | %-10s%n",
	                "ID", "Land ID", "Buyer", "Seller", "Amount", "Pay Method", "Type", "Recorded Date", " Status");
	        System.out.println("-".repeat(130));

	        for (TransRec transaction : transactions) {
	            System.out.printf("%-4s | %-7s | %-6s | %-6s | %-10s | %-12s | %-20s | %-25s | %-10s%n",
	                    transaction.getTransID(), transaction.getLandID(), transaction.getBuyerID(), transaction.getSellerID(),
	                    transaction.getAmount(), transaction.getPaymentMethod(), transaction.getTransType(),
	                    transaction.getRecDate(), transaction.getTranStatus());
	        }
	    }

        System.out.println("-".repeat(130));
        
        return transactions;
    }

    public TransRec displayCurrentTransactionInfo(int transID) {
        List<TransRec> transactions = readTransRec();

        List<TransRec> filteredTransactions = transactions.stream()
                .filter(transaction -> transaction.getTransID() == transID)
                .collect(Collectors.toList());

        if (filteredTransactions.size() == 1) {
            TransRec transaction = filteredTransactions.get(0);

            System.out.println("\n");
            System.out.println("*".repeat(50));
            System.out.println("Updated Transaction Information:");
            System.out.printf("Transaction ID #" + transaction.getTransID() +
                    "\nLand ID\t\t:" + transaction.getLandID() +
                    "\nBuyer ID\t\t:" + transaction.getBuyerID() +
                    "\nSeller ID\t\t:" + transaction.getSellerID() +
                    "\nAmount\t\t\t:" + transaction.getAmount() +
                    "\nPayment Method\t\t:" + transaction.getPaymentMethod() +
                    "\nTransaction Type\t:" + transaction.getTransType() +
                    "\nTransaction Status\t:" + transaction.getTranStatus() +
                    "\nRecorded Date\t\t:" + transaction.getRecDate() + "\n");
            System.out.println("*".repeat(50));
            System.out.println("\n");
            return transaction;
        } else if (filteredTransactions.isEmpty()) {
            System.out.println("** Transaction ID not found. **\n");
        } else {
            System.out.println("** Multiple entries found for the same Transaction ID. **\n");
        }
		return null;
    }

    public boolean newTransaction(int mode, int landID, int buyerID, int sellerID) {
        Scanner scanner = new Scanner(System.in);
        LandRecHandler lrh = new LandRecHandler();
        
        List<TransRec> trans = readTransRec();

        final int MAX_ATTEMPTS = 3;
        int attempts = 0;
        String enteredPassword = null;
        
        paymentMethod paymentMethod = null;
        double amount = 0;

        // 1 - REGISTRATIONOFTITLE     // reg land to gov
        // 2 - CONVEYANCE              // between buyer n seller
        // 3 - REGISTRATIONOFDEEDS     // gov record. deeds, mortgages, etc

        System.out.println("\n");
        if (mode == 1) {
            System.out.println("Registration of Title Fee: RM100");
            amount = 100.0;
        } else if (mode == 2) {
            System.out.println("Conveyance Fee: RM200");
            amount = 200.0;
        } else if (mode == 3) {
            System.out.println("Registration of Deeds Fee: RM100");
            amount = 100.0;
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
            System.out.println("** Transaction canceled. **");
            return false;
        }

        if (paymentMethod != null) {
            while (attempts < MAX_ATTEMPTS) {
                System.out.print("\nEnter your password for validation ('X' to cancel): ");
                enteredPassword = scanner.nextLine();  // Use nextLine() to consume the entire line

                if (enteredPassword.equalsIgnoreCase("X")) {
                    System.out.println("\n** Transaction canceled. **");
                    return false;
                }

                if (enteredPassword.equals(login.getCurrentUserPass())) {
                    System.out.println("** Transaction successful. **");
                    
                    int transID = generateNewTransRecID(trans);
                    Timestamp recDate = new Timestamp(System.currentTimeMillis());

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
                    
                    // Add the new transaction to the list
                    FileHandler.addObject(newTransaction, TRANSACTION_FILE);
                    
                    lrh.newLandRec(1,landID,buyerID,transID);
                    
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

    // update status to COMPLETE
	// user can choose to approve all transactions by input specific transRecID to approve
	
	public void approveTransaction() {
	    Scanner scanner = new Scanner(System.in);

	    List<TransRec> allTransactions = readTransRec();
	    
	    // Display a list of transactions pending approval
	    List<TransRec> pendingTransactions = allTransactions.stream()
	            .filter(transaction -> transaction.getTranStatus() == enuum.status.PENDING)
	            .collect(Collectors.toList());

	    if (pendingTransactions != null) {
		    // Ask the user to input the transaction ID to approve
		    System.out.print("Transaction ID to approve (0 to approve all): ");
		    int transRecID = scanner.nextInt();

		    if (transRecID == 0) {
		        // Approve all transactions
		        for (TransRec transaction : pendingTransactions) {
		            transaction.setTranStatus(enuum.status.COMPLETE);
		        }
		        System.out.println("** All transactions approved. **");
		    } else {
		        // Approve a specific transaction by updating its status
		        TransRec transactionToApprove = pendingTransactions.stream()
		                .filter(transaction -> transaction.getTransID() == transRecID)
		                .findFirst()
		                .orElse(null);

		        if (transactionToApprove != null) {
		            transactionToApprove.setTranStatus(enuum.status.COMPLETE);
		            System.out.println("** Transaction ID #" + transRecID + " approved. **");
		        } else {
		            System.out.println("** Transaction not found or already approved. **");
		        }
		    }

		    // Update the transaction file with the modified transactions
		    FileHandler.writeData(allTransactions, TRANSACTION_FILE);
	    }
	}
}
