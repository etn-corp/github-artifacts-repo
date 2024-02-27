package com.eaton.electrical.smrc.bo;

public class WorkflowPriorApprover {

	String workflowStepName;
	String priorApproverLastName;
	String priorApproverFirstName;
	String userId;
	int workflowApprovalId;
	
	
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
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the priorApproverFirstName
	 */
	public String getPriorApproverFirstName() {
		return priorApproverFirstName;
	}
	/**
	 * @param priorApproverFirstName the priorApproverFirstName to set
	 */
	public void setPriorApproverFirstName(String priorApproverFirstName) {
		this.priorApproverFirstName = priorApproverFirstName;
	}
	/**
	 * @return the priorApproverLastName
	 */
	public String getPriorApproverLastName() {
		return priorApproverLastName;
	}
	/**
	 * @param priorApproverLastName the priorApproverLastName to set
	 */
	public void setPriorApproverLastName(String priorApproverLastName) {
		this.priorApproverLastName = priorApproverLastName;
	}
	/**
	 * @return the workflowStepName
	 */
	public String getWorkflowStepName() {
		return workflowStepName;
	}
	/**
	 * @param workflowStepName the workflowStepName to set
	 */
	public void setWorkflowStepName(String workflowStepName) {
		this.workflowStepName = workflowStepName;
	}
	
	
}
