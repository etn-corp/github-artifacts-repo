
package com.eaton.electrical.smrc.bo;

import java.text.*;
import java.util.*;

/**	This class contains all of the methods and attributes needed by the Target Account Planner
*	as needed
*
*	@author Carl Abel
*/
public class SearchResults implements java.io.Serializable {
	String _id = "";
	String _description = "";
	double _potential = 0;
	double _forecast = 0;
	double _competitor = 0;
	int _volume= 0;
	int _level = 0;
	String _group = "";
	boolean _subline = false;

	double _crCurMonthSales = 0;
	double _crYtdSales = 0;
	double _crPrevYtdSales = 0;
	double _crPrevYearTotalSales = 0;
	double _crPrevYearMonthSales = 0;
	double _endMktCurMonthSales = 0;
	double _endMktYtdSales = 0;
	double _endMktPrevYtdSales = 0;
	double _endMktPrevYearTotalSales = 0;
	double _endMktPrevYearMonthSales = 0;
	double _dirCurMonthSales = 0;
	double _dirYtdSales = 0;
	double _dirPrevYtdSales = 0;
	double _dirPrevYearTotalSales = 0;
	double _dirPrevYearMonthSales = 0;
	double _crCurMonthOrder = 0;
	double _crYtdOrder = 0;
	double _crPrevYtdOrder = 0;
	double _crPrevYearTotalOrder = 0;
	double _crPrevYearMonthOrder = 0;
	double _endMktCurMonthOrder = 0;
	double _endMktYtdOrder = 0;
	double _endMktPrevYtdOrder = 0;
	double _endMktPrevYearTotalOrder = 0;
	double _endMktPrevYearMonthOrder = 0;
	double _dirCurMonthOrder = 0;
	double _dirYtdOrder = 0;
	double _dirPrevYtdOrder = 0;
	double _dirPrevYearTotalOrder = 0;
	double _dirPrevYearMonthOrder = 0;

	double _ssoCurMonth = 0;
	double _ssoYtd = 0;
	double _ssoPrevYtd = 0;
	double _ssoPrevYearTotal = 0;
	double _ssoPrevYearMonth = 0;

	private static final long serialVersionUID = 100;

	public void setId(String id) {
		_id = id;
	}

	public void setDescription(String desc) {
		_description = desc;
	}

	public void setPotentialDollars(double potential) {
		_potential = potential / 1000;
	}

	public void setForecastDollars(double forecast) {
		_forecast = forecast / 1000;
	}

	public void setCompetitorSales (double comp) {
		_competitor = comp / 1000;
	}

	public void setVolume(int volume) {
		_volume = volume;
	}

	public void setLevel(int level) {
		_level = level;
	}

	public void setGroup(String group) {
		_group = group;
	}


	// Set Actual Sales
	public void setCRCurMoSales(double crCurMonthSales) {
		_crCurMonthSales = crCurMonthSales / 1000;
	}

	public void setCRYTDSales(double crYtdSales) {
		_crYtdSales = crYtdSales / 1000;
	}

	public void setCRPrevYTDSales(double crPrevYtdSales) {
		_crPrevYtdSales = crPrevYtdSales / 1000;
	}

	public void setCRPrevYrTotSales(double crPrevYearTotalSales) {
		_crPrevYearTotalSales = crPrevYearTotalSales / 1000;
	}

	public void setCRPrevYrMoSales(double crPrevYearMonthSales) {
		_crPrevYearMonthSales = crPrevYearMonthSales / 1000;
	}

	public void setEMCurMoSales(double endMktCurMonthSales) {
		_endMktCurMonthSales = endMktCurMonthSales / 1000;
	}

	public void setEMYTDSales(double endMktYtdSales) {
		_endMktYtdSales = endMktYtdSales / 1000;
	}

	public void setEMPrevYTDSales(double endMktPrevYtdSales) {
		_endMktPrevYtdSales = endMktPrevYtdSales / 1000;
	}

