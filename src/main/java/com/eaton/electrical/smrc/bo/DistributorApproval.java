package com.eaton.electrical.smrc.bo;

import java.util.*;

/**
 * @author E0062708
 *
 */
public class DistributorApproval implements java.io.Serializable {
	
	private String vcn = null;
	private int distributorId = 0;
	private String commitmentLevel = null;
	private Date exclusiveEatonCommitment = null;
	private double projEatonSalesYr1 = 0;
	private double projEatonSalesYr3 = 0;
	private ArrayList summaryCompetitors = null;
	private ArrayList impactedDistributors = null;
	private String districtStrategy = null;
	private double netAreaImpactYr1 = 0;
	private double netAreaImpactYr3 = 0;
	private ArrayList modules = null;
	private String districtManager = null;
	private String zoneManager = null;
	private String channelMarketingManager = null;
	private String creditManager = null;
	private Date districtManagerApprDate = null;
	private Date zoneManagerApprDate = null;
	private Date channelMarketingManagerApprDate = null;
	private Date creditManagerApprDate = null;
	
	
	private static final long serialVersionUID = 100;

	public DistributorApproval(){
		vcn="";
		commitmentLevel = "";
		summaryCompetitors = new ArrayList();
		impactedDistributors = new ArrayList();
		modules = new ArrayList();
		districtManager = "";
		zoneManager = "";
		channelMarketingManager = "";
		creditManager = "";
		exclusiveEatonCommitment = null;
		
	}
	
	public String getChannelMarketingManager() {
		return channelMarketingManager;
	}
	public void setChannelMarketingManager(String channelMarketingManager) {
		this.channelMarketingManager = channelMarketingManager;
	}
	public Date getChannelMarketingManagerApprDate() {
		return channelMarketingManagerApprDate;
	}
	public void setChannelMarketingManagerApprDate(
			Date channelMarketingManagerApprDate) {
		this.channelMarketingManagerApprDate = channelMarketingManagerApprDate;
	}
	public String getCommitmentLevel() {
		return commitmentLevel;
	}
	public void setCommitmentLevel(String commitmentLevel) {
		this.commitmentLevel = commitmentLevel;
	}
	public ArrayList getSummaryCompetitors() {
		return summaryCompetitors;
	}
	public void setSummaryCompetitors(ArrayList summaryCompetitors) {
		this.summaryCompetitors = summaryCompetitors;
	}
	public void addSummaryCompetitor(SummaryCompetitor summaryCompetitor) {
		summaryCompetitors.add(summaryCompetitor);
	}
	public void clearSummaryCompetitors() {
		summaryCompetitors.clear();
	}
	public String getCreditManager() {
		return creditManager;
	}
	public void setCreditManager(String creditManager) {
		this.creditManager = creditManager;
	}
	public Date getCreditManagerApprDate() {
		return creditManagerApprDate;
	}
	public void setCreditManagerApprDate(Date creditManagerApprDate) {
		this.creditManagerApprDate = creditManagerApprDate;
	}
	public int getDistributorId() {
		return distributorId;
	}
	public void setDistributorId(int distributorId) {
		this.distributorId = distributorId;
	}
	public String getDistrictManager() {
		return districtManager;
	}
	public void setDistrictManager(String districtManager) {
		this.districtManager = districtManager;
	}
	public Date getDistrictManagerApprDate() {
		return districtManagerApprDate;
	}
	public void setDistrictManagerApprDate(Date districtManagerApprDate) {
		this.districtManagerApprDate = districtManagerApprDate;
	}
	public Date getExclusiveEatonCommitment() {
		if(exclusiveEatonCommitment!=null && exclusiveEatonCommitment.toString().equals("0002-11-30")){
			return null;
		}
		return exclusiveEatonCommitment;
	}
	public void setExclusiveEatonCommitment(Date exclusiveEatonCommitment) {
		this.exclusiveEatonCommitment = exclusiveEatonCommitment;
	}
	public ArrayList getImpactedDistributors() {
		return impactedDistributors;
	}
	public void setImpactedDistributors(ArrayList impactedDistributors) {
		this.impactedDistributors = impactedDistributors;
	}
	public void addImpactedDistributor(ImpactedDistributor impactedDist){
		this.impactedDistributors.add(impactedDist);
	}
	public void clearImpactedDistributors(){
		this.impactedDistributors.clear();
	}
	public ArrayList getModules() {
		return modules;
	}
	public void setModules(ArrayList modules) {
		this.modules = modules;
	}
	public String getDistrictStrategy() {
		return this.districtStrategy;
	}
	public void setDistrictStrategy(String districtStrategy){
		this.districtStrategy = districtStrategy;
	}
	public double getNetAreaImpactYr1() {
		return netAreaImpactYr1;
	}
	public void setNetAreaImpactYr1(double netAreaImpactYr1) {
		this.netAreaImpactYr1 = netAreaImpactYr1;
	}
	public double getNetAreaImpactYr3() {
		return netAreaImpactYr3;
	}
	public void setNetAreaImpactYr3(double netAreaImpactYr3) {
		this.netAreaImpactYr3 = netAreaImpactYr3;
	}
	public double getProjEatonSalesYr1() {
		return projEatonSalesYr1;
	}
	public void setProjEatonSalesYr1(double projEatonSalesYr1) {
		this.projEatonSalesYr1 = projEatonSalesYr1;
	}
	public double getProjEatonSalesYr3() {
		return projEatonSalesYr3;
	}
	public void setProjEatonSalesYr3(double projEatonSalesYr3) {
		this.projEatonSalesYr3 = projEatonSalesYr3;
	}
	public String getZoneManager() {
		return zoneManager;
	}
	public void setZoneManager(String zoneManager) {
		this.zoneManager = zoneManager;
	}
	public Date getZoneManagerApprDate() {
		return zoneManagerApprDate;
	}
	public void setZoneManagerApprDate(Date zoneManagerApprDate) {
		this.zoneManagerApprDate = zoneManagerApprDate;
	}
	public String getVcn() {
		return vcn;
	}
	public void setVcn(String vcn) {
		this.vcn = vcn;
	}

