/*
 * ReportHeaderSorter.java
 *
 * Created on August 11, 2004, 8:49 AM
 */

package com.eaton.electrical.smrc.bo;

import java.util.*;

/**
 *
 * @author  Jason Lubbert
 */
public class CMSReportHeaderSorter implements Comparator {
    
    /** Creates a new instance of ReportHeaderSorter */
    public CMSReportHeaderSorter() {
    }
    
    public int compare(Object a, Object b){
        CMSReportHeader sr1 = (CMSReportHeader) a;
        CMSReportHeader sr2 = (CMSReportHeader) b;
        
        int comp1 = sr1.getSequence();
        int comp2 = sr2.getSequence();
        // return results in descending order
        
        if (comp1 < comp2){
            return -1;
        } else if (comp1 > comp2){
            return 1;
        } else {
            return 0;
        }
       
    }
    
    
}
