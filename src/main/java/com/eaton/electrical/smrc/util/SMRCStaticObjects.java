package com.eaton.electrical.smrc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.eaton.electrical.smrc.bo.SystemProperty;
import com.eaton.electrical.smrc.bo.SystemPropertyItem;

import com.eaton.electrical.smrc.exception.SMRCException;

import com.eaton.electrical.smrc.service.SMRCConnectionPoolUtils;
import com.eaton.electrical.smrc.service.SMRCLogger;
 


public class SMRCStaticObjects {
		
	private static HashMap 	_staticObjects = null;
	private static String 	_localEnvironment = null;
	
	static {
		
		try {
			_staticObjects = new HashMap();
			SMRCStaticObjects.populateStaticMap();
		} catch (Exception Ex) {
				System.err.println("::FATAL::");
				System.err.println("Unable to initialize Six Sigma static objects");
				Ex.printStackTrace();
		}		
	}	


	public SMRCStaticObjects() throws SMRCException {		
	}

	private static void populateStaticMap() throws SMRCException {
		
		SMRCStaticObjects.loadSystemProperties();
				
	}
	
	public static Object get(String key) throws SMRCException {
		
		return _staticObjects.get(key);
		
	}
	
	protected static void put(String key, Object object) {
		
		_staticObjects.put(key, object);
		
	}
		
	private static void loadSystemProperties() throws SMRCException {
		
		final String systemPropSql = "select system_property_id_no, property_name, property_value, order_sequence"  
			+ " from system_properties"
			+ " order by property_name, order_sequence";
		
		Connection conn = null;		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int idNo = 0;
		int sequence = 0;
		String name = null;
		String value = null;
		
		SystemPropertyItem propItem = null;
				
		// We are not going to set up a logger because the logger needs this to be populated first
		
		try {
			
			conn = SMRCConnectionPoolUtils.getDatabaseConnection();
			pstmt = conn.prepareStatement(systemPropSql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
							
				idNo = rs.getInt("system_property_id_no");
				sequence = rs.getInt("order_sequence");
				name = rs.getString("property_name");
				value = rs.getString("property_value");
				
				propItem = new SystemPropertyItem();
				
				propItem.setOrder(sequence);
				propItem.setSystemPropertyIdNo(idNo);
				propItem.setPropertyValue(value);
				
				addSystemValue(name, propItem);
				
			} // end while rs has next
			
			rs.close();
			rs = null;
			
			pstmt.close();
			pstmt = null;
			
			conn.close();
			conn = null;
			
		} catch (Exception ex) {
			
			throw new SMRCException("Exception while loading system properties", ex);
			
		} finally {
			
			if (rs != null) {				
				try {				
					rs.close();					
				} catch (SQLException sqlex) {					
					new SMRCException("Exception while closing result set", sqlex);					
				}				
			}
			
			if (pstmt != null) {				
				try {				
					pstmt.close();					
				} catch (SQLException sqlex) {					
					new SMRCException("Exception while closing pstmt", sqlex);					
				}				
			}
			
			if (conn != null) {				
				try {				
					conn.close();					
				} catch (SQLException sqlex) {					
					new SMRCException("Exception while closing conn", sqlex);					
				}				
			}
		} // end finally
		
		// Log the system values.  This only happens once so no hit against performance
		
		if (SMRCLogger.isDebuggerEnabled()) {
			
			StringBuffer logOutput = new StringBuffer("System Property Values:"); 
			
			try {
				
				HashMap systemProp = getAllSystemProperties();
				Iterator iter = systemProp.keySet().iterator();
				
				while (iter.hasNext()) {
					
					logOutput.append("\n" + ((SystemProperty)systemProp.get((String)iter.next())).toString(1));
					
				}
				
			} catch (SMRCException ssEx) {
				
				// This is just logging so we don't want to crash the transaction.  Just log it.
				SMRCLogger.error("The previous Six Sigma error occured during logging");
				
			} catch (Exception ex) {
				
				new SMRCException("Error occured while outputting System Property Values for logging", ex);
				
			}
			
			SMRCLogger.debug(logOutput.toString());
						
		} // end isDebuggerEnabled
		
	} // end loadSystemProperties
	
	
	private static HashMap getAllSystemProperties() throws SMRCException {
		
		HashMap systemMap = (HashMap)SMRCStaticObjects.get(SMRCStaticConst.CONFIG_SYSTEM_PROP);
		
		if (systemMap == null) {
			
			return new HashMap();
			
		}
		
		return systemMap;
		
	} // end getAllSystemPropertys
	
