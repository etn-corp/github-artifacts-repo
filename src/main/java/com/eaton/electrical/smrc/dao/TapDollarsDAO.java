
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
public class TapDollarsDAO {
	
	private static final String getDivisionsQuery = "SELECT * FROM DIVISIONS";
	private static final String getDivisionQuery = "SELECT * FROM DIVISIONS WHERE DIVISION_ID=?";
	
	public static ArrayList getDivisions(Connection DBConn) throws Exception{
		ArrayList divisions = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getDivisionsQuery);

			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				Division div = new Division();			
				
				div.setId(StringManipulation.noNull(""+rs.getInt("DIVISION_ID")));
				div.setCode(StringManipulation.noNull(rs.getString("DIVISION_CODE")));
				div.setName(StringManipulation.noNull(rs.getString("DIVISION_DESCRIPTION")));
				
				divisions.add(div);
			}
		}catch (Exception e)	{
			SMRCLogger.error("DivisionsDAO.getDivisions(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return divisions;
	}

	
	public static Division getDivision(String divisionId, Connection DBConn) throws Exception{
		Division div = new Division();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getDivisionQuery);
			pstmt.setInt(1, Globals.a2int(divisionId));
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				div.setId(StringManipulation.noNull(""+rs.getInt("DIVISION_ID")));
				div.setCode(StringManipulation.noNull(rs.getString("DIVISION_CODE")));
				div.setName(StringManipulation.noNull(rs.getString("DIVISION_DESCRIPTION")));
			}
		}catch (Exception e)	{
			SMRCLogger.error("DivisionsDAO.getDivision(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
	
		return div;
	}
	
}
