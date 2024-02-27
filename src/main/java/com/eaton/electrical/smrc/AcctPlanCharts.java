//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.24  2005/02/11 19:07:27  lubbejd
// Remove calls to MiscDAO.saveAPLog() from everywhere, and added the call
// to SMRCBaseServlet to update with every forward to a jsp
//
// Revision 1.23  2005/02/10 18:43:10  lubbejd
// Changed MiscDAO.saveAPLog() to use User object instead of userid string
// so it can call UserDAO.getGeogForUser for storing the sp_geog on the activity
// log.
//
// Revision 1.22  2005/01/10 22:17:24  vendejp
// code cleanup
//
// Revision 1.21  2005/01/10 03:00:24  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.20  2005/01/09 05:59:55  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.19  2005/01/05 22:40:25  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.18  2004/12/23 18:12:50  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.17  2004/10/19 14:36:41  schweks
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
import com.eaton.electrical.smrc.exception.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

/**
 * This is the reporting servlet for the OEM Account Planning application. Split
 * off from OEMAcctPlan on 1/28 so each one would be smaller
 * 
 * @author Carl Abel
 * @date January 28, 2002
 */
public class AcctPlanCharts extends SMRCBaseServlet {


	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Connection DBConn = null;

		String sFwdUrl = "/SMRCErrorPage.jsp";
		boolean redirect = false;
		User user = null;

		try {
			DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();

			user = SMRCSession.getUser(request, DBConn);
			String userid = user.getUserid();

			String page = "charts";
			SalesGroup sGroup = new SalesGroup();

			HeaderBean hdr = new HeaderBean();
			hdr.setPage(page);
			hdr.setPopup(true);
			hdr.setUser(user);
			hdr.setThisGroup(sGroup);
			request.setAttribute("header", hdr);

			printChart(request, userid);
			sFwdUrl = "/AcctPlanCharts.jsp";

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

	private void printChart(HttpServletRequest request, String userid) {
		StringBuffer parms = new StringBuffer("");

		Enumeration parmNames = request.getParameterNames();
		boolean useAnd = false;

		while (parmNames.hasMoreElements()) {
			String nm = (String) parmNames.nextElement();

			if (useAnd) {
				parms.append("&");
			} else {
				useAnd = true;
			}

			parms.append(nm);
			parms.append("=");
			parms.append(request.getParameter(nm));
		}

		String newURL = "AcctPlanStdRptChart?userid=" + userid + "&" + parms.toString();
		request.setAttribute("newURL", newURL);

	}

} //class