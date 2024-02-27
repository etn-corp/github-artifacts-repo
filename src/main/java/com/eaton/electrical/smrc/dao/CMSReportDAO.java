package com.eaton.electrical.smrc.dao;

import java.sql.*;
import java.util.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.exception.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;


/**
 * @author Jason Lubbert
 *
 */

public class CMSReportDAO {

	public static ArrayList executeReportSQL(String sql, Connection DBConn) throws Exception, SMRCException {
           
	    ArrayList results = new ArrayList();
        
	    Statement stmt = null;
        ResultSet rs  = null;
        ResultSetMetaData meta = null;
        int columnCount = 0;

        try {
    	    /*
    	     * Get the maximum amount of time to allow a query to run before timing out.
    	     * The default will be 1800 sec or 30 min for now.
    	     */
    	    //ResourceBundle rb = ResourceBundle.getBundle("com.eaton.electrical.dbinfo.smrc.db");
    	    //int maxTimeout = Globals.a2int(rb.getString("SMRC_MAX_QUERY_TIMEOUT"));
        	int maxTimeout = 1800;
        	if(maxTimeout==0) maxTimeout=1800;
            
            CMSReportSearchResults resultHeaders = new CMSReportSearchResults();
            stmt = DBConn.createStatement();
            stmt.setQueryTimeout(maxTimeout);
            rs = stmt.executeQuery(sql);
            meta = rs.getMetaData();
            columnCount = meta.getColumnCount();

            //  Get headers from results
            for (int i=1; i < (columnCount + 1); i++){
                resultHeaders.addToHeadings(meta.getColumnName(i));
            }

            results.add(resultHeaders);

            while (rs.next()){
                   CMSReportSearchResults searchresults = new CMSReportSearchResults();
                   for (int i=1; i < (columnCount + 1); i++){
                       if (meta.getColumnType(i) == java.sql.Types.NUMERIC){
/*                            if (meta.getColumnName(i).equalsIgnoreCase("id") ||
                                meta.getColumnName(i).equalsIgnoreCase("id2")){
                                int dollarfield = rs.getInt(i);
                                searchresults.addToResultObjects(new Integer(dollarfield));
                            } else {*/
                                double dollarfield = rs.getDouble(i);
                                searchresults.addToResultObjects(new Double(dollarfield));
/*                            }*/
                        }
                        if (meta.getColumnType(i) == java.sql.Types.VARCHAR){
                            String textfield = rs.getString(i);
                            searchresults.addToResultObjects(textfield);
                        }
                    }    
                   results.add(searchresults);
            }
        }catch (SQLException se){
            if(se.getErrorCode()==1013) {
                /*
                 * If the query takes longer than stmt.setQueryTimeout() is set to
                 * I only write a warn and throw a new SMRCException, which is handled
                 * differently by the caller so as not to log it as an ERROR
                 */
                SMRCLogger.warn("CMSReportDAO.executeReportSQL(): ",  se);
                throw new SMRCException("This report has timed out.  Please hit the back button and change your report filters to be more specific.");
            }
            SMRCLogger.error("CMSReportDAO.executeReportSQL(): SQL: " + sql);
            SMRCLogger.error("CMSReportDAO.executeReportSQL(): ", se);
            throw se;
            
        }catch (Exception e)	{
        		SMRCLogger.error("ReportDAO.executeReportSQL(): SQL: " + sql);
                SMRCLogger.error("ReportDAO.executeReportSQL(): ", e);
                throw e;
        }
        finally {
                SMRCConnectionPoolUtils.close(rs);
                SMRCConnectionPoolUtils.close(stmt);
        }

        return results;
	}
	
}