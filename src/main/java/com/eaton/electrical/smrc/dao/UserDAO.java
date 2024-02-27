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
public class UserDAO {
	
	private static final String getUserQry = "SELECT * FROM users, user_groups, current_salesman_v, user_groups_users_xref ugx WHERE current_salesman_v.user_id(+)=users.vistaline_id AND user_groups.user_group_id(+) = users.user_group_id AND lower(users.userid) = ? and ugx.userid (+) = users.userid";
	private static final String getSalesIdsQuery = "SELECT SALESMAN_ID FROM CURRENT_SALESMAN_V WHERE lower(USER_ID) = ?";
	private static final String getUserQry2 = "select * from user_geog_security where userid = ?";
	private static final String getUserQry3 = "select * from user_segments where userid = ?";
	//private static final String getAllUsersQuery = "SELECT * FROM users, user_groups, current_salesman_v WHERE current_salesman_v.user_id(+)=users.vistaline_id AND user_groups.user_group_id(+) = users.user_group_id order by last_name, first_name";
	private static final String getAllUsersTreeMapQuery = "SELECT userid, first_name, last_name from users where first_name is not null and last_name is not null order by last_name, first_name";
	private static final String getUserByVistalineId = "select * from users where vistaline_id = ?";
	private static final String getUserBySalesId = "select users.* from oemapnew.current_salesman_v t, oemapnew.users where t.user_id = users.vistaline_id and t.salesman_id = ?";
	private static final String getUsersInGroupQuery = "SELECT * FROM users, user_groups, current_salesman_v, user_groups_users_xref ugx WHERE current_salesman_v.user_id(+)=users.vistaline_id AND user_groups.user_group_id(+) = users.user_group_id and user_groups.user_group_name=? and ugx.userid (+) = users.userid order by last_name, first_name";
    private static final String getUserGroupsCodeByTitle = "select * from user_groups where user_group_name = ?";
    private static final String getUseridsByUserGroupCode = "select userid from users where user_group_id = ?";
    private static final String getProjectSalesManagerForGeog = "select users.userid from users, user_groups_users_xref ugx where users.user_group_id = ? and ugx.userid = users.userid and ugx.sp_geog = ?";
    private static final String getDivisionManagerForDivision = "select users.userid from users, user_groups_users_xref ugx, divisions where users.user_group_id = ? and ugx.userid = users.userid and ugx.division_id = divisions.division_id and divisions.division_description = ?";
    
    //private static final String getUsersQuery = "SELECT * FROM users ORDER BY last_name ASC, first_name ASC";

    private static final String userExistsSelect = "SELECT count(*) FROM users WHERE userid = ?";
    private static final String createUserInsert = "insert into users " +
	"(userid,last_name,first_name,vistaline_id,email_address,hide_from_assignments," +
	"override_security,set_security,view_everything,date_added,date_changed) " +
	"values (?,?,?,?,?,'N','N','N','N',sysdate,sysdate)";
    private static final String updateUserUpdate = "update users set first_name = ?, last_name = ?, email_address = ?, vistaline_id = ?, date_changed = sysdate where userid = ?";
    private static final String updateVistaSecurityRecordsUpdate = "update users set override_security = 'Y' where userid = ?";
    private static final String updateVistaSecurityRecordsDelete = "delete from user_geog_security where security_type = 'V' and userid = ?";
    private static final String updateVistaSecurityRecordsInsert = "insert into user_geog_security (userid,sp_geog,user_level,view_salesman,able_to_update,security_type) values (?,?,?,?,?,'V')";
    
    private static final String createUserWithAddressInsert = "insert into users " +
	"(userid,last_name,first_name,vistaline_id,email_address,hide_from_assignments," +
	"override_security,set_security,view_everything," +
	"addr1,city,state,zip,country,date_added,date_changed) " +
	"values (?,?,?,?,?,'N','N','N','N',?,?,?,?,?,sysdate,sysdate)";
    private static final String updateUserWithAddressUpdate = "update users set first_name = ?, last_name = ?, email_address = ?, vistaline_id = ?, addr1 = ?, city = ?, state = ?, zip = ?, country = ?, date_changed = sysdate where userid = ?";
    
    public static final String addSegmentOverrideInsert = "INSERT INTO USER_SEGMENTS (SEGMENT_ID, USERID) VALUES(?,?)";
    public static final String removeSegmentOverrideDelete = "DELETE FROM USER_SEGMENTS WHERE SEGMENT_ID=? AND USERID=?";
    public static final String getSegmentOverridesSelect = "SELECT b.* FROM USER_SEGMENTS a, SEGMENTS b WHERE a.userid=? and a.segment_id=b.segment_id order by b.segment_name";
    private static final String getGeogForUserQuery = "select sp_geog_cd from current_salesman_v where user_id = ?";
    private static final String getUserNameQuery = "select first_name, last_name from users where userid =?";
    private static final String getUserEmailAddressQuery = "select email_address from users where userid =?";
    
