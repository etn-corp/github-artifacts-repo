/*
 * TargetMarket.java
 *
 * Created on October 6, 2004, 10:08 AM
 */

package com.eaton.electrical.smrc.bo;

import java.util.*;

/**
 *
 * @author  Jason Lubbert
 */
public class TargetMarket implements java.io.Serializable {
    private int targetMarketPlanId = 0;
    private String planDescription = null;
    private int contactId = 0;
    private String competitorConvert = null;
    private int competitorId = 0;
    private int commitmentLevel = 0;
    private String businessObjective = null;
    private java.util.Date startDate = null;
    private java.util.Date endDate = null;
    private int salesObjective = 0;
    private int incrementalGrowth = 0;
    private int salesTrackingTypeId = 0;
    private java.util.Date baselineStartDate = null;
    private java.util.Date baselineEndDate = null;
    private String salesObjectiveOtherNotes = null;
    private String salesPlanUsed = null;
    private double percentGrowth1 = 0;
    private double percentPayout1 = 0;
    private double percentGrowth2 = 0;
    private double percentPayout2 = 0;
    private double percentGrowth3 = 0;
    private double percentPayout3 = 0;
    private double percentGrowth4 = 0;
    private double percentPayout4 = 0;
//    private String includeBidmanRebate = null;
    private String userAdded = null;
    private String userChanged = null;
    private java.util.Date dateAdded = null;
    private java.util.Date dateChanged = null;
    private double maximumPayout = 0;
    private String status = null;
    private String segmentOtherNotes = null;
    private CodeType salesTrackingCodeType = null;
    private boolean pendingResubmission = false;

    //  This is an ArrayList of account ids
    private ArrayList endCustomers = null;
    //  This is an ArrayList of account ids
    private ArrayList planAccounts = null;
    // This is an ArrayList of Contact objects
    private ArrayList planContacts = null;
    // This is an ArrayList of TargetMarketProduct objects
    private ArrayList planProducts = null;
    // This is an ArrayList of segment ids in Integer Objects
    private ArrayList planSegments = null;
    
	private static final long serialVersionUID = 100;

	/** Creates a new instance of TargetMarket */
    public TargetMarket() {
        planDescription = "";
        competitorConvert = "";
        businessObjective = "";
        startDate = new java.util.Date();
        endDate = new java.util.Date();
        baselineStartDate = new java.util.Date();
        baselineEndDate = new java.util.Date();
        salesObjectiveOtherNotes = "";
        salesPlanUsed = "";
  //      includeBidmanRebate = "";
        userAdded = "";
        userChanged = "";
        dateAdded = new java.util.Date();
        dateChanged = new java.util.Date();
        endCustomers = new ArrayList();
        planAccounts = new ArrayList();
        planContacts = new ArrayList();
        planProducts = new ArrayList();
        planSegments = new ArrayList();
        status = "";
        segmentOtherNotes = "";
        salesTrackingCodeType = new CodeType();
    }
    
    public int getTargetMarketPlanId(){
        return targetMarketPlanId;
    }
    public String getPlanDescription (){
        return planDescription;
    }
    public int getContactId (){
        return contactId;
    }
    public String getCompetitorConvert(){
        return competitorConvert;
    }
    public int getCompetitorId () {
        return competitorId;
    }
    public int getCommitmentLevel() {
        return commitmentLevel;
    }
    public String getBusinessObjective(){
        return businessObjective;
    }
    public java.util.Date getStartDate() {
        return startDate;
    }
    public java.util.Date getEndDate(){
        return endDate;
    }
    public int getSalesObjective () {
        return salesObjective;
    }
    public int getIncrementalGrowth() {
        return incrementalGrowth;
    }
    public int getSalesTrackingTypeId (){
        return salesTrackingTypeId;
    }
    public java.util.Date getBaselineStartDate(){
        return baselineStartDate;
    }
    public java.util.Date getBaselineEndDate(){
        return baselineEndDate;
    }
    public String getSalesObjectiveOtherNotes(){
        return salesObjectiveOtherNotes;
    }
    public String getSalesPlanUsed(){
        return salesPlanUsed;
    }
    public double getPercentGrowth1 (){
        return percentGrowth1;
    }
    public double getPercentPayout1 (){
        return percentPayout1;
    }
    public double getPercentGrowth2 (){
        return percentGrowth2;
    }
    public double getPercentPayout2 (){
        return percentPayout2;
    }
    public double getPercentGrowth3 (){
        return percentGrowth3;
    }
    public double getPercentPayout3 (){
        return percentPayout3;
    }
    public double getPercentGrowth4 (){
        return percentGrowth4;
    }
    public double getPercentPayout4 (){
        return percentPayout4;
    }
    /*
    public String getIncludeBidmanRebate (){
        return includeBidmanRebate;
    }
     */
    public String getUserAdded (){
        return userAdded;
    }
    public String getUserChanged (){
        return userChanged;
    }
    public java.util.Date getDateAdded (){
        return dateAdded;
    }
    public java.util.Date getDateChanged (){
        return dateChanged;
    }
    public double getMaximumPayout (){
        return maximumPayout;
    }
    public String getStatus(){
        return status;
    }
    public String getSegmentOtherNotes(){
        return segmentOtherNotes;
    }
    
