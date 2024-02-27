<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="com.eaton.electrical.smrc.bo.*" %>
<%@ page import="com.eaton.electrical.smrc.util.*" %>

<html>
<%@ include file="./TAPheader.jsp" %>
<%@ include file="analytics.jsp" %>

<%
	 ArrayList bidDateRanges = (ArrayList) request.getAttribute("bidDateRanges");
     ArrayList cops = (ArrayList) request.getAttribute("cops");
     ArrayList vendors = (ArrayList) request.getAttribute("vendors");
     ArrayList products = (ArrayList) request.getAttribute("products");
     ArrayList statuses = (ArrayList) request.getAttribute("statuses");
     ArrayList reasons = (ArrayList) request.getAttribute("reasons");
     ArrayList preferences = (ArrayList) request.getAttribute("preferences");
     ArrayList zones = (ArrayList) request.getAttribute("zones");
     ArrayList districts = (ArrayList) request.getAttribute("districts");
     ArrayList bidStatuses = (ArrayList) request.getAttribute("bidStatuses");
     ArrayList jobTypes = (ArrayList) request.getAttribute("jobTypes");
     ArrayList selectedSENames = (ArrayList) request.getAttribute("selectedSENames");
     ArrayList selectedSEs = (ArrayList) session.getAttribute("selectedSEs");
     String selectedBidDateRange = StringManipulation.noNull((String) request.getAttribute("selectedBidDateRange"));
	 String selectedHasGO = StringManipulation.noNull((String) request.getAttribute("selectedHasGO"));
     String selectedViewDetails = StringManipulation.noNull((String) request.getAttribute("selectedViewDetails"));
     ArrayList selectedBidStatus = (ArrayList) request.getAttribute("selectedBidStatus");
	 ArrayList selectedJobTypes = (ArrayList) request.getAttribute("selectedJobTypes");
	 ArrayList selectedZones = (ArrayList) request.getAttribute("selectedZones");
	 ArrayList selectedDistricts = (ArrayList) request.getAttribute("selectedDistricts");
	 ArrayList selectedProducts = (ArrayList) request.getAttribute("selectedProducts");
    
  
%>
<script>
    function clearSEs(){
    	 document.bidmanreportform.clearSEs.value="Y";
    	 document.bidmanreportform.refresh.value="Y";
         document.bidmanreportform.submit();
	}

        
</script>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>

		<span class="crumbarrow">&gt;</span>

		<span class="crumbcurrent">Project Reports</span>
      </p> 
    </td>
  </tr>
</table>
<br>
<table width="95%" class="tableBorder" cellspacing="1" cellpadding="1" align="center">
    <tr class=cellColor>
        <td valign=top width="50%">
            <form action="AcctPlanProjRpt" name="targetprojectform">

		<input type="hidden" name="page" value='projresults'>
		<input type="hidden" name="rptType" value="targetProj">
                <table class="innerBorder" cellspacing="1" cellpadding="1" border="0" align="center" width="100%">
                <caption class="heading2">Target Projects<br><div class="textnormal">Provides list of target projects based on the filters you select below</div></caption>
                    <tr>
                        <td class=innerColor><div class=smallFontL>Last Revision Date Range</div></td>
        		<td class=cellColor><select class=smallFontL name=revDate>
                        			<option value='' selected>All</option>
                                		<option value=day>Previous Day</option>
                        			<option value=week>Last 7 days</option>
                                		<option value=thisMonth>This Month</option>
                            			<option value=lastMonth>Last Month</option>
                                		<option value=thisYear>This Year</option>
                                                <option value=lastYear>Last Year</option>
					</select>
			</td>
                	<td class=innerColor><div class=smallFontL>Bid Date Range</div></td>
			<td class=cellColor><select class=smallFontL name=bidDate>
					<option value='' selected>All</option>
					<option value=nextYear>Upcoming Year</option>
                                	<option value=6month>Next 6 Months</option>
					<option value=3month>Next 3 Months</option>
                        		<option value=nextMonth>Upcoming Month</option>
					<option value=nextWeek>Upcoming Week</option>
                        		<option value=day>Previous Day</option>
					<option value=week>Last 7 days</option>
					<option value=thisMonth>This Month</option>
					<option value=lastMonth>Last Month</option>
					<option value=thisYear>This Year</option>
					<option value=lastYear>Last Year</option>
					</select>
			</td>
		</tr>
		<tr>
                    <td class=innerColor><div class=smallFontL>Change Order Potential</div></td>
                    <td class=cellColor><select class=smallFontL multiple name=cop>
                                    <option value='' selected>All</option>
