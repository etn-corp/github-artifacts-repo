/*
 * Created on Mar 9, 2005
 *
  */
package com.eaton.electrical.smrc.bo;

/**
 * @author Jason Lubbert
 *
 */
public class TargetMarketSortingBean implements java.io.Serializable {
    
    double growthPercentage = 0;
    double payoutPercentage = 0;
    
	private static final long serialVersionUID = 100;

	public TargetMarketSortingBean(){
         growthPercentage = 0;
         payoutPercentage = 0;
    }
    
    public TargetMarketSortingBean(double growthPercentage, double payoutPercentage){
        this.growthPercentage = growthPercentage;
        this.payoutPercentage = payoutPercentage;
    }

    /**
     * @return Returns the growthPercentage.
     */
    public double getGrowthPercentage() {
        return growthPercentage;
    }
    /**
     * @param growthPercentage The growthPercentage to set.
     */
    public void setGrowthPercentage(double growthPercentage) {
        this.growthPercentage = growthPercentage;
    }
    /**
     * @return Returns the payoutPercentage.
     */
    public double getPayoutPercentage() {
        return payoutPercentage;
    }
    /**
     * @param payoutPercentage The payoutPercentage to set.
     */
    public void setPayoutPercentage(double payoutPercentage) {
        this.payoutPercentage = payoutPercentage;
    }
}
