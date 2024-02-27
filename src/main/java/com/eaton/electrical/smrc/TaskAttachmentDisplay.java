//Copyright (c) 2004 Eaton, Inc.
//


package com.eaton.electrical.smrc;

import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.exception.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;
import com.eaton.ee.manageAttachments.*;

/**
 * 
 * @author Jason Lubbert
 */
public class TaskAttachmentDisplay extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection DBConn = null;
        User user = null;
        try {
            DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();
            user = SMRCSession.getUser(request, DBConn);
            long docId = 0;
            if (request.getParameter("docId") != null){
            	docId = Globals.a2long(request.getParameter("docId"));
            }
            Connection tempConn = DBConn.getMetaData().getConnection();
            TaskAttachment attachment = new TaskAttachment();
            attachment = AccountDAO.getTaskAttachment(docId,DBConn);
            if (attachment.getContentType() != null && !attachment.getContentType().equalsIgnoreCase("")){
            	response.setHeader("Content-Type", attachment.getContentType());
            } else {
            	response.setHeader("Content-Type", "text/html");
            }
    		response.setHeader("Content-Disposition", "attachment; filename=\""
    					+ attachment.getFileName() + "\";");
            
            
            BlobAttachmentPerform.getAttachmentWriteToResponse(tempConn,"sales_plan_pap_files",docId,
            		"sales_plan_pap_files_id","attached_file",response);
            
            
            
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

        } finally {
            SMRCConnectionPoolUtils.close(DBConn);
         
        }

    }
    
    
} //class

