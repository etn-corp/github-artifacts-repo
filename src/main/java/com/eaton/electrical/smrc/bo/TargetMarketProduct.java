/*
 * TargetMarketProduct.java
 *
 * Created on October 8, 2004, 9:42 AM
 */

package com.eaton.electrical.smrc.bo;

import com.eaton.electrical.smrc.util.*;

/**
 *
 * @author  Jason Lubbert
 */
public class TargetMarketProduct implements java.io.Serializable {
    
    int targetMarketPlanId = 0;
    String productId = null;
    int periodYYYY = 0;
    double salesObjective = 0;
    double incrementalGrowth = 0;
    Product product = null;
    String checked = null;
    double ytdSales = 0;
    double prevSales = 0;
    double baselineSales = 0;
    double salesInPlan = 0;
    double totalGrowth = 0;
    double growthPercentage = 0;
    double payout = 0;
    double prevPlanToDateSales = 0;
    
	private static final long serialVersionUID = 100;
 
    /** Creates a new instance of TargetMarketProduct */
    public TargetMarketProduct() {
        productId = "";
    }
    
    public int getTargetMarketPlanId () {
        return targetMarketPlanId;
    }
    public String getProductId () {
        return productId;
    }
    public int getPeriodYYYY (){
        return periodYYYY;
    }
    public double getSalesObjective () {
        return salesObjective;
    }
    public double getIncrementalGrowth (){
        return incrementalGrowth;
    }
    public Product getProduct(){
        return product;
    }
    public String getChecked(){
        return StringManipulation.noNull(checked);
    }
    public double getYTDSales(){
        return ytdSales;
    }
    public double getPrevSales(){
        return prevSales;
    }
    public double getBaselineSales(){
        return baselineSales;
    }
    public double getSalesInPlan(){
        return salesInPlan;
    }
    public double getTotalGrowth(){
        return totalGrowth;
    }
    public double getGrowthPercentage(){
        return growthPercentage;
    }
    public double getPayout(){
        return payout;
    }
        
    
    public void setTargetMarketPlanId (int targetMarketPlanId) {
        this.targetMarketPlanId = targetMarketPlanId;
    }
    public void setProductId (String productId) {
        this.productId = productId;
    }
    public void setPeriodYYYY (int periodYYYY){
        this.periodYYYY = periodYYYY;
    }
    public void setSalesObjective (double salesObjective) {
        this.salesObjective = salesObjective;
    }
    public void setIncrementalGrowth (double incrementalGrowth){
        this.incrementalGrowth = incrementalGrowth;
    }
    public void setProduct(Product product){
        this.product = product;
    }
    public void setChecked(String checked){
        this.checked = checked;
    }
    public void setYTDSales(double ytdSales){
        this.ytdSales = ytdSales;
    }
    public void setPrevSales(double prevSales){
        this.prevSales = prevSales;
    }
    public void setBaselineSales(double baselineSales){
        this.baselineSales = baselineSales;
    }
    public void setSalesInPlan(double salesInPlan){
        this.salesInPlan = salesInPlan;
    }
    public void setTotalGrowth(double totalGrowth){
        this.totalGrowth = totalGrowth;
    }
    public void setGrowthPercentage (double growthPercentage){
        this.growthPercentage = growthPercentage;
    }
    public void setPayout(double payout){
        this.payout = payout;
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

		returnString += "\ttargetMarketPlanId  = " + this.getTargetMarketPlanId ();
		returnString += "\tproductId  = " + this.getProductId ();
		returnString += "\tperiodYYYY  = " + this.getPeriodYYYY ();
		returnString += "\tsalesObjective  = " + this.getSalesObjective ();
		returnString += "\tincrementalGrowth  = " + this.getIncrementalGrowth ();
		returnString += "\tproduct = " + this.getProduct();
		returnString += "\tchecked = " + this.getChecked();
		returnString += "\tyTDSales = " + this.getYTDSales();
		returnString += "\tprevSales = " + this.getPrevSales();
		returnString += "\tbaselineSales = " + this.getBaselineSales();
		returnString += "\tsalesInPlan = " + this.getSalesInPlan();
		returnString += "\ttotalGrowth = " + this.getTotalGrowth();
		returnString += "\tgrowthPercentage = " + this.getGrowthPercentage();
		returnString += "\tpayout = " + this.getPayout();

		return returnString;
	}

	public double getPrevPlanToDateSales() {
		return prevPlanToDateSales;
	}

	public void setPrevPlanToDateSales(double prevPlanToDateSales) {
		this.prevPlanToDateSales = prevPlanToDateSales;
	}
}
