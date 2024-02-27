package com.eaton.electrical.smrc.bo;

import java.util.*;

/** This class will hold all of the data that is retrieved on one record
 *	of the modification_log in the Target Account Planner
 */
public class CustChange implements java.io.Serializable {
	private int _id = 0;
	private String _table = "";
	private String _field = "";
	private String _modType = "";
	private String _oldValue = "";
	private String _userid = "";
	private java.sql.Date _modDate;
	private java.sql.Time _modTime;
	private String _vcn = "";
	private String _prodId = "";
	private String _prodSubId = "";
	private String _indId = "";
	private String _taskId = "";
	private String _objId = "";
	private String _prodSvcId = "";
	private String _groupId = "";
	private String _custName = "";
	
	private static final long serialVersionUID = 100;

	public void setId(int id) {
		_id = id;
	}
	
	public void setTable(String data) {
		_table = data;
	}
	
	public void setField(String data) {
		_field = data;
	}
	
	public void setModType(String data) {
		_modType = data;
	}
	
	public void setOldValue(String data) {
		_oldValue = data;
	}
	
	public void setUserid(String data) {
		_userid = data;
	}
	
	public void setModDate(java.sql.Date data) {
		_modDate = data;
	}
	
	public void setModTime(java.sql.Time data) {
		_modTime = data;
	}
	
	public void setVistaCustNum(String data) {
		_vcn = data;
	}
	
	public void setProductId(String data) {
		_prodId = data;
	}
	
	public void setProdSublineId(String data) {
		_prodSubId = data;
	}
	
	public void setIndustryId(String data) {
		_indId = data;
	}
	
	public void setTaskId(String data) {
		_taskId = data;
	}
	
	public void setObjectiveId(String data) {
		_objId = data;
	}
	
	public void setProdServiceId(String data) {
		_prodSvcId = data;
	}
	
	public void setGroupId(String data) {
		_groupId = data;
	}
	
	public void setCustomerName(String name) {
		_custName = name;
	}
	
	public String getModTimeAsString() {
		Calendar date = Calendar.getInstance();
		date.setTime(_modDate);
		Calendar time = Calendar.getInstance();
		time.setTime(_modTime);
		
		String amPm = "AM";
		if (time.get(Calendar.AM_PM) == 1) {
			amPm = "PM";
		}
	
		String sMinute =null;
		int minute = time.get(Calendar.MINUTE);		
		if(minute<10){
			sMinute="0"+minute;
		}else{
			sMinute=""+minute;
		}
		
		return (date.get(Calendar.MONTH)+1) + "/" + date.get(Calendar.DATE) + "/" +
		date.get(Calendar.YEAR) + " at " + time.get(Calendar.HOUR ) + ":" +
		sMinute + " " + amPm;
		//return (date.get(Calendar.MONTH)+1) + "/" + date.get(Calendar.DATE) + "/" +
		//date.get(Calendar.YEAR) + " at " + date.getTime() + " - " + time.getTime();		
	}
	
	public int getId() {
		return _id;
	}
	
	public String getTable() {
		return _table;
	}
	
	public String getField() {
		return _field;
	}
	
	public String getModType() {
		return _modType;
	}
	
	public String getModTypeDescription() {
		if (_modType.equals("I")) {
			return "Added";
		}
		else if (_modType.equals("D")) {
			return "Deleted";
		}
		else if (_modType.equals("U")) {
			return "Updated";
		}
		
		return "";
	}
	
	public String getOldValue() {
		return _oldValue;
	}
	
	public String getUserid() {
		return _userid;
	}
	
	public String getVistaCustNum() {
		return _vcn;
	}
	
	public String getProductId() {
		return _prodId;
	}
	
	public String getProdSublineId() {
		return _prodSubId;
	}
	
	public String getIndustryId() {
		return _indId;
	}
	
	public String getTaskId() {
		return _taskId;
	}
	
	public String getObjectiveId() {
		return _objId;
	}
	
	public String getProdServiceId() {
		return _prodSvcId;
	}
	
	public String getGroupId() {
		return _groupId;
	}
	
	public String getCustomerName() {
		return _custName;
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

		returnString += "\tmodTimeAsString = " + this.getModTimeAsString() + "\n";
		returnString += "\tid = " + this.getId() + "\n";
		returnString += "\ttable = " + this.getTable() + "\n";
		returnString += "\tfield = " + this.getField() + "\n";
		returnString += "\tmodType = " + this.getModType() + "\n";
		returnString += "\tmodTypeDescription = " + this.getModTypeDescription() + "\n";
		returnString += "\toldValue = " + this.getOldValue() + "\n";
		returnString += "\tuserid = " + this.getUserid() + "\n";
		returnString += "\tvistaCustNum = " + this.getVistaCustNum() + "\n";
		returnString += "\tproductId = " + this.getProductId() + "\n";
		returnString += "\tprodSublineId = " + this.getProdSublineId() + "\n";
		returnString += "\tindustryId = " + this.getIndustryId() + "\n";
		returnString += "\ttaskId = " + this.getTaskId() + "\n";
		returnString += "\tobjectiveId = " + this.getObjectiveId() + "\n";
		returnString += "\tprodServiceId = " + this.getProdServiceId() + "\n";
		returnString += "\tgroupId = " + this.getGroupId() + "\n";
		returnString += "\tcustomerName = " + this.getCustomerName() + "\n";

		return returnString;
	}
}
