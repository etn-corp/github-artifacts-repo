//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.26  2005/11/23 16:21:45  e0073445
// Added pagination functionality.  Corrected issue that would only query the first 101 values.
//
// Revision 1.25  2005/02/04 20:02:35  lubbejd
// Add Sales Engineer filter to bid tracker reports, using the existing SEBrowse Servlet
// (CR27592)
//
// Revision 1.24  2005/01/19 20:54:38  vendejp
// added code that sets anchor tag in session for refreshes
//
// Revision 1.23  2005/01/10 03:00:23  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.22  2005/01/09 05:59:54  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.21  2005/01/05 22:40:24  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.20  2004/12/23 18:12:49  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.19  2004/10/19 14:59:11  schweks
// Removing unused variables and code.
//
// Revision 1.18  2004/10/18 20:10:33  schweks
// Added Eaton header comment.
// Reformatted source code.
//

package com.eaton.electrical.smrc;

import java.io.*;
import java.sql.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.exception.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

/**
 * 
 * @author Jason Lubbert
 */
public class SEBrowse extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sFwdUrl = "/SMRCErrorPage.jsp";
        boolean redirect = false;
        Connection DBConn = null;
        User user = null;
        boolean bidmanReports = false;
        ArrayList selectedSEs = new ArrayList();
        if (request.getParameter("callPage") != null){
            if (request.getParameter("callPage").equalsIgnoreCase("bidmanReports")){
                bidmanReports = true;
            }
        }
        try {
            DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();
            HttpSession session = request.getSession(true);
            user = SMRCSession.getUser(request, DBConn);

            String page = StringManipulation.noNull(request
                    .getParameter("page"));
            if (page.equalsIgnoreCase("saveSE") && !bidmanReports) {
                session.setAttribute("se"
                        + StringManipulation.noNull(request.getParameter("se")),
                        StringManipulation.noNull(request.getParameter("seId")));
                if (StringManipulation.noNull(request.getParameter("se")).equals("1")) {
                    session.setAttribute("seGeog", StringManipulation.noNull(request.getParameter("seGeog")));
                }
                session.setAttribute("anchor", "salesengineer");
                sFwdUrl = "/closeWindow.jsp";
               
            } else {
                boolean refreshCaller = false;
                if ((bidmanReports) && page.equalsIgnoreCase("saveSE")){
                    if (session.getAttribute("selectedSEs") != null){
                        selectedSEs = (ArrayList) session.getAttribute("selectedSEs");
                    }
                    String thisSE = StringManipulation.noNull(request.getParameter("seId"));
                    selectedSEs.add(thisSE);
                    session.setAttribute("selectedSEs", selectedSEs);
                    refreshCaller = true;
                    
                }

                String searchString = StringManipulation.noNull(request.getParameter("searchString"));

                SMRCHeaderBean hdr = new SMRCHeaderBean();
                hdr.setUser(user);

                request.setAttribute("header", hdr);
                // Get the current index
                
              	int firstIndex = 1;
            	String stringFirstIndex = (String)request.getParameter("recNum");
            	
            	try {
            	
            		if ((stringFirstIndex != null) && (stringFirstIndex.trim().length() > 0)) {
            			
            			firstIndex = Integer.parseInt(stringFirstIndex);
            			
            		}
 
            	} catch (NumberFormatException NFE) {
            		
            		// Missing or invalid type.  Stick with the defualt value but log it so we can fix          	
           		
    	            SMRCLogger.error(this.getClass().getName() + 
    		            	".doGet(): Unable to parse next page value.  Using defualt but this is not correct.  MESSAGE=" + NFE.getMessage(), NFE);
            	}

                ArrayList seResults = new ArrayList();
                if (request.getParameter("seid") != null
                        && !request.getParameter("seid").equals("")) {
                    seResults = SalesDAO.browseSE(request.getParameter("seid"),
                            "seid", DBConn, firstIndex);
                } else if (request.getParameter("submittingSearch") != null) {
                    seResults = SalesDAO.browseSE(createWildcards(searchString), "name", DBConn, firstIndex);
                }

                request.setAttribute("seResults", seResults);
                request.setAttribute("searchString", searchString);
                request.setAttribute("bidmanReports", new Boolean(bidmanReports));
                request.setAttribute("refreshCaller", new Boolean(refreshCaller));
                
                sFwdUrl = "/se_browse.jsp";
            }

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
    
    private static String createWildcards(String inString){
	    StringBuffer returnValue = new StringBuffer();
        for (int i=0; i< inString.length(); i++){
            char thisChar = inString.charAt(i);
            if (thisChar != '*'){
                returnValue.append(thisChar);
            } else {
                returnValue.append("%");
            }
        }
        return returnValue.toString();
	}

} //class
