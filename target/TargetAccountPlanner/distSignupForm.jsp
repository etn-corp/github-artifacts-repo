<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*" %>
<%@ page import="com.eaton.electrical.smrc.util.StringManipulation" %>
<html>
<%@ include file="./SMRCHeader.jsp" %>
<%@ include file="analytics.jsp" %>

<script language="javascript" src="<%= jsURL %>validation/distSignupForm.js"></script>

<%

Distributor dist = (Distributor)request.getAttribute("dist");

Account acct = header.getAccount();//(Account)request.getAttribute("acct");
User usr = header.getUser();//(User)request.getAttribute("usr");
boolean ableToUpdate = usr.ableToUpdate(acct.getDistrict()) || usr.hasSegmentOverride(acct) || (usr.equals(acct.getUserIdAdded()) && acct.isProspect());
if(!usr.hasOverrideSecurity()){
	if(!acct.isProspect()){
		ableToUpdate=true;
	}
}
String disabled="";
if(!ableToUpdate){
	disabled=" disabled";
}

ArrayList segments = dist.getSegments();
ArrayList countySales = dist.getCountySales();
TreeMap counties = (TreeMap)request.getAttribute("counties");
ArrayList locationCodes =(ArrayList)request.getAttribute("locationCodes");
ArrayList customerCategory =(ArrayList)request.getAttribute("customerCategory");
ArrayList applyingForCodes = (ArrayList)request.getAttribute("applyingForCodes");
ArrayList ownershipCodes = (ArrayList)request.getAttribute("ownershipCodes");
ArrayList bizActivityCodes = (ArrayList)request.getAttribute("bizActivityCodes");
ArrayList facilitiesCodes =(ArrayList)request.getAttribute("facilitiesCodes");
ArrayList productLines = (ArrayList)request.getAttribute("productLines");

ArrayList elCodes = (ArrayList)request.getAttribute("electricalLines");

int selectedCodeId = -1;
%>
<script language='javascript'>

	
	function changePercentage(type){
	    newTotal = 0;
	    divId = "";
	    if (type == "segment"){
	        newTotal = getSegmentPercentage();
	        divId = "totalSegmentPercentage";
	    } else if (type == "market"){
	        newTotal = getMarketPercentage();
	        divId = "totalMarketPercentage";
	    } else {
	        newTotal = getIntoStockPercentage();
	        divId = "totalIntoStockPercentage";
	    }
	    newTotalString = newTotal + "%";
	    setInnerHTML(divId, newTotalString);
	}
	
	function getSegmentPercentage(){
		newTotal = 0;
		<% for (int i= 0; i<segments.size(); i++){
		    Segment seg = (Segment) segments.get(i);
		%>
				if ((isNaN(document.theform.SEGMENT_<%= seg.getSegmentId() %>.value)) ||
				     (document.theform.SEGMENT_<%= seg.getSegmentId() %>.value == "")){
				    document.theform.SEGMENT_<%= seg.getSegmentId() %>.value = 0;
				}
				newTotal += parseFloat(document.theform.SEGMENT_<%= seg.getSegmentId() %>.value);
				
		<%   }
		%>
		if ((isNaN(document.theform.OTHER_CUSTOMER_SEGMENT.value)) ||
			     (document.theform.OTHER_CUSTOMER_SEGMENT.value == "")){
		    document.theform.OTHER_CUSTOMER_SEGMENT.value = 0;
			}
		newTotal += parseFloat(document.theform.OTHER_CUSTOMER_SEGMENT.value);
		return newTotal;
	}
	
	function getIntoStockPercentage(){
		newTotal = 0;
		<% for(int i=0;i<productLines.size();i++){
			Product product = (Product)productLines.get(i);
		%>
				if ((isNaN(document.theform.PRODUCTLINE_STOCK_<%= product.getId() %>.value)) ||
				     (document.theform.PRODUCTLINE_STOCK_<%= product.getId() %>.value == "")){
				    document.theform.PRODUCTLINE_STOCK_<%= product.getId() %>.value = 0;
				}
				newTotal += parseFloat(document.theform.PRODUCTLINE_STOCK_<%= product.getId() %>.value);
		
		<%   }
		%>
		return newTotal;
	}

	function getMarketPercentage(){
		newTotal = 0;
		<% for(int i=0;i<12;i++){
		%>
				if ((isNaN(document.theform.COUNTY_SALES<%= (i+1) %>_VALUE.value)) ||
				     (document.theform.COUNTY_SALES<%= (i+1) %>_VALUE.value == "")){
				    document.theform.COUNTY_SALES<%= (i+1) %>_VALUE.value = 0;
				}
				newTotal += parseFloat(document.theform.COUNTY_SALES<%= (i+1) %>_VALUE.value);
		
		<%   }
		%>
		return newTotal;
	}	
	
	var setInnerHTML = function( id, str ){
		if(!document.getElementById) return; // Not Supported
		if(document.getElementById){
			document.getElementById(id).innerHTML = str;
		}
	}
	
	function getPrinter() {
		window.print();
	}
