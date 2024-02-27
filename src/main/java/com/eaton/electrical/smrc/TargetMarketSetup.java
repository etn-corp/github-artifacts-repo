//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.64  2007/06/06 15:45:52  lubbejd
// Retrieve latest info for target market before sending email.
//
// Revision 1.63  2007/05/18 19:04:34  c9962207
// Changed the user changed to empty string when a revise/resubmit is taking place. Also updated sending email code.
//
// Revision 1.62  2007/05/14 16:48:17  lubbejd
// Fixed calculations of forecasted growth on targetMarketSetup and target market report results.
//
// Revision 1.61  2007/05/03 14:38:25  c9962207
// Updated getTargetMarketPlanPriorApprovers method. Changed else if targetMarketPlanId == 0 to return an empty array list instead of throwing an exception.
//
// Revision 1.60  2007/05/01 20:01:02  c9962207
// Added Target Market Plan setup revise/resubmit functionality.
//
// Revision 1.59  2007/04/25 12:54:34  lubbejd
// Add workflow approvals updating for target market plans to pending approvals.
//
// Revision 1.58  2007/04/23 18:04:57  lubbejd
// Fixed calculations of projected growth. Fixed projections calculations ifprevious year did not have any sales. Fixed sales dollars if end customers and "end market dollars" are selected.
//
// Revision 1.57  2007/04/10 18:54:02  lubbejd
// More changes for target market reports/setup.
//
// Revision 1.56  2007/04/10 14:32:30  lubbejd
// Changes to add "Group by Plan" filter. Corrected all group by queries and target market setup page.
//
// Revision 1.55  2007/04/05 18:28:34  lubbejd
// Changed the calculations of growth, growth percentage, and payout.
//
// Revision 1.54  2007/04/03 18:20:16  lubbejd
// Use Y instead of A for approved flag.
//
// Revision 1.53  2007/04/03 18:15:11  lubbejd
// Changes for using the workflow tables for target market plans.
//
// Revision 1.52  2006/10/26 18:54:38  lubbejd
// CR 35687, Functionality 2.1 - Allow users to purge "saved" or "rejected" target market plans.
//
// Revision 1.51  2006/10/26 15:05:42  lubbejd
// CR 35687, Functionality 2.1 - Place asterisk next to currently viewed plan. Add status next to each TM Plan. Arrange list of plans by date added, descending. Allow decimal values for growth/payout table values.
//
// Revision 1.50  2006/03/16 19:13:32  e0073445
// Removed code that was producing warnings.
//
// Revision 1.49  2005/06/28 18:39:20  lubbejd
// merged changes from QA_1_2_1.
//
// Revision 1.48.4.2  2005/06/23 18:54:34  lubbejd
// Changed account ArrayList to use Strings instead Account objects. Fixed some resulting null pointer problems from getting geog this way.
//
// Revision 1.48.4.1  2005/06/22 17:18:12  lubbejd
// Changed account ArrayList to use Strings instead Account objects.
//
// Revision 1.48  2005/03/10 13:49:54  lubbejd
// Replaced TreeMap with TargetMarketSortingBean and TargetMarketGrowthPercentageSorter
// for sorting the growth/payout percentages to fix problems that occur
// when less than 4 percentages are entered. (Bug #878)
//
// Revision 1.47  2005/01/24 16:18:46  lubbejd
// Correct problems with saving and sending target market plans
//
// Revision 1.46  2005/01/12 04:50:43  schweks
// Changed to use Globals.a2int instead.
//
// Revision 1.45  2005/01/10 03:00:23  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.44  2005/01/09 05:59:53  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.43  2005/01/05 22:40:24  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.42  2004/12/23 18:12:50  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.41  2004/12/16 22:27:41  vendejp
// Changes so that the creator of an account can edit the account even if he/she does not have geog driven security
//
// Revision 1.40  2004/12/15 18:07:43  vendejp
// Fixed some bugs and added segment overrides everywhere.
//
// Revision 1.39  2004/12/09 15:28:37  vendejp
// Changes to the way the User object operates.  Have to explicitly check for security override now.
//
// Revision 1.38  2004/11/12 11:53:02  lubbejd
// Changed vcn.length() check in saving plan from "== 6" to " > 1" to allow users
// to save P-number customers from the customer pop-up windows.
//
// Revision 1.37  2004/11/10 18:50:11  lubbejd
// Store end date with last day of the month for day
//
// Revision 1.36  2004/11/09 14:48:37  lubbejd
// Changes for handling start/end dates
//
// Revision 1.35  2004/11/03 12:29:41  lubbejd
// additional security for Target Market Setup
//
// Revision 1.34  2004/11/02 14:06:04  vendejp
// *** empty log message ***
//
// Revision 1.33  2004/10/30 22:52:42  vendejp
// Changes to accomodate header requirements.  Made User and Account objects set in the header object where I could.
//
// Revision 1.32  2004/10/29 18:46:36  lubbejd
// add TODO comment
//
// Revision 1.31  2004/10/28 10:55:07  lubbejd
// added TODO comments
//
// Revision 1.30  2004/10/27 14:14:25  lubbejd
// Remove references to Bidman Rebate for CIFD
//
// Revision 1.29  2004/10/25 12:51:36  lubbejd
// Fixed some approval/rejection things..
//
// Revision 1.28  2004/10/20 11:32:34  lubbejd
// add method to put growth percentage table in order before saving
//
// Revision 1.27  2004/10/19 15:16:26  schweks
// Removing unused variables and code.
//
// Revision 1.26  2004/10/19 14:59:49  lubbejd
// changes to target market setup
//
// Revision 1.25  2004/10/18 20:10:33  schweks
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

