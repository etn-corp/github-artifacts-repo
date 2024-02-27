/*
 * Created on Mar 28, 2005
 *
 */
package com.eaton.electrical.smrc.dao;

import com.eaton.electrical.smrc.bo.*;

/**
 * @author Jason Lubbert
 *
 * The class does not actually execute queries, but it contains the sql that is executed
 * for charting.
 * 
 */
public class ChartingQueries {

    public static ChartQueryBean getZoneMarketShareQuery(String zone){
        ChartQueryBean bean = new ChartQueryBean();
        String zoneMarketShareQuery = "select year \"Year\", pct_market_share \"Market Share\" from market_share where sp_geog = '" + zone + "' order by year ";
        
        bean.setTheQuery(zoneMarketShareQuery);
        bean.setSeriesLabel("Year");
        bean.addPointLabels("Market Share");
        
        return bean;
    }
    
    public static ChartQueryBean getZoneTotalSalesQuery(String zone, int srYear, int srMonth, String salesOrders){
        ChartQueryBean bean = new ChartQueryBean();
        String totalSalesChartQuery = "with se as ( select g.description, salesman_id from salesman_detail_mv, geographies g where sp_geog_cd like '" + zone.substring(0,4) + "%' " +
        "and inactive_dt is null and to_number(to_char(start_dt,'YYYY')) = " + srYear + " and g.sp_geog = '" + zone + "' ) " +
        "select se.description \"Zone\", sum(case when (month <= " + srMonth + ") and (year = " + srYear + ") then u.total_" + salesOrders + " else 0 end) \"" + srYear + " YTD\", " +
        "sum(case when (month <= " + srMonth + ") and (year = (" + srYear + " - 1)) then u.total_" + salesOrders + " else 0 end) \"" + (srYear - 1) + " YTD\", " +
        "sum(case when (month <= " + srMonth + ") and (year = (" + srYear + " - 2)) then u.total_" + salesOrders + " else 0 end) \"" + (srYear - 2) + " YTD\" " +
        "from oemapnew.credit_salesman_sales u, se, products where u.salesman_id = se.salesman_id " +
        "and u.year >= (" + srYear + " - 2) " +
        " and products.product_id = u.product_id and products.sp_load_total = 'T' and " +
	    "products.period_yyyy = " + srYear + " " +
        " group by se.description";
        
        bean.setTheQuery(totalSalesChartQuery);
        bean.addPointLabels(srYear + " YTD");
        bean.addPointLabels((srYear - 1) + " YTD");
        bean.addPointLabels((srYear - 2) + " YTD");
        bean.setSeriesLabel("Zone");
        return bean;
    }
    
    public static ChartQueryBean getZoneSalesByProductsLineQuery(String zone, int srYear, int srMonth, String salesOrders){
        ChartQueryBean bean = new ChartQueryBean();
        String salesByProductLinesQuery = " with se as ( select salesman_id, substr(sp_geog_cd,1,5) district from salesman_detail_mv " +
        "where sp_geog_cd like '" + zone.substring(0,4) + "%' and inactive_dt is null and to_number(to_char(start_dt,'YYYY')) = " + srYear + " ) " +
        ", sumProd as ( select * from products where sp_load_total = 'S' and period_yyyy = " + srYear + " ) " +
        "select sumProd.product_description \"Product\", sum(case when (month <= " + srMonth + ") and (year = " + srYear + ") then u.total_" + salesOrders + " else 0 end) \"" + srYear + " YTD\", " +
        "sum(case when (month <= " + srMonth + ") and (year = " + (srYear - 1) + ") then u.total_" + salesOrders + " else 0 end) \"" + (srYear - 1) + " YTD\" " +
        "from oemapnew.credit_salesman_sales u, se, sumProd where u.salesman_id = se.salesman_id and sumProd.product_id = u.product_id " +
        "and sumProd.period_yyyy = " + srYear + 
        "and u.year >= (" + srYear + " - 1) group by sumProd.product_description";
        
        bean.setTheQuery(salesByProductLinesQuery);
        bean.setSeriesLabel("Product");
        bean.addPointLabels(srYear + " YTD");
        bean.addPointLabels((srYear - 1) + " YTD");
        return bean;
    }
    
