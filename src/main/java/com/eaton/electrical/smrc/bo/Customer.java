package com.eaton.electrical.smrc.bo;

import java.text.*;

/**	This class contains all of the methods and attributes needed by the Target Account Planner
 *	as needed
 *
 *	@author Carl Abel
 */
public class Customer implements java.io.Serializable {
	String _vcn = "";
	String _name = "";
	String _address = "";
	String _city = "";
	String _state = "";
	String _zip = "";
	String _phone = "";
	String _salesId = "";
	String salesEngineer2 = "";
	String salesEngineer3 = "";
	String salesEngineer4 = "";
	String _region = "";
	String _segment = "";
	String _dunsNum = "";
	String _sicCode = "";
	String _webSite = "";
	String _desc = "";
	int _focusType = 0;
	String _indOther = "";
	int _stage = 0;
	String stageName=null;
	String _group;
	int _numStores = 0;
	String _parent = "";
	String _spGeog = "";
	String _gam = "", salesmanName="", focusTypeDesc="", sicDescription="", zoneGeogName="", distGeogName="";
	int _pcntWithCust = 0;
	boolean ableToSee=false, customerExists=false;
	boolean targetAccount=false;
	
	private static final long serialVersionUID = 100;

	public Customer() {
		stageName="";
	}
	
	public Customer(String vcn) {
		setVistaCustNum(vcn);
	}
	
	public void setGroup(String group) {
		_group = group;
	}
	
	public String getGroup() {
		return _group;
	}
	
	public void setVistaCustNum(String vcn) {
		_vcn = vcn;
	}
	
	public void setParent(String vcn) {
		_parent = vcn;
	}
	
	public void setName (String name) {
		_name = name;
	}
	
	public void setAddress (String address) {
		_address = address;
	}
	
	public void setCity (String city) {
		_city = city;
	}
	
	public void setState (String state) {
		_state = state;
	}
	
	public void setZipCode (String zip) {
		_zip = zip;
	}
	
	public void setPhoneNumber (String phone) {
		_phone = phone;
	}
	
	public void setSalesId (String salesId) {
		_salesId = salesId;
	}
	
	public void setRegion (String region) {
		_region = region;
	}
	
	public void setSegment (String segment) {
		_segment = segment;
	}
	
	public void setRegionWithGeog (String geog) {
		_region = geog.substring(2,4);
	}
	
	public void setDunsNum (String dunsNum) {
		_dunsNum = dunsNum;
	}
	
	public void setSICCode (String sicCode) {
		_sicCode = sicCode;
	}
	
	public void setWebSite (String webSite) {
		_webSite = webSite;
	}
	
	public void setDescription (String desc) {
		_desc = desc;
	}
	
	public void setFocusType(int id) {
		_focusType = id;
	}
	
	public void setIndustryOther(String indOther) {
		_indOther = indOther;
	}
	
	public void setStage(int stage) {
		_stage = stage;
	}
	
	public void setNumStores(int num) {
		_numStores = num;
	}
	
	public void setSPGeog(String spGeog) {
		_spGeog = spGeog;
	}
	
	public void setGlobalAcctMgr(String gam) {
		_gam = gam;
	}
	
	public void setPcntTimeWithCust(int pcnt) {
		_pcntWithCust = pcnt;
	}
	
	public String getVistaCustNum() {
		return _vcn;
	}
	
	public String getName () {
		return _name;
	}
	
	public String getAddress () {
		return _address;
	}
	
	public String getCity () {
		return _city;
	}
	
	public String getState () {
		return _state;
	}
	
	public String getZipCode () {
		return _zip;
	}
	
	public String getPhoneNumber () {
		return _phone;
	}
	
	public String getSalesId () {
		return _salesId;
	}
	
	public String getRegion () {
		return _region;
	}
	
	public String getSegment() {
		return _segment;
	}
	
	public String getDunsNum () {
		return _dunsNum;
	}
	
	public String getSICCode () {
		return _sicCode;
	}
	
	public String getWebSite () {
		return _webSite;
	}
	
	public String getDescription () {
		return _desc;
	}
	
	public int getFocusType() {
		return _focusType;
	}
	
	public String getIndustryOther() {
		return _indOther;
	}
	
	public int getStage() {
		return _stage;
	}
	
	public int getNumStores() {
		return _numStores;
	}
	
	public String getParent() {
		return _parent;
	}
	
	public String getSPGeog() {
		return _spGeog;
	}
	
	public boolean isParent() {
		return (_parent.equals(_vcn));
	}
	
	public String getGlobalAcctMgr() {
		return _gam;
	}
	
	public int getPcntTimeWithCust() {
		return _pcntWithCust;
	}
	
	public String getPcntTimeWithCustAsString() {
		NumberFormat nf = NumberFormat.getPercentInstance();
		return nf.format(_pcntWithCust/100);
	}
	
	
	public void setAbleToSee(boolean ableToSee) {
		this.ableToSee=ableToSee;
		
	}
	public boolean getAbleToSee() {
		return this.ableToSee;
		
	}
	
	/**
	 * added by jpv so dont have to call local method when looping thru custs
	 */
	public void setSalesmanName(String salesmanName) {
		this.salesmanName=salesmanName;
		
	}
	public String getSalesmanName() {
		return this.salesmanName;
		
	}
	
