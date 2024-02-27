
package com.eaton.electrical.smrc.bo;

import java.util.ArrayList;

/**
 * @author E0062708
 *
 */
public class ImpactedDistributor implements java.io.Serializable {
	private String vcn = null;
	private String customerName = null;
	private String state = null;
	private String city = null;
	private double annualEatonSales = 0;
	private int distId = 0;
	private int contactId = 0;
	private double salesAtRisk = 0;
	private String notes = null;
	private ArrayList contacts = null;
	
	private static final long serialVersionUID = 100;

	public ImpactedDistributor(){
		vcn = "";
		customerName = "";
		state = "";
		city = "";
		notes = "";
	}
	
	public int getContactId() {
		return contactId;
	}
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
	public int getDistId() {
		return distId;
	}
	public void setDistId(int distId) {
		this.distId = distId;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public double getSalesAtRisk() {
		return salesAtRisk;
	}
	public void setSalesAtRisk(double salesAtRisk) {
		this.salesAtRisk = salesAtRisk;
	}
	public String getVcn() {
		return vcn;
	}
	public void setVcn(String vcn) {
		this.vcn = vcn;
	}
	public double getAnnualEatonSales() {
		return annualEatonSales;
	}
	public void setAnnualEatonSales(double annualEatonSales) {
		this.annualEatonSales = annualEatonSales;
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

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	/** Sets a list of contacts associated with this Impacted Distributor
	 * @param contactList List of contacts
	 */
	public void setContacts(ArrayList contactList) {
		this.contacts = contactList;
	}
	
	/** Gets a list of contacts associated with this ImpactedDistributor
	 * @return list of contacts associated with this ImpactedDistributor
	 */
	public ArrayList getContacts() {
		return contacts;
	}

	public boolean equals(Object obj) {
		if (this == null || obj == null) {
			return false;
		}

		return super.equals(obj);
	}

	public String toString() {
		String returnString = "";

		returnString += "\tcontactId = " + this.getContactId() + "\n";
		returnString += "\tdistId = " + this.getDistId() + "\n";
		returnString += "\tnotes = " + this.getNotes() + "\n";
		returnString += "\tsalesAtRisk = " + this.getSalesAtRisk() + "\n";
		returnString += "\tvcn = " + this.getVcn() + "\n";
		returnString += "\tannualEatonSales = " + this.getAnnualEatonSales() + "\n";
		returnString += "\tcity = " + this.getCity() + "\n";
		returnString += "\tcustomerName = " + this.getCustomerName() + "\n";
		returnString += "\tstate = " + this.getState() + "\n";

		return returnString;
	}
}
