<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ include file="analytics.jsp" %>
<html>
<%@ include file="./SMRCHeaderNoNav.jsp" %> 
<%
    String searchString = (String)request.getAttribute("searchString");
    ArrayList zipResults = (ArrayList)request.getAttribute("zipResults");
%>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
  <td width="750" valign="top"> 
      <p>&nbsp;</p>
      <p class="heading2">Zip Code Browse</p><br>
	   <table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="15%"><div align="right">Enter Zip Code:</div></td>
		<td width="3%">&nbsp;</td>
                <form name="searchForm" action="ZipcodeBrowse">
                    <input type="hidden" name="page" value="search">
                    <input type="hidden" name="submittingSearch" value="true">  
                    <input type="hidden" name="addr" value="<%= request.getParameter("addr") %>">  
                    <td width="22%"><input type="text" name="searchString" value="<%= searchString %>"></td>
                    <td width="60%"><input type="image" src="<%= sImagesURL %>button_search.gif" width="70" height="20"></td>
                </form>
		</td>
        </tr>
</table>
	<br><hr align="left" width="600" size="1" color="#999999" noshade>
      <br>
	  <table width="50%" border="0" cellspacing="1" cellpadding="0">
      	<tr>
      		<th><div align="left">Zip Code</div></th>
      		<th><div align="left">City</div></th>
      		<th><div align="left">State</div></th>
      	</tr>
				<%
				String shade = null;
				for(int i=0;i<zipResults.size();i++){

					Address addr = (Address)zipResults.get(i);
					if(i%2==0){
						shade="";
				}else{
						shade=" class=\"cellShade\"";
					}
				%>
            <tr<%= shade %>>
                    <td><a href="ZipcodeBrowse?page=save&zip=<%= addr.getZip() %>&city=<%= addr.getCity() %>&state=<%= addr.getState() %>&addr=<%= request.getParameter("addr") %>"><%= addr.getZip() %></a></td>
                    <td><%= addr.getCity() %></td>
                    <td><%= addr.getState() %></td>
                    <td></td>
              </tr>
			<% } 
			
			if(zipResults.size()==0 && !searchString.equalsIgnoreCase("")){
			%>
				<tr>
           <td>&nbsp;</td>
   			</tr>
			  <tr>
           <td>No zip codes found with <%= searchString %></td>
   			</tr>
			<%
			}
			%>


      	      	 
  </table><br>

  <p>&nbsp;</p>
  </td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>
