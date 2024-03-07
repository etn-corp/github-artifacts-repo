<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<html>    
<%@ include file="./TAPheader.jsp" %>
<%@ include file="analytics.jsp" %>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>
		<span class="crumbarrow">&gt;</span>        
		<span class="crumbcurrent">Suggestions</span>
      </p> 
    </td>
  </tr>
</table>
<br>

<%
if (request.getAttribute("topMessage") != null) {
%>
	<div class=smallFontLeft><%= request.getAttribute("topMessage") %></div>
<%
}
%>
<form action=Suggestions method=post>
    <input type=hidden name=page value='suggestions'>
    <input type=hidden name=action value='save'>
    <div class=smallFontC>Enter your suggestion in the box below and it will be sent to the Target Account Planner IT Team.</div>
    <div class=smallFontC><textarea cols=100 rows=20 name=suggestion class=smallFontL></textarea></div>
    <div class=smallFontC><input type=submit name=submit value=Send class=smallFontC></div>
</form>
</body>
</html>
