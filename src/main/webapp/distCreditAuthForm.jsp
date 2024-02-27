<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.*"%>
<html>

<%@ include file="./SMRCHeader.jsp" %>
<%@ include file="analytics.jsp" %>

<script language="javascript" src="<%= jsURL %>validation/distCreditAuthForm.js"></script>
<%

DistributorCreditAuthorization creditAuth = (DistributorCreditAuthorization)request.getAttribute("creditAuth");

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

		<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Distributor Credit Application</span></p> 
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
            <% if(acct.isDistributor()){ %>
            Distributor Credit Application
            <% }else{ %>
            Credit Application
            <% } %>
            </p>
           <p class="heading3">Account: <%= acct.getCustomerName() %></p>
			<table width="100%" border="0" cellspacing="1" cellpadding="0">
            	<tr>
            		<td colspan="6" ><span class="tableTitle"><font class="crumbcurrent">*</font> Financial Information - Current Financial Statement
            				(If Available)<br>
            				<i>Distributors Bank Affiliation:</i></span></td>
       		 </tr>
            	<tr>
            		<td>
            		<% if(ableToUpdate){ %>
            		<textarea name="FINANCIAL_INFORMATION" cols="50" rows="4"><%= creditAuth.getFinancialInformation() %></textarea>
            		<% }else{ %>
            		<blockquote><%= creditAuth.getFinancialInformation() %></blockquote>
            		<% } %>
       			 </td>
            	</tr>
       	  </table>
       	  <br>
			<table width="100%" border="0" cellspacing="1" cellpadding="0">
            	<tr>
            		<td colspan="6" ><span class="tableTitle">Trade References</span></td>
       		 </tr>
            	<tr>
            		<th width="1"></th>
            		<th>Name</th>
            		<th >Address</th>
            		<th></th>
            		<th width="1">Phone Number</th>
            		<th>Fax Number</th>
       		 </tr>
            	<tr class="cellShade">
            		<td><font class="crumbcurrent">*</font></td>
            		<td>
            			<div align="center">
            				<%= StringManipulation.createTextBox("TRADE_REFERENCE1",creditAuth.getTradeReference1(),ableToUpdate,"") %>
            			</div>

           		 </td>
            		<td>
            			<div align="center">
            				<%= StringManipulation.createTextBox("TRADE_REFERENCE1_ADDR",creditAuth.getTradeReference1Addr(),ableToUpdate,"") %>
            			</div>
            		</td>
            		<td><font class="crumbcurrent">*</font></td>
            		<td>
            			<div align="center">
            				<%= StringManipulation.createTextBox("TRADE_REFERENCE1_PHONE",creditAuth.getTradeReference1Phone(),ableToUpdate,"") %>
            			</div>
            		</td>
            		<td>
            			<div align="center">
            				<%= StringManipulation.createTextBox("TRADE_REFERENCE1_FAX",creditAuth.getTradeReference1Fax(),ableToUpdate,"") %>
            			</div>
            		</td>
       		 </tr>
            	<tr class="cellShade">
            	<td></td>
            		<td>
            			<div align="center">
            				<%= StringManipulation.createTextBox("TRADE_REFERENCE2",creditAuth.getTradeReference2(),ableToUpdate,"") %>
            			</div>

           		 </td>
            		<td>
            			<div align="center">
            				<%= StringManipulation.createTextBox("TRADE_REFERENCE2_ADDR",creditAuth.getTradeReference2Addr(),ableToUpdate,"") %>
            			</div>
            	<td></td>            			
            		</td>
            		<td>
            			<div align="center">
            				<%= StringManipulation.createTextBox("TRADE_REFERENCE2_PHONE",creditAuth.getTradeReference2Phone(),ableToUpdate,"") %>

            			</div>
            		</td>
            		<td>
            			<div align="center">
            				<%= StringManipulation.createTextBox("TRADE_REFERENCE2_FAX",creditAuth.getTradeReference2Fax(),ableToUpdate,"") %>
            			</div>
            		</td>
       		 </tr>
            	<tr class="cellShade">
            	<td></td>            	
            		<td>
            			<div align="center">
            				<%= StringManipulation.createTextBox("TRADE_REFERENCE3",creditAuth.getTradeReference3(),ableToUpdate,"") %>
            			</div>

           		 </td>
            		<td>
            			<div align="center">
            				<%= StringManipulation.createTextBox("TRADE_REFERENCE3_ADDR",creditAuth.getTradeReference3Addr(),ableToUpdate,"") %>
            			</div>
            		</td>
            	<td></td>            		
            		<td>
            			<div align="center">
            				<%= StringManipulation.createTextBox("TRADE_REFERENCE3_PHONE",creditAuth.getTradeReference3Phone(),ableToUpdate,"") %>

            			</div>
            		</td>
            		<td>
            			<div align="center">
            				<%= StringManipulation.createTextBox("TRADE_REFERENCE3_FAX",creditAuth.getTradeReference3Fax(),ableToUpdate,"") %>
            			</div>
            		</td>
       		 </tr>
            	</table>
			<br>

<table width="100%" border="0" cellspacing="1" cellpadding="0">
            	<tr>
            		<td colspan="6" ><span class="tableTitle"><i>Additional Sales Information (If Applicable):</i></span></td>
       		 </tr>
            	<tr>
            		<td>
            		<% if(ableToUpdate){ %>
            		<textarea name="ADDITIONAL_SALES_INFORMATION" cols="50" rows="4"><%= creditAuth.getAdditionalSalesInfo() %></textarea>
            		<% }else{ %>
            		<blockquote><%= creditAuth.getAdditionalSalesInfo() %></blockquote>
            		<% } %>
       			 </td>
            	</tr>
       	  </table>

<br>
			<table width="100%" border="0" cellspacing="1" cellpadding="0">
           <tr>
            	<td>
							<br><strong>Sales Tax Exemption Information</strong><br>
							A current copy of the state sales tax exemption certificate must accompany this form and must be made out to Eaton Corporation<br><br>
							Distributor applications will not be processed without a State Sales Tax Exemption Form.<br><br>
							Please forward these forms to <a href="mailto:taxteameg@eaton.com" >Tax Team EG</a> (Fax: 412-893-2120).<br>
       			 </td>

           	 </tr>           	 
       	  </table>
			<br>
			<% if(ableToUpdate){ %>
			<table width="100%" border="0" cellspacing="10" cellpadding="0">
				<tr>
					<td width="6%">
						<div align="right"><input type="image" src="<%= sImagesURL %>button_save.gif" width="70" height="20"></div>
					</td>
					<td width="94%"></td>
				</tr>
			</table>
			<% } %>
			<br>

	      <p>&nbsp;</p>
		   <p><br>
   	      </p>
         </td>
        </tr>
      </table>
    
    </td>
  </tr>
</table>
<input type="hidden" name="acctId" value="<%= creditAuth.getVcn() %>">
<input type="hidden" name="page" value="saveCredit">
</form>
<p>&nbsp;</p>

  </body>
</html>

