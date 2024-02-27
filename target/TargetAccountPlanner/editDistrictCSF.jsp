<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ include file="analytics.jsp" %>

<%
CriticalSuccessFactor csf = (CriticalSuccessFactor)request.getAttribute("csf");
Geography geog = (Geography)request.getAttribute("geog");
String saveMsg = (String)request.getAttribute("saveMsg");
String from = "district";
if (request.getAttribute("from") != null){
    from = (String) request.getAttribute("from");
}
String divId = "";
if (request.getAttribute("divId") != null){
    divId = (String) request.getAttribute("divId");
}

String color = null;


%>
<html>
<%@ include file="./SMRCHeader.jsp" %>
<%
if(csf.getColor().equals("Green")){
	color="Status: <img src='" + sImagesURL + "green_status.gif' border='0' width='10' height='10'>";
}else if(csf.getColor().equals("Yellow")){
	color="Status: <img src='" + sImagesURL + "yellow_status.gif' border='0' width='10' height='10'>";
}else if(csf.getColor().equals("Red")){
	color="Status: <img src='" + sImagesURL + "red_status.gif' border='0' width='10' height='10'>";
}else{
	 	color="";
}
%>


<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>
		<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Critical Success Factors</span>
      </p> 
    </td>
  </tr>
</table>
<form method="POST" action="CSF">
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
   <td width="750" valign="top"> 
      <p>&nbsp;</p>
			<% if(saveMsg.length()!=0){ %>
						<blockquote><font class="crumbcurrent"><%= saveMsg %></font></blockquote>
			<% } 
               if (from.equalsIgnoreCase("division")) {%>
      <a href="CSF?page=Division&divisionId=<%= divId %>">&lt;&lt; Back to Division Critical Success Factors</a>
      <input type=hidden name="divId" value=<%= divId %>>
      <input type=hidden name="from" value=<%= from %>>
            <%   } else { %>
      <a href="CSF?page=District&district=<%= request.getParameter("district") %>">&lt;&lt; Back to District Critical Success Factors</a>
            <%   } %>
      <p class="heading2"><%= geog.getDescription() %> Critical Success Factor</p>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		</tr>
			<tr>
			<td>
			&nbsp;
			</td>
		</tr>
		<tr>
			<td class="heading3"><%= csf.getName() %>
			</td>
		</tr>
	<% if(!color.equals("")){	%>		
			<tr><td><%= color %></td></tr>
	<% } %>
			<tr>
			<td><br>
			<%
			ArrayList notes = csf.getMultipleNotes();
			if(notes.size()==0){
			%>
			There are no notes yet<br>
			<br>
			<hr align="left" width="600" size="1" color="#999999" noshade>
			<br>
			<%
			}
			
			int noteCounter = 1;
			for(int i=0; i<notes.size(); i++){
				CriticalSuccessFactorNote note = (CriticalSuccessFactorNote)notes.get(i);
				if (!note.getNote().equals("")){
					%>
					<font class="tableTitle"><%= noteCounter++ %>.</font> <%= note.getNote() %><br>
					<br>
					<font size="1">added by <%= (note.getUserAddedName().equals("")?"unknown":note.getUserAddedName()) %> on <%= note.getDateAdded() %></font><br>
					<!--<font size="1"><a href="CSF?page=noteStatus&csfnoteid=<%= note.getId() %>&status=N&district=<%= request.getParameter("district") %>&csfid=<%= request.getParameter("csfid") %>">click to deactivate this note</a></font><br>-->
					<hr align="left" width="600" size="1" color="#999999" noshade>
					<br>
			<% 
				}
			} 
			%>
			</td>
		</tr>
			<tr>
			<td>
			&nbsp;
			</td>
		</tr>
			<tr>
			<td>
			Add new note:
			</td>
		</tr>
			<tr>
			<td>
			<textarea name="NOTE" cols="50" rows="4" id="csf1"></textarea>
			</td>
		</tr>						
	</table>
	<br>

   </td>
  </tr>
</table>

  <table width="100%" border="0" cellspacing="10" cellpadding="0">
		<tr>
			<td><input type="image" src="<%= sImagesURL %>button_save.gif" width="70" height="20"></td>

		</tr>
	</table>
<input type="hidden" name="csfid" value="<%= csf.getId() %>">
<input type="hidden" name="district" value="<%= request.getParameter("district") %>">
<input type="hidden" name="page" value="saveNote">
</form>
  </body>
</html>
