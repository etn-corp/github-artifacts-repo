
package com.eaton.electrical.smrc.bo;

/**
 * @author E0062708
 */
public class SessionProfile implements java.io.Serializable {
	private String userid = null;
	private String email = null;
	private String firstName = null;
	private String lastName = null;
	private String vistaID = null;
	private String street = null;
	private String city = null;
	private String state = null;
	private String zipCode = null;
	private String country = null;
	
	private static final long serialVersionUID = 100;

	public SessionProfile(){
		userid = "";
		email = "";
		firstName = "";
		lastName = "";
		vistaID = "";
		street = "";
		city = "";
		state = "";
		zipCode = "";
		country = "";
	}

	public SessionProfile(String userid, String email, 
			String lastName, String firstName, 
			String vistaID, String street, String city,
			String state, String zipCode, String country){
		this.userid = userid;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.vistaID = vistaID;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.country = country;
	}
	
	public String getEmail() {
	    return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getVistaID() {
	    return vistaID;
	}
	
	public void setVistaID(String vistaID) {
		this.vistaID = vistaID;
	}
	
	public String getStreet() {
	    return street;
	}
	
	public void setStreet(String aStreet) {
		this.street = aStreet;
	}
	
	public String getCity() {
	    return city;
	}
	
	public void setCity(String aCity) {
		this.city = aCity;
	}
	
	public String getState() {
	    return state;
	}
	
	public void setState(String aState) {
		this.state = aState;
	}
	
	public String getZipCode() {
	    return zipCode;
	}
	
	public void setZipCode(String aZipCode) {
		this.zipCode = aZipCode;
	}
	
	public String getCountry() {
	    return country;
	}
	
	public void setCuntry(String aCountry) {
		this.country = aCountry;
	}
	
}