	public void setEMPrevYrTotSales(double endMktPrevYearTotalSales) {
		_endMktPrevYearTotalSales = endMktPrevYearTotalSales / 1000;
	}

	public void setEMPrevYrMoSales(double endMktPrevYearMonthSales) {
		_endMktPrevYearMonthSales = endMktPrevYearMonthSales / 1000;
	}

	public void setDirCurMoSales(double dirCurMonthSales) {
		_dirCurMonthSales = dirCurMonthSales / 1000;
	}

	public void setDirYTDSales(double dirYtdSales) {
		_dirYtdSales = dirYtdSales / 1000;
	}

	public void setDirPrevYTDSales(double dirPrevYtdSales) {
		_dirPrevYtdSales = dirPrevYtdSales / 1000;
	}

	public void setDirPrevYrTotSales(double dirPrevYearTotalSales) {
		_dirPrevYearTotalSales = dirPrevYearTotalSales / 1000;
	}

	public void setDirPrevYrMoSales(double dirPrevYearMonthSales) {
		_dirPrevYearMonthSales = dirPrevYearMonthSales / 1000;
	}

	public void setCRCurMoOrder(double crCurMonthOrder) {
		_crCurMonthOrder = crCurMonthOrder / 1000;
	}

	public void setCRYTDOrder(double crYtdOrder) {
		_crYtdOrder = crYtdOrder / 1000;
	}

	public void setCRPrevYTDOrder(double crPrevYtdOrder) {
		_crPrevYtdOrder = crPrevYtdOrder / 1000;
	}

	public void setCRPrevYrTotOrder(double crPrevYearTotalOrder) {
		_crPrevYearTotalOrder = crPrevYearTotalOrder / 1000;
	}

	public void setCRPrevYrMoOrder(double crPrevYearMonthOrder) {
		_crPrevYearMonthOrder = crPrevYearMonthOrder / 1000;
	}

	public void setEMCurMoOrder(double endMktCurMonthOrder) {
		_endMktCurMonthOrder = endMktCurMonthOrder / 1000;
	}

	public void setEMYTDOrder(double endMktYtdOrder) {
		_endMktYtdOrder = endMktYtdOrder / 1000;
	}

	public void setEMPrevYTDOrder(double endMktPrevYtdOrder) {
		_endMktPrevYtdOrder = endMktPrevYtdOrder / 1000;
	}

	public void setEMPrevYrTotOrder(double endMktPrevYearTotalOrder) {
		_endMktPrevYearTotalOrder = endMktPrevYearTotalOrder / 1000;
	}

	public void setEMPrevYrMoOrder(double endMktPrevYearMonthOrder) {
		_endMktPrevYearMonthOrder = endMktPrevYearMonthOrder / 1000;
	}

	public void setDirCurMoOrder(double dirCurMonthOrder) {
		_dirCurMonthOrder = dirCurMonthOrder / 1000;
	}

	public void setDirYTDOrder(double dirYtdOrder) {
		_dirYtdOrder = dirYtdOrder / 1000;
	}

	public void setDirPrevYTDOrder(double dirPrevYtdOrder) {
		_dirPrevYtdOrder = dirPrevYtdOrder / 1000;
	}

	public void setDirPrevYrTotOrder(double dirPrevYearTotalOrder) {
		_dirPrevYearTotalOrder = dirPrevYearTotalOrder / 1000;
	}

	public void setDirPrevYrMoOrder(double dirPrevYearMonthOrder) {
		_dirPrevYearMonthOrder = dirPrevYearMonthOrder / 1000;
	}

	public void setSSOCurMo(double dol) {
		_ssoCurMonth = dol / 1000;
	}

	public void setSSOYTD(double dol) {
		_ssoYtd = dol / 1000;
	}

	public void setSSOPrevYTD(double dol) {
		_ssoPrevYtd = dol / 1000;
	}

