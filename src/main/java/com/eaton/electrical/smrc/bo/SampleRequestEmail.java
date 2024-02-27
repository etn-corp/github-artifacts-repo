
package com.eaton.electrical.smrc.bo;

import java.sql.*;
import java.text.*;
import java.util.*;

import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;


/**	This class represents an email that is sent to the pricing manager for product sample requests
 *  Vince: This is not really a business object and should be moved out of this package.
 */
public class SampleRequestEmail implements java.io.Serializable {
	//private Connection DBConn = null;

	private char newline = 13;
	
	String _use = "";
	int _contactId = 0;
	Vector _samples = new Vector(5,5);
	String _smCode = "";
	String _shipMethod = "";
	String _acctNum = "";
	String _requestorId = "";
	String _shipToNum = "";
	int _stageId = 0;
	float _pot = 0;
	float _act = 0;
	float _compet = 0;
	float _forecast = 0;
	String _attn = "";
	String _addr1 = "";
	String _addr2 = "";
	String _addr3 = "";
	String _city = "";
	String _state = "";
	String _zip = "";
	StringBuffer errorTracker = new StringBuffer("");
	String _salesName = "";
	String _salesOffice = "";
	String _districtMgr = "";
	Vector _channelType = new Vector(4,4);
	Vector _channelList = new Vector(4,4);
	String _contactOverride = "";
	
	private static final long serialVersionUID = 100;

	public void setActualDol(String dol) {
		Float temp = new Float(dol);
		_act = temp.floatValue();
	}
	
	public void setPotentialDol(String dol) {
		Float temp = new Float(dol);
		_pot = temp.floatValue();
	}
	
	public void setCompetDol(String dol) {
		Float temp = new Float(dol);
		_compet = temp.floatValue();
	}
	
	public void setForecastDol(String dol) {
		Float temp = new Float(dol);
		_forecast = temp.floatValue();
	}
	
	public void setUse(String use) {
		_use = use;
	}
	
	public void setCust(String cust) {
		_shipToNum = cust;
	}
	
	public void setContact(int contactId) {
		_contactId = contactId;
	}
	
	public void setContactOverride(String cont) {
		_contactOverride = cont;
	}
	
	public void setShipMethod(String shipMethod) {
		_smCode = shipMethod;
		if (shipMethod.equalsIgnoreCase("air")) {
			_shipMethod = "Air Freight";
		}
		else if (shipMethod.equalsIgnoreCase("best")) {
			_shipMethod = "Best Way";
		}
		else {
			_shipMethod = shipMethod;
		}
	}
	
	public void setStage(int stageid) {
		_stageId = stageid;
	}
	
	public void setAcctNum(String acctNum) {
		_acctNum = acctNum;
	}

	public void setRequestor(String userid) {
		_requestorId = userid;
	}
	
	public void addSample(int qty, String cat) {
		SampleProduct sample = new SampleProduct();
		sample.setQty(qty);
		sample.setCatalogNum(cat);
		
		_samples.addElement(sample);
	}
	
