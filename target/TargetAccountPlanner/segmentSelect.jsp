<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.dao.SegmentsDAO"%>
<%@ page import="com.eaton.electrical.smrc.util.StringManipulation"%>
<%@ page import="com.eaton.electrical.smrc.util.Globals"%>
<html>
<%@ include file="./SMRCHeaderNoNav.jsp" %> 
<%@ include file="analytics.jsp" %>
<%

/*
This JSP will need a fair amount of cleanup.

It should be changed to be flexible for n levels of segments
*/

ArrayList segments = (ArrayList)request.getAttribute("segments");

String modify = (String)request.getAttribute("isModify");
String vcn = (String)request.getAttribute("acctId");           

boolean isModify = false;
if(modify!=null && modify.equals("Y")){
	isModify = true;
}
String lock = (String)request.getAttribute("lock");
boolean lockSegments = false;
if(lock!=null && lock.equals("true")){
	lockSegments=true;
}

%>
<script language="Javascript">

window.opener.document.theform.isModify.value = '<%=modify%>';

function checkSegments(){
	var whichitem = 0;
	var numberOfSegmentsSelected=0;
  while (whichitem < document.segmentForm.SEGMENTS.length) {
    if (document.segmentForm.SEGMENTS[whichitem].checked) {
    	numberOfSegmentsSelected++;
    	
    }
    whichitem++;
  }
  if(numberOfSegmentsSelected<2){
  	alert('You must select down to a secondary segment.\n\nPlease make sure you have selected a primary and secondary segment.');
  	return false;
  }

}

function modify() {
	document.forms[0].action = 'ModifySegment';
	document.forms[0].submit();
}

</script>


