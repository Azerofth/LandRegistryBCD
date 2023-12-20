package handler;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import blockchain.DigitalSignature;
import enuum.paymentMethod;
import enuum.status;
import enuum.transType;
import model.LandInfo;
import model.TransRec;
import blockchain.*;

public class TransHandler {
    static LoginHandler login = LoginHandler.getInstance();
    
	private static final String TRANSACTION_FILE = "transaction.txt";
	
    private static final String ALGORITHM = "SHA256WithRSA";
    private static final DigitalSignature digitalSignature = DigitalSignature.getInstance();

    public List<TransRec> readTransRec() {
        return FileHandler.readData(TRANSACTION_FILE);
    }
    
    private static int generateNewTransRecID(List<TransRec> trans) {
        // Find the maximum transID from existing transactions and increment by 1
        int maxID = 0;
        for (TransRec tran : trans) {
            if (tran.getTransID() > maxID) {
                maxID = tran.getTransID();
            }
        }
        return maxID + 1;
    }

    public TransRec getTransRecByTransID(int transID) {
        List<TransRec> allTransactions = readTransRec();

        for (TransRec transaction : allTransactions) {
            if (transaction.getTransID() == transID) {
                return transaction;
            }
        }

        return null;
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
    	TransRec transaction = getTransRecByTransID(transID);

        if (transaction != null) {
        	System.out.println("\n");
            System.out.println("*".repeat(50));
            System.out.println("                Transaction");
            System.out.printf("Transaction ID  #" + transaction.getTransID() +
                    "\nLand ID\t\t\t: " + transaction.getLandID() +
                    "\nBuyer ID\t\t: " + transaction.getBuyerID() +
                    "\nSeller ID\t\t: " + transaction.getSellerID() +
                    "\nAmount\t\t\t: " + transaction.getAmount() +
                    "\nPayment Method\t\t: " + transaction.getPaymentMethod() +
                    "\nTransaction Type\t: " + transaction.getTransType() +
                    "\nTransaction Status\t: " + transaction.getTranStatus() +
                    "\nRecorded Date\t\t: " + transaction.getRecDate() + "\n");
            System.out.println("*".repeat(50));
            System.out.println("\n");
            return transaction;
        } else {
            System.out.println("** Transaction ID not found. **\n");
        }
		return null;
    }

    public boolean newTransaction(int mode, int landID, int buyerID, int sellerID) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        Scanner scanner = new Scanner(System.in);
        LandInfoHandler lih = new LandInfoHandler();
        LandRecHandler lrh = new LandRecHandler();
        
        List<TransRec> trans = readTransRec();

        final int MAX_ATTEMPTS = 3;
        int attempts = 0;
        String enteredPassword = null;
        transType transType = null;
        byte[] myDigitalSignature = null;
        
        paymentMethod paymentMethod = null;
        double amount = 0;
        
        LandInfo involvedLand = lih.getLandInfoByLandID(landID);

        // 1 - REGISTRATIONOFTITLE     	//register land
        // 2 - REGISTRATIONOFDEEDS     	//sell
        // 3 - CONVEYANCE				//buy land