	public void setShipTo(String attn,String addr1,String addr2,String addr3,String city,String state,String zip) {
		_attn = attn;
		_addr1 = addr1;
		_addr2 = addr2;
		_addr3 = addr3;
		_city = city;
		_state = state;
		_zip = zip;
	}
	
	
	private String getText(SPStage stage,User requestor,
			Account shipTo,Contact contact, TAPMail tm, Connection DBConn) throws Exception{
		
		StringBuffer text = new StringBuffer("");
		StringBuffer htmlText = new StringBuffer("");
		text.append(newline + "A product sample request has been requested for a Key account. ");
		text.append("Please evaluate the request and let the requestor know if the request ");
		text.append("has been approved and processed as soon as possible. If there are any ");
		text.append("questions, please contact the industry manager (if available).");
		
		text.append(newline + "________________________________________________________________________________________");
		text.append(newline);
		text.append(newline + "Requested By: ");
		text.append(requestor.getFirstName());
		text.append(" ");
		text.append(requestor.getLastName());
		text.append(newline);

/*		
		text.append("Industry Manager(s): ");

		Vector indMgrs = getIndustryManagers(shipTo.getVcn(), DBConn);
		for (int i=0; i < indMgrs.size(); i++) {
			if (i > 0) {
				text.append(", ");
			}
			
			text.append((String)indMgrs.elementAt(i));
		}
		
		text.append(newline);
*/
		
		java.util.Date today = new java.util.Date();
		DateFormat todayOut = DateFormat.getInstance();
		
		text.append(newline + "Request Date: ");
		text.append(todayOut.format(today));
		text.append(newline + "________________________________________________________________________________________");
		text.append(newline);
		text.append(newline + "Sales Engineer for this customer: ");
		text.append(_salesName);
		text.append(newline + "Sales Number: ");
		text.append(shipTo.getSalesEngineer1());
		text.append(newline + "District Mgr: ");
		text.append(_districtMgr);
		text.append(newline + "Sales Office: ");
		text.append(_salesOffice);
		
		text.append(newline + "Geography: ");
		text.append(shipTo.getDistrict());
		text.append(newline + "" + newline + "Use or Application of the samples:" + newline);
		text.append(_use);
		text.append(newline + "" + newline + "Stage of this account: ");
		text.append(stage.getDescription());
		
		text.append(newline + "" + newline + "Contact Name at customer location: ");
		if (_contactOverride != null && _contactOverride.length() > 0) {
			text.append(_contactOverride);
		}
		else {
			text.append(contact.getFirstName());
			text.append(" ");
			text.append(contact.getLastName());
			text.append("   Phone: ");
			text.append(contact.getPhone());
		}
		
		text.append(newline + "" + newline + "End Customer: ");
		text.append(shipTo.getCustomerName());
		text.append("    Vista #: ");
		text.append(shipTo.getVcn());
		text.append(newline + "" + newline + "Company's Address:" + newline);
		Address businessAddress = shipTo.getBusinessAddress();
		text.append(businessAddress.getAddress1());
		text.append(newline);
		text.append(businessAddress.getAddress2());
		text.append(newline);
		text.append(businessAddress.getAddress3());
		text.append(newline);
		text.append(businessAddress.getAddress4());
		text.append(newline);
		text.append(businessAddress.getCity());
		text.append(", ");
		text.append(businessAddress.getState());
		text.append(" ");
		text.append(businessAddress.getZip());
		text.append(newline + "" + newline + "Customer's Phone Number: ");
		text.append(shipTo.getPhone());
		
		text.append(newline + "" + newline + "Distributer this customer purchases through: ");
		
		for (int i=0; i < _channelType.size(); i++) {
			text.append(newline);
			text.append((String)_channelType.elementAt(i));
			text.append(" - ");
			text.append((String)_channelList.elementAt(i));
		}
		
		text.append(newline + "" + newline + "Customer Sales Information");
		
		NumberFormat dols = NumberFormat.getCurrencyInstance();
		dols.setMaximumFractionDigits(0);
		dols.setMinimumFractionDigits(0);
		
		text.append(newline + "C-H Potential Dollars: ");
		text.append(dols.format(_pot));
		text.append(newline + "Actual this year: ");
		text.append(dols.format(_act));
		text.append(newline + "Competitor this year: ");
		text.append(dols.format(_compet));
		text.append(newline + "" + newline + "Samples Requested:" + newline);
		
		for (int i=0; i < _samples.size(); i++) {
			SampleProduct prod = (SampleProduct)_samples.elementAt(i);
			
			text.append(newline + "Catalog Number: ");
			text.append(prod.getCatalogNum());
			text.append("  Quantity: ");
			text.append(prod.getQty());
		}
		
		text.append(newline + "" + newline + "Shipping Information:");
		text.append(newline + "Ship by: ");
		text.append(_shipMethod);
		
		if (_smCode.equalsIgnoreCase("air")) {
			text.append(newline + "Carrier and Account (for Air Freight):");
			text.append(_acctNum);
		}
		
		text.append(newline + "" + newline + "Shipping Address:");
		text.append(newline + "Attn ");
		text.append(_attn);
		text.append(newline);
		text.append(_addr1);
		text.append(newline);
		text.append(_addr2);
		text.append(newline);
		text.append(_addr3);
		text.append(newline);
		text.append(_city);
		text.append(", ");
		text.append(_state);
		text.append(" ");
		text.append(_zip);
		
		text.append(newline + "" + newline + "____________________________________________________________________");
		text.append(newline + "Listed below is a list of samples sent to this customer in the past year ");
		text.append("through one of the online account planner tools.");
		
		PrevSamples prevSamples = getPreviousSamples(DBConn);
		
		text.append(newline);
		//   Everything is the same for text and html up to this point
		htmlText.append(text.toString());
		
		text.append("Request Date       Requestor                 Quantity  Catalog Number");
		text.append(newline + "-----------------  ---------------------  --------  --------------------");
		htmlText.append("<TABLE BORDER=0><TR><TD>Request Date</TD><TD>Requestor</TD><TD>Quantity</TD><TD>Catalog Number</TD></TR>");
		htmlText.append("<TR><TD>-----------------</TD><TD>---------------------</TD><TD>--------</TD><TD>--------------------</TD></TR>");
		
		int psCnt = 0;
		
		while (prevSamples.next()) {
			psCnt++;
			text.append(newline);
			htmlText.append("<TR><TD>");
			text.append(todayOut.format(prevSamples.getRequestDate()));
			htmlText.append(todayOut.format(prevSamples.getRequestDate()));
			
			for (int i=todayOut.format(prevSamples.getRequestDate()).length(); i < 19; i++) {
				text.append(" ");
				
			}
			
			User usr = UserDAO.getUser(prevSamples.getRequestorId(), DBConn);
			String name = usr.getFirstName() + " " + usr.getLastName();
			
			text.append(name);
			htmlText.append("</TD><TD>" + name + "</TD>");
			
			for (int i = name.length(); i < 25; i++) {
				text.append(" ");
			}
			
			text.append(prevSamples.getQty());
			htmlText.append("<TD>" + prevSamples.getQty() + "</TD>");
			String strQty = "" + prevSamples.getQty();
			for (int i = strQty.length(); i < 8; i++) {
				text.append(" ");
			}
			
			text.append(prevSamples.getCatalogNum());
			htmlText.append("<TD>" + prevSamples.getCatalogNum() + "</TD></TR>");
		}
		
		htmlText.append("</TABLE>");
		if (psCnt == 0) {
			text.append(newline + "No samples have been shipped to this customer in the past year through an account planner application.");
			htmlText.append(newline + "No samples have been shipped to this customer in the past year through an account planner application.");
		}
		
		text.append(newline + "" + newline + "For more detailed reports, please visit the Target Account Planner on the Sales Resources channel on JOE. ");
		text.append(newline + "" + newline + "Please reply to this message if you have any problems." + newline);
		htmlText.append(newline + "" + newline + "For more detailed reports, please visit the Target Account Planner on the Sales Resources channel on JOE. ");
		htmlText.append(newline + "" + newline + "Please reply to this message if you have any problems." + newline);
		
		
		text.append(errorTracker.toString());
		htmlText.append(errorTracker.toString());
		tm.setHtmlMessage(htmlText.toString());
		
		return text.toString();
	}
	
/*
 * 
 *   Removed per requestIT 30881
	private Vector getIndustryManagers(String cust, Connection DBConn) {
		Vector indMgrs = new Vector(5,5);
		
		String sel = "select industry_subgroup_mgr " +
			"from oemapnew.customer_industries_o t1, oemapnew.industry_subgroup_o t2 " +
			"where t1.industry_subgroup_id = t2.industry_subgroup_id " +
			"and t1.vista_customer_number = '" + cust + "'";
		
		Statement stmt = null;
		ResultSet res = null;
		
		try {
			stmt = DBConn.createStatement();
			res = stmt.executeQuery(sel);
			
			while (res.next()) {
				if (res.getString("industry_subgroup_mgr") != null) {
					indMgrs.addElement(res.getString("industry_subgroup_mgr"));
				}
			}
		}
		catch (Exception e)	{
		    SMRCLogger.error(this.getClass().getName() + ".getIndustryManagers(): ", e);
		    errorTracker.append("\nError: Error getting industry managers: ");
			errorTracker.append(e.getMessage());
		}
		finally {
			SMRCConnectionPoolUtils.close(stmt);
			SMRCConnectionPoolUtils.close(res);
		}
		
		return indMgrs;
	}
*/	
		
