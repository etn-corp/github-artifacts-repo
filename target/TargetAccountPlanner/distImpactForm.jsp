<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.*"%>
<html>
<%@ include file="./SMRCHeader.jsp" %>
<%@ include file="analytics.jsp" %>

<script language="javascript" src="<%= jsURL %>validation/distImpactForm.js"></script>
<%

DistributorImpactAnalysis impactAnalysis  = (DistributorImpactAnalysis)request.getAttribute("impactAnalysis");

Account acct = header.getAccount();
User usr = header.getUser();
boolean ableToUpdate = usr.ableToUpdate(acct.getDistrict()) || usr.hasSegmentOverride(acct) || (usr.equals(acct.getUserIdAdded()) && acct.isProspect());
if(!usr.hasOverrideSecurity()){
	if(!acct.isProspect()){
		ableToUpdate=false;
	}
}
%>

<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>

		<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Distributor Impact Analysis</span></p> 
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
					<% } %>		         
            <p class="heading2">
            Distributor Impact Analysis</p>
           <p class="heading3">Account: <%= acct.getCustomerName() %></p>
            <br>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="1%" >&nbsp;</td>
				    <td width="97%" ><p class="heading3">List Existing Distributors</p></td>
				</tr>

		  </table>
			<br><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
            	<tr>
            		<td width="25%">
            			<div align="right"><font class="crumbcurrent">*</font> A. Maintain:</div>
            		</td>
            		<td width="3%">&nbsp;</td>

       		        <td width="72%" >&nbsp;&nbsp;&nbsp;&nbsp;+ $
					    <%= StringManipulation.createTextBox("MAINTAIN_DOLLARS",Money.formatDoubleAsDollarsAndCents(impactAnalysis.getMaintainDollars()),ableToUpdate,"") %>
       		        </td>
           	 </tr>
       	  </table>
		  <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
            	<tr>
            		<td width="25%">

            			<div align="right"><font class="crumbcurrent">*</font> B. Grow:</div>
            		</td>
            		<td width="3%">&nbsp;</td>
       		        <td width="72%">
						&nbsp;&nbsp;&nbsp;&nbsp;+ $
						<%= StringManipulation.createTextBox("GROW_DOLLARS",Money.formatDoubleAsDollarsAndCents(impactAnalysis.getGrowDollars()),ableToUpdate,"") %>
       		        </td>
           	 </tr>
       	  </table>

		  <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
            	<tr>
            		<td width="25%">
            			<div align="right"><font class="crumbcurrent">*</font> C. Penetrate:</div>
            		</td>
            		<td width="3%">&nbsp;</td>
       		        <td width="72%">&nbsp;&nbsp;&nbsp;&nbsp;+ $
       		        <%= StringManipulation.createTextBox("PENETRATE_DOLLARS",Money.formatDoubleAsDollarsAndCents(impactAnalysis.getPenetrateDollars()),ableToUpdate,"") %>
       		        </td>
           	 </tr>
       	  </table>
			<br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
            	<tr>
            		<td width="25%">
            			<div align="right"><font class="crumbcurrent">*</font> Distributor(s) to add:</div>

            		</td>
            		<td width="3%">&nbsp;</td>
       		        <td width="72%">
						&nbsp;&nbsp;&nbsp;&nbsp;+ $
						<%= StringManipulation.createTextBox("ADD_DOLLARS",Money.formatDoubleAsDollarsAndCents(impactAnalysis.getAddDollars()),ableToUpdate,"") %>
       		        </td>
           	 </tr>
       	  </table>
            <br>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="25%">
						<div align="right"><font class="crumbcurrent">*</font> Distributor(s) to terminate:</div>
					</td>
					<td width="3%">&nbsp;</td>
				    <td width="72%">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- $
						<%= StringManipulation.createTextBox("TERMINATE_DOLLARS",Money.formatDoubleAsDollarsAndCents(impactAnalysis.getTerminateDollars()),ableToUpdate,"") %>
				    </td>
				</tr>
			</table>
			<br>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="25%">
						<div align="right"><font class="crumbcurrent">*</font> Distributor(s) at risk:</div>

					</td>
					<td width="3%">&nbsp;</td>
				    <td width="72%">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- $
						<%= StringManipulation.createTextBox("RISK_DOLLARS",Money.formatDoubleAsDollarsAndCents(impactAnalysis.getRiskDollars()),ableToUpdate,"") %>
				    </td>
				</tr>
			</table>
			<br>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="25%">
						<div align="right">Area Impact = Sum Gain (Loss):</div>
					</td>
					<td width="3%">&nbsp;</td>
					<td width="72%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; $
						<%= Money.formatDoubleAsDollarsAndCents(impactAnalysis.getAreaImpact()) %>

			        </td>
				</tr>
			</table>
			<br>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="25%">
						<div align="right"><font class="crumbcurrent">*</font> Impact of chain(s) in other geographies:</div>

					</td>
					<td width="3%">&nbsp;</td>
					<td width="72%">
						&nbsp;+/- $
						<%= StringManipulation.createTextBox("OTHER_CHAIN_IMPACT",Money.formatDoubleAsDollarsAndCents(impactAnalysis.getOtherChainImpact()),ableToUpdate,"") %>
					</td>
				</tr>
			</table>
			<br>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="25%">
						<div align="right"><strong>Total Impact</strong>:</div>
					</td>
					<td width="3%">&nbsp;</td>
					<td width="72%">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>$ <%= Money.formatDoubleAsDollarsAndCents(impactAnalysis.getGeographyImpact()) %></strong>
					</td>
				</tr>
			</table>
									
			<br><br>
			<% if(ableToUpdate){ %>
			<table width="100%" border="0" cellspacing="10" cellpadding="0">
				<tr>
					<td width="33%">
						<div align="right"><input type="image" src="<%= sImagesURL %>button_save.gif" width="70" height="20"></div>
					</td>

					<td width="67%"></td>
				</tr>
			</table>
			<% } %>
			<br>
	      <p>&nbsp;</p>

         </td>
        </tr>
      </table>

    
    </td>
  </tr>
</table>
<input type="hidden" name="acctId" value="<%= impactAnalysis.getVcn() %>">
<input type="hidden" name="page" value="saveImpact">
</form>
<p>&nbsp;</p>
  </body>
</html>

