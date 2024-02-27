package com.eaton.electrical.smrc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.eaton.electrical.smrc.bo.CommitmentChangeRequest;
import com.eaton.electrical.smrc.bo.CommitmentHistory;
import com.eaton.electrical.smrc.service.SMRCConnectionPoolUtils;
import com.eaton.electrical.smrc.service.SMRCLogger;

public class CommitmentDAO {
	private static final String insertIntoCommitmentApproval = "INSERT INTO commitment_approvals(CREATE_APPROVAL_ID,VCN,APPROVER_NAME,APPROVAL_FLAG,DATE_ADDED,APPROVAL_ORDER,COM_LEVEL,PROJ_SALES_1,PROJ_SALES_3,PERC_SALES_1,PERC_SALES_3,ELECTRIC_LINES) VALUES(commitments_seq.nextval,?,?,?,SYSDATE,?,?,?,?,?,?,?)";
	private static final String getCommimentChangeRequestByVCN = "SELECT * FROM commitment_approvals WHERE vcn = ? AND approval_order = ?";
	private static final String hasPendingApproval = "SELECT * FROM commitment_approvals WHERE vcn = ?";
	private static final String commitmentRejected = "DELETE commitment_approvals WHERE vcn=?";
	private static final String updateApprovalFlag = "UPDATE commitment_approvals SET approval_flag = ?, approver_name = ?, date_added = SYSDATE WHERE vcn = ? AND approval_order = ?";
	private static final String getPendingApprovalsForChannel = "SELECT * FROM commitment_approvals WHERE approver_name = ?";
	private static final String getPendingApprovalsForDM = "SELECT * FROM commitment_approvals WHERE approver_name = ? AND approval_flag = ?";
	private static final String insertApprovalHistory = "INSERT INTO commitment_approval_history(APPROVAL_HISTORY_ID,VCN,APPROVER_NAME,ACTION,DATE_ADDED) VALUES (commitment_history_seq.nextval,?,?,?,SYSDATE)";
	private static final String getHistoryByVCN = "SELECT * FROM commitment_approval_history WHERE vcn = ?";
	
	public static void insertIntoCommitmentApproval(CommitmentChangeRequest ccr, Connection DBConn) throws Exception {
		PreparedStatement pstmt = null;

		try {
			pstmt = DBConn.prepareStatement(insertIntoCommitmentApproval);
			//SEQUENCE FOR CREATE APPROVAL ID
			pstmt.setString(1, ccr.getVcn());
			pstmt.setString(2, ccr.getApproverName());
			pstmt.setString(3, ccr.getApprovalFlag());
			//SYSDATE FOR DATE ADDED
			pstmt.setInt(4, ccr.getApprovalOrder());
			pstmt.setInt(5, ccr.getComLevel());
			pstmt.setInt(6, ccr.getProjSales1());
			pstmt.setInt(7, ccr.getProjSales3());
			pstmt.setInt(8, ccr.getPercSales1());
			pstmt.setInt(9, ccr.getPercSales3());
			pstmt.setString(10, ccr.getElectricLines());
			pstmt.executeUpdate();
									
		}catch (Exception e)	{
			SMRCLogger.error("CommitmentDAO.insertIntoCommitmentApproval(): ", e);
			throw e;
		}finally {
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(pstmt);
		}
	}
	
