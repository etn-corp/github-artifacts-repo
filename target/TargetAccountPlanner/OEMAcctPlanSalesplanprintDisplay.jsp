<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.*"%>
<%@ include file="analytics.jsp" %>
<%
String userid = (String)request.getAttribute("userid");
OEMAcctPlanBean OEMAPVars = (OEMAcctPlanBean)request.getAttribute("variables");
request.setAttribute("header", OEMAPVars.getHeader());
ArrayList tasks = OEMAPVars.getTasks();
%>

<HTML>

<style>
	<!-- Changes the fonts in the html controls to match the font on the rest of the page -->
	textarea, input, select {
		color: #000000;   
		font-family: Arial, Helvetica, sans-serif, monospace;   
		font-size: 12px;   
		font-weight: normal;   
		font-style: normal;   
		text-decoration: none; 
	}
	
</style>



<%@ include file="./TAPheader.jsp" %>
<%
SalesPlan salesPlan = OEMAPVars.getSalesPlan();


Customer cust = OEMAPVars.getCust();
String backSort = OEMAPVars.getBackSort();
String backParm = OEMAPVars.getBackParm();
String salesmanName = OEMAPVars.getSalesmanName();
//boolean ableToSee = OEMAPVars.getAbleToSee();
//boolean ableToUpdate = OEMAPVars.getAbleToUpdate();

User user = OEMAPVars.getHeader().getUser();
TreeMap usersTm = (TreeMap)request.getAttribute("usersTm");
boolean ableToUpdate = false;
boolean ableToSee = false;

Account acct = OEMAPVars.getHeader().getAccount();

if (user.ableToUpdate(acct) || (user.equals(acct.getUserIdAdded()) && acct.isProspect())) {
	ableToUpdate = true;
	ableToSee = true;
}else if(user.ableToSee(acct) || (user.equals(acct.getUserIdAdded()) && acct.isProspect())) {
	ableToSee = true;
}

String genMsg = OEMAPVars.getGenMsg();
ArrayList stages = OEMAPVars.getSalesPlanStages();
ArrayList objectives = OEMAPVars.getObjectives();
String lastUpdateDate = OEMAPVars.getLastUpdateDate();

