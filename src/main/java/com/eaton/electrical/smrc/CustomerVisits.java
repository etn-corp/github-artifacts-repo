// Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.25  2005/03/01 15:36:23  vendejp
// Fixed problem with multiple cc users
//
// Revision 1.24  2005/01/12 04:50:43  schweks
// Changed to use Globals.a2int instead.
//
// Revision 1.23  2005/01/10 03:00:24  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.22  2005/01/09 05:59:55  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.21  2005/01/05 22:40:25  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.20  2005/01/05 21:01:53  vendejp
// Changes for log4j logging and exception handling
//
// Revision 1.19  2004/12/23 18:12:50  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.18  2004/12/07 21:07:20  vendejp
// *** empty log message ***
//
// Revision 1.17  2004/11/09 06:10:43  vendejp
// *** empty log message ***
//
// Revision 1.16  2004/11/08 14:15:00  lubbejd
// remove System out println.
//
// Revision 1.15  2004/11/05 18:43:13  lubbejd
// Moved methods out of OEMAcctPlan to DAOs. Beginning of changes for adding tasks to customer visits.
//
// Revision 1.14  2004/10/30 22:52:43  vendejp
// Changes to accomodate header requirements.  Made User and Account objects set in the header object where I could.
//
// Revision 1.13  2004/10/28 20:50:10  vendejp
// added the "add contact" functionality
//
// Revision 1.12  2004/10/19 14:51:03  schweks
// Removing unused variables and code.
//
// Revision 1.11  2004/10/16 18:14:59  manupvj
// Added abstract servlet for common functions and common point of logging.
//
// Revision 1.10  2004/10/14 17:40:51  schweks
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

public class CustomerVisits extends SMRCBaseServlet {

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
            
            String acctId = request.getParameter("acctId");
			Account acct = null;
			if(acctId!=null){
				acct = AccountDAO.getAccount(acctId, DBConn);
			}

            SMRCHeaderBean hdr = new SMRCHeaderBean();
			hdr.setAccount(acct);
            hdr.setUser(user);
            
            request.setAttribute("header", hdr);
            request.setAttribute("fwdPage", request.getParameter("fwdPage"));

            String page = StringManipulation.noNull(request.getParameter("page"));

            CustomerVisit visit = new CustomerVisit();
            ArrayList tasks = new ArrayList();

            if (page.equalsIgnoreCase("report")) {
                request.setAttribute("visits", CustomerVisitsDAO.searchVisits(request,user, DBConn));
                sFwdUrl = "/customerVisitsSearchResults.jsp";
            } else {
            	
            	if (page.equalsIgnoreCase("deleteTask") && request.getParameter("deleteTask") != null){
            		int deleteTaskId = Globals.a2int(request.getParameter("deleteTask"));
            		if (deleteTaskId > 0){
            			AccountDAO.deleteTask(deleteTaskId, DBConn);
            		}
            		page = "refresh";
            	}

                if (page.equalsIgnoreCase("save") || page.equalsIgnoreCase("refresh")) {
                    CustomerVisit saveVisit = new CustomerVisit();

                    saveVisit.setId(Globals.a2int(request.getParameter("visitId")));
                    saveVisit.setDescription(StringManipulation.noNull(request.getParameter("VISIT_DESCRIPTION")));
                    saveVisit.setUsers(request.getParameterValues("VISIT_EMPLOYEES"));
                    saveVisit.setContacts(request.getParameterValues("VISIT_CONTACTS"));
                    saveVisit.setOutcomeId(Globals.a2int(request.getParameter("VISIT_OUTCOME_TYPE_ID")));
                    saveVisit.setReasonId(Globals.a2int(request.getParameter("VISIT_REASON_TYPE_ID")));
                    saveVisit.setNotes(StringManipulation.noNull(request.getParameter("VISIT_NOTES")));
                    String visitMon = StringManipulation.noNull(request.getParameter("VISIT_DATE_MON"));
                    String visitDay = StringManipulation.noNull(request.getParameter("VISIT_DATE_DAY"));
                    String visitYear = StringManipulation.noNull(request.getParameter("VISIT_DATE_YEAR"));
                    Date visitDate = Globals.getDate(visitYear, visitMon,visitDay);
                    String nextVisitMon = StringManipulation.noNull(request.getParameter("NEXT_VISIT_DATE_MON"));
                    String nextVisitDay = StringManipulation.noNull(request.getParameter("NEXT_VISIT_DATE_DAY"));
                    String nextVisitYear = StringManipulation.noNull(request.getParameter("NEXT_VISIT_DATE_YEAR"));
                    Date nextVisitDate = Globals.getDate(nextVisitYear,nextVisitMon, nextVisitDay);
                    
                    saveVisit.setVisitDate(visitDate);
                    saveVisit.setNextVisitDate(nextVisitDate);
                    saveVisit.setVcn(acctId);
                    saveVisit.setUserIdChanged(user.getUserid());

                    if(page.equalsIgnoreCase("refresh")){
                    	visit = saveVisit;
                    }else{
                        int newVisitId = CustomerVisitsDAO.saveVisit(saveVisit,DBConn);
                        visit = CustomerVisitsDAO.getOneVisit(newVisitId, DBConn);
                    }
                    
                } else if (request.getParameter("visitId") != null && !request.getParameter("visitId").equals("")) {
                    visit = CustomerVisitsDAO.getOneVisit(Globals.a2int(request.getParameter("visitId")), DBConn);
                }

                if (visit.getId() > 0){
                	ArrayList temptasks = AccountDAO.getCustTasks(DBConn, acct.getVcn(), visit.getId());
                	for (int i = 0; i < temptasks.size(); i++) {
                        PurchaseActionProgram task = (PurchaseActionProgram) temptasks.get(i);
                        User taskuser = UserDAO.getUser(task.getAssignedTo(), DBConn);
                        task.setAssignedUser(taskuser);
                        ArrayList ccUserIds = task.getCCEmail();
                        for (int j = 0; j < ccUserIds.size(); j++) {
                        	taskuser = UserDAO.getUser((String) ccUserIds.get(j), DBConn);
                            task.addCcUser(taskuser);
                        }

                        tasks.add(task);
                    }
                    
                }
                request.setAttribute("tasks", tasks);
                request.setAttribute("selectedUsers", CustomerVisitsDAO.getUsers(visit,DBConn));
                request.setAttribute("selectedContacts", CustomerVisitsDAO.getContacts(visit, DBConn));           
                request.setAttribute("contacts", ContactsDAO.getContacts(acctId, DBConn));
                request.setAttribute("allUsersTm", UserDAO.getAllUsersTreeMap(DBConn));
                request.setAttribute("allVisits", CustomerVisitsDAO.getVisits(acctId,DBConn));
                request.setAttribute("visitOutcomes", MiscDAO.getCodes("cust_visit_outcome", DBConn));
                request.setAttribute("visitReasons", MiscDAO.getCodes("cust_visit_reason", DBConn));
                request.setAttribute("visit", visit);

                
                sFwdUrl = "/customerVisitLog.jsp";

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