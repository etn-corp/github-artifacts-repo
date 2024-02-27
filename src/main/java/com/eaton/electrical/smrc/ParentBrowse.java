//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.24  2006/01/05 20:04:40  e0073445
// Added Parent Name to the Parent Number on Account Profile page
//
// Revision 1.23  2005/11/23 16:21:45  e0073445
// Added pagination functionality.  Corrected issue that would only query the first 101 values.
//
// Revision 1.22  2005/01/19 20:54:38  vendejp
// added code that sets anchor tag in session for refreshes
//
// Revision 1.21  2005/01/10 03:00:24  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.20  2005/01/09 05:59:55  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.19  2005/01/05 22:40:26  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.18  2004/12/23 18:12:51  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.17  2004/11/01 15:20:38  vendejp
// *** empty log message ***
//
// Revision 1.16  2004/10/19 14:59:11  schweks
// Removing unused variables and code.
//
// Revision 1.15  2004/10/18 20:10:33  schweks
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
public class ParentBrowse extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sFwdUrl = "/SMRCErrorPage.jsp";
        boolean redirect = false;
        Connection DBConn = null;
        User user = null;
        
        try {
            DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();

            HttpSession session = request.getSession(true);

            user = SMRCSession.getUser(request, DBConn);

            String page = StringManipulation.noNull(request.getParameter("page"));
            String searchString = StringManipulation.noNull(request.getParameter("searchString"));

            if (page.equalsIgnoreCase("saveParent")) {
                String parentId = StringManipulation.noNull(request.getParameter("parent"));
            	session.setAttribute("parentNum", parentId);
                
                // Get the name for the session
                Parent parent = ParentDAO.getAccount(parentId, DBConn);
                session.setAttribute("parentName", parent.getParentName());
                session.setAttribute("anchor", "parent");
                sFwdUrl = "/closeWindow.jsp";
            } else {

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

                ArrayList customerResults = new ArrayList();

                if (request.getParameter("submittingSearch") != null) {
                    customerResults = AccountDAO.parentSearch(searchString, DBConn, firstIndex);
                }

                request.setAttribute("customerResults", customerResults);
                request.setAttribute("searchString", searchString);

                sFwdUrl = "/parent_browse.jsp";

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
