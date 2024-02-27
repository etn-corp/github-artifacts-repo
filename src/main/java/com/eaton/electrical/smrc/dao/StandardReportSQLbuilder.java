/*
 * StandardReportSQLbuilder.java
 *
 * Created on July 12, 2004, 8:27 AM
 */

package com.eaton.electrical.smrc.dao;

import java.sql.*;
import java.util.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.util.*;


/**
 *
 * @author  Jason Lubbert
 */
public class StandardReportSQLbuilder extends SQLBuilder {
    
    boolean showPotential = false;
    boolean showForecast = false;
    boolean showCompetitor = false;
    
    boolean showCreditSales = false;
    boolean showCreditOrder = false;
    boolean showCreditSalesMonthly = false;
    boolean showCreditSalesYTD = false;
    boolean showCreditSalesPrevYTD = false;
    boolean showCreditSalesPrevYear = false;
    boolean showCreditSalesPrevMonth = false;
    boolean showCreditOrderMonthly = false;
    boolean showCreditOrderYTD = false;
    boolean showCreditOrderPrevYTD = false;
    boolean showCreditOrderPrevYear = false;
    boolean showCreditOrderPrevMonth = false;
    
    boolean showEndSales = false;
    boolean showEndOrder = false;
    boolean showEndSalesMonthly = false;
    boolean showEndSalesYTD = false;
    boolean showEndSalesPrevYTD = false;
    boolean showEndSalesPrevYear = false;
    boolean showEndSaleePrevMonth = false;
    boolean showEndOrderMonthly = false;
    boolean showEndOrderYTD = false;
    boolean showEndOrderPrevYTD = false;
    boolean showEndOrderPrevYear = false;
    boolean showEndOrderPrevMonth = false;
    
    boolean showChargeSales = false;
    boolean showChargeOrder = false;
    boolean showChargeSalesMonthly = false;
    boolean showChargeSalesYTD = false;
    boolean showChargeSalesPrevYTD = false;
    boolean showChargeSalesPrevYear = false;
    boolean showChargeSaleePrevMonth = false;
    boolean showChargeOrderMonthly = false;
    boolean showChargeOrderYTD = false;
    boolean showChargeOrderPrevYTD = false;
    boolean showChargeOrderPrevYear = false;
    boolean showChargeOrderPrevMonth = false;
    
    // If this is false, then oracle will do a hint to materialize
    // the temporary table.  For right now this is set conservative (don't 
    // materialize).  After we see it work for a while, we might change the default
    
    boolean isFiltered = true;      
    
    int month = 0; 
    int year = 0;
    
    boolean groupByProduct = false;
    boolean groupByAccount = false;
    boolean groupByParent = false;
    boolean groupByZone = false;
    boolean groupByDistrict = false;
    boolean groupByTeam = false;
    boolean groupBySalesOrg = false;
    boolean groupByGroupCode = false;
    boolean groupBySE = false;
    boolean groupBySummaryProduct = false;
    boolean groupByPrimarySegment = false;
    boolean groupBySecondarySegment = false;
    
    ArrayList dollarTypeList = new ArrayList();
    
    boolean productFilter = false;
    ArrayList productValues = new ArrayList();
    
    boolean focusTypeFilter = false;
    ArrayList focusTypeValues = new ArrayList();
    boolean parentFilter = false;
    ArrayList parentValues = new ArrayList();
    boolean seFilter = false;
    ArrayList seValues = new ArrayList();
    boolean segmentFilter = false;
    ArrayList segmentValues = new ArrayList();
    boolean applicationFilter = false;
    ArrayList applicationValues = new ArrayList();
    boolean geographyFilter = false;
    ArrayList geographyValues = new ArrayList();
    boolean specialProgramFilter = false;
    ArrayList specialProgramValues = new ArrayList();
    
    boolean targetAccounts = false;
    
    boolean emailingListOnly = false;
    
    ArrayList securityGeogs = new ArrayList();
    ArrayList segmentOverrideList = new ArrayList();
    
    User user = new User();
    
    int srYear = 0;
    int srMonth = 0;
        
    
       
