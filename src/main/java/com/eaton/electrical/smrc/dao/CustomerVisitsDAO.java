
package com.eaton.electrical.smrc.dao;

import java.sql.*;
import java.util.*;

import javax.servlet.http.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

/**
 * @author E0062708
 *
 */
public class CustomerVisitsDAO {
	private static final String getCustomerVisitsQuery = "SELECT * FROM CUSTOMER_VISITS WHERE VISTA_CUSTOMER_NUMBER=? ORDER BY VISIT_DATE DESC, DATE_CHANGED";
	private static final String getCustomerVisitQuery = "SELECT * FROM CUSTOMER_VISITS WHERE CUSTOMER_VISIT_ID=?";
	private static final String prospectSequenceQuery = "SELECT CUSTOMER_VISITS_SEQ.NEXTVAL FROM DUAL";
	private static final String saveCustomerVisitInsert = "INSERT INTO CUSTOMER_VISITS (VISIT_DATE,NEXT_VISIT_DATE,VISIT_REASON_TYPE_ID," +
		"VISIT_OUTCOME_TYPE_ID,DESCRIPTION,VISIT_NOTES,VISTA_CUSTOMER_NUMBER,USER_ADDED,USER_CHANGED,DATE_ADDED,DATE_CHANGED,CUSTOMER_VISIT_ID)" +
		" VALUES(?,?,?,?,?,?,?,?,?,SYSDATE,SYSDATE,?)";
	private static final String saveCustomerVisitUpdate = "UPDATE CUSTOMER_VISITS SET VISIT_DATE=?,NEXT_VISIT_DATE=?," +
		"VISIT_REASON_TYPE_ID=?,VISIT_OUTCOME_TYPE_ID=?,DESCRIPTION=?,VISIT_NOTES=?,VISTA_CUSTOMER_NUMBER=?," +
		"USER_CHANGED=?,DATE_CHANGED=SYSDATE WHERE CUSTOMER_VISIT_ID=?";
	private static final String doesCustomerVisitExistQuery="SELECT COUNT(*) FROM CUSTOMER_VISITS WHERE CUSTOMER_VISIT_ID=?";
	private static final String getUsersSelect = "SELECT * FROM CUSTOMER_VISITS_EMPLOYEES WHERE CUSTOMER_VISIT_ID=?";
	private static final String setUsersDelete = "DELETE FROM CUSTOMER_VISITS_EMPLOYEES WHERE CUSTOMER_VISIT_ID=?";
	private static final String setUsersInsert="INSERT INTO CUSTOMER_VISITS_EMPLOYEES (CUSTOMER_VISIT_ID,USERID) VALUES (?,?)";
	private static final String getContactsSelect = "SELECT * FROM CUSTOMER_VISITS_CONTACTS WHERE CUSTOMER_VISIT_ID=?";
	private static final String setContactsDelete = "DELETE FROM CUSTOMER_VISITS_CONTACTS WHERE CUSTOMER_VISIT_ID=?";
	private static final String setContactsInsert = "INSERT INTO CUSTOMER_VISITS_CONTACTS (CUSTOMER_VISIT_ID,CONTACT_ID) VALUES (?,?)";
	private static final String getDistinctVisitDatesSelect = "SELECT DISTINCT VISIT_DATE FROM CUSTOMER_VISITS ORDER BY VISIT_DATE DESC";
	
