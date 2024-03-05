<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<html>
<%@ include file="./SMRCHeader.jsp" %>
<%@ include file="analytics.jsp" %>

<%
Workflow workflow = (Workflow)request.getAttribute("workflow");
ArrayList steps = workflow.getWorkflowSteps();
String workflowType = workflow.getWorkflowType();

Account acct = header.getAccount();//(Account)request.getAttribute("acct");
User usr = header.getUser();//(User)request.getAttribute("usr");

boolean ableToUpdate = usr.ableToUpdate(acct.getDistrict()) || usr.hasSegmentOverride(acct) || (usr.equals(acct.getUserIdAdded()) && acct.isProspect());
String msg = (String)request.getAttribute("msg");
String preQual = (String)request.getAttribute("preQual");

String distTermUrl = "";           
String extraInfo = "";

AccountRegistrationResults registrationResults = (AccountRegistrationResults)request.getAttribute("registrationResults");

String alertMessage="";
String vistaStatus=null;

String confirmationMessage = null;
String vistaReferenceNumber = null;
String vistaCustomerNumber = null;
String districtRegisteredTo = null;

if(registrationResults!=null){
	confirmationMessage = registrationResults.getConfirmationMessage();
	vistaReferenceNumber = registrationResults.getVistaReferenceNumber();
	districtRegisteredTo = registrationResults.getDistrictRegisteredTo();
	//vistaCustomerNumber = registrationResults.getRegisteredAccountVCN();

	if(registrationResults.isRegistrationImmediate()){
		vistaStatus="Vista account setup SUCCESSFUL.";
		vistaCustomerNumber = registrationResults.getRegisteredAccountVCN();
	}else{
		vistaStatus="Vista account setup is PENDING.";
		alertMessage="Something in Vista has prevented this account from a complete registration.  The account <b>is</b> in Vista and can be reference by the number below but has <b>not</b> been assigned a Vista customer number.<BR><BR>Vista administrators will address the issue and SMRC will be updated accordingly.<BR><BR>No further action is necessary.<BR><BR>If you have any questions regarding this request, please email <a href=\"mailto:SalesReporting@eaton.com\">SalesReporting@eaton.com</a>";
		vistaCustomerNumber = acct.getVcn();
	}
	
}else{

	confirmationMessage = "";
	vistaReferenceNumber = acct.getVistaReferenceNumber();
	districtRegisteredTo = "";
	vistaCustomerNumber = acct.getVcn();

	if(workflow.isRejected()){
		vistaStatus="This account was rejected and will not be sent to Vista";
	}else if(workflow.isComplete()){ 
		if(acct.isRejected()) {
		    vistaStatus="This account was rejected from Vista.";
		}else if(acct.isPending()){
		    vistaStatus="This account is pending in Vista.";
		    alertMessage="Something in Vista has prevented this account from a complete registration.  The account <b>is</b> in Vista and can be reference by the number below but has <b>not</b> been assigned a Vista customer number.<BR><BR>Vista administrators will address the issue and SMRC will be updated accordingly.<BR><BR>No further action is necessary.<BR><BR>If you have any questions regarding this request, please email <a href=\"mailto:SalesReporting@eaton.com\">SalesReporting@eaton.com</a>";
		}else {
		    vistaStatus="This account is in Vista.";
		}
	}else if(workflow.isLastApproval()){
		vistaStatus="This account will be sent to Vista after this approval";
	}else{
		vistaStatus="This account will be sent to Vista after all steps are approved.";
	}
}

if(workflowType.equals("Distributor Termination")){
	distTermUrl="&termination=true";
}

String statusBarMsg = "Applying workflow change.  Please be patient...";
if(workflow.isLastApproval()){
	statusBarMsg="Registering account in Vista.  Please be patient...";
}

