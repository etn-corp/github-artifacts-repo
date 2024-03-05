<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*" %>

<html>


<%@ include file="./TAPheader.jsp" %>
<%@ include file="analytics.jsp" %>
<%
        SalesGroup sGroup = header.getSalesGroup();  
        ArrayList results = (ArrayList) request.getAttribute("results");
        ArrayList assocResults = (ArrayList) request.getAttribute("assocResults");
        String orderType = (String) request.getAttribute("orderType");
        String salesmanName = (String) request.getAttribute("salesmanName");
        String sid = (String) request.getAttribute("sid");
        String [] ob = (String []) request.getAttribute("ob");
        Calendar rptDate = Calendar.getInstance();
	rptDate.add(Calendar.DATE,-7);


%>
<div class=columnheader-center>Salesperson Report for <%= salesmanName %></div>
<table align=center class=tableBorder border=0 cellpadding=1 cellspacing=1>
<caption class=notes>Dollars represent <%= sGroup.getDolDescription() %> (All dollars are in thousands for <%= (rptDate.get(Calendar.MONTH) + 1) %>/<%= rptDate.get(Calendar.YEAR) %>)</caption>
    <thead class=cellColor>
        <td><a href="AcctPlanReport?groupId=<%= sGroup.getId() %>&page=sidRpt&sid=<%= sid %>&orderBy=customer_name&orderType=<%= orderType %>" class=searchResHdrL>Customer</a></td>
        <td><a href="AcctPlanReport?groupId=<%= sGroup.getId() %>&page=sidRpt&sid=<%= sid %>&orderBy=pd&orderType=<%= orderType %>" class=searchResHdrR>Potential $</a></td>
        <td><a href="AcctPlanReport?groupId=<%= sGroup.getId() %>&page=sidRpt&sid=<%= sid %>&orderBy=fd&orderType=<%= orderType %>" class=searchResHdrR>Forecast $</a></td>
        <td><a href="AcctPlanReport?groupId=<%= sGroup.getId() %>&page=sidRpt&sid=<%= sid %>&orderBy=cd&orderType=<%= orderType %>" class=searchResHdrR>Competitor $</a></td>
	<td><a href="AcctPlanReport?groupId=<%= sGroup.getId() %>&page=sidRpt&sid=<%= sid %>&orderBy=<%= ob[0] %>&orderType=<%= orderType %>" class=searchResHdrR>Cur Month</a></td>
        <td><a href="AcctPlanReport?groupId=<%= sGroup.getId() %>&page=sidRpt&sid=<%= sid %>&orderBy=<%= ob[1] %>&orderType=<%= orderType %>" class=searchResHdrR>YTD</a></td>
        <td><a href="AcctPlanReport?groupId=<%= sGroup.getId() %>&page=sidRpt&sid=<%= sid %>&orderBy=<%= ob[2] %>&orderType=<%= orderType %>" class=searchResHdrR>Prev YTD</a></td>
        <td><a href="AcctPlanReport?groupId=<%= sGroup.getId() %>&page=sidRpt&sid=<%= sid %>&orderBy=<%= ob[3] %>&orderType=<%= orderType %>" class=searchResHdrR>Prev Yr Total</a></td>
        <td><a href="AcctPlanReport?groupId=<%= sGroup.getId() %>&page=sidRpt&sid=<%= sid %>&orderBy=<%= ob[4] %>&orderType=<%= orderType %>" class=searchResHdrR>Prev Yr Month</a></td>
<%
                if (sGroup.usesVolume()) {
%>                        <td class=searchResHdrR><a href="AcctPlanReport?groupId=<%= sGroup.getId() %>&page=sidRpt&sid=<%= sid %>&orderBy=v&orderType=<%= orderType %>" class="searchResHdrR">Volume</a></td>
<%              }
%>
    </thead>
