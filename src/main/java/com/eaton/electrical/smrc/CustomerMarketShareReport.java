//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.2  2006/06/18 17:19:04  e0073445
// Tap Dollars : Paging complete and correct
//
// Revision 1.1  2006/06/16 23:31:54  e0073445
// Good compile, but errors on CMS paging.  I need to get it saved somewhere....
//
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
import java.text.NumberFormat;

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
public class CustomerMarketShareReport extends SMRCBaseServlet {

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

            user = SMRCSession.getUser(request, DBConn);

            SMRCHeaderBean hdr = new SMRCHeaderBean();
            hdr.setUser(user);
            request.setAttribute("header", hdr);

            if (request.getParameter("submit") != null) {
                sFwdUrl = writeReportResults(request, DBConn, user,
                        session);
            } else {
                writeReportOptions(request, DBConn, user);
                sFwdUrl = "/customerMarketShareReport.jsp";
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

    	
		//
		// Get products for pull down list and store in request
		//
	    	
	        // Get year.  This is used for getting the current product list
	        
	        HttpSession session = request.getSession(false);
	        
	        int srYear = Globals.a2int((String) session.getAttribute("srYear"));
	       	// If the year is zero, something went wrong... set to current year
	    	if ( srYear == 0) {
	    		Calendar cal = new GregorianCalendar();
	    	    
	    	    // Get the components of the date
	    	    srYear = cal.get(Calendar.YEAR);
	    	}   	
	    	
	        request.setAttribute("products", ProductDAO.getProductsAlphabetically(srYear, DBConn));
	        
	    
	    // Get segments for jsp output list and store and store in request
	        request.setAttribute("segments", SegmentsDAO.getSegments(0,4,DBConn));
	        
	    // Get applications for jsp output list and store in request
	        request.setAttribute("applications", MiscDAO.getCodes("account_applications", DBConn));
	        
	    // Get the geographies for jsp output list and store in request
	        request.setAttribute("allGeogs", TAPcommon.getGeogsUserCanSee(user,DBConn));
	       
	    // Get the focus types for jsp output list and store in request
	        request.setAttribute("focusTypes", MiscDAO.getFocusTypes(DBConn));
	
	    // Get the Special programs and store in request
	        request.setAttribute("specialPrograms", MiscDAO.getSpecialPrograms(DBConn));
	        
	    // Get the divisions for the jsp output
			request.setAttribute("divisions", DivisionDAO.getDivisions(DBConn));
        
        

    }

