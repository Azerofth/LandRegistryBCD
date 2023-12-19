package z.customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import handler.LandInfoHandler;
import handler.LandRecHandler;
import handler.LoginHandler;
import handler.TransHandler;
import model.LandInfo;
import model.LandRec;
import model.TransRec;
import model.User;

public class landConveyance {
	LoginHandler login = LoginHandler.getInstance();
	
	private static void printLandInfoWLandRec(int ownerID) {
	    LandInfoHandler lih = new LandInfoHandler();
	    LandRecHandler lrh = new LandRecHandler();
	    TransHandler th = new TransHandler(); // Assuming you have a TransRecHandler

	    List<LandInfo> landInfos = lih.readLandInfo();
	    List<LandRec> landRecs = lrh.readLandRec();

	    // Create a map to store the latest LandRec for each landID
	    Map<Integer, LandRec> latestLandRecs = new HashMap<>();
	    Map<Integer, Integer> latestRecIDs = new HashMap<>(); // Map to store the latest RecID for each LandID

	    // Iterate through LandRecs to find the latest for each landID with transID's tranStatus is COMPLETE
	    for (LandRec landRec : landRecs) {
	        if (landRec.getRegStatus() == enuum.status.COMPLETE) {
	            int recID = landRec.getRecID();

	            // Get the corresponding transRec for the current LandRec
	            TransRec transRec = th.getTransRecByTransID(landRec.getTransID());

	            // Check if transRec's tranStatus is also COMPLETE
	            if (transRec != null && transRec.getTranStatus() == enuum.status.COMPLETE) {
	                // Keep only the latest LandRec for each recID
	                latestLandRecs.put(recID, landRec);

	                // Keep track of the latest RecID for each LandID
	                int landID = landRec.getLandID();
	                if (!latestRecIDs.containsKey(landID) || recID > latestRecIDs.get(landID)) {
	                    latestRecIDs.put(landID, recID);
	                }
	            }
	        }
	    }

	    System.out.printf("\n\n%" + 50 + "s%s%n", "", "Owned Land");
	    System.out.println("-".repeat(130));
	    System.out.printf("%-4s | %-9s | %-9s | %-9s | %-5s | %-6s | %-25s | %-10s | %-15s | %-15s%n",
	            "ID", "Area", "Height", "Volume", "Year", "Owner", "Registered Date", "Condition", "Value", "Status");
	    System.out.println("-".repeat(130));

	    for (LandInfo landInfo : landInfos) {
	        int landID = landInfo.getLandID();
	        LandRec latestLandRec = latestLandRecs.get(latestRecIDs.getOrDefault(landID, 0));

	        if (latestLandRec != null && latestLandRec.getOwnerID() == ownerID && latestLandRec.getLandStatus() == enuum.landStatus.OWNED) {
	            // Get the latest owner information from LandRec
	            int latestOwnerID = latestLandRec.getOwnerID();

	            // Print land information along with the latest regStatus and owner
	            System.out.printf("%-4s | %-9s | %-9s | %-9s | %-5s | %-6s | %-25s | %-10s | %-15s | %-15s%n",
	                    landInfo.getLandID(), landInfo.getLandArea(), landInfo.getLandHeight(), landInfo.getLandVolume(),
	                    landInfo.getYearOfCon(), latestOwnerID, landInfo.getRegDate(), landInfo.getLandCond(),
	                    landInfo.getValue(), latestLandRec.getRegStatus());
	        }
	    }

	    System.out.println("-".repeat(130));
	    
	    
	    System.out.printf("\n%" + 50 + "s%s%n", "", "Onsale Land");
	    System.out.println("-".repeat(130));
	    System.out.printf("%-4s | %-9s | %-9s | %-9s | %-5s | %-6s | %-25s | %-10s | %-15s | %-15s%n",
	            "ID", "Area", "Height", "Volume", "Year", "Owner", "Registered Date", "Condition", "Value", "Status");
	    System.out.println("-".repeat(130));

	    for (LandInfo landInfo : landInfos) {
	        int landID = landInfo.getLandID();
	        LandRec latestLandRec = latestLandRecs.get(latestRecIDs.getOrDefault(landID, 0));

	        if (latestLandRec != null && latestLandRec.getOwnerID() == ownerID && latestLandRec.getLandStatus() == enuum.landStatus.ONSALE) {
	            // Get the latest owner information from LandRec
	            int latestOwnerID = latestLandRec.getOwnerID();

	            // Print land information along with the latest regStatus and owner
	            System.out.printf("%-4s | %-9s | %-9s | %-9s | %-5s | %-6s | %-25s | %-10s | %-15s | %-15s%n",
	                    landInfo.getLandID(), landInfo.getLandArea(), landInfo.getLandHeight(), landInfo.getLandVolume(),
	                    landInfo.getYearOfCon(), latestOwnerID, landInfo.getRegDate(), landInfo.getLandCond(),
	                    landInfo.getValue(), latestLandRec.getRegStatus());
	        }
	    }

	    System.out.println("-".repeat(130));
	}
	
