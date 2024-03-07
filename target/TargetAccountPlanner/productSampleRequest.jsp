<%@ page language="java" errorPage="SMRCErrorPage.jsp"%>
<%@ include file="analytics.jsp" %>
<%! 
	public String shipToSelect(String shipTo, String aVal) {
		if ( shipTo.equalsIgnoreCase( aVal ) ) {
			return "SELECTED ";
		}
		return "";
	}

	public String shipByCheck(String shipBy, String aVal) {
		if ( shipBy.equalsIgnoreCase( aVal ) ) {
			return "CHECKED ";
		} else if ( shipBy.equals("") &&
		            aVal.equalsIgnoreCase( "best" ) ) {
			return "CHECKED ";
		}
		return "";
	}
%>

<HTML>
<%@ include file="./SMRCHeader.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

	Account account = header.getAccount();
	User user = header.getUser();
	Customer customer = (Customer)request.getAttribute("customer");
	User salesEngineerUser = ( User )request.getAttribute( "salesEngineerUser" );
	
	// These are used when the page gets refreshed
	String use = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "use" ) );
	String contactId = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "contactId" ) );
	String contactOverride = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "contactOverride" ) );
	String qty1 = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "qty1" ) );
	String cat1 = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "cat1" ) );
	String qty2 = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "qty2" ) );
	String cat2 = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "cat2" ) );
	String qty3 = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "qty3" ) );
	String cat3 = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "cat3" ) );
	String qty4 = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "qty4" ) );
	String cat4 = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "cat4" ) );
	String qty5 = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "qty5" ) );
	String cat5 = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "cat5" ) );
	String qty6 = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "qty6" ) );
	String cat6 = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "cat6" ) );
	String shipTo = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "shipTo" ) );
	String attn = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "attn" ) );
	String addr1 = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "addr1" ) );
	String addr2 = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "addr2" ) );
	String addr3 = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "addr3" ) );
	String city = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "city" ) );
	String state = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "state" ) );
	String zip = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "zip" ) );
	String shipby = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "shipby" ) );
	String acct = com.eaton.electrical.smrc.util.StringManipulation.noNull( ( String )request.getAttribute( "acct" ) );
