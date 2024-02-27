<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*, java.text.* " %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="org.apache.xpath.patterns.StepPattern" %>

<html>
<%@ include file="./SMRCHeader.jsp" %>
<%@ include file="analytics.jsp" %>
<%
Account acct = header.getAccount();
boolean missingSE = ((Boolean) request.getAttribute("missingSE")).booleanValue();
boolean newNoteRequired = false;
boolean onResubmit = false;

String where = request.getParameter("where");
User usr = header.getUser();
String acctName = (String)request.getAttribute("acctName");

ArrayList productModules = (ArrayList)request.getAttribute("productModules");
ArrayList selectedModules = (ArrayList)request.getAttribute("selectedModules");
boolean hasCurrentChangeRequest = ((Boolean) request.getAttribute("hasCurrentChangeRequest")).booleanValue(); 

ModuleChangeRequest moduleChangeRequest = null;
Workflow workflow = null;
boolean canApprove = false;

if (hasCurrentChangeRequest){
	moduleChangeRequest = (ModuleChangeRequest) request.getAttribute("moduleChangeRequest");
	workflow = (Workflow) request.getAttribute("workflow");
	canApprove = ((Boolean) request.getAttribute("canApprove")).booleanValue();
	newNoteRequired = ((Boolean) request.getAttribute("newNoteRequired")).booleanValue();
	onResubmit = ((Boolean) request.getAttribute("onResubmit")).booleanValue();
}
 //requested in SD0000000786638
//boolean ableToUpdate = ((Boolean) request.getAttribute("ableToUpdate")).booleanValue();
boolean ableToUpdate = true;
SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");

