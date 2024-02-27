package com.eaton.electrical.smrc.bo;

import java.sql.*;

/**
 * @author E0062708
 *
 */
public class DistributorImpactAnalysis implements java.io.Serializable {
	
	private String vcn = null;
	private Date dateAdded = null;
	private int distributorId = 0;
	private double maintainDollars = 0;
	private double growDollars = 0;
	private double penetrateDollars = 0;
	private double addDollars = 0;
	private double terminateDollars = 0;
	private double riskDollars = 0;
	private double otherChainImpact = 0;
	private Date dateChanged = null;
	private String userAdded = null;
	private String userChanged = null;
	
	private static final long serialVersionUID = 100;

	public DistributorImpactAnalysis(){
		vcn = "";
		userAdded = "";
		userChanged = "";
	}
	
	public String getVcn() {
		return vcn;
	}
	public void setVcn(String vcn) {
		this.vcn = vcn;
	}
	public double getAddDollars() {
		return addDollars;
	}
	public void setAddDollars(double addDollars) {
		this.addDollars = addDollars;
	}
	public Date getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	public Date getDateChanged() {
		return dateChanged;
	}
	public void setDateChanged(Date dateChanged) {
		this.dateChanged = dateChanged;
	}
	public double getGrowDollars() {
		return growDollars;
	}
	public void setGrowDollars(double growDollars) {
		this.growDollars = growDollars;
	}
	public double getOtherChainImpact() {
		return otherChainImpact;
	}
	public void setOtherChainImpact(double otherChainImpact) {
		this.otherChainImpact = otherChainImpact;
	}
	public double getPenetrateDollars() {
		return penetrateDollars;
	}
	public void setPenetrateDollars(double penetrateDollars) {
		this.penetrateDollars = penetrateDollars;
	}
	public double getRiskDollars() {
		return riskDollars;
	}
	public void setRiskDollars(double riskDollars) {
		this.riskDollars = riskDollars;
	}
	public double getTerminateDollars() {
		return terminateDollars;
	}
	public void setTerminateDollars(double terminateDollars) {
		this.terminateDollars = terminateDollars;
	}
	public String getUserAdded() {
		return userAdded;
	}
	public void setUserAdded(String userAdded) {
		this.userAdded = userAdded;
	}
	public String getUserChanged() {
		return userChanged;
	}
	public void setUserChanged(String userChanged) {
		this.userChanged = userChanged;
	}
	public double getMaintainDollars() {
		return maintainDollars;
	}
	public void setMaintainDollars(double maintainDollars) {
		this.maintainDollars = maintainDollars;
	}
	public int getDistributorId() {
		return distributorId;
	}
	public void setDistributorId(int distributorId) {
		this.distributorId = distributorId;
	}
	public double getAreaImpact() {
		return maintainDollars + growDollars + addDollars + penetrateDollars - terminateDollars - riskDollars;
	}
	public double getGeographyImpact() {
		return getAreaImpact() + otherChainImpact;
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

		returnString += "\tvcn = " + this.getVcn() + "\n";
		returnString += "\taddDollars = " + this.getAddDollars() + "\n";
		returnString += "\tdateAdded = " + this.getDateAdded() + "\n";
		returnString += "\tdateChanged = " + this.getDateChanged() + "\n";
		returnString += "\tgrowDollars = " + this.getGrowDollars() + "\n";
		returnString += "\totherChainImpact = " + this.getOtherChainImpact() + "\n";
		returnString += "\tpenetrateDollars = " + this.getPenetrateDollars() + "\n";
		returnString += "\triskDollars = " + this.getRiskDollars() + "\n";
		returnString += "\tterminateDollars = " + this.getTerminateDollars() + "\n";
		returnString += "\tuserAdded = " + this.getUserAdded() + "\n";
		returnString += "\tuserChanged = " + this.getUserChanged() + "\n";
		returnString += "\tmaintainDollars = " + this.getMaintainDollars() + "\n";
		returnString += "\tdistributorId = " + this.getDistributorId() + "\n";
		returnString += "\tareaImpact = " + this.getAreaImpact() + "\n";
		returnString += "\tgeographyImpact = " + this.getGeographyImpact() + "\n";

		return returnString;
	}
}
