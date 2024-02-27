<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="java.math.*"%>
<%@ page import="java.text.*"%>
<%@ include file="analytics.jsp" %>
<%

OEMAcctPlanBean OEMAPVars = (OEMAcctPlanBean)request.getAttribute("variables");
SalesGroup sGroup = OEMAPVars.getSalesGroup();
User user = OEMAPVars.getUser();

Customer thisCust = OEMAPVars.getCust();
%>
<HTML>
		<%@ include file="./TAPheader.jsp" %>
		

		<div class=pageHeader>Product Sample Request Form</div>
		<div class=centerOnly><%= thisCust.getName() %></div>
		<form name=samplesForm action=OEMAcctPlan>
		<input type=hidden name=groupId value=<%= sGroup.getId() %>>
		<input type=hidden name=cust value=<%= thisCust.getVistaCustNum() %>>
		<input type=hidden name=page value=sendSample>
		<%
		if (request.getParameter("backSort") != null) {
			%>
			<input type=hidden name=backSort value=<%= request.getParameter("backSort") %>>
			<%
		}

		User pm = OEMAPVars.getPricingManager();

		if (pm.getUserid() != null && pm.getUserid().length() > 0) {
			%>
			<br>Pricing Manager: <%= pm.getFirstName() %> <%= pm.getLastName() %>
			<input type=hidden name=pmid value=<%= pm.getUserid() %>>
			<%
		}
		else {
			%>
			<br>There is a problem getting the pricing manager for this customer. Please contact IT support for assistance.
                  
      <%
		}
		%>
		<br>Salesperson: <%= thisCust.getSalesmanName() %>
		<br>Why are you requesting samples to be sent to this customer?:
		<br><textarea name=use rows=5 cols=50 wrap></textarea>
		<input type=hidden name=stage value=<%= thisCust.getStage() %>>
		
		<br>Who is the contact at this customer?
		<select name=contactId>
		<%
		ArrayList contacts = (ArrayList)request.getAttribute("contacts");
		for (int i=0; i < contacts.size(); i++) {
			Contact cont = (Contact)contacts.get(i);
			%>
			<option value=<%= cont.getId() %>><%= cont.getLastName() %>, <%= cont.getFirstName() %></option>
				<%
		}
		%>
		</select>
		<br>If the contact is an Eaton Electrical employee, enter their name here: 
		<input name=contactOverride><br>
		<%
		

		ArrayList products = OEMAPVars.getProducts();
		
		//if(products==null){
		//System.out.println("products is null");
		//}

		float totPot = 0;
		float totAct =  0;
		float totCompet =  0;
		float totForecast =  0;

		for (int i=0; i < products.size(); i++) {
			Product thisProd = (Product)products.get(i);

			CustomerProduct cProd =
				thisProd.getCustomerProduct();

			totPot += cProd.getPotentialDollars();
			totCompet += cProd.getCompetitorDollars();
			totForecast += cProd.getForecastDollars();
		}
%>
		<input type=hidden name=potDol value=<%= totPot %>>
		<input type=hidden name=actDol value=<%= totAct %>>
		<input type=hidden name=competDol value=<%= totCompet %>>
		<input type=hidden name=forecastDol value=<%= totForecast %>>

		<table>
			<thead>
				<td class=columnHeader>Qty</td>
				<td class=columnHeader>Catalog Num</td>
				<td class=columnHeader>Qty</td>
				<td class=columnHeader>Catalog Num</td>
			</thead>
			<tr>
				<td><input name=qty1 size=5></td>
				<td><input name=cat1 size=35 maxlength=35></td>
				<td><input name=qty2 size=5></td>
				<td><input name=cat2 size=35 maxlength=35></td>
			</tr>
			<tr>
				<td><input name=qty3 size=5></td>
				<td><input name=cat3 size=35 maxlength=35></td>
				<td><input name=qty4 size=5></td>
				<td><input name=cat4 size=35 maxlength=35></td>
			</tr>
			<tr>
				<td><input name=qty5 size=5></td>
				<td><input name=cat5 size=35 maxlength=35></td>
				<td><input name=qty6 size=5></td>
				<td><input name=cat6 size=35 maxlength=35></td>
			</tr>
			<tr>
				<td><input name=qty7 size=5></td>
				<td><input name=cat7 size=35 maxlength=35></td>
				<td><input name=qty8 size=5></td>
				<td><input name=cat8 size=35 maxlength=35></td>
			</tr>
		</table>

		<table>
			<caption>Ship To</caption>
			<tr>
				<td>Attention</td>
				<td><input name=attn size=35 maxlength=50></td>
			</tr>
			<tr>
				<td>Address 1</td>
				<td><input name=addr1 size=35 maxlength=35></td>
			</tr>
			<tr>
				<td>Address 2</td>
				<td><input name=addr2 size=35 maxlength=35></td>
			</tr>
			<tr>
				<td>Address 3</td>
				<td><input name=addr3 size=35 maxlength=35></td>
			</tr>
			<tr>
				<td>City/State/Zip</td>
				<td><input name=city size=25 maxlength=25>&nbsp;
				<input name=state size=2 maxlength=2>&nbsp;
				<input name=zip size=10 maxlength=10></td>
			</tr>
		</table>

		Ship by: Best Way <input type=radio name=shipby value=best checked>
		 Air <input type=radio name=shipby value=air>
		<br>
		Carrier and Account number to charge Air Freight shipments
		<input name=acct>
		<input type=submit name=submit value='Place Request'>

		</form>
	
	
		
</BODY>
</HTML>