	private void setChannels( String cust, Connection DBConn) {
		String sel = "SELECT channel_description, channel_name " +
			"from oemapnew.customer_channel_o t, oemapnew.purchase_channels_o " +
			"where t.channel_id = oemapnew.purchase_channels_o.channel_id " + 
			"and t.vista_customer_number = '" + cust + "'";	    
		
		Statement stmt = null;
		ResultSet res = null;
		
		try {
			stmt = DBConn.createStatement();
			res = stmt.executeQuery(sel);
			
			while (res.next()) {
				_channelType.addElement(res.getString("channel_description"));
				_channelList.addElement(res.getString("channel_name"));
			}
		}
		catch (Exception e)	{
		    SMRCLogger.error(this.getClass().getName() + ".setChannels(): ", e);
			errorTracker.append("\nError: Error getting channels: ");
			errorTracker.append(e.getMessage());
		}
		finally {
			SMRCConnectionPoolUtils.close(stmt);
			SMRCConnectionPoolUtils.close(res);
		}
	}
	
	private PrevSamples getPreviousSamples(Connection DBConn) {
		PrevSamples ps = new PrevSamples();
		
		String sel = "SELECT * from product_samples ps, product_sample_detail psd " +
		"WHERE ps.product_sample_id = psd.product_sample_id " +
		"AND ps.vista_customer_number = '" + _shipToNum + "' " +
		"AND ps.request_date >= ADD_MONTHS(sysdate,-12) " +
		"AND ps.request_date < sysdate - 1";
		
		Statement stmt = null;
		ResultSet res = null;
		
		try {
			stmt = DBConn.createStatement();
			res = stmt.executeQuery(sel);
			
			while (res.next()) {
				String cat = res.getString("catalog_num");
				String requestor = res.getString("requestor");
				int qty = res.getInt("qty");
				java.sql.Date reqDate = res.getDate("request_date");
				
				java.util.Date temp = reqDate;
				
				ps.add(cat,qty,temp,requestor);
			}
		}
		catch (Exception e)	{
		    SMRCLogger.error(this.getClass().getName() + ".getPreviousSamples(): ", e);		    
			errorTracker.append("\nError: Error getting Previous Samples: ");
			errorTracker.append(e.getMessage());
		}
		finally {
			SMRCConnectionPoolUtils.close(stmt);
			SMRCConnectionPoolUtils.close(res);
		}
		
		return ps;
	}
	
	
	private SPStage getSPStage(int stageId, Connection DBConn) {
		String sel = "SELECT * from sales_plan_stages where sp_stage_id = " + stageId;
		SPStage stage = new SPStage();
		
		Statement stmt = null;
		ResultSet res = null;
		
		try {
			stmt = DBConn.createStatement();
			res = stmt.executeQuery(sel);
			
			while (res.next()) {
				stage.setId(stageId);
				stage.setDescription(res.getString("description"));
			}
		}
		catch (Exception e)	{
		    SMRCLogger.error(this.getClass().getName() + ".getSPStage(): ", e);		    
			errorTracker.append("\nError: Error getting SPStage: ");
			errorTracker.append(e.getMessage());
			
		}
		finally {
			SMRCConnectionPoolUtils.close(stmt);
			SMRCConnectionPoolUtils.close(res);
		}
		
		return stage;
	}
	
