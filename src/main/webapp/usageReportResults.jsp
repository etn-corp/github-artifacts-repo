<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.dao.*" %>
<%@ page import="com.eaton.electrical.smrc.util.*" %>
<%@ page import="java.text.*" %>
<html>
<%@ include file="./SMRCHeader.jsp" %> 
<%@ include file="analytics.jsp" %>

<%
        ArrayList reportBeans = (ArrayList) request.getAttribute("reportBeans");
        String filterString = (String) request.getAttribute("filterString");
        String groupBy = (String) request.getAttribute("groupBy");
        String sortQueryString = (String) request.getAttribute("sortQueryString");
        String sortDir = (String) request.getAttribute("sortDir");
        String noResultMessage = (String) request.getAttribute("noResultMessage");
        if (sortDir != null && sortDir.equalsIgnoreCase("asc")){
            sortDir = "desc";
        } else {
            sortDir = "asc";
        }
        int beginWith = ((Integer) request.getAttribute("beginWith")).intValue();
        int endWith = ((Integer) request.getAttribute("endWith")).intValue();
        boolean lastPage = ((Boolean) request.getAttribute("lastPage")).booleanValue();
        String paginateQueryString = (String) request.getAttribute("paginateQueryString");

%>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="seHome.html">Home Page</a>
		<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Usage Report</span>
      </p> 
    </td>
  </tr>
</table>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
   <td width="400" valign="top"> 
      <p>&nbsp;</p>
      <p class="heading2">Usage Report Report</p>

	  <table width="100%" border="0" cellspacing="1" cellpadding="0">
	  <tr>
	  	<td colspan="2"><b>Filters applied:</b> <%= filterString %></td>
	  </tr>
	  <tr>
	  	<td colspan="2">&nbsp;</td>
	  </tr>
	  <tr>
	  	<td colspan="2">
		  	<p align="center">
				<%
				if (beginWith > 0){
				%> <a href="UsageReportResults?beginWith=<%= beginWith %>&endWith=<%=endWith%>&prevPage=Y&<%= paginateQueryString %>">&lt;&lt;Prev 50</a> |
				<%  } else { %>
				<font class="textgray">&lt;&lt;Prev 50 | </font>
				<% } %>
				<%  if (!lastPage){
				%> <a href="UsageReportResults?beginWith=<%= beginWith %>&endWith=<%=endWith%>&nextPage=Y&<%= paginateQueryString %>">Next 50&gt;&gt;</a>
				<%  } else { %>
				<font class="textgray">Next 50&gt;&gt;</font>
				<% } %>
			</p>
	  	</td>
	  </tr>
	  <tr>
                        <th><a href="UsageReportResults?<%= sortQueryString %>&sortBy=description&sortDir=<%= sortDir %>" class="tableLink"><%= groupBy %></a></th>
                        <th><a href="UsageReportResults?<%= sortQueryString %>&sortBy=pageViews&sortDir=<%= sortDir %>" class="tableLink">Page Views</a></th>
	  		
	  </tr>
<%             if (reportBeans.size() > 0){
                    for (int i=0; i < reportBeans.size(); i++){
                    UsageReportBean bean = (UsageReportBean) reportBeans.get(i);
                    String cellShade = "";
                    if(i%2==0){
                            cellShade=" class=\"cellShade\"";
                    }else{
                            cellShade="";
                    }
%>
            <tr<%= cellShade %>>
            <td>
	  			<div align="left"><%= bean.getDescription() %></div>
	  		</td>
	  		<td>
	  			<div align="right"><%= bean.getPageViews() %></div>
  		 </td>
	  		
  		</tr>
  		
<%              } %>
		</table>
<%		
		} else {
%>
		</table><br>
		<b> <%= noResultMessage %> </b>
<%		}  %>

	  	
	
   </td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>