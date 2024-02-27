package com.eaton.electrical.smrc.bo;

/**
 * @author E0062708
 * Used by the Customer Browse pages
 */
public class AccountBrowseRecord implements java.io.Serializable {
	
	private static final long serialVersionUID = 100;

	private String vcn = null;
	private String customerName = null;
	private String city = null;
	private String state = null;
	private String zip = null;
	private String parentNumber = null;
	
	public AccountBrowseRecord(){
		vcn = "";
		customerName = "";
		city = "";
		state = "";
		zip = "";
		parentNumber = "";
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getVcn() {
		return vcn;
	}
	public void setVcn(String vcn) {
		this.vcn = vcn;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getParentNumber (){
		return parentNumber;
	}
	public void setParentNumber(String parentNumber){
		this.parentNumber = parentNumber;
	}
}
