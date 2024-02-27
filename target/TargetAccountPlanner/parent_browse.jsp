<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.util.Globals"%>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<html>
<%@ include file="./SMRCHeaderNoNav.jsp" %> 
<%@ include file="analytics.jsp" %>
<%
    String searchString = (String) request.getAttribute("searchString");
    ArrayList customerResults = (ArrayList) request.getAttribute("customerResults");
    
	String recNum = request.getParameter("recNum");
	
	if(recNum==null){
		recNum="1";
	}			
	int nextRecNum=Globals.a2int(recNum)+50;
	int prevRecNum=Globals.a2int(recNum)-50;
	
	StringBuffer pageUrl = new StringBuffer();
	
	// Get values to pass into the links
	
	String accountName = (String)request.getParameter("searchString");
	
	if ((accountName != null) && (accountName.length() > 0)) {
	
		pageUrl.append("&searchString=" + accountName);
		
	}
	
	pageUrl.append("&page=parentBrowse&submittingSearch=true");
	

%>

<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
  <td width="750" valign="top"> 
      <p>&nbsp;</p>
      <p class="heading2">Parent Browse</p><br>
	   <table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr> 
		<td width="15%"><div align="right">Account Name:</div></td>
		<td width="3%">&nbsp;</td>
                <form name="searchForm" method="POST" action="ParentBrowse">
                    <input type="hidden" name="page" value="parentBrowse">
                    <input type="hidden" name="submittingSearch" value="true">  
                    <td width="22%"><input type="text" name="searchString" value="<%= searchString %>"></td>
                    <td width="60%"><input type="image" src="<%= sImagesURL %>button_search.gif" width="70" height="20"></td>
                </form>
	</tr>
</table>
	<br><hr align="left" width="600" size="1" color="#999999" noshade>
      <br>
	  <table width="80%" border="0" cellspacing="1" cellpadding="0">
	  
		<%
			if (customerResults.size() > 0) {					
		%>
		  	<tr><td>&nbsp;</td></tr>
		  	<tr>
		  		<td align = "center">
					<%
							if(!recNum.equals("1")){
							%>
							<a href="ParentBrowse?page=search&recNum=<%= prevRecNum %>&link=paginate<%= pageUrl %>">&lt;&lt; Prev 50</a> | 
							<% }else{ %>
							<font class="textgray">&lt;&lt; Prev 50 | </font>
							<% } %>
							<% if(customerResults.size()==51){ %>
							<a href="ParentBrowse?page=search&recNum=<%= nextRecNum %>&link=paginate<%= pageUrl %>">Next 50  &gt;&gt;</a>
							<% } else { %>
							<font class="textgray">Next 50 &gt;&gt;</font>
							<% }  
					%>
		  		</td>
		  	</tr>
		  	<tr><td>&nbsp;</td></tr>
	      	<tr>
	      		<th><div align="left">Account Name</div></th>
	      		<th><div align="left">Vista Number</div></th>
	      		<th><div align="left">City</div></th>
	      		<th><div align="left">State</div></th>
	      		<th><div align="left">Zip Code</div></th>
		   </tr>
					      	
		<% } // end if acounts.size() > 0 %>      		  
	  
	  
<%

// We need to build in an offset.  Since the initail load comes in with a single value
// of the existing selected SE and the searched list comes in with 51 records (to see if
// the 'Next' link should be displayed), we need to build an offset for the counter.  If 
// there is 51 records, then delete the last one (this will be put on the next page).

int offset = 0;

if (customerResults.size() == 51) {
	   
	   offset = 1;
	   
}


   if (customerResults.size() > 0){
       for (int i=0; i < (customerResults.size()-offset); i++){
            Account account = (Account) customerResults.get(i);
            if ((i%2) == 0){
    %>
            <tr class="cellShade">
    <%      } else {
    %>
            <tr>
    <%      }
    %>
                    <td><a href="ParentBrowse?page=saveParent&parent=<%= account.getVcn() %>"><%= account.getCustomerName()%></a></td>
                    <td><%= account.getVcn() %></td>
                    <td><%= account.getBusinessAddress().getCity() %></td>
                    <td><%= account.getBusinessAddress().getState() %></td>
                    <td><%= account.getBusinessAddress().getZip() %></td>
              </tr>

    <%
        }
    } else if(!searchString.equals("")) {

%>
    <tr>
           <td><br>No accounts found with <%= searchString %></td>
    </tr>
<%  } %>

		<%
			if (customerResults.size() > 0) {					
		%>
		  	<tr><td>&nbsp;</td></tr>
		  	<tr>
		  		<td align = "center">
					<%
							if(!recNum.equals("1")){
							%>
							<a href="ParentBrowse?page=search&recNum=<%= prevRecNum %>&link=paginate<%= pageUrl %>">&lt;&lt; Prev 50</a> | 
							<% }else{ %>
							<font class="textgray">&lt;&lt; Prev 50 | </font>
							<% } %>
							<% if(customerResults.size()==51){ %>
							<a href="ParentBrowse?page=search&recNum=<%= nextRecNum %>&link=paginate<%= pageUrl %>">Next 50  &gt;&gt;</a>
							<% } else { %>
							<font class="textgray">Next 50 &gt;&gt;</font>
							<% }  
					%>
		  		</td>
		  	</tr>
		  	<tr><td>&nbsp;</td></tr>
		<% } // end if acounts.size() > 0 %>      		  
	  

  </table>
	  <br>
     <!--
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
      	<tr>
      		<td width="10%"><a href="ParentBrowse?page=parentBrowse&direction=prev&listIndex=">&lt; Prev 50</a></td>
      		<td width="57%"><a href="ParentBrowse?page=parentBrowse&direction=next&listIndex">Next 50 &gt;</a></td>
      		<td width="33%">&nbsp;</td>
	  		</tr>
 			 </table>
 			 -->
  <p>&nbsp;</p>
  </td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>
