<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ include file="analytics.jsp" %>
<html>
	<%
	     ArrayList prods = (ArrayList) request.getAttribute("prods");
	     ArrayList rollUpGeogs = (ArrayList) request.getAttribute("rollUpGeogs");
	     ArrayList users = (ArrayList) request.getAttribute("users");
	     ArrayList goodSegments = (ArrayList) request.getAttribute("goodSegments");
	
	%>
	<%@ include file="./TAPheader.jsp" %>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750">
				<p><br>
					<a class="crumb" href="SMRCHome">Home Page</a>
					<span class="crumbarrow">&gt;</span>
					<span class="crumbcurrent">Sample Reports</span>
				</p>
			</td>
		</tr>
	</table>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750" valign="top">
						<p>&nbsp;</p>
				<p class=heading2>Product Sample Report</p>
				<form action=AcctPlanReport name=sampleReport>
					<input type=hidden name=page value=sampleRpt>
					<table cellspacing=1 cellpadding=1 class=tableBorder>
						<tr class=cellColor>
							<td class=textnormal bgcolor="#F8F8FF">Get all samples after which month?</td>
							<%
									Calendar startDt = Calendar.getInstance();
									Calendar stopDt = Calendar.getInstance();
									startDt.set(Calendar.MONTH,8);
									startDt.set(Calendar.YEAR,2001);
							%>
							<td><select name=startDt class=smallFontL>
									<%
											while (startDt.before(stopDt)) {
									%> <option value="<%=startDt.get(Calendar.YEAR)%><%= (startDt.get(Calendar.MONTH) + 1) %>"><%= (startDt.get(Calendar.MONTH) + 1) %>/<%=  startDt.get(Calendar.YEAR) %></option>
									<%			startDt.add(Calendar.MONTH,1);
											}
									%>
								</select></td>
						</tr>
						<tr class=cellColor>
							<td class=textnormal bgcolor="#F8F8FF">Enter the Vista Customer Number</td>
							<td class=smallFont><input name=vcn size=6 maxlength=6>(leave blank for all)</td>
						</tr>
						<tr class=cellColor>
							<td class=textnormal bgcolor="#F8F8FF">Select the Catalog Number</td>
							<td><select name=catNum class=smallFont>
									<option value=''>All</option>
									<%
											for (int i=0; i < prods.size(); i++) {
												String catNum = (String)prods.get(i);
									%>
									<option class="smallFont" value='<%= catNum %>'><%= catNum %></option>
									<%		}
									%>
								</select>
							</td>
						</tr>
						<tr class=cellColor>
							<td class=textnormal bgcolor="#F8F8FF">Enter the City the product was shipped to</td>
							<td><input name=city size=20 maxlength=35></td>
						</tr>
						<tr class=cellColor>
							<td class=textnormal bgcolor="#F8F8FF">Enter the State the product was shipped to</td>
							<td><input name=state size=2 maxlength=2></td>
						</tr>
						<tr class=cellColor>
							<td class=textnormal bgcolor="#F8F8FF">Which geography do you want to see?</td>
							<td>
								<select name=geog class=smallFont>
									<%
											for (int i=0; i < rollUpGeogs.size(); i++) {
												Region seg = (Region)rollUpGeogs.get(i);
									%>
									<option value='<%= seg.getSPGeog() %>'><%= seg.getRegion() %>-<%= seg.getSegment() %> <%= seg.getDescription() %></option>
									<%		}
											for (int i=0; i < goodSegments.size(); i++) {
												Region seg = (Region) goodSegments.get(i);
									%>
									<option value='<%= seg.getSPGeog() %>'><%= seg.getRegion() %>-<%= seg.getSegment() %> <%= seg.getDescription() %></option>
									<%		}
									%>
								</select>
							</td>
						</tr>
						<tr class=cellColor>
							<td class="textnormal" bgcolor="#F8F8FF">Select the user that made the sample request&nbsp;</td>
							<td><select name="requestor" class="smallFont">
									<option value=''>All</option>
									<%
											for (int i=0; i < users.size(); i++) {
												User thisUser = (User)users.get(i);
									%> <option value='<%= thisUser.getUserid() %>'><%= thisUser.getLastName() %>, <%= thisUser.getFirstName() %></option>
									<%		}
									%>
								</select>
							</td>
						</tr>
						<tr class=cellColor><td colspan=2 align=center><input type=submit name=submit value=Submit class=smallFont>&nbsp;<input type=reset name=reset value=Reset class=smallFont></td></tr>
					</table>
				</form>
			</td>
		</tr>
	</table>
	</body>
</html>
