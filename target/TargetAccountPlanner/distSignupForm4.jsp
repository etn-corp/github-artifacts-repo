<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*" %>
<%@ page import="com.eaton.electrical.smrc.util.*" %>


<html>
<%@ include file="./SMRCHeader.jsp" %>
<%@ include file="analytics.jsp" %>

<script language="javascript" src="<%= jsURL %>validation/distSignupForm4.js"></script>
<%

Distributor dist = (Distributor)request.getAttribute("dist");
ArrayList segments = dist.getSegments();
Account acct = header.getAccount();
User usr = header.getUser();

boolean ableToUpdate = usr.ableToUpdate(acct.getDistrict()) || usr.hasSegmentOverride(acct) || (usr.equals(acct.getUserIdAdded()) && acct.isProspect());
if(!usr.hasOverrideSecurity()){
	if(!acct.isProspect()){
		ableToUpdate=false;
	}
}

ArrayList productLines = (ArrayList)request.getAttribute("productLines");
ArrayList vendors = (ArrayList)request.getAttribute("vendors");
ArrayList keyAccounts = (ArrayList)request.getAttribute("keyAccounts");

TreeMap counties = (TreeMap)request.getAttribute("counties");

ArrayList countySales = dist.getCountySales();

boolean vendorFound=false;

