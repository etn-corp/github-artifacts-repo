<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.Money"%>
<%@ page import="com.eaton.electrical.smrc.util.Globals"%>
<%@ include file="analytics.jsp" %>
<%
ArrayList accounts = (ArrayList)request.getAttribute("accounts");
String message = (String)request.getAttribute("message");




// Braffet : REmoved for TAP Dollars

//String defaultDollarType = (String)request.getAttribute("defaultDollarType");
//if(defaultDollarType==null || defaultDollarType.length()==0){
//	defaultDollarType="CASEY REMOVE Charge To";
//}

//String seid=request.getParameter("SE_ID");





User usr = (User)request.getAttribute("usr");


String recNum = request.getParameter("recNum");
if(recNum==null){
	recNum="1";
}
int nextRecNum=Globals.a2int(recNum)+50;
int prevRecNum=Globals.a2int(recNum)-50;
//int prevRecNum=Globals.a2int(recNum)-3;

Enumeration params = request.getParameterNames();

StringBuffer urlParams = new StringBuffer();
StringBuffer xcelUrl = new StringBuffer("?xcel=true");
while (params.hasMoreElements()) {
String nameParam = (String) params.nextElement();
	xcelUrl.append("&" + nameParam + "=" + request.getParameter(nameParam));
	if(!request.getParameter(nameParam).equals("") && !nameParam.equals("show") && !nameParam.equals("sort") && !nameParam.equals("recNum") && !nameParam.equals("link") && !nameParam.equals("sortDir")){
		urlParams.append("&" + nameParam + "=" + request.getParameter(nameParam));
	}
}

StringBuffer showUrl = new StringBuffer(urlParams.toString());
StringBuffer sortUrl = new StringBuffer(urlParams.toString());
StringBuffer pageUrl = new StringBuffer(urlParams.toString());

String sortDir = "asc";
if(request.getParameter("sortDir")!=null){
	if(request.getParameter("sortDir").equals("asc")){
		sortDir="desc";
	}else{
		sortDir="asc";
	}
}


if(request.getParameter("show")!=null){
	sortUrl.append("&show=" + request.getParameter("show"));
	pageUrl.append("&show=" + request.getParameter("show"));
}
if(request.getParameter("recNum")!=null){
	sortUrl.append("&recNum=" + request.getParameter("recNum"));
	showUrl.append("&recNum=" + request.getParameter("recNum"));
}
if(request.getParameter("sort")!=null){
	showUrl.append("&sort=" + request.getParameter("sort") + "&sortDir=" + request.getParameter("sortDir"));
	pageUrl.append("&sort=" + request.getParameter("sort") + "&sortDir=" + request.getParameter("sortDir"));
}


%>

<html>
<%@ include file="./SMRCHeader.jsp" %>

<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>
		<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Customer Listing</span>

      </p> 
    </td>
  </tr>
