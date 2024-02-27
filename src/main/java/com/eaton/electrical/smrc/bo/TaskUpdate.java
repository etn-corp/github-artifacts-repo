package com.eaton.electrical.smrc.bo;

import java.sql.*;
import java.util.*;

import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;


/**	This class contains all of the methods and attributes needed by the Target Account Planner
 *	as needed to email an update of a task assignment to an individual.
 *	<br><br>These tasks are assigned using the account planner by anyone with an id.
 *	<br>Each task is by sales group / by customer / by product.
 *	That is, each task is assigned to one product for a given customer's sales plan
 *	who is in one sales group.
 *	<br>Each email is created and sent in a separate thread than the application itself. This
 *	allows the application to continue without keeping the user waiting for the email to be sent.
 *
 *	@author Carl Abel
 */
public class TaskUpdate implements java.io.Serializable // extends Thread
{
	 
	//private Connection DBConn = null;
	
	//private String _product = "";
	private String _task = "";
	private String _cust = "";
	//private SalesGroup _sGroup = new SalesGroup();
	private String _oldAssignTo = "";
	private String _oldCCTo[] = null;
    private char newline = 13;
	
	private static final long serialVersionUID = 100;

	/** Constructor
	 *	@param String The email address of the user the task is assigned
	 *	@param String The email address of the user assigning the task
	 *	@param String The product id this task is to be linked
	 *	@param String The customer number this task is to be linked
	 *	@param int The task number for this task
	 *	@param SalesGroup The sales group this customer and user are linked to
	 *	@param String The old Assigned To id
	 *	@param String the old CC Email id
	 */
	public TaskUpdate(String cust, String task, String oat, String oct[]) {
		setTask(task);
		setCust(cust);
		_oldAssignTo = oat;
		_oldCCTo = oct;
	}
	
	/** This method lets the calling routine override the sales group assigned in the constructor
	 *	@param SalesGroup The new sales group for this task
	 */
	//public void setSalesGroup(SalesGroup sGroup) {
	//	_sGroup = sGroup;
	//}
	
	/** This method lets the calling routine override the product id assigned in the constructor
	 *	@param String The new product id for this task
	 */
	//public void setProduct(String product) {
	//	_product = product;
	//}
	
