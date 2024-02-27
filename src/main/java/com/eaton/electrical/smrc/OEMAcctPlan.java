//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.80  2006/06/05 17:14:27  e0073445
// 4.2.2 - CSF changes for tap dollars
//
// Revision 1.79  2006/03/30 22:21:16  e0073445
// Tap Dollars : Changes for 4.2.3 along with a lot of cleanup including sublines and removing old dollar types that are no longer used
//
// Revision 1.78  2006/03/16 19:13:33  e0073445
// Removed code that was producing warnings.
//
// Revision 1.77  2005/09/19 18:04:14  lubbejd
// More changes for adding attachments to tasks (CR28583). Not quite ready yet.
//
// Revision 1.76  2005/09/08 19:25:32  lubbejd
// More changes for adding attachments to tasks (CR28583)
//
// Revision 1.75  2005/06/01 14:49:56  lubbejd
// Maybe final changes for CR30585 (Change to target project approval process).
// Removed commented out code.
//
// Revision 1.74  2005/05/31 15:25:08  lubbejd
// More changes for CR30585 (Change to target project approval process).
//
// Revision 1.73  2005/05/25 18:36:15  lubbejd
// More changes for CR30585 (Change to target project approval process).
//
// Revision 1.72  2005/05/03 17:39:18  lubbejd
// More changes for adding the pending approval page (CR30290). Also moved
// some methods from OEMAcctPlan to ProjectDAO related to retrieving target
// project information, and move sendNotifications() and sendRejectionNotifications()
// from AccountProfile to Workflow.
//
// Revision 1.71  2005/05/02 19:16:32  lubbejd
// First changes for adding the pending approval page (CR30290). Also moved
// some methods from OEMAcctPlan to ProjectDAO related to retrieving target
// project information.
//
// Revision 1.70  2005/03/09 17:08:22  vendejp
// removed commented code
//
// Revision 1.69  2005/03/07 19:05:59  vendejp
// Changes so user profile has the "View Profiles" ability for helpdesk
//
// Revision 1.68  2005/03/03 22:01:03  vendejp
// changes so that users with multiple sales ids can see all accounts.
//
// Revision 1.67  2005/03/01 15:36:23  vendejp
// Fixed problem with multiple cc users
//
// Revision 1.66  2005/02/11 19:07:27  lubbejd
// Remove calls to MiscDAO.saveAPLog() from everywhere, and added the call
// to SMRCBaseServlet to update with every forward to a jsp
//
// Revision 1.65  2005/02/10 18:43:10  lubbejd
// Changed MiscDAO.saveAPLog() to use User object instead of userid string
// so it can call UserDAO.getGeogForUser for storing the sp_geog on the activity
// log.
//
// Revision 1.64  2005/02/02 15:58:07  lubbejd
// Add export to excel link to product mix page (CR27594)
//
// Revision 1.63  2005/01/27 19:34:36  lubbejd
// Change Product Mix page to only allow users to enter dollars for lower product
// lines, and calculate the totals in summary and total product lines automatically
//
// Revision 1.62  2005/01/21 20:02:15  vendejp
// changes and tweaks for the sending email part of Sales Plan tasks
//
// Revision 1.61  2005/01/14 21:32:58  schweks
// Josh's version from 1.59 again.
//
// Revision 1.59  2005/01/14 20:26:22  vendejp
// Fixed bug where code to update sales plan tasks was commented out.
//
// Revision 1.58  2005/01/13 18:52:36  lubbejd
// Changes to use srYear instead of current date
//
// Revision 1.57  2005/01/12 05:04:07  schweks
// Changed new Integer().intValue() occurrences to Globals.a2int() instead.
//
// Revision 1.56  2005/01/10 22:16:44  vendejp
// added segment override to user profile
//
// Revision 1.55  2005/01/10 03:00:24  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.54  2005/01/09 05:59:55  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.53  2005/01/06 16:19:26  lubbejd
// Changes to use Years table for cust_prod_sales instead of sysdate
//
// Revision 1.52  2005/01/05 22:40:25  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.51  2005/01/05 15:50:56  lubbejd
// TODO comments stating that the usage of sysdate might need to change to using the Years table
//
// Revision 1.50  2004/12/23 18:12:51  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.49  2004/12/15 18:07:43  vendejp
// Fixed some bugs and added segment overrides everywhere.
//
// Revision 1.48  2004/12/09 15:28:37  vendejp
// Changes to the way the User object operates.  Have to explicitly check for security override now.
//
// Revision 1.47  2004/11/24 16:40:09  vendejp
// *** empty log message ***
//
// Revision 1.46  2004/11/17 14:37:46  vendejp
// removed code for segment hierarchy (until it can be revisited later)
//
// Revision 1.45  2004/11/09 06:10:43  vendejp
// *** empty log message ***
//
// Revision 1.44  2004/11/08 22:12:59  vendejp
// *** empty log message ***
//
// Revision 1.43  2004/11/08 21:38:28  vendejp
// *** empty log message ***
//
// Revision 1.42  2004/11/05 20:22:41  lubbejd
// Changes for adding tasks to customer visits
//
// Revision 1.41  2004/11/05 18:43:12  lubbejd
// Moved methods out of OEMAcctPlan to DAOs. Beginning of changes for adding tasks to customer visits.
//
// Revision 1.40  2004/11/05 18:19:09  lubbejd
// manually merged conflicts
//
// Revision 1.39  2004/11/05 17:29:56  vendejp
// changed queries to not use sales_id from the USER table
//
// Revision 1.38  2004/10/30 22:52:42  vendejp
// Changes to accomodate header requirements.  Made User and Account objects set in the header object where I could.
//
// Revision 1.37  2004/10/22 21:21:08  vendejp
// *** empty log message ***
//
// Revision 1.36  2004/10/22 17:02:29  vendejp
// changes for user profile
//
// Revision 1.35  2004/10/21 14:16:32  vendejp
// manually merged Kristers changes with mine
//
// Revision 1.34  2004/10/20 13:24:32  schweks
// Removing unused variables and code.
// Moved final instance variable to doGet().
//
// Revision 1.33  2004/10/19 19:58:17  schweks
// Removed user and userid shared instance variables.
//
// Revision 1.32  2004/10/19 19:42:45  schweks
// Removed OEMAPVars from shared variables.
// Removed more unused code.
//
// Revision 1.31  2004/10/19 18:42:52  schweks
// Cleaned up DBConn instances.
//

package com.eaton.electrical.smrc;


import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.eaton.electrical.smrc.bo.Bid;
import com.eaton.electrical.smrc.bo.Customer;
import com.eaton.electrical.smrc.bo.CustomerProduct;
import com.eaton.electrical.smrc.bo.HeaderBean;
import com.eaton.electrical.smrc.bo.JobType;
import com.eaton.electrical.smrc.bo.OEMAcctPlanBean;
import com.eaton.electrical.smrc.bo.Product;
import com.eaton.electrical.smrc.bo.PurchaseActionProgram;
import com.eaton.electrical.smrc.bo.SPStage;
import com.eaton.electrical.smrc.bo.SalesPlan;
import com.eaton.electrical.smrc.bo.TargetProject;
import com.eaton.electrical.smrc.bo.TargetProjectUpdateEmail;
import com.eaton.electrical.smrc.bo.User;
import com.eaton.electrical.smrc.bo.UserGeogSecurity;
import com.eaton.electrical.smrc.dao.AccountDAO;
import com.eaton.electrical.smrc.dao.MiscDAO;
import com.eaton.electrical.smrc.dao.ProductDAO;
import com.eaton.electrical.smrc.dao.ProjectDAO;
import com.eaton.electrical.smrc.dao.SalesDAO;
import com.eaton.electrical.smrc.dao.SegmentsDAO;
import com.eaton.electrical.smrc.dao.TAPcommon;
import com.eaton.electrical.smrc.dao.UserDAO;
import com.eaton.electrical.smrc.exception.ProfileException;
import com.eaton.electrical.smrc.exception.SMRCException;
import com.eaton.electrical.smrc.service.SMRCConnectionPoolUtils;
import com.eaton.electrical.smrc.service.SMRCLogger;
import com.eaton.electrical.smrc.util.Globals;
import com.eaton.electrical.smrc.util.SMRCSession;
import com.eaton.electrical.smrc.util.StringManipulation;

/**
 * This is the main servlet for the OEM Account Planning application. If this
 * servlet gets too large, break out the logic for each tab into a separate
 * servlet
 * 
 * Yes Carl, it got too big.  Way, way too big.
 * 
 * @author Carl Abel
 * @date June 4, 2001
 *  
 */
public class OEMAcctPlan extends SMRCBaseServlet {

 	private static final long serialVersionUID = 100;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        Connection DBConn = null;
        OEMAcctPlanBean OEMAPVars = null;
        User user = null;
        String sFwdUrl = "/SMRCErrorPage.jsp";
        boolean redirect = false;
        
