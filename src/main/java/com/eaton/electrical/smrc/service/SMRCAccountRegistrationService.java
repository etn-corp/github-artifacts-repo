package com.eaton.electrical.smrc.service;

import java.util.*;
import java.sql.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.exception.*;

import com.etn.cutlerhammer.VistaMessageService.Data.*;
import com.etn.cutlerhammer.VistaMessageService.Data.Record;
import com.etn.cutlerhammer.VistaMessageService.Transaction.*;
import com.etn.cutlerhammer.VistaMessageService.Util.*;

/**
 * @author Vince Manuppelli
 *
 * This class provides the services necessary to send and receive data to and from VISTA for account registration.
 *
 * This class uses the VistaMessageService (VMS) for VISTA communication.
 * Note that the VistaMessageService does NOT use Log4J internally so internal error messages will go to standard out.
 * The VistaMessageService logs its error messages to the console.
 *
 * @see VistaMessageSerivce at http://java.ch.etn.com/webservices/java/vista_message.html
 *
 */
public class SMRCAccountRegistrationService {

    private static ResourceBundle config = ResourceBundle.getBundle ("com.eaton.electrical.smrc.SMRC") ;

    /**
     * This does not work fully yet but should build cleanly and should do some logging if called.
     * There is still some data mapping to finish.
     *
     * @param aProspectNumber - The prospect number to register.
     * @param theLoggedInUser - The logged-in user who is trying to register the account.
     * @return - The results of the registration process.
     * @throws Exception - If the registration process fails.
     */
    public static AccountRegistrationResults registerAccount(Account account, User theLoggedInUser, Connection DBConn) throws Exception {

        String vistaModuleName = null;
        String vistaModuleEnvironment = null;

        /*
         * These are returned from Vista, returned to the caller AND updated to our local datastore.
         */
        String vistaCustomerNumber = null;
        String vistaReferenceNumber = null;
        /*
         * This is returned from Vista and only returned to the caller.  No update to local datastore (yet).
         */
        String districtRegisteredTo = null;





        StringBuffer messageSB = new StringBuffer(1000);

        try {
       	
            //DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();

            /* At this time the account should be a prospect account.  Create it and populate its attributes from DAOs.
	         * If all goes well after the mainframe call the account will become a registered (non-prospect) account.
	         * We will update a few values in the SMRC database after the call.
	         * Those values are:
	         *
	         * 1. Reference Number - this will be returned if the registration is immediate or pending.
	         * 2. Vista Customer Number - this will be returned if the registration is immediate.
	         * 3. Salesman ID #1 - sometimes this value is changed by the mainframe routine.
	         */
	        //Account account = AccountDAO.getAccount(aProspectNumber, DBConn);

	        /* remove these lines ... temp for testing
	        SMRCLogger.debug("---------------------------------");
	        SMRCLogger.debug(account.toString());
	        SMRCLogger.debug("---------------------------------");
	        */

	        /*
	         * Pre-qualify for registration.
	         * This will throw an exception if the Prospect does not qualify.
	         */
        	SMRCLogger.debug("SMRCAccountRegistrationService.registerAccount() before checking isQualified");
	        isQualified(account,theLoggedInUser,DBConn,true);
	        SMRCLogger.debug("SMRCAccountRegistrationService.registerAccount() after checking isQualified");
	        
	        /*
             * Get the Vista environment from our properties file.
             * This must change from DEV, QA, TRNG, PROD as needed.
             */
	        vistaModuleEnvironment = config.getString("VISTA_ENVIRONMENT");
	        /*
	         * Get our customer registration module from our properties file.
	         * This should never change.
	         */
	        vistaModuleName = config.getString("ACCOUNT_REGISTRATION_MODULE");
	        
            /*
             * Ask a VistaModuleFactory for an instance of a VistaModule.
             * This VistaModule will correspond to the mainframe program CP0550N1 and will be localized for CH_US only.
             */
            VistaModuleFactory vistaModuleFactory = new VistaModuleFactory();
	        LegalEntity legalEntity = LegalEntity.CH_US;
	        Localizer localizer = new Localizer(legalEntity, Localizer.LANGUAGE_ENGLISH, Localizer.CURRENCY_US_DOLLARS);
	        VistaModule vistaModule = vistaModuleFactory.getVistaModuleFor(vistaModuleName, vistaModuleEnvironment, localizer);
	        
	        /*
	         * Use any/all of the following to generate debug messages to standard out or to the CICS logs.
	         */
	          vistaModule.setShowWebDebugMessages(false); //to standard out
	          vistaModule.setShowWebTimingMessages(false); //to standard out
	         /* vistaModule.setShowMainframeDebugMessages(true); //to mainframe CICS logs
	         */

	        /*
	         * Ask the VistaModule for its input record template (x 1 occurrence of input).
	         */
	        RecordSet requestRecordSet = vistaModule.createRecordSetFromTemplate(vistaModule.getRequestDataRecordTemplate(), 1);
	        
	        /*
	         * Set all Fields of the RecordSet appropriately.
	         * Fields are automatically defaulted with a value of the empty String.
	         * If a Field is not present here, it is defaulted to the empty String.
	         * If a Field value is explicitly specified as null or "", then it is set to the empty String.
	         */
	        requestRecordSet.setFieldValue(0, "EXTERNAL-REFERENCE", account.getProspectNumber());
	        requestRecordSet.setFieldValue(0, "VISTA-CUST-NAME", account.getCustomerName());
	        
	        boolean isParentSameAsSelf=false;
	        if(account.getParentCustNumber().equalsIgnoreCase(account.getVcn())){
	        	// the parent number is the same as the prospect number, dont need to set PARENT-NUMBER
	            // but flag as same for the batch update
	            isParentSameAsSelf=true;
	        }else {
	            // the parent number and prospect number are different, so send the parent number to vista
	            requestRecordSet.setFieldValue(0, "PARENT-NUMBER", account.getParentCustNumber());
	        }
	        
	        requestRecordSet.setFieldValue(0, "BUSINESS-ADDRESS-1", account.getBusinessAddress().getAddress1());
	        requestRecordSet.setFieldValue(0, "BUSINESS-ADDRESS-2", account.getBusinessAddress().getAddress2());
	        requestRecordSet.setFieldValue(0, "BUSINESS-ADDRESS-3", account.getBusinessAddress().getAddress3());
	        requestRecordSet.setFieldValue(0, "BUSINESS-ADDRESS-4", account.getBusinessAddress().getAddress4());
	        requestRecordSet.setFieldValue(0, "BUSINESS-CITY", account.getBusinessAddress().getCity());
	        requestRecordSet.setFieldValue(0, "BUSINESS-STATE", account.getBusinessAddress().getState());
	        requestRecordSet.setFieldValue(0, "BUSINESS-ZIP-CODE", account.getBusinessAddress().getZip());
	        requestRecordSet.setFieldValue(0, "INTL-BILL-ADDRESS", account.getBillToAddress().isInternational() ? "Y" : "N");
	        if(!account.getBillToAddress().equals(account.getBusinessAddress())){
		        requestRecordSet.setFieldValue(0, "BILL-ADDR-1", account.getBillToAddress().getAddress1());
		        requestRecordSet.setFieldValue(0, "BILL-ADDR-2", account.getBillToAddress().getAddress2());
		        requestRecordSet.setFieldValue(0, "BILL-ADDR-3", account.getBillToAddress().getAddress3());
		        requestRecordSet.setFieldValue(0, "BILL-ADDR-4", account.getBillToAddress().getAddress4());
		        requestRecordSet.setFieldValue(0, "BILL-ADDR-CITY", account.getBillToAddress().getCity());
		        requestRecordSet.setFieldValue(0, "BILL-ADDR-STATE", account.getBillToAddress().getState());
		        requestRecordSet.setFieldValue(0, "BILL-ADDR-ZIP", account.getBillToAddress().getZip());
	        }
	        if(!account.getShipAddress().equals(account.getBusinessAddress())){
		        requestRecordSet.setFieldValue(0, "SHIP-ADDR-1", account.getShipAddress().getAddress1());
		        requestRecordSet.setFieldValue(0, "SHIP-ADDR-2", account.getShipAddress().getAddress2());
		        requestRecordSet.setFieldValue(0, "SHIP-ADDR-3", account.getShipAddress().getAddress3());
		        requestRecordSet.setFieldValue(0, "SHIP-ADDR-4", account.getShipAddress().getAddress4());
		        requestRecordSet.setFieldValue(0, "SHIP-ADDR-CITY", account.getShipAddress().getCity());
		        requestRecordSet.setFieldValue(0, "SHIP-ADDR-STATE", account.getShipAddress().getState());
		        requestRecordSet.setFieldValue(0, "SHIP-ADDR-ZIP", account.getShipAddress().getZip());
	        }
	        requestRecordSet.setFieldValue(0, "USER-PHONE", account.getPhone().trim().length()==0 ? account.getIntlPhoneNumber() : account.getPhone());
	        requestRecordSet.setFieldValue(0, "USER-FAX", account.getFax().trim().length()==0 ? account.getIntlFaxNumber() : account.getFax());
	        
	        requestRecordSet.setFieldValue(0, "SOURCE-NAME", account.getAPCont());
	        requestRecordSet.setFieldValue(0, "PHONE-NUMBER", account.getAPContPhoneNumber());
	        requestRecordSet.setFieldValue(0, "EMAIL-ID", account.getAPContEmailAddress());
	        
	        
	        requestRecordSet.setFieldValue(0, "SALESMAN-ID-1", account.getSalesEngineer1());
	        requestRecordSet.setFieldValue(0, "SALESMAN-ID-2", account.getSalesEngineer2());
	        requestRecordSet.setFieldValue(0, "SALESMAN-ID-3", account.getSalesEngineer3());
	        requestRecordSet.setFieldValue(0, "SALESMAN-ID-4", account.getSalesEngineer4());

	        /* testing only... */
	        //requestRecordSet.setFieldValue(0, "SIC-NUM", "5399");
	        requestRecordSet.setFieldValue(0, "SIC-NUM", account.getSicCode());

	        requestRecordSet.setFieldValue(0, "SEND-CONFIRMATION-A", account.isSendConfirmation() ? "Y" : "N");
	        requestRecordSet.setFieldValue(0, "EXEMPT-CERT-REQUIRED-A", account.isExemptCertRequired() ? "Y" : "N");
	        requestRecordSet.setFieldValue(0, "COUNTRY-CODE", account.getBusinessAddress().getVistaCountryCode()); //Table: COUNTRY_MV Column: COUNTRY_CODE


	        String[] classification = getClassification(account,DBConn);

	        requestRecordSet.setFieldValue(0, "CLASSIFICATION", classification[0]);
	        requestRecordSet.setFieldValue(0, "CLASSIFICATION-TYPE", classification[1]);

	        //requestRecordSet.setFieldValue(0, "CLASSIFICATION", "END CUSTOMER");
	        //requestRecordSet.setFieldValue(0, "CLASSIFICATION-TYPE", "END CUSTOMER");

	        // don't know where to get this one.  Is it for distributors only?
	        //requestRecordSet.setFieldValue(0, "EATON-CUST-ID", null);

	        // don't know where to get this one.  Is it for distributors only?
	        //requestRecordSet.setFieldValue(0, "DUNS-NUM", null);

	        //may need this one if the sales reporting guys add it to the mainframe layout.
	        //requestRecordSet.setFieldValue(0, "CUST-CATEGORY", null);

	        /*
	         * Notes are also displayed on the mainframe so don't break notes in the middle of a word.
	         * The following code is needed to make sure that this does not happen.
	         * The JSP page that originally collects the notes should include a textarea of 60 chars wide and wrap=physical.
	         * Also, client-side validation should ensure that the total length of the notes cannot exceed 300 characters.
	         */
	        String notes = null;
	        if (account.getSendToVistaNotes().length() > 300) {
	            notes = account.getSendToVistaNotes().substring(0, 300);
	        }
	        else {
	            notes = account.getSendToVistaNotes();
	        }
	        
	        int end = 0;
	        int noteLine = 0;
	        while (end < notes.length()) {
	            if ((noteLine*60)+60 <= notes.length()) {
	                end = (noteLine*60)+60;
	            }
	            else {
	                end = notes.length();
	            }
	            String temp = notes.substring(noteLine*60, end);
	            requestRecordSet.setFieldValue(0, "USER-NOTES60-" + (noteLine+1), temp);
	            noteLine++;
	        }

	        requestRecordSet.setFieldValue(0, "DPC-NUM", account.getDpcNum());
	        requestRecordSet.setFieldValue(0, "STORE-ID-NUM", account.getStoreNumber());
	        
	        /*
	         * The TAX-ID-NUM and MODULE-LOADINGS are only needed for distributors.
	         */
	        Distributor distributor = null;
	        if (account.isDistributor()) {
	            distributor = DistributorDAO.getDistributor(account.getProspectNumber(), DBConn);
	            requestRecordSet.setFieldValue(0, "TAX-ID-NUM", distributor.getFederalTaxId());

                ArrayList modules=distributor.getLoadModules();
                for(int moduleNumber=0;moduleNumber<modules.size();moduleNumber++){
                	LoadModule module = (LoadModule)modules.get(moduleNumber);
                	requestRecordSet.setFieldValue(0, "MODULE-LOADINGS-" + (moduleNumber+1), module.getModuleCode());
                }
	        } //if (account.isDistributor())

	        String vistaUserId = theLoggedInUser.getVistaId();

	        /*
	        // TODO temporary - remove when hooked up!
	        if (vistaUserId == null || vistaUserId.trim().length() == 0) {
	            System.out.println("********** HARD-CODED VISTA NUMBER IN CLASS! ************");
	            SMRCLogger.error("********** HARD-CODED VISTA NUMBER IN CLASS! ************");
	            vistaUserId = "VSTA211";
	        }
	        */
	        
	        requestRecordSet.setFieldValue(0, "AUDIT-USER-ADDED", vistaUserId); //the logged-in user.
	        // TODO remove hardcoded id before moving to QA!!!!
	        //requestRecordSet.setFieldValue(0, "AUDIT-USER-ADDED", "VSTA290");
	        //requestRecordSet.setFieldValue(0, "AUDIT-USER-ADDED", " ");
	        requestRecordSet.setFieldValue(0, "SYNERGY-CODE", account.getSynergyCode());
	        requestRecordSet.setFieldValue(0, "PRIMARY-SEGMENT", account.getPrimarySegment().getSegmentId() + "");
	        requestRecordSet.setFieldValue(0, "SECONDARY-SEGMENT", account.getSecondarySegment().getSegmentId() + "");
	        requestRecordSet.setFieldValue(0, "GENESIS-NUM", account.getGenesisNumber());

	        vistaModule.setRequestRecordSet(requestRecordSet);
	        String moduleCallCode = VistaModule.CALL_CODE_INIT;

	        /*
	         * We suggest that the mainframe only return one record but the mainframe can send more or less.
	         */
	        int numberOfReplyDataRecordsExpected = 1;

	        /*
	         * This calls the mainframe and mutates the vistaModule Object itself.
	         * This will throw an exception if:
	         *
	         * 1. connectivity to the mainframe fails.
	         * 2. we cannot find out module's record layout in our local datastore.
	         * 3. the data lengths of the mainframe data and the web data do not match each other.
	         */
	        SMRCLogger.debug("SMRCAccountRegistrationService.registerAccount() - The VistaModule BEFORE it executes \n" + vistaModule);
	        SMRCLogger.debug("SMRCAccountRegistrationService.registerAccount() - CALLING THE MAINFRAME NOW!\n");
	        vistaModule.execute(moduleCallCode, vistaUserId, numberOfReplyDataRecordsExpected);
	        SMRCLogger.debug("SMRCAccountRegistrationService.registerAccount() - The VistaModule AFTER it executes \n" + vistaModule);


	        /*
	         * This gets any REPLY DATA records from the mainframe.
	         * The REPLY DATA record will hold any return values like REFERENCE-NUMBER AND VISTA-CUSTOMER-NUMBER.
	         */
	        RecordSet replyDataRecordSet = vistaModule.getReplyDataRecordSet();

	        if (replyDataRecordSet != null) {
		        /*
		         * We only expect to get up to one data record back so throw an exception if more than one.
		         */
		        if (replyDataRecordSet.getNumberOfRecords() > 1) {
		            throw new Exception("Too many records in reply from Vista!  Only one record is expected.");
		        }

		        /*
		         * Iterate over any REPLY DATA records to get the important values returned from the mainframe.
		         * This should be up to only one iteration due to the fact that we only expect to get up to one record back.
		         */
		        Iterator replyDataRecordSetIterator = replyDataRecordSet.iterator();
		        Record replyDataRecord = null;

		        while (replyDataRecordSetIterator.hasNext()) {
		            replyDataRecord = (Record)replyDataRecordSetIterator.next();
		            vistaCustomerNumber = replyDataRecord.getField("VISTA-CUST-NUM").getValue();
		            vistaReferenceNumber = replyDataRecord.getField("REFERENCE-NUM").getValue();
		            districtRegisteredTo = replyDataRecord.getField("SALESMAN-ID-1").getValue();

		            if (vistaReferenceNumber.trim().length() == 0){
		                throw new Exception ("Account registration failed!  Reason: Vista did not return a reference number.");
		            }

		        }
	        } //if (replyDataRecordSet != null)


	        /*
	         * This gets any REPLY ERROR records from the mainframe.
	         * The REPLY ERROR record will hold any return ERROR or INFORMATIONAL messages from the mainframe.
	         * If one or more ERROR messages are present, we will throw an exception with the appropriate ERROR text inside.
	         * If only INFORMATIONAL messages are returned, we will put them into the returned AccountRegistrationResults.
	         */
	        RecordSet replyErrorRecordSet = vistaModule.getReplyErrorRecordSet();

		    if (replyErrorRecordSet != null) {
		        /*
		         * Iterate over any REPLY ERROR records to get the important values returned from the mainframe.
		         * This should be up to only one iteration due to the fact that we only expect to get up to one record back.
		         */
		        Iterator replyErrorRecordSetIterator = replyErrorRecordSet.iterator();
		        Record replyErrorRecord = null;
		        //messageSB = new StringBuffer(1000);

		        /*
		         * Accumulate ALL of the ERROR/WARNING/INFORMATIONAL messages and neatly format them into one String.
		         * We don't really know what the mainframe will be sending so just collect them all for now.
		         */
		        String errorLabel = "";
		        while (replyErrorRecordSetIterator.hasNext()) {
		            replyErrorRecord = (Record)replyErrorRecordSetIterator.next();
		            if (replyErrorRecord.getField("ERROR-LEVEL").getValue().equalsIgnoreCase("E2")) {
		                errorLabel = "      Error: ";
		            }
		            else if (replyErrorRecord.getField("ERROR-LEVEL").getValue().equalsIgnoreCase("E3")) {
		                errorLabel = "    Warning: ";
		            }
		            else if (replyErrorRecord.getField("ERROR-LEVEL").getValue().equalsIgnoreCase("E4")) {
		                errorLabel = "Information: ";
		            }
		            else {
		                //this cannot really happen.
		                errorLabel = "";
		            }
		            messageSB.append(errorLabel).append(replyErrorRecord.getField("ERROR-DESC").getValue()).append("  ");
		            messageSB.append("(").append(replyErrorRecord.getField("ERROR-FIELD").getValue()).append(")<br>\n");
		        }

		        if (vistaModule.hasErrorMessages()) {
		            /*
		             * Our VistaModule returned ERROR (not just WARNING or INFORMATIONAL) messages.
		             * Since there is an ERROR, it means that we cannot register the account.
		             */
		        	SMRCLogger.warn("SMRCAccountRegistrationService.registerAccount():: Errors returned from VistaModule reply " + messageSB.toString());
		        	throw new VistaErrorMessageException(messageSB.toString());
		        //    throw new Exception(messageSB.toString());
		        }

		    } // if (replyErrorRecordSet != null)

	        /*
	         * Update our SMRC datastore locally with the returned values from the mainframe.
	         * If we got this far, then we have no major problems but the following method can still
	         * throw an exception.  Note that we CANNOT ROLLBACK the mainframe so we will have a problem.
	         */

		    updateSMRCDataStore(vistaReferenceNumber, vistaCustomerNumber, account.getProspectNumber(), isParentSameAsSelf, DBConn);

	        /*
	         * If we get this far, we have only WARNING/INFORMATIONAL messages.
	         * Return them in the AccountRegistrationResults.
	         * This is done in the "bottom code" after the finally block.
	         */

	    }
        catch (TransactionException te) {
            /*
             * If the VistaMessageService throws a TransactionException then the mainframe did not
             * reply within the set amount of time.  We will write "MF_ERROR" as the reference number
             * so that we can find these later.  Decisionstream should update based on prospect number
             * and fix up the records overnight.
             */
            SMRCLogger.error("SMRCAccountRegistrationService.registerAccount() - Vista TransactionException - ", te);
            updateSMRCDataStore("MF_ERROR", "", account.getProspectNumber(), false, DBConn);
            return new AccountRegistrationResults(vistaCustomerNumber, "", "", "Your request has been received and will be processed within 24 hours.");
        }
        catch(Exception e) {
        	if (!(e instanceof VistaErrorMessageException)){
        		// don't log error for VistaErrorMessageException
        		SMRCLogger.error("SMRCAccountRegistrationService.registerAccount() ", e);
        	}
        	throw e;
        //	SMRCLogger.error("SMRCAccountRegistrationService.registerAccount() ", e);
        //	throw e;
        }

        return new AccountRegistrationResults(vistaCustomerNumber, vistaReferenceNumber, districtRegisteredTo, messageSB.toString());

    } //method