    public static ChartQueryBean getZoneSalesByDistrictQuery(String zone, int srYear, int srMonth, String salesOrders){
        ChartQueryBean bean = new ChartQueryBean();
        String salesByDistrictQuery = "with se as ( select salesman_id, substr(sp_geog_cd,1,5) district from salesman_detail_mv " +
        "where sp_geog_cd like '" + zone.substring(0,4) + "%' and inactive_dt is null and to_number(to_char(start_dt,'YYYY')) = " + srYear + " ) " +
        "select g.description \"District\", sum(case when (month <= " + srMonth + ") and (year = " + srYear + ") then u.total_" + salesOrders + " else 0 end) \"" + srYear + " YTD\", " +
        "sum(case when (month <= " + srMonth + ") and (year = " + srYear + " - 1) then u.total_" + salesOrders + " else 0 end) \"" + (srYear - 1) + " YTD\" " +
        "from oemapnew.credit_salesman_sales u, geographies g, se, products where u.salesman_id = se.salesman_id " + 
        "and g.sp_geog = se.district and u.year >= (" + srYear + " - 1) " +
        " and products.product_id = u.product_id and products.sp_load_total = 'T' and " +
		"products.period_yyyy = " + srYear + " " +
        "group by g.description";
        
        bean.setTheQuery(salesByDistrictQuery);
        bean.setSeriesLabel("District");
        bean.addPointLabels(srYear + " YTD");
        bean.addPointLabels((srYear - 1) + " YTD");
        return bean;
    }
    
    public static ChartQueryBean getZoneForecastVsActualByDistrictQuery(String zone, int srYear, int srMonth, String salesOrders){
        ChartQueryBean bean = new ChartQueryBean();
        String forecastVersusActualByDistrictQuery = " with se as ( select salesman_id, substr(sp_geog_cd,1,5) district " +
        "from salesman_detail_mv where sp_geog_cd like '" + zone.substring(0,4) + "%' and inactive_dt is null and to_number(to_char(start_dt,'YYYY')) = " + srYear +
        "), forecast as ( select df.sp_geog district, sum(case when to_date(to_char(nvl(df.month, 12)) || '/' || to_char(nvl(df.year,9999)), 'MM/YYYY') >  (add_months(to_date(to_char(" + srMonth + ") || '/' || to_char(" + srYear + "), 'MM/YYYY'), -12)) and to_date(to_char(df.month) || '/' || to_char(df.year), 'MM/YYYY') <= to_date(to_char(" + srMonth + ") || '/' || to_char(" + srYear + "), 'MM/YYYY') then df.forecast_dollars else 0 end) forecast_dollars " +
        "from district_forecasts df where df.sp_geog like '" + zone.substring(0,4) + "%' group by df.sp_geog ) " +
        "select g.description \"District\", forecast.forecast_dollars \"Forecast $\", sum(case when to_date(to_char(u.month) || '/' || to_char(u.year), 'MM/YYYY') >  (add_months(to_date(to_char(" + srMonth + ") || '/' || to_char(" + srYear + "), 'MM/YYYY'), -12)) and to_date(to_char(u.month) || '/' || to_char(u.year), 'MM/YYYY') <= to_date(to_char(" + srMonth + ") || '/' || to_char(" + srYear + "), 'MM/YYYY') then u.total_" + salesOrders + " else 0 end) \"Prev 12 months\" " +
        "from oemapnew.credit_salesman_sales u, geographies g, se, forecast, products where u.salesman_id = se.salesman_id and g.sp_geog = se.district " +
        "and forecast.district(+) = se.district and products.product_id = u.product_id and products.sp_load_total = 'T' and " +
        " products.period_yyyy = " + srYear + " " + 
        " group by g.description, forecast.forecast_dollars " +
        "order by g.description";
        
        bean.setTheQuery(forecastVersusActualByDistrictQuery);
        bean.setSeriesLabel("District");
        bean.addPointLabels("Forecast $");
        bean.addPointLabels("Prev 12 months");
        return bean;
    }
    
