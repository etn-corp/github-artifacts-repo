//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.3  2005/01/05 21:12:36  schweks
// Updated for test users so when an invalid E# is used in search that an Exception is not thrown, just returns blank info.
//
// Revision 1.2  2004/11/19 21:42:21  schweks
// Split the db password info to another properties file.
//
// Revision 1.1  2004/11/15 07:31:56  schweks
// DAO to handle getting information from ED.
//

package com.eaton.electrical.smrc.dao;

import java.util.*;

import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.ldap.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.service.SMRCLogger;

/**
 * This class represents access to the Eaton Enterprise Directory
 * 
 * @author E0027146
 *
 */
public class EnterpriseDirectoryDAO {
    
    public static Address getAddressForUser( String aUserID ) {

        Address myAddress = null;
        
        try {
            // Specify the ids of the attributes to return
            // street=street, l=city, st=state, postalCode=zip, c=country
            String[] attrIDs = { "street", "l", "st", "postalCode", "c" };
            SearchControls ctls = new SearchControls();
            ctls.setReturningAttributes(attrIDs);

            // Search the directory
            NamingEnumeration dirResults = search( "SUBTREE", "o=eaton.com", "(uid=" + aUserID + ")", ctls );
            
            // if for some reason the user is not found in the ED, return a blank address
            if ( dirResults==null ||
            	 !dirResults.hasMoreElements() ) {
            	return new Address();
            }
            
            // Due to the nature of the directory, only one record will be returned (because of unique UIDs)
            SearchResult result = ( SearchResult )dirResults.next(); // only one result since uid's are unique

            String street = getFieldValue(result.getAttributes(), "street");
            String city = getFieldValue(result.getAttributes(), "l");
            String state = getFieldValue(result.getAttributes(), "st");
            String zipCode = getFieldValue(result.getAttributes(), "postalCode");
            String country = getFieldValue(result.getAttributes(), "c");
            
            myAddress = new Address();
            myAddress.setAddress1( street );
            myAddress.setCity( city );
            myAddress.setState( state );
            myAddress.setZip( zipCode );
            myAddress.setCountry( country );
            
        } catch (NamingException e) {
			SMRCLogger.warn("EnterpriseDirectoryDAO.getAddressForUser(): ", e);
        }

        return myAddress;
    }

    public static String getVistaIDForUser( String aUserID ) {
        String myVistaID = null;
        
        try {
            // Specify the ids of the attributes to return
            // eatonICCVistaUid=Vista ID
            String[] attrIDs = { "eatonICCVistaUid" };
            SearchControls ctls = new SearchControls();
            ctls.setReturningAttributes(attrIDs);

            // Search the directory
            NamingEnumeration dirResults = search( "SUBTREE", "o=eaton.com", "(uid=" + aUserID + ")", ctls );
            // Due to the nature of the directory, only one record will be returned (because of unique UIDs)
            SearchResult result = ( SearchResult )dirResults.next(); // only one result since uid's are unique

            myVistaID = getFieldValue(result.getAttributes(), "eatonICCVistaUid");

        } catch (NamingException e) {
			SMRCLogger.warn("EnterpriseDirectoryDAO.getVistaIDForUser(): ", e);
        }

        return myVistaID;
    }

    /**
     * Get an initial context.  If the principal and credentials are
     * non-null, authentication is performed.
     *
     * @throws MissingResourceException - when a missing resource exception occurs
     * @throws NamingException - when a naming exception occurs
     */
    private static InitialLdapContext getContext() throws MissingResourceException, NamingException {
        
        ResourceBundle smrcProperties = ResourceBundle.getBundle( "com.eaton.electrical.dbinfo.smrc.db" );

        String principal = smrcProperties.getString( "ed.principal" );
        String credentials = smrcProperties.getString( "ed.credentials" );
        String server = smrcProperties.getString( "ed.server" );
        String port = smrcProperties.getString( "ed.port" );

        Hashtable env = new Hashtable();

        env.put( Context.INITIAL_CONTEXT_FACTORY, getInitialContextFactory() );
        String edURL = "ldap://" + server + ":" + port;
        env.put( Context.PROVIDER_URL, edURL );

        if ( ( principal != null ) && ( credentials != null ) ) {
            env.put( Context.SECURITY_PRINCIPAL, principal );
            env.put( Context.SECURITY_CREDENTIALS, credentials );
        }

        return new InitialLdapContext( env, null );

    } // method getContext

    private static String getInitialContextFactory() {
        return "com.sun.jndi.ldap.LdapCtxFactory";
    }

    /**
     * Perform a search on the database.  If <code>anAuthenticationFlag</code>
     * is <code>true</code>, perform authentication.
     *
     * @param aSearchScope the <code>String</code> specifying the search scope.
     * The valid values are "SUBTREE", "ONELEVEL", and "OBJECT".
     * @param aSearchBase the <code>String</code> specifying the starting
     * point for this search operation.
     * @param aSearchFilter the <code>String</code> specifying an LDAP search filter.
     * @param aSearchControls the <code>SearchControls</code> that set limits
     * on this search.  If this is set to <code>null</code>, use the default
     * constraints.
     *
     * @throws NamingException - when a naming exception occurs
     */

    private static NamingEnumeration search( String aSearchScope, String aSearchBase,
        String aSearchFilter, SearchControls aSearchControls ) throws NamingException {

        SearchControls constraints = null;
        NamingEnumeration searchResults = null;
        InitialLdapContext context = null;
        
        try {

	        context = getContext();
	
	        // if we are given null for the search controls parameter,
	        // just use the defaults
	        if ( aSearchControls == null ) {
	            constraints = new SearchControls();
	        } else {
	            constraints = aSearchControls;
	        }
	
	        if ( aSearchScope.equalsIgnoreCase( "SUBTREE" ) ) {
	            constraints.setSearchScope( SearchControls.SUBTREE_SCOPE );
	        } else if ( aSearchScope.equalsIgnoreCase( "ONELEVEL" ) ) {
	            constraints.setSearchScope( SearchControls.ONELEVEL_SCOPE );
	        } else if ( aSearchScope.equalsIgnoreCase( "OBJECT" ) ) {
	            constraints.setSearchScope( SearchControls.OBJECT_SCOPE );
	        } else {
	            throw new IllegalArgumentException( "Invalid search scope. "
	            + aSearchScope + "\nThe valid search scopes are "
	            + "SUBTREE, ONELEVEL, and OBJECT." );
	        }
	
	        searchResults = context.search( aSearchBase, aSearchFilter, constraints );
	
        } finally {
	        context.close();
	        context = null;
        }
	
        return searchResults;
    }

    private static String getFieldValue(Attributes attrs, String key) throws NamingException {

        if (attrs == null || key == null) {
            return " ";
        }

        Attribute attr = attrs.get(key);

        if (attr == null) {
            return " ";
        }

        Object result = attr.get();

        if (result == null) {
            return " ";
        }

        return result.toString();
    }

}