    private String writeReportResults(HttpServletRequest request, Connection DBConn, User user,
            HttpSession session) throws ServletException, IOException,
            Exception, SMRCException {

        String[] productFilter = new String[0];
        String[] divisionFilter = new String[0];
        String[] geographyFilter = new String[0];
        String[] focusTypeFilter = new String[0];
        String[] specialProgramFilter = new String[0];
        String[] applicationFilter = new String[0];
        String[] segmentFilter = new String[0];
        
        // This is used to see if there is any filter on the input.  If there is
        // no filter, then we will give oracle a hint (materialize)
        


        Calendar cal = Calendar.getInstance();
        
        cal.setTime(new java.util.Date());
        
        String rptMth = Integer.toString(cal.get(Calendar.MONTH) + 1);
        String rptYr = Integer.toString(cal.get(Calendar.YEAR));
        

        // Braffet : Not sure if we need this for the CMS report yet...still investigating.  Just using
        // current date for now.
        
/*        String rptMth = "0";
        String rptYr = "0";
        String rptMY = request.getParameter("rptMonth");

        rptYr = rptMY.substring(0, 4);
        rptMth = rptMY.substring(4, rptMY.length());
*/
        
        //
        // Paging
        //
        
        int beginWith = 0;
        int endWith = 50;

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

         
        CMSSQLbuilder sqlBuilder = new CMSSQLbuilder(
                rptMth, rptYr);
        
        if (request.getParameter("emailExcel") == null) {
            sqlBuilder.limitResultsReturned(beginWith + 1, endWith);
        }        
        
        CMSTapSQLbuilder sqlTapBuilder = new CMSTapSQLbuilder(rptMth, rptYr);
        
        if (request.getParameter("sortby") != null) {
        	// Have to put quotes around non-dollar fields
        	if ((request.getParameter("sortby").indexOf("description") > -1) ||
       	        (request.getParameter("sortby").indexOf("id") > -1)){
        		sqlBuilder.setOverrideSort(request.getParameter("sortby"), request.getParameter("sortdir"));
        		sqlTapBuilder.setOverrideSort(request.getParameter("sortby"), request.getParameter("sortdir"));
            } else {
            	sqlBuilder.setOverrideSort("\"" + request.getParameter("sortby") + "\"", request.getParameter("sortdir"));
            	sqlTapBuilder.setOverrideSort("\"" + request.getParameter("sortby") + "\"", request.getParameter("sortdir"));
        	}
        }
        
        
        // Start Filters
        
        if (request.getParameter("product") != null) {
            if (request.getParameterValues("product").length > 0
                    && !request.getParameterValues("product")[0].equals("")) {
                productFilter = request.getParameterValues("product");
                ArrayList productValues = new ArrayList();
                for (int i = 0; i < productFilter.length; i++) {
                    productValues.add(productFilter[i]);
                }
                sqlBuilder.useProductFilter(productValues);
                sqlTapBuilder.useProductFilter(productValues);
                
            }
        }

        if (request.getParameter("division") != null) {
            if (request.getParameterValues("division").length > 0
                    && !request.getParameterValues("division")[0].equals("")) {
            	divisionFilter = request.getParameterValues("division");
                ArrayList divisionValues = new ArrayList();
                for (int i = 0; i < divisionFilter.length; i++) {
                	divisionValues.add(divisionFilter[i]);
                }
                sqlBuilder.useDivisionFilter(divisionValues);
                sqlTapBuilder.useDivisionFilter(divisionValues);
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
                sqlTapBuilder.useGeographyFilter(geographyValues);
           }
            
        }
        
        if (request.getParameter("ftype") != null) {
            if (request.getParameterValues("ftype").length > 0
                    && !request.getParameterValues("ftype")[0].equals("")) {
                focusTypeFilter = request.getParameterValues("ftype");
                ArrayList ftypeValues = new ArrayList();
                for (int i = 0; i < focusTypeFilter.length; i++) {
                    ftypeValues.add(focusTypeFilter[i]);
                }
                sqlBuilder.useFocusTypeFilter(ftypeValues);
                sqlTapBuilder.useFocusTypeFilter(ftypeValues);

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
                sqlTapBuilder.useSpecialProgramFilter(specialProgramValues);
            }
            
        }
        
        if (request.getParameter("applications") != null) {
            if (request.getParameterValues("applications").length > 0
                    && !request.getParameterValues("applications")[0].equals("")) {
                applicationFilter = request.getParameterValues("applications");
                ArrayList applicationValues = new ArrayList();
                for (int i = 0; i < applicationFilter.length; i++) {
                    applicationValues.add(applicationFilter[i]);
                }
                sqlBuilder.useApplicationFilter(applicationValues);
                sqlTapBuilder.useApplicationFilter(applicationValues);
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
                sqlTapBuilder.useSegmentFilter(segmentValues);
           }            
 
        }

        //  end of filters
        
        // Set the value if the filter is set.  This allows for Oracle hints
        
//        sqlBuilder.setIsFiltered(isFiltered);

        //  Beginning of group by


        if (request.getParameter("viewAcct") != null) {
            sqlBuilder.setGroupByAccount();
            sqlTapBuilder.setGroupByAccount();
        }
        if (request.getParameter("viewParents") != null) {
            sqlBuilder.setGroupByParent();
            sqlTapBuilder.setGroupByParent();
        }
        if (request.getParameter("viewDistrict") != null) {
            sqlBuilder.setGroupByDistrict();
            sqlTapBuilder.setGroupByDistrict();
        }
        if (request.getParameter("viewTeam") != null) {
            sqlBuilder.setGroupByTeam();
            sqlTapBuilder.setGroupByTeam();
        }
        if (request.getParameter("viewZone") != null) {
            sqlBuilder.setGroupByZone();
            sqlTapBuilder.setGroupByZone();
        }
        if (request.getParameter("viewPrimarySegment") != null) {
            sqlBuilder.setGroupByPrimarySegment();
            sqlTapBuilder.setGroupByPrimarySegment();
        }
        if (request.getParameter("viewSecondarySegment") != null) {
            sqlBuilder.setGroupBySecondarySegment();
            sqlTapBuilder.setGroupBySecondarySegment();
        }
        if (request.getParameter("viewProd") != null) {
            sqlBuilder.setGroupByProduct();
            sqlTapBuilder.setGroupByProduct();
        }
        if (request.getParameter("viewDivision") != null) {
            sqlBuilder.setGroupByDivision();
            sqlTapBuilder.setGroupByDivision();
        }
        if (request.getParameter("viewSE") != null) {
            sqlBuilder.setGroupBySE();
            sqlTapBuilder.setGroupBySE();
        }

        // End of Group by

        // Target Accounts Only?
        
        if (request.getParameter("targetAcctOnly") != null) {
            sqlBuilder.targetAccountsOnly();
            sqlTapBuilder.targetAccountsOnly();
        }

        String theQuery = "";
        
        try {
            theQuery = sqlBuilder.returnSQL(DBConn);
        }catch (Exception e)	{
			SMRCLogger.error("CMSReport.writeReportResults(): ", e);
			throw e;
		}
        
        // We get the Tap dollars as a seprate part.  It is having a hard enough time getting
        // this query
        
        String theTapQuery = "";
        
        try {
        	theTapQuery = sqlTapBuilder.returnSQL(DBConn);
        }catch (Exception e)	{
			SMRCLogger.error("CMSReport.writeReportResults(): ", e);
			throw e;
		}
        
        
        // Get the headerlist if it exists for the sort        
        
        ArrayList results = new ArrayList();
        ArrayList tapResults = new ArrayList();  // Tap Dollar Query Results


        if (SMRCLogger.isDebuggerEnabled()) {
        
        	SMRCLogger.debug("SQL - CMS.writeReportResults()\n" + theQuery);
        	
        }
        
        if (SMRCLogger.isDebuggerEnabled()) {
            
        	SMRCLogger.debug("SQL - CMSTap.writeReportResults()\n" + theTapQuery);
        	
        }
        
        //
        // Get and proccess the results from the main query
        //
        
        results = CMSReportDAO.executeReportSQL(theQuery, DBConn);

        if (results.size() < 50) {
            request.setAttribute("lastPage", new Boolean(true));
        } else {
            request.setAttribute("lastPage", new Boolean(false));
        }
        CMSReportSearchResults resultHeaders = (CMSReportSearchResults) results
                .get(0);
        ArrayList headers = resultHeaders.getHeadings();
        ArrayList sortedHeaders = new ArrayList();
        if (headers.size() > 0) {
            for (int i = 0; i < headers.size(); i++) {
                CMSReportHeader rptHdr = new CMSReportHeader();
                rptHdr.assignHeaderAndSequence((String) headers.get(i), i);
                sortedHeaders.add(rptHdr);
            }

        }

        CMSReportHeaderSorter sorter = new CMSReportHeaderSorter();
        Collections.sort(sortedHeaders, sorter);
        
        //
        // Get and proccess the results from the Tap Dollars
        //
        
//        tapResults = CMSTapReportDAO.executeReportSQL(theTapQuery, DBConn);

        
        //
        // Build the list to be used for the headers
        //
        
        ArrayList vendorList = MiscDAO.getVendors(true, DBConn);
        ArrayList headerList = new ArrayList();
        
        // We just need the first header.  It is the 'dynamic' sort by value
        
        headerList.add(((CMSReportHeader) sortedHeaders.get(0)).getReportHeader());

        // The second and third are hard coded
        
        headerList.add("Total Market Available");
        headerList.add("Eaton's Market Share");

        // Now, get add the vendors from the vendor list
        
        for (int i = 0; i < vendorList.size(); i++ ) {
        	
        	headerList.add(((Vendor)vendorList.get(i)).getDescription());
        	
        	
        } // end iterate through the vendors
        
        // Add the 'Other' value
        
        headerList.add("Other");
        
        // Save the headerList
        
        request.setAttribute("headerList", headerList);
        
        // End get HeaderList
        
        //
        // Build output data
        //

        // First, get the values of vendor per product.  We will have two HashMaps.  The outer will be based on
        // product id.  The inner will be the vendors that have return values for those ids.  The data in the 
        // inner will be the numeric that we are going to output.
        
        HashMap groupByMap = new HashMap();
        HashMap vendorMap = null;
        
        HashMap groupByPotentialMap = new HashMap();
        
        // Iterate through results ArrayList.   The first value is the headers, so skip that
        
        CMSReportSearchResults currentResults = null;
        ArrayList currentRecord = null;
        
//        String currentProduct = null;
        Double currentVendor = null;
        Double currentCompetitorDollars = null;
        String currentGroupByValue = null;
        
        Double summedPotential = null;
        double currentPotentialDollars = 0;
        
        
        
        for (int i = 1; i < results.size(); i ++ ) {
        	
        	currentResults = (CMSReportSearchResults) results.get(i);
        	currentRecord = currentResults.getResultObjects();
        	
        	
        	// The mapping of the record set is :
        	//	0. Group by value
        	//  1. Vendor Id
        	//  2. Competitor Dollar Value
        	// 	3. Potential Dollar Value
        	
        	currentGroupByValue = (String)currentRecord.get(0);
        	
        	// If the vendor is null, then make the vendor number -1.  This will be the 'other' column
        	
        	try {
        	
        		currentVendor = (Double)currentRecord.get(1);
        		
        	} catch (Exception ex) {
        		
        		currentVendor = new Double(-1);
        		
        	}
        	
        	if (currentVendor.doubleValue() == 0.0) {
        		
        		currentVendor = new Double(-1);
        		
        	}
        	
        	
        	currentCompetitorDollars = (Double)currentRecord.get(2);
        	
        	// Get the vendorMap based on the product id.  If it does not exist, create it.  Then 
        	// populate it.
        	
        	vendorMap = (HashMap)groupByMap.get(currentGroupByValue);
        	
        	if (vendorMap == null) {
        		
        		vendorMap = new HashMap();
        		groupByMap.put(currentGroupByValue, vendorMap);
        		
        	}
        	
        	vendorMap.put(currentVendor, currentCompetitorDollars);
        	        	

        	//
        	// Get the potential dollars
        	//
        	
        	// Get the value stored for the currentPotentialDollar from the HashMap
        	
        	currentPotentialDollars = ((Double)currentRecord.get(3)).doubleValue();
        	
        	summedPotential = (Double)groupByPotentialMap.get(currentGroupByValue);
        	
        	if (summedPotential == null) {
        		
        		summedPotential = new Double(0);
        		
        	}
        	
        	// Add the value to the stored value
        	
        	summedPotential = new Double(currentPotentialDollars + summedPotential.doubleValue());
        	groupByPotentialMap.put(currentGroupByValue, summedPotential);
        	
        	
        	
        	currentPotentialDollars = 0;
        	
        } // end itertate through results
        
        //
        // Populate the Tap Dollars values into a hash based on the grouped by selection.  This will be
        // used when populating the below return set
        //
        
        CMSReportSearchResults currentTapResults = null;
        HashMap tapDollarMap = new HashMap();

        for (int i = 0; i < tapResults.size(); i++) {
        	
        	// There are only two fields returned.  The grouped by function and the sum of the tap dollars
        	// values
        	
        	currentTapResults = (CMSReportSearchResults) tapResults.get(i);
        	currentRecord = currentTapResults.getResultObjects();
        	
        	tapDollarMap.put(currentRecord.get(0), currentRecord.get(1));
        	
        	
        } // end iterate through tapResults.size()
        
        
        //
        // Populate the result list.  It will be based on different values from the 
        // the sql result set
        //
        
        ArrayList returnList = new ArrayList();
        ArrayList recordList = null;
        
        int index = 1; 
        String currentValue = null; // This is the grouped by value
        String loopValue = null;
        
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(0);
        
        int innerIndex = 0;
        int skipIndex = 0;
        
        
        while (index < results.size()) {
        	
        	SMRCLogger.debug(new Integer(index));
        	
        	currentResults = (CMSReportSearchResults) results.get(index++);
        	currentRecord = currentResults.getResultObjects();
        	
        	currentValue = (String)currentRecord.get(0);
        	
        	if ((!currentValue.equals(loopValue)) && (innerIndex < 51)) {
        		
        		// We have to get to the index that we begin with first.  Then we start adding to
        		// the result set.
        		
        		if (skipIndex < (beginWith)) {
        			
        			skipIndex ++;
        			
        		} else if (innerIndex < 50){
        		
        			// Only process the first 50.  We iterate one more time to see if there is another page
        			
	        		innerIndex++;
	        		
	        		// The output colums are:
	        		
	        			// 0.  The group by value
	        			// 1.  The Potential value based on the product
	        			// 2.  The Eaton Market Share
	        			// 3.....54?  The vendors...many of them for a pretty web form
	        		
	        		
	        		
	        		// Create the record.  We have a new row
	        		recordList = new ArrayList();
	        		
	        		//
	        		// Add the grouped by value to the output
	        		//
	        		
	        		recordList.add(currentValue);
	        		
	        		//
	        		// Get the potential value and store it based on the product value
	        		//
	        		
	        		currentGroupByValue = (String)currentRecord.get(0);
	        		
	        		recordList.add(nf.format(((Double)groupByPotentialMap.get(currentGroupByValue))).toString());
	        		
	        		// 
	        		// Add the Eaton market value
	        		//
	        		
	        		recordList.add(tapDollarMap.get(currentValue));
	        		
	        		// 
	        		// Populate the vendor values
	        		//
	        		
	        		
	        		// Iterate through each vendor. We must do it from the vendor list 
	        		// Because that is where the order is coming from
	        		
	        		for (int i = 0; i < vendorList.size(); i ++) {
	        			
	        			vendorMap = (HashMap)groupByMap.get(currentGroupByValue);
	        			
	        			if (vendorMap == null) {
	        				
	        				// There is no vendors for this product
	        				recordList.add("$0");
	        				
	        			}
	        			
	       			
	        			currentCompetitorDollars = (Double)vendorMap.get(new Double(((Vendor)vendorList.get(i)).getId()));
	        			
	        			if ((currentCompetitorDollars == null) || (currentCompetitorDollars.doubleValue() == 0.0)) {
	        				
	        				recordList.add("$0");       				
	        				
	        			} else {
	        				
	        				recordList.add(nf.format(currentCompetitorDollars).toString());
	        				
	        			}
	        		} // end iterate through vendorList
	        		
	        		// Add the other column.  This is the value that is in the -1 vendor value
	        		
	    			vendorMap = (HashMap)groupByMap.get(currentGroupByValue);
	    			
	    			if (vendorMap == null) {
	    				
	    				// There is no vendors for this product
	    				recordList.add("$0");
	    				
	    			}
	    			
	   			
	    			currentCompetitorDollars = (Double)vendorMap.get(new Double(-1));
	    			
	    			if ((currentCompetitorDollars == null) || (currentCompetitorDollars.doubleValue() == 0.0)) {
	    				
	    				recordList.add("$0");       				
	    				
	    			} else {
	    				
	    				recordList.add(nf.format(currentCompetitorDollars).toString());
	    				
	    			} 	        		
	        	
	        		// Add the current record to the returnlist (the table)
	    			
	        		returnList.add(recordList);
	        		
        		}  // end if else skipIndex < beginWith
        		
        		//
        		// Set the values for the next iteration
        		//
        		
        		loopValue = currentValue;		        		
       		
        		
        	} // end if !currentValue.equals(loopValue)
        	
    	
        }  // end while index < results.size()
        
    	// If the innerIndex is 50, there there is another page
    	
    	if (innerIndex == 50) {
    		
            request.setAttribute("lastPage", new Boolean(false));
    		
    	} else {
    		
            request.setAttribute("lastPage", new Boolean(true));
    		        		
    	}

    	request.setAttribute("returnList", returnList);
       
/*        request.setAttribute("sortedHeaders", sortedHeaders);
        request.setAttribute("results", results);
        request.setAttribute("sqlBuilder", sqlBuilder);
*/        request.setAttribute("beginWith", new Integer(beginWith));
        request.setAttribute("endWith", new Integer(endWith));
/*        request.setAttribute("recordLinkList", recordLinkList);
        request.setAttribute("multiLevel", new Boolean(sqlBuilder
                .isMultiLevel()));
        request.setAttribute("filterString", filterString);
        request.setAttribute("user", user);
*/    

        String sFwdUrl = "/SMRCErrorPage.jsp";
        if (request.getParameter("emailExcel") != null) {
            sFwdUrl = "/customerMarketShareReportResultsExcel.jsp";
        } else {
            sFwdUrl = "/customerMarketShareReportResults.jsp";
        }
        return sFwdUrl;

    }

