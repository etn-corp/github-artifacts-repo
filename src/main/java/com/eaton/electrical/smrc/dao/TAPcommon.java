/*
 * TAPcommon.java
 *
 * Created on April 19, 2004, 10:00 AM
 */

package com.eaton.electrical.smrc.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.service.*;

/**
 *
 * @author  Jason Lubbert
 */
public class TAPcommon {
	
	public static ChangeOrderPotential getChangeOrderPotential(String id, Connection DBConn) {
		ChangeOrderPotential cop = new ChangeOrderPotential();
		
		String sel = "select * from change_order_potentials where cop_id = " + id;
		
		Statement s = null;
		ResultSet r = null;
		
		try {
			s = DBConn.createStatement();
			r = s.executeQuery(sel);
			
			while (r.next()) {
				cop.setId(r.getInt("cop_id"));
				cop.setDescription(r.getString("description"));
			}

		}
		catch (Exception e) {
			SMRCLogger.error("TapCommon.getChangeOrderPotential() ", e);
			return null;
		}
		finally {
			SMRCConnectionPoolUtils.close(r);
		    SMRCConnectionPoolUtils.close(s);
		}
		
		return cop;
	}
	

	public static String getHelpPage(String page, Connection DBConn) {
		String help = "index.html";
		
		String sel = "select * from help_pages where app_page = '" + page + "'";
		
		Statement s = null;
		ResultSet r = null;
		
		try {
			s = DBConn.createStatement();
			r = s.executeQuery(sel);
			
			if (r.next()) {
				help = r.getString("help_page");
			}
			
		}
		catch (Exception e) {
			SMRCLogger.error("TapCommon.getHelpPage() ", e);
			return null;
		}
		finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}
		
