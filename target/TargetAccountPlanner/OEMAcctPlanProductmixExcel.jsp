<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.Globals"%>
<%@ page contentType="application/msexcel"%>

<%			
response.setContentType("text/txt");
response.setHeader("Content-Disposition", "attachment; filename=\"tapreport.xls\"");
OEMAcctPlanBean OEMAPVars = (OEMAcctPlanBean)request.getAttribute("variables");
request.setAttribute("header", OEMAPVars.getHeader());
 //SD0000002826202 - retrieve value of salesOrders  from request
String show = (String) request.getAttribute("salesOrders");
ArrayList vendors = (ArrayList)request.getAttribute("vendors");
int selectedVendor=-1;
%>
<HTML>
<%
ArrayList products = OEMAPVars.getProducts();

%>
		
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="100%" valign="top"> 
            
		<table cellspacing=1 cellpadding=1 border=0 width="100%">
			<thead>
				<td colspan=2>Product Line</td>

		<%
		int yr = Globals.a2int((String) session.getAttribute("srYear"));
		// If the year is zero, something went wrong... set to current year
		if ( yr == 0 ) {
			Calendar cal = new GregorianCalendar();
		    
		    // Get the components of the date
		    yr = cal.get(Calendar.YEAR);
		}
		%>

				<td><%= yr-- %></td>
				<td><%= yr %> YTD</td>
				<td><%= yr-- %> Total</td>
				<td><%= yr %> Total</td>
				<td>Potential</td>
				<td>Forecast</td>
				<td>Competitor 1</td>
				<td>C1 Sales</td>
				<td>Competitor 2</td>
				<td>C2 Sales </td>
				<td>Competitor / Comments</td>
			</thead>

		<%
 			
		for (int i=0; i < products.size(); i++) {
		    Product thisProd = (Product)products.get(i);
			CustomerProduct thisCustProd = thisProd.getCustomerProduct();
			if (show.equals("orders")) {	// SD0000002826202 -- conditionally assigning values of TapDollers/SalesDollers here		
			%>
			<tr>
			<td valign=top colspan=2><%= thisProd.getId() %> - <%= thisProd.getDescription() %></td>
			<td valign=top ><%= thisCustProd.getTapDollarsOrderYTD() %></td>
			<td valign=top ><%= thisCustProd.getTapDollarsOrderLYTD() %></td>
			<td valign=top ><%= thisCustProd.getTapDollarsOrderLY() %></td>
			<td valign=top ><%= thisCustProd.getTapDollarsOrder2LY() %></td>
             <!-- SD0000002826202 - Start here - Conditional TapDollers/SalesDollers to be assigned --> 
             <% }else { %>             
            	<tr>
     			<td valign=top colspan=2><%= thisProd.getId() %> - <%= thisProd.getDescription() %></td>
     			<td valign=top><%= thisCustProd.getTapDollarsInvoiceYTD() %></td>
     			<td valign=top><%= thisCustProd.getTapDollarsInvoiceLYTD() %></td>
     			<td valign=top><%= thisCustProd.getTapDollarsInvoiceLY() %></td>
     			<td valign=top><%= thisCustProd.getTapDollarsInvoice2LY() %></td>
     			     			
             <% }  %>      
             <!-- SD0000002826202 - End here  -->            

				<td valign=top ><%= thisCustProd.getPotentialDollars() %></td>
		    	<td valign=top >
						<%= thisCustProd.getForecastDollars() %>
					</td>
					<td valign=top>
						<%
						if(thisCustProd.getCompetitorName()!=0){
								selectedVendor=thisCustProd.getCompetitorName();
								for(int j=0;j<vendors.size();j++){
					    			Vendor vendor = (Vendor)vendors.get(j);
					    			if(selectedVendor==vendor.getId()){
				        %>
						    			<%= vendor.getDescription() %>
				        <%
						    			break;
					    			}
					    		}
				    	}
						%>
				    </td>
				    <td>
						<%= thisCustProd.getCompetitorDollars() %>
					</td>
					<td valign=top >
						<%
						if(thisCustProd.getCompetitor2Name()!=0){
								selectedVendor=thisCustProd.getCompetitor2Name();
								for(int j=0;j<vendors.size();j++){
					    			Vendor vendor = (Vendor)vendors.get(j);
					    			if(selectedVendor==vendor.getId()){
					    %>
						    			<%= vendor.getDescription() %>
				        <%
						    			break;
					    			}
					    		}
				    	}
						%>
					</td>
					<td>
						<%= thisCustProd.getCompetitor2Dollars() %>
					</td>
				
			        <td valign=top ><%= thisCustProd.getNotes() %></td>
			
				</tr>
			<%
			
		}
		%>				

		
		</table>

		</td></tr>
		</table>
  </BODY>
</HTML>	