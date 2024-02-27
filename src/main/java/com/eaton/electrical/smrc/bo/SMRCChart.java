/*
 * Created on Mar 28, 2005
 *
  */
package com.eaton.electrical.smrc.bo;

import java.util.*;
import java.sql.*;
import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.AbstractCategoryItemRenderer;
import org.jfree.util.TableOrder;
import org.jfree.chart.title.*;


import java.awt.*;

import com.eaton.electrical.smrc.service.SMRCLogger;


/**
 * @author Jason Lubbert
 * 
 * This class is used to create various charts. A very basic bar chart can be created
 * by creating an instance of this class, setting a ChartQueryBean, and calling the 
 * getChart() method. The rest of the available features are controlled through "set"
 * methods.
 * 
 * To add more chart types, start by seeing what is necessary for the ChartFactory class
 * to create it. From there, you may have to create new methods in SMRCChartData to return
 * different types of datasets. Changes may also have to be made in getPlot() to handle 
 * different types of Plots. The getPlot() method handles most of the chart customization,
 * although some things are handle by the JFreeChart object and in getLegend();
 *
 */
public class SMRCChart implements java.io.Serializable {
    
    public final static String AREA_CHART = new String("Area");
    public final static String BAR_CHART = new String("Bar");
    public final static String BAR_CHART_3D = new String("Bar - 3D");
    public final static String MULTIPLE_PIE = new String("Multiple Pie");
    public final static String MULTIPLE_PIE_3D = new String("Multiple Pie - 3D");
    public final static String STACKED_AREA = new String("Stacked Area");
    public final static String STACKED_BAR = new String("Stacked Bar");
    public final static String STACKED_BAR_3D = new String("Stacked Bar - 3D");
     
    private String chartType = null;
    private PlotOrientation plotOrientation = null; 
    private ChartQueryBean chartQueryBean = null;
    // main chart fields
    private String chartTitle = null;
    private Color titleColor = null;
    private int titleFontSize = 20;
    private int titleFontStyle = Font.PLAIN;
    private String XAxisLabel = null;
    private String YAxisLabel = null;
    private Color backgroundColor = null;
    
    
    // Plot fields
    private Color foregroundColor = null;
    private int foregroundFontStyle = Font.PLAIN;
    private boolean showActualValues = false;
    private Color chartAreaBackgroundColor = null;
    
    // Legend fields
    private boolean showLegend = true;
    private Color legendBackgroundColor = null;
    private Color legendItemColor = null;
    private int legendFontSize = 10;
    private int legendFontStyle = Font.PLAIN;
        
    
	private static final long serialVersionUID = 100;

	public SMRCChart(){
        setDefaults();
    }
    
    public SMRCChart(String chartType){
        setDefaults();
        this.chartType = chartType;
    }
    
    // This method sets the chart defaults. This allows for easy creation of simple charts.
    private void setDefaults(){
        chartTitle = new String();
        XAxisLabel = new String();
        YAxisLabel = new String();
        plotOrientation = PlotOrientation.VERTICAL;
        chartType = BAR_CHART;
        showLegend = true;
        legendBackgroundColor = Color.WHITE;
        legendItemColor = Color.BLACK;
        legendFontSize = 10;
        legendFontStyle = Font.PLAIN;
        backgroundColor = Color.LIGHT_GRAY;
        foregroundColor = Color.BLACK;
        showActualValues = false;
        chartAreaBackgroundColor = Color.WHITE;
        titleColor = Color.BLACK;
        titleFontSize = 20;
        titleFontStyle = Font.PLAIN;
    }
       
