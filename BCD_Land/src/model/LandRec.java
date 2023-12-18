package model;

import java.io.Serializable;

import enuum.landStatus;
import enuum.status;

@SuppressWarnings("serial")
public class LandRec implements Serializable{
    private int recID;
    private int landID;
    private int ownerID;
    private String prevUse;
    private int transID;
    private landStatus landStatus;
    private status regStatus;
    
	public LandRec() {
		
	}

	public LandRec(int recID, int landID, int ownerID, String prevUse, int transID, enuum.landStatus landStatus) {
		super();
		this.recID = recID;
		this.landID = landID;
		this.ownerID = ownerID;
		this.prevUse = prevUse;
		this.transID = transID;
		this.landStatus = landStatus;
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

	public int ownerID() {
		return ownerID;
	}

	public void setownerID(int ownerID) {
		this.ownerID = ownerID;
	}

	public String getPrevUse() {
		return prevUse;
	}

	public void setPrevUse(String prevUse) {
		this.prevUse = prevUse;
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
				", prevUse=" + prevUse + 
				", transID=" + transID + 
				", landStatus=" + landStatus +
				", regStatus=" + regStatus + "]";
	}    
    
}

