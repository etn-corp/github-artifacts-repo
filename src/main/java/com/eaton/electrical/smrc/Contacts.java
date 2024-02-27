//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.21  2005/01/10 22:17:24  vendejp
// code cleanup
//
// Revision 1.20  2005/01/10 03:00:24  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.19  2005/01/09 05:59:54  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.18  2005/01/05 22:40:24  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.17  2004/12/23 18:12:50  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.16  2004/10/19 14:51:03  schweks
// Removing unused variables and code.
//
// Revision 1.15  2004/10/16 18:14:59  manupvj
// Added abstract servlet for common functions and common point of logging.
//
// Revision 1.14  2004/10/14 18:41:38  vendejp
// *** empty log message ***
//
// Revision 1.13  2004/10/14 17:40:51  schweks
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

public class Contacts extends SMRCBaseServlet {
	
	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String sFwdUrl="/SMRCErrorPage.jsp";
		boolean redirect=false;
		
		Connection DBConn = null;
		User user = null;
		
		try{
			DBConn = SMRCConnectionPoolUtils.getDatabaseConnection() ;
			
			user = SMRCSession.getUser(request,DBConn);
			
			SMRCHeaderBean hdr = new SMRCHeaderBean();
			hdr.setUser(user);
			request.setAttribute("header", hdr);
			request.setAttribute("fwdPage",request.getParameter("fwdPage"));
			
			String page = request.getParameter("page");	
			if(page.equalsIgnoreCase("edit") || page.equalsIgnoreCase("add") || page.equalsIgnoreCase("delete")){
				Contact contact = new Contact();
				if(page.equalsIgnoreCase("edit") || page.equalsIgnoreCase("delete")){ 
					contact = ContactsDAO.getContact(StringManipulation.noNull(request.getParameter("contactid")), DBConn);
				}
				
				TreeMap jobFunctions = MiscDAO.getJobFunctions(DBConn);
				request.setAttribute("jobFunctions",jobFunctions);
				
				ArrayList codes = MiscDAO.getCodes("functional_position", DBConn);
				request.setAttribute("codes",codes);
				
				request.setAttribute("contact",contact);
				sFwdUrl="/contactsAdd.jsp";

			}else if(page.equalsIgnoreCase("save")){
	        	Contact contact = new Contact();
	        	contact.setId(Globals.a2int(request.getParameter("contactid")));
	        	contact.setCustomer(StringManipulation.noNull(request.getParameter("acctId")));
	        	contact.setFirstName(StringManipulation.noNull(request.getParameter("FIRST_NAME")));
	        	contact.setLastName(StringManipulation.noNull(request.getParameter("LAST_NAME")));
	        	contact.setTitle(StringManipulation.noNull(request.getParameter("JOB_TITLE")));
	        	contact.setFunctionalPosition(StringManipulation.noNull(request.getParameter("POSITION")));
	        	contact.setPhoneNumber(StringManipulation.noNull(request.getParameter("PHONE")));
	        	contact.setFax(StringManipulation.noNull(request.getParameter("FAX")));
	        	contact.setComments(StringManipulation.noNull(request.getParameter("COMMENTS")));
	        	contact.setEmailAddress(StringManipulation.noNull(request.getParameter("EMAIL")));
	        	if(StringManipulation.noNull(request.getParameter("PRICING_CONTACT")).equals("Y")){
	        		contact.setPricingContact(true);
	        	}
	        	ContactsDAO.saveContact(contact, DBConn);
	        	sFwdUrl="/closeWindow.jsp";
				
			}else if(page.equalsIgnoreCase("dodelete")){
	        	ContactsDAO.deleteContact(request.getParameter("contactid"), DBConn);
	        	sFwdUrl="/closeWindow.jsp";
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
			gotoPage(sFwdUrl, request, response,redirect);
		}

	}
	
} //class
