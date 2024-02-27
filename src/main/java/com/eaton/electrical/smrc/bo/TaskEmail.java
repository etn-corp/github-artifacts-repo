package com.eaton.electrical.smrc.bo;

import java.sql.*;
import java.util.*;

import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

/**	This class contains all of the methods and attributes needed by the Target Account Planner
 *	as needed to email a task assignment to an individual.
 *	<br><br>These tasks are assigned using the account planner by anyone with an id.
 *	<br>Each task is by sales group / by customer / by product.
 *	That is, each task is assigned to one product for a given customer's sales plan
 *	who is in one sales group.
 *	<br>Each email is created and sent in a separate thread than the application itself. This
 *	allows the application to continue without keeping the user waiting for the email to be sent.
 *
 *	@author Carl Abel
 */
public class TaskEmail implements java.io.Serializable // extends Thread
{
	
	//private Connection DBConn = null;
	private char newline = 13;
	
	String _assignedTo = "";
	String _assignedBy = "";
	String _product = "";
	int _task = 0;
	String _cust = "";
	//SalesGroup _sGroup = new SalesGroup();
	Vector _cc = new Vector(5,5);
	
	private static final long serialVersionUID = 100;

	/** Constructor
	 *	@param String The email address of the user the task is assigned
	 *	@param String The email address of the user assigning the task
	 *	@param String The product id this task is to be linked
	 *	@param String The customer number this task is to be linked
	 *	@param int The task number for this task
	 *	@param SalesGroup The sales group this customer and user are linked to
	 */
	public TaskEmail(String assignedTo, String assignedBy, String product, String cust, int task) {
		setToUser(assignedTo);
		setAssignedByUser(assignedBy);
		setProduct(product);
		setTask(task);
		setCust(cust);
		//setSalesGroup(sGroup);
	}
	
	/** This method lets the calling routine override the sales group assigned in the constructor
	 *	@param SalesGroup The new sales group for this task
	 */
	//public void setSalesGroup(SalesGroup sGroup) {
	//	_sGroup = sGroup;
	//}
	
	/** This method lets the calling routine override the email address of the person
	 *	assigned the task. The original was assigned in the constructor
	 *	@param String The new email address for the user assigned to complete this task
	 */
	public void setToUser(String to) {
		_assignedTo = to;
	}
	
	/** This method lets the calling routine override the email address of the person
	 *	that assigned the task. The original was assigned in the constructor
	 *	@param String The new email address for the user that assigned this task
	 */
	public void setAssignedByUser(String assignUser) {
		_assignedBy = assignUser;
	}
	
	/** This method allows the calling routine to add a list of users that
	 *	this particular task should be sent to as a Carbon Copy (CC).
	 *	This Vector is expecting objects of the "User" type
	 *	@param Vector The list of users that should receive this task as a CC
	 */
	public void setCCUsers(Vector cc) {
		_cc = cc;
	}
	
	/** This method lets the calling routine override the product id assigned in the constructor
	 *	@param String The new product id for this task
	 */
	public void setProduct(String product) {
		_product = product;
	}
	
	/** This method lets the calling routine override the task id assigned in the constructor
	 *	@param int The new task id for this task
	 */
	public void setTask (int task) {
		_task = task;
	}
	
	/** This method lets the calling routine override the customer number assigned in the constructor
	 *	@param String The new customer number for this task
	 */
	public void setCust (String cust) {
		_cust = cust;
	}
	
	
	/** Get the Purchase Action Program for this task/product/customer/sales group
	 *	@return PurchaseActionProgram The PAP for this task
	 */
	private PurchaseActionProgram getPAP(Connection DBConn) {
		PurchaseActionProgram pap = new PurchaseActionProgram();
		
		String sel = "SELECT * " +
		"from sales_plan_pap " +
		"where vista_customer_number = '" + _cust + "' " +
		"and product_id = '" + _product + "' " +
		"and task_id = " + _task;
		
		Statement stmt = null;
		ResultSet res = null;
		
		try {
		    
			stmt = DBConn.createStatement();
			res = stmt.executeQuery(sel);
			
			if (res.next())	{
			    
				pap.setCustomer(res.getString("vista_customer_number"));
				pap.setProduct(res.getString("product_id"));
				pap.setTaskId(res.getInt("task_id"));
				
				if (res.getString("action") != null) {
					pap.setAction(res.getString("action"));
				}
				
				if (res.getString("objective") != null) {
					pap.setObjective(res.getString("objective"));
				}
				
				if (res.getDate("schedule") != null) {
					pap.setSchedule(res.getDate("schedule"));
				}
				
				if (res.getString("results") != null) {
					pap.setResults(res.getString("results"));
				}
				
				if (res.getString("assigned_to") != null) {
					pap.setAssignedTo(res.getString("assigned_to"));
				}
				
				if (res.getString("ebe_id") != null) {
					pap.setEBECategory(res.getInt("ebe_id"));
				}
				
				if (res.getString("complete") != null) {
					if (res.getString("complete").equals("Y")) {
						pap.setComplete(true);
					}
					else {
						pap.setComplete(false);
					}
				}
				
				pap.setCCEmail(AccountDAO.getSalesPlanCCUsers(res.getInt("task_id"), DBConn));

			}
			
		}
		catch (Exception e)	{
		    SMRCLogger.error(this.getClass().getName() + ".getPAP(): ", e);
		    return null;
		}
		finally {
		    SMRCConnectionPoolUtils.close(res);
		    SMRCConnectionPoolUtils.close(stmt);
		}
		
		return pap;
	}
	
	
	/** This method retrieves the text to be included in the email.
	 *	@param User The user that this email is to be sent (the one assigned to complete the task)
	 *	@param User The user that assigned the task
	 *	@return String The message in the email.
	 */
	private String getText(User toUser, User ccUser, Connection DBConn) throws Exception {
		PurchaseActionProgram thisPap = getPAP(DBConn);
		
		StringBuffer text = new StringBuffer("");
		
		text.append(toUser.getFirstName());
		text.append("," + newline);
		text.append("You have been assigned the following task in Target Account Planner for ");
		text.append(AccountDAO.getAccountName(_cust, DBConn));
		text.append(" (");
		text.append(_cust);
		text.append(")");
		text.append(newline + "Product: ");
		text.append(thisPap.getProduct());
		text.append(" - ");
		text.append(TAPcommon.getProductDescription(thisPap.getProduct(), DBConn));
		text.append(newline + "EBE Category: ");
		text.append(TAPcommon.getEBEDescription(thisPap.getEBECategory() + "", DBConn));
		
		text.append(newline + "Your Task: ");
		text.append(thisPap.getAction());
		text.append(newline + "Complete: ");
		text.append(thisPap.isComplete());
		text.append(newline + "Due Date: ");
		text.append(thisPap.getScheduleAsString());
		text.append(newline + "Results: ");
		text.append(thisPap.getResults());
		
		text.append(newline + "" + newline + "To respond to this task, please log on to Target Account Planner ");
		text.append(" by using the Target Account Planner link in the Sales Resources channel on JOE." + newline + "" + newline + "Click on \"My Open Tasks\" from the Home page and update your task.");
		
		text.append(newline + "" + newline + "If you have any questions about the task, please contact ");
		text.append(ccUser.getFirstName());
		text.append(" ");
		text.append(ccUser.getLastName());
		text.append(" at ");
		text.append(ccUser.getEmailAddress());
		
		ArrayList ccEmails = thisPap.getCCEmail();
		
		for(int i=0;i<ccEmails.size();i++){
			String tempUser = (String)ccEmails.get(i);
			User papCCUser = UserDAO.getUser(tempUser, DBConn);
			
			text.append(newline);
			text.append(papCCUser.getFirstName());
			text.append(" ");
			text.append(papCCUser.getLastName());
			text.append(" has been copied on this email as a reference.");
			
		}
		
		return text.toString();
	}
	
