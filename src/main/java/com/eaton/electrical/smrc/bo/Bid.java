
package com.eaton.electrical.smrc.bo;

import java.text.*;
import java.util.*;


public class Bid implements java.io.Serializable {
	private String _neg = "";
	private String _jobName = "";
	private String _status = "";
	private String _salesId = "";
	private double _bidDol = 0;
	private JobType _jobType = new JobType();
	private String _goNum = "";
	private Calendar _goDt;
	private Calendar _bidDt;
	private String _geog = "";
	private ArrayList _prods = new ArrayList(5);
	private boolean custReceivedOrder=false;
	private String salesmanName="";

	private static final long serialVersionUID = 100;

	public void setNegNum(String neg) {
		_neg = neg;
	}

	public void setJobName(String name) {
		_jobName = name;
	}

	public void setStatus(String stat) {
		_status = stat;
	}

	public void setSalesId(String sid) {
		_salesId = sid;
	}

	public void setBidDollars(double dol) {
		_bidDol = dol;
	}

	public void setJobType(JobType type) {
		_jobType = type;
	}

	public void setGONum(String go) {
		_goNum = go;
	}

	public void setGODate(java.util.Date dt) {
		_goDt = Calendar.getInstance();
		_goDt.setTime(dt);
	}

	public void setBidDate(java.util.Date dt) {
		_bidDt = Calendar.getInstance();
		_bidDt.setTime(dt);
	}

	public void setSPGeog(String geog) {
		_geog = geog;
	}

	public String getNegNum() {
		return _neg;
	}

	public String getJobName() {
		return _jobName;
	}

	public String getStatus() {
		return _status;
	}

	public String getSalesId() {
		return _salesId;
	}

	public double getBidDollars() {
		return _bidDol;
	}

	public String getBidDollarsForDisplay() {
		NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
		return nf.format(_bidDol);
	}

	public JobType getJobType() {
		return _jobType;
	}

	public String getGONum() {
		return _goNum;
	}

	public Calendar getGODate() {
		return _goDt;
	}

	public String getGODateAsString() {
		if (_goDt != null ) {
			return (_goDt.get(Calendar.MONTH)+1) + "/" +
				_goDt.get(Calendar.DATE) + "/" +
				_goDt.get(Calendar.YEAR);
		}
		return "";
		
	}

	public Calendar getBidDate() {
		return _bidDt;
	}

	public String getBidDateAsString() {
		if (_bidDt != null ) {
			return (_bidDt.get(Calendar.MONTH)+1) + "/" +
				_bidDt.get(Calendar.DATE) + "/" +
				_bidDt.get(Calendar.YEAR);
		}
		return "";

	}

	public String getSPGeog() {
		return _geog;
	}

	public void addProduct(BidProduct bp) {
		_prods.add(bp);
	}

	public ArrayList getAllProducts() {
		return _prods;
	}

	/*
	 * added by jpv
	 */
	public void setCustReceivedOrder(boolean custReceivedOrder) {
		this.custReceivedOrder=custReceivedOrder;
		
	}
	public boolean getCustReceivedOrder() {
		return this.custReceivedOrder;
		
	}

	/*
	 * added by jpv
	 */
	public void setSalesmanName(String salesmanName) {
		this.salesmanName=salesmanName;
		
	}
	public String getSalesmanName() {
		return this.salesmanName;
		
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

		returnString += "\tnegNum = " + this.getNegNum() + "\n";
		returnString += "\tjobName = " + this.getJobName() + "\n";
		returnString += "\tstatus = " + this.getStatus() + "\n";
		returnString += "\tsalesId = " + this.getSalesId() + "\n";
		returnString += "\tbidDollars = " + this.getBidDollars() + "\n";
		returnString += "\tbidDollarsForDisplay = " + this.getBidDollarsForDisplay() + "\n";
		returnString += "\tjobType = " + this.getJobType() + "\n";
		returnString += "\tgONum = " + this.getGONum() + "\n";
		returnString += "\tgODate = " + this.getGODate() + "\n";
		returnString += "\tgODateAsString = " + this.getGODateAsString() + "\n";
		returnString += "\tbidDate = " + this.getBidDate() + "\n";
		returnString += "\tbidDateAsString = " + this.getBidDateAsString() + "\n";
		returnString += "\tsPGeog = " + this.getSPGeog() + "\n";
		returnString += "\tallProducts = " + this.getAllProducts() + "\n";
		returnString += "\tcustReceivedOrder = " + this.getCustReceivedOrder() + "\n";
		returnString += "\tsalesmanName = " + this.getSalesmanName() + "\n";

		return returnString;
	}
}
