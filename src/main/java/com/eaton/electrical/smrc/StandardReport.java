//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.63  2006/03/16 19:13:30  e0073445
// Removed code that was producing warnings.
//
// Revision 1.62  2005/12/12 21:11:33  e0073445
// P163133 - Error when standard report is run for all products.  Added an Oracle hint to 'materialize' the with clause table.
//
// Revision 1.61  2005/04/12 18:15:30  lubbejd
// Add vista customer number to standard report results when grouped
// by account or parent. (CR30102)
//
// Revision 1.60  2005/04/11 14:21:38  lubbejd
// Changes to add special program filtering to standard reports (CR28590).
//
// Revision 1.59  2005/04/08 14:15:16  lubbejd
// Changes to improve and correct the associated users email process from
// the standard reports. Bug# 729. Also added some debug logging of sql
// statement execution.
//
// Revision 1.58  2005/04/01 18:42:21  lubbejd
// Changes to remove cewolf from chart rendering. Added a lot of features to
// the standard report graphing.
//
// Revision 1.57  2005/02/16 14:39:12  lubbejd
// Removed commented out code
//
// Revision 1.56  2005/02/15 19:25:47  lubbejd
// Changes for adding Usage report to Other reports
//
// Revision 1.55  2005/01/28 21:02:55  lubbejd
// Added check for override security to disabling of grouping options.
//
// Revision 1.54  2005/01/25 15:23:28  lubbejd
// Changed how we search for accounts based on the geography filter to use the
// Geography object instead of looking for the first zero in the geography string
//
// Revision 1.53  2005/01/21 14:43:49  lubbejd
// Corrections to segment overrides, some grouping combinations using dollars from
// customer product, disabling of groupBySalesOrg
//
// Revision 1.52  2005/01/18 15:31:53  lubbejd
// Added coding for segment overrides. Some minor cosmetic changes to showing
// the list of filters. Corrected problem of showing geog filters multiple times. Corrected
// some minor query problems.
//
// Revision 1.51  2005/01/14 13:15:30  lubbejd
// Changes to use srYear instead of current year
//
// Revision 1.50  2005/01/12 16:45:15  lubbejd
// Make mailing list query more efficient. Replace sysdate years with srYear in queries.
//
// Revision 1.49  2005/01/12 16:20:55  vendejp
// Added query timeouts to the standard report
//
// Revision 1.48  2005/01/12 04:50:43  schweks
// Changed to use Globals.a2int instead.
//
// Revision 1.47  2005/01/10 03:00:23  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.46  2005/01/09 05:59:54  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.45  2005/01/05 22:40:24  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.44  2004/12/23 18:12:49  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.43  2004/11/11 19:00:38  lubbejd
// Changes to clean up column heading names on graphs
//
// Revision 1.42  2004/11/04 15:36:08  lubbejd
// Use JFreeChart code for StandardReport Graphing
//
// Revision 1.41  2004/10/29 16:39:04  lubbejd
// added log4j around query execution
//
// Revision 1.40  2004/10/28 11:54:35  lubbejd
// Correct creating of links for report results for new geography structure
//
// Revision 1.39  2004/10/26 19:26:47  lubbejd
// Change geography selection and filtering.
//
// Revision 1.38  2004/10/25 19:29:24  lubbejd
// Changes to accomodate application filter
//
// Revision 1.37  2004/10/25 14:08:05  lubbejd
// Changes to standard report to use all segments and align screen. Changed getProductsAlphabetically() in ProductDAO
// to use the current year for period_yyyy.
//
// Revision 1.36  2004/10/19 14:59:11  schweks
// Removing unused variables and code.
//
// Revision 1.35  2004/10/18 20:10:33  schweks
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
public class StandardReport extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session;
        User user = null;
        String sFwdUrl = "/SMRCErrorPage.jsp";
        boolean redirect = false;
        Connection DBConn = null;
        
        try {
            DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();

            session = request.getSession();
            // Don't remove graphing sql if this processing is for mailing list
       //     if (session.getAttribute("standardReportGraphSQL") != null &&
       //         request.getParameter("emailMailingList") == null) {
       //         session.removeAttribute("standardReportGraphSQL");
       //     }
            user = SMRCSession.getUser(request, DBConn);

            SMRCHeaderBean hdr = new SMRCHeaderBean();
            hdr.setUser(user);
            request.setAttribute("header", hdr);

            if (request.getParameter("submit") != null) {
                sFwdUrl = writeReportResults(request, DBConn, user,
                        session);
            } else {
                writeReportOptions(request, DBConn, user);
                sFwdUrl = "/standardReport.jsp";
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
        } catch (SMRCException smrce) {
            /*
             * We only get here if there is a timeout error running the query.
             * The exception was already written as a warning before thrown, so no need to log.
             */ 
        	SMRCConnectionPoolUtils.rollbackTransaction(DBConn);

            request.setAttribute("exception", smrce.getMessage());
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

    private void writeReportOptions(HttpServletRequest request, Connection DBConn, User user)
            throws ServletException, IOException, Exception {

        ArrayList focusTypes = MiscDAO.getFocusTypes(DBConn);
        HttpSession session = request.getSession(false);
        int srYear = Globals.a2int((String) session.getAttribute("srYear"));
       	// If the year is zero, something went wrong... set to current year
    	if ( srYear == 0) {
    		Calendar cal = new GregorianCalendar();
    	    
    	    // Get the components of the date
    	    srYear = cal.get(Calendar.YEAR);
    	}
    	boolean hasSegmentOverride = false;
     	ArrayList segmentOverrideList = user.getSegmentOverrides();
     	if (segmentOverrideList.size() > 0){
     		hasSegmentOverride = true;
     	}
        ArrayList products = ProductDAO.getProductsWithDivision(srYear, DBConn);
        
        ArrayList segments = SegmentsDAO.getSegments(0,4,DBConn);
        ArrayList applications = MiscDAO.getCodes("account_applications", DBConn);

        String acctDisable = "disabled", groupDisable = "disabled", zoneDisable = "disabled", districtDisable = "disabled", teamDisable = "disabled", seDisable = "disabled", salesorgDisable = "disabled";

        if (user.ableToViewEverything() || user.hasOverrideSecurity() || (hasSegmentOverride)) {
            acctDisable = salesorgDisable = groupDisable = zoneDisable = districtDisable = teamDisable = seDisable = "";
        } else {
            ArrayList ugsList = user.getAllUGS();
            for (int i = 0; i < ugsList.size(); i++) {
                UserGeogSecurity ugs = (UserGeogSecurity) ugsList.get(i);
                if (ugs.ableToSeeNationalLevel()) {
                    salesorgDisable = "";
                }
                if (ugs.ableToSeeGroupLevel()) {
                    groupDisable = "";
                }
                if (ugs.ableToSeeZoneLevel()) {
                    zoneDisable = "";
                }
                if (ugs.ableToSeeDistrictLevel()) {
                    districtDisable = "";
                }
                if (ugs.ableToSeeTeamLevel()) {
                    teamDisable = "";
                }
                if (ugs.ableToViewSalesman()) {
                    acctDisable = "";
                    seDisable = "";
                }
            }
        }

                
        ArrayList allGeogs = TAPcommon.getGeogsUserCanSee(user,DBConn);
        
        request.setAttribute("allGeogs", allGeogs);
        request.setAttribute("focusTypes", focusTypes);
        request.setAttribute("products", products);
        request.setAttribute("segments", segments);
        request.setAttribute("applications", applications);
        request.setAttribute("acctDisable", acctDisable);
        request.setAttribute("groupDisable", groupDisable);
        request.setAttribute("zoneDisable", zoneDisable);
        request.setAttribute("districtDisable", districtDisable);
        request.setAttribute("teamDisable", teamDisable);
        request.setAttribute("seDisable", seDisable);
        request.setAttribute("salesorgDisable", salesorgDisable);
        request.setAttribute("specialPrograms", MiscDAO.getSpecialPrograms(DBConn));
        

    }

    private String writeReportResults(HttpServletRequest request, Connection DBConn, User user,
            HttpSession session) throws ServletException, IOException,
            Exception, SMRCException {

        boolean emailMailingList = false;

        String[] productFilter = new String[0];
        String[] ftypeFilter = new String[0];
        String[] parentFilter = new String[0];
        String[] seFilter = new String[0];
        String[] segmentFilter = new String[0];
        String[] applicationFilter = new String[0];
        String[] geographyFilter = new String[0];
        String[] specialProgramFilter = new String[0];
        
        // This is used to see if there is any filter on the input.  If there is
        // no filter, than we will give oracle a hint (materialize)
        
        boolean isFiltered = false; 

        String rptMth = "0";
        String rptYr = "0";
        String rptMY = request.getParameter("rptMonth");

        rptYr = rptMY.substring(0, 4);
        rptMth = rptMY.substring(4, rptMY.length());

        int beginWith = 0;
        int endWith = 50;

        if (request.getParameter("emailMailingList") != null) {
            emailMailingList = true;
        }

        if (request.getParameter("beginWith") != null) {
            beginWith = Globals.a2int(request.getParameter("beginWith"));
        }
        if (request.getParameter("endWith") != null) {
            endWith = Globals.a2int(request.getParameter("endWith"));
        }

        if (request.getParameter("prevPage") != null) {
            beginWith -= 50;
            endWith -= 50;
        }

        if (request.getParameter("nextPage") != null) {
            beginWith += 50;
            endWith += 50;
        }

        int srYear = Globals.a2int((String) session.getAttribute("srYear"));
        int srMonth = Globals.a2int((String) session.getAttribute("srMonth"));
        
        StandardReportSQLbuilder sqlBuilder = new StandardReportSQLbuilder(
                rptMth, rptYr);

        sqlBuilder.setUser(user);
        sqlBuilder.setSRMonth(srMonth);
        sqlBuilder.setSRYear(srYear);
        

        if ((request.getParameter("emailExcel") == null) && (!emailMailingList)) {
            sqlBuilder.limitResultsReturned(beginWith + 1, endWith);
        }

        if ((request.getParameter("sortby") != null) && (!emailMailingList)) {
        	// Have to put quotes around non-dollar fields
        	if ((request.getParameter("sortby").indexOf("description") > -1) ||
       	        (request.getParameter("sortby").indexOf("id") > -1)){
        		sqlBuilder.setOverrideSort(request.getParameter("sortby"), request.getParameter("sortdir"));
            } else {
            	sqlBuilder.setOverrideSort("\"" + request.getParameter("sortby") + "\"", request.getParameter("sortdir"));
        	}
        }

        // Beginning of Filters

        if (request.getParameter("ftype") != null) {
            if (request.getParameterValues("ftype").length > 0
                    && !request.getParameterValues("ftype")[0].equals("")) {
                ftypeFilter = request.getParameterValues("ftype");
                ArrayList ftypeValues = new ArrayList();
                for (int i = 0; i < ftypeFilter.length; i++) {
                    ftypeValues.add(ftypeFilter[i]);
                }
                sqlBuilder.useFocusTypeFilter(ftypeValues);
                
                isFiltered = true;

            }
        }

        if (request.getParameter("product") != null) {
            if (request.getParameterValues("product").length > 0
                    && !request.getParameterValues("product")[0].equals("")) {
                productFilter = request.getParameterValues("product");
                ArrayList productValues = new ArrayList();
                for (int i = 0; i < productFilter.length; i++) {
                    productValues.add(productFilter[i]);
                }
                sqlBuilder.useProductFilter(productValues);
                
                isFiltered = true;
            }
        }

        if (request.getParameter("segments") != null) {
            if (request.getParameterValues("segments").length > 0
                    && !request.getParameterValues("segments")[0].equals("")) {
                segmentFilter = request.getParameterValues("segments");
                ArrayList segmentValues = new ArrayList();
                for (int i = 0; i < segmentFilter.length; i++) {
                    segmentValues.add(segmentFilter[i]);
                }
                sqlBuilder.useSegmentFilter(segmentValues);
                isFiltered = true;
           }            
 
        }
        
        if (request.getParameter("app1ications") != null) {
            if (request.getParameterValues("app1ications").length > 0
                    && !request.getParameterValues("app1ications")[0].equals("")) {
                applicationFilter = request.getParameterValues("app1ications");
                ArrayList applicationValues = new ArrayList();
                for (int i = 0; i < applicationFilter.length; i++) {
                    applicationValues.add(applicationFilter[i]);
                }
                sqlBuilder.useApplicationFilter(applicationValues);
                isFiltered = true;
            }
            

        }
        
        if (request.getParameter("geography") != null) {
            if (request.getParameterValues("geography").length > 0
                    && !request.getParameterValues("geography")[0].equals("")) {
                geographyFilter = request.getParameterValues("geography");
                ArrayList geographyValues = new ArrayList();
                for (int i = 0; i < geographyFilter.length; i++) {
                    geographyValues.add(geographyFilter[i]);
                }
                sqlBuilder.useGeographyFilter(geographyValues);
                isFiltered = true;
           }
            
        }

        if (request.getParameter("parent") != null) {
            if (request.getParameterValues("parent").length > 0
                    && !request.getParameterValues("parent")[0].equals("")) {
                parentFilter = request.getParameterValues("parent");
                ArrayList parentValues = new ArrayList();
                for (int i = 0; i < parentFilter.length; i++) {
                    parentValues.add(parentFilter[i]);
                }
                sqlBuilder.useParentFilter(parentValues);

            }
        }

        if (request.getParameter("se") != null) {
            if (request.getParameterValues("se").length > 0
                    && !request.getParameterValues("se")[0].equals("")) {
                seFilter = request.getParameterValues("se");
                ArrayList seValues = new ArrayList();
                for (int i = 0; i < seFilter.length; i++) {
                    seValues.add(seFilter[i]);
                }
                sqlBuilder.useSalesEngineerFilter(seValues);
            }
        }
        
        if (request.getParameter("specialPrograms") != null) {
            if (request.getParameterValues("specialPrograms").length > 0
                    && !request.getParameterValues("specialPrograms")[0].equals("")) {
                specialProgramFilter = request.getParameterValues("specialPrograms");
                ArrayList specialProgramValues = new ArrayList();
                for (int i = 0; i < specialProgramFilter.length; i++) {
                    specialProgramValues.add(specialProgramFilter[i]);
                }
                sqlBuilder.useSpecialProgramFilter(specialProgramValues);
                isFiltered = true;
            }
            
        }

        //  end of filters
        
        // Set the value if the filter is set
        
        sqlBuilder.setIsFiltered(isFiltered);

        //  Beginning of group by

        if (emailMailingList) {
            sqlBuilder.setGroupByAccount();
        } else {
            if (request.getParameter("viewProd") != null) {
                sqlBuilder.setGroupByProduct();
            }
            if (request.getParameter("viewAcct") != null) {
                sqlBuilder.setGroupByAccount();
            }
            if (request.getParameter("viewParents") != null) {
                sqlBuilder.setGroupByParent();
            }
            if (request.getParameter("viewZone") != null) {
                sqlBuilder.setGroupByZone();
            }
            if (request.getParameter("viewDistrict") != null) {
                sqlBuilder.setGroupByDistrict();
            }
            if (request.getParameter("viewSE") != null) {
                sqlBuilder.setGroupBySE();
            }
            if (request.getParameter("viewSummaryProductline") != null) {
                sqlBuilder.setGroupBySummaryProduct();
            }
            if (request.getParameter("viewPrimarySegment") != null) {
                sqlBuilder.setGroupByPrimarySegment();
            }
            if (request.getParameter("viewSecondarySegment") != null) {
                sqlBuilder.setGroupBySecondarySegment();
            }
            if (request.getParameter("viewTeam") != null) {
                sqlBuilder.setGroupByTeam();
            }
            if (request.getParameter("viewSalesOrg") != null) {
                sqlBuilder.setGroupBySalesOrg();
            }
            if (request.getParameter("viewGroupCode") != null) {
                sqlBuilder.setGroupByGroupCode();
            }
        }

        // End of Group by

        //  Beginning of Dollar Types

        // pointLabels are used for charting. These are the labels of the sql columns.
        // Make sure they match the labels assigned in StandardReportSQLBuilder.
        ArrayList pointLabels = new ArrayList();
        
        if (request.getParameter("showPotential") != null) {
            sqlBuilder.showPotentialDollars();
            pointLabels.add("Potential $");
        }
        if (request.getParameter("showForcast") != null) {
            sqlBuilder.showForecastDollars();
            pointLabels.add("Forecast $");
        }
        if (request.getParameter("showCompetitor") != null) {
            sqlBuilder.showCompetitorDollars();
            pointLabels.add("Competitor $");
        }

        if (request.getParameter("targetAcctOnly") != null) {
            sqlBuilder.targetAccountsOnly();
        }

        ArrayList dollarTypes = new ArrayList();

        

        // End Market Dollars   - now TAP Dollars
        if (request.getParameterValues("em_sal_ord") != null) {
            String[] vals = request.getParameterValues("em_sal_ord");
            DollarTypeRptBean endDollarType = new DollarTypeRptBean("end");

            for (int i = 0; i < vals.length; i++) {
                if (vals[i].equals("sal")) {
                    endDollarType.setSales();
                } else if (vals[i].equals("ord")) {
                    endDollarType.setOrders();
                }
            }
            if (request.getParameter("emsal_curmo") != null) {
                endDollarType.setMonthly();
            }

            if (request.getParameter("emsal_curytd") != null) {
                endDollarType.setYTDmonthly();
            }
            if (request.getParameter("emsal_prevytd") != null) {
                endDollarType.setPrevYTDmonthly();
            }
            if (request.getParameter("emsal_prevtot") != null) {
                endDollarType.setPrevYearTotal();
            }
            if (request.getParameter("emsal_mtm") != null) {
                endDollarType.setPrevYearMonthly();
            }
            dollarTypes.add(endDollarType);
        }

        

        // end of Dollar Types

        sqlBuilder.setDollarTypes(dollarTypes);
        // Set graphSQLBuilder before declaring sqlBuilder is for emailList
        StandardReportSQLbuilder graphSQLBuilder = (StandardReportSQLbuilder) sqlBuilder.clone();
        StandardReportSQLbuilder assocUsersSQLBuilder = (StandardReportSQLbuilder) sqlBuilder.clone();
        if (emailMailingList){
        	sqlBuilder.setEmailingListOnly();
        }
        String theQuery = "";
        try {
            theQuery = sqlBuilder.returnSQL(DBConn);
        }catch (Exception e)	{
			SMRCLogger.error("StandardReport.writeReportResults(): ", e);
			throw e;
		}
        String filterString = sqlBuilder.returnFilterString(DBConn);
        String securityString = sqlBuilder.returnSecurityFilterString(DBConn);
        graphSQLBuilder.turnOffLimitResults();
        graphSQLBuilder.setGraphSQL(true);
        String graphSQL = "";
        String assocUsersSQL = "";
        assocUsersSQLBuilder.setAssociatedUsersSQL(true);
        try {
        	graphSQL = graphSQLBuilder.returnSQL(DBConn);
            SMRCLogger.debug("graphSQL: " + graphSQL);
            assocUsersSQL = assocUsersSQLBuilder.returnSQL(DBConn);
        }catch (Exception e)	{
			SMRCLogger.error("StandardReport.writeReportResults(): ", e);
			throw e;
		}
        if (!emailMailingList){
            ChartQueryBean bean = new ChartQueryBean();
            bean.setTheQuery(graphSQL);
            bean.setSeriesLabel("DESCRIPTION");
            pointLabels.addAll(getDollarTypePointLabels(dollarTypes));
            bean.setPointLabels(pointLabels);
        	session.setAttribute("standardReportChartQueryBean", bean);
        	session.setAttribute("assocUsersSQL", assocUsersSQL);
        }

        ArrayList results = new ArrayList();

        if (emailMailingList) {
            SMRCLogger.debug("SQL - StandardReport.writeReportResults()\n" + theQuery);
            results = ReportDAO.executeMailingListSQL(theQuery, DBConn);
            request.setAttribute("mailingListBeans", results);
        } else {
            SMRCLogger.debug("SQL - StandardReport.writeReportResults()\n" + theQuery);
            results = ReportDAO.executeReportSQL(theQuery, DBConn);

            if (results.size() < 50) {
                request.setAttribute("lastPage", new Boolean(true));
            } else {
                request.setAttribute("lastPage", new Boolean(false));
            }
            StandardReportSearchResults resultHeaders = (StandardReportSearchResults) results
                    .get(0);
            ArrayList headers = resultHeaders.getHeadings();
            ArrayList sortedHeaders = new ArrayList();
            if (headers.size() > 0) {
                for (int i = 0; i < headers.size(); i++) {
                    StandardReportHeader rptHdr = new StandardReportHeader();
                    rptHdr.assignHeaderAndSequence((String) headers.get(i), i);
                    sortedHeaders.add(rptHdr);
                }

            }

            ReportHeaderSorter sorter = new ReportHeaderSorter();
            Collections.sort(sortedHeaders, sorter);

            ArrayList recordLinkList = getLinkString(sqlBuilder, request);

            request.setAttribute("sortedHeaders", sortedHeaders);
            request.setAttribute("results", results);
            request.setAttribute("sqlBuilder", sqlBuilder);
            request.setAttribute("beginWith", new Integer(beginWith));
            request.setAttribute("endWith", new Integer(endWith));
            request.setAttribute("recordLinkList", recordLinkList);
            request.setAttribute("multiLevel", new Boolean(sqlBuilder
                    .isMultiLevel()));
            request.setAttribute("filterString", filterString);
            request.setAttribute("securityString", securityString);
            request.setAttribute("user", user);
        }

        String sFwdUrl = "/SMRCErrorPage.jsp";
        if (request.getParameter("emailExcel") != null) {
            sFwdUrl = "/standardReportResultsExcel.jsp";
        } else if (emailMailingList) {
            sFwdUrl = "/standardReportMailingListExcel.jsp";
        } else {
            sFwdUrl = "/standardReportResults.jsp";
        }
        return sFwdUrl;

    }

    private ArrayList getLinkString(StandardReportSQLbuilder sqlBuilder,
            HttpServletRequest request) {
        ArrayList stringList = new ArrayList();
        String queryString = request.getQueryString();

        //   We don't want any sort or prev/next information here
        int pageLoc = -1;
        if (queryString.indexOf("page") > 0) {
            pageLoc = queryString.indexOf("page");
        }
        StringBuffer newQueryString = new StringBuffer(queryString);
        if (pageLoc >= 0) {
            newQueryString.delete(0, pageLoc);
        }

        String recordLink = null;
        String recordLink2 = null;
        String recordLink3 = null;

        // Sometimes recordsLink3 is set to "&" because the additional parameter
        // should not be used when multilevel groupings are both geographies
        if (sqlBuilder.returnDescriptionColumnHeading().equalsIgnoreCase(
                "Account")) {
            recordLink = "OEMAcctPlan?page=productmix&cust=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Parent")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "viewParents=on");
            recordLink = "StandardReport?" + linkString.toString()
                    + "&viewAcct=on&parent=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Sales Engineer")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "viewSE=on");
            recordLink = "StandardReport?" + linkString.toString()
                    + "&viewAcct=on&se=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Zone")) {
            StringBuffer linkString = removeStringValue(newQueryString, "geography=");
            linkString = removeStringValue(linkString, "viewZone=on");
            recordLink = "StandardReport?" + linkString.toString()
                    + "&viewDistrict=on&geography=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("District")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "geography=");
            linkString = removeStringValue(linkString, "viewDistrict=on");
            recordLink = "StandardReport?" + linkString.toString()
                    + "&viewTeam=on&geography=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Sales Organization")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "geography=");
            linkString = removeStringValue(linkString, "viewSalesOrg=on");
            recordLink = "StandardReport?" + linkString.toString()
                    + "&viewGroupCode=on&geography=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Group Code")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "geography=");
            linkString = removeStringValue(linkString, "viewGroupCode=on");
            recordLink = "StandardReport?" + linkString.toString()
                    + "&viewZone=on&geography=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Team")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "geography=");
            linkString = removeStringValue(linkString, "viewTeam=");
            recordLink = "StandardReport?" + linkString.toString()
                    + "&viewAcct=on&geography=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Product Line")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "product=");
            linkString = removeStringValue(linkString, "viewProd=on");
            recordLink = "StandardReport?" + linkString.toString()
                    + "&viewAcct=on&product=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Summary Product")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "product=");
            linkString = removeStringValue(linkString,
                    "viewSummaryProductline=on");
            recordLink = "StandardReport?" + linkString.toString()
                    + "&viewAcct=on&product=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Primary Segment")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "segments=");
            linkString = removeStringValue(linkString, "viewPrimarySegment=on");
            recordLink = "StandardReport?" + linkString.toString()
                    + "&viewAcct=on&segments=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Secondary Segment")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "segments=");
            linkString = removeStringValue(linkString,
                    "viewSecondarySegment=on");
            recordLink = "StandardReport?" + linkString.toString()
                    + "&viewAcct=on&segments=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Zone / District")) {
            StringBuffer linkString = removeStringValue(newQueryString, "geography=");
            linkString = removeStringValue(linkString, "viewZone=on");
            recordLink = "StandardReport?" + linkString.toString()
                    + "&viewDistrict=on&geography=";
        //    linkString = removeStringValue(newQueryString, "district=");
            linkString = removeStringValue(linkString, "viewDistrict=on");
            recordLink2 = "StandardReport?" + linkString.toString()
                    + "&viewTeam=on&geography=";
            recordLink3 = "&";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("District / Product")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "geography=");
            linkString = removeStringValue(linkString, "viewDistrict=on");
            recordLink = "StandardReport?" + linkString.toString()
                    + "&viewTeam=on&geography=";
            linkString = removeStringValue(newQueryString, "product=");
            linkString = removeStringValue(linkString, "viewProd=on");
            recordLink2 = "StandardReport?" + linkString.toString()
                    + "&viewAcct=on&product=";
            recordLink3 = "&geography=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("District / Summary Product")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "geography=");
            linkString = removeStringValue(linkString, "viewDistrict=on");
            recordLink = "StandardReport?" + linkString.toString()
                    + "&viewTeam=on&geography=";
            linkString = removeStringValue(newQueryString, "product=");
            linkString = removeStringValue(linkString,
                    "viewSummaryProductline=on");
            recordLink2 = "StandardReport?" + linkString.toString()
                    + "&viewAcct=on&product=";
            recordLink3 = "&geography=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("District / Team")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "geography=");
            linkString = removeStringValue(linkString, "viewDistrict=on");
            recordLink = "StandardReport?" + linkString.toString()
                    + "&viewTeam=on&geography=";
       //     linkString = removeStringValue(newQueryString, "teams=");
            linkString = removeStringValue(linkString, "viewTeam=on");
            recordLink2 = "StandardReport?" + linkString.toString()
                    + "&viewAcct=on&geography=";
            recordLink3 = "&";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Primary Segment / District")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "segments=");
            linkString = removeStringValue(linkString, "viewPrimarySegment=on");
            recordLink = "StandardReport?" + linkString.toString()
                    + "&viewAcct=on&segments=";
            linkString = removeStringValue(newQueryString, "geography=");
            linkString = removeStringValue(linkString, "viewDistrict=on");
            recordLink2 = "StandardReport?" + linkString.toString()
                    + "&viewTeam=on&geography=";
            recordLink3 = "&segments=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Secondary Segment / District")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "segments=");
            linkString = removeStringValue(linkString,
                    "viewSecondarySegment=on");
            recordLink = "StandardReport?" + linkString.toString()
                    + "&viewAcct=on&segments=";
            linkString = removeStringValue(newQueryString, "geography=");
            linkString = removeStringValue(linkString, "viewDistrict=on");
            recordLink2 = "StandardReport?" + linkString.toString()
                    + "&viewTeam=on&geography=";
            recordLink3 = "&segments=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Parent / Account")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "viewParents=on");
            recordLink = "StandardReport?" + linkString.toString()
                    + "&viewAcct=on&parent=";
            recordLink2 = "OEMAcctPlan?page=productmix&cust=";
            recordLink3 = "parent=";
        } else {
            recordLink = "StandardReport?page=sr";
        }

        stringList.add(recordLink);
        if (recordLink2 != null) {
            stringList.add(recordLink2);
            stringList.add(recordLink3);
        }

        return stringList;

    }

    //** Remove specified strings from current query string. This is necessary
    // so null values
    //** for the same parameter from previous query are not used
    private StringBuffer removeStringValue(StringBuffer queryString,
            String parameter) {
        while ((queryString.toString()).indexOf(parameter) > -1) {
            int beginning = (queryString.toString()).indexOf(parameter);
            int ending = (beginning + parameter.length());
            if (beginning > -1) {
                queryString.delete(beginning, ending);
            }
        }

        return queryString;

    }

    /*
    private ArrayList checkUserGeogAccess(ArrayList geogList, User user) {
        ArrayList returnList = new ArrayList();
        for (int i = 0; i < geogList.size(); i++) {
            Geography geog = (Geography) geogList.get(i);
            if (user.ableToSee(geog.getGeog())) {
                returnList.add(geogList.get(i));
            }

        }
        return returnList;
    }
*/    
    // This method returns an ArrayList of pointLabels to be used in charting
    // The text of the label is the same that is assigned in StandardReportSQLBuilder
    private ArrayList getDollarTypePointLabels(ArrayList dollarTypeRptBeans){
        ArrayList pointLabels = new ArrayList();
        for (int i=0; i < dollarTypeRptBeans.size(); i++){
            DollarTypeRptBean dollarBean = new DollarTypeRptBean();
            dollarBean = (DollarTypeRptBean) dollarTypeRptBeans.get(i);
            String dollarType = dollarBean.getDollarType();
            String labelPrefix = null;
            /*  only using Tap dollars now
            if (dollarType.equalsIgnoreCase("credit")){
                labelPrefix = "Credit";
            } else if (dollarType.equalsIgnoreCase("end")){
                labelPrefix = "End Mrkt";
            } else {
                labelPrefix = "Charge";
            }
            */
            labelPrefix = "Tap";
            for (int so=0; so < 2; so++){
                String salesOrders = null;
                boolean keepGoing = false;
                if (so == 0 && dollarBean.useSales()){
                        salesOrders = "Invoice";  // no longer Sales
                        keepGoing = true;
                } else if (so == 1 && dollarBean.useOrders()){
                        salesOrders = "Order";
                        keepGoing = true;
                } else {
                        keepGoing = false;
                }
                                
                if (keepGoing){
                    	if (dollarBean.useMonthly()){
                        	String label = labelPrefix + " Month " + salesOrders;
                        	pointLabels.add(label);
                        }
                    	if (dollarBean.useYTDmonthly()){
                            String label = labelPrefix + " YTD " + salesOrders;
                            pointLabels.add(label);
                        }
                        if (dollarBean.usePrevYTDmonthly()){
                            String label = labelPrefix + " Prev YTD " + salesOrders;
                            pointLabels.add(label);
                        }
                        if (dollarBean.usePrevYearTotal()){
                        	String label = labelPrefix + " Prev Year " + salesOrders;
                        	pointLabels.add(label);
                        }
                        if (dollarBean.usePrevYearMonthly()){
                        	String label = labelPrefix + " Prev YR Month " + salesOrders;
                        	pointLabels.add(label);
                        }
                }
            
            }
             
        }
        
        return pointLabels;
    }

}