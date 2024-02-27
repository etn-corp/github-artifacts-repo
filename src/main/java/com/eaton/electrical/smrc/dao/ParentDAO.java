package com.eaton.electrical.smrc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.eaton.electrical.smrc.bo.Parent;
import com.eaton.electrical.smrc.service.SMRCConnectionPoolUtils;
import com.eaton.electrical.smrc.service.SMRCLogger;
import com.eaton.electrical.smrc.util.StringManipulation;


/**
 * @author E0073445
 *
 */

public class ParentDAO {
	
	private static final String getParentQuery = "SELECT VISTA_CUSTOMER_NUMBER, CUSTOMER_NAME from CUSTOMER cust WHERE cust.vista_customer_number=?";
	
	/**
	 * Gets a Parent object based on a passed in VCN
	 * @param acctId  the VCN 
	 * @param DBConn  the database connection
	 * @return an Parent for the specified acctId
	 * @throws Exception
	 */
	public static Parent getAccount(String acctId, Connection DBConn) throws Exception{
		
		Parent parent = new Parent();	

		if ((acctId == null) || (acctId.length() < 1)) {
			return parent;
		}
		

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		try {
			pstmt = DBConn.prepareStatement(getParentQuery);
			
			pstmt.setString(1, acctId);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
				parent.setVistaId(StringManipulation.noNull(rs.getString("VISTA_CUSTOMER_NUMBER"))) ;
				parent.setParentName(StringManipulation.noNull(rs.getString("CUSTOMER_NAME"))) ;
			
			}
		}catch (Exception e)	{
			SMRCLogger.error("Parent.getAccount() for Account Id : " + acctId, e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return parent;
		
	}
	
	
	
}
