
package com.eaton.electrical.smrc.bo;

import java.text.*;
import java.util.*;


public class TargetProject extends Object implements java.io.Serializable {
	// All of these codes should be part of a DAO, but just in
	// order to put this change from TAP into SMRC, I am copying
	// the code in the appropriate places; we'll have to 
	// clean up later...
	private static final String END_CUSTOMER_CODE = "C";
	private static final String DISTRIBUTOR_CODE = "D";
	private static final String ELECTRICAL_CONTRACTOR_CODE = "E";
	private static final String ACTIVE_CODE = "A";
	private static final String DELETED_CODE = "D";
	private static final String DM_APPROVED_CODE = "Z";
	private static final String PROJECT_SALES_MGR_APPROVED_CODE = "M";
	private static final String NEW_PROJECT_CODE = "N";

	private int _projectID = 0;
	private String _spGeog = "";
	private String _jobName = "";
	private String _consultant = "";
	private double _chValue = 0;
	private double _totalValue = 0;
	private Calendar _bidDate;
	private ChangeOrderPotential _cop = new ChangeOrderPotential();
	private String _generalContractors = "";
	private String _electricalContractors = "";
	private String _chPosition = "";
	private ProjectStatus _status = new ProjectStatus();
	private SpecPreference _preference = new SpecPreference();
	private TargetReason _reason = new TargetReason();
	private String _statusNotes = "";
	private String _preferenceNotes = "";
	private String _stratReasonNotes = "";
	private String _negNum = "";
	private String _notes = "";
	private String _userAdded = "";
	private Calendar _dateAdded;
	private Calendar _dateChanged;
	private String _salesmanId = "";
	private String _internalStatus = "";
	private String _dmApproveId = "";
	private String _zmApproveId = "";
	private String _champsMgrApproveId = "";
	private String _projSalesMgrApproveId = "";
	private String _mktMgrApproveId = "", geogName="";
	private Calendar _dmApproveDt;
	private Calendar _zmApproveDt;
	private Calendar _mktMgrApproveDt;
	private Calendar _champsMgrApproveDt;
	private Calendar _projSalesMgrApproveDt;
	private ArrayList _custs = new ArrayList(5);
	private ArrayList _custTypes = new ArrayList(5);
	private ArrayList _vendors = new ArrayList(5);
	private ArrayList _products = new ArrayList(5);
	private boolean canApprove=false;

	private static final long serialVersionUID = 100;

	public void setCHValue(double val) {
		_chValue = val;
	}

	public void setTotalValue(double val) {
		_totalValue = val;
	}

	public void setSPGeog(String geog) {
		_spGeog = geog;
	}

	public void setJobName(String name) {
		_jobName = name;
	}

	public void setConsultant(String consult) {
		_consultant = consult;
	}

	public void setGenContractors(String contract) {
		_generalContractors = contract;
	}

	public void setElecContractors(String contract) {
		_electricalContractors = contract;
	}

	public void setCHPosition(String chPos) {
		_chPosition = chPos;
	}

	public void setStatusNotes(String stat) {
		_statusNotes = stat;
	}

	public void setPreferenceNotes(String pref) {
		_preferenceNotes = pref;
	}

	public void setStratReasonNotes(String reason) {
		_stratReasonNotes = reason;
	}

	public void setNegNum(String neg) {
		_negNum = neg;
	}

	public void setNotes(String notes) {
		_notes = notes;
	}

	public void setUserAdded(String user) {
		_userAdded = user;
	}

	public void setDateAdded(java.sql.Date date) {
		_dateAdded = Calendar.getInstance(Locale.US);
		_dateAdded.setTime(date);
	}

	public void setDateChanged(java.sql.Date date) {
		_dateChanged = Calendar.getInstance(Locale.US);
		_dateChanged.setTime(date);
	}

	public void setBidDate(java.sql.Date date) {
		_bidDate = Calendar.getInstance(Locale.US);
		_bidDate.setTime(date);
	}

	public void setId(int id) {
		_projectID = id;
	}

