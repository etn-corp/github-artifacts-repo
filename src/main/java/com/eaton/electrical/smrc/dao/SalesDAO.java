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

public class SalesDAO {
	private static final String getSalesmanNameQry = "SELECT initcap(lower(first_nm)) as first_nm, initcap(lower(last_nm)) as last_nm FROM current_salesman_v WHERE salesman_id = ? ORDER BY start_dt desc";
	private static final String getSalesGroupQry = "SELECT * FROM sales_groups WHERE group_id = ?";
	private static final String browseSEQuery1 = "select * from current_salesman_v where salesman_id=?";
	private static final String browseSEQuery2 = 
		"select * from ( " +
			"select salesman_id, last_nm, first_nm, middle_init, sales_office_cd, sales_org_cd, sales_org_nm, group_cd, group_nm, " +
				"zone_cd, zone_nm, district_cd, district_nm, team_cd, team_nm, company_cd, division_cd, country_cd, title_tx, " +
				"status_cd, active_dt, inactive_dt, sp_geog_cd, function_cd, prior_salesman_id, start_dt, available_for_credit, rownum rn " +
				"from ( " +
					"select salesman_id, last_nm, first_nm, middle_init, sales_office_cd, sales_org_cd, sales_org_nm, group_cd, group_nm, " +
					"zone_cd, zone_nm, district_cd, district_nm, team_cd, team_nm, company_cd, division_cd, country_cd, title_tx, " +
					"status_cd, active_dt, inactive_dt, sp_geog_cd, function_cd, prior_salesman_id, start_dt, available_for_credit, rownum rn " +
					"from current_salesman_v " +
					"where LOWER(last_nm) like LOWER(?) AND AVAILABLE_FOR_CREDIT='Y' " +
					"order by last_nm, first_nm " +
			") " +
		") where rn >= ? and rn <= ?";

	public static String getSalesmanName(String salesId, Connection DBConn) throws Exception {
		String name = "";
		
		PreparedStatement pstmt = null;
		ResultSet rs  = null;
		
		try {
			pstmt = DBConn.prepareStatement(getSalesmanNameQry);			
			pstmt.setString(1, salesId);
			rs = pstmt.executeQuery();
			
			if (rs.next())		// Only want the 1st occurance so use if instead of while
			{
				name = rs.getString("first_nm") + " "  + rs.getString("last_nm");
			}
		}catch (Exception e)	{
			SMRCLogger.error("SalesDAO.getSalesmanName(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return name;
	}
	
	public static SalesGroup getSalesGroup(String groupId, Connection DBConn) throws Exception {
		SalesGroup group = new SalesGroup();

		PreparedStatement pstmt = null;
		ResultSet rs  = null;
		
		try {
			pstmt = DBConn.prepareStatement(getSalesGroupQry);
			
			pstmt.setString(1, groupId);
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				group.setId(rs.getString("group_id"));
				group.setDescription(rs.getString("group_description"));
				group.setLogo(rs.getString("group_logo"));
				group.setViewVolume(rs.getString("view_volume"));
				group.setSalesOrders(rs.getString("sales_orders"));
				group.setDollarType(rs.getString("dollar_type"));

				if (rs.getString("num_of_description") != null) {
					group.setNumberOfDescription(rs.getString("num_of_description"));
				}
				
				if (rs.getString("show_global_acct_mgr") != null) {
					group.setUseGlobalAcctMgr(rs.getString("show_global_acct_mgr"));
				}
				
				if (rs.getString("show_slsmn_pcnt_with_cust") != null) {
					group.setUseSlsmnPcntWithCust(rs.getString("show_slsmn_pcnt_with_cust"));
				}
			}
		}catch (Exception e)	{
			SMRCLogger.error("SalesDAO.getSalesGroup(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return group;
	}

	/*
	public static District getDistrictBySE(String SEid)throws Exception {
		District dist = new District();
		dist.setName("Pittsburgh, Pa");
		dist.setCode("D");
		
		return dist;
	}
	*/
        
    public static ArrayList browseSE (String searchString, String howToSearch, Connection DBConn, int firstRecordNumber ) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs  = null;
        ArrayList salesmanList = new ArrayList();
		try {

	        if(howToSearch.equals("seid")){
	        	pstmt = DBConn.prepareStatement(browseSEQuery1);
	        	pstmt.setString(1, searchString);
	        	SMRCLogger.debug(browseSEQuery1 + "\n?=" + searchString);
	        }else{
	        	pstmt = DBConn.prepareStatement(browseSEQuery2);
	        	pstmt.setString(1, searchString + "%");
	        	
				// Set the current page indexing
				
				pstmt.setInt(2, firstRecordNumber);
				pstmt.setInt(3, firstRecordNumber + 50); // Get one extra record to see if there is another page
	        	SMRCLogger.debug(browseSEQuery2 + "\n?=" + searchString + "%");
	        }
	        
	        
			rs = pstmt.executeQuery();
                       
			while (rs.next())
			{
            	Salesman salesman = new Salesman();
                salesman.setFirstName(StringManipulation.noNull(rs.getString("FIRST_NM")));
                salesman.setLastName(StringManipulation.noNull(rs.getString("LAST_NM")));
                salesman.setGeogCd(StringManipulation.noNull(rs.getString("SP_GEOG_CD")));
                salesman.setSalesId(StringManipulation.noNull(rs.getString("SALESMAN_ID")));
                salesman.setSalesOffice(StringManipulation.noNull(rs.getString("SALES_OFFICE_CD")));
                salesman.setDistrictName(StringManipulation.noNull(rs.getString("DISTRICT_NM")));
                salesmanList.add(salesman);               
			}
		}catch (Exception e)	{
			SMRCLogger.error("SalesDAO.browseSE(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
         return salesmanList;
	}
	
	
}