    // This method returns an ArrayList of Strings describing the types of charts available
    public static ArrayList getChartTypes(){
        ArrayList types = new ArrayList();
        types.add(AREA_CHART);
        types.add(BAR_CHART);
        types.add(BAR_CHART_3D);
        types.add(MULTIPLE_PIE);
        types.add(MULTIPLE_PIE_3D);
        types.add(STACKED_AREA);
        types.add(STACKED_BAR);
        types.add(STACKED_BAR_3D);    
                
        return types;
    }
    
   
    // This method returns the JFreeChart object to be rendered by the calling servlet.
    public JFreeChart getChart(Connection DBConn) throws Exception {
        JFreeChart chart = null;
        
        SMRCChartData chartData = new SMRCChartData(chartQueryBean);
        
        
        try {
            // The legends are not created when initial chart is created below because
            // they are assigned later, if a legend is desired.
	        if (chartType.equals(AREA_CHART)){
	            chart = ChartFactory.createAreaChart(chartTitle, XAxisLabel, YAxisLabel, chartData.getCategoryDataset(DBConn),
	                    plotOrientation, false,false,false);
	        } else if (chartType.equals(BAR_CHART)){
	            chart = ChartFactory.createBarChart(chartTitle, XAxisLabel, YAxisLabel, chartData.getCategoryDataset(DBConn),
	                    plotOrientation, false,false,false);
	        } else if (chartType.equals(BAR_CHART_3D)){
	            chart = ChartFactory.createBarChart3D(chartTitle, XAxisLabel, YAxisLabel, chartData.getCategoryDataset(DBConn),
	                    plotOrientation, false,false,false);
	        } else if (chartType.equals(MULTIPLE_PIE)){
	            chart = ChartFactory.createMultiplePieChart(chartTitle, chartData.getCategoryDataset(DBConn),
	                    TableOrder.BY_COLUMN, false,false,false);
	        } else if (chartType.equals(MULTIPLE_PIE_3D)){
	            chart = ChartFactory.createMultiplePieChart3D(chartTitle, chartData.getCategoryDataset(DBConn),
	                    TableOrder.BY_COLUMN, false,false,false);
	        } else if (chartType.equals(STACKED_AREA)){
	            chart = ChartFactory.createStackedAreaChart(chartTitle, XAxisLabel, YAxisLabel, chartData.getCategoryDataset(DBConn),
	                    plotOrientation, false,false,false);
	        } else if (chartType.equals(STACKED_BAR)){
	            chart = ChartFactory.createStackedBarChart(chartTitle, XAxisLabel, YAxisLabel, chartData.getCategoryDataset(DBConn),
	                    plotOrientation, false,false,false);
	        } else if (chartType.equals(STACKED_BAR_3D)){
	            chart = ChartFactory.createStackedBarChart3D(chartTitle, XAxisLabel, YAxisLabel, chartData.getCategoryDataset(DBConn),
	                    plotOrientation, false,false,false);
	        }
        }catch (Exception e)	{
			SMRCLogger.error("SMRCChart.getChart(): " , e);
			throw e;
		}
        
        Plot plot = getPlot(chart.getPlot());
        
        // A new chart has to be created to implement the changes to the plot that occurred
        // in getPlot().
        chart = new JFreeChart(chartTitle, JFreeChart.DEFAULT_TITLE_FONT, plot, false);       
        
        // Assign the legend
        if (isShowLegend()){
            chart.setLegend(getLegend());
        }
        
        chart.setBackgroundPaint(backgroundColor);
        Font font = new Font("SMRCFont", titleFontStyle, titleFontSize);
        chart.setTitle(new TextTitle(chartTitle, font, titleColor));
                        
        return chart;
       
    }

   
    /**
     * @return Returns the chartType.
     */
    public String getChartType() {
        return chartType;
    }
    /**
     * @param chartType The chartType to set.
     * The String has to match one of the class fields declared at the top.
     */
    public void setChartType(String chartType) {
        this.chartType = chartType;
    }
    
    // True turns the chart horizontal; False (default) turns it vertical
    public void invertChart(boolean invertChart){
        if (invertChart){
            plotOrientation = PlotOrientation.HORIZONTAL;
        } else { 
            plotOrientation = PlotOrientation.VERTICAL;
        }
    }
    
    
    /**
     * @return Returns the queryBean.
     */
    public ChartQueryBean getChartQueryBean() {
        return chartQueryBean;
    }
    /**
     * @param queryBean The queryBean to set.
     */
    public void setChartQueryBean(ChartQueryBean chartQueryBean) {
        this.chartQueryBean = chartQueryBean;
    }
    /**
     * @return Returns the chartTitle.
     */
    public String getChartTitle() {
        return chartTitle;
    }
    /**
     * @param chartTitle The chartTitle to set.
     */
    public void setChartTitle(String chartTitle) {
        this.chartTitle = chartTitle;
    }
    /**
     * @return Returns the xAxisLabel.
     */
    public String getXAxisLabel() {
        return XAxisLabel;
    }
    /**
     * @param axisLabel The xAxisLabel to set.
     */
    public void setXAxisLabel(String axisLabel) {
        XAxisLabel = axisLabel;
    }
    /**
     * @return Returns the yAxisLabel.
     */
    public String getYAxisLabel() {
        return YAxisLabel;
    }
    /**
     * @param axisLabel The yAxisLabel to set.
     */
    public void setYAxisLabel(String axisLabel) {
        YAxisLabel = axisLabel;
    }
    
