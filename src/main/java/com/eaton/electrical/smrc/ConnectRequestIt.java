package com.eaton.electrical.smrc;

import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

public class ConnectRequestIt extends SMRCBaseServlet {
	
	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection DBConn = null;
		
		String sFwdUrl = "/SMRCErrorPage.jsp";
		boolean redirect = false;
		User user = null;
		
		try {
			DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();
			user = SMRCSession.getUser(request, DBConn);
			String userid = user.getUserid();
			SMRCHeaderBean hdr = new SMRCHeaderBean();
			hdr.setUser(user);
			if (userid == null) { // User does not have a portal session
				throw new Exception("Portal Session is Expired");
			}
	/*		
			ServletContext smrcContext = this.getServletContext();
						
			ServletContext requestItContext = smrcContext.getContext("/requestit");
			
			RequestDispatcher requestItDisp = requestItContext.getRequestDispatcher("/login.jsp");
			request.setAttribute("callingApp", "SMRC");
			requestItDisp.forward(request,response);
			System.out.println("after forward");
		//	response.sendRedirect("/requestit/login.do");
			
		*/			
			String requestItHref = Globals.getRequestItHref();
			request.setAttribute("requestItHref", requestItHref);
			sFwdUrl = "/connectRequestIt.jsp";
			request.setAttribute("header", hdr);
					
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
		} catch (com.eaton.electrical.smrc.exception.ProfileException pe) {
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
