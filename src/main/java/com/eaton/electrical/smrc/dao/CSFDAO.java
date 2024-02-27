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
public class CSFDAO {
	private static final String getCSFsForDistrictQuery="select division_csf.division_csf_id,"+
		"division_csf.division_id,"+
		"division_csf.division_csf,"+
		"csf_notes.csf_note,"+
		"csf_notes.sp_geog,"+
		"csf_notes.user_added as note_user_added," +
		"csf_notes.user_changed as note_user_changed," +
		"division_csf.date_added as CSF_ADDED,"+
		"division_csf.date_changed as CSF_CHANGED,"+
		"csf_notes.date_added as NOTE_ADDED,"+
		"csf_notes.date_changed as NOTE_CHANGED,"+
		"division_csf.user_added as csf_user_added,"+
		"division_csf.user_changed as csf_user_changed" + 
		" from csf_notes,"+
		"division_csf"+
		" where division_csf.division_csf_id =csf_notes.division_csf_id(+)"+
		" AND division_id=? and sp_geog(+)=? and division_csf.active_flag='Y' and rownum<5";
	private static final String getCSFsForDivisionQuery = "SELECT * FROM DIVISION_CSF WHERE DIVISION_ID=? AND ACTIVE_FLAG!='N' ORDER BY DATE_ADDED DESC";
	private static final String getCSFsForDivisionAndDistrictQuery = "SELECT DIVISION_CSF.*, csf_notes.color districtColor FROM DIVISION_CSF, csf_notes WHERE DIVISION_CSF.DIVISION_ID=? " + 
		"AND DIVISION_CSF.ACTIVE_FLAG!='N' AND DIVISION_CSF.DIVISION_CSF_ID = csf_notes.division_csf_id and csf_notes.sp_geog = ? and csf_notes.division_manager is not null ORDER BY DIVISION_CSF.DATE_ADDED DESC";
	private static final String getDistrictNotesQuery="select csf_notes.csf_note,"+
		"csf_notes.date_added,"+
		"csf_notes.date_changed,"+
		"csf_notes.active_flag,"+
		"csf_notes.division_csf_id,"+
		"csf_notes.user_added,"+
		"csf_notes.user_changed,"+
		"geographies.sp_geog,"+
		"geographies.description, color"+
		" from csf_notes,"+
		"geographies"+
		" where csf_notes.sp_geog = geographies.sp_geog"+
		" AND DIVISION_CSF_ID(+)=?"+
		" ORDER BY SP_GEOG, DATE_ADDED";
	private static final String getCSFNotesQuery="SELECT * FROM CSF_NOTES WHERE SP_GEOG=? AND DIVISION_CSF_ID=? AND ACTIVE_FLAG='Y' ORDER BY DATE_ADDED";
	private static final String newCSFInsert="INSERT INTO DIVISION_CSF (DIVISION_CSF_ID,DIVISION_ID,DIVISION_CSF,ACTIVE_FLAG,DATE_ADDED,DATE_CHANGED,USER_ADDED,USER_CHANGED)" +
		" VALUES(?,?,?,'Y',SYSDATE,SYSDATE,?,?)";
	private static final String saveNoteInsert="INSERT INTO CSF_NOTES (CSF_NOTE_ID,SP_GEOG,DIVISION_CSF_ID,DATE_ADDED,CSF_NOTE,ACTIVE_FLAG,DATE_CHANGED,USER_ADDED,USER_CHANGED)" +
		" VALUES(CSF_NOTES_SEQ.NEXTVAL,?,?,SYSDATE,?,'Y',SYSDATE,?,?)";
	private static final String changeCSFStatusUpdate="UPDATE DIVISION_CSF SET ACTIVE_FLAG=?, DATE_CHANGED=SYSDATE, USER_CHANGED=? WHERE DIVISION_CSF_ID=?";
	private static final String changeCSFNotesActiveStatus = "UPDATE CSF_NOTES SET ACTIVE_FLAG=?, DATE_CHANGED=SYSDATE, DIVISION_MANAGER_UPD_DATE=SYSDATE,DIVISION_MANAGER_UPDATE=?,USER_CHANGED=? WHERE DIVISION_CSF_ID=?";
//	private static final String changeCSFColorUpdate="UPDATE DIVISION_CSF SET color=?, DATE_CHANGED=SYSDATE, USER_CHANGED=? WHERE DIVISION_CSF_ID=?";
	private static final String changeCSFColorUpdate="UPDATE CSF_NOTES SET color=?, DATE_CHANGED=SYSDATE,USER_CHANGED=? WHERE DIVISION_CSF_ID=? and sp_geog=?";
	private static final String getCSFQuery = "SELECT * FROM DIVISION_CSF WHERE DIVISION_CSF_ID=? AND ACTIVE_FLAG!='N' ORDER BY DATE_ADDED";
	private static final String changeNoteStatusUpdate="UPDATE CSF_NOTES SET ACTIVE_FLAG=?, DATE_CHANGED=SYSDATE, USER_CHANGED=? WHERE CSF_NOTE_ID=?";
	
/*	private static final String getChargeSalesForDistrictCSF = "with targetCust as (select customer.vista_customer_number from customer " + 
		"where customer.target_account_flag = 'Y' and (customer.target_account_type_id = 2 or customer.target_account_type_id = 3) and customer.sp_geog like ?) " + 
		"select divisions.division_id, sum(case when (month <= ?) and (year = ?) then u.total_sales else 0 end) curr_ytd_total, " + 
		"sum(case when (month <= ?) and (year = (? - 1)) then u.total_sales else 0 end) prev_ytd_total from charge_end_market_sales u, targetCust, divisions, customer_divisions, products " + 
		"where targetCust.vista_customer_number = u.charge_to_customer_number and u.year >= (? - 1) and customer_divisions.division_id  = divisions.division_id " + 
		"and customer_divisions.vista_customer_number = targetCust.vista_customer_number and products.product_id = u.product_id  " +
		"and products.sp_load_total = 'T' and products.period_yyyy = ? group by divisions.division_id order by divisions.division_id";
		
	private static final String getEMSalesForDistrictCSF = "with targetCust as (select customer.vista_customer_number from customer " + 
	"where customer.target_account_flag = 'Y' and (customer.target_account_type_id = 2 or customer.target_account_type_id = 3) and customer.sp_geog like ?) " +
*/	 
//	"select /*+ ORDERED */ divisions.division_id, sum(case when (month <= ?) and (year = ?) then u.total_sales else 0 end) curr_ytd_total, " + 
/*
	"sum(case when (month <= ?) and (year = (? - 1)) then u.total_sales else 0 end) prev_ytd_total from targetCust, charge_end_market_sales u, divisions, customer_divisions, products " +
	"where targetCust.vista_customer_number = u.end_market_customer_number and u.year >= (? - 1) and customer_divisions.division_id  = divisions.division_id " + 
	"and customer_divisions.vista_customer_number = targetCust.vista_customer_number and products.product_id = u.product_id  " +
	"and products.sp_load_total = 'T' and products.period_yyyy = ? group by divisions.division_id order by divisions.division_id";

	
	private static final String getChargeOrdersForDistrictCSF = "with targetCust as (select customer.vista_customer_number from customer " + 
		"where customer.target_account_flag = 'Y' and (customer.target_account_type_id = 2 or customer.target_account_type_id = 3) and customer.sp_geog like ?) " + 
		"select divisions.division_id, sum(case when (month <= ?) and (year = ?) then u.total_orders else 0 end) curr_ytd_total, " + 
		"sum(case when (month <= ?) and (year = (? - 1)) then u.total_orders else 0 end) prev_ytd_total from charge_end_market_sales u, targetCust, divisions, customer_divisions, products " + 
		"where targetCust.vista_customer_number = u.charge_to_customer_number and u.year >= (? - 1) and customer_divisions.division_id  = divisions.division_id " + 
		"and customer_divisions.vista_customer_number = targetCust.vista_customer_number and products.product_id = u.product_id  " +
		"and products.sp_load_total = 'T' and products.period_yyyy = ? group by divisions.division_id order by divisions.division_id";
	private static final String getEMOrdersForDistrictCSF = "with targetCust as (select customer.vista_customer_number from customer " + 
	"where customer.target_account_flag = 'Y' and (customer.target_account_type_id = 2 or customer.target_account_type_id = 3) and customer.sp_geog like ?) " + 
*/
//	"select /*+ ORDERED */ divisions.division_id, sum(case when (month <= ?) and (year = ?) then u.total_orders else 0 end) curr_ytd_total, " + 
/*	"sum(case when (month <= ?) and (year = (? - 1)) then u.total_orders else 0 end) prev_ytd_total from targetCust, charge_end_market_sales u, divisions, customer_divisions, products " + 
	"where targetCust.vista_customer_number = u.end_market_customer_number and u.year >= (? - 1) and customer_divisions.division_id  = divisions.division_id " + 
	"and customer_divisions.vista_customer_number = targetCust.vista_customer_number and products.product_id = u.product_id  " +
	"and products.sp_load_total = 'T' and products.period_yyyy = ? group by divisions.division_id order by divisions.division_id";

	
	private static final String getEMOrdersForDivisionCSF = "with targetCust as ( select customer.vista_customer_number, customer.sp_geog from customer " + 
	" where customer.target_account_flag = 'Y' and (customer.target_account_type_id = 2 or customer.target_account_type_id = 3) and customer.sp_geog like '145%' " +
*/	
//	" ) select /*+ ORDERED */ substr(geographies.sp_geog,1,5) district, sum(case when (u.month <= ?) and (u.year = ?) then u.total_orders else 0 end) curr_ytd_total, " + 
/*	" sum(case when (u.month <= ?) and (u.year = (? - 1)) then u.total_orders else 0 end) prev_ytd_total from targetCust, charge_end_market_sales u, customer_divisions, geographies, products " + 
	" where targetCust.vista_customer_number = u.end_market_customer_number and u.year >= (? - 1) and customer_divisions.vista_customer_number = targetCust.vista_customer_number " + 
	" and targetCust.sp_geog = geographies.sp_geog and customer_divisions.division_id = ? and products.product_id = u.product_id " + 
	" and products.sp_load_total = 'T' and products.period_yyyy = ? group by substr(geographies.sp_geog,1,5)"; 

	private static final String getChargeOrdersForDivisionCSF = "with targetCust as ( select customer.vista_customer_number, customer.sp_geog from customer " + 
	" where customer.target_account_flag = 'Y' and (customer.target_account_type_id = 2 or customer.target_account_type_id = 3) and customer.sp_geog like '145%' " +
	" ) select substr(geographies.sp_geog,1,5) district, sum(case when (u.month <= ?) and (u.year = ?) then u.total_orders else 0 end) curr_ytd_total, " + 
	" sum(case when (u.month <= ?) and (u.year = (? - 1)) then u.total_orders else 0 end) prev_ytd_total from charge_end_market_sales u, targetCust, customer_divisions, geographies, products " + 
	" where targetCust.vista_customer_number = u.charge_to_customer_number and u.year >= (? - 1) and customer_divisions.vista_customer_number = targetCust.vista_customer_number " + 
	" and targetCust.sp_geog = geographies.sp_geog and customer_divisions.division_id = ? and products.product_id = u.product_id " + 
	" and products.sp_load_total = 'T' and products.period_yyyy = ? group by substr(geographies.sp_geog,1,5)"; 


	private static final String getEMSalesForDivisionCSF = "with targetCust as ( select customer.vista_customer_number, customer.sp_geog from customer " + 
		" where customer.target_account_flag = 'Y' and (customer.target_account_type_id = 2 or customer.target_account_type_id = 3) and customer.sp_geog like '145%' " +
*/
//		" ) select /*+ ORDERED */ substr(geographies.sp_geog,1,5) district, sum(case when (u.month <= ?) and (u.year = ?) then u.total_sales else 0 end) curr_ytd_total, " + 
/*
		" sum(case when (u.month <= ?) and (u.year = (? - 1)) then u.total_sales else 0 end) prev_ytd_total from targetCust, charge_end_market_sales u, customer_divisions, geographies, products  " + 
		" where targetCust.vista_customer_number = u.end_market_customer_number and u.year >= (? - 1) and customer_divisions.vista_customer_number = targetCust.vista_customer_number " + 
		" and targetCust.sp_geog = geographies.sp_geog and customer_divisions.division_id = ? and products.product_id = u.product_id " + 
		" and products.sp_load_total = 'T' and products.period_yyyy = ? group by substr(geographies.sp_geog,1,5)"; 
*/
	private static final String getTapDollarsOrdersForDivisionCSF = "with targetCust as ( select customer.vista_customer_number, customer.sp_geog from customer " + 
	" where customer.target_account_flag = 'Y' and (customer.target_account_type_id = 2 or customer.target_account_type_id = 3) and customer.sp_geog like '145%' " +
	" ) select substr(geographies.sp_geog,1,5) district, sum(case when (tap.month <= ?) and (tap.year = ?) then tap.order_tap_dollars else 0 end) curr_ytd_total, " + 
	" sum(case when (tap.month <= ?) and (tap.year = (? - 1)) then tap.order_tap_dollars else 0 end) prev_ytd_total from tap_dollars tap, targetCust, customer_divisions, geographies, products " + 
	" where targetCust.vista_customer_number = tap.vista_customer_number and tap.year >= (? - 1) and customer_divisions.vista_customer_number = targetCust.vista_customer_number " + 
	" and targetCust.sp_geog = geographies.sp_geog and customer_divisions.division_id = ? and products.product_id = tap.product_id " + 
	" and products.sp_load_total = 'T' and products.period_yyyy = ? group by substr(geographies.sp_geog,1,5)"; 

