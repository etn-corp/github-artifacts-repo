
package com.eaton.electrical.smrc.bo;

import java.text.*;
import java.io.*;

/**	This class contains all of the methods and attributes needed by the Target Account Planner
 *	as needed
 *
 *	@author Carl Abel
 */
public class CustomerProduct implements Serializable {
	String _vcn;
	String _product;
	float _potential = 0;
	float _competitor = 0;
	float _competitor2 = 0;
	int competitorName = 0;
	int competitor2Name = 0;
	float _forecast = 0;
	int _volume = 0;
	String _notes = "";
	boolean _sells = false;
	
	float _cred = 0; // current YTD actual
	float _credLY = 0;
	float _credLYTD = 0;
	float _cred2LY = 0;
	float _tapDollarsOrder = 0; // current YTD actual
	float _tapDollarsOrderLY = 0;
	float _tapDollarsOrderLYTD = 0;
	float _tapDollarsOrder2LY = 0;
	float _tapDollarsInvoice = 0; // current YTD actual
	float _tapDollarsInvoiceLY = 0;
	float _tapDollarsInvoiceLYTD = 0;
	float _tapDollarsInvoice2LY = 0;
	float _dir = 0;	// current YTD actual
	float _dirLY = 0;
	float _dirLYTD = 0;
	float _dir2LY = 0;
	float _em = 0;	// current YTD actual
	float _emLY = 0;
	float _emLYTD = 0;
	float _em2LY = 0;
	float _sso = 0;	// current YTD actual
	float _ssoLY = 0;
	float _ssoLYTD = 0;
	float _sso2LY = 0;
	
	private static final long serialVersionUID = 100;

	public void setCustomer(String vcn) {
		_vcn = vcn;
	}
	
	public void setProduct(String product) {
		_product = product;
	}
	
	public void setPotentialDollars(float potential) {
		_potential = potential;
	}
/*
 * 
 *  * 
 * Braffet : 20060330 : Don't think these are used any more - Tap dollars now
 * 		
	public void setCreditYTDDollars(float dol) {
		_cred = dol;
	}
	
	public void setCreditLYDollars(float dol) {
		_credLY = dol;
	}
	
	public void setCreditLYTDDollars(float dol) {
		_credLYTD = dol;
	}
	
	public void setCredit2LYDollars(float dol) {
		_cred2LY = dol;
	}
	
	public void setDirectYTDDollars(float dol) {
		_dir = dol;
	}
	
	public void setDirectLYDollars(float dol) {
		_dirLY = dol;
	}
	
	public void setDirectLYTDDollars(float dol) {
		_dirLYTD = dol;
	}
	
	public void setDirect2LYDollars(float dol) {
		_dir2LY = dol;
	}
	
	public void setEndMktYTDDollars(float dol) {
		_em = dol;
	}
	
	public void setEndMktLYDollars(float dol) {
		_emLY = dol;
	}
	
	public void setEndMktLYTDDollars(float dol) {
		_emLYTD = dol;
	}
	
	public void setEndMkt2LYDollars(float dol) {
		_em2LY = dol;
	}
	
	public void setSSOYTDDollars(float dol) {
		_sso = dol;
	}
	
	public void setSSOLYDollars(float dol) {
		_ssoLY = dol;
	}
	
	public void setSSOLYTDDollars(float dol) {
		_ssoLYTD = dol;
	}
	
	public void setSSO2LYDollars(float dol) {
		_sso2LY = dol;
	}
*/	
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
	
	public String getProduct() {
		return _product;
	}
	
	public float getPotentialDollars() {
		return _potential;
	}
/*
 * 
 * Braffet : 20060330 : Don't think these are used any more
 * 	
	public float getCreditYTDDollars() {
		return _cred;
	}
	
	public float getCreditLYDollars() {
		return _credLY;
	}
	
	public float getCreditLYTDDollars() {
		return _credLYTD;
	}
	
	public float getCredit2LYDollars() {
		return _cred2LY;
	}
	
	public float getDirectYTDDollars() {
		return _dir;
	}
	
	public float getDirectLYDollars() {
		return _dirLY;
	}
	
	public float getDirectLYTDDollars() {
		return _dirLYTD;
	}
	
	public float getDirect2LYDollars() {
		return _dir2LY;
	}
	
	public float getEndMktYTDDollars() {
		return _em;
	}
	
	public float getEndMktLYDollars() {
		return _emLY;
	}
	
	public float getEndMktLYTDDollars() {
		return _emLYTD;
	}
	
	public float getEndMkt2LYDollars() {
		return _em2LY;
	}
	
	
	public float getSSOYTDDollars() {
		return _sso;
	}
	
	public float getSSOLYDollars() {
		return _ssoLY;
	}
	
	public float getSSOLYTDDollars() {
		return _ssoLYTD;
	}
	
	public float getSSO2LYDollars() {
		return _sso2LY;
	}
*/	
	public float getForecastDollars() {
		return _forecast;
	}
	
