// Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.26  2006/03/16 19:13:31  e0073445
// Removed code that was producing warnings.
//
// Revision 1.25  2005/01/19 20:54:38  vendejp
// added code that sets anchor tag in session for refreshes
//
// Revision 1.24  2005/01/10 03:00:24  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.23  2005/01/09 05:59:54  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.22  2005/01/05 21:29:17  schweks
// Changed the exception handlers to include extra info in log message.
//
// Revision 1.21  2004/12/23 18:12:50  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.20  2004/11/09 21:33:03  vendejp
// *** empty log message ***
//
// Revision 1.19  2004/10/22 15:02:32  vendejp
// added changes to allow for 4th level (quadriary ?)
//
// Revision 1.18  2004/10/22 14:37:12  vendejp
// changed so DAO to get segments isnt called from jsp.  servlet gets arraylist of segments and
// specifies how many levels deep to go.
//
// Revision 1.17  2004/10/21 21:58:43  vendejp
// Changed so that 1st and 2nd levels are locked if the account has been sent to vista
//
// Revision 1.16  2004/10/19 14:59:11  schweks
// Removing unused variables and code.
//
// Revision 1.15  2004/10/18 20:10:33  schweks
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
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;
import com.eaton.electrical.smrc.exception.*;

public class SegmentSelect extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sFwdUrl = "/SMRCErrorPage.jsp";
        boolean redirect = false;
        Connection DBConn = null;
        User user = null;
        
        try {
            DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();

            HttpSession session = request.getSession(true);    
            user = SMRCSession.getUser(request, DBConn);
            SMRCHeaderBean hdr = new SMRCHeaderBean();
            hdr.setUser(user);
            
            request.setAttribute("header", hdr);
            request.setAttribute("fwdPage", request.getParameter("fwdPage"));
            request.setAttribute("isModify", request.getParameter("isModify"));
            request.setAttribute("acctId", request.getParameter("acctId"));
            
            boolean isModify = false;
            if(request.getParameter("isModify") != null) {
            	if(request.getParameter("isModify").equals("Y")) {
            		isModify = true;
            	}
            }
            String page = StringManipulation.noNull(request.getParameter("page"));

            if (page.equalsIgnoreCase("save")) {
                String[] segments = request.getParameterValues("SEGMENTS");

                session.removeAttribute("segment1");
                session.removeAttribute("segment2");
                session.setAttribute("segments", segments);
                session.setAttribute("anchor", "segments");
                sFwdUrl = "/closeWindow.jsp";
            } else {
            	
                if (request.getParameter("segment") != null) {
                    Segment seg = SegmentsDAO.getSegment(Globals.a2int(request.getParameter("segment")), DBConn);
                    if (seg.getLevel() == 1) {
                        session.setAttribute("segment1", seg);
                        session.removeAttribute("segment2");
                    } else if (seg.getLevel() == 2) {
                        session.setAttribute("segment2", seg);
                    }
                } else {
                    session.removeAttribute("segment1");
                    session.removeAttribute("segment2");
                    
                    String acctId = request.getParameter("acctId");
                    Account acct = AccountDAO.getAccount(acctId, DBConn);
                    ArrayList accountSegments = acct.getSegments();
                    
                    boolean isOneOfLockedSegments = false;
                    for(int i=0; i<accountSegments.size();i++){
                    	Segment segment = (Segment)accountSegments.get(i);
                    	if(segment.getName().equalsIgnoreCase("Channel") || segment.getName().equalsIgnoreCase("Contractor")){
                    		isOneOfLockedSegments=true;
                    		break;
                    	}
                    }
                    
                    if(isOneOfLockedSegments && (acct.isPending() || !acct.isProspect())){
                    if(isModify) {
                    	request.setAttribute("lock","false");
                    } else {
                    	request.setAttribute("lock","true");
                    }
                    	
	                    for(int i=0; i<accountSegments.size();i++){
	                    	Segment segment = (Segment)accountSegments.get(i);
	                        if (segment.getLevel() == 1) {
	                            session.setAttribute("segment1", segment);
	                            session.removeAttribute("segment2");
	                        } else if (segment.getLevel() == 2) {
	                            session.setAttribute("segment2", segment);
	                        }
	                    }
                    }

                }

                ArrayList segments = SegmentsDAO.getSegments(0,3,DBConn);
                request.setAttribute("segments", segments);
                request.setAttribute("isModify", request.getParameter("isModify"));
                
                sFwdUrl = "/segmentSelect.jsp?isModify="+request.getParameter("isModify");
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
