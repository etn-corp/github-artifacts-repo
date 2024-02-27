//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.24  2007/04/06 17:37:25  lubbejd
// Added start/end date filter to target market reports.
//
// Revision 1.23  2006/03/16 19:13:32  e0073445
// Removed code that was producing warnings.
//
// Revision 1.22  2005/06/28 18:39:20  lubbejd
// merged changes from QA_1_2_1.
//
// Revision 1.21.6.1  2005/06/22 17:18:12  lubbejd
// Changed account ArrayList to use Strings instead Account objects.
//
// Revision 1.21  2005/01/12 05:04:07  schweks
// Changed new Integer().intValue() occurrences to Globals.a2int() instead.
//
// Revision 1.20  2005/01/10 22:17:24  vendejp
// code cleanup
//
// Revision 1.19  2005/01/10 03:00:23  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.18  2005/01/09 05:59:53  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.17  2005/01/05 22:40:24  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.16  2004/12/23 18:12:49  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.15  2004/10/27 14:14:25  lubbejd
// Remove references to Bidman Rebate for CIFD
//
// Revision 1.14  2004/10/25 12:04:18  lubbejd
// fix sorting headings
//
// Revision 1.13  2004/10/24 21:33:34  lubbejd
// added sorting by headers
//
// Revision 1.12  2004/10/24 20:52:59  lubbejd
// add links to result descriptions
//
// Revision 1.11  2004/10/22 19:23:48  lubbejd
// work in progress
//
// Revision 1.10  2004/10/22 18:09:57  lubbejd
// work in progress
//
// Revision 1.9  2004/10/22 15:05:47  lubbejd
// work in progress
//
// Revision 1.8  2004/10/22 13:40:48  lubbejd
// work in progress
//
// Revision 1.7  2004/10/22 13:03:09  lubbejd
// work in progress
//
// Revision 1.6  2004/10/21 19:26:45  lubbejd
// work in progress
//
// Revision 1.5  2004/10/21 18:10:19  lubbejd
// work in progress
//
// Revision 1.4  2004/10/21 16:52:47  lubbejd
// work in progress
//
// Revision 1.3  2004/10/20 17:43:07  lubbejd
// work in progress
//
// Revision 1.2  2004/10/19 15:11:25  schweks
// Removing unused variables and code.
//

package com.eaton.electrical.smrc;

import java.io.*;
import java.sql.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;
import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.exception.*;

/**
 * 
 * @author Jason Lubbert
 */