 /*   private ArrayList getLinkString(CMSSQLbuilder sqlBuilder,
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
            recordLink = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewAcct=on&parent=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Sales Engineer")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "viewSE=on");
            recordLink = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewAcct=on&se=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Zone")) {
            StringBuffer linkString = removeStringValue(newQueryString, "geography=");
            linkString = removeStringValue(linkString, "viewZone=on");
            recordLink = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewDistrict=on&geography=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("District")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "geography=");
            linkString = removeStringValue(linkString, "viewDistrict=on");
            recordLink = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewTeam=on&geography=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Sales Organization")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "geography=");
            linkString = removeStringValue(linkString, "viewSalesOrg=on");
            recordLink = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewGroupCode=on&geography=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Group Code")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "geography=");
            linkString = removeStringValue(linkString, "viewGroupCode=on");
            recordLink = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewZone=on&geography=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Team")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "geography=");
            linkString = removeStringValue(linkString, "viewTeam=");
            recordLink = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewAcct=on&geography=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Product Line")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "product=");
            linkString = removeStringValue(linkString, "viewProd=on");
            recordLink = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewAcct=on&product=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Summary Product")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "product=");
            linkString = removeStringValue(linkString,
                    "viewSummaryProductline=on");
            recordLink = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewAcct=on&product=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Primary Segment")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "segments=");
            linkString = removeStringValue(linkString, "viewPrimarySegment=on");
            recordLink = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewAcct=on&segments=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Secondary Segment")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "segments=");
            linkString = removeStringValue(linkString,
                    "viewSecondarySegment=on");
            recordLink = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewAcct=on&segments=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Zone / District")) {
            StringBuffer linkString = removeStringValue(newQueryString, "geography=");
            linkString = removeStringValue(linkString, "viewZone=on");
            recordLink = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewDistrict=on&geography=";
        //    linkString = removeStringValue(newQueryString, "district=");
            linkString = removeStringValue(linkString, "viewDistrict=on");
            recordLink2 = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewTeam=on&geography=";
            recordLink3 = "&";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("District / Product")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "geography=");
            linkString = removeStringValue(linkString, "viewDistrict=on");
            recordLink = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewTeam=on&geography=";
            linkString = removeStringValue(newQueryString, "product=");
            linkString = removeStringValue(linkString, "viewProd=on");
            recordLink2 = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewAcct=on&product=";
            recordLink3 = "&geography=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("District / Summary Product")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "geography=");
            linkString = removeStringValue(linkString, "viewDistrict=on");
            recordLink = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewTeam=on&geography=";
            linkString = removeStringValue(newQueryString, "product=");
            linkString = removeStringValue(linkString,
                    "viewSummaryProductline=on");
            recordLink2 = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewAcct=on&product=";
            recordLink3 = "&geography=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("District / Team")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "geography=");
            linkString = removeStringValue(linkString, "viewDistrict=on");
            recordLink = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewTeam=on&geography=";
       //     linkString = removeStringValue(newQueryString, "teams=");
            linkString = removeStringValue(linkString, "viewTeam=on");
            recordLink2 = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewAcct=on&geography=";
            recordLink3 = "&";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Primary Segment / District")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "segments=");
            linkString = removeStringValue(linkString, "viewPrimarySegment=on");
            recordLink = "CustomerMarketShareReport?" + linkString.toString()
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
            recordLink = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewAcct=on&segments=";
            linkString = removeStringValue(newQueryString, "geography=");
            linkString = removeStringValue(linkString, "viewDistrict=on");
            recordLink2 = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewTeam=on&geography=";
            recordLink3 = "&segments=";
        } else if (sqlBuilder.returnDescriptionColumnHeading()
                .equalsIgnoreCase("Parent / Account")) {
            StringBuffer linkString = removeStringValue(newQueryString,
                    "viewParents=on");
            recordLink = "CustomerMarketShareReport?" + linkString.toString()
                    + "&viewAcct=on&parent=";
            recordLink2 = "OEMAcctPlan?page=productmix&cust=";
            recordLink3 = "parent=";
        } else {
            recordLink = "CustomerMarketShareReport?page=sr";
        }

        stringList.add(recordLink);
        if (recordLink2 != null) {
            stringList.add(recordLink2);
            stringList.add(recordLink3);
        }

        return stringList;

    }*/

