<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*" %>
<%@ page import="com.eaton.electrical.smrc.util.StringManipulation" %>
<html>
<%@ include file="./SMRCHeader.jsp" %>
<%@ include file="analytics.jsp" %>

<script language="javascript" src="<%= jsURL %>validation/distSignupForm.js"></script>


<%
Distributor dist = (Distributor)request.getAttribute("dist");
Account acct = header.getAccount();//(Account)request.getAttribute("acct");
User usr = header.getUser();//(User)request.getAttribute("usr");
boolean ableToUpdate = usr.ableToUpdate(acct.getDistrict()) || usr.hasSegmentOverride(acct) || (usr.equals(acct.getUserIdAdded()) && acct.isProspect());
if(!usr.hasOverrideSecurity()){
	if(!acct.isProspect()){
		ableToUpdate=false;
	}
}

boolean hasPlatinum = false;
boolean nullInitiator = false;
boolean nullDM = false;
boolean nullChannel = false;

ArrayList commitmentProgram = (ArrayList)request.getAttribute("commitmentProgram");
ArrayList elCodes = (ArrayList)request.getAttribute("electricalLines");
Boolean approvalProcess =  (Boolean)request.getAttribute("approvalProcess");
CommitmentChangeRequest initiator = (CommitmentChangeRequest)request.getAttribute("initiator");
if(initiator == null) {
	nullInitiator = true;
}
CommitmentChangeRequest dm = (CommitmentChangeRequest)request.getAttribute("dm");
if(dm == null) {
	nullDM = true;
}
CommitmentChangeRequest channel = (CommitmentChangeRequest)request.getAttribute("channel");
if(channel == null) {
	nullChannel = true;
} else {
	if(channel.getApprovalFlag().equalsIgnoreCase("Y")) {	
		ableToUpdate = false;
		hasPlatinum = true;
	}
}

ArrayList history = (ArrayList)request.getAttribute("history");

String disabled="";
if(!ableToUpdate){
	disabled=" disabled";
}


int selectedCodeId = -1;
%>

<script language='javascript'>
	function commitmentProgram() {
		var isDist = 'FALSE';
		var distID = '<%=dist.getVcn()%>';
		<%=dist.getVcn().indexOf("P")==-1?"isDist='TRUE';":"isDist='FALSE';"%>
		var comProg = <%=dist.getCommitmentProgram()%>;
		
		if(isDist=="TRUE") {
			//document.getElementById("platinumWarning").style.display = "";
			
			for(i=0; i < document.forms[0].COMMITMENT_PROGRAM.length; i++) {
				if(document.forms[0].COMMITMENT_PROGRAM[i].checked) {
					
					var newCom = document.forms[0].COMMITMENT_PROGRAM[i].value;
					
					if(document.forms[0].COMMITMENT_PROGRAM[i].value == '141') {
						if(!confirm("Platinum level commitment requires approval.\nClick ok if you wish to continue updating this distributor to Platinum or cancel to reset to the previous level.")) {							
							setBackComLevel(comProg);
						} else {
							document.forms[0].AUTH.value = 'YES';
						}
					} else {
						if(document.forms[0].COMMITMENT_PROGRAM[i].value != comProg) {
							if(!confirm("You are requesting a change to the Commitment Program for this distributor.\nA supervisor will be notified of this change by email.\nClick ok if you wish to continue updating the commitment level or cancel to reset to the previous level.")) {							
								setBackComLevel(comProg);
							}	
						}
					}
				}
			}
		} 
	}
	
	function setBackComLevel(comProg) {	
		for(j=0; j < document.forms[0].COMMITMENT_PROGRAM.length; j++) {
			if(document.forms[0].COMMITMENT_PROGRAM[j].value == comProg) {
				document.forms[0].COMMITMENT_PROGRAM[j].checked = true;
				return false;
			}
		}
	}
	
	function approveReject(who,action) {
		if(action=='reset') {
			document.forms[0].AUTH.value = 'RESET';
			document.forms[0].submit();
		} else {
			window.location = 'CommitmentApproveReject?who='+who+'&action='+action+'&vcn='+<%=dist.getVcn()%>;
		}
	}
	
	function openHistory() {
		if(document.getElementById("comHistory").style.display == 'none') {
			document.getElementById("comHistory").style.display = '';
		} else {
			document.getElementById("comHistory").style.display = 'none';
		}
	}
	
</script>

<body>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>

		<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Commitment Level Request</span></p> 		
    </td>
  </tr>