%>

	<script language="javascript" src="<%= jsURL %>validation/productSampleRequest.js"></script>
	<script language="javascript">
		function newWindow(url, windowName) {
			catWindow = window.open(url, windowName, config='width=650,height=475,location=no,scrollbars=yes,toolbar=no,resizeable=yes,status=1')
		}
		
		function changeShipTo( control ) {
    		var i = control.selectedIndex;
    		
    		if ( i == 1 ) {
<%
	Address acctAddress = account.getBusinessAddress();
%>    		
				var contId = document.theform.contactId.selectedIndex;
				// This Account
				if ( contId > 0 ) {
	    			document.theform.attn.value = eval("document.theform.cont_" + contId + ".value");
				} else {
	    			document.theform.attn.value = "";
	    		}
    			document.theform.addr1.value = "<%=acctAddress.getAddress1()%>";
    			document.theform.addr2.value = "<%=acctAddress.getAddress2()%>";
    			document.theform.addr3.value = "<%=acctAddress.getAddress3()%>";
    			document.theform.city.value = "<%=acctAddress.getCity()%>";
    			document.theform.state.value = "<%=acctAddress.getState()%>";
    			document.theform.zip.value = "<%=acctAddress.getZip()%>";
    		} else if ( i == 2 ) {
<%
	if ( salesEngineerUser != null &&
		 ( salesEngineerUser.getUserid() != null &&
		   salesEngineerUser.getUserid().trim() != "" ) ) {
		Address seAddress = EnterpriseDirectoryDAO.getAddressForUser( salesEngineerUser.getUserid() );
%>    		
				// Sales Engineer
    			document.theform.attn.value = "<%=salesEngineerUser.getFirstName()%> <%=salesEngineerUser.getLastName()%>";
    			document.theform.addr1.value = "<%=seAddress.getAddress1()%>";
    			document.theform.addr2.value = "<%=seAddress.getAddress2()%>";
    			document.theform.addr3.value = "<%=seAddress.getAddress3()%>";
    			document.theform.city.value = "<%=seAddress.getCity()%>";
    			document.theform.state.value = "<%=seAddress.getState()%>";
    			document.theform.zip.value = "<%=seAddress.getZip()%>";
<%
	} else {
%>
    			document.theform.attn.value = "";
    			document.theform.addr1.value = "";
    			document.theform.addr2.value = "";
    			document.theform.addr3.value = "";
    			document.theform.city.value = "";
    			document.theform.state.value = "";
    			document.theform.zip.value = "";
<%
	}
%>
    		} else if ( i == 3 ) {
<%
	Address myAddress = EnterpriseDirectoryDAO.getAddressForUser( user.getUserid() );
%>    		
				// Person Making Request
    			document.theform.attn.value = "<%=user.getFirstName()%> <%=user.getLastName()%>";
    			document.theform.addr1.value = "<%=myAddress.getAddress1()%>";
    			document.theform.addr2.value = "<%=myAddress.getAddress2()%>";
    			document.theform.addr3.value = "<%=myAddress.getAddress3()%>";
    			document.theform.city.value = "<%=myAddress.getCity()%>";
    			document.theform.state.value = "<%=myAddress.getState()%>";
    			document.theform.zip.value = "<%=myAddress.getZip()%>";

    		} else if ( i == 0 || i == 4 ) {
    			document.theform.attn.value = "";
    			document.theform.addr1.value = "";
    			document.theform.addr2.value = "";
    			document.theform.addr3.value = "";
    			document.theform.city.value = "";
    			document.theform.state.value = "";
    			document.theform.zip.value = "";
    		}
    		
			return true;
		}
	</script>

	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<TR>
			<TD width="10">&nbsp;</TD>
			<TD width="750">
				<p><br>
					<a class="crumb" href="SMRCHome">Home Page</a>
					<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Product Sample Request</span></p>
			</TD>
		</TR>
	</table>

	<form name="theform" method="POST" action="ProductSampleRequest" onSubmit="javascript:return formValidation();">

		<table width="760" border="0" cellspacing="0" cellpadding="0">
			<TR>
				<TD width="10">&nbsp;</TD>
				<TD align="left" width="750">
					<div class="heading2">Sample Request Form</div>
					<div class="heading3"><%= customer.getName() %></div>
					<BR>
				</TD>
			</TR>
			<TR>
				<TD width="10">&nbsp;</TD>
				<TD width="750">
					Salesperson: <%= customer.getSalesmanName() == null || customer.getSalesmanName().trim().length() == 0 ? "Unspecified" : customer.getSalesmanName()%>
					<BR><BR>
					Why are you requesting samples to be sent to this customer?: <BR>
					<TEXTAREA NAME="use" ROWS="5" COLS="50"><%=use%></TEXTAREA> 
					<INPUT TYPE="hidden" NAME="stage" VALUE="<%= customer.getStage() %>"> 
					<BR>
					<BR>
			
					<TABLE WIDTH="100%" BORDER="0" CELLSPACING="0" CELLPADDING="0">
						<TR>
							<TD>Select the contact at this customer to receive this sample: 
								<SELECT NAME="contactId">
									<OPTION VALUE="">choose...</OPTION>
<%
	ArrayList contacts = ( ArrayList )request.getAttribute("contacts");
	
	String contactSelected = "";
			
	for (int i=0; i < contacts.size(); i++) {
		Contact cont = (Contact)contacts.get(i);
		if ( contactId.equals( new Integer( cont.getId() ).toString() ) ) {
			contactSelected = " SELECTED";
		} else {
			contactSelected = "";
		}
%>
									<OPTION<%=contactSelected%> VALUE="<%= cont.getId() %>"><%= cont.getLastName() %>, <%= cont.getFirstName() %></OPTION>
<%
	}
