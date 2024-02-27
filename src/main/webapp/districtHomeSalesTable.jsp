<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*, java.text.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*, com.eaton.electrical.smrc.util.*"%>
<%@ include file="analytics.jsp" %>
	
<%
String district=(String)session.getAttribute("district");
ArrayList reportBeans = (ArrayList) request.getAttribute("reportBeans");
double monthlyForecast = ((Double) request.getAttribute("monthlyForecast")).doubleValue();
User user = (User) request.getAttribute("user");
ResourceBundle rb = ResourceBundle.getBundle("com.eaton.electrical.smrc.SMRC");
String imagesURL = rb.getString("imagesURL");
String cssURL = rb.getString("cssURL");
String jsURL = rb.getString("jsURL");
String salesOrders = (String) request.getAttribute("salesOrders");

int srMonth = Globals.a2int((String) session.getAttribute("srMonth"));
// If the month is zero, something went wrong... set to current month
if ( srMonth == 0 ) {
	Calendar cal = new GregorianCalendar();
    
    // Get the components of the date
    srMonth = cal.get(Calendar.MONTH) + 1;
}

%>
<html>
<link rel=stylesheet type="text/css" href="<%= cssURL %>style_2_1.css">
<link rel=stylesheet type="text/css" href="<%= cssURL %>oem-styles.css">
	<form name="districtHomeForm" action="DistrictHomeSalesTable" method="post">
	<input type=hidden name="salesorders" value="<%= salesOrders %>">
					<table width="100%" border="0" cellspacing="1" cellpadding="0">
						<tr>
							<th id="tableSpace">Description</th>
							<th>YTD Goal $</th>
							<th>YTD Total $</th>
							<th>% Difference</th>
							<th>Monthly Goal $</th>
							<th>MTD Goal $</th>
							<th>Monthly Total $</th>
							<th>% Difference</th>
							<th>Monthly Forecast $</th>
						</tr>
						<%              NumberFormat df = NumberFormat.getInstance();
						                NumberFormat pf = NumberFormat.getPercentInstance();
						                df.setMaximumFractionDigits(0);
						                pf.setMaximumFractionDigits(1);
						                String href = "";
						                String trbgcolor=null;
						                int segmentId=0;
						          for (int i=0; i< reportBeans.size(); i++){
						              segmentId=0;      
						              DistrictHomePageReportBean bean = (DistrictHomePageReportBean) reportBeans.get(i);
						                    if (bean.getDescription().equalsIgnoreCase("District Wide")){
						                        href = "CustomerListing?page=listing&DISTRICT=" + district;
						                    } else if (bean.getDescription().equalsIgnoreCase("Target Accounts")){
						                        href = "CustomerListing?page=listing&districttargets=true";
						                    } else if (bean.getDescription().equalsIgnoreCase("Division Target Accounts")){
						                        href = "CustomerListing?page=listing&districttargets=true&divisiontargets=true";
						                    } else {
						                        segmentId=Globals.a2int(bean.getId());
						                        href = "CustomerListing?page=listing&otherHomeSegment=" + bean.getId() + "&DISTRICT=" + district;
						                    }
						                    
						                    if(i%2==0) {
						                        trbgcolor="";
						                    }else {
						                        trbgcolor=" class=\"cellShade\"";
						                    }
						%>
						<tr<%= trbgcolor %>>
							<td>
								<div align="left" id="cellShade"><a href="<%= href %>" target=_top><%= bean.getDescription() %></a>
								<% if(segmentId!=0) { %>
								    &nbsp;&nbsp;<a target=_top href="AccountToolbox?segmentId=<%= segmentId %>">[ toolbox ]</a>
								<% } %>
								&nbsp;
						</div>
							</td>
							<%                      if (bean.getDescription().equalsIgnoreCase("District Wide")){
							%>
							<td bgcolor="#E0E0E0">
								<div align="right"><%= df.format(bean.getCurrYTDGoal()) %>&nbsp;</div>
							</td>
							<td>
								<div align="right"><%= df.format(bean.getCurrYTDTotal()) %>&nbsp;</div>
							</td>
							<td bgcolor="#E0E0E0">
								<div align="right"><%= pf.format(bean.getYTDDifference()) %>&nbsp;</div>
							</td>
							<td>
								<div align="right"><%= df.format(bean.getMonthlyGoal()) %>&nbsp;</div>
							</td>
							<td bgcolor="#E0E0E0">
								<div align="right"><%= df.format(bean.getMTDGoal()) %>&nbsp;</div>
							</td>
							<td>
								<div align="right"><%= df.format(bean.getMonthlyTotal()) %></div>
							</td>
							<td bgcolor="#E0E0E0">
								<div align="right"><%= pf.format(bean.getMonthlyDifference()) %>&nbsp;</div>
							</td>
							<td align="right">
								<%                              if (user.isDistrictManager() || user.hasOverrideSecurity()){
								%>
								<div align="center">
									<input name="forecast" type="text" size="8" maxlength="12" value="<%= df.format(monthlyForecast) %>">
								</div>
								<%                              } else {
								%>
								<%= df.format(monthlyForecast) %>
								<%                              }
								%>
							</td>
							<%                      } else {
							%>
							<td>&nbsp;</td>
							<td>
								<div align="right"><%= df.format(bean.getCurrYTDTotal()) %></div>
							</td>
							<td colspan="6">&nbsp;</td>
							<%                      }
							%>
						</tr>
						<%              }
						%>
					</table>
					<%                         if (user.isDistrictManager() || user.hasOverrideSecurity()){
					%>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="85%">
								<div align="right"> </div>
							</td>
							<td width="15%">
								<div align="center">
									<input type="submit" name="submit" value="Save Forecast">
								</div>
							</td>
						</tr>
					</table>
					
					<%                            }
					%>
	</form>
					
	</body>
</html>