	public void setChangeOrderPotential(ChangeOrderPotential cop) {
		_cop = cop;
	}

	public void setStatus(ProjectStatus stat) {
		_status = stat;
	}

	public void setPreference(SpecPreference pref) {
		_preference= pref;
	}

	public void setReason(TargetReason reason) {
		_reason = reason;
	}

	public void addVendor(Vendor ven) {
		_vendors.add(ven);
	}

	public void addCustomer(Customer cust, String custType) {
		_custs.add(cust);
		_custTypes.add(custType);
	}

	public void addProduct(Product p) {
		_products.add(p);
	}

	public void setSalesmanId(String sid) {
		_salesmanId = sid;
	}


	public String getSPGeog() {
		return _spGeog;
	}

	public String getJobName() {
		return _jobName;
	}

	public String getConsultant() {
		return _consultant;
	}

	public String getGenContractors() {
		return _generalContractors;
	}

	public String getElecContractors() {
		return _electricalContractors;
	}

	public String getCHPosition() {
		return _chPosition;
	}

	public String getStatusNotes() {
		return _statusNotes;
	}

	public String getPreferenceNotes() {
		return _preferenceNotes;
	}

	public String getStratReasonNotes() {
		return _stratReasonNotes;
	}

	public String getNegNum() {
		return _negNum;
	}

	public String getNotes() {
		return _notes;
	}

	public String getUserAdded() {
		return _userAdded;
	}

	public Calendar getDateAdded() {
		return _dateAdded;
	}

	public String getDateAddedAsString() {
		if (_dateAdded == null) {
			return "New Project";
		}

		return (_dateAdded.get(Calendar.MONTH)+1) + "/" +
				_dateAdded.get(Calendar.DATE) + "/" +
				_dateAdded.get(Calendar.YEAR);

	}

	public Calendar getDateChanged() {
		return _dateChanged;
	}

	public String getDateChangedAsString() {
		if (_dateChanged == null) {
			return "New Project";
		}

		return (_dateChanged.get(Calendar.MONTH)+1) + "/" +
				_dateChanged.get(Calendar.DATE) + "/" +
				_dateChanged.get(Calendar.YEAR);

	}

	public Calendar getBidDate() {
		return _bidDate;
	}

	public String getBidDateAsString() {
		if (_bidDate == null) {
			return "";
		}

		return (_bidDate.get(Calendar.MONTH)+1) + "/" +
				_bidDate.get(Calendar.DATE) + "/" +
				_bidDate.get(Calendar.YEAR);

	}

	public int getId() {
		return _projectID;
	}

	public ChangeOrderPotential getChangeOrderPotential() {
		return _cop;
	}

	public ProjectStatus getStatus() {
		return _status;
	}

	public SpecPreference getPreference() {
		return _preference;
	}

	public TargetReason getReason() {
		return _reason;
	}

	public double getCHValue() {
		return _chValue;
	}

	public String getCHValueForDisplay() {
		NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
		return nf.format(_chValue);
	}

	public String getTotalValueForDisplay() {
		NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
		return nf.format(_totalValue);
	}

	public double getTotalValue() {
		return _totalValue;
	}

	public String getSalesmanId() {
		return _salesmanId;
	}

	public void setInternalStatus(String stat) {
		_internalStatus = stat;
	}

	public void setDMApprovalId(String id) {
		_dmApproveId = id;
	}

	public void setZMApprovalId(String id) {
		_zmApproveId = id;
	}

	public void setMktMgrApprovalId(String id) {
		_mktMgrApproveId = id;
	}

	public void setDMApprovalDate(Date dt) {
		_dmApproveDt = Calendar.getInstance();
		_dmApproveDt.setTime(dt);
	}

	public void setZMApprovalDate(Date dt) {
		_zmApproveDt = Calendar.getInstance();
		_zmApproveDt.setTime(dt);
	}

	public void setMktMgrApprovalDate(Date dt) {
		_mktMgrApproveDt = Calendar.getInstance();
		_mktMgrApproveDt.setTime(dt);
	}

	public String getInternalStatus() {
		return _internalStatus;
	}

