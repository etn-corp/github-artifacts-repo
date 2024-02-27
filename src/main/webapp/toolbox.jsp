<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.service.*"%>
<%
Toolbox toolbox = (Toolbox)request.getAttribute("toolbox");
ArrayList groups = toolbox.getGroups();

%>
<html>
<%@ include file="./SMRCHeader.jsp" %>

<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>
			<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Toolbox</span>
      </p> 
    </td>
  </tr>
</table>

<table width="760" border="0" cellspacing="0" cellpadding="0">

  <tr> 
    <td width="10">&nbsp;</td>
   <td width="750" valign="top"> 
      <p>&nbsp;</p>
      <span class="heading2">Toolbox</span>
	  <table width="70%" border="0" cellspacing="10" cellpadding="0">
	  	
<%
if(groups.size()==0) {
    %>
    <tr><td valign="top">There are no toolboxes at this segment level</td></tr>
    <%
}
for (int i=0; i < groups.size(); i++) {
	if(i%2==0){
		%>
		<tr><td width="40%" valign="top">
		<%
	}else{
		%>
		<td width="50%" valign="top">
		<%
	}
			ToolboxGroup tg = (ToolboxGroup)groups.get(i);
			%>
			<br><font class="heading3"><%= tg.getDescription() %></font><br>
			<%
			
			ArrayList toolboxElements = tg.getDetails();
			for(int j=0;j<toolboxElements.size();j++){
				ToolboxElement toolboxElement = (ToolboxElement)toolboxElements.get(j);
				String def = "";
				if (!toolboxElement.ieOnly() && !toolboxElement.netOnly()) {
					if(!toolboxElement.getDefinition().equals("")){
						def=" (" + toolboxElement.getDefinition() + ")";
					}
					%>
						<a target="newwin" href="<%= toolboxElement.getURL() %>"><%= toolboxElement.getDescription() %></a><%= def %><br>
					<%
				}else{
							
					%>
					<script language='javascript'>
					var isNav = (navigator.appName.indexOf("Netscape") != -1);
					<%
					if (toolboxElement.netOnly()) {
					%>
					if (isNav){
					<%
					}else if (toolboxElement.ieOnly()) {
					%>
					if (!isNav){
					<%
					}
					if(toolboxElement.getDefinition().length() > 0){
						def=" (" + toolboxElement.getDefinition() + ")";
					}
					%>
					document.write("<a target=newwin href=<%= toolboxElement.getURL() %> target=docs><%= toolboxElement.getDescription() %></a><%= def %><br>");
					}
					</script>
					<%
				}
			}

	if(i%2==0){
		%>
		</td>
	  		<td width="10%"></td>
		<%
	}else{
		%>
		</td>
	  	</tr>
		<%
	}

}
%>

  	</table>
   </td>
  </tr>

</table>

<p>&nbsp;</p>

  </body>
</html>
