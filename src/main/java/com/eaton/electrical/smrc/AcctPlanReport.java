//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.49  2006/03/16 19:13:33  e0073445
// Removed code that was producing warnings.
//
// Revision 1.48  2005/09/12 19:07:50  lubbejd
// More changes for adding attachments to tasks (CR28583)
//
// Revision 1.47  2005/07/05 15:15:25  lubbejd
// Changes for CR29627 (Remove security for tasks).
//
// Revision 1.46  2005/02/15 19:25:47  lubbejd
// Changes for adding Usage report to Other reports
//
// Revision 1.45  2005/02/11 19:07:27  lubbejd
// Remove calls to MiscDAO.saveAPLog() from everywhere, and added the call
// to SMRCBaseServlet to update with every forward to a jsp
//
// Revision 1.44  2005/02/10 18:43:10  lubbejd
// Changed MiscDAO.saveAPLog() to use User object instead of userid string
// so it can call UserDAO.getGeogForUser for storing the sp_geog on the activity
// log.
//
// Revision 1.43  2005/01/31 15:26:05  lubbejd
// Changes to check other sales engineer fields for change request 28706
//
// Revision 1.42  2005/01/14 13:15:30  lubbejd
// Changes to use srYear instead of current year
//
// Revision 1.41  2005/01/10 22:17:24  vendejp
// code cleanup
//
// Revision 1.40  2005/01/09 05:59:55  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.39  2005/01/05 22:40:25  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.38  2005/01/05 21:01:53  vendejp
// Changes for log4j logging and exception handling
//
// Revision 1.37  2004/12/23 18:53:26  lubbejd
// Improve Task Report Performance
//
// Revision 1.36  2004/12/23 18:12:51  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.35  2004/12/15 18:07:44  vendejp
// Fixed some bugs and added segment overrides everywhere.
//
// Revision 1.34  2004/11/08 11:58:01  lubbejd
// Removed getProductsAlphabetically() and changed program to use same method in ProductDAO
//
// Revision 1.33  2004/11/03 19:17:12  vendejp
// *** empty log message ***
//
// Revision 1.32  2004/10/29 21:11:54  vendejp
// *** empty log message ***
//
// Revision 1.31  2004/10/29 16:22:04  vendejp
// *** empty log message ***
//
// Revision 1.30  2004/10/28 18:05:50  lubbejd
// prevent sales engineers from running target market report
//
// Revision 1.29  2004/10/28 10:55:07  lubbejd
// added TODO comments
//
// Revision 1.28  2004/10/27 15:59:53  vendejp
// *** empty log message ***
//
// Revision 1.27  2004/10/20 17:43:07  lubbejd
// work in progress
//
// Revision 1.26  2004/10/19 14:36:41  schweks
// Removing unused variables and code.
//
// Revision 1.25  2004/10/18 19:00:25  lubbejd
// work in progress
//
// Revision 1.24  2004/10/18 16:33:05  lubbejd
// Deleted a lot of obsolete code related to standard reports and salesman report.
//
// Revision 1.23  2004/10/16 18:14:58  manupvj
// Added abstract servlet for common functions and common point of logging.
//
// Revision 1.22  2004/10/14 17:40:50  schweks
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
 * This is the reporting servlet for the OEM Account Planning application. Split
 * off from OEMAcctPlan on 1/28 so each one would be smaller
 * 
 * @author Carl Abel
 * @date January 28, 2002
 */
