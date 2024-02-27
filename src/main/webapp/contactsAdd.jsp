<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.*"%>

<%
Contact contact = (Contact)request.getAttribute("contact");
TreeMap jobFunctions = (TreeMap)request.getAttribute("jobFunctions");

boolean ableToEdit=true;
String disabled="";
if(!ableToEdit){
	disabled=" disabled";
}

%>

<html>
<%@ include file="./SMRCHeaderNoNav.jsp" %>
<%@ include file="analytics.jsp" %>
<script language="javascript" src="<%= jsURL %>validation/accountProfileDisplay.js"></script>
<form name="theform" action="Contacts" method="POST" onSubmit="javascript:return contactValidation();">
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
   <td width="750" valign="top"> 
      <p>&nbsp;</p>
      <% if(request.getParameter("page").equals("add")){
      %>
		  <p class="heading2">Add Contact</p>
		  <% }else if(request.getParameter("page").equals("edit")){ %>
		  <p class="heading2">Edit Contact</p>
		  <% }else if(request.getParameter("page").equals("delete")){ %>
		  <p class="heading2">Delete Contact</p>
		  <p class="heading3">Are you sure you want to delete this contact?</p>
		  <%
		  ableToEdit=false;
			disabled=" disabled";
		  }
		  %> 
   <table width="100%" border="0" cellspacing="10" cellpadding="0">

   	<tr>
   		<td width="22%"><div align="right"><font class="crumbcurrent">*</font> First Name:</div></td>
   		<td>
   		<%= StringManipulation.createTextBox("FIRST_NAME",contact.getFirstName(),ableToEdit,"","25") %>
   		</td>
   </tr>
   	<tr>
   		<td><div align="right"><font class="crumbcurrent">*</font> Last Name:</div></td>
   		<td>
			<%= StringManipulation.createTextBox("LAST_NAME",contact.getLastName(),ableToEdit,"","25") %>
   		</td>
   </tr>
   	<tr>
   		<td><div align="right"><font class="crumbcurrent">*</font>Job Title:</div></td>
   		<td>
   					<%= StringManipulation.createTextBox("JOB_TITLE",contact.getTitle(),ableToEdit,"","50") %>
  		</td>
   </tr>
   	<tr>

   		<td><div align="right">Functional Position:</div></td>
   		<td>
   		<%
   		if(ableToEdit){
   		%>
   		<select name="POSITION">
   		<option selected value="0">Select One...</option>
   		<%
   		}
   		
			String selectedFunction="";
   		Set functions = jobFunctions.entrySet();
      Iterator it = functions.iterator();

      while (it.hasNext()){
	      String selected = "";
				java.util.Map.Entry function = (java.util.Map.Entry) it.next();
				if(contact.getTitleId()==Globals.a2int((String)function.getValue())){
					selected=" selected";
					selectedFunction=(String)function.getKey();
					//salesValue=rec.getPercentage();
					
				}
				if(ableToEdit){
				%>
				<option value="<%= function.getValue() %>"<%= selected %>><%= function.getKey() %></option>
				<%
				}
			}
			if(ableToEdit){
			%>
			</select>
   		<%
   		}else{
   		%>
			<%= selectedFunction %>
   		<%
   		}
			%>	    
   		</td>
   </tr>
   	<tr>

   		<td><div align="right"><font class="crumbcurrent">*</font> Phone Number:</div></td>
   		<td>
   					<%= StringManipulation.createTextBox("PHONE",contact.getPhone(),ableToEdit,"","25") %>
   		</td>
   </tr>
   	<tr>
   		<td><div align="right">Fax Number:</div></td>
   		<td>
   					<%= StringManipulation.createTextBox("FAX",contact.getFax(),ableToEdit,"","25") %>
   		</td>

   </tr>
   	<tr>
   		<td><div align="right"><font class="crumbcurrent">*</font> Email Address:</div></td>
   		<td>
   		<%= StringManipulation.createTextBox("EMAIL",contact.getEmailAddress(),ableToEdit,"","100") %>
   		</td>
   </tr>
   	<tr>
   		<td><div align="right">Comments:</div></td>
   		<td>
   		<%
   		if(ableToEdit){
   		%>
   		<textarea name="COMMENTS" cols="40" rows="4"><%= contact.getComments() %></textarea>
   		<% }else{ %>
   		<%= contact.getComments() %><input type="hidden" name="<%= contact.getComments() %>" value="">
   		<% } %>   			
   		</td>
   </tr>
   	<tr>
   		<td><div align="right">Pricing Contact?</div></td>
   		<td>
   		<%
   		String checked="";
			if(contact.isPricingContact()){
				checked=" checked";
			}
			%>
   		<input type="checkbox" name="PRICING_CONTACT" value="Y"<%= checked + disabled %>>
   		</td>
   </tr>
   </table><br><br>
   <table width="100%" border="0" cellspacing="10" cellpadding="0">
   	<tr>
   		<td width="33%"><div align="right">
   		
   		<% if(request.getParameter("page").equals("delete")){
      %>
   		<input type="image" src="<%= sImagesURL %>button_delete.gif" width="70" height="20">&nbsp;<input type="image" src="<%= sImagesURL %>button_cancel.gif" width="70" height="20" onclick="javascript: self.close()">
   		<% }else{ %>
   		<input type="image" src="<%= sImagesURL %>button_save.gif" width="70" height="20">
   		<% } %>   		
   		</div></td>
   		<td width="67%"></td>
   </tr>
   </table>
   <p>&nbsp;</p>

   </td>
  </tr>
</table>
<input type="hidden" name="fwdPage" value="<%= StringManipulation.noNull(request.getParameter("fwdPage")) %>">
<% if(request.getParameter("page").equals("add")){ %>
<input type="hidden" name="acctId" value="<%= request.getParameter("acctId") %>">
<% }else{ %>
<input type="hidden" name="acctId" value="<%= contact.getCustomer() %>">
<% } %>
<input type="hidden" name="contactid" value="<%= contact.getId() %>">
<% if(request.getParameter("page").equals("delete")){
%>
<input type="hidden" name="page" value="dodelete">
<% }else{ %>
<input type="hidden" name="page" value="save">
<% } %>   		
</form>
<p>&nbsp;</p>
  </body>
</html>
