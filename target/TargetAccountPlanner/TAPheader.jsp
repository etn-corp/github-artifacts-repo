<%@page contentType="text/html"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import ="java.util.ResourceBundle" %>
<head>
	<title>Target Account Planner</title>
<jsp:useBean id="header" class="com.eaton.electrical.smrc.bo.HeaderBean" scope="request"/>
<%
	String sImagesURL = (String)header.getImagesURL();
	String sGetPage = (String)header.getPage();
	String jsURL = (String)header.getJsURL();
	String cssURL = (String)header.getCssURL();
	
	ResourceBundle rb = ResourceBundle.getBundle("com.eaton.electrical.smrc.SMRC");
%>
	<link rel="stylesheet" type="text/css" href="<%= cssURL %>style_2_1.css">
	<link rel="stylesheet" type="text/css" href="<%= cssURL %>oem-styles.css">
	<script language="JavaScript" src="<%= jsURL %>prototypeScript.js"></script>
	<script language="JavaScript" src='<%= jsURL %>scripts.js'></script>
</head>

<body bgColor="#ffffff" leftMargin="0" topMargin="0" onload="MM_preloadImages('<%= sImagesURL %>r2_home_hot.gif','<%= sImagesURL %>r2_reports_hot.gif','<%= sImagesURL %>r2_suggestions_hot.gif','<%= sImagesURL %>r2_profile_hot.gif','<%= sImagesURL %>r2_email_hot.gif','<%= sImagesURL %>r2_help_hot.gif','<%= sImagesURL %>r2_log_out_hot.gif','<%= sImagesURL %>r3_tab1_hot.gif','<%= sImagesURL %>r3_tab2_hot.gif','<%= sImagesURL %>r3_tab3_hot.gif','<%= sImagesURL %>r3_tab4_hot.gif','<%= sImagesURL %>r3_tab5_hot.gif')" marginwidth="0" marginheight="0">

<table cellSpacing="0" cellPadding="0" width="100%" border="0">
	<tbody>	
		<tr>		
    		<td width="152"><img height="40" src="<%= sImagesURL %>r1_image.gif" width="152"></td>	
			<%		
				if (header.getError()) {
			%>
			<td width="314"><img height="40" src="<%= sImagesURL %>r1_logo_title_target.gif" width="405"></td>
			<%
			   } else {
			%>
			<td width="314"><img src="<%= sImagesURL %>header/r1_logo_title.gif" width="336" height="40"></td>
			<%
			   }
			%>
		    <td width="99%" bgColor="#cebc87">&nbsp</td>
	    	<td width="79"><img height=40 src="<%= sImagesURL %>r1_intranet_logo.gif" width="79"></td>
		</tr>
  	</tbody>
</table>
<table cellSpacing="0" cellPadding="0" width="100%" border="0">
	<tbody>
		<tr>
		    <td width="152"><img height="14" src="<%= sImagesURL %>r2_image.gif" width=152></td>		
		    <td width="295"><img height="14" src="<%= sImagesURL %>r2_spacer.gif" width=295></td>
		    <td width="99%" bgColor="#cebc87"><FONT size=1>&nbsp;</FONT></td>
