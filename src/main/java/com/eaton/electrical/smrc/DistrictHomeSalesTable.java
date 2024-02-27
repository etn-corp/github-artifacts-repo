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
import com.eaton.electrical.smrc.threads.*;
import com.eaton.electrical.smrc.util.*;

public class DistrictHomeSalesTable extends SMRCBaseServlet {

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
            user = SMRCSession.getUser( request, false, DBConn );
            session = request.getSession();
            String district = (String) session.getAttribute("district");     
            String salesOrders = "sales";
            int srYear = Globals.a2int((String) session.getAttribute("srYear"));
            int srMonth = Globals.a2int((String) session.getAttribute("srMonth"));

            if (request.getParameter("salesorders") != null){
                salesOrders = request.getParameter("salesorders");
            }

            double monthlyForecast = 0;
            
            if (request.getParameter("submit") != null){
                if (request.getParameter("forecast") != null){
                    monthlyForecast = (new Double(StringManipulation.removeCurrency(request.getParameter("forecast")))).doubleValue();
                    DistrictDAO.updateMonthlyForecast(district, monthlyForecast, user, srYear, srMonth, DBConn);
                    // Commit here so it's picked up in next line
                    // TODO jpv removed cuz I dont think its necessary
                    //DBConn.commit();
                }
            }
            
            
            monthlyForecast = DistrictDAO.getDistrictMonthlyForecast(district, srYear, srMonth, DBConn);
            ArrayList reportBeans = getAllDistrictHomeBeans(district, salesOrders, srYear, srMonth, DBConn);
            request.setAttribute("reportBeans", reportBeans);
            request.setAttribute("monthlyForecast", new Double(monthlyForecast));
            request.setAttribute("user", user);
            request.setAttribute("salesOrders", salesOrders);
            sFwdUrl = "/districtHomeSalesTable.jsp";

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
    
    private ArrayList getAllDistrictHomeBeans(String district, String salesOrders, int srYear, int srMonth, Connection DBConn) throws Exception{
        ArrayList allBeans = new ArrayList();
        
        try {
            // District Wide
            
            //DistrictHomeRunnable r = new DistrictHomeRunnable(district, salesOrders, srYear, srMonth, DBConn);
            DistrictHomeThread districtWideThread = new DistrictHomeThread(district, salesOrders, srYear, srMonth, DBConn, "district");
            districtWideThread.start();
            
            
            DistrictHomeThread targetThread = new DistrictHomeThread(district, salesOrders, srYear, srMonth, DBConn, "target");
            targetThread.start();
            
            
            DistrictHomeThread divisionsThread = new DistrictHomeThread(district, salesOrders, srYear, srMonth, DBConn, "division");
            divisionsThread.start();
            
            DistrictHomeThread segmentsThread = new DistrictHomeThread(district, salesOrders, srYear, srMonth, DBConn, "segments");
            segmentsThread.start();
            
            
            districtWideThread.join();
            targetThread.join();
            divisionsThread.join();
            segmentsThread.join();
            
            ArrayList districtWideBeans = districtWideThread.getResults();
            ArrayList targetAccountBeans = targetThread.getResults();
            ArrayList divisionTargetBeans = divisionsThread.getResults();
            ArrayList segmentBeans = segmentsThread.getResults();
            
            allBeans.addAll(districtWideBeans);
            allBeans.addAll(targetAccountBeans);
            allBeans.addAll(divisionTargetBeans);
            allBeans.addAll(segmentBeans);
            
            /*
            ArrayList districtWideBeans = DistrictDAO.getYTDandMTDSalesForDistrict(district, salesOrders, srYear, srMonth, DBConn);
            allBeans.addAll(districtWideBeans);
            ArrayList targetAccountBeans = DistrictDAO.getYTDSalesForDistrictTarget(district, salesOrders, srYear, srMonth, DBConn);
            allBeans.addAll(targetAccountBeans);
            ArrayList divisionTargetBeans = DistrictDAO.getYTDSalesForDistrictDivisionTargets(district, salesOrders, srYear, srMonth, DBConn);
            allBeans.addAll(divisionTargetBeans);
            ArrayList segmentBeans = DistrictDAO.getYTDSalesForDistrictSegments(district, salesOrders, srYear, srMonth, DBConn);
            allBeans.addAll(segmentBeans);
            */
        }catch (Exception e){
            SMRCLogger.error("DistrictHomeSalesTable.getAllDistrictHomeBeans(): ", e);
            throw e;
        }
        
        
        
        return allBeans;
        
    }
    
    
} //class