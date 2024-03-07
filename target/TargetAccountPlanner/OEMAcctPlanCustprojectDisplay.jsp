<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="java.math.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.text.NumberFormat"%>
<%@ include file="analytics.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

OEMAcctPlanBean OEMAPVars = (OEMAcctPlanBean)request.getAttribute("variables");
request.setAttribute("header", OEMAPVars.getHeader());

		Customer cust = OEMAPVars.getCust();

		User user = OEMAPVars.getUser();
		ArrayList bids = OEMAPVars.getBids();
		ArrayList targetProjects = OEMAPVars.getProjects();
		String salesmanName = OEMAPVars.getSalesmanName();
		String vcn = request.getParameter("cust");
%>
<HTML>
	<%@ include file="./TAPheader.jsp" %>
	
		<div class=centerOnly>
		<div class=heading2><%= cust.getName() %></div>
		<div class=heading3><%= salesmanName %></div>
		</div>
		<table border=0 align=center width="90%">
			<tr width="100%">
				<td width="50%" valign=top>


					<table class=tableBorder cellspacing=1 cellpadding=1 border=0 width="100%">
						<caption class=heading3>Target Projects</caption>
						<thead class=innerColor>
							<td class=searchCenter1>Delete?</td>
							<td class=searchCenter1>Name</td>
							<td class=searchCenter1>Project Status</td>
							<td class=searchCenter1>Target Reason</td>
							<td class=searchCenter1>Spec Preference</td>
							<td class=searchCenter1>CH Value</td>
						</thead>


<%
		for (int i=0; i < targetProjects.size(); i++) {
			TargetProject proj = (TargetProject)targetProjects.get(i);

			if (proj.isActive()) {
				boolean canApprove = proj.getCanApprove();
				boolean canDelete = canApprove||user.getUserid().equals(proj.getUserAdded());
				%>
								<tr class=cellColor>
				<%
				if (canDelete) {
					%>
					<td class=smallFontC><a href=OEMAcctPlan?page=custproject&cust=<%= vcn %>&delProj=<%= proj.getId() %>><img src=<%= sImagesURL %>remove.gif border=0 alt='Remove Target Account'></a></td>
						<%
				}
				else {
				%>
					<td class=smallFontC>&nbsp;</td>
				<%
				}

				%>
									<td class=smallFontL><a href=TargetProjectPopup?projId=<%= proj.getId() %>&cust=<%= vcn %>><%= proj.getJobName() %></a></td>
									<td class=smallFontL><%= proj.getStatus().getDescription() %></td>
									<td class=smallFontL><%= proj.getReason().getDescription() %></td>
									<td class=smallFontL><%= proj.getPreference().getDescription() %></td>
									<td class=smallFontR><%= proj.getCHValueForDisplay() %></td>
								</tr>
				<%
			}
		}
%>
					</table>

					<table class=tableBorder cellspacing=1 cellpadding=1 border=0 width="100%">
						<caption class=heading3>Inactive Target Projects</caption>
						<thead class=innerColor>
							<td class=searchCenter1>Approve / Delete?</td>
							<td class=searchCenter1>Name</td>
							<td class=searchCenter1>Project Status</td>
							<td class=searchCenter1>Target Reason</td>
							<td class=searchCenter1>Spec Preference</td>
							<td class=searchCenter1>CH Value</td>
							<td class=searchCenter1>Approval Status</td>
						</thead>

<%

		for (int i=0; i < targetProjects.size(); i++) {
			TargetProject proj = (TargetProject)targetProjects.get(i);

			if (!proj.isActive()) {
				%>
								<tr class=cellColor>
								<td class=smallFontC>
				<%
				boolean canApprove = false;
				boolean canDelete = false;

				if (!proj.deleted()) {
					canApprove = proj.getCanApprove();
					canDelete = canApprove||user.getUserid().equals(proj.getUserAdded());

					if (canApprove) {
						%>
						<a href=OEMAcctPlan?page=custproject&cust=<%= vcn %>&approve=<%= proj.getId() %>><img src=<%= sImagesURL %>check_mark.jpg border=0 alt='Approve Target Account'></a>
						<%
					}

					if (canDelete) {
						%>
						<a href=OEMAcctPlan?page=custproject&cust=<%= vcn %>&delProj=<%= proj.getId() %>><img src=<%= sImagesURL %>remove.gif border=0 alt='Remove Target Account'></a>
						<%
					}
				}

				if (!canApprove&&!canDelete) {
					%>
					&nbsp;
					<%
				}
				%>
									</td>
									<td class=smallFontL><a href=TargetProjectPopup?projId=<%= proj.getId() %>&cust=<%= vcn %>><%= proj.getJobName() %></a></td>
									<td class=smallFontL><%= proj.getStatus().getDescription() %></td>
									<td class=smallFontL><%= proj.getReason().getDescription() %></td>
									<td class=smallFontL><%= proj.getPreference().getDescription() %></td>
									<td class=smallFontR><%= proj.getCHValueForDisplay() %></td>
				
				<%
				if (proj.deleted()) {
					%>
										<td class=smallFontL>Deleted</td>
					<%
				}
				else if (proj.waitingForDM()) {
					%>
										<td class=smallFontL>Awaiting District Manger Approval</td>
					<%
				}
				else if (proj.waitingForProjectSalesMgr()) {
					%>
										<td class=smallFontL>Awaiting Project Sales Manager Approval</td>
					<%
				}
				else if (proj.waitingForChampsMgr()) {
					%>
										<td class=smallFontL>Awaiting CHAMPS Mgr Approval</td>
					<%
				}
				%>
								</tr>
				<%
			}
		}
		%>
					</table>
		<br><div class=smallFontL>Click <a href=TargetProjectPopup?page=newtargetproject&cust=<%= vcn %>>here</a> to add a new target project for this customer</div>
				</td>
				<td width="50%" valign=top>
					<table class=tableBorder cellspacing=1 cellpadding=1 border=0 width="100%">
						<caption class=heading3>Bid Tracking Summary Results</caption>
						<thead class=innerColor>
							<td class=searchCenter1>Total Obtained</td>
							<td class=searchCenter1>Total Created</td>
							<td class=searchCenter1>Neg Hit Rate</td>
							<td class=searchCenter1>Dollars Obtained</td>
							<td class=searchCenter1>Dollars Created</td>
							<td class=searchCenter1>% Dollars Obtained</td>
						</thead>

