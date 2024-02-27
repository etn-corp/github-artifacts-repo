package com.eaton.electrical.smrc.dao;

import java.sql.*;
import java.util.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;


/**
 * @author E0062708
 *
 */
public class ContactsDAO {
	private static final String getContactsQuery = "SELECT * FROM CONTACTS a,CONTACT_TITLES b WHERE a.TITLE_ID=b.TITLE_ID(+) AND VISTA_CUSTOMER_NUMBER=? ORDER BY LAST_NAME";
	private static final String newContactInsert="INSERT INTO CONTACTS (VISTA_CUSTOMER_NUMBER,FIRST_NAME,LAST_NAME," +
		"PHONE_NUMBER,FAX_NUMBER,TITLE,TITLE_ID,EMAIL_ADDRESS,GENERAL_COMMENTS,CONTACT_FOR_PRICE_UPDATE_FLAG,DATE_ADDED,DATE_CHANGED,CONTACT_ID) VALUES" +
		"(?,?,?,?,?,?,?,?,?,?,SYSDATE,SYSDATE,CONTACTS_SEQ.NEXTVAL)";
	private static final String newContactUpdate="UPDATE CONTACTS SET VISTA_CUSTOMER_NUMBER=?,FIRST_NAME=?,LAST_NAME=?," +
		"PHONE_NUMBER=?,FAX_NUMBER=?,TITLE=?,TITLE_ID=?,EMAIL_ADDRESS=?,GENERAL_COMMENTS=?,CONTACT_FOR_PRICE_UPDATE_FLAG=?,DATE_CHANGED=SYSDATE" +
		" WHERE CONTACT_ID=?";
	private static final String getContactQuery="SELECT * FROM CONTACTS a, CONTACT_TITLES b WHERE a.TITLE_ID=b.TITLE_ID(+) AND CONTACT_ID=?";
	private static final String contactDelete="DELETE FROM CONTACTS WHERE CONTACT_ID=?";
	
	public static void saveContact(Contact contact, Connection DBConn) throws Exception{

		if(contact.getId()==0){
			updateContact(contact,true, DBConn);
		}else{
			updateContact(contact,false, DBConn);
		}
			
	}
	
