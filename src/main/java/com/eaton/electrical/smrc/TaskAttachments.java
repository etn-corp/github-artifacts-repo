//Copyright (c) 2004 Eaton, Inc.
//


package com.eaton.electrical.smrc;

import java.io.*;
import java.sql.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import com.eaton.ee.manageAttachments.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.exception.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

/**
 * 
 * @author Jason Lubbert
 */
public class TaskAttachments extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sFwdUrl = "/SMRCErrorPage.jsp";
        boolean redirect = false;
        Connection DBConn = null;
        User user = null;
        
        try {
            DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();
            user = SMRCSession.getUser(request, DBConn);
            SMRCHeaderBean hdr = new SMRCHeaderBean();
            hdr.setUser(user);
            request.setAttribute("header", hdr);
            int taskRow = 0;
            ArrayList fileNames = new ArrayList();
            ArrayList hiddenNames = new ArrayList();
            
            if (request.getParameter("taskRow") != null){
            	taskRow = Globals.a2int(request.getParameter("taskRow"));
            } else {
            	// Store the attachment temporarily on a filesystem with a unique
            	// name of the user's userid and the time 
            	String tempFileName = Globals.getTaskAttachmentTempFileLocation();
            	java.util.Date rightNow = new java.util.Date();
            	tempFileName += "SMRCTaskAttach" + user.getUserid() + rightNow.getTime();
            	File tempFile = new File(tempFileName);
            	tempFile.deleteOnExit();
            	MultipartRequest multipartRequest = BlobAttachmentPerform.saveFileFromForm(tempFile,request,Globals.getTaskAttachmentFileSizeMax());
          
            	if (multipartRequest.getParameter("taskRow") != null){
            		taskRow = Globals.a2int((String) multipartRequest.getParameter("taskRow"));
            	}
            	if (multipartRequest.getParameter("availableSlots") != null){
            		int available = Globals.a2int((String) multipartRequest.getParameter("availableSlots"));
            		for (int i=0; i < available ; i++){
            			String paramName = "doc" + i;
            			if (multipartRequest.getParameter(paramName) != null){
            				String fileName = (String) multipartRequest.getParameter(paramName);
            				fileNames.add(fileName);
            				paramName += "_hidden";
            				hiddenNames.add((String) multipartRequest.getParameter(paramName));
            			}
            			if (available <= fileNames.size()){
                			fileNames.remove(0);
                			hiddenNames.remove(0);
                		}
            		}
            		if (available > 0){
            			if (multipartRequest.getParameter("uploader") != null){
            				fileNames.add((String) multipartRequest.getParameter("uploader"));
            				hiddenNames.add(tempFileName);
            			}
            		}
            	}
            	
            	
            }
            
            request.setAttribute("taskRow", new Integer(taskRow));
            request.setAttribute("fileNames", fileNames);
            request.setAttribute("hiddenNames", hiddenNames);
            sFwdUrl = "/taskAttachments.jsp";
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

