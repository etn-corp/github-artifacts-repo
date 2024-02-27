/*
 * Created on May 5, 2005
 *
*/
package com.eaton.electrical.smrc.bo;

import java.util.*;
import java.sql.*;
import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.service.*;
/**
 * @author Jason Lubbert
 *
 */
public class UserApprovals implements java.io.Serializable {
    
    boolean showCustomerBox = false;
    boolean showDistributorBox = false;
    boolean showTerminationBox = false;
    boolean showProjectBox = false;
    boolean showTargetMarketBox = false;
    boolean showModuleChangeRequestBox = false;
    
    ArrayList customerRequests = null;
    ArrayList distributorsRequests = null;
    ArrayList terminationsRequests = null;
    ArrayList projectsRequests = null;
    ArrayList targetMarketPlansRequests = null;
    ArrayList moduleChangeRequests = null;
    HashMap tmAccount = null;
    
    int pendingApprovals = 0;
    User user = null;
    
	private static final long serialVersionUID = 100;

	// Only one constructor to force the assignment of booleans based on User
    public UserApprovals (User user){
        this.user = user;
        if (this.user.hasOverrideSecurity()){
            showCustomerBox = true;
            showDistributorBox = true;
            showTerminationBox = true;
            showProjectBox = true;
            showTargetMarketBox = true;
            showModuleChangeRequestBox = true;
        } else {
            	// These audits are in separate if's because a user can
                // have an SE title and be a project sales manager or champs manager
	            if (this.user.isSalesEngineer()){
	                showCustomerBox = true;
	                showDistributorBox = true;
		            showProjectBox = true;
		            showTargetMarketBox = true;
		            showModuleChangeRequestBox = true;
		        } 
	            if (this.user.isDistrictManager()){
		            showCustomerBox = true;
		            showDistributorBox = true;
		            showTerminationBox = true;
		            showProjectBox = true;
		            showTargetMarketBox = true;
		            showModuleChangeRequestBox = true;
		        } 
	            if (this.user.isZoneManager()){
		            showCustomerBox = true;
		            showDistributorBox = true;
		            showTerminationBox = true;
		        } 
	            if (this.user.isCreditManager()){
		            showDistributorBox = true;
		            showTerminationBox = true;
		        } 
	            if (this.user.isChannelMarketingManager()){
		            showDistributorBox = true;
		            showTerminationBox = true;
		            showTargetMarketBox = true;
		            showModuleChangeRequestBox = true;
		  //      } else if (this.user.isDivisionManager()){
		  //          showProjectBox = true;
		        } 
	            if (this.user.isCHAMPSManager()){
		            showProjectBox = true;
		        } 
	            if (this.user.isProjectSalesManager()){
		            showProjectBox = true;
		        }
        }
        
        // Users with segment overrides may approve customers in their segments
        ArrayList segmentOverrides = this.user.getSegmentOverrides();
        if (segmentOverrides.size() > 0){
            showCustomerBox = true;
            showModuleChangeRequestBox = true;
        }
        
        // Users that can update a district may be able to approve customer requests
        if (!showCustomerBox){
            ArrayList ugsList = this.user.getAllUGS();
            for (int i=0; i< ugsList.size(); i++){
                UserGeogSecurity ugs = (UserGeogSecurity) ugsList.get(i);
                if (ugs.ableToSeeDistrictLevel() && ugs.ableToUpdate()){
                    showCustomerBox = true;
                }
            }
        }
        
        // Users that can update a district may be able to approve module change requests
        if (!showModuleChangeRequestBox){
            ArrayList ugsList = this.user.getAllUGS();
            for (int i=0; i< ugsList.size(); i++){
                UserGeogSecurity ugs = (UserGeogSecurity) ugsList.get(i);
                if (ugs.ableToSeeDistrictLevel() && ugs.ableToUpdate()){
                	showModuleChangeRequestBox = true;
                }
            }
        }
        
        customerRequests = new ArrayList();
        distributorsRequests = new ArrayList();
        terminationsRequests = new ArrayList();
        projectsRequests = new ArrayList();
        targetMarketPlansRequests = new ArrayList();
        moduleChangeRequests = new ArrayList();
        tmAccount = new HashMap();
        
    }

