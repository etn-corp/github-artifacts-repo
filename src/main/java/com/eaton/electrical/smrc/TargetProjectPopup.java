//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.41  2006/03/16 19:13:32  e0073445
// Removed code that was producing warnings.
//
// Revision 1.40  2005/09/06 16:59:24  lubbejd
// Changed chValue and totalValue fields to doubles instead of Strings, and added length check in isNumber(), to prevent SQL Exceptions occurring when users remove 0.0 from these fields and try to save spaces.
//
// Revision 1.39  2005/05/31 19:14:31  lubbejd
// More changes for CR30585 (Change to target project approval process).
//
// Revision 1.38  2005/05/31 15:25:09  lubbejd
// More changes for CR30585 (Change to target project approval process).
//
// Revision 1.37  2005/05/25 19:25:53  lubbejd
// More changes for CR30585 (Change to target project approval process).
//
// Revision 1.36  2005/05/24 11:45:39  lubbejd
// Removed local getProjectBOM methods and replaced with reference to
// ProjectDAO.getProjectBOM().
//
// Revision 1.35  2005/03/07 19:05:59  vendejp
// Changes so user profile has the "View Profiles" ability for helpdesk
//
// Revision 1.34  2005/02/11 19:07:27  lubbejd
// Remove calls to MiscDAO.saveAPLog() from everywhere, and added the call
// to SMRCBaseServlet to update with every forward to a jsp
//
// Revision 1.33  2005/02/10 18:43:11  lubbejd
// Changed MiscDAO.saveAPLog() to use User object instead of userid string
// so it can call UserDAO.getGeogForUser for storing the sp_geog on the activity
// log.
//
// Revision 1.32  2005/01/21 16:45:53  vendejp
// added null check in fixQuotes()
//
// Revision 1.31  2005/01/12 04:50:43  schweks
// Changed to use Globals.a2int instead.
//
// Revision 1.30  2005/01/10 03:00:23  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.29  2005/01/09 05:59:54  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.28  2005/01/05 22:40:24  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.27  2004/12/23 18:12:50  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.26  2004/12/15 18:07:43  vendejp
// Fixed some bugs and added segment overrides everywhere.
//
// Revision 1.25  2004/12/09 15:28:38  vendejp
// Changes to the way the User object operates.  Have to explicitly check for security override now.
//
// Revision 1.24  2004/11/11 22:41:26  schweks
// getProducts private method was changed to go to ProductDAO
//
// Revision 1.23  2004/11/08 22:12:59  vendejp
// *** empty log message ***
//
// Revision 1.22  2004/11/05 17:29:04  vendejp
// changed queries to not use sales_id from the USER table
//
// Revision 1.21  2004/10/22 17:01:45  vendejp
// removed hideFromAssignments stuff
//
// Revision 1.20  2004/10/19 15:11:25  schweks
// Removing unused variables and code.
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
 * This displays the users associated with a given customer
 * 
 * @author Carl Abel
 * @date March 12, 2003
 *  
 */
public class TargetProjectPopup extends SMRCBaseServlet {

    // JPV: All of these codes should be part of a DAO, but just in
    // order to put this change from TAP into SMRC, I am copying
    // the code in the appropriate places; we'll have to
    // clean up later...
	private static final long serialVersionUID = 100;

	private static final String END_CUSTOMER_CODE = "C";

    private static final String DISTRIBUTOR_CODE = "D";

    private static final String ELECTRICAL_CONTRACTOR_CODE = "E";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean redirect = false;
        Connection DBConn = null;
        String sFwdUrl = "/SMRCErrorPage.jsp";
        User user = null;
        
