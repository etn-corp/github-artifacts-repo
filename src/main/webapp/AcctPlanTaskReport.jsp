<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="com.eaton.electrical.smrc.bo.*" %>
<html>
	<%@ include file="./TAPheader.jsp" %>
	<%@ include file="analytics.jsp" %>
	<%
	    SalesGroup sGroup = header.getSalesGroup();
	    User assignedTo = (User) request.getAttribute("assignedToUser");
	    String assignedToUserId=request.getParameter("assignedTo");
	    if(assignedToUserId==null){
	    	assignedToUserId=assignedTo.getUserid();
	    }
	    ArrayList tasks = (ArrayList) request.getAttribute("tasks");
	    
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
					<span class="crumbcurrent">Task Reports Results</span>
				</p>
			</td>
		</tr>
	</table>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750" valign="top">
				<p>&nbsp;</p>
				<div class=heading2>Task Report</div>
				<%
						String open = "";
						if (request.getParameter("seeComplete") == null) {
							open = "Open ";
						}
						if (!assignedToUserId.equals("%")) {
				%> <p>All <%= open %> Tasks for <b><%= assignedTo.getFirstName()%> <%= assignedTo.getLastName() %></b>
				<%		}
						else {
				%> <p>All <%= open %> Tasks for <b>All Users</b>
				<%		}
						if (request.getAttribute("geogName") != null) {
				%> in the <b><%= request.getAttribute("geogName") %></b>
				<%		}
						if (request.getAttribute("industryDesc") != null) {
				%> in industry <b><%= request.getAttribute("industryDesc") %></b>
				<%		}
						if (request.getAttribute("productDesc") != null) {
				%> in product <b><%= request.getAttribute("productDesc") %></b>
				<%		}
						if (request.getAttribute("ebeDesc") != null) {
				%> with EBE Category <b><%= request.getAttribute("ebeDesc") %></b>
				<%		}
				%>
				<br><br>
				<table width="100%" class=tableBorder border=0 cellpadding=1 cellspacing=1>
					<thead class=cellColor>
						<%
								if (assignedToUserId.equals("%")) {
						%> <td valign="top"><b>Assigned To</b></td>
						<%		}
						%>
						<td valign="top"><b>Customer</b></td>
						<td valign="top"><b>Product</b></td>
						<td valign="top"><b>EBE Category</b></td>
						<td valign="top"><b>Action/Objective</b></td>
						<td valign="top"><b>Deadline</b></td>
						<td valign="top"><b>Complete ?</b></td>
						<td valign="top"><b>Results</b></td>
						<td valign="top"><b>Attached Documents</b></td>
					</thead>
					<%
					if(tasks.size()==0){
						if (assignedToUserId.equals("%")) {
						%>
						<tr class=cellColor><td colspan="8">No Tasks Found.</td></tr>
						<%
						}else{
						%>
						<tr class=cellColor><td colspan="7"><br>&nbsp;No Tasks Found.</td></tr>
						<%
						}
					}
							for (int i=0; i < tasks.size(); i++) {
								PurchaseActionProgram pap = (PurchaseActionProgram) tasks.get(i);
					%> <tr class=cellColor>
						<%
									if (assignedToUserId.equals("%")) {
						%> <td><%= pap.getAssignedUserName() %></td>
						<%			}
						%> <td>
							<a href="OEMAcctPlan?page=salesplan&cust=<%= pap.getCustomer() %>&pap=<%= pap.getProduct() %>">
								<%= pap.getCustomerName() %></a></td>
						<td><%= pap.getProductDescription() %></td>
						<td><%= pap.getEBEDescription() %></td>
						<td><%=  pap.getAction() %></td>
						<td><%= pap.getScheduleAsString() %></td>
						<%
										if (pap.isComplete()) {
						%> <td>Yes</td>
						<%				}
										else {
						%> <td>No</td>
						<%				}
						%> <td><%= pap.getResults() %></td>
						<td>
						<%
							ArrayList taskAttachments = pap.getAttachments();
							for (int attIndex=0; attIndex < taskAttachments.size(); attIndex++){
								TaskAttachment attachment = (TaskAttachment) taskAttachments.get(attIndex);
						%>
								<a href="javascript:newWindow('TaskAttachmentDisplay?docId=<%= attachment.getId()  %>','AttachmentDisplay')"><%= attachment.getFileName() %></a><br>
						<%	}
						%>
						</td>
					</tr>
					<%		}
					%>
				</table>
			</td>
		</tr>
	</table>
	</body>
</html>
