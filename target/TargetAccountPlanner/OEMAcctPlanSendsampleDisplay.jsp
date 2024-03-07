<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="java.math.*"%>
<%@ page import="java.text.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

OEMAcctPlanBean OEMAPVars = (OEMAcctPlanBean)request.getAttribute("variables");
request.setAttribute("header", OEMAPVars.getHeader());

%>
<HTML>
	<%@ include file="./TAPheader.jsp" %>
	
		Your request has been processed.  <br><br>
		You should receive a copy of the email that has been sent to the appropriate
		Pricing Manager.<br><br>
		If you do not receive this email within the next hour, please
		notify this site's administrator at <a href=mailto:oemacctplanner@eaton.com>
		oemacctplanner@eaton.com</a>
		
		
</BODY>
</HTML>