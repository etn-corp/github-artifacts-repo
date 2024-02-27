/*
 * StandardReportHeader.java
 *
 * Created on September 2, 2004, 4:22 PM
 */

package com.eaton.electrical.smrc.bo;

/**
 *
 * @author  Jason Lubbert
 */
public class StandardReportHeader extends Object implements java.io.Serializable {
    
    int sequence = 0;
    String reportHeader = null;
    String sqlColumnName = null;
    int columnIndex = 0;
    boolean description = false;
    boolean id = false;
    boolean description2 = false;
    boolean id2 = false;
    
	private static final long serialVersionUID = 100;
    
    /** Creates a new instance of StandardReportHeader */
    public StandardReportHeader() {
    }
    
    public int getSequence(){
        return this.sequence;
    }
    
    public String getReportHeader(){
        return this.reportHeader;
    }
    
    public int getColumnIndex (){
        return this.columnIndex;
    }
    
    public String getSQLColumnName(){
        return sqlColumnName;
    }
    
    public void assignHeaderAndSequence(String columnName, int columnIndex){
         this.sqlColumnName = columnName;
         this.columnIndex = columnIndex;
                 
         if (columnName.equalsIgnoreCase("id") || columnName.equalsIgnoreCase("id2")){
             this.sequence = 0;
             if (columnName.equalsIgnoreCase("id")){
                 id = true;
             }
             if (columnName.equalsIgnoreCase("id2")){
                 id2 = true;
             }
         } else if (columnName.equalsIgnoreCase("description") || columnName.equalsIgnoreCase("description2")){
             this.sequence = 1;
             if (columnName.equalsIgnoreCase("description")){
                 description = true;
             }
             if (columnName.equalsIgnoreCase("description2")){
                 description2 = true;
             }
         } else if (columnName.equalsIgnoreCase("Potential $")){
             this.reportHeader = columnName;
             this.sequence = 2;
         } else if (columnName.equalsIgnoreCase("Forecast $")){
         	 this.reportHeader = columnName;
             this.sequence = 3;
         } else if (columnName.equalsIgnoreCase("Competitor $")){
         	this.reportHeader = columnName;
             this.sequence = 4;
         } else if (columnName.equalsIgnoreCase("Tap Month Invoice")){
         	this.reportHeader = columnName;
             this.sequence = 6;
         } else if (columnName.equalsIgnoreCase("Tap YTD Invoice")){
         	this.reportHeader = columnName;
             this.sequence = 7;
         } else if (columnName.equalsIgnoreCase("Tap Prev YTD Invoice")){
         	this.reportHeader = columnName;
             this.sequence = 8;
         } else if (columnName.equalsIgnoreCase("Tap Prev Year Invoice")){
         	this.reportHeader = columnName;
             this.sequence = 9;
         } else if (columnName.equalsIgnoreCase("Tap Prev YR Month Invoice")){
         	this.reportHeader = columnName;
             this.sequence = 10;
         } else if (columnName.equalsIgnoreCase("Tap Month Order")){
         	this.reportHeader = columnName;
             this.sequence = 11;
         } else if (columnName.equalsIgnoreCase("Tap YTD Order")){
         	this.reportHeader = columnName;
             this.sequence = 12;
         } else if (columnName.equalsIgnoreCase("Tap Prev YTD Order")){
         	this.reportHeader = columnName;
             this.sequence = 13;
         } else if (columnName.equalsIgnoreCase("Tap Prev Year Order")){
         	 this.reportHeader = columnName;
             this.sequence = 14;
         } else if (columnName.equalsIgnoreCase("Tap Prev YR Month Order")){
         	this.reportHeader = columnName;
             this.sequence = 15;
         } else if (columnName.equalsIgnoreCase("End Mrkt Month Sales")){
         	this.reportHeader = columnName;
             this.sequence = 16;
         } else if (columnName.equalsIgnoreCase("End Mrkt YTD Sales")){
         	this.reportHeader = columnName;
             this.sequence = 17;
         } else if (columnName.equalsIgnoreCase("End Mrkt Prev YTD Sales")){
         	this.reportHeader = columnName;
             this.sequence = 18;
         } else if (columnName.equalsIgnoreCase("End Mrkt Prev Year Sales")){
         	this.reportHeader = columnName;
             this.sequence = 19;
         } else if (columnName.equalsIgnoreCase("End Mrkt Prev YR Month Sales")){
         	 this.reportHeader = columnName;
             this.sequence = 20;
         } else if (columnName.equalsIgnoreCase("End Mrkt Month Order")){
         	 this.reportHeader = columnName; 
             this.sequence = 21;
         } else if (columnName.equalsIgnoreCase("End Mrkt YTD Order")){
         	 this.reportHeader = columnName;
             this.sequence = 22;
         } else if (columnName.equalsIgnoreCase("End Mrkt Prev YTD Order")){
         	 this.reportHeader = columnName;
             this.sequence = 23;
         } else if (columnName.equalsIgnoreCase("End Mrkt Prev Year Order")){
         	 this.reportHeader = columnName;
             this.sequence = 24;
         } else if (columnName.equalsIgnoreCase("End Mrkt Prev YR Month Order")){
         	 this.reportHeader = columnName;
             this.sequence = 25;
         } else if (columnName.equalsIgnoreCase("Charge Month Sales")){
         	 this.reportHeader = columnName;
             this.sequence = 26;
         } else if (columnName.equalsIgnoreCase("Charge YTD Sales")){
         	 this.reportHeader = columnName;
             this.sequence = 27;
         } else if (columnName.equalsIgnoreCase("Charge Prev YTD Sales")){
         	 this.reportHeader = columnName;
             this.sequence = 28;
         } else if (columnName.equalsIgnoreCase("Charge Prev Year Sales")){
         	 this.reportHeader = columnName;
             this.sequence = 29;
         } else if (columnName.equalsIgnoreCase("Charge Prev YR Month Sales")){
         	 this.reportHeader = columnName;
             this.sequence = 30;
         } else if (columnName.equalsIgnoreCase("Charge Month Order")){
         	 this.reportHeader = columnName;
             this.sequence = 31;
         } else if (columnName.equalsIgnoreCase("Charge YTD Order")){
         	 this.reportHeader = columnName;
             this.sequence = 32;
         } else if (columnName.equalsIgnoreCase("Charge Prev YTD Order")){
         	 this.reportHeader = columnName;
             this.sequence = 33;
         } else if (columnName.equalsIgnoreCase("Charge Prev Year Order")){
         	 this.reportHeader = columnName;
             this.sequence = 34;
         } else if (columnName.equalsIgnoreCase("Charge Prev YR Month Order")){
         	 this.reportHeader = columnName;
             this.sequence = 35;
         } else if (columnName.equalsIgnoreCase("rownumber")){
             this.sequence = 99;
         }
         
    }
    
    public boolean isDescription (){
        return description;
    }
    
    public boolean isId (){
        return id;
    }
    
    public boolean isDescription2 (){
        return description2;
    }
    
    public boolean isId2 (){
        return id2;
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

		returnString += "\tsequence = " + this.getSequence() + "\n";
		returnString += "\treportHeader = " + this.getReportHeader() + "\n";
		returnString += "\tcolumnIndex  = " + this.getColumnIndex () + "\n";
		returnString += "\tsQLColumnName = " + this.getSQLColumnName() + "\n";

		return returnString;
	}
}
