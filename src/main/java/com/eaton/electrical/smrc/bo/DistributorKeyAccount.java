
package com.eaton.electrical.smrc.bo;

import java.util.*;

/**
 * @author E0062708
 *
 */
public class DistributorKeyAccount implements java.io.Serializable  {
	
	private String vcn = null;
	private int distributorId = -1;
	private String accountName = null;
	private int potentialEatonDollars=0;
	private ArrayList productIds = null;
	
	private static final long serialVersionUID = 100;

	public DistributorKeyAccount(){
		vcn="";
		accountName="";
		productIds = new ArrayList();		
	}
	
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public int getDistributorId() {
		return distributorId;
	}
	public void setDistributorId(int distributorId) {
		this.distributorId = distributorId;
	}
	public String getVcn() {
		return vcn;
	}
	public void setVcn(String vcn) {
		this.vcn = vcn;
	}
	public int getPotentialEatonDollars() {
		return potentialEatonDollars;
	}
	public void setPotentialEatonDollars(int potentialEatonDollars) {
		this.potentialEatonDollars = potentialEatonDollars;
	}
	public ArrayList getProductIds() {
		return productIds;
	}
	public void setProductIds(ArrayList productIds) {
		this.productIds = productIds;
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

		returnString += "\taccountName = " + this.getAccountName() + "\n";
		returnString += "\tdistributorId = " + this.getDistributorId() + "\n";
		returnString += "\tvcn = " + this.getVcn() + "\n";
		returnString += "\tpotentialEatonDollars = " + this.getPotentialEatonDollars() + "\n";
		returnString += "\tproductIds = " + this.getProductIds() + "\n";

		return returnString;
	}
}
