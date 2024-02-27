
package com.eaton.electrical.smrc.bo;

import java.text.*;

/**	This class contains all of the methods and attributes needed by the Target Account Planner
 *	as needed
 *
 *	@author Carl Abel
 */
public class CustProdSubline implements java.io.Serializable {
	String _vcn;
	String _product;
	float _potential = 0;
	float _actual = 0;
	float _lySales = 0;
	float _competitor = 0;
	float _competitor2 = 0;
	float _forecast = 0;
	int _volume = 0;
	String _notes = "";
	boolean _sells = false;
	String _group;
	
	float _cred = 0; // current YTD actual
	float _credLY = 0;
	float _credLYTD = 0;
	float _cred2LY = 0;
	float _dir = 0;	// current YTD actual
	float _dirLY = 0;
	float _dirLYTD = 0;
	float _dir2LY = 0;
	float _em = 0;	// current YTD actual
	float _emLY = 0;
	float _emLYTD = 0;
	float _em2LY = 0;
	
	private static final long serialVersionUID = 100;

	public void setCustomer(String vcn) {
		_vcn = vcn;
	}
	
	public void setProductSubline(String product) {
		_product = product;
	}
	
	public void setPotentialDollars(float potential) {
		_potential = potential;
	}
	
	public void setActualDollars(float actual) {
		_actual = actual;
	}
	
	public void setLastYearDollars(float ly) {
		_lySales = ly;
	}
	
	public void setForecastDollars(float forecast) {
		_forecast = forecast;
	}
	
	public void setCompetitorDollars(float competitor) {
		_competitor = competitor;
	}
	public void setCompetitor2Dollars(float competitor2) {
		_competitor2 = competitor2;
	}	
	public void setVolume(int vol) {
		_volume = vol;
	}
	
	public void setNotes(String notes) {
		_notes = notes;
	}
	
	public void setSells(boolean sells) {
		_sells = sells;
	}
	
	public String getCustomer() {
		return _vcn;
	}
	
	public String getProductSubline() {
		return _product;
	}
	
	public float getPotentialDollars() {
		return _potential;
	}
	
	public float getForecastDollars() {
		return _forecast;
	}
	
	public float getCompetitorDollars() {
		return _competitor;
	}
	public float getCompetitor2Dollars() {
		return _competitor2;
	}
/*
 * 
 *  * 
 * Braffet : 20060330 : Don't think these are used any more - tap dollars now
 * 	
 	public void setCreditYTDDollars(float dol) {
		_cred = dol;
	}
	
	public void setCreditLYDollars(float dol) {
		_credLY = dol;
	}
	
	public void setCredit2LYDollars(float dol) {
		_cred2LY = dol;
	}
	
	public void setCreditLYTDDollars(float dol) {
		_credLYTD = dol;
	}
	
	public void setDirectYTDDollars(float dol) {
		_dir = dol;
	}
	
	public void setDirectLYDollars(float dol) {
		_dirLY = dol;
	}
	
	public void setDirect2LYDollars(float dol) {
		_dir2LY = dol;
	}
	
	public void setDirectLYTDDollars(float dol) {
		_dirLYTD = dol;
	}
	
	public void setEndMktYTDDollars(float dol) {
		_em = dol;
	}
	
	public void setEndMktLYDollars(float dol) {
		_emLY = dol;
	}
	
	public void setEndMkt2LYDollars(float dol) {
		_em2LY = dol;
	}
	
	public void setEndMktLYTDDollars(float dol) {
		_emLYTD = dol;
	}


	public float getCreditYTDDollars() {
		return _cred;
	}
	
	public float getCreditLYDollars() {
		return _credLY;
	}
	
	public float getCredit2LYDollars() {
		return _cred2LY;
	}
	
	public float getCreditLYTDDollars() {
		return _credLYTD;
	}
	
	public float getDirectYTDDollars() {
		return _dir;
	}
	
	public float getDirectLYDollars() {
		return _dirLY;
	}
	
	public float getDirect2LYDollars() {
		return _dir2LY;
	}
	
	public float getDirectLYTDDollars() {
		return _dirLYTD;
	}
	
	public float getEndMktYTDDollars() {
		return _em;
	}
	
	public float getEndMktLYDollars() {
		return _emLY;
	}
	
	public float getEndMkt2LYDollars() {
		return _em2LY;
	}
	
	public float getEndMktLYTDDollars() {
		return _emLYTD;
	}
*/	
	public int getVolume() {
		return _volume;
	}
	
	public String getNotes() {
		return _notes;
	}
	
	public boolean sells() {
		return _sells;
	}
	
	public String displayPotentialDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_potential);
	}
	
	public String displayActualDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_actual);
	}
	
	public String displayLastYearDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_lySales);
	}
	
	public String displayCompetitorDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_competitor);
	}

	public String displayCompetitor2Dollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_competitor2);
	}
	
	public String displayForecastDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_forecast);
	}
	
	public String displayVolume() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_volume);
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

		returnString += "\tcustomer = " + this.getCustomer() + "\n";
		returnString += "\tproductSubline = " + this.getProductSubline() + "\n";
		returnString += "\tpotentialDollars = " + this.getPotentialDollars() + "\n";
		returnString += "\tforecastDollars = " + this.getForecastDollars() + "\n";
		returnString += "\tcompetitorDollars = " + this.getCompetitorDollars() + "\n";
		returnString += "\tcompetitor2Dollars = " + this.getCompetitor2Dollars() + "\n";
//		returnString += "\tcreditYTDDollars = " + this.getCreditYTDDollars() + "\n";
//		returnString += "\tcreditLYDollars = " + this.getCreditLYDollars() + "\n";
//		returnString += "\tcredit2LYDollars = " + this.getCredit2LYDollars() + "\n";
//		returnString += "\tcreditLYTDDollars = " + this.getCreditLYTDDollars() + "\n";
//		returnString += "\tdirectYTDDollars = " + this.getDirectYTDDollars() + "\n";
//		returnString += "\tdirectLYDollars = " + this.getDirectLYDollars() + "\n";
//		returnString += "\tdirect2LYDollars = " + this.getDirect2LYDollars() + "\n";
//		returnString += "\tdirectLYTDDollars = " + this.getDirectLYTDDollars() + "\n";
//		returnString += "\tendMktYTDDollars = " + this.getEndMktYTDDollars() + "\n";
//		returnString += "\tendMktLYDollars = " + this.getEndMktLYDollars() + "\n";
//		returnString += "\tendMkt2LYDollars = " + this.getEndMkt2LYDollars() + "\n";
//		returnString += "\tendMktLYTDDollars = " + this.getEndMktLYTDDollars() + "\n";
		returnString += "\tvolume = " + this.getVolume() + "\n";
		returnString += "\tnotes = " + this.getNotes() + "\n";

		return returnString;
	}
}
