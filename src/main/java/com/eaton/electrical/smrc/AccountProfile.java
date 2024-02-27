//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.76  2009/06/16 21:47:50  E0079188
// is account profile completed
//
// Revision 1.75  2007/11/19 15:27:47  lubbejd
// Send email when user decides new account is a duplicate of an existing account. Handle errors returned from VMS call more gracefully by displaying them to the user without throwing a "real" exception. Use VistaErrorMessageException instead to recognize what is a real exception and what exceptions are just VMS messages.
//
// Revision 1.74  2007/11/12 18:21:10  lubbejd
// Changes to make the user go back to the appropriate page after authorizing the account.
//
// Revision 1.73  2007/11/05 20:23:43  lubbejd
// RIT32957 - Beginning of duplicate customer checking changes.
//
// Revision 1.72  2007/06/07 17:14:08  lubbejd
// Moved retrieval of pending approvers to if statement for send to vista pages. This was causing errors for users attempting to create a new account.
//
// Revision 1.71  2007/05/18 19:02:58  c9962207
// Changed the user changed to empty string when a revise/resubmit is taking place.
//
// Revision 1.70  2007/04/24 16:49:10  c9962207
// Added revise/resubmit functionality.
//
// Revision 1.69  2007/04/03 18:15:11  lubbejd
// Changes for using the workflow tables for target market plans.
//
// Revision 1.68  2007/03/26 21:33:37  c9962207
// Removed '/SalesResourceChannel/' from the forward URL.
//
// Revision 1.67  2006/03/16 19:13:30  e0073445
// Removed code that was producing warnings.
//
// Revision 1.66  2006/01/05 20:04:40  e0073445
// Added Parent Name to the Parent Number on Account Profile page
//
// Revision 1.65  2005/05/03 19:24:35  lubbejd
// More changes for adding the pending approval page (CR30290).
//
// Revision 1.64  2005/05/03 17:39:18  lubbejd
// More changes for adding the pending approval page (CR30290). Also moved
// some methods from OEMAcctPlan to ProjectDAO related to retrieving target
// project information, and move sendNotifications() and sendRejectionNotifications()
// from AccountProfile to Workflow.
//
// Revision 1.63  2005/04/11 12:46:16  lubbejd
// More changes to add special programs to account profile. (CR28590)
//
// Revision 1.62  2005/04/08 19:45:54  lubbejd
// Beginning to make changes for CR28590 (Special Programs)
//
// Revision 1.61  2005/04/05 13:23:17  lubbejd
// Added check to see if the account is a distributor around code adding
// Global Channel mgrs, edi, security, pricing and training to the emails.
// (Copied from 1.60.2.1)
//
// Revision 1.60.2.1  2005/04/04 19:12:32  lubbejd
// Added check to see if the account is a distributor around code adding
// Global Channel mgrs, edi, security, pricing and training to the emails.
//
// Revision 1.60  2005/03/14 16:34:17  lubbejd
// Use new email_addresses table instead of the TAPMail.properties to retrieve
// some email addresses. Fixed problem on accountWorkflow.jsp related to
// sales id change.
//
// Revision 1.59  2005/03/01 18:52:48  lubbejd
// Added/Changed workflow emails per CR29400 and Bug# 847; Corrected
// link for rejected termination requests
//
// Revision 1.58  2005/01/31 19:26:40  vendejp
// Added focus type drop down to account profile page
//
// Revision 1.57  2005/01/19 20:55:00  vendejp
// added code for anchor tags on refresh
//
// Revision 1.56  2005/01/18 18:21:03  vendejp
// when an account does not exist for an acctId passed in, it now logs it as a WARN rather than ERROR
//
// Revision 1.55  2005/01/10 03:00:23  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.54  2005/01/09 05:59:54  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.53  2005/01/07 19:04:29  vendejp
// changed workflow emails to come from user logged in
//
// Revision 1.52  2005/01/05 22:40:24  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.51  2005/01/04 22:39:19  vendejp
// added url in email sent for workflow approval
//
// Revision 1.50  2004/12/23 18:12:50  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.49  2004/12/16 22:27:41  vendejp
// Changes so that the creator of an account can edit the account even if he/she does not have geog driven security
//
// Revision 1.48  2004/11/24 16:40:09  vendejp
// *** empty log message ***
//
// Revision 1.47  2004/11/05 17:30:23  vendejp
// Code for "Reject" in workflow
//
// Revision 1.46  2004/11/04 17:08:24  vendejp
// *** empty log message ***
//
// Revision 1.45  2004/11/03 19:17:12  vendejp
// *** empty log message ***
//
// Revision 1.44  2004/11/02 14:06:04  vendejp
// *** empty log message ***
//
// Revision 1.43  2004/10/30 22:52:42  vendejp
// Changes to accomodate header requirements.  Made User and Account objects set in the header object where I could.
//
// Revision 1.42  2004/10/28 14:58:17  vendejp
// Changed/added prequalification check for sending to vista
//
// Revision 1.41  2004/10/27 15:59:53  vendejp
// *** empty log message ***
//
// Revision 1.40  2004/10/26 12:59:42  vendejp
// Added sending email to workflow
//
// Revision 1.39  2004/10/22 17:00:55  vendejp
// added saved msg if saved
//
// Revision 1.38  2004/10/19 21:48:21  vendejp
// *** empty log message ***
//
// Revision 1.37  2004/10/19 20:38:26  schweks
// Got a little crazy changing variable names and had to rename request.setAttribute("user" to "usr"
//
// Revision 1.36  2004/10/19 14:25:05  schweks
// Removing unused variables.
//
// Revision 1.35  2004/10/16 20:00:44  vendejp
// modifications for country javascript and processing
//
// Revision 1.34  2004/10/16 18:14:59  manupvj
// Added abstract servlet for common functions and common point of logging.
//
// Revision 1.33  2004/10/16 17:34:20  vendejp
// *** empty log message ***
//
// Revision 1.32  2004/10/15 04:14:49  vendejp
// *** empty log message ***
//
// Revision 1.31  2004/10/14 18:41:37  vendejp
// *** empty log message ***
//
// Revision 1.30  2004/10/14 17:40:51  schweks
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
import com.eaton.electrical.smrc.exception.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

