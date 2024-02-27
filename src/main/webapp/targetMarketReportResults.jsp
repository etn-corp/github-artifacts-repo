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
        String reportType = (String) request.getAttribute("reportType");
        String filterString = (String) request.getAttribute("filterString");
        String href = (String) request.getAttribute("href");
        String sortQueryString = (String) request.getAttribute("sortQueryString");

%>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="seHome.html">Home Page</a>
		<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Target Market Report</span>
      </p> 
    </td>
  </tr>
</table>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
   <td width="750" valign="top"> 
      <p>&nbsp;</p>
      <p class="heading2">Target Market <%= reportType %> Report</p>

	  <table width="100%" border="0" cellspacing="1" cellpadding="0">
	  <tr>
	  	<td colspan="6"><b>Filters applied:</b> <%= filterString %></td>
	  </tr>
	  <tr>
	  	<td colspan="6">&nbsp;</td>
	  </tr>
	  <tr>
                        <th><a href="TargetMarketReportResults?<%= sortQueryString %>" class="tableLink"><%= reportType %></a></th>
<%                      if (reportType.equalsIgnoreCase("Account")){
%>
                        <th><a href="#" class="tableLink">Division</a></th>
<%                      }
%>
                        <th><a href="TargetMarketReportResults?<%= sortQueryString %>&sortBy=baselineSales" class="tableLink">Prev Plan<br> Sales to Date</a></th>
                        <th><a href="TargetMarketReportResults?<%= sortQueryString %>&sortBy=planSales" class="tableLink">Sales in Plan<br>to Date</a></th>
	  		<th><a href="TargetMarketReportResults?<%= sortQueryString %>&sortBy=growthPercent" class="tableLink">% Growth</a></th>
	  		<th><a href="TargetMarketReportResults?<%= sortQueryString %>&sortBy=growthPayout" class="tableLink">Growth Payout</a></th>
<!--	  		<th><a href="TargetMarketReportResults?<%= sortQueryString %>&sortBy=bidman" class="tableLink">Bid Manager Rebate</a></th>
	  		<th><a href="TargetMarketReportResults?<%= sortQueryString %>&sortBy=payout" class="tableLink">Current Payout</a></th>
-->	  		<th><a href="TargetMarketReportResults?<%= sortQueryString %>&sortBy=forecast" class="tableLink">Max Payout</a></th>
	  </tr>
<%                  NumberFormat nf = NumberFormat.getCurrencyInstance();
                    nf.setMaximumFractionDigits(0);
                    NumberFormat pnf = NumberFormat.getPercentInstance();
                    pnf.setMaximumFractionDigits(0);
              if (reportBeans.size() > 0){
                    for (int i=0; i < reportBeans.size(); i++){
                    TargetMarketReportBean tmrBean = (TargetMarketReportBean) reportBeans.get(i);
                    String cellShade = "";
                    if(i%2==0){
                            cellShade=" class=\"cellShade\"";
                    }else{
                            cellShade="";
                    }
%>
            <tr<%= cellShade %>>
                        <td>
	  			<div align="left"><a href="<%= href %><%= tmrBean.getId() %>"><%= tmrBean.getDescription() %></a></div>
	  		</td>
<%                      if (reportType.equalsIgnoreCase("Account")){
%>
                        <td>
	  			<div align="right"><%= tmrBean.getSubDescription() %></div>
	  		</td>
<%                      }
%>
                        <td>
	  			<div align="right"><%= nf.format(tmrBean.getSalesBaseline()) %></div>
	  		</td>
	  		<td>
	  			<div align="right"><%= nf.format(tmrBean.getSalesInPlan()) %></div>
  		 </td>
	  		<td>
	  			<div align="right"><%= pnf.format(tmrBean.getPercentageGrowth()) %></div>
	  		</td>
	  		<td>
	  			<div align="right"><%= nf.format(tmrBean.getGrowthPayout()) %></div>
	  		</td>
	  		<td>
	  			<div align="right"><%= nf.format(tmrBean.getForecastMaxPayout()) %></div>
	  		</td>
  		</tr>
  		
<%              } %>
		</table>
<%		
		} else {
%>
		</table><br>
		<b>There is currently no sales data for Target Market plans selected for this report </b>
<%		}  %>

	  	
	
   </td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>