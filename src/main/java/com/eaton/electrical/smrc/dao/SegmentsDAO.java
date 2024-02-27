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
public class SegmentsDAO {
	private static final String getSegmentsQuery = "SELECT * FROM SEGMENTS WHERE SEGMENT_PARENT=? AND HIDDEN_FLAG!='Y'";
	private static final String getSegmentsForAccountQuery = "select b.SEGMENT_NAME,"+
       " b.SEGMENT_LEVEL,"+
       " b.SEGMENT_PARENT,"+
       " b.DISTRIBUTOR_SEGMENT_FLAG,"+
       " b.TARGET_MARKET_SEGMENT_FLAG,"+
       " b.DISTRIBUTOR_DISPLAY_FLAG," +
       " b.DEFAULT_SIC_CODE,"+
       " b.HIDDEN_FLAG,"+	
       " a.SEGMENT_ID"+
	   " FROM CUSTOMER_SEGMENTS a,"+
       " SEGMENTS b"+
	   " WHERE VISTA_CUSTOMER_NUMBER=?"+
       " AND b.SEGMENT_ID = a.SEGMENT_ID" +
       " AND b.HIDDEN_FLAG!='Y'";
	
	private static final String getSegmentQuery = "SELECT * FROM SEGMENTS WHERE SEGMENT_ID=?";
	private static final String getDistCustSegmentsQuery = "SELECT * FROM SEGMENTS WHERE DISTRIBUTOR_DISPLAY_FLAG='Y' AND HIDDEN_FLAG='Y'";
	private static final String deleteSegments = "DELETE FROM CUSTOMER_SEGMENTS WHERE VISTA_CUSTOMER_NUMBER=?";
	private static final String insertSegment = "INSERT INTO CUSTOMER_SEGMENTS (SEGMENT_ID,VISTA_CUSTOMER_NUMBER) VALUES(?,?)";
//	private static final String getAccountsQuery="select * from customer, customer_segments"+
//		" where customer.vista_customer_number = customer_segments.vista_customer_number"+
//		" AND sales_engineer1=? AND SEGMENT_ID=? order by customer_name";
//	private static final String getAccountsQueryTargetOnly="select * from customer, customer_segments"+
//		" where customer.vista_customer_number = customer_segments.vista_customer_number"+
//		" AND sales_engineer1=? AND SEGMENT_ID=? AND TARGET_ACCOUNT_FLAG='Y' ORDER BY CUSTOMER_NAME";

    private static final String getTMSegments = "SELECT * FROM SEGMENTS WHERE TARGET_MARKET_SEGMENT_FLAG = 'Y' AND HIDDEN_FLAG!='Y'";
    private static final String getSegmentNameQuery = "select segment_name from segments where segment_id = ?";

	public static Segment getSegment(int segmentId, Connection DBConn) throws Exception{
		Segment segment = new Segment();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getSegmentQuery);
			