</script>
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
			<% if(request.getParameter("save")!=null){ %>
			<blockquote><font class="crumbcurrent">Save Successful</font></blockquote>
			<% } %>
            <p class="heading2">Distributor Application</p>
            <table width="600" border="0" cellspacing="5" cellpadding="2">
            	<tr>
           			<td colspan='2' class="heading3">Account: <%= acct.getCustomerName() %></td>
            	</tr>
           		<tr>
           			<td colspan='2'><a href='javascript:getPrinter()'>Click here to print this page</a></td>
           		</tr>
              	<tr>
					<td colspan='2'><hr align="center" width="600" size="1" color="#999999" noshade></td>
   	     		</tr>
   	     		<tr>
   					<td class="heading3" colspan='2'>Key Management Personnel</td>
   				</tr>
   	     		<tr>
   	     			<td colspan='2' class="crumbcurrent">
   	     				All Distributer Applications must contain a KEY Contact. If no contacts are displayed below, please click the Add Contact button to enter contact information before you start the application process.
   	     			</td>
   	     		</tr>
  				<tr>	
   					<td colspan='2'>
                   		<table width="100%" border="0" cellspacing="1" cellpadding="0">
                    		<tr>
                    			<th width="5%"><div align="center"></div></th>
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
													<a href="javascript:newWindow('Contacts?page=edit&fwdPage=app1&contactid=<%= contact.getId() %>','contacts')"><img src="<%= sImagesURL %>editIcon.gif" border="0" align="absmiddle" alt="Edit"></a>&nbsp;<a href="javascript:newWindow('Contacts?page=delete&fwdPage=refreshApp3&contactid=<%= contact.getId() %>','contacts')"><img src="<%= sImagesURL %>deleteIcon.gif" border="0" align="absmiddle" alt="Delete"></a>&nbsp;
													</div>
													<% } %>
													</td>
                    			<td><div align="left"><%= contact.getLastName() %></div></td>
                    			<td><div align="left"><%= contact.getFirstName() %></div></td>
                    			<td><div align="left"><%= contact.getTitle() %></div></td>
                    			<td><div align="left"><%= contact.getFunctionalPosition() %></div></td>
                    			<td><div align="left"><%= contact.getPhone() %></div></td>
                    			<input type='hidden' name='CONTACT_LAST_NAME' value='<%=contact.getLastName() %>'>
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
						 	<% if(ableToUpdate){ %>
	                    	<tr>
	                    		<td bgcolor='#EFEFEF' colspan='8'><a href="javascript:newWindow('Contacts?page=add&fwdPage=app1&acctId=<%= dist.getVcn() %>','contacts')"><img src="<%= sImagesURL %>button_add_contact.gif" width="70" height="20" border="0"></a></td>
	               		 	</tr>
	               		 	<% } %>
           		 		</table>
            		</td>
   				</tr>
   				<tr>
					<td colspan='2'><hr align="center" width="600" size="1" color="#999999" noshade></td>
   	     		</tr>
   	     		<tr>
   	     			<td colspan='2'>
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
   	     			</td>
   	     		</tr>
   	     		<tr>
            		<td><span class="crumbcurrent">*</span>Federal Tax ID:</td>
            		<td><%=StringManipulation.createTextBox("FEDERAL_TAX_ID",dist.getFederalTaxId(),ableToUpdate,"","10")%>(xx-xxxxxxx)</td>
           	 	</tr>
				<tr>
			 		<td><span class="crumbcurrent">*</span>Dun and Bradstreet Number:</td>
			 		<td><%=StringManipulation.createTextBox("DUNN_BRADSTREET",dist.getDunnBradStreet(),ableToUpdate,"","11")%>(xx-xxx-xxxx)</td>
			 	</tr>
			 	<tr>
			 		<td><span class="crumbcurrent">*</span>Location Type:</td>
			 		<td>
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
			 		<td>Name of Chain:</td>
			 		<td><%= StringManipulation.createTextBox("CHAIN_NAME",dist.getChainName(),ableToUpdate,"") %></td>
			 	</tr>
			 	<tr>
			 		<td><span class="crumbcurrent">*</span>Customer Category</td>
			 		<td>
			 			<% if(ableToUpdate) { %>
		   		        	<select name="CUSTOMER_CATEGORY">
		   		        		<option selected value="0">Select One...</option>
								<%
									selectedCodeId = dist.getCustomerCategory();
									for(int i=0; i < customerCategory.size(); i++) {
										CodeType code = (CodeType)customerCategory.get(i);
										if(code.getId()==selectedCodeId){
								%>       	
	                    					<option value="<%= code.getId() %>" selected><%= code.getName() %></option>
	              						<% }else{ %>
	             				 			<option value="<%= code.getId() %>"><%= code.getName() %></option>
	              						<% }
	             			 		}
	               				%>
	        		    	</select>
	        		     <% } else {
								selectedCodeId = dist.getCustomerCategory();
								for(int i=0; i < customerCategory.size(); i++){
									CodeType code = (CodeType)customerCategory.get(i);
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
	        		<td><span class="crumbcurrent">*</span>Applying For:</td>
		   			<td>
							<%
							if(ableToUpdate){
							%>
							 <select name="APPLYING_FOR_TYPE_ID">
											<option selected value="0">Select One...</option>
							<%
							
							selectedCodeId = dist.getApplyingFor();
								for(int i=0;i<applyingForCodes.size();i++){
									CodeType code = (CodeType)applyingForCodes.get(i);
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

								selectedCodeId = dist.getApplyingFor();
								for(int i=0;i<applyingForCodes.size();i++){
									CodeType code = (CodeType)applyingForCodes.get(i);
									if(code.getId()==selectedCodeId){
		                    	%>
		                    	<b><%= code.getName() %></b>
		                    	<%
		                    	break;
											}
			              	}
			               %>
		              
		              	<% } %>

	   				</td>
		   		</tr>
		   		<tr>
		   			<td>&nbsp;</td>
		   		    <td>
						<%= StringManipulation.createTextBox("APPLYING_FOR_OTHER_NOTES",dist.getApplyingForOtherNotes(),ableToUpdate,"") %>
	         			<%
							if(!ableToUpdate && !dist.getApplyingForOtherNotes().equals("")){
							%>
							<br><span class="textgray">(Explanation for Other) </span></td>
							<%
							}else if(ableToUpdate){
	         			%>
	         			<br><span class="textgray">(If other please explain) </span>
	         		</td>
         			<% } %>
			   	</tr>
	           	<tr>
	           		<td>Previous Name of Company:</td>
	           		<td><%= StringManipulation.createTextBox("PREVIOUS_NAME",dist.getPreviousName(),ableToUpdate,"") %></td>
	       	   </tr>		   
	           	<tr>
           			<td>Previous Vista Number:</td>
           			<td><%= StringManipulation.createTextBox("PREVIOUS_VISTA_CUSTOMER_NUMBER",dist.getPreviousVistaCustNumber(),ableToUpdate,"") %></td>
           		</tr>
			   	<tr>
		   			<td><span class="crumbcurrent">*</span>Form of Ownership:</td>
		   			<td>
   		        		<% if(ableToUpdate){ %>
   		        			<select name="OWNERSHIP_FORM_TYPE_ID">
                   				<option selected value="0">Select One...</option>
							<%
								selectedCodeId = dist.getFormOfOwnership();
							
								for(int i=0;i<ownershipCodes.size();i++){
									CodeType code = (CodeType)ownershipCodes.get(i);
									if(code.getId()==selectedCodeId){
								%>       	
	                    			<option value="<%= code.getId() %>" selected><%= code.getName() %></option>
	              				<% }else{ %>
	             				 	<option value="<%= code.getId() %>"><%= code.getName() %></option>
	              				<% }
              					}
               					%>
                 			</select>
                		<%
                			}else{
                				selectedCodeId = dist.getFormOfOwnership();
									for(int i=0;i<ownershipCodes.size();i++){
										CodeType code = (CodeType)ownershipCodes.get(i);
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
   		        	<td>&nbsp;</td>
	         		<td><%= StringManipulation.createTextBox("OWNERSHIP_FORM_NOTES",dist.getFormOfOwnershipNotes(),ableToUpdate,"") %>
	        			<%
						if(ableToUpdate || !dist.getFormOfOwnershipNotes().equals("")){
						%>
         				<br><span class="textgray">(Subsidiary of Whom OR Explanation for other)</span>
						<%
					 	} %> &nbsp;           
		   			</td>
		   		</tr>
		   		<tr>
		   			<td><span class="crumbcurrent">*</span>Primary Business Activity:</td>
	   		    	<td>
	   		    		<%
							if(ableToUpdate){
							%>
	   		    			<select name="PRIMARY_BUS_ACTIVITY_TYPE_ID">
	   		    				<option selected value="0">Select One...</option>
							<%
							
							selectedCodeId = dist.getPrimaryBusinessActivity();
							for(int i=0;i<bizActivityCodes.size();i++){
								CodeType code = (CodeType)bizActivityCodes.get(i);
								if(code.getId()==selectedCodeId){
							%>       	
                    	<option value="<%= code.getId() %>" selected><%= code.getName() %></option>
              			<% }else{ %>
             				 <option value="<%= code.getId() %>"><%= code.getName() %></option>
              				<% }
              				}
              			 %>
              			</select>
              				<%
              				}else{

										selectedCodeId = dist.getPrimaryBusinessActivity();
										for(int i=0;i<bizActivityCodes.size();i++){
											CodeType code = (CodeType)bizActivityCodes.get(i);
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
		   			<td><span class="crumbcurrent">*</span>Approximate square footage at this facility:</td>
					<td><%= StringManipulation.createTextBox("FACILITY_AREA",dist.getFacilityArea(),ableToUpdate,"") %></td>
		   		</tr>
		   		<tr>
          			<td><span class="crumbcurrent">*</span>Which facilities exist at this location:<br><span class="textgray">(Check all that apply)</span></td>
       				<td>
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
								//if(code.getSeq() > 0) {
									if(facilitiesHM.containsKey(""+code.getId())){
										checked=" checked";
									}else{
										checked="";
									}
								//}
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
   				<tr>
   					<td colspan='2'><span class="crumbcurrent">*</span>Percentage of sales made to:</td>
   				</tr>
   				<tr>
   					<td colspan='2'>
						<table width="100%" border="0" cellspacing="5" cellpadding="0">
						<%
							
							for(int i=0;i<segments.size();i++){
								Segment segment = (Segment)segments.get(i);
						%>
							<tr>
								<td width="28%"><div align="right"><%= segment.getName() %>:</div></td>
								<td colspan="2"><%= StringManipulation.createTextBoxWithOnBlur("SEGMENT_" + segment.getSegmentId(),""+segment.getSalesPercentage(),ableToUpdate,"3","3", "changePercentage('segment')") %> %
								<input type="hidden" name="SEGMENT_NAME_<%= segment.getSegmentId() %>" value="<%= segment.getName() %>"><input type="hidden" name="SEGMENT_IDS" value="<%= segment.getSegmentId() %>"></td>
							</tr>
						<% } %>
							<tr>
								<td width="28%"><div align="right">Other:</div></td>
								<td width="15%">
									<%= StringManipulation.createTextBoxWithOnBlur("OTHER_CUSTOMER_SEGMENT",""+dist.getOtherCustomerSegmentPercentage(),ableToUpdate,"3","3", "changePercentage('segment')") %> %
								</td>
							    <td width="57%"><span class="textgray">If other please specify:</span>
									<%= StringManipulation.createTextBox("OTHER_CUSTOMER_SEGMENT_VALUE",dist.getOtherCustomerSegmentPercentageNote(),ableToUpdate,"","15") %>				    	
							    </td>
			
							</tr>
							<tr>
								<td width="28%"><div align="right"><b>Total:</b></div></td>
								<td width="15%">
									<div id="totalSegmentPercentage"><script>changePercentage("segment")</script></div>
								</td>
								<td width="57%">(Total Percentage must equal 100%)</td>
								</td>
							</tr>
										
						</table>
					</td>
				</tr>
				<tr>
					<td colspan='2'><hr align="center" width='100%' size="1" color="#999999" noshade></td>
				</tr>
				<tr>
					<td colspan='2' class="heading3"><span class="crumbcurrent">*</span>Market Coverage</td>
				</tr>
				<tr>
					<td colspan='2'><hr align="center" width='100%' size="1" color="#999999" noshade></td>
				</tr>
				<tr>
					<td><b>County, State:</b></td>
					<td><b>% Sold into Each County:</b></td>		
				</tr>
			<%
				String selected="";
				for(int i=0; i < 12; i++){
			%>
				<tr>
					<td>
						<% if(ableToUpdate){ %>
							<select name="COUNTY_SALES<%= i+1 %>">
							<option value="0">Please Select...</option>
						<% }						
							DistributorCountySalesRecord rec = new DistributorCountySalesRecord();
							if(i<countySales.size()){
								rec = (DistributorCountySalesRecord)countySales.get(i);				
							}							
							
							Set countySet = counties.entrySet();
				      		Iterator it = countySet.iterator();
					    	String salesValue="";
				      		String countyValue="";
				      
				      		while (it.hasNext()){
			         			selected="";
				        		java.util.Map.Entry county = (java.util.Map.Entry) it.next();
								if(rec.getCounty().equals(county.getValue())){
									selected=" selected";
									countyValue=(String)county.getKey();
									salesValue=rec.getPercentage();
									
								}
								if(ableToUpdate){
								%>
								<option value="<%= county.getValue() %>"<%= selected %>><%= county.getKey() %></option>
								<%
								}
				      		}
						%>
						<% if(ableToUpdate){ %>
	           				</select>
						<% }else{ %>
							<%= countyValue %>
						<% } %>
					</td>
					<td>
					<%= StringManipulation.createTextBoxWithOnBlur("COUNTY_SALES" + (i+1) + "_VALUE",salesValue,ableToUpdate,"","3","changePercentage('market')") %> %
<!--					<input type="text" name="COUNTY_SALES<%= i+1 %>_VALUE" value="<%= salesValue %>">-->
					</td>
				</tr>
						<%
						}
						%>	
				<tr class="cellshade">
						<td>
							<div align="right"><strong>Total</strong>:</div>

						</td>
					    <td>
					    	<strong><div id="totalMarketPercentage"><script>changePercentage('market')</script></div></strong>
					    </td>
				</tr>
       	 		<tr>
					<td colspan='2'><hr align="center" width="600" size="1" color="#999999" noshade></td>
   	     		</tr>
 				<tr>
					<td><span class="crumbcurrent">*</span>Projected Eaton Stock Sales:</td>
					<td>
						$<%= StringManipulation.createTextBox("PROJECTED_EATON_SALES_1",""+dist.getProjectedEatonSalesYr1(),ableToUpdate,"10") %> 
						&nbsp;&nbsp;<span class="textgray"><font class="crumbcurrent">*</font> Year 1</span>
						<br>
				    	$<%= StringManipulation.createTextBox("PROJECTED_EATON_SALES_2",""+dist.getProjectedEatonSalesYr2(),ableToUpdate,"10") %>  
				    	&nbsp;&nbsp;<span class="textgray"><font class="crumbcurrent">*</font> Year 3</span>
             		</td>
				</tr>
   	     		<tr>
   	     			<td><span class="crumbcurrent">*</span>Projected % Eaton Stock Sales vs Competitor<br><span class="textgray">(If Exclusive = 100% Eaton)</span></td>
					<td>
						&nbsp;<%= StringManipulation.createTextBox("PROJECTED_SALES_VS_COMP_1",""+dist.getProjectedVScompYr1(),ableToUpdate,"10") %>
						&nbsp;&nbsp;<span class="textgray"><font class="crumbcurrent">*</font> Year 1</span>
						<br>
						&nbsp;<%= StringManipulation.createTextBox("PROJECTED_SALES_VS_COMP_2",""+dist.getProjectedVScompYr2(),ableToUpdate,"10") %>
						&nbsp;&nbsp;<span class="textgray"><font class="crumbcurrent">*</font> Year 3</span>
					</td>
		   		</tr>
  	     		<tr>
   	     			<td><span class="crumbcurrent">*</span>Competiting Electrical Lines<br><span class="textgray">(Choose as many as apply)</span></td>
   	     			<td>
 					<%	
							ArrayList checkedEL = dist.getElectricalLines();
							//System.out.println(checkedEL);
							HashMap elHM = new HashMap();
							for(int i=0;i< checkedEL.size();i++){
								elHM.put((String)checkedEL.get(i),null);
							}

							checked = "";
							checkedOther = "";

							for(int i=0; i < elCodes.size(); i++){
								CodeType code = (CodeType)elCodes.get(i);
								//if(code.getSeq() > 0) {
									if(elHM.containsKey(""+code.getId())){
										checked=" checked";
									}else{
										checked="";
									}
								//}
							%>       	
                  			<input type="checkbox" name="ELECTRICAL_LINES" value="<%= code.getId() %>" <%= checked + disabled %>><%= code.getName() %><br>
              				<% 
              				}
               				%>
   	     			</td>
   	     		</tr>
   	     		<tr>
   	     			<td>Business Justification</td>
   	     			<td>
   	     				<a target='_blank' href='http://ecm-prod-cs.etn.com/ecm/idcplg?IdcService=GET_FILE&allowInterrupt=1&RevisionSelectionMethod=LatestReleased&noSaveAs=1&Rendition=Primary&&dDocName=ChannelRiskAnalysis'>
   	     					Ken's Strategy Sheet
   	     				</a>
					</td>
   	     		</tr>
				<tr>
					<td>Buying Group Association:</td>
					<td>
       					<%
							
							ArrayList checkedBuyingGroups = dist.getBuyingGroupAssn();
							HashMap groupsHM = new HashMap();
							for(int i=0;i< checkedBuyingGroups.size();i++){
								groupsHM.put((String)checkedBuyingGroups.get(i),null);
							}
							
							ArrayList codes = (ArrayList)request.getAttribute("buyingGroupCodes");

							checked = "";
							checkedOther = "";
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
               		</td>					
				</tr>
				<tr>
					<td>Other notes or comments:</td>
					<td>
					<% if(ableToUpdate){ %>
							<textarea name="DISTRIBUTOR_NOTES" cols="40" rows="4"><%= dist.getDistributorNotes() %></textarea>
					<% }else{ %>
							<%= dist.getDistributorNotes() %>
					<% } %>
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
			<table>
			   	<tr>
					<td><hr align="center" width="600" size="1" color="#999999" noshade></td>
 	     	   	</tr>
 	     	   	<tr>
					<td class="heading3">Module Information</td>
				</tr>
				<tr>
					<td class="crumbcurrent">
						All Distributor Applications must have associated module information. Please make sure you have clicked the 'Save' button above, then click the 'Add/Modify Product Module' link below or in the left navigation pane to fill out the Module Information section completing the Distributor Application section of 'Required Vista Forms'.
					</td>
				</tr>
				<tr>
					<%
						String modURL = "";
						if(distForms.isDistributor()){
							modURL = "DistributorProductLoadingModule?where=distAPP&acctId=" + acct.getVcn();
						} 
						if(distForms.isDistributor() && !acct.isProspect() ){
							modURL = "DistributorProductLoadingModuleHistory?where=distAPP&acctId=" + acct.getVcn();
						}
					 %>
					<td><span class="crumbcurrent">*</span><a href="<%=modURL%>">Add/Modify Product Module</a></td>
				</tr>
		    	<tr>
					<td><hr align="center" width="600" size="1" color="#999999" noshade></td>
 	     		</tr>
			</table>
         </td>

        </tr>
    </table>
    
    </td>
  </tr>
</table>
<input type="hidden" name="acctId" value="<%= dist.getVcn() %>">
<input type="hidden" name="page" value="saveApp1">

</form>
<p>&nbsp;</p>

  </body>
</html>