    //** Remove specified strings from current query string. This is necessary
    // so null values
    //** for the same parameter from previous query are not used
/*    private StringBuffer removeStringValue(StringBuffer queryString,
            String parameter) {
        while ((queryString.toString()).indexOf(parameter) > -1) {
            int beginning = (queryString.toString()).indexOf(parameter);
            int ending = (beginning + parameter.length());
            if (beginning > -1) {
                queryString.delete(beginning, ending);
            }
        }

        return queryString;

    }*/

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
/*    private ArrayList getDollarTypePointLabels(ArrayList dollarTypeRptBeans){
        ArrayList pointLabels = new ArrayList();
        for (int i=0; i < dollarTypeRptBeans.size(); i++){
            DollarTypeRptBean dollarBean = new DollarTypeRptBean();
            dollarBean = (DollarTypeRptBean) dollarTypeRptBeans.get(i);
            String dollarType = dollarBean.getDollarType();
            String labelPrefix = null;
            if (dollarType.equalsIgnoreCase("credit")){
                labelPrefix = "Credit";
            } else if (dollarType.equalsIgnoreCase("end")){
                labelPrefix = "End Mrkt";
            } else {
                labelPrefix = "Charge";
            }
            for (int so=0; so < 2; so++){
                String salesOrders = null;
                boolean keepGoing = false;
                if (so == 0 && dollarBean.useSales()){
                        salesOrders = "Sales";
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
    */

}