			pstmt.setInt(1, segmentId);
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				segment.setSegmentId(rs.getInt("SEGMENT_ID"));
				segment.setParentId(rs.getInt("SEGMENT_PARENT"));
				segment.setName(StringManipulation.noNull(rs.getString("SEGMENT_NAME")));
				segment.setLevel(rs.getInt("SEGMENT_LEVEL"));
				if(StringManipulation.noNull(rs.getString("DISTRIBUTOR_SEGMENT_FLAG")).equals("Y")){
					segment.setDistributor(true);
				}else{
					segment.setDistributor(false);
				}
				if(StringManipulation.noNull(rs.getString("TARGET_MARKET_SEGMENT_FLAG")).equals("Y")){
					segment.setTargetMarketSegment(true);
				}else{
					segment.setTargetMarketSegment(false);
				}
				if(StringManipulation.noNull(rs.getString("DISTRIBUTOR_DISPLAY_FLAG")).equals("Y")){
					segment.setDistributorFormDisplay(true);
				}else{
					segment.setDistributorFormDisplay(false);
				}
				if(StringManipulation.noNull(rs.getString("HIDDEN_FLAG")).equals("Y")){
					segment.setHidden(true);
				}else{
					segment.setHidden(false);
				}
				segment.setSicCode(StringManipulation.noNull(rs.getString("DEFAULT_SIC_CODE")));
			
			}
		}catch (Exception e)	{
			SMRCLogger.error("SegmentsDAO.getSegment(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
	
		return segment;
	}

	/**
	 * Returns an ArrayList of Segment objects.  If no level is specified 
	 * all primary segments are returned
	 * 
	 * @param DBConn TODO
	 * @param level  the level of segments to get.  If specified, gets all segments at this level
	 * @return       the segments
	 */
	public static ArrayList getSegments(int parent, int depth, Connection DBConn) throws Exception {
		ArrayList segments = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getSegmentsQuery);
			
			pstmt.setInt(1, parent);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				
				Segment segment = new Segment();
				segment.setSegmentId(rs.getInt("SEGMENT_ID"));
				segment.setName(StringManipulation.noNull(rs.getString("SEGMENT_NAME")));
				segment.setLevel(rs.getInt("SEGMENT_LEVEL"));
				if(StringManipulation.noNull(rs.getString("DISTRIBUTOR_SEGMENT_FLAG")).equals("Y")){
					segment.setDistributor(true);
				}else{
					segment.setDistributor(false);
				}
				if(StringManipulation.noNull(rs.getString("TARGET_MARKET_SEGMENT_FLAG")).equals("Y")){
					segment.setTargetMarketSegment(true);
				}else{
					segment.setTargetMarketSegment(false);
				}
				if(StringManipulation.noNull(rs.getString("DISTRIBUTOR_DISPLAY_FLAG")).equals("Y")){
					segment.setDistributorFormDisplay(true);
				}else{
					segment.setDistributorFormDisplay(false);
				}
				if(StringManipulation.noNull(rs.getString("HIDDEN_FLAG")).equals("Y")){
					segment.setHidden(true);
				}else{
					segment.setHidden(false);
				}
				segment.setSicCode(StringManipulation.noNull(rs.getString("DEFAULT_SIC_CODE")));
				
				if(depth>0){
					segment.setSubSegments(getSegments(rs.getInt("SEGMENT_ID"),depth-1, DBConn));
				}
				segments.add(segment);
			}
		}catch (Exception e)	{
			SMRCLogger.error("SegmentsDAO.getSegments(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
	
		return segments;
	}
	
	public static ArrayList getSegmentsForAccount(String vcn, Connection DBConn) throws Exception {
		ArrayList segments = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		SMRCLogger.debug("SQL - SegmentsDAO.getSegmentsForAccount:\n" + getSegmentsForAccountQuery);
		
		try {
			pstmt = DBConn.prepareStatement(getSegmentsForAccountQuery);
			
			pstmt.setString(1, vcn);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Segment segment = new Segment();
				segment.setSegmentId(rs.getInt("SEGMENT_ID"));
				segment.setName(rs.getString("SEGMENT_NAME"));
				segment.setLevel(rs.getInt("SEGMENT_LEVEL"));
				
				if(StringManipulation.noNull(rs.getString("DISTRIBUTOR_SEGMENT_FLAG")).equals("Y")){
					segment.setDistributor(true);
				}else{
					segment.setDistributor(false);
				}
				if(StringManipulation.noNull(rs.getString("TARGET_MARKET_SEGMENT_FLAG")).equals("Y")){
					segment.setTargetMarketSegment(true);
				}else{
					segment.setTargetMarketSegment(false);
				}
				if(StringManipulation.noNull(rs.getString("DISTRIBUTOR_DISPLAY_FLAG")).equals("Y")){
					segment.setDistributorFormDisplay(true);
				}else{
					segment.setDistributorFormDisplay(false);
				}
				if(StringManipulation.noNull(rs.getString("HIDDEN_FLAG")).equals("Y")){
					segment.setHidden(true);
				}else{
					segment.setHidden(false);
				}
				segment.setSicCode(StringManipulation.noNull(rs.getString("DEFAULT_SIC_CODE")));
				
				segments.add(segment);
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("SegmentsDAO.getSegmentsForAccount(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
	
		return segments;
	}	
	
	public static void saveSegments(ArrayList segments, String vcn, Connection DBConn) throws Exception{
		PreparedStatement pstmt = null;

		try {
			pstmt = DBConn.prepareStatement(deleteSegments);
			pstmt.setString(1,vcn);
			pstmt.executeUpdate();
			
			pstmt = DBConn.prepareStatement(insertSegment);
			
			for(int i=0;i<segments.size();i++){
				Segment segment = (Segment)segments.get(i);
				pstmt.setInt(1,segment.getSegmentId());
				pstmt.setString(2,vcn);
				pstmt.executeUpdate();
			}
	
		}catch (Exception e)	{
			SMRCLogger.error("SegmentsDAO.saveSegments(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
	}	
	

	public static ArrayList getSegments(Connection DBConn) throws Exception{
		return getSegments(0,1, DBConn);
	}
	


	public static ArrayList getAccounts(int segmentId, User user, boolean targetOnly, String showSalesOrders, int srYear, int srMonth, Connection DBConn) throws Exception{
        ArrayList accounts = new ArrayList();
        ResultSet rs = null;
        Statement stmt = null;
        //PreparedStatement pstmt = null;
        String query = null;
        StringBuffer getAccountsBuffer = new StringBuffer();
        
        getAccountsBuffer.append("select * from customer, customer_segments where customer.vista_customer_number = customer_segments.vista_customer_number AND SEGMENT_ID=" + segmentId);
        getAccountsBuffer.append(" AND (sales_engineer1 in (" + user.salesIdsToQuotedString() + ")");
        getAccountsBuffer.append(" OR sales_engineer2 in (" + user.salesIdsToQuotedString() + ")");
        getAccountsBuffer.append(" OR sales_engineer3 in (" + user.salesIdsToQuotedString() + ")");
        getAccountsBuffer.append(" OR sales_engineer4 in (" + user.salesIdsToQuotedString() + "))");               
        if(targetOnly)  getAccountsBuffer.append(" AND TARGET_ACCOUNT_FLAG='Y'");
        getAccountsBuffer.append(" order by customer_name");
        
        query=getAccountsBuffer.toString();
        SMRCLogger.debug("SegmentsDAO.getAccounts() SQL\n" + query);
        /*
        StringBuffer getAccountsBuffer = new StringBuffer();
        getAccountsBuffer.append("select * from customer, customer_segments where customer.vista_customer_number = customer_segments.vista_customer_number AND SEGMENT_ID=?");
        getAccountsBuffer.append(" AND sales_engineer1=?");
        getAccountsBuffer.append(" order by customer_name");
        
        String getAccountsQuery="select * from customer, customer_segments"+
		" where customer.vista_customer_number = customer_segments.vista_customer_number"+
		" AND sales_engineer1=? AND SEGMENT_ID=? order by customer_name";
    	String getAccountsQueryTargetOnly="select * from customer, customer_segments"+
		" where customer.vista_customer_number = customer_segments.vista_customer_number"+
		" AND sales_engineer1=? AND SEGMENT_ID=? AND TARGET_ACCOUNT_FLAG='Y' ORDER BY CUSTOMER_NAME";
*/
        
		try {
		    
		    stmt = DBConn.createStatement();
			rs = stmt.executeQuery(query);;
		    
		    /*
	        if(targetOnly){
	        	pstmt = DBConn.prepareStatement(getAccountsQueryTargetOnly);
	        	SMRCLogger.debug(getAccountsQueryTargetOnly + "\n");
	        }else{
	        	pstmt = DBConn.prepareStatement(getAccountsQuery);
	        	SMRCLogger.debug(getAccountsQuery + "\n");
	        }
			pstmt.setString(1,SEId);
			pstmt.setInt(2,segmentId);
			rs = pstmt.executeQuery();
	        */
			while(rs.next()){
                Account account = new Account();
                account.setVcn(rs.getString("vista_customer_number"));
                account.setCustomerName(rs.getString("customer_name"));
                account.setNumbers(AccountDAO.getSEHomeNumbers(rs.getString("vista_customer_number"),showSalesOrders, user, srYear, srMonth, DBConn));
                accounts.add(account);
	        }
		                        
		}catch (Exception e)	{
			SMRCLogger.error("SegmentsDAO.getAccounts(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(stmt);
		}
		        
        return accounts;
		
	}


	
	public static ArrayList getDistCustSegments(Connection DBConn) throws Exception{
		ArrayList segments = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getDistCustSegmentsQuery);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Segment segment = new Segment();
				segment.setSegmentId(rs.getInt("SEGMENT_ID"));
				segment.setName(rs.getString("SEGMENT_NAME"));
				segment.setLevel(rs.getInt("SEGMENT_LEVEL"));
				if(StringManipulation.noNull(rs.getString("DISTRIBUTOR_SEGMENT_FLAG")).equals("Y")){
					segment.setDistributor(true);
				}else{
					segment.setDistributor(false);
				}
				if(StringManipulation.noNull(rs.getString("TARGET_MARKET_SEGMENT_FLAG")).equals("Y")){
					segment.setTargetMarketSegment(true);
				}else{
					segment.setTargetMarketSegment(false);
				}
				if(StringManipulation.noNull(rs.getString("DISTRIBUTOR_DISPLAY_FLAG")).equals("Y")){
					segment.setDistributorFormDisplay(true);
				}else{
					segment.setDistributorFormDisplay(false);
				}
				if(StringManipulation.noNull(rs.getString("HIDDEN_FLAG")).equals("Y")){
					segment.setHidden(true);
				}else{
					segment.setHidden(false);
				}
				segment.setSicCode(StringManipulation.noNull(rs.getString("DEFAULT_SIC_CODE")));
				segments.add(segment);
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("SegmentsDAO.getDistCustSegments(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
	
		return segments;
		
	}	
	

	
    public static ArrayList getTargetMarketSegments(Connection DBConn) throws Exception {
		ArrayList results = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getTMSegments);
			
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Segment segment = new Segment();
				segment.setSegmentId(rs.getInt("SEGMENT_ID"));
				segment.setName(rs.getString("SEGMENT_NAME"));
				segment.setLevel(rs.getInt("SEGMENT_LEVEL"));
				if(StringManipulation.noNull(rs.getString("DISTRIBUTOR_SEGMENT_FLAG")).equals("Y")){
					segment.setDistributor(true);
				}else{
					segment.setDistributor(false);
				}
				if(StringManipulation.noNull(rs.getString("TARGET_MARKET_SEGMENT_FLAG")).equals("Y")){
					segment.setTargetMarketSegment(true);
				}else{
					segment.setTargetMarketSegment(false);
				}
				if(StringManipulation.noNull(rs.getString("DISTRIBUTOR_DISPLAY_FLAG")).equals("Y")){
					segment.setDistributorFormDisplay(true);
				}else{
					segment.setDistributorFormDisplay(false);
				}
				if(StringManipulation.noNull(rs.getString("HIDDEN_FLAG")).equals("Y")){
					segment.setHidden(true);
				}else{
					segment.setHidden(false);
				}
				segment.setSicCode(StringManipulation.noNull(rs.getString("DEFAULT_SIC_CODE")));				
                results.add(segment);
			}
		}catch (Exception e)	{
			SMRCLogger.error("SegmentsDAO.getTargetMarketSegments(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
	
		return results;
	}
	
    public static String getSegmentName(int segmentId, Connection DBConn) throws Exception {
		String segmentName = new String();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		SMRCLogger.debug("SQL - SegmentsDAO.getSegmentName:\n" + getSegmentNameQuery);
		
		try {
			pstmt = DBConn.prepareStatement(getSegmentNameQuery);
			
			pstmt.setInt(1, segmentId);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				segmentName = rs.getString("SEGMENT_NAME");
				
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("SegmentsDAO.getSegmentName(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
	
		return segmentName;
	}	
	
    
}
