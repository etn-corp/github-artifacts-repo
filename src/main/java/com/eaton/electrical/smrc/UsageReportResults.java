//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.4  2005/02/16 14:21:30  lubbejd
// CR27591 - I forgot to paginate the Usage report
//
// Revision 1.3  2005/02/16 12:59:01  lubbejd
// Addition of Usage report (CR27591). AcctPlanOtherReports.jsp will need to have
// the breakdate changed to March 1st before moving to production.
//
// Revision 1.2  2005/02/15 20:26:09  lubbejd
// Changes for Usage Report
//
// Revision 1.1  2005/02/15 19:25:47  lubbejd
// Changes for adding Usage report to Other reports
//


package com.eaton.electrical.smrc;

import java.io.*;
import java.sql.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;
import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.exception.*;

/**
 * 
 * @author Jason Lubbert
 */
public class UsageReportResults extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = null;
        Connection DBConn = null;
        String sFwdUrl = "/SMRCErrorPage.jsp";

        boolean redirect = false;
        
        try {
            DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();

            user = SMRCSession.getUser(request, DBConn);

            SMRCHeaderBean hdr = new SMRCHeaderBean();
            hdr.setUser(user);
            request.setAttribute("header", hdr);
            writeResultsPage(request, DBConn);
            sFwdUrl = "/usageReportResults.jsp";

            SMRCConnectionPoolUtils.commitTransaction(DBConn);
        } catch (ProfileException pe) {
        	// See if we can get the user info for the log message.
        	if ( user != null ) {
	        	// Put as much on the first line as possible for NetCool
	            SMRCLogger.warn(this.getClass().getName() + 
	            	".doGet(): USERID=" + user.getUserid() + 
					"; MESSAGE=" + pe.getMessage() + 
					"\nUSER:" + user, pe);
        	} else {
	        	// Put as much on the first line as possible for NetCool
	            SMRCLogger.warn(this.getClass().getName() + 
	            	".doGet(): MESSAGE=" + pe.getMessage(), pe);
        	}
        	
            SMRCConnectionPoolUtils.rollbackTransaction(DBConn);

            request.setAttribute("exception", pe.getMessage());
            sFwdUrl = "/SMRCErrorPage.jsp";
            redirect = false;

        } catch (Exception e) {
        	// See if we can get the user info for the log message.
        	if ( user != null ) {
	        	// Put as much on the first line as possible for NetCool
	            SMRCLogger.error(this.getClass().getName() + 
	                	".doGet(): USERID=" + user.getUserid() + 
	    				"; MESSAGE=" + e.getMessage() + 
	    				"\nUSER:" + user, e);
        	} else {
	        	// Put as much on the first line as possible for NetCool
	            SMRCLogger.error(this.getClass().getName() + 
	            	".doGet(): MESSAGE=" + e.getMessage(), e);
        	}

        	SMRCConnectionPoolUtils.rollbackTransaction(DBConn);

            request.setAttribute("exception", e.getMessage());
            sFwdUrl = "/SMRCErrorPage.jsp";
            redirect = false;

        } finally {
            SMRCConnectionPoolUtils.close(DBConn);
            gotoPage(sFwdUrl, request, response, redirect);
        }

    }
    
    private void writeResultsPage(HttpServletRequest request, Connection DBConn) throws Exception {
        String geogFilter = "";
        String dateFilter = "";
        
        String groupBy = "";
        StringBuffer filterString = new StringBuffer();
        
        User user = SMRCSession.getUser(request, DBConn);
        
        try {
            
            int beginWith = 0;
            int endWith = 50;
            
            if (request.getParameter("beginWith") != null) {
                beginWith = Globals.a2int(request.getParameter("beginWith"));
            }
            if (request.getParameter("endWith") != null) {
                endWith = Globals.a2int(request.getParameter("endWith"));
            }

            if (request.getParameter("prevPage") != null) {
                beginWith -= 50;
                endWith -= 50;
            }

            if (request.getParameter("nextPage") != null) {
                beginWith += 50;
                endWith += 50;
            }
            
            if (request.getParameter("usageGroupBy") != null){
                if (request.getParameter("usageGroupBy").equalsIgnoreCase("usageGroupByMonth")){
                    groupBy = "Month/Year";
                } else if (request.getParameter("usageGroupBy").equalsIgnoreCase("usageGroupByZone")){
                    groupBy = "Zone";
                } else if (request.getParameter("usageGroupBy").equalsIgnoreCase("usageGroupByDistrict")){
                    groupBy = "District";
                } else if (request.getParameter("usageGroupBy").equalsIgnoreCase("usageGroupByTeam")){
                    groupBy = "Team";
                } else if (request.getParameter("usageGroupBy").equalsIgnoreCase("usageGroupByUser")){
                    groupBy = "User";
                }
            }
            
            if (request.getParameter("usageDateFilter") != null) {
                if (request.getParameterValues("usageDateFilter").length > 0
                        && !request.getParameterValues("usageDateFilter")[0].equals("0")) {
                    dateFilter = request.getParameterValues("usageDateFilter")[0];
                    filterString.append("<br><b>Date: </b>" + dateFilter.substring(4,dateFilter.length()) + "/" + dateFilter.substring(0,4));
                }
            }
            
            if (request.getParameter("usageGeogFilter") != null) {
                if (request.getParameterValues("usageGeogFilter").length > 0
                        && !request.getParameterValues("usageGeogFilter")[0].equals("0")) {
                    geogFilter = request.getParameterValues("usageGeogFilter")[0];
                    filterString.append("<br><b>Geography: </b>" + DistrictDAO.getDistrictName(geogFilter, DBConn));
                }
            }
            
           
            String sortBy = "description";
            String sortDir = "asc"; 
            if (request.getParameter("sortBy") != null){
                sortBy = request.getParameter("sortBy");
            }
            if (request.getParameter("sortDir") != null){
                sortDir = request.getParameter("sortDir");
            }

            String noResultMessage = "There is currently no usage data for the filters you have selected for this report";
            ArrayList reportBeans = new ArrayList();
            
            boolean runReport = true;
            boolean hasSegmentOverride = false;
         	ArrayList segmentOverrideList = user.getSegmentOverrides();
         	if (segmentOverrideList.size() > 0){
         		hasSegmentOverride = true;
         	}
            if (!user.ableToViewEverything() && !user.hasOverrideSecurity() && !hasSegmentOverride){
                String level = "";
          	    if (groupBy.equalsIgnoreCase("team")){
          	       		level = "TEAM";
                } else if (groupBy.equalsIgnoreCase("district")){
                		level = "DISTRICT";
                } else if (groupBy.equalsIgnoreCase("zone")){
                		level = "ZONE";
                } else if (groupBy.equalsIgnoreCase("user")){
                        level = "TEAM";
                }
                if (!user.ableToSeeThisLevel(geogFilter, level)){
                    noResultMessage = "Your security does not allow you to group by " + groupBy + " for " + DistrictDAO.getDistrictName(geogFilter, DBConn);
                    runReport = false;
                } 
            } 
            
            if (runReport){
                reportBeans = ActivityDAO.getUsageReportResults(groupBy, geogFilter, dateFilter, sortBy, 
                        sortDir, user, beginWith, endWith, DBConn);
            }
            
            
            if (filterString.length() == 0){
                filterString.append("No filters selected");
            }
            
            
            // Remove previous occurrance of sortBy and sortDir
            String sortQueryString = changeQueryString(request.getQueryString(), "sortBy");
            sortQueryString = changeQueryString(sortQueryString, "sortDir");
            // Remove previous occurrance of endWith and beginWith
            String paginateQueryString = changeQueryString(request.getQueryString(), "endWith");
            paginateQueryString = changeQueryString(paginateQueryString, "beginWith");
            paginateQueryString = changeQueryString(paginateQueryString, "prevPage");
            paginateQueryString = changeQueryString(paginateQueryString, "nextPage");
            
            request.setAttribute("reportBeans", reportBeans);
            if (reportBeans.size() < 50) {
                request.setAttribute("lastPage", new Boolean(true));
            } else {
                request.setAttribute("lastPage", new Boolean(false));
            }
            request.setAttribute("filterString", filterString.toString());
            request.setAttribute("groupBy", groupBy);
            request.setAttribute("sortQueryString", sortQueryString);
            request.setAttribute("paginateQueryString", paginateQueryString);
            request.setAttribute("sortDir", sortDir);
            request.setAttribute("noResultMessage", noResultMessage);
            request.setAttribute("beginWith", new Integer(beginWith));
            request.setAttribute("endWith", new Integer(endWith));
                                    
            
        }catch (Exception e)	{
                SMRCLogger.error("UsageReportResults.writeResultsPage(): ", e);
                throw e;
        }
        
    }
    
    private String changeQueryString (String queryString, String removeParam){
        StringBuffer newString = new StringBuffer();
        int paramLoc = queryString.indexOf(removeParam);
        int nextParamLoc = -1;
        if (paramLoc > -1){
            nextParamLoc = queryString.indexOf("&", paramLoc);
        }
        if (paramLoc > -1){
            if (nextParamLoc > -1){
                newString.append(queryString.substring(0,paramLoc) + queryString.substring(nextParamLoc));
            } else {
                newString.append(queryString.substring(0,paramLoc));
            }
        } else {
            if (nextParamLoc > -1){
                newString.append(queryString.substring(nextParamLoc));
            } else {
                newString.append(queryString);
            }
        }
        return newString.toString();
        
        
        
    }
    
  

}