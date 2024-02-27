package com.eaton.electrical.smrc.bo;

import java.util.*;

/**
 * @author E0062708
 *
 */
public class Segment implements java.io.Serializable {

	private int segmentId = -1;
	private String name = null;
	private String description = null;
	private int salesPercentage = 0;
	private int level = 0;
	private String sicCode = null;
	ArrayList subSegments = null;
	ArrayList accounts = null;
	private boolean targetMarketSegment = false;
	private boolean distributor = false;
	private boolean distributorFormDisplay = false;
	private boolean hidden = false;
	private int parentId;
	
	private static final long serialVersionUID = 100;

	public Segment() {
		name = "";
		description = "";
		subSegments = new ArrayList();
		accounts = new ArrayList();
		sicCode = "";
	}


	/**
	 * @return the description of the segment
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description  the segment description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the name of the segment
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name  the segment name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the segment level
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * @param level  the segment level
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	/**
	 * @return the segment id
	 */
	public int getSegmentId() {
		return segmentId;
	}
	/**
	 * @param segmentId the segment id
	 */
	public void setSegmentId(int segmentId) {
		this.segmentId = segmentId;
	}


	/**
	 * @param subSegments
	 */
	public void setSubSegments(ArrayList subSegments) {
		this.subSegments=subSegments;
		
	}
	public ArrayList getSubSegments() {
		return this.subSegments;
		
	}
	/**
	 * @return Returns the accounts.
	 */
	public ArrayList getAccounts() {
		return accounts;
	}
	/**
	 * @param accounts The accounts to set.
	 */
	public void setAccounts(ArrayList accounts) {
		this.accounts = accounts;
	}
	public int getSalesPercentage() {
		return salesPercentage;
	}
	public void setSalesPercentage(int salesPercentage) {
		this.salesPercentage = salesPercentage;
	}
	public boolean isPrimary() {
		if(level==1){
			return true;
		}
		return false;
		
	}
	public boolean isSecondary() {
		if(level==2){
			return true;
		}
		return false;

	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public boolean equals(Object obj) {
		if (this == null || obj == null) {
			return false;
		}

		return super.equals(obj);
	}

	public String toString() {
		String returnString = "";

		returnString += "\tdescription = " + this.getDescription() + "\n";
		returnString += "\tname = " + this.getName() + "\n";
		returnString += "\tlevel = " + this.getLevel() + "\n";
		returnString += "\tsegmentId = " + this.getSegmentId() + "\n";
		returnString += "\tsubSegments = " + this.getSubSegments() + "\n";
		returnString += "\taccounts = " + this.getAccounts() + "\n";
		returnString += "\tsalesPercentage = " + this.getSalesPercentage() + "\n";
		returnString += "\tsicCode = " + this.getSicCode() + "\n";
		returnString += "\tdistributor? = " + this.isDistributor() + "\n";
		returnString += "\ttargetMarketSegment? = " + this.isTargetMarketSegment() + "\n";
		returnString += "\tdistributorFormDisplay? = " + this.isDistributorFormDisplay() + "\n";
		returnString += "\thidden? = " + this.isHidden() + "\n";
		
		return returnString;
	}
	public String getSicCode() {
		return sicCode;
	}
	public void setSicCode(String sicCode) {
		this.sicCode = sicCode;
	}
	public boolean isDistributor() {
		return distributor;
	}
	public void setDistributor(boolean distributor) {
		this.distributor = distributor;
	}
	public boolean isDistributorFormDisplay() {
		return distributorFormDisplay;
	}
	public void setDistributorFormDisplay(boolean distributorFormDisplay) {
		this.distributorFormDisplay = distributorFormDisplay;
	}
	public boolean isTargetMarketSegment() {
		return targetMarketSegment;
	}
	public void setTargetMarketSegment(boolean targetMarketSegment) {
		this.targetMarketSegment = targetMarketSegment;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
    public int getParentId() {
        return parentId;
    }
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