    /** Creates a new instance of AcctPlanSQLbuilder */
    public StandardReportSQLbuilder(String month, String year) {
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
    
    //** Sets flag to show potential dollars on report
    public void showPotentialDollars (){
        showPotential = true;
    }
    //** Sets flag to show forecast dollars on report
    public void showForecastDollars (){
        showForecast = true;
    }
    //** Sets flag to show competitor dollars on report
    public void showCompetitorDollars (){
        showCompetitor = true;
    }
    
    //** Sets flag to create sql for email list to true
    public void setEmailingListOnly(){
    	emailingListOnly = true;
    }
    
    //** Sets flag to only look for target accounts
    public void targetAccountsOnly(){
        targetAccounts = true;
    }
    //** Sets flag to show potential dollars on report
    public boolean usingPotentialDollars (){
        return showPotential;
    }
    //** Sets flag to show forecast dollars on report
    public boolean usingForecastDollars (){
        return showForecast;
    }
    //** Sets flag to show competitor dollars on report
    public boolean usingCompetitorDollars (){
        return showCompetitor;
    }
   
    
 
    
    
   //** sets groupByProduct to true
    public void setGroupByProduct (){
        groupByProduct = true;
    }
    //** sets groupByAccount to true
    public void setGroupByAccount (){
        groupByAccount = true;
    }
    //** sets groupByParent to true
    public void setGroupByParent (){
        groupByParent = true;
    }
    //** sets groupByZone to true
    public void setGroupByZone (){
        groupByZone = true;
    }
    //** sets groupByDistrict to true
    public void setGroupByDistrict (){
        groupByDistrict = true;
    }
    //** sets groupByTeam to true
    public void setGroupByTeam (){
        groupByTeam = true;
    }
    //** sets groupBySalesOrg to true
    public void setGroupBySalesOrg (){
        groupBySalesOrg = true;
    }
    //** sets groupByGroupCode to true
    public void setGroupByGroupCode (){
        groupByGroupCode = true;
    }
    //** sets groupBySE to true
    public void setGroupBySE (){
        groupBySE = true;
    }
    //** sets groupBySummaryProduct to true
    public void setGroupBySummaryProduct (){
        groupBySummaryProduct = true;
    }
    //** sets groupByPrimarySegment to true
    public void setGroupByPrimarySegment (){
        groupByPrimarySegment = true;
    }
    //** sets groupBySecondarySegment to true
    public void setGroupBySecondarySegment (){
        groupBySecondarySegment = true;
    }
    
    
    
    //** Recieve ArrayList of DollarTypeRptBean objects
    public void setDollarTypes (ArrayList dollarTypes){
        dollarTypeList = dollarTypes;
    }
    
    //** Need User object for checking security
    public void setUser (User user){
        this.user = user;
    }
    
    //** We have to add each value of the ArrayLists below seperately
    //** instead of assigning the whole ArrayList because the method could
    //** be called more than once
    
    //** Use product filter and populate values to use in filter
    public void useProductFilter (ArrayList filterValues){
        productFilter = true;
        for (int i=0; i < filterValues.size(); i++){
            productValues.add(filterValues.get(i));
        }
    }
    
    //** Use focus type filter and populate values to use in filter    
    public void useFocusTypeFilter (ArrayList filterValues){
        focusTypeFilter = true;
        for (int i=0; i < filterValues.size(); i++){
            focusTypeValues.add(filterValues.get(i));
        }
    }
    
    //** Use parent filter and populate values to use in filter
    public void useParentFilter (ArrayList filterValues){
        parentFilter = true;
        for (int i=0; i < filterValues.size(); i++){
            parentValues.add(filterValues.get(i));
        }
    }
    //** Use sales engineer filter and populate values to use in filter
    public void useSalesEngineerFilter (ArrayList filterValues){
        seFilter = true;
        for (int i=0; i < filterValues.size(); i++){
            seValues.add(filterValues.get(i));
        }
    }
    
    //** Use segment filter and populate values to use in filter
    public void useSegmentFilter (ArrayList filterValues){
        segmentFilter = true;
        for (int i=0; i < filterValues.size(); i++){
            segmentValues.add(filterValues.get(i));
        }
        
    }
    //** Use application filter and populate values to use in filter
    public void useApplicationFilter (ArrayList filterValues){
        applicationFilter = true;
        for (int i=0; i < filterValues.size(); i++){
            applicationValues.add(filterValues.get(i));
        }
        
    }
    //** Use geography filter and populate values to use in filter
    public void useGeographyFilter (ArrayList filterValues){
        geographyFilter = true;
        for (int i=0; i < filterValues.size(); i++){
            geographyValues.add(filterValues.get(i));
        }
        
    }
//  ** Use special programs filter and populate values to use in filter
    public void useSpecialProgramFilter (ArrayList filterValues){
        specialProgramFilter = true;
        for (int i=0; i < filterValues.size(); i++){
            specialProgramValues.add(filterValues.get(i));
        }
        
    }
    
    public void setIsFiltered(boolean isFiltered) {   	
    	this.isFiltered = isFiltered;    	
   		this.setWithHintMaterialze(0, !isFiltered);
    }
    
    //** return if the filter is set  */  
    public boolean isFiltered() {  
    	return isFiltered;    
    }   
        
    public String returnSQL (Connection DBConn) throws Exception{
        getGeogSecurityCode();
     
 //   Acct Plan Report always needs these lines in the sql
        setWithSelectDistinct(0);    // Do we ever NOT want distinct for custlist???
        addToWithNames("custlist");
        addToWithFromTable("customer", 0);
        addToWithSelectFields("customer.vista_customer_number", 0);
        addToWithSelectFields("customer.customer_name", 0);
        addToFromTable("custlist");
        addToFromTable("tap_dollars");
        addToWhereConditions("tap_dollars.vista_customer_number = custlist.vista_customer_number");
        if (targetAccounts){
            addToWithWhereConditions("customer.target_account_flag = 'Y'", 0);
        }
        
 //   If a product filter is not used, or the results are not grouped by product or summary product,
 //   we have to check only for products with a sp_load_total of 'T' so the totals are not 3X the correct
 //   amount due to the inclusion of summary products and the grand total product id
        
       if (!groupByProduct && !groupBySummaryProduct && !productFilter && !emailingListOnly){
       		addToFromTable("products");
       		addToWhereConditions("products.product_id = tap_dollars.product_id");
       		addToWhereConditions("products.sp_load_total = 'T'");
       		addToWhereConditions("products.period_yyyy = " + srYear + " ");
       }
        
        
        
 //   Group by sql
        if (groupByAccount || groupByParent){
            if (groupByAccount && groupByParent){
                setMultiLevel();
                addToGroupByFields("pcust.vista_customer_number, pcust.customer_name, customer.vista_customer_number, customer.customer_name");
                addToFromTable("customer pcust");
                addToFromTable("customer");
                addToWhereConditions("customer.vista_customer_number = custlist.vista_customer_number");
                addToWhereConditions("pcust.vista_customer_number = customer.parent_num");
                addToSelectFields("customer.customer_name description2");
                addToSelectFields("customer.vista_customer_number id2");
                addToSelectFields("pcust.vista_customer_number id");
                addToSelectFields("pcust.customer_name description");
                if (showPotential || showForecast || showCompetitor){
                        addToWithSelectFields("customer_product.vista_customer_number", 1);
                        addToWithGroupByFields("customer_product.vista_customer_number", 1);
                        addToWhereConditions("custprod.vista_customer_number(+) = custlist.vista_customer_number");
                }
            } else if (groupByAccount){
                    addToSelectFields("custlist.vista_customer_number id");
                    addToSelectFields("custlist.customer_name description");
                    if (!emailingListOnly){
                    	addToGroupByFields("custlist.vista_customer_number");
                    	addToGroupByFields("custlist.customer_name");
                    }
                    addToOrderByFields("custlist.customer_name");
                    if (showPotential || showForecast || showCompetitor){
                        addToWithSelectFields("customer_product.vista_customer_number", 1);
                        addToWithGroupByFields("customer_product.vista_customer_number", 1);
                        addToWhereConditions("custprod.vista_customer_number(+) = custlist.vista_customer_number");
                    }
            } else if (groupByParent){
                    addToWithFromTable("customer pcust", 0);
                    addToWithWhereConditions("customer.parent_num = pcust.vista_customer_number", 0);
                    addToWithSelectFields("customer.parent_num", 0);
                    addToSelectFields("custlist.parent_num id");
                    addToGroupByFields("custlist.parent_num");
                    // if the parent is not found, uses customer name from custlist
                    addToSelectFields("nvl(customer.customer_name, custlist.customer_name) description");
                    addToGroupByFields("nvl(customer.customer_name, custlist.customer_name)");
                    addToOrderByFields("nvl(customer.customer_name, custlist.customer_name)");
                    addToFromTable("customer");
                    addToWhereConditions("customer.vista_customer_number = custlist.parent_num");
                    if (showPotential || showForecast || showCompetitor){
                        addToWithFromTable("customer pcust", 1);
                        addToWithFromTable("customer", 1);
                        addToWithSelectFields("customer.parent_num", 1);
                        addToWithGroupByFields("customer.parent_num", 1);
                        addToWithWhereConditions("customer.vista_customer_number = customer_product.vista_customer_number", 1);
                        addToWhereConditions("custprod.parent_num(+) = custlist.parent_num");
                        addToWithWhereConditions("customer.parent_num = pcust.vista_customer_number", 1);
                    }
            }
        
        } else if (groupBySE){
            addToWithSelectFields("customer.sales_engineer1", 0);
            addToSelectFields("custlist.sales_engineer1 id");
            addToGroupByFields("custlist.sales_engineer1");
            addToGroupByFields("salesman_detail_mv.last_nm");
            addToGroupByFields("salesman_detail_mv.first_nm");
            addToOrderByFields("salesman_detail_mv.last_nm");
            addToFromTable("salesman_detail_mv");
//            addToSelectFields("salesman_detail_mv.first_nm");
  //          addToSelectFields("salesman_detail_mv.last_nm description");
            addToSelectFields("salesman_detail_mv.last_nm || ',' || ' ' || salesman_detail_mv.first_nm description");
            addToWhereConditions("salesman_detail_mv.salesman_id = custlist.sales_engineer1");
            addToWhereConditions("salesman_detail_mv.inactive_dt is null");
            addToWhereConditions("to_char(start_dt, 'YYYY') = '" + year + "'");
            if (showPotential || showForecast || showCompetitor){
                addToWithFromTable("customer", 1);
                addToWithSelectFields("customer.sales_engineer1", 1);
                addToWithGroupByFields("customer.sales_engineer1", 1);
                addToWhereConditions("custprod.sales_engineer1(+) = custlist.sales_engineer1");
                addToWithWhereConditions("customer.vista_customer_number = customer_product.vista_customer_number", 1);
            }
        } else if (groupByZone && groupByDistrict){
            setMultiLevel();
            addToWithSelectFields("customer.sp_geog", 0);
            addToSelectFields("geographies.description description");
            addToSelectFields("geographies.sp_geog id");
            addToSelectFields("geogdist.description description2");
            addToSelectFields("geogdist.sp_geog id2");
            addToFromTable("geographies geogdist");
            addToFromTable("geographies");
            addToWhereConditions("substr(custlist.sp_geog,1,4)||'0' = geographies.sp_geog");
            addToWhereConditions("substr(custlist.sp_geog,1,5) = geogdist.sp_geog");
            addToGroupByFields("geographies.sp_geog, geographies.description, geogdist.sp_geog, geogdist.description");
            if (showPotential || showForecast || showCompetitor){
                addToWithFromTable("customer", 1);
                addToWithSelectFields("substr(customer.sp_geog,1,5) district", 1);
                addToWithGroupByFields("substr(customer.sp_geog,1,5)", 1);
                addToWhereConditions("custprod.district (+) = geogdist.sp_geog");
                addToWithWhereConditions("customer.vista_customer_number = customer_product.vista_customer_number", 1);
            }
        } else if (groupByTeam && groupByDistrict){
            setMultiLevel();
            addToWithSelectFields("customer.sp_geog", 0);
            addToSelectFields("geographies.description description");
            addToSelectFields("geographies.sp_geog id");
            addToSelectFields("geogteam.description description2");
            addToSelectFields("geogteam.sp_geog id2");
            addToFromTable("geographies geogteam");
            addToFromTable("geographies");
            addToWhereConditions("substr(custlist.sp_geog,1,5) = geographies.sp_geog");
            addToWhereConditions("custlist.sp_geog = geogteam.sp_geog");
            addToGroupByFields("geographies.sp_geog, geographies.description, geogteam.sp_geog, geogteam.description");
            if (showPotential || showForecast || showCompetitor){
                addToWithFromTable("customer", 1);
                addToWithSelectFields("customer.sp_geog team", 1);
                addToWithGroupByFields("customer.sp_geog", 1);
                addToWhereConditions("custprod.team (+) = geogteam.sp_geog");
                addToWithWhereConditions("customer.vista_customer_number = customer_product.vista_customer_number", 1);
            }  
        } else if (groupByDistrict && groupByProduct){
            setMultiLevel();
            addToWithSelectFields("substr(customer.sp_geog,1,5) district", 0);
            addToSelectFields("geographies.description description");
            addToSelectFields("geographies.sp_geog id");
            addToSelectFields("products.product_id id2");
            addToSelectFields("products.product_description description2");
            addToFromTable("geographies");
            addToFromTable("products");
            addToWhereConditions("custlist.district = geographies.sp_geog");
            addToWhereConditions("products.product_id = tap_dollars.product_id");
            addToWhereConditions("products.sp_load_total = 'L'");
            addToWhereConditions("products.period_yyyy = " + srYear + " ");
            addToGroupByFields("geographies.sp_geog, geographies.description, products.product_id, products.product_description");
            if (showPotential || showForecast || showCompetitor){
                if (showPotential || showForecast || showCompetitor){
                    setWithSelectDistinct(1);
                    addToWithFromTable("products", 1);
                    addToWithFromTable("customer", 1);
                    addToWithSelectFields("customer_product.product_id", 1);
                    addToWithSelectFields("substr(customer.sp_geog,1,5) district", 1);
                    addToWithGroupByFields("customer_product.product_id", 1);
                    addToWithGroupByFields("substr(customer.sp_geog,1,5)", 1);
                    addToWhereConditions("custprod.product_id(+) = tap_dollars.product_id");
                    addToWhereConditions("custprod.district  = geographies.sp_geog");
                    addToWithWhereConditions("products.product_id = customer_product.product_id", 1);
                    addToWithWhereConditions("products.sp_load_total = 'T'", 1);
                    addToWithWhereConditions("products.period_yyyy = " + srYear + " ", 1);
                    addToWithWhereConditions("customer.vista_customer_number = customer_product.vista_customer_number", 1);
                }
            } 
        } else if (groupByDistrict && groupBySummaryProduct){
            setMultiLevel();
            addToWithSelectFields("substr(customer.sp_geog,1,5) district", 0);
            addToSelectFields("geographies.description description");
            addToSelectFields("geographies.sp_geog id");
            addToSelectFields("sumprod.product_description description2");
            addToSelectFields("sumprod.product_id id2");
            addToFromTable("geographies");
            addToFromTable("products sumprod");
            addToWhereConditions("custlist.district = geographies.sp_geog");
            addToWhereConditions("sumprod.product_id = tap_dollars.product_id");
            addToWhereConditions("sumprod.sp_load_total = 'S'");
            addToWhereConditions("sumprod.period_yyyy = " + srYear + " ");
            addToGroupByFields("geographies.sp_geog, geographies.description, sumprod.product_id, sumprod.product_description ");
            if (showPotential || showForecast || showCompetitor){
                if (showPotential || showForecast || showCompetitor){
                    setWithSelectDistinct(1);
                    addToWithFromTable("customer", 1);
                    addToWithSelectFields("customer_product.product_id", 1);
                    addToWithSelectFields("substr(customer.sp_geog,1,5) district", 1);
                    addToWithGroupByFields("customer_product.product_id", 1);
                    addToWithGroupByFields("substr(customer.sp_geog,1,5)", 1);
                    addToWhereConditions("custprod.product_id(+) = tap_dollars.product_id");
                    addToWhereConditions("custprod.district = geographies.sp_geog");
                    addToWithWhereConditions("customer.vista_customer_number = customer_product.vista_customer_number", 1);
                }
            } 
        } else if ((groupByPrimarySegment && groupByDistrict) || (groupBySecondarySegment && groupByDistrict)){
            int segLevel = 0;
            if (groupByPrimarySegment){
                segLevel = 1;
            } else {
                segLevel = 2;
            }
            setMultiLevel();
            addToWithSelectFields("customer.sp_geog", 0);
            addToWithSelectFields("segments.segment_id", 0);
            addToWithFromTable("customer_segments", 0);
            addToWithFromTable("segments", 0);
            addToWithWhereConditions("customer_segments.vista_customer_number = customer.vista_customer_number", 0);
            addToWithWhereConditions("segments.segment_id = customer_segments.segment_id", 0);
            addToWithWhereConditions("segments.segment_level = " + segLevel, 0);
            addToSelectFields("geographies.description description2");
            addToSelectFields("geographies.sp_geog id2");
            addToSelectFields("segments.segment_id id");
            addToSelectFields("segments.segment_name description");
            addToFromTable("segments");
            addToFromTable("geographies");
            addToWhereConditions("segments.segment_id = custlist.segment_id");
            addToWhereConditions("substr(custlist.sp_geog,1,5) = geographies.sp_geog");
            addToGroupByFields("segments.segment_id, segments.segment_name, geographies.sp_geog, geographies.description");
            if (showPotential || showForecast || showCompetitor){
                addToWithFromTable("customer_segments", 1);
                addToWithFromTable("segments", 1);
                addToWithWhereConditions("customer_segments.vista_customer_number = customer.vista_customer_number", 1);
                addToWithWhereConditions("segments.segment_id = customer_segments.segment_id", 1);
                addToWithWhereConditions("segments.segment_level = " + segLevel, 1);
                addToWithSelectFields("customer_segments.segment_id", 1);
                addToWithFromTable("customer", 1);
                addToWithWhereConditions("customer.vista_customer_number = customer_product.vista_customer_number", 1);
                addToWithSelectFields("substr(customer.sp_geog,1,5) district", 1);
                addToWithGroupByFields("substr(customer.sp_geog,1,5)", 1);
                addToWithGroupByFields("customer_segments.segment_id", 1);
                addToWhereConditions("custprod.district = geographies.sp_geog");
                addToWhereConditions("custprod.segment_id (+) = segments.segment_id");
            }            
        } else if (groupByZone){
            addToWithSelectFields("customer.sp_geog", 0);
            addToFromTable("geographies");
            addToSelectFields("geographies.description");
            addToSelectFields("substr(custlist.sp_geog,1,4)||\'0\' id");
            addToOrderByFields("geographies.description");
            addToGroupByFields("substr(custlist.sp_geog,1,4)||\'0\'");
            addToGroupByFields("geographies.description");
            addToWhereConditions("substr(custlist.sp_geog,1,4)||\'0\' = geographies.sp_geog");
           if (showPotential || showForecast || showCompetitor){
                addToWithFromTable("customer", 1);
                addToWithSelectFields("substr(customer.sp_geog,1,4)||\'0\' zone", 1);
                addToWithGroupByFields("substr(customer.sp_geog,1,4)||\'0\'", 1);
                addToWhereConditions("custprod.zone (+) = substr(custlist.sp_geog,1,4)||\'0\'");
                addToWithWhereConditions("customer.vista_customer_number = customer_product.vista_customer_number", 1);
            }
        } else if (groupByDistrict){
            addToWithSelectFields("customer.sp_geog", 0);
            addToFromTable("geographies");
            addToSelectFields("geographies.description");
            addToSelectFields("substr(custlist.sp_geog,1,5) id");
            addToOrderByFields("geographies.description");
            addToGroupByFields("substr(custlist.sp_geog,1,5)");
            addToGroupByFields("geographies.description");
            addToWhereConditions("substr(custlist.sp_geog,1,5) = geographies.sp_geog");
            if (showPotential || showForecast || showCompetitor){
                addToWithFromTable("customer", 1);
                addToWithSelectFields("substr(customer.sp_geog,1,5) district", 1);
                addToWithGroupByFields("substr(customer.sp_geog,1,5)", 1);
                addToWhereConditions("custprod.district (+) = substr(custlist.sp_geog,1,5)");
                addToWithWhereConditions("customer.vista_customer_number = customer_product.vista_customer_number", 1);
            }
        } else if (groupByTeam){
            addToWithSelectFields("customer.sp_geog", 0);
            addToWithFromTable("geographies", 0);
            addToWithWhereConditions("geographies.sp_geog = customer.sp_geog", 0);
            addToWithWhereConditions("geographies.team is not null", 0);
            addToFromTable("geographies");
            addToSelectFields("geographies.description");
            addToSelectFields("custlist.sp_geog id");
            addToOrderByFields("geographies.description");
            addToGroupByFields("custlist.sp_geog");
            addToGroupByFields("geographies.description");
            addToWhereConditions("custlist.sp_geog = geographies.sp_geog");
            if (showPotential || showForecast || showCompetitor){
                addToWithFromTable("customer", 1);
                addToWithFromTable("geographies", 1);
                addToWithSelectFields("customer.sp_geog team", 1);
                addToWithGroupByFields("customer.sp_geog", 1);
                addToWhereConditions("custprod.team (+) = custlist.sp_geog");
                addToWithWhereConditions("customer.vista_customer_number = customer_product.vista_customer_number", 1);
                addToWithWhereConditions("geographies.sp_geog = customer.sp_geog", 1);
                addToWithWhereConditions("geographies.team is not null", 1);
            }
        } else if (groupBySalesOrg){
            addToWithSelectFields("customer.sp_geog", 0);
            addToFromTable("geographies");
            addToSelectFields("geographies.description");
            addToSelectFields("substr(custlist.sp_geog,1,1)||\'0000\' id");
            addToOrderByFields("geographies.description");
            addToGroupByFields("substr(custlist.sp_geog,1,1)||\'0000\'");
            addToGroupByFields("geographies.description");
            addToWhereConditions("substr(custlist.sp_geog,1,1)||\'0000\' = geographies.sp_geog");
           if (showPotential || showForecast || showCompetitor){
                addToWithFromTable("customer", 1);
                addToWithSelectFields("substr(customer.sp_geog,1,1)||\'0000\' salesorg", 1);
                addToWithGroupByFields("substr(customer.sp_geog,1,1)||\'0000\'", 1);
                addToWhereConditions("custprod.salesorg (+) = substr(custlist.sp_geog,1,1)||\'0000\'");
                addToWithWhereConditions("customer.vista_customer_number = customer_product.vista_customer_number", 1);
            }
        } else if (groupByGroupCode){
            addToWithSelectFields("customer.sp_geog", 0);
            addToFromTable("geographies");
            addToSelectFields("geographies.description");
            addToSelectFields("substr(custlist.sp_geog,1,2)||\'000\' id");
            addToOrderByFields("geographies.description");
            addToGroupByFields("substr(custlist.sp_geog,1,2)||\'000\'");
            addToGroupByFields("geographies.description");
            addToWhereConditions("substr(custlist.sp_geog,1,2)||\'000\' = geographies.sp_geog");
           if (showPotential || showForecast || showCompetitor){
                addToWithFromTable("customer", 1);
                addToWithSelectFields("substr(customer.sp_geog,1,2)||\'000\' groupcode", 1);
                addToWithGroupByFields("substr(customer.sp_geog,1,2)||\'000\'", 1);
                addToWhereConditions("custprod.groupcode (+) = substr(custlist.sp_geog,1,2)||\'000\'");
                addToWithWhereConditions("customer.vista_customer_number = customer_product.vista_customer_number", 1);
            }
        } else if (groupByProduct){
            setWithSelectDistinct(0);
            addToWithFromTable("tap_dollars", 0);
            addToWithFromTable("products", 0);
            addToWithSelectFields("tap_dollars.product_id", 0);
            addToWithWhereConditions("tap_dollars.vista_customer_number = customer.vista_customer_number", 0);
            addToWithWhereConditions("products.product_id = tap_dollars.product_id", 0);
            addToWithWhereConditions("products.sp_load_total = 'L'", 0);
            addToWithWhereConditions("products.period_yyyy = " + srYear + " ", 0);
            addToGroupByFields("tap_dollars.product_id");
            addToGroupByFields("products.product_description || '(' || products.product_id || ')'");
            addToWhereConditions("tap_dollars.product_id = custlist.product_id");
            addToWhereConditions("products.product_id = custlist.product_id");
            addToWhereConditions("products.period_yyyy = " + srYear + " ");
            addToFromTable("tap_dollars");
            addToFromTable("products");
            addToSelectFields("tap_dollars.product_id id");
            addToSelectFields("products.product_description || '(' || products.product_id || ')' description");
            addToOrderByFields("description");
            if (showPotential || showForecast || showCompetitor){
                setWithSelectDistinct(1);
                addToWithFromTable("products", 1);
                addToWithSelectFields("customer_product.product_id", 1);
                addToWithGroupByFields("customer_product.product_id", 1);
                addToWhereConditions("custprod.product_id(+) = custlist.product_id");
                addToWithWhereConditions("products.product_id = customer_product.product_id", 1);
                addToWithWhereConditions("products.sp_load_total = 'L'", 1);
                addToWithWhereConditions("products.period_yyyy = " + srYear + " ", 1);
            }
        } else  if (groupBySummaryProduct){
            setWithSelectDistinct(0);
            addToWithFromTable("tap_dollars", 0);
            addToWithSelectFields("tap_dollars.product_id", 0);
            addToWithWhereConditions("tap_dollars.vista_customer_number = customer.vista_customer_number", 0);
            addToGroupByFields("sumprod.product_id, sumprod.product_description");
            addToWhereConditions("tap_dollars.product_id = custlist.product_id");
            addToWhereConditions("sumprod.product_id = custlist.product_id");
            addToWhereConditions("sumprod.sp_load_total = 'S'");
            addToWhereConditions("sumprod.period_yyyy = " + srYear + " ");
            addToFromTable("tap_dollars");
            addToFromTable("products sumprod");
            addToSelectFields("sumprod.product_description description");
            addToSelectFields("sumprod.product_id id");
            addToOrderByFields("sumprod.product_description");
            if (showPotential || showForecast || showCompetitor){
                addToWithSelectFields("customer_product.product_id", 1);
                addToWithGroupByFields("customer_product.product_id", 1);
                addToWhereConditions("custprod.product_id(+) = custlist.product_id");
            }
        } else if (groupByPrimarySegment || groupBySecondarySegment){
            int segLevel = 0;
            if (groupByPrimarySegment){
                segLevel = 1;
            } else {
                segLevel = 2;
            }
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
            }
        }
        
// end of Group By sql
        
// beginning of Filter sql
               
        if (productFilter){
            setWithSelectDistinct(0);
            addToWithFromTable("tap_dollars", 0);
            StringBuffer whereClause = new StringBuffer();
            whereClause.append("tap_dollars.product_id in (");
            for (int i=0; i < productValues.size(); i++){
                if (i > 0){
                    whereClause.append(", ");
                }
                whereClause.append("\'" + productValues.get(i) + "\'");
            }
            whereClause.append(")");
            addToWithWhereConditions(whereClause.toString(), 0);
            addToWhereConditions(whereClause.toString());
            addToWithWhereConditions("tap_dollars.vista_customer_number = customer.vista_customer_number", 0);
            if (showPotential || showForecast || showCompetitor){
                setWithSelectDistinct(1);
                StringBuffer whereClause2 = new StringBuffer();
                whereClause2.append("customer_product.product_id in (");
                for (int i=0; i < productValues.size(); i++){
                    if (i > 0){
                        whereClause2.append(", ");
                    }
                    whereClause2.append("\'" + productValues.get(i) + "\'");
                }
                whereClause2.append(")");
                addToWithWhereConditions(whereClause2.toString(), 1);
            }
     
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
     //           int firstZero = thisGeog.indexOf("0");
     //           String searchString = thisGeog;
     //           if (firstZero > -1){
     //               searchString = thisGeog.substring(0,firstZero);
     //           }
                String searchString = getGeogSearchString(thisGeog, DBConn);
                whereClause.append("'" + searchString + "%'");
            }
            whereClause.append(")");
            addToWithWhereConditions(whereClause.toString(), 0);
            if (showPotential || showForecast || showCompetitor){
                addToWithFromTable("customer", 1);
                addToWithWhereConditions("customer_product" + ".vista_customer_number = customer.vista_customer_number", 1);
                addToWithWhereConditions(whereClause.toString(), 1);
            }
            
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
            addToWithWhereConditions(whereClause.toString(), 0);
            if (showPotential || showForecast || showCompetitor){
                addToWithFromTable("customer_focus_type", 1);
                addToWithWhereConditions("customer_product" + ".vista_customer_number = customer_focus_type.vista_customer_number", 1);
                addToWithWhereConditions(whereClause.toString(), 1);
            }
        }
        
