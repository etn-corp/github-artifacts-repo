/*
 * Created on Nov 12, 2004
 *
 */
package com.eaton.electrical.smrc.bo;

/**
 * @author Jason Lubbert
 *
 */
public class TargetProjectReportBean implements java.io.Serializable {
	
	String internalStatus = null;
	String district = null;
	String jobName = null;
	String status = null;
	String targetReason = null;
	String specPreference = null;
	java.util.Date bidDate = null;
	double egValue = 0;
	double totalValue = 0;
	int targetProjectId = 0;
	String geog = null;
	int statusId = 0;
	int specPreferenceId = 0;
	int targetReasonId = 0;
	String internalStatusId = null;
	Object sortField = null;
	String sortFieldType = null;
	
	
	private static final long serialVersionUID = 100;

	public TargetProjectReportBean(){
		internalStatus = "";
		district = "";
		jobName = "";
		status = "";
		targetReason = "";
		specPreference = "";
		geog = "";
		internalStatusId = "";
		sortFieldType = "";
		
	}

	/**
	 * @return Returns the bidDate.
	 */
	public java.util.Date getBidDate() {
		return bidDate;
	}
	/**
	 * @param bidDate The bidDate to set.
	 */
	public void setBidDate(java.util.Date bidDate) {
		this.bidDate = bidDate;
	}
	/**
	 * @return Returns the district.
	 */
	public String getDistrict() {
		return district;
	}
	/**
	 * @param district The district to set.
	 */
	public void setDistrict(String district) {
		this.district = district;
	}
	/**
	 * @return Returns the egValue.
	 */
	public double getEgValue() {
		return egValue;
	}
	/**
	 * @param egValue The egValue to set.
	 */
	public void setEgValue(double egValue) {
		this.egValue = egValue;
	}
	/**
	 * @return Returns the internalStatus.
	 */
	public String getInternalStatus() {
		if (internalStatusId.equalsIgnoreCase("A")) {
			internalStatus =  "Active";
		} else if (internalStatusId.equalsIgnoreCase("M")) {
			internalStatus =  "Awaiting CHAMPS Mgr Approval";
		} else if (internalStatusId.equalsIgnoreCase("Z")) {
			internalStatus =  "Awaiting Project Sales Mgr Approval";
		} else if (internalStatusId.equalsIgnoreCase("N")) {
			internalStatus =  "Awaiting District Mgr Approval";
		} else if (internalStatusId.equalsIgnoreCase("D")) {
			internalStatus =  "Deleted";
		} else {
			internalStatus =  "Unknown";
		}
			
		return internalStatus;
	}
	/**
	 * @param internalStatus The internalStatus to set.
	 */
	public void setInternalStatus(String internalStatus) {
		this.internalStatus = internalStatus;
	}
	/**
	 * @return Returns the jobName.
	 */
	public String getJobName() {
		return jobName;
	}
	/**
	 * @param jobName The jobName to set.
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	/**
	 * @return Returns the specPreference.
	 */
	public String getSpecPreference() {
		return specPreference;
	}
	/**
	 * @param specPreference The specPreference to set.
	 */
	public void setSpecPreference(String specPreference) {
		this.specPreference = specPreference;
	}
	/**
	 * @return Returns the status.
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return Returns the targetReason.
	 */
	public String getTargetReason() {
		return targetReason;
	}
	/**
	 * @param targetReason The targetReason to set.
	 */
	public void setTargetReason(String targetReason) {
		this.targetReason = targetReason;
	}
	/**
	 * @return Returns the totalValue.
	 */
	public double getTotalValue() {
		return totalValue;
	}
	/**
	 * @param totalValue The totalValue to set.
	 */
	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}
	/**
	 * @return Returns the geog.
	 */
	public String getGeog() {
		return geog;
	}
	/**
	 * @param geog The geog to set.
	 */
	public void setGeog(String geog) {
		this.geog = geog;
	}
	/**
	 * @return Returns the internalStatusId.
	 */
	public String getInternalStatusId() {
		return internalStatusId;
	}
	/**
	 * @param internalStatusId The internalStatusId to set.
	 */
	public void setInternalStatusId(String internalStatusId) {
		this.internalStatusId = internalStatusId;
	}
	/**
	 * @return Returns the specPrferenceId.
	 */
	public int getSpecPreferenceId() {
		return specPreferenceId;
	}
	/**
	 * @param specPrferenceId The specPrferenceId to set.
	 */
	public void setSpecPreferenceId(int specPreferenceId) {
		this.specPreferenceId = specPreferenceId;
	}
	/**
	 * @return Returns the statusId.
	 */
	public int getStatusId() {
		return statusId;
	}
	/**
	 * @param statusId The statusId to set.
	 */
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	/**
	 * @return Returns the targetProjectId.
	 */
	public int getTargetProjectId() {
		return targetProjectId;
	}
	/**
	 * @param targetProjectId The targetProjectId to set.
	 */
	public void setTargetProjectId(int targetProjectId) {
		this.targetProjectId = targetProjectId;
	}
	/**
	 * @return Returns the targetReasonId.
	 */
	public int getTargetReasonId() {
		return targetReasonId;
	}
	/**
	 * @param targetReasonId The targetReasonId to set.
	 */
	public void setTargetReasonId(int targetReasonId) {
		this.targetReasonId = targetReasonId;
	}
	/**
	 * @return Returns the sortField.
	 */
	public Object getSortField() {
		return sortField;
	}
	/**
	 * @param sortField The sortField to set.
	 */
	public void setSortField(Object sortField, String sortFieldType) {
		this.sortField = sortField;
		this.sortFieldType = sortFieldType;
	}
	/**
	 * @return Returns the sortField.
	 */
	public String getSortFieldType() {
		return sortFieldType;
	}
}
