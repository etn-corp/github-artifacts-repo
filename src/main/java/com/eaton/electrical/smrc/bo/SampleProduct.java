package com.eaton.electrical.smrc.bo;



/**	This class contains all of the methods and attributes needed by the Target Account Planner
*	as needed for each product that is on the sample request
*
*	@author Carl Abel
*/
public class SampleProduct implements java.io.Serializable {
	int _qty = 0;
	String _cat = "";

	private static final long serialVersionUID = 100;

	/** Set the number of products to be ordered on this sample
	*	@param int The number of products to be ordered on this sample
	*/
	public void setQty (int qty) {
		_qty = qty;
	}

	/** Set the catalog number of the product to be ordered
	*	@param String The catalog number of the product to be ordered
	*/
	public void setCatalogNum(String cat) {
		_cat = cat;
	}

	/** Retrieve the number of products ordered on this sample
	*	@return int The number of products ordered on this sample
	*/
	public int getQty() {
		return _qty;
	}

	/** Retrieve the catalog number of the product ordered on this sample
	*	@return String the catalog number of the product ordered on this sample
	*/
	public String getCatalogNum() {
		return _cat;
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

		returnString += "\tqty = " + this.getQty() + "\n";
		returnString += "\tcatalogNum = " + this.getCatalogNum() + "\n";

		return returnString;
	}
}
