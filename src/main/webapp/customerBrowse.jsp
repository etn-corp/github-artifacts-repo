<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.Globals"%>
<%@ page import="com.eaton.electrical.smrc.util.StringManipulation"%>
<html>
<%@ include file="./SMRCHeaderNoNav.jsp" %>
<%@ include file="analytics.jsp" %> 
<%
    //String searchString = (String) request.getAttribute("searchString");
		ArrayList records = (ArrayList)request.getAttribute("records");
		if(records==null){
			records = new ArrayList();
		}
		
		String gotoPage=null;
		String distApproval = request.getParameter("distApproval");
		if(distApproval!=null && distApproval.equals("true")){
			gotoPage="saveApproval";
		}else{
			gotoPage="save";
		}
		
		String recNum = request.getParameter("recNum");
		
		if(recNum==null){
			recNum="1";
		}			
		int nextRecNum=Globals.a2int(recNum)+50;
		int prevRecNum=Globals.a2int(recNum)-50;
		
		StringBuffer pageUrl = new StringBuffer();
		
		// Get tmId and type to pass on
		
		String distId = (String)request.getParameter("distId");
		
		if (distId != null) {
		
			pageUrl.append("&distId=" + distId);
			
		}
		
		String vcn = (String)request.getParameter("vcn");
		
		if ((vcn != null) && (vcn.length() > 0)) {
		
			pageUrl.append("&vcn=" + vcn);
			
		}

		String accountName = (String)request.getParameter("ACCOUNT_NAME");
		
		if ((accountName != null) && (accountName.length() > 0)) {
		
			pageUrl.append("&ACCOUNT_NAME=" + accountName);
			
		}
		
		String vistaNumber = (String)request.getParameter("VISTA_CUSTOMER_NUMBER");
		
		if ((vistaNumber != null) && (vistaNumber.length() > 0)) {
		
			pageUrl.append("&VISTA_CUSTOMER_NUMBER=" + vistaNumber);
			
		}

%>
 <form name="searchForm" method="POST" action="CustomerBrowse">
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
  <td width="750" valign="top"> 
      <p>&nbsp;</p>
      <p class="heading2">Account Browse</p><br>
	   <table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr> 
		<td width="15%"><div align="right">Account Name:</div></td>
		<td width="3%">&nbsp;</td>
        <td width="22%"><input type="text" name="ACCOUNT_NAME" value="<%= StringManipulation.noNull(request.getParameter("ACCOUNT_NAME")) %>"></td>
        <td width="60%"></td>
	</tr>
	<tr> 
		<td width="15%"><div align="right">Vista Number:</div></td>
		<td width="3%">&nbsp;</td>
	  <td width="22%"><input type="text" name="VISTA_CUSTOMER_NUMBER" value="<%= StringManipulation.noNull(request.getParameter("VISTA_CUSTOMER_NUMBER")) %>"></td>
	  <td width="60%"><input type="image" src="<%= sImagesURL %>button_search.gif" width="70" height="20"></td>
	</tr>	
</table>
<input type="hidden" name="page" value="search">
<input type="hidden" name="distId" value="<%= distId %>">
<%
		if(distApproval!=null && distApproval.equals("true")){
			%>
			<input type="hidden" name="distApproval" value="true">
			<%
		}
%>

</form>
	<br><hr align="left" width="600" size="1" color="#999999" noshade>
      <br>
	  <table width="80%" border="0" cellspacing="1" cellpadding="0">
	  
		<%
			if (records.size() > 0) {					
		%>
		  	<tr><td>&nbsp;</td></tr>
		  	<tr>
		  		<td align = "center">
					<%
							if(!recNum.equals("1")){
							%>
							<a href="CustomerBrowse?page=search&recNum=<%= prevRecNum %>&link=paginate<%= pageUrl %>">&lt;&lt; Prev 50</a> | 
							<% }else{ %>
							<font class="textgray">&lt;&lt; Prev 50 | </font>
							<% } %>
							<% if(records.size()==51){ %>
							<a href="CustomerBrowse?page=search&recNum=<%= nextRecNum %>&link=paginate<%= pageUrl %>">Next 50  &gt;&gt;</a>
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
if(StringManipulation.noNull(request.getParameter("page")).equalsIgnoreCase("search") && records.size()==0){
%>
    <tr>
           <td>No accounts found</td>
    </tr>
<%
}

// We need to build in an offset.  Since the initail load comes in with a single value
// of the existing selected SE and the searched list comes in with 51 records (to see if
// the 'Next' link should be displayed), we need to build an offset for the counter.  If 
// there is 51 records, then delete the last one (this will be put on the next page).

int offset = 0;

if (records.size() == 51) {
	   
	   offset = 1;
	   
}

for(int i=0;i<(records.size()-offset);i++){
AccountBrowseRecord record = (AccountBrowseRecord)records.get(i);
String cellShade=null;
if(i%2==0){
	cellShade=" class=\"cellShade\"";
}else{
	cellShade="";
}
%>
            <tr<%= cellShade %>>
                    <td><a href="CustomerBrowse?page=<%= gotoPage %>&distId=<%= distId %>&keyAcct=<%= record.getVcn() %>"><%= record.getCustomerName() %></a></td>
                    <td><%= record.getVcn() %></td>
                    <td><%= record.getCity() %></td>
                    <td><%= record.getState() %></td>
                    <td><%= record.getZip() %></td>
              </tr>
<% } %>

 		<%
			if (records.size() > 0) {					
		%>
		  	<tr><td>&nbsp;</td></tr>
		  	<tr>
		  		<td align = "center">
					<%
							if(!recNum.equals("1")){
							%>
							<a href="CustomerBrowse?page=search&recNum=<%= prevRecNum %>&link=paginate<%= pageUrl %>">&lt;&lt; Prev 50</a> | 
							<% }else{ %>
							<font class="textgray">&lt;&lt; Prev 50 | </font>
							<% } %>
							<% if(records.size()==51){ %>
							<a href="CustomerBrowse?page=search&recNum=<%= nextRecNum %>&link=paginate<%= pageUrl %>">Next 50  &gt;&gt;</a>
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
      
  <p>&nbsp;</p>
  </td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>
