package model;

import java.io.Serializable;

import enuum.landStatus;
import enuum.status;

@SuppressWarnings("serial")
public class LandRec implements Serializable{
    private int recID;
    private int landID;
    private int ownerID;
    private int transID;
    private landStatus landStatus;
    private status regStatus;
    
	public LandRec() {
		
	}

	public LandRec(int recID, int landID, int ownerID, int transID, landStatus landStatus, status regStatus) {
		super();
		this.recID = recID;
		this.landID = landID;
		this.ownerID = ownerID;
		this.transID = transID;
		this.landStatus = landStatus;
		this.regStatus = regStatus;
	}

	public int getRecID() {
		return recID;
	}

	public void setRecID(int recID) {
		this.recID = recID;
	}

	public int getLandID() {
		return landID;
	}

	public void setLandID(int landID) {
		this.landID = landID;
	}

	public int getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	public int getTransID() {
		return transID;
	}

	public void setTransID(int transID) {
		this.transID = transID;
	}

	public landStatus getLandStatus() {
		return landStatus;
	}

	public void setLandStatus(enuum.landStatus landStatus) {
		this.landStatus = landStatus;
	}
	
	public status getRegStatus() {
		return regStatus;
	}

	public void setRegStatus(enuum.status regStatus) {
		this.regStatus = regStatus;
	}

