package com.eaton.electrical.smrc.bo;

import java.sql.*;

/**
 * @author E0062708
 *
 */
public class DistributorCreditAuthorization implements java.io.Serializable {
	private Date requestDate = null;
	private String vcn =  null;
	private String salesEngineer = null;
	private String districtManager = null;
	private String distributionManager = null;
	private String creditManager = null;
	private String requestedCreditLine = null;
	private String approvedCreditLine = null;
	private Date distMgrUpdateDate = null;
	private String distMgrApproved = null;
	private Date distrMgrUpdateDate = null;
	private String distrMgrApproved = null;
	private Date creditMgrUpdateDate = null;
	private String creditMgrApproved = null;
	private String financialInformation = null;
	private String tradeReference1 = null;
	private String tradeReference2 = null;
	private String tradeReference3 = null;
	private String tradeReference1Addr = null;
	private String tradeReference2Addr = null;
	private String tradeReference3Addr = null;
	private String tradeReference1Phone = null;
	private String tradeReference2Phone = null;
	private String tradeReference3Phone = null;
	private String tradeReference1Fax = null;
	private String tradeReference2Fax = null;
	private String tradeReference3Fax = null;
	private String additionalSalesInfo = null;
	private String creditDeptComments = null;
	
	private static final long serialVersionUID = 100;

	public DistributorCreditAuthorization(){
		//requestDate = new Date();
		vcn =  "";
		salesEngineer = "";
		districtManager = "";
		distributionManager = "";
		creditManager = "";
		requestedCreditLine = "";
		approvedCreditLine = "";
		//distMgrUpdateDate = new java.sql.Date();
		distMgrApproved = "";
		//distrMgrUpdateDate = "";
		distrMgrApproved = "";
		//creditMgrUpdateDate = "";
		creditMgrApproved = "";
		financialInformation = "";
		tradeReference1 = "";
		tradeReference2 = "";
		tradeReference3 = "";
		tradeReference1Addr = "";
		tradeReference2Addr = "";
		tradeReference3Addr = "";
		tradeReference1Phone = "";
		tradeReference2Phone = "";
		tradeReference3Phone = "";
		tradeReference1Fax = "";
		tradeReference2Fax = "";
		tradeReference3Fax = "";
		additionalSalesInfo = "";
		creditDeptComments = "";		
		
	}
	
