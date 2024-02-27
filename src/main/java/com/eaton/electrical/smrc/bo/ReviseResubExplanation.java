package com.eaton.electrical.smrc.bo;

import java.util.Date;

public class ReviseResubExplanation {
	
	private int id;
	private int workflowApprovalId;
	private String explantion;
	private int targetProjectId;
	private String userAdded;
	private String userChanged;
	private Date dateAdded;
	private Date dateChanged;
	
	/**
	 * @return the dataChanged
	 */
	public Date getDateChanged() {
		return dateChanged;
	}
	/**
	 * @param dataChanged the dataChanged to set
	 */
	public void setDateChanged(Date dateChanged) {
		this.dateChanged = dateChanged;
	}
	/**
	 * @return the dateAdded
	 */
	public Date getDateAdded() {
		return dateAdded;
	}
	/**
	 * @param dateAdded the dateAdded to set
	 */
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	/**
	 * @return the explantion
	 */
	public String getExplantion() {
		return explantion;
	}
	/**
	 * @param explantion the explantion to set
	 */
	public void setExplantion(String explantion) {
		this.explantion = explantion;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the targetProjectId
	 */
	public int getTargetProjectId() {
		return targetProjectId;
	}
	/**
	 * @param targetProjectId the targetProjectId to set
	 */
	public void setTargetProjectId(int targetProjectId) {
		this.targetProjectId = targetProjectId;
	}
	/**
	 * @return the userAdded
	 */
	public String getUserAdded() {
		return userAdded;
	}
	/**
	 * @param userAdded the userAdded to set
	 */
	public void setUserAdded(String userAdded) {
		this.userAdded = userAdded;
	}
	/**
	 * @return the userChanged
	 */
	public String getUserChanged() {
		return userChanged;
	}
	/**
	 * @param userChanged the userChanged to set
	 */
	public void setUserChanged(String userChanged) {
		this.userChanged = userChanged;
	}
	/**
	 * @return the workflowApprovalId
	 */
	public int getWorkflowApprovalId() {
		return workflowApprovalId;
	}
	/**
	 * @param workflowApprovalId the workflowApprovalId to set
	 */
	public void setWorkflowApprovalId(int workflowApprovalId) {
		this.workflowApprovalId = workflowApprovalId;
	}
	
	
}