%>
								</SELECT> 
<%
	for (int i=0; i < contacts.size(); i++) {
		Contact cont = (Contact)contacts.get(i);
%>
<input type="hidden" name="cont_<%=i+1%>" value="<%= cont.getFirstName() %> <%=cont.getLastName() %>">
<%
	}
%>
<% 
	//Vince: hardcode for now in case only certain people can update.
	boolean ableToUpdate = true;
			
	if ( ableToUpdate ) {
%> 
								<BR>
								<A HREF="javascript:newWindow('Contacts?page=add&acctId=<%= request.getParameter("acctId") %>','contacts', 650, 470)"><IMG SRC="<%= sImagesURL %>button_add_contact.gif" WIDTH="70" HEIGHT="20" BORDER="0" ALT=""></A> 
<%
	} 
%>
							</TD>
						</TR>
						<TR>
							<TD>
								<BR>
								If the contact is an Eaton employee, enter their name here: 
								<INPUT NAME="contactOverride" value="<%=contactOverride%>">
							</TD>
						</TR>
					</TABLE>
				</TD>
			</TR>
		<BR>
		<BR>
<%
	ArrayList products = (ArrayList)request.getAttribute("products");
		
	if ( products==null ) {
		products = new ArrayList();
	}
	float totPot = 0;
	float totAct =  0;
	float totCompet =  0;
	float totForecast =  0;

	for (int i=0; i < products.size(); i++) {
		Product thisProd = (Product)products.get(i);

		CustomerProduct cProd = thisProd.getCustomerProduct();

		totPot += cProd.getPotentialDollars();
		totCompet += cProd.getCompetitorDollars();
		totForecast += cProd.getForecastDollars();
	}
