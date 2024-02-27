package com.eaton.electrical.smrc;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.service.*;

import java.sql.*;
import org.jfree.chart.*;

/**
 * Servlet used for manipulation of the CewolfChart and CewolfImg attributes
 * used on web/standardReportChart.jsp.
 */
public class StandardReportChart extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SMRCHeaderBean tempSMRCHeaderBean = new SMRCHeaderBean();
		request.setAttribute("header", tempSMRCHeaderBean);
		String sFwdUrl = "/standardReportChart.jsp";
		boolean redirect = false;
		Connection DBConn = null;

		try {
			HttpSession session = request.getSession();
			
			DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();
			ChartQueryBean bean = (ChartQueryBean) session.getAttribute("standardReportChartQueryBean");

			String chartType = SMRCChart.BAR_CHART;
			if (request.getParameter("chartType") != null){
			    chartType = request.getParameter("chartType");
			}
			
			SMRCChart chart = new SMRCChart(chartType);
			
			int chartWidth = 400;
			int chartHeight = 400;
			if (request.getParameter("chartHeight") != null){
			    chartHeight = (new Integer(request.getParameter("chartHeight"))).intValue();
			}
			if (request.getParameter("chartWidth") != null){
			    chartWidth = (new Integer(request.getParameter("chartWidth"))).intValue();
			}
			
			chart.setChartQueryBean(bean);
			chart.setShowLegend(true);
			if (request.getParameter("showLegend") != null &&
			    request.getParameter("showLegend").equalsIgnoreCase("N")){
			    chart.setShowLegend(false);
			}
			if (request.getParameter("legendBackgroundColor") != null){
			    chart.setLegendBackgroundColor(request.getParameter("legendBackgroundColor"));
			}
			if (request.getParameter("legendForegroundColor") != null){
			    chart.setLegendItemColor(request.getParameter("legendForegroundColor"));
			}
			if (request.getParameter("legendFontSize") != null){
			    int legendFontSize = (new Integer(request.getParameter("legendFontSize"))).intValue();
			    chart.setLegendFontSize(legendFontSize);
			}
			if (request.getParameter("legendFontStyle") != null){
			    chart.setLegendFontStyle(request.getParameter("legendFontStyle"));
			}
			if (request.getParameter("chartTitle") != null){
			    chart.setChartTitle(request.getParameter("chartTitle"));
			}
			if (request.getParameter("yAxisLabel") != null){
			    chart.setYAxisLabel(request.getParameter("yAxisLabel"));
			}
			if (request.getParameter("xAxisLabel") != null){
			    chart.setXAxisLabel(request.getParameter("xAxisLabel"));
			}
			if (request.getParameter("invertChart") != null){
			    if (request.getParameter("invertChart").equalsIgnoreCase("Y")){
			        chart.invertChart(true);
			    } else {
			        chart.invertChart(false);
			    }
			}
			if (request.getParameter("showValues") != null){
			    if (request.getParameter("showValues").equalsIgnoreCase("Y")){
			        chart.setShowActualValues(true);
			    } else {
			        chart.setShowActualValues(false);
			    }
			}
			
			if (request.getParameter("backgroundColor") != null){
			    chart.setBackgroundColor(request.getParameter("backgroundColor"));
			}
			if (request.getParameter("foregroundColor") != null){
			    chart.setForegroundColor(request.getParameter("foregroundColor"));
			}
			if (request.getParameter("foregroundFontStyle") != null){
			    chart.setForegroundFontStyle(request.getParameter("foregroundFontStyle"));
			}
			if (request.getParameter("chartAreaBackgroundColor") != null){
			    chart.setChartAreaBackgroundColor(request.getParameter("chartAreaBackgroundColor"));
			}
			if (request.getParameter("titleFontColor") != null){
			    chart.setTitleColor(request.getParameter("titleFontColor"));
			}
			if (request.getParameter("titleFontSize") != null){
			    int titleFontSize = (new Integer(request.getParameter("titleFontSize"))).intValue();
			    chart.setTitleFontSize(titleFontSize);
			}
			if (request.getParameter("titleFontStyle") != null){
			    chart.setTitleFontStyle(request.getParameter("titleFontStyle"));
			}
			
			JFreeChart jfreechart = chart.getChart(DBConn);
			
			session.setAttribute("standardReportChart", jfreechart);
			session.setAttribute("chartHeight", new Integer(chartHeight));
			session.setAttribute("chartWidth", new Integer(chartWidth));
			request.setAttribute("colorList", SMRCChart.getAvailableChartColorList());
			request.setAttribute("chartTypes", SMRCChart.getChartTypes());
			request.setAttribute("fontStyleList", SMRCChart.getAvailableFontStyles());
		}
		catch(Exception e){
		    SMRCLogger.error(this.getClass().getName() + ".doGet() ", e);
		    
			request.setAttribute("exception", e.getMessage());
			sFwdUrl = "/SMRCErrorPage.jsp";
			redirect = false;	
		}
		finally{
		    SMRCConnectionPoolUtils.close(DBConn);
			gotoPage(sFwdUrl, request, response, redirect);
		}
		
	}
	
	
} //class