public class TargetMarketReportResults extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = null;
        Connection DBConn = null;
        String sFwdUrl = "/SMRCErrorPage.jsp";

        boolean redirect = false;
        
        try {
            DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();

            user = SMRCSession.getUser(request, DBConn);

            SMRCHeaderBean hdr = new SMRCHeaderBean();
            hdr.setUser(user);
            request.setAttribute("header", hdr);
            writeResultsPage(request, DBConn);
            sFwdUrl = "/targetMarketReportResults.jsp";

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
    
    private void writeResultsPage(HttpServletRequest request, Connection DBConn) throws Exception {
        String zoneFilter = "";
        String districtFilter = "";
        String divisionFilter = "";
        String conversionFilter = "";
        String segmentFilter = "";
        Calendar startCal = null;
        Calendar endCal = null;
        
        StringBuffer href = new StringBuffer();
        
        String groupBy = "";
        boolean multiLevel = false;
        StringBuffer filterString = new StringBuffer();
        
        try {
            
            if (request.getParameter("tmGroupBy") != null){
                if (request.getParameter("tmGroupBy").equalsIgnoreCase("tmGroupByDivision")){
                    groupBy = "Division";
                    String newQueryString = changeQueryString(request.getQueryString(), "tmDivisionFilter");
                    newQueryString = changeQueryString(newQueryString, "tmGroupBy");
                    href.append("TargetMarketReportResults?tmGroupBy=tmGroupByAccount&" + newQueryString + "&tmDivisionFilter=");
                } else if (request.getParameter("tmGroupBy").equalsIgnoreCase("tmGroupByDistrict")){
                    groupBy = "District";
                    String newQueryString = changeQueryString(request.getQueryString(), "tmDistrictFilter");
                    newQueryString = changeQueryString(newQueryString, "tmGroupBy");
                    href.append("TargetMarketReportResults?tmGroupBy=tmGroupByAccount&" + newQueryString + "&tmDistrictFilter=");
                } else if (request.getParameter("tmGroupBy").equalsIgnoreCase("tmGroupByAccount")){
                    groupBy = "Account";
                    href.append("TargetMarketSetup?acctId=");
                    multiLevel = true;
                } else if (request.getParameter("tmGroupBy").equalsIgnoreCase("tmGroupByTMP")){
                    groupBy = "Plan";
                    href.append("TargetMarketSetup?tmId=");
                }
            }
            
            if (request.getParameter("tmZoneFilter") != null) {
                if (request.getParameterValues("tmZoneFilter").length > 0
                        && !request.getParameterValues("tmZoneFilter")[0].equals("0")) {
                    zoneFilter = request.getParameterValues("tmZoneFilter")[0];
                    filterString.append("<br>Zone: " + DistrictDAO.getDistrictName(zoneFilter, DBConn));
                }
            }
            
            if (request.getParameter("tmDivisionFilter") != null) {
                if (request.getParameterValues("tmDivisionFilter").length > 0
                        && !request.getParameterValues("tmDivisionFilter")[0].equals("0")) {
                    divisionFilter = request.getParameterValues("tmDivisionFilter")[0];
                    Division filterDivision = DivisionDAO.getDivision(divisionFilter, DBConn);
                    filterString.append("<br>Division: " + filterDivision.getName());
                }
            }
            
            if (request.getParameter("tmDistrictFilter") != null) {
                if (request.getParameterValues("tmDistrictFilter").length > 0
                        && !request.getParameterValues("tmDistrictFilter")[0].equals("0")) {
                    districtFilter = request.getParameterValues("tmDistrictFilter")[0];
                    filterString.append("<br>District: " + DistrictDAO.getDistrictName(districtFilter, DBConn));
                }
            }
            
            if (request.getParameter("tmConversionFilter") != null) {
                if (request.getParameterValues("tmConversionFilter").length > 0
                        && !request.getParameterValues("tmConversionFilter")[0].equals("0")) {
                    conversionFilter = request.getParameterValues("tmConversionFilter")[0];
                    Vendor vendor = MiscDAO.getVendorInfo(Globals.a2int(conversionFilter), DBConn);
                    filterString.append("<br>Conversion: " + vendor.getDescription());
               }
            }

            if (request.getParameter("tmSegmentFilter") != null) {
                if (request.getParameterValues("tmSegmentFilter").length > 0
                        && !request.getParameterValues("tmSegmentFilter")[0].equals("0")) {
                    segmentFilter = request.getParameterValues("tmSegmentFilter")[0];
                    Segment segment = SegmentsDAO.getSegment((new Integer(segmentFilter)).intValue(), DBConn);
                    filterString.append("<br>Market Segment: " + segment.getName());
                }
            }

            if (request.getParameter("targetMarketBeginDate") != null){
            	String beginDateString = request.getParameter("targetMarketBeginDate");
            	int slashLoc = beginDateString.indexOf("/");
            	String beginMonth = beginDateString.substring(0,slashLoc);
            	String beginYear = beginDateString.substring(slashLoc + 1);
            	startCal = Calendar.getInstance();
            	startCal.set(Calendar.MONTH,(Globals.a2int(beginMonth) - 1));
            	startCal.set(Calendar.YEAR,Globals.a2int(beginYear));
            	SMRCLogger.debug("beginMonth: " + beginMonth + " beginYear: " + beginYear + " startCal: " + startCal.get(Calendar.MONTH) + "-" + startCal.get(Calendar.YEAR));
            }
            
            if (request.getParameter("targetMarketEndDate") != null){
            	String endDateString = request.getParameter("targetMarketEndDate");
            	int slashLoc = endDateString.indexOf("/");
            	String endMonth = endDateString.substring(0,slashLoc);
            	String endYear = endDateString.substring(slashLoc + 1);
            	endCal = Calendar.getInstance();
            	endCal.set(Calendar.MONTH,(Globals.a2int(endMonth) - 1));
            	endCal.set(Calendar.YEAR,Globals.a2int(endYear));
            	SMRCLogger.debug("endMonth: " + endMonth + " endYear: " + endYear + " endCal: " + endCal.get(Calendar.MONTH) + "-" + endCal.get(Calendar.YEAR));
            }
            
            filterString.append("<br>Active between " + (startCal.get(Calendar.MONTH) + 1) + "/" + startCal.get(Calendar.YEAR));
            filterString.append(" and " + (endCal.get(Calendar.MONTH) + 1) + "/" + endCal.get(Calendar.YEAR));
            
            // This section finds all active target market plans and determines what type
            // of dollars to use for sales
            // ArrayList chargeIds = new ArrayList();
            ArrayList tapDollarIds = new ArrayList();
            ArrayList endMarketIds = new ArrayList();
            
            HashMap activeIdMap = new HashMap();
            activeIdMap = TargetMarketDAO.getActiveTMsAndSalesTrackingId(conversionFilter, segmentFilter, startCal, endCal, DBConn);
            SMRCLogger.debug("activeIdMap = "  + activeIdMap.size());
            Set activeIdSet = activeIdMap.keySet();
            Iterator iterator = activeIdSet.iterator();
            while (iterator.hasNext()){
                Integer tmId = (Integer) iterator.next();
                Integer salesTrackingTypeId = (Integer) activeIdMap.get(tmId);
               // charge ids no longer used...
               // if (salesTrackingTypeId.intValue() == 61){
               //     chargeIds.add(tmId);
               // } else 
                if (salesTrackingTypeId.intValue() == 62){
                	tapDollarIds.add(tmId);
                } else if (salesTrackingTypeId.intValue() == 63){
                    endMarketIds.add(tmId);
                }
            }
            SMRCLogger.debug(" tapDollarIds = " + tapDollarIds.size() + " endMarketIds = " + endMarketIds.size());
           // Find all necessary totals for all active target market plans using correct dollar types
            ArrayList tmBeans = new ArrayList();
            tmBeans = TargetMarketDAO.getTMTotals(tapDollarIds, endMarketIds, startCal, endCal, DBConn);
            SMRCLogger.debug("tmBeans:" + tmBeans.size());
            //for (int i=0; i < tmBeans.size(); i++){
            //    TMBean tmBean = (TMBean) tmBeans.get(i);
            //}
            
            // Find/Calculate all necessary totals for all grouping types in all active target market plans
            // using correct dollar type
            ArrayList tmGroupByBeans = new ArrayList();
            tmGroupByBeans = TargetMarketDAO.getTMGroupByTotals(tapDollarIds, endMarketIds, groupBy, 
                    divisionFilter, zoneFilter, districtFilter, DBConn);
            TreeSet groupingIds = new TreeSet();
            for (int i=0; i < tmGroupByBeans.size(); i++){
                TMGroupByBean tmGroupByBean = (TMGroupByBean) tmGroupByBeans.get(i);
                groupingIds.add(tmGroupByBean.getGroupingId());
                for (int x=0; x < tmBeans.size(); x++){
                    TMBean tmBean = (TMBean) tmBeans.get(x);
                    if (tmBean.getTmid() == tmGroupByBean.getTmid()){
                        double baselinePercentage = (tmGroupByBean.getBaselineSales() / tmBean.getBaselineSales());
                        tmGroupByBean.setBaselinePercentage(baselinePercentage);
                        tmGroupByBean.setForecastMaxPayout(baselinePercentage * tmBean.getMaxPayout());
                        tmGroupByBean.setGrowthPayout(baselinePercentage * tmBean.getGrowthPayout());
                    }
                }
           }
            
            // Calculate totals by GroupBy
	    ArrayList reportBeans = new ArrayList();
            Iterator groupIterator = groupingIds.iterator();
            while (groupIterator.hasNext()){
                TreeSet subSet = new TreeSet();
                String thisGroup = (String) groupIterator.next();
                String subGroup = "";
                double totalBaselineSales = 0;
                double totalPlanSales = 0;
                double totalGrowthPayout = 0;
                double totalForecastPayout = 0;
                for (int i=0; i < tmGroupByBeans.size(); i++){
                    TMGroupByBean tmGroupByBean = (TMGroupByBean) tmGroupByBeans.get(i);
                    if (thisGroup.equalsIgnoreCase(tmGroupByBean.getGroupingId())){
                        totalBaselineSales += tmGroupByBean.getBaselineSales();
                        totalPlanSales += tmGroupByBean.getPlanSales();
                        totalGrowthPayout += tmGroupByBean.getGrowthPayout();
                        totalForecastPayout += tmGroupByBean.getForecastMaxPayout();
                        if (multiLevel){
                            subSet.add(tmGroupByBean.getSubGroupingId());
                        }
                        
                    }
                    
                }
                
                
                TargetMarketReportBean reportBean = new TargetMarketReportBean();
                reportBean.setGrowthPayout(totalGrowthPayout);
                reportBean.setSalesBaseline(totalBaselineSales);
                reportBean.setSalesInPlan(totalPlanSales);
                reportBean.setForecastMaxPayout(totalForecastPayout);
                reportBean.setId(thisGroup);
                if (groupBy.equalsIgnoreCase("division")){
                    Division division = DivisionDAO.getDivision(thisGroup, DBConn);
                    reportBean.setDescription(division.getName());
                } else if (groupBy.equalsIgnoreCase("district")){
                    reportBean.setDescription(DistrictDAO.getDistrictName(thisGroup, DBConn));
                } else if (groupBy.equalsIgnoreCase("plan")){
                	reportBean.setDescription(TargetMarketDAO.getTargetMarketPlanDescription(Globals.a2int(thisGroup), DBConn));
                } else {
                    reportBean.setDescription(AccountDAO.getAccountName(thisGroup, DBConn) + "(" + thisGroup + ")");
                }
                if (request.getParameter("sortBy") != null){
                    if (!request.getParameter("sortBy").equalsIgnoreCase("description")){
                        setSortField(request, reportBean);
                    }
                }
                
                reportBeans.add(reportBean);
                
                // For multiLevel reports, loop through again to calculate subtotals
                if (multiLevel){
                    Iterator subIterator = subSet.iterator();
                    while (subIterator.hasNext()){
                        double subtotalBaselineSales = 0;
                        double subtotalPlanSales = 0;
                        double subtotalGrowthPayout = 0;
                        double subtotalForecastPayout = 0;
                        subGroup = (String) subIterator.next();
                        for (int i=0; i < tmGroupByBeans.size(); i++){
                            TMGroupByBean tmGroupByBean = (TMGroupByBean) tmGroupByBeans.get(i);
                            if ((thisGroup.equalsIgnoreCase(tmGroupByBean.getGroupingId())) && 
                                 (subGroup.equalsIgnoreCase(tmGroupByBean.getSubGroupingId()))){
                                subtotalBaselineSales += tmGroupByBean.getBaselineSales();
                                subtotalPlanSales += tmGroupByBean.getPlanSales();
                                subtotalGrowthPayout += tmGroupByBean.getGrowthPayout();
                                subtotalForecastPayout += tmGroupByBean.getForecastMaxPayout();
                            }
                        }
                        TargetMarketReportBean subreportBean = new TargetMarketReportBean();
                        subreportBean.setGrowthPayout(subtotalGrowthPayout);
                        subreportBean.setSalesBaseline(subtotalBaselineSales);
                        subreportBean.setSalesInPlan(subtotalPlanSales);
                        subreportBean.setForecastMaxPayout(subtotalForecastPayout);
                        if (groupBy.equalsIgnoreCase("account")){
                            Division subdivision = DivisionDAO.getDivision(subGroup, DBConn);
                            subreportBean.setSubDescription(subdivision.getName());
                        } 
                        reportBeans.add(subreportBean);
                    }
                }
                
            }
            SMRCLogger.debug("reportBeans: " + reportBeans.size());
            if (filterString.length() == 0){
                filterString.append("No filters selected");
            }
            
            // Sorting results
            if (request.getParameter("sortBy") != null){
                // Already sorted by description at this point
                if (!request.getParameter("sortBy").equalsIgnoreCase("description")){
                    TargetMarketReportSorter sorter = new TargetMarketReportSorter();
                    Collections.sort(reportBeans, sorter);
                }
            }
            // Remove previous occurrance of sortBy 
            String sortQueryString = changeQueryString(request.getQueryString(), "sortBy");
            
            request.setAttribute("reportType", groupBy);       
            request.setAttribute("reportBeans", reportBeans);
            request.setAttribute("filterString", filterString.toString());
            request.setAttribute("sortQueryString", sortQueryString);
            request.setAttribute("href", href.toString());
                        
            
        }catch (Exception e)	{
                SMRCLogger.error("TargetMarketReportResults.writeResultsPage(): ", e);
                throw e;
        }
        
    }
    
    private String changeQueryString (String queryString, String removeParam){
        StringBuffer newString = new StringBuffer();
        int paramLoc = queryString.indexOf(removeParam);
        int nextParamLoc = -1;
        if (paramLoc > -1){
            nextParamLoc = queryString.indexOf("&", paramLoc);
        }
        if (paramLoc > -1){
            if (nextParamLoc > -1){
                newString.append(queryString.substring(0,paramLoc) + queryString.substring(nextParamLoc));
            } else {
                newString.append(queryString.substring(0,paramLoc));
            }
        } else {
            if (nextParamLoc > -1){
                newString.append(queryString.substring(nextParamLoc));
            } else {
                newString.append(queryString);
            }
        }
        return newString.toString();
        
        
        
    }
    
    private void setSortField (HttpServletRequest request, TargetMarketReportBean reportBean){
        if (request.getParameter("sortBy") != null){
            if (!request.getParameter("sortBy").equalsIgnoreCase("description")){
                if (request.getParameter("sortBy").equalsIgnoreCase("baselineSales")){
                    reportBean.setSortField(reportBean.getSalesBaseline());
                }
                if (request.getParameter("sortBy").equalsIgnoreCase("planSales")){
                    reportBean.setSortField(reportBean.getSalesInPlan());
                }
                if (request.getParameter("sortBy").equalsIgnoreCase("growthPercent")){
                    reportBean.setSortField(reportBean.getPercentageGrowth());
                }
                if (request.getParameter("sortBy").equalsIgnoreCase("growthPayout")){
                    reportBean.setSortField(reportBean.getGrowthPayout());
                }
/*                if (request.getParameter("sortBy").equalsIgnoreCase("bidman")){
                    reportBean.setSortField(reportBean.getBidManRebate());
                }
                if (request.getParameter("sortBy").equalsIgnoreCase("payout")){
                    reportBean.setSortField(reportBean.getCurrentPayout());
                }
*/
                if (request.getParameter("sortBy").equalsIgnoreCase("forecast")){
                    reportBean.setSortField(reportBean.getForecastMaxPayout());
                }
            }
        }
    }

}