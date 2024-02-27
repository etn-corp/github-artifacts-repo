<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*, java.text.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*,com.eaton.electrical.smrc.util.*"%>
	
<%@ page import="org.jfree.chart.*" %>
<%@ page import="org.jfree.data.category.*" %>

<html>
<body>

<%
// "Forecast Versus Actual By District"

    JFreeChart chart = (JFreeChart) session.getAttribute("zoneForecastChart");
    ServletOutputStream imgout = response.getOutputStream();
    ChartUtilities.writeChartAsJPEG(imgout,chart,360,260);
  %>

  </body>
</html>