<%
		for (int i=0; i < cops.size(); i++) {
			ChangeOrderPotential cop = (ChangeOrderPotential)cops.get(i);
%>                                  <option value='<%= cop.getId() %>'><%= cop.getDescription() %></option>
<%		}
%>
                                    </select>
                    </td>
                    <td class=innerColor><div class=smallFontL>Vendors</div></td>
                    <td class=cellColor><select class=smallFontL multiple name=vendor>
                        		<option value='' selected>All</option>
<%
		for (int i=0; i < vendors.size(); i++) {
			Vendor ven = (Vendor)vendors.get(i);
%>			<option value='<%= ven.getId() %>'><%= ven.getDescription() %></option>
<%		}
%>
                        		</select>
                    </td>
		</tr>
		<tr>
                    <td class=innerColor><div class=smallFontL>Products</div></td>
                    <td class=cellColor><select class=smallFontL multiple name=bom>
					<option value='' selected>All</option>
<%
		for (int i=0; i < products.size(); i++) {
			Product prod = (Product)products.get(i);
%>                              	<option value='<%= prod.getId() %>'><%= prod.getDescription() %></option>
<%		}
%>
					</select>
                    </td>
                    <td class=innerColor><div class=smallFontL>Statuses</div></td>
                    <td class=cellColor><select class=smallFontL multiple name=status>
                            <option value='' selected>All</option>
<%
		for (int i=0; i < statuses.size(); i++) {
			ProjectStatus stat = (ProjectStatus)statuses.get(i);
%>                          <option value='<%= stat.getId() %>'><%= stat.getDescription() %></option>
<%		}
%>
                            </select>
                    </td>
		</tr>
		<tr>
                    <td class=innerColor><div class=smallFontL>Reasons</div></td>
                    <td class=cellColor><select class=smallFontL multiple name=reason>
					<option value='' selected>All</option>
<%
		for (int i=0; i < reasons.size(); i++) {
			TargetReason reason = (TargetReason)reasons.get(i);
%>                              	<option value='<%= reason.getId() %>'><%= reason.getDescription() %></option>
<%		}
%>
                                    </select>
                    </td>
                    <td class=innerColor><div class=smallFontL>Preferences</div></td>
                    <td class=cellColor><select class=smallFontL multiple name=preference>
					<option value='' selected>All</option>
<%
		for (int i=0; i < preferences.size(); i++) {
			SpecPreference sp = (SpecPreference)preferences.get(i);
%>                                      <option value='<%= sp.getId() %>'><%= sp.getDescription() %></option>
<%		}
%>
                        		</select>
                    </td>
		</tr>
		<tr>
                    <td class=innerColor><div class=smallFontL>Zones</div></td>
                    <td class=cellColor><select class=smallFontL multiple name=zone>
                                    <option value='' selected>All</option>
<%
		for (int i=0; i < zones.size(); i++) {
			Region zone = (Region)zones.get(i);
%>                                  <option value='<%= zone.getSPGeog() %>'><%= zone.getDescription() %></option>
<%		}
%>
                                    </select>
                    </td>
                    <td class=innerColor><div class=smallFontL>Districts</div></td>
                    <td class=cellColor><select class=smallFontL multiple name=dist>
                                <option value='' selected>All</option>