        try {

            DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();

            user = SMRCSession.getUser(request, DBConn);

// The following code was removed because all values are never used            
//            String userid = user.getUserid();

//            String page = "targetprojectpopup";
//            if (request.getParameter("printLayout") != null) {
//                page = "targprojprint";
//            }

            sFwdUrl = printPage(request, DBConn);
			// Now if something goes wrong and a page is not returned, go to the
			// error page
			if (sFwdUrl.equals("")) {
				throw new Exception(
						"Incorrect parameters.  Forward to page not found.");
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

    private String printPage(HttpServletRequest request, Connection aConnection) throws java.io.IOException, Exception {
        
    	String returnVal = "";
    	
        String page = "targetprojectpopup";
        if (request.getParameter("printLayout") != null) {
            page = "targprojprint";
        }

        User user = SMRCSession.getUser(request, aConnection);
        String userid = user.getUserid();
                
        //		JDL - NEW HEADER CODE
        HeaderBean hdr = new HeaderBean();
        hdr.setPage(page);
        hdr.setHelpPage(TAPcommon.getHelpPage(page, aConnection));

        ArrayList tmp = new ArrayList(1);
        hdr.setGroups(tmp);
        hdr.setUser(user);
        hdr.setPopup(false); // Changed before released but after code was
                             // finished.

        if (request.getParameter("cust") != null) {
            hdr.setForceCustLinks(true);
        }

        Customer cust = getCustomer(aConnection, request.getParameter("cust"));
        hdr.setCustomer(cust);
        hdr.setAccount(AccountDAO.getAccount(request.getParameter("cust"), aConnection));
        
        TargetProject proj = new TargetProject();
        String msg = "";

        if (request.getParameter("projId") != null) {
            String projId = request.getParameter("projId");

            if (request.getParameter("option") != null) {
//                TargetProject oldTP = getTargetProject(aConnection, projId);
                TargetProject oldTP = ProjectDAO.getTargetProject(aConnection,projId);

                String option = request.getParameter("option");
                boolean error = false;

                if (option.equals("removeDist")) {
                    String vcn = request.getParameter("dist");
                    error = removeDist(aConnection,projId, vcn);
                } else if (option.equals("removeCust")) {
                    String vcn = request.getParameter("cust");
                    error = removeCust(aConnection, projId, vcn);
                } else if (option.equals("removeEC")) {
                    String vcn = request.getParameter("ec");
                    error = removeEC(aConnection,projId, vcn);
                } else if (option.equals("save")) {
                    if (request.getParameter("apprDel") != null
                            && (request.getParameter("apprDel").equals("A") || 
                                request.getParameter("apprDel").equals("D"))) {
                        error = approveDelete(request, aConnection, user);
                    } else {
                        error = updateProject(request, aConnection, userid);
                    }

                    if (request.getParameter("projId") == null ||
                        request.getParameter("projId").equals("0")) {
                        
                        String sel = "select target_project_seq.currVal from dual";
                        Statement stmt = null;
                        ResultSet rs = null;
                        try {
                            stmt = aConnection.createStatement();
                            rs = stmt.executeQuery(sel);

                            if (rs.next()) {
                                projId = rs.getString(1);
                            }

                        } catch (Exception e) {
                            SMRCLogger.error(this.getClass().getName()
                                    + ".printPage() ", e);
                            throw e;
                        } finally {
                            SMRCConnectionPoolUtils.close(rs);
                            SMRCConnectionPoolUtils.close(stmt);
                        }
                    }
                }

//                TargetProject newTP = getTargetProject(aConnection,projId);
                TargetProject newTP = ProjectDAO.getTargetProject(aConnection, projId);

                if (!error) {
                    notifyUsersOfChange(projId, user, oldTP, newTP);
                    msg = "Save Successful";
                } else {
                    msg = "An error occured while making your changes. Please contact IT Support.";
                }
            }

//            proj = getTargetProject(aConnection,projId);
            proj = ProjectDAO.getTargetProject(aConnection,projId);
        }

        ArrayList statuses = getStatuses(aConnection);
        ArrayList reasons = getReasons(aConnection);
        ArrayList preferences = getPreferences(aConnection);
        ArrayList vendors = getVendors(aConnection);
        ArrayList cops = getChangeOrderPotentials(aConnection);
        boolean viewOnly = false;

        viewOnly = page.equals("targprojprint") || proj.deleted();

        // force those not allowed to update to view only
        if (!viewOnly) {
            if (request.getParameter("projId") != null) {
                if (!(user.ableToSetSecurity() || 
                      user.ableToUpdate(proj.getSPGeog())|| 
                      user.getUserid().equals(proj.getUserAdded()) ||
                	  allowedToApproveTargetProject(aConnection,user,proj))) {
                    viewOnly = true;
                }
            }
        }

        //		JDL - NEW CODE

        TreeMap statusDesc = new TreeMap();
        for (int i = 0; i < statuses.size(); i++) {
            ProjectStatus stat = (ProjectStatus) statuses.get(i);
            statusDesc.put(new Integer(stat.getId()), stat.getDescription());
        }

        TreeMap reasonDesc = new TreeMap();
        for (int i = 0; i < reasons.size(); i++) {
            TargetReason reason = (TargetReason) reasons.get(i);
            reasonDesc.put(new Integer(reason.getId()), reason.getDescription());
        }

        TreeMap preferenceDesc = new TreeMap();
        for (int i = 0; i < preferences.size(); i++) {
            SpecPreference pref = (SpecPreference) preferences.get(i);
            preferenceDesc.put(new Integer(pref.getId()), pref.getDescription());
        }

        ArrayList distribs = getProjectDistributors(aConnection, proj.getId());
        TreeMap distMap = new TreeMap();
        for (int i = 0; i < distribs.size(); i++) {
            String vcn = (String) distribs.get(i);
            distMap.put(vcn, AccountDAO.getAccountName(vcn, aConnection));
        }

        ArrayList contractors = getProjectContractors(aConnection, proj.getId());
        TreeMap contMap = new TreeMap();
        for (int i = 0; i < contractors.size(); i++) {
            String vcn = (String) contractors.get(i);
            contMap.put(vcn, AccountDAO.getAccountName(vcn, aConnection));
        }

        String customer = getProjectCustomer(aConnection, proj.getId());
        TreeMap custMap = new TreeMap();
        custMap.put(customer, AccountDAO.getAccountName(customer, aConnection));

        if (!proj.getNegNum().equals("")) {
            Bid bid = getBid(aConnection, proj.getNegNum());
            TreeMap bidMap = new TreeMap();
            bidMap.put(bid, SalesDAO.getSalesmanName(bid.getSalesId(), aConnection));
            request.setAttribute("bid", bidMap);
        }

        ArrayList products = ProductDAO.getProductsBySequence( aConnection );
        ArrayList bom = getTPBOM(aConnection,proj.getId());
        ArrayList tpVens = getTPVendors(aConnection, proj.getId());

        request.setAttribute("geogName", 
                MiscDAO.getGeography(proj.getSPGeog(), aConnection).getDescription());
        request.setAttribute("statusDescriptions", statusDesc);
        request.setAttribute("reasonDescriptions", reasonDesc);
        request.setAttribute("preferenceDescriptions", preferenceDesc);
        request.setAttribute("distributors", distMap);
        request.setAttribute("contractors", contMap);
        request.setAttribute("customers", custMap);
        request.setAttribute("users", getUsers(aConnection));
        request.setAttribute("memberTypes", getMemberTypes(aConnection));
        request.setAttribute("members", 
                getProjectMembers(aConnection, proj.getId()));
        request.setAttribute("addedBy", 
                UserDAO.getUser(proj.getUserAdded(), aConnection));
        request.setAttribute("message", msg);
        request.setAttribute("header", hdr);
        request.setAttribute("project", proj);

        if (viewOnly) {
        	returnVal = "/TargetProjectPopupViewOnly.jsp";
            TreeMap prod_id_desc = new TreeMap();
            for (int i = 0; i < products.size(); i++) {
                Product p = (Product) products.get(i);
                for (int j = 0; j < bom.size(); j++) {
                    if (((String) bom.get(j)).equals(p.getId())) {
                        prod_id_desc.put(p.getId(), p.getDescription());
                    }
                }
            }
            request.setAttribute("product_hashtable", prod_id_desc);
            TreeMap vendorDesc = new TreeMap();
            for (int i = 0; i < vendors.size(); i++) {
                Vendor v = (Vendor) vendors.get(i);
                for (int j = 0; j < tpVens.size(); j++) {
                    if (Integer.parseInt((String) tpVens.get(j)) == v.getId()) {
                        vendorDesc.put(new Integer(v.getId()), v.getDescription());
                    }
                }
            }
            request.setAttribute("vendorDescriptions", vendorDesc);

        } else {
            boolean canApprove = allowedToApproveTargetProject(aConnection, user, proj)
                    && !proj.isActive() && !proj.deleted();
            boolean canDelete = (canApprove || user.getUserid().equals(
                    proj.getUserAdded()))
                    && !proj.deleted();
            request.setAttribute("canApprove", new Boolean(canApprove));
            request.setAttribute("canDelete", new Boolean(canDelete));
            ArrayList districts = getDistricts(aConnection);
            request.setAttribute("districts", districts);
            request.setAttribute("allProducts", products);
            request.setAttribute("bom", bom);
            request.setAttribute("cops", cops);
            request.setAttribute("vendors", vendors);
            request.setAttribute("tpVens", tpVens);

            returnVal = "/TargetProjectPopupUpdate.jsp";
        }
        
        return returnVal;

    }

    //	JDL - END NEW CODE

    private ArrayList getProjectMembers(Connection aConnection, int id) throws Exception {
        ArrayList members = new ArrayList(15);

        String sel = "select * from project_user_xref where target_project_id = " + id;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                ProjectMember pm = new ProjectMember();

                pm.setUserid(rs.getString("userid"));
                pm.setProjectId(id);
                pm.setMemberType(rs.getString("user_type"));

                members.add(pm);
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".getProjectMembers() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return members;
    }

    private ArrayList getMemberTypes(Connection aConnection) throws Exception {
        ArrayList mt = new ArrayList(5);

        String sel = "select * from project_user_types where description is not null order by description";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                ProjectMemberType pmt = new ProjectMemberType();

                pmt.setId(rs.getString("user_type"));
                pmt.setDescription(rs.getString("description"));

                mt.add(pmt);
            }

        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getMemberTypes() ",
                    e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return mt;
    }

    private boolean updateProject(HttpServletRequest request, Connection aConnection, String userid)
            throws Exception {
        boolean err = false;
        //String userId = request.getParameter("userid");
        String geog = "";
        if (request.getParameter("spGeog") != null) {
            geog = request.getParameter("spGeog");
        }
        String jobName = "";
        if (request.getParameter("jobName") != null) {
            jobName = request.getParameter("jobName");
        }
        String consultant = "";
        if (request.getParameter("consultant") != null) {
            consultant = request.getParameter("consultant");
        }
        String[] bom = new String[0];
        if (request.getParameter("bom") != null) {
            bom = request.getParameterValues("bom");
        }
        String cop = "";
        if (request.getParameter("copId") != null) {
            cop = request.getParameter("copId");
        }
        String gc = "";
        if (request.getParameter("gc") != null) {
            gc = request.getParameter("gc");
        }
        double chValue = 0;
        if (request.getParameter("chValue") != null) {
        	if (isNumber(request.getParameter("chValue"))){
        		chValue = (new Double(request.getParameter("chValue"))).doubleValue();
      	  	}
        }
        double totalValue = 0;
        if (request.getParameter("totalValue") != null) {
        	  if (isNumber(request.getParameter("totalValue"))){
        		  totalValue = (new Double(request.getParameter("totalValue"))).doubleValue();
        	  }
        }
        String chPos = "";
        if (request.getParameter("chPos") != null) {
            chPos = request.getParameter("chPos");
        }
        String bidDate = "";
        if (request.getParameter("bidDate") != null) {
            bidDate = formatDate(request.getParameter("bidDate"));
        }
        String status = "1";
        if (request.getParameter("status") != null) {
            status = request.getParameter("status");
        }
        String reason = "1";
        if (request.getParameter("reason") != null) {
            reason = request.getParameter("reason");
        }
        String preference = "1";
        if (request.getParameter("preference") != null) {
            preference = request.getParameter("preference");
        }
        String statusNotes = "";
        if (request.getParameter("statusNotes") != null) {
            statusNotes = request.getParameter("statusNotes");
        }
        String srNotes = "";
        if (request.getParameter("stratReasonNotes") != null) {
            srNotes = request.getParameter("stratReasonNotes");
        }
        String prefNotes = "";
        if (request.getParameter("prefNotes") != null) {
            prefNotes = request.getParameter("prefNotes");
        }
        String negNum = "";
        if (request.getParameter("negNum") != null) {
            negNum = request.getParameter("negNum");
        }
        String[] vendors = new String[0];
        if (request.getParameter("vendor") != null) {
            vendors = request.getParameterValues("vendor");
        }

        String upd = "";

        if (request.getParameter("projId") != null &&
                !request.getParameter("projId").equals("0")) {
            String bidDtClause = "";
            if (bidDate.equals("")) {
                bidDtClause = "bid_date = null, ";
            } else {
                bidDtClause = "bid_date = to_date('" + bidDate
                        + "','MM/DD/YYYY'), ";
            }
            String copClause = "";
            if (cop.equals("")) {
                copClause = "cop_id = null, ";
            } else {
                copClause = "cop_id = " + cop + ", ";
            }

            upd = "update target_projects " + "set sp_geog = '" + geog + "', "
                    + "job_name = '" + fixQuotes(jobName) + "', "
                    + "consultant = '" + fixQuotes(consultant) + "', "
                    + "ch_value = " + chValue + ", " + "total_value = "
                    + totalValue + ", " + bidDtClause + copClause
                    + "general_contractors = '" + fixQuotes(gc) + "', "
                    + "ch_position_with_contractor = '" + fixQuotes(chPos)
                    + "', " + "status_id = " + status + ", "
                    + "preference_id = " + preference + ", "
                    + "strat_reason_id = " + reason + ", " + "status_notes = '"
                    + fixQuotes(statusNotes) + "', " + "preference_notes = '"
                    + fixQuotes(prefNotes) + "', " + "strat_reason_notes = '"
                    + fixQuotes(srNotes) + "', " + "neg_num = '" + negNum
                    + "', " + "date_changed = sysdate "
                    + "where target_project_id = " + request.getParameter("projId");
        } else {
            String bidDtField = "";
            String bidDtVal = "";
            if (!bidDate.equals("")) {
                bidDtField = "bid_date,";
                bidDtVal = "to_date('" + bidDate + "','MM/DD/YYYY'), ";
            }

            if (cop.equals("")) {
                upd = "insert into target_projects "
                        + "(target_project_id,sp_geog, job_name, consultant, ch_value, total_value, "
                        + bidDtField
                        + "general_contractors, ch_position_with_contractor, "
                        + "status_id, preference_id, strat_reason_id, status_notes, preference_notes, "
                        + "strat_reason_notes, neg_num, notes, user_added, date_added, date_changed,internal_status) "
                        + "values (target_project_seq.nextVal,'" + geog + "','"
                        + fixQuotes(jobName) + "','" + fixQuotes(consultant)
                        + "'," + chValue + "," + totalValue + "," + bidDtVal
                        + "'" + fixQuotes(gc) + "','" + fixQuotes(chPos) + "',"
                        + status + "," + preference + "," + reason + ",'"
                        + fixQuotes(statusNotes) + "','" + fixQuotes(prefNotes)
                        + "','" + fixQuotes(srNotes) + "','" + negNum
                        + "',null,'" + userid + "',sysdate,sysdate,'N')";
            } else {
                upd = "insert into target_projects "
                        + "(target_project_id,sp_geog, job_name, consultant, ch_value, total_value, "
                        + bidDtField
                        + "cop_id, general_contractors, ch_position_with_contractor, "
                        + "status_id, preference_id, strat_reason_id, status_notes, preference_notes, "
                        + "strat_reason_notes, neg_num, notes, user_added, date_added, date_changed,internal_status) "
                        + "values (target_project_seq.nextVal,'" + geog + "','"
                        + fixQuotes(jobName) + "','" + fixQuotes(consultant)
                        + "'," + chValue + "," + totalValue + "," + bidDtVal
                        + cop + ",'" + fixQuotes(gc) + "','" + fixQuotes(chPos)
                        + "'," + status + "," + preference + "," + reason
                        + ",'" + fixQuotes(statusNotes) + "','"
                        + fixQuotes(prefNotes) + "','" + fixQuotes(srNotes)
                        + "','" + negNum + "',null,'" + userid
                        + "',sysdate,sysdate,'N')";
            }
        }
        Statement stmt = null;
        SMRCLogger.debug("TargetProjectPopup.updateProject() - SQL\n" + upd);
        try {
            stmt = aConnection.createStatement();
            stmt.executeUpdate(upd);

        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".updateProject() ", e);
            SMRCLogger.error("TargetProjectPopup SQL:: " + upd);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(stmt);
        }

        String projId = "";

        if (request.getParameter("projId") == null || 
                request.getParameter("projId").equals("0")) {
            
            String sel = "select target_project_seq.currVal from dual";
            ResultSet rs = null;
            try {
                stmt = aConnection.createStatement();
                rs = stmt.executeQuery(sel);

                if (rs.next()) {
                    projId = rs.getString(1);
                }

            } catch (Exception e) {
                SMRCLogger.error(this.getClass().getName()
                        + ".updateProject() ", e);
                throw e;
            } finally {
                SMRCConnectionPoolUtils.close(rs);
                SMRCConnectionPoolUtils.close(stmt);
            }
        } else {
            projId = request.getParameter("projId");
        }

        if (!err) {
            if (request.getParameter("distVCN") != null) {
                String[] distVCN = request.getParameterValues("distVCN");

                for (int i = 0; i < distVCN.length; i++) {
                    if (distVCN[i] != null && !distVCN[i].equals("")) {
                        String ins = "insert into customer_project_xref "
                                + "values (" + projId + ",'" + distVCN[i]
                                + "','D')";

                        try {
                            stmt = aConnection.createStatement();
                            stmt.executeUpdate(ins);

                        } catch (Exception e) {
                            SMRCLogger.error(this.getClass().getName()
                                    + ".updateProject() ", e);
                            throw e;
                        } finally {
                            SMRCConnectionPoolUtils.close(stmt);
                        }
                    }
                }
            }
        }

        if (!err) {
            if (request.getParameter("ecVCN") != null) {
                String[] ecVCN = request.getParameterValues("ecVCN");

                for (int i = 0; i < ecVCN.length; i++) {
                    if (ecVCN[i] != null && !ecVCN[i].equals("")) {
                        String ins = "insert into customer_project_xref "
                                + "values (" + projId + ",'" + ecVCN[i]
                                + "','E')";

                        try {
                            stmt = aConnection.createStatement();
                            stmt.executeUpdate(ins);

                            stmt.close();
                        } catch (Exception e) {
                            SMRCLogger.error(this.getClass().getName()
                                    + ".updateProject() ", e);
                            throw e;
                        } finally {
                            SMRCConnectionPoolUtils.close(stmt);
                        }
                    }
                }
            }
        }

        if (!err) {
            if (request.getParameter("custVCN") != null
                    && !request.getParameter("custVCN").equals("")) {
                String custVCN = request.getParameter("custVCN");

                String ins = "insert into customer_project_xref " + "values ("
                        + projId + ",'" + custVCN + "','C')";

                try {
                    stmt = aConnection.createStatement();
                    stmt.executeUpdate(ins);

                } catch (Exception e) {
                    SMRCLogger.error(this.getClass().getName()
                            + ".updateProject() ", e);
                    throw e;
                } finally {
                    SMRCConnectionPoolUtils.close(stmt);
                }
            }
        }

        if (!err) {
            ArrayList mt = getMemberTypes(aConnection);

            for (int i = 0; i < mt.size(); i++) {
                ProjectMemberType pmt = (ProjectMemberType) mt.get(i);

                // deletes all records so those de-selected are removed
                deleteProjectUserXref(aConnection, projId, pmt.getId());

                String[] users = request.getParameterValues("user_" + pmt.getId());

                if (users != null) {
                    for (int j = 0; j < users.length; j++) {
                        String ins = "insert into project_user_xref values "
                                + "('" + users[j] + "'," + projId + ",'"
                                + pmt.getId() + "')";

                        try {
                            stmt = aConnection.createStatement();
                            stmt.executeUpdate(ins);

                        } catch (Exception e) {
                            SMRCLogger.error(this.getClass().getName()
                                    + ".updateProject() ", e);
                            throw e;
                        } finally {
                            SMRCConnectionPoolUtils.close(stmt);
                        }
                    }
                }
            }
        }

        if (bom.length > 0 && !err) {
            err = updateProjectBOM(aConnection,projId, bom);
        }

        if (vendors.length > 0 && !err) {
            err = updateProjectVendors(aConnection,projId, vendors);
        }

        return err;
    }

