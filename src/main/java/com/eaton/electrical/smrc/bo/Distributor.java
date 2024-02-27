package com.eaton.electrical.smrc.bo;

import java.sql.*;
import java.util.*;

import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.service.*;

/**
 * @author E0062708
 *
 */
public class Distributor implements java.io.Serializable {
	private int id = 0;
	private String vcn = null;
	private String status = null;
	private String federalTaxId = null;
	private String dunnBradStreet = null;
	private int customerCategory = 0;
	private int applyingFor = 0;
	private String applyingForOtherNotes = null;
	private String previousName = null;
	private String previousVistaCustNumber = null;
	private int formOfOwnership = 0;
	private String formOfOwnershipNotes = null;
	private String parentCompany = null;
	private int PrimaryBusinessActivity = 0;
	private int LocationType = 0;
	private String chainName = null;
	private int numOfBranches = 0;
	private int numOfYrsAtLocation = 0;
	private String facilityArea = null;
	private String facilitiesOther = null;
	private String servicesOther = null;
	private ArrayList facilities = null;
	private ArrayList services = null;
	private ArrayList electricalLines = null;
	private int commitmentProgram = 0;
	private String commitmentReason = null;
	
	private ArrayList eCommerce = null;
	private int insideSalesConstrPersonnel = 0;
	private int insideSalesIndstrPersonnel = 0;
	private int insideSalesGenPersonnel = 0;
	private int outsideSalesConstrPersonnel = 0;
	private int outsideSalesIndstrPersonnel = 0;
	private int outsideSalesGenPersonnel = 0;
	private int electricalEngPersonnel = 0;
	private int specialistPersonnel = 0;
	private int counterSalesPersonnel = 0;
	private int whseDriversPersonnel = 0;
	private int managementPersonnel = 0;
	private int adminPersonnel = 0;
	private ArrayList segments = null;
	
	private int otherCustomerSegmentPercentage = 0;
	private String otherCustomerSegmentPercentageNote = null;
	
	//product sales data needs to go in here
	private ArrayList products = null;
	private ArrayList countySales = null;
	private ArrayList keyCustomers = null;
	private ArrayList suppliersProducts = null;
	private int prior3YrsTotalSales = 0;
	private int currentYrSalesEstimate = 0;
	private int priorYrActualSales = 0;
	private int prior2YrsActualSales = 0;
	private int approxInventory = 0;
	private int projectedEatonSalesYr1 = 0;
	private int projectedEatonSalesYr2 = 0;
	private int projectedVScompYr1 = 0;
	private int projectedVScompYr2 = 0;
	private String naedParticipation = null;
	private String tradeAssociations = null;
	private ArrayList buyingGroupAssn = null;
	private String buyingGroupOther = null;
	private String distributorNotes = null;
	private int productLoadingTypeId = 0;
	private ArrayList loadModules = null;
	
	

	
	private static final long serialVersionUID = 100;

	public Distributor(){
		vcn="";
		status = "";
		federalTaxId = "";
		applyingForOtherNotes = "";
		previousName = "";
		previousVistaCustNumber = "";
		formOfOwnershipNotes = "";
		parentCompany = "";
		chainName = "";
		facilityArea = "";
		facilitiesOther = "";
		servicesOther = "";
		facilities = new ArrayList();
		electricalLines = new ArrayList();
		services = new ArrayList();
		eCommerce = new ArrayList();
		segments = new ArrayList();
		loadModules = new ArrayList();
		
		Connection DBConn = null;
    	try{
    		DBConn = SMRCConnectionPoolUtils.getDatabaseConnection() ;   
    		segments = SegmentsDAO.getDistCustSegments(DBConn);
    		SMRCConnectionPoolUtils.commitTransaction(DBConn);
    	}catch(Exception e){
    	    SMRCLogger.error(this.getClass().getName() + ".Distributor(): ", e);
        }finally{
        	SMRCConnectionPoolUtils.close(DBConn);
        }
		
    	otherCustomerSegmentPercentageNote="";
		
		products = new ArrayList();
		countySales = new ArrayList();
		keyCustomers = new ArrayList();
		suppliersProducts = new ArrayList();
		naedParticipation = "";
		tradeAssociations = "";
		buyingGroupAssn = new ArrayList();
		buyingGroupOther = "";
		distributorNotes = "";

		
		
	}
	