	public static ArrayList getVisits(String vcn,Connection DBConn) throws Exception{
		ArrayList visits= new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DBConn.prepareStatement(getCustomerVisitsQuery);
			pstmt.setString(1, vcn);
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				CustomerVisit visit = new CustomerVisit();
				visit.setId(rs.getInt("CUSTOMER_VISIT_ID"));
				visit.setVisitDate(rs.getDate("VISIT_DATE"));
				visit.setNextVisitDate(rs.getDate("NEXT_VISIT_DATE"));
				visit.setReasonId(rs.getInt("VISIT_REASON_TYPE_ID"));
				visit.setOutcomeId(rs.getInt("VISIT_OUTCOME_TYPE_ID"));
				visit.setVcn(rs.getString("VISTA_CUSTOMER_NUMBER"));
				visit.setDescription(StringManipulation.noNull(rs.getString("DESCRIPTION")));
				visit.setNotes(StringManipulation.noNull(rs.getString("VISIT_NOTES")));
				visits.add(visit);
				
			}
		}catch (Exception e)	{
			SMRCLogger.error("CustomerVisitsDAO.getCustomerVisits(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
			
			return visits;
	}
	
	public static CustomerVisit getOneVisit(int id,Connection DBConn) throws Exception{
		CustomerVisit visit = new CustomerVisit();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DBConn.prepareStatement(getCustomerVisitQuery);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				visit.setId(rs.getInt("CUSTOMER_VISIT_ID"));
				visit.setVisitDate(rs.getDate("VISIT_DATE"));
				visit.setNextVisitDate(rs.getDate("NEXT_VISIT_DATE"));
				visit.setDateAdded(rs.getDate("DATE_ADDED"));
				visit.setReasonId(rs.getInt("VISIT_REASON_TYPE_ID"));
				visit.setOutcomeId(rs.getInt("VISIT_OUTCOME_TYPE_ID"));
				visit.setVcn(rs.getString("VISTA_CUSTOMER_NUMBER"));
				visit.setDescription(StringManipulation.noNull(rs.getString("DESCRIPTION")));
				visit.setNotes(StringManipulation.noNull(rs.getString("VISIT_NOTES")));
			}
		}catch (Exception e)	{
			SMRCLogger.error("CustomerVisitsDAO.getCustomerVisit(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);

		}
			
			return visit;
	}
	
	public static int saveVisit(CustomerVisit visit,Connection DBConn) throws Exception{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long visitNum=0;

		try {
			boolean isUpdate = visit.getId()!=0 && doesVisitExist(visit,DBConn);
			if(isUpdate){
				pstmt = DBConn.prepareStatement(saveCustomerVisitUpdate);

			}else{
				pstmt = DBConn.prepareStatement(prospectSequenceQuery);
				rs = pstmt.executeQuery();
				rs.next();
				visitNum = rs.getLong(1);
				visit.setId((int)visitNum);
				pstmt = DBConn.prepareStatement(saveCustomerVisitInsert);
			}

			int pIndex=0;

			pstmt.setDate(++pIndex, new java.sql.Date(visit.getVisitDate().getTime()));
			
			if(visit.getNextVisitDate()==null){
				pstmt.setDate(++pIndex, null);
			}else{
				pstmt.setDate(++pIndex, new java.sql.Date(visit.getNextVisitDate().getTime()));
			}
			
			pstmt.setInt(++pIndex, visit.getReasonId());
			pstmt.setInt(++pIndex, visit.getOutcomeId());
			pstmt.setString(++pIndex, visit.getDescription());
			pstmt.setString(++pIndex, visit.getNotes());
			pstmt.setString(++pIndex, visit.getVcn());
			if(!isUpdate){
				pstmt.setString(++pIndex, visit.getUserIdChanged());
			}
			pstmt.setString(++pIndex, visit.getUserIdChanged());
			pstmt.setInt(++pIndex, visit.getId());

			
			pstmt.executeUpdate();

			//setContacts();
			if(visit.getUsers()!=null){
				setUsers(visit,DBConn);
			}
			if(visit.getContacts()!=null){
				setContacts(visit,DBConn);
			}

			return visit.getId();
			
		}catch (Exception e)	{
			SMRCLogger.error("CustomerVisitsDAO.saveVisit(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);

		}
	}



	private static boolean doesVisitExist(CustomerVisit visit,Connection DBConn) throws Exception{
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DBConn.prepareStatement(doesCustomerVisitExistQuery);
			pstmt.setInt(1, visit.getId());
			rs = pstmt.executeQuery();
			int count=0;
			while (rs.next()){
				count=rs.getInt(1);
			}
			if(count==0){
				return false;
			}
			return true;
			
		}catch (Exception e)	{
			SMRCLogger.error("CustomerVisitsDAO.doesCustomerVisitExist(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);

		}
		
	}
	
	public static TreeMap getUsers(CustomerVisit visit,Connection DBConn) throws Exception{

		TreeMap users= new TreeMap();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DBConn.prepareStatement(getUsersSelect);
			pstmt.setInt(1, visit.getId());
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				users.put(rs.getString("USERID"), "x");
			}
		}catch (Exception e)	{
			SMRCLogger.error("CustomerVisitsDAO.getUsers(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
			
			return users;
	}
	private static void setUsers(CustomerVisit visit, Connection DBConn) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = DBConn.prepareStatement(setUsersDelete);
			pstmt.setInt(1,visit.getId());
			pstmt.executeUpdate();
			
			String[] userIds = visit.getUsers(); 
			
			pstmt = DBConn.prepareStatement(setUsersInsert);
			for(int i=0;i<userIds.length;i++){
				pstmt.setInt(1,visit.getId());
				pstmt.setString(2,userIds[i]);
				pstmt.executeUpdate();
			}

		}catch (Exception e)	{
			SMRCLogger.error("CustomerVisitsDAO.setUsers(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
	}

	public static TreeMap getContacts(CustomerVisit visit,Connection DBConn) throws Exception{

		TreeMap contacts= new TreeMap();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DBConn.prepareStatement(getContactsSelect);
			pstmt.setInt(1, visit.getId());
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				contacts.put(""+rs.getInt("CONTACT_ID"), "x");
			}
		}catch (Exception e)	{
			SMRCLogger.error("CustomerVisitsDAO.getContacts(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
			
			return contacts;
	}
	private static void setContacts(CustomerVisit visit, Connection DBConn) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = DBConn.prepareStatement(setContactsDelete);
			pstmt.setInt(1,visit.getId());
			pstmt.executeUpdate();
			
			String[] contactIds = visit.getContacts(); 
			
			pstmt = DBConn.prepareStatement(setContactsInsert);
			for(int i=0;i<contactIds.length;i++){
				pstmt.setInt(1,visit.getId());
				pstmt.setString(2,contactIds[i]);
				pstmt.executeUpdate();
			}

		}catch (Exception e)	{
			SMRCLogger.error("CustomerVisitsDAO.setContacts(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
	}
	
	public static ArrayList getDistinctVisitDates(Connection DBConn) throws Exception{
		ArrayList dates= new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DBConn.prepareStatement(getDistinctVisitDatesSelect);
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				dates.add(rs.getDate("VISIT_DATE"));
			}
		}catch (Exception e)	{
			SMRCLogger.error("CustomerVisitsDAO.getCustomerVisits(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
			
			return dates;
	}
	
	public static ArrayList searchVisits(HttpServletRequest request, User user, Connection DBConn) throws Exception{
		ArrayList visits= new ArrayList();
		
		Statement stmt = null;
		ResultSet rs = null;
		String[] segmentIds = request.getParameterValues("segments");
		
		
		StringBuffer queryBuffer = new StringBuffer("SELECT distinct a.*, b.customer_name, b.sp_geog, c.first_nm, c.last_nm FROM CUSTOMER_VISITS a, CUSTOMER b, CURRENT_SALESMAN_V c");
		
		if(request.getParameter("myvisits")!=null){
			queryBuffer.append(", CUSTOMER_VISITS_EMPLOYEES d");
			queryBuffer.append(" WHERE a.vista_customer_number=b.vista_customer_number AND b.sales_engineer1=c.salesman_id(+)");
			queryBuffer.append(" AND a.CUSTOMER_VISIT_ID=d.CUSTOMER_VISIT_ID(+) AND (a.user_added='" + user.getUserid() + "' OR d.USERID='" + user.getUserid() + "')");
		}else{
			if(segmentIds!=null && segmentIds.length!=0){
				queryBuffer.append(", CUSTOMER_SEGMENTS d");
			}
			
			queryBuffer.append(" WHERE a.vista_customer_number=b.vista_customer_number AND b.sales_engineer1=c.salesman_id(+)");
			
			if(segmentIds!=null && segmentIds.length!=0){
				boolean first = true;
				queryBuffer.append(" AND b.vista_customer_number=d.vista_customer_number");
			
				queryBuffer.append(" AND (");
				for(int i=0;i<segmentIds.length;i++){
					if(first){
						first=false;
					}else{
						queryBuffer.append(" OR");
					}
					queryBuffer.append(" d.segment_id=" + segmentIds[i]);
				}
				queryBuffer.append(")");
			
			}
			
			
			if(!request.getParameter("VISIT_DATE").equals("-1")){
				queryBuffer.append(" AND VISIT_DATE=TO_DATE('" + request.getParameter("VISIT_DATE") + "','YYYY-MM-DD')");
			}
			if(!request.getParameter("VISIT_DISTRICT").equals("-1")){
				String qry = getDistrictQueryString(request.getParameter("VISIT_DISTRICT"));
				queryBuffer.append(" AND sp_geog LIKE '" + qry + "'");
			}
			if(!request.getParameter("VISIT_REASON_TYPE_ID").equals("-1")){
				queryBuffer.append(" AND VISIT_REASON_TYPE_ID=" + request.getParameter("VISIT_REASON_TYPE_ID"));
			}
			if(!request.getParameter("VISIT_OUTCOME_TYPE_ID").equals("-1")){
				queryBuffer.append(" AND VISIT_OUTCOME_TYPE_ID=" + request.getParameter("VISIT_OUTCOME_TYPE_ID"));
			}

		}
			
		queryBuffer.append(" AND ROWNUM<200 ORDER BY VISIT_DATE DESC");
		
		SMRCLogger.debug(queryBuffer.toString());
		
		try {
			stmt = DBConn.createStatement();
			rs = stmt.executeQuery(queryBuffer.toString());
			
			ArrayList reasonCodes = MiscDAO.getCodes("cust_visit_reason",DBConn);
			ArrayList outcomeCodes = MiscDAO.getCodes("cust_visit_outcome",DBConn);
			
			while (rs.next())
			{
				CustomerVisit visit = new CustomerVisit();
				visit.setId(rs.getInt("CUSTOMER_VISIT_ID"));
				visit.setVisitDate(rs.getDate("VISIT_DATE"));
				visit.setNextVisitDate(rs.getDate("NEXT_VISIT_DATE"));
				visit.setDateAdded(rs.getDate("DATE_ADDED"));
				visit.setReasonId(rs.getInt("VISIT_REASON_TYPE_ID"));
				
				for(int i =0;i<reasonCodes.size();i++){
					CodeType code=(CodeType)reasonCodes.get(i);
					if(visit.getReasonId()==code.getId()){
						visit.setReason(StringManipulation.noNull(code.getName()));
						break;
					}
				}
				
				visit.setOutcomeId(rs.getInt("VISIT_OUTCOME_TYPE_ID"));
				
				for(int i =0;i<outcomeCodes.size();i++){
					CodeType code=(CodeType)outcomeCodes.get(i);
					if(visit.getOutcomeId()==code.getId()){
						visit.setOutcome(StringManipulation.noNull(code.getName()));
						break;
					}
				}
				
				visit.setVcn(rs.getString("VISTA_CUSTOMER_NUMBER"));
				visit.setDescription(StringManipulation.noNull(rs.getString("DESCRIPTION")));
				visit.setNotes(StringManipulation.noNull(rs.getString("VISIT_NOTES")));
				visit.setCustomerName(StringManipulation.noNull(rs.getString("CUSTOMER_NAME")));
				visit.setSalesEngineer(StringManipulation.noNull(rs.getString("LAST_NM")) + ", " + StringManipulation.noNull(rs.getString("FIRST_NM")));

				visits.add(visit);
			}
		}catch (Exception e)	{
			SMRCLogger.error("CustomerVisitsDAO.searchVisits(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(stmt);
		}
			
			return visits;
	}
	
	private static String getDistrictQueryString(String tst){
		String geog = "";

			if (tst.length() >= 5) {

				//if (tst.substring(5,6).equals("0")) {

					if (tst.substring(4,5).equals("0")) {

						if (tst.substring(3,4).equals("0")) {
							if (tst.substring(2,3).equals("0")) {
								if (tst.substring(1,2).equals("0")) {
									geog = tst.substring(0,1) + "%";
								}
								else {
									geog = tst.substring(0,2) + "%";
								}
							}
							else {
								geog = tst.substring(0,3) + "%";
							}
						}
						else {
							geog = tst.substring(0,4) + "%";
						}
					}
					else {
						geog = tst.substring(0,5) + "%";
					}
				//}
				//else {
				//	geog = tst + "%";
				//}
			}
			else {
				geog = tst + "%";
			}

		return geog;
	}
	
}
