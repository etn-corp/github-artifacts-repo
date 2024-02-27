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
public class DistrictDAO {
	
	private static final String getDistrictNameQry = "select * from geographies where sp_geog = ?";

	private static final String getAllDistrictsQuery = "SELECT sp_geog,description,zone,district from geographies where sp_geog like '146%' " +
	"and (team is null or team = '' or team = ' ')" +
	" and (substr(sp_geog,5,1) <> '0') and period_yyyy=?" +
	" order by sp_geog";
	
	
	private static final String getDistrictsAndZonesLikeQuery = "SELECT sp_geog,description,zone,district from geographies " + 		
	"where sp_geog like ? and (team is null or team = '' or team = ' ') " +
	"and (substr(sp_geog,4,1) <> '0') order by sp_geog";
	private static final String getCustomerSearchDistrictsQuery="select *"+
	" from geographies where sales_org='1' AND group_code='4' and district!='0' and team is null order by zone, district";
	
	private static final String getAllTeamsQuery = "SELECT sp_geog,description,zone,district from geographies " + 
	"where sp_geog like '145%' " +
	"and team is not null and period_yyyy=?" + 
	"order by sp_geog";
	private static final String getAllGroupCodesQuery = "SELECT * from geographies " + 
	"where sp_geog like '1%' " +
	"and team is null " +
	"and zone = 0 " +
	"and district = 0 " +
	"and group_code <> 0 and period_yyyy=?" +   // This line added to prevent sales orgs from showing up in group codes
	"order by sp_geog";
	private static final String getAllSalesOrganizationsQuery = "SELECT * from geographies " +
	"where team is null " +
	"and zone = 0 " +
	"and district = 0 " +
	"and group_code = 0 and period_yyyy=?" +
	"order by sp_geog";
	