	public int getAdminPersonnel() {
		return adminPersonnel;
	}
	public void setAdminPersonnel(int adminPersonnel) {
		this.adminPersonnel = adminPersonnel;
	}
	public int getApplyingFor() {
		return applyingFor;
	}
	public void setApplyingFor(int applyingFor) {
		this.applyingFor = applyingFor;
	}
	public String getApplyingForOtherNotes() {
		return applyingForOtherNotes;
	}
	public void setApplyingForOtherNotes(String applyingForOtherNotes) {
		this.applyingForOtherNotes = applyingForOtherNotes;
	}
	public int getApproxInventory() {
		return approxInventory;
	}
	public void setApproxInventory(int approxInventory) {
		this.approxInventory = approxInventory;
	}
	public ArrayList getBuyingGroupAssn() {
		return buyingGroupAssn;
	}
	public void setBuyingGroupAssn(ArrayList buyingGroupAssn) {
		this.buyingGroupAssn = buyingGroupAssn;
	}
	
	public void setBuyingGroupAssn(String[] buyingGroups) {
		if(buyingGroups!=null){
			for(int i=0; i<buyingGroups.length;i++){
				this.buyingGroupAssn.add(buyingGroups[i]);
			}
		}
	}	
	public void addBuyingGroupAssn(String buyingGroupAssn) {
		this.buyingGroupAssn.add(buyingGroupAssn);
	}	
	
	public void clearBuyingGroupAssn() {
		this.buyingGroupAssn.clear();
	}		
	
	public void clearElectricalLines() {
		this.electricalLines.clear();
	}
	
	public String getChainName() {
		return chainName;
	}
	public void setChainName(String chainName) {
		this.chainName = chainName;
	}
	public int getCounterSalesPersonnel() {
		return counterSalesPersonnel;
	}
	public void setCounterSalesPersonnel(int counterSalesPersonnel) {
		this.counterSalesPersonnel = counterSalesPersonnel;
	}
	public ArrayList getCountySales() {
		return countySales;
	}
	public void setCountySales(DistributorCountySalesRecord distRec) {
		if(!distRec.getCounty().equals("0")){
			this.countySales.add(distRec);
		}
	}
	public void setCountySales(ArrayList countySales) {
		this.countySales=countySales;
	}	
	public void clearCountySales() {
		this.countySales.clear();
	}
	public int getCurrentYrSalesEstimate() {
		return currentYrSalesEstimate;
	}
	public void setCurrentYrSalesEstimate(int currentYrSalesEstimate) {
		this.currentYrSalesEstimate = currentYrSalesEstimate;
	}
	public String getDistributorNotes() {
		return distributorNotes;
	}
	public void setDistributorNotes(String distributorNotes) {
		this.distributorNotes = distributorNotes;
	}
	public int getElectricalEngPersonnel() {
		return electricalEngPersonnel;
	}
	public void setElectricalEngPersonnel(int electricalEngPersonnel) {
		this.electricalEngPersonnel = electricalEngPersonnel;
	}
	public ArrayList getFacilities() {
		return facilities;
	}
	public void setFacilities(String[] facilities) {
		if(facilities!=null){
			for(int i=0; i<facilities.length;i++){
				this.facilities.add(facilities[i]);
			}
		}
	}
	public ArrayList getElectricalLines() {
		return electricalLines;
	}


