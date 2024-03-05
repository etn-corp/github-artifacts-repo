<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<html>


<%@ include file="./TAPheader.jsp" %>
<%@ include file="analytics.jsp" %>
 
<%
     SalesGroup sGroup = header.getSalesGroup(); 
    // ArrayList industries = (ArrayList) request.getAttribute("industries");
     ArrayList focusTypes = (ArrayList) request.getAttribute("focusTypes");
     ArrayList products = (ArrayList) request.getAttribute("products");
     ArrayList sublines = (ArrayList) request.getAttribute("sublines");
     ArrayList regions = (ArrayList) request.getAttribute("regions");
     ArrayList segments = (ArrayList) request.getAttribute("segments");
     ArrayList sicCodes = (ArrayList) request.getAttribute("sicCodes");

%>

<!-- Main Text	-->
<table align=center width="100%">
    <tr width="100">
        <td width="100%" colspan=2>
            <form action=AcctPlanReport name=dolReport onSubmit='return validateReportForm(this);'>
		<input type=hidden name=groupId value='<%= header.getSalesGroup().getId() %>'>
		<input type=hidden name=page value=results>
		<table class=tableBorder border=0 cellpadding=2 cellspacing=2>
		<caption class=columnheader-center>Standard Reports</caption>
			<tr class=cellColor>
				<td valign=top class=question width="25%" bgcolor="#F8F8FF">Please select the dollar types you'd like to see from the options on the right.
                                            <br><br>And select the month/year you'd like the data to represent <select name=rptMonth class=smallFont>
<%
		boolean cont = true;
		Calendar breakDate = Calendar.getInstance();
		breakDate.set(2001,0,1);		// Sets the earliest date to Jan. 1, 2001
		Calendar testDt = Calendar.getInstance();

		while (cont) {
%>
			<option value="<%= testDt.get(Calendar.YEAR)%><%=(testDt.get(Calendar.MONTH) + 1) %>" class="smallFont"><%= (testDt.get(Calendar.MONTH) + 1) %>/<%= testDt.get(Calendar.YEAR) %></option>
<%
			testDt.add(Calendar.MONTH,-1);

			if (testDt.get(Calendar.YEAR) < breakDate.get(Calendar.YEAR) ||
				(testDt.get(Calendar.YEAR) == breakDate.get(Calendar.YEAR) &&
					testDt.get(Calendar.MONTH) < breakDate.get(Calendar.MONTH)))
			{
				cont = false;
			}
		}
%>
                                    </select>
                                </td>
                                <td valign=top class=smallFontC><table width="100%">
                            </tr>
                            <tr>
