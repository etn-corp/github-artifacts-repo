package com.eaton.electrical.smrc.bo;

import java.util.*;

public class ModuleChangeReasonNotes {
	
	private long id;
	private long moduleChangeRequestId;
	private String reasonNotes;
	private String userAdded;
	private String userChanged;
	private Date dateAdded;
	private Date dateChanged;
	private String userAddedName;
	

	public ModuleChangeReasonNotes (){
		
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


	public String getReasonNotes() {
		return reasonNotes;
	}


	public void setReasonNotes(String reasonNotes) {
		this.reasonNotes = reasonNotes;
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


	public String getUserAddedName() {
		return userAddedName;
	}


	public void setUserAddedName(String userAddedName) {
		this.userAddedName = userAddedName;
	}



}
