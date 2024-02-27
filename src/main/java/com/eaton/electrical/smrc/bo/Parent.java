package com.eaton.electrical.smrc.bo;

public class Parent {

	// This very small class was used instead of just another Account object for the parent to prevent a possible
	// recusive SQL loop (if an account somehow ended up being its own parent, it could get 'stuck')
	
	private String vistaId = null;
	private String parentName = null;
	
	private static final long serialVersionUID = 100;

	public void setVistaId(String vistaId) {
		this.vistaId = vistaId;	
	}
	
	public String getVistaId() {
		return this.vistaId;
	}
	
	public void setParentName(String parentName) {
		this.parentName = parentName;	
	}
	
	public String getParentName() {
		return this.parentName;
	}

}
