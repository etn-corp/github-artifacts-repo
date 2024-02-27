
package com.eaton.electrical.smrc.bo;

/**
 * @author E0062708
 *
 */
public class DistributorSupplierProduct {
	
	private String productId = null;
	private String productName= null;
	private String vendorId = null;
	private String vendorName = null;
	
	private static final long serialVersionUID = 100;

	public DistributorSupplierProduct(){
		productId = "";
		productName= "";
		vendorId = "";
		vendorName = "";	
		
	}
	public DistributorSupplierProduct(String productId, String vendorId){
		this.productId=productId;
		this.vendorId=vendorId;
		
	}
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
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

		returnString += "\tproductId = " + this.getProductId() + "\n";
		returnString += "\tproductName = " + this.getProductName() + "\n";
		returnString += "\tvendorId = " + this.getVendorId() + "\n";
		returnString += "\tvendorName = " + this.getVendorName() + "\n";

		return returnString;
	}
}
