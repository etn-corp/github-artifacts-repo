package com.eaton.electrical.smrc.dao;

import java.sql.*;
import java.util.*;
import java.io.*;

import javax.activation.FileDataSource;
import javax.servlet.http.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;
import com.eaton.ee.manageAttachments.*;

/**
 * @author E0062708 - Josh Vender
 *
 * This class provides most database calls related to the Account object.
 * It is mostly used in the Account Profile page.
 *
 */
public class AccountDAO {
	//account statements
	private static final String getAccountQuery = "SELECT a.*, b.focus_type_id FROM CUSTOMER a, CUSTOMER_FOCUS_TYPE b WHERE a.vista_customer_number=b.vista_customer_number(+) AND a.VISTA_CUSTOMER_NUMBER=?";
	private static final String updateAccountUpdate = "UPDATE CUSTOMER SET " +
		"CUSTOMER_NAME=?,PARENT_NUM=?,SALES_ENGINEER1=?,SALES_ENGINEER2=?,SALES_ENGINEER3=?,SALES_ENGINEER4=?,SP_GEOG=?,PHONE_NUMBER=?,FAX_NUMBER=?,INTERNATIONAL_PHONE_NUMBER=?,INTERNATIONAL_FAX_NUMBER=?," +
		"WEB_SITE=?,TARGET_ACCOUNT_FLAG=?,TARGET_ACCOUNT_TYPE_ID=?,NUM_OF_VALUE=?,BACKGROUND_INFORMATION=?," +
		"DIRECT_FLAG=?,PARENT_ONLY_FLAG=?,SEND_CONFIRMATION=?,EXEMPT_CERT_REQUIRED=?,GENESIS_NUMBER=?,DPC_NUMBER=?,STORE_ID_NUMBER=?,SYNERGY_CODE=?,NOTES=?, APPLICATION_ID=?, USER_CHANGED=?,APCONTACT=?,APCONTACT_PHONE_NUMBER=?,APCONTACT_EMAIL_ADDRESS=?, DATE_CHANGED=SYSDATE " +
		"WHERE VISTA_CUSTOMER_NUMBER=?";
	private static final String newAccountInsert = "INSERT INTO CUSTOMER (PROSPECT_NUMBER,CUSTOMER_NAME,VISTA_CUSTOMER_NUMBER,PARENT_NUM,"+
		"SALES_ENGINEER1,SALES_ENGINEER2,SALES_ENGINEER3,SALES_ENGINEER4,SP_GEOG,PHONE_NUMBER,FAX_NUMBER,INTERNATIONAL_PHONE_NUMBER,INTERNATIONAL_FAX_NUMBER,WEB_SITE,TARGET_ACCOUNT_FLAG,TARGET_ACCOUNT_TYPE_ID,NUM_OF_VALUE,BACKGROUND_INFORMATION,"+
		"DIRECT_FLAG,PARENT_ONLY_FLAG,SEND_CONFIRMATION,EXEMPT_CERT_REQUIRED,CUSTOMER_STATUS,GENESIS_NUMBER,DPC_NUMBER,STORE_ID_NUMBER,SYNERGY_CODE,NOTES,APPLICATION_ID,USER_ADDED,USER_CHANGED,APCONTACT,APCONTACT_PHONE_NUMBER,APCONTACT_EMAIL_ADDRESS,DATE_ADDED,DATE_CHANGED) "+
		"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,SYSDATE)";
	private static final String changeAccountStatusUpdate="UPDATE CUSTOMER SET CUSTOMER_STATUS=? WHERE VISTA_CUSTOMER_NUMBER=?";
	private static final String getAccountNameQuery="SELECT CUSTOMER_NAME FROM CUSTOMER WHERE VISTA_CUSTOMER_NUMBER=?";

	//address statements
	private static final String getAddressQuery = "SELECT a.*, b.*, c.COUNTRY_NAME, c.COUNTRY_CODE FROM CODE_TYPES a, CUSTOMER_ADDRESS b, COUNTRY_MV c WHERE b.COUNTRY=c.COUNTRY_CODE(+) AND a.CODE_DESCRIPTION=? AND b.VISTA_CUSTOMER_NUMBER=? AND a.CODE_TYPE_ID=b.ADDRESS_TYPE_ID";
	private static final String doesAddrExistQuery="SELECT COUNT(*) FROM CUSTOMER_ADDRESS WHERE VISTA_CUSTOMER_NUMBER=? AND ADDRESS_TYPE_ID=?";
	private static final String setAddressInsert="INSERT INTO CUSTOMER_ADDRESS"+
		"(VISTA_CUSTOMER_NUMBER,ADDRESS_TYPE_ID,ADDRESS_SEQ_NUM,ADDRESS_LINE1,ADDRESS_LINE2,"+
		"ADDRESS_LINE3,ADDRESS_LINE4,CITY,STATE,ZIP,COUNTRY) VALUES "+
		"(?,?,?,?,?,?,?,?,?,?,?)";
	private static final String setAddressUpdate="UPDATE CUSTOMER_ADDRESS "+
		"SET ADDRESS_LINE1=?,ADDRESS_LINE2=?,ADDRESS_LINE3=?,ADDRESS_LINE4=?,CITY=?,STATE=?," +
		"ZIP=?,COUNTRY=? WHERE VISTA_CUSTOMER_NUMBER=? AND ADDRESS_TYPE_ID=?";


	//target divisions statements
	private static final String targetDivisionsDelete = "DELETE FROM CUSTOMER_DIVISIONS WHERE VISTA_CUSTOMER_NUMBER=?";
	private static final String targetDivisionsInsert = "INSERT INTO CUSTOMER_DIVISIONS(VISTA_CUSTOMER_NUMBER,DIVISION_ID) VALUES(?,?)";
	private static final String targetDivisionsQuery = "SELECT * FROM CUSTOMER_DIVISIONS WHERE VISTA_CUSTOMER_NUMBER=?";
	private static final String getProspectSeqQuery = "SELECT prospects_seq.nextval FROM dual ";

	// Gets distributor ID (DISTRIBUTOR table) from VCN
	private static final String getDistributorIdQuery="SELECT DISTRIBUTOR_ID FROM DISTRIBUTORS WHERE VISTA_CUSTOMER_NUMBER=?";

	// Query for the "Parent" popup window
	private static final String parentSearchQuery =
		"select * from (" +
			"select vista_customer_number, customer_name, city, state, zip, rownum rn from ("	+
				"select a.vista_customer_number, a.customer_name, b.city, b.state, b.zip, rownum rn " +
				"from customer a, customer_address b, code_types c where " +
				"a.vista_customer_number=b.vista_customer_number(+) "+
				"AND b.address_type_id=c.code_type_id " +
				"AND c.code_description='Business Address' " +
				"and a.vista_customer_number = a.parent_num " +
				"and a.vista_customer_number not like 'P%' " +
				"and LOWER(a.customer_name) like LOWER(?) " +
				"order by a.customer_name " +
			")" +
		")  where rn >= ? and rn <= ? ";









	// Query for "Account Browse" popups - by customer name
	private static final String accountBrowseQuery1 =
		"select * from ( " +
			"select vista_customer_number, customer_name, city, state, zip, parent_num, rownum rn from ( " +
				"select a.vista_customer_number, a.customer_name, b.city, b.state, b.zip, a.parent_num, rownum rn from customer a, customer_address b, code_types c where "+
				"a.vista_customer_number=b.vista_customer_number(+) " +
				"and b.address_type_id=c.code_type_id " +
				"and c.code_description='Business Address' " +
				"and LOWER(customer_name) like LOWER(?) " +
				"order by customer_name "+
			") " +
	    ") where rn >= ? and rn <= ? ";

	// Query for "Account Browse" popups - by VCN
	private static final String accountBrowseQuery2 =
		"select * from (" +
			"select vista_customer_number, customer_name, city, state, zip, parent_num, rownum rn from( " +
				"select a.vista_customer_number, a.customer_name, b.city, b.state, b.zip, a.parent_num, rownum rn from customer a, customer_address b, code_types c where "+
				"a.vista_customer_number=b.vista_customer_number(+) " +
				"and b.address_type_id=c.code_type_id " +
				"and c.code_description='Business Address' " +
				"and LOWER(a.vista_customer_number) like LOWER(?) " +
				"order by customer_name " +
			") " +
		 ") where rn >= ? and rn <= ? ";

//	 Query for "Distributor" popups - by customer name
	private static final String distributorBrowseQuery1 =
		"select * from (" +
			"select vista_customer_number, customer_name, city, state, zip, parent_num, rownum rn from( " +
				"select a.vista_customer_number, a.customer_name, b.city, b.state, b.zip, a.parent_num , rownum rn " +
				"from customer a, customer_address b, code_types c " +
				"where a.vista_customer_number=b.vista_customer_number(+) " +
				"and b.address_type_id=c.code_type_id " +
				"and c.code_description='Business Address' " +
				"and a.customer_status = 'Active' " +
				"and a.vista_customer_number in ( " +
					"select distinct(cs.vista_customer_number) " +
					"from segments s, customer_segments cs " +
					"where s.distributor_segment_flag = 'Y' " +
					"and cs.segment_id = s.segment_id " +
	            ") and LOWER(customer_name) like LOWER(?) " +
	            "order by customer_name " +
	        ") " +
	    ") where rn >= ? and rn <= ? ";

	// Query for "Distributor" popups - by VCN
	private static final String distributorBrowseQuery2 =
		"select * from ( " +
			"select vista_customer_number, customer_name, city, state, zip, parent_num, rownum rn from ( " +
				"select a.vista_customer_number, a.customer_name, b.city, b.state, b.zip, a.parent_num , rownum rn " +
				"from customer a, customer_address b, code_types c " +
				"where a.vista_customer_number=b.vista_customer_number(+) " +
				"AND b.address_type_id=c.code_type_id " +
				"AND c.code_description='Business Address' " +
				"and a.customer_status = 'Active' " +
				"and a.vista_customer_number in ( " +
					"select distinct(cs.vista_customer_number) " +
					"from segments s, customer_segments cs " +
					"where s.distributor_segment_flag = 'Y' " +
					"and cs.segment_id = s.segment_id " +
	            ") AND LOWER(a.vista_customer_number) like LOWER(?) " +
				"order by customer_name " +
	        ")" +
	    ") where rn >= ? and rn <= ?";









	// Queries for Associated Users" popup on Account Profile page
	private static final String getAssociatedUsersQuery = "select user_cust_xref.userid,"+
		"users.last_name,"+
		"users.first_name,"+
		"users.email_address"+
		" from USER_CUST_XREF,"+
		" users"+
		" where user_cust_xref.userid=users.userid(+) and VISTA_CUST_NUM=?"+
		" and users.userid = user_cust_xref.userid ORDER BY users.last_name";
	private static final String associatedUserDelete = "DELETE FROM USER_CUST_XREF WHERE USERID=? AND VISTA_CUST_NUM=?";
	private static final String associatedUserInsert = "INSERT INTO USER_CUST_XREF (USERID,VISTA_CUST_NUM) VALUES(?,?)";

	// Queries for "Sales Plan" page
	// TODO - should these salesplan queries be moved?
	private static final String getCCemailsQuery = "SELECT * FROM SALES_PLAN_PAP_CC WHERE TASK_ID=?";
	private static final String deleteSalesPlanCCUsers = "DELETE FROM SALES_PLAN_PAP_CC WHERE TASK_ID=?";
	private static final String insertSalesPlanCCUsers = "INSERT INTO SALES_PLAN_PAP_CC (TASK_ID,CC_ID) VALUES(?,?)";
	private static final String deleteTask = "delete from sales_plan_pap where task_id = ?";
	private static final String getTasksQuery = "SELECT * FROM SALES_PLAN_PAP WHERE VISTA_CUSTOMER_NUMBER = ? ORDER BY DATE_ADDED DESC";
	private static final String getTasksForVisitsQuery = "select * from sales_plan_pap where vista_customer_number = ? and customer_visit_id = ? ORDER BY DATE_ADDED DESC";
	private static final String getOneTask = "select * from sales_plan_pap where task_id = ?";

	// The following 2 queries updates the CONTACTS table, but is updated when the Account Profile is saved
	private static final String setDistributorStatementContactUpdate1 = "UPDATE CONTACTS SET contact_for_price_update_flag='N' WHERE VISTA_CUSTOMER_NUMBER=? AND CONTACT_ID!=?";
	private static final String setDistributorStatementContactUpdate2 = "UPDATE CONTACTS SET contact_for_price_update_flag='Y' WHERE CONTACT_ID=?";

	// Queries that are called fromt he "Approval Summary" page of distributor forms
	private static final String setCustomerCategoryUpdate = "UPDATE CUSTOMER SET CUSTOMER_CATEGORY=? WHERE VISTA_CUSTOMER_NUMBER=?";
	private static final String getCustomerCategoryUpdate = "SELECT CUSTOMER_CATEGORY FROM CUSTOMER WHERE VISTA_CUSTOMER_NUMBER=?";

	private static final String getChildrenVCNQuery = "select vista_customer_number from customer where parent_num = ?";
	private static final String getSpecialProgramsByVCNQuery = "select special_program_id from cust_special_programs_xref where vista_customer_number = ?";
	private static final String deleteSpecialProgramsByVCN = "delete cust_special_programs_xref where vista_customer_number = ?";
	private static final String insertSpecialProgramsForVCN = "insert into cust_special_programs_xref (vista_customer_number, special_program_id) values (?,?)";
	private static final String getAccountGeogQuery = "select sp_geog from customer where vista_customer_number = ?";
	private static final String getAccountUserAddedQuery = "select user_added from customer where vista_customer_number = ?";