        try {
            OEMAPVars = new OEMAcctPlanBean();
            DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();

            user = SMRCSession.getUser(request, DBConn);

            // userid is needed some times, so what happens if we just 
            // put it in the request all the time...
            request.setAttribute("userid", user.getUserid());

            sFwdUrl = writeNewPage(request, DBConn, OEMAPVars, user);
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

// Braffet : 20060326 - Changed to an error        		
/*	            SMRCLogger.warn(this.getClass().getName() + 
	            	".doGet(): USERID=" + user.getUserid() + 
					"; MESSAGE=" + pe.getMessage() + 
					"\nUSER:" + user, pe);
					
*/					
        		new SMRCException(this.getClass().getName() + 
	            	".doGet(): USERID=" + user.getUserid() + 
					"; MESSAGE=" + pe.getMessage() + 
					"\nUSER:" + user, pe);
        		
        	} else {
	        	// Put as much on the first line as possible for NetCool

//        		 Braffet : 20060326 - Changed to an error        		
//	            SMRCLogger.warn(this.getClass().getName() + 
//	            	".doGet(): MESSAGE=" + pe.getMessage(), pe);
        		
        		new SMRCException(this.getClass().getName() + 
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
//       		 Braffet : 20060326 - Changed to an error        		
/*	            SMRCLogger.error(this.getClass().getName() + 
	                	".doGet(): USERID=" + user.getUserid() + 
	    				"; MESSAGE=" + e.getMessage() + 
	    				"\nUSER:" + user, e);
*/
        		new SMRCException(this.getClass().getName() + 
	                	".doGet(): USERID=" + user.getUserid() + 
	    				"; MESSAGE=" + e.getMessage() + 
	    				"\nUSER:" + user, e);

	    				
        	} else {
	        	// Put as much on the first line as possible for NetCool
//       		 Braffet : 20060326 - Changed to an error        		
//	            SMRCLogger.error(this.getClass().getName() + 
//	            	".doGet(): MESSAGE=" + e.getMessage(), e);
	            
        		new SMRCException(this.getClass().getName() + 
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

    private void updateUser(Connection aConnection, String userid,
            String over, String set, String vAll, String viewProfiles,
            String[] geogOverrides, String[] levels, String[] viewSalesman,
            String[] update, String[] delete, String newGeogOver,
            String newLevel, String newSlsmn, String newUpdate,String dollarTypeCode)
            throws Exception {

        Statement stmt = null;
        StringBuffer querySB = null;

        try {

            /*
             * UPDATE users SET 
             * override_security = 'str2', set_security = 'str3',
             * view_everything = 'str4' WHERE userid = 'str5'
             */
            querySB = new StringBuffer(500);
            querySB.append("UPDATE users ");
            querySB.append("SET override_security = ");
            querySB.append("'").append(over).append("', "); //'str2',
            querySB.append("set_security = ");
            querySB.append("'").append(set).append("', "); //'str3',
            querySB.append("view_everything = ");
            querySB.append("'").append(vAll).append("', "); //'str4'
            querySB.append("view_profile = ");
            querySB.append("'").append(viewProfiles).append("' "); //'str4'            
            querySB.append("WHERE userid = ");
            querySB.append("'").append(userid).append("' "); //'str5'
           if (SMRCLogger.isDebuggerEnabled()) {

        	   SMRCLogger.debug("\nMethod name: OEMAcctPlan.updateUser(userid...)\nquerySB = "
                       + querySB);

           }

            stmt = aConnection.createStatement();
            stmt.executeUpdate(querySB.toString());

        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".updateUser() - USER table ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(stmt);
        }

        /*
         * INSERTING the records into user_group will allow the user to view
         * other account planners.
         */
        String geog = null;
        String viewSlsmn = null;
        String canUpd = null;
        boolean deleteRec = false;

        if (geogOverrides != null) {

            for (int i = 0; i < geogOverrides.length; i++) {

                geog = geogOverrides[i];
                viewSlsmn = "N";
                canUpd = "N";
                deleteRec = false;

                try {

                    if (viewSalesman != null) {
                        for (int j = 0; j < viewSalesman.length; j++) {
                            if (viewSalesman[j].equals(geog)) {
                                viewSlsmn = "Y";
                            }
                        }
                    }

                    if (update != null) {
                        for (int j = 0; j < update.length; j++) {
                            if (update[j].equals(geog)) {
                                canUpd = "Y";
                            }
                        }
                    }
                    if (delete != null) {
                        for (int j = 0; j < delete.length; j++) {
                            if (delete[j].equals(geog)) {
                                deleteRec = true;
                            }
                        }
                    }

                    if (userGeogSecurityExists(aConnection, userid, geog)) {

                        if (deleteRec) {
                            /*
                             * DELETE user_geog_security WHERE user_level =
                             * 'str1' AND userid = 'str2' AND sp_geog = 'str3'
                             * AND security_type = 'M'
                             * 
                             * Note that security_type = 'M' means manual entry
                             */
                            querySB = new StringBuffer();
                            querySB.append("DELETE user_geog_security ");
                            querySB.append("WHERE user_level = ");
                            querySB.append("'").append(levels[i]).append("' "); //'str1'
                            querySB.append("AND userid = ");
                            querySB.append("'").append(userid).append("' "); //'str2'
                            querySB.append("AND sp_geog = ");
                            querySB.append("'").append(geog).append("' "); //'str3'
                            querySB.append("AND security_type = 'M'");
 
                            if (SMRCLogger.isDebuggerEnabled()) {
                            	
                                SMRCLogger.debug("\nMethod name: OEMAcctPlan.updateUser(userid...)\nquerySB = "
                                        + querySB);                           	
                         
                            }

                        } else {
                            /*
                             * UPDATE user_geog_security SET view_salesman =
                             * 'str1', able_to_Update = 'str2', user_level =
                             * 'str3' WHERE userid = 'str4' AND sp_geog = 'str5'
                             * AND security_type = 'M'
                             * 
                             * Note that security_type = 'M' means manual entry
                             */
                            querySB = new StringBuffer();
                            querySB.append("UPDATE user_geog_security ");
                            querySB.append("SET view_salesman = ");
                            querySB.append("'").append(viewSlsmn).append("', "); //'str1',
                            querySB.append("able_to_Update = ");
                            querySB.append("'").append(canUpd).append("', "); //'str2',
                            querySB.append("user_level = ");
                            querySB.append("'").append(levels[i]).append("' "); //'str3'
                            querySB.append("WHERE userid = ");
                            querySB.append("'").append(userid).append("' "); //'str4'
                            querySB.append("AND sp_geog = ");
                            querySB.append("'").append(geog).append("' "); //'str5'
                            querySB.append("AND security_type = 'M'");
                            
                            if (SMRCLogger.isDebuggerEnabled()) {
                            	
                            	SMRCLogger.debug("\nMethod name: OEMAcctPlan.updateUser(userid...)\nquerySB = "
                                                + querySB);
                            	
                            }
 
                        } //if (deleteRec)

                        stmt = aConnection.createStatement();
                        stmt.executeUpdate(querySB.toString());
                        if (SMRCLogger.isDebuggerEnabled()) {
                        	
                           SMRCLogger.debug("Debug: querySB = " + querySB);
                           
                        }

                    } //if (userGeogSecurityExists)

                } catch (Exception e) {
                	
                	throw new SMRCException(this.getClass().getName()
                            + ".updateUser() - USER_GEOG_SECURITY table ", e);
                 } finally {
                    SMRCConnectionPoolUtils.close(stmt);
                }

            } //for (int i=0; i < geogOverrides.length; i++)

        } //if (geogOverrides != null)

        if (newGeogOver != null && !newGeogOver.equals("")) {
            String newVS = "N";
            String newUpd = "N";

            if (newSlsmn != null && newSlsmn.equals("new")) {
                newVS = "Y";
            }

            if (newUpdate != null && newUpdate.equals("new")) {
                newUpd = "Y";
            }

            if (!userGeogSecurityExists(aConnection, userid, newGeogOver)) {

                /*
                 * INSERT INTO user_geog_security (userid, sp_geog, user_level,
                 * view_salesman, able_to_update, security_type) VALUES
                 * ('str1','str2','str3','str4','str5','M')
                 * 
                 * Note that security_type = 'M' means manual entry
                 */
                querySB = new StringBuffer(250);
                querySB
                        .append("INSERT INTO user_geog_security (userid, sp_geog, user_level, view_salesman, able_to_update, security_type) ");
                querySB.append("VALUES ");
                querySB.append("("); //begin set of values
                querySB.append("'").append(userid).append("',"); //'str1',
                querySB.append("'").append(newGeogOver.toUpperCase()).append(
                        "',"); //'str2',
                querySB.append("'").append(newLevel).append("',"); //'str3',
                querySB.append("'").append(newVS).append("',"); //'str4',
                querySB.append("'").append(newUpd).append("',"); //'str5',
                querySB.append("'").append('M').append("')"); //'M'
                if (SMRCLogger.isDebuggerEnabled()) {
                	SMRCLogger.debug("\nMethod name: OEMAcctPlan.updateUser(userid...)\nquerySB = " + querySB);                	
                }

                try {
                    stmt = aConnection.createStatement();
                    stmt.executeUpdate(querySB.toString());
                    if (SMRCLogger.isDebuggerEnabled()) {
                        SMRCLogger.debug("Debug: querySB = " + querySB);
                    }
                } catch (Exception e) {
                	
                	throw new SMRCException(this.getClass().getName()
                            + ".updateUser()  - USER_GEOG_SECURITY table 2nd time ", e);
                } finally {
                    SMRCConnectionPoolUtils.close(stmt);
                }

            }
        }


    } //method

    private boolean userGeogSecurityExists(Connection aConnection, String userid, String geog)
            throws Exception {

        Statement stmt = null;
        ResultSet rs = null;
        StringBuffer querySB = null;
        boolean exists = false;

        try {
            // SELECT * FROM user_geog_security WHERE userid = 'str1' AND
            // sp_geog = 'str2' AND security_type = 'M'
            querySB = new StringBuffer(100);
            querySB.append("SELECT * FROM user_geog_security ");
            querySB.append("WHERE userid = ");
            querySB.append("'").append(userid).append("' "); //'str1'
            querySB.append("AND sp_geog = ");
            querySB.append("'").append(geog).append("'"); //'str2'
            querySB.append("AND security_type = ");
            querySB.append("'").append("M").append("'"); //'M'
            if (SMRCLogger.isDebuggerEnabled()) {
                SMRCLogger.debug("\nMethod name: OEMAcctPlan.userGeogSecurityExists(userid, geog)\nquerySB = "
                                + querySB);
            }

            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(querySB.toString());

            if (rs.next()) {
                exists = true;
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".userGeogSecurityExists() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }
        return exists;
    }
 
    
    private SPStage getCustomerStage(Connection aConnection, String cust) throws Exception {

        //Vince: this should be one query with a join of two tables - customer
        // and sales_plan_stages
        //       if there is a FK relationship between the respective tables and their
        // sp_stage_id values.

        Statement stmt = null;
        ResultSet rs = null;
        StringBuffer querySB = null;
        SPStage spStage = new SPStage();

        try {
            querySB = new StringBuffer(100);
            querySB.append("SELECT a.sp_stage_id, b.description FROM customer a, sales_plan_stages b ");
            querySB.append("WHERE a.sp_stage_id=b.sp_stage_id(+) and a.vista_customer_number = ");
            querySB.append("'").append(cust).append("'");
            if (SMRCLogger.isDebuggerEnabled()) {
                SMRCLogger.debug("\nMethod name: OEMAcctPlan.getCustomerStage(cust)\nquerySB = "
                        + querySB);
            }
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(querySB.toString());

            if(rs.next()){
	            spStage.setId(rs.getInt("sp_stage_id"));
	            spStage.setDescription(StringManipulation.noNull(rs.getString("description")));
            }
        } catch (Exception e) {
            SMRCLogger.error(
                    this.getClass().getName() + ".getCustomerStage() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }
        return spStage;

 

    } //method

    private ArrayList getSalesPlanStages(Connection aConnection) throws Exception {

        Statement stmt = null;
        ResultSet rs = null;
        String queryString = null;
        ArrayList spStages = new ArrayList(10);
        SPStage spStage = null;

        //top-down flow control. If error gets set to true, we leave the
        // method.
        boolean error = false;

        try {
            //"SELECT * FROM sales_plan_stages ORDER BY sp_stage_id ASC";
            queryString = "SELECT * FROM sales_plan_stages ORDER BY sp_stage_id ASC";
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(queryString);

            while (rs.next()) {
                spStage = new SPStage();
                spStage.setId(rs.getInt("sp_stage_id"));
                spStage.setDescription(rs.getString("description"));
                spStages.add(spStage);
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".getSalesPlanStages() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return error ? null : spStages;

    } //method

    private SalesPlan getSalesPlan(Connection aConnection, String vcn) throws Exception {

        Statement stmt = null;
        ResultSet rs = null;
        StringBuffer querySB = null;
        SalesPlan plan = new SalesPlan();

        //top-down flow control. If error gets set to true, we leave the
        // method.
        boolean error = false;

        try {
            /*
             * SELECT * FROM sales_plans WHERE vista_customer_number = 'str1'
             */
            querySB = new StringBuffer(100);
            querySB.append("SELECT * FROM sales_plans ");
            querySB.append("WHERE vista_customer_number = ");
            querySB.append("'").append(vcn).append("'"); //'str1'
            if (SMRCLogger.isDebuggerEnabled()) {
                SMRCLogger.debug("\nMethod name: OEMAcctPlan.getSalesPlan(vcn)\nquerySB = "
                        + querySB);
            }
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(querySB.toString());

            while (rs.next()) {
                plan.setVistaCustNum(rs.getString("vista_customer_number"));
                plan.setCompetitiveProdsAndPositions(StringManipulation.noNull(rs.getString("compet_prods_positions")));
                plan.setObjectiveResponse(StringManipulation.noNull(rs.getString("sales_plan_objective_response")));

                
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getSalesPlan() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return error ? null : plan;

    } //method

    private CustomerProduct getCustomerProduct(Connection aConnection, User user, String vcn, String product, int srYear, int srMonth)
            throws Exception {

        Statement stmt = null;
        ResultSet rs = null;
        StringBuffer querySB = null;
        CustomerProduct custProd = new CustomerProduct();

        try {
            /*
             * SELECT vista_customer_number, product_id,
             * nvl(potential_dollars,0) potential_dollars,
             * nvl(competitor_dollars,0) competitor_dollars,
             * nvl(competitor2_dollars,0)competitor2_dollars,
             * nvl(forecast_dollars,0) forecast_dollars, nvl(volume,0) volume,
             * notes, competitor1_id, competitor2_id FROM customer_product WHERE
             * vista_customer_number = 'str1' AND product_id = 'str2'
             */
            querySB = new StringBuffer(500);
            querySB
                    .append("SELECT vista_customer_number, product_id, nvl(potential_dollars,0) potential_dollars, nvl(competitor_dollars,0) competitor_dollars, nvl(competitor2_dollars,0) competitor2_dollars, nvl(forecast_dollars,0) forecast_dollars, nvl(volume,0) volume, notes, competitor1_id, competitor2_id ");
            querySB.append("FROM customer_product ");
            querySB.append("WHERE vista_customer_number = ");
            querySB.append("'").append(vcn).append("' "); //'str1'
            querySB.append("AND product_id = ");
            querySB.append("'").append(product).append("'"); //'str2'
            if (SMRCLogger.isDebuggerEnabled()) {
                SMRCLogger.debug("\nMethod name: OEMAcctPlan.getCustomerProduct(vcn, product)\nquerySB = "
                        + querySB);
            }
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(querySB.toString());

            while (rs.next()) {
                custProd.setCustomer(rs
                        .getString("vista_customer_number"));
                custProd.setProduct(rs.getString("product_id"));
                custProd.setPotentialDollars(rs
                        .getFloat("potential_dollars"));
                custProd.setCompetitorDollars(rs
                        .getFloat("competitor_dollars"));
                custProd.setCompetitor2Dollars(rs
                        .getFloat("competitor2_dollars"));
                custProd.setCompetitorName(rs.getInt("competitor1_id"));
                custProd.setCompetitor2Name(rs.getInt("competitor2_id"));
                custProd.setForecastDollars(rs
                        .getFloat("forecast_dollars"));
                custProd.setVolume(rs.getInt("volume"));
                custProd.setNotes(rs.getString("notes") == null ? ""
                        : rs.getString("notes"));

                custProd.setSells(true);
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".getCustomerProduct() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        try {

            querySB = new StringBuffer(2000);
            querySB.append("SELECT type, otd, itd ");
            querySB.append("FROM ");
            querySB.append("( "); //begin sub-query
            querySB.append("      SELECT 'ly' type, ");
            
            querySB.append("        sum(nvl(order_tap_dollars, 0)) otd, ");
            querySB.append("        sum(nvl(invoice_tap_dollars, 0)) itd ");
            querySB.append("		FROM tap_dollars td ");
            querySB.append("        WHERE td.product_id = ").append("'").append(product).append("'"); //'str1'
            querySB.append("         AND td.vista_customer_number = ").append("'")
            .append(vcn).append("'"); //'str2'
            querySB.append(" and td.year = " + (srYear - 1));
            querySB.append("       UNION ");
            querySB.append("      SELECT '2ly' type, ");
            querySB.append("        sum(nvl(order_tap_dollars, 0)) otd, ");
            querySB.append("        sum(nvl(invoice_tap_dollars, 0)) itd ");
            querySB.append("                  FROM tap_dollars td ");

            querySB.append("       WHERE td.product_id = ").append("'").append(
                    product).append("'"); //'str1'
            querySB.append("         AND td.vista_customer_number = ").append("'")
            .append(vcn).append("'"); //'str2'
            querySB.append("         AND td.year = " + (srYear - 2));
            querySB.append("       UNION ");
            querySB.append("      SELECT 'ytd' type, ");
            // From tap_dollars 
            querySB.append("        sum(nvl(order_tap_dollars, 0)) otd, ");
            querySB.append("        sum(nvl(invoice_tap_dollars, 0)) itd ");
            
            querySB.append("                  FROM tap_dollars td ");

            querySB.append("       WHERE td.product_id = ").append("'").append(
                    product).append("'"); //'str1'
             querySB.append("         AND td.vista_customer_number = ").append("'")
            .append(vcn).append("'"); //'str2'
            querySB.append("         AND td.year = " + srYear);
            querySB.append("       UNION ");
            querySB.append("      SELECT 'lytd' type, ");
            // From tap_dollars 
            querySB.append("        sum(nvl(order_tap_dollars, 0)) otd, ");
            querySB.append("        sum(nvl(invoice_tap_dollars, 0)) itd ");
            
            querySB.append("                  FROM tap_dollars td");

            querySB.append("       WHERE td.product_id = ").append("'").append(
                    product).append("'"); //'str1'
            querySB.append("         AND td.vista_customer_number = ").append("'")
            .append(vcn).append("'"); //'str2'
            querySB.append("         AND td.year =  " + (srYear - 1));
            querySB.append("         AND td.month <=  " + srMonth);
            querySB.append(")"); //end sub-query

            if (SMRCLogger.isDebuggerEnabled()) {
                SMRCLogger.debug("\nMethod name: OEMAcctPlan.getCustomerProduct(vcn, product)querySB = "
                        + querySB);
            }
 
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(querySB.toString());

            while (rs.next()) {
                String type = rs.getString("type");

                if (type.equals("ly")) {
//                    custProd.setCreditLYDollars(rs.getFloat("cred"));
//                    custProd.setEndMktLYDollars(rs.getFloat("em"));
                    custProd.setTapDollarsInvoiceLY(rs.getFloat("itd"));
                    custProd.setTapDollarsOrderLY(rs.getFloat("otd"));
//                    custProd.setDirectLYDollars(rs.getFloat("dir"));
//                    custProd.setSSOLYDollars(rs.getFloat("sso"));
                } else if (type.equals("2ly")) {
                    custProd.setTapDollarsInvoice2LY(rs.getFloat("itd"));
                    custProd.setTapDollarsOrder2LY(rs.getFloat("otd"));
//                    custProd.setCredit2LYDollars(rs.getFloat("cred"));
//                   custProd.setEndMkt2LYDollars(rs.getFloat("em"));
//                    custProd.setDirect2LYDollars(rs.getFloat("dir"));
//                    custProd.setSSO2LYDollars(rs.getFloat("sso"));
                } else if (type.equals("lytd")) {
                    custProd.setTapDollarsInvoiceLYTD(rs.getFloat("itd"));
                    custProd.setTapDollarsOrderLYTD(rs.getFloat("otd"));
//                    custProd.setCreditLYTDDollars(rs.getFloat("cred"));
//                    custProd.setEndMktLYTDDollars(rs.getFloat("em"));
//                    custProd.setDirectLYTDDollars(rs.getFloat("dir"));
//                    custProd.setSSOLYTDDollars(rs.getFloat("sso"));
                } else if (type.equals("ytd")) {
//                    custProd.setCreditYTDDollars(rs.getFloat("cred"));
//                    custProd.setEndMktYTDDollars(rs.getFloat("em"));
                    custProd.setTapDollarsInvoiceYTD(rs.getFloat("itd"));
                    custProd.setTapDollarsOrderYTD(rs.getFloat("otd"));
//                    custProd.setDirectYTDDollars(rs.getFloat("dir"));
//                    custProd.setSSOYTDDollars(rs.getFloat("sso"));
                }
            }

        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".getCustomerProduct() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }
        return custProd;

    } //method
/*
    private CustProdSubline getCustomerProductSubline(Connection aConnection, User user, String vcn, String product)
            throws Exception {

        CustProdSubline cps = new CustProdSubline();
        String sel = "SELECT * from customer_product_subline "
                + "WHERE vista_customer_number = '" + vcn + "' "
                + "AND prod_subline_id = '" + product + "'";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                cps.setCustomer(rs.getString("vista_customer_number"));
                cps.setProductSubline(rs.getString("prod_subline_id"));
                cps.setPotentialDollars(rs.getFloat("potential_dollars"));
                cps.setCompetitorDollars(rs
                        .getFloat("competitor_dollars"));
                cps.setCompetitor2Dollars(rs
                        .getFloat("competitor_dollars"));
                cps.setForecastDollars(rs.getFloat("forecast_dollars"));
                cps.setVolume(rs.getInt("volume"));

                if (rs.getString("notes") != null) {
                    cps.setNotes(rs.getString("notes"));
                } else {
                    cps.setNotes("");
                }

                cps.setSells(true);
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".getCustomerProductSubline() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        sel = "";

        if (user.getShowSalesOrders().equals("s")) {
        	// TODO May need to use Years table here
            sel = "select type,cred,em,dir "
                    + " from  "
                    + " ( "
                    + " select 'ly' type, sum(nvl(credit_sales,0)) cred,sum(nvl(end_mkt_sales,0)) em,sum(nvl(direct_sales,0)) dir "
                    + " from cust_prod_subline_sales "
                    + " where prod_subline_id = '"
                    + product
                    + "' "
                    + " and vista_customer_number = '"
                    + vcn
                    + "' "
                    + " and year = to_char(add_months(sysdate,-12),'YYYY') "
                    + " union "
                    + " select '2ly' type, sum(nvl(credit_sales,0)) cred,sum(nvl(end_mkt_sales,0)) em,sum(nvl(direct_sales,0)) dir "
                    + " from cust_prod_subline_sales "
                    + " where prod_subline_id = '"
                    + product
                    + "' "
                    + " and vista_customer_number = '"
                    + vcn
                    + "' "
                    + " and year = to_char(add_months(sysdate,-24),'YYYY') "
                    + " union "
                    + " select 'ytd' type, sum(nvl(credit_sales,0)) cred,sum(nvl(end_mkt_sales,0)) em,sum(nvl(direct_sales,0)) dir "
                    + " from cust_prod_subline_sales "
                    + " where prod_subline_id = '"
                    + product
                    + "' "
                    + " and vista_customer_number = '"
                    + vcn
                    + "' "
                    + " and year = to_char(sysdate,'YYYY') "
                    + " union "
                    + " select 'lytd' type, sum(nvl(credit_sales,0)) cred,sum(nvl(end_mkt_sales,0)) em,sum(nvl(direct_sales,0)) dir "
                    + " from cust_prod_subline_sales "
                    + " where prod_subline_id = '" + product + "' "
                    + " and vista_customer_number = '" + vcn + "' "
                    + " and year = to_char(add_months(sysdate,-12),'YYYY') "
                    + " and month <= to_char(sysdate,'MM') " + " )";
        } else {
        	// TODO May need to use Years table here
            sel = "select type,cred,em,dir "
                    + " from  "
                    + " ( "
                    + " select 'ly' type, sum(nvl(credit_order,0)) cred,sum(nvl(end_mkt_order,0)) em,sum(nvl(direct_order,0)) dir "
                    + " from cust_prod_subline_sales "
                    + " where prod_subline_id = '"
                    + product
                    + "' "
                    + " and vista_customer_number = '"
                    + vcn
                    + "' "
                    + " and year = to_char(add_months(sysdate,-12),'YYYY') "
                    + " union "
                    + " select '2ly' type, sum(nvl(credit_order,0)) cred,sum(nvl(end_mkt_order,0)) em,sum(nvl(direct_order,0)) dir "
                    + " from cust_prod_subline_sales "
                    + " where prod_subline_id = '"
                    + product
                    + "' "
                    + " and vista_customer_number = '"
                    + vcn
                    + "' "
                    + " and year = to_char(add_months(sysdate,-24),'YYYY') "
                    + " union "
                    + " select 'ytd' type, sum(nvl(credit_order,0)) cred,sum(nvl(end_mkt_order,0)) em,sum(nvl(direct_order,0)) dir "
                    + " from cust_prod_subline_sales "
                    + " where prod_subline_id = '"
                    + product
                    + "' "
                    + " and vista_customer_number = '"
                    + vcn
                    + "' "
                    + " and year = to_char(sysdate,'YYYY') "
                    + " union "
                    + " select 'lytd' type, sum(nvl(credit_order,0)) cred,sum(nvl(end_mkt_order,0)) em,sum(nvl(direct_order,0)) dir "
                    + " from cust_prod_subline_sales "
                    + " where prod_subline_id = '" + product + "' "
                    + " and vista_customer_number = '" + vcn + "' "
                    + " and year = to_char(add_months(sysdate,-12),'YYYY') "
                    + " and month <= to_char(sysdate,'MM') " + " )";
        }

        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                String type = rs.getString("type");

                if (type.equals("ly")) {
                    cps.setCreditLYDollars(rs.getFloat("cred"));
                    cps.setEndMktLYDollars(rs.getFloat("em"));
                    cps.setDirectLYDollars(rs.getFloat("dir"));
                } else if (type.equals("2ly")) {
                    cps.setCredit2LYDollars(rs.getFloat("cred"));
                    cps.setEndMkt2LYDollars(rs.getFloat("em"));
                    cps.setDirect2LYDollars(rs.getFloat("dir"));
                } else if (type.equals("lytd")) {
                    cps.setCreditLYTDDollars(rs.getFloat("cred"));
                    cps.setEndMktLYTDDollars(rs.getFloat("em"));
                    cps.setDirectLYTDDollars(rs.getFloat("dir"));
                } else if (type.equals("ytd")) {
                    cps.setCreditYTDDollars(rs.getFloat("cred"));
                    cps.setEndMktYTDDollars(rs.getFloat("em"));
                    cps.setDirectYTDDollars(rs.getFloat("dir"));
                }
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".getCustomerProductSubline() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }
        return cps;

    } //method
*/
    private String fixQuotes(String in) {

        StringBuffer newString = new StringBuffer("");

        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);

            if (c == '\'') {
                newString.append("''");
            } else {
                newString.append(c);
            }
        }

        return newString.toString();

    } //method



    private void updatePlan(Connection aConnection, SalesPlan plan, User user) throws Exception {

        String upd = "UPDATE sales_plans " + "SET compet_prods_positions = '"
                + fixQuotes(plan.getCompetitiveProdsAndPositions()) + "', "
                + "sales_plan_objective_response = '" + fixQuotes(plan.getObjectiveResponse()) + "', "
				+ "user_changed = '" + user.getUserid() + "', "
                + " date_changed = sysdate "
                + "WHERE vista_customer_number = '" + plan.getVistaCustNum()
                + "'";
        Statement stmt = null;
        try {
            stmt = aConnection.createStatement();
            stmt.executeUpdate(upd);
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".updatePlan() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(stmt);
        }


    } //method

    private void insertPlan(Connection aConnection, User user, String cust, String cpap,String objectiveResponse)
            throws Exception {

        if (cpap == null || cpap.length() <= 0) {
            cpap = " ";
        }

        String upd = "INSERT INTO sales_plans (vista_customer_number,compet_prods_positions,sales_plan_objective_response,date_added,date_changed,user_added,user_changed)"
                + "VALUES('"
                + cust
                + "','"
                + fixQuotes(cpap)
                + "','"
                + fixQuotes(objectiveResponse)				
                + "',sysdate,sysdate,'"
                + user.getUserid()
                + "','"
                + user.getUserid() + "')";
        Statement stmt = null;
        try {
            stmt = aConnection.createStatement();
            stmt.executeUpdate(upd);
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".insertPlan() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(stmt);
        }


    } //method

    private boolean salesPlanExists(Connection aConnection, String cust) throws Exception {

        boolean exists = false;
        String sel = "SELECT count(*) from sales_plans where vista_customer_number = '"
                + cust + "'";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                if (rs.getInt(1) > 0) {
                    exists = true;
                }
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".salesPlanExists() ",
                    e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return exists;

    } //method

    private void updateStage(HttpServletRequest request, Connection aConnection, String userid)
            throws Exception {

        int stage = Globals.a2int(request.getParameter("spstage"));

        String upd = "";

        if (stage == 0) {
            upd = "UPDATE customer" + " SET sp_stage_id = null, "
                    + " user_changed = '" + userid + "', "
                    + " date_changed = sysdate "
                    + " WHERE vista_customer_number = '"
                    + request.getParameter("cust") + "'";
        } else {
            upd = "UPDATE customer" + " SET sp_stage_id = " + stage
                    + ", " + " user_changed = '" + userid + "', "
                    + " date_changed = sysdate "
                    + " WHERE vista_customer_number = '"
                    + request.getParameter("cust") + "'";
        }
        Statement stmt = null;
        try {
            stmt = aConnection.createStatement();
            stmt.executeUpdate(upd);
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".updateStage() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(stmt);
        }

    } //method

    private void updateSalesPlan(HttpServletRequest request, Connection aConnection, User user)
            throws Exception {

    	if (!salesPlanExists(aConnection, request.getParameter("cust"))) {
            insertPlan(aConnection, user, request.getParameter("cust"), 
                    request.getParameter("compet"),request.getParameter("objective_response"));
            updateStage(request, aConnection, user.getUserid());
            AccountDAO.updatePAP(request, aConnection, user.getUserid());

        } else {
            SalesPlan plan = new SalesPlan();

            plan.setVistaCustNum(request.getParameter("cust"));
            plan.setCompetitiveProdsAndPositions(request.getParameter("compet"));
            plan.setObjectiveResponse(request.getParameter("objective_response"));

            updatePlan(aConnection, plan, user);
            updateStage(request, aConnection, user.getUserid());
            AccountDAO.updatePAP(request, aConnection, user.getUserid());


        }
        
    	// Delete temporary attachment files created
    	// Do this last because all temporary files, including those copied to database
    	// are to be deleted
    	if (request.getParameter("deleteTempFile") != null){
    		String [] values = request.getParameterValues("deleteTempFile");
    		for (int i=0; i < values.length; i++){
    			File deleteFile = new File(values[i]);
    			deleteFile.delete();
    		}
    	}

    } //method

    private boolean updateCustomerProducts(HttpServletRequest request, Connection aConnection, User user)
            throws Exception {

        HttpSession session = request.getSession(false);
        int srYear = Globals.a2int((String) session.getAttribute("srYear"));
        int srMonth = Globals.a2int((String) session.getAttribute("srMonth"));
        ArrayList allProds = ProductDAO.getProductsCurrentYear(srYear, aConnection);
        
        for (int i = 0; i < allProds.size(); i++) {
            Product product = (Product) allProds.get(i);
            String potentialParm = request.getParameter("pot_"
                    + product.getId());
            String competitorParm = request.getParameter("comp_"
                    + product.getId());
            String competitor2Parm = request.getParameter("comp2_"
                    + product.getId());
            String competitorNameParm = request.getParameter("comp_name_"
                    + product.getId());
            String competitor2NameParm = request.getParameter("comp2_name_"
                    + product.getId());
            String forecastParm = request.getParameter("forecast_"
                    + product.getId());
            String volumeParm = request.getParameter("volume_"
                    + product.getId());
            String notes = request.getParameter("comment_" + product.getId());

            //			Set blanks = 0
            if (potentialParm == null) {
                potentialParm = "0";
            } else if (potentialParm.equals("null")) {
                potentialParm = "0";
            } else if (potentialParm.length() <= 0) {
                potentialParm = "0";
            }
            
            if (competitorParm == null) {
                competitorParm = "0";
            } else if (competitorParm.equals("null")) {
                competitorParm = "0";
            } else if (competitorParm.length() <= 0) {
                competitorParm = "0";
            }

            if (competitor2Parm == null) {
                competitor2Parm = "0";
            } else if (competitor2Parm.equals("null")) {
                competitor2Parm = "0";
            } else if (competitor2Parm.length() <= 0) {
                competitor2Parm = "0";
            }
            
            if (competitorNameParm == null) {
                competitorNameParm = "0";
            } else if (competitorNameParm.equals("null")) {
                competitorNameParm = "0";
            } else if (competitorNameParm.length() <= 0) {
                competitorNameParm = "0";
            }

            if (competitor2NameParm == null) {
                competitor2NameParm = "0";
            } else if (competitor2NameParm.equals("null")) {
                competitor2NameParm = "0";
            } else if (competitor2NameParm.length() <= 0) {
                competitor2NameParm = "0";
            }

            if (forecastParm == null) {
                forecastParm = "0";
            } else if (forecastParm.equals("null")) {
                forecastParm = "0";
            } else if (forecastParm.length() <= 0) {
                forecastParm = "0";
            }

            if (volumeParm == null) {
                volumeParm = "0";
            } else if (volumeParm.equals("null")) {
                volumeParm = "0";
            } else if (volumeParm.length() <= 0) {
                volumeParm = "0";
            }

            if (notes == null) {
                notes = "";
            } else if (notes.equals("null")) {
                notes = "";
            }

            Float potential = new Float(StringManipulation.removeCurrency(potentialParm));
            Float forecast = new Float(StringManipulation.removeCurrency(forecastParm));
            Float competitor = new Float(StringManipulation.removeCurrency(competitorParm));
            Float competitor2 = new Float(StringManipulation.removeCurrency(competitor2Parm));
            
            
            //			Remove commas from volume
            StringTokenizer removeCommas = new StringTokenizer(volumeParm, ",");
            StringBuffer noComma = new StringBuffer();

            if (!removeCommas.hasMoreElements()) {
                noComma.append(volumeParm);
            }

            while (removeCommas.hasMoreElements()) {
                noComma.append(removeCommas.nextElement());
            }

            int volume = Globals.a2int(noComma.toString());

            String upd = "";
            CustomerProduct test = getCustomerProduct(aConnection, user,
                    request.getParameter("cust"), product.getId(), srYear, srMonth);
            Calendar now = Calendar.getInstance();

            if (competitorNameParm.equals("0")) {
                competitorNameParm = null;
            }
            if (competitor2NameParm.equals("0")) {
                competitor2NameParm = null;
            }

            if (test.getProduct() == null
                    || !test.getProduct().equals(product.getId())) {
                //				record not on db
                upd = "INSERT INTO customer_product "
                        + "(vista_customer_number,product_id,potential_dollars,competitor_dollars,competitor2_dollars,competitor1_id, competitor2_id,notes,date_added,date_changed,forecast_dollars,volume,period_yyyy) "
                        + "VALUES ('" + request.getParameter("cust") + "','"
                        + product.getId() + "'," + potential.floatValue() + ","
                        + competitor.floatValue() + ","
                        + competitor2.floatValue() + "," + competitorNameParm
                        + "," + competitor2NameParm + ",'" + fixQuotes(notes)
                        + "',sysdate,sysdate," + forecast.floatValue() + ","
                        + volume + "," + now.get(Calendar.YEAR)
                        + ")";
            } else {
                upd = "update customer_product set " + " potential_dollars = "
                        + potential.floatValue() + ","
                        + " competitor_dollars = " + competitor.floatValue()
                        + "," + " competitor2_dollars = "
                        + competitor2.floatValue() + "," + " competitor1_id = "
                        + competitorNameParm + "," + " competitor2_id = "
                        + competitor2NameParm + "," + " notes = '"
                        + fixQuotes(notes) + "'," + " forecast_dollars = "
                        + forecast.floatValue() + "," + " volume = "
                        + volume + "," + " period_yyyy = "
                        + now.get(Calendar.YEAR) + ", date_changed = sysdate "
                        + " WHERE vista_customer_number = '"
                        + request.getParameter("cust") + "' "
                        + " AND product_id = '" + product.getId() + "'";
            }

            if (SMRCLogger.isDebuggerEnabled()) {
            	
            	SMRCLogger.debug(this.getClass().getName()
                + ".updateCustomerProducts() sql : " + upd);
            	
            }
            
            Statement stmt = null;
            try {
                stmt = aConnection.createStatement();
                stmt.executeUpdate(upd);
            } catch (Exception e) {
                throw new SMRCException (this.getClass().getName()
                        + ".updateCustomerProducts() ", e);
            } finally {
                SMRCConnectionPoolUtils.close(stmt);
            }

            /*
             * 	Braffet : 20060330 - The product subline functionality was removed a ways back
             * 
           while (product.moreSublines()) {
                ProductSubline ps = product.getNextSubline();
                potentialParm = request.getParameter("ps_pot_" + ps.getId());
                competitorParm = request.getParameter("ps_comp_" + ps.getId());
                competitor2Parm = request
                        .getParameter("ps_comp2_" + ps.getId());
                competitorNameParm = request.getParameter("ps_comp_name_"
                        + ps.getId());
                competitor2NameParm = request.getParameter("ps_comp2_name_"
                        + ps.getId());
                forecastParm = request
                        .getParameter("ps_forecast_" + ps.getId());
                volumeParm = request.getParameter("ps_volume_" + ps.getId());
                notes = request.getParameter("ps_comment_" + ps.getId());

                //				Set blanks = 0
                if (potentialParm == null) {
                    potentialParm = "0";
                } else if (potentialParm.equals("null")) {
                    potentialParm = "0";
                } else if (potentialParm.length() <= 0) {
                    potentialParm = "0";
                }

                if (competitorParm == null) {
                    competitorParm = "0";
                } else if (competitorParm.equals("null")) {
                    competitorParm = "0";
                } else if (competitorParm.length() <= 0) {
                    competitorParm = "0";
                }

                if (competitor2Parm == null) {
                    competitor2Parm = "0";
                } else if (competitor2Parm.equals("null")) {
                    competitor2Parm = "0";
                } else if (competitor2Parm.length() <= 0) {
                    competitor2Parm = "0";
                }

                if (forecastParm == null) {
                    forecastParm = "0";
                } else if (forecastParm.equals("null")) {
                    forecastParm = "0";
                } else if (forecastParm.length() <= 0) {
                    forecastParm = "0";
                }

                if (volumeParm == null) {
                    volumeParm = "0";
                } else if (volumeParm.equals("null")) {
                    volumeParm = "0";
                } else if (volumeParm.length() <= 0) {
                    volumeParm = "0";
                }

                if (notes == null) {
                    notes = "";
                } else if (notes.equals("null")) {
                    notes = "";
                }

                //				Remove commas from potential dollars
                removeCommas = new StringTokenizer(potentialParm, ",");
                noComma = new StringBuffer();

                if (!removeCommas.hasMoreElements()) {
                    noComma.append(potentialParm);
                }

                while (removeCommas.hasMoreElements()) {
                    noComma.append(removeCommas.nextElement());
                }

                potential = new Float(noComma.toString());

                //				Remove commas from competitor dollars
                removeCommas = new StringTokenizer(competitorParm, ",");
                noComma = new StringBuffer();

                if (!removeCommas.hasMoreElements()) {
                    noComma.append(competitorParm);
                }

                while (removeCommas.hasMoreElements()) {
                    noComma.append(removeCommas.nextElement());
                }

                competitor = new Float(noComma.toString());

                //				Remove commas from competitor2 dollars
                removeCommas = new StringTokenizer(competitor2Parm, ",");
                noComma = new StringBuffer();

                if (!removeCommas.hasMoreElements()) {
                    noComma.append(competitor2Parm);
                }

                while (removeCommas.hasMoreElements()) {
                    noComma.append(removeCommas.nextElement());
                }

                competitor2 = new Float(noComma.toString());

                //				Remove commas from forecast dollars
                removeCommas = new StringTokenizer(forecastParm, ",");
                noComma = new StringBuffer();

                if (!removeCommas.hasMoreElements()) {
                    noComma.append(forecastParm);
                }

                while (removeCommas.hasMoreElements()) {
                    noComma.append(removeCommas.nextElement());
                }

                forecast = new Float(noComma.toString());

                //				Remove commas from volume
                removeCommas = new StringTokenizer(volumeParm, ",");
                noComma = new StringBuffer();

                if (!removeCommas.hasMoreElements()) {
                    noComma.append(volumeParm);
                }

                while (removeCommas.hasMoreElements()) {
                    noComma.append(removeCommas.nextElement());
                }

                volume = Globals.a2int(noComma.toString());

 //               CustProdSubline tst = getCustomerProductSubline(aConnection, user, 
 //                       request.getParameter("cust"), ps.getId());
                upd = "";

                if (competitorNameParm.equals("0")) {
                    competitorNameParm = null;
                }
                if (competitor2NameParm.equals("0")) {
                    competitor2NameParm = null;
                }
                if (tst.getProductSubline() == null || // record not on db
                        !tst.getProductSubline().equals(ps.getId())) {
                    upd = "INSERT INTO customer_product_subline "
                            + "(vista_customer_number,prod_subline_id,potential_dollars,competitor_dollars,competitor2_dollars,competitor1_id,competitor2_id,notes,date_added,date_changed,forecast_dollars,volume,period_yyyy) "
                            + "VALUES ('" + request.getParameter("cust")
                            + "','" + ps.getId() + "',"
                            + potential.floatValue() + ","
                            + competitor.floatValue() + ","
                            + competitor2.floatValue() + ","
                            + competitorNameParm + "," + competitor2NameParm
                            + ",'" + fixQuotes(notes) + "',sysdate,sysdate,"
                            + forecast.floatValue() + "," + volume
                            + "," + now.get(Calendar.YEAR) + ")";
                } else {
                    upd = "UPDATE customer_product_subline "
                            + " set potential_dollars = "
                            + potential.floatValue()
                            + ", competitor_dollars = "
                            + competitor.floatValue()
                            + ", competitor2_dollars = "
                            + competitor2.floatValue() + ", competitor1_id = "
                            + competitorNameParm + ", competitor2_id = "
                            + competitor2NameParm + ", forecast_dollars = "
                            + forecast.floatValue() + ", volume = "
                            + volume + ", period_yyyy = "
                            + now.get(Calendar.YEAR) + ", notes = '"
                            + fixQuotes(notes) + "', date_changed = sysdate "
                            + " where vista_customer_number = '"
                            + request.getParameter("cust") + "' "
                            + " and prod_subline_id = '" + ps.getId() + "'";
                }

                try {
                    stmt = aConnection.createStatement();
                    stmt.executeUpdate(upd);

                } catch (Exception e) {
                    SMRCLogger.error(this.getClass().getName()
                            + ".updateCustomerProducts() ", e);
                    throw e;
                } finally {
                    SMRCConnectionPoolUtils.close(stmt);
                }
                
                
            }*/
        }

        return true;

    } //method

    private String writeNewPage(HttpServletRequest request, Connection aConnection, 
            OEMAcctPlanBean OEMAPVars, User user) throws java.io.IOException, Exception {

        String returnVal = "";
        String page = "home";

        if (request.getParameter("page") != null) {
            page = request.getParameter("page");
        }

        HeaderBean hdr = new HeaderBean();
        hdr.setPage(page);
        hdr.setHelpPage(TAPcommon.getHelpPage(page, aConnection));
        hdr.setUser(user);

        if (request.getParameter("backSort") != null) {
            hdr.setBackSort(request.getParameter("backSort"));
        }

        if (page.equals("papdetails") || page.equals("paphistory")
                || page.equals("viewspobj") || page.equals("viewspcpp")) {
            hdr.setPopup(true);
        }

        String testVCN = "";
        if (request.getParameter("cust") != null) {
            testVCN = request.getParameter("cust");
            hdr.setCustomer(AccountDAO.getOneCustomer(request.getParameter("cust"), aConnection));
            hdr.setAccount(AccountDAO.getAccount(request.getParameter("cust"), aConnection));
        }

        if (request.getParameter("add") != null) {
            hdr.setAddCust(true, (AccountDAO.customerExists(testVCN, aConnection)));
        }

        if (request.getParameter("geog") != null) {
            hdr.setGeog(request.getParameter("geog"));
        }

        if (request.getParameter("industry") != null) {
            hdr.setIndustry(request.getParameter("industry"));
        }
        
        OEMAPVars.setHeader(hdr);

        if (page.equals("salesplan")) {
            writeSalesPlan(request, aConnection, OEMAPVars);
            returnVal = "/OEMAcctPlanSalesplanDisplay.jsp";
        } else if (page.equals("productmix")) {
            writeProductMix(request, aConnection, user, OEMAPVars);
            if (request.getParameter("excel") != null){
                returnVal = "/OEMAcctPlanProductmixExcel.jsp";
            } else {
                returnVal = "/OEMAcctPlanProductmixDisplay.jsp";
            }
        } else if (page.equals("salesplanprint")) {
            writeSalesPlanPrint(request, aConnection, user, OEMAPVars);
            returnVal = "/OEMAcctPlanSalesplanprintDisplay.jsp";
        } else if (page.equals("userprofile")) {
            writeUserProfile(request, aConnection, user, OEMAPVars);
            returnVal = "/OEMAcctPlanUserprofileDisplay.jsp";
        } else if (page.equals("custproject")) {
            writeCustProjects(request, aConnection, user, OEMAPVars);
            returnVal = "/OEMAcctPlanCustprojectDisplay.jsp";
        } else if (page.equals("myProjects")) {
            writeUserTargetProjects(request, aConnection, user, OEMAPVars);
            returnVal = "/OEMAcctPlanMyprojectsDisplay.jsp";
        }

        request.setAttribute("variables", OEMAPVars);
        
        return returnVal;

    } //method



    private void writeProductMix(HttpServletRequest request, Connection aConnection, 
            User user, OEMAcctPlanBean OEMAPVars) throws java.io.IOException, Exception {
    	
    	// Get the show request value
    	
    	String show = (String)request.getParameter("salesOrders");
    	
    	if (show == null) {
    		
    		show = "invoice";
    		
    	}

        if (request.getParameter("updateProds") != null) {
            if (request.getParameter("updateProds").equals("true")) {
                if (updateCustomerProducts(request,aConnection, user)) {
                    OEMAPVars.setGenMsg("Save Successful");
                } else {
                    OEMAPVars.setGenMsg("There was an error saving your changes. Please contact IT Support.<br><a href='mailto:oemaccountplanner@eaton.com?subject=Product Mix Error'>oemaccountplanner@eaton.com</a>");
                }
            }
        }

        HttpSession session = request.getSession(false);
        int srYear = Globals.a2int((String) session.getAttribute("srYear"));
        int srMonth = Globals.a2int((String) session.getAttribute("srMonth"));
        ArrayList products = ProductDAO.getProductsCurrentYear(srYear, aConnection);
        Customer cust = AccountDAO.getOneCustomer(request.getParameter("cust"), aConnection);

        OEMAPVars.setCust(cust);
        OEMAPVars.setUser(user);

        OEMAPVars.setSalesmanName(SalesDAO.getSalesmanName(cust.getSalesId(), aConnection));

        ArrayList products2 = new ArrayList();
        String totalsProductId = "";
        for (int i = 0; i < products.size(); i++) {
            Product product = (Product) products.get(i);
            if (product.getSpLoadTotal().equalsIgnoreCase("T")){
                totalsProductId = product.getId();
            }
            product.setCustomerProduct(getCustomerProduct(aConnection, user, 
                    cust.getVistaCustNum(), product.getId(), srYear, srMonth));
            products2.add(product);
        }
        OEMAPVars.setProducts(products2);
        OEMAPVars.setLastUpdateDate(getLastUpdateDate(aConnection,
                cust.getVistaCustNum(), "productmix"));
        ArrayList vendors = MiscDAO.getVendors(true, aConnection);
        request.setAttribute("vendors", vendors);
        request.setAttribute("totalsProductId", totalsProductId);
        
        // Set the current show value as the sales orders attribute.  This defines if inovoice or 
        // order tap dollars are displayed
        
        request.setAttribute("salesOrders", show);
        
    } //method

    private void writeSalesPlanPrint(HttpServletRequest request, Connection aConnection, 
            User user, OEMAcctPlanBean OEMAPVars) throws java.io.IOException, Exception {

        if (request.getParameter("saveChanges") != null) {
            if (request.getParameter("saveChanges").equals("true")) {
                updateSalesPlan(request, aConnection, user);
                OEMAPVars.setGenMsg("Save Successful");

            }
        }

        SalesPlan salesPlan = getSalesPlan(aConnection, request.getParameter("cust"));
        Customer cust = AccountDAO.getOneCustomer(request.getParameter("cust"),
                aConnection);

        /*
        boolean canUpdate = false;
        boolean canSee = false;

        if (user.ableToUpdate(cust)) {
            canUpdate = true;
            canSee = true;
        } else if (user.ableToSee(cust)) {
            canSee = true;
        }
        
        OEMAPVars.setAbleToSee(canSee);
        OEMAPVars.setAbleToUpdate(canUpdate);
        */
        OEMAPVars.setSalesPlan(salesPlan);
        OEMAPVars.setCust(cust);

        OEMAPVars.setSalesmanName(SalesDAO.getSalesmanName(cust.getSalesId(), aConnection));
        OEMAPVars.setSalesPlanStages(getSalesPlanStages(aConnection));
        OEMAPVars.setCustomerStage(getCustomerStage(aConnection,cust.getVistaCustNum()));

        ArrayList tasks = AccountDAO.getCustTasks(aConnection, cust.getVistaCustNum(), 0);
        // Moved this code from the jsp so we don't pass in tasks that are not displayed
        ArrayList showTasks = new ArrayList();
        for (int i=0; i < tasks.size(); i++){
        	PurchaseActionProgram task = (PurchaseActionProgram)tasks.get(i);
	        if (task.getSchedule() != null) {
				GregorianCalendar gc = new GregorianCalendar() ;
				java.util.Date currentDate = gc.getTime() ;
				java.util.Date scheduleDate = (java.util.Date)task.getSchedule() ;
				long days = (currentDate.getTime()-scheduleDate.getTime()) / (24*3600*1000) ;
				
				if(days>365 && task.isComplete()){
					continue;
				} else {
					showTasks.add(task);
				}
			
			}
        }
        HttpSession session = request.getSession(false);
        int srYear = Globals.a2int((String) session.getAttribute("srYear"));
//      If the year is zero, something went wrong... set to current year
        if ( srYear == 0 ) {
        	Calendar cal = new GregorianCalendar();
         // Get the components of the date
        	srYear = cal.get(Calendar.YEAR);
        }
        ArrayList products = ProductDAO.getProductsCurrentYear(srYear, aConnection);

        request.setAttribute("usersTm",UserDAO.getAllUsersTreeMap(aConnection));
        
        ArrayList ebeCategories = MiscDAO.getEBECategories(aConnection);

    //    OEMAPVars.setTasks(tasks);
        OEMAPVars.setTasks(showTasks);
        OEMAPVars.setProducts(products);
        OEMAPVars.setEbeCategories(ebeCategories);

        OEMAPVars.setLastUpdateDate(getLastUpdateDate(aConnection, 
                cust.getVistaCustNum(), "salesplan"));

    } //method
    
    private void writeSalesPlan(HttpServletRequest request, Connection aConnection,
            OEMAcctPlanBean OEMAPVars) throws java.io.IOException, Exception {

        SalesPlan salesPlan = getSalesPlan(aConnection, request.getParameter("cust"));
        Customer cust = AccountDAO.getOneCustomer(request.getParameter("cust"),
                aConnection);

        OEMAPVars.setSalesPlan(salesPlan);
        OEMAPVars.setCust(cust);

        String backSort = "";
        if (request.getParameter("backSort") != null) {
            backSort = request.getParameter("backSort");
        }

        String backParm = "";

        if (request.getParameter("geog") != null) {
            backParm = "&geog=" + request.getParameter("geog");
            if (!backSort.equals("")) {
                backParm = backParm + "&backSort=" + backSort;
            }
        } else if (request.getParameter("industry") != null) {
            backParm = "&industry=" + request.getParameter("industry");
        }
        OEMAPVars.setBackSort(backSort);
        OEMAPVars.setBackParm(backParm);
        OEMAPVars.setCustomerName(cust.getName());

        OEMAPVars.setSalesmanName(cust.getSalesmanName());
        
        SPStage stage = getCustomerStage(aConnection, request.getParameter("cust"));
        OEMAPVars.setCustomerStage(stage);

        ArrayList tasks = AccountDAO.getCustTasks(aConnection, cust.getVistaCustNum(), 0);

        ArrayList tasks2 = new ArrayList();
        for (int i = 0; i < tasks.size(); i++) {
            PurchaseActionProgram task = (PurchaseActionProgram) tasks.get(i);
            User user = UserDAO.getUser(task.getAssignedTo(), aConnection);
            task.setAssignedUser(user);
            ArrayList ccUserIds = task.getCCEmail();
            for (int j = 0; j < ccUserIds.size(); j++) {
                user = UserDAO.getUser((String) ccUserIds.get(j), aConnection);
                task.addCcUser(user);
            }

            tasks2.add(task);
        }
        OEMAPVars.setTasks(tasks2);
        OEMAPVars.setLastUpdateDate(getLastUpdateDate(aConnection, 
                cust.getVistaCustNum(), "salesplan"));
    } //method

    private void writeUserProfile(HttpServletRequest request, Connection aConnection, 
            User maintUser, OEMAcctPlanBean OEMAPVars) throws java.io.IOException, Exception {
        
        if (request.getParameter("save") != null) {
            String userid = request.getParameter("maintUser");
            String over = request.getParameter("overrideSecurity");
            String set = request.getParameter("setSecurity");
            String viewAll = request.getParameter("viewAll");
            String viewProfiles = request.getParameter("viewProfiles");

            String[] geogOverrides = request.getParameterValues("geogOverride");
            String[] levels = request.getParameterValues("gLevel");
            String[] viewSalesman = request.getParameterValues("gSalesman");
            String[] update = request.getParameterValues("gUpdate");
            String[] delete = request.getParameterValues("gDelete");
            String newGeog = request.getParameter("newGeogOverride");
            String newLevel = request.getParameter("newLevel");
            String newSlsmn = request.getParameter("newSalesman");
            String newUpdate = request.getParameter("newUpdate");
            String dollarTypeCode = request.getParameter("DOLLAR_TYPE");

            updateUser(aConnection, userid, over, set, viewAll,viewProfiles,
                    geogOverrides, levels, viewSalesman, update, delete,
                    newGeog, newLevel, newSlsmn, newUpdate,dollarTypeCode);

            String[] removeSegmentOverrides = request.getParameterValues("rem_segment_override");
            if(removeSegmentOverrides!=null && removeSegmentOverrides.length!=0){
				for(int i=0;i<removeSegmentOverrides.length;i++){
				    UserDAO.removeSegmentOverride(userid, Globals.a2int(removeSegmentOverrides[i]), aConnection);
				}
			}
            
            String[] segmentOverrides = request.getParameterValues("segments");
            if(segmentOverrides!=null && segmentOverrides.length!=0){
				for(int i=0;i<segmentOverrides.length;i++){
				    UserDAO.addSegmentOverride(userid, Globals.a2int(segmentOverrides[i]), aConnection);
				}
			}
            
            if(userid.equals(maintUser.getUserid())){
            	// As a left over, this should not propate the error. Not sure why.  That is 
            	// how it is.  I will leave it as such until I figure out what was going on.
            	
            	try {
                	maintUser=SMRCSession.getUser(request,true,aConnection);
            	} catch (Exception ex) {
            		
            		SMRCLogger.error("Error while getting profile - this error is absorbed and not"
            				+ " passed into the application", ex);
            		
            	}
            }
            

            if (request.getParameter("switchuser") == null || request.getParameter("switchuser").equals("false")) {
                OEMAPVars.setGenMsg("Save Successful");
            }
        }
        
        OEMAPVars.setMaintUser(maintUser);
        User user = new User();

        if (request.getParameter("newuser") != null) {
            user = UserDAO.getUser(request.getParameter("newuser"), aConnection);
        } else {
            user = maintUser;
        }
        
        OEMAPVars.setUser(user);
        
        if (maintUser.ableToSetSecurity() || maintUser.ableToViewProfile()) {
            request.setAttribute("allUsersTm",UserDAO.getAllUsersTreeMap(aConnection));
        }
        
        if (maintUser.ableToSetSecurity() || maintUser.ableToViewProfile()) {
            OEMAPVars.setUserGeogOverrides(getUserGeogOverrides(aConnection,
                    user.getUserid()));
        }
        
        // Get segments for the segment override
        request.setAttribute ("segmentOverrides",UserDAO.getSegmentOverrides(user.getUserid(), aConnection));
    	request.setAttribute ("segments",SegmentsDAO.getSegments(0,4,aConnection));
        request.setAttribute("dollarTypeCodes",MiscDAO.getCodes("dollarTypeCodes", aConnection));
    } //method

    private void writeCustProjects(HttpServletRequest request, Connection aConnection, 
            User user, OEMAcctPlanBean OEMAPVars) throws java.io.IOException, Exception {

        String vcn = request.getParameter("cust");
        Customer cust = AccountDAO.getOneCustomer(vcn, aConnection);
        OEMAPVars.setCust(cust);
        OEMAPVars.setUser(user);

        if (request.getParameter("delProj") != null) {
            String pId = request.getParameter("delProj");

            TargetProject oldTP = ProjectDAO.getTargetProject(aConnection, pId);
            ProjectDAO.deleteProject(aConnection, oldTP);
            TargetProject newTP = ProjectDAO.getTargetProject(aConnection, pId);

            notifyUsersOfTargetProjectChange(pId, user, oldTP, newTP);
        } else if (request.getParameter("approve") != null) {
            String pId = request.getParameter("approve");

            TargetProject oldTP = ProjectDAO.getTargetProject(aConnection, pId);
            ProjectDAO.approveProject(aConnection, oldTP, user);
            TargetProject newTP = ProjectDAO.getTargetProject(aConnection, pId);

            notifyUsersOfTargetProjectChange(pId, user, oldTP, newTP);
        }

        ArrayList targetProjects = getTargetProjects(aConnection, user, vcn);
        ArrayList bids = getBids(aConnection, vcn);
        OEMAPVars.setBids(bids);
        OEMAPVars.setProjects(targetProjects);
        OEMAPVars.setSalesmanName(SalesDAO.getSalesmanName(cust.getSalesId(),
                aConnection));

    } //method

    private boolean custReceivedOrder(Connection aConnection, Bid bid, Customer cust) throws Exception {

        boolean received = false;

        String sel = "select * from bid_cust_xref where neg_num = '"
                + bid.getNegNum() + "' " + " and vista_cust_num = '"
                + cust.getVistaCustNum() + "'";

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                if (rs.getString("received_order") != null
                        && rs.getString("received_order").equals("Y")) {
                    received = true;
                }
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".custReceivedOrder() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return received;

    } //method

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

    } //method

    private void writeUserTargetProjects(HttpServletRequest request, Connection aConnection, 
            User user, OEMAcctPlanBean OEMAPVars) throws java.io.IOException, Exception {

        if (request.getParameter("approve") != null) {
            String pId = request.getParameter("approve");

            TargetProject oldTP = ProjectDAO.getTargetProject(aConnection, pId);
            ProjectDAO.approveProject(aConnection, oldTP, user);
            TargetProject newTP = ProjectDAO.getTargetProject(aConnection, pId);

            notifyUsersOfTargetProjectChange(pId, user, oldTP, newTP);
        } else if (request.getParameter("delete") != null) {
            String pId = request.getParameter("delete");

            TargetProject oldTP = ProjectDAO.getTargetProject(aConnection, pId);
            ProjectDAO.deleteProject(aConnection, oldTP);
            TargetProject newTP = ProjectDAO.getTargetProject(aConnection, pId);

            notifyUsersOfTargetProjectChange(pId, user, oldTP, newTP);
        }

        ArrayList projects = getTargetProjects(aConnection, user);
        OEMAPVars.setUser(user);
        OEMAPVars.setProjects(projects);

    } //method

    private ArrayList getTargetProjects(Connection aConnection, User user) throws Exception {

        ArrayList projects = new ArrayList(10);

        boolean isMgr = false;
        StringBuffer geog = new StringBuffer("");

        String dmSel = "select salesman_id, inactive_dt, sp_geog_cd, title_tx from current_salesman_v "
                + " where user_id = '"
                + user.getVistaId()
                + "'"
                + " and start_dt <= sysdate " + " order by start_dt desc";

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(dmSel);

            if (rs.next()) {
                if (rs.getString("inactive_dt") == null
                        || rs.getString("inactive_dt").length() <= 0) {
                    String title = rs.getString("title_tx");

                    // If District Manager, Zone Manager, or National Manager
                    if (title.equals("DM") || title.equals("ZM")
                            || title.equals("NM")) {
                        isMgr = true;
                        String spGeog = rs.getString("sp_geog_cd");
                        StringBuffer geogBackward = new StringBuffer("");

                        boolean haveNonZero = false;
                        for (int i = spGeog.length() - 1; i >= 0; i--) {
                            char c = spGeog.charAt(i);

                            if (haveNonZero) {
                                geogBackward.append(c);
                            } else if (c != '0') {
                                haveNonZero = true;
                                geogBackward.append(c);
                            }
                        }

                        for (int i = geogBackward.toString().length() - 1; i >= 0; i--) {
                            char c = geogBackward.charAt(i);
                            geog.append(c);
                        }
                    }
                }
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".getTargetProjects() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        String mgrCondition = "";

        if (isMgr) {
            mgrCondition = " or sp_geog like '" + geog.toString() + "%'";
        }

        String sel = "select b.* " + " from (" + " select a.target_project_id"
                + " from target_projects a, project_user_xref b"
                + " where a.target_project_id = b.target_project_id"
                + " and b.userid = '" + user.getUserid() + "'" + " union "
                + " select target_project_id" + " from target_projects"
                + " where user_added = '" + user.getUserid() + "' "
                + mgrCondition + ") a, target_projects b"
                + " where a.target_project_id = b.target_project_id";

        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                TargetProject tp = new TargetProject();
                tp = ProjectDAO.getTargetProject(aConnection,rs.getString("target_project_id"));

                /*
                 * added by jpv so allowedToApproveTargetProject() doesnt need
                 * to be called while looping through arraylist
                 */
                tp.setCanApprove(allowedToApproveTargetProject(aConnection, user, tp));

                projects.add(tp);
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".getTargetProjects() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }
        return projects;

    } //method

    private ArrayList getTargetProjects(Connection aConnection, User user, String vcn) throws Exception {

        ArrayList projects = new ArrayList(10);
        String sel = "select a.* from target_projects a, customer_project_xref b "
                + " where a.target_project_id = b.target_project_id "
                + " and b.vista_customer_number = '" + vcn + "'";

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                TargetProject tp = new TargetProject();
                tp = ProjectDAO.getTargetProject(aConnection,rs.getString("target_project_id"));
        
                // setCanApprove added by jpv
                tp.setCanApprove(allowedToApproveTargetProject(aConnection, user, tp));

                projects.add(tp);
            }
        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".getTargetProjects() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return projects;

    } //method

    private ArrayList getBids(Connection aConnection, String vcn) throws Exception {

        ArrayList bids = new ArrayList(10);
        Customer cust = AccountDAO.getOneCustomer(vcn, aConnection);

        String sel = "select a.* from bid_tracker a, bid_cust_xref b "
                + "where a.neg_num = b.neg_num " + "and b.vista_cust_num = '"
                + vcn + "'";

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                Bid bid = new Bid();
                bid.setNegNum(rs.getString("neg_num"));

                if (rs.getString("job_name") != null) {
                    bid.setJobName(rs.getString("job_name"));
                }
                if (rs.getString("status") != null) {
                    bid.setStatus(rs.getString("status"));
                }
                if (rs.getString("sales_id") != null) {
                    bid.setSalesId(rs.getString("sales_id"));

                    // setSalesmanName added by jpv so local method doesnt have
                    // to be called in loop
                    bid.setSalesmanName(SalesDAO.getSalesmanName(rs.getString("sales_id"), aConnection));
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

                bid.setCustReceivedOrder(custReceivedOrder(aConnection, bid, cust));

                bids.add(bid);
            }

        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName() + ".getBids() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }
        return bids;

    } //method

    private String getLastUpdateDate(Connection aConnection, String vcn, String page) throws Exception {

        String dt = "";
        String sel = "";

        if (page.equals("salesplan")) {
            sel = "select to_char(date_changed,'fmMonth DD, YYYY')||' '||to_char(date_changed,'HH:MI A.M.') as change_dt "
                    + " from (select max(date_changed) date_changed from (("
                    + " select max(date_changed) date_changed from sales_plans "
                    + " where vista_customer_number = '"
                    + vcn
                    + "' "
                    + ") union ("
                    + " select max(date_changed) date_changed from sales_plan_pap "
                    + " where vista_customer_number = '" + vcn + "' " + ")))";
        } else if (page.equals("productmix")) {
            sel = "select to_char(date_changed,'fmMonth DD, YYYY')||' '||to_char(date_changed,'HH:MI A.M.') as change_dt "
                    + " from (select max(date_changed) date_changed from "
                    + " ("
                    + " (select date_changed from customer_product "
                    + " where vista_customer_number = '"
                    + vcn
                    + "' "
                    + " ) union ( "
                    + " select date_changed from customer_product_subline "
                    + " where vista_customer_number = '" + vcn + "' " + ")))";
        } else { // in case this method gets called with an invalid page
            return "";
        }
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            if (rs.next()) {
                if (rs.getString("change_dt") != null) {
                    dt = rs.getString("change_dt");
                }
            }

        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".getLastUpdateDate() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return dt;

    } //method

    private ArrayList getUserGeogOverrides(Connection aConnection, String userid) throws Exception {

        ArrayList ugo = new ArrayList(10);

        String sel = "select * from user_geog_security where userid = '"
                + userid + "'";

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = aConnection.createStatement();
            rs = stmt.executeQuery(sel);

            while (rs.next()) {
                UserGeogSecurity ugs = new UserGeogSecurity();
                ugs.setUserid(userid);

                if (rs.getString("user_level") != null) {
                    ugs.setLevel(rs.getString("user_level"));
                }

                if (rs.getString("view_salesman") != null) {
                    ugs.setViewSalesman(rs.getString("view_salesman"));
                }

                if (rs.getString("able_to_update") != null) {
                    ugs.setMaintenance(rs.getString("able_to_update"));
                }

                if (rs.getString("security_type") != null) {
                    ugs.setSecurityType(rs.getString("security_type"));
                }

                ugs.setSalesmanName(SalesDAO.getSalesmanName(rs
                        .getString("sp_geog"), aConnection));
                ugs
                        .setGeogName(MiscDAO.getGeography(
                                rs.getString("sp_geog"), aConnection)
                                .getDescription());

                ugs.setSPGeog(rs.getString("sp_geog"));
                ugo.add(ugs);
            }

        } catch (Exception e) {
            SMRCLogger.error(this.getClass().getName()
                    + ".getUserGeogOverrides() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(stmt);
        }

        return ugo;

    } //method

    private boolean allowedToApproveTargetProject(Connection aConnection, User user, TargetProject tp)
            throws Exception {

        if (user.ableToSetSecurity()) { // user is allowed to set security
            // [admins only]
            return true;
        }

        boolean ok = false;

        if (tp.waitingForDM()) {
            if (userIsDistrictManager(aConnection, user, tp.getSPGeog())) {
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

    } //method

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
                    if (rs.getString("user_id").equalsIgnoreCase(
                            user.getVistaId())) {
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

    } //method

/*    private boolean userIsZoneManager(Connection aConnection, User user, String spGeog)
            throws Exception {

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

    } //method
*/
    private void notifyUsersOfTargetProjectChange(String projId, User user,
            TargetProject oldTP, TargetProject newTP) {

        TargetProjectUpdateEmail tpue = new TargetProjectUpdateEmail();
        tpue.setProjectId(projId);
        tpue.setUpdateUser(user);
        tpue.setOldProject(oldTP);
        tpue.setNewProject(newTP);

        tpue.run();

    } //method

} //class
