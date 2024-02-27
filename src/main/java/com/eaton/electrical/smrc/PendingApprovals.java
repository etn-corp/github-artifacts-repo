/*
 * Created on May 2, 2005
 *
  */
package com.eaton.electrical.smrc;

import java.io.IOException;
import java.sql.Connection;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.exception.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

/**
 * 
 * @author Jason Lubbert
 */
public class PendingApprovals extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = null;
        Connection DBConn = null;
        String sFwdUrl = "/SMRCErrorPage.jsp";

        boolean redirect = false;
        
        try {
            DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();
            
            user = SMRCSession.getUser(request, DBConn);
            sFwdUrl = "/pendingApprovals.jsp";
            SMRCHeaderBean hdr = new SMRCHeaderBean();
            hdr.setUser(user);
            if (request.getParameter("requestType") != null){
            	
                if (request.getParameter("requestType").equalsIgnoreCase("targetMarketPlan")){
                    String tmId = request.getParameter("tmId");
                    String action = request.getParameter("action");
                    updateTargetMarketStatus(tmId, action, user, request, DBConn);//request object used when action is revise only
                } else if (request.getParameter("requestType").equalsIgnoreCase("project")){
                    String projectId = request.getParameter("projId");
                    String action = request.getParameter("action");
                    updateTargetProjectStatus(projectId, action, user, DBConn);
                } else if (request.getParameter("requestType").equalsIgnoreCase("workflow")){
                    String action = request.getParameter("action");
                    String approvalId = request.getParameter("approvalId");
                    String acctId = request.getParameter("vcn");
                    String type = request.getParameter("type");
                    String reviseResubmitExplantion = request.getParameter("reviseResubmitExplantion");
                    Account acct = AccountDAO.getAccount(acctId,DBConn);
                    String workflowType = "";
                    if (type.equalsIgnoreCase("termination")) {
                        workflowType = "Distributor Termination";
                    } else if (type.equalsIgnoreCase("distributor")) {
                        workflowType = "Distributor Application";
                    } else {
                        workflowType = "Customer";
                    }
                    if (action != null && action.trim().equalsIgnoreCase("approve") && this.hasPotentialDuplicates(acct,user,workflowType,DBConn,request)){
                    	hdr.setAccount(acct);
                    	sFwdUrl = "/accountDuplicateCustomers.jsp";
                    } else {
                    	updateDistributorWorkflow(action, approvalId, acct, workflowType, user, DBConn, reviseResubmitExplantion, request);
                    }
                } else if (request.getParameter("requestType").equalsIgnoreCase("module")){
                	SMRCLogger.debug("got the module request");
            	    this.updateModuleChangeRequest(request,user,DBConn);
                }
            }
            
            request.setAttribute("header", hdr);

            UserApprovals userApprovals = new UserApprovals(user);
            userApprovals.fetchApprovals(DBConn);
           
            request.setAttribute("userApprovals", userApprovals);
            
            //prior approvers for each customer request
            //***************************************************************************************
            ArrayList customers = userApprovals.getCustomerRequests();
            ArrayList alOfCustomersPriorApproversAL = new ArrayList();
            int customersSize = (customers != null) ? customers.size() : 0;
            
            for(int customersIndex=0; customersIndex < customersSize; customersIndex++){
            	
            	WorkflowStep step = (WorkflowStep) customers.get(customersIndex);
            	alOfCustomersPriorApproversAL.add(getPriorApprovers(step.getVcn(), DBConn));
            }
            request.setAttribute("alOfCustomersPriorApproversAL", alOfCustomersPriorApproversAL);
            //***************************************************************************************
            
            
            
            
            //prior approvers for each distributor request          
            //***************************************************************************************
            ArrayList distributors = userApprovals.getDistributorsRequests();
            ArrayList alOfDistributorsPriorApproversAL = new ArrayList();
            int distributorsSize = (distributors != null) ? distributors.size() : 0;
            
            for(int distributorsIndex=0; distributorsIndex < distributorsSize; distributorsIndex++){
            	
            	WorkflowStep step = (WorkflowStep) distributors.get(distributorsIndex);
            	alOfDistributorsPriorApproversAL.add(getPriorApprovers(step.getVcn(), DBConn));
            }
            request.setAttribute("alOfDistributorsPriorApproversAL", alOfDistributorsPriorApproversAL);
            //******************************************************************************************
            
            
            
            
            //prior approvers for each Distributor Termination Request
            //***************************************************************************************
            ArrayList terminations = userApprovals.getTerminationsRequests();
            ArrayList alOfTerminationsPriorApproversAL = new ArrayList();
            int terminationsSize = (terminations != null) ? terminations.size() : 0;
            
            for(int terminationsIndex=0; terminationsIndex < terminationsSize; terminationsIndex++){
            	
            	WorkflowStep step = (WorkflowStep) terminations.get(terminationsIndex);
            	alOfTerminationsPriorApproversAL.add(getPriorApprovers(step.getVcn(), DBConn));
            }
            request.setAttribute("alOfTerminationsPriorApproversAL", alOfTerminationsPriorApproversAL);
            //******************************************************************************************
            
            
            
            //prior approvers for each Target Market Plan Request
            //***************************************************************************************
            ArrayList targetMarketPlans = userApprovals.getTargetMarketPlansRequests();
            ArrayList alOfTargetMarketPlansPriorApproversAL = new ArrayList();
            int targetMarketPlansSize = (targetMarketPlans != null) ? targetMarketPlans.size() : 0;
            
            for(int targetMarketPlansIndex=0; targetMarketPlansIndex < targetMarketPlansSize; targetMarketPlansIndex++){
            	
            	TargetMarket currentTargetMarket = (TargetMarket) targetMarketPlans.get(targetMarketPlansIndex);
            	alOfTargetMarketPlansPriorApproversAL.add(getTargetMarketPlanPriorApprovers(currentTargetMarket.getTargetMarketPlanId(), DBConn));
            }
            request.setAttribute("alOfTargetMarketPlansPriorApproversAL", alOfTargetMarketPlansPriorApproversAL);
            //******************************************************************************************
              
            
            // prior approvers for each module change request          
            //***************************************************************************************
            ArrayList moduleChanges = userApprovals.getModuleChangeRequests();
            ArrayList alOfModulePriorApproversAL = new ArrayList();
            int moduleSize = (moduleChanges != null) ? moduleChanges.size() : 0;
            
            for(int moduleIndex=0; moduleIndex < moduleSize; moduleIndex++){
            	
            	WorkflowStep step = (WorkflowStep) moduleChanges.get(moduleIndex);
            //	alOfModulePriorApproversAL.add(getPriorApprovers(step.getVcn(), DBConn));
            	alOfModulePriorApproversAL.add(WorkflowDAO.getModuleChangePriorApprovers(step.getModuleChangeRequestId(),DBConn));
            }
            request.setAttribute("alOfModulePriorApproversAL", alOfModulePriorApproversAL);
            //******************************************************************************************
            
            //COMMITMENT APPROVALS
            //*******************************************************************************************
            ArrayList ccrList = new ArrayList();
            
            if(user.isChannelMarketingManager()) {
            	try {
            		ccrList = CommitmentDAO.getPendingApprovalsForChannel(DBConn);
            	} catch(Exception e) {
           			SMRCLogger.error("PendingApprovals.getPendingApprovalsForChannel()-committment: ", e);
        			System.out.println(e);
        			throw e;
            	}
            } else if(user.isDistrictManager()){
            	try {
            		ccrList = CommitmentDAO.getPendingApprovalsForDM(user.getFullName(),DBConn);
            	} catch(Exception e) {
        			SMRCLogger.error("PendingApprovals.getPendingApprovalsForDM()-committment: ", e);
        			System.out.println(e);
        			throw e;
            	}
            } else {
            	//request.setAttribute("ccrList", "");
            }
            
            request.setAttribute("ccrList", ccrList);
         
            //*******************************************************************************************
           
            //CUSTOMER CATEGORY APPROVALS
            //*********************************************************************************************
            Hashtable ccaList = new Hashtable();
            String managerType = "";
            if(user.isChannelMarketingManager()) {
            	managerType = "cm";
            	try {
            		ccaList = ModifySegmentsDAO.getPendingApprovalsForChannel(DBConn);
            	} catch(Exception e) {
        			SMRCLogger.error("PendingApprovals.getPendingApprovalsForChannel()-category: ", e);
        			System.out.println(e);
        			throw e;
            	}
            } else if(user.isDistrictManager()){
            	managerType = "dm";
            	try {
            		ccaList = ModifySegmentsDAO.getPendingApprovalsForDM(user.getFullName(),DBConn);
               	} catch(Exception e) {
        			SMRCLogger.error("PendingApprovals.getPendingApprovalsForDM()-category: ", e);
        			System.out.println(e);
        			throw e;
            	}
            } else {
            	//request.setAttribute("ccrList", "");
            }
            
            request.setAttribute("ccaList", ccaList);          
            request.setAttribute("managerType", managerType);
            //*********************************************************************************************
            
            SMRCConnectionPoolUtils.commitTransaction(DBConn);
        } catch (ProfileException pe) {
        	// See if we can get the user info for the log message.
        	if ( user != null ) {
	        	// Put as much on the first line as possible for NetCool
	            SMRCLogger.warn(this.getClass().getName() + 
	            	".doGet(): USERID=" + user.getUserid() + 
					"; MESSAGE=" + pe.getMessage() + 
					"\nUSER:" + user, pe);
        	} else {
	        	// Put as much on the first line as possible for NetCool
	            SMRCLogger.warn(this.getClass().getName() + 
	            	".doGet(): MESSAGE=" + pe.getMessage(), pe);
        	}
        	
            SMRCConnectionPoolUtils.rollbackTransaction(DBConn);

            request.setAttribute("exception", pe.getMessage());
            sFwdUrl = "/SMRCErrorPage.jsp";
            redirect = false;

        } catch (Exception e) {
        	// See if we can get the user info for the log message.
        	if ( user != null ) {
	        	// Put as much on the first line as possible for NetCool
	            SMRCLogger.error(this.getClass().getName() + 
	                	".doGet(): USERID=" + user.getUserid() + 
	    				"; MESSAGE=" + e.getMessage() + 
	    				"\nUSER:" + user, e);
        	} else {
	        	// Put as much on the first line as possible for NetCool
	            SMRCLogger.error(this.getClass().getName() + 
	            	".doGet(): MESSAGE=" + e.getMessage(), e);
        	}

        	SMRCConnectionPoolUtils.rollbackTransaction(DBConn);

            request.setAttribute("exception", e.getMessage());
            sFwdUrl = "/SMRCErrorPage.jsp";
            redirect = false;

        } finally {
            SMRCConnectionPoolUtils.close(DBConn);
            gotoPage(sFwdUrl, request, response, redirect);
        }

    }
    
    private void updateTargetMarketStatus (String tmId, String action, User user, HttpServletRequest request, Connection DBConn) throws Exception {
        
    	TargetMarket targetMarket = TargetMarketDAO.getTargetMarket(Globals.a2int(tmId),DBConn);
        String previousStatus = targetMarket.getStatus();
        String workflowApprovalFlag = "Y";
        User selectedPriorApproverUser = new User();
        String reviseResubmitExplantion = null;
        
        if (action.equals("approve")){
	        if (targetMarket.isSubmitted()) {
	            targetMarket.setStatus("D");
	        } else if (targetMarket.isDMApproved()) {
	            targetMarket.setStatus("A");
	        }else if(targetMarket.isPendingResubmission()){
	        	targetMarket.setStatus("B");//Submitted to DM
	        }
	        
        } else if (action.equals("reject")){
            targetMarket.setStatus("R");
            workflowApprovalFlag = "R";
        }
        else if(action.equals("revise")){
        	
        	//****************************************************************************************************************

        	String targetMarketStatus = request.getParameter("targetMarketStatus");
        	if(targetMarketStatus != null){
        		targetMarket.setStatus(targetMarketStatus);
            	//update target market status
            	updateTargetMarketStatus(targetMarket.getTargetMarketPlanId(), targetMarketStatus, DBConn);
        	}
        	else{
        		throw new Exception("targetMarketStatus is null");
        	}
        	
        	workflowApprovalFlag = "V";
        	String workflowType = "Target Market Plans";
        	reviseResubmitExplantion = request.getParameter("reviseResubmitExplantion");
        	String selectedPriorApproverWorkflowApprovalId = request.getParameter("selectedPriorApproverWorkflowApprovalId");

        	if(selectedPriorApproverWorkflowApprovalId != null){
            
            	String selectedPriorApproverUserId = WorkflowDAO.getWorkflowApprovalUserChanged(selectedPriorApproverWorkflowApprovalId, DBConn);

            	selectedPriorApproverUser = UserDAO.getUser(selectedPriorApproverUserId, DBConn);

            	//now update all the workflow_approvals with vista_customer_number (given) that has data changed later (aka greater) than 
            	//the date_changed for the given work_approval_id

            	WorkflowDAO.updateTargetMarketWorkflowLaterApproversToNotApproved(tmId, selectedPriorApproverWorkflowApprovalId, DBConn);

        		WorkflowDAO.changeApprovalStatus(selectedPriorApproverWorkflowApprovalId, "", "V", tmId, DBConn);//user that submitted the resive resub.

        		Workflow workflow = WorkflowDAO.getWorkflow(tmId,workflowType,user,DBConn);//user that submitted the resive resub.

            	//user that submitted the resive resub.
            	ReviseResubExplanationDAO.insertReviseResubExplanationWithoutTargetProject(Integer.parseInt(selectedPriorApproverWorkflowApprovalId), reviseResubmitExplantion, user.getUserid(), DBConn);//user that submitted the resive resub.
            	//user to whome the resive resub was submitted.
            	//workflow.sendReviseAndResubmitNotification(acct, selectedPriorApproverUser, DBConn, reviseResubmitExplantion);
        	}
        	else{
        		throw new Exception("selectedPriorApproverUserId is null");
        	}
            
            //******************************************************************************************************************************
        
        }//else if(action.equals("revise"))

        TargetMarketDAO.updateTargetMarketPlan(targetMarket, DBConn, user.getUserid());
        // Commit here so email reflects current information
        SMRCConnectionPoolUtils.commitTransaction(DBConn);
        // Retrieve updated information
        targetMarket = TargetMarketDAO.getTargetMarket(Globals.a2int(tmId),DBConn);
    
        //This is called for the second time (when action = revise), I don't think should be called again....
        if(!action.equals("revise")){
        	WorkflowDAO.updateTargetMarketPlanWorkflow(previousStatus,workflowApprovalFlag,Globals.a2int(tmId),user,DBConn);//THIS IS CALLED 2ND TIME!!!!
        }
        
        if(action.equals("revise")){
        	TargetMarketEmail tmEmail = new TargetMarketEmail(targetMarket,DBConn);
        	tmEmail.sendReviseResubmitEmail(selectedPriorApproverUser, reviseResubmitExplantion);
        }
        else{
        	TargetMarketEmail tmEmail = new TargetMarketEmail(targetMarket,DBConn);
        	tmEmail.sendEmail();
        }
    }
    
    private void updateTargetProjectStatus(String projectId, String action, User user, Connection DBConn) throws Exception {
        TargetProject oldTP = ProjectDAO.getTargetProject(DBConn, projectId);
        if (action.equalsIgnoreCase("approve")) {
            ProjectDAO.approveProject(DBConn, oldTP, user);
        } else if (action.equalsIgnoreCase("delete")) {
            ProjectDAO.deleteProject(DBConn, oldTP);
        }
        // Commit here so email reflects current information
        SMRCConnectionPoolUtils.commitTransaction(DBConn);
        // Retrieve updated information
        TargetProject newTP = ProjectDAO.getTargetProject(DBConn, projectId);
        TargetProjectUpdateEmail tpue = new TargetProjectUpdateEmail();
        tpue.setProjectId(projectId);
        tpue.setUpdateUser(user);
        tpue.setOldProject(oldTP);
        tpue.setNewProject(newTP);

        tpue.run();
    }
    
    private void updateDistributorWorkflow(String action, String approvalId, Account acct, String workflowType, User user, Connection DBConn, String reviseResubmitExplantion, HttpServletRequest request) throws Exception {
        
    	 
        if (action.equalsIgnoreCase("approve")) {
            
            WorkflowDAO.changeApprovalStatus(approvalId, user.getUserid(), "Y", acct.getVcn(), DBConn);
            Workflow workflow = WorkflowDAO.getWorkflow(acct.getVcn(),workflowType,user,DBConn);
            
            if(!workflow.isComplete()){
                workflow.sendNotifications(acct, user, DBConn);

            }
            if(workflow.isComplete()){
                //if(!workflowType.equals("Distributor Termination")){
            	if(workflowType.equals("Customer") || workflowType.equals("Distributor Application")){
            		// send to vista
                    AccountRegistrationResults registrationResults = SMRCAccountRegistrationService.registerAccount(acct, user, DBConn);
                	if(registrationResults.isRegistrationImmediate()){
                		acct = AccountDAO.getAccount(registrationResults.getRegisteredAccountVCN(), DBConn);	
                	}
                 	workflow.sendNotifications(acct, user, DBConn);
            	}else{
            	    workflow.sendNotifications(acct, user, DBConn);
            	}
            }
            
        	
        }else if(action.equalsIgnoreCase("reject")){
        	
            WorkflowDAO.changeApprovalStatus(approvalId, user.getUserid(), "R", acct.getVcn(), DBConn);
        	Workflow workflow = WorkflowDAO.getWorkflow(acct.getVcn(),workflowType,user,DBConn);
        	if(workflow.getWorkflowType().equalsIgnoreCase("Customer") || workflow.getWorkflowType().equalsIgnoreCase("Distributor Application")){
        	    AccountDAO.changeAccountStatus(acct.getVcn(),"Rejected",DBConn);
    		}
        	workflow.sendRejectionNotification(acct, user, DBConn);
        }
        else if(action.equalsIgnoreCase("revise")){
        	
        	//approvalId is the "workflow_approval_id"
        	//acctId is the vcn (vista customer number)
        	
        	//get the selected approver workflow approval Id from the selected radio button
        	String selectedPriorApproverWorkflowApprovalId = request.getParameter("selectedPriorApproverWorkflowApprovalId");
        	
        	if(selectedPriorApproverWorkflowApprovalId != null){
	        
	        	String selectedPriorApproverUserId = WorkflowDAO.getWorkflowApprovalUserChanged(selectedPriorApproverWorkflowApprovalId, DBConn);
	        	User selectedPriorApproverUser = UserDAO.getUser(selectedPriorApproverUserId, DBConn);
	        	
	        	//now update all the workflow_approvals with vista_customer_number (given) that has data changed later (aka greater) than 
	        	//the date_changed for the given work_approval_id
	        	WorkflowDAO.updateWorkflowLaterApproversToNotApproved(acct.getVcn(), selectedPriorApproverWorkflowApprovalId, DBConn);
        		
        		//Change the status 
	            //WorkflowDAO.changeApprovalStatus(approvalId, user.getUserid(), "V", acctId, DBConn);//user that submitted the resive resub.
        		WorkflowDAO.changeApprovalStatus(selectedPriorApproverWorkflowApprovalId, "", "V", acct.getVcn(), DBConn);//user that submitted the resive resub.
	        	Workflow workflow = WorkflowDAO.getWorkflow(acct.getVcn(),workflowType,user,DBConn);//user that submitted the resive resub.
	        	if(workflow.getWorkflowType().equalsIgnoreCase("Customer") || workflow.getWorkflowType().equalsIgnoreCase("Distributor Application")){
	        	    AccountDAO.changeAccountStatus(acct.getVcn(),"Pending Resubmission",DBConn);
	    		}
	        	
	        	//user that submitted the resive resub.
	        	ReviseResubExplanationDAO.insertReviseResubExplanationWithoutTargetProject(Integer.parseInt(selectedPriorApproverWorkflowApprovalId), reviseResubmitExplantion, user.getUserid(), DBConn);//user that submitted the resive resub.
	        	
	        	//user to whome the resive resub was submitted.
	        	workflow.sendReviseAndResubmitNotification(acct, selectedPriorApproverUser, user, DBConn, reviseResubmitExplantion);
        	}
        	else{
        		throw new Exception("selectedPriorApproverUserId is null");
        	}
			
        }
        
    }
    
    private boolean hasPotentialDuplicates (Account acct, User user, String workflowType, Connection DBConn, HttpServletRequest request) throws Exception {
    	boolean possibleDuplicate = false;
    	Workflow workflow = WorkflowDAO.getWorkflow(acct.getVcn(),workflowType,user,DBConn);
    	
    	if (workflow.isLastApproval() && 
    			(workflowType.equals(Workflow.WORKFLOW_TYPE_CUSTOMER) || workflowType.equals(Workflow.WORKFLOW_TYPE_DISTRIBUTOR_APPLICATION))){
    		DuplicateCustomerBean[] duplicates = SMRCAccountRegistrationService.getPotentialDuplicates(acct,user);
        	if (duplicates != null && duplicates.length > 0){
        		possibleDuplicate = true;
        		request.setAttribute("duplicates", duplicates);
        		request.setAttribute("returnToPage","/PendingApprovals");
        	}
    		
    	}
    	
    	return possibleDuplicate;
    	
    }
    
    
    private ArrayList getPriorApprovers (String vistaCustomerNumber, Connection DBConn) throws Exception{
    	return WorkflowDAO.getWorkflowPriorApprovers(vistaCustomerNumber, DBConn); 
    }
    
    private ArrayList getTargetMarketPlanPriorApprovers (int targetMarketPlanId, Connection DBConn) throws Exception{
      	 
    	if(targetMarketPlanId != 0){
    		return WorkflowDAO.getTargetMarketPlanWorkflowPriorApprovers(targetMarketPlanId, DBConn);
    	}
    	else{
    		//throw new Exception("targetMarketPlanId is 0");
    		
    		//just clicking on the TargetMarket tab means that there is no target market id, so your first line will produce a 0. 
    		//The getTargetMarketPlanPriorApprovers method wrongly returns an exception if the tmid is a 0. If no specific plan is selected, 
    		//then you don't need any prior approvers, so you'll have to either handle null values or send in an empty ArrayList.
    		return new ArrayList();
    	}
    }
    
    private TargetMarket getTargetMarket(int targetMarketPlanId, Connection DBConn) throws Exception {
    	
    	if(targetMarketPlanId != 0){
    		return TargetMarketDAO.getTargetMarket(targetMarketPlanId, DBConn);
    	}
    	else{
    		throw new Exception("targetMarketPlanId is 0");
    	}
    }
    
    private void updateTargetMarketStatus (int targetMarketPlanId, String targetMarketStatus, Connection DBConn) throws Exception{
    	
    	if(targetMarketPlanId != 0){
    		TargetMarketDAO.updateTargetMarketStatus(targetMarketPlanId, targetMarketStatus, DBConn);
    	}
    	else{
    		throw new Exception("targetMarketPlanId is 0");
    	}
    }

    private void updateModuleChangeRequest(HttpServletRequest request, User user, Connection DBConn) throws Exception {
    	String submitAction = request.getParameter("action");
        int approvalId = Globals.a2int(request.getParameter("approvalId"));
        String vcn = request.getParameter("vcn");
        long moduleId = Globals.a2long(request.getParameter("moduleId"));
        
    	ModuleChangeRequest moduleChangeRequest = ProductModuleDAO.getModuleChangeRequest(moduleId,DBConn);
    	Workflow workflow = WorkflowDAO.getWorkflow(moduleId+"",Workflow.WORKFLOW_PRODUCT_MODULE,user,DBConn);
    	String moduleRequestProductVistaStatus = ProductModuleDAO.MODULE_CHANGE_VISTA_STATUS_IN_PROCESS;
    	
    	// See if this approval is the final approval needed (Global Channel Manager)
    	WorkflowStep[] currentSteps = workflow.getCurrentSteps();
		for (int i=0; i < currentSteps.length; i++){
			approvalId = currentSteps[i].getApprovalId();
			if (currentSteps[i].getStepName().trim().equalsIgnoreCase(WorkflowStep.STEP_NAME_GLOBAL_CHANNEL_MANAGER) &&
					submitAction.trim().equalsIgnoreCase("approve")	){
				moduleRequestProductVistaStatus = ProductModuleDAO.MODULE_CHANGE_VISTA_STATUS_APPROVED;
			}
		}
		
		
		String status = "Y";
		if (submitAction.trim().equalsIgnoreCase("reject")){
			status = "R";
			moduleRequestProductVistaStatus = ProductModuleDAO.MODULE_CHANGE_VISTA_STATUS_REJECTED;
		} else if (submitAction.trim().equalsIgnoreCase("revise")){
			String reviseId = request.getParameter("selectedPriorApproverWorkflowApprovalId");
			String reviseResubmitExplantion = request.getParameter("reviseResubmitExplantion");
			status = "V";
			SMRCLogger.debug("revise resubmit to " + reviseId);
			//	now update all the workflow_approvals with module change request id that has data changed later than 
        	// the date_changed for the given work_approval_id
			WorkflowDAO.updateModuleChangeWorkflowLaterApproversToNotApproved(moduleChangeRequest.getId(), Globals.a2long(reviseId), DBConn);
			ReviseResubExplanationDAO.insertReviseResubExplanationWithoutTargetProject(Globals.a2int(reviseId), reviseResubmitExplantion, user.getUserid(), DBConn);//user that submitted the resive resub.
			approvalId = Globals.a2int(reviseId);
		}
		
		// update workflow for module request
		WorkflowDAO.changeApprovalStatus(approvalId+"",user.getUserid(),status,vcn,DBConn);
		// retrieve workflow again for updated information
		workflow = WorkflowDAO.getWorkflow(moduleId+"",Workflow.WORKFLOW_PRODUCT_MODULE,user,DBConn);
		// update module change request first
		moduleChangeRequest.setUserChanged(user.getUserid());
		moduleChangeRequest = ProductModuleDAO.saveOrUpdateModuleChangeRequest(moduleChangeRequest,DBConn);
		// update all products with new status if rejected or approved by Channel Mgr
		long vistaStatusId = ProductModuleDAO.getModuleChangeVistaStatusId(moduleRequestProductVistaStatus,DBConn);
		ProductModuleDAO.updateAllProductsForModule(moduleId,vistaStatusId,user,DBConn);
		
		
		Account account = AccountDAO.getAccount(vcn,DBConn);
		
		// Populate a WorkflowPriorApprover Array to pass into sendNotificationEmail
		ArrayList priorApprovers = WorkflowDAO.getModuleChangePriorApprovers(moduleId,DBConn);
		WorkflowPriorApprover[] priorApproversArray = null;
		if (priorApprovers.size() > 0){
			int paSize = priorApprovers.size();
			priorApproversArray = new WorkflowPriorApprover[paSize];
			for (int pa=0; pa < paSize; pa++){
				priorApproversArray[pa] = (WorkflowPriorApprover) priorApprovers.get(pa);
			}
		}
		
		// add products so they can be used in email
		moduleChangeRequest.addModuleProducts(ProductModuleDAO.getModuleChangeProductsForRequest(moduleChangeRequest.getId(),DBConn));
		moduleChangeRequest.sendNotificationEmail(workflow,account,priorApproversArray, DBConn);
		
    }
 
}