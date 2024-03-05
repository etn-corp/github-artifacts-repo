<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ include file="analytics.jsp" %>
<%
TreeMap states = (TreeMap)request.getAttribute("states");
ArrayList districts = (ArrayList)request.getAttribute("districts");
%>


<html>
<%@ include file="./SMRCHeader.jsp" %>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>
		<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Customer Search</span>

      </p> 
    </td>
  </tr>
</table>
<form action="CustomerListing" method="POST">
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
   <td width="750" valign="top"> 
      <p>&nbsp;</p>
   <p class="heading2">Customer Search</p>
   <table width="100%" border="0" cellspacing="10" cellpadding="0">

   	<tr>
   		<td width="22%"><div align="right">Customer Name:</div></td>
   		<td colspan="2"><input type="text" name="CUSTOMER_NAME">
   		</td>
   </tr>
   	<tr>
   		<td><div align="right">Vista Customer Number:</div></td>
   		<td colspan="2"><input type="text" name="VISTA_CUSTOMER_NUMBER">

   		</td>
   </tr>
   	<tr>

   		<td><div align="right">Parents Only:</div></td>
   		<td colspan="2"><input type="checkbox" name="PARENTS_ONLY" value="Y">
</td>
   </tr>
   	<tr>
   		<td><div align="right">Sales Engineer:</div></td>
   		<td width="26%"><font class="crumbcurrent">*</font> Last: <input type="text" name="LAST_NAME">
   		</td>
        <td width="52%"><font class="crumbcurrent">*</font> First: <input type="text" name="FIRST_NAME">
      </td>
   </tr>
   	<tr>
   		<td><div align="right"><font class="crumbcurrent">*</font> State:</div></td>
   		<td colspan="2">
				<select name="STATE" size="1">
				 <option value="0">Select One...</option>
				<%
				Set stateSet = states.entrySet();
	      Iterator it = stateSet.iterator();
	      while (it.hasNext()){
	        java.util.Map.Entry state = (java.util.Map.Entry) it.next();
					%>
					<option value="<%= state.getKey() %>"><%= state.getValue() %></option>
					<%	       
	      }
				%>
					</select>	
   		</td>
   </tr>

   	<tr>
   		<td><div align="right">District:</div></td>
   		<td colspan="2">
   			<select name="DISTRICT">
   			<option value="0" selected>Select One...</option>
   			<%
   			for(int i=0;i<districts.size();i++){
   				Geography district = (Geography)districts.get(i);
   			%>
   			<option value="<%= district.getGeog() %>"><%= district.getZone() %>-<%= district.getDistrict() %> <%= district.getDescription() %></option>
		    <% } %>
		    
		    </select>
</td>
   </tr>

   	<tr>
   		<td><div align="right">Potential Customers Only:</div></td>
   		<td colspan="2"><input type="checkbox" name="POTENTIAL_CUSTOMERS_ONLY" value="Y">
   		</td>
   </tr>
  	<tr>
		<td><div align="right"></div></td>
		<td colspan="2">
		<font class="crumbcurrent">*</font> Accounts added today will not be searchable if you filter by these fields.
		</td>
		</tr>
   </table>
   <br>
   <table width="100%" border="0" cellspacing="10" cellpadding="0">

   	<tr>
   		<td width="33%"><div align="right"><input type="image" src="<%= sImagesURL %>button_search.gif" width="70" height="20"></div></td>
   		<td>&nbsp;</td>
   </tr>
   </table>
   <p>&nbsp;</p>
   </td>
  </tr>
</table>
<input type="hidden" name="page" value="listing">
</form>
<p>&nbsp;</p>
  <script language='javascript'>
   	document.forms[0].CUSTOMER_NAME.focus();
   </script>
  </body>
</html>
