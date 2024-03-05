<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ include file="analytics.jsp" %>

<!--
<html>
<head><title>Target Account Planner</title>
-->
<%
   String changeType = (String) request.getAttribute("changeType");
   ArrayList custChanges = (ArrayList) request.getAttribute("custChanges");
   HashMap ProductDescList = (HashMap) request.getAttribute("ProductDescList");
   HashMap ProductSublineDescList = (HashMap) request.getAttribute("ProductSublineDescList");
   HashMap IndustrySubgroupDescList = (HashMap)request.getAttribute("IndustrySubgroupDescList");
   HashMap TaskDescList = (HashMap) request.getAttribute("TaskDescList");
   HashMap changesUsers = (HashMap) request.getAttribute("changesUsers");
   Boolean showProduct = (Boolean) request.getAttribute("showProduct");
   Boolean showIndustry = (Boolean) request.getAttribute("showIndustry");
   Boolean showSalesPlan = (Boolean) request.getAttribute("showSalesPlan");

%>
<!--
</head>
<body>
-->


<%@ include file="./TAPheader.jsp" %>    



<div class=centerOnly><div class=heading2><%= changeType %> for 

<%
		if (header.getPage().equals("viewcustchanges")) {
%>			<%= header.getCustomer().getName()%>
<%		}
		else {
%>			<%= header.getSalesGroup().getDescription() %>
<%		}

%>
</div></div>

<%

		if (custChanges.size() > 0) {
%>
			<table class="tableBorder" width="100%" cellspacing=1 cellpadding=1>
                            <thead class="innerColor">

<%			if (header.getPage().equals("viewplannerchanges")) {
%>				<td class=smallFontC>Customer</td>
<%			}

%>			<td class="smallFontC">Change Type</td>
			<td class="smallFontC">Date</td>
			<td class="smallFontC">User</td>
			<td class="smallFontC">Old Value</td>

<%			if (showProduct.booleanValue()) {
%>				<td class="smallFontC">Product</td>
				<td class="smallFontC">Subline</td>
<%			}

			if (showIndustry.booleanValue()) {
%>				<td class=smallFontC>Industry</td>
<%			}

			if (showSalesPlan.booleanValue()) {
%>				<td class=smallFontC>Task</td>
<%			}

%>			</thead>
<%			for (int i=0; i < custChanges.size(); i++) {
				CustChange cc = (CustChange)custChanges.get(i);
 				User changeUser = (User) changesUsers.get(new Integer(cc.getId()));

%>				<tr class=cellColor>

<%				if (header.getPage().equals("viewplannerchanges")) {
%>					<td class="smallFontL"><%= cc.getCustomerName() %></td>
<%				}
%>				<td class="smallFontL"><%= cc.getModTypeDescription() %></td>
				<td class="smallFontL"><%= cc.getModTimeAsString() %></td>

				<td class="smallFontL"><a href="mailto:<%= changeUser.getEmailAddress() %>"> <%= changeUser.getFirstName() %> <%= changeUser.getLastName() %></a></td>  


                		<td class="smallFontL"><%= cc.getOldValue() %></td>
<%				if (showProduct.booleanValue()) {
%>					<td class="smallFontL"><%= (String) ProductDescList.get(new Integer(cc.getId())) %></td>
					<td class="smallFontL"><%= (String) ProductSublineDescList.get(new Integer(cc.getId())) %></td>
<%				}
				if (showIndustry.booleanValue()) {
					if (cc.getIndustryId() != null && !cc.getIndustryId().equals("")) {
%>						<td class="smallFontL"><%= IndustrySubgroupDescList.get(new Integer(cc.getId())) %></td>
<%					}
					else {
%>						<td class="smallFontL">&nbsp;</td>
<%					}
				}
				if (showSalesPlan.booleanValue()) {
%>					<td class="smallFontL"><%= TaskDescList.get(new Integer(cc.getId())) %></td>
<%                                }
%>				</tr>
<%			}

%>			</table>
<%		}
		else {
			if (header.getPage().equals("viewcustchanges")) {
%>				<br><div class="heading3">There have been no changes to this customer since all changes started being logged in May 2003.</div>
<%			}
			else {
%>				<br><div class="heading3">There have been no changes to this planner in the past two days.</div>
<%			}
		}

%>

</body>
</html>