System.out.println("CAN APPROVE = " + canApprove);
System.out.println("HAS CURRENT CHANGE REQUEST = " + hasCurrentChangeRequest);
%>
<script>

	var newNoteRequired = <%= newNoteRequired %>;
	var currentStatusNotes = '"V" = Module is loaded for this customer in Vista';
	var proposedActionNotes = '"A" = Requesting addition of this module &nbsp;&nbsp;&nbsp;&nbsp; "R" = Requesting removal of this module';

	function countChars() {
			document.forms[0].reasonCount.value = 2000 - document.forms[0].changeReason.value.length;
	}

    var setInnerHTML = function( id, str ){
		if(!document.getElementById) return; // Not Supported
		if(document.getElementById){
			document.getElementById(id).innerHTML = str;
		}
	}
	
	
    moduleAttArray = new Array();
    
	function moduleAtt (shortDesc,shortcode, vistaCode, actionCodeElement, actionCode, moduleId, url){
		this.shortDesc = shortDesc;
		this.shortcode=shortcode;
		this.vistaCode = vistaCode;
		this.actionCodeElement = actionCodeElement;
		this.actionCode = actionCode;
		this.moduleId = moduleId;
		this.url = url;
		if ((vistaCode == "V" && actionCode == "") || actionCode == "A"){
			this.actionSymbol = "-";
		} else {
			this.actionSymbol = "+";
		}
	}
	
	resubmitOptionArray = new Array();
	
	function resubmitOption (spanId, approvalId, approverName){
		this.spanId = spanId;
		this.approvalId = approvalId;
		this.approverName = approverName;
	}
	
	function writeHeaderCells(rowType) {
	    writeString = "";
	    for (i=0; i < moduleAttArray.length; i++){
			var writeValue = "";
			var classType ="moduleHeader";
			if (rowType == 'shortDesc'){
				if(moduleAttArray[i].shortcode=='(ICD)' || moduleAttArray[i].shortcode=='(PS+)' ||  moduleAttArray[i].shortcode=='(SIM)'){
						continue;
					}
				else{
				if((moduleAttArray[i].url) != "") {
					writeValue ="<a href='"+(moduleAttArray[i].url)+ "' target='_blank'><b><u>"+(moduleAttArray[i].shortDesc)+"</u></b></a>"+" "+(moduleAttArray[i].shortcode);
								} else { 
					writeValue = (moduleAttArray[i].shortDesc)+" "+(moduleAttArray[i].shortcode);
									}
				classType ="moduleHeaderBold";
				<%	if (acct.isProspect()){ %>
						writeValue += '&nbsp;&nbsp;<input type="checkbox" name="MODULES" value="';
						writeValue += moduleAttArray[i].moduleId + '"';
						if (moduleAttArray[i].vistaCode == 'V'){
							writeValue += ' checked ';
						}
					if(moduleAttArray[i].shortcode== '(PRX)'){
						writeValue += ' disabled> ' + 
						'<td class="moduleHeaderBoldShaded">Product line approval required. Contact your CRDS RMM.</td>';
						}
					else if(moduleAttArray[i].shortcode== '(MRO)' || moduleAttArray[i].shortcode== '(RBM)' || moduleAttArray[i].shortcode== '(OEM)'){
						writeValue += ' disabled> ' + 
						'<td class="moduleHeaderBoldShaded">Product line approval required. Click on module name to link to request form.</td>';
						}
					else if(moduleAttArray[i].shortcode== '(LAM)'){
						 writeValue += ' > ' + 
						'<td class="moduleHeaderBoldShaded">OEM Sales Director and product line approval will be obtained by Channel once requested.</td>';
						}
					else if(moduleAttArray[i].shortcode== '(SEN)'){
						writeValue += ' disabled> ' + 
						 '<td class="moduleHeaderBoldShaded">Module being phased out. Pricing now included in LAM module.</td>';
						}
					else if(moduleAttArray[i].shortcode== '(OIC)'){
						writeValue += ' disabled> ' + 
						'<td class="moduleHeaderBoldShaded">Module being phased out.</td>';
						}
					else
						writeValue += ' > ' + 
					'<td class="moduleHeaderBoldShaded"></td>';
				<%	} %>
			}
			} else {
				if (rowType == 'vistaCode'){
					if(moduleAttArray[i].shortcode=='(ICD)' || moduleAttArray[i].shortcode=='(PS+)' ||  moduleAttArray[i].shortcode=='(SIM)'){
							continue;
						}
					else{
				<%	if (acct.isProspect()){ %>
				<%	} else { %>
				    if (moduleAttArray[i].vistaCode == 'V'){
				    	writeValue = "<span onmouseover='showNotes(currentStatusNotes);' onmouseout='hideNotes()'>V</span>";
				    } else {
				    	writeValue = "";
				    }
				    if(moduleAttArray[i].shortcode== '(LAM)'){
				    	let lamtext = (moduleAttArray[i].shortDesc)+" "+(moduleAttArray[i].shortcode)+ " - OEM Sales Director and product line approval will be obtained by Channel once requested.";
						document.getElementById("lam").innerHTML = lamtext;
					}else if(moduleAttArray[i].shortcode== '(OIC)'){
						let oictext = (moduleAttArray[i].shortDesc)+" "+(moduleAttArray[i].shortcode)+ " - Module being phased out.";
						document.getElementById("oic").innerHTML = oictext;
				    }else if(moduleAttArray[i].shortcode== '(SEN)'){
				    	let sentext = (moduleAttArray[i].shortDesc)+" "+(moduleAttArray[i].shortcode)+ " - Module being phased out. Pricing now included in LAM module.";
						document.getElementById("sen").innerHTML = sentext;
					}else if(moduleAttArray[i].shortcode== '(PRX)'){
						let prxtext = (moduleAttArray[i].shortDesc)+" "+(moduleAttArray[i].shortcode)+ " - Product line approval required. Contact your CRDS RMM.";
						document.getElementById("prx").innerHTML = prxtext;
				    }else if(moduleAttArray[i].shortcode== '(MRO)'){
						let mrotext = (moduleAttArray[i].shortDesc)+" "+(moduleAttArray[i].shortcode)+ " - Product line approval required. Click on module name to link to request form.";
						document.getElementById("mro").innerHTML = mrotext;
					}else if(moduleAttArray[i].shortcode== '(OEM)'){
					    let oemtext = (moduleAttArray[i].shortDesc)+" "+(moduleAttArray[i].shortcode)+ " - Product line approval required. Click on module name to link to request form.";
					    document.getElementById("oem").innerHTML = oemtext;
					}else if(moduleAttArray[i].shortcode== '(RBM)'){
					    let rbmtext = (moduleAttArray[i].shortDesc)+" "+(moduleAttArray[i].shortcode)+ " - Product line approval required. Click on module name to link to request form.";
					    document.getElementById("rbm").innerHTML = rbmtext;
					}
					
				<%	}	%>
					}
				} else {
					if (rowType == 'actionCode'){
						if(moduleAttArray[i].shortcode=='(ICD)' || moduleAttArray[i].shortcode=='(PS+)' ||  moduleAttArray[i].shortcode=='(SIM)'){
								continue;
							}
						else{
						writeValue = ("<span id='actionCodeSpan_" + i + "'  onmouseover='showNotes(proposedActionNotes);' onmouseout='hideNotes()' >" + moduleAttArray[i].actionCode + "</span>");
						}
						} else {
						if (rowType == 'actionSymbol'){
							if(moduleAttArray[i].shortcode=='(ICD)' || moduleAttArray[i].shortcode=='(PS+)' ||  moduleAttArray[i].shortcode=='(SIM)'){
									continue;
								}
							else{
								if(moduleAttArray[i].shortcode== '(PRX)' || moduleAttArray[i].shortcode== '(MRO)' || moduleAttArray[i].shortcode== '(RBM)' || moduleAttArray[i].shortcode== '(OEM)'
								   || moduleAttArray[i].shortcode== '(OIC)' || moduleAttArray[i].shortcode== '(SEN)'){
									writeValue = ("<a href='javascript:changeAction(\"" + i + "\")' style='pointer-events: none'><span id='actionSymbolSpan_" + i + "'>" + moduleAttArray[i].actionSymbol + "</span></a>");
								}
								else{
								writeValue = ("<a href='javascript:changeAction(\"" + i + "\")'><span id='actionSymbolSpan_" + i + "'>" + moduleAttArray[i].actionSymbol + "</span></a>");
								}
							}
						}
					}
				}
			}
			if (rowType == 'shortDesc'){
					classType = "moduleHeaderBoldShaded";
			}
			else if(rowType == 'vistaCode' || rowType == 'actionCode' || rowType == 'actionSymbol'){
				classType = "moduleHeaderShaded";
				}
		
			align = "<%=acct.isProspect()?"align='right'":""%>";
			writeString += "<tr><td class='" + classType + "' " + align + " >";
			writeString += writeValue;
			writeString += "&nbsp;</td></tr>";
		}
	    
		return writeString;
	}
	
	function changeAction(index){
		if (moduleAttArray[index].actionSymbol == '+'){
		   if (moduleAttArray[index].vistaCode == "V"){
				moduleAttArray[index].actionCode = '';
			} else {
				moduleAttArray[index].actionCode = 'A';
			}
			moduleAttArray[index].actionSymbol = '-';
		} else {
			if (moduleAttArray[index].vistaCode == "V"){
				moduleAttArray[index].actionCode = 'R';
			} else {
				moduleAttArray[index].actionCode = '';
			}
			moduleAttArray[index].actionSymbol = '+';
		}
		setInnerHTML("actionCodeSpan_" + index,moduleAttArray[index].actionCode);
		setInnerHTML("actionSymbolSpan_" + index,moduleAttArray[index].actionSymbol);
	}
	
	
	function submitForm (submitAction){
	<% // An SE is required so TAP can refer to a geography when finding a DM or ZM.
		if (missingSE){  %>
			alert("Product loading module requests cannot be created for accounts without an assigned sales engineer. Please add a sales engineer to this account in Vista, then create this request the following day");
	<%	} else { %>
			if (newNoteRequired && document.forms[0].reasonCount.value == '2000'){
				alert("A reason for change is required before submitting a module change request to the district manager");
			} else {
				for (i=0; i < moduleAttArray.length; i++){
					moduleAttArray[i].actionCodeElement.value = moduleAttArray[i].actionCode;
				}
				document.forms[0].submitAction.value = submitAction;
				document.forms[0].submit();
			}
	<%	} %>
	}
	
	function showResubmitOptions(){
		for (i=0; i < resubmitOptionArray.length; i++){
			var linkString = "<br>Click <a href='javascript:reviseResubmit(\"" + resubmitOptionArray[i].approvalId + "\")'><u><em>here</em></u></a> to send request back to " + resubmitOptionArray[i].approverName + "<br>";
			setInnerHTML(resubmitOptionArray[i].spanId, linkString);
		}
		if (resubmitOptionArray.length > 0){
			setInnerHTML("reviseReasonSpan","<b>Explanation:</b> <br><textarea name=\"reviseReason\" wrap rows=\"3\" cols=\"20\" >");
			setInnerHTML("reviseButtonSpan","&nbsp;");
		}
	}
	
	function reviseResubmit(approvalId){
		if (document.forms[0].reviseReason.value.length == 0){
			alert("Please enter an explanation for requesting a revision.");
		} else {
			document.forms[0].resubmitApprovalId.value = approvalId;
			submitForm('reviseResubmit');
		}
	}
	
	function showNotes(displayVariable){
		setInnerHTML("helpNotes",displayVariable);
	}
	
	function hideNotes(){
		setInnerHTML("helpNotes","&nbsp;");
	}
	
	function doNothing(){
		nothingVar = 0;
	}
	
