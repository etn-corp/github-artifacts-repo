package com.eaton.electrical.smrc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.eaton.electrical.smrc.bo.Account;
import com.eaton.electrical.smrc.bo.SMRCHeaderBean;
import com.eaton.electrical.smrc.bo.Segment;
import com.eaton.electrical.smrc.bo.User;
import com.eaton.electrical.smrc.dao.AccountDAO;
import com.eaton.electrical.smrc.dao.DistrictDAO;
import com.eaton.electrical.smrc.dao.ModifySegmentsDAO;
import com.eaton.electrical.smrc.dao.SegmentsDAO;
import com.eaton.electrical.smrc.dao.UserDAO;
import com.eaton.electrical.smrc.service.SMRCConnectionPoolUtils;
import com.eaton.electrical.smrc.service.SMRCLogger;
import com.eaton.electrical.smrc.util.SMRCSession;

public class ModifySegment extends SMRCBaseServlet {

	/**
	 * Constructor of the object.
	 */
	public ModifySegment() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            
            String vcn = request.getParameter("acctId");
            
            if(ModifySegmentsDAO.isPendingApproval(vcn, DBConn)) {
            	request.setAttribute("vcn", vcn);
            	sFwdUrl = "/pendingSegmentChange.jsp";
            } else {
            	//YOU MUST INSERT THE APPROVAL RECORD BEFORE THE SEGMENT RECORD TO ATTACH THE CORRECT BINDING ID
            	String spGeog = AccountDAO.getAccountGeog(vcn, DBConn); //GET GEOG
				
				User dmU = UserDAO.getUserByVistalineId(DistrictDAO.getManagerForGeography(spGeog.substring(0,5), "DM", DBConn),DBConn);  //GET DM
				//User zmU = UserDAO.getUserByVistalineId(DistrictDAO.getManagerForGeography(spGeog.substring(0,5), "ZM", DBConn),DBConn);
				String dmEmail = dmU.getEmailAddress();
            	String dm = dmU.getFullName();
            	String zm = null;
            	String zmEmail = null;
            	
            	ModifySegmentsDAO.insertApprovalRecord(vcn, user.getFullName(), user.getEmailAddress(), dm, dmEmail, zm, zmEmail, DBConn);
            	
            	//INSERT SEGMENT RECORD
            	String[] segments = request.getParameterValues("SEGMENTS");
            
            	for(int i=0; i < segments.length; i++) {
            		int segment = new Integer(segments[i]).intValue();
            		ModifySegmentsDAO.insertSegments(vcn, segment, "N", "N", DBConn);
            	}
            	
            	try {
            		ModifySegmentApproveReject.sendRequesterEmail(dmEmail, user.getEmailAddress(), vcn, user.getFullName(), DBConn);
            	} catch(Exception e) {
            		System.out.println(e);
            		SMRCLogger.error("ModifySegment.sendRequesterEmail(): ", e);
            		request.setAttribute("exception", e.getMessage());
            		sFwdUrl = "/SMRCErrorPage.jsp";
            	}
            	request.setAttribute("fwdPage","ModifySegment");
            	sFwdUrl = "/closeWindow.jsp";
            }
        } catch(Exception e) {
        	System.out.println(e);
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
