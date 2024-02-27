<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="com.eaton.electrical.smrc.bo.*" %>
<%@ page import="java.math.*" %>
<%@ page import="java.text.*" %>
<html>
	<%@ include file="./TAPheader.jsp" %>
	<%@ include file="analytics.jsp" %>
	<%
	    StringBuffer filters = (StringBuffer) request.getAttribute("filters");
	    StringBuffer sortParms = (StringBuffer) request.getAttribute("sortParms");
	    BigDecimal chVal = (BigDecimal) request.getAttribute("chVal");
	    BigDecimal totVal = (BigDecimal) request.getAttribute("totVal");
	    ArrayList reportBeans = (ArrayList) session.getAttribute("reportBeans");
	    String searchString = request.getQueryString();
	
	%>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750">
				<p><br>
					<a class="crumb" href="SMRCHome">Home Page</a>
					<span class="crumbarrow">&gt;</span>
					<a class="crumb" href="AcctPlanProjRpt?page=projects">Project Reports</a>
					<span class="crumbarrow">&gt;</span>
					<span class="crumbcurrent">Project Reports Results</span>
				</p>
			</td>
		</tr>
	</table>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750" valign="top">
				<p>&nbsp;</p>
				<p class=heading2>Target Projects</p>
				<a href="AcctPlanProjRpt?<%= (searchString + "&excel=y") %>" target="blank.html">Export To Excel</a>
				<table width="100%" class=tableBorder cellspacing=1 cellpadding=1>
					<thead class=innerColor>
						<td class=heading3 width="10%">Filters</td>
						<td class=heading3 width="90%">Results</td>
					</thead>
					<tr class=cellColor>
						<%		// Filters go here    %>
						<td valign=top>
							<table width="100%">
								<%= filters.toString()%>
							</table>
						</td>
						<%
								// Results go here
						%>
						<td valign=top>
							<table class=innerBorder cellspacing=1 cellpadding=1 width="100%">
								<thead class=innerColor>
									<td valign="top"><a href="AcctPlanProjRpt?sortMeth=internal_status<%= sortParms.toString() %>"><span class=tocactive>Internal Status</span></a></td>
									<td valign="top"><a href="AcctPlanProjRpt?sortMeth=job_name<%= sortParms.toString() %>"><span class=tocactive>Name</span></a></td>
									<td valign="top"><a href="AcctPlanProjRpt?sortMeth=sp_geog<%= sortParms.toString() %>"><span class=tocactive>District</span></a></td>
									<td valign="top"><a href="AcctPlanProjRpt?sortMeth=status_id<%= sortParms.toString() %>"><span class=tocactive>Status</span></a></td>
									<td valign="top"><a href="AcctPlanProjRpt?sortMeth=strat_reason_id<%= sortParms.toString() %>"><span class=tocactive>Target Reason</span></a></td>
									<td valign="top"><a href="AcctPlanProjRpt?sortMeth=preference_id<%= sortParms.toString() %>"><span class=tocactive>Spec Preference</span></a></td>
									<td valign="top"><a href="AcctPlanProjRpt?sortMeth=bid_date<%= sortParms.toString() %>"><span class=tocactive>Bid Date</span></a></td>
									<td valign="top"><a href="AcctPlanProjRpt?sortMeth=ch_value<%= sortParms.toString() %>"><span class=tocactive>EG Value</span></a></td>
									<td valign="top"><a href="AcctPlanProjRpt?sortMeth=total_value<%= sortParms.toString() %>"><span class=tocactive>Total Value</span></a></td>
								</thead>
								<%
										NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
										Calendar cal = Calendar.getInstance();
								                for (int i=0; i < reportBeans.size(); i++){
								                	TargetProjectReportBean bean = (TargetProjectReportBean) reportBeans.get(i);
								%> <tr class=cellColor><td class=smallFontL><%= bean.getInternalStatus() %></td>
									<td class=smallFontL><a href="TargetProjectPopup?projId=<%= bean.getTargetProjectId() %>"><%= bean.getJobName() %></a></td>
									<%                      if ((bean.getDistrict().equalsIgnoreCase("Unknown"))){ %>
									<td class=smallFontL></td>
									<%                      } else {  %>
									<td class=smallFontL><%= bean.getDistrict() %></td>
									<%                      }           %>
									<td class=smallFontL><%= bean.getStatus() %></td>
									<td class=smallFontL><%= bean.getTargetReason() %></td>
									<td class=smallFontL><%= bean.getSpecPreference() %></td>
									<%						if (bean.getBidDate() != null){
																StringBuffer bidDate = new StringBuffer();
																cal.setTime(bean.getBidDate());
																bidDate.append((cal.get(Calendar.MONTH) + 1));
																bidDate.append("/" + cal.get(Calendar.DAY_OF_MONTH) + "/");
																bidDate.append(cal.get(Calendar.YEAR));
									
									%>
									<td class=smallFontL><%= bidDate.toString() %></td>
									<%						} else {   %>
									<td class=smallFontL></td>
									<%						} %>
									<td class=smallFontR><%=  nf.format(bean.getEgValue()) %></td>
									<td class=smallFontR><%= nf.format(bean.getTotalValue()) %></td>
								</tr>
								<%          }
								%>
								<tr class=cellColor>
									<td class=smallFontR colspan=7>Totals: </td>
									<td class=smallFontR><%= nf.format(chVal.doubleValue()) %></td>
									<td class=smallFontR><%= nf.format(totVal.doubleValue()) %></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td></tr>
	</table>
	</body>
</html>
