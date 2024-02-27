package com.eaton.electrical.smrc.bo;

/**	This class contains all of the methods and attributes needed by the Target Account Planner
*	as needed to describe each sales group.
*
*	@author Carl Abel
*/
public class SalesGroup implements java.io.Serializable {
	private String _id = "";
	private String _desc = "";
	private String _logo = "";
	private boolean _showVolume = false;
	private boolean _useOrder = false;
	private boolean _useSales = false;
	private boolean _useCredit = false;
	private boolean _useDirect = false;
	private boolean _useEndMkt = false;
	private boolean _useSSO = false;
	private String _numOfDesc = "";
	private boolean _usesGAM = false;
	private boolean _usesPcntWithCust = false;
	private boolean hasFullAccessForGroup=false, allowedToDelete=false;

	private static final long serialVersionUID = 100;

	/** Lets the calling routine set the group id
	*	@param String The group id for this sales group
	*/
	public void setId(String id) {
		_id = id;
	}

	/** Lets the calling routine set the description
	*	@param String The description of this sales group
	*/
	public void setDescription(String desc) {
		_desc = desc;
	}

	/** Lets the calling routine set the logo or image for this sales group
	*	@param String The file name for this logo or image
	*/
	public void setLogo(String logo) {
		_logo = logo;
	}

	/** Lets the calling routine identify whether this sales group uses the volume field
	*	@param String Y/N indicator
	*/
	public void setViewVolume(String vol) {
		_showVolume = vol.equals("Y");
	}

	/** Lets the calling routine identify whether this sales group uses the volume field
	*	@param boolean T/F indicator
	*/
	public void setViewVolume(boolean vol) {
		_showVolume = vol;
	}

	/** Each group will identify whether sales or orders are the primary view of the dollars
	*	@param String Indicator stating whether sales or orders is the primary view of dollars
	*/
	public void setSalesOrders(String salOrd) {
		_useSales = (salOrd.equals("S"));
		_useOrder = (salOrd.equals("O"));
	}

	/** Each group will identify whether it uses direct, credit, or end mkt dollars primarily
	*	@param String Indicator stating whether group uses direct, credit, or end mkt dollars
	*/
	public void setDollarType(String dolType) {
		_useCredit = (dolType.equals("C"));
		_useDirect = (dolType.equals("D"));
		_useEndMkt = (dolType.equals("E"));
		_useSSO = (dolType.equals("S"));
	}

	/** This method will describe the codes used to identify the default dollar type
	*	@return String The description of the dollars for this group
	*/
	public String getDolDescription() {
		StringBuffer desc = new StringBuffer("");

		if (_useCredit) {
			desc.append("Credit");
		}
		else if (_useDirect) {
			desc.append("Charge To");
		}
		else if (_useEndMkt) {
			desc.append("End Market");
		}
		else if (_useSSO) {
			desc.append("SSO");
		}

		if (_useSales) {
			desc.append(" Sales");
		}
		else if (_useOrder) {
			desc.append(" Order");
		}

		return desc.toString();
	}

	/** Some groups want the ability to track numbers that are specific to their planner.
	*	These include Number of New Stores, Number of Electricians, etc...
	*	This description lets each group have its own
	*	@param String The description for the number of box
	*/
	public void setNumberOfDescription(String desc) {
		_numOfDesc = desc;
	}

	/** Retrieves the group id for this sales group
	*	@return String The group id for this sales group
	*/
	public String getId() {
		return _id;
	}

	/** Retrieves the description for this sales group
	*	@return String the description of this sales group
	*/
	public String getDescription() {
		return _desc;
	}

	/** Retrieves the file name for the logo of this sales group
	*	@return String The file name for the logo used by this sales group
	*/
	public String getLogo() {
		return _logo;
	}


	/** Indicates whether this sales group uses the unit volume
	*	@return boolean The indicator on viewing unit volume
	*/
	public boolean usesVolume() {
		return _showVolume;
	}

	/** Indicates whether this group uses sales as the primary view of the dollars
	*	@return boolean The indicator on whether this group uses sales
	*/
	public boolean usesSales() {
		return _useSales;
	}

	/** Indicates whether this group uses orders as the primary view of the dollars
	*	@return boolean The indicator on whether this group uses orders
	*/
	public boolean usesOrders() {
		return _useOrder;
	}

	/** Indicates whether this group uses credit dollars as the primary view of the dollars
	*	@return boolean The indicator on whether this group uses credit dollars
	*/
	public boolean usesCredit() {
		return _useCredit;
	}

	/** Indicates whether this group uses charge to dollars as the primary view of the dollars
	*	@return boolean The indicator on whether this group uses direct dollars
	*/
	public boolean usesDirect() {
		return _useDirect;
	}

	/** Indicates whether this group uses SSO dollars as the primary view of the dollars
	*	@return boolean The indicator on whether this group uses direct dollars
	*/
	public boolean usesSSO() {
		return _useSSO;
	}

	/** Indicates whether this group uses end market dollars as the primary view of the dollars
	*	@return boolean The indicator on whether this group uses end market dollars
	*/
	public boolean usesEndMkt() {
		return _useEndMkt;
	}

	/** Retrieves the number of description
	*	@return String The description for the number of box
	*/
	public String getNumberOfDescription() {
		return _numOfDesc;
	}

	public void setUseGlobalAcctMgr(String yn) {
		_usesGAM = yn.equals("Y");
	}

	public void setUseSlsmnPcntWithCust(String yn) {
		_usesPcntWithCust = yn.equals("Y");
	}

	public boolean usesGlobalAcctMgr() {
		return _usesGAM;
	}

	public boolean usesSlsmnPcntWithCust() {
		return _usesPcntWithCust;
	}


	public void setHasFullAccessForGroup(boolean hasFullAccessForGroup) {
		this.hasFullAccessForGroup=hasFullAccessForGroup;
		
	}
	public boolean getHasFullAccessForGroup() {
		return this.hasFullAccessForGroup;
		
	}

	public void setAllowedToDelete(boolean allowedToDelete) {
		this.allowedToDelete=allowedToDelete;
		
	}
	public boolean getAllowedToDelete() {
		return this.allowedToDelete;
		
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

		returnString += "\tdolDescription = " + this.getDolDescription() + "\n";
		returnString += "\tid = " + this.getId() + "\n";
		returnString += "\tdescription = " + this.getDescription() + "\n";
		returnString += "\tlogo = " + this.getLogo() + "\n";
		returnString += "\tnumberOfDescription = " + this.getNumberOfDescription() + "\n";
		returnString += "\thasFullAccessForGroup = " + this.getHasFullAccessForGroup() + "\n";
		returnString += "\tallowedToDelete = " + this.getAllowedToDelete() + "\n";

		return returnString;
	}
}
