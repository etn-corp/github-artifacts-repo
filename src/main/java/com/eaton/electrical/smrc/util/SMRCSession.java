
package com.eaton.electrical.smrc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import javax.servlet.http.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.exception.ProfileException;
import com.eaton.electrical.smrc.service.*;
import com.eaton.portal.sso.SSOAttribute;
import com.eaton.portal.sso.SSOPortalUserFactory;
import com.eaton.portal.sso.TagValue;


/**
 * @author E0062708
 *
 */
public class SMRCSession {
	
	public static SessionProfile getSessionProfile (HttpServletRequest request) throws ProfileException, Exception
	
	{
		User user = new User();
		String userID = ""; 
		String fName = "";
		String lName = "";
		String email = "";
	    String vistaID = ""; 
	    String street = ""; 
	    String city = ""; 
	    String state = ""; 
	    String zipCode = ""; 
	    String country = ""; 

	    InputStream inputStream = SMRCSession.class.getResourceAsStream("/com/eaton/electrical/smrc/conf/TapPortal.xml");
	    
	    try {
	      //  File xmlFile = File.createTempFile("taptmpfile", ".xml");
	      //  FileOutputStream writer = new FileOutputStream(xmlFile);
	        
	        	        
	       /* SSOAttribute[] attributes = SSOPortalUserFactory.getSSOAttributes(request,inputStream,0);
	        //xmlFile.delete();

	        
			if (attributes != null){
				for (int i=0; i < attributes.length; i++)
				{
					//log.debug("SSOAttribute[" + i + "] = " + attributes[i].getTagName() + " and the value is " + attributes[i].getTagValue());
					//System.out.println("SSOAttribute[" + i + "] = " + attributes[i].getTagName() + " and the value is " + attributes[i].getTagValue().getValue().toUpperCase());
					if (attributes[i].getTagName().equalsIgnoreCase(SSOAttribute.PORTAL_COOKIE_USER_ID_TEXT)) {
						user.setUserid((String)attributes[i].getTagValue().getValue().toUpperCase());
					}    	

					if (attributes[i].getTagName().equalsIgnoreCase(SSOAttribute.PORTAL_COOKIE_USER_FIRSTNAME_TEXT)) {
						user.setFirstName((String)attributes[i].getTagValue().getValue());
					}    	

					if (attributes[i].getTagName().equalsIgnoreCase(SSOAttribute.PORTAL_COOKIE_USER_LASTNAME_TEXT)) {
						user.setLastName((String)attributes[i].getTagValue().getValue());
					}    	

					if (attributes[i].getTagName().equalsIgnoreCase(SSOAttribute.PORTAL_COOKIE_USER_EMAIL_TEXT)) {
						user.setEmailAddress((String)attributes[i].getTagValue().getValue());
					}    	  	
				}	    	
			}*/
	    	user.setUserid("E0623366");
	    	user.setFirstName("Kunal");
	    	user.setLastName("Khairnar");
	    	user.setEmailAddress("kunalvkhairnar@eaton.com");
	    	
			TransferBO t = new TransferBO();
	    	LDAPAuth la = new LDAPAuth();
	    	t = la.getProfile(user);
	    	
		    fName = t.getU().getFirstName();
			lName = t.getU().getLastName();
			email = t.getU().getEmailAddress();
		    vistaID = t.getU().getVistaId(); 
		    userID = t.getU().getUserid();
		    street = t.getA().getAddress1(); 
		    city = t.getA().getCity(); 
		    state = t.getA().getState(); 
		    zipCode = t.getA().getZip(); 
		    country = t.getA().getCountryName(); 
	    	
			return new SessionProfile( userID, email, lName, fName, vistaID, street, city, state, zipCode, country);
			
	    } catch ( ProfileException pe ) {
		    
			ResourceBundle rb = ResourceBundle.getBundle("com.eaton.electrical.smrc.SMRC");
			
			String testHost = rb.getString("TEST_HOST");
			userID = rb.getString("TEST_USER");
			email = rb.getString("TEST_EMAIL");
			fName = rb.getString("TEST_FIRST_NAME");
			lName = rb.getString("TEST_LAST_NAME");
			vistaID = rb.getString("TEST_VISTA_ID");
		    street = rb.getString("TEST_STREET"); 
		    city = rb.getString("TEST_CITY"); 
		    state = rb.getString("TEST_STATE"); 
		    zipCode = rb.getString("TEST_ZIPCODE"); 
		    country = rb.getString("TEST_COUNTRY"); 

			if (request.getHeader("host").equals( testHost )) {
				return new SessionProfile( userID, email, lName, fName, vistaID, street, city, state, zipCode, country );
			}

			// User does not have access to the application
			throw pe;
		}
	    catch ( Exception e ) 
		{
		    SMRCLogger.error("SMRCSession.getSessionProfile() ", e) ;
		    throw e;
	    
	    
		}
	    finally
	    {	    	
	    if(inputStream!=null)inputStream.close();
	    }
	}
	
	//	getNewUser forces to get a new user object and replace what is in session.
	public static User getUser(HttpServletRequest request,boolean getNewUser,Connection DBConn) throws ProfileException, Exception{
		HttpSession session = request.getSession(true) ;
		if(session.getAttribute("usr")!=null && !getNewUser){
			return (User)session.getAttribute("usr");
		}
		SessionProfile sessionProfile = getSessionProfile(request);
		User usr = UserDAO.getUser(sessionProfile.getUserid(), DBConn);
		session.setAttribute("usr",usr);
		return usr;
	}
	
	// if user doest pass in the boolean for "get New user"
	// the default behavior is to check session first
	public static User getUser(HttpServletRequest request,Connection DBConn) throws Exception{
		return getUser(request,false,DBConn);
	}
	
	
	// getUserOverride is for the UserOverride servlet to set the user based on userid
	public static User getUserOverride(HttpServletRequest request,Connection DBConn) throws Exception{
		HttpSession session = request.getSession(true) ;
		User usr = UserDAO.getUser(request.getParameter("userid"), DBConn);
		session.setAttribute("usr",usr);
		return usr;	
	}
	
	public static void setUser(HttpServletRequest request, User usr) throws Exception{
		HttpSession session = request.getSession(true) ;
		session.setAttribute("usr",usr);
	}
}
