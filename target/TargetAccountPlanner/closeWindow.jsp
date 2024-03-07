<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.util.StringManipulation" %>
<%
String fwdPage = StringManipulation.noNull((String)request.getAttribute("fwdPage"));
if(fwdPage.equals("")){
	fwdPage="refresh";
}
%>
<html>
  <head>
  <script language="javascript">
   
  window.opener.document.theform.page.value="<%= fwdPage %>";
  window.opener.document.theform.submit();
  self.close();
  </script>

    <title></title>
  </head>
  
  <body>

  </body>
</html>
