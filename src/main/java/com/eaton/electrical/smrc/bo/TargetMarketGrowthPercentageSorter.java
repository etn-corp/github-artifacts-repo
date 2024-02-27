/*
 * TargetMarketGrowthPercentageSorter.java
 *
 * Created on March 9, 2005, 1:49 AM
 */

package com.eaton.electrical.smrc.bo;

import java.util.*;

/**
 *
 * @author  Jason Lubbert
 */
public class TargetMarketGrowthPercentageSorter implements Comparator {
    
    /** Creates a new instance of TargetMarketGrowthPercentageSorter */
    public TargetMarketGrowthPercentageSorter() {
    }
    
    public int compare(Object a, Object b){
        TargetMarketSortingBean bean1 = (TargetMarketSortingBean) a;
        TargetMarketSortingBean bean2 = (TargetMarketSortingBean) b;
        
        double growth1 = bean1.getGrowthPercentage();
        double growth2 = bean2.getGrowthPercentage();
        double payout1 = bean1.getPayoutPercentage();
        double payout2 = bean2.getPayoutPercentage();
        // return results in ascending order
        if (growth1 < growth2){
            return -1;
        } else if (growth1 > growth2){
            return 1;
        } else {
            // If the growths are the same, sort by payout. This will prevent 
            // problems in calculating payouts where less than 4 growth/payouts are entered.
            if (payout1 < payout2){
                return -1;
            } else if (payout1 > payout2){
                return 1;
            } else {
                return 0;
            }
        }
       
    }
    
    
}