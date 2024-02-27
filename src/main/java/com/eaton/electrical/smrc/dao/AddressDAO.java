package com.eaton.electrical.smrc.dao;

import java.sql.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

/**
 * This class handles getting addresses from the database
 * 
 * @author E0027146
 */
public class AddressDAO {

	private static final String getAccountQuery = "select addr1, addr2, addr3, addr4, city, state, zip from users where users.userid =?";
	
	public AddressDAO() {}
	
	public static Address getAddressForUser( String aUserID ) throws Exception {
		
		Connection DBConn = null;

		Address address = new Address();	
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			DBConn = SMRCConnectionPoolUtils.getDatabaseConnection() ;
			pstmt = DBConn.prepareStatement(getAccountQuery);
			
			pstmt.setString(1, aUserID);
			rs = pstmt.executeQuery();
			
			// There should only be one user record
			if (rs.next()) {
				
				address.setAddress1(StringManipulation.noNull(rs.getString("ADDR1"))) ;
				address.setAddress2(StringManipulation.noNull(rs.getString("ADDR2"))) ;
				address.setAddress3(StringManipulation.noNull(rs.getString("ADDR3"))) ;
				address.setAddress4(StringManipulation.noNull(rs.getString("ADDR4"))) ;
				address.setCity(StringManipulation.noNull(rs.getString("CITY"))) ;
				address.setState(StringManipulation.noNull(rs.getString("STATE"))) ;
				address.setZip(StringManipulation.noNull(rs.getString("ZIP"))) ;
				
			}
		}catch (Exception e)	{
			SMRCLogger.error("AccountDAO.getAccount(): " , e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return address;
		
	}

}
