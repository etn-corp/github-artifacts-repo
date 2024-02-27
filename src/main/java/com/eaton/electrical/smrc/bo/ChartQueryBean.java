/*
 * Created on Mar 28, 2005
 *
 */
package com.eaton.electrical.smrc.bo;

import java.util.*;
/**
 * @author Jason Lubbert
 * This class holds query information to be used in the creation of charts by SMRCChart.
 * seriesLabel and pointLabels are column labels from sql results. The data in the series
 * column are typically the descriptions that appear in the chart's legend. The data in 
 * the pointLabels columns are the values to be charted.
*/
public class ChartQueryBean implements java.io.Serializable {
    
       
    private String seriesLabel = null;
    private ArrayList pointLabels = null;
    private String theQuery = null;
    
    
	private static final long serialVersionUID = 100;

	public ChartQueryBean(){
        // empty constructor
        seriesLabel = "";
        pointLabels = new ArrayList();
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
    
    public void addPointLabels(String pointLabel){
        pointLabels.add(pointLabel);
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
