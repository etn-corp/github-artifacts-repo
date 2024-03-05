<%@ page import="java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.Money"%>
<%
ArrayList accounts = (ArrayList)request.getAttribute("accounts");
User usr = (User)request.getAttribute("usr");
%>
<html>
	<%
	response.setContentType("text/txt");
	response.setHeader("Content-Disposition", "attachment; filename=\"tap_customer_listing.xls\"");
	%>
	Customer Listing Results - TAP Dollars
	<br>
		<table width="100%" border="0" cellspacing="1" cellpadding="0">
			<thead>
			    <th>Vista Customer Number</th>
				<th>Account Name</th>
				<th>Sales Engineer</th>
				<th>District</th>
				<th>Target Account</th>
				<th>Primary Segment</th>
				<th>Secondary Segment</th>
				<th>Stage</th>
				<th>YTD Sales ($)</th>
				<th>Prev YTD Sales ($)</th>
			</thead>
			<%
			for (int i=0; i < accounts.size(); i++) {
			Account account = (Account)accounts.get(i);
			AccountNumbers numbers = account.getNumbers();
			// The space before the vcn is to prevent losing leading zeros in Excel
			%>
			<tr>
				<td>
					&nbsp;<%= account.getVcn() %>
			    </td>
				<td>
					<%= account.getCustomerName() %>
				</td>
				<td>
					<%= account.getSalesEngineer1() %>
				</td>
				<td>
					<%= account.getDistrict() %>
				</td>
				<td>
					<%
					if(account.isTargetAccount()){
						out.println("Y");
					}else{
						out.println("N");
					}
					%>
				</td>
				<td>
					<%= account.getPrimarySegmentName() %>
				</td>
				<td>
					<%= account.getSecondarySegmentName() %>
				</td>
				<td>
					<%= account.getStage() %>
				</td>
				<td>
					<% if(usr.ableToSee(account) || usr.hasOverrideSecurity() || usr.hasSegmentOverride(account)){ %>
					<%= Money.formatDoubleAsDollars(numbers.getCurrentYTD()) %>
					<% }else{ %>
					N/A
					<% } %>
				</td>
				<td>
					<% if(usr.ableToSee(account) || usr.hasOverrideSecurity() || usr.hasSegmentOverride(account)){ %>
					<%= Money.formatDoubleAsDollars(numbers.getCurrentMinusOneYTD()) %>
					<% }else{ %>
					N/A
					<% } %>
				</td>
			</tr>
			<% } %>
		</table>
		</body>
</html>
