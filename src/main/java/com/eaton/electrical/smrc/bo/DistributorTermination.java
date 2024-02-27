package com.eaton.electrical.smrc.bo;

import java.util.*;

/**
 * @author E0062708
 *
 */
public class DistributorTermination implements java.io.Serializable {
	private String vcn = null;	
	private int distributorId = 0;
	private Date proposedDate = null;
	private Date effectiveDate = null;
	private Date requestDate = null;
	private int reasonTypeId = -1;
	private String explanation = null;
	private String actionNotes = null;
	private double estInventoryStdDE = 0;
	private double estInventoryPDCD = 0;
	private double estInventoryStdControl = 0;
	private double potReturnStdDE = 0;
	private double potReturnPDCD = 0;
	private double potReturnStdControl = 0;

	
	private String additionalComments = null;

	private static final long serialVersionUID = 100;

	public DistributorTermination(){
		vcn = "";
		explanation = "";
		actionNotes = "";
		additionalComments = "";	

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
	public String getVcn() {
		return vcn;
	}
	public void setVcn(String vcn) {
		this.vcn = vcn;
	}
	public String getActionNotes() {
		return actionNotes;
	}
	public void setActionNotes(String actionNotes) {
		this.actionNotes = actionNotes;
	}
	public String getAdditionalComments() {
		return additionalComments;
	}
	public void setAdditionalComments(String additionalComments) {
		this.additionalComments = additionalComments;
	}

	public int getDistributorId() {
		return distributorId;
	}
	public void setDistributorId(int distributorId) {
		this.distributorId = distributorId;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public double getEstInventoryPDCD() {
		return estInventoryPDCD;
	}
	public void setEstInventoryPDCD(double estInventoryPDCD) {
		this.estInventoryPDCD = estInventoryPDCD;
	}
	public double getEstInventoryStdControl() {
		return estInventoryStdControl;
	}
	public void setEstInventoryStdControl(double estInventoryStdControl) {
		this.estInventoryStdControl = estInventoryStdControl;
	}
	public double getEstInventoryStdDE() {
		return estInventoryStdDE;
	}
	public void setEstInventoryStdDE(double estInventoryStdDE) {
		this.estInventoryStdDE = estInventoryStdDE;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	public double getPotReturnPDCD() {
		return potReturnPDCD;
	}
	public void setPotReturnPDCD(double potReturnPDCD) {
		this.potReturnPDCD = potReturnPDCD;
	}
	public double getPotReturnStdControl() {
		return potReturnStdControl;
	}
	public void setPotReturnStdControl(double potReturnStdControl) {
		this.potReturnStdControl = potReturnStdControl;
	}
	public double getPotReturnStdDE() {
		return potReturnStdDE;
	}
	public void setPotReturnStdDE(double potReturnStdDE) {
		this.potReturnStdDE = potReturnStdDE;
	}
	public int getReasonTypeId() {
		return reasonTypeId;
	}
	public void setReasonTypeId(int reasonTypeId) {
		this.reasonTypeId = reasonTypeId;
	}
	public Date getProposedDate() {
		return proposedDate;
	}
	public void setProposedDate(Date proposedDate) {
		this.proposedDate = proposedDate;
	}
	
	public String getProposedDateDate() {
		if(proposedDate==null || getProposedDate().toString().equals("0002-11-30")){
			return "";
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime (proposedDate);
	    return ""+calendar.get(Calendar.DATE);

	}
	public String getProposedDateMonth() {
		if(proposedDate==null || getProposedDate().toString().equals("0002-11-30")){
			return "";
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime (proposedDate);
	    return ""+(calendar.get(Calendar.MONTH)+1);
		
	}
	public String getProposedDateYear() {
		if(proposedDate==null || getProposedDate().toString().equals("0002-11-30")){
			return "";
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime (proposedDate);
	    return ""+calendar.get(Calendar.YEAR);

	}
	public String getEffectiveDateDate() {
		if(effectiveDate==null || getEffectiveDate().toString().equals("0002-11-30")){
			return "";
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime (effectiveDate);
	    return ""+calendar.get(Calendar.DATE);

	}
	public String getEffectiveDateMonth() {
		if(effectiveDate==null || getEffectiveDate().toString().equals("0002-11-30")){
			return "";
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime (effectiveDate);
	    return ""+(calendar.get(Calendar.MONTH)+1);

	}
	public String getEffectiveDateYear() {
		if(effectiveDate==null || getEffectiveDate().toString().equals("0002-11-30")){
			return "";
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime (effectiveDate);
	    return ""+calendar.get(Calendar.YEAR);

	}

	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
}
