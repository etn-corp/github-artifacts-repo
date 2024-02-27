package com.eaton.electrical.smrc.bo;

import java.util.*;


/**
 * @author E0062708
 *
 */
public class WorkflowStep implements java.io.Serializable {
	
	private String vcn = null;
	private String customerName = null;
	private int approvalId;
	private int stepId;
	private String stepName = null;
	private int prevStepId;
	private int workflowId;
	private int sequenceId;
	private String workflowName = null;	
	private String userAdded = null;
	private String userChanged = null;
	private User userApprovedOrRejected = null;
	private Date dateAdded = null;
	private Date dateChanged = null;
	private Date previousStepApprovalDate = null;
	private String statusFlag = null;
	// User added on customer record
	private String customerUserAdded = null;
	private String customerGeog = null;
	private long moduleChangeRequestId = 0;
	
	private static final long serialVersionUID = 100;
	
	public static final String STEP_NAME_INITIAL_ACCOUNT_INFO = "Initial Account Information";
	public static final String STEP_NAME_DISTRICT_MANAGER = "District Manager";
	public static final String STEP_NAME_ZONE_MANAGER = "Zone Manager";
	public static final String STEP_NAME_GLOBAL_CHANNEL_MANAGER = "Global Channel Manager";
	public static final String STEP_NAME_CREDIT_MANAGER = "Credit Manager";

	public WorkflowStep(){
		vcn="";
		customerName="";
		stepName = "";
		workflowName="";
		userAdded="";
		userAdded="";
		userApprovedOrRejected = new User();
		statusFlag = "";
		customerUserAdded = "";
		customerGeog = "";
	}
	
	public int getPrevStepId() {
		return prevStepId;
	}
	public void setPrevStepId(int prevStepId) {
		this.prevStepId = prevStepId;
	}
	public int getStepId() {
		return stepId;
	}
	public void setStepId(int stepId) {
		this.stepId = stepId;
	}
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public String getVcn() {
		return vcn;
	}
	public void setVcn(String vcn) {
		this.vcn = vcn;
	}
	public int getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(int workflowId) {
		this.workflowId = workflowId;
	}
	public String getWorkflowName() {
		return workflowName;
	}
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}
	public int getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}
	public boolean isApproved() {
		if(getStatusFlag().equals("Y")){
			return true;
		}
		return false;
	}

	public Date getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	public Date getDateChanged() {
		return dateChanged;
	}
	public void setDateChanged(Date dateChanged) {
		this.dateChanged = dateChanged;
	}
	public String getUserAdded() {
		return userAdded;
	}
	public void setUserAdded(String userAdded) {
		this.userAdded = userAdded;
	}
	public String getUserChanged() {
		return userChanged;
	}
	public void setUserChanged(String userChanged) {
		this.userChanged = userChanged;
	}
	public int getApprovalId() {
		return approvalId;
	}
	public void setApprovalId(int approvalId) {
		this.approvalId = approvalId;
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

		returnString += "\tprevStepId = " + this.getPrevStepId() + "\n";
		returnString += "\tstepId = " + this.getStepId() + "\n";
		returnString += "\tstepName = " + this.getStepName() + "\n";
		returnString += "\tvcn = " + this.getVcn() + "\n";
		returnString += "\tworkflowId = " + this.getWorkflowId() + "\n";
		returnString += "\tworkflowName = " + this.getWorkflowName() + "\n";
		returnString += "\tsequenceId = " + this.getSequenceId() + "\n";
		returnString += "\tdateAdded = " + this.getDateAdded() + "\n";
		returnString += "\tdateChanged = " + this.getDateChanged() + "\n";
		returnString += "\tuserAdded = " + this.getUserAdded() + "\n";
		returnString += "\tuserChanged = " + this.getUserChanged() + "\n";
		returnString += "\tapprovalId = " + this.getApprovalId() + "\n";

		return returnString;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Date getPreviousStepApprovalDate() {
		return previousStepApprovalDate;
	}
	public void setPreviousStepApprovalDate(Date previousStepApprovalDate) {
		this.previousStepApprovalDate = previousStepApprovalDate;
	}
	public User getUserApprovedOrRejected() {
		return userApprovedOrRejected;
	}
	public void setUserApprovedOrRejected(User userApproved) {
		this.userApprovedOrRejected = userApproved;
	}
	public boolean isRejected() {
		if(getStatusFlag().equals("R")){
			return true;
		}
		return false;
	}
	
	public boolean isRevisedAndResubmitted() {
		if(getStatusFlag().equals("V")){
			return true;
		}
		return false;
	}

	public String getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}
	
	public String getStatusDescription(){
		if (this.isApproved()){
			return "Approved";
		} else if (this.isRejected()){
			return "Rejected";
		} else if (this.isRevisedAndResubmitted()){
			return "Requested revision";
		} else {
			return "No action taken";
		}
	}

	public String getCustomerUserAdded() {
		return customerUserAdded;
	}

	public void setCustomerUserAdded(String customerUserAdded) {
		this.customerUserAdded = customerUserAdded;
	}

	public String getCustomerGeog() {
		return customerGeog;
	}

	public void setCustomerGeog(String customerGeog) {
		this.customerGeog = customerGeog;
	}

	public long getModuleChangeRequestId() {
		return moduleChangeRequestId;
	}

	public void setModuleChangeRequestId(long moduleChangeRequestId) {
		this.moduleChangeRequestId = moduleChangeRequestId;
	}
}
