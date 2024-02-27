
package com.eaton.electrical.smrc.bo;

import java.util.*;

/**
 * @author E0062708
 *
 */
public class OEMAcctPlanBean implements java.io.Serializable {
	private HeaderBean header;
	private SalesGroup sGroup, primaryUsersSalesGroup;
	private ArrayList alIndustrySub, appChanges, alChannels, alObjectives, alTasks;
	private User user, maintUser, pm;
	private Toolbox toolbox;
	private SalesPlan salesPlan;
	private Customer cust, custParent;
	private String backSort, backParm, customerName, sSalesmanName, updateDate;
	private String dolSort, tySort, pytdSort, pytotSort;
	private String deleteCustMsg, page, industrySubgroupName;
	private Integer ind;
	private String sortMeth;
	private ArrayList customers, products, salesPlanStages, ebeCategories, users, groups, usersGroups, userGeogOverrides, projects, results, fTypes, children, industries, contacts, bids;
	private boolean ableToUpdate, ableToSee, isAssociated=false, custInGroup=false;
	private SPStage stage;
	private Double[] cspSales;
		
	private static final long serialVersionUID = 100;

	public void setHeader(HeaderBean header) {
		this.header=header;
		
	}	
	public HeaderBean getHeader() {
		return header;
	}

	public void setSalesGroup(SalesGroup sGroup) {
		this.sGroup=sGroup;
	}
	public SalesGroup getSalesGroup() {
		return this.sGroup;
	}
	public void setIndustryFocusSubgroups(ArrayList alIndustrySub) {
		this.alIndustrySub=alIndustrySub;
	}
	public ArrayList getIndustryFocusSubgroups() {
		return this.alIndustrySub;
	}

	public void setUser(User user) {
		this.user=user;	
	}
	public User getUser() {
		return this.user;
	}

	public void setToolBox(Toolbox toolbox) {
		this.toolbox=toolbox;
	}
	public Toolbox getToolBox() {
		return this.toolbox;
	}

	public void setAppChanges(ArrayList appChanges) {
		this.appChanges=appChanges;
	}
	public ArrayList getAppChanges() {
		return this.appChanges;
	}

	public void setSalesPlan(SalesPlan salesPlan) {
		this.salesPlan=salesPlan;
	}
	
	public SalesPlan getSalesPlan() {
		return this.salesPlan;
	}
	
	public void setCust(Customer cust) {
		this.cust=cust;
	}
	
	public Customer getCust() {
		return this.cust;
	}
	
	public void setBackSort(String backSort) {
		this.backSort=backSort;
	}
	
	public String getBackSort() {
		return this.backSort;
	}
	
	public void setBackParm(String backParm) {
		this.backParm=backParm;
	}
	
	public String getBackParm() {
		return this.backParm;
	}

	public void setCustomerName(String customerName) {
		this.customerName=customerName;
	}
	
	public String getCustomerName() {
		return this.customerName;
	}

	public void setChannels(ArrayList alChannels) {
		this.alChannels=alChannels;
	}
	
	public ArrayList getChannels() {
		return this.alChannels;
	}

	public void setSalesmanName(String sSalesmanName) {
		this.sSalesmanName=sSalesmanName;
	}
	
	public String getSalesmanName() {
		return this.sSalesmanName;
	}

	public void setObjectives(ArrayList alObjectives) {
		this.alObjectives=alObjectives;
	}
	
	public ArrayList getObjectives() {
		return this.alObjectives;
	}

	public void setTasks(ArrayList alTasks) {
		this.alTasks=alTasks;
	}
	public ArrayList getTasks() {
		return this.alTasks;		
	}

	public void setLastUpdateDate(String updateDate) {
		this.updateDate=updateDate;
		
	}
	public String getLastUpdateDate() {
		return this.updateDate;
		
	}
	public void setDolSort(String dolSort) {
		this.dolSort=dolSort;		
	}
	public String getDolSort() {
		return this.dolSort;
	}

	public void setTySort(String tySort) {
		this.tySort=tySort;
	}
	public String getTySort() {
		return this.tySort;
	}

	public void setPytdSort(String pytdSort) {
		this.pytdSort=pytdSort;
	}
	public String getPytdSort() {
		return this.pytdSort;
	}

	public void setPytotSort(String pytotSort) {
		this.pytotSort=pytotSort;		
	}
	public String getPytotSort() {
		return this.pytotSort;
	}
	
	/**
	 * @return
	 */
	public ArrayList getCustomers() {
		return customers;
	}

	/**
	 * @return
	 */
	public String getGenMsg() {
		return deleteCustMsg;
	}

