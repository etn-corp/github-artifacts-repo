package com.eaton.electrical.smrc.bo;


/** This class holds all of the data and methods needed for one Vista Security record
 @author Carl Abel
 @date July 10, 2002
 */
public class VSRecord implements java.io.Serializable {
	private String team = "";
	private String level = "";
	private String viewSalesman = "";
	private String maint = "";
	
	private static final long serialVersionUID = 100;
	
	/**
	 * I construct a VSRecord (Vista Security Record).
	 * 
	 * @param aTeam
	 * @param aLevel
	 * @param ableToViewSalesman
	 * @param ableToUpdate
	 */
	public VSRecord(String aTeam, String aLevel, String aViewSalesman, String aMaint) {      	    
	    setTeam(aTeam);
	    setLevel(aLevel);
	    setViewSalesman(aViewSalesman);
	    setMaint(aMaint);
	}
	
	
	/** Sets the "Team" for this security
	 *	<br>The team is how the security is actually loaded. It can be a sales id or a geography.
	 *	@param String The team
	 */
	private void setTeam (String team) {
		this.team = team.trim();
	}
	
	/** Sets the level for this security
	 *	<br>The level indicates where the security comes from
	 *	<ul>
	 *	<li>TEAM indicates that this user can see the customers at the team level
	 *	<li>DISTRICT indicates that this user can see summary information at the district level
	 *	<li>ZONE indicates that this user can see summary information at the ZONE level
	 *	</ul>
	 *
	 *	@param String The level
	 */
	private void setLevel (String level) {
		this.level = level.trim();
	}
	
	/** Indicates that this user is allowed to see all of the detailed information for the
	 *	given level of security. It will always be set to true for the TEAM level
	 *	@param String the Indicator
	 */
	private void setViewSalesman(String viewSalesman) {
		this.viewSalesman = viewSalesman.trim();
	}
	
	/** Indicates that this user is allowed to update data - not just view it
	 *	@param string The indicator
	 */
	private void setMaint(String maint) {
		this.maint = maint.trim();
	}
	
	/** Retrieves the team (sp_geog or sales id) for this record
	 *	@return String The team (sp_geog or sales id) for this record
	 */
	public String getTeam() {
		return team;
	}
	
	/** Retrieves the level for this record
	 *	<br>The level indicates where the security comes from
	 *	<ul>
	 *	<li>TEAM indicates that this user can see the customers at the team level
	 *	<li>DISTRICT indicates that this user can see summary information at the district level
	 *	<li>ZONE indicates that this user can see summary information at the ZONE level
	 *	<li>SALESID indicates that this user can see all customers for this sales id
	 *	</ul>
	 *	@return String The level for this record
	 */
	public String getLevel() {
		return level;
	}
	
	/** Retrieves the view salesman flag for this record
	 *	@return String The view salesman flag for this record
	 */
	public String getViewSalesman() {
		return viewSalesman;
	}
	
	/** Retrieves the maintenance flag for this record
	 *	@return String The maintenance flag for this record
	 */
	public String getMaint() {
		return maint;
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

		returnString += "\tteam = " + this.getTeam() + "\n";
		returnString += "\tlevel = " + this.getLevel() + "\n";
		returnString += "\tviewSalesman = " + this.getViewSalesman() + "\n";
		returnString += "\tmaint = " + this.getMaint() + "\n";

		return returnString;
	}
}