%>
			<INPUT TYPE="hidden" NAME="potDol" VALUE="<%= totPot %>">
			<INPUT TYPE="hidden" NAME="actDol" VALUE="<%= totAct %>">
			<INPUT TYPE="hidden" NAME="competDol" VALUE="<%= totCompet %>">
			<INPUT TYPE="hidden" NAME="forecastDol" VALUE="<%= totForecast %>">

			<TR>
				<TD width="10">&nbsp;</TD>
				<TD width="750">
					<BR>
					<TABLE>
						<TR>
							<TD>Qty</TD>
							<TD>Catalog Num</TD>
							<TD>Qty</TD>
							<TD>Catalog Num</TD>
						</TR>
						<TR>
							<TD><INPUT NAME="qty1" VALUE="<%=qty1%>" SIZE="5"></TD>
							<TD><INPUT NAME="cat1" VALUE="<%=cat1%>" SIZE="35" MAXLENGTH="35"></TD>
							<TD><INPUT NAME="qty2" VALUE="<%=qty2%>" SIZE="5"></TD>
							<TD><INPUT NAME="cat2" VALUE="<%=cat2%>" SIZE="35" MAXLENGTH="35"></TD>
						</TR>
						<TR>
							<TD><INPUT NAME="qty3" VALUE="<%=qty3%>" SIZE="5"></TD>
							<TD><INPUT NAME="cat3" VALUE="<%=cat3%>" SIZE="35" MAXLENGTH="35"></TD>
							<TD><INPUT NAME="qty4" VALUE="<%=qty4%>" SIZE="5"></TD>
							<TD><INPUT NAME="cat4" VALUE="<%=cat4%>" SIZE="35" MAXLENGTH="35"></TD>
						</TR>
						<TR>
							<TD><INPUT NAME="qty5" VALUE="<%=qty5%>" SIZE="5"></TD>
							<TD><INPUT NAME="cat5" VALUE="<%=cat5%>" SIZE="35" MAXLENGTH="35"></TD>
							<TD><INPUT NAME="qty6" VALUE="<%=qty6%>" SIZE="5"></TD>
							<TD><INPUT NAME="cat6" VALUE="<%=cat6%>" SIZE="35" MAXLENGTH="35"></TD>
						</TR>
					</TABLE>
				</TD>
			</TR>
			<TR>
				<TD width="10">&nbsp;</TD>
				<TD width="750">
					<BR>
					<TABLE>
						<TR>
							<TD>
								Ship to<BR>
								<SELECT NAME="shipTo" onChange="javascript:return changeShipTo( this );">
									<OPTION <%=shipToSelect(shipTo,"")%>VALUE="">Choose...</OPTION>			
									<OPTION <%=shipToSelect(shipTo,"This Account")%>VALUE="This Account">This Account</OPTION>
									<OPTION <%=shipToSelect(shipTo,"Sales Engineer")%>VALUE="Sales Engineer">Sales Engineer</OPTION>
									<OPTION <%=shipToSelect(shipTo,"Person Making Request")%>VALUE="Person Making Request">Person making request</OPTION>
									<OPTION <%=shipToSelect(shipTo,"Other")%>VALUE="Other">Other</OPTION>
								</SELECT>
							</TD>
							<TD>&nbsp;</TD>
							<TD>Attention</TD>
							<TD><INPUT NAME="attn" VALUE="<%=attn%>" SIZE="35" MAXLENGTH="50"></TD>
						</TR>
						<TR>
							<TD>&nbsp;</TD>
							<TD>&nbsp;</TD>
							<TD>Address 1</TD>
							<TD><INPUT NAME="addr1" VALUE="<%=addr1%>" SIZE="35" MAXLENGTH="35"></TD>
						</TR>
						<TR>
							<TD>&nbsp;</TD>
							<TD>&nbsp;</TD>
							<TD>Address 2</TD>
							<TD><INPUT NAME="addr2" VALUE="<%=addr2%>" SIZE="35" MAXLENGTH="35"></TD>
						</TR>
						<TR>
							<TD>&nbsp;</TD>
							<TD>&nbsp;</TD>
							<TD>Address 3</TD>
							<TD><INPUT NAME="addr3" VALUE="<%=addr3%>" SIZE="35" MAXLENGTH="35"></TD>
						</TR>
						<TR>
							<TD>&nbsp;</TD>
							<TD>&nbsp;</TD>
							<TD>City/State/Zip</TD>
							<TD><INPUT NAME="city" VALUE="<%=city%>" SIZE="25" MAXLENGTH="25">&nbsp; <INPUT NAME="state" VALUE="<%=state%>" SIZE="2" MAXLENGTH="2">&nbsp; <INPUT NAME="zip" VALUE="<%=zip%>" SIZE="10" MAXLENGTH="10"></TD>
						</TR>
					</TABLE>
				</TD>
			</TR>
			<TR>
				<TD width="10">&nbsp;</TD>
				<TD width="750">
					<BR>
					Ship by: Best Way
					<INPUT TYPE="radio" NAME="shipby" <%=shipByCheck( shipby, "best") %>VALUE="best">
					Air
					<INPUT TYPE="radio" NAME="shipby" <%=shipByCheck( shipby, "air") %>VALUE="air">
					<BR>
					Carrier and Account number to charge Air Freight shipments
					<INPUT NAME="acct" VALUE="<%=acct%>">
				</TD>	
			</TR>	
			<TR>
				<TD width="10">&nbsp;</TD>
				<TD width="750">
					<BR>
					<INPUT TYPE="submit" NAME="button_submit" VALUE='Place Request'>
					<BR>
				</TD>	
			</TR>	
		</TABLE>
		<input type="hidden" name="acctId" value="<%= customer.getVistaCustNum() %>">
		<input type="hidden" name="page" value="sendSample">
	</FORM>
</HTML>
