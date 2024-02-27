<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<html>
	<%@ include file="./TAPheader.jsp" %>
	<%@ include file="analytics.jsp" %>
	<%
	    User user = header.getUser();
	    StringBuffer parms = (StringBuffer) request.getAttribute("parms");
	    ArrayList requests = (ArrayList) request.getAttribute("requests");
	    ArrayList custList = (ArrayList) request.getAttribute("custList");
	    String catNum = (String) request.getAttribute("catNum");
	    boolean foundOne=false;
	%>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750">
				<p><br>
					<a class="crumb" href="SMRCHome">Home Page</a>
					<span class="crumbarrow">&gt;</span>
					<a class="crumb" href="AcctPlanReport?page=reportsample">Sample Reports</a>
					<span class="crumbarrow">&gt;</span>
					<span class="crumbcurrent">Sample Reports Results</span>
				</p>
			</td>
		</tr>
	</table>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750" valign="top">
									<p>&nbsp;</p>
				<p class=heading2>Product Sample Report Results</p>
				<table width="100%" class=tableBorder cellspacing=1 cellpadding=2>
					<thead class=cellColor>
						<td valign="top"><a href="AcctPlanReport<%= parms.toString() %>&orderBy=customer_name"><span class=tocactive>Customer</span></a></td>
						<td valign="top"><a href="AcctPlanReport<%= parms.toString() %>&orderBy=last_name"><span class=tocactive>Requestor</span></td>
						<td valign="top"><a href="AcctPlanReport<%= parms.toString() %>&orderBy=request_date"><span class=tocactive>Request Date</span></td>
						<td valign="top"><a href="AcctPlanReport<%= parms.toString() %>&orderBy=ship_attention"><span class=tocactive>Shipped To</span></td>
						<td valign="top"><a href="AcctPlanReport<%= parms.toString() %>&orderBy=ship_city"><span class=tocactive>Ship City</span></td>
						<td valign="top"><a href="AcctPlanReport<%= parms.toString() %>&orderBy=ship_state"><span class=tocactive>Ship State</span></td>
						<td valign="top">Products Requested<br>(Catalog Number & Qty)</td>
					</thead>
					<%
							for (int i=0; i < requests.size(); i++) {
								SampleRequest sr = (SampleRequest)requests.get(i);
								Customer cust = (Customer) custList.get(i);
								if (user.ableToUpdate(cust)) {
									foundOne=true;
					%> <tr class=cellColor>
						<td class=smallFontL valign=top><a href="OEMAcctPlan?page=salesplan&cust=<%= sr.getVistaCustNum() %>"><%= sr.getCustomerName() %></a></td>
						<td class=smallFontL valign=top><%= sr.getRequestor() %></td>
						<td class=smallFontL valign=top><%= sr.getRequestDateAsString() %></td>
						<td class=smallFontL valign=top><%= sr.getAttention() %></td>
						<td class=smallFontL valign=top><%= sr.getCity() %></td>
						<td class=smallFontL valign=top><%= sr.getState() %></td>
						<td class=smallFontL valign=top>
							<table valign=top>
								<%
												ArrayList prods = sr.getProducts();
												for (int j=0; j < prods.size(); j++) {
													SampleProduct sp = (SampleProduct)prods.get(j);
													String bold = "";
													String unBold = "";
													if (sp.getCatalogNum().toUpperCase().equals(catNum.toUpperCase())) {
														bold = "<b>";
														unBold = "</b>";
													}
								%>
								<tr><td class=smallFontL><%= bold + sp.getCatalogNum() + unBold %></td><td class=smallFontR><%= bold + sp.getQty() + unBold %></td></tr>
								<%				}
								%> </table></td>
					</tr>
					<%			}
							}
					if(foundOne==false){
					%>
					<tr class=cellColor><td colspan="7"><br>&nbsp;No results found.</td></tr>
					<%
					}
					%>
				</table>
			</td>
		</tr>
	</table>
	</body>
</html>
