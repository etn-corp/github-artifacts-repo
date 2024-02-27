/*
 * Created on Feb 14, 2005
 *
 */
package com.eaton.electrical.smrc.dao;

import java.sql.*;
import java.util.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.service.*;

/**
 * @author Jason Lubbert
 *
 */
public class ActivityDAO {
    
    public static ArrayList getUsageReportResults (String groupBy, String geogFilter, String dateFilter, 
            String sortBy, String sortDir, User user, int beginWith, int endWith, Connection DBConn) 
    	throws Exception {
        
        ArrayList beans = new ArrayList();
        String descriptionSQL = "";
        String tablesSQL = "";
        String groupBySQL = "";
        StringBuffer whereSQL = new StringBuffer();
        if (groupBy.equalsIgnoreCase("zone")){
            descriptionSQL = " (substr(activity_log.user_geog,0,4) || '0 -' || geographies.description) ";
            tablesSQL = ", geographies ";
            groupBySQL = " substr(activity_log.user_geog,0,4), geographies.description ";
            whereSQL.append(" and geographies.sp_geog = substr(activity_log.user_geog,0,4)||0 ");
        } else if (groupBy.equalsIgnoreCase("district")){
            descriptionSQL = " (substr(activity_log.user_geog,0,5) || '-' || geographies.description) ";
            tablesSQL = ", geographies ";
            groupBySQL = " substr(activity_log.user_geog,0,5), geographies.description ";
            whereSQL.append(" and geographies.sp_geog = substr(activity_log.user_geog,0,5) ");
        } else if (groupBy.equalsIgnoreCase("team")){
            descriptionSQL = " (substr(activity_log.user_geog,0,6) || '-' || geographies.description) ";
            tablesSQL = ", geographies ";
            groupBySQL = " substr(activity_log.user_geog,0,6), geographies.description ";
            whereSQL.append(" and geographies.sp_geog = substr(activity_log.user_geog,0,6) ");
            whereSQL.append(" and geographies.team is not null ");
        } else if (groupBy.equalsIgnoreCase("month/year")){
            descriptionSQL = " (to_number(to_char(activity_log.add_date, 'YYYY')) || ' - '  || to_number(to_char(activity_log.add_date, 'MM'))) orderbyfield, " +
                      "to_char(activity_log.add_date,'Month YYYY') ";            
            groupBySQL = " to_number(to_char(activity_log.add_date, 'YYYY')), to_number(to_char(activity_log.add_date, 'MM')), to_char(activity_log.add_date,'Month YYYY') ";
            if (sortBy.equalsIgnoreCase("description")){
                sortBy = "orderbyfield";
            }
        } else {
            descriptionSQL = " (users.last_name || ', ' || users.first_name) ";
            tablesSQL = ", users ";
            groupBySQL = " activity_log.userid, users.last_name, users.first_name ";
            whereSQL.append(" and users.userid = activity_log.userid ");
        }
        
        if (geogFilter != null && !geogFilter.equals("")){
            Geography geography = new Geography(geogFilter);
            String filterSQL = "";
            
            if (geography.getGroupCode().equals("0")){
                filterSQL = geogFilter.substring(0,1) + "%";
            } else if (geography.getZone().equals("00")){
                filterSQL = geogFilter.substring(0,2) + "%";
            } else if (geography.getDistrict().equals("0")){
                filterSQL = geogFilter.substring(0,4) + "%";
            } else {
                filterSQL = geogFilter + "%";
            }
            whereSQL.append(" and activity_log.user_geog like '" + filterSQL + "' ");
        }
        
        if (dateFilter != null && !dateFilter.equals("")){
            String year = dateFilter.substring(0,4);
            String month = dateFilter.substring(4,dateFilter.length());
            whereSQL.append(" and to_number(to_char(activity_log.add_date, 'YYYY')) = " + year);
            whereSQL.append(" and to_number(to_char(activity_log.add_date, 'MM')) = " + month);
        }
             
        StringBuffer theQuery = new StringBuffer();
        theQuery.append("select * from (");
        theQuery.append("select rownum rownumber, ");
        theQuery.append("description, pageviews ");
        
        theQuery.append(" from (");
        theQuery.append("select count(*) pageviews, ");
        theQuery.append(descriptionSQL + " description ");
        theQuery.append("from activity_log " + tablesSQL);
        theQuery.append(" where user_geog is not null ");
        theQuery.append(whereSQL.toString());
        theQuery.append(" group by " + groupBySQL);
        theQuery.append(" order by " + sortBy + " " + sortDir);
        
        theQuery.append(") ) where rownumber between " + (beginWith + 1) + "  and " + endWith);
                
        Statement stmt = null;
        ResultSet rs = null;
        try {
            SMRCLogger.debug(" SQL - ActivityDAO.getUsageReportResults()\n" + theQuery.toString());
            stmt = DBConn.createStatement();
            rs = stmt.executeQuery(theQuery.toString());
            while (rs.next()){
                UsageReportBean bean = new UsageReportBean();
                bean.setDescription(rs.getString("description"));
                bean.setPageViews(rs.getInt("pageviews"));
                beans.add(bean);
            }
            
            
        } catch (Exception e) {
            SMRCLogger.error("ActivityDAO.getUsageReportResults(): ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }
        
        return beans;
    }
    
   

}
