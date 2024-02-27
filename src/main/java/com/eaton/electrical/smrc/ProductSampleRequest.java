// Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.25  2005/01/28 22:25:36  vendejp
// Changed product sample request to get email to send to from TAPmail.properties rather than from db
//
// Revision 1.24  2005/01/13 18:52:36  lubbejd
// Changes to use srYear instead of current date
//
// Revision 1.23  2005/01/12 05:04:07  schweks
// Changed new Integer().intValue() occurrences to Globals.a2int() instead.
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
// Revision 1.19  2005/01/06 22:27:37  schweks
// Updated to handle refresh appropriately.
//
// Revision 1.18  2005/01/05 22:40:25  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.17  2005/01/05 21:02:00  vendejp
// Changes for log4j logging and exception handling
//
// Revision 1.16  2005/01/03 05:39:18  schweks
// Several changes to get the sample request to work.
//
// Revision 1.15  2004/12/30 16:20:03  lubbejd
// Changes to prevent errors caused by a customer not having a geography and therefore not having a pricing manager.
//
// Revision 1.14  2004/12/23 18:12:51  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.13  2004/11/15 13:36:17  schweks
// Modified to handle new requirements.
//
// Revision 1.12  2004/11/09 16:45:15  schweks
// Most of the way there.
//
// Revision 1.11  2004/11/03 04:33:14  schweks
// Round 1.
//
// Revision 1.10  2004/10/27 15:59:53  vendejp
// *** empty log message ***
//
// Revision 1.9  2004/10/19 14:59:11  schweks
// Removing unused variables and code.
//
// Revision 1.8  2004/10/18 20:10:33  schweks
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

