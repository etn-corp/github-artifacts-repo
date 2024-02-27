// Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.68  2009/06/15 18:21:37  E0079188
// comment out system messages
//
// Revision 1.67  2008/10/21 19:14:24  E0079188
// *** empty log message ***
//
// Revision 1.66  2008/10/07 19:08:46  E0079188
// complete merge into one distributor application
//
// Revision 1.65  2007/10/19 17:12:01  lubbejd
// Removed all product loading module page code out of DistributorSetup into DistributorProductLoadingModule. Changed link on left hand nav accordingly. Added revise/resubmit functionality to product loading module page.
//
// Revision 1.64  2007/10/17 12:34:46  lubbejd
// Many changes for RIT32957, mostly in setting up module change requests. Also added new method to WorkflowStep to show status description, and added "Product Module" to workflow.
//
// Revision 1.63  2007/10/02 17:17:00  lubbejd
// Beginning of changes for Module Change Request portion of RIT32957.
//
// Revision 1.62  2006/03/16 19:13:32  e0073445
// Removed code that was producing warnings.
//
// Revision 1.61  2006/01/05 22:20:15  e0073445
// Added District Strategy field
//
// Revision 1.60  2005/11/08 18:48:30  e0073445
// Change for contacts dropdown in Impacted Distributor list and customer category
//
// Revision 1.59  2005/01/31 20:29:50  vendejp
// few tweaks
//
// Revision 1.57  2005/01/31 19:51:44  vendejp
// Changed p4 of the distributor form to allow 12 county drop down boxes
//
// Revision 1.56  2005/01/24 22:30:44  vendejp
// Fixed bug where record wasnt inserted into DISTRIBUTOR table
//
// Revision 1.55  2005/01/14 13:15:30  lubbejd
// Changes to use srYear instead of current year
//
// Revision 1.54  2005/01/13 18:26:50  lubbejd
// Changed query to use srYear instead of hard coded 2004
//
// Revision 1.53  2005/01/13 18:06:14  lubbejd
// Replaced sysdate with srYear for searching on products table
//
// Revision 1.52  2005/01/10 03:00:24  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.51  2005/01/09 05:59:54  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.50  2005/01/05 22:40:24  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.49  2005/01/05 21:02:00  vendejp
// Changes for log4j logging and exception handling
//
// Revision 1.48  2004/12/23 18:12:50  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.47  2004/12/07 21:07:20  vendejp
// *** empty log message ***
//
// Revision 1.46  2004/12/01 21:29:38  vendejp
// *** empty log message ***
//
// Revision 1.45  2004/11/24 16:40:09  vendejp
// *** empty log message ***
//
// Revision 1.44  2004/11/17 21:40:49  vendejp
// *** empty log message ***
//
// Revision 1.43  2004/11/15 16:41:26  vendejp
// Completed the "Competitor Sales" figures on the distributor approval summary page
//
// Revision 1.42  2004/11/12 16:49:13  vendejp
// removed a System out println
//
// Revision 1.41  2004/11/10 22:47:57  vendejp
// *** empty log message ***
//
// Revision 1.40  2004/11/10 19:12:26  vendejp
// *** empty log message ***
//
// Revision 1.39  2004/11/03 19:17:12  vendejp
// *** empty log message ***
//
// Revision 1.38  2004/11/02 14:06:03  vendejp
// *** empty log message ***
//
// Revision 1.37  2004/10/30 22:52:42  vendejp
// Changes to accomodate header requirements.  Made User and Account objects set in the header object where I could.
//
// Revision 1.36  2004/10/22 21:21:08  vendejp
// *** empty log message ***
//
// Revision 1.35  2004/10/21 21:57:52  vendejp
// added loadModules code
//
// Revision 1.34  2004/10/21 06:24:36  vendejp
// *** empty log message ***
//
// Revision 1.33  2004/10/19 21:10:40  schweks
// Got a little crazy changing variable names and had to rename request.setAttribute("user" to "usr"
//
// Revision 1.32  2004/10/19 14:51:03  schweks
// Removing unused variables and code.
//
// Revision 1.31  2004/10/16 18:20:17  manupvj
// Added abstract servlet for common functions and common point of logging.
//
// Revision 1.30  2004/10/16 17:34:20  vendejp
// *** empty log message ***
//
// Revision 1.29  2004/10/14 17:40:50  schweks
// Added Eaton header comment.
// Reformatted source code.
//

package com.eaton.electrical.smrc;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.exception.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

public class DistributorSignup extends SMRCBaseServlet {

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
            int srYear = Globals.a2int((String) session.getAttribute("srYear"));
            if ( srYear == 0 ) {
            	Calendar cal = new GregorianCalendar();
                // Get the components of the date
                srYear = cal.get(Calendar.YEAR);
            }

            user = SMRCSession.getUser(request, DBConn);

            String page = request.getParameter("page");          
            String acctId = request.getParameter("acctId");
			Account acct = null;
			
			
			if(acctId!=null){
			    DistributorDAO.initializeDistributor(acctId, DBConn);
				acct = AccountDAO.getAccount(acctId, DBConn);
			}

            SMRCHeaderBean hdr = new SMRCHeaderBean();
            hdr.setUser(user);
            hdr.setAccount(acct);
            
            request.setAttribute("header", hdr);

            if (page == null || page.equals("app")) {
                page = "app1";
            }

