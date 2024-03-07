<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*" %>
<%@ page import="com.eaton.electrical.smrc.util.StringManipulation" %>

 <script language="javascript">
	function newWindow(url, windowName) {
		catWindow = window.open(url, windowName, config='width=650,height=475,location=yes,scrollbars=yes,toolbar=no,resizeable=yes,status=1')
	}
	</script>
<html>

<%@ include file="./SMRCHeader.jsp" %>
<%@ include file="analytics.jsp" %>

<script language="javascript" src="<%= jsURL %>validation/distSignupForm3.js"></script>
<%

Distributor dist = (Distributor)request.getAttribute("dist");

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
									    <td width="12%" align="center"><span class="navCurrent">[ 3 ]</span></td>
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
					<% }else if(request.getParameter("refresh")!=null){ %>
					<blockquote><font class="crumbcurrent"><img align="absmiddle" src="<%= sImagesURL %>alert.gif">&nbsp;The page refreshed with your selection, but you must click the "Save" button to save your changes</font></blockquote>
					<% } %>     	
            <p class="heading2">Distributor Application</p>
           <p class="heading3">Account: <%= acct.getCustomerName() %></p>

            <table width="600" border="0" cellspacing="0" cellpadding="0">
              <tr> 
				<td valign="top"> 
	      	<br><hr align="center" width="600" size="1" color="#999999" noshade><br>
			<span class="heading3">Personnel</span><br>
			<span class="textgray">How many people are employed at this location?<br>
			</span>			<br>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="40%">

						<table width="100%" border="0" cellspacing="5" cellpadding="0">
							<tr>
								<td width="55%"><strong>Inside Sales:</strong></td>
								<td width="45%">&nbsp;</td>
							</tr>
							<tr>
								<td><div align="right"><font class="crumbcurrent">*</font> Construction:</div></td>
								<td><%= StringManipulation.createTextBox("INSIDE_SALES_CONSTR_PERSONNEL",""+dist.getInsideSalesConstrPersonnel(),ableToUpdate,"5") %></td>
							</tr>
							<tr>
								<td><div align="right"><font class="crumbcurrent">*</font> Industrial:</div></td>
								<td><%= StringManipulation.createTextBox("INSIDE_SALES_INDSTR_PERSONNEL",""+dist.getInsideSalesIndstrPersonnel(),ableToUpdate,"5") %></td>
							</tr>
							<tr>
								<td><div align="right"><font class="crumbcurrent">*</font> General:</div></td>
								<td><%= StringManipulation.createTextBox("INSIDE_SALES_GEN_PERSONNEL",""+dist.getInsideSalesGenPersonnel(),ableToUpdate,"5") %></td>

							</tr>
						</table>
						<table width="100%" border="0" cellspacing="5" cellpadding="0">
                        	<tr>
                        		<td width="56%" ><strong>Outside Sales:</strong></td>
                        		<td width="44%">&nbsp;</td>
                   		 	</tr>
                        	<tr>

                        		<td><div align="right"><font class="crumbcurrent">*</font> Construction:</div></td>
                        		<td><%= StringManipulation.createTextBox("OUTSIDE_SALES_CONSTR_PERSONNEL",""+dist.getOutsideSalesConstrPersonnel(),ableToUpdate,"5") %></td>
               			 </tr>
                        	<tr>
                        		<td><div align="right"><font class="crumbcurrent">*</font> Industrial:</div></td>
                        		<td><%= StringManipulation.createTextBox("OUTSIDE_SALES_INDSTR_PERSONNEL",""+dist.getOutsideSalesIndstrPersonnel(),ableToUpdate,"5") %></td>
                   		 	</tr>
                        	<tr>
                        		<td><div align="right"><font class="crumbcurrent">*</font> General:</div>
                   			 </td>
                        		<td><%= StringManipulation.createTextBox("OUTSIDE_SALES_GEN_PERSONNEL",""+dist.getOutsideSalesGenPersonnel(),ableToUpdate,"5") %></td>
                   		 </tr>
                   	 </table>						
					</td>
					<td width="60%" valign="top">
						<table width="100%" border="0" cellspacing="5" cellpadding="0">
							<tr>

								<td><strong>Non Sales:</strong></td>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td width="156">
									<div align="right"><font class="crumbcurrent">*</font> Management:</div>
								</td>
								<td width="183"><%= StringManipulation.createTextBox("MANAGEMENT_PERSONNEL",""+dist.getManagementPersonnel(),ableToUpdate,"5") %>
								</td>
							</tr>
							<tr>
								<td>
									<div align="right"><font class="crumbcurrent">*</font> Counter Sales:</div>
								</td>
								<td><%= StringManipulation.createTextBox("COUNTER_SALES_PERSONNEL",""+dist.getCounterSalesPersonnel(),ableToUpdate,"5") %>
								</td>
							</tr>
							<tr>
								<td>
									<div align="right"><font class="crumbcurrent">*</font> Specialists:</div>
								</td>
								<td><%= StringManipulation.createTextBox("SPECIALIST_PERSONNEL",""+dist.getSpecialistPersonnel(),ableToUpdate,"5") %>
								</td>
							</tr>

							<tr>
								<td>
									<div align="right"><font class="crumbcurrent">*</font> Staff Electrical Engineers:</div>
								</td>
								<td width="183">
								    <%= StringManipulation.createTextBox("ELECTRICAL_ENGINEER_PERSONNEL",""+dist.getElectricalEngPersonnel(),ableToUpdate,"5") %>
								</td>
							</tr>

							<tr>
								<td>
									<div align="right"><font class="crumbcurrent">*</font> Warehouse / Drivers:</div>
								</td>
								<td><%= StringManipulation.createTextBox("WHSE_DRIVERS_PERSONNEL",""+dist.getWhseDriversPersonnel(),ableToUpdate,"5") %>
								</td>
							</tr>
							<tr>

								<td>
									<div align="right"><font class="crumbcurrent">*</font> Administrative / Other:</div>
								</td>
								<td><%= StringManipulation.createTextBox("ADMIN_PERSONNEL",""+dist.getAdminPersonnel(),ableToUpdate,"5") %>
								</td>
							</tr>								
						</table>
					</td>
				</tr>

			</table><br>
	      	<p class="heading2">Key Management Personnel</p>
                   <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    		<tr>
                    			<th width="5%"><div align="center">Last Name</div></th>
                    			<th width="9%"><div align="center">Last Name</div></th>
                    			<th width="12%"><div align="center">First Name</div></th>
                    			<th width="13%"><div align="center">Title</div></th>
                    			<th width="14%"><div align="center">Job Functions</div></th>
                    			<th width="16%"><div align="center">Phone Number</div></th>
                    			<th width="8%"><div align="center">Statement*</div></th>                     			
                    			<th width="30%"><div align="center">Email</div></th>
               			 </tr>
									<%
									ArrayList contacts = new ArrayList();
									contacts = (ArrayList)request.getAttribute("contacts");
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
									%>
                    		<tr<%= bgcolor %>>
													<td nowrap>
													<% 
													if(ableToUpdate){
													%>
													<div align="left">
													<a href="javascript:newWindow('Contacts?page=edit&fwdPage=refreshApp3&contactid=<%= contact.getId() %>','contacts')"><img src="<%= sImagesURL %>editIcon.gif" border="0" align="absmiddle" alt="Edit"></a>&nbsp;<a href="javascript:newWindow('Contacts?page=delete&fwdPage=refreshApp3&contactid=<%= contact.getId() %>','contacts')"><img src="<%= sImagesURL %>deleteIcon.gif" border="0" align="absmiddle" alt="Delete"></a>&nbsp;
													</div>
													<% } %>
													</td>
                    			<td><div align="left"><%= contact.getLastName() %></div></td>
                    			<td><div align="left"><%= contact.getFirstName() %></div></td>
                    			<td><div align="left"><%= contact.getTitle() %></div></td>
                    			<td><div align="left"><%= contact.getFunctionalPosition() %></div></td>
                    			<td><div align="left"><%= contact.getPhone() %></div></td>
                    			<td><div align="center">
                    			<% if(contact.isDistributorStatement()){
                    				out.println("Y");
                    			}
                    			%>
                    			</div></td>
                    			<td><div align="left"><a href="mailto:<%= contact.getEmailAddress() %>"><%= contact.getEmailAddress() %></a></div></td>
               			 </tr>                    		
									<% } %>
											<tr>
												<td colspan="8"><br>* Indicates contact will receive Distributor Statement</td>
										 	</tr>
           		 </table>
            	<br>
           		<%
           		if(ableToUpdate){
           		%>
           		 <table width="100%" border="0" cellspacing="5" cellpadding="0">
                    	<tr>
                    		<td><a href="javascript:newWindow('Contacts?page=add&fwdPage=refreshApp3&acctId=<%= dist.getVcn() %>','contacts')"><img src="<%= sImagesURL %>button_add_contact.gif" width="70" height="20" border="0"></a></td>
               		 </tr>
            	</table>
            <br>   
		  			<br>
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
<input type="hidden" name="page" value="saveApp3">
</form>
<p>&nbsp;</p>

  </body>
</html>
