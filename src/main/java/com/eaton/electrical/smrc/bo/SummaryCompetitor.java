package com.eaton.electrical.smrc.bo;

/**
 * @author E0062708
 *
 */
public class SummaryCompetitor extends Vendor implements java.io.Serializable {
	double year1Sales = 0;
	double year3Sales = 0;
	private static final long serialVersionUID = 100;

	public double getYear1Sales() {
		return year1Sales;
	}
	public void setYear1Sales(double year1Sales) {
		this.year1Sales = year1Sales;
	}
	public double getYear3Sales() {
		return year3Sales;
	}
	public void setYear3Sales(double year3Sales) {
		this.year3Sales = year3Sales;
	}
}
