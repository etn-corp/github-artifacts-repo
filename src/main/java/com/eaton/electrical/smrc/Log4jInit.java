//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.5  2004/10/16 18:15:30  manupvj
// Added abstract servlet for common functions and common point of logging.
//
// Revision 1.4  2004/10/15 04:14:49  vendejp
// *** empty log message ***
//
// Revision 1.2  2004/10/14 17:40:50  schweks
// Added Eaton header comment.
// Reformatted source code.
//
// Revision 1.1  2004/10/11 21:21:54  schweks
// This is the log4j initializer Servlet.
//

package com.eaton.electrical.smrc;

import java.io.*;
import java.util.ResourceBundle;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.*;

/**
 * This class handles initializing the logging properties file.
 *
 * @author  schweks
 * @version 1.0
 */
public class Log4jInit extends HttpServlet {

	private static final long serialVersionUID = 100;

	public void init() throws ServletException {
		String file = getInitParameter( "log4j-init-file" );
		// if the log4j-init-file is not set, then no point in trying
		if (file != null) {
			PropertyConfigurator.configure(file);
		}
		super.init();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
	}
}