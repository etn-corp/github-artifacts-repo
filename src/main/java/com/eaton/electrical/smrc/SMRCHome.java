// Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.61  2007/10/31 14:45:23  lubbejd
// More changes for RIT32957. Added Module Change requests to Pending Approvals page.
//
// Revision 1.60  2006/03/16 19:13:32  e0073445
// Removed code that was producing warnings.
//
// Revision 1.59  2005/05/05 18:05:54  lubbejd
// More changes for adding the pending approval page (CR30290).
//
// Revision 1.58  2005/05/02 19:16:32  lubbejd
// First changes for adding the pending approval page (CR30290). Also moved
// some methods from OEMAcctPlan to ProjectDAO related to retrieving target
// project information.
//
// Revision 1.57  2005/04/01 18:42:21  lubbejd
// Changes to remove cewolf from chart rendering. Added a lot of features to
// the standard report graphing.
//
// Revision 1.56  2005/03/03 22:01:03  vendejp
// changes so that users with multiple sales ids can see all accounts.
//
// Revision 1.55  2005/01/12 04:50:43  schweks
// Changed to use Globals.a2int instead.
//
// Revision 1.54  2005/01/10 03:00:22  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.53  2005/01/09 05:59:53  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.52  2005/01/05 21:29:17  schweks
// Changed the exception handlers to include extra info in log message.
//
// Revision 1.51  2005/01/05 21:07:06  vendejp
// Manually merged changes and committed.
//
// Added catch for profile exception
//
// Revision 1.50  2005/01/05 20:34:13  lubbejd
// Changes to use Years table instead of sysdate
//
// Revision 1.49  2005/01/05 20:15:07  lubbejd
// Changes to use Years table instead of sysdate
//
// Revision 1.48  2004/12/22 19:39:01  vendejp
// changed DBConn.commit to use the connection pools class to close the conn
//
// Revision 1.47  2004/12/08 21:04:24  schweks
// Updated for new portal.
//
// Revision 1.46  2004/12/07 21:07:20  vendejp
// *** empty log message ***
//
// Revision 1.45  2004/12/01 21:29:38  vendejp
// *** empty log message ***
//
// Revision 1.44  2004/11/24 16:40:09  vendejp
// *** empty log message ***
//
// Revision 1.43  2004/11/17 19:51:38  lubbejd
// Place sales table in an iframe. Minor changes to all jsps inside the iframes for District Home page.
//
// Revision 1.42  2004/11/17 12:51:44  lubbejd
// Beginning of using iframes for zone home charts
//
// Revision 1.41  2004/11/16 19:55:08  lubbejd
// Begin moving home page charts and tables to iframes
//
// Revision 1.40  2004/11/15 07:30:33  schweks
// Changed the way the initial user object is set up.  Not quite the way it should be yet.
//
// Revision 1.39  2004/11/03 16:02:08  lubbejd
// Adding zone maps to zone home and links to district home
//
// Revision 1.38  2004/10/30 22:52:42  vendejp
// Changes to accomodate header requirements.  Made User and Account objects set in the header object where I could.
//
// Revision 1.37  2004/10/29 15:58:30  lubbejd
// changes for sales/orders
//
// Revision 1.36  2004/10/28 16:05:33  lubbejd
// removed hard coded zones from zone home page queries
//
// Revision 1.35  2004/10/28 13:57:52  lubbejd
// More district home page changes
//
// Revision 1.34  2004/10/27 19:23:10  lubbejd
// Changes for district home page
//
// Revision 1.33  2004/10/26 17:16:06  lubbejd
// fix something
//
// Revision 1.32  2004/10/26 16:39:31  lubbejd
// beginning of district home page changes
//
// Revision 1.31  2004/10/21 06:24:36  vendejp
// *** empty log message ***
//
// Revision 1.30  2004/10/19 20:33:21  schweks
// Got a little crazy changing variable names and had to rename request.setAttribute("user" to "usr"
//
// Revision 1.29  2004/10/19 14:59:11  schweks
// Removing unused variables and code.
//
// Revision 1.28  2004/10/18 15:20:37  schweks
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