        // Set transaction type based on the mode
        System.out.println("\n");
        if (mode == 1) {
            System.out.println("Registration of Title Fee: RM100");
            amount = 100.0;
            transType = enuum.transType.REGISTRATIONOFTITLE;	
        } else if (mode == 2) {
            System.out.println("Registration of Deeds Fee: RM100");
            amount = 100.0;
            transType = enuum.transType.REGISTRATIONOFDEEDS;
        } else if (mode == 3) {
            System.out.println("Conveyance Fee\t: RM200");
            
            double landValue = involvedLand.getValue();
            System.out.println("Land Value\t: RM" + landValue);
            
            // Include the land value in the transaction amount
            amount = 200.0 + landValue;
            
            System.out.println("** Total\t: RM" + amount + " **\n");
            
            transType = enuum.transType.CONVEYANCE;	
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

                    // Set transaction status to PENDING as it needs approval
                    status tranStatus = enuum.status.PENDING;
                    
                    String transactionData = landID + "_" + buyerID + "_" + sellerID;
                    
                    myDigitalSignature = digitalSignature.generateDigitalSignature(transactionData);

<<<<<<< Updated upstream
                    // Create a new transaction object
                    TransRec newTransaction = new TransRec(transID, landID, buyerID, sellerID, recDate, amount, paymentMethod, transType, tranStatus, myDigitalSignature);
=======
                    //temp signature help me
                    byte[] adminSignature = null;
                    
                    // Create a new transaction object
                    TransRec newTransaction = new TransRec(transID, landID, buyerID, sellerID, recDate, amount, paymentMethod, transType, tranStatus, adminSignature);
                    String transactionString = newTransaction.toString();
                    System.out.println(transactionString);
//                    Blockchain.createBlockchain(transactionString);
>>>>>>> Stashed changes
                    
                    // Add the new transaction to the list
                    FileHandler.addObject(newTransaction, TRANSACTION_FILE);
                    displayCurrentTransactionInfo(transID);
                    
                    lrh.newLandRec(mode, landID, buyerID, transID);
                    
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
	
    public void approveTransaction() throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        Scanner scanner = new Scanner(System.in);

        // Read Transactions
        List<TransRec> allTransactions = readTransRec();

        // Display a list of transactions pending approval
        List<TransRec> pendingTransactions = allTransactions.stream()
                .filter(transaction -> transaction.getTranStatus() == enuum.status.PENDING)
                .collect(Collectors.toList());

        if (!pendingTransactions.isEmpty()) {
            // Display Transaction IDs with pending status
            System.out.println("Transaction IDs with pending status:");
            pendingTransactions.forEach(transaction ->
                    System.out.println("Transaction ID # " + transaction.getTransID()));

            // Ask the user to input Transaction ID to approve (0 to approve all)
            System.out.print("Transaction ID to approve (0 to approve all): ");
            int transIDToApprove = scanner.nextInt();

            if (transIDToApprove == 0) {
                // Approve all transactions
                boolean allApproved = true;

                for (TransRec transaction : pendingTransactions) {
                    String transactionData = transaction.getLandID() + "_"
                            + transaction.getBuyerID() + "_"
                            + transaction.getSellerID();

                    if (!digitalSignature.verifyDigitalSignature(transactionData, transaction.getMyDigitalSignature())) {
                        System.out.println("** Digital signature verification failed for Transaction ID #" + transaction.getTransID() + ". **");
                        allApproved = false;
                        break;
                    }

                    transaction.setTranStatus(enuum.status.COMPLETE);
                    System.out.println("** Transaction ID #" + transaction.getTransID() + " approved. **");
                }

                if (allApproved) {
                    System.out.println("** All transactions approved. **");
                } else {
                    System.out.println("** Not all transactions approved due to failed signature verification. **");
                }
            } else {
                // Approve a specific transaction by updating its status
                TransRec transactionToApprove = pendingTransactions.stream()
                        .filter(transaction -> transaction.getTransID() == transIDToApprove)
                        .findFirst()
                        .orElse(null);

                if (transactionToApprove != null) {
                    String transactionData = transactionToApprove.getLandID() + "_"
                            + transactionToApprove.getBuyerID() + "_"
                            + transactionToApprove.getSellerID();

                    if (digitalSignature.verifyDigitalSignature(transactionData, transactionToApprove.getMyDigitalSignature())) {
                        transactionToApprove.setTranStatus(enuum.status.COMPLETE);
                        
                        System.out.println("** Transaction ID #" + transIDToApprove + " approved. **");
                    } else {
                        System.out.println("** Digital signature verification failed for Transaction ID #" + transIDToApprove + ". **");
                    }
                } else {
                    System.out.println("** Transaction not found or already approved. **");
                }
            }

            // Update the transaction file with the modified transactions
            FileHandler.writeData(allTransactions, TRANSACTION_FILE);
        } else {
            System.out.println("** No pending transactions found. **");
        }
    }

}





