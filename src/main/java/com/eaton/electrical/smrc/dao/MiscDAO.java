package com.eaton.electrical.smrc.dao;

import java.sql.*;
import java.util.*;

import javax.servlet.http.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.exception.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;


/**
 * @author E0062708
 *
 */
public class MiscDAO {
	
	private static final String getCodesQuery = "SELECT * FROM code_types WHERE code_type = ? ORDER BY code_seq_num ";
	private static final String getCountiesQuery = "SELECT * FROM COUNTIES ORDER BY STATE, COUNTY_NAME";
	private static final String getCountriesQuery = "SELECT COUNTRY_CODE, COUNTRY_NAME, COUNTRY_ALPHA_CODE FROM COUNTRY_MV ORDER BY COUNTRY_NAME";
	private static final String getCountryFromCodeQuery = "SELECT COUNTRY_NAME FROM COUNTRY_MV WHERE COUNTRY_CODE=?";
	private static final String getDistributorsVendorsQuery = "SELECT * FROM VENDORS WHERE DISTRIB_COMPETITOR_FLAG='Y' ORDER BY DESCRIPTION";
	private static final String getVendorsQuery = "SELECT * FROM VENDORS ORDER BY DESCRIPTION";
    private static final String getFocusTypeDesc = "SELECT DESCRIPTION FROM FOCUS_TYPE WHERE FOCUS_TYPE_ID=?";
    private static final String getVendorsForApprovalQuery = "SELECT pri_current_competitor_id,pri_proposed_competitor_id,sec_current_competitor_id,sec_proposed_competitor_id,other_current_competitor_id,other_proposed_competitor_id" +
	" from distrib_prd_competitors a" +
	" where distributor_id=?";
    private static final String getGeographiesQuery="select * from geographies where sp_geog like '145%' and team is null and district!='0' order by sp_geog";
    //private static final String getZonesQuery="select * from geographies where sp_geog like '145%' and team is null and district='0' order by description";
    private static final String getGeographyQuery="select * from geographies where sp_geog=?";
    private static final String getDistrictManagerByGeogQuery="select a.*, b.email_address, b.last_name, b.first_name, b.vistaline_id from current_salesman_v a, users b where a.user_id = b.vistaline_id AND a.title_tx='DM' and a.sales_id_vertical = 'GS' AND" +
	" a.SALES_ORG_CD=? AND a.GROUP_CD=? AND a.ZONE_CD=? AND a.DISTRICT_CD=?";
    private static final String getZoneManagerByGeogQuery="select a.*, b.email_address, b.last_name, b.first_name, b.vistaline_id from current_salesman_v a, users b where a.user_id = b.vistaline_id and a.title_tx='ZM' and a.sales_id_vertical = 'GS' AND" +
	" a.SALES_ORG_CD=? AND a.GROUP_CD=? AND a.ZONE_CD=?";
    private static final String getFocusTypesQuery = "SELECT * from focus_type order by description";
    private static final String getAllZonesQuery = "select * from geographies where sp_geog like '146%' and team is null and district='0' and period_yyyy = ? order by description";
    private static final String getToolboxDetailsQuery = "select * from toolbox_detail where toolbox_id =? order by seq_num";
    private static final String getZipCodesQuery="select * from sp_zip_code_mv where postal_zip_code like ? order by POSTAL_ZIP_CODE, POSTAL_CITY_NAME";	
    private static final String saveAPLogInsert = "INSERT INTO activity_log (id,userid,page,action,user_geog) VALUES(activity_log_seq.nextval,?,?,'',?)";
    private static final String getCodesByIdQuery = "select * from code_types where code_type_id = ?";
    private static final String getJobFunctionsQuery = "select * from contact_titles order by description";
    private static final String getVendorInfoQuery = "select * from vendors where vendor_id = ?";
    private static final String getEbeCategoriesQuery = "select * from ebe_category order by description";
    private static final String getStatesQuery = "select state, state_name from state_mv order by state_name";
    private static final String getYearsQuery = "select * from years";
    private static final String getStaticEmailAddressesQuery = "select * from email_addresses where LOWER(email_address_type) = ?";
    private static final String getSpecialProgramsQuery = "select * from special_programs order by special_program_name";
    private static final String getSpecialProgramDescQuery = "select * from special_programs where special_program_id = ?";
    
//  Vince: Junk not yet baked. TODO
	//private static final String getSICDescriptionQuery = "Vince: Not yet implemented properly.  See temp method below";
	
	
	public static TreeMap getCountries(Connection DBConn) throws Exception{
		TreeMap countries= new TreeMap();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getCountriesQuery);
			rs = pstmt.executeQuery();
	
