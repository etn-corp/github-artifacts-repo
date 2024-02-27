<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*, java.text.* " %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>

<html>
<%@ include file="./SMRCHeader.jsp" %>
<%@ include file="analytics.jsp" %>

<%
Account acct = header.getAccount();

User usr = header.getUser();
String acctName = (String)request.getAttribute("acctName");

boolean hasHistory = ((Boolean) request.getAttribute("hasHistory")).booleanValue(); 

ModuleChangeRequest[] moduleChangeHistory = null;
Workflow[] workflows = null;
String[] moduleStatusDescriptions = null;

if (hasHistory){
	moduleChangeHistory = (ModuleChangeRequest[]) request.getAttribute("moduleChangeHistory");
	workflows = (Workflow[]) request.getAttribute("workflows");
	moduleStatusDescriptions = (String[]) request.getAttribute("moduleStatusDescriptions");
}
 
SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");


%>

<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>

		<span class="crumbarrow">&gt;</span>
		<span class="crumbcurrent">Product Module Request History</span></p> 
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
                
            <p class="heading2">
            Product Module Request</p>
           <p class="heading3">Account: <%= acct.getCustomerName() %></p>            
            <span class="heading3">Distributor Product Module Change History</span><br><br>
          	 <br><hr width="100%"><br>  
		 		   
		 	<%  if (hasHistory){  
		 			for (int i=0; i < moduleChangeHistory.length; i++){
		 				ModuleChangeRequest moduleChangeRequest = moduleChangeHistory[i];
		 	%>
		 				<br><span class="moduleHeaderBold">Request Status:</span>&nbsp;<%= moduleStatusDescriptions[i]  %><br>
		 			<% if (moduleStatusDescriptions[i].trim().equalsIgnoreCase(ProductModuleDAO.MODULE_CHANGE_VISTA_STATUS_APPROVED)) {	%>
		 				<span class=textredbold>This request is approved and pending an update in Vista.</span><br>
		 			<% } %>
				 		<br><span class="moduleHeaderBold">Module Changes:</span> <br>
				 	<%	ModuleChangeProduct[] products = moduleChangeRequest.getModuleProducts();	
				 		for (int p=0; p < products.length; p++){
				 			ModuleChangeProduct product = products[p];
				 	%>
					   		<%= product.getLoadingModuleName() %> (<%= product.getLoadingModuleShortCode() %>) : <%= (product.getAction().trim().equalsIgnoreCase("A"))?"Added":"Removed" %> <br>
					<%	} // end of products loop	%>
						<br><span class="moduleHeaderBold">Notes:</span><br>
					<%	ModuleChangeReasonNotes[] notes = moduleChangeRequest.getModuleChangeReasonNotes();
						for (int n=0; n < notes.length; n++){
							ModuleChangeReasonNotes note = notes[n];
					%>
							<u><%= note.getUserAddedName()  %> (<%= sdf.format(note.getDateAdded()) %>)</u> : <em><%= note.getReasonNotes() %></em>  <br>
							
					<%	} // end of notes loop	%>
						<br><span class="moduleHeaderBold">Approvals:</span> <br>
					<% if (workflows[i] != null) {	
						  Workflow workflow = workflows[i];
						  WorkflowStep[] steps = workflow.orderWorkflowSteps();
						  for (int s=0; s < steps.length; s++){
						  	WorkflowStep step = steps[s];
					%>
							<u><%= step.getStepName() %></u>: 
						<% if (!step.getStatusFlag().trim().equalsIgnoreCase("N")){ %>
							<%= (step.getStatusFlag().trim().equalsIgnoreCase("Y"))?"Approved":"Rejected"  %> by <%= step.getUserApprovedOrRejected().getFullName() %> on <%= sdf.format(step.getDateChanged()) %>
						<% }	 %><br>
					<%	  } // end of workflow steps
						} // workflow not null  %>
							<br><hr width="100%"><br>
			<% 
						
					} // end of request loop
				} else { %>
				This distributor does not have any previous module change requests.
			<%	} 	%>
				
       	  
         </td>
        </tr>
      </table>
    
    </td>

  </tr>
</table>
<p>&nbsp;</p>
  </body>
</html>