<%
		for (int i=0; i < districts.size(); i++) {
			Region dist = (Region)districts.get(i);
%>				<option value='<%= dist.getSPGeog() %>'><%= dist.getDescription() %></option>
<%		}
%>
				</select>
                    </td>
		</tr>
		<tr valign="top">
                    <td class=innerColor><div class=smallFontL><br>Internal Status</div></td>
                    <td class=cellColor><select class=smallFontL multiple name=internal_status>
                        		<option value='' selected>All</option>
					<option value='A'>Active</option>
					<option value='M'>Awaiting CHAMPS Mgr Approval</option>
					<option value='Z'>Awaiting Project Sales Mgr Approval</option>
					<option value='N'>Awaiting District Mgr Approval</option>
					<option value='D'>Deleted</option>
                    </td>
                    <td class=innerColor><br>Segments</td>
                    <td class=cellColor>
                    <% Collection segments = (Collection)request.getAttribute("segments"); %> 
                    <%@ include file="./segmentSelectTree.jsp" %>
                    </td>
		</tr>
            </table><br>
            <div class=centerOnly>
           <a href="javascript:document.targetprojectform.submit()"><img src="<%= sImagesURL %>button_submit.gif" width="70" height="20" align="top" border="0"></a>
           </div>
            <br>
            </form>
        </td>

	<td valign=top width="50%">
	<form action=AcctPlanProjRpt name="bidmanreportform">

	        <input type=hidden name=refresh value='n'>
	        <input type=hidden name=clearSEs value="N">
            <input type=hidden name=page value='projresults'>
            <input type=hidden name=rptType value=bidTrack>
                <table class=innerBorder cellspacing=1 cellpadding=1 border=0 align=center width="100%">
		<caption class=heading2>Bid Tracking<br><div class=textnormal>Provides list of jobs based on the filters you select below</div></caption>
                	<tr>
                            <td class=innerColor><div class=smallFontL>Bid Date Range</div></td>
                            <td class=cellColor><select class=smallFontL name=bidDate>
      <% String bdSelected = "selected";
         if (!selectedBidDateRange.equals("")){
             bdSelected = "";
         }
      %>
                        	<option value='' <%= bdSelected %>>All</option>
       
	<%  for (int i=0; i < bidDateRanges.size(); i++){
	    	DropDownBean bdOption = (DropDownBean) bidDateRanges.get(i);
	    	if (selectedBidDateRange.equals(bdOption.getName())){
	    	    bdSelected = "selected";
	    	} else {
	    	    bdSelected = "";
	    	}
	%>
			<option value='<%= bdOption.getName() %>' <%= bdSelected %>><%= bdOption.getValue() %></option>
	<% }
	%>
				</select>
                            </td>
                            <td class=innerColor><div class=smallFontL>Bid Statuses</div></td>
                            <td class=cellColor><select class=smallFontL multiple name=bidStatus>
<%		String bidStatusSelected = "selected";
        if (selectedBidStatus.size() > 0){
            bidStatusSelected = "";
        }
%>
                                        <option value='' <%= bidStatusSelected %>>All</option>
<%		for (int i=0; i < bidStatuses.size(); i++) {
			String bs = (String)bidStatuses.get(i);
			bidStatusSelected = "";
			for (int x=0; x< selectedBidStatus.size(); x++){
			    String status = (String) selectedBidStatus.get(x);
			    if (status.equalsIgnoreCase(bs)){
			        bidStatusSelected = "selected";
			    }
			}
%>				<option value='<%= bs %>' <%= bidStatusSelected %>><%= bs %></option>
<%			
		}
%>
					</select>
                            </td>
                        </tr>
			<tr>
                            <td class=innerColor><div class=smallFontL>Job Types</div></td>
                            <td class=cellColor><select class=smallFontL multiple name=jobType>
<%		String jobTypeSelected = "selected";
		if (selectedJobTypes.size() > 0){
		    jobTypeSelected = "";
		}
%>
                           
                            <option value='' <%= jobTypeSelected %>>All</option>
<%
		for (int i=0; i < jobTypes.size(); i++) {
			JobType jt = (JobType)jobTypes.get(i);
			jobTypeSelected = "";
			for (int x=0; x < selectedJobTypes.size(); x++){
			    String thisJobType = (String) selectedJobTypes.get(x);
			    if (thisJobType.equalsIgnoreCase(jt.getId())){
			        jobTypeSelected = "selected";
			    }
			}
%>					<option value='<%= jt.getId() %>' <%= jobTypeSelected %>><%= jt.getDescription() %></option>
<%		}
%>
					</select>
                            </td>
                            <td class=innerColor><div class=smallFontL>Has GO Number?</div></td>
                            <td class=innerColor><select class=smallFontL name=goNum>
                            <% String goSelected = "selected";
							   String goNotSelected = "";
							%>
                                        <option value='' <%= (selectedHasGO.equalsIgnoreCase("") ? goSelected : goNotSelected) %>>Show All</option>
                                        <option value='Y' <%= (selectedHasGO.equalsIgnoreCase("Y") ? goSelected : goNotSelected) %>>Has GO Number</option>
                                        <option value='N' <%= (selectedHasGO.equalsIgnoreCase("N") ? goSelected : goNotSelected) %>>No GO Number</option>
                                        </select>
                            </td>
                        </tr>
                        <tr>
                            <td class=innerColor><div class=smallFontL>Zones</div></td>
                            <td class=cellColor><select class=smallFontL multiple name=zone>
<%		String zoneSelected = "selected";
		if (selectedZones.size() > 0){
		    zoneSelected = "";
		}