public class ProductSampleRequest extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sFwdUrl = "/SMRCErrorPage.jsp";
        Connection DBConn = null;
        boolean redirect = false;
        User user = null;

        try {
            DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();
           
            //Vince: I get nervous passing request to this method. What does
            // it do?
            user = SMRCSession.getUser(request, DBConn);
            request.setAttribute("user", user);

            //page has multiple behaviors so what is it that we are doing now?
            // Default to display on null action.
            String page = request.getParameter("page") == null ? "display" : request.getParameter("page");

            SMRCHeaderBean hdr = new SMRCHeaderBean();
            hdr.setUser(user);

            if (request.getParameter("acctId") != null) {
            	Account thisAccount = AccountDAO.getAccount(request.getParameter("acctId"), DBConn); 
                hdr.setAccount( thisAccount );

                // getSalesEngineer() returns the Vista ID...
                request.setAttribute( "salesEngineerUser", UserDAO.getUserBySalesId( thisAccount.getSalesEngineer1(), DBConn ) );
            }

            request.setAttribute("header", hdr);
            request.setAttribute("fwdPage", request.getParameter("fwdPage"));

            if (page.equalsIgnoreCase("display")) {
                writeSampleRequest(request, user, DBConn);
                sFwdUrl = "/productSampleRequest.jsp";
            } else if (page.equalsIgnoreCase("refresh")) {
                request.setAttribute("use", request.getParameter( "use" ));
                request.setAttribute("contactId", request.getParameter( "contactId" ));
                request.setAttribute("contactOverride", request.getParameter( "contactOverride" ));
                request.setAttribute("qty1", request.getParameter( "qty1" ));
                request.setAttribute("cat1", request.getParameter( "cat1" ));
                request.setAttribute("qty2", request.getParameter( "qty2" ));
                request.setAttribute("cat2", request.getParameter( "cat2" ));
                request.setAttribute("qty3", request.getParameter( "qty3" ));
                request.setAttribute("cat3", request.getParameter( "cat3" ));
                request.setAttribute("qty4", request.getParameter( "qty4" ));
                request.setAttribute("cat4", request.getParameter( "cat4" ));
                request.setAttribute("qty5", request.getParameter( "qty5" ));
                request.setAttribute("cat5", request.getParameter( "cat5" ));
                request.setAttribute("qty6", request.getParameter( "qty6" ));
                request.setAttribute("cat6", request.getParameter( "cat6" ));
                request.setAttribute("shipTo", request.getParameter( "shipTo" ));
                request.setAttribute("attn", request.getParameter( "attn" ));
                request.setAttribute("addr1", request.getParameter( "addr1" ));
                request.setAttribute("addr2", request.getParameter( "addr2" ));
                request.setAttribute("addr3", request.getParameter( "addr3" ));
                request.setAttribute("city", request.getParameter( "city" ));
                request.setAttribute("state", request.getParameter( "state" ));
                request.setAttribute("zip", request.getParameter( "zip" ));
                request.setAttribute("shipby", request.getParameter( "shipby" ));
                request.setAttribute("acct", request.getParameter( "acct" ));

                writeSampleRequest(request, user, DBConn);
                sFwdUrl = "/productSampleRequest.jsp";
            } else if (page.equalsIgnoreCase("sendSample")) {
                request.setAttribute("acctId", request.getParameter("acctId"));
                sendSampleRequestEmail(request, user);
                sFwdUrl = "/productSampleRequestConfirmation.jsp";
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

    private void writeSampleRequest(HttpServletRequest aRequest, User aUser,
            Connection DBConn) throws Exception {

        String customerId = aRequest.getParameter("acctId");
        Customer customer = AccountDAO.getOneCustomer(customerId, DBConn);
        
        HttpSession session = aRequest.getSession(false);
        int srYear = Globals.a2int((String) session.getAttribute("srYear"));
//      If the year is zero, something went wrong... set to current year
	     if ( srYear == 0 ) {
	     	 Calendar cal = new GregorianCalendar();
	         // Get the components of the date
	         srYear = cal.get(Calendar.YEAR);
	     }
        ArrayList products = ProductDAO.getProductsCurrentYear(srYear, DBConn);
        ArrayList contacts = ContactsDAO.getContacts(aRequest.getParameter("acctId"), DBConn);

        if (contacts == null) {
            SMRCLogger.info(this.getClass().getName() + "contacts are null for customer" + customerId);
            contacts = new ArrayList();
        }

        aRequest.setAttribute("user", aUser);
        aRequest.setAttribute("contacts", contacts);
        aRequest.setAttribute("customer", customer);
        aRequest.setAttribute("products", products);

    } //method

    private void sendSampleRequestEmail(HttpServletRequest aRequest, User aUser) throws Exception {

        try {
            SampleRequestEmail email = new SampleRequestEmail();
            email.setPotentialDol(aRequest.getParameter("potDol"));
            email.setActualDol(aRequest.getParameter("actDol"));
            email.setCompetDol(aRequest.getParameter("competDol"));
            email.setForecastDol(aRequest.getParameter("forecastDol"));
            email.setUse(aRequest.getParameter("use"));
            email.setCust(aRequest.getParameter("acctId"));
            email.setShipMethod(aRequest.getParameter("shipby"));
            email.setAcctNum(aRequest.getParameter("acct"));
            //email.setPricingManager(aRequest.getParameter("pmid"));
            email.setRequestor(aUser.getUserid());
            email.setContactOverride(aRequest.getParameter("contactOverride"));

            int stage = 0;

            if (aRequest.getParameter("stage") != null) {
                stage = Globals.a2int(aRequest.getParameter("stage"));
            }

            email.setStage(stage);

            int contact = 0;

            if (aRequest.getParameter("contactId") != null) {
                contact = Globals.a2int(aRequest.getParameter("contactId"));
            }

            email.setContact(contact);

            int qty = 0;
            for (int i = 1; i < 9; i++) {
                if (aRequest.getParameter("qty" + i) != null
                        && aRequest.getParameter("cat" + i) != null
                        && aRequest.getParameter("qty" + i).length() > 0
                        && aRequest.getParameter("cat" + i).length() > 0) {
                    qty = Globals.a2int(aRequest.getParameter("qty" + i));
                    email.addSample(qty, aRequest.getParameter("cat" + i));
                }
            }

            email.setShipTo(aRequest.getParameter("attn"), aRequest.getParameter("addr1"), aRequest.getParameter("addr2"),
                    aRequest.getParameter("addr3"), aRequest.getParameter("city"), aRequest.getParameter("state"), 
                    aRequest.getParameter("zip"));
            email.send();
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".sendSampleRequestEmail() ", e);
            throw e;
        }

    } //method

}