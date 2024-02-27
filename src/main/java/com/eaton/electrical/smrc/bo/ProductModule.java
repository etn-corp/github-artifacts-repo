
package com.eaton.electrical.smrc.bo;

import java.util.*;

/**
 * @author E0062708
 *
 */
public class ProductModule implements java.io.Serializable {
	
	private String productId = null;
	private String productDescription = null;
	private boolean summaryLine = false;
	private ArrayList loadModules = null;
	
	private static final long serialVersionUID = 100;

	public ProductModule(){
		setProductId("");
		setProductDescription("");
		loadModules = new ArrayList();
	}
	
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public boolean isSummaryLine() {
		return summaryLine;
	}
	public void setSummaryLine(boolean summaryLine) {
		this.summaryLine = summaryLine;
	}
	public ArrayList getLoadModules() {
		return loadModules;
	}
	public void setLoadModules(ArrayList loadModules) {
		this.loadModules = loadModules;
	}
	
	public boolean equals(Object obj) {
		if (this == null || obj == null) {
			return false;
		}

		return super.equals(obj);
	}
	
	public String toString() {
		String returnString = "";

		returnString += "\tproductId  = " + this.getProductId () + "\n";
		returnString += "\tproductDescription  = " + this.getProductDescription () + "\n";
		returnString += "\tsummaryLine? = " + this.isSummaryLine() + "\n";
		returnString += "\tloadModules size  = " + this.getLoadModules().size () + "\n";

		return returnString;
	}
	
	
}
