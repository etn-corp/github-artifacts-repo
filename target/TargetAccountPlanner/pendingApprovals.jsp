<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.AccountApplicationCompleted" %>
<%@ include file="analytics.jsp" %>
<%
UserApprovals userApprovals = (UserApprovals) request.getAttribute("userApprovals");
ArrayList distributors = userApprovals.getDistributorsRequests();
ArrayList projects = userApprovals.getProjectsRequests();
ArrayList targetMarketPlans = userApprovals.getTargetMarketPlansRequests();
ArrayList customers = userApprovals.getCustomerRequests();
ArrayList terminations = userApprovals.getTerminationsRequests();
ArrayList moduleChanges = userApprovals.getModuleChangeRequests();
HashMap tmAccountMap = userApprovals.getTmAccount();

boolean showCustomerBox = userApprovals.isShowCustomerBox();
boolean showDistributorBox = userApprovals.isShowDistributorBox();
boolean showTerminationBox = userApprovals.isShowTerminationBox();
boolean showProjectBox = userApprovals.isShowProjectBox();
boolean showTargetMarketBox = userApprovals.isShowTargetMarketBox();
boolean showModuleChangeBox = userApprovals.isShowModuleChangeRequestBox();

ArrayList alOfCustomersPriorApproversAL = (ArrayList) request.getAttribute("alOfCustomersPriorApproversAL");
ArrayList alOfDistributorsPriorApproversAL = (ArrayList) request.getAttribute("alOfDistributorsPriorApproversAL");
ArrayList alOfTerminationsPriorApproversAL = (ArrayList) request.getAttribute("alOfTerminationsPriorApproversAL");
ArrayList alOfModulePriorApproversAL = (ArrayList) request.getAttribute("alOfModulePriorApproversAL");
ArrayList alOfTargetMarketPlansPriorApproversAL = (ArrayList) request.getAttribute("alOfTargetMarketPlansPriorApproversAL");


ArrayList commitmentApprovals = (ArrayList)request.getAttribute("ccrList");
Hashtable categoryApprovals = (Hashtable)request.getAttribute("ccaList");
String managerType = (String)request.getAttribute("managerType");

%>

<HTML>

<%@ include file="./SMRCHeader.jsp" %>