	/**
	 * Returns an ArrayList of system values based on a passed in key in order of defined sequence
	 * @param key Key for values passed in
	 * @return ArrayList of Strings of system values 
	 */
	
	public static ArrayList getSystemValues(String key) throws SMRCException {
		
		HashMap systemMap = (HashMap)SMRCStaticObjects.get(SMRCStaticConst.CONFIG_SYSTEM_PROP);
		
		if (systemMap == null) {
			throw new SMRCException("No system values defined");
		}
		
		// Check and see if we have a local environment set up that needs to be appended as a value
		
		if (_localEnvironment == null) {
			
			checkEnvironmentPropertyFile();			
		}
		
		SystemProperty systemProperty = null;
		
		// First, check to see if there is a local version of the property and return that first
		
		if (_localEnvironment.length() > 0) {
			
			systemProperty = (SystemProperty)systemMap.get(_localEnvironment.toUpperCase() + "_" + key);
			
			if (systemProperty != null) {
				
				return systemProperty.getPropertyValues();
				
			}
			
		}
		
		// At this point, there is no property with local appended to it.  Therefore, check for the key without 
		// the local appended.  This would be a value that is either specifice to the deployment environment
		// or common between the local and server environments
		
		systemProperty = (SystemProperty)systemMap.get(key);		
		
		if (systemProperty == null) {
		
			throw new SMRCException("Unable to find any system values for key " + key);
		}
		
		return systemProperty.getPropertyValues();
		
		
	} // end getSystemValues
	
	/**
	 * Returns a single system value.  If there is more than one system value an exception is thrown
	 * 
	 * @param key Key for value
	 * @return System value
	 */
	
	public static String getSystemValueSingle(String key) throws SMRCException {
		
		ArrayList valueList = SMRCStaticObjects.getSystemValues(key);
		
		if (valueList.size() > 1) {
		
			throw new SMRCException("Single system value requested and multiple values found for key "  + key);
			
		}
		
		return (String)valueList.get(0);
		
		
	} // end getSystemValueSingle
	
	private static void addSystemValue(String key, SystemPropertyItem systemPropItem) throws SMRCException {
		
		HashMap systemMap = (HashMap)SMRCStaticObjects.get(SMRCStaticConst.CONFIG_SYSTEM_PROP);
		
		if (systemMap == null) {
			
			systemMap = new HashMap();
			SMRCStaticObjects.put(SMRCStaticConst.CONFIG_SYSTEM_PROP, systemMap);
			
		}
		
		SystemProperty currentProperty = (SystemProperty)systemMap.get(key);
		
		if (currentProperty == null) {
			
			currentProperty = new SystemProperty(key);
			
			currentProperty.addPropertyItem(systemPropItem);

			systemMap.put(key, currentProperty);
			
		} else {
			
			currentProperty.addPropertyItem(systemPropItem);

			
		}
		
	} // end addSystemValue
	
	/**
	 * Populates property file value.  If sixSigmaEnvironment.properties exists, then it gets the
	 * environment value from the file and stores it in the static variable.  This is then appended
	 * to the system look values to see if they exist
	 *
	 */
	
	private static void checkEnvironmentPropertyFile() {
		
		// Look for the property file
		
		try {
			
			ResourceBundle bundle = ResourceBundle.getBundle("com.eaton.ee.sixsigma.sixsigmaEnvironment");
			
			_localEnvironment = bundle.getString("environment");
			
		} catch (MissingResourceException mre) {
		
			// No error.  This just means that it is a server environment, not a local one
			// We set the value to the empty string so that we only do this check once in the getSystemValues
			// method.
			
			_localEnvironment = "";
		}
		
	} // end checkEnvironmentPropertyFile
	
	public static void resetStaticObject() throws SMRCException {
		
		_staticObjects = null;
		_staticObjects = new HashMap();
		
		SMRCStaticObjects.populateStaticMap();
		
	}
	
	public static boolean isPopulated() {
		
		if (_staticObjects != null) {
			
			return true;
			
		}
		
		return false;
		
		
	}
	
}
