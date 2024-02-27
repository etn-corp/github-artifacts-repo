//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.20  2005/01/19 20:54:38  vendejp
// added code that sets anchor tag in session for refreshes
//
// Revision 1.19  2005/01/10 03:00:24  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.18  2005/01/09 05:59:54  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.17  2005/01/05 22:40:25  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.16  2004/12/23 18:12:50  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.15  2004/10/19 15:11:25  schweks
// Removing unused variables and code.
//
// Revision 1.14  2004/10/18 20:10:33  schweks
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
public class ZipcodeBrowse extends SMRCBaseServlet {

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

            SMRCHeaderBean hdr = new SMRCHeaderBean();
            hdr.setUser(user);

            request.setAttribute("header", hdr);

            ArrayList zipResults = new ArrayList();

            if (page.equals("save")) {
                session.setAttribute("zip", StringManipulation.noNull(request.getParameter("zip")));
                session.setAttribute("city", StringManipulation.noNull(request.getParameter("city")));
                session.setAttribute("state", StringManipulation.noNull(request.getParameter("state")));
                session.setAttribute("addr", StringManipulation.noNull(request.getParameter("addr")));
                session.setAttribute("anchor", StringManipulation.noNull(request.getParameter("addr")));
                sFwdUrl = "/closeWindow.jsp";

            } else {
                if (!searchString.equals("")) {
                    zipResults = MiscDAO.getZipCodes(searchString, DBConn);
                }

                request.setAttribute("zipResults", zipResults);
                request.setAttribute("searchString", searchString);

                sFwdUrl = "/zipcode_browse.jsp";
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
