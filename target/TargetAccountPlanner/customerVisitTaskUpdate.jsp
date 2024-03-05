<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.*"%>
<%@ include file="analytics.jsp" %>

<%
	ArrayList products = (ArrayList) request.getAttribute("products");
	TreeMap usersTm = (TreeMap) request.getAttribute("usersTm");
	ArrayList ebeCategories = (ArrayList) request.getAttribute("ebeCategories");	
	String action = (String) request.getAttribute("action");
	String visitId = (String) request.getAttribute("visitId");
	String cust = (String) request.getAttribute("cust");
	boolean refreshVisits = ((Boolean) request.getAttribute("refreshVisits")).booleanValue();
		
%>

<html>
<%@ include file="./SMRCHeaderNoNav.jsp" %>

<SCRIPT LANGUAGE='JavaScript' SRC='<%= jsURL %>calendar.js'></SCRIPT>
<%
	if (refreshVisits){
%>
	<script>
		window.opener.theform.submit();
	</script>
<%	}
%>
<table width="650" border=0>	
<form name="SALESPLAN" action="CustomerVisitTaskUpdate">
		<input type=hidden name="action" value="<%= action %>">
		<input type=hidden name="visitId" value="<%= visitId %>">
		<input type=hidden name="cust" value="<%= cust %>">
		<input type=hidden name=totTask value="1">
		<table width="100%" class=tableBorder cellspacing=1 cellpadding=1>
			<caption class="heading3">Task Assignments</caption>
			<thead bgcolor=#F8F8FF>
				<td class=columnheader-acctprofile>Action</td>
				<td class=columnheader-acctprofile>Notify</td>
				<td class=columnheader-acctprofile>Product</td>
				<td class=columnheader-acctprofile>Schedule</td>
				<td class=columnheader-acctprofile>Results</td>
			</thead>

		<%
	if (action.equalsIgnoreCase("update")){
			PurchaseActionProgram task = (PurchaseActionProgram) request.getAttribute("tasks");
		    int i=0;
		    
		    
								
				%>
				<tr class=cellColor>
					<input type=hidden name="taskId" value="<%= task.getTaskId() %>">
					<td class=smallFontL>
					<input type=hidden name=taskId<%= i %> value="<%= task.getTaskId() %>">
					<textarea class=smallFontL rows=5 cols=35 name=action<%= i %>><%= task.getAction() %></textarea>
					<input type=hidden name=prevAct<%= i %> value="<%= task.getAction() %>"></td>
					<td class=smallFontL><table width="100%">
						<tr><td class=smallFontL><u>Assign To:</u></td></tr>
						<tr><td class=smallFontL><select class=smallFontL name=assignedto<%= i %>>
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
                }
                %>
			</select></td></tr>
			<tr><td class=smallFontL><u>Carbon Copy:</u></td></tr>
			<tr><td class=smallFontL>

			<select multiple size="3" class=smallFontL name=ccemail<%= i %>>
			<option class=smallFontL value='0'></option>
			<%
				StringBuffer prevCCEmailBuffer = new StringBuffer();
				ArrayList ccEmails = task.getCCEmail();
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
                }
                
				%>
			
					</select>
					<%= prevCCEmailBuffer.toString() %>
					<input type=hidden name=prevAssign<%= i %> value="<%= task.getAssignedTo() %>">
					<!--<input type=hidden name=prevCCEmail<%= i %> value="<%= task.getCCEmail() %>">-->
					</td></tr></table></td>


					<td class=smallFontL><table width="100%">
						<tr><td class=smallFontL><u>Product Line:</u></td></tr>
						<tr><td class=smallFontL><select class=smallFontL name=product<%= i %>>
						<option class=smallFontL value=''></option>

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
						</select></td></tr>
						<tr><td class=smallFontL><u>EBE Category:</u></td></tr>
						<tr><td class=smallFontL><select class=smallFontL name=ebeCat<%=+ i %>>
						<option class=smallFontL value=''></option>

		

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
					</select><input type=hidden name=prodOld<%= i %> value="<%= task.getProduct() %>"><input type=hidden name=ebeCatOld<%= i %> value="<%= task.getEBECategory() %>"></td></tr></table></td>
							<td class=smallFontC>
			<%
			if (task.getSchedule() != null) {
			%>		
									<input size=10 name=sched<%= i %> onFocus='this.blur();' value="<%= task.getScheduleAsString() %>" class=smallFont>
									<A HREF='JavaScript:displayCalendar(document.SALESPLAN,"sched<%= i %>");'>
					<br><IMG SRC='<%= sImagesURL %>Calendar.gif' WIDTH='23' HEIGHT='19' BORDER='0' ALT='Calendar'></A>
					<input type=hidden name=prevSched<%= i %> value="<%= task.getScheduleAsString() %>">
			<%
			}
			else {
			%>
						<input size=10 name=sched<%= i %> onFocus='this.blur();' class=smallFont>
					<A HREF='JavaScript:displayCalendar(document.SALESPLAN,"sched<%= i %>");'>
					<br><IMG SRC='<%= sImagesURL %>Calendar.gif' WIDTH='23' HEIGHT='19' BORDER='0' ALT='Calendar'></A>
			<%
			}

			String checked = "";
			String yN = "N";

			if (task.isComplete()) {
				checked = " checked";
				yN = "Y";
			}			

			if(header.getUser().getUserid().equals(task.getUserAdded())){
			%>
			<div class=smallFontC>Done? <input type=checkbox class=smallFontC name=complete<%= i %> <%= checked %>><input type=hidden name=prevComp<%= i %> value='<%= yN %>'></div>
			<%
			}else{
			%>
			<div class=smallFontC>Done? <input type=checkbox disabled class=smallFontC name=complete<%= i %> <%= checked %>><input type=hidden name=prevComp<%= i %> value='<%= yN %>'></div>
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
			<td class=smallFontL><textarea class=smallFontL rows=5 cols=35 name=results<%= i %>><%= task.getResults() %></textarea><input type=hidden name=prevRes<%= i %> value="<%= task.getResults() %>"></td>
			</tr>

		
			<% 
			
	} else {	
	// No need to loop, so i is just 0
		int i = 0;	
			%>
						
						<tr class=cellColor>
						<td class=smallFontL>
						<input type=hidden name=taskId<%= i %> value="new">
						<textarea name=action<%= i %> rows=5 cols=35 class=smallFontL></textarea></td>
							<td class=smallFontL><table width="100%">
								<tr><td class=smallFontL><u>Assign To:</u></td></tr>
								<tr><td class=smallFontL><select class=smallFontL name=assignedto<%= i %>>
								<option class=smallFontL value=''></option>
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
				                }
				                %>
								</select></td></tr>
								<tr><td class=smallFontL><u>Carbon Copy:</u></td></tr>
								<tr><td class=smallFontL><select multiple size="3" class=smallFontL name=ccemail<%= i %>>
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
				                }
				                %>
							</select></td></tr></table></td>
							<td class=smallFontL><table width="100%">
								<tr><td class=smallFontL><u>Product Line:</u></td></tr>
								<tr><td class=smallFontL><select class=smallFontL name=product<%= i %>>
							<option class=smallFontL value=''></option>
					<%
					for (int j=0; j < products.size(); j++) {
						Product prd = (Product)products.get(j);
						%>
								<option class=smallFontL value="<%= prd.getId() %>"><%= prd.getDescription() %></option>
						<%
					}
					%>
								</select></td></tr>
								<tr><td class=smallFontL><u>EBE Category:</u></td></tr>
								<tr><td class=smallFontL><select class=smallFontL name=ebeCat<%= i %>>
							<option class=smallFontL value=''></option>
					<%
					for (int j=0; j < ebeCategories.size(); j++) {
						EBECategory cat = (EBECategory)ebeCategories.get(j);
						%>
								<option class=smallFontL value="<%= cat.getId() %>"><%= cat.getDescription() %></option>
						<%
					}
					%>
							</select></td></tr></table></td>
							<td class=smallFontC><input size=10 name=sched<%= i %> onFocus='this.blur();' class=smallFont>
							<A HREF='JavaScript:displayCalendar(document.SALESPLAN,"sched<%= i %>");'>
							<br><IMG SRC='<%= sImagesURL %>Calendar.gif' WIDTH='23' HEIGHT='19' BORDER='0' ALT='Calendar'></A>
							<div class=smallFontC>Done? <input type=checkbox name=complete<%= i %> class=smallFontC></td>
							<td class=smallFontL><textarea name=results<%= i %> rows=5 cols=35 class=smallFontL></textarea></td>
						</tr>
					<%
   }
		%>
		</table>
		<br>
			<input type="image" src="<%= sImagesURL %>button_save.gif" width="70" height="20">
			<input type=hidden name=DATE_FIELD_NAME value=''>
			<input type=hidden name="update" value="update">
</form>	
</table>
</body>
</html>
