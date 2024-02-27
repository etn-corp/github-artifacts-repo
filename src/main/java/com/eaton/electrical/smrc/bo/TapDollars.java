package com.eaton.electrical.smrc.bo;

import java.util.*;

/**
 * @author E0062708
 *
 */
public class TapDollars implements java.io.Serializable {
	
	String vistaCustomerNumber = null;
	String productId = null;
	String year = null;
	String month = null;
	String orderTapDollars = null;
	String invoiceTapDollars = null;
	
	private static final long serialVersionUID = 100;

	public TapDollars() {
	}

	/**
	 * @return Returns the serialVersionUID.
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * @return Returns the invoiceTapDollars.
	 */
	public String getInvoiceTapDollars() {
		return invoiceTapDollars;
	}

	/**
	 * @param invoiceTapDollars The invoiceTapDollars to set.
	 */
	public void setInvoiceTapDollars(String invoiceTapDollars) {
		this.invoiceTapDollars = invoiceTapDollars;
	}

	/**
	 * @return Returns the month.
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * @param month The month to set.
	 */
	public void setMonth(String month) {
		this.month = month;
	}

	/**
	 * @return Returns the orderTapDollars.
	 */
	public String getOrderTapDollars() {
		return orderTapDollars;
	}

	/**
	 * @param orderTapDollars The orderTapDollars to set.
	 */
	public void setOrderTapDollars(String orderTapDollars) {
		this.orderTapDollars = orderTapDollars;
	}

	/**
	 * @return Returns the productId.
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId The productId to set.
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return Returns the vistaCustomerNumber.
	 */
	public String getVistaCustomerNumber() {
		return vistaCustomerNumber;
	}

	/**
	 * @param vistaCustomerNumber The vistaCustomerNumber to set.
	 */
	public void setVistaCustomerNumber(String vistaCustomerNumber) {
		this.vistaCustomerNumber = vistaCustomerNumber;
	}

	/**
	 * @return Returns the year.
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @param year The year to set.
	 */
	public void setYear(String year) {
		this.year = year;
	}
	

}
