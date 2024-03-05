<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import="com.eaton.electrical.smrc.util.StringManipulation" %>
<%
String vcn = StringManipulation.noNull((String)request.getAttribute("vcn"));
if(vcn.equals("")) {
 vcn = "unknown";
}


 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>This Segment is Pending a Change</title>
 
  </head>
  
  <body>
    <%@ include file="./SMRCHeaderNoNav.jsp" %> 
    <p>
    Vista Customer Number <%=vcn%> is currently pending an approval for a category change and can not be modified further until that process is complete.
    <img src="<%= sImagesURL %>button_cancel.gif" width="70" height="20" onClick='self.close()' onmouseover="this.style.cursor='hand';" onmouseout="this.style.cursor='default';">
    
  </body>
</html>