public class SMRCHome extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String sFwdUrl = "/SMRCErrorPage.jsp";
        boolean redirect = false;
        Connection DBConn = null;
        HttpSession session;
        User user = null;
        try {
            DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();
            session = request.getSession( false );
            // Check to see if session was created
            if ( session == null ) {
                // User's first visit
            	user = MiscDAO.setFirstVisit(request, DBConn);
                session = request.getSession( true );
            } else {
                user = SMRCSession.getUser( request, false, DBConn );
            }

            if (session.getAttribute("srYear") == null){
            	session.setAttribute("srYear", MiscDAO.getSRYear(DBConn));
            }
            if (session.getAttribute("srMonth") == null){
            	session.setAttribute("srMonth", MiscDAO.getSRMonth(DBConn));
            }
            int srYear = Globals.a2int((String) session.getAttribute("srYear"));
            int srMonth = Globals.a2int((String) session.getAttribute("srMonth"));
            
            String page = user.getHomePage();
            String district=request.getParameter("district");
            if(district!=null){
            	page="District";
            }
            
            SMRCHeaderBean hdr = new SMRCHeaderBean();
            hdr.setUser(user);
            request.setAttribute("header", hdr);
            UserApprovals userApprovals = new UserApprovals(user);
            boolean canSeeApprovalLink = false;
            int approvalsPending = 0;
            if (userApprovals.isShowCustomerBox() || userApprovals.isShowDistributorBox()
                || userApprovals.isShowProjectBox() || userApprovals.isShowTargetMarketBox()
                || userApprovals.isShowTerminationBox() || userApprovals.isShowModuleChangeRequestBox()){
                canSeeApprovalLink = true;
                userApprovals.fetchApprovals(DBConn);
                approvalsPending = userApprovals.getPendingApprovals();
            }
            
            int commitmentCount = 0;
            int categoryCount = 0;
            
            if(user.isChannelMarketingManager()) {
            	commitmentCount = CommitmentDAO.getPendingApprovalsForChannel(DBConn).size();
            	categoryCount = ModifySegmentsDAO.getPendingApprovalsForChannel(DBConn).size();
            } else if(user.isDistrictManager()){
            	commitmentCount = CommitmentDAO.getPendingApprovalsForDM(user.getFullName(), DBConn).size();
            	categoryCount = ModifySegmentsDAO.getPendingApprovalsForDM(user.getFullName(), DBConn).size();
            }
            request.setAttribute("canSeeApprovalLink", new Boolean(canSeeApprovalLink));
            request.setAttribute("approvalsPending", new Integer(approvalsPending+commitmentCount+categoryCount));

   //         ArrayList workflowSteps = WorkflowDAO.getWorkflowStepsForUser(user,DBConn);
   //         request.setAttribute("workflowSteps", workflowSteps);

            if (page.equalsIgnoreCase("SE")) {
                String showOrderSales = null;
                if (request.getParameter("show") != null) {
                    if (request.getParameter("show").equals("orders")) {
                        showOrderSales = "orders";
                    } else {
                        showOrderSales = "sales";
                    }
                } else if (user.getShowSalesOrders().equals("o")) {
                    showOrderSales = "orders";
                } else {
                    showOrderSales = "sales";
                }

                ArrayList segments = SegmentsDAO.getSegments(DBConn);
                ArrayList returnSegments = new ArrayList();
                for (int i = 0; i < segments.size(); i++) {
                    Segment segment = (Segment) segments.get(i);
                    segment.setAccounts(SegmentsDAO.getAccounts(segment.getSegmentId(), user, true,
                            showOrderSales, srYear, srMonth, DBConn));
                    returnSegments.add(segment);
                }

                request.setAttribute("segments", returnSegments);
                sFwdUrl = "/SMRCSEHomeDisplay.jsp";

            } else if (page.equalsIgnoreCase("District")) {
                String salesOrders = "sales";
                boolean zoneManager = false;
                if (request.getParameter("salesorders") != null){
                    salesOrders = request.getParameter("salesorders");
                }
                // Passed in from zone home page
                if (request.getParameter("district") != null){
                	district = request.getParameter("district");
                	zoneManager = true;
                } else {
                // If it's a district manager, geography should be a district
                	district = user.getGeography();
                }
                String districtName = "";
                if(district==null){
                	district="";
                } else {
                    districtName = DistrictDAO.getDistrictName(district, DBConn);
                }
                
                               
                session.setAttribute("district", district);
                request.setAttribute("zoneManager", new Boolean(zoneManager));
                request.setAttribute("districtName", districtName);
                request.setAttribute("salesOrders", salesOrders);
                
                SMRCChart goalVsActualChart = new SMRCChart(SMRCChart.BAR_CHART);
                goalVsActualChart.setChartQueryBean(ChartingQueries.getDistrictGoalVsSummaryProductsQuery(district,srYear,srMonth,salesOrders));
                goalVsActualChart.setChartTitle("Goal Vs. Actual");
                session.setAttribute("districtGoalVsActualChart", goalVsActualChart.getChart(DBConn));
                
                SMRCChart marketShareChart = new SMRCChart(SMRCChart.BAR_CHART);
                marketShareChart.setChartQueryBean(ChartingQueries.getDistrictMarketShareQuery(district,srYear));
                marketShareChart.setChartTitle("Market Share");
                session.setAttribute("districtMarketShareChart", marketShareChart.getChart(DBConn));
                
                SMRCChart forecastVsActualChart = new SMRCChart(SMRCChart.BAR_CHART);
                forecastVsActualChart.setChartQueryBean(ChartingQueries.getDistrictForecastVsActualMonthlyQuery(district,srYear,srMonth,salesOrders));
                forecastVsActualChart.setChartTitle("Monthly Forcast Vs. Actual");
                forecastVsActualChart.setShowLegend(false);
                session.setAttribute("districtForecastVsActualChart", forecastVsActualChart.getChart(DBConn));
                
                SMRCChart focusProductsChart = new SMRCChart(SMRCChart.BAR_CHART);
                focusProductsChart.setChartQueryBean(ChartingQueries.getDistrictFocusProductsQuery(district,srYear,srMonth,salesOrders));
                focusProductsChart.setChartTitle("Focus Products");
                session.setAttribute("districtFocusProductsChart", focusProductsChart.getChart(DBConn));
                
                
                
                sFwdUrl = "/SMRCDistrictHomeDisplay.jsp";

            } else if (page.equalsIgnoreCase("Zone")) {

                String salesOrders = "Sales";
                if (request.getParameter("salesorders") != null){
                    salesOrders = request.getParameter("salesorders");
                }
                String zone = user.getGeography();
                String zoneName = DistrictDAO.getDistrictName(zone, DBConn);
                request.setAttribute("zone", zone);
                request.setAttribute("salesOrders", salesOrders);
                request.setAttribute("zoneName", zoneName);
                
                SMRCChart marketShareChart = new SMRCChart(SMRCChart.BAR_CHART);
                marketShareChart.setChartQueryBean(ChartingQueries.getZoneMarketShareQuery(zone));
                marketShareChart.setChartTitle("Market Share For Zone");
                session.setAttribute("zoneMarketShareChart", marketShareChart.getChart(DBConn));
                
                SMRCChart totalSalesChart = new SMRCChart(SMRCChart.BAR_CHART);
                totalSalesChart.setChartQueryBean(ChartingQueries.getZoneTotalSalesQuery(zone,srYear,srMonth,salesOrders));
                totalSalesChart.setChartTitle("Total " + salesOrders);
                totalSalesChart.setShowLegend(false);
                session.setAttribute("zoneTotalSalesChart", totalSalesChart.getChart(DBConn));
                
                SMRCChart salesByProductChart = new SMRCChart(SMRCChart.BAR_CHART);
                salesByProductChart.setChartQueryBean(ChartingQueries.getZoneSalesByProductsLineQuery(zone,srYear,srMonth,salesOrders));
                salesByProductChart.setChartTitle(salesOrders + " By Product Line");
                session.setAttribute("zoneSalesByProductChart", salesByProductChart.getChart(DBConn));
                
                SMRCChart salesByDistrictChart = new SMRCChart(SMRCChart.BAR_CHART);
                salesByDistrictChart.setChartQueryBean(ChartingQueries.getZoneSalesByDistrictQuery(zone,srYear,srMonth,salesOrders));
                salesByDistrictChart.setChartTitle(salesOrders + " By District");
                session.setAttribute("zoneSalesByDistrictChart", salesByDistrictChart.getChart(DBConn));
                
                SMRCChart forecastChart = new SMRCChart(SMRCChart.BAR_CHART);
                forecastChart.setChartQueryBean(ChartingQueries.getZoneForecastVsActualByDistrictQuery(zone,srYear,srMonth,salesOrders));
                forecastChart.setChartTitle("Forecast Versus Actual By District");
                session.setAttribute("zoneForecastChart", forecastChart.getChart(DBConn));
                
                
                sFwdUrl = "/SMRCZoneHome.jsp";

            } else {
                // this is "Other"

                String segmentId = request.getParameter("segmentId");
                ArrayList divisions = DivisionDAO.getDivisions(DBConn);
                request.setAttribute("divisions", divisions);
                ArrayList segments = null;
                if (segmentId != null && segmentId.length() != 0) {
                    segments = SegmentsDAO.getSegments(Globals.a2int((segmentId)), 1, DBConn);
                    Segment currentSegment = SegmentsDAO.getSegment(Globals.a2int(segmentId), DBConn);
                    request.setAttribute("currentSegment", currentSegment);
                    request.setAttribute("segments", segments);
                    if (segments.size() == 0) {
                        redirect = true;
                        sFwdUrl = "/TargetAccountPlanner/CustomerListing?page=listing&otherHomeSegment="
                                + segmentId;
                    } else {
                        sFwdUrl = "/SMRCOtherHomeDisplay.jsp";

                    }
                } else {
                    segments = SegmentsDAO.getSegments(DBConn);
                    request.setAttribute("segments", segments);
                    sFwdUrl = "/SMRCOtherHomeDisplay.jsp";
                }
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
