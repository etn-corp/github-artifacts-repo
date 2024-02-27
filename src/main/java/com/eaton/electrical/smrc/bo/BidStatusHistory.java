
package com.eaton.electrical.smrc.bo;

import java.util.*;

public class BidStatusHistory implements java.io.Serializable {
	private String _negNum = "";
	private Calendar _bidDt;
	private Calendar _obtainDt;
	private Calendar _abandonDt;
	private Calendar _lostDt;
	private Calendar _newDt;
	private Calendar _budgetDt;
	private Calendar _buyDt;

	private static final long serialVersionUID = 100;

	public void setNegNum(String neg) {
		_negNum = neg;
	}

	public String getNegNum() {
		return _negNum;
	}

	public void setBidDate(Date dt) {
		_bidDt = Calendar.getInstance();
		_bidDt.setTime(dt);
	}

	public Calendar getBidDate() {
		return _bidDt;
	}

	public String getBidDateAsString() {
		if (_bidDt != null) {
			return (_bidDt.get(Calendar.MONTH)+1) + "/" +
				_bidDt.get(Calendar.DATE) + "/" +
				_bidDt.get(Calendar.YEAR);
		}
		
		return "";
		
	}

	public void setObtainDate(Date dt) {
		_obtainDt = Calendar.getInstance();
		_obtainDt.setTime(dt);
	}

	public Calendar getObtainDate() {
		return _obtainDt;
	}

	public String getObtainDateAsString() {
		if (_obtainDt != null) {
			return (_obtainDt.get(Calendar.MONTH)+1) + "/" +
				_obtainDt.get(Calendar.DATE) + "/" +
				_obtainDt.get(Calendar.YEAR);
		}

		return "";
		
	}


	public void setAbandonDate(Date dt) {
		_abandonDt = Calendar.getInstance();
		_abandonDt.setTime(dt);
	}

	public Calendar getAbandonDate() {
		return _abandonDt;
	}

	public String getAbandonDateAsString() {
		if (_abandonDt != null) {
			return (_abandonDt.get(Calendar.MONTH)+1) + "/" +
				_abandonDt.get(Calendar.DATE) + "/" +
				_abandonDt.get(Calendar.YEAR);
		}

		return "";

	}


	public void setLostDate(Date dt) {
		_lostDt = Calendar.getInstance();
		_lostDt.setTime(dt);
	}

	public Calendar getLostDate() {
		return _lostDt;
	}

	public String getLostDateAsString() {
		if (_lostDt != null) {
			return (_lostDt.get(Calendar.MONTH)+1) + "/" +
				_lostDt.get(Calendar.DATE) + "/" +
				_lostDt.get(Calendar.YEAR);
		}

		return "";

	}


	public void setNewDate(Date dt) {
		_newDt = Calendar.getInstance();
		_newDt.setTime(dt);
	}

	public Calendar getNewDate() {
		return _newDt;
	}

	public String getNewDateAsString() {
		if (_newDt != null) {
			return (_newDt.get(Calendar.MONTH)+1) + "/" +
				_newDt.get(Calendar.DATE) + "/" +
				_newDt.get(Calendar.YEAR);
		}

		return "";

	}


	public void setBudgetDate(Date dt) {
		_budgetDt = Calendar.getInstance();
		_budgetDt.setTime(dt);
	}

	public Calendar getBudgetDate() {
		return _budgetDt;
	}

	public String getBudgetDateAsString() {
		if (_budgetDt != null) {
			return (_budgetDt.get(Calendar.MONTH)+1) + "/" +
				_budgetDt.get(Calendar.DATE) + "/" +
				_budgetDt.get(Calendar.YEAR);
		}

		return "";

	}


	public void setBuyDate(Date dt) {
		_buyDt = Calendar.getInstance();
		_buyDt.setTime(dt);
	}

	public Calendar getBuyDate() {
		return _buyDt;
	}

	public String getBuyDateAsString() {
		if (_buyDt != null) {
			return (_buyDt.get(Calendar.MONTH)+1) + "/" +
				_buyDt.get(Calendar.DATE) + "/" +
				_buyDt.get(Calendar.YEAR);
		}

		return "";

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
		returnString += "\tbidDate = " + this.getBidDate() + "\n";
		returnString += "\tbidDateAsString = " + this.getBidDateAsString() + "\n";
		returnString += "\tobtainDate = " + this.getObtainDate() + "\n";
		returnString += "\tobtainDateAsString = " + this.getObtainDateAsString() + "\n";
		returnString += "\tabandonDate = " + this.getAbandonDate() + "\n";
		returnString += "\tabandonDateAsString = " + this.getAbandonDateAsString() + "\n";
		returnString += "\tlostDate = " + this.getLostDate() + "\n";
		returnString += "\tlostDateAsString = " + this.getLostDateAsString() + "\n";
		returnString += "\tnewDate = " + this.getNewDate() + "\n";
		returnString += "\tnewDateAsString = " + this.getNewDateAsString() + "\n";
		returnString += "\tbudgetDate = " + this.getBudgetDate() + "\n";
		returnString += "\tbudgetDateAsString = " + this.getBudgetDateAsString() + "\n";
		returnString += "\tbuyDate = " + this.getBuyDate() + "\n";
		returnString += "\tbuyDateAsString = " + this.getBuyDateAsString() + "\n";

		return returnString;
	}
}
