package com.eaton.electrical.smrc.dao;

import java.sql.*;
import java.util.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

public class ModifySegmentsDAO {
	private static final String checkForPendingModification = "SELECT ";
		
	public static void insertSegments(String vcn, int segmentID, String isnewSegment, String approved, Connection DBConn) throws Exception{
		PreparedStatement pstmt = null;
		
		String query = "INSERT INTO CATEGORY_MODIFY (CATEGORY_MODIFY_ID,VISTA_CUSTOMER_NUMBER,SEGMENT_ID,ISNEW_SEGMENT,APPROVED,CATEGORY_MODIFY_APPROVALS_ID) VALUES(category_modify_SEQ.nextval,?,?,?,?,category_modify_approvals_SEQ.CURRVAL)";

		try {
			pstmt = DBConn.prepareStatement(query);
			
			pstmt.setString(1,vcn);
			pstmt.setInt(2,segmentID);
			pstmt.setString(3, isnewSegment);
			pstmt.setString(4, approved);
			pstmt.executeUpdate();
	
		} catch (Exception e)	{
			SMRCLogger.error("ModifySegmentsDAO.insertSegments(): ", e);
			System.out.println(e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(pstmt);
		}
	}
	
	public static void insertApprovalRecord(String vcn, String requester, String requesterEmail, String dm, String dmEmail, String zm, String zmEmail, Connection DBConn) throws Exception {
		PreparedStatement ps = null;
		
		String query = "INSERT INTO CATEGORY_MODIFY_APPROVALS (CATEGORY_MODIFY_APPROVALS_ID,VISTA_CUSTOMER_NUMBER,REQUESTER,REQUESTER_EMAIL,DM,DM_EMAIL,DM_APPROVAL,DM_DATE,ZM,ZM_EMAIL,ZM_APPROVAL,ZM_DATE,CHANNEL,CHANNEL_APPROVAL,CHANNEL_DATE) VALUES (category_modify_approvals_SEQ.nextval,?,?,?,?,?,?,SYSDATE,?,?,?,SYSDATE,?,?,SYSDATE)";
		
		try { 
			ps = DBConn.prepareStatement(query);
			//(category_modify_approvals_SEQ.nextval,?,?,?,?,?,?,SYSDATE,?,?,?,SYSDATE,?,?,SYSDATE)";
			ps.setString(1,vcn);
			ps.setString(2,requester);
			ps.setString(3,requesterEmail);
			ps.setString(4,dm);
			ps.setString(5,dmEmail);
			ps.setString(6,"N");
			ps.setString(7,zm);
			ps.setString(8, zmEmail);
			ps.setString(9,"N");
			ps.setString(10,"channel");
			ps.setString(11,"N");
			
			ps.executeUpdate();
		} catch (Exception e)	{
			SMRCLogger.error("ModifySegmentsDAO.insertApprovalRecords(): ", e);
			System.out.println(e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(ps);
		}
	
	}
	
	public static boolean isChannel(String vcn, Connection DBConn) throws Exception {
		boolean pendingChannel = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = "SELECT segment_id FROM CATEGORY_MODIFY WHERE vista_customer_number = ?";
		
		int eachSegmentID;
		try {
			pstmt = DBConn.prepareStatement(query);
			pstmt.setString(1,vcn);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				eachSegmentID = rs.getInt("SEGMENT_ID");
				if(eachSegmentID == 3) {
					pendingChannel = true;
					break;
				}
			}
		} catch (Exception e)	{
			SMRCLogger.error("ModifySegmentsDAO.getSegmentIdByVCN(): ", e);
			System.out.println(e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return pendingChannel;
	}
	
	public static boolean isPendingApproval(String vcn, Connection DBConn) throws Exception {
		boolean hasPending = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = "SELECT vista_customer_number FROM CATEGORY_MODIFY WHERE vista_customer_number = ? AND APPROVED = ?";
		
		try {
			pstmt = DBConn.prepareStatement(query);
			pstmt.setString(1,vcn);
			pstmt.setString(2,"N");
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				hasPending = true;
				break;
			}
		} catch (Exception e)	{
			SMRCLogger.error("ModifySegmentsDAO.getSegmentIdByVCN(): ", e);
			System.out.println(e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return hasPending;
	}
	
	public static ModifySegmentApprovalsBO getApprovalRecordForVCN(String vcn, Connection DBConn) throws Exception {
		ModifySegmentApprovalsBO msab = new ModifySegmentApprovalsBO();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT * FROM category_modify_approvals WHERE vista_customer_number = ? AND channel_approval = ?";
		
		try {
			ps = DBConn.prepareStatement(query);
			ps.setString(1,vcn);
			ps.setString(2, "N");
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				msab.setId(rs.getInt("CATEGORY_MODIFY_APPROVALS_ID"));
				msab.setChannel(rs.getString("CHANNEL"));
				msab.setChannelApproval(rs.getString("CHANNEL_APPROVAL"));
				msab.setChannelDate(rs.getDate("CHANNEL_DATE"));
				msab.setDm(rs.getString("DM"));
				msab.setDmApproval(rs.getString("DM_APPROVAL"));
				msab.setDmEMail(rs.getString("DM_EMAIL"));
				msab.setDmDate(rs.getDate("DM_DATE"));
				msab.setRequester(rs.getString("REQUESTER"));
				msab.setRequesterEmail(rs.getString("REQUESTER_EMAIL"));
				msab.setVcn(rs.getString("VISTA_CUSTOMER_NUMBER"));
				msab.setZm(rs.getString("ZM"));
				msab.setZmApproval(rs.getString("ZM_APPROVAL"));
				msab.setZmEmail(rs.getString("ZM_EMAIL"));
				msab.setZmDate(rs.getDate("ZM_DATE"));
			}
		} catch (Exception e)	{
			SMRCLogger.error("ModifySegmentsDAO.getApprovalRecordForVCN(): ", e);
			System.out.println(e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(ps);
		}
		
		return msab;
	}
	
	public static ArrayList getModifiedSegmentsForVCN(String vcn, Connection DBConn) throws Exception {
		ArrayList al = new ArrayList();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT segment_id FROM category_modify WHERE vista_customer_number = ? AND approved = ? ORDER BY segment_id ASC";
		
		try {
			ps = DBConn.prepareStatement(query);
			ps.setString(1,vcn);
			ps.setString(2, "N");
			rs = ps.executeQuery();
			
			while(rs.next()) {			
				al.add(rs.getObject("SEGMENT_ID"));
			}
		} catch (Exception e)	{
			SMRCLogger.error("ModifySegmentsDAO.getModifiedSegmentsForVCN(): ", e);
			System.out.println(e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(ps);
		}
		return al;
	}
	
	public static void updateDmApproval(String vcn, String approvalFlag, String approver, int id, Connection DBConn) throws Exception {
		try {
			updateApprovals(vcn, approvalFlag, "DM", approver, id, DBConn);
		} catch(Exception e) {
			SMRCLogger.error("ModifySegmentsDAO.updateDmApproval(): ", e);
			System.out.println(e);
			throw e;
		}
	}
	
	public static void updateZmApproval(String vcn, String approvalFlag, String approver, int id, Connection DBConn) throws Exception {
		try {
			updateApprovals(vcn, approvalFlag, "ZM", approver, id, DBConn);
		} catch(Exception e) {
			SMRCLogger.error("ModifySegmentsDAO.updateZmApproval(): ", e);
			System.out.println(e);
			throw e;
		}		
	}
	
	public static void updateChannelApproval(String vcn, String approvalFlag, String approver, int id, Connection DBConn) throws Exception {
		try {
			updateApprovals(vcn, approvalFlag, "CHANNEL", approver, id, DBConn);
		} catch(Exception e) {
			SMRCLogger.error("ModifySegmentsDAO.updateChannelApproval(): ", e);
			System.out.println(e);
			throw e;
		}		
	}
	
	private static void updateApprovals(String vcn, String approvalFlag, String column, String approver, int id, Connection DBConn) throws Exception {
		String query = "UPDATE category_modify_approvals SET "+column+"_approval='"+approvalFlag+"',"+column+"='"+approver+"',"+column+"_date=SYSDATE WHERE vista_customer_number ='"+vcn+ "' AND category_modify_approvals_id ="+id;
		Statement s = DBConn.createStatement();
		
		try {
			s.executeUpdate(query);
		} catch (Exception e)	{
			SMRCLogger.error("ModifySegmentsDAO.updateApprovals(): ", e);
			System.out.println(e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(s);
		}
	}
	
	public static void approveCategoryModify(String vcn, int id, Connection DBConn) throws Exception {
		String query = "UPDATE category_modify SET approved = ? WHERE vista_customer_number = ? AND category_modify_approvals_id = ?";
		PreparedStatement ps = null;
		
		try {
			ps = DBConn.prepareStatement(query);
			ps.setString(1, "Y");
			ps.setString(2, vcn);
			ps.setInt(3, id);
		
			ps.executeUpdate();
		} catch (Exception e)	{
			SMRCLogger.error("ModifySegmentsDAO.approveCategoryModify(): ", e);
			System.out.println(e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(ps);
		}
	}

	public static void rejectCategoryModify(String vcn, int id, Connection DBConn) throws Exception {
		String query = "UPDATE category_modify SET approved = ? WHERE vista_customer_number = ? AND category_modify_approvals_id = ?";
		PreparedStatement ps = null;
		
		try {
			ps = DBConn.prepareStatement(query);
			ps.setString(1, "R");
			ps.setString(2, vcn);
			ps.setInt(3, id);
			
			ps.executeUpdate();
		} catch (Exception e)	{
			SMRCLogger.error("ModifySegmentsDAO.rejectCategoryModify(): ", e);
			System.out.println(e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(ps);
		}
	}
	
	public static boolean hasApprovalHistory(String vcn, Connection DBConn) throws Exception {
		boolean hasPending = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = "SELECT vista_customer_number FROM CATEGORY_MODIFY WHERE vista_customer_number = ? AND APPROVED != ?";
		
		try {
			pstmt = DBConn.prepareStatement(query);
			pstmt.setString(1,vcn);
			pstmt.setString(2,"N");
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				hasPending = true;
				break;
			}
		} catch (Exception e)	{
			SMRCLogger.error("ModifySegmentsDAO.getSegmentIdByVCN(): ", e);
			System.out.println(e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return hasPending;
	}
	
	public static ArrayList getApprovalHistory(String vcn, Connection DBConn) throws Exception {
		ArrayList al = new ArrayList();
		ArrayList segments = new ArrayList();
		int id;
		PreparedStatement ps = null;
		
		ResultSet rs = null;
		
		String query = "SELECT * FROM CATEGORY_MODIFY_APPROVALS WHERE channel_approval != ? AND vista_customer_number IN (SELECT vista_customer_number FROM category_modify WHERE vista_customer_number = ? AND APPROVED != ?)";
		
		try {
			ps = DBConn.prepareStatement(query);
			ps.setString(1,"N");
			ps.setString(2,vcn);
			ps.setString(3,"N");
			rs = ps.executeQuery();
			
			while(rs.next()) {
				ModifySegmentsHistoryBO hsh = new ModifySegmentsHistoryBO();
				
				id = rs.getInt("CATEGORY_MODIFY_APPROVALS_ID");
				
				hsh.setChannel(rs.getString("CHANNEL"));
				hsh.setChannelDate(rs.getDate("CHANNEL_DATE"));
				hsh.setChannelApproval(rs.getString("CHANNEL_APPROVAL"));
				hsh.setDm(rs.getString("DM"));
				hsh.setDmDate(rs.getDate("DM_DATE"));
				hsh.setDmApproval(rs.getString("DM_APPROVAL"));
				hsh.setId(id);
				hsh.setRequester(rs.getString("REQUESTER"));
				hsh.setVcn(rs.getString("VISTA_CUSTOMER_NUMBER"));
				hsh.setZm(rs.getString("ZM"));
				hsh.setZmDate(rs.getDate("ZM_DATE"));
				hsh.setZmApproval(rs.getString("ZM_APPROVAL"));
				
				try {
					segments = getSegmentsForApprovalHistory(id, DBConn);
				} catch(Exception e) {
					SMRCLogger.error("ModifySegmentsDAO.getSegmentsForApprovalHistory(): ", e);
					System.out.println(e);
					throw e;
				}
				hsh.setSegments(segments);
				al.add(hsh);
			}
			
		} catch (Exception e)	{
			SMRCLogger.error("ModifySegmentsDAO.getSegmentIdByVCN(): ", e);
			System.out.println(e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(ps);
		}
		return al;
	}
	
	private static ArrayList getSegmentsForApprovalHistory(int id, Connection DBConn) throws Exception {
		ArrayList al = new ArrayList();
		int segmentId;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT segment_id FROM CATEGORY_MODIFY WHERE category_modify_approvals_id = ? ORDER BY segment_id ASC";

		try {
			ps = DBConn.prepareStatement(query);
			ps.setInt(1, id);
			rs = ps.executeQuery();

			while(rs.next()) {
				segmentId = rs.getInt("SEGMENT_ID");
				al.add(SegmentsDAO.getSegmentName(segmentId, DBConn));
			}
		} catch(Exception e) {
			SMRCLogger.error("ModifySegmentsDAO.getSegmentsForApprovalHistory(): ", e);
			System.out.println(e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(ps);
		}
		return al;
	}
	
	public static void updateChannelApprovalFromDmRejection(String vcn, int id, Connection DBConn) throws Exception {
		String query = "UPDATE category_modify_approvals SET channel_approval = ? WHERE vista_customer_number = ? AND category_modify_approvals_id = ?";
		PreparedStatement ps = null;
		
		try {
			ps = DBConn.prepareStatement(query);
			ps.setString(1, "D");
			ps.setString(2, vcn);
			ps.setInt(3, id);
			
			ps.executeUpdate();
		} catch (Exception e)	{
			SMRCLogger.error("ModifySegmentsDAO.updateChannelApprovalFromDmRejection(): ", e);
			System.out.println(e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(ps);
		}
	}
	
   	public static Hashtable getPendingApprovalsForChannel(Connection DBConn) throws Exception {
   		Hashtable tm = new Hashtable();
   		String query = "SELECT c.vista_customer_number FROM category_modify_approvals c WHERE c.dm_approval = ? AND c.channel_approval = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DBConn.prepareStatement(query);
			ps.setString(1, "Y");
			ps.setString(2, "N");
			rs = ps.executeQuery();

			while(rs.next()) {
				tm.put(rs.getString("VISTA_CUSTOMER_NUMBER"),AccountDAO.getAccountName(rs.getString("VISTA_CUSTOMER_NUMBER"), DBConn));
			}
		} catch(Exception e) {
			SMRCLogger.error("ModifySegmentsDAO.getPendingApprovalsForChannel(): ", e);
			System.out.println(e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(ps);
		}
		
   		return tm;
   	}

   	public static Hashtable getPendingApprovalsForDM(String dmName, Connection DBConn) throws Exception {
   		Hashtable tm = new Hashtable();
   		
   		String query = "SELECT c.vista_customer_number FROM category_modify_approvals c WHERE c.dm_approval = ? AND c.dm = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DBConn.prepareStatement(query);
			ps.setString(1, "N");
			ps.setString(2, dmName);
			rs = ps.executeQuery();

			while(rs.next()) {
				tm.put(rs.getString("VISTA_CUSTOMER_NUMBER"),AccountDAO.getAccountName(rs.getString("VISTA_CUSTOMER_NUMBER"), DBConn));
			}
		} catch(Exception e) {
			SMRCLogger.error("ModifySegmentsDAO.getPendingApprovalsForChannel(): ", e);
			System.out.println(e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(ps);
		}
   		return tm;
   	}
}