<script language='javascript' src='<%= jsURL %>layerHandler.js'></script>
<script language="javascript">
  
	function hideShow(layerName, showExplanationLayer) {
	
		if(showExplanationLayer == true){
			hideAllLayers();
			showLayer(layerName);
		}else {
			hideAllLayers();
			//hideLayer(layerName);
		}
	}
	
	function submitReviseResubmit(location, number, type, radioButtonsCount){
	
		var selectedPriorApproverWorkflowApprovalId = "";
		//var radioButtonsCount = eval('document.pendingApprovalsForm.' + 'reviseResubmit' + type + 'Approver_' + number).length;
		var selectedPriorApproverTargetMarketStatus = "";
		
		//I can't iterate through radio buttons (as done in the else below) when there is only 1 radio button and hence the special case to check for radioButtonsCount == 1
        if(radioButtonsCount == 1) {
            selectedPriorApproverWorkflowApprovalId = eval('document.pendingApprovalsForm.' + 'reviseResubmit' + type + 'Approver_' + number).value;
            //selectedPriorApproverTargetMarketStatus = "S";//since there is only one radio button, it means that there's only Initial Account Information.
            selectedPriorApproverTargetMarketStatus = getTargetMarketStatus();
        } else {
			for(i = 0; i < radioButtonsCount; i++){
				if(eval('document.pendingApprovalsForm.' + 'reviseResubmit' + type + 'Approver_' + number + '[' + i + ']').checked){
					selectedPriorApproverWorkflowApprovalId = eval('document.pendingApprovalsForm.' + 'reviseResubmit' + type + 'Approver_' + number + '[' + i + ']').value;
					
					/*if(i == 0){//Initial Account Information
						selectedPriorApproverTargetMarketStatus = "S";
					}
					else if (i == 1){//District Manager
						selectedPriorApproverTargetMarketStatus = "B";
					}*/
					
					selectedPriorApproverTargetMarketStatus = getTargetMarketStatus();
				}
			}
	    }
	    
		var explanation = eval('document.pendingApprovalsForm.reviseResubmit'+type+'Explanation_'+number).value;
		if(selectedPriorApproverWorkflowApprovalId != "" && explanation.length > 0 && explanation.length <= 2000){
			location = location + "&reviseResubmitExplantion=" + explanation + "&selectedPriorApproverWorkflowApprovalId=" + selectedPriorApproverWorkflowApprovalId + "&targetMarketStatus=" + selectedPriorApproverTargetMarketStatus;
			document.location = location;
		}
		else if (selectedPriorApproverWorkflowApprovalId == ""){
			alert("Please pick a prior approver if you want to use the 'Revise Resubmit' option.");
		}
		else if (explanation.length <= 0){
			alert("Please enter something in the Explanation Text before clicking Submit.")
		}
		else if (explanation.length > 2000) {
			alert("The explanation entered exceeds the maximum limit of 2000 characters.");
		}
	}
		
	function goto(location, layer){
		hideShow(layer, false);
		document.location = location;
	}
	
	function hideAllLayers(){
	
		for(i=0;i<document.pendingApprovalsForm.customersSize.value;i++){
			hideLayer("reviseResubmitCustomerExplanationDiv_" + i);
		}
		for(i=0;i<document.pendingApprovalsForm.distributorsSize.value;i++){
			hideLayer("reviseResubmitDistributorExplanationDiv_" + i);
		}
		for(i=0;i<document.pendingApprovalsForm.terminationsSize.value;i++){
			hideLayer("reviseResubmitTerminationExplanationDiv_" + i);
		}
		
		/*for(i=0;i<document.pendingApprovalsForm.projectsSize.value;i++){
			hideLayer("reviseResubmitProjectExplanationDiv_" + i);
		}*/
		
		for(i=0;i<document.pendingApprovalsForm.targetMarketPlansSize.value;i++){
			hideLayer("reviseResubmitTargetMarketPlansExplanationDiv_" + i);
		}
		
		for(i=0;i<document.pendingApprovalsForm.moduleChangeSize.value;i++){
			hideLayer("reviseResubmitModuleExplanationDiv_" + i);
		}
		
	}
	
	function updateReviseResubmitApproverWorkflowStepName(workflowStepName){
		document.pendingApprovalsForm.reviseResubmitApproverWorkflowStepName.value = workflowStepName;
	}
	
	function getTargetMarketStatus(){
		
		var workflowStepName = document.pendingApprovalsForm.reviseResubmitApproverWorkflowStepName.value;
		
		if(workflowStepName == "<%=WorkflowStep.STEP_NAME_INITIAL_ACCOUNT_INFO%>"){
			return "S";
		}
		else if(workflowStepName == "<%=WorkflowStep.STEP_NAME_DISTRICT_MANAGER%>"){
			return "B";
		}
		else{
			alert("workflowStepName error. workflowStepName = " + workflowStepName);
		}
	}

	function approveReject(which,who,action,vcn) {
		if(action=='reset') {
			document.forms[0].AUTH.value = 'RESET';
			document.forms[0].submit();
		} else {
			if(which == "commitment") {
				window.location = 'CommitmentApproveReject?who='+who+'&action='+action+'&vcn='+vcn;
			} else if(which == "category") {
				window.location = 'ModifySegmentApproveReject?WHO='+who+'&ACTION='+action+'&VCN='+vcn;
			} else {
				alert("Error: Neither Commitment nor Category.");
				return false;
			}
		}
	}		
</script>

<%

%>

<form action="/PendingApprovals" method="post" name="pendingApprovalsForm">

<input type=hidden name="customersSize" value="<%=(customers != null) ? customers.size() : 0%>">
<input type=hidden name="distributorsSize" value="<%=(distributors != null) ? distributors.size() : 0%>">
<input type=hidden name="terminationsSize" value="<%=(terminations != null) ? terminations.size() : 0%>">
<input type=hidden name="projectsSize" value="<%=(projects != null) ? projects.size() : 0%>">
<input type=hidden name="targetMarketPlansSize" value="<%=(targetMarketPlans != null) ? targetMarketPlans.size() : 0%>">
<input type=hidden name="moduleChangeSize" value="<%=(moduleChanges != null) ? moduleChanges.size() : 0%>">
<input type=hidden name="reviseResubmitApproverWorkflowStepName" value="">

