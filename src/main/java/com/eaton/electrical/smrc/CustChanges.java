//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.28  2005/02/11 19:07:26  lubbejd
// Remove calls to MiscDAO.saveAPLog() from everywhere, and added the call
// to SMRCBaseServlet to update with every forward to a jsp
//
// Revision 1.27  2005/02/10 18:43:10  lubbejd
// Changed MiscDAO.saveAPLog() to use User object instead of userid string
// so it can call UserDAO.getGeogForUser for storing the sp_geog on the activity
// log.
//
// Revision 1.26  2005/01/10 03:00:22  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.25  2005/01/09 05:59:53  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.24  2005/01/05 22:40:23  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.23  2004/12/23 18:12:49  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.22  2004/12/15 18:07:43  vendejp
// Fixed some bugs and added segment overrides everywhere.
//
// Revision 1.21  2004/10/19 14:51:03  schweks
// Removing unused variables and code.
//
// Revision 1.20  2004/10/16 18:14:58  manupvj
// Added abstract servlet for common functions and common point of logging.
//
// Revision 1.19  2004/10/14 18:41:37  vendejp
// *** empty log message ***
//
// Revision 1.18  2004/10/14 17:40:50  schweks
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

/** This displays all changes made to a given customer since the triggers were started.
 *
 *	@author Carl Abel
 *	@date March 28, 2003
 */
public class CustChanges extends SMRCBaseServlet {
	
	private static final long serialVersionUID = 100;

	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sFwdUrl="/SMRCErrorPage.jsp";
		boolean redirect=false;
		Connection DBConn = null;
		User user = null;
		
