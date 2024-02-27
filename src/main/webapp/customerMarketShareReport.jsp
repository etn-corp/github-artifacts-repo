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
	     ArrayList allGeogs = (ArrayList) request.getAttribute("allGeogs");
	     ArrayList specialPrograms = (ArrayList) request.getAttribute("specialPrograms");
	     ArrayList divisions = (ArrayList)request.getAttribute("divisions");
	
	
	%>
	<!-- Main Text	-->
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750">
				<p><br>
					<a class="crumb" href="SMRCHome">Home Page</a>
					<span class="crumbarrow">&gt;</span>
					<span class="crumbcurrent">Customer Market Share Report</span>
				</p>
			</td>
		</tr>
		<tr>
			<td>
				&nbsp;
			</td>
		</tr>
		<tr>
			<td>
				&nbsp;
			</td>
		</tr>
	</table>
<form action="CustomerMarketShareReport" name=cmsReport onSubmit='return validateCMSReportForm(this);'>
	<input type = "hidden" name = "page" value = "cmsReport">
	<table align=center width="800">
		<tr class=cellColor>
			<td valign=top class=question bgcolor="#F8F8FF"><span class="textnormal">How would you like the results grouped?</span></td>
			<td valign=top>
				<table cellpadding=2 cellspacing=2 width="100%">
					<tr>
						<td class=smallFontCU>By Customer</td>
						<td class=smallFontCU>By Parents</td>
						<td class=smallFontCU>By District</td>
						<td class=smallFontCU>By Team </td>
						<td class=smallFontCU>By Zone</td>
						<td class=smallFontCU>By Primary<br>Segment </td>
						<td class=smallFontCU>By Secondary<br>Segment </td>
						<td class=smallFontCU>By Product<br>Line</td>
						<td class=smallFontCU>By Division</td>
						<td class=smallFontCU>By Sales<br>Engineer</td>
					</tr>
					<tr>
						<td class=smallFontC><input type=checkbox name=viewAcct onClick="clickCustomerMarketShareViewAcct(cmsReport)"></td>
						<td class=smallFontC><input type=checkbox name=viewParents onClick="clickCustomerMarketShareViewParents(cmsReport)"></td>
						<td class=smallFontC><input type=checkbox name=viewDistrict onClick="clickCustomerMarketShareViewDistrict(cmsReport)"></td>
						<td class=smallFontC><input type=checkbox name=viewTeam onClick="clickCustomerMarketShareViewTeam(cmsReport)"></td>
						<td class=smallFontC><input type=checkbox name=viewZone onClick="clickCustomerMarketShareViewZone(cmsReport)"></td>
						<td class=smallFontC><input type=checkbox name=viewPrimarySegment onClick="clickCustomerMarketShareViewPrimarySegment(cmsReport)"></td>
						<td class=smallFontC><input type=checkbox name=viewSecondarySegment onClick="clickCustomerMarketShareViewSecondarySegment(cmsReport)"></td>
						<td class=smallFontC><input type=checkbox name=viewProd onClick="clickCustomerMarketShareViewProd(cmsReport)"></td>
						<td class=smallFontC><input type=checkbox name=viewDivision onClick="clickCustomerMarketShareViewDivision(cmsReport)"></td>
						<td class=smallFontC><input type=checkbox name=viewSE onClick="clickCustomerMarketShareViewSE(cmsReport)"></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr class=cellColor>
			<td valign=top class=question bgcolor="#F8F8FF"><span class="textnormal">Search the above but also filter the results with these restrictions.</span></td>
			<td valign=top>
				<table>
					<tr valign="top">
						<td class=smallFont align = "right">Product</td>
						<td><select name=product class=smallFont size=4 multiple>
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
						<td class=smallFont align = "right">Product Division</td>
						<td><select name=division class=smallFont size=4 multiple>
								<option value='' selected>All</option>
								<%
								
										for (int i=0; i < divisions.size(); i++) {
											Division currentDivision = (Division) divisions.get(i);
								%>
								<option value='<%= currentDivision.getId() %>'><%= currentDivision.getName() %></option>
								<%		}
								%>
							</select>
						</td>
						
					</tr>
					<tr><td></td></tr>
					<tr valign="top">
						<td class=smallFont align = "right">Geography</td>
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
						<td class=smallFont align = "right">Focus Types /<br>&nbsp;&nbsp;Account Coverage</td>
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
					</tr>
					<tr><td></td></tr>
					<tr valign="top">
						<td class=smallFont align = "right">Special Program(s)</td>
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
						<td class=smallFont align = "right">Application</td>
						<td><select name=applications class=smallFont size=4 multiple>
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
					<tr><td></td></tr>
					<tr valign="top">
						<td class=smallFont align = "right">Segments</td>
						<td colspan="3">
							<% Collection segments = (Collection)request.getAttribute("segments"); %>
							<%@ include file="/segmentSelectTree.jsp" %>
						</td>
					</tr>
					<tr><td></td></tr>
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
