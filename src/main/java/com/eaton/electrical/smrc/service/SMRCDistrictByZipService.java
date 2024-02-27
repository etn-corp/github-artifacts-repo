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
 * This class provides the services necessary to send and receive data to and from VISTA for looking up District by business address zip code.
 *
 * This class uses the VistaMessageService (VMS) for VISTA communication.
 * Note that the VistaMessageService does NOT use Log4J internally so internal error messages will go to standard out.
 * The VistaMessageService logs its error messages to the console.
 *
 * @see VistaMessageSerivce at http://java.ch.etn.com/webservices/java/vista_message.html
 *
 */
public class SMRCDistrictByZipService {

    private static ResourceBundle config = ResourceBundle.getBundle ("com.eaton.electrical.smrc.SMRC") ;

    public static String returnDistrictFromVista(String zip, String vistaID) throws Exception {
    	
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
	        
	        /*
             * Get the Vista environment from our properties file.
             * This must change from DEV, QA, TRNG, PROD as needed.
             */
	        vistaModuleEnvironment = config.getString("VISTA_ENVIRONMENT");
	        /*
	         * Get our customer registration module from our properties file.
	         * This should never change.
	         */
	        //vistaModuleName = config.getString("ACCOUNT_REGISTRATION_MODULE");
	        vistaModuleName = "CP1480N4";
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

	        requestRecordSet.setFieldValue(0, "ZIP", zip);
	        

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
	        vistaModule.execute(moduleCallCode, vistaID, numberOfReplyDataRecordsExpected);
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
		            
		            districtRegisteredTo = replyDataRecord.getField("DISTRICT").getValue();
		            
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

	    }
        catch (TransactionException te) {
            /*
             * If the VistaMessageService throws a TransactionException then the mainframe did not
             * reply within the set amount of time.  We will write "MF_ERROR" as the reference number
             * so that we can find these later.  Decisionstream should update based on prospect number
             * and fix up the records overnight.
             */
            SMRCLogger.error("SMRCDistrictByZipService.returnDistrictFromVista() - Vista TransactionException - ", te);
        }
        catch(Exception e) {
        	if (!(e instanceof VistaErrorMessageException)){
        		// don't log error for VistaErrorMessageException
        		SMRCLogger.error("SMRCDistrictByZipService.returnDistrictFromVista() - Vista TransactionException - ", e);
        	}
        	throw e;
        }
        return districtRegisteredTo;

    } //method

} //class
