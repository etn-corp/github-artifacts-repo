
package com.eaton.electrical.smrc.bo;

import java.util.*;

public class GeogRptRecord extends SearchResults implements java.io.Serializable {
	private String _se = "";
	private String _focusType = "";
	private String _stage = "";
	private ArrayList _channels = new ArrayList(5);
	private int _newStores = 0;
	private Customer customer;
	private String salesManName;
	
	private static final long serialVersionUID = 100;

	public void setSalesEngineer(String se) {
		_se = se;
	}
	
	public void setFocusType(String type) {
		_focusType = type;
	}
	
	public void setStage(String stage) {
		_stage = stage;
	}
	
	public void setNumStores(int store) {
		_newStores = store;
	}
	
	public void addChannel(String channel) {
		_channels.add(channel);
	}
	
	public String getSalesEngineer() {
		return _se;
	}
	
	public String getFocusType() {
		return _focusType;
	}
	
	public String getStage() {
		return _stage;
	}
	
	public int getNumStores() {
		return _newStores;
	}
	
	public ArrayList getChannels() {
		return _channels;
	}
	
	public void setThisCust(Customer customer) {
		this.customer=customer;
		
	}
	public Customer getThisCust() {
		return this.customer;
		
	}
	
	public void setSalesmanName(String salesManName) {
		this.salesManName=salesManName;
		
	}
	public String getSalesmanName() {
		return salesManName;
		
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

		returnString += "\tsalesEngineer = " + this.getSalesEngineer() + "\n";
		returnString += "\tfocusType = " + this.getFocusType() + "\n";
		returnString += "\tstage = " + this.getStage() + "\n";
		returnString += "\tnumStores = " + this.getNumStores() + "\n";
		returnString += "\tchannels = " + this.getChannels() + "\n";
		returnString += "\tthisCust = " + this.getThisCust() + "\n";
		returnString += "\tsalesmanName = " + this.getSalesmanName() + "\n";

		return returnString;
	}
}