	public String getAdditionalSalesInfo() {
		return additionalSalesInfo;
	}
	public void setAdditionalSalesInfo(String additionalSalesInfo) {
		this.additionalSalesInfo = additionalSalesInfo;
	}
	public String getApprovedCreditLine() {
		return approvedCreditLine;
	}
	public void setApprovedCreditLine(String approvedCreditLine) {
		this.approvedCreditLine = approvedCreditLine;
	}
	public String getCreditDeptComments() {
		return creditDeptComments;
	}
	public void setCreditDeptComments(String creditDeptComments) {
		this.creditDeptComments = creditDeptComments;
	}
	public String getCreditManager() {
		return creditManager;
	}
	public void setCreditManager(String creditManager) {
		this.creditManager = creditManager;
	}
	public String getCreditMgrApproved() {
		return creditMgrApproved;
	}
	public void setCreditMgrApproved(String creditMgrApproved) {
		this.creditMgrApproved = creditMgrApproved;
	}
	public Date getCreditMgrUpdateDate() {
		return creditMgrUpdateDate;
	}
	public void setCreditMgrUpdateDate(Date creditMgrUpdateDate) {
		this.creditMgrUpdateDate = creditMgrUpdateDate;
	}
	public String getDistMgrApproved() {
		return distMgrApproved;
	}
	public void setDistMgrApproved(String distMgrApproved) {
		this.distMgrApproved = distMgrApproved;
	}
	public Date getDistMgrUpdateDate() {
		return distMgrUpdateDate;
	}
	public void setDistMgrUpdateDate(Date distMgrUpdateDate) {
		this.distMgrUpdateDate = distMgrUpdateDate;
	}
	public String getDistributionManager() {
		return distributionManager;
	}
	public void setDistributionManager(String distributionManager) {
		this.distributionManager = distributionManager;
	}
	public String getDistrictManager() {
		return districtManager;
	}
	public void setDistrictManager(String districtManager) {
		this.districtManager = districtManager;
	}
	public String getDistrMgrApproved() {
		return distrMgrApproved;
	}
	public void setDistrMgrApproved(String distrMgrApproved) {
		this.distrMgrApproved = distrMgrApproved;
	}
	public Date getDistrMgrUpdateDate() {
		return distrMgrUpdateDate;
	}
	public void setDistrMgrUpdateDate(Date distrMgrUpdateDate) {
		this.distrMgrUpdateDate = distrMgrUpdateDate;
	}
	public String getFinancialInformation() {
		return financialInformation;
	}
	public void setFinancialInformation(String financialInformation) {
		this.financialInformation = financialInformation;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public String getRequestedCreditLine() {
		return requestedCreditLine;
	}
	public void setRequestedCreditLine(String requestedCreditLine) {
		this.requestedCreditLine = requestedCreditLine;
	}
	public String getSalesEngineer() {
		return salesEngineer;
	}
	public void setSalesEngineer(String salesEngineer) {
		this.salesEngineer = salesEngineer;
	}
	public String getTradeReference1() {
		return tradeReference1;
	}
	public void setTradeReference1(String tradeReference1) {
		this.tradeReference1 = tradeReference1;
	}
	public String getTradeReference1Addr() {
		return tradeReference1Addr;
	}
	public void setTradeReference1Addr(String tradeReference1Addr) {
		this.tradeReference1Addr = tradeReference1Addr;
	}
	public String getTradeReference1Fax() {
		return tradeReference1Fax;
	}
	public void setTradeReference1Fax(String tradeReference1Fax) {
		this.tradeReference1Fax = tradeReference1Fax;
	}
	public String getTradeReference1Phone() {
		return tradeReference1Phone;
	}
	public void setTradeReference1Phone(String tradeReference1Phone) {
		this.tradeReference1Phone = tradeReference1Phone;
	}
	public String getTradeReference2() {
		return tradeReference2;
	}
	public void setTradeReference2(String tradeReference2) {
		this.tradeReference2 = tradeReference2;
	}
	public String getTradeReference2Addr() {
		return tradeReference2Addr;
	}
	public void setTradeReference2Addr(String tradeReference2Addr) {
		this.tradeReference2Addr = tradeReference2Addr;
	}
	public String getTradeReference2Fax() {
		return tradeReference2Fax;
	}
	public void setTradeReference2Fax(String tradeReference2Fax) {
		this.tradeReference2Fax = tradeReference2Fax;
	}
	public String getTradeReference2Phone() {
		return tradeReference2Phone;
	}
	public void setTradeReference2Phone(String tradeReference2Phone) {
		this.tradeReference2Phone = tradeReference2Phone;
	}
	public String getTradeReference3() {
		return tradeReference3;
	}
	public void setTradeReference3(String tradeReference3) {
		this.tradeReference3 = tradeReference3;
	}
	public String getTradeReference3Addr() {
		return tradeReference3Addr;
	}
	public void setTradeReference3Addr(String tradeReference3Addr) {
		this.tradeReference3Addr = tradeReference3Addr;
	}
	public String getTradeReference3Fax() {
		return tradeReference3Fax;
	}
	public void setTradeReference3Fax(String tradeReference3Fax) {
		this.tradeReference3Fax = tradeReference3Fax;
	}
	public String getTradeReference3Phone() {
		return tradeReference3Phone;
	}
	public void setTradeReference3Phone(String tradeReference3Phone) {
		this.tradeReference3Phone = tradeReference3Phone;
	}
	public String getVcn() {
		return vcn;
	}
	public void setVcn(String vcn) {
		this.vcn = vcn;
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

		returnString += "\tadditionalSalesInfo = " + this.getAdditionalSalesInfo() + "\n";
		returnString += "\tapprovedCreditLine = " + this.getApprovedCreditLine() + "\n";
		returnString += "\tcreditDeptComments = " + this.getCreditDeptComments() + "\n";
		returnString += "\tcreditManager = " + this.getCreditManager() + "\n";
		returnString += "\tcreditMgrApproved = " + this.getCreditMgrApproved() + "\n";
		returnString += "\tcreditMgrUpdateDate = " + this.getCreditMgrUpdateDate() + "\n";
		returnString += "\tdistMgrApproved = " + this.getDistMgrApproved() + "\n";
		returnString += "\tdistMgrUpdateDate = " + this.getDistMgrUpdateDate() + "\n";
		returnString += "\tdistributionManager = " + this.getDistributionManager() + "\n";
		returnString += "\tdistrictManager = " + this.getDistrictManager() + "\n";
		returnString += "\tdistrMgrApproved = " + this.getDistrMgrApproved() + "\n";
		returnString += "\tdistrMgrUpdateDate = " + this.getDistrMgrUpdateDate() + "\n";
		returnString += "\tfinancialInformation = " + this.getFinancialInformation() + "\n";
		returnString += "\trequestDate = " + this.getRequestDate() + "\n";
		returnString += "\trequestedCreditLine = " + this.getRequestedCreditLine() + "\n";
		returnString += "\tsalesEngineer = " + this.getSalesEngineer() + "\n";
		returnString += "\ttradeReference1 = " + this.getTradeReference1() + "\n";
		returnString += "\ttradeReference1Addr = " + this.getTradeReference1Addr() + "\n";
		returnString += "\ttradeReference1Fax = " + this.getTradeReference1Fax() + "\n";
		returnString += "\ttradeReference1Phone = " + this.getTradeReference1Phone() + "\n";
		returnString += "\ttradeReference2 = " + this.getTradeReference2() + "\n";
		returnString += "\ttradeReference2Addr = " + this.getTradeReference2Addr() + "\n";
		returnString += "\ttradeReference2Fax = " + this.getTradeReference2Fax() + "\n";
		returnString += "\ttradeReference2Phone = " + this.getTradeReference2Phone() + "\n";
		returnString += "\ttradeReference3 = " + this.getTradeReference3() + "\n";
		returnString += "\ttradeReference3Addr = " + this.getTradeReference3Addr() + "\n";
		returnString += "\ttradeReference3Fax = " + this.getTradeReference3Fax() + "\n";
		returnString += "\ttradeReference3Phone = " + this.getTradeReference3Phone() + "\n";
		returnString += "\tvcn = " + this.getVcn() + "\n";

		return returnString;
	}
}
