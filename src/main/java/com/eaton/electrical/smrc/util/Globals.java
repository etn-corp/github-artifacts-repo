/*
 * Created on Jun 28, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.eaton.electrical.smrc.util;

import java.util.*;
import java.sql.Connection;

import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.dao.*;


/**
 * @author E0062708
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Globals {


	public static Date getDate(String year, String month, String day)
	{
		
		Date result = null ;
		if(year.trim().length()==0 && month.trim().length()==0 && day.trim().length()==0){
			return result;
		}
		try
		{
			int iMonth = Globals.a2int(month)-1 ;
			int iYear  = Globals.a2int(year) ;
			int iDay   = Globals.a2int(day) ;
			GregorianCalendar gc = new GregorianCalendar(iYear, iMonth, iDay) ;
			result = gc.getTime() ;
		}
		catch(Exception e) {
			SMRCLogger.error("Globals.getDate() - Error converting date: " + month + "/" + day + "/" + year, e) ;			
			result = null ;
		}
		return result ;
	}
	
	 
	public static long a2long(String a){
		try{
			return Long.parseLong(a);
		}catch(NumberFormatException e){
			return 0 ;
		}
	}	
	public static int a2int(String a){
		try{
			return Integer.parseInt(a);
		}catch(NumberFormatException e){
			return 0 ;
		}
	}			
	public static double a2double(String a){
		try{
			return Double.parseDouble(a);
		}catch(NumberFormatException e){
			return 0 ;
		}
	}	
	public static String getGSSPricingEmailAddress(Connection DBConn) throws Exception {
	    String emailAddress = "";
	    ArrayList addresses = new ArrayList();
	    try {
	        addresses = MiscDAO.getStaticEmailAddress("GSS Pricing", DBConn);
	        if (addresses.size() > 0){
    //	          There should only be one address for GSS Pricing
	            emailAddress = (String) addresses.get(0);
	        }
	    } catch (Exception e) {
            SMRCLogger.error("Globals.getGSSPricingEmailAddress() ", e);
            throw e;
        }
	    	    
	    return emailAddress;
	}
	
	public static String getGSSTrainingEmailAddress(Connection DBConn) throws Exception{
	    String emailAddress = "";
	    ArrayList addresses = new ArrayList();
	    try {
	        addresses = MiscDAO.getStaticEmailAddress("GSS Training", DBConn);
	        if (addresses.size() > 0){
    //	          There should only be one address for GSS Training
	            emailAddress = (String) addresses.get(0);
	        }
	    } catch (Exception e) {
            SMRCLogger.error("Globals.getGSSTrainingEmailAddress() ", e);
            throw e;
        }
	    return emailAddress;
	}
	
	public static String getEnterpriseSystemsSecurityEmailAddress(Connection DBConn) throws Exception{
	    String emailAddress = "";
	    ArrayList addresses = new ArrayList();
	    try {
	        addresses = MiscDAO.getStaticEmailAddress("Enterprise Systems Security", DBConn);
	        if (addresses.size() > 0){
    //	          There should only be one address for Enterprise Systems Security
	            emailAddress = (String) addresses.get(0);
	        }
	    } catch (Exception e) {
            SMRCLogger.error("Globals.getEnterpriseSystemsSecurityEmailAddress() ", e);
            throw e;
        }
	    return emailAddress;
	}
	
	public static String getECommerceIntegrationTeamEmailAddress(Connection DBConn) throws Exception {
	    String emailAddress = "";
	    ArrayList addresses = new ArrayList();
	    try {
	        addresses = MiscDAO.getStaticEmailAddress("eCommerce Integration Team", DBConn);
	        if (addresses.size() > 0){
    //	          There should only be one address for eCommerce Integration Team
	            emailAddress = (String) addresses.get(0);
	        }
	    } catch (Exception e) {
            SMRCLogger.error("Globals.getECommerceIntegrationTeamEmailAddress() ", e);
            throw e;
        }
	    return emailAddress;
	}
	
	
	public static String getChannelTAPRequestsEmailAddress(Connection DBConn) throws Exception {
	    String emailAddress = "";
	    ArrayList addresses = new ArrayList();
	    try {
	        addresses = MiscDAO.getStaticEmailAddress("Channel TAP Requests", DBConn);
	        if (addresses.size() > 0){
    //	          There should only be one address for eCommerce Integration Team
	            emailAddress = (String) addresses.get(0);
	        }
	    } catch (Exception e) {
            SMRCLogger.error("Globals.getChannelTAPRequestsEmailAddress ", e);
            throw e;
        }
	    return emailAddress;
	}
	/*
	public static String getChampsManagerEmailAddress(Connection DBConn) throws Exception {
	    String emailAddress = "";
	    ArrayList addresses = new ArrayList();
	    try {
	        addresses = MiscDAO.getStaticEmailAddress("Champs Manager", DBConn);
	        if (addresses.size() > 0){
    //	          There should only be one address for Champs Manager
	            emailAddress = (String) addresses.get(0);
	        }
	    } catch (Exception e) {
            SMRCLogger.error("Globals.getChampsManagerEmailAddress() ", e);
            throw e;
        }
	    return emailAddress;
	}
	*/
	public static String getRequestItHref(){
		String href = "";
		ResourceBundle rb = ResourceBundle.getBundle("com.eaton.electrical.smrc.SMRC");
		href = rb.getString("requestItHref");
		return href;
	}
	
	public static int getTaskAttachmentLimit(){
		int limit = 0;
		ResourceBundle rb = ResourceBundle.getBundle("com.eaton.electrical.smrc.SMRC");
		limit = a2int(rb.getString("taskAttachmentLimit"));
		return limit;
	}
	
	public static int getTaskAttachmentFileSizeMax(){
		int fileSizeMax = 0;
		ResourceBundle rb = ResourceBundle.getBundle("com.eaton.electrical.smrc.SMRC");
		fileSizeMax = a2int(rb.getString("taskAttachmentFileSizeMax"));
		return fileSizeMax;
	}
	
	public static String getTaskAttachmentTempFileLocation(){
		String tempLoc = "";
		ResourceBundle rb = ResourceBundle.getBundle("com.eaton.electrical.smrc.SMRC");
		tempLoc = rb.getString("taskAttachmentTempFileLocation");
		return tempLoc;
	}
	
	public static String getProductModuleInstructionsLink(){
		String link = "";
		ResourceBundle rb = ResourceBundle.getBundle("com.eaton.electrical.smrc.SMRC");
		link = rb.getString("productModuleInstructions");
		return link;		
	}
	
	public static int calculateDifferenceInMonths(Calendar firstCal, Calendar secondCal, boolean inclusive){
		int totalMonths = 0;
		if ((secondCal.get(Calendar.MONTH) >= firstCal.get(Calendar.MONTH)) && (secondCal.get(Calendar.YEAR) == firstCal.get(Calendar.YEAR))){
			// 1st and 2nd dates are in the same year
			totalMonths = (secondCal.get(Calendar.MONTH) - firstCal.get(Calendar.MONTH));
		} else if ((secondCal.get(Calendar.MONTH) < firstCal.get(Calendar.MONTH))){
			// 1st date is in a previous year, but ending month is less than beginning month 
			// subtract 1 from difference in years, multiply 12 to get months in difference of years
			// subtract beginCal's months from 12 to get time from start to end of the year
			// add the ending months to include the months of the ending partial year
			totalMonths = (((secondCal.get(Calendar.YEAR) - firstCal.get(Calendar.YEAR) - 1) * 12) + ((12 - firstCal.get(Calendar.MONTH)) + secondCal.get(Calendar.MONTH)));
		} else if ((secondCal.get(Calendar.MONTH) > firstCal.get(Calendar.MONTH))){
			// 1st date is in a previous year, but ending month is greater than beginning month
			// find difference in years and multiply by 12 to get total months
			// add difference between ending month and beginning month
			totalMonths = (((secondCal.get(Calendar.YEAR) - firstCal.get(Calendar.YEAR)) * 12) + (secondCal.get(Calendar.MONTH) - firstCal.get(Calendar.MONTH)));
		} else if ((secondCal.get(Calendar.MONTH) == firstCal.get(Calendar.MONTH))){
			// 1st date is in a previous year, but has the same beginning and ending month
			// find difference in years and multiply by 12 to get total months
			totalMonths = ((secondCal.get(Calendar.YEAR) - firstCal.get(Calendar.YEAR)) * 12);
		}
		if (inclusive){
			totalMonths++;
		}
		
		return totalMonths;
	}
	
}
