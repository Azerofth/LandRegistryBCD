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
        System.out.printf("%-4s | %-9s | %-9s | %-9s | %-5s | %-6s | %-25s | %-10s | %-18s | %-15s%n",
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
                    System.out.printf("%-4s | %-9s | %-9s | %-9s | %-5s | %-6s | %-25s | %-10s | %-18s | %-15s%n",
                            landInfo.getLandID(), landInfo.getLandArea(), landInfo.getLandHeight(), landInfo.getLandVolume(),
                            landInfo.getYearOfCon(), latestOwnerID, landInfo.getRegDate(), landInfo.getLandCond(),
                            String.format("RM%.2f", landInfo.getValue()), latestLandRec.getRegStatus());
                }
            }
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
            printLandInfo(000); //admin use
        }

        int landID = generateNewLandInfoID(landInfo);
        Timestamp regDate = new Timestamp(System.currentTimeMillis());

        System.out.printf("\nLand ID #%d", landID);

        double landArea;
        do {
            System.out.print("\nLand Area\t\t: ");
            landArea = getValidDoubleInput(scanner);
            if (landArea < 100) {
                System.out.println("** Land Area must be at least 100. **");
            }
        } while (landArea < 100);

        double landHeight;
        do {
            System.out.print("Land Height\t\t: ");
            landHeight = getValidDoubleInput(scanner);
            if (landHeight < 100) {
                System.out.println("** Land Height must be at least 100. **");
            }
        } while (landHeight < 100);

        double landVolume;
        do {
            System.out.print("Land Volume\t\t: ");
            landVolume = getValidDoubleInput(scanner);
            if (landVolume < 500) {
                System.out.println("** Land Volume must be at least 500. **");
            }
        } while (landVolume < 100);

        int yearOfCon;
        do {
            System.out.print("Year of Construction\t: ");
            yearOfCon = getValidIntInput(scanner);
            if (yearOfCon < 1900 || yearOfCon > 2025) {
                System.out.println("** Year of Construction must be between 1900 and 2025. **");
            }
        } while (yearOfCon < 1900 || yearOfCon > 2025);

        int owner;
        if (mode == 1) {
            owner = currentUser.getUserID(); //if customer mode then set currentUser as owner
        } else {
            do {
                System.out.print("Owner ID\t\t: ");
                owner = getValidIntInput(scanner);
                if (owner == 1 || !isUserIdExisting(owner)) {
                    System.out.println("** Invalid Owner ID. Please enter a valid ID that exists in user.txt. **");
                }
            } while (owner == 1 || !isUserIdExisting(owner));
        }

        int landCond;
        do {
            System.out.print("Land Condition (1 - 5)\t: ");
            landCond = getValidIntInput(scanner);
            if (landCond < 1 || landCond > 5) {
                System.out.println("** Land Condition must be between 1 and 5. **");
            }
        } while (landCond < 1 || landCond > 5);

        double value;
        do {
            System.out.print("Value\t\t\t: RM ");
            value = getValidDoubleInput(scanner);
            if (value < 5000) {
                System.out.println("** Value must be at least RM5000. **");
            }
        } while (value < 5000);

        // Create a new user object
        LandInfo newLandInfo = new LandInfo(landID, landArea, landHeight, landVolume, yearOfCon, owner, regDate, landCond, value);

        boolean transaction = th.newTransaction(1, landID, owner, 0);

        if (transaction) {
            FileHandler.addObject(newLandInfo, LANDINFO_FILE);

            System.out.println("** Successfully registered new land. Please patiently wait for approval. **");
            String newLand = newLandInfo.toString();
            Blockchain.createBlockchain(newLand);
        } else {
            System.out.println("** Failed to register new land. **");
        }
    }

    private double getValidDoubleInput(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.println("** Invalid input. Please enter a number. **");
            scanner.next(); // Consume the invalid input
        }
        return scanner.nextDouble();
    }

    private int getValidIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("** Invalid input. Please enter an integer. **");
            scanner.next(); // Consume the invalid input
        }
        return scanner.nextInt();
    }

    private boolean isUserIdExisting(int userID) {
    	UserHandler uh = new UserHandler();
        List<User> users = uh.readUsers();
        return users.stream().anyMatch(user -> user.getUserID() == userID);
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
	
}