%>

 <script language='javascript'>
 window.name = 'salesplan';
 attachmentArray = new Array(); 
 tempFilesDeleted = new Array();

  
 var setInnerHTML = function( id, str ){
	    if(!document.getElementById) return; // Not Supported
		if(document.getElementById){
			document.getElementById(id).innerHTML = str;
		}
 }
	
 function showAttachedFiles(){
 		var attachString = "";
 		for (i=0; i < tempFilesDeleted.length; i++){
 			attachString += " <input type=hidden name=deleteTempFile value='" + tempFilesDeleted[i] + "' >";
 		}
 <% 
 	for (int taskCount=0; taskCount < (tasks.size()+2); taskCount++){
 %>
 		 
 		for (i=0; i < attachmentArray.length; i++){
 			if (attachmentArray[i].taskRow == <%= taskCount %>){
 			    attachString += "&nbsp;&nbsp;&#149;&nbsp;&nbsp;";
 			    // attachment already saved on task
 			    if (attachmentArray[i].fileId > 0){
 			    	attachString += "<a href=\"javascript:newWindow(\'TaskAttachmentDisplay?docId=" + attachmentArray[i].fileId + "',\'AttachmentDisplay\')\">";
					attachString += "<span >" + attachmentArray[i].fileName + "</span>";
					attachString += "</a>";
 			    } else {
 			    	attachString += attachmentArray[i].fileName;
 			    	attachString += "<input type=hidden name=attachDoc" + attachmentArray[i].taskRow + " value=" + attachmentArray[i].hiddenName + " > ";
 			    	attachString += "<input type=hidden name=attachDoc" + attachmentArray[i].taskRow + "_Name value=" + attachmentArray[i].fileName + " > ";
 			    }
 			    if (attachmentArray[i].deleted == 'Y'){
 			    	attachString += "&nbsp;&nbsp;&nbsp;&nbsp;<span class=\"textgray\"> &lt;deletion pending&gt; </span> &nbsp;&nbsp;<strong>|</strong>&nbsp;&nbsp;<a href=\"javascript:updateDeletedValue(" + i + ",'N')\"><span class=\"textgray\" ><u>Restore</u></span></a> <br>" ;
 			    	attachString += " <input type=hidden name=deletedDocs value=" + attachmentArray[i].fileId + " > ";
 			    } else {
 			    	if (attachmentArray[i].fileId == 0){
 			    		attachString += "&nbsp;&nbsp;&nbsp;&nbsp;<span class=\"textgray\">&lt; unsaved &gt;</span> &nbsp;&nbsp;&nbsp;&nbsp;<strong>|</strong>&nbsp;&nbsp;<a href=\"javascript:removeFromList(";
						attachString += i;	
						attachString += ")\"><span class=\"textgray\" ><u>Remove</u></span></a><br>" ;
 			    	} else {
 			    		attachString += "&nbsp;&nbsp;&nbsp;&nbsp;<strong>|</strong>&nbsp;&nbsp;<a href=\"javascript:updateDeletedValue(" + i + ",'Y')\"><span class=\"textgray\" ><u>Remove</u></span></a> <br>" ;
 			    	}
 			    }
 			}
 		}
 		
 		 		
 	//	attachString += "</font></span>";
 	//	attachString += "<br>";
 		setInnerHTML("attachmentDisplay<%= taskCount %>", attachString); 
 		// Reset attachString before looping
 		attachString = ""; 
 <%	}
 %>
 		
 }
 
 
 function updateDeletedValue(attachIndex, value){
 	attachmentArray[attachIndex].deleted = value;
 	showAttachedFiles();
 }

 function removeFromList(arrayIndex){
 	for (i=arrayIndex; i<(attachmentArray.length - 1); i++){
 		attachmentArray[i] = attachmentArray[i+1];
 	}
 	attachmentArray.length = (attachmentArray.length - 1);
 	showAttachedFiles();
 }
 
 function taskAttachment(taskRow,attachmentRow,fileId,fileName,hiddenName){
 	this.deleted = 'N';
 	this.taskRow = taskRow;
 	this.attachmentRow = attachmentRow;
 	// fileId is sales_plan_pap_files.sales_plan_pap_files.id and indicates it has been saved
 	this.fileId = fileId;
 	this.fileName = fileName;
 	// If \ or / is found, then pathName was given
 	if (fileName.lastIndexOf("\\") != -1){
		this.fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length);
	} else {
		if (fileName.lastIndexOf("/") != -1){
			this.fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length);
		}
	}
	this.hiddenName = hiddenName;
 	
 
 } 
 
 function getNumberAvailable(taskRow){
 	currentlyAttached = 0;
 	for (i=0; i < attachmentArray.length; i++){
 		if (attachmentArray[i].taskRow == taskRow){
 			currentlyAttached++;
 		}
 	}
 	available = <%= Globals.getTaskAttachmentLimit() %> - currentlyAttached;
 	return available;
 
 }
 
 function addAttachment(taskRow,attachmentRow,fileName,hiddenName){
 	attachmentArray[attachmentArray.length] = new taskAttachment(taskRow,attachmentRow,0,fileName,hiddenName);
 
 }
 
 function addToTempFilesDeleted(filename) {
 	tempFilesDeleted[tempFilesDeleted.length] = filename;
 }
 
  
 </script>
 <SCRIPT LANGUAGE='JavaScript' SRC='<%= jsURL %>calendar.js'></SCRIPT>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>

		<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Sales Plan - Update</span></p> 
    </td>
  </tr>
