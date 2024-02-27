package com.eaton.electrical.smrc.bo;

/**
 * @author E0062708
 *
 */
public class Geography implements java.io.Serializable {
	private String geog = null;
	private String salesOrg = null;
	private String groupCode = null;
	private String zone = null;
	private String district = null;
	private String team = null;
	private String description = null;
	
	private static final long serialVersionUID = 100;

	public Geography(){
		geog = "";
		description = "";
		salesOrg = "";
		groupCode = "";
		zone = "";
		district = "";
		team = "";
		
	}
	public Geography(String geog, String description){
		this.geog = geog;
		this.description = description;
		salesOrg = "";
		groupCode = "";
		zone = "";
		district = "";
		team = "";
	}

	public Geography(String geog){
		this.geog = geog;
		description = "";
		salesOrg = "";
		groupCode = "";
		zone = "";
		district = "";
		team = "";

	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDistrict() {
		if(district.equals("")){
			if(geog.length()>4){
				return String.valueOf(geog.charAt(4));
			}
			return "";

		}
		return district;
		
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getGroupCode() {
		if(groupCode.equals("")){
			if(geog.length()>1){
				return String.valueOf(geog.charAt(1));	
			}
			return "";

		}
		return groupCode;		
	}
	public String getSalesOrg() {
		if(salesOrg.equals("")){
			if(geog.length()>0){
				return String.valueOf(geog.charAt(0));
			}
			return "";
		}
		return salesOrg;
	}
	public String getGeog() {
		return geog;
	}
	public void setGeog(String geog) {
		this.geog = geog.trim();
	}
	public String getTeam() {
		if(team.equals("")){
			if(geog.length()>5){
				return String.valueOf(geog.charAt(5));
			}
			return "";
		}
		return team;		
	}
	public String getZone() {
		if(zone.equals("")){
			if(geog.length()>3){
				return String.valueOf(geog.charAt(2)) + String.valueOf(geog.charAt(3));
			}
				return "";
		}
		return zone;
	}
	
	
	/* TODO remove all these if never needed
	private boolean isSalesId() {
		return (geog.length() == 4);
	}

	private boolean isTeam() {
		return (geog.length() == 6 && geog.charAt(5) != ' ');
	}

	private boolean isDistrict() {
		return (geog.length() == 5 && geog.charAt(4) != '0');
	}

	private boolean isZone() {
		return (geog.length() == 5 && geog.charAt(4) == '0' &&
				geog.charAt(3) != '0' && geog.charAt(2) != '0');
	}

	private boolean isGroup() {
		return (geog.length() == 5 && geog.charAt(4) == '0' &&
				geog.charAt(3) == '0' && geog.charAt(2) == '0' &&
				geog.charAt(1) != '0');
	}

	private boolean isNational() {
		return (geog.length() == 5 && geog.charAt(4) == '0' &&
				geog.charAt(3) == '0' && geog.charAt(2) == '0' &&
				geog.charAt(1) == '0' && geog.charAt(0) != '0');
	}
	*/
	
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	
	public void getDistrictManager() {
		
		
		
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

		returnString += "\tdescription = " + this.getDescription() + "\n";
		returnString += "\tdistrict = " + this.getDistrict() + "\n";
		returnString += "\tgroupCode = " + this.getGroupCode() + "\n";
		returnString += "\tsalesOrg = " + this.getSalesOrg() + "\n";
		returnString += "\tgeog = " + this.getGeog() + "\n";
		returnString += "\tteam = " + this.getTeam() + "\n";
		returnString += "\tzone = " + this.getZone() + "\n";

		return returnString;
	}
}
