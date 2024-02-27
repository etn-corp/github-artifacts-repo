<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ include file="analytics.jsp" %>

<%
//String path = request.getContextPath();
//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

//OEMAcctPlanBean OEMAPVars = (OEMAcctPlanBean)request.getAttribute("variables");
//request.setAttribute("header", OEMAPVars.getHeader());
%>

		<form name=searchCusts action=OEMAcctPlan>
		<input type=hidden name=groupId value=<%= sGroup.getId() %>>
		<input type=hidden name=page value=getCusts>

		<table align=center cellspacing=1 cellpadding=1 border=0 class=tableBorder>
		<tr class=cellColor>
			<td>Customer Number</td>
			<td><input name=custNum size=6 maxlength=6></td>
		</tr>
		<tr class=cellColor>
			<td>Customer Name</td>
			<td><input name=custName size=35 maxlength=35></td>
		</tr>
		<tr class=cellColor>
			<td>Salesman (Last Name / First Name)</td>
			<td><input name=salesLName size=20 maxlength=35>
			<input name=salesFName size=20 maxlength=35></td>
		</tr>
		<tr class=cellColor>
			<td colspan=2><div class=addCust>OR</div></td>
		</tr>
		<tr class=cellColor>
			<td>Customers modified in last "X" days (enter "X")</td>
			<td><input name=moddays size=5 maxlength=10></td>
		</tr>
		<tr class=cellColor>
			<td align=center colspan=2>Tip - use '*' as a wild card
			<br><input type=submit name=submit value='Search'></td>
		</tr>
		</table>


		</form>
