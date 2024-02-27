/*
 * StandardReportSQLbuilder.java
 *
 * Created on July 12, 2004, 8:27 AM
 */

package com.eaton.electrical.smrc.dao;

import java.sql.*;
import java.util.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.exception.SMRCException;
import com.eaton.electrical.smrc.util.*;


/**
 *
 * @author  Jason Lubbert
 */
public class CMSSQLbuilder extends SQLBuilder {

    
    int month = 0; 
    int year = 0;
    
    boolean groupByAccount = false;
    boolean groupByParent = false;
    boolean groupByDistrict = false;
    boolean groupByTeam = false;
    boolean groupByZone = false;
    boolean groupByPrimarySegment = false;
    boolean groupBySecondarySegment = false;
    boolean groupByProduct = false;
    boolean groupByDivision = false;
    boolean groupBySE = false;
  
    
    // If this is false, then oracle will do a hint to materialize
    // the temporary table.  For right now this is set conservative (don't 
    // materialize).  After we see it work for a while, we might change the default
    
    boolean isFiltered = true;          
    
    boolean productFilter = false;
    ArrayList productValues = new ArrayList();
    boolean divisionFilter = false;
    ArrayList divisionValues = new ArrayList();
    boolean geographyFilter = false;
    ArrayList geographyValues = new ArrayList();
    boolean focusTypeFilter = false;
    ArrayList focusTypeValues = new ArrayList();
    boolean specialProgramFilter = false;
    ArrayList specialProgramValues = new ArrayList();
    boolean applicationFilter = false;
    ArrayList applicationValues = new ArrayList();
    boolean segmentFilter = false;
    ArrayList segmentValues = new ArrayList();
    
    boolean targetAccounts = false;
    
    int srYear = 0;
    int srMonth = 0;
      
       
    /** Creates a new instance of AcctPlanSQLbuilder */
    public CMSSQLbuilder(String month, String year) {
        this.month = Globals.a2int( month );
        this.year = Globals.a2int( year );
    }
    
   
    
    //** Returns reporting year
    public int getYear(){
        return this.year;
    }
    //** Returns reporting month
    public int getMonth(){
        return this.month;
    }
    
    //** Set the year according to sales reporting
    public void setSRYear(int srYear){
    	this.srYear = srYear;
    }
    
    //** Set the month according to sales reporting
    public void setSRMonth(int srMonth){
    	this.srMonth = srMonth;
    }
    
    //** Sets flag to only look for target accounts
    public void targetAccountsOnly(){
        targetAccounts = true;
    }

     
    //** sets groupByAccount to true
    public void setGroupByAccount (){
        groupByAccount = true;
    }
    //** sets groupByParent to true
    public void setGroupByParent (){
        groupByParent = true;
    }
    //** sets groupByDistrict to true
    public void setGroupByDistrict (){
        groupByDistrict = true;
    }
    //** sets groupByTeam to true
    public void setGroupByTeam (){
        groupByTeam = true;
    }
    //** sets groupByZone to true
    public void setGroupByZone (){
        groupByZone = true;
    }
    //** sets groupByPrimarySegment to true
    public void setGroupByPrimarySegment (){
        groupByPrimarySegment = true;
    }
    //** sets groupBySecondarySegment to true
    public void setGroupBySecondarySegment (){
        groupBySecondarySegment = true;
    }
    //** sets groupByProduct to true
    public void setGroupByProduct (){
        groupByProduct = true;
    }
    //** sets groupByDivision to true
    public void setGroupByDivision (){
        groupByDivision = true;
    }
    //** sets groupBySE to true
    public void setGroupBySE (){
        groupBySE = true;
    }
    

    //** Use product filter and populate values to use in filter
    public void useProductFilter (ArrayList filterValues){
        productFilter = true;
        for (int i=0; i < filterValues.size(); i++){
            productValues.add(filterValues.get(i));
        }
    }
    
    //** Use division and populate values to use in filter    
    public void useDivisionFilter (ArrayList filterValues){
        divisionFilter = true;
        for (int i=0; i < filterValues.size(); i++){
            divisionValues.add(filterValues.get(i));
        }
    }
    
    //** Use geography filter and populate values to use in filter
    public void useGeographyFilter (ArrayList filterValues){
        geographyFilter = true;
        for (int i=0; i < filterValues.size(); i++){
            geographyValues.add(filterValues.get(i));
        }
        
    }    
    
    //** Use focus type filter and populate values to use in filter    
    public void useFocusTypeFilter (ArrayList filterValues){
        focusTypeFilter = true;
        for (int i=0; i < filterValues.size(); i++){
            focusTypeValues.add(filterValues.get(i));
        }
    }

    //** Use special programs filter and populate values to use in filter
    public void useSpecialProgramFilter (ArrayList filterValues){
        specialProgramFilter = true;
        for (int i=0; i < filterValues.size(); i++){
            specialProgramValues.add(filterValues.get(i));
        }
        
    }   
    
    //** Use application filter and populate values to use in filter
    public void useApplicationFilter (ArrayList filterValues){
        applicationFilter = true;
        for (int i=0; i < filterValues.size(); i++){
            applicationValues.add(filterValues.get(i));
        }
        
    }    

    //** Use segment filter and populate values to use in filter
    public void useSegmentFilter (ArrayList filterValues){
        segmentFilter = true;
        for (int i=0; i < filterValues.size(); i++){
            segmentValues.add(filterValues.get(i));
        }
        
    }    
       
/*    public void setIsFiltered(boolean isFiltered) {   	
    	this.isFiltered = isFiltered;    	
   		this.setWithHintMaterialze(0, !isFiltered);
    }*/
    
