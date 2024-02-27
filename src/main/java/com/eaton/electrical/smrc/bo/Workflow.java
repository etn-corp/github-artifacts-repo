package com.eaton.electrical.smrc.bo;

import java.sql.*;
import java.util.*;

import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.util.*;
import com.eaton.electrical.smrc.service.*;


/**
 * @author E0062708
 *
 */
public class Workflow implements java.io.Serializable {
	boolean complete = false;
	ArrayList workflowSteps = null;
	String workflowType = null;
	
	private static final long serialVersionUID = 100;
	public static final String WORKFLOW_TYPE_DISTRIBUTOR_TERMINATION = "Distributor Termination";
	public static final String WORKFLOW_TYPE_DISTRIBUTOR_APPLICATION = "Distributor Application";
	public static final String WORKFLOW_TYPE_CUSTOMER = "Customer"; 
	public static final String WORKFLOW_TYPE_TARGET_MARKET_PLANS = "Target Market Plans";
	public static final String WORKFLOW_PRODUCT_MODULE = "Product Module";

	public Workflow(){
		workflowSteps = new ArrayList();
		setWorkflowType("");
	}

	public boolean isComplete() {
		for(int i=0;i<workflowSteps.size();i++){
			WorkflowStep step = (WorkflowStep)workflowSteps.get(i);
			if(!step.isApproved()){
				return false;
			}
		}
		return true;
	}
	public ArrayList getWorkflowSteps() {
		return workflowSteps;
	}
	public void setWorkflowSteps(ArrayList workflowSteps) {
		this.workflowSteps = workflowSteps;
	}
	public String getWorkflowType() {
		return workflowType;
	}
	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}
	/*
	public ArrayList getCurrentSteps(){
		ArrayList currentSteps= new ArrayList();
		for(int i=0;i<workflowSteps.size();i++){
			WorkflowStep step = (WorkflowStep)workflowSteps.get(i);
			
			for(int j=0;j<workflowSteps.size(); j++){
				WorkflowStep step2 = (WorkflowStep)workflowSteps.get(j);
				
				if(step.getPrevStepId()==step2.getStepId() && step2.isApproved() && !step.isApproved()){
					currentSteps.add(step);
				
				}
			}
		}
		return currentSteps;
	}
	*/
	public boolean isLastApproval(){
		int remainingApprovals=0;
		for(int i=0;i<workflowSteps.size();i++){
			WorkflowStep step = (WorkflowStep)workflowSteps.get(i);
			if(!step.isApproved()){
				remainingApprovals++;
				if(remainingApprovals>1){
					return false;
				}
			}
		}
		if(remainingApprovals==1){
			return true;
		}
		return false;

	}
	
	public boolean isRejected(){
		for(int i=0;i<workflowSteps.size();i++){
			WorkflowStep step = (WorkflowStep)workflowSteps.get(i);
			if(step.isRejected()){
				return true;
			}
		}
		return false;
	}
	

	public void sendNotifications(Account acct, User user,  Connection DBConn) throws Exception {
		char newline = 13;
		if(!isComplete()){
			ArrayList steps = getWorkflowSteps();
			
			for(int i=0;i<steps.size();i++){
				WorkflowStep step = (WorkflowStep)steps.get(i);

				for(int j=0;j<steps.size(); j++){
					WorkflowStep step2 = (WorkflowStep)steps.get(j);
					if(step.getPrevStepId()==step2.getStepId() && step2.isApproved() && !step.isApproved()){
						TAPMail tapmail = new TAPMail();
						
						StringBuffer msg = new StringBuffer();
						msg.append("The following account in Target Account Planner is ready for an approval.  If you have already received an email notification about this account then a different step has been approved and an approval is still pending for you." + newline + newline);
						msg.append("Approval Type - " + getWorkflowType() + newline);
						msg.append("Account - " + acct.getCustomerName());
						msg.append(" (" + acct.getBusinessAddress().getCity() + ", " + acct.getBusinessAddress().getState() + ") ");
						msg.append(" " + newline + newline);
						
						if(step.getStepName().equals("District Manager")){
							if(!acct.getDistrictManager().getEmailAddress().equals("")){
								msg.append("Step - District Manager" + newline + newline);
								tapmail.addRecipient(acct.getDistrictManager().getEmailAddress());
							}
						}else if(step.getStepName().equals("Zone Manager")){
							if(!acct.getZoneManager().getEmailAddress().equals("")){
								msg.append("Step - Zone Manager" + newline + newline);
								tapmail.addRecipient(acct.getZoneManager().getEmailAddress());
							}
							if (acct.isDistributor()){
							    // Add code here for notifying GSS Pricing and GSS training - do not add them to workflow!
							    tapmail.addCCRecipient(Globals.getGSSPricingEmailAddress(DBConn));
							    tapmail.addCCRecipient(Globals.getGSSTrainingEmailAddress(DBConn));
							}

						}else if(step.getStepName().equals("Global Channel Manager")){
							ArrayList peeps = UserDAO.getUsersInGroup("Global Channel Manager",DBConn);
							for(int k=0;k<peeps.size();k++){
								User peep = (User)peeps.get(k);
								tapmail.addRecipient(peep.getEmailAddress());
							}
							tapmail.addRecipient(Globals.getChannelTAPRequestsEmailAddress(DBConn));
							msg.append("Step - Global Channel Manager *" + newline + newline);
							msg.append("* Note - Any Global Channel Manager can approve this account, so you may find it has already been approved." + newline + newline);							
						}else if(step.getStepName().equals("Credit Manager")){
							ArrayList peeps = UserDAO.getUsersInGroup("Credit Manager",DBConn);
							for(int k=0;k<peeps.size();k++){
								User peep = (User)peeps.get(k);
								tapmail.addRecipient(peep.getEmailAddress());
								
							}
							msg.append("Step - Credit Manager *" + newline + newline);
							msg.append("* Note - Any Credit Manager can approve this account, so you may find it has already been approved." + newline + newline);							
						}

						msg.append("To approve this account, log into JOE and access Target Account Planner via the Sales Marketing Resource Channel." + newline);
						tapmail.setText(msg.toString());
						tapmail.setSenderInfo("Target Account Planner", user.getEmailAddress());
						tapmail.setSubject("Target Account Planner - Account Approval Pending");
						tapmail.sendMessage();

					}
					
				}
				
				
			}
		} else {
		    // workflow is complete
		    TAPMail tapmail = new TAPMail();
		    StringBuffer msg = new StringBuffer();
		    ArrayList steps = getWorkflowSteps();
		    if (getWorkflowType().equalsIgnoreCase("Distributor Termination")){
		        msg.append(acct.getCustomerName() + " (" + acct.getVcn() + ") has received all necessary approvals for termination." + newline + newline);
		        tapmail.setSubject("Target Account Planner - Account Terminated");
		        tapmail.addCCRecipient(Globals.getGSSPricingEmailAddress(DBConn));
		    } else {
		    	msg.append(acct.getCustomerName());
		    	msg.append(" (" + acct.getBusinessAddress().getCity() + ", " + acct.getBusinessAddress().getState() + ") ");
				msg.append(" has received all necessary approvals and has been sent to Vista.");
		        msg.append(" " + newline + newline);
		        tapmail.setSubject("Target Account Planner - Account sent to Vista");
			}
		    for(int i=0;i<steps.size();i++){
			    WorkflowStep thisStep = (WorkflowStep) steps.get(i);
			    msg.append(newline + thisStep.getStepName() + ": " + UserDAO.getUserName(thisStep.getUserChanged(),DBConn) + " (" + thisStep.getDateChanged() + ")");
			    tapmail.addRecipient(UserDAO.getUserEmailAddress(thisStep.getUserChanged(),DBConn));
			}
		    if (acct.isDistributor()){
		        // Send to all global channel managers
				ArrayList channelMgrs = UserDAO.getUsersInGroup("Global Channel Manager",DBConn);
				for(int i=0; i<channelMgrs.size(); i++){
					User mgr = (User)channelMgrs.get(i);
					tapmail.addRecipient(mgr.getEmailAddress());
				}
				tapmail.addCCRecipient(Globals.getEnterpriseSystemsSecurityEmailAddress(DBConn));
				tapmail.addCCRecipient(Globals.getECommerceIntegrationTeamEmailAddress(DBConn));
				tapmail.addCCRecipient(Globals.getChannelTAPRequestsEmailAddress(DBConn));
		    }
			tapmail.setText(msg.toString());
			tapmail.setSenderInfo("Target Account Planner", user.getEmailAddress());
			tapmail.sendMessage();
		    
		}
	}
	

	public void sendRejectionNotification(Account acct, User user, Connection DBConn) throws Exception {
		char newline = 13;

		if(!isComplete()){
		    ArrayList steps = getWorkflowSteps();
			TAPMail tapmail = new TAPMail();
					
			StringBuffer msg = new StringBuffer();
			if (getWorkflowType().equalsIgnoreCase("Distributor Termination")){
			    msg.append("Termination of this account has been rejected in Target Account Planner." + newline + newline);
			    tapmail.setSubject("Target Account Planner - Account Termination Rejected");
			} else {
			    msg.append("The following account in Target Account Planner has been rejected." + newline + newline);
			    tapmail.setSubject("Target Account Planner - Account Rejected");
			}
			msg.append("Approval Type - " + getWorkflowType() + newline);
			msg.append("Account - " + acct.getCustomerName());
			msg.append(" (" + acct.getBusinessAddress().getCity() + ", " + acct.getBusinessAddress().getState() + ") ");
			msg.append(newline);
			for(int i=0;i<steps.size();i++){
			    WorkflowStep thisStep = (WorkflowStep) steps.get(i);
			    msg.append(newline + thisStep.getStepName() + ": " + UserDAO.getUserName(thisStep.getUserChanged(),DBConn));
			    if (thisStep.getDateChanged() != null){
			        msg.append(" (" + thisStep.getDateChanged() + ")");
			    }
			    if (thisStep.getUserChanged() != null && thisStep.isRejected()){
			        msg.append("  Rejected");
			        tapmail.addRecipient(UserDAO.getUserEmailAddress(thisStep.getUserChanged(),DBConn));
				} else if (thisStep.getUserChanged() != null && thisStep.isApproved()){
			        msg.append("  Approved");
			        tapmail.addRecipient(UserDAO.getUserEmailAddress(thisStep.getUserChanged(),DBConn));
			    }
			}

			tapmail.setText(msg.toString());
			tapmail.setSenderInfo("Target Account Planner", user.getEmailAddress());
			tapmail.sendMessage();
			
		}	
	}

	public void sendReviseAndResubmitNotification(Account acct, User sendToUser, User sendByUser, Connection DBConn, String reviseResubmitExplantion) throws Exception {
		char newline = 13;
		
		if(!isComplete()){
		    //ArrayList steps = getWorkflowSteps();
			TAPMail tapmail = new TAPMail();
					
			StringBuffer msg = new StringBuffer();
			if (getWorkflowType().equalsIgnoreCase("Distributor Termination")){
			    msg.append("The following account in Target Account Planner has been sent back to you for the reasons listed below. Update the information in TAP, save, and then approve or resubmit to any prior approver to update the necessary information. Once information has been updated and saved, click the Send to VISTA link and 'Approve' the account. If you have already received an email notification about this account then a different step has been approved and an approval is still pending for you." + newline + newline);
			    tapmail.setSubject("Target Account Planner - Account Termination Information Needs Revised");
			} else {
			    msg.append("The following account in Target Account Planner has been sent back to you for the reasons listed below. Update the information in TAP, save, and then approve or resubmit to any prior approver to update the necessary information. Once information has been updated and saved, click the Send to VISTA link and 'Approve' the account. If you have already received an email notification about this account then a different step has been approved and an approval is still pending for you." + newline + newline);
			    tapmail.setSubject("Target Account Planner - Account Information Needs Revised");
			}
			msg.append("Approval Type - " + getWorkflowType() + newline);
			msg.append("Account - " + acct.getCustomerName());
			msg.append(" (" + acct.getBusinessAddress().getCity() + ", " + acct.getBusinessAddress().getState() + ") ");
			msg.append(newline);

	        msg.append(newline + "  Step - Pending Resubmission * " + newline + newline);
	        msg.append("* Note - Any Global Channel Manager can approve this account, so you may find it has already been approved. " + newline + newline);
	        msg.append(reviseResubmitExplantion + newline + newline);
	        msg.append("To approve this account, log into JOE and access Target Account Planner via the Sales Marketing Resource Channel.");

	        tapmail.addRecipient(sendToUser.getEmailAddress());

			tapmail.setText(msg.toString());

			tapmail.setSenderInfo("Target Account Planner", sendByUser.getEmailAddress());
			tapmail.sendMessage();
			
		}	
	}
	
	public void sendTargetMarketReviseAndResubmitNotification(Account acct, User user, Connection DBConn, String reviseResubmitExplantion) throws Exception {
		char newline = 13;
		
		if(!isComplete()){
			
		    ArrayList steps = getWorkflowSteps();
			TAPMail tapmail = new TAPMail();
					
			StringBuffer msg = new StringBuffer();
			if (getWorkflowType().equalsIgnoreCase("Distributor Termination")){
			    msg.append("The following account in Target Account Planner has been sent back to you for the reasons listed below. Update the information in TAP, save, and then approve or resubmit to any prior approver to update the necessary information. Once information has been updated and saved, click the Send to VISTA link and 'Approve' the account. If you have already received an email notification about this account then a different step has been approved and an approval is still pending for you." + newline + newline);
			    tapmail.setSubject("Target Account Planner - Account Termination Information Needs Revised");
			} else {
			    msg.append("The following account in Target Account Planner has been sent back to you for the reasons listed below. Update the information in TAP, save, and then approve or resubmit to any prior approver to update the necessary information. Once information has been updated and saved, click the Send to VISTA link and 'Approve' the account. If you have already received an email notification about this account then a different step has been approved and an approval is still pending for you." + newline + newline);
			    tapmail.setSubject("Target Account Planner - Account Information Needs Revised");
			}
			msg.append("Approval Type - " + getWorkflowType() + newline);
			msg.append("Account - " + acct.getCustomerName());
			msg.append(" (" + acct.getBusinessAddress().getCity() + ", " + acct.getBusinessAddress().getState() + ") ");
			msg.append(newline);
			
			int stepsSize = steps.size();
			
			for(int i=0;i<stepsSize;i++){
				
			    WorkflowStep thisStep = (WorkflowStep) steps.get(i);
			    //msg.append(newline + thisStep.getStepName() + ": " + UserDAO.getUserName(thisStep.getUserChanged(),DBConn));
			    
			    //if (thisStep.getDateChanged() != null){
			        //msg.append(" (" + thisStep.getDateChanged() + ")");
			    //}

			    if (thisStep.getUserChanged() != null && thisStep.isRejected()){
			        msg.append("  Rejected");
			        tapmail.addRecipient(UserDAO.getUserEmailAddress(thisStep.getUserChanged(),DBConn));
				} else if (thisStep.getUserChanged() != null && thisStep.isApproved()){
			        msg.append("  Approved");
			        tapmail.addRecipient(UserDAO.getUserEmailAddress(thisStep.getUserChanged(),DBConn));
			    }
				else if (thisStep.getUserChanged() != null && thisStep.isRevisedAndResubmitted()){

			        msg.append(newline + "  Step - Pending Resubmission * " + newline + newline);
			        msg.append("* Note - Any Global Channel Manager can approve this account, so you may find it has already been approved. " + newline + newline);
			        msg.append(reviseResubmitExplantion + newline + newline);
			        msg.append("To approve this account, log into JOE and access Target Account Planner via the Sales Marketing Resource Channel.");

			        tapmail.addRecipient(UserDAO.getUserEmailAddress(thisStep.getUserChanged(),DBConn));
			    }
			}

			tapmail.setText(msg.toString());
			
			tapmail.setSenderInfo("Target Account Planner", user.getEmailAddress());
			tapmail.sendMessage();
			
		}	
	}
	
	
	public void sendDuplicateSelectedNotification (Account acct, String duplicateVCN, User user, Connection DBConn) throws Exception {
		char newline = 13;
		TAPMail tapmail = new TAPMail();
		Account duplicateAccount = AccountDAO.getAccount(duplicateVCN,DBConn);
		StringBuffer message = new StringBuffer();
		
		// Get cc: list from users that have added or approved this workflow 
		ArrayList workflowSteps = this.getWorkflowSteps();
		HashSet approvers = new HashSet();
		for (int i=0; i < workflowSteps.size(); i++){
			WorkflowStep step = (WorkflowStep) workflowSteps.get(i);
			if (step.getUserChanged() != null && !step.getUserChanged().trim().equals("")){
				approvers.add(step.getUserChanged());
			}
			approvers.add(step.getUserAdded());
		}
		Iterator iter = approvers.iterator();
		while (iter.hasNext()){
			String userid = (String) iter.next();
			User approver = UserDAO.getUser(userid,DBConn);
			if (approver != null && approver.getEmailAddress() != null && !approver.getEmailAddress().trim().equals("")){
				tapmail.addRecipient(approver.getEmailAddress());
			}
		}
		
		tapmail.addCCRecipient(Globals.getChannelTAPRequestsEmailAddress(DBConn));
		
		message.append(user.getFullName() + " has determined that " + acct.getCustomerName() + " (" + acct.getVcn() + ") is a duplicate of the following customer:" + newline);
		message.append(newline + duplicateAccount.getCustomerName() + " (" + duplicateAccount.getVcn() + ") " + newline);
		message.append(duplicateAccount.getBusinessAddress().getAddress1() + newline);
		if (duplicateAccount.getBusinessAddress().getAddress2() != null && !duplicateAccount.getBusinessAddress().getAddress2().trim().equals("")){
			message.append(duplicateAccount.getBusinessAddress().getAddress2() + newline);
		}
		if (duplicateAccount.getBusinessAddress().getAddress3() != null && !duplicateAccount.getBusinessAddress().getAddress3().trim().equals("")){
			message.append(duplicateAccount.getBusinessAddress().getAddress3() + newline);
		}
		if (duplicateAccount.getBusinessAddress().getAddress4() != null && !duplicateAccount.getBusinessAddress().getAddress4().trim().equals("")){
			message.append(duplicateAccount.getBusinessAddress().getAddress4() + newline);
		}
		message.append(duplicateAccount.getBusinessAddress().getCity() + ", " + duplicateAccount.getBusinessAddress().getState() + "  " + duplicateAccount.getBusinessAddress().getZip() + newline);
		message.append(newline + "No further action is required. If this was a distributor setup, the profile for " + duplicateAccount.getVcn() + " will be updated to match the TAP profile for " + acct.getVcn() + ".");
		
		tapmail.setSubject("Target Account Planner - " + acct.getCustomerName() + " Not sent to Vista");
		tapmail.setText(message.toString());
		tapmail.setSenderInfo("Target Account Planner", user.getEmailAddress());
		tapmail.sendMessage();
		
	}
	
	public WorkflowStep[] getCurrentSteps(){
    	
    	WorkflowStep[] orderedSteps = this.orderWorkflowSteps();
    	ArrayList currentStepList = new ArrayList();
    	for (int i=0; i < orderedSteps.length; i++){
    		WorkflowStep step  = orderedSteps[i];
    		if (step.getStatusFlag().trim().equalsIgnoreCase("V")){
    			// If workflow was sent back to this step for revision, this is the current step
    			currentStepList.add(step);
    		} else if (step.getStatusFlag().trim().equalsIgnoreCase("N")){
    			// If not yet approved or rejected, could be current step
    			if (i == 0){
    				// First step in workflow
    				currentStepList.add(step);
    			} else {
    				// previous step was approved, but this one is still N
    				if (orderedSteps[i-1].getStatusFlag().trim().equalsIgnoreCase("Y")){
    					currentStepList.add(step);
    				}
    			}
    		}
    	}
    	
    	
    	int size = currentStepList.size();
    	WorkflowStep[] currentSteps = new WorkflowStep[size];
    	for (int i=0; i < currentStepList.size(); i++){
    		currentSteps[i] = (WorkflowStep) currentStepList.get(i);
    	}
    	return currentSteps;
    }
    
