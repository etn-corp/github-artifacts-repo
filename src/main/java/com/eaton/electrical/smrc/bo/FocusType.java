package com.eaton.electrical.smrc.bo;

/**	This class contains all of the methods and attributes needed by the Target Account Planner
*	as needed
*
*	@author Carl Abel
*/
public class FocusType extends Object implements java.io.Serializable {
	int id = 0;
	String desc = "";
	String group;

	private static final long serialVersionUID = 100;

	public void setId(int id) {
		this.id = id;
	}

	public void setDescription(String desc) {
		this.desc = desc;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return desc;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getGroup() {
		return group;
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
		returnString += "\tgroup = " + this.getGroup() + "\n";

		return returnString;
	}
}