    //** return if the filter is set  */  
    public boolean isFiltered() {  
    	return isFiltered;    
    }   
        
    public String returnSQL (Connection DBConn) throws Exception{
    	
    	
    	// Format of SQL:  Here is an example ->
    	
/*    	select vista_customer_number, Competitor_name, sum(nvl(Competitor, 0)), sum(nvl(Potential, 0)) from (

    			(select cust_prod.vista_customer_number, cust_prod.competitor1_id Competitor, vend.description Competitor_name, cust_prod.competitor_dollars, cust_prod.potential_dollars Potential

    			from oemapnew.customer_product cust_prod, oemapnew.products prod, vendors vend

    			where prod.period_yyyy = 2006 and cust_prod.competitor_dollars > 0.00 and prod.product_id(+) = cust_prod.product_id 
    			      and cust_prod.competitor1_id = vend.vendor_id)

    			union all

    			(select  cust_prod.vista_customer_number, cust_prod.competitor2_id, vend.description Competitor_name, cust_prod.competitor2_dollars, cust_prod.potential_dollars Potential

    			from oemapnew.customer_product cust_prod, oemapnew.products prod, vendors vend

    			where prod.period_yyyy = 2006 and cust_prod.competitor2_dollars > 0.00 and prod.product_id(+) = cust_prod.product_id  
    			
			)
			
			group by vista_customer_number, Competitor_name
			
			order by vista_customer_number


    			*/  	
    	
    	
    	// We will use three different SQL statements.  The outer select, the First select and the second select.
    	// The first and second select are necessary because the cometitor information is held in two different
    	// columns of the table.  These selects are run seperatly and then unioned together.
    	
 
/*    	select cust_prod.vista_customer_number, cust_prod.competitor1_id Competitor, vend.description Competitor_name, cust_prod.competitor_dollars, cust_prod.potential_dollars Potential

		from oemapnew.customer_product cust_prod, oemapnew.products prod, vendors vend

		where prod.period_yyyy = 2006 and cust_prod.competitor_dollars > 0.00 and prod.product_id(+) = cust_prod.product_id 
		      and cust_prod.competitor1_id = vend.vendor_id    	
*/    	
    	
    	SQLBuilder query1 = new SQLBuilder();
    	SQLBuilder query2 = new SQLBuilder();
    	

    	// Common selects
    	   	
    	query1.addToSelectFields("products.product_id, customer_product.competitor1_id, vendors.vendor_id, customer_product.competitor_dollars, customer_product.potential_dollars");
    	
    	query2.addToSelectFields("products.product_id, customer_product.competitor2_id, vendors.vendor_id, customer_product.competitor2_dollars, customer_product.potential_dollars");
    	
    	// Common from tables
    	query1.addToFromTable("customer_product");
    	query1.addToFromTable("products");
    	query1.addToFromTable("vendors");
    	
    	query2.addToFromTable("customer_product");
    	query2.addToFromTable("products");
    	query2.addToFromTable("vendors");
    	
    	//
    	// Common Where Statements
    	//
    	
    	// Limit the query to this year's products
    	
    	query1.addToWhereConditions("products.period_yyyy =" + this.year);
    	query2.addToWhereConditions("products.period_yyyy =" + this.year);
    	
    	// Join the products and customer_product table
    	
    	query1.addToWhereConditions("products.product_id(+) = customer_product.product_id");
    	query2.addToWhereConditions("products.product_id(+) = customer_product.product_id");
    	
    	// Join the vendors and customer_product table
    	
    	query1.addToWhereConditions("customer_product.competitor1_id(+) = vendors.vendor_id");
    	query2.addToWhereConditions("customer_product.competitor2_id(+) = vendors.vendor_id");
    	
    	//
    	// Definition and population of common outer query values
    	//
    	
    	String outterSelect = "vendor_id, sum(nvl(competitor_dollars, 0)) Competitor, sum(nvl(potential_dollars, 0)) Potential";
    	String outterGroupBySuffix = "vendor_id";
    	String outterGroupByPrefix = "";  // Defined now...This value will be based on the group
    	
    	// 
    	// Add the statements group by values
    	//
    	
    	if (groupByAccount) {
    	
        	query1.addToSelectFields("customer_product.vista_customer_number");
        	query2.addToSelectFields("customer_product.vista_customer_number");

        	outterSelect = "vista_customer_number, " + outterSelect;
        	outterGroupByPrefix = "vista_customer_number";    	   	

    		
    	} else if (groupByProduct) {
    		
        	query1.addToSelectFields("products.product_id product_group");
        	query2.addToSelectFields("products.product_id product_group");

        	outterSelect = "product_group, " + outterSelect;
        	outterGroupByPrefix = "product_group";    		
    		
    	} else if (groupByParent) {
    		
        	query1.addToSelectFields("parent_num");
        	query2.addToSelectFields("parent_num");
        	
        	query1.addToFromTable("customer");
        	query2.addToFromTable("customer");
        	
        	query1.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	query2.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	
        	query1.addToWhereConditions("customer.parent_num is not null");
        	query2.addToWhereConditions("customer.parent_num is not null");

        	outterSelect = "parent_num, " + outterSelect;
        	outterGroupByPrefix = "parent_num";    		
    		
    	} else if (groupByDistrict) {
    		
        	query1.addToSelectFields("substr(customer.sp_geog,1,5) district");
        	query2.addToSelectFields("substr(customer.sp_geog,1,5) district");
        	
        	query1.addToFromTable("customer");
        	query2.addToFromTable("customer");

        	query1.addToFromTable("geographies");
            query2.addToFromTable("geographies");
       	
            query1.addToWhereConditions("substr(customer.sp_geog,1,5) = geographies.sp_geog");
            query2.addToWhereConditions("substr(customer.sp_geog,1,5) = geographies.sp_geog");
        	        	
        	query1.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	query2.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	
        	query1.addToWhereConditions("customer.parent_num is not null");
        	query2.addToWhereConditions("customer.parent_num is not null");

        	outterSelect = "district, " + outterSelect;
        	outterGroupByPrefix = "district";    		
    		
    	} else if (groupByZone) {
    		
        	query1.addToSelectFields("substr(customer.sp_geog,1,4) zone");
        	query2.addToSelectFields("substr(customer.sp_geog,1,4) zone");
        	
        	query1.addToFromTable("customer");
        	query2.addToFromTable("customer");

        	query1.addToFromTable("geographies");
            query2.addToFromTable("geographies");
       	
            query1.addToWhereConditions("substr(customer.sp_geog,1,4)||'0' =  geographies.sp_geog");
            query2.addToWhereConditions("substr(customer.sp_geog,1,4)||'0' =  geographies.sp_geog");
        	        	
        	query1.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	query2.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	
        	query1.addToWhereConditions("customer.parent_num is not null");
        	query2.addToWhereConditions("customer.parent_num is not null");

        	outterSelect = "zone, " + outterSelect;
        	outterGroupByPrefix = "zone";    		
    		
    	} else if (groupByTeam) {
    		
        	query1.addToSelectFields("customer.sp_geog team");
        	query2.addToSelectFields("customer.sp_geog team");
        	
        	query1.addToFromTable("customer");
        	query2.addToFromTable("customer");

        	query1.addToFromTable("geographies");
            query2.addToFromTable("geographies");
       	
            query1.addToWhereConditions("customer.sp_geog = geographies.sp_geog");
            query2.addToWhereConditions("customer.sp_geog = geographies.sp_geog");
        	        	
        	query1.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	query2.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	
        	query1.addToWhereConditions("customer.parent_num is not null");
        	query2.addToWhereConditions("customer.parent_num is not null");

        	outterSelect = "team, " + outterSelect;
        	outterGroupByPrefix = "team";    		
    		
    	} else if (groupByPrimarySegment) {
    		
        	query1.addToSelectFields("segments.segment_name primary");
        	query2.addToSelectFields("segments.segment_name primary");
        	
        	query1.addToFromTable("customer");
        	query2.addToFromTable("customer");

        	query1.addToFromTable("customer_segments");
            query2.addToFromTable("customer_segments");
       	
        	query1.addToFromTable("segments");
            query2.addToFromTable("segments");

            query1.addToWhereConditions("customer_segments.vista_customer_number(+) = customer.vista_customer_number");
            query2.addToWhereConditions("customer_segments.vista_customer_number(+) = customer.vista_customer_number");
        	        	
            query1.addToWhereConditions("segments.segment_level = 1");
            query2.addToWhereConditions("segments.segment_level = 1");

            query1.addToWhereConditions("segments.segment_id = customer_segments.segment_id");
            query2.addToWhereConditions("segments.segment_id = customer_segments.segment_id");
            
            
            
            query1.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	query2.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	
        	query1.addToWhereConditions("customer.parent_num is not null");
        	query2.addToWhereConditions("customer.parent_num is not null");

        	outterSelect = "primary, " + outterSelect;
        	outterGroupByPrefix = "primary";    		
    		
    	} else if (groupBySecondarySegment) {
    		
        	query1.addToSelectFields("segments.segment_name secondary");
        	query2.addToSelectFields("segments.segment_name secondary");
        	
        	query1.addToFromTable("customer");
        	query2.addToFromTable("customer");

        	query1.addToFromTable("customer_segments");
            query2.addToFromTable("customer_segments");
       	
        	query1.addToFromTable("segments");
            query2.addToFromTable("segments");

            query1.addToWhereConditions("customer_segments.vista_customer_number(+) = customer.vista_customer_number");
            query2.addToWhereConditions("customer_segments.vista_customer_number(+) = customer.vista_customer_number");
        	        	
            query1.addToWhereConditions("segments.segment_level = 2");
            query2.addToWhereConditions("segments.segment_level = 2");

            query1.addToWhereConditions("segments.segment_id = customer_segments.segment_id");
            query2.addToWhereConditions("segments.segment_id = customer_segments.segment_id");
            
            
            
            query1.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	query2.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	
        	query1.addToWhereConditions("customer.parent_num is not null");
        	query2.addToWhereConditions("customer.parent_num is not null");

        	outterSelect = "secondary, " + outterSelect;
        	outterGroupByPrefix = "secondary";    		
    		
    	} else if (groupByDivision) {
    		
        	query1.addToSelectFields("division_description");
        	query2.addToSelectFields("division_description");
        	
        	query1.addToFromTable("customer");
        	query2.addToFromTable("customer");
        	
        	query1.addToFromTable("customer_divisions");
        	query2.addToFromTable("customer_divisions");
        	
        	query1.addToFromTable("divisions");
        	query2.addToFromTable("divisions");
        	
        	query1.addToWhereConditions("customer_divisions.vista_customer_number = customer.vista_customer_number");
        	query2.addToWhereConditions("customer_divisions.vista_customer_number = customer.vista_customer_number");
        	
        	query1.addToWhereConditions("customer_divisions.division_id = divisions.division_id");
        	query2.addToWhereConditions("customer_divisions.division_id = divisions.division_id");


        	outterSelect = "division_description, " + outterSelect;
        	outterGroupByPrefix = "division_description";    		
		
    		
    	} else if (groupBySE) {
    		
        	query1.addToSelectFields("sales_engineer");
        	query2.addToSelectFields("sales_engineer");
        	
        	query1.addToFromTable("customer");
        	query2.addToFromTable("customer");
        	
        	query1.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	query2.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	
        	query1.addToWhereConditions("customer.sales_engineer is not null");
        	query2.addToWhereConditions("customer.sales_engineer is not null");

        	outterSelect = "sales_engineer, " + outterSelect;
        	outterGroupByPrefix = "sales_engineer";    		
    		
    	} else {
    		
    		throw new SMRCException("No group by selected or supported for the passed in type");
    		
    	}
    	
    	//
    	// Start Filters
    	//
    	
        if (productFilter){
            StringBuffer whereClause = new StringBuffer();
            whereClause.append("products.product_id in (");
            for (int i=0; i < productValues.size(); i++){
                if (i > 0){
                    whereClause.append(", ");
                }
                whereClause.append("\'" + productValues.get(i) + "\'");
            }
            whereClause.append(")");
            query1.addToWhereConditions(whereClause.toString());     
            query2.addToWhereConditions(whereClause.toString());     
        }

        if (divisionFilter){
        	StringBuffer whereClause = new StringBuffer();
            whereClause.append("(");
            for (int i=0; i < divisionValues.size(); i++){
                if (i > 0){
                    whereClause.append(" or ");
                }
                
                whereClause.append("customer_divisions.division_id = ");
                whereClause.append("\'" + divisionValues.get(i) + "\'");
            }
            whereClause.append(")");

            query1.addToWhereConditions(whereClause.toString());
            query2.addToWhereConditions(whereClause.toString());
            
        	query1.addToFromTable("customer");
        	query2.addToFromTable("customer");
        	
        	query1.addToFromTable("customer_divisions");
        	query2.addToFromTable("customer_divisions");

        	query1.addToWhereConditions("customer_divisions.vista_customer_number = customer.vista_customer_number");
        	query2.addToWhereConditions("customer_divisions.vista_customer_number = customer.vista_customer_number");

        	query1.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	query2.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	
        
        }        
         
        
        if (geographyFilter){
        	StringBuffer whereClause = new StringBuffer();
            whereClause.append("(");
            for (int i=0; i < geographyValues.size(); i++){
                if (i > 0){
                    whereClause.append(" or ");
                }
                whereClause.append("customer.sp_geog like ");
                String thisGeog = (String) geographyValues.get(i);
                String searchString = getGeogSearchString(thisGeog, DBConn);
                whereClause.append("'" + searchString + "%'");
            }
            whereClause.append(")");

            query1.addToWhereConditions(whereClause.toString());
            query2.addToWhereConditions(whereClause.toString());
            
        	query1.addToFromTable("customer");
        	query2.addToFromTable("customer");
        	
        	query1.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	query2.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        }        
        
        if (focusTypeFilter){
            addToWithFromTable("customer_focus_type", 0);
            addToWithWhereConditions("customer.vista_customer_number = customer_focus_type.vista_customer_number", 0);
            StringBuffer whereClause = new StringBuffer();
            whereClause.append("customer_focus_type.focus_type_id in (");
            for (int i=0; i < focusTypeValues.size(); i++){
                if (i > 0){
                    whereClause.append(", ");
                }
                whereClause.append("\'" + focusTypeValues.get(i) + "\'");
            }
            whereClause.append(")");
            
            query1.addToWhereConditions(whereClause.toString());
            query2.addToWhereConditions(whereClause.toString());
            
        	query1.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	query2.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
 
        	query1.addToWhereConditions("customer_focus_type.vista_customer_number = customer.vista_customer_number");
        	query2.addToWhereConditions("customer_focus_type.vista_customer_number = customer.vista_customer_number");

        	query1.addToFromTable("customer");
        	query2.addToFromTable("customer");

        	query1.addToFromTable("customer_focus_type");
        	query2.addToFromTable("customer_focus_type");

        }        

        if (specialProgramFilter){
            addToWithFromTable("cust_special_programs_xref", 0);
            
            StringBuffer whereClause = new StringBuffer();
            whereClause.append("cust_special_programs_xref.special_program_id in (");
            for (int i=0; i < specialProgramValues.size(); i++){
                if (i > 0){
                    whereClause.append(", ");
                }
                whereClause.append("\'" + specialProgramValues.get(i) + "\'");
            }
            whereClause.append(")");
            
            query1.addToWhereConditions(whereClause.toString());
            query2.addToWhereConditions(whereClause.toString());
            
        	query1.addToFromTable("cust_special_programs_xref");
        	query2.addToFromTable("cust_special_programs_xref");

        	query1.addToFromTable("customer");
        	query2.addToFromTable("customer");

        	query1.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	query2.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");

        	query1.addToWhereConditions("cust_special_programs_xref.vista_customer_number = customer.vista_customer_number");
        	query2.addToWhereConditions("cust_special_programs_xref.vista_customer_number = customer.vista_customer_number");
        }        
        
        if (applicationFilter){
            StringBuffer whereClause = new StringBuffer();
            whereClause.append("customer.application_id in (");
            for (int i=0; i < applicationValues.size(); i++){
                if (i > 0){
                    whereClause.append(", ");
                }
                whereClause.append("\'" + applicationValues.get(i) + "\'");
            }
            whereClause.append(")");
            query1.addToWhereConditions(whereClause.toString());
            query2.addToWhereConditions(whereClause.toString());

            query1.addToFromTable("customer");
        	query2.addToFromTable("customer");

        	query1.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	query2.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        }        
        
        if (segmentFilter){
            StringBuffer whereClause = new StringBuffer();
            whereClause.append("customer_segments.segment_id in (");
            for (int i=0; i < segmentValues.size(); i++){
                if (i > 0){
                    whereClause.append(", ");
                }
                whereClause.append("\'" + segmentValues.get(i) + "\'");
            }
            whereClause.append(")");
            
            query1.addToWhereConditions(whereClause.toString());
            query2.addToWhereConditions(whereClause.toString());            
            
            query1.addToWithWhereConditions("customer_segments.vista_customer_number = customer.vista_customer_number", 0);
            query2.addToWithWhereConditions("customer_segments.vista_customer_number = customer.vista_customer_number", 0);
             
            query1.addToFromTable("customer_segments");
            query2.addToFromTable("customer_segments");
            
            query1.addToFromTable("customer");
        	query2.addToFromTable("customer");

        	query1.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        	query2.addToWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number");
        }        
    	/*
    	
        addToWithFromTable("customer_segments", 0);
        addToWithFromTable("segments", 0);
        addToWithWhereConditions("customer_segments.vista_customer_number = customer.vista_customer_number", 0);
        addToWithWhereConditions("segments.segment_id = customer_segments.segment_id", 0);
        addToWithWhereConditions("segments.segment_level = " + segLevel, 0);
        addToWithSelectFields("customer_segments.segment_id", 0);
        addToSelectFields("custlist.segment_id id");
        addToGroupByFields("custlist.segment_id");
        addToSelectFields("segments.segment_name description");
        addToGroupByFields("segments.segment_name");
        addToOrderByFields("segments.segment_name");
        addToFromTable("segments");
        addToWhereConditions("segments.segment_id = custlist.segment_id");
        if (showPotential || showForecast || showCompetitor){
            addToWithFromTable("customer_segments", 1);
            addToWithFromTable("segments", 1);
            addToWithWhereConditions("customer_segments.vista_customer_number = customer.vista_customer_number", 1);
            addToWithWhereConditions("segments.segment_id = customer_segments.segment_id", 1);
            addToWithWhereConditions("segments.segment_level = " + segLevel, 1);
            addToWithSelectFields("customer_segments.segment_id", 1);
            addToWithFromTable("customer", 1);
            addToWithGroupByFields("customer_segments.segment_id", 1);
            addToWithWhereConditions("customer.vista_customer_number = customer_product.vista_customer_number", 1);
            addToWhereConditions("custprod.segment_id(+) = custlist.segment_id");
	

        addToFromTable("geographies geogdist");
        addToFromTable("geographies");
        addToWhereConditions("substr(custlist.sp_geog,1,4)||'0' = geographies.sp_geog");
        addToWhereConditions("substr(custlist.sp_geog,1,5) = geogdist.sp_geog");
        addToGroupByFields("geographies.sp_geog, geographies.description, geogdist.sp_geog, geogdist.description");
*/
    	// Braffet : For testing, we are going to just group by product_id with no filter.
    	// We will chang this after it goes through successfully once or twice.
    	
        
/*        // Build the first and last part of the outer query to return just 50 rows
        
        String limitPrefix = "";
        String limitSuffix = "";
        
        if (limitResults) {
        	
        	limitPrefix = "select * from (select " + outterGroupByPrefix + ", vendor_id, Competitor, Potential, rownum rownumber from (";
            
        	limitSuffix = ") ) where rownumber between " + beginWith + " and " + endWith;
        	
        }*/
    	
    	
    	//
    	// Build the final SQL query
    	
    	String finalQuery = "select " + outterSelect 
    		+ " from "
    			+ "("
	    			+ "("
	    				+ query1.createSQLQuery()
	    			+ ")"
	    			+" UNION ALL"
	    			+ "("
					+ query2.createSQLQuery()
					+ ")"
				+ ")"
			+ " group by "
				+ outterGroupByPrefix + ", " + outterGroupBySuffix;

    	return finalQuery;
        
    }
    
