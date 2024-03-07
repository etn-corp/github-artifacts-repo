<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.*"%>
<%@ include file="analytics.jsp" %>
<html>
	<%@ include file="./SMRCHeader.jsp" %>
	<%
		ArrayList allVisits = (ArrayList)request.getAttribute("allVisits");
		CustomerVisit visit = (CustomerVisit)request.getAttribute("visit");
		ArrayList contacts = (ArrayList)request.getAttribute("contacts");
		TreeMap allUsersTm = (TreeMap)request.getAttribute("allUsersTm");
		ArrayList visitOutcomes = (ArrayList)request.getAttribute("visitOutcomes");
		ArrayList visitReasons = (ArrayList)request.getAttribute("visitReasons");
		TreeMap selectedUsers = (TreeMap)request.getAttribute("selectedUsers");
		TreeMap selectedContacts = (TreeMap)request.getAttribute("selectedContacts");
		ArrayList tasks = (ArrayList) request.getAttribute("tasks");
	
		Account acct = header.getAccount();
		User theUser = header.getUser();
	
		boolean ableToUpdate = theUser.ableToUpdate(acct) || theUser.hasSegmentOverride(acct) || (theUser.equals(acct.getUserIdAdded()) && acct.isProspect());

		if (visit.getDateAdded() != null) {
			GregorianCalendar gc = new GregorianCalendar() ;
			Date currentDate = gc.getTime() ;
			Date addedDate = visit.getDateAdded() ;
			long days = (currentDate.getTime()-addedDate.getTime()) / (24*3600*1000) ;
			if(days>=10){
				ableToUpdate=false;
			}
	
		}
	%>
	<script language="javascript" src="<%= jsURL %>validation/customerVisitLog.js"></script>
	<script language="javascript">
		function newWindow(url, windowName, width, height) {
		catWindow = window.open(url, windowName, config="\'width=" + width + ",height=" + height + ",location=no,scrollbars=yes,toolbar=no,resizeable=yes,status=1\'");
		}
		function deleteTask(deleteId){
		theform.deleteTask.value = deleteId;
		theform.page.value = "deleteTask";
		theform.submit();
		}
	</script>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750">
				<p><br>
					<a class="crumb" href="SMRCHome">Home Page</a>
					<span class="crumbarrow">&gt;</span>
					<span class="crumbcurrent">Log Customer Visits</span>
				</p>
			</td>
		</tr>
	</table>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750" valign="top">
				<p>&nbsp;</p>
				<% if(request.getParameter("page")!=null && request.getParameter("page").equals("save")){ %>
				<blockquote><font class="crumbcurrent">Save Successful</font></blockquote>
				<% }else if(request.getParameter("page")!=null && request.getParameter("page").equals("refresh")){ %>
				<blockquote><font class="crumbcurrent"><img align="absmiddle" src="<%= sImagesURL %>alert.gif">&nbsp;The page refreshed with your selection, but you must click the "Save" button to save your changes</font></blockquote>
				<% }else if(request.getParameter("page")!=null && request.getParameter("page").equals("deleteTask")){ %>
				<blockquote><font class="crumbcurrent"><img align="absmiddle" src="<%= sImagesURL %>alert.gif">&nbsp;Task Deletion Successful</font></blockquote>
				<% } %>
				<p class="heading2">Log Customer Visits</p>
				<p><i>*Note - You are not able to edit visit details after 10 days</i></p>
				<form action="CustomerVisits" name="theform" method="GET" onSubmit="javascript:return formValidation();">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="23%"><div align="right"><font class="crumbcurrent">*</font> General Description:</div></td>
							<td width="2%">&nbsp;</td>
							<td width="75%">
								<%= StringManipulation.createTextBox("VISIT_DESCRIPTION",visit.getDescription(),ableToUpdate,"45","45") %>
							</td>
						</tr>
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr>
							<td width="23%" valign="top"><div align="right">Customer Employees Present:</div></td>
							<td width="2%">&nbsp;</td>
							<td width="75%">
								<% if(ableToUpdate){ %>
								<select multiple name="VISIT_CONTACTS" size="6">
									<%
									for(int i=0;i<contacts.size();i++){
										Contact contact = (Contact)contacts.get(i);
										if(selectedContacts!=null && selectedContacts.containsKey(""+contact.getId())){
									%>
									<option selected value="<%= contact.getId() %>"><%= contact.getLastName() %>, <%= contact.getFirstName() %></option>
									<% }else{ %>
									<option value="<%= contact.getId() %>"><%= contact.getLastName() %>, <%= contact.getFirstName() %></option>
									<% }
									} %>
								</select>
								<a href="javascript:newWindow('Contacts?page=add&acctId=<%= request.getParameter("acctId") %>','contacts', 650, 470)"><img src="<%= sImagesURL %>button_add_new.gif" width="70" height="20" hspace="5" border="0"></a>
								<% }else{
												if(selectedContacts!=null){
															for(int i=0;i<contacts.size();i++){
																		Contact contact = (Contact)contacts.get(i);
																		if(selectedContacts.containsKey(""+contact.getId())){
																		%>
								<%= contact.getLastName() %>, <%= contact.getFirstName() %><br>
								<%
								}
								}
								}
								}
								%>
							</td>
						</tr>
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr>
							<td><div align="right">Eaton Employees Present:</div></td>
							<td>&nbsp;</td>
							<td>
								<% if(ableToUpdate){ %>
								<select multiple name="VISIT_EMPLOYEES" size="6">
									<%
					            Iterator it = allUsersTm.entrySet().iterator();  
								while(it.hasNext()) {
					                    Map.Entry e = (Map.Entry)it.next();
					                    if (e != null) {
					                    String key = (String)e.getKey();
					                    	if (key != null && key.length() > 0) {
					                    	    String value = (String)e.getValue();
					                    	    if(selectedUsers!=null && selectedUsers.containsKey(value)){
					                    	    %>
					                    	        <option selected value="<%= value %>"><%= key %></option>
					                    	    <%
					                    	    }else {
					                    	    %>
					                    	        <option value="<%= value %>"><%= key %></option>
					                    	    <%
					                    	    }
					                    	} // end if key not null
					                    } // end if entry not null
					                }
					                %>
					           </select>
									
					           <% }else if(selectedUsers!=null){
						              Iterator it = allUsersTm.entrySet().iterator();
						              while(it.hasNext()) {
						                    Map.Entry e = (Map.Entry)it.next();
						                    if (e != null) {
						                    String key = (String)e.getKey();
						                    	if (key != null && key.length() > 0) {
						                    	    String value = (String)e.getValue();
						                    	    if(selectedUsers.containsKey(value)){
						                    	        out.println(key + "<br>");
						                    	    }
						                    	    
						                    	    //out.println(key + "<br>");
						                    	} // end if key not null
						                    } // end if entry not null
						              }
								}
								%>
							</td>
						</tr>
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr>
							<td><div align="right"><font class="crumbcurrent">*</font> Date of Visit:</div></td>
							<td>&nbsp;</td>
							<td>
								<% if(ableToUpdate || visit.getVisitDate()!=null){ %>
								<%= StringManipulation.createTextBox("VISIT_DATE_MON",visit.getVisitMonth(),ableToUpdate,"2","2") %>/
								<%= StringManipulation.createTextBox("VISIT_DATE_DAY",visit.getVisitDay(),ableToUpdate,"2","2") %>/
								<%= StringManipulation.createTextBox("VISIT_DATE_YEAR",visit.getVisitYear(),ableToUpdate,"4","4") %>
								<% } %>&nbsp;&nbsp;&nbsp;MM/DD/YYYY
							</td>
						</tr>
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr>
							<td><div align="right"><font class="crumbcurrent">*</font> Reason for Visit:</div></td>
							<td>&nbsp;</td>
							<td>
								<% if(ableToUpdate){ %>
								<select name="VISIT_REASON_TYPE_ID">
									<option value="0">Select One...</option>
									<%
									int selectedCodeId = visit.getReasonId();
									for(int i=0;i<visitReasons.size();i++){
										CodeType code = (CodeType)visitReasons.get(i);
										if(code.getId()==selectedCodeId){
									%>
									<option value="<%= code.getId() %>" selected><%= code.getName() %></option>
									<% }else{ %>
									<option value="<%= code.getId() %>"><%= code.getName() %></option>
									<% }
									}
									 %>
								</select>
								<% }else{
												int selectedCodeId = visit.getReasonId();
												for(int i=0;i<visitReasons.size();i++){
													CodeType code = (CodeType)visitReasons.get(i);
													if(code.getId()==selectedCodeId){
													%>
								<%= code.getName() %>
								<%
								break;
								}
								}
								} %>
							</td>
						</tr>
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr>
							<td><div align="right"><font class="crumbcurrent">*</font> Outcome of Visit:</div></td>
							<td>&nbsp;</td>
							<td>
								<% if(ableToUpdate){ %>
								<select name="VISIT_OUTCOME_TYPE_ID">
									<option value="0">Select One...</option>
									<%
									int selectedCodeId = visit.getOutcomeId();
									for(int i=0;i<visitOutcomes.size();i++){
										CodeType code = (CodeType)visitOutcomes.get(i);
										if(code.getId()==selectedCodeId){
									%>
									<option value="<%= code.getId() %>" selected><%= code.getName() %></option>
									<% }else{ %>
									<option value="<%= code.getId() %>"><%= code.getName() %></option>
									<% }
									}
									 %>
								</select>
								<% }else{
												int selectedCodeId = visit.getOutcomeId();
												for(int i=0;i<visitOutcomes.size();i++){
													CodeType code = (CodeType)visitOutcomes.get(i);
													if(code.getId()==selectedCodeId){
													%>
								<%= code.getName() %>
								<%
								break;
								}
								}
								} %>
							</td>
						</tr>
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr>
							<td><div align="right">Date of Next Visit:</div></td>
							<td>&nbsp;</td>
							<td>
								<% if(ableToUpdate || visit.getNextVisitDate()!=null){ %>
								<%= StringManipulation.createTextBox("NEXT_VISIT_DATE_MON",visit.getNextVisitMonth(),ableToUpdate,"2","2") %>/
								<%= StringManipulation.createTextBox("NEXT_VISIT_DATE_DAY",visit.getNextVisitDay(),ableToUpdate,"2","2") %>/
								<%= StringManipulation.createTextBox("NEXT_VISIT_DATE_YEAR",visit.getNextVisitYear(),ableToUpdate,"4","4") %>
								<% } %>&nbsp;&nbsp;&nbsp;MM/DD/YYYY
							</td>
						</tr>
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr>
							<td valign="top"><div align="right">Notes:</div></td>
							<td>&nbsp;</td>
							<td>
								<% if(ableToUpdate){ %>
								<textarea name="VISIT_NOTES" cols="45" rows="7"><%= visit.getNotes() %></textarea>
								<% }else{ %>
								<%= visit.getNotes() %>
								<% } %>
							</td>
						</tr>
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
					</table>
					<% if(ableToUpdate){ %>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tr>
							<td width="24%"><div align="right"></div></td>
							<td width="76%"><input type="image" src="<%= sImagesURL %>button_save.gif" width="70" height="20"></td>
						</tr>
					</table>
					<% } %>
					<input type="hidden" name="page" value="save">
					<input type="hidden" name="acctId" value="<%= request.getParameter("acctId") %>">
					<input type="hidden" name="visitId" value="<%= visit.getId() %>">
					<br><hr width="100%" noshade color="#DDDDDD"><br>
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
							<%	if (ableToUpdate){ %>
							<td class=columnheader-acctprofile>Action</td>
							<% } %>
						</thead>
						<%
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
							User usr = new User();
							if (task.getAssignedUser() != null){
							    usr = task.getAssignedUser();
							%>
								<td class=smallFontL><%= usr.getFirstName() %> <%= usr.getLastName() %></td>
							<%
							} else {
							%>
							    <td class=smallFontL>&nbsp;</td>
							<%
							}
							ArrayList ccUsers = task.getCcUsers();
							StringBuffer ccUsersBuffer = new StringBuffer();
							for(int j=0;j<ccUsers.size();j++){
								usr = (User)ccUsers.get(j);
								ccUsersBuffer.append(usr.getFirstName() + " " + usr.getLastName());
								if(j!=ccUsers.size()-1){
									ccUsersBuffer.append(", ");
								}
							}
							//usr = task.getCCUser();
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
							<% if (ableToUpdate){  %>
							<td>
								<a href="javascript:deleteTask('<%= task.getTaskId() %>')"><img src="<%= sImagesURL %>button_delete.gif" width="70" height="20" hspace="5" border="0"></a>
								<a href="javascript:newWindow('CustomerVisitTaskUpdate?action=update&taskId=<%=  task.getTaskId() %>&visitId=<%= visit.getId() %>&acctId=<%= request.getParameter("acctId") %>', 'UpdateTask', 800, 400)"><img src="<%= sImagesURL %>button_update.gif" width="70" height="20" hspace="5" border="0"></a>
							</td>
							<%}%>
						</tr>
						<%
							}
							%>
					</table>
					<input type=hidden name=deleteTask value="0">
					<%
					if(tasks.size()==0){
					%>
					<br>No tasks for this visit
					<%
					}
					%>
					<br><br>
					<%
					if (ableToUpdate && (visit.getId() > 0)){
					%>
					<a href="javascript:newWindow('CustomerVisitTaskUpdate?action=add&visitId=<%= visit.getId() %>&acctId=<%= request.getParameter("acctId") %>', 'AddTask', 800, 400)"><img src="<%= sImagesURL %>button_add_new.gif" width="70" height="20" hspace="5" border="0"></a>
					<%}%>
					<% if(visit.getId()==0){ %>
					* You will be able to add tasks after you save this customer visit
					<% } %>
				</form>
				<% if(allVisits.size()!=0){ %>
				<br>
				<hr width="100%" noshade color="#DDDDDD"><br>
				<form method="POST" action="CustomerVisits">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="23%"><div align="right">Other Visits to this Account:</div></td>
							<td width="2%">&nbsp; </td>
							<td width="75%">
								<select name="visitId">
									<option value="">Select One...</option>
									<%
									for(int i=0;i<allVisits.size();i++){
									CustomerVisit exitingVisit = (CustomerVisit)allVisits.get(i);
									%>
									<option value="<%= exitingVisit.getId() %>"><%= exitingVisit.getVisitDate() %> - <%= exitingVisit.getDescription() %></option>
									<% } %>
								</select>
								<input type="hidden" name="acctId" value="<%= request.getParameter("acctId") %>">
								<input type="image" src="<%= sImagesURL %>button_view.gif" width="70" height="20" hspace="5" align="absmiddle"><a href="CustomerVisits?acctId=<%= request.getParameter("acctId") %>"><img src="<%= sImagesURL %>button_new_visit.gif" width="70" height="20" border="0" hspace="5" align="absmiddle"></a></td>
						</tr>
					</table>
				</form>
				<% } %>
				<p>&nbsp;</p>
			</td>
		</tr>
	</table>
	<p>&nbsp;</p>
	</body>
</html>