<%
                double[] totalVals = new double[8];

                for (int i=0; i < totalVals.length; i++) {
                        totalVals[i] = 0;
                }

                int totalVolume = 0;

                for (int i=0; i < results.size(); i++) {
                        SearchResults thisRes = (SearchResults)results.get(i);
                        int x = 0;

                        StringBuffer linkParms = new StringBuffer("?page=productmix&cust=");
                        linkParms.append(thisRes.getId());
                        linkParms.append("&groupId=");
                        linkParms.append(sGroup.getId());
%>
                        <tr class=cellColor>
                            <td><a href="OEMAcctPlan<%= linkParms.toString() %>"><%= thisRes.getDescription() %></a></td>
                            <td class="searchRight"><%= thisRes.displayPotentialDollars() %></td>

<%                      totalVals[x++] += thisRes.getPotentialDollars(); 
%>
                            <td class=searchRight ><%= thisRes.displayForecastDollars() %></td>
<%
                        totalVals[x++] += thisRes.getForecastDollars();
%>
                            <td class=searchRight ><%= thisRes.displayCompetitorDollars() %></td>
<%
                        totalVals[x++] += thisRes.getCompetitorDollars();

                        if (sGroup.usesSales()) {
                                if (sGroup.usesCredit()) {
%>                                      <td class="searchRight"><%= thisRes.displayCRCurMoSales() %></td>
                                        <td class="searchRight"><%= thisRes.displayCRYTDSales() %></td>
                                        <td class="searchRight"><%= thisRes.displayCRPrevYTDSales() %></td>
                                        <td class="searchRight"><%= thisRes.displayCRPrevYrTotSales() %></td>
                                        <td class="searchRight"><%= thisRes.displayCRPrevYrMoSales() %></td>
<%
                                        totalVals[x++] += thisRes.getCRCurMoSales();
                                        totalVals[x++] += thisRes.getCRYTDSales();
                                        totalVals[x++] += thisRes.getCRPrevYTDSales();
                                        totalVals[x++] += thisRes.getCRPrevYrTotSales();
                                        totalVals[x++] += thisRes.getCRPrevYrMoSales();
                                }
                                else if (sGroup.usesDirect()) {
%>                                       <td class="searchRight"><%= thisRes.displayDirCurMoSales() %></td>
                                         <td class="searchRight"><%= thisRes.displayDirYTDSales() %></td>
                                         <td class="searchRight"><%= thisRes.displayDirPrevYTDSales() %></td>
                                         <td class="searchRight"><%= thisRes.displayDirPrevYrTotSales() %></td>
                                         <td class="searchRight"><%= thisRes.displayDirPrevYrMoSales() %></td>
<%
                                        totalVals[x++] += thisRes.getDirCurMoSales();
                                        totalVals[x++] += thisRes.getDirYTDSales();
                                        totalVals[x++] += thisRes.getDirPrevYTDSales();
                                        totalVals[x++] += thisRes.getDirPrevYrTotSales();
                                        totalVals[x++] += thisRes.getDirPrevYrMoSales();
                                }
                                else if (sGroup.usesEndMkt()) {
%>                                       <td class="searchRight"><%= thisRes.displayEMCurMoSales() %></td>
                                         <td class="searchRight"><%= thisRes.displayEMYTDSales() %></td>
                                         <td class="searchRight"><%= thisRes.displayEMPrevYTDSales() %></td>
                                         <td class="searchRight"><%= thisRes.displayEMPrevYrTotSales() %></td>
                                         <td class="searchRight"><%= thisRes.displayEMPrevYrMoSales() %></td>
<%
                                        totalVals[x++] += thisRes.getEMCurMoSales();
                                        totalVals[x++] += thisRes.getEMYTDSales();
                                        totalVals[x++] += thisRes.getEMPrevYTDSales();
                                        totalVals[x++] += thisRes.getEMPrevYrTotSales();
                                        totalVals[x++] += thisRes.getEMPrevYrMoSales();
                                }
                                else if (sGroup.usesSSO()) {
%>                                      <td class="searchRight"><%= thisRes.displaySSOCurMo() %></td>
                                        <td class="searchRight"><%= thisRes.displaySSOYTD() %></td>
                                        <td class="searchRight"><%= thisRes.displaySSOPrevYTD() %></td>
                                        <td class="searchRight"><%= thisRes.displaySSOPrevYrTot() %></td>
                                        <td class="searchRight"><%= thisRes.displaySSOPrevYrMo() %></td>
<%
                                        totalVals[x++] += thisRes.getSSOCurMoOrder();
                                        totalVals[x++] += thisRes.getSSOYTD();
                                        totalVals[x++] += thisRes.getSSOPrevYTD();
                                        totalVals[x++] += thisRes.getSSOPrevYrTot();
                                        totalVals[x++] += thisRes.getSSOPrevYrMo();
                                }
                        }
                        else if (sGroup.usesOrders()) {
                                if (sGroup.usesCredit()) {
%>                                      <td class="searchRight"><%= thisRes.displayCRCurMoOrder() %></td>
                                        <td class="searchRight"><%= thisRes.displayCRYTDOrder() %></td>
                                        <td class="searchRight"><%= thisRes.displayCRPrevYTDOrder() %></td>
                                        <td class="searchRight"><%= thisRes.displayCRPrevYrTotOrder() %></td>
                                        <td class="searchRight"><%= thisRes.displayCRPrevYrMoOrder() %></td>
<%
                                        totalVals[x++] += thisRes.getCRCurMoOrder();
                                        totalVals[x++] += thisRes.getCRYTDOrder();
                                        totalVals[x++] += thisRes.getCRPrevYTDOrder();
                                        totalVals[x++] += thisRes.getCRPrevYrTotOrder();
                                        totalVals[x++] += thisRes.getCRPrevYrMoOrder();
                                }
                                else if (sGroup.usesDirect()) {
%>                                      <td class="searchRight"><%= thisRes.displayDirCurMoOrder() %></td>
                                        <td class="searchRight"><%= thisRes.displayDirYTDOrder() %></td>
                                        <td class="searchRight"><%= thisRes.displayDirPrevYTDOrder() %></td>
                                        <td class="searchRight"><%= thisRes.displayDirPrevYrTotOrder() %></td>
                                        <td class="searchRight"><%= thisRes.displayDirPrevYrMoOrder() %></td>
<%
                                        totalVals[x++] += thisRes.getDirCurMoOrder();
                                        totalVals[x++] += thisRes.getDirYTDOrder();
                                        totalVals[x++] += thisRes.getDirPrevYTDOrder();
                                        totalVals[x++] += thisRes.getDirPrevYrTotOrder();
                                        totalVals[x++] += thisRes.getDirPrevYrMoOrder();
                                }
                                else if (sGroup.usesEndMkt()) {
%>                                      <td class="searchRight"><%= thisRes.displayEMCurMoOrder() %></td>
                                        <td class="searchRight"><%= thisRes.displayEMYTDOrder()  %></td>
                                        <td class="searchRight"><%= thisRes.displayEMPrevYTDOrder()  %></td>
                                        <td class="searchRight"><%= thisRes.displayEMPrevYrTotOrder()  %></td>
                                        <td class="searchRight"><%= thisRes.displayEMPrevYrMoOrder()  %></td>
<%
                                        totalVals[x++] += thisRes.getEMCurMoOrder();
                                        totalVals[x++] += thisRes.getEMYTDOrder();
                                        totalVals[x++] += thisRes.getEMPrevYTDOrder();
                                        totalVals[x++] += thisRes.getEMPrevYrTotOrder();
                                        totalVals[x++] += thisRes.getEMPrevYrMoOrder();
                                }
                                else if (sGroup.usesSSO()) {
%>                                      <td class="searchRight"><%= thisRes.displaySSOCurMo() %></td>
                                        <td class="searchRight"><%= thisRes.displaySSOYTD() %></td>
                                        <td class="searchRight"><%= thisRes.displaySSOPrevYTD() %></td>
                                        <td class="searchRight"><%= thisRes.displaySSOPrevYrTot() %></td>
                                        <td class="searchRight"><%= thisRes.displaySSOPrevYrMo() %></td>
<%
                                        totalVals[x++] += thisRes.getSSOCurMoOrder();
                                        totalVals[x++] += thisRes.getSSOYTD();
                                        totalVals[x++] += thisRes.getSSOPrevYTD();
                                        totalVals[x++] += thisRes.getSSOPrevYrTot();
                                        totalVals[x++] += thisRes.getSSOPrevYrMo();
                                }
                        }

                        if (sGroup.usesVolume()) {
%>                                <td class="searchRight"><%= thisRes.displayVolume() %></td>
<%                                totalVolume += thisRes.getVolume();
                        }
%>
                        </tr>
<%                }

                NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
                nf.setMinimumFractionDigits(0);
                nf.setMaximumFractionDigits(0);

                NumberFormat volF = NumberFormat.getNumberInstance(Locale.US);
                volF.setMinimumFractionDigits(0);
                volF.setMaximumFractionDigits(0);
