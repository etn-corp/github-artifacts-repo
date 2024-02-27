/*
 * TMBean.java
 *
 * Created on October 21, 2004, 8:38 AM
 */

package com.eaton.electrical.smrc.bo;

/**
 *
 * @author  Jason Lubbert
 */
public class TMBean implements java.io.Serializable {
    
    int tmid = 0;
    double baselineSales = 0;
    double planSales = 0;
    double growthPayout = 0;
    double growth = 0;
    double maxPayout = 0;
    double growthPercentage = 0;
        
	private static final long serialVersionUID = 100;
    
    /** Creates a new instance of TMBean */
    public TMBean() {
    }
    
    public int getTmid (){
        return tmid;
    }
    public double getGrowthPercentage (){
        return growthPercentage;
    }
    public double getBaselineSales (){
        return baselineSales;
    }
    public double getPlanSales (){
        return planSales;
    }
    public double getGrowthPayout (){
        return growthPayout;
    }
    public double getGrowth(){
        return growth;
    }
    public double getMaxPayout(){
        return maxPayout;
    }
    
    public void setTmid (int tmid){
        this.tmid = tmid;
    }
    public void setGrowthPercentage (double growthPercentage){
        this.growthPercentage = growthPercentage;
    }
    public void setBaselineSales (double baselineSales){
        this.baselineSales = baselineSales;
    }
    public void setPlanSales (double planSales){
        this.planSales = planSales;
    }
    public void setGrowthPayout (double growthPayout){
        this.growthPayout = growthPayout;
    }
    public void setGrowth(double growth){
        this.growth = growth;
    }
    public void setMaxPayout(double maxPayout){
        this.maxPayout = maxPayout;
    }
    
    
}
