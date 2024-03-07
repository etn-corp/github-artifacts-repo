<%
ArrayList workflowSteps = (ArrayList)request.getAttribute("workflowSteps");
if(workflowSteps.size()!=0){
%>
<blockquote>
<table width="75%">
			<tr class="cellShade">
				<td colspan="5"><span class="crumbcurrent">Approval Queue</span> - The following accounts require an approval</td>
		
			</tr>
			<tr>
				<td><span class="heading3">Account</span></td>
				<!--<td><span class="heading3">Vista Number</span></td>-->
				<td><span class="heading3">Approval Type</span></td>
				<td><span class="heading3">Waiting since</span></td>
				<td><span class="heading3">Action</span></td>				
			</tr>
			<%
			String dateChanged=null;
			String shade = null;
			for(int i=0;i<workflowSteps.size();i++){
				WorkflowStep step = (WorkflowStep)workflowSteps.get(i);
				String termination = "";
				if(step.getWorkflowName().equals("Distributor Termination")){
					termination = "&termination=true";
				}	
				if(i%2==0){
					shade="";
				}else{
					shade=" class=\"cellShade\"";
				}
				
				if(step.getDateChanged()==null){
					dateChanged="";
				}else{
					dateChanged = step.getDateChanged().toString();
				}
			%>
			<tr<%= shade %>>
					<td><a href="AccountProfile?acctId=<%= step.getVcn() %>"><%= step.getCustomerName() %></a></td>
					<!--<td><%= step.getVcn() %></td>-->
					<td><%= step.getWorkflowName() %></td>
					<td><%= step.getPreviousStepApprovalDate() %></td>
					<td>
					 <a href="AccountProfile?page=workflowApprove<%= termination %>&acctId=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>">Approve</a> | 
					 <a href="AccountProfile?acctId=<%= step.getVcn() %>">View Account</a>
					 </td>		
			</tr>			
			<%
			}
			%>      
</table>
</blockquote>
<% } %>