public class AccountProfile extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sFwdUrl="/SMRCErrorPage.jsp";
		boolean redirect=false;
		Connection DBConn = null;
		User user = null;
		
		try{
			DBConn = SMRCConnectionPoolUtils.getDatabaseConnection() ;
			HttpSession session = request.getSession(true) ;
			
			user = SMRCSession.getUser(request,DBConn);
			String userid=user.getUserid();

			String page = request.getParameter("page");	
			String acctId = request.getParameter("acctId");//acctId is the same as the VCN	
			//Account acct = null;
			Account acct = new Account();
			if(acctId!=null){
				acct = AccountDAO.getAccount(acctId, DBConn);
			}
			

			if(ModifySegmentsDAO.isChannel(acctId, DBConn)) {
				request.setAttribute("displayDistApplication", "Y");
			}
			
			
			if(request.getParameter("isModify") != null) {
				request.setAttribute("isModify", request.getParameter("isModify"));
			} else {
				request.setAttribute("isModify", "N");
			}
			
			SMRCHeaderBean hdr = new SMRCHeaderBean();
			hdr.setUser(user);

			if(page == null){
				page = "profile";
			}
			
			if(ModifySegmentsDAO.isPendingApproval(acctId, DBConn)) {
				ModifySegmentApprovalsBO approvalRecord = new ModifySegmentApprovalsBO(); 
				approvalRecord = ModifySegmentsDAO.getApprovalRecordForVCN(acctId, DBConn);
				ArrayList modifiedSegments = ModifySegmentsDAO.getModifiedSegmentsForVCN(acctId, DBConn);
				ArrayList modifiedSegmentsName = new ArrayList();
				
				for(int i=0; i < modifiedSegments.size(); i++) {
					modifiedSegmentsName.add(SegmentsDAO.getSegmentName((Integer.parseInt(modifiedSegments.get(i).toString())), DBConn));
				}
				
				request.setAttribute("APPROVALRECORD", approvalRecord);
				request.setAttribute("MODIFIEDSEGMENTS", modifiedSegmentsName);
			}
			
			if(ModifySegmentsDAO.hasApprovalHistory(acctId, DBConn)) {
				ArrayList approvalHistory = ModifySegmentsDAO.getApprovalHistory(acctId, DBConn);
				request.setAttribute("APPROVALHISTORY", approvalHistory);
			}
 			request.setAttribute("HASAPPROVALHISTORY", new Boolean(ModifySegmentsDAO.hasApprovalHistory(acctId, DBConn)));
			
			if(page.equalsIgnoreCase("saveProfile")){
				
				acct.setCustomerName(StringManipulation.noNull(request.getParameter("CUSTOMER_NAME"))) ;
				acct.setVcn(StringManipulation.noNull(request.getParameter("VISTA_CUSTOMER_NUMBER")));
				acct.setStatus(StringManipulation.noNull(request.getParameter("SP_STAGE_ID")));
				acct.setParentCustNumber(StringManipulation.noNull(request.getParameter("PARENT_NUM")));
						
				acct.setSalesEngineer1(StringManipulation.noNull(request.getParameter("SALES_ENGINEER1")));
				acct.setSalesEngineer2(StringManipulation.noNull(request.getParameter("SALES_ENGINEER2")));
				acct.setSalesEngineer3(StringManipulation.noNull(request.getParameter("SALES_ENGINEER3")));
				acct.setSalesEngineer4(StringManipulation.noNull(request.getParameter("SALES_ENGINEER4")));
				
				acct.setAPCont(StringManipulation.noNull(request.getParameter("APCONTACT")));
				acct.setAPContPhoneNumber(StringManipulation.noNull(request.getParameter("APCONTACT_PHONE_NUMBER")));
				acct.setAPContEmailAddress(StringManipulation.noNull(request.getParameter("APCONTACT_EMAIL_ADDRESS")));
				
				//acct.setDistrict(StringManipulation.noNull(request.getParameter("DISTRICT")));
				//call new servlet to talk to sales reporting and bring back District --make sure to check for other setters
				//System.out.println("ZIP----SAVE--PROFILE--" + request.getParameter("ZIP"));
				if(!request.getParameter("ZIP").equals("")) {
					acct.setDistrict(StringManipulation.noNull(SMRCDistrictByZipService.returnDistrictFromVista(request.getParameter("ZIP"), user.getVistaId())));
				}
				
				Address addr = new Address();
				addr.setAddress1(StringManipulation.noNull(request.getParameter("ADDRESS_LINE1")));
				addr.setAddress2(StringManipulation.noNull(request.getParameter("ADDRESS_LINE2")));
				addr.setAddress3(StringManipulation.noNull(request.getParameter("ADDRESS_LINE3")));
				addr.setAddress4(StringManipulation.noNull(request.getParameter("ADDRESS_LINE4")));
				addr.setCity(StringManipulation.noNull(request.getParameter("CITY")));
				addr.setState(StringManipulation.noNull(request.getParameter("STATE")));
				addr.setZip(StringManipulation.noNull(request.getParameter("ZIP")));
				addr.setCountry(StringManipulation.noNull(request.getParameter("COUNTRY")));
				acct.setBusinessAddress(addr);
				
				Address shipAddress = new Address();
				shipAddress.setAddress1(StringManipulation.noNull(request.getParameter("SHIP_ADDRESS_LINE1")));
				shipAddress.setAddress2(StringManipulation.noNull(request.getParameter("SHIP_ADDRESS_LINE2")));
				shipAddress.setAddress3(StringManipulation.noNull(request.getParameter("SHIP_ADDRESS_LINE3")));
				shipAddress.setAddress4(StringManipulation.noNull(request.getParameter("SHIP_ADDRESS_LINE4")));
				shipAddress.setCity(StringManipulation.noNull(request.getParameter("SHIP_CITY")));
				shipAddress.setState(StringManipulation.noNull(request.getParameter("SHIP_STATE")));
				shipAddress.setZip(StringManipulation.noNull(request.getParameter("SHIP_ZIP")));
				shipAddress.setCountry(StringManipulation.noNull(request.getParameter("SHIP_COUNTRY")));
				acct.setShipAddress(shipAddress);
				
				Address billToAddress = new Address();
				billToAddress.setAddress1(StringManipulation.noNull(request.getParameter("BILLTO_ADDRESS_LINE1")));
				billToAddress.setAddress2(StringManipulation.noNull(request.getParameter("BILLTO_ADDRESS_LINE2")));
				billToAddress.setAddress3(StringManipulation.noNull(request.getParameter("BILLTO_ADDRESS_LINE3")));
				billToAddress.setAddress4(StringManipulation.noNull(request.getParameter("BILLTO_ADDRESS_LINE4")));
				billToAddress.setCity(StringManipulation.noNull(request.getParameter("BILLTO_CITY")));
				billToAddress.setState(StringManipulation.noNull(request.getParameter("BILLTO_STATE")));
				billToAddress.setZip(StringManipulation.noNull(request.getParameter("BILLTO_ZIP")));
				billToAddress.setCountry(StringManipulation.noNull(request.getParameter("BILLTO_COUNTRY")));
				acct.setBillToAddress(billToAddress);
				
				
				String [] segments = request.getParameterValues("SEGMENTS");
				acct.clearSegments();
				if(segments!=null){
					for(int i=0;i<segments.length;i++){
						acct.addSegment(SegmentsDAO.getSegment(Globals.a2int(segments[i]), DBConn));
					}
					
				}
				
				if(StringManipulation.noNull(request.getParameter("PHONE_NUMBER")).length()>10){
					acct.setIntlPhoneNumber(StringManipulation.noNull(request.getParameter("PHONE_NUMBER")));
					acct.setPhone("");
				}else{
					acct.setIntlPhoneNumber("");
					acct.setPhone(StringManipulation.noNull(request.getParameter("PHONE_NUMBER")));
				}
				
				if(StringManipulation.noNull(request.getParameter("FAX_NUMBER")).length()>10){
					acct.setIntlFaxNumber(StringManipulation.noNull(request.getParameter("FAX_NUMBER")));
					acct.setFax("");
				}else{
					acct.setIntlFaxNumber("");
					acct.setFax(StringManipulation.noNull(request.getParameter("FAX_NUMBER")));
				}
				
				acct.setWebsite(StringManipulation.noNull(request.getParameter("WEB_SITE")));
				
				acct.setDirect(StringManipulation.noNull(request.getParameter("DIRECT_FLAG")));
				acct.setParentOnly(StringManipulation.noNull(request.getParameter("PARENT_ONLY_FLAG")));
				acct.setSendConfirmation(StringManipulation.noNull(request.getParameter("SEND_CONFIRMATION")));
				acct.setExemptCertRequired(StringManipulation.noNull(request.getParameter("EXEMPT_CERT_REQUIRED")));
				

				acct.setDivisionTargetAccount(request.getParameterValues("TARGET_ACCOUNTS"),true);
				
				acct.setNumOfWhatever(StringManipulation.noNull(request.getParameter("NUM_OF_VALUE")));
				acct.setContacts(ContactsDAO.getContacts(acctId, DBConn));
				acct.setBackgroundInfo(StringManipulation.noNull(request.getParameter("BACKGROUND_INFORMATION")));
				
				acct.setDpcNum(StringManipulation.noNull(request.getParameter("DPC_NUM")));
				acct.setSynergyCode(StringManipulation.noNull(request.getParameter("SYNERGY_CODE")));
				acct.setStoreNumber(StringManipulation.noNull(request.getParameter("STORE_NUM")));
				acct.setGenesisNumber(StringManipulation.noNull(request.getParameter("GENESIS_NUMBER")));
				acct.setDistributorStatementContact(Globals.a2int(request.getParameter("DIST_STATEMENT")));
				acct.setApplicationCode(Globals.a2int(request.getParameter("APPLICATION_ID")));
				acct.setFocusType(Globals.a2int(request.getParameter("FOCUS_TYPE")));
				ArrayList specialProgramIds = new ArrayList();
				if (request.getParameter("SPECIAL_PROGRAMS") != null){
				    String[] spIds = request.getParameterValues("SPECIAL_PROGRAMS");
				    for (int i=0; i< spIds.length; i++){
				        if (!spIds[i].equals("0")){
				            specialProgramIds.add(spIds[i]);
				        }
					}
				}
				acct.setSpecialProgramIds(specialProgramIds);
				
				String accountId=AccountDAO.saveAccount(acct, userid, DBConn);
				redirect=true;
				sFwdUrl="AccountProfile?page=profile&acctId=" + accountId + "&saved=true";
				
	
			}else if(page.equalsIgnoreCase("refresh")){
	
				acct.setCustomerName(StringManipulation.noNull(request.getParameter("CUSTOMER_NAME"))) ;
				acct.setVcn(StringManipulation.noNull(request.getParameter("VISTA_CUSTOMER_NUMBER")));
	
				/*
				 * If an anchor is specified in session, pass the anchor in the redirect
				 */
				String anchor="";
				if(session.getAttribute("anchor")!=null){
					anchor="#" + (String)session.getAttribute("anchor");
					session.removeAttribute("anchor");
				}
				
				if(session.getAttribute("parentNum")!=null){
					acct.setParentCustNumber((String)session.getAttribute("parentNum"));
					session.removeAttribute("parentNum");
					acct.setParentName((String)session.getAttribute("parentName"));
					session.removeAttribute("parentName");
				}else{
					acct.setParentCustNumber(StringManipulation.noNull(request.getParameter("PARENT_NUM")));
					acct.setParentName(StringManipulation.noNull(request.getParameter("PARENT_NAME")));
				}
	
				//if(session.getAttribute("seGeog")!=null){
					//acct.setDistrict((String)session.getAttribute("seGeog"));
					//session.removeAttribute("seGeog");
				//}else{
					//acct.setDistrict(StringManipulation.noNull(request.getParameter("DISTRICT")));
				//}
				//System.out.println("ZIP----REFRESH--" + request.getParameter("ZIP"));
				if(!request.getParameter("ZIP").equals("")) {
					acct.setDistrict(StringManipulation.noNull(SMRCDistrictByZipService.returnDistrictFromVista(request.getParameter("ZIP"), user.getVistaId())));
					//System.out.println("getting disr = " + acct.getDistrict());
				}
				
				if(session.getAttribute("se1")!=null){
					acct.setSalesEngineer1((String)session.getAttribute("se1"));
					session.removeAttribute("se1");
				}else{
					acct.setSalesEngineer1(StringManipulation.noNull(request.getParameter("SALES_ENGINEER1")));
				}
				if(session.getAttribute("se2")!=null){
					acct.setSalesEngineer2((String)session.getAttribute("se2"));
					session.removeAttribute("se2");
				}else{
					acct.setSalesEngineer2(StringManipulation.noNull(request.getParameter("SALES_ENGINEER2")));
				}
				if(session.getAttribute("se3")!=null){
					acct.setSalesEngineer3((String)session.getAttribute("se3"));
					session.removeAttribute("se3");
				}else{
					acct.setSalesEngineer3(StringManipulation.noNull(request.getParameter("SALES_ENGINEER3")));
				}
				if(session.getAttribute("se4")!=null){
					acct.setSalesEngineer4((String)session.getAttribute("se4"));
					session.removeAttribute("se4");
				}else{
					acct.setSalesEngineer4(StringManipulation.noNull(request.getParameter("SALES_ENGINEER4")));
				}
				
		
				
				Address addr = new Address();
				addr.setAddress1(StringManipulation.noNull(request.getParameter("ADDRESS_LINE1")));
				addr.setAddress2(StringManipulation.noNull(request.getParameter("ADDRESS_LINE2")));
				addr.setAddress3(StringManipulation.noNull(request.getParameter("ADDRESS_LINE3")));
				addr.setAddress4(StringManipulation.noNull(request.getParameter("ADDRESS_LINE4")));
				if(session.getAttribute("addr")!=null && session.getAttribute("addr").equals("biz")){
					if(session.getAttribute("city")!=null){
						addr.setCity((String)session.getAttribute("city"));
						session.removeAttribute("city");
					}else{
						addr.setCity(StringManipulation.noNull(request.getParameter("CITY")));
					}
					
					if(session.getAttribute("state")!=null){
						addr.setState((String)session.getAttribute("state"));
						session.removeAttribute("state");
					}else{
						addr.setState(StringManipulation.noNull(request.getParameter("STATE")));
					}
					if(session.getAttribute("zip")!=null){
						addr.setZip((String)session.getAttribute("zip"));
						session.removeAttribute("zip");
					}else{
						addr.setZip(StringManipulation.noNull(request.getParameter("ZIP")));
					}
					
				}else{
					addr.setCity(StringManipulation.noNull(request.getParameter("CITY")));
					addr.setState(StringManipulation.noNull(request.getParameter("STATE")));
					addr.setZip(StringManipulation.noNull(request.getParameter("ZIP")));
				}
				addr.setCountry(StringManipulation.noNull(request.getParameter("COUNTRY")));
				acct.setBusinessAddress(addr);
				
				Address shipAddress = new Address();
				shipAddress.setAddress1(StringManipulation.noNull(request.getParameter("SHIP_ADDRESS_LINE1")));
				shipAddress.setAddress2(StringManipulation.noNull(request.getParameter("SHIP_ADDRESS_LINE2")));
				shipAddress.setAddress3(StringManipulation.noNull(request.getParameter("SHIP_ADDRESS_LINE3")));
				shipAddress.setAddress4(StringManipulation.noNull(request.getParameter("SHIP_ADDRESS_LINE4")));
				if(session.getAttribute("addr")!=null && session.getAttribute("addr").equals("ship")){
					if(session.getAttribute("city")!=null){
						shipAddress.setCity((String)session.getAttribute("city"));
						session.removeAttribute("city");
					}else{
						shipAddress.setCity(StringManipulation.noNull(request.getParameter("SHIP_CITY")));
					}
					
					if(session.getAttribute("state")!=null){
						shipAddress.setState((String)session.getAttribute("state"));
						session.removeAttribute("state");
					}else{
						shipAddress.setState(StringManipulation.noNull(request.getParameter("SHIP_STATE")));
					}
					if(session.getAttribute("zip")!=null){
						shipAddress.setZip((String)session.getAttribute("zip"));
						session.removeAttribute("zip");
					}else{
						shipAddress.setZip(StringManipulation.noNull(request.getParameter("SHIP_ZIP")));
					}
					
				}else{
					shipAddress.setCity(StringManipulation.noNull(request.getParameter("SHIP_CITY")));
					shipAddress.setState(StringManipulation.noNull(request.getParameter("SHIP_STATE")));
					shipAddress.setZip(StringManipulation.noNull(request.getParameter("SHIP_ZIP")));
				}
				shipAddress.setCountry(StringManipulation.noNull(request.getParameter("SHIP_COUNTRY")));
				acct.setShipAddress(shipAddress);
				
				Address billToAddress = new Address();
				billToAddress.setAddress1(StringManipulation.noNull(request.getParameter("BILLTO_ADDRESS_LINE1")));
				billToAddress.setAddress2(StringManipulation.noNull(request.getParameter("BILLTO_ADDRESS_LINE2")));
				billToAddress.setAddress3(StringManipulation.noNull(request.getParameter("BILLTO_ADDRESS_LINE3")));
				billToAddress.setAddress4(StringManipulation.noNull(request.getParameter("BILLTO_ADDRESS_LINE4")));
				if(session.getAttribute("addr")!=null && session.getAttribute("addr").equals("billto")){
					if(session.getAttribute("city")!=null){
						billToAddress.setCity((String)session.getAttribute("city"));
						session.removeAttribute("city");
					}else{
						//billToAddress.setCity(StringManipulation.noNull(request.getParameter("BILLTO_CITY")));
					}
					
					if(session.getAttribute("state")!=null){
						billToAddress.setState((String)session.getAttribute("state"));
						session.removeAttribute("state");
					}else{
						billToAddress.setState(StringManipulation.noNull(request.getParameter("BILLTO_STATE")));
					}
					if(session.getAttribute("zip")!=null){
						billToAddress.setZip((String)session.getAttribute("zip"));
						session.removeAttribute("zip");
					}else{
						billToAddress.setZip(StringManipulation.noNull(request.getParameter("BILLTO_ZIP")));
					}
					
				}else{
					billToAddress.setCity(StringManipulation.noNull(request.getParameter("BILLTO_CITY")));
					billToAddress.setState(StringManipulation.noNull(request.getParameter("BILLTO_STATE")));
					billToAddress.setZip(StringManipulation.noNull(request.getParameter("BILLTO_ZIP")));
				}
				billToAddress.setCountry(StringManipulation.noNull(request.getParameter("BILLTO_COUNTRY")));
				acct.setBillToAddress(billToAddress);
				
				acct.clearSegments();
				if(session.getAttribute("segments")!=null){
					String [] segments = (String[])session.getAttribute("segments");
					session.removeAttribute("segments");
					if(segments!=null){
						for(int i=0;i<segments.length;i++){
							acct.addSegment(SegmentsDAO.getSegment(Globals.a2int(segments[i]), DBConn));
						}
					}
					
				}else{
					String [] segments = request.getParameterValues("SEGMENTS");
					if(segments!=null){
						for(int i=0;i<segments.length;i++){
							acct.addSegment(SegmentsDAO.getSegment(Globals.a2int(segments[i]), DBConn));
						}
						
					}
				}
				
				
				acct.setPhone(StringManipulation.noNull(request.getParameter("PHONE_NUMBER")));
				acct.setFax(StringManipulation.noNull(request.getParameter("FAX_NUMBER")));
				
				
				acct.setAPCont(StringManipulation.noNull(request.getParameter("APCONTACT")));
				acct.setAPContPhoneNumber(StringManipulation.noNull(request.getParameter("APCONTACT_PHONE_NUMBER")));
				acct.setAPContEmailAddress(StringManipulation.noNull(request.getParameter("APCONTACT_EMAIL_ADDRESS")));
				
				acct.setWebsite(StringManipulation.noNull(request.getParameter("WEB_SITE")));
				acct.setDivisionTargetAccount(request.getParameterValues("TARGET_ACCOUNTS"),true);
				
				acct.setDirect(StringManipulation.noNull(request.getParameter("DIRECT_FLAG")));
				acct.setParentOnly(StringManipulation.noNull(request.getParameter("PARENT_ONLY_FLAG")));
				acct.setSendConfirmation(StringManipulation.noNull(request.getParameter("SEND_CONFIRMATION")));
				acct.setExemptCertRequired(StringManipulation.noNull(request.getParameter("EXEMPT_CERT_REQUIRED")));
				
				acct.setNumOfWhatever(StringManipulation.noNull(request.getParameter("NUM_OF_VALUE")));
				acct.setContacts(ContactsDAO.getContacts(StringManipulation.noNull(request.getParameter("VISTA_CUSTOMER_NUMBER")), DBConn));
				acct.setBackgroundInfo(StringManipulation.noNull(request.getParameter("BACKGROUND_INFORMATION")));
				
				acct.setDpcNum(StringManipulation.noNull(request.getParameter("DPC_NUM")));
				acct.setSynergyCode(StringManipulation.noNull(request.getParameter("SYNERGY_CODE")));
				acct.setStoreNumber(StringManipulation.noNull(request.getParameter("STORE_NUM")));
				acct.setGenesisNumber(StringManipulation.noNull(request.getParameter("GENESIS_NUMBER")));
				acct.setApplicationCode(Globals.a2int(request.getParameter("APPLICATION_ID")));
				acct.setFocusType(Globals.a2int(request.getParameter("FOCUS_TYPE")));
				ArrayList specialProgramIds = new ArrayList();
				if (request.getParameter("SPECIAL_PROGRAMS") != null){
				    String[] spIds = request.getParameterValues("SPECIAL_PROGRAMS");
				    for (int i=0; i< spIds.length; i++){
				        if (!spIds[i].equals("0")){
						    specialProgramIds.add(spIds[i]);
				        }
					}
				}
				acct.setSpecialProgramIds(specialProgramIds);
				session.setAttribute("account",acct);//---------------------------------------------------------------------------------
				redirect=true;
				sFwdUrl="AccountProfile?page=profile&refresh=true&acctId=" + StringManipulation.noNull(request.getParameter("VISTA_CUSTOMER_NUMBER") + anchor+"&isModify="+request.getParameter("isModify"));
				
	
			}else if(page.equalsIgnoreCase("associatedUsers")){
				boolean deleteUser=false;
				if(StringManipulation.noNull(request.getParameter("update")).equals("add")){
					AccountDAO.updateAssociatedUser(userid,acctId,deleteUser, DBConn);
				}else if(StringManipulation.noNull(request.getParameter("update")).equals("remove")){
					deleteUser=true;
					AccountDAO.updateAssociatedUser(userid,acctId,deleteUser, DBConn);
				}

				sFwdUrl="/associatedUsers.jsp";
				ArrayList users = AccountDAO.getAssociatedUsers(acctId, DBConn);
				request.setAttribute("users",users);				
			

			} else if (page.equalsIgnoreCase("workflow")
                    || page.equalsIgnoreCase("workflowApprove")
					|| page.equalsIgnoreCase("workflowReject")
					|| page.equalsIgnoreCase("workflowSaveNotes")
					|| page.equalsIgnoreCase("workflowRevise")) {
				ArrayList priorApprovers = getPriorApprovers(acctId, DBConn);
				request.setAttribute("priorApprovers", priorApprovers);
				sFwdUrl = "/accountWorkflow.jsp";
                DistributorForms distForms = new DistributorForms(user,acct);
                String workflowType = "";
                if (request.getParameter("termination") != null) {
                    workflowType = "Distributor Termination";
                } else if (distForms.isDistributor() == true) {
                    workflowType = "Distributor Application";
                } else {
                    workflowType = "Customer";
                }

                String msg = "";
                if (page.equalsIgnoreCase("workflowApprove")) {
                	boolean possibleDuplicate = false;
                	boolean isDuplicate = false;
                	String duplicateVCN = null;
                	Workflow workflow = WorkflowDAO.getWorkflow(acct.getVcn(),workflowType,user,DBConn);
                	if (request.getParameter("fromDuplicate") != null){
                	// Coming from potential duplicate page
                		Enumeration enumeration = request.getParameterNames();
                		while (enumeration.hasMoreElements()){
                			String paramName = (String) enumeration.nextElement();
                			if (paramName.startsWith("customer_")){
                				String answer = request.getParameter(paramName);
                				if (answer.trim().equalsIgnoreCase("Y")){
                					isDuplicate = true;
                					duplicateVCN = paramName.substring(9);
                					SMRCLogger.debug("Is a duplicate of " + duplicateVCN);
                				}
                			}
                		}
                	} else {
                	// Coming from send to Vista page, have to check for duplicates
	                	if (workflow.isLastApproval() && 
	                			(workflowType.equals(Workflow.WORKFLOW_TYPE_CUSTOMER) || workflowType.equals(Workflow.WORKFLOW_TYPE_DISTRIBUTOR_APPLICATION))){
	                		// check for duplicates
	                		try {
		                    	DuplicateCustomerBean[] duplicates = SMRCAccountRegistrationService.getPotentialDuplicates(acct,user);
		                    	if (duplicates != null && duplicates.length > 0){
		                    		// Show users potential duplicates, if none are selected, continue
		                    		// with registration process, otherwise mark account as rejected
		                    		sFwdUrl = "/accountDuplicateCustomers.jsp";
		                    		possibleDuplicate = true;
		                    		request.setAttribute("duplicates", duplicates);
		                    		request.setAttribute("returnToPage","/AccountProfile?page=workflow&acctId=" + acct.getVcn());
		                    	}
	                		} catch (VistaErrorMessageException vme) {
	                			// Don't throw these errors out to the error page
	                			// Show them politely to the user
	                			SMRCLogger.debug("caught vme here...");
	                			possibleDuplicate = true;
	                			sFwdUrl="/accountWorkflow.jsp";
	                			msg = "This account cannot be submitted to Vista due to the following errors: <br> " + vme.getMessage();
	                		}
	                	}
                	}
                	
                	if (!possibleDuplicate){
                		/*
	                    WorkflowDAO.changeApprovalStatus(request.getParameter("approvalId"), userid, "Y", acct.getVcn(), DBConn);
	                    msg = "Workflow Approval Success";
	                    workflow = WorkflowDAO.getWorkflow(acct.getVcn(),workflowType,user,DBConn);
	                    if(!workflow.isComplete()){
	                        sendNotifications(workflow, acct, user, request, DBConn);
	                        workflow.sendNotifications(acct,user,DBConn);
	                    }
	                    */
	               //     if(workflow.isComplete()){
                		boolean updateWorkflow = true;
                		String approvalStatus = "Y";
	                    if (workflow.isLastApproval()){
	                    	if(!workflowType.equals("Distributor Termination")){
	                        	if (isDuplicate){
	                        		AccountDAO.changeAccountStatus(acctId,"Rejected",DBConn);
	                        		sFwdUrl = "/AccountProfile?page=workflow&acctId=" + acct.getVcn();
	                        		workflow.sendDuplicateSelectedNotification(acct,duplicateVCN,user,DBConn);
	                        	} else {
	                        		try {
		                        		AccountRegistrationResults registrationResults = SMRCAccountRegistrationService.registerAccount(acct, user, DBConn);
			                        	if(registrationResults.isRegistrationImmediate()){
			                        		acct = AccountDAO.getAccount(registrationResults.getRegisteredAccountVCN(), DBConn);	
			                        	}
			                        	request.setAttribute("registrationResults",registrationResults);
			                        	msg = "Workflow Approval Success";
			     	                    SMRCLogger.debug("done with registering");
			     	                    if (request.getParameter("returnToPage") != null){
			                        		sFwdUrl = request.getParameter("returnToPage");
			                        	}
			                        	SMRCLogger.debug("at this point, sFwdUrl = " + sFwdUrl);
			                        	if (sFwdUrl.indexOf("acctId=") > 0){
			                        		// replace acctId value with new account number
			                        		int acctIdIndex = (sFwdUrl.indexOf("acctId=") + 7);
			                        		int nextValue = sFwdUrl.indexOf("&",acctIdIndex);
			                        		StringBuffer buffer = new StringBuffer(sFwdUrl);
			                        		if (nextValue > 0){
			                        			buffer.replace(acctIdIndex,nextValue,acct.getVcn());
			                        		} else {
			                        			buffer.replace(acctIdIndex,(acctIdIndex + sFwdUrl.length()),acct.getVcn());
			                        		}
			                        		sFwdUrl = buffer.toString();
			                        	}
			                        	SMRCLogger.debug("now, sFwdUrl = " + sFwdUrl);
	                        		} catch (VistaErrorMessageException vme) {
	    	                			// Don't throw these errors out to the error page
	    	                			// Show them politely to the user
	    	                			SMRCLogger.debug("caught vme here...");
	    	                			sFwdUrl="/accountWorkflow.jsp";
	    	                			msg = "This account cannot be submitted to Vista due to the following errors: <br> " + vme.getMessage();
	    	                			updateWorkflow = false;
	    	                		}
	                        	}
                        		//              System.out.println("Sent to Vista!!!");
	                            
	                        	
	                    	}else{
	                    		msg = "Workflow Approval Success<br>This termination information has been sent to the Vista Group.";
	                    	}
	                    } else {
	                    	// Workflow not on last approval
	                    	msg = "Workflow Approval Success";
    	                    
	                    }
	                    if (updateWorkflow){
	                    	WorkflowDAO.changeApprovalStatus(request.getParameter("approvalId"), userid, approvalStatus, acct.getVcn(), DBConn);
	                    	msg = "Workflow Approval Success";
    	                    workflow = WorkflowDAO.getWorkflow(acct.getVcn(),workflowType,user,DBConn);
    	                    if (!isDuplicate){
    	                    	workflow.sendNotifications(acct,user,DBConn);
    	                    }
	                    }
                    }
                	SMRCLogger.debug("should go to " + sFwdUrl);
                }else if(page.equalsIgnoreCase("workflowReject")){
                    WorkflowDAO.changeApprovalStatus(request.getParameter("approvalId"), userid, "R", acct.getVcn(), DBConn);
                	msg = "Workflow Rejection Complete";
                	Workflow workflow = WorkflowDAO.getWorkflow(acct.getVcn(),workflowType,user,DBConn);
                	if(!workflow.getWorkflowType().equalsIgnoreCase("Distributor Termination")){
                	    AccountDAO.changeAccountStatus(acctId,"Rejected",DBConn);
            		}
                	workflow.sendRejectionNotification(acct,user,DBConn);
                	
                }else if(page.equalsIgnoreCase("workflowRevise")){
                	
                	//approvalId is the "workflow_approval_id"
                	//acctId is the vcn (vista customer number)
                	
                	String reviseResubmitExplantion = request.getParameter("reviseResubmitExplantion");
                	
                	//get the selected approver workflow approval Id from the selected radio button
                	String selectedPriorApproverWorkflowApprovalId = request.getParameter("selectedPriorApproverWorkflowApprovalId");

                	if(selectedPriorApproverWorkflowApprovalId != null){
        	        
        	        	String selectedPriorApproverUserId = WorkflowDAO.getWorkflowApprovalUserChanged(selectedPriorApproverWorkflowApprovalId, DBConn);
        	        	User selectedPriorApproverUser = UserDAO.getUser(selectedPriorApproverUserId, DBConn);
        	        	
        	        	//now update all the workflow_approvals with vista_customer_number (given) that has data changed later (aka greater) than 
        	        	//the date_changed for the given work_approval_id
        	        	WorkflowDAO.updateWorkflowLaterApproversToNotApproved(acctId, selectedPriorApproverWorkflowApprovalId, DBConn);
                		
                		//Change the status 
        	            //WorkflowDAO.changeApprovalStatus(approvalId, user.getUserid(), "V", acctId, DBConn);//user that submitted the resive resub.
                		WorkflowDAO.changeApprovalStatus(selectedPriorApproverWorkflowApprovalId, "", "V", acctId, DBConn);//user that submitted the resive resub.
        	        	Workflow workflow = WorkflowDAO.getWorkflow(acctId,workflowType,user,DBConn);//user that submitted the resive resub.
        	        	if(workflow.getWorkflowType().equalsIgnoreCase("Customer") || workflow.getWorkflowType().equalsIgnoreCase("Distributor Application")){
        	        	    AccountDAO.changeAccountStatus(acctId,"Pending Resubmission",DBConn);
        	    		}
        	        	
        	        	//user that submitted the resive resub.
        	        	ReviseResubExplanationDAO.insertReviseResubExplanationWithoutTargetProject(Integer.parseInt(selectedPriorApproverWorkflowApprovalId), reviseResubmitExplantion, user.getUserid(), DBConn);//user that submitted the resive resub.
        	        	
        	        	//user to whome the resive resub was submitted.
        	        	workflow.sendReviseAndResubmitNotification(acct, selectedPriorApproverUser, user, DBConn, reviseResubmitExplantion);
                	}
                	else{
                		throw new Exception("selectedPriorApproverUserId is null");
                	}
        			
                }else if(page.equalsIgnoreCase("workflowSaveNotes")){
                	acct.setSendToVistaNotes(StringManipulation.noNull(request.getParameter("NOTES")));
                 	AccountDAO.saveAccount(acct, userid, DBConn);
                	msg = "Comments saved";
                	
                }
                SMRCLogger.debug("After actions, acct is " + acct.getVcn());
                Workflow workflow = WorkflowDAO.getWorkflow(acct.getVcn(),workflowType,user,DBConn);
               
                //System.out.println("WF= " + workflow.getWorkflowSteps());
                //System.out.println("Complete " + workflow.isComplete());
                String preQual="";
                if(!workflow.isComplete() && !workflow.getWorkflowType().equalsIgnoreCase("Distributor Termination")){
        	        /* 
        	         * Pre-qualify for registration.
        	         * This will return an error string if the Prospect does not qualify.
        	         * It will NOT throw an exception
        	         */
                	preQual = SMRCAccountRegistrationService.isQualified(acct,user,DBConn,false);
                }
                String complete = "Y";
                if(distForms.isDistributor()) {
                	if(!workflow.getWorkflowType().equalsIgnoreCase("Distributor Termination")){
                		AccountApplicationCompleted aac = new AccountApplicationCompleted();
                		complete = aac.AccountCompleted(acct.getVcn());
                	}
                }
                request.setAttribute("complete", complete);
                request.setAttribute("preQual", preQual);
				request.setAttribute("workflow", workflow);
                request.setAttribute("msg", msg);

            //    sFwdUrl = "/accountWorkflow.jsp";	
			} else if(page.equalsIgnoreCase("Reinstate")){
				String vcn = (String)request.getParameter("acctId");
				//System.out.println(vcn);
				AccountDAO.changeAccountStatus(vcn, "Active", DBConn); //set account to active
				
				
				int approvalId = 0;
				
				ArrayList approversForID = WorkflowDAO.getWorkflowPriorApprovers(vcn, DBConn);
				
				for(int i=0; i < approversForID.size(); i++) {
					WorkflowPriorApprover wpa = (WorkflowPriorApprover)approversForID.get(i);
					approvalId = wpa.getWorkflowApprovalId();
					
					WorkflowDAO.updateApprovalFlag("Z", vcn, DBConn);
				}
				
				acct = AccountDAO.getAccount(vcn, DBConn);
				
				String text = "Customer number "+ vcn + " has been reinstated and is now currently an ACTIVE customer.";
				TAPMail tm = new TAPMail();
				tm.addRecipient("ChannelTapReqEGGSS@eaton.com");
				tm.setSubject("Customer Reinstated");
				tm.setText(text);
				
				tm.setSenderInfo("Target Account Planner", "Tap@eaton.com");
				tm.sendMessage();
				
				redirect=true;
				sFwdUrl="AccountProfile?acctId="+vcn;
				
				SMRCConnectionPoolUtils.commitTransaction(DBConn);
			} else if(page.equalsIgnoreCase("RemoveProspect")) {
				String vcn = (String)request.getParameter("acctId");
				AccountDAO.deleteProspect(vcn, DBConn);
				
				redirect=true;
				sFwdUrl="SMRCHome";
				
				SMRCConnectionPoolUtils.commitTransaction(DBConn);
			}else{ // page = profile
				String refresh=StringManipulation.noNull(request.getParameter("refresh"));
				//System.out.println("REFRESH == " + refresh);
				if(refresh.equals("true") && session.getAttribute("account")!=null){
					acct = (Account)session.getAttribute("account");
					session.removeAttribute("account");
					
				}	
				
				if(acctId!=null && acct.getVcn().equals("") && request.getParameter("refresh")==null){
					throw new SMRCException("This Account does not Exist.");
				}
	
				request.setAttribute("userGeography",user.getGeography());
				
				ArrayList divisions = DivisionDAO.getDivisions(DBConn);
				TreeMap countries = MiscDAO.getCountries(DBConn);
				request.setAttribute("divisions",divisions);
				request.setAttribute("countries",countries);
				
				request.setAttribute("se1Name",SalesDAO.getSalesmanName(acct.getSalesEngineer1(),DBConn));
				request.setAttribute("se2Name",SalesDAO.getSalesmanName(acct.getSalesEngineer2(),DBConn));
				request.setAttribute("se3Name",SalesDAO.getSalesmanName(acct.getSalesEngineer3(),DBConn));
				request.setAttribute("se4Name",SalesDAO.getSalesmanName(acct.getSalesEngineer4(),DBConn));
				
				request.setAttribute("geography",MiscDAO.getGeography(acct.getDistrict(),DBConn));
				
				request.setAttribute("focusTypes", MiscDAO.getFocusTypes(DBConn));
				request.setAttribute("specialPrograms", MiscDAO.getSpecialPrograms(DBConn));
				
				request.setAttribute("applicationCodes", MiscDAO.getCodes("account_applications", DBConn));
	
				if(page.equalsIgnoreCase("ModifySegment")) {
					if(ModifySegmentsDAO.isChannel(acctId, DBConn)) {
						request.setAttribute("displayDistApplication", "Y");
					}
				}
				sFwdUrl="/accountProfileDisplay.jsp";
			
			}
	
			hdr.setAccount(acct);
			request.setAttribute("header", hdr);
			
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
		} catch (SMRCException smrce) {
            SMRCLogger.warn(this.getClass().getName() + 
	            	".doGet(): USERID=" + user.getUserid() + 
					"; MESSAGE=" + smrce.getMessage() + 
					"\nUSER:" + user, smrce);
        	SMRCConnectionPoolUtils.rollbackTransaction(DBConn);
            request.setAttribute("exception", smrce.getMessage());
            sFwdUrl = "/SMRCErrorPage.jsp";
            redirect = false;
            
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
			//System.out.println("GOTO PAGE");
			gotoPage(sFwdUrl, request, response,redirect);
		}
	}

    public ArrayList getPriorApprovers (String vistaCustomerNumber, Connection DBConn) throws Exception{
    	 
    	if(vistaCustomerNumber != null){
    		return WorkflowDAO.getWorkflowPriorApprovers(vistaCustomerNumber, DBConn);
    	}
    	else{
    		throw new Exception("vistaCustomerNumber is null");
    	}
    }
	
} //class
