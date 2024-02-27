<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="com.eaton.electrical.smrc.bo.*" %>
<%@ page import="java.math.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page contentType="application/msexcel"%>

<html>
	<%
		response.setContentType("text/txt");
		response.setHeader("Content-Disposition", "attachment; filename=\"tapreport.xls\"");
		StringBuffer filters = (StringBuffer) request.getAttribute("filters");
	    StringBuffer sortParms = (StringBuffer) request.getAttribute("sortParms");
	    BigDecimal chVal = (BigDecimal) request.getAttribute("chVal");
	    BigDecimal totVal = (BigDecimal) request.getAttribute("totVal");
	    ArrayList reportBeans = (ArrayList) session.getAttribute("reportBeans");
	
	%>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="750" valign="top">
				<p><b>Target Projects</b></p>
				<p><b>Filters: </b><%= filters.toString()%></p>
				
				<table width="100%" cellspacing=1 cellpadding=1>
					<tr></tr>
					<tr>
						
						<%
								// Results go here
						%>
						<td valign=top>
							<table cellspacing=1 cellpadding=1 width="100%">
								<thead>
									<td valign="top"><b><u>Internal Status</u></b></td>
									<td valign="top"><b><u>Name</u></b></td>
									<td valign="top"><b><u>District</u></b></td>
									<td valign="top"><b><u>Status</u></b></td>
									<td valign="top"><b><u>Target Reason</u></b></td>
									<td valign="top"><b><u>Spec Preference</u></b></td>
									<td valign="top"><b><u>Bid Date</u></b></td>
									<td valign="top"><b><u>EG Value</u></b></td>
									<td valign="top"><b><u>Total Value</u></b></td>
								</thead>
								<%
										Calendar cal = Calendar.getInstance();
								                for (int i=0; i < reportBeans.size(); i++){
								                	TargetProjectReportBean bean = (TargetProjectReportBean) reportBeans.get(i);
								%> <tr><td><%= bean.getInternalStatus() %></td>
									<td><%= bean.getJobName() %></td>
									<%                      if ((bean.getDistrict().equalsIgnoreCase("Unknown"))){ %>
									<td></td>
									<%                      } else {  %>
									<td><%= bean.getDistrict() %></td>
									<%                      }           %>
									<td><%= bean.getStatus() %></td>
									<td><%= bean.getTargetReason() %></td>
									<td><%= bean.getSpecPreference() %></td>
									<%						if (bean.getBidDate() != null){
																StringBuffer bidDate = new StringBuffer();
																cal.setTime(bean.getBidDate());
																bidDate.append((cal.get(Calendar.MONTH) + 1));
																bidDate.append("/" + cal.get(Calendar.DAY_OF_MONTH) + "/");
																bidDate.append(cal.get(Calendar.YEAR));
									
									%>
									<td><%= bidDate.toString() %></td>
									<%						} else {   %>
									<td></td>
									<%						} %>
									<td><%=  bean.getEgValue() %></td>
									<td><%= bean.getTotalValue() %></td>
								</tr>
								<%          }
								%>
								<tr>
									<td colspan=7><b>Totals: </b></td>
									<td><%= chVal.doubleValue() %></td>
									<td><%= totVal.doubleValue() %></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td></tr>
	</table>
	</body>
</html>