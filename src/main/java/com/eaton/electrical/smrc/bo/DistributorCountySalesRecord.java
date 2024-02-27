
package com.eaton.electrical.smrc.bo;

/**
 * @author E0062708
 *
 */
public class DistributorCountySalesRecord implements java.io.Serializable {
	String percentage = null;
	String county = null;
	
	private static final long serialVersionUID = 100;

	public DistributorCountySalesRecord() {
		this.percentage = "";
		this.county = "";
	}
	public DistributorCountySalesRecord(String county,String percentage) {
		this.percentage = percentage;
		this.county = county;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
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

		returnString += "\tcounty = " + this.getCounty() + "\n";
		returnString += "\tpercentage = " + this.getPercentage() + "\n";

		return returnString;
	}
}
