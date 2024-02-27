/*
 * ErrorBean.java
 *
 * Created on April 5, 2004, 8:45 AM
 */

package com.eaton.electrical.smrc.bo;


/**
 *
 * @author  Jason Lubbert
 */
public class ErrorBean extends Object implements java.io.Serializable{
	
	private String ErrorMessage = "";
	private String JSPcalled = "";
	private String className = "";
	
	private static final long serialVersionUID = 100;

	/** Creates a new instance of ErrorBean */
	
	public ErrorBean() {
		
	}
	
	/** sets message to be displayed on screen */
	public void setErrorMessage(String errMsg){
		ErrorMessage = errMsg;
	}
	
	/** returns message to be displayed on screen */
	public String getErrorMessage(){
		return ErrorMessage;
	}
	
	/** Marks what JSP was being called when error occurred */
	public void setJSPcalled (String JSP){
		JSPcalled = JSP;
	}
	
	/** Returns what JSP was being called when error occurred */
	public String getJSPcalled (){
		return JSPcalled;
	}
	
	/** Marks the name of the class the error occurred in */
	public void setClassName (String classname) {
		className = classname;   
	}
	
	/** Returns the name of the class the error occurred in */
	public String getClassName (){
		return className;   
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

		returnString += "\terrorMessage = " + this.getErrorMessage() + "\n";
		returnString += "\tjSPcalled  = " + this.getJSPcalled () + "\n";
		returnString += "\tclassName  = " + this.getClassName () + "\n";

		return returnString;
	}
}