<form name="segmentForm" action="SegmentSelect" onsubmit="return checkSegments()">
<input type='hidden' name='isModify' value=<%=modify%>>
<input type='hidden' name='acctId' value=<%=vcn%>>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
  	<td width="750" valign="top"> 
      <p>&nbsp;</p>
      <p class="heading2">Category Browse</p>
      <p class="heading3">You must select down to a secondary category.</p>
     	<%
     		if(lockSegments){
			%>
      	<span class="crumbcurrent">You cannot modify the Primary or Secondary Categories because this account is in Vista.<br><br></span>
      <% } %>
	   	<table width="80%" border="0" cellspacing="0" cellpadding="0">
			<tr> 
				<td width="100%">
				<ul>
				<b>Primary Categories</b>
				<%
				for(int i=0;i<segments.size();i++){
					Segment segment = (Segment)segments.get(i);
					if(session.getAttribute("segment1")!=null){
						Segment segment1 = (Segment)session.getAttribute("segment1");
						if(segment.getSegmentId()==segment1.getSegmentId()){
							if(lockSegments){
							%>
							<li><input type="checkbox" name="SEGMENTS" value="<%= segment.getSegmentId() %>" disabled checked><input type="hidden" name="SEGMENTS" value="<%= segment.getSegmentId() %>&isModify=<%=modify%>&acctId=<%=vcn%>">&nbsp;<%= segment.getName() %><br>
							<%							
							}else{
							%>
							<li><input type="checkbox" name="SEGMENTS" value="<%= segment.getSegmentId() %>" onclick="javascript: location.href='SegmentSelect?segment=<%= segment.getSegmentId() %>&isModify=<%=modify%>&acctId=<%=vcn%>'" checked><!--<input type="hidden" name="SEGMENTS" value="<%= segment.getSegmentId() %>">-->&nbsp;<a href="SegmentSelect?segment=<%= segment.getSegmentId() %>"><%= segment.getName() %></a><br>
							<%
							}
							ArrayList subSegments = segment.getSubSegments();
							out.println("<ul><b>Secondary Categories</b>");
							for(int j=0;j<subSegments.size();j++){
								Segment subSegment = (Segment)subSegments.get(j);
								if(session.getAttribute("segment2")!=null){
									Segment segment2 = (Segment)session.getAttribute("segment2");
									if(subSegment.getSegmentId()==segment2.getSegmentId()){
										if(lockSegments){
										%>
										<li><input type="checkbox" name="SEGMENTS" value="<%= subSegment.getSegmentId() %>" disabled checked><input type="hidden" name="SEGMENTS" value="<%= subSegment.getSegmentId() %>&isModify=<%=modify%>&acctId=<%=vcn%>">&nbsp;<%= subSegment.getName() %><br>					
										<%										
										}else{
										%>
										<li><input type="checkbox" name="SEGMENTS" value="<%= subSegment.getSegmentId() %>" onclick="javascript: location.href='SegmentSelect?segment=<%= subSegment.getSegmentId() %>&isModify=<%=modify%>&acctId=<%=vcn%>'" checked><!--<input type="hidden" name="SEGMENTS" value="<%= subSegment.getSegmentId() %>">-->&nbsp;<a href="SegmentSelect?segment=<%= subSegment.getSegmentId() %>"><%= subSegment.getName() %></a><br>					
										<%
										}
										ArrayList tertiarySegments = subSegment.getSubSegments();

										if(tertiarySegments.size()==0){
										%>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;No Sub-Segments<br>					
										<%
										}
										for(int k=0;k<tertiarySegments.size();k++){
											Segment tertiarySegment = (Segment)tertiarySegments.get(k);
										%>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="SEGMENTS" value="<%= tertiarySegment.getSegmentId() %>">&nbsp;<%= tertiarySegment.getName() %><br>					
										<%
										
													ArrayList quadriarySegments = tertiarySegment.getSubSegments();
													for(int q=0;q<quadriarySegments.size();q++){
														Segment quadriarySegment = (Segment)quadriarySegments.get(q);
													%>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="SEGMENTS" value="<%= quadriarySegment.getSegmentId() %>">&nbsp;<%= quadriarySegment.getName() %><br>					
													<%

													}
										}
										
									}else{
										if(lockSegments){
										%>
										<li><input type="checkbox" name="SEGMENTS" value="<%= subSegment.getSegmentId() %>" disabled>&nbsp;<%= subSegment.getName() %><br>					
										<%										
										}else{
										%>
										<li><input type="checkbox" name="SEGMENTS" value="<%= subSegment.getSegmentId() %>" onclick="javascript: location.href='SegmentSelect?segment=<%= subSegment.getSegmentId() %>&isModify=<%=modify%>&acctId=<%=vcn%>'">&nbsp;<a href="SegmentSelect?segment=<%= subSegment.getSegmentId() %>"><%= subSegment.getName() %></a><br>					
										<%
										}
									}
								}else{
									if(lockSegments){								
									%>
									<li><input type="checkbox" name="SEGMENTS" value="<%= subSegment.getSegmentId() %>" disabled>&nbsp;<%= subSegment.getName() %><br>					
									<%									
									}else{
									%>
									<li><input type="checkbox" name="SEGMENTS" value="<%= subSegment.getSegmentId() %>" onclick="javascript: location.href='SegmentSelect?segment=<%= subSegment.getSegmentId() %>&isModify=<%=modify%>&acctId=<%=vcn%>'">&nbsp;<a href="SegmentSelect?segment=<%= subSegment.getSegmentId() %>&isModify=<%=modify%>&acctId=<%=vcn%>"><%= subSegment.getName() %></a><br>					
									<%
									}
								}
							}
							out.println("</ul>");
						}else{
							if(lockSegments){
							%>
							<li><input type="checkbox" name="SEGMENTS" value="<%= segment.getSegmentId() %>" disabled>&nbsp;<%= segment.getName() %><br>
							<%						
							}else{
							%>
							<li><input type="checkbox" name="SEGMENTS" value="<%= segment.getSegmentId() %>" onclick="javascript: location.href='SegmentSelect?segment=<%= segment.getSegmentId() %>&isModify=<%=modify%>&acctId=<%=vcn%>'">&nbsp;<a href="SegmentSelect?segment=<%= segment.getSegmentId() %>&isModify=<%=modify%>&acctId=<%=vcn%>"><%= segment.getName() %></a><br>
							<%
							}
						}
					}else{
						if(lockSegments){	
						%>
						<li><input type="checkbox" name="SEGMENTS" value="<%= segment.getSegmentId() %>" disabled>&nbsp;<%= segment.getName() %><br>
						<%						
						}else{				
						%>
						<li><input type="checkbox" name="SEGMENTS" value="<%= segment.getSegmentId() %>" onclick="javascript: location.href='SegmentSelect?segment=<%= segment.getSegmentId() %>&isModify=<%=modify%>&acctId=<%=vcn%>'">&nbsp;<a href="SegmentSelect?segment=<%= segment.getSegmentId() %>&isModify=<%=modify%>&acctId=<%=vcn%>"><%= segment.getName() %></a><br>
						<%
						}		
					}
				}
				
				%>
				</ul>
				</td>
			</tr>	
			<tr>
			
				<td>
					<br><br>
					<% if(isModify) { %>
						<img src="<%= sImagesURL %>button_save.gif" width="70" height="20" onClick='modify()' onmouseover="this.style.cursor='hand';" onmouseout="this.style.cursor='default';">
						<img src="<%= sImagesURL %>button_cancel.gif" width="70" height="20" onClick='self.close()' onmouseover="this.style.cursor='hand';" onmouseout="this.style.cursor='default';">
					<% } else { %>
						<input type="image" src="<%= sImagesURL %>button_save.gif" width="70" height="20">
					<% } %>
				</td>
			
			</tr>
			</table>
		</td>
	</tr>
</table>
<input type="hidden" name="page" value="save">
<input type="hidden" name="acctId" value="<%= request.getParameter("acctId") %>">
</form>
<p>&nbsp;</p>
</body>
</html>