    private boolean updateProjectBOM(Connection aConnection,String projId, String[] bom)
            throws Exception {
        String del = "delete from target_project_bom where target_project_id = "
                + projId;
        Statement stmt = null;
        try {
            stmt = aConnection.createStatement();
            stmt.executeUpdate(del);
        } catch (Exception e) {
            SMRCLogger.error(
                    this.getClass().getName() + ".updateProjectBOM() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(stmt);
        }

        for (int i = 0; i < bom.length; i++) {
            String ins = "insert into target_project_bom values (" + projId
                    + ",'" + bom[i] + "')";

            try {
                stmt = aConnection.createStatement();
                stmt.executeUpdate(ins);

            } catch (Exception e) {
                SMRCLogger.error(this.getClass().getName()
                        + ".updateProjectBOM() ", e);
                throw e;
            } finally {
                SMRCConnectionPoolUtils.close(stmt);
            }
        }

        return false;
    }

    private boolean updateProjectVendors(Connection aConnection,String projId, String[] vendors)
            throws Exception {
        String del = "delete from target_project_vendors where target_project_id = "
                + projId;
        Statement stmt = null;
        try {
            stmt = aConnection.createStatement();
            stmt.executeUpdate(del);

        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".updateProjectVendors() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(stmt);
        }

        for (int i = 0; i < vendors.length; i++) {
            String ins = "insert into target_project_vendors values (" + projId
                    + "," + vendors[i] + ")";

            try {
                stmt = aConnection.createStatement();
                stmt.executeUpdate(ins);

            } catch (Exception e) {
                SMRCLogger.error(this.getClass().getName()
                        + ".updateProjectVendors() ", e);
                throw e;
            } finally {
                SMRCConnectionPoolUtils.close(stmt);
            }
        }

        return false;
    }

    private void deleteProjectUserXref(Connection aConnection,String projId, String memberType)
            throws Exception {
        String del = "delete from project_user_xref "
                + "where target_project_id = " + projId + " and user_type = '"
                + memberType + "'";
        Statement stmt = null;
        try {
            stmt = aConnection.createStatement();
            stmt.executeUpdate(del);
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".deleteProjectUserXref() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(stmt);
        }
    }

    private ArrayList getTPVendors(Connection aConnection,int tpId) throws Exception {
        ArrayList venIds = new ArrayList(5);

        String sel = "select * from target_project_vendors where target_project_id = "
                + tpId;

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                if (rs.getString("vendor_id") != null)
                    venIds.add(rs.getString("vendor_id"));
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getTPVendors() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return venIds;
    }

    private ArrayList getTPBOM(Connection aConnection,int tpId) throws Exception {
        ArrayList bom = new ArrayList(5);

        String sel = "select * from target_project_bom where target_project_id = "
                + tpId;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                if (rs.getString("product_id") != null)
                    bom.add(rs.getString("product_id"));
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getTPBOM() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return bom;
    }
/* 
    private TargetProject getTargetProject(Connection aConnection, String id) throws Exception {
        TargetProject tp = new TargetProject();

        String sel = "select * from target_projects where target_project_id = "
                + id;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                 
                tp.setId(rs.getInt("target_project_id"));

                if (rs.getString("sp_geog") != null) {
                    tp.setSPGeog(rs.getString("sp_geog"));
                }

                if (rs.getString("job_name") != null) {
                    tp.setJobName(rs.getString("job_name"));
                }

                if (rs.getString("consultant") != null) {
                    tp.setConsultant(rs.getString("consultant"));
                }

                if (rs.getString("ch_value") != null) {
                    tp.setCHValue(rs.getDouble("ch_value"));
                }

                if (rs.getString("total_value") != null) {
                    tp.setTotalValue(rs.getDouble("total_value"));
                }

                if (rs.getString("bid_date") != null) {
                    tp.setBidDate(rs.getDate("bid_date"));
                }

                if (rs.getString("cop_id") != null) {
                    tp.setChangeOrderPotential(TAPcommon.getChangeOrderPotential(rs.getString("cop_id"),
                            aConnection));
                }

                if (rs.getString("general_contractors") != null) {
                    tp.setGenContractors(rs.getString("general_contractors"));
                }

                if (rs.getString("electrical_contractors") != null) {
                    tp.setElecContractors(rs.getString("electrical_contractors"));
                }

                if (rs.getString("ch_position_with_contractor") != null) {
                    tp.setCHPosition(rs.getString("ch_position_with_contractor"));
                }

                if (rs.getString("status_id") != null) {
                    tp.setStatus(TAPcommon.getProjectStatus(rs.getInt("status_id")
                            + "", aConnection));
                }

                if (rs.getString("preference_id") != null) {
                    tp.setPreference(TAPcommon.getSpecPreference(rs.getInt("preference_id")
                            + "", aConnection));
                }

                if (rs.getString("strat_reason_id") != null) {
                    tp.setReason(getTargetReason(aConnection, rs.getInt("strat_reason_id")));
                }

                if (rs.getString("status_notes") != null) {
                    tp.setStatusNotes(rs.getString("status_notes"));
                }

                if (rs.getString("preference_notes") != null) {
                    tp.setPreferenceNotes(rs.getString("preference_notes"));
                }

                if (rs.getString("strat_reason_notes") != null) {
                    tp.setStratReasonNotes(rs.getString("strat_reason_notes"));
                }

                if (rs.getString("neg_num") != null) {
                    tp.setNegNum(rs.getString("neg_num"));
                }

                if (rs.getString("notes") != null) {
                    tp.setNotes(rs.getString("notes"));
                }

                if (rs.getString("user_added") != null) {
                    tp.setUserAdded(rs.getString("user_added"));
                }

                if (rs.getString("date_added") != null) {
                    tp.setDateAdded(rs.getDate("date_added"));
                }

                if (rs.getString("date_changed") != null) {
                    tp.setDateChanged(rs.getDate("date_changed"));
                }

                if (rs.getString("internal_status") != null) {
                    tp.setInternalStatus(rs.getString("internal_status"));
                }

                if (rs.getString("dm_approval_id") != null) {
                    tp.setDMApprovalId(rs.getString("dm_approval_id"));
                }
                if (rs.getString("zm_approval_id") != null) {
                    tp.setZMApprovalId(rs.getString("zm_approval_id"));
                }
                if (rs.getString("mkt_mgr_approval_id") != null) {
                    tp.setMktMgrApprovalId(rs.getString("mkt_mgr_approval_id"));
                }
                if (rs.getString("dm_approval_date") != null) {
                    tp.setDMApprovalDate(rs.getDate("dm_approval_date"));
                }
                if (rs.getString("zm_approval_date") != null) {
                    tp.setZMApprovalDate(rs.getDate("zm_approval_date"));
                }
                if (rs.getString("mkt_mgr_approval_date") != null) {
                    tp.setMktMgrApprovalDate(rs.getDate("mkt_mgr_approval_date"));
                }
            }
        } catch (Exception e) {
            SMRCLogger.error(
                    this.getClass().getName() + ".getTargetProject() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        ArrayList vendors = getProjectVendors(aConnection, id);
        for (int i = 0; i < vendors.size(); i++) {
            Vendor v = (Vendor) vendors.get(i);
            tp.addVendor(v);
        }
        ArrayList bom = ProjectDAO.getProjectBOM(aConnection,id);
        for (int i = 0; i < bom.size(); i++) {
            Product p = (Product) bom.get(i);
            tp.addProduct(p);
        }

        sel = "select * from customer_project_xref "
                + "where target_project_id = " + id;

        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                String vcn = rs.getString("vista_customer_number");
                String name = AccountDAO.getAccountName(vcn, aConnection);
                String type = rs.getString("customer_type");

                Customer c = new Customer();
                c.setVistaCustNum(vcn);
                c.setName(name);

                tp.addCustomer(c, type);
            }

        } catch (Exception e) {
            SMRCLogger.error(
                    this.getClass().getName() + ".getTargetProject() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }
        return tp;
    }

    private ArrayList getProjectVendors(Connection aConnection,String id) throws Exception {
        ArrayList vendors = new ArrayList(5);

        String sel = "select v.vendor_id, v.description from target_project_vendors tpv, vendors v "
                + "where v.vendor_id = tpv.vendor_id "
                + "and tpv.target_project_id = " + id;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                Vendor v = new Vendor();

                v.setId(rs.getInt("vendor_id"));
                v.setDescription(rs.getString("description"));

                vendors.add(v);
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".getProjectVendors() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }
        return vendors;
    }
    */
/*
    private ArrayList getProjectBOM(Connection aConnection, String id) throws Exception {
        ArrayList bom = new ArrayList(5);

        String sel = "select p.product_id, p.product_description from target_project_bom tpb, products p "
                + "where p.product_id = tpb.product_id "
                + "and tpb.target_project_id = " + id;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                Product p = new Product();

                p.setId(rs.getString("product_id"));
                p.setDescription(rs.getString("product_description"));

                bom.add(p);
            }
        } catch (Exception e) {
            SMRCLogger
                    .error(this.getClass().getName() + ".getProjectBOM() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return bom;
    }
*/
    private ArrayList getChangeOrderPotentials(Connection aConnection) throws Exception {
        ArrayList cops = new ArrayList(10);

        String sel = "select * from change_order_potentials";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                ChangeOrderPotential cop = new ChangeOrderPotential();
                cop.setId(rs.getInt("cop_id"));
                cop.setDescription(rs.getString("description"));

                cops.add(cop);
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".getChangeOrderProtentials() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }
        return cops;
    }

    private ArrayList getStatuses(Connection aConnection) throws Exception {
        ArrayList statuses = new ArrayList(10);

        String sel = "select * from project_statuses order by status_id";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                ProjectStatus stat = new ProjectStatus();

                stat.setId(rs.getInt("status_id"));
                stat.setDescription(rs.getString("description"));

                statuses.add(stat);
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getStatuses() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return statuses;
    }

    private ArrayList getReasons(Connection aConnection) throws Exception {
        ArrayList reasons = new ArrayList(10);

        String sel = "select * from target_reasons";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                TargetReason reas = new TargetReason();

                reas.setId(rs.getInt("strat_reason_id"));
                reas.setDescription(rs.getString("description"));

                reasons.add(reas);
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getReasons() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }
        return reasons;
    }

