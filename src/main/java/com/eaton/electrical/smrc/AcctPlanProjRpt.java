//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.37  2006/03/16 19:13:32  e0073445
// Removed code that was producing warnings.
//
// Revision 1.36  2005/05/25 18:36:15  lubbejd
// More changes for CR30585 (Change to target project approval process).
//
// Revision 1.35  2005/02/11 19:07:26  lubbejd
// Remove calls to MiscDAO.saveAPLog() from everywhere, and added the call
// to SMRCBaseServlet to update with every forward to a jsp
//
// Revision 1.34  2005/02/10 18:43:10  lubbejd
// Changed MiscDAO.saveAPLog() to use User object instead of userid string
// so it can call UserDAO.getGeogForUser for storing the sp_geog on the activity
// log.
//
// Revision 1.33  2005/02/07 19:03:53  lubbejd
// Add ability to export to excel to the bid tracker summary and detail reports
//
// Revision 1.32  2005/02/07 16:14:27  lubbejd
// Add excel export to target projects report (CR27593)
//
// Revision 1.31  2005/02/04 20:02:34  lubbejd
// Add Sales Engineer filter to bid tracker reports, using the existing SEBrowse Servlet
// (CR27592)
//
// Revision 1.30  2005/01/14 13:15:30  lubbejd
// Changes to use srYear instead of current year
//
// Revision 1.29  2005/01/12 05:04:07  schweks
// Changed new Integer().intValue() occurrences to Globals.a2int() instead.
//
// Revision 1.28  2005/01/10 22:17:24  vendejp
// code cleanup
//
// Revision 1.27  2005/01/10 03:00:23  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.26  2005/01/09 05:59:54  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.25  2005/01/05 22:40:24  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.24  2004/12/23 18:12:49  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.23  2004/11/12 17:25:03  lubbejd
// Fix sorting on target project reports
//
// Revision 1.22  2004/11/03 19:21:53  lubbejd
// add logger for query
//
// Revision 1.21  2004/11/03 18:52:35  lubbejd
// add segment selection for target project reports
//
// Revision 1.20  2004/10/19 14:36:41  schweks
// Removing unused variables and code.
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
import java.math.*;
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
 * This is the project reporting servlet for the OEM Account Planning
 * application.
 * 
 * @author Carl Abel
 * @date February 24, 2003
 */
