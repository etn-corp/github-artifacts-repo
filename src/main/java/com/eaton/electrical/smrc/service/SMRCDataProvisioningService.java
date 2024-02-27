package com.eaton.electrical.smrc.service;

import java.util.*;
//import java.sql.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.exception.*;

import com.etn.cutlerhammer.VistaMessageService.Data.*;
import com.etn.cutlerhammer.VistaMessageService.Data.Record;
import com.etn.cutlerhammer.VistaMessageService.Transaction.*;
import com.etn.cutlerhammer.VistaMessageService.Util.*;

/**
 * @author e0062722
 *
 * This class provides the services necessary call Vista for data security (provisioning).
 * 
 * This class uses the VistaMessageService (VMS) for VISTA communication.
 * Note that the VistaMessageService does NOT use Log4J internally so internal error messages will go to standard out. 
 * The VistaMessageService logs its error messages to the console. 
 * 
 * @see VistaMessageSerivce at http://java.ch.etn.com/webservices/java/vista_message.html
 * 
 */

public class SMRCDataProvisioningService {


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
        public static VistaSecurity getVistaSecurity(User theLoggedInUser) throws Exception {
        	boolean universalAccess = false;
            Collection vistaSecurityRecords = new ArrayList();
            if(theLoggedInUser==null) {
                SMRCLogger.warn("SMRCDataProvisioningService.getVistaSecurity() - a null user object was passed to getVista Security.  This should never happen");
            	return new VistaSecurity(universalAccess, vistaSecurityRecords);
            }
            if(theLoggedInUser.getVistaId().trim().length()==0){
            	SMRCLogger.debug("SMRCDataProvisioningService.getVistaSecurity() - User (" + theLoggedInUser.getUserid() + ") has no Vista ID, so VMS Not called");
            	return new VistaSecurity(universalAccess, vistaSecurityRecords);
            }
           
            SMRCLogger.debug("user=" + theLoggedInUser.getUserid() + " " + theLoggedInUser.getVistaId() + " " + theLoggedInUser.getVistaId().length());
            //if(true){
           // return new VistaSecurity(universalAccess, vistaSecurityRecords);
            //}

            
          //  Connection DBConn = null;
            String vistaModuleName = null; 
            String vistaModuleEnvironment = null;
            StringBuffer messageSB = null;
            

            try {

               // DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();
                
    	        /*
                 * Get the Vista environment from our properties file.
                 * This must change from DEV, QA, TRNG, PROD as needed.
                 */
    	        vistaModuleEnvironment = config.getString("VISTA_ENVIRONMENT");
    	        /*
    	         * Get our customer registration module from our properties file.
    	         * This should never change.
    	         */
    	        vistaModuleName = config.getString("DATA_SECURITY_MODULE");
    	        
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
    	        
    	        String vistaUserId = theLoggedInUser.getVistaId();
    	        
    	        /*
    	         * Set all input Fields of the RecordSet appropriately.
    	         * Fields are automatically defaulted with a value of the empty String.
    	         * If a Field is not present here, it is defaulted to the empty String.
    	         * If a Field value is explicitly specified as null or "", then it is set to the empty String.
    	         */
     	        requestRecordSet.setFieldValue(0, "USERID", vistaUserId);
   	        	        
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
    	        SMRCLogger.debug("SMRCDataProvisioningService.getVistaSecurity - The VistaModule BEFORE it executes \n" + vistaModule);
    	        SMRCLogger.debug("SMRCDataProvisioningService.getVistaSecurity - CALLING THE MAINFRAME NOW!\n");
    	        vistaModule.execute(moduleCallCode, vistaUserId, numberOfReplyDataRecordsExpected);
    	        SMRCLogger.debug("SMRCDataProvisioningService.getVistaSecurity - The VistaModule AFTER it executes \n" + vistaModule);
    	        
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
    		            throw new TransactionException("Too many records in reply from Vista!  Only one record is expected.");
    		        }
    		        
    		        
    		        VSRecord vistaSecurityRecord = null;
    		        
    		        String team = null;
    		        String level = null;
    		        String salesman = null;  //Y or N flag
    		        String maint = null;  //Y or N flag
    		        
    		        /*
    		         * Iterate over any REPLY DATA records to get the important values returned from the mainframe.
    		         * This should be up to only one iteration due to the fact that we only expect to get up to one record back.
    		         */
    		        Iterator replyDataRecordSetIterator = replyDataRecordSet.iterator();
    		        Record replyDataRecord = null;
    		       
    		        while (replyDataRecordSetIterator.hasNext()) {  
    		            
    		            replyDataRecord = (Record)replyDataRecordSetIterator.next();
    		            //SMRCLogger.debug("********************************replyDataRecord = " + replyDataRecord.toString());
    		            
    		            String universalAccessString = replyDataRecord.getField("UNIVERSAL-ACCESS").getValue();
    		            if (universalAccessString.equalsIgnoreCase("Y")) {
    		                universalAccess = true;
    		            }
    		            
    		            /*
    		             * Currently we have 30 possible occurrences of security records possible.
    		             */
    		            
    		            for (int securityRecordNumber=1; securityRecordNumber<30; securityRecordNumber++) {
	    		            
    		                team = replyDataRecord.getField("ONLINE-TEAM-" + securityRecordNumber).getValue();	            
	    		            
    		                /*
    		                 * If the team is 4 characters then it is a salesman ID and
    		                 * level, salesman, and maint might be null because they are assumed values in vista
    		                 */
    		                if(team.trim().length()==4){
	    		                level = "TEAM";  
		    		            salesman = "Y";
		    		            maint = "Y";
	    		            }else{
	    		                level = replyDataRecord.getField("ONLINE-LEVEL-" + securityRecordNumber).getValue();  
		    		            salesman = replyDataRecord.getField("ONLINE-SLSMN-" + securityRecordNumber).getValue();      		            
		    		            maint = replyDataRecord.getField("ONLINE-MAINT-" + securityRecordNumber).getValue();  
	    		            }

	    		            if (team.trim().length() > 0) {
	    		                //we found a populated occurrence so add it.
		    		            vistaSecurityRecord = new VSRecord(team, level, salesman, maint);
		    		            vistaSecurityRecords.add(vistaSecurityRecord);
	    		            }
	    		            else {
	    		                //if we didn't find a populated occurrence, lets quit.
	    		                break;
	    		            }
	    		            
    		            } //while (hasMoreTeams)
    		              		            
    		        } //while (replyDataRecordSet.hasNext()  
    		        
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
    		        messageSB = new StringBuffer(1000);
    		 
    		        /*
    		         * Accumulate ALL of the ERROR/WARNING/INFORMATIONAL messages and neatly format them into one String.
    		         * We don't really know what the mainframe will be sending so just collect them all for now.
    		         */
    		        while (replyErrorRecordSetIterator.hasNext()) {
    		            replyErrorRecord = (Record)replyErrorRecordSetIterator.next();
    		            // JDL - 06/20/2008 - changed from ERROR to Warning since we're logging it as a 
    		            // warning it below when we catch it again. Too many emails are being sent because
    		            // this had ERROR in the message.
    		            messageSB.append("Warning: ").append(replyErrorRecord.getField("ERROR-DESC").getValue()).append("  ");
    		            messageSB.append("(").append(replyErrorRecord.getField("ERROR-FIELD").getValue()).append(")<br>\n");
    		        }
    		        
    		        if (vistaModule.hasErrorMessages()) {
    		            /*
    		             * Our VistaModule returned ERROR (not just WARNING or INFORMATIONAL) messages.
    		             */
    		            throw new SMRCException(messageSB.toString());
    		        }
    	        
    		    } // if (replyErrorRecordSet != null)
    	    
 
    	        /*
    	         * If we get this far, we have only WARNING/INFORMATIONAL messages.
    	         * Return them in the AccountRegistrationResults.
    	         * This is done in the "bottom code" after the finally block.
    	         */
    		  //  SMRCConnectionPoolUtils.commitTransaction(DBConn);
    	    }catch (SMRCException smrce) {
                /*
                 * This will be thrown if users dont have a Vista Profile created
                 */
            	SMRCLogger.warn("SMRCDataProvisioningService.getVistaSecurity\n" + theLoggedInUser.toString(), smrce);
            }catch (TransactionException te) {
                /*
                 * If the VistaMessageService throws a TransactionException getting security
                 * records we still want the user to enter the system, but log the error
                 * Dont throw the exception up
                 */
            	SMRCLogger.error("SMRCDataProvisioningService.getVistaSecurity - Vista TransactionException\n" + theLoggedInUser.toString(), te);
            }catch(Exception e) {
            	SMRCLogger.error("SMRCDataProvisioningService.getVistaSecurity\n" + theLoggedInUser.toString(), e);
            	throw e;
            }
            finally {
              //  SMRCConnectionPoolUtils.close(DBConn);
            }
            
            //SMRCLogger.debug("returning new vistasecurity");
            return new VistaSecurity(universalAccess, vistaSecurityRecords);
            
        } //method
    
} //class