	/**
	 * added by jpv so local method doesnt need called
	 */
	public void setFocusTypeDesc(String focusTypeDesc) {
		this.focusTypeDesc=focusTypeDesc;
		
	}
	public String getFocusTypeDesc() {
		return this.focusTypeDesc;
		
	}
	
	/**
	 * added by jpv
	 */
	public void setCustomerExists(boolean customerExists) {
		this.customerExists=customerExists;
		
	}
	public boolean getCustomerExists() {
		return this.customerExists;
		
	}
	
	/**
	 * added by jpv so local method doesnt need called
	 */
	public void setSICDescription(String sicDescription) {
		this.sicDescription=sicDescription;
		
	}
	public String getSICDescription() {
		return this.sicDescription;
		
	}
	
	/**
	 * added by jpv so local method doesnt need called
	 */
	public void setDistrictGeogName(String distGeogName) {
		this.distGeogName=distGeogName;
		
	}
	public String getDistrictGeogName() {
		return this.distGeogName;
		
	}
	/**
	 * added by jpv so local method doesnt need called
	 */
	public void setZoneGeogName(String zoneGeogName) {
		this.zoneGeogName=zoneGeogName;
		
	}
	public String getZoneGeogName() {
		return this.zoneGeogName;
		
	}

	public boolean isTargetAccount() {
		return targetAccount;
	}
	public void setTargetAccount(boolean targetAccount) {
		this.targetAccount = targetAccount;
	}
	public String getStageName() {
		return stageName;
	}
	public void setStageName(String stageName) {
		this.stageName = stageName;
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

		returnString += "\tgroup = " + this.getGroup() + "\n";
		returnString += "\tvistaCustNum = " + this.getVistaCustNum() + "\n";
		returnString += "\tname  = " + this.getName () + "\n";
		returnString += "\taddress  = " + this.getAddress () + "\n";
		returnString += "\tcity  = " + this.getCity () + "\n";
		returnString += "\tstate  = " + this.getState () + "\n";
		returnString += "\tzipCode  = " + this.getZipCode () + "\n";
		returnString += "\tphoneNumber  = " + this.getPhoneNumber () + "\n";
		returnString += "\tsalesId  = " + this.getSalesId () + "\n";
		returnString += "\tregion  = " + this.getRegion () + "\n";
		returnString += "\tsegment = " + this.getSegment() + "\n";
		returnString += "\tdunsNum  = " + this.getDunsNum () + "\n";
		returnString += "\tsICCode  = " + this.getSICCode () + "\n";
		returnString += "\twebSite  = " + this.getWebSite () + "\n";
		returnString += "\tdescription  = " + this.getDescription () + "\n";
		returnString += "\tfocusType = " + this.getFocusType() + "\n";
		returnString += "\tindustryOther = " + this.getIndustryOther() + "\n";
		returnString += "\tstage = " + this.getStage() + "\n";
		returnString += "\tnumStores = " + this.getNumStores() + "\n";
		returnString += "\tparent = " + this.getParent() + "\n";
		returnString += "\tsPGeog = " + this.getSPGeog() + "\n";
		returnString += "\tglobalAcctMgr = " + this.getGlobalAcctMgr() + "\n";
		returnString += "\tpcntTimeWithCust = " + this.getPcntTimeWithCust() + "\n";
		returnString += "\tpcntTimeWithCustAsString = " + this.getPcntTimeWithCustAsString() + "\n";
		returnString += "\tableToSee = " + this.getAbleToSee() + "\n";
		returnString += "\tsalesmanName = " + this.getSalesmanName() + "\n";
		returnString += "\tfocusTypeDesc = " + this.getFocusTypeDesc() + "\n";
		returnString += "\tcustomerExists = " + this.getCustomerExists() + "\n";
		returnString += "\tsICDescription = " + this.getSICDescription() + "\n";
		returnString += "\tdistrictGeogName = " + this.getDistrictGeogName() + "\n";
		returnString += "\tzoneGeogName = " + this.getZoneGeogName() + "\n";
		returnString += "\tstageName = " + this.getStageName() + "\n";
		returnString += "\tsalesEngineer2 = " + this.getSalesEngineer2() + "\n";
		returnString += "\tsalesEngineer3 = " + this.getSalesEngineer3() + "\n";
		returnString += "\tsalesEngineer4 = " + this.getSalesEngineer4() + "\n";

		return returnString;
	}
    /**
     * @return Returns the salesEngineer2.
     */
    public String getSalesEngineer2() {
        return salesEngineer2;
    }
    /**
     * @param salesEngineer2 The salesEngineer2 to set.
     */
    public void setSalesEngineer2(String salesEngineer2) {
        this.salesEngineer2 = salesEngineer2;
    }
    /**
     * @return Returns the salesEngineer3.
     */
    public String getSalesEngineer3() {
        return salesEngineer3;
    }
    /**
     * @param salesEngineer3 The salesEngineer3 to set.
     */
    public void setSalesEngineer3(String salesEngineer3) {
        this.salesEngineer3 = salesEngineer3;
    }
    /**
     * @return Returns the salesEngineer4.
     */
    public String getSalesEngineer4() {
        return salesEngineer4;
    }
    /**
     * @param salesEngineer4 The salesEngineer4 to set.
     */
    public void setSalesEngineer4(String salesEngineer4) {
        this.salesEngineer4 = salesEngineer4;
    }
}