    public ArrayList getEndCustomers (){
        return endCustomers;
    }
    public ArrayList getPlanAccounts (){
        return planAccounts;
    }
    public ArrayList getPlanContacts (){
        return planContacts;
    }
    public ArrayList getPlanProducts (){
        return planProducts;
    }
    public ArrayList getPlanSegments (){
        return planSegments;
    }
    
    public CodeType getSalesTrackingCodeType(){
        return salesTrackingCodeType;
    }
    
    public void setTargetMarketPlanId(int targetMarketPlanId){
        this.targetMarketPlanId = targetMarketPlanId;
    }
    public void setPlanDescription (String planDescription){
        this.planDescription = planDescription;
    }
    public void setContactId (int contactId){
        this.contactId = contactId;
    }
    public void setCompetitorConvert(String competitorConvert){
        this.competitorConvert = competitorConvert;
    }
    public void setCompetitorId (int competitorId) {
        this.competitorId = competitorId;
    }
    public void setCommitmentLevel(int commitmentLevel) {
        this.commitmentLevel = commitmentLevel;
    }
    public void setBusinessObjective(String businessObjective){
        this.businessObjective = businessObjective;
    }
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(java.util.Date endDate){
        this.endDate = endDate;
    }
    public void setSalesObjective (int salesObjective) {
        this.salesObjective = salesObjective;
    }
    public void setIncrementalGrowth(int incrementalGrowth) {
        this.incrementalGrowth = incrementalGrowth;
    }
    public void setSalesTrackingTypeId (int salesTrackingTypeId){
        this.salesTrackingTypeId = salesTrackingTypeId;
    }
    public void setBaselineStartDate(java.util.Date baselineStartDate){
        this.baselineStartDate = baselineStartDate;
    }
    public void setBaselineEndDate(java.util.Date baselineEndDate){
        this.baselineEndDate = baselineEndDate;
    }
    public void setSalesObjectiveOtherNotes(String salesObjectiveOtherNotes){
        this.salesObjectiveOtherNotes = salesObjectiveOtherNotes;
    }
    public void setSalesPlanUsed(String salesPlanUsed){
        this.salesPlanUsed = salesPlanUsed;
    }
    public void setPercentGrowth1 (double percentGrowth1){
        this.percentGrowth1 = percentGrowth1;
    }
    public void setPercentPayout1 (double percentPayout1){
        this.percentPayout1 = percentPayout1;
    }
    public void setPercentGrowth2 (double percentGrowth2){
        this.percentGrowth2 = percentGrowth2;
    }
    public void setPercentPayout2 (double percentPayout2){
        this.percentPayout2 = percentPayout2;
    }
    public void setPercentGrowth3 (double percentGrowth3){
        this.percentGrowth3 = percentGrowth3;
    }
    public void setPercentPayout3 (double percentPayout3){
        this.percentPayout3 = percentPayout3;
    }
    public void setPercentGrowth4 (double percentGrowth4){
        this.percentGrowth4 = percentGrowth4;
    }
    public void setPercentPayout4 (double percentPayout4){
        this.percentPayout4 = percentPayout4;
    }
    /*
    public void setIncludeBidmanRebate (String includeBidmanRebate){
        this.includeBidmanRebate = includeBidmanRebate;
    }
     */
    public void setUserAdded (String userAdded){
        this.userAdded = userAdded;
    }
    public void setUserChanged (String userChanged){
        this.userChanged = userChanged;
    }
    public void setDateAdded (java.util.Date dateAdded){
        this.dateAdded = dateAdded;
    }
    public void setDateChanged (java.util.Date dateChanged){
        this.dateChanged = dateChanged;
    }
    public void setMaximumPayout (double maximumPayout){
        this.maximumPayout = maximumPayout;
    }
    public void setStatus (String status){
        this.status = status;
    }
    public void setSegmentOtherNotes (String segmentOtherNotes){
        this.segmentOtherNotes = segmentOtherNotes;
    }
    
    public void setEndCustomers (ArrayList endCustomers){
        this.endCustomers = endCustomers;
    }
    public void setPlanAccounts (ArrayList planAccounts){
        this.planAccounts = planAccounts;
    }
    public void setPlanContacts (ArrayList planContacts){
        this.planContacts = planContacts;
    }
    public void setPlanProducts (ArrayList planProducts){
        this.planProducts = planProducts;
    }
    public void setPlanSegments (ArrayList planSegments){
        this.planSegments = planSegments;
    }
    public void setSalesTrackingCodeType(CodeType salesTrackingCodeType){
        this.salesTrackingCodeType = salesTrackingCodeType;
    }
    
