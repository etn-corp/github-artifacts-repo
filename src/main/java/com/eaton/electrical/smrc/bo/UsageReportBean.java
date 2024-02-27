/*
 * Created on Feb 14, 2005
 *
 */
package com.eaton.electrical.smrc.bo;

/**
 * @author Jason Lubbert
 *
 */
public class UsageReportBean implements java.io.Serializable {
    
    String description = null;
    int pageViews = 0;
    
	private static final long serialVersionUID = 100;

	public UsageReportBean(){
        description = "";
        
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return Returns the pageViews.
     */
    public int getPageViews() {
        return pageViews;
    }
    /**
     * @param pageViews The pageViews to set.
     */
    public void setPageViews(int pageViews) {
        this.pageViews = pageViews;
    }
}