	private static final String getManagerforGeography = "select user_id from salesman_detail_mv where title_tx = ? " +
	"and sp_geog_cd = ? and status_cd = 'A' order by start_dt desc";
/*
	private static final String getYTDandMTDSalesForDistrict = "with se as ( select salesman_id from salesman_detail_mv " +
	"where sp_geog_cd like ? and inactive_dt is null and to_number(to_char(start_dt,'YYYY')) = ? ) " +
	"select sum(case when (month <= ?) and (year = ?) then u.total_sales else 0 end) curr_ytd_total, " +
	"sum(case when (month = ?) and (year = ?) then u.total_sales else 0 end) curr_mtd_total, " +
	"sum(case when (month <= ?) and (year = ?) then u.goals_sales else 0 end) ytd_goals, " +
	"sum(case when (month = ?) and (year = ?) then u.goals_sales else 0 end) month_goals, " +
	"sum(case when (month = ?) and (year = ?) then u.mtd_goals_sales else 0 end) mtd_goals " +
	"from credit_salesman_sales u, se, products where u.salesman_id = se.salesman_id " + 
	" and products.product_id = u.product_id and products.sp_load_total = 'T' and " +
    "products.period_yyyy = ? ";
    
	private static final String getYTDandMTDSalesForDistrict = "with se as ( select salesman_id from current_salesman_v " + 
    " where sales_org_cd = ? and group_cd = ? and zone_cd = ? and district_cd = ? ) " +
	"select sum(case when (month <= ?) and (year = ?) then u.total_sales else 0 end) curr_ytd_total, " +
	"sum(case when (month = ?) and (year = ?) then u.total_sales else 0 end) curr_mtd_total, " +
	"sum(case when (month <= ?) and (year = ?) then u.goals_sales else 0 end) ytd_goals, " +
	"sum(case when (month = ?) and (year = ?) then u.goals_sales else 0 end) month_goals, " +
	"sum(case when (month = ?) and (year = ?) then u.mtd_goals_sales else 0 end) mtd_goals " +
	"from credit_salesman_sales u, se, products where u.salesman_id = se.salesman_id " + 
	" and products.product_id = u.product_id and products.sp_load_total = 'T' and " +
    "products.period_yyyy = ? ";
	private static final String getYTDSalesForDistrictTarget = "with se as ( select salesman_id from current_salesman_v " + 
    " where sales_org_cd = ? and group_cd = ? and zone_cd = ? and district_cd = ? ) " +
	"select sum(case when (month <= ?) and (year = ?) then u.total_sales else 0 end) curr_ytd_total " +
	"from credit_customer_sales u, se, customer, products where u.salesman_id = se.salesman_id and u.credit_customer_number = customer.vista_customer_number and customer.target_account_flag = 'Y' " +
	" and products.product_id = u.product_id and products.sp_load_total = 'T' and " +
    "products.period_yyyy = ? ";
	private static final String getYTDSalesForDistrictDivisionTargets = "with se as ( select salesman_id from current_salesman_v " + 
    " where sales_org_cd = ? and group_cd = ? and zone_cd = ? and district_cd = ? ) " +
	"select sum(case when (month <= ?) and (year = ?) then u.total_sales else 0 end) curr_ytd_total " +
	"from credit_customer_sales u, se, customer, products where u.salesman_id = se.salesman_id and u.credit_customer_number = customer.vista_customer_number and customer.target_account_flag = 'Y' " +
	"and (customer.target_account_type_id = 2 or customer.target_account_type_id = 3)" + 
	" and products.product_id = u.product_id and products.sp_load_total = 'T' and " +
    "products.period_yyyy = ? ";
	private static final String getYTDSalesForDistrictSegments = "with se as ( select salesman_id from current_salesman_v " + 
    " where sales_org_cd = ? and group_cd = ? and zone_cd = ? and district_cd = ? )  " +
	"select segments.segment_name, segments.segment_id, sum(case when (month <= ?) and (year = ?) then u.total_sales else 0 end) curr_ytd_total " +
	"from credit_customer_sales u, se, customer_segments cs, segments, products where u.salesman_id = se.salesman_id " +
	"and u.credit_customer_number = cs.vista_customer_number and cs.segment_id = segments.segment_id " +
	"and segments.segment_level = '1' " + 
	" and products.product_id = u.product_id and products.sp_load_total = 'T' and " +
    "products.period_yyyy = ? " + 
	" group by segments.segment_id, segments.segment_name ";
	
	private static final String getYTDandMTDOrdersForDistrict = "with se as ( select salesman_id from current_salesman_v " + 
    " where sales_org_cd = ? and group_cd = ? and zone_cd = ? and district_cd = ? ) " +
	"select sum(case when (month <= ?) and (year = ?) then u.total_orders else 0 end) curr_ytd_total, " +
	"sum(case when (month = ?) and (year = ?) then u.total_orders else 0 end) curr_mtd_total, " +
	"sum(case when (month <= ?) and (year = ?) then u.goals_orders else 0 end) ytd_goals, " +
	"sum(case when (month = ?) and (year = ?) then u.goals_orders else 0 end) month_goals, " +
	"sum(case when (month = ?) and (year = ?) then u.mtd_goals_orders else 0 end) mtd_goals " +
	"from credit_salesman_sales u, se, products where u.salesman_id = se.salesman_id " + 
	" and products.product_id = u.product_id and products.sp_load_total = 'T' and " +
    "products.period_yyyy = ? ";

	private static final String getYTDOrdersForDistrictTarget = "with se as ( select salesman_id from current_salesman_v " + 
    " where sales_org_cd = ? and group_cd = ? and zone_cd = ? and district_cd = ?) " +
	"select sum(case when (month <= ?) and (year = ?) then u.total_orders else 0 end) curr_ytd_total " +
	"from credit_customer_sales u, se, customer, products where u.salesman_id = se.salesman_id and u.credit_customer_number = customer.vista_customer_number and customer.target_account_flag = 'Y'" + 
	" and products.product_id = u.product_id and products.sp_load_total = 'T' and " +
    "products.period_yyyy = ? ";
	private static final String getYTDOrdersForDistrictDivisionTargets = "with se as ( select salesman_id from current_salesman_v " + 
    " where sales_org_cd = ? and group_cd = ? and zone_cd = ? and district_cd = ? ) " +
	"select  sum(case when (month <= ?) and (year = ?) then u.total_orders else 0 end) curr_ytd_total " +
	"from credit_customer_sales u, se, customer, products where u.salesman_id = se.salesman_id and u.credit_customer_number = customer.vista_customer_number and customer.target_account_flag = 'Y' " +
	"and (customer.target_account_type_id = 2 or customer.target_account_type_id = 3)" + 
	" and products.product_id = u.product_id and products.sp_load_total = 'T' and " +
    "products.period_yyyy = ?";
	private static final String getYTDOrdersForDistrictSegments = "with se as ( select salesman_id from current_salesman_v " + 
    " where sales_org_cd = ? and group_cd = ? and zone_cd = ? and district_cd = ? )  " +
	"select segments.segment_name, segments.segment_id, sum(case when (month <= ?) and (year = ?) then u.total_orders else 0 end) curr_ytd_total " +
	"from credit_customer_sales u, se, customer_segments cs, segments, products where u.salesman_id = se.salesman_id " +
	"and u.credit_customer_number = cs.vista_customer_number and cs.segment_id = segments.segment_id " +
	"and segments.segment_level = '1' " + 
	" and products.product_id = u.product_id and products.sp_load_total = 'T' and " +
    "products.period_yyyy = ? " + 
	" group by segments.segment_id, segments.segment_name";
*/
	private static final String getDistrictMonthlyForecast = "select forecast_dollars from district_forecasts where sp_geog = ? " +
	"and year = ? and month = ?";
	