</script>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>

		<span class="crumbarrow">&gt;</span>
		<span class="crumbcurrent">Product Module Request</span></p> 
    </td>
  </tr>
</table>
<table width="875" border="0" cellspacing="0" cellpadding="0">
  <tr> 
     <td width="175"> 
     <p>&nbsp;</p>
      <table width="900" border="0" cellspacing="0" cellpadding="0">

        <tr valign="top"> 
          <td width="10%" valign="left"> 
						<%@ include file="./accountLeftNav.jsp" %>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
          </td>
          <td width="5" align="left" background="<%= sImagesURL %>divider.gif">&nbsp;</td>

         <td width="600"> 
         <% if(request.getParameter("submitAction")!=null){ %>
         <blockquote><font class="crumbcurrent">Save Successful</font></blockquote>
         <% } %>         
            <p class="heading2">
            Product Module Request</p>
           <p class="heading3">Account: <%= acct.getCustomerName() %></p>            
            <span class="heading3">Distributor Agreement Product Module Description</span><br>
            <br><br>
            <% if (!acct.isProspect()){ %>
          	Click <a href="<%= Globals.getProductModuleInstructionsLink() %>" target="blank.html" ><b>here </b></a>to view instructions on how to request product module changes.
          	<%} %>
          <form method="POST" action="DistributorProductLoadingModule">
	 		   <input type="hidden" name="page" value="product">
	 		   <input type="hidden" name="submitAction" value="">
	 		   <input type="hidden" name="resubmitApprovalId" value="">
	 		   
		 		   <input type="hidden" name="acctId" value="<%= request.getParameter("acctId") %>">
		 		   <input type="hidden" name="where" value="<%=where %>">
		 		   
		 	<%  
		 	if (ableToUpdate || hasCurrentChangeRequest){  %>
			   <table width="100%" cellspacing="0" cellpadding="5" <%= (acct.isProspect())?" border='0' ":" border='2' rules='cols' frame='void'" %> >
					<tr valign="top">
						<td width="25%">
						<% if (!acct.isProspect()){ %>
							<table>
							<%	if (ableToUpdate){ %>
								
								<tr>
									<td><span class="moduleHeaderBold">Provide a brief (<em>2000 Characters max</em>) business case of why a module change is being requested:</span><td>
								</tr>
								<tr>
									<td><textarea name="changeReason" wrap rows="4" cols="40" onkeyup="countChars()" <%= (ableToUpdate)?"":" disabled " %>   ></textarea></td>
								</tr>
								<tr>
									<td>Remaining: <input type="text" name="reasonCount" disabled value="2000" maxlength="4" size="4" ></td>
								</tr>
								<% }
						    	if (hasCurrentChangeRequest){ 
						    	 %>
						    	
								<tr><td>&nbsp;</td></tr>
								<tr><td class="moduleHeaderBold">Previous notes:</td></tr>
								<tr>
									<td>
									<%	if (moduleChangeRequest.getModuleChangeReasonNotes() != null && moduleChangeRequest.getModuleChangeReasonNotes().length > 0){		
									  		
											for (int i=0; i < moduleChangeRequest.getModuleChangeReasonNotes().length; i++){
												ModuleChangeReasonNotes note = moduleChangeRequest.getModuleChangeReasonNotes()[i];
									%>
											<u><%= note.getUserAddedName() %> (<%= sdf.format(note.getDateAdded()) %>)</u>:<em> <%= note.getReasonNotes() %></em><br>
									<% 		}
										}
									%>
									</td>
								</tr>
							<%	} %>
							
							</table>
					    <% } else { %>
					   
						    &nbsp;
						<% } 	%>
							
						</td>
						<td width="25%" align="left">
							<table>
								<tr>
									<td>
								<% if(ableToUpdate){ %>
									<a href="javascript:submitForm('save')"><img src="<%= sImagesURL %>button_save.gif" width="70" height="20"  border="0" ></a>
							 
					    		<% } else { %>
					        		&nbsp;
					    		<% }	%> 
					    			</td>
					    		</tr>
					    		<tr><td>&nbsp;</td></tr>
					    		<%	if (hasCurrentChangeRequest) {	
					    			%>
							    		<tr><td class="moduleHeaderBold">Approvals:</td></tr>
							    		<%	WorkflowStep[] steps = workflow.orderWorkflowSteps();	
							    			for (int w=0; w < steps.length; w++){
							    				WorkflowStep step = steps[w];
							    		%>
							    				<tr><td nowrap="nowrap"><u><%= step.getStepName() %>:</u></td></tr>
							    				<tr><td>
							    				<% if (step.getUserChanged() != null && !step.getUserChanged().trim().equals("")){ %> 
							    					<em><%= step.getStatusDescription() %> by <%= step.getUserApprovedOrRejected().getFullName()  %> at <%= sdf.format(step.getDateChanged()) %></em>
							    				<%	  if (!step.getStatusFlag().trim().equalsIgnoreCase("V")){ %>
							    					<br><span id="resubLink_<%= w %>" class="crumbcurrent" >&nbsp;</span>
							    					<script>
							    						resubmitOptionArray[resubmitOptionArray.length] = new resubmitOption("resubLink_<%= w %>", '<%= step.getApprovalId() %>', '<%= step.getUserApprovedOrRejected().getFullName()  %>');
							    					</script>
							    				<%	   }		
							    				  	} %><br>
							    				
							    				</td></tr>
										<%	}	%>
										<tr><td><span id="reviseReasonSpan">&nbsp</span></td></tr>
										<% if (canApprove) { 
											String approveButton = "button_approve.gif";
											if (onResubmit){
												approveButton = "button_resubmit.gif";
											}
										%>
										<tr><td><a href="javascript:submitForm('approve')"><img src="<%= sImagesURL %><%= approveButton %>" width="70" height="20"  border="0" ></a>&nbsp;
												<a href="javascript:submitForm('reject')"><img src="<%= sImagesURL %>button_reject.gif" width="70" height="20"  border="0" ></a>&nbsp;
												<span id="reviseButtonSpan"><a href="javascript:showResubmitOptions()"><img src="<%= sImagesURL %>button_revise_resubmit.gif" width="70" height="20"  border="0" ></a></span>
												<script>
												// If there are no options to request a revision from, do not show button
													if (resubmitOptionArray.length < 1){
														setInnerHTML("reviseButtonSpan","&nbsp;");
													}
												</script>
											</td>
										</tr>	
										<%	} 		
										if (workflow.isComplete()){ %>
											<tr><td class=textredbold>This request is approved and pending an update in Vista.</td></tr>
								<%		}
									}	%>
								</table>
						</td>
					</tr>
				</table>
				<% } // end of if (ableToUpdate || hasCurrentChangeRequest) %>
				<hr width="118%">
				<% if (!acct.isProspect()){ %>
				<br><br><b>Notes: <br><b id="lam"></b><br><b id="sen"></b><br><b id="oic"></b><br><b id="prx"></b><br><b id="mro"></b><br><b id="oem"></b><br><b id="rbm"></b></br>
				 <% } %>
				 
			<table width="120%" border="0" cellspacing="1" cellpadding="1" >
     		     <tr valign="top">
     		     	  <%
		       		   ArrayList loadModuleHeaders=((ProductModule)productModules.get(0)).getLoadModules();
		       		   %>
		       		   
		       		   <input type="hidden" name="productCount" value="<%= loadModuleHeaders.size() %>" >
		       		   <%
		       		  
		       		   for(int j=0;j<loadModuleHeaders.size();j++){
								LoadModule loadModule = (LoadModule)loadModuleHeaders.get(j);
					 			boolean inVista = false;
					 			String requestedAction = "";
					 			for(int i=0;i<selectedModules.size();i++){
									LoadModule selectedModule = (LoadModule)selectedModules.get(i);
									if(selectedModule.equals(loadModule)){
					 					inVista = true;
					 					break;
					 				}
								}
								if (hasCurrentChangeRequest && moduleChangeRequest.getModuleProducts() != null){
									ModuleChangeProduct[] requestProds = moduleChangeRequest.getModuleProducts();
									for (int rp=0; rp < requestProds.length; rp++){
										if (requestProds[rp].getModuleId() == loadModule.getModuleId()){
											requestedAction = requestProds[rp].getAction();
										}
									}
								}
							
					 		%>
						 		<input type="hidden" name="actionCode_modId_<%= loadModule.getModuleId() %>" value="<%= requestedAction %>" >
						 		<%String input=loadModule.getModuleName();%>
						 		<%String code= "("+loadModule.getModuleShortCode()+")"; %>
						 		<script>
						 			moduleAttArray[moduleAttArray.length] = new moduleAtt('<%=input%>','<%=code%>', '<%= (inVista)?"V":"" %>', document.forms[0].actionCode_modId_<%= loadModule.getModuleId() %>, '<%= requestedAction %>','<%= loadModule.getModuleId() %>','<%=loadModule.getUrl()%>');
						 		</script>
						<% } %>
				</tr>
				<%--    start header/action section of table  --%>
				<tr>
					<td class="crumbcurrent" colspan="<%= loadModuleHeaders.size() %>"><span id="helpNotes">&nbsp;</span></td>
				</tr>
				<tr>
					<td>
						
						<% if (!acct.isProspect()&&(ableToUpdate)){%>
						<table>
						<col  align="left" width="775" />
						<%}	else { %>
						<table width=850>
						<col align="left" width=350 />
						<% } %> 
							<tr>
							<th align="left" class="moduleHeaderBold">Product Modules</th>
							<th align="left" class="moduleHeaderBold" id="notes">Notes</th>
							</tr>
							<tr>
								<td> 
									<script>
										document.write(writeHeaderCells('shortDesc'));
									</script>
								</td>
							</tr>
						</table>
					</td>
					
					<td>
					<% if (!acct.isProspect()){ %>
						<table>
						<script type="text/javascript">document.getElementById("notes").style.display = "none";</script>
						<col width="400" />
							<th class="moduleHeaderBold"><%= (acct.isProspect())?"":"Current Vista Status:<a href='javascript:doNothing()' onmouseover='showNotes(currentStatusNotes);' onmouseout='hideNotes()'><u>?</u></a>"%></th>
							<td>
							<script>
								document.write(writeHeaderCells('vistaCode'));
							</script>
							</td>
							<% } %>
						</table>
					</td>
					
					<td>
					<% if (!acct.isProspect()){%>
					<%  if (ableToUpdate){	%>
						<table>
						<col width="300" />
						<th class="moduleHeaderBold" >Proposed Action:<a href="javascript:doNothing()" onmouseover="showNotes(proposedActionNotes);" onmouseout="hideNotes()" ><u>?</u></a></th>
						    <td>
								<script>
									document.write(writeHeaderCells('actionCode'));
								</script>
							</td>
						</table>
						</td>
							<td>
							<table>
							<col width="200" />
								<th class="smallFontI">(+)Add or(-)Remove</th>
								<td>
										<script>
											document.write(writeHeaderCells('actionSymbol'));
										</script>
										</td>
									</table>
								</td>
							<%	} %>
						<%	} %>
						
					<tr><td colspan="<%= loadModuleHeaders.size() + 1 %>" ><hr width="100%"></td></tr>
				
				<%--    end header/action section of table --%>
				
				<%--    start module section of table   --%>
					</td>		          		   
					
       	  </table>
       	  </form>
       	  <br>
       
			<!-- 
            <p>	C = Construction Distributor, IC = Industrial/Construction Distributor (full line), R=Residential Distributor.<br>
            	C & IC Modules provide access to the products which are in the Eaton Electrical Catalog with
            	the<br>
            	exception of Switchgear products.  Where an A is listed the product will be available,
            	but at an access<br>

            	price.<br>  
           	 <u>C only and IC only distributors cannot sell Switchgear Products without Distributor
            	Department approval</u>
             <br>
                RBM = Replacement Breaker Module.  Product by Part Number.
            </p>-->
	      <p>&nbsp;</p>   
 
         </td>
        </tr>
      </table>
    
    </td>

  </tr>
</table>
<p>&nbsp;</p>
  </body>
</html>
