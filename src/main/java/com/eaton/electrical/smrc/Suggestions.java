//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.23  2005/02/11 19:07:27  lubbejd
// Remove calls to MiscDAO.saveAPLog() from everywhere, and added the call
// to SMRCBaseServlet to update with every forward to a jsp
//
// Revision 1.22  2005/02/10 18:43:11  lubbejd
// Changed MiscDAO.saveAPLog() to use User object instead of userid string
// so it can call UserDAO.getGeogForUser for storing the sp_geog on the activity
// log.
//
// Revision 1.21  2005/01/13 12:54:40  lubbejd
// Added use of StringManipulation.fixQuotes() for suggestion message.
//
// Revision 1.20  2005/01/10 22:17:24  vendejp
// code cleanup
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
// Revision 1.15  2004/10/19 14:59:11  schweks
// Removing unused variables and code.
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
 * @author Carl Abel
 * @date June 17, 2003
 *  
 */
public class Suggestions extends SMRCBaseServlet {

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

			String page = "suggestions";

			HeaderBean hdr = new HeaderBean();
			hdr.setPage(page);
			hdr.setHelpPage(TAPcommon.getHelpPage(page, DBConn));

			ArrayList tmp = new ArrayList(1);
			hdr.setGroups(tmp);
			hdr.setUser(user);
			hdr.setPopup(false);

			if (userid == null) { // User does not have a portal session
				throw new Exception("Portal Session is Expired");
			}
			sFwdUrl = "/Suggestions.jsp";
			request.setAttribute("header", hdr);
			printPage(request, DBConn);
			

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

	private void printPage(HttpServletRequest request, Connection aConnection) throws java.io.IOException, Exception {
		
		User user = SMRCSession.getUser(request, aConnection);
		String action = "";

		if (request.getParameter("action") != null) {
			action = request.getParameter("action");
		}

		if (action.equals("save")) {
			String suggestion = request.getParameter("suggestion");

			if (saveSuggestion(aConnection, user, suggestion)) {
				request.setAttribute("topMessage",
						"Your suggestion was submitted successfully.");
			} else {
				request.setAttribute("topMessage",
						"An error occurred processing your suggestion. Contact IT Support.");
			}
		}

	}

	private boolean saveSuggestion(Connection aConnection, User user, String suggestion)
			throws Exception {
		if (suggestion.length() > 4000) {
			suggestion = suggestion.substring(0, 3999);
		}

		String ins = "insert into suggestions "
				+ "(sugg_id, description, user_added, date_added) "
				+ "values (suggestions_seq.nextVal, '" + StringManipulation.fixQuotes(suggestion) + "','"
				+ user.getUserid() + "',sysdate)";
		Statement s = null;
		try {
			s = aConnection.createStatement();
			s.executeUpdate(ins);
		} catch (Exception e) {
			SMRCLogger.error(this.getClass().getName() + ".saveSuggestion() ",
					e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(s);
		}

		char returncode = 13;
		StringBuffer text = new StringBuffer("");
		text.append(user.getFirstName());
		text.append(" ");
		text.append(user.getLastName());
		text
				.append(" has made the following suggestion for the Target Account Planner");
		text.append(returncode);
		text.append(suggestion);

		String from = user.getEmailAddress().trim();
		String to = "oemaccountplanner@eaton.com";
		
		try {

			TAPMail tm = new TAPMail();
			tm.setHtmlMessage(text.toString());
			tm.addRecipient(to);
			tm.addCCRecipient(from);
			tm.setSenderInfo(user.getFirstName() + " " + user.getLastName(),
					from);
			tm.sendMessage(text.toString(),
					"New Suggestion for the Target Account Planner");
		} catch (Exception e) {
			SMRCLogger.error(this.getClass().getName() + ".saveSuggestion() ",
					e);
			throw e;
		}

		return true;
	}

} //class