	private Contact getContact (int contactId, Connection DBConn) {
		String sel = "SELECT * from contacts where contact_id = " + contactId;
		Contact contact = new Contact();
		
		Statement stmt = null;
		ResultSet res = null;
		
		try {
			stmt = DBConn.createStatement();
			res = stmt.executeQuery(sel);
			
			while (res.next()) {
				contact.setId(res.getInt("contact_id"));
				contact.setFirstName(res.getString("first_name"));
				contact.setLastName(res.getString("last_name"));
				contact.setPhoneNumber(res.getString("phone_number"));
				contact.setTitle(res.getString("title"));
			}
		}
		catch (Exception e)	{
		    SMRCLogger.error(this.getClass().getName() + ".getContact(): ", e);		    
			errorTracker.append("\nError: Error getting contact: ");
			errorTracker.append(e.getMessage());
			
		}
		finally {
			SMRCConnectionPoolUtils.close(stmt);
			SMRCConnectionPoolUtils.close(res);
		}
		
		return contact;
	}
	
	public void send() {
       
		Connection DBConn = null;
    	
		try{
	    
    	    DBConn = SMRCConnectionPoolUtils.getDatabaseConnection() ;   
    	    
    	    
			User requestor = new User();
			Account shipTo = new Account();
			Contact contact = new Contact();
			SPStage stage = new SPStage();
			
			TAPMail tm = new TAPMail();

			requestor = UserDAO.getUser(_requestorId, DBConn);
			shipTo = AccountDAO.getAccount(_shipToNum, DBConn);
			stage = getSPStage(_stageId, DBConn);
			
			if (_contactOverride == null || _contactOverride.length() <= 0) {
				contact = getContact(_contactId, DBConn);
			}
			setChannels( shipTo.getVcn(), DBConn);
			
			tm.setSenderInfo(requestor.getFirstName() + " " + requestor.getLastName(), requestor.getEmailAddress());
			String sendTo = getSampleRequestEmailAddress(DBConn);
			SMRCLogger.debug("SampleRequestEmail.send() - email will be sent to " + sendTo);
			tm.addRecipient(sendTo);
			tm.addCCRecipient(requestor.getEmailAddress());
			String msg_text = getText(stage,requestor,shipTo,contact, tm, DBConn);
			tm.sendMessage(msg_text, "Product Sample Request from SMRC Application");

			// TODO ? This needs to be taken out of here...
			saveToDB(DBConn);
			
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			
		}
		catch (Exception e) {
		    SMRCLogger.error(this.getClass().getName() + ".send(): ", e);
		    SMRCConnectionPoolUtils.rollbackTransaction(DBConn);
		}
		finally {
			SMRCConnectionPoolUtils.close(DBConn);
		}
		
	} //method
	
