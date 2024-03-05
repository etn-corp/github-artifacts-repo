<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.*"%>

<html>
<%@ include file="./SMRCHeader.jsp" %>
<%@ include file="analytics.jsp" %>
<%
ArrayList segments = (ArrayList)request.getAttribute("segments");
User usr = header.getUser();
String showSalesOrders = usr.getShowSalesOrders();
boolean canSeeApprovalLink = ((Boolean) request.getAttribute("canSeeApprovalLink")).booleanValue();
int approvalsPending = ((Integer) request.getAttribute("approvalsPending")).intValue();


// Calendar cal = Calendar.getInstance();
// int currYear = cal.get(Calendar.YEAR);
int currYear = Globals.a2int((String) session.getAttribute("srYear"));
// If the year is zero, something went wrong... set to current year
if ( currYear == 0 ) {
	Calendar cal = new GregorianCalendar();
    
    // Get the components of the date
    currYear = cal.get(Calendar.YEAR);
}

%>

<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>
		<!-- <span class="crumbarrow">&gt;</span><span class="crumbcurrent">Current Page</span></p> -->
    </td>
  </tr>
</table>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750" valign="top"> 
	<br>
      <p class="heading3"><span class="heading2">Target Accounts</span></p>
   
	  <table width="100%" border="0" cellspacing="0" cellpadding="0">
	  	<tr>
	  		<td width="90%"><span align="left" class="heading2"><%= usr.getFirstName() %> <%= usr.getLastName() %></span></td>
	  		<td width="10%">
				<%
				if(request.getParameter("show")!=null){
					if(request.getParameter("show").equals("orders")){
					%>
					<a href="SMRCHome?show=sales">Show Sales</a>
					<% }else{ %>
					<a href="SMRCHome?show=orders">Show Orders</a>
					<%
					}
				}else if(usr.getShowSalesOrders().equals("o")){
				%>
					<a href="SMRCHome?show=sales">Show Sales</a>
				<% }else{ %>
					<a href="SMRCHome?show=orders">Show Orders</a>
				<% } %>
				</td>
  		</tr>
  	</table>
	  
	  
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <% if (canSeeApprovalLink) { %>
          <tr>
      		<td><span><a href="PendingApprovals">You have <%= approvalsPending %> requests for approval</a></span>
      	  </tr>
      <% } %>
        <tr>
        	<td>&nbsp;</td>
        </tr>
      	<tr>
      		<td width="100%">
      		<a href="AccountProfile">Add New Account</a>
      		</td>
	   </tr>
   	 </table>
	 <br>
			<%
			for (int i=0; i < segments.size(); i++) {
			Segment segment = (Segment)segments.get(i);
			ArrayList accounts = segment.getAccounts();
                        double totCurrYear = 0;
                        double totPrevYear = 0;
                        double totPrevYTD = 0;
                        double totPrevYear2 = 0;
                        double totPotential = 0;
                        double totForecast = 0;
                        double totCompetitor1 = 0;
                        double totCompetitor2 = 0;
			%>
			
      <table width="100%" border="0" cellspacing="1" cellpadding="1">
      	<tr>
      		<td colspan="10" ><span id="tableTitle"><%= segment.getName() %></span>&nbsp;&nbsp;<a href="AccountToolbox?segmentId=<%= segment.getSegmentId() %>">[ toolbox ]</a></td>	   
	   </tr>
      	<tr>
      		<th valign="top" width="29%"><div align="center" >Name</div></th>
      		<th valign="top" width="6%" ><div align="center" ><%= currYear %> YTD $</div></th>
      		<th valign="top" width="6%" ><div align="center" ><%= currYear - 1 %> YTD $</div></th>
      		<th valign="top" width="6%" ><div align="center" ><%= currYear - 1 %> Total $</div></th>
      		<th valign="top" width="6%"><div align="center" ><%= currYear - 2 %> Total $</div></th>
      		<th valign="top" width="5%"><div align="center" >Potential $</div></th>
      		<th valign="top" width="5%"><div align="center" >Forecast $</div></th>
      		<th valign="top" width="15%"><div align="center" >Competitor 1 $</div></th>
      		<th valign="top" width="15%"><div align="center" >Competitor 2 $</div></th>
	   </tr>
			<%
			String bgcolor=null;
			for (int j=0; j < accounts.size(); j++) {
			Account account = (Account)accounts.get(j);
				AccountNumbers numbers = account.getNumbers();
				if(j%2==0){
					bgcolor="";
				}else{
					bgcolor=" class=\"cellShade\"";
				}
                totCurrYear += numbers.getCurrentTotal();
                totPrevYear += numbers.getCurrentMinusOneTotal();
                totPrevYTD += numbers.getCurrentMinusOneYTD();
                totPrevYear2 += numbers.getCurrentMinusTwoTotal();
                totPotential += numbers.getPotential();
                totForecast += numbers.getForecast();
                totCompetitor1 += numbers.getCompetitor1Dollars();
                totCompetitor2 += numbers.getCompetitor2Dollars();
			%>
      	<tr<%= bgcolor %>>
      		<td valign="top"><div align="left"><a href="AccountProfile?acctId=<%= account.getVcn() %>"><%= account.getCustomerName() %></a></div></td>
      		<td valign="top" bgcolor="#E0E0E0"><div align="right">&nbsp;&nbsp;<%= Money.formatDoubleAsDollars(numbers.getCurrentYTD())  %>&nbsp;</div></td>
      		<td valign="top"><div align="right">&nbsp;&nbsp;<%= Money.formatDoubleAsDollars(numbers.getCurrentMinusOneYTD())  %>&nbsp;</div></td>
      		<td valign="top" bgcolor="#E0E0E0"><div align="right">&nbsp;&nbsp;<%= Money.formatDoubleAsDollars(numbers.getCurrentMinusOneTotal())  %>&nbsp;</div></td>
      		<td valign="top"><div align="right">&nbsp;&nbsp;<%= Money.formatDoubleAsDollars(numbers.getCurrentMinusTwoTotal())  %>&nbsp;</div></td>
      		<td valign="top" bgcolor="#E0E0E0"><div align="right">&nbsp;&nbsp;<%= Money.formatDoubleAsDollars(numbers.getPotential())  %>&nbsp;</div></td>
      		<td valign="top"><div align="right">&nbsp;&nbsp;<%= Money.formatDoubleAsDollars(numbers.getForecast())  %>&nbsp;</div></td>
					<% 
					if (numbers.getCompetitor1().getId() > 0){
					%>
						<td valign="top" bgcolor="#E0E0E0"><div align="right">&nbsp;&nbsp;<%= numbers.getCompetitor1().getDescription() %> - <%= Money.formatDoubleAsDollars(numbers.getCompetitor1Dollars()) %>&nbsp;</div></td>
					<% } else { %>
          				<td valign="top" bgcolor="#E0E0E0"><div align="right">&nbsp;&nbsp;<%= Money.formatDoubleAsDollars(numbers.getCompetitor1Dollars()) %>&nbsp;</div></td>
					<% } %>
					<% if (numbers.getCompetitor2().getId() > 0){ %>
						<td valign="top"><div align="right">&nbsp;&nbsp;<%= numbers.getCompetitor2().getDescription() %> - <%= Money.formatDoubleAsDollars(numbers.getCompetitor2Dollars()) %>&nbsp;</div></td>
					<% } else { %>
             			<td valign="top"><div align="right">&nbsp;&nbsp;<%= Money.formatDoubleAsDollars(numbers.getCompetitor2Dollars()) %>&nbsp;</div></td>
					<% } %>	
					</tr>
	   <% } %>
          <tr><td colspan="9">&nbsp;</td></tr>
          <tr>
          <td valign="top"><div align="left"><b>Totals for <%= segment.getName() %></b></div></td>
      		<td valign="top" bgcolor="#E0E0E0"><div align="right">&nbsp;&nbsp;<b><%= Money.formatDoubleAsDollars(totCurrYear)  %></b>&nbsp;</div></td>
      		<td valign="top"><div align="right">&nbsp;&nbsp;<b><%= Money.formatDoubleAsDollars(totPrevYTD)  %></b>&nbsp;</div></td>
      		<td valign="top" bgcolor="#E0E0E0"><div align="right">&nbsp;&nbsp;<b><%= Money.formatDoubleAsDollars(totPrevYear)  %></b>&nbsp;</div></td>
      		<td valign="top"><div align="right">&nbsp;&nbsp;<b><%= Money.formatDoubleAsDollars(totPrevYear2)  %></b>&nbsp;</div></td>
      		<td valign="top"  bgcolor="#E0E0E0"><div align="right">&nbsp;&nbsp;<b><%= Money.formatDoubleAsDollars(totPotential)  %></b>&nbsp;</div></td>
      		<td valign="top"><div align="right">&nbsp;&nbsp;<b><%= Money.formatDoubleAsDollars(totForecast)  %></b>&nbsp;</div></td>
          	<td valign="top"  bgcolor="E8E8E8"><div align="right">&nbsp;&nbsp;<b><%= Money.formatDoubleAsDollars(totCompetitor1)  %></b>&nbsp;</div></td>
      		<td valign="top"><div align="right">&nbsp;&nbsp;<b><%= Money.formatDoubleAsDollars(totCompetitor2)  %></b>&nbsp;</div></td>
          </tr>
   	 </table>
	 <br>
	 <table width="100%" border="0" cellspacing="0" cellpadding="0">
	 <tr>
		<td width="2%">&nbsp;</td>
		<td width="98%"><a href="CustomerListing?page=listing&segment=<%= segment.getSegmentId() %>">All <%= segment.getName() %></a></td>
	</tr>
	</table>
  <br><hr width="100%" noshade color="#DDDDDD"><br>  
	<% } %>

   </td>
  </tr>
</table>
<p>&nbsp;</p>

  </body>
</html>
