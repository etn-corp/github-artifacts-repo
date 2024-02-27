/*
 * CSFReportBean.java
 *
 * Created on October 30, 2004, 3:46 PM
 */

package com.eaton.electrical.smrc.bo;

/**
 *
 * @author  Jason Lubbert
 */
public class CSFReportBean implements java.io.Serializable {
    
    String id = null;
    double ytdSales = 0;
    double prevYTDSales = 0;
    double growthPercentage = 0;
    
	private static final long serialVersionUID = 100;

	/** Creates a new instance of CSFReportBean */
    public CSFReportBean() {
        id = "";
    }
    
    public String getId (){
        return id;
    }
    public double getYTDSales(){
        return ytdSales;
    }
    public double getPrevYTDSales(){
        return prevYTDSales;
    }
    public double getGrowthPercentage(){
        if (prevYTDSales > 0){
            growthPercentage = ((ytdSales - prevYTDSales) / prevYTDSales);
        } else {
            growthPercentage = 99999;
        }
        return growthPercentage;
    }
    public void setId (String id){
        this.id = id;
    }
    public void setYTDSales(double ytdSales){
        this.ytdSales = ytdSales;
    }
    public void setPrevYTDSales(double prevYTDSales){
        this.prevYTDSales = prevYTDSales;
    }
    
}