	private static void updateSMRCDataStore(String aReferenceNumber, String aVistaCustomerNumber, String aProspectNumber, boolean isParentSameAsSelf, Connection DBConn) throws Exception  {

		Statement stmt = null;
		StringBuffer sqlSB = new StringBuffer(100);

		try {

		    stmt = DBConn.createStatement();

		    /*
		     * We will be submitting two separate SQL statements together in a "batch".
		     *
		     * The first statement is to temporarily tell the DB to ignore the foreign key constraints that we
		     * will be violating while trying to update the primary key of the parent table.
		     *
		     * SET CONSTRAINTS ALL DEFERRED
		     *
		     * The second statement is to update the necessary values.
		     *
		     * UPDATE CUSTOMER
		     *    SET VISTA_REFERENCE_NUMBER = aReferenceNumber,
		     *        VISTA_CUSTOMER_NUMBER = aVistaCustomerNumber
		     *  WHERE PROSPECT_NUMBER = aProspectNumber
		     */
		    SMRCLogger.debug("SQL SMRCAccountRegistrationService.updateSMRCDataStore():\nSET CONSTRAINTS ALL DEFERRED");
		    stmt.addBatch("SET CONSTRAINTS ALL DEFERRED");

		    sqlSB.append("UPDATE CUSTOMER SET ");
		    sqlSB.append("VISTA_REFERENCE_NUMBER = ").append("'").append(aReferenceNumber).append("'");
		    if(aVistaCustomerNumber.trim().length()!=0){
		    	sqlSB.append(",VISTA_CUSTOMER_NUMBER = ").append("'").append(aVistaCustomerNumber).append("' ");
		    	sqlSB.append(",CUSTOMER_STATUS = 'Active' ");
		    }else{
		    	sqlSB.append(",CUSTOMER_STATUS = 'Pending' ");
		    }
		    if(isParentSameAsSelf){
		        sqlSB.append(",PARENT_NUM=").append("'").append(aVistaCustomerNumber).append("' ");
		    }
		    sqlSB.append(" WHERE PROSPECT_NUMBER = ").append("'").append(aProspectNumber).append("'");
		    SMRCLogger.debug("SQL SMRCAccountRegistrationService.updateSMRCDataStore():\n" + sqlSB.toString());
		    stmt.addBatch(sqlSB.toString());

		    /*
		     * Whenever a batch is executed, there is an int return code for each batched statement.
		     * We are interested in the second one that holds the number of rows updated
		     * while executing the second SQL command - UPDATE CUSTOMER SET ...
		     */
		    int[] returnCodes = stmt.executeBatch();

			if ( returnCodes[1] < 1) {
			    throw new Exception ("No records were updated while trying to register prospect " + aProspectNumber + " !");
			}
			else if ( returnCodes[1] > 1) {
			    throw new Exception ("More than one record was updated while trying to register prospect " + aProspectNumber + " !");
			}

			/*
			 * If we COMMIT, then our SET CONSTRAINTS is ended.
			 * This is what we want because we don't want to leave the DB in a state that lets constraints be violated.
			 */
			//SMRCConnectionPoolUtils.commitTransaction(DBConn);

		}catch (Exception e)	{
			SMRCLogger.error("SMRCAccountRegistrationService.registerAccount(): " , e);

			/*
			 * If we ROLLBACK, then our SET CONSTRAINTS is ended.
			 * This is what we want because we don't want to leave the DB in a state that lets constraints be violated.
			 */
			//SMRCConnectionPoolUtils.rollbackTransaction(DBConn);
			throw e;
		}
		finally {
		    SMRCConnectionPoolUtils.close(stmt);
		}

	} //method


