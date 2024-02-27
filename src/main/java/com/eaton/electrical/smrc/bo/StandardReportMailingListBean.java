/*
 * Created on Jan 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.eaton.electrical.smrc.bo;

/**
 * @author Jason Lubbert
 *
  */
public class StandardReportMailingListBean implements java.io.Serializable {
	
	private static final long serialVersionUID = 100;

	String customerName = null;
	String vistaCustomerNumber = null; 
	String firstName = null;
	String lastName = null;
	String phoneNumber = null;
	String title = null;
	String emailAddress = null;
	String faxNumber = null;
	String titleDescription = null;
	String addressLine1 = null;
	String city = null;
	String state = null;
	String zip = null;
	String companyPhoneNumber = null;
	
	public StandardReportMailingListBean (){
		customerName = "";
		vistaCustomerNumber = "";
		firstName = "";
		lastName = "";
		phoneNumber = "";
		title = "";
		emailAddress = "";
		faxNumber = "";
		titleDescription = "";
		addressLine1 = "";
		city = "";
		state = "";
		zip = "";
		companyPhoneNumber = "";
	}
	
	
	
	

	/**
	 * @return Returns the addressLine1.
	 */
	public String getAddressLine1() {
		return addressLine1;
	}
	/**
	 * @param addressLine1 The addressLine1 to set.
	 */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	/**
	 * @return Returns the city.
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city The city to set.
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return Returns the customerName.
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName The customerName to set.
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return Returns the emailAddress.
	 */
	public String getEmailAddress() {
		return emailAddress;
	}
	/**
	 * @param emailAddress The emailAddress to set.
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	/**
	 * @return Returns the faxNumber.
	 */
	public String getFaxNumber() {
		return faxNumber;
	}
	/**
	 * @param faxNumber The faxNumber to set.
	 */
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return Returns the phoneNumber.
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * @param phoneNumber The phoneNumber to set.
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * @return Returns the state.
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state The state to set.
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return Returns the titleDescription.
	 */
	public String getTitleDescription() {
		return titleDescription;
	}
	/**
	 * @param titleDescription The titleDescription to set.
	 */
	public void setTitleDescription(String titleDescription) {
		this.titleDescription = titleDescription;
	}
	/**
	 * @return Returns the vistaCustomerNumber.
	 */
	public String getVistaCustomerNumber() {
		return vistaCustomerNumber;
	}
	/**
	 * @param vistaCustomerNumber The vistaCustomerNumber to set.
	 */
	public void setVistaCustomerNumber(String vistaCustomerNumber) {
		this.vistaCustomerNumber = vistaCustomerNumber;
	}
	/**
	 * @return Returns the zip.
	 */
	public String getZip() {
		return zip;
	}
	/**
	 * @param zip The zip to set.
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}
	/**
	 * @return Returns the companyPhoneNumber.
	 */
	public String getCompanyPhoneNumber() {
		return companyPhoneNumber;
	}
	/**
	 * @param companyPhoneNumber The companyPhoneNumber to set.
	 */
	public void setCompanyPhoneNumber(String companyPhoneNumber) {
		this.companyPhoneNumber = companyPhoneNumber;
	}
}
