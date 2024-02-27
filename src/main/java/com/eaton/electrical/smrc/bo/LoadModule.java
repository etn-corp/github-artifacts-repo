package com.eaton.electrical.smrc.bo;

/**
 * @author E0062708
 *
 */
public class LoadModule implements java.io.Serializable {
	
	private int moduleId=-1;
	private String productModuleCode = null;
	private boolean required = false;
	private String moduleName = null;
	private String moduleCode = null;
	private String moduleShortCode = null;
	private String url = null;
	
	private static final long serialVersionUID = 100;

	public LoadModule(){
		productModuleCode = "";
		moduleName = "";
		moduleCode = "";
		moduleShortCode = "";
		url = "";
	}

	public String getModuleCode() {
		return moduleCode;
	}
	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}
	public int getModuleId() {
		return moduleId;
	}
	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getProductModuleCode() {
		return productModuleCode;
	}
	public void setProductModuleCode(String productModuleCode) {
		this.productModuleCode = productModuleCode;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public String getModuleShortCode() {
		return moduleShortCode;
	}
	public void setModuleShortCode(String moduleShortCode) {
		this.moduleShortCode = moduleShortCode;
	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public boolean equals(LoadModule module){
		if(getModuleId()==module.getModuleId()){
			return true;
		}
		return false;

	}
	
	public boolean equals(Object obj) {
		if (this == null || obj == null) {
			return false;
		}

		return super.equals(obj);
	}
	
	
	public String toString() {
		String returnString = "";

		returnString += "\tmoduleId  = " + this.getModuleId () + "\n";
		returnString += "\tproductModuleCode  = " + this.getProductModuleCode () + "\n";
		returnString += "\trequired? = " + this.isRequired() + "\n";
		returnString += "\tmoduleName  = " + this.getModuleName () + "\n";
		returnString += "\tmoduleCode  = " + this.getModuleCode () + "\n";
		returnString += "\tmoduleShortCode  = " + this.getModuleShortCode () + "\n";
		returnString += "\turl = " + this.getUrl() + "\n";
		return returnString;
	}
	
	
}
