
package com.eaton.electrical.smrc.bo;

import java.util.*;

/**
 * @author E0062708
 *
 */
public class CustomerVisit implements java.io.Serializable {
	
	private int id = 0;
	private int reasonId = 0;
	private String reason = null;
	private int outcomeId = 0;
	private String outcome = null;
	private Date visitDate = null;
	private Date nextVisitDate = null;
	private Date dateAdded = null;
	private String notes = null;
	private String description = null;
	private String vcn = null;
	private String[] users;
	private String[] contacts;
	private String customerName = null;
	private String salesEngineer = null;
	private String userIdAdded = null;
	private String userIdChanged = null;
	
	private static final long serialVersionUID = 100;

	public CustomerVisit(){
		notes = "";
		vcn = "";
		description="";
		visitDate=new GregorianCalendar().getTime();
		customerName = "";
		salesEngineer = "";
		reason = "";
		outcome = "";
		userIdAdded="";
		userIdChanged = "";
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getNextVisitDate() {
		if(nextVisitDate!=null && nextVisitDate.toString().equals("0002-11-30")){
			return null;
		}
		return nextVisitDate;

	}
	public void setNextVisitDate(Date nextVisitDate) {
		this.nextVisitDate = nextVisitDate;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public int getOutcomeId() {
		return outcomeId;
	}
	public void setOutcomeId(int outcomeId) {
		this.outcomeId = outcomeId;
	}
	public int getReasonId() {
		return reasonId;
	}
	public void setReasonId(int reasonId) {
		this.reasonId = reasonId;
	}
	public String getVcn() {
		return vcn;
	}
	public void setVcn(String vcn) {
		this.vcn = vcn;
	}
	public Date getVisitDate() {
		if(visitDate!=null && visitDate.toString().equals("0002-11-30")){
			return null;
		}
		return visitDate;

	}
	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}
	
	
	public String getVisitDay() {
		if(getVisitDate()==null){
			return "";
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime (visitDate);
	    return ""+calendar.get(Calendar.DATE);
	}
	public String getVisitMonth() {
		if(getVisitDate()==null){
			return "";
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime (visitDate);
	    return ""+(calendar.get(Calendar.MONTH)+1);
	}
	public String getVisitYear() {
		if(getVisitDate()==null){
			return "";
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime (visitDate);
	    return ""+calendar.get(Calendar.YEAR);
	}
	
	public String getNextVisitDay() {
		if(getNextVisitDate()==null){
			return "";
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime (nextVisitDate);
	    return ""+calendar.get(Calendar.DATE);
	}
	public String getNextVisitMonth() {
		if(getNextVisitDate()==null){
			return "";
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime (nextVisitDate);
	    return ""+(calendar.get(Calendar.MONTH)+1);
	}
	public String getNextVisitYear() {
		if(getNextVisitDate()==null){
			return "";
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime (nextVisitDate);
	    return ""+calendar.get(Calendar.YEAR);
	}
	
	
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public void setUsers(String[] users) {
		this.users = users;
	}

	public void setContacts(String[] contacts) {
		this.contacts=contacts;
		
	}
	public String[] getContacts() {
		return contacts;
	}
	public String[] getUsers() {
		return users;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getSalesEngineer() {
		return salesEngineer;
	}
	public void setSalesEngineer(String salesEngineer) {
		this.salesEngineer = salesEngineer;
	}
	public String getOutcome() {
		return outcome;
	}
	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getUserIdAdded() {
		return userIdAdded;
	}
	public void setUserIdAdded(String userIdAdded) {
		this.userIdAdded = userIdAdded;
	}
	public String getUserIdChanged() {
		return userIdChanged;
	}
	public void setUserIdChanged(String userIdChanged) {
		this.userIdChanged = userIdChanged;
	}
}