<%

		NumberFormat nf = NumberFormat.getPercentInstance(Locale.US);
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		NumberFormat cf = NumberFormat.getCurrencyInstance(Locale.US);

		int tot = 0;
		int obt = 0;
		double totDol = 0;
		double obtDol = 0;
		ArrayList receivedOrders = new ArrayList(10);

		for (int i=0; i < bids.size(); i++) {
			Bid bid = (Bid)bids.get(i);
			tot++;
			totDol = totDol + bid.getBidDollars();

			if (bid.getStatus().equals("Obtained") && bid.getCustReceivedOrder()) {
				obt++;
				obtDol = obtDol + bid.getBidDollars();
				receivedOrders.add(bid);
			}
		}

		%>
						<tr class=cellColor>
							<td class=smallFontR><%= obt %></td>
							<td class=smallFontR><%= tot %></td>
		<%
		if (tot > 0) {
			%>
								<td class=smallFontR><%= nf.format((double)obt/(double)tot) %></td>
			<%
		}
		else {
			%>
								<td class=smallFontR>N/A</td>
			<%
		}
		%>
							<td class=smallFontR><%= cf.format(obtDol) %></td>
							<td class=smallFontR><%= cf.format(totDol) %></td>
		<%
		if (totDol > 0) {
			%>
								<td class=smallFontR><%= nf.format(obtDol/totDol) %></td>
			<%
		}
		else {
			%>
								<td class=smallFontR>N/A</td>
			<%
		}
		%>
						</tr>
					</table>
					<br>

					<table class=tableBorder cellspacing=1 cellpadding=1 border=0 width="100%">
						<caption class=heading3>Bid Tracking Details</caption>
						<thead class=innerColor>
							<td class=searchCenter1>Neg Number</td>
							<td class=searchCenter1>Job Name</td>
							<td class=searchCenter1>Status</td>
							<td class=searchCenter1>Salesman</td>
							<td class=searchCenter1>Bid Dollars</td>
							<td class=searchCenter1>Job Type</td>
							<td class=searchCenter1>Order Number</td>
							<td class=searchCenter1>Order Date</td>
						</thead>
<%
		for (int i=0; i < bids.size(); i++) {
			Bid bid = (Bid)bids.get(i);
			String bgColor = "#FFFFFF";
			String fontColor = "#000000";

			if (bid.getGONum() != null && !bid.getGONum().equals("")) {
				bgColor = "#FF0000";
				fontColor = "#FFFFFF";

				for (int j=0; j < receivedOrders.size(); j++) {
					Bid order = (Bid)receivedOrders.get(j);

					if (order.getNegNum().equals(bid.getNegNum())) {
						bgColor = "#008000";
					}
				}
			}
			%>
							<tr class=cellColor>
								<td class=smallFontL><a href=AcctPlanProjRpt?page=projresults&rptType=negDetails&negNum=<%= bid.getNegNum() %>><%= bid.getNegNum() %></a></td>
								<td class=smallFontL><%= bid.getJobName() %></td>
								<td class=smallFontL><%= bid.getStatus() %></td>
								<td class=smallFontL><%= bid.getSalesmanName() %></td>
								<td class=smallFontR><%= bid.getBidDollarsForDisplay() %></td>
								<td class=smallFontL><%= bid.getJobType().getDescription() %></td>
								<td class=smallFontL bgColor="<%= bgColor %>"><font color="<%= fontColor %>"><%= bid.getGONum() %></font></td>
								<td class=smallFontL bgColor="<%= bgColor %>"><font color="<%= fontColor %>"><%= bid.getGODateAsString() %></font></td>
							</tr>
			<%
		}
		%>
					</table>
				</td>
			</tr>
		</table>




</BODY>
</HTML>