/**
 * 
 * @author Jason Lubbert
 */

//TODO  -  javascript validation in jsp

public class TargetMarketSetup extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        javax.servlet.http.HttpSession session;
        User user = null;
        Connection DBConn = null;
        String sFwdUrl = "/SMRCErrorPage.jsp";

        boolean redirect = false;
        
        try {
            DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();

            session = request.getSession();

            user = SMRCSession.getUser(request, DBConn);

            Account acct = null;
    		String acctId = request.getParameter("acctId");
    		if(acctId!=null){
    			acct = AccountDAO.getAccount(acctId, DBConn);
    		} else if (request.getParameter("tmId") != null && !request.getParameter("tmId").trim().equals("")){
    			// this should mean user is coming from Target Market Reports
    			ArrayList accounts = TargetMarketDAO.getTMOtherAccounts(Globals.a2int(request.getParameter("tmId").trim()),DBConn);
    			if (accounts.size() > 0){
    				acctId = (String) accounts.get(0);
    				acct = AccountDAO.getAccount(acctId,DBConn);
    			}
    			
    		}
    		
    		int targetMarketPlanId = (request.getParameter("tmId") != null) ? Integer.parseInt(request.getParameter("tmId")) : 0;
    		ArrayList priorApprovers = getTargetMarketPlanPriorApprovers(targetMarketPlanId, DBConn);
			request.setAttribute("priorApprovers", priorApprovers);
    		
            SMRCHeaderBean hdr = new SMRCHeaderBean();
            hdr.setUser(user);
            hdr.setAccount(acct);
            request.setAttribute("header", hdr);
            if (!user.ableToSee(acct) && !user.hasSegmentOverride(acct) && !(user.equals(acct.getUserIdAdded()) && acct.isProspect())){
            	throw new Exception("User cannot view this account");
            }
            
            writeSetupPage(request, DBConn, user, acct, session);
            
            sFwdUrl = "/targetMarketSetup.jsp";

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

    private void writeSetupPage(HttpServletRequest request, Connection DBConn, User user,
            Account account, javax.servlet.http.HttpSession session) throws Exception {

        TargetMarket targetMarket = null;
		
        String seName = SalesDAO.getSalesmanName(account.getSalesEngineer1(),
                DBConn);
        int tmId = 0;
        ArrayList directSalesResults = new ArrayList();
        ArrayList allContacts = new ArrayList();
        boolean purgeTM = false;
        if (request.getParameter("action") != null) {
            targetMarket = (TargetMarket) session.getAttribute("targetMarket");
            String action = request.getParameter("action");
            String previousStatus = targetMarket.getStatus();
            if (action.equalsIgnoreCase("save")) {
                targetMarket.setStatus("S");
                tmId = saveTargetMarket(targetMarket, request, DBConn, session,
                        user);
            } else if (action.equalsIgnoreCase("purge")){
            	targetMarket.setStatus("P");
            	saveTargetMarket(targetMarket,request,DBConn,session,user);
            	purgeTM = true;
            } else if (action.equalsIgnoreCase("send")) {
                targetMarket.setStatus("B");
                tmId = saveTargetMarket(targetMarket, request, DBConn, session,
                        user);
                WorkflowDAO.updateTargetMarketPlanWorkflow(previousStatus,"Y",tmId,user,DBConn);
                // Need to commit before sending email
                SMRCConnectionPoolUtils.commitTransaction(DBConn);
                // Get new targetMarket information after saving
                targetMarket = TargetMarketDAO.getTargetMarket(tmId, DBConn);
                TargetMarketEmail tmEmail = new TargetMarketEmail(targetMarket,
                        DBConn);
                tmEmail.sendEmail();
            } else if (action.equalsIgnoreCase("approve")) {
                if (targetMarket.isSubmitted()) {
                    targetMarket.setStatus("D");
                } else if (targetMarket.isDMApproved()) {
                    targetMarket.setStatus("A");
                }
                tmId = saveTargetMarket(targetMarket, request, DBConn, session,
                        user);
                WorkflowDAO.updateTargetMarketPlanWorkflow(previousStatus,"Y",tmId,user,DBConn);
                targetMarket = TargetMarketDAO.getTargetMarket(tmId, DBConn);
                TargetMarketEmail tmEmail = new TargetMarketEmail(targetMarket,
                        DBConn);
                tmEmail.sendEmail();
            } else if (action.equalsIgnoreCase("reject")) {
                targetMarket.setStatus("R");
                tmId = saveTargetMarket(targetMarket, request, DBConn, session,
                        user);
                WorkflowDAO.updateTargetMarketPlanWorkflow(previousStatus,"R",tmId,user,DBConn);
                TargetMarketEmail tmEmail = new TargetMarketEmail(targetMarket,
                        DBConn);
                tmEmail.sendEmail();
            }else if (action.equalsIgnoreCase("revise")) {

            	String targetMarketStatus = request.getParameter("targetMarketStatus");
            	if(targetMarketStatus != null){
            		targetMarket.setStatus(targetMarketStatus);
                	//update target market status
                	updateTargetMarketStatus(targetMarket.getTargetMarketPlanId(), targetMarketStatus, DBConn);
            	}
            	else{
            		throw new Exception("targetMarketStatus is null");
            	}
            	
            	tmId = saveTargetMarket(targetMarket, request, DBConn, session, user);

            	String tmIdStr = request.getParameter("tmId");
            	
            	//write update workflow code, same as the pending approval revise code.
            	//then, send email (part of workflow code in pending approvals).
            	
            	//***************************************************************************

            	//approvalId is the "workflow_approval_id"
            	//acctId is the vcn (vista customer number)
            	//String acctId = account.getVcn();
            	String workflowType = "Target Market Plans";
            	String reviseResubmitExplantion = request.getParameter("reviseResubmitExplantion");
            	
            	//get the selected approver workflow approval Id from the selected radio button
            	String selectedPriorApproverWorkflowApprovalId = request.getParameter("selectedPriorApproverWorkflowApprovalId");

            	if(selectedPriorApproverWorkflowApprovalId != null){
    	        
    	        	String selectedPriorApproverUserId = WorkflowDAO.getWorkflowApprovalUserChanged(selectedPriorApproverWorkflowApprovalId, DBConn);
    	        	User selectedPriorApproverUser = UserDAO.getUser(selectedPriorApproverUserId, DBConn);

    	        	//now update all the workflow_approvals with vista_customer_number (given) that has data changed later (aka greater) than 
    	        	//the date_changed for the given work_approval_id
    	        	WorkflowDAO.updateTargetMarketWorkflowLaterApproversToNotApproved(tmIdStr, selectedPriorApproverWorkflowApprovalId, DBConn);
            		
            		//Change the status 
            		WorkflowDAO.changeApprovalStatus(selectedPriorApproverWorkflowApprovalId, "", "V", tmIdStr, DBConn);//user that submitted the resive resub.
    	        	Workflow workflow = WorkflowDAO.getWorkflow(tmIdStr,workflowType,user,DBConn);//user that submitted the resive resub.
    	        	
    	        	//if(workflow.getWorkflowType().equalsIgnoreCase("Target Market Plans") || workflow.getWorkflowType().equalsIgnoreCase("Customer") || workflow.getWorkflowType().equalsIgnoreCase("Distributor Application")){
    	        		//System.out.println("changing status to pending resubmission.....");
    	        		//AccountDAO.changeAccountStatus(tmIdStr,"Pending Resubmission",DBConn);
    	    		//}
    	        	
    	        	//user that submitted the resive resub.
    	        	ReviseResubExplanationDAO.insertReviseResubExplanationWithoutTargetProject(Integer.parseInt(selectedPriorApproverWorkflowApprovalId), reviseResubmitExplantion, user.getUserid(), DBConn);//user that submitted the resive resub.
    	        	
    	        	//user to whome the resive resub was submitted.
    	        	
    	        	//workflow.sendReviseAndResubmitNotification(account, selectedPriorApproverUser, DBConn, reviseResubmitExplantion);
    	        	//workflow.sendTargetMarketReviseAndResubmitNotification(account, selectedPriorApproverUser, DBConn, reviseResubmitExplantion);

    	        	targetMarket = TargetMarketDAO.getTargetMarket(tmId,DBConn);
    	        	
    	        	TargetMarketEmail tmEmail = new TargetMarketEmail(targetMarket,DBConn);
    	        	tmEmail.sendReviseResubmitEmail(selectedPriorApproverUser, reviseResubmitExplantion);

            	}
            	else{
            		throw new Exception("selectedPriorApproverUserId is null");
            	}
            	
            	//***************************************************************************

            	/*if (targetMarket.isSubmitted()) {
                    targetMarket.setStatus("D");
                } else if (targetMarket.isDMApproved()) {
                    targetMarket.setStatus("A");
                }
                tmId = saveTargetMarket(targetMarket, request, DBConn, session,
                        user);
                this.updateWorkflow(previousStatus,"Y",tmId,user,DBConn);
                TargetMarketEmail tmEmail = new TargetMarketEmail(targetMarket,
                        DBConn);
                tmEmail.sendEmail();*/

            } 
            else if (action.equalsIgnoreCase("silentsave")) {
                if (targetMarket.getTargetMarketPlanId() == 0
                        || targetMarket.isUnsaved()) {
                    targetMarket.setStatus("U");
                }
                tmId = saveTargetMarket(targetMarket, request, DBConn, session,
                        user);
            }

        }
        //   Only want the TargetMarket in session for updating above
        session.removeAttribute("targetMarket");
        session.removeAttribute("combinedProductList");

        if (((request.getParameter("tmId") != null) || (tmId > 0)) && !purgeTM) {
            if (tmId == 0) {
                tmId = Globals.a2int(request.getParameter("tmId"));
            }
            targetMarket = TargetMarketDAO.getTargetMarket(tmId, DBConn);
            if (targetMarket.isActive()) {
                directSalesResults = TargetMarketDAO.getSalesForApprovedTM(targetMarket, DBConn);
            } else {
                directSalesResults = TargetMarketDAO.getSalesForTM(targetMarket, DBConn);
            }
            allContacts = TargetMarketDAO.getAllContactsForAllTMAccounts(tmId,DBConn);

        } else {
            allContacts = account.getContacts();
            directSalesResults = TargetMarketDAO.getSalesForVCN(account.getVcn(),DBConn);
            targetMarket = new TargetMarket();
        }

        ArrayList combinedProductList = new ArrayList();
        combinedProductList = combineProductResults(directSalesResults, targetMarket, request, DBConn);
        ArrayList competitors = MiscDAO.getVendors(true, DBConn);
        ArrayList otherTargetMarketList = TargetMarketDAO.getAccountTMIds(account.getVcn(), DBConn);
        ArrayList allTMSegments = SegmentsDAO.getTargetMarketSegments(DBConn);
        ArrayList salesObjectives = TargetMarketDAO.getSalesObjectives(DBConn);
        ArrayList salesTrackingDollars = TargetMarketDAO.getSalesTrackingDollars(DBConn);

        TreeMap planAcctMap = getCustomerNamesOfAccounts(targetMarket.getPlanAccounts(),DBConn);
        TreeMap endCustMap = getCustomerNamesOfAccounts(targetMarket.getEndCustomers(),DBConn);
        boolean canApprove = determineApproval(targetMarket, user, DBConn);

        request.setAttribute("canApprove", new Boolean(canApprove));
        request.setAttribute("combinedProductList", combinedProductList);
        request.setAttribute("seName", seName);
        request.setAttribute("account", account);
        request.setAttribute("competitors", competitors);
        request.setAttribute("targetMarket", targetMarket);
        request.setAttribute("otherTargetMarketList", otherTargetMarketList);
        request.setAttribute("allTMSegments", allTMSegments);
        request.setAttribute("salesObjectives", salesObjectives);
        request.setAttribute("salesTrackingDollars", salesTrackingDollars);
        request.setAttribute("allContacts", allContacts);
        request.setAttribute("tmId", new Integer(tmId));
        request.setAttribute("planAcctMap", planAcctMap);
        request.setAttribute("endCustMap", endCustMap);

    }

    // This method combines the information from the ArrayList of all the
    // products and the
    // information from the ArrayList of products in the target market plan so
    // the jsp
    // can work with one list
    private ArrayList combineProductResults(ArrayList allProducts, TargetMarket targetMarket, HttpServletRequest request, Connection dbConn) throws Exception {
        ArrayList combinedProducts = new ArrayList();
        ArrayList tmProducts = targetMarket.getPlanProducts();
        double planTotalSales = 0;
        double prevPlanTotalBaseline = 0;
        double prevPlanToDate = 0;
        for (int allIndex = 0; allIndex < allProducts.size(); allIndex++) {
            ArrayList productList = (ArrayList) allProducts.get(allIndex);
            TargetMarketProduct targetMarketProduct = new TargetMarketProduct();
            targetMarketProduct.setProduct((Product) productList.get(0));
            Product thisProduct = (Product) productList.get(0);
           // SMRCLogger.debug("product " + thisProduct.getId());
            targetMarketProduct.setYTDSales(((Double) productList.get(1)).doubleValue());
            targetMarketProduct.setPrevSales(((Double) productList.get(2)).doubleValue());
            targetMarketProduct.setPrevPlanToDateSales(((Double) productList.get(3)).doubleValue());
            if (targetMarket.isActive()) {
                double salesInPlan = ((Double) productList.get(4)).doubleValue();
                targetMarketProduct.setSalesInPlan(salesInPlan);
                double baselineSales = ((Double) productList.get(5)).doubleValue();
                targetMarketProduct.setBaselineSales(baselineSales);
                double projectedGrowthPercentage = 0;
                double projectedGrowth = 0;
                double payout = 0;
                if (targetMarketProduct.getPrevPlanToDateSales() != 0){
	                projectedGrowthPercentage = ((targetMarketProduct.getSalesInPlan() - targetMarketProduct.getPrevPlanToDateSales()) / targetMarketProduct.getPrevPlanToDateSales());
	                targetMarketProduct.setGrowthPercentage(projectedGrowthPercentage);
	                projectedGrowth = ((targetMarketProduct.getBaselineSales() * (1 + targetMarketProduct.getGrowthPercentage())) - targetMarketProduct.getBaselineSales());
	                SMRCLogger.debug("Product id: " + thisProduct.getId() + "  projectedGrowth: " + projectedGrowth + " targetMarketProduct.getPrevPlanToDateSales()= " + targetMarketProduct.getPrevPlanToDateSales() + " targetMarketProduct.getGrowthPercentage()= " + targetMarketProduct.getGrowthPercentage());
	                targetMarketProduct.setTotalGrowth(projectedGrowth);
	                payout = determinePayout(projectedGrowth,targetMarketProduct.getGrowthPercentage(), targetMarket);
	                targetMarketProduct.setPayout(payout);
                } else {
                	this.setProratedGrowthValues(targetMarketProduct,targetMarket,dbConn);
                }
            }
            for (int tmIndex = 0; tmIndex < tmProducts.size(); tmIndex++) {
                TargetMarketProduct tmProduct2 = (TargetMarketProduct) tmProducts
                        .get(tmIndex);
                if (tmProduct2.getProductId().equalsIgnoreCase(
                        thisProduct.getId())) {
                    targetMarketProduct.setChecked("checked");
                    targetMarketProduct.setSalesObjective(tmProduct2.getSalesObjective());
                    double incrementalGrowth = (tmProduct2.getSalesObjective())
                            - (targetMarketProduct.getPrevSales());
                    targetMarketProduct.setIncrementalGrowth(incrementalGrowth);
                    prevPlanToDate += targetMarketProduct.getPrevPlanToDateSales();
                    prevPlanTotalBaseline += targetMarketProduct.getBaselineSales();
                    planTotalSales += targetMarketProduct.getSalesInPlan();
                }
            }
            String checked = StringManipulation.noNull(targetMarketProduct.getChecked());
            if ((targetMarket.isActive())
                    && (checked.equalsIgnoreCase("checked"))) {
                combinedProducts.add(targetMarketProduct);
            } else if (!targetMarket.isActive()) {
                combinedProducts.add(targetMarketProduct);
            }
        }
        if (targetMarket.isActive()){
            double planGrowthPercentage = 0;
            double planTotalGrowth = (planTotalSales - prevPlanToDate);
            SMRCLogger.debug("planTotalSales = " + planTotalSales + " prevPlanToDate= " + prevPlanToDate + " planTotalGrowth=" + planTotalGrowth);
           // if (prevPlanToDate > 0) {
           //     planGrowthPercentage = (planTotalGrowth / prevPlanToDate) * 100;
           // } else {
           //     planGrowthPercentage = 100;
           // }
            if (prevPlanToDate > 0) {
                planGrowthPercentage = (planTotalGrowth / prevPlanToDate) ;
            } else {
                planGrowthPercentage = 1;
            }
            SMRCLogger.debug("planGrowthPercentage = " + planGrowthPercentage);
           // double forecastedGrowth = (prevPlanTotalBaseline * (planGrowthPercentage / 100));
            double forecastedGrowth = (prevPlanTotalBaseline * planGrowthPercentage );
            double planTotalPayout = determinePayout(forecastedGrowth, planGrowthPercentage, targetMarket);
            request.setAttribute("planTotalPayout", new Double(planTotalPayout));
        }

        return combinedProducts;
    }

    //** This method returns the payout figure for this product based on this
    // target market's growth/payout table
    private double determinePayout(double totalGrowth, double growthPercentage,TargetMarket targetMarket) {
    	SMRCLogger.debug("TargetMarketSetup.determinePayout  totalGrowth = " + totalGrowth + "  growthPercentage" + growthPercentage);
        double payout = 0;
        double payoutPercentage = 0;
        ArrayList sortingBeans = new ArrayList();
        sortingBeans.add(new TargetMarketSortingBean(targetMarket.getPercentGrowth1(), targetMarket.getPercentPayout1()));
        sortingBeans.add(new TargetMarketSortingBean(targetMarket.getPercentGrowth2(), targetMarket.getPercentPayout2()));
        sortingBeans.add(new TargetMarketSortingBean(targetMarket.getPercentGrowth3(), targetMarket.getPercentPayout3()));
        sortingBeans.add(new TargetMarketSortingBean(targetMarket.getPercentGrowth4(), targetMarket.getPercentPayout4()));
        TargetMarketGrowthPercentageSorter sorter = new TargetMarketGrowthPercentageSorter();
        Collections.sort(sortingBeans, sorter);
        for (int i=0; i< sortingBeans.size(); i++){
            TargetMarketSortingBean bean = (TargetMarketSortingBean) sortingBeans.get(i);
            SMRCLogger.debug("growth% " + growthPercentage + "  bean.growth% " + bean.getGrowthPercentage());
            if ((growthPercentage * 100) >= bean.getGrowthPercentage()) {
                payoutPercentage = bean.getPayoutPercentage();
            }
        }
        SMRCLogger.debug("calculating payout - payoutPercentage= " + payoutPercentage + "  totalGrowth= "  + totalGrowth);
        if (payoutPercentage > 0) {
            payout = (totalGrowth *  (payoutPercentage / 100));
        }
        SMRCLogger.debug("payout=" + payout);
        return payout;

    }

    //** Performs inserting or updating of target market information on all
    // related tables
    //** Returns the target market id, which is necessary for new plans
    private int saveTargetMarket(TargetMarket targetMarket,
            HttpServletRequest request, Connection DBConn,
            javax.servlet.http.HttpSession session, User user) throws Exception {
        ArrayList planSegmentList = new ArrayList();
        ArrayList planEndCustList = new ArrayList();
        ArrayList planAcctList = new ArrayList();
        ArrayList planContactList = new ArrayList();
        ArrayList planProductList = new ArrayList();
        Calendar cal = Calendar.getInstance();
        java.util.Date startDate = targetMarket.getStartDate();
        java.util.Date endDate = targetMarket.getEndDate();
        cal.setTime(startDate);
        int startDateMo = cal.get(Calendar.MONTH) + 1;
        int startDateYear = cal.get(Calendar.YEAR);
        cal.setTime(endDate);
        int endDateMo = cal.get(Calendar.MONTH) + 1;
        int endDateYear = cal.get(Calendar.YEAR);
        Enumeration params = request.getParameterNames();
        planAcctList.add(request.getParameter("acctId"));
        while (params.hasMoreElements()) {
            String param = (String) params.nextElement();
            String[] value = request.getParameterValues(param);
            for (int i = 0; i < value.length; i++) {
                if (param.equalsIgnoreCase("planDescription")) {
                    targetMarket.setPlanDescription(value[i]);
                } else if (param.equalsIgnoreCase("primaryContact")) {
                    if (Globals.a2int(value[i]) > 0) {
                        targetMarket.setContactId(Globals.a2int(value[i]));
                    }
                } else if (param.equalsIgnoreCase("competitorConvert")) {
                    targetMarket.setCompetitorConvert("Y");
                } else if (param.equalsIgnoreCase("competitor")) {
                    if (Globals.a2int(value[i]) > 0) {
                        targetMarket.setCompetitorId(Globals.a2int(value[i]));
                    }
                } else if (param.equalsIgnoreCase("segmentOtherNotes")) {
                    targetMarket.setSegmentOtherNotes(value[i]);
                } else if (param.equalsIgnoreCase("businessObjective")) {
                    targetMarket.setBusinessObjective(value[i]);
                } else if (param.equalsIgnoreCase("startDateMo")){
                	startDateMo = Globals.a2int(value[i]);
                } else if (param.equalsIgnoreCase("startDateYear")){
                	startDateYear = Globals.a2int(value[i]);
  /*              } else if (param.equalsIgnoreCase("startDate")) {
                    java.util.Date date = convertDate(value[i]);
                    targetMarket.setStartDate(date);
                } else if (param.equalsIgnoreCase("endDate")) {
                    java.util.Date date = convertDate(value[i]);
                    targetMarket.setEndDate(date);
  */
                } else if (param.equalsIgnoreCase("endDateMo")){
                	endDateMo = Globals.a2int(value[i]);
                } else if (param.equalsIgnoreCase("endDateYear")){
                	endDateYear = Globals.a2int(value[i]);
                } else if (param.equalsIgnoreCase("salesObjectives")) {
                    targetMarket.setSalesObjective(Globals.a2int(value[i]));
                } else if (param.equalsIgnoreCase("salesObjectiveOtherNotes")) {
                    targetMarket.setSalesObjectiveOtherNotes(value[i]);
                } else if (param.equalsIgnoreCase("salesPlan")) {
                    if (value[i].equalsIgnoreCase("salesPlan1")) {
                        targetMarket.setSalesPlanUsed("Y");
                    } else {
                        targetMarket.setSalesPlanUsed("N");
                    }
 //               } else if (param.equalsIgnoreCase("bidManRebate")) {
   //                 targetMarket.setIncludeBidmanRebate("Y");
                } else if (param.equalsIgnoreCase("percentGrowth1")) {
                    targetMarket.setPercentGrowth1(Globals.a2double(value[i]));
                } else if (param.equalsIgnoreCase("percentGrowth2")) {
                    targetMarket.setPercentGrowth2(Globals.a2double(value[i]));
                } else if (param.equalsIgnoreCase("percentGrowth3")) {
                    targetMarket.setPercentGrowth3(Globals.a2double(value[i]));
                } else if (param.equalsIgnoreCase("percentGrowth4")) {
                    targetMarket.setPercentGrowth4(Globals.a2double(value[i]));
                } else if (param.equalsIgnoreCase("percentPayout1")) {
                    targetMarket.setPercentPayout1(Globals.a2double(value[i]));
                } else if (param.equalsIgnoreCase("percentPayout2")) {
                    targetMarket.setPercentPayout2(Globals.a2double(value[i]));
                } else if (param.equalsIgnoreCase("percentPayout3")) {
                    targetMarket.setPercentPayout3(Globals.a2double(value[i]));
                } else if (param.equalsIgnoreCase("percentPayout4")) {
                    targetMarket.setPercentPayout4(Globals.a2double(value[i]));
                } else if (param.equalsIgnoreCase("salesTrackingDollar")) {
                    targetMarket.setSalesTrackingTypeId(Globals.a2int(value[i]));
                } else if (param.equalsIgnoreCase("maximumPayout")) {
                    targetMarket.setMaximumPayout((int) Globals.a2double(value[i]));
                } else if (param.equalsIgnoreCase("segments")) {
                    planSegmentList.add((new Integer(value[i])));
                } else if (param.equalsIgnoreCase("distributorContacts")) {
                    Contact contact = ContactsDAO.getContact(value[i], DBConn);
                    planContactList.add(contact);
                    //  addAccount should be a full 6 character vista customer
                    // number if coming from TargetMarketCustomerBrowse
                } else if (param.equalsIgnoreCase("distributors")
                        || (param.equalsIgnoreCase("addAccount") && value[i]
                                .length() > 1)) {
                    String vcn = value[i];
                    if (!vcn.equalsIgnoreCase(request.getParameter("removeAccount"))) {
                        planAcctList.add(vcn);
                    }
                } else if (param.equalsIgnoreCase("endcustomers")
                        || (param.equalsIgnoreCase("addendcustomer") && value[i]
                                .length() > 1)) {
                    String vcn = value[i];
                    if (!vcn.equalsIgnoreCase(request.getParameter("removeEndCustomer"))) {
                        planEndCustList.add(vcn);
                    }
                }
            }
        }
        // Set target dates in java.util.Date format
        java.util.Date date = convertDate(startDateMo, startDateYear, "start");
        targetMarket.setStartDate(date);
        date = convertDate(endDateMo, endDateYear, "end");
        targetMarket.setEndDate(date);
        
        
        String[] accountChildValues = request.getParameterValues("addAccountChildren");
        for (int i = 0; i < accountChildValues.length; i++) {
            if (accountChildValues[i].length() == 6) {
                String parent = accountChildValues[i];
                ArrayList children = AccountDAO.getAllChildrenForVCN(parent,DBConn);
                for (int childIndex = 0; childIndex < children.size(); childIndex++) {
                    String childAccount = (String) children.get(childIndex);
                    // Don't want to duplicate parent number since it was added
                    // above in addAccount parameter
                    if (!childAccount.equalsIgnoreCase(parent)) {
                        planAcctList.add(childAccount);
                    }
                }
            }
        }
        String[] endCustomerChildValues = request.getParameterValues("addEndCustomerChildren");
        for (int i = 0; i < endCustomerChildValues.length; i++) {
            if (endCustomerChildValues[i].length() == 6) {
                String parent = endCustomerChildValues[i];
                ArrayList children = AccountDAO.getAllChildrenForVCN(parent,DBConn);
                for (int childIndex = 0; childIndex < children.size(); childIndex++) {
                    String childAccount = (String) children.get(childIndex);
                    // Don't want to duplicate parent number since it was added
                    // above in addEndCustomer parameter
                    if (!childAccount.equalsIgnoreCase(parent)) {
                        planEndCustList.add(childAccount);
                    }
                }
            }
        }
        targetMarket.setPlanSegments(planSegmentList);
        targetMarket.setPlanContacts(planContactList);
        targetMarket.setPlanAccounts(planAcctList);
        targetMarket.setEndCustomers(planEndCustList);

        // Loops through products to match up to productChecked parameter
        ArrayList allProducts = (ArrayList) session.getAttribute("combinedProductList");
        for (int i = 0; i < allProducts.size(); i++) {
            TargetMarketProduct targetMarketProduct = (TargetMarketProduct) allProducts.get(i);
            String id = targetMarketProduct.getProduct().getId();
            if (request.getParameter("productChecked" + id) != null) {
                targetMarketProduct.setChecked("checked");
                if (request.getParameter("prodSalesObj" + id) != null) {
                    String[] value = request.getParameterValues("prodSalesObj"
                            + id);
                    double prodSalesObjective = (new Double(value[0]))
                            .doubleValue();
                    targetMarketProduct.setSalesObjective(prodSalesObjective);
                }
            } else {
                targetMarketProduct.setChecked("");
            }
            if (targetMarketProduct.getChecked().equalsIgnoreCase("checked")) {
                planProductList.add(targetMarketProduct);
            }
        }

        if (planProductList.size() > 0) {
            targetMarket.setPlanProducts(planProductList);
        }

        int tmId = 0;

        // Before saving, always sort the percent table in ascending order
        adjustPercentTableOrder(targetMarket);
        if (targetMarket.getTargetMarketPlanId() > 0) {
            tmId = targetMarket.getTargetMarketPlanId();
            TargetMarketDAO.updateTargetMarketPlan(targetMarket, DBConn, user
                    .getUserid());
         } else {
            tmId = TargetMarketDAO.insertNewTargetMarketPlan(targetMarket,
                    DBConn, user.getUserid());
            WorkflowDAO.initializeWorkflow((""+tmId),Workflow.WORKFLOW_TYPE_TARGET_MARKET_PLANS,user,DBConn);
        }

        return tmId;

    }

    //Vince: put this somewhere where we can share it like in a DateUtils class
    // or something.
    // Returns date with the inputted month, year, and a DAY_OF_MONTH of 1
    private java.util.Date convertDate(int month, int year, String startEnd) {
        Calendar calendar = Calendar.getInstance();
        // Subtract one to get correct system month
        calendar.set(Calendar.MONTH, (month - 1));
        calendar.set(Calendar.YEAR, year);
        if (startEnd.equalsIgnoreCase("end")){
        	int dayOfMonth = getLastDayOfMonth(year, month - 1);
        	calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        } else {
        	calendar.set(Calendar.DAY_OF_MONTH, 1);
        }
        java.util.Date newDate = new java.util.Date();
        newDate = calendar.getTime();
        return newDate;

    }

    //** TODO - Correct logic for determining if user is Channel Marketing Manager

    private boolean determineApproval(TargetMarket targetMarket, User user, Connection DBConn) throws Exception {

    	if (targetMarket.isSubmitted() && (user.isDistrictManager() || user.hasOverrideSecurity())) {
    	    if (user.isDistrictManager()){
	            String userGeog = user.getGeography();
	            ArrayList tmAccounts = targetMarket.getPlanAccounts();
	            for (int i = 0; i < tmAccounts.size(); i++) {
	                String account = (String) tmAccounts.get(i);
	                String geog = AccountDAO.getAccountGeog(account,DBConn);
	                 // Only needs to match one of the geographies involved
	                if (geog!= null && userGeog.substring(0, 5).equalsIgnoreCase(geog.substring(0, 5))) {
	                    return true;
	                }
	            }
	              return false;
    	    } else {
    	        // user must have override security
    	        return true;
    	    }
            // return true; // for testing
        }
        if (targetMarket.isDMApproved() && (user.isChannelMarketingManager() || user.hasOverrideSecurity())) {
            return true;
        }
        // All other situations get false
         return false;
       // return true; // for testing
    }
    
    //** This method assures the growth percentage table is stored in ascending order
    private void adjustPercentTableOrder (TargetMarket targetMarket){
        ArrayList sortingBeans = new ArrayList();
        sortingBeans.add(new TargetMarketSortingBean(targetMarket.getPercentGrowth1(), targetMarket.getPercentPayout1()));
        sortingBeans.add(new TargetMarketSortingBean(targetMarket.getPercentGrowth2(), targetMarket.getPercentPayout2()));
        sortingBeans.add(new TargetMarketSortingBean(targetMarket.getPercentGrowth3(), targetMarket.getPercentPayout3()));
        sortingBeans.add(new TargetMarketSortingBean(targetMarket.getPercentGrowth4(), targetMarket.getPercentPayout4()));
        TargetMarketGrowthPercentageSorter sorter = new TargetMarketGrowthPercentageSorter();
        Collections.sort(sortingBeans, sorter);
        for (int i=0; i< sortingBeans.size(); i++){
            TargetMarketSortingBean bean = (TargetMarketSortingBean) sortingBeans.get(i);
            if (i == 0){
                targetMarket.setPercentGrowth1(bean.getGrowthPercentage());
                targetMarket.setPercentPayout1(bean.getPayoutPercentage());
            } else if (i == 1){
                targetMarket.setPercentGrowth2(bean.getGrowthPercentage());
                targetMarket.setPercentPayout2(bean.getPayoutPercentage());
            } else if (i == 2){
                targetMarket.setPercentGrowth3(bean.getGrowthPercentage());
                targetMarket.setPercentPayout3(bean.getPayoutPercentage());
            } else if (i == 3){
                targetMarket.setPercentGrowth4(bean.getGrowthPercentage());
                targetMarket.setPercentPayout4(bean.getPayoutPercentage());
            } 
            
        }
        
    }
    
    private static int getLastDayOfMonth(int year, int month) {
    	java.util.GregorianCalendar cal = new java.util.GregorianCalendar(year, month, 1);
        // Next month
        cal.roll(Calendar.MONTH, true);
        // Day before beginning of next month
        cal.roll(java.util.Calendar.DAY_OF_YEAR, false);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    
    private TreeMap getCustomerNamesOfAccounts(ArrayList acctList, Connection DBConn) throws Exception{
    	TreeMap acctMap = new TreeMap();
    	for (int i=0; i < acctList.size(); i++){
    		String acctId = (String) acctList.get(i);
    		String custName = AccountDAO.getAccountName(acctId, DBConn);
    		acctMap.put(custName, acctId);
    	}
    	
    	return acctMap;
    }

   
    
    // This method is called when the previousPlanToDateSales is zero to determine a reasonable 
    // growth percentage and payout
    private void setProratedGrowthValues (TargetMarketProduct targetMarketProduct, TargetMarket targetMarket, Connection dbConn) throws Exception {
    	int srMonth = Globals.a2int(MiscDAO.getSRMonth(dbConn));
    	int srYear = Globals.a2int(MiscDAO.getSRYear(dbConn));
    	
    	Calendar endCal = Calendar.getInstance();
    	Calendar beginCal = Calendar.getInstance();
    	endCal.setTime(targetMarket.getEndDate());
    	beginCal.setTime(targetMarket.getStartDate());
    	
    	
    	boolean planOver = false;
    	if (srYear > endCal.get(Calendar.YEAR)){
    		planOver = true;
    	} else if ((srMonth - 1) > endCal.get(Calendar.MONTH)){
    		planOver = true;
    	}
    	
    	double projectedGrowthPercentage = targetMarketProduct.getSalesInPlan()  / 100;
        targetMarketProduct.setGrowthPercentage(projectedGrowthPercentage);
        
    	if (planOver){
    		double projectedGrowth = targetMarketProduct.getSalesInPlan();
            targetMarketProduct.setTotalGrowth(projectedGrowth);
        } else {
    		Calendar srCal = Calendar.getInstance();
        	srCal.set(Calendar.MONTH,(srMonth-1));
        	srCal.set(Calendar.YEAR, srYear);
    		int totalPlanMonths = Globals.calculateDifferenceInMonths(beginCal,endCal,true);
    		int monthsIntoPlan = Globals.calculateDifferenceInMonths(beginCal,srCal,true);
    		int monthsLeft = totalPlanMonths - monthsIntoPlan;
    		double averageMonthlySales = (targetMarketProduct.getSalesInPlan() / (double) monthsIntoPlan);
    		targetMarketProduct.setTotalGrowth(targetMarketProduct.getSalesInPlan() + (averageMonthlySales * (double) monthsLeft));
    	}
    	
    	double payout = determinePayout(targetMarketProduct.getTotalGrowth(),targetMarketProduct.getGrowthPercentage(), targetMarket);
        targetMarketProduct.setPayout(payout);
    	
    }
        
    private ArrayList getTargetMarketPlanPriorApprovers (int targetMarketPlanId, Connection DBConn) throws Exception{
   	 
    	if(targetMarketPlanId != 0){
    		return WorkflowDAO.getTargetMarketPlanWorkflowPriorApprovers(targetMarketPlanId, DBConn);
    	}
    	else{
    		//throw new Exception("targetMarketPlanId is 0");
    		
    		//just clicking on the TargetMarket tab means that there is no target market id, so your first line will produce a 0. 
    		//The getTargetMarketPlanPriorApprovers method wrongly returns an exception if the tmid is a 0. If no specific plan is selected, 
    		//then you don't need any prior approvers, so you'll have to either handle null values or send in an empty ArrayList.
    		return new ArrayList();
    	}
    }
    
    private void updateTargetMarketStatus (int targetMarketPlanId, String targetMarketStatus, Connection DBConn) throws Exception{
    	
    	if(targetMarketPlanId != 0){
    		TargetMarketDAO.updateTargetMarketStatus(targetMarketPlanId, targetMarketStatus, DBConn);
    	}
    	else{
    		throw new Exception("targetMarketPlanId is 0");
    	}
    }

} //class
