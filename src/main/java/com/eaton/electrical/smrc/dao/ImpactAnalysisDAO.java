package com.eaton.electrical.smrc.dao;

import java.sql.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.service.*;

/**
 * @author E0062708
 *
 */

public class ImpactAnalysisDAO {
	private static final String getImpactAnalysisQuery = "SELECT * FROM DISTRIB_IMPACT_ANALYSIS WHERE DISTRIBUTOR_ID=?";
	private static final String saveImpactAnalysisInsert = "INSERT INTO DISTRIB_IMPACT_ANALYSIS (" +
			"MAINTAIN_DOLLARS,GROW_DOLLARS,PENETRATE_DOLLARS,ADD_DOLLARS,TERMINATE_DOLLARS,RISK_DOLLARS,OTHER_CHAIN_IMPACT," +
			"DISTRIBUTOR_ID,DATE_ADDED) VALUES (?,?,?,?,?,?,?,?,?)";
	private static final String saveImpactAnalysisUpdate = "UPDATE DISTRIB_IMPACT_ANALYSIS SET MAINTAIN_DOLLARS=?,GROW_DOLLARS=?,"+
			"PENETRATE_DOLLARS=?,ADD_DOLLARS=?,TERMINATE_DOLLARS=?,RISK_DOLLARS=?,OTHER_CHAIN_IMPACT=?"+
			" WHERE DISTRIBUTOR_ID=? AND DATE_ADDED=?"; 

	
	public static void saveImpactAnalysis(DistributorImpactAnalysis impactAnalysis, Connection DBConn) throws Exception{
		int distId= impactAnalysis.getDistributorId();
		if ( distId==0 ){	
			insertNewImpactAnalysis(impactAnalysis,true, DBConn);
		}
		else{	
			insertNewImpactAnalysis(impactAnalysis,false, DBConn);
		}	
	}
	
	private static void insertNewImpactAnalysis(DistributorImpactAnalysis impactAnalysis,boolean insert, Connection DBConn) throws Exception
	{

		//DistributorDAO.initializeDistributor(impactAnalysis.getVcn(), DBConn);
		
		PreparedStatement pstmt = null;
		
		try {
			if(insert){
				pstmt = DBConn.prepareStatement(saveImpactAnalysisInsert);
			}else{
				pstmt = DBConn.prepareStatement(saveImpactAnalysisUpdate);
			}
			int pIndex = 0 ;

			
			pstmt.setDouble( ++pIndex, impactAnalysis.getMaintainDollars());
			pstmt.setDouble( ++pIndex, impactAnalysis.getGrowDollars());
			pstmt.setDouble( ++pIndex, impactAnalysis.getPenetrateDollars());
			pstmt.setDouble( ++pIndex, impactAnalysis.getAddDollars());
			pstmt.setDouble( ++pIndex, impactAnalysis.getTerminateDollars());
			pstmt.setDouble( ++pIndex, impactAnalysis.getRiskDollars());
			pstmt.setDouble( ++pIndex, impactAnalysis.getOtherChainImpact());
			if(insert){
				pstmt.setInt( ++pIndex, AccountDAO.getDistributorId(impactAnalysis.getVcn(), DBConn));
			}else{
				pstmt.setInt( ++pIndex, impactAnalysis.getDistributorId());
			}
			if(insert){
				pstmt.setDate( ++pIndex, new java.sql.Date( new java.util.Date().getTime()));
			}else{
				pstmt.setDate( ++pIndex, impactAnalysis.getDateAdded());
			}

			pstmt.executeUpdate();

			
		}catch (Exception e)	{
			SMRCLogger.error("ImpactAnalysisDAO.insertNewImpactAnalysis(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
	}
	
	public static DistributorImpactAnalysis getImpactAnalysis(String acctId, Connection DBConn) throws Exception{
		DistributorImpactAnalysis impactAnalysis = new DistributorImpactAnalysis();		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getImpactAnalysisQuery);
			
			pstmt.setInt(1, AccountDAO.getDistributorId(acctId, DBConn));
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				impactAnalysis.setVcn(acctId);
				impactAnalysis.setDistributorId(rs.getInt("DISTRIBUTOR_ID"));
				impactAnalysis.setDateAdded(rs.getDate("DATE_ADDED"));
				impactAnalysis.setMaintainDollars(rs.getDouble("MAINTAIN_DOLLARS"));
				impactAnalysis.setGrowDollars(rs.getDouble("GROW_DOLLARS"));
				impactAnalysis.setPenetrateDollars(rs.getDouble("PENETRATE_DOLLARS"));
				impactAnalysis.setAddDollars(rs.getDouble("ADD_DOLLARS"));
				impactAnalysis.setTerminateDollars(rs.getDouble("TERMINATE_DOLLARS"));
				impactAnalysis.setRiskDollars(rs.getDouble("RISK_DOLLARS"));
				impactAnalysis.setOtherChainImpact(rs.getDouble("OTHER_CHAIN_IMPACT"));

			}
		}catch (Exception e)	{
			SMRCLogger.error("ImpactAnalysisDAO.getImpactAnalysis(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
			
	return impactAnalysis;
		
	}
	
	
}