<%
	if (!header.getPopup() && !header.getError()) {
%>
		    <td width="32"><a href="SMRCHome" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('home','','<%= sImagesURL %>header/r2_home_hot.gif',1)"><img src="<%= sImagesURL %>header/r2_home_off.gif" name="home" width="32" height="14" border="0"></a></td>
	    	<td width="32"><a href=<%=rb.getString("helpURL")%> target="_blank" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('help','','<%= sImagesURL %>header/r2_help_hot.gif',1)"><img src="<%= sImagesURL %>header/r2_help_off.gif" name="help" width="32" height="14" border="0"></a></td>
		    <td width="48"><a href="StandardReport?page=sr" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('reports','','<%= sImagesURL %>header/r2_reports_hot.gif',1)"><img src="<%= sImagesURL %>header/r2_reports_off.gif" name="reports" width="48" height="14" border="0"></a></td>
		    <td width="40"><a href="OEMAcctPlan?page=userprofile" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('profile','','<%= sImagesURL %>header/r2_profile_hot.gif',1)"><img src="<%= sImagesURL %>header/r2_profile_off.gif" name="profile" width="40" height="14" border="0"></a></td> 
		 <!--    <td width="69"><a href="javascript: openPopup('ConnectRequestIt','requestIt',700,800)" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('suggestions','','<%= sImagesURL %>header/r2_suggestions_hot.gif',1)"><img src="<%= sImagesURL %>header/r2_suggestions_off.gif" name="suggestions" width="69" height="14" border="0"></a></td> -->
		    <td width="41"><a href="mailto:oemaccountplanner@eaton.com" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('email','','<%= sImagesURL %>header/r2_email_hot.gif',1)"><img src="<%= sImagesURL %>header/r2_email_off.gif" name="email" width="41" height="14" border="0"></a></td>
		    <td width="44"><a href="javascript: close();" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('logout','','<%= sImagesURL %>header/r2_log_out_hot.gif',1)"><img src="<%= sImagesURL %>header/r2_log_out_off.gif" name="logout" width="44" height="14" border="0"></a></td>
<%
	}
%>
  		</tr>
  	</tbody>