//package handler;
//
//import java.sql.Timestamp;
//import java.util.List;
//import java.util.Scanner;
//import java.util.stream.Collectors;
//
//import enuum.paymentMethod;
//import enuum.status;
//import enuum.transType;
//import model.LandInfo;
//import model.TransRec;
//
//public class TransHandler {
//    static LoginHandler login = LoginHandler.getInstance();
//
//	private static final String TRANSACTION_FILE = "transaction.txt";
//
//    public List<TransRec> readTransRec() {
//        return FileHandler.readData(TRANSACTION_FILE);
//    }
//    
//    private static int generateNewTransRecID(List<TransRec> trans) {
//        // Find the maximum transID from existing transactions and increment by 1
//        int maxID = 0;
//        for (TransRec tran : trans) {
//            if (tran.getTransID() > maxID) {
//                maxID = tran.getTransID();
//            }
//        }
//        return maxID + 1;
//    }
//
//    public TransRec getTransRecByTransID(int transID) {
//        List<TransRec> allTransactions = readTransRec();
//
//        for (TransRec transaction : allTransactions) {
//            if (transaction.getTransID() == transID) {
//                return transaction;
//            }
//        }
//
//        return null;
//    }
//    
//    public List<TransRec> printTransactionInfo(int mode) {
//        List<TransRec> transactions = readTransRec();
//
//        if (mode == 1) {		//read pending only
//    	    List<TransRec> filteredTransactions = transactions.stream()
//    		        .filter(transaction -> transaction.getTranStatus() == enuum.status.PENDING)
//    		        .collect(Collectors.toList());
//    		transactions = filteredTransactions;
//    		System.out.printf("\n\n%" + 50 + "s%s%n", "", "Pending Transaction");
//    		System.out.println("-".repeat(130));
//        }else {
//            System.out.printf("\n\n%" + 50 + "s%s%n", "", "Transaction Information");
//            System.out.println("-".repeat(130));
//        }
//        
//	    if (transactions.isEmpty()) {
//	        System.out.println("No pending transactions.");
//	    } else {
//	        System.out.printf("%-4s | %-7s | %-6s | %-6s | %-10s | %-12s | %-20s | %-25s | %-10s%n",
//	                "ID", "Land ID", "Buyer", "Seller", "Amount", "Pay Method", "Type", "Recorded Date", " Status");
//	        System.out.println("-".repeat(130));
//
//	        for (TransRec transaction : transactions) {
//	            System.out.printf("%-4s | %-7s | %-6s | %-6s | %-10s | %-12s | %-20s | %-25s | %-10s%n",
//	                    transaction.getTransID(), transaction.getLandID(), transaction.getBuyerID(), transaction.getSellerID(),
//	                    transaction.getAmount(), transaction.getPaymentMethod(), transaction.getTransType(),
//	                    transaction.getRecDate(), transaction.getTranStatus());
//	        }
//	    }
//
//        System.out.println("-".repeat(130));
//        
//        return transactions;
//    }
//
//    public TransRec displayCurrentTransactionInfo(int transID) {
//    	TransRec transaction = getTransRecByTransID(transID);
//
//        if (transaction != null) {
//        	System.out.println("\n");
//            System.out.println("*".repeat(50));
//            System.out.println("                Transaction");
//            System.out.printf("Transaction ID  #" + transaction.getTransID() +
//                    "\nLand ID\t\t\t: " + transaction.getLandID() +
//                    "\nBuyer ID\t\t: " + transaction.getBuyerID() +
//                    "\nSeller ID\t\t: " + transaction.getSellerID() +
//                    "\nAmount\t\t\t: " + transaction.getAmount() +
//                    "\nPayment Method\t\t: " + transaction.getPaymentMethod() +
//                    "\nTransaction Type\t: " + transaction.getTransType() +
//                    "\nTransaction Status\t: " + transaction.getTranStatus() +
//                    "\nRecorded Date\t\t: " + transaction.getRecDate() + "\n");
//            System.out.println("*".repeat(50));
//            System.out.println("\n");
//            return transaction;
//        } else {
//            System.out.println("** Transaction ID not found. **\n");
//        }
//		return null;
//    }
//
//    public boolean newTransaction(int mode, int landID, int buyerID, int sellerID) {
//        Scanner scanner = new Scanner(System.in);
//        LandInfoHandler lih = new LandInfoHandler();
//        LandRecHandler lrh = new LandRecHandler();
//        
//        List<TransRec> trans = readTransRec();
//
//        final int MAX_ATTEMPTS = 3;
//        int attempts = 0;
//        String enteredPassword = null;
//        transType transType = null;
//        byte[] digitalSignature = null;
//        
//        paymentMethod paymentMethod = null;
//        double amount = 0;
//        
//        LandInfo involvedLand = lih.getLandInfoByLandID(landID);
//
//        // 1 - REGISTRATIONOFTITLE     	//register land
//        // 2 - REGISTRATIONOFDEEDS     	//sell
//        // 3 - CONVEYANCE				//buy land
//
//        // Set transaction type based on the mode
//        System.out.println("\n");
//        if (mode == 1) {
//            System.out.println("Registration of Title Fee: RM100");
//            amount = 100.0;
//            transType = enuum.transType.REGISTRATIONOFTITLE;	
//        } else if (mode == 2) {
//            System.out.println("Registration of Deeds Fee: RM100");
//            amount = 100.0;
//            transType = enuum.transType.REGISTRATIONOFDEEDS;
//        } else if (mode == 3) {
//            System.out.println("Conveyance Fee\t: RM200");
//            
//            double landValue = involvedLand.getValue();
//            System.out.println("Land Value\t: RM" + landValue);
//            
//            // Include the land value in the transaction amount
//            amount = 200.0 + landValue;
//            
//            System.out.println("** Total\t: RM" + amount + " **\n");
//            
//            transType = enuum.transType.CONVEYANCE;	
//        }
//
//        System.out.print("Select Payment Method\n");
//        System.out.print("1. Cash\n");
//        System.out.print("2. Debit Card\n");
//        System.out.print("3. Credit Card\n");
//        System.out.print("4. QR Pay\n");
//        System.out.print("5. Cancel\n");
//        System.out.print("Enter Payment Method: ");
//        int paymentMet = scanner.nextInt();
//        scanner.nextLine();
//
//        if (paymentMet == 1) {
//            paymentMethod = enuum.paymentMethod.CASH;
//        } else if (paymentMet == 2) {
//            paymentMethod = enuum.paymentMethod.DEBITCARD;
//        } else if (paymentMet == 3) {
//            paymentMethod = enuum.paymentMethod.CREDITCARD;
//        } else if (paymentMet == 4) {
//            paymentMethod = enuum.paymentMethod.QRPAY;
//        } else if (paymentMet == 5) {
//            System.out.println("** Transaction canceled. **");
//            return false;
//        }
//
//        if (paymentMethod != null) {
//            while (attempts < MAX_ATTEMPTS) {
//                System.out.print("\nEnter your password for validation ('X' to cancel): ");
//                enteredPassword = scanner.nextLine();  // Use nextLine() to consume the entire line
//
//                if (enteredPassword.equalsIgnoreCase("X")) {
//                    System.out.println("\n** Transaction canceled. **");
//                    return false;
//                }
//
//                if (enteredPassword.equals(login.getCurrentUserPass())) {
//                    System.out.println("** Transaction successful. **");
//                    
//                    int transID = generateNewTransRecID(trans);
//                    Timestamp recDate = new Timestamp(System.currentTimeMillis());
//
//                    // Set transaction status to PENDING as it needs approval
//                    status tranStatus = enuum.status.PENDING;
//
//                    // Create a new transaction object
//                    TransRec newTransaction = new TransRec(transID, landID, buyerID, sellerID, recDate, amount, paymentMethod, transType, tranStatus, digitalSignature);
//                    
//                    // Add the new transaction to the list
//                    FileHandler.addObject(newTransaction, TRANSACTION_FILE);
//                    displayCurrentTransactionInfo(transID);
//                    
//                    lrh.newLandRec(mode, landID, buyerID, transID);
//                    
//                    return true;
//                } else {
//                    attempts++;
//                    System.out.println("Invalid password. Attempts remaining: " + (MAX_ATTEMPTS - attempts));
//                }
//            }
//
//            System.out.println("Max attempts reached. Transaction failed.");
//        } else {
//            System.out.println("Invalid payment method. Transaction canceled.");
//        }
//
//        return false;
//    }
//
//    // update status to COMPLETE
//	// user can choose to approve all transactions by input specific transRecID to approve
//	
//    public void approveTransaction() {
//        Scanner scanner = new Scanner(System.in);
//
//        // Read Transactions
//        List<TransRec> allTransactions = readTransRec();
//
//        // Display a list of transactions pending approval
//        List<TransRec> pendingTransactions = allTransactions.stream()
//                .filter(transaction -> transaction.getTranStatus() == enuum.status.PENDING)
//                .collect(Collectors.toList());
//
//        if (!pendingTransactions.isEmpty()) {
//            // Display Transaction IDs with pending status
//            System.out.println("Transaction IDs with pending status:");
//            pendingTransactions.forEach(transaction ->
//                    System.out.println("Transaction ID # " + transaction.getTransID()));
//
//            // Ask the user to input Transaction ID to approve (0 to approve all)
//            System.out.print("Transaction ID to approve (0 to approve all): ");
//            int transIDToApprove = scanner.nextInt();
//
//            if (transIDToApprove == 0) {
//                // Approve all transactions
//                pendingTransactions.forEach(transaction ->
//                        transaction.setTranStatus(enuum.status.COMPLETE));
//                System.out.println("** All transactions approved. **");
//            } else {
//                // Approve a specific transaction by updating its status
//                TransRec transactionToApprove = pendingTransactions.stream()
//                        .filter(transaction -> transaction.getTransID() == transIDToApprove)
//                        .findFirst()
//                        .orElse(null);
//
//                if (transactionToApprove != null) {
//                    transactionToApprove.setTranStatus(enuum.status.COMPLETE);
//                    System.out.println("** Transaction ID #" + transIDToApprove + " approved. **");
//                } else {
//                    System.out.println("** Transaction not found or already approved. **");
//                }
//            }
//
//            // Update the transaction file with the modified transactions
//            FileHandler.writeData(allTransactions, TRANSACTION_FILE);
//        } else {
//            System.out.println("** No pending transactions found. **");
//        }
//    }
<<<<<<< Updated upstream
//}
=======
    

    public void approveTransaction() {
        Scanner scanner = new Scanner(System.in);

        // Read Transactions
        List<TransRec> allTransactions = readTransRec();

        // Display a list of transactions pending approval
        List<TransRec> pendingTransactions = allTransactions.stream()
                .filter(transaction -> transaction.getTranStatus() == enuum.status.PENDING)
                .collect(Collectors.toList());

        if (!pendingTransactions.isEmpty()) {
            // Display Transaction IDs with pending status
            System.out.println("Transaction IDs with pending status:");
            pendingTransactions.forEach(transaction ->
                    System.out.println("Transaction ID # " + transaction.getTransID()));

            // Ask the user to input Transaction ID to approve (0 to approve all)
            System.out.print("Transaction ID to approve (0 to approve all): ");
            int transIDToApprove = scanner.nextInt();

            if (transIDToApprove == 0) {
                // Approve all transactions
                pendingTransactions.forEach(transaction ->
                        transaction.setTranStatus(enuum.status.COMPLETE));
                System.out.println("** All transactions approved. **");
            } else {
                // Approve a specific transaction by updating its status
                TransRec transactionToApprove = pendingTransactions.stream()
                        .filter(transaction -> transaction.getTransID() == transIDToApprove)
                        .findFirst()
                        .orElse(null);

                if (transactionToApprove != null) {
                    transactionToApprove.setTranStatus(enuum.status.COMPLETE);
                    System.out.println("** Transaction ID #" + transIDToApprove + " approved. **");
                    String approvedTransaction = transactionToApprove.toString();
                    System.out.println(approvedTransaction);
                    Blockchain.createBlockchain(approvedTransaction);
                } else {
                    System.out.println("** Transaction not found or already approved. **");
                }
            }

            // Update the transaction file with the modified transactions
            FileHandler.writeData(allTransactions, TRANSACTION_FILE);
        } else {
            System.out.println("** No pending transactions found. **");
        }
    }
}
>>>>>>> Stashed changes
