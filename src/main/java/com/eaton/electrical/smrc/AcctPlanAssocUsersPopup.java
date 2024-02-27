//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.32  2005/04/11 14:21:39  lubbejd
// Changes to add special program filtering to standard reports (CR28590).
//
// Revision 1.31  2005/04/08 14:15:16  lubbejd
// Changes to improve and correct the associated users email process from
// the standard reports. Bug# 729. Also added some debug logging of sql
// statement execution.
//
// Revision 1.30  2005/02/11 19:07:27  lubbejd
// Remove calls to MiscDAO.saveAPLog() from everywhere, and added the call
// to SMRCBaseServlet to update with every forward to a jsp
//
// Revision 1.29  2005/02/10 18:43:10  lubbejd
// Changed MiscDAO.saveAPLog() to use User object instead of userid string
// so it can call UserDAO.getGeogForUser for storing the sp_geog on the activity
// log.
//
// Revision 1.28  2005/01/25 15:23:28  lubbejd
// Changed how we search for accounts based on the geography filter to use the
// Geography object instead of looking for the first zero in the geography string
//
// Revision 1.27  2005/01/12 05:04:07  schweks
// Changed new Integer().intValue() occurrences to Globals.a2int() instead.
//
// Revision 1.26  2005/01/10 22:17:24  vendejp
// code cleanup
//
// Revision 1.25  2005/01/10 03:00:23  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.24  2005/01/09 05:59:53  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.23  2005/01/05 22:40:23  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.22  2004/12/23 18:12:50  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.21  2004/10/26 19:26:47  lubbejd
// Change geography selection and filtering.
//
// Revision 1.20  2004/10/19 14:25:05  schweks
// Removing unused variables.
//
// Revision 1.19  2004/10/16 18:14:58  manupvj
// Added abstract servlet for common functions and common point of logging.
//
// Revision 1.18  2004/10/14 17:40:50  schweks
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
 * Allows users to send an email to all users associated to any customer
 * retrieved by the Standard Report filters.
 * 
 * @author Jason Lubbert
 */