	private void saveToDB(Connection DBConn) {
		String ins = "INSERT INTO product_samples " +
		"(product_sample_id,vista_customer_number,request_date,requestor," +
		"use,ship_method,ship_addr1,ship_addr2,ship_addr3,ship_city,ship_state,ship_zip," +
		"ship_attention) " +
		"VALUES(product_samples_seq.nextVal,'" + _shipToNum + "',sysdate,'" +
		_requestorId + "','" + _use + "','" + _smCode + "','" + _addr1 + "','" +
		_addr2 + "','" + _addr3 + "','" + _city + "','" + _state + "','" + _zip + "','" +
		_attn + "')";
		
		Statement stmt = null;
		
		try {
			stmt = DBConn.createStatement();
			stmt.executeUpdate(ins);
		}
		catch (Exception e)	{
			errorTracker.append("\nError: Error inserting into product_samples: ");
			errorTracker.append(e.getMessage());
			errorTracker.append("\n");
			errorTracker.append(ins);
		}
		finally {
			SMRCConnectionPoolUtils.close(stmt);
		}
		
		for (int i=0; i < _samples.size(); i++) {
			SampleProduct sample = (SampleProduct)_samples.elementAt(i);
			
			ins = "INSERT INTO product_sample_detail " +
			"(product_sample_detail_id,product_sample_id,qty,catalog_num) " +
			"VALUES(product_sample_detail_seq.nextval,product_samples_seq.currval," +
			sample.getQty() + ",'" + sample.getCatalogNum() + "')";
			
			try {
				stmt = DBConn.createStatement();
				stmt.executeUpdate(ins);
			}
			catch (Exception e)	{
				errorTracker.append("\nError: Error inserting into product_sample_detail: ");
				errorTracker.append(e.getMessage());
				errorTracker.append("\n");
				errorTracker.append(ins);
				
			}
			finally {
				SMRCConnectionPoolUtils.close(stmt);
			}
		}
	
	} //method
	
	private String getSampleRequestEmailAddress(Connection DBConn) throws Exception {
	    String email = "";
	    try {
	        ArrayList emailList = MiscDAO.getStaticEmailAddress("Sample Request", DBConn);
	        // There should only be one address for sample requests
	        email = (String) emailList.get(0);
	    } catch (Exception e) {
            SMRCLogger.error("SampleRequestEmail.getSampleRequestEmailAddress() ", e);
            throw e;
        }
	    
	    return email;
	}
	
} //class