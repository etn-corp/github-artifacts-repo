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
public class CMSReportHeader extends Object implements java.io.Serializable {
    
    int sequence = 0;
    String reportHeader = null;
    String sqlColumnName = null;
    int columnIndex = 0;
    
	private static final long serialVersionUID = 100;
    
    /** Creates a new instance of StandardReportHeader */
    public CMSReportHeader() {
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
         
         // There are only three columns to display.  We just need to make sure that these
         // two are in order.  Greater than 99 is for values that are not doubles? (maybe not display values)
         
         
         
         if (columnName.equalsIgnoreCase("vendor_id")){
             this.reportHeader = columnName;
             this.sequence = 100;

         } else if (columnName.equalsIgnoreCase("Competitor")){
             this.reportHeader = columnName;
             this.sequence = 5;

         } else if (columnName.equalsIgnoreCase("Potential")){
             this.reportHeader = columnName;
             this.sequence = 4;
             
         // All the following have a sequence of 1.  They are all mutually exclusive.  We are just putting
         // the if statements in for nice names to be outputted
         } else if (columnName.equalsIgnoreCase("vista_customer_number")){
             this.reportHeader = "Vista Customer Number";
             this.sequence = 1;
             
         } else if (columnName.equalsIgnoreCase("parent_num")){
             this.reportHeader = "Parent Vista Number";
             this.sequence = 1;
             
         } else if (columnName.equalsIgnoreCase("district")){
             this.reportHeader = "District";
             this.sequence = 1;
             
         } else if (columnName.equalsIgnoreCase("zone")){
             this.reportHeader = "Zone";
             this.sequence = 1;
             
         } else if (columnName.equalsIgnoreCase("team")){
             this.reportHeader = "Team";
             this.sequence = 1;
             
         } else if (columnName.equalsIgnoreCase("primary")){
             this.reportHeader = "Primary Segments";
             this.sequence = 1;
             
         } else if (columnName.equalsIgnoreCase("secondary")){
             this.reportHeader = "Secondary Segments";
             this.sequence = 1;
             
         } else if (columnName.equalsIgnoreCase("product_group")){
             this.reportHeader = "Product Id";
             this.sequence = 1;

         } else if (columnName.equalsIgnoreCase("division_description")){
             this.reportHeader = "Division Description";
             this.sequence = 1;

         } else if (columnName.equalsIgnoreCase("sales_engineer")){
             this.reportHeader = "Sales Engineer";
             this.sequence = 1;

         } else {
        	 // This is whatever the group by value name was
         	 this.reportHeader = columnName;
             this.sequence = 1;
         }
         
         
         
         
         
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
