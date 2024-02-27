/*
 * Created on Mar 28, 2005
 *
 */
package com.eaton.electrical.smrc.bo;

import java.sql.*;
import java.util.*;
import org.jfree.data.category.*;

import com.eaton.electrical.smrc.service.SMRCConnectionPoolUtils;
import com.eaton.electrical.smrc.service.SMRCLogger;

/**
 * @author Jason Lubbert
 * 
 * This class takes data from the ChartQueryBean and creates charting datasets. Currently,
 * there is only a method for creating DefaultCategoryDataset objects, but it may be 
 * necessary to add more if different chart types are implemented in SMRCChart. A ChartQueryBean
 * has to be set before any type of dataset can be returned.
 * 
 * seriesLabel and pointLabels are column labels from sql results. The data in the series
 * column are typically the descriptions that appear in the chart's legend. The data in 
 * the pointLabels columns are the values to be charted.
 * 
  * */

public class SMRCChartData implements java.io.Serializable {
    
    private String seriesLabel = null;
    private ArrayList pointLabels = null;
    private ChartQueryBean chartQueryBean = null;
    private String theQuery = null;
    
	private static final long serialVersionUID = 100;
       
    public SMRCChartData(){
        // empty constructor
    }
    
    public SMRCChartData(ChartQueryBean queryBean){
        setChartQueryBean(queryBean);
    }
    
    // This method returns the data retrieved from the execution of the ChartQueryBean's
    // sql statement in a DefaultCategoryDataset object.
    public DefaultCategoryDataset getCategoryDataset(Connection DBConn) throws Exception{
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ResultSet rs = null;
        Statement stmt = null;
        try {
            stmt = DBConn.createStatement();
            SMRCLogger.debug("SMRCChartData.getCategoryDataset(): SQL Query " + theQuery);
            rs = stmt.executeQuery(theQuery);
            while (rs.next()){
                String series = rs.getString(seriesLabel);
                for (int i=0; i< pointLabels.size(); i++){
                    String pointLabel = (String) pointLabels.get(i);
                    dataset.addValue(rs.getDouble(pointLabel),series,pointLabel);
                }
                
            }
            
        }catch (Exception e)	{
        	SMRCLogger.error("SMRChartData SQL: " + theQuery);
			SMRCLogger.error("SMRCChartData.getCategoryDataset(): " , e);
			throw e;
		} finally {
		    SMRCConnectionPoolUtils.close(rs);
		    SMRCConnectionPoolUtils.close(stmt);
		}
        
        return dataset;
        
    }
    
          
    
    /**
     * @return Returns the chartQueryBean.
     */
    public ChartQueryBean getChartQueryBean() {
        return chartQueryBean;
    }
    /**
     * @param chartQueryBean The chartQueryBean to set.
     */
    public void setChartQueryBean(ChartQueryBean chartQueryBean) {
        this.chartQueryBean = chartQueryBean;
        this.seriesLabel = this.chartQueryBean.getSeriesLabel();
        this.pointLabels = this.chartQueryBean.getPointLabels();
        this.theQuery = this.chartQueryBean.getTheQuery();
    }
    /**
     * @return Returns the pointLabels.
     */
    public ArrayList getPointLabels() {
        return pointLabels;
    }
    /**
     * @param pointLabels The pointLabels to set.
     */
    public void setPointLabels(ArrayList pointLabels) {
        this.pointLabels = pointLabels;
    }
    /**
     * @return Returns the seriesLabel.
     */
    public String getSeriesLabel() {
        return seriesLabel;
    }
    /**
     * @param seriesLabel The seriesLabel to set.
     */
    public void setSeriesLabel(String seriesLabel) {
        this.seriesLabel = seriesLabel;
    }
    /**
     * @return Returns the theQuery.
     */
    public String getTheQuery() {
        return theQuery;
    }
    /**
     * @param theQuery The theQuery to set.
     */
    public void setTheQuery(String theQuery) {
        this.theQuery = theQuery;
    }
}
