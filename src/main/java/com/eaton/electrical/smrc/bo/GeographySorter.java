/*
 * GeographySorter.java
 *
 * Created on October 26, 2004, 12:51 PM 
 */

package com.eaton.electrical.smrc.bo;

import java.util.*;

/**
 *
 * @author  Jason Lubbert
 */
public class GeographySorter implements Comparator {
    
	private static final long serialVersionUID = 100;

	/** Creates a new instance of GeographySorter */
    public GeographySorter() {
    }
    
    public int compare(Object a, Object b){
        Geography geog1 = (Geography) a;
        Geography geog2 = (Geography) b;
        
        String comp1 = geog1.getGeog();
        String comp2 = geog2.getGeog();
        // return results in descending order
        
        if (comp1.compareTo(comp2) > 0){
            return 1;
        } else if (comp1.compareTo(comp2) < 0){
            return -1;
        } else {
            return 0;
        }
       
    }
    
    
}