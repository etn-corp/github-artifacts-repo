package com.eaton.electrical.smrc.bo;

import java.util.*;

/**
 * @author E0062708
 *
 */
public class Division implements java.io.Serializable {
	String id = null;
	String code = null;
	String name = null;
	ArrayList cSFs = null;
	
	private static final long serialVersionUID = 100;

	public Division() {
		id = "";
		code = "";
		name = "";
		cSFs = new ArrayList();
		
	}
	
	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the cSFs.
	 */
	public ArrayList getCSFs() {
		return cSFs;
	}
	/**
	 * @param fs The cSFs to set.
	 */
	public void setCSFs(ArrayList fs) {
		cSFs = fs;
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

		returnString += "\tcode = " + this.getCode() + "\n";
		returnString += "\tid = " + this.getId() + "\n";
		returnString += "\tname = " + this.getName() + "\n";
		returnString += "\tcSFs = " + this.getCSFs() + "\n";

		return returnString;
	}
}
