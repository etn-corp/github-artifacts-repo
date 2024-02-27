package com.eaton.electrical.smrc.bo;



/**	This class contains all of the methods and attributes needed by the Target Account Planner
*	as needed for each stage of a sales plan.
*
*	@author Carl Abel
*/
public class SPStage implements java.io.Serializable {
	int _id = 0;
	String _desc = "";

	private static final long serialVersionUID = 100;

	/** Lets the calling routine set the id from the DB for this stage of the sales plan
	*	@param int The id for this stage
	*/
	public void setId(int id) {
		_id = id;
	}

	/** Lets the calling routine set the description for this stage of the sales plan
	*	@param String the description of the sales plan
	*/
	public void setDescription(String desc) {
		_desc = desc;
	}

	/** Retrieves the stage id
	*	@return int The stage id
	*/
	public int getId() {
		return _id;
	}

	/** Retrieves the description of the stage
	*	@return String The description of the stage
	*/
	public String getDescription() {
		return _desc;
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

		returnString += "\tid = " + this.getId() + "\n";
		returnString += "\tdescription = " + this.getDescription() + "\n";

		return returnString;
	}
}
