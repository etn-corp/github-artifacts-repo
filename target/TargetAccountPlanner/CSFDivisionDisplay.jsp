<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*, java.text.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ include file="analytics.jsp" %>

<%
Division division = (Division)request.getAttribute("division");
ArrayList csfs = (ArrayList)request.getAttribute("csfs");
String saveMsg = (String)request.getAttribute("saveMsg");
String salesOrders = (String) request.getAttribute("salesOrders");
ArrayList reportBeans = (ArrayList) request.getAttribute("reportBeans");
ArrayList geogs = (ArrayList) request.getAttribute("geogs");

%>
<html>
<script>
	function submitColorUpdate(csfId, district,color){
        UpdateColor.CSF_ID.value = csfId;
        UpdateColor.DISTRICT.value = district;
        UpdateColor.COLOR.value = color;
        UpdateColor.submit();
    }
</script>
<%@ include file="./SMRCHeader.jsp" %>

<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>

		<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Division Critical Success Factors</span>
      </p> 
    </td>
  </tr>
</table>

<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
   <td width="750" valign="top"> 
      <p>&nbsp;</p>
			<% if(saveMsg.length()!=0){ %>
						<blockquote><font class="crumbcurrent"><%= saveMsg %></font></blockquote>
			<% } %>      
      <p class="heading2"><%= division.getName() %> Critical Success Factors</p>
	  <table width="100%" border="0" cellspacing="1" cellpadding="0">
	  <tr>
	  	<td height="20" colspan="4" >&nbsp;</td>
  	   </tr>

  	   <tr>
			<td colspan="5">
		<b>TAP Dollars</b> - <a href="javascript:openPopup('TapDollarsExplainPopup.jsp', 'tapDollars', '200', '400')" >What are TAP Dollars?</a>
	  <table width="100%" border="0" cellspacing="0" cellpadding="0">
	  	<tr>
	  		<td width="60%">&nbsp;</td>
	  		<td width="40%">
	  		<div align="right">
	  			<%
	  				String show = (String)request.getAttribute("salesOrders");
	  				if ((show == null) || (show.length() < 1)) {
	  					
	  					show = "invoice";
	  					
	  				}
	  				
					if(show.equals("orders")){
					%>
					You are current viewing Order Tap Dollars.  <br>To change to Sales Tap Dollars, please select the following link :<br> <br>
					<a href="CSF?page=Division&divisionId=<%= division.getId() %>&salesOrders=invoice">Show Sales Tap Dollars</a>
					<% }else{ %>
					You are current viewing Sales Tap Dollars.  <br>To change to Order Tap Dollars, please select the following link : <br><br>
					<a href="CSF?page=Division&divisionId=<%= division.getId() %>&salesOrders=orders">Show Order Tap Dollars</a>
					<%
					}
	  			%>
	  			</div>
	  			<br>
  		 </td>
  		</tr>
  	</table>			
			</td>  	   
  	   </tr>   
	  <tr>
  		  <th width="17%">District</th>
	  		<th width="17%">YTD <%= salesOrders %></th>
	  		<th width="17%">Prev YTD <%= salesOrders %></th>
	  		<th width="17%">% Growth</th>
	   </tr>
	  	
	  <%
	  		NumberFormat nf = NumberFormat.getCurrencyInstance();
	  		NumberFormat pf = NumberFormat.getPercentInstance();
	  		nf.setMaximumFractionDigits(0);
	  		pf.setMaximumFractionDigits(0);
	  		
	  		double grandTotalYTDSales = 0;
	  		double grandTotalPrevYTDSales = 0;
	  		double zoneYTDSales = 0;
	  		double zonePrevYTDSales = 0;
	  		double zoneTotalGrowthPercentage = 0;
	  		
	  		String currZone = "";
	  		Geography zone = new Geography();
			for (int i=0; i < geogs.size(); i++) {
				Geography geog = (Geography)geogs.get(i);
				boolean resultsFound = false;
				if ((geog.getGeog().substring(4,5)).equalsIgnoreCase("0")){
					if (i > 0){
						zoneTotalGrowthPercentage = 99999;
						if (zonePrevYTDSales != 0){
  							zoneTotalGrowthPercentage = ((zoneYTDSales - zonePrevYTDSales) / zonePrevYTDSales);
  						}
  					
	  	%>
		  				<tr class="cellShade">
							<td width="17%">
			
							<div align="center"><strong><%= zone.getDescription() %></strong>:</div>
							</td>
							<td><div align="right"><strong><%= nf.format(zoneYTDSales) %></strong></div>
							</td>
					  		<td><div align="right"><strong><%= nf.format(zonePrevYTDSales) %></strong></div>
					  		</td>
					  		<%  if (zonePrevYTDSales != 0){ %>
						  		<td><div align="right"><%= pf.format(zoneTotalGrowthPercentage) %></div>
						  	<%  } else { %>
						  		<td><div align="right">N/A</div>
						  	<%  } %>
					  		</td>
	 					</tr>
	 <%
	  					zonePrevYTDSales = 0;
	  					zoneYTDSales = 0;
	  				
					}
					zone = geog;
				} else {
				
	   %>
	   		<tr class="cellShade">
	  		<td><div align="left" id="cellShade"><a href="CustomerListing?page=listing&targetOnly=true&DIVISION=<%= division.getId() %>&DISTRICT=<%= geog.getGeog() %>"><%= geog.getDescription() %> (<%= geog.getGeog() %>)</a></div>
  		 	</td>
  		<%
				for (int beanIndex = 0; beanIndex < reportBeans.size(); beanIndex++){
					CSFReportBean bean = (CSFReportBean) reportBeans.get(beanIndex);
					if (geog.getGeog().equalsIgnoreCase(bean.getId())){
						resultsFound = true;
						grandTotalYTDSales += bean.getYTDSales();
						grandTotalPrevYTDSales += bean.getPrevYTDSales();
						zoneYTDSales += bean.getYTDSales();
						zonePrevYTDSales += bean.getPrevYTDSales();

		%>
	  	
	  		<td><div align="right"><%= nf.format(bean.getYTDSales()) %></div>
			</td>
	  		<td><div align="right"><%= nf.format(bean.getPrevYTDSales()) %></div>
	  		</td>
	  	<%  if (bean.getPrevYTDSales() != 0){ %>
	  		<td><div align="right"><%= pf.format(bean.getGrowthPercentage()) %></div>
	  	<%  } else { %>
	  		<td><div align="right">N/A</div>
	  	<%  } %>
	  		</td>
  		
	  	<%
	  				} 
	  			}	
	  			if (!resultsFound){   
	  	 %>
	    				<td><div align="right"><%= nf.format(0) %></div>
						</td>
	  					<td><div align="right"><%= nf.format(0) %></div>
	  					</td>
	  					<td><div align="right">N/A</div>
	  					</td>
	    
	  	<%		}
	  	
	  	%>
	  		</tr>
	  	<%  	
	  			}
	  		}
	  		zoneTotalGrowthPercentage = 99999;
			if (zonePrevYTDSales != 0){
  					zoneTotalGrowthPercentage = ((zoneYTDSales - zonePrevYTDSales) / zonePrevYTDSales);
  			}	
	  	%>
		  				<tr class="cellShade">
							<td width="17%">
			
							<div align="center"><strong><%= zone.getDescription() %></strong>:</div>
							</td>
							<td><div align="right"><strong><%= nf.format(zoneYTDSales) %></strong></div>
							</td>
					  		<td><div align="right"><strong><%= nf.format(zonePrevYTDSales) %></strong></div>
					  		</td>
					  		<%  if (zonePrevYTDSales != 0){ %>
						  		<td><div align="right"><%= pf.format(zoneTotalGrowthPercentage) %></div>
						  	<%  } else { %>
						  		<td><div align="right">N/A</div>
						  	<%  } %>
					  		</td>
	 					</tr>	
	   <%		
	  		double grandTotalGrowthPercentage = 99999;
	  		if (grandTotalPrevYTDSales != 0){
	  			grandTotalGrowthPercentage = ((grandTotalYTDSales - grandTotalPrevYTDSales) / grandTotalPrevYTDSales);
	  		}
  	    %>
	
		<tr class="cellShade">
			<td width="17%">

				<div align="center"><strong>Grand Total</strong>:</div>
			</td>
			<td><div align="right"><strong><%= nf.format(grandTotalYTDSales) %></strong></div>
			</td>
	  		<td><div align="right"><strong><%= nf.format(grandTotalPrevYTDSales) %></strong></div>
	  		</td>
	  		<%  if (grandTotalPrevYTDSales != 0){ %>
		  		<td><div align="right"><%= pf.format(grandTotalGrowthPercentage) %></div>
		  	<%  } else { %>
		  		<td><div align="right">N/A</div>
		  	<%  } %>
	  		</td>
	 </tr>
	</table>
	<br><hr align="left" width="600" size="1" color="#999999" noshade>
	<br><br>
	<!-- the values of these fields will changed when status update is submitted  -->
	<form action="CSF" method="POST" name="UpdateColor">
	<input type="hidden" name="CSF_ID" value="">
	<input type="hidden" name="divisionId" value="<%= division.getId() %>">
	<input type="hidden" name="page" value="updateColor">
	<input type="hidden" name="DISTRICT" value="">
	<input type="hidden" name="COLOR" value="">
	</form>
	<%
	for (int i=0; i < csfs.size(); i++) {
				CriticalSuccessFactor csf = (CriticalSuccessFactor)csfs.get(i);
				ArrayList notes = csf.getDistrictsWithNotes();
	%>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>

			<td class="tableTitle">
		
			<font class="heading3"><%= csf.getName() %></font> - <font size="1">Added by <%= (csf.getUserAddedName().equals("")?"unknown":csf.getUserAddedName()) %> on <%= csf.getDateAdded() %></font>
			&nbsp;&nbsp;&nbsp;&nbsp;
		<% if(header.getUser().isAbleToSeeDivisionCSF()) { %>
			
			<a href="/TargetAccountPlanner/CSF?page=deactivate&csfid=<%= csf.getId() %>&divisionId=<%= division.getId() %>"><img src="<%= sImagesURL %>button_deactivate.gif" align="absmiddle" border="0" width="70" height="20"></a>
			<% }%>
				
			
			<br><br>
			

		    </td>
	    </tr>
		<tr>
			<td>
			<%
			//ArrayList csfDistricts = csf.getDistrictsWithNotes();
			String prevGeog = "";
			for (int j=0; j < notes.size(); j++) {
				CriticalSuccessFactorNote note = (CriticalSuccessFactorNote)notes.get(j);
				Geography geog = new Geography(note.getGeog());
				if (!geog.getGeog().equals(prevGeog)){
				    if (j>0){
			%>      
						</blockquote><br>
			<%		}
		    %>
				    
			   		<b><%= geog.getZone() %>-<%= geog.getDistrict() %>&nbsp;&nbsp;<%= note.getGeogDescription() %></b>
			   		
    		<% 		// The first csf_notes record should always be the one created by the creation of the CSF
    		       //  so it's only important to pull the color from that record.
    				String colorImage = "";
            		if (note.getColor().equalsIgnoreCase("Green")){
            		    colorImage = "<img src=\"" + sImagesURL + "\\green_status.gif\" border=\"0\">";
            		} else if (note.getColor().equalsIgnoreCase("Yellow")){
            		    colorImage = "<img src=\"" + sImagesURL + "\\yellow_status.gif\" border=\"0\">";
            		} else if (note.getColor().equalsIgnoreCase("Red")){
            		    colorImage = "<img src=\"" + sImagesURL + "\\red_status.gif\" border=\"0\">";
            		}
    				if (header.getUser().isAbleToSeeDivisionCSF()){
		    %>
		        		&nbsp;&nbsp; <a href="CSF?page=editDistrictCSF&district=<%= geog.getGeog() %>&csfid=<%= csf.getId() %>&from=division&divId=<%= division.getId() %>">
		        		Edit
		        		</a>&nbsp;&nbsp; <%= colorImage %>&nbsp;&nbsp;
		        		<select name="COLOR_<%= csf.getId() %>_<%= geog.getGeog() %>">
		        			<option value="" <%= (note.getColor().equalsIgnoreCase("")) ? "selected" : " " %>>None</option>
		        			<option value="Green" <%= (note.getColor().equalsIgnoreCase("Green")) ? "selected" : " " %>>Green</option>
		        			<option value="Yellow" <%= (note.getColor().equalsIgnoreCase("Yellow")) ? "selected" : " " %>>Yellow</option>
		        			<option value="Red" <%= (note.getColor().equalsIgnoreCase("Red")) ? "selected" : " " %>>Red</option>
		        	    </select>&nbsp;&nbsp;
		        		<input type="image" src="<%= sImagesURL%>button_save_status.gif" align="absmiddle" width="82" height="20" border="0" onclick="submitColorUpdate(<%= csf.getId() %>,<%= geog.getGeog() %>,COLOR_<%= csf.getId() %>_<%= geog.getGeog() %>.value)">&nbsp;
	        <% 		} else {
	         %>
	         			&nbsp;&nbsp;<%= colorImage %>
	        <%		}
	        %>
		    		<blockquote>
			
				
			<% }
				if ((note.getDateChanged()!=null) && !(note.getNote().equals(""))){
				%>
				 <font size="1" color="#71674A">&nbsp;&nbsp;<b>Added by <%= (note.getUserAddedName().equals(""))?"unknown":note.getUserAddedName() %> on <%=note.getDateAdded() %> :</b>&nbsp; </font>
				 <%= note.getNote() %><br><br>
				<%  
				}
				%>
				
			<%
				prevGeog = geog.getGeog();
			} 
			%>
			</blockquote>				
			</td>
	    </tr>
	</table>

<br><br><hr align="left" width="500" size="1" color="#999999" noshade>
<br>
	<% } %>
	
  <%
   if(header.getUser().isAbleToSeeDivisionCSF()) { 
  %>
  <a href="/TargetAccountPlanner/CSF?page=add&divisionId=<%= division.getId() %>"><img src="<%= sImagesURL %>button_new_csf.gif" width="70" height="20" border="0"></a>
   <%    } 
    %>
   
   </td>
  </tr>
</table>
<p>&nbsp;</p>
  </body>
</html>
