package handler;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import blockchain.Blockchain;
import enuum.status;
import model.LandInfo;
import model.LandRec;
import model.TransRec;
import model.User;

public class LandInfoHandler {
	private static final String LANDINFO_FILE = "landInfo.txt";

    public List<LandInfo> readLandInfo() {
        return FileHandler.readData(LANDINFO_FILE);
    }
	
    public void printLandInfo(int ownerID) {
        LandRecHandler lrh = new LandRecHandler();
        TransHandler th = new TransHandler();

        List<LandInfo> landInfos = readLandInfo();
        List<LandRec> landRecs = lrh.readLandRec();

        if (ownerID != 000) {
            List<LandInfo> filteredLandInfos = landInfos.stream()
                    .filter(landInfo -> landInfo.getOwner() == ownerID)
                    .collect(Collectors.toList());
            landInfos = filteredLandInfos;
        }

        // Create a map to store the latest LandRec for each landID
        Map<Integer, LandRec> latestLandRecs = new HashMap<>();

     // Iterate through LandRecs to find the latest for each landID
        for (LandRec landRec : landRecs) {
            int landID = landRec.getLandID();
            // Keep only the latest LandRec for each landID
            if (!latestLandRecs.containsKey(landID) || landRec.getRecID() > latestLandRecs.get(landID).getRecID()) {
                latestLandRecs.put(landID, landRec);
            }
        }

        System.out.printf("\n\n%" + 50 + "s%s%n", "", "Registered Land");
        System.out.println("-".repeat(130));
        System.out.printf("%-4s | %-9s | %-9s | %-9s | %-5s | %-6s | %-25s | %-10s | %-15s | %-15s%n",
                "ID", "Area", "Height", "Volume", "Year", "Owner", "Registered Date", "Condition", "Value", "Status");
        System.out.println("-".repeat(130));

        for (LandInfo landInfo : landInfos) {
            int landID = landInfo.getLandID();
            LandRec latestLandRec = latestLandRecs.get(landID);

            if (latestLandRec != null) {
                // Get the latest owner information from TransRec
                TransRec latestTransRec = th.readTransRec().stream()
                        .filter(transRec -> transRec.getTransID() == latestLandRec.getTransID())
                        .findFirst()
                        .orElse(null);

                if (latestTransRec != null) {
                    int latestOwnerID = latestTransRec.getBuyerID();
                    
                    // Print land information along with the latest regStatus and owner
                    System.out.printf("%-4s | %-9s | %-9s | %-9s | %-5s | %-6s | %-25s | %-10s | %-15s | %-15s%n",
                            landInfo.getLandID(), landInfo.getLandArea(), landInfo.getLandHeight(), landInfo.getLandVolume(),
                            landInfo.getYearOfCon(), latestOwnerID, landInfo.getRegDate(), landInfo.getLandCond(),
                            landInfo.getValue(), latestLandRec.getRegStatus());
                }
            }
        }

        System.out.println("-".repeat(130));
    }

    
//    public void displayCurrentLandInfo(int landID) {
//        List<LandInfo> landInfos = readLandInfo();
//
//        List<LandInfo> filteredLandInfos = landInfos.stream()
//                .filter(landInfo -> landInfo.getLandID() == landID)
//                .collect(Collectors.toList());
//
//        if (filteredLandInfos.size() == 1) {
//            LandInfo landInfo = filteredLandInfos.get(0);
//
//            System.out.println("\n");
//            System.out.println("*".repeat(50));
//            System.out.println("Updated Registered Land Information:");
//            System.out.printf("Land ID #" + landInfo.getLandID() +
//                    "\nArea\t\t:" + landInfo.getLandArea() +
//                    "\nHeight\t\t:" + landInfo.getLandHeight() +
//                    "\nVolume\t\t\t:" + landInfo.getLandVolume() +
//                    "\nYear\t\t\t:" + landInfo.getYearOfCon() +
//                    "\nOwner\t\t:" + landInfo.getOwner() +
//                    "\nRegistered Date\t\t:" + landInfo.getRegDate() +
//                    "\nCondition\t\t:" + landInfo.getLandCond() +
//                    "\nValue\t\t:" + landInfo.getValue() + "\n");
//            System.out.println("*".repeat(50));
//            System.out.println("\n");
//        } else if (filteredLandInfos.isEmpty()) {
//            System.out.println("** Land ID not found. **\n");
//        } else {
//            System.out.println("** Multiple entries found for the same Land ID. **\n");
//        }
//    }
    
