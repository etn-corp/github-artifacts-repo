package com.eaton.electrical.smrc.bo;



/**	This class contains all of the methods and attributes needed by the Target Account Planner
*	as needed for a given sales plan. Each sales plan has a product id and customer number as the key.
*
*	@author Carl Abel
*/
public class SalesPlan implements java.io.Serializable {
	String _vcn = null;
	String _competProdPos = null;
	String objectiveResponse = null;

	private static final long serialVersionUID = 100;

	public SalesPlan(){
		_vcn = "";
		_competProdPos = "";
		objectiveResponse = "";
	}
	
	/** Lets the calling routine set the vista customer number for a sales plan
	*	@param String The Vista Cust Num for this sales plan's customer
	*/
	public void setVistaCustNum(String vcn) {
		_vcn = vcn;
	}

	/** The product id for this sales plan.
	*	@param String The product id for this sales plan
	*/
	public void setCompetitiveProdsAndPositions(String prodPos) {
		_competProdPos = prodPos;
	}

	/** Retrieves the Vista Cust Num for this sales plan
	*	@return String The Vista Cust Num for this sales plan
	*/
	public String getVistaCustNum() {
		return _vcn;
	}

	/** Retrieves the product id for this sales plan
	*	@return String The product id for this sales plan
	*/
	public String getCompetitiveProdsAndPositions() {
		return _competProdPos;
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

		returnString += "\tvistaCustNum = " + this.getVistaCustNum() + "\n";
		returnString += "\tcompetitiveProdsAndPositions = " + this.getCompetitiveProdsAndPositions() + "\n";
		returnString += "\tobjectiveResponse = " + this.getObjectiveResponse() + "\n";

		return returnString;
	}
	public String getObjectiveResponse() {
		return objectiveResponse;
	}
	public void setObjectiveResponse(String objectiveResponse) {
		this.objectiveResponse = objectiveResponse;
	}
}