        if (parentFilter){
            StringBuffer whereClause = new StringBuffer();
            whereClause.append("customer.parent_num in (");
            for (int i=0; i < parentValues.size(); i++){
                if (i > 0){
                    whereClause.append(", ");
                }
                whereClause.append("\'" + parentValues.get(i) + "\'");
            }
            whereClause.append(")");
            addToWithWhereConditions(whereClause.toString(), 0);
            if (showPotential || showForecast || showCompetitor){
                addToWithFromTable("customer", 1);
                addToWithWhereConditions("customer_product" + ".vista_customer_number = customer.vista_customer_number", 1);
                addToWithWhereConditions(whereClause.toString(), 1);
            }            
        }
        
        if (seFilter){
            StringBuffer whereClause = new StringBuffer();
            whereClause.append("customer.sales_engineer1 in (");
            for (int i=0; i < seValues.size(); i++){
                if (i > 0){
                    whereClause.append(", ");
                }
                whereClause.append("\'" + seValues.get(i) + "\'");
            }
            whereClause.append(")");
            addToWithWhereConditions(whereClause.toString(), 0);
            if (showPotential || showForecast || showCompetitor){
                addToWithFromTable("customer", 1);
                addToWithWhereConditions("customer_product.vista_customer_number = customer.vista_customer_number", 1);
                addToWithWhereConditions(whereClause.toString(), 1);
            }            
        }
        
