package com.eaton.electrical.smrc.dao;

import java.sql.*;
import java.util.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

/**
 * @author E0062708
 *
 */
public class WorkflowDAO {

	private static final String isWorkflowInitializedQuery="select count(*)"+
		" from workflow_approvals t,"+
		"workflow_step_sequences,"+
		"workflows"+
		" where workflow_step_sequences.workflow_step_sequence_id = t.workflow_step_sequence_id"+
		" and workflows.workflow_id = workflow_step_sequences.workflow_id and workflow_name=? and vista_customer_number=?";
	private static final String isTargetMarketWorkflowInitializedQuery="select count(*)"+
	" from workflow_approvals t,"+
	"workflow_step_sequences,"+
	"workflows"+
	" where workflow_step_sequences.workflow_step_sequence_id = t.workflow_step_sequence_id"+
	" and workflows.workflow_id = workflow_step_sequences.workflow_id and workflow_name=? and target_market_plan_id =?";
	private static final String isModuleChangeWorkflowInitializedQuery="select count(*)"+
	" from workflow_approvals t,"+
	"workflow_step_sequences,"+
	"workflows"+
	" where workflow_step_sequences.workflow_step_sequence_id = t.workflow_step_sequence_id"+
	" and workflows.workflow_id = workflow_step_sequences.workflow_id and workflow_name=? and t.module_change_requests_id =?";
	private static final String getWorkflowStepsQuery="select workflows.workflow_name,"+
       "workflow_steps.workflow_step_name,"+
       "workflow_step_sequences.current_step_id,"+
       "workflow_step_sequences.prev_step_id,"+
       "workflow_step_sequences.workflow_step_sequence_id,"+
       "workflows.workflow_id,"+
       "workflows.workflow_name"+
	   " from workflows,"+
       "workflow_step_sequences,"+
       "workflow_steps"+
	   " where workflows.workflow_id = workflow_step_sequences.workflow_id"+
       " AND WORKFLOW_NAME=?"+
       " and workflow_steps.workflow_step_id = workflow_step_sequences.current_step_id";
	private static final String initializeWorkflowInsert = "INSERT INTO WORKFLOW_APPROVALS (WORKFLOW_APPROVAL_ID,VISTA_CUSTOMER_NUMBER,CURRENT_STEP_ID,APPROVED_FLAG,DATE_ADDED,WORKFLOW_STEP_SEQUENCE_ID,USER_ADDED)" +
		" VALUES(WORKFLOW_APPROVALS_SEQ.NEXTVAL,?,?,'N',SYSDATE,?,?)";
	private static final String initializeTargetMarketWorkflowInsert = "INSERT INTO WORKFLOW_APPROVALS (WORKFLOW_APPROVAL_ID,target_market_plan_id,CURRENT_STEP_ID,APPROVED_FLAG,DATE_ADDED,WORKFLOW_STEP_SEQUENCE_ID,USER_ADDED)" +
	" VALUES(WORKFLOW_APPROVALS_SEQ.NEXTVAL,?,?,'N',SYSDATE,?,?)";
	private static final String initializeModuleChangeWorkflowInsert = "INSERT INTO WORKFLOW_APPROVALS (WORKFLOW_APPROVAL_ID,VISTA_CUSTOMER_NUMBER,CURRENT_STEP_ID,APPROVED_FLAG,DATE_ADDED," +
			" WORKFLOW_STEP_SEQUENCE_ID,USER_ADDED,MODULE_CHANGE_REQUESTS_ID) VALUES(WORKFLOW_APPROVALS_SEQ.NEXTVAL, " +
			" ( select d.vista_customer_number from module_change_requests mcr, distributors d where mcr.module_change_requests_id = ?" +
			"   and mcr.distributor_id = d.distributor_id ),?,'N',SYSDATE,?,?,?)";
	private static final String getWorkflowStepsForAccountQuery="select * from workflows_jv where workflow_name=?" +
		" and VISTA_CUSTOMER_NUMBER=? and APPROVED_FLAG != 'Z'"+
		" order by workflow_step_id";
	private static final String getWorkflowStepsForModuleChangeQuery="select * from workflows_jv where workflow_name=?" +
	" and module_change_requests_id=?"+
	" order by workflow_step_id";
	private static final String getWorkflowStepsForTargetMarketQuery="select * from workflows_jv where workflow_name=?" +
	" and target_market_plan_id=?"+
	" order by workflow_step_id";
	private static final String getWorkflowStepsForUser1="select a.*, c.customer_name customer_name, c.vista_customer_number vcn, b.date_changed prev_changed from workflows_jv a, workflows_jv b, customer c"+
		" where ((b.approved_flag='Y'"+
		" and (a.approved_flag = 'N' or a.approved_flag = 'V')) OR ((a.approved_flag = 'N' or a.approved_flag = 'V') and a.prev_step_id is null))"+
		" and a.prev_step_id = b.current_step_id(+)"+
		" and a.workflow_id=b.workflow_id(+)"+
		" and a.workflow_step_name=?" +
		" and a.vista_customer_number = b.vista_customer_number(+)"+
		" and a.vista_customer_number=c.vista_customer_number" +
		" and a.workflow_name = ? " +
		" order by prev_changed desc";
	private static final String getWorkflowStepsForUser2="select a.*, c.customer_name customer_name, c.vista_customer_number vcn, b.date_changed prev_changed from workflows_jv a, workflows_jv b, customer c"+
		" where ((b.approved_flag='Y'"+
		" and (a.approved_flag = 'N' or a.approved_flag = 'V')) OR ((a.approved_flag = 'N' or a.approved_flag = 'V') and a.prev_step_id is null))"+
		" and a.prev_step_id = b.current_step_id(+)"+		
		" and a.workflow_id=b.workflow_id(+)"+
		" and a.workflow_step_name='District Manager'" +
		" and a.vista_customer_number = b.vista_customer_number(+)"+
		" and a.vista_customer_number=c.vista_customer_number" +
		" and a.workflow_name = ? " +
		" and substr(sp_geog,0,5)=substr(?,0,5)" + 
		" order by prev_changed desc";
	private static final String getWorkflowStepsForUser3="select a.*, c.customer_name customer_name, c.vista_customer_number vcn, b.date_changed prev_changed  from workflows_jv a, workflows_jv b, customer c"+
		" where ((b.approved_flag='Y'"+
		" and (a.approved_flag = 'N' or a.approved_flag = 'V')) OR ((a.approved_flag = 'N' or a.approved_flag = 'V') and a.prev_step_id is null))"+
		" and a.prev_step_id = b.current_step_id(+)"+		
		" and a.workflow_id=b.workflow_id(+)"+
		" and a.workflow_step_name='Zone Manager'" +
		" and a.vista_customer_number = b.vista_customer_number(+)"+
		" and a.vista_customer_number=c.vista_customer_number" +
		" and a.workflow_name = ? " +
		" and substr(sp_geog,0,4)=substr(?,0,4)" +
		" order by prev_changed desc";
	private static final String getWorkflowStepsForUser4="select a.*, c.customer_name customer_name, c.vista_customer_number vcn, b.date_changed prev_changed  from workflows_jv a, workflows_jv b, customer c"+
	" where ((b.approved_flag='Y'"+
	" and (a.approved_flag = 'N' or a.approved_flag = 'V')) OR ((a.approved_flag = 'N' or a.approved_flag = 'V') and a.prev_step_id is null))"+
	" and a.prev_step_id = b.current_step_id(+)"+		
	" and a.workflow_id=b.workflow_id(+)"+
	" and a.workflow_step_name='Initial Account Information'" + //'Initial Account Information'
	" and a.vista_customer_number = b.vista_customer_number(+)"+
	" and a.vista_customer_number=c.vista_customer_number" +
	" and a.workflow_name = ? " +
	" and substr(sp_geog,0,5)=substr(?,0,5)" + //not sure about '5'
	" order by prev_changed desc";	
	private static final String changeApprovalStatusUpdate="UPDATE WORKFLOW_APPROVALS SET APPROVED_FLAG=?, USER_CHANGED=?, DATE_CHANGED=SYSDATE WHERE WORKFLOW_APPROVAL_ID=?";
	private static final String getCustomerPendingApprovalsQuery = "select c.vista_customer_number vcn, c.customer_name, c.user_added customer_user_added, c.sp_geog, wjv.* from workflows_jv wjv, customer c where wjv.workflow_name = 'Customer' and wjv.current_step_id = 1 " +
		"and wjv.date_changed is null and wjv.vista_customer_number like 'P%' and c.vista_customer_number = wjv.vista_customer_number";
	