//  Orders the WorkflowStepSequences based on current step/previous step
    public WorkflowStep[] orderWorkflowSteps(){
    	// Initialize orderedList with current order so the complete list is
    	// available for checking placement
    	ArrayList orderedList = new ArrayList(this.workflowSteps);
    	Iterator iter = this.workflowSteps.iterator();
    	while (iter.hasNext()){
    		WorkflowStep step = (WorkflowStep) iter.next();
    		// Remove the current placement for this step
    		orderedList.remove(step);
    		int index = this.findPlacementInStepSequences(orderedList,step.getStepId());
    		orderedList.add(index,step);
    	}
    	
    	    	
    	int size = orderedList.size();
    	WorkflowStep[] orderedSteps = new WorkflowStep[size];
    	for (int i=0; i < orderedList.size(); i++){
    		orderedSteps[i] = (WorkflowStep) orderedList.get(i);
    	}
    	return orderedSteps;
    }
    
    // This method finds the appropriate index in the ArrayList to place the WorkflowStep 
    // by comparing the current step to previous steps in the list
    private int findPlacementInStepSequences(ArrayList orderedList, int currentStepId){
    	int index = 0;
    	boolean placementFound = false;
    	for (int i=0; i < orderedList.size(); i++){
    		WorkflowStep seq = (WorkflowStep) orderedList.get(i);
    		int stepId = seq.getPrevStepId();
    		if (stepId == currentStepId){
    			index = i;
    			placementFound = true;
    			break;
    		}
    	}
    	if (!placementFound){
    		index = orderedList.size();
    	}
    	return index;
    }

	public User getLastUserChanged(){
		WorkflowStep[] steps = this.orderWorkflowSteps();
		TreeMap map = new TreeMap();
		User user = null;
		for (int i=0; i < steps.length; i++){
			if (steps[i].getDateChanged() != null){
				map.put(new Integer(steps[i].getApprovalId()),steps[i].getDateChanged());
			}
		}
		// Allow TreeMap to sort by Date ascending, and pull the last value in the map
		// Loop through steps again to find matching approval id
		if (map.size() > 0){
			int lastApprovalId = ((Integer) map.lastKey()).intValue();
			for (int i=0; i < steps.length; i++){
				if (steps[i].getApprovalId() == lastApprovalId && steps[i].getUserApprovedOrRejected() != null){
					user = steps[i].getUserApprovedOrRejected();
				}
			}
		}
		
		return user;
	}
    
}