	/** This method lets the calling routine override the task id assigned in the constructor
	 *	@param int The new task id for this task
	 */
	public void setTask (String task) {
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
		
		String sel = "SELECT * from sales_plan_pap where task_id = " + _task;
		//Connection DBConn = null;
		
		Statement stmt = null;
		ResultSet res = null;
		
		try {
			//DBConn = SMRCConnectionPoolUtils.getDatabaseConnection() ;
			stmt = DBConn.createStatement();
			res = stmt.executeQuery(sel);
			
			if (res.next())	{
				if (res.getString("vista_customer_number") != null) {
					pap.setCustomer(res.getString("vista_customer_number"));
				}
				
				if (res.getString("product_id") != null) {
					pap.setProduct(res.getString("product_id"));
				}
				
				if (res.getString("task_id") != null) {
					pap.setTaskId(res.getInt("task_id"));
				}
				
				if (res.getString("user_added") != null) {
					pap.setUserAdded(res.getString("user_added"));
				}
				
				if (res.getString("user_changed") != null) {
					pap.setUserChanged(res.getString("user_changed"));
				}
				
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
				
				if (res.getString("complete") != null) {
					if (res.getString("complete").equals("Y")) {
						pap.setComplete(true);
					}
					else {
						pap.setComplete(false);
					}
				}
				
				if (res.getString("ebe_id") != null) {
					pap.setEBECategory(res.getInt("ebe_id"));
				}
				
				pap.setCCEmail(AccountDAO.getSalesPlanCCUsers(res.getInt("task_id"), DBConn));
				
			}

		}

		catch (Exception e) {
		    SMRCLogger.error(this.getClass().getName() + ".getRecipients(): ", e);
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
	private String getText(PurchaseActionProgram thisPap,User changedBy, Connection DBConn) throws Exception {
		StringBuffer text = new StringBuffer("");
		
		text.append(newline + "A task created in Target Account Planner has been modified by ");
		text.append(changedBy.getFirstName());
		text.append(" ");
		text.append(changedBy.getLastName());
		text.append("." + newline + "If you have any questions about the task, please contact this individual at ");
		text.append(changedBy.getEmailAddress());
		
		
		text.append(newline + "" + newline);
		
		text.append("The task is for ");
		text.append(AccountDAO.getAccountName(_cust, DBConn));
		text.append(" (");
		text.append(_cust);
		text.append(")" + newline);
		text.append("Below is the new task information" + newline + "" + newline);
		text.append("Product: ");
		text.append(thisPap.getProduct());
		text.append(" - ");
		text.append(TAPcommon.getProductDescription(thisPap.getProduct(), DBConn));
		text.append(newline + "EBE Category: ");
		text.append(TAPcommon.getEBEDescription(thisPap.getEBECategory() + "", DBConn));
		text.append(newline + "Task: ");
		text.append(thisPap.getAction());
		text.append(newline + "Complete: ");
		text.append(thisPap.isComplete());
		text.append(newline + "Due Date: ");
		text.append(thisPap.getScheduleAsString());
		text.append(newline + "Results: ");
		text.append(thisPap.getResults());
	//	text.append(newline + "" + newline + "The Target Account Planner Planner can be found in the Sales Resources channel on JOE. ");
	//	text.append(newline + "" + newline + "Please reply to this message if you have any problems.");
		
		text.append(newline + "" + newline + "To respond to this task, please log on to Target Account Planner ");
		text.append(" by using the Target Account Planner link in the Sales Resources channel on JOE." + newline + "" + newline + "Click on \"My Open Tasks\" from the Home page and update your task.");
		
		text.append(newline + "" + newline + "If you have any questions about the task, please contact ");
		text.append(changedBy.getFirstName());
		text.append(" ");
		text.append(changedBy.getLastName());
		text.append(" at ");
		text.append(changedBy.getEmailAddress());
		return text.toString();
	}
	
	/** This is a required method in all objects that extend "Thread"
	 *	<br>This is run each time this email should be sent (each time the tread needs to be executed)
	 */
	public void run() {
		        
		TAPMail tapmail = new TAPMail();
		
		Connection DBConn = null;

		try {
		    
		    DBConn = SMRCConnectionPoolUtils.getDatabaseConnection() ;
		    
			PurchaseActionProgram thisPap = getPAP(DBConn);
			
			if (thisPap == null) {
				return;
			}
			
			User addedBy = new User();
			if (thisPap.getUserAdded() != null) {
				addedBy = UserDAO.getUser(thisPap.getUserAdded(), DBConn);
			}
			
			User changedBy = new User();
			if (thisPap.getUserChanged() != null) {
				changedBy = UserDAO.getUser(thisPap.getUserChanged(), DBConn);
			}
			
			User assignedTo = new User();
			if (thisPap.getAssignedTo() != null) {
				assignedTo = UserDAO.getUser(thisPap.getAssignedTo(), DBConn);
			}
			if(assignedTo==null) return;
			
			ArrayList papCCUsers = thisPap.getCCEmail();
			ArrayList papCCUserObjects = new ArrayList();

			for(int m=0;m<papCCUsers.size();m++){
				papCCUserObjects.add(UserDAO.getUser((String)papCCUsers.get(m), DBConn));
			}
			
			User oldAssignTo = new User();
			if (_oldAssignTo != null && _oldAssignTo.length() > 0) {
				oldAssignTo = UserDAO.getUser(_oldAssignTo, DBConn);
			}

			ArrayList oldCCTos = new ArrayList();
			if(_oldCCTo!=null) {
				for(int i=0;i<_oldCCTo.length;i++) {
					oldCCTos.add(UserDAO.getUser(_oldCCTo[i], DBConn));
				}
			}

			String from = changedBy.getEmailAddress().trim();
			String to = addedBy.getEmailAddress().trim();
			
			if (to.length() == 0) {
				to = from;
			}
			
			ArrayList recipients = new ArrayList();	// used to keep duplicate emails from being sent to the same person
			recipients.add(to);
			
			String cc = "";
			
			if (changedBy.getEmailAddress().length() > 0) {
				cc = changedBy.getEmailAddress().trim();
				
				boolean send = true;
				for (int i=0; i < recipients.size(); i++) {
					String test = (String)recipients.get(i);
					if (test.equals(cc)) {
						send = false;
					}
				}
				
				if (send) {
					recipients.add(cc);
                    tapmail.addCCRecipient(cc);
				}
			} //if (changedBy.getEmailAddress().length() > 0)
			
			for(int j=0;j<papCCUserObjects.size();j++) {
				User tempUsr = (User)papCCUserObjects.get(j);
				cc = tempUsr.getEmailAddress().trim();
				
				
				boolean send = true;
				for (int i=0; i < recipients.size(); i++) {
					String test = (String)recipients.get(i);
					if (test.equals(cc)) {
						send = false;
					}
				}
				
				if (send) {
                    recipients.add(cc);
                    tapmail.addCCRecipient(cc);
				}
				
			} //for(int j=0;j<papCCUserObjects.size();j++)
			
			
			if (assignedTo.getEmailAddress() != null &&	assignedTo.getEmailAddress().length() > 0) {
				cc = assignedTo.getEmailAddress();
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
                    tapmail.addCCRecipient(cc);
				}
				
			} //if (assignedTo.getEmailAddress() != null ...
			
			if (oldAssignTo.getEmailAddress() != null && oldAssignTo.getEmailAddress().length() > 0) {
				cc = oldAssignTo.getEmailAddress();
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
                    tapmail.addCCRecipient(cc);
				}
				
			} //if (oldAssignTo.getEmailAddress() != null ...
			
			for(int j=0;j<oldCCTos.size();j++){
				User ccUser = (User)oldCCTos.get(j);
				
				cc = ccUser.getEmailAddress();
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
                    tapmail.addCCRecipient(cc);
				}
				
			} //for(int j=0;j<oldCCTos.size();j++)

			String msgSubject = "Target Account Planner Task Update - " + AccountDAO.getAccountName(_cust, DBConn) + "(" + _cust + ")";
            
            tapmail.addRecipient(to);
			tapmail.setSenderInfo(changedBy.getFirstName() + " " + changedBy.getLastName(), from);
			tapmail.sendMessage(getText(thisPap,changedBy, DBConn), msgSubject);
			
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
		}
		catch (Exception e) {
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
