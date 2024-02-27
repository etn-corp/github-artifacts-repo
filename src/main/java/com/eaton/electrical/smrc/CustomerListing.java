//Copyright (c) 2004 Eaton, Inc.
//
//$Log: not supported by cvs2svn $
//Revision 1.35  2006/03/16 23:24:35  e0073445
//4.2.1 Customer Listing Tap Dollar Change
//
//Revision 1.34  2006/03/16 19:13:31  e0073445
//Removed code that was producing warnings.
//
//Revision 1.33  2005/03/03 22:01:04  vendejp
//changes so that users with multiple sales ids can see all accounts.
//
//Revision 1.32  2005/01/10 03:00:23  schweks
//Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
//Revision 1.31  2005/01/09 05:59:53  schweks
//Added check to see if user is null before trying to stick user info in the log file.
//
//Revision 1.30  2005/01/05 22:40:24  vendejp
//declared the user object at the class level and changed the catches to include the ProfileException handling
//
//Revision 1.29  2004/12/23 18:12:50  vendejp
//Changes to connection objects and cleaned up reports look and feel
//
//Revision 1.28  2004/11/09 06:10:42  vendejp
//*** empty log message ***
//
//Revision 1.27  2004/11/08 21:38:28  vendejp
//*** empty log message ***
//
//Revision 1.26  2004/11/03 14:31:53  lubbejd
//Added links to CustomerListing from CSF pages. Corrected csfDivision queries.
//
//Revision 1.25  2004/10/28 12:30:24  lubbejd
//Changes to allow for filtering by division targets for a district
//
//Revision 1.24  2004/10/19 20:38:26  schweks
//Got a little crazy changing variable names and had to rename request.setAttribute("user" to "usr"
//
//Revision 1.23  2004/10/19 14:51:03  schweks
//Removing unused variables and code.
//
//Revision 1.22  2004/10/16 18:14:59  manupvj
//Added abstract servlet for common functions and common point of logging.
//
//Revision 1.21  2004/10/15 04:14:49  vendejp
//*** empty log message ***
//
//Revision 1.20  2004/10/14 17:40:50  schweks
//Added Eaton header comment.
//Reformatted source code.
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

