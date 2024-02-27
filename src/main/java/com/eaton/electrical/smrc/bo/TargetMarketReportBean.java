/*
 * TargetMarketReportBean.java
 *
 * Created on October 20, 2004, 9:41 AM
 */

package com.eaton.electrical.smrc.bo;

/**
 *
 * @author  Jason Lubbert
 */
public class TargetMarketReportBean implements java.io.Serializable {
    
    String description = null;
    String id = null;
    String subDescription = null;
    double salesBaseline = 0;
    double salesInPlan = 0;
    double percentageGrowth = 0;
    double growthPayout = 0;
//    double bidManRebate = 0;
//    double currentPayout = 0;
    double forecastMaxPayout = 0;
    // Only used when sorting numeric fields, default sort is description
    double sortField = 0;
    String status = null;
    
	private static final long serialVersionUID = 100;
   
    /** Creates a new instance of TargetMarketReportBean */
    public TargetMarketReportBean() {
        description = "";
        subDescription = "";
        id = "";
        status = "";
    }
    
    public void setId (String id){
        this.id = id;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setSubDescription (String subDescription){
        this.subDescription = subDescription;
    }
    public void setSalesBaseline (double salesBaseline){
        this.salesBaseline = salesBaseline;
    }
    public void setSalesInPlan (double salesInPlan){
        this.salesInPlan = salesInPlan;
    }
    public void setPercentageGrowth (double percentageGrowth){
        this.percentageGrowth = percentageGrowth;
    }
    public void setGrowthPayout (double growthPayout){
        this.growthPayout = growthPayout;
    }
    /*
    public void setBidManRebate (double bidManRebate){
        this.bidManRebate = bidManRebate;
    }
    public void setCurrentPayout (double currentPayout){
        this.currentPayout = currentPayout;
    }
     */
    public void setForecastMaxPayout (double forecastMaxPayout){
        this.forecastMaxPayout = forecastMaxPayout;
    }
    public void setSortField (double sortField){
        this.sortField = sortField;
    }
    
    public String getId(){
        return id;
    }
    public String getDescription(){
        return description;
    }
    public String getSubDescription(){
        return subDescription;
    }
    public double getSalesBaseline (){
        return salesBaseline;
    }
    public double getSalesInPlan (){
        return salesInPlan;
    }
    public double getPercentageGrowth (){
        percentageGrowth = ((salesInPlan - salesBaseline) / salesBaseline);
        return percentageGrowth; 
    }
    public double getGrowthPayout (){
        return growthPayout;
    }
    /*
    public double getBidManRebate (){
        return bidManRebate;
    }
    public double getCurrentPayout (){
        currentPayout = (growthPayout + bidManRebate);
        return currentPayout;
    }
     */
    public double getForecastMaxPayout (){
        return forecastMaxPayout;
    }
    public double getSortField (){
        return sortField;
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

		returnString += "\tid = " + this.getId();
		returnString += "\tdescription = " + this.getDescription();
		returnString += "\tsubDescription = " + this.getSubDescription();
		returnString += "\tsalesBaseline  = " + this.getSalesBaseline ();
		returnString += "\tsalesInPlan  = " + this.getSalesInPlan ();
		returnString += "\tpercentageGrowth  = " + this.getPercentageGrowth ();
		returnString += "\tgrowthPayout  = " + this.getGrowthPayout ();
		returnString += "\tforecastMaxPayout  = " + this.getForecastMaxPayout ();
		returnString += "\tsortField  = " + this.getSortField ();

		return returnString;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
