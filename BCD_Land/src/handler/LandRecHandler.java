package handler;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import blockchain.Blockchain;
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
            if (landRec.getRecID() > maxID) {
            	maxID = landRec.getRecID();
            }
        }
        return maxID + 1;
    }

    // Assuming you have a method to get a LandRec by landID in your LandRecHandler
    private LandRec getLandRecByRecID(int recID) {
    	List<LandRec> landRecs = readLandRec();
    	
        for (LandRec landRec : landRecs) {
            if (landRec.getTransID() == recID) {
                return landRec;
            }
        }
        
        return null;
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
                    landRec.getRecID(), landRec.getLandID(), landRec.getOwnerID(), landRec.getTransID(),
                    transRec.getTranStatus(), landRec.getLandStatus(), landRec.getRegStatus());
        }

        System.out.println("-".repeat(100));
    }


    public LandRec displayCurrentLandRecByID(int recID) {
        LandRec landRec = getLandRecByRecID(recID);

        if (landRec != null) {
        	System.out.println("\n");
            System.out.println("*".repeat(50));
            System.out.println("                Land Record");
            System.out.printf("Record ID  #" + landRec.getRecID() +
                    "\nLand ID\t\t\t: " + landRec.getLandID() +
                    "\nOwner ID\t\t: " + landRec.getOwnerID() +
                    "\nTransaction ID\t\t: " + landRec.getTransID() +
                    "\nLand Status\t\t: " + landRec.getLandStatus() +
                    "\nRegistration Status\t: " + landRec.getRegStatus() + "\n");
            System.out.println("*".repeat(50));
            System.out.println("\n");
            return landRec;
        } else {
            System.out.println("** Land ID not found or no land records available. **\n");
        }
        return null;
    }
    
	
    public void newLandRec(int mode, int landID, int ownerID, int transID) {
        Scanner scanner = new Scanner(System.in);
        LandRec newLandRec = null;

        List<LandRec> landRecs = readLandRec();
        int recID = generateNewLandRecID(landRecs);
        
        if (mode == 1) {		//when register land
            landStatus landStatus = enuum.landStatus.OWNED;
            status regStatus = enuum.status.PENDING;
            newLandRec = new LandRec(recID, landID, ownerID, transID, landStatus, regStatus);
        } else if (mode == 2) {		//when sell land
            landStatus landStatus = enuum.landStatus.ONSALE;
            status regStatus = enuum.status.COMPLETE;
            newLandRec = new LandRec(recID, landID, ownerID, transID, landStatus, regStatus);
        } else if (mode == 3) {		//when buy land
            landStatus landStatus = enuum.landStatus.OWNED;
            status regStatus = enuum.status.COMPLETE;
            newLandRec = new LandRec(recID, landID, ownerID, transID, landStatus, regStatus);
        }
        
        FileHandler.addObject(newLandRec, LANDREC_FILE);
        
        System.out.println("** Land Record Created. **");
        

        displayCurrentLandRecByID(recID);
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
                String ApprovedRegistration = landRegistrationsToApprove.toString();
                Blockchain.createBlockchain(ApprovedRegistration);
                
            } else {
                // Approve a specific land registration by updating its status
                LandRec landRegistrationToApprove = landRegistrationsToApprove.stream()
                        .filter(landRec -> landRec.getLandID() == landIDToApprove)
                        .findFirst()
                        .orElse(null);

                if (landRegistrationToApprove != null) {
                    landRegistrationToApprove.setRegStatus(enuum.status.COMPLETE);
                    System.out.println("** LandID #" + landIDToApprove + " registration approved. **");
                    String ApprovedRegistration = landRegistrationToApprove.toString();
                    Blockchain.createBlockchain(ApprovedRegistration);
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
