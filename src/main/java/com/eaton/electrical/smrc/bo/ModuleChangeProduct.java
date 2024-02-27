package com.eaton.electrical.smrc.bo;

import java.util.Date;

public class ModuleChangeProduct {
	
	private long id;
	private long moduleId;
	private long moduleChangeRequestId;
	private String action;
	private long vistaStatusId;
	private String userAdded;
	private String userChanged;
	private Date dateAdded;
	private Date dateChanged;
	private String loadingModuleCode;
	private String loadingModuleName;
	private String loadingModuleShortCode;
	
	
	public ModuleChangeProduct () {
		
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Date getDateChanged() {
		return dateChanged;
	}

	public void setDateChanged(Date dateChanged) {
		this.dateChanged = dateChanged;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getModuleChangeRequestId() {
		return moduleChangeRequestId;
	}

	public void setModuleChangeRequestId(long moduleChangeRequestId) {
		this.moduleChangeRequestId = moduleChangeRequestId;
	}

	public long getModuleId() {
		return moduleId;
	}

	public void setModuleId(long moduleId) {
		this.moduleId = moduleId;
	}

	public String getUserAdded() {
		return userAdded;
	}

	public void setUserAdded(String userAdded) {
		this.userAdded = userAdded;
	}

	public String getUserChanged() {
		return userChanged;
	}

	public void setUserChanged(String userChanged) {
		this.userChanged = userChanged;
	}

	public long getVistaStatusId() {
		return vistaStatusId;
	}

	public void setVistaStatusId(long vistaStatusId) {
		this.vistaStatusId = vistaStatusId;
	}

	public String getLoadingModuleCode() {
		return loadingModuleCode;
	}

	public void setLoadingModuleCode(String loadingModuleCode) {
		this.loadingModuleCode = loadingModuleCode;
	}

	public String getLoadingModuleName() {
		return loadingModuleName;
	}

	public void setLoadingModuleName(String loadingModuleName) {
		this.loadingModuleName = loadingModuleName;
	}

	public String getLoadingModuleShortCode() {
		return loadingModuleShortCode;
	}

	public void setLoadingModuleShortCode(String loadingModuleShortCode) {
		this.loadingModuleShortCode = loadingModuleShortCode;
	}

}
