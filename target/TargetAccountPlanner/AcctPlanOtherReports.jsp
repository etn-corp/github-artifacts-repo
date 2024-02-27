<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.*"%>

<html>
	<%@ include file="./TAPheader.jsp" %>
	<%@ include file="./analytics.jsp" %>
	<%
	     ArrayList users = (ArrayList) request.getAttribute("users");
	     ArrayList goodRegions = (ArrayList) request.getAttribute("goodRegions");
	     ArrayList goodSegments = (ArrayList) request.getAttribute("goodSegments");
	     ArrayList industries = (ArrayList) request.getAttribute("industries");
	     ArrayList products = (ArrayList) request.getAttribute("products");
	     ArrayList ebeCats = (ArrayList) request.getAttribute("ebeCats");
	     ArrayList rollUpGeogs = (ArrayList) request.getAttribute("rollUpGeogs");
	     ArrayList visitOutcomes = (ArrayList)request.getAttribute("visitOutcomes");
	     ArrayList visitReasons = (ArrayList)request.getAttribute("visitReasons");
	     ArrayList visitDates = (ArrayList)request.getAttribute("visitDates");
	     ArrayList competitors = (ArrayList) request.getAttribute("competitors");
	     ArrayList tmSegments = (ArrayList) request.getAttribute("tmSegments");
	     ArrayList divisions = (ArrayList) request.getAttribute("divisions");
	     boolean canRunTMReport = ((Boolean) request.getAttribute("canRunTMReport")).booleanValue();
	     boolean canRunUsageReport = ((Boolean) request.getAttribute("canRunUsageReport")).booleanValue();
	     boolean hasSegmentOverride = ((Boolean) request.getAttribute("hasSegmentOverride")).booleanValue();
	     ArrayList usageGeogs = null;
	     if (canRunUsageReport){
	         usageGeogs = (ArrayList) request.getAttribute("usageGeogs");
		 }
		 String srMonth = (String) request.getAttribute("srMonth");
		 String srYear = (String) request.getAttribute("srYear");
	     
	     // Load javascript arrays for zone/district drop downs in target market reports
	     if (canRunTMReport){
	%>
	<script language=javascript>
	function loadZoneDropDown(){
	<%      for (int i=0; i < goodRegions.size(); i++){
	            Region reg = (Region)goodRegions.get(i);
	%>
	document.targetmarketreport.tmZoneFilter[document.targetmarketreport.tmZoneFilter.length] =
	new Option('<%= StringManipulation.addBackslashToQuotes(reg.getDescription()) %>', '<%= reg.getSPGeog() %>');
	<%      }
	%>
	}
	function loadDistrictDropDown(){
	<%      for (int i=0; i < goodSegments.size(); i++){
	            Region seg = (Region)goodSegments.get(i);
	%>
	document.targetmarketreport.tmDistrictFilter[document.targetmarketreport.tmDistrictFilter.length] =
	new Option('<%= seg.getRegion() %>-<%= seg.getSegment() %> <%= StringManipulation.addBackslashToQuotes(seg.getDescription()) %>', '<%= seg.getSPGeog() %>');
	<%      }
	%>
	}
	function changeDivisions(zoneFilter){
	var zoneId = document.targetmarketreport.tmZoneFilter[zoneFilter.selectedIndex].value;
	var districtsArray = new Array();
	loadDistrictDropDown();
	for (i=0; i< document.targetmarketreport.tmDistrictFilter.length; i++){
	var district = document.targetmarketreport.tmDistrictFilter[i].value;
	if (district.substring(0,4) == zoneId.substring(0,4)){
	districtsArray[districtsArray.length] = document.targetmarketreport.tmDistrictFilter[i];
	}
	}
	document.targetmarketreport.tmDistrictFilter.length = 0;
	for (x=0; x < districtsArray.length; x++){
	document.targetmarketreport.tmDistrictFilter[document.targetmarketreport.tmDistrictFilter.length] =
	districtsArray[x];
	}
	}
	
	</script>
		<%  }
		%>
    <script>
    	function disableCompletedTaskForAll(){
		if (taskReport.assignedTo.value == '%' &&
		taskReport.region.value == '' &&
		taskReport.segment.value == '' &&
		taskReport.product.value == '' &&
		taskReport.ebe.value == ''){
		taskReport.seeComplete.checked = false;
		taskReport.seeComplete.disabled = true;
		} else {
		taskReport.seeComplete.disabled = false;
		}
		}
    </script>
		<table width="760" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="10">&nbsp;</td>
				<td width="750">
					<p><br>
						<a class="crumb" href="SMRCHome">Home Page</a>
						<span class="crumbarrow">&gt;</span>
						<span class="crumbcurrent">Other Reports</span>
					</p>
				</td>
			</tr>
		</table>
		<table width="760" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="10">&nbsp;</td>
				<td width="750" valign="top">
					<p>&nbsp;</p>
					<p class="heading2">Other Reports</p>
					<p><span class="heading2">Task Reports</span></p>
					<form action=AcctPlanReport name=taskReport>
						<input type=hidden name=page value=taskReport>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="26%">Whose tasks do you want to see?</td>
								<td width="2%">&nbsp;</td>
								<td width="72%">
									<select name="assignedTo" onchange="Javascript:disableCompletedTaskForAll()">
										<option value='%'>All</option>
										<%
												for (int i=0; i < users.size(); i++) {
													User thisUser = (User)users.get(i);
													String selected = "";
													if (thisUser.getUserid().equals(header.getUser().getUserid())) {
														selected = " selected";
													}
										%>
										<option value='<%= thisUser.getUserid() %>' <%= selected %>><%= thisUser.getLastName() %>, <%= thisUser.getFirstName() %></option>
										<%		}
										%>
									</select>
								</td>
							</tr>
							<tr>
								<td colspan="3">&nbsp;</td>
							</tr>
							<tr>
								<td colspan="3" class="heading3">Filters</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td align="left">&nbsp;</td>
							</tr>
							<tr>
								<td><div align="right">Zone:</div></td>
								<td>&nbsp;</td>
								<td>
									<select name="region" onchange="Javascript:disableCompletedTaskForAll()">
										<option value=''>All</option>
										<%
												for (int i=0; i < goodRegions.size(); i++) {
													Region reg = (Region)goodRegions.get(i);
										%> <option value='<%= reg.getSPGeog() %>'><%= reg.getDescription() %></option>
										<%		}
										%>
									</select>
								</td>
							</tr>
							<tr>
								<td colspan="3">&nbsp;</td>
							</tr>
							<tr>
								<td><div align="right">District:</div></td>
								<td>&nbsp;</td>
								<td>
									<select name="segment" onchange="Javascript:disableCompletedTaskForAll()">
										<option value=''>All</option>
										<%
												for (int i=0; i < goodSegments.size(); i++) {
													Region seg = (Region)goodSegments.get(i);
										%>
										<option value='<%= seg.getSPGeog() %>'><%= seg.getRegion() %>-<%= seg.getSegment() %> <%= seg.getDescription() %></option>
										<%		}
										%>
									</select>
								</td>
							</tr>
							<tr>
								<td colspan="3">&nbsp;</td>
							</tr>
							<tr>
								<td><div align="right">Product:</div></td>
								<td>&nbsp;</td>
								<td>
									<select name="product" onchange="Javascript:disableCompletedTaskForAll()">
										<option value=''>All</option>
										<%
												for (int i=0; i < products.size(); i++) {
													Product prod = (Product)products.get(i);
										%>
										<option value='<%= prod.getId() %>'><%= prod.getDescription() %></option>
										<%		}
										%>
									</select>
								</td>
							</tr>
							<tr>
								<td colspan="3">&nbsp;</td>
							</tr>
							<tr>
								<td><div align="right">EBE Category:</div></td>
								<td>&nbsp;</td>
								<td>
									<select name="ebe" onchange="Javascript:disableCompletedTaskForAll()">
										<option value=''>All</option>
										<%
												for (int i=0; i < ebeCats.size(); i++) {
													EBECategory ec = (EBECategory)ebeCats.get(i);
										%> <option value='<%= ec.getId() %>'><%= ec.getDescription() %></option>
										<%		}
										%>
									</select>
								</td>
							</tr>
							<tr>
								<td colspan="3">&nbsp;</td>
							</tr>
							<tr>
								<td><div align="right">Check here to see completed tasks:</div></td>
								<td>&nbsp;</td>
								<td><input type=checkbox name=seeComplete onClick="Javascript:disableCompletedTaskForAll()"></td>
							</tr>
							<tr>
								<td colspan="3">&nbsp;</td>
							</tr>
						</table>
						<table width="100%" border="0" cellspacing="10" cellpadding="0">
							<tr>
								<td width="36%"><div align="right"><input type="image" src="<%= sImagesURL %>button_view_report.gif" width="70" height="20" ></div></td>
								<td width="64%"></td>
							</tr>
						</table>
					</form>
					<%             if (canRunTMReport){
					%>
					<br><hr width="100%" noshade color="#DDDDDD">
					<form name="targetmarketreport" action="TargetMarketReportResults" method="Get">
					    <input type=hidden name=page value='results'>
						<p><span class="heading2">Target Market Reports</span></p>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="26%" valign="top"><div align="right">Group By:</div></td>
								<td width="2%" widht="2%">&nbsp;</td>
								<td colspan="3">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td><input type="radio" name="tmGroupBy" value="tmGroupByTMP" checked>Target Market Plan </td>
										</tr>
										<tr>
											<td><input type="radio" name="tmGroupBy" value="tmGroupByDivision" >Division </td>
										</tr>
										<tr>
											<td><input type="radio" name="tmGroupBy" value="tmGroupByDistrict">District </td>
										</tr>
										<tr>
											<td><input type="radio" name="tmGroupBy" value="tmGroupByAccount">Account </td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td colspan="5">&nbsp;</td></tr>
							<tr>
								<td class="heading3"><strong>Filters</strong></td>
								<td>&nbsp;</td>
								<td colspan="3">&nbsp;</td>
							</tr>
							<tr><td colspan="5">&nbsp;</td></tr>
							<tr>
								<td><div align="right">Division:</div></td>
								<td>&nbsp;</td>
								<td colspan="3">
									<select name="tmDivisionFilter">
										<option selected value='0'>All</option>
										<%                              for (int i=0; i< divisions.size(); i++){
										                                Division division = (Division) divisions.get(i);
										%>
										<option value='<%= division.getId() %>'><%= division.getName() %></option>
										<%                              }
										%>
									</select>
								</td>
							</tr>
							<tr><td colspan="5">&nbsp;</td></tr>
							<tr>
								<td><div align="right">Zone:</div></td>
								<td>&nbsp;</td>
								<td colspan="3">
									<select name="tmZoneFilter" onchange="changeDivisions(this)">
										<option selected value='0'>All</option>
										<script language="javascript">
											loadZoneDropDown();
										</script>
									</select>
								</td>
							</tr>
							<tr><td colspan="5">&nbsp;</td></tr>
							<tr>
								<td><div align="right">District:</div></td>
								<td>&nbsp;</td>
								<td colspan="3">
									<select name="tmDistrictFilter">
										<option selected value='0'>All</option>
										<script language="javascript">
											loadDistrictDropDown();
										</script>
									</select>
								</td>
							</tr>
							<tr><td colspan="5">&nbsp;</td></tr>
							<tr>
								<td><div align="right">Competitor Conversions:</div></td>
								<td>&nbsp;</td>
								<td colspan="3">
									<select name="tmConversionFilter">
										<option selected value='0'>All</option>
										<%
										                                for (int i=0; i < competitors.size(); i++){
										                                Vendor vendor = (Vendor) competitors.get(i);
										
										%>
										<option value='<%= vendor.getId() %>'><%= vendor.getDescription() %></option>
										<%                                                                                      }
										%>
									</select>
								</td>
							</tr>
							<tr><td colspan="5">&nbsp;</td></tr>
							<tr>
								<td><div align="right">Market Segment:</div></td>
								<td>&nbsp;</td>
								<td colspan="3">
									<select name="tmSegmentFilter">
										<option selected value='0'>All</option>
										<%                              for (int i=0; i< tmSegments.size(); i++){
										                                    Segment segment = (Segment) tmSegments.get(i);
										%>
										<option value='<%= segment.getSegmentId() %>'><%= segment.getName() %> </option>
										<%                              }
										%>
									</select>
								</td>
							</tr>
							<tr><td colspan="5">&nbsp;</td></tr>
							<tr>
								<td><div align="right">Active Date Range:</div></td>
								<td>&nbsp;</td>
								<td colspan="3" valign="center">
									<select name="targetMarketBeginDate">
									<% 
										Calendar startCal = Calendar.getInstance();
										startCal.set(Calendar.MONTH, Calendar.JANUARY);
										startCal.set(Calendar.YEAR, 2005);
										Calendar endCal = Calendar.getInstance();
										endCal.set(Calendar.MONTH,Globals.a2int(srMonth));
										endCal.set(Calendar.YEAR, Globals.a2int(srYear));
										Calendar loopCal = Calendar.getInstance();
										loopCal.setTime(startCal.getTime());
										while (endCal.getTime().after(loopCal.getTime())) {
											String selected = "";
											if (loopCal.get(Calendar.MONTH) == 0 && loopCal.get(Calendar.YEAR) == Globals.a2int(srYear)){
												selected = "selected";
											}
									%>
											<option value="<%=  ( loopCal.get(Calendar.MONTH) + 1) + "/" + loopCal.get(Calendar.YEAR)  %>" <%= selected %>><%= (loopCal.get(Calendar.MONTH) + 1) + "/" + loopCal.get(Calendar.YEAR) %></option>
									<%	
											loopCal.add(Calendar.MONTH,1);
										} 
									%>
									</select>
									&nbsp;TO&nbsp;
									<select name="targetMarketEndDate">
									<% loopCal.setTime(startCal.getTime());
										while (endCal.getTime().after(loopCal.getTime())) {
											String selected = "";
											if (loopCal.get(Calendar.MONTH) == (Globals.a2int(srMonth) - 1) && loopCal.get(Calendar.YEAR) == Globals.a2int(srYear)){
												selected = "selected";
											}
									%>
											<option value="<%=  ( loopCal.get(Calendar.MONTH) + 1) + "/" + loopCal.get(Calendar.YEAR)  %>" <%= selected %> ><%= (loopCal.get(Calendar.MONTH) + 1) + "/" + loopCal.get(Calendar.YEAR) %></option>
									<%	
											loopCal.add(Calendar.MONTH,1);
										} 
									%>
									</select>
								</td>
								
							</tr>
							<tr><td colspan="5">&nbsp;</td></tr>
						</table>
						<table width="100%" border="0" cellspacing="10" cellpadding="0">
							<tr>
								<td width="36%"><div align="right"><input type="image" src="<%= sImagesURL %>button_view_report.gif" width="70" height="20"></div></td>
								<td width="64%">&nbsp;</td>
							</tr>
						</table>
					</form>
					<%              }
					%>
					<br><hr width="100%" noshade color="#DDDDDD">
					<p><span class="heading2">Customer Visit Reports</span></p>
					<form action="CustomerVisits" method="POST">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="3">&nbsp;</td>
							</tr>
							<tr>
								<td width="26%"><div align="right">Date of Visit:</div></td>
								<td width="2%">&nbsp;</td>
								<td width="72%">
									<select name="VISIT_DATE">
										<option value="-1">All</option>
										<%
										for(int i=0;i<visitDates.size();i++){
											Date visitDate = (Date)visitDates.get(i);
										%>
										<option value="<%= visitDate %>"><%= visitDate %></option>
										<% } %>
									</select>
								</td>
							</tr>
							<tr>
								<td colspan="3">&nbsp;</td>
							</tr>
							<tr>
								<td><div align="right">Customer's District:</div></td>
								<td>&nbsp;</td>
								<td>
									<select name="VISIT_DISTRICT">
										<option value="-1">All</option>
										<%
										for (int i=0; i < goodSegments.size(); i++) {
												Region seg = (Region)goodSegments.get(i);
										%>
										<option value='<%= seg.getSPGeog() %>'><%= seg.getRegion() %>-<%= seg.getSegment() %> <%= seg.getDescription() %></option>
										<% } %>
									</select>
								</td>
							</tr>
							<tr>
								<td colspan="3">&nbsp;</td>
							</tr>
							<tr>
								<td><div align="right">Reason for Visit	:</div></td>
								<td>&nbsp;</td>
								<td>
									<select name="VISIT_REASON_TYPE_ID">
										<option value="-1">All</option>
										<%
										for(int i=0;i<visitReasons.size();i++){
											CodeType code = (CodeType)visitReasons.get(i);
										%>
										<option value="<%= code.getId() %>"><%= code.getName() %></option>
										<% } %>
									</select>
								</td>
							</tr>
							<tr>
								<td colspan="3">&nbsp;</td>
							</tr>
							<tr>
								<td><div align="right">Outcome of Visit	:</div></td>
								<td>&nbsp;</td>
								<td>
									<select name="VISIT_OUTCOME_TYPE_ID">
										<option value="-1">All</option>
										<%
										for(int i=0;i<visitOutcomes.size();i++){
											CodeType code = (CodeType)visitOutcomes.get(i);
										%>
										<option value="<%= code.getId() %>"><%= code.getName() %></option>
										<% } %>
									</select>
								</td>
							</tr>
							<tr>
								<td colspan="3">&nbsp;</td>
							</tr>
							<tr>
								<td><div align="right">Customer's Segment:</div></td>
								<td>&nbsp;</td>
								<td>
									<% Collection segments = (Collection)request.getAttribute("segments"); %>
									<%@ include file="./segmentSelectTree.jsp" %>
								</td>
							</tr>
							<tr>
								<td colspan="3">&nbsp;</td>
							</tr>
							<tr>
								<td><div align="right"><a href="CustomerVisits?page=report&myvisits=true">My Visits in Past Year&nbsp;</a></div></td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td colspan="3">&nbsp;</td>
							</tr>
						</table>
						<table width="100%" border="0" cellspacing="10" cellpadding="0">
							<tr>
								<td width="36%"><div align="right"><input type="image" src="<%= sImagesURL %>button_view_report.gif" width="70" height="20"></div></td>
								<td width="64%">&nbsp;</td>
							</tr>
						</table>
						<input type="hidden" name="page" value="report">
					</form>
					<%   if (canRunUsageReport){
					    	
						%>
						<br><hr width="100%" noshade color="#DDDDDD">
						<form name="usagereport" action="UsageReportResults" method="Get">
						    <input type=hidden name='page' value='results'>
							<p><span class="heading2">Usage Report</span></p>
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="26%" valign="top"><div align="right">Group By:</div></td>
									<td width="2%" widht="2%">&nbsp;</td>
									<td colspan="3">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td><input type="radio" name="usageGroupBy" value="usageGroupByMonth" checked>Month </td>
									       </tr>
											<tr>
												<td><input type="radio" name="usageGroupBy" value="usageGroupByZone" >Zone </td>
											</tr>
											<tr>
												<td><input type="radio" name="usageGroupBy" value="usageGroupByDistrict">District </td>
											</tr>
											<tr>
												<td><input type="radio" name="usageGroupBy" value="usageGroupByTeam">Team </td>
											</tr>
											<tr>
												<td><input type="radio" name="usageGroupBy" value="usageGroupByUser">User </td>
										    </tr>
										</table>
									</td>
								</tr>
								<tr><td colspan="5">&nbsp;</td></tr>
								<tr>
									<td class="heading3"><strong>Filters</strong></td>
									<td>&nbsp;</td>
									<td colspan="3">&nbsp;</td>
								</tr>
								<tr><td colspan="5">&nbsp;</td></tr>
								<tr>
									<td><div align="right">Month/Year:</div></td>
									<td>&nbsp;</td>
									<td colspan="3">
										<select name="usageDateFilter">
											<option selected value='0'>All</option>
									<%
											boolean cont = true;
											Calendar breakDate = Calendar.getInstance();
											breakDate.set(2005,3,1);		// Sets the earliest date to April 1, 2005
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
								</tr>
								<tr>
									<td colspan="5">&nbsp;</td>
							    </tr>
								<tr>
									<td><div align="right">Geography:</div></td>
									<td>&nbsp;</td>
									<td colspan="3">
										<select name="usageGeogFilter">
			<%  User user = header.getUser();
				
				if (user.hasOverrideSecurity() || user.ableToViewEverything() || hasSegmentOverride){
				      
			%>
											<option selected value='0'>All</option>  
	        <%   }
	        %>
	          <% for (int i=0; i< usageGeogs.size(); i++){
	                Geography geog = (Geography) usageGeogs.get(i);
	          %>
											<option value='<%= geog.getGeog()  %>' ><%= geog.getDescription() %> (<%= geog.getGeog() %>)</option>
			 <% }
			 %>
										</select>
									</td>
								</tr>
								<tr><td colspan="5">&nbsp;</td></tr>
							</table>
							<table width="100%" border="0" cellspacing="10" cellpadding="0">
								<tr>
									<td width="36%"><div align="right"><input type="image" src="<%= sImagesURL %>button_view_report.gif" width="70" height="20"></div></td>
									<td width="64%">&nbsp;</td>
								</tr>
							</table>
						</form>
						<%              }
						%>
				</td>
			</tr>
		</table>
		<p>&nbsp;</p>
		</body>
</html>
