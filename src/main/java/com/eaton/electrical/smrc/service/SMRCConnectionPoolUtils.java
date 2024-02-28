package com.eaton.electrical.smrc.service;

import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.eaton.electrical.smrc.exception.SMRCException;

/**
 * Database connection that uses the connection pool from the application server.  If you are
 * running this in Tomcat, you need to make an entry in the Context for the server in 
 * server.xml.  This version does not use the property files in any way to get the connection.
 * This allows deployment of a single war over all environments.
 * 
 * @author Braffet
 * 
 */

public class SMRCConnectionPoolUtils {
    
	/**
	 * Gets the database from the connection pool.  This pool is provided by the app server.
	 * 
	 * @return Database connection from pool
	 * @throws SMRCException
	 */
    public static Connection getDatabaseConnection () throws Exception {
    	try {
			
			InitialContext jndiCntx = new InitialContext();
			DataSource ds;
			try {
				jndiCntx = new InitialContext();
			} catch (NamingException e) {
				System.out.println("getConnection()---Error occurred getting JNDICNTX");
				e.printStackTrace();
				throw e;
			}  
			
			try {
				ds = (DataSource)jndiCntx.lookup("java:/datasources/tapDB");
			} catch (NamingException e) {
				System.out.println("getConnection()---Error occured while doing the LOOKUP");
				e.printStackTrace();
				throw e;
			}      
			Connection con = ds.getConnection(); 
			con.setAutoCommit(false);
			return con;
		} catch (Exception sqle) {
			System.out.println("getConnection()---GENERAL");
			sqle.printStackTrace();
			throw sqle;
		}
    }
	
	
	/**
	 * Starts a transaction by setting the autocommit to false allowing a new transaction to start
	 * 
	 * @param con Database connection that is to run the transaction
	 * @return Returns false if connection is null or if the commit level could not be set
     * @throws SMRCException
	 */
    
    public static boolean startTransaction (Connection con) throws SMRCException {
        if ( con == null ) return false ;
        boolean result = true ;
        try {
            con.setAutoCommit ( false ) ;
        }
        catch(Exception e) {
        	throw new SMRCException("Error occured while starting a transaction", e);       
        }
        return result ;
    }

    
    /**
     * Commits the transaction on the passed in database connection
     * 
     * @param con Database connection that contains the transaction to commit
     * @return Returns false if connection is false or if an exception occurs when commiting
     * @throws SMRCException
     **/
    
    public static boolean commitTransaction (Connection con) throws SMRCException {
        if ( con == null ) return false ;
        boolean result = true ;
        try {
        	con.setAutoCommit(false);
            con.commit () ;
            con.setAutoCommit ( true ) ;
            SMRCLogger.debug("SMRCConnectionPoolUtils.commitTransaction()" );
        }
        catch(SQLException e) {
        	System.out.println(e);
        	throw new SMRCException("Error occured while commiting a transaction");
        }
        return result ;
    }
    
    /**
     * Rolls back a transaction on the passed in database connection. <br><br>NOTE: No exception is thrown if there is an internal exception but there is an output to the log
     * 
     * @param con Database connection that contains the transaction to rollback
     * @return Returns false if connection is null
     * @throws SMRCException
     */
    
    public static boolean rollbackTransaction (Connection con) {
        if ( con == null ) return false ;
        boolean result = true ;
        try {
            con.rollback () ;
            con.setAutoCommit (true) ;
            SMRCLogger.debug("SMRCConnectionPoolUtils.rollbackTransaction()");
        }
        catch(Exception e) {
        	new SMRCException("Error occured while rolling back transaction", e);
        }
        return result ;
    }


    /**
     * Closes passed in connection.  <br><br>NOTE: No exception is thrown if there is an internal exception but there is an output to the log
     * 
     * @param con Database connection to close.  This just returns the connection to the pool
     */
    public static void close ( Connection con ) {
        try {
            if ( con!= null) {
               con.close () ;
            }
        }
        catch(Exception e) {
            new SMRCException("Error while closing connection ", e);
        }
    }
	
    /**
     * Closes the passed in statement.  <br><br>Note : No exception is thrown if there is an internal exception but there is an output to the log
     * 
     * @param statement Statement to be closed
     */
    
    public static void close(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		}
		catch (Exception e) {
		   new SMRCException("Error while closing statement", e);
		}
	}    
    
    /**
     * Closes the passed in result set.  <br><br>Note : No exception is thrown if there is an internal exception but there is an output to the log
     * 
     * @param resultSet
     */
	public static void close(ResultSet resultSet) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
		}
		catch (Exception e) {
		    new SMRCException("Error while closing result set", e);
		}
	} //method

	
}
