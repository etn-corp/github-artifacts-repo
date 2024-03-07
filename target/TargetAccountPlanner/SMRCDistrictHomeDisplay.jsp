<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*, java.text.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%
String district=(String)session.getAttribute("district");
String districtName = (String) request.getAttribute("districtName");
String salesOrders = (String) request.getAttribute("salesOrders");
boolean zoneManager = ((Boolean) request.getAttribute("zoneManager")).booleanValue();
boolean canSeeApprovalLink = ((Boolean) request.getAttribute("canSeeApprovalLink")).booleanValue();
int approvalsPending = ((Integer) request.getAttribute("approvalsPending")).intValue();

%>
<html>
	<%@ include file="./SMRCHeader.jsp" %>
	<%@ include file="analytics.jsp" %>
	<%
	User user = header.getUser();
	%>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750">
				<p><br>
				<a class="crumb" href="SMRCHome">Home Page</a>
				<!-- <span class="crumbarrow">&gt;</span><span class="crumbcurrent">Current Page</span></p> -->
			</td>
		</tr>
	</table>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750" valign="top">
				<p>&nbsp;</p>
				<p class="heading2">
					District Home
				</p>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
					<% if (canSeeApprovalLink) { %>
					    <td width="89%"><span><a href="PendingApprovals">You have <%= approvalsPending %> requests for approval</a></span></td>
				    <% } else { %>
						<td width="89%">&nbsp;</td>
					<% } %>
						<td width="11%">
							<%
							String districtQuery = "";
							if (zoneManager){
								districtQuery = "&district=" + district;
							}
							if (salesOrders.equalsIgnoreCase("orders")){
							%>
							<div align="right"><a href="SMRCHome?salesorders=sales<%= districtQuery %>">Show Sales</a></div>
							<% } else { %>
							<div align="right"><a href="SMRCHome?salesorders=orders<%= districtQuery %>">Show Orders</a></div>
							<% } %>
						</td>
					</tr>
				</table>
				<br>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
					<td width="100%"><a href="CSF?page=District&district=<%= district %>">Critical Success Factors</a></td>
					<tr>
						<td><a href="AccountProfile">Add New Account</a></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
				</table>
				<br>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><%= districtName %></td>
					</tr>
					<tr>
						<td valign="top"><iframe src="DistrictHomeSalesTable?salesorders=<%= salesOrders %>" frameborder=0 scrolling=no width="750" height="250"></iframe>
						</td>
					</tr>
				</table>
				<hr width="100%" noshade color="#DDDDDD">
				<br>
				<center>Please be patient while charts are loading...</center>
				<table width="100%" border="0" cellspacing="1" cellpadding="1" >
					<tr>
						<td width="300" height="195" valign="top"><iframe src="districtHomeChart1.jsp?salesOrders=<%= salesOrders %>" frameborder=0 scrolling=no width="350" height="215"></iframe>
						</td>
						<td width="300" height="195" valign="top"><iframe src="districtHomeChart2.jsp?salesOrders=<%= salesOrders %>" frameborder=0 scrolling=no width="350" height="215"></iframe>
						</td>
					</tr>
					<tr>
						<td width="300" height="195" valign="top"><iframe src="districtHomeChart3.jsp?salesOrders=<%= salesOrders %>" frameborder=0 scrolling=no width="350" height="215"></iframe>
						</td>
						<td width="300" height="195" valign="top"><iframe src="districtHomeChart4.jsp?salesOrders=<%= salesOrders %>" frameborder=0 scrolling=no width="350" height="215"></iframe>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<p>&nbsp;</p>
	</body>
</html>
