package com.eaton.electrical.smrc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.eaton.electrical.smrc.service.SMRCConnectionPoolUtils;
import com.eaton.electrical.smrc.service.SMRCLogger;

public class CodeTypeDAO {

	private static final String getCodeTypeId = "select ct.code_type_id from code_types ct where ct.code_value = ?";
	private static final String getCodeTypeDescriptionByCodeTypeId = "select ct.code_description from code_types ct where ct.code_type_id = ?";
	
	public static int getCodeTypeId (String codeValue, Connection DBConn) throws Exception{
		
		int codeTypeId = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getCodeTypeId);
			pstmt.setString(1, codeValue);
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				codeTypeId = rs.getInt("code_type_id");
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getCodeTypeId(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return codeTypeId;
	}
	
	public String getCodeTypeDescriptionByCodeTypeId(int cid, Connection DBConn) throws Exception {
		String description="";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getCodeTypeDescriptionByCodeTypeId);
			pstmt.setInt(1, cid);
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				description = rs.getString("CODE_DESCRIPTION");
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getCodeTypeId(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return description;
	}
}
