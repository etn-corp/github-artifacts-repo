/*
 * TargetMarketReportSorter.java
 *
 * Created on October 24, 2004, 5:01 PM
 */

package com.eaton.electrical.smrc.bo;

import java.util.*;

/**
 *
 * @author  Jason Lubbert
 */
public class TargetMarketReportSorter implements Comparator {
    
	/** Creates a new instance of TargetMarketReportSorter */
    public TargetMarketReportSorter() {
    }
    
    public int compare(Object a, Object b){
        TargetMarketReportBean tmrb1 = (TargetMarketReportBean) a;
        TargetMarketReportBean tmrb2 = (TargetMarketReportBean) b;
        
        double comp1 = tmrb1.getSortField();
        double comp2 = tmrb2.getSortField();
        // return results in descending order
        
        if (comp1 < comp2){
            return 1;
        } else if (comp1 > comp2){
            return -1;
        } else {
            return 0;
        }
       
    }
    
    
}