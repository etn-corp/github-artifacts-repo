package com.eaton.electrical.smrc.bo;
import java.util.*;

/**
 * @author E0062708
 *
 */

public class SMRCHeaderBean implements java.io.Serializable {
	static ResourceBundle rb = ResourceBundle.getBundle("com.eaton.electrical.smrc.SMRC");
	
	
	private static final String imagesURL = rb.getString("imagesURL");
	private static final String cssURL = rb.getString("cssURL");
	private static final String jsURL = rb.getString("jsURL");
	private Account account = null;
	
	private static final long serialVersionUID = 100;

	private User user = null;

	public SMRCHeaderBean(){
		user = new User();
		account = new Account();
	
	}
	
	/**
	* Returns path for images. 
	*
	* @param  
	* @return      the image path from properties file
	*/
	public String getImagesURL () {
		return imagesURL;
	}
	
	/**
	* Returns path for css files. 
	*
	* @param  
	* @return      the css path from properties file
	*/
	public String getCssURL (){
		return cssURL;
	}
	
	/**
	* Returns path for javascript files. 
	*
	* @param  
	* @return      the javascript path from properties file
	*/
	public String getJsURL (){
		return jsURL;   
	}
	
	/**
	* Sets the User object. 
	*
	* @param  user  the user object for the user logged in
	* @return
	*/
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	* Returns the User object. 
	*
	* @param  
	* @return    the user object for the user logged in
	*/	
	public User getUser(){
		return this.user;
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

		returnString += "\timagesURL  = " + this.getImagesURL () + "\n";
		returnString += "\tcssURL  = " + this.getCssURL () + "\n";
		returnString += "\tjsURL  = " + this.getJsURL () + "\n";
		returnString += "\tuser = " + this.getUser() + "\n";

		return returnString;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
}