	private static final String getWorkflowStepsForModuleUser1="select a.*, c.customer_name customer_name, c.vista_customer_number vcn, b.date_changed prev_changed from workflows_jv a, workflows_jv b, customer c"+
			" where ((b.approved_flag='Y'"+
			" and (a.approved_flag = 'N' or a.approved_flag = 'V')) OR ((a.approved_flag = 'N' or a.approved_flag = 'V') and a.prev_step_id is null))"+
			" and a.prev_step_id = b.current_step_id(+)"+
			" and a.workflow_id=b.workflow_id(+)"+
			" and a.workflow_step_name=?" +
			" and a.module_change_requests_id = b.module_change_requests_id(+)"+
			" and a.vista_customer_number=c.vista_customer_number" +
			" and a.workflow_name = ? " +
			" order by prev_changed desc";
	private static final String getWorkflowStepsForModuleUser2="select a.*, c.customer_name customer_name, c.vista_customer_number vcn, b.date_changed prev_changed from workflows_jv a, workflows_jv b, customer c"+
			" where ((b.approved_flag='Y'"+
			" and (a.approved_flag = 'N' or a.approved_flag = 'V')) OR ((a.approved_flag = 'N' or a.approved_flag = 'V') and a.prev_step_id is null))"+
			" and a.prev_step_id = b.current_step_id(+)"+		
			" and a.workflow_id=b.workflow_id(+)"+
			" and a.workflow_step_name='District Manager'" +
			" and a.module_change_requests_id = b.module_change_requests_id(+)"+
			" and a.vista_customer_number=c.vista_customer_number" +
			" and a.workflow_name = ? " +
			" and substr(sp_geog,0,5)=substr(?,0,5)" + 
			" order by prev_changed desc";
		private static final String getWorkflowStepsForModuleUser4="select a.*, c.customer_name customer_name, c.vista_customer_number vcn, b.date_changed prev_changed  from workflows_jv a, workflows_jv b, customer c"+
			" where ((b.approved_flag='Y'"+
			" and (a.approved_flag = 'N' or a.approved_flag = 'V')) OR ((a.approved_flag = 'N' or a.approved_flag = 'V') and a.prev_step_id is null))"+
			" and a.prev_step_id = b.current_step_id(+)"+		
			" and a.workflow_id=b.workflow_id(+)"+
			" and a.workflow_step_name='Initial Account Information'" + //'Initial Account Information'
			" and a.module_change_requests_id = b.module_change_requests_id(+)"+
			" and a.vista_customer_number=c.vista_customer_number" +
			" and a.workflow_name = ? " +
			" and substr(sp_geog,0,5)=substr(?,0,5)" + //not sure about '5'
			" order by prev_changed desc";	
	
