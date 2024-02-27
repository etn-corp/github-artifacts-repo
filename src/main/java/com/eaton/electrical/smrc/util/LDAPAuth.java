package com.eaton.electrical.smrc.util;

import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;


import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.service.SMRCLogger;

public class LDAPAuth {
	private static ResourceBundle smrcResourceBundle = null;
	
    private String employeeSearchBase = "";
    private String employeeSearchFilter = "";
    private String contractorSearchBase = "";
    private String contractorSearchFilter = "";
	
	public TransferBO getProfile(User u) throws Exception {
		TransferBO t = new TransferBO();
		Address a = new Address();
		
		String userId = u.getUserid();
		
		try {
			smrcResourceBundle = ResourceBundle.getBundle("com.eaton.electrical.smrc.TapEdi");
			
		    employeeSearchBase = smrcResourceBundle.getString("employeeSearchBase");
		    employeeSearchFilter = smrcResourceBundle.getString("employeeSearchFilter");
		    contractorSearchBase = smrcResourceBundle.getString("contractorSearchBase");
		    contractorSearchFilter = smrcResourceBundle.getString("contractorSearchFilter");
		    
			SearchResult findEntry = null;
			
			String filter = employeeSearchFilter + "=" + userId.trim();
	        SearchControls constraints = new SearchControls();
	        constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
	 
        	Hashtable env = new Hashtable();
            	env.put(Context.INITIAL_CONTEXT_FACTORY, smrcResourceBundle.getString("INITIAL_CONTEXT_FACTORY")); 
            	env.put(Context.PROVIDER_URL, smrcResourceBundle.getString("PROVIDER_URL"));
            	env.put(Context.SECURITY_AUTHENTICATION, smrcResourceBundle.getString("SECURITY_AUTHENTICATION"));
            	env.put(Context.SECURITY_PRINCIPAL, smrcResourceBundle.getString("SECURITY_PRINCIPAL"));
            	env.put(Context.SECURITY_CREDENTIALS, smrcResourceBundle.getString("SECURITY_CREDENTIALS"));
	        
			InitialDirContext ldapCtx = new InitialDirContext(env);
			NamingEnumeration res = ldapCtx.search(employeeSearchBase, filter, constraints);
			
			SMRCLogger.info(res);
			System.out.println(res);
			
			if (res.hasMoreElements() != false) findEntry = (SearchResult)res.next();
			if (findEntry == null) {
				filter = contractorSearchFilter + "=" + userId.trim();
				res = ldapCtx.search(contractorSearchBase, filter, constraints);
				if (res.hasMoreElements() != false) {
					findEntry = (SearchResult)res.next();
				}
			}
			
			Attributes las = findEntry.getAttributes();
		    
	        for (NamingEnumeration ae = las.getAll(); ae.hasMoreElements();) {
	        	Attribute la = (Attribute)ae.next();
	        	String attrId = la.getID();
	        	
	 			if (la.getID().equalsIgnoreCase("givenname")) {
	 				u.setFirstName(getLDAPAttribStringValue(la));
	 			}
				if (la.getID().equalsIgnoreCase("sn")) {
					u.setLastName(getLDAPAttribStringValue(la));
				}
				if (la.getID().equalsIgnoreCase("eatonICCVistaUid")) {
					u.setVistaId(getLDAPAttribStringValue(la));
				}
				if (la.getID().equalsIgnoreCase("mail")) {
					u.setEmailAddress(getLDAPAttribStringValue(la));
				}
				
				if (la.getID().equalsIgnoreCase("street")) {
					a.setAddress1(getLDAPAttribStringValue(la));
				}
				if (la.getID().equalsIgnoreCase("postalCode")) {
					a.setZip(getLDAPAttribStringValue(la));
				}
				if (la.getID().equalsIgnoreCase("st")) {
					a.setState(getLDAPAttribStringValue(la));
				}
				if (la.getID().equalsIgnoreCase("city")) {
					a.setCity(getLDAPAttribStringValue(la));
				}
				if (la.getID().equalsIgnoreCase("country")) {
					a.setCountryName(getLDAPAttribStringValue(la));
				}
	        }
			t.setA(a);
			t.setU(u);
		} catch(Exception e) {
		    SMRCLogger.error("LDAPAuth.getProfile() ", e) ;
		    throw e;
		}
		
		return t;
	}
	
	
	 private String getLDAPAttribStringValue(Attribute la) {
		String value = "";
		try{
			Enumeration enm = la.getAll();
			if (enm.hasMoreElements()) value = (String) enm.nextElement();
		} catch(Exception e) {
			SMRCLogger.error("LDAPAuth.getLDAPAttribStringValue ", e) ;
		}
	
		return value;
     }
}