	public void setElectricalLines(String[] electricalLines) {
		if(electricalLines != null) {
			for(int i=0; i < electricalLines.length; i++) {
				this.electricalLines.add(electricalLines[i]);
			}
		}
	}
	public void clearFacilities() {
		facilities.clear();
	}	
	public String getFacilityArea() {
		return facilityArea;
	}
	public void setFacilityArea(String facilityArea) {
		this.facilityArea = facilityArea;
	}
	public String getFederalTaxId() {
		return federalTaxId;
	}
	public void setFederalTaxId(String federalTaxId) {
		this.federalTaxId = federalTaxId;
	}
	public int getFormOfOwnership() {
		return formOfOwnership;
	}
	public void setFormOfOwnership(int formOfOwnership) {
		this.formOfOwnership = formOfOwnership;
	}
	public String getFormOfOwnershipNotes() {
		return formOfOwnershipNotes;
	}
	public void setFormOfOwnershipNotes(String formOfOwnershipNotes) {
		this.formOfOwnershipNotes = formOfOwnershipNotes;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getInsideSalesConstrPersonnel() {
		return insideSalesConstrPersonnel;
	}
	public void setInsideSalesConstrPersonnel(int insideSalesConstrPersonnel) {
		this.insideSalesConstrPersonnel = insideSalesConstrPersonnel;
	}
	public int getInsideSalesGenPersonnel() {
		return insideSalesGenPersonnel;
	}
	public void setInsideSalesGenPersonnel(int insideSalesGenPersonnel) {
		this.insideSalesGenPersonnel = insideSalesGenPersonnel;
	}
	public int getInsideSalesIndstrPersonnel() {
		return insideSalesIndstrPersonnel;
	}
	public void setInsideSalesIndstrPersonnel(int insideSalesIndstrPersonnel) {
		this.insideSalesIndstrPersonnel = insideSalesIndstrPersonnel;
	}
	public ArrayList getKeyCustomers() {
		return keyCustomers;
	}
	public void setKeyCustomers(ArrayList keyCustomers) {
		this.keyCustomers = keyCustomers;
	}
	public int getLocationType() {
		return LocationType;
	}
	public void setLocationType(int locationType) {
		LocationType = locationType;
	}
	public int getManagementPersonnel() {
		return managementPersonnel;
	}
	public void setManagementPersonnel(int managementPersonnel) {
		this.managementPersonnel = managementPersonnel;
	}
	public String getNaedParticipation() {
		return naedParticipation;
	}
	public void setNaedParticipation(String naedParticipation) {
		this.naedParticipation = naedParticipation;
	}
	public int getNumOfBranches() {
		return numOfBranches;
	}
	public void setNumOfBranches(int numOfBranches) {
		this.numOfBranches = numOfBranches;
	}
	public int getNumOfYrsAtLocation() {
		return numOfYrsAtLocation;
	}
	public void setNumOfYrsAtLocation(int numOfYrsAtLocation) {
		this.numOfYrsAtLocation = numOfYrsAtLocation;
	}
	public int getOutsideSalesConstrPersonnel() {
		return outsideSalesConstrPersonnel;
	}
	public void setOutsideSalesConstrPersonnel(int outsideSalesConstrPersonnel) {
		this.outsideSalesConstrPersonnel = outsideSalesConstrPersonnel;
	}
	public int getOutsideSalesGenPersonnel() {
		return outsideSalesGenPersonnel;
	}
	public void setOutsideSalesGenPersonnel(int outsideSalesGenPersonnel) {
		this.outsideSalesGenPersonnel = outsideSalesGenPersonnel;
	}
	public int getOutsideSalesIndstrPersonnel() {
		return outsideSalesIndstrPersonnel;
	}
	public void setOutsideSalesIndstrPersonnel(int outsideSalesIndstrPersonnel) {
		this.outsideSalesIndstrPersonnel = outsideSalesIndstrPersonnel;
	}
	public String getParentCompany() {
		return parentCompany;
	}
	public void setParentCompany(String parentCompany) {
		this.parentCompany = parentCompany;
	}
	public String getPreviousName() {
		return previousName;
	}
	public void setPreviousName(String previousName) {
		this.previousName = previousName;
	}
	public String getPreviousVistaCustNumber() {
		if(previousVistaCustNumber==null){
			return "";
		}
		return previousVistaCustNumber;

	}
	public void setPreviousVistaCustNumber(String previousVistaCustNumber) {
		this.previousVistaCustNumber = previousVistaCustNumber;
	}
	public int getPrimaryBusinessActivity() {
		return PrimaryBusinessActivity;
	}
	public void setPrimaryBusinessActivity(int primaryBusinessActivity) {
		PrimaryBusinessActivity = primaryBusinessActivity;
	}
	public int getPrior2YrsActualSales() {
		return prior2YrsActualSales;
	}
	public void setPrior2YrsActualSales(int prior2YrsActualSales) {
		this.prior2YrsActualSales = prior2YrsActualSales;
	}
	public int getPrior3YrsTotalSales() {
		return prior3YrsTotalSales;
	}
	public void setPrior3YrsTotalSales(int prior3YrsTotalSales) {
		this.prior3YrsTotalSales = prior3YrsTotalSales;
	}
	public int getPriorYrActualSales() {
		return priorYrActualSales;
	}
	public void setPriorYrActualSales(int priorYrActualSales) {
		this.priorYrActualSales = priorYrActualSales;
	}
	public int getProductLoadingTypeId() {
		return productLoadingTypeId;
	}
	public void setProductLoadingTypeId(int productLoadingTypeId) {
		this.productLoadingTypeId = productLoadingTypeId;
	}
	public int getProjectedEatonSalesYr1() {
		return projectedEatonSalesYr1;
	}
	public void setProjectedEatonSalesYr1(int projectedEatonSalesYr1) {
		this.projectedEatonSalesYr1 = projectedEatonSalesYr1;
	}
	public int getProjectedEatonSalesYr2() {
		return projectedEatonSalesYr2;
	}
	public void setProjectedEatonSalesYr2(int projectedEatonSalesYr2) {
		this.projectedEatonSalesYr2 = projectedEatonSalesYr2;
	}
	public ArrayList getSegments() {
		return segments;
	}
	public void setSegment(int segmentId, int percentage,String name) {
		Segment rec = new Segment();
		rec.setSegmentId(segmentId);
		rec.setName(name);
		rec.setSalesPercentage(percentage);
		
		this.segments.add(rec);
	}
	public void setSegments(ArrayList segments) {
		this.segments=segments;
	}	
	public void clearSegments() {
		this.segments.clear();
	}		
	public ArrayList getServices() {
		return services;
	}
	public void setServices(String[] services) {
		if(services!=null){
			for(int i=0; i<services.length;i++){
				this.services.add(services[i]);
			}
		}
	}
	public void clearServices() {
		services.clear();
	}		
	
	public int getSpecialistPersonnel() {
		return specialistPersonnel;
	}
	public void setSpecialistPersonnel(int specialistPersonnel) {
		this.specialistPersonnel = specialistPersonnel;
	}
	public ArrayList getSuppliersProducts() {
		return suppliersProducts;
	}
	public void setSuppliersProducts(ArrayList suppliersProducts) {
		this.suppliersProducts = suppliersProducts;
	}
	public void addSuppliersProducts(DistributorSupplierProduct supplierProduct) {
		this.suppliersProducts.add(supplierProduct);
	}	
	public void clearSuppliersProducts() {
		this.suppliersProducts.clear();
	}	
	public String getTradeAssociations() {
		return tradeAssociations;
	}
	public void setTradeAssociations(String tradeAssociations) {
		this.tradeAssociations = tradeAssociations;
	}
	public int getWhseDriversPersonnel() {
		return whseDriversPersonnel;
	}
	public void setWhseDriversPersonnel(int whseDriversPersonnel) {
		this.whseDriversPersonnel = whseDriversPersonnel;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getVcn() {
		return vcn;
	}
	public void setVcn(String vcn) {
		this.vcn = vcn;
	}
	public ArrayList getECommerce() {
		return eCommerce;
	}
	public void setECommerce(String[] commerce) {
		if(commerce!=null){
			for(int i=0; i<commerce.length;i++){
				this.eCommerce.add(commerce[i]);
			}
		}
	}
	public void clearECommerce() {
		eCommerce.clear();
	}		
	public String getFacilitiesOther() {
		return facilitiesOther;
	}
	public void setFacilitiesOther(String facilitiesOther) {
		this.facilitiesOther = facilitiesOther;
	}
	public String getServicesOther() {
		return servicesOther;
	}
	public void setServicesOther(String servicesOther) {
		this.servicesOther = servicesOther;
	}
	public ArrayList getProducts() {
		return products;
	}
	public void setProducts(Product prod) {
		this.products.add(prod);
	}
	public void setProducts(ArrayList products) {
		this.products = products;
	}
	public void clearProducts() {
		this.products.clear();
	}	
	public String getBuyingGroupOther() {
		return buyingGroupOther;
	}
	public void setBuyingGroupOther(String buyingGroupOther) {
		this.buyingGroupOther = buyingGroupOther;
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

		returnString += "\tadminPersonnel = " + this.getAdminPersonnel() + "\n";
		returnString += "\tapplyingFor = " + this.getApplyingFor() + "\n";
		returnString += "\tapplyingForOtherNotes = " + this.getApplyingForOtherNotes() + "\n";
		returnString += "\tapproxInventory = " + this.getApproxInventory() + "\n";
		returnString += "\tbuyingGroupAssn = " + this.getBuyingGroupAssn() + "\n";
		returnString += "\tchainName = " + this.getChainName() + "\n";
		returnString += "\tcounterSalesPersonnel = " + this.getCounterSalesPersonnel() + "\n";
		returnString += "\tcountySales = " + this.getCountySales() + "\n";
		returnString += "\tcurrentYrSalesEstimate = " + this.getCurrentYrSalesEstimate() + "\n";
		returnString += "\tdistributorNotes = " + this.getDistributorNotes() + "\n";
		returnString += "\telectricalEngPersonnel = " + this.getElectricalEngPersonnel() + "\n";
		returnString += "\tfacilities = " + this.getFacilities() + "\n";
		returnString += "\tfacilityArea = " + this.getFacilityArea() + "\n";
		returnString += "\tfederalTaxId = " + this.getFederalTaxId() + "\n";
		returnString += "\tformOfOwnership = " + this.getFormOfOwnership() + "\n";
		returnString += "\tformOfOwnershipNotes = " + this.getFormOfOwnershipNotes() + "\n";
		returnString += "\tid = " + this.getId() + "\n";
		returnString += "\tinsideSalesConstrPersonnel = " + this.getInsideSalesConstrPersonnel() + "\n";
		returnString += "\tinsideSalesGenPersonnel = " + this.getInsideSalesGenPersonnel() + "\n";
		returnString += "\tinsideSalesIndstrPersonnel = " + this.getInsideSalesIndstrPersonnel() + "\n";
		returnString += "\tkeyCustomers = " + this.getKeyCustomers() + "\n";
		returnString += "\tlocationType = " + this.getLocationType() + "\n";
		returnString += "\tmanagementPersonnel = " + this.getManagementPersonnel() + "\n";
		returnString += "\tnaedParticipation = " + this.getNaedParticipation() + "\n";
		returnString += "\tnumOfBranches = " + this.getNumOfBranches() + "\n";
		returnString += "\tnumOfYrsAtLocation = " + this.getNumOfYrsAtLocation() + "\n";
		returnString += "\toutsideSalesConstrPersonnel = " + this.getOutsideSalesConstrPersonnel() + "\n";
		returnString += "\toutsideSalesGenPersonnel = " + this.getOutsideSalesGenPersonnel() + "\n";
		returnString += "\toutsideSalesIndstrPersonnel = " + this.getOutsideSalesIndstrPersonnel() + "\n";
		returnString += "\tparentCompany = " + this.getParentCompany() + "\n";
		returnString += "\tpreviousName = " + this.getPreviousName() + "\n";
		returnString += "\tpreviousVistaCustNumber = " + this.getPreviousVistaCustNumber() + "\n";
		returnString += "\tprimaryBusinessActivity = " + this.getPrimaryBusinessActivity() + "\n";
		returnString += "\tprior2YrsActualSales = " + this.getPrior2YrsActualSales() + "\n";
		returnString += "\tprior3YrsTotalSales = " + this.getPrior3YrsTotalSales() + "\n";
		returnString += "\tpriorYrActualSales = " + this.getPriorYrActualSales() + "\n";
		returnString += "\tproductLoadingTypeId = " + this.getProductLoadingTypeId() + "\n";
		returnString += "\tprojectedEatonSalesYr1 = " + this.getProjectedEatonSalesYr1() + "\n";
		returnString += "\tprojectedEatonSalesYr2 = " + this.getProjectedEatonSalesYr2() + "\n";
		returnString += "\tsegments = " + this.getSegments() + "\n";
		returnString += "\tservices = " + this.getServices() + "\n";
		returnString += "\tspecialistPersonnel = " + this.getSpecialistPersonnel() + "\n";
		returnString += "\tsuppliersProducts = " + this.getSuppliersProducts() + "\n";
		returnString += "\ttradeAssociations = " + this.getTradeAssociations() + "\n";
		returnString += "\twhseDriversPersonnel = " + this.getWhseDriversPersonnel() + "\n";
		returnString += "\tstatus = " + this.getStatus() + "\n";
		returnString += "\tvcn = " + this.getVcn() + "\n";
		returnString += "\teCommerce = " + this.getECommerce() + "\n";
		returnString += "\tfacilitiesOther = " + this.getFacilitiesOther() + "\n";
		returnString += "\tservicesOther = " + this.getServicesOther() + "\n";
		returnString += "\tproducts = " + this.getProducts() + "\n";
		returnString += "\tbuyingGroupOther = " + this.getBuyingGroupOther() + "\n";
		returnString += "\tloadModules size = " + this.getLoadModules().size() + "\n";

		return returnString;
	}
	public ArrayList getLoadModules() {
		return loadModules;
	}
	public void setLoadModules(ArrayList loadModules) {
		this.loadModules = loadModules;
	}
	public int getOtherCustomerSegmentPercentage() {
		return otherCustomerSegmentPercentage;
	}
	public void setOtherCustomerSegmentPercentage(int otherCustomerSegmentPercentage) {
		this.otherCustomerSegmentPercentage = otherCustomerSegmentPercentage;
	}
	public String getOtherCustomerSegmentPercentageNote() {
		return otherCustomerSegmentPercentageNote;
	}
	public void setOtherCustomerSegmentPercentageNote(
			String otherCustomerSegmentPercentageNote) {
		this.otherCustomerSegmentPercentageNote = otherCustomerSegmentPercentageNote;
	}


	public int getCustomerCategory() {
		return customerCategory;
	}


	public void setCustomerCategory(int customerCategory) {
		this.customerCategory = customerCategory;
	}


	public String getDunnBradStreet() {
		return dunnBradStreet;
	}


	public void setDunnBradStreet(String dunnBradStreet) {
		this.dunnBradStreet = dunnBradStreet;
	}


	public int getCommitmentProgram() {
		return commitmentProgram;
	}


	public void setCommitmentProgram(int commitmentProgram) {
		this.commitmentProgram = commitmentProgram;
	}


	public int getProjectedVScompYr2() {
		return projectedVScompYr2;
	}


	public void setProjectedVScompYr2(int projectedVScompYr2) {
		this.projectedVScompYr2 = projectedVScompYr2;
	}


	public int getProjectedVScompYr1() {
		return projectedVScompYr1;
	}


	public void setProjectedVScompYr1(int projectedVScompYr1) {
		this.projectedVScompYr1 = projectedVScompYr1;
	}


	public String getCommitmentReason() {
		return commitmentReason;
	}


	public void setCommitmentReason(String commitmentReason) {
		this.commitmentReason = commitmentReason;
	}


	


	
}