	private static void updateContact(Contact contact, boolean isNew, Connection DBConn) throws Exception
	{

		PreparedStatement pstmt = null;		
		try {

			if(isNew){
				pstmt = DBConn.prepareStatement(newContactInsert);	
				SMRCLogger.debug("insert new");
			}else{
				pstmt = DBConn.prepareStatement(newContactUpdate);
				SMRCLogger.debug("update");
			}
			
			int pIndex = 0 ;
		
			pstmt.setString( ++pIndex, contact.getCustomer() ) ;
			pstmt.setString( ++pIndex, contact.getFirstName() ) ;
			pstmt.setString( ++pIndex, contact.getLastName() ) ;
			pstmt.setString( ++pIndex, contact.getPhone()) ;
			pstmt.setString( ++pIndex, contact.getFax() ) ;
			pstmt.setString( ++pIndex, contact.getTitle() ) ;
			pstmt.setInt( ++pIndex, Globals.a2int(contact.getFunctionalPosition())) ;
			pstmt.setString( ++pIndex, contact.getEmailAddress() ) ;
			pstmt.setString( ++pIndex, contact.getComments() ) ;
			if(contact.isPricingContact()){
				pstmt.setString( ++pIndex, "Y" ) ;
			}else{
				pstmt.setString( ++pIndex, "N" ) ;
			}
			if(!isNew){
				pstmt.setInt( ++pIndex, contact.getId() ) ;
			}
			
			pstmt.executeUpdate();
			
		}catch (Exception e)	{
			SMRCLogger.error("ContactsDAO.updateContact(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);

		}
	}
	
	
	public static Contact getContact(String contactId, Connection DBConn) throws Exception{
		
		Contact contact = new Contact();
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DBConn.prepareStatement(getContactQuery);
			
			pstmt.setString(1, contactId);
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				contact.setId(rs.getInt("CONTACT_ID"));
				contact.setCustomer(StringManipulation.noNull(rs.getString("VISTA_CUSTOMER_NUMBER")));
				contact.setFirstName(StringManipulation.noNull(rs.getString("FIRST_NAME")));
				contact.setLastName(StringManipulation.noNull(rs.getString("LAST_NAME")));
				contact.setPhoneNumber(StringManipulation.noNull(rs.getString("PHONE_NUMBER")));
				contact.setFax(StringManipulation.noNull(rs.getString("FAX_NUMBER")));
				contact.setTitle(StringManipulation.noNull(rs.getString("TITLE")));
				contact.setTitleId(rs.getInt("TITLE_ID"));
				contact.setFunctionalPosition(StringManipulation.noNull(rs.getString("DESCRIPTION")));				
				contact.setEmailAddress(StringManipulation.noNull(rs.getString("EMAIL_ADDRESS")));
				contact.setComments(StringManipulation.noNull(rs.getString("GENERAL_COMMENTS")));
				if(StringManipulation.noNull(rs.getString("CONTACT_FOR_PRICE_UPDATE_FLAG")).equals("Y")){
					contact.setPricingContact(true);	
				}
				if(StringManipulation.noNull(rs.getString("CONTACT_FOR_PRICE_UPDATE_FLAG")).equals("Y")){
					contact.setDistributorStatement(true);	
				}				
			}
		}catch (Exception e){
			SMRCLogger.error("ContactsDAO.getContact(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return contact;
	}
	
	public static void deleteContact(String contactId, Connection DBConn) throws Exception
	{

		PreparedStatement pstmt = null;
	
		try {
			pstmt = DBConn.prepareStatement(contactDelete);	
			pstmt.setInt( 1, Globals.a2int(contactId));

			pstmt.executeUpdate();
			
		}catch (Exception e)	{
			SMRCLogger.error("ContactsDAO.deleteContact(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
	}
	
	public static ArrayList getContacts(String acctId, Connection DBConn) throws Exception{

		ArrayList contacts = new ArrayList();
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DBConn.prepareStatement(getContactsQuery);
			
			pstmt.setString(1, acctId);
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				Contact contact = new Contact();
				
				contact.setId(rs.getInt("CONTACT_ID"));
				contact.setCustomer(StringManipulation.noNull(rs.getString("VISTA_CUSTOMER_NUMBER")));
				contact.setFirstName(StringManipulation.noNull(rs.getString("FIRST_NAME")));
				contact.setLastName(StringManipulation.noNull(rs.getString("LAST_NAME")));
				contact.setPhoneNumber(StringManipulation.noNull(rs.getString("PHONE_NUMBER")));
				contact.setFax(StringManipulation.noNull(rs.getString("FAX_NUMBER")));
				contact.setTitle(StringManipulation.noNull(rs.getString("TITLE")));
				contact.setTitleId(rs.getInt("TITLE_ID"));
				contact.setFunctionalPosition(StringManipulation.noNull(rs.getString("DESCRIPTION")));				
				contact.setEmailAddress(StringManipulation.noNull(rs.getString("EMAIL_ADDRESS")));
				contact.setComments(StringManipulation.noNull(rs.getString("GENERAL_COMMENTS")));
				if(StringManipulation.noNull(rs.getString("CONTACT_FOR_PRICE_UPDATE_FLAG")).equals("Y")){
					contact.setPricingContact(true);	
				}
				if(StringManipulation.noNull(rs.getString("CONTACT_FOR_PRICE_UPDATE_FLAG")).equals("Y")){
					contact.setDistributorStatement(true);	
				}	
				
				contacts.add(contact);
	
			}
		}catch (Exception e)	{
			SMRCLogger.error("ContactsDAO.getContacts(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);

		}
		
		return contacts;	
	}
	
}