	private static final String getWorkflowPriorApprovers = "select wv.workflow_step_name, u.first_name, u.last_name, u.userid, wv.workflow_approval_id from workflows_jv wv, users u where wv.vista_customer_number = ? and wv.approved_flag = 'Y' and wv.user_changed = u.userid";
	private static final String getModuleChangePriorApprovers = "select wv.workflow_step_name, u.first_name, u.last_name, u.userid, wv.workflow_approval_id from workflows_jv wv, users u where wv.module_change_requests_id = ? and wv.approved_flag = 'Y' and wv.user_changed = u.userid order by wv.workflow_approval_id asc";
	
	private static final String getWorkflowApprovalUserChanged = "select wa.user_changed from workflow_approvals wa where wa.workflow_approval_id = ?";
	
	private static final String updateWorkflowLaterApproversToNotApproved = 
		" update workflow_approvals wa_upd " + 
   " set wa_upd.approved_flag = 'N', " +
   	   " wa_upd.date_changed = null, " +
   	   " wa_upd.user_changed = null " +
 " where wa_upd.workflow_approval_id in " +
       " (select wa_out.workflow_approval_id " +
          " from workflow_approvals wa_out " +
         " where wa_out.vista_customer_number = ? " +
           " and wa_out.date_changed > " +
               " (select wa_in.date_changed " +
                  " from workflow_approvals wa_in " +
                 " where wa_in.workflow_approval_id = ?)) ";
	
	private static final String updateTargetMarketWorkflowLaterApproversToNotApproved = 
		" update workflow_approvals wa_upd " + 
   " set wa_upd.approved_flag = 'N', " +
   	   " wa_upd.date_changed = null, " +
   	   " wa_upd.user_changed = null " +
 " where wa_upd.workflow_approval_id in " +
       " (select wa_out.workflow_approval_id " +
          " from workflow_approvals wa_out " +
         " where wa_out.target_market_plan_id = ? " +
           " and wa_out.date_changed > " +
               " (select wa_in.date_changed " +
                  " from workflow_approvals wa_in " +
                 " where wa_in.workflow_approval_id = ?)) ";
	
	private static final String updateModuleChangeWorkflowLaterApproversToNotApproved = 
		" update workflow_approvals wa_upd " + 
   " set wa_upd.approved_flag = 'N', " +
   	   " wa_upd.date_changed = null, " +
   	   " wa_upd.user_changed = null " +
 " where wa_upd.workflow_approval_id in " +
       " (select wa_out.workflow_approval_id " +
          " from workflow_approvals wa_out " +
         " where wa_out.module_change_requests_id = ? " +
           " and wa_out.date_changed > " +
               " (select wa_in.date_changed " +
                  " from workflow_approvals wa_in " +
                 " where wa_in.workflow_approval_id = ?)) ";

	private static final String getTargetMarketPlanWorkflowPriorApprovers = "select wv.workflow_step_name, u.first_name, u.last_name, u.userid, wv.workflow_approval_id from workflows_jv wv, users u where wv.target_market_plan_id = ? and wv.user_changed = u.userid and wv.approved_flag = 'Y'";
	
