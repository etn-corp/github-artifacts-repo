<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ include file="analytics.jsp" %>
<%
OEMAcctPlanBean OEMAPVars = (OEMAcctPlanBean)request.getAttribute("variables");
request.setAttribute("header", OEMAPVars.getHeader());
%>
<html>
	<%@ include file="./TAPheader.jsp" %>
	<%
	SalesPlan salesPlan = OEMAPVars.getSalesPlan();
	Customer cust = OEMAPVars.getCust();
	String backSort = OEMAPVars.getBackSort();
	String backParm = OEMAPVars.getBackParm();
	String customerName = OEMAPVars.getCustomerName();
	String salesmanName = OEMAPVars.getSalesmanName();
	%>
	<script LANGUAGE='JavaScript' SRC='<%= jsURL %>calendar.js'></script>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750">
				<p><br>
					<a class="crumb" href="SMRCHome">Home Page</a>
					<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Sales Plan</span></p>
			</td>
		</tr>
	</table>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750" valign="top">
				<p>&nbsp;</p>
				<div class=heading2><%= cust.getName() %></div>
				<br>
				<a href="OEMAcctPlan?page=salesplanprint&cust=<%= cust.getVistaCustNum() %>"><img src="<%= sImagesURL %>button_update_plan.gif" width="70" height="20" align="absmiddle" border="0"></a>
				<br>
				<table width="100%">
					<tr>
						<td colspan=2>Salesperson: <%= salesmanName %></td>
					</tr>
					<tr>
						<td>
							<%
							SPStage customerStage = OEMAPVars.getCustomerStage();
							if(customerStage.getId()==0){
							customerStage.setDescription("No stage selected");
							}
							%>
							<div class="heading3">Stage of the Account</div><%= customerStage.getDescription() %>
						</td>
					</tr>
					<tr>
						<td valign=top><div class="heading3">Objectives and Strategies</div>
							<font color=red>Must be measurable, attainable, and have a time focus</font>
							<table width="100%" cellspacing=0 cellpadding=0>
								<tr>
									<td colspan=2>Strategic Focus, Desired Position, Short/Long Term Objectives</td>
								</tr>
								<tr>
									<td colspan=2>
										<blockquote>
										<% if(salesPlan.getObjectiveResponse().trim().length()==0) { %>
										    Nothing entered yet.
										<% }else { %>
										    <%= salesPlan.getObjectiveResponse() %>	    
										<% } %>
										</blockquote>
										<br><br>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td valign=top colspan=2><div class="heading3">Competitive Products and Position:</div>
							What are the competitors commercial and technical positions? Strengths and weaknesses? Their expected defense strategy and tactics?
						</td>
					</tr>
					<tr>
						<td colspan=2>
						<blockquote>
						<% if(salesPlan.getCompetitiveProdsAndPositions().trim().length()==0) { %>
						    Nothing entered yet.
						<% }else { %>
						    <%= salesPlan.getCompetitiveProdsAndPositions() %>	    
						<% } %>
							</blockquote>
							<br><br>
						</td>
					</tr>
					<tr>
						<td colspan=2>
							<table width="100%" class=tableBorder cellspacing=1 cellpadding=1>
								<div class="heading3">Task Assignments</div>
								<thead bgcolor=#F8F8FF>
									<td class=columnheader-acctprofile>Action</td>
									<td class=columnheader-acctprofile>Assigned To</td>
									<td class=columnheader-acctprofile>Notified</td>
									<td class=columnheader-acctprofile>Product</td>
									<td class=columnheader-acctprofile>EBE Category</td>
									<td class=columnheader-acctprofile>Schedule</td>
									<td class=columnheader-acctprofile>Complete?</td>
									<td class=columnheader-acctprofile>Results</td>
									<td class=columnheader-acctprofile>Customer Visit?</td>
									<td class=columnheader-acctprofile>Attached Documents</td>
								</thead>
								<%
								ArrayList tasks = OEMAPVars.getTasks();
								for (int i=0; i < tasks.size(); i++) {
									PurchaseActionProgram task = (PurchaseActionProgram)tasks.get(i);
								
											if (task.getSchedule() != null) {
												GregorianCalendar gc = new GregorianCalendar() ;
												Date currentDate = gc.getTime() ;
												Date scheduleDate = (Date)task.getSchedule() ;
												long days = (currentDate.getTime()-scheduleDate.getTime()) / (24*3600*1000) ;
								
												if(days>365 && task.isComplete()){
													continue;
												}
								
											}
								
									%>
								<tr class=cellColor>
									<td class=smallFontL><%= task.getAction() %></td>
									<%
									User usr = task.getAssignedUser();
									%>
									<td class=smallFontL>
									<%
									if(usr!=null){
									%>
										<%= usr.getFirstName() %> <%= usr.getLastName() %>
									<% } %>
									</td>
									<%
										ArrayList ccUsers = task.getCcUsers();
										StringBuffer ccUsersBuffer = new StringBuffer();
										for(int j=0;j<ccUsers.size();j++){
											usr = (User)ccUsers.get(j);
											ccUsersBuffer.append(usr.getFirstName() + " " + usr.getLastName());
											if(j!=ccUsers.size()-1){
												ccUsersBuffer.append(", ");
											}
										}

									%>
									<td class=smallFontL><%= ccUsersBuffer.toString() %></td>
									<td class=smallFontL><%= task.getProductDescription() %></td>
									<td class=smallFontL><%= task.getEBEDescription() %></td>
									<%
									if (task.getSchedule() != null) {
										%>
									<td class=smallFontC><%= task.getScheduleAsString() %></td>
									<%
									}
									else {
									%>
									<td class=smallFontC>&nbsp;</td>
									<%
									}
									if (task.isComplete()) {
									%>
									<td class=smallFontC>Yes</td>
									<%
									}
									else {
									%>
									<td class=smallFontC>No</td>
									<%
									}
									%>
									<td class=smallFontL><%= task.getResults() %></td>
									<td class=smallFontL>
										<%
										if (task.isFromCustomerVisit()){
										%>
										<a href="CustomerVisits?acctId=<%= task.getCustomer() %>&visitId=<%= task.getCustomerVisitId() %>">Customer Visit</a>
										<%
										} %>
									</td>
									<td class=smallFontL>
										<%
											ArrayList taskAttachments = task.getAttachments();
											for (int attIndex=0; attIndex < taskAttachments.size(); attIndex++){
												TaskAttachment attachment = (TaskAttachment) taskAttachments.get(attIndex);
										%>
												<a href="javascript:newWindow('TaskAttachmentDisplay?docId=<%= attachment.getId()  %>','AttachmentDisplay')"><span class=smallFontL><%= attachment.getFileName() %><span></a><br>
										<%	}
										%>
									</td>
								</tr>
								<%
									}
								
									%>
							</table>
								<%
								if(tasks.size()==0){
								%>
								<br>No tasks for this Sales Plan		
								<%
								}
								%>
								<br><br>
						</td>
					</tr>
				</table>
				<!--
				<%
				String updateDate = OEMAPVars.getLastUpdateDate();
				%>
				<div class=smallFont>Last Updated: <%= updateDate %> <a href="javascript: openPopup('CustChanges?cust=<%= cust.getVistaCustNum() %>&viewPage=<%= request.getParameter("page") %>','custChanges',300,600)">View Changes</a></div>
				-->
			</td>
		</tr>
	</table>
	<p>&nbsp;</p>
	<p>&nbsp;</p>
	</body>
</html>