    private static int generateNewLandInfoID(List<LandInfo> landInfos) {
        // Find the maximum userID from existing users and increment by 1
        int maxID = 0;
        for (LandInfo landInfo : landInfos) {
            if (landInfo.getLandID() > maxID) {
            	maxID = landInfo.getLandID();
            }
        }
        return maxID + 1;
    }
	
	// do basic input new land first
	// then add mode for customer usage

	// registerLand
    // mode to pass in, owner
	public void addLandInfo(int mode) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
		
        Scanner scanner = new Scanner(System.in);
        LoginHandler login = LoginHandler.getInstance();
        TransHandler th = new TransHandler();
        
        User currentUser = null;
        
        List<LandInfo> landInfo = readLandInfo();
        
    	if (mode == 1) {
    		currentUser = login.getCurrentUser();
    		printLandInfo(currentUser.getUserID());
    		//customer use
    	} else {
    		printLandInfo(000);	//admin use
    	}
        
        int landID = generateNewLandInfoID(landInfo);
        Timestamp regDate = new Timestamp(System.currentTimeMillis());
        
        
        System.out.printf("\nLand ID #%d", landID);
        
        System.out.print("\nLand Area\t\t: ");
        double landArea = scanner.nextDouble();
        scanner.nextLine(); 
        
        System.out.print("Land Height\t\t: ");
        double landHeight = scanner.nextDouble();
        scanner.nextLine(); 
        
        System.out.print("Land Volume\t\t: ");
        double landVolume = scanner.nextDouble();
        scanner.nextLine(); 
        
        System.out.print("Year of Construction\t: ");
        int yearOfCon = scanner.nextInt();
        scanner.nextLine(); 
        
        int owner;
		if (mode == 1) {
	        owner = currentUser.getUserID();			//if customer mode then set currentUser as owner
		}else {
	        System.out.print("Owner ID\t\t: ");
	        owner = scanner.nextInt();
	        scanner.nextLine(); 
		}
        
        System.out.print("Land Condition (max 5)\t: ");
        int landCond = scanner.nextInt();
        scanner.nextLine(); 
        
        System.out.print("Value\t\t\t: ");
        double value = scanner.nextDouble();
        scanner.nextLine(); 

        // Create a new user object
        LandInfo newLandInfo = new LandInfo(landID, landArea, landHeight, landVolume, yearOfCon, owner, regDate, landCond, value);

        boolean transaction = th.newTransaction(1, landID, owner, 0);
        
        if (transaction == true) {
            FileHandler.addObject(newLandInfo, LANDINFO_FILE);
            
            System.out.println("** Successfully registered new land. Please patiently wait for approval. **");
            String newLand = newLandInfo.toString();
            Blockchain.createBlockchain(newLand);
        } else {
        	System.out.println("** Failed register new land. **");
        }
	}

	public LandInfo getLandInfoByLandID(int landID) {
		List<LandInfo> allLandInfos = readLandInfo();
		
        for (LandInfo landInfo : allLandInfos) {
            if (landInfo.getLandID() == landID) {
                return landInfo;
            }
        }
        
		return null;
	}
	
	// search for ID
	// mode:	admin - 000, customer/seller - 1