<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>
		<span class="crumbarrow">&gt;</span>        
		<span class="crumbcurrent">Pending Approvals</span>
      </p> 
    </td>
  </tr>
</table>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="10">&nbsp;</td>
    <td width="750" valign="top" align="center"> 
    <p>&nbsp;</p>
		<div class=heading2 align="left">Pending Approvals</div>
		<% if (showCustomerBox){ 
		%>
		
			<br><br>
			<table class=tableBorder cellspacing=1 cellpadding=1 border=0 width="75%" >
				<caption class=heading3>Customer Requests</caption>
			<%
			    if (customers.size() == 0){
			%>
					<tr class=cellColor>
						<td class=smallFontL colspan="2">There are currently no customer requests</td>
					</tr>
			
			<%    } else {
			        for (int i=0; i < customers.size(); i++){
			            WorkflowStep step = (WorkflowStep) customers.get(i);
			            
			            ArrayList currentPriorAppoversAL = (ArrayList) alOfCustomersPriorApproversAL.get(i);
			%>
					<tr class=cellColor>
							<td class=smallFontL><a href="AccountProfile?page=workflow&acctId=<%= step.getVcn() %>"><%= step.getCustomerName() %></a></td>
						<%-- <td class=smallFontC  width="40%"><a href="PendingApprovals?page=pendingapprovals&requestType=workflow&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=approve&type=customer">Approve</a> / <a href="PendingApprovals?page=pendingapprovals&requestType=workflow&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=reject&type=customer">Reject</a> / <a href="PendingApprovals?page=pendingapprovals&requestType=workflow&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=revise&type=customer">Revise & Resubmit</a> </td> --%>

							<td class=smallFontC  width="40%">
								<a href="#" onclick='goto("PendingApprovals?page=pendingapprovals&requestType=workflow&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=approve&type=customer", "reviseResubmitCustomerExplanationDiv_<%=i%>")'>Approve</a>
								 / <a href="#" onclick='goto("PendingApprovals?page=pendingapprovals&requestType=workflow&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=reject&type=customer", "reviseResubmitCustomerExplanationDiv_<%=i%>")'>Reject</a>
								  / <a href="#" onclick='hideShow("reviseResubmitCustomerExplanationDiv_<%=i%>", true);return false;'>Revise & Resubmit</a> 
							</td>
													
							<td>
								<div id="reviseResubmitCustomerExplanationDiv_<%=i%>" style="display: none">
									<table>
										<tr>
											<td>
												<u>Workflow Step, Name</u>
											</td>
										</tr>
			<%
									int currentPriorAppoversALSize = currentPriorAppoversAL.size();
									if(currentPriorAppoversALSize == 0){
			%>						
										<tr>
											<td>
												No prior approvals.
											</td>
										</tr>		
			<%						
									}
									else{											
										for(int currentPriorAppoversALIndex=0; currentPriorAppoversALIndex < currentPriorAppoversALSize; currentPriorAppoversALIndex++){
											
											WorkflowPriorApprover currentWorkflowPriorApprover = (WorkflowPriorApprover)currentPriorAppoversAL.get(currentPriorAppoversALIndex);
				%>
											<tr>
												<td>
													<INPUT TYPE=radio NAME="reviseResubmitCustomerApprover_<%=i%>" onclick='updateReviseResubmitApproverWorkflowStepName("<%=currentWorkflowPriorApprover.getWorkflowStepName()%>");' VALUE="<%=currentWorkflowPriorApprover.getWorkflowApprovalId()%>"><%=currentWorkflowPriorApprover.getWorkflowStepName()%>, <%=currentWorkflowPriorApprover.getPriorApproverFirstName()%>&nbsp;<%=currentWorkflowPriorApprover.getPriorApproverLastName()%>
												</td>
											</tr>											
				<%							
										}
	
				%>																				
											<tr>
												<td>
													Explanation Text:<textarea rows="3" cols="20" name="reviseResubmitCustomerExplanation_<%=i%>"></textarea>
													<br><a href="#" title="Explain what needs updated by prior approver in TAP." onclick="alert('Explain what needs updated by prior approver in TAP.');">Help</a>
												</td>
												<td>
													<a href="#" onclick='submitReviseResubmit("PendingApprovals?page=pendingapprovals&requestType=workflow&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=revise&type=customer", "<%=i%>", "Customer", "<%=currentPriorAppoversALSize%>")'> Submit </a>
												</td>
											</tr>
				<%
									}	
				%>							
									</table>
								</div>
							</td>
					</tr>
		    <%  	}
			    }
	        %>
					
			</table>
		<% } 
		   if (showDistributorBox){
		%>
			  
			<br><br>
			<table class=tableBorder cellspacing=1 cellpadding=1 border=0 width="75%">
				<caption class=heading3>Distributor Requests</caption>
			<%
			    if (distributors.size() == 0){
			%>
					<tr class=cellColor>
						<td class=smallFontL colspan="2">There are currently no distributor requests</td>
					</tr>
			
			<%    } else {
			        for (int i=0; i < distributors.size(); i++){
			            WorkflowStep step = (WorkflowStep) distributors.get(i);
			            
			            ArrayList currentPriorAppoversAL = (ArrayList) alOfDistributorsPriorApproversAL.get(i);
			%>
					<tr class=cellColor>
							<td class=smallFontL><a href="AccountProfile?page=workflow&acctId=<%= step.getVcn() %>"><%= step.getCustomerName() %></a></td>
						<%--	<td class=smallFontC  width="20%"><a href="PendingApprovals?page=pendingapprovals&requestType=workflow&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=approve&type=distributor">Approve</a> / <a href="PendingApprovals?page=pendingapprovals&requestType=workflow&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=reject&type=distributor">Reject</a> <a href="PendingApprovals?page=pendingapprovals&requestType=workflow&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=revise&type=distributor">Revise & Resubmit</a> </td>	--%>		
							
							<td class=smallFontC  width="40%">
								<% AccountApplicationCompleted aac = new AccountApplicationCompleted(); %>
								<% if(aac.AccountCompleted(step.getVcn()).equals("Y")) { %>
									<a href="#" onclick='goto("PendingApprovals?page=pendingapprovals&requestType=workflow&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=approve&type=distributor", "reviseResubmitDistributorExplanationDiv_<%=i%>")'>Approve</a>
								<% } else { %>
									Not Completed
								<% } %>
								 / <a href="#" onclick='goto("PendingApprovals?page=pendingapprovals&requestType=workflow&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=reject&type=distributor", "reviseResubmitDistributorExplanationDiv_<%=i%>")'>Reject</a>
								  / <a href="#" onclick='hideShow("reviseResubmitDistributorExplanationDiv_<%=i%>", true);return false;'>Revise & Resubmit</a> 
							</td>
													
							<td>
					
								<div id="reviseResubmitDistributorExplanationDiv_<%=i%>" style="display: none">
									<table>
										<tr>
											<td>
												<u>Workflow Step, Name</u>
											</td>
										</tr>
			<%
									int currentPriorAppoversALSize = currentPriorAppoversAL.size();											
									if(currentPriorAppoversALSize == 0){
			%>						
										<tr>
											<td>
												No prior approvals.
											</td>
										</tr>		
			<%						
									}
									else{
										for(int currentPriorAppoversALIndex=0; currentPriorAppoversALIndex < currentPriorAppoversALSize; currentPriorAppoversALIndex++){
											
											WorkflowPriorApprover currentWorkflowPriorApprover = (WorkflowPriorApprover)currentPriorAppoversAL.get(currentPriorAppoversALIndex);
											
				%>
											<tr>
												<td>
													<INPUT TYPE=radio NAME="reviseResubmitDistributorApprover_<%=i%>" onclick='updateReviseResubmitApproverWorkflowStepName("<%=currentWorkflowPriorApprover.getWorkflowStepName()%>");' VALUE="<%=currentWorkflowPriorApprover.getWorkflowApprovalId()%>"><%=currentWorkflowPriorApprover.getWorkflowStepName()%>, <%=currentWorkflowPriorApprover.getPriorApproverFirstName()%>&nbsp;<%=currentWorkflowPriorApprover.getPriorApproverLastName()%>
												</td>
											</tr>											
				<%							
										}
	
				%>									
											<tr>
												<td>
													Explanation Text:<textarea rows="3" cols="20" name="reviseResubmitDistributorExplanation_<%=i%>"></textarea>
													<br><a href="#" title="Explain what needs updated by prior approver in TAP." onclick="alert('Explain what needs updated by prior approver in TAP.');">Help</a>
												</td>
												<td>
													<a href="#" onclick='submitReviseResubmit("PendingApprovals?page=pendingapprovals&requestType=workflow&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=revise&type=distributor", "<%=i%>", "Distributor", "<%=currentPriorAppoversALSize%>")'> Submit </a>
												</td>
											</tr>
				<%
									}
				%>							
									</table>
								</div>
							</td>
					</tr>
		    <%  	}
			    }
	        %>
			</table>
		<% }
		   if (showTerminationBox){
		%>
			
			<br><br>
			<table class=tableBorder cellspacing=1 cellpadding=1 border=0 width="75%">
				<caption class=heading3>Distributor Termination Requests</caption>
			<%
			    if (terminations.size() == 0){
			%>
					<tr class=cellColor>
						<td class=smallFontL colspan="2">There are currently no distributor termination requests</td>
					</tr>
			
			<%    } else {
			        for (int i=0; i < terminations.size(); i++){
			            WorkflowStep step = (WorkflowStep) terminations.get(i);
			            
			            ArrayList currentPriorAppoversAL = (ArrayList) alOfTerminationsPriorApproversAL.get(i);
			%>
					<tr class=cellColor>
							<td class=smallFontL><a href="AccountProfile?page=workflow&acctId=<%= step.getVcn() %>"><%= step.getCustomerName() %></a></td>
						<%-- <td class=smallFontC  width="20%"><a href="PendingApprovals?page=pendingapprovals&requestType=workflow&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=approve&type=termination">Approve</a> / <a href="PendingApprovals?page=pendingapprovals&requestType=workflow&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=reject&type=termination">Reject</a> <a href="PendingApprovals?page=pendingapprovals&requestType=workflow&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=revise&type=termination">Revise & Resubmit</a> </td> --%>
						
							<td class=smallFontC  width="40%">
								<a href="#" onclick='goto("PendingApprovals?page=pendingapprovals&requestType=workflow&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=approve&type=termination", "reviseResubmitTerminationExplanationDiv_<%=i%>")'>Approve</a>
								 / <a href="#" onclick='goto("PendingApprovals?page=pendingapprovals&requestType=workflow&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=reject&type=termination", "reviseResubmitTerminationExplanationDiv_<%=i%>")'>Reject</a>
								  / <a href="#" onclick='hideShow("reviseResubmitTerminationExplanationDiv_<%=i%>", true);return false;'>Revise & Resubmit</a> 
							</td>
													
							<td>
								<div id="reviseResubmitTerminationExplanationDiv_<%=i%>" style="display: none">
									<table>
										<tr>
											<td>
												<u>Workflow Step, Name</u>
											</td>
										</tr>
			<%
									int currentPriorAppoversALSize = currentPriorAppoversAL.size();											
									if(currentPriorAppoversALSize == 0){
			%>						
										<tr>
											<td>
												No prior approvals.
											</td>
										</tr>		
			<%						
									}
									else{
										for(int currentPriorAppoversALIndex=0; currentPriorAppoversALIndex < currentPriorAppoversALSize; currentPriorAppoversALIndex++){
											
											WorkflowPriorApprover currentWorkflowPriorApprover = (WorkflowPriorApprover)currentPriorAppoversAL.get(currentPriorAppoversALIndex);
				%>
											<tr>
												<td>
													<INPUT TYPE=radio NAME="reviseResubmitTerminationApprover_<%=i%>" onclick='updateReviseResubmitApproverWorkflowStepName("<%=currentWorkflowPriorApprover.getWorkflowStepName()%>");' VALUE="<%=currentWorkflowPriorApprover.getWorkflowApprovalId()%>"><%=currentWorkflowPriorApprover.getWorkflowStepName()%>, <%=currentWorkflowPriorApprover.getPriorApproverFirstName()%>&nbsp;<%=currentWorkflowPriorApprover.getPriorApproverLastName()%>
												</td>
											</tr>											
				<%							
										}
	
				%>									
											<tr>
												<td>
													Explanation Text:<textarea rows="3" cols="20" name="reviseResubmitTerminationExplanation_<%=i%>"></textarea>
													<br><a href="#" title="Explain what needs updated by prior approver in TAP." onclick="alert('Explain what needs updated by prior approver in TAP.');">Help</a>
												</td>
												<td>
													<a href="#" onclick='submitReviseResubmit("PendingApprovals?page=pendingapprovals&requestType=workflow&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=revise&type=termination", "<%=i%>", "Termination", "<%=currentPriorAppoversALSize%>")'> Submit </a>
												</td>
											</tr>
				<%
									}
				%>							
									</table>
								</div>
							</td>
					</tr>
		    <%  	}
			    }
	        %>
			</table>
		<% }
		   if (showProjectBox) {
		%>
			<br><br>
			<table class=tableBorder cellspacing=1 cellpadding=1 border=0 width="75%">
				<caption class=heading3>Target Project Requests</caption>
				
		    <%	if (projects.size() == 0){
		    %>    
				    <tr class=cellColor>
						<td class=smallFontL colspan="2">There are currently no target project requests</td>
					</tr>
		    <%
		    	} else {
		    	    for (int i=0; i < projects.size(); i++){
		    	        TargetProject project = (TargetProject) projects.get(i);
		    %>
					<tr class=cellColor>
							<td class=smallFontL><a href="TargetProjectPopup?projId=<%= project.getId() %>"><%= project.getJobName() %></a></td>
							<td class=smallFontC  width="20%"><a href="PendingApprovals?page=pendingapprovals&requestType=project&action=approve&projId=<%= project.getId() %>">Approve</a> / <a href="PendingApprovals?page=pendingapprovals&requestType=project&action=delete&projId=<%= project.getId() %>">Reject</a> <a href="PendingApprovals?page=pendingapprovals&requestType=project&action=revise&projId=<%= project.getId() %>">Revise & Resubmit</a> </td>
					</tr>
			<% 		}
		    	}
			%>
	
			</table>
		<% }
		   if (showTargetMarketBox){
        %>		
			<br><br>
			<table class=tableBorder cellspacing=1 cellpadding=1 border=0 width="75%">
				<caption class=heading3>Target Market Plan Requests</caption>
			<% if (targetMarketPlans.size() == 0){
			%>
					<tr class=cellColor>
						<td class=smallFontL colspan="2">There are currently no target market plan requests</td>
					</tr>
			<% } else {
			    	for (int i=0; i < targetMarketPlans.size(); i++){
			    	    TargetMarket tm = (TargetMarket) targetMarketPlans.get(i);
			    	    String acctId = (String) tmAccountMap.get(new Integer(tm.getTargetMarketPlanId()));
			    	    			    	    
			    	    ArrayList currentPriorAppoversAL = (ArrayList) alOfTargetMarketPlansPriorApproversAL.get(i);			    	    
			%>
					<tr class=cellColor>
							<td class=smallFontL><a href="TargetMarketSetup?acctId=<%= acctId %>&tmId=<%= tm.getTargetMarketPlanId() %>"><%= tm.getPlanDescription()  %></a></td>
							<td class=smallFontC  width="20%"><a href="PendingApprovals?page=pendingapprovals&requestType=targetMarketPlan&action=approve&tmId=<%= tm.getTargetMarketPlanId() %>">Approve</a> / <a href="PendingApprovals?page=pendingapprovals&requestType=targetMarketPlan&action=reject&tmId=<%= tm.getTargetMarketPlanId() %>">Reject</a> 
							<a href="#" onclick='hideShow("reviseResubmitTargetMarketPlansExplanationDiv_<%=i%>", true);return false;'>Revise & Resubmit</a> </td>
							
							<%-- <a href="PendingApprovals?page=pendingapprovals&requestType=targetMarketPlan&action=revise&tmId=<%= tm.getTargetMarketPlanId() %>">Revise & Resubmit</a> </td> --%>
							
							<td>
								<div id="reviseResubmitTargetMarketPlansExplanationDiv_<%=i%>" style="display: none">
									<table>
										<tr>
											<td>
												<u>Workflow Step, Name</u>
											</td>
										</tr>
			<%
									int currentPriorAppoversALSize = currentPriorAppoversAL.size();											
									if(currentPriorAppoversALSize == 0){
			%>						
										<tr>
											<td>
												No prior approvals.
											</td>
										</tr>		
			<%						
									}
									else{
										for(int currentPriorAppoversALIndex=0; currentPriorAppoversALIndex < currentPriorAppoversALSize; currentPriorAppoversALIndex++){
											
											WorkflowPriorApprover currentWorkflowPriorApprover = (WorkflowPriorApprover)currentPriorAppoversAL.get(currentPriorAppoversALIndex);
				%>
											<tr>
												<td>
													<INPUT TYPE=radio NAME="reviseResubmitTargetMarketPlansApprover_<%=i%>" onclick='updateReviseResubmitApproverWorkflowStepName("<%=currentWorkflowPriorApprover.getWorkflowStepName()%>");' VALUE="<%=currentWorkflowPriorApprover.getWorkflowApprovalId()%>"><%=currentWorkflowPriorApprover.getWorkflowStepName()%>, <%=currentWorkflowPriorApprover.getPriorApproverFirstName()%>&nbsp;<%=currentWorkflowPriorApprover.getPriorApproverLastName()%>
												</td>
											</tr>											
				<%							
										}
	
				%>									
											<tr>
												<td>
													Explanation Text:<textarea rows="3" cols="20" name="reviseResubmitTargetMarketPlansExplanation_<%=i%>"></textarea>
													<br><a href="#" title="Explain what needs updated by prior approver in TAP." onclick="alert('Explain what needs updated by prior approver in TAP.');">Help</a>
												</td>
												<td>
													<a href="#" onclick='submitReviseResubmit("PendingApprovals?page=pendingapprovals&requestType=targetMarketPlan&action=revise&tmId=<%=tm.getTargetMarketPlanId()%>", "<%=i%>", "TargetMarketPlans", "<%=currentPriorAppoversALSize%>")'> Submit </a>
												</td>
											</tr>
				<%
									}
				%>							
									</table>
								</div>
							</td>
					</tr>
			<%		}
				}
			%>
			</table>
		<% } 
		   if (showModuleChangeBox){
		%>
			  
			<br><br>
			<table class=tableBorder cellspacing=1 cellpadding=1 border=0 width="75%">
				<caption class=heading3>Module Change Requests</caption>
			<%
			    if (moduleChanges.size() == 0){
			%>
					<tr class=cellColor>
						<td class=smallFontL colspan="2">There are currently no module change requests</td>
					</tr>
			
			<%    } else {
			        for (int i=0; i < moduleChanges.size(); i++){
			            WorkflowStep step = (WorkflowStep) moduleChanges.get(i);
			            
			            ArrayList currentPriorAppoversAL = (ArrayList) alOfModulePriorApproversAL.get(i);
			%>
					<tr class=cellColor>
							<td class=smallFontL><a href="DistributorProductLoadingModule?acctId=<%= step.getVcn() %>"><%= step.getCustomerName() %></a></td>
							<td class=smallFontC  width="40%">
								<a href="#" onclick='goto("PendingApprovals?page=pendingapprovals&requestType=module&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=approve&type=module&moduleId=<%= step.getModuleChangeRequestId() %>", "reviseResubmitModuleExplanationDiv_<%=i%>")'>Approve</a>
								 / <a href="#" onclick='goto("PendingApprovals?page=pendingapprovals&requestType=module&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=reject&type=module&moduleId=<%= step.getModuleChangeRequestId() %>", "reviseResubmitModuleExplanationDiv_<%=i%>")'>Reject</a>
								  / <a href="#" onclick='hideShow("reviseResubmitModuleExplanationDiv_<%=i%>", true);return false;'>Revise & Resubmit</a> 
							</td>
													
							<td>
					
								<div id="reviseResubmitModuleExplanationDiv_<%=i%>" style="display: none">
									<table>
										<tr>
											<td>
												<u>Workflow Step, Name</u>
											</td>
										</tr>
			<%
									int currentPriorAppoversALSize = currentPriorAppoversAL.size();											
									if(currentPriorAppoversALSize == 0){
			%>						
										<tr>
											<td>
												No prior approvals.
											</td>
										</tr>		
			<%						
									}
									else{
										for(int currentPriorAppoversALIndex=0; currentPriorAppoversALIndex < currentPriorAppoversALSize; currentPriorAppoversALIndex++){
											
											WorkflowPriorApprover currentWorkflowPriorApprover = (WorkflowPriorApprover)currentPriorAppoversAL.get(currentPriorAppoversALIndex);
											
				%>
											<tr>
												<td>
													<INPUT TYPE=radio NAME="reviseResubmitModuleApprover_<%=i%>" onclick='updateReviseResubmitApproverWorkflowStepName("<%=currentWorkflowPriorApprover.getWorkflowStepName()%>");' VALUE="<%=currentWorkflowPriorApprover.getWorkflowApprovalId()%>"><%=currentWorkflowPriorApprover.getWorkflowStepName()%>, <%=currentWorkflowPriorApprover.getPriorApproverFirstName()%>&nbsp;<%=currentWorkflowPriorApprover.getPriorApproverLastName()%>
												</td>
											</tr>											
				<%							
										}
	
				%>									
											<tr>
												<td>
													Explanation Text:<textarea rows="3" cols="20" name="reviseResubmitModuleExplanation_<%=i%>"></textarea>
													<br><a href="#" title="Explain what needs updated by prior approver in TAP." onclick="alert('Explain what needs updated by prior approver in TAP.');">Help</a>
												</td>
												<td>
													<a href="#" onclick='submitReviseResubmit("PendingApprovals?page=pendingapprovals&requestType=module&vcn=<%= step.getVcn() %>&approvalId=<%= step.getApprovalId() %>&action=revise&type=module&moduleId=<%= step.getModuleChangeRequestId() %>", "<%=i%>", "Module", "<%=currentPriorAppoversALSize%>")'> Submit </a>
												</td>
											</tr>
				<%
									}
				%>							
									</table>
								</div>
							</td>
					</tr>
		    <%  	}
			    }
	        %>
			</table>
		<% } %>
		<p>
		<% if(commitmentApprovals != null || commitmentApprovals.size() > 0) { %>
			<table class=tableBorder cellspacing=1 cellpadding=1 border=0 width="75%">
				<caption class=heading3>Commitment Level Change Requests</caption>
				<% 
					for(int i=0; i < commitmentApprovals.size(); i++) { 
					  CommitmentChangeRequest ccr = (CommitmentChangeRequest)commitmentApprovals.get(i); 
				%>
					<tr class=cellColor>
						<td class=smallFontL>&nbsp;<a href="DistributorSignup?page=commitment&acctId=<%= ccr.getVcn() %>"><%=ccr.getName()%>(<%=ccr.getVcn()%>)</a></td>
						<td class=smallFontL>
							<a href="#" onClick='javascript:approveReject("committment","dm","approve","<%=ccr.getVcn()%>")'>Approve</a>
							| 
							<a href="#" onClick='javascript:approveReject("committment","dm","reject","<%=ccr.getVcn()%>")'>Reject</a>
						</td>
					</tr>
				<% } %>
			</table>
		<% } %>
		<p>
		<% if(categoryApprovals != null || categoryApprovals.size() > 0) { %>
			<table class=tableBorder cellspacing=1 cellpadding=1 border=0 width="75%">
				<caption class=heading3>Customer Category Change Requests</caption>
				<% 
				Enumeration keys = categoryApprovals.keys();

				while(keys.hasMoreElements()) {
					String vcn = (String)keys.nextElement();
					String name = (String)categoryApprovals.get(vcn);
				%>
					<tr class=cellColor>
						<td class=smallFontL>&nbsp;<a href="AccountProfile?acctId=<%=vcn%>"><%=name%>(<%=vcn%>)</a></td>
						<td class=smallFontL>
							<a href="#" onClick='javascript:approveReject("category","<%=managerType%>","Y","<%=vcn%>")'>Approve</a>
							| 
							<a href="#" onClick='javascript:approveReject("category","<%=managerType%>","R","<%=vcn%>")'>Reject</a>
						</td>
					</tr>
				<% } %>
			</table>
		<% } %>
    </td>
  </tr>
</table>

</form>
<br><br>			
</BODY>
</HTML>	