        if (segmentFilter){
            addToWithFromTable("customer_segments", 0);
            StringBuffer whereClause = new StringBuffer();
            whereClause.append("customer_segments.segment_id in (");
            for (int i=0; i < segmentValues.size(); i++){
                if (i > 0){
                    whereClause.append(", ");
                }
                whereClause.append("\'" + segmentValues.get(i) + "\'");
            }
            whereClause.append(")");
            addToWithWhereConditions(whereClause.toString(), 0);
            addToWithWhereConditions("customer_segments.vista_customer_number = customer.vista_customer_number", 0);
            if (showPotential || showForecast || showCompetitor){
                addToWithFromTable("customer_segments", 1);
                addToWithWhereConditions("customer_product.vista_customer_number = customer_segments.vista_customer_number", 1);
                addToWithWhereConditions(whereClause.toString(), 1);
            }  
            
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
            addToWithWhereConditions(whereClause.toString(), 0);
            if (showPotential || showForecast || showCompetitor){
                addToWithFromTable("customer", 1);
                addToWithWhereConditions(whereClause.toString(), 1);
            }  
            
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
            addToWithWhereConditions(whereClause.toString(), 0);
            addToWithWhereConditions("cust_special_programs_xref.vista_customer_number = customer.vista_customer_number", 0);
            if (showPotential || showForecast || showCompetitor){
                addToWithFromTable("cust_special_programs_xref", 1);
                addToWithWhereConditions("customer_product.vista_customer_number = cust_special_programs_xref.vista_customer_number", 1);
                addToWithWhereConditions(whereClause.toString(), 1);
            }  
            
        }
        
//  end of Filter sql
        