//	public void modifyLandInfo(int mode) {
//		
//        Scanner scanner = new Scanner(System.in);
//        LoginHandler login = LoginHandler.getInstance();
//        
//        int landInputToModify = 0;
//        LandInfo landToModify = null;
//        LandInfo updatedLandInfo = null;
//        int newOwner = 0;
//        
//        // Display existing users for reference
//        List<LandInfo> landInfos = readLandInfo();
//
//		if (mode == 1) {
//			printLandInfo(login.getCurrentUserID());
//            List<LandInfo> filteredLandInfos = landInfos.stream()
//                    .filter(landInfo -> landInfo.getOwner() == login.getCurrentUserID())
//                    .collect(Collectors.toList());
//            landInfos = filteredLandInfos;
//		}else {
//			printLandInfo(000);
//		}
//		
//        System.out.print("Enter Land ID to modify: ");
//        landInputToModify = scanner.nextInt();
//        scanner.nextLine();
//    
//        // Find the user with the specified username or user ID
//	    for (int i = 0; i < landInfos.size(); i++) {
//	        LandInfo landInfo = landInfos.get(i);
//	        if (landInfo.getLandID() == landInputToModify) {
//	        	landToModify = landInfo;
//	            break;
//	        }
//	    }
//
//	       if (landToModify != null) {
//	            // Collect updated user details from user input
//	           System.out.print("\nNew Land Area\t\t\t: ");
//	           double newLandArea = scanner.nextDouble();
//	           scanner.nextLine(); 
//	           
//	           System.out.print("New Land Height\t\t\t: ");
//	           double newLandHeight = scanner.nextDouble();
//	           scanner.nextLine(); 
//	           
//	           System.out.print("New Land Volume\t\t\t: ");
//	           double newLandVolume = scanner.nextDouble();
//	           scanner.nextLine(); 
//	           
//	           System.out.print("New Year of Construction\t: ");
//	           int newYearOfCon = scanner.nextInt();
//	           scanner.nextLine(); 
//	           
//	           if (mode == 000) {
//		           System.out.print("New Owner ID\t\t\t: ");			//if customer mode then set currentUser as owner
//		           newOwner = scanner.nextInt();
//		           scanner.nextLine(); 
//	           } else {
//	        	   newOwner = login.getCurrentUserID();
//	           }
//	           
//	           System.out.print("New Land Condition (max 5)\t: ");
//	           int newLandCond = scanner.nextInt();
//	           scanner.nextLine(); 
//	           
//	           System.out.print("New Value\t\t\t: ");
//	           double newValue = scanner.nextDouble();
//	           scanner.nextLine(); 
//
//	            // Create a new LandInfo object with updated details
//	            updatedLandInfo = new LandInfo(
//	            		landToModify.getLandID(),
//	            		newLandArea,
//	            		newLandHeight,
//	            		newLandVolume,
//	            		newYearOfCon,
//	            		newOwner,
//	            		landToModify.getRegDate(),
//	            		newLandCond,
//	            		newValue
//	            );
//	            
//	            // Replace the old user with the updated user in the list
//	            landInfos.set(landInfos.indexOf(landToModify), updatedLandInfo);
//	            // Write the updated user list to the file
//	            FileHandler.writeData(landInfos, LANDINFO_FILE);
//
//	            System.out.println("** Land Information modified successfully. **");
//	            System.out.println(updatedLandInfo);
//	            
//	            printLandInfo(mode); // Display updated user list
//	        } else {
//	            System.out.println("** Land not found. **");
//	        }
//	}
	
	
	//note to create landRec when transferation happen
//	public void newLandOwner(int landID, int buyerID) {
//        
//        int landInputToModify = 0;
//        LandInfo landToModify = null;
//        LandInfo updatedLandInfo = null;
//        int newOwner = 0;
//        
//        // Display existing users for reference
//        List<LandInfo> landInfos = readLandInfo();
//        
//        landInputToModify = landID;
//    
//        // Find the user with the specified username or user ID
//	    for (int i = 0; i < landInfos.size(); i++) {
//	        LandInfo landInfo = landInfos.get(i);
//	        if (landInfo.getLandID() == landInputToModify) {
//	        	landToModify = landInfo;
//	            break;
//	        }
//	    }
//
//	       if (landToModify != null) {
//	    	   System.out.printf("\nLand ID to update: %d", landToModify.getLandID());
//
//	    	   int pastOwner = landToModify.getOwner();
//	    	   System.out.printf("\nPast Owner: %d", pastOwner);
//	    	   newOwner = buyerID;
//	    	   System.out.printf("\nNew Owner: %d", newOwner);
//	    	   
//	            // Create a new LandInfo object with updated details
//	            updatedLandInfo = new LandInfo(
//	            		landToModify.getLandID(),
//	            		landToModify.getLandArea(),
//	            		landToModify.getLandHeight(),
//	            		landToModify.getLandVolume(),
//	            		landToModify.getYearOfCon(),
//	            		newOwner,
//	            		landToModify.getRegDate(),
//	            		landToModify.getLandCond(),
//	            		landToModify.getValue()
//	            );
//	            
//	            // Replace the old user with the updated user in the list
//	            landInfos.set(landInfos.indexOf(landToModify), updatedLandInfo);
//	            // Write the updated user list to the file
//	            FileHandler.writeData(landInfos, LANDINFO_FILE);
//
//	            System.out.println("** Land Information modified successfully. **");
//	            displayCurrentLandInfo(updatedLandInfo.getLandID()); // Display updated
//	            
//	        } else {
//	            System.out.println("Land not found.");
//	        }
//	}
}
