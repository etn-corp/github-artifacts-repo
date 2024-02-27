package com.eaton.electrical.smrc.dao;

import java.sql.*;
import com.eaton.electrical.smrc.service.*;


public class ReviseResubExplanationDAO {

	private static final String insertReviseResubExplanationWithoutTargetProject = "insert into revise_resub_explanation (revise_resubmit_explanation_id, workflow_approval_id, explanation, user_added, user_changed, data_added, date_changed) values (revise_resub_explanation_seq.nextval, ?, ?, ?, ?, sysdate, sysdate)";
	private static final String getLastExplanationForApprovalId = "select t.explanation from revise_resub_explanation t " +
			" where t.workflow_approval_id = ? order by t.data_added desc";
	
	public static void insertReviseResubExplanationWithoutTargetProject(int workflowApprovalId, String explantion, String userId, Connection DBConn) throws Exception{
		
		PreparedStatement pstmt = null;
		
		try {
			int pIndex=0;
			pstmt = DBConn.prepareStatement(insertReviseResubExplanationWithoutTargetProject);
			
			pstmt.setInt( ++pIndex,workflowApprovalId);
			pstmt.setString( ++pIndex,explantion);
			pstmt.setString( ++pIndex,userId);
			pstmt.setString( ++pIndex,userId);
			
			pstmt.executeUpdate();
			
		}
		catch (Exception e)	{
			SMRCLogger.error("ReviseResubExplanationDAO.insertReviseResubExplanationWithoutTargetProject: " , e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
	}
	
	public static String getLastExplanationForReviseByApprovalId (long approvalId, Connection DBConn) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String explanation = null;
		try {
			pstmt = DBConn.prepareStatement(getLastExplanationForApprovalId);
			pstmt.setLong(1,approvalId);
			rs = pstmt.executeQuery();
			// We only want the last explanation
			if (rs.next()){
				explanation = rs.getString(1);
			}
			
		}catch (Exception e)	{
				SMRCLogger.error("ReviseResubExplanationDAO.getLastExplanationForReviseByApprovalId:  approvalId = " + approvalId + "  " + e , e);
				throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
			SMRCConnectionPoolUtils.close(rs);
		}
		
		return explanation;
	}
	
	
	
}
