// Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.16  2005/11/23 16:21:45  e0073445
// Added pagination functionality.  Corrected issue that would only query the first 101 values.
//
// Revision 1.15  2005/11/17 14:52:34  lubbejd
// Change customer browsing to limit results to active distributors if necessary.
//
// Revision 1.14  2005/06/28 18:39:20  lubbejd
// merged changes from QA_1_2_1.
//
// Revision 1.13.6.1  2005/06/22 17:18:12  lubbejd
// Changed account ArrayList to use Strings instead Account objects.
//
// Revision 1.13  2005/01/12 04:50:43  schweks
// Changed to use Globals.a2int instead.
//
// Revision 1.12  2005/01/10 03:00:24  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.11  2005/01/09 05:59:54  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.10  2005/01/05 22:40:24  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.9  2004/12/23 18:12:50  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.8  2004/11/03 20:21:05  lubbejd
// use AccountBrowseRecord instead of Account object in TargetMarketCustomerBrowse
//
// Revision 1.7  2004/11/01 15:47:43  vendejp
// backed up some changes I just made that ended up in a bug
//
// Revision 1.6  2004/10/19 15:11:25  schweks
// Removing unused variables and code.
//
// Revision 1.5  2004/10/18 20:10:33  schweks
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

public class TargetMarketCustomerBrowse extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sFwdUrl = "/SMRCErrorPage.jsp";
        boolean redirect = false;
        Connection DBConn = null;
        javax.servlet.http.HttpSession session;
        User user = null;
        
        try {
            DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();
            boolean distributorSearch = false;
            session = request.getSession();
            user = SMRCSession.getUser(request, DBConn);

            SMRCHeaderBean hdr = new SMRCHeaderBean();
            hdr.setUser(user);
            request.setAttribute("header", hdr);
            request.setAttribute("fwdPage", request.getParameter("fwdPage"));

            String page = StringManipulation.noNull(request
                    .getParameter("page"));
            // Determines if we're updating other distributors or end customers
            String calledFor = "";
            if (request.getParameter("type") != null) {
                calledFor = request.getParameter("type");
            } else if (session.getAttribute("calledFor") != null) {
                calledFor = (String) session.getAttribute("calledFor");
                session.removeAttribute("calledFor");
            }
            if (calledFor.equalsIgnoreCase("dist")){
            	distributorSearch = true;
            }
            request.setAttribute("calledFor", calledFor);
            int tmId = Globals.a2int(StringManipulation.noNull(request.getParameter("tmId")));

            ArrayList tmAccounts = new ArrayList();
            
            if (tmId > 0) {
                if (calledFor.equalsIgnoreCase("dist")) {
                    tmAccounts = TargetMarketDAO.getTMOtherAccounts(tmId,DBConn);
                } else {
                    tmAccounts = TargetMarketDAO.getTMEndCustomers(tmId, DBConn);
                }
            }
            ArrayList tmAccountBeans = new ArrayList();
            for (int i=0; i< tmAccounts.size(); i++){
            	String acctId = (String) tmAccounts.get(i);
            	DropDownBean bean = new DropDownBean();
            	bean.setValue(acctId);
            	bean.setName(AccountDAO.getAccountName(acctId,DBConn));
            	tmAccountBeans.add(bean);
            }

            request.setAttribute("tmAccountBeans", tmAccountBeans);
            request.setAttribute("tmId", new Integer(tmId));

            if (page.equalsIgnoreCase("search")) {
            	
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
            	
                ArrayList accounts = AccountDAO.accountBrowseSearch(StringManipulation.noNull(request.getParameter("ACCOUNT_NAME")),StringManipulation.noNull(request.getParameter("VISTA_CUSTOMER_NUMBER")),DBConn,distributorSearch, firstIndex);
                request.setAttribute("accounts", accounts);
                sFwdUrl = "/targetMarketCustomerBrowse.jsp";

            } else {
                sFwdUrl = "/targetMarketCustomerBrowse.jsp";

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
