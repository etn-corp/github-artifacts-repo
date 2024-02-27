package com.eaton.electrical.smrc.dao;

import java.sql.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

/**
 * @author E0062708
 *
 */
public class DistributorTerminationDAO {
	
	private static final String getDistributorTerminationQuery = "SELECT * FROM DISTRIB_TERMINATION WHERE VISTA_CUSTOMER_NUMBER=?";
	private static final String doesTerminationExistQuery = "SELECT COUNT(*) FROM DISTRIB_TERMINATION WHERE DISTRIBUTOR_ID=?";
	private static final String distributorTerminationInsert = "INSERT INTO DISTRIB_TERMINATION (" +
	"VISTA_CUSTOMER_NUMBER,PROPOSED_TERMINATION_DATE,TERMINATION_EFFECTIVE_DATE,TERMINATION_REASON_TYPE_ID,EXPLANATION,ACTION_NOTES," +
	"ESTIMATED_INVENTORY_STDDE,ESTIMATED_INVENTORY_PDCD,ESTIMATED_INVENTORY_STDCTL,POT_RETURN_STDDE,POT_RETURN_PDCD,POT_RETURN_STDCTL," +
	"DISTRIBUTOR_ID,TERMINATION_REQUEST_DATE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE)";
	private static final String distributorTerminationUpdate = "UPDATE DISTRIB_TERMINATION SET VISTA_CUSTOMER_NUMBER=?,PROPOSED_TERMINATION_DATE=?,"+
	"TERMINATION_EFFECTIVE_DATE=?,TERMINATION_REASON_TYPE_ID=?,EXPLANATION=?,ACTION_NOTES=?,ESTIMATED_INVENTORY_STDDE=?,ESTIMATED_INVENTORY_PDCD=?,ESTIMATED_INVENTORY_STDCTL=?," +
	"POT_RETURN_STDDE=?,POT_RETURN_PDCD=?,POT_RETURN_STDCTL=?"+
	" WHERE DISTRIBUTOR_ID=? AND trunc(TERMINATION_REQUEST_DATE) = ?"; 

	
	public static DistributorTermination getDistributorTermination(String acctId, Connection DBConn) throws Exception{
		DistributorTermination distTerm = new DistributorTermination();		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getDistributorTerminationQuery);
			
			pstmt.setString(1, acctId);
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				distTerm.setVcn(acctId);
				distTerm.setDistributorId(rs.getInt("DISTRIBUTOR_ID"));
				distTerm.setRequestDate(rs.getDate("TERMINATION_REQUEST_DATE"));
				distTerm.setProposedDate(rs.getDate("PROPOSED_TERMINATION_DATE"));
				distTerm.setEffectiveDate(rs.getDate("TERMINATION_EFFECTIVE_DATE"));
				distTerm.setReasonTypeId(rs.getInt("TERMINATION_REASON_TYPE_ID"));
				distTerm.setExplanation(StringManipulation.noNull(rs.getString("EXPLANATION")));
				distTerm.setActionNotes(StringManipulation.noNull(rs.getString("ACTION_NOTES")));
				distTerm.setEstInventoryStdDE(rs.getInt("ESTIMATED_INVENTORY_STDDE"));
				distTerm.setEstInventoryPDCD(rs.getInt("ESTIMATED_INVENTORY_PDCD"));
				distTerm.setEstInventoryStdControl(rs.getInt("ESTIMATED_INVENTORY_STDCTL"));
				distTerm.setPotReturnStdDE(rs.getInt("POT_RETURN_STDDE"));
				distTerm.setPotReturnPDCD(rs.getInt("POT_RETURN_PDCD"));
				distTerm.setPotReturnStdControl(rs.getInt("POT_RETURN_STDCTL"));
			}
		}catch (Exception e)	{
			SMRCLogger.error("DistributorTerminationDAO.getDistributorTermination(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
			
	return distTerm;
		
	}
	
	public static void saveDistributorTermination(DistributorTermination distTerm, Connection DBConn) throws Exception{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// check to see if the distributor record (parent key) exists in distributor table
			Distributor dist = new Distributor();
			dist.setVcn(distTerm.getVcn());

			if(distTerm.getDistributorId()==0){
				distTerm.setDistributorId(AccountDAO.getDistributorId(distTerm.getVcn(), DBConn));
			}
			
			pstmt = DBConn.prepareStatement(doesTerminationExistQuery);
			pstmt.setInt(1,distTerm.getDistributorId());
			rs=pstmt.executeQuery();
			rs.next();	
			
			if(rs.getInt(1)==0){
				updateDistributorTermination(distTerm,true, DBConn);
			}
			else{	
				updateDistributorTermination(distTerm,false, DBConn);
			}	
				

		}catch (Exception e)	{
			SMRCLogger.error("DistributorTerminationDAO.saveDistributorTermination(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}				
	}
	
	private static void updateDistributorTermination(DistributorTermination distTerm,boolean insert, Connection DBConn) throws Exception
	{
		PreparedStatement pstmt = null;

		try {
			if(insert){		
				pstmt = DBConn.prepareStatement(distributorTerminationInsert);
			}else{
				pstmt = DBConn.prepareStatement(distributorTerminationUpdate);
			}
			int pIndex = 0 ;

			pstmt.setString( ++pIndex, distTerm.getVcn());
			pstmt.setDate( ++pIndex, new java.sql.Date(distTerm.getProposedDate().getTime()));
			pstmt.setDate( ++pIndex, new java.sql.Date(distTerm.getEffectiveDate().getTime()));
			pstmt.setInt( ++pIndex, distTerm.getReasonTypeId());
			pstmt.setString( ++pIndex, distTerm.getExplanation());
			pstmt.setString( ++pIndex, distTerm.getActionNotes());
			pstmt.setDouble( ++pIndex, distTerm.getEstInventoryStdDE());
			pstmt.setDouble( ++pIndex, distTerm.getEstInventoryPDCD());
			pstmt.setDouble( ++pIndex, distTerm.getEstInventoryStdControl());
			pstmt.setDouble( ++pIndex, distTerm.getPotReturnStdDE());
			pstmt.setDouble( ++pIndex, distTerm.getPotReturnPDCD());
			pstmt.setDouble( ++pIndex, distTerm.getPotReturnStdControl());
			pstmt.setInt( ++pIndex, distTerm.getDistributorId());
			if(!insert){
				pstmt.setDate( ++pIndex, new java.sql.Date(distTerm.getRequestDate().getTime()));
			}
			
			pstmt.executeUpdate();
			
		}catch (Exception e)	{
			SMRCLogger.error("DistributorTerminationDAO.updateDistributorTermination(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}	
		
	}
	
}
