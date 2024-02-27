<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*" %>
<%@ page import="com.eaton.electrical.smrc.util.StringManipulation" %>


<html>
<%@ include file="./SMRCHeader.jsp" %>
<%@ include file="analytics.jsp" %>
<script language="javascript" src="<%= jsURL %>validation/distSignupForm2.js"></script>
<%

Distributor dist = (Distributor)request.getAttribute("dist");

Account acct = header.getAccount();
User usr = header.getUser();

ArrayList locationCodes =(ArrayList)request.getAttribute("locationCodes");
ArrayList facilitiesCodes =(ArrayList)request.getAttribute("facilitiesCodes");
ArrayList servicesCodes =(ArrayList)request.getAttribute("servicesCodes");
ArrayList ediCustCodes =(ArrayList)request.getAttribute("ediCustCodes");
ArrayList ediSuppliersCodes =(ArrayList)request.getAttribute("ediSuppliersCodes");
ArrayList vmiCodes =(ArrayList)request.getAttribute("vmiCodes");
ArrayList webAccessCodes =(ArrayList)request.getAttribute("webAccessCodes");
			
boolean ableToUpdate = usr.ableToUpdate(acct.getDistrict()) || usr.hasSegmentOverride(acct) || (usr.equals(acct.getUserIdAdded()) && acct.isProspect());
if(!usr.hasOverrideSecurity()){
	if(!acct.isProspect()){
		ableToUpdate=false;
	}
}
String disabled="";
if(!ableToUpdate){
	disabled=" disabled";
}

