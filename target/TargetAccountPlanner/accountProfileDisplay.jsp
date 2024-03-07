<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.*"%>
<html>
	<%@ include file="./SMRCHeader.jsp" %>
	<%@ include file="analytics.jsp" %>
	<%
	Account acct = header.getAccount();//(Account)request.getAttribute("acct");
	User usr = header.getUser();//(User)request.getAttribute("usr");
	Address shippingAddress = acct.getShipAddress();
	Address billToAddress = acct.getBillToAddress();
	Address address = acct.getBusinessAddress();
	ArrayList divisions = (ArrayList)request.getAttribute("divisions");
	TreeMap countries = (TreeMap)request.getAttribute("countries");
	ArrayList focusTypes = (ArrayList)request.getAttribute("focusTypes");
	ArrayList specialPrograms = (ArrayList) request.getAttribute("specialPrograms");
	ArrayList applicationCodes = (ArrayList)request.getAttribute("applicationCodes");
	String se1Name = (String)request.getAttribute("se1Name");
	String se2Name = (String)request.getAttribute("se2Name");
	String se3Name = (String)request.getAttribute("se3Name");
	String se4Name = (String)request.getAttribute("se4Name");
	Geography geography = (Geography)request.getAttribute("geography");
	ModifySegmentApprovalsBO approvalRecord = (ModifySegmentApprovalsBO)request.getAttribute("APPROVALRECORD");
	ArrayList modifiedSegments = (ArrayList)request.getAttribute("MODIFIEDSEGMENTS");
	Boolean hasApprovalHistory = (Boolean)request.getAttribute("HASAPPROVALHISTORY");
	ArrayList approvalHistory = (ArrayList)request.getAttribute("APPROVALHISTORY");
	boolean hasApprovalRecord = false;
	if(approvalRecord != null) {
		if(approvalRecord.getRequester() != null || !approvalRecord.getRequester().equals("")) {
			hasApprovalRecord = true;
		}
	}
	
	//String printCountry = (String)request.getAttribute("country");
	
	//boolean ableToUpdate = usr.ableToUpdate(acct.getDistrict()) || usr.hasSegmentOverride(acct) || (usr.equals(acct.getUserIdAdded()) && acct.isProspect());
	boolean ableToUpdate = usr.ableToUpdate(acct) || (usr.equals(acct.getUserIdAdded()) && acct.isProspect());

	boolean ableToUpdateNonVista = ableToUpdate;
	
	String isModify = (String)request.getAttribute("isModify");
	if(usr.hasOverrideSecurity()){
		ableToUpdate=true;
		ableToUpdateNonVista=true;
	} else {
		if(acct.isNewAccount()){
			ableToUpdate=true;
			ableToUpdateNonVista=true;
		}else if(!acct.isActive() && !acct.isProspect()){
			ableToUpdate=true;
			ableToUpdateNonVista=true;
		}else if(!acct.isProspect()){
			ableToUpdate=true;
		}

	}
		
	String disabled="";
	if(!ableToUpdate){
		disabled=" disabled";
	}
	%>
	<script language="javascript" src="<%= jsURL %>validation/accountProfileDisplay.js"></script>
	<script language="javascript">
		
		function showCategoryHistory(whichTable) {
			if(document.getElementById(whichTable).style.display == 'none') {
				document.getElementById(whichTable).style.display = '';
			} else {
				document.getElementById(whichTable).style.display = 'none';
			}
		}
		
			
		
		function approveReject(who,action) {
				window.location = 'ModifySegmentApproveReject?WHO='+who+'&ACTION='+action+'&VCN=<%=acct.getVcn()%>';
		}
		

		function changeCountry(){
			if(document.theform.COUNTRY.value!='777' && document.theform.COUNTRY.value!='888' && document.theform.COUNTRY.value!='778' && document.theform.COUNTRY.value!='031' && document.theform.COUNTRY.value!='803' && document.theform.COUNTRY.value!='813' && document.theform.COUNTRY.value!='036' && document.theform.COUNTRY.value!='085' && document.theform.COUNTRY.value!='811' && document.theform.COUNTRY.value!='079'){
				setInnerHTML('ZIP_DIV','<input type="text" name="ZIP" value="'+document.theform.ZIP.value+'" maxlength="10"></input>');
				setInnerHTML('STATE_DIV','<input type="text" name="STATE" value="'+document.theform.STATE.value+'" maxlength="2"></input>');
				setInnerHTML('CITY_DIV','<input type="text" name="CITY" value="'+document.theform.CITY.value+'" maxlength="25"></input>');
				setInnerHTML('BUTTON_DIV','');
			}else{
			
				setInnerHTML('ZIP_DIV','<%= address.getZip() %><input type="hidden" name="ZIP" value="<%= address.getZip() %>">');
				setInnerHTML('STATE_DIV','<%= address.getState() %><input type="hidden" name="STATE" value="<%= address.getState() %>">');
				setInnerHTML('CITY_DIV','<%= address.getCity() %><input type="hidden" name="CITY" value="<%= address.getCity() %>">');
				setInnerHTML('BUTTON_DIV','<a href="javascript:newWindow(\'ZipcodeBrowse?addr=biz\',\'zip\')"><img align="absmiddle" src="<%= sImagesURL %>button_browse.gif" width="70" height="20" border="0"></a>');
			}
		}
		function changeShipCountry(sameAsBiz){
			if(sameAsBiz=="true"){
				if(document.theform.SHIP_COUNTRY.value!='777' && document.theform.SHIP_COUNTRY.value!='888' && document.theform.SHIP_COUNTRY.value!='778' && document.theform.SHIP_COUNTRY.value!='031' && document.theform.SHIP_COUNTRY.value!='803' && document.theform.SHIP_COUNTRY.value!='813' && document.theform.SHIP_COUNTRY.value!='036' && document.theform.SHIP_COUNTRY.value!='085' && document.theform.SHIP_COUNTRY.value!='811' && document.theform.SHIP_COUNTRY.value!='079'){
					setInnerHTML('SHIP_ZIP_DIV','<input type="text" name="SHIP_ZIP" value="'+document.theform.ZIP.value+'" maxlength="10"></input>');
					setInnerHTML('SHIP_STATE_DIV','<input type="text" name="SHIP_STATE" value="'+document.theform.STATE.value+'" maxlength="2"></input>');
					setInnerHTML('SHIP_CITY_DIV','<input type="text" name="SHIP_CITY" value="'+document.theform.CITY.value+'" maxlength="25"></input>');
					setInnerHTML('SHIP_BUTTON_DIV','');
				}else{
					setInnerHTML('SHIP_ZIP_DIV','<%= address.getZip() %><input type="hidden" name="SHIP_ZIP" value="<%= address.getZip() %>">');
					setInnerHTML('SHIP_STATE_DIV','<%= address.getState() %><input type="hidden" name="SHIP_STATE" value="<%= address.getState() %>">');
					setInnerHTML('SHIP_CITY_DIV','<%= address.getCity() %><input type="hidden" name="SHIP_CITY" value="<%= address.getCity() %>">');
					setInnerHTML('SHIP_BUTTON_DIV','<a href="javascript:newWindow(\'ZipcodeBrowse?addr=ship\',\'zip\')"><img align="absmiddle" src="<%= sImagesURL %>button_browse.gif" width="70" height="20" border="0"></a>');
				}
			}else{
				if(document.theform.SHIP_COUNTRY.value!='777' && document.theform.SHIP_COUNTRY.value!='888' && document.theform.SHIP_COUNTRY.value!='778' && document.theform.SHIP_COUNTRY.value!='031' && document.theform.SHIP_COUNTRY.value!='803' && document.theform.SHIP_COUNTRY.value!='813' && document.theform.SHIP_COUNTRY.value!='036' && document.theform.SHIP_COUNTRY.value!='085' && document.theform.SHIP_COUNTRY.value!='811' && document.theform.SHIP_COUNTRY.value!='079'){
					setInnerHTML('SHIP_ZIP_DIV','<input type="text" name="SHIP_ZIP" value="'+document.theform.SHIP_ZIP.value+'" maxlength="10"></input>');
					setInnerHTML('SHIP_STATE_DIV','<input type="text" name="SHIP_STATE" value="'+document.theform.SHIP_STATE.value+'" maxlength="2"></input>');
					setInnerHTML('SHIP_CITY_DIV','<input type="text" name="SHIP_CITY" value="'+document.theform.SHIP_CITY.value+'" maxlength="25"></input>');
					setInnerHTML('SHIP_BUTTON_DIV','');
				}else{
					setInnerHTML('SHIP_ZIP_DIV','<%= shippingAddress.getZip() %><input type="hidden" name="SHIP_ZIP" value="<%= shippingAddress.getZip() %>">');
					setInnerHTML('SHIP_STATE_DIV','<%= shippingAddress.getState() %><input type="hidden" name="SHIP_STATE" value="<%= shippingAddress.getState() %>">');
					setInnerHTML('SHIP_CITY_DIV','<%= shippingAddress.getCity() %><input type="hidden" name="SHIP_CITY" value="<%= shippingAddress.getCity() %>">');
					setInnerHTML('SHIP_BUTTON_DIV','<a href="javascript:newWindow(\'ZipcodeBrowse?addr=ship\',\'zip\')"><img align="absmiddle" src="<%= sImagesURL %>button_browse.gif" width="70" height="20" border="0"></a>');
				}
			}
		}
		function changeBillToCountry(sameAsBiz){
			if(sameAsBiz=="true"){
				if(document.theform.BILLTO_COUNTRY.value!='777' && document.theform.BILLTO_COUNTRY.value!='888' && document.theform.BILLTO_COUNTRY.value!='778' && document.theform.BILLTO_COUNTRY.value!='031' && document.theform.BILLTO_COUNTRY.value!='803' && document.theform.BILLTO_COUNTRY.value!='813' && document.theform.BILLTO_COUNTRY.value!='036' && document.theform.BILLTO_COUNTRY.value!='085' && document.theform.BILLTO_COUNTRY.value!='811' && document.theform.BILLTO_COUNTRY.value!='079'){
					setInnerHTML('BILLTO_ZIP_DIV','<input type="text" name="BILLTO_ZIP" value="'+document.theform.ZIP.value+'" maxlength="10"></input>');
					setInnerHTML('BILLTO_STATE_DIV','<input type="text" name="BILLTO_STATE" value="'+document.theform.STATE.value+'" maxlength="2"></input>');
					setInnerHTML('BILLTO_CITY_DIV','<input type="text" name="BILLTO_CITY" value="'+document.theform.CITY.value+'" maxlength="25"></input>');
					setInnerHTML('BILLTO_BUTTON_DIV','');
				}else{
					setInnerHTML('BILLTO_ZIP_DIV','<%= address.getZip() %><input type="hidden" name="BILLTO_ZIP" value="<%= address.getZip() %>">');
					setInnerHTML('BILLTO_STATE_DIV','<%= address.getState() %><input type="hidden" name="BILLTO_STATE" value="<%= address.getState() %>">');
					setInnerHTML('BILLTO_CITY_DIV','<%= address.getCity() %><input type="hidden" name="BILLTO_CITY" value="<%= address.getCity() %>">');
					setInnerHTML('BILLTO_BUTTON_DIV','<a href="javascript:newWindow(\'ZipcodeBrowse?addr=billto\',\'zip\')"><img align="absmiddle" src="<%= sImagesURL %>button_browse.gif" width="70" height="20" border="0"></a>');
				}
			}else{
				if(document.theform.BILLTO_COUNTRY.value!='777' && document.theform.BILLTO_COUNTRY.value!='888' && document.theform.BILLTO_COUNTRY.value!='778' && document.theform.BILLTO_COUNTRY.value!='031' && document.theform.BILLTO_COUNTRY.value!='803' && document.theform.BILLTO_COUNTRY.value!='813' && document.theform.BILLTO_COUNTRY.value!='036' && document.theform.BILLTO_COUNTRY.value!='085' && document.theform.BILLTO_COUNTRY.value!='811' && document.theform.BILLTO_COUNTRY.value!='079'){
					setInnerHTML('BILLTO_ZIP_DIV','<input type="text" name="BILLTO_ZIP" value="'+document.theform.BILLTO_ZIP.value+'" maxlength="10"></input>');
					setInnerHTML('BILLTO_STATE_DIV','<input type="text" name="BILLTO_STATE" value="'+document.theform.BILLTO_STATE.value+'" maxlength="2"></input>');
					setInnerHTML('BILLTO_CITY_DIV','<input type="text" name="BILLTO_CITY" value="'+document.theform.BILLTO_CITY.value+'" maxlength="25"></input>');
					setInnerHTML('BILLTO_BUTTON_DIV','');
				}else{
					setInnerHTML('BILLTO_ZIP_DIV','<%= billToAddress.getZip() %><input type="hidden" name="BILLTO_ZIP" value="<%= billToAddress.getZip() %>">');
					setInnerHTML('BILLTO_STATE_DIV','<%= billToAddress.getState() %><input type="hidden" name="BILLTO_STATE" value="<%= billToAddress.getState() %>">');
					setInnerHTML('BILLTO_CITY_DIV','<%= billToAddress.getCity() %><input type="hidden" name="BILLTO_CITY" value="<%= billToAddress.getCity() %>">');
					setInnerHTML('BILLTO_BUTTON_DIV','<a href="javascript:newWindow(\'ZipcodeBrowse?addr=billto\',\'zip\')"><img align="absmiddle" src="<%= sImagesURL %>button_browse.gif" width="70" height="20" border="0"></a>');
				}
			}
		}

	</script>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750">
				<p><br>
					<a class="crumb" href="SMRCHome">Home Page</a>
					<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Account Profile</span></p>
			</td>
		</tr>
	</table>
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
								<% if(request.getParameter("saved")!=null){ %>
								<blockquote><font class="crumbcurrent">Save Successful</font></blockquote>
								<% }else if(request.getParameter("refresh")!=null){ %>
								<blockquote><font class="crumbcurrent"><img align="absmiddle" src="<%= sImagesURL %>alert.gif">&nbsp;The page refreshed with your selection, but you must click the "Save" button to save your changes</font></blockquote>
								<% } %>
								<p class="heading2">Account Profile</p>
								<p class="heading2">Customer Information</p>
								<form name="theform" method="POST" action="AccountProfile" onSubmit="javascript:return formValidation(<%= acct.isProspect() %>);">
								<input type='hidden' name='isModify' value='<%=isModify%>'>
								<% if(ableToUpdate || ableToUpdateNonVista || isModify.equalsIgnoreCase("Y")){ %>
									<table width="100%" border="0" cellspacing="10" cellpadding="0">
										<tr>
											<td width="20%"><div align="left"><input type="image" src="<%= sImagesURL %>button_save.gif" width="70" height="20"></div></td>
											<td width="80%"></td>
										</tr>
									</table>
								<% } %>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td valign="top">
											<table width="100%" border="0" cellspacing="0" cellpadding="0"> <!-- START outter Most table for fields -->
												<tr>
													<td>
														<table width="100%" border="0" cellspacing="0" cellpadding="0"> <!-- START table contained in left half of outter table -->
															<tr>
																<td width="22%"><div align="right"><font class="crumbcurrent">*</font> <b>Customer Name:</b></div></td>
																<td width="4%">&nbsp;</td>
																<td colspan="2" width="78%">
																	<%= StringManipulation.createTextBox("CUSTOMER_NAME",acct.getCustomerName(),ableToUpdate,"40","35") %>
																</td>
															</tr>
															<tr><td colspan="4">&nbsp;</td></tr>
															<tr>
																<td><div align="right"><b>Status:</b></div></td>
																<td>&nbsp;</td>
																<td colspan="2" >
																		<% if(acct.isNewAccount()){ %>
																		Prospect
																		<% }else{ %>
																		<%= acct.getStatus() %>
																		<% } %>
																	</td>
															</tr>
															<tr><td colspan="4">&nbsp;</td></tr>
															<tr>
																<td><div align="right">
																		<%
																		if((acct.getVcn().length()>0 && acct.getVcn().substring(0,1).equals("P")) || acct.getVcn().length()==0){
																		%>
																		<b>Prospect Number:</b>
																		<% }else{ %>
																		<b>Vista Number:</b>
																		<% } %>
																	</div></td>
																<td>&nbsp;</td>
																<td colspan="2" ><%= acct.getVcn() %>
																	<input type="hidden" name="VISTA_CUSTOMER_NUMBER" value="<%= acct.getVcn() %>">
																	<input type="hidden" name="acctId" value="<%= acct.getVcn() %>">
																</td>
															</tr>
															<tr><td colspan="4">&nbsp;</td></tr>
															<tr>
																<td valign="top"><a name="parent"></a><div align="right"><b>Parent Customer:</b><br>
																	<!--
																	<% if(ableToUpdate){ %>
																	<a href="javascript:newWindow('ParentBrowse','parent')">Find</a>
																	<% if(!acct.getParentCustNumber().equals("")){ %>
																	 | <a href="javascript: clearValues('PARENT_NUM','<%= acct.getVcn() %>')">Clear</a>
																	<% } %>	
																	<% } %>
																	-->																																
																</div></td>
																<td>&nbsp;</td>
																<td valign="top" colspan="2">
																	<%
																	if(acct.getParentCustNumber().equals("")){
																	%>
																	<div id="PARENT_NUM_DIV"><i>None Selected</i></div>
																	<%
																	}else{
																		String parentOutput = null;
																		String parentName = acct.getParentName();
																		if ((parentName != null) && (parentName.length() > 0)) {
																			parentOutput = parentName + " (" +  acct.getParentCustNumber() + ")";
																		} else {									
																			parentOutput = acct.getParentCustNumber();		
																		}
																	%>
																		
																	<div id="PARENT_NUM_DIV"><%= parentOutput %></div>
																	<% } %>
																	<input type="hidden" id="PARENT_NUM" name="PARENT_NUM" value="<%= acct.getParentCustNumber() %>">
																	<input type="hidden" id="PARENT_NAME" name="PARENT_NAME" value="<%= acct.getParentName() %>">
																	<% if(ableToUpdate){ %>
																	<img src="<%= sImagesURL %>spacer.gif" border="0" width="1" height="5"><br>
																	<a href="javascript:newWindow('ParentBrowse','parent')"><img src="<%= sImagesURL %>button_browse.gif" width="70" height="20" align="absmiddle" border="0"></a>
																	&nbsp;<a href="javascript: clearValues('PARENT_NUM','<%= acct.getVcn() %>')"><img src="<%= sImagesURL %>button_clear.gif" width="70" height="20" align="absmiddle" border="0"></a>
																	<% } %>
																	
																</td>
															</tr>
															<tr><td colspan="4">&nbsp;</td></tr>
															<tr>
																<td valign="top"><div align="right"><a name="salesengineer"></a><font class="crumbcurrent">*</font> <b>Lead Sales Engineer:</b><br>
																	<!--
																	<% if(ableToUpdate){ %>
																	<a href="javascript:newWindow('SEBrowse?se=1&seid=<%= acct.getSalesEngineer1() %>','SE1')">Find</a> 
																	<% if(!acct.getSalesEngineer1().equals("")){ %>
																	 | <a href="javascript: clearValues('SALES_ENGINEER1','<i>Not Selected<i>')">Clear</a>
																	<% } %>
																	<% } %>
																	-->
																</div></td>
																<td>&nbsp;</td>
																<td valign="top" colspan="2">
																	<%
																	if(acct.getSalesEngineer1().equals("")){
																	%>
																	<div id="SALES_ENGINEER1_DIV"><i>Not Selected<i></div>
																	<%
																	}else{
																	%>
																	<div id="SALES_ENGINEER1_DIV"><%= acct.getSalesEngineer1() %>&nbsp;&nbsp;<%= se1Name %></div>
																	<% } %>
																	<input type="hidden" id="SALES_ENGINEER1" name="SALES_ENGINEER1" value="<%= acct.getSalesEngineer1() %>">
																	<% if(ableToUpdate){ %>
																	<img src="<%= sImagesURL %>spacer.gif" border="0" width="1" height="5"><br>
																	 <a href="javascript:newWindow('SEBrowse?se=1&seid=<%= acct.getSalesEngineer1() %>','SE1')"><img src="<%= sImagesURL %>button_browse.gif" width="70" height="20" align="absmiddle" border="0"></a>
																	&nbsp;<a href="javascript: clearValues('SALES_ENGINEER1','<i>Not Selected<i>')"><img src="<%= sImagesURL %>button_clear.gif" width="70" height="20" align="absmiddle" border="0"></a>
																	
																	<% } %>
																	
																</td>
															</tr>
															<tr><td colspan="4">&nbsp;</td></tr>
															<tr>
																<td valign="top"><div align="right"><b>Sales Engineer 2:</b><br>
																	<!--
																	<% if(ableToUpdate){ %>
																	<a href="javascript:newWindow('SEBrowse?se=2&seid=<%= acct.getSalesEngineer2() %>','SE2')">Find</a>
																	<%
																	if(!acct.getSalesEngineer2().equals("")){ %>
																	 | <a href="javascript: clearValues('SALES_ENGINEER2','<i>Not Selected<i>')">Clear</a>
																	<% }
																	} %>
																	-->
																</div></td>
																<td>&nbsp;</td>
																<td valign="top" colspan="2">
																	<%
																	if(acct.getSalesEngineer2().equals("")){
																	%>
																	<div id="SALES_ENGINEER2_DIV"><i>Not Selected</i></div>
																	<%
																	}else{
																	%>
																	<div id="SALES_ENGINEER2_DIV"><%= acct.getSalesEngineer2() %>&nbsp;&nbsp;<%= se2Name %></div>
																	<% } %>
																	<input type="hidden" id="SALES_ENGINEER2" name="SALES_ENGINEER2" value="<%= acct.getSalesEngineer2() %>">
																	<% if(ableToUpdate){ %>
																	<img src="<%= sImagesURL %>spacer.gif" border="0" width="1" height="5"><br>
																	<a href="javascript:newWindow('SEBrowse?se=2&seid=<%= acct.getSalesEngineer2() %>','SE2')"><img src="<%= sImagesURL %>button_browse.gif" width="70" height="20" align="absmiddle" border="0"></a>
																	&nbsp;<a href="javascript: clearValues('SALES_ENGINEER2','<i>Not Selected<i>')"><img src="<%= sImagesURL %>button_clear.gif" width="70" height="20" align="absmiddle" border="0"></a>
																	<% } %>
																	
																</td>
															</tr>
															<tr><td colspan="4">&nbsp;</td></tr>
															<tr>
																<td valign="top"><div align="right"><b>Sales Engineer 3:</b><br>
																	<!--
																	<% if(ableToUpdate){ %>
																	<a href="javascript:newWindow('SEBrowse?se=3&seid=<%= acct.getSalesEngineer3() %>','SE3')">Find</a>
																	<%
																	if(!acct.getSalesEngineer3().equals("")){ %>
																	 | <a href="javascript: clearValues('SALES_ENGINEER3','<i>Not Selected<i>')">Clear</a>
																	<% }
																	} %>
																	-->
																</div></td>
																<td>&nbsp;</td>
																<td valign="top" colspan="2">
																	<%
																	if(acct.getSalesEngineer3().equals("")){
																	%>
																	<div id="SALES_ENGINEER3_DIV"><i>Not Selected</i></div>
																	<%
																	}else{
																	%>
																	<div id="SALES_ENGINEER3_DIV"><%= acct.getSalesEngineer3() %>&nbsp;&nbsp;<%= se3Name %></div>
																	<% } %>
																	<input type="hidden" id="SALES_ENGINEER3" name="SALES_ENGINEER3" value="<%= acct.getSalesEngineer3() %>">
																	<% if(ableToUpdate){ %>
																	<img src="<%= sImagesURL %>spacer.gif" border="0" width="1" height="5"><br>
																	<a href="javascript:newWindow('SEBrowse?se=3&seid=<%= acct.getSalesEngineer3() %>','SE3')"><img src="<%= sImagesURL %>button_browse.gif" width="70" height="20" align="absmiddle" border="0"></a>
																	&nbsp;<a href="javascript: clearValues('SALES_ENGINEER3','<i>Not Selected<i>')"><img src="<%= sImagesURL %>button_clear.gif" width="70" height="20" align="absmiddle" border="0"></a>
																	<% } %>
																	
																</td>
															</tr>
															<tr><td colspan="4">&nbsp;</td></tr>
															<tr>
																<td valign="top"><div align="right"><b>Sales Engineer 4:</b><br>
																	<!--
																	<% if(ableToUpdate){ %>
																	<a href="javascript:newWindow('SEBrowse?se=4&seid=<%= acct.getSalesEngineer4() %>','SE4')">Find</a>
																	<%
																	if(!acct.getSalesEngineer4().equals("")){ %>
																	 | <a href="javascript: clearValues('SALES_ENGINEER4','<i>Not Selected<i>')">Clear</a>
																	<% }
																	} %>
																	-->
																</div></td>
																<td>&nbsp;</td>
																<td valign="top" colspan="2">
																	<%
																	if(acct.getSalesEngineer4().equals("")){
																	%>
																	<div id="SALES_ENGINEER4_DIV"><i>Not Selected</i></div>
																	<%
																	}else{
																	%>
																	<div id="SALES_ENGINEER4_DIV"><%= acct.getSalesEngineer4() %>&nbsp;&nbsp;<%= se4Name %></div>
																	<% } %>
																	<input type="hidden" id="SALES_ENGINEER4" name="SALES_ENGINEER4" value="<%= acct.getSalesEngineer4() %>">
																	<% if(ableToUpdate){ %>
																	<img src="<%= sImagesURL %>spacer.gif" border="0" width="1" height="5"><br>
																	<a href="javascript:newWindow('SEBrowse?se=4&seid=<%= acct.getSalesEngineer4() %>','SE4')"><img src="<%= sImagesURL %>button_browse.gif" width="70" height="20" align="absmiddle" border="0"></a>
																	&nbsp;<a href="javascript: clearValues('SALES_ENGINEER4','<i>Not Selected<i>')"><img src="<%= sImagesURL %>button_clear.gif" width="70" height="20" align="absmiddle" border="0"></a>
																	<% } %>
																	
																</td>
															</tr>
															<tr><td colspan="4">&nbsp;</td></tr>
															<tr>
																<td><div align="right"><b>District:</b></div></td>
																<td>&nbsp;</td>
																<td colspan="2">
																	<%
																	if(acct.getDistrict().equals("")){
																		out.println("N/A");
																	}else{
																		%>
																	<%= acct.getDistrict() %> - <%= geography.getDescription()  %>
																	<%
																	}
																	%>
																	<input type="hidden" name="DISTRICT" value="<%= acct.getDistrict() %>">
																</td>
															</tr>
															<tr><td colspan="4">&nbsp;</td></tr>
															<tr>
																<td valign="top"><a name="segments"></a><div align="right"><font class="heading3">Customer Categories: </font></div></td>
																<td>&nbsp;</td>
																<%if(usr.hasOverrideSecurity()) { %>
																<td colspan="2">
																	<a href="javascript:newWindow('SegmentSelect?acctId=<%= acct.getVcn() %>&isModify=N','SegmentSelect')"><img src="<%= sImagesURL %>button_browse.gif" width="70" height="20" align="absmiddle" border="0"></a>
																</td>
																<% }else { %>
																<td colspan="2">
																	<% if(acct.isNewAccount() || acct.isProspect()) { %>
																		<% if(ableToUpdateNonVista){ %>
																			<a href="javascript:newWindow('SegmentSelect?acctId=<%= acct.getVcn() %>&isModify=N','SegmentSelect')"><img src="<%= sImagesURL %>button_browse.gif" width="70" height="20" align="absmiddle" border="0"></a>
																		<% } %>
																	<% } %>
																</td>
																<% } %>
															</tr>
															<tr><td colspan="4">&nbsp;</td></tr>
															<tr>
																<td valign="top"><div align="right"><font class="crumbcurrent">*</font> <b>Primary:</b> </div></td>
																<td>&nbsp;</td>
																<td>
																	<%
																	ArrayList segments = acct.getSegments();
																	boolean segmentFound=false;
																	for(int i=0;i<segments.size();i++){
																		Segment segment = (Segment)segments.get(i);
																		if(segment.getLevel()==1){
																	 %>
																	<%= segment.getName() %><input type="hidden" name="SEGMENTS" value="<%= segment.getSegmentId() %>"><br>
																	<%
																	segmentFound=true;
																	break;
																	}
																	}
																	if(!segmentFound){
																	%>
																	<i>Not Selected</i><br>
																	<%
																	}
																	%>
																</td>
																<td rowspan='5' align='top'>
																	<% if(hasApprovalRecord) { %>
																		<table border='0' cellspacing='4'>
																			<tr>
																				<td>
																					<%=approvalRecord.getRequester()%> has requested that <br>this account's categories be changed:<br>
																					<% 
																						for(int i=0; i < modifiedSegments.size(); i++) {
																							out.print("<li>" + modifiedSegments.get(i));
																						}
																					%>
																				</td>
																			</tr>
																			<tr>
																				<td>
																					<% if(approvalRecord.getDmApproval().equalsIgnoreCase("N")) { %>
																						<% if(usr.hasOverrideSecurity() || usr.isDistrictManager() || usr.isChannelMarketingManager()) { %>
																							District Manager:&nbsp;
																							<a href="#" onClick='javascript:approveReject("dm","Y")'>Approve</a>
				   	     																		| 
				   	     																		<a href="#" onClick='javascript:approveReject("dm","R")'>Reject</a>
																						<% } else { %>
																							This segment change is awaiting approval from <%=approvalRecord.getDm()%>
																						<% } %>
																					<% } else if(approvalRecord.getDmApproval().equalsIgnoreCase("Y")) { %>
																						<%=approvalRecord.getDm()%>&nbsp;Approved on&nbsp;<%=approvalRecord.getDmDate()%>
																					<% } else if(approvalRecord.getDmApproval().equalsIgnoreCase("R")) { %>
																						<%=approvalRecord.getDm()%>&nbsp;Rejected on&nbsp;<%=approvalRecord.getDmDate()%>
																					<% } %>
																				</td>
																			</tr>
																			<tr>
																				<td>
																					<% if(approvalRecord.getDmApproval().equalsIgnoreCase("Y")) { %>
																						<%if(approvalRecord.getChannelApproval().equalsIgnoreCase("R")) { %>
																							<%=approvalRecord.getChannel()%>&nbsp;Rejected on&nbsp;<%=approvalRecord.getChannelDate()%>
																						<% } else if(approvalRecord.getChannelApproval().equalsIgnoreCase("Y")) { %>
																							<%=approvalRecord.getChannel()%>&nbsp;Approved on&nbsp;<%=approvalRecord.getChannelDate()%>
																						<% } else { %>
																							<% if(usr.hasOverrideSecurity() || usr.isChannelMarketingManager()) { %>
																								Channel Manager:&nbsp;
																								<a href="#" onClick='javascript:approveReject("cm","Y")'>Approve</a>
																								| 
				   	     																			<a href="#" onClick='javascript:approveReject("cm","R")'>Reject</a>
				   	     																		<% } else { %>
																								This segment change is awaiting approval from <%=approvalRecord.getChannel()%>
																							<% } %>
				   	     																	<% } %>
																					<% } %>
																				</td>
																			</tr>
																		</table>
																	<% } %>
																	
																	
																</td>
															</tr>
															<tr><td colspan="3">&nbsp;</td></tr>
															<tr>
																<td valign="top"><div align="right"><font class="crumbcurrent">*</font> <b>Secondary:</b> </div></td>
																<td>&nbsp;</td>
																<td>
																	<%
																	segmentFound=false;
																	for(int i=0;i<segments.size();i++){
																		Segment segment = (Segment)segments.get(i);
																		if(segment.getLevel()==2){
																	 %>
																	<%= segment.getName() %><input type="hidden" name="SEGMENTS" value="<%= segment.getSegmentId() %>"><br>
																	<%
																	segmentFound=true;
																	break;
																	}
																	}
																	if(!segmentFound){
																	%>
																	<i>Not Selected</i><br>
																	<%
																	}
																	%>
																</td>
															</tr>
															<tr><td colspan="3">&nbsp;</td></tr>
															<tr>
																<td valign="top"><div align="right"><b>Sub-Categories:</b> </div></td>
																<td>&nbsp;</td>
																<td>
																	<%
																	segmentFound=false;
																	for(int i=0;i<segments.size();i++){
																		Segment segment = (Segment)segments.get(i);
																		if(segment.getLevel()==3 || segment.getLevel()==4){
																	 %>
																	<%= segment.getName() %><input type="hidden" name="SEGMENTS" value="<%= segment.getSegmentId() %>"><br>
																	<%
																	segmentFound=true;
																	}
																	}
																	if(!segmentFound){
																	%>
																	<i>None Selected</i><br>
																	<%
																	}
																	%>
																</td>
															</tr>
															<tr><td colspan="4">&nbsp;</td></tr>
															
															
															<% if(hasApprovalHistory.booleanValue()) { %>
															<tr>
																<td colspan='3'>
																	&nbsp;
																	<p>
																	<a href='javascript:showCategoryHistory("categoryHistory");'>Customer Category Modification History</a>
																	
																	<div id="categoryHistory" style="display:none">
																		<table border='0' cellspacing='4'>
																			<% for(int i=0; i < approvalHistory.size(); i++) { %>
																				<% ModifySegmentsHistoryBO msh = (ModifySegmentsHistoryBO)approvalHistory.get(i); %>
																				<tr>
																					<td>District:</td>
																					<td><%=msh.getDm()%></td>
																					<td><%=msh.getDmApproval().equalsIgnoreCase("Y")?"Approved":"Rejected"%></td>
																					<td><%=msh.getDmDate()%></td>
																				</tr>
																				<tr>
																					<td>Channel:</td>
																					<td><%=msh.getChannel()%></td>
																					<td>
																						<% 
																							if(msh.getChannelApproval().equalsIgnoreCase("Y")) {
																								out.print("Approved");
																							} else if(msh.getChannelApproval().equalsIgnoreCase("R")) {
																								out.print("Rejected");
																							} else if(msh.getChannelApproval().equalsIgnoreCase("D")) {
																								out.print("Rejected at District");
																							}
																						%>
																					
																					</td>
																					<td><%=msh.getChannelDate()%></td>
																				</tr>
																				<tr>
																					<td colspan='4'>
																						<% for(int j=0; j < msh.getSegments().size(); j++) { %>
																							<li><%=msh.getSegments().get(j)%>
																						<% } %>	
																					</td>
																				</tr>
																				<tr>
																					<td colspan='3'><hr></td>
																				</tr>
																			<% } %>
																		</table>
																	</div>
																</td>
															</tr>
															<% } %>
														</table>
														<!-- END table contained in left half of outter table -->
													</td>
													<td valign="top">
														<!--END table contained in right half of outter table -->
													</td>
												</tr>
											</table>
											<br>
											<hr align="center" width="600" size="1" color="#999999" noshade>
											<span class="heading2">Business Information </span><br><br>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr><td width="50%" valign="top">
														<table width="100%" border="0" cellspacing="0" cellpadding="0">
															<%
															String directFlag = "";
															String parentOnly = "";
															String exemptCertRequired = "";
															String sendConfirmation = "";
															String checked = " checked";
															//Changes for distributor check
																if(acct.isDirect()){
																directFlag=checked;
															}
															if(acct.isParentOnly()){
																parentOnly=checked;
															}
															if(acct.isExemptCertRequired()){
																exemptCertRequired=checked;
															}
															if(acct.isSendConfirmation()){
																sendConfirmation=checked;
															}
															%>
															<tr>
																<td width="5%">&nbsp;</td><td colspan="2" width="95%">Options: </td>
															</tr>
															<tr>
																<td width="5%">&nbsp;</td>
																<td colspan="2" valign="top" width="95%">
																	<input type="checkbox" name="DIRECT_FLAG" value="Y"<%= directFlag + disabled %>>Direct
																	<% if(!ableToUpdate && acct.isDirect()){ %>
																		<input type="hidden" name="DIRECT_FLAG" value="Y">
																	<% } %>
																		
																	<% if(acct.isDistributor()){ %>
																		<input type="hidden" name="DIST_FLAG" value="Y">
																	<%} else{ %>
																		<input type="hidden" name="DIST_FLAG" value="N">
																	<%} %>
																</td>
															</tr>
															<tr>
																<td width="5%">&nbsp;</td>
																<td colspan="2" valign="top" width="95%">
																	<input type="checkbox" name="EXEMPT_CERT_REQUIRED" value="Y"<%= exemptCertRequired + disabled %>>Excemption Certificate on File
																	<% if(!ableToUpdate && acct.isExemptCertRequired()){ %>
																	<input type="hidden" name="EXEMPT_CERT_REQUIRED" value="Y">
																	<% } %>
																</td>
															</tr>
															<tr>
															<!--<tr>
																	<td width="5%">&nbsp;</td>
															 	<td colspan="2" valign="top" width="95%">
																		<input type="checkbox" name="PARENT_ONLY_FLAG" value="Y"<%= parentOnly + disabled %>>Parent Only
																	</td>
															  </tr>-->
															<tr>
																<td width="5%">&nbsp;</td>
																<td colspan="2" valign="top" width="95%">
																	<input type="checkbox" name="SEND_CONFIRMATION" value="Y"<%= sendConfirmation + disabled %>>Order Confirmation Print
																	<% if(!ableToUpdate && acct.isSendConfirmation()){ %>
																	<input type="hidden" name="SEND_CONFIRMATION" value="Y">
																	<% } %>																
																</td>
															</tr>
															<tr>
																<td colspan="3">&nbsp;</td>
															</tr>
															<tr>
																<td colspan="3">&nbsp;</td>
															</tr>
														</table>
														<table width="100%" border="0" cellspacing="0" cellpadding="0">
														<tr>
															<td width="60%"><div align="right">Number of Employees:</div></td>
															<td width="3%">&nbsp;</td>
															<td width="37%">
																<%= StringManipulation.createTextBox("NUM_OF_VALUE",acct.getNumOfWhatever(),ableToUpdateNonVista,"","10") %>
															</td>
														</tr>
														<tr><td colspan="3">&nbsp;</td></tr>
														<tr>
														<td width="60%"><div align="right">Application:</div></td>
														<td width="3%">&nbsp;</td>
														<td width="37%">
															<%
															int selectedCodeId=0;
															if(ableToUpdateNonVista){
															%>
															<select name="APPLICATION_ID">
																<option selected value="0">Select One...</option>
																<%
																selectedCodeId = acct.getApplicationCode();
																for(int i=0;i<applicationCodes.size();i++){
																	CodeType code = (CodeType)applicationCodes.get(i);
																	if(code.getId()==selectedCodeId){
																%>
																<option value="<%= code.getId() %>" selected><%= code.getName() %></option>
																<% }else{ %>
																<option value="<%= code.getId() %>"><%= code.getName() %></option>
																<% }
																}
																 %>
															</select>
															<% }else{
															%>
															<%
															selectedCodeId = acct.getApplicationCode();
															for(int i=0;i<applicationCodes.size();i++){
															CodeType code = (CodeType)applicationCodes.get(i);
															if(code.getId()==selectedCodeId){
															%>
															<%= code.getName() %>
															<input type="hidden" name="APPLICATION_ID" value="<%= code.getId() %>">
															<%
															break;
															}
															}
															%>
															<% } %>
													</td>
												</tr>
												<tr><td colspan="3">&nbsp;</td></tr>
												<tr>
												<td width="60%"><div align="right">Focus Type:</div></td>
												<td width="3%">&nbsp;</td>
												<td width="37%">
													<%
													int selectedFocusType = acct.getFocusType();
													if(ableToUpdateNonVista){
													%>
													<select name="FOCUS_TYPE">
														<option selected value="0">Select One...</option>
														<%
														
														for(int i=0;i<focusTypes.size();i++){
															FocusType focusType = (FocusType)focusTypes.get(i);
															if(focusType.getId()==selectedFocusType){
																%>
																<option value="<%= focusType.getId() %>" selected><%= focusType.getDescription() %></option>
																<% }else{ %>
																<option value="<%= focusType.getId() %>"><%= focusType.getDescription() %></option>
																<%
															}
														}
														 %>
													</select>
													<% }else{
													%>
													<%

													for(int i=0;i<focusTypes.size();i++){
														FocusType focusType = (FocusType)focusTypes.get(i);
														if(focusType.getId()==selectedFocusType){
															%>
															<%= focusType.getDescription() %>
															<input type="hidden" name="FOCUS_TYPE" value="<%= focusType.getId() %>">
															<%
															break;
														}
													}
													%>
													<% } %>
											</td>
										</tr>
										<tr><td colspan="3">&nbsp;</td></tr>
										<tr>
										<td width="60%" nowrap><div align="right">Special&nbsp;&nbsp;&nbsp;<br>Program(s):</div></td>
										<td width="3%">&nbsp;</td>
										<td width="37%" nowrap>
											<%
											ArrayList acctSpecialPrograms = acct.getSpecialProgramIds();
											if(ableToUpdateNonVista){
											%>
											<select name="SPECIAL_PROGRAMS" size="3" multiple>
												<option <%= (acctSpecialPrograms.size() == 0)?" selected ": " " %>value="0">None</option>
												<%
												
												for(int i=0;i<specialPrograms.size();i++){
													DropDownBean specialProgram = (DropDownBean)specialPrograms.get(i);
													String spSelected = "";
													for (int x=0; x<acctSpecialPrograms.size(); x++){
													    String thisSpecialProgram = (String) acctSpecialPrograms.get(x);
													    if (thisSpecialProgram.equalsIgnoreCase(specialProgram.getValue())){
													        spSelected = " selected ";
													    }
													}
													%>
													<option value="<%= specialProgram.getValue() %>" <%= spSelected%>><%= specialProgram.getName() %></option>
													<%
												}
												 %>
											</select>
											<% }else{
											%>
											<%

											for(int i=0;i<specialPrograms.size();i++){
											    DropDownBean specialProgram = (DropDownBean)specialPrograms.get(i);
											    for (int x=0; x<acctSpecialPrograms.size(); x++){
												    String thisSpecialProgram = (String) acctSpecialPrograms.get(x);
												    if (thisSpecialProgram.equalsIgnoreCase(specialProgram.getValue())){
												        %>
														<%= specialProgram.getName() %><br>
														<input type="hidden" name="SPECIAL_PROGRAMS" value="<%= specialProgram.getValue() %>">
														<%
												    }
												}
											}
											%>
											<% } %>
									</td>
								</tr>
											</table>
										</td><td valign="top" width="50%">
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="40%"><div align="right">Target Account:</div></td>
													<td width="3%">&nbsp;</td>
													<td width="56%">
														<%
														if(ableToUpdateNonVista && (usr.isTeamLeader() || usr.isDistrictManager() || usr.isSalesEngineer())){
														%>
														<select name="TARGET_ACCOUNTS" size="5" multiple>
															<%
															if(acct.isDistrictTargetAccount()){
															%>
															<option value="DISTRICT" selected>District</option>
															<%
															}else{
															%>
															<option value="DISTRICT">District</option>
															<%
															     }
															    ArrayList targetAccounts = acct.getDivisionTargetAccount();
															boolean found=false;
															for(int i=0;i<divisions.size();i++){
															Division division = (Division)divisions.get(i);
															found=false;
															for(int j=0;j<targetAccounts.size();j++){
															if(division.getId().equals(targetAccounts.get(j))){
																%>
															<option value="<%= division.getId() %>" selected><%= division.getName() %></option>
															<%
															found=true;
															break;
															}
															}
															if(found==false){
															%>
															<option value="<%= division.getId() %>"><%= division.getName() %></option>
															<%
															}
															}
															}else{
															boolean isTarget = false;
															if(acct.isDistrictTargetAccount()){
															out.println("<input type=\"hidden\" name=\"TARGET_ACCOUNTS\" value=\"DISTRICT\"> District<br>");
															isTarget=true;
															}
															ArrayList targetAccounts = acct.getDivisionTargetAccount();
															for(int i=0;i<divisions.size();i++){
															Division division = (Division)divisions.get(i);
															for(int j=0;j<targetAccounts.size();j++){
															if(division.getId().equals(targetAccounts.get(j))){
															isTarget=true;
															%><input type="hidden" name="TARGET_ACCOUNTS" value="<%= division.getId() %>">
															<%= division.getName() %><br>
															<%
															}
															}
															}
															if(!isTarget){
															out.println("No");
															}
															}
															%>
														</select>
													</td>
												</tr>
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
											</table>
											<%
											if(!acct.isNewAccount()){
											%>
											<br>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="22%">&nbsp;</td>
													<td width="78%"><a href="javascript:newWindow('AccountProfile?page=associatedUsers&acctId=<%= acct.getVcn() %>&update=add','users')">Associate yourself to this account</a></td>
												</tr>
												<tr>
													<td>&nbsp;</td>
													<td><a href="javascript:newWindow('AccountProfile?page=associatedUsers&acctId=<%= acct.getVcn() %>','users')">Users Associated to Account</a></td>
												</tr>
											</table>
											<% } %>
										</td></tr></table>
								<br><br>
								<!-- END outter most table -->
								<hr align="center" width="600" size="1" color="#999999" noshade>
								<p><a name="biz"></a><span class="heading2">Business	Address </span></p>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<!-- START outter Most table for fields -->
									<tr>
										<td width="100%" valign="top">
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<!-- START table contained in left half of outter table -->
												<tr>
													<td width="25%"><div align="right"><font class="crumbcurrent">*</font> Line 1:</div>
													</td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<%= StringManipulation.createTextBox("ADDRESS_LINE1",address.getAddress1(),ableToUpdate,"","35") %>
													</td>
												</tr>
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												<tr>
													<td width="25%"><div align="right">Line 2:</div>
													</td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<%= StringManipulation.createTextBox("ADDRESS_LINE2",address.getAddress2(),ableToUpdate,"","35") %>
													</td>
												</tr>
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												<tr>
													<td width="25%"><div align="right">Line 3:</div>
													</td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<%= StringManipulation.createTextBox("ADDRESS_LINE3",address.getAddress3(),ableToUpdate,"","35") %>
													</td>
												</tr>
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												<tr>
													<td width="25%"><div align="right">Line 4:</div>
													</td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<%= StringManipulation.createTextBox("ADDRESS_LINE4",address.getAddress4(),ableToUpdate,"","35") %>
													</td>
												</tr>
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												<tr>
													<td width="25%"><div align="right"><font class="crumbcurrent">*</font> City:</div></td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<div id="CITY_DIV">
														<% if(!ableToUpdate) out.println(address.getCity()); %>
														<input type="hidden" name="CITY" value="<%= address.getCity() %>">
														</div>
													</td>
												</tr>
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												<tr>
													<td width="25%"><div align="right">State:</div></td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<div id="STATE_DIV">
														<% if(!ableToUpdate) out.println(address.getState()); %>
														<input type="hidden" name="STATE" value="<%= address.getState() %>">
														</div>
													</td>
												</tr>
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												<tr>
													<td width="25%"><div align="right">Zip:</div></td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<div id="ZIP_DIV">
														<% if(!ableToUpdate) out.println(address.getZip()); %>
														<input type="hidden" name="ZIP" value="<%= address.getZip() %>">
														</div>
													</td>
												</tr>
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												<% if(ableToUpdate){ %>
												<tr>
													<td width="25%"><div align="right"><i>City Locator</i></div></td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<div id="BUTTON_DIV"></div>
													</td>
												</tr>
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												<% } %>
												<tr>
													<td width="25%"><div align="right"><font class="crumbcurrent">*</font> Country:</div></td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<%
														String myCountry = address.getCountry();
														if(myCountry.equals("")){
														myCountry="777";
														}
														if(ableToUpdate){
														%>
														<select onchange="javascript: changeCountry()" name="COUNTRY" id="country">
															<option value="">Select One...</option>
															<%
															Set countrySet = countries.entrySet();
															   Iterator it2 = countrySet.iterator();
															   while (it2.hasNext()){
															     java.util.Map.Entry country = (java.util.Map.Entry) it2.next();
															    	if(country.getValue().equals(myCountry)){
																%>
															<option value="<%= country.getValue() %>" selected><%= country.getKey() %></option>
															<%
															}else{
															%>
															<option value="<%= country.getValue() %>"><%= country.getKey() %></option>
															<%
															}
															
															  }
															%>
														</select>
														<% }else{
														
																	Set countrySet = countries.entrySet();
															   	Iterator it2 = countrySet.iterator();
															   	while (it2.hasNext()){
															     	java.util.Map.Entry country = (java.util.Map.Entry) it2.next();
															    	if(country.getValue().equals(myCountry)){
																		out.println(country.getKey());
																		break;
																		}
																	}
																%>
																<input type="hidden" name="COUNTRY" value="<%= myCountry %>">
														<% } %>
													</td>
												</tr>
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												<tr>
													<td><div align="right"><font class="crumbcurrent">*</font> Phone:</div>
													</td>
													<td>&nbsp;</td>
													<td>
														<%
														String phone = null;
														if(acct.getPhone().trim().length()!=0){
															phone=acct.getPhone();
														}else{
															phone=acct.getIntlPhoneNumber();
														}
														
														String fax = null;
														if(acct.getFax().trim().length()!=0){
															fax=acct.getFax();
														}else{
															fax=acct.getIntlFaxNumber();
														}
														
														String apCont = null;
														if(acct.getAPCont().trim().length()!=0){
															apCont=acct.getAPCont();
														}else{
															apCont="";
														}
														
														String apContPhone = null;
														if(acct.getAPContPhoneNumber().trim().length()!=0){
															apContPhone=acct.getAPContPhoneNumber();
														}else{
															apContPhone="";
														}
															
														
														String apContEmail = null;
														if(acct.getAPContEmailAddress().trim().length()!=0){
															apContEmail=acct.getAPContEmailAddress();
														}else{
															apContEmail="";
														}
														%>
														<%= StringManipulation.createTextBox("PHONE_NUMBER",phone,ableToUpdate,"","21") %><% if(ableToUpdate) out.println("&nbsp;i.e. 4125551234");  %>
													</td>
												</tr>
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												<tr>
													<td width="25%"><div align="right"><span class="crumbcurrent">*</span>Fax: </div>
													</td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<%= StringManipulation.createTextBox("FAX_NUMBER",fax,ableToUpdate,"","21") %><% if(ableToUpdate) out.println("&nbsp;i.e. 4125551234");  %>
													</td>
												</tr>
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												<tr>
													<td width="25%"><div align="right">AP Contact</div></td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<%= StringManipulation.createTextBox("APCONTACT",apCont,ableToUpdate,"","50") %>
													</td>
												</tr>
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												<tr>
													<td width="25%"><div align="right">AP Contact Phone Number:</div></td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<%= StringManipulation.createTextBox("APCONTACT_PHONE_NUMBER",apContPhone,ableToUpdate,"","21") %><% if(ableToUpdate) out.println("&nbsp;i.e. 4125551234");  %>
													</td>
												</tr>
												<tr><td colspan="4">&nbsp;</td></tr>
												<tr>
													<td width="25%"><div align="right">AP Contact Email Address:</div></td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<%= StringManipulation.createTextBox("APCONTACT_EMAIL_ADDRESS",apContEmail,ableToUpdate,"","100") %> <% if(ableToUpdate) out.println("&nbsp;i.e. example@host.com");  %>
													</td>
												</tr>
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												<tr>
													<td width="25%"><div align="right">Website: </div>
													</td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<%= StringManipulation.createTextBox("WEB_SITE",acct.getWebsite(),ableToUpdate,"","150") %>
													</td>
												</tr>
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
											</table>
											<!-- END table contained in left half of outter table -->
										</td>
									</tr>
								</table>
								<br><br>
								<hr align="center" width="600" size="1" color="#999999" noshade>
								<table width="100%"><tr><td width="100%">
											<a name="ship"></a><p class="heading2">Shipping Address</p>
											<% if(ableToUpdate){ %>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="4%"><div align="center"></div></td>
													<td width="92%" bgcolor="#EEEEEE">
														<!--<input type="checkbox" name="SHIP_CHECK" onclick="javascript: clickShip()" value="Y">Check if same as Business Address-->
														<a href="javascript: clickShip()">Click here to copy business address to shipping address.</a>
													</td>
													<td width="4%">&nbsp;</td>
												</tr>
											</table>
											<br>
											<% } %>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="25%">
														<div align="right">Line 1:</div>
													</td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<%= StringManipulation.createTextBox("SHIP_ADDRESS_LINE1",shippingAddress.getAddress1(),ableToUpdate,"","35") %>
													</td>
												</tr>
											</table>
											<br>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="25%">
														<div align="right">Line 2:</div>
													</td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<%= StringManipulation.createTextBox("SHIP_ADDRESS_LINE2",shippingAddress.getAddress2(),ableToUpdate,"","35") %>
													</td>
												</tr>
											</table>
											<br>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="25%">
														<div align="right">Line 3:</div>
													</td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<%= StringManipulation.createTextBox("SHIP_ADDRESS_LINE3",shippingAddress.getAddress3(),ableToUpdate,"","35") %>
													</td>
												</tr>
											</table>
											<br>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="25%">
														<div align="right">Line 4:</div>
													</td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<%= StringManipulation.createTextBox("SHIP_ADDRESS_LINE4",shippingAddress.getAddress4(),ableToUpdate,"","35") %>
													</td>
												</tr>
											</table>
											<br>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="25%">
														<div align="right">City:</div>
													</td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<div id="SHIP_CITY_DIV">
														<% if(!ableToUpdate) out.println(shippingAddress.getCity()); %>
														<input type="hidden" name="SHIP_CITY" value="<%= shippingAddress.getCity() %>">
														</div>
													</td>
												</tr>
											</table>
											<br>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="25%">
														<div align="right">State:</div>
													</td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<div id="SHIP_STATE_DIV">
														<% if(!ableToUpdate) out.println(shippingAddress.getState()); %>
														<input type="hidden" name="SHIP_STATE" value="<%= shippingAddress.getState() %>">
														</div>
													</td>
												</tr>
											</table>
											<br>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="25%">
														<div align="right">Zip Code:</div>
													</td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<div id="SHIP_ZIP_DIV">
														<% if(!ableToUpdate) out.println(shippingAddress.getZip()); %>
														<input type="hidden" name="SHIP_ZIP" value="<%= shippingAddress.getZip() %>">
														</div>
													</td>
												</tr>
												<% if(ableToUpdate){ %>
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												<tr>
													<td width="25%"><div align="right"><i>City Locator</i></div></td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<div id="SHIP_BUTTON_DIV"></div>
													</td>
												</tr>
												<% } %>
											</table>
											<br>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="25%"><div align="right">Country</div></td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<%
														myCountry = shippingAddress.getCountry();
														if(myCountry.equals("")){
														myCountry="777";
														}
														if(ableToUpdate){
														%>
														<select onchange="javascript: changeShipCountry()" name="SHIP_COUNTRY">
															<option value="">Select One...</option>
															<%
															Set countrySet = countries.entrySet();
															   Iterator it2 = countrySet.iterator();
															   while (it2.hasNext()){
															     java.util.Map.Entry country = (java.util.Map.Entry) it2.next();
															    	if(country.getValue().equals(myCountry)){
																%>
															<option value="<%= country.getValue() %>" selected><%= country.getKey() %></option>
															<%
															}else{
															%>
															<option value="<%= country.getValue() %>"><%= country.getKey() %></option>
															<%
															}
															
															  }
															%>
														</select>
														<% }else{
														
																	Set countrySet = countries.entrySet();
															   	Iterator it2 = countrySet.iterator();
															   	while (it2.hasNext()){
															     	java.util.Map.Entry country = (java.util.Map.Entry) it2.next();
															    	if(country.getValue().equals(myCountry)){
																		out.println(country.getKey());
																		break;
																		}
																	}
																%>
																<input type="hidden" name="SHIP_COUNTRY" value="<%= myCountry %>">
														<% } %>
													</td>
												</tr>
											</table>
											<br> <hr align="center" width="600" size="1" color="#999999" noshade>
											<a name="billto"></a><p class="heading2">Bill-To  Address</p>
											<% if(ableToUpdate){ %>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="4%"><div align="center"></div></td>
													<td width="92%" bgcolor="#EEEEEE">
														<!--<input type="checkbox" name="BILLTO_CHECK" onclick="javascript: clickBillTo()" value="Y">Check if same as Business Address-->
														<a href="javascript: clickBillTo()">Click here to copy business address to billing address.</a>
													</td>
													<td width="4%">&nbsp;</td>
												</tr>
											</table>
											<br>
											<% } %>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="25%">
														<div align="right">Line 1:</div>
													</td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<%= StringManipulation.createTextBox("BILLTO_ADDRESS_LINE1",billToAddress.getAddress1(),ableToUpdate,"","35") %>
													</td>
												</tr>
											</table>
											<br>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="25%">
														<div align="right">Line 2:</div>
													</td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<%= StringManipulation.createTextBox("BILLTO_ADDRESS_LINE2",billToAddress.getAddress2(),ableToUpdate,"","35") %>
													</td>
												</tr>
											</table>
											<br>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="25%">
														<div align="right">Line 3:</div>
													</td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<%= StringManipulation.createTextBox("BILLTO_ADDRESS_LINE3",billToAddress.getAddress3(),ableToUpdate,"","35") %>
													</td>
												</tr>
											</table>
											<br>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="25%">
														<div align="right">Line 4:</div>
													</td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<%= StringManipulation.createTextBox("BILLTO_ADDRESS_LINE4",billToAddress.getAddress4(),ableToUpdate,"","35") %>
													</td>
												</tr>
											</table>
											<br>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="25%">
														<div align="right">City:</div>
													</td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<div id="BILLTO_CITY_DIV">
														<% if(!ableToUpdate) out.println(billToAddress.getCity()); %>
														<input type="hidden" name="BILLTO_CITY" value="<%= billToAddress.getCity() %>">
														</div>
													</td>
												</tr>
											</table>
											<br>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="25%">
														<div align="right">State:</div>
													</td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<div id="BILLTO_STATE_DIV">
														<% if(!ableToUpdate) out.println(billToAddress.getState()); %>
														<input type="hidden" name="BILLTO_STATE" value="<%= billToAddress.getState() %>">
														</div>
													</td>
												</tr>
											</table>
											<br>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="25%">
														<div align="right">Zip Code:</div>
													</td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<div id="BILLTO_ZIP_DIV">
														<% if(!ableToUpdate) out.println(billToAddress.getZip()); %>
														<input type="hidden" name="BILLTO_ZIP" value="<%= billToAddress.getZip() %>">
														</div>
													</td>
												</tr>
												<% if(ableToUpdate){ %>
												<tr>
													<td colspan="3">&nbsp;</td>
												</tr>
												<tr>
													<td width="25%"><div align="right"><i>City Locator</i></div></td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<div id="BILLTO_BUTTON_DIV"></div>
													</td>
												</tr>
												<% } %>
											</table>
											<br>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="25%"><div align="right">Country</div></td>
													<td width="4%">&nbsp;</td>
													<td width="71%">
														<%
														myCountry = billToAddress.getCountry();
														if(myCountry.equals("")){
														myCountry="777";
														}
														if(ableToUpdate){
														%>
														<select onchange="javascript: changeBillToCountry()" name="BILLTO_COUNTRY" id="country">
															<option value="">Select One...</option>
															<%
															Set countrySet = countries.entrySet();
															   Iterator it2 = countrySet.iterator();
															   while (it2.hasNext()){
															     java.util.Map.Entry country = (java.util.Map.Entry) it2.next();
															    	if(country.getValue().equals(myCountry)){
																%>
															<option value="<%= country.getValue() %>" selected><%= country.getKey() %></option>
															<%
															}else{
															%>
															<option value="<%= country.getValue() %>"><%= country.getKey() %></option>
															<%
															}
															
															  }
															%>
														</select>
														<% }else{
														
																	Set countrySet = countries.entrySet();
															   	Iterator it2 = countrySet.iterator();
															   	while (it2.hasNext()){
															     	java.util.Map.Entry country = (java.util.Map.Entry) it2.next();
															    	if(country.getValue().equals(myCountry)){
																		out.println(country.getKey());
																		break;
																		}
																	}
																%>
														<input type="hidden" name="BILLTO_COUNTRY" value="<%= myCountry %>">
														<% } %>
													</td>
												</tr>
											</table>
										</td></tr></table>
								<br><br>
							</td>
						</tr>
					</table>
					<%
					if(!acct.isNewAccount()){
					%>
					<br> <hr align="center" width="600" size="1" color="#999999" noshade>
					<p class="heading2">Contacts</p>
					<table width="100%" border="0" cellspacing="1" cellpadding="0">
						<tr>
							<th width="5%"><div align="center"></div></th>
							<th width="9%"><div align="center">Last Name</div></th>
							<th width="12%"><div align="center">First Name</div></th>
							<th width="13%"><div align="center">Title</div></th>
							<th width="14%"><div align="center">Job Functions</div></th>
							<th width="16%"><div align="center">Phone Number</div></th>
							<% if(acct.isDistributor()){ %>
							<th width="8%"><div align="center">Statement*</div></th>
							<% } %>
							<th width="22%"><div align="center">Email</div></th>
						</tr>
						<%
						ArrayList contacts = new ArrayList();
						contacts = acct.getContacts();
						if(contacts.size()==0){
						%>
						<tr>
							<td nowrap>
							</td>
							<td colspan="6"><div align="left">No contacts for this account</div></td>
						</tr>
						<%
						}
						String bgcolor="";
						for(int i=0;i<contacts.size();i++){
							Contact contact = (Contact)contacts.get(i);
							if(i%2==0){
								bgcolor="";
							}else{
							  bgcolor=" class=\"cellShade\"";
							}
							String checkedRadio="";
							if(contact.isDistributorStatement()){
								checkedRadio=" checked";
							}
						%>
						<tr<%= bgcolor %>>
							<td nowrap>
								<% if(ableToUpdateNonVista){ %>
								<div align="left">
									<a href="javascript:newWindow('Contacts?page=edit&contactid=<%= contact.getId() %>','contacts')"><img src="<%= sImagesURL %>editIcon.gif" border="0" align="absmiddle" alt="Edit"></a>&nbsp;<a href="javascript:newWindow('Contacts?page=delete&contactid=<%= contact.getId() %>','contacts')"><img src="<%= sImagesURL %>deleteIcon.gif" border="0" align="absmiddle" alt="Delete"></a>&nbsp;
								</div>
								<% } %>
							</td>
							<td><div align="left"><%= contact.getLastName() %></div></td>
							<td><div align="left"><%= contact.getFirstName() %></div></td>
							<td><div align="left"><%= contact.getTitle() %></div></td>
							<td><div align="left"><%= contact.getFunctionalPosition() %></div></td>
							<td><div align="left"><%= contact.getPhone() %></div></td>
							<% if(acct.isDistributor()){ %>
							<td><div align="center"><input type="radio" name="DIST_STATEMENT" value="<%= contact.getId() %>"<%= checkedRadio %>></div></td>
							<% } %>
							<td><div align="left"><a href="mailto:<%= contact.getEmailAddress() %>"><%= contact.getEmailAddress() %></a></div></td>
						</tr>
						<%
							}
						if(acct.isDistributor()){
						 %>
						<tr>
							<td colspan="8"><br>* Indicates contact will receive Distributor Statement</td>
						</tr>
						<% } %>
					</table>
					<% if(ableToUpdateNonVista){ %>
					<br>
					<table width="100%" border="0" cellspacing="5" cellpadding="0">
						<tr>
							<td><a href="javascript:newWindow('Contacts?page=add&acctId=<%= acct.getVcn() %>','contacts')"><img src="<%= sImagesURL %>button_add_contact.gif" width="70" height="20" border="0"></a></td>
						</tr>
					</table>
					<% } %>
					<% } %>
					<br><br>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="20%"><div align="right">Background<br>
									Information:                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    </div></td>
							<td width="2%">&nbsp;</td>
							<td width="78%">
								<% if(ableToUpdateNonVista){ %>
								<textarea name="BACKGROUND_INFORMATION" cols="40" rows="4"><%= acct.getBackgroundInfo() %></textarea>
								<% }else{ %>
								<%= acct.getBackgroundInfo() %><input type="hidden" name="BACKGROUND_INFORMATION" value="<%= acct.getBackgroundInfo() %>">
								<% } %>
							</td>
						</tr>
					</table>
					<br><br>
					<hr align="center" width="600" size="1" color="#999999" noshade>
					<p class="heading2">Cross Reference</p>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td valign="top">
								<table width="100%" border="0" cellspacing="0" cellpadding="0"> <!-- START outter Most table for fields -->
									<tr>
										<td>
											<table width="100%" border="0" cellspacing="0" cellpadding="0"> <!-- START table contained in left half of outter table -->
												<tr>
													<td width="22%"><div align="right">DPC-Num:</div></td>
													<td width="4%">&nbsp;</td>
													<td colspan="2" width="78%">
														<%= StringManipulation.createTextBox("DPC_NUM",acct.getDpcNum(),ableToUpdate,"","9") %>
													</td>
												</tr>
												<tr><td colspan="4">&nbsp;</td></tr>
												<tr>
													<td width="22%"><div align="right">Synergy Code:</div></td>
													<td width="4%">&nbsp;</td>
													<td colspan="2" width="78%">
													<% if(ableToUpdate){ %>
													<select name="SYNERGY_CODE">
														<option value=""></option>
														<%
														if(acct.getSynergyCode().equals("ISO")){
														%>
														<option value="ISO" selected>ISO</option>
														<%
														}else{
														%>
														<option value="ISO">ISO</option>														
														<%
														}
														%>
														<%
														if(acct.getSynergyCode().equals("MGE")){
														%>
														<option value="MGE" selected>MGE</option>
														<%
														}else{
														%>
														<option value="MGE">MGE</option>														
														<%
														}
														%>
														<%
														if(acct.getSynergyCode().equals("PWR")){
														%>
														<option value="PWR" selected>PWR</option>
														<%
														}else{
														%>
														<option value="PWR">PWR</option>
														<%
														}
														%>
														<%
														if(acct.getSynergyCode().equals("MOE")){
														%>		
														<option value="MOE" selected>MOE</option>
														<%
														} else { 
														%>
														<option value="MOE">MOE</option>
														<% 
														} 
														%>	
														<!-- INC000004247360 - PP39843 Cooper Acquisition Synergy Sales tracking -->
														<%
														if(acct.getSynergyCode().equals("COO")){
														%>
														<option value="COO" selected>COO</option>
														<%
														}else{
														%>
														<option value="COO">COO</option>														
														<%
														}
														%>
														<!-- INC000004247360 - PP39843 Cooper Acquisition Synergy Sales tracking -->											
														</select>
														<% }else{ 
														%>
														<%= acct.getSynergyCode() %><input type="hidden" name="SYNERGY_CODE" value="<%= acct.getSynergyCode() %>">
														<%
														}
														%>
													</td>
												</tr>
												<tr><td colspan="4">&nbsp;</td></tr>
												<tr>
													<td width="22%"><div align="right">Store Number:</div></td>
													<td width="4%">&nbsp;</td>
													<td colspan="2" width="78%">
														<%= StringManipulation.createTextBox("STORE_NUM",acct.getStoreNumber(),ableToUpdate,"4","4") %>
													</td>
												</tr>
												<tr><td colspan="4">&nbsp;</td></tr>
												<tr>
													<td width="22%"><div align="right">Genesis Number:</div></td>
													<td width="4%">&nbsp;</td>
													<td colspan="2" width="78%">
														<%= StringManipulation.createTextBox("GENESIS_NUMBER",acct.getGenesisNumber(),ableToUpdate,"7","7") %>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					<br><br>
					<% if(ableToUpdate || ableToUpdateNonVista || isModify.equalsIgnoreCase("Y")){ %>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tr>
							<td width="20%"><div align="left"><input type="image" src="<%= sImagesURL %>button_save.gif" width="70" height="20"></div></td>
							<td width="80%"></td>
						</tr>
					</table>
					<% } %>
				
			<input type="hidden" name="page" value="saveProfile">
			</form>	
			<center><font size="1" color="#707070 ">
			<% if(acct.getDateAdded()!=null) { %>
			Added: <%= acct.getDateAdded() %><br>
			<% } 
			if(acct.getDateChanged()!=null) {
			%>
			Changed: <%= acct.getDateChanged() %>
			<% } %>
			</font></center>
			</td>
			</tr>
		</table>

		</td>
	</tr>
</table>

</td>
</tr>
</table>

<p>&nbsp;</p>
<% if(ableToUpdate){ %>
<script language="javascript">
	changeCountry();
	changeBillToCountry();
	changeShipCountry();
</script>
<% } %>
</body>
</html>