<%
		if (sGroup.usesSSO()) {
%>			<td class=smallFontC>
                            <table class="innerBorder" width="100%" cellspacing="1" cellpadding=1>
                            <caption class="columnHeader-acctProfile">SSO Dollars</caption>
				<tr class="innerColor"><td class="smallFontC" valign="top">Sales/Orders?</td>
                                    <td class="smallFontC" valign="top">
                                        <select name="sso_sal_ord" multiple size="2" class="smallFontC">
                                            <option value="sal" class="smallFontC" selected>SSO</option>
                                        </select>
                                    </td>
                                </tr>
				<tr class="innerColor">
                                    <td class="smallFontC" valign="top">Monthly $</td>
                                    <td class="smallFontC" valign="top"><input type="checkbox" name="sso_curmo"></td>
                                </tr>
				<tr class="innerColor">
                                    <td class="smallFontC" valign="top">YTD for Month</td>
                                    <td class="smallFontC" valign="top"><input type="checkbox" name="sso_curytd"></td>
                                </tr>
				<tr class="innerColor">
                                    <td class="smallFontC" valign="top">Previous Year YTD</td>
                                    <td class="smallFontC" valign="top"><input type="checkbox" name="sso_prevytd"></td>
                                </tr>
                                <tr class="innerColor">
                                    <td class="smallFontC" valign="top">Previous Year Total</td>
                                    <td class="smallFontC" valign="top"><input type="checkbox" name="sso_prevtot"></td>
                                </tr>
				<tr class="innerColor">
                                    <td class="smallFontC" valign="top">Prev Year's Monthly $</td>
                                    <td class="smallFontC" valign="top"><input type="checkbox" name="sso_mtm"></td>
                                </tr>
                            </table>
			</td>
<%		}
%>
                                <td class=smallFontC>
                                    <table class=innerBorder width="100%" cellspacing=1 cellpadding=1>
                                    <caption class=columnHeader-acctProfile>Credit</caption>
                                        <tr class=innerColor><td class=smallFontC valign=top>Sales/Orders?</td><td class=smallFontC valign=top><select name=cred_sal_ord multiple size=2 class=smallFontC><option value=sal class=smallFontC>Sales</option><option value=ord class=smallFontC>Orders</option></select></td></tr>
                                        <tr class=innerColor><td class=smallFontC valign=top>Monthly $</td><td class=smallFontC valign=top><input type=checkbox name=credsal_curmo></td></tr>
                                        <tr class=innerColor><td class=smallFontC valign=top>YTD for Month</td><td class=smallFontC valign=top><input type=checkbox name=credsal_curytd></td></tr>
                                        <tr class=innerColor><td class=smallFontC valign=top>Previous Year YTD</td><td class=smallFontC valign=top><input type=checkbox name=credsal_prevytd></td></tr>
    					<tr class=innerColor><td class=smallFontC valign=top>Previous Year Total</td><td class=smallFontC valign=top><input type=checkbox name=credsal_prevtot></td></tr>
					<tr class=innerColor><td class=smallFontC valign=top>Prev Year's Monthly $</td><td class=smallFontC valign=top><input type=checkbox name=credsal_mtm></td></tr>
                                    </table>
				</td>
				<td class=smallFontC>
                                    <table class=innerBorder width="100%" cellspacing=1 cellpadding=1>
                                    <caption class=columnHeader-acctProfile>End Market</caption>
                                        <tr class=innerColor><td class=smallFontC valign=top>Sales/Orders?</td><td class=smallFontC valign=top><select name=em_sal_ord multiple size=2 class=smallFontC><option value=sal class=smallFontC>Sales</option><option value=ord class=smallFontC>Orders</option></select></td></tr>
					<tr class=innerColor><td class=smallFontC valign=top>Monthly $</td><td class=smallFontC valign=top><input type=checkbox name=emsal_curmo></td></tr>
					<tr class=innerColor><td class=smallFontC valign=top>YTD for Month</td><td class=smallFontC valign=top><input type=checkbox name=emsal_curytd></td></tr>
					<tr class=innerColor><td class=smallFontC valign=top>Previous Year YTD</td><td class=smallFontC valign=top><input type=checkbox name=emsal_prevytd></td></tr>
					<tr class=innerColor><td class=smallFontC valign=top>Previous Year Total</td><td class=smallFontC valign=top><input type=checkbox name=emsal_prevtot></td></tr>
					<tr class=innerColor><td class=smallFontC valign=top>Prev Year's Monthly $</td><td class=smallFontC><input type=checkbox name=emsal_mtm></td>
                                    </table>
				</td>
				<td class=smallFontC>
                                    <table class=innerBorder width="100%" cellspacing=1 cellpadding=1>
                                    <caption class=columnHeader-acctProfile>Charge To</caption>
					<tr class=innerColor><td class=smallFontC valign=top>Sales/Orders?</td><td class=smallFontC valign=top><select name=dir_sal_ord multiple size=2 class=smallFontC><option value=sal class=smallFontC>Sales</option><option value=ord class=smallFontC>Orders</option></select></td></tr>
					<tr class=innerColor><td class=smallFontC valign=top>Monthly $</td><td class=smallFontC valign=top><input type=checkbox name=dirsal_curmo></td></tr>
					<tr class=innerColor><td class=smallFontC valign=top>YTD for Month</td><td class=smallFontC valign=top><input type=checkbox name=dirsal_curytd></td></tr>
					<tr class=innerColor><td class=smallFontC valign=top>Previous Year YTD</td><td class=smallFontC valign=top><input type=checkbox name=dirsal_prevytd></td></tr>
					<tr class=innerColor><td class=smallFontC valign=top>Previous Year Total</td><td class=smallFontC valign=top><input type=checkbox name=dirsal_prevtot></td></tr>
					<tr class=innerColor><td class=smallFontC valign=top>Prev Year's Monthly $</td><td class=smallFontC><input type=checkbox name=dirsal_mtm></td>
                                    </table>
        			</td>
                            </tr>
                            <tr>

