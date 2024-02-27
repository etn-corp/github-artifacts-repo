/*
 * DistrictHomePageReportBean.java
 *
 * Created on October 26, 2004, 9:42 AM
 */

package com.eaton.electrical.smrc.bo;

//import java.util.*;


/**
 *
 * @author  Jason Lubbert
 */
public class DistrictHomePageReportBean implements java.io.Serializable {
    
    String description = null;
    String id = null;
    double currYTDGoal = 0;
    double currYTDTotal = 0;
    double ytdDifference = 0;
    double monthlyGoal = 0;
    double MTDGoal = 0;
    double monthlyTotal = 0;
    double monthlyDifference = 0;
   
    
	private static final long serialVersionUID = 100;

	/** Creates a new instance of DistrictHomePageReportBean */
    public DistrictHomePageReportBean() {
        description = "";
        id = "";
    }
   
    public String getDescription(){
        return description;
    }
    public String getId (){
        return id;
    }
    public double getCurrYTDGoal(){
        return currYTDGoal;
    }
    public double getCurrYTDTotal (){
        return currYTDTotal;
    }
    public double getYTDDifference(){
        // Formatting in jsp, no need to multiply by 100
        if (currYTDGoal != 0){
            ytdDifference = ((currYTDTotal - currYTDGoal) / currYTDGoal);
        } else {
            ytdDifference = 1;
        }
        return ytdDifference;
    }
    public double getMonthlyGoal (){
        return monthlyGoal;
    }
    
    public double getMTDGoal(){
        return MTDGoal;
    }
    public double getMonthlyTotal (){
        return monthlyTotal;
    }
    public double getMonthlyDifference (){
        // Formatting in jsp, no need to multiply by 100
        if (MTDGoal != 0){
            monthlyDifference = ((monthlyTotal - MTDGoal) / MTDGoal);
        } else {
            monthlyDifference = 1;
        }
        return monthlyDifference;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    public void setId (String id){
        this.id = id;
    }
    public void setCurrYTDGoal(double currYTDGoal){
        this.currYTDGoal = currYTDGoal;
    }
    public void setCurrYTDTotal (double currYTDTotal){
        this.currYTDTotal = currYTDTotal;
    }
    public void setYTDDifference(double ytdDifference){
        this.ytdDifference = ytdDifference;
    }
    public void setMonthlyGoal (double monthlyGoal){
        this.monthlyGoal = monthlyGoal;
    }
    public void setMTDGoal (double MTDGoal){
        this.MTDGoal = MTDGoal;
    }
    public void setMonthlyTotal (double monthlyTotal){
        this.monthlyTotal = monthlyTotal;
    }
    public void setMonthlyDifference (double monthlyDifference){
        this.monthlyDifference = monthlyDifference;
    }
    
    
}