	public String getCommitmentDay() {
		if(getExclusiveEatonCommitment()==null){
			return "";
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime (exclusiveEatonCommitment);
	    return ""+calendar.get(Calendar.DATE);
		
	}
	public String getCommitmentMonth() {
		if(getExclusiveEatonCommitment()==null){
			return "";
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime (exclusiveEatonCommitment);
	    return ""+(calendar.get(Calendar.MONTH)+1);
	}
	public String getCommitmentYear() {
		if(getExclusiveEatonCommitment()==null){
			return "";
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime (exclusiveEatonCommitment);
	    return ""+calendar.get(Calendar.YEAR);
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

		returnString += "\tchannelMarketingManager = " + this.getChannelMarketingManager() + "\n";
		returnString += "\tchannelMarketingManagerApprDate = " + this.getChannelMarketingManagerApprDate() + "\n";
		returnString += "\tcommitmentLevel = " + this.getCommitmentLevel() + "\n";
		//returnString += "\tcompetitorSales = " + this.getCompetitorSales() + "\n";
		returnString += "\tcreditManager = " + this.getCreditManager() + "\n";
		returnString += "\tcreditManagerApprDate = " + this.getCreditManagerApprDate() + "\n";
		returnString += "\tdistributorId = " + this.getDistributorId() + "\n";
		returnString += "\tdistrictManager = " + this.getDistrictManager() + "\n";
		returnString += "\tdistrictManagerApprDate = " + this.getDistrictManagerApprDate() + "\n";
		returnString += "\texclusiveEatonCommitment = " + this.getExclusiveEatonCommitment() + "\n";
		returnString += "\timpactedDistributors = " + this.getImpactedDistributors() + "\n";
		returnString += "\tmodules = " + this.getModules() + "\n";
		returnString += "\tnetAreaImpactYr1 = " + this.getNetAreaImpactYr1() + "\n";
		returnString += "\tnetAreaImpactYr3 = " + this.getNetAreaImpactYr3() + "\n";
		returnString += "\tprojEatonSalesYr1 = " + this.getProjEatonSalesYr1() + "\n";
		returnString += "\tprojEatonSalesYr3 = " + this.getProjEatonSalesYr3() + "\n";
		returnString += "\tzoneManager = " + this.getZoneManager() + "\n";
		returnString += "\tzoneManagerApprDate = " + this.getZoneManagerApprDate() + "\n";
		returnString += "\tvcn = " + this.getVcn() + "\n";
		returnString += "\texclusiveEatonCommitment (Date) = " + this.getExclusiveEatonCommitment() + "\n";
		returnString += "\tcommitmentMonth = " + this.getCommitmentMonth() + "\n";
		returnString += "\tcommitmentYear = " + this.getCommitmentYear() + "\n";

		return returnString;
	}
}
