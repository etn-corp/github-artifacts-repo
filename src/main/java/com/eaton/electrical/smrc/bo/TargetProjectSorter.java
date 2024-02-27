/*
 * TargetProjectSorter
 *
 * Created on November 12, 2004 
 */

package com.eaton.electrical.smrc.bo;

import java.util.*;
import com.eaton.electrical.smrc.util.*;

/**
 *
 * @author  Jason Lubbert
 */
public class TargetProjectSorter implements Comparator {
    
	private static final long serialVersionUID = 100;

	/** Creates a new instance of TargetProjectSorter */
    public TargetProjectSorter() {
    }
    
    public int compare(Object a, Object b){
        TargetProjectReportBean bean1 = (TargetProjectReportBean) a;
        TargetProjectReportBean bean2 = (TargetProjectReportBean) b;
        String classType = bean1.getSortFieldType();
        
        if (classType.indexOf("String") > -1){
        	String comp1 = StringManipulation.noNull((String) bean1.getSortField());
        	String comp2 = StringManipulation.noNull((String) bean2.getSortField());
//        	 return results in ascending order
            if (comp1.compareTo(comp2) > 0){
                return 1;
            } else if (comp1.compareTo(comp2) < 0){
                return -1;
            } else {
                return 0;
            }
        } else if (classType.indexOf("Date") > -1){
        	java.util.Date comp1 = null;
        	java.util.Date comp2 = null;
        	if (bean1.getSortField() == null){
        		// Set date to 1975 to push it to the back of the list in sorting
        		comp1 = Globals.getDate("1975", "01", "01");
        	} else {
        		comp1 = (java.util.Date) bean1.getSortField();
        	}
        	if (bean2.getSortField() == null){
        		comp2 = Globals.getDate("1975", "01", "01");
        	} else {
        		comp2 = (java.util.Date) bean2.getSortField();
        	}
        	//        	 return results in ascending order
            if (comp1.compareTo(comp2) < 0){
                return 1;
            } else if (comp1.compareTo(comp2) > 0){
                return -1;
            } else {
                return 0;
            }
        } else if (classType.indexOf("Double") > -1){
        	Double comp1 = (Double) bean1.getSortField();
        	Double comp2 = (Double) bean2.getSortField();
//        	 return results in descending order
            if (comp1.compareTo(comp2) < 0){
                return 1;
            } else if (comp1.compareTo(comp2) > 0){
                return -1;
            } else {
                return 0;
            }
        } else {
        	return 1;
        }
        
       
    }
    
    
}