%>
    <tr class=cellColor>
        <td><div class=linkLookalike>Totals</td>
<%
                for (int i=0; i < totalVals.length; i++) {
                        if(nf.format(0).equals("$1"));	// clearing nf so all 0's appear
%>                        <td class="searchRight"><%= nf.format(totalVals[i]) %></td>
<%                }

                if (sGroup.usesVolume()) {
%>                        <td class="searchRight"><%= volF.format(totalVolume) %></td>
<%                }
%>
    </tr>
</table>

<br>
<div class=columnheader-center>Customers <%= salesmanName %> is associated with</div>
<table align=center class=tableBorder border=0 cellpadding=1 cellspacing=1>
<caption class=notes>Dollars represent <%= sGroup.getDolDescription() %> (All dollars are in thousands for <%= (rptDate.get(Calendar.MONTH) + 1)%>/<%=  rptDate.get(Calendar.YEAR) %>)</caption>
    <thead class=cellColor>
        <td><a href="AcctPlanReport?groupId=<%= sGroup.getId() %>&page=sidRpt&sid=<%= sid %>&orderBy=customer_name&orderType=<%= orderType %>" class=searchResHdrL>Customer</a></td>
        <td><a href="AcctPlanReport?groupId=<%= sGroup.getId() %>&page=sidRpt&sid=<%= sid %>&orderBy=pd&orderType=<%= orderType %>" class=searchResHdrR>Potential $</a></td>
	<td><a href="AcctPlanReport?groupId=<%= sGroup.getId() %>&page=sidRpt&sid=<%= sid %>&orderBy=fd&orderType=<%= orderType %>" class=searchResHdrR>Forecast $</a></td>
        <td><a href="AcctPlanReport?groupId=<%= sGroup.getId() %>&page=sidRpt&sid=<%= sid %>&orderBy=cd&orderType=<%= orderType %>" class=searchResHdrR>Competitor $</a></td>
        <td><a href="AcctPlanReport?groupId=<%= sGroup.getId() %>&page=sidRpt&sid=<%= sid %>&orderBy=<%= ob[0] %>&orderType=<%= orderType %>" class=searchResHdrR>Cur Month</a></td>
        <td><a href="AcctPlanReport?groupId=<%= sGroup.getId() %>&page=sidRpt&sid=<%= sid %>&orderBy=<%= ob[1] %>&orderType=<%= orderType %>" class=searchResHdrR>YTD</a></td>
        <td><a href="AcctPlanReport?groupId=<%= sGroup.getId() %>&page=sidRpt&sid=<%= sid %>&orderBy=<%= ob[2] %>&orderType=<%= orderType %>" class=searchResHdrR>Prev YTD</a></td>
        <td><a href="AcctPlanReport?groupId=<%= sGroup.getId() %>&page=sidRpt&sid=<%= sid %>&orderBy=<%= ob[3] %>&orderType=<%= orderType %>" class=searchResHdrR>Prev Yr Total</a></td>
        <td><a href="AcctPlanReport?groupId=<%= sGroup.getId() %>&page=sidRpt&sid=<%= sid %>&orderBy=<%= ob[4] %>&orderType=<%= orderType %>" class=searchResHdrR>Prev Yr Month</a></td>