    public boolean isActive(){
        if (status.equalsIgnoreCase("A")){
            return true;
        }
        return false;

    }
    
    public boolean isDMApproved(){
        if (status.equalsIgnoreCase("D")){
            return true;
        }
        return false;

    }
    
    public boolean isSaved(){
        if (status.equalsIgnoreCase("S")){
            return true;
        }
        return false;
    }
    
    public boolean isUnsaved(){
        if (status.equalsIgnoreCase("U")){
            return true;
        }
        return false;

    }
    
    public boolean isRejected(){
        if (status.equalsIgnoreCase("R")){
            return true;
        }
        return false;
    }
    
    public boolean isSubmitted(){
        if (status.equalsIgnoreCase("B")){
            return true;
        }
        return false;
    }
    
    public boolean isPurged(){
        if (status.equalsIgnoreCase("P")){
            return true;
        }
        return false;
    }
  
    
/*    public boolean isChargeToSales(){
        
        if (salesTrackingCodeType.getDescription().equalsIgnoreCase("Charge to Sales (sales to the distributor)")){
            return true;
        }
        return false;
    }
*/    
/*   
    public boolean isIntoStockSales(){
        if (salesTrackingCodeType.getDescription().equalsIgnoreCase("Charge to Into-Stock sales only")){
            return true;
        }
        return false;
    }
*/    
    public boolean isTAPDollars(){
        
        if (salesTrackingCodeType.getDescription().equalsIgnoreCase("TAP Dollars")){
            return true;
        }
        return false;
    }

    
    public boolean isEndMarketSales(){
        
        if (salesTrackingCodeType.getDescription().equalsIgnoreCase("End Market Sales (sales from distributor to customer)")){
            return true;
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

		returnString += "\ttargetMarketPlanId = " + this.getTargetMarketPlanId();
		returnString += "\tplanDescription  = " + this.getPlanDescription ();
		returnString += "\tcontactId  = " + this.getContactId ();
		returnString += "\tcompetitorConvert = " + this.getCompetitorConvert();
		returnString += "\tcompetitorId  = " + this.getCompetitorId ();
		returnString += "\tcommitmentLevel = " + this.getCommitmentLevel();
		returnString += "\tbusinessObjective = " + this.getBusinessObjective();
		returnString += "\tstartDate = " + this.getStartDate();
		returnString += "\tendDate = " + this.getEndDate();
		returnString += "\tsalesObjective  = " + this.getSalesObjective ();
		returnString += "\tincrementalGrowth = " + this.getIncrementalGrowth();
		returnString += "\tsalesTrackingTypeId  = " + this.getSalesTrackingTypeId ();
		returnString += "\tbaselineStartDate = " + this.getBaselineStartDate();
		returnString += "\tbaselineEndDate = " + this.getBaselineEndDate();
		returnString += "\tsalesObjectiveOtherNotes = " + this.getSalesObjectiveOtherNotes();
		returnString += "\tsalesPlanUsed = " + this.getSalesPlanUsed();
		returnString += "\tpercentGrowth1  = " + this.getPercentGrowth1 ();
		returnString += "\tpercentPayout1  = " + this.getPercentPayout1 ();
		returnString += "\tpercentGrowth2  = " + this.getPercentGrowth2 ();
		returnString += "\tpercentPayout2  = " + this.getPercentPayout2 ();
		returnString += "\tpercentGrowth3  = " + this.getPercentGrowth3 ();
		returnString += "\tpercentPayout3  = " + this.getPercentPayout3 ();
		returnString += "\tpercentGrowth4  = " + this.getPercentGrowth4 ();
		returnString += "\tpercentPayout4  = " + this.getPercentPayout4 ();
		returnString += "\tuserAdded  = " + this.getUserAdded ();
		returnString += "\tuserChanged  = " + this.getUserChanged ();
		returnString += "\tdateAdded  = " + this.getDateAdded ();
		returnString += "\tdateChanged  = " + this.getDateChanged ();
		returnString += "\tmaximumPayout  = " + this.getMaximumPayout ();
		returnString += "\tstatus = " + this.getStatus();
		returnString += "\tsegmentOtherNotes = " + this.getSegmentOtherNotes();
		returnString += "\tendCustomers  = " + this.getEndCustomers ();
		returnString += "\tplanAccounts  = " + this.getPlanAccounts ();
		returnString += "\tplanContacts  = " + this.getPlanContacts ();
		returnString += "\tplanProducts  = " + this.getPlanProducts ();
		returnString += "\tplanSegments  = " + this.getPlanSegments ();
		returnString += "\tsalesTrackingCodeType = " + this.getSalesTrackingCodeType();

		return returnString;
	}

	public boolean isPendingResubmission() {
		return pendingResubmission;
	}

	public void setPendingResubmission(boolean pendingResubmission) {
		this.pendingResubmission = pendingResubmission;
	}
}