    private ArrayList getPreferences(Connection aConnection) throws Exception {
        ArrayList preferences = new ArrayList(10);

        String sel = "select * from ch_spec_preferences";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                SpecPreference pref = new SpecPreference();

                pref.setId(rs.getInt("preference_id"));
                pref.setDescription(rs.getString("description"));

                preferences.add(pref);
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getPreferences() ",
                    e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return preferences;
    }

    private ArrayList getVendors(Connection aConnection) throws Exception {
        ArrayList vendors = new ArrayList(10);

        String sel = "select * from vendors";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                Vendor ven = new Vendor();

                ven.setId(rs.getInt("vendor_id"));
                ven.setDescription(rs.getString("description"));

                vendors.add(ven);
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getVendors() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return vendors;
    }

    private ArrayList getDistricts(Connection aConnection) throws Exception {
        ArrayList districts = new ArrayList(50);

        String sel = "select sp_geog, description from geographies where sales_org = '1' and team is null and zone not like '6%' and district <> '0' order by description";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                Region g = new Region();

                g.setSPGeog(rs.getString("sp_geog"));
                g.setDescription(rs.getString("description"));

                districts.add(g);
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getDistricts() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return districts;
    }

    private Customer getCustomer(Connection aConnection, String vcn) throws Exception {
        Customer customer = new Customer();
        String sel = "SELECT * from customer where vista_customer_number = '" + vcn + "'";
        Statement stmt = null;
        ResultSet response = null;
        try {
            stmt = aConnection.createStatement();
            response = stmt.executeQuery(sel);

            while (response.next()) {
                customer = new Customer(response.getString("vista_customer_number"));

                if (response.getString("phone_number") != null) {
                    customer.setPhoneNumber(response.getString("phone_number"));
                }

                if (response.getString("sales_engineer1") != null) {
                    customer.setSalesId(response.getString("sales_engineer1"));
                }

                if (response.getString("duns_number") != null) {
                    customer.setDunsNum(response.getString("duns_number"));
                }

                if (response.getString("sic_code") != null) {
                    customer.setSICCode(response.getString("sic_code"));
                }

                if (response.getString("web_site") != null) {
                    customer.setWebSite(response.getString("web_site"));
                }

                if (response.getString("description") != null) {
                    customer.setDescription(response.getString("description"));
                }

                if (response.getString("customer_name") != null) {
                    customer.setName(response.getString("customer_name"));
                }

                if (response.getString("industry_other") != null) {
                    customer.setIndustryOther(response.getString("industry_other"));
                }

                customer.setStage(response.getInt("sp_stage_id"));
                customer.setNumStores(response.getInt("num_stores"));

                if (response.getString("parent_num") != null) {
                    customer.setParent(response.getString("parent_num"));
                }

                if (response.getString("sp_geog") != null) {
                    customer.setSPGeog(response.getString("sp_geog"));
                }
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getCustomer() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(response);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return customer;
    }

    private ArrayList getUsers(Connection aConnection) throws Exception {
        ArrayList users = new ArrayList(300);
        String sel = "SELECT * FROM users, user_groups, current_salesman_v, user_groups_users_xref ugx WHERE current_salesman_v.user_id(+)=users.vistaline_id AND user_groups.user_group_id(+) = users.user_group_id AND users.hide_from_assignments <> 'Y' and ugx.userid (+) = users.userid order by last_name, first_name";
        
        /*
         * TODO I changed this query but havent tested.  It should work
         * Also changed all the setters so that we have an almost complete user object
         * The code used to get sales ID from the user table, which we dont want
         */
        //String sel = "SELECT * FROM users "
        //        + "WHERE hide_from_assignments <> 'Y' "
        //        + "ORDER BY last_name, first_name";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                User user = new User();

				user.setUserid(StringManipulation.noNull(rs.getString("userid")));
				user.setFirstName(StringManipulation.noNull(rs.getString("first_name")));
				user.setLastName(StringManipulation.noNull(rs.getString("last_name")));
				user.setVistaId((StringManipulation.noNull(rs.getString("vistaline_id"))).toUpperCase());
				user.setEmailAddress(StringManipulation.noNull(rs.getString("email_address")));
				user.setSalesId((StringManipulation.noNull(rs.getString("salesman_id"))).toUpperCase());
				if (StringManipulation.noNull(rs.getString("override_security")).equals("Y")) {
					user.setOverrideSecurity(true);
				}
				if (StringManipulation.noNull(rs.getString("set_security")).equals("Y")) {
					user.setSetSecurity(true);
				}
				if (StringManipulation.noNull(rs.getString("view_profile")).equals("Y")) {
					user.setViewProfile(true);
				}				
				user.setViewEverything(StringManipulation.noNull(rs.getString("view_everything")));
				//user.setMarketingMgr(StringManipulation.noNull(rs.getString("force_pwd_change")));
				user.setGroupId(rs.getInt("user_group_id"));
				user.setUserGroup(StringManipulation.noNull(rs.getString("user_group_name")));
				if (rs.getString("sp_geog") != null){
				    user.setUserGroupGeog(rs.getString("sp_geog"));
				}
				if (rs.getString("division_id") != null){
				    user.setUserGroupDivisionId(rs.getString("division_id"));
				}
				user.setJobTitle(StringManipulation.noNull(rs.getString("hr_job_title")));
				user.setHomePage(StringManipulation.noNull(rs.getString("homepage")));
				if (StringManipulation.noNull(rs.getString("div_csf")).equals("Y")) {
					user.setAbleToSeeDivisionCSF(true);
				}
				if (StringManipulation.noNull(rs.getString("district_csf")).equals("Y")) {
					user.setAbleToSeeDistrictCSF(true);
				}
				user.setGeography(StringManipulation.noNull(rs.getString("sp_geog_cd")));
                user.setTitleTx(StringManipulation.noNull(rs.getString("title_tx")));
// Braffet : Removed with Tap dollars                user.setDollarTypeCode(rs.getInt("DEFAULT_DOLLAR_TYPE_ID"));
				
                users.add(user);
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getUsers() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return users;
    }

    private ArrayList getProjectDistributors(Connection aConnection,int id) throws Exception {
        ArrayList vcns = new ArrayList(10);

        String sel = "select vista_customer_number from customer_project_xref "
                + " where target_project_id = " + id + " and customer_type = '"
                + DISTRIBUTOR_CODE + "'";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                vcns.add(rs.getString("vista_customer_number"));
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".getProjectDistributors() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }
        return vcns;
    }

    private ArrayList getProjectContractors(Connection aConnection,int id) throws Exception {
        ArrayList vcns = new ArrayList(10);

        String sel = "select vista_customer_number from customer_project_xref "
                + " where target_project_id = " + id + " and customer_type = '"
                + ELECTRICAL_CONTRACTOR_CODE + "'";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                vcns.add(rs.getString("vista_customer_number"));
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".getProjectContractors() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return vcns;
    }

    private String getProjectCustomer(Connection aConnection, int id) throws Exception {
        String vcn = "";

        String sel = "select vista_customer_number from customer_project_xref "
                + " where target_project_id = " + id + " and customer_type = '"
                + END_CUSTOMER_CODE + "'";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            if (rs.next()) {
                vcn = rs.getString("vista_customer_number");
            }

        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".getProjectCustomer() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return vcn;
    }

    private boolean removeDist(Connection aConnection, String projId, String vcn) throws Exception {
        boolean err = false;
        String upd = "delete from customer_project_xref "
                + "where vista_customer_number = '" + vcn + "' "
                + "and customer_type = '" + DISTRIBUTOR_CODE
                + "' and target_project_id = " + projId;
        log(upd);
        Statement stmt = null;
        try {
            stmt = aConnection.createStatement();
            stmt.executeUpdate(upd);
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".removeDist() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(stmt);
        }

        return err;
    }

    private boolean removeCust(Connection aConnection, String projId, String vcn) throws Exception {
        boolean err = false;
        String upd = "delete from customer_project_xref "
                + "where vista_customer_number = '" + vcn + "' "
                + "and customer_type = '" + END_CUSTOMER_CODE
                + "' and target_project_id = " + projId;
        Statement stmt = null;
        try {
            stmt = aConnection.createStatement();
            stmt.executeUpdate(upd);
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".removeCust() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(stmt);
        }

        return err;
    }

    private boolean removeEC(Connection aConnection,String projId, String vcn) throws Exception {
        boolean err = false;
        String upd = "delete from customer_project_xref "
                + "where vista_customer_number = '" + vcn + "' "
                + "and customer_type = '" + ELECTRICAL_CONTRACTOR_CODE
                + "' and target_project_id = " + projId;
        Statement stmt = null;
        try {
            stmt = aConnection.createStatement();
            stmt.executeUpdate(upd);
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".removeEC() ", e);
            throw e;
        } finally {

            SMRCConnectionPoolUtils.close(stmt);
        }
        return err;
    }

    private Bid getBid(Connection aConnection,String neg) throws Exception {
        Bid bid = new Bid();
        String sel = "select * from bid_tracker where neg_num = '" + neg + "'";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                bid.setNegNum(rs.getString("neg_num"));

                if (rs.getString("job_name") != null) {
                    bid.setJobName(rs.getString("job_name"));
                }
                if (rs.getString("status") != null) {
                    bid.setStatus(rs.getString("status"));
                }
                if (rs.getString("sales_id") != null) {
                    bid.setSalesId(rs.getString("sales_id"));
                }
                if (rs.getString("bid_dollars") != null) {
                    bid.setBidDollars(rs.getDouble("bid_dollars"));
                }
                if (rs.getString("job_type") != null) {
                    bid.setJobType(getJobType(aConnection, rs.getString("job_type")));
                }
                if (rs.getString("order_date") != null) {
                    bid.setGODate(rs.getDate("order_date"));
                }
                if (rs.getString("go_num") != null) {
                    bid.setGONum(rs.getString("go_num"));
                }
                if (rs.getString("bid_date") != null) {
                    bid.setBidDate(rs.getDate("bid_date"));
                }
                if (rs.getString("sp_geog") != null) {
                    bid.setSPGeog(rs.getString("sp_geog"));
                }
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getBid() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }
        return bid;
    }

