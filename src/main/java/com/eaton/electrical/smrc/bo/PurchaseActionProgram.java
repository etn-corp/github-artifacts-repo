
package com.eaton.electrical.smrc.bo;

import java.sql.Date;
import java.util.*;


/**	This class contains all of the methods and attributes needed by the Target Account Planner
*	as needed
*
*	@author Carl Abel
*/
public class PurchaseActionProgram implements java.io.Serializable {
	String _vcn = "";
	String _product = "";
	String _action = "";
	String _objective = "";
	Date _schedule;
	String _results = "";
	String _assignedTo = "";
	boolean _complete = false;
	int _taskId = 0;
	String _userAdded = "";
	String _userChanged = "";
	ArrayList _ccEmail = null;
	int _ebeCat = 0;
	String _productDescription="", _ebeDescription="";
	ArrayList ccUsers = null;
	//User ccUser, assignedUser;
	User assignedUser;
	int customerVisitId = 0;
	String customerName = "";
	String assignedUserName = "";
	ArrayList attachments = null;
	
	private static final long serialVersionUID = 100;

	public PurchaseActionProgram(){
		_ccEmail = new ArrayList();
		ccUsers = new ArrayList();
		attachments = new ArrayList();
	}
	
	public void setCustomer(String vcn) {
		_vcn = vcn;
	}

	public void setProduct(String product) {
		_product = product;
	}

	public void setAction(String action) {
		_action = action;
	}

	public void setObjective(String objective) {
		_objective = objective;
	}

	public void setAssignedTo(String assignedTo) {
		_assignedTo = assignedTo;
	}

	public void setCCEmail(ArrayList ccEmail) {
		_ccEmail = ccEmail;
	}

	public void setComplete(boolean complete) {
		_complete = complete;
	}

	public void setSchedule(Date schedule) {
		_schedule = schedule;
	}

	public void setResults(String results) {
		_results = results;
	}

	public void setTaskId(int taskId) {
		_taskId = taskId;
	}

	public void setUserAdded(String user) {
		_userAdded = user;
	}

	public void setUserChanged(String user) {
		_userChanged = user;
	}

	public void setEBECategory(int ebe) {
		_ebeCat = ebe;
	}
	
	public void setCustomerVisitId (int customerVisitId){
		this.customerVisitId = customerVisitId;
	}
	
	public int getCustomerVisitId (){
		return customerVisitId;
	}
	public boolean isFromCustomerVisit(){
		if (customerVisitId > 0){
			return true;
		}
		return false;

	}

	public String getCustomer() {
		return _vcn;
	}

	public String getProduct() {
		return _product;
	}

	public String getAction() {
		return _action;
	}

	public String getObjective() {
		return _objective;
	}

	public String getAssignedTo() {
		return _assignedTo;
	}

	public ArrayList getCCEmail() {
		return _ccEmail;
	}
	public void addCCEmail(String userid) {
		_ccEmail.add(userid);
	}

	public boolean isComplete() {
		return _complete;
	}

	public Date getSchedule() {
		return _schedule;
	}

	public String getScheduleAsString() {
		if (_schedule != null && _schedule.toString().length() >= 10) {
			String year = _schedule.toString().substring(0,4);
			String mo = _schedule.toString().substring(5,7);
			String day = _schedule.toString().substring(8,10);
			return mo + "/" + day + "/" + year;
		}
		return "";
	}

	public String getResults() {
		return _results;
	}

	public int getTaskId() {
		return _taskId;
	}

	public String getUserAdded() {
		return _userAdded;
	}

	public String getUserChanged() {
		return _userChanged;
	}

	public int getEBECategory() {
		return _ebeCat;
	}


	public void setProductDescription(String productDescription) {
		_productDescription = productDescription;
	}
	public String getProductDescription() {
		return _productDescription;
	}
	
	public void setEBEDescription(String ebeDescription) {
		_ebeDescription = ebeDescription;
		
	}
	public String getEBEDescription() {
			return _ebeDescription;
	}


	public void setAssignedUser(User assignedUser) {
		this.assignedUser = assignedUser;
		
	}
	public User getAssignedUser() {
		return this.assignedUser;
		
	}
	
	public void setAssignedUserName(String assignedUserName){
		this.assignedUserName = assignedUserName;
	}
	public String getAssignedUserName(){
		return this.assignedUserName;
	}

	/*
	public void setCCUser(User ccUser) {
		this.ccUser=ccUser;
	}
	public User getCCUser() {
		return this.ccUser;
	}
	*/
	
	public ArrayList getCcUsers() {
		return ccUsers;
	}
	public void setCcUsers(ArrayList ccUsers) {
		this.ccUsers = ccUsers;
	}
	public void addCcUser(User user) {
		this.ccUsers.add(user);
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getCustomerName() { 
		return this.customerName;
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

		returnString += "\tcustomer = " + this.getCustomer() + "\n";
		returnString += "\tproduct = " + this.getProduct() + "\n";
		returnString += "\taction = " + this.getAction() + "\n";
		returnString += "\tobjective = " + this.getObjective() + "\n";
		returnString += "\tassignedTo = " + this.getAssignedTo() + "\n";
		returnString += "\tcCEmail = " + this.getCCEmail() + "\n";
		returnString += "\tschedule = " + this.getSchedule() + "\n";
		returnString += "\tscheduleAsString = " + this.getScheduleAsString() + "\n";
		returnString += "\tresults = " + this.getResults() + "\n";
		returnString += "\ttaskId = " + this.getTaskId() + "\n";
		returnString += "\tuserAdded = " + this.getUserAdded() + "\n";
		returnString += "\tuserChanged = " + this.getUserChanged() + "\n";
		returnString += "\teBECategory = " + this.getEBECategory() + "\n";
		returnString += "\tproductDescription = " + this.getProductDescription() + "\n";
		returnString += "\teBEDescription = " + this.getEBEDescription() + "\n";
		returnString += "\tassignedUser = " + this.getAssignedUser() + "\n";
		returnString += "\tccUsers = " + this.getCcUsers() + "\n";
		returnString += "\tcccustomerName = " + this.getCustomerName() + "\n";
		returnString += "\tccassignedUserName = " + this.getAssignedUserName() + "\n";
		
		return returnString;
	}

	public ArrayList getAttachments() {
		return attachments;
	}

	public void setAttachments(ArrayList attachments) {
		this.attachments = attachments;
	}
	public void addAttachment(TaskAttachment attachment){
		this.attachments.add(attachment);
	}
	
}
