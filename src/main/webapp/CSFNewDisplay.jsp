<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ include file="analytics.jsp" %>
<%
String divisionId = request.getParameter("divisionId");
ArrayList districtList = (ArrayList) request.getAttribute("districtList");
%>
<html>
<script>
 function checkDistrictSelection(){
        districtSelected = false;
    	for (i=0; i< csfform.csfDistricts.options.length; i++){
    	    if (csfform.csfDistricts.options[i].selected){
    	        districtSelected = true;
    	    }
    	}
    	if (!districtSelected){
    	    alert("You must select at least one district!");
    	    return false;
    	} else {
    	    return true;
    	}
 }

</script>
<%@ include file="./SMRCHeader.jsp" %>

<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>

		<span class="crumbarrow">&gt;</span>
		<a class="crumb" href="CSF?page=Division&divisionId=<%= divisionId %>">Division Critical Success Factors</a>
		<span class="crumbarrow">&gt;</span>
		<span class="crumbcurrent">New CSF</span>
      </p> 
    </td>
  </tr>
</table>
<form name="csfform" action="CSF" method="POST" onsubmit="return checkDistrictSelection()">
<table width="760" border="0" cellspacing="0" cellpadding="0">

  <tr> 
    <td width="10">&nbsp;</td>
   <td width="750" valign="top"> 
      <p>&nbsp;</p>
      <p class="heading2">New Critical Success Factor</p><br>
	  <table width="100%" border="0" cellspacing="10" cellpadding="0">
	  	<tr>
	  		<td width="14%">
	  			<div align="right">CSF Description:</div>
	  		</td>
	  		<td width="46%">
  		    	<input name="CSF" type="text" size="50">
		 	</td>
	  	    <td width="40%"><input type="image" src="<%= sImagesURL %>button_save.gif" width="70" height="20"></td>
	  	</tr>
	  	<tr>
	  		<td width="14%" align="right">Districts:
	  		</td>
	  		<td><select name="csfDistricts" multiple>
	  			 	<% for (int i=0; i< districtList.size(); i++){
	  			 	        String selected = "";
	  			 	    	DropDownBean bean = (DropDownBean) districtList.get(i);
	  			 	    	if (bean.getValue().equalsIgnoreCase("All")){
	  			 	    	    selected = "selected";
	  			 	    	}
	  			 	%>
	  			 			<option value="<%= bean.getValue() %>" <%= selected %>><%= bean.getName() %></option>
	  			 	<% }
	  			 	%>
	  		    </select>
	  		</td>
	  	</tr>
  	</table>
   </td>
  </tr>
  
</table>
<input name="divisionId" type="hidden" value="<%= request.getParameter("divisionId") %>">
<input name="page" type="hidden" value="saveNew">
</form>
<p>&nbsp;</p>

  </body>
</html>
