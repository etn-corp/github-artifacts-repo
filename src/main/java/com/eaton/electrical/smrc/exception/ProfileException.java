package com.eaton.electrical.smrc.exception;

/**
 * @author E0062708
 *
 */
public class ProfileException extends Exception{

	private static final long serialVersionUID = 100;
	
	public ProfileException()
    {
		super("JOE or Directory Profile Exception") ;
    }
    public ProfileException(String msg)
    {
		super( msg ) ;
    }

}