	private static final String getTaskAttachmentsQuery = "select * from sales_plan_pap_files where task_id = ?";
	private static final String insertTaskAttachmentQuery = "insert into sales_plan_pap_files (sales_plan_pap_files_id,task_id,attached_file_name,date_added,date_changed,user_added,user_changed,content_type) " +
				" values (?,?,?,sysdate,sysdate,?,?,?)";
	private static final String getTaskAttachmentNextValQuery = "select sales_plan_pap_files_seq.nextval from dual";
	private static final String deleteTaskAttachmentRecord = "delete sales_plan_pap_files where sales_plan_pap_files_id = ?";
	private static final String getTaskAttachmentInfo = "select * from sales_plan_pap_files where sales_plan_pap_files_id = ?";

	private static final String deleteCustomerAddressForProspect = "DELETE FROM customer_address WHERE vista_customer_number = ?";
	private static final String deleteCustomerDivisionForProspect = "DELETE FROM customer_divisions WHERE vista_customer_number = ?";
	private static final String deleteDistributorsForProspect = "DELETE FROM distributors WHERE vista_customer_number = ?";
	private static final String deleteCustomerForProspect = "DELETE FROM customer WHERE vista_customer_number = ?";

	private static final String deleteProspect = "UPDATE customer SET customer_status = ? WHERE vista_customer_number = ?";
	//	TODO Vince: Junk not yet baked.
	//private static final String getOneCustomerQuery = "Vince: Not yet implemented properly.  See temp method below";
	//private static final String customerExistsQuery = "Vince: Not yet implemented properly.  See temp method below";


	/**
	 * I get an Account from the datasource.
	 * @param acctId  the VCN or Prospect Number
	 * @deprecated Please use AddressDAO.getAccount(String, Connection)
	 * THAT SOUNDS GREAT EXCEPT THAT METHOD DOES NOT EXIST
	 * @return an Account for the specified acctId
	 * @throws Exception
	 */
	/*
	 * TODO - remove this call from the jsp that is calling it
	 * It is called by standardReportMailingListExcel.jsp
	 * AND CALLED BY DOCUMENTSERVLET
	 */
	public static Account getAccount(String acctId) throws Exception{
		Connection DBConn = SMRCConnectionPoolUtils.getDatabaseConnection() ;
		Account acct = getAccount(acctId, DBConn);
		SMRCConnectionPoolUtils.commitTransaction(DBConn);
		SMRCConnectionPoolUtils.close(DBConn);
		return acct;
	}

	/**
	 * I get an Account from the datasource.
	 * @param acctId  the VCN or Prospect Number
	 * @param DBConn  the database connection
	 * @return an Account for the specified acctId
	 * @throws Exception
	 */
	public static Account getAccount(String acctId, Connection DBConn) throws Exception{

		Account acct = new Account();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DBConn.prepareStatement(getAccountQuery);

			pstmt.setString(1, acctId);
			rs = pstmt.executeQuery();

			while (rs.next()) {

				acct.setCustomerName(StringManipulation.noNull(rs.getString("CUSTOMER_NAME"))) ;
				acct.setProspectNumber(StringManipulation.noNull(rs.getString("PROSPECT_NUMBER"))) ;
				acct.setVcn(StringManipulation.noNull(rs.getString("VISTA_CUSTOMER_NUMBER")));
				System.out.println("DAO VCN == " + acct.getVcn());
				acct.setStatus(StringManipulation.noNull(rs.getString("CUSTOMER_STATUS")));

				acct.setParentCustNumber(StringManipulation.noNull(rs.getString("PARENT_NUM")));
				acct.setParentName((ParentDAO.getAccount(acct.getParentCustNumber(), DBConn)).getParentName());
				acct.setSalesEngineer1(StringManipulation.noNull(rs.getString("SALES_ENGINEER1")));
				acct.setSalesEngineer2(StringManipulation.noNull(rs.getString("SALES_ENGINEER2")));
				acct.setSalesEngineer3(StringManipulation.noNull(rs.getString("SALES_ENGINEER3")));
				acct.setSalesEngineer4(StringManipulation.noNull(rs.getString("SALES_ENGINEER4")));

				// district comes from users profile
				acct.setDistrict(StringManipulation.noNull(rs.getString("SP_GEOG")));
				acct.setSegments(SegmentsDAO.getSegmentsForAccount(acctId, DBConn));
				acct.setPhone(StringManipulation.noNull(rs.getString("PHONE_NUMBER")));
				acct.setFax(StringManipulation.noNull(rs.getString("FAX_NUMBER")));
				acct.setIntlPhoneNumber(StringManipulation.noNull(rs.getString("INTERNATIONAL_PHONE_NUMBER")));
				acct.setIntlFaxNumber(StringManipulation.noNull(rs.getString("INTERNATIONAL_FAX_NUMBER")));
				acct.setWebsite(StringManipulation.noNull(rs.getString("WEB_SITE")));
				acct.setContacts(ContactsDAO.getContacts(acctId, DBConn));

				if(rs.getInt("TARGET_ACCOUNT_TYPE_ID")==1 || rs.getInt("TARGET_ACCOUNT_TYPE_ID")==3){
					acct.setDistrictTargetAccount(true);
				}else{
					acct.setDistrictTargetAccount(false);
				}
				if(StringManipulation.noNull(rs.getString("TARGET_ACCOUNT_FLAG")).equals("Y")){
					acct.setTargetAccount(true);
				}else{
					acct.setTargetAccount(false);
				}
				acct.setDivisionTargetAccount(getTargetDivisions(acctId, DBConn),false);
				acct.setNumOfWhatever(StringManipulation.noNull(rs.getString("NUM_OF_VALUE")));
				acct.setBackgroundInfo(StringManipulation.noNull(rs.getString("BACKGROUND_INFORMATION")));

				acct.setGenesisNumber(StringManipulation.noNull(rs.getString("GENESIS_NUMBER")));
				acct.setDpcNum(StringManipulation.noNull(rs.getString("DPC_NUMBER")));
				acct.setStoreNumber(StringManipulation.noNull(rs.getString("STORE_ID_NUMBER")));
				acct.setSynergyCode(StringManipulation.noNull(rs.getString("SYNERGY_CODE")));
				acct.setSendToVistaNotes(StringManipulation.noNull(rs.getString("NOTES")));
				acct.setVistaReferenceNumber(StringManipulation.noNull(rs.getString("VISTA_REFERENCE_NUMBER")));
				acct.setApplicationCode(Globals.a2int(rs.getString("APPLICATION_ID")));
				acct.setFocusType(Globals.a2int(rs.getString("FOCUS_TYPE_ID")));


				acct.setIntlPhoneNumber (StringManipulation.noNull(rs.getString("INTERNATIONAL_PHONE_NUMBER")));
				acct.setIntlFaxNumber(StringManipulation.noNull(rs.getString("INTERNATIONAL_FAX_NUMBER")));
				acct.setParentOnly(StringManipulation.noNull(rs.getString("PARENT_ONLY_FLAG")));
				acct.setDirect(StringManipulation.noNull(rs.getString("DIRECT_FLAG")));
				acct.setExemptCertRequired(StringManipulation.noNull(rs.getString("EXEMPT_CERT_REQUIRED")));
				acct.setSendConfirmation(StringManipulation.noNull(rs.getString("SEND_CONFIRMATION")));
				acct.setCommitmentLevel(StringManipulation.noNull(rs.getString("COMMITMENT_LEVEL")));

				acct.setBusinessAddress(AccountDAO.getAddress(acctId, Address.DESCRIPTION_BUSINESS_ADDRESS, DBConn));
				acct.setBillToAddress(AccountDAO.getAddress(acctId, Address.DESCRIPTION_BILLING_ADDRESS, DBConn));
				acct.setShipAddress(AccountDAO.getAddress(acctId, Address.DESCRIPTION_SHIPPING_ADDRESS, DBConn));
				
				acct.setAPCont(StringManipulation.noNull(rs.getString("APCONTACT")));
				acct.setAPContPhoneNumber(StringManipulation.noNull(rs.getString("APCONTACT_PHONE_NUMBER")));
				acct.setAPContEmailAddress(StringManipulation.noNull(rs.getString("APCONTACT_EMAIL_ADDRESS")));

				acct.setDistrictManager(MiscDAO.getDistrictManagerByGeog(StringManipulation.noNull(rs.getString("SP_GEOG")), DBConn));
				acct.setZoneManager(MiscDAO.getZoneManagerByGeog(StringManipulation.noNull(rs.getString("SP_GEOG")), DBConn));
				acct.setUserIdAdded(StringManipulation.noNull(rs.getString("USER_ADDED")));
				acct.setUserIdChanged(StringManipulation.noNull(rs.getString("USER_CHANGED")));

				acct.setDateAdded(rs.getDate("DATE_ADDED"));
				acct.setDateChanged(rs.getDate("DATE_CHANGED"));
				acct.setSpecialProgramIds(AccountDAO.getSpecialProgramsForAccount(acctId,DBConn));
			}
		}catch (Exception e)	{
			SMRCLogger.error("AccountDAO.getAccount(): " , e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}

		return acct;

	}

	/**
	 * Determines if an account needs to be inserted or updated based on VCN
	 * @param acctId  the VCN or Prospect Number
	 * @param userid  the userId of the logged in user, for change tracking purposes
	 * @param DBConn  the database connection
	 * @throws Exception
	 */
	public static String saveAccount(Account account, String userid, Connection DBConn) throws Exception{
		String vcn = account.getVcn();
		if(vcn.equals("")){
			return insertNewAccount(account,userid,DBConn);
		}
		return updateAccount(account,userid,DBConn);

	}

