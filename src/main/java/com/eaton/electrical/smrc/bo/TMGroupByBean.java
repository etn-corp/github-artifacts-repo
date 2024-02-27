/*
 * TMProductBean.java
 *
 * Created on October 21, 2004, 11:15 AM
 */

package com.eaton.electrical.smrc.bo;
    

/**
 *
 * @author  Jason Lubbert
 */
public class TMGroupByBean implements java.io.Serializable {
    
    int tmid = 0;
    String productId = null;
    int divisionId = 0;
    double baselineSales = 0;
    double planSales = 0;
    double growthPayout = 0;
    double forecastMaxPayout = 0;
    double baselinePercentage = 0;
    String groupingId = null;
    String subGroupingId = null;
    
	private static final long serialVersionUID = 100;

	/** Creates a new instance of TMProductBean */
    public TMGroupByBean() {
        productId = "";
        groupingId = "";
        subGroupingId = "";
    }
    
    public int getTmid(){
        return tmid;
    }
    public String getProductId(){
        return productId;
    }
    public int getDivisionId(){
        return divisionId;
    }
    public double getBaselineSales(){
        return baselineSales;
    }
    public double getPlanSales(){
        return planSales;
    }
    public double getGrowthPayout(){
        return growthPayout;
    }
    public double getForecastMaxPayout(){
        return forecastMaxPayout;
    }
    public double getBaselinePercentage(){
        return baselinePercentage;
    }
    public String getGroupingId(){
        return groupingId;
    }
    public String getSubGroupingId(){
        return subGroupingId;
    }
    
    public void setTmid(int tmid){
        this.tmid = tmid;
    }
    public void setProductId(String productId){
        this.productId = productId;
    }
    public void setDivisionId(int divisionId){
        this.divisionId = divisionId;
    }
    public void setBaselineSales(double baselineSales){
        this.baselineSales = baselineSales;
    }
    public void setPlanSales(double planSales){
        this.planSales = planSales;
    }
    public void setGrowthPayout(double growthPayout){
        this.growthPayout = growthPayout;
    }
    public void setForecastMaxPayout(double forecastMaxPayout){
        this.forecastMaxPayout = forecastMaxPayout;
    }
    public void setBaselinePercentage(double baselinePercentage){
        this.baselinePercentage = baselinePercentage;
    }
    public void setGroupingId (String groupingId){
        this.groupingId = groupingId;
    }
    public void setSubGroupingId (String subGroupingId){
        this.subGroupingId = subGroupingId;
    }
    
    
}