	public boolean dmApproved() {
		if (_dmApproveId != null && !_dmApproveId.equals("")) {
			return true;
		}
		return false;
	   
	}

	public boolean zmApproved() {
		if (_zmApproveId != null && !_zmApproveId.equals("")) {
			return true;
		}
	    
		return false;
	}

	public boolean mktMgrApproved() {
		if (_mktMgrApproveId != null && !_mktMgrApproveId.equals("")) {
			return true;
		}
	    
		return false;
	}
	public boolean champsMgrApproved() {
		if (_champsMgrApproveId != null && !_champsMgrApproveId.equals("")) {
			return true;
		}
		return false;
	   
	}
	public boolean projSalesMgrApproved() {
		if (_projSalesMgrApproveId != null && !_projSalesMgrApproveId.equals("")) {
			return true;
		}
	    
		return false;
	}

	public boolean isActive() {
//		return (dmApproved() && (zmApproved() || projSalesMgrApproved()) 
//		        && (mktMgrApproved() || champsMgrApproved()) && !deleted());
	    if (_internalStatus.equalsIgnoreCase(ACTIVE_CODE)){
	        return true;
	    } else {
	        return false;
	    }
	}

	public boolean deleted() {
		if (_internalStatus.equalsIgnoreCase(DELETED_CODE)) {
			return true;
		}
		return false;
	}

	public ArrayList getVendors() {
		return _vendors;
	}

	public ArrayList getContractors() {
		ArrayList ret = new ArrayList(5);
		for (int i=0; i < _custs.size(); i++) {
			Customer c = (Customer)_custs.get(i);
			String type = (String)_custTypes.get(i);

			if (type.equals( ELECTRICAL_CONTRACTOR_CODE )) {
				ret.add(c);
			}
		}

		return ret;
	}

	public Customer getEndCustomer() {
		Customer ret = new Customer();
		for (int i=0; i < _custs.size(); i++) {
			Customer c = (Customer)_custs.get(i);
			String type = (String)_custTypes.get(i);

			if (type.equals( END_CUSTOMER_CODE )) {
				ret = c;
			}
		}
		return ret;
	}

	public ArrayList getDistributors() {
		ArrayList ret = new ArrayList(5);
		for (int i=0; i < _custs.size(); i++) {
			Customer c = (Customer)_custs.get(i);
			String type = (String)_custTypes.get(i);

			if (type.equals( DISTRIBUTOR_CODE )) {
				ret.add(c);
			}
		}
		return ret;
	}

	public ArrayList getProducts() {
		return _products;
	}

	/** added by jpv
	 * 
	 */
	public void setCanApprove(boolean canApprove) {
		this.canApprove=canApprove;
		
	}
	public boolean getCanApprove() {
		return this.canApprove;
		
	}

