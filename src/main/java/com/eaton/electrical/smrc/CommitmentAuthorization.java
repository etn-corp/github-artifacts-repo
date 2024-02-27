package com.eaton.electrical.smrc;
import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.exception.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

public class CommitmentAuthorization extends SMRCBaseServlet {


	/**
	 * Constructor of the object.
	 */
	public CommitmentAuthorization() {
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
		
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		
		Connection DBConn = null;
		Account acct = null;
		
		String auth = request.getParameter("AUTH");
		
		String action = "";
		
		int newCom = Integer.valueOf(request.getParameter("COMMITMENT_PROGRAM")).intValue();
		String distID = request.getParameter("acctid");
		String[] electricalLines = request.getParameterValues("ELECTRICAL_LINES");
		int pes1 = Integer.valueOf(request.getParameter("PROJECTED_EATON_SALES_1")).intValue();
		int pes2 = Integer.valueOf(request.getParameter("PROJECTED_EATON_SALES_2")).intValue();
		int psc1 = Integer.valueOf(request.getParameter("PROJECTED_SALES_VS_COMP_1")).intValue();
		int psc2 = Integer.valueOf(request.getParameter("PROJECTED_SALES_VS_COMP_2")).intValue();
		String cr = request.getParameter("COMMITMENT_REASON");
		
		String sFwdUrl = "/Commitments.jsp";
        boolean redirect = false;
        
		try {
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
			int oldComHolder = dist.getCommitmentProgram();
			
			TAPMail tm = new TAPMail();
            
	        if(auth.equalsIgnoreCase("NO")) {  
	        	try {
		            dist.setCommitmentProgram(newCom);           
		            dist.setCommitmentReason(cr);
		            dist.setProjectedEatonSalesYr1(pes1);
		            dist.setProjectedEatonSalesYr2(pes2);
		            dist.setProjectedVScompYr1(psc1);
		            dist.setProjectedVScompYr2(psc2);
			        dist.clearElectricalLines();
			        dist.setElectricalLines(electricalLines);
					DistributorDAO.saveDistributor(dist, 1, DBConn);
					
					if(oldComHolder != newCom) {
						CodeTypeDAO ctd = new CodeTypeDAO();
						String comName = ctd.getCodeTypeDescriptionByCodeTypeId(newCom, DBConn);
						
						String name = user.getFullName();
						if(name == null) {
							name = "";
						}
						
						action = "Level changed to " + comName;
						CommitmentDAO.insertApprovalHistory(distID, name, action, DBConn);
						
						String text = name+ " has modified the commitment level for Distributor" +distID +"  to " + comName+ ".";
						
						tm.setSenderInfo("Commmitments at SMRC", "commitments@eaton.com");
						//tm.addRecipient("michaeladefilippo@eaton.com");
						tm.addRecipient("ChannelTapReqEGGSS@eaton.com");
						tm.setSubject("Commitment level has been updated");
						tm.setText(text);
						tm.sendMessage();
					}
					
					String refresh = StringManipulation.noNull(request.getParameter("refresh"));
	                if (refresh.equals("true") && session.getAttribute("distributor") != null) {
	                    dist = (Distributor) session.getAttribute("distributor");
	                    session.removeAttribute("distributor");
	                } else if (distID != null) {
	                    dist = DistributorDAO.getDistributor(distID, DBConn);
	                    dist.setSegments(DistributorDAO.getDistributorSegments(dist, DBConn));
	                }
	                if (dist.getVcn().equals("")) {
	                    dist.setVcn(distID);
	                }	
	                
	                request.setAttribute("history", CommitmentDAO.getCommitmentHistoryByVCN(distID, DBConn));
	                request.setAttribute("approvalProcess", new Boolean(false));
					request.setAttribute("dist", dist);
					request.setAttribute("electricalLines", MiscDAO.getCodes("electrical_lines", DBConn));
					request.setAttribute("commitmentProgram", MiscDAO.getCodes("commitment_program", DBConn));
					request.setAttribute("save", "save");
				} catch(Exception e) {
					System.out.println("CAUGHT ERROR--DistributorDAO.updateCommitmentsProgram" + e);
			        SMRCLogger.error(this.getClass().getName() + ".doGet(): MESSAGE=" + e.getMessage(), e);
			        SMRCConnectionPoolUtils.rollbackTransaction(DBConn);					
				}
				
			} else if(auth.equalsIgnoreCase("YES")) {	//start approval process
				//BACK UP CURRENT DATA IF REJECTED YOU WILL REPUT THIS IN THE DISTRUBTOR TABLE
				//GET APPROVERS LIST AND PUT IN COMMITMENT APPROVAL TABLE
				
				ArrayList approversList = new ArrayList();
				
				approversList.add(user.getFullName());  //INITIAL REQUESTOR OF CHANGE
				
				String spGeog = AccountDAO.getAccountGeog(distID, DBConn); //GET GEOG
				
				User dm = UserDAO.getUserByVistalineId(DistrictDAO.getManagerForGeography(spGeog.substring(0,5), "DM", DBConn),DBConn);  //GET DM
				approversList.add(dm.getFullName());
				String dmEmail = dm.getEmailAddress();
				
				approversList.add("Channel");
				
				if(approversList.size() <= 0) {
					request.setAttribute("exception", "No approvers could be found.");
			        sFwdUrl = "/SMRCErrorPage.jsp";
			        redirect = false;
				} else {
					CommitmentChangeRequest ccr = new CommitmentChangeRequest(); //no need to continue to reset certain info in the loop
					
					String elBuffer = "";
					for(int i=0; i < dist.getElectricalLines().size();i++) {
						if(elBuffer.equals("")) {
							elBuffer = dist.getElectricalLines().get(i).toString();
						} else {
							elBuffer = elBuffer + "-"+ dist.getElectricalLines().get(i).toString();
						}
					}
					
					ccr.setElectricLines(elBuffer);
					ccr.setComLevel(oldComHolder);
					ccr.setPercSales1(dist.getProjectedVScompYr1());
					ccr.setPercSales3(dist.getProjectedVScompYr2());
					ccr.setProjSales1(dist.getProjectedEatonSalesYr1());
					ccr.setProjSales3(dist.getProjectedEatonSalesYr2());
					
					for(int i=0; i < approversList.size(); i++) {	
						ccr.setVcn(distID);
						ccr.setApprovalOrder(i+1);
						ccr.setApproverName(approversList.get(i).toString());
						if(i==0) {
							ccr.setApprovalFlag("Y");
						} else {
							ccr.setApprovalFlag("N");
						}
						CommitmentDAO.insertIntoCommitmentApproval(ccr, DBConn);
					}
				
					CodeTypeDAO ctd = new CodeTypeDAO();
					String comName = ctd.getCodeTypeDescriptionByCodeTypeId(newCom, DBConn);
					
					String name = user.getFullName();
					if(name == null) {
						name = "";
					}
					
					action = "Platinum request initiated";
					CommitmentDAO.insertApprovalHistory(distID, name, action, DBConn);
					
					String text = name+ " has modified the commitment level for Distributor" +distID +"  to " + comName+ ".";
					tm.setSenderInfo("Commitments at SMRC", "commitments@eaton.com");
					tm.addRecipient(dmEmail);
					tm.addCCRecipient(user.getEmailAddress());
					tm.addRecipient("ChannelTapReqEGGSS@eaton.com");
					tm.setSubject("Commitment level has been updated");
					tm.setText(text);
					tm.sendMessage();
					
		            dist.setCommitmentProgram(newCom);           
		            dist.setCommitmentReason(cr);
		            dist.setProjectedEatonSalesYr1(pes1);
		            dist.setProjectedEatonSalesYr2(pes2);
		            dist.setProjectedVScompYr1(psc1);
		            dist.setProjectedVScompYr2(psc2);
			        dist.clearElectricalLines();
			        dist.setElectricalLines(electricalLines);
					DistributorDAO.saveDistributor(dist, 1, DBConn);
					
					hasApprovalProcess(request,dist,DBConn);
					
					request.setAttribute("history", CommitmentDAO.getCommitmentHistoryByVCN(distID, DBConn));
					request.setAttribute("approvalProcess", new Boolean(true));
					request.setAttribute("dist", dist);
					request.setAttribute("electricalLines", MiscDAO.getCodes("electrical_lines", DBConn));
					request.setAttribute("commitmentProgram", MiscDAO.getCodes("commitment_program", DBConn));
					request.setAttribute("save", "save");
				}
			} else if(auth.equalsIgnoreCase("RESET")) {
				dist.setCommitmentProgram(Integer.valueOf(request.getParameter("COMMITMENT_PROGRAM")).intValue());
				dist.setVcn(distID);
				dist.setElectricalLines(request.getParameterValues("ELECTRICAL_LINES"));
				dist.setProjectedEatonSalesYr1(Integer.valueOf(request.getParameter("PROJECTED_EATON_SALES_1")).intValue());
				dist.setProjectedEatonSalesYr2(Integer.valueOf(request.getParameter("PROJECTED_EATON_SALES_2")).intValue());
				dist.setProjectedVScompYr1(Integer.valueOf(request.getParameter("PROJECTED_SALES_VS_COMP_1")).intValue());
				dist.setProjectedVScompYr2(Integer.valueOf(request.getParameter("PROJECTED_SALES_VS_COMP_2")).intValue());
				dist.setCommitmentReason(request.getParameter("COMMITMENT_REASON"));
				
				DistributorDAO.saveDistributor(dist, 1, DBConn);
				CommitmentDAO.CommitmentRejected(dist.getVcn(), DBConn);
				
				String name = user.getFullName();
				if(name == null) {
					name = "";
				}
				
				action = "Platinum request rejected";
				CommitmentDAO.insertApprovalHistory(distID, name, action, DBConn);
                
				request.setAttribute("history", CommitmentDAO.getCommitmentHistoryByVCN(distID, DBConn));
				request.setAttribute("approvalProcess", new Boolean(false));
				request.setAttribute("dist", dist);
				request.setAttribute("electricalLines", MiscDAO.getCodes("electrical_lines", DBConn));
				request.setAttribute("commitmentProgram", MiscDAO.getCodes("commitment_program", DBConn));
				request.setAttribute("save", "save");
			} else {
				request.setAttribute("exception", "Commitment Authorization Flag Could Not be Determined.");
		        sFwdUrl = "/SMRCErrorPage.jsp";
		        redirect = false;
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
	public static void hasApprovalProcess(HttpServletRequest request, Distributor dist, Connection DBConn) {
       	try {
       		CommitmentChangeRequest initiatorInformation = CommitmentDAO.getCommitmentChangeRequestByApprovalOrder(dist.getVcn(), 1, DBConn);
       		CommitmentChangeRequest dmInformation = CommitmentDAO.getCommitmentChangeRequestByApprovalOrder(dist.getVcn(), 2, DBConn);
       		CommitmentChangeRequest channelInformation = CommitmentDAO.getCommitmentChangeRequestByApprovalOrder(dist.getVcn(), 3, DBConn);
       		
       		if(initiatorInformation != null) {
       			if(initiatorInformation.getApprovalFlag()!=null){
       				request.setAttribute("initiator", initiatorInformation);
       			}
       		}
	    	if(dmInformation != null) {
	    		if(dmInformation.getApprovalFlag()!=null){
	    			request.setAttribute("dm", dmInformation);
	    		}
	    	}
	    	
	    	if(channelInformation != null) {	
	    		if(channelInformation.getApprovalFlag()!=null){
	    			request.setAttribute("channel", channelInformation);
	    		}
	    	}
      	} catch (Exception e) {
	        request.setAttribute("exception", e.getMessage());    		
       	}
	}
}
