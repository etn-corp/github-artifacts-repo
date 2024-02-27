package com.eaton.electrical.smrc.dao;

import java.sql.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;
/**
 * @author E0062708
 *
 */
public class CreditAuthorizationDAO {
	
	public static final String getCreditAuthQuery = "SELECT * FROM CREDIT_AUTHORIZATION WHERE VISTA_CUSTOMER_NUMBER=?";
	public static final String saveCreditAuthInsert = "INSERT INTO CREDIT_AUTHORIZATION (FINANCIAL_INFORMATION,TRADE_REFERENCE1,"+
		"TRADE_REFERENCE2,TRADE_REFERENCE3,TRADE_REFERENCE1_ADDR,TRADE_REFERENCE2_ADDR,TRADE_REFERENCE3_ADDR,"+
		"TRADE_REFERENCE1_PHONE,TRADE_REFERENCE2_PHONE,TRADE_REFERENCE3_PHONE,TRADE_REFERENCE1_FAX,TRADE_REFERENCE2_FAX,"+
		"TRADE_REFERENCE3_FAX,ADDITIONAL_SALES_INFORMATION,VISTA_CUSTOMER_NUMBER,CREDIT_REQUEST_DATE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String saveCreditAuthUpdate = "UPDATE CREDIT_AUTHORIZATION SET FINANCIAL_INFORMATION=?,TRADE_REFERENCE1=?,"+
	"TRADE_REFERENCE2=?,TRADE_REFERENCE3=?,TRADE_REFERENCE1_ADDR=?,TRADE_REFERENCE2_ADDR=?,TRADE_REFERENCE3_ADDR=?,"+
	"TRADE_REFERENCE1_PHONE=?,TRADE_REFERENCE2_PHONE=?,TRADE_REFERENCE3_PHONE=?,TRADE_REFERENCE1_FAX=?,TRADE_REFERENCE2_FAX=?,"+
	"TRADE_REFERENCE3_FAX=?,ADDITIONAL_SALES_INFORMATION=? WHERE VISTA_CUSTOMER_NUMBER=? AND CREDIT_REQUEST_DATE=?"; 

	
	public static void saveCreditAuth(DistributorCreditAuthorization creditAuth, Connection DBConn) throws Exception{

		Date theDate = creditAuth.getRequestDate();
		if ( theDate==null ){
			insertNewCreditAuth(creditAuth,true, DBConn);
		}else{	
			insertNewCreditAuth(creditAuth,false, DBConn);
		}	
	}
	
