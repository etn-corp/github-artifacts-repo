//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.24  2005/11/23 16:21:45  e0073445
// Added pagination functionality.  Corrected issue that would only query the first 101 values.
//
// Revision 1.23  2005/11/17 14:52:34  lubbejd
// Change customer browsing to limit results to active distributors if necessary.
//
// Revision 1.22  2005/01/10 03:00:24  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.21  2005/01/09 05:59:55  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.20  2005/01/05 22:40:25  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.19  2004/12/23 18:12:51  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.18  2004/11/01 15:20:33  vendejp
// Made changes to CusomerBrowse page uses 1 query with joins rather than looping and doing another query for an account per record
//
// Revision 1.17  2004/10/19 14:51:03  schweks
// Removing unused variables and code.
//
// Revision 1.16  2004/10/16 18:14:58  manupvj
// Added abstract servlet for common functions and common point of logging.
//
// Revision 1.15  2004/10/14 17:40:50  schweks
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

public class CustomerBrowse extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sFwdUrl = "/SMRCErrorPage.jsp";
        boolean redirect = false;
        Connection DBConn = null;
        User user = null;
        
        try {
            DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();

            user = SMRCSession.getUser(request, DBConn);

            SMRCHeaderBean hdr = new SMRCHeaderBean();
            hdr.setUser(user);
            request.setAttribute("header", hdr);
            request.setAttribute("fwdPage", request.getParameter("fwdPage"));

            String page = StringManipulation.noNull(request
                    .getParameter("page"));

            if (page.equalsIgnoreCase("save")) {
                DistributorDAO.saveKeyAccount(StringManipulation.noNull(request
                        .getParameter("distId")), StringManipulation
                        .noNull(request.getParameter("keyAcct")), DBConn);
                request.setAttribute("fwdPage", "refreshApp4");
                sFwdUrl = "/closeWindow.jsp";

            } else if (page.equalsIgnoreCase("saveApproval")) {
                DistributorDAO.saveDistributorsImpacted(StringManipulation
                        .noNull(request.getParameter("distId")),
                        StringManipulation.noNull(request
                                .getParameter("keyAcct")), DBConn);
                request.setAttribute("fwdPage", "refreshApproval");
                sFwdUrl = "/closeWindow.jsp";

            } else if (page.equalsIgnoreCase("search")) {
            	
            	// Get the index for what page of results we are suposed to be displaying
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
            	
            	// Get the records 
            	
            	ArrayList records = AccountDAO.accountBrowseSearch(StringManipulation.noNull(request.getParameter("ACCOUNT_NAME")),StringManipulation.noNull(request.getParameter("VISTA_CUSTOMER_NUMBER")),DBConn,false, firstIndex);
                request.setAttribute("records", records);
                sFwdUrl = "/customerBrowse.jsp";

            } else {
                sFwdUrl = "/customerBrowse.jsp";

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

} //class