            if (page.equalsIgnoreCase("credit")) {
                DistributorCreditAuthorization creditAuth = new DistributorCreditAuthorization();
                if (acctId != null) {
                    creditAuth = CreditAuthorizationDAO.getCreditAuth(acctId, DBConn);
                }
                if (creditAuth.getVcn().equals("")) {
                    creditAuth.setVcn(acctId);
                }

                request.setAttribute("creditAuth", creditAuth);
                sFwdUrl = "/distCreditAuthForm.jsp";

            } else if (page.equalsIgnoreCase("saveCredit")) {
                DistributorCreditAuthorization creditAuth = new DistributorCreditAuthorization();
                if (acctId != null) {
                    creditAuth = CreditAuthorizationDAO.getCreditAuth(acctId, DBConn);
                }
                if (creditAuth.getVcn().equals("")) {
                    creditAuth.setVcn(acctId);
                }

                creditAuth.setFinancialInformation(StringManipulation.noNull(request.getParameter("FINANCIAL_INFORMATION")));
                creditAuth.setTradeReference1(StringManipulation.noNull(request.getParameter("TRADE_REFERENCE1")));
                creditAuth.setTradeReference2(StringManipulation.noNull(request.getParameter("TRADE_REFERENCE2")));
                creditAuth.setTradeReference3(StringManipulation.noNull(request.getParameter("TRADE_REFERENCE3")));
                creditAuth.setTradeReference1Addr(StringManipulation.noNull(request.getParameter("TRADE_REFERENCE1_ADDR")));
                creditAuth.setTradeReference2Addr(StringManipulation.noNull(request.getParameter("TRADE_REFERENCE2_ADDR")));
                creditAuth.setTradeReference3Addr(StringManipulation.noNull(request.getParameter("TRADE_REFERENCE3_ADDR")));
                creditAuth.setTradeReference1Phone(StringManipulation.noNull(request.getParameter("TRADE_REFERENCE1_PHONE")));
                creditAuth.setTradeReference2Phone(StringManipulation.noNull(request.getParameter("TRADE_REFERENCE2_PHONE")));
                creditAuth.setTradeReference3Phone(StringManipulation.noNull(request.getParameter("TRADE_REFERENCE3_PHONE")));
                creditAuth.setTradeReference1Fax(StringManipulation.noNull(request.getParameter("TRADE_REFERENCE1_FAX")));
                creditAuth.setTradeReference2Fax(StringManipulation.noNull(request.getParameter("TRADE_REFERENCE2_FAX")));
                creditAuth.setTradeReference3Fax(StringManipulation.noNull(request.getParameter("TRADE_REFERENCE3_FAX")));
                creditAuth.setAdditionalSalesInfo(StringManipulation.noNull(request.getParameter("ADDITIONAL_SALES_INFORMATION")));

                CreditAuthorizationDAO.saveCreditAuth(creditAuth, DBConn);

                redirect = true;
                sFwdUrl = "/TargetAccountPlanner/DistributorSignup?page=credit&acctId=" + acctId + "&save=true";

            } else if (page.equalsIgnoreCase("impact")) {
                DistributorImpactAnalysis impactAnalysis = new DistributorImpactAnalysis();
                if (acctId != null) {
                    impactAnalysis = ImpactAnalysisDAO.getImpactAnalysis(
                            acctId, DBConn);
                }
                if (impactAnalysis.getVcn().equals("")) {
                    impactAnalysis.setVcn(acctId);
                }

                request.setAttribute("impactAnalysis", impactAnalysis);
                sFwdUrl = "/distImpactForm.jsp";

            } else if (page.equalsIgnoreCase("saveImpact")) {
                DistributorImpactAnalysis impactAnalysis = new DistributorImpactAnalysis();
                if (acctId != null) {
                    impactAnalysis = ImpactAnalysisDAO.getImpactAnalysis(
                            acctId, DBConn);
                }
                if (impactAnalysis.getVcn().equals("")) {
                    impactAnalysis.setVcn(acctId);
                }

                impactAnalysis.setMaintainDollars(Double.valueOf(request.getParameter("MAINTAIN_DOLLARS")).doubleValue());
                impactAnalysis.setGrowDollars(Double.valueOf(request.getParameter("GROW_DOLLARS")).doubleValue());
                impactAnalysis.setPenetrateDollars(Double.valueOf(request.getParameter("PENETRATE_DOLLARS")).doubleValue());
                impactAnalysis.setAddDollars(Double.valueOf(request.getParameter("ADD_DOLLARS")).doubleValue());
                impactAnalysis.setTerminateDollars(Double.valueOf(request.getParameter("TERMINATE_DOLLARS")).doubleValue());
                impactAnalysis.setRiskDollars(Double.valueOf(request.getParameter("RISK_DOLLARS")).doubleValue());
                impactAnalysis.setOtherChainImpact(Double.valueOf(request.getParameter("OTHER_CHAIN_IMPACT")).doubleValue());

                ImpactAnalysisDAO.saveImpactAnalysis(impactAnalysis, DBConn);

                redirect = true;
                sFwdUrl = "/TargetAccountPlanner/DistributorSignup?page=impact&acctId=" + acctId + "&save=true";

          
            } else if (page.equalsIgnoreCase("termination")) {
                DistributorTermination distTerm = new DistributorTermination();
                distTerm = DistributorTerminationDAO.getDistributorTermination(acctId, DBConn);
                request.setAttribute("distTerm", distTerm);

                sFwdUrl = "/distTerminationForm.jsp";

            } else if (page.equalsIgnoreCase("saveTermination")) {
                DistributorTermination distTerm = new DistributorTermination();
                if (acctId != null) {
                    distTerm = DistributorTerminationDAO
                            .getDistributorTermination(acctId, DBConn);
                }
                if (distTerm.getVcn().equals("")) {
                    distTerm.setVcn(acctId);
                }

                String proposedMon = StringManipulation.noNull(request
                        .getParameter("PROPOSED_TERMINATION_MON"));
                String proposedDay = StringManipulation.noNull(request
                        .getParameter("PROPOSED_TERMINATION_DAY"));
                String proposedYear = StringManipulation.noNull(request
                        .getParameter("PROPOSED_TERMINATION_YEAR"));
                Date proposedDate = Globals.getDate(proposedYear, proposedMon,
                        proposedDay);

                String effectiveMon = StringManipulation.noNull(request
                        .getParameter("TERMINATION_EFFECTIVE_MON"));
                String effectiveDay = StringManipulation.noNull(request
                        .getParameter("TERMINATION_EFFECTIVE_DAY"));
                String effectiveYear = StringManipulation.noNull(request
                        .getParameter("TERMINATION_EFFECTIVE_YEAR"));
                Date effectiveDate = Globals.getDate(effectiveYear,
                        effectiveMon, effectiveDay);
                distTerm.setReasonTypeId(Globals.a2int(request
                        .getParameter("TERMINATION_REASON_TYPE_ID")));
                distTerm.setProposedDate(proposedDate);
                distTerm.setEffectiveDate(effectiveDate);
                distTerm.setReasonTypeId(Globals.a2int(request
                        .getParameter("TERMINATION_REASON_TYPE_ID")));
                distTerm.setExplanation(StringManipulation.noNull(request
                        .getParameter("EXPLANATION")));
                distTerm.setActionNotes(StringManipulation.noNull(request
                        .getParameter("ACTION_NOTES")));
                distTerm.setEstInventoryStdDE(Double.valueOf(
                        request.getParameter("EST_INV_STDDE")).doubleValue());
                distTerm.setEstInventoryPDCD(Double.valueOf(
                        request.getParameter("EST_INV_PDCD")).doubleValue());
                distTerm.setEstInventoryStdControl(Double.valueOf(
                        request.getParameter("EST_INV_STDCTL")).doubleValue());
                distTerm.setPotReturnStdDE(Double.valueOf(
                        request.getParameter("POT_RET_STDDE")).doubleValue());
                distTerm.setPotReturnPDCD(Double.valueOf(
                        request.getParameter("POT_RET_PDCD")).doubleValue());
                distTerm.setPotReturnStdControl(Double.valueOf(
                        request.getParameter("POT_RET_STDCTL")).doubleValue());

                DistributorTerminationDAO.saveDistributorTermination(distTerm,
                        DBConn);

                redirect = true;
                sFwdUrl = "/TargetAccountPlanner/DistributorSignup?page=termination&acctId="
                        + acctId + "&save=true";
            } else if (page.equalsIgnoreCase("commitment")) {
               Distributor dist = new Distributor();
                
                String refresh = StringManipulation.noNull(request.getParameter("refresh"));
                if (refresh.equals("true") && session.getAttribute("distributor") != null) {
                    dist = (Distributor) session.getAttribute("distributor");
                    session.removeAttribute("distributor");
                } else if (acctId != null) {
                    dist = DistributorDAO.getDistributor(acctId, DBConn);
                }
                if (dist.getVcn().equals("")) {
                    dist.setVcn(acctId);
                }
                //IS THERE A COMMITMENT APPROVAL PROCESS
                //boolean hasApprovalProcess = CommitmentDAO.hasPendingApproval(dist.getVcn(), DBConn);
                
                //if(hasApprovalProcess) {
                	//CommitmentAuthorization.hasApprovalProcess(request, dist, DBConn);
                //}
                
                //request.setAttribute("history", CommitmentDAO.getCommitmentHistoryByVCN(acctId, DBConn));
                //request.setAttribute("approvalProcess", new Boolean(hasApprovalProcess));
				request.setAttribute("save", "notsave");
                request.setAttribute("dist", dist);

                request.setAttribute("commitmentProgram", MiscDAO.getCodes("commitment_program", DBConn));
                request.setAttribute("electricalLines", MiscDAO.getCodes("electrical_lines", DBConn));
                
            	sFwdUrl = "/Commitments.jsp";
            } else if (page.equalsIgnoreCase("approval")) {
                DistributorApproval distApproval = new DistributorApproval();

                String refresh = StringManipulation.noNull(request.getParameter("refresh"));
                ArrayList vendors = new ArrayList();
                
                if (refresh.equals("true") && session.getAttribute("distApproval") != null) {
                    distApproval = (DistributorApproval) session.getAttribute("distApproval");
                    session.removeAttribute("distApproval");
                    vendors = distApproval.getSummaryCompetitors();

                } else if (acctId != null) {
                    distApproval = ApprovalSummaryDAO.getApprovalSummary(acctId, srYear, DBConn);
                    vendors = ApprovalSummaryDAO.getSummaryCompetitors(distApproval.getDistributorId(),DBConn);
                }
                if (distApproval.getVcn().equals("")) {
                    distApproval.setVcn(acctId);
                    distApproval.setDistributorId(AccountDAO.getDistributorId(acctId,DBConn));
                }
                
                
                distApproval.setImpactedDistributors(ApprovalSummaryDAO.getImpactedDistributors(distApproval.getDistributorId(), srYear, DBConn));

                if (distApproval.getVcn().equals("")) {
                    distApproval.setVcn(acctId);
                }
              
                request.setAttribute("vendors",vendors);
                request.setAttribute("selectedCategory",AccountDAO.getCustomerCategory(acctId,DBConn));
                request.setAttribute("modules",ProductModuleDAO.getSelectedModules(distApproval.getDistributorId(), DBConn));
                request.setAttribute("distApproval", distApproval);

                sFwdUrl = "/distApprovalForm.jsp";

            } else if (page.equalsIgnoreCase("saveApproval")) {
                DistributorApproval distApproval = new DistributorApproval();
                if (acctId != null) {
                    distApproval = ApprovalSummaryDAO.getApprovalSummary(acctId, srYear, DBConn);
                }
                if (distApproval.getVcn().equals("")) {
                    distApproval.setVcn(acctId);
                    distApproval.setDistributorId(AccountDAO.getDistributorId(acctId,DBConn));
                }

                String commitMonth = StringManipulation.noNull(request.getParameter("EXCLUSIVE_EATON_COMMITMENT_MON"));
                String commitDay = StringManipulation.noNull(request.getParameter("EXCLUSIVE_EATON_COMMITMENT_DAY"));
                String commitYear = StringManipulation.noNull(request.getParameter("EXCLUSIVE_EATON_COMMITMENT_YEAR"));
                Date commitDate = Globals.getDate(commitYear, commitMonth, commitDay);
                
            

                String[] impactedAccounts = request.getParameterValues("IMP_DIST");
                distApproval.clearImpactedDistributors();
                if (impactedAccounts != null) {
                    for (int i = 0; i < impactedAccounts.length; i++) {
                        ImpactedDistributor impactedDist = new ImpactedDistributor();
                        impactedDist.setDistId(distApproval.getDistributorId());
                        impactedDist.setVcn(impactedAccounts[i]);
                        impactedDist.setSalesAtRisk(Globals.a2double(request.getParameter("IMP_DIST_SALES_AT_RISK_" + impactedAccounts[i])));
                        impactedDist.setContactId(Globals.a2int((request.getParameter("IMP_DIST_CONTACT_" + impactedAccounts[i]))));
                        impactedDist.setNotes(StringManipulation.noNull(request.getParameter("IMP_DIST_NOTES_" + impactedAccounts[i])));
                        distApproval.addImpactedDistributor(impactedDist);
                    }
                }

                distApproval.setCommitmentLevel(StringManipulation.noNull(request.getParameter("COMMITMENT_LEVEL")));
                distApproval.setExclusiveEatonCommitment(commitDate);
                distApproval.setProjEatonSalesYr1(Double.valueOf(request.getParameter("PROJECTED_EATON_SALES_YEAR1")).doubleValue());
                distApproval.setProjEatonSalesYr3(Double.valueOf(request.getParameter("PROJECTED_EATON_SALES_YEAR3")).doubleValue());
                distApproval.setNetAreaImpactYr1(Double.valueOf(request.getParameter("NET_AREA_IMPACT_YEAR1")).doubleValue());
                distApproval.setNetAreaImpactYr3(Double.valueOf(request.getParameter("NET_AREA_IMPACT_YEAR3")).doubleValue());

                distApproval.setDistrictStrategy(StringManipulation.noNull(request.getParameter(("DISTRICT_STRATEGY"))));
                
                if(request.getParameter("CUSTOMER_CATEGORY")!=null){
                	AccountDAO.setCustomerCategory(distApproval.getVcn(),request.getParameter("CUSTOMER_CATEGORY"),DBConn);
                }
                
                String[] summaryCompetitorIds = request.getParameterValues("SUMMARY_COMPETITORS");
                distApproval.clearSummaryCompetitors();
                if (summaryCompetitorIds != null) {
                    for (int i = 0; i < summaryCompetitorIds.length; i++) {
                        SummaryCompetitor summaryCompetitor = new SummaryCompetitor();
                        summaryCompetitor.setId(Globals.a2int(summaryCompetitorIds[i]));
                        summaryCompetitor.setDescription(request.getParameter("COMPETITOR_NAME_" + summaryCompetitorIds[i]));
                        summaryCompetitor.setYear1Sales(Globals.a2double(request.getParameter("COMPETITOR_YEAR1_" + summaryCompetitorIds[i])));
                        summaryCompetitor.setYear3Sales(Globals.a2double(request.getParameter("COMPETITOR_YEAR3_" + summaryCompetitorIds[i])));
                        distApproval.addSummaryCompetitor(summaryCompetitor);
                    }
                }
                
                ApprovalSummaryDAO.saveApprovalSummary(distApproval, DBConn);
            
                redirect = true;
                sFwdUrl = "/TargetAccountPlanner/DistributorSignup?page=approval&acctId=" + acctId + "&save=true";

            } else if (page.equalsIgnoreCase("refreshApproval")) {

                DistributorApproval distApproval = new DistributorApproval();
                if (acctId != null) {
                    distApproval = ApprovalSummaryDAO.getApprovalSummary(acctId, srYear, DBConn);
                }
                if (distApproval.getVcn().equals("")) {
                    distApproval.setVcn(acctId);
                    distApproval.setDistributorId(AccountDAO.getDistributorId(acctId,DBConn));
                }

                String commitMonth = StringManipulation.noNull(request.getParameter("EXCLUSIVE_EATON_COMMITMENT_MON"));
                String commitDay = StringManipulation.noNull(request.getParameter("EXCLUSIVE_EATON_COMMITMENT_DAY"));
                String commitYear = StringManipulation.noNull(request.getParameter("EXCLUSIVE_EATON_COMMITMENT_YEAR"));
                Date commitDate = Globals.getDate(commitYear, commitMonth,commitDay);

                distApproval.setCommitmentLevel(StringManipulation.noNull(request.getParameter("COMMITMENT_LEVEL")));
                distApproval.setExclusiveEatonCommitment(commitDate);
                distApproval.setProjEatonSalesYr1(Double.valueOf(request.getParameter("PROJECTED_EATON_SALES_YEAR1")).doubleValue());
                distApproval.setProjEatonSalesYr3(Double.valueOf(request.getParameter("PROJECTED_EATON_SALES_YEAR3")).doubleValue());
                distApproval.setNetAreaImpactYr1(Double.valueOf(request.getParameter("NET_AREA_IMPACT_YEAR1")).doubleValue());
                distApproval.setNetAreaImpactYr3(Double.valueOf(request.getParameter("NET_AREA_IMPACT_YEAR3")).doubleValue());

                String[] summaryCompetitorIds = request.getParameterValues("SUMMARY_COMPETITORS");
                distApproval.clearSummaryCompetitors();
                if (summaryCompetitorIds != null) {
                    for (int i = 0; i < summaryCompetitorIds.length; i++) {
                        SummaryCompetitor summaryCompetitor = new SummaryCompetitor();
                        summaryCompetitor.setId(Globals.a2int(summaryCompetitorIds[i]));
                        summaryCompetitor.setDescription(request.getParameter("COMPETITOR_NAME_" + summaryCompetitorIds[i]));
                        summaryCompetitor.setYear1Sales(Globals.a2double(request.getParameter("COMPETITOR_YEAR1_" + summaryCompetitorIds[i])));
                        summaryCompetitor.setYear3Sales(Globals.a2double(request.getParameter("COMPETITOR_YEAR3_" + summaryCompetitorIds[i])));
                        distApproval.addSummaryCompetitor(summaryCompetitor);
                    }
                }
                
                session.setAttribute("distApproval",distApproval); 
                redirect = true;
                sFwdUrl = "/TargetAccountPlanner/DistributorSignup?page=approval&refresh=true&acctId="
                        + acctId;
            } else if (page.equalsIgnoreCase("app1")) {
                //  page=app
                Distributor dist = new Distributor();
                
                String refresh = StringManipulation.noNull(request.getParameter("refresh"));
                if (refresh.equals("true") && session.getAttribute("distributor") != null) {
                    dist = (Distributor) session.getAttribute("distributor");
                    session.removeAttribute("distributor");
                } else if (acctId != null) {
                    dist = DistributorDAO.getDistributor(acctId, DBConn);
                    dist.setSegments(DistributorDAO.getDistributorSegments(dist, DBConn));
                }
                if (dist.getVcn().equals("")) {
                    dist.setVcn(acctId);
                }
                request.setAttribute("dist", dist);

                request.setAttribute("locationCodes", MiscDAO.getCodes("dist_location_type", DBConn));
                request.setAttribute("customerCategory", MiscDAO.getCodes("customer_category", DBConn));
                request.setAttribute("applyingForCodes", MiscDAO.getCodes("dist_applying_for", DBConn));
                request.setAttribute("ownershipCodes", MiscDAO.getCodes("dist_form_of_ownership", DBConn));
                request.setAttribute("bizActivityCodes", MiscDAO.getCodes("dist_primary_biz_activity", DBConn));
                request.setAttribute("facilitiesCodes", MiscDAO.getCodes("dist_facilities", DBConn));
                //request.setAttribute("commitmentProgram", MiscDAO.getCodes("commitment_program", DBConn));
                request.setAttribute("contacts", ContactsDAO.getContacts(acctId, DBConn));
                request.setAttribute("electricalLines", MiscDAO.getCodes("electrical_lines", DBConn));
                
                ArrayList productLines = ProductDAO.getSummaryProductLines(srYear, DBConn);

                request.setAttribute("productLines", productLines);
                request.setAttribute("counties", MiscDAO.getCounties(DBConn));
                request.setAttribute("buyingGroupCodes", MiscDAO.getCodes("dist_buying_grp", DBConn));
                
                sFwdUrl = "/distSignupForm.jsp";

            } else if (page.equalsIgnoreCase("saveApp1")) {
            	//System.out.println("I AM HERE");
                Distributor dist = DistributorDAO.getDistributor(acctId, DBConn);

                if (dist.getVcn().equals("")) {
                    dist.setVcn(acctId);
                }

                dist.setFederalTaxId(StringManipulation.noNull(request.getParameter("FEDERAL_TAX_ID")));
                dist.setDunnBradStreet(StringManipulation.noNull(request.getParameter("DUNN_BRADSTREET")));
                dist.setLocationType(Integer.parseInt(request.getParameter("LOCATION_TYPE_ID")));
                dist.setChainName(StringManipulation.noNull(request.getParameter("CHAIN_NAME")));
                dist.setCustomerCategory(Integer.parseInt(request.getParameter("CUSTOMER_CATEGORY")));
                dist.setApplyingFor(Integer.parseInt(request.getParameter("APPLYING_FOR_TYPE_ID")));
                dist.setApplyingForOtherNotes(StringManipulation.noNull(request.getParameter("APPLYING_FOR_OTHER_NOTES")));
                dist.setPreviousName(StringManipulation.noNull(request.getParameter("PREVIOUS_NAME")));
                dist.setPreviousVistaCustNumber(StringManipulation.noNull(request.getParameter("PREVIOUS_VISTA_CUSTOMER_NUMBER")));
                dist.setFormOfOwnership(Integer.parseInt(request.getParameter("OWNERSHIP_FORM_TYPE_ID")));
                dist.setFormOfOwnershipNotes(StringManipulation.noNull(request.getParameter("OWNERSHIP_FORM_NOTES")));
                dist.setParentCompany(StringManipulation.noNull(request.getParameter("PARENT_COMPANY")));
                dist.setPrimaryBusinessActivity(Integer.parseInt(request.getParameter("PRIMARY_BUS_ACTIVITY_TYPE_ID")));
                dist.setFacilityArea(StringManipulation.noNull(request.getParameter("FACILITY_AREA")));
                dist.clearFacilities();
                dist.setFacilities(request.getParameterValues("FACILITIES"));
                dist.setFacilitiesOther(StringManipulation.noNull(request.getParameter("FACILITIES_OTHER")));
                //dist.setCommitmentProgram(Integer.parseInt(request.getParameter("COMMITMENT_PROGRAM")));
                //dist.setCommitmentReason(StringManipulation.noNull(request.getParameter("COMMITMENT_REASON")));
                dist.setProjectedEatonSalesYr1(Globals.a2int(request.getParameter("PROJECTED_EATON_SALES_1")));
                dist.setProjectedEatonSalesYr2(Globals.a2int(request.getParameter("PROJECTED_EATON_SALES_2")));
                dist.setProjectedVScompYr1(Integer.parseInt(request.getParameter("PROJECTED_SALES_VS_COMP_1")));
                dist.setProjectedVScompYr2(Integer.parseInt(request.getParameter("PROJECTED_SALES_VS_COMP_2")));
                dist.clearCountySales();                
                for(int j=0;j<12;j++) {
                    dist.setCountySales(new DistributorCountySalesRecord(request.getParameter("COUNTY_SALES"+(j+1)), request.getParameter("COUNTY_SALES"+(j+1)+"_VALUE"))); 
                }
               
                String[] buyingGroup = request.getParameterValues("BUYING_GROUP");
                dist.clearBuyingGroupAssn();
                if (buyingGroup != null) {
                    for (int i = 0; i < buyingGroup.length; i++) {
                        dist.addBuyingGroupAssn(buyingGroup[i]);
                    }
                }
                dist.setBuyingGroupOther(StringManipulation.noNull(request.getParameter("BUYING_GROUP_OTHER")));


                String[] electricalLines = request.getParameterValues("ELECTRICAL_LINES");
                dist.clearElectricalLines();
                dist.setElectricalLines(electricalLines);
                
                String[] segmentIds = request.getParameterValues("SEGMENT_IDS");
                dist.clearSegments();
                for (int i = 0; i < segmentIds.length; i++) {
                    dist.setSegment(Globals.a2int(segmentIds[i]),Globals.a2int(request.getParameter("SEGMENT_" + segmentIds[i])),StringManipulation.noNull(request.getParameter("SEGMENT_NAME_"+ segmentIds[i])));
                }
                //System.out.println("OTHER = " +Globals.a2int(request.getParameter("OTHER_CUSTOMER_SEGMENT")) );
                //FThread.sleep(2000);
                dist.setOtherCustomerSegmentPercentage(Globals.a2int(request.getParameter("OTHER_CUSTOMER_SEGMENT")));
                dist.setOtherCustomerSegmentPercentageNote(StringManipulation.noNull(request.getParameter("OTHER_CUSTOMER_SEGMENT_VALUE")));
                
                dist.setDistributorNotes(StringManipulation.noNull(request.getParameter("DISTRIBUTOR_NOTES")));
                DistributorDAO.saveDistributor(dist, 1, DBConn);
                


                redirect = true;
                //********************************************************************************
                //*******************************************************************************
                //THIS IS REALLY BAD -- IT MUST MATCH THE path ELEMENT IN THE SERVER.XML
                sFwdUrl = "/TargetAccountPlanner/DistributorSignup?page=app&acctId=" + acctId + "&save=true";
                //sFwdUrl = "/tap/DistributorSignup?page=app&acctId=" + acctId + "&save=true";

            } else if (page.equalsIgnoreCase("app2")) {
                Distributor dist = new Distributor();
                if (acctId != null) {
                    dist = DistributorDAO.getDistributor(acctId, DBConn);
                }
                if (dist.getVcn().equals("")) {
                    dist.setVcn(acctId);
                }

                request.setAttribute("dist", dist);

                
                
                request.setAttribute("servicesCodes", MiscDAO.getCodes("dist_services", DBConn));
                request.setAttribute("ediCustCodes", MiscDAO.getCodes("dist_edi_cust", DBConn));
                request.setAttribute("ediSuppliersCodes", MiscDAO.getCodes("dist_edi_suppliers", DBConn));
                request.setAttribute("vmiCodes", MiscDAO.getCodes("dist_vmi_transactions", DBConn));
                request.setAttribute("webAccessCodes", MiscDAO.getCodes("dist_web_access", DBConn));

                sFwdUrl = "/distSignupForm2.jsp";

            } else if (page.equalsIgnoreCase("saveApp2")) {
                Distributor dist = DistributorDAO.getDistributor(acctId, DBConn);

                if (dist.getVcn().equals("")) {
                    dist.setVcn(acctId);
                }

                
                
                dist.setNumOfBranches(Integer.parseInt(request.getParameter("NUM_OF_BRANCH_LOCATIONS")));
                dist.setNumOfYrsAtLocation(Integer.parseInt(request.getParameter("NUM_OF_YEARS_AT_LOCATION")));
                
                
                dist.setServicesOther(StringManipulation.noNull(request.getParameter("SERVICES_OTHER")));
                dist.clearFacilities();
                dist.clearServices();
                dist.clearECommerce();
                dist.setFacilities(request.getParameterValues("FACILITIES"));
                dist.setServices(request.getParameterValues("SERVICES"));
                dist.setECommerce(request.getParameterValues("ECOMMERCE"));
                dist.setFacilitiesOther(StringManipulation.noNull(request.getParameter("FACILITIES_OTHER")));
                dist.setServicesOther(StringManipulation.noNull(request.getParameter("SERVICES_OTHER")));


                DistributorDAO.saveDistributor(dist, 2, DBConn);


                redirect = true;
                sFwdUrl = "/TargetAccountPlanner/DistributorSignup?page=app2&acctId=" + acctId + "&save=true";

            } else if (page.equalsIgnoreCase("app3")) {
                Distributor dist = new Distributor();

                String refresh = StringManipulation.noNull(request.getParameter("refresh"));
                if (refresh.equals("true") && session.getAttribute("distributor") != null) {
                    dist = (Distributor) session.getAttribute("distributor");
                    session.removeAttribute("distributor");
                } else if (acctId != null) {
                    dist = DistributorDAO.getDistributor(acctId, DBConn);
                }
                if (dist.getVcn().equals("")) {
                    dist.setVcn(acctId);
                }

                
                request.setAttribute("dist", dist);

                sFwdUrl = "/distSignupForm3.jsp";

            } else if (page.equalsIgnoreCase("refreshApp1")) {

                Distributor dist = new Distributor();
                if (acctId != null) {
                    dist = DistributorDAO.getDistributor(acctId, DBConn);
                }
                if (dist.getVcn().equals("")) {
                    dist.setVcn(acctId);
                }

                dist.setFederalTaxId(StringManipulation.noNull(request.getParameter("FEDERAL_TAX_ID")));
                dist.setDunnBradStreet(StringManipulation.noNull(request.getParameter("DUNN_BRADSTREET")));
                dist.setLocationType(Integer.parseInt(request.getParameter("LOCATION_TYPE_ID")));
                dist.setChainName(StringManipulation.noNull(request.getParameter("CHAIN_NAME")));
                dist.setCustomerCategory(Integer.parseInt(request.getParameter("CUSTOMER_CATEGORY")));
                dist.setApplyingFor(Integer.parseInt(request.getParameter("APPLYING_FOR_TYPE_ID")));
                dist.setApplyingForOtherNotes(StringManipulation.noNull(request.getParameter("APPLYING_FOR_OTHER_NOTES")));
                dist.setPreviousName(StringManipulation.noNull(request.getParameter("PREVIOUS_NAME")));
                dist.setPreviousVistaCustNumber(StringManipulation.noNull(request.getParameter("PREVIOUS_VISTA_CUSTOMER_NUMBER")));
                dist.setFormOfOwnership(Integer.parseInt(request.getParameter("OWNERSHIP_FORM_TYPE_ID")));
                dist.setFormOfOwnershipNotes(StringManipulation.noNull(request.getParameter("OWNERSHIP_FORM_NOTES")));
                dist.setParentCompany(StringManipulation.noNull(request.getParameter("PARENT_COMPANY")));
                dist.setPrimaryBusinessActivity(Integer.parseInt(request.getParameter("PRIMARY_BUS_ACTIVITY_TYPE_ID")));
                dist.setFacilityArea(StringManipulation.noNull(request.getParameter("FACILITY_AREA")));
                dist.clearFacilities();
                dist.setFacilities(request.getParameterValues("FACILITIES"));
                dist.setFacilitiesOther(StringManipulation.noNull(request.getParameter("FACILITIES_OTHER")));
                //dist.setCommitmentProgram(Integer.parseInt(request.getParameter("COMMITMENT_PROGRAM")));
                dist.setProjectedEatonSalesYr1(Globals.a2int(request.getParameter("PROJECTED_EATON_SALES_1")));
                dist.setProjectedEatonSalesYr2(Globals.a2int(request.getParameter("PROJECTED_EATON_SALES_2")));
                dist.setProjectedVScompYr1(Integer.parseInt(request.getParameter("PROJECTED_SALES_VS_COMP_1")));
                dist.setProjectedVScompYr2(Integer.parseInt(request.getParameter("PROJECTED_SALES_VS_COMP_2")));
                dist.clearCountySales();                
                for(int j=0;j<12;j++) {
                    dist.setCountySales(new DistributorCountySalesRecord(request.getParameter("COUNTY_SALES"+(j+1)), request.getParameter("COUNTY_SALES"+(j+1)+"_VALUE"))); 
                }
               
                String[] buyingGroup = request.getParameterValues("BUYING_GROUP");
                dist.clearBuyingGroupAssn();
                if (buyingGroup != null) {
                    for (int i = 0; i < buyingGroup.length; i++) {
                        dist.addBuyingGroupAssn(buyingGroup[i]);
                    }
                }
                dist.setBuyingGroupOther(StringManipulation.noNull(request.getParameter("BUYING_GROUP_OTHER")));
                
                String[] electricalLines = request.getParameterValues("ELECTRICAL_LINES");
                dist.setElectricalLines(electricalLines);
                
                String[] segmentIds = request.getParameterValues("SEGMENT_IDS");
                dist.clearSegments();
                for (int i = 0; i < segmentIds.length; i++) {
                    dist.setSegment(Globals.a2int(segmentIds[i]),Globals.a2int(request.getParameter("SEGMENT_" + segmentIds[i])),StringManipulation.noNull(request.getParameter("SEGMENT_NAME_"+ segmentIds[i])));
                }
                dist.setOtherCustomerSegmentPercentage(Globals.a2int(request.getParameter("OTHER_CUSTOMER_SEGMENT")));
                dist.setOtherCustomerSegmentPercentageNote(StringManipulation.noNull(request.getParameter("OTHER_CUSTOMER_SEGMENT_VALUE")));
                
                dist.setDistributorNotes(StringManipulation.noNull(request.getParameter("DISTRIBUTOR_NOTES")));

                session.setAttribute("distributor", dist);

                redirect = true;
                sFwdUrl = "/TargetAccountPlanner/DistributorSignup?page=app1&refresh=true&acctId=" + acctId;

            } else if (page.equalsIgnoreCase("saveApp3")) {
                Distributor dist = DistributorDAO.getDistributor(acctId, DBConn);

                if (dist.getVcn().equals("")) {
                    dist.setVcn(acctId);
                }

                dist.setInsideSalesConstrPersonnel(Integer.parseInt(request.getParameter("INSIDE_SALES_CONSTR_PERSONNEL")));
                dist.setInsideSalesGenPersonnel(Integer.parseInt(request.getParameter("INSIDE_SALES_INDSTR_PERSONNEL")));
                dist.setInsideSalesIndstrPersonnel(Integer.parseInt(request.getParameter("INSIDE_SALES_GEN_PERSONNEL")));
                dist.setOutsideSalesConstrPersonnel(Integer.parseInt(request.getParameter("OUTSIDE_SALES_CONSTR_PERSONNEL")));
                dist.setOutsideSalesGenPersonnel(Integer.parseInt(request.getParameter("OUTSIDE_SALES_INDSTR_PERSONNEL")));
                dist.setOutsideSalesIndstrPersonnel(Integer.parseInt(request.getParameter("OUTSIDE_SALES_GEN_PERSONNEL")));
                dist.setManagementPersonnel(Integer.parseInt(request.getParameter("MANAGEMENT_PERSONNEL")));
                dist.setCounterSalesPersonnel(Integer.parseInt(request.getParameter("COUNTER_SALES_PERSONNEL")));
                dist.setSpecialistPersonnel(Integer.parseInt(request.getParameter("SPECIALIST_PERSONNEL")));
                dist.setElectricalEngPersonnel(Integer.parseInt(request.getParameter("ELECTRICAL_ENGINEER_PERSONNEL")));
                dist.setWhseDriversPersonnel(Integer.parseInt(request.getParameter("WHSE_DRIVERS_PERSONNEL")));
                dist.setAdminPersonnel(Integer.parseInt(request.getParameter("ADMIN_PERSONNEL")));

                DistributorDAO.saveDistributor(dist, 3, DBConn);

                redirect = true;
                sFwdUrl = "/TargetAccountPlanner/DistributorSignup?page=app3&acctId=" + acctId + "&save=true";

            } else if (page.equalsIgnoreCase("app4")) {
                Distributor dist = new Distributor();

                String refresh = StringManipulation.noNull(request.getParameter("refresh"));
                if (refresh.equals("true") && session.getAttribute("distributor") != null) {
                    dist = (Distributor) session.getAttribute("distributor");
                    session.removeAttribute("distributor");
                    
                } else if (acctId != null) {
                    dist = DistributorDAO.getDistributor(acctId, DBConn);
                    dist.setSegments(DistributorDAO.getDistributorSegments(dist, DBConn));
                }
                if (dist.getVcn().equals("")) {
                    dist.setVcn(acctId);
                }

                //dist.setSegments(DistributorDAO.getDistributorSegments(dist,DBConn));
                ArrayList keyAccounts = DistributorDAO.getKeyAccounts(dist.getId(), DBConn);
                ArrayList vendors = MiscDAO.getVendors(true, DBConn);
//                int productPeriod = ProductDAO.getProductsPeriod(dist.getId(),DBConn);
//                ArrayList productLines = ProductDAO.getSummaryProductLines(productPeriod, DBConn);
               // jdl - I think we want to use the srYear instead using the lines above
                ArrayList productLines = ProductDAO.getSummaryProductLines(srYear, DBConn);

                request.setAttribute("productLines", productLines);
                request.setAttribute("vendors", vendors);
                request.setAttribute("keyAccounts", keyAccounts);
                request.setAttribute("dist", dist);

                request.setAttribute("counties", MiscDAO.getCounties(DBConn));

                sFwdUrl = "/distSignupForm4.jsp";

            } else if (page.equalsIgnoreCase("refreshApp4")) {
            	//DistributorDAO.initializeDistributor(acctId,DBConn);
                Distributor dist = DistributorDAO.getDistributor(acctId, DBConn);

                if (dist.getVcn().equals("")) {
                    dist.setVcn(acctId);
                }

                dist.clearCountySales();
                
                for(int j=0;j<12;j++) {
                    dist.setCountySales(new DistributorCountySalesRecord(request.getParameter("COUNTY_SALES"+(j+1)), request.getParameter("COUNTY_SALES"+(j+1)+"_VALUE"))); 
                }

                String[] productLines = request
                        .getParameterValues("PRODUCT_LINES");
                dist.clearProducts();
                for (int i = 0; i < productLines.length; i++) {
                    Product prod = new Product();
                    prod.setId(StringManipulation.noNull(productLines[i]));
                    prod.setPeriodYYYY(Integer.parseInt(request.getParameter("PRODUCT_PERIOD")));
                    prod.setTotalSales(Globals.a2double(request.getParameter("PRODUCTLINE_TOTAL_" + productLines[i])));
                    prod.setTotalSalesThruStock(StringManipulation.noNull(request.getParameter("PRODUCTLINE_STOCK_" + productLines[i])));
                    prod.setCurrentPrimaryManufacturer(StringManipulation.noNull(request.getParameter("PRODUCTLINE_CURRENT_PRIMARY_" + productLines[i])));
                    prod.setCurrentSecondaryManufacturer(StringManipulation.noNull(request.getParameter("PRODUCTLINE_CURRENT_SECONDARY_" + productLines[i])));
                    prod.setCurrentOtherManufacturer(StringManipulation.noNull(request.getParameter("PRODUCTLINE_CURRENT_OTHER_" + productLines[i])));
                    prod.setProposedPrimaryManufacturer(StringManipulation.noNull(request.getParameter("PRODUCTLINE_PROPOSED_PRIMARY_" + productLines[i])));
                    prod.setProposedSecondaryManufacturer(StringManipulation.noNull(request.getParameter("PRODUCTLINE_PROPOSED_SECONDARY_" + productLines[i])));
                    prod.setProposedOtherManufacturer(StringManipulation.noNull(request.getParameter("PRODUCTLINE_PROPOSED_OTHER_" + productLines[i])));
                    dist.setProducts(prod);
                }

                String[] segmentIds = request.getParameterValues("SEGMENT_IDS");
                dist.clearSegments();
                for (int i = 0; i < segmentIds.length; i++) {
                	dist.setSegment(Globals.a2int(segmentIds[i]), Globals.a2int(request.getParameter("SEGMENT_" + segmentIds[i])), StringManipulation.noNull(request.getParameter("SEGMENT_NAME_" + segmentIds[i])));
                }

                
                /*
                 * This actually saves key accounts to the database on refresh
                 * logic on this page is so crazy.  Will need cleaned up.
                 */
		        String[] deleteKeyAccounts = request.getParameterValues("DELETE_KEY_ACCOUNTS");
		        if (deleteKeyAccounts != null) {
		            for (int i = 0; i < deleteKeyAccounts.length; i++) {
		                DistributorDAO.deleteKeyAccount(dist.getId(), deleteKeyAccounts[i], DBConn);
		            }
		        }
		
		        String[] keyAccounts = request.getParameterValues("KEY_ACCOUNTS");
		        if (keyAccounts != null) {
		            for (int i = 0; i < keyAccounts.length; i++) {
		                boolean isDeleted = false;
		                if (deleteKeyAccounts != null) {
		                    for (int j = 0; j < deleteKeyAccounts.length; j++) {
		                        if (keyAccounts[i].equals(deleteKeyAccounts[j])) {
		                            isDeleted = true;
		                            break;
		                        }
		                    }
		                }
		                if (!isDeleted) {
		                    DistributorDAO.updateKeyAccount(dist.getId(),keyAccounts[i],request.getParameterValues("KEY_ACCOUNT_PRODUCTS_" + keyAccounts[i]),request.getParameter("KEY_ACCOUNT_POTENTIAL_" + keyAccounts[i]),DBConn);
		                }
		            }
		        }
                
                
                dist.setOtherCustomerSegmentPercentage(Globals.a2int(request.getParameter("OTHER_CUSTOMER_SEGMENT")));
                dist.setOtherCustomerSegmentPercentageNote(StringManipulation.noNull(request.getParameter("OTHER_CUSTOMER_SEGMENT_VALUE")));
            	
            	
                
                session.setAttribute("distributor", dist);

                redirect = true;
                sFwdUrl = "/TargetAccountPlanner/DistributorSignup?page=app4&refresh=true&acctId=" + acctId;

            } else if (page.equalsIgnoreCase("saveApp4")) {
                Distributor dist = DistributorDAO.getDistributor(acctId, DBConn);

                if (dist.getVcn().equals("")) {
                    dist.setVcn(acctId);
                }
                dist.clearCountySales();
                
                for(int j=0;j<12;j++) {
                    dist.setCountySales(new DistributorCountySalesRecord(request.getParameter("COUNTY_SALES"+(j+1)), request.getParameter("COUNTY_SALES"+(j+1)+"_VALUE"))); 
                }

                String[] productLines = request.getParameterValues("PRODUCT_LINES");
                dist.clearProducts();
                for (int i = 0; i < productLines.length; i++) {
                    Product prod = new Product();
                    prod.setId(StringManipulation.noNull(productLines[i]));
                    prod.setPeriodYYYY(Integer.parseInt(request.getParameter("PRODUCT_PERIOD")));
                    prod.setTotalSales(Globals.a2double(request.getParameter("PRODUCTLINE_TOTAL_" + productLines[i])));
                    prod.setTotalSalesThruStock(StringManipulation.noNull(request.getParameter("PRODUCTLINE_STOCK_" + productLines[i])));
                    prod.setCurrentPrimaryManufacturer(StringManipulation.noNull(request.getParameter("PRODUCTLINE_CURRENT_PRIMARY_" + productLines[i])));
                    prod.setCurrentSecondaryManufacturer(StringManipulation.noNull(request.getParameter("PRODUCTLINE_CURRENT_SECONDARY_" + productLines[i])));
                    prod.setCurrentOtherManufacturer(StringManipulation.noNull(request.getParameter("PRODUCTLINE_CURRENT_OTHER_" + productLines[i])));
                    prod.setProposedPrimaryManufacturer(StringManipulation.noNull(request.getParameter("PRODUCTLINE_PROPOSED_PRIMARY_" + productLines[i])));
                    prod.setProposedSecondaryManufacturer(StringManipulation.noNull(request.getParameter("PRODUCTLINE_PROPOSED_SECONDARY_" + productLines[i])));
                    prod.setProposedOtherManufacturer(StringManipulation.noNull(request.getParameter("PRODUCTLINE_PROPOSED_OTHER_" + productLines[i])));
                    dist.setProducts(prod);
                }

                String[] deleteKeyAccounts = request.getParameterValues("DELETE_KEY_ACCOUNTS");
                if (deleteKeyAccounts != null) {
                    for (int i = 0; i < deleteKeyAccounts.length; i++) {
                        DistributorDAO.deleteKeyAccount(dist.getId(), deleteKeyAccounts[i], DBConn);
                    }
                }

                String[] keyAccounts = request.getParameterValues("KEY_ACCOUNTS");
                if (keyAccounts != null) {
                    for (int i = 0; i < keyAccounts.length; i++) {
                        boolean isDeleted = false;
                        if (deleteKeyAccounts != null) {
                            for (int j = 0; j < deleteKeyAccounts.length; j++) {
                                if (keyAccounts[i].equals(deleteKeyAccounts[j])) {
                                    isDeleted = true;
                                    break;
                                }
                            }
                        }
                        if (!isDeleted) {
                            DistributorDAO.updateKeyAccount(dist.getId(),keyAccounts[i],request.getParameterValues("KEY_ACCOUNT_PRODUCTS_" + keyAccounts[i]),request.getParameter("KEY_ACCOUNT_POTENTIAL_" + keyAccounts[i]),DBConn);
                        }
                    }
                }

                String[] segmentIds = request.getParameterValues("SEGMENT_IDS");
                dist.clearSegments();
                for (int i = 0; i < segmentIds.length; i++) {
                    dist.setSegment(Globals.a2int(segmentIds[i]),Globals.a2int(request.getParameter("SEGMENT_" + segmentIds[i])),StringManipulation.noNull(request.getParameter("SEGMENT_NAME_"+ segmentIds[i])));
                }
                dist.setOtherCustomerSegmentPercentage(Globals.a2int(request.getParameter("OTHER_CUSTOMER_SEGMENT")));
                dist.setOtherCustomerSegmentPercentageNote(StringManipulation.noNull(request.getParameter("OTHER_CUSTOMER_SEGMENT_VALUE")));
            	
            	
                DistributorDAO.saveDistributor(dist, 4, DBConn);

                redirect = true;
                sFwdUrl = "/TargetAccountPlanner/DistributorSignup?page=app4&acctId=" + acctId + "&save=true";

            } else if (page.equalsIgnoreCase("app5")) {
                Distributor dist = new Distributor();

                if (acctId != null) {
                    dist = DistributorDAO.getDistributor(acctId, DBConn);

                }
                if (dist.getVcn().equals("")) {
                    dist.setVcn(acctId);
                }

                ArrayList vendors = MiscDAO.getVendors(false, DBConn);
                ArrayList supplierProducts = DistributorDAO.getSupplierProducts(DBConn);
                ArrayList selectedSupplierProducts = DistributorDAO.getSelectedSupplierProducts(dist, DBConn);

                request.setAttribute("selectedSupplierProducts",selectedSupplierProducts);
                request.setAttribute("supplierProducts", supplierProducts);
                request.setAttribute("vendors", vendors);

                request.setAttribute("dist", dist);
                request.setAttribute("buyingGroupCodes", MiscDAO.getCodes("dist_buying_grp", DBConn));

                sFwdUrl = "/distSignupForm5.jsp";

            } else if (page.equalsIgnoreCase("saveApp5")) {
                Distributor dist = DistributorDAO.getDistributor(acctId, DBConn);

                if (dist.getVcn().equals("")) {
                    dist.setVcn(acctId);
                }

                dist.clearSuppliersProducts();
                for (int i = 0; i < 5; i++) {
                    if (!request.getParameter("PRODUCT_" + i).equals("0") && !request.getParameter("VENDOR_" + i).equals("0")) {
                        dist.addSuppliersProducts(new DistributorSupplierProduct(request.getParameter("PRODUCT_" + i),request.getParameter("VENDOR_" + i)));
                    }
                }

                dist.setCurrentYrSalesEstimate(Globals.a2int(request.getParameter("CURRENT_YR_SALES_EST")));
                dist.setPriorYrActualSales(Globals.a2int(request.getParameter("PRIOR_YR_SALES_ACTUAL")));
                dist.setPrior2YrsActualSales(Globals.a2int(request.getParameter("PRIOR_2YR_SALES_ACTUAL")));
                dist.setPrior3YrsTotalSales(Globals.a2int(request.getParameter("PRIOR_3YR_SALES_ACTUAL")));
                dist.setApproxInventory(Globals.a2int(request.getParameter("APROX_INVENTORY")));
                dist.setProjectedEatonSalesYr1(Globals.a2int(request.getParameter("PROJECTED_EATON_SALES_1")));
                dist.setProjectedEatonSalesYr2(Globals.a2int(request.getParameter("PROJECTED_EATON_SALES_2")));
                dist.setNaedParticipation(StringManipulation.noNull(request.getParameter("NAED")));

                String[] buyingGroup = request.getParameterValues("BUYING_GROUP");
                dist.clearBuyingGroupAssn();
                if (buyingGroup != null) {
                    for (int i = 0; i < buyingGroup.length; i++) {
                        dist.addBuyingGroupAssn(buyingGroup[i]);
                    }
                }

                dist.setBuyingGroupOther(StringManipulation.noNull(request.getParameter("BUYING_GROUP_OTHER")));
                dist.setTradeAssociations(StringManipulation.noNull(request.getParameter("TRADE_ASSNS")));
                dist.setDistributorNotes(StringManipulation.noNull(request.getParameter("DISTRIBUTOR_NOTES")));

                DistributorDAO.saveDistributor(dist, 5, DBConn);


                redirect = true;
                sFwdUrl = "/TargetAccountPlanner/DistributorSignup?page=app5&acctId=" + acctId + "&save=true";
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