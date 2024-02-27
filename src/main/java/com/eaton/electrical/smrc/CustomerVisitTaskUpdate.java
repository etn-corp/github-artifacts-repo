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
 * @author Jason Lubbert
 *
 */
public class CustomerVisitTaskUpdate extends SMRCBaseServlet {
	
	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException {

		 String sFwdUrl = "/SMRCErrorPage.jsp";
		 boolean redirect = false;
		 Connection DBConn = null;
		 User user = null;
		 HttpSession session;
		 
		 try {
		     DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();
		
		     user = SMRCSession.getUser(request, DBConn);
		     
		     String acctId = request.getParameter("acctId");
				Account acct = null;
				if(acctId!=null){
					acct = AccountDAO.getAccount(acctId, DBConn);
				}
				
		     SMRCHeaderBean hdr = new SMRCHeaderBean();
			 hdr.setAccount(acct);
		     hdr.setUser(user);
		     
		     request.setAttribute("header", hdr);
		     
		     String action = request.getParameter("action");
		     String visitId = request.getParameter("visitId");
		     boolean refreshVisits = false;
		     int taskId = 0;
		     PurchaseActionProgram task = null;
		     
		     if (request.getParameter("update") != null){
		     	long nextTaskId = AccountDAO.updatePAP(request,DBConn,user.getUserid());
		     	if (nextTaskId > 0){
		     		taskId = (int) nextTaskId;
		     		action = "update";
		     	}
		     	refreshVisits = true;
		     	
		     }
		     
		     if (request.getParameter("taskId") != null && (taskId == 0)){
		     	taskId = Globals.a2int(request.getParameter("taskId"));
		     }
		     
		     if (taskId > 0){
		     	task = AccountDAO.getOneTask(DBConn, taskId);
		     	request.setAttribute("tasks", task);
		     }
		     session = request.getSession(false);
		     int srYear = Globals.a2int((String) session.getAttribute("srYear"));
//		   If the year is zero, something went wrong... set to current year
		     if ( srYear == 0 ) {
		     	 Calendar cal = new GregorianCalendar();
		         // Get the components of the date
		         srYear = cal.get(Calendar.YEAR);
		     }
		     ArrayList products = ProductDAO.getProductsCurrentYear(srYear, DBConn);

		     request.setAttribute("usersTm", UserDAO.getAllUsersTreeMap(DBConn));
		     
		     ArrayList ebeCategories = MiscDAO.getEBECategories(DBConn);
		     
		     request.setAttribute("action", action);
		     request.setAttribute("visitId", visitId);
		     request.setAttribute("products", products);
		     request.setAttribute("ebeCategories", ebeCategories);
		     request.setAttribute("cust", acctId);
		     request.setAttribute("refreshVisits", new Boolean(refreshVisits));
		     
		     sFwdUrl = "/customerVisitTaskUpdate.jsp";
		     
		     
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
}
