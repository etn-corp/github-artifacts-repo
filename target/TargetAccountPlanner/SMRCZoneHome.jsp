<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ include file="analytics.jsp" %>
<%
	String zone = (String) request.getAttribute("zone");
    String salesOrders = (String) request.getAttribute("salesOrders");
    String zoneName = (String) request.getAttribute("zoneName");
    boolean canSeeApprovalLink = ((Boolean) request.getAttribute("canSeeApprovalLink")).booleanValue();
    int approvalsPending = ((Integer) request.getAttribute("approvalsPending")).intValue();

%>
<html>
	<%@ include file="./SMRCHeader.jsp" %>
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
	<br>
		<table width="760" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="10">&nbsp;</td>
				<td width="750" valign="top">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="3"><span class="heading2"><%= zoneName %></span></td>
							<td align="center">
								<%  if (salesOrders.equalsIgnoreCase("Orders")){ %>
								<div align="right"><a href="SMRCHome?salesorders=Sales">Show Sales</a></div>
								<% } else { %>
								<div align="right"><a href="SMRCHome?salesorders=Orders">Show Orders</a></div>
								<% } %>
							</td>
						</tr>
						<% if (canSeeApprovalLink) { %>
						    <tr>
								<td colspan="4" align="right"><span><a href="PendingApprovals">You have <%= approvalsPending %> requests for approval</a></span>
							</tr>
					      <% } %>
						
					</table>
					<br>
					<center>Please be patient while charts are loading...</center><br>
					<table width="750" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td>
								<%@ include file="./zoneHomeMap.jsp" %>
							</td>
							<!--<td width="2">
								&nbsp;
							</td>-->
							<td height="325" valign="top"><iframe src="zoneHomeChart1.jsp" frameborder=0 scrolling=no width="374" height="325"></iframe>
							</td>
						</tr>
					<%--
					</table>
					<table width="750" border="0" cellspacing="0" cellpadding="0">
					--%> 
						<!--<tr>
							<td colspan="4">&nbsp;</td>
						</tr>-->
						
						<tr>
							<td width="374" height="285" valign="top"><iframe src="zoneHomeChart2.jsp" frameborder=0 scrolling=no width="374" height="285"></iframe>
							</td>
							
							<td width="374" height="285" valign="top"><iframe src="zoneHomeChart3.jsp" frameborder=0 scrolling=no width="374" height="285"></iframe>
							</td>
						</tr>
						
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						
						<tr>
							<td width="374" height="285" valign="top"><iframe src="zoneHomeChart4.jsp" frameborder=0 scrolling=no width="374" height="285"></iframe>
							</td>
							
							<td width="374" height="285" valign="top"><iframe src="zoneHomeChart5.jsp" frameborder=0 scrolling=no width="374" height="285"></iframe>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br>&nbsp;
		<br>&nbsp;
		<br>&nbsp;
		<br>&nbsp;
		<br>&nbsp;								
		</body>
</html>