	private static List<Integer> getDisplayedLandIDs(int ownerID) {
	    LandInfoHandler lih = new LandInfoHandler();
	    LandRecHandler lrh = new LandRecHandler();
	    TransHandler th = new TransHandler();

	    List<LandInfo> landInfos = lih.readLandInfo();
	    List<LandRec> landRecs = lrh.readLandRec();

	    // Create a map to store the latest LandRec for each landID
	    Map<Integer, LandRec> latestLandRecs = new HashMap<>();

	    // Iterate through LandRecs to find the latest for each landID with transID's tranStatus is COMPLETE
	    for (LandRec landRec : landRecs) {
	        if (landRec.getRegStatus() == enuum.status.COMPLETE) {
	            int recID = landRec.getRecID();

	            // Get the corresponding transRec for the current LandRec
	            TransRec transRec = th.getTransRecByTransID(landRec.getTransID());

	            // Check if transRec's tranStatus is also COMPLETE
	            if (transRec != null && transRec.getTranStatus() == enuum.status.COMPLETE) {
	                // Keep only the latest LandRec for each recID
	                latestLandRecs.put(recID, landRec);
	            }
	        }
	    }

	    // Get the list of landIDs displayed to the user
	    return latestLandRecs.values().stream()
	            .filter(l -> l.getOwnerID() == ownerID)
	            .map(LandRec::getLandID)
	            .collect(Collectors.toList());
	}
	
	public void userSellLand() {
	    Scanner scanner = new Scanner(System.in);
	    TransHandler th = new TransHandler();
	    int landID;
	    User currentUser = login.getCurrentUser();

	    // Display land information for the current user
	    printLandInfoWLandRec(currentUser.getUserID());

	    // Get the list of landIDs displayed to the user
	    List<Integer> displayedLandIDs = getDisplayedLandIDs(currentUser.getUserID());

	    System.out.print("Do you want to put any land on sale? (Y/N): ");
	    String option = scanner.nextLine().toUpperCase();

	    if (option.equals("Y")) {
	        // Prompt the user to enter a valid Land ID from the displayed list
	        while (true) {
	            System.out.print("Enter Land ID to put on sale (0 to cancel): ");
	            landID = scanner.nextInt();

	            if (landID == 0) {
	                System.out.println("\n** Sale operation canceled. **");
	                break;
	            }

	            if (displayedLandIDs.contains(landID)) {
	                // Land ID is valid, create a new transaction
	                th.newTransaction(2, landID, currentUser.getUserID(), 0); // set as sellerID 0 because not sold yet

	                // Add your logic to handle putting the land on sale with the given landID
	                // For example, you can update the LandRec or LandInfo to mark it as "on sale"
	                // and store this information in your files.

	                System.out.println("\n** Land with ID " + landID + " has been put on sale. **");
	                break;
	            } else {
	                System.out.println("\n** Invalid Land ID. Please enter a valid Land ID from the list. **");
	            }
	        }
	    } else {
	        System.out.println("\n** No land is put on sale. **");
	    }
	}



	
	// buy land
	// display ALL land ON SALE & RecID's TransID's transaction COMPLETE
	// select landID
	// transaction
	// approve transaction
	// set landrec with buyer ID
	
	//check if owner changed

}
