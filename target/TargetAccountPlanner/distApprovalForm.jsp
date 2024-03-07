<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.*"%>

<html>
<%@ include file="./SMRCHeader.jsp" %>
<%@ include file="analytics.jsp" %>
<%

Account acct = header.getAccount();
User usr = header.getUser();

DistributorApproval distApproval  = (DistributorApproval)request.getAttribute("distApproval");
ArrayList impactedDistributors = distApproval.getImpactedDistributors();
//ArrayList contacts = (ArrayList)request.getAttribute("contacts");
ArrayList modules = (ArrayList)request.getAttribute("modules");
ArrayList vendors = (ArrayList)request.getAttribute("vendors");

String selectedCategory = (String)request.getAttribute("selectedCategory");

boolean ableToUpdate = usr.ableToUpdate(acct.getDistrict()) || usr.hasSegmentOverride(acct) || (usr.equals(acct.getUserIdAdded()) && acct.isProspect());
//if(!usr.hasOverrideSecurity()){
	if(!acct.isProspect()){
		ableToUpdate=false;
	}
//}


%>

<script language="javascript" src="<%= jsURL %>validation/distApprovalForm.js"></script>
<script language="javascript">
    var previousValue = 0;
	function newWindow(url, windowName) {
		catWindow = window.open(url, windowName, config='width=650,height=475,location=no,scrollbars=yes,toolbar=no,resizeable=yes,status=1')
	}
	function validateDollarAmounts(formObject, hiddenObject){
	    value = formObject.value;
	    enteredValue = removeCurrency(value);
	    if (isNaN(enteredValue)){
	        alert(value + " is not a valid number.");
	        formObject.value = previousValue;
	        
	    } 
    }
    
	function textCounter(field, maxlimit) {
		if (field.value.length > maxlimit) { // if too long...trim it!
			field.value = field.value.substring(0, maxlimit);
			alert("The maximum size for the District Strategy field is 3000 characters.\nThe extra characters have been removed from the end of the field.");
		}
	}

</script>    
    
	</script>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>

		<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Application Summary</span></p> 
    </td>
  </tr>
