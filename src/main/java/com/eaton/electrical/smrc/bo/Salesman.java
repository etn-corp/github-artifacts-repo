package com.eaton.electrical.smrc.bo;

import java.util.*;


// Don't use - can't get phone number or other user_profile information....
public class Salesman implements java.io.Serializable {
	private String vistaline_id = null;
	private String firstName = null;
	private String lastName = null;
    private String geogCd = null;
    private String title = null;
	private String salesId = null;
    private String salesOffice = null;
    private String districtName=null;
    private String emailAddress = null;
    private List salesIds = null;
	
	private static final long serialVersionUID = 100;

	public Salesman(){
    	vistaline_id = "";
    	firstName = "";
    	lastName = "";
        geogCd = "";
        title = "";
    	salesId = "";
        salesOffice = "";
    	districtName="";
    	emailAddress="";
    	salesIds = new ArrayList();
    }
    
    public void setVistalineId (String vistaline_id){
        this.vistaline_id = vistaline_id;
    }
    
    public void setFirstName (String firstName){
        this.firstName = firstName;
    }
    
    public void setLastName (String lastName){
        this.lastName = lastName;
    }
    
    public void setGeogCd (String geogCd){
        this.geogCd = geogCd;
    }
    
    public void setTitle (String title){
        this.title = title;
    }
    
    public void setSalesId (String salesId){
        this.salesId = salesId;
    }
    
    public void setSalesOffice (String salesOffice){
        this.salesOffice = salesOffice;
    }
    
    public String getVistalineId (){
        return this.vistaline_id;
    }
    
    public String getFirstName (){
        return this.firstName;
    }
    
    public String getLastName (){
        return this.lastName;
    }
    
    public String getGeogCd (){
        return this.geogCd;
    }
    
    public String getTitle (){
        return this.title;
    }
    
    public String getSalesId (){
        return this.salesId;
    }
    
     public String getSalesOffice (){
        return this.salesOffice;
    }
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
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

		returnString += "\tvistalineId  = " + this.getVistalineId () + "\n";
		returnString += "\tfirstName  = " + this.getFirstName () + "\n";
		returnString += "\tlastName  = " + this.getLastName () + "\n";
		returnString += "\tgeogCd  = " + this.getGeogCd () + "\n";
		returnString += "\ttitle  = " + this.getTitle () + "\n";
		returnString += "\tsalesId  = " + this.getSalesId () + "\n";
		returnString += "\tsalesOffice  = " + this.getSalesOffice () + "\n";
		returnString += "\tdistrictName = " + this.getDistrictName() + "\n";
		returnString += "\temailAddress = " + this.getEmailAddress() + "\n";

		return returnString;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

    public List getSalesIds() {
        return salesIds;
    }

    public void setSalesIds(List salesIds) {
        this.salesIds = salesIds;
    }
    public String salesIdsToString(){
        StringBuffer returnBuffer = new StringBuffer();
        Iterator it = salesIds.iterator();
        boolean addComma=false;
        while(it.hasNext()) {
            if(addComma==true) returnBuffer.append(", ");
            returnBuffer.append(it.next());
            addComma=true;
        }
        return returnBuffer.toString();
        
    }
    public boolean isSalesId(String id) {
        Iterator it = salesIds.iterator();
        while(it.hasNext()) {
            if(it.next().equals(id)) {
                return true;
            }
        }
        return false;
    }

    
}
