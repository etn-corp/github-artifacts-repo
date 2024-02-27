<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*, java.text.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>

<html>
<%
	String requestItHref = (String) request.getAttribute("requestItHref");
%>
<SCRIPT LANGUAGE="JavaScript"><!-- Hide from non-JavaScript browsers

function redirect () {
	setTimeout("go_now()",2000); 
}
			
function go_now () {
    var theUrl = "<%= requestItHref %>";
	window.location.href = theUrl;
}
//--></SCRIPT>

<body onLoad="redirect()">
<%@ include file="./SMRCHeaderNoNav.jsp" %>	
	<p class="heading2"> Connecting to RequestIt.... &nbsp; <img src=images/progress.gif border=0> 
	
</body>
</html>