</table>
<form action="DistributorSignup" name="theform" method="POST" onSubmit="javascript:return formValidation();">
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p>&nbsp;</p>
      <table width="750" border="0" cellspacing="0" cellpadding="0">
        <tr valign="top"> 
          <td width="140"> 
						<%@ include file="./accountLeftNav.jsp" %>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
          </td>
          <td width="10" align="left" background="<%= sImagesURL %>divider.gif">&nbsp;</td>
         <td width="600"> 
					<% if(request.getParameter("save")!=null){ %>
					<blockquote><font class="crumbcurrent">Save Successful</font></blockquote>
					<% }else if(request.getParameter("refresh")!=null){ %>
					<blockquote><font class="crumbcurrent"><img align="absmiddle" src="<%= sImagesURL %>alert.gif">&nbsp;The page refreshed with your selection, but you must click the "Save" button to save your changes</font></blockquote>
					<% } %>     
            <p class="heading2">Application Summary</p>
           <p class="heading3">Account: <%= acct.getCustomerName() %></p>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="25%"><div align="right">Commitment Program Level:</div></td>
					<td width="3%">&nbsp;</td>
					<td width="72%">
						<%
						String commit100="";
						String commit99="";
						String commit59="";
						String commitNA="";
						
						String commitLevel="";
						if(distApproval.getCommitmentLevel().equals("100")){
								commit100 = " checked";
								commitLevel = "100%";
						}else if(distApproval.getCommitmentLevel().equals("99")){
								commit99 = " checked";
								commitLevel = "60-99%";
						}else if(distApproval.getCommitmentLevel().equals("59")){
								commit59 = " checked";
								commitLevel = "Developing";
						}else if(distApproval.getCommitmentLevel().equals("0")){
								commitNA = " checked";
								commitLevel = "Not Applicable";
						}
						if(ableToUpdate){
						%>
						<input type="radio" name="COMMITMENT_LEVEL" value="100"<%= commit100 %>>100%
						<input type="radio" name="COMMITMENT_LEVEL" value="99"<%= commit99 %>>60-99%
						<input type="radio" name="COMMITMENT_LEVEL" value="59"<%= commit59 %>>Developing
						<input type="radio" name="COMMITMENT_LEVEL" value="0"<%= commitNA %>>Not Applicable 
						<% }else{
						out.println(commitLevel);
						} %>
					</td>

			</tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><div align="right">Exclusive commitment?</div></td>

					<td>&nbsp;</td>
					<td>
						<%= StringManipulation.createTextBox("EXCLUSIVE_EATON_COMMITMENT_MON",distApproval.getCommitmentMonth(),ableToUpdate,"2","2") %>/
						<%= StringManipulation.createTextBox("EXCLUSIVE_EATON_COMMITMENT_DAY",distApproval.getCommitmentDay(),ableToUpdate,"2","2") %>/
						<%= StringManipulation.createTextBox("EXCLUSIVE_EATON_COMMITMENT_YEAR",distApproval.getCommitmentYear(),ableToUpdate,"4","4") %>
						<span class="textgray"> (Commitment date MM/DD/YYYY)</span></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>

				</tr>
		 </table>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<input type="hidden" name="PROJECTED_EATON_SALES_YEAR1" value="<%= distApproval.getProjEatonSalesYr1() %>" >
				<input type="hidden" name="PROJECTED_EATON_SALES_YEAR3" value="<%= distApproval.getProjEatonSalesYr3() %>" >
				<tr>
					<td width="25%" valign="top"><div align="right">Projected Total Eaton Sales</div></td>
					<td width="3%" valign="top">&nbsp;</td>
					<td width="15%" valign="top"><div align="center"><span class="textgray">Year 1 ($)</span><br><%= Money.formatDoubleAsDollarsAndCents(distApproval.getProjEatonSalesYr1()) %><br></div></td>
				    <td>&nbsp;</td>
				    <td width="15%" valign="top"><span class="textgray">Year 3 ($)</span><br><%= Money.formatDoubleAsDollarsAndCents(distApproval.getProjEatonSalesYr3()) %></td>
				    <td width="42%" valign="top">&nbsp;</td>
				</tr>
			</table>
			<br>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">

				<tr>
					<td><br>Estimated % Eaton Sales vs. Competitor (i.e, exclusive = 100% Eaton)<br><br></td>
			    </tr>
		 </table>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="25%">&nbsp;</td>
					<td width="3%">&nbsp;</td>

					<td width="15%" class="textgray"><div align="center">Year 1 (%)</div></td>
					<td width="15%" class="textgray"><div align="center">Year 3 (%)</div></td>
				    <td width="42%" class="textgray">&nbsp;</td>
				</tr>
				<%
				for(int i =0;i<vendors.size();i++){
					SummaryCompetitor summaryCompetitor = (SummaryCompetitor)vendors.get(i);
				%>
				<tr>
					<td><div align="right"><font class="crumbcurrent">*</font> <%= summaryCompetitor.getDescription() %>:</div>
					<input type="hidden" name="SUMMARY_COMPETITORS" value="<%= summaryCompetitor.getId() %>">
					<input type="hidden" name="COMPETITOR_NAME_<%= summaryCompetitor.getId()  %>" value="<%= summaryCompetitor.getDescription() %>">					
					</td>
					<td>&nbsp;</td>
					<td>
						<div align="center"><%= StringManipulation.createTextBox("COMPETITOR_YEAR1_" + summaryCompetitor.getId(),Money.formatDoubleAsDollars(summaryCompetitor.getYear1Sales()),ableToUpdate,"10","3") %></div>
				  </td>
					<td>
						<div align="center"><%= StringManipulation.createTextBox("COMPETITOR_YEAR3_" + summaryCompetitor.getId(),Money.formatDoubleAsDollars(summaryCompetitor.getYear3Sales()),ableToUpdate,"10","3") %></div>
					</td>
				    <td>&nbsp;</td>
				</tr>
				<% } %>
			</table>
				<br><br>
			<table width="100%" border="0" cellspacing="1" cellpadding="2">
				<tr>
					<td colspan="3" class="heading3"> Distributors Impacted</td><td colspan="3" class="tableTitle"><% if(ableToUpdate){ %> <a href="javascript:newWindow('CustomerBrowse?distApproval=true&distId=<%= distApproval.getDistributorId() %>','keyAccts')">Add distributor to list</a> <% } %></td>
				</tr>
				<tr>
					<td colspan="6" class="tableTitle">&nbsp;</td>

				</tr>				
				<tr>
					<th width="15%">Name</th>
					<th width="15%">Location</th>
					<th width="15%">Annual Eaton Sales ($)</th>
				    <th width="15%">Eaton Sales at Risk ($)</th>
				    <th width="15%">Contact Notified</th>
				    <th width="25%">Notes</th>
			    </tr>
				<%
				if(impactedDistributors.size()==0){
				%>
					<tr><td colspan="6"><br>No Distributors indicated</td></tr>
				<%
				}

				String shade=null;
				ArrayList contacts = null;
				for(int i=0;i<impactedDistributors.size();i++){
					ImpactedDistributor impactedDist = (ImpactedDistributor)impactedDistributors.get(i);
					contacts = impactedDist.getContacts();
					if(i%2==0){
						shade="";
					}else{
						shade=" class=\"cellShade\"";
					}
				%>
				
				<tr<%= shade %>>
					<td valign="top"><%= impactedDist.getCustomerName() %><input type="hidden" name="IMP_DIST" value="<%= impactedDist.getVcn() %>"><br>
					</td>
					<td valign="top" bgcolor="#E0E0E0"><%= impactedDist.getCity() %>, <%= impactedDist.getState() %></td>
					<td valign="top" align="right"><%= Money.formatDoubleAsDollarsAndCents(impactedDist.getAnnualEatonSales()) %>&nbsp;&nbsp;</td>
				  <td valign="top" align="center">
			    			<%= StringManipulation.createTextBoxWithOnBlur("IMP_DIST_SALES_AT_RISK_" + impactedDist.getVcn(),Money.formatDoubleAsDollarsAndCents(impactedDist.getSalesAtRisk()),ableToUpdate,"10","10","previousValue=" + Money.formatDoubleAsDollarsAndCents(impactedDist.getSalesAtRisk()) + ";validateDollarAmounts(this)") %>
					</td>
				  <td valign="top">
					<% if(ableToUpdate && contacts.size()!=0){ %>
					<select name="IMP_DIST_CONTACT_<%= impactedDist.getVcn() %>">
					<option></option>
					<%
					}
					for(int j=0;j<contacts.size();j++){
						Contact contact = (Contact)contacts.get(j);
						if(ableToUpdate){
							if(contact.getId()==impactedDist.getContactId()){	
							%>
								<option value="<%= contact.getId() %>" selected><%= contact.getLastName() %>, <%= contact.getFirstName() %></option>
							<% }else{ %>
								<option value="<%= contact.getId() %>"><%= contact.getLastName() %>, <%= contact.getFirstName() %></option>
							<%
							 }
						}else{
							if(contact.getId()==impactedDist.getContactId()){	
								out.println(contact.getLastName() + ", " + contact.getFirstName());
								break;
							}
						}	
					}
					if(ableToUpdate && contacts.size()!=0){
					%>
					</select>
					<% } %>
					</td>
				  <td valign="top" align="center">
				    	<%= StringManipulation.createTextBox("IMP_DIST_NOTES_" + impactedDist.getVcn(),impactedDist.getNotes(),ableToUpdate,"") %>
					</td>
			    </tr>		
			    <% } %>			
			</table>
			<br><br>		
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="25%"><div align="right">District Strategy</div></td>
					<td width="3%">&nbsp;</td>
					<td width="70%"><div align="left"><textarea cols="40" rows=4 id="DISTRICT_STRATEGY" name="DISTRICT_STRATEGY" onblur="textCounter(this.form.DISTRICT_STRATEGY,3000);"><%=StringManipulation.noNull(distApproval.getDistrictStrategy())%></textarea></div></td>
					<td width="2%">&nbsp;</td>
				</tr>
			</table>
			<br>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="25%"><div align="right">Net Area Impact:<br>(impact = sum gain / loss)</div></td>

					<td width="3%">&nbsp;</td>
					<td width="15%"><div align="center"><span class="textgray">Year 1 ($)</span><br>
						<%= StringManipulation.createTextBoxWithOnBlur("NET_AREA_IMPACT_YEAR1",Money.formatDoubleAsDollarsAndCents(distApproval.getNetAreaImpactYr1()),ableToUpdate,"10","10","previousValue=" + Money.formatDoubleAsDollarsAndCents(distApproval.getNetAreaImpactYr1()) + ";validateDollarAmounts(this)") %>
						</div></td>
					<td width="15%"><div align="center"><span class="textgray">Year 3 ($)</span><br>
						<%= StringManipulation.createTextBoxWithOnBlur("NET_AREA_IMPACT_YEAR3",Money.formatDoubleAsDollarsAndCents(distApproval.getNetAreaImpactYr3()),ableToUpdate,"10","10","previousValue=" + Money.formatDoubleAsDollarsAndCents(distApproval.getNetAreaImpactYr3()) + ";validateDollarAmounts(this)") %>
						</div></td>
					<td width="42%">&nbsp;</td>
				</tr>
		 </table><br>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="25%"><div align="right">Module(s) Requested:</div></td>
					<td width="3%">&nbsp;</td>
					<td width="72%">
					<%
						for(int i=0;i<modules.size(); i++){
						LoadModule mod = (LoadModule)modules.get(i);
						%>
						<%= mod.getModuleShortCode() %>
					<%
							if(i+1!=modules.size()){
							out.println(", ");
							}
						 
						}
					%>
					</td>
				</tr>
		 </table>
		<br> 
		<%
		if((usr.isChannelMarketingManager() || usr.hasOverrideSecurity()) && ableToUpdate){

			boolean isHVAC = false;
			boolean isManufacturedHousing = false;
			boolean isOtherDistributors = false;
			boolean isMiscStockingDist = false;
			boolean isChannel = false;
			boolean isDIY = false;
			
			ArrayList segments = acct.getSegments();

			for(int i=0;i<segments.size();i++){
				Segment segment = (Segment)segments.get(i);
				if(segment.getName().equals("HVAC / Mechanical Distributors")){
					isHVAC=true;
				}else if(segment.getName().equals("Manufactured Housing")){
					isManufacturedHousing=true;
				}else if(segment.getName().equals("Other Distributors")){
					isOtherDistributors=true;
				}else if(segment.getName().equals("Misc. Stocking Distributor")){
					isMiscStockingDist=true;
				}else if(segment.getName().equals("Channel")){
					isChannel=true;
				}else if(segment.getName().equals("Retail / DIY")){
					isDIY=true;
				}
			}
			
			
			String select01 = "";
			String select09 = "";
			String select10 = "";
			String select13 = "";
			String select14 = "";
			String select15 = "";
			String select19 = "";
			
			if(selectedCategory.trim().length()==0){
				if(isHVAC){
					select13=" selected";
				}else if(isManufacturedHousing){
					select15=" selected";
				}else if(isOtherDistributors && !isHVAC && !isManufacturedHousing){
					select14=" selected";
				}else if(isMiscStockingDist){
					select19=" selected";
				}else if(isChannel && !isOtherDistributors && !isDIY){
					select10=" selected";
				}else if(isDIY){
					select01=" selected";
				}
			}else{
				if(selectedCategory.equals("01")){
					select01=" selected";
				}else if(selectedCategory.equals("09")){					
					select09=" selected";
				}else if(selectedCategory.equals("10")){
					select10=" selected";
				}else if(selectedCategory.equals("13")){
					select13=" selected";
				}else if(selectedCategory.equals("14")){
					select14=" selected";
				}else if(selectedCategory.equals("15")){
					select15=" selected";
				}else if(selectedCategory.equals("19")){
					select19=" selected";
				}
			}

		%>		
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="25%"><div align="right">Customer Category:</div></td>
					<td width="3%">&nbsp;</td>
					<td width="72%">
					<select name="CUSTOMER_CATEGORY">
					<option value=""></option>
					<option value="01"<%= select01 %>>01 - DIY</option>
					<option value="09"<%= select09 %>>09 - WESCO</option>
					<option value="10"<%= select10 %>>10 - INDEPENDENT</option>					
					<option value="13"<%= select13 %>>13 - INDUSTRY SPECIALIST (ACQUIRED)</option>
					<option value="14"<%= select14 %>>14 - INDUSTRY SPECIALIST</option>
					<option value="15"<%= select15 %>>15 - WESCO MFG STR</option>
					<option value="19"<%= select19 %>>19 - MISC STK DISTRB</option>
					</select>
					</td>
				</tr>
		 </table>
			<br><br>
	<% } %>
			
			<% if(ableToUpdate){ %>
			<table width="100%" border="0" cellpadding="0" cellspacing="10">
				<tr>
					<td width="14%"><div align="right"><input type="image" src="<%= sImagesURL %>button_save.gif" width="70" height="20"></div></td>
					<td width="14%"><input type="image" src="<%= sImagesURL %>button_reject.gif" width="70" height="20"></td>
				    <td width="72%"></td>
				</tr>
			</table>
			<% } %>
	      <p>&nbsp;</p>
         </td>

        </tr>
      </table>
    
    </td>
  </tr>
</table>
<input type="hidden" name="acctId" value="<%= distApproval.getVcn() %>">
<input type="hidden" name="page" value="saveApproval">
</form>
<p>&nbsp;</p>

  </body>
</html>