		try{
			DBConn = SMRCConnectionPoolUtils.getDatabaseConnection() ;
			
			user = SMRCSession.getUser(request,DBConn);
			
			sFwdUrl="/CustChanges.jsp";
			printPage(request,DBConn,user);

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
			SMRCConnectionPoolUtils.close(DBConn) ;
			gotoPage(sFwdUrl, request, response,redirect);
		}
	}
	
	private void printPage(HttpServletRequest request, Connection aConnection, User user) throws java.io.IOException, Exception
			{
		HeaderBean hdr = new HeaderBean();
//		String userid = user.getUserid();
		
		String vcn = "";
		String page = "";
		Customer cust = new Customer();
		if (request.getParameter("cust") != null) {
			vcn = request.getParameter("cust");
			page = "viewcustchanges";
			cust = getCustomer(aConnection,vcn);
			hdr.setCustomer(cust);
			hdr.setAccount(AccountDAO.getAccount(request.getParameter("cust"), aConnection));
		}
		else {
			page = "viewplannerchanges";
		}
		
		//SalesGroup sGroup = TAPcommon.getSalesGroup(request.getParameter("groupId"), DBConn);
		
		hdr.setPage(page);
		hdr.setHelpPage(TAPcommon.getHelpPage(page,aConnection));
		
		ArrayList tmp = new ArrayList(1);
		//tmp.add(sGroup);
		hdr.setGroups(tmp);
		hdr.setUser(user);
		//hdr.setThisGroup(sGroup);
		hdr.setPopup(true);

		String viewPage = "";
		if (request.getParameter("viewPage") != null) {
			viewPage = request.getParameter("viewPage");
		}
		
		ArrayList changes = new ArrayList(1);
		if (page.equals("viewcustchanges")) {
			changes = getChanges(aConnection,vcn,viewPage);
		}
		else {
			//changes = getChanges();
		}
		
		boolean showProduct = false;
		boolean showIndustry = false;
		boolean showSalesPlan = false;
		String changeType = "";

		if (viewPage.equals("")) {
			showProduct = true;
			showIndustry = true;
			showSalesPlan = true;
			if (page.equals("viewcustchanges")) {
				changeType = "All Changes";
			}
			else {
				changeType = "Recent Changes";
			}
		}
		else if (viewPage.equals("profile")) {
			showIndustry = true;
			changeType = "Profile Changes";
		}
		else if (viewPage.equals("salesplan")||viewPage.equals("salesplanprint")) {
			showSalesPlan = true;
			changeType = "Sales Plan Changes";
		}
		else if (viewPage.equals("productmix")) {
			showProduct = true;
			changeType = "Product Mix Changes";
		}

//		Create arraylist of descriptions matching items in changes list
		HashMap ProductDescList = new HashMap();
		HashMap ProductSublineDescList = new HashMap();
		//HashMap IndustrySubgroupDescList = new HashMap();
		HashMap TaskDescList = new HashMap();
		HashMap changesUsers = new HashMap();

		for (int i=0; i < changes.size(); i++){

			CustChange cc = (CustChange)changes.get(i);
			ProductDescList.put(new Integer(cc.getId()), TAPcommon.getProductDescription(cc.getProductId(), aConnection));
			ProductSublineDescList.put(new Integer(cc.getId()), getProductSublineDescription(aConnection,cc.getProdSublineId()));
			//IndustrySubgroupDescList.put(new Integer(cc.getId()), getIndustrySubgroupDescription(cc.getIndustryId(),sGroup));
			TaskDescList.put(new Integer(cc.getId()), getTaskDescription(aConnection,cc.getTaskId()));
			changesUsers.put(new Integer(cc.getId()), UserDAO.getUser(cc.getUserid(), aConnection));
		}

		request.setAttribute("changeType", changeType);
		request.setAttribute("custChanges", changes);
		request.setAttribute("customer", cust);
		request.setAttribute("showIndustry", new Boolean(showIndustry));
		request.setAttribute("showSalesPlan", new Boolean(showSalesPlan));
		request.setAttribute("showProduct", new Boolean(showProduct));
		request.setAttribute("ProductDescList", ProductDescList);
		request.setAttribute("ProductSublineDescList", ProductSublineDescList);
		//request.setAttribute("IndustrySubgroupDescList", IndustrySubgroupDescList);
		request.setAttribute("TaskDescList", TaskDescList);
		request.setAttribute("changesUsers", changesUsers);
		request.setAttribute("header", hdr);

	}
	
	private ArrayList getChanges(Connection aConnection,String vcn,String viewPage)throws Exception {
		ArrayList changes = new ArrayList(50);
		
		String sel = "";
		
		if (viewPage.equals("")) {
			sel = "select * from modification_log where vista_cust_num = '" + vcn +
			"' order by mod_time desc, table_name, field_name";
		}
		else if (viewPage.equals("profile")) {
			sel = "select * from modification_log " +
			"where vista_cust_num = '" + vcn + "' " +
//			"and table_name in ('customer','customer_industries','customer_focus_type','customer_channel') " +
			"and table_name in ('customer','customer_industries','customer_focus_type') " +
			"order by mod_time desc, table_name, field_name";
		}
		else if (viewPage.equals("salesplan")||viewPage.equals("salesplanprint")) {
			sel = "select * from modification_log " +
			"where vista_cust_num = '" + vcn + "' " +
			"and table_name in ('sales_plans','sales_plan_pap') " +
//			"and table_name in ('sales_plans','sales_plan_pap','sales_plans_prods_services','sales_plan_objectives') " +
			"order by mod_time desc, table_name, field_name";
		}
		else if (viewPage.equals("productmix")) {
			sel = "select * from modification_log " +
			"where vista_cust_num = '" + vcn + "' " +
			"and table_name in ('customer_product','customer_product_subline') " +
			"order by mod_time desc, table_name, field_name";
		}
		Statement s = null;
		ResultSet r = null;
		try {
			s = aConnection.createStatement();
			r = s.executeQuery(sel);
			
			while (r.next()) {
				CustChange cc = new CustChange();
				
				if (r.getString("id") != null) {
					cc.setId(r.getInt("id"));
				}
				
				if (r.getString("mod_time") != null) {
					cc.setModDate(r.getDate("mod_time"));
					cc.setModTime(r.getTime("mod_time"));
				}
				
				if (r.getString("table_name") != null) {
					cc.setTable(r.getString("table_name"));
				}
				
				if (r.getString("field_name") != null) {
					cc.setField(r.getString("field_name"));
				}
				
				if (r.getString("mod_type") != null) {
					cc.setModType(r.getString("mod_type"));
				}
				
				if (r.getString("old_value") != null) {
					cc.setOldValue(r.getString("old_value"));
				}
				
				if (r.getString("userid") != null) {
					cc.setUserid(r.getString("userid"));
				}
				
				if (r.getString("vista_cust_num") != null) {
					cc.setVistaCustNum(r.getString("vista_cust_num"));
				}
				
				if (r.getString("product_id") != null) {
					cc.setProductId(r.getString("product_id"));
				}
				
				if (r.getString("prod_subline_id") != null) {
					cc.setProdSublineId(r.getString("prod_subline_id"));
				}
				
				if (r.getString("industry_id") != null) {
					cc.setIndustryId(r.getString("industry_id"));
				}
				
				if (r.getString("task_id") != null) {
					cc.setTaskId(r.getString("task_id"));
				}
				
				if (r.getString("objective_id") != null) {
					cc.setObjectiveId(r.getString("objective_id"));
				}
				
				if (r.getString("prod_service_id") != null) {
					cc.setProdServiceId(r.getString("prod_service_id"));
				}
				
				//if (r.getString("group_id") != null) {
				//	cc.setGroupId(r.getString("group_id"));
				//}
				
				changes.add(cc);
			}
			
		}catch (Exception e)	{
		    SMRCLogger.error(this.getClass().getName() + ".getChanges() ", e);
			throw e;
		}finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}
		
		return changes;
	}
	
	private Customer getCustomer(Connection aConnection, String vcn)throws Exception {
	    
		Customer customer = new Customer();
		String sel = "SELECT * from customer where vista_customer_number = '" + vcn + "'";
		
		Statement stmt = null;
		ResultSet response = null;
		try {
			stmt = aConnection.createStatement();
			response = stmt.executeQuery(sel);
			
			while (response.next())
			{
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
				
				try {
					customer.setStage(response.getInt("sp_stage_id"));
				}
				catch(NullPointerException e) {
					log("NullPointerException for customer in stage id");
				}
				
				try {
					customer.setNumStores(response.getInt("num_stores"));
				}
				catch(NullPointerException e) {
					log("NullPointerException for customer in num stores");
				}
				
				if (response.getString("parent_num") != null) {
					customer.setParent(response.getString("parent_num"));
				}
				
				if (response.getString("sp_geog") != null) {
					customer.setSPGeog(response.getString("sp_geog"));
				}
			}
			
		}catch (Exception e)	{
		    SMRCLogger.error(this.getClass().getName() + ".getCustomer() ", e);
			throw e;
		}finally {
			SMRCConnectionPoolUtils.close(response);
			SMRCConnectionPoolUtils.close(stmt);
		}
		
		return customer;
	}
	
	private String getProductSublineDescription(Connection aConnection,String id)throws Exception {
		String returnVal = "";
		
		String sel = "SELECT description from product_subline where prod_subline_id = '" + id + "'";
		
		Statement stmt = null;
		ResultSet response = null;
		try {
			stmt = aConnection.createStatement();
			response = stmt.executeQuery(sel);
			
			while (response.next())
			{
				returnVal = response.getString("description");
			}
			
		}catch (Exception e)	{
		    SMRCLogger.error(this.getClass().getName() + ".getProductSublineDescription() ", e);
			throw e;
		}finally {
			SMRCConnectionPoolUtils.close(response);
			SMRCConnectionPoolUtils.close(stmt);
		}
		
		return returnVal;
	}

	private String getTaskDescription(Connection aConnection,String id)throws Exception {
		String val = "";
		
		if (!id.equals("")) {
			String sel = "select action from sales_plan_pap where task_id = " + id;
			Statement s = null;
			ResultSet r = null;
			try {
				s = aConnection.createStatement();
				r = s.executeQuery(sel);
				
				while (r.next()) {
					val = r.getString("action");
				}
			}catch (Exception e)	{
			    SMRCLogger.error(this.getClass().getName() + ".getTaskDescription() ", e);
				throw e;
			}finally {
				SMRCConnectionPoolUtils.close(r);
				SMRCConnectionPoolUtils.close(s);
			}
		}
		
		return val;
	}
		
} //class