    private JobType getJobType(Connection aConnection, String id) throws Exception {
        JobType jt = new JobType();

        String sel = "select * from job_type where job_type = '" + id + "'";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                jt.setId(rs.getString("job_type"));
                jt.setDescription(rs.getString("description"));
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getJobType() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return jt;
    }

    private void notifyUsersOfChange(String projId, User user,
            TargetProject oldTP, TargetProject newTP) {
        TargetProjectUpdateEmail tpue = new TargetProjectUpdateEmail();
        tpue.setProjectId(projId);
        tpue.setUpdateUser(user);
        tpue.setOldProject(oldTP);
        tpue.setNewProject(newTP);

        tpue.run();
    }
/*
    private TargetReason getTargetReason(Connection aConnection, int id) throws Exception {
        TargetReason tr = new TargetReason();
        String sel = "select * from target_reasons where strat_reason_id = "
                + id;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            if (rs.next()) {
                tr.setId(rs.getInt("strat_reason_id"));
                tr.setDescription(rs.getString("description"));
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getTargetReason() ",
                    e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }
        return tr;
    }
*/
    private boolean allowedToApproveTargetProject(Connection aConnection, User user, TargetProject tp)
            throws Exception {
        if (user.ableToSetSecurity()) { // user is allowed to set security [admins
                                     // only]
            return true;
        }

        boolean ok = false;

        if (tp.waitingForDM()) {
            if (userIsDistrictManager(aConnection,user, tp.getSPGeog())) {
                ok = true;
            }
        } else if (tp.waitingForProjectSalesMgr()) {
   //         if (userIsZoneManager(aConnection, user, tp.getSPGeog())) {
            if ((user.isProjectSalesManager()) &&
                (user.getUserGroupGeog() != null) &&
                (user.getUserGroupGeog().substring(0,4).equalsIgnoreCase(tp.getSPGeog().substring(0,4)))){
                ok = true;
            }
        } else if (tp.waitingForChampsMgr()) {
            if (user.isCHAMPSManager() || user.hasOverrideSecurity()) {
                ok = true;
            }
        }

        return ok;
    }

