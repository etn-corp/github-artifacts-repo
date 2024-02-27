
package com.eaton.electrical.smrc.bo;

import java.util.*;

/**	This class contains all of the methods and attributes needed by the Target Account Planner
*	as needed
*
*	@author Carl Abel
*/
public class SampleRequest implements java.io.Serializable {
	private String _use = "";
	private Calendar _reqDt;
	private String _vcn = "";
	private String _custName = "";
	private ArrayList _samples = new ArrayList(5);
	private String _shipMethod = "";
	private String _requestor = "";
	private String _attn = "";
	private String _addr1 = "";
	private String _addr2 = "";
	private String _addr3 = "";
	private String _city = "";
	private String _state = "";
	private String _zip = "";

	private static final long serialVersionUID = 100;

	public void setVistaCustNum(String vcn) {
		_vcn = vcn;
	}

	public void setCustomerName(String name) {
		_custName = name;
	}

	public void setRequestDate(Calendar dt) {
		_reqDt = dt;
	}

	public void setRequestor(String req) {
		_requestor = req;
	}

	public void setUse(String use) {
		_use = use;
	}

	public void setShipMethod(String sm) {
		_shipMethod = sm;
	}

	public void setAttention(String attn) {
		_attn = attn;
	}

	public void setAddress1(String addr1) {
		_addr1 = addr1;
	}

	public void setAddress2(String addr2) {
		_addr2 = addr2;
	}

	public void setAddress3(String addr3) {
		_addr3 = addr3;
	}

	public void setCity(String city) {
		_city = city;
	}

	public void setState(String state) {
		_state = state;
	}

	public void setZip(String zip) {
		_zip = zip;
	}

	public void addProduct(SampleProduct prod) {
		_samples.add(prod);
	}

	/*
	*/
	public String getVistaCustNum() {
		return _vcn;
	}

	public String getCustomerName() {
		return _custName;
	}

	public Calendar getRequestDate() {
		return _reqDt;
	}

	public String getRequestDateAsString() {
		StringBuffer ret = new StringBuffer("");
		ret.append((_reqDt.get(Calendar.MONTH) + 1));
		ret.append("/");
		ret.append(_reqDt.get(Calendar.DATE));
		ret.append("/");
		ret.append(_reqDt.get(Calendar.YEAR));

		return ret.toString();
	}

	public String getRequestor() {
		return _requestor;
	}

	public String getUse() {
		return _use;
	}

	public String getShipMethod() {
		return _shipMethod;
	}

	public String getAttention() {
		return _attn;
	}

	public String getAddress1() {
		return _addr1;
	}

	public String getAddress2() {
		return _addr2;
	}

	public String getAddress3() {
		return _addr3;
	}

	public String getCity() {
		return _city;
	}

	public String getState() {
		return _state;
	}

	public String getZip() {
		return _zip;
	}

	public ArrayList getProducts() {
		return _samples;
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
		returnString += "\tcustomerName = " + this.getCustomerName() + "\n";
		returnString += "\trequestDate = " + this.getRequestDate() + "\n";
		returnString += "\trequestDateAsString = " + this.getRequestDateAsString() + "\n";
		returnString += "\trequestor = " + this.getRequestor() + "\n";
		returnString += "\tuse = " + this.getUse() + "\n";
		returnString += "\tshipMethod = " + this.getShipMethod() + "\n";
		returnString += "\tattention = " + this.getAttention() + "\n";
		returnString += "\taddress1 = " + this.getAddress1() + "\n";
		returnString += "\taddress2 = " + this.getAddress2() + "\n";
		returnString += "\taddress3 = " + this.getAddress3() + "\n";
		returnString += "\tcity = " + this.getCity() + "\n";
		returnString += "\tstate = " + this.getState() + "\n";
		returnString += "\tzip = " + this.getZip() + "\n";
		returnString += "\tproducts = " + this.getProducts() + "\n";

		return returnString;
	}
}