	private static final String getTapDollarsInvoiceForDivisionCSF = "with targetCust as ( select customer.vista_customer_number, customer.sp_geog from customer " + 
	" where customer.target_account_flag = 'Y' and (customer.target_account_type_id = 2 or customer.target_account_type_id = 3) and customer.sp_geog like '145%' " +
	" ) select substr(geographies.sp_geog,1,5) district, sum(case when (tap.month <= ?) and (tap.year = ?) then tap.invoice_tap_dollars else 0 end) curr_ytd_total, " + 
	" sum(case when (tap.month <= ?) and (tap.year = (? - 1)) then tap.invoice_tap_dollars else 0 end) prev_ytd_total from tap_dollars tap, targetCust, customer_divisions, geographies, products " + 
	" where targetCust.vista_customer_number = tap.vista_customer_number and tap.year >= (? - 1) and customer_divisions.vista_customer_number = targetCust.vista_customer_number " + 
	" and targetCust.sp_geog = geographies.sp_geog and customer_divisions.division_id = ? and products.product_id = tap.product_id " + 
	" and products.sp_load_total = 'T' and products.period_yyyy = ? group by substr(geographies.sp_geog,1,5)"; 

	private static final String getTapDollarsInvoiceForDistrictCSF = "with targetCust as (select customer.vista_customer_number from customer " + 
	"where customer.target_account_flag = 'Y' and (customer.target_account_type_id = 2 or customer.target_account_type_id = 3) and customer.sp_geog like ?) " + 
	"select divisions.division_id, sum(case when (month <= ?) and (year = ?) then tap.invoice_tap_dollars else 0 end) curr_ytd_total, " + 
	"sum(case when (month <= ?) and (year = (? - 1)) then tap.invoice_tap_dollars else 0 end) prev_ytd_total from tap_dollars tap, targetCust, divisions, customer_divisions, products " + 
	"where targetCust.vista_customer_number = tap.vista_customer_number and tap.year >= (? - 1) and customer_divisions.division_id  = divisions.division_id " + 
	"and customer_divisions.vista_customer_number = targetCust.vista_customer_number and products.product_id = tap.product_id  " +
	"and products.sp_load_total = 'T' and products.period_yyyy = ? group by divisions.division_id order by divisions.division_id";
	
