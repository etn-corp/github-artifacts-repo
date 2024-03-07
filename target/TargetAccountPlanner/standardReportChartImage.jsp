<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*, java.text.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*,com.eaton.electrical.smrc.util.*"%>
	
<%@ page import="org.jfree.chart.*" %>
<%@ page import="org.jfree.data.category.*" %>

<html>
<body>

<%

    JFreeChart chart = (JFreeChart) session.getAttribute("standardReportChart");
    int chartHeight = ((Integer) session.getAttribute("chartHeight")).intValue();
    int chartWidth = ((Integer) session.getAttribute("chartWidth")).intValue();
    ServletOutputStream imgout = response.getOutputStream();
 //  ChartUtilities.writeChartAsJPEG(imgout,chart,chartWidth,chartHeight);
 // The PNG Image appears to be a little clearer than the JPEG
    ChartUtilities.writeChartAsPNG(imgout,chart,chartWidth,chartHeight);
  %>

  </body>
</html>