%>
					<option value='' <%= zoneSelected %>>All</option>
<%
		for (int i=0; i < zones.size(); i++) {
			Region zone = (Region)zones.get(i);
			zoneSelected = "";
			for (int x=0; x < selectedZones.size(); x++){
			    String thisZone = (String) selectedZones.get(x);
			    if (thisZone.equalsIgnoreCase(zone.getSPGeog())){
			        zoneSelected = "selected";
			    }
			}
%>					<option value='<%= zone.getSPGeog() %>' <%= zoneSelected %>><%= zone.getDescription() %></option>
<%		}
%>
					</select>
                            </td>
                            <td class=innerColor><div class=smallFontL>Districts</div></td>
                            <td class=cellColor><select class=smallFontL multiple name=dist>
<%		String districtSelected = "selected";
		if (selectedDistricts.size() > 0){
		    districtSelected = "";
		}
%>
                                        <option value='' <%= districtSelected %>>All</option>
<%
		for (int i=0; i < districts.size(); i++) {
			Region dist = (Region)districts.get(i);
			districtSelected = "";
			for (int x=0; x< selectedDistricts.size(); x++){
			    String thisDistrict = (String) selectedDistricts.get(x);
			    if (thisDistrict.equalsIgnoreCase(dist.getSPGeog())){
			        districtSelected = "selected";
			    }
			}
%>                                      <option value='<%= dist.getSPGeog() %>' <%= districtSelected %>><%= dist.getDescription() %></option>
<%		}
%>
					</select>
                            </td>
			</tr>
			<tr>
                            <td class=innerColor><div class=smallFontL>Products</div></td>
                            <td class=cellColor><select class=smallFontL multiple name=prods>
<%		String productSelected = "selected";
		if (selectedProducts.size() > 0){
		    productSelected = "";
		}
%>
                                        <option value='' <%= productSelected %>>All</option>
<%
		for (int i=0; i < products.size(); i++) {
			Product prod = (Product)products.get(i);
			productSelected = "";
			for (int x=0; x < selectedProducts.size(); x++){
			    String thisProduct = (String) selectedProducts.get(x);
			    if (thisProduct.equalsIgnoreCase(prod.getId())){
			        productSelected = "selected";
			    }
			}
%>			<option value='<%= prod.getId() %>' <%= productSelected %>><%= prod.getId() %> - <%= prod.getDescription() %></option>
<%		}
%>
					</select>
                            </td>

                            <td class=innerColor><div class=smallFontL>Sales Engineer</div></td>
                            <td class=cellColor>
	                            <a href="javascript:newWindow('SEBrowse?callPage=bidmanReports','SE1')"><img src="<%= sImagesURL %>button_browse.gif" width="70" height="20" align="top" border="0"></a>
								&nbsp;<a href="javascript:clearSEs()"><img src="<%= sImagesURL %>button_clear.gif" width="70" height="20" align="top" border="0"></a><br>
								<% for (int i=0; i < selectedSENames.size(); i++){
								      String seName = (String) selectedSENames.get(i);
								      String seId = (String) selectedSEs.get(i);
								%>
								      <input type=hidden name=bidSEFilter value='<%= seId %>'>
									  <%= seName %><br>
								<%
									}
								%>
								
							</td>
                        </tr>
                    </table>
                    <br><div class=smallFontC>Check here to view the detailed results: 
                        <% String detailsChecked = "";
                        	if (selectedViewDetails.equalsIgnoreCase("y")){
                        	    detailsChecked = "checked";
                           }
                        %>
                        <input type=checkbox name=viewDetails value='Y' <%= detailsChecked %>></div>
                    <div class=centerOnly>
                   		<a href="javascript:document.bidmanreportform.submit()"><img src="<%= sImagesURL %>button_submit.gif" width="70" height="20" align="top" border="0"></a>
                    </div>
                </form>

                <br>
		<form action=AcctPlanProjRpt name="negnumberreport">

                    <input type=hidden name=page value='projresults'>
                    <input type=hidden name=rptType value=negDetails>
                    <div class=smallFontL>Enter Neg Number to see details for a specific negotiation: 
                            <input class=smallFontL name=negNum size=12 maxlength=12>
                    <div class=centerOnly>
                   <a href="javascript:document.negnumberreport.submit()"><img src="<%= sImagesURL %>button_submit.gif" width="70" height="20" align="top" border="0"></a>
                    </div>
		</form>
            </td>
	</tr>
    </table>
    
    <br><br><br>
</body>
</html>