    /**
     * I determine if an Account is ready to attempt to be registered.
     * Some minimal requirements must be met before calling the Vista Customer Registration Process.
     * if boolean throwException=true an error causes an exception, otherwise a String of errors is returned.
     *
     * @throws - Exception if the Account does not meet minimal qualifications for registration.
     */

	public static String isQualified(Account anAccount, User theLoggedInUser, Connection DBConn, boolean throwException) throws Exception {
	    final String preQualifyErrorMessagePrefix = "<b>This account does not pre qualify for registration!</b><br>Reason:<br> ";
	    StringBuffer errorBuffer = new StringBuffer(preQualifyErrorMessagePrefix);
	    boolean foundError=false;

	    /*
	     * If an acocunt is not a prospect, it cant be sent.
	     */
	    if (theLoggedInUser.getVistaId().trim().length()==0) {
	    	errorBuffer.append("> You do not have a valid Vista account to register a customer.<br>");
	    	foundError=true;
	    }

	    
	    /*
	     * If an acocunt is not a prospect, it cant be sent.
	     */
	    if (!anAccount.isProspect()) {
	    	errorBuffer.append("> Not a prospect account.<br>");
	    	foundError=true;
	    }

	    /*
	     * Distributor accounts
	     */
	    if(anAccount.isDistributor()){
	    	Distributor distributor = DistributorDAO.getDistributor(anAccount.getProspectNumber(), DBConn);

		    /*
		     * Distributor accounts must have a federal tax id
		     */
	    	if(isValidTaxId(distributor.getFederalTaxId())){
	        	errorBuffer.append("> Distributor Application - Federal Tax Id is empty or of invalid format.  Format i.e. 99-9999999<br>");
	        	foundError=true;
	    	}
	    	if(((isEmpty(anAccount.getFax()) && isEmpty(anAccount.getIntlFaxNumber())) || (anAccount.getFax().length()<10) && anAccount.getIntlFaxNumber().length()<10)){
	        	errorBuffer.append(">Account Profile - Fax number is required for distributors or fax number is invalid.<br>");
	        	foundError=true;

	    	}


		    /*
		     * Primary and Secondary segments are required
		     */
		    if(anAccount.getPrimarySegment()==null || anAccount.getSecondarySegment()==null){
		    	errorBuffer.append("> Primary and/or Secondary segment is empty.<br>");
		    	foundError=true;
		    }else{

		    	/*
		    	 * Find out if the account is the exception to the distributor rule
		    	 * otherwise make sure at least one product module is selected
		    	 */
		    	ArrayList segments = anAccount.getSegments();
		    	boolean foundOtherDistributors=false;
		    	boolean foundOtherDistributor = false;
		    	for(int i=0;i<segments.size();i++){
		    		Segment segment = (Segment)segments.get(i);
		    		if(segment.getName().trim().equalsIgnoreCase("Other Distributors")){
		    			foundOtherDistributors=true;
		    		}else if(segment.getName().trim().equalsIgnoreCase("Other Distributor")){
		    			foundOtherDistributor=true;
		    		}
		    	}
		    	if(!foundOtherDistributors || !foundOtherDistributor){
		    		distributor = DistributorDAO.getDistributor(anAccount.getProspectNumber(), DBConn);
	                ArrayList modules=distributor.getLoadModules();
	                if(modules.size()==0){
	    	        	errorBuffer.append("> At least one Product Module must be selected for a distributor.<br>");
	    	        	foundError=true;
	                }
		    	}


			    /*
			     *  CLASSIFICATION and CLASSIFICATION-TYPE are required
			     */
		        String[] classification = getClassification(anAccount,DBConn);
		        if(isEmpty(classification[0])){
			    	errorBuffer.append("> CLASSIFICATION cannot be determined from selected segments.<br>");
			    	foundError=true;
		        }
		        if(isEmpty(classification[1])){
			    	errorBuffer.append("> CLASSIFICATION-TYPE cannot be determined from selected segments.<br>");
			    	foundError=true;
		        }

		    }

	    }


	    /*
	     *  If it has a value, DPC NUM code must be exactly 9 characters
	     */
	    if(!isEmpty(anAccount.getDpcNum()) && anAccount.getDpcNum().length()!=9){
	    	errorBuffer.append("> DPC-Num is not 9 characters.<br>");
	    	foundError=true;
	    }

	    /*
	     *  If it has a value, Store Number must be exactly 4 numbers
	     */
	    if(!isEmpty(anAccount.getStoreNumber()) && anAccount.getStoreNumber().length()!=4){
	    	errorBuffer.append("> Store Number is not 4 numbers.<br>");
	    	foundError=true;
	    }

	    /*
	     *  If it has a value, Genesis code must be exactly 7 numbers
	     */
	    if(!isEmpty(anAccount.getGenesisNumber()) && anAccount.getGenesisNumber().length()!=7){
	    	errorBuffer.append("> Genesis Number is not 7 numbers.<br>");
	    	foundError=true;
	    }

	    /*
	     *  If it has a value, synergy code must be exactly 3 characters
	     */
	    if(!isEmpty(anAccount.getSynergyCode()) && anAccount.getSynergyCode().length()!=3){
	    	errorBuffer.append("> Synergy code is not 3 characters.<br>");
	    	foundError=true;
	    }

	    /*
	     *  Line 1 of business address is required
	     */
	    if(isEmpty(anAccount.getBusinessAddress().getAddress1())){
	    	errorBuffer.append("> Business Address Line 1 is empty.<br>");
	    	foundError=true;
	    }


    	/*
    	 *  Phone number is required and cant be less than 10 characters
    	 * TODO do I need special logic to make sure intl phone is only use don intl accts?
    	 */
    	if((isEmpty(anAccount.getPhone()) || anAccount.getPhone().length()<10) && (isEmpty(anAccount.getIntlPhoneNumber()) || anAccount.getIntlPhoneNumber().length()<10)){
    		errorBuffer.append("> Phone Number is empty or of invalid length.<br>");
    		foundError=true;
    	}
    	
    
	    /*
	     *  Direct accounts must have a valid fax number
	     * TODO do I need special logic to make sure intl fax is only use don intl accts?
	     */
	    if(anAccount.isDirect() && ((isEmpty(anAccount.getFax()) && isEmpty(anAccount.getIntlFaxNumber())) || (anAccount.getFax().length()<10) && anAccount.getIntlFaxNumber().length()<10)){
			errorBuffer.append("> Fax number is required for direct accounts or fax number is invalid.<br>");
			foundError=true;
	    }

	    /*
	     *  Primary sales engineer is required.
	     */
	    if(isEmpty(anAccount.getSalesEngineer1())){
			errorBuffer.append("> Primary Sales Engineer is empty.<br>");
			foundError=true;
	    }

    	/*
    	 *  Country is required
    	 */
    	if(isEmpty(anAccount.getBusinessAddress().getCountry())){
    		errorBuffer.append("> Business Address - Country is empty.<br>");
    		foundError=true;
    	}

	    /*
	     *  For domestic accounts, the following are required...
	     */
	    if(anAccount.getBusinessAddress().isDomestic()){

	    	/*
	    	 *  Zip code, city, and state are required.
	    	 */
		    if(isEmpty(anAccount.getBusinessAddress().getZip()) || isEmpty(anAccount.getBusinessAddress().getState()) || isEmpty(anAccount.getBusinessAddress().getCity())){
		    	errorBuffer.append("> Business Address - Zip Code, State, or City are empty.<br>");
		    	foundError=true;
		    }

	    	/*
	    	 *  SIC code is required.  This is derived from the secondary segment.
	    	 */
	    	if(isEmpty(anAccount.getSicCode())){
	    		errorBuffer.append("> Problems getting SIC code from secondary segment or secondary segment is invalid.<br>");
	    		foundError=true;
	    	}
	    }



	    /*
	     *  Send to vista notes cant be more than 300 characters
	     */
	    if(anAccount.getSendToVistaNotes().length()>295){
			errorBuffer.append("> Notes to send to vista cannot exceed 300 Characters.");
			foundError=true;
	    }

	    /*else{
	    	if(isEmpty(anAccount.getIntlPhoneNumber()) || isEmpty(anAccount.getIntlFaxNumber()) || anAccount.getIntlPhoneNumber().length()>21 || anAccount.getIntlFaxNumber().length()>21){
	    		errorBuffer.append("> International Phone Number or Fax Number is empty or of invalid length.<br>");
	    		foundError=true;

	    	}
	    }*/

	    if(foundError){
	    	/*
	    	 *  Some callers expect an exception thrown if there is an error
	    	 */
	    	if(throwException){
	    		
	    		// Output the account information to try and get that pesky error from 'Does not qualify'

	    		throw new Exception (errorBuffer.toString() + "\n\nAccount Information : \n" + anAccount.toString());
	    	}
    		/*
    		 * and some expect an error string
    		 */
	    	return(errorBuffer.toString());
	    	

	    }
	    
	    return "";
	    

	} //method

