
package com.eaton.electrical.smrc.bo;

import java.util.*;

/**	This class contains all of the methods and attributes needed by the Target Account Planner
 *	as needed
 *
 *	@author Carl Abel
 */
public class Product extends Object implements java.io.Serializable {
	private String _id = null;
	private String _desc = null;
	private ArrayList _sublines = null;
	private int _sublineIdx = -1;
	private CustomerProduct product =  null;
	
	private int periodYYYY=0;
	private double totalSales = 0;
	private String totalSalesThruStock = null;
	private String currentPrimaryManufacturer = null;
	private String currentSecondaryManufacturer = null;
	private String currentOtherManufacturer = null;
	private String proposedPrimaryManufacturer = null;
	private String proposedSecondaryManufacturer = null;
	private String proposedOtherManufacturer = null;
	private String spLoadTotal = null;
	private String summaryProductId = null;
	private String grandTotalId1 = null;
	private String grandTotalId2 = null;
	private Division division = null;
	
	private static final long serialVersionUID = 100;

	public Product(){
		_sublines = new ArrayList();
		product = new CustomerProduct();
	
		totalSalesThruStock = "";
		currentPrimaryManufacturer = "";
		currentSecondaryManufacturer = "";
		currentOtherManufacturer = "";
		proposedPrimaryManufacturer = "";
		proposedSecondaryManufacturer = "";
		proposedOtherManufacturer = "";
		spLoadTotal = "";
		summaryProductId = "";
		grandTotalId1 = "";
		grandTotalId2 = "";
		
	}
	
	public void setId(String id) {
		_id = id;
	}
	public void setSubLineIdx() {
		_sublineIdx = -1;
		
	}	
	public void setDescription(String desc) {
		_desc = desc;
	}
	
	public void addSubline(ProductSubline ps) {
		_sublines.add(ps);
	}
	
	public String getId() {
		return _id;
	}
	
	public String getDescription() {
		return _desc;
	}
	
	
	public boolean moreSublines() {
		return (_sublineIdx < (_sublines.size() - 1));
	}
	
	public ProductSubline getNextSubline() {
		if (moreSublines()) {
			_sublineIdx++;
			return (ProductSubline)_sublines.get(_sublineIdx);
		}
		return null;

	}
	
	/**
	 * @param product
	 */
	public void setCustomerProduct(CustomerProduct product) {
		this.product=product;
		
	}
	public CustomerProduct getCustomerProduct() {
		return this.product;
		
	}	
	public String getCurrentOtherManufacturer() {
		return currentOtherManufacturer;
	}
	public void setCurrentOtherManufacturer(String currentOtherManufacturer) {
		this.currentOtherManufacturer = currentOtherManufacturer;
	}
	public String getCurrentPrimaryManufacturer() {
		return currentPrimaryManufacturer;
	}
	public void setCurrentPrimaryManufacturer(String currentPrimaryManufacturer) {
		this.currentPrimaryManufacturer = currentPrimaryManufacturer;
	}
	public String getCurrentSecondaryManufacturer() {
		return currentSecondaryManufacturer;
	}
	public void setCurrentSecondaryManufacturer(
			String currentSecondaryManufacturer) {
		this.currentSecondaryManufacturer = currentSecondaryManufacturer;
	}
	public String getProposedOtherManufacturer() {
		return proposedOtherManufacturer;
	}
	public void setProposedOtherManufacturer(String proposedOtherManufacturer) {
		this.proposedOtherManufacturer = proposedOtherManufacturer;
	}
	public String getProposedPrimaryManufacturer() {
		return proposedPrimaryManufacturer;
	}
	public void setProposedPrimaryManufacturer(
			String proposedPrimaryManufacturer) {
		this.proposedPrimaryManufacturer = proposedPrimaryManufacturer;
	}
	public String getProposedSecondaryManufacturer() {
		return proposedSecondaryManufacturer;
	}
	public void setProposedSecondaryManufacturer(
			String proposedSecondaryManufacturer) {
		this.proposedSecondaryManufacturer = proposedSecondaryManufacturer;
	}
	public double getTotalSales() {
		return totalSales;
	}
	public void setTotalSales(double totalSales) {
		this.totalSales = totalSales;
	}
	public String getTotalSalesThruStock() {
		return totalSalesThruStock;
	}
	public void setTotalSalesThruStock(String totalSalesThruStock) {
		this.totalSalesThruStock = totalSalesThruStock;
	}
	public int getPeriodYYYY() {
		return periodYYYY;
	}
	public void setPeriodYYYY(int periodYYYY) {
		this.periodYYYY = periodYYYY;
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

		returnString += "\tid = " + this.getId() + "\n";
		returnString += "\tdescription = " + this.getDescription() + "\n";
		returnString += "\tnextSubline = " + this.getNextSubline() + "\n";
		returnString += "\tcustomerProduct = " + this.getCustomerProduct() + "\n";
		returnString += "\tcurrentOtherManufacturer = " + this.getCurrentOtherManufacturer() + "\n";
		returnString += "\tcurrentPrimaryManufacturer = " + this.getCurrentPrimaryManufacturer() + "\n";
		returnString += "\tcurrentSecondaryManufacturer = " + this.getCurrentSecondaryManufacturer() + "\n";
		returnString += "\tproposedOtherManufacturer = " + this.getProposedOtherManufacturer() + "\n";
		returnString += "\tproposedPrimaryManufacturer = " + this.getProposedPrimaryManufacturer() + "\n";
		returnString += "\tproposedSecondaryManufacturer = " + this.getProposedSecondaryManufacturer() + "\n";
		returnString += "\ttotalSales = " + this.getTotalSales() + "\n";
		returnString += "\ttotalSalesThruStock = " + this.getTotalSalesThruStock() + "\n";
		returnString += "\tperiodYYYY = " + this.getPeriodYYYY() + "\n";
		returnString += "\tsummaryProductId = " + this.getSummaryProductId() + "\n";
		returnString += "\tgrandTotalId1 = " + this.getGrandTotalId1() + "\n";
		returnString += "\tgrandTotalId2 = " + this.getGrandTotalId2() + "\n";

		return returnString;
	}
    /**
     * @return Returns the spLoadTotal.
     */
    public String getSpLoadTotal() {
        return spLoadTotal;
    }
    /**
     * @param spLoadTotal The spLoadTotal to set.
     */
    public void setSpLoadTotal(String spLoadTotal) {
        this.spLoadTotal = spLoadTotal;
    }
    /**
     * @return Returns the summaryProductId.
     */
    public String getSummaryProductId() {
        return summaryProductId;
    }
    /**
     * @param summaryProductId The summaryProductId to set.
     */
    public void setSummaryProductId(String summaryProductId) {
        this.summaryProductId = summaryProductId;
    }
    /**
     * @return Returns the grandTotalId1.
     */
    public String getGrandTotalId1() {
        return grandTotalId1;
    }
    /**
     * @param grandTotalId1 The grandTotalId1 to set.
     */
    public void setGrandTotalId1(String grandTotalId1) {
        this.grandTotalId1 = grandTotalId1;
    }
    /**
     * @return Returns the grandTotalId2.
     */
    public String getGrandTotalId2() {
        return grandTotalId2;
    }
    /**
     * @param grandTotalId2 The grandTotalId2 to set.
     */
    public void setGrandTotalId2(String grandTotalId2) {
        this.grandTotalId2 = grandTotalId2;
    }
    /**
     * @return Returns the division.
     */
    public Division getDivision() {
        return division;
    }
    /**
     * @param division The division to set.
     */
    public void setDivision(Division division) {
        this.division = division;
    }
}
