<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

	User user = (User)request.getAttribute("user");
	Customer customer = (Customer)request.getAttribute("customer");

	String acctId=(String)request.getAttribute("acctId");
%>

<HTML>
<%@ include file="./SMRCHeader.jsp" %>

	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<TR>
			<TD width="10">&nbsp;</TD>
			<TD width="750">
				<p><br>
					<a class="crumb" href="SMRCHome">Home Page</a>
					<span class="crumbarrow">&gt;</span><a class="crumb" href="ProductSampleRequest?acctId=<%= acctId %>">Product Sample Request</a>
					<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Product Sample Request Confirmation</span></p>
			</TD>
		</TR>
	</table>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<TR>
			<TD width="10">&nbsp;</TD>
			<TD width="750">
				<BR>
				Your request has been processed.  <BR><BR>
				You should receive a copy of the email that has been sent to the ePOD group.<BR><BR>
				If you do not receive this email within the next hour, please notify this site's administrator at <A HREF="mailto:oemacctplanner@eaton.com">oemacctplanner@eaton.com</A>
			</TD>
		</TR>
	</table>
		
</BODY>
</HTML>