</table> 
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750" valign="top"> 
    	<p>&nbsp;</p>
    	<%
			if(genMsg!=null) {
			%>
			<blockquote><font class="crumbcurrent"><%= genMsg %></font></blockquote>
			<%
			}
			%>
		<p class=heading2><%= cust.getName() %></p>
		<br>
		<a href="OEMAcctPlan?page=salesplan&cust=<%= cust.getVistaCustNum() %>">Click here to view plan.</a>
		<br>
		<form action=OEMAcctPlan method=post name=SALESPLAN>
		
		<%
		if (request.getParameter("geog") != null) {
			%>
			<input type=hidden name=geog value="<%= request.getParameter("geog") %>">
			<%
		}
		else if (request.getParameter("industry") != null) {
			%>
			<input type=hidden name=industry value="<%= request.getParameter("industry") %>">
			<%
		}
		%>

		<input type=hidden name=page value="salesplanprint">
		<input type=hidden name=saveChanges value="true">
		<input type=hidden name=cust value="<%= cust.getVistaCustNum() %>">
		
		<%
		if (request.getParameter("backSort") != null) {
		%>
			<input type=hidden name=backSort value="<%= request.getParameter("backSort") %>">
		<%
		}
		%>

		<table width="80%" align=center>

			<tr>
				<td class=smallFont>Salesperson: <%= salesmanName %></td>
				<td rowspan=2><div class="heading3">Stage of the Account
		
				<%
			if (ableToUpdate) {
					%>
						<select name=spstage class=smallFont>
							<option value="0" class=smallFont></option>
					<%
					for (int i=0; i < stages.size(); i++) {
							SPStage stage = (SPStage)stages.get(i);
							String sel = "";
							if (stage.getId() == cust.getStage()) {
								sel = "selected";
							}
							%>
							<option value="<%= stage.getId() %>" <%= sel %> class=smallFont><%= stage.getDescription() %></option>
							<%
					}
				%>
				</select>
				<%
			}else {
				SPStage stage = OEMAPVars.getCustomerStage();
				%>
				<%= stage.getDescription() %>
				<%
			}
		%>
				</div></td>
			</tr>
		</table>

		<table width="100%" cellspacing=1 cellpadding=1 class=tableBorder>
			<tr class=cellColor>
				<td valign=top width="50%"><div class="heading3">Objectives and Strategies</div>
					<font color=red>Must be measurable, attainable, and have a time focus</font>
					<table width="100%" cellspacing=0 cellpadding=0>
							<tr>
								<td colspan=2 class=smallFont>Strategic Focus, Desired Position, Short/Long Term Objectives</td>
							</tr>
			<%
			if (ableToUpdate) {
				%>
								<tr>
									<td width="45%"><textarea name="objective_response" rows=5 cols=60 wrap class=smallFont><%= salesPlan.getObjectiveResponse() %></textarea></td>
								</tr>
				<%
			}
			else {
				%>
				<tr><td class=smallFont><%= salesPlan.getObjectiveResponse() %><input type="hidden" name="objective_response" value="<%= salesPlan.getObjectiveResponse() %>"></td></tr>
				
			<%
			}
			%>
					</table>
				</td>
				<td class=smallFont><table><tr><td><div class="heading3">Competitive Products and Position:</div>
					What are the competitors commercial and
					technical positions? Strengths and weaknesses? Their expected defense strategy and
					tactics?
				</td>
			</tr>
			<tr>
				<td class=smallFont colspan=2>
					<table width="100%" cellspacing=0 cellpadding=0>
						<tr>
		<%
		if (ableToUpdate) {
			%>
					<td width="45%"><textarea rows=5 cols=60 wrap name=compet class=smallFont><%= salesPlan.getCompetitiveProdsAndPositions() %></textarea></td>
			<%
		}
		else {
			%>
					<td class=smallFont><%= salesPlan.getCompetitiveProdsAndPositions() %><input type="hidden" name="compet" value="<%= salesPlan.getCompetitiveProdsAndPositions() %>"></td>
			<%
		}
		%>
						</tr>
					</table>
				</tr></table></td>
			</tr>
		</table>
</table>		
		<%	
		
		ArrayList products = OEMAPVars.getProducts();
		ArrayList ebeCategories = OEMAPVars.getEbeCategories();		
		%>
		
		<input type=hidden name=totTask value="<%= (tasks.size() + 2) %>">

<%-- #################################################################### --%>
<%-- #####################   Start of Task table ######################## --%>
<%-- #################################################################### --%>
		<br>
		<p class="heading2">Task Assignments</p>
	
