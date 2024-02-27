package com.eaton.electrical.smrc;

import java.io.IOException;
import java.sql.Connection;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.exception.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

public class ModifySegmentApproveReject extends SMRCBaseServlet {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = null;
		
		Connection DBConn = null;
		String sFwdUrl="/SMRCErrorPage.jsp";
		boolean redirect=false;
		
		String approvalFlag = request.getParameter("ACTION");
		String vcn = request.getParameter("VCN");
		
		try{
			DBConn = SMRCConnectionPoolUtils.getDatabaseConnection() ;
			
			user = SMRCSession.getUser(request,DBConn);
			String userName = user.getFullName();
			String email = user.getEmailAddress();
			
			ModifySegmentApprovalsBO msa = ModifySegmentsDAO.getApprovalRecordForVCN(vcn, DBConn);
			
			if(request.getParameter("WHO").equalsIgnoreCase("DM")) {
				ModifySegmentsDAO.updateDmApproval(vcn, approvalFlag, userName, msa.getId(), DBConn);
				if(approvalFlag.equalsIgnoreCase("R")) {
					ModifySegmentsDAO.rejectCategoryModify(vcn, msa.getId(), DBConn);
					ModifySegmentsDAO.updateChannelApprovalFromDmRejection(vcn, msa.getId(), DBConn);
				}
				sendMail(vcn,approvalFlag,msa.getRequesterEmail(),msa.getDmEMail(),"DM", userName, DBConn);
			} else if(request.getParameter("WHO").equalsIgnoreCase("ZM")) {
				ModifySegmentsDAO.updateZmApproval(vcn, approvalFlag, userName, msa.getId(), DBConn);
				//handle ZM if/when the time comes
				//if ZM comes all 3 update methods will need modified to pass the extra email addy
			} else if(request.getParameter("WHO").equalsIgnoreCase("CM")) {
				ModifySegmentsDAO.updateChannelApproval(vcn, approvalFlag, userName, msa.getId(), DBConn);
				if(approvalFlag.equalsIgnoreCase("Y")) {
					ModifySegmentsDAO.approveCategoryModify(vcn, msa.getId(), DBConn);
				} else if(approvalFlag.equalsIgnoreCase("R")) {
					ModifySegmentsDAO.rejectCategoryModify(vcn, msa.getId(), DBConn);
				}
				sendMail(vcn,approvalFlag,msa.getRequesterEmail(),msa.getDmEMail(),"CM",userName, DBConn);
			} else {
				SMRCLogger.error("ModifySegmentApproveReject: Not DM, ZM, or CHANNEL");
				System.out.println("Not DM, ZM, or CHANNEL");
				
	            request.setAttribute("exception", "Not DM, ZM, or CHANNEL");
	            sFwdUrl = "/SMRCErrorPage.jsp";
	            redirect = false;
			}
			
			redirect=true;
			sFwdUrl="AccountProfile?page=profile&acctId=" + vcn + "&saved=true";
		} catch(Exception e) {
            SMRCConnectionPoolUtils.rollbackTransaction(DBConn);
			SMRCLogger.error("ModifySegmentApproveReject: ", e);
			System.out.println(e);
			
            request.setAttribute("exception", e.getMessage());
            sFwdUrl = "/SMRCErrorPage.jsp";
            redirect = false;
	    } finally {
			SMRCConnectionPoolUtils.close(DBConn);
			//System.out.println("GOTO PAGE");
			gotoPage(sFwdUrl, request, response,redirect);
	    }
		
	}
	
	public static void sendRequesterEmail(String dmEmail, String requesterEmail, String vcn, String userName, Connection DBConn) throws Exception {
		try {
			sendMail(vcn, "none", requesterEmail, dmEmail, "", userName, DBConn);
       	} catch(Exception e) {
    		System.out.println(e);
    		SMRCLogger.error("ModifySegment.sendRequesterEmail(): ", e);
    		throw(e);
    	}
	}
	
	private static void sendMail(String vcn, String approvalFlag, String requesterEmail, String dmEmail, String approvalType, String userName, Connection DBConn) throws Exception {
		TAPMail tm = new TAPMail();
		String approval = "N";
		String text = null;
		
		if(approvalFlag.equalsIgnoreCase("Y")) {
			approval = "Approved the";
		} else if(approvalFlag.equalsIgnoreCase("R")) {
			approval = "Rejected the";	
		} else {
			approval = "Requested a";
		}
		
		text = userName + " has " + approval + " Customer Category Change Request for " + vcn;
		tm.addRecipient("ChannelTapReqEGGSS@eaton.com");
		//tm.addRecipient("bbhmusic@hotmail.com");
		tm.addRecipient(dmEmail);
		tm.setSenderInfo("Customer Category Change Request at SMRC", "category@eaton.com");
		tm.addCCRecipient(requesterEmail);
		tm.setSubject("Customer Category Change Request");
		tm.setText(text);
		tm.sendMessage();
	}

}
