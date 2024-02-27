<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.*"%>
<%@ include file="analytics.jsp" %>
<%@ page import="com.eaton.electrical.smrc.util.StringManipulation"%>
<html>
<%@ include file="./SMRCHeaderNoNav.jsp" %>
<%@ include file="analytics.jsp" %> 
<%
    		ArrayList accounts = (ArrayList)request.getAttribute("accounts");
            ArrayList tmAccountBeans = (ArrayList) request.getAttribute("tmAccountBeans");
            int tmId = ((Integer) request.getAttribute("tmId")).intValue();
			if(accounts==null){
				accounts = new ArrayList();
			}
            String gotoPage="save";
            String calledFor = (String) request.getAttribute("calledFor");
            session.setAttribute("calledFor", calledFor);
            String accountType = "";
            if (calledFor.equalsIgnoreCase("dist")){
                accountType = "Distributors ";
            } else {
                accountType = "End Customers ";
            }
		
    		String recNum = request.getParameter("recNum");
    		
    		if(recNum==null){
    			recNum="1";
    		}			
    		int nextRecNum=Globals.a2int(recNum)+50;
    		int prevRecNum=Globals.a2int(recNum)-50;
    		
    		StringBuffer pageUrl = new StringBuffer();
    		
    		// Get tmId and type to pass on
    		
    		String tmIdString = (String)request.getParameter("tmId");
    		
    		if (tmIdString != null) {
    		
    			pageUrl.append("&tmId=" + tmIdString);
    			
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
<script language="javascript">

function addAccount(vcn, children){   
<% if (calledFor.equalsIgnoreCase("dist")){
%>
  window.opener.document.tmSetup.addAccount.value=vcn;
  if (children){
        window.opener.document.tmSetup.addAccountChildren.value=vcn;
  }
<% } else {
%>
  window.opener.document.tmSetup.addEndCustomer.value=vcn;
  if (children){
        window.opener.document.tmSetup.addEndCustomerChildren.value=vcn;
  }
<% }
%>
  window.opener.document.tmSetup.action.value = "silentsave";
  window.opener.document.tmSetup.submit();
  history.go(0);

}

function removeAccount(vcn){   
<% if (calledFor.equalsIgnoreCase("dist")){
%>
        window.opener.document.tmSetup.removeAccount.value=vcn;
<% } else {
%>
        window.opener.document.tmSetup.removeEndCustomer.value=vcn;
<% }
%>
  window.opener.document.tmSetup.action.value = "silentsave";
  window.opener.document.tmSetup.submit();
  history.go(0);

}


</script>

 <form name="searchForm" action="TargetMarketCustomerBrowse">
<input type=hidden name="tmId" value="<%= tmId %>">
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
  <td width="750" valign="top"> 
      <p>&nbsp;</p>
      <p class="heading2">Target Market <%= accountType %> Selection</p><br>
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
<input type="hidden" name="distId" value="<%= request.getParameter("distId") %>">

<br>


<br>
      <table width="400" border="0" cellspacing="0" cellpadding="0">
            <tr><td colspan="2"><b><%= accountType %>in this Target Market Plan:</b><td>
<%
            for (int i=0; (i < tmAccountBeans.size()) && (i < 50) ; i++){
                DropDownBean tmAccount = (DropDownBean) tmAccountBeans.get(i);
%>
            <tr><td><%= tmAccount.getName() %> (<%= tmAccount.getValue() %>)</td><td><a href="javascript:removeAccount('<%= tmAccount.getValue() %>')">Remove</a></td>
<%          }
%>

      </table>


</form>
	<br><hr align="left" width="600" size="1" color="#999999" noshade>

      

            
	  <table width="80%" border="0" cellspacing="1" cellpadding="0">
		<%
			if (accounts.size() > 0) {					
		%>
		  	<tr><td>&nbsp;</td></tr>
		  	<tr>
		  		<td align = "center">
					<%
							if(!recNum.equals("1")){
							%>
							<a href="TargetMarketCustomerBrowse?page=search&recNum=<%= prevRecNum %>&link=paginate<%= pageUrl %>">&lt;&lt; Prev 50</a> | 
							<% }else{ %>
							<font class="textgray">&lt;&lt; Prev 50 | </font>
							<% } %>
							<% if(accounts.size()==51){ %>
							<a href="TargetMarketCustomerBrowse?page=search&recNum=<%= nextRecNum %>&link=paginate<%= pageUrl %>">Next 50  &gt;&gt;</a>
							<% } else { %>
							<font class="textgray">Next 50 &gt;&gt;</font>
							<% }  
					%>
		  		</td>
		  	</tr>
		  	<tr><td>&nbsp;</td></tr>
	      	<tr>
	      		<th colspan="2"><div align="left">Account  (Click on name to add to Target Market Plan)</div></th>
	      	</tr>
					      	
		<% } // end if acounts.size() > 0 %>      	
					      	
<%
if(StringManipulation.noNull(request.getParameter("page")).equalsIgnoreCase("search") && accounts.size()==0){
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

if (accounts.size() == 51) {
	   
	   offset = 1;
	   
}



for(int i=0;i<(accounts.size()-offset);i++){
AccountBrowseRecord account = (AccountBrowseRecord)accounts.get(i);
String cellShade=null;
if(i%2==0){
	cellShade=" class=\"cellShade\"";
}else{
	cellShade="";
}
%>
            <tr<%= cellShade %>>
                    <td width="50%"><a href="javascript:addAccount('<%= account.getVcn() %>', false)"><%= account.getCustomerName() %> (<%= account.getVcn() %>)</a>
<%                  if (account.getVcn().equalsIgnoreCase(account.getParentNumber())){
%>
                        <td width="50%"><a href="javascript:addAccount('<%= account.getVcn() %>', true)">Add All Children</a></td>
<%                  } else {
%>
                        <td> &nbsp; </td>
<%                  }
%>

              </tr>
<% } %>

		<%
			if (accounts.size() > 0) {					
		%>
		  	<tr><td>&nbsp;</td></tr>
		  	<tr>
		  		<td align = "center">
					<%
							if(!recNum.equals("1")){
							%>
							<a href="TargetMarketCustomerBrowse?page=search&recNum=<%= prevRecNum %>&link=paginate<%= pageUrl %>">&lt;&lt; Prev 50</a> | 
							<% }else{ %>
							<font class="textgray">&lt;&lt; Prev 50 | </font>
							<% } %>
							<% if(accounts.size()==51){ %>
							<a href="TargetMarketCustomerBrowse?page=search&recNum=<%= nextRecNum %>&link=paginate<%= pageUrl %>">Next 50  &gt;&gt;</a>
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