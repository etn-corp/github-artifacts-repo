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

public class DistributorProductLoadingModuleHistory extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sFwdUrl = "/SMRCErrorPage.jsp";
        boolean redirect = false;
        Connection DBConn = null;
        User user = null;
        
        try {
            DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();

            HttpSession session = request.getSession(true);
            user = SMRCSession.getUser(request, DBConn);

            String acctId = request.getParameter("acctId");
			Account acct = null;
			
			if(acctId!=null){
			    acct = AccountDAO.getAccount(acctId, DBConn);
			}

            SMRCHeaderBean hdr = new SMRCHeaderBean();
            hdr.setUser(user);
            hdr.setAccount(acct);
            
            request.setAttribute("header", hdr);
	
        	Distributor dist = DistributorDAO.getDistributor(acctId, DBConn);
        	ModuleChangeRequest[] moduleChangeHistory = ProductModuleDAO.getModuleChangeRequestHistory(dist.getId(),DBConn);
        	boolean hasHistory = false;
        	Workflow[] workflows = null;
        	if (moduleChangeHistory != null && moduleChangeHistory.length > 0){
        		hasHistory = true;
        		workflows = new Workflow[moduleChangeHistory.length];
        	}
        	
        	
    		if (hasHistory){
    			String[] moduleStatusDescriptions = new String[moduleChangeHistory.length];
    			for (int i=0; i < moduleChangeHistory.length; i++){
    				moduleChangeHistory[i].addModuleProducts(ProductModuleDAO.getModuleChangeProductsForRequest(moduleChangeHistory[i].getId(),DBConn));
    				moduleChangeHistory[i].setModuleChangeReasonNotes(ProductModuleDAO.getModuleChangeReasonNotesForRequest(moduleChangeHistory[i].getId(),DBConn));
    				workflows[i] = WorkflowDAO.getWorkflow(moduleChangeHistory[i].getId()+"",Workflow.WORKFLOW_PRODUCT_MODULE,user,DBConn);
    				moduleStatusDescriptions[i] = ModuleChangeRequest.determineModuleRequestStatus(moduleChangeHistory[i].getModuleProducts(),DBConn);
    			}
    			request.setAttribute("moduleChangeHistory", moduleChangeHistory);
        		request.setAttribute("workflows", workflows);
        		request.setAttribute("moduleStatusDescriptions",moduleStatusDescriptions);
    		} 
        	
        	request.setAttribute("hasHistory", new Boolean(hasHistory));
        	sFwdUrl = "/distributorProductLoadingModuleHistory.jsp";

            

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