        if (isLimitedQuery() || isMultiLevel()){
            addToOuterSelectFields("description");
            addToOuterSelectFields("id");
            if (isMultiLevel()){
                addToOuterSelectFields("description2");
                addToOuterSelectFields("id2");
            }
        }

//  Beginning of dollar type sql
        
        if (showPotential || showForecast || showCompetitor){
            addToWithNames("custprod");
            addToWithFromTable("customer_product",  1);
            addToFromTable("custprod");
            
            if (!groupByProduct && !groupBySummaryProduct && !productFilter && !emailingListOnly){
                addToWithFromTable("products", 1);
                addToWithWhereConditions("products.product_id = customer_product.product_id", 1);
                addToWithWhereConditions("products.sp_load_total = 'T'", 1);
                addToWithWhereConditions("products.period_yyyy = " + srYear , 1);
            }

            if (showPotential){
            	if (!emailingListOnly){
            		addToWithSelectFields("sum(" + "customer_product" + ".potential_dollars) potential_dollars", 1);
            		addToSelectFields("max(custprod.potential_dollars) \"Potential $\"");
            	}
                if (isLimitedQuery() || isMultiLevel()){
                    addToOuterSelectFields("\"Potential $\"");
                }
            }
            if (showForecast){
            	if (!emailingListOnly){
            		addToWithSelectFields("sum(" + "customer_product" + ".forecast_dollars) forecast_dollars", 1);
            		addToSelectFields("max(custprod.forecast_dollars) \"Forecast $\"");
            	}
                if (isLimitedQuery() || isMultiLevel()){
                    addToOuterSelectFields("\"Forecast $\"");
                }
            }
            if (showCompetitor){
            	if (!emailingListOnly){
            		addToWithSelectFields("sum(nvl(customer_product.competitor_dollars,0) + nvl(customer_product.competitor2_dollars,0)) competitor_dollars", 1);
            		addToSelectFields("max(custprod.competitor_dollars) \"Competitor $\"");
            	}
                if (isLimitedQuery() || isMultiLevel()){
                    addToOuterSelectFields("\"Competitor $\"");
                }
            }
            if (targetAccounts){
                addToWithFromTable("customer", 1);
                addToWithWhereConditions("customer.vista_customer_number = customer_product.vista_customer_number", 1);
                addToWithWhereConditions("customer.target_account_flag = 'Y'", 1);
            }
           
        }
      