    private static boolean isEmpty(String string){
    	if(string==null || string.equals("")){
    		return true;
    	}
    	return false;
    }

    private static boolean isValidTaxId(String string){
    	if(string==null || string.equals("") || string.length()!=10 || string.charAt(2)!='-'){
    		return true;
    	}
    	return false;
    }

    /*
     * This returns an array of 2 strings.  The first string is the Classification
     * and the second string is the Classification Type
     * 	classification[0]=Classification
     *	classification[1]=Classification Type
     */
	private static String[] getClassification(Account account, Connection DBConn) throws Exception  {
		String[] classification = new String[2];
		if(account==null){
			SMRCLogger.debug("account is null");
		}
		String selectedCat = AccountDAO.getCustomerCategory(account.getVcn(),DBConn);
		if(selectedCat==null){
			SMRCLogger.debug("selected cat is null");
		}
		if(selectedCat.trim().length()!=0){
			if(selectedCat.equals("01")){
				classification[0]="DIY";
				classification[1]="DIY";
			}else if(selectedCat.equals("09")){
				classification[0]="DISTRIBUTOR";
				classification[1]="WESCO";
			}else if(selectedCat.equals("10")){
				classification[0]="DISTRIBUTOR";
				classification[1]="INDEPENDENT";
			}else if(selectedCat.equals("13")){
				classification[0]="DISTRIBUTOR";
				classification[1]="IND SPCL HVAC";
			}else if(selectedCat.equals("14")){
				classification[0]="DISTRIBUTOR";
				classification[1]="IND SPCL OTHER";
			}else if(selectedCat.equals("15")){
				classification[0]="DISTRIBUTOR";
				classification[1]="WESCO MFG STR";
			}else if(selectedCat.equals("19")){
				classification[0]="DISTRIBUTOR";
				classification[1]="MISC STK DISTRB";
			}else{
				throw new Exception("Error determining the Classification and Classification Type");
			}
		}else{
			boolean isHVAC = false;
			boolean isManufacturedHousing = false;
			boolean isOtherDistributors = false;
			boolean isMiscStockingDist = false;
			boolean isChannel = false;
			boolean isDIY = false;

			ArrayList segments = account.getSegments();

			for(int i=0;i<segments.size();i++){
				Segment segment = (Segment)segments.get(i);
				if(segment.getName().equals("HVAC / Mechanical Distributors")){
					isHVAC=true;
				}else if(segment.getName().equals("Manufactured Housing")){
					isManufacturedHousing=true;
				}else if(segment.getName().equals("Other Distributors")){
					isOtherDistributors=true;
				}else if(segment.getName().equals("Misc. Stocking Distributor")){
					isMiscStockingDist=true;
				}else if(segment.getName().equals("Channel")){
					isChannel=true;
				}else if(segment.getName().equals("Retail / DIY")){
					isDIY=true;
				}
			}

			if(!isChannel && account.isDirect()){
				classification[0]="DIRECT";
				classification[1]="DIRECT";
			}else if(!isChannel && !account.isDirect()){
				classification[0]="END CUSTOMER";
				classification[1]="END CUSTOMER";
			}else if(isHVAC){
				classification[0]="DISTRIBUTOR";
				classification[1]="IND SPCL HVAC";
			}else if(isManufacturedHousing){
				classification[0]="DISTRIBUTOR";
				classification[1]="WESCO MFG STR";
			}else if(isOtherDistributors && !isHVAC && !isManufacturedHousing){
				classification[0]="DISTRIBUTOR";
				classification[1]="IND SPCL OTHER";
			}else if(isMiscStockingDist){
				classification[0]="DISTRIBUTOR";
				classification[1]="MISC STK DISTRB";
			}else if(isChannel && !isOtherDistributors && !isDIY){
				classification[0]="DISTRIBUTOR";
				classification[1]="INDEPENDENT";
			}else if(isDIY){
				classification[0]="DIY";
				classification[1]="DIY";
			}else{
				throw new Exception("Error determining the Classification and Classification Type");
			}
		}

		SMRCLogger.debug("SMRCAccountRegistration.getClassification -\n classification = " + classification[0] + "\n classification type = " + classification[1]);
		return classification;

	}


