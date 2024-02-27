<%@ page import = "java.util.*" %>
<%@ include file="analytics.jsp" %>
<%
	int chartHeight = ((Integer) session.getAttribute("chartHeight")).intValue();
	int chartWidth = ((Integer) session.getAttribute("chartWidth")).intValue();
	
	ArrayList colorList = (ArrayList) request.getAttribute("colorList");
	ArrayList chartTypes = (ArrayList) request.getAttribute("chartTypes");
	ArrayList fontStyleList = (ArrayList) request.getAttribute("fontStyleList");
	
%>


<html>
	<head>
		<title></title>
	</head>

<body>
	<%@ include file = "./SMRCHeaderNoNav.jsp" %>
	<script language="javascript" src="<%= jsURL %>validation/standardReportChart.js"></script>
	<script>
    function loadingMessage(){
        setInnerHTML("loadingMessage", "<center><b><font size=5 color=\"red\">New Chart is Loading....</font></b></center>");
	}

</script>
	<center>
	<table>
	<tr><br><br><center><p class="heading1">Standard Report Graphs</p></center>
	</tr>
	<tr><td><center>
	<form name="theform" action="StandardReportChart" method="post" onSubmit="javascript:return formValidation();">
	
	<iframe src="standardReportChartImage.jsp" frameborder=0 scrolling=no width="<%= chartWidth + 50 %>" height="<%= chartHeight + 50 %>"></iframe>
	
		</center></td></tr>
	<tr><td>
			<div id="loadingMessage">&nbsp;</div>
	    </td>
	</tr>
	<tr><td><center>
		
 <br>
		<table border="1" bordercolor="gray" cellspacing="5" cellpadding="5">
		<tr>
			<th colspan=4 ><center>Chart Options</center></th>
		</tr>
			<td class=smallFont>Type Of Chart</td>
			<td>
			<select name="chartType">
	<%		for (int i=0; i< chartTypes.size(); i++){
	            String chartType = (String) chartTypes.get(i);
	            String chartSelection = "";
	            if (request.getParameter("chartType") != null){
	                if (request.getParameter("chartType").equalsIgnoreCase(chartType)){
	                    chartSelection = " selected ";
	                }
	            } else if (chartType.equalsIgnoreCase("Bar")){
	                chartSelection = " selected ";
	            }
	            
	%>
				<option value="<%= chartType %>" <%= chartSelection %>><%= chartType %></option>
	<%		}  %>
			
			</select>
			</td>
			<td class=smallFont>Height</td>
			<td>
				<input type="text" name="chartHeight" value="<%= chartHeight %>">
			</td>
		</tr>
			
		<tr>
			<td class=smallFont>Show Values?</td>
			<td><select name="showValues">
			<%      String valuesSelection = "N";
			        if (request.getParameter("showValues") != null && 
			            request.getParameter("showValues").equalsIgnoreCase("Y")){
			            valuesSelection = "Y";
			        }
	         %>
				<option value="Y" <%= (valuesSelection.equals("Y")) ? " selected " : " " %>>Yes</option>
				<option value="N" <%= (valuesSelection.equals("N")) ? " selected " : " " %>>No</option>
			</select>
			</td>
			
			<td class=smallFont>Width</td>
			<td>
				<input type="text" name="chartWidth" value="<%= chartWidth %>">
			</td>
		</tr>
		
		<tr>
			<td class=smallFont>Invert Chart?</td>
			<td><select name="invertChart">
			<%      String invertSelection = "N";
			        if (request.getParameter("invertChart") != null && 
			            request.getParameter("invertChart").equalsIgnoreCase("Y")){
			            invertSelection = "Y";
			        }
	         %>
				<option value="Y" <%= (invertSelection.equals("Y")) ? " selected " : " " %>>Yes</option>
				<option value="N" <%= (invertSelection.equals("N")) ? " selected " : " " %>>No</option>
			</select>
			</td>
			<td class=smallFont>X Axis Label</td>
			<td>
			<%      String xAxisLabel = "";
			        if (request.getParameter("xAxisLabel") != null){
			            xAxisLabel = request.getParameter("xAxisLabel");
			        }
			%>
						<input type="text" name="xAxisLabel" maxlength=35 value="<%= xAxisLabel %>">
			</td>
		</tr>
		<tr>
			<td class=smallFont>Background Color</td>
			<td>
			<select name="backgroundColor">
	<%		for (int i=0; i < colorList.size(); i++){
	    		String color = (String) colorList.get(i);
	    		String colorSelected = "";
	    		if (request.getParameter("backgroundColor") != null){ 
	    		   if (request.getParameter("backgroundColor").equalsIgnoreCase(color)){
	    		       colorSelected = " selected ";
	    		   }
	    		} else if (color.equalsIgnoreCase("Light Gray")){
	    		   colorSelected = " selected ";
	    		}
	%>
				<option value="<%= color %>" <%= colorSelected %>><%= color %></option>
				
	<%		} %>
			</select>
			</td>
			<td class=smallFont>Y Axis Label</td>
			<td>
			<%      String yAxisLabel = "";
			        if (request.getParameter("yAxisLabel") != null){
			            yAxisLabel = request.getParameter("yAxisLabel");
			        }
			%>
						<input type="text" name="yAxisLabel" maxlength=35 value="<%= yAxisLabel %>">
			</td>
			
		</tr>
		<tr>
			<td class=smallFont>Foreground Color</td>
			<td>
			<select name="foregroundColor">
	<%		for (int i=0; i < colorList.size(); i++){
	    		String color = (String) colorList.get(i);
	    		String colorSelected = "";
	    		if (request.getParameter("foregroundColor") != null){ 
	    		   if (request.getParameter("foregroundColor").equalsIgnoreCase(color)){
	    		       colorSelected = " selected ";
	    		   }
	    		} else if (color.equalsIgnoreCase("Black")){
	    		   colorSelected = " selected ";
	    		}
	%>
				<option value="<%= color %>" <%= colorSelected %>><%= color %></option>
				
	<%		} %>
			</select>
			</td>
			<td class=smallFont>Chart Area Background Color</td>
			<td>
			<select name="chartAreaBackgroundColor">
	<%		for (int i=0; i < colorList.size(); i++){
	    		String color = (String) colorList.get(i);
	    		String colorSelected = "";
	    		if (request.getParameter("chartAreaBackgroundColor") != null){ 
	    		   if (request.getParameter("chartAreaBackgroundColor").equalsIgnoreCase(color)){
	    		       colorSelected = " selected ";
	    		   }
	    		} else if (color.equalsIgnoreCase("White")){
	    		   colorSelected = " selected ";
	    		}
	%>
				<option value="<%= color %>" <%= colorSelected %>><%= color %></option>
				
	<%		} %>
			</select>
			</td>
		</tr>
		<tr>
			<td class=smallFont>Foreground Font Style</td>
	    	<td>
			<select name="foregroundFontStyle">
	<%		for (int i=0; i < fontStyleList.size(); i++){
	    		String fontStyle = (String) fontStyleList.get(i);
	    		String styleSelected = "";
	    		if (request.getParameter("foregroundFontStyle") != null){ 
	    		   if (request.getParameter("foregroundFontStyle").equalsIgnoreCase(fontStyle)){
	    		       styleSelected = " selected ";
	    		   }
	    		} else if (fontStyle.equalsIgnoreCase("Plain")){
	    		    styleSelected = " selected ";
	    		}
	%>
				<option value="<%= fontStyle %>" <%= styleSelected %>><%= fontStyle %></option>
				
	<%		} %>
			</select>
			</td>
			<td colspan=2>&nbsp;</td>
		</tr>
		<tr>
	    	<th colspan=2><center>Title Options</center></th>
	    	<th colspan=2><center>Legend Options</center></th>
	    </tr>
	    <tr>
		    <td class=smallFont>Title</td>
			<td>
	<%      String chartTitle = "";
	        if (request.getParameter("chartTitle") != null){
	            chartTitle = request.getParameter("chartTitle");
	        }
	%>
				<input type="text" name="chartTitle" maxlength=35 value="<%= chartTitle %>">
			</td>
		    <td class=smallFont>Show Legend</td>
			<td>
			<select name="showLegend">
	<%      String legendSelection = "Y";
	        if (request.getParameter("showLegend") != null && 
	            request.getParameter("showLegend").equalsIgnoreCase("N")){
	            legendSelection = "N";
	        }
	%>
				<option value="Y" <%= (legendSelection.equals("Y")) ? " selected " : " " %>>Yes</option>
				<option value="N" <%= (legendSelection.equals("N")) ? " selected " : " " %>>No</option>
			</select>
			</td>
	    </tr>
	    <tr>
		    <td class=smallFont>Title Font Size</td>
			<td>
			<%      String titleFontSize = "20";
			        if (request.getParameter("titleFontSize") != null){
			            titleFontSize = request.getParameter("titleFontSize");
			        } 
			%>
						<input type="text" name="titleFontSize" maxlength=3 value="<%= titleFontSize %>">
			</td>
			<td class=smallFont>Legend Background Color</td>
			<td>
			<select name="legendBackgroundColor">
	<%		for (int i=0; i < colorList.size(); i++){
	    		String color = (String) colorList.get(i);
	    		String colorSelected = "";
	    		if (request.getParameter("legendBackgroundColor") != null){ 
	    		   if (request.getParameter("legendBackgroundColor").equalsIgnoreCase(color)){
	    		       colorSelected = " selected ";
	    		   }
	    		} else if (color.equalsIgnoreCase("White")){
	    		   colorSelected = " selected ";
	    		}
	%>
				<option value="<%= color %>" <%= colorSelected %>><%= color %></option>
				
	<%		} %>
			</select>
			</td>
		</tr>
		<tr>
			<td class=smallFont>Title Font Color</td>
			<td>
			<select name="titleFontColor">
	<%		for (int i=0; i < colorList.size(); i++){
	    		String color = (String) colorList.get(i);
	    		String colorSelected = "";
	    		if (request.getParameter("titleFontColor") != null){ 
	    		   if (request.getParameter("titleFontColor").equalsIgnoreCase(color)){
	    		       colorSelected = " selected ";
	    		   }
	    		} else if (color.equalsIgnoreCase("Black")){
	    		   colorSelected = " selected ";
	    		}
	%>
				<option value="<%= color %>" <%= colorSelected %>><%= color %></option>
				
	<%		} %>
			</select>
			</td>
			<td class=smallFont>Legend Foreground Color</td>
			<td>
			<select name="legendForegroundColor">
	<%		for (int i=0; i < colorList.size(); i++){
	    		String color = (String) colorList.get(i);
	    		String colorSelected = "";
	    		if (request.getParameter("legendForegroundColor") != null){ 
	    		   if (request.getParameter("legendForegroundColor").equalsIgnoreCase(color)){
	    		       colorSelected = " selected ";
	    		   }
	    		} else if (color.equalsIgnoreCase("Black")){
	    		   colorSelected = " selected ";
	    		}
	%>
				<option value="<%= color %>" <%= colorSelected %>><%= color %></option>
				
	<%		} %>
			</select>
			</td>
	    </tr>
	    <tr>
	    	<td class=smallFont>Title Font Style</td>
	    	<td>
			<select name="titleFontStyle">
	<%		for (int i=0; i < fontStyleList.size(); i++){
	    		String fontStyle = (String) fontStyleList.get(i);
	    		String styleSelected = "";
	    		if (request.getParameter("titleFontStyle") != null){ 
	    		   if (request.getParameter("titleFontStyle").equalsIgnoreCase(fontStyle)){
	    		       styleSelected = " selected ";
	    		   }
	    		} else if (fontStyle.equalsIgnoreCase("Plain")){
	    		    styleSelected = " selected ";
	    		}
	%>
				<option value="<%= fontStyle %>" <%= styleSelected %>><%= fontStyle %></option>
				
	<%		} %>
			</select>
			</td>
	    	
	    	<td class=smallFont>Legend Font Size</td>
			<td>
			<%      String legendFontSize = "10";
			        if (request.getParameter("legendFontSize") != null){
			            legendFontSize = request.getParameter("legendFontSize");
			        } 
			%>
						<input type="text" name="legendFontSize" maxlength=3 value="<%= legendFontSize %>">
			</td>
	    </tr>
	    <tr>
	    	<td colspan=2>&nbsp;
			<td class=smallFont>Legend Font Style</td>
	    	<td>
			<select name="legendFontStyle">
	<%		for (int i=0; i < fontStyleList.size(); i++){
	    		String fontStyle = (String) fontStyleList.get(i);
	    		String styleSelected = "";
	    		if (request.getParameter("legendFontStyle") != null){ 
	    		   if (request.getParameter("legendFontStyle").equalsIgnoreCase(fontStyle)){
	    		       styleSelected = " selected ";
	    		   }
	    		} else if (fontStyle.equalsIgnoreCase("Plain")){
	    		    styleSelected = " selected ";
	    		}
	%>
				<option value="<%= fontStyle %>" <%= styleSelected %>><%= fontStyle %></option>
				
	<%		} %>
			</select>
			</td>
		</tr>
	</table>
<br>

	</center></td></tr>
	<tr><td>
	<center><input type="submit" value="Update Chart" onClick="loadingMessage();"></center>
	</td></tr>
	</form>
	</table>
	</center>
	<br>
</body>

</html>
