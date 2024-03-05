<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>

<%

boolean showDistApp = false;
if(request.getAttribute("displayDistApplication")!= null) {

	String distApp = (String)request.getAttribute("displayDistApplication");
	if(distApp.equalsIgnoreCase("Y")) {
		showDistApp = true;
	}
}
 // Added to avoid Add Module option for Inactive and Marked for deletion Customers SD0000002660312. 
String module_status=(String)acct.getStatus();

DistributorForms distForms = new DistributorForms(usr,acct);
if(!acct.getVcn().equals(""))
{

%>


<table width="175" border="0" cellspacing="1" cellpadding="0">
             <tr> 
                <td>&nbsp;Required Vista Forms</td>
             </tr>
             <tr> 
                <td>&nbsp;&nbsp;&nbsp;<a href="AccountProfile?acctId=<%= acct.getVcn() %>">Account Profile</a></td>
             </tr>
             <%
             if(distForms.isDistributor() || showDistApp){
             %>
             <tr>
                <td>&nbsp;&nbsp;&nbsp;<a href="DistributorSignup?page=app&acctId=<%= acct.getVcn() %>">Distributor Application</a></td>
             </tr>
             <%
             }
            
        // Added to avoid Add Module option for Inactive and Marked for deletion Customers SD0000002660312.     
             if((!module_status.equalsIgnoreCase("I")&&!module_status.equalsIgnoreCase("Inactive")&&!module_status.equalsIgnoreCase("Marked for Delete"))&&(distForms.isDistributor()||showDistApp))
             {
             %>
             <tr> 
                <td>&nbsp;&nbsp;&nbsp;<a href="DistributorProductLoadingModule?where=notApp&acctId=<%= acct.getVcn() %>">Add/Modify Product Module</a></td>
             </tr>
             <%
             }
            
             
             if(distForms.isDistributor() && !acct.isProspect() ){
             %>
             <tr> 
                <td>&nbsp;&nbsp;&nbsp;<a href="DistributorProductLoadingModuleHistory?acctId=<%= acct.getVcn() %>">Module Request History</a></td>
             </tr>
             <%
             }
             if(distForms.isDistributor()){
             %>		 			 			 		
             <!-- tr> 
                <td>&nbsp;&nbsp;&nbsp;<a href="DistributorSignup?page=approval&acctId=<= acct.getVcn() %>">Application Summary</a></td>
             </tr-->
             <% } 
              if(distForms.isCreditAuthorization()){
             %>
             <tr> 
                <td>&nbsp;&nbsp;&nbsp;<a href="DistributorSignup?page=credit&acctId=<%= acct.getVcn() %>">Distributor Credit Application</a></td>
             </tr>
             <%
             }
             %>
             <tr> 
                <td>&nbsp;&nbsp;&nbsp;<a href="AccountProfile?page=workflow&acctId=<%= acct.getVcn() %>">Vista Submit/Status</a></td>
             </tr>
			
			<% 
			if(acct.getStatus().equalsIgnoreCase("REJECTED") || acct.getStatus().equalsIgnoreCase("INACTIVE")) {
				if(!acct.isProspect()) {
			%>		
					<p>
					<tr>
						<td>&nbsp;&nbsp;&nbsp;
						<% if(usr.isChannelMarketingManager()) { %>
							<a href="AccountProfile?page=Reinstate&acctId=<%= acct.getVcn() %>">Reinstate Customer</a>
						<% } else { %>
							<a href='javascript:alert("Please contact Channel Management to Reinstate this Customer.")'>Reinstate Customer</a>
						<% } %>
						</td>
					</tr>
			<%
				}
			}
			
			%>
			<% if(acct.isProspect()) { %>
				<% if(usr.isChannelMarketingManager() || usr.getUserid().equalsIgnoreCase(acct.getUserIdAdded())) { %>
					<tr>
						<td>&nbsp;&nbsp;&nbsp;<a href="AccountProfile?page=RemoveProspect&acctId=<%= acct.getVcn() %>">Remove Prospect</a></td>
					</tr>
				<% } %>
			<% } %>
			<% if(!acct.isProspect()) { %>
			<tr>
				<td>&nbsp;&nbsp;&nbsp;<a href="javascript:newWindow('SegmentSelect?acctId=<%= acct.getVcn() %>&isModify=Y','SegmentModify')">Change Categories</a></td>
			</tr>
			<% } %>
			<%
             if(distForms.isTermination()){
             %>
             <tr> 
                <td>&nbsp;</td>
             </tr>
             <tr> 
                <td><a href="DistributorSignup?page=termination&acctId=<%= acct.getVcn() %>">Termination Form</a></td>
             </tr>
             <tr> 
                <td><a href="AccountProfile?page=workflow&acctId=<%= acct.getVcn() %>&termination=true">Termination Approvals</a></td>
             </tr>
             <% } %>             
		   </table>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
<% } %>
