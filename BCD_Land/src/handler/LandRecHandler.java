package handler;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import enuum.landStatus;
import enuum.status;
import model.LandRec;
import model.TransRec;

public class LandRecHandler {
    static LoginHandler login = LoginHandler.getInstance();

	private static final String LANDREC_FILE = "landRec.txt";
	
	public List<LandRec> readLandRec() {
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

    public void printLandRec() {
    	TransHandler th = new TransHandler();
    	
        List<LandRec> landRecs = readLandRec();
        List<TransRec> transRecs = th.readTransRec();

        // Create a map to store the latest LandRec for each recID
        Map<Integer, LandRec> latestLandRecs = new HashMap<>();

        // Iterate through LandRecs to find the latest for each recID
        for (LandRec landRec : landRecs) {
            int recID = landRec.getRecID();
            latestLandRecs.put(recID, latestLandRecs.getOrDefault(recID, landRec));
        }

        System.out.printf("\n\n%" + 40 + "s%s%n", "", "Land Record");
        System.out.println("-".repeat(100));
        System.out.printf("%-9s | %-9s | %-9s | %-9s | %-15s | %-15s | %-15s%n",
                "RecID", "LandID", "OwnerID", "TransID", "TranStatus", "LandStatus", "RegStatus");
        System.out.println("-".repeat(100));

        for (LandRec landRec : latestLandRecs.values()) {
            // Find corresponding TransRec for the LandRec
            TransRec transRec = transRecs.stream()
                    .filter(tr -> tr.getTransID() == landRec.getTransID())
                    .findFirst()
                    .orElse(new TransRec()); // If not found, create an empty TransRec

            // Print LandRec and TranRec information
            System.out.printf("%-9s | %-9s | %-9s | %-9s | %-15s | %-15s | %-15s%n",
                    landRec.getRecID(), landRec.getLandID(), landRec.ownerID(), landRec.getTransID(),
                    transRec.getTranStatus(), landRec.getLandStatus(), landRec.getRegStatus());
        }

        System.out.println("-".repeat(100));
    }


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
        Scanner scanner = new Scanner(System.in);
    	TransHandler th = new TransHandler();

        // Read LandRecs
        List<LandRec> allLandRecs = readLandRec();
        
        // Read Transactions
        List<TransRec> allTransactions = th.readTransRec();

        // Filter LandRecs with specified conditions
        List<LandRec> landRegistrationsToApprove = allLandRecs.stream()
                .filter(landRec -> landRec.getRegStatus() == enuum.status.PENDING &&
                        isTransactionStatusComplete(allTransactions, landRec.getTransID()))
                .collect(Collectors.toList());

        if (!landRegistrationsToApprove.isEmpty()) {
            // Display LandID with the specified conditions
            System.out.println("Land ID with pending registration:");
            landRegistrationsToApprove.forEach(landRec ->
                    System.out.println("LandID # " + landRec.getLandID()));

            // Ask the user to input LandID to approve (0 to approve all)
            System.out.print("LandID to approve (0 to approve all): ");
            int landIDToApprove = scanner.nextInt();

            if (landIDToApprove == 0) {
                // Approve all land registrations
                landRegistrationsToApprove.forEach(landRec ->
                        landRec.setRegStatus(enuum.status.COMPLETE));
                System.out.println("** All land registrations approved. **");
            } else {
                // Approve a specific land registration by updating its status
                LandRec landRegistrationToApprove = landRegistrationsToApprove.stream()
                        .filter(landRec -> landRec.getLandID() == landIDToApprove)
                        .findFirst()
                        .orElse(null);

                if (landRegistrationToApprove != null) {
                    landRegistrationToApprove.setRegStatus(enuum.status.COMPLETE);
                    System.out.println("** LandID #" + landIDToApprove + " registration approved. **");
                } else {
                    System.out.println("** Land registration not found or already approved. **");
                }
            }

            // Update the LandRec file with the modified records
            FileHandler.writeData(allLandRecs, LANDREC_FILE);
        } else {
            System.out.println("** No pending land registrations found. Check any pending transaction. **");
        }
    }

    private boolean isTransactionStatusComplete(List<TransRec> transactions, int transID) {
        return transactions.stream()
                .anyMatch(transaction -> transaction.getTransID() == transID &&
                        transaction.getTranStatus() == enuum.status.COMPLETE &&
                        transaction.getTransType() == enuum.transType.REGISTRATIONOFTITLE);
    }

	
}
