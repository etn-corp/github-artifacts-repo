<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.Globals"%>
<%@ page import="java.text.NumberFormat"%>

<%@ include file="analytics.jsp" %>

<%			
OEMAcctPlanBean OEMAPVars = (OEMAcctPlanBean)request.getAttribute("variables");
request.setAttribute("header", OEMAPVars.getHeader());
ArrayList vendors = (ArrayList)request.getAttribute("vendors");
String totalsProductId = (String) request.getAttribute("totalsProductId");
int selectedVendor=-1;
%>
<HTML>
<%@ include file="./TAPheader.jsp" %>
<%
String genMsg = OEMAPVars.getGenMsg();

Customer cust = OEMAPVars.getCust();
ArrayList products = OEMAPVars.getProducts();
User user = OEMAPVars.getUser();

String show = (String) request.getAttribute("salesOrders");


Account acct = OEMAPVars.getHeader().getAccount();
boolean isUserAbleToUpdate = user.ableToUpdate(acct) || (user.equals(acct.getUserIdAdded()) && acct.isProspect());

%>
		<script language='javascript' src='<%= jsURL %>scripts.js'>
		</script>
		<script>
		function updateTotals (prodObj, sumProdObj, hiddenObj, totalObj1, totalObj2){
    // Products that do not have a grand_total_line2 will update the dummy "null" hidden field
             newValue = removeCurrency(prodObj.value);
             if (!(isNaN(newValue))){
                 summaryIsTotalLine = false;
                 if (sumProdObj.name == totalObj1.name){
                     summaryIsTotalLine = true;
                 }
                 sumProdObj.value = removeCurrency(sumProdObj.value);
                 hiddenObj.value = removeCurrency(hiddenObj.value);
                 totalObj1.value = removeCurrency(totalObj1.value);
                 totalObj2.value = removeCurrency(totalObj2.value);
                 sumProdObj.value = parseFloat(sumProdObj.value) - parseFloat(hiddenObj.value) + parseFloat(newValue);
                 if (!summaryIsTotalLine){
                     totalObj1.value = parseFloat(totalObj1.value) - parseFloat(hiddenObj.value) + parseFloat(newValue);
                 }
                 totalObj2.value = parseFloat(totalObj2.value) - parseFloat(hiddenObj.value) + parseFloat(newValue);
                 hiddenObj.value = parseFloat(newValue);
                 prodObj.value = parseFloat(newValue);
             } else {
                 alert(prodObj.value + " is not a valid number.");
                 prodObj.value = hiddenObj.value;
                 prodObj.select();
                 prodObj.focus();
             }
            
		}

        
		</script>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>

		<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Product Mix</span></p> 
    </td>
  </tr>