	public float getCompetitorDollars() {
		return _competitor;
	}
	public float getCompetitor2Dollars() {
		return _competitor2;
	}
	
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
		NumberFormat displayPot = NumberFormat.getCurrencyInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_potential);
	}
/*	
	public String displayCreditYTDDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_cred);
	}
	
	public String displayCreditLYDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_credLY);
	}
	
	public String displayCreditLYTDDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_credLYTD);
	}
	
	public String displayCredit2LYDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_cred2LY);
	}
	
	public String displayDirectYTDDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_dir);
	}
	
	public String displayDirectLYDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_dirLY);
	}
	
	public String displayDirectLYTDDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_dirLYTD);
	}
	
	public String displayDirect2LYDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_dir2LY);
	}
	
	public String displayEndMktYTDDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_em);
	}
	
	public String displayEndMktLYDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_emLY);
	}
	
	public String displayEndMktLYTDDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_emLYTD);
	}
	
	public String displayEndMkt2LYDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_em2LY);
	}
	
	public String displaySSOYTDDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_sso);
	}
	
	public String displaySSOLYDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_ssoLY);
	}
	
	public String displaySSOLYTDDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_ssoLYTD);
	}
	
	public String displaySSO2LYDollars() {
		NumberFormat displayPot = NumberFormat.getInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_sso2LY);
	}
*/	
	public String displayCompetitorDollars() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_competitor);
	}
	
	public String displayCompetitor2Dollars() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance();
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);
		
		return displayPot.format(_competitor2);
	}
	
	public String displayForecastDollars() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance();
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
	/**
	 * @return Returns the competitor2Name.
	 */
	public int getCompetitor2Name() {
		return competitor2Name;
	}
	/**
	 * @param competitor2Name The competitor2Name to set.
	 */
	public void setCompetitor2Name(int competitor2Name) {
		this.competitor2Name = competitor2Name;
	}
	/**
	 * @return Returns the competitorName.
	 */
	public int getCompetitorName() {
		return competitorName;
	}
	/**
	 * @param competitorName The competitorName to set.
	 */
	public void setCompetitorName(int competitorName) {
		this.competitorName = competitorName;
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
		returnString += "\tproduct = " + this.getProduct() + "\n";
		returnString += "\tpotentialDollars = " + this.getPotentialDollars() + "\n";
//		returnString += "\tcreditYTDDollars = " + this.getCreditYTDDollars() + "\n";
//		returnString += "\tcreditLYDollars = " + this.getCreditLYDollars() + "\n";
//		returnString += "\tcreditLYTDDollars = " + this.getCreditLYTDDollars() + "\n";
//		returnString += "\tcredit2LYDollars = " + this.getCredit2LYDollars() + "\n";
//		returnString += "\tdirectYTDDollars = " + this.getDirectYTDDollars() + "\n";
//		returnString += "\tdirectLYDollars = " + this.getDirectLYDollars() + "\n";
//		returnString += "\tdirectLYTDDollars = " + this.getDirectLYTDDollars() + "\n";
//		returnString += "\tdirect2LYDollars = " + this.getDirect2LYDollars() + "\n";
//		returnString += "\tendMktYTDDollars = " + this.getEndMktYTDDollars() + "\n";
//		returnString += "\tendMktLYDollars = " + this.getEndMktLYDollars() + "\n";
//		returnString += "\tendMktLYTDDollars = " + this.getEndMktLYTDDollars() + "\n";
//		returnString += "\tendMkt2LYDollars = " + this.getEndMkt2LYDollars() + "\n";
//		returnString += "\tsSOYTDDollars = " + this.getSSOYTDDollars() + "\n";
//		returnString += "\tsSOLYDollars = " + this.getSSOLYDollars() + "\n";
//		returnString += "\tsSOLYTDDollars = " + this.getSSOLYTDDollars() + "\n";
//		returnString += "\tsSO2LYDollars = " + this.getSSO2LYDollars() + "\n";
		returnString += "\tforecastDollars = " + this.getForecastDollars() + "\n";
		returnString += "\tcompetitorDollars = " + this.getCompetitorDollars() + "\n";
		returnString += "\tcompetitor2Dollars = " + this.getCompetitor2Dollars() + "\n";
		returnString += "\tvolume = " + this.getVolume() + "\n";
		returnString += "\tnotes = " + this.getNotes() + "\n";
		returnString += "\tcompetitor2Name = " + this.getCompetitor2Name() + "\n";
		returnString += "\tcompetitorName = " + this.getCompetitorName() + "\n";

		return returnString;
	}

	/**
	 * @return Returns the tapDollarsInvoice.
	 */
	public float getTapDollarsInvoiceYTD() {
		return _tapDollarsInvoice;
	}

	/**
	 * @param tapDollarsInvoice The tapDollarsInvoice to set.
	 */
	public void setTapDollarsInvoiceYTD(float tapDollarsInvoice) {
		_tapDollarsInvoice = tapDollarsInvoice;
	}

	/**
	 * @return Returns the _tapDollarsInvoice2LY.
	 */
	public float getTapDollarsInvoice2LY() {
		return _tapDollarsInvoice2LY;
	}

	/**
	 * @param tapDollarsInvoice2LY The tapDollarsInvoice2LY to set.
	 */
	public void setTapDollarsInvoice2LY(float tapDollarsInvoice2LY) {
		_tapDollarsInvoice2LY = tapDollarsInvoice2LY;
	}

	/**
	 * @return Returns the tapDollarsInvoiceLY.
	 */
	public float getTapDollarsInvoiceLY() {
		return _tapDollarsInvoiceLY;
	}

	/**
	 * @param tapDollarsInvoiceLY The tapDollarsInvoiceLY to set.
	 */
	public void setTapDollarsInvoiceLY(float tapDollarsInvoiceLY) {
		_tapDollarsInvoiceLY = tapDollarsInvoiceLY;
	}

	/**
	 * @return Returns the tapDollarsInvoiceLYTD.
	 */
	public float getTapDollarsInvoiceLYTD() {
		return _tapDollarsInvoiceLYTD;
	}

	/**
	 * @param tapDollarsInvoiceLYTD The tapDollarsInvoiceLYTD to set.
	 */
	public void setTapDollarsInvoiceLYTD(float tapDollarsInvoiceLYTD) {
		_tapDollarsInvoiceLYTD = tapDollarsInvoiceLYTD;
	}

	/**
	 * @return Returns the tapDollarsOrder.
	 */
	public float getTapDollarsOrderYTD() {
		return _tapDollarsOrder;
	}

	/**
	 * @param tapDollarsOrder The tapDollarsOrder to set.
	 */
	public void setTapDollarsOrderYTD(float tapDollarsOrder) {
		_tapDollarsOrder = tapDollarsOrder;
	}

	/**
	 * @return Returns the tapDollarsOrder2LY.
	 */
	public float getTapDollarsOrder2LY() {
		return _tapDollarsOrder2LY;
	}

	/**
	 * @param tapDollarsOrder2LY The tapDollarsOrder2LY to set.
	 */
	public void setTapDollarsOrder2LY(float tapDollarsOrder2LY) {
		_tapDollarsOrder2LY = tapDollarsOrder2LY;
	}

	/**
	 * @return Returns the tapDollarsOrderLY.
	 */
	public float getTapDollarsOrderLY() {
		return _tapDollarsOrderLY;
	}

	/**
	 * @param tapDollarsOrderLY The tapDollarsOrderLY to set.
	 */
	public void setTapDollarsOrderLY(float tapDollarsOrderLY) {
		_tapDollarsOrderLY = tapDollarsOrderLY;
	}

	/**
	 * @return Returns the tapDollarsOrderLYTD.
	 */
	public float getTapDollarsOrderLYTD() {
		return _tapDollarsOrderLYTD;
	}

	/**
	 * @param tapDollarsOrderLYTD The tapDollarsOrderLYTD to set.
	 */
	public void setTapDollarsOrderLYTD(float tapDollarsOrderLYTD) {
		_tapDollarsOrderLYTD = tapDollarsOrderLYTD;
	}
}