	private static final String getTapDollarsOrdersForDistrictCSF = "with targetCust as (select customer.vista_customer_number from customer " + 
	"where customer.target_account_flag = 'Y' and (customer.target_account_type_id = 2 or customer.target_account_type_id = 3) and customer.sp_geog like ?) " + 
	"select divisions.division_id, sum(case when (month <= ?) and (year = ?) then tap.order_tap_dollars else 0 end) curr_ytd_total, " + 
	"sum(case when (month <= ?) and (year = (? - 1)) then tap.order_tap_dollars else 0 end) prev_ytd_total from tap_dollars tap, targetCust, divisions, customer_divisions, products " + 
	"where targetCust.vista_customer_number = tap.vista_customer_number and tap.year >= (? - 1) and customer_divisions.division_id  = divisions.division_id " + 
	"and customer_divisions.vista_customer_number = targetCust.vista_customer_number and products.product_id = tap.product_id  " +
	"and products.sp_load_total = 'T' and products.period_yyyy = ? group by divisions.division_id order by divisions.division_id";
	
	private static final String getAllCSFdistrictsQuery = "select geographies.sp_geog, (zone || '-' || district || ' ' || description) label from geographies where geographies.sp_geog like '145%' " +
			" and geographies.team is null and geographies.district != '0' order by geographies.sp_geog";
	