	public static DuplicateCustomerBean[] getPotentialDuplicates (Account account, User user) throws VistaErrorMessageException, Exception{
		
		// TODO remove boolean from arguments to call real callVistaDuplicateChecking method
		DuplicateCustomerBean[] beans = callVistaDuplicateChecking(account,user);
		if (beans == null){
			SMRCLogger.debug("SMRCAccountRegistrationService.getPotentialDuplicates()  potential duplicates found: beans is null");
		} else {
			SMRCLogger.debug("SMRCAccountRegistrationService.getPotentialDuplicates()  potential duplicates found: " + beans.length);
		}
		return beans;
		
	}
	
	
	private static DuplicateCustomerBean[] callVistaDuplicateChecking (Account account, User user) throws VistaErrorMessageException, Exception {
	 	String vistaModuleName = null;
        String vistaModuleEnvironment = null;
        DuplicateCustomerBean[] beans = null;
        StringBuffer messageSB = new StringBuffer();
        
        try {
        	/*
             * Get the Vista environment from our properties file.
             * This must change from DEV, QA, TRNG, PROD as needed.
             */
	        vistaModuleEnvironment = config.getString("VISTA_ENVIRONMENT");
	        /*
	         * Get our customer registration module from our properties file.
	         * This should never change.
	         */
	        vistaModuleName = config.getString("DUPLICATE_CUSTOMER_AUDIT_MODULE");
	        
            /*
             * Ask a VistaModuleFactory for an instance of a VistaModule.
             * This VistaModule will correspond to the mainframe program CP0550N1 and will be localized for CH_US only.
             */
            VistaModuleFactory vistaModuleFactory = new VistaModuleFactory();
	        LegalEntity legalEntity = LegalEntity.CH_US;
	        Localizer localizer = new Localizer(legalEntity, Localizer.LANGUAGE_ENGLISH, Localizer.CURRENCY_US_DOLLARS);
	        VistaModule vistaModule = vistaModuleFactory.getVistaModuleFor(vistaModuleName, vistaModuleEnvironment, localizer);
	        
	        /*
	         * Use any/all of the following to generate debug messages to standard out or to the CICS logs.
	         */
	          vistaModule.setShowWebDebugMessages(false); //to standard out
	          vistaModule.setShowWebTimingMessages(false); //to standard out
	         /* vistaModule.setShowMainframeDebugMessages(true); //to mainframe CICS logs
	         */

	        /*
	         * Ask the VistaModule for its input record template (x 1 occurrence of input).
	         */
	        RecordSet requestRecordSet = vistaModule.createRecordSetFromTemplate(vistaModule.getRequestDataRecordTemplate(), 1);
	        
	        /*
	         * Set all Fields of the RecordSet appropriately.
	         * Fields are automatically defaulted with a value of the empty String.
	         * If a Field is not present here, it is defaulted to the empty String.
	         * If a Field value is explicitly specified as null or "", then it is set to the empty String.
	         */
	        requestRecordSet.setFieldValue(0, "VISTA-CUST-NAME", account.getCustomerName());
	        requestRecordSet.setFieldValue(0, "BUSINESS-ADDRESS-1", account.getBusinessAddress().getAddress1());
	        requestRecordSet.setFieldValue(0, "BUSINESS-ADDRESS-2", account.getBusinessAddress().getAddress2());
	        requestRecordSet.setFieldValue(0, "BUSINESS-ADDRESS-3", account.getBusinessAddress().getAddress3());
	        requestRecordSet.setFieldValue(0, "BUSINESS-ADDRESS-4", account.getBusinessAddress().getAddress4());
	        requestRecordSet.setFieldValue(0, "BUSINESS-CITY", account.getBusinessAddress().getCity());
	        requestRecordSet.setFieldValue(0, "BUSINESS-STATE", account.getBusinessAddress().getState());
	        requestRecordSet.setFieldValue(0, "BUSINESS-ZIP-CODE", account.getBusinessAddress().getZip());
	        requestRecordSet.setFieldValue(0, "COUNTRY-CODE", account.getBusinessAddress().getVistaCountryCode()); //Table: COUNTRY_MV Column: COUNTRY_CODE
	        
	        String phoneNumber = account.getPhone().trim().length()==0 ? account.getIntlPhoneNumber() : account.getPhone();
	        if (phoneNumber.length() > 10){
	        	phoneNumber = phoneNumber.substring(0,10);
	        }
	        requestRecordSet.setFieldValue(0, "PHONE-NUMBER", phoneNumber);
	        
	        String faxNumber = account.getFax().trim().length()==0 ? account.getIntlFaxNumber() : account.getFax();
	        if (faxNumber.length() > 10){
	        	faxNumber = faxNumber.substring(0,10);
	        }
	        requestRecordSet.setFieldValue(0, "FAX-NUMBER", faxNumber);
	        
	        /*requestRecordSet.setFieldValue(0, "SOURCE-NAME", account.getAPCont());
	        requestRecordSet.setFieldValue(0, "PHONE-NUMBER", account.getAPContPhoneNumber());
	        requestRecordSet.setFieldValue(0, "EMAIL-ID", account.getAPContEmailAddress());*/
	        

	        vistaModule.setRequestRecordSet(requestRecordSet);
	        String moduleCallCode = VistaModule.CALL_CODE_INIT;

	        /*
	         * We suggest that the mainframe only return one record but the mainframe can send more or less.
	         */
	        int numberOfReplyDataRecordsExpected = 1;

	        /*
	         * This calls the mainframe and mutates the vistaModule Object itself.
	         * This will throw an exception if:
	         *
	         * 1. connectivity to the mainframe fails.
	         * 2. we cannot find out module's record layout in our local datastore.
	         * 3. the data lengths of the mainframe data and the web data do not match each other.
	         */
	        SMRCLogger.debug("SMRCAccountRegistrationService.callVistaDuplicateChecking() - The VistaModule BEFORE it executes \n" + vistaModule);
	        SMRCLogger.debug("SMRCAccountRegistrationService.callVistaDuplicateChecking() - CALLING THE MAINFRAME NOW!\n");
	        vistaModule.execute(moduleCallCode, user.getVistaId(), numberOfReplyDataRecordsExpected);
	        SMRCLogger.debug("SMRCAccountRegistrationService.callVistaDuplicateChecking() - The VistaModule AFTER it executes \n" + vistaModule);


	        /*
	         * This gets any REPLY DATA records from the mainframe.
	         * The REPLY DATA record will hold any return values like REFERENCE-NUMBER AND VISTA-CUSTOMER-NUMBER.
	         */
	        RecordSet replyDataRecordSet = vistaModule.getReplyDataRecordSet();
	        ArrayList beanList = new ArrayList();
	        if (replyDataRecordSet != null) {
		        
		        /*
		         * Iterate over any REPLY DATA records to get the important values returned from the mainframe.
		         * This should be up to only one iteration due to the fact that we only expect to get up to one record back.
		         */
		        Iterator replyDataRecordSetIterator = replyDataRecordSet.iterator();
		        Record replyDataRecord = null;

		        while (replyDataRecordSetIterator.hasNext()) {
		            replyDataRecord = (Record)replyDataRecordSetIterator.next();
		            DuplicateCustomerBean bean = new DuplicateCustomerBean();
		            bean.setBusinessAddress1(replyDataRecord.getField("BUSINESS-ADDRESS-1").getValue());
		            bean.setBusinessAddress2(replyDataRecord.getField("BUSINESS-ADDRESS-2").getValue());
		            bean.setBusinessAddress3(replyDataRecord.getField("BUSINESS-ADDRESS-3").getValue());
		            bean.setBusinessAddress4(replyDataRecord.getField("BUSINESS-ADDRESS-4").getValue());
		            bean.setBusinessCity(replyDataRecord.getField("BUSINESS-CITY").getValue());
		            bean.setBusinessState(replyDataRecord.getField("BUSINESS-STATE").getValue());
		            bean.setBusinessZip(replyDataRecord.getField("BUSINESS-ZIP-CODE").getValue());
		            bean.setCategoryCode(replyDataRecord.getField("CUST-CATAGORY").getValue());
		            bean.setCategoryDescription(replyDataRecord.getField("CATEGORY-DESCRIPTION").getValue());
		            bean.setFaxNumber(replyDataRecord.getField("FAX-NUMBER").getValue());
			        
			        /*bean.setAPCont(replyDataRecord.getField("SOURCE-NAME").getValue());
			        bean.setAPContPhoneNumber(replyDataRecord.getField("PHONE-NUMBER").getValue());
			        bean.setAPContEmailAddress(replyDataRecord.getField("EMAIL-ID").getValue());*/
			        
		            bean.setPhoneNumber(replyDataRecord.getField("PHONE-NUMBER").getValue());
		            bean.setSalesLocation(replyDataRecord.getField("SALES-LOCATION").getValue());
		            bean.setSalesmanFirstName(replyDataRecord.getField("SALESMAN-FIRST-NAME-01").getValue());
		            bean.setSalesmanId(replyDataRecord.getField("SALESMAN-ID-1").getValue());
		            bean.setSalesmanLastName(replyDataRecord.getField("SALESMAN-LAST-NAME-01").getValue());
		            bean.setSpGeog(replyDataRecord.getField("SP-GEOG").getValue());
		            bean.setStatus(replyDataRecord.getField("STATUS").getValue());
		            bean.setStatusDescription(replyDataRecord.getField("STATUS-DESCRIPTION").getValue());
		            bean.setVistaCustomerName(replyDataRecord.getField("VISTA-CUST-NAME").getValue());
		            bean.setVistaCustomerNumber(replyDataRecord.getField("VISTA-CUST-NUM").getValue());
		            beanList.add(bean);
		        }
	        } //if (replyDataRecordSet != null)

	        int size = beanList.size();
	        beans = new DuplicateCustomerBean[size];
	        for (int i=0; i < size; i++){
	        	beans[i] = (DuplicateCustomerBean) beanList.get(i);
	        }

	        /*
	         * This gets any REPLY ERROR records from the mainframe.
	         * The REPLY ERROR record will hold any return ERROR or INFORMATIONAL messages from the mainframe.
	         * If one or more ERROR messages are present, we will throw an exception with the appropriate ERROR text inside.
	         * If only INFORMATIONAL messages are returned, we will put them into the returned AccountRegistrationResults.
	         */
	        RecordSet replyErrorRecordSet = vistaModule.getReplyErrorRecordSet();

		    if (replyErrorRecordSet != null) {
		        /*
		         * Iterate over any REPLY ERROR records to get the important values returned from the mainframe.
		         * This should be up to only one iteration due to the fact that we only expect to get up to one record back.
		         */
		        Iterator replyErrorRecordSetIterator = replyErrorRecordSet.iterator();
		        Record replyErrorRecord = null;
		        //messageSB = new StringBuffer(1000);

		        /*
		         * Accumulate ALL of the ERROR/WARNING/INFORMATIONAL messages and neatly format them into one String.
		         * We don't really know what the mainframe will be sending so just collect them all for now.
		         */
		        String errorLabel = "";
		        while (replyErrorRecordSetIterator.hasNext()) {
		            replyErrorRecord = (Record)replyErrorRecordSetIterator.next();
		            if (replyErrorRecord.getField("ERROR-LEVEL").getValue().equalsIgnoreCase("E2")) {
		                errorLabel = "      Error: ";
		            }
		            else if (replyErrorRecord.getField("ERROR-LEVEL").getValue().equalsIgnoreCase("E3")) {
		                errorLabel = "    Warning: ";
		            }
		            else if (replyErrorRecord.getField("ERROR-LEVEL").getValue().equalsIgnoreCase("E4")) {
		                errorLabel = "Information: ";
		            }
		            else {
		                //this cannot really happen.
		                errorLabel = "";
		            }
		            messageSB.append(errorLabel).append(replyErrorRecord.getField("ERROR-DESC").getValue()).append("  ");
		            messageSB.append("(").append(replyErrorRecord.getField("ERROR-FIELD").getValue()).append(")<br>\n");
		        }

		        if (vistaModule.hasErrorMessages()) {
		            /*
		             * Our VistaModule returned ERROR (not just WARNING or INFORMATIONAL) messages.
		             * Since there is an ERROR, it means that we cannot register the account.
		             */
		          //  throw new Exception(messageSB.toString());
		        	SMRCLogger.warn("SMRCAccountRegistrationService.callVistaDuplicateChecking():: Errors returned from VistaModule reply " + messageSB.toString());
		        	throw new VistaErrorMessageException(messageSB.toString()); 
		        } else if (vistaModule.hasInformationalMessages() || vistaModule.hasWarningMessages()){
		        	SMRCLogger.info("SMRCAccountRegistrationService.callVistaDuplicateChecking() - messages from VistaModule reply: " + messageSB.toString());
		        }

		    } // if (replyErrorRecordSet != null)

	        

	    }
        catch (TransactionException te) {
            /*
             * If the VistaMessageService throws a TransactionException then the mainframe did not
             * reply within the set amount of time.  We will write "MF_ERROR" as the reference number
             * so that we can find these later.  Decisionstream should update based on prospect number
             * and fix up the records overnight.
             */
            SMRCLogger.error("SMRCAccountRegistrationService.callVistaDuplicateChecking() - Vista TransactionException - ", te);
            throw te;
        }
        catch(Exception e) {
        	if (!(e instanceof VistaErrorMessageException)){
        		// don't log error for VistaErrorMessageException
        		SMRCLogger.error("SMRCAccountRegistrationService.callVistaDuplicateChecking() ", e);
        	}
        	throw e;
        }
	
		return beans;
	}

} //class
