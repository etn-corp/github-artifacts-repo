
package com.eaton.electrical.smrc.bo;

import java.util.*;


/**	This class contains all of the methods and attributes needed by the Target Account Planner
*	as needed
*
*	@author Carl Abel
*/
public class StandardReportSearchResults extends Object implements java.io.Serializable {
    
        ArrayList headings = new ArrayList();
        ArrayList textFields = new ArrayList(); 
        ArrayList dollarFields = new ArrayList(); 
        ArrayList resultObjects = new ArrayList();
        
    
    	private static final long serialVersionUID = 100;

    	public StandardReportSearchResults(){
        }
        
        // Only used for the headings of the search results
        public void addToHeadings(String header){
            headings.add(header);
         
        }
        
        // Text fields returned by the query - customer #, name, etc.
        public void addToTextFields(String textfield){
            textFields.add(textfield);
        }
        
        // Numeric fields returned by the query - potential $, sales goals, etc.
        public void addToDollarFields(Double dollarfield){
            dollarFields.add(dollarfield) ;           
        }
        
        public void addToResultObjects (Object resultField){
            resultObjects.add(resultField);
        }
        
        public ArrayList getHeadings(){
            return headings;
        }
        
        public ArrayList getTextFields(){
            return textFields;
        }
        
        public ArrayList getDollarFields(){
            return dollarFields;
        }
        
        public ArrayList getResultObjects(){
            return resultObjects;
        }
        
    


	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public boolean equals(Object obj) {
		if (this == null || obj == null) {
			return false;
		}

		return super.equals(obj);
	}

	public String toString() {
		String returnString = "";

		returnString += "\theadings = " + this.getHeadings() + "\n";
		returnString += "\ttextFields = " + this.getTextFields() + "\n";
		returnString += "\tdollarFields = " + this.getDollarFields() + "\n";
		returnString += "\tresultObjects = " + this.getResultObjects() + "\n";

		return returnString;
	}
}