<%
		if (sGroup.usesSSO()) {
%>			<td class=smallFontC>&nbsp;</td>
<%		}
%>
                                <td class=smallFontC>Potential Dollars: <input type=checkbox name=showPot></td>
                                <td class=smallFontC>Forecast Dollars: <input type=checkbox name=showFor></td>
                                <td class=smallFontC>Competitor Dollars: <input type=checkbox name=showComp></td>
                            </tr>
                    </table>
                </td>
            </tr>
            <tr class=cellColor>
                <td valign=top class=question bgcolor="#F8F8FF">How would you like the results grouped?</td>
                <td valign=top>
                    <table cellpadding=2 cellspacing=2 width="100%">
                        <tr>
                            <td class=smallFontCU>By Account</td>
                            <td class=smallFontCU>By Parents</td>
                            <td class=smallFontCU>By Industry</td>
                            <td class=smallFontCU>By Product</td>
                            <td class=smallFontCU>By Product Subline</td>
                            <td class=smallFontCU>By Zone</td>
                            <td class=smallFontCU>By District</td>
                            <td class=smallFontCU>By SIC Code</td>
                            <td class=smallFontCU>By Sales Engineer</td>
                        </tr>
                        <tr>
                            <td class=smallFontC><input type=checkbox name=viewAcct onClick=clickAccountReport(dolReport)></td>
                            <td class=smallFontC><input type=checkbox name=viewParents onClick=clickParentReport(dolReport)></td>
                            <td class=smallFontC><input type=checkbox name=viewInd onClick=clickIndustryReport(dolReport)></td>
                            <td class=smallFontC><input type=checkbox name=viewProd onClick=clickProductReport(dolReport)></td>
                            <td class=smallFontC><input type=checkbox name=viewPS onClick=clickPSReport(dolReport)></td>
                            <td class=smallFontC><input type=checkbox name=viewRegion onClick=clickRegionReport(dolReport)></td>
                            <td class=smallFontC><input type=checkbox name=viewSegment onClick=clickSegmentReport(dolReport)></td>
                            <td class=smallFontC><input type=checkbox name=viewSIC onClick=clickSICReport(dolReport)></td>
                            <td class=smallFontC><input type=checkbox name=viewSE onClick=clickSEReport(dolReport)></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr class=cellColor>
                <td valign=top class=question bgcolor="#F8F8FF">Search the above but also filter the results with these restrictions.</td>
                <td valign=top>
                    <table>
                        <tr>
                            <td class=smallFont>Industry</td>
                            <td class=smallFont><select name=industry class=smallFont multiple>
                                <option value='' selected>All</option>

<%
		for (int i=0; i < industries.size(); i++) {
			IndustrySubgroup ind = (IndustrySubgroup)industries.get(i);

			String asterisk = "";

			if (ind.getFocus() != null && ind.getFocus().equals("Y")) {
				asterisk = "* ";
			}
%>
			<option value='<%= ind.getSubgroupId() %>'><%= asterisk + ind.getDescription() %></option>
<%		}
%>
                                </select>
                                * Focus Industry
                            </td>
                            <td class=smallFont>Focus Types / Account Coverage</td>
                            <td><select name=ftype class=smallFont multiple>
                                <option value='' selected>All</option>

<%
		
		for (int i=0; i < focusTypes.size(); i++) {
			FocusType fType = (FocusType)focusTypes.get(i);
%>
			<option value='<%= fType.getId() %>'><%= fType.getDescription() %></option>
<%		}
%>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class=smallFont>Product</td>
                            <td><select name=product class=smallFont multiple>
                                <option value='' selected>All</option>

<%
		
		for (int i=0; i < products.size(); i++) {
			Product prod = (Product)products.get(i);
%>
			<option value='<%= prod.getId() %>'><%=  prod.getDescription() %></option>
<%		}
%>
                                </select>
                            </td>
                            <td class=smallFont>Product Subline</td>
                            <td><select name=subline class=smallFont multiple>
                                <option value='' selected>All</option>
<%
		for (int i=0; i < sublines.size(); i++) {
			ProductSubline ps = (ProductSubline)sublines.get(i);
%>
			<option value='<%= ps.getId() %>'><%=  ps.getDescription() %></option>
<%		}
%>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class=smallFont>Zone</td>
                            <td><select name=region class=smallFont multiple>
                                <option value='' selected>All</option>
<%
		for (int i=0; i < regions.size(); i++) {
			Region reg = (Region)regions.get(i);

			if (header.getUser().ableToSee(reg.getSPGeog())) {
%>				<option value='<%= reg.getSPGeog() %>'><%= reg.getDescription() %></option>
<%			}
		}
%>
                                </select>
                            </td>
                            <td class=smallFont>District</td>
                            <td><select name=segment class=smallFont multiple>
                                <option value='' selected>All</option>
<%
		for (int i=0; i < segments.size(); i++) {
			Region seg = (Region)segments.get(i);

			if (header.getUser().ableToSee(seg.getSPGeog())) {
%>				<option value='<%=  seg.getSPGeog() %>'><%= seg.getRegion() %>-<%=  seg.getSegment() %> <%= seg.getDescription() %></option>

<%			}
		}
%>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class=smallFont>SIC Code</td>
                            <td colspan=3><select name=sic class=smallFont multiple>
                                <option value='' selected>All</option>
<%
		
		for (int i=0; i < sicCodes.size(); i++) {
			SICCode sic = (SICCode)sicCodes.get(i);
%>
			<option value='<%= sic.getSICCode() %>'><%=  sic.getDescription() %> (<%=  sic.getSICCode() %>)</option>
<%		}
%>
                                </select>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <br>
        <center>
        <input type=submit name=submit value=Submit class=smallFont>&nbsp;
        <input type=reset name=reset value=Reset class=smallFont></center>
        </form>
      </td>
    </tr>
</table>
</body>
</html>
