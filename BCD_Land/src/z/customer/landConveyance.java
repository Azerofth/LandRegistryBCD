package z.customer;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
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
	
//	mode 1, filters for "My Onsale Land" (current user and on sale).
//	mode 2, filters for "My Owned Land" (current user and owned, not on sale).
//	mode 3, filters for "Not My Onsale Land" (not the current user but on sale).

	//need check if transaction done then remove
	public List<LandRec> getLandRecList(int mode, int ownerID) {
	    LandInfoHandler lih = new LandInfoHandler();
	    LandRecHandler lrh = new LandRecHandler();
	    TransHandler th = new TransHandler();

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

	    List<LandRec> landRecList = new ArrayList<>();

	    if (mode == 1) {
	        for (LandInfo landInfo : landInfos) {
	            int landID = landInfo.getLandID();
	            LandRec latestLandRec = latestLandRecs.get(latestRecIDs.getOrDefault(landID, 0));

	            // current user and on sale
	            if (latestLandRec != null && latestLandRec.getOwnerID() == ownerID && latestLandRec.getLandStatus() == enuum.landStatus.ONSALE) {
	                landRecList.add(latestLandRec);
	            }
	        }
	    } else if (mode == 2) {
	        for (LandInfo landInfo : landInfos) {
	            int landID = landInfo.getLandID();
	            LandRec latestLandRec = latestLandRecs.get(latestRecIDs.getOrDefault(landID, 0));

	            // current user and owned (not on sale)
	            if (latestLandRec != null && latestLandRec.getOwnerID() == ownerID && latestLandRec.getLandStatus() == enuum.landStatus.OWNED) {
	                landRecList.add(latestLandRec);
	            }
	        }
	    } else if (mode == 3) {
	        for (LandInfo landInfo : landInfos) {
	            int landID = landInfo.getLandID();
	            LandRec latestLandRec = latestLandRecs.get(latestRecIDs.getOrDefault(landID, 0));

	            // not current user but on sale
	            if (latestLandRec != null && latestLandRec.getOwnerID() != ownerID && latestLandRec.getLandStatus() == enuum.landStatus.ONSALE) {
	                landRecList.add(latestLandRec);
	            }
	        }
	    }

	    return landRecList;
	}

	