<%
                if (sGroup.usesVolume()) {
%>                        <td class="searchResHdrR"><a href="AcctPlanReport?groupId=<%= sGroup.getId() %>&page=sidRpt&sid=<%= sid %>&orderBy=v&orderType=<%= orderType %>" class="searchResHdrR">Volume</a></td>
<%                }
%>
    </thead>
<%

                totalVals = new double[8];

                for (int i=0; i < totalVals.length; i++) {
                        totalVals[i] = 0;
                }

                totalVolume = 0;

                for (int i=0; i < assocResults.size(); i++) {
                        SearchResults thisRes = (SearchResults) assocResults.get(i);
                        int x = 0;

                        StringBuffer linkParms = new StringBuffer("?page=productmix&cust=");
                        linkParms.append(thisRes.getId());
                        linkParms.append("&groupId=");
                        linkParms.append(sGroup.getId());
%>
                        <tr class="cellColor">
                        <td><a href="OEMAcctPlan<%= linkParms.toString() %>"><%= thisRes.getDescription() %></a></td>
                        <td class="searchRight"><%= thisRes.displayPotentialDollars() %></td>
<%                        totalVals[x++] += thisRes.getPotentialDollars();
%>
                        <td class="searchRight"><%= thisRes.displayForecastDollars() %></td>
<%                        totalVals[x++] += thisRes.getForecastDollars();
%>
                        <td class="searchRight"><%= thisRes.displayCompetitorDollars() %></td>
<%                        totalVals[x++] += thisRes.getCompetitorDollars();

                        if (sGroup.usesSales()) {
                                if (sGroup.usesCredit()) {
%>                                      <td class="searchRight"><%= thisRes.displayCRCurMoSales() %></td>
                                        <td class="searchRight"><%= thisRes.displayCRYTDSales() %></td>
                                        <td class="searchRight"><%= thisRes.displayCRPrevYTDSales() %></td>
                                        <td class="searchRight"><%= thisRes.displayCRPrevYrTotSales() %></td>
                                        <td class="searchRight"><%= thisRes.displayCRPrevYrMoSales() %></td>
<%
                                        totalVals[x++] += thisRes.getCRCurMoSales();
                                        totalVals[x++] += thisRes.getCRYTDSales();
                                        totalVals[x++] += thisRes.getCRPrevYTDSales();
                                        totalVals[x++] += thisRes.getCRPrevYrTotSales();
                                        totalVals[x++] += thisRes.getCRPrevYrMoSales();
                                }
                                else if (sGroup.usesDirect()) {
%>                                      <td class="searchRight"><%= thisRes.displayDirCurMoSales() %></td>
                                        <td class="searchRight"><%= thisRes.displayDirYTDSales() %></td>
                                        <td class="searchRight"><%= thisRes.displayDirPrevYTDSales() %></td>
                                        <td class="searchRight"><%= thisRes.displayDirPrevYrTotSales() %></td>
                                        <td class="searchRight"><%= thisRes.displayDirPrevYrMoSales() %></td>
<%
                                        totalVals[x++] += thisRes.getDirCurMoSales();
                                        totalVals[x++] += thisRes.getDirYTDSales();
                                        totalVals[x++] += thisRes.getDirPrevYTDSales();
                                        totalVals[x++] += thisRes.getDirPrevYrTotSales();
                                        totalVals[x++] += thisRes.getDirPrevYrMoSales();
                                }
                                else if (sGroup.usesEndMkt()) {
%>                                      <td class="searchRight"><%= thisRes.displayEMCurMoSales() %></td>
                                        <td class="searchRight"><%= thisRes.displayEMYTDSales() %></td>
                                        <td class="searchRight"><%= thisRes.displayEMPrevYTDSales() %></td>
                                        <td class="searchRight"><%= thisRes.displayEMPrevYrTotSales() %></td>
                                        <td class="searchRight"><%= thisRes.displayEMPrevYrMoSales() %></td>
<%
                                        totalVals[x++] += thisRes.getEMCurMoSales();
                                        totalVals[x++] += thisRes.getEMYTDSales();
                                        totalVals[x++] += thisRes.getEMPrevYTDSales();
                                        totalVals[x++] += thisRes.getEMPrevYrTotSales();
                                        totalVals[x++] += thisRes.getEMPrevYrMoSales();
                                }
                                else if (sGroup.usesSSO()) {
%>                                      <td class="searchRight"><%= thisRes.displaySSOCurMo() %></td>
                                        <td class="searchRight"><%= thisRes.displaySSOYTD() %></td>
                                        <td class="searchRight"><%= thisRes.displaySSOPrevYTD() %></td>
                                        <td class="searchRight"><%= thisRes.displaySSOPrevYrTot() %></td>
                                        <td class="searchRight"><%= thisRes.displaySSOPrevYrMo() %></td>
<%
                                        totalVals[x++] += thisRes.getSSOCurMoOrder();
                                        totalVals[x++] += thisRes.getSSOYTD();
                                        totalVals[x++] += thisRes.getSSOPrevYTD();
                                        totalVals[x++] += thisRes.getSSOPrevYrTot();
                                        totalVals[x++] += thisRes.getSSOPrevYrMo();
                                }
                        }
                        else if (sGroup.usesOrders()) {
                                if (sGroup.usesCredit()) {
%>                                      <td class="searchRight"><%= thisRes.displayCRCurMoOrder() %></td>
                                        <td class="searchRight"><%= thisRes.displayCRYTDOrder() %></td>
                                        <td class="searchRight"><%= thisRes.displayCRPrevYTDOrder() %></td>
                                        <td class="searchRight"><%= thisRes.displayCRPrevYrTotOrder() %></td>
                                        <td class="searchRight"><%= thisRes.displayCRPrevYrMoOrder() %></td>
<%
                                        totalVals[x++] += thisRes.getCRCurMoOrder();
                                        totalVals[x++] += thisRes.getCRYTDOrder();
                                        totalVals[x++] += thisRes.getCRPrevYTDOrder();
                                        totalVals[x++] += thisRes.getCRPrevYrTotOrder();
                                        totalVals[x++] += thisRes.getCRPrevYrMoOrder();
                                }
                                else if (sGroup.usesDirect()) {
%>                                      <td class="searchRight"><%= thisRes.displayDirCurMoOrder() %></td>
                                        <td class="searchRight"><%= thisRes.displayDirYTDOrder() %></td>
                                        <td class="searchRight"><%= thisRes.displayDirPrevYTDOrder() %></td>
                                        <td class="searchRight"><%= thisRes.displayDirPrevYrTotOrder() %></td>
                                        <td class="searchRight"><%= thisRes.displayDirPrevYrMoOrder() %></td>
<%
                                        totalVals[x++] += thisRes.getDirCurMoOrder();
                                        totalVals[x++] += thisRes.getDirYTDOrder();
                                        totalVals[x++] += thisRes.getDirPrevYTDOrder();
                                        totalVals[x++] += thisRes.getDirPrevYrTotOrder();
                                        totalVals[x++] += thisRes.getDirPrevYrMoOrder();
                                }
                                else if (sGroup.usesEndMkt()) {
%>                                      <td class="searchRight"><%= thisRes.displayEMCurMoOrder() %></td>
                                        <td class="searchRight"><%= thisRes.displayEMYTDOrder() %>"</td>
                                        <td class="searchRight"><%= thisRes.displayEMPrevYTDOrder() %></td>
                                        <td class="searchRight"><%= thisRes.displayEMPrevYrTotOrder() %></td>
                                        <td class="searchRight"><%= thisRes.displayEMPrevYrMoOrder() %></td>
<%
                                        totalVals[x++] += thisRes.getEMCurMoOrder();
                                        totalVals[x++] += thisRes.getEMYTDOrder();
                                        totalVals[x++] += thisRes.getEMPrevYTDOrder();
                                        totalVals[x++] += thisRes.getEMPrevYrTotOrder();
                                        totalVals[x++] += thisRes.getEMPrevYrMoOrder();
                                }
                                else if (sGroup.usesSSO()) {
%>                                      <td class="searchRight"><%= thisRes.displaySSOCurMo() %></td>
                                        <td class="searchRight"><%= thisRes.displaySSOYTD() %></td>
                                        <td class="searchRight"><%= thisRes.displaySSOPrevYTD() %></td>
                                        <td class="searchRight"><%= thisRes.displaySSOPrevYrTot() %></td>
                                        <td class="searchRight"><%= thisRes.displaySSOPrevYrMo() %></td>
<%
                                        totalVals[x++] += thisRes.getSSOCurMoOrder();
                                        totalVals[x++] += thisRes.getSSOYTD();
                                        totalVals[x++] += thisRes.getSSOPrevYTD();
                                        totalVals[x++] += thisRes.getSSOPrevYrTot();
                                        totalVals[x++] += thisRes.getSSOPrevYrMo();
                                }
                        }

                        if (sGroup.usesVolume()) {
%>                                <td class=searchRight><%= thisRes.displayVolume() %></td>
<%                                totalVolume += thisRes.getVolume();
                        }
%>
                        </tr>
<%                }

                nf = NumberFormat.getCurrencyInstance(Locale.US);
                nf.setMinimumFractionDigits(0);
                nf.setMaximumFractionDigits(0);

                volF = NumberFormat.getNumberInstance(Locale.US);
                volF.setMinimumFractionDigits(0);
                volF.setMaximumFractionDigits(0);
%>
    <tr class=cellColor>
        <td><div class=linkLookalike>Totals</td>
<%
                for (int i=0; i < totalVals.length; i++) {
                        if(nf.format(0).equals("$1"));	// clearing nf so all 0's appear
%>                          <td class="searchRight"><%= nf.format(totalVals[i]) %></td>
<%                }

                if (sGroup.usesVolume()) {
%>                          <td class="searchRight"><%= volF.format(totalVolume) %></td>
<%                }
%>
    </tr>
</table>

</body>
</html>