    public static User getUser(String userid, Connection DBConn) throws Exception {
		User user = new User();
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		PreparedStatement pstmt3 = null;
		ResultSet rs3 = null;
		try {
			
			//get the user
			pstmt1 = DBConn.prepareStatement(getUserQry);
			pstmt1.setString(1, userid.toLowerCase());
			try {
				rs1 = pstmt1.executeQuery();				
			} catch (SQLException ex) {				
				SMRCLogger.error("UserDAO.getUser(): ", ex);
				SMRCLogger.error("UserDAO.getUser(): SQL : " + getUserQry + "\nArgs: " + userid.toLowerCase());
				throw ex;
			}
			
			if (rs1.next())
			{
				user.setUserid(userid);
				user.setFirstName(StringManipulation.noNull(rs1.getString("first_name")));
				user.setLastName(StringManipulation.noNull(rs1.getString("last_name")));
				user.setVistaId((StringManipulation.noNull(rs1.getString("vistaline_id"))).toUpperCase());
				user.setEmailAddress(StringManipulation.noNull(rs1.getString("email_address")));
				user.setSalesIds(getSalesIds(StringManipulation.noNull(rs1.getString("vistaline_id")),DBConn));
				user.setSalesId((StringManipulation.noNull(rs1.getString("salesman_id"))).toUpperCase());
				if (StringManipulation.noNull(rs1.getString("override_security")).equals("Y")) {
					user.setOverrideSecurity(true);
				}
				if (StringManipulation.noNull(rs1.getString("set_security")).equals("Y")) {
					user.setSetSecurity(true);
				}
				if (StringManipulation.noNull(rs1.getString("view_profile")).equals("Y")) {
					user.setViewProfile(true);
				}
				user.setViewEverything(StringManipulation.noNull(rs1.getString("view_everything")));
				//user.setMarketingMgr(StringManipulation.noNull(rs.getString("force_pwd_change")));
				user.setGroupId(rs1.getInt("user_group_id"));
				user.setUserGroup(StringManipulation.noNull(rs1.getString("user_group_name")));
				if (rs1.getString("sp_geog") != null){
				    user.setUserGroupGeog(rs1.getString("sp_geog"));
				}
				if (rs1.getString("division_id") != null){
				    user.setUserGroupDivisionId(rs1.getString("division_id"));
				}
				user.setJobTitle(StringManipulation.noNull(rs1.getString("hr_job_title")));
				user.setHomePage(StringManipulation.noNull(rs1.getString("homepage")));
				if (StringManipulation.noNull(rs1.getString("div_csf")).equals("Y")) {
					user.setAbleToSeeDivisionCSF(true);
				}
				if (StringManipulation.noNull(rs1.getString("district_csf")).equals("Y")) {
					user.setAbleToSeeDistrictCSF(true);
				}
				user.setGeography(StringManipulation.noNull(rs1.getString("sp_geog_cd")));
                user.setTitleTx(StringManipulation.noNull(rs1.getString("title_tx")));
// Braffet : Removed for Tap dollars                user.setDollarTypeCode(rs.getInt("DEFAULT_DOLLAR_TYPE_ID"));
                CodeType code = MiscDAO.getCodeById(rs1.getInt("DEFAULT_DOLLAR_TYPE_ID"),DBConn);
				if (StringManipulation.noNull(code.getDescription()).equalsIgnoreCase("End Market")){
				    user.setUseCredit(false);
				    user.setUseDirect(false);
				    user.setUseEndMarket(true);
				} else if (StringManipulation.noNull(code.getDescription()).equalsIgnoreCase("Charge To")) {
				    user.setUseCredit(false);
				    user.setUseDirect(true);
				    user.setUseEndMarket(false);
				}
			}else{
				// Return null if user is not in database.  This should never happen
			    SMRCLogger.warn("UserDAO.getUser() - Could not find user: " + userid.toLowerCase());
			    return null;
				
			}
			
			// get the security overrides for geographies
			pstmt2 = DBConn.prepareStatement(getUserQry2);
			pstmt2.setString(1, userid);
			try {
				rs2 = pstmt2.executeQuery();
			} catch (SQLException ex) {				
				SMRCLogger.error("UserDAO.getUser(): ", ex);
				SMRCLogger.error("UserDAO.getUser(): SQL : " + getUserQry2 + "\nArgs: " + userid);
				throw ex;
			}
				
				
			while (rs2.next()) {
				UserGeogSecurity ugs = new UserGeogSecurity();
				ugs.setUserid(StringManipulation.noNull(rs2.getString("userid")));
				ugs.setLevel(StringManipulation.noNull(rs2.getString("user_level")));
				ugs.setViewSalesman(StringManipulation.noNull(rs2.getString("view_salesman")));
				ugs.setSecurityType(StringManipulation.noNull(rs2.getString("security_type")));
				ugs.setSPGeog(StringManipulation.noNull(rs2.getString("sp_geog")));
				if (StringManipulation.noNull(rs2.getString("sp_geog")).length() == 4) {
					ugs.setMaintenance("Y");
				}else {
					ugs.setMaintenance(StringManipulation.noNull(rs2.getString("able_to_update")));
				}
				
				user.addGeogSecurity(ugs);
			}
			// Get the segment overrides for the user
			pstmt3 = DBConn.prepareStatement(getUserQry3);
			pstmt3.setString(1, userid);
			try {
				rs3 = pstmt3.executeQuery();
			} catch (SQLException ex) {				
				SMRCLogger.error("UserDAO.getUser(): ", ex);
				SMRCLogger.error("UserDAO.getUser(): SQL : " + getUserQry3 + "\nArgs: " + userid);
				throw ex;
			}
				
			while (rs3.next()) {
				user.addSegmentOverride(rs3.getInt("SEGMENT_ID"));
			}
		}catch (Exception e)	{
			SMRCLogger.error("UserDAO.getUser(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs1);
			SMRCConnectionPoolUtils.close(pstmt1);
			SMRCConnectionPoolUtils.close(rs2);
			SMRCConnectionPoolUtils.close(pstmt2);
			SMRCConnectionPoolUtils.close(rs3);
			SMRCConnectionPoolUtils.close(pstmt3);
		}

		return user;
	}

    public static List getSalesIds(String vistaId, Connection DBConn) throws Exception {
		
        ArrayList salesIds = new ArrayList();
        if(vistaId==null || vistaId.trim().length()==0) return salesIds;
        
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			
			//get the user
			pstmt = DBConn.prepareStatement(getSalesIdsQuery);
			pstmt.setString(1, vistaId.toLowerCase());
			try {
				rs = pstmt.executeQuery();
			} catch (SQLException ex) {				
				SMRCLogger.error("UserDAO.getSalesIds(): ", ex);
				SMRCLogger.error("UserDAO.getSalesIds(): SQL : " + getSalesIdsQuery + "\nArgs: " + vistaId.toLowerCase());
				throw ex;
			}
				
			
			while(rs.next()){
			    salesIds.add(StringManipulation.noNull(rs.getString("SALESMAN_ID")));
			}
			
		}catch (Exception e){
			SMRCLogger.error("UserDAO.getSalesIds(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return salesIds;
    }
    
	
	public static boolean userExists(String userid, Connection DBConn)throws Exception {
		
		PreparedStatement pstmt = null;
		ResultSet res = null;
		boolean exists = false;
		
		try {	
			pstmt = DBConn.prepareStatement(userExistsSelect);
			pstmt.setString(1,userid);
			try {
				res = pstmt.executeQuery();
			} catch (SQLException ex) {				
				SMRCLogger.error("UserDAO.userExists(): ", ex);
				SMRCLogger.error("UserDAO.userExists(): SQL : " + userExistsSelect + "\nArgs: " + userid);
				throw ex;
			}
							
			if (res.next()) {
				if (res.getInt(1) > 0) {
					exists = true;
				}
			}
		}catch (Exception e){
			SMRCLogger.error("UserDAO.userExists(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(res);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return exists;
	}
	
	public static void createUser(String userid, String firstName, String lastName, String email, String vistaId,Connection DBConn)throws Exception {
		

		PreparedStatement pstmt = null;
		try {
			int pIndex = 0 ;
			pstmt = DBConn.prepareStatement(createUserInsert);
			pstmt.setString(++pIndex,userid);
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(lastName));
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(firstName));
			pstmt.setString(++pIndex,vistaId);
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(email));
			try {
				pstmt.executeUpdate();
			} catch (SQLException ex) {				
				SMRCLogger.error("UserDAO.createUser(): ", ex);
				SMRCLogger.error("UserDAO.createUser(): SQL : " + createUserInsert 
						+ "\nArgs: " + userid + ", " + StringManipulation.fixQuotes(lastName) + ", " + StringManipulation.fixQuotes(firstName)
						+ ", " + vistaId + ", " + StringManipulation.fixQuotes(email));
				throw ex;
			}

		}catch (Exception e){
			SMRCLogger.error("UserDAO.createUser(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
	}
	
	public static void createUser(String userid, String firstName, String lastName, String email,
		String vistaId, String aStreet, String aCity, String aState, String aZipCode, String aCountry, Connection DBConn )throws Exception	{

		PreparedStatement pstmt = null;
		try {
			int pIndex = 0 ;
			pstmt = DBConn.prepareStatement(createUserWithAddressInsert);
			pstmt.setString(++pIndex,userid);
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(lastName));
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(firstName));
			pstmt.setString(++pIndex,vistaId);
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(email));
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(aStreet));
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(aCity));
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(aState));
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(aZipCode));
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(aCountry));
			try {
				pstmt.executeUpdate();
			} catch (SQLException ex) {				
				SMRCLogger.error("UserDAO.createUser(): ", ex);
				SMRCLogger.error("UserDAO.createUser(): SQL : " + createUserWithAddressInsert 
						+ "\nArgs: " + userid + ", " + StringManipulation.fixQuotes(lastName) + ", " + StringManipulation.fixQuotes(firstName)
						+ ", " + vistaId + ", " + email + ", " + aStreet +", " + aCity + ", " + aState + ", " + aZipCode + ", " + aCountry);
				throw ex;
			}


		}catch (Exception e){
			SMRCLogger.error("UserDAO.createUser(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
	}
	
	public static void updateUser(String userid, String firstName, String lastName, String email, String vistaId,Connection DBConn)throws Exception	{
			
		PreparedStatement pstmt = null;
		try {
			int pIndex = 0 ;
			pstmt = DBConn.prepareStatement(updateUserUpdate);
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(firstName));
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(lastName));
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(email));
			pstmt.setString(++pIndex,vistaId);
			pstmt.setString(++pIndex,userid);
			try {
				pstmt.executeUpdate();
			} catch (SQLException ex) {				
				SMRCLogger.error("UserDAO.updateUser(): ", ex);
				SMRCLogger.error("UserDAO.updateUser(): SQL : " + updateUserUpdate 
						+ "\nArgs: " + StringManipulation.fixQuotes(firstName) + ", " + StringManipulation.fixQuotes(lastName)
						+ ", " + StringManipulation.fixQuotes(email) + ", " + vistaId + ", " + userid);
				throw ex;
			}
			
		}catch (Exception e){
			SMRCLogger.error("UserDAO.updateUser(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
	}
	
	public static void updateUser( String userid, String firstName, String lastName, String email, 
			String vistaId, String aStreet, String aCity, String aState, String aZipCode, String aCountry, Connection DBConn )throws Exception	{
		
		PreparedStatement pstmt = null;
		try {
			int pIndex = 0 ;
			pstmt = DBConn.prepareStatement(updateUserWithAddressUpdate);
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(firstName));
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(lastName));
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(email));
			pstmt.setString(++pIndex,vistaId);
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(aStreet));
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(aCity));
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(aState));
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(aZipCode));
			pstmt.setString(++pIndex,StringManipulation.fixQuotes(aCountry));
			pstmt.setString(++pIndex,userid);
			try {
				pstmt.executeUpdate();
			} catch (SQLException ex) {				
				SMRCLogger.error("UserDAO.updateUser(): ", ex);
				SMRCLogger.error("UserDAO.updateUser(): SQL : " + updateUserWithAddressUpdate 
						+ "\nArgs: " + userid + ", " + StringManipulation.fixQuotes(firstName) + ", " + StringManipulation.fixQuotes(lastName)
						+ ", " + StringManipulation.fixQuotes(email) + ", " + vistaId + ", " + userid);
				throw ex;
			}
			
		}catch (Exception e){
			SMRCLogger.error("UserDAO.updateUser(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(pstmt);
		}
	
	}

	public static void updateVistaSecurityRecords(VistaSecurity vs,User user,Connection DBConn)throws Exception {
		SMRCLogger.debug("in UserDAO.updateVistaSecurityRecords()");
		if (vs == null || user == null) {
			return;
		}
		
		if (vs.hasUniversalAccess()) {

			PreparedStatement pstmt = null;
			try {
				pstmt = DBConn.prepareStatement(updateVistaSecurityRecordsUpdate);
				pstmt.setString(1,user.getUserid());
				pstmt.executeUpdate();
				
			}catch (SQLException e){
				SMRCLogger.error("UserDAO.updateVistaSecurityRecords(): ", e);
				SMRCLogger.error("UserDAO.updateVistaSecuirtyRecords(): SQL : " + updateVistaSecurityRecordsUpdate + "\nArgs: " + user.getUserid());
				throw e;
			}
			finally {
				SMRCConnectionPoolUtils.close(pstmt);
			}
		}
		else {
			PreparedStatement pstmt = null;
			try {
				pstmt = DBConn.prepareStatement(updateVistaSecurityRecordsDelete);
				pstmt.setString(1,user.getUserid());
				pstmt.executeUpdate();
			}catch (SQLException e){
				SMRCLogger.error("UserDAO.updateVistaSecurityRecords(): ", e);
				SMRCLogger.error("UserDAO.updateVistaSecuirtyRecords(): SQL : " + updateVistaSecurityRecordsDelete + "\nArgs: " + user.getUserid());
				throw e;
			}
			finally {
				SMRCConnectionPoolUtils.close(pstmt);
			}
			
			Collection vsRecords = vs.getVistaSecurityRecords();
			Iterator vsRecordsIterator = vsRecords.iterator();
			
			while (vsRecordsIterator.hasNext()) {
				VSRecord vsr = (VSRecord)vsRecordsIterator.next();
				
				try {
					int pIndex = 0 ;
					pstmt = DBConn.prepareStatement(updateVistaSecurityRecordsInsert);
					pstmt.setString(++pIndex,user.getUserid());
					pstmt.setString(++pIndex,vsr.getTeam());
					pstmt.setString(++pIndex,vsr.getLevel());
					pstmt.setString(++pIndex,vsr.getViewSalesman());
					pstmt.setString(++pIndex,vsr.getMaint());
					
					pstmt.executeUpdate();
				}catch (Exception e){
					SMRCLogger.error("UserDAO.updateVistaSecurityRecords(): ", e);
					SMRCLogger.error("UserDAO.updateVistaSecuirtyRecords(): SQL : " + updateVistaSecurityRecordsInsert 
							+ "\nArgs: " + user.getUserid() + " ," + vsr.getTeam() + " ," + vsr.getLevel()+ " ," + vsr.getViewSalesman()+ " ," + vsr.getMaint());
					throw e;
				}
				finally {
					SMRCConnectionPoolUtils.close(pstmt);
				}
			}
		}
	
	} //method
	
	public static TreeMap getAllUsersTreeMap(Connection DBConn) throws Exception{
		TreeMap users = new TreeMap();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DBConn.prepareStatement(getAllUsersTreeMapQuery);
			try {
				rs = pstmt.executeQuery();
			} catch (SQLException ex) {				
				SMRCLogger.error("UserDAO.getAllUsersTreeMap(): ", ex);
				SMRCLogger.error("UserDAO.getAllUsersTreeMap(): SQL : " + getAllUsersTreeMapQuery);
				throw ex;
			}
				
			while (rs.next()){
			    users.put(StringManipulation.noNull(rs.getString("last_name")) + ", " + StringManipulation.noNull(rs.getString("first_name")), StringManipulation.noNull(rs.getString("userid")));
			}
		}catch (Exception e){
			SMRCLogger.error("UserDAO.getAllUsersTreeMap(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return users;
	}

	// Returns a User object for a specific vistalineId
	public static User getUserByVistalineId(String vistalineId, Connection DBConn) throws Exception {
		User user = new User();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DBConn.prepareStatement(getUserByVistalineId);
                        pstmt.setString(1, vistalineId);
            try {
            	rs = pstmt.executeQuery();
			}catch (SQLException e){
				SMRCLogger.error("UserDAO.getUserByVistalineId(): ", e);
				SMRCLogger.error("UserDAO.getUserByVistalineId(): SQL : " + getUserByVistalineId 
						+ "\nArgs: " + vistalineId);
				throw e;
			}
            	
			
			while (rs.next())
			{
				if (rs.getString("userid") != null) {
					user = getUser(rs.getString("userid"), DBConn);             
				}
				
			}
			
		}catch (Exception e){
			SMRCLogger.error("UserDAO.getUserByVistalineId(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		
		return user;
	}
        
	// Returns a User object for a specific salesId
	public static User getUserBySalesId(String aSalesId, Connection DBConn) throws Exception {
		User user = new User();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DBConn.prepareStatement(getUserBySalesId);
            pstmt.setString(1, aSalesId);
            try {
            	rs = pstmt.executeQuery();
			}catch (SQLException e){
				SMRCLogger.error("UserDAO.getUserBySalesId(): ", e);
				SMRCLogger.error("UserDAO.getUserBySalesId(): SQL : " + getUserBySalesId 
						+ "\nArgs: " + aSalesId);
				throw e;
			}
            	
			
			while (rs.next())
			{
				if (rs.getString("userid") != null) {
					user = getUser(rs.getString("userid"), DBConn);             
				}
				
			}
			
		}catch (Exception e){
			SMRCLogger.error("UserDAO.getUserBySalesId(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		
		return user;
	}
        
        // Returns the user_group_id for a specific job title
    public static int getUserGroupsCodeByTitle(String title, Connection DBConn) throws Exception {
		int userGroupCode = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DBConn.prepareStatement(getUserGroupsCodeByTitle);
            pstmt.setString(1, title);
            
            try {
            	rs = pstmt.executeQuery();
			}catch (SQLException e){
				SMRCLogger.error("UserDAO.getUserBySalesId(): ", e);
				SMRCLogger.error("UserDAO.getUserBySalesId(): SQL : " + getUserGroupsCodeByTitle 
						+ "\nArgs: " + title);
				throw e;
			}

			
			while (rs.next())
			{
				userGroupCode = rs.getInt("user_group_id");
  			}
			
		}catch (Exception e){
			SMRCLogger.error("UserDAO.getUserGroupsCodeByTitle(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		
		return userGroupCode;
	}
        
        // Returns an ArrayList of all channel marketing managers in User Objects
    public static ArrayList getAllChannelMarketingManagers(Connection DBConn) throws Exception {
		ArrayList allCMManagers = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
            int user_group_id = getUserGroupsCodeByTitle("Global Channel Manager", DBConn);
			pstmt = DBConn.prepareStatement(getUseridsByUserGroupCode);
            pstmt.setInt(1, user_group_id);
            
            try {
            	rs = pstmt.executeQuery();
			}catch (SQLException e){
				SMRCLogger.error("UserDAO.getAllChannelMarketingManagers(): ", e);
				SMRCLogger.error("UserDAO.getAllChannelMarketingManagers(): SQL : " + getUseridsByUserGroupCode 
						+ "\nArgs: " + user_group_id);
				throw e;
			}
            	
			
			while (rs.next()){
                User user = new User();
                user = getUser(rs.getString("userid"), DBConn);
				allCMManagers.add(user);
  			}
			
		}catch (Exception e){
			SMRCLogger.error("UserDAO.getAllChannelMarketingManagers(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		
		return allCMManagers;
	}
    
    // Returns an ArrayList of all division managers in User Objects
    public static ArrayList getAllDivisionManagers(Connection DBConn) throws Exception {
		ArrayList allDivisionManagers = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
            int user_group_id = getUserGroupsCodeByTitle("Division Manager", DBConn);
			pstmt = DBConn.prepareStatement(getUseridsByUserGroupCode);
            pstmt.setInt(1, user_group_id);
            try {
            	rs = pstmt.executeQuery();
			}catch (SQLException e){
				SMRCLogger.error("UserDAO.getAllDivisionManagers(): ", e);
				SMRCLogger.error("UserDAO.getAllDivisionManagers(): SQL : " + getUseridsByUserGroupCode 
						+ "\nArgs: " + user_group_id);
				throw e;
			}
            	
			
			while (rs.next()){
                User user = new User();
                user = getUser(rs.getString("userid"), DBConn);
                allDivisionManagers.add(user);
  			}
			
		}catch (Exception e){
			SMRCLogger.error("UserDAO.getAllDivisionManagers(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		
		return allDivisionManagers;
	}
   
	public static ArrayList getUsersInGroup(String group, Connection DBConn) throws Exception {
		ArrayList users = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DBConn.prepareStatement(getUsersInGroupQuery);
			pstmt.setString(1,group);
			try {
				rs = pstmt.executeQuery();
			}catch (SQLException e){
				SMRCLogger.error("UserDAO.getUsersInGroup(): ", e);
				SMRCLogger.error("UserDAO.getUsersInGroup(): SQL : " + getUsersInGroupQuery 
						+ "\nArgs: " + group);
				throw e;
			}
				
			
			while (rs.next())
			{
				User user = new User();
				

				user.setUserid(StringManipulation.noNull(rs.getString("userid")));
				user.setFirstName(StringManipulation.noNull(rs.getString("first_name")));
				user.setLastName(StringManipulation.noNull(rs.getString("last_name")));
				user.setVistaId((StringManipulation.noNull(rs.getString("vistaline_id"))).toUpperCase());
				user.setEmailAddress(StringManipulation.noNull(rs.getString("email_address")));
				user.setSalesId((StringManipulation.noNull(rs.getString("salesman_id"))).toUpperCase());
				user.setSalesIds(getSalesIds(StringManipulation.noNull(rs.getString("vistaline_id")),DBConn));
				if (StringManipulation.noNull(rs.getString("override_security")).equals("Y")) {
					user.setOverrideSecurity(true);
				}
				if (StringManipulation.noNull(rs.getString("set_security")).equals("Y")) {
					user.setSetSecurity(true);
				}
				if (StringManipulation.noNull(rs.getString("view_profile")).equals("Y")) {
					user.setViewProfile(true);
				}				
				user.setViewEverything(StringManipulation.noNull(rs.getString("view_everything")));
				//user.setMarketingMgr(StringManipulation.noNull(rs.getString("force_pwd_change")));
				user.setGroupId(rs.getInt("user_group_id"));
				user.setUserGroup(StringManipulation.noNull(rs.getString("user_group_name")));
				if (rs.getString("sp_geog") != null){
				    user.setUserGroupGeog(rs.getString("sp_geog"));
				}
				if (rs.getString("division_id") != null){
				    user.setUserGroupDivisionId(rs.getString("division_id"));
				}
				user.setJobTitle(StringManipulation.noNull(rs.getString("hr_job_title")));
				user.setHomePage(StringManipulation.noNull(rs.getString("homepage")));
				if (StringManipulation.noNull(rs.getString("div_csf")).equals("Y")) {
					user.setAbleToSeeDivisionCSF(true);
				}
				if (StringManipulation.noNull(rs.getString("district_csf")).equals("Y")) {
					user.setAbleToSeeDistrictCSF(true);
				}
				user.setGeography(StringManipulation.noNull(rs.getString("sp_geog_cd")));
                user.setTitleTx(StringManipulation.noNull(rs.getString("title_tx")));
// Removed for tap dollars                user.setDollarTypeCode(rs.getInt("DEFAULT_DOLLAR_TYPE_ID"));
				
				users.add(user);
			}
			
		}catch (Exception e){
			SMRCLogger.error("UserDAO.getUsersInGroup(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		
		return users;
	}
	
	/*
	 * Called from the user profile page for giving segment overrides to users
	 * If the record already exists, log it and continue without throwing error up
	 */
	public static void addSegmentOverride(String userid, int segmentId, Connection DBConn)throws Exception {
		

		PreparedStatement pstmt = null;
		SMRCLogger.debug("UserDAO.addSegmentOverride() SQL-\n" + addSegmentOverrideInsert + "\n?=" + segmentId + "?=\n" + userid);
		try {
			int pIndex = 0 ;
			pstmt = DBConn.prepareStatement(addSegmentOverrideInsert);
			pstmt.setInt(++pIndex,segmentId);
			pstmt.setString(++pIndex,userid);
			pstmt.executeUpdate();
		}catch (SQLException e){
				SMRCLogger.error("UserDAO.addSegmentOverride(): ", e);
				SMRCLogger.error("UserDAO.addSegmentOverride(): SQL : " + addSegmentOverrideInsert 
						+ "\nArgs: " + segmentId + ", " + userid);
				throw e;
		} catch (Exception e){
			SMRCLogger.error("UserDAO.addSegmentOverride(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
	}
	
	public static void removeSegmentOverride(String userid, int segmentId, Connection DBConn)throws Exception {
		

		PreparedStatement pstmt = null;
		SMRCLogger.debug("UserDAO.removeSegmentOverride() SQL-\n" + removeSegmentOverrideDelete + "\n?=" + segmentId + "?=\n" + userid);
		try {
			int pIndex = 0 ;
			pstmt = DBConn.prepareStatement(removeSegmentOverrideDelete);
			pstmt.setInt(++pIndex,segmentId);
			pstmt.setString(++pIndex,userid);
			pstmt.executeUpdate();
		} catch (SQLException e){
			SMRCLogger.error("UserDAO.removeSegmentOverride(): ", e);
			SMRCLogger.error("UserDAO.removeSegmentOverride(): SQL : " + removeSegmentOverrideDelete 
					+ "\nArgs: " + segmentId + ", " + userid);
			throw e;
		} catch (Exception e){
			SMRCLogger.error("UserDAO.removeSegmentOverride(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
	}
	public static ArrayList getSegmentOverrides(String userid, Connection DBConn)throws Exception {
	
		SMRCLogger.debug("UserDAO.getSegmentOverrides() SQL-\n" + getSegmentOverridesSelect + "\n?=" + userid);
	    ArrayList segments = new ArrayList();
	    PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			int pIndex = 0 ;
			pstmt = DBConn.prepareStatement(getSegmentOverridesSelect);
			pstmt.setString(++pIndex,userid);
			try {
			 rs=pstmt.executeQuery();
			} catch (SQLException e){
				SMRCLogger.error("UserDAO.getSegmentOverrides(): ", e);
				SMRCLogger.error("UserDAO.getSegmentOverrides(): SQL : " + getSegmentOverridesSelect 
						+ "\nArgs: " + userid);
				throw e;
			}			 
			
			
			while(rs.next()){
			    Segment segment = new Segment();
			    segment.setName(rs.getString("SEGMENT_NAME"));
			    segment.setSegmentId(rs.getInt("SEGMENT_ID"));
			    segments.add(segment);
			}
			
			return segments;
		}catch (Exception e){
			SMRCLogger.error("UserDAO.getSegmentOverrides(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
	}
	// Returns the geog from current_salesman_v for the specified vistaline id 
	public static String getGeogForUser(String vistalineId, Connection DBConn)throws Exception {
		
			SMRCLogger.debug("UserDAO.getGeogForUser() SQL-\n" + getGeogForUserQuery + "\n?=" + vistalineId);
		    String userGeog = "";
		    PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = DBConn.prepareStatement(getGeogForUserQuery);
				pstmt.setString(1, vistalineId);
				try {
					rs=pstmt.executeQuery();
				} catch (SQLException e){
					SMRCLogger.error("UserDAO.getGeogForUser(): ", e);
					SMRCLogger.error("UserDAO.getGeogForUser(): SQL : " + getGeogForUserQuery 
							+ "\nArgs: " + vistalineId);
					throw e;
				}			 
			
				while(rs.next()){
				    userGeog = rs.getString("sp_geog_cd");
				}
				
				return userGeog;
			}catch (Exception e){
				SMRCLogger.error("UserDAO.getGeogForUser(): ", e);
				throw e;
			}
			finally {
				SMRCConnectionPoolUtils.close(rs);
				SMRCConnectionPoolUtils.commitTransaction(DBConn);
				SMRCConnectionPoolUtils.close(pstmt);
			}
			
		}
    
//	 Returns a users name 
	public static String getUserName(String userid, Connection DBConn)throws Exception {
		
			SMRCLogger.debug("UserDAO.getUserName() SQL-\n" + getUserNameQuery + "\n?=" + userid);
		    String userName = "";
		    PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
//				int pIndex = 0 ;
				pstmt = DBConn.prepareStatement(getUserNameQuery);
				pstmt.setString(1, userid);
				try {
					rs=pstmt.executeQuery();
				} catch (SQLException e){
					SMRCLogger.error("UserDAO.getUserName(): ", e);
					SMRCLogger.error("UserDAO.getUserName(): SQL : " + getUserNameQuery 
							+ "\nArgs: " + userid);
					throw e;
				}			 
	
			
				while(rs.next()){
				    userName = rs.getString("first_name") + " " + rs.getString("last_name");
				}
				
				return userName;
			}catch (Exception e){
				SMRCLogger.error("UserDAO.getUserName(): ", e);
				throw e;
			}
			finally {
				SMRCConnectionPoolUtils.close(rs);
				SMRCConnectionPoolUtils.commitTransaction(DBConn);
				SMRCConnectionPoolUtils.close(pstmt);
			}
			
		}
    


//Returns a users email address 
public static String getUserEmailAddress (String userid, Connection DBConn)throws Exception {
	
		SMRCLogger.debug("UserDAO.getUserEmailAddress() SQL-\n" + getUserEmailAddressQuery + "\n?=" + userid);
	    String emailAddress = "";
	    PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DBConn.prepareStatement(getUserEmailAddressQuery);
			pstmt.setString(1, userid);
			try {
				rs=pstmt.executeQuery();
			} catch (SQLException e){
				SMRCLogger.error("UserDAO.getUserEmailAddress(): ", e);
				SMRCLogger.error("UserDAO.getUserEmailAddress(): SQL : " + getUserEmailAddressQuery 
						+ "\nArgs: " + userid);
				throw e;
			}			 

		
			while(rs.next()){
			    emailAddress = rs.getString("email_address");
			}
			
			return emailAddress;
		}catch (Exception e){
			SMRCLogger.error("UserDAO.getUserEmailAddress(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
	}

// Returns an ArrayList of all CHAMPS managers in User Objects
	public static ArrayList getAllCHAMPSManagers(Connection DBConn) throws Exception {
		ArrayList allCHAMPSManagers = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
	        int user_group_id = getUserGroupsCodeByTitle("CHAMPS Manager", DBConn);
			pstmt = DBConn.prepareStatement(getUseridsByUserGroupCode);
	        pstmt.setInt(1, user_group_id);
	        try {
	        	rs = pstmt.executeQuery();
			} catch (SQLException e){
				SMRCLogger.error("UserDAO.getAllCHAMPSManagers(): ", e);
				SMRCLogger.error("UserDAO.getAllCHAMPSManagers(): SQL : " + getUseridsByUserGroupCode 
						+ "\nArgs: " + user_group_id);
				throw e;
			}			 
	      
			
			while (rs.next()){
	            User user = new User();
	            user = getUser(rs.getString("userid"), DBConn);
	            allCHAMPSManagers.add(user);
				}
			
		}catch (Exception e){
			SMRCLogger.error("UserDAO.getAllCHAMPSManagers(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		
		return allCHAMPSManagers;
	}


//	 Returns an ArrayList of User Object of the Project Sales Manager of a specified geography
	public static ArrayList getProjectSalesManagers(String geog, Connection DBConn) throws Exception {
		ArrayList projectSalesManagers = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
	        int user_group_id = getUserGroupsCodeByTitle("Project Sales Manager", DBConn);
			pstmt = DBConn.prepareStatement(getProjectSalesManagerForGeog);
	        pstmt.setInt(1, user_group_id);
	        pstmt.setString(2,geog);
	        try {
	        	rs = pstmt.executeQuery();
			} catch (SQLException e){
				SMRCLogger.error("UserDAO.getProjectSalesManagers(): ", e);
				SMRCLogger.error("UserDAO.getProjectSalesManagers(): SQL : " + getProjectSalesManagerForGeog 
						+ "\nArgs: " + user_group_id + ", " + geog);
				throw e;
			}			 
	        	
			
			while (rs.next()){
	            User user = new User();
	            user = getUser(rs.getString("userid"), DBConn);
	            projectSalesManagers.add(user);
				}
			
		}catch (Exception e){
			SMRCLogger.error("UserDAO.getProjectSalesManagers(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		
		return projectSalesManagers;
	}
	
//	 Returns an ArrayList of User Object of the Division Managers of a specified division
	public static ArrayList getDivisionManagersForDivision(String divisionName, Connection DBConn) throws Exception {
		ArrayList divisionManagers = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
	        int user_group_id = getUserGroupsCodeByTitle("Division Manager", DBConn);
			pstmt = DBConn.prepareStatement(getDivisionManagerForDivision);
	        pstmt.setInt(1, user_group_id);
	        pstmt.setString(2,divisionName);
	        try {
	        	rs = pstmt.executeQuery();
			} catch (SQLException e){
				SMRCLogger.error("UserDAO.getDivisionManagersForDivision(): ", e);
				SMRCLogger.error("UserDAO.getDivisionManagersForDivision(): SQL : " + getDivisionManagerForDivision 
						+ "\nArgs: " + user_group_id + ", " + divisionName);
				throw e;
			}			 
	        	
			
			while (rs.next()){
	            User user = new User();
	            user = getUser(rs.getString("userid"), DBConn);
	            divisionManagers.add(user);
			}
			
		}catch (Exception e){
			SMRCLogger.error("UserDAO.getDivisionManagersForDivision(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		
		return divisionManagers;
	}
	
}

