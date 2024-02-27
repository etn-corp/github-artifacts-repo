package com.eaton.electrical.smrc;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.exception.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

public class DistributorProductLoadingModule extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sFwdUrl = "/SMRCErrorPage.jsp";
        boolean redirect = false;
        Connection DBConn = null;
        User user = null;
        
        try {
            DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();

            HttpSession session = request.getSession(true);
            int srYear = Globals.a2int((String) session.getAttribute("srYear"));
            if ( srYear == 0 ) {
            	Calendar cal = new GregorianCalendar();
                // Get the components of the date
                srYear = cal.get(Calendar.YEAR);
            }

            user = SMRCSession.getUser(request, DBConn);

            String acctId = request.getParameter("acctId");
			Account acct = null;
			
			
			if(acctId!=null){
			    DistributorDAO.initializeDistributor(acctId, DBConn);
				acct = AccountDAO.getAccount(acctId, DBConn);
			}

            SMRCHeaderBean hdr = new SMRCHeaderBean();
            hdr.setUser(user);
            hdr.setAccount(acct);
            
            request.setAttribute("header", hdr);
	
        	Distributor dist = DistributorDAO.getDistributor(acctId, DBConn);
        	Account account = AccountDAO.getAccount(dist.getVcn(),DBConn);
        	ModuleChangeRequest moduleChangeRequest = ProductModuleDAO.getCurrentModuleChangeRequest(dist.getId(),DBConn);
        	Workflow workflow = null;
        	boolean hasCurrentChangeRequest = false;
        	boolean workflowAction = false;
        	ArrayList priorApprovers = new ArrayList();
        	
        	if (moduleChangeRequest != null){
        		hasCurrentChangeRequest = true;
        	}
        	
        	if (!account.isProspect()){
        		if (hasCurrentChangeRequest){
        			moduleChangeRequest.addModuleProducts(ProductModuleDAO.getModuleChangeProductsForRequest(moduleChangeRequest.getId(),DBConn));
        			workflow = WorkflowDAO.getWorkflow(moduleChangeRequest.getId()+"",Workflow.WORKFLOW_PRODUCT_MODULE,user,DBConn);
        			priorApprovers = WorkflowDAO.getModuleChangePriorApprovers(moduleChangeRequest.getId(),DBConn);
        		} 
        	} 
        	
        	
        	// User taking some action
        	if(request.getParameter("submitAction")!=null && !request.getParameter("submitAction").trim().equals("")){
        		String submitAction = request.getParameter("submitAction");
        		if (acct.isProspect()){
        			// For prospects, work with distrib_product_modules table 
        			// like before RIT32957, do not use module change request changes
        			if (submitAction.trim().equalsIgnoreCase("save")){
            			String[] moduleIds = request.getParameterValues("MODULES");
                		ProductModuleDAO.saveModules(acctId,moduleIds,DBConn);
                		// Get distributor object again to pick up changes to modules
                		dist = DistributorDAO.getDistributor(acctId, DBConn);
        			}
        		} else {
            		if (!hasCurrentChangeRequest){
	            		moduleChangeRequest = new ModuleChangeRequest();
	            		moduleChangeRequest.setDistributorId(dist.getId());
	            		moduleChangeRequest.setUserAdded(user.getUserid());
	            	} else {
	            		moduleChangeRequest.setUserChanged(user.getUserid());
	            	}
            		
            		moduleChangeRequest = ProductModuleDAO.saveOrUpdateModuleChangeRequest(moduleChangeRequest,DBConn);
            		if (!hasCurrentChangeRequest){
            			// If this request didn't already exist, create a new workflow for it
            			workflow = WorkflowDAO.getWorkflow(moduleChangeRequest.getId()+"",Workflow.WORKFLOW_PRODUCT_MODULE,user,DBConn);
            		}
            		
            		String moduleRequestProductVistaStatus = ProductModuleDAO.MODULE_CHANGE_VISTA_STATUS_IN_PROCESS;
            		
            		if (submitAction.trim().equalsIgnoreCase("approve") ||
            				submitAction.trim().equalsIgnoreCase("reject") ||
            				submitAction.trim().equalsIgnoreCase("reviseResubmit") ||
            				submitAction.trim().equalsIgnoreCase("resubmit")){
            			
            				workflowAction = true; 
            				WorkflowStep[] currentSteps = workflow.getCurrentSteps();
            				int approvalId = 0;
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
            				} else if (submitAction.trim().equalsIgnoreCase("reviseResubmit")){
            					status = "V";
            					int reviseResubmitId = Globals.a2int(request.getParameter("resubmitApprovalId"));
            					SMRCLogger.debug("revise resubmit to " + reviseResubmitId);
            					//	now update all the workflow_approvals with module change request id that has data changed later than 
            	            	// the date_changed for the given work_approval_id
            					WorkflowDAO.updateModuleChangeWorkflowLaterApproversToNotApproved(moduleChangeRequest.getId(), reviseResubmitId, DBConn);
            					approvalId = reviseResubmitId;
            					if (request.getParameter("reviseReason") != null && !request.getParameter("reviseReason").trim().equalsIgnoreCase("")){
            						ReviseResubExplanationDAO.insertReviseResubExplanationWithoutTargetProject(reviseResubmitId, request.getParameter("reviseReason"), user.getUserid(), DBConn);//user that submitted the resive resub.
            					}
            	            	
            				}
            				WorkflowDAO.changeApprovalStatus(approvalId +"",user.getUserid(),status,account.getVcn(),DBConn);
            				// Get updated workflow after change
            				workflow = WorkflowDAO.getWorkflow(moduleChangeRequest.getId()+"",Workflow.WORKFLOW_PRODUCT_MODULE,user,DBConn);
            		}
            		
            		if (request.getParameter("changeReason") != null && !request.getParameter("changeReason").trim().equals("")){
            			String changeReason = request.getParameter("changeReason");
            			ModuleChangeReasonNotes note = new ModuleChangeReasonNotes();
            			note.setModuleChangeRequestId(moduleChangeRequest.getId());
            			note.setReasonNotes(changeReason);
            			note.setUserAdded(user.getUserid());
            			ProductModuleDAO.insertModuleChangeReasonNote(note,DBConn);
            		}
            		
            		// Delete existing moduleChangeProduct records for this request, add new ones
            		// below as updates
            		ProductModuleDAO.deleteExistingProducts(moduleChangeRequest.getId(),DBConn);
            		
            		long vistaStatusId = ProductModuleDAO.getModuleChangeVistaStatusId(moduleRequestProductVistaStatus,DBConn);
            		Enumeration enumeration = request.getParameterNames();
            		ArrayList newProducts = new ArrayList();
            		while (enumeration.hasMoreElements()){
            			String paramName = (String) enumeration.nextElement();
            			if (paramName.startsWith("actionCode_modId_")){
            				//SMRCLogger.debug(paramName + " = " + request.getParameter(paramName));
            				String action = request.getParameter(paramName);
            				if (action != null && !action.trim().equals("")){
	            				int moduleId = Integer.parseInt(paramName.substring(17));
	            				boolean productExisted = false;
	            				ModuleChangeProduct product = new ModuleChangeProduct();
	            				// See if this product existed already on request
	            				if (moduleChangeRequest.getModuleProducts() != null){
	            					for (int i=0; i < moduleChangeRequest.getModuleProducts().length; i++){
	            						ModuleChangeProduct oldProduct = moduleChangeRequest.getModuleProducts()[i];
	            						if (oldProduct.getModuleId() == moduleId){
	            							product.setUserAdded(oldProduct.getUserAdded());
	            							product.setDateAdded(oldProduct.getDateAdded());
	            							productExisted = true;
	            						}
	            					}
	            				}
	            				product.setAction(request.getParameter(paramName));
	            				if (!productExisted){
	            					product.setDateAdded(new Date());
	            					SMRCLogger.debug("Date added: " + product.getDateAdded().toString());
	            					product.setUserAdded(user.getUserid());
	            				}
	            				product.setDateChanged(new Date());
	            				product.setModuleChangeRequestId(moduleChangeRequest.getId());
	            				product.setModuleId(moduleId);
	            				product.setUserChanged(user.getUserid());
	            				product.setVistaStatusId(vistaStatusId);
	            				ProductModuleDAO.updateModuleChangeProduct(product,DBConn);
	            			}  // end of if checking for an action
            			} // end of looping through module parameters passed in for module change requests
            		} // end of looping through all parameters passed in 
            		
    				
            		// Remove previous modules products and replace with new fully loaded objects
            		moduleChangeRequest.removeAllModuleProducts();
            		moduleChangeRequest.addModuleProducts(ProductModuleDAO.getModuleChangeProductsForRequest(moduleChangeRequest.getId(),DBConn));
            		
            		
            		if (!hasCurrentChangeRequest){
            			// If user has taken action, then we now have a current change request if 
            			// we didn't before
            			hasCurrentChangeRequest = true;
            		}
            		
            		
                	
        		} // end else of "if prospect"
        		
        	}  // end of User taking some action
        	
        	
        	//	 Get all change reasons notes here to include any newly added notes.
        	if (hasCurrentChangeRequest){
        		moduleChangeRequest.setModuleChangeReasonNotes(ProductModuleDAO.getModuleChangeReasonNotesForRequest(moduleChangeRequest.getId(),DBConn));
        		boolean canApprove = canApproveModuleChangeRequest(user,workflow, account);
        		boolean newNoteRequired = false;
        		boolean onResubmit = false;
        		WorkflowStep[] currentSteps = workflow.getCurrentSteps();
        		for (int i=0; i < currentSteps.length; i++){
        			// At least one note has to be attached to the request when someone approves the first step
        			if (currentSteps[i].getStepName().trim().equalsIgnoreCase(WorkflowStep.STEP_NAME_INITIAL_ACCOUNT_INFO) &&
        					(moduleChangeRequest.getModuleChangeReasonNotes() == null ||
        							moduleChangeRequest.getModuleChangeReasonNotes().length == 0)){
        				newNoteRequired = true;
        			}
        			if (currentSteps[i].getStatusFlag().trim().equalsIgnoreCase("V")){
        				onResubmit = true;
        			}
        		}
        		
        		// Do not send email until everything has been updated within objects involved
        		if (workflowAction){
        			WorkflowPriorApprover[] priorApproversArray = null;
        			if (priorApprovers.size() > 0){
        				int paSize = priorApprovers.size();
        				priorApproversArray = new WorkflowPriorApprover[paSize];
        				for (int pa=0; pa < paSize; pa++){
        					priorApproversArray[pa] = (WorkflowPriorApprover) priorApprovers.get(pa);
        				}
        			}
        			moduleChangeRequest.sendNotificationEmail(workflow,account,priorApproversArray, DBConn);
        		}
        		
        		request.setAttribute("newNoteRequired", new Boolean(newNoteRequired));
        		request.setAttribute("onResubmit", new Boolean(onResubmit));
        		request.setAttribute("moduleChangeRequest", moduleChangeRequest);
        		request.setAttribute("workflow", workflow);
        		request.setAttribute("canApprove", new Boolean(canApprove));
        		
        	}
        	
        	
        	
        	boolean missingSE = false;
        	if (!acct.isProspect() && 
        		StringManipulation.noNull(acct.getSalesEngineer1()).trim().equals("") && 
        		StringManipulation.noNull(acct.getSalesEngineer2()).trim().equals("") &&
        		StringManipulation.noNull(acct.getSalesEngineer3()).trim().equals("") &&
        		StringManipulation.noNull(acct.getSalesEngineer4()).trim().equals("")
        		){
        		missingSE = true;
        	}
        	
        	ArrayList selectedModules = dist.getLoadModules();
        	ArrayList productModules = ProductModuleDAO.getProductModules(srYear, DBConn);
        	boolean ableToUpdate = canUpdateProductModulePage(user,account,workflow);
        	
        	request.setAttribute("missingSE", new Boolean(missingSE));
        	request.setAttribute("ableToUpdate",new Boolean(ableToUpdate));
        	request.setAttribute("hasCurrentChangeRequest", new Boolean(hasCurrentChangeRequest));
        	request.setAttribute("selectedModules",selectedModules);
        	request.setAttribute("productModules",productModules);
        	sFwdUrl = "/distProductModulesForm.jsp";

            

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
	
	private boolean canApproveModuleChangeRequest (User user, Workflow workflow, Account account) {
		boolean canApprove = false;
		WorkflowStep[] currentSteps = workflow.getCurrentSteps();
		for (int i=0; i < currentSteps.length; i++){
			WorkflowStep step = currentSteps[i];
			if (step.getStepName().equalsIgnoreCase(WorkflowStep.STEP_NAME_INITIAL_ACCOUNT_INFO)){
				System.out.println(WorkflowStep.STEP_NAME_INITIAL_ACCOUNT_INFO);
				System.out.println("USER ADDED + " + account.getUserIdAdded());
				canApprove = user.ableToUpdate(account) || user.hasSegmentOverride(account) || (user.equals(account.getUserIdAdded()) && account.isProspect());
				System.out.println("canApprove-java = " + canApprove);
			} else if (step.getStepName().equalsIgnoreCase(WorkflowStep.STEP_NAME_DISTRICT_MANAGER)){
				System.out.println(WorkflowStep.STEP_NAME_DISTRICT_MANAGER);
				if (user.hasOverrideSecurity()){
					System.out.println("2");
					canApprove = true;
				} else if (user.isDistrictManager()){
					System.out.println("3");
					// if (geog!= null && userGeog.substring(0, 5).equalsIgnoreCase(geog.substring(0, 5))) {
                   // return true;
					String userGeog = user.getGeography();
					String acctGeog = account.getDistrict();
					if (acctGeog != null && userGeog != null){
						if (userGeog.substring(0,5).equalsIgnoreCase(acctGeog.substring(0,5))){
							canApprove = true;
						}
					}
                }
				
			} else if (step.getStepName().equalsIgnoreCase(WorkflowStep.STEP_NAME_GLOBAL_CHANNEL_MANAGER)){
				System.out.println(WorkflowStep.STEP_NAME_GLOBAL_CHANNEL_MANAGER);
				if (user.isChannelMarketingManager() || user.hasOverrideSecurity()){
					canApprove = true;
				}
			}
		}
		
		return canApprove;
	}
	
	private boolean canUpdateProductModulePage (User user, Account account, Workflow workflow){
		boolean ableToUpdate = false;
		if (workflow == null){
			ableToUpdate = user.ableToUpdate(account) || user.hasSegmentOverride(account) || (user.equals(account.getUserIdAdded()) && account.isProspect());
			
		} else {
			ableToUpdate = canApproveModuleChangeRequest(user,workflow,account);
		}
		return ableToUpdate;
	}

} //class
