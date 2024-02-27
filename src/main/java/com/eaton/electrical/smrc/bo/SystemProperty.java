package com.eaton.electrical.smrc.bo;

import java.io.Serializable;
import java.util.ArrayList;

import com.eaton.electrical.smrc.util.StringManipulation;;

public class SystemProperty implements Serializable {

	private String 		_propertyName 			= null;
	private ArrayList	_propertyItems			= null;
	
	private static final long serialVersionUID = 100;
	
	public SystemProperty() {
				
	}

	public SystemProperty(String name) {
		
		this.setPropertyName(name);
	}
	
	/**
	 * @return Returns the propertyName.
	 */
	public String getPropertyName() {
		return _propertyName;
	}


	/**
	 * @param name The propertyName to set.
	 */
	public void setPropertyName(String name) {
		_propertyName = name;
	}
	
	/**
	 * 
	 * @param item The property item to be added
	 */
	public void addPropertyItem(SystemPropertyItem item) {
		
		if (_propertyItems == null) {
		
			_propertyItems = new ArrayList();
			
		}		
		_propertyItems.add(item);
	}
	
	/**
	 * 
	 * @return The current property item list
	 */
	
	public ArrayList getPropertyItemList() {
	
		return _propertyItems;
		
	}
	
	/**
	 * @return Arraylist with the values of the property
	 */
	
	public ArrayList getPropertyValues() {
		
		ArrayList itemList = this.getPropertyItemList();
		ArrayList returnList = new ArrayList();
		
		for (int i = 0; i < itemList.size(); i ++) {
			
			returnList.add(((SystemPropertyItem)itemList.get(i)).getPropertyValue());
			
		}
		
		return returnList;
		
	}
	
	
	/**
	 * @return Displays data in object
	 */
	
	public String toString() {
		
		return toString(1);
	}
	
	/**
	 * 
	 * @param indent - Number of tabs to place in front of string.  This is used for logging format
	 * @return - Displays data in object
	 */
	
	public String toString(int indent) {
		
		String identValue = StringManipulation.indentTabs(indent);
		
		StringBuffer buffer = new StringBuffer(identValue + "SystemProperty : PropertyName:" + _propertyName);
		
		for (int i = 0; i < _propertyItems.size(); i ++) {
			
			buffer.append("\n" + identValue + "\t" + ((SystemPropertyItem)_propertyItems.get(i)).toString());
			
		}
		
		return buffer.toString();
		
	}

	
}