	private static final String insertMonthlyForecast = "insert into district_forecasts (sp_geog, year, month, forecast_dollars, date_added, date_changed, user_added, user_changed) " +
	"values (?, ?, ?, ?, sysdate, sysdate, ?, ?)";
	private static final String updateMonthlyForecast = "update district_forecasts set forecast_dollars = ?, date_changed = sysdate, " +
	"user_changed = ? where sp_geog = ? and year = ? and month = ?";
	
	
	
	public static String getDistrictName(String geog, Connection DBConn) throws Exception {
		StringBuffer spGeog = new StringBuffer(geog);
		
		if (geog.length() < 5) {
			for (int i=geog.length(); i < 5; i++) {
				spGeog.append("0");
			}
		}
		
		String desc = "";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DBConn.prepareStatement(getDistrictNameQry);
			pstmt.setString(1, spGeog.toString());
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				desc = rs.getString("description");
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("DistrictDAO.getDistrictName(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return desc + " (" + spGeog.toString() + ")";
	}
	
	
	// Only return districts in 145xx
	public static ArrayList getAllDistricts(Connection DBConn) throws Exception {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		ArrayList districtList = new ArrayList();
		String currentYear = MiscDAO.getSRYear(DBConn); 
		 
		try {
			pstmt = DBConn.prepareStatement(getAllDistrictsQuery);
			pstmt.setString(1, currentYear);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Geography district = new Geography();
				district.setDescription(rs.getString("description"));
				district.setGeog(rs.getString("sp_geog"));
				districtList.add(district);
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("DistrictDAO.getAllDistricts(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return districtList;
	}
	
	
	public static ArrayList getDistrictsAndZonesLike(String geogString, Connection DBConn) throws Exception {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		ArrayList zoneList = new ArrayList();
		
		try {
			pstmt = DBConn.prepareStatement(getDistrictsAndZonesLikeQuery);
			pstmt.setString(1, geogString + "%");
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Geography zone = new Geography();
				zone.setDescription(rs.getString("description"));
				zone.setGeog(rs.getString("sp_geog"));
				zoneList.add(zone);
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("DistrictDAO.getDistrictsAndZonesLike(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return zoneList;
	}
	
	
	
	public static ArrayList getCustomerSearchDistricts(Connection DBConn) throws Exception{
		ArrayList geographies= new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getCustomerSearchDistrictsQuery);
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				Geography geography = new Geography(rs.getString("SP_GEOG"),rs.getString("DESCRIPTION"));
				geography.setSalesOrg(StringManipulation.noNull(rs.getString("SALES_ORG")));
				geography.setGroupCode(StringManipulation.noNull(rs.getString("GROUP_CODE")));
				geography.setZone(StringManipulation.noNull(rs.getString("ZONE")));
				geography.setDistrict(StringManipulation.noNull(rs.getString("DISTRICT")));
				geography.setTeam(StringManipulation.noNull(rs.getString("TEAM")));
				geographies.add(geography);
			}
		}catch (Exception e)	{
			SMRCLogger.error("DistrictDAO.getCustomerSearchDistricts(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return geographies;
		
	}	
	
	public static ArrayList getAllTeams(Connection DBConn) throws Exception {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		ArrayList teamList = new ArrayList();
		String currentYear = MiscDAO.getSRYear(DBConn);
		System.out.println("**************"+currentYear);
		try {
			pstmt = DBConn.prepareStatement(getAllTeamsQuery);
			pstmt.setString(1, currentYear);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Geography team = new Geography();
				team.setDescription(rs.getString("description"));
				team.setGeog(rs.getString("sp_geog"));
				teamList.add(team);
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("DistrictDAO.getAllTeams(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return teamList;
	}
	
	public static ArrayList getAllGroupCodes(Connection DBConn) throws Exception {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		ArrayList groupcodeList = new ArrayList();
		String currentYear = MiscDAO.getSRYear(DBConn); 
		
		try {
			pstmt = DBConn.prepareStatement(getAllGroupCodesQuery);
			pstmt.setString(1, currentYear);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Geography groupcode = new Geography();
				groupcode.setDescription(rs.getString("description"));
				groupcode.setGeog(rs.getString("sp_geog"));
				groupcodeList.add(groupcode);
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("DistrictDAO.getAllGroupCodes(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return groupcodeList;
	}
	
	public static ArrayList getAllSalesOrganizations(Connection DBConn) throws Exception {
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		ArrayList salesOrgList = new ArrayList();
		String currentYear = MiscDAO.getSRYear(DBConn); 
		
		try {
			pstmt = DBConn.prepareStatement(getAllSalesOrganizationsQuery);
			pstmt.setString(1, currentYear);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Geography salesOrg = new Geography();
				salesOrg.setDescription(rs.getString("description"));
				salesOrg.setGeog(rs.getString("sp_geog"));
				salesOrgList.add(salesOrg);
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("DistrictDAO.getAllSalesOrganizations(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return salesOrgList;
	}
	
	public static String getManagerForGeography (String geog, String managerType, Connection DBConn) throws Exception{
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String vistalineId = new String();
		
		try {
			pstmt = DBConn.prepareStatement(getManagerforGeography);
			pstmt.setString(1, managerType);
			pstmt.setString(2, geog);
			rs = pstmt.executeQuery();
			// Pull the most recent record - ordered by start_dt
			if (rs.next()) {
				vistalineId = rs.getString("user_id");
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("DistrictDAO.getManagerForGeography(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return vistalineId;
		
	}
	
	public static ArrayList getYTDandMTDSalesForDistrict(String district, String salesOrders, int srYear, int srMonth, Connection DBConn) throws Exception {
		ResultSet rs = null;
//		PreparedStatement pstmt = null;
		Statement stmt = null;
		ArrayList results = new ArrayList();
	//	SMRCLogger.debug("\n\n>>>>>>> SQL - DistrictDAO.getYTDandMTDSalesForDistrict():\n" + getYTDandMTDSalesForDistrict + "\n? = " + district + "%\n");
		try {
	/*		if (salesOrders.equalsIgnoreCase("orders")){
				pstmt = DBConn.prepareStatement(getYTDandMTDOrdersForDistrict);
			} else {
				pstmt = DBConn.prepareStatement(getYTDandMTDSalesForDistrict);
			}
			// Break down the sales org, group code, zone, and district to avoid
			// using a "like" statement. This is to avoid an oracle bug using "like"
			// in PreparedStatements with a thin client
			pstmt.setString(1, district.substring(0,1)); 
			pstmt.setString(2, district.substring(1,2));
			pstmt.setString(3, district.substring(2,4));
			pstmt.setString(4, district.substring(4,5));
			pstmt.setInt(5, srMonth);
			pstmt.setInt(6, srYear);
			pstmt.setInt(7, srMonth);
			pstmt.setInt(8, srYear);
			pstmt.setInt(9, srMonth);
			pstmt.setInt(10, srYear);
			pstmt.setInt(11, srMonth);
			pstmt.setInt(12, srYear);
			pstmt.setInt(13, srMonth);
			pstmt.setInt(14, srYear);
			pstmt.setInt(15, srYear);
			*/
			String getYTDandMTDOrdersForDistrict = "with se as ( select salesman_id from current_salesman_v  where sp_geog_cd like '" + district + "%' ) " +
			"select sum(case when (month <= " + srMonth + ") and (year = " + srYear + ") then u.total_" + salesOrders + " else 0 end) curr_ytd_total, " +
			"sum(case when (month = " + srMonth + ") and (year = " + srYear + ") then u.total_" + salesOrders + " else 0 end) curr_mtd_total, " +
			"sum(case when (month <= " + srMonth + ") and (year = " + srYear + ") then u.goals_" + salesOrders + " else 0 end) ytd_goals, " +
			"sum(case when (month = " + srMonth + ") and (year = " + srYear + ") then u.goals_" + salesOrders + " else 0 end) month_goals, " +
			"sum(case when (month = " + srMonth + ") and (year = " + srYear + ") then u.mtd_goals_" + salesOrders + " else 0 end) mtd_goals " +
			"from credit_salesman_sales u, se, products where u.salesman_id = se.salesman_id " + 
			" and products.product_id = u.product_id and products.sp_load_total = 'T' and " +
		    "products.period_yyyy = " + srYear ;
			
			stmt = DBConn.createStatement();
			long initTime=System.currentTimeMillis();
			SMRCLogger.debug("\n\nSQL TIMING\nDistrictDAO.getYTDandMTDSalesForDistrict()\nbefore query: " + initTime+ " ms");
//			rs = pstmt.executeQuery();
			SMRCLogger.debug("\nGoing to execute this query: " + getYTDandMTDOrdersForDistrict);
			rs = stmt.executeQuery(getYTDandMTDOrdersForDistrict);
			long afterTime=System.currentTimeMillis();
			SMRCLogger.debug("after query: " + afterTime + " ms");
			SMRCLogger.debug("difference: " + (afterTime-initTime)/1000 + " s");
			
			
			
			while (rs.next()) {
				DistrictHomePageReportBean bean = new DistrictHomePageReportBean();
				bean.setDescription("District Wide");
				bean.setId(district);
				bean.setCurrYTDTotal(rs.getDouble("curr_ytd_total"));
				bean.setMonthlyTotal(rs.getDouble("curr_mtd_total"));
				bean.setCurrYTDGoal(rs.getDouble("ytd_goals"));
				bean.setMonthlyGoal(rs.getDouble("month_goals"));
				bean.setMTDGoal(rs.getDouble("mtd_goals"));
				results.add(bean);
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("DistrictDAO.getYTDandMTDSalesForDistrict(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(stmt);
		}
		return results;
		
	}
	
	public static ArrayList getYTDSalesForDistrictTarget(String district, String salesOrders, int srYear, int srMonth, Connection DBConn) throws Exception {
		ResultSet rs = null;
//		PreparedStatement pstmt = null;
		Statement stmt = null;
		ArrayList results = new ArrayList();
	//	SMRCLogger.debug("\n\n>>>>>> SQL - DistrictDAO.getYTDSalesForDistrictTarget():\n" + getYTDSalesForDistrictTarget + "\n? = " + district + "%\n");
		try {
	/*		if (salesOrders.equalsIgnoreCase("orders")){
				pstmt = DBConn.prepareStatement(getYTDOrdersForDistrictTarget);
			} else {
				pstmt = DBConn.prepareStatement(getYTDSalesForDistrictTarget);
			}
//			 Break down the sales org, group code, zone, and district to avoid
			// using a "like" statement. This is to avoid an oracle bug using "like"
			// in PreparedStatements with a thin client
			pstmt.setString(1, district.substring(0,1)); 
			pstmt.setString(2, district.substring(1,2));
			pstmt.setString(3, district.substring(2,4));
			pstmt.setString(4, district.substring(4,5));
			pstmt.setInt(5, srMonth);
			pstmt.setInt(6, srYear);
			pstmt.setInt(7, srYear);
	*/		
			stmt = DBConn.createStatement();
			String getYTDSalesForDistrictTarget = "with se as ( select salesman_id from current_salesman_v  where sp_geog_cd like '" + district + "%' ) " +
			"select sum(case when (month <= " + srMonth + ") and (year = " + srYear + ") then u.total_" + salesOrders + " else 0 end) curr_ytd_total " +
			"from credit_customer_sales u, se, customer, products where u.salesman_id = se.salesman_id and u.credit_customer_number = customer.vista_customer_number and customer.target_account_flag = 'Y' " +
			" and products.product_id = u.product_id and products.sp_load_total = 'T' and " +
		    "products.period_yyyy = " + srYear ;
			long initTime=System.currentTimeMillis();
			SMRCLogger.debug("\n\nSQL TIMING\nDistrictDAO.getYTDSalesForDistrictTarget()\nbefore query: " + initTime+ " ms");
	//		rs = pstmt.executeQuery();
			SMRCLogger.debug("\nGoing to execute this query: " + getYTDSalesForDistrictTarget);
			rs = stmt.executeQuery(getYTDSalesForDistrictTarget);
			long afterTime=System.currentTimeMillis();
			SMRCLogger.debug("after query: " + afterTime + " ms");
			SMRCLogger.debug("difference: " + (afterTime-initTime)/1000 + " s");
		
			while (rs.next()) {
				DistrictHomePageReportBean bean = new DistrictHomePageReportBean();
				bean.setDescription("Target Accounts");
				bean.setId(district);
				bean.setCurrYTDTotal(rs.getDouble("curr_ytd_total"));
				results.add(bean);
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("DistrictDAO.getYTDSalesForDistrictTarget(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(stmt);
		}
		return results;
		
	}
	
	public static ArrayList getYTDSalesForDistrictDivisionTargets(String district, String salesOrders, int srYear, int srMonth, Connection DBConn) throws Exception {
		ResultSet rs = null;
//		PreparedStatement pstmt = null;
		Statement stmt = null;
		ArrayList results = new ArrayList();
	//	SMRCLogger.debug("\n\n>>>>>>>> SQL - DistrictDAO.getYTDSalesForDistrictDivisionTargets():\n" + getYTDOrdersForDistrictDivisionTargets + "\n? = " + district + "%\n");
		try {
	/*		if (salesOrders.equalsIgnoreCase("orders")){
				pstmt = DBConn.prepareStatement(getYTDOrdersForDistrictDivisionTargets);
			} else {
				pstmt = DBConn.prepareStatement(getYTDSalesForDistrictDivisionTargets);
			}
			// Break down the sales org, group code, zone, and district to avoid
			// using a "like" statement. This is to avoid an oracle bug using "like"
			// in PreparedStatements with a thin client
			pstmt.setString(1, district.substring(0,1)); 
			pstmt.setString(2, district.substring(1,2));
			pstmt.setString(3, district.substring(2,4));
			pstmt.setString(4, district.substring(4,5));
			pstmt.setInt(5, srMonth);
			pstmt.setInt(6, srYear);
			pstmt.setInt(7, srYear);
			*/
			String getYTDOrdersForDistrictDivisionTargets = "with se as ( select salesman_id from current_salesman_v  where sp_geog_cd like '" + district + "%' ) " +
			"select  sum(case when (month <= " + srMonth + ") and (year = " + srYear + ") then u.total_" + salesOrders + " else 0 end) curr_ytd_total " +
			"from credit_customer_sales u, se, customer, products where u.salesman_id = se.salesman_id and u.credit_customer_number = customer.vista_customer_number and customer.target_account_flag = 'Y' " +
			"and (customer.target_account_type_id = 2 or customer.target_account_type_id = 3)" + 
			" and products.product_id = u.product_id and products.sp_load_total = 'T' and " +
		    "products.period_yyyy = " + srYear ;
			stmt = DBConn.createStatement();
			long initTime=System.currentTimeMillis();
			SMRCLogger.debug("\n\nSQL TIMING\nDistrictDAO.getYTDSalesForDistrictDivisionTargets()\nbefore query: " + initTime+ " ms");
	//		rs = pstmt.executeQuery();
			SMRCLogger.debug("\nGoing to execute this query: " + getYTDOrdersForDistrictDivisionTargets);
			rs = stmt.executeQuery(getYTDOrdersForDistrictDivisionTargets);
			long afterTime=System.currentTimeMillis();
			SMRCLogger.debug("after query: " + afterTime + " ms");
			SMRCLogger.debug("difference: " + (afterTime-initTime)/1000 + " s");
			
			while (rs.next()) {
				DistrictHomePageReportBean bean = new DistrictHomePageReportBean();
				bean.setDescription("Division Target Accounts");
				bean.setId(district);
				bean.setCurrYTDTotal(rs.getDouble("curr_ytd_total"));
				results.add(bean);
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("DistrictDAO.getYTDSalesForDistrictDivisionTargets(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(stmt);
		}
		return results;
		
	}
	
	public static ArrayList getYTDSalesForDistrictSegments(String district, String salesOrders, int srYear, int srMonth, Connection DBConn) throws Exception {
		ResultSet rs = null;
//		PreparedStatement pstmt = null;
		ArrayList results = new ArrayList();
		Statement stmt = null;
		
	//	SMRCLogger.debug("\n\n>>>>>>> SQL - DistrictDAO.getYTDSalesForDistrictSegments():\n" + getYTDOrdersForDistrictSegments + "\n? = " + district + "%\n");
		
		try {
			if (salesOrders.equalsIgnoreCase("orders")){
	//			pstmt = DBConn.prepareStatement(getYTDOrdersForDistrictSegments);
				
			} else {
	//			pstmt = DBConn.prepareStatement(getYTDSalesForDistrictSegments);
			}
			// Break down the sales org, group code, zone, and district to avoid
			// using a "like" statement. This is to avoid an oracle bug using "like"
			// in PreparedStatements with a thin client
	/*		pstmt.setString(1, district.substring(0,1)); 
			pstmt.setString(2, district.substring(1,2));
			pstmt.setString(3, district.substring(2,4));
			pstmt.setString(4, district.substring(4,5));
			pstmt.setInt(5, srMonth);
			pstmt.setInt(6, srYear);
			pstmt.setInt(7, srYear);
			*/
			String theQuery = "with se as ( select salesman_id from current_salesman_v  where sp_geog_cd like '" + district + "%' )  " +
				"select segments.segment_name, segments.segment_id, sum(case when (month <= " + srMonth + ") and (year = " + srYear + ") then u.total_" + salesOrders + " else 0 end) curr_ytd_total " +
				"from credit_customer_sales u, se, customer_segments cs, segments, products where u.salesman_id = se.salesman_id " +
				"and u.credit_customer_number = cs.vista_customer_number and cs.segment_id = segments.segment_id " +
				"and segments.segment_level = '1' " + 
				" and products.product_id = u.product_id and products.sp_load_total = 'T' and " +
			    "products.period_yyyy =  " + srYear + " group by segments.segment_id, segments.segment_name";
			stmt = DBConn.createStatement();
			
			long initTime=System.currentTimeMillis();
			SMRCLogger.debug("\n\nSQL TIMING\nDistrictDAO.getYTDSalesForDistrictSegments()\n? = " + district + "%\nbefore query: " + initTime+ " ms");
//			rs = pstmt.executeQuery();
			SMRCLogger.debug("\nGoing to execute this query: " + theQuery);
			rs = stmt.executeQuery(theQuery);
			long afterTime=System.currentTimeMillis();
			SMRCLogger.debug("after query: " + afterTime + " ms");
			SMRCLogger.debug("difference: " + (afterTime-initTime)/1000 + " s");
			
			
			while (rs.next()) {
				DistrictHomePageReportBean bean = new DistrictHomePageReportBean();
				bean.setDescription(rs.getString("segment_name"));
				bean.setId(rs.getString("segment_id"));
				bean.setCurrYTDTotal(rs.getDouble("curr_ytd_total"));
				results.add(bean);
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("DistrictDAO.getYTDSalesForDistrictSegments(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(stmt);
		}
		return results;
		
	}
	
	public static double getDistrictMonthlyForecast(String district, int srYear, int srMonth, Connection DBConn) throws Exception {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		double forecast = 0;
		
		try {
			pstmt = DBConn.prepareStatement(getDistrictMonthlyForecast);
			pstmt.setString(1, district);
			pstmt.setInt(2, srYear);
			pstmt.setInt(3, srMonth);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				forecast = rs.getDouble("forecast_dollars");
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("DistrictDAO.getDistrictMonthlyForecast(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return forecast;
		
	}
	
	public static void updateMonthlyForecast(String district, double monthlyForecast, User user, int srYear, int srMonth, Connection DBConn) throws Exception {
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		boolean update = false;
		Calendar cal = Calendar.getInstance();
		java.util.Date today = new java.util.Date();
		cal.setTime(today);
		
		try {
			pstmt = DBConn.prepareStatement(getDistrictMonthlyForecast);
			pstmt.setString(1, district);
			pstmt.setInt(2, srYear);
			pstmt.setInt(3, srMonth);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				update = true;
			}
			if (update){
				pstmt = DBConn.prepareStatement(updateMonthlyForecast);
				pstmt.setDouble(1, monthlyForecast);
				pstmt.setString(2, user.getUserid());
				pstmt.setString(3, district);
				pstmt.setInt(4, srYear);
				pstmt.setInt(5, srMonth);
			} else {
				pstmt = DBConn.prepareStatement(insertMonthlyForecast);
				pstmt.setString(1, district);
				pstmt.setInt(2, srYear);
				pstmt.setInt(3, srMonth);
				pstmt.setDouble(4, monthlyForecast);
				pstmt.setString(5, user.getUserid());
				pstmt.setString(6, user.getUserid());
			}
			pstmt.executeUpdate();
			
		}catch (Exception e)	{
			SMRCLogger.error("DistrictDAO.updateMonthlyForecast(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		
	}
}
