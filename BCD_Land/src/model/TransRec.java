package model;

import java.io.Serializable;
import java.sql.Date;

import misc.*;

@SuppressWarnings("serial")
public class TransRec implements Serializable{
	private int transID;
	private int landID;
	private int buyerID;
	private int sellerID;
	private Date recDate;
	private double amount;
	private paymentMethod paymentMethod;
	private transType transType;
	private tranStatus tranStatus;
	
	public TransRec() {
		
	}

	public TransRec(int transID, int landID, int buyerID, int sellerID, Date recDate, double amount,
			misc.paymentMethod paymentMethod, misc.transType transType, misc.tranStatus tranStatus) {
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

	public Date getRecDate() {
		return recDate;
	}

	public void setRecDate(Date recDate) {
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

	public tranStatus getTranStatus() {
		return tranStatus;
	}

	public void setTranStatus(tranStatus tranStatus) {
		this.tranStatus = tranStatus;
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
				", tranStatus=" + tranStatus + "]";
	}
	
	
	
}
