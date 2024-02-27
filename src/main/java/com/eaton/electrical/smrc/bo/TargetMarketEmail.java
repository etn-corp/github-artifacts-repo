/*
 * TargetMarketEmail.java
 *
 * Created on October 15, 2004, 1:59 PM
 */

package com.eaton.electrical.smrc.bo;

import com.eaton.electrical.smrc.util.*;
import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.service.*;
import java.sql.*;
import java.util.*;

/**
 *
 * @author  Jason Lubbert
 */
public class TargetMarketEmail implements java.io.Serializable {
    
    TargetMarket targetMarket = null;
    Connection DBConn = null;
    
	private static final long serialVersionUID = 100;
    
    /** Creates a new instance of TargetMarketEmail */
    public TargetMarketEmail(TargetMarket targetMarket, Connection DBConn) {
        this.targetMarket = targetMarket;
        this.DBConn = DBConn;
        
    }
    
    public void sendEmail() throws Exception{

        try {
            SMRCLogger.debug("in TargetMarketEmail.sendEmail()");
                TAPMail tapmail = new TAPMail();
                ArrayList recipients = new ArrayList();
                if (targetMarket.isSubmitted()){//District Managers
                    recipients = getAllDMs(targetMarket, DBConn);
                } else if (targetMarket.isDMApproved()){
                    recipients = UserDAO.getAllChannelMarketingManagers(DBConn);
                } else if (targetMarket.isActive() || targetMarket.isRejected()){
                    recipients.add(UserDAO.getUser(targetMarket.getUserAdded(), DBConn));
                }

                for (int i=0; i < recipients.size(); i++){
                    User user = (User) recipients.get(i);
                    tapmail.addRecipient(user.getEmailAddress());
                }

                if (targetMarket.isDMApproved() || targetMarket.isActive()){
                	tapmail.addRecipient(Globals.getChannelTAPRequestsEmailAddress(DBConn));
                }
                
                User fromUser = UserDAO.getUser(targetMarket.getUserChanged(), DBConn);
                tapmail.setSenderInfo(fromUser.getFirstName() + " " + fromUser.getLastName(), fromUser.getEmailAddress());
                tapmail.addCCRecipient(fromUser.getEmailAddress());
                
                
                String message = getMessage(targetMarket, "");
                SMRCLogger.debug("going to send message");
                
                
                tapmail.sendMessage(message, "Target Market Plan");
                
                
        }catch (Exception e)	{
			SMRCLogger.error("TargetMarketEmail.sendEmail(): ", e);
			throw e;
        }
	}
        
    public void sendReviseResubmitEmail(User recipientUser, String reviseResubmitExplantion) throws Exception{
    	
        try {
                TAPMail tapmail = new TAPMail();
                
                tapmail.addRecipient(recipientUser.getEmailAddress());

                User fromUser = UserDAO.getUser(targetMarket.getUserChanged(), DBConn);
                tapmail.setSenderInfo(fromUser.getFirstName() + " " + fromUser.getLastName(), fromUser.getEmailAddress());
                tapmail.addCCRecipient(fromUser.getEmailAddress());
                
                String message = getMessage(targetMarket, reviseResubmitExplantion);
                SMRCLogger.debug("going to send message");
                
                tapmail.sendMessage(message, "Target Market Plan");
                
        }catch (Exception e)	{
			SMRCLogger.error("TargetMarketEmail.sendEmail(): ", e);
			throw e;
		} 
    }
    
    private ArrayList getAllDMs(TargetMarket targetMarket, Connection DBConn) throws Exception {
        try {
            ArrayList allAccounts = targetMarket.getPlanAccounts();
            ArrayList allAccountDMs = new ArrayList();
            for (int i = 0; i< allAccounts.size(); i++){
                String account = (String) allAccounts.get(i);
                String geog = AccountDAO.getAccountGeog(account,DBConn);
                if (geog != null){
                	User user = UserDAO.getUserByVistalineId(DistrictDAO.getManagerForGeography(geog.substring(0,5), "DM", DBConn),DBConn);
                	allAccountDMs.add(user);
                }
            }
            return allAccountDMs;
        } catch (Exception e)	{
		SMRCLogger.error("TargetMarketEmail.getAllDMs(): ", e);
		throw e;
        }
    }
    