	/**
	 * @return
	 */
	public Integer getInd() {
		return ind;
	}

	/**
	 * @return
	 */
	public String getSortMeth() {
		return sortMeth;
	}

	/**
	 * @param list
	 */
	public void setCustomers(ArrayList list) {
		customers = list;
	}

	/**
	 * @param string
	 */
	public void setGenMsg(String string) {
		deleteCustMsg = string;
	}

	/**
	 * @param integer
	 */
	public void setInd(Integer integer) {
		ind = integer;
	}

	/**
	 * @param string
	 */
	public void setSortMeth(String string) {
		sortMeth = string;
	}

	/**
	 * @return
	 */
	public String getPage() {
		return page;
	}

	/**
	 * @param string
	 */
	public void setPage(String string) {
		page = string;
	}

	/**
	 * @return
	 */
	public String getIndustrySubgroupName() {
		return industrySubgroupName;
	}

	/**
	 * @param string
	 */
	public void setIndustrySubgroupName(String string) {
		industrySubgroupName = string;
	}

	public void setProducts(ArrayList products) {
		this.products=products;
		
	}
	public ArrayList getProducts() {
		return this.products;
		
	}
	public void setAbleToSee(boolean ableToSee) {
		this.ableToSee=ableToSee;
		
	}
	public boolean getAbleToSee() {
		return this.ableToSee;
	}
	public void setAbleToUpdate(boolean ableToUpdate) {
		this.ableToUpdate=ableToUpdate;
	}
	public boolean getAbleToUpdate() {
		return this.ableToUpdate;
	}

	public void setCustomerStage(SPStage stage) {
		this.stage=stage;
		
	}
	public SPStage getCustomerStage() {
		return this.stage;
		
	}
	public void setSalesPlanStages(ArrayList salesPlanStages) {
		this.salesPlanStages=salesPlanStages;
		
	}
	public ArrayList getSalesPlanStages() {
		return this.salesPlanStages;
		
	}

	public void setUsers(ArrayList users) {
		this.users=users;
		
	}
	public ArrayList getUsers() {
		return this.users;
		
	}
	public void setEbeCategories(ArrayList ebeCategories) {
		this.ebeCategories=ebeCategories;
		
	}
	public ArrayList getEbeCategories() {
		return this.ebeCategories;
		
	}

	public void setMaintUser(User maintUser) {
		this.maintUser=maintUser;
		
	}
	public User getMaintUser() {
		return this.maintUser;
		
	}	
	public void setGroups(ArrayList groups) {
		this.groups=groups;
		
	}
	public ArrayList getGroups() {
		return this.groups;
		
	}

	public void setUsersGroups(ArrayList usersGroups) {
		this.usersGroups=usersGroups;
		
	}	
	public ArrayList getUsersGroups() {
		return this.usersGroups;
		
	}

	public void setPrimaryUsersSalesGroup(SalesGroup primaryUsersSalesGroup) {
		this.primaryUsersSalesGroup=primaryUsersSalesGroup;
		
	}	
	public SalesGroup getPrimaryUsersSalesGroup() {
		return this.primaryUsersSalesGroup;
		
	}

	public void setUserGeogOverrides(ArrayList userGeogOverrides) {
		this.userGeogOverrides=userGeogOverrides;
		
	}	
	public ArrayList getUserGeogOverrides() {
		return this.userGeogOverrides;
		
	}

	public void setProjects(ArrayList projects) {
		this.projects=projects;
		
	}	
	public ArrayList getProjects() {
		return this.projects;
		
	}

	public void setResults(ArrayList results) {
		this.results=results;
		
	}
	public ArrayList getResults() {
		return this.results;
		
	}

	public void setIsAssociated(boolean isAssociated) {
		this.isAssociated=isAssociated;
		
	}			
	public boolean getIsAssociated() {
		return this.isAssociated;
		
	}

	public void setFocusTypes(ArrayList fTypes) {
		this.fTypes=fTypes;
		
	}			
	public ArrayList getFocusTypes() {
		return this.fTypes;
		
	}

	public void setChildren(ArrayList children) {
		this.children=children;
		
	}
	public ArrayList getChildren() {
		return this.children;
		
	}

	public void setCustInGroup(boolean custInGroup) {
		this.custInGroup=custInGroup;
		
	}
	public boolean getCustInGroup() {
		return this.custInGroup;
		
	}

	public void setCustParent(Customer custParent) {
		this.custParent=custParent;
		
	}
	public Customer getCustParent() {
		return this.custParent;
		
	}