int selectedCodeId = -1;
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
									    <td width="12%" align="center"><span class="navCurrent">[ 2 ]</span></td>
									    <td width="12%" align="center"><span class="navArrow"></span><a href="DistributorSignup?page=app3&acctId=<%= request.getParameter("acctId") %>" class="navLink">3</a></td>
									    <td width="12%" align="center"><span class="navArrow"></span><a href="DistributorSignup?page=app4&acctId=<%= request.getParameter("acctId") %>" class="navLink">4</a></td>
									    <td width="12%" align="center"><span class="navArrow"></span><a href="DistributorSignup?page=app5&acctId=<%= request.getParameter("acctId") %>" class="navLink">5</a></td>
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

           <p class="heading3">Location Details:</p>
           <table width="100%" border="0" cellspacing="5" cellpadding="0">
           	<tr>
           		<td width="21%"> <div align="right"><font class="crumbcurrent">*</font> Location Type:</div></td>
           		<td width="3%">&nbsp;</td>
   		        <td width="76%">
   		        	<%
   		        	if(ableToUpdate){
   		        	%>
   		        	<select name="LOCATION_TYPE_ID">
   		        	<option selected value="0">Select One...</option>
							<%
							selectedCodeId = dist.getLocationType();
							for(int i=0;i<locationCodes.size();i++){
								CodeType code = (CodeType)locationCodes.get(i);
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
										selectedCodeId = dist.getLocationType();
										for(int i=0;i<locationCodes.size();i++){
											CodeType code = (CodeType)locationCodes.get(i);
												if(code.getId()==selectedCodeId){
                    			out.println(code.getName());
                    			break;
												}
										}
        		    
        		    }
        		    %>
   		        </td>
       	   </tr>
           	<tr>

           		<td><div align="right"><font class="crumbcurrent">*</font> Name of Chain:</div></td>
           		<td>&nbsp;</td>
   		        <td>
   		        <%= StringManipulation.createTextBox("CHAIN_NAME",dist.getChainName(),ableToUpdate,"") %>
   		        </td>
       	   </tr>
           	<tr>
           		<td><div align="right"><font class="crumbcurrent">*</font> Number of Branches:</div></td>
           		<td>&nbsp;</td>

   		        <td>
							<%= StringManipulation.createTextBox("NUM_OF_BRANCH_LOCATIONS",""+dist.getNumOfBranches(),ableToUpdate,"5") %>
   		        </td>
       	   </tr>
		              	<tr>
           		<td><div align="right"><font class="crumbcurrent">*</font> Years at this location:</div></td>
           		<td>&nbsp;</td>
   		        <td>
				<%= StringManipulation.createTextBox("NUM_OF_YEARS_AT_LOCATION",""+dist.getNumOfYrsAtLocation(),ableToUpdate,"5") %>
   		        </td>

       	   </tr>
           	</table>
           <br><hr align="center" width="600" size="1" color="#999999" noshade>
          <p class="heading3">Facilities</p>
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
          	<tr>
          		<td width="33%">
          			<div align="right"><font class="crumbcurrent">*</font> Approximate total square footage at this facility:</div>

          		</td>
          		<td width="6%">&nbsp;</td>
          		<td width="61%">
          			<%= StringManipulation.createTextBox("FACILITY_AREA",dist.getFacilityArea(),ableToUpdate,"") %>
          		</td>
   		   </tr>
       	  </table><br>
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
          	<tr>

          		<td width="100%" valign="top">
          			<div align="left">Which facilities exist at this location:<br>
       			        <span class="textgray">(Check all that apply)</span> </div><br>
       				<%
							
							ArrayList checkedFacilities = dist.getFacilities();
							HashMap facilitiesHM = new HashMap();
							for(int i=0;i< checkedFacilities.size();i++){
								facilitiesHM.put((String)checkedFacilities.get(i),null);
							}

							String checked = "";
							String checkedOther = "";
							for(int i=0;i<facilitiesCodes.size();i++){
								CodeType code = (CodeType)facilitiesCodes.get(i);

								if(facilitiesHM.containsKey(""+code.getId())){
									checked=" checked";
								}else{
									checked="";
								}
							%>       	
                  <input type="checkbox" name="FACILITIES" value="<%= code.getId() %>" <%= checked + disabled %>><%= code.getName() %><br>
              <% 
              }
               %>
                  <%= StringManipulation.createTextBox("FACILITIES_OTHER",dist.getFacilitiesOther(),ableToUpdate,"") %>
				        	<%
				        	if(ableToUpdate || !dist.getFacilitiesOther().equals("")){
				        	%>
				        	<span class="textgray">&nbsp;(Specify if Other)</span><br> 
				        	<% } %>                   
          		</td>
   					 </tr>
   				 </table>
		     </td>
   		   </tr>
       	  </table>
		   <br><hr align="center" width="600" size="1" color="#999999" noshade><br>
		   <span class="heading3">Services</span><br>
		   <span class="textgray">What services are provided at this location?</span><br><br>

           <table width="100%" border="0" cellspacing="5" cellpadding="0">
           	<tr>
           		<td width="100%" valign="top">
							<%
							
							ArrayList checkedServices = dist.getServices();
							HashMap servicesHM = new HashMap();
							for(int i=0;i< checkedServices.size();i++){
								servicesHM.put((String)checkedServices.get(i),null);
							}							

							checked = "";
							checkedOther = "";
							for(int i=0;i<servicesCodes.size();i++){
								CodeType code = (CodeType)servicesCodes.get(i);
								if(servicesHM.containsKey(""+code.getId())){
									checked=" checked";
								}else{
									checked="";
								}
							%>       	
                  <input type="checkbox" name="SERVICES" value="<%= code.getId() %>" <%= checked + disabled %>><%= code.getName() %><br>
              <% 
              }
              %>
                  <%= StringManipulation.createTextBox("SERVICES_OTHER",dist.getServicesOther(),ableToUpdate,"") %>
				        	<%
				        	if(ableToUpdate || !dist.getServicesOther().equals("")){
				        	%>
				        	<span class="textgray">&nbsp;(Specify if Other)</span><br> 
				        	<% } %>               
           		</td>

       		</tr>
       	  </table>
	      <br><hr align="center" width="600" size="1" color="#999999" noshade><br>
			<span class="heading3">Electronic Commerce</span><br>
			<span class="textgray">What e-commerce capabilities do you currently have?</span>
			<table width="100%" border="0" cellspacing="5" cellpadding="0">
            	<tr>
            		<td width="100%"> <div align="left">EDI Transactions with Customers:</div><br>
							<%
							ArrayList checkedECommerce = dist.getECommerce();
							HashMap ecommerceHM = new HashMap();
							for(int i=0;i< checkedECommerce.size();i++){
								ecommerceHM.put((String)checkedECommerce.get(i),null);
							}		

							checked = "";
							checkedOther = "";
							for(int i=0;i<ediCustCodes.size();i++){
								CodeType code = (CodeType)ediCustCodes.get(i);
								if(ecommerceHM.containsKey(""+code.getId())){
									checked=" checked";
								}else{
									checked="";
								}
							%>       	
                  <input type="checkbox" name="ECOMMERCE" value="<%= code.getId() %>" <%= checked + disabled %>><%= code.getName() %><br>
							<%
              }
               %>
               <br>
							</td>
            </tr>
            	<tr>
            		<td width="100%"> <div align="left">EDI Transactions with Suppliers:</div><br>
            <%
							checked = "";
							checkedOther = "";
							for(int i=0;i<ediSuppliersCodes.size();i++){
								CodeType code = (CodeType)ediSuppliersCodes.get(i);
								if(ecommerceHM.containsKey(""+code.getId())){
									checked=" checked";
								}else{
									checked="";
								}
							%>       	
                  <input type="checkbox" name="ECOMMERCE" value="<%= code.getId() %>" <%= checked + disabled %>><%= code.getName() %><br>
              <% 
              }
               %>
               <br>               
            	</td>
            	</tr>
            	<tr>
            		<td width="100%"><div align="left">VMI Transactions:</div><br>
            		<%
							checked = "";
							checkedOther = "";
							for(int i=0;i<vmiCodes.size();i++){
								CodeType code = (CodeType)vmiCodes.get(i);
								if(ecommerceHM.containsKey(""+code.getId())){
									checked=" checked";
								}else{
									checked="";
								}
							%>       	
                  <input type="checkbox" name="ECOMMERCE" value="<%= code.getId() %>" <%= checked + disabled %>><%= code.getName() %><br>
              <% 
              }
               %>
               <br>               
               </td>
        	   	</tr>
			</table>
			<table width="100%" border="0" cellspacing="5" cellpadding="0">

				<tr>
					<td width="100%"><div align="left">Web Access?</div><br>
							<%
							checked = "";
							checkedOther = "";
							for(int i=0;i<webAccessCodes.size();i++){
								CodeType code = (CodeType)webAccessCodes.get(i);
								if(ecommerceHM.containsKey(""+code.getId())){
									checked=" checked";
								}else{
									checked="";
								}
							%>       	
                  <input type="checkbox" name="ECOMMERCE" value="<%= code.getId() %>" <%= checked + disabled %>><%= code.getName() %><br>
              <% 
              }
               %>
               <br>               
					</td>
				</tr>			
        </table>
		  <br>
			<%
			if(ableToUpdate){
			%>
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
<input type="hidden" name="page" value="saveApp2">
</form>
<p>&nbsp;</p>

  </body>
</html>