    private String getMessage(TargetMarket targetMarket, String reviseResubmitExplantion)throws Exception {
        char newline = 13;
        StringBuffer message = new StringBuffer();
        try {

                if (targetMarket.isActive()){
                    message.append("The following target market plan is now active." + newline);
                } else if (targetMarket.isRejected()){
                    message.append("The following target market plan was rejected." + newline);
                } else if(targetMarket.isPendingResubmission()){
                    message.append("The following target market plan has been revised and resubmitted for your approval. Please log into ");
                    message.append("the Sales and Marketing Resource Channel to revise and resubmit the plan." + newline);
                }
                else {
                    message.append("The following target market plan has been submitted for your approval. Please log into ");
                    message.append("the Sales and Marketing Resource Channel and approve or reject the plan." + newline);
                }
                message.append(newline + "Plan Description: " + targetMarket.getPlanDescription());
                Calendar cal = Calendar.getInstance();
                cal.setTime(targetMarket.getStartDate());
                String startDate = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
                cal.setTime(targetMarket.getEndDate());
                String endDate = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
                message.append(newline + "Starting: " + startDate + "  Ending: " + endDate + newline);
                message.append(newline + "Distributors: " + newline);
                ArrayList distributors = targetMarket.getPlanAccounts();
                for (int i = 0; i < distributors.size(); i++){
                    String account = (String) distributors.get(i);
                    message.append(" " + AccountDAO.getAccountName(account,DBConn) + " (" + account + ")" + newline);
                }
                message.append(newline + "End Customers: " + newline);
                ArrayList endCustomers = targetMarket.getEndCustomers();
                for (int i = 0; i < endCustomers.size(); i++){
                    String account = (String) endCustomers.get(i);
                    message.append(" " + AccountDAO.getAccountName(account,DBConn) + " (" + account + ")" + newline);
                }
                message.append(newline + "Products: " + newline);
                ArrayList products = targetMarket.getPlanProducts();
                for (int i = 0; i < products.size(); i++){
                    TargetMarketProduct tmProduct = (TargetMarketProduct) products.get(i);
                    Product product = tmProduct.getProduct();
                    message.append(" " + product.getId() + " - " + product.getDescription() + newline);
                }
                
                if(targetMarket.isPendingResubmission()){
                	message.append("\n" + newline + reviseResubmitExplantion + newline + newline);
                }

                User createdBy = UserDAO.getUser(targetMarket.getUserAdded(), DBConn);
                message.append(newline + "This plan was created by " + createdBy.getFirstName() + " " + createdBy.getLastName());
                cal.setTime(targetMarket.getDateAdded());
                String dateAdded = (cal.get(Calendar.MONTH) + 1) + "/" + (cal.get(Calendar.DAY_OF_MONTH)) + "/" + (cal.get(Calendar.YEAR));
                message.append(" on " + dateAdded);

                if (targetMarket.isDMApproved() || targetMarket.isActive() || targetMarket.isRejected()){
                    String action = "Approved by ";
                    if (targetMarket.isRejected()){
                        action = "Rejected by ";
                    }
                    User approvedBy = UserDAO.getUser(targetMarket.getUserChanged(), DBConn);
                    cal.setTime(targetMarket.getDateChanged());
                    String dateApproved = (cal.get(Calendar.MONTH) + 1) + "/" + (cal.get(Calendar.DAY_OF_MONTH)) + "/" + (cal.get(Calendar.YEAR));
                    message.append(newline + action + approvedBy.getFirstName() + " " + approvedBy.getLastName());
                    message.append(" on " + dateApproved);

                }

        }catch (Exception e)	{
              SMRCLogger.error("TargetMarketEmail.getMessage(): ", e);
              throw e;
        } 
        
        return message.toString();
        
        
        
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
