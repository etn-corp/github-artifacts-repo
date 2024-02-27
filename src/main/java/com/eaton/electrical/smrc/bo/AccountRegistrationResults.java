/*
 * Created on Oct 13, 2004
 *
 */
package com.eaton.electrical.smrc.bo;

/**
 * @author e0062722
 *
 */
public class AccountRegistrationResults implements java.io.Serializable {
    
	private static final long serialVersionUID = 100;

	//if registration fails miserably, then you should not get an instance of this class at all.
    //please DO NOT instantiate this class yourself!  Let SMRCVMSConnector.registerAccount() do it for you.
    
    //if this is non-null, then the registration was immediate.
    private String registeredAccountVCN = null;
    
    //if this is non-null, then it should be presented to the user.  Registration was immediate or pending.
    private String vistaReferenceNumber = null;
    
    //if this is non-null, then it should be presented to the user for acknowledgement.  Registration was immediate or pending.
    private String confirmationMessage = null;
    
    //if this is non-null, then it should be presented to the user for acknowledgement.
    //**Display only if registration is immediate.**
    private String districtRegisteredTo = null;
    
    /**
     * I construct an AccountRegistrationResults instance.
     * Nobody other than SMRCVMSConnector should construct me though.
     * 
     * @param aVCN
     * @param aVistaReferenceNumber
     * @param aConfirmationMessage
     */
    public AccountRegistrationResults (String aVCN, String aVistaReferenceNumber, String aDistrictRegisteredTo, String aConfirmationMessage ){
        setRegisteredAccountVCN(aVCN);
        setVistaReferenceNumber(aVistaReferenceNumber);
        setDistrictRegisteredTo(aDistrictRegisteredTo);
        setConfirmationMessage(aConfirmationMessage);
    }
    
    /**
     * Don't try and construct me yourself.
     * Nobody other than SMRCVMSConnector should construct me.
     */
    public AccountRegistrationResults() throws Exception {
        //don't use this constructor.
        
        throw (new Exception("Don't construct me like this."));
    }
    
    /**
     * @return Returns the vistaReferenceNumber.
     */
    public String getVistaReferenceNumber() {
        return vistaReferenceNumber;
    }
    
    /**
     * @return Returns the registeredAccountVCN.
     */
    public String getRegisteredAccountVCN() {
        return registeredAccountVCN;
    }

    /**
     * @return Returns the districtRegisteredTo.
     */
    public String getDistrictRegisteredTo() {
        return districtRegisteredTo;
    }

    /**
     * @return Returns the confirmationMessage.
     */
    public String getConfirmationMessage() {
        return confirmationMessage;
    }
    
    /**
     * @return Returns whether registration of the account was immediate.
     */
    public boolean isRegistrationImmediate() {
        return (getRegisteredAccountVCN() != null && getRegisteredAccountVCN().trim().length() == 6);
    }
    
    /**
     * @return Returns whether registration of the account was pending.
     */
    public boolean isRegistrationPending() {
        return (!isRegistrationImmediate());
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer(100);
        sb.append("AccountRegistrationResults.toString() ...");
        sb.append("\n   is registration immediate? = ").append(this.isRegistrationImmediate());
        sb.append("\n     is registration pending? = ").append(this.isRegistrationPending());
        sb.append("\n         registeredAccountVCN = ").append(getRegisteredAccountVCN());
        sb.append("\n         vistaReferenceNumber = ").append(getVistaReferenceNumber());
        sb.append("\n         districtRegisteredTo = ").append(getDistrictRegisteredTo());
        sb.append("\n          confirmationMessage = ").append(getConfirmationMessage());
        
        return sb.toString();
    }
    
    /**
     * @param vistaReferenceNumber The vistaReferenceNumber to set.
     */
    private void setVistaReferenceNumber(
            String vistaReferenceNumber) {
        this.vistaReferenceNumber = vistaReferenceNumber;
    }
    
    /**
     * @param registeredAccountVCN The registeredAccountVCN to set.
     */
    private void setRegisteredAccountVCN(String registeredAccountVCN) {
        this.registeredAccountVCN = registeredAccountVCN;
    }
    
    /**
     * @param istrictRegisteredTo The istrictRegisteredTo to set.
     */
    private void setDistrictRegisteredTo(String districtRegisteredTo) {
        this.districtRegisteredTo = districtRegisteredTo;
    }
    
    /**
     * @param confirmationMessage The confirmationMessage to set.
     */
    private void setConfirmationMessage(String confirmationMessage) {
        this.confirmationMessage = confirmationMessage;
    }
    


} //class
