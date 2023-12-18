package handler;

import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import enuum.status;
import model.LandInfo;
import model.User;

public class LandInfoHandler {
	private static final String LANDINFO_FILE = "landInfo.txt";
	
	UserHandler uh = new UserHandler();

    public List<LandInfo> readLandInfo() {
        return FileHandler.readData(LANDINFO_FILE);
    }
	
    public void printLandInfo(int ownerID) {
    	List<LandInfo> landInfos = readLandInfo();
    	
    	if (ownerID != 000) {    		
            List<LandInfo> filteredLandInfos = landInfos.stream()
                    .filter(landInfo -> landInfo.getOwner() == ownerID)
                    .collect(Collectors.toList());
            landInfos = filteredLandInfos;
    	}
        
        System.out.printf("\n\n%" + 50 + "s%s%n", "", "Registered Land");
        System.out.println("-".repeat(130));
        System.out.printf("%-4s | %-7s | %-7s | %-7s | %-5s | %-6s | %-25s | %-10s | %-15s | %-15s%n",
        		"ID", "Area", "Height", "Volume", "Year", "Owner", "Registered Date", "Condition", "Value", "Status");
        System.out.println("-".repeat(130));
        for (LandInfo landInfo : landInfos) {
            System.out.printf("%-4s | %-7s | %-7s | %-7s | %-5s | %-6s | %-25s | %-10s | %-15s | %-15s%n",
            		landInfo.getLandID(), landInfo.getLandArea(), landInfo.getLandHeight(), landInfo.getLandVolume(), landInfo.getYearOfCon(),
            		landInfo.getOwner(), landInfo.getRegDate(), landInfo.getLandCond(), landInfo.getValue(), landInfo.getRegStatus());
        }
        System.out.println("-".repeat(130));
    }
    
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
	public void addLandInfo(int mode) {
		
        Scanner scanner = new Scanner(System.in);
        LoginHandler login = LoginHandler.getInstance();
        
        List<LandInfo> landInfo = readLandInfo();
        printLandInfo(000);
        
    	if (mode == 1) {
    		
    		User currentUser = login.getCurrentUser();
    		printLandInfo(currentUser.getUserID());
    		System.out.println(currentUser);	//pass currentUser that register land if mode = 1
    		login.displayCurrentUser();
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
        
        System.out.print("Owner ID\t\t: ");			//if customer mode then set currentUser as owner
        int owner = scanner.nextInt();
        scanner.nextLine(); 
        
        System.out.print("Land Condition (max 5)\t: ");
        int landCond = scanner.nextInt();
        scanner.nextLine(); 
        
        System.out.print("Value\t\t\t: ");
        double value = scanner.nextDouble();
        scanner.nextLine(); 
        
        status regStatus = enuum.status.PENDING;  // need approval

        // Create a new user object
        LandInfo newLandInfo = new LandInfo(landID, landArea, landHeight, landVolume, yearOfCon, owner, regDate, landCond, value, regStatus);

        // Add the new user to the list
        FileHandler.addObject(newLandInfo, LANDINFO_FILE);
        System.out.println("Successfully registered new land. Please patiently wait for approval.");
        
	}

	// search for ID
	// mode:	admin - 000, customer/seller - 1
	public void modifyLandInfo(int mode) {
		
        Scanner scanner = new Scanner(System.in);
        LoginHandler login = LoginHandler.getInstance();
        
        int landInputToModify = 0;
        LandInfo landToModify = null;
        LandInfo updatedLandInfo = null;
        int newOwner = 0;
        
        // Display existing users for reference
        List<LandInfo> landInfos = readLandInfo();

		if (mode == 1) {
			printLandInfo(login.getCurrentUserID());
            List<LandInfo> filteredLandInfos = landInfos.stream()
                    .filter(landInfo -> landInfo.getOwner() == login.getCurrentUserID())
                    .collect(Collectors.toList());
            landInfos = filteredLandInfos;
		}else {
			printLandInfo(000);
		}
		
        System.out.print("Enter Land ID to modify: ");
        landInputToModify = scanner.nextInt();
        scanner.nextLine();
    
        // Find the user with the specified username or user ID
	    for (int i = 0; i < landInfos.size(); i++) {
	        LandInfo landInfo = landInfos.get(i);
	        if (landInfo.getLandID() == landInputToModify) {
	        	landToModify = landInfo;
	            break;
	        }
	    }

	       if (landToModify != null) {
	            // Collect updated user details from user input
	           System.out.print("\nNew Land Area\t\t\t: ");
	           double newLandArea = scanner.nextDouble();
	           scanner.nextLine(); 
	           
	           System.out.print("New Land Height\t\t\t: ");
	           double newLandHeight = scanner.nextDouble();
	           scanner.nextLine(); 
	           
	           System.out.print("New Land Volume\t\t\t: ");
	           double newLandVolume = scanner.nextDouble();
	           scanner.nextLine(); 
	           
	           System.out.print("New Year of Construction\t: ");
	           int newYearOfCon = scanner.nextInt();
	           scanner.nextLine(); 
	           
	           if (mode == 000) {
		           System.out.print("New Owner ID\t\t: ");			//if customer mode then set currentUser as owner
		           newOwner = scanner.nextInt();
		           scanner.nextLine(); 
	           } else {
	        	   newOwner = login.getCurrentUserID();
	           }
	           
	           System.out.print("New Land Condition (max 5)\t: ");
	           int newLandCond = scanner.nextInt();
	           scanner.nextLine(); 
	           
	           System.out.print("New Value\t\t\t: ");
	           double newValue = scanner.nextDouble();
	           scanner.nextLine(); 

	            // Create a new LandInfo object with updated details
	            updatedLandInfo = new LandInfo(
	            		landToModify.getLandID(),
	            		newLandArea,
	            		newLandHeight,
	            		newLandVolume,
	            		newYearOfCon,
	            		newOwner,
	            		landToModify.getRegDate(),
	            		newLandCond,
	            		newValue,
	            		landToModify.getRegStatus()
	            );
	            
	            // Replace the old user with the updated user in the list
	            landInfos.set(landInfos.indexOf(landToModify), updatedLandInfo);
	            // Write the updated user list to the file
	            FileHandler.writeData(landInfos, LANDINFO_FILE);

	            System.out.println("Land Information modified successfully.");
	            System.out.println(updatedLandInfo);
	            
	            printLandInfo(mode); // Display updated user list
	        } else {
	            System.out.println("Land not found.");
	        }
	}
	
	public void newLandOwner(int buyerID) {
		
	}
	
	//display pending
	public void approveLandInfo() {
		
	}
	
	//display completed/registered
	//set status to removed
	public void deleteLandInfo() {
		
		
	}
}