    private boolean userIsDistrictManager(Connection aConnection, User user, String spGeog)
            throws Exception {
        boolean dm = false;

        String sel = "select first_nm, last_nm, user_id from current_salesman_v a,"
                + " (select salesman_id, max(start_dt) st_dt from current_salesman_v group by salesman_id) b"
                + " where sp_geog_cd = '"
                + spGeog
                + "'"
                + " and a.salesman_id = b.salesman_id"
                + " and a.start_dt = b.st_dt"
                + " and inactive_dt is null"
                + " and a.start_dt >= to_date('01-01-'||to_char(sysdate,'YYYY'),'MM-DD-YYYY')"
                + " and function_cd = 'DS'";

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                if (rs.getString("user_id") != null) {
                    if (rs.getString("user_id").equalsIgnoreCase(user.getVistaId())) {
                        dm = true;
                    }
                }
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".userIsDistrictManager() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return dm;
    }
/*
    private boolean userIsZoneManager(Connection aConnection, User user, String spGeog) throws Exception {
        boolean zm = false;

        if (spGeog.length() >= 4) {
            String sel = "select first_nm, last_nm, user_id from current_salesman_v a,"
                    + " (select salesman_id, max(start_dt) st_dt from current_salesman_v group by salesman_id) b"
                    + " where sp_geog_cd = '"
                    + spGeog.substring(0, 4)
                    + "0'"
                    + " and a.salesman_id = b.salesman_id"
                    + " and a.start_dt = b.st_dt"
                    + " and inactive_dt is null"
                    + " and a.start_dt >= to_date('01-01-'||to_char(sysdate,'YYYY'),'MM-DD-YYYY')"
                    + " and function_cd = 'ZN'";
            Statement stmt = null;
            ResultSet rs = null;
            try {
                stmt = aConnection.createStatement();
                rs = stmt.executeQuery(sel);

                while (rs.next()) {
                    if (rs.getString("user_id") != null) {
                        if (rs.getString("user_id").equalsIgnoreCase(
                                user.getVistaId())) {
                            zm = true;
                        }
                    }
                }
            } catch (Exception e) {
                SMRCLogger.error(this.getClass().getName()
                        + ".userIsZoneManager() ", e);
                throw e;
            } finally {
                SMRCConnectionPoolUtils.close(rs);
                SMRCConnectionPoolUtils.close(stmt);
            }
        }

        return zm;
    }
*/
    private boolean approveDelete(HttpServletRequest request, Connection aConnection, User user)
            throws Exception {
        boolean yn = false;

        String projId = request.getParameter("projId");

        if (request.getParameter("apprDel").equals("A")) {
            yn = approveProject(aConnection, projId, user);
        } else if (request.getParameter("apprDel").equals("D")) {
            yn = deleteProject(aConnection, projId);
        }

        return yn;
    }