	private static final String targetMarketIsPendingResubmission = "select * from workflow_approvals t where t.target_market_plan_id = ? and t.approved_flag = 'V'";
		/**
	 * 
	 * @param foreignKeyId  - vista customer number or target market plan id
	 * @param workflowType
	 * @param DBConn
	 * @return
	 * @throws Exception
	 */
	private static boolean isWorkflowInitialized(String foreignKeyId, String workflowType, Connection DBConn) throws Exception{
		int count=0;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			if (workflowType.trim().equalsIgnoreCase("Target Market Plans")){
				pstmt = DBConn.prepareStatement(isTargetMarketWorkflowInitializedQuery);
			} else if (workflowType.trim().equalsIgnoreCase(Workflow.WORKFLOW_PRODUCT_MODULE)){
				pstmt = DBConn.prepareStatement(isModuleChangeWorkflowInitializedQuery);
			} else {
				pstmt = DBConn.prepareStatement(isWorkflowInitializedQuery);
			}
			pstmt.setString(1,workflowType);
			pstmt.setString(2,foreignKeyId);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
					count=rs.getInt(1);
			}
			if(count==0){
				return false;
			}
			return true;
			
		}catch (Exception e)	{
			SMRCLogger.error("WorkflowDAO.isWorkflowInitialized(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
	}	
	
	
	public static ArrayList getWorkflowSteps(String workflowType, Connection DBConn) throws Exception{
		ArrayList steps = new ArrayList();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			pstmt = DBConn.prepareStatement(getWorkflowStepsQuery);
			pstmt.setString(1,workflowType);
			//pstmt.setString(2,acctId);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				WorkflowStep step = new WorkflowStep();	
				//step.setVcn(acctId);
				step.setStepId(rs.getInt("CURRENT_STEP_ID"));
				step.setStepName(rs.getString("WORKFLOW_STEP_NAME"));
				step.setPrevStepId(rs.getInt("PREV_STEP_ID"));
				step.setSequenceId(rs.getInt("WORKFLOW_STEP_SEQUENCE_ID"));
				step.setWorkflowId(rs.getInt("WORKFLOW_ID"));
				step.setWorkflowName(rs.getString("WORKFLOW_NAME"));

				steps.add(step);
			}

		}catch (Exception e)	{
			SMRCLogger.error("WorkflowDAO.getWorkflowSteps(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return steps;
	}		
	
	/**
	 * 
	 * @param foreignKeyId  - vista customer number or target market plan id
	 * @param workflowType
	 * @param DBConn
	 * @throws Exception
	 */
	public static void initializeWorkflow(String foreignKeyId, String workflowType, User user,Connection DBConn) throws Exception{
		SMRCLogger.debug("WorkflowDAO.initializeWorkflow  foreignKeyId=" + foreignKeyId + " workflowType: " + workflowType);
		if(isWorkflowInitialized(foreignKeyId,workflowType,DBConn)){
			return;
		}
		
		int count=0;
		ArrayList steps = getWorkflowSteps(workflowType, DBConn);

		PreparedStatement pstmt = null;
		
		try {
			/*
			 * private static final String initializeModuleChangeWorkflowInsert = "INSERT INTO WORKFLOW_APPROVALS (WORKFLOW_APPROVAL_ID,VISTA_CUSTOMER_NUMBER,CURRENT_STEP_ID,APPROVED_FLAG,DATE_ADDED," +
			" WORKFLOW_STEP_SEQUENCE_ID,USER_ADDED,MODULE_CHANGE_REQUESTS_ID) VALUES(WORKFLOW_APPROVALS_SEQ.NEXTVAL, " +
			" ( select d.vista_customer_number from module_change_requests mcr, distributors d where mcr.module_change_requests_id = ?" +
			"   and mcr.distributor_id = d.distributor_id ),?,'N',SYSDATE,?,?,?)";
			 */
			if (workflowType.trim().equalsIgnoreCase("Target Market Plans")){
				pstmt = DBConn.prepareStatement(initializeTargetMarketWorkflowInsert);
			} else if (workflowType.trim().equalsIgnoreCase(Workflow.WORKFLOW_PRODUCT_MODULE)){
				pstmt = DBConn.prepareStatement(initializeModuleChangeWorkflowInsert);
			} else {
				pstmt = DBConn.prepareStatement(initializeWorkflowInsert);
			}

			for(int i=0; i<steps.size();i++){
				WorkflowStep step = (WorkflowStep)steps.get(i);
				int pIndex = 0 ;
				pstmt.setString(++pIndex,foreignKeyId);
				pstmt.setInt(++pIndex,step.getStepId());
				pstmt.setInt(++pIndex,step.getSequenceId());
				pstmt.setString(++pIndex,user.getUserid());
				if (workflowType.trim().equalsIgnoreCase(Workflow.WORKFLOW_PRODUCT_MODULE)){
					pstmt.setString(++pIndex,foreignKeyId);
				}
				count = count+pstmt.executeUpdate();
			}
			
			
		}catch (Exception e)	{
			SMRCLogger.error("WorkflowDAO.initializeWorkflow(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
	}		
	
	private static ArrayList getWorkflowStepsForAccount(String acctId, String workflowType, Connection DBConn) throws Exception{
		ArrayList steps = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			if (workflowType.trim().equalsIgnoreCase(Workflow.WORKFLOW_TYPE_TARGET_MARKET_PLANS)){
				pstmt = DBConn.prepareStatement(getWorkflowStepsForTargetMarketQuery);
				pstmt.setInt(2,Integer.parseInt(acctId));
			} else if (workflowType.trim().equalsIgnoreCase(Workflow.WORKFLOW_PRODUCT_MODULE)){
				pstmt = DBConn.prepareStatement(getWorkflowStepsForModuleChangeQuery);
				pstmt.setString(2,acctId);
			} else {
				pstmt = DBConn.prepareStatement(getWorkflowStepsForAccountQuery);
				pstmt.setString(2,acctId);
			}
			pstmt.setString(1,workflowType);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				WorkflowStep step = new WorkflowStep();	

				step.setApprovalId(rs.getInt("WORKFLOW_APPROVAL_ID"));
				step.setStepId(rs.getInt("CURRENT_STEP_ID"));
				step.setStepName(rs.getString("WORKFLOW_STEP_NAME"));
				step.setPrevStepId(rs.getInt("PREV_STEP_ID"));
				step.setSequenceId(rs.getInt("WORKFLOW_STEP_SEQUENCE_ID"));
				step.setWorkflowId(rs.getInt("WORKFLOW_ID"));
				step.setWorkflowName(rs.getString("WORKFLOW_NAME"));
				step.setDateAdded(rs.getTimestamp("DATE_ADDED"));
				step.setDateChanged(rs.getTimestamp("DATE_CHANGED"));
				step.setUserAdded(StringManipulation.noNull(rs.getString("USER_ADDED")));
				step.setUserChanged(StringManipulation.noNull(rs.getString("USER_CHANGED")));
				step.setStatusFlag(StringManipulation.noNull(rs.getString("APPROVED_FLAG")));
				if(step.isApproved() || step.isRejected() || step.isRevisedAndResubmitted()){
					step.setUserApprovedOrRejected(UserDAO.getUser(step.getUserChanged(),DBConn));
				}
				steps.add(step);
			}
		}catch (Exception e)	{
			SMRCLogger.error("WorkflowDAO.getWorkflowStepsForAccount(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return steps;
	}		
	
	public static ArrayList getWorkflowStepsForUser(User usr, String workflowType, Connection DBConn) throws Exception{
		ArrayList steps = new ArrayList();

		//Added Sales Engineer to the list.
		if(!usr.getUserGroup().equals("Sales Engineer") && !usr.getUserGroup().equals("Credit Manager") && !usr.getUserGroup().equals("Global Channel Manager") && !usr.getUserGroup().equals("District Manager") && !usr.getUserGroup().equals("Zone Manager")){
			return steps;	
		}
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			if(usr.getUserGroup().equals("Credit Manager") || usr.getUserGroup().equals("Global Channel Manager")){
				if (workflowType.trim().equalsIgnoreCase(Workflow.WORKFLOW_PRODUCT_MODULE)){
					pstmt = DBConn.prepareStatement(getWorkflowStepsForModuleUser1);
				} else {
					pstmt = DBConn.prepareStatement(getWorkflowStepsForUser1);
				}
				pstmt.setString(1,usr.getUserGroup());
				pstmt.setString(2,workflowType);
			}else if(usr.getUserGroup().equals("District Manager")){
				if (workflowType.trim().equalsIgnoreCase(Workflow.WORKFLOW_PRODUCT_MODULE)){
					pstmt = DBConn.prepareStatement(getWorkflowStepsForModuleUser2);
				} else {
					pstmt = DBConn.prepareStatement(getWorkflowStepsForUser2);
				}
				pstmt.setString(1,workflowType);
				pstmt.setString(2,usr.getGeography());
			}else if(usr.getUserGroup().equals("Zone Manager")){
				// Zone manager not used for Module Change Requests so there's
				// no need to check the workflow type
				pstmt = DBConn.prepareStatement(getWorkflowStepsForUser3);
				pstmt.setString(1,workflowType);
				pstmt.setString(2,usr.getGeography());
			}
			//added the else if below for Sales Engineer
			else if(usr.getUserGroup().equals("Sales Engineer")){
				if (workflowType.trim().equalsIgnoreCase(Workflow.WORKFLOW_PRODUCT_MODULE)){
					pstmt = DBConn.prepareStatement(getWorkflowStepsForModuleUser4);
				} else {
					pstmt = DBConn.prepareStatement(getWorkflowStepsForUser4);
				}
				pstmt.setString(1,workflowType);
				pstmt.setString(2,usr.getGeography());
			}
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				WorkflowStep step = new WorkflowStep();	

				step.setApprovalId(rs.getInt("WORKFLOW_APPROVAL_ID"));
				step.setStepId(rs.getInt("CURRENT_STEP_ID"));
				step.setStepName(rs.getString("WORKFLOW_STEP_NAME"));
				step.setPrevStepId(rs.getInt("PREV_STEP_ID"));
				step.setSequenceId(rs.getInt("WORKFLOW_STEP_SEQUENCE_ID"));
				step.setWorkflowId(rs.getInt("WORKFLOW_ID"));
				step.setWorkflowName(rs.getString("WORKFLOW_NAME"));
				step.setStatusFlag(StringManipulation.noNull(rs.getString("APPROVED_FLAG")));
				if(step.isApproved() || step.isRejected()){
					step.setUserApprovedOrRejected(UserDAO.getUser(step.getUserChanged(),DBConn));
				}				
				step.setDateAdded(rs.getDate("DATE_ADDED"));
				step.setDateChanged(rs.getDate("DATE_CHANGED"));
				step.setUserAdded(rs.getString("USER_ADDED"));
				step.setUserChanged(rs.getString("USER_CHANGED"));
				step.setCustomerName(rs.getString("customer_name"));
				step.setVcn(rs.getString("vcn"));
				step.setPreviousStepApprovalDate(rs.getDate("prev_changed"));
				step.setModuleChangeRequestId(rs.getLong("module_change_requests_id"));
				steps.add(step);
			}
		}catch (Exception e)	{
			SMRCLogger.error("WorkflowDAO.getWorkflowStepsForUser(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return steps;
	}		
	
	/**
	 * 
	 * @param acctId
	 * @param workflowType - Use constants from Workflow
	 * @param user
	 * @param DBConn
	 * @return
	 * @throws Exception
	 */
	public static Workflow getWorkflow(String foreignKeyId, String workflowType, User user, Connection DBConn) throws Exception{
		Workflow workflow = new Workflow();
		if (foreignKeyId.substring(0, 1).equals("P")
                || (workflowType.equals(Workflow.WORKFLOW_TYPE_DISTRIBUTOR_TERMINATION) || workflowType.equals(Workflow.WORKFLOW_PRODUCT_MODULE))) {
			initializeWorkflow(foreignKeyId,workflowType,user,DBConn);
		}
		
		workflow.setWorkflowSteps(getWorkflowStepsForAccount(foreignKeyId, workflowType, DBConn));
		workflow.setWorkflowType(workflowType);

		return workflow;
	}		
	

	public static void changeApprovalStatus(String approvalId, String user, String status, String acctId, Connection DBConn) throws Exception{
		PreparedStatement pstmt = null;
		SMRCLogger.debug("WorkflowDAO.changeApprovalStatus  approvalId:" + approvalId + " status: " + status);
			
		try {
			pstmt = DBConn.prepareStatement(changeApprovalStatusUpdate);
			pstmt.setString(1, status);
			pstmt.setString(2, user);
			pstmt.setInt(3, Globals.a2int(approvalId));
			pstmt.executeUpdate();
		}catch (Exception e)	{
			SMRCLogger.error("WorkflowDAO.changeApprovalStatus(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
	}
	
	// This method returns all WorkflowSteps of customer prospects awaiting approval
	public static ArrayList getCustomerPendingApprovals(Connection DBConn) throws Exception{
		ArrayList steps = new ArrayList();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DBConn.prepareStatement(getCustomerPendingApprovalsQuery);
						
			rs = pstmt.executeQuery();
			while(rs.next()){
			    
				WorkflowStep step = new WorkflowStep();	
				step.setApprovalId(rs.getInt("WORKFLOW_APPROVAL_ID"));
				step.setStepId(rs.getInt("CURRENT_STEP_ID"));
				step.setStepName(rs.getString("WORKFLOW_STEP_NAME"));
				step.setPrevStepId(rs.getInt("PREV_STEP_ID"));
				step.setSequenceId(rs.getInt("WORKFLOW_STEP_SEQUENCE_ID"));
				step.setWorkflowId(rs.getInt("WORKFLOW_ID"));
				step.setWorkflowName(rs.getString("WORKFLOW_NAME"));
				step.setStatusFlag(StringManipulation.noNull(rs.getString("APPROVED_FLAG")));
				if(step.isApproved() || step.isRejected()){
					step.setUserApprovedOrRejected(UserDAO.getUser(step.getUserChanged(),DBConn));
				}				
				step.setDateAdded(rs.getDate("DATE_ADDED"));
				step.setDateChanged(rs.getDate("DATE_CHANGED"));
				step.setUserAdded(rs.getString("USER_ADDED"));
				step.setUserChanged(rs.getString("USER_CHANGED"));
				step.setCustomerName(rs.getString("customer_name"));
				step.setVcn(rs.getString("vcn"));
				step.setCustomerUserAdded(rs.getString("customer_user_added"));
				step.setCustomerGeog(rs.getString("sp_geog"));
		//		step.setPreviousStepApprovalDate(rs.getDate("prev_changed"));
				
				steps.add(step);
			    
			}
		}catch (Exception e)	{
			SMRCLogger.error("WorkflowDAO.getCustomerPendingApprovals(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return steps;
	}	
	
	public static ArrayList getWorkflowPriorApprovers(String vistaCustomerNumber, Connection DBConn) throws Exception{
		
		ArrayList workflowPriorApprovers = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			pstmt = DBConn.prepareStatement(getWorkflowPriorApprovers);
			pstmt.setString(1,vistaCustomerNumber);		
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				
				WorkflowPriorApprover workflowPriorApprover = new WorkflowPriorApprover(); 
				workflowPriorApprover.setWorkflowStepName(rs.getString("WORKFLOW_STEP_NAME"));
				workflowPriorApprover.setPriorApproverFirstName(rs.getString("FIRST_NAME"));
				workflowPriorApprover.setPriorApproverLastName(rs.getString("LAST_NAME"));
				workflowPriorApprover.setUserId(rs.getString("USERID"));
				workflowPriorApprover.setWorkflowApprovalId(rs.getInt("WORKFLOW_APPROVAL_ID"));
				
				workflowPriorApprovers.add(workflowPriorApprover);
			}

		}catch (Exception e)	{
			SMRCLogger.error("WorkflowDAO.getWorkflowPriorApprovers(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return workflowPriorApprovers;
	}
		
	public static ArrayList getTargetMarketPlanWorkflowPriorApprovers (int targetMarketPlanId, Connection DBConn) throws Exception{
		
		ArrayList targetMarketPlanWorkflowPriorApprovers = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			pstmt = DBConn.prepareStatement(getTargetMarketPlanWorkflowPriorApprovers);
			pstmt.setInt(1,targetMarketPlanId);		
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				
				WorkflowPriorApprover workflowPriorApprover = new WorkflowPriorApprover(); 
				workflowPriorApprover.setWorkflowStepName(rs.getString("WORKFLOW_STEP_NAME"));
				workflowPriorApprover.setPriorApproverFirstName(rs.getString("FIRST_NAME"));
				workflowPriorApprover.setPriorApproverLastName(rs.getString("LAST_NAME"));
				workflowPriorApprover.setUserId(rs.getString("USERID"));
				workflowPriorApprover.setWorkflowApprovalId(rs.getInt("WORKFLOW_APPROVAL_ID"));
				
				targetMarketPlanWorkflowPriorApprovers.add(workflowPriorApprover);
			}

		}catch (Exception e)	{
			SMRCLogger.error("WorkflowDAO.getWorkflowPriorApprovers(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return targetMarketPlanWorkflowPriorApprovers;
	}	
	
	public static ArrayList getModuleChangePriorApprovers(long moduleChangeRequestId, Connection DBConn) throws Exception{
		
		ArrayList workflowPriorApprovers = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			pstmt = DBConn.prepareStatement(getModuleChangePriorApprovers);
			pstmt.setLong(1,moduleChangeRequestId);		
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				
				WorkflowPriorApprover workflowPriorApprover = new WorkflowPriorApprover(); 
				workflowPriorApprover.setWorkflowStepName(rs.getString("WORKFLOW_STEP_NAME"));
				workflowPriorApprover.setPriorApproverFirstName(rs.getString("FIRST_NAME"));
				workflowPriorApprover.setPriorApproverLastName(rs.getString("LAST_NAME"));
				workflowPriorApprover.setUserId(rs.getString("USERID"));
				workflowPriorApprover.setWorkflowApprovalId(rs.getInt("WORKFLOW_APPROVAL_ID"));
				
				workflowPriorApprovers.add(workflowPriorApprover);
			}

		}catch (Exception e)	{
			SMRCLogger.error("WorkflowDAO.getModuleChangePriorApprovers(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return workflowPriorApprovers;
	}
	
	public static String getWorkflowApprovalUserChanged(String workflowApprovalId, Connection DBConn) throws Exception{
		
		String userChanged = null; 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			pstmt = DBConn.prepareStatement(getWorkflowApprovalUserChanged);
			pstmt.setString(1,workflowApprovalId);			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				userChanged = rs.getString("USER_CHANGED");
			}

		}catch (Exception e)	{
			SMRCLogger.error("WorkflowDAO.getWorkflowApprovalUserChanged(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return userChanged;
	}	
	
	
	public static void updateWorkflowLaterApproversToNotApproved(String vistaCustomerNumber, String workflowApprovalId, Connection DBConn) throws Exception{
		
		PreparedStatement pstmt = null;
		SMRCLogger.debug("WorkflowDAO.updateWorkflowLaterApproversToNotApproved  vistaCustomerNumber:" + vistaCustomerNumber + " workflowApprovalId: " + workflowApprovalId);
			
		try {
			pstmt = DBConn.prepareStatement(updateWorkflowLaterApproversToNotApproved);
			pstmt.setString(1, vistaCustomerNumber);
			pstmt.setString(2, workflowApprovalId);
			pstmt.executeUpdate();
		}catch (Exception e)	{
			SMRCLogger.error("WorkflowDAO.updateWorkflowLaterApproversToNotApproved(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
	}
	
	public static void updateTargetMarketWorkflowLaterApproversToNotApproved(String tmId, String workflowApprovalId, Connection DBConn) throws Exception{
		
		PreparedStatement pstmt = null;
		SMRCLogger.debug("WorkflowDAO.updateTargetMarketWorkflowLaterApproversToNotApproved  tmId:" + tmId + " workflowApprovalId: " + workflowApprovalId);
			
		try {
			pstmt = DBConn.prepareStatement(updateTargetMarketWorkflowLaterApproversToNotApproved);
			pstmt.setString(1, tmId);
			pstmt.setString(2, workflowApprovalId);
			pstmt.executeUpdate();
		}catch (Exception e)	{
			SMRCLogger.error("WorkflowDAO.updateTargetMarketWorkflowLaterApproversToNotApproved(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
	}
	
	public static void updateModuleChangeWorkflowLaterApproversToNotApproved(long moduleChangeRequestId, long workflowApprovalId, Connection DBConn) throws Exception{
		
		PreparedStatement pstmt = null;
		SMRCLogger.debug("WorkflowDAO.updateModuleChangeWorkflowLaterApproversToNotApproved  moduleChangeRequestId:" + moduleChangeRequestId + " workflowApprovalId: " + workflowApprovalId);
			
		try {
			pstmt = DBConn.prepareStatement(updateModuleChangeWorkflowLaterApproversToNotApproved);
			pstmt.setLong(1, moduleChangeRequestId);
			pstmt.setLong(2, workflowApprovalId);
			pstmt.executeUpdate();
		}catch (Exception e)	{
			SMRCLogger.error("WorkflowDAO.updateModuleChangeWorkflowLaterApproversToNotApproved(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
	}
	
	public static boolean targetMarketIsPendingResubmission (int targetMarketPlanId, Connection DBConn) throws Exception {
		boolean pendingResubmission = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(targetMarketIsPendingResubmission);
			pstmt.setInt(1,targetMarketPlanId);
			rs = pstmt.executeQuery();
			if (rs.next()){
				pendingResubmission = true;
			}
			return pendingResubmission;
		}catch (Exception e)	{
			SMRCLogger.error("WorkflowDAO.targetMarketIsPendingResubmission(): ", e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		
	}
	
	/**
     * 
     * @param previousStatus  - should reflect what current user is doing
     * @param action - approve (Y) or reject (R)
     * @param targetMarketId
     * @param user
     * @param DBConn
     * @throws Exception
     */
    public static void updateTargetMarketPlanWorkflow (String previousStatus, String action, int targetMarketId, User user, Connection DBConn) throws Exception {
    	SMRCLogger.debug("WorkflowDAO.updateTargetMarketPlanWorkflow  previousStatus= " + previousStatus + " action=" + action);
    	Workflow workflow = WorkflowDAO.getWorkflow((""+targetMarketId),Workflow.WORKFLOW_TYPE_TARGET_MARKET_PLANS,user,DBConn);
    	ArrayList steps = workflow.getWorkflowSteps();
    	String findStepName = "";
    	if (previousStatus.trim().equalsIgnoreCase("S") || previousStatus.trim().equalsIgnoreCase("U") || previousStatus.trim().equalsIgnoreCase("")){
    		// saved, unsaved, or new being sent to DM
    		findStepName = WorkflowStep.STEP_NAME_INITIAL_ACCOUNT_INFO;
    	} else if (previousStatus.trim().equalsIgnoreCase("B")){
    		// DM is approving
    		findStepName = WorkflowStep.STEP_NAME_DISTRICT_MANAGER;
    	} else if (previousStatus.trim().equalsIgnoreCase("D")){
    		// Global Channel Manager is approving
    		findStepName = WorkflowStep.STEP_NAME_GLOBAL_CHANNEL_MANAGER;
    	}
    	for (int i=0; i < steps.size(); i++){
    		WorkflowStep step = (WorkflowStep) steps.get(i);
    		SMRCLogger.debug("step: " + step.getStepName() + "  findStepName:" + findStepName);
    		if (step.getStepName().trim().equalsIgnoreCase(findStepName)){
    			WorkflowDAO.changeApprovalStatus((""+step.getApprovalId()),user.getUserid(),action,null,DBConn);
    		}
    	}
    	
    }

	public static void resetWorkflow(String step, String pNum, Connection DBConn) throws Exception {
		PreparedStatement pstmtWorkflow = null;
		PreparedStatement pstmtCustomer = null;
		
		String resetWorkflow =  "update workflow_approvals wa " +
								"set wa.user_changed = null, " +
								"wa.date_changed = null, " +
								"wa.approved_flag = 'N' " +
								"where wa.workflow_approval_id in " +
								"( select t.workflow_approval_id " +
								"from workflow_approvals t, " +
								"workflow_steps ws, " +
								"workflow_step_sequences ss, " +
								"workflows w where t.vista_customer_number = ? " +
								"and ws.workflow_step_name = ? " +
								"and ws.workflow_step_id = t.workflow_step_sequence_id " +
								"and ss.current_step_id = ws.workflow_step_id " +
								"and w.workflow_id = ss.workflow_id " +
								"and w.workflow_name = 'Distributor Application' )";
		
		 String setCustomer = "update customer t set t.customer_status = 'Prospect' where t.vista_customer_number = ?";

		try {
			pstmtWorkflow = DBConn.prepareStatement(resetWorkflow);
			pstmtWorkflow.setString(1,pNum);
			pstmtWorkflow.setString(2,step);
			pstmtWorkflow.executeUpdate();
			
			pstmtCustomer = DBConn.prepareStatement(setCustomer);
			pstmtCustomer.setString(1,pNum);
			pstmtCustomer.executeUpdate();
			
		}catch (Exception e)	{
			SMRCLogger.error("WorkflowDAO.resetWorkflow(): ", e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(pstmtWorkflow);
			SMRCConnectionPoolUtils.close(pstmtCustomer);
		}
			
	}
	
	public static void updateApprovalFlag(String flag, String vcn, Connection DBConn) throws Exception {
		String updateApprovedFlag = "update workflow_approvals wa set wa.approved_flag = ? where wa.vista_customer_number = ? and wa.workflow_step_sequence_id in (6,7,8)";

		PreparedStatement pstmt = null;
		try {
			pstmt = DBConn.prepareStatement(updateApprovedFlag);
			pstmt.setString(1, flag);
			pstmt.setString(2, vcn);
			pstmt.executeUpdate();
		} catch(Exception e) {
			SMRCLogger.error("WorkflowDAO.resetWorkflow(): ", e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
	}
}