      // If it's only for a mailing list, don't calculate totals, just return contact info
      if (emailingListOnly){
      	setSelectDistinct();
      	addToSelectFields("contacts.first_name");
      	addToSelectFields("contacts.last_name");
      	addToSelectFields("contacts.phone_number");
      	addToSelectFields("contacts.title");
      	addToSelectFields("contacts.email_address");
      	addToSelectFields("contacts.fax_number");
      	addToSelectFields("contact_titles.description title_description");
      	addToSelectFields("customer_address.address_line1");
      	addToSelectFields("customer_address.city");
      	addToSelectFields("customer_address.state");
      	addToSelectFields("customer_address.zip");
      	addToSelectFields("custlist.company_phone_number");
      	addToWithSelectFields("customer.phone_number company_phone_number", 0);
      	addToFromTable("contacts");
      	addToFromTable("contact_titles");
      	addToFromTable("code_types");
      	addToFromTable("customer_address");
      	addToWhereConditions("contacts.TITLE_ID = contact_titles.TITLE_ID(+)");
      	if (showPotential || showForecast || showCompetitor){
      		addToWhereConditions("(contacts.VISTA_CUSTOMER_NUMBER  = custlist.vista_customer_number or contacts.vista_customer_number = custprod.vista_customer_number)");
      	} else {
      		addToWhereConditions("contacts.VISTA_CUSTOMER_NUMBER  = custlist.vista_customer_number");
      	}
      	addToWhereConditions("CODE_TYPES.CODE_DESCRIPTION  = 'Business Address'");
      	addToWhereConditions("CUSTOMER_ADDRESS.VISTA_CUSTOMER_NUMBER (+) = custlist.vista_customer_number");
      	addToWhereConditions("CODE_TYPES.CODE_TYPE_ID = CUSTOMER_ADDRESS.ADDRESS_TYPE_ID");
      	      	
      } else {
             
        for (int i=0; i < dollarTypeList.size(); i++){
            DollarTypeRptBean dollarBean = new DollarTypeRptBean();
            dollarBean = (DollarTypeRptBean) dollarTypeList.get(i);
            String dollarType = dollarBean.getDollarType();
            
            // fieldPrefix is for credit, end_mkt, or direct
            String fieldPrefix = "tap_dollars";
            String labelPrefix = "Tap";
            /*            if (dollarType.equalsIgnoreCase("credit")){
                fieldPrefix = "credit";
                labelPrefix = "Credit";
            } else if (dollarType.equalsIgnoreCase("end")){
                fieldPrefix = "end_mkt";
                labelPrefix = "End Mrkt";
            } else {
                //   "charge to" is called "direct" in TAP tables
                fieldPrefix = "direct";
                labelPrefix = "Charge";
            }
 */           

            for (int so=0; so < 2; so++){
                // salesOrders is to denote Sales or Orders
                String salesOrders = null;
                boolean keepGoing = false;
                if (so == 0 && dollarBean.useOrders()){
                        salesOrders = "Order";
                        keepGoing = true;
                } else if (so == 1 && dollarBean.useSales()){
                        salesOrders = "Invoice";
                        keepGoing = true;
                } else {
                        keepGoing = false;
                }
                                
                if (keepGoing){
                
                        if (dollarBean.useMonthly()){
                        	String label = labelPrefix + " Month " + salesOrders;
                            addToSelectFields("nvl(sum(case when tap_dollars.year = " + year + " and tap_dollars.month = " + month + " then tap_dollars." + salesOrders + "_" + fieldPrefix + " else 0 end) ,0) \"" + label + "\"");
                            if (isLimitedQuery() || isMultiLevel()){
                                addToOuterSelectFields("\"" + label + "\"");
                            }
                        }

                        if (dollarBean.useYTDmonthly()){
                            for (int x=1; x < month + 1; x++){
                            	String label = labelPrefix + " YTD " + salesOrders;
                                addToSelectFields("nvl(sum(case when tap_dollars.year = " + year + " and tap_dollars.month <= " + month + " then tap_dollars." + salesOrders + "_" + fieldPrefix + " else 0 end) ,0) \"" + label + "\"");
                                if (isLimitedQuery() || isMultiLevel()){
                                    addToOuterSelectFields("\"" + label + "\"");
                                }
                            }

                        }
                        
                        if (dollarBean.usePrevYTDmonthly()){
                            for (int x=1; x < month + 1; x++){
                            	String label = labelPrefix + " Prev YTD " + salesOrders;
                                addToSelectFields("nvl(sum(case when tap_dollars.year = " + (year - 1) + " and tap_dollars.month <= " + month + " then tap_dollars." + salesOrders +  "_" + fieldPrefix + " else 0 end) ,0) \"" + label + "\"");
                                if (isLimitedQuery() || isMultiLevel()){
                                    addToOuterSelectFields("\"" + label + "\"");
                                }
                            }

                        }

                        if (dollarBean.usePrevYearTotal()){
                        	String label = labelPrefix + " Prev Year " + salesOrders;
                            addToSelectFields("nvl(sum(case when tap_dollars.year = " + (year - 1) + " then tap_dollars." + salesOrders + "_" + fieldPrefix + " else 0 end) ,0) \"" + label + "\"");
                            if (isLimitedQuery() || isMultiLevel()){
                                    addToOuterSelectFields("\"" + label + "\"");
                            }
                        }


                        if (dollarBean.usePrevYearMonthly()){
                        	String label = labelPrefix + " Prev YR Month " + salesOrders;
                            addToSelectFields("nvl(sum(case when tap_dollars.year = " + (year - 1) + " and tap_dollars.month = " + month + " then tap_dollars." + salesOrders + "_" + fieldPrefix + " else 0 end) ,0) \"" + label + "\"");
                            if (isLimitedQuery() || isMultiLevel()){
                                    addToOuterSelectFields("\"" + label + "\"");
                            }
                        }
                }
            
            }
             
        }
     } // end of else for emailingListOnly condition
        
        
//  end of dollar type sql        
        
