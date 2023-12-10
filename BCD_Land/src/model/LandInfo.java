package model;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class LandInfo implements Serializable {
	private int landID;
	private double landArea;
	private double landHeight;
	private double landVolume;
	private int yearOfCon;
	private int owner;
	private Date regDate;
	private int landCond; 	//0 to 5
	private double value;
	
	public LandInfo() {
		
	}

	public LandInfo(int landID, double landArea, double landHeight, double landVolume, int yearOfCon, int owner, Date regDate, int landCond, double value) {
		super();
		this.landID = landID;
		this.landArea = landArea;
		this.landHeight = landHeight;
		this.landVolume = landVolume;
		this.yearOfCon = yearOfCon;
		this.owner = owner;
		this.regDate = regDate;
		this.landCond = landCond;
		this.value = value;
	}

	public int getLandID() {
		return landID;
	}

	public void setLandID(int landID) {
		this.landID = landID;
	}

	public double getLandArea() {
		return landArea;
	}

	public void setLandArea(double landArea) {
		this.landArea = landArea;
	}

	public double getLandHeight() {
		return landHeight;
	}

	public void setLandHeight(double landHeight) {
		this.landHeight = landHeight;
	}

	public double getLandVolume() {
		return landVolume;
	}

	public void setLandVolume(double landVolume) {
		this.landVolume = landVolume;
	}

	public int getYearOfCon() {
		return yearOfCon;
	}

	public void setYearOfCon(int yearOfCon) {
		this.yearOfCon = yearOfCon;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public int getLandCond() {
		return landCond;
	}

	public void setLandCond(int landCond) {
		this.landCond = landCond;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "LandInfo [landID=" + landID + 
				", landArea=" + landArea + 
				", landHeight=" + landHeight + 
				", landVolume="	+ landVolume + 
				", yearOfCon=" + yearOfCon + 
				", owner=" + owner + 
				", regDate=" + regDate + 
				", landCond=" + landCond + 
				", value=" + value + "]";
	}

}
