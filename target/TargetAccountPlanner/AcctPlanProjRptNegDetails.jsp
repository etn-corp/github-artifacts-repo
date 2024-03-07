<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="com.eaton.electrical.smrc.bo.*" %>
<%@ page import="java.text.*" %>
<html>
	<%@ include file="./TAPheader.jsp" %>
	<%@ include file="analytics.jsp" %>
	<%
	    Bid bid = (Bid) request.getAttribute("bid");
	    BidStatusHistory bsh = (BidStatusHistory) request.getAttribute("bsh");
	    ArrayList customersLinkedToNeg = (ArrayList) request.getAttribute("customersLinkedToNeg");
	    ArrayList products = (ArrayList) request.getAttribute("products");
	    ArrayList productDesc = (ArrayList) request.getAttribute("productDesc");
	    String salesmanName = (String) request.getAttribute("salesmanName");
	    String geogName = (String) request.getAttribute("geogName");
	
	%>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750">
				<p><br>
					<a class="crumb" href="SMRCHome">Home Page</a>
					<span class="crumbarrow">&gt;</span>
					<a class="crumb" href="AcctPlanReport?page=reportother">Other Reports</a>
					<span class="crumbarrow">&gt;</span>
					<span class="crumbcurrent">Negotiation Details</span>
				</p>
			</td>
		</tr>
	</table>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750" valign="top">
				<!--<p>&nbsp;</p>
				<p class="heading2">Other Reports</p>-->
				<p>&nbsp;</p>
				<p class="heading2">Neg Details</p>
				<table class=tableBorder cellspacing=1 cellpadding=1 width="100%">
					<tr>
						<td class=innerColor><div class=searchLeft2>Neg Number</div></td>
						<td class=cellColor><div class=smallFontL><%=  bid.getNegNum() %></div></td>
						<td class=innerColor><div class=searchLeft2>Bid Date</div></td>
						<td class=cellColor><div class=smallFontL><%= bid.getBidDateAsString() %></div></td>
						<td class=innerColor><div class=searchLeft2>Job Name</div></td>
						<td class=cellColor><div class=smallFontL><%= bid.getJobName() %></div></td>
					</tr>
					<tr>
						<td class=innerColor><div class=searchLeft2>Current Status</div></td>
						<td class=cellColor><div class=smallFontL><%= bid.getStatus() %></div></td>
						<td class=innerColor><div class=searchLeft2>Job Type</div></td>
						<td class=cellColor><div class=smallFontL><%= bid.getJobType().getDescription() %></div></td>
						<td class=innerColor><div class=searchLeft2>GO Number</div></td>
						<td class=cellColor><div class=smallFontL><%= bid.getGONum() %></div></td>
					</tr>
					<tr>
						<td class=innerColor><div class=searchLeft2>Bid Dollars</div></td>
						<td class=cellColor><div class=smallFontL><%= bid.getBidDollarsForDisplay() %></div></td>
						<td class=innerColor><div class=searchLeft2>Salesman</div></td>
						<td class=cellColor><div class=smallFontL><%= salesmanName %> (<%= bid.getSalesId() %>)</div></td>
						<td class=innerColor><div class=searchLeft2>Geography</div></td>
						<td class=cellColor><div class=smallFontL><%= geogName %> (<%= bid.getSPGeog() %>)</div></td>
					</tr>
				</table>
				<br>
				<p class="heading2">Neg Status Milestone Dates</p>
				<table class=tableBorder cellspacing=1 cellpadding=1 width="100%">
					<tr>
						<td class=innerColor><div class=searchLeft2>New Date</div></td>
						<td class=cellColor><div class=smallFontL><%= bsh.getNewDateAsString() %></div></td>
						<td class=innerColor><div class=searchLeft2>Budget Date</div></td>
						<td class=cellColor><div class=smallFontL><%= bsh.getBudgetDateAsString() %></div></td>
						<td class=innerColor><div class=searchLeft2>Bid Date</div></td>
						<td class=cellColor><div class=smallFontL><%= bsh.getBidDateAsString() %></div></td>
						<td class=innerColor><div class=searchLeft2>Buy Date</div></td>
						<td class=cellColor><div class=smallFontL><%= bsh.getBuyDateAsString() %></div></td>
					</tr>
					<tr>
						<td class=innerColor><div class=searchLeft2>Obtain Date</div></td>
						<td class=cellColor><div class=smallFontL><%= bsh.getObtainDateAsString() %></div></td>
						<td class=innerColor><div class=searchLeft2>Abandon Date</div></td>
						<td class=cellColor><div class=smallFontL><%= bsh.getAbandonDateAsString() %></div></td>
						<td class=innerColor><div class=searchLeft2>Lost Date</div></td>
						<td class=cellColor><div class=smallFontL><%= bsh.getLostDateAsString() %></div></td>
						<td class=innerColor><div class=smallFontL>&nbsp;</td>
						<td class=cellColor><div class=smallFontL>&nbsp;</td>
					</tr>
				</table>
				<br>
				<p class="heading2">Customers Linked to Negotiation</p>
				<table class=tableBorder cellspacing=1 cellpadding=1 width="100%">
					<tr class=cellColor>
						<%
								for (int i=0; i < customersLinkedToNeg.size(); i++) {
									Customer cust = (Customer)customersLinkedToNeg.get(i);
									%>
						<td class=smallFontL><a href="AccountProfile?acctId=<%= cust.getVistaCustNum() %>"><%= cust.getName() %> (<%= cust.getVistaCustNum() %>)</a></td>
						<%
						}
						%>
					</tr>
				</table>
				<br>
				<p class="heading2">Product Lines on Negotiation</p>
				<table class=tableBorder cellspacing=1 cellpadding=1 width="100%">
					<tr class=innerColor>
						<td class=searchLeft2>Product Line</td>
						<td class=searchLeft2>Bid Dollars</td>
						<td class=searchLeft2>Percent of Order</td>
					</tr>
					<%
							for (int i=0; i < products.size(); i++) {
								BidProduct bp = (BidProduct)products.get(i);
					%>
					<tr class=cellColor>
						<td class=smallFontL><%= productDesc.get(i) %> (<%= bp.getProductId() %>)</td>
						<td class=smallFontR><%= bp.getBidDollarsAsString() %></td>
						<%
									NumberFormat nf = NumberFormat.getPercentInstance(Locale.US);
									nf.setMaximumFractionDigits(1);
									nf.setMinimumFractionDigits(1);
						%>
						<td class=smallFontR><%= nf.format(bp.getBidDollars()/bid.getBidDollars()) %></td>
					</tr>
					<%		}
					%>
				</table>
				<br><b>Note:</b> Because not all products on each order are linked to a product line, <br>the sum of bid dollars by product line may not equal the total amount of bid dollars on the neg.
				<br><br><br><br>
			</td>
		</tr>
	</table>
	</body>
</html>