	@Override
	public String toString() {
		return "LandRec [recID=" + recID + 
				", landID=" + landID + 
				", ownerID=" + ownerID + 
				", transID=" + transID + 
				", landStatus=" + landStatus +
				", regStatus=" + regStatus + "]";
	}    
    
//	// mode 1 to sell display, mode 2 for buy display
//	private static void printLandInfoWLandRec(int mode, int ownerID) {
//	    LandInfoHandler lih = new LandInfoHandler();
//	    LandRecHandler lrh = new LandRecHandler();
//	    TransHandler th = new TransHandler(); // Assuming you have a TransRecHandler
//
//	    List<LandInfo> landInfos = lih.readLandInfo();
//	    List<LandRec> landRecs = lrh.readLandRec();
//
//	    // Create a map to store the latest LandRec for each landID
//	    Map<Integer, LandRec> latestLandRecs = new HashMap<>();
//	    Map<Integer, Integer> latestRecIDs = new HashMap<>(); // Map to store the latest RecID for each LandID
//
//	    // Iterate through LandRecs to find the latest for each landID with transID's tranStatus is COMPLETE
//	    for (LandRec landRec : landRecs) {
//	        if (landRec.getRegStatus() == enuum.status.COMPLETE) {
//	            int recID = landRec.getRecID();
//
//	            // Get the corresponding transRec for the current LandRec
//	            TransRec transRec = th.getTransRecByTransID(landRec.getTransID());
//
//	            // Check if transRec's tranStatus is also COMPLETE
//	            if (transRec != null && transRec.getTranStatus() == enuum.status.COMPLETE) {
//	                // Keep only the latest LandRec for each recID
//	                latestLandRecs.put(recID, landRec);
//
//	                // Keep track of the latest RecID for each LandID
//	                int landID = landRec.getLandID();
//	                if (!latestRecIDs.containsKey(landID) || recID > latestRecIDs.get(landID)) {
//	                    latestRecIDs.put(landID, recID);
//	                }
//	            }
//	        }
//	    }
//
//	    if (mode == 1) {
//		    System.out.printf("\n\n%" + 50 + "s%s%n", "", "Owned Land");
//		    System.out.println("-".repeat(130));
//		    System.out.printf("%-4s | %-9s | %-9s | %-9s | %-5s | %-6s | %-25s | %-10s | %-15s | %-15s%n",
//		            "ID", "Area", "Height", "Volume", "Year", "Owner", "Registered Date", "Condition", "Value", "Status");
//		    System.out.println("-".repeat(130));
//
//		    for (LandInfo landInfo : landInfos) {
//		        int landID = landInfo.getLandID();
//		        LandRec latestLandRec = latestLandRecs.get(latestRecIDs.getOrDefault(landID, 0));
//
//		        if (latestLandRec != null && latestLandRec.getOwnerID() == ownerID && latestLandRec.getLandStatus() == enuum.landStatus.OWNED) {
//		            // Get the latest owner information from LandRec
//		            int latestOwnerID = latestLandRec.getOwnerID();
//
//		            // Print land information along with the latest regStatus and owner
//		            System.out.printf("%-4s | %-9s | %-9s | %-9s | %-5s | %-6s | %-25s | %-10s | %-15s | %-15s%n",
//		                    landInfo.getLandID(), landInfo.getLandArea(), landInfo.getLandHeight(), landInfo.getLandVolume(),
//		                    landInfo.getYearOfCon(), latestOwnerID, landInfo.getRegDate(), landInfo.getLandCond(),
//		                    landInfo.getValue(), latestLandRec.getRegStatus());
//		        }
//		    }
//		    System.out.println("-".repeat(130));
//		    
//		    
//		    System.out.printf("\n%" + 45 + "s%s%n", "", "My Onsale Land");
//		    System.out.println("-".repeat(130));
//		    System.out.printf("%-4s | %-9s | %-9s | %-9s | %-5s | %-6s | %-25s | %-10s | %-15s | %-15s%n",
//		            "ID", "Area", "Height", "Volume", "Year", "Owner", "Registered Date", "Condition", "Value", "Status");
//		    System.out.println("-".repeat(130));
//
//		    for (LandInfo landInfo : landInfos) {
//		        int landID = landInfo.getLandID();
//		        LandRec latestLandRec = latestLandRecs.get(latestRecIDs.getOrDefault(landID, 0));
//
//		     // current user but on sale
//		        if (latestLandRec != null && latestLandRec.getOwnerID() == ownerID && latestLandRec.getLandStatus() == enuum.landStatus.ONSALE) {
//		            // Get the latest owner information from LandRec
//		            int latestOwnerID = latestLandRec.getOwnerID();
//
//		            System.out.printf("%-4s | %-9s | %-9s | %-9s | %-5s | %-6s | %-25s | %-10s | %-15s | %-15s%n",
//		                    landInfo.getLandID(), landInfo.getLandArea(), landInfo.getLandHeight(), landInfo.getLandVolume(),
//		                    landInfo.getYearOfCon(), latestOwnerID, landInfo.getRegDate(), landInfo.getLandCond(),
//		                    landInfo.getValue(), latestLandRec.getRegStatus());
//		        }
//		    }
//		    System.out.println("-".repeat(130));
//	    } else if (mode == 2) {   
//		    
//		    System.out.printf("\n%" + 45 + "s%s%n", "", "Onsale Land");
//		    System.out.println("-".repeat(130));
//		    System.out.printf("%-4s | %-9s | %-9s | %-9s | %-5s | %-6s | %-25s | %-10s | %-15s | %-15s%n",
//		            "ID", "Area", "Height", "Volume", "Year", "Owner", "Registered Date", "Condition", "Value", "Status");
//		    System.out.println("-".repeat(130));
//
//		    for (LandInfo landInfo : landInfos) {
//		        int landID = landInfo.getLandID();
//		        LandRec latestLandRec = latestLandRecs.get(latestRecIDs.getOrDefault(landID, 0));
//
//		        // not current user but on sale
//		        if (latestLandRec != null && latestLandRec.getOwnerID() != ownerID && latestLandRec.getLandStatus() == enuum.landStatus.ONSALE) {
//		            // Get the latest owner information from LandRec
//		            int latestOwnerID = latestLandRec.getOwnerID();
//
//		            System.out.printf("%-4s | %-9s | %-9s | %-9s | %-5s | %-6s | %-25s | %-10s | %-15s | %-15s%n",
//		                    landInfo.getLandID(), landInfo.getLandArea(), landInfo.getLandHeight(), landInfo.getLandVolume(),
//		                    landInfo.getYearOfCon(), latestOwnerID, landInfo.getRegDate(), landInfo.getLandCond(),
//		                    landInfo.getValue(), latestLandRec.getRegStatus());
//		        }
//		    }
//		    System.out.println("-".repeat(130));
//	    }
//
//	}

}