	private static void insertNewCreditAuth(DistributorCreditAuthorization creditAuth,boolean insert, Connection DBConn) throws Exception
	{

		PreparedStatement pstmt = null;

		try {
			if(insert){
				pstmt = DBConn.prepareStatement(saveCreditAuthInsert);
			}else{
				pstmt = DBConn.prepareStatement(saveCreditAuthUpdate);
			}
			int pIndex = 0 ;

			
			pstmt.setString( ++pIndex, creditAuth.getFinancialInformation());
			pstmt.setString( ++pIndex, creditAuth.getTradeReference1());
			pstmt.setString( ++pIndex, creditAuth.getTradeReference2());
			pstmt.setString( ++pIndex, creditAuth.getTradeReference3());
			pstmt.setString( ++pIndex, creditAuth.getTradeReference1Addr());
			pstmt.setString( ++pIndex, creditAuth.getTradeReference2Addr());
			pstmt.setString( ++pIndex, creditAuth.getTradeReference3Addr());
			pstmt.setString( ++pIndex, creditAuth.getTradeReference1Phone());
			pstmt.setString( ++pIndex, creditAuth.getTradeReference2Phone());
			pstmt.setString( ++pIndex, creditAuth.getTradeReference3Phone());
			pstmt.setString( ++pIndex, creditAuth.getTradeReference1Fax());
			pstmt.setString( ++pIndex, creditAuth.getTradeReference2Fax());
			pstmt.setString( ++pIndex, creditAuth.getTradeReference3Fax());
			pstmt.setString( ++pIndex, creditAuth.getAdditionalSalesInfo());
			pstmt.setString( ++pIndex, creditAuth.getVcn());
			if(insert){
				pstmt.setDate( ++pIndex, new java.sql.Date( new java.util.Date().getTime()));
			}else{
				pstmt.setDate( ++pIndex, creditAuth.getRequestDate());
			}
			
			pstmt.executeUpdate();

		}catch (Exception e)	{
			SMRCLogger.error("CreditAuthorizationDAO.insertNewCreditAuth(): ", e);
			throw e;
		}
		finally {

			SMRCConnectionPoolUtils.close(pstmt);

		}

	}

	
	public static DistributorCreditAuthorization getCreditAuth(String acctId, Connection DBConn) throws Exception{
		DistributorCreditAuthorization creditAuth = new DistributorCreditAuthorization();		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DBConn.prepareStatement(getCreditAuthQuery);
			
			pstmt.setString(1, acctId);
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				creditAuth.setVcn(acctId);
				creditAuth.setRequestDate(rs.getDate("CREDIT_REQUEST_DATE"));
				creditAuth.setSalesEngineer(StringManipulation.noNull(rs.getString("SALES_ENGINEER")));
				creditAuth.setDistrictManager(StringManipulation.noNull(rs.getString("DISTRICT_MANAGER")));
				creditAuth.setDistributionManager(StringManipulation.noNull(rs.getString("DISTRIBUTION_MANAGER")));
				creditAuth.setCreditManager(StringManipulation.noNull(rs.getString("CREDIT_MANAGER")));
				creditAuth.setRequestedCreditLine(StringManipulation.noNull(rs.getString("REQUESTED_CREDIT_LINE")));
				creditAuth.setApprovedCreditLine(StringManipulation.noNull(rs.getString("APPROVED_CREDIT_LINE")));
				creditAuth.setDistMgrUpdateDate(rs.getDate("DIST_MGR_UPDATE_DATE"));
				creditAuth.setDistMgrApproved(StringManipulation.noNull(rs.getString("DIST_MGR_APPROVED")));
				creditAuth.setDistrMgrUpdateDate(rs.getDate("DISTR_MGR_UPDATE_DATE"));
				creditAuth.setDistrMgrApproved(StringManipulation.noNull(rs.getString("DISTR_MGR_APPROVED")));
				creditAuth.setCreditMgrUpdateDate(rs.getDate("CREDIT_MGR_UPDATE_DATE"));
				creditAuth.setCreditMgrApproved(StringManipulation.noNull(rs.getString("CREDIT_MGR_APPROVED")));
				creditAuth.setFinancialInformation(StringManipulation.noNull(rs.getString("FINANCIAL_INFORMATION")));
				creditAuth.setTradeReference1(StringManipulation.noNull(rs.getString("TRADE_REFERENCE1")));
				creditAuth.setTradeReference2(StringManipulation.noNull(rs.getString("TRADE_REFERENCE2")));
				creditAuth.setTradeReference3(StringManipulation.noNull(rs.getString("TRADE_REFERENCE3")));
				creditAuth.setTradeReference1Addr(StringManipulation.noNull(rs.getString("TRADE_REFERENCE1_ADDR")));
				creditAuth.setTradeReference2Addr(StringManipulation.noNull(rs.getString("TRADE_REFERENCE2_ADDR")));
				creditAuth.setTradeReference3Addr(StringManipulation.noNull(rs.getString("TRADE_REFERENCE3_ADDR")));
				creditAuth.setTradeReference1Phone(StringManipulation.noNull(rs.getString("TRADE_REFERENCE1_PHONE")));
				creditAuth.setTradeReference2Phone(StringManipulation.noNull(rs.getString("TRADE_REFERENCE2_PHONE")));
				creditAuth.setTradeReference3Phone(StringManipulation.noNull(rs.getString("TRADE_REFERENCE3_PHONE")));
				creditAuth.setTradeReference1Fax(StringManipulation.noNull(rs.getString("TRADE_REFERENCE1_FAX")));
				creditAuth.setTradeReference2Fax(StringManipulation.noNull(rs.getString("TRADE_REFERENCE2_FAX")));
				creditAuth.setTradeReference3Fax(StringManipulation.noNull(rs.getString("TRADE_REFERENCE3_FAX")));
				creditAuth.setAdditionalSalesInfo(StringManipulation.noNull(rs.getString("ADDITIONAL_SALES_INFORMATION")));
			}
		}catch (Exception e)	{
			SMRCLogger.error("CreditAuthorizationDAO.getCreditAuth(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);

		}
			
	return creditAuth;
		
	}
	
	
}