    private boolean deleteProject(Connection aConnection, String projId) throws Exception {
        String upd = "update target_projects " + "set internal_status = 'D',"
                + "date_changed = sysdate " + "where target_project_id = "
                + projId;
        Statement stmt = null;
        try {
            stmt = aConnection.createStatement();
            stmt.executeUpdate(upd);
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".deleteProject() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(stmt);
        }

        return false;
    }

    private boolean approveProject(Connection aConnection, String projId, User user) throws Exception {
   //     TargetProject tp = getTargetProject(aConnection, projId);
        TargetProject tp = ProjectDAO.getTargetProject(aConnection, projId);
        String upd = "";

        if (tp.waitingForDM()) {
            upd = "update target_projects " + "set internal_status = 'Z',"
                    + "dm_approval_id = '" + user.getUserid() + "', "
                    + "dm_approval_date = sysdate, "
                    + "date_changed = sysdate " + "where target_project_id = "
                    + tp.getId();
        } else if (tp.waitingForProjectSalesMgr()) {
            upd = "update target_projects " + "set internal_status = 'M',"
                    + "proj_sales_mgr_approval_id = '" + user.getUserid() + "', "
                    + "proj_sales_mgr_approval_date = sysdate, "
                    + "date_changed = sysdate " + "where target_project_id = "
                    + tp.getId();
        } else {
            upd = "update target_projects " + "set internal_status = 'A',"
                    + "champs_mgr_approval_id = '" + user.getUserid() + "', "
                    + "champs_mgr_approval_date = sysdate, "
                    + "date_changed = sysdate " + "where target_project_id = "
                    + tp.getId();
        }
        Statement stmt = null;
        try {
            stmt = aConnection.createStatement();
            stmt.executeUpdate(upd);
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".approveProject() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(stmt);
        }

        return false;
    }