</table>

	  <table width="100%" border="0" cellspacing="0" cellpadding="0">
	  	<tr>
			<td width="50%" height="15" align="left">
				&nbsp;&nbsp;&nbsp;&nbsp;<b>TAP Dollars</b> - <a href="javascript:openPopup('TapDollarsExplainPopup.jsp', 'tapDollars', '200', '400')" >What are TAP Dollars?</a>					
			</td>
	  		<td width="50%">
	  		<div align="right">
	  			<%
	  			
	  				String backSort = request.getParameter("backSort");
	  				if ((backSort == null) ||(backSort.length() < 1)) {
	  					
	  					backSort = "customer_name";
	  					
	  				}
	  			
	  			
	  				if ((show == null) || (show.length() < 1)) {
	  					
	  					show = "invoice";
	  					
	  				}
	  				
					if(show.equals("orders")){
					%>
					You are current viewing Order Tap Dollars.  <br>To change to Sales Tap Dollars, please select the following link :<br> <br>
					<a href="OEMAcctPlan?page=productmix&cust=<%=cust.getVistaCustNum()%>&backSort=<%=backSort%>&salesOrders=invoice">Show Sales Tap Dollars</a>
					<% }else{ %>
					You are current viewing Sales Tap Dollars.  <br>To change to Order Tap Dollars, please select the following link : <br><br>
					<a href="OEMAcctPlan?page=productmix&cust=<%=cust.getVistaCustNum()%>&backSort=<%=backSort%>&salesOrders=orders">Show Order Tap Dollars</a>
					<%
					}
	  			%>
	  			</div>
  		 </td>
  		</tr>
  	</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="100%" valign="top"> 
      <p>&nbsp;</p> 
					<% if(genMsg!=null){ %>
					<blockquote><font class="crumbcurrent"><%= genMsg %></font></blockquote>
					<% } %>		      
		<p class="heading2"><%= cust.getName() %></p>
		Salesperson: <%= OEMAPVars.getSalesmanName() %>
		&nbsp;&nbsp;&nbsp;&nbsp; <!-- SD0000002826202 - Assigned value of Salesorder to current value of show in request -->
		<a href="OEMAcctPlan?page=productmix&cust=<%= cust.getVistaCustNum() %>&excel=Y&salesOrders=<%=show%> " target="blank.html">
		Export to Excel</a>
		
		<form action=OEMAcctPlan method=post onSubmit="return validateMix();" name="theForm">
		<!-- These fields are just dummy fields to allow the products without a grand_total_line2 to process 
	     without errors
	-->
	        <input type=hidden name=pot_null value="$0">
	        <input type=hidden name=forecast_null value="$0">
	        <input type=hidden name=comp_null value="$0">
	        <input type=hidden name=comp2_null value="$0">


		<%
		if (request.getParameter("geog") != null) {
			%>
			<input type=hidden name=geog value=<%= request.getParameter("geog") %>>
			<%
		}
		else if (request.getParameter("industry") != null) {
			%>
			<input type=hidden name=industry value=<%= request.getParameter("industry") %>>
			<%
		}
		%>
		
		<input type=hidden name=page value=productmix>
		<input type=hidden name=cust value=<%= cust.getVistaCustNum() %>>
		<input type=hidden name=updateProds value=true>
		<%
		if (request.getParameter("backSort") != null) {
			%>
			<input type=hidden name=backSort value=<%= request.getParameter("backSort") %>>
			<%
		}
		%>

		<div class=smallFontL></div>
		<table class=tableBorder cellspacing=1 cellpadding=1 border=0 width="100%">
			<thead>
				<td class=cellColor colspan=2><div class=prodMixHdr>Product Mix</div></td>

		<%
		int yr = Globals.a2int((String) session.getAttribute("srYear"));
		// If the year is zero, something went wrong... set to current year
		if ( yr == 0 ) {
			Calendar cal = new GregorianCalendar();
		    
		    // Get the components of the date
		    yr = cal.get(Calendar.YEAR);
		}
		
		%>

				<td class=cellColor><div class=prodMixHdr><%= yr-- %></div></td>
				<td class=cellColor><div class=prodMixHdr><%= yr %> YTD</div></td>
				<td class=cellColor><div class=prodMixHdr><%= yr-- %> Total</div></td>
				<td class=cellColor><div class=prodMixHdr><%= yr %> Total</div></td>
				<td class=cellColor><div class=prodMixHdr>Potential</div></td>
				<td class=cellColor><div class=prodMixHdr>Forecast</div></td>
				<td class=cellColor><div class=prodMixHdr>Competitor 1</div></td>
				<td class=cellColor><div class=prodMixHdr>Competitor 2</div></td>
				<td class=cellColor><div class=prodMixHdr>Competitor / Comments</div></td>
			</thead>

		<%
 		double totPotential = 0;
		double totYTD = 0;
		double totLY = 0;
		double totLYTD = 0;
		double tot2LY = 0;
		double totCompetitor = 0;
		double totCompetitor2 = 0;
		double totForecast = 0;
		int totVolume = 0;

		NumberFormat cf = NumberFormat.getCurrencyInstance(Locale.US);
	    cf.setMaximumFractionDigits(0);		
		
		boolean cfFormatEquals1=true;//cf.format(0).equals("$1");
		
		for (int i=0; i < products.size(); i++) {
		    boolean isDetailProductLine = true;
			Product thisProd = (Product)products.get(i);
			CustomerProduct thisCustProd = thisProd.getCustomerProduct();
			String bgcolor = " class=\"cellColor\"";
			String fontType = " class=\"smallFontR\"";
			String disabled = " ";
		    if (!thisProd.getSpLoadTotal().equalsIgnoreCase("L")){
		        bgcolor=" class=\"cellShade\"";
		        // Cannot use "disabled" tag because it will not pass the values to the servlet
		        // So we have to blur upon focus
		        disabled = " onFocus=\"this.blur()\"	 ";
		        isDetailProductLine = false;
		    }
		    if (thisProd.getSpLoadTotal().equalsIgnoreCase("T")){
		        fontType =" class=\"smallFontBR\"";
		    }
			
			%>
			<tr>
			<td valign=top <%= bgcolor %> colspan=2><div <%= fontType %>><%= thisProd.getId() %> - <%= thisProd.getDescription() %></div></td>
			<%
			// Braffet : 20060331 : The only kinds of dollars are now tap dollars.  The check for endMarket, UserDirect, and SSO have been 
			// removed from here and replaced with code that always looks at tap dollars
	
			
			if(cfFormatEquals1){	// clearing cf so all 0's appear
				if (show.equals("orders")) {
				
					%>
					<td valign=top <%= bgcolor %>><div <%= fontType %>><%= cf.format(thisCustProd.getTapDollarsOrderYTD()) %></td>
					<td valign=top <%= bgcolor %>><div <%= fontType %>><%= cf.format(thisCustProd.getTapDollarsOrderLYTD()) %></td>
					<td valign=top <%= bgcolor %>><div <%= fontType %>><%= cf.format(thisCustProd.getTapDollarsOrderLY()) %></td>
					<td valign=top <%= bgcolor %>><div <%= fontType %>><%= cf.format(thisCustProd.getTapDollarsOrder2LY()) %></td>
					<%
				
					totYTD += thisCustProd.getTapDollarsOrderYTD();
					totLY += thisCustProd.getTapDollarsOrderLY();
					totLYTD += thisCustProd.getTapDollarsOrderLYTD();
					tot2LY += thisCustProd.getTapDollarsOrder2LY();
				} else {
					%>
					<td valign=top <%= bgcolor %>><div <%= fontType %>><%= cf.format(thisCustProd.getTapDollarsInvoiceYTD()) %></td>
					<td valign=top <%= bgcolor %>><div <%= fontType %>><%= cf.format(thisCustProd.getTapDollarsInvoiceLYTD()) %></td>
					<td valign=top <%= bgcolor %>><div <%= fontType %>><%= cf.format(thisCustProd.getTapDollarsInvoiceLY()) %></td>
					<td valign=top <%= bgcolor %>><div <%= fontType %>><%= cf.format(thisCustProd.getTapDollarsInvoice2LY()) %></td>
					<%
				
					totYTD += thisCustProd.getTapDollarsInvoiceYTD();
					totLY += thisCustProd.getTapDollarsInvoiceLY();
					totLYTD += thisCustProd.getTapDollarsInvoiceLYTD();
					tot2LY += thisCustProd.getTapDollarsInvoice2LY();					
					
					
				}
				
			} // end if cfFormateEquals1


			if (isUserAbleToUpdate) {
						%>
						<td valign=top <%= bgcolor %>><input name=pot_<%= thisProd.getId() %> <%= disabled %> value='<%= thisCustProd.displayPotentialDollars() %>' size=10 maxlength="10" <%= fontType %> onChange="modElement();updateTotals(this, pot_<%= thisProd.getSummaryProductId() %>, pot_hidden_<%= thisProd.getId() %>, pot_<%= thisProd.getGrandTotalId1() %>, pot_<%= thisProd.getGrandTotalId2() %>)">
						     <input type=hidden name=pot_hidden_<%= thisProd.getId() %> value='<%= thisCustProd.displayPotentialDollars() %>'>
						</td>
						<%
			}
			else {
			    if(cfFormatEquals1);	// clearing cf so all 0's appear
				%>
				<td valign=top <%= bgcolor %>><div <%= fontType %>><%= cf.format(thisCustProd.getPotentialDollars()) %></div></td>
				<%
			}

			if (isUserAbleToUpdate) {
				%>
				
				        
						<td valign=top <%= bgcolor %>><input name=forecast_<%= thisProd.getId() %> <%= disabled %> value='<%= thisCustProd.displayForecastDollars() %>' size=10 maxlength="10" <%= fontType %> onChange="modElement();updateTotals(this, forecast_<%= thisProd.getSummaryProductId() %>, forecast_hidden_<%= thisProd.getId() %>, forecast_<%= thisProd.getGrandTotalId1() %>, forecast_<%= thisProd.getGrandTotalId2() %>)">
						<input type=hidden name=forecast_hidden_<%= thisProd.getId() %> value='<%= thisCustProd.displayForecastDollars() %>'>
						</td>
						<td valign=top <%= bgcolor %>>
						<% if (isDetailProductLine){ %>
   				    <select name="comp_name_<%= thisProd.getId() %>" <%= disabled %>>
		    				    		<option value="0"></option>
		    				    		<%
		    				    		selectedVendor=thisCustProd.getCompetitorName();
		    				    		for(int j=0;j<vendors.size();j++){
		    				    			Vendor vendor = (Vendor)vendors.get(j);
		    				    			if(selectedVendor==vendor.getId()){
		    				    		%>
		    				    			<option value="<%= vendor.getId() %>" selected><%= vendor.getDescription() %></option>
		    				    		<%
		    				    			}else{
		    				    		%>
		    				    			<option value="<%= vendor.getId() %>"><%= vendor.getDescription() %></option>
		    				    		<%		    				    			
		    				    			}
		    				    		}
		    				    		%>
				    	</select>
				    	<% } %>
						<input name=comp_<%= thisProd.getId() %> <%= disabled %> value='<%= thisCustProd.displayCompetitorDollars() %>' size=10 maxlength="10" <%= fontType %> onChange="modElement();updateTotals(this, comp_<%= thisProd.getSummaryProductId() %>, comp_hidden_<%= thisProd.getId() %>, comp_<%= thisProd.getGrandTotalId1() %>, comp_<%= thisProd.getGrandTotalId2() %>)">
						<input type=hidden name=comp_hidden_<%= thisProd.getId() %> value='<%= thisCustProd.displayCompetitorDollars() %>'>
						</td>
						<td valign=top <%= bgcolor %>>
						<% if (isDetailProductLine) { %>
   				    <select name="comp2_name_<%= thisProd.getId() %>" <%= disabled %>>
		    				    		<option value="0"></option>
		    				    		<%
		    				    		selectedVendor=thisCustProd.getCompetitor2Name();
		    				    		for(int j=0;j<vendors.size();j++){
		    				    			Vendor vendor = (Vendor)vendors.get(j);
		    				    			if(selectedVendor==vendor.getId()){
		    				    		%>
		    				    			<option value="<%= vendor.getId() %>" selected><%= vendor.getDescription() %></option>
		    				    		<%
		    				    			}else{
		    				    		%>
		    				    			<option value="<%= vendor.getId() %>"><%= vendor.getDescription() %></option>
		    				    		<%		    				    			
		    				    			}
		    				    		}
		    				    		%>
				    	</select>
				    	<% } %>
						<input name=comp2_<%= thisProd.getId() %> <%= disabled %> value='<%= thisCustProd.displayCompetitor2Dollars() %>' size=10 maxlength="10" <%= fontType %> onChange="modElement();updateTotals(this, comp2_<%= thisProd.getSummaryProductId() %>, comp2_hidden_<%= thisProd.getId() %>, comp2_<%= thisProd.getGrandTotalId1() %>, comp2_<%= thisProd.getGrandTotalId2() %>)">
						<input type=hidden name=comp2_hidden_<%= thisProd.getId() %> value='<%= thisCustProd.displayCompetitor2Dollars() %>'>
						</td>
						<td valign=top <%= bgcolor %>><textarea name=comment_<%= thisProd.getId() %> rows=2 cols=35 wrap class=smallFontL onChange="modElement()"><%= thisCustProd.getNotes() %></textarea></td>
				<%
			}
			else {
			    if(cfFormatEquals1){	// clearing cf so all 0's appear
				    
				%>
					<td valign=top <%= bgcolor %>>
						<div <%= fontType %>><%= cf.format(thisCustProd.getForecastDollars()) %>
						</div>
					</td>
					<td valign=top <%= bgcolor %>>
						<div <%= fontType %>>
						<%
						if(thisCustProd.getCompetitorName()!=0){
								selectedVendor=thisCustProd.getCompetitorName();
				    		for(int j=0;j<vendors.size();j++){
				    			Vendor vendor = (Vendor)vendors.get(j);
				    			if(selectedVendor==vendor.getId()){
					    			out.println(vendor.getDescription() + "<br>");
					    			break;
				    			}
				    		}
						}
						%>
						<%= cf.format(thisCustProd.getCompetitorDollars()) %>
						</div>
					</td>
					<td valign=top <%= bgcolor %>>
					<div <%= fontType %>>
						<%
						if(thisCustProd.getCompetitor2Name()!=0){
								selectedVendor=thisCustProd.getCompetitor2Name();
				    		for(int j=0;j<vendors.size();j++){
				    			Vendor vendor = (Vendor)vendors.get(j);
				    			if(selectedVendor==vendor.getId()){
					    			out.println(vendor.getDescription() + "<br>");
					    			break;
				    			}
				    		}
						}
						%>
						<%= cf.format(thisCustProd.getCompetitor2Dollars()) %>
					</div>
					</td>
				<%
				}
			    
			        %>
			        <td valign=top <%= bgcolor %>><div class=smallFontBL><%= thisCustProd.getNotes() %></div></td>
			  <%
			   
			}

			%>
				</tr>
			<%
			int psCnt = 0;
			String psListing = "";
			// Removed code for product sublines

			%>
			<input type=hidden name=<%= thisProd.getId() %>_sublist value=<%= psListing %>>
			<%
			totPotential += thisCustProd.getPotentialDollars();
			totCompetitor += thisCustProd.getCompetitorDollars();
			totCompetitor2 += thisCustProd.getCompetitor2Dollars();
			totForecast += thisCustProd.getForecastDollars();


		}
		%>				

		
		</table>

		<%
		if (isUserAbleToUpdate) {
			%>
			<br><input type="image" src="<%= sImagesURL %>button_save.gif" width="70" height="20">
			<%
		}
		%>

		</form>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		</td></tr>
		</table>
  </BODY>
</HTML>
