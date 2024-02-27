<%@page contentType="text/html"%>
<%
	String forecastActualTag = "Forecasted";
	if (request.getParameter("tag") != null && !request.getParameter("tag").trim().equals("")){
		forecastActualTag = request.getParameter("tag");
	}
%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="stylesheets/style_2_1.css" />
		<title>Target Market Plan Chart Help</title>
	</head>
	<body>
		<table rules="rows" >
			<tr><td><b>YTD Sales:</b></td><td>Total sales for this year for the product line through the current month</td></tr>
			<tr><td><b>Prev Yr Sales:</b></td><td>Total sales for this product line the previous year.</td></tr>
			<tr><td><b>Planned Sales Objective:</b></td><td>Goal for this product line for the length of the target market plan.</td></tr>
			<tr><td><b>Incremental Growth:</b></td><td>Growth, in dollars, for this product line expected according to the planned sales objective (Planned sales objective - Prev Yr Sales)</td></tr>
			<tr><td><b>Prev Plan Total Sales:</b></td><td>Sales for this product line the previous year, for the months included in the plan.</td></tr>
			<tr><td><b>Prev Plan Sales to Date:</b></td><td>Sales for this product line the previous year, starting with the beginning month of the plan and ending in the current month.</td></tr>
			<tr><td><b>Sales in Plan:</b></td><td>Sales for the current year in the product line, beginning with the first month of the plan through today.</td></tr>
			<tr><td><b><%= forecastActualTag %> Total Growth:</b></td><td>Growth, in dollars, accomplished during the plan.<br><em>Calculation: (Prev Plan Total Sales) * (<%= forecastActualTag %> % Growth)</em></td></tr>
			<tr><td><b><%= forecastActualTag %> % Growth:</b></td><td>Growth percentage accomplished during the plan. <br><em>Calculation: (Sales in Plan - Prev Plan Sales to Date) / Prev Plan Sales to Date</em></td></tr>
			<tr><td><b><%= forecastActualTag %> Payout:</b></td><td>Payout is determined by multiplying the payout percentage from the incentive payout chart times the total growth. The payout percentage is determined by the growth percentage. </td></tr>
			<tr><td colspan="2">* Only YTD Sales, Prev Yr Sales, Planned Sales Objective, and Incremental Growth are populated before a plan is active.</td></tr>
	</body>
</html>