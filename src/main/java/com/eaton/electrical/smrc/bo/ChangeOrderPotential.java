package com.eaton.electrical.smrc.bo;

public class ChangeOrderPotential implements java.io.Serializable {
	int _id = 0;
	String _desc = "";

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