    //  returns the text appearing at the top of the first column in the report
    public String returnDescriptionColumnHeading (){
                
        if (groupByZone && groupByDistrict){
            return "Zone / District";
        } else if (groupByDistrict && groupByProduct){
            return "District / Product";
        } /*else if (groupByDistrict && groupBySummaryProduct){
            return "District / Summary Product";
        } */
          else if (groupByDistrict && groupByTeam){
            return "District / Team";
        } else if (groupByPrimarySegment && groupByDistrict){
            return "Primary Segment / District";
        } else if (groupBySecondarySegment && groupByDistrict){
            return "Secondary Segment / District";
        } else if (groupByParent && groupByAccount){
            return "Parent / Account";
        } else if (groupByAccount){
            return "Account";
        } else if (groupByParent){
            return "Parent";
        } else if (groupByZone){
            return "Zone";
        } else if (groupByDistrict){
            return "District";
        } else if (groupByTeam){
            return "Team";
        } /*else if (groupBySalesOrg){
            return "Sales Organization";
        } else if (groupByGroupCode){
            return "Group Code";
        } */else if (groupBySE){
            return "Sales Engineer";
        } else if (groupByProduct){
            return "Product Line";
        } /*else if (groupBySummaryProduct){
            return "Summary Product";
        }*/ else if (groupByPrimarySegment){
            return "Primary Segment";
        } else if (groupBySecondarySegment){
            return "Secondary Segment";
        }
        
        // if it doesn't find any of the above
        return "Description";
        
    }
 /*  
    //** Returns an ArrayList of Column Headers for report
    public ArrayList returnColumnHeadings(){
        ArrayList headers = new ArrayList();
*/        
/*        
        if (showPotential){
            headers.add("Potential $");
        }
        if (showForecast){
            headers.add("Forecast $");
        }
        if (showCompetitor){
            headers.add("Competitor $");
        }  */
                
/*        for (int i=0; i < dollarTypeList.size(); i++){
            DollarTypeRptBean dollarBean = new DollarTypeRptBean();
            dollarBean = (DollarTypeRptBean) dollarTypeList.get(i);
            String fieldPrefix = null;
            String dollarType = dollarBean.getDollarType();
            if (dollarType.equalsIgnoreCase("credit")){
                fieldPrefix = "Credit";
            } else if (dollarType.equalsIgnoreCase("end")){
                fieldPrefix = "End Mkt";
            } else {
               fieldPrefix = "Charge";
            }
            if (dollarBean.useMonthly()){
                if (dollarBean.useOrders()){
                    headers.add(fieldPrefix + " Month Order");
                }
                if (dollarBean.useSales()){
                    headers.add(fieldPrefix + " Month Sales");
                }
            }
            if (dollarBean.useYTDmonthly()){
                if (dollarBean.useOrders()){
                    headers.add(fieldPrefix + " YTD Order");
                }
                if (dollarBean.useSales()){
                    headers.add(fieldPrefix + " YTD Sales");
                }
            }
            if (dollarBean.usePrevYTDmonthly()){
                if (dollarBean.useOrders()){
                    headers.add(fieldPrefix + " Prev YTD Month Order");
                }
                if (dollarBean.useSales()){
                    headers.add(fieldPrefix + " Prev YTD Month Sales");
                }
            }
            if (dollarBean.usePrevYearTotal()){
                if (dollarBean.useOrders()){
                    headers.add(fieldPrefix + " Prev Year Order");
                }
                if (dollarBean.useSales()){
                    headers.add(fieldPrefix + " Prev Year Sales");
                }
            }
            if (dollarBean.usePrevYearMonthly()){
                if (dollarBean.useOrders()){
                    headers.add(fieldPrefix + " Prev Month Order");
                }
                if (dollarBean.useSales()){
                    headers.add(fieldPrefix + " Prev Month Sales");
                }
            }
        }*/
/*        return headers;
        
    }
    */
    public String returnFilterString(Connection DBConn) throws Exception{
        
    	StringBuffer filterString = new StringBuffer();
        int filterCount = 0;
        filterString.append("<b>This report was filtered to just show </b>");
        if (targetAccounts){
            filterCount++;
            filterString.append("<BR>Target Accounts");
        }
        if (productFilter){
            filterCount++;
            filterString.append("<br><b>Products: </b>");
            for(int i=0; i < productValues.size(); i++){
                Product product = ProductDAO.productLookup((String) productValues.get(i), year, DBConn);
                if (i>0){
                    filterString.append(", ");
                }
                filterString.append(product.getDescription());
            }
        }
        
        if (divisionFilter){
            filterCount++;
            filterString.append("<br><b>Product Division: </b>");
            for(int i=0; i < divisionValues.size(); i++){
                Division division = DivisionDAO.getDivision((String) divisionValues.get(i), DBConn);
                if (i>0){
                    filterString.append(", ");
                }
                filterString.append(division.getName());
            }
        }
        
        if (focusTypeFilter){
            filterCount++;
            filterString.append("<br><b>Focus Types: </b>");
            for(int i=0; i < focusTypeValues.size(); i++){
                if (i>0){
                    filterString.append(", ");
                }
                String ftName = MiscDAO.getFocusTypeDescription((String) focusTypeValues.get(i), DBConn);
                filterString.append(ftName);
            }
        }
        
/*        if (parentFilter){
            filterCount++;
            filterString.append("<br><b>Parents: </b>");
            for(int i=0; i < parentValues.size(); i++){
                if (i>0){
                    filterString.append(", ");
                }
                String parentString = AccountDAO.getAccountName((String)parentValues.get(i), DBConn);
                filterString.append(parentString + " (" + parentValues.get(i) + ")");
            }
        }
        if (seFilter){
            filterCount++;
            filterString.append("<br><b>Sales Engineers: </b>");
            for(int i=0; i < seValues.size(); i++){
                if (i>0){
                    filterString.append(", ");
                }
                String seString = SalesDAO.getSalesmanName((String) seValues.get(i), DBConn);
                filterString.append(seString + " (" + seValues.get(i) + ")");
            }
        }*/
       
        if (segmentFilter){
            filterCount++;
            filterString.append("<br><b>Segments: </b>");
            for(int i=0; i < segmentValues.size(); i++){
                if (i>0){
                    filterString.append(", ");
                }
                int segmentId = Globals.a2int((String) segmentValues.get(i));
                Segment segment = SegmentsDAO.getSegment(segmentId, DBConn);
                filterString.append(segment.getName());
            }
        }
        
        if (applicationFilter){
            filterCount++;
            filterString.append("<br><b>Applications: </b>");
            for(int i=0; i < applicationValues.size(); i++){
                if (i>0){
                    filterString.append(", ");
                }
                int appId = Globals.a2int((String) applicationValues.get(i));
                CodeType app = MiscDAO.getCodeById(appId, DBConn);
                filterString.append(app.getDescription());
            }
        }
        
        if (geographyFilter){
        	filterCount++;
            filterString.append("<br><b>Geography: </b>");
            for(int i=0; i < geographyValues.size(); i++){
                if (i>0){
                    filterString.append(", ");
                }
                String geogname = DistrictDAO.getDistrictName((String) geographyValues.get(i), DBConn);
                filterString.append(geogname);
            }
        }
        
        if (specialProgramFilter){
            filterCount++;
            filterString.append("<br><b>Special Programs: </b>");
            for(int i=0; i < specialProgramValues.size(); i++){
                if (i>0){
                    filterString.append(", ");
                }
                String spId = (String) specialProgramValues.get(i);
                filterString.append(MiscDAO.getSpecialProgramDescription(spId,DBConn));
            }
        }
        
        if (filterCount > 0){
            return filterString.toString();
        }
        return ("<b>No filters selected</b>");
    
    }
    
/*    public void getGeogSecurityCode(){
         HashSet geogFilters = new HashSet();
         StringBuffer geogFilterString = new StringBuffer();
                    
                
         // Set automatic filters for users security
         
         // Do not need to add security for users that can view everything, have override
         // security, or if the group by is not geographical and no geographies have been 
         // selected as filters
         if (!user.ableToViewEverything() && !user.hasOverrideSecurity()){
  //            && (requiresGeogSecurityAudit() || geographyFilter)){
            ArrayList ugsList = user.getAllUGS();
         	boolean hasSegmentOverride = false;
         	segmentOverrideList = user.getSegmentOverrides();
         	if (segmentOverrideList.size() > 0){
         		hasSegmentOverride = true;
         	}
         	// Check geography filter values and remove them if the user cannot see them
         	// at the group by level
         	
         	if (geographyFilter && (!hasSegmentOverride)){
         	    String level = "";
         		if (groupByAccount || groupByParent || groupBySE){
                	level = "ACCOUNT";        
                } else if (groupByTeam){
                    level = "TEAM";
                } else if (groupByDistrict){
                	level = "DISTRICT";
                } else if (groupByZone){
                	level = "ZONE";
                } else if (groupByGroupCode){
                	level = "GROUP";
                } else if (groupBySalesOrg){
                	level = "NATIONAL";
                } 
                for (int i=0; i < geographyValues.size(); i++){
                	String geography = (String) geographyValues.get(i);
                	if (!user.ableToSeeThisLevel(geography, level)){
                		geographyValues.remove(i);
                		i--;     // so we don't skip next value!
                	}
                }
                if (geographyValues.size() == 0){
                	geographyFilter = false;
                }
         	
         	}
         	
                    
	            //  DO NOT COMBINE THIS IF WITH AN "ELSE" TO THE PREVIOUS IF
	            //  These checks are seperate in case the filters are reset to false
	            //  by the previous if statement
	            // if no filters selected, force any necessary security restrictions
	            for (int i=0; i < ugsList.size(); i++){
	                UserGeogSecurity ugs = (UserGeogSecurity) ugsList.get(i);
	                
	                
	                 * added !ugs.isSalesId() because we dont want to add
	                 * a geog check for sales ids
	                  
//	                if (!geographyFilter && !ugs.isSalesId()){
	  //          	if (!ugs.isSalesId()){	
	                
	                	if (groupByAccount || groupByParent || groupBySE){
	                            if (ugs.ableToViewSalesman()){
	                                geogFilters.add(getGeogFilterString(ugs));
	                            }
	                    } else if (groupByTeam){
	                            if (ugs.ableToSeeTeamLevel()){
	                                geogFilters.add(getGeogFilterString(ugs));
	                            }
	                    } else if (groupByDistrict){
	                            if (ugs.ableToSeeDistrictLevel()){
	                                geogFilters.add(getGeogFilterString(ugs));
	                            }
	                    } else if (groupByZone){
	                            if (ugs.ableToSeeZoneLevel()){
	                                geogFilters.add(getGeogFilterString(ugs));
	                            }
	                    } else if (groupByGroupCode){
	                            if (ugs.ableToSeeGroupLevel()){
	                                geogFilters.add(getGeogFilterString(ugs));
	                            }
	                    } else if (groupBySalesOrg){
	                            if (ugs.ableToSeeNationalLevel()){
	                                geogFilters.add(getGeogFilterString(ugs));
	                            }
	                    } else if (groupByProduct || groupBySummaryProduct ||
	                           groupByPrimarySegment || groupBySecondarySegment){
	                           geogFilters.add(getGeogFilterString(ugs)); 
	                    }
	               
	                                        
	            }
                
                if ((geogFilters.size() > 0) || (hasSegmentOverride)){
                    geogFilterString.append("( ");
                    Iterator iter = geogFilters.iterator();
                    while (iter.hasNext()){
                        geogFilterString.append((String) iter.next());
                        if (iter.hasNext() || (hasSegmentOverride)){
                            geogFilterString.append(" or ");
                        }
                    }
                    // Segment Override stuff here
                    if (hasSegmentOverride){
                        geogFilterString.append("customer.vista_customer_number in ( ");
                        geogFilterString.append("select customer.vista_customer_number ");
                        geogFilterString.append("from customer, customer_segments ");
                        geogFilterString.append("where customer.vista_customer_number = customer_segments.vista_customer_number ");
                        geogFilterString.append("and (customer_segments.segment_id in (");
                    	for (int segIndex=0; segIndex < segmentOverrideList.size(); segIndex++){
                    		if (segIndex > 0){
                    			geogFilterString.append(", ");
                    		}
                    		Integer seg = (Integer) segmentOverrideList.get(segIndex);
                    		geogFilterString.append("'" + seg.intValue() + "'");
                    	}
                    	geogFilterString.append(") ) ) ");
                    }
                    
                    
                    geogFilterString.append(" ) ");
                
                    addToWithWhereConditions(geogFilterString.toString(), 0);
                    if  (showPotential || showForecast || showCompetitor){
                        addToWithFromTable("customer", 1);
                        addToWithWhereConditions("customer.vista_customer_number = customer_product.vista_customer_number", 1);
                        addToWithWhereConditions(geogFilterString.toString(), 1);
                     }
                }
                    
           }
         
        // return geogFilterString.toString();
         
    }*/
    
    
/*    private String getGeogFilterString(UserGeogSecurity ugs){
    	String tempString = "";
        if (ugs.isDistrict()){
            tempString = "(substr(customer.sp_geog,1,5) like '" + ugs.getSPGeog().substring(0,5) + "%')";
            securityGeogs.add(ugs.getSPGeog());
        }
        if (ugs.isGroup()){
            tempString = "(substr(customer.sp_geog,1,2) like '" + ugs.getSPGeog().substring(0,2) + "%')";
            securityGeogs.add(ugs.getSPGeog());
        }
        if (ugs.isNational()){
            tempString = "(substr(customer.sp_geog,1,1) like '" + ugs.getSPGeog().substring(0,1) + "%')";
            securityGeogs.add(ugs.getSPGeog());
        }
        if (ugs.isTeam()){
            tempString = "(substr(customer.sp_geog,1,6) = '" + ugs.getSPGeog().substring(0,6) + "')";
            securityGeogs.add(ugs.getSPGeog());
        }
        if (ugs.isZone()){
            tempString = "(substr(customer.sp_geog,1,4) like '" + ugs.getSPGeog().substring(0,4) + "%')";
            securityGeogs.add(ugs.getSPGeog());
        }
        if (ugs.isSalesId()){
            tempString = "(customer.sales_engineer1 = '" + ugs.getSPGeog() + "' or " +
            			" customer.sales_engineer2 = '" + ugs.getSPGeog() + "' or " +
            			" customer.sales_engineer3 = '" + ugs.getSPGeog() + "' or " +
            			" customer.sales_engineer4 = '" + ugs.getSPGeog() + "') ";
            securityGeogs.add(ugs.getSPGeog());
        }
       
        return tempString;
    }*/
    
