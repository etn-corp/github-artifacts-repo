
// imports only include specific classes needed to improve performance
package com.eaton.electrical.smrc.bo;

import java.math.*;
import java.text.*;
import java.util.*;

public class BidProduct {
	private String _prod = "";
	private String _neg = "";
	private BigDecimal _bidDol = new BigDecimal(0);
	private BigDecimal _ordDol = new BigDecimal(0);

	private static final long serialVersionUID = 100;

	public void setProductId(String id) {
		_prod = id;
	}

	public void setNegNum(String neg) {
		_neg = neg;
	}

	public void setBidDollars(double dol) {
		_bidDol = new BigDecimal(dol);
	}

	public void setBidDollars(BigDecimal dol) {
		// creating a new instance instead of just assigning reference
		// so any math done to BigDecimal being passed in does not
		// affect this one
		_bidDol = new BigDecimal(dol.doubleValue());
	}

	public void setOrderDollars(double dol) {
		_ordDol = new BigDecimal(dol);
	}

	public void setOrderDollars(BigDecimal dol) {
		// creating a new instance instead of just assigning reference
		// so any math done to BigDecimal being passed in does not
		// affect this one
		_ordDol = new BigDecimal(dol.doubleValue());
	}

	public String getProductId() {
		return _prod;
	}

	public String getNegNum() {
		return _neg;
	}

	public BigDecimal getBidDollarsAsBigDecimal() {
		return _bidDol;
	}

	public BigDecimal getOrderDollarsAsBigDecimal() {
		return _ordDol;
	}

	public double getBidDollars() {
		return _bidDol.doubleValue();
	}

	public double getOrderDollars() {
		return _ordDol.doubleValue();
	}

	public String getBidDollarsAsString() {
		NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);

		return nf.format(_bidDol.doubleValue());
	}

	public String getOrderDollarsAsString() {
		NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);

		return nf.format(_ordDol.doubleValue());
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

		returnString += "\tproductId = " + this.getProductId() + "\n";
		returnString += "\tnegNum = " + this.getNegNum() + "\n";
		returnString += "\tbidDollarsAsBigDecimal = " + this.getBidDollarsAsBigDecimal() + "\n";
		returnString += "\torderDollarsAsBigDecimal = " + this.getOrderDollarsAsBigDecimal() + "\n";
		returnString += "\tbidDollars = " + this.getBidDollars() + "\n";
		returnString += "\torderDollars = " + this.getOrderDollars() + "\n";
		returnString += "\tbidDollarsAsString = " + this.getBidDollarsAsString() + "\n";
		returnString += "\torderDollarsAsString = " + this.getOrderDollarsAsString() + "\n";

		return returnString;
	}
}
