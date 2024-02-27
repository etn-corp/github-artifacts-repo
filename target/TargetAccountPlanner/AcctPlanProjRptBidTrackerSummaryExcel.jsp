<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="com.eaton.electrical.smrc.bo.*" %>
<%@ page contentType="application/msexcel"%>
<html>
	<%
		response.setContentType("text/txt");
		response.setHeader("Content-Disposition", "attachment; filename=\"tapreport.xls\"");
		StringBuffer filters = (StringBuffer) request.getAttribute("filters");
	    BidTrackerSummaryResults results = (BidTrackerSummaryResults) request.getAttribute("results");
	    StringBuffer sortParms = (StringBuffer) request.getAttribute("sortParms");
	
	%>
	
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="750" valign="top">
				<p><b>Bid Tracking Results</b></p>
				<p><b>Filter: </b><%= filters.toString() %> </p>
				<table width="100%" cellspacing=1 cellpadding=1>
					<tr></tr>
					<tr>
						<%		// Results go here        %>
						<td valign=top>
							<table cellspacing=1 cellpadding=1 width="100%">
								<thead>
									<td valign="top"><b><u>Total Created</u></b></td>
									<td valign="top"><b><u>Total Obtained</u></b></td>
									<td valign="top"><b><u>Neg Hit Rate</u></b></td>
									<td valign="top"><b><u>Bid Dollars Created</u></b></td>
									<td valign="top"><b><u>Bid Dollars Obtained</u></b></td>
									<td valign="top"><b><u>% Dollars Obtained</u></b></td>
								</thead>
								<tr>
									<td><%=  results.getTotalCntForDisplay() %></td>
									<td><%= results.getObtainedCntForDisplay() %></td>
									<td><%= results.getNegHitRate() %></td>
									<td><%= results.getTotalDolForDisplay() %></td>
									<td><%= results.getObtainedDolForDisplay() %></td>
									<td><%= results.getNegDolRate() %></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td></tr>
	</table>
	</body>
</html>