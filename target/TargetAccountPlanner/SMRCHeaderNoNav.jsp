<%@page contentType="text/html"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.SMRCHeaderBean"%>

<head><title>Target Account Planner</title>
<%
SMRCHeaderBean header = (SMRCHeaderBean)request.getAttribute("header");
String sImagesURL = (String)header.getImagesURL();
String cssURL = (String)header.getCssURL();
String jsURL = (String)header.getJsURL();
%>

<link rel=stylesheet type="text/css" href="<%= cssURL %>style_2_1.css" />
<link rel=stylesheet type="text/css" href="<%= cssURL %>oem-styles.css" />
<script language="javascript" src="<%= jsURL %>prototypeScript.js"></script>
<script language="javascript" src="<%= jsURL %>scripts.js"></script>
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">



<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr bgcolor="#71674A">
	<td width="99%"></td><td><IMG src="<%= sImagesURL %>header/popup_header.gif" width="125" height="23" alt="" border="0"></td>
  </tr>
</table>