	/** This is a required method in all objects that extend "Thread"
	 *	<br>This is run each time this email should be sent (each time the tread needs to be executed)
	 */
	public void run() {

        Connection DBConn =null;
		TAPMail tm = new TAPMail();
				
		try {
			
		    DBConn = SMRCConnectionPoolUtils.getDatabaseConnection() ;
			
			User toUser = UserDAO.getUser(_assignedTo, DBConn);
			User ccUser = UserDAO.getUser(_assignedBy, DBConn);
			ArrayList papCCUsers = getPAP(DBConn).getCCEmail();
			ArrayList papCCUserObjects = new ArrayList();
			for(int i=0;i<papCCUsers.size();i++){	
				papCCUserObjects.add(UserDAO.getUser((String)papCCUsers.get(i), DBConn));
			}
			
			String from = ccUser.getEmailAddress().trim();
			String to = toUser.getEmailAddress() + " ";
			to = to.trim();
			
			ArrayList recipients = new ArrayList();	// used to keep duplicate emails from being sent to the same person
			recipients.add(to);
			
			String cc = ccUser.getEmailAddress() + " ";
			cc = cc.trim();
			boolean send = true;
			for (int i=0; i < recipients.size(); i++) {
				String test = (String)recipients.get(i);
				if (test.equals(cc)) {
					send = false;
				}
			}
			
			if (send) {
				
				recipients.add(cc);
				tm.addCCRecipient(cc);
			}
			
			for(int k=0;k<papCCUserObjects.size();k++){
				User ccUser2 = (User)papCCUserObjects.get(k);
				cc= ccUser2.getEmailAddress().trim();
				if (cc.length() > 0) {
					send = true;
					for (int i=0; i < recipients.size(); i++) {
						String test = (String)recipients.get(i);
						if (test.equals(cc)) {
							send = false;
						}
					}
					
					if (send) {
						recipients.add(cc);
						tm.addCCRecipient(cc);
					}
				}
				
			}
			
			
			for (int i=0; i < _cc.size();i++) {
				User ccUser2 = (User)_cc.elementAt(i);
				cc = ccUser2.getEmailAddress() + " ";
				cc = cc.trim();
				
				send = true;
				for (int j=0; j < recipients.size(); j++) {
					String test = (String)recipients.get(j);
					if (test.equals(cc)) {
						send = false;
					}
				}
				
				if (send) {
					recipients.add(cc);
					tm.addCCRecipient(cc);
				}
			}
			
			
			String subject = "New Task Assignment - " + AccountDAO.getAccountName(_cust, DBConn) +
			"(" + _cust + ")";
			
			tm.addRecipient(to);
			tm.setSenderInfo(ccUser.getFirstName() + " " + ccUser.getLastName(), from);
			tm.sendMessage(getText(toUser, ccUser,DBConn), subject);
		
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
		}catch (Exception e) {
		    SMRCLogger.error(this.getClass().getName() + ".run(): ", e);
		    SMRCConnectionPoolUtils.rollbackTransaction(DBConn);
		}
		finally {
			SMRCConnectionPoolUtils.close(DBConn) ;			
		}
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public boolean equals(Object obj) {
		if (this == null || obj == null) {
			return false;
		}

		return super.equals(obj);
	}

	public String toString() {
		String returnString = "";


		return returnString;
	}
}