			while (rs.next()){
				countries.put(rs.getString("COUNTRY_NAME"), rs.getString("COUNTRY_CODE"));
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getCountries(): ", e);
			throw e;
		}finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return countries;

	}
	
	public static String getCountryFromCode(String countryCode, Connection DBConn) throws Exception{
		String country = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getCountryFromCodeQuery);
			pstmt.setString(1, countryCode);
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				return rs.getString("COUNTRY_NAME");
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getCountryFromCode(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return country;

	}	
	
	public static TreeMap getStates(Connection DBConn) throws Exception{
	    
		TreeMap states= new TreeMap();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getStatesQuery);
			rs = pstmt.executeQuery();
	
			while (rs.next()){
			    states.put(rs.getString("STATE"), rs.getString("STATE_NAME"));
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getStates(): ", e);
			throw e;
		}finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return states;
	
	}
	
	public static TreeMap getCounties(Connection DBConn) throws Exception{
		TreeMap counties= new TreeMap();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getCountiesQuery);
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				counties.put(rs.getString("STATE") + ", " + rs.getString("COUNTY_NAME"), rs.getString("COUNTY_ID"));
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getCounties(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return counties;

	}	

	public static ArrayList getCodes(String type, Connection DBConn) throws Exception{
		ArrayList codes = new ArrayList();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getCodesQuery);
			
			pstmt.setString(1, type);
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				CodeType code = new CodeType();
				code.setId(101);
				code.setId(rs.getInt("code_type_id"));
				code.setName(rs.getString("code_description"));
                code.setDescription(rs.getString("code_description"));
                code.setValue(rs.getString("code_value"));
				codes.add(code);
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getCodes(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return codes;
		
	}		

	/*
	public static ArrayList getCodeValue(String type, Connection DBConn) throws Exception{
		ArrayList codes = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getCodesQuery);
			
			pstmt.setString(1, type);
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				CodeType code = new CodeType();
				code.setId(rs.getInt("code_type_id"));
				code.setName(rs.getString("code_description"));
				codes.add(code);
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getCodeValue(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return codes;
		
	}	
	*/
	
	public static ArrayList getVendors(boolean getDistOnly, Connection DBConn) throws Exception{
		ArrayList vendors = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			if(getDistOnly){
				pstmt = DBConn.prepareStatement(getDistributorsVendorsQuery);
			}else{
				pstmt = DBConn.prepareStatement(getVendorsQuery);
			}
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				Vendor vendor = new Vendor();
				vendor.setId(rs.getInt("VENDOR_ID"));
				vendor.setDescription(StringManipulation.noNull(rs.getString("DESCRIPTION")));
				vendors.add(vendor);
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getVendors(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return vendors;
		
	}	
	
	public static ArrayList getVendorsForApprovalForm(int distId, Connection DBConn) throws Exception{
		ArrayList vendors = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			pstmt = DBConn.prepareStatement(getVendorsForApprovalQuery);
			pstmt.setInt(1, distId);
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				
				Vendor vendor = new Vendor();
				vendor.setId(rs.getInt("VENDOR_ID"));
				vendor.setDescription(StringManipulation.noNull(rs.getString("DESCRIPTION")));
				vendors.add(vendor);
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getVendorsForApprovalForm(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return vendors;
		
	}	
	
	
	public static ArrayList getGeographies(Connection DBConn) throws Exception{
		
		ArrayList geographies= new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getGeographiesQuery);
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
			SMRCLogger.error("MiscDAO.getGeographies(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return geographies;

	}
	
	/*
	public static ArrayList getZones(String geog, Connection DBConn) throws Exception{

		ArrayList geographies= new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getZonesQuery);
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				Geography geography = new Geography(rs.getString("SP_GEOG"),rs.getString("DESCRIPTION"));
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getZones(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return geographies;

	}	
	*/
	
	public static Geography getGeography(String geog, Connection DBConn) throws Exception{
		Geography geography = new Geography();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getGeographyQuery);
			pstmt.setString(1,geog);
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				geography.setGeog(StringManipulation.noNull(rs.getString("SP_GEOG")));
				geography.setDescription(StringManipulation.noNull(rs.getString("DESCRIPTION")));
				geography.setSalesOrg(StringManipulation.noNull(rs.getString("SALES_ORG")));
				geography.setGroupCode(StringManipulation.noNull(rs.getString("GROUP_CODE")));
				geography.setZone(StringManipulation.noNull(rs.getString("ZONE")));
				geography.setDistrict(StringManipulation.noNull(rs.getString("DISTRICT")));
				geography.setTeam(StringManipulation.noNull(rs.getString("TEAM")));
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getGeography(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return geography;

	}	
	
	public static Salesman getDistrictManagerByGeog(String geog, Connection DBConn) throws Exception{
		//Geography geography = MiscDAO.getGeography(geog, DBConn);
		Geography geography = new Geography(geog);
		String salesOrg = geography.getSalesOrg();
		String groupCode = geography.getGroupCode();
		String zone = geography.getZone();
		String district = geography.getDistrict();		
		
		Salesman returnSalesman = new Salesman();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SMRCLogger.debug("SQL - MiscDAO.getDistrictManagerByGeog():\n" + getDistrictManagerByGeogQuery);
		try {
			pstmt = DBConn.prepareStatement(getDistrictManagerByGeogQuery);
			pstmt.setString(1,salesOrg);
			pstmt.setString(2,groupCode);
			pstmt.setString(3,zone);
			pstmt.setString(4,district);
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				returnSalesman.setFirstName(StringManipulation.noNull(rs.getString("FIRST_NAME")));
				returnSalesman.setLastName(StringManipulation.noNull(rs.getString("LAST_NAME")));
				returnSalesman.setTitle(StringManipulation.noNull(rs.getString("TITLE_TX")));
				//returnSalesman.setSalesId(StringManipulation.noNull(rs.getString("SALESMAN_ID")));
				returnSalesman.setSalesIds(UserDAO.getSalesIds(StringManipulation.noNull(rs.getString("VISTALINE_ID")), DBConn));
				returnSalesman.setVistalineId(StringManipulation.noNull(rs.getString("USER_ID")));
				returnSalesman.setEmailAddress(StringManipulation.noNull(rs.getString("EMAIL_ADDRESS")));
				
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getDistrictManagerByGeog(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return returnSalesman;

	}	        
	
	public static Salesman getZoneManagerByGeog(String geog, Connection DBConn) throws Exception{
		//Geography geography = MiscDAO.getGeography(geog, DBConn);
		Geography geography = new Geography(geog);
		String salesOrg = geography.getSalesOrg();
		String groupCode = geography.getGroupCode();
		String zone = geography.getZone();
	
		Salesman returnSalesman = new Salesman();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getZoneManagerByGeogQuery);
			pstmt.setString(1,salesOrg);
			pstmt.setString(2,groupCode);
			pstmt.setString(3,zone);
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				returnSalesman.setFirstName(StringManipulation.noNull(rs.getString("FIRST_NAME")));
				returnSalesman.setLastName(StringManipulation.noNull(rs.getString("LAST_NAME")));
				returnSalesman.setTitle(StringManipulation.noNull(rs.getString("TITLE_TX")));
				//returnSalesman.setSalesId(StringManipulation.noNull(rs.getString("SALESMAN_ID")));
				returnSalesman.setSalesIds(UserDAO.getSalesIds(StringManipulation.noNull(rs.getString("VISTALINE_ID")), DBConn));
				returnSalesman.setVistalineId(StringManipulation.noNull(rs.getString("USER_ID")));
				returnSalesman.setEmailAddress(StringManipulation.noNull(rs.getString("EMAIL_ADDRESS")));
			
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getZoneManagerByGeog(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return returnSalesman;
	}
	
   public static ArrayList getFocusTypes(Connection DBConn) throws Exception {
		ArrayList fTypeList = new ArrayList ();
		
        ResultSet rs = null;
        Statement stmt = null;

		try {
			stmt = DBConn.createStatement();
			rs = stmt.executeQuery(getFocusTypesQuery);
	
			while (rs.next()){
                FocusType ftype = new FocusType();
				ftype.setId(rs.getInt("focus_type_id"));
                ftype.setDescription(rs.getString("description"));
                fTypeList.add(ftype);
			}
		}catch (Exception e){
			SMRCLogger.error("MiscDAO.getFocusTypes(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(stmt);
		}
		
		return fTypeList;
	}
    
    public static String getFocusTypeDescription(String focusTypeId, Connection DBConn) throws Exception{
		
    	String ftDesc = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getFocusTypeDesc);
			pstmt.setString(1,focusTypeId);
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				ftDesc = StringManipulation.noNull(rs.getString("DESCRIPTION"));
				
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getFocusTypeDescription(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return ftDesc;

	}
    
	protected static void setFocusType(String vcn, int focusTypeId, Connection DBConn) throws Exception {
		PreparedStatement pstmt = null;
		String deleteFocusTypes = "DELETE FROM CUSTOMER_FOCUS_TYPE WHERE VISTA_CUSTOMER_NUMBER=?";
		String insertFocusType = "INSERT INTO CUSTOMER_FOCUS_TYPE (VISTA_CUSTOMER_NUMBER, FOCUS_TYPE_ID,DATE_CHANGED,DATE_ADDED) VALUES(?,?,sysdate,sysdate)";
		
		try {
		    int pIndex = 0 ;
	        pstmt = DBConn.prepareStatement(deleteFocusTypes);
	        pstmt.setString( ++pIndex, vcn) ;
	        pstmt.executeUpdate();
	        
	        if(focusTypeId!=0) {
		        pIndex = 0 ;
				pstmt = DBConn.prepareStatement(insertFocusType);
				pstmt.setString( ++pIndex, vcn) ;
				pstmt.setInt( ++pIndex, focusTypeId) ;
				pstmt.executeUpdate();
	        }
			
			
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.setFocusType(): " , e);
			
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}	
	}
    
   public static ArrayList getAllZones(Connection DBConn) throws Exception {
		ArrayList zoneList = new ArrayList ();

        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String currentYear = getSRYear(DBConn); 
        
		try {
			pstmt = DBConn.prepareStatement(getAllZonesQuery);
			pstmt.setString(1, currentYear);
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
                Geography zone = new Geography();
				zone.setGeog(rs.getString("sp_geog"));
                zone.setDescription(rs.getString("description"));
                zoneList.add(zone);
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getAllZones(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return zoneList;
	}
 	

	public static Toolbox getToolbox(String acctId, Connection DBConn) throws Exception {
		Toolbox tb = new Toolbox();
		
 		ArrayList segments = SegmentsDAO.getSegmentsForAccount(acctId, DBConn);
 		if(segments.size()==0){
 			return tb;
 		}
 		
 		StringBuffer selectBuffer = new StringBuffer("select * from toolbox where");
		
 		boolean firstInstance=true;
 		for(int i=0;i<segments.size();i++){
			Segment segment = (Segment)segments.get(i);
			if(firstInstance){
				firstInstance=false;
			}else{
				selectBuffer.append(" OR");
			}
			selectBuffer.append(" segment_id=" + segment.getSegmentId());
		}
 		selectBuffer.append(" order by row_num,col_num");
 		String sel = selectBuffer.toString();
 		
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DBConn.createStatement();
			rs = stmt.executeQuery(sel);
			
			while (rs.next()) {
				ToolboxGroup item = new ToolboxGroup();
				
				if (rs.getString("url") != null) {
					item.setURL(rs.getString("url"));
				}
				
				item.setDescription(rs.getString("description"));
				
				if (rs.getString("logo") != null) {
					item.setImage(rs.getString("logo"));
				}
				
				item.setBrowserFlag(rs.getString("browser_flag"));
				item.setRow(rs.getInt("row_num"));
				item.setColumn(rs.getInt("col_num"));
				item.setColumnCount(rs.getInt("col_width"));
				
				item.addDetails(getToolboxDetails(rs.getInt("toolbox_id"), DBConn));
				
				tb.addGroup(item);
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getToolbox(String vcn): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(stmt);
		}
		return tb;
	}
	
	public static Toolbox getToolbox(int segmentId, Connection DBConn) throws Exception {
		Toolbox tb = new Toolbox();
		ArrayList segments = new ArrayList();
		
		Segment thisSegment = SegmentsDAO.getSegment(segmentId,DBConn);
		//Segment parent=null;
		segments.add(thisSegment);
		while(thisSegment.getParentId()!=0){	    
		    thisSegment=SegmentsDAO.getSegment(thisSegment.getParentId(),DBConn);
		    segments.add(thisSegment);
		}

 		StringBuffer selectBuffer = new StringBuffer("select * from toolbox where");
		
 		boolean firstInstance=true;
 		for(int i=0;i<segments.size();i++){
			Segment segment = (Segment)segments.get(i);
			if(firstInstance){
				firstInstance=false;
			}else{
				selectBuffer.append(" OR");
			}
			selectBuffer.append(" segment_id=" + segment.getSegmentId());
		}
 		selectBuffer.append(" order by row_num,col_num");
 		String sel = selectBuffer.toString();
 		
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DBConn.createStatement();
			rs = stmt.executeQuery(sel);
			
			while (rs.next()) {
				ToolboxGroup item = new ToolboxGroup();
				
				if (rs.getString("url") != null) {
					item.setURL(rs.getString("url"));
				}
				
				item.setDescription(rs.getString("description"));
				
				if (rs.getString("logo") != null) {
					item.setImage(rs.getString("logo"));
				}
				
				item.setBrowserFlag(rs.getString("browser_flag"));
				item.setRow(rs.getInt("row_num"));
				item.setColumn(rs.getInt("col_num"));
				item.setColumnCount(rs.getInt("col_width"));
				
				item.addDetails(getToolboxDetails(rs.getInt("toolbox_id"), DBConn));
				
				tb.addGroup(item);
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getToolbox(int segmentId): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(stmt);
		}
		return tb;
	}
	
	
	private static ArrayList getToolboxDetails(int toolboxId, Connection DBConn) throws Exception {
		ArrayList details = new ArrayList(10);

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DBConn.prepareStatement(getToolboxDetailsQuery);
			pstmt.setInt(1,toolboxId);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				ToolboxElement item = new ToolboxElement();
				
				item.setURL(rs.getString("url"));
				item.setDescription(rs.getString("description"));
				item.setDefinition(rs.getString("definition"));
				
				if (rs.getString("logo") != null) {
					item.setImage(rs.getString("logo"));
				}
				
				item.setBrowserFlag(rs.getString("browser_flag"));
				
				details.add(item);
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getToolboxDetails(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return details;
	}	
 	
	public static ArrayList getZipCodes(String zip, Connection DBConn) throws Exception{
		ArrayList results = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getZipCodesQuery);
			pstmt.setString(1,zip + "%");
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				Address addr = new Address();
				addr.setZip(StringManipulation.noNull(rs.getString("POSTAL_ZIP_CODE")));
				addr.setState(StringManipulation.noNull(rs.getString("POSTAL_STATE_ABBREV")));
				addr.setCity(StringManipulation.noNull(rs.getString("POSTAL_CITY_NAME")));
				results.add(addr);
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getZipCodes(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return results;

	}
        
        
    public static void saveAPLog(User user, String page, Connection DBConn) throws Exception {
        String userGeog = "";
        if ((user.getVistaId() != null) && (user.getVistaId().length() > 1)){
            userGeog = UserDAO.getGeogForUser(user.getVistaId(), DBConn);
        }
        PreparedStatement pstmt = null;
		
		try {
			pstmt = DBConn.prepareStatement(saveAPLogInsert);
			pstmt.setString(1,user.getUserid());
			pstmt.setString(2,page);
			pstmt.setString(3,userGeog);
			pstmt.executeUpdate();
			
		}catch (Exception e)	{
		    SMRCLogger.error("MiscDAO.saveAPLog(): ", e);
			throw e;
		}
		finally {
		    SMRCConnectionPoolUtils.close(pstmt);
		}
	}
        
    //** Generic method that returns an ArrayList from an executed query
   public static ArrayList genericQueryReturningArrayList(String sql, Connection DBConn) throws Exception{
		ArrayList results = new ArrayList();
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = DBConn.createStatement();
			rs = stmt.executeQuery(sql);
			SMRCLogger.debug("MiscDAO.genericQueryReturningArrayList(): SQL Query: " + sql);
			while (rs.next())
			{
				String result = rs.getString(1);
				results.add(result);
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.genericQueryReturningArrayList(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(stmt);
		}
		
		return results;

	}
	
   public static User setFirstVisit(HttpServletRequest request, Connection DBConn) throws ProfileException, Exception{
		
		SessionProfile sessionProfile = SMRCSession.getSessionProfile(request);
		String email = sessionProfile.getEmail();
		String fName = sessionProfile.getFirstName();
		String lName = sessionProfile.getLastName();
		String userid = sessionProfile.getUserid();
		String vid = sessionProfile.getVistaID();
		
		// Address information
		String street = sessionProfile.getStreet();
		String city = sessionProfile.getCity();
		String state = sessionProfile.getState();
		String zipCode = sessionProfile.getZipCode();
		String country = sessionProfile.getCountry();

		if (!UserDAO.userExists(userid,DBConn)) {
			UserDAO.createUser(userid,fName,lName,email,vid,street,city,state,zipCode,country,DBConn);
		}else {
			UserDAO.updateUser(userid,fName,lName,email,vid,street,city,state,zipCode,country,DBConn);
		}
		
		// get new user object from db and set in session
		User usr = SMRCSession.getUser(request,true,DBConn);
		
		VistaSecurity vs = SMRCDataProvisioningService.getVistaSecurity(usr);
		UserDAO.updateVistaSecurityRecords(vs,usr,DBConn);
		
		return usr;
		
   }
   
   public static String getSICDescription(String sic, Connection DBConn)throws Exception {
	
	String desc = "";
	String sel = "select sic_description from sic where sic_code = '" + sic + "'";
	Statement stmt = null;
	ResultSet res = null;
	try {
		stmt = DBConn.createStatement();
		res = stmt.executeQuery(sel);
		
		if (res.next()) {
			desc = res.getString("sic_description");
		}
		
	}catch (Exception e){
		SMRCLogger.error("MiscDAO.getSICDescription(): ", e);
		throw e;
	}
	finally {
		SMRCConnectionPoolUtils.close(res);
		SMRCConnectionPoolUtils.close(stmt);
	}
	
	return desc;

   } //method
   
   
   public static CodeType getCodeById(int codeId, Connection DBConn)throws Exception {
	
	PreparedStatement pstmt = null;
	ResultSet rs = null;
        CodeType codeType = new CodeType();
	try {
		pstmt = DBConn.prepareStatement(getCodesByIdQuery);
                pstmt.setInt(1, codeId);
		rs = pstmt.executeQuery();
		
		if (rs.next()) {
                        codeType.setId(rs.getInt("code_type_id"));
                        codeType.setName(rs.getString("code_type"));
                        codeType.setValue(rs.getString("code_value"));
                        codeType.setSeq(rs.getInt("code_seq_num"));
                        codeType.setDescription(rs.getString("code_description"));
		}
		
	}catch (Exception e){
		SMRCLogger.error("MiscDAO.getCodeById(): ", e);
		throw e;
	}
	finally {
		SMRCConnectionPoolUtils.close(rs);
		SMRCConnectionPoolUtils.close(pstmt);
	}
	
        return codeType;

   } 

	public static TreeMap getJobFunctions(Connection DBConn) throws Exception{
		TreeMap functions= new TreeMap();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getJobFunctionsQuery);
			rs = pstmt.executeQuery();
	
			while (rs.next()){
				functions.put(rs.getString("description"), rs.getString("title_id"));
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getCountries(): ", e);
			throw e;
		}finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return functions;

	}
        
    public static Vendor getVendorInfo(int vendorId, Connection DBConn) throws Exception{
		Vendor vendor = new Vendor();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getVendorInfoQuery);
            pstmt.setInt(1, vendorId);
			rs = pstmt.executeQuery();
	
			while (rs.next()){
				vendor.setDescription(rs.getString("description"));
                vendor.setId(rs.getInt("vendor_id"));
                                
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getVendorInfo(): ", e);
			throw e;
		}finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return vendor;

	}
    
    public static String getVendorName(int vendorId, Connection DBConn) throws Exception{
		String vendorName = "";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getVendorInfoQuery);
            pstmt.setInt(1, vendorId);
			rs = pstmt.executeQuery();
	
			while (rs.next()){
				vendorName = rs.getString("description");
                                
			}
		}catch (Exception e)	{
			SMRCLogger.error("MiscDAO.getVendorInfo(): ", e);
			throw e;
		}finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return vendorName;

	}
        
    public static ArrayList getEBECategories(Connection aConnection) throws Exception {

            ArrayList cats = new ArrayList(10);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = aConnection.prepareStatement(getEbeCategoriesQuery);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    EBECategory ec = new EBECategory();

                    ec.setId(rs.getInt("ebe_id"));
                    ec.setDescription(rs.getString("description"));

                    cats.add(ec);
                }
            } catch (Exception e) {
                SMRCLogger.error("MiscDAO.getEBECategories() ", e);
                throw e;
            } finally {
                SMRCConnectionPoolUtils.close(rs);
                SMRCConnectionPoolUtils.close(pstmt);
            }

            return cats;

     } //method
    
    // This method reads the Years table and returns the year that is current
    // according to sales reporting.
    public static String getSRYear(Connection aConnection) throws Exception {

        String year = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = aConnection.prepareStatement(getYearsQuery);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                year = rs.getString("year");
            }
            
        } catch (Exception e) {
            SMRCLogger.error("MiscDAO.getSRYear() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(pstmt);
        }

        return year;

 } //method
    
//  This method reads the Years table and returns the month that is current
// according to sales reporting.
    public static String getSRMonth(Connection aConnection) throws Exception {

        String month = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = aConnection.prepareStatement(getYearsQuery);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                month = rs.getString("month");
            }
            
        } catch (Exception e) {
            SMRCLogger.error("MiscDAO.getSRMonth() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(pstmt);
        }

        return month;

 } 
    
//  This method retrieves all email addresses from the email_address table for 
//  the specified type
     public static ArrayList getStaticEmailAddress(String type, Connection aConnection) throws Exception {

         ArrayList emailAddresses = new ArrayList();
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         try {
             pstmt = aConnection.prepareStatement(getStaticEmailAddressesQuery);
             pstmt.setString(1,type.toLowerCase());
             rs = pstmt.executeQuery();

             while (rs.next()) {
                 emailAddresses.add(rs.getString("email_address"));
             }
             
         } catch (Exception e) {
             SMRCLogger.error("MiscDAO.getStaticEmailAddress() ", e);
             throw e;
         } finally {
             SMRCConnectionPoolUtils.close(rs);
             SMRCConnectionPoolUtils.close(pstmt);
         }

         return emailAddresses;

  } //method
     
	 public static ArrayList getSpecialPrograms(Connection DBConn) throws Exception {
		ArrayList spList = new ArrayList ();
		
	     ResultSet rs = null;
	     Statement stmt = null;
	
		try {
			stmt = DBConn.createStatement();
			rs = stmt.executeQuery(getSpecialProgramsQuery);
	
			while (rs.next()){
	            DropDownBean bean = new DropDownBean();
				bean.setValue(rs.getString("special_program_id"));
	            bean.setName(rs.getString("special_program_name"));
	            spList.add(bean);
			}
		}catch (Exception e){
			SMRCLogger.error("MiscDAO.getSpecialPrograms(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(stmt);
		}
		
		return spList;
	}
	 
	 public static String getSpecialProgramDescription(String spId, Connection DBConn) throws Exception {
			String spDescription = "";
			
		     ResultSet rs = null;
		     PreparedStatement pstmt = null;
		
			try {
				pstmt = DBConn.prepareStatement(getSpecialProgramDescQuery);
				int spIdInt = Globals.a2int(spId);
				pstmt.setInt(1,spIdInt);
				rs = pstmt.executeQuery();
		
				while (rs.next()){
		            spDescription = rs.getString("special_program_name");
				}
			}catch (Exception e){
				SMRCLogger.error("MiscDAO.getSpecialProgramDescription(): ", e);
				throw e;
			}
			finally {
				SMRCConnectionPoolUtils.close(rs);
				SMRCConnectionPoolUtils.close(pstmt);
			}
			
			return spDescription;
		}
     
}