	private static final String getCSFCountforGeogAndDivisionQuery = "select count(*) from  (  select csf_notes.division_csf_id, csf_notes.sp_geog " +
			"from division_csf, csf_notes where division_csf.active_flag = 'Y' and division_csf.division_id = ?  and csf_notes.division_csf_id = division_csf.division_csf_id " +
			"group by csf_notes.sp_geog, csf_notes.division_csf_id  ) a where a.sp_geog = ?";
	
	private static final String insertDistrictInCSFNotes = "insert into csf_notes (sp_geog,division_csf_id,user_added,date_added,active_flag,division_manager_update,division_manager_upd_date,division_manager,csf_note_id) " +
			"values (?,?,?,sysdate,'Y',?,sysdate,?,csf_notes_seq.nextval)";
	
	private static final String getDivisionCSFNextValQuery = "select division_csf_seq.nextval from dual";
	
	public static ArrayList getCSFsForDistrict(String divisionId, String districtId, Connection DBConn) throws Exception{
		ArrayList csfs= new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getCSFsForDistrictQuery);
			pstmt.setInt(1, Globals.a2int(divisionId));
			pstmt.setString(2, districtId);
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				CriticalSuccessFactor csf = new CriticalSuccessFactor();
				csf.setId(""+rs.getInt("DIVISION_CSF_ID"));
				csf.setName(rs.getString("DIVISION_CSF"));
				csf.setDateAdded(rs.getDate("CSF_ADDED"));
				csf.setUserAdded(StringManipulation.noNull(rs.getString("csf_user_added")));
				csf.setUserChanged(StringManipulation.noNull(rs.getString("csf_user_changed")));
				csf.setUserAddedName(UserDAO.getUserName(StringManipulation.noNull(rs.getString("csf_user_added")),DBConn));
				csf.setUserChangedName(UserDAO.getUserName(StringManipulation.noNull(rs.getString("csf_user_changed")),DBConn));
				csfs.add(csf);
				
			}
		}catch (Exception e)	{
			SMRCLogger.error("CSFDAO.getCSFsForDistrict(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return csfs;
		
	}	
	
	
	
	
	public static ArrayList getCSFsForDivision(String division, Connection DBConn) throws Exception{
		
		ArrayList csfs= new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getCSFsForDivisionQuery);
			pstmt.setInt(1, Globals.a2int(division));
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				CriticalSuccessFactor csf = new CriticalSuccessFactor();
				csf.setId(""+rs.getInt("DIVISION_CSF_ID"));
				csf.setName(StringManipulation.noNull(rs.getString("DIVISION_CSF")));
				csf.setColor(StringManipulation.noNull(rs.getString("COLOR")));
				csf.setDateAdded(rs.getDate("DATE_ADDED"));
				csf.setDistrictsWithNotes(getDistrictNotes(csf.getId(), DBConn));
				csf.setUserAdded(StringManipulation.noNull(rs.getString("user_added")));
				csf.setUserChanged(StringManipulation.noNull(rs.getString("user_changed")));
				csf.setUserAddedName(UserDAO.getUserName(StringManipulation.noNull(rs.getString("user_added")),DBConn));
				csf.setUserChangedName(UserDAO.getUserName(StringManipulation.noNull(rs.getString("user_changed")),DBConn));
				csf.setDateChanged(rs.getDate("date_changed"));
				csfs.add(csf);
			}
		}catch (Exception e)	{
			SMRCLogger.error("CSFDAO.getCSFsForDivision(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
			
		}
		return csfs;
		
	}	
	
	public static ArrayList getCSFsForDivisionAndDistrict(String division, String district, Connection DBConn) throws Exception{
		
		ArrayList csfs= new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getCSFsForDivisionAndDistrictQuery);
			pstmt.setInt(1, Globals.a2int(division));
			pstmt.setString(2, district);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				CriticalSuccessFactor csf = new CriticalSuccessFactor();
				csf.setId(""+rs.getInt("DIVISION_CSF_ID"));
				csf.setName(StringManipulation.noNull(rs.getString("DIVISION_CSF")));
				csf.setColor(StringManipulation.noNull(rs.getString("districtColor")));
				csf.setDateAdded(rs.getDate("DATE_ADDED"));
				csf.setDistrictsWithNotes(getDistrictNotes(csf.getId(), DBConn));
				csf.setUserAdded(StringManipulation.noNull(rs.getString("user_added")));
				csf.setUserChanged(StringManipulation.noNull(rs.getString("user_changed")));
				csf.setUserAddedName(UserDAO.getUserName(StringManipulation.noNull(rs.getString("user_added")),DBConn));
				csf.setUserChangedName(UserDAO.getUserName(StringManipulation.noNull(rs.getString("user_changed")),DBConn));
				csf.setDateChanged(rs.getDate("date_changed"));
				csfs.add(csf);
			}
		}catch (Exception e)	{
			SMRCLogger.error("CSFDAO.getCSFsForDivision(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
			
		}
		return csfs;
		
	}
	
	public static ArrayList getDistrictNotes(String csfId, Connection DBConn) throws Exception{
		ArrayList csfNotes = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getDistrictNotesQuery);
			pstmt.setInt(1, Globals.a2int(csfId));
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				CriticalSuccessFactorNote note = new CriticalSuccessFactorNote();
				note.setGeog(StringManipulation.noNull(rs.getString("SP_GEOG")));
				note.setGeogDescription(StringManipulation.noNull(rs.getString("DESCRIPTION")));
				note.setNote(StringManipulation.noNull(rs.getString("CSF_NOTE")));
				note.setDateChanged(rs.getDate("DATE_CHANGED"));
				note.setDateAdded(rs.getDate("DATE_ADDED"));
				note.setUserAdded(StringManipulation.noNull(rs.getString("user_added")));
				note.setUserChanged(StringManipulation.noNull(rs.getString("user_changed")));
				note.setUserAddedName(UserDAO.getUserName(StringManipulation.noNull(rs.getString("user_added")),DBConn));
				note.setUserChangedName(UserDAO.getUserName(StringManipulation.noNull(rs.getString("user_changed")),DBConn));
				note.setColor(StringManipulation.noNull(rs.getString("color")));
				if(StringManipulation.noNull(rs.getString("ACTIVE_FLAG")).equals("Y")){
					note.setActive(true);
				}
				csfNotes.add(note);
				
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("CSFDAO.getDistrictNotes(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
			
		}
		
		return csfNotes;
		
	}
	
	
	
	public static ArrayList getCSFNotes(String csfId, String spGeog, Connection DBConn) throws Exception{
		
		ArrayList csfNotes = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getCSFNotesQuery);
			pstmt.setString(1, spGeog);
			pstmt.setInt(2, Globals.a2int(csfId));
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				CriticalSuccessFactorNote csfNote = new CriticalSuccessFactorNote();
				csfNote.setId(rs.getInt("CSF_NOTE_ID"));
				csfNote.setNote(StringManipulation.noNull(rs.getString("CSF_NOTE")));
				csfNote.setDateAdded(rs.getDate("DATE_ADDED"));
				csfNote.setDateChanged(rs.getDate("DATE_CHANGED"));
				csfNote.setUserAdded(StringManipulation.noNull(rs.getString("user_added")));
				csfNote.setUserChanged(StringManipulation.noNull(rs.getString("user_changed")));
				csfNote.setUserAddedName(UserDAO.getUserName(StringManipulation.noNull(rs.getString("user_added")),DBConn));
				csfNote.setUserChangedName(UserDAO.getUserName(StringManipulation.noNull(rs.getString("user_changed")),DBConn));
				csfNotes.add(csfNote);
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("CSFDAO.getCSFNotes(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return csfNotes;
		
	}
	
	// This methods returns the nextval it used for the id so it can be used in creating
	// the csf_notes records
	public static int newCSF(String divId, String csf, String userid, Connection DBConn) throws Exception{
		
		PreparedStatement pstmt = null;
		int nextval = 0;
		
		try {
		    nextval = getDivisionCSFNextVal(DBConn);
			pstmt = DBConn.prepareStatement(newCSFInsert);
			pstmt.setInt(1, nextval);
			pstmt.setInt(2, Globals.a2int(divId));
			pstmt.setString(3, csf);
			pstmt.setString(4,userid);
			pstmt.setString(5,userid);
			pstmt.executeUpdate();
			
			
		}catch (Exception e)	{
			SMRCLogger.error("CSFDAO.newCSF(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return nextval;
		
	}
	
	public static void saveNote(String district, String csfid, String note, String userid, Connection DBConn) throws Exception{
		
		PreparedStatement pstmt = null;
		
		try {
			pstmt = DBConn.prepareStatement(saveNoteInsert);
			pstmt.setString(1, district);
			pstmt.setInt(2, Globals.a2int(csfid));
			pstmt.setString(3, note);
			pstmt.setString(4, userid);
			pstmt.setString(5, userid);
			pstmt.executeUpdate();
		}catch (Exception e)	{
			SMRCLogger.error("CSFDAO.saveNote(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
	}	
	
	public static void changeCSFStatus(String csfID, String status, String userid, Connection DBConn) throws Exception{
		PreparedStatement pstmt = null;
		
		try {
			pstmt = DBConn.prepareStatement(changeCSFStatusUpdate);
			pstmt.setString(1, status);
			pstmt.setString(2, userid);
			pstmt.setInt(3, Globals.a2int(csfID));
			pstmt.executeUpdate();
			// Update all csf_notes records for this csf also
			pstmt = DBConn.prepareStatement(changeCSFNotesActiveStatus);
			pstmt.setString(1,status);
			pstmt.setString(2,userid);
			pstmt.setString(3,userid);
			pstmt.setInt(4,Globals.a2int(csfID));
			pstmt.executeUpdate();
			
		}catch (Exception e)	{
			SMRCLogger.error("CSFDAO.changeCSFStatus(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
	}
	
	public static void changeCSFColor(String csfId, String district, String color, String userid, Connection DBConn) throws Exception{

		PreparedStatement pstmt = null;
				
		try {
			pstmt = DBConn.prepareStatement(changeCSFColorUpdate);
			pstmt.setString(1, color);
			pstmt.setString(2, userid);
			pstmt.setInt(3, Globals.a2int(csfId));
			pstmt.setString(4,district);
			SMRCLogger.debug("SQL - changeCSFColor: " + changeCSFColorUpdate + "\ncolor= " + color + "\nuserid=" + userid + "\ncsfId=" + csfId + "\ndistrict=" + district);
			pstmt.executeUpdate();
		}
		catch (Exception e)	{
			SMRCLogger.error("CSFDAO.changeCSFColor(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
	}
	
	public static CriticalSuccessFactor getCSF(String csfId, String district, Connection DBConn) throws Exception{
		
		CriticalSuccessFactor csf = new CriticalSuccessFactor();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getCSFQuery);
			pstmt.setInt(1, Globals.a2int(csfId));
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				csf.setId(""+rs.getInt("DIVISION_CSF_ID"));
				csf.setName(rs.getString("DIVISION_CSF"));
				csf.setDateAdded(rs.getDate("DATE_ADDED"));
				csf.setColor(StringManipulation.noNull(rs.getString("COLOR")));
				csf.setMultipleNotes(getCSFNotes(csfId,district, DBConn));
				csf.setDateChanged(rs.getDate("DATE_CHANGED"));
				csf.setUserAdded(StringManipulation.noNull(rs.getString("user_added")));
				csf.setUserChanged(StringManipulation.noNull(rs.getString("user_changed")));
				csf.setUserAddedName(UserDAO.getUserName(StringManipulation.noNull(rs.getString("user_added")),DBConn));
				csf.setUserChangedName(UserDAO.getUserName(StringManipulation.noNull(rs.getString("user_changed")),DBConn));
			}
			
		}
		catch (Exception e)	{
			SMRCLogger.error("CSFDAO.getCSF(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return csf;
		
	}	
	
	public static void changeNoteStatus(String csfNoteID, String status, String userid, Connection DBConn) throws Exception{
		
		PreparedStatement pstmt = null;
		
		try {
			pstmt = DBConn.prepareStatement(changeNoteStatusUpdate);
			pstmt.setString(1, status);
			pstmt.setString(2, userid);
			pstmt.setInt(2, Globals.a2int(csfNoteID));
			pstmt.executeUpdate();
		}
		catch (Exception e)	{
			SMRCLogger.error("CSFDAO.changeNoteStatus(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
	}
	
	public static ArrayList getSalesForDistrictCSF(String district, String salesOrders, int srYear, int srMonth, User user, Connection DBConn) throws Exception{
		
		ArrayList results = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
				
		try {
//		    if (user.isUseDirect()){
			if (salesOrders.equalsIgnoreCase("invoice")){
				if (SMRCLogger.isDebuggerEnabled()) {
					SMRCLogger.debug("SQL: CSFDAO.getSalesForDistrictCSF():\n" + getTapDollarsInvoiceForDistrictCSF + "\n? = " + district + "%");
				}
				pstmt = DBConn.prepareStatement(getTapDollarsInvoiceForDistrictCSF);
			} else {
				if (SMRCLogger.isDebuggerEnabled()) {
					SMRCLogger.debug("SQL: CSFDAO.getSalesForDistrictCSF():\n" + getTapDollarsOrdersForDistrictCSF + "\n? = " + district + "%");
				}
				pstmt = DBConn.prepareStatement(getTapDollarsOrdersForDistrictCSF);
			}
/*		    } else {
		        if (salesOrders.equalsIgnoreCase("sales")){
					if (SMRCLogger.isDebuggerEnabled()) {
						SMRCLogger.debug("SQL: CSFDAO.getSalesForDistrictCSF():\n" + getEMSalesForDistrictCSF + "\n? = " + district + "%");
					}
					pstmt = DBConn.prepareStatement(getEMSalesForDistrictCSF);
				} else {
					if (SMRCLogger.isDebuggerEnabled()) {
						SMRCLogger.debug("SQL: CSFDAO.getSalesForDistrictCSF():\n" + getEMOrdersForDistrictCSF + "\n? = " + district + "%");
					}
					pstmt = DBConn.prepareStatement(getEMOrdersForDistrictCSF);
				}
		    }
*/
			pstmt.setString(1, district + "%");
			pstmt.setInt(2, srMonth);
			pstmt.setInt(3, srYear);
			pstmt.setInt(4, srMonth);
			pstmt.setInt(5, srYear);
			pstmt.setInt(6, srYear);
			pstmt.setInt(7, srYear);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				CSFReportBean bean = new CSFReportBean();
				bean.setId(rs.getString("Division_id"));
				bean.setYTDSales(rs.getDouble("curr_ytd_total"));
				bean.setPrevYTDSales(rs.getDouble("prev_ytd_total"));
				results.add(bean);
			}
			
		}
		catch (Exception e)	{
			SMRCLogger.error("CSFDAO.getSalesForDistrictCSF(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return results;
		
	}	
	
	public static ArrayList getSalesForDivisionCSF(int divisionId, String salesOrders, int srYear, int srMonth, User user, Connection DBConn) throws Exception{
		
		ArrayList results = new ArrayList();
		
		PreparedStatement pstmt = null;
	//	Statement pstmt = null;
		ResultSet rs = null;
		
		try {
		  //  pstmt = DBConn.createStatement();
		    
//		    if (user.isUseDirect()){
			
			if (salesOrders.equalsIgnoreCase("invoice")){
				SMRCLogger.debug("SQL: CSFDAO.getSalesForDivisionCSF():\n" + getTapDollarsInvoiceForDivisionCSF + "\n? = " + divisionId);
				pstmt = DBConn.prepareStatement(getTapDollarsInvoiceForDivisionCSF);
			} else {
				SMRCLogger.debug("SQL: CSFDAO.getSalesForDivisionCSF():\n" + getTapDollarsOrdersForDivisionCSF + "\n? = " + divisionId);
				pstmt = DBConn.prepareStatement(getTapDollarsOrdersForDivisionCSF);	
			}
			
			
			
/*		    } else {
		        if (salesOrders.equalsIgnoreCase("sales")){
					SMRCLogger.debug("SQL: CSFDAO.getSalesForDivisionCSF():\n" + getEMSalesForDivisionCSF + "\n? = " + divisionId);
					pstmt = DBConn.prepareStatement(getEMSalesForDivisionCSF);
				} else {
					SMRCLogger.debug("SQL: CSFDAO.getSalesForDivisionCSF():\n" + getEMOrdersForDivisionCSF + "\n? = " + divisionId);
					pstmt = DBConn.prepareStatement(getEMOrdersForDivisionCSF);
				}
		    }
*/			
			pstmt.setInt(1, srMonth);
			pstmt.setInt(2, srYear);
			pstmt.setInt(3, srMonth);
			pstmt.setInt(4, srYear);
			pstmt.setInt(5, srYear);
			pstmt.setInt(6, divisionId);
			pstmt.setInt(7, srYear);
			
		    /*
		    String sql = "with targetCust as ( select customer.vista_customer_number, customer.sp_geog from customer " + 
		" where customer.target_account_flag = 'Y' and (customer.target_account_type_id = 2 or customer.target_account_type_id = 3) and customer.sp_geog like '145%' " +
		" ) select substr(geographies.sp_geog,1,5) district, sum(case when (u.month <= " + srMonth + ") and (u.year = " + srYear + ") then u.total_sales else 0 end) curr_ytd_total, " + 
		" sum(case when (u.month <= " + srMonth + ") and (u.year = (" + srYear + " - 1)) then u.total_sales else 0 end) prev_ytd_total from charge_end_market_sales u, targetCust, customer_divisions, geographies, products " + 
		" where targetCust.vista_customer_number = u.end_market_customer_number and u.year >= (" + srYear + " - 1) and customer_divisions.vista_customer_number = targetCust.vista_customer_number " + 
		" and targetCust.sp_geog = geographies.sp_geog and customer_divisions.division_id = " + divisionId + " and products.product_id = u.product_id " + 
		" and products.sp_load_total = 'T' and products.period_yyyy = " + srYear + " group by substr(geographies.sp_geog,1,5)"; 
			*/
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				CSFReportBean bean = new CSFReportBean();
				bean.setId(rs.getString("district")); 
				bean.setYTDSales(rs.getDouble("curr_ytd_total"));
				bean.setPrevYTDSales(rs.getDouble("prev_ytd_total"));
				results.add(bean);
			}
			
		}
		catch (Exception e)	{
			SMRCLogger.error("CSFDAO.getSalesForDivisionCSF(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return results;
		
	}	
	
	// This method returns an ArrayList of districts that can been assigned a new CSF (meaning
	// they have less than 4 currently) in alphabetical order in DropDownBean objects
	// "All" is the first district returned if no districts are disqualified by the limit of 4 CSFs
	public static ArrayList getAvailableDistrictsForDivisionCSF(int divisionId, Connection DBConn) throws Exception{
		
		ArrayList districts = new ArrayList();
		
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		
		try {
		    // This query returns all districts with a sales_org of 1
		    pstmt1 = DBConn.prepareStatement(getAllCSFdistrictsQuery);
		    pstmt2 = DBConn.prepareStatement(getCSFCountforGeogAndDivisionQuery);
		    pstmt2.setInt(1,divisionId);
		    rs1 = pstmt1.executeQuery();
		    boolean allDistricts = true;
		    while (rs1.next()){
		        String district = rs1.getString("sp_geog");
		        String label = rs1.getString("label");
		        pstmt2.setString(2,district);
		        rs2 = pstmt2.executeQuery();
		        if (rs2.next()){
		            int recCount = rs2.getInt(1);
		            if (recCount < 4){
		                DropDownBean bean = new DropDownBean();
		                bean.setName(label);
		                bean.setValue(district);
		                districts.add(bean);
		            } else {
		                allDistricts = false;
		            }
		        }
		        
		    }
		    if (allDistricts){
		        DropDownBean allBean = new DropDownBean();
		        allBean.setName("All");
		        allBean.setValue("All");
		        districts.add(0,allBean);
		    }
			
		}
		catch (Exception e)	{
			SMRCLogger.error("CSFDAO.getAvailableDistrictsForDivisionCSF(): ", e);
			throw e;
		}
		finally {
		    SMRCConnectionPoolUtils.close(rs2);
			SMRCConnectionPoolUtils.close(pstmt2);
			SMRCConnectionPoolUtils.close(rs1);
			SMRCConnectionPoolUtils.close(pstmt1);
		}
		
		return districts;
		
	}	
	
	// This method inserts a record into CSF_NOTES to assign the csf to the specified district
	public static void addDistrictToCSFNotes(String district, int divCSFId, String userid, Connection DBConn) throws Exception{
		
		PreparedStatement pstmt = null;
		
		try {
			pstmt = DBConn.prepareStatement(insertDistrictInCSFNotes);
			pstmt.setString(1, district);
			pstmt.setInt(2, divCSFId);
			pstmt.setString(3, userid);
			pstmt.setString(4, userid);
			pstmt.setString(5, userid);
			SMRCLogger.debug("SQL: CSFDAO.addDistrictToCSFNotes():\n" + insertDistrictInCSFNotes + "\ndistrict = " + district + "\ndivCSFId= " + divCSFId + "\nuserid= " + userid);
			pstmt.executeUpdate();
		}catch (Exception e)	{
			SMRCLogger.error("CSFDAO.addDistrictToCSFNotes(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
	}	
	
	public static int getDivisionCSFNextVal(Connection DBConn) throws Exception{
		
		int nextval = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getDivisionCSFNextValQuery);
			rs = pstmt.executeQuery();
			
			if (rs.next())
			{
				nextval = rs.getInt(1);
			}
		}catch (Exception e)	{
			SMRCLogger.error("CSFDAO.getDivisionCSFNextVal(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return nextval;
	}
	
	
}
