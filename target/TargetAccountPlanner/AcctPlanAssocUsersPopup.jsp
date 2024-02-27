<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.dao.*" %>
<%@ page import="com.eaton.electrical.smrc.*" %>
<html>
<%@ include file="./SMRCHeader.jsp" %>
<%@ include file="analytics.jsp" %>
 
<script language="javascript" src="<%= jsURL %>validation/standardReportChart.js"></script>
<script>
function loadingMessage(){
    setInnerHTML("loadingMessage", "<center><b><font size=4 color=\"red\">Please wait while message is sent...</font></b></center>");
}
</script>
<%
//        SalesGroup sGroup = header.getSalesGroup();  
        User user = header.getUser();
        ArrayList parmNameList = (ArrayList) request.getAttribute("parmNameList");
        ArrayList parmVals = (ArrayList) request.getAttribute("parmVals");
        String message = null;
        if (request.getAttribute("message") != null){
            message = (String) request.getAttribute("message");
        }

%>
<%
if (message != null){
%>
    <div class=heading3><%= message %>
    </div>
    
<%
}

%>
<div id="loadingMessage">&nbsp;</div>
<div class=centerOnly>
<form action=AcctPlanAssocUsersPopup>
<input type=hidden name=userid value="<%= user.getUserid() %>">
<input type=hidden name=page value=sendEmail>
<%
                int arrayListIndex = 0;
                for (int p=0; p < parmNameList.size(); p++) {
			String name = (String) parmNameList.get(p);
			if ((parmVals.get(arrayListIndex)) != null){
                                String [] vals = (String []) parmVals.get(arrayListIndex);
                                for (int i=0; i < vals.length; i++) {

%>
					<input type=hidden name="<%= name %>" value="<%= vals[i] %>">
<%                              }
                        }
			arrayListIndex++;
		}
%>
		<table class=tableBorder cellspacing=1 cellpadding=1 border=0>
		<caption class=heading3>Enter the text of your message below.</caption>
                    <tr class=cellColor>
                        <td class=smallFontL>Subject: </td>
                        <td class=smallFontL><input name=subject class=smallFontL size=60></td>
                    </tr>
                    <tr class=cellColor>
			<td colspan=2><textarea class=smallFontL name=message rows=10 cols=50></textarea></td>
                    </tr>
		</table>
		<input class=smallFontC type=submit name=submit value='Send Message' onClick="loadingMessage();">
</form>
</div>

</body>
</html>
