package com.eaton.electrical.smrc.bo;

import java.util.*;

import com.eaton.electrical.smrc.util.*;

/**
 * @author E0062708
 *
 */
public class CustomerSearchCriteria implements java.io.Serializable {
	
	private String customerName = null;
	private String vcn = null;
	private boolean parentsOnly = false;
	private String lastName = null;
	private String firstName = null;
	private String state = null;
	private String district = null;
	private String division = null;
	private boolean potentialCustomersOnly = false;
	private String firstRecNum = null;
	private String seId = null;
	private List salesIds = null;
	private boolean showSales = false;
	private String sort = null;
	private String sortDir = null;
	private String segment = null;
	private String otherHomeSegment=null;
	private boolean targetOnly = false;
	private boolean districtTargets = false;
    private boolean divisionTargets = false;
	private User usr = null;
	private String dollarType = null;
	private boolean xcel = false;
	
	private static final long serialVersionUID = 100;

	public CustomerSearchCriteria(){
		customerName = "";
		vcn="";
		lastName = "";
		firstName = "";
		state = "0";
		district = "0";
		division = "0";
		firstRecNum = "";
		seId="";
		sort = "";
		sortDir = "";
		segment="";
		otherHomeSegment="";
		usr = new User();
		dollarType = "";
		salesIds = new ArrayList();
	}
	
	
	
	public String getCustomerName() {
		return StringManipulation.fixSQLQuotes(customerName.trim());
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getDistrict() {
		return district.trim();
	}
	public void setDistrict(String district) {
		if(district.equals("")){
			district="0";
		}
		this.district = district;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		if(division.equals("")){
			division="0";
		}
		this.division = division;
	}
	public String getFirstName() {
		return StringManipulation.fixSQLQuotes(firstName.trim());
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return StringManipulation.fixSQLQuotes(lastName.trim());
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public boolean isParentsOnly() {
		return parentsOnly;
	}
	public void setParentsOnly(String parentsOnly) {
		if(parentsOnly.equals("Y")){
			this.parentsOnly = true;
		}
	}
	public boolean isPotentialCustomersOnly() {
		return potentialCustomersOnly;
	}
	public void setPotentialCustomersOnly(String potentialCustomersOnly) {
		if(potentialCustomersOnly.equals("Y")){
			this.potentialCustomersOnly = true;
		}
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		if(state.equals("")){
			state="0";
		}
		this.state = state;
	}
	public String getVcn() {
		return vcn.trim();
	}
	public void setVcn(String vcn) {
		this.vcn = vcn;
	}
	public String getFirstRecNum() {
		return firstRecNum;
	}
	public void setFirstRecNum(String firstRecNum) {
		this.firstRecNum = firstRecNum;
	}
	public String getSeId() {
		return seId;
	}
	public void setSeId(String seId) {
		this.seId = seId;
	}
	public boolean isValidCriteria() {
		if(customerName.equals("")
				&& vcn.equals("")
				&& lastName.equals("")
				&& firstName.equals("")
				&& state.equals("0")
				&& district.equals("0")
				&& seId.equals("")
				&& segment.equals("")
				&& otherHomeSegment.equals("")
				&& !isParentsOnly()
				&& !isPotentialCustomersOnly()
				&& !isDistrictTargets()){
			return false;
		}
		return true;
		
	}	
	public boolean isShowSales() {
		return showSales;
	}
	public void setShowSales(boolean showSales) {
		this.showSales = showSales;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getSortDir() {
		return sortDir;
	}
	public void setSortDir(String sortDir) {
		this.sortDir = sortDir;
	}
	public String getSegment() {
		return segment;
	}
	public void setSegment(String segment) {
		this.segment = segment;
	}
	public String getOtherHomeSegment() {
		return otherHomeSegment;
	}
	public void setOtherHomeSegment(String otherHomeSegment) {
		this.otherHomeSegment = otherHomeSegment;
	}
	public boolean isTargetOnly() {
		return targetOnly;
	}
	public void setTargetOnly(boolean targetOnly) {
		this.targetOnly = targetOnly;
	}
        public boolean isDivisionTargets(){
                return divisionTargets;
        }
        
        public void setDivisionTargets(String divisionTargets){
                if(divisionTargets.equals("true")){
			this.divisionTargets = true;
		}
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

		returnString += "\tcustomerName = " + this.getCustomerName() + "\n";
		returnString += "\tdistrict = " + this.getDistrict() + "\n";
		returnString += "\tdivision = " + this.getDivision() + "\n";
		returnString += "\tfirstName = " + this.getFirstName() + "\n";
		returnString += "\tlastName = " + this.getLastName() + "\n";
		returnString += "\tstate = " + this.getState() + "\n";
		returnString += "\tvcn = " + this.getVcn() + "\n";
		returnString += "\tfirstRecNum = " + this.getFirstRecNum() + "\n";
		returnString += "\tseId = " + this.getSeId() + "\n";
		returnString += "\tsort = " + this.getSort() + "\n";
		returnString += "\tsortDir = " + this.getSortDir() + "\n";
		returnString += "\tsegment = " + this.getSegment() + "\n";
		returnString += "\totherHomeSegment = " + this.getOtherHomeSegment() + "\n";

		return returnString;
	}
	public boolean isDistrictTargets() {
		return districtTargets;
	}
	public void setDistrictTargets(String districtTargets) {
		if(districtTargets.equals("true")){
			this.districtTargets = true;
		}
	}
	public User getUser() {
		return usr;
	}
	public void setUser(User usr) {
		this.usr = usr;
	}
	public String getDollarType() {
		return dollarType;
	}
	public void setDollarType(String dollarType) {
		this.dollarType = dollarType;
	}
	
	public boolean isDirectDollars() {
		if(getDollarType().equalsIgnoreCase("Charge To")){
			return true;
		}
		return false;
		
	}
	public boolean isEndMarketDollars() {
		if(getDollarType().equalsIgnoreCase("End Market")){
			return true;
		}
		return false;

	}
	
	public boolean isXcel() {
		return xcel;
	}
	public void setXcel(boolean xcel) {
		this.xcel = xcel;
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
            if(addComma==true) returnBuffer.append(",");
            returnBuffer.append("'").append(it.next()).append("'");
            addComma=true;
        }
        return returnBuffer.toString();
        
    }
}
