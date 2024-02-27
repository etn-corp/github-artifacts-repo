package com.eaton.electrical.smrc.bo;



/**	This class contains all of the methods and attributes needed by the Target Account Planner
*	as needed for each region (or zone)
*
*	@author Carl Abel
*/
public class Region implements java.io.Serializable {
	String _region = "";
	String _desc = "";
	String _geog = "";
	String _segment = "";

	private static final long serialVersionUID = 100;

	/** Lets the calling routine set the region or zone
	*	@param String The region or zone
	*/
	public void setRegion(String region) {
		_region = region;
	}

	/** Retrieves the region or zone
	*	@return String the region or zone
	*/
	public String getRegion () {
		return _region;
	}

	/** Lets the calling routine set the description
	*	@param String The description
	*/
	public void setDescription(String desc) {
		_desc = desc;
	}

	/** Retrieves the description
	*	@return String The description
	*/
	public String getDescription () {
		return _desc;
	}

	/** Lets the calling routine define the SP_Geog for this region or zone
	*	@param String the SP_Geog for the region or zone
	*/
	public void setSPGeog(String geog) {
		_geog = geog;
	}

	/** Retrieves the sp_geog for this region or zone
	*	@param String the sp_geog for this region or zone
	*/
	public String getSPGeog() {
		return _geog;
	}

	/** Lets the calling routine set the segment or district
	*	@param String The segment or district
	*/
	public void setSegment(String segment) {
		_segment = segment;
	}

	/** Retrieves the segment or district
	*	@return String The segment or district
	*/
	public String getSegment () {
		return _segment;
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

		returnString += "\tregion  = " + this.getRegion () + "\n";
		returnString += "\tdescription  = " + this.getDescription () + "\n";
		returnString += "\tsPGeog = " + this.getSPGeog() + "\n";
		returnString += "\tsegment  = " + this.getSegment () + "\n";

		return returnString;
	}
}