<%
		for (int i=0; i < tasks.size(); i++) {

			PurchaseActionProgram task = (PurchaseActionProgram)tasks.get(i);
			ArrayList taskAttachments = task.getAttachments();
%>
			   	<script>
<%
				   for (int attachCount=0; attachCount < taskAttachments.size(); attachCount++){
				   		TaskAttachment thisAttachment = (TaskAttachment) taskAttachments.get(attachCount);
%>
						attachmentArray[attachmentArray.length] = new taskAttachment(<%= i %>,<%= attachCount %>,<%= thisAttachment.getId() %>,'<%= thisAttachment.getFileName() %>');
<% 
					} // end for
%>
				   </script>
		<table width="100%" class=tableBorder cellspacing=1 cellpadding=1> 
			<tr bgcolor="#F8F8FF">
				<td width="27%"><strong>Action</strong></td>
				<td width="19%"><strong>Notify</strong></td>
				<td width="20%"><strong>Product</strong></td>
				<td width="7%"><strong>Schedule</strong></td>
				<td width="27%"><strong>Results</strong></td>
			</tr>
			<tr class=cellColor>
				<td class=smallFontL>
					<input type=hidden name=taskId<%= i %> value="<%= task.getTaskId() %>">
					
					<textarea class=smallFontL rows=5 cols=35 name="action<%= i %>"><%= task.getAction() %></textarea>
					<input type=hidden name=prevAct<%= i %> value="<%= task.getAction() %>">
				</td>
				<td class=smallFontL valign="top">
					<table width="100%">
						<tr>
							<td class=smallFontL>Assign To:</td>
						</tr>
						<tr>
							<td class=smallFontL>
								<select class=smallFontL name=assignedto<%= i %>>
									<option class=smallFontL value=""></option>
<%
		                Iterator it = usersTm.entrySet().iterator();
						while(it.hasNext()) {
						    String selected = "";
		                    Map.Entry e = (Map.Entry)it.next();
		                    if (e != null) {
		                    String key = (String)e.getKey();
		                    	if (key != null && key.length() > 0) {
		                    	    String value = (String)e.getValue();
		            				if (value.equals(task.getAssignedTo())) {
		            					selected = " selected";
		            				}
%>
	   								<option class=smallFontL value="<%= value %>" <%= selected %>><%= key %></option>
<%
	
		                    	} // end if key not null
		                    } // end if entry not null
		                } // end while
%>
	
								</select>
							</td>
						</tr>
						<tr>
							<td class=smallFontL>Carbon Copy:</td>
						</tr>
						<tr>
							<td class=smallFontL>
								<select multiple size="3" class=smallFontL name=ccemail<%= i %>>
									<option class=smallFontL value="0"></option>
<%
								StringBuffer prevCCEmailBuffer = new StringBuffer();
								ArrayList ccEmails = ccEmails = task.getCCEmail();
								it = usersTm.entrySet().iterator();
								while(it.hasNext()) {
								    String selected = "";
								    Map.Entry e = (Map.Entry)it.next();
				                    if (e != null) {
				                    String key = (String)e.getKey();
				                    	if (key != null && key.length() > 0) {
				                    	    String value = (String)e.getValue();
				        					for(int k=0;k<ccEmails.size();k++){
				        						String cc=(String)ccEmails.get(k);
				        						if(cc.equals(value)){
				        							selected = " selected";
				        							prevCCEmailBuffer.append("<input type=hidden name=prevCCEmail" + i + " value=\"" + cc + "\">");
				        							break;
				        						}
				        					}
%>
									<option class=smallFontL value="<%= value %>" <%= selected %>><%= key %></option>
<%	
				                    	} // end if key not null
				                    } // end if entry not null
					           } //end while
%>
								</select>
							<%= prevCCEmailBuffer.toString() %>
								
								<input type=hidden name=prevAssign<%= i %> value="<%= task.getAssignedTo() %>">
								
								<!--<input type=hidden name=prevCCEmail<%= i %> value="<%= task.getCCEmail() %>">-->
							</td>
						</tr>
					</table>
				</td>
				<td class=smallFontL valign="top">
					<table width="100%">
						<tr>
							<td class=smallFontL>Product Line:</td>
						</tr>
						<tr>
							<td class=smallFontL>
								<select class=smallFontL name=product<%= i %>>
									<option class=smallFontL value=""></option>
	
<%
								for (int j=0; j < products.size(); j++) {
									String selected = "";
									Product prd = (Product)products.get(j);
					
									if (prd.getId().equals(task.getProduct())) {
										selected = " selected";
									}
%>
										<option class=smallFontL value="<%= prd.getId() %>" <%= selected %>><%= prd.getDescription() %></option>
									<%
								}
								%>
									</select>
								</td>
							</tr>
							<tr>
								<td class=smallFontL>EBE Category:</td>
							</tr>
							<tr>
								<td class=smallFontL>
									<select class=smallFontL name=ebeCat<%=+ i %>>
										<option class=smallFontL value=""></option>
<%
								for (int j=0; j < ebeCategories.size(); j++) {
									String selected = "";
									EBECategory cat = (EBECategory)ebeCategories.get(j);
					
									if (cat.getId() == task.getEBECategory()) {
										selected = " selected";
									}
%>
										<option class=smallFontL value="<%= cat.getId()%>" <%= selected %>><%= cat.getDescription() %></option>
<%
								}
%>
									</select>
									<input type=hidden name=prodOld<%= i %> value="<%= task.getProduct() %>">
									<input type=hidden name=ebeCatOld<%= i %> value="<%= task.getEBECategory() %>">
								</td>
							</tr>
						</table>
					</td>
					<td class=smallFontC valign="top" align="left">
						<table width="100%">
						<tr>
						<td>					
<%
					if (task.getSchedule() != null) {
%>		
						
						Date:
						<A HREF='JavaScript:displayCalendar(document.SALESPLAN,"sched<%= i %>");'>
						<IMG SRC='<%= sImagesURL %>cal.gif' WIDTH='16' HEIGHT='16' BORDER='0' ALT='Calendar' align="absmiddle"></A>
						</td>
						</tr>
						<tr>
						<td>
						<input size=10 name=sched<%= i %> onFocus='this.blur();' value="<%= task.getScheduleAsString() %>" class="smallFont">
						<input type=hidden name=prevSched<%= i %> value="<%= task.getScheduleAsString() %>">
						</td>
						</tr>
<%
					} else {
%>
						Date:
						<A HREF='JavaScript:displayCalendar(document.SALESPLAN,"sched<%= i %>");'>
						<IMG SRC='<%= sImagesURL %>cal.gif' WIDTH='16' HEIGHT='16' BORDER='0' ALT='Calendar' align="absmiddle"></A>
						
						<tr>
						<td>
						<input size=10 name=sched<%= i %> onFocus='this.blur();' class=smallFont>
						</td>
						</tr>

<%
					}
	
						String checked = "";
						String yN = "N";
	
						if (task.isComplete()) {
							checked = " checked";
							yN = "Y";
						}			
	
						if(userid.equals(task.getUserAdded())){
%>
						</td>
						</tr>
						<tr>
						<td>
						<div class=smallFontC>
							Done?
							<input type=checkbox class=smallFontC name=complete<%= i %> <%= checked %>>
							<input type=hidden name=prevComp<%= i %> value="<%= yN %>">
						</div>
<%
						} else {
%>
						</td>
						</tr>
						<tr>
						<td>
						<div class=smallFontC>
							Done? 
							<input type=checkbox disabled class=smallFontC name=complete<%= i %> <%= checked %>>
							<input type=hidden name=prevComp<%= i %> value="<%= yN %>">
						</div>
						<input type=hidden name=prevComp<%= i %> value='<%= yN %>'>
<%
							if(yN.equals("Y")){
%>
						<input type=hidden name=complete<%= i %> value='<%= yN %>'>
<%
							}
				
						}
%>
				
						</td>
						</tr>
						</table>
					</td>
					<td class=smallFontL>
						<textarea class=smallFontL rows=5 cols=35 name=results<%= i %>><%= task.getResults() %></textarea>
						<input type=hidden name=prevRes<%= i %> value="<%= task.getResults() %>">
					</td>
				</tr>

				<tr class=cellColor>
					<td colspan=5>
					<table width="100%" border="0" cellpadding="3" cellspacing="0">
						<tr bgcolor="#ffffff">
							<td class=smallFontL width="40%">
								<p><span class="heading3">Manage Attachments</span></p>
								<u><a href="javascript:newWindow('TaskAttachments?taskRow=<%= i %>', 'TA')"><IMG SRC='<%= sImagesURL %>paperClip.gif' WIDTH='14' HEIGHT='14' BORDER='0' ALT='Attach' align="absmiddle"><strong>Attach documents</strong></a></u>
								<br>
								&nbsp;&nbsp;<font size=1 color="gray">(Limit <%= Globals.getTaskAttachmentLimit() %> files per Task, <%= Globals.getTaskAttachmentFileSizeMax() %> MB per File)</font><br><br>
								<div id=attachmentDisplay<%= i %>>&nbsp;</div>
							</td>
							<td width="30%">
								<p style="bgcolor:#CCCCCC"><span class="textredbold">Note</span>: Any modifications done in the attachments section
								are not saved to the task until "Save" is clicked at the bottom of the page.</p>
							</td>
							<td width="30%">&nbsp;</td>
						</tr>
					</table>	
					</td>					
				</tr>
			</table><br><br>
<%
		} // end for that paints the tasks
