package com.eaton.electrical.smrc.bo;

/**	This class contains all of the methods and attributes needed by the Target Account Planner
*	as needed to manage the contacts in the account planner
*
*	@author Carl Abel
*/
public class Contact implements java.io.Serializable {
	private int _id = 0;
	private String _first = null;
	private String _last = null;
	private String _phone = null;
	private String fax = null;
	private String _vcn = null;
	private String _title = null;
	private String _email = null;
	private String functionalPosition = null;
	private String titleDescription=null;
	private String comments=null;
	private boolean pricingContact=false;
	private boolean distributorStatement=false;
	private int _titleId = 0;

	private static final long serialVersionUID = 100;

	public Contact(){
		_first = "";
		_last = "";
		_phone = "";
		fax="";
		_vcn = "";
		_title = "";
		functionalPosition="";
		_email = "";
		titleDescription="";
		comments="";	
	}
	
	/** Lets the calling routine set the id for this contact
	*	@param int The id for this contact
	*/
	public void setId(int id) {
		_id = id;
	}

	/** Lets the calling routine set the first name of this contact
	*	@param String The first name of this contact
	*/
	public void setFirstName(String first) {
		_first = first;
	}

	/** Lets the calling routine set the last name of this contact
	*	@param String The last name of this contact
	*/
	public void setLastName(String last) {
		_last = last;
	}

	/** Lets the calling routine set the phone number of this contact
	*	@param String The phone number of this contact
	*/
	public void setPhoneNumber(String phone) {
		_phone = phone;
	}

	/** Lets the calling routine assign a customer number to this contact
	*	@param String The customer number to be assigned this contact
	*/
	public void setCustomer(String vcn) {
		_vcn = vcn;
	}

	/** Lets the calling routine set this contact's title
	*	@param String This contact's title
	*/
	public void setTitle(String title) {
		_title = title;
	}

	/** Lets the calling routine set the email address of this contact
	*	@param String This contact's email address
	*/
	public void setEmailAddress(String email) {
		_email = email;
	}

	public void setTitleId(int id) {
		_titleId = id;
	}

	/** Retrieves the id for this contact
	*	@return int The id for this contact
	*/
	public int getId() {
		return _id;
	}

	/** Retrieves the first name for this contact
	*	@return String The first name of this contact
	*/
	public String getFirstName() {
		return _first;
	}

	/** Retrieves the last name for this contact
	*	@return String The last name of this contact
	*/
	public String getLastName() {
		return _last;
	}

	/** Retrieves the phone number for this contact
	*	@return String The phone number for this contact
	*/
	public String getPhone() {
		return _phone;
	}

	/** Retrieves the customer number for this contact
	*	@return String The customer number for this contact
	*/
	public String getCustomer() {
		return _vcn;
	}

	/** Retrieves the title for this contact
	*	@return String This contact's title
	*/
	public String getTitle() {
		return _title;
	}

	/** Retrieves the area code from this contact's phone number
	*	@return String The area code from this contact's phone number
	*/
	public String getAreaCode() {
		if (_phone.length() >= 3) {
			return _phone.substring(0,3);
		}

		return "";

	}

	/** Retrieves the local exchange for this contact's phone number
	*	@return String The local exchange for this contact's phone number
	*/
	public String getLocalNum() {
		if (_phone.length() >= 6) {
			return _phone.substring(3,6);
		}

		return "";

	}

	/** Retrieves the extension for this contact's phone number
	*	@return String The extension from this contact's phone number
	*/
	public String getExchange() {
		if (_phone.length() >= 10) {
			return _phone.substring(6,10);
		}

		return "";

	}

	/** Retrieves the email address for this contact
	*	@return String The email address for this contact
	*/
	public String getEmailAddress() {
		return _email;
	}

	public int getTitleId() {
		return _titleId;
	}

	/*
	 * Added by jpv so local method doesnt need called from jsp
	 */
	public void setTitleDescription(String titleDescription) {
		this.titleDescription=titleDescription;
		
	}
	public String getTitleDescription() {
		return this.titleDescription;
		
	}
	/**
	 * @return Returns the comments.
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments The comments to set.
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	/**
	 * @return Returns the pricingContact.
	 */
	public boolean isPricingContact() {
		return pricingContact;
	}
	/**
	 * @param pricingContact The pricingContact to set.
	 */
	public void setPricingContact(boolean pricingContact) {
		this.pricingContact = pricingContact;
	}
	public String getFunctionalPosition() {
		return functionalPosition;
	}
	public void setFunctionalPosition(String functionalPosition) {
		this.functionalPosition = functionalPosition;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
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
		returnString += "\tfirstName = " + this.getFirstName() + "\n";
		returnString += "\tlastName = " + this.getLastName() + "\n";
		returnString += "\tphone = " + this.getPhone() + "\n";
		returnString += "\tcustomer = " + this.getCustomer() + "\n";
		returnString += "\ttitle = " + this.getTitle() + "\n";
		returnString += "\tareaCode = " + this.getAreaCode() + "\n";
		returnString += "\tlocalNum = " + this.getLocalNum() + "\n";
		returnString += "\texchange = " + this.getExchange() + "\n";
		returnString += "\temailAddress = " + this.getEmailAddress() + "\n";
		returnString += "\ttitleId = " + this.getTitleId() + "\n";
		returnString += "\ttitleDescription = " + this.getTitleDescription() + "\n";
		returnString += "\tcomments = " + this.getComments() + "\n";
		returnString += "\tfunctionalPosition = " + this.getFunctionalPosition() + "\n";
		returnString += "\tfax = " + this.getFax() + "\n";
		returnString += "\tdistributorStatement? = " + this.isDistributorStatement() + "\n";

		return returnString;
	}
	public boolean isDistributorStatement() {
		return distributorStatement;
	}
	public void setDistributorStatement(boolean distributorStatement) {
		this.distributorStatement = distributorStatement;
	}
}
