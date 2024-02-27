package com.eaton.electrical.smrc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*; 


import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.service.SMRCConnectionPoolUtils;

public class ResetWorkflow extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ResetWorkflow() {
		super();
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection DBConn = null;
		
		String step = request.getParameter("workflowStepName");
		String pNum = request.getParameter("acctId");
		
		try {
			DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();
			WorkflowDAO.resetWorkflow(step, pNum, DBConn);
			DBConn.commit();
		} catch (Exception e) {
			System.out.println(e);
		} 
		
		//RequestDispatcher dispatcher = getServletContext().getRequestDispatcher( "/AccountProfile?page=workflowApprove&acctId="+pNum );
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/PendingApprovals");
		dispatcher.forward( request, response );


	}

}
