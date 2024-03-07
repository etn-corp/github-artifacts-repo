<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.util.*" %>
<html>
	<%@ include file="/SMRCHeader.jsp" %>
	<%@ include file="analytics.jsp" %>
	<%

		ArrayList headerList = (ArrayList)request.getAttribute("headerList");
		ArrayList returnList = (ArrayList)request.getAttribute("returnList");
		

	
	     int beginWith = ((Integer) request.getAttribute("beginWith")).intValue();
	     int endWith = ((Integer) request.getAttribute("endWith")).intValue();
	     boolean lastPage = ((Boolean) request.getAttribute("lastPage")).booleanValue();
	     String queryString = request.getQueryString();
	//   We don't want any sort or prev/next information here
	     int pageLoc = -1;
	     if (queryString.indexOf("page") > 0){
	         pageLoc = queryString.indexOf("page");
	     }
	     StringBuffer newQueryString = new StringBuffer(queryString);
	     if (pageLoc >= 0){
	        newQueryString.delete(0, pageLoc);
	     }
	//   We want the sorting information here, but not the prev/next information
	     StringBuffer newPrevNextQueryString = new StringBuffer(queryString);
	     int prevLoc = -1;
	     int nextLoc = -1;
	     if (queryString.indexOf("prevPage") >= 0){
	        prevLoc = queryString.indexOf("prev");
	     }
	     if (queryString.indexOf("nextPage") >= 0){
	        nextLoc = queryString.indexOf("next");
	     }
	     if (prevLoc >= 0){
	        newPrevNextQueryString.delete(0, prevLoc + 11);
	     }
	     if (nextLoc >= 0){
	        newPrevNextQueryString.delete(0, nextLoc + 11);
	     }
	%>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750">
				<p><br>
					<a class="crumb" href="SMRCHome">Home Page</a><span class="crumbarrow">&gt;</span>
					<a class="crumb" href="CustomerMarketShareReport?page=sr">Customer Market Share Report</a>
					<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Customer Market Share Results</span>
				</p>
			</td>
		</tr>
	</table>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750" valign="top">
				<p>&nbsp;</p>
				<!-- Main Text	-->
				<p class="heading2">Customer Market Share Report Results</p>
				<br><center>
					<p align="center">
						<%
						if (beginWith > 0){
						%> <a href="CustomerMarketShareReport?beginWith=<%= beginWith %>&endWith=<%=endWith%>&prevPage=Y&<%= newPrevNextQueryString.toString() %>">&lt;&lt;Prev 50</a> |
						<%  } else { %>
						<font class="textgray">&lt;&lt;Prev 50 | </font>
						<% } %>
						<%  if (!lastPage){
						%> <a href="CustomerMarketShareReport?beginWith=<%= beginWith %>&endWith=<%=endWith%>&nextPage=Y&<%= newPrevNextQueryString.toString() %>">Next 50&gt;&gt;</a>
						<%  } else { %>
						<font class="textgray">Next 50&gt;&gt;</font>
						<% } %>
					</p>
					
					<table class=tableBorder width="100%" align=left border=0 cellpadding=2 cellspacing=1>

						<tr bgcolor="#F8F8FF">
<%
						// Output the headers
						
						String sortOrder = (String)request.getAttribute("sortOrder");
						
						if (sortOrder == null) {
							
							sortOrder = "";
							
						} else {
							
							sortOrder = "&sortOrder=desc";
							
						}
						for (int i = 0; i < headerList.size(); i ++) {
							
							%>
								<td align="center" nowrap><%=(String)headerList.get(i)%></td>
							
							<% 
														
						} // end iterate through headerList
%>

						</tr>
<%
						// Output the data fields
						
						ArrayList recordList = null;

						for (int i = 0; i < returnList.size(); i ++ ) {

%>
							<tr bgcolor="#F8F8FF">
<%
							
							recordList = (ArrayList)returnList.get(i);
							
							for (int j = 0; j < recordList.size(); j++ ) {
								
%>
								<td align = "right"><%=(String)recordList.get(j) %></td>
<%							
								
							} // end iterate through recordList

%>
							</tr>
<%
							
							
						} // end iterate throuh returnList
%>						
						
						
						
					</table>					

					<p align="center">
						<%
						if (beginWith > 0){
						%> <a href="CustomerMarketShareReport?beginWith=<%= beginWith %>&endWith=<%=endWith%>&prevPage=Y&<%= newPrevNextQueryString.toString() %>">&lt;&lt;Prev 50</a> |
						<%  } else { %>
						<font class="textgray">&lt;&lt;Prev 50 | </font>
						<% } %>
						<%  if (!lastPage){
						%> <a href="CustomerMarketShareReport?beginWith=<%= beginWith %>&endWith=<%=endWith%>&nextPage=Y&<%= newPrevNextQueryString.toString() %>">Next 50&gt;&gt;</a>
						<%  } else { %>
						<font class="textgray">Next 50&gt;&gt;</font>
						<% } %>
					</p>
				</center>
			</td>
		</tr>
	</table>
	<br><br><br>
		</body>
</html>