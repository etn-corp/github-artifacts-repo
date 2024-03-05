<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="com.eaton.electrical.smrc.bo.*" %>
<%@ page import="java.math.*" %>
<%@ page import="java.text.*" %>
<%@ page contentType="application/msexcel"%>
<%@ page import="java.util.*" %>
<html>
	<%
		response.setContentType("text/txt");
		response.setHeader("Content-Disposition", "attachment; filename=\"tapreport.xls\"");
		StringBuffer filters = (StringBuffer) request.getAttribute("filters");
	    StringBuffer sortParms = (StringBuffer) request.getAttribute("sortParms");
	    ArrayList results = (ArrayList) request.getAttribute("results");
	    BigDecimal bidDol = (BigDecimal) request.getAttribute("bidDol");
	%>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="750" valign="top">
				<p><b>Bid Tracking Results</b></p>
				<p><b>Filters: </b><%= filters.toString() %></p>
				<table width="100%" cellspacing=1 cellpadding=1 align=center>
					<tr></tr>
					<tr>
						<%		// Results go here   %>
						<td valign=top>
							<table cellspacing=1 cellpadding=1 width="100%">
								<thead>
									<td valign="top"><b><u>Neg Number</u></b></td>
									<td valign="top"><b><u>SE</u></b></td>
									<td valign="top"><b><u>Bid Date</u></b></td>
									<td valign="top"><b><u>Job Name</u></b></td>
									<td valign="top"><b><u>Status</u></b></td>
									<td valign="top"><b><u>Job Type</u></b></td>
									<td valign="top"><b><u>GO Numberv</td>
									<td valign="top"><b><u>Bid Dollars</u></b></td>
								</thead>
								<%
										for (int i=0; i < results.size(); i++) {
											Bid bid = (Bid)results.get(i);
								%>
								<tr>
									<td><%= bid.getNegNum() %></td>
									<td><%= bid.getSalesId() %></td>
									<td><%= bid.getBidDateAsString() %></td>
									<td><%= bid.getJobName() %></td>
									<td><%= bid.getStatus() %></td>
									<td><%= bid.getJobType().getDescription() %></td>
									<td><%= bid.getGONum() %></td>
									<td><%= bid.getBidDollarsForDisplay() %></td>
								</tr>
								<%
										}
								%>
								<tr>
									<td colspan=7 align=right><b>Total:</b></td>
									<td ><%=bidDol.doubleValue() %></td>
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