public class AcctPlanReport extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection DBConn = null;
        boolean redirect = false;
        String sFwdUrl = "/SMRCErrorPage.jsp";
        User user = null;
        
        try {
            DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();

            user = SMRCSession.getUser(request, DBConn);

            setHeader(request, DBConn, user);
            sFwdUrl = writeNewPage(request, DBConn, user);
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

    private ArrayList getUsers(Connection aConnection) throws Exception {
        ArrayList users = new ArrayList(300);

        String sel = "SELECT userid,first_name,last_name,vistaline_id,view_everything "
                + "FROM users "
                + "WHERE hide_from_assignments <> 'Y' "
                + "ORDER BY last_name, first_name";

        Statement stmt = null;
        ResultSet response = null;
        try {
            stmt = aConnection.createStatement();
            response = stmt.executeQuery(sel);

            while (response.next()) {
                User user = new User();

                if (response.getString("userid") != null) {
                    user.setUserid(response.getString("userid"));
                }
                if (response.getString("first_name") != null) {
                    user.setFirstName(response.getString("first_name"));
                }
                if (response.getString("last_name") != null) {
                    user.setLastName(response.getString("last_name"));
                }
                if (response.getString("vistaline_id") != null) {
                    user.setVistaId(response.getString("vistaline_id")
                            .toUpperCase());
                }
                if (response.getString("view_everything") != null) {
                    user.setViewEverything(response
                            .getString("view_everything"));
                }
                users.add(user);
            }

        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getUsers() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(response);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return users;
    }

    private ArrayList getUserTasks(Connection aConnection, String assignedTo,
            boolean showComplete, String geog, String product,
            String ebe, User user) throws Exception {
        ArrayList tasks = new ArrayList(200);

        StringBuffer fromClause = new StringBuffer("from sales_plan_pap spp, ebe_category, products, users, customer c ");
        StringBuffer whereClause = new StringBuffer("WHERE assigned_to like '");
        whereClause.append(assignedTo);
        whereClause.append("'");

        if (!geog.equals("")) {
 //           fromClause.append(",customer c");
 //           selectCustomerName = ",c.customer_name customer_name ";
 //           whereClause.append(" AND spp.vista_customer_number = c.vista_customer_number");
            whereClause.append(" AND c.sp_geog like '");
            whereClause.append(geog);
            whereClause.append("%' ");
 //           whereClause.append(" and c.vista_customer_number (+) = spp.vista_customer_number ");
        }

        if (!product.equals("")) {
            whereClause.append(" AND spp.product_id = '" + product + "'");
        }

        if (!ebe.equals("")) {
            whereClause.append(" AND spp.ebe_id = " + ebe + " ");
        }

        if (!showComplete) {
            whereClause.append(" AND (complete = 'N' OR complete is null)");
        }

        whereClause.append(" AND ebe_category.ebe_id (+) = spp.ebe_id ");
        whereClause.append(" AND products.product_id (+) = spp.product_id ");
        whereClause.append(" and nvl(products.current_flag,'Y') = 'Y' ");
        whereClause.append(" and users.userid (+) = spp.assigned_to ");
        whereClause.append(" and c.vista_customer_number (+) = spp.vista_customer_number ");
        
        String sel = "SELECT spp.vista_customer_number,spp.product_id,spp.action,spp.objective,"
                + "spp.schedule,spp.results,spp.task_id,spp.complete,spp.assigned_to,spp.cc_email,spp.ebe_id, "
				+ "ebe_category.description ebe_description, products.product_description product_description, " 
				+ "users.last_name assignedUserLastName, users.first_name assignedUserFirstName " 
				+ ",c.customer_name customer_name "
                + fromClause + " " + whereClause.toString();

        SMRCLogger.debug("SQL - AcctPlanReport.getUserTasks()\n" + sel);
        
        Statement stmt = null;
        ResultSet response = null;
        try {
            stmt = aConnection.createStatement();
            response = stmt.executeQuery(sel);

            while (response.next()) {
                String vcn = response.getString("vista_customer_number");
 //               Customer cust = getOneCustomer(aConnection, vcn);

      //          if (user.ableToSee(cust)) {
                    PurchaseActionProgram pap = new PurchaseActionProgram();

                    pap.setCustomer(vcn);
                    pap.setProduct(response.getString("product_id"));
                    pap.setTaskId(response.getInt("task_id"));

                    if (response.getString("action") != null) {
                        pap.setAction(response.getString("action"));
                    }

                    if (response.getString("objective") != null) {
                        pap.setObjective(response.getString("objective"));
                    }

                    if (response.getDate("schedule") != null) {
                        pap.setSchedule(response.getDate("schedule"));
                    }

                    if (response.getString("results") != null) {
                        pap.setResults(response.getString("results"));
                    }

                    if (response.getString("assigned_to") != null) {
                        pap.setAssignedTo(response.getString("assigned_to"));
                    }

                    if (response.getString("cc_email") != null) {
                        //pap.setCCEmail(response.getString("cc_email"));
                        // commented by jpv
                    }

                    if (response.getString("ebe_id") != null) {
                        pap.setEBECategory(response.getInt("ebe_id"));
                    }
                    
                    if (response.getString("ebe_description") != null){
                    	pap.setEBEDescription(response.getString("ebe_description"));
                    }

                    if (response.getString("product_description") != null){
                    	pap.setProductDescription(response.getString("product_description"));
                    }
                    
                    if (response.getString("customer_name") != null){
                    	pap.setCustomerName(response.getString("customer_name"));
                    }
                    
                    if (response.getString("assignedUserFirstName") != null &&
                    	response.getString("assignedUserLastName") != null){
                    	pap.setAssignedUserName(response.getString("assignedUserFirstName") + " " + response.getString("assignedUserLastName"));
                    }
                    
                    if (response.getString("complete") != null) {
                        if (response.getString("complete").equals("Y")) {
                            pap.setComplete(true);
                        } else {
                            pap.setComplete(false);
                        }
                    }
                    
                    pap.setAttachments(AccountDAO.getTaskAttachments(pap.getTaskId(),aConnection));
                    
                    tasks.add(pap);
       //         }
            }

        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getUserTasks() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(response);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return tasks;
    }

    private ArrayList getRegions(Connection aConnection) throws Exception {
        ArrayList regions = new ArrayList(10);

        String sel = "SELECT sp_geog,description,zone from geographies where district = '0' and "
                + " substr(sp_geog,1,4) in (select substr(sp_geog,1,4) from customer) "
                + " order by description";
        Statement stmt = null;
        ResultSet response = null;
        try {
            stmt = aConnection.createStatement();
            response = stmt.executeQuery(sel);

            while (response.next()) {
                Region region = new Region();
                region.setRegion(response.getString("zone"));
                region.setDescription(response.getString("description"));
                region.setSPGeog(response.getString("sp_geog"));

                regions.add(region);
            }

        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getRegions() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(response);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return regions;
    }

    private ArrayList getDistricts(Connection aConnection) throws Exception {
        ArrayList districts = new ArrayList(100);

        String sel = "SELECT sp_geog,description,zone,district from geographies where (team is null or team = '' or team = ' ') and "
                + " substr(sp_geog,1,5) in (select substr(sp_geog,1,5) from customer ) "
                + " order by sp_geog";
        Statement stmt = null;
        ResultSet response = null;
        try {
            stmt = aConnection.createStatement();
            response = stmt.executeQuery(sel);

            while (response.next()) {
                Region district = new Region();
                district.setRegion(response.getString("zone"));
                district.setDescription(response.getString("description"));
                district.setSegment(response.getString("district"));
                district.setSPGeog(response.getString("sp_geog"));

                districts.add(district);
            }

        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getDistricts() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(response);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return districts;
    }

    private boolean customerExists(Connection aConnection, String vcn)
            throws Exception {
        int cnt = 0;
        String sel = "SELECT count(*) from customer where vista_customer_number = '"
                + vcn + "'";
        Statement stmt = null;
        ResultSet response = null;
        try {
            stmt = aConnection.createStatement();
            response = stmt.executeQuery(sel);

            while (response.next()) {
                cnt = response.getInt(1);
            }

        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".customerExists() ",
                    e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(response);
            SMRCConnectionPoolUtils.close(stmt);
        }

        if (cnt == 0) {
            return false;
        }
        return true;
       
    }

    private Customer getOneCustomer(Connection aConnection, String vcn)
            throws Exception {
        Customer customer = new Customer();
        String sel = "SELECT * from customer where vista_customer_number = '"
                + vcn + "'";
        Statement stmt = null;
        ResultSet response = null;
        try {
            stmt = aConnection.createStatement();
            response = stmt.executeQuery(sel);

            while (response.next()) {
                customer = new Customer(response
                        .getString("vista_customer_number"));

                if (response.getString("phone_number") != null) {
                    customer.setPhoneNumber(response.getString("phone_number"));
                }

                if (response.getString("sales_engineer1") != null) {
                    customer.setSalesId(response.getString("sales_engineer1"));
                }
                
                if (response.getString("sales_engineer2") != null) {
                    customer.setSalesEngineer2(response.getString("sales_engineer2"));
                }
                
                if (response.getString("sales_engineer3") != null) {
                    customer.setSalesEngineer3(response.getString("sales_engineer3"));
                }
                
                if (response.getString("sales_engineer4") != null) {
                    customer.setSalesEngineer4(response.getString("sales_engineer4"));
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

                if (response.getString("sp_geog") != null) {
                    customer.setSPGeog(response.getString("sp_geog"));
                }

                customer.setStage(response.getInt("sp_stage_id"));

                customer.setNumStores(response.getInt("num_stores"));

            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getOneCustomer() ",
                    e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(response);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return customer;
    }

    private String writeNewPage(HttpServletRequest request, Connection aConnection, User user)
            throws java.io.IOException, Exception {

        String page = "home";
        if (request.getParameter("page") != null) {
            page = request.getParameter("page");
        }

        
        if (page.equals("reportsample")) {
            return writeSampleReportSelect(request, aConnection, user);
        } else if (page.equals("reportother")) {
        	ArrayList segments = SegmentsDAO.getSegments(0,4,aConnection);
        	request.setAttribute ("segments",segments);
        	return writeOtherReportSelect(request, aConnection, user);
        } else if (page.equals("taskReport")) {
            return writeTaskReport(request, aConnection, user);
        } else if (page.equals("sampleRpt")) {
            return writeSampleReport(request, aConnection);
        }

        return "";

    }

    private String writeSampleReportSelect(HttpServletRequest request, Connection aConnection, User user)
            throws java.io.IOException, Exception {
        //		User user = getUser(userid);
        ArrayList users = getUsers(aConnection);
        ArrayList districts = getDistricts(aConnection);
        ArrayList goodSegments = new ArrayList(20);
        ArrayList rollUpGeogs = new ArrayList(20); // will hold geographies like
                                                   // 14000
        ArrayList prods = getProductsRequested(aConnection);

        for (int i = 0; i < districts.size(); i++) {
            Region seg = (Region) districts.get(i);

            //			VistaSecurity vs = new VistaSecurity();
            //			vs.setVistaSecurityForGeog(user,seg.getRegion(),seg.getSegment());

            if (user.ableToSee(seg.getSPGeog())) {
                goodSegments.add(seg);

                boolean inRollup1 = false; // National
                boolean inRollup2 = false; // Sales Org
                boolean inRollup3 = false; // Zones

                for (int j = 0; j < rollUpGeogs.size(); j++) {
                    Region tstSeg = (Region) rollUpGeogs.get(j);

                    if (tstSeg.getSPGeog().equals(
                            seg.getSPGeog().substring(0, 1))) {
                        inRollup1 = true;
                    }

                    if (tstSeg.getSPGeog().equals(
                            seg.getSPGeog().substring(0, 2))) {
                        inRollup2 = true;
                    }

                    if (tstSeg.getSPGeog().equals(
                            seg.getSPGeog().substring(0, 4))) {
                        inRollup3 = true;
                    }
                }

                if (!inRollup1) {
                    Region roll1 = new Region();
                    roll1.setSPGeog(seg.getSPGeog().substring(0, 1));
                    roll1.setRegion("00");
                    roll1.setSegment("0");
                    roll1.setDescription(getDistrictName(aConnection, seg
                            .getSPGeog().substring(0, 1)
                            + "0000"));
                    rollUpGeogs.add(roll1);
                }

                if (!inRollup2) {
                    Region roll2 = new Region();
                    roll2.setSPGeog(seg.getSPGeog().substring(0, 2));
                    roll2.setRegion("00");
                    roll2.setSegment("0");
                    roll2.setDescription(getDistrictName(aConnection, seg
                            .getSPGeog().substring(0, 2)
                            + "000"));
                    rollUpGeogs.add(roll2);
                }

                if (!inRollup3) {
                    Region roll3 = new Region();
                    roll3.setSPGeog(seg.getSPGeog().substring(0, 4));
                    roll3.setRegion(seg.getSPGeog().substring(2, 4));
                    roll3.setSegment("0");
                    roll3.setDescription(getDistrictName(aConnection, seg
                            .getSPGeog().substring(0, 4)
                            + "0"));
                    rollUpGeogs.add(roll3);
                }
            }
        }

        request.setAttribute("prods", prods);
        request.setAttribute("rollUpGeogs", rollUpGeogs);
        request.setAttribute("users", users);
        request.setAttribute("goodSegments", goodSegments);

        return "/AcctPlanSampleReportSelect.jsp";

    }

    private ArrayList getProductsRequested(Connection aConnection)
            throws Exception {
        ArrayList prods = new ArrayList(100);

        String sel = "select distinct upper(catalog_num) as catalog_num from product_sample_detail psd, product_samples ps "
                + " where ps.product_sample_id = psd.product_sample_id "
                + " order by catalog_num";
        Statement s = null;
        ResultSet r = null;
        try {
            s = aConnection.createStatement();
            r = s.executeQuery(sel);

            while (r.next()) {
                prods.add(r.getString("catalog_num"));
            }

        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".getProductsRequested() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(r);
            SMRCConnectionPoolUtils.close(s);
        }
        return prods;
    }

    private String writeOtherReportSelect(HttpServletRequest request, Connection aConnection, User user)
            throws java.io.IOException, Exception {

        ArrayList districts = getDistricts(aConnection);
        ArrayList goodSegments = new ArrayList(20);
        ArrayList rollUpGeogs = new ArrayList(20); // will hold geographies like
                                                   // 14000
        HttpSession session = request.getSession(false);
        int srYear = Globals.a2int((String) session.getAttribute("srYear"));
       	// If the year is zero, something went wrong... set to current year
    	if ( srYear == 0) {
    		Calendar cal = new GregorianCalendar();
    	    
    	    // Get the components of the date
    	    srYear = cal.get(Calendar.YEAR);
    	}
        ArrayList products = ProductDAO.getProductsAlphabetically(srYear, aConnection);

        ArrayList regions = getRegions(aConnection);
        ArrayList goodRegions = new ArrayList(20);
        ArrayList ebeCats = getEBECategories(aConnection);

        /*
        for (int i = 0; i < regions.size(); i++) {
            Region reg = (Region) regions.get(i);

            if (user.ableToSee(reg.getSPGeog())) {
                goodRegions.add(reg);
            }
        }
        */
        goodRegions = regions;

        for (int i = 0; i < districts.size(); i++) {
            Region seg = (Region) districts.get(i);

            //if (user.ableToSee(seg.getSPGeog())) {
                goodSegments.add(seg);

                boolean inRollup1 = false; // National
                boolean inRollup2 = false; // Sales Org
                boolean inRollup3 = false; // Zones

                for (int j = 0; j < rollUpGeogs.size(); j++) {
                    Region tstSeg = (Region) rollUpGeogs.get(j);

                    if (tstSeg.getSPGeog().equals(
                            seg.getSPGeog().substring(0, 1))) {
                        inRollup1 = true;
                    }

                    if (tstSeg.getSPGeog().equals(
                            seg.getSPGeog().substring(0, 2))) {
                        inRollup2 = true;
                    }

                    if (tstSeg.getSPGeog().equals(
                            seg.getSPGeog().substring(0, 4))) {
                        inRollup3 = true;
                    }
                }

                if (!inRollup1) {
                    Region roll1 = new Region();
                    roll1.setSPGeog(seg.getSPGeog().substring(0, 1));
                    roll1.setRegion("00");
                    roll1.setSegment("0");
                    roll1.setDescription(getDistrictName(aConnection, seg
                            .getSPGeog().substring(0, 1)
                            + "0000"));
                    rollUpGeogs.add(roll1);
                }

                if (!inRollup2) {
                    Region roll2 = new Region();
                    roll2.setSPGeog(seg.getSPGeog().substring(0, 2));
                    roll2.setRegion("00");
                    roll2.setSegment("0");
                    roll2.setDescription(getDistrictName(aConnection, seg
                            .getSPGeog().substring(0, 2)
                            + "000"));
                    rollUpGeogs.add(roll2);
                }

                if (!inRollup3) {
                    Region roll3 = new Region();
                    roll3.setSPGeog(seg.getSPGeog().substring(0, 4));
                    roll3.setRegion(seg.getSPGeog().substring(2, 4));
                    roll3.setSegment("0");
                    roll3.setDescription(getDistrictName(aConnection, seg
                            .getSPGeog().substring(0, 4)
                            + "0"));
                    rollUpGeogs.add(roll3);
                }
           // }
        }

        ArrayList users = getUsers(aConnection);
        
        

        //ArrayList visitDistricts = DistrictDAO.getAllDistricts(DBConn);
        //request.setAttribute("visitDistricts", visitDistricts);
        request.setAttribute("visitOutcomes", MiscDAO.getCodes(
                "cust_visit_outcome", aConnection));
        request.setAttribute("visitReasons", MiscDAO.getCodes(
                "cust_visit_reason", aConnection));
        request.setAttribute("visitDates", CustomerVisitsDAO
                .getDistinctVisitDates(aConnection));

        request.setAttribute("users", users);
        // goodRegions and goodSegments are used in both task and target market reporting
        request.setAttribute("goodRegions", goodRegions);
        request.setAttribute("goodSegments", goodSegments);

        request.setAttribute("products", products);
        request.setAttribute("ebeCats", ebeCats);
        request.setAttribute("rollUpGeogs", rollUpGeogs);
        request.setAttribute("srMonth", MiscDAO.getSRMonth(aConnection));
        request.setAttribute("srYear", MiscDAO.getSRYear(aConnection));
        
        //**TODO See if we can change District filter selections based on selected Zones
        
        // For Target Market reports
        ArrayList competitors = MiscDAO.getVendors(true, aConnection);
        ArrayList tmSegments = SegmentsDAO.getTargetMarketSegments(aConnection);
        ArrayList divisions = DivisionDAO.getDivisions(aConnection);
       
        boolean canRunTMReport = true;
        if (user.isSalesEngineer() && !user.hasOverrideSecurity()){
            canRunTMReport = false;
        }
        
        request.setAttribute("competitors", competitors);
        request.setAttribute("tmSegments", tmSegments);
        request.setAttribute("divisions", divisions);
        request.setAttribute("canRunTMReport", new Boolean(canRunTMReport));
        
        // For usage reports
        boolean canRunUsageReport = false;
        boolean hasSegmentOverride = false;
        ArrayList segmentOverrideList = user.getSegmentOverrides();
     	if (segmentOverrideList.size() > 0){
     		hasSegmentOverride = true;
     	}
     	// SE's and users without overrides or any geog security cannot run the report
        if (user.hasOverrideSecurity() || user.ableToViewEverything() || hasSegmentOverride){
            canRunUsageReport = true;
        } else if (user.isSalesEngineer()){
            canRunUsageReport = false;
        } else {
            ArrayList ugsList = user.getAllUGS();
            if (ugsList.size() > 0){
                canRunUsageReport = true;
            }
        }
        if (canRunUsageReport){
            ArrayList usageGeogs = TAPcommon.getGeogsUserCanSee(user, aConnection);
        
            request.setAttribute("usageGeogs", usageGeogs);
        }
        request.setAttribute("canRunUsageReport", new Boolean(canRunUsageReport));
        request.setAttribute("hasSegmentOverride", new Boolean(hasSegmentOverride));
        return "/AcctPlanOtherReports.jsp";

    }

    private String getDistrictName(Connection aConnection, String geog)
            throws Exception {
        StringBuffer spGeog = new StringBuffer(geog);

        if (geog.length() < 5) {
            for (int i = geog.length(); i < 5; i++) {
                spGeog.append("0");
            }
        }

        String desc = "";

        String sel = "select * from geographies where sp_geog = '"
                + spGeog.toString() + "'";
        Statement s = null;
        ResultSet r = null;
        try {
            s = aConnection.createStatement();
            r = s.executeQuery(sel);

            while (r.next()) {
                desc = r.getString("description");
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getDistrictName() ",
                    e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(r);
            SMRCConnectionPoolUtils.close(s);
        }

        return desc + " (" + spGeog.toString() + ")";
    }
    
   
    private String writeTaskReport(HttpServletRequest request, Connection aConnection, User user)
            throws java.io.IOException, Exception {
        //		User user = getUser(userid);
        //User assignedTo = UserDAO.getUser(request.getParameter("assignedTo"),
        //        aConnection);
        //assignedTo = user;
    	User assignedTo=null;
    	String userQueryAppend = null;
    	
    	if(request.getParameter("assignedTo")!=null){
    		assignedTo = UserDAO.getUser(request.getParameter("assignedTo"),aConnection);
    		userQueryAppend=request.getParameter("assignedTo");
    	}else{
    		assignedTo=user;
    		userQueryAppend=user.getUserid();
    	}
    	
        boolean comp = false;
        String product = "";
        String geog = "";
        String ebe = "";

        if (request.getParameter("seeComplete") != null) {
            comp = true;
        }

        if (request.getParameter("region") != null
                && request.getParameter("region").length() > 0) {
            geog = request.getParameter("region").substring(0, 4);
        }

        if (request.getParameter("segment") != null
                && request.getParameter("segment").length() > 0) {
            geog = request.getParameter("segment").substring(0, 5);
        }

        if (request.getParameter("product") != null
                && request.getParameter("product").length() > 0) {
            product = request.getParameter("product");
        }

        if (request.getParameter("ebe") != null
                && request.getParameter("ebe").length() > 0) {
            ebe = request.getParameter("ebe");
        }

        ArrayList tasks = getUserTasks(aConnection, userQueryAppend, comp, geog, product,ebe, user);
        
        
        if (!geog.equals("")) {
            StringBuffer geogNm = new StringBuffer(geog);
            for (int i = geogNm.length(); i < 5; i++) {
                geogNm.append("0");
            }
            request.setAttribute("geogName", MiscDAO.getGeography(geogNm.toString(), aConnection).getDescription());
        }

        if (!product.equals("")) {
            request.setAttribute("productDesc", TAPcommon.getProductDescription(product, aConnection));
        }

        if (!ebe.equals("")) {
            request.setAttribute("ebeDesc", TAPcommon.getEBEDescription(ebe,aConnection));
        }

  
        request.setAttribute("assignedToUser", assignedTo);
        request.setAttribute("tasks", tasks);
  

        return "/AcctPlanTaskReport.jsp";

    }

    private String writeSampleReport(HttpServletRequest request, Connection aConnection)
            throws java.io.IOException, Exception {
        StringBuffer parms = new StringBuffer("");
        boolean firstName = true;
        Enumeration parmNames = request.getParameterNames();
        //		User user = getUser(userid);

        while (parmNames.hasMoreElements()) {
            String name = (String) parmNames.nextElement();

            if (!name.equalsIgnoreCase("orderBy") && !name.equals("submit")) {
                String[] vals = request.getParameterValues(name);

                for (int i = 0; i < vals.length; i++) {
                    if (firstName) {
                        firstName = false;
                        parms.append("?");
                    } else {
                        parms.append("&");
                    }
                    parms.append(name);
                    parms.append("=");
                    parms.append(vals[i]);
                }
            }
        }

        String vcn = "%";
        String startDt = "%";
        String catNum = "%";
        String requestor = "%";
        String city = "%";
        String state = "%";
        String orderBy = "customer_name";
        String geog = "%";

        if (request.getParameter("startDt") != null) {
            String dt = request.getParameter("startDt");
            int mo = Integer.parseInt(dt.substring(4, dt.length()));

            if (mo < 10) {
                startDt = "0" + mo + "/";
            } else {
                startDt = mo + "/";
            }

            startDt = startDt + dt.substring(0, 4);
        }

        if (request.getParameter("vcn") != null
                && request.getParameter("vcn").length() > 0) {
            vcn = request.getParameter("vcn");
        }

        if (request.getParameter("catNum") != null
                && request.getParameter("catNum").length() > 0) {
            catNum = request.getParameter("catNum");
        }

        if (request.getParameter("requestor") != null
                && request.getParameter("requestor").length() > 0) {
            requestor = request.getParameter("requestor");
        }

        if (request.getParameter("city") != null
                && request.getParameter("city").length() > 0) {
            city = request.getParameter("city");
        }

        if (request.getParameter("state") != null
                && request.getParameter("state").length() > 0) {
            state = request.getParameter("state");
        }

        if (request.getParameter("orderBy") != null
                && request.getParameter("orderBy").length() > 0) {
            orderBy = request.getParameter("orderBy");
        }

        if (request.getParameter("geog") != null
                && request.getParameter("geog").length() > 0) {
            String tst = request.getParameter("geog");

            if (tst.length() >= 5) {
                //removed by jpv because this is a bug. it can cause
                // arrayoutofbounds error
                //if (tst.substring(5,6).equals("0")) {

                if (tst.substring(4, 5).equals("0")) {

                    if (tst.substring(3, 4).equals("0")) {
                        if (tst.substring(2, 3).equals("0")) {
                            if (tst.substring(1, 2).equals("0")) {
                                geog = tst.substring(0, 1) + "%";
                            } else {
                                geog = tst.substring(0, 2) + "%";
                            }
                        } else {
                            geog = tst.substring(0, 3) + "%";
                        }
                    } else {
                        geog = tst.substring(0, 4) + "%";
                    }
                } else {
                    geog = tst.substring(0, 5) + "%";
                }
                //}
                //else {
                //	geog = tst + "%";
                //}
            } else {
                geog = tst + "%";
            }
        }

        ArrayList requests = getSampleRequests(aConnection, startDt, vcn,
                catNum, requestor, city, state, orderBy, geog);
        ArrayList custList = new ArrayList();

        for (int i = 0; i < requests.size(); i++) {
            SampleRequest sr = (SampleRequest) requests.get(i);
            custList.add(getOneCustomer(aConnection, sr.getVistaCustNum()));
        }

        request.setAttribute("parms", parms);
        request.setAttribute("requests", requests);
        request.setAttribute("custList", custList);
        request.setAttribute("catNum", catNum);

        return "/AcctPlanSampleReport.jsp";

    }

    private ArrayList getSampleRequests(Connection aConnection, String startDt,
            String vcn, String catNum, String requestor, String city,
            String state, String orderBy, String geog) throws Exception {

        ArrayList requests = new ArrayList();

        String sel = "select a.*, customer_name, last_name||', '||first_name usr \n"
                + " from product_samples a, customer b, users c \n"
                + " where a.vista_customer_number like '"
                + vcn
                + "' \n"
                + " and a.vista_customer_number = b.vista_customer_number \n"
                + " and lower(requestor) like '"
                + requestor.replace('*', '%').toLowerCase()
                + "%' \n"
                + " and a.requestor = c.userid \n"
                + " and upper(a.ship_city) like '"
                + city.toUpperCase().replace('*', '%')
                + "' \n"
                + " and upper(a.ship_state) like '"
                + state.toUpperCase().replace('*', '%')
                + "' \n"
                + " and b.sp_geog like '"
                + geog
                + "' \n"
                + " and request_date >= to_date('"
                + startDt
                + "','MM/YYYY') \n"
                + " and product_sample_id in (select product_sample_id from product_sample_detail where upper(catalog_num) like '"
                + catNum.toUpperCase() + "') \n" +
                //" and a.vista_customer_number in (select
                // vista_customer_number from cust_group_xref where group_id =
                // '" + sGroup.getId() + "') " +
                " order by " + orderBy;

        		SMRCLogger.debug("SQL - AcctPlanReport.getSampleRequests():\n" + sel);
        
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                SampleRequest sr = new SampleRequest();

                if (rs.getString("vista_customer_number") != null) {
                    sr.setVistaCustNum(rs.getString("vista_customer_number"));
                }

                if (rs.getString("customer_name") != null) {
                    sr.setCustomerName(rs.getString("customer_name"));
                }

                if (rs.getString("usr") != null) {
                    sr.setRequestor(rs.getString("usr"));
                }

                if (rs.getString("use") != null) {
                    sr.setUse(rs.getString("use"));
                }

                if (rs.getString("ship_method") != null) {
                    sr.setShipMethod(rs.getString("ship_method"));
                }

                if (rs.getString("ship_attention") != null) {
                    sr.setAttention(rs.getString("ship_attention"));
                }

                if (rs.getString("ship_addr1") != null) {
                    sr.setAddress1(rs.getString("ship_addr1"));
                }

                if (rs.getString("ship_addr2") != null) {
                    sr.setAddress2(rs.getString("ship_addr2"));
                }

                if (rs.getString("ship_addr3") != null) {
                    sr.setAddress3(rs.getString("ship_addr3"));
                }

                if (rs.getString("ship_city") != null) {
                    sr.setCity(rs.getString("ship_city"));
                }

                if (rs.getString("ship_state") != null) {
                    sr.setState(rs.getString("ship_state"));
                }

                if (rs.getString("ship_zip") != null) {
                    sr.setZip(rs.getString("ship_zip"));
                }

                if (rs.getDate("request_date") != null) {
                    Calendar reqDt = Calendar.getInstance();
                    reqDt.setTime(rs.getDate("request_date"));
                    sr.setRequestDate(reqDt);
                }

                String sel2 = "select * from product_sample_detail "
                        + " where product_sample_id = "
                        + rs.getString("product_sample_id") +
                        //				" and upper(catalog_num) like '" +
                        // catNum.toUpperCase() + "' " +
                        " order by catalog_num";

                
                SMRCLogger.debug("SQL - AcctPlanReport.getSampleRequests():\n" + sel2);
                
                Statement s2 = null;
                ResultSet r2 = null;
                try {
                    s2 = aConnection.createStatement();
                    r2 = s2.executeQuery(sel2);

                    while (r2.next()) {
                        SampleProduct sp = new SampleProduct();

                        sp.setQty(r2.getInt("qty"));
                        sp.setCatalogNum(r2.getString("catalog_num"));

                        sr.addProduct(sp);
                    }

                } catch (Exception e) {
                    SMRCLogger.error(this.getClass().getName()
                            + ".getSampleRequests() ", e);
                    throw e;
                } finally {
                    SMRCConnectionPoolUtils.close(r2);
                    SMRCConnectionPoolUtils.close(s2);
                }

                requests.add(sr);
            }

        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".getSampleRequests() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return requests;
    }

    private ArrayList getEBECategories(Connection aConnection) throws Exception {
        ArrayList cats = new ArrayList(10);

        String sel = "select * from ebe_category order by description";

        Statement s = null;
        ResultSet r = null;
        try {
            s = aConnection.createStatement();
            r = s.executeQuery(sel);

            while (r.next()) {
                EBECategory ec = new EBECategory();

                ec.setId(r.getInt("ebe_id"));
                ec.setDescription(r.getString("description"));

                cats.add(ec);
            }

        } catch (Exception e) {
            SMRCLogger.error(
                    this.getClass().getName() + ".getEBECategories() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(r);
            SMRCConnectionPoolUtils.close(s);
        }

        return cats;
    }

    private void setHeader(HttpServletRequest request, Connection aConnection, User user)
            throws Exception {
        String page = "home";
        //SalesGroup sGroup = new SalesGroup();

        if (request.getParameter("page") != null) {
            page = request.getParameter("page");
        }

        //if (request.getParameter("groupId") != null) {
        //	sGroup = TAPcommon.getSalesGroup(request.getParameter("groupId"),
        // DBConn);
        //}
        //else {
        //	sGroup = getUsersSalesGroup(user.getUserid());
        //}

        
        HeaderBean hdr = new HeaderBean();
        hdr.setPage(page);
        hdr.setHelpPage(TAPcommon.getHelpPage(page, aConnection));
        //hdr.setGroups(TAPcommon.getUsersAuthorizedSalesGroups(user.getUserid(),DBConn));
        hdr.setUser(user);
        //hdr.setThisGroup(sGroup);

        if (request.getParameter("cust") != null) {
            hdr.setCustomer(getOneCustomer(aConnection, request.getParameter("cust")));
            hdr.setAccount(AccountDAO.getAccount(request.getParameter("cust"), aConnection));
        }

        if (request.getParameter("add") != null) {
            hdr.setAddCust(true, (customerExists(aConnection, request
                    .getParameter("cust"))));
            //(customerExists(request.getParameter("cust")) &&
            // TAPcommon.custInGroup(request.getParameter("cust"),sGroup.getId(),
            // DBConn)));
        }

        if (request.getParameter("geog") != null) {
            hdr.setGeog(request.getParameter("geog"));
        }

        if (request.getParameter("industry") != null) {
            hdr.setIndustry(request.getParameter("industry"));
        }

        request.setAttribute("header", hdr);

    }
    
    
} //class