	public void setSSOPrevYrTot(double dol) {
		_ssoPrevYearTotal = dol / 1000;
	}

	public void setSSOPrevYrMo(double dol) {
		_ssoPrevYearMonth = dol / 1000;
	}

	/** If the user clicks product subline instead of products, use this to indicate
	*/
	public void setSubline(boolean subline) {
		_subline = subline;
	}

	public String getGroupId() {
		return _group;
	}

	public String getId() {
		return _id;
	}

	public String getDescription() {
		return _description;
	}

	public double getPotentialDollars() {
		return _potential;
	}

	public double getForecastDollars() {
		return _forecast;
	}

	public int getVolume() {
		return _volume;
	}

	public double getCompetitorDollars () {
		return _competitor;
	}


	public int getLevel() {
		return _level;
	}

	public boolean useSubline() {
		return _subline;
	}

	public String displayPotentialDollars() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_potential);
	}

	public String displayForecastDollars() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_forecast);
	}

	public String displayCompetitorDollars () {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_competitor);
	}

	public String displayVolume() {
		NumberFormat displayPot = NumberFormat.getInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_volume);
	}


	// Retrieve Actual Sales
	public double getCRCurMoSales() {
		return _crCurMonthSales;
	}

	public double getCRYTDSales() {
		return _crYtdSales;
	}

	public double getCRPrevYTDSales() {
		return _crPrevYtdSales;
	}

	public double getCRPrevYrTotSales() {
		return _crPrevYearTotalSales;
	}

	public double getCRPrevYrMoSales() {
		return _crPrevYearMonthSales;
	}

	public double getEMCurMoSales() {
		return _endMktCurMonthSales;
	}

	public double getEMYTDSales() {
		return _endMktYtdSales;
	}

	public double getEMPrevYTDSales() {
		return _endMktPrevYtdSales;
	}

	public double getEMPrevYrTotSales() {
		return _endMktPrevYearTotalSales;
	}

	public double getEMPrevYrMoSales() {
		return _endMktPrevYearMonthSales;
	}

	public double getDirCurMoSales() {
		return _dirCurMonthSales;
	}

	public double getDirYTDSales() {
		return _dirYtdSales;
	}

	public double getDirPrevYTDSales() {
		return _dirPrevYtdSales;
	}

	public double getDirPrevYrTotSales() {
		return _dirPrevYearTotalSales;
	}

	public double getDirPrevYrMoSales() {
		return _dirPrevYearMonthSales;
	}

	public double getCRCurMoOrder() {
		return _crCurMonthOrder;
	}

	public double getCRYTDOrder() {
		return _crYtdOrder;
	}

	public double getCRPrevYTDOrder() {
		return _crPrevYtdOrder;
	}

	public double getCRPrevYrTotOrder() {
		return _crPrevYearTotalOrder;
	}

	public double getCRPrevYrMoOrder() {
		return _crPrevYearMonthOrder;
	}

	public double getEMCurMoOrder() {
		return _endMktCurMonthOrder;
	}

	public double getEMYTDOrder() {
		return _endMktYtdOrder;
	}

	public double getEMPrevYTDOrder() {
		return _endMktPrevYtdOrder;
	}

	public double getEMPrevYrTotOrder() {
		return _endMktPrevYearTotalOrder;
	}

	public double getEMPrevYrMoOrder() {
		return _endMktPrevYearMonthOrder;
	}

	public double getDirCurMoOrder() {
		return _dirCurMonthOrder;
	}

	public double getDirYTDOrder() {
		return _dirYtdOrder;
	}

	public double getDirPrevYTDOrder() {
		return _dirPrevYtdOrder;
	}

	public double getDirPrevYrTotOrder() {
		return _dirPrevYearTotalOrder;
	}

	public double getDirPrevYrMoOrder() {
		return _dirPrevYearMonthOrder;
	}

	public double getSSOCurMoOrder() {
		return _ssoCurMonth;
	}

	public double getSSOYTD() {
		return _ssoYtd;
	}

	public double getSSOPrevYTD() {
		return _ssoPrevYtd;
	}

	public double getSSOPrevYrTot() {
		return _ssoPrevYearTotal;
	}

	public double getSSOPrevYrMo() {
		return _ssoPrevYearMonth;
	}

	// Display Actual Sales
	public String displayCRCurMoSales() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_crCurMonthSales);
	}

	public String displayCRYTDSales() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_crYtdSales);
	}

	public String displayCRPrevYTDSales() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_crPrevYtdSales);
	}

	public String displayCRPrevYrTotSales() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_crPrevYearTotalSales);
	}

	public String displayCRPrevYrMoSales() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_crPrevYearMonthSales);
	}

	public String displayEMCurMoSales() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_endMktCurMonthSales);
	}

	public String displayEMYTDSales() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_endMktYtdSales);
	}

	public String displayEMPrevYTDSales() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_endMktPrevYtdSales);
	}

	public String displayEMPrevYrTotSales() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_endMktPrevYearTotalSales);
	}

	public String displayEMPrevYrMoSales() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_endMktPrevYearMonthSales);
	}

	public String displayDirCurMoSales() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_dirCurMonthSales);
	}

	public String displayDirYTDSales() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_dirYtdSales);
	}

	public String displayDirPrevYTDSales() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_dirPrevYtdSales);
	}

	public String displayDirPrevYrTotSales() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_dirPrevYearTotalSales);
	}

	public String displayDirPrevYrMoSales() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_dirPrevYearMonthSales);
	}

	public String displayCRCurMoOrder() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_crCurMonthOrder);
	}

	public String displayCRYTDOrder() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_crYtdOrder);
	}

	public String displayCRPrevYTDOrder() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_crPrevYtdOrder);
	}

	public String displayCRPrevYrTotOrder() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_crPrevYearTotalOrder);
	}

	public String displayCRPrevYrMoOrder() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_crPrevYearMonthOrder);
	}

	public String displayEMCurMoOrder() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_endMktCurMonthOrder);
	}

	public String displayEMYTDOrder() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_endMktYtdOrder);
	}

	public String displayEMPrevYTDOrder() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_endMktPrevYtdOrder);
	}

	public String displayEMPrevYrTotOrder() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_endMktPrevYearTotalOrder);
	}

	public String displayEMPrevYrMoOrder() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_endMktPrevYearMonthOrder);
	}

	public String displayDirCurMoOrder() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_dirCurMonthOrder);
	}

	public String displayDirYTDOrder() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_dirYtdOrder);
	}

	public String displayDirPrevYTDOrder() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_dirPrevYtdOrder);
	}

	public String displayDirPrevYrTotOrder() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_dirPrevYearTotalOrder);
	}

	public String displayDirPrevYrMoOrder() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_dirPrevYearMonthOrder);
	}

	public String displaySSOCurMo() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_ssoCurMonth);
	}

	public String displaySSOYTD() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_ssoYtd);
	}

	public String displaySSOPrevYTD() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_ssoPrevYtd);
	}

	public String displaySSOPrevYrTot() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_ssoPrevYearTotal);
	}

	public String displaySSOPrevYrMo() {
		NumberFormat displayPot = NumberFormat.getCurrencyInstance(Locale.US);
		displayPot.setMaximumFractionDigits(0);
		displayPot.setMinimumFractionDigits(0);

		return displayPot.format(_ssoPrevYearMonth);
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

		returnString += "\tgroupId = " + this.getGroupId() + "\n";
		returnString += "\tid = " + this.getId() + "\n";
		returnString += "\tdescription = " + this.getDescription() + "\n";
		returnString += "\tpotentialDollars = " + this.getPotentialDollars() + "\n";
		returnString += "\tforecastDollars = " + this.getForecastDollars() + "\n";
		returnString += "\tvolume = " + this.getVolume() + "\n";
		returnString += "\tcompetitorDollars  = " + this.getCompetitorDollars () + "\n";
		returnString += "\tlevel = " + this.getLevel() + "\n";
		returnString += "\tcRCurMoSales = " + this.getCRCurMoSales() + "\n";
		returnString += "\tcRYTDSales = " + this.getCRYTDSales() + "\n";
		returnString += "\tcRPrevYTDSales = " + this.getCRPrevYTDSales() + "\n";
		returnString += "\tcRPrevYrTotSales = " + this.getCRPrevYrTotSales() + "\n";
		returnString += "\tcRPrevYrMoSales = " + this.getCRPrevYrMoSales() + "\n";
		returnString += "\teMCurMoSales = " + this.getEMCurMoSales() + "\n";
		returnString += "\teMYTDSales = " + this.getEMYTDSales() + "\n";
		returnString += "\teMPrevYTDSales = " + this.getEMPrevYTDSales() + "\n";
		returnString += "\teMPrevYrTotSales = " + this.getEMPrevYrTotSales() + "\n";
		returnString += "\teMPrevYrMoSales = " + this.getEMPrevYrMoSales() + "\n";
		returnString += "\tdirCurMoSales = " + this.getDirCurMoSales() + "\n";
		returnString += "\tdirYTDSales = " + this.getDirYTDSales() + "\n";
		returnString += "\tdirPrevYTDSales = " + this.getDirPrevYTDSales() + "\n";
		returnString += "\tdirPrevYrTotSales = " + this.getDirPrevYrTotSales() + "\n";
		returnString += "\tdirPrevYrMoSales = " + this.getDirPrevYrMoSales() + "\n";
		returnString += "\tcRCurMoOrder = " + this.getCRCurMoOrder() + "\n";
		returnString += "\tcRYTDOrder = " + this.getCRYTDOrder() + "\n";
		returnString += "\tcRPrevYTDOrder = " + this.getCRPrevYTDOrder() + "\n";
		returnString += "\tcRPrevYrTotOrder = " + this.getCRPrevYrTotOrder() + "\n";
		returnString += "\tcRPrevYrMoOrder = " + this.getCRPrevYrMoOrder() + "\n";
		returnString += "\teMCurMoOrder = " + this.getEMCurMoOrder() + "\n";
		returnString += "\teMYTDOrder = " + this.getEMYTDOrder() + "\n";
		returnString += "\teMPrevYTDOrder = " + this.getEMPrevYTDOrder() + "\n";
		returnString += "\teMPrevYrTotOrder = " + this.getEMPrevYrTotOrder() + "\n";
		returnString += "\teMPrevYrMoOrder = " + this.getEMPrevYrMoOrder() + "\n";
		returnString += "\tdirCurMoOrder = " + this.getDirCurMoOrder() + "\n";
		returnString += "\tdirYTDOrder = " + this.getDirYTDOrder() + "\n";
		returnString += "\tdirPrevYTDOrder = " + this.getDirPrevYTDOrder() + "\n";
		returnString += "\tdirPrevYrTotOrder = " + this.getDirPrevYrTotOrder() + "\n";
		returnString += "\tdirPrevYrMoOrder = " + this.getDirPrevYrMoOrder() + "\n";
		returnString += "\tsSOCurMoOrder = " + this.getSSOCurMoOrder() + "\n";
		returnString += "\tsSOYTD = " + this.getSSOYTD() + "\n";
		returnString += "\tsSOPrevYTD = " + this.getSSOPrevYTD() + "\n";
		returnString += "\tsSOPrevYrTot = " + this.getSSOPrevYrTot() + "\n";
		returnString += "\tsSOPrevYrMo = " + this.getSSOPrevYrMo() + "\n";

		return returnString;
	}
}
