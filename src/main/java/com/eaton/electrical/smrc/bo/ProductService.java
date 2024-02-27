package com.eaton.electrical.smrc.bo;



/**	This class contains all of the methods and attributes needed by the Target Account Planner
*	as needed
*
*	@author Carl Abel
*/
public class ProductService implements java.io.Serializable {
	int _id;
	String _desc;
	String _group;

	private static final long serialVersionUID = 100;

	public void setId(int id) {
		_id = id;
	}

	public void setDescription(String desc) {
		_desc = desc;
	}

	public int getId() {
		return _id;
	}

	public String getDescription() {
		return _desc;
	}

	public void setGroup (String group) {
		_group = group;
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