    public static ChartQueryBean getDistrictGoalVsSummaryProductsQuery(String district, int srYear, int srMonth, String salesOrders){
        ChartQueryBean bean = new ChartQueryBean();
        String goalVsActualSummaryProductsQuery = "with se as ( select salesman_id, substr(sp_geog_cd,1,5) district from salesman_detail_mv " +
        "where sp_geog_cd like '" + district + "%' and inactive_dt is null and to_number(to_char(start_dt,'YYYY')) = " + srYear + ") " +
        ", sumProd as ( select * from products where sp_load_total = 'S' " +
        "and period_yyyy = " + srYear + " ) " +
        "select sumProd.product_description \"Product\", sum(case when (month <= " + srMonth + " ) and (year = " + srYear + " ) then u.total_" + salesOrders + " else 0 end) \"Actual YTD " + salesOrders + "\", " +
        "sum(case when (month <= " + srMonth + " ) and (year = " + srYear + " ) then u.goals_sales else 0 end) \"YTD Goals\"" +
        "from oemapnew.credit_salesman_sales u, se, sumProd where u.salesman_id = se.salesman_id  " +
        "and sumProd.product_id = u.product_id and sumProd.period_yyyy = " + srYear  +
        "and u.year >= " + (srYear - 1) + " group by sumProd.product_description";
        
        bean.setTheQuery(goalVsActualSummaryProductsQuery);
        bean.setSeriesLabel("Product");
        bean.addPointLabels("Actual YTD " + salesOrders);
        bean.addPointLabels("YTD Goals");
        return bean;
    }
    
    public static ChartQueryBean getDistrictMarketShareQuery(String district, int srYear){
        ChartQueryBean bean = new ChartQueryBean();
        String districtMarketShareQuery = "select year \"Year\", pct_market_share \"Market Share\" from oemapnew.market_share " +
        "where sp_geog = '" + district + "' and year > (" + srYear + " - 5)";
        
        bean.setTheQuery(districtMarketShareQuery);
        bean.setSeriesLabel("Year");
        bean.addPointLabels("Market Share");
        return bean;
    }
    
    public static ChartQueryBean getDistrictForecastVsActualMonthlyQuery(String district, int srYear, int srMonth, String salesOrders){
        ChartQueryBean bean = new ChartQueryBean();
        String districtForecastVsActualMonthQuery = "with se as ( select salesman_id, substr(sp_geog_cd,1,5) district " +
        "from salesman_detail_mv where sp_geog_cd like '" + district + "%' and inactive_dt is null and to_number(to_char(start_dt,'YYYY')) =  " + srYear + 
        "), forecast as ( select sp_geog district, forecast_dollars from district_forecasts where sp_geog = '" + district + "' " +
        "and year = " + srYear + " and month = " + srMonth + " ) " +
        "select g.description \"District\", forecast.forecast_dollars \"Monthly Forecast\", sum(case when (u.month = " + srMonth + " and (u.year = " + srYear + ")) then u.total_" + salesOrders + " else 0 end) \"Actual Month " + salesOrders + "\"  " +
        "from oemapnew.credit_salesman_sales u, geographies g, se, forecast, products where u.salesman_id = se.salesman_id " +
        "and g.sp_geog = se.district and forecast.district(+) = se.district " + 
        " and products.product_id = u.product_id and products.sp_load_total = 'T' and " +
    	"products.period_yyyy =  " + srYear + " " +
        "group by g.description, forecast.forecast_dollars order by g.description";
        
        bean.setTheQuery(districtForecastVsActualMonthQuery);
        bean.setSeriesLabel("District");
        bean.addPointLabels("Monthly Forecast");
        bean.addPointLabels("Actual Month " + salesOrders);
        return bean;
    }
    
    public static ChartQueryBean getDistrictFocusProductsQuery(String district, int srYear, int srMonth, String salesOrders){
        ChartQueryBean bean = new ChartQueryBean();
        String focusProductQuery = "with se as ( select salesman_id, substr(sp_geog_cd,1,5) district from salesman_detail_mv " +
        "where sp_geog_cd like '" + district + "%' and inactive_dt is null and to_number(to_char(start_dt,'YYYY')) = " + srYear + " )  " +
        ", focusProducts as ( select product_id from product_line_focus where period_yyyy = " + srYear + " ) " +
        "select products.product_description \"Product\", sum(case when (month <= " + srMonth + ") and (year = " + srYear + ") then u.total_" + salesOrders + " else 0 end) \"YTD " + salesOrders + "\", " +
        "sum(case when (month <= " + srMonth + ") and (year = " + srYear + ") then u.goals_sales else 0 end) \"YTD Goals \" " +
        "from oemapnew.credit_salesman_sales u, se, focusProducts, products where u.salesman_id = se.salesman_id and focusProducts.product_id = u.product_id  " +
        "and products.product_id = focusProducts.product_id and u.year >= (" + srYear + " - 1) group by products.product_description";

        bean.setTheQuery(focusProductQuery);
        bean.setSeriesLabel("Product");
        bean.addPointLabels("YTD " + salesOrders);
        bean.addPointLabels("YTD Goals");
        return bean;
    }
    
}
