package model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;

import enuum.*;

@SuppressWarnings("serial")
public class TransRec implements Serializable{
	private int transID;
	private int landID;
	private int buyerID;
	private int sellerID;
	private Timestamp recDate;
	private double amount;
	private paymentMethod paymentMethod;
	private transType transType;
	private status tranStatus;
	private byte[] adminSignature;
	
	public TransRec() {
		
	}


	public TransRec(int transID, int landID, int buyerID, int sellerID, Timestamp recDate, double amount,
			enuum.paymentMethod paymentMethod, enuum.transType transType, status tranStatus, byte[] adminSignature) {
		super();
		this.transID = transID;
		this.landID = landID;
		this.buyerID = buyerID;
		this.sellerID = sellerID;
		this.recDate = recDate;
		this.amount = amount;
		this.paymentMethod = paymentMethod;
		this.transType = transType;
		this.tranStatus = tranStatus;
		this.adminSignature = adminSignature;
	}

	public int getTransID() {
		return transID;
	}


	public void setTransID(int transID) {
		this.transID = transID;
	}


	public int getLandID() {
		return landID;
	}


	public void setLandID(int landID) {
		this.landID = landID;
	}


	public int getBuyerID() {
		return buyerID;
	}


	public void setBuyerID(int buyerID) {
		this.buyerID = buyerID;
	}


	public int getSellerID() {
		return sellerID;
	}


	public void setSellerID(int sellerID) {
		this.sellerID = sellerID;
	}


	public Timestamp getRecDate() {
		return recDate;
	}


	public void setRecDate(Timestamp recDate) {
		this.recDate = recDate;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public paymentMethod getPaymentMethod() {
		return paymentMethod;
	}


	public void setPaymentMethod(paymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}


	public transType getTransType() {
		return transType;
	}


	public void setTransType(transType transType) {
		this.transType = transType;
	}


	public status getTranStatus() {
		return tranStatus;
	}


	public void setTranStatus(status tranStatus) {
		this.tranStatus = tranStatus;
	}


	public byte[] getAdminSignature() {
		return adminSignature;
	}


	public void setAdminSignature(byte[] adminSignature) {
		this.adminSignature = adminSignature;
	}

	@Override
	public String toString() {
		return "TransRec [transID=" + transID + 
				", landID=" + landID + 
				", buyerID=" + buyerID + 
				", sellerID=" + sellerID + 
				", recDate=" + recDate + 
				", amount=" + amount + 
				", paymentMethod=" + paymentMethod + 
				", transType=" + transType + 
				", tranStatus=" + tranStatus + 
				", adminSignature=" + Arrays.toString(adminSignature) + "]";
	}
	
	
	
}
