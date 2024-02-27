//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.2  2004/10/18 20:10:33  schweks
// Added Eaton header comment.
// Reformatted source code.
//

package com.eaton.electrical.smrc;

import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.SMRCSession;
import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.bo.*;

/**
 * @author E0062722
 *  
 */
public abstract class SMRCBaseServlet extends HttpServlet {

    public void init() throws ServletException {
        SMRCLogger.info(getClass().getName() + ".init() ");
        super.init();
    } //method

    public void destroy() {
        SMRCLogger.info(getClass().getName() + ".destroy() ");
        super.destroy();
    } //method

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    } //method

    protected void gotoPage(String address, HttpServletRequest request,
            HttpServletResponse response, boolean redirect)
            throws ServletException, IOException {
        SMRCLogger.debug(getClass().getName() + ".gotoPage() is going to page ... " + address);

        if (redirect) {
            response.sendRedirect(address);
        } else {
            Connection DBConn = null;
            User user = null;
            try {
    	        DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();
    	        user = SMRCSession.getUser(request, false, DBConn );
    	        String logPage = address;
    	        if (logPage.charAt(0) == '/'){
    	            logPage = logPage.substring(1,logPage.length());
    	        }
    	        if (logPage.indexOf(".jsp") > 0){
    	            logPage = logPage.substring(0,logPage.indexOf(".jsp"));
    	        }
    	        
    	        MiscDAO.saveAPLog(user,logPage,DBConn);
    	        SMRCConnectionPoolUtils.commitTransaction(DBConn);
            } catch (Exception e){
                SMRCLogger.warn(this.getClass().getName() + ".gotoPage():USERID=" + user.getUserid() + 
    	    				"; MESSAGE=" + e.getMessage() + 
    	    				"\nUSER:" + user, e);
            } finally {
                SMRCConnectionPoolUtils.close(DBConn);
            }
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(address);
            dispatcher.forward(request, response);
        }
    } //method

}