    /**
     * @return Returns the customerRequests.
     */
    public ArrayList getCustomerRequests() {
        return customerRequests;
    }
    /**
     * @return Returns the distributorsRequests.
     */
    public ArrayList getDistributorsRequests() {
        return distributorsRequests;
    }
    /**
     * @return Returns the pendingApprovals.
     */
    public int getPendingApprovals() {
         return (customerRequests.size() + distributorsRequests.size() +
                 terminationsRequests.size() + projectsRequests.size() + 
                 targetMarketPlansRequests.size() + moduleChangeRequests.size());
    }
    /**
     * @return Returns the projectsRequests.
     */
    public ArrayList getProjectsRequests() {
        return projectsRequests;
    }
    /**
     * @return Returns the showCustomerBox.
     */
    public boolean isShowCustomerBox() {
        return showCustomerBox;
    }
    
    /**
     * @return Returns the showDistributorBox.
     */
    public boolean isShowDistributorBox() {
        return showDistributorBox;
    }
    /**
     * @return Returns the showProjectBox.
     */
    public boolean isShowProjectBox() {
        return showProjectBox;
    }
    /**
     * @return Returns the showTargetMarketBox.
     */
    public boolean isShowTargetMarketBox() {
        return showTargetMarketBox;
    }
    /**
     * @return Returns the showTerminationBox.
     */
    public boolean isShowTerminationBox() {
        return showTerminationBox;
    }
    
    public boolean isShowModuleChangeRequestBox(){
    	return showModuleChangeRequestBox;
    }
    
    /**
     * @return Returns the targetMarketPlansRequests.
     */
    public ArrayList getTargetMarketPlansRequests() {
        return targetMarketPlansRequests;
    }
    /**
     * @return Returns the terminationsRequests.
     */
    public ArrayList getTerminationsRequests() {
        return terminationsRequests;
    }
    
    public ArrayList getModuleChangeRequests(){
    	return moduleChangeRequests;
    }
    
