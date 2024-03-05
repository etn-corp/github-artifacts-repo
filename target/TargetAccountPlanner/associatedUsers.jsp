<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<html>
<%@ include file="./SMRCHeaderNoNav.jsp" %> 
<%@ include file="analytics.jsp" %>
<%
ArrayList users = (ArrayList)request.getAttribute("users");
Account acct = header.getAccount();//(Account)request.getAttribute("account");
User myUser = header.getUser();//(User)request.getAttribute("usr");
boolean isAssociated=false;
%>

<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
  <td width="750" valign="top"> 
      <p>&nbsp;</p>
      <p class="heading2">Associated Users</p><br>
	  <table width="80%" border="0" cellspacing="1" cellpadding="0">
      	<tr>
      		<th><div align="left">Last Name</div></th>
      		<th><div align="left">First Name</div></th>
      		<th><div align="left">Email</div></th>
	  		</tr>
    	
    	<%
    	if(users.size()==0){
    	%>
    	<tr>
           <td colspan="3"><br>No users associated to this account.</td>
    	</tr>
    	<%
    	}
			for(int i=0;i<users.size();i++){
				User user = (User)users.get(i);
				if(user.getUserid().equals(myUser.getUserid())){
					isAssociated=true;
				}
				%>
				<tr>
				<td align="left">
				<% if(isAssociated){
				out.println("<b>");
				} %>
				<%= user.getLastName() %>
				<% if(isAssociated){
				out.println("</b>");
				} %>
				</td>
				<td align="left"><%= user.getFirstName() %></td>
				<td align="left"><a href="mailto:<%= user.getEmailAddress() %>"><%= user.getEmailAddress() %></a></td>
				</tr>
			<% } %>
    
    	<tr>
           <td colspan="3"><br>
           <br>
           <%
           if(isAssociated){
           %>
           <a href="AccountProfile?page=associatedUsers&acctId=<%= acct.getVcn() %>&update=remove">Remove your association to this account</a></td>
					 <% }else{ %>
					  <a href="AccountProfile?page=associatedUsers&acctId=<%= acct.getVcn() %>&update=add">Associate yourself to this account</a></td>
					 <% } %>
					   <br><br>
					   
    	</tr>
  	</table>
  <p>&nbsp;</p>
  </td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>