</table>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
   <td width="750" valign="top"> 
      <p>&nbsp;</p>
      <p class="heading2">Customer Listing</p>
      
		<%
		if(accounts.size()==0){
			if(message!=null){
			%>
			<div align="left"><%= message %><br><br></div>
			<%
			}else{
			%>
			<div align="left">No Customers found with selected criteria OR the criteria was not specific enough.<br><br></div>		
			<%
			}
			%>
			<a href="CustomerListing">Click here to search for a customer</a><br>
			<%
		}else{
		%>
     <b>TAP Dollars</b> - <a href="javascript:openPopup('TapDollarsExplainPopup.jsp', 'tapDollars', '200', '400')" >What are TAP Dollars?</a>
	  <table width="100%" border="0" cellspacing="0" cellpadding="0">
	  	<tr>
	  		<td width="60%">&nbsp;</td>
	  		<td width="40%">
	  		<div align="right">
	  			<%
	  				String show = request.getParameter("show");
	  				if ((show == null) || (show.length() < 1)) {
	  					
	  					show = "invoice";
	  					
	  				}
	  				
					if(show.equals("order")){
					%>
					You are current viewing Order Tap Dollars.  <br>To change to Sales Tap Dollars, please select the following link :<br> <br>
					<a href="CustomerListing?show=invoice&link=so<%= showUrl %>">Show Invoice Tap Dollars</a>
					<% }else{ %>
					You are current viewing Sales Tap Dollars.  <br>To change to Order Tap Dollars, please select the following link : <br><br>
					<a href="CustomerListing?show=order&link=so<%= showUrl %>">Show Order Tap Dollars</a>
					<%
					}
	  			%>
	  			</div>
  		 </td>
  		</tr>
  	</table>

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
      	<tr>
      		<td width="17%">
      			<p><a href="AccountProfile">Add new Account<br>
      			</a></p>
   			</td>
      		<td width="83%"><a href="CustomerListing<%= xcelUrl.toString() %>"><img src="<%= sImagesURL %>excel.gif" border="0" align="absmiddle">&nbsp;Export to Excel</a><a href="accountProfile.html"> </a></td>
	 </tr>
   	 </table>
   	 	<p align="center">
			<%
			if(!recNum.equals("1")){
			%>
			<a href="CustomerListing?recNum=<%= prevRecNum %>&link=paginate<%= pageUrl %>">&lt;&lt; Prev 50</a> | 
			<% }else{ %>
			<font class="textgray">&lt;&lt; Prev 50 | </font>
			<% } %>
			<% if(accounts.size()==51){ %>
			<a href="CustomerListing?recNum=<%= nextRecNum %>&link=paginate<%= pageUrl %>">Next 50  &gt;&gt;</a>
			<% } else { %>
			<font class="textgray">Next 50 &gt;&gt;</font>
			<% } %>
	  </p>
	  <table width="100%" border="0" cellspacing="1" cellpadding="0">
	  <tr>
	  		<th width="5"></th>
	  		<th width="40"><a href="CustomerListing?sort=vista_customer_number&link=sort&sortDir=<%= sortDir %><%= sortUrl %>" class="tableLink">Vista Customer Number</a></th>
	  		<th width="160"><a href="CustomerListing?sort=customer_name&link=sort&sortDir=<%= sortDir %><%= sortUrl %>" class="tableLink">Account Name</a></th>
	  		<th width="80"><a href="CustomerListing?sort=sales_engineer1_last_name&link=sort&sortDir=<%= sortDir %><%= sortUrl %>" class="tableLink">Sales Engineer</a></th>
	  		<th width="80"><a href="CustomerListing?sort=sp_geog&link=sort&sortDir=<%= sortDir %><%= sortUrl %>" class="tableLink">District</a></th>
	  		<th width="40"><a href="CustomerListing?sort=target_account_flag&link=sort&sortDir=<%= sortDir %><%= sortUrl %>" class="tableLink">Target Account</a></th>
	  		<th width="80"><a href="CustomerListing?sort=primary_seg_name&link=sort&sortDir=<%= sortDir %><%= sortUrl %>" class="tableLink">Primary Segment</a></th>
	  		<th width="80"><a href="CustomerListing?sort=sec_seg_name&link=sort&sortDir=<%= sortDir %><%= sortUrl %>" class="tableLink">Secondary Segment</a></th>
	  		<th width="40"><a href="CustomerListing?sort=sp_stage&link=sort&sortDir=<%= sortDir %><%= sortUrl %>" class="tableLink">Stage</a></th>
	  		<th width="80"><a href="CustomerListing?sort=curr_ytd_total&link=sort&sortDir=<%= sortDir %><%= sortUrl %>" class="tableLink">YTD Sales ($)</a></th>
	  		<th width="80"><a href="CustomerListing?sort=prev_ytd_total&link=sort&sortDir=<%= sortDir %><%= sortUrl %>" class="tableLink">Prev YTD Sales ($)</a></th>
  		</tr>
  	<%
		String bgcolor="";
		
		
	for (int i=0; i<accounts.size() && i<50; i++) {
			Account account = (Account)accounts.get(i);
			AccountNumbers numbers = account.getNumbers();
			if(i%2==0){
				bgcolor=" class=\"cellShade\"";
			}else{
				bgcolor="";
			}
		%>
  		
	  	<tr<%= bgcolor %>>
	  		<td valign="top">
	  			<div align="left"><%= account.getRownum() %>.&nbsp;&nbsp;</div>
  		    </td>
  		 	<td valign="top">
  		 		<div align="left"><a href="AccountProfile?acctId=<%= account.getVcn() %>&isModify=N"><%= account.getVcn() %></a></div>
	        </td>
	  		<td valign="top">
	  			<div align="left"><a href="AccountProfile?acctId=<%= account.getVcn() %>&isModify=N"><%= account.getCustomerName() %></a></div>
  		 </td>
	  		<td valign="top">
	  			<div align="center"><%= account.getSalesEngineer1Name() %></div>
	  		</td>

	  		<td valign="top">
	  			<div align="center"><%= account.getDistrict() %></div>
	  		</td>
	  		<td valign="top">
	  			<div align="center">
	  			<%
	  			if(account.isTargetAccount()){
	  				out.println("Y");
	  			}else{
	  				out.println("N");	  			
	  			}
	  			%>
	  			</div>
	  		</td>
	  		<td valign="top">
	  			<div align="center">
	  			<%= account.getPrimarySegmentName() %>
	  			</div>

	  		</td>
	  		<td valign="top">
	  			<div align="center">
	  			<%= account.getSecondarySegmentName() %>
	  			</div>
	  		</td>
	  		<td valign="top">
	  			<div align="center"><%= account.getStage() %></div>
	  		</td>
	  		<td valign="top">

	  			<div align="right">
	  			<% if(usr.ableToSee(account) || usr.hasOverrideSecurity() || usr.hasSegmentOverride(account)){ %>
	  			<%= Money.formatDoubleAsDollars(numbers.getCurrentYTD()) %>
	  			<% }else{ %>
	  			N/A
	  			<% } %>
	  			</div>
	  		</td>
	  		<td valign="top">
	  			<div align="right">
	  			<% if(usr.ableToSee(account) || usr.hasOverrideSecurity() || usr.hasSegmentOverride(account)){ %>
	  			<%= Money.formatDoubleAsDollars(numbers.getCurrentMinusOneYTD()) %>
	  			<% }else{ %>
	  			N/A
	  			<% } %>	  			
	  			</div>
	  		</td>
  		</tr>

		<% } %>

  	</table>
	<p align="center">
			<%
			if(!recNum.equals("1")){
			%>
			<a href="CustomerListing?recNum=<%= prevRecNum %>&link=paginate<%= pageUrl %>">&lt;&lt; Prev 50</a> | 
			<% }else{ %>
			<font class="textgray">&lt;&lt; Prev 50 | </font>
			<% } %>
			<% if(accounts.size()==51){ %>
			<a href="CustomerListing?recNum=<%= nextRecNum %>&link=paginate<%= pageUrl %>">Next 50&gt;&gt;</a>
			<% } else { %>
			<font class="textgray">Next 50 &gt;&gt;</font> 
			<% } %>
	<% } %>
	</p>

   </td>
  </tr>
</table>
<p>&nbsp;</p>

  </body>
</html>
