<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="com.eaton.electrical.smrc.bo.*" %>
<%@ page import="java.math.*" %>
<%@ page import="java.text.*" %>
<html>
	<%@ include file="./TAPheader.jsp" %>
	<%@ include file="analytics.jsp" %>
	
	<%
	    StringBuffer filters = (StringBuffer) request.getAttribute("filters");
	    StringBuffer sortParms = (StringBuffer) request.getAttribute("sortParms");
	    ArrayList results = (ArrayList) request.getAttribute("results");
	    BigDecimal bidDol = (BigDecimal) request.getAttribute("bidDol");
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
				<table width="100%" class=tableBorder cellspacing=1 cellpadding=1 align=center>
					<thead class=innerColor>
						<td class=heading3 width="10%">Filters</td>
						<td class=heading3 width="90%">Results</td>
					</thead>
					<tr class=cellColor>
						<% // Filters go here   %>
						<td valign=top><table width="100%">
								<%= filters.toString() %>
							</table>
						</td>
						<%		// Results go here   %>
						<td valign=top>
							<table class=innerBorder cellspacing=1 cellpadding=1 width="100%">
								<thead class=innerColor>
									<td valign="top"><a href="AcctPlanProjRpt?sortMeth=neg_num<%= sortParms.toString() %>"><span class=tocactive>Neg Number</span></a></td>
									<td valign="top"><a href="AcctPlanProjRpt?sortMeth=sales_id<%= sortParms.toString() %>"><span class=tocactive>SE</span></a></td>
									<td valign="top"><a href="AcctPlanProjRpt?sortMeth=bid_date<%= sortParms.toString() %>"><span class=tocactive>Bid Date</span></a></td>
									<td valign="top"><a href="AcctPlanProjRpt?sortMeth=job_name<%= sortParms.toString() %>"><span class=tocactive>Job Name</span></a></td>
									<td valign="top"><a href="AcctPlanProjRpt?sortMeth=status<%= sortParms.toString() %>"><span class=tocactive>Status</span></a></td>
									<td valign="top"><a href="AcctPlanProjRpt?sortMeth=job_type<%= sortParms.toString() %>"><span class=tocactive>Job Type</span></a></td>
									<td valign="top"><a href="AcctPlanProjRpt?sortMeth=go_num<%= sortParms.toString() %>"><span class=tocactive>GO Number</span></a></td>
									<td valign="top"><a href="AcctPlanProjRpt?sortMeth=bid_dollars<%= sortParms.toString() %>"><span class=tocactive>Bid Dollars</span></a></td>
								</thead>
								<%
										NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
										for (int i=0; i < results.size(); i++) {
											Bid bid = (Bid)results.get(i);
								%>
								<tr class=cellColor>
									<td class=smallFontL><a href="AcctPlanProjRpt?page=projresults&rptType=negDetails&negNum=<%= bid.getNegNum() %>"><%= bid.getNegNum() %></a></td>
									<td class=smallFontL><%= bid.getSalesId() %></td>
									<td class=smallFontL><%= bid.getBidDateAsString() %></td>
									<td class=smallFontL><%= bid.getJobName() %></td>
									<td class=smallFontL><%= bid.getStatus() %></td>
									<td class=smallFontL><%= bid.getJobType().getDescription() %></td>
									<td class=smallFontL><%= bid.getGONum() %></td>
									<td class=smallFontR><%= bid.getBidDollarsForDisplay() %></td>
								</tr>
								<%
										}
								%>
								<tr class=cellColor>
									<td colspan=7 class=smallFontL>&nbsp;</td>
									<td class=smallFontR><%= nf.format(bidDol.doubleValue()) %></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</body>
</html>