	/**
	 * Inserts a new Account.  Account did not previously exist
	 * @param acct  the Account object
	 * @param userid  the userId of the logged in user, for change tracking purposes
	 * @param DBConn  the database connection
	 * @return the new Prospect number of the account
	 * @throws Exception
	 */
	private static String insertNewAccount(Account acct, String userid, Connection DBConn) throws Exception{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long prospectVal = -1;

		try {

			pstmt = DBConn.prepareStatement(getProspectSeqQuery);
			rs = pstmt.executeQuery();
			while (rs.next()){
				prospectVal = rs.getLong(1);
			}

			pstmt = DBConn.prepareStatement(newAccountInsert);
			int pIndex = 0 ;

			pstmt.setString( ++pIndex, "P"+prospectVal ) ;
			pstmt.setString( ++pIndex, acct.getCustomerName() ) ;
			pstmt.setString( ++pIndex, "P"+prospectVal ) ;
			pstmt.setString( ++pIndex, acct.getParentCustNumber() ) ;
			pstmt.setString( ++pIndex, acct.getSalesEngineer1() ) ;
			pstmt.setString( ++pIndex, acct.getSalesEngineer2() ) ;
			pstmt.setString( ++pIndex, acct.getSalesEngineer3() ) ;
			pstmt.setString( ++pIndex, acct.getSalesEngineer4() ) ;
			pstmt.setString( ++pIndex, acct.getDistrict()) ;
			pstmt.setString( ++pIndex, acct.getPhone() ) ;
			pstmt.setString( ++pIndex, acct.getFax() ) ;
			pstmt.setString( ++pIndex, acct.getIntlPhoneNumber() ) ;
			pstmt.setString( ++pIndex, acct.getIntlFaxNumber() ) ;
			pstmt.setString( ++pIndex, acct.getWebsite() ) ;
			if(acct.isTargetAccount()){
				pstmt.setString( ++pIndex, "Y" ) ;
			}else{
				pstmt.setString( ++pIndex, "N" ) ;
			}

			//pstmt.setString( ++pIndex, acct.getDistrictTargetAccount() ) ;
			pstmt.setInt( ++pIndex, acct.getTargetAccountType()) ;
			pstmt.setInt( ++pIndex, Integer.parseInt(acct.getNumOfWhatever())) ;
			pstmt.setString( ++pIndex, acct.getBackgroundInfo() ) ;

			pstmt.setString( ++pIndex, acct.isDirect() ? "Y" : "N" ) ;
			pstmt.setString( ++pIndex, acct.isParentOnly() ? "Y" : "N" ) ;
			pstmt.setString( ++pIndex, acct.isSendConfirmation() ? "Y" : "N" ) ;
			pstmt.setString( ++pIndex, acct.isExemptCertRequired() ? "Y" : "N" ) ;

			pstmt.setString( ++pIndex, "Prospect" ) ;

			pstmt.setString( ++pIndex, acct.getGenesisNumber());
			pstmt.setString( ++pIndex, acct.getDpcNum());
			pstmt.setString( ++pIndex, acct.getStoreNumber());
			pstmt.setString( ++pIndex, acct.getSynergyCode());
			pstmt.setString( ++pIndex, acct.getSendToVistaNotes());
			pstmt.setInt( ++pIndex, acct.getApplicationCode());
			pstmt.setString( ++pIndex, userid);
			pstmt.setString( ++pIndex, userid);
			
			pstmt.setString( ++pIndex, acct.getAPCont());
			pstmt.setString( ++pIndex, acct.getAPContPhoneNumber());
			pstmt.setString( ++pIndex, acct.getAPContEmailAddress());

			pstmt.executeUpdate();

			setTargetDivisions("P"+prospectVal,acct.getDivisionTargetAccount(), DBConn);
			setAddress("P"+prospectVal,46,acct.getBusinessAddress(), DBConn);
			setAddress("P"+prospectVal,47,acct.getShipAddress(), DBConn);
			setAddress("P"+prospectVal,48,acct.getBillToAddress(), DBConn);
			MiscDAO.setFocusType("P"+prospectVal,acct.getFocusType(), DBConn);
			if(acct.getDistributorStatementContact()!=0){
				setDistributorStatementContact(acct.getVcn(),acct.getDistributorStatementContact(),DBConn);
			}

			SegmentsDAO.saveSegments(acct.getSegments(),"P"+prospectVal, DBConn);
			DistributorDAO.initializeDistributor("P"+prospectVal,DBConn);
			// Setting the vcn as the prospect number so it can be used for the special program update
			if (acct.getVcn().equals("")){
			    acct.setVcn("P"+prospectVal);
			}
			updateSpecialProgramsForAccount(acct,DBConn);

		}catch (Exception e)	{
			SMRCLogger.error("AccountDAO.insertNewAccount(): " , e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return "P" + prospectVal ;
	}



	/**
	 * Updates an existing Account
	 * @param acct  the Account object
	 * @param userid  the userId of the logged in user, for change tracking purposes
	 * @param DBConn  the database connection
	 * @return the acctID which is the VCN or Prospect number of the account
	 * @throws Exception
	 */
	private static String updateAccount(Account acct, String userid, Connection DBConn) throws Exception  {
		PreparedStatement pstmt = null;

		try {
			pstmt = DBConn.prepareStatement(updateAccountUpdate);

			int pIndex = 0 ;
			pstmt.setString( ++pIndex, acct.getCustomerName() ) ;
			pstmt.setString( ++pIndex, acct.getParentCustNumber() ) ;
			pstmt.setString( ++pIndex, acct.getSalesEngineer1() ) ;
			pstmt.setString( ++pIndex, acct.getSalesEngineer2() ) ;
			pstmt.setString( ++pIndex, acct.getSalesEngineer3() ) ;
			pstmt.setString( ++pIndex, acct.getSalesEngineer4() ) ;
			pstmt.setString( ++pIndex, acct.getDistrict() ) ;
			pstmt.setString( ++pIndex, acct.getPhone() ) ;
			pstmt.setString( ++pIndex, acct.getFax() ) ;
			pstmt.setString( ++pIndex, acct.getIntlPhoneNumber() ) ;
			pstmt.setString( ++pIndex, acct.getIntlFaxNumber() ) ;
			pstmt.setString( ++pIndex, acct.getWebsite() ) ;

			pstmt.setString( ++pIndex, acct.isTargetAccount() ? "Y" :"N" ) ;

			pstmt.setInt( ++pIndex, acct.getTargetAccountType()) ;
			pstmt.setInt( ++pIndex, Integer.parseInt(acct.getNumOfWhatever()) ) ;
			pstmt.setString( ++pIndex, acct.getBackgroundInfo() ) ;

			pstmt.setString( ++pIndex, acct.isDirect() ? "Y" : "N" ) ;
			pstmt.setString( ++pIndex, acct.isParentOnly() ? "Y" : "N" ) ;
			pstmt.setString( ++pIndex, acct.isSendConfirmation() ? "Y" : "N" ) ;
			pstmt.setString( ++pIndex, acct.isExemptCertRequired() ? "Y" : "N" ) ;

			pstmt.setString( ++pIndex, acct.getGenesisNumber());
			pstmt.setString( ++pIndex, acct.getDpcNum());
			pstmt.setString( ++pIndex, acct.getStoreNumber());
			pstmt.setString( ++pIndex, acct.getSynergyCode());
			pstmt.setString( ++pIndex, acct.getSendToVistaNotes());
			pstmt.setInt( ++pIndex, acct.getApplicationCode());
			pstmt.setString( ++pIndex, userid);
			
			pstmt.setString( ++pIndex, acct.getAPCont());
			pstmt.setString( ++pIndex, acct.getAPContPhoneNumber());
			pstmt.setString( ++pIndex, acct.getAPContEmailAddress());

			pstmt.setString( ++pIndex, acct.getVcn() ) ;
			pstmt.executeUpdate();

			SegmentsDAO.saveSegments(acct.getSegments(),acct.getVcn(), DBConn);

			setTargetDivisions(acct.getVcn(),acct.getDivisionTargetAccount(), DBConn);

			setAddress(acct.getVcn(),46,acct.getBusinessAddress(), DBConn);
			setAddress(acct.getVcn(),47,acct.getShipAddress(), DBConn);
			setAddress(acct.getVcn(),48,acct.getBillToAddress(), DBConn);

			MiscDAO.setFocusType(acct.getVcn(),acct.getFocusType(), DBConn);
			updateSpecialProgramsForAccount(acct,DBConn);

			if(acct.getDistributorStatementContact()!=0){
				setDistributorStatementContact(acct.getVcn(),acct.getDistributorStatementContact(),DBConn);
			}


		}catch (Exception e)	{
			SMRCLogger.error("AccountDAO.updateAccount(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}

		return acct.getVcn();

	}

	/**
	 * I get an Address from the datastore.
	 * @param acctId  the VCN or Prospect Number
	 * @param addressTypeDescription  the string that corresponds to the type of
	 * 			address to get.  The address types are in the CODE_TYPES table
	 * @deprecated Please use AddressDAO.getAddress(String, String, Connection)
	 * @return an Address of the specified type.
	 * @throws Exception
	 */
	/*
	 * TODO this is called from standardReportMailingListExcel.jsp, which
	 * doesnt have a connection object to pass.... needs fixed?
	 */
	public static Address getAddress(String acctId, String addressTypeDescription) throws Exception{
		Connection DBConn = SMRCConnectionPoolUtils.getDatabaseConnection() ;
		Address addr = getAddress(acctId, addressTypeDescription, DBConn);
		SMRCConnectionPoolUtils.commitTransaction(DBConn);
		SMRCConnectionPoolUtils.close(DBConn);
		return addr;

	}

	/**
	 * I get an Address from the datastore.
	 * @param acctId  the VCN or Prospect Number
	 * @param addressTypeDescription  the string that corresponds to the type of
	 * 			address to get.  The address types are in the CODE_TYPES table
	 * @param DBConn  the database connection
	 * @return an Address of the specified type.
	 * @throws Exception
	 */
	private static Address getAddress(String acctId, String addressTypeDescription, Connection DBConn) throws Exception{

		Address address = new Address();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DBConn.prepareStatement(getAddressQuery);

			pstmt.setString(1, addressTypeDescription); //table=CODE_TYPES column=CODE_DESCRIPTOIN
			pstmt.setString(2, acctId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				address.setAddress1(StringManipulation.noNull(rs.getString("ADDRESS_LINE1")));
				address.setAddress2(StringManipulation.noNull(rs.getString("ADDRESS_LINE2")));
				address.setAddress3(StringManipulation.noNull(rs.getString("ADDRESS_LINE3")));
				address.setAddress4(StringManipulation.noNull(rs.getString("ADDRESS_LINE4")));
				address.setCity(StringManipulation.noNull(rs.getString("CITY")));
				address.setState(StringManipulation.noNull(rs.getString("STATE")));
				address.setZip(StringManipulation.noNull(rs.getString("ZIP")));
				address.setCountry(StringManipulation.noNull(rs.getString("COUNTRY")));
				address.setCountryName(StringManipulation.noNull(rs.getString("COUNTRY_NAME")));
				address.setVistaCountryCode(StringManipulation.noNull(rs.getString("COUNTRY_CODE")));
			}

		}
		catch (Exception e)	{
			SMRCLogger.error("AccountDAO.getAddress(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}

		return address;


	}

	/**
	 * I determine if an address should be updated or inserted and call the appropriate method
	 * @param acctId  the VCN or Prospect Number
	 * @param addressTypeId  the id that corresponds to the type of
	 * 			address to get.  The address types are in the CODE_TYPES table
	 * @param address  the Address object to save
	 * @param DBConn  the database connection
	 * @throws Exception
	 */
	private static void setAddress(String acctId, int addressTypeId, Address address, Connection DBConn) throws Exception{
		boolean exists=false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			pstmt = DBConn.prepareStatement(doesAddrExistQuery);
			pstmt.setString(1,acctId);
			pstmt.setInt(2,addressTypeId);
			rs=pstmt.executeQuery();
			while (rs.next())
			{
				if(rs.getInt(1)!=0){
					exists=true;
				}
			}
			if(exists){
				updateAddress(acctId, addressTypeId, address, DBConn);
			}else{
				insertAddress(acctId, addressTypeId, address, DBConn);
			}

		}
		catch (Exception e)	{
			SMRCLogger.error("AccountDAO.setAddress(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}

	}

	/**
	 * I save an Address to the datastore via INSERT.
	 * @param acctId  the VCN or Prospect Number
	 * @param addressTypeId  the id that corresponds to the type of
	 * 			address to get.  The address types are in the CODE_TYPES table
	 * @param address  the Address object to save
	 * @param DBConn  the database connection
	 * @throws Exception
	 */
	public static void insertAddress(String acctId, int addressTypeId, Address address, Connection DBConn) throws Exception{

		PreparedStatement pstmt = null;

		try {
			int pIndex=0;
			pstmt = DBConn.prepareStatement(setAddressInsert);

			pstmt.setString( ++pIndex,acctId);
			pstmt.setInt( ++pIndex,addressTypeId);
			pstmt.setInt( ++pIndex,1);
			pstmt.setString( ++pIndex,address.getAddress1());
			pstmt.setString( ++pIndex,address.getAddress2());
			pstmt.setString( ++pIndex,address.getAddress3());
			pstmt.setString( ++pIndex,address.getAddress4());
			pstmt.setString( ++pIndex,address.getCity());
			pstmt.setString( ++pIndex,address.getState());
			pstmt.setString( ++pIndex,address.getZip());
			pstmt.setString( ++pIndex,address.getCountry());

			pstmt.executeUpdate();

		}
		catch (Exception e)	{
			SMRCLogger.error("AccountDAO.insertAddress(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);

		}


	}

	/**
	 * I save an Address to the datastore via UPDATE
	 * @param acctId  the VCN or Prospect Number
	 * @param addressTypeId  the id that corresponds to the type of
	 * 			address to get.  The address types are in the CODE_TYPES table
	 * @param address  the Address object to save
	 * @param DBConn  the database connection
	 * @throws Exception
	 */
	public static void updateAddress(String acctId, int addressTypeId, Address address, Connection DBConn) throws Exception{
		PreparedStatement pstmt = null;

		try {
			int pIndex = 0 ;
			pstmt = DBConn.prepareStatement(setAddressUpdate);

			pstmt.setString( ++pIndex,address.getAddress1());
			pstmt.setString( ++pIndex,address.getAddress2());
			pstmt.setString( ++pIndex,address.getAddress3());
			pstmt.setString( ++pIndex,address.getAddress4());
			pstmt.setString( ++pIndex,address.getCity());
			pstmt.setString( ++pIndex,address.getState());
			pstmt.setString( ++pIndex,address.getZip());
			pstmt.setString( ++pIndex,address.getCountry());
			pstmt.setString( ++pIndex,acctId);
			pstmt.setInt( ++pIndex,addressTypeId);

			pstmt.executeUpdate();
		}
		catch (Exception e)	{
			SMRCLogger.error("AccountDAO.updateAddress(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);

		}

	}

	/*
	private static void setSegments(String acctId,ArrayList segs, Connection DBConn) throws Exception{

		PreparedStatement pstmt = null;
		ResultSet rs = null;


		if(segs.size()!=0){
			try {
				pstmt = DBConn.prepareStatement(doesSegmentExist);

				for(int i=0;i<segs.size();i++){

					pstmt.setString(1,acctId);
					pstmt.setInt(2,Integer.parseInt((String)segs.get(i)));
					rs=pstmt.executeQuery();
					rs.next();
					if(rs.getInt(1)!=0){
						//updateSegment(acctId, (String)segs.get(i));
					}else{
						insertSegment(acctId, (String)segs.get(i), DBConn);
					}
				}
			}
			catch (Exception e)	{
				SMRCLogger.error("AccountDAO.setSegments(): " , e);

				throw e;
			}
			finally {
				SMRCConnectionPoolUtils.close(rs);
				SMRCConnectionPoolUtils.close(pstmt);

			}

		}

	}





	private static void insertSegment(String acctId,String segId, Connection DBConn) throws Exception {

		PreparedStatement pstmt = null;


		try {
			pstmt = DBConn.prepareStatement(setSegmentInsert);

			int pIndex = 0 ;
			pstmt.setString( ++pIndex,acctId);
			pstmt.setInt( ++pIndex,Integer.parseInt(segId));
			pstmt.executeUpdate();

		}
		catch (Exception e)	{
			SMRCLogger.error("AccountDAO.insertSegment(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);

		}

	}
	*/


	/**
	 * Gets the ids of target divisions for an Account
	 * @param acctId  the VCN or Prospect Number
	 * @param DBConn  the database connection
	 * @return an Array of division Ids
	 * @throws Exception
	 */
	public static String[] getTargetDivisions(String acctId, Connection DBConn) throws Exception{

		String[] divisionIDs = null;
		ArrayList divisions = new ArrayList();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DBConn.prepareStatement(targetDivisionsQuery);
			pstmt.setString(1, acctId);
			rs = pstmt.executeQuery();

			while (rs.next())
			{
				divisions.add(rs.getString("DIVISION_ID"));
			}
			divisionIDs = new String[divisions.size()];
			for(int i=0;i<divisions.size();i++){
				divisionIDs[i]=(String)divisions.get(i);
			}

		}
		catch (Exception e)	{
			SMRCLogger.error("AccountDAO.getTargetDivisions(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);

		}

		return divisionIDs;

	}

	/**
	 * Sets the ids of target divisions for an Account
	 * @param acctId  the VCN or Prospect Number
	 * @param divisions  the divisions to set as target divisions in an Account
	 * @param DBConn  the database connection
	 * @throws Exception
	 */
	private static void setTargetDivisions(String acctId,ArrayList divisions, Connection DBConn) throws Exception{

		PreparedStatement pstmt = null;


		try {
			pstmt = DBConn.prepareStatement(targetDivisionsDelete);
			pstmt.setString(1,acctId);
			SMRCLogger.debug("SQL AccountDAO.setTargetDivisions() - delete:\n" + targetDivisionsDelete);
			SMRCLogger.debug("SQL AccountDAO.setTargetDivisions() - delete setString 1: " + acctId);


			int deletedRecords = pstmt.executeUpdate();
			SMRCLogger.debug("SQL AccountDAO.setTargetDivisions() - " + deletedRecords + " rows deleted\n");

			pstmt = DBConn.prepareStatement(targetDivisionsInsert);
			SMRCLogger.debug("SQL AccountDAO.setTargetDivisions() - insert:\n" + targetDivisionsInsert);

			for(int i=0;i<divisions.size();i++){
				int pIndex = 0 ;
				pstmt.setString( ++pIndex,acctId);
				pstmt.setInt( ++pIndex,Integer.parseInt((String)divisions.get(i)));
				SMRCLogger.debug("SQL AccountDAO.setTargetDivisions() - setInt " + pIndex + ": " + (String)divisions.get(i));
				pstmt.executeUpdate();
			}


		}
		catch (Exception e)	{
			SMRCLogger.error("AccountDAO.setTargetDivisions(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);

		}

	}

	/**
	 * Gets the distributor ID of an account
	 * @param acctId  the VCN or Prospect Number
	 * @param DBConn  the database connection
	 * @return the distributor ID
	 * @throws Exception
	 */
	public static int getDistributorId(String acctId, Connection DBConn) throws Exception{
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DBConn.prepareStatement(getDistributorIdQuery);
			pstmt.setString(1, acctId);
			rs = pstmt.executeQuery();
			int returnId=0;
			while (rs.next())
			{
				returnId = rs.getInt("DISTRIBUTOR_ID");
			}
			return returnId;


		}
		catch (Exception e)	{
			SMRCLogger.error("AccountDAO.getTargetDivisions(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);

		}

	}

	/**
	 * Gets the name of an Account
	 * @param acctId  the VCN or Prospect Number
	 * @param DBConn  the database connection
	 * @return the name of the Account
	 * @throws Exception
	 */
	public static String getAccountName(String acctId, Connection DBConn) throws Exception{
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DBConn.prepareStatement(getAccountNameQuery);
			pstmt.setString(1, acctId);
			rs = pstmt.executeQuery();
			String accountName="";
			while (rs.next())
			{
				accountName = rs.getString("CUSTOMER_NAME");
			}
			return accountName;

		}
		catch (Exception e)	{
			SMRCLogger.error("AccountDAO.getTargetDivisions(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);

		}

	}

	/**
	 * Gets non Prospect accounts that are parents of themselves (vcn=parent number) by name
	 * @param searchString  the name of the account to find
	 * @param DBConn  the database connection
	 * @return the Accounts that meet the criteria
	 * @throws Exception
	 */
	public static ArrayList parentSearch(String searchString, Connection DBConn, int firstRecordNumber) throws Exception{

		ArrayList accountList = new ArrayList();
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try {
			SMRCLogger.debug("SQL - AccountDAO.parentSearch():\n" + parentSearchQuery + "\n? = " + searchString);
			pstmt = DBConn.prepareStatement(parentSearchQuery);
			pstmt.setString(1,searchString + "%");
			pstmt.setInt(2, firstRecordNumber);
			pstmt.setInt(3, firstRecordNumber + 50);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Account account = new Account();
				Address addr = new Address();
				account.setVcn(rs.getString("vista_customer_number"));
				account.setCustomerName(rs.getString("customer_name"));
				addr.setZip(StringManipulation.noNull(rs.getString("zip")));
				addr.setState(StringManipulation.noNull(rs.getString("state")));
				addr.setCity(StringManipulation.noNull(rs.getString("city")));
				account.setBusinessAddress(addr);
				accountList.add(account);
			}

		}
		catch (Exception e)	{
			SMRCLogger.error("AccountDAO.parentSearch(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);

		}

		return accountList;

	}

	/**
	 * Gets Accounts by name
	 * @param acctName  the name of the account to find
	 * @param vcn  the vcn of the account to find (if acctName is empty)
	 * @param DBConn  the database connection
	 * @return the Accounts that meet the criteria
	 * @throws Exception
	 */
	public static ArrayList accountBrowseSearch(String acctName, String vcn, Connection DBConn, boolean distributorsOnly, int firstRecordNumber ) throws Exception{

		ArrayList records = new ArrayList();
		ResultSet rs = null;

		PreparedStatement pstmt = null;

		try {
			if(!acctName.equals("")){
				if (distributorsOnly){
					pstmt = DBConn.prepareStatement(distributorBrowseQuery1);
					SMRCLogger.debug("SQL - AccountDAO.accountBrowseSearch():\n" + distributorBrowseQuery1 + "\n? = " + acctName);
				} else {
					pstmt = DBConn.prepareStatement(accountBrowseQuery1);
					SMRCLogger.debug("SQL - AccountDAO.accountBrowseSearch():\n" + accountBrowseQuery1 + "\n? = " + acctName);
				}
				pstmt.setString(1,acctName.trim() + "%");
			}else{
				if (distributorsOnly){
					pstmt = DBConn.prepareStatement(distributorBrowseQuery2);
					SMRCLogger.debug("SQL - AccountDAO.accountBrowseSearch():\n" + distributorBrowseQuery2 + "\n? = " + acctName);
				} else {
					pstmt = DBConn.prepareStatement(accountBrowseQuery2);
					SMRCLogger.debug("SQL - AccountDAO.accountBrowseSearch():\n" + accountBrowseQuery2 + "\n? = " + acctName);
				}
				pstmt.setString(1,vcn.trim() + "%");
			}


			// Set the current page indexing

			pstmt.setInt(2, firstRecordNumber);
			pstmt.setInt(3, firstRecordNumber + 50); // Get one extra record to see if there is another page

			// Execute and get returns

			rs = pstmt.executeQuery();
			while(rs.next()){
				AccountBrowseRecord record = new AccountBrowseRecord();
				record.setVcn(rs.getString("VISTA_CUSTOMER_NUMBER"));
				record.setCustomerName(rs.getString("CUSTOMER_NAME"));
				record.setCity(StringManipulation.noNull(rs.getString("CITY")));
				record.setState(StringManipulation.noNull(rs.getString("STATE")));
				record.setZip(StringManipulation.noNull(rs.getString("ZIP")));
				record.setParentNumber(StringManipulation.noNull(rs.getString("parent_num")));
				records.add(record);
			}

		}
		catch (Exception e)	{
			SMRCLogger.error("AccountDAO.accountBrowseSearch(): " , e);

			throw e;
		}
		finally{
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);

		}

		return records;

	}


	/**
	 * Gets users associated to an account from the user profile page
	 * @param acctId  the VCN or prospect number of the account
	 * @param DBConn  the database connection
	 * @return the users that meet the criteria
	 * @throws Exception
	 */
	public static ArrayList getAssociatedUsers(String acctId, Connection DBConn) throws Exception{

		ArrayList users = new ArrayList();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DBConn.prepareStatement(getAssociatedUsersQuery);

			pstmt.setString(1, acctId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				User user = new User();
				user.setUserid(rs.getString("USERID"));
				user.setFirstName(StringManipulation.noNull(rs.getString("FIRST_NAME")));
				user.setLastName(StringManipulation.noNull(rs.getString("LAST_NAME")));
				user.setEmailAddress(StringManipulation.noNull(rs.getString("EMAIL_ADDRESS")));
				users.add(user);
			}
		}
		catch (Exception e)	{
			SMRCLogger.error("AccountDAO.getContacts(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);

		}

		return users;
	}

	/**
	 * Adds or removes an associated user to an account
	 * @param userId  the id of the user to associate or remove
	 * @param vcn  the VCN or prospect number of the account
	 * @param deleteUser  true if its a delete, false if its an add
	 * @param DBConn  the database connection
	 * @throws Exception
	 */
	public static void updateAssociatedUser(String userId, String vcn,  boolean deleteUser, Connection DBConn) throws Exception{

		PreparedStatement pstmt = null;

		try {
			if(deleteUser){
				pstmt = DBConn.prepareStatement(associatedUserDelete);
			}else{
				pstmt = DBConn.prepareStatement(associatedUserInsert);
			}
			pstmt.setString(1,userId);
			pstmt.setString(2,vcn);
			pstmt.executeUpdate();

		}catch(SQLException se) {
			/*
			 * If a user associates themself to an account they are already
			 * associated to, it will throw an ORA-00001 error.  This is OK, so
			 * just log it as an info, otherwise, log an error and throw it.
			 */
		    if(se.getErrorCode()==1) {
		        // Error Code of 1 means unique constraint violation which is OK
		        SMRCLogger.info("AccountDAO.updateAssociatedUser(): Expected unique constraint violation");
            }else {
                SMRCLogger.error("AccountDAO.updateAssociatedUser(): ", se);
                throw se;
            }
		}catch (Exception e)	{
			SMRCLogger.error("AccountDAO.updateAssociatedUser(): " , e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);

		}

	}

	/**
	 * Gets the sales figures for the SE Homepage
	 * @param acctId  the VCN or prospect number of the account
	 * @param showSalesOrders  whether or not to show Sales or Orders figures
	 * @param salesId  the salesman ID of the logged in user
	 * @param DBConn  the database connection
	 * @return  the $ values for the SE Homepage
	 * @throws Exception
	 */
	public static AccountNumbers getSEHomeNumbers(String acctId, String showSalesOrders, User user, int srYear, int srMonth, Connection DBConn) throws Exception{
		AccountNumbers numbers = new AccountNumbers();

		//PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;

		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("with cp as ( select customer_product.vista_customer_number, ");
		queryBuffer.append("sum(case when customer_product.period_yyyy = " + srYear + " then customer_product.potential_dollars else 0 end) potential_dollars,  ");
		queryBuffer.append("sum(case when customer_product.period_yyyy = " + srYear + " then customer_product.forecast_dollars else 0 end) forecast_dollars, ");
		queryBuffer.append("sum(case when customer_product.period_yyyy = " + srYear + " then customer_product.competitor_dollars else 0 end) competitor1_dollars, ");
		queryBuffer.append("sum(case when customer_product.period_yyyy = " + srYear + " then customer_product.competitor2_dollars else 0 end) competitor2_dollars, ");
		queryBuffer.append("customer_product.competitor1_id, customer_product.competitor2_id from customer_product, products where ");
		queryBuffer.append("customer_product.vista_customer_number = '" + acctId + "' ");
		queryBuffer.append("and products.sp_load_total = 'T' ");
		queryBuffer.append("and products.period_yyyy = " + srYear + " ");
		queryBuffer.append("and products.product_id = customer_product.product_id ");
		queryBuffer.append("group by customer_product.vista_customer_number, customer_product.competitor1_id, customer_product.competitor2_id ) ");
		queryBuffer.append("select credit_customer_sales.credit_customer_number, max(cp.forecast_dollars) forecast_dollars, ");
		queryBuffer.append("max(cp.potential_dollars) potential_dollars, max(cp.competitor1_dollars) competitor1_dollars, ");
		queryBuffer.append("max(cp.competitor2_dollars) competitor2_dollars, ");
        queryBuffer.append("cp.competitor1_id, cp.competitor2_id, ");
        queryBuffer.append("sum(case when year = " + srYear + " then credit_customer_sales.total_" + showSalesOrders + " else 0 end) curr_year_total, ");
        queryBuffer.append("sum(case when year = " + (srYear - 1) + " then credit_customer_sales.total_" + showSalesOrders + " else 0 end) prev_year_total, ");
		queryBuffer.append("sum(case when year = " + (srYear - 2) + " then credit_customer_sales.total_" + showSalesOrders + " else 0 end) prev_2year_total, ");
        queryBuffer.append("sum(case when (month <= " + srMonth + ") and (year = " + (srYear - 1) + ") then credit_customer_sales.total_" + showSalesOrders + "  else 0 end) prev_ytd_total ");
        queryBuffer.append("from credit_customer_sales, cp, products where credit_customer_sales.credit_customer_number(+) =  '" + acctId + "' and ");
        queryBuffer.append("credit_customer_sales.credit_customer_number = cp.vista_customer_number(+) and ");
        queryBuffer.append("credit_customer_sales.salesman_id in (" + user.salesIdsToQuotedString() + ") ");
		queryBuffer.append("and products.product_id = credit_customer_sales.product_id and products.sp_load_total = 'T' and ");
        queryBuffer.append("products.period_yyyy = " + srYear + " ");
	    queryBuffer.append("group by credit_customer_sales.credit_customer_number, cp.competitor1_id, cp.competitor2_id");

		SMRCLogger.debug("AcountDAO.getSEHomeNumbers() SQL:\n" + queryBuffer);

		try {
			stmt = DBConn.createStatement();
			rs = stmt.executeQuery(queryBuffer.toString());

			while (rs.next())
			{
				numbers.setCurrentYTD(rs.getDouble("curr_year_total"));
				numbers.setCurrentMinusOneYTD(rs.getDouble("prev_ytd_total"));
				numbers.setCurrentTotal(rs.getDouble("curr_year_total"));
				numbers.setCurrentMinusOneTotal(rs.getDouble("prev_year_total"));
				numbers.setCurrentMinusTwoTotal(rs.getDouble("prev_2year_total"));
				numbers.setPotential(rs.getDouble("potential_dollars"));
				numbers.setForecast(rs.getDouble("forecast_dollars"));
				numbers.setCompetitor1Dollars(rs.getDouble("competitor1_dollars"));
				numbers.setCompetitor2Dollars(rs.getDouble("competitor2_dollars"));
				numbers.setCompetitor1(MiscDAO.getVendorInfo(rs.getInt("competitor1_id"), DBConn));
				numbers.setCompetitor2(MiscDAO.getVendorInfo(rs.getInt("competitor2_id"), DBConn));
			}
		}
		catch (Exception e)	{
			SMRCLogger.error("AccountDAO.getSEHomeNumbers(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(stmt);

		}

		return numbers;
	}

	/**
	 * Gets the users that are CCed on a sales plan
	 * @param taskId  the ID of the task
	 * @param DBConn  the database connection
	 * @return  the users that are cced on the sales plan
	 * @throws Exception
	 */
	public static ArrayList getSalesPlanCCUsers(int taskId, Connection DBConn) throws Exception {

		ArrayList userIds=new ArrayList();

		PreparedStatement pstmt=null;
		ResultSet rs = null;
		try {
			pstmt = DBConn.prepareStatement(getCCemailsQuery);
			pstmt.setInt(1,taskId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				userIds.add(rs.getString("CC_ID"));
			}
		}
		catch (Exception e) {
			SMRCLogger.error("AccountDAO.getSalesPlanCCUsers(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}

		return userIds;
	}

	/**
	 * Deletes a CC user from a sales plan
	 * @param taskId  the ID of the task
	 * @param DBConn  the database connection
	 * @return  true if success, false if failed
	 * @throws Exception
	 */
	public static boolean deleteSalesPlanCCUsers(int taskId, Connection DBConn) throws Exception {

		PreparedStatement pstmt=null;

		try {
			pstmt = DBConn.prepareStatement(deleteSalesPlanCCUsers);
			pstmt.setInt(1,taskId);
			pstmt.executeUpdate();
		}
		catch (Exception e) {
			SMRCLogger.error("AccountDAO.deleteSalesPlanCCUsers(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);

		}
		return true;
	}

	/**
	 * Adds a CC user from a sales plan
	 * @param taskId  the ID of the task
	 * @param ccUserId  the ID user to add
	 * @param DBConn  the database connection
	 * @return  true if success, false if failed
	 * @throws Exception
	 */
	public static boolean insertSalesPlanCCUsers(int taskId, String ccUserId, Connection DBConn) throws Exception {
		PreparedStatement pstmt=null;
		int count=0;
		try {
			pstmt = DBConn.prepareStatement(insertSalesPlanCCUsers);
			pstmt.setInt(1,taskId);
			pstmt.setString(2,ccUserId);
			count = pstmt.executeUpdate();
		}
		catch (Exception e)	{
			SMRCLogger.error("AccountDAO.insertSalesPlanCCUsers(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);

		}
		if(count!=0){
			return true;
		}

		return false;

	}

	/**
	 * Gets the accounts for the Customer listing page
	 * @param criteria  the criteria for the search
	 * @param DBConn  the database connection
	 * @return  accounts that meet the criteria
	 * @throws Exception
	 */

	//Function changed to accept Customer status from Customerlisting.java file

	public static ArrayList getCustomerListing(CustomerSearchCriteria criteria,String customerStatusNotShown,Connection DBConn) throws Exception{
		ArrayList accounts = new ArrayList();

		if(criteria.isValidCriteria()){

			Statement stmt = null;
			ResultSet rs = null;
			StringBuffer queryBuffer = new StringBuffer();
			StringBuffer tableBuffer = new StringBuffer();

			//boolean useAnd=false;

			String salesOrders = null;

			if (criteria.getDollarType().equalsIgnoreCase("invoice")) {
				salesOrders="sales";
			} else {
				salesOrders="order";
			}



//	Braffet : Tap Dollar Change		 Casey - we will no longer need to take dollar type into account

/*			String dollarType =  null;

			if(criteria.isEndMarketDollars()){
				dollarType="END_MKT";
			}else if(criteria.isDirectDollars()){
				dollarType="DIRECT";
			}else{
				dollarType="DIRECT";
			}

*/
// End Casey dollar type

			//}else if(criteria.isDistrictTargets()){
			if(criteria.isDistrictTargets()){
				queryBuffer.append(" AND a.target_account_flag='Y'");
				if (criteria.isDivisionTargets()){
					queryBuffer.append(" and (a.target_account_type_id = 2 or a.target_account_type_id = 3)");
				}
				boolean first = true;
				StringBuffer userGeogBuffer = new StringBuffer();
				ArrayList ugsList = criteria.getUser().getAllUGS();

				for (int i=0; i < ugsList.size(); i++){

					UserGeogSecurity ugs = (UserGeogSecurity) ugsList.get(i);
					if (ugs.ableToViewSalesman()){
						String tmp = getGeogFilterString(ugs);
						if(!tmp.equals("")){
							if(!first){
								userGeogBuffer.append(" or");
							}else{
								userGeogBuffer.append(" and (");
								first = false;
							}
						}
						userGeogBuffer.append(tmp);
					}
				}
				queryBuffer.append(userGeogBuffer.toString());
				if(first==false){
					queryBuffer.append(")");
				}else{
					return accounts;
					// added this because the query should not
					// execute if there arent ANY geogs for the user
				}

			}else if(!criteria.getSegment().equals("")){
				tableBuffer.append(", customer_segments b");
				queryBuffer.append(" AND c.vista_customer_number=b.vista_customer_number");
				queryBuffer.append(" and b.segment_id=" + criteria.getSegment());


			}else{
				if(!criteria.getVcn().equals("")){
					queryBuffer.append(" AND c.vista_customer_number like '"+ criteria.getVcn() + "%'");
					//useAnd=true;
				}
				if(!criteria.getCustomerName().equals("")){
					queryBuffer.append(" AND LOWER(c.customer_name) LIKE LOWER('" + criteria.getCustomerName() + "%')\n");
					//useAnd=true;
				}
				if(criteria.isParentsOnly()){
					//if(useAnd){
					//	queryBuffer.append(" AND");
					//}
					//useAnd=true;
					queryBuffer.append(" AND c.vista_customer_number=c.parent_num\n");
				}

				if(!criteria.getState().equals("0")){
					//if(useAnd){
					//	queryBuffer.append(" AND");
					//}
					//useAnd=true;
					queryBuffer.append(" AND a.STATE='" + criteria.getState() + "'\n");
				}

				if(!criteria.getDistrict().equals("0")){
					//if(useAnd){
					//	queryBuffer.append(" AND");
					//}
					//useAnd=true;

					queryBuffer.append(" AND c.sp_geog like '" + criteria.getDistrict() + "%'\n");
				}

				if(!criteria.getDivision().equals("0")){
					//if(useAnd){
					//	queryBuffer.append(" AND");
					//}
					//useAnd=true;
					tableBuffer.append(", customer_divisions ");
					queryBuffer.append(" AND customer_divisions.division_id = " + criteria.getDivision() + "\n");
					queryBuffer.append(" and c.vista_customer_number = customer_divisions.vista_customer_number \n");
				}

				if(!criteria.getFirstName().equals("")){
					//if(useAnd){
					//	queryBuffer.append(" AND");
					//}
					//useAnd=true;
					queryBuffer.append(" AND LOWER(a.sales_engineer1_first_name) LIKE LOWER('" + criteria.getFirstName() + "%')\n");
				}

				if(!criteria.getLastName().equals("")){
					//if(useAnd){
					//	queryBuffer.append(" AND");
					//}
					//useAnd=true;
					queryBuffer.append(" AND LOWER(a.sales_engineer1_last_name) LIKE LOWER('" + criteria.getLastName() + "%')\n");
				}
				if(criteria.isPotentialCustomersOnly()){
					//if(useAnd){
					//	queryBuffer.append(" AND");
					//}
					//useAnd=true;
					queryBuffer.append(" AND c.vista_customer_number like 'P%'\n");

				}
				if(criteria.getSeId().equals("true")){
					//if(useAnd){
					//	queryBuffer.append(" AND");
					//}
					//useAnd=true;
					//queryBuffer.append(" AND c.sales_engineer1='" + criteria.getSeId() + "'\n");

				    if(criteria.getSalesIds().size()==0) {
				        /*
				         * If the code reaches here, we are looking for accounts
				         * for this salesman, but if he/she has no sales ids
				         * then there cant be any accounts.  return empty arraylist
				         */
				        return accounts;
				    }else{
				        queryBuffer.append(" AND (");
				        queryBuffer.append(" c.sales_engineer1 in (" + criteria.salesIdsToString() + ") or \n");
						queryBuffer.append(" c.sales_engineer2 in (" + criteria.salesIdsToString() + ") or \n");
						queryBuffer.append(" c.sales_engineer3 in (" + criteria.salesIdsToString() + ") or \n");
						queryBuffer.append(" c.sales_engineer4 in (" + criteria.salesIdsToString() + ") \n");
						queryBuffer.append(") \n");
						/*
						boolean useOr = false;
					    queryBuffer.append(" AND (");
					    Iterator it = criteria.getSalesIds().iterator();
						while(it.hasNext()) {
						    String salesId = (String)it.next();
						    if(useOr) queryBuffer.append(" or ");
						    queryBuffer.append(" c.sales_engineer1='" + salesId + "' or \n");
							queryBuffer.append(" c.sales_engineer2='" + salesId + "' or \n");
							queryBuffer.append(" c.sales_engineer3='" + salesId + "' or \n");
							queryBuffer.append(" c.sales_engineer4='" + salesId + "' \n");
							useOr=true;
						}
						queryBuffer.append(") \n");
*/
						if(criteria.isTargetOnly()){
							queryBuffer.append(" and c.target_account_flag='Y'");
						}
				    }

				}
				if(!criteria.getOtherHomeSegment().equals("") && !criteria.getOtherHomeSegment().equals("0")){

					//if(useAnd){
					//	queryBuffer.append(" AND");
					//}
					tableBuffer.append(", customer_segments b");
					queryBuffer.append(" AND c.vista_customer_number=b.vista_customer_number");
					queryBuffer.append(" and b.segment_id=" + criteria.getOtherHomeSegment());


				}

			}

			int recNum = recNum=Globals.a2int(criteria.getFirstRecNum());
			if(recNum==0) recNum++;
			//int lastRecNum=recNum+50;
			int lastRecNum=recNum+51;


			if(!criteria.getSort().equals("")){
				if(criteria.getSort().equals("curr_ytd_total") || criteria.getSort().equals("prev_ytd_total")){
					queryBuffer.append(" order by nvl(" + criteria.getSort() + ",0) " + criteria.getSortDir());
				}else if(criteria.getSort().equals("customer_name") || criteria.getSort().equals("sp_geog")) {
				    queryBuffer.append(" order by c." + criteria.getSort() + " " + criteria.getSortDir());
				}else if(criteria.getSort().equals("target_account_flag")) {
				    queryBuffer.append(" order by nvl(c." + criteria.getSort() + ",'N') " + criteria.getSortDir());
				}else if (criteria.getSort().equals("vista_customer_number")){
				    queryBuffer.append(" order by c." + criteria.getSort() + " " + criteria.getSortDir());
				}else{
					queryBuffer.append(" order by a." + criteria.getSort() + " " + criteria.getSortDir());
				}
			}else{
				queryBuffer.append(" order by c.customer_name");
			}



			StringBuffer finalQueryBuffer = new StringBuffer("select * from(\n");
			finalQueryBuffer.append("select rownum rnum,vista_customer_number,sales_engineer1_first_name,\n");
			finalQueryBuffer.append("sales_engineer1_last_name,sales_engineer1,customer_name,\n");
			finalQueryBuffer.append("target_account_flag,primary_seg_name,sec_seg_name,sp_geog,sp_stage,\n");
			finalQueryBuffer.append("curr_ytd_total,prev_ytd_total,sales_engineer2,sales_engineer3,sales_engineer4,customer_status \n");
			finalQueryBuffer.append(" from (\n");
			finalQueryBuffer.append("select rownum rnum,c.vista_customer_number,a.sales_engineer1_first_name,\n");
			finalQueryBuffer.append("a.sales_engineer1_last_name,c.sales_engineer1,c.sales_engineer2,c.sales_engineer3,c.sales_engineer4,c.customer_name,\n");
			finalQueryBuffer.append("c.target_account_flag,a.primary_seg_name,a.sec_seg_name,c.sp_geog,a.sp_stage,c.customer_status,\n");
// Braffet : Tap Dollars Change
//			finalQueryBuffer.append("a." + dollarType + "_" + salesOrders + "_YTD curr_ytd_total,a." + dollarType + "_" + salesOrders + "_PREV_YTD prev_ytd_total from customer_listing_mv a, customer c " + tableBuffer.toString());
			finalQueryBuffer.append("a.TAP_DOLLAR_" + salesOrders + "_YTD curr_ytd_total,a.TAP_DOLLAR_" + salesOrders + "_PREV_YTD prev_ytd_total " +
					"from customer_listing_mv a, customer c " + tableBuffer.toString());
			finalQueryBuffer.append(" where a.vista_customer_number(+)=c.vista_customer_number" + queryBuffer.toString());
			finalQueryBuffer.append("))\n");
			if(!criteria.isXcel()){
				finalQueryBuffer.append(" where rnum >= " + recNum + " and rnum<" + lastRecNum + "\n");

				//Updated Query to avoid Display of Marked for Delete status Customers SD0000002380193 Start here

				finalQueryBuffer.append(" AND upper(customer_status) not in ('"+customerStatusNotShown+"')\n" );

				//SD0000002380193 END
			}

			String query = finalQueryBuffer.toString();

			if (SMRCLogger.isDebuggerEnabled()) {

				SMRCLogger.debug("SQL - AccountDAO.getCustomerListing()\n" + query);

			}

			try {
				stmt = DBConn.createStatement();
				rs = stmt.executeQuery(query);;
				while (rs.next()) {
					Account account = new Account();

					account.setRownum(rs.getInt("rnum"));
					account.setVcn(rs.getString("vista_customer_number"));

					String firstName = StringManipulation.noNull(rs.getString("sales_engineer1_first_name"));
					String lastName = StringManipulation.noNull(rs.getString("sales_engineer1_last_name"));
					if(firstName.length()!=0 || lastName.length()!=0) {
					    account.setSalesEngineer1Name(firstName + " " + lastName + " - " + StringManipulation.noNull(rs.getString("sales_engineer1")));
					}else {
					    account.setSalesEngineer1Name(firstName + " " + lastName + " " + StringManipulation.noNull(rs.getString("sales_engineer1")));
					    //account.setSalesEngineer1(StringManipulation.noNull(rs.getString("sales_engineer1_first_name")) + " " + StringManipulation.noNull(rs.getString("sales_engineer1_last_name")) + " - " + StringManipulation.noNull(rs.getString("sales_engineer1")));
					}

					//account.setSalesEngineer1Name(StringManipulation.noNull(rs.getString("sales_engineer1_first_name")) + " " + StringManipulation.noNull(rs.getString("sales_engineer1_last_name")) + " - " + StringManipulation.noNull(rs.getString("sales_engineer1")));
					account.setSalesEngineer1(StringManipulation.noNull(rs.getString("sales_engineer1")));
					account.setSalesEngineer2(StringManipulation.noNull(rs.getString("sales_engineer2")));
					account.setSalesEngineer3(StringManipulation.noNull(rs.getString("sales_engineer3")));
					account.setSalesEngineer4(StringManipulation.noNull(rs.getString("sales_engineer4")));





					account.setCustomerName(StringManipulation.noNull(rs.getString("customer_name")));
					if(StringManipulation.noNull(rs.getString("target_account_flag")).equals("Y")){
						account.setTargetAccount(true);
					}

					account.setPrimarySegmentName(StringManipulation.noNull(rs.getString("primary_seg_name")));
					account.setSecondarySegmentName(StringManipulation.noNull(rs.getString("sec_seg_name")));

					/*
					 *  TODO i didnt like what vince did here and it caused problems
					 * on the customer listing page, so I removed it.  May address later
					 */
					//Vince: this is cheesy because they really aren't complete segments, but oh well.
					//Segment skeletalPrimarySegment = new Segment();
					//skeletalPrimarySegment.setName(rs.getString("primary_seg_name"));

					//Segment skeletalSecondarySegment = new Segment();
					//skeletalSecondarySegment.setName(rs.getString("sec_seg_name"));

					//ArrayList segments = new ArrayList();
					//segments.add(skeletalPrimarySegment);
					//segments.add(skeletalSecondarySegment);
					//account.setSegments(segments);

					/*
					 * TODO this is a HUGE bandaid.  I need to think about how I want to handle this.
					 * With the advent of "Segment" overrides for user, I need to make sure that the
					 * account segments are populated.  Since there could be n amount of 3rd level
					 * segments, this could prove tricky.
					 */
					account.setSegments(SegmentsDAO.getSegmentsForAccount(rs.getString("vista_customer_number"), DBConn));
					account.setDistrict(StringManipulation.noNull(rs.getString("sp_geog")));
					account.setStage(StringManipulation.noNull(rs.getString("sp_stage")));

					AccountNumbers numbers = new AccountNumbers();
					numbers.setCurrentYTD(rs.getDouble("curr_ytd_total"));
					numbers.setCurrentMinusOneYTD(rs.getDouble("prev_ytd_total"));
					account.setNumbers(numbers);

					accounts.add(account);
					//System.out.println(account);
				}

			}catch (Exception e)	{
				SMRCLogger.error("AccountDAO.getCustomerListing(): " , e);

				throw e;
			}
			finally {
				SMRCConnectionPoolUtils.close(rs);
				SMRCConnectionPoolUtils.close(stmt);
			}
		}
		return accounts;
	}

	/**
	 * Gets an SQL string to append to a query.  Determines what geographies a user can see
	 * 		Used by getCustomerListing()
	 * @param ugs  the users geography securities
	 * @param DBConn  the database connection
	 * @return  an SQl substring to append to a query
	 * @throws Exception
	 */
	private static String getGeogFilterString(UserGeogSecurity ugs){
		String tempString = "";
		if (ugs.isDistrict()){
			tempString = " (substr(a.sp_geog,1,5) like '" + ugs.getSPGeog().substring(0,5) + "%')";
		}else if (ugs.isGroup()){
			tempString = " (substr(a.sp_geog,1,2) like '" + ugs.getSPGeog().substring(0,2) + "%')";
		}else if (ugs.isNational()){
			tempString = " (substr(a.sp_geog,1,1) like '" + ugs.getSPGeog().substring(0,1) + "%')";
		}else if (ugs.isTeam()){
			tempString = " (substr(a.sp_geog,1,6) = '" + ugs.getSPGeog().substring(0,6) + "')";
		}else if (ugs.isZone()){
			tempString = " (substr(a.sp_geog,1,4) like '" + ugs.getSPGeog().substring(0,4) + "%')";
		}

		return tempString;
	}


	/**
	 * Gets a Customer
	 * @param vcn  the VCN or prospect number of the customer
	 * @param DBConn  the database connection
	 * @return  a Customer
	 * @throws Exception
	 */
	//Vince: TODO should use getOneCustomerQuery in the future
	public static Customer getOneCustomer(String vcn, Connection DBConn) throws Exception {

		Customer customer = new Customer();
		String sel = "SELECT * from customer where vista_customer_number = '" + vcn + "'";
		Statement stmt = null;
		ResultSet res = null;
		try {
			stmt = DBConn.createStatement();
			res = stmt.executeQuery(sel);

			while (res.next()) {
				customer = new Customer(res.getString("vista_customer_number"));

				if (res.getString("phone_number") != null) {
					customer.setPhoneNumber(res.getString("phone_number"));
				}

				if (res.getString("sales_engineer1") != null) {
					customer.setSalesId(res.getString("sales_engineer1"));

					//added by jpv
					customer.setSalesmanName(SalesDAO.getSalesmanName(res.getString("sales_engineer1"), DBConn));
				}

				if (res.getString("duns_number") != null) {
					customer.setDunsNum(res.getString("duns_number"));
				}

				if (res.getString("sic_code") != null) {
					customer.setSICCode(res.getString("sic_code"));

					//added by jpv
					customer.setSICDescription(MiscDAO.getSICDescription(res.getString("sic_code"), DBConn));
				}

				if (res.getString("web_site") != null) {
					customer.setWebSite(res.getString("web_site"));
				}

				if (res.getString("description") != null) {
					customer.setDescription(res.getString("description"));
				}

				if (res.getString("customer_name") != null) {
					customer.setName(res.getString("customer_name"));
				}

				if (res.getString("industry_other") != null) {
					customer.setIndustryOther(res.getString("industry_other"));
				}

				if (res.getString("sp_geog") != null) {
					customer.setSPGeog(res.getString("sp_geog"));

					//added by jpv
					customer.setZoneGeogName(MiscDAO.getGeography((res.getString("sp_geog").substring(0,4) + "0"),DBConn).getDescription());
					customer.setDistrictGeogName(MiscDAO.getGeography(res.getString("sp_geog").substring(0,5),DBConn).getDescription());
				}

				if (res.getString("pct_time_with_cust") != null) {
					customer.setPcntTimeWithCust(res.getInt("pct_time_with_cust"));
				}

				if (res.getString("global_acct_mgr") != null) {
					customer.setGlobalAcctMgr(res.getString("global_acct_mgr"));
				}

				customer.setStage(res.getInt("sp_stage_id"));
				customer.setNumStores(res.getInt("num_stores"));

				if (res.getString("parent_num") != null) {
					customer.setParent(res.getString("parent_num"));

					// added by jpv
					customer.setCustomerExists(AccountDAO.customerExists(res.getString("parent_num"), DBConn));

				}
			}

		}catch (Exception e){
			SMRCLogger.error("AccountDAO.getOneCustomer(): " , e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(res);
			SMRCConnectionPoolUtils.close(stmt);
		}
		return customer;

	} //method

	/**
	 * Checks to see if a customer exists
	 * @param vcn  the VCN or prospect number of the customer
	 * @param DBConn  the database connection
	 * @return  true if exists, false if doesnt exist
	 * @throws Exception
	 */
	//	TODO Vince: should use customerExistsQuery in the future
	public static boolean customerExists(String vcn, Connection DBConn)throws Exception {

		int cnt = 0;
		String sel = "SELECT count(*) from customer where vista_customer_number = '" + vcn + "'";

		Statement stmt = null;
		ResultSet res = null;
		try {
			stmt = DBConn.createStatement();
			res = stmt.executeQuery(sel);

			while (res.next()) {
				cnt = res.getInt(1);
			}

		}catch (Exception e){
			SMRCLogger.error("AccountDAO.customerExists(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(res);
			SMRCConnectionPoolUtils.close(stmt);
		}

		if (cnt == 0) {
			return false;
		}

		return true;

	} //method

	/**
	 * This method gets the children of the specified parent
	 * @param vcn  the VCN or prospect number of the customer
	 * @param DBConn  the database connection
	 * @return  account ids in String objects
	 * @throws Exception
	 */
	public static ArrayList getAllChildrenForVCN(String vcn, Connection DBConn)throws Exception {
		ArrayList results = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DBConn.prepareStatement(getChildrenVCNQuery);
			pstmt.setString(1, vcn);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String childVcn = rs.getString("vista_customer_number");
				results.add(childVcn);
			}

		}catch (Exception e){
			SMRCLogger.error("AccountDAO.getAllChildrenForVCN(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}


		return results;

	} //method

	/**
	 * Flags one contact as the user to recieve a distributor statement (Distributors only)
	 * 		The contact is selected from an account contacts on the Account Profile page
	 * @param acctId  the VCN or prospect number of the customer
	 * @param contactId  id of the user to set as the Distributor Statement Contact
	 * @param DBConn  the database connection
	 * @throws Exception
	 */
	private static void setDistributorStatementContact(String acctId,int contactId, Connection DBConn) throws Exception{

		PreparedStatement pstmt = null;

		try {
			pstmt = DBConn.prepareStatement(setDistributorStatementContactUpdate1);
			pstmt.setString(1,acctId);
			pstmt.setInt(2,contactId);
			pstmt.executeUpdate();

			pstmt = DBConn.prepareStatement(setDistributorStatementContactUpdate2);
			pstmt.setInt(1,contactId);
			pstmt.executeUpdate();

		}
		catch (Exception e)	{
			SMRCLogger.error("AccountDAO.setDistributorStatementContact(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);

		}

	}

	/**
	 * Sets the customer category, if overridden.  Only updated by credit managers
	 * 			on the "Approval Summary" page by the
	 * @param acctId  the VCN or prospect number of the customer
	 * @param custCat  the Category to set
	 * @param DBConn  the database connection
	 * @throws Exception
	 */
	public static void setCustomerCategory(String acctId, String custCat, Connection DBConn) throws Exception{

		PreparedStatement pstmt = null;

		try {
			pstmt = DBConn.prepareStatement(setCustomerCategoryUpdate);
			pstmt.setString(1,custCat);
			pstmt.setString(2,acctId);
			pstmt.executeUpdate();

		}
		catch (Exception e)	{
			SMRCLogger.error("AccountDAO.setCustomerCategory(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);

		}

	}

	/**
	 * Gets the customer category.  Returns an empty string if customer category was never overridden
	 * @param acctId  the VCN or prospect number of the customer
	 * @param DBConn  the database connection
	 * @return  the customer category
	 * @throws Exception
	 */
	public static String getCustomerCategory(String acctId, Connection DBConn) throws Exception{

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DBConn.prepareStatement(getCustomerCategoryUpdate);
			pstmt.setString(1,acctId);
			rs=pstmt.executeQuery();
			if(rs.next()){
				return StringManipulation.noNull(rs.getString(1));
			}
			return "";

		}
		catch (Exception e)	{
			SMRCLogger.error("AccountDAO.getCustomerCategory(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);

		}

	}

	/**
	 * Gets tasks for a customer.  If visit ID is specified then only gets tasks for the visit
	 * @param DBConn  the database connection
	 * @param vcn  the VCN or prospect number of the customer
	 * @param custVisitId  the id of the visit
	 * @return  a list of PurchaseActionProgram (tasks)
	 * @throws Exception
	 */
	public static ArrayList getCustTasks(Connection DBConn, String vcn, int custVisitId) throws Exception {

		ArrayList tasks = new ArrayList();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (custVisitId > 0){
				pstmt = DBConn.prepareStatement(getTasksForVisitsQuery);
				pstmt.setString(1, vcn);
				pstmt.setInt(2, custVisitId);
			} else {
				pstmt = DBConn.prepareStatement(getTasksQuery);
				pstmt.setString(1, vcn);
			}

			rs = pstmt.executeQuery();

			while (rs.next()) {

				PurchaseActionProgram task = new PurchaseActionProgram();

				task.setCustomer(rs.getString("vista_customer_number"));

				if (rs.getString("product_id") != null) {
					task.setProduct(rs.getString("product_id"));
					task.setProductDescription(TAPcommon.getProductDescription(
							rs.getString("product_id"), DBConn));
				}

				if (rs.getString("action") != null) {
					task.setAction(rs.getString("action"));
				}

				if (rs.getString("objective") != null) {
					task.setObjective(rs.getString("objective"));
				}

				if (rs.getString("assigned_to") != null) {
					task.setAssignedTo(rs.getString("assigned_to"));
				}

				if (rs.getString("complete") != null) {
					task
					.setComplete(rs.getString("complete").equals(
					"Y"));
				}

				if (rs.getDate("schedule") != null) {
					task.setSchedule(rs.getDate("schedule"));
				}

				if (rs.getString("results") != null) {
					task.setResults(rs.getString("results"));
				}

				if (rs.getString("ebe_id") != null) {
					task.setEBECategory(rs.getInt("ebe_id"));
					task.setEBEDescription(
							TAPcommon.getEBEDescription(rs.getInt("ebe_id")
									+ "", DBConn));
				}

				task.setTaskId(rs.getInt("task_id"));
				task.setUserAdded(rs.getString("user_added"));
				task.setUserChanged(rs.getString("user_changed"));
				if (rs.getString("customer_visit_id") != null){
					task.setCustomerVisitId(rs.getInt("customer_visit_id"));
				}

				task.setCCEmail(AccountDAO.getSalesPlanCCUsers(rs.getInt("task_id"), DBConn));
				task.setAttachments(AccountDAO.getTaskAttachments(rs.getInt("task_id"),DBConn));

				tasks.add(task);
			}
		} catch (Exception e) {
			SMRCLogger.error("AccountDAO.getCustTasks() ", e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return tasks;

	} //method

	/**
	 * Gets one tasks for a customer.
	 * @param DBConn  the database connection
	 * @param taskId  the ID of the task
	 * @return  one PurchaseActionProgram (tasks)
	 * @throws Exception
	 */
	public static PurchaseActionProgram getOneTask(Connection DBConn, int taskId) throws Exception {

		PurchaseActionProgram task = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DBConn.prepareStatement(getOneTask);
			pstmt.setInt(1, taskId);
			rs = pstmt.executeQuery();

			if (rs.next()) {

				task = new PurchaseActionProgram();

				task.setCustomer(rs.getString("vista_customer_number"));

				if (rs.getString("product_id") != null) {
					task.setProduct(rs.getString("product_id"));
					task.setProductDescription(TAPcommon.getProductDescription(
							rs.getString("product_id"), DBConn));
				}

				if (rs.getString("action") != null) {
					task.setAction(rs.getString("action"));
				}

				if (rs.getString("objective") != null) {
					task.setObjective(rs.getString("objective"));
				}

				if (rs.getString("assigned_to") != null) {
					task.setAssignedTo(rs.getString("assigned_to"));
				}

				if (rs.getString("complete") != null) {
					task
					.setComplete(rs.getString("complete").equals(
					"Y"));
				}

				if (rs.getDate("schedule") != null) {
					task.setSchedule(rs.getDate("schedule"));
				}

				if (rs.getString("results") != null) {
					task.setResults(rs.getString("results"));
				}

				if (rs.getString("ebe_id") != null) {
					task.setEBECategory(rs.getInt("ebe_id"));
					task.setEBEDescription(
							TAPcommon.getEBEDescription(rs.getInt("ebe_id")
									+ "", DBConn));
				}

				task.setTaskId(rs.getInt("task_id"));
				task.setUserAdded(rs.getString("user_added"));
				task.setUserChanged(rs.getString("user_changed"));
				if (rs.getString("customer_visit_id") != null){
					task.setCustomerVisitId(rs.getInt("customer_visit_id"));
				}

				task.setCCEmail(AccountDAO.getSalesPlanCCUsers(rs.getInt("task_id"), DBConn));


			}
		} catch (Exception e) {
			SMRCLogger.error("AccountDAO.getOneTask() ", e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return task;

	} //method

	/**
	 * Deletes one tasks for a customer.
	 * @param taskId  the ID of the task
	 * @param DBConn  the database connection
	 * @throws Exception
	 */
	public static void deleteTask(int taskId, Connection DBConn) throws Exception {

		PreparedStatement pstmt=null;

		try {
			deleteSalesPlanCCUsers(taskId, DBConn);
			pstmt = DBConn.prepareStatement(deleteTask);
			pstmt.setInt(1,taskId);
			pstmt.executeUpdate();
		}
		catch (Exception e) {
			SMRCLogger.error("AccountDAO.deleteTask(): " , e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);

		}

	}

	/**
	 * Updates or inserts tasks records
	 * @param request  the HttpServletRequest object
	 * @param aConnection  the database connection
	 * @param userid  the ID of the user updating
	 * @return  nextTaskId if inserting a new record, otherwise returns 0
	 * @throws Exception
	 */
	public static long updatePAP(HttpServletRequest request, Connection aConnection, String userid)
	throws Exception {

		long nextTaskId = 0;
		if (request.getParameter("totTask") == null) {
			return nextTaskId;
		}

		String cust = request.getParameter("cust");
		int totTask = Globals.a2int(request.getParameter("totTask"));

		for (int i = 0; i < totTask; i++) {
			String taskId = "";
			String action = "";
			String objective = "";
			String schedule = "";
			String results = "";
			String assignedTo = "";
			String complete = "N";
			String actionOld = "";
			String objectiveOld = "";
			String scheduleOld = "";
			String resultsOld = "";
			String assignedToOld = "";
			String completeOld = "N";
			String[] ccEmail = null;
			String[] ccEmailOld = null;
			String product = "";
			String prodOld = "";
			String ebe = "0";
			String ebeOld = "0";
			String visitId = null;

			if (request.getParameter("taskId" + i) != null
					&& request.getParameter("taskId" + i).length() > 0) {
				taskId = request.getParameter("taskId" + i);
			}

			if (request.getParameter("product" + i) != null
					&& request.getParameter("product" + i).length() > 0) {
				product = request.getParameter("product" + i);
			}

			if (request.getParameter("prodOld" + i) != null
					&& request.getParameter("prodOld" + i).length() > 0) {
				prodOld = request.getParameter("prodOld" + i);
			}

			if (request.getParameter("ebeCat" + i) != null
					&& request.getParameter("ebeCat" + i).length() > 0) {
				ebe = request.getParameter("ebeCat" + i);
			}

			if (request.getParameter("ebeCatOld" + i) != null
					&& request.getParameter("ebeCatOld" + i).length() > 0) {
				ebeOld = request.getParameter("ebeCatOld" + i);
			}

			if (request.getParameter("action" + i) != null
					&& request.getParameter("action" + i).length() > 0) {
				action = request.getParameter("action" + i);
			}

			if (request.getParameter("prevAct" + i) != null
					&& request.getParameter("prevAct" + i).length() > 0) {
				actionOld = request.getParameter("prevAct" + i);
			}

			if (request.getParameter("obj" + i) != null
					&& request.getParameter("obj" + i).length() > 0) {
				objective = request.getParameter("obj" + i);
			}

			if (request.getParameter("prevObj" + i) != null
					&& request.getParameter("prevObj" + i).length() > 0) {
				objectiveOld = request.getParameter("prevObj" + i);
			}

			if (request.getParameter("assignedto" + i) != null
					&& request.getParameter("assignedto" + i).length() > 0) {
				assignedTo = request.getParameter("assignedto" + i);
			}

			if (request.getParameter("prevAssign" + i) != null
					&& request.getParameter("prevAssign" + i).length() > 0) {
				assignedToOld = request.getParameter("prevAssign" + i);
			}

			if (request.getParameterValues("ccemail" + i) != null) {
				ccEmail = request.getParameterValues("ccemail" + i);
			}

			if (request.getParameterValues("prevCCEmail" + i) != null) {
				ccEmailOld = request.getParameterValues("prevCCEmail" + i);
			}

			if (request.getParameter("complete" + i) != null) {
				complete = "Y";
			}

			if (request.getParameter("prevComp" + i) != null
					&& request.getParameter("prevComp" + i).equals("Y")) {
				completeOld = "Y";
			}

			if (request.getParameter("sched" + i) != null
					&& request.getParameter("sched" + i).toString().length() > 0) {

				String sched = request.getParameter("sched" + i);

				int mo = Globals.a2int(sched.substring(0, 2));
				int day = Globals.a2int(sched.substring(3, 5));
				int yr = Globals.a2int(sched.substring(6, 10));

				Calendar cal = Calendar.getInstance();
				cal.clear();
				cal.set(yr, mo - 1, day);

				java.sql.Date sqlDate = new java.sql.Date(cal.getTime()
						.getTime());
				schedule = "to_date('" + sqlDate + "', 'YYYY-MM-DD')";
			} else {
				schedule = "null";
			}

			if (request.getParameter("prevSched" + i) != null
					&& request.getParameter("prevSched" + i).length() > 0) {

				String sched = request.getParameter("prevSched" + i);

				int mo = Globals.a2int(sched.substring(0, 2));
				int day = Globals.a2int(sched.substring(3, 5));
				int yr = Globals.a2int(sched.substring(6, 10));

				Calendar cal = Calendar.getInstance();
				cal.clear();
				cal.set(yr, mo - 1, day);

				java.sql.Date sqlDate = new java.sql.Date(cal.getTime()
						.getTime());
				scheduleOld = "to_date('" + sqlDate + "', 'YYYY-MM-DD')";
			} else {
				scheduleOld = "null";
			}

			if (request.getParameter("results" + i) != null
					&& request.getParameter("results" + i).length() > 0) {
				results = request.getParameter("results" + i);
			}

			if (request.getParameter("prevRes" + i) != null
					&& request.getParameter("prevRes" + i).length() > 0) {
				resultsOld = request.getParameter("prevRes" + i);
			}

			if (request.getParameter("visitId") != null){
				visitId = request.getParameter("visitId");
			}

			if (!taskId.equals("new")) {
				if (!action.trim().equals(actionOld.trim())
						|| !objective.trim().equals(objectiveOld.trim())
						|| !schedule.trim().equals(scheduleOld.trim())
						|| !results.trim().equals(resultsOld.trim())
						|| !assignedTo.trim().equals(assignedToOld.trim())
						|| !complete.trim().equals(completeOld.trim())
						|| !isCCSame(ccEmail, ccEmailOld)
						|| !ebe.trim().equals(ebeOld.trim())
						|| !product.trim().equals(prodOld.trim())) {
					String upd = "Update sales_plan_pap " + "SET action = '"
					+ StringManipulation.fixQuotes(action) + "', " + "objective = '"
					+ StringManipulation.fixQuotes(objective) + "', " + "schedule = "
					+ schedule + ", " + "results = '"
					+ StringManipulation.fixQuotes(results) + "', " + "assigned_to = '"
					+ assignedTo + "', " +
					//"cc_email = '" + ccEmail + "', " +
					"complete = '" + complete + "', "
					+ "user_changed = '" + userid + "', "
					+ "product_id = '" + product + "', " + "ebe_id = "
					+ ebe + ", " + "date_changed = sysdate "
					+ "WHERE task_id = " + taskId;

					Statement stmt = null;
					try {
						stmt = aConnection.createStatement();
						stmt.executeUpdate(upd);

					} catch (Exception e) {
						SMRCLogger.error("AccountDAO.updatePAP() ", e);
						throw e;
					} finally {
						SMRCConnectionPoolUtils.close(stmt);
					}

					AccountDAO.deleteSalesPlanCCUsers(Globals.a2int(taskId), aConnection);

					if (ccEmail != null) {

						for (int m = 0; m < ccEmail.length; m++) {
							String ccUserId = ccEmail[m];
							if (!ccUserId.equals("0")) {
								AccountDAO.insertSalesPlanCCUsers(Globals.a2int(taskId), ccUserId, aConnection);
							}
						}

					}
					// We have to commit here, otherwise the email will contain old information
					SMRCConnectionPoolUtils.commitTransaction(aConnection);
					TaskUpdate tu = new TaskUpdate(cust, taskId,assignedToOld, ccEmailOld);
					tu.run();
				}
			} else if (action != null && action.length() > 0) {

				String getNextTaskIdQry = "SELECT task_id_seq.nextval FROM dual ";
				Statement stmt1 = null;
				ResultSet rs = null;

				try {
					stmt1 = aConnection.createStatement();
					rs = stmt1.executeQuery(getNextTaskIdQry);
					while (rs.next()) {
						nextTaskId = rs.getLong(1);
					}
				} catch (Exception e) {
					SMRCLogger.error("AccountDAO.updatePAP() ", e);
					throw e;
				} finally {
					SMRCConnectionPoolUtils.close(rs);
					SMRCConnectionPoolUtils.close(stmt1);
				}

				String ins = "insert into sales_plan_pap "
					+ "(action,objective,schedule,results,assigned_to,complete,user_changed,"
					+ "user_added,date_added,date_changed,vista_customer_number,product_id,"
					+ "task_id,ebe_id, customer_visit_id) " + "values ('" + StringManipulation.fixQuotes(action)
					+ "','" + StringManipulation.fixQuotes(objective) + "'," + schedule + ",'"
					+ StringManipulation.fixQuotes(results) + "','" + assignedTo + "','"
					+ complete + "','" + userid + "','" + userid
					+ "',sysdate,sysdate,'" + cust + "','" + product + "',"
					+ nextTaskId + "," + ebe + "," + visitId + ")";

				Statement stmt = null;
				try {

					stmt = aConnection.createStatement();
					stmt.executeUpdate(ins);

				} catch (Exception e) {
					SMRCLogger.error("AccountDAO.updatePAP() ", e);
					throw e;
				} finally {
					SMRCConnectionPoolUtils.close(stmt);
				}
				if (ccEmail != null) {
					for (int m = 0; m < ccEmail.length; m++) {
						String ccUserId = ccEmail[m];
						if (!ccUserId.equals("0")) {
							AccountDAO.insertSalesPlanCCUsers((int) nextTaskId,
									ccUserId, aConnection);
						}
					}
				}

				if (assignedTo != null && assignedTo.length() > 1) {
					int newTask = 0;

					String sel = "select task_id_seq.currVal from dual";
					try {
						stmt = aConnection.createStatement();
						rs = stmt.executeQuery(sel);

						while (rs.next()) {
							newTask = rs.getInt(1);
						}
					} catch (Exception e) {
						SMRCLogger.error("AccountDAO.updatePAP() ", e);
						throw e;
					} finally {
						SMRCConnectionPoolUtils.close(rs);
						SMRCConnectionPoolUtils.close(stmt);
					}

					//	            notifyUserOfAssignment(assignedTo, cust, product, newTask,
					//	                    userid);
//					 We have to commit here, otherwise the email will contain old information
					SMRCConnectionPoolUtils.commitTransaction(aConnection);
					TaskEmail email = new TaskEmail(assignedTo, userid, product, cust, newTask);
					email.run();
				}

			} //if/else
			long attachTaskId = 0;
			if (Globals.a2long(taskId) != 0){
				attachTaskId = Globals.a2long(taskId);
			} else if (nextTaskId != 0){
				attachTaskId = nextTaskId;
			}
			// Don't save files unless there is a task id
			if (attachTaskId > 0){
				if (request.getParameter("attachDoc" + i) != null){
					String [] values = request.getParameterValues("attachDoc" + i);
					String [] nameValues = request.getParameterValues("attachDoc" + i + "_Name");
					for (int attValues = 0; attValues < values.length; attValues++){
						String attValue = values[attValues];
						String attName = nameValues[attValues];
						AccountDAO.insertTaskAttachments(attachTaskId,attValue,attName,userid,aConnection);
					}
				}
			}

		}

		if (request.getParameter("deletedDocs") != null){
			String [] values = request.getParameterValues("deletedDocs");
			for (int attValues = 0; attValues < values.length; attValues++){
				long attachmentId = Globals.a2long(values[attValues]);
				AccountDAO.deleteAttachments(attachmentId,aConnection);
			}
		}

		return nextTaskId;

	} //method

	/**
	 * Compares existing CC users to new/updated cc users when updating a sales plan
	 * 		The salesplan uses this to determine if it should send an email to users notifying
	 * 		them of updates
	 * @param ccEmail  list of new or currently select user IDs
	 * @param ccEmailOld  list of user IDs that currently exist in the database
	 * @return  true if they are the same, false if different
	 * @throws Exception
	 */
	private static boolean isCCSame(String[] ccEmail, String[] ccEmailOld) {

		if (ccEmail == null && ccEmailOld == null) {
			return true;

		} else if (ccEmail == null || ccEmailOld == null) {
			return false;
		}

		if (ccEmail.length != ccEmailOld.length) {
			return false;
		}

		for (int i = 0; i < ccEmail.length; i++) {
			boolean found = false;
			for (int j = 0; j < ccEmailOld.length; j++) {
				if (ccEmail[i].equals(ccEmailOld[j])) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;

	} //method


	/**
	 * Changes an account status
	 * @param acctId  the VCN or prospect number of the account
	 * @param status  new status
	 * @param DBConn  the database connection
	 * @throws Exception
	 */
	public static void changeAccountStatus(String acctId, String status, Connection DBConn) throws Exception{

		PreparedStatement pstmt = null;

		try {
			pstmt = DBConn.prepareStatement(changeAccountStatusUpdate);
			pstmt.setString(1, status);
			pstmt.setString(2, acctId);
			pstmt.executeUpdate();
		}catch (Exception e)	{
			SMRCLogger.error("AccountDAO.changeAccountStatus(): ", e);
			throw e;
		}
		finally {

			SMRCConnectionPoolUtils.close(pstmt);
		}

	}

	/**
	 * Gets special programs for an account
	 * @param acctId  the VCN or prospect number of the account
	 * @param DBConn  the database connection
	 * @return the special program ids that meet the criteria
	 * @throws Exception
	 */
	public static ArrayList getSpecialProgramsForAccount(String acctId, Connection DBConn) throws Exception{

		ArrayList specialProgramIds = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DBConn.prepareStatement(getSpecialProgramsByVCNQuery);
			pstmt.setString(1, acctId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				specialProgramIds.add(rs.getString("special_program_id"));
			}
		}
		catch (Exception e)	{
			SMRCLogger.error("AccountDAO.getSpecialProgramsForAccount(): " , e);

			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);

		}

		return specialProgramIds;
	}

	/**
	 * Updates special programs for an account by deleting existing records and inserting new ones
	 * @param acct  the Account object being updated
	 * @param DBConn  the database connection
	  * @throws Exception
	 */
	public static void updateSpecialProgramsForAccount(Account acct, Connection DBConn) throws Exception{

		PreparedStatement pstmt = null;
		try {
			pstmt = DBConn.prepareStatement(deleteSpecialProgramsByVCN);
			pstmt.setString(1, acct.getVcn());
			int deleteResult = pstmt.executeUpdate();
			// Commit here so there is no conflict with the insert below
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			ArrayList spList = acct.getSpecialProgramIds();
			for (int i=0; i< spList.size(); i++){
			    Integer specialProgramId = new Integer((String) spList.get(i));
			    pstmt = DBConn.prepareStatement(insertSpecialProgramsForVCN);
			    pstmt.setString(1,acct.getVcn());
			    pstmt.setInt(2,specialProgramId.intValue());
			    int insertResult = pstmt.executeUpdate();
			}
		}
		catch (Exception e)	{
			SMRCLogger.error("AccountDAO.updateSpecialProgramsForAccount(): " , e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}


	}

	public static String getAccountGeog (String acctId, Connection DBConn) throws Exception {
	    String geog = "";
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    try {
	        pstmt = DBConn.prepareStatement(getAccountGeogQuery);
	        pstmt.setString(1,acctId);
	        rs = pstmt.executeQuery();
	        if (rs.next()){
	            geog = rs.getString("sp_geog");
	        }
	    } catch (Exception e) {
	        SMRCLogger.error("AccountDAO.getAccountGeog(): ", e);
	        throw e;
	    } finally {
	        SMRCConnectionPoolUtils.close(rs);
	        SMRCConnectionPoolUtils.close(pstmt);
	    }
	    return geog;
	}

	public static String getAccountUserAdded (String acctId, Connection DBConn) throws Exception {
	    String userAdded = "";
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    try {
	        pstmt = DBConn.prepareStatement(getAccountUserAddedQuery);
	        pstmt.setString(1,acctId);
	        rs = pstmt.executeQuery();
	        if (rs.next()){
	            userAdded = rs.getString("user_added");
	        }
	    } catch (Exception e) {
	        SMRCLogger.error("AccountDAO.getAccountUserAdded(): ", e);
	        throw e;
	    } finally {
	        SMRCConnectionPoolUtils.close(rs);
	        SMRCConnectionPoolUtils.close(pstmt);
	    }
	    return userAdded;
	}

	// Returns an ArrayList of all attachments for a specified task, in TaskAttachment objects
	public static ArrayList getTaskAttachments (int taskId, Connection DBConn) throws Exception {
	    ArrayList attachments = new ArrayList();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    try {
	        pstmt = DBConn.prepareStatement(getTaskAttachmentsQuery);
	        pstmt.setInt(1,taskId);
	        rs = pstmt.executeQuery();
	        while (rs.next()){
	            TaskAttachment attachment = new TaskAttachment();
	            attachment.setFileName(rs.getString("attached_file_name"));
	            attachment.setId(rs.getInt("sales_plan_pap_files_id"));
	            attachment.setContentType(rs.getString("content_type"));
	            attachments.add(attachment);
	        }
	    } catch (Exception e) {
	        SMRCLogger.error("AccountDAO.getTaskAttachments(): ", e);
	        throw e;
	    } finally {
	        SMRCConnectionPoolUtils.close(rs);
	        SMRCConnectionPoolUtils.close(pstmt);
	    }
	    return attachments;
	}

	public static void insertTaskAttachments (long taskId, String fileLocation, String fileName, String userid, Connection DBConn) throws Exception {

		File theFile = new File(fileName);
		FileDataSource dataSource = new FileDataSource(theFile);


	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	    	// Get nextVal for sequence
	    	pstmt = DBConn.prepareStatement(getTaskAttachmentNextValQuery);
	    	rs = pstmt.executeQuery();
	    	if (rs.next()){
	    		int attachmentId = rs.getInt("nextval");
	    		pstmt = DBConn.prepareStatement(insertTaskAttachmentQuery);
	    		pstmt.setInt(1,attachmentId);
		        pstmt.setLong(2,taskId);
		        pstmt.setString(3,fileName);
		        pstmt.setString(4,userid);
		        pstmt.setString(5,userid);
		        pstmt.setString(6,dataSource.getContentType());
		        int returncode = pstmt.executeUpdate();
		        // Commit here so BlobAttachmentPerform.saveNewAttachment() can update record
		        SMRCConnectionPoolUtils.commitTransaction(DBConn);
		        Connection tempConn = DBConn.getMetaData().getConnection();
		        try {
		        	BlobAttachmentPerform.saveNewAttachment(tempConn,fileLocation,"sales_plan_pap_files",attachmentId,"sales_plan_pap_files_id","attached_file",Globals.getTaskAttachmentFileSizeMax());
		        } catch (Exception e) {
		        	// If exception is thrown saving BLOB, delete committed record
		        	AccountDAO.deleteAttachments(attachmentId,DBConn);
		        	throw e;
		        }

		    } else {
	    		throw new Exception("AccountDAO:: nextVal not found for sales_plan_pap_files_seq");
	    	}

	    } catch (Exception e) {
	        SMRCLogger.error("AccountDAO.insertTaskAttachments(): ", e);
	        throw e;
	    } finally {
	        SMRCConnectionPoolUtils.close(pstmt);
	        SMRCConnectionPoolUtils.close(rs);
	    }

	}

	public static void deleteAttachments (long attachmentId, Connection DBConn) throws Exception {
	    PreparedStatement pstmt = null;
	    try {
	        pstmt = DBConn.prepareStatement(deleteTaskAttachmentRecord);
	        pstmt.setLong(1,attachmentId);
	        int returnCode = pstmt.executeUpdate();
	    } catch (Exception e) {
	        SMRCLogger.error("AccountDAO.deleteAttachments(): ", e);
	        throw e;
	    } finally {
	        SMRCConnectionPoolUtils.close(pstmt);
	    }
	}

	public static void deleteProspect(String vcn, Connection DBConn) throws Exception {

		PreparedStatement ps = null;
		try {
			ps = DBConn.prepareStatement(deleteProspect);

			ps.setString(1, "Marked for Delete");
			ps.setString(2, vcn);

			ps.executeUpdate();
		} catch(Exception e) {
	        SMRCLogger.error("AccountDAO.deleteProspect(): ", e);
	        throw e;
	    } finally {
	        SMRCConnectionPoolUtils.close(ps);
	    }
	}

	public static TaskAttachment getTaskAttachment (long attachmentId, Connection DBConn) throws Exception {
		TaskAttachment attachment = new TaskAttachment();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    try {
	        pstmt = DBConn.prepareStatement(getTaskAttachmentInfo);
	        pstmt.setLong(1,attachmentId);
	        rs = pstmt.executeQuery();
	        if (rs.next()){
	        	attachment.setContentType(rs.getString("content_type"));
	        	attachment.setFileName(rs.getString("attached_file_name"));
	        	attachment.setId(rs.getLong("sales_plan_pap_files_id"));
	        	attachment.setTaskId(rs.getLong("task_id"));
	        }
	    } catch (Exception e) {
	        SMRCLogger.error("AccountDAO.deleteAttachments(): ", e);
	        throw e;
	    } finally {
	    	SMRCConnectionPoolUtils.close(rs);
	        SMRCConnectionPoolUtils.close(pstmt);
	    }

	    return attachment;
	}

} //class