    public void fetchApprovals(Connection DBConn) throws Exception {
        try {
	        if (showCustomerBox){
	            ArrayList tempCustRequests = WorkflowDAO.getCustomerPendingApprovals(DBConn);
	            customerRequests = checkApprovalSecurity(tempCustRequests,user,DBConn);
	        }
	        if (showDistributorBox){
	            distributorsRequests = WorkflowDAO.getWorkflowStepsForUser(user, Workflow.WORKFLOW_TYPE_DISTRIBUTOR_APPLICATION, DBConn);
	        }
	        if (showTerminationBox){
	            terminationsRequests = WorkflowDAO.getWorkflowStepsForUser(user, Workflow.WORKFLOW_TYPE_DISTRIBUTOR_TERMINATION, DBConn);
	        }
	        if (this.showModuleChangeRequestBox){
	        	ArrayList tempRequests = WorkflowDAO.getWorkflowStepsForUser(user, Workflow.WORKFLOW_PRODUCT_MODULE, DBConn);
	        	SMRCLogger.debug("initial list of module requests returned: " + tempRequests.size());
	        	this.moduleChangeRequests = checkApprovalSecurity(tempRequests,user,DBConn);
	        	SMRCLogger.debug("after checkApprovalSecurity, size is " + moduleChangeRequests.size());
	        }
	        if (showProjectBox){
	            projectsRequests = ProjectDAO.getApprovalPendingProjects(user,DBConn);
	        }
	        if (showTargetMarketBox){
	            targetMarketPlansRequests = TargetMarketDAO.getTargetMarketsPendingApproval(user,DBConn);
	             // We need one account id to use in the link to the target market plan on the
	            // pending approvals page. It has to be an account that the user can see or approve.
	            for (int i=0; i < targetMarketPlansRequests.size(); i++){
		    	    TargetMarket tm = (TargetMarket) targetMarketPlansRequests.get(i);
		    	    String acctId = "";
		    	    ArrayList planAccounts = tm.getPlanAccounts();
		    	    // It does not matter which account we use for channel marketing managers
		    	    if (user.isChannelMarketingManager()){
		    	        acctId = (String) planAccounts.get(0);
		    	    } else {
		    	        for (int x=0; x < planAccounts.size(); x++){
		    	            String account = (String) planAccounts.get(x);
		    	            String geog = AccountDAO.getAccountGeog(account,DBConn);
		    	            if (geog != null && geog.substring(0,5).equals(user.getGeography().substring(0,5))){
		    	                acctId = account;
		    	                break;
		    	            }
		    	        }
		    	    }
		    	    tmAccount.put(new Integer(tm.getTargetMarketPlanId()), acctId);
	            }
	        }
        } catch (Exception e) { 
            SMRCLogger.error("UserApprovals.fetchApprovals(): ", e);
            throw e;
        }
        
               
    }
    
      
    // This method returns a list of the WorkflowSteps the user is allowed to see.
    // I created this method to avoid creating an Account object for each vcn returned
    // to improve performance
    private ArrayList checkApprovalSecurity(ArrayList workflowSteps, User user, Connection DBConn) throws Exception{
        ArrayList returnList = new ArrayList();
//      if (usr.ableToUpdate(acct.getDistrict()) || usr.hasSegmentOverride(acct) || (usr.equals(acct.getUserIdAdded()) && acct.isProspect())){
        if (user.hasOverrideSecurity()){
            return workflowSteps;
        } else {
	        try {
		        for (int i=0; i< workflowSteps.size(); i++){
		            WorkflowStep step = (WorkflowStep) workflowSteps.get(i);
		            String acctId = step.getVcn();
		     //       String geog = AccountDAO.getAccountGeog(acctId, DBConn);
		            String geog = step.getCustomerGeog();
		            SMRCLogger.debug("UserApprovals.checkApprovalSecurity()  acctId=" + acctId + "  geog: " + geog);
		            if ((geog != null) && (user.ableToUpdate(geog))){
		                returnList.add(step);
		   //         } else if (user.getUserid().equalsIgnoreCase(AccountDAO.getAccountUserAdded(acctId,DBConn))){
		            } else if (user.getUserid().equalsIgnoreCase(step.getCustomerUserAdded())){        	
		                returnList.add(step);
		            } else if (acctId != null && user.ableToUpdate(AccountDAO.getAccount(acctId,DBConn))){
		            	returnList.add(step);
		            } else {
		                ArrayList userSegmentOverrides = user.getSegmentOverrides();
		                if (userSegmentOverrides.size() > 0){
		                    ArrayList acctSegments = SegmentsDAO.getSegmentsForAccount(acctId,DBConn);
		                    userSeg:
		                    for (int userSegIndex=0; userSegIndex < userSegmentOverrides.size(); userSegIndex++){
		                        int userSegId = ((Integer) userSegmentOverrides.get(userSegIndex)).intValue();
		                        for (int acctSegIndex = 0; acctSegIndex < acctSegments.size(); acctSegIndex++){
		                            Segment segment = (Segment) acctSegments.get(acctSegIndex);
		                            if (userSegId == segment.getSegmentId()){
		                                returnList.add(step);
		                                // Once one matches, jump out user segment loop
		                                break userSeg;
		                            }
		                        }
		                    } // end of userSeg
		                }
		            }
		        }
	        } catch (Exception e) {
	            SMRCLogger.error("UserApprovals.checkApprovalSecurity(): ", e);
	            throw e;
	        }
        }
        
        return returnList;
    }

	public HashMap getTmAccount() {
		return tmAccount;
	}

	public void setTmAccount(HashMap tmAccount) {
		this.tmAccount = tmAccount;
	}
    
}
