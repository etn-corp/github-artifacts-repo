<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*" %>
<%@ page import="com.eaton.electrical.smrc.util.StringManipulation" %>
<%@ page import="com.eaton.electrical.smrc.util.Globals" %>


<html>
<%@ include file="./SMRCHeader.jsp" %>
<%@ include file="analytics.jsp" %>

<script language="javascript" src="<%= jsURL %>validation/distSignupForm5.js"></script>
<%

Distributor dist = (Distributor)request.getAttribute("dist");
Account acct = header.getAccount();
User usr = header.getUser();
String acctName = (String)request.getAttribute("acctName");
ArrayList vendors = (ArrayList)request.getAttribute("vendors");
ArrayList supplierProducts = (ArrayList)request.getAttribute("supplierProducts");
ArrayList selectedSupplierProducts = (ArrayList)request.getAttribute("selectedSupplierProducts");

String disabled="";

boolean ableToUpdate = usr.ableToUpdate(acct.getDistrict()) || usr.hasSegmentOverride(acct) || (usr.equals(acct.getUserIdAdded()) && acct.isProspect());
if(!usr.hasOverrideSecurity()){
	if(!acct.isProspect()){
		ableToUpdate=false;
	}
}

if(!ableToUpdate){
disabled=" disabled";
}


%>