</table>
<table cellSpacing="0" cellPadding="0" width="100%" border="0">
  	<tbody>
  		<tr>
		    <td width="152"><img height="26" src="<%= sImagesURL %>r3_image.gif" width="152"></td>
	<%
	if (!header.getError()) {
		String backParam = "";
      if (sGetPage.equals("salesplan") ||
			sGetPage.equals("productmix") || sGetPage.equals("sample") ||
			sGetPage.equals("sendSample") || sGetPage.equals("salesplanprint") ||
			sGetPage.equals("custproject") || header.getForceCustLinks()) {

			if (header.getGeog().length() > 1) {
				backParam = "&geog=" + header.getGeog();
			}else if(header.getIndustry().length() > 0){
				backParam = "&industry=" + header.getIndustry();
			}
			if (header.getBackSort().length() > 0) {
				backParam = backParam + "&backSort=" + header.getBackSort();
			}
			%>
			<td width="76"><A onmouseover="MM_swapImage('tab1','','<%= sImagesURL %>r3_tab1_hot.gif',1)" onmouseout=MM_swapImgRestore() href="AccountProfile?acctId=<%= header.getCustomer().getVistaCustNum() %>" onClick='return itemChanged();'><img height="26" src="<%= sImagesURL %>r3_tab1_off.gif" width="76" border="0" name="tab1"></A></td>
			<td width="76"><A onmouseover="MM_swapImage('tab2','','<%= sImagesURL %>r3_tab2_hot.gif',1)" onmouseout=MM_swapImgRestore() href="OEMAcctPlan?page=salesplan&cust=<%= header.getCustomer().getVistaCustNum() %><%= backParam %>" onClick='return itemChanged();'><img height="26" src="<%= sImagesURL %>r3_tab2_off.gif" width="76" border="0" name="tab2"></A></td>
			<% if (header.getUser().ableToSee(header.getAccount())  || (header.getUser().equals(header.getAccount().getUserIdAdded()) && header.getAccount().isProspect())) { %>
			<td width="76"><A onmouseover="MM_swapImage('tab3','','<%= sImagesURL %>r3_tab3_hot.gif',1)" onmouseout=MM_swapImgRestore() href="OEMAcctPlan?page=productmix&cust=<%= header.getCustomer().getVistaCustNum() %><%= backParam %>" onClick='return itemChanged();'><img height="26" src="<%= sImagesURL %>r3_tab3_off.gif" width="76" border="0" name="tab3"></A></td>
				
			<td width="76"><A onmouseover="MM_swapImage('tab5','','<%= sImagesURL %>r3_tab5_hot.gif',1)" onmouseout=MM_swapImgRestore() href="OEMAcctPlan?page=custproject&cust=<%= header.getCustomer().getVistaCustNum() %><%= backParam %>" onClick='return itemChanged();'><img height="26" src="<%= sImagesURL %>r3_tab5_off.gif" width="76" border="0" name="tab5"></A></td>
			<% } %>
			<% if(header.getAccount().isDistributor() && header.getUser().ableToSee(header.getAccount()) && header.getAccount().isActive() && !header.getAccount().isProspect()) { %>
	     	<td width="76"><A onmouseover="MM_swapImage('tab6','','<%= sImagesURL %>r3_tab6_hot.gif',1)" onmouseout="MM_swapImgRestore()" href="TargetMarketSetup?acctId=<%= header.getCustomer().getVistaCustNum() %>" onClick='return itemChanged();'><img height="26" src="<%= sImagesURL %>r3_tab6_off.gif" width="76" border="0" name="tab6"></A></td>	
	     	<% } %>
		    <td width="76"><A onmouseover="MM_swapImage('tab7','','<%= sImagesURL %>r3_tab7_hot.gif',1)" onmouseout=MM_swapImgRestore() href="CustomerVisits?acctId=<%= header.getCustomer().getVistaCustNum() %>" onClick='return itemChanged();'><img height="26" src="<%= sImagesURL %>r3_tab7_off.gif" width="76" border="0" name="tab7"></A></td>	    
		    <td width="76"><A onmouseover="MM_swapImage('tab8','','<%= sImagesURL %>r3_tab8_hot.gif',1)" onmouseout=MM_swapImgRestore() href="AccountToolbox?acctId=<%= header.getCustomer().getVistaCustNum() %>" onClick='return itemChanged();'><img height="26" src="<%= sImagesURL %>r3_tab8_off.gif" width="76" border="0" name="tab8"></A></td>	    
			<td width="1"><img height="26" src="<%= sImagesURL %>r3_tab_spacer.gif" width="1"></td>
	     	<td width="99%" bgColor="#71674a">&nbsp;</td>
<%	
		} else if ( sGetPage.equals("reporting") || sGetPage.equals("reportsample") || 
					sGetPage.equals("reportother") || sGetPage.equals("results") ||
					sGetPage.equals("sampleRpt") || sGetPage.equals("sidRpt") ||
					sGetPage.equals("distreport")|| sGetPage.equals("taskReport") ||
					sGetPage.equals("projects") || sGetPage.equals("projresults")) {
%>
			<td width="76"><a onmouseover="MM_swapImage('tab9','','<%= sImagesURL %>r3_tab9_hot.gif',9)" onmouseout="MM_swapImgRestore()" href="StandardReport?page=sr" onClick='return itemChanged();'><IMG height="26" src="<%= sImagesURL %>r3_tab9_off.gif" width="76" border="0" name="tab9"></a></td>
			<td width="76"><a onmouseover="MM_swapImage('tab10','','<%= sImagesURL %>r3_tab10_hot.gif',10)" onmouseout="MM_swapImgRestore()" href="AcctPlanProjRpt?page=projects" onClick='return itemChanged();'><IMG height="26" src="<%= sImagesURL %>r3_tab10_off.gif" width="76" border="0" name="tab10"></a></td>
			<td width="76"><a onmouseover="MM_swapImage('tab11','','<%= sImagesURL %>r3_tab11_hot.gif',11)" onmouseout="MM_swapImgRestore()" href="AcctPlanReport?page=reportsample" onClick='return itemChanged();'><IMG height="26" src="<%= sImagesURL %>r3_tab11_off.gif" width="76" border="0" name="tab11"></a></td>
			<td width="76"><a onmouseover="MM_swapImage('tab13','','<%= sImagesURL %>r3_tab13_hot.gif',11)" onmouseout="MM_swapImgRestore()" href="CustomerMarketShareReport?page=cmsReport" onClick='return itemChanged();'><IMG height="26" src="<%= sImagesURL %>r3_tab13_off.gif" width="76" border="0" name="tab13"></a></td>
			<td width="76"><a onmouseover="MM_swapImage('tab12','','<%= sImagesURL %>r3_tab12_hot.gif',12)"  onmouseout="MM_swapImgRestore()" href="AcctPlanReport?page=reportother" onClick='return itemChanged();'><IMG height="26" src="<%= sImagesURL %>r3_tab12_off.gif" width="76" border="0" name="tab12"></A></td>
			<td width="99%" bgColor="#71674a">&nbsp;</td>
<%
		} else { 
%>
      		<td width="76"><a href="CustomerListing?page=listing&SE_ID=true" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('tab1','','<%= sImagesURL %>header/r3_tab1_hot.gif',1)"><img name="tab1" border="0" src="<%= sImagesURL %>header/r3_tab1_off.gif" width="76" height="26"></a></td>
		    <td width="76"><a href="CustomerListing" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('tab2','','<%= sImagesURL %>header/r3_tab2_hot.gif',1)"><img name="tab2" border="0" src="<%= sImagesURL %>header/r3_tab2_off.gif" width="76" height="26"></a></td>
		    <td width="76"><a href="OEMAcctPlan?page=myProjects" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('tab3','','<%= sImagesURL %>header/r3_tab3_hot.gif',1)"><img name="tab3" border="0" src="<%= sImagesURL %>header/r3_tab3_off.gif" width="76" height="26"></a></td>
		    <td width="76"><a href="TargetProjectPopup?page=newtargetproject" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('tab4','','<%= sImagesURL %>header/r3_tab4_hot.gif',1)"><img name="tab4" border="0" src="<%= sImagesURL %>header/r3_tab4_off.gif" width="76" height="26"></a></td>
		    <td width="76"><a href="AcctPlanReport?page=taskReport" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('tab5','','<%= sImagesURL %>header/r3_tab5_hot.gif',1)"><img name="tab5" border="0" src="<%= sImagesURL %>header/r3_tab5_off.gif" width="76" height="26"></a></td>
		    <% if(header.getUser().isSalesEngineer() || header.getUser().hasOverrideSecurity()){ %>
		    <td width="76"><a href="CustomerListing?page=listing&SE_ID=true&targetOnly=true" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('tab6','','<%= sImagesURL %>header/r3_tab6_hot.gif',1)"><img name="tab6" border="0" src="<%= sImagesURL %>header/r3_tab6_off.gif" width="76" height="26"></a></td>
		    <% }
		    if(header.getUser().isOtherUser() || header.getUser().hasOverrideSecurity()){ %>
		    <td width="76"><a href="CustomerListing?page=listing&districttargets=true&divisiontargets=true" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('tab7','','<%= sImagesURL %>header/r3_tab7_hot.gif',1)"><img name="tab7" border="0" src="<%= sImagesURL %>header/r3_tab7_off.gif" width="76" height="26"></a></td>
		    <% }
		    if(header.getUser().isDistrictManager() || header.getUser().isZoneManager() || header.getUser().hasOverrideSecurity()){ %>
		    <td width="76"><a href="CustomerListing?page=listing&districttargets=true" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('tab8','','<%= sImagesURL %>header/r3_tab8_hot.gif',1)"><img name="tab8" border="0" src="<%= sImagesURL %>header/r3_tab8_off.gif" width="76" height="26"></a></td>
		    <% } %>
	        <td width="1"><IMG height="26" src="<%= sImagesURL %>r3_tab_spacer.gif" width="1"></td>
			<td width="99%" bgColor="#71674a">&nbsp;</td>
<%
		}
	} else {
%>
			<td width="1"><img height="26" src="<%= sImagesURL %>r3_tab_spacer.gif" width="1"></td>
			<td width="99%" bgColor="#71674a">&nbsp;</td>
<%
	}
%>
  		</tr>
	</tbody>
</table>