//	mode 1, filters for "My Onsale Land" (current user and on sale).
//	mode 2, filters for "My Owned Land" (current user and owned, not on sale).
//	mode 3, filters for "Not My Onsale Land" (not the current user but on sale).
	
	public void printLandInfoWLandRec(int mode, List<LandRec> landRecList, int ownerID) {
	    LandInfoHandler lih = new LandInfoHandler();
	    String title = "";

	    if (mode == 1) {
	        title = "My Onsale Land";
	    } else if (mode == 2) {
	        title = "My Owned Land";
	    } else if (mode == 3) {
	        title = "Onsale Land";
	    }

	    System.out.printf("\n\n%" + 50 + "s%s%n", "", title);
	    System.out.println("-".repeat(130));
	    System.out.printf("%-4s | %-9s | %-9s | %-9s | %-5s | %-6s | %-25s | %-10s | %-15s | %-15s%n",
	            "ID", "Area", "Height", "Volume", "Year", "Owner", "Registered Date", "Condition", "Value", "Status");
	    System.out.println("-".repeat(130));

	    for (LandRec landRec : landRecList) {
	        // Get the corresponding landInfo for the current LandRec
	        LandInfo landInfo = lih.getLandInfoByLandID(landRec.getLandID());

	        if (landInfo != null) {
	            // Get the latest owner information from LandRec
	            int latestOwnerID = landRec.getOwnerID();

	            // Print land information along with the latest regStatus and owner
	            System.out.printf("%-4s | %-9s | %-9s | %-9s | %-5s | %-6s | %-25s | %-10s | %-15s | %-15s%n",
	                    landInfo.getLandID(), landInfo.getLandArea(), landInfo.getLandHeight(), landInfo.getLandVolume(),
	                    landInfo.getYearOfCon(), latestOwnerID, landInfo.getRegDate(), landInfo.getLandCond(),
	                    landInfo.getValue(), landRec.getRegStatus());
	        }
	    }
	    System.out.println("-".repeat(130));
	}
	
	public void userSellLand() throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
	    Scanner scanner = new Scanner(System.in);
	    TransHandler th = new TransHandler();
	    User currentUser = login.getCurrentUser();

	    // my onsale land
	    List<LandRec> myOnsale = getLandRecList(1, currentUser.getUserID());
	    printLandInfoWLandRec(1, myOnsale, currentUser.getUserID());

	    // my owned not on sale land
	    List<LandRec> filteredRec = getLandRecList(2, currentUser.getUserID());
	    printLandInfoWLandRec(2, filteredRec, currentUser.getUserID());

	    System.out.print("Do you want to put any land on sale? (Y/N): ");
	    String option = scanner.nextLine().toUpperCase();

	    if (option.equals("Y")) {
	        // Prompt the user to enter a valid Land ID from the displayed list
	        while (true) {
	            System.out.print("Enter Land ID to put on sale (0 to cancel): ");
	            int selectedLandID = scanner.nextInt();

	            if (selectedLandID == 0) {
	                System.out.println("\n** Sale operation canceled. **");
	                break;
	            }

	            // Check if the entered landID is in the list of filteredRec
	            boolean isValidLandID = filteredRec.stream().anyMatch(rec -> rec.getLandID() == selectedLandID);

	            if (isValidLandID) {
	                // Land ID is valid, create a new transaction
	                final int landID = selectedLandID; // effectively final
	                th.newTransaction(2, landID, currentUser.getUserID(), 0); // set as sellerID 0 because not sold yet

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



	public void userBuyLand() throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
	    Scanner scanner = new Scanner(System.in);
	    TransHandler th = new TransHandler();
	    User currentUser = login.getCurrentUser();

	    // get OTHERS onsale land
	    List<LandRec> onsaleLand = getLandRecList(3, currentUser.getUserID());
	    printLandInfoWLandRec(3, onsaleLand, currentUser.getUserID());

	    System.out.print("Do you want to buy any land? (Y/N): ");
	    String option = scanner.nextLine().toUpperCase();

	    if (option.equals("Y")) {
	        // Prompt the user to enter a valid Land ID from the displayed list
	        while (true) {
	            System.out.print("Enter Land ID to buy (0 to cancel): ");
	            int selectedLandID = scanner.nextInt();

	            if (selectedLandID == 0) {
	                System.out.println("\n** Purchase operation canceled. **");
	                break;
	            }

	            // Check if the entered landID is in the list of onsaleLand
	            boolean isValidLandID = onsaleLand.stream().anyMatch(rec -> rec.getLandID() == selectedLandID);

	            if (isValidLandID) {
	                // Find the LandRec associated with the selectedLandID
	                LandRec selectedLandRec = onsaleLand.stream()
	                        .filter(rec -> rec.getLandID() == selectedLandID)
	                        .findFirst()
	                        .orElse(null);

	                if (selectedLandRec != null) {
	                    // Find the transaction associated with the selectedLandRec
	                    TransRec transRec = th.getTransRecByTransID(selectedLandRec.getTransID());

	                    if (transRec != null) {
	                        // Get the sellerID from the latest buyer's userID
	                        int sellerID = transRec.getBuyerID();

	                        // Land ID is valid, create a new transaction
	                        th.newTransaction(3, selectedLandID, currentUser.getUserID(), sellerID);

	                        System.out.println("\n** Land with ID " + selectedLandID + " has been purchased. Wait for approval. **");
	                        break;
	                    }
	                }
	            } else {
	                System.out.println("\n** Invalid Land ID. Please enter a valid Land ID from the list. **");
	            }
	        }
	    } else {
	        System.out.println("\n** No land is purchased. **");
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
