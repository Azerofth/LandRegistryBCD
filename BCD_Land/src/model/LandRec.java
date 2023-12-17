package model;

import java.io.Serializable;

import enuum.landStatus;

@SuppressWarnings("serial")
public class LandRec implements Serializable{
    private int recID;
    private int landID;
    private int pastOwner;
    private int buyer;
    private String prevUse;
    private int transID;
    private landStatus landStatus;
    
	public LandRec() {
		
	}

	public LandRec(int recID, int landID, int pastOwner, int buyer, String prevUse, int transID, enuum.landStatus landStatus) {
		super();
		this.recID = recID;
		this.landID = landID;
		this.pastOwner = pastOwner;
		this.buyer = buyer;
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

	public int getPastOwner() {
		return pastOwner;
	}

	public void setPastOwner(int pastOwner) {
		this.pastOwner = pastOwner;
	}

	public int getBuyer() {
		return buyer;
	}

	public void setBuyer(int buyer) {
		this.buyer = buyer;
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

	public landStatus getStatus() {
		return landStatus;
	}

	public void setStatus(enuum.landStatus landStatus) {
		this.landStatus = landStatus;
	}

	@Override
	public String toString() {
		return "LandRec [recID=" + recID + 
				", landID=" + landID + 
				", pastOwner=" + pastOwner + 
				", buyer=" + buyer + 
				", prevUse=" + prevUse + 
				", transID=" + transID + 
				", status=" + landStatus + "]";
	}    
    
}

