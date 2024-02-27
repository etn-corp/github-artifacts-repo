package com.eaton.electrical.smrc.bo;

import java.util.*;
import java.text.*;
import com.eaton.electrical.smrc.util.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.dao.*;
import java.sql.Connection;

public class ModuleChangeRequest {
	
	private long id;
	private long distributorId;
	private String userAdded;
	private String userChanged;
	private Date dateAdded;
	private Date dateChanged;
	private ArrayList modules;
	private ArrayList notes;
	
	
	public ModuleChangeRequest (){
		modules = new ArrayList();
		notes = new ArrayList();
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

	public long getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(long distributorId) {
		this.distributorId = distributorId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ModuleChangeProduct[] getModuleProducts() {
		int listSize = this.modules.size();
		ModuleChangeProduct[] moduleArray = new ModuleChangeProduct[listSize];
		for (int i=0; i < listSize; i++){
			moduleArray[i] = (ModuleChangeProduct) this.modules.get(i);
		}
		return moduleArray;
	}

	public void addModuleProduct(ModuleChangeProduct moduleProduct) {
		this.modules.add(moduleProduct);
	}
	
	public void addModuleProducts(ModuleChangeProduct[] moduleProducts){
		if (moduleProducts != null){
			for (int i=0; i < moduleProducts.length; i++){
				this.modules.add(moduleProducts[i]);
			}
		}
	}
	
	public void removeAllModuleProducts(){
		this.modules.clear();
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



	
	public ModuleChangeReasonNotes[] getModuleChangeReasonNotes() {
		ModuleChangeReasonNotes[] noteArray = new ModuleChangeReasonNotes[this.notes.size()];
		for (int i=0; i < this.notes.size(); i++){
			noteArray[i] = (ModuleChangeReasonNotes)this.notes.get(i);
		}
		return noteArray;
	}

	public void setModuleChangeReasonNotes(ModuleChangeReasonNotes[] reasonNotes) {
		this.notes.clear();
		if (reasonNotes != null){
			for (int i=0; i < reasonNotes.length; i++){
				this.notes.add(reasonNotes[i]);
			}
		}
	}
	
	public void addModuleChangeReasonNote(ModuleChangeReasonNotes reasonNote){
		this.notes.add(reasonNote);
	}
	public void deleteAllModuleChangeReasonNotes(){
		this.notes.clear();
	}
	
	
	public void sendNotificationEmail(Workflow workflow, Account account, WorkflowPriorApprover[] priorApprovers, Connection DBConn){
		SMRCLogger.debug("in sendNotificationEmail");
		String workflowStatus = this.getWorkflowStatus(workflow);
		
		// Create subject
		StringBuffer subject = new StringBuffer();
		subject.append("Product Loading Module Change Request - ");
		if (workflowStatus.trim().equalsIgnoreCase("A")){
			subject.append("Approval");
		} else if (workflowStatus.trim().equalsIgnoreCase("R")){
			subject.append("Rejection");
		} else if (workflowStatus.trim().equalsIgnoreCase("V")){
			subject.append("Revise and Resubmit");
		}
		
		
		TAPMail tapmail = new TAPMail();
		tapmail.setSubject(subject.toString());
		tapmail.setText(this.createNotificationMessage(workflow,account,workflowStatus,DBConn));
		tapmail.setSenderInfo("Target Account Planner", "oemaccountplanner@eaton.com");
		String[] emailAddresses = this.getRecipients(workflow,account,workflowStatus,DBConn);
		for (int i =0; i < emailAddresses.length; i++){
			tapmail.addRecipient(emailAddresses[i]);
		}
		if (workflowStatus.trim().equalsIgnoreCase("V") && priorApprovers != null && priorApprovers.length > 0){
			for (int i=0; i < priorApprovers.length; i++){
				WorkflowPriorApprover pa = priorApprovers[i];
				try {
					String paEmail = UserDAO.getUserEmailAddress(pa.getUserId(),DBConn);
					if (paEmail != null && !paEmail.trim().equals("")){
						tapmail.addRecipient(paEmail);
						SMRCLogger.debug("Adding email for prior approver " + pa.getUserId() + "  " + paEmail);
					}
				} catch (Exception e) {
					SMRCLogger.error("ModuleChangeReqeust.sendNotificationEmail():: Exception occurred getting email address for prior approver for moduleRequest " + this.getId() + "  priorApprover id: " + pa.getUserId() + "  " + e, e);
				} 
			}
		}
		
		SMRCLogger.debug("Going to send email");
		tapmail.sendMessage();
	}
	
	private String createNotificationMessage(Workflow workflow, Account account, String workflowStatus, Connection DBConn){
		char returncode = 13;
		StringBuffer message = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		message.append("The product loading module change request for ");
		message.append(account.getCustomerName());
		message.append(" (" + account.getVcn() + ") ");
		message.append("has been ");
		if (workflowStatus.trim().equalsIgnoreCase("A")){
			message.append("approved ");
		} else if (workflowStatus.trim().equalsIgnoreCase("R")){
			message.append("rejected ");
		} else if (workflowStatus.trim().equalsIgnoreCase("V")){
			message.append("returned for revision ");
		}
		message.append("by " + workflow.getLastUserChanged().getFullName() + ". ");
		
		
		String revisionExplanation = null;
		
		WorkflowStep[] currentSteps = workflow.getCurrentSteps();
		WorkflowStep[] orderedSteps = workflow.orderWorkflowSteps();
		if (orderedSteps[orderedSteps.length - 1].getStatusFlag().trim().equalsIgnoreCase("Y") &&
				(currentSteps == null || currentSteps.length == 0)){
			message.append("This request is COMPLETE. All required approvals have been provided. ");
			message.append("Target Account Planner will reflect the correct loading modules the day after the channel team updates Vista");
		} else if (!workflowStatus.trim().equalsIgnoreCase("R")){
			message.append("This request is currently at step(s): " + returncode);
			for (int i=0; i < currentSteps.length; i++){
				if (i > 0){
					message.append(returncode);
				}
				message.append(currentSteps[i].getStepName());
				if (workflowStatus.trim().equalsIgnoreCase("V")){
					// If a revise/resubmit, get explanation to add into email, if an exception is thrown, just log and move on
					// We don't want to not send the email because of an error here
					try {
						revisionExplanation = ReviseResubExplanationDAO.getLastExplanationForReviseByApprovalId(currentSteps[i].getApprovalId(),DBConn);
					} catch (Exception e) {
						SMRCLogger.error("ModuleChangeReqeust.createNotificationMessage():: Exception occurred getting revision explanation for moduleRequest " + this.getId() + " - " + e, e);
					}
				}
			}
		}
		
		if (revisionExplanation != null){
			message.append(returncode);
			message.append(returncode + "Request for revision explanation: " + revisionExplanation);
		}
		
		message.append(returncode);
		message.append(returncode);
		message.append("Request information" + returncode);
		message.append(returncode + "Module changes: " + returncode);
		ModuleChangeProduct[] products = this.getModuleProducts();
		for (int i=0; i < products.length; i++){
			ModuleChangeProduct product = products[i];
			String action = "added";
			if (product.getAction().trim().equalsIgnoreCase("R")){
				action = "removed";
			}
			message.append(product.getLoadingModuleName() + " (" + product.getLoadingModuleShortCode() + ") " + " : " + action + " " + returncode);
		}
		message.append(returncode);
		message.append(returncode);
		
		message.append("Notes: " + returncode);
		ModuleChangeReasonNotes[] notes = this.getModuleChangeReasonNotes();
		if (notes.length == 0){
			message.append("No notes have been provided.");
		} else {
			for (int i=0; i < notes.length; i++){
				ModuleChangeReasonNotes note = notes[i];
				message.append(note.getUserAddedName() + " (" + sdf.format(note.getDateAdded()) + ") : ");
				message.append(note.getReasonNotes());
				message.append(returncode);
			}
		}
		
		message.append(returncode);
		message.append(returncode);
		
		message.append("Approvals: " + returncode);
		
		for (int i=0; i < orderedSteps.length; i++){
			WorkflowStep step = orderedSteps[i];
			message.append(step.getStepName() + ": ");
			if (step.getDateChanged() != null){
				String action = "Approved";
				if (step.getStatusFlag().trim().equalsIgnoreCase("R")){
					action = "Rejected";
				} else if (step.getStatusFlag().trim().equalsIgnoreCase("V")){
					action = "Requested revision";
				}
				message.append(action + " by " + step.getUserApprovedOrRejected().getFullName() + " on " + sdf.format(step.getDateChanged()));
			}
			message.append(returncode);
		}
		
		return message.toString();
	}
	
	// Assumes it's an approval unless it finds a rejected step
	// or a revise/resubmit step
	private String getWorkflowStatus (Workflow workflow){
		String notificationType = "A";
		ArrayList steps = workflow.getWorkflowSteps();
		for (int i=0; i < steps.size(); i++){
			WorkflowStep step = (WorkflowStep) steps.get(i);
			if (step.getStatusFlag().trim().equalsIgnoreCase("R") || (step.getStatusFlag().trim().equalsIgnoreCase("V"))){
				notificationType = step.getStatusFlag();
			}
		}
		return notificationType;
	}
	
	/**
	 * This method returns a String array of email addresses to be added as recipients of the ModuleChangeRequest email
	 * based on what step it's on, and who has approved. Some Exception are caught but not thrown because we do not 
	 * want the process to be stopped because it could not obtain an email address. Errors are written to the logs
	 * so development will be alerted to these situations.
	 * @param workflow
	 * @param account
	 * @param workflowStatus
	 * @param DBConn
	 * @return
	 */
	private String[] getRecipients(Workflow workflow, Account account, String workflowStatus, Connection DBConn) {
		SMRCLogger.debug("ModuleChangeRequest.getRecipients() begin");
		ArrayList emailAddresses = new ArrayList();
		HashMap approvers = new HashMap();
		WorkflowStep[] steps = workflow.orderWorkflowSteps();
		for (int i=0; i < steps.length; i++){
			if (steps[i].getUserApprovedOrRejected() != null && steps[i].getUserApprovedOrRejected().getEmailAddress() != null
					&& !steps[i].getUserApprovedOrRejected().getEmailAddress().trim().equals("")){
				approvers.put(steps[i].getUserChanged(),steps[i].getUserApprovedOrRejected().getEmailAddress());
			}
			// Include user that added workflow because they may not be one of the approvers
			String userAdded = steps[i].getUserAdded();
			if (!approvers.containsKey(userAdded)){
				// Don't bother getting email if user is already in approvers HashMap
				try {
					String userAddedEmail = UserDAO.getUserEmailAddress(userAdded,DBConn);
					if (userAddedEmail != null && !userAddedEmail.trim().equals("")){
						approvers.put(userAdded,userAddedEmail);
						SMRCLogger.debug("adding in user added: " + userAddedEmail);
					}
				} catch (Exception e) {
					SMRCLogger.error("ModuleChangeReqeust.getRecipients():: Exception occurred getting email address for user added for moduleRequest " + this.getId() + " - " + e, e);
				}
			}
		}
		
		Iterator iter = approvers.keySet().iterator();
		while (iter.hasNext()){
			String key = (String) iter.next();
			String emailAddress = (String) approvers.get(key);
			SMRCLogger.debug("Would send to email of approver " + key);
			emailAddresses.add(emailAddress);
		}
		
		boolean sendToChannelGroup = false;
		if (steps[steps.length - 1].getStatusFlag().trim().equalsIgnoreCase("Y")){
			// Last step was approved
			sendToChannelGroup = true;
		}
		
		WorkflowStep[] currentSteps = workflow.getCurrentSteps();
		
		for (int i=0; i < currentSteps.length; i++){
			WorkflowStep step = currentSteps[i];
			SMRCLogger.debug("Current step " + step.getStepName());
			if (step.getStepName().trim().equalsIgnoreCase(WorkflowStep.STEP_NAME_GLOBAL_CHANNEL_MANAGER)){
				sendToChannelGroup = true;
			} else if (step.getStepName().trim().equalsIgnoreCase(WorkflowStep.STEP_NAME_DISTRICT_MANAGER)){
				try {
					String geog = account.getDistrict();
					if (geog.length() > 5){
						geog = geog.substring(0,5);
					}
					String vistaId = DistrictDAO.getManagerForGeography(geog,"DM",DBConn);
					SMRCLogger.debug("vistaId of DM " + vistaId + "  geog: " + geog);
					if (vistaId != null && !vistaId.trim().equals("")){
						User dm = UserDAO.getUserByVistalineId(vistaId, DBConn);
						if (dm != null && dm.getEmailAddress() != null && !dm.getEmailAddress().trim().equals("")){
							emailAddresses.add(dm.getEmailAddress());
							SMRCLogger.debug("Adding DM for " + account.getDistrict() + "  " + dm.getFullName() + "  " + dm.getEmailAddress());
						}
					}
				} catch (Exception e) {
					SMRCLogger.error("ModuleChangeReqeust.getRecipients():: Exception occurred getting emails for District Manager for moduleRequest " + this.getId() + " - " + e, e);
				}
				
			}
		}
		
				
		if (sendToChannelGroup){
			try {
				String channelTAPRequestsEmailAddress = Globals.getChannelTAPRequestsEmailAddress(DBConn);
				emailAddresses.add(channelTAPRequestsEmailAddress);
				SMRCLogger.debug("Added email address for channel managers inbox: " + channelTAPRequestsEmailAddress);
			} catch (Exception e) {
				SMRCLogger.error("ModuleChangeReqeust.getRecipients():: Exception occurred getting ChannelTAPReqeustsEmailAddress for moduleRequest " + this.getId() + " - " + e, e);
			}
			ArrayList channelManagers = new ArrayList();
			try {
				channelManagers = UserDAO.getAllChannelMarketingManagers(DBConn);
				for (int cm=0; cm < channelManagers.size(); cm++){
					User channelManager = (User) channelManagers.get(cm);
					if (channelManager.getEmailAddress() != null && !channelManager.getEmailAddress().trim().equals("")){
						emailAddresses.add(channelManager.getEmailAddress());
						SMRCLogger.debug("Added channel manager email address for " + channelManager.getFullName());
					}
				}
			} catch (Exception e) {
				SMRCLogger.error("ModuleChangeReqeust.getRecipients():: Exception occurred getting all Channel managers for moduleRequest " + this.getId() + " - " + e, e);
			}
		}
		
		
		// Convert ArrayList to String[] 
		int size = emailAddresses.size();
		String emails[] = new String[size];
		for (int i=0; i < size; i++){
			emails[i] = (String) emailAddresses.get(i);
		}
		
		SMRCLogger.debug("ModuleChangeRequest.getRecipients() end   emails: " + emails.length);
		
		return emails;
	}
	
	/**
	 * This method returns the status of the module change request based on the status of it's
	 * products. "Invalid" is returned if there are no products, or if all of the products do not
	 * have the same status.
	 * @param products
	 * @param DBConn
	 * @return
	 * @throws Exception
	 */
	public static String determineModuleRequestStatus (ModuleChangeProduct[] products, Connection DBConn) throws Exception {
		String status = "Invalid";
		if (products == null || products.length == 0){
			return status;
		}
		HashSet productStatus = new HashSet();
		// Put all vista status into a set to get distinct set of ids, this should always be one
		for (int i=0; i < products.length; i++){
			productStatus.add(new Long(products[i].getVistaStatusId()));
		}
		
		// If more than one status was put in this set, then not all products have the same vista
		// status and there's a problem
		if (productStatus.size() != 1){
			return status;
		} else {
			Iterator iter = productStatus.iterator();
			long vistaStatusId = ((Long)iter.next()).longValue();
			status = ProductModuleDAO.getModuleChangeVistaStatusName(vistaStatusId,DBConn);
		}
		
		return status;
	}

}