	public static boolean hasPendingApproval(String vcn, Connection DBConn) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean hasPendingApprovalB = false;
		try {
			pstmt = DBConn.prepareStatement(hasPendingApproval);
			
			pstmt.setString(1, vcn);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				hasPendingApprovalB = true;
			}
	
		}catch (Exception e)	{
			SMRCLogger.error("CommitmentDAO.hasPendingApproval(): ", e);
			throw e;
		}finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return hasPendingApprovalB;
	}
	 
	public static CommitmentChangeRequest getCommitmentChangeRequestByApprovalOrder(String vcn, int order, Connection DBConn) throws Exception {
		
		CommitmentChangeRequest ccr = new CommitmentChangeRequest();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getCommimentChangeRequestByVCN);
			
			pstmt.setString(1, vcn);
			pstmt.setInt(2, order);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				ccr.setApproverName(rs.getString("APPROVER_NAME"));
				ccr.setApprovalOrder(rs.getInt("APPROVAL_ORDER"));
				ccr.setApprovalFlag(rs.getString("APPROVAL_FLAG"));
				ccr.setDateAdded(rs.getDate("DATE_ADDED"));
				ccr.setComLevel(rs.getInt("COM_LEVEL"));
				ccr.setElectricLines(rs.getString("ELECTRIC_LINES"));
				ccr.setPercSales1(rs.getInt("PERC_SALES_1"));
				ccr.setPercSales3(rs.getInt("PERC_SALES_3"));
				ccr.setProjSales1(rs.getInt("PROJ_SALES_1"));
				ccr.setProjSales3(rs.getInt("PROJ_SALES_3"));
			}
	
		}catch (Exception e)	{
			SMRCLogger.error("CommitmentDAO.getCommitmentChangeRequestByApprovalOrder(): ", e);
			throw e;
		}finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return ccr;
	}
	
	public static void CommitmentRejected(String vcn, Connection DBConn)  throws Exception{
		PreparedStatement pstmt = null;

		try {
			pstmt = DBConn.prepareStatement(commitmentRejected);
			
			pstmt.setString(1, vcn);
			pstmt.executeUpdate();
						
		}catch (Exception e)	{
			SMRCLogger.error("CommitmentDAO.CommitmentRejected(): ", e);
			throw e;
		}finally {
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(pstmt);
		}		
	}
	
	public static void UpdateApprovalFlag(String vcn, int order, String name, Connection DBConn)  throws Exception{
		PreparedStatement pstmt = null;

		try {
			pstmt = DBConn.prepareStatement(updateApprovalFlag);
			pstmt.setString(1, "Y");
			pstmt.setString(2, name);
			pstmt.setString(3, vcn);
			pstmt.setInt(4, order);
			pstmt.executeUpdate();
							
		}catch (Exception e)	{
			SMRCLogger.error("CommitmentDAO.UpdateApprovalFlag(): ", e);
			throw e;
		}finally {
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(pstmt);
		}		
	}
	
	public static ArrayList getPendingApprovalsForDM(String name, Connection DBConn) throws Exception {
		ArrayList ccrList = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getPendingApprovalsForDM);
			
			pstmt.setString(1, name);
			pstmt.setString(2, "N");
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				CommitmentChangeRequest ccr = new CommitmentChangeRequest();
				ccr.setApproverName(rs.getString("APPROVER_NAME"));
				ccr.setApprovalOrder(rs.getInt("APPROVAL_ORDER"));
				ccr.setApprovalFlag(rs.getString("APPROVAL_FLAG"));
				ccr.setDateAdded(rs.getDate("DATE_ADDED"));
				ccr.setComLevel(rs.getInt("COM_LEVEL"));
				ccr.setElectricLines(rs.getString("ELECTRIC_LINES"));
				ccr.setPercSales1(rs.getInt("PERC_SALES_1"));
				ccr.setPercSales3(rs.getInt("PERC_SALES_3"));
				ccr.setProjSales1(rs.getInt("PROJ_SALES_1"));
				ccr.setProjSales3(rs.getInt("PROJ_SALES_3"));
				ccr.setVcn(rs.getString("VCN"));
				ccr.setName(AccountDAO.getAccountName(ccr.getVcn(), DBConn));
				
				ccrList.add(ccr);
			}
	
		}catch (Exception e)	{
			SMRCLogger.error("CommitmentDAO.getPendingApprovalsForDM(): ", e);
			throw e;
		}finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(pstmt);
		}	

		return ccrList;
	}
	
	public static ArrayList getPendingApprovalsForChannel(Connection DBConn) throws Exception {
		ArrayList ccrList = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getPendingApprovalsForChannel);
			
			pstmt.setString(1, "Channel");
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				CommitmentChangeRequest ccr = new CommitmentChangeRequest();
				ccr.setApproverName(rs.getString("APPROVER_NAME"));
				ccr.setApprovalOrder(rs.getInt("APPROVAL_ORDER"));
				ccr.setApprovalFlag(rs.getString("APPROVAL_FLAG"));
				ccr.setDateAdded(rs.getDate("DATE_ADDED"));
				ccr.setComLevel(rs.getInt("COM_LEVEL"));
				ccr.setElectricLines(rs.getString("ELECTRIC_LINES"));
				ccr.setPercSales1(rs.getInt("PERC_SALES_1"));
				ccr.setPercSales3(rs.getInt("PERC_SALES_3"));
				ccr.setProjSales1(rs.getInt("PROJ_SALES_1"));
				ccr.setProjSales3(rs.getInt("PROJ_SALES_3"));
				ccr.setVcn(rs.getString("VCN"));
				ccr.setName(AccountDAO.getAccountName(ccr.getVcn(), DBConn));
				
				ccrList.add(ccr);
			}
	
		}catch (Exception e)	{
			SMRCLogger.error("CommitmentDAO.getPendingApprovalsForChannel(): ", e);
			throw e;
		}finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return ccrList;
	}
	
	public static void insertApprovalHistory(String vcn, String name, String action, Connection DBConn) throws Exception {
		PreparedStatement pstmt = null;

		try {
			pstmt = DBConn.prepareStatement(insertApprovalHistory);
			//SEQUENCE FOR CREATE APPROVAL ID
			pstmt.setString(1, vcn);
			pstmt.setString(2, name);
			pstmt.setString(3, action);
			//SYSDATE FOR DATE ADDED
			pstmt.executeUpdate();
							
		}catch (Exception e)	{
			SMRCLogger.error("CommitmentDAO.insertApprovalHistory(): ", e);
			throw e;
		}finally {
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(pstmt);
		}
	}
	
	public static ArrayList getCommitmentHistoryByVCN(String vcn, Connection DBConn) throws Exception {
		
		ArrayList al = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getHistoryByVCN);
			
			pstmt.setString(1, vcn);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				CommitmentHistory ch = new CommitmentHistory();
				ch.setApprover_name(rs.getString("APPROVER_NAME"));
				ch.setVcn(rs.getString("VCN"));
				ch.setAction(rs.getString("ACTION"));
				ch.setDate(rs.getDate("DATE_ADDED"));
				
				al.add(ch);
				
			}
	
		}catch (Exception e)	{
			SMRCLogger.error("CommitmentDAO.getCommitmentHistoryByVCN(): ", e);
			throw e;
		}finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return al;
	}
	
}