	/**
	 * added by jpv
	 */
	public void setGeogName(String geogName) {
		this.geogName=geogName;
		
	}
	public String getGeogName() {
		return this.geogName;
		
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

		returnString += "\tsPGeog = " + this.getSPGeog() + "\n";
		returnString += "\tjobName = " + this.getJobName() + "\n";
		returnString += "\tconsultant = " + this.getConsultant() + "\n";
		returnString += "\tgenContractors = " + this.getGenContractors() + "\n";
		returnString += "\telecContractors = " + this.getElecContractors() + "\n";
		returnString += "\tcHPosition = " + this.getCHPosition() + "\n";
		returnString += "\tstatusNotes = " + this.getStatusNotes() + "\n";
		returnString += "\tpreferenceNotes = " + this.getPreferenceNotes() + "\n";
		returnString += "\tstratReasonNotes = " + this.getStratReasonNotes() + "\n";
		returnString += "\tnegNum = " + this.getNegNum() + "\n";
		returnString += "\tnotes = " + this.getNotes() + "\n";
		returnString += "\tuserAdded = " + this.getUserAdded() + "\n";
		returnString += "\tdateAdded = " + this.getDateAdded() + "\n";
		returnString += "\tdateAddedAsString = " + this.getDateAddedAsString() + "\n";
		returnString += "\tdateChanged = " + this.getDateChanged() + "\n";
		returnString += "\tdateChangedAsString = " + this.getDateChangedAsString() + "\n";
		returnString += "\tbidDate = " + this.getBidDate() + "\n";
		returnString += "\tbidDateAsString = " + this.getBidDateAsString() + "\n";
		returnString += "\tid = " + this.getId() + "\n";
		returnString += "\tchangeOrderPotential = " + this.getChangeOrderPotential() + "\n";
		returnString += "\tstatus = " + this.getStatus() + "\n";
		returnString += "\tpreference = " + this.getPreference() + "\n";
		returnString += "\treason = " + this.getReason() + "\n";
		returnString += "\tcHValue = " + this.getCHValue() + "\n";
		returnString += "\tcHValueForDisplay = " + this.getCHValueForDisplay() + "\n";
		returnString += "\ttotalValueForDisplay = " + this.getTotalValueForDisplay() + "\n";
		returnString += "\ttotalValue = " + this.getTotalValue() + "\n";
		returnString += "\tsalesmanId = " + this.getSalesmanId() + "\n";
		returnString += "\tinternalStatus = " + this.getInternalStatus() + "\n";
		returnString += "\tvendors = " + this.getVendors() + "\n";
		returnString += "\tcontractors = " + this.getContractors() + "\n";
		returnString += "\tendCustomer = " + this.getEndCustomer() + "\n";
		returnString += "\tdistributors = " + this.getDistributors() + "\n";
		returnString += "\tproducts = " + this.getProducts() + "\n";
		returnString += "\tcanApprove = " + this.getCanApprove() + "\n";
		returnString += "\tgeogName = " + this.getGeogName() + "\n";

		return returnString;
	}
    /**
     * @return Returns the _champsMgrApproveDt.
     */
    public Calendar getChampsMgrApproveDt() {
        return _champsMgrApproveDt;
    }
    /**
     * @param mgrApproveDt The _champsMgrApproveDt to set.
     */
    public void setChampsMgrApproveDt(Date date) {
        _champsMgrApproveDt = Calendar.getInstance();
        _champsMgrApproveDt.setTime(date);
    }
    /**
     * @return Returns the _champsMgrApproveId.
     */
    public String getChampsMgrApproveId() {
        return _champsMgrApproveId;
    }
    /**
     * @param mgrApproveId The _champsMgrApproveId to set.
     */
    public void setChampsMgrApproveId(String mgrApproveId) {
        _champsMgrApproveId = mgrApproveId;
    }
    /**
     * @return Returns the _projSalesMgrApproveDt.
     */
    public Calendar getProjSalesMgrApproveDt() {
        return _projSalesMgrApproveDt;
    }
    /**
     * @param salesMgrApproveDt The _projSalesMgrApproveDt to set.
     */
    public void setProjSalesMgrApproveDt(Date date) {
        _projSalesMgrApproveDt = Calendar.getInstance();
        _projSalesMgrApproveDt.setTime(date);
    }
    /**
     * @return Returns the _projSalesMgrApproveId.
     */
    public String getProjSalesMgrApproveId() {
        return _projSalesMgrApproveId;
    }
    /**
     * @param salesMgrApproveId The _projSalesMgrApproveId to set.
     */
    public void setProjSalesMgrApproveId(String salesMgrApproveId) {
        _projSalesMgrApproveId = salesMgrApproveId;
        
    }
    public boolean waitingForChampsMgr(){
        if (_internalStatus.equalsIgnoreCase(PROJECT_SALES_MGR_APPROVED_CODE)){
            return true;
        } else {
            return false;
        }
    }
    public boolean waitingForDM(){
        if (_internalStatus.equalsIgnoreCase(NEW_PROJECT_CODE)){
            return true;
        } else {
            return false;
        }
    }
    public boolean waitingForProjectSalesMgr(){
        if (_internalStatus.equalsIgnoreCase(DM_APPROVED_CODE)){
            return true;
        } else {
            return false;
        }
    }
    
}
