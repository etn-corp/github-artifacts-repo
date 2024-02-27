package com.eaton.electrical.smrc.bo;



public class UserGeogSecurity implements java.io.Serializable {
	private String _userid = "";
	private String _geog = "";
	private String _level = "";
	private String _viewSalesman = "";
	private String _maint = "";
	private String _securityType = "";		// identifies whether the record came from Vista or was entered manually
	private String salesManName="", geogName="";
	private static final long serialVersionUID = 100;

	/** Lets user set the userid
	*	@param String Userid
	*/
	public void setUserid(String id) {
		_userid = id;
	}

	/** Lets user set the geography or sales id
	*	@param String geography or sales id
	*/
	public void setSPGeog(String geog) {
		_geog = geog.trim();

		if (_geog.length() == 4) {	// record is sales id - user gets full access
			setViewSalesman("Y");
			setMaintenance("Y");
		}
	}

	/** Lets user set the level of this record
	*	<br>The level indicates where the security comes from
	*	<ul>
	*	<li>TEAM indicates that this user can see the customers at the team level
	*	<li>DISTRICT indicates that this user can see summary information at the district level
	*	<li>ZONE indicates that this user can see summary information at the ZONE level
	*	</ul>
	*	@param String the level of this record
	*/
	public void setLevel(String level) {
		_level = level;
	}

	/** Accepts Y or N. Y indicates that userid is allowed to view details to the
	*	salesman level - ie, user can see salesman and customer information.
	*	N indicates that userid is only allowed to see summary information at the level
	*	@param String View Salesman indicator
	*/
	public void setViewSalesman(String slsmn) {
		_viewSalesman = slsmn;
	}

	/** Accepts Y or N. Y indicates that userid is allowed to do maintenance at the level
	*	indicated. N indicates that userid is not allowed to perform maintenance.
	*	@param String Maintenance indicator
	*/
	public void setMaintenance(String maint) {
		_maint = maint;
	}

	/** Lets user set the security type: M = Manual, V = Vista
	*	@param String security type
	*/
	public void setSecurityType(String st) {
		_securityType = st;
	}

	/** Sends user the userid
	*	@return String userid
	*/
	public String getUserid() {
		return _userid;
	}

	/** Sends user the geography or sales id
	*	@return String geography or sales id
	*/
	public String getSPGeog() {
		return _geog;
	}

	/** Sends user the level
	*	@return String level
	*/
	public String getLevel() {
		return _level;
	}

	/** Lets user know if this is a Vista record or not
	*	@return boolean indicator telling user if this is a Vista record or not
	*/
	public boolean recordFromVista() {
		return _securityType.equals("V");
	}

	/** Lets user know if this is a manual record or not
	*	@return boolean indicator telling user if this is a manual record or not
	*/
	public boolean manualRecord() {
		return _securityType.equals("M");
	}

	/** Checks level to determine if user can see team information
	*	@param boolean Indicator of whether user can see team info
	*/
	public boolean ableToSeeTeamLevel() {
		return _level.equals("TEAM") || isTeam() || isSalesId();
	}

	/** Checks level to determine if user can see district information
	*	@param boolean Indicator of whether user can see district info
	*/
	public boolean ableToSeeDistrictLevel() {
		return _level.equals("DISTRICT") || isDistrict() ||
			(!isTeam()&&!isSalesId()&&_level.equals("TEAM"));
	}

	/** Checks level to determine if user can see zone information
	*	@param boolean Indicator of whether user can see zone info
	*/
	public boolean ableToSeeZoneLevel() {
		return _level.equals("ZONE") || isZone() ||
			((isNational()||isGroup())&&!(_level.equals("NATIONAL")||_level.equals("GROUP")));
	}

	/** Checks level to determine if user can see group information
	*	@param boolean Indicator of whether user can see group info
	*/
	public boolean ableToSeeGroupLevel() {
		return _level.equals("GROUP") || isGroup() ||
			(isNational()&&!_level.equals("NATIONAL"));
	}

	/** Checks level to determine if user can see national information
	*	@param boolean Indicator of whether user can see national info
	*/
	public boolean ableToSeeNationalLevel() {
		return _level.equals("NATIONAL") || isNational();
	}

	/** Checks to see if user can do updates
	*	@param boolean Indicator of whether user can do updates
	*/
	public boolean ableToUpdate() {
		return _maint.equals("Y");
	}

	/** Checks to see if user can see salesman (customer) information
	*	@param boolean Indicator of whether user can see salesman (customer) info
	*/
	public boolean ableToViewSalesman() {
		return _viewSalesman.equals("Y");
	}

	public boolean isSalesId() {
		return (_geog.length() == 4);
	}

	public boolean isTeam() {
		return (_geog.length() == 6 && _geog.charAt(5) != ' ');
	}

	public boolean isDistrict() {
		return (_geog.length() == 5 && _geog.charAt(4) != '0');
	}

	public boolean isZone() {
		return (_geog.length() == 5 && _geog.charAt(4) == '0' &&
				(!(_geog.substring(2,4).equals("00"))));
	}

	public boolean isGroup() {
		return (_geog.length() == 5 && _geog.charAt(4) == '0' &&
				_geog.charAt(3) == '0' && _geog.charAt(2) == '0' &&
				_geog.charAt(1) != '0');
	}

	public boolean isNational() {
		return (_geog.length() == 5 && _geog.charAt(4) == '0' &&
				_geog.charAt(3) == '0' && _geog.charAt(2) == '0' &&
				_geog.charAt(1) == '0' && _geog.charAt(0) != '0');
	}


	public void setSalesmanName(String salesManName) {
		this.salesManName=salesManName;
		
	}
	public String getSalesmanName() {
		return this.salesManName;
		
	}

	public void setGeogName(String geogName) {
		this.geogName=geogName;
		
	}
	public String getGeogName() {
		return this.geogName;
		
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

		returnString += "\tuserid = " + this.getUserid() + "\n";
		returnString += "\tsPGeog = " + this.getSPGeog() + "\n";
		returnString += "\tlevel = " + this.getLevel() + "\n";
		returnString += "\tsalesmanName = " + this.getSalesmanName() + "\n";
		returnString += "\tgeogName = " + this.getGeogName() + "\n";

		return returnString;
	}
}
