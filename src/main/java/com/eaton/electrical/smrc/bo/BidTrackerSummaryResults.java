
package com.eaton.electrical.smrc.bo;

import java.math.*;
import java.text.*;
import java.util.*;

public class BidTrackerSummaryResults implements java.io.Serializable {
	private long _obtCnt = 0;
	private long _totCnt = 0;
	private BigDecimal _obtDol = new BigDecimal(0);
	private BigDecimal _totDol = new BigDecimal(0);

	private static final long serialVersionUID = 100;

	public void setObtainedCnt(long cnt) {
		_obtCnt = cnt;
	}

	public void setTotalCnt(long cnt) {
		_totCnt = cnt;
	}

	public void setObtainedDol(double dol) {
		_obtDol = new BigDecimal(dol);
	}

	public void setTotalDol(double dol) {
		_totDol = new BigDecimal(dol);
	}

	public String getObtainedCntForDisplay() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(0);
		nf.setMinimumFractionDigits(0);
		return nf.format(_obtCnt);
	}

	public String getTotalCntForDisplay() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(0);
		nf.setMinimumFractionDigits(0);
		return nf.format(_totCnt);
	}

	public String getNegHitRate() {
		if (_totCnt == 0) {
			return "N/A";
		}

		NumberFormat nf = NumberFormat.getPercentInstance(Locale.US);
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		return nf.format((double)_obtCnt/(double)_totCnt);
		
	}

	public String getObtainedDolForDisplay() {
		NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
		return nf.format(_obtDol.doubleValue());
	}

	public String getTotalDolForDisplay() {
		NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
		return nf.format(_totDol.doubleValue());
	}

	public String getNegDolRate() {
		if (_totDol.doubleValue() == 0) {
			return "N/A";
		}

		NumberFormat nf = NumberFormat.getPercentInstance(Locale.US);
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		return nf.format(_obtDol.doubleValue()/_totDol.doubleValue());
		
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

		returnString += "\tobtainedCntForDisplay = " + this.getObtainedCntForDisplay() + "\n";
		returnString += "\ttotalCntForDisplay = " + this.getTotalCntForDisplay() + "\n";
		returnString += "\tnegHitRate = " + this.getNegHitRate() + "\n";
		returnString += "\tobtainedDolForDisplay = " + this.getObtainedDolForDisplay() + "\n";
		returnString += "\ttotalDolForDisplay = " + this.getTotalDolForDisplay() + "\n";
		returnString += "\tnegDolRate = " + this.getNegDolRate() + "\n";

		return returnString;
	}
}
