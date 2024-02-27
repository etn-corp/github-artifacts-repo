<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="com.eaton.electrical.smrc.bo.*" %>
<html>
	<%@ include file="./TAPheader.jsp" %>
	<%@ include file="analytics.jsp" %>
	<%
	    StringBuffer filters = (StringBuffer) request.getAttribute("filters");
	    BidTrackerSummaryResults results = (BidTrackerSummaryResults) request.getAttribute("results");
	    StringBuffer sortParms = (StringBuffer) request.getAttribute("sortParms");
	    String searchString = request.getQueryString();
	
	%>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750">
				<p><br>
					<a class="crumb" href="SMRCHome">Home Page</a>
					<span class="crumbarrow">&gt;</span>
					<a class="crumb" href="AcctPlanProjRpt?page=projects">Project Reports</a>
					<span class="crumbarrow">&gt;</span>
					<span class="crumbcurrent">Bid Tracking Results</span>
				</p>
			</td>
		</tr>
	</table>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750" valign="top">
			<p>&nbsp;</p>
				<p class=heading2>Bid Tracking Results</p>
				<a href="AcctPlanProjRpt?<%= (searchString + "&excel=y") %>" target="blank.html">Export To Excel</a>
				<table width="100%" class=tableBorder cellspacing=1 cellpadding=1>
					<thead class=innerColor>
						<td class=heading3 width="10%">Filters</td>
						<td class=heading3 width="90%">Results</td>
					</thead>
					<tr class=cellColor>
						<%		// Filters go here   %>
						<td valign=top><table width="100%">
								<%= filters.toString() %>
							</table>
						</td>
						<%		// Results go here        %>
						<td valign=top>
							<table class=innerBorder cellspacing=1 cellpadding=1 width="100%">
								<thead class=innerColor>
									<td class=searchRight2 valign="top">Total Created</td>
									<td class=searchRight2 valign="top">Total Obtained</td>
									<td class=searchRight2 valign="top">Neg Hit Rate</td>
									<td class=searchRight2 valign="top">Bid Dollars Created</td>
									<td class=searchRight2 valign="top">Bid Dollars Obtained</td>
									<td class=searchRight2 valign="top">% Dollars Obtained</td>
								</thead>
								<tr class=cellColor>
									<td class=smallFontR><%=  results.getTotalCntForDisplay() %></td>
									<td class=smallFontR><%= results.getObtainedCntForDisplay() %></td>
									<td class=smallFontR><%= results.getNegHitRate() %></td>
									<td class=smallFontR><%= results.getTotalDolForDisplay() %></td>
									<td class=smallFontR><%= results.getObtainedDolForDisplay() %></td>
									<td class=smallFontR><%= results.getNegDolRate() %></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<br>
				<div class="textnormal">Click <a href="AcctPlanProjRpt?viewDetails=Y<%= sortParms.toString() %>">Here</a> to view the details behind these number</div>
			</td></tr>
	</table>
	</body>
</html>
