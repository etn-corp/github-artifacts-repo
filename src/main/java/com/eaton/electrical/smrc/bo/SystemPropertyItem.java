package com.eaton.electrical.smrc.bo;

import java.io.Serializable;

public class SystemPropertyItem implements Serializable {
	
	private int 	_systemPropertyIdNo = 0;
	private int		_order = 0;
	private String 	_propertyValue = null;

	private static final long serialVersionUID = 100;

	public SystemPropertyItem() {
				
	}
	
	public SystemPropertyItem(int systemPropertyIdNo, int order, String propertyValue) {
		
		this.setSystemPropertyIdNo(systemPropertyIdNo);
		this.setOrder(order);
		this.setPropertyValue(propertyValue);
		
	}

	/**
	 * @return Returns the order.
	 */
	public int getOrder() {
		return _order;
	}

	/**
	 * @param order The order of the value to set.
	 */
	public void setOrder(int order) {
		this._order = order;
	}

	/**
	 * @return Returns the propertyValue.
	 */
	public String getPropertyValue() {
		return _propertyValue;
	}

	/**
	 * @param value The propertyValue to set.
	 */
	public void setPropertyValue(String value) {
		_propertyValue = value;
	}

	/**
	 * @return Returns the systemPropertyIdNo.
	 */
	public int getSystemPropertyIdNo() {
		return _systemPropertyIdNo;
	}

	/**
	 * @param propertyIdNo The systemPropertyIdNo to set.
	 */
	public void setSystemPropertyIdNo(int propertyIdNo) {
		_systemPropertyIdNo = propertyIdNo;
	}
	
	/**
	 * Display the system property.
	 * 
	 */
	
	public String toString() {
		
		return "Value-" + _propertyValue+ "|Order-" + _order + "|SystemPropId-" + _systemPropertyIdNo ;
		
	}
	
}
