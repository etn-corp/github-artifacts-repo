package com.eaton.electrical.smrc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.exception.SMRCException;
import com.eaton.electrical.smrc.service.SMRCConnectionPoolUtils;
import com.eaton.electrical.smrc.service.SMRCLogger;
import com.eaton.electrical.smrc.util.SMRCSession;
import com.eaton.electrical.smrc.util.TAPMail;

public class CommitmentApproveReject extends SMRCBaseServlet {

	/**
	 * Constructor of the object.
	 */
	public CommitmentApproveReject() {
		super();
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		
		Connection DBConn = null;
		Account acct = null;
		
		String who = request.getParameter("who");
		String action = request.getParameter("action");
		String distID = request.getParameter("vcn");
		String historyAction = "";
		
		String sFwdUrl = "/Commitments.jsp";
        boolean redirect = false;
        
		try{
			DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();
			
			User user = SMRCSession.getUser(request, DBConn);
			String userID = user.getUserid();
			if(userID == null) {
				userID = "";
			}
			if(distID!=null){
			    DistributorDAO.initializeDistributor(distID, DBConn);
			    acct = AccountDAO.getAccount(distID, DBConn);
			}
	
	        SMRCHeaderBean hdr = new SMRCHeaderBean();
	        hdr.setUser(user);
	        hdr.setAccount(acct);
	        
	        request.setAttribute("header", hdr);
	        
			Distributor dist = DistributorDAO.getDistributor(distID, DBConn);
			TAPMail tm = new TAPMail();
			String spGeog = AccountDAO.getAccountGeog(distID, DBConn);
			User dm = UserDAO.getUserByVistalineId(DistrictDAO.getManagerForGeography(spGeog.substring(0,5), "DM", DBConn),DBConn);  //GET DM
			String dmEmail = dm.getEmailAddress();
			
			if(action.equalsIgnoreCase("APPROVE")) {
				ArrayList approverInfo = new ArrayList();
				int order = 1;
				if(CommitmentDAO.getCommitmentChangeRequestByApprovalOrder(distID, 1, DBConn).getApprovalFlag().equalsIgnoreCase("Y")) {
					if(CommitmentDAO.getCommitmentChangeRequestByApprovalOrder(distID, 2, DBConn).getApprovalFlag().equalsIgnoreCase("Y")) {
						order = 3;
					} else {
						order = 2;
					}
				} else {
					System.out.println("THROW ERROR HERE AS IT SHOULD NEVER HAPPEN");
				}
				
				CommitmentDAO.UpdateApprovalFlag(distID, order, user.getFullName(), DBConn);
				
				historyAction = "Platinum request approved";
				CommitmentDAO.insertApprovalHistory(distID, user.getFullName(), historyAction, DBConn);
				
				String text = "The Platinum commitment level request for Distributor" +distID +"  has been approved.";
				
				tm.setSenderInfo("Commitments at SMRC", "commitments@eaton.com");
				tm.addRecipient(user.getEmailAddress());
				tm.addRecipient(dmEmail);
				tm.addRecipient("ChannelTapReqEGGSS@eaton.com");
				if(order == 3){
					tm.setSubject("Commitment level request has been approved");
					text = "The Platinum commitment level request for Distributor" +distID +"  has been approved by "+ CommitmentDAO.getCommitmentChangeRequestByApprovalOrder(distID, 3, DBConn).getApproverName();
				} else {
					tm.setSubject("Commitment level request has been initiated");
					text = "The Platinum commitment level request for Distributor" +distID +"  has been approved by "+ CommitmentDAO.getCommitmentChangeRequestByApprovalOrder(distID, 2, DBConn).getApproverName();
				}
				tm.setText(text);
				tm.sendMessage();
				
				request.setAttribute("history", CommitmentDAO.getCommitmentHistoryByVCN(distID, DBConn));
	            request.setAttribute("approvalProcess", new Boolean(true));
				request.setAttribute("save", "save");
	            request.setAttribute("dist", dist);

	            request.setAttribute("commitmentProgram", MiscDAO.getCodes("commitment_program", DBConn));
	            request.setAttribute("electricalLines", MiscDAO.getCodes("electrical_lines", DBConn));
	            CommitmentAuthorization.hasApprovalProcess(request, dist, DBConn);
			} else if(action.equalsIgnoreCase("REJECT")) {
				CommitmentChangeRequest ccr = CommitmentDAO.getCommitmentChangeRequestByApprovalOrder(distID, 2, DBConn);
	
				dist.setCommitmentProgram(ccr.getComLevel());
				dist.setCommitmentReason(dist.getCommitmentReason());
				String[] electricalLines;
				if(ccr.getElectricLines() != null) {
					electricalLines = ccr.getElectricLines().split("-");
					dist.clearElectricalLines();
					dist.setElectricalLines(electricalLines);
				}
				dist.setProjectedEatonSalesYr1(ccr.getProjSales1());
				dist.setProjectedEatonSalesYr2(ccr.getProjSales3());
				dist.setProjectedVScompYr1(ccr.getPercSales1());
				dist.setProjectedVScompYr2(ccr.getPercSales3());
				
				DistributorDAO.saveDistributor(dist, 1, DBConn);
				
				String text = "The Platinum commitment level request for Distributor" +distID +"  has been rejected.";
				
				tm.setSenderInfo("Commitments at SMRC", "commitments@eaton.com");
				tm.addRecipient(user.getEmailAddress());
				tm.addRecipient(dmEmail);
				tm.addRecipient("ChannelTapReqEGGSS@eaton.com");
				tm.setSubject("Commitment level request has been rejected");
				tm.setText(text);
				tm.sendMessage();
				
				CommitmentDAO.CommitmentRejected(distID, DBConn);
				
				historyAction = "Platinum request rejected";
				CommitmentDAO.insertApprovalHistory(distID, user.getFullName(), historyAction, DBConn);
				
				request.setAttribute("history", CommitmentDAO.getCommitmentHistoryByVCN(distID, DBConn));
                request.setAttribute("approvalProcess", new Boolean(false));
				request.setAttribute("save", "save");
                request.setAttribute("dist", dist);

                request.setAttribute("commitmentProgram", MiscDAO.getCodes("commitment_program", DBConn));
                request.setAttribute("electricalLines", MiscDAO.getCodes("electrical_lines", DBConn));
                CommitmentAuthorization.hasApprovalProcess(request, dist, DBConn);
			} else {
				System.out.println("THERE IS NO OTHER ACTION AT THE MOMENT.");
			}
	    } catch (Exception e) {
	    	System.out.println("CAUGHT ERROR--General");
	        SMRCLogger.error(this.getClass().getName() + ".doGet(): MESSAGE=" + e.getMessage(), e);
	        SMRCConnectionPoolUtils.rollbackTransaction(DBConn);

	        request.setAttribute("exception", e.getMessage());
	        sFwdUrl = "/SMRCErrorPage.jsp";
	        redirect = false;
	     } finally {
	    	try {
	    		SMRCConnectionPoolUtils.commitTransaction(DBConn);
	    	} catch (SMRCException se) {
		        SMRCLogger.error(this.getClass().getName() + ".doGet(): MESSAGE=" + se.getMessage(), se);
		        SMRCConnectionPoolUtils.rollbackTransaction(DBConn);

		        request.setAttribute("exception", se.getMessage());
		        sFwdUrl = "/SMRCErrorPage.jsp";
		        redirect = false;
	    	}
    		SMRCConnectionPoolUtils.close(DBConn);
	        gotoPage(sFwdUrl, request, response, redirect);
	     }
 	}

}
