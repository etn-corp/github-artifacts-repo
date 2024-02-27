
package com.eaton.electrical.smrc.bo;

import java.util.*;

public class HeaderBean extends Object implements java.io.Serializable {
	ResourceBundle rb = ResourceBundle.getBundle("com.eaton.electrical.smrc.SMRC");
	
	private String _page="";
	private ArrayList _groups= new ArrayList();
	private User _user = new User();
	private SalesGroup _sGroup = new SalesGroup();
	private Customer _cust= new Customer();
	private boolean _addCust = false;
	private boolean _custExists = false;
	private String _geog="";
	private String _industry="";
	private String _help="";
	private String _backSort="";
	private boolean _popup = false;
	private boolean _forceCustLinks = false;
	private boolean _error = false;
	
	private String imagesURL = rb.getString("imagesURL");
	private String cssURL = rb.getString("cssURL");
	private String jsURL = rb.getString("jsURL");
	
	private Account account = null;
	
	private static final long serialVersionUID = 100;

	public HeaderBean(){
		account = new Account();
	}
	
	public void setPage(String page) {
		_page = page;
	}
	
	public String getPage (){
		return _page;   
	}
	
	public void setHelpPage(String help) {
		_help = help;
	}
	
	public String getHelp () {
		return _help;   
	}
	
	public void setGroups(ArrayList groups) {
		_groups = groups;
	}
	
	public ArrayList getGroups(){
		return _groups;   
	}
	public void setUser(User user) {
		_user = user;
	}
	
	public User getUser (){
		return _user;   
	}
	public void setThisGroup(SalesGroup thisGroup) {
		_sGroup = thisGroup;
	}
	
	public SalesGroup getSalesGroup (){
		return _sGroup;   
	}
	public void setCustomer(Customer cust) {
		_cust = cust;
	}
	
	public Customer getCustomer (){
		return _cust;   
	}
	public void setBackSort(String backSort) {
		_backSort = backSort;
	}
	
	public String getBackSort (){
		return _backSort;   
	}
	public void setAddCust(boolean add, boolean custExists) {
		_addCust = add;
		_custExists = custExists;
	}
	
	public boolean getAddCust (){
		return _addCust;   
	}
	
	public boolean getCustExists (){
		return _custExists;   
	}
	public void setGeog(String geog) {
		_geog = geog;
	}
	
	public String getGeog (){
		return _geog;   
	}
	public void setIndustry(String industry) {
		_industry = industry;
	}
	
	public String getIndustry (){
		return _industry;   
	}
	public void setPopup(boolean popup) {
		_popup = popup;
	}
	
	public boolean getPopup (){
		return _popup;   
	}
	public void setForceCustLinks(boolean force) {
		_forceCustLinks = force;
	}
	
	public boolean getForceCustLinks (){
		return _forceCustLinks;   
	}
	public void setError(boolean yn) {
		_error = yn;
	}
	
	public boolean getError(){
		return _error;   
	}
	
	public String getImagesURL () {
		return imagesURL;
	}
	
	public String getCssURL (){
		return cssURL;
	}
	
	public String getJsURL (){
		return jsURL;   
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

		returnString += "\tpage  = " + this.getPage () + "\n";
		returnString += "\thelp  = " + this.getHelp () + "\n";
		returnString += "\tgroups = " + this.getGroups() + "\n";
		returnString += "\tuser  = " + this.getUser () + "\n";
		returnString += "\tsalesGroup  = " + this.getSalesGroup () + "\n";
		returnString += "\tcustomer  = " + this.getCustomer () + "\n";
		returnString += "\tbackSort  = " + this.getBackSort () + "\n";
		returnString += "\taddCust  = " + this.getAddCust () + "\n";
		returnString += "\tcustExists  = " + this.getCustExists () + "\n";
		returnString += "\tgeog  = " + this.getGeog () + "\n";
		returnString += "\tindustry  = " + this.getIndustry () + "\n";
		returnString += "\tpopup  = " + this.getPopup () + "\n";
		returnString += "\tforceCustLinks  = " + this.getForceCustLinks () + "\n";
		returnString += "\terror = " + this.getError() + "\n";
		returnString += "\timagesURL  = " + this.getImagesURL () + "\n";
		returnString += "\tcssURL  = " + this.getCssURL () + "\n";
		returnString += "\tjsURL  = " + this.getJsURL () + "\n";
		returnString += "\taccount  = " + this.getAccount().toString() + "\n";

		return returnString;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
}