%>

<%-- ######################################################################################### --%>
<%--          End of the tasks with information.  the below loop adds 2 blank tasks            --%>
<%-- ######################################################################################### --%>
		
<%	
		for (int i=tasks.size(); i < (tasks.size()+2); i++) {
%>
		
		<table width="100%" class="tableBorder" cellspacing="1" cellpadding="1">
			<tr bgcolor="#F8F8FF">
				<td width="27%"><strong>Action</strong></td>
				<td width="19%"><strong>Notify</strong></td>
				<td width="20%"><strong>Product</strong></td>
				<td width="7%"><strong>Schedule</strong></td>
				<td width="27%"><strong>Results</strong></td>
			</tr>
			<tr class="cellColor">
				<td class="smallFontL">
					<input type="hidden" name="taskId<%= i %>" value="new">
					<textarea name="action<%= i %>" rows=5 cols=35 class="smallFontL"></textarea>
				</td>
				<td class="smallFontL">
					<table width="100%">
						<tr>
							<td class=smallFontL>Assign To:</td>
						</tr>
						<tr>
							<td class=smallFontL>
								<select class=smallFontL name=assignedto<%= i %>>
									<option class=smallFontL value=""></option>
<%
							    Iterator it = usersTm.entrySet().iterator();
								while(it.hasNext()) {
							        Map.Entry e = (Map.Entry)it.next();
							        if (e != null) {
							        String key = (String)e.getKey();
							        	if (key != null && key.length() > 0) {
							        	    String value = (String)e.getValue();
%>
									<option class=smallFontL value="<%= value %>"><%= key %></option>
<%
					                	} // end if key not null
					                } // end if entry not null
					            }// end while
%>
								</select>
							</td>
						</tr>
						<tr>
							<td class=smallFontL>Carbon Copy:</td>
						</tr>
						<tr>
							<td class=smallFontL>
								<select multiple size="3" class=smallFontL name=ccemail<%= i %>>
									<option class=smallFontL value='0'></option>
<%
				                it = usersTm.entrySet().iterator();
								while(it.hasNext()) {
				                    Map.Entry e = (Map.Entry)it.next();
				                    if (e != null) {
				                    String key = (String)e.getKey();
				                    	if (key != null && key.length() > 0) {
				                    	    String value = (String)e.getValue();
%>
		    						<option class=smallFontL value="<%= value %>"><%= key %></option>
<%
				                    	} // end if key not null
				                    } // end if entry not null
				                } //end while
%>
								</select>
							</td>
						</tr>
					</table>
				</td>
				<td class=smallFontL valign="top">
					<table width="100%">
						<tr>
							<td class=smallFontL>Product Line:</td>
						</tr>
						<tr>
							<td class=smallFontL>
								<select class=smallFontL name=product<%= i %>>
									<option class=smallFontL value=""></option>
<%
								for (int j=0; j < products.size(); j++) {
									Product prd = (Product)products.get(j);
%>
									<option class=smallFontL value="<%= prd.getId() %>"><%= prd.getDescription() %></option>
<%
								}
%>
								</select>
							</td>
						</tr>
						<tr>
							<td class=smallFontL>EBE Category:</td>
						</tr>
						<tr>
							<td class=smallFontL>
								<select class=smallFontL name=ebeCat<%= i %>>
									<option class=smallFontL value=""></option>
<%
								for (int j=0; j < ebeCategories.size(); j++) {
									EBECategory cat = (EBECategory)ebeCategories.get(j);
%>
									<option class=smallFontL value="<%= cat.getId() %>"><%= cat.getDescription() %></option>
<%
								}
%>
								</select>
							</td>
						</tr>
					</table>
				</td>
				<td class="smallFontC" valign="top">
					<table width="100%">								
						<tr>						
							<td>Date:
								<A HREF='JavaScript:displayCalendar(document.SALESPLAN,"sched<%= i %>");'>
								<IMG SRC='<%= sImagesURL %>cal.gif' WIDTH='16' HEIGHT='16' BORDER='0' ALT='Calendar'></A>
							</td>
						</tr>
						<tr>
							<td>
								<input size=10 name=sched<%= i %> onFocus='this.blur();' class=smallFont>
							</td>
						</tr>
						<tr>
							<td>
								<div class=smallFontC>Done? 
								<input type=checkbox name=complete<%= i %> class=smallFontC>
							</td>
						</tr>
					</table>
				</td>
				<td class=smallFontL><textarea name=results<%= i %> rows=5 cols=35 class=smallFontL></textarea></td>
			</tr>
			<tr class=cellColor valign="top">
				<td colspan=5>
				<table width="100%" border="0" cellpadding="3" cellspacing="0">
					<tr bgcolor="#ffffff">
						<td class=smallFontL width="40%">
							<p><span class="heading3">Manage Attachments</span></p>
							<u><a href="javascript:newWindow('TaskAttachments?taskRow=<%= i %>', 'TA')">
								<IMG SRC='<%= sImagesURL %>paperClip.gif' WIDTH='14' HEIGHT='14' BORDER='0' ALT='Attach' align="absmiddle"><strong>Attach documents</strong>
							</a></u><br>
							&nbsp;&nbsp;<font size=1 color="gray">(Limit <%= Globals.getTaskAttachmentLimit() %> files per Task, <%= Globals.getTaskAttachmentFileSizeMax() %> MB per File)</font><br><br>
							<div id=attachmentDisplay<%= i %>>&nbsp;</div>
						</td>
						<td width="30%">
							<p style="bgcolor:#CCCCCC"><span class="textredbold">Note</span>: Any modifications done in the attachments section
							are not saved to the task until "Save" is clicked at the bottom of the page.</p>
						</td>
						<td width="30%">&nbsp;</td>						
					</tr>
				</table>
				</td>
			</tr>
		</table><br><br>
<%
		}
%>
		<!-- run showAttachedFiles here so all hidden fields have been created -->
 		<script> showAttachedFiles(); </script> 


<%-- #################################################################### --%>
<%-- #####################   END of Task table   ######################## --%>
<%-- #################################################################### --%>
		
			<br>
			<input type="image" src="<%= sImagesURL %>button_save.gif" width="70" height="20">
			<input type=hidden name=DATE_FIELD_NAME value="">
		
		<br>
		<!--<div class=smallFont>Last Updated: <%= lastUpdateDate %>
			<a href="javascript: openPopup('CustChanges?cust=<%= cust.getVistaCustNum() %>&viewPage=<%= request.getParameter("page") %>','custChanges',300,600)">View Changes</a></div>
			-->
		</form>
</td>
</tr>
</table>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
  </BODY>
</HTML>
