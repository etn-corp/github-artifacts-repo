package com.eaton.electrical.smrc.bo;


import java.util.*;


/**
 * @author E0062708
 *
 */
public class Account extends Object implements java.io.Serializable {

	private static final long serialVersionUID = 100;

	private String vcn = null;
	private String prospectNumber = null;
	private String customerName = null;
	private String status = null;
	private String parentCustNumber = null;
	private String parentName = null;
	//private String leadSE = null;
	private String salesEngineer1 = null;
	// Used in customer listing only
	private String salesEngineer1Name = null;
	private String salesEngineer2 = null;
	private String salesEngineer3 = null;
	private String salesEngineer4 = null;
	private String district = null;
	private ArrayList segments = null;
	private Address businessAddress = null;
	private Address shipAddress = null;
	private Address billToAddress = null;
	private String phone = null;
	private String fax = null;
	private String website = null;
	
	private boolean targetAccount = false;
	//private int targetAccountType = 0;
	private boolean districtTargetAccount = false;
	private ArrayList divisionTargetAccount = null;
	
	private String numOfWhatever = null;
	private ArrayList contacts = null;
	private String backgroundInfo = null;
	private String intlPhoneNumber = null;
	private String intlFaxNumber = null;
	private boolean parentOnly=false;
	private boolean direct = false;
	private boolean exemptCertRequired= false;
	private boolean sendConfirmation = false;
	
	private String APCont = null;
	private String APContPhoneNumber = null;
	private String APContEmailAddress = null;
	
	/*
	 * TODO primarySegmentName and secondarySegmentName are for the customer listing page
	 * the are postfixed with "Name" to indicate that a segment object is not returned.  Just the name
	 * It is NOT always populated (with getAccount()) so may need addressed later
	 */
	private String primarySegmentName = null;
	private String secondarySegmentName = null;
	
	private AccountNumbers numbers = null;
	private int rownum = 0;
	private String commitmentLevel = null;
	private String dpcNum = null;
	private String storeNumber = null;
	private String synergyCode = null;
	private String genesisNumber = null;
	private String sendToVistaNotes = null;
	private String vistaReferenceNumber = null;
	
	// distributor statement contact is the id of the contact selected in Account Profile
	// to receive the distributor statement
	private int distributorStatementContact = 0;
	private int applicationCode = 0;
	private int focusType;

	//private String sicCode=null;
	private String stage=null;
	
	private Salesman districtManager = null;
	private Salesman zoneManager = null;
	private String userIdAdded = null;
	private String userIdChanged = null;
	private Date dateAdded = null;
	private Date dateChanged = null;
	private ArrayList specialProgramIds = null;
	
	public Account() {
		vcn = "";
		customerName = "";
		prospectNumber = "";
		status = "Prospect";
		parentCustNumber = "";
		//leadSE = "";
		salesEngineer1 = "";
		salesEngineer1Name = "";
		salesEngineer2 = "";
		salesEngineer3 = "";
		salesEngineer4 = "";
		district = "";
		segments = new ArrayList();
		businessAddress = new Address();
		shipAddress = new Address();
		billToAddress = new Address();
		phone = "";
		fax = "";	
		website = "";
		divisionTargetAccount = new ArrayList();
		numOfWhatever = "";
		contacts = new ArrayList();
		backgroundInfo = "";
		intlPhoneNumber = "";
		intlFaxNumber = "";
		numbers = new AccountNumbers();
        commitmentLevel = "";
        dpcNum = "";
        storeNumber="";
        synergyCode = "";
        genesisNumber = "";
        sendToVistaNotes = "";
        vistaReferenceNumber = "";
        //sicCode = "";
        stage="";
    	primarySegmentName = "";
    	secondarySegmentName = "";
    	districtManager = new Salesman();
    	zoneManager = new Salesman();
    	userIdAdded="";
    	userIdChanged="";
    	focusType=0;
    	specialProgramIds = new ArrayList();
    	APCont ="";
    	APContPhoneNumber="";
    	APContEmailAddress="";

	} 


	public String getBackgroundInfo() {
		return backgroundInfo;
	}
	public void setBackgroundInfo(String backgroundInfo) {
		this.backgroundInfo = backgroundInfo;
	}
	public ArrayList getContacts() {
		return contacts;
	}
	public void setContacts(ArrayList contacts) {
		this.contacts = contacts;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}

