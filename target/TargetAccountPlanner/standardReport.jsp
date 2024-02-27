<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.dao.*" %>
<html>
	<%@ include file="./SMRCHeader.jsp" %>
	<%@ include file="analytics.jsp" %>
	<%
	     ArrayList focusTypes = (ArrayList) request.getAttribute("focusTypes");
	     ArrayList products = (ArrayList) request.getAttribute("products");
	     ArrayList applications = (ArrayList) request.getAttribute("applications");
	     String acctDisable = (String) request.getAttribute("acctDisable");
	     String groupDisable = (String) request.getAttribute("groupDisable");
	     String zoneDisable = (String) request.getAttribute("zoneDisable");
	     String districtDisable = (String) request.getAttribute("districtDisable");
	     String teamDisable = (String) request.getAttribute("teamDisable");
	     String seDisable = (String) request.getAttribute("seDisable");
	     String salesorgDisable = (String) request.getAttribute("salesorgDisable");
	     ArrayList allGeogs = (ArrayList) request.getAttribute("allGeogs");
	     ArrayList specialPrograms = (ArrayList) request.getAttribute("specialPrograms");
	
	
	%>
	<!-- Main Text	-->
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750">
				<p><br>
					<a class="crumb" href="SMRCHome">Home Page</a>
					<span class="crumbarrow">&gt;</span>
					<span class="crumbcurrent">Standard Reports</span>
				</p>
			</td>
		</tr>
	</table>
	<table align=center width="100%">
		<tr width="100">
			<td width="100%" colspan=2>
				<form action=StandardReport name=dolReport onSubmit='return validateReportForm(this);'>
				<input type=hidden name=page value=results>
				<br>
				<p class="heading2">Standard Reports </p>
				<table class=innerborder border=0 cellpadding=1 cellspacing=1>
					<tr class=cellColor>
						<td valign=top class=question width="25%" bgcolor="#F8F8FF"><span class="textnormal">Please select the dollar types you'd like to see from the options on the right.
								<br><br>And select the month/year you'd like the data to represent </span><select name=rptMonth class=smallFont>
								<%
										boolean cont = true;
										Calendar breakDate = Calendar.getInstance();
										int breakYear = (breakDate.get(Calendar.YEAR) - 2);
										breakDate.set(breakYear,0,1);		// Sets the earliest date to Jan. 1, 2001
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
							
							<br><br>
								     <b>TAP Dollars</b> - <a href="javascript:openPopup('TapDollarsExplainPopup.jsp', 'tapDollars', '200', '400')" >What are TAP Dollars?</a>
						</td>
						<td valign=top class=smallFontC><table width="100%">
					</tr>
					<tr >
						
						<td class=smallFontC >
							<table class=innerBorder width="40%" cellspacing=1 cellpadding=1 >
								<caption class=columnHeader-acctProfile>Tap Dollars</caption>
								<tr class=innerColor><td class=smallFontC valign=top>Sales/Orders?</td><td class=smallFontC valign=top><select name=em_sal_ord multiple size=2 class=smallFontC><option value=sal class=smallFontC>Sales</option><option value=ord class=smallFontC>Orders</option></select></td></tr>
								<tr class=innerColor><td class=smallFontC valign=top>Monthly $</td><td class=smallFontC valign=top><input type=checkbox name=emsal_curmo></td></tr>
								<tr class=innerColor><td class=smallFontC valign=top>YTD for Month</td><td class=smallFontC valign=top><input type=checkbox name=emsal_curytd></td></tr>
								<tr class=innerColor><td class=smallFontC valign=top>Previous Year YTD</td><td class=smallFontC valign=top><input type=checkbox name=emsal_prevytd></td></tr>
								<tr class=innerColor><td class=smallFontC valign=top>Previous Year Total</td><td class=smallFontC valign=top><input type=checkbox name=emsal_prevtot></td></tr>
								<tr class=innerColor><td class=smallFontC valign=top>Prev Year's Monthly $</td><td class=smallFontC><input type=checkbox name=emsal_mtm></td>
							</table>
						</td>

					</tr>
					 <tr>
						<td align="center">
							<table width="40%" ><tr>
									<td class=smallFontC width="25%">Potential Dollars: <input type=checkbox name=showPotential></td>
									<td class=smallFontC width="25%">Forecast Dollars: <input type=checkbox name=showForcast></td>
									<td class=smallFontC width="25%">Competitor Dollars: <input type=checkbox name=showCompetitor></td>
								</tr></table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr class=cellColor>
			<td valign=top class=question bgcolor="#F8F8FF"><span class="textnormal">How would you like the results grouped?</span></td>
			<td valign=top>
				<table cellpadding=2 cellspacing=2 width="100%">
					<tr>
						<td class=smallFontCU>By Account</td>
						<td class=smallFontCU>By Parents</td>
						<td class=smallFontCU>By Product Line</td>
						<td class=smallFontCU>By Summary Product Line </td>
						<td class=smallFontCU>By Sales Org </td>
						<td class=smallFontCU>By Group Code </td>
						<td class=smallFontCU>By Zone</td>
						<td class=smallFontCU>By District</td>
						<td class=smallFontCU>By Team </td>
						<td class=smallFontCU>By Sales Engineer</td>
						<td class=smallFontCU>By Primary Segment </td>
						<td class=smallFontCU>By Secondary Segment </td>
					</tr>
					<tr>
						<td class=smallFontC><input type=checkbox name=viewAcct onClick="clickAccountReport(dolReport)" <%= acctDisable %>></td>
						<td class=smallFontC><input type=checkbox name=viewParents onClick="clickParentReport(dolReport)" <%= acctDisable %>></td>
						<td class=smallFontC><input type=checkbox name=viewProd onClick="clickProductReport(dolReport)"></td>
						<td class=smallFontC><input type=checkbox name=viewSummaryProductline onClick="clickSummaryProductlineReport(dolReport)"></td>
						<td class=smallFontC><input type=checkbox name=viewSalesOrg onClick="clickSalesOrgReport(dolReport)" <%= salesorgDisable %>></td>
						<td class=smallFontC><input type=checkbox name=viewGroupCode onClick="clickGroupCodeReport(dolReport)" <%= groupDisable %>></td>
						<td class=smallFontC><input type=checkbox name=viewZone onClick="clickZoneReport(dolReport)" <%= zoneDisable %>></td>
						<td class=smallFontC><input type=checkbox name=viewDistrict onClick="clickDistrictReport(dolReport)" <%= districtDisable %>></td>
						<td class=smallFontC><input type=checkbox name=viewTeam onClick="clickTeamReport(dolReport)" <%= teamDisable %>></td>
						<td class=smallFontC><input type=checkbox name=viewSE onClick="clickSEReport(dolReport)" <%= seDisable %>></td>
						<td class=smallFontC><input type=checkbox name=viewPrimarySegment onClick="clickPrimarySegmentReport(dolReport)"></td>
						<td class=smallFontC><input type=checkbox name=viewSecondarySegment onClick="clickSecondarySegmentReport(dolReport)"></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr class=cellColor>
			<td valign=top class=question bgcolor="#F8F8FF"><span class="textnormal">Search the above but also filter the results with these restrictions.</span></td>
			<td valign=top>
				<table>
					<tr valign="top">
						<td class=smallFont>Focus Types / Account Coverage</td>
						<td><select name=ftype class=smallFont size=4 multiple>
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
						<td class=smallFont>Application</td>
						<td><select name=app1ications class=smallFont size=4 multiple>
								<option value='' selected>All</option>
								<%
								
										for (int i=0; i < applications.size(); i++) {
											CodeType thisApp = (CodeType) applications.get(i);
								%>
								<option value='<%= thisApp.getId() %>'><%= thisApp.getDescription() %></option>
								<%		}
								%>
							</select>
						</td>
					</tr>
					<tr valign="top">
						<td class=smallFont>Product</td>
						<td><select name=product class=smallFont size=4 multiple>
								<option value='' selected>All</option>
								<%
								
										for (int i=0; i < products.size(); i++) {
											Product prod = (Product)products.get(i);
								%>
								<option value='<%= prod.getId() %>'><%= prod.getId()+" - "+prod.getDescription()+" - "+prod.getDivision().getName() %></option>
								<%		}
								%>
							</select>
						</td>
						<td class=smallFont>Geography</td>
						<td><select name=geography class=smallFont size=4 multiple>
								<option value='' selected>All</option>
								<%
								
										for (int i=0; i < allGeogs.size(); i++) {
											Geography geog = (Geography) allGeogs.get(i);
								%>
								<option value='<%= geog.getGeog() %>'><%= geog.getGeog() %> - <%= geog.getDescription() %></option>
								<%		}
								%>
							</select>
						</td>
					</tr>
					<tr valign="top">
						<td class=smallFont>Special Program(s)</td>
						<td><select name=specialPrograms class=smallFont size=4 multiple>
								<option value='' selected>All</option>
								<%
								
										for (int i=0; i < specialPrograms.size(); i++) {
											DropDownBean specialProgram = (DropDownBean) specialPrograms.get(i);
								%>
								<option value='<%= specialProgram.getValue() %>'><%= specialProgram.getName() %></option>
								<%		}
								%>
							</select>
						</td>
						<td colspan=2>&nbsp;</td>
					</tr>
					<tr valign="top">
						<td class=smallFont>Segments</td>
						<td colspan="3">
							<% Collection segments = (Collection)request.getAttribute("segments"); %>
							<%@ include file="./segmentSelectTree.jsp" %>
						</td>
					</tr>
					<tr>
						<td class=smallFontC colspan=4>Target Accounts Only? <input type=checkbox name="targetAcctOnly"></td>
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
