<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*, java.text.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ include file="analytics.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
ArrayList divisions = (ArrayList)request.getAttribute("divisions");
Geography geog = (Geography)request.getAttribute("geog");
String salesOrders = (String) request.getAttribute("salesOrders");
ArrayList reportBeans = (ArrayList) request.getAttribute("reportBeans");
%>
<html>
	<%@ include file="./SMRCHeader.jsp" %>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750">
				<p><br>
					<a class="crumb" href="SMRCHome">Home Page</a>
					<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Critical Success Factors</span>
				</p>
			</td>
		</tr>
	</table>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750" valign="top">
			<p>&nbsp;</p>
			<p class="heading2"><%= geog.getDescription() %> Critical Success Factors</p>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="89%" height="15">
						<div align="right"></div>
					</td>
					<td width="11%">
						<% if (salesOrders.equalsIgnoreCase("sales")){ %>
						<div align="left"><a href="CSF?page=District&district=<%= geog.getGeog() %>&salesOrders=orders">Show Orders</a></div>
						<% } else { %>
						<div align="left"><a href="CSF?page=District&district=<%= geog.getGeog() %>&salesOrders=sales">Show Sales</a></div>
						<% } %>
					</td>
				</tr>
			</table>
	<b>TAP Dollars</b> - <a href="javascript:openPopup('TapDollarsExplainPopup.jsp', 'tapDollars', '200', '400')" >What are TAP Dollars?</a>
	  <table width="100%" border="0" cellspacing="0" cellpadding="0">
	  	<tr>
	  		<td width="60%">&nbsp;</td>
	  		<td width="40%">
	  		<div align="right">
	  			<%
	  				String show = (String)request.getAttribute("salesOrders");
	  				if ((show == null) || (show.length() < 1)) {
	  					
	  					show = "invoice";
	  					
	  				}
	  				
					if(show.equals("orders")){
					%>
					You are current viewing Order Tap Dollars.  <br>To change to Sales Tap Dollars, please select the following link :<br> <br>
					<a href="CSF?page=District&district=<%= geog.getGeog() %>&salesOrders=invoice">Show Sales Tap Dollars</a>
					<% }else{ %>
					You are current viewing Sales Tap Dollars.  <br>To change to Order Tap Dollars, please select the following link : <br><br>
					<a href="CSF?page=District&district=<%= geog.getGeog() %>&salesOrders=orders">Show Order Tap Dollars</a>
					<%
					}
	  			%>
	  			</div>
  		 </td>
  		</tr>
  	</table>
			<%
	       NumberFormat nf = NumberFormat.getCurrencyInstance();
	       nf.setMaximumFractionDigits(0);
	       NumberFormat pf = NumberFormat.getPercentInstance();
	       pf.setMaximumFractionDigits(0);
					for (int i=0; i < divisions.size(); i++) {
					Division division = (Division)divisions.get(i);
			 		boolean totalsFound = false;
			%>
			<table width="100%" border="0" cellspacing="1" cellpadding="0">
				<tr>
					<td height="20" colspan="9" >&nbsp;</td>
				</tr>
				<tr>
					<td height="20" colspan="9" class="tableTitle"><%= division.getName() %></td>
				</tr>
				<tr>
					<th width="17%">&nbsp;</th>
					<th width="17%">YTD Sales</th>
					<th width="17%">Prev YTD Sales</th>
					<th width="17%">% Growth</th>
				</tr>
				<tr class="cellShade">
					<%
					for (int beanIndex = 0; beanIndex < reportBeans.size(); beanIndex++){
              CSFReportBean  bean = (CSFReportBean) reportBeans.get(beanIndex);
              if (division.getId().equalsIgnoreCase(bean.getId())){
                  totalsFound = true;
					%>
					<td>
						<div align="left" id="cellShade"><a href="CustomerListing?page=listing&targetOnly=true&DIVISION=<%= division.getId() %>&DISTRICT=<%= geog.getGeog() %>">Division Target Accounts in District</a></div>
					</td>
					<td>
						<div align="center"><%= nf.format(bean.getYTDSales()) %></div>
					</td>
					<td>
						<div align="center"><%= nf.format(bean.getPrevYTDSales()) %></div>
					</td>
					<% if (bean.getPrevYTDSales() != 0){ %>
					<td>
						<div align="center"><%= pf.format(bean.getGrowthPercentage()) %></div>
					</td>
					<% } else { %>
					<td>
						<div align="center">N/A</div>
					</td>
					<% } %>
					<% }
					  }
					 		if (!totalsFound){
					%>
					<td>
						<div align="left" id="cellShade"><a href="CustomerListing?page=listing&targetOnly=true&DIVISION=<%= division.getId() %>&DISTRICT=<%= geog.getGeog() %>">Division Target Accounts in District</a></div>
					</td>
					<td>
						<div align="center"><%= nf.format(0) %></div>
					</td>
					<td>
						<div align="center"><%= nf.format(0) %></div>
					</td>
					<td>
						<div align="center">N/A</div>
					</td>
					<%                          }
					%>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
		</tr>
		<tr>
			<td>
				&nbsp;
			</td>
		</tr>
		<%
		ArrayList csfs = division.getCSFs();
		String csfMsg=null;
		if(csfs.size()==0){
			csfMsg = "No Critical Success factors for this division and district.";
		}else{
			csfMsg = "Click on the critical success factor to view and modify notes.";
		}
		%>
		<tr>
			<td>
				<%= csfMsg %><br><br>
			</td>
		</tr>
		<%
		for (int j=0; j < csfs.size(); j++) {
		CriticalSuccessFactor csf = (CriticalSuccessFactor)csfs.get(j);
		  String color = null;
		  if(csf.getColor().equalsIgnoreCase("Green")){
		  	color="Status: <img src='" + sImagesURL + "green_status.gif' border='0' width='10' height='10'>";
		  }else if(csf.getColor().equalsIgnoreCase("Yellow")){
		  	color="Status: <img src='" + sImagesURL + "yellow_status.gif' border='0' width='10' height='10'>";
		  }else if(csf.getColor().equalsIgnoreCase("Red")){
		  	color="Status: <img src='" + sImagesURL + "red_status.gif' border='0' width='10' height='10'>";
		  }else{
		 	 	color="";
		  }
		 %>
		<tr>
			<td class="tableTitle"><a href="/TargetAccountPlanner/CSF?page=editDistrictCSF&district=<%= request.getParameter("district") %>&csfid=<%= csf.getId() %>"><%= csf.getName() %></a>
				<%
				if(csf.getDateAdded()!=null){
				%>
				 <font size="1">(<%= (csf.getUserAddedName().equals("")?"unknown":csf.getUserAddedName()) %> - <%= csf.getDateAdded() %>)</font>&nbsp;&nbsp;<%= color %>
				<%
				}
				%>
			</td>
		</tr>
		<% } %>
		</td>
	</tr>
</table>
<br>
	<% } %>
	</td>
</tr>
</table>
<!--
<table width="100%" border="0" cellspacing="10" cellpadding="0">
	<tr>
		<td><img src="<%= sImagesURL %>button_save.gif" width="70" height="20"></td>
	</tr>
</table>
-->
</body>
</html>