public class CustomerListing extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;



	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {

		String sFwdUrl="/SMRCErrorPage.jsp";
		boolean redirect=false;
		Connection DBConn = null;
		User user = null;

		try{

			//Fetching Marked for delete value from property file SD0000002380193 Start Here

			ResourceBundle rbCustomerStatus = ResourceBundle.getBundle("com.eaton.electrical.smrc.SMRC");

           //customerStatusNotShown variable created to cater information from property file

			String customerStatusNotShown=rbCustomerStatus.getString("customerStatus");

			//SD0000002380193 END Here


			DBConn = SMRCConnectionPoolUtils.getDatabaseConnection() ;

			user = SMRCSession.getUser(request,DBConn);

			request.setAttribute("usr", user);

			SMRCHeaderBean hdr = new SMRCHeaderBean();
			hdr.setUser(user);
			request.setAttribute("header", hdr);

			String show =null;
			if(request.getParameter("show")!=null){
				if(request.getParameter("show").equals("orders")){
					show="order";
				}else{
					show="sales";
				}
			}else if(user.getShowSalesOrders().equals("o")){
				show="order";
			}else{
				show="sales";
			}



			String seId = request.getParameter("SE_ID");
			boolean targetOnly=false;
			if(seId!=null){
				if(StringManipulation.noNull(request.getParameter("targetOnly")).equals("true")){
					targetOnly=true;
					request.setAttribute("message","You are not a sales engineer on any target accounts.");
				}else{
					request.setAttribute("message","You are not a sales engineer on any accounts.");
				}
			}else{
				seId="";
				request.setAttribute("message","No Customers found with selected criteria OR the criteria was not specific enough.");
			}

			if(request.getParameter("districttargets")!=null){
				request.setAttribute("message","No Target Accounts found.");
			}

			String page = request.getParameter("page");
			if(page == null){
				page = "search";
			}


			if(page.equalsIgnoreCase("search")){
				TreeMap states = MiscDAO.getStates(DBConn);
				ArrayList districts = DistrictDAO.getCustomerSearchDistricts(DBConn);
				request.setAttribute("states", states);
				request.setAttribute("districts", districts);

				sFwdUrl="/customerListingSearchDisplay.jsp";
			}else if(page.equalsIgnoreCase("listing")){

				CustomerSearchCriteria criteria = new CustomerSearchCriteria();

				if(request.getParameter("xcel")!=null){
					criteria.setXcel(true);
					sFwdUrl="/customerListingDisplayXcel.jsp";
				}else{
					sFwdUrl="/customerListingDisplay.jsp";
				}

				if(show.equals("sales")){
					criteria.setShowSales(true);
				}
				criteria.setFirstRecNum(StringManipulation.noNull(request.getParameter("recNum")));
				criteria.setSort(StringManipulation.noNull(request.getParameter("sort")));
				criteria.setSortDir(StringManipulation.noNull(request.getParameter("sortDir")));
				criteria.setSegment(StringManipulation.noNull(request.getParameter("segment")));
				criteria.setOtherHomeSegment(StringManipulation.noNull(request.getParameter("otherHomeSegment")));
				criteria.setSeId(seId);
				criteria.setSalesIds(user.getSalesIds());
				criteria.setTargetOnly(targetOnly);

				criteria.setDistrictTargets(StringManipulation.noNull(request.getParameter("districttargets")));
				criteria.setDivisionTargets(StringManipulation.noNull(request.getParameter("divisiontargets")));

				criteria.setUser(user);

				criteria.setCustomerName(StringManipulation.noNull(request.getParameter("CUSTOMER_NAME")));
				criteria.setVcn(StringManipulation.noNull(request.getParameter("VISTA_CUSTOMER_NUMBER")));
				criteria.setParentsOnly(StringManipulation.noNull(request.getParameter("PARENTS_ONLY")));
				criteria.setLastName(StringManipulation.noNull(request.getParameter("LAST_NAME")));
				criteria.setFirstName(StringManipulation.noNull(request.getParameter("FIRST_NAME")));
				criteria.setState(StringManipulation.noNull(request.getParameter("STATE")));
				criteria.setDistrict(StringManipulation.noNull(request.getParameter("DISTRICT")));
				criteria.setDivision(StringManipulation.noNull(request.getParameter("DIVISION")));
				criteria.setPotentialCustomersOnly(StringManipulation.noNull(request.getParameter("POTENTIAL_CUSTOMERS_ONLY")));

				// Get the tap dollar type code from the request

				String tapDollarType = (String)request.getParameter("show");

				if ((tapDollarType == null) || (tapDollarType.length() < 1)) {

					tapDollarType = "invoice";

				} else if ((!tapDollarType.equalsIgnoreCase("order")) && (!tapDollarType.equalsIgnoreCase("invoice"))) {

					throw new ServletException("Invalid dollar type.  Must be either invoice or order");

				}

				criteria.setDollarType(StringManipulation.noNull(tapDollarType));
				request.setAttribute("tapDollarType", tapDollarType);

// Braffet : Removed for TAP Dollars
				/*

				int dollarTypeCode = user.getDollarTypeCode();
				CodeType code = MiscDAO.getCodeById(dollarTypeCode,DBConn);
				criteria.setDollarType(StringManipulation.noNull(code.getDescription()));
				request.setAttribute("defaultDollarType",code.getDescription());
*/

				//Function modified to pass customer status variable from prop file SD0000002380193 Start here

				ArrayList accounts = AccountDAO.getCustomerListing(criteria,customerStatusNotShown, DBConn);

				//SD0000002380193 END Here

				request.setAttribute("accounts", accounts);


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
