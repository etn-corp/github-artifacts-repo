package com.eaton.electrical.smrc.bo;

import java.util.*;

/**
 * @author E0062708
 *
 */
public class DistributorForms implements java.io.Serializable {

	private Account account = null;
	private User usr = null;

	private static final long serialVersionUID = 100;

	public DistributorForms(User usr, Account acct){
		setUsr(usr);
		setAccount(acct);
	}
	
	public User getUsr() {
		return usr;
	}
	
	public void setUsr(User usr) {
		this.usr = usr;
	}
	

	public void setAccount(Account acct) {
		this.account = acct;
	}
	
	public boolean isCreditAuthorization() {
		if(isDistributor() || account.isDirect()){
			return true;
		}
		return false;
		
	}

	public boolean isDistributor() {
		return account.isDistributor();
	}

	public boolean isTermination() {
		if(account.isActive() && account.isDistributor()) {
	//		if(account.getDistrictManager().getSalesId().equals(usr.getSalesId())
	//			|| account.getZoneManager().getSalesId().equals(usr.getSalesId()) 
		    if (isThisAccountsDMorZM() || usr.getUserGroup().equals("Global Channel Manager") || usr.getUserGroup().equals("Credit Manager")){
		    	return true;
			}
			return false;
		}
		return false;
	}
	
	private boolean isThisAccountsDMorZM(){
	   
	    ArrayList userSalesIds = new ArrayList(usr.getSalesIds());
	    for (int userIndex=0; userIndex < userSalesIds.size(); userIndex++){
	        String userId = (String) userSalesIds.get(userIndex);
	        ArrayList salesIds = new ArrayList(account.getDistrictManager().getSalesIds());
		    for (int i=0; i< salesIds.size(); i++){
		        String id = (String) salesIds.get(i);
		        if (userId.equalsIgnoreCase(id)){
		            return true;
		        }
		    }
		    salesIds = new ArrayList(account.getZoneManager().getSalesIds());
		    for (int i=0; i< salesIds.size(); i++){
		        String id = (String) salesIds.get(i);
		        if (userId.equalsIgnoreCase(id)){
		            return true;
		        }
		    }
	    }
	    
	    
	    return false;
	    
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

		returnString += "\taccount = " + this.account.toString() + "\n";
		returnString += "\tusr = " + this.usr.toString() + "\n";
		returnString += "\tdistributor? = " + this.isDistributor() + "\n";
		returnString += "\tcreditAuthorization? = " + this.isCreditAuthorization() + "\n";
		returnString += "\ttermination? = " + this.isTermination() + "\n";
		
		return returnString;
	}



}