		return help;
	}
	
	public static String getEBEDescription(String id, Connection DBConn) {
		String desc = "";
		
		String sel = "select description from ebe_category where ebe_id = " + id;
		
		Statement s = null;
		ResultSet r = null;
		
		try {
			s = DBConn.createStatement();
			r = s.executeQuery(sel);
			
			while (r.next()) {
				desc = r.getString("description");
			}

		}
		catch (Exception e) {
			SMRCLogger.error("TapCommon.getEBEDescription() ", e);
			return null;
		}
		finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}
		
		return desc;
	}
	
	public static String getFocusTypeDesc(String focusType, Connection DBConn) {
		String desc = "";
		
		String sel = "SELECT * from focus_type WHERE focus_type_id = " + focusType;
		
		Statement s = null;
		ResultSet r = null;
		
		try {
			s = DBConn.createStatement();
			r = s.executeQuery(sel);
			
			while (r.next())
			{
				desc = r.getString("description");
			}

		}
		catch (Exception e) {
			SMRCLogger.error("TapCommon.getFocusTypeDesc() ", e);
			return null;
		}
		finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}		
		return desc;
	}
	
	public static String getProductDescription(String id,Connection DBConn) {
		String returnVal = "";
		
		String sel = "SELECT product_description from products where product_id = '" + id + "'";
		
		Statement s = null;
		ResultSet r = null;
		
		try {
			s = DBConn.createStatement();
			r = s.executeQuery(sel);
			
			while (r.next())
			{
				returnVal = r.getString("product_description");
			}
			
		}
		catch (Exception e) {
			SMRCLogger.error("TapCommon.getProductDescription() ", e);
			return null;
		}
		finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}	
		
		return returnVal;
	}

	
	public static ProjectStatus getProjectStatus (String id, Connection DBConn) {
		ProjectStatus ret = new ProjectStatus();
		
		String sel = "select * from project_statuses where status_id = " + id;
		
		Statement s = null;
		ResultSet r = null;
		
		try {
			s = DBConn.createStatement();
			r = s.executeQuery(sel);
			
			while (r.next()) {
				ret.setId(r.getInt("status_id"));
				ret.setDescription(r.getString("description"));
			}
			
		}
		catch (Exception e) {
			SMRCLogger.error("TapCommon.getProjectStatus() ", e);
			return null;
		}
		finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}	
		
		return ret;
	}
	
	public static SpecPreference getSpecPreference (String id, Connection DBConn) {
		SpecPreference ret = new SpecPreference();
		
		String sel = "select * from ch_spec_preferences where preference_id = " + id;
		
		Statement s = null;
		ResultSet r = null;
		
		try {
			s = DBConn.createStatement();
			r = s.executeQuery(sel);
			
			while (r.next()) {
				ret.setId(r.getInt("preference_id"));
				ret.setDescription(r.getString("description"));
			}

		}
		catch (Exception e) {
			SMRCLogger.error("TapCommon.getSpecPreference() ", e);
			return null;
		}
		finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}
		
		return ret;
	}
	
	public static TargetReason getStratReason (String id, Connection DBConn) {
		TargetReason ret = new TargetReason();
		
		String sel = "select * from target_reasons where strat_reason_id = " + id;
		
		Statement s = null;
		ResultSet r = null;
		
		try {
			s = DBConn.createStatement();
			r = s.executeQuery(sel);
			
			while (r.next()) {
				ret.setId(r.getInt("strat_reason_id"));
				ret.setDescription(r.getString("description"));
			}
			
		}
		catch (Exception e) {
			SMRCLogger.error("TapCommon.getStratReason() ", e);
			return null;
		}
		finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}	
		
		return ret;
	}
		
	public static ArrayList getGeogsUserCanSee(User user, Connection DBConn) throws Exception {
	   
	    boolean hasSegmentOverride = false;
	    ArrayList segmentOverrideList = user.getSegmentOverrides();
     	if (segmentOverrideList.size() > 0){
     		hasSegmentOverride = true;
     	}
	    ArrayList zones = new ArrayList();
        ArrayList districts = new ArrayList();
        ArrayList salesOrgs = new ArrayList();
        ArrayList groupcodes = new ArrayList();
        ArrayList teams = new ArrayList();
        try {
	        if (hasSegmentOverride){
	        	zones = MiscDAO.getAllZones(DBConn);
	        	districts = DistrictDAO.getAllDistricts(DBConn);
	        	salesOrgs = DistrictDAO.getAllSalesOrganizations(DBConn);
	        	groupcodes = DistrictDAO.getAllGroupCodes(DBConn);
	        	teams = DistrictDAO.getAllTeams(DBConn);
	        } else {
	        	zones = checkUserGeogAccess(MiscDAO.getAllZones(DBConn), user);
	        	districts = checkUserGeogAccess(DistrictDAO.getAllDistricts(DBConn), user);
	        	salesOrgs = checkUserGeogAccess(DistrictDAO.getAllSalesOrganizations(DBConn), user);
	        	groupcodes = checkUserGeogAccess(DistrictDAO.getAllGroupCodes(DBConn), user);
	        	teams = checkUserGeogAccess(DistrictDAO.getAllTeams(DBConn),user);
	        }
        } catch (Exception e){
            SMRCLogger.error("TAPcommon.getGeogsUserCanSee(): ", e);
			throw e;
        }
        ArrayList allGeogs = new ArrayList();
        allGeogs.addAll(salesOrgs);
        allGeogs.addAll(groupcodes);
        allGeogs.addAll(zones);
        allGeogs.addAll(districts);
        allGeogs.addAll(teams);
        
        GeographySorter sorter = new GeographySorter();
        Collections.sort(allGeogs, sorter);
        return allGeogs;
        
	}
	
	private static ArrayList checkUserGeogAccess(ArrayList geogList, User user) {
        ArrayList returnList = new ArrayList();
        for (int i = 0; i < geogList.size(); i++) {
            Geography geog = (Geography) geogList.get(i);
            if (user.ableToSee(geog.getGeog())) {
                returnList.add(geogList.get(i));
            }

        }
        return returnList;
    }
	
}
