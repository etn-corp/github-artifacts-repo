<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.Globals"%>
<%@ page import="com.eaton.electrical.smrc.util.StringManipulation" %>
<%@ include file="analytics.jsp" %>
<html>
<%@ include file="./SMRCHeaderNoNav.jsp" %> 
<%
    String searchString = (String) request.getAttribute("searchString");
 	ArrayList seResults = (ArrayList) request.getAttribute("seResults");
 	boolean refreshCaller = ((Boolean) request.getAttribute("refreshCaller")).booleanValue();
 	boolean bidmanReports = ((Boolean) request.getAttribute("bidmanReports")).booleanValue();
 	String callPage = "";
 	String searchParams = "";
 	if (bidmanReports){
 	   callPage = "&callPage=bidmanReports";
 	   searchParams = "&searchString=" + searchString + "&submittingSearch=true";
 	}
 	
	String recNum = request.getParameter("recNum");
	
	if(recNum==null){
		recNum="1";
	}			
	int nextRecNum=Globals.a2int(recNum)+50;
	int prevRecNum=Globals.a2int(recNum)-50;
	
	StringBuffer pageUrl = new StringBuffer();
	
	// Get tmId and type to pass on
	
	String se = (String)request.getParameter("se");
	
	if (se != null) {
	
		pageUrl.append("&se=" + se);
		
	}
	
	String seid = (String)request.getParameter("seid");
	
	if ((seid != null) && (seid.length() > 0)) {
	
		pageUrl.append("&seid=" + seid);
		
	}
	
	if ((searchString != null) && (searchString.length() > 0)) {
	
		pageUrl.append("&searchString=" + searchString);
		
	}
 	
 	pageUrl.append("&page=SEBrowse&submittingSearch=true");
 	 			 			
%>

<% if (refreshCaller){ %>
     <SCRIPT>
     window.opener.document.bidmanreportform.refresh.value="y";
     window.opener.document.bidmanreportform.submit();
    </SCRIPT>
<% }  %>

<SCRIPT>
	function checkSearchString(){
          var searchString = document.searchForm.searchString.value;
          if (searchString == "*" || searchString == "%" || searchString == ""){
              alert("Please enter at least one letter.");
              return false;
          } else {
              document.searchForm.submit();
          }
	}
</SCRIPT>

<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
  <td width="600" valign="top"> 
      <p>&nbsp;</p>
      <p class="heading2">Sales Engineer Browse</p><br>
	   <table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="15%"><div align="right">Enter Sales Engineer's Last Name:</div></td>
		<td width="3%">&nbsp;</td>
                <form name="searchForm" method="POST" action="SEBrowse?<%= callPage %>" onSubmit="return checkSearchString()">
                    <input type="hidden" name="page" value="SEBrowse">
                    <input type="hidden" name="submittingSearch" value="true">  
                    <td width="22%"><input type="text" name="searchString" value="<%= searchString %>"></td>
                    <td width="40%"><input type="image" src="<%= sImagesURL %>button_search.gif" width="70" height="20"></td>
                <input type="hidden" name="se" value="<%= StringManipulation.noNull(request.getParameter("se")) %>">
                </form>
		</td>
		<td align=center width="20%"><a href="javascript:close()">Close Window</a>
		</td>
        </tr>
</table>
	<br><hr align="left" width="600" size="1" color="#999999" noshade>
      <br>
	  <table width="80%" border="0" cellspacing="1" cellpadding="0">
		<%
			if (seResults.size() > 0) {					
		%>
		  	<tr><td>&nbsp;</td></tr>
		  	<tr>
		  		<td colspan = "5">
					<%
							if(!recNum.equals("1")){
							%>
							<a href="SEBrowse?page=search&recNum=<%= prevRecNum %>&link=paginate<%= pageUrl %>">&lt;&lt; Prev 50</a> | 
							<% }else{ %>
							<font class="textgray">&lt;&lt; Prev 50 | </font>
							<% } %>
							<% if(seResults.size()==51){ %>
							<a href="SEBrowse?page=search&recNum=<%= nextRecNum %>&link=paginate<%= pageUrl %>">Next 50  &gt;&gt;</a>
							<% } else { %>
							<font class="textgray">Next 50 &gt;&gt;</font>
							<% }  
					%>
		  		</td>
		  	</tr>
		  	<tr><td>&nbsp;</td></tr>
	      	<tr>
	      		<th><div align="left">Sales Engineer</div></th>
	      		<th><div align="left">Salesman ID</div></th>
	      		<th><div align="left">Sales Office</div></th>
	      		<th><div align="left">Geography</div></th>
		  	</tr>
					      	
		<% } // end if acounts.size() > 0 %>      		  
<%
   
   // We need to build in an offset.  Since the initail load comes in with a single value
   // of the existing selected SE and the searched list comes in with 51 records (to see if
   // the 'Next' link should be displayed), we need to build an offset for the counter.  If 
   // there is 51 records, then delete the last one (this will be put on the next page).
   
   int offset = 0;

   if (seResults.size() == 51) {
	   
	   offset = 1;
	   
   }

   if (seResults.size() > 0){
       for (int i=0; i < (seResults.size() - offset); i++){
            Salesman salesman = (Salesman) seResults.get(i);
            if ((i%2) == 0){
    %>
            <tr class="cellShade">
    <%      } else {
    %>
            <tr>
    <%      }
            if (bidmanReports){
    %>
    			<td><a href="SEBrowse?page=saveSE&seId=<%= salesman.getSalesId() %>&se=<%= request.getParameter("se") %>&seGeog=<%= salesman.getGeogCd() %><%= searchParams %><%= callPage %>"><%= salesman.getLastName() %>,  <%= salesman.getFirstName() %></a></td>
    <%      } else {     %>
        		<td><a href="SEBrowse?page=saveSE&seId=<%= salesman.getSalesId() %>&se=<%= request.getParameter("se") %>&seGeog=<%= salesman.getGeogCd() %>"><%= salesman.getLastName() %>,  <%= salesman.getFirstName() %></a></td>
    <%      } %>
                    <td><%= salesman.getSalesId() %></td>
                    <td><%= salesman.getSalesOffice() %></td>
                    <td><%= salesman.getGeogCd() %> -  <%= salesman.getDistrictName() %></td>
              </tr>

    <%
        }
    } else if(!searchString.equals("")){
%>
    <tr>
           <td>No sales engineers found beginning with <%= searchString %></td>
    </tr>
<%  } %>

	  <table width="80%" border="0" cellspacing="1" cellpadding="0">
		<%
			if (seResults.size() > 0) {					
		%>
		  	<tr><td>&nbsp;</td></tr>
		  	<tr>
		  		<td colspan = "5">
					<%
							if(!recNum.equals("1")){
							%>
							<a href="SEBrowse?page=search&recNum=<%= prevRecNum %>&link=paginate<%= pageUrl %>">&lt;&lt; Prev 50</a> | 
							<% }else{ %>
							<font class="textgray">&lt;&lt; Prev 50 | </font>
							<% } %>
							<% if(seResults.size()==51){ %>
							<a href="SEBrowse?page=search&recNum=<%= nextRecNum %>&link=paginate<%= pageUrl %>">Next 50  &gt;&gt;</a>
							<% } else { %>
							<font class="textgray">Next 50 &gt;&gt;</font>
							<% }  
					%>
		  		</td>
		  	</tr>
		  	<tr><td>&nbsp;</td></tr>
		<% } // end if acounts.size() > 0 %> 
      	      	 
  </table><br>

  <p>&nbsp;</p>
  </td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>
