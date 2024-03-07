<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.*"%>
<%@ include file="analytics.jsp" %>
<%
	ArrayList visits = (ArrayList)request.getAttribute("visits");

	
%>

<html>
<%@ include file="./SMRCHeader.jsp" %>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>
		<span class="crumbarrow">&gt;</span>
		<a class="crumb" href="AcctPlanReport?page=reportother">Other Reports</a>
	<span class="crumbarrow">&gt;</span>
	<span class="crumbcurrent">Customer Visit Report</span>
      </p> 
  </td>
  </tr>
</table>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
   <td width="750" valign="top"> 
      <p>&nbsp;</p>
      <p class="heading2">Customer Visit Report</p>

	  <table width="100%" border="0" cellspacing="1" cellpadding="0">

	  <tr>
  		  <th width="17%" valign="top">Account Name</th>
	  		<th width="12%" valign="top">Sales Engineer</th>
	  		<th width="15%" valign="top">Visit Description</th>
	  		<th width="18%" valign="top">Visit Date</th>
	  		<th width="18%" valign="top">Visit Reason</th>
	  		<th width="20%" valign="top">Visit Outcome</th>
	  </tr>
	  <%
	  if(visits.size()==0){
	  %>
	  	 <tr>
	  		<td colspan="6">
	  			<div align="left" id="cellShade"><br>&nbsp;No visits found.</div>
  		 </td>
  		 </tr>
	  <%
	  }
	  String shade=null;
	  for(int i=0;i<visits.size();i++){
	  	CustomerVisit visit = (CustomerVisit)visits.get(i);
		  if(i%2==0){
		 		shade="";
		  }else{
		  	shade=" class=\"cellShade\"";
		  }
	  	
	  %>
	  	<tr<%= shade %>>
	  		<td>
	  			<div align="left" id="cellShade"><%= visit.getCustomerName() %></div>
  		 </td>

	  		<td>
	  			<div align="left"><%= visit.getSalesEngineer() %></div>
	  		</td>
	  		<td>
	  			<div align="left"><a href="CustomerVisits?acctId=<%= visit.getVcn() %>&visitId=<%= visit.getId() %>"><%= visit.getDescription() %></a></div>
	  		</td>
	  		<td>
	  			<div align="left"><%= visit.getVisitDate() %></div>
	  		</td>
	  		<td>
	  			<div align="left"><%= visit.getReason() %></div>
	  		</td>
	  		<td>
	  			<div align="left"><%= visit.getOutcome() %></div>
	  		</td>
  		</tr>
		<% } %>
  	</table><br>
  	<br>
   </td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>