    // The colors in this list should match up with the colors in getColorObject()
	public static ArrayList getAvailableChartColorList(){
	    ArrayList colors = new ArrayList();
        colors.add("Black");
        colors.add("Blue");
        colors.add("Cyan");
        colors.add("Dark Gray");
        colors.add("Gray");
        colors.add("Green");
        colors.add("Light Gray");
        colors.add("Magenta");
        colors.add("Orange");
        colors.add("Pink");
        colors.add("Red");
        colors.add("White");
        colors.add("Yellow");
                
        return colors;
    }
	
	// This method returns the Color object of the color described by the string
	// Make sure this list matches up with getAvailableChartColorList()
    private java.awt.Color getColorObject(String colorString){
        if (colorString.equalsIgnoreCase("Black")){
            return java.awt.Color.BLACK;
        } else if (colorString.equalsIgnoreCase("Blue")){
            return java.awt.Color.BLUE;
        } else if (colorString.equalsIgnoreCase("Cyan")){
            return java.awt.Color.CYAN;
        } else if (colorString.equalsIgnoreCase("Dark Gray")){
            return java.awt.Color.DARK_GRAY;
        } else if (colorString.equalsIgnoreCase("Gray")){
            return java.awt.Color.GRAY;
        } else if (colorString.equalsIgnoreCase("Green")){
            return java.awt.Color.GREEN;
        } else if (colorString.equalsIgnoreCase("Light Gray")){
            return java.awt.Color.LIGHT_GRAY;
        } else if (colorString.equalsIgnoreCase("Magenta")){
            return java.awt.Color.MAGENTA;
        } else if (colorString.equalsIgnoreCase("Orange")){
            return java.awt.Color.ORANGE;
        } else if (colorString.equalsIgnoreCase("Pink")){
            return java.awt.Color.PINK;
        } else if (colorString.equalsIgnoreCase("Red")){
            return java.awt.Color.RED;
        } else if (colorString.equalsIgnoreCase("White")){
            return java.awt.Color.WHITE;
        } else if (colorString.equalsIgnoreCase("Yellow")){
            return java.awt.Color.YELLOW;
        }
        // default white 
        return java.awt.Color.WHITE;
    }

   
    /**
     * @return Returns the showLegend.
     */
    public boolean isShowLegend() {
        return showLegend;
    }
    /**
     * @param showLegend The showLegend to set.
     */
    public void setShowLegend(boolean showLegend) {
        this.showLegend = showLegend;
    }
    
    // This method returns a StandardLegend object with any customization implemented
    private StandardLegend getLegend(){
        StandardLegend legend = new StandardLegend();
        legend.setBackgroundPaint(legendBackgroundColor);
        legend.setItemPaint(legendItemColor);
        Font legendFont = new Font("Legend Font", legendFontStyle, legendFontSize);
        legend.setItemFont(legendFont);
       
        return legend;
    }
    /**
     * @return Returns the legendBackgroundColor.
     */
    public Color getLegendBackgroundColor() {
        return legendBackgroundColor;
    }
    /**
     * @param legendBackgroundColor The legendBackgroundColor to set.
     */
    public void setLegendBackgroundColor(Color legendBackgroundColor) {
        this.legendBackgroundColor = legendBackgroundColor;
    }
    public void setLegendBackgroundColor(String legendBackgroundColorString){
        this.legendBackgroundColor = getColorObject(legendBackgroundColorString); 
    }
    /**
     * @return Returns the legendItemColor.
     */
    public Color getLegendItemColor() {
        return legendItemColor;
    }
    /**
     * @param legendItemColor The legendItemColor to set.
     */
    public void setLegendItemColor(Color legendItemColor) {
        this.legendItemColor = legendItemColor;
    }
    public void setLegendItemColor(String legendItemColorString){
        this.legendItemColor = getColorObject(legendItemColorString);
    }
    /**
     * @return Returns the backgoundColor.
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }
    /**
     * @param backgoundColor The backgoundColor to set.
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    public void setBackgroundColor(String backgroundColorString){
        this.backgroundColor = getColorObject(backgroundColorString);
    }
    
    // This method returns the Plot object with any customization. This method may
    // need modified if new chart types are introduced to SMRCChart. 
    private Plot getPlot(Plot oldPlot){
        Plot plot = null;
        Font tempFont = null;
        if (chartType.equals(MULTIPLE_PIE) || chartType.equals(MULTIPLE_PIE_3D)){
            MultiplePiePlot multiplePiePlot = (MultiplePiePlot) oldPlot;
            JFreeChart tempChart = multiplePiePlot.getPieChart();
            PiePlot piePlot = (PiePlot) tempChart.getPlot();
            // Set color of chart area
            piePlot.setBackgroundPaint(chartAreaBackgroundColor);
            /* Show values
             * From the javadocs from JFreeChart:
             * For the label format, use {0} where the pie section key should be inserted, 
             * {1} for the absolute section value and {2} for the percent amount of the 
             * pie section, e.g. "{0} = {1} ({2})" will display as apple = 120 (5%). 
             */
            String labelFormat = "";
            if (showActualValues){
                labelFormat = "{2} ${1}";
            } else {
                labelFormat = "{2}";
            }
            StandardPieItemLabelGenerator pieLabelGenerator = new StandardPieItemLabelGenerator(labelFormat);
            piePlot.setLabelGenerator(pieLabelGenerator);