public class AcctPlanProjRpt extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Connection DBConn = null;
		String sFwdUrl = "/SMRCErrorPage.jsp";
		boolean redirect = false;
		HttpSession session;
		User user = null;
		
		try {
			DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();

			user = SMRCSession.getUser(request, DBConn);
//			String userid = user.getUserid();
			session = request.getSession();

			String page = request.getParameter("page");

			HeaderBean hdr = new HeaderBean();
			hdr.setPage(page);
			hdr.setHelpPage(TAPcommon.getHelpPage(page, DBConn));
			hdr.setUser(user);

			request.setAttribute("header", hdr);
			sFwdUrl = writeNewPage(request, DBConn, session);
			// Now if something goes wrong and a page is not returned, go to the
			// error page
			if (sFwdUrl.equals("")) {
				throw new Exception(
						"Incorrect parameters.  Forward to page not found.");
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

	private String writeNewPage(HttpServletRequest request, Connection aConnection, HttpSession session)
			throws java.io.IOException, Exception {

		String page = request.getParameter("page");
		String refresh = StringManipulation.noNull(request.getParameter("refresh"));
		

		if (page.equals("projects") || refresh.equalsIgnoreCase("y")) {
			return writeSelectionPage(request, aConnection);
		} else if (page.equals("projresults")) {
			return writeResultsPage(request, aConnection, session);
		}

		return "";

	}

	private String writeSelectionPage(HttpServletRequest request, Connection aConnection)
			throws java.io.IOException, Exception {

		HttpSession session = request.getSession(false);
		String selectedBidDateRange = "";
		String selectedHasGO = "";
		String selectedViewDetails = "";
		ArrayList selectedBidStatus = new ArrayList();
		ArrayList selectedJobTypes = new ArrayList();
		ArrayList selectedZones = new ArrayList();
		ArrayList selectedDistricts = new ArrayList();
		ArrayList selectedProducts = new ArrayList();
		
		int srYear = Globals.a2int((String) session.getAttribute("srYear"));
	   	// If the year is zero, something went wrong... set to current year
		if ( srYear == 0) {
			Calendar cal = new GregorianCalendar();
		    
		    // Get the components of the date
		    srYear = cal.get(Calendar.YEAR);
		}
		ArrayList selectedSENames = new ArrayList();
		if (session.getAttribute("selectedSEs") != null){
		    if ((request.getParameter("clearSEs") != null) && (request.getParameter("clearSEs").equalsIgnoreCase("y"))){
		        session.removeAttribute("selectedSEs");
		    } else {
			    ArrayList selectedSEs = (ArrayList) session.getAttribute("selectedSEs");
			    for (int i=0; i< selectedSEs.size(); i++){
			        String seId = (String) selectedSEs.get(i);
			        String seName = (SalesDAO.getSalesmanName(seId, aConnection) + " (" + seId + ") ");
			        selectedSENames.add(seName);
			    }
		    }
		}
		if ((request.getParameter("refresh") != null) && (request.getParameter("refresh").equalsIgnoreCase("y"))){
		    selectedBidDateRange = StringManipulation.noNull(request.getParameter("bidDate"));
			selectedHasGO = StringManipulation.noNull(request.getParameter("goNum"));
			selectedViewDetails = StringManipulation.noNull(request.getParameter("viewDetails"));
			selectedBidStatus = returnSelectedParams(request.getParameterValues("bidStatus"));
			selectedJobTypes = returnSelectedParams(request.getParameterValues("jobType"));
			selectedZones = returnSelectedParams(request.getParameterValues("zone"));
			selectedDistricts = returnSelectedParams(request.getParameterValues("dist"));
			selectedProducts = returnSelectedParams(request.getParameterValues("prods"));
		}
		request.setAttribute("bidDateRanges", loadBidDateRangeOptions());
		request.setAttribute("cops", getChangeOrderPotentials(aConnection));
		request.setAttribute("vendors", getVendors(aConnection));
		request.setAttribute("products", ProductDAO.getProductsAlphabetically(srYear, aConnection));
		request.setAttribute("statuses", getStatuses(aConnection));
		request.setAttribute("reasons", getReasons(aConnection));
		request.setAttribute("preferences", getPreferences(aConnection));
		request.setAttribute("zones", getZones(aConnection));
		request.setAttribute("districts", getDistricts(aConnection));
		request.setAttribute("bidStatuses", getBidStatuses(aConnection));
		request.setAttribute("jobTypes", getJobTypes(aConnection));
		request.setAttribute("segments", SegmentsDAO.getSegments(0,4,aConnection));
		request.setAttribute("selectedSENames", selectedSENames);
		request.setAttribute("selectedBidDateRange", selectedBidDateRange);
		request.setAttribute("selectedHasGO", selectedHasGO);
        request.setAttribute("selectedViewDetails", selectedViewDetails);
		request.setAttribute("selectedBidStatus", selectedBidStatus);
		request.setAttribute("selectedJobTypes",selectedJobTypes);
		request.setAttribute("selectedZones", selectedZones);
		request.setAttribute("selectedDistricts", selectedDistricts);
		request.setAttribute("selectedProducts",selectedProducts);
		

		return "/AcctPlanProjRptSelection.jsp";

	}
// Braffet
	private String writeResultsPage(HttpServletRequest request, Connection aConnection, HttpSession session)
			throws java.io.IOException, Exception {
		String rptType = request.getParameter("rptType");

		if (rptType.equals("targetProj")) {
			return writeTargetProjectResults(request, aConnection, session);
		} else if (rptType.equals("bidTrack")) {
			if (request.getParameter("viewDetails") != null
					&& request.getParameter("viewDetails").equals("Y")) {
				return writeBidTrackerResults(request, aConnection);
			}
			return writeBidTrackerSummaryResults(request, aConnection);
			
		} else if (rptType.equals("negDetails")) {
			return writeNegDetails(request, aConnection);
		}
		return "";
	}

	private String writeNegDetails(HttpServletRequest request, Connection aConnection)
			throws java.io.IOException, Exception {
		String neg = request.getParameter("negNum");
		Bid bid = getBid(neg, aConnection);
		BidStatusHistory bsh = getBidStatusHistory(neg, aConnection);
		ArrayList customersLinkedToNeg = getCustomersLinkedToNeg(neg,
				aConnection);
		ArrayList products = bid.getAllProducts();
		ArrayList productDesc = new ArrayList();
		for (int i = 0; i < products.size(); i++) {
			BidProduct bp = (BidProduct) products.get(i);
			productDesc.add(TAPcommon.getProductDescription(bp.getProductId(),
					aConnection));
		}
		request.setAttribute("salesmanName", SalesDAO.getSalesmanName(bid
				.getSalesId(), aConnection));
		request.setAttribute("geogName", MiscDAO.getGeography(bid.getSPGeog(),
				aConnection).getDescription());
		request.setAttribute("bid", bid);
		request.setAttribute("bsh", bsh);
		request.setAttribute("customersLinkedToNeg", customersLinkedToNeg);
		request.setAttribute("products", products);
		request.setAttribute("productDesc", productDesc);

		return "/AcctPlanProjRptNegDetails.jsp";

	}

	private ArrayList getCustomersLinkedToNeg(String neg, Connection aConnection)
			throws Exception {
		ArrayList custs = new ArrayList(10);
		String sel = "select * from bid_cust_xref where neg_num = '" + neg
				+ "'";

		Statement s = null;
		ResultSet r = null;
		try {
			s = aConnection.createStatement();
			r = s.executeQuery(sel);

			while (r.next()) {
				Customer c = new Customer();
				c.setVistaCustNum(r.getString("vista_cust_num"));
				c.setName(AccountDAO.getAccountName(r
						.getString("vista_cust_num"), aConnection));

				custs.add(c);
			}

		} catch (Exception e) {
			SMRCLogger.error(this.getClass().getName()
					+ ".getCustomersLinkedToNeg() ", e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}
		return custs;
	}

	private BidStatusHistory getBidStatusHistory(String neg,
			Connection aConnection) throws Exception {
		BidStatusHistory bsh = new BidStatusHistory();
		String sel = "select * from bid_status_history where neg_num = '" + neg
				+ "'";

		Statement s = null;
		ResultSet r = null;
		try {
			s = aConnection.createStatement();
			r = s.executeQuery(sel);

			while (r.next()) {
				bsh.setNegNum(neg);

				if (r.getDate("bid_date") != null) {
					bsh.setBidDate(r.getDate("bid_date"));
				}
				if (r.getDate("obtain_date") != null) {
					bsh.setObtainDate(r.getDate("obtain_date"));
				}
				if (r.getDate("abandon_date") != null) {
					bsh.setAbandonDate(r.getDate("abandon_date"));
				}
				if (r.getDate("lost_date") != null) {
					bsh.setLostDate(r.getDate("lost_date"));
				}
				if (r.getDate("new_date") != null) {
					bsh.setNewDate(r.getDate("new_date"));
				}
				if (r.getDate("budget_date") != null) {
					bsh.setBudgetDate(r.getDate("budget_date"));
				}
				if (r.getDate("buy_date") != null) {
					bsh.setBuyDate(r.getDate("buy_date"));
				}
			}

		} catch (Exception e) {
			SMRCLogger.error(this.getClass().getName()
					+ ".getBidStatusHistory() ", e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}
		return bsh;
	}

	private Bid getBid(String neg, Connection aConnection) throws Exception {
		Bid bid = new Bid();
		String sel = "select * from bid_tracker where neg_num = '"
				+ neg.toUpperCase() + "'";

		Statement s = null;
		ResultSet r = null;
		try {
			s = aConnection.createStatement();
			r = s.executeQuery(sel);

			while (r.next()) {
				bid.setNegNum(r.getString("neg_num"));

				if (r.getString("job_name") != null) {
					bid.setJobName(r.getString("job_name"));
				}
				if (r.getString("status") != null) {
					bid.setStatus(r.getString("status"));
				}
				if (r.getString("sales_id") != null) {
					bid.setSalesId(r.getString("sales_id"));
				}
				if (r.getString("bid_dollars") != null) {
					bid.setBidDollars(r.getDouble("bid_dollars"));
				}
				if (r.getString("job_type") != null) {
					bid.setJobType(getJobType(r.getString("job_type"),
							aConnection));
				}
				if (r.getString("order_date") != null) {
					bid.setGODate(r.getDate("order_date"));
				}
				if (r.getString("go_num") != null) {
					bid.setGONum(r.getString("go_num"));
				}
				if (r.getString("bid_date") != null) {
					bid.setBidDate(r.getDate("bid_date"));
				}
				if (r.getString("sp_geog") != null) {
					bid.setSPGeog(r.getString("sp_geog"));
				}
			}

			String sel2 = "select * from bid_products where neg_num = '" + neg
					+ "'";

			s = aConnection.createStatement();
			r = s.executeQuery(sel2);

			while (r.next()) {
				BidProduct bp = new BidProduct();

				bp.setProductId(r.getString("product_line"));
				bp.setNegNum(neg);

				if (r.getString("bid_dollars") != null) {
					bp.setBidDollars(r.getDouble("bid_dollars"));
				}

				if (r.getString("order_dollars") != null) {
					bp.setOrderDollars(r.getDouble("order_dollars"));
				}

				bid.addProduct(bp);
			}

		} catch (Exception e) {
			SMRCLogger.error(this.getClass().getName() + ".getBid() ", e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}

		return bid;
	}

	private String writeBidTrackerSummaryResults(HttpServletRequest request, Connection aConnection)
			throws java.io.IOException, Exception {
		StringBuffer sortParms = new StringBuffer("");
		Enumeration parmNames = request.getParameterNames();

		while (parmNames.hasMoreElements()) {
			String name = (String) parmNames.nextElement();
			String[] vals = request.getParameterValues(name);

			if (name.equals("viewDetails")) {
			} else {
				for (int i = 0; i < vals.length; i++) {
					sortParms.append("&");
					sortParms.append(name);
					sortParms.append("=");
					sortParms.append(vals[i]);
				}
			}
		}

		StringBuffer filters = getBidTrackerFilters(request, aConnection);
		BidTrackerSummaryResults results = getBidTrackerSummaryResults(request,
				aConnection);

		request.setAttribute("filters", filters);
		request.setAttribute("results", results);
		request.setAttribute("sortParms", sortParms);

		if (request.getParameter("excel") != null){
		    return "/AcctPlanProjRptBidTrackerSummaryExcel.jsp";
		} else {
		    return "/AcctPlanProjRptBidTrackerSummary.jsp";
		}

	}

	private StringBuffer getResultsWhereClause(HttpServletRequest request) {
		String[] bidDates = request.getParameterValues("bidDate");
		String[] statuses = request.getParameterValues("bidStatus");
		String[] types = request.getParameterValues("jobType");
		String goNum = request.getParameter("goNum");
		String[] zones = request.getParameterValues("zone");
		String[] districts = request.getParameterValues("dist");
		String[] salesEngineers = request.getParameterValues("bidSEFilter");
		StringBuffer moreWhere = new StringBuffer("");

		if (bidDates != null && !bidDates[0].equals("")) {
			for (int i = 0; i < bidDates.length; i++) {
				if (bidDates[i].equals("nextYear")) {
					moreWhere
							.append(" and bid_date between sysdate and add_months(sysdate,12)");
				} else if (bidDates[i].equals("6month")) {
					moreWhere
							.append(" and bid_date between sysdate and add_months(sysdate,6)");
				} else if (bidDates[i].equals("3month")) {
					moreWhere
							.append(" and bid_date between sysdate and add_months(sysdate,3)");
				} else if (bidDates[i].equals("nextMonth")) {
					moreWhere
							.append(" and bid_date between sysdate and add_months(sysdate,1)");
				} else if (bidDates[i].equals("nextWeek")) {
					moreWhere
							.append(" and bid_date between sysdate and (sysdate+7)");
				} else if (bidDates[i].equals("day")) {
					moreWhere
							.append(" and bid_date >= sysdate-1 and bid_date <= sysdate");
				} else if (bidDates[i].equals("week")) {
					moreWhere
							.append(" and bid_date >= sysdate-7 and bid_date <= sysdate");
				} else if (bidDates[i].equals("thisMonth")) {
					moreWhere
							.append(" and to_char(bid_date,'MMYYYY') = to_char(sysdate,'MMYYYY')");
				} else if (bidDates[i].equals("lastMonth")) {
					moreWhere
							.append(" and to_char(bid_date,'MMYYYY') = to_char(add_months(sysdate,-1),'MMYYYY')");
				} else if (bidDates[i].equals("thisYear")) {
					moreWhere
							.append(" and to_char(bid_date,'YYYY') = to_char(sysdate,'YYYY')");
				} else if (bidDates[i].equals("lastYear")) {
					moreWhere
							.append(" and to_char(bid_date,'YYYY') = to_char(add_months(sysdate,-12),'YYYY')");
				}
			}
		}
		if (statuses != null && !statuses[0].equals("")) {
			moreWhere.append(" and status in (");

			for (int i = 0; i < statuses.length; i++) {
				if (i > 0) {
					moreWhere.append(",");
				}
				moreWhere.append("'");
				moreWhere.append(statuses[i]);
				moreWhere.append("'");
			}

			moreWhere.append(") ");
		}
		if (types != null && !types[0].equals("")) {
			moreWhere.append(" and job_type in (");

			for (int i = 0; i < types.length; i++) {
				if (i > 0) {
					moreWhere.append(",");
				}
				moreWhere.append("'");
				moreWhere.append(types[i]);
				moreWhere.append("'");
			}

			moreWhere.append(") ");
		}
		if (goNum != null && !goNum.equals("")) {
			if (goNum.equals("N")) {
				moreWhere.append(" and go_num is null ");
			} else if (goNum.equals("Y")) {
				moreWhere.append(" and go_num is not null ");
			}
		}
		if (zones != null && !zones[0].equals("")) {
			moreWhere.append(" and (");

			for (int i = 0; i < zones.length; i++) {
				if (i > 0) {
					moreWhere.append(" or ");
				}
				moreWhere.append("sp_geog like '");
				moreWhere.append(zones[i].substring(0, 4));
				moreWhere.append("%'");
			}

			moreWhere.append(") ");
		}
		if (districts != null && !districts[0].equals("")) {
			moreWhere.append(" and (");

			for (int i = 0; i < districts.length; i++) {
				if (i > 0) {
					moreWhere.append(" or ");
				}
				moreWhere.append("sp_geog like '");
				moreWhere.append(districts[i]);
				moreWhere.append("%'");
			}

			moreWhere.append(") ");
		}
		if (salesEngineers != null && !salesEngineers[0].equals("")) {
			moreWhere.append(" and sales_id in (");

			for (int i = 0; i < salesEngineers.length; i++) {
				if (i > 0) {
					moreWhere.append(", ");
				}
				moreWhere.append("'");
				moreWhere.append(salesEngineers[i]);
				moreWhere.append("'");
			}

			moreWhere.append(") ");
		}

		return moreWhere;
	}

	private ArrayList getBidTrackerSearchResults(HttpServletRequest request,
			Connection aConnection) throws Exception {
		ArrayList results = new ArrayList(100);
		StringBuffer moreWhere = getResultsWhereClause(request);

		String sortMeth = "job_name";
		StringBuffer sortParms = new StringBuffer("");
		Enumeration parmNames = request.getParameterNames();

		while (parmNames.hasMoreElements()) {
			String name = (String) parmNames.nextElement();
			String[] vals = request.getParameterValues(name);

			if (name.equals("sortMeth")) {

				if (vals[0].equals("bid_dollars")) {
					sortMeth = "nvl(a." + vals[0] + ",0) desc";
				} else if (vals[0].equals("neg_num")) {
					sortMeth = "a.neg_num";
				} else {
					sortMeth = vals[0];

				}
			} else {
				for (int i = 0; i < vals.length; i++) {
					sortParms.append("&");
					sortParms.append(name);
					sortParms.append("=");
					sortParms.append(vals[i]);
				}
			}
		}

		String sel = "";
		boolean useBidProds = false;
		String[] prodIds = new String[1];

		if (request.getParameter("prods") != null) {
			if (request.getParameterValues("prods")[0].length() > 0) {
				useBidProds = true;
				prodIds = request.getParameterValues("prods");
			}
		}

		if (useBidProds) {
			StringBuffer prodWhere = new StringBuffer(" and product_line in (");

			for (int i = 0; i < prodIds.length; i++) {
				prodWhere.append("'");
				prodWhere.append(prodIds[i]);
				prodWhere.append("'");

				if (i + 1 < prodIds.length) {
					prodWhere.append(",");
				}
			}

			prodWhere.append(") ");

			sel = "select a.neg_num, job_name, status, sales_id, job_type, order_date, go_num, bid_date, sum(b.bid_dollars) as bid_dollars "
					+ " from bid_tracker a, bid_products b "
					+ " where a.neg_num = b.neg_num "
					+ prodWhere.toString()
					+ moreWhere.toString()
					+ " group by a.neg_num, job_name, status, sales_id, job_type, order_date, go_num, bid_date "
					+ " order by " + sortMeth;

		} else {

			sel = "select * from bid_tracker a " + " where 1=1 "
					+ moreWhere.toString() + " order by " + sortMeth;

		}

		Statement s = null;
		ResultSet r = null;
		try {
			s = aConnection.createStatement();
			r = s.executeQuery(sel);

			while (r.next()) {
				Bid bid = new Bid();
				bid.setNegNum(r.getString("neg_num"));

				if (r.getString("job_name") != null) {
					bid.setJobName(r.getString("job_name"));
				}
				if (r.getString("status") != null) {
					bid.setStatus(r.getString("status"));
				}
				if (r.getString("sales_id") != null) {
					bid.setSalesId(r.getString("sales_id"));
				}
				if (r.getString("bid_dollars") != null) {
					bid.setBidDollars(r.getDouble("bid_dollars"));
				}
				if (r.getString("job_type") != null) {
					bid.setJobType(getJobType(r.getString("job_type"),
							aConnection));
				}
				if (r.getString("order_date") != null) {
					bid.setGODate(r.getDate("order_date"));
				}
				if (r.getString("go_num") != null) {
					bid.setGONum(r.getString("go_num"));
				}
				if (r.getString("bid_date") != null) {
					bid.setBidDate(r.getDate("bid_date"));
				}

				results.add(bid);
			}

		} catch (Exception e) {
			SMRCLogger.error(this.getClass().getName()
					+ ".getBidTrackerSearchResults() ", e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}

		return results;
	}

	private BidTrackerSummaryResults getBidTrackerSummaryResults(
			HttpServletRequest request, Connection aConnection)
			throws Exception {
		BidTrackerSummaryResults results = new BidTrackerSummaryResults();
		StringBuffer moreWhere = getResultsWhereClause(request);

		String sel = "";
		boolean useBidProds = false;
		String[] prodIds = new String[1];

		if (request.getParameter("prods") != null) {
			if (request.getParameterValues("prods")[0].length() > 0) {
				useBidProds = true;
				prodIds = request.getParameterValues("prods");
			}
		}

		if (useBidProds) {
			StringBuffer prodWhere = new StringBuffer(" and product_line in (");

			for (int i = 0; i < prodIds.length; i++) {
				prodWhere.append("'");
				prodWhere.append(prodIds[i]);
				prodWhere.append("'");

				if (i + 1 < prodIds.length) {
					prodWhere.append(",");
				}
			}

			prodWhere.append(") ");

			sel = "select obtCnt,totCnt, obtDol, totDol "
					+ "from (select count(*) obtCnt, sum(b.bid_dollars) obtDol from bid_tracker a, bid_products b where 1=1 and status = 'Obtained' and a.neg_num = b.neg_num "
					+ prodWhere.toString()
					+ moreWhere.toString()
					+ ") a, "
					+ "(select count(*) totCnt, sum(b.bid_dollars) totDol from bid_tracker a, bid_products b where a.neg_num = b.neg_num "
					+ prodWhere.toString() + moreWhere.toString() + ") b";

		} else {

			sel = "select obtCnt,totCnt, obtDol, totDol "
					+ "from (select count(*) obtCnt, sum(bid_dollars) obtDol from bid_tracker where 1=1 and status = 'Obtained' "
					+ moreWhere.toString()
					+ ") a, "
					+ "(select count(*) totCnt, sum(bid_dollars) totDol from bid_tracker where 1=1 "
					+ moreWhere.toString() + ") b";

		}

		Statement s = null;
		ResultSet r = null;
		try {
			s = aConnection.createStatement();
			r = s.executeQuery(sel);

			while (r.next()) {
				results.setObtainedCnt(r.getLong("obtCnt"));
				results.setObtainedDol(r.getDouble("obtDol"));
				results.setTotalCnt(r.getLong("totCnt"));
				results.setTotalDol(r.getDouble("totDol"));
			}

		} catch (Exception e) {
			SMRCLogger.error(this.getClass().getName()
					+ ".getBidTrackerSummaryResults() ", e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}

		return results;
	}

	private StringBuffer getBidTrackerFilters(HttpServletRequest request,
			Connection aConnection) throws Exception {
		String[] bidDates = request.getParameterValues("bidDate");
		String[] statuses = request.getParameterValues("bidStatus");
		String[] types = request.getParameterValues("jobType");
		String goNum = request.getParameter("goNum");
		String[] zones = request.getParameterValues("zone");
		String[] districts = request.getParameterValues("dist");
		StringBuffer filters = new StringBuffer("");
		String[] products = request.getParameterValues("prods");
		String[] salesEngineers = request.getParameterValues("bidSEFilter");

		if (bidDates != null && !bidDates[0].equals("")) {
			filters
					.append("<tr><td valign=top class=smallFontBL>Bid Dates:</td><td valign=top class=smallFontL>");

			for (int i = 0; i < bidDates.length; i++) {
				if (i > 0) {
					filters.append(", ");
				}

				if (bidDates[i].equals("nextYear")) {
					filters.append("Upcoming Year");
				} else if (bidDates[i].equals("6month")) {
					filters.append("Next 6 Months");
				} else if (bidDates[i].equals("3month")) {
					filters.append("Next 3 Months");
				} else if (bidDates[i].equals("nextMonth")) {
					filters.append("Upcoming Month");
				} else if (bidDates[i].equals("nextWeek")) {
					filters.append("Upcoming Week");
				} else if (bidDates[i].equals("day")) {
					filters.append("Previous Day");
				} else if (bidDates[i].equals("week")) {
					filters.append("Previous Week");
				} else if (bidDates[i].equals("thisMonth")) {
					filters.append("This Month");
				} else if (bidDates[i].equals("lastMonth")) {
					filters.append("Last Month");
				} else if (bidDates[i].equals("thisYear")) {
					filters.append("This Year");
				} else if (bidDates[i].equals("lastYear")) {
					filters.append("Last Year");
				}
			}

			filters.append("</td></tr>");
		}
		if (statuses != null && !statuses[0].equals("")) {
			filters
					.append("<tr><td valign=top class=smallFontBL>Bid Statuses:</td><td valign=top class=smallFontL>");

			for (int i = 0; i < statuses.length; i++) {
				if (i > 0) {
					filters.append(", ");
				}
				filters.append(statuses[i]);
			}

			filters.append("</td></tr>");
		}
		if (types != null && !types[0].equals("")) {
			filters
					.append("<tr><td valign=top class=smallFontBL>Job Types:</td><td valign=top class=smallFontL>");

			for (int i = 0; i < types.length; i++) {
				if (i > 0) {
					filters.append(", ");
				}
				filters.append(types[i]);
			}

			filters.append("</td></tr>");
		}
		if (goNum != null && !goNum.equals("")) {
			filters
					.append("<tr><td valign=top class=smallFontBL>Has GO Number:</td><td valign=top class=smallFontL>");
			filters.append(goNum);
			filters.append("</td></tr>");
		}
		if (zones != null && !zones[0].equals("")) {
			filters
					.append("<tr><td valign=top class=smallFontBL>Zones:</td><td valign=top class=smallFontL>");

			for (int i = 0; i < zones.length; i++) {
				if (i > 0) {
					filters.append(", ");
				}
				filters.append(MiscDAO.getGeography(zones[i], aConnection)
						.getDescription());
			}

			filters.append("</td></tr>");
		}
		if (districts != null && !districts[0].equals("")) {
			filters
					.append("<tr><td valign=top class=smallFontBL>Districts:</td><td valign=top class=smallFontL>");

			for (int i = 0; i < districts.length; i++) {
				if (i > 0) {
					filters.append(", ");
				}
				filters.append(MiscDAO.getGeography(districts[i], aConnection)
						.getDescription());
			}

			filters.append("</td></tr>");
		}
		if (products != null && !products[0].equals("")) {
			filters
					.append("<tr><td valign=top class=smallFontBL>Products:</td><td valign=top class=smallFontL>");

			for (int i = 0; i < products.length; i++) {
				if (i > 0) {
					filters.append(", ");
				}
				filters.append(TAPcommon.getProductDescription(products[i],
						aConnection));
			}

			filters.append("</td></tr>");
		}
		if (salesEngineers != null && !salesEngineers[0].equals("")) {
			filters.append("<tr><td valign=top class=smallFontBL>Sales Engineers:</td><td valign=top class=smallFontL>");

			for (int i = 0; i < salesEngineers.length; i++) {
				if (i > 0) {
					filters.append(", ");
				}
				filters.append(SalesDAO.getSalesmanName(salesEngineers[i],aConnection) + " (" + salesEngineers[i] + ")");
			}

			filters.append("</td></tr>");
		}

		return filters;
	}

	private String writeBidTrackerResults(HttpServletRequest request, Connection aConnection)
			throws java.io.IOException, Exception {
	    
		StringBuffer filters = getBidTrackerFilters(request, aConnection);
		StringBuffer sortParms = new StringBuffer("");
		Enumeration parmNames = request.getParameterNames();

		while (parmNames.hasMoreElements()) {
			String name = (String) parmNames.nextElement();
			String[] vals = request.getParameterValues(name);

			if (!name.equals("sortMeth")) {
				for (int i = 0; i < vals.length; i++) {
					sortParms.append("&");
					sortParms.append(name);
					sortParms.append("=");
					sortParms.append(vals[i]);
				}
			}
		}

		ArrayList results = getBidTrackerSearchResults(request, aConnection);
		BigDecimal bidDol = new BigDecimal(0);

		for (int i = 0; i < results.size(); i++) {
			Bid bid = (Bid) results.get(i);
			bidDol = bidDol.add(new BigDecimal(bid.getBidDollars()));
		}

		request.setAttribute("filters", filters);
		request.setAttribute("sortParms", sortParms);
		request.setAttribute("results", results);
		request.setAttribute("bidDol", bidDol);

		if (request.getParameter("excel") != null){
		    return "/AcctPlanProjRptBidTrackerResultsExcel.jsp";
		} else {
		    return "/AcctPlanProjRptBidTrackerResults.jsp";
		}

	}

	private String writeTargetProjectResults(HttpServletRequest request, Connection aConnection, HttpSession session)
			throws java.io.IOException, Exception {
	    
		ArrayList reportBeans = new ArrayList();
		String[] revDates = request.getParameterValues("revDate");
		String[] bidDates = request.getParameterValues("bidDate");
		String[] cops = request.getParameterValues("cop");
		String[] vendors = request.getParameterValues("vendor");
		String[] products = request.getParameterValues("bom");
		String[] statuses = request.getParameterValues("status");
		String[] reasons = request.getParameterValues("reason");
		String[] preferences = request.getParameterValues("preference");
		String[] zones = request.getParameterValues("zone");
		String[] districts = request.getParameterValues("dist");
		String[] internalStatuses = request
				.getParameterValues("internal_status");
		String[] segments = request.getParameterValues("segments");
		StringBuffer filters = new StringBuffer("");
		StringBuffer moreFrom = new StringBuffer("");
		StringBuffer moreWhere = new StringBuffer("");

		if (revDates != null && !revDates[0].equals("")) {
			filters
					.append("<tr><td valign=top class=smallFontBL>Revision Dates:</td><td valign=top class=smallFontL>");

			for (int i = 0; i < revDates.length; i++) {
				if (i > 0) {
					filters.append(", ");
				}

				if (revDates[i].equals("day")) {
					moreWhere.append(" and date_changed >= sysdate-1");
					filters.append("Previous Day");
				} else if (revDates[i].equals("week")) {
					moreWhere.append(" and date_changed >= sysdate-7");
					filters.append("This Week");
				} else if (revDates[i].equals("thisMonth")) {
					moreWhere
							.append(" and to_char(date_changed,'MMYYYY') = to_char(sysdate,'MMYYYY')");
					filters.append("This Month");
				} else if (revDates[i].equals("lastMonth")) {
					moreWhere
							.append(" and to_char(date_changed,'MMYYYY') = to_char(add_months(sysdate,-1),'MMYYYY')");
					filters.append("Last Month");
				} else if (revDates[i].equals("thisYear")) {
					moreWhere
							.append(" and to_char(date_changed,'YYYY') = to_char(sysdate,'YYYY')");
					filters.append("This Year");
				} else if (revDates[i].equals("lastYear")) {
					moreWhere
							.append(" and to_char(date_changed,'YYYY') = to_char(add_months(sysdate,-12),'YYYY')");
					filters.append("Last Year");
				}
			}

			filters.append("</td></tr>");
		}
		if (bidDates != null && !bidDates[0].equals("")) {
			filters
					.append("<tr><td valign=top class=smallFontBL>Bid Dates:</td><td valign=top class=smallFontL>");

			for (int i = 0; i < bidDates.length; i++) {
				if (i > 0) {
					filters.append(", ");
				}

				if (bidDates[i].equals("nextYear")) {
					moreWhere
							.append(" and bid_date between sysdate and add_months(sysdate,12)");
					filters.append("Upcoming Year");
				} else if (bidDates[i].equals("6month")) {
					moreWhere
							.append(" and bid_date between sysdate and add_months(sysdate,6)");
					filters.append("Next 6 Months");
				} else if (bidDates[i].equals("3month")) {
					moreWhere
							.append(" and bid_date between sysdate and add_months(sysdate,3)");
					filters.append("Next 3 Months");
				} else if (bidDates[i].equals("nextMonth")) {
					moreWhere
							.append(" and bid_date between sysdate and add_months(sysdate,1)");
					filters.append("Upcoming Month");
				} else if (bidDates[i].equals("nextWeek")) {
					moreWhere
							.append(" and bid_date between sysdate and (sysdate+7)");
					filters.append("Upcoming Week");
				} else if (bidDates[i].equals("day")) {
					moreWhere
							.append(" and bid_date >= sysdate-1 and bid_date <= sysdate");
					filters.append("Previous Day");
				} else if (bidDates[i].equals("week")) {
					moreWhere
							.append(" and bid_date >= sysdate-7 and bid_date <= sysdate");
					filters.append("Previous Week");
				} else if (bidDates[i].equals("thisMonth")) {
					moreWhere
							.append(" and to_char(bid_date,'MMYYYY') = to_char(sysdate,'MMYYYY')");
					filters.append("This Month");
				} else if (bidDates[i].equals("lastMonth")) {
					moreWhere
							.append(" and to_char(bid_date,'MMYYYY') = to_char(add_months(sysdate,-1),'MMYYYY')");
					filters.append("Last Month");
				} else if (bidDates[i].equals("thisYear")) {
					moreWhere
							.append(" and to_char(bid_date,'YYYY') = to_char(sysdate,'YYYY')");
					filters.append("This Year");
				} else if (bidDates[i].equals("lastYear")) {
					moreWhere
							.append(" and to_char(bid_date,'YYYY') = to_char(add_months(sysdate,-12),'YYYY')");
					filters.append("Last Year");
				}
			}

			filters.append("</td></tr>");
		}
		if (cops != null && !cops[0].equals("")) {
			filters
					.append("<tr><td valign=top class=smallFontBL>Change Order Potentials:</td><td valign=top class=smallFontL>");
			moreWhere.append(" and cop_id in (");

			for (int i = 0; i < cops.length; i++) {
				if (i > 0) {
					filters.append(", ");
					moreWhere.append(",");
				}
				filters.append(TAPcommon.getChangeOrderPotential(cops[i],
						aConnection).getDescription());
				moreWhere.append(cops[i]);
			}

			moreWhere.append(") ");
			filters.append("</td></tr>");
		}
		if (vendors != null && !vendors[0].equals("")) {
			filters.append("<tr><td valign=top class=smallFontBL>Vendors:</td><td valign=top class=smallFontL>");
			moreFrom.append(",target_project_vendors ");
			moreWhere
					.append(" and a.target_project_id = target_project_vendors.target_project_id ");
			moreWhere.append(" and vendor_id in (");

			for (int i = 0; i < vendors.length; i++) {
				if (i > 0) {
					filters.append(", ");
					moreWhere.append(",");
				}
				filters.append(MiscDAO.getVendorName((new Integer(vendors[i])).intValue(), aConnection));
				moreWhere.append(vendors[i]);
			}

			filters.append("</td></tr>");
			moreWhere.append(") ");
		}
		if (products != null && !products[0].equals("")) {
			filters
					.append("<tr><td valign=top class=smallFontBL>Products:</td><td valign=top class=smallFontL>");
			moreFrom.append(", target_project_bom ");
			moreWhere
					.append(" and a.target_project_id = target_project_bom.target_project_id ");
			moreWhere.append(" and product_id in (");

			for (int i = 0; i < products.length; i++) {
				if (i > 0) {
					filters.append(", ");
					moreWhere.append(",");
				}
				filters.append(TAPcommon.getProductDescription(products[i],
						aConnection));
				moreWhere.append("'");
				moreWhere.append(products[i]);
				moreWhere.append("'");
			}

			filters.append("</td></tr>");
			moreWhere.append(") ");
		}
		if (statuses != null && !statuses[0].equals("")) {
			filters
					.append("<tr><td valign=top class=smallFontBL>Statuses:</td><td valign=top class=smallFontL>");
			moreWhere.append(" and status_id in (");

			for (int i = 0; i < statuses.length; i++) {
				if (i > 0) {
					filters.append(", ");
					moreWhere.append(",");
				}
				filters.append(TAPcommon.getProjectStatus(statuses[i],
						aConnection).getDescription());
				moreWhere.append(statuses[i]);
			}

			filters.append("</td></tr>");
			moreWhere.append(") ");
		}
		if (reasons != null && !reasons[0].equals("")) {
			filters
					.append("<tr><td valign=top class=smallFontBL>Reasons:</td><td valign=top class=smallFontL>");
			moreWhere.append(" and strat_reason_id in (");

			for (int i = 0; i < reasons.length; i++) {
				if (i > 0) {
					filters.append(", ");
					moreWhere.append(",");
				}
				filters.append(TAPcommon
						.getStratReason(reasons[i], aConnection)
						.getDescription());
				moreWhere.append(reasons[i]);
			}

			filters.append("</td></tr>");
			moreWhere.append(")");
		}
		if (preferences != null && !preferences[0].equals("")) {
			filters
					.append("<tr><td valign=top class=smallFontBL>Preferences:</td><td valign=top class=smallFontL>");
			moreWhere.append(" and preference_id in (");

			for (int i = 0; i < preferences.length; i++) {
				if (i > 0) {
					filters.append(", ");
					moreWhere.append(",");
				}
				filters.append(TAPcommon.getSpecPreference(preferences[i],
						aConnection).getDescription());
				moreWhere.append(preferences[i]);
			}

			filters.append("</td></tr>");
			moreWhere.append(") ");
		}
		if (zones != null && !zones[0].equals("")) {
			filters
					.append("<tr><td valign=top class=smallFontBL>Zones:</td><td valign=top class=smallFontL>");
			moreWhere.append(" and (");

			for (int i = 0; i < zones.length; i++) {
				if (i > 0) {
					filters.append(", ");
					moreWhere.append(" or ");
				}
				filters.append(MiscDAO.getGeography(zones[i], aConnection)
						.getDescription());
				moreWhere.append("sp_geog like '");
				moreWhere.append(zones[i].substring(0, 4));
				moreWhere.append("%'");
			}

			filters.append("</td></tr>");
			moreWhere.append(") ");
		}
		if (districts != null && !districts[0].equals("")) {
			filters
					.append("<tr><td valign=top class=smallFontBL>Districts:</td><td valign=top class=smallFontL>");
			moreWhere.append(" and (");

			for (int i = 0; i < districts.length; i++) {
				if (i > 0) {
					moreWhere.append(" or ");
					filters.append(", ");
				}
				filters.append(MiscDAO.getGeography(districts[i], aConnection)
						.getDescription());
				moreWhere.append("sp_geog like '");
				moreWhere.append(districts[i]);
				moreWhere.append("%'");
			}

			filters.append("</td></tr>");
			moreWhere.append(") ");
		}
		if (internalStatuses != null && !internalStatuses[0].equals("")) {
			filters
					.append("<tr><td valign=top class=smallFontBL>Internal Statuses:</td><td valign=top class=smallFontL>");
			moreWhere.append(" and internal_status in (");

			for (int i = 0; i < internalStatuses.length; i++) {
				if (i > 0) {
					filters.append(", ");
					moreWhere.append(",");
				}
				if (internalStatuses[i].equals("A")) {
					filters.append("Active");
				} else if (internalStatuses[i].equals("M")) {
					filters.append("Awaiting CHAMPS Mgr Approval");
				} else if (internalStatuses[i].equals("Z")) {
					filters.append("Awaiting Project Sales Mgr Approval");
				} else if (internalStatuses[i].equals("N")) {
					filters.append("Awaiting District Mgr Approval");
				} else if (internalStatuses[i].equals("D")) {
					filters.append("Deleted");
				}

				moreWhere.append("'");
				moreWhere.append(internalStatuses[i]);
				moreWhere.append("'");
			}

			filters.append("</td></tr>");
			moreWhere.append(") ");
		}
		
		if (segments != null && !segments[0].equals("")) {
			filters.append("<tr><td valign=top class=smallFontBL>Segments:</td><td valign=top class=smallFontL>");
			moreWhere.append(" and (");
			moreFrom.append(", customer_segments ");
			moreFrom.append(", customer_project_xref ");
			moreWhere.append(" customer_project_xref.target_project_id = a.target_project_id ");
			moreWhere.append(" and customer_project_xref.vista_customer_number = customer_segments.vista_customer_number ");
			moreWhere.append(" and customer_segments.segment_id in (");

			for (int i = 0; i < segments.length; i++) {
				if (i > 0) {
					moreWhere.append(", ");
					filters.append(", ");
				}
				int segId = Globals.a2int( segments[i] );
				Segment seg = SegmentsDAO.getSegment(segId, aConnection);
				filters.append(seg.getName());
				moreWhere.append("'" + segments[i] + "'");
				
			}

			moreWhere.append(")");
			moreWhere.append(") ");
			filters.append("</td></tr>");
			
		}

		StringBuffer sortMeth = new StringBuffer("job_name");
		StringBuffer sortParms = new StringBuffer("");
		Enumeration parmNames = request.getParameterNames();
		boolean justSorting = false;
		BigDecimal chVal = new BigDecimal(0);
		BigDecimal totVal = new BigDecimal(0);


		while (parmNames.hasMoreElements()) {
			String name = (String) parmNames.nextElement();
			String[] vals = request.getParameterValues(name);

			if (name.equals("sortMeth")) {
				justSorting = true;
				reportBeans = (ArrayList) session.getAttribute("reportBeans");
				for (int i=0; i < reportBeans.size(); i++){
					TargetProjectReportBean bean = (TargetProjectReportBean) reportBeans.get(i);
					if (vals[0].equalsIgnoreCase("internal_status")){
						bean.setSortField(bean.getInternalStatus(), "String");
					} else if (vals[0].equalsIgnoreCase("job_name")){
						bean.setSortField(bean.getJobName(), "String");
					} else if (vals[0].equalsIgnoreCase("sp_geog")){
						bean.setSortField(bean.getDistrict(), "String");
					} else if (vals[0].equalsIgnoreCase("status_id")){
						bean.setSortField(bean.getStatus(), "String");
					} else if (vals[0].equalsIgnoreCase("strat_reason_id")){
						bean.setSortField(bean.getTargetReason(), "String");
					} else if (vals[0].equalsIgnoreCase("preference_id")){
						bean.setSortField(bean.getSpecPreference(), "String");
					} else if (vals[0].equalsIgnoreCase("bid_date")){
						bean.setSortField(bean.getBidDate(), "Date");
					} else if (vals[0].equalsIgnoreCase("ch_value")){
						bean.setSortField(new Double(bean.getEgValue()), "Double");
					} else if (vals[0].equalsIgnoreCase("total_value")){
						bean.setSortField(new Double(bean.getTotalValue()), "Double");
					} else {
						bean.setSortField(bean.getJobName(), "String");
					}
//					 Totals for the report
					chVal = chVal.add(new BigDecimal(bean.getEgValue()));
					totVal = totVal.add(new BigDecimal(bean.getTotalValue()));
				}
	//			sortMeth.delete(0, sortMeth.length() - 1);
	/*			sortMeth.delete(0, sortMeth.length());
				sortMeth.append(vals[0]);
				if (vals[0].equals("ch_value") || vals[0].equals("total_value")) {
					sortMeth.append(" desc");
				}
	*/
			} else {
				for (int i = 0; i < vals.length; i++) {
					sortParms.append("&");
					sortParms.append(name);
					sortParms.append("=");
					sortParms.append(vals[i]);
				}
			}
		}

		// If sorting, do not perform another query
		if (justSorting){
			TargetProjectSorter sorter = new TargetProjectSorter();
			Collections.sort(reportBeans, sorter);
		} else {
			String sel = "";
					
			sel = "select job_name, nvl(ch_value,0) as ch_value, nvl(total_value,0) as total_value, "
		//			+ "to_char(bid_date,'Month DD, YYYY') as date_of_bid," +
				 	+ "bid_date as date_of_bid, "
					+" a.target_project_id, sp_geog, "
					+ "status_id, strat_reason_id, preference_id, internal_status "
					+ "from target_projects a "
					+ moreFrom.toString()
					+ "where 1=1 "
					+ moreWhere.toString()
					+ " order by "
					+ sortMeth.toString();
	
			Statement s = null;
			ResultSet r = null;
			
			try {
				s = aConnection.createStatement();
				if (SMRCLogger.isDebuggerEnabled()) {
					SMRCLogger.debug("SQL - AcctPlanProjRpt.writeTargetProjectResults()\n" + sel);
				}
				r = s.executeQuery(sel);
				while (r.next()) {
					TargetProjectReportBean reportBean = new TargetProjectReportBean();
					if (r.getString("internal_status") != null) {
						reportBean.setInternalStatusId(r.getString("internal_status"));
					} 
					reportBean.setTargetProjectId(r.getInt("target_project_id"));
					reportBean.setJobName(r.getString("job_name"));
					reportBean.setGeog(r.getString("sp_geog"));
					reportBean.setDistrict(MiscDAO.getGeography(r.getString("sp_geog"),aConnection).getDescription());
					reportBean.setStatusId(r.getInt("status_id"));
					reportBean.setStatus(TAPcommon.getProjectStatus(r.getString("status_id"), aConnection).getDescription());
					reportBean.setTargetReasonId(r.getInt("strat_reason_id"));
					reportBean.setTargetReason(TAPcommon.getStratReason(r.getString("strat_reason_id"), aConnection).getDescription());
					reportBean.setSpecPreferenceId(r.getInt("preference_id"));
					reportBean.setSpecPreference(TAPcommon.getSpecPreference(r.getString("preference_id"), aConnection).getDescription());
					if (r.getDate("date_of_bid") != null){
						reportBean.setBidDate(r.getDate("date_of_bid"));
					}
					reportBean.setEgValue(r.getDouble("ch_value"));
					reportBean.setTotalValue(r.getDouble("total_value"));
					reportBean.setSortField(reportBean.getJobName(), "String");
					// Totals for the report
					chVal = chVal.add(new BigDecimal(r.getDouble("ch_value")));
					totVal = totVal.add(new BigDecimal(r.getDouble("total_value")));
					
					
					reportBeans.add(reportBean);
	
				}
			} catch (Exception e) {
				SMRCLogger.error(this.getClass().getName()
						+ ".writeTargetProjectResults() ", e);
				throw e;
			} finally {
				SMRCConnectionPoolUtils.close(r);
				SMRCConnectionPoolUtils.close(s);
			}
		}

		request.setAttribute("filters", filters);
		request.setAttribute("sortParms", sortParms);
		request.setAttribute("chVal", chVal);
		request.setAttribute("totVal", totVal);
		if (session.getAttribute("reportBeans") != null){
			session.removeAttribute("reportBeans");
		}
		session.setAttribute("reportBeans", reportBeans);

		if (request.getParameter("excel") != null){
		    return "/AcctPlanProjRptTargetProjectExcel.jsp";
		} else {
		    return "/AcctPlanProjRptTargetProjectResults.jsp";
		}

	}

	private ArrayList getZones(Connection aConnection) throws Exception {
		ArrayList regions = new ArrayList(10);

		String sel = "SELECT sp_geog,description,zone from geographies where district = '0' and sales_org = '1' and zone like '5%' order by description";
		Statement stmt = null;
		ResultSet response = null;
		try {
			stmt = aConnection.createStatement();
			response = stmt.executeQuery(sel);

			while (response.next()) {
				Region region = new Region();
				region.setRegion(response.getString("zone"));
				region.setDescription(response.getString("description") + " ("
						+ response.getString("zone") + ")");
				region.setSPGeog(response.getString("sp_geog"));

				regions.add(region);
			}

		} catch (Exception e) {
			SMRCLogger.error(this.getClass().getName() + ".getZones() ", e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(response);
			SMRCConnectionPoolUtils.close(stmt);
		}

		return regions;
	}

	private ArrayList getDistricts(Connection aConnection) throws Exception {
		ArrayList districts = new ArrayList(100);

		String sel = "SELECT sp_geog,description,zone,district from geographies where (team is null or team = '' or team = ' ') and district <> '0' and sales_org = '1' and zone like '5%' order by sp_geog";

		Statement stmt = null;
		ResultSet response = null;
		try {
			stmt = aConnection.createStatement();
			response = stmt.executeQuery(sel);

			while (response.next()) {
				Region district = new Region();
				district.setRegion(response.getString("zone"));
				district.setDescription(response.getString("description")
						+ " (" + response.getString("zone") + "-"
						+ response.getString("district") + ")");
				district.setSegment(response.getString("district"));
				district.setSPGeog(response.getString("sp_geog"));

				districts.add(district);
			}

		} catch (Exception e) {
			SMRCLogger.error(this.getClass().getName() + ".getDistricts() ", e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(response);
			SMRCConnectionPoolUtils.close(stmt);
		}

		return districts;
	}

	private ArrayList getChangeOrderPotentials(Connection aConnection)
			throws Exception {
		ArrayList cops = new ArrayList(10);

		String sel = "select * from change_order_potentials";
		Statement s = null;
		ResultSet r = null;
		try {
			s = aConnection.createStatement();
			r = s.executeQuery(sel);

			while (r.next()) {
				ChangeOrderPotential cop = new ChangeOrderPotential();
				cop.setId(r.getInt("cop_id"));
				cop.setDescription(r.getString("description"));

				cops.add(cop);
			}

		} catch (Exception e) {
			SMRCLogger.error(this.getClass().getName()
					+ ".getChangeOrderPotentials() ", e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}

		return cops;
	}

	private ArrayList getStatuses(Connection aConnection) throws Exception {
		ArrayList statuses = new ArrayList(10);

		String sel = "select * from project_statuses";

		Statement s = null;
		ResultSet r = null;
		try {
			s = aConnection.createStatement();
			r = s.executeQuery(sel);

			while (r.next()) {
				ProjectStatus stat = new ProjectStatus();

				stat.setId(r.getInt("status_id"));
				stat.setDescription(r.getString("description"));

				statuses.add(stat);
			}
		} catch (Exception e) {
			SMRCLogger.error(this.getClass().getName() + ".getStatuses() ", e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}

		return statuses;
	}

	private ArrayList getReasons(Connection aConnection) throws Exception {
		ArrayList reasons = new ArrayList(10);

		String sel = "select * from target_reasons";

		Statement s = null;
		ResultSet r = null;
		try {
			s = aConnection.createStatement();
			r = s.executeQuery(sel);

			while (r.next()) {
				TargetReason reas = new TargetReason();

				reas.setId(r.getInt("strat_reason_id"));
				reas.setDescription(r.getString("description"));

				reasons.add(reas);
			}
		} catch (Exception e) {
			SMRCLogger.error(this.getClass().getName() + ".getReasons() ", e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}

		return reasons;
	}

	private ArrayList getPreferences(Connection aConnection) throws Exception {
		ArrayList preferences = new ArrayList(10);

		String sel = "select * from ch_spec_preferences";
		Statement s = null;
		ResultSet r = null;
		try {
			s = aConnection.createStatement();
			r = s.executeQuery(sel);

			while (r.next()) {
				SpecPreference pref = new SpecPreference();

				pref.setId(r.getInt("preference_id"));
				pref.setDescription(r.getString("description"));

				preferences.add(pref);
			}
		} catch (Exception e) {
			SMRCLogger.error(this.getClass().getName() + ".getPreferences() ",
					e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}

		return preferences;
	}

	private ArrayList getVendors(Connection aConnection) throws Exception {
		ArrayList vendors = new ArrayList(10);

		String sel = "select * from vendors";
		Statement s = null;
		ResultSet r = null;
		try {
			s = aConnection.createStatement();
			r = s.executeQuery(sel);

			while (r.next()) {
				Vendor ven = new Vendor();

				ven.setId(r.getInt("vendor_id"));
				ven.setDescription(r.getString("description"));

				vendors.add(ven);
			}
		} catch (Exception e) {
			SMRCLogger.error(this.getClass().getName() + ".getVendors() ", e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}

		return vendors;
	}

	private ArrayList getBidStatuses(Connection aConnection) throws Exception {
		ArrayList bidStats = new ArrayList(10);

		String sel = "select distinct status from bid_tracker where status is not null order by 1 asc";
		Statement s = null;
		ResultSet r = null;
		try {
			s = aConnection.createStatement();
			r = s.executeQuery(sel);

			while (r.next()) {
				String stat = r.getString(1);

				bidStats.add(stat);
			}

		} catch (Exception e) {
			SMRCLogger.error(this.getClass().getName() + ".getBidStatuses() ",
					e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}

		return bidStats;
	}

	private JobType getJobType(String id, Connection aConnection)
			throws Exception {
		JobType jt = new JobType();

		String sel = "select * from job_type where job_type = '" + id + "'";

		Statement s = null;
		ResultSet r = null;
		try {
			s = aConnection.createStatement();
			r = s.executeQuery(sel);

			while (r.next()) {
				jt.setId(r.getString("job_type"));
				jt.setDescription(r.getString("description"));
			}
		} catch (Exception e) {
			SMRCLogger.error(this.getClass().getName() + ".getJobType() ", e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}

		return jt;
	}

	private ArrayList getJobTypes(Connection aConnection) throws Exception {
		ArrayList types = new ArrayList(10);

		String sel = "select * from job_type order by description";
		Statement s = null;
		ResultSet r = null;
		try {
			s = aConnection.createStatement();
			r = s.executeQuery(sel);

			while (r.next()) {
				JobType jt = new JobType();
				jt.setId(r.getString("job_type"));
				jt.setDescription(r.getString("description"));
				types.add(jt);
			}
		} catch (Exception e) {
			SMRCLogger.error(this.getClass().getName() + ".getJobTypes() ", e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}

		return types;
	}
	
	private ArrayList returnSelectedParams(String[] paramValues){
	    ArrayList list = new ArrayList();
	    if (paramValues != null){
	        for (int i=0; i < paramValues.length; i++){
	            if (!paramValues[i].equals("")){
	                list.add(paramValues[i]);
	            }
	        }
	    }
	    return list;
	}
	// This is done here instead of in the jsp so we can loop through and match up any selections
	// with the options when refreshing the page
	private ArrayList loadBidDateRangeOptions(){
	    ArrayList list = new ArrayList();
	    list.add(new DropDownBean("nextYear", "Upcoming Year"));
	    list.add(new DropDownBean("6month", "Next 6 Months"));
	    list.add(new DropDownBean("3month", "Next 3 Months"));
	    list.add(new DropDownBean("nextMonth", "Upcoming Month"));
	    list.add(new DropDownBean("nextWeek", "Upcoming Week"));
	    list.add(new DropDownBean("day", "Previous Day"));
	    list.add(new DropDownBean("week", "Last 7 days"));
	    list.add(new DropDownBean("thisMonth", "This Month"));
	    list.add(new DropDownBean("lastMonth", "Last Month"));
	    list.add(new DropDownBean("thisYear", "This Year"));
	    list.add(new DropDownBean("lastYear", "Last Year"));
	    return list;
	    
	}

} //class