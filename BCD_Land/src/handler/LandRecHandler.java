package handler;

import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import enuum.landStatus;
import enuum.paymentMethod;
import enuum.status;
import enuum.transType;
import model.LandRec;
import model.TransRec;

public class LandRecHandler {
    static LoginHandler login = LoginHandler.getInstance();

	private static final String LANDREC_FILE = "landRec.txt";
	
	UserHandler uh = new UserHandler();
	
    private static List<LandRec> readLandRec() {
        return FileHandler.readData(LANDREC_FILE);
    }
    
    private static int generateNewLandRecID(List<LandRec> landRecs) {
        // Find the maximum userID from existing users and increment by 1
        int maxID = 0;
        for (LandRec landRec : landRecs) {
            if (landRec.getLandID() > maxID) {
            	maxID = landRec.getLandID();
            }
        }
        return maxID + 1;
    }

//    public List<LandRec> printTransactionInfo(int mode) {
//        List<LandRec> landRecs = readTransRec();
//
//        if (mode == 1) {		//read pending land rec only
//    	    List<LandRec> filteredTransactions = landRecs.stream()
//    		        .filter(landRec -> landRec.getRegStatus() == enuum.status.PENDING)
//    		        .collect(Collectors.toList());
//    	    landRecs = filteredTransactions;
//    		System.out.printf("\n\n%" + 50 + "s%s%n", "", "Pending Transaction");
//    		System.out.println("-".repeat(130));
//        }else {
//            System.out.printf("\n\n%" + 50 + "s%s%n", "", "Transaction Information");
//            System.out.println("-".repeat(130));
//        }
//        
//	    if (landRecs.isEmpty()) {
//	        System.out.println("No pending transactions.");
//	    } else {
//	        System.out.printf("%-4s | %-7s | %-6s | %-6s | %-10s | %-12s | %-20s | %-25s | %-10s%n",
//	                "ID", "Land ID", "Buyer", "Seller", "Amount", "Pay Method", "Type", "Recorded Date", " Status");
//	        System.out.println("-".repeat(130));
//
//	        for (LandRec landRec : landRecs) {
//	            System.out.printf("%-4s | %-7s | %-6s | %-6s | %-10s | %-12s | %-20s | %-25s | %-10s%n",
//	            		landRec.getTransID(), landRec.getLandID(), landRec.getBuyerID(), landRec.getSellerID(),
//	            		landRec.getAmount(), landRec.getPaymentMethod(), landRec.getTransType(),
//	            		landRec.getRecDate(), landRec.getTranStatus());
//	        }
//	    }
//
//        System.out.println("-".repeat(130));
//        
//        return transactions;
//    }
//
//    public TransRec displayCurrentTransactionInfo(int transID) {
//        List<TransRec> transactions = readTransRec();
//
//        List<TransRec> filteredTransactions = transactions.stream()
//                .filter(transaction -> transaction.getTransID() == transID)
//                .collect(Collectors.toList());
//
//        if (filteredTransactions.size() == 1) {
//            TransRec transaction = filteredTransactions.get(0);
//
//            System.out.println("\n");
//            System.out.println("*".repeat(50));
//            System.out.println("Updated Transaction Information:");
//            System.out.printf("Transaction ID #" + transaction.getTransID() +
//                    "\nLand ID\t\t:" + transaction.getLandID() +
//                    "\nBuyer ID\t\t:" + transaction.getBuyerID() +
//                    "\nSeller ID\t\t:" + transaction.getSellerID() +
//                    "\nAmount\t\t\t:" + transaction.getAmount() +
//                    "\nPayment Method\t\t:" + transaction.getPaymentMethod() +
//                    "\nTransaction Type\t:" + transaction.getTransType() +
//                    "\nTransaction Status\t:" + transaction.getTranStatus() +
//                    "\nRecorded Date\t\t:" + transaction.getRecDate() + "\n");
//            System.out.println("*".repeat(50));
//            System.out.println("\n");
//            return transaction;
//        } else if (filteredTransactions.isEmpty()) {
//            System.out.println("** Transaction ID not found. **\n");
//        } else {
//            System.out.println("** Multiple entries found for the same Transaction ID. **\n");
//        }
//		return null;
//    }
	
    public void newLandRec(int mode, int landID, int ownerID, int transID) {
        Scanner scanner = new Scanner(System.in);
        
        List<LandRec> landRecs = readLandRec();

        int recID = generateNewLandRecID(landRecs);
        
        landStatus landStatus = enuum.landStatus.OWNED;
        
        status regStatus = enuum.status.PENDING;
        
        //create pending transaction n pending reg
        LandRec newLandRec = new LandRec(recID, landID, ownerID, transID, landStatus, regStatus);
        
        FileHandler.addObject(newLandRec, LANDREC_FILE);
        
        System.out.println("** Land Record Created. **");
    }
    
    // update status to COMPLETE
	// user can choose to approve all transactions by input specific transRecID to approve
	
	public void approveLandRec() {
//	    Scanner scanner = new Scanner(System.in);
//
//	    List<TransRec> allTransactions = readTransRec();
//	    
//	    // Display a list of transactions pending approval
//	    List<TransRec> pendingTransactions = allTransactions.stream()
//	            .filter(transaction -> transaction.getTranStatus() == enuum.status.PENDING)
//	            .collect(Collectors.toList());
//
//	    if (pendingTransactions != null) {
//		    // Ask the user to input the transaction ID to approve
//		    System.out.print("Transaction ID to approve (0 to approve all): ");
//		    int transRecID = scanner.nextInt();
//
//		    if (transRecID == 0) {
//		        // Approve all transactions
//		        for (TransRec transaction : pendingTransactions) {
//		            transaction.setTranStatus(enuum.status.COMPLETE);
//		        }
//		        System.out.println("** All transactions approved. **");
//		    } else {
//		        // Approve a specific transaction by updating its status
//		        TransRec transactionToApprove = pendingTransactions.stream()
//		                .filter(transaction -> transaction.getTransID() == transRecID)
//		                .findFirst()
//		                .orElse(null);
//
//		        if (transactionToApprove != null) {
//		            transactionToApprove.setTranStatus(enuum.status.COMPLETE);
//		            System.out.println("** Transaction ID #" + transRecID + " approved. **");
//		        } else {
//		            System.out.println("** Transaction not found or already approved. **");
//		        }
//		    }
//
//		    // Update the transaction file with the modified transactions
//		    FileHandler.writeData(allTransactions, TRANSACTION_FILE);
//	    }
	}
}