%>

 <script language="javascript"> 
    function newWindow(url, windowName) {
		catWindow = window.open(url, windowName, config='width=650,height=475,location=no,scrollbars=yes,toolbar=no,resizeable=yes,status=1')
	}


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
									    <td width="12%" align="center"><span class="navCurrent">[ 4 ]</span></td>
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
				<br>
	      	<hr align="center" width="600" size="1" color="#999999" noshade><br>

			<span class="heading3">Customer Segments</span><br>
			<span class="textgray">Percentage of sales made to:</span><br><br>
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

	      	<br><hr align="center" width="600" size="1" color="#999999" noshade><br>
			<span class="heading3">Products / Sales</span><br>
			<span class="textgray">Identify total sales, by product category, at this location</span><br><br>
			<table width="100%" border="0" cellspacing="1" cellpadding="2">
				<tr>

					<th width="30%">&nbsp;</th>
					<th width="20%"><div align="center">Total Sales</div></th>
					<th width="6%"><div align="center">% Sold Through Stock</div></th>
				  <th width="20%">Manufacturer Name<br>Current</th>
				  <th width="24%">Manufacturer Name<br>Proposed</th>
				</tr>
				<%
				String selected="";
				double totalSalesTotal = 0;
				int totalSalesThruStockTotal = 0;
				
				ArrayList setProducts = dist.getProducts();
				TreeMap prodsTM= new TreeMap();
				
				for(int i=0;i<setProducts.size();i++){
					Product prod = (Product)setProducts.get(i); 
					prodsTM.put(prod.getId(), prod);
				}

				
				
				for(int i=0;i<productLines.size();i++){
					Product product = (Product)productLines.get(i);
					Product setProduct = (Product)prodsTM.get(product.getId());
					if(setProduct==null){
						setProduct= new Product();
					}
					
					totalSalesTotal=totalSalesTotal+setProduct.getTotalSales();
					totalSalesThruStockTotal=totalSalesThruStockTotal+Globals.a2int(setProduct.getTotalSalesThruStock());					
					
					
					%>
					
				<tr class="cellShade">
					<td valign="top"><div align="right"><%= product.getDescription() %>:<br>
					<span class="textgray"></span></div></td>	
					<td nowrap valign="top">
						<div align="center">
					$<%= StringManipulation.createTextBox("PRODUCTLINE_TOTAL_" + product.getId(),Money.formatDoubleAsDollars(setProduct.getTotalSales()),ableToUpdate,"8","12") %><input type="hidden" name="PRODUCT_LINES" value="<%= product.getId() %>">
	    		</div></td>
					<td nowrap width="9%" valign="top">
						<div align="center"><%= StringManipulation.createTextBoxWithOnBlur("PRODUCTLINE_STOCK_" + product.getId(),setProduct.getTotalSalesThruStock(),ableToUpdate,"4","3","changePercentage('intoStock')") %> %
				   </div></td>
				   
				   
					<td valign="top">

				    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
				    		<tr>
				    			<td><div align="left">Primary<br>
			    				    <% if(ableToUpdate){ %>
			    				    <select name="PRODUCTLINE_CURRENT_PRIMARY_<%= product.getId() %>">
		    				    		<option value="0">Select One...</option>
		    				    		<%
		    				    		for(int j=0;j<vendors.size();j++){
		    				    			selected = "";
		    				    			Vendor vendor = (Vendor)vendors.get(j);
		    				    			if(vendor.getId()==Globals.a2int(setProduct.getCurrentPrimaryManufacturer())){
		    				    				selected = " selected";
		    				    			}
		    				    		%>
		    				    			<option value="<%= vendor.getId() %>" <%= selected %>><%= vendor.getDescription() %></option>
		    				    		<%
		    				    		}
		    				    		%>
				    	        	</select>
				    	        <% }else{
				    	       			 for(int j=0;j<vendors.size();j++){
			    				    			selected = "";
			    				    			Vendor vendor = (Vendor)vendors.get(j);
			    				    			if(vendor.getId()==Globals.a2int(setProduct.getCurrentPrimaryManufacturer())){
			    				    				%>
			    				    				&nbsp;&nbsp;<b><%= vendor.getDescription() %></b><br>
			    				    				<%
			    				    				vendorFound=true;
			    				    				break;
			    				    			}
			    				    		}
			    				    		if(vendorFound==false){
														out.println("&nbsp;&nbsp;<b>N/A</b>");   				    		
			    				    		}else{
				    				    		vendorFound=false;
				    				    	}
				    	        
				    	        }
				    	        %>
				    			</div></td>
			    			</tr>

				    		<tr>
				    			<td><div align="left">Secondary<br>
		    				       <% if(ableToUpdate){ %>
		    				       <select name="PRODUCTLINE_CURRENT_SECONDARY_<%= product.getId() %>">
                        <option value="0">Select One...</option>
		    				    		<%
		    				    		
		    				    		for(int j=0;j<vendors.size();j++){
		    				    			selected = "";
		    				    			Vendor vendor = (Vendor)vendors.get(j);
		    				    			if(vendor.getId()==Globals.a2int(setProduct.getCurrentSecondaryManufacturer())){
		    				    				selected = " selected";
		    				    			}
		    				    		%>
		    				    			<option value="<%= vendor.getId() %>" <%= selected %>><%= vendor.getDescription() %></option>
		    				    		<%
		    				    		}
		    				    		%>                                   		
                        </select>
				    	        <% }else{
				    	       			 for(int j=0;j<vendors.size();j++){
			    				    			selected = "";
			    				    			Vendor vendor = (Vendor)vendors.get(j);
			    				    			if(vendor.getId()==Globals.a2int(setProduct.getCurrentSecondaryManufacturer())){
			    				    				%>
			    				    				&nbsp;&nbsp;<b><%= vendor.getDescription() %></b><br>
			    				    				<%
			    				    				vendorFound=true;
			    				    				break;
			    				    			}
			    				    		}
				    	       			if(vendorFound==false){
														out.println("&nbsp;&nbsp;<b>N/A</b>");   				    		
			    				    		}else{
				    				    		vendorFound=false;
				    				    	}
				    	        }
				    	        %>                        
				    			</div></td>
			    			</tr>
				    		<tr>

				    			<td><div align="left">Other<br>
		    				        <% if(ableToUpdate){ %>
		    				        <select name="PRODUCTLINE_CURRENT_OTHER_<%= product.getId() %>">
                        <option value="0">Select One...</option>
		    				    		<%

		    				    		for(int j=0;j<vendors.size();j++){
		    				    			selected = "";
		    				    			Vendor vendor = (Vendor)vendors.get(j);
		    				    			if(vendor.getId()==Globals.a2int(setProduct.getCurrentOtherManufacturer())){
		    				    				selected = " selected";
		    				    			}
		    				    		%>
		    				    			<option value="<%= vendor.getId() %>" <%= selected %>><%= vendor.getDescription() %></option>
		    				    		<%
		    				    		}
		    				    		%>
                        </select>
				    	        <% }else{
				    	       			 for(int j=0;j<vendors.size();j++){
			    				    			selected = "";
			    				    			Vendor vendor = (Vendor)vendors.get(j);
			    				    			if(vendor.getId()==Globals.a2int(setProduct.getCurrentOtherManufacturer())){
			    				    				%>
			    				    				&nbsp;&nbsp;<b><%= vendor.getDescription() %></b><br>
			    				    				<%
			    				    				vendorFound=true;
			    				    				break;
			    				    			}
			    				    		}
				    	        		if(vendorFound==false){
														out.println("&nbsp;&nbsp;<b>N/A</b>");  				    		
			    				    		}else{
				    				    		vendorFound=false;
				    				    	}
				    	        }
				    	        %>                          
				    			</div></td>
			    			</tr>
		    		</table>
				    </td>

				    <td valign="top">
				    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
                        	<tr>
                    		<td><div align="left">Primary<br>
               			   <% if(ableToUpdate){ %>
                			  <select name="PRODUCTLINE_PROPOSED_PRIMARY_<%= product.getId() %>">
                               	<option value="0">Select One...</option>
		    				    		<%

		    				    		for(int j=0;j<vendors.size();j++){
		    				    			selected = "";
		    				    			Vendor vendor = (Vendor)vendors.get(j);
		    				    			if(vendor.getId()==Globals.a2int(setProduct.getProposedPrimaryManufacturer())){
		    				    				selected = " selected";
		    				    			}
		    				    		%>
					    				    			<option value="<%= vendor.getId() %>" <%= selected %>><%= vendor.getDescription() %></option>
					    				    		<%
					    				    		}
					    				    		%>
                        </select>
                       <% }else{
				    	       			 for(int j=0;j<vendors.size();j++){
			    				    			selected = "";
			    				    			Vendor vendor = (Vendor)vendors.get(j);
			    				    			if(vendor.getId()==Globals.a2int(setProduct.getProposedPrimaryManufacturer())){
			    				    				%>
			    				    				&nbsp;&nbsp;<b><%= vendor.getDescription() %></b><br>
			    				    				<%
			    				    				vendorFound=true;
			    				    				break;
			    				    			}
			    				    		}
				    	        		if(vendorFound==false){
														out.println("&nbsp;&nbsp;<b>N/A</b>");			    		
			    				    		}else{
				    				    		vendorFound=false;
				    				    	}
				    	        }
				    	        %>  
                        
                   			</div></td>

                   		</tr>
                        	<tr>
                    		<td><div align="left">Secondary<br>
                			  <% if(ableToUpdate){ %>
                			 <select name="PRODUCTLINE_PROPOSED_SECONDARY_<%= product.getId() %>">
                               		<option value="0">Select One...</option>
		    				    		<%

		    				    		for(int j=0;j<vendors.size();j++){
			    				    		selected = "";
		    				    			Vendor vendor = (Vendor)vendors.get(j);
		    				    			if(vendor.getId()==Globals.a2int(setProduct.getProposedSecondaryManufacturer())){
		    				    				selected = " selected";
		    				    			}
		    				    		%>
						    				    			<option value="<%= vendor.getId() %>" <%= selected %>><%= vendor.getDescription() %></option>
						    				    		<%
						    				    		}
						    				    		%>                               		
                         </select>
                       <% }else{
				    	       			 for(int j=0;j<vendors.size();j++){
			    				    			selected = "";
			    				    			Vendor vendor = (Vendor)vendors.get(j);
			    				    			if(vendor.getId()==Globals.a2int(setProduct.getProposedSecondaryManufacturer())){
			    				    				%>
			    				    				&nbsp;&nbsp;<b><%= vendor.getDescription() %></b><br>
			    				    				<%
			    				    				vendorFound=true;
			    				    				break;
			    				    			}
			    				    		}
				    	        		if(vendorFound==false){
														out.println("&nbsp;&nbsp;<b>N/A</b>"); 				    		
			    				    		}else{
				    				    		vendorFound=false;
				    				    	}
				    	        }
				    	        %>                           
                   			</div></td>
                   		</tr>

                        	<tr>
                    		<td><div align="left">Other<br>
                			 <% if(ableToUpdate){ %>
                			 <select name="PRODUCTLINE_PROPOSED_OTHER_<%= product.getId() %>">
                               		<option value="0">Select One...</option>
		    				    		<%

		    				    		for(int j=0;j<vendors.size();j++){
		    				    			selected = "";
		    				    			Vendor vendor = (Vendor)vendors.get(j);
		    				    			if(vendor.getId()==Globals.a2int(setProduct.getProposedOtherManufacturer())){
		    				    				selected = " selected";
		    				    			}
		    				    		%>
					    				    			<option value="<%= vendor.getId() %>" <%= selected %>><%= vendor.getDescription() %></option>
					    				    		<%
					    				    		}
					    				    		%>                               		
                        </select>
                       <% }else{
				    	       			 for(int j=0;j<vendors.size();j++){
			    				    			selected = "";
			    				    			Vendor vendor = (Vendor)vendors.get(j);
			    				    			if(vendor.getId()==Globals.a2int(setProduct.getProposedOtherManufacturer())){
			    				    				%>
			    				    				&nbsp;&nbsp;<b><%= vendor.getDescription() %></b><br>
			    				    				<%
			    				    				vendorFound=true;
			    				    				break;
			    				    			}
			    				    		}
				    	        		if(vendorFound==false){
														out.println("&nbsp;&nbsp;<b>N/A</b>");    				    		
			    				    		}else{
				    				    		vendorFound=false;
				    				    	}
				    	        }
				    	        %>                          
                   			</div></td>
                   		</tr>
                        	</table>
							<%
							if(i==productLines.size()-1){
							%>
							<input type="hidden" name="PRODUCT_PERIOD" value="<%= product.getPeriodYYYY() %>">					
							
							<%
							}
							%>
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
						<strong>$<%= Money.formatDoubleAsDollarsAndCents(totalSalesTotal) %></strong>
					</td>
				    <td colspan="3">
				    	<strong><div id="totalIntoStockPercentage"><script>changePercentage('intoStock')</script></div></strong>
				    </td>
				</tr>																
		  </table><br>

	      

	      	<hr align="center" width="600" size="1" color="#999999" noshade>
			<p class="heading3">Market Coverage</p>

			<table width="100%" border="0" cellspacing="5" cellpadding="0">
	<tr>
					<td width="18%"><b>County, State:</b></td>
					<td width="4%">&nbsp;</td>
					<td width="78%"><b>% Sold into Each County:</b></td>
					
			</tr>

						
						<%
						for(int i=0;i<12;i++){
							%>
							<tr>
							<td>
							<% if(ableToUpdate){ %>
							<select name="COUNTY_SALES<%= i+1 %>">
							<option value="0">Please Select...</option>
							<%
							}						
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
					<td>&nbsp;</td>
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
						<td>&nbsp;</td>
					    <td colspan="3">
					    	<strong><div id="totalMarketPercentage"><script>changePercentage('market')</script></div></strong>
					    </td>
				</tr>
				<tr>

					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
		 </table>
			<table width="100%" border="0" cellspacing="5" cellpadding="0">
				<tr>
					<td width="40%"><p class="heading3">Key Customers:</p></td>

					<td>
					<% if(ableToUpdate){ %>
					<a href="javascript:newWindow('CustomerBrowse?distId=<%= dist.getId() %>&vcn=<%= acct.getVcn() %>','keyAccts')">Choose Accounts	</a>
					<% } %>
					</td>
				</tr>
		 </table><br>
			<table width="70%" border="0" cellspacing="1" cellpadding="0">
				<tr>
					<th width="5%"><img src="<%= sImagesURL %>deleteIcon.gif" border="0" align="absmiddle" alt="Delete"></th>
					<th width="32%">Name</th>
					<th width="40%">Product Opportunities</th>

					<th width="32%">Potential Eaton $</th>
				</tr>
				<%
				
				if(keyAccounts.size()==0 && ableToUpdate){
				%>
				<tr>
					<td colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="4">
					No Accounts selected.  Click "Choose Accounts" to add Key Customers.	
					</td>
				</tr>
				<%
				}else if(keyAccounts.size()==0){
				%>
				<tr>
					<td colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="4">
					No Accounts selected.	
					</td>
				</tr>
				<%				
				}
				
				for(int i=0;i<keyAccounts.size();i++){
					DistributorKeyAccount keyAcct = (DistributorKeyAccount)keyAccounts.get(i);
					ArrayList productIds = keyAcct.getProductIds();
					String cellShade = null;
					if(i%2==0){
						cellShade=" class=\"cellShade\"";
					}else{
						cellShade="";
					}
				%>
				<tr<%= cellShade %>>
				
					<td>
					<% if(ableToUpdate){ %>
					<input type="checkbox" name="DELETE_KEY_ACCOUNTS" value="<%= keyAcct.getVcn() %>">
					<% } %>
					</td>
					<td><%= keyAcct.getAccountName() %></td>
					<td>
						<div align="left">
						<% if(ableToUpdate){ %>
						<select name="KEY_ACCOUNT_PRODUCTS_<%= keyAcct.getVcn() %>" size="4" multiple>
						<%
						}
						for(int j=0;j<productLines.size();j++){
							Product product = (Product)productLines.get(j);
							selected="";
							StringBuffer productBuffer = new StringBuffer();
							
							for(int k=0;k<productIds.size();k++){
								String productId = (String)productIds.get(k);
								if(productId.equals(product.getId())){
									productBuffer.append(product.getDescription() + "<br>");
									selected = " selected";
									break;
								}
							}
							if(ableToUpdate){
							%>
							<option value="<%= product.getId() %>"<%= selected %>><%= product.getDescription() %></option>
							<%
							}else{
							%>
							<%= productBuffer.toString() %>
							<%
							}
						}
						if(ableToUpdate){ %>
			      </select>
			      <% } %>
				    </div>
				  </td>
					<td><div align="center">
					<%= StringManipulation.createTextBox("KEY_ACCOUNT_POTENTIAL_" + keyAcct.getVcn(),""+keyAcct.getPotentialEatonDollars(),ableToUpdate,"7") %>
					<!--<input name="KEY_ACCOUNT_POTENTIAL_<%= keyAcct.getVcn() %>" type="text" size="7" value="<%= keyAcct.getPotentialEatonDollars() %>">-->
					</div>
					<input type="hidden" name="KEY_ACCOUNTS" value="<%= keyAcct.getVcn() %>">
					</td>
				</tr>
				<% } %>		
		 </table>
		  <br>
			<% if(ableToUpdate){ %>
			<table width="100%" border="0" cellspacing="10" cellpadding="0">
				<tr>
					<td width="47%">
					<p>&nbsp;</p>
					
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
<input type="hidden" name="page" value="saveApp4">
</form>
<p>&nbsp;</p>

  </body>
</html>
