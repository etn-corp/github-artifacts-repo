package com.eaton.electrical.smrc.exception;

import com.eaton.electrical.smrc.service.SMRCLogger;


/**
 * @author E0062708
 *
 */
public class SMRCException extends Exception {
	
	private static final long serialVersionUID = 100;

    /** Constructs an Exception without a message. */
    public SMRCException() {
        super();
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param aMessage The message associated with the exception.
     */
    public SMRCException(String aMessage) {
        super(aMessage);
        
        outputToLog(aMessage);
    }

    /**
     * Constructs an Exception with a detailed message and a cause.
     * @param message The message associated with the exception.
     * @param cause The cause of the exception.
     */
    public SMRCException(String message, Throwable cause) {
        super(message, cause);
        
        String lineFeedTab = System.getProperty("line.separator") + "\t";

        // outputToLog(this.toString() + EPException.stackTraceToString(this) + lineFeedTab + lineFeedTab + "Cause : " + cause.toString() + " : " + cause.getMessage() + EPException.stackTraceToString(cause));
        
        StringBuffer outString = new StringBuffer();
        
        outString.append(this.toString() + stackTraceToString(this));
        
        Throwable nestedCause = cause;

        while (nestedCause != null) {
            
         outString.append(lineFeedTab + lineFeedTab + "Cause : " + cause.toString() + " : " + cause.getMessage() + stackTraceToString(cause));            
            
         nestedCause = nestedCause.getCause();
         
        } // end while cause != null
        
        outputToLog(outString.toString());
    }
    
    public static String stackTraceToString(Throwable cause) {
        
        StringBuffer outString = new StringBuffer();
        StackTraceElement[] elements = cause.getStackTrace();
        String lineFeedTab = System.getProperty("line.separator") + "\t\t";
        
        for(int i = 0; i < elements.length; i ++) {
            
            outString.append(lineFeedTab + ((StackTraceElement)elements[i]).toString());
            
        }  // end iterate through stack trace element
        
        return outString.toString();
        
    } // end stackTraceToString
    
    private void outputToLog(String message) {
         
    	SMRCLogger.error(message);
        
    } // end outputToLog
}