package com.eaton.electrical.smrc.bo;



public class SICCode implements java.io.Serializable {
	private String _sic = "";
	private String _desc = "";

	private static final long serialVersionUID = 100;

	public void setSICCode(String sic) {
		_sic = sic;
	}

	public void setDescription(String desc) {
		_desc = desc;
	}

	public String getSICCode() {
		return _sic;
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

		returnString += "\tsICCode = " + this.getSICCode() + "\n";
		returnString += "\tdescription = " + this.getDescription() + "\n";

		return returnString;
	}
}