    private String fixQuotes(String in) {
        if(in==null) return "";
        StringBuffer newString = new StringBuffer("");

        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);

            if (c == '\'') {
                newString.append("`");
            } else {
                newString.append(c);
            }
        }

        return newString.toString();
    }

    private String formatDate(String inDt) {
        StringBuffer outDt = new StringBuffer("");

        if (!inDt.equals("")) {
            StringTokenizer st = new StringTokenizer(inDt, "/");

            if (st.countTokens() <= 1) {
                st = new StringTokenizer(inDt, "-");
            }

            // Token is not / or - so the date is not valid. Return an empty
            // string.
            if (st.countTokens() <= 1) {
                return "";
            }

            int mo = 0;
            int day = 0;
            int yr = 0;
            int cnt = 0;
            boolean haveMo = false;
            boolean haveDay = false;
            boolean haveYr = false;

            while (st.hasMoreElements()) {
                String token = st.nextToken();
                cnt++;
                int tst = 0;

                try {
                    tst = Integer.parseInt(token);
                } catch (Exception e) {
                    log("formatDate passed an alpha character: "
                            + e.getMessage());
                    return "";
                }

                // if tst > 12, cannot be month
                // if tst > 31, cannot be day

                if (!haveMo && !haveDay && !haveYr) {
                    if (tst > 31) { // tst is year
                        if (tst < 1000) {
                            yr = tst + 2000;
                        } else {
                            yr = tst;
                        }
                        haveYr = true;
                    } else if (tst > 12) {
                        day = tst;
                        haveDay = true;
                    } else {
                        mo = tst;
                        haveMo = true;
                    }
                } else if (!haveDay && !haveYr) {
                    if (tst > 31) { // tst is year
                        if (tst < 1000) {
                            yr = tst + 2000;
                        } else {
                            yr = tst;
                        }
                        haveYr = true;
                    } else {
                        day = tst;
                        haveDay = true;
                    }
                } else if (!haveMo && !haveDay) {
                    if (tst > 31) {
                        return ""; // invalid month or day
                    } else if (tst > 12) {
                        day = tst;
                        haveDay = true;
                    } else {
                        mo = tst;
                        haveMo = true;
                    }
                } else if (!haveMo && !haveYr) {
                    if (tst > 31) { // tst is year
                        if (tst < 1000) {
                            yr = tst + 2000;
                        } else {
                            yr = tst;
                        }
                        haveYr = true;
                    } else if (tst > 12) {
                        return ""; // invalid month
                    } else {
                        mo = tst;
                        haveMo = true;
                    }
                } else if (!haveMo) {
                    if (tst > 12) {
                        return "";
                    }

                    mo = tst;
                    haveMo = true;
                } else if (!haveDay) {
                    if (tst > 31) {
                        return ""; // invalid day
                    }

                    day = tst;
                    haveDay = true;
                } else if (!haveYr) {
                    if (tst < 1000) {
                        yr = tst + 2000;
                    } else {
                        yr = tst;
                    }
                    haveYr = true;
                }

                // if mo == 4,6,9,11, day <= 30
                // if mo == 2 and year%4 != 0, day <=28
                // if mo == 2 and year%4 == 0, day <= 29

                if (((mo == 4 || mo == 6 || mo == 9 || mo == 11) && day > 30)
                        || (mo == 2 && day > 29)
                        || (mo == 2 && day > 28 && (yr % 4 != 0))) {
                    return ""; // invalid date
                }
            }

            if (mo == 0 || day == 0 || yr == 0) {
                return ""; // breaking date apart didn't work - pass back enpty
                           // string
            }

            if (mo < 10)
                outDt.append("0");
            outDt.append(mo);
            outDt.append("/");

            if (day < 10)
                outDt.append("0");
            outDt.append(day);
            outDt.append("/");
            outDt.append(yr);
        }

        return outDt.toString();
    }

    private boolean isNumber(String inVal) {
        boolean num = true;
        boolean haveDec = false;

        char[] chars = inVal.toCharArray();
        if (chars.length > 0){
	        for (int i = 0; i < chars.length; i++) {
	            if (chars[i] < '0' || chars[i] > '9') {
	                if (chars[i] == '.') {
	                    if (haveDec) {
	                        num = false;
	                    }
	                    haveDec = true;
	                } else {
	                    num = false;
	                }
	            }
	        }
        } else {
        	num = false;
        }

        return num;
    }

} //class