ArrayList priorApprovers = new ArrayList();
priorApprovers = (ArrayList)request.getAttribute("priorApprovers");
String applicationFilledOutInFull = (String)request.getAttribute("complete");
String rejectedMsg = "";
%>
	<script language='javascript' src='<%= jsURL %>layerHandler.js'></script>

	<script language="javascript">
		function resetWorkflow() {
			document.forms[0].action = "ResetWorkflow";
			document.forms[0].submit();
		}
		
		function hideShow(layerName, showExplanationLayer) {
			if(showExplanationLayer == true){
				hideAllLayers();
				showLayer(layerName);
			}else {
				hideAllLayers();
				//hideLayer(layerName);
			}
		}
			
		function hideAllLayers(){
	
			for(i=0;i<document.theform.priorApproversSize.value;i++){
				hideLayer("reviseResubmitExplanationDiv_" + i);
			}
		}
	
		function submitReviseResubmit(location, number, radioButtonsCount){
	
			var selectedPriorApproverWorkflowApprovalId = "";
			
			//I can't iterate through radio buttons (as done in the else below) when there is only 1 radio button and hence the special case to check for radioButtonsCount == 1
	        if(radioButtonsCount == 1) {
	            selectedPriorApproverWorkflowApprovalId = eval('document.theform.' + 'reviseResubmitApprover_' + number).value;
	        } else {
				for(i = 0; i < radioButtonsCount; i++){
					if(eval('document.theform.reviseResubmitApprover_' + number + '[' + i + ']').checked){
						selectedPriorApproverWorkflowApprovalId = eval('document.theform.reviseResubmitApprover_' + number + '[' + i + ']').value;
					}
				}
		    }
		    
			var explanation = eval('document.theform.reviseResubmitExplanation_'+number).value;
			if(selectedPriorApproverWorkflowApprovalId != "" && explanation.length > 0 && explanation.length <= 2000){
				location = location + "&reviseResubmitExplantion=" + explanation + "&selectedPriorApproverWorkflowApprovalId=" + selectedPriorApproverWorkflowApprovalId;
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
	
		function statusBar(){	
			setInnerHTML('STATUS_BAR','<br><%= statusBarMsg %><br><br><img src="<%= sImagesURL %>progress.gif" border="0"><br>');
			setInnerHTML('APPROVE_REJECT','');
		}
		var setInnerHTML = function( id, str ){
			if(!document.getElementById) return; // Not Supported
			if(document.getElementById){
				document.getElementById(id).innerHTML = str;
			}
		}
		
		function validateTextAreas() {
			notes = document.theform.NOTES;
	    if ( getLineCount(notes, 60) > 5) {
	        alert('Cannot exceed 5 lines of input in the Notes');
	        return false; 
	    }
		}
		
		function confirmReject() {
			if(!confirm("Rejecting this approval will permanently lock the account as rejected.\nAre you sure that you want to do this?")){
				return false;
			}else{
				statusBar();
			}

		}
		
		image1 = new Image(49,8);
		image1.src="<%= sImagesURL %>progress.gif"
		
		</script>
		<script language="JavaScript">
		  javascript:window.history.forward(1);
		</script>
		
<form action="AccountProfile" onSubmit="return validateTextAreas()" name="theform" method="POST">		
		
<input type=hidden name="priorApproversSize" value="<%=(priorApprovers != null) ? priorApprovers.size() : 0%>">		
		
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;
		</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>
		<span class="crumbarrow">&gt;</span>
		<span class="crumbcurrent">Send to Vista</span></p> 
    </td>
  </tr>
</table>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p>&nbsp;</p>
      <table width="750" border="0" cellspacing="0" cellpadding="0">
        <tr valign="top"> 
          <td width="140"> 
						<%@ include file="./accountLeftNav.jsp" %>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
          </td>
          <td width="10" align="left" background="<%= sImagesURL %>divider.gif">&nbsp;</td>
         <td width="600">	
					<% if(msg.length()!=0){ %>
								<blockquote><font class="crumbcurrent"><%= msg %></font></blockquote>
					<% } %>
           <p class="heading2">Workflow - <%= workflowType %></p>
           <p class="heading3">Account: <%= acct.getCustomerName() %></p>
				<% if(!workflowType.equals("Distributor Termination")){ %>
   				<blockquote>
   				<p>
								<table width="500" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="10">&nbsp;</td>
										<td width="490" colspan="2" align="left"><span class="heading3">Vista Details</span></td>
									</tr>
									<tr>
										<td width="10">&nbsp;</td>
										<td width="490" colspan="2">
											<table width="100%" border="0" cellspacing="2" cellpadding="2">
												<tr>
													<td valign="top" width="160"><font class="crumb">Vista Status:</font></td>
													<td valign="top">
													<span class="crumbcurrent"><%= vistaStatus %></span>
													</td>
												</tr>
												<% if(confirmationMessage.trim().length()!=0){ %>
												<tr>
													<td valign="top" width="160"><font class="crumb">Confirmation:</font></td>
													<td valign="top"><span class="crumbcurrent"><%= confirmationMessage %></span></td>
												</tr>
												<% } %>
												<% if(districtRegisteredTo.trim().length()!=0){ %>
												<tr>
													<td valign="top" width="160"><font class="crumb">District:</font></td>
													<td valign="top"><span class="crumbcurrent"><%= districtRegisteredTo %></span></td>
												</tr>		
												<% } %>										
												<% if(preQual.trim().length()!=0 && acct.isProspect()){ %>
												<tr>
													<td valign="top" width="160"><font class="crumb">Required to Send:</font></td>
													<td valign="top"><%= preQual %></td>
												</tr>												
												<% } %>
												<% if(alertMessage.trim().length()!=0){ %>
												<tr>
													<td valign="top" width="160"><font class="crumb">Alert Message:</font></td>
													<td valign="top"><%= alertMessage %></td>
												</tr>
												<% } %>
												<% if(!vistaCustomerNumber.substring(0,1).equals("P")){ %>
												<tr>
													<td valign="top" width="160"><font class="crumb">Vista Customer Number:</font></td>
													<td valign="top"><%= acct.getVcn() %></td>
												</tr>
												<% } %>
												<% if(vistaReferenceNumber.trim().length()!=0){ %>
												<tr>
													<td valign="top" width="160"><font class="crumb">Vista Reference Number:</font></td>
													<td valign="top"><%= vistaReferenceNumber %></td>
												</tr>
												<% } %>
												<tr>
												<td colspan="2" align="center"><div id="STATUS_BAR"></div></td>
												</tr>
																				
											</table>
										</td>
									</tr>
								</table>

					</p>
					</blockquote>
          <br><br>
			<% } %>
			
			
	<% if(steps.size()!=0){ %>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
					<td><font class="heading2">Step</font></td><td><font class="heading2">Status</font></td><td><font class="heading2">Date</font></td><td><font class="heading2">Action</font></td>	
			</tr>
			<%
			String cellShade = "";
			//boolean sentYet = true;
			boolean isAbleToEditNotes = false;
			
			for(int i=0;i<steps.size(); i++){
				String statusMsg = "";
				String dateChanged = "";
				
				User userApprovedOrRejected=new User();
				boolean ableToApprove = false;

				WorkflowStep step = (WorkflowStep)steps.get(i);	
				String stepUser="";

				if(step.getStepName().equals("Initial Account Information")){
					if(ableToUpdate){
						ableToApprove=true;
					//} else {
						//step.setApprovalId(1);
						//extraInfo = " Application is not completed.";
					}
				}else if(step.getStepName().equals("District Manager")){
					if(!acct.getDistrictManager().getLastName().equals("")){
						stepUser=" (" + acct.getDistrictManager().getLastName() + ", " + acct.getDistrictManager().getFirstName() + ")";
					}
					List idList = acct.getDistrictManager().getSalesIds();
					for (int idindex=0; idindex < idList.size(); idindex++){
					    String thisId = (String) idList.get(idindex);
					    if(thisId.equals(usr.getSalesId())){
					        ableToApprove=true;
					    }
					}
				}else if(step.getStepName().equals("Zone Manager")){
					if(!acct.getZoneManager().getLastName().equals("")){
						stepUser=" (" + acct.getZoneManager().getLastName() + ", " + acct.getZoneManager().getFirstName() + ")";
					}
					List idList = acct.getZoneManager().getSalesIds();
					for (int idindex=0; idindex < idList.size(); idindex++){
					    String thisId = (String) idList.get(idindex);
					    if(thisId.equals(usr.getSalesId())){
					        ableToApprove=true;
					    }
					}
					
				}else if(step.getStepName().equals("Global Channel Manager")){
					if(usr.isChannelMarketingManager() || usr.hasOverrideSecurity()){
						ableToApprove=true;
					}
				}else if(step.getStepName().equals("Credit Manager")){
				    if(usr.isCreditManager() || usr.hasOverrideSecurity()){
						ableToApprove=true;
					}
				}
					
				if(step.isApproved()){
						userApprovedOrRejected = step.getUserApprovedOrRejected();
						statusMsg="Approved (" + userApprovedOrRejected.getLastName() + ", " + userApprovedOrRejected.getFirstName() + ")";
				}else if(step.isRejected()){
						userApprovedOrRejected = step.getUserApprovedOrRejected();
						statusMsg="Rejected (" + userApprovedOrRejected.getLastName() + ", " + userApprovedOrRejected.getFirstName() + ")";
						rejectedMsg = "R";
				}else if(step.isRevisedAndResubmitted()){
						userApprovedOrRejected = step.getUserApprovedOrRejected();
						statusMsg="Pending Resubmission" + extraInfo;
				}
				else if(step.getPrevStepId()==0){
						statusMsg="Pending Approval";
						//sentYet=false;
				}else{
					for(int j=0;j<steps.size(); j++){
						WorkflowStep step2 = (WorkflowStep)steps.get(j);
						if(step.getPrevStepId()==step2.getStepId() && step2.isApproved()  && !step.isApproved()){
							statusMsg="Pending Approval";
						}
					}
					//sentYet=false;
				}
				
				if(step.getDateChanged()==null){
					dateChanged="";
				}else{
					dateChanged = step.getDateChanged().toString();
				}
				if(i%2==0){
					cellShade="";
				}else{
					cellShade=" class=\"cellShade\"";
				}
				
			%>
			<tr<%= cellShade %>>
					<td><%= step.getStepName() %><%= stepUser %></td>
					<td><%= statusMsg %></td>
					<td><%= dateChanged %></td>
					<td>
					<%
					 if((statusMsg.equals("Pending Approval") || statusMsg.equals("Pending Resubmission")) && (ableToApprove || usr.hasOverrideSecurity())){
					 	isAbleToEditNotes=true;

					 	if(preQual.length()==0 || !workflow.isLastApproval()){
					 		if(applicationFilledOutInFull.equals("Y")) {
					 %>
						
							 <div id="APPROVE_REJECT">
							 	<a onclick="javascript: statusBar()" href="AccountProfile?page=workflowApprove&acctId=<%= acct.getVcn() %>&approvalId=<%= step.getApprovalId() %><%= distTermUrl %>">Approve</a> | 
								<a onclick="return confirmReject()" href="AccountProfile?page=workflowReject&acctId=<%= acct.getVcn() %>&approvalId=<%= step.getApprovalId() %><%= distTermUrl %>">Reject</a> |
								<a href="#" onclick='hideShow("reviseResubmitExplanationDiv_<%=i%>", true);return false;'>Revise & Resubmit</a> 
							 </div>
					
					 <%
					 		} else {
					 %>
					 			<div id="APPROVE_REJECT">
					 				Application is not filled out completely.
					 			</div>
					 <%
					 		}
					 	}
					 }
					 %>
					 </td>
					 
					 <td>
							<div id="reviseResubmitExplanationDiv_<%=i%>" style="display: none">
								<table>
									<tr>
										<td>
											<u>Workflow Step, Name</u>
										</td>
									</tr>
		<%
								int priorApproversSize = priorApprovers.size();											
								if(priorApproversSize == 0){
		%>						
									<tr>
										<td>
											No prior approvals.
										</td>
									</tr>		
		<%						
								}
								else{
									for(int priorApproversIndex=0; priorApproversIndex < priorApproversSize; priorApproversIndex++){
										
										WorkflowPriorApprover currentWorkflowPriorApprover = (WorkflowPriorApprover)priorApprovers.get(priorApproversIndex);
			%>
										<tr>
											<td>
												<INPUT TYPE=radio NAME="reviseResubmitApprover_<%=i%>" VALUE="<%=currentWorkflowPriorApprover.getWorkflowApprovalId()%>"><%=currentWorkflowPriorApprover.getWorkflowStepName()%>, <%=currentWorkflowPriorApprover.getPriorApproverFirstName()%>&nbsp;<%=currentWorkflowPriorApprover.getPriorApproverLastName()%>
											</td>
										</tr>											
			<%							
									}

			%>									
										<tr>
											<td>
												Explanation Text:<textarea rows="3" cols="20" name="reviseResubmitExplanation_<%=i%>"></textarea>
												<br><a href="#" title="Explain what needs updated by prior approver in TAP." onclick="alert('Explain what needs updated by prior approver in TAP.');">Help</a>
											</td>
											<td>
												<a href="#" onclick='submitReviseResubmit("AccountProfile?page=workflowRevise&acctId=<%= acct.getVcn() %>&approvalId=<%= step.getApprovalId() %><%= distTermUrl %>", "<%=i%>", "<%=priorApproversSize%>")'> Submit </a>
											</td>
										</tr>
			<%
								}
			%>							
								</table>
							</div>
					 </td>
					 		
			</tr>			
			<%
			}
			%>
			<% if(rejectedMsg.equals("R") && usr.getGroupId() == 7){%>
			<tr>
				<td colspan='4'><hr noshade></td>
			</tr>
			<tr>
				<td>Reset to step...</td>
				<td colspan='2'>
					<select name='workflowStepName'>
						<option>Initial Account Information</option>
						<option>District Manager</option>
						<option>Zone Manager</option>
						<option>Global Channel Manager</option>
						<option>Credit Manager</option>
					</select>
				</td>
				<td><button onClick='resetWorkflow()'>Reset</button>
			</tr>
			<% } %>
		 </table>
										
    <p>&nbsp;</p>

    <%-- <form action="AccountProfile" onSubmit="return validateTextAreas()" name="theform" method="POST"> --%>
    <table align="center" border="0" width="290"><tr><td>
    <%
    if(!workflow.isComplete() && isAbleToEditNotes && acct.isProspect() && !workflowType.equals("Distributor Termination")){
    %>
    Comments to send to Vista Group with account:
    <textarea cols="60" wrap="PHYSICAL" rows="5" name="NOTES"><%= acct.getSendToVistaNotes() %></textarea>
    <br><br>
    <input type="image" src="<%= sImagesURL %>button_save.gif" width="70" height="20"><br>
    * Note - you must click "Save" button after changes to the comments.  <i>Save changes before approving.</i>
    </td></tr>

    <% }else{
    		if(!workflow.isComplete() && !acct.getSendToVistaNotes().equals("")){
		    %>
    		<b>Comments to send to Vista Group with account:</b><br><br>
    		<%= acct.getSendToVistaNotes() %><br>
    		<% }else if(!acct.getSendToVistaNotes().equals("")){ %>
    		<b>Comments sent Vista Group with account:</b><br><br>
    		<%= acct.getSendToVistaNotes() %><br>
     <%	}
    	} %>
    </table>
    <input type="hidden" name="page" value="workflowSaveNotes">
    <input type="hidden" name="acctId" value="<%= acct.getVcn() %>">
    </form>
    
    
    
  <% } %>
    </td>
        </tr>
      </table>
         
    </td>
  </tr>
</table>
<p>&nbsp;</p>

  </body>
</html>