	/*
	public String getLeadSE() {
		return leadSE;
	}
	*/
	//public void setLeadSE(String leadSE) {
	//	this.leadSE = leadSE;
	//}
	public String getNumOfWhatever() {
		if(numOfWhatever.equals("")){
			return "0";
		}
		return this.numOfWhatever;
		
	}
	public void setNumOfWhatever(String numOfWhatever) {
		this.numOfWhatever=numOfWhatever;
	}
	public String getParentCustNumber() {
		if(parentCustNumber.equals("")){
			return vcn;
		}
		return this.parentCustNumber;			
		
	}
	public void setParentCustNumber(String parentCustNumber) {
		this.parentCustNumber = parentCustNumber;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public ArrayList getSegments() {
		return segments;
	}
	public void setSegments(ArrayList segments) {
		this.segments=segments;
	}
	public void addSegment(Segment segment) {
		segments.add(segment);
	}
	public void clearSegments() {
		segments.clear();
	}
	public String getStatus() {
		/*if(this.status.equals("Prospect") && !isProspect()){
			return "Active";
		}*/
		return this.status;
	}
	public void setStatus(String status) {
		//if(status.equals("")){
		//	this.status = "Prospect";
		//}else{
			this.status = status;
		//}
	}
	public ArrayList getDivisionTargetAccount() {
		return divisionTargetAccount;
	}
	
	public void setDivisionTargetAccount(String[] divisionTargetAccount,boolean setDist) {
		boolean isDist=false;
		setTargetAccount(false);
		this.divisionTargetAccount.clear();
		if(divisionTargetAccount!=null){

			setTargetAccount(true);
			for(int i=0; i<divisionTargetAccount.length;i++){
				if(divisionTargetAccount[i].equals("DISTRICT")){
					isDist=true;
				}else{
					this.divisionTargetAccount.add(divisionTargetAccount[i]);
				}
			}
		}
		
		if(setDist){
			if(isDist==true){
				setDistrictTargetAccount(true);
			}else{
				setDistrictTargetAccount(false);
			}	
		}
		
	}
	
	public String getVcn() {
		return vcn;
	}
	public void setVcn(String vcn) {
		this.vcn = vcn;
		
		
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public boolean isDirect() {
		return direct;
	}
	public void setDirect(String direct) {
		if(direct.equalsIgnoreCase("y")){
			this.direct = true;
		}else{
			this.direct = false;
		}
	}
	public boolean isExemptCertRequired() {
		return exemptCertRequired;
	}
	public void setExemptCertRequired(String exemptCertRequired) {
		if(exemptCertRequired.equalsIgnoreCase("y")){
			this.exemptCertRequired = true;
		}else{
			this.exemptCertRequired = false;
		}
	}
	public boolean isParentOnly() {
		return this.parentOnly;
	}
	public void setParentOnly(String parentOnly) {
		if(parentOnly.equalsIgnoreCase("y")){
			this.parentOnly=true;
		}else{
			this.parentOnly = false;
		}
	}
	public boolean isSendConfirmation() {
		return sendConfirmation;
	}
	public void setSendConfirmation(String sendConfirmation) {
		if(sendConfirmation.equalsIgnoreCase("y")){
			this.sendConfirmation = true;
		}else{
			this.sendConfirmation = false;
		}
	}
	public String getIntlFaxNumber() {
		return intlFaxNumber;
	}
	public void setIntlFaxNumber(String intlFaxNumber) {
		this.intlFaxNumber = intlFaxNumber;
	}
	public String getIntlPhoneNumber() {
		return intlPhoneNumber;
	}
	public void setIntlPhoneNumber(String intlPhoneNumber) {
		this.intlPhoneNumber = intlPhoneNumber;
	}
	public String getProspectNumber() {
		return prospectNumber;
	}
	public void setProspectNumber(String prospectNumber) {
		this.prospectNumber = prospectNumber;
	}
	public Address getBusinessAddress() {
		return businessAddress;
	}
	public void setBusinessAddress(Address businessAddress) {
		this.businessAddress = businessAddress;
	}
	public boolean isDistrictTargetAccount() {
		return districtTargetAccount;
	}
	public void setDistrictTargetAccount(boolean districtTargetAccount) {
		//if(districtTargetAccount.equals("Y")){
		//	setTargetAccount(true);
		//}
		this.districtTargetAccount = districtTargetAccount;
	}
	public Address getBillToAddress() {
		return billToAddress;
	}
	public void setBillToAddress(Address billToAddress) {
		this.billToAddress = billToAddress;
	}
	public Address getShipAddress() {
		return shipAddress;
	}
	public void setShipAddress(Address shipAddress) {
		this.shipAddress = shipAddress;
	}
	public String getSalesEngineer1() {
		return salesEngineer1;
	}
	public void setSalesEngineer1(String salesEngineer1) {
		this.salesEngineer1 = salesEngineer1;
	}
	public String getSalesEngineer2() {
		return salesEngineer2;
	}
	public void setSalesEngineer2(String salesEngineer2) {
		this.salesEngineer2 = salesEngineer2;
	}
	public String getSalesEngineer3() {
		return salesEngineer3;
	}
	public void setSalesEngineer3(String salesEngineer3) {
		this.salesEngineer3 = salesEngineer3;
	}
	public String getSalesEngineer4() {
		return salesEngineer4;
	}
	public void setSalesEngineer4(String salesEngineer4) {
		this.salesEngineer4 = salesEngineer4;
	}
	public boolean isTargetAccount() {
		return targetAccount;
	}
	public void setTargetAccount(boolean targetAccount) {
		this.targetAccount=targetAccount;
	}	
	public String getParentName() {
		return this.parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	//public void setTargetAccount(boolean targetAccount) {
	//	this.targetAccount = targetAccount;
	//}
	public int getTargetAccountType() {
		/*
		0 = no target account OR legacy account where type is not specified
		1 = district only
		2 = division only
		3 = district AND division
		*/
		
		int type=0;
		if(isDistrictTargetAccount()){
			type=1;
		}	
		if(divisionTargetAccount.size()!=0 && type==1){
			type=3;
		}else if(divisionTargetAccount.size()!=0){
			type=2;
		}
		
		return type;
	}

	public AccountNumbers getNumbers() {
		return numbers;
	}
	public void setNumbers(AccountNumbers numbers) {
		this.numbers = numbers;
	}
	public Segment getPrimarySegment() {
		if(segments==null){
			return null;
		}
		
		for(int i=0;i<segments.size();i++){
			Segment segment = (Segment)segments.get(i);
			if(segment.getLevel()==1){
				return segment;
			}
		}
		return null;
		// TODO Might need to return empty segment object
	}
	public Segment getSecondarySegment() {
		if(segments==null){
			return null;
		}
		
		for(int i=0;i<segments.size();i++){
			Segment segment = (Segment)segments.get(i);
			if(segment.getLevel()==2){
				return segment;
			}
		}
		return null;
		// TODO Might need to return empty segment object
	}
    public String getCommitmentLevel(){
            return commitmentLevel;
    }
    public void setCommitmentLevel(String commitmentLevel){
            this.commitmentLevel = commitmentLevel;
    }
	

	public Salesman getDistrictManager(){
		return this.districtManager;
	}
	
	public Salesman getZoneManager(){
		return this.zoneManager;
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
		returnString += "\tvcn = " + this.getVcn() + "\n";
		returnString += "\tcustomerName = " + this.getCustomerName() + "\n";		
		returnString += "\tvistaReferenceNumber = " + this.getVistaReferenceNumber() + "\n";
		returnString += "\tprospectNumber = " + this.getProspectNumber() + "\n";
		returnString += "\tparentCustNumber = " + this.getParentCustNumber() + "\n";		
		returnString += "\tbackgroundInfo = " + this.getBackgroundInfo() + "\n";
		//returnString += "\tcontacts = " + this.getContacts() + "\n";
		returnString += "\tdistrict = " + this.getDistrict() + "\n";
		returnString += "\tsalesEngineer1 = " + this.getSalesEngineer1() + "\n";
		returnString += "\tsalesEngineer2 = " + this.getSalesEngineer2() + "\n";
		returnString += "\tsalesEngineer3 = " + this.getSalesEngineer3() + "\n";
		returnString += "\tsalesEngineer4 = " + this.getSalesEngineer4() + "\n";		
		returnString += "\tphone = " + this.getPhone() + "\n";
		returnString += "\tfax = " + this.getFax() + "\n";
		returnString += "\tintlPhoneNumber = " + this.getIntlPhoneNumber() + "\n";
		returnString += "\tintlFaxNumber = " + this.getIntlFaxNumber() + "\n";
		returnString += "\tnumOfWhatever = " + this.getNumOfWhatever() + "\n";	
		//returnString += "\tsegments = " + this.getSegments() + "\n";
		returnString += "\tstatus = " + this.getStatus() + "\n";
		//returnString += "\tdivisionTargetAccount = " + this.getDivisionTargetAccount() + "\n";
		returnString += "\twebsite = " + this.getWebsite() + "\n";
		//returnString += "\tbusinessAddress = " + this.getBusinessAddress() + "\n";
		//returnString += "\tbillToAddress = " + this.getBillToAddress() + "\n";
		//returnString += "\tshipAddress = " + this.getShipAddress() + "\n";
		returnString += "\ttargetAccountType = " + this.getTargetAccountType() + "\n";
		//returnString += "\tnumbers = " + this.getNumbers() + "\n";
		//returnString += "\tprimarySegment = " + this.getPrimarySegment() + "\n";
		//returnString += "\tsecondarySegment = " + this.getSecondarySegment() + "\n";
		//returnString += "\tdistrictManager = " + this.getDistrictManager() + "\n";
		//returnString += "\tzoneManager = " + this.getZoneManager() + "\n";
		returnString += "\tdpcNum = " + this.getDpcNum() + "\n";
		returnString += "\tstoreNumber = " + this.getStoreNumber() + "\n";
		returnString += "\tsynergyCode = " + this.getSynergyCode() + "\n";
		returnString += "\tsendToVistaNotes = " + this.getSendToVistaNotes() + "\n";
		returnString += "\tgenesisNumber = " + this.getGenesisNumber() + "\n";
		returnString += "\tdistributorStatementContact = " + this.getDistributorStatementContact() + "\n";
		returnString += "\tapplicationCode = " + this.getApplicationCode() + "\n";
		returnString += "\tstage = " + this.getStage() + "\n";
		returnString += "\tactive? = " + this.isActive() + "\n";
		returnString += "\tdirect? = " + this.isDirect() + "\n";
		returnString += "\tdistributor? = " + this.isDistributor() + "\n";		
		returnString += "\tdistrictTargetAccount? = " + this.isDistrictTargetAccount() + "\n";
		returnString += "\texemptCertRequired? = " + this.isExemptCertRequired() + "\n";
		returnString += "\tnewAccount? = " + this.isNewAccount() + "\n";
		returnString += "\tparentOnly? = " + this.isParentOnly() + "\n";
		returnString += "\tpending? = " + this.isPending() + "\n";
		returnString += "\tprospect? = " + this.isProspect() + "\n";
		returnString += "\trejected? = " + this.isRejected() + "\n";
		returnString += "\tsendConfirmation? = " + this.isSendConfirmation() + "\n";
		returnString += "\ttargetAccount? = " + this.isTargetAccount() + "\n";
		returnString += "\tfocusType? = " + this.getFocusType() + "\n";
		returnString += "\tAPContact? = " + this.getAPCont() + "\n";
		returnString += "\tAPContPhone? = " + this.getAPContPhoneNumber() + "\n";
		returnString += "\tAPContEmail? = " + this.getAPContEmailAddress() + "\n";
		
		return returnString;
	}
	
	/*
	 * TODO This will need cleaned up.  I cant check by getStatus() because
	 * brand new accounts sent to vista that have NOT been updated by
	 * Decision Stream will still be "Prospect" even if they were sent and 
	 * are in Vista.
	 */


	
	public int getRownum() {
		return rownum;
	}
	public void setRownum(int rownum) {
		this.rownum = rownum;
	}

	public String getDpcNum() {
		return dpcNum;
	}
	public void setDpcNum(String dpcNum) {
		this.dpcNum = dpcNum;
	}
	public String getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}
	public String getSynergyCode() {
		return synergyCode;
	}
	public void setSynergyCode(String synergyCode) {
		this.synergyCode = synergyCode;
	}
	public String getGenesisNumber() {
		return genesisNumber;
	}
	public void setGenesisNumber(String genesisNumber) {
		this.genesisNumber = genesisNumber;
	}
	public String getSendToVistaNotes() {
		return sendToVistaNotes;
	}
	public void setSendToVistaNotes(String sendToVistaNotes) {
		this.sendToVistaNotes = sendToVistaNotes;
	}
	public String getVistaReferenceNumber() {
		return vistaReferenceNumber;
	}
	public void setVistaReferenceNumber(String vistaReferenceNumber) {
		this.vistaReferenceNumber = vistaReferenceNumber;
	}
	public boolean isDistributor() {
		if(segments==null){
			return false;
		}
		for(int i=0;i<segments.size();i++){
			Segment segment = (Segment)segments.get(i);
			if(segment.isDistributor()){
				return true;
			}
		}
		return false;
	}
	public int getDistributorStatementContact() {
		return distributorStatementContact;
	}
	public void setDistributorStatementContact(int distributorStatementContact) {
		this.distributorStatementContact = distributorStatementContact;
	}
	public int getApplicationCode() {
		return applicationCode;
	}
	public void setApplicationCode(int applicationCode) {
		this.applicationCode = applicationCode;
	}
	public String getSicCode() {
		for(int i=0;i<segments.size();i++){
			Segment segment = (Segment)segments.get(i);
			if(segment.getLevel()==2){
				return segment.getSicCode();
			}
		}
		return "";
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public void setPrimarySegmentName(String primarySegmentName) {
		this.primarySegmentName = primarySegmentName;
	}
	public void setSecondarySegmentName(String secondarySegmentName) {
		this.secondarySegmentName = secondarySegmentName;
	}
	public String getPrimarySegmentName() {
		return primarySegmentName;
	}
	public String getSecondarySegmentName() {
		return secondarySegmentName;
	}
	public void setDistrictManager(Salesman districtManager) {
		this.districtManager = districtManager;
	}
	public void setZoneManager(Salesman zoneManager) {
		this.zoneManager = zoneManager;
	}
	public boolean isActive(){
		if(getStatus().equalsIgnoreCase("Active")) {
			return true;
		} else if(getStatus().equalsIgnoreCase("A")) {
			return true;
		} else {
			return false;
		}
	}
	public boolean isRejected(){
		return getStatus().equalsIgnoreCase("Rejected");
	}
	
	public boolean isPending() {
		return getStatus().equalsIgnoreCase("Pending");
		//return ((isProspect() && vistaReferenceNumber.length()!=0) || getStatus().equals("Pending"));
	}

	public boolean isProspect() {
	    
		if(getVcn()==null || getVcn().trim().length()<2){
	    	return false;
	    }
		if(getVcn().trim().substring(0,1).equals("P")){
	    	return true;
	    }else{
	    	return false;
	    }
	    
		//return getStatus().equalsIgnoreCase("Prospect");
	}
	
	public boolean isNewAccount() {
	    /*
		if(getVcn()==null || getVcn().trim().length()<2){
	    	return false;
	    }
		if(getVcn().trim().substring(0,1).equals("P")){
	    	return true;
	    }else{
	    	return false;
	    }
	    */
		return getVcn().equals("");
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
    /**
     * @return Returns the salesEngineer1Name. Customer Listing only.
     */
    public String getSalesEngineer1Name() {
        return salesEngineer1Name;
    }
    /**
     * @param salesEngineer1Name The salesEngineer1Name to set. Customer Listing only.
     */
    public void setSalesEngineer1Name(String salesEngineer1Name) {
        this.salesEngineer1Name = salesEngineer1Name;
    }
    
    public int getFocusType() {
        return focusType;
    }
    public void setFocusType(int focusType) {
        this.focusType = focusType;
    }
    
    
    public Date getDateAdded() {
        return dateAdded;
    }
    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
    public Date getDateChanged() {
        return dateChanged;
    }
    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }
    
    /**
     * @return Returns the specialProgramIds.
     */
    public ArrayList getSpecialProgramIds() {
        return specialProgramIds;
    }
    /**
     * @param specialProgramIds The specialProgramIds to set.
     */
    public void setSpecialProgramIds(ArrayList specialProgramIds) {
        this.specialProgramIds = specialProgramIds;
    }

	public void setAPContPhoneNumber(String aPContPhoneNumber) {
		APContPhoneNumber = aPContPhoneNumber;
	}


	public String getAPContPhoneNumber() {
		return APContPhoneNumber;
	}


	public void setAPContEmailAddress(String aPContEmailAddress) {
		APContEmailAddress = aPContEmailAddress;
	}


	public String getAPContEmailAddress() {
		return APContEmailAddress;
	}


	public void setAPCont(String aPCont) {
		APCont = aPCont;
	}


	public String getAPCont() {
		return APCont;
	}
}