            tempChart = new JFreeChart(chartTitle, JFreeChart.DEFAULT_TITLE_FONT, piePlot, false);
            // Since a multiple chart is made up of several pie charts, the background color
            // has to be set for each pie chart here
            tempChart.setBackgroundPaint(backgroundColor);
            
            // Each pie charts title in the multiple pie format looks like it should be
            // included with the "foreground", so we'll change the color here and
            // reset the title in the chart
            TextTitle pieTextTitle = tempChart.getTitle();
            pieTextTitle.setPaint(foregroundColor);
            tempFont = pieTextTitle.getFont();
            pieTextTitle.setFont(new Font("pieFont",foregroundFontStyle,tempFont.getSize()));
            tempChart.setTitle(pieTextTitle);
            
            multiplePiePlot.setPieChart(tempChart);
            // set background color of overall multiple pie chart
            multiplePiePlot.setBackgroundPaint(backgroundColor);
            plot = multiplePiePlot;
        } else {
	        CategoryPlot categoryPlot = (CategoryPlot) oldPlot;
	        // Modify "foreground" color and font style
	        // "tickmarks" are the lines that show intervals in the chart
	        // "ticklabels" are the descriptions of the point labels
	        // "labels" are the x/y axis labels
		    CategoryAxis domainAxis = categoryPlot.getDomainAxis();
		    ValueAxis rangeAxis = categoryPlot.getRangeAxis();
		    
		    domainAxis.setTickLabelPaint(foregroundColor);
		    tempFont = domainAxis.getTickLabelFont();
		    domainAxis.setTickLabelFont(new Font("dTickLabelFont",foregroundFontStyle,tempFont.getSize()));
		    domainAxis.setTickMarkPaint(foregroundColor);
		    domainAxis.setLabelPaint(foregroundColor);
		    tempFont = domainAxis.getLabelFont();
		    domainAxis.setLabelFont(new Font("dLabelFont",foregroundFontStyle,tempFont.getSize()));
		    
		    rangeAxis.setTickLabelPaint(foregroundColor);
		    tempFont = rangeAxis.getTickLabelFont();
		    rangeAxis.setTickLabelFont(new Font("rTickLabelFont",foregroundFontStyle,tempFont.getSize()));
		    rangeAxis.setTickMarkPaint(foregroundColor);
		    rangeAxis.setLabelPaint(foregroundColor);
		    tempFont = rangeAxis.getLabelFont();
		    rangeAxis.setLabelFont(new Font("rLabelFont",foregroundFontStyle,tempFont.getSize()));
		    
		    
		    // Show values
	        AbstractCategoryItemRenderer renderer = (AbstractCategoryItemRenderer) categoryPlot.getRenderer();
	        // You have to set the label generator to show the values, even though the method is at the renderer level
	        StandardCategoryLabelGenerator labelGenerator = new StandardCategoryLabelGenerator();
	        renderer.setLabelGenerator(labelGenerator);
	        renderer.setItemLabelsVisible(showActualValues);
	        categoryPlot.setRenderer(renderer);
	        // Set background color
	        categoryPlot.setBackgroundPaint(chartAreaBackgroundColor);
	        plot = categoryPlot;
        }
        plot.setNoDataMessage("No Data Available");
        return plot;
    }
    /**
     * @return Returns the foregroundColor.
     */
    public Color getForegroundColor() {
        return foregroundColor;
    }
    /**
     * @param foregroundColor The foregroundColor to set.
     */
    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }
    public void setForegroundColor(String foregroundColorString){
        this.foregroundColor = getColorObject(foregroundColorString);
    }
    /**
     * @return Returns the showActualValues.
     */
    public boolean isShowActualValues() {
        return showActualValues;
    }
    /**
     * @param showActualValues The showActualValues to set.
     */
    public void setShowActualValues(boolean showActualValues) {
        this.showActualValues = showActualValues;
    }
    /**
     * @return Returns the chartAreaBackground.
     */
    public Color getChartAreaBackgroundColor() {
        return chartAreaBackgroundColor;
    }
    /**
     * @param chartAreaBackground The chartAreaBackground to set.
     */
    public void setChartAreaBackgroundColor(Color chartAreaBackgroundColor) {
        this.chartAreaBackgroundColor = chartAreaBackgroundColor;
    }
    public void setChartAreaBackgroundColor(String chartAreaBackgroundColorString){
        this.chartAreaBackgroundColor = getColorObject(chartAreaBackgroundColorString);
    }
    /**
     * @return Returns the titleColor.
     */
    public Color getTitleColor() {
        return titleColor;
    }
    /**
     * @param titleColor The titleColor to set.
     */
    public void setTitleColor(Color titleColor) {
        this.titleColor = titleColor;
    }
    public void setTitleColor(String titleColorString){
        this.titleColor = getColorObject(titleColorString);
    }
    /**
     * @return Returns the titleFontSize.
     */
    public int getTitleFontSize() {
        return titleFontSize;
    }
    /**
     * @param titleFontSize The titleFontSize to set.
     */
    public void setTitleFontSize(int titleFontSize) {
        this.titleFontSize = titleFontSize;
    }
    /**
     * @return Returns the legendFontSize.
     */
    public int getLegendFontSize() {
        return legendFontSize;
    }
    /**
     * @param legendFontSize The legendFontSize to set.
     */
    public void setLegendFontSize(int legendFontSize) {
        this.legendFontSize = legendFontSize;
    }
    
    // This method returns an ArrayList of available Font Styles in String objects
    // This list should match up with the Strings in getFontStyle()
    public static ArrayList getAvailableFontStyles(){
        ArrayList fontList = new ArrayList();
        fontList.add("Bold");
        fontList.add("Italic");
        fontList.add("Plain");
        
        return fontList;
    }
    
    // This method returns the appropriate Font static field based on the String fontStyle
    // The Strings in the conditions should match up with the options in getAvailableFontStyles()
    private int getFontStyle(String fontStyle){
        if (fontStyle.equalsIgnoreCase("Bold")){
            return Font.BOLD;
        } else if (fontStyle.equalsIgnoreCase("Italic")){
            return Font.ITALIC;
        } else if (fontStyle.equalsIgnoreCase("Plain")){
            return Font.PLAIN;
        }
        
        // Default to Plain
        return Font.PLAIN;
    }
    /**
     * @return Returns the titleFontStyle.
     */
    public int getTitleFontStyle() {
        return titleFontStyle;
    }
    /**
     * @param titleFontStyle The titleFontStyle to set.
     */
    public void setTitleFontStyle(int titleFontStyle) {
        this.titleFontStyle = titleFontStyle;
    }
    public void setTitleFontStyle(String titleFontStyleString){
        this.titleFontStyle = getFontStyle(titleFontStyleString);
    }
    /**
     * @return Returns the legendFontStyle.
     */
    public int getLegendFontStyle() {
        return legendFontStyle;
    }
    /**
     * @param legendFontStyle The legendFontStyle to set.
     */
    public void setLegendFontStyle(int legendFontStyle) {
        this.legendFontStyle = legendFontStyle;
    }
    public void setLegendFontStyle(String legendFontStyleString){
        this.legendFontStyle = getFontStyle(legendFontStyleString);
    }
    /**
     * @return Returns the foregroundFontStyle.
     */
    public int getForegroundFontStyle() {
        return foregroundFontStyle;
    }
    /**
     * @param foregroundFontStyle The foregroundFontStyle to set.
     */
    public void setForegroundFontStyle(int foregroundFontStyle) {
        this.foregroundFontStyle = foregroundFontStyle;
    }
    public void setForegroundFontStyle(String foregroundFontStyleString){
        this.foregroundFontStyle = getFontStyle(foregroundFontStyleString);
    }
}
