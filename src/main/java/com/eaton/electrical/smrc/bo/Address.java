package com.eaton.electrical.smrc.bo;

/**
 * @author E0062708
 *
 */
public class Address implements java.io.Serializable {

    public static final String DESCRIPTION_BUSINESS_ADDRESS = "Business Address";
    public static final String DESCRIPTION_BILLING_ADDRESS = "Bill-To Address";
    public static final String DESCRIPTION_SHIPPING_ADDRESS = "Shipping Address";
	
    private int id = -1;
	private String address1 = null;
	private String address2 = null;
	private String address3 = null;
	private String address4 = null;
	private String city = null;
	private String state = null;
	private String zip = null;
	private String country = null;
	private String countryName = null;
	private String vistaCountryCode = null; //COUNTRY_MV (mainframe codes)

	private static final long serialVersionUID = 100;
		
	public Address() {
		address1 = "";
		address2 = "";
		address3 = "";
		address4 = "";
		city = "";
		state = "";
		zip = "";
		country = "";
		countryName="";
		vistaCountryCode ="";
	}
	
	
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getAddress3() {
		return address3;
	}
	public void setAddress3(String address3) {
		this.address3 = address3;
	}
	public String getAddress4() {
		return address4;
	}
	public void setAddress4(String address4) {
		this.address4 = address4;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getVistaCountryCode() {
		return vistaCountryCode;
	}
	public void setVistaCountryCode(String vistaCountryCode) {
		this.vistaCountryCode = vistaCountryCode;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
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
	
	/*
	 * For sending to vista, we need to make sure we dont send the billing
	 * and shipping address if they are the same as the business address.
	 */
	public boolean equals(Address address) {
		if(this.address1.equalsIgnoreCase(address.getAddress1()) &&
			this.address2.equalsIgnoreCase(address.getAddress2()) &&
			this.address3.equalsIgnoreCase(address.getAddress3()) &&
			this.address4.equalsIgnoreCase(address.getAddress4()) &&
			this.getCity().equalsIgnoreCase(address.getCity()) &&
			this.getState().equalsIgnoreCase(address.getState()) &&
			this.getZip().equalsIgnoreCase(address.getZip()) &&
			this.country.equalsIgnoreCase(address.getCountry())){
			return true;
		}
		return false;
	}
	
	/**
	 * I tell whether an address is international (not domestic).
	 * @see this.isDomestic()
	 * @return true if this address is not domestic.
	 */
	public boolean isInternational() {
	    return !isDomestic();
	}
	
	/**
	 * I tell whether an address is "domestic".
	 * By the current problem domain definition, a domestic addresses requires a zip-code.
	 * Note that "international" addresses do not require a zip code.
	 * 
	 * We may need an additional behavior in the future: isZipCodeRequred() as well which will be somehow based on isDomestic().
	 * @return true if this address is domestic (US or US owned) or not.
	 */
	public boolean isDomestic() {
	    
	    //if country code is not set, assume that it is domestic.
	    //perhaps later we should default country code to "777" even in the database.
	    if (getVistaCountryCode() == null || getVistaCountryCode().trim().length() == 0) {
	        return true;
	    }
	    
	    return getVistaCountryCode().equals("777") || //US
	           getVistaCountryCode().equals("888") || //US - EXPORT
	           getVistaCountryCode().equals("778") || //US - CANADA BILLING
	           getVistaCountryCode().equals("031") || //PUERTO RICO
	           getVistaCountryCode().equals("803") || //GUAM
	           getVistaCountryCode().equals("813") || //SAMOA
	           getVistaCountryCode().equals("036") || //US VIRGIN ISLANDS
	           getVistaCountryCode().equals("085") || //PALAU
	           getVistaCountryCode().equals("811") || //N. MARIANA ISLANDS
	           getVistaCountryCode().equals("079");   //MICRONESIA, FEDERATE STATE OF
	}
	public String toString() {
		String returnString = "";

		returnString += "\taddress1 = " + this.getAddress1() + "\n";
		returnString += "\taddress2 = " + this.getAddress2() + "\n";
		returnString += "\taddress3 = " + this.getAddress3() + "\n";
		returnString += "\taddress4 = " + this.getAddress4() + "\n";
		returnString += "\tcity = " + this.getCity() + "\n";
		returnString += "\tcountry = " + this.getCountry() + "\n";
		returnString += "\tid = " + this.getId() + "\n";
		returnString += "\tstate = " + this.getState() + "\n";
		returnString += "\tzip = " + this.getZip() + "\n";
		returnString += "\tvistaCountryCode = " + this.getVistaCountryCode() + "\n";
		returnString += "\tdomestic? = " + this.isDomestic() + "\n";
		returnString += "\tinternational? = " + this.isInternational() + "\n";
		returnString += "\tcountryName = " + this.countryName + "\n";

		return returnString;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
}
