/*
 * DollarTypeRptBean.java
 *
 * Created on July 14, 2004, 1:18 PM
 */

package com.eaton.electrical.smrc.bo;

/**
 *
 * @author  Jason Lubbert
 *
 *
 */
public class DollarTypeRptBean extends Object implements java.io.Serializable  {
    
    // valid dollar types: credit, end, charge
    String dollarType = null;
    boolean monthly = false;
    boolean YTDmonthly = false;
    boolean prevYTDmonthly = false;
    boolean prevYearTotal = false;
    boolean prevYearMonthly = false;
    boolean sales = false;
    boolean orders = false;
    
	private static final long serialVersionUID = 100;

	/** Creates a new instance of DollarTypeRptBean with dollarType */
    public DollarTypeRptBean(String dollarType) {
        this.dollarType = dollarType;
    }
    
    /** Creates a new instance of DollarTypeRptBean */
    public DollarTypeRptBean() {
    }
    
    public void setDollarType (String dollarType){
        this.dollarType = dollarType;
    }
    
    public void setMonthly (){
        monthly = true;
    }
    
    public void setYTDmonthly (){
        YTDmonthly = true;
    }
    
    public void setPrevYTDmonthly (){
        prevYTDmonthly = true;
    }
    
    public void setPrevYearTotal (){
        prevYearTotal = true;
    }
    
    public void setPrevYearMonthly (){
        prevYearMonthly = true;
    }
    
    public void setSales(){
        sales = true;
    }
    
    public void setOrders(){
        orders = true;
    }
    
    public boolean useMonthly (){
        return monthly;
    }
    
    public boolean useYTDmonthly (){
        return YTDmonthly;
    }
    
    public boolean usePrevYTDmonthly(){
        return prevYTDmonthly;
    }
    
    public boolean usePrevYearTotal(){
        return prevYearTotal;
    }
    
    public boolean usePrevYearMonthly(){
        return prevYearMonthly;
    }
    
    public boolean useSales (){
        return sales;
    }
    
    public boolean useOrders (){
        return orders;
    }
    
    public String getDollarType(){
        return dollarType;
    }
}