public class AcctPlanAssocUsersPopup extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	private static final char NEWLINE = 13;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sFwdUrl = "/SMRCErrorPage.jsp";
		boolean redirect = false;

		Connection DBConn = null;
		User user = null;
		
		try {
			DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();

			user = SMRCSession.getUser(request, DBConn);

			printPage(request, user, DBConn);
			sFwdUrl = "/AcctPlanAssocUsersPopup.jsp";

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

	private void printPage(HttpServletRequest request, User aUser, Connection aConnection)
			throws IOException, Exception {

		String page = request.getParameter("page");
		
		SMRCHeaderBean hdr = new SMRCHeaderBean();
		hdr.setUser(aUser);
		request.setAttribute("header", hdr);

		if (page.equals("results")) {
			printInputPage(request);
		} else {
			setUpEmail(aUser, request, aUser, aConnection);
			String returnMessage = "Your email has been sent. If you don't receive a copy within the next hour, please contact IT Support";
			request.setAttribute("message", returnMessage);
			printInputPage(request);
		}
	}

	private void printInputPage(HttpServletRequest request) {

		Enumeration parmNames = request.getParameterNames();
		ArrayList parmNameList = new ArrayList();
		ArrayList parmVals = new ArrayList();
		while (parmNames.hasMoreElements()) {
			String name = (String) parmNames.nextElement();
			parmNameList.add(name);
			if (!(name.equals("userid") || name.equals("page"))) {
				String[] vals = request.getParameterValues(name);
				parmVals.add(vals);
			} else {
				parmVals.add(null);
			}

		} //while

		request.setAttribute("parmNameList", parmNameList);
		request.setAttribute("parmVals", parmVals);
	}

	private void setUpEmail(User user, HttpServletRequest request, User aUser,
			Connection aConnection) throws Exception {
         
	    HttpSession session = request.getSession();
		String[] productFilter = new String[0];
        String[] ftypeFilter = new String[0];
		String[] parentFilter = new String[0];
		String[] seFilter = new String[0];
		String[] applicationFilter = new String[0];
        String[] geographyFilter = new String[0];
        String[] segmentFilter = new String[0];
        String[] specialProgramFilter = new String[0];
		StringBuffer whyMessage = new StringBuffer();

		String rptYr = "0";
		String rptMY = request.getParameter("rptMonth");

		rptYr = rptMY.substring(0, 4);

		int year = Globals.a2int(rptYr);

		if (request.getParameter("ftype") != null) {
			if (request.getParameterValues("ftype").length > 0
					&& !request.getParameterValues("ftype")[0].equals("")) {
				whyMessage.append(NEWLINE + "Focus Type: " + NEWLINE);
				ftypeFilter = request.getParameterValues("ftype");
				ArrayList ftypeValues = new ArrayList();
				for (int i = 0; i < ftypeFilter.length; i++) {
					ftypeValues.add(ftypeFilter[i]);
					String ftName = MiscDAO.getFocusTypeDescription(
							(String) ftypeValues.get(i), aConnection);
					if (i > 0) {
						whyMessage.append(", ");
					}
					whyMessage.append(ftName);
				}
			}
		} //if (request.getParameter("ftype") != null)

		if (request.getParameter("product") != null) {
			if (request.getParameterValues("product").length > 0
					&& !request.getParameterValues("product")[0].equals("")) {
				whyMessage.append(NEWLINE + "Product: " + NEWLINE);
				productFilter = request.getParameterValues("product");
				ArrayList productValues = new ArrayList();
				for (int i = 0; i < productFilter.length; i++) {
					productValues.add(productFilter[i]);
					Product product = ProductDAO.productLookup(
							(String) productValues.get(i), year, aConnection);
					if (i > 0) {
						whyMessage.append(", ");
					}
					whyMessage.append(product.getDescription());
				}
			}
		}

                if (request.getParameter("applications") != null) {
			if (request.getParameterValues("applications").length > 0
					&& !request.getParameterValues("applications")[0].equals("")) {
				whyMessage.append(NEWLINE + "Application: " + NEWLINE);
				applicationFilter = request.getParameterValues("applications");
				ArrayList applicationValues = new ArrayList();
				for (int i = 0; i < applicationFilter.length; i++) {
					applicationValues.add(applicationFilter[i]);
                    int appId = Globals.a2int((String) applicationValues.get(i));
                    CodeType app = MiscDAO.getCodeById(appId, aConnection);
                    String appname = app.getDescription();
					if (i > 0) {
						whyMessage.append(", ");
					}
					whyMessage.append(appname);
				}
			}
		}
         if (request.getParameter("geography") != null) {
			if (request.getParameterValues("geography").length > 0
					&& !request.getParameterValues("geography")[0].equals("")) {
			    whyMessage.append(NEWLINE + "Geography: " + NEWLINE);
                geographyFilter = request.getParameterValues("geography");
				ArrayList geographyValues = new ArrayList();
				for (int i = 0; i < geographyFilter.length; i++) {
					geographyValues.add(geographyFilter[i]);
					String geographyname = DistrictDAO.getDistrictName(
							(String) geographyValues.get(i), aConnection);
					if (i > 0) {
						whyMessage.append(", ");
					}
					whyMessage.append(geographyname);
				}
			}
		}

		if (request.getParameter("parent") != null) {
			if (request.getParameterValues("parent").length > 0
					&& !request.getParameterValues("parent")[0].equals("")) {
				whyMessage.append(NEWLINE + "Parent: " + NEWLINE);
				parentFilter = request.getParameterValues("parent");
				ArrayList parentValues = new ArrayList();
				for (int i = 0; i < parentFilter.length; i++) {
					parentValues.add(parentFilter[i]);
					String parentString = AccountDAO.getAccountName(
							(String) parentValues.get(i), aConnection);
					if (i > 0) {
						whyMessage.append(", ");
					}
					whyMessage.append(parentString + " (" + parentValues.get(i)
							+ ")");
				}
			}
		}

		if (request.getParameter("se") != null) {
			if (request.getParameterValues("se").length > 0
					&& !request.getParameterValues("se")[0].equals("")) {
				whyMessage.append(NEWLINE + "Sales Engineer: " + NEWLINE);
				seFilter = request.getParameterValues("se");
				ArrayList seValues = new ArrayList();
				for (int i = 0; i < seFilter.length; i++) {
					seValues.add(seFilter[i]);
					String seString = SalesDAO.getSalesmanName(
							(String) seValues.get(i), aConnection);
					if (i > 0) {
						whyMessage.append(", ");
					}
					whyMessage.append(seString + " (" + seValues.get(i) + ")");
				}
			}
		}
		
		if (request.getParameter("segments") != null) {
			if (request.getParameterValues("segments").length > 0
					&& !request.getParameterValues("segments")[0].equals("")) {
				whyMessage.append(NEWLINE + "Segments: " + NEWLINE);
				segmentFilter = request.getParameterValues("segments");
				ArrayList segmentValues = new ArrayList();
				for (int i = 0; i < segmentFilter.length; i++) {
				    segmentValues.add(segmentFilter[i]);
					String segmentString = SegmentsDAO.getSegmentName((new Integer(segmentFilter[i])).intValue(),aConnection);
					if (i > 0) {
						whyMessage.append(", ");
					}
					whyMessage.append(segmentString);
				}
			}
		}
		
		if (request.getParameter("specialPrograms") != null) {
			if (request.getParameterValues("specialPrograms").length > 0
					&& !request.getParameterValues("specialPrograms")[0].equals("")) {
				whyMessage.append(NEWLINE + "Special Programs: " + NEWLINE);
				specialProgramFilter = request.getParameterValues("specialPrograms");
				ArrayList specialProgramValues = new ArrayList();
				for (int i = 0; i < specialProgramFilter.length; i++) {
				    specialProgramValues.add(specialProgramFilter[i]);
					if (i > 0) {
						whyMessage.append(", ");
					}
					whyMessage.append(MiscDAO.getSpecialProgramDescription(specialProgramFilter[i],aConnection));
				}
			}
		}

		String theQuery = "";
		if (session.getAttribute("assocUsersSQL") != null){
		    theQuery = (String) session.getAttribute("assocUsersSQL");
		}
		ArrayList emailAddresses = MiscDAO.genericQueryReturningArrayList(
				theQuery, aConnection);
		sendEmail(emailAddresses, request, aUser, whyMessage.toString());

	}

	private void sendEmail(ArrayList emailAddresses,
			HttpServletRequest request, User aUser, String aMessage)
			throws Exception {
                String msg = request.getParameter("message");
		String subject = request.getParameter("subject");

		try {

			String from = aUser.getEmailAddress().trim();
			ArrayList to = emailAddresses;

			StringBuffer text = new StringBuffer("");
			text.append(msg);
			text.append(NEWLINE + "" + NEWLINE + "" + NEWLINE
					+ "This message was sent from the ");
			text.append(" Sales and Marketing Resource Channel");
			text.append(". You were included on this message because you are associated with one of the customers that met the following conditions."
							+ NEWLINE + NEWLINE);
			text.append(aMessage);

			TAPMail tapmail = new TAPMail();

			for (int i = 0; i < to.size(); i++) {
				String email = (String) to.get(i);
				tapmail.addRecipient(email.trim());

			}

			tapmail.setSenderInfo(aUser.getFirstName() + " "
					+ aUser.getLastName(), from);
			tapmail.addCCRecipient(from);
			tapmail.sendMessage(text.toString(), subject);

		} catch (Exception e) {
			SMRCLogger.error(this.getClass().getName() + ".sendEmail() ", e);
			throw e;
		}
	}

} //class