 //
        return createSQLQuery();
        
    }
    
    //  returns the text appearing at the top of the first column in the report
    public String returnDescriptionColumnHeading (){
                
        if (groupByZone && groupByDistrict){
            return "Zone / District";
        } else if (groupByDistrict && groupByProduct){
            return "District / Product";
        } else if (groupByDistrict && groupBySummaryProduct){
            return "District / Summary Product";
        } else if (groupByDistrict && groupByTeam){
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
        } else if (groupBySalesOrg){
            return "Sales Organization";
        } else if (groupByGroupCode){
            return "Group Code";
        } else if (groupBySE){
            return "Sales Engineer";
        } else if (groupByProduct){
            return "Product Line";
        } else if (groupBySummaryProduct){
            return "Summary Product";
        } else if (groupByPrimarySegment){
            return "Primary Segment";
        } else if (groupBySecondarySegment){
            return "Secondary Segment";
        }
        
        // if it doesn't find any of the above
        return "Description";
        
    }
   
    //** Returns an ArrayList of Column Headers for report
    public ArrayList returnColumnHeadings(){
        ArrayList headers = new ArrayList();
        
        
        if (showPotential){
            headers.add("Potential $");
        }
        if (showForecast){
            headers.add("Forecast $");
        }
        if (showCompetitor){
            headers.add("Competitor $");
        }
                
        for (int i=0; i < dollarTypeList.size(); i++){
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
        }
        return headers;
        
    }
    
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
        
        if (parentFilter){
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
        }
       
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
    
    public void getGeogSecurityCode(){
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
	                
	                /*
	                 * added !ugs.isSalesId() because we dont want to add
	                 * a geog check for sales ids
	                 */ 
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
         
    }
    
    
    private String getGeogFilterString(UserGeogSecurity ugs){
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
    }
    
    // Returns String of Geography Names that the user is limited to by security for 
    // this specific search
     public String returnSecurityFilterString(Connection DBConn) throws Exception{
        
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
    
    }
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
      
    public boolean isGroupBySalesOrg(){
        return groupBySalesOrg;
    }  
    
    public boolean isGroupByGroupCode(){
        return groupByGroupCode;
    }
    
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
    
    public boolean isGroupBySummaryProduct(){
        return groupBySummaryProduct;
    }
    
   
    public boolean isGroupByPrimarySegment(){
        return groupByPrimarySegment;
    }
    
    public boolean isGroupBySecondarySegment(){
        return groupBySecondarySegment;
    }
    
    
    
}