	public void setCspSales(Double[] cspSales) {
		this.cspSales=cspSales;
		
	}	
	public Double[] getCspSales() {
		return this.cspSales;
		
	}

	public void setIndustries(ArrayList industries) {
		this.industries=industries;
		
	}
	public ArrayList getIndustries() {
		return this.industries;
		
	}

	public void setContacts(ArrayList contacts) {
		this.contacts=contacts;
		
	}	
	public ArrayList getContacts() {
		return this.contacts;
		
	}


	// added by jpv
	public void setPricingManager(User pm) {
			this.pm=pm;
		
	}
	public User getPricingManager() {
			return this.pm;
		
	}

	public void setBids(ArrayList bids) {
		this.bids=bids;
		
	}
	public ArrayList getBids() {
		return this.bids;
		
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

		returnString += "\theader = " + this.getHeader() + "\n";
		returnString += "\tsalesGroup = " + this.getSalesGroup() + "\n";
		returnString += "\tindustryFocusSubgroups = " + this.getIndustryFocusSubgroups() + "\n";
		returnString += "\tuser = " + this.getUser() + "\n";
		returnString += "\ttoolBox = " + this.getToolBox() + "\n";
		returnString += "\tappChanges = " + this.getAppChanges() + "\n";
		returnString += "\tsalesPlan = " + this.getSalesPlan() + "\n";
		returnString += "\tcust = " + this.getCust() + "\n";
		returnString += "\tbackSort = " + this.getBackSort() + "\n";
		returnString += "\tbackParm = " + this.getBackParm() + "\n";
		returnString += "\tcustomerName = " + this.getCustomerName() + "\n";
		returnString += "\tchannels = " + this.getChannels() + "\n";
		returnString += "\tsalesmanName = " + this.getSalesmanName() + "\n";
		returnString += "\tobjectives = " + this.getObjectives() + "\n";
		returnString += "\ttasks = " + this.getTasks() + "\n";
		returnString += "\tlastUpdateDate = " + this.getLastUpdateDate() + "\n";
		returnString += "\tdolSort = " + this.getDolSort() + "\n";
		returnString += "\ttySort = " + this.getTySort() + "\n";
		returnString += "\tpytdSort = " + this.getPytdSort() + "\n";
		returnString += "\tpytotSort = " + this.getPytotSort() + "\n";
		returnString += "\tcustomers = " + this.getCustomers() + "\n";
		returnString += "\tgenMsg = " + this.getGenMsg() + "\n";
		returnString += "\tind = " + this.getInd() + "\n";
		returnString += "\tsortMeth = " + this.getSortMeth() + "\n";
		returnString += "\tpage = " + this.getPage() + "\n";
		returnString += "\tindustrySubgroupName = " + this.getIndustrySubgroupName() + "\n";
		returnString += "\tproducts = " + this.getProducts() + "\n";
		returnString += "\tableToSee = " + this.getAbleToSee() + "\n";
		returnString += "\tableToUpdate = " + this.getAbleToUpdate() + "\n";
		returnString += "\tcustomerStage = " + this.getCustomerStage() + "\n";
		returnString += "\tsalesPlanStages = " + this.getSalesPlanStages() + "\n";
		returnString += "\tusers = " + this.getUsers() + "\n";
		returnString += "\tebeCategories = " + this.getEbeCategories() + "\n";
		returnString += "\tmaintUser = " + this.getMaintUser() + "\n";
		returnString += "\tgroups = " + this.getGroups() + "\n";
		returnString += "\tusersGroups = " + this.getUsersGroups() + "\n";
		returnString += "\tprimaryUsersSalesGroup = " + this.getPrimaryUsersSalesGroup() + "\n";
		returnString += "\tuserGeogOverrides = " + this.getUserGeogOverrides() + "\n";
		returnString += "\tprojects = " + this.getProjects() + "\n";
		returnString += "\tresults = " + this.getResults() + "\n";
		returnString += "\tisAssociated = " + this.getIsAssociated() + "\n";
		returnString += "\tfocusTypes = " + this.getFocusTypes() + "\n";
		returnString += "\tchildren = " + this.getChildren() + "\n";
		returnString += "\tcustInGroup = " + this.getCustInGroup() + "\n";
		returnString += "\tcustParent = " + this.getCustParent() + "\n";
		returnString += "\tcspSales = " + this.getCspSales() + "\n";
		returnString += "\tindustries = " + this.getIndustries() + "\n";
		returnString += "\tcontacts = " + this.getContacts() + "\n";
		returnString += "\tpricingManager = " + this.getPricingManager() + "\n";
		returnString += "\tbids = " + this.getBids() + "\n";

		return returnString;
	}
}