<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>

		<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Distributor Application</span></p> 		
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
          <table width="100%" border="0" cellspacing="0" cellpadding="2">
						<tr bgcolor="#EEEEEE">
							<td width="56%"><div align="right"></div></td>
						    <td width="40%" bgcolor="#F1F1F1">
							    <table width="100%" border="0" cellspacing="0" cellpadding="2">
								    <tr>
									    <td width="40%" align="center"><span class="navTitle">Step</span></td>
									   	<td width="12%" align="center"><span class="navArrow"></span><a href="DistributorSignup?page=app1&acctId=<%= request.getParameter("acctId") %>" class="navLink">1</a></td>
									    <td width="12%" align="center"><span class="navArrow"></span><a href="DistributorSignup?page=app2&acctId=<%= request.getParameter("acctId") %>" class="navLink">2</a></td>
									    <td width="12%" align="center"><span class="navArrow"></span><a href="DistributorSignup?page=app3&acctId=<%= request.getParameter("acctId") %>" class="navLink">3</a></td>
									    <td width="12%" align="center"><span class="navArrow"></span><a href="DistributorSignup?page=app4&acctId=<%= request.getParameter("acctId") %>" class="navLink">4</a></td>
									    <td width="12%" align="center"><span class="navCurrent">[ 5 ]</span></td>
										</tr>
									</table>								
						    </td>
						    <td width="4%">&nbsp;</td>
						</tr>
					</table>
					<% if(request.getParameter("save")!=null){ %>
					<blockquote><font class="crumbcurrent">Save Successful</font></blockquote>
					<% } %>					
            <p class="heading2">Distributor Application</p>
           <p class="heading3">Account: <%= acct.getCustomerName() %></p>

            <table width="600" border="0" cellspacing="0" cellpadding="0">
              <tr> 
				<td valign="top"> 

			<br>
			<hr align="center" width="600" size="1" color="#999999" noshade>
			<p class="heading3">Top 5 Suppliers (All Products)</p>

			<table width="80%" border="0" cellspacing="1" cellpadding="0">

				<tr>
					<th width="48%"><div align="left">Supplier Name</div></th>
					<th width="2%">&nbsp;</th>
					<th width="48%"><div align="left">Product</div></th>
				</tr>
				
				<%
				for(int j=0;j<5;j++){
					String selectedProduct="0";
					String selectedVendor="0";
					if(j<selectedSupplierProducts.size()){
						DistributorSupplierProduct selectedSupplierProduct = (DistributorSupplierProduct)selectedSupplierProducts.get(j);
						selectedProduct=selectedSupplierProduct.getProductId();
						selectedVendor=selectedSupplierProduct.getVendorId();
					}
				%>
				<tr class="cellShade">
					<td>
					<% if(ableToUpdate){ %>
						<select name="VENDOR_<%= j %>">
							<option value="0">Select One..</option>
					<% } %>
							<%
							for(int i=0;i<vendors.size();i++){
								Vendor vendor = (Vendor)vendors.get(i);
								if(ableToUpdate){
									if(Globals.a2int(selectedVendor)==vendor.getId()){
										%>
										<option value="<%= vendor.getId() %>" selected><%= vendor.getDescription() %></option>
										<%				
									}else{
										%>
										<option value="<%= vendor.getId() %>"><%= vendor.getDescription() %></option>
										<%							
									}
								}else{
									if(Globals.a2int(selectedVendor)==vendor.getId()){
										%>
										<%= vendor.getDescription()%><br>
										<%
										break;
									}
								}

							}
						if(ableToUpdate){
						%>
						</select>
						<% } %>
					</td>
					<td>&nbsp;</td>
					<td>
					<% if(ableToUpdate){ %>
						<select name="PRODUCT_<%= j %>">
							<option value="0">Select One..</option>
							<%
						}	
							for(int i=0;i<supplierProducts.size();i++){
								Product supplierProduct = (Product)supplierProducts.get(i);
								if(ableToUpdate){
									if(selectedProduct.equals(supplierProduct.getId())){
										%>
										<option value="<%= supplierProduct.getId() %>" selected><%= supplierProduct.getDescription() %></option>
										<%
									}else{
										%>
										<option value="<%= supplierProduct.getId() %>"><%= supplierProduct.getDescription() %></option>
										<%
									}
								}else{
										if(selectedProduct.equals(supplierProduct.getId())){
										%>
										<%= supplierProduct.getDescription()%><br>
										<%
										}
								}

							}
						if(ableToUpdate){
						%>
						</select>
						<% } %>
					</td>
				</tr>
				<% } %>
				
			</table>
	      	<br><hr align="center" width="600" size="1" color="#999999" noshade>
			<p class="heading3">Sales and Inventory</p>
			<table width="100%" border="0" cellspacing="5" cellpadding="0">
				<tr>
					<td width="40%"><div align="right"><font class="crumbcurrent">*</font> Current Year Total Sales Estimate:</div></td>
					<td width="3%">&nbsp;</td>
					<td colspan="2">
					$<%= StringManipulation.createTextBox("CURRENT_YR_SALES_EST",""+dist.getCurrentYrSalesEstimate(),ableToUpdate,"10") %>
					</td>
				</tr>
				<tr>
					<td>
						<div align="right"><font class="crumbcurrent">*</font> Previous Year Total Sales:</div>
					</td>
					<td>&nbsp;</td>
					<td colspan="2">
					$<%= StringManipulation.createTextBox("PRIOR_YR_SALES_ACTUAL",""+dist.getPriorYrActualSales(),ableToUpdate,"10") %>
					</td>
				</tr>
				<tr>
					<td>
						<div align="right"><font class="crumbcurrent">*</font> Year Before Last Total Sales:</div>
					</td>
					<td>&nbsp;</td>

					<td colspan="2">
						$<%= StringManipulation.createTextBox("PRIOR_2YR_SALES_ACTUAL",""+dist.getPrior2YrsActualSales(),ableToUpdate,"10") %>
					</td>
				</tr>
		<!--		<tr>
					<td>
						<div align="right"><font class="crumbcurrent">*</font> Total sales of all products from this location over the last three years:</div>
					</td>

					<td>&nbsp;</td>
					<td colspan="2">
						$<%= StringManipulation.createTextBox("PRIOR_3YR_SALES_ACTUAL",""+dist.getPrior3YrsTotalSales(),ableToUpdate,"10") %>
					</td>
				</tr>	 
		-->
				<tr>
					<td>
						<div align="right"><font class="crumbcurrent">*</font> Approximate Inventory at this location:</div>
					</td>

					<td>&nbsp;</td>
					<td colspan="2">
						$<%= StringManipulation.createTextBox("APROX_INVENTORY",""+dist.getApproxInventory(),ableToUpdate,"10") %>
					</td>
				</tr>
				<tr>
					<td>
						<div align="right"><font class="crumbcurrent">*</font> Projected Eaton Sales:</div>

					</td>
					<td>&nbsp;</td>
					<td width="30%">
						$<%= StringManipulation.createTextBox("PROJECTED_EATON_SALES_1",""+dist.getProjectedEatonSalesYr1(),ableToUpdate,"10") %> 
						&nbsp;&nbsp;<span class="textgray"><font class="crumbcurrent">*</font> Year 1</span>
					</td>
				    <td width="38%">
				    $<%= StringManipulation.createTextBox("PROJECTED_EATON_SALES_2",""+dist.getProjectedEatonSalesYr2(),ableToUpdate,"10") %>  
				    	&nbsp;&nbsp;<span class="textgray"><font class="crumbcurrent">*</font> Year 3
                    </span>
             </td>
				</tr>
		  </table>
	      	<br><hr align="center" width="600" size="1" color="#999999" noshade>
			<p class="heading3">Additional Information</p>
			<table width="100%" border="0" cellspacing="5" cellpadding="0">
				<tr>
					<td width="30%"><div align="right"><font class="crumbcurrent">*</font> Participates in NAED?</div></td>
					<td width="4%">&nbsp;</td>

					<td width="66%" colspan="3">
						<%
						if(ableToUpdate){
						%>
						<select name="NAED">
							<option value="N">Select One...</option>
							<% if(dist.getNaedParticipation().equals("Y")){ %>
							<option value="Y" selected>Yes</option>
							<% }else{ %>
							<option value="Y">Yes</option>
							<% } %>
							<% if(dist.getNaedParticipation().equals("N")){ %>
							<option value="N" selected>No</option>
							<% }else{ %>
							<option value="N">No</option>
							<% } %>							
						</select>
						<%
						}else{
						%>
						<%= dist.getNaedParticipation() %>
						<%
						}						
						%>
					</td>
				</tr>
				<tr>
					<td>

						<div align="right">Buying Group Association:</div>
					</td>
					<td>&nbsp;</td>
					<td colspan="3">
       				<%
							
							ArrayList checkedBuyingGroups = dist.getBuyingGroupAssn();
							HashMap groupsHM = new HashMap();
							for(int i=0;i< checkedBuyingGroups.size();i++){
								groupsHM.put((String)checkedBuyingGroups.get(i),null);
							}
							
							ArrayList codes = (ArrayList)request.getAttribute("buyingGroupCodes");

							String checked = "";
							String checkedOther = "";
							for(int i=0;i<codes.size();i++){
								CodeType code = (CodeType)codes.get(i);

								if(groupsHM.containsKey(""+code.getId())){
									checked=" checked";
								}else{
									checked="";
								}
							%>       	
                  <input type="checkbox" name="BUYING_GROUP" value="<%= code.getId() %>" <%= checked + disabled %>><%= code.getName() %><br>
              <% 
              }
               %>						
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>

					<td colspan="3">
					<%= StringManipulation.createTextBox("BUYING_GROUP_OTHER",dist.getBuyingGroupOther(),ableToUpdate,"") %> 
						Other</td>
				</tr>
				<tr>
					<td>
						<div align="right"><p>List other trade associations:<br> (ie, AHTD, EASA, etc...)</div>  
					</td>

					<td>&nbsp;</td>
					<td colspan="3">
					<% if(ableToUpdate){ %>
							<textarea name="TRADE_ASSNS" cols="40" rows="4"><%= dist.getTradeAssociations() %></textarea>
					<% }else{ %>
							<%= dist.getTradeAssociations() %>
					<% } %>
					</td>
				</tr>
				<tr>
					<td>
						<div align="right">Other notes or comments:</div>

					</td>
					<td>&nbsp;</td>
					<td colspan="3">
					<% if(ableToUpdate){ %>
							<textarea name="DISTRIBUTOR_NOTES" cols="40" rows="4"><%= dist.getDistributorNotes() %></textarea>
					<% }else{ %>
							<%= dist.getDistributorNotes() %>
					<% } %>
					</td>
				</tr>
		  </table>
		  <br>
		  <% if(ableToUpdate){ %>
			<table width="100%" border="0" cellspacing="10" cellpadding="0">
				<tr>
					<td width="47%">
						<div align="left"><input type="image" src="<%= sImagesURL %>button_save.gif" width="70" height="20"></div>
					</td>
					<td width="53%"></td>
				</tr>
			</table>
			<% } %>
			<p class="heading2">&nbsp;</p>
         </td>

        </tr>
    </table>
    
    </td>
  </tr>
</table>
<input type="hidden" name="acctId" value="<%= dist.getVcn() %>">
<input type="hidden" name="page" value="saveApp5">
</form>
<p>&nbsp;</p>

  </body>
</html>
