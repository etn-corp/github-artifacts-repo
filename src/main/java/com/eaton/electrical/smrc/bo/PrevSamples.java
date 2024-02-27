
package com.eaton.electrical.smrc.bo;

import java.util.*;

/**	This class contains all of the methods and attributes needed by the Target Account Planner
 *	as needed
 *
 *	@author Carl Abel
 */
public class PrevSamples implements java.io.Serializable {
	Vector _cats = new Vector(5,5);
	Vector _qtys = new Vector(5,5);
	Vector _dates = new Vector(5,5);
	Vector _requestors = new Vector(5,5);
	int idx = -1;
	
	private static final long serialVersionUID = 100;

	public void add(String cat, int qty, Date date, String requestor) {
		_cats.addElement(cat);
		_dates.addElement(date);
		_requestors.addElement(requestor);
		
		Integer cnvt = new Integer(qty);
		_qtys.addElement(cnvt);
	}
	
	public boolean next() {
		if (idx < _cats.size() - 1) {
			idx++;
			return true;
		}
		return false;

	}
	
	public boolean prev() {
		if (idx > 0) {
			idx--;
			return true;
		}
		return false;

	}
	
	public String getCatalogNum() {
		return (String)_cats.elementAt(idx);
	}
	
	public int getQty() {
		Integer temp = (Integer)_qtys.elementAt(idx);
		return temp.intValue();
	}
	
	public Date getRequestDate() {
		return (Date)_dates.elementAt(idx);
	}
	
	public String getRequestorId() {
		return (String)_requestors.elementAt(idx);
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

		returnString += "\tcatalogNum = " + this.getCatalogNum() + "\n";
		returnString += "\tqty = " + this.getQty() + "\n";
		returnString += "\trequestDate = " + this.getRequestDate() + "\n";
		returnString += "\trequestorId = " + this.getRequestorId() + "\n";

		return returnString;
	}
}