</table>
<form action="CommitmentAuthorization" name="theform" method="POST">
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
			<% if(request.getParameter("save")!=null){ %>
				<blockquote><font class="crumbcurrent">Save Successful</font></blockquote>
			<% } %>
			<p class="heading2">Commitment Level Request</p>
			<table width="600" border="0" cellspacing="5" cellpadding="2">
            	<tr>
           			<td colspan='2' class="heading3">Account: <%= acct.getCustomerName() %> -- <%=acct.getVcn() %></td>
            	</tr>
            	<tr>
   	     			<td colspan='2'>
						<table width="100%" border="0" cellspacing="10" cellpadding="0">
							<tr>
								<td width="47%">
									<div align="left">
									<% if(!hasPlatinum) { %>
										<input type="image" src="<%= sImagesURL %>button_save.gif" width="70" height="20">
									<% } else { %>
										<% if(usr.hasOverrideSecurity() || usr.isChannelMarketingManager()) {%>
											<% disabled = ""; %>
											<a href="#" onClick='javascript:approveReject("cm","reset")'>Remove Platinum Status</a>
										<% } else { %>
											Please check with Global Channel Manager to remove Platinum Commitment Level
										<% } %>
									<% } %>
									</div>
								</td>
								<td width="53%"></td>
							</tr>
							<%
								String checked = "";
								String checkedOther = "";
							%>	
							<tr>
								<td colspan='2'><hr align="center" width="600" size="1" color="#999999" noshade></td>
   	     					</tr>
   	     					<tr>
								<td colspan='2' class="heading3"><span class="crumbcurrent">*</span>Commitment Program</td>
							</tr>
       	 					<tr>
								<td colspan='2'><hr align="center" width="600" size="1" color="#999999" noshade></td>
   	     					</tr>
   	     					<% if(history != null) { 
   	     						if(history.size() > 0) {
   	     					%>
   	     					<tr>
   	     						<td colspan='2'>
   	     							<a href="javascript:openHistory()">View Commitment Level Change History</a>
   	     							<div id="comHistory" style='display:none'>
   	     							<%
	     									for(int i=0; i < history.size(); i++) {
	     										CommitmentHistory ch = (CommitmentHistory)history.get(i);
	     										out.write("\n");
	     										if(ch.getVcn() != null) {
	     											out.write(ch.getApprover_name());
	     											out.write("--");
	     											out.write(ch.getAction());
	     											out.write("--");
	     											out.write(ch.getDate().toString());
	     											out.write("<br>");
	     										}
	     									}
   	     							 %>
   	     							</div>
   	     						</td>
   	     					</tr>
   	     					<% } 
   	     					  }	
   	     					%>
			   	     		<tr>
			   	     			<td>
			   	     			<%	
			   	     				int commitmentProgramSelected = 132;
			   	     				if(dist.getCommitmentProgram()!= 0) {
										commitmentProgramSelected = dist.getCommitmentProgram();
									}
									for(int i=0; i < commitmentProgram.size(); i++){
										CodeType code = (CodeType)commitmentProgram.get(i);
									%>       	
			               			<input type="radio" onClick='commitmentProgram()' name="COMMITMENT_PROGRAM" value='<%= code.getId() %>' <%= code.getId() == commitmentProgramSelected?"checked":"" %>><%= code.getName() %><br>
			           				<% 
			           				}
			               		 %>       
			   	     			</td>
			   	     			
			   	     			<% if(commitmentProgramSelected == 141) { %>
			   	     			<td valign='top'>
			   	     				<% if(approvalProcess.booleanValue()) { %>
			   	     					<table>
			   	     						<tr>
			   	     							<td>Initiated By:</td>
			   	     							<td><%=initiator.getApproverName()%></td>
			   	     							<td><%=initiator.getDateAdded()%></td>
			   	     						</tr>
			   	     						<% if(usr.hasOverrideSecurity() || usr.isDistrictManager() || usr.isChannelMarketingManager() || channel.getApprovalFlag().equalsIgnoreCase("Y")){ %>
			   	     						 <tr>
			   	     							<td>District Manager:</td>
			   	     							<% if(dm.getApprovalFlag().equalsIgnoreCase("N")) {%>
			   	     							 <td>
				   	     								<a href="#" onClick='javascript:approveReject("dm","approve")'>Approve</a>
				   	     								| 
				   	     								<a href="#" onClick='javascript:approveReject("dm","reject")'>Reject</a>
				   	     						 </td>
				   	     						 <td></td>
				   	     						<% } else { %>
				   	     						 <td><%=dm.getApproverName()%></td>
			   	     							 <td><%=dm.getDateAdded()%></td>
			   	     							<% } %>
			   	     						 </tr>
			   	     						 <%if(dm.getApprovalFlag().equalsIgnoreCase("Y")) { %>
			   	     						 <tr>
			   	     							 <td>Channel Manager:</td>
			   	     							<% if(channel.getApprovalFlag().equalsIgnoreCase("N")&& usr.isChannelMarketingManager()) {%> 
			   	     							 <td>
			   	     								<a href="#" onClick='javascript:approveReject("cm","approve")'>Approve</a>
			   	     								| 
			   	     								<a href="#" onClick='javascript:approveReject("cm","reject")'>Reject</a>
			   	     							 </td>
			   	     							 <td></td>
			   	     							<% } else { %>
			   	     								<% if(channel.getApprovalFlag().equalsIgnoreCase("Y")) { %>
			   	     							 	<td><%=channel.getApproverName()%></td>
			   	     							 	<td><%=channel.getDateAdded()%></td>
			   	     							 	<% } %>
			   	     							<% } %>
			   	     						 </tr>
			   	     						 <% } %>
			   	     						<% } %>
			   	     					</table>
			   	     				<% } %>
			   	     			</td>
			   	     			<% } else { %>
			   	     			<td valign='top'>
			   	     				Selecting Platinum requires approval and 100% Commitment to the Core Commitment Products.
			   	     			</td>
			   	     			<td rowspan='4'>
			   	     				<img src='<%= sImagesURL %>Core_Commitment.jpg' border='0'>
			   	     			</td>
			   	     			<% } %>
			   	     		</tr>
			 				<tr>
								<td valign='top'><span class="crumbcurrent">*</span>Projected Eaton Stock Sales:</td>
								<td>
									$<%= StringManipulation.createTextBox("PROJECTED_EATON_SALES_1",""+dist.getProjectedEatonSalesYr1(),true,"10") %> 
									&nbsp;&nbsp;<span class="textgray"><font class="crumbcurrent">*</font> Year 1</span>
									<br>
							    	$<%= StringManipulation.createTextBox("PROJECTED_EATON_SALES_2",""+dist.getProjectedEatonSalesYr2(),true,"10")  %>  
							    	&nbsp;&nbsp;<span class="textgray"><font class="crumbcurrent">*</font> Year 3</span>
			             		</td>
							</tr>
			   	     		<tr>
			   	     			<td valign='top'><span class="crumbcurrent">*</span>Projected % Eaton Stock Sales vs Competitor<br><span class="textgray">(If Exclusive = 100% Eaton)</span></td>
								<td>
									&nbsp;<%= StringManipulation.createTextBox("PROJECTED_SALES_VS_COMP_1",""+dist.getProjectedVScompYr1(),true,"10") %>
									&nbsp;&nbsp;<span class="textgray"><font class="crumbcurrent">*</font> Year 1</span>
									<br>
									&nbsp;<%= StringManipulation.createTextBox("PROJECTED_SALES_VS_COMP_2",""+dist.getProjectedVScompYr2(),true,"10") %>
									&nbsp;&nbsp;<span class="textgray"><font class="crumbcurrent">*</font> Year 3</span>
								</td>
					   		</tr>
			 	     		<tr>
			   	     			<td valign='top'><span class="crumbcurrent">*</span>Competiting Electrical Lines<br><span class="textgray">(Choose as many as apply)</span></td>
			   	     			<td>
			 					<%	
										ArrayList checkedEL = dist.getElectricalLines();
										//System.out.println(checkedEL);
										HashMap elHM = new HashMap();
										for(int i=0;i< checkedEL.size();i++){
											elHM.put((String)checkedEL.get(i),null);
										}
			
										checked = "";
										checkedOther = "";
			
										for(int i=0; i < elCodes.size(); i++){
											CodeType code = (CodeType)elCodes.get(i);
											//if(code.getSeq() > 0) {
												if(elHM.containsKey(""+code.getId())){
													checked=" checked";
												}else{
													checked="";
												}
											//}
										%>       	
			                  			<input type="checkbox" name="ELECTRICAL_LINES" value="<%= code.getId() %>" <%= checked  %>><%= code.getName() %><br>
			              				<% 
			              				}
			               				%>
			   	     			</td>
			   	     		</tr>
			  	     		<tr>
			   	     			<td>Business Justification</td>
			   	     			<td>
			   	     				<a target='_blank' href='http://ecm-prod-cs.etn.com/ecm/idcplg?IdcService=GET_FILE&allowInterrupt=1&RevisionSelectionMethod=LatestReleased&noSaveAs=1&Rendition=Primary&&dDocName=ChannelRiskAnalysis'>
			   	     					Ken's Strategy Sheet
			   	     				</a>
								</td>
			   	     		</tr>
							<tr>
								<td>Reason for Change:</td>
								<td>
									<textarea name="COMMITMENT_REASON" cols="40" rows="4"><%=dist.getCommitmentReason()%></textarea>
								</td>
							</tr>
						</table>
					  	<br>
						<table width="100%" border="0" cellspacing="10" cellpadding="0">
							<tr>
								<td width="47%">
									<div align="left">
									<% if(!hasPlatinum) { %>
										<input type="image" src="<%= sImagesURL %>button_save.gif" width="70" height="20">
									<% } else { %>
										<% if(usr.hasOverrideSecurity() || usr.isChannelMarketingManager()) {%>
											<% disabled = ""; %>
											<a href="#" onClick='javascript:approveReject("cm","reset")'>Remove Platinum Status</a>
										<% } else { %>
											Please check with Global Channel Manager to remove Platinum Commitment Level
										<% } %>
									<% } %>
									</div>
								</td>
								<td width="53%"></td>
							</tr>
						</table>
   	     			</td>
   	     		</tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<input type="hidden" name="acctid" value="<%= dist.getVcn() %>">
<input type="hidden" name="page" valule="commitment">
<input type="hidden" name="AUTH" value="NO">
</form>
  </body>
</html>