    // Returns String of Geography Names that the user is limited to by security for 
    // this specific search
/*     public String returnSecurityFilterString(Connection DBConn) throws Exception{
        
        StringBuffer securityString = new StringBuffer();
        if (securityGeogs.size() > 0){
            securityString.append("<b>Security has limited this report to these geographies: </b>");
            for(int i=0; i < securityGeogs.size(); i++){
                if (i>0){
                    securityString.append(", ");
                }
                String geog = (String) securityGeogs.get(i);
                if (geog.length() > 4){
                    String geogname = DistrictDAO.getDistrictName(geog, DBConn);
                    securityString.append(geogname);
                } else {
                    String seName = SalesDAO.getSalesmanName(geog, DBConn);
                    securityString.append(seName + "(" + geog + ")");
                }
            }
        } else {
            securityString.append("");
        }
        if (segmentOverrideList.size() > 0){
        	securityString.append("<br><b>Geographical Security has been overridden for accounts in these segments and their child segments:</b> ");
            for(int i=0; i < segmentOverrideList.size(); i++){
                if (i>0){
                    securityString.append(", ");
                }
                String segmentName = SegmentsDAO.getSegmentName(((Integer) segmentOverrideList.get(i)).intValue(), DBConn);
                securityString.append(segmentName);
            }
        }
        
        return securityString.toString();
    
    }*/
    //** Returns true is geog security needs to be checked for this grouping
/*
     private boolean requiresGeogSecurityAudit() {
        // All of these groupings should have security checked
        if (groupByZone || groupByDistrict || groupByTeam || groupBySalesOrg 
                ||groupByGroupCode || groupBySE || groupByAccount || groupByParent){
            return true;
        }
        return false;
        
    }
 */   
    public boolean isGroupByZone(){
        return groupByZone;
    }
    
    public boolean isGroupByDistrict(){
        return groupByDistrict;
    }
     
    public boolean isGroupByTeam(){
        return groupByTeam;
    }
      
 /*   public boolean isGroupBySalesOrg(){
        return groupBySalesOrg;
    }  
    
    public boolean isGroupByGroupCode(){
        return groupByGroupCode;
    }
    */
    public boolean isGroupByProduct(){
        return groupByProduct;
    }
    
    public boolean isGroupByAccount(){
        return groupByAccount;
    }
    
    public boolean isGroupByParent(){
        return groupByParent;
    }
    
    public boolean isGroupBySE(){
        return groupBySE;
    }
    
/*    public boolean isGroupBySummaryProduct(){
        return groupBySummaryProduct;
    }
    
   */
    public boolean isGroupByPrimarySegment(){
        return groupByPrimarySegment;
    }
    
    public boolean isGroupBySecondarySegment(){
        return groupBySecondarySegment;
    }
    
    
    
}