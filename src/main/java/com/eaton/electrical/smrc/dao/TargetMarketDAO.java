/*
 * TargetMarketDAO.java
 *
 * Created on October 6, 2004, 8:23 AM
 */

package com.eaton.electrical.smrc.dao;

import java.sql.*;
import java.util.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.exception.SMRCException;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

/**
 *
 * @author Jason Lubbert
 */
public class TargetMarketDAO {

//	 TODO May need to use Years table here

		private static final String totalSalesQueryByTM = "with custSales as ( select tap_dollars.product_id, " +
			"nvl(sum(case when year = ? then invoice_tap_dollars else 0 end),0) curr_ytd, " +
                        "nvl(sum(case when year = (? - 1) then invoice_tap_dollars else 0 end),0) prev_year,  " +
                        "nvl(sum(case when year = (? - 1) and month >= ? and month <= ? then invoice_tap_dollars else 0 end),0) prev_plan_todate " + 
                        "from tap_dollars where tap_dollars.year >= (? - 1)  " +
                        "and tap_dollars.vista_customer_number in ( " +
                        "select vista_customer_number from target_mrkt_plan_accts where target_market_plan_id = ? ) " +
                        "group by product_id ) select products.product_id, products.product_description, products.seq_num, " +
                        "nvl(custSales.curr_ytd,0) curr_ytd, nvl(custSales.prev_year,0) prev_year, nvl(custSales.prev_plan_todate,0) prev_plan_todate " +
                        "from products, custSales where products.product_id  = custSales.product_id (+) and products.current_flag = 'Y' " +
                        "group by products.product_id, products.product_description, products.seq_num, curr_ytd, prev_year, prev_plan_todate order by seq_num ";
//       TODO May need to use Years table here
         
          private static final String endMarketSalesQueryByTM = "with custSales as ( select charge_end_market_sales.product_id, " +
			"nvl(sum(case when year = ? then total_sales else 0 end),0) curr_ytd, " +
                        "nvl(sum(case when year = (? - 1) then total_sales else 0 end),0) prev_year,  " +
                        "nvl(sum(case when year = (? - 1) and month >= ? and month <= ? then total_sales else 0 end),0) prev_plan_todate  " +
                        "from charge_end_market_sales where charge_end_market_sales.year >= (? - 1)  " +
                        "and charge_end_market_sales.charge_to_customer_number in ( " +
                        "select vista_customer_number from target_mrkt_plan_accts where target_market_plan_id = ? ) " +
                        "and charge_end_market_sales.end_market_customer_number in ( " +
                        "select vista_customer_number from target_mrkt_ec_sales_plans where target_market_plan_id = ? ) " +
                        "group by product_id ) select products.product_id, products.product_description, products.seq_num, " +
                        "nvl(custSales.curr_ytd,0) curr_ytd, nvl(custSales.prev_year,0) prev_year, nvl(custSales.prev_plan_todate,0) prev_plan_todate  " +
                        "from products, custSales where products.product_id  = custSales.product_id (+) and products.current_flag = 'Y' " +
                        "group by products.product_id, products.product_description, products.seq_num, curr_ytd, prev_year, prev_plan_todate order by seq_num ";
               
          private static final String endMarketSalesQueryByTMWithoutECs = "with custSales as ( select charge_end_market_sales.product_id, " +
			"nvl(sum(case when year = ? then total_sales else 0 end),0) curr_ytd, " +
                      "nvl(sum(case when year = (? - 1) then total_sales else 0 end),0) prev_year,  " +
                      "nvl(sum(case when year = (? - 1) and month >= ? and month <= ? then total_sales else 0 end),0) prev_plan_todate  " +
                      "from charge_end_market_sales where charge_end_market_sales.year >= (? - 1)  " +
                      "and charge_end_market_sales.charge_to_customer_number in ( " +
                      "select vista_customer_number from target_mrkt_plan_accts where target_market_plan_id = ? ) " +
                      "group by product_id ) select products.product_id, products.product_description, products.seq_num, " +
                      "nvl(custSales.curr_ytd,0) curr_ytd, nvl(custSales.prev_year,0) prev_year, nvl(custSales.prev_plan_todate,0) prev_plan_todate  " +
                      "from products, custSales where products.product_id  = custSales.product_id (+) and products.current_flag = 'Y' " +
                      "group by products.product_id, products.product_description, products.seq_num, curr_ytd, prev_year, prev_plan_todate order by seq_num ";
             
//       TODO May need to use Years table here
         
         // Braffet - This is changed to TAP Dollars.  Need to test
        private static final String totalSalesQueryByVcn = "with custSales as ( select tap_dollars.product_id, " +
                    "nvl(sum(case when year = ? then invoice_tap_dollars else 0 end),0) curr_ytd,  " +
                    "nvl(sum(case when year = (? - 1) then invoice_tap_dollars else 0 end),0) prev_year,  " +
                    "nvl(sum(case when year = (? - 1) and month <= ? then invoice_tap_dollars else 0 end),0) prev_sales_to_date  " +
                    "from tap_dollars where tap_dollars.year >= (? - 1)  " +
                    "and tap_dollars.vista_customer_number = ? " +
                    "group by product_id ) select products.product_id, products.product_description, products.seq_num, " +
                    "nvl(custSales.curr_ytd,0) curr_ytd, nvl(custSales.prev_year,0) prev_year, nvl(custSales.prev_sales_to_date,0) prev_sales_to_date " +
                    "from products, custSales where products.product_id  = custSales.product_id (+) and products.current_flag = 'Y' " +
                    "group by products.product_id, products.product_description, products.seq_num, curr_ytd, prev_year, prev_sales_to_date order by seq_num";
        
      
//      TODO May need to use Years table here
//		TODO Please don't put any comments in this code that could be helpful.  Please put in comments that say what needs to be done, but nothing to explain how it works.
 
        private static final String totalSalesQueryByTMApproved = "with custSales as ( select tap_dollars.product_id, " +
        "nvl(sum(case when year = ? then invoice_tap_dollars else 0 end),0) curr_ytd, " +
        "nvl(sum(case when year = (? - 1) then invoice_tap_dollars else 0 end),0) prev_year,  " +
        "nvl(sum(case when year = ? and month >= ? and month <= ? then invoice_tap_dollars else 0 end),0) sales_in_plan,  " +
        "nvl(sum(case when year = (? - 1) and month >= ? and month <= ? then invoice_tap_dollars else 0 end),0) sales_baseline,  " +
        "nvl(sum(case when year = (? - 1) and month >= ? and month <= ? then invoice_tap_dollars else 0 end),0) sales_baseline_todate  " +
        "from tap_dollars where tap_dollars.year >= (? - 1)  " +
        "and tap_dollars.vista_customer_number in (  " + 
        "select vista_customer_number from target_mrkt_plan_accts where target_market_plan_id = ? ) " +
        "group by product_id ) select products.product_id, products.product_description, products.seq_num, " +
        "nvl(custSales.curr_ytd,0) curr_ytd, nvl(custSales.prev_year,0) prev_year, " +
        "nvl(custSales.sales_in_plan, 0) sales_in_plan, nvl(custSales.sales_baseline,0) sales_baseline, nvl(custSales.sales_baseline_todate,0) sales_baseline_todate  " +
        "from products, custSales where products.product_id  = custSales.product_id (+)  and products.current_flag = 'Y' " +
        "group by products.product_id, products.product_description, products.seq_num, curr_ytd, sales_baseline, prev_year, sales_in_plan,  sales_baseline_todate order by seq_num";

        
        
//       TODO May need to use Years table here
        
         private static final String endMarketSalesQueryByTMApproved = "with custSales as ( select charge_end_market_sales.product_id, " +
                "nvl(sum(case when year = ? then total_sales else 0 end),0) curr_ytd, " +
                "nvl(sum(case when year = (? - 1) then total_sales else 0 end),0) prev_year,  " +
                "nvl(sum(case when year = ? and month >= ? and month <= ? then total_sales else 0 end),0) sales_in_plan,  " +
                "nvl(sum(case when year = (? - 1) and month >= ? and month <= ? then total_sales else 0 end),0) sales_baseline,  " +
                "nvl(sum(case when year = (? - 1) and month >= ? and month <= ? then total_sales else 0 end),0) sales_baseline_todate   " + 
                "from charge_end_market_sales where charge_end_market_sales.year >= (? - 1)  " +
                "and charge_end_market_sales.charge_to_customer_number in (  " +
                "select vista_customer_number from target_mrkt_plan_accts where target_market_plan_id = ? ) " +
                "and charge_end_market_sales.end_market_customer_number in (  " +
                "select vista_customer_number from target_mrkt_ec_sales_plans where target_market_plan_id = ? ) " +
                "group by product_id ) select products.product_id, products.product_description, products.seq_num, " +
                "nvl(custSales.curr_ytd,0) curr_ytd, nvl(custSales.prev_year,0) prev_year, " +
                "nvl(custSales.sales_in_plan, 0) sales_in_plan, nvl(custSales.sales_baseline,0) sales_baseline, nvl(custSales.sales_baseline_todate,0) sales_baseline_todate " +
                "from products, custSales where products.product_id  = custSales.product_id (+) and products.current_flag = 'Y'  " +
                "group by products.product_id, products.product_description, products.seq_num, curr_ytd, sales_baseline, prev_year, sales_in_plan, sales_baseline_todate order by seq_num";

        
        private static final String tmQuery = "select * from target_market_plans where target_market_plan_id = ?";
        private static final String accountTMIdQuery = "select target_market_plans.*, code_types.code_description " +
        		"     from target_mrkt_plan_accts, target_market_plans, code_types " +
        		"     where target_mrkt_plan_accts.vista_customer_number = ? " +
        		"     and target_market_plans.target_market_plan_id = target_mrkt_plan_accts.target_market_plan_id " +
        		"     and target_market_plans.target_market_plan_status_id = code_types.code_type_id" +
        		"     and code_types.code_description != 'Unsaved' " +
        		"     and code_types.code_description != 'Purged' " +
        		"      order by target_market_plans.start_date desc ";
        private static final String endCustomerQuery = "select vista_customer_number from target_mrkt_ec_sales_plans where target_market_plan_id = ?";
        private static final String otherAccountsQuery = "select vista_customer_number from target_mrkt_plan_accts where target_market_plan_id = ?";
        private static final String contactsQuery = "select contact_id from target_mrkt_plan_contacts where target_market_plan_id = ?";
        private static final String tmProductQuery = "select * from target_mrkt_plan_products where target_market_plan_id = ?";
        private static final String salesObjQuery = "select code_type_id, code_description from code_types where code_type = 'tm_sales_objective' order by code_seq_num";
        private static final String salesTrackingDollarsQuery = "select code_type_id, code_description from code_types where code_type = 'tm_sales_dollars' order by code_seq_num";
        private static final String allContactsForAllTMAccountsQuery = "select contacts.contact_id from target_mrkt_plan_accts, contacts, target_market_plans " +
                                    "where contacts.vista_customer_number = target_mrkt_plan_accts.vista_customer_number " +
                                    "and target_market_plans.target_market_plan_id = ? " +
                                    "and target_market_plans.target_market_plan_id = target_mrkt_plan_accts.target_market_plan_id";
        private static final String tmSegmentQuery = "select segment_id from target_mrkt_plan_segments where target_market_plan_id = ?";
        // There are 31 fields in targetMarketInsert
        private static final String targetMarketInsert = "insert into target_market_plans (target_market_plan_id, plan_description, contact_id, competitor_convert, competitor_id, commitment_level, business_objective," +
        " start_date, end_date, sales_objective, incremental_growth, sales_tracking_type_id, baseline_start_date, baseline_end_date, sales_objective_other_notes, sales_plans_used, percent_growth1, percent_payout1, " +
        " percent_growth2, percent_payout2, percent_growth3, percent_payout3, percent_growth4, percent_payout4, include_bidman_rebate, user_added, user_changed, date_added, date_changed, maximum_payout, " +
        " target_market_plan_status_id, segment_other_notes) " +
        "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        private static final String targetMarketPlansNextvalQuery = "SELECT target_market_plans_seq.nextval FROM DUAL";
        private static final String tmSegmentInsert = "insert into target_mrkt_plan_segments (target_market_plan_id, segment_id, user_changed, date_changed) values (?,?,?,?)";
        private static final String tmSegmentDelete = "delete target_mrkt_plan_segments where target_market_plan_id = ? and segment_id = ?";
        private static final String tmContactInsert = "insert into target_mrkt_plan_contacts (target_market_plan_id, contact_id, vista_customer_number, user_changed, date_changed) values (?,?,?,?,?)";
        private static final String tmContactDelete = "delete target_mrkt_plan_contacts where target_market_plan_id = ? and contact_id = ?";
        private static final String tmPlanAccountsInsert = "insert into target_mrkt_plan_accts (target_market_plan_id, vista_customer_number, user_changed, date_changed) values (?,?,?,?)";
        private static final String tmPlanAccountsDelete = "delete target_mrkt_plan_accts where target_market_plan_id = ? and vista_customer_number = ?";
        private static final String tmEndCustomersInsert = "insert into target_mrkt_ec_sales_plans (target_market_plan_id, vista_customer_number, user_changed, date_changed) values (?,?,?,?)";
        private static final String tmEndCustomersDelete = "delete target_mrkt_ec_sales_plans where target_market_plan_id = ? and vista_customer_number = ?";
        private static final String tmPlanProductsInsert = "insert into target_mrkt_plan_products (target_market_plan_id, product_id, period_yyyy, sales_objective, incremental_growth) values (?,?,?,?,?)";
        private static final String tmPlanProductsDelete = "delete target_mrkt_plan_products where target_market_plan_id = ? and product_id = ?";
        private static final String tmPlanProductsUpdate = "update target_mrkt_plan_products set period_yyyy = ?, sales_objective = ? where target_market_plan_id = ? and product_id = ?";
        private static final String targetMarketUpdate = "update target_market_plans set plan_description = ?, contact_id = ?, " +
                    "competitor_convert=?, competitor_id=?, commitment_level=?, business_objective=?, " +
                    "start_date=?, end_date=?, sales_objective=?, incremental_growth=?, sales_tracking_type_id=?, " +
                    "baseline_start_date=?, baseline_end_date=?, sales_objective_other_notes=?, " +
                    "sales_plans_used=?, percent_growth1=?, percent_payout1=?, percent_growth2=?, percent_payout2=?, " +
                    "percent_growth3=?, percent_payout3=?, percent_growth4=?, percent_payout4=?, " +
                    "user_changed=?, date_changed=?,maximum_payout=?,  " +
                    "segment_other_notes=?, target_market_plan_status_id=? where target_market_plan_id = ?";
        private static final String getPendingDMApprovalsQuery = "select distinct tmp.target_market_plan_id from target_market_plans tmp, customer c, target_mrkt_plan_accts tma, code_types ct " +  
        			"where target_market_plan_status_id = ct.code_type_id and ct.code_type = 'target_market_status' and ct.code_value = ? " +
        			"and tma.target_market_plan_id = tmp.target_market_plan_id and c.vista_customer_number = tma.vista_customer_number " +
        			"and c.sp_geog like ?"; 
        private static final String getPendingCMMApprovalsQuery = "select tmp.target_market_plan_id from target_market_plans tmp, code_types ct " +  
        			"where target_market_plan_status_id = ct.code_type_id and ct.code_type = 'target_market_status' and ct.code_value = ?";
        
        private static final String getPendingSEApprovalsQuery = 
        	" select distinct wa.target_market_plan_id " + 
        	" from workflow_approvals wa " +
        	" where wa.target_market_plan_id in " +
	        	" (select distinct tmp.target_market_plan_id " +
	        	" from target_market_plans    tmp, " +
	        	" customer               c, " +
	        	" target_mrkt_plan_accts tma, " +
	        	" code_types             ct " +
	        	" where target_market_plan_status_id = ct.code_type_id " +
	        	" and ct.code_type = 'target_market_status' " +
	        	" and ct.code_value = ? " +
	        	" and tma.target_market_plan_id = tmp.target_market_plan_id " +
	        	" and c.vista_customer_number = tma.vista_customer_number " +
	        	" and c.sp_geog like ?) " + 
        	" and wa.approved_flag = 'V' ";
        
        private static final String getTargetMarketPlanDescription = "select tmp.plan_description from target_market_plans tmp where tmp.target_market_plan_id = ?";
        
        private static final String getTargetMarketPlanStatusId = "select t.target_market_plan_status_id from target_market_plans t where t.target_market_plan_id = ?";
        
        private static final String updateTargetMarketStatus = "update target_market_plans tmp set tmp.target_market_plan_status_id = ? where tmp.target_market_plan_id = ?";
        
	public static ArrayList getSalesForTM(TargetMarket targetMarket, Connection DBConn) throws Exception {
	    /*
	     * TODO this needs to be changed so that it doesnt loop through and
	     * do another query.  Can t the product be joined?
	     */
		
		ArrayList results = new ArrayList();

		String srYear = MiscDAO.getSRYear(DBConn);
		String srMonth = MiscDAO.getSRMonth(DBConn);
		PreparedStatement pstmt = null;
		ResultSet rs  = null;
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(targetMarket.getStartDate());
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(targetMarket.getEndDate());
		
		try {
            if (targetMarket.isTAPDollars()){
                pstmt = DBConn.prepareStatement(totalSalesQueryByTM);
                if (SMRCLogger.isDebuggerEnabled()) {                	
                	SMRCLogger.debug("GetSalesForTM " + totalSalesQueryByTM);                	
                }
           } else if (targetMarket.isEndMarketSales()){
        	   if (targetMarket.getEndCustomers() != null & targetMarket.getEndCustomers().size() > 0){
                 pstmt = DBConn.prepareStatement(endMarketSalesQueryByTM);
                 pstmt.setInt(8, targetMarket.getTargetMarketPlanId());
        	   } else {
        		 pstmt = DBConn.prepareStatement(endMarketSalesQueryByTMWithoutECs); 
        	   }
                 if (SMRCLogger.isDebuggerEnabled()) {                	
                  	SMRCLogger.debug("GetSalesForTM " + endMarketSalesQueryByTM + "Argument 5 = " + targetMarket.getTargetMarketPlanId());                	
                 }
            } else {
                 throw new SMRCException("Invalid dollar type on target market " + targetMarket.getTargetMarketPlanId());
            }
            pstmt.setInt(1, startCal.get(Calendar.YEAR));
            pstmt.setInt(2, startCal.get(Calendar.YEAR));
            pstmt.setInt(3, startCal.get(Calendar.YEAR));
            pstmt.setInt(4, startCal.get(Calendar.MONTH));
            pstmt.setInt(5, Globals.a2int(srMonth));
            pstmt.setInt(6, startCal.get(Calendar.YEAR));
            pstmt.setInt(7, targetMarket.getTargetMarketPlanId());

            if (SMRCLogger.isDebuggerEnabled()) {            	
            	SMRCLogger.debug("Arguments : " + startCal.get(Calendar.YEAR) + ", " + startCal.get(Calendar.YEAR) + " ," 
            			+ startCal.get(Calendar.YEAR) + ", " + targetMarket.getTargetMarketPlanId());
            }
            
            rs = pstmt.executeQuery();
            
			while (rs.next())		
			{
                ArrayList record = new ArrayList();
                String productId = rs.getString("product_id");
                
                Product product = ProductDAO.productLookup(productId, Globals.a2int(srYear), DBConn);
                record.add(product);
                record.add((new Double(rs.getDouble("curr_ytd"))));
                record.add((new Double(rs.getDouble("prev_year"))));
                record.add((new Double(rs.getDouble("prev_plan_todate"))));
                results.add(record);
			}
		}catch (Exception e)	{			
			throw new SMRCException ("TargetMarketDAO.getDirectSalesForTM(): ", e);
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return results;
	}
        
    public static ArrayList getSalesForApprovedTM(TargetMarket targetMarket, Connection DBConn) throws Exception {
    	
        /*
         * TODO this needs to be changed so that it doesnt loop through and
         * do another query.  Cant the product be joined?
         */
    	
        int startMonth = getMonthValue(targetMarket.getStartDate());
        int endMonth = getMonthValue(targetMarket.getEndDate());
		ArrayList results = new ArrayList();
		String srYear = MiscDAO.getSRYear(DBConn);
		String srMonth = MiscDAO.getSRMonth(DBConn);
        
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(targetMarket.getStartDate());
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(targetMarket.getEndDate());
                                
		PreparedStatement pstmt = null;
		ResultSet rs  = null;

		try {
            if (targetMarket.isTAPDollars()){
                pstmt = DBConn.prepareStatement(totalSalesQueryByTMApproved);
                if (SMRCLogger.isDebuggerEnabled()) {                	
                  	SMRCLogger.debug("getSalesForApprovedTM " + totalSalesQueryByTMApproved);                	
                 }
            } else if (targetMarket.isEndMarketSales()){
                pstmt = DBConn.prepareStatement(endMarketSalesQueryByTMApproved);
                pstmt.setInt(14, targetMarket.getTargetMarketPlanId());
                if (SMRCLogger.isDebuggerEnabled()) {                	
                  	SMRCLogger.debug("getSalesForApprovedTM " + endMarketSalesQueryByTMApproved + "Argument 11 = " + targetMarket.getTargetMarketPlanId());                	
                 }
            } else {
                throw new SMRCException("Invalid dollar type for plan " + targetMarket.getTargetMarketPlanId());
            }
            pstmt.setInt(1, startCal.get(Calendar.YEAR));
            pstmt.setInt(2, startCal.get(Calendar.YEAR));
            pstmt.setInt(3, startCal.get(Calendar.YEAR));
            pstmt.setInt(4, startMonth);
            pstmt.setInt(5, endMonth);
            pstmt.setInt(6, startCal.get(Calendar.YEAR));
            pstmt.setInt(7, startMonth);
            pstmt.setInt(8, endMonth);
            pstmt.setInt(9, startCal.get(Calendar.YEAR));
            pstmt.setInt(10, startMonth);
            if ((startCal.get(Calendar.YEAR) < Globals.a2int(srYear))  || (endMonth < Globals.a2int(srMonth))){
            	pstmt.setInt(11, endMonth);
            } else {
            	pstmt.setInt(11, Globals.a2int(srMonth));
            }
            pstmt.setInt(12, startCal.get(Calendar.YEAR));
            pstmt.setInt(13, targetMarket.getTargetMarketPlanId());
            if (SMRCLogger.isDebuggerEnabled()) {                	
              	SMRCLogger.debug("Arguments : " + startCal.get(Calendar.YEAR) + ", " + startCal.get(Calendar.YEAR) + ", " 
              			+ startCal.get(Calendar.YEAR) + ", " + startMonth + ", " + endMonth + ", " + startCal.get(Calendar.YEAR)
              			+ ", " + startMonth + ", " + endMonth + ", " + startCal.get(Calendar.YEAR) + ", " + targetMarket.getTargetMarketPlanId());
             }
            rs = pstmt.executeQuery();
			while (rs.next())		
			{
                ArrayList record = new ArrayList();
                String productId = rs.getString("product_id");

                Product product = ProductDAO.productLookup(productId, Globals.a2int(srYear), DBConn);
                record.add(product);
                record.add((new Double(rs.getDouble("curr_ytd"))));
                record.add((new Double(rs.getDouble("prev_year"))));
                record.add((new Double(rs.getDouble("sales_baseline_todate"))));
                record.add((new Double(rs.getDouble("sales_in_plan"))));
                record.add((new Double(rs.getDouble("sales_baseline"))));
                results.add(record);
			}
		}catch (Exception e)	{
			throw new SMRCException("TargetMarketDAO.getDirectSalesForApprovedTM(): ", e);
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return results;
	}
        
    public static ArrayList getSalesForVCN(String vcn, Connection DBConn) throws Exception {
        
        /*
         * TODO this needs to be changed so that it doesnt loop through and
         * do another query.  Cant the product be joined?
         */
    	
    	ArrayList results = new ArrayList();

    	int srYear = Globals.a2int(MiscDAO.getSRYear(DBConn));
    	int srMonth = Globals.a2int(MiscDAO.getSRMonth(DBConn));
                
		PreparedStatement pstmt = null;
		ResultSet rs  = null;
		
		try {
			pstmt = DBConn.prepareStatement(totalSalesQueryByVcn);	
			pstmt.setInt(1,srYear);
			pstmt.setInt(2,srYear);
			pstmt.setInt(3,srYear);
			pstmt.setInt(4,srMonth);
			pstmt.setInt(5,srYear);
			pstmt.setString(6, vcn);
            
			if (SMRCLogger.isDebuggerEnabled()) {
				SMRCLogger.debug("getSalesForVCN : " + totalSalesQueryByVcn + "\nArguments: " + srYear + ", " + srYear
				+ ", " + srYear + ", " + srMonth + ", " + srYear + ", " + vcn);				
			}
			
			rs = pstmt.executeQuery();
			while (rs.next())		
			{
                ArrayList record = new ArrayList();
                String productId = rs.getString("product_id");
                
                Product product = ProductDAO.productLookup(productId, srYear, DBConn);
                record.add(product);
                record.add((new Double(rs.getDouble("curr_ytd"))));
                record.add((new Double(rs.getDouble("prev_year"))));
                record.add((new Double(rs.getDouble("prev_sales_to_date"))));
                results.add(record);
			}
		}catch (Exception e)	{
			throw new SMRCException("TargetMarketDAO.getDirectSales(): ", e);
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return results;
	}
        
    public static TargetMarket getTargetMarket(int tmId, Connection DBConn) throws Exception {
		TargetMarket targetMarket = new TargetMarket();
                                
		PreparedStatement pstmt = null;
		ResultSet rs  = null;
		
		try {
			pstmt = DBConn.prepareStatement(tmQuery);			
			pstmt.setInt(1, tmId);
            
			if (SMRCLogger.isDebuggerEnabled()) {
				SMRCLogger.debug("getTargetMarket : " + tmQuery + "\nArguments : " + tmId);				
			}
			rs = pstmt.executeQuery();
			while (rs.next()){
	            targetMarket.setTargetMarketPlanId(rs.getInt("target_market_plan_id"));
	        	targetMarket.setPlanDescription(StringManipulation.noNull((rs.getString("plan_description"))));
	            targetMarket.setContactId(rs.getInt("contact_Id"));
	            targetMarket.setCompetitorConvert(StringManipulation.noNull((rs.getString("competitor_convert"))));
	            targetMarket.setCompetitorId(rs.getInt("competitor_Id"));
	            targetMarket.setCommitmentLevel(rs.getInt("commitment_level"));
	            targetMarket.setBusinessObjective(StringManipulation.noNull((rs.getString("business_objective"))));
	            targetMarket.setStartDate(rs.getDate("start_date"));
	            targetMarket.setEndDate(rs.getDate("end_Date"));
	            targetMarket.setSalesObjective(rs.getInt("sales_objective"));
	            targetMarket.setIncrementalGrowth(rs.getInt("incremental_growth"));
	            targetMarket.setSalesTrackingTypeId(rs.getInt("sales_tracking_type_id"));
	            targetMarket.setSalesTrackingCodeType(MiscDAO.getCodeById(rs.getInt("sales_tracking_type_id"), DBConn));
	            targetMarket.setBaselineStartDate(rs.getDate("baseline_Start_Date"));
	            targetMarket.setBaselineEndDate(rs.getDate("baseline_End_Date"));
	            targetMarket.setSalesObjectiveOtherNotes(StringManipulation.noNull((rs.getString("sales_Objective_Other_Notes"))));
	            targetMarket.setSalesPlanUsed(StringManipulation.noNull((rs.getString("sales_Plans_Used"))));
	            targetMarket.setPercentGrowth1(rs.getDouble("percent_Growth1"));
	            targetMarket.setPercentPayout1(rs.getDouble("percent_Payout1"));
	            targetMarket.setPercentGrowth2(rs.getDouble("percent_Growth2"));
	            targetMarket.setPercentPayout2(rs.getDouble("percent_Payout2"));
	            targetMarket.setPercentGrowth3(rs.getDouble("percent_Growth3"));
	            targetMarket.setPercentPayout3(rs.getDouble("percent_Payout3"));
	            targetMarket.setPercentGrowth4(rs.getDouble("percent_Growth4"));
	            targetMarket.setPercentPayout4(rs.getDouble("percent_Payout4"));
	            //targetMarket.setIncludeBidmanRebate(StringManipulation.noNull((rs.getString("include_Bidman_Rebate"))));
	            targetMarket.setUserAdded(StringManipulation.noNull((rs.getString("user_Added")))); 
	            targetMarket.setUserChanged(StringManipulation.noNull((rs.getString("user_Changed"))));
	            targetMarket.setDateAdded(rs.getDate("date_Added"));
	            targetMarket.setDateChanged(rs.getDate("date_Changed"));
	            targetMarket.setMaximumPayout(rs.getDouble("maximum_Payout"));
	            targetMarket.setSegmentOtherNotes(StringManipulation.noNull((rs.getString("segment_other_notes"))));
	            targetMarket.setEndCustomers(getTMEndCustomers(tmId, DBConn));
	            targetMarket.setPlanAccounts(getTMOtherAccounts(tmId, DBConn));
	            targetMarket.setPlanContacts(getTMContacts(tmId, DBConn));
	            targetMarket.setPlanSegments(getTMSegments(tmId, DBConn));
	            targetMarket.setPlanProducts(getTargetMarketProducts(tmId, DBConn));
	            int codeTypeId = rs.getInt("target_market_plan_status_id");
	            targetMarket.setStatus(StringManipulation.noNull(MiscDAO.getCodeById(codeTypeId, DBConn).getValue()));
                targetMarket.setPendingResubmission(WorkflowDAO.targetMarketIsPendingResubmission(tmId,DBConn));
                convertOldSegmentOptionsToOther(targetMarket,DBConn);
			}
		}catch (Exception e)	{
			throw new SMRCException("TargetMarketDAO.getTargetMarket(): ", e);
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return targetMarket;
	}
        
    private static void convertOldSegmentOptionsToOther(TargetMarket targetMarket, Connection DBConn) throws Exception {
    	ArrayList planSegments = targetMarket.getPlanSegments();
    	StringBuffer oldSegmentString = new StringBuffer();
    	for (int i=0; i < planSegments.size(); i++){
    		int segmentId = ((Integer) planSegments.get(i)).intValue();
    		Segment segment = SegmentsDAO.getSegment(segmentId,DBConn);
    		if (!segment.isTargetMarketSegment()){
    			oldSegmentString.append(segment.getName() + "; ");
    		}
    	}
    	oldSegmentString.append(targetMarket.getSegmentOtherNotes());
    	targetMarket.setSegmentOtherNotes(oldSegmentString.toString());
    	
    }
    
    
    public static int getTargetMarketPlanStatusId(String targetMarketPlanId, Connection DBConn) throws Exception {
		
        int targetMarketPlanStatusId = 0;                        
		PreparedStatement pstmt = null;
		ResultSet rs  = null;
		
		try {
			pstmt = DBConn.prepareStatement(getTargetMarketPlanStatusId);			
			pstmt.setString(1, targetMarketPlanId);
            
			if (SMRCLogger.isDebuggerEnabled()) {
				SMRCLogger.debug("getTargetMarketPlanStatusId : " + getTargetMarketPlanStatusId + "\nArguments : " + targetMarketPlanId);				
			}
			rs = pstmt.executeQuery();
			while (rs.next()){
				targetMarketPlanStatusId = rs.getInt("target_market_plan_status_id");          
			}
		}catch (Exception e)	{
			throw new SMRCException("TargetMarketDAO.getTargetMarketPlanStatusId(): ", e);
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return targetMarketPlanStatusId;
	}    
        
        // Returns ArrayList of TargetMarketReportBeans 
    public static ArrayList getAccountTMIds(String vcn, Connection DBConn) throws Exception {
        ArrayList results = new ArrayList();
        PreparedStatement pstmt = null;
        ResultSet rs  = null;

        try {
                pstmt = DBConn.prepareStatement(accountTMIdQuery);			
                pstmt.setString(1, vcn);
                
                if (SMRCLogger.isDebuggerEnabled()) {
                	SMRCLogger.debug("getAccountTMIds : " + accountTMIdQuery + "\nArguments : " + vcn);                	
                }
                rs = pstmt.executeQuery();
                while (rs.next()){
                	TargetMarketReportBean bean = new TargetMarketReportBean();
                    bean.setDescription(rs.getString("plan_description"));
                    bean.setId(rs.getString("target_market_plan_id"));
                    bean.setStatus(rs.getString("code_description"));
                    if (WorkflowDAO.targetMarketIsPendingResubmission(Globals.a2int(bean.getId()),DBConn)){
                    	bean.setStatus("Pending Resubmission");
                    } else if (bean.getStatus().equalsIgnoreCase("Active")){
                    	java.util.Date today = new java.util.Date();
                    	java.util.Date tmpEndDate = rs.getDate("end_date");
                    	if (today.after(tmpEndDate)){
                    		bean.setStatus("Complete");
                    	}
                    	
                    }
                    results.add(bean);
                }
        }catch (Exception e)	{
        	throw new SMRCException("TargetMarketDAO.getAccountTMIds(): ", e);
         }
        finally {
        	SMRCConnectionPoolUtils.close(rs);
        	SMRCConnectionPoolUtils.close(pstmt);
        }

        return results;
	}
    
       //  Returns an ArrayList of end customer ids for a specified target market
    public static ArrayList getTMEndCustomers(int tmId, Connection DBConn) throws Exception {
       
        ArrayList results = new ArrayList();
        PreparedStatement pstmt = null;
        ResultSet rs  = null;

        try {
                pstmt = DBConn.prepareStatement(endCustomerQuery);			
                pstmt.setInt(1, tmId);
                if (SMRCLogger.isDebuggerEnabled()) {
                	SMRCLogger.debug("getTMEndCustomers : " + endCustomerQuery + "\nArguments : " + tmId);
                }
                rs = pstmt.executeQuery();
                while (rs.next())		
                {
                        String vcn = rs.getString("vista_customer_number");
                        results.add(vcn);
                }
        }catch (Exception e)	{
        	throw new SMRCException("TargetMarketDAO.getTMEndCustomers(): ", e);
         }
        finally {
        	SMRCConnectionPoolUtils.close(rs);
        	SMRCConnectionPoolUtils.close(pstmt);
        }

        return results;
	}
       
   //  Returns an ArrayList of other accout ids involved in a specified target market 
   public static ArrayList getTMOtherAccounts(int tmId, Connection DBConn) throws Exception {
    
	    ArrayList results = new ArrayList();
        PreparedStatement pstmt = null;
        ResultSet rs  = null;

        try {
                pstmt = DBConn.prepareStatement(otherAccountsQuery);			
                pstmt.setInt(1, tmId);
                if (SMRCLogger.isDebuggerEnabled()) {
                	SMRCLogger.debug("getTMOtherAccounts : " + otherAccountsQuery + "\nArguments : " + tmId);
                }
                rs = pstmt.executeQuery();
                while (rs.next())		
                {
                        String vcn = rs.getString("vista_customer_number");
                        results.add(vcn);
                }
        }catch (Exception e)	{
        	throw new SMRCException("TargetMarketDAO.getTMOtherAccounts(): ", e);
        }
        finally {
        	SMRCConnectionPoolUtils.close(rs);
        	SMRCConnectionPoolUtils.close(pstmt);
        }

        return results;
	}
    
    //  Returns an ArrayList of the contacts for a specified target market in Contact Objects
    public static ArrayList getTMContacts(int tmId, Connection DBConn) throws Exception {
        /*
         * TODO this needs to be changed so that it doesnt loop through and
         * get a contact record for each record set.  It should be 1 query.
         */
    	ArrayList results = new ArrayList();
        PreparedStatement pstmt = null;
        ResultSet rs  = null;

        try {
                pstmt = DBConn.prepareStatement(contactsQuery);			
                pstmt.setInt(1, tmId);
                if (SMRCLogger.isDebuggerEnabled()) {
                	SMRCLogger.debug("getTMContacts : " + contactsQuery + "\nArguments : " + tmId);
                }
               rs = pstmt.executeQuery();
                while (rs.next()){
                    String contactId = rs.getString("contact_id");
                    Contact contact = ContactsDAO.getContact(contactId, DBConn);
                    results.add(contact);
                }
        }catch (Exception e)	{
        	throw new SMRCException("TargetMarketDAO.getTMContacts(): ", e);
         }
        finally {
        	SMRCConnectionPoolUtils.close(rs);
        	SMRCConnectionPoolUtils.close(pstmt);
        }

        return results;
	}
       
      //  Returns an ArrayList of the contacts for all of the accounts in a specified target market in Contact Objects
   public static ArrayList getAllContactsForAllTMAccounts(int tmId, Connection DBConn) throws Exception {
    /*
     * TODO this needs to be changed so that it doesnt loop through and
     * get a contact record for each record set.  It should be 1 query.
     */    
   		ArrayList results = new ArrayList();
	    PreparedStatement pstmt = null;
	    ResultSet rs  = null;
	
	    try {
	            pstmt = DBConn.prepareStatement(allContactsForAllTMAccountsQuery);			
	            pstmt.setInt(1, tmId);
                if (SMRCLogger.isDebuggerEnabled()) {
                	SMRCLogger.debug("getAllContactsForAllTMAccounts : " + allContactsForAllTMAccountsQuery + "\nArguments : " + tmId);
                }
	            rs = pstmt.executeQuery();
	            while (rs.next())		
	            {
                    String contactId = rs.getString("contact_id");
                    Contact contact = ContactsDAO.getContact(contactId, DBConn);
                    results.add(contact);
	            }
	    }catch (Exception e)	{
	    	throw new SMRCException("TargetMarketDAO.getAllContactsForAllTMAccounts(): ", e);
	    }
	    finally {
	    	SMRCConnectionPoolUtils.close(rs);
	    	SMRCConnectionPoolUtils.close(pstmt);
	    }
	
	    return results;
	}
       
       
       //  Returns an ArrayList of ArrayLists of sales objectives for target market plans
    public static ArrayList getSalesObjectives(Connection DBConn) throws Exception {
        ArrayList results = new ArrayList();
        PreparedStatement pstmt = null;
        ResultSet rs  = null;

        try {
                pstmt = DBConn.prepareStatement(salesObjQuery);			
                if (SMRCLogger.isDebuggerEnabled()) {
                	SMRCLogger.debug("getSalesObjectives : " + salesObjQuery);
                }
                rs = pstmt.executeQuery();
                while (rs.next())		
                {
                    ArrayList salesObj = new ArrayList();
                    salesObj.add(new Integer(rs.getInt("code_type_id")));
                    salesObj.add(rs.getString("code_description"));
                    results.add(salesObj);
                }
        }catch (Exception e)	{
        	throw new SMRCException("TargetMarketDAO.getTMContacts(): ", e);
        }
        finally {
        	SMRCConnectionPoolUtils.close(rs);
        	SMRCConnectionPoolUtils.close(pstmt);
        }

        return results;
	}
    
   
       //  Returns an ArrayList of products in the specified target market in TargetMarketProduct objects
    public static ArrayList getTargetMarketProducts(int tmId, Connection DBConn) throws Exception {
        ArrayList results = new ArrayList();
        PreparedStatement pstmt = null;
        ResultSet rs  = null;

        try {
                pstmt = DBConn.prepareStatement(tmProductQuery);
                pstmt.setInt(1,tmId);
                if (SMRCLogger.isDebuggerEnabled()) {
                	SMRCLogger.debug("getTargetMarketProducts : " + tmProductQuery + "\nArguments : " + tmId);                	
                }
                rs = pstmt.executeQuery();
                while (rs.next())		
                {
                    TargetMarketProduct tmProduct = new TargetMarketProduct();
                    tmProduct.setTargetMarketPlanId(rs.getInt("target_market_plan_id"));
                    tmProduct.setProductId(rs.getString("product_id"));
                    tmProduct.setPeriodYYYY(rs.getInt("period_YYYY"));
                    tmProduct.setProduct(ProductDAO.productLookup(rs.getString("product_id"), rs.getInt("period_YYYY"), DBConn));
                    tmProduct.setIncrementalGrowth(rs.getInt("incremental_growth"));
                    tmProduct.setSalesObjective(rs.getInt("sales_objective"));
                    results.add(tmProduct);
                }
        }catch (Exception e)	{
        	throw new SMRCException("TargetMarketDAO.getTargetMarketProducts(): ", e);
        }
        finally {
        	SMRCConnectionPoolUtils.close(rs);
        	SMRCConnectionPoolUtils.close(pstmt);
        }

        return results;
	}
    
        //  Returns an ArrayList of segment ids in the specified target market
   public static ArrayList getTMSegments(int tmId, Connection DBConn) throws Exception {
        ArrayList results = new ArrayList();
        PreparedStatement pstmt = null;
        ResultSet rs  = null;

        try {
                pstmt = DBConn.prepareStatement(tmSegmentQuery);
                pstmt.setInt(1,tmId);
                if (SMRCLogger.isDebuggerEnabled()) {
                	SMRCLogger.debug("getTMSegments : " + tmSegmentQuery + "\nArguments : " + tmId);                	
                }
                rs = pstmt.executeQuery();
                while (rs.next())		
                {
                    results.add(new Integer(rs.getInt("segment_id")));
                }
        }catch (Exception e)	{
        	throw new SMRCException("TargetMarketDAO.getTMSegments(): ", e);
        }
        finally {
        	SMRCConnectionPoolUtils.close(rs);
        	SMRCConnectionPoolUtils.close(pstmt);
        }

        return results;
	}
    
        //  Returns an ArrayList of ArrayLists of sales tracking dollar options for target market plans
   public static ArrayList getSalesTrackingDollars(Connection DBConn) throws Exception {
        ArrayList results = new ArrayList();
        PreparedStatement pstmt = null;
        ResultSet rs  = null;

        try {
                pstmt = DBConn.prepareStatement(salesTrackingDollarsQuery);			
                rs = pstmt.executeQuery();
                if (SMRCLogger.isDebuggerEnabled()) {
                	SMRCLogger.debug("getSalesTrackingDollars : " + salesTrackingDollarsQuery);                	
                }
                while (rs.next())		
                {
                    ArrayList salesDollars = new ArrayList();
                    salesDollars.add(new Integer(rs.getInt("code_type_id")));
                    salesDollars.add(rs.getString("code_description"));
                    results.add(salesDollars);
                }
        }catch (Exception e)	{
        	throw new SMRCException("TargetMarketDAO.getSalesTrackingDollars(): ", e);
        }
        finally {
        	SMRCConnectionPoolUtils.close(rs);
        	SMRCConnectionPoolUtils.close(pstmt);
        }

        return results;
	}
       
       //** Creates new Target Market plan in target_market_plans, returns the new target market plan id
   public static int insertNewTargetMarketPlan(TargetMarket targetMarket, Connection DBConn, String userid) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs  = null;
        int tmId = 0;

        try {
        //  Finding nextVal for target_market_plan_seq
            pstmt = DBConn.prepareStatement(targetMarketPlansNextvalQuery);
            rs = pstmt.executeQuery();
			rs.next();
			tmId = (int) rs.getLong(1);
	       //  Inserting new record
			pstmt = DBConn.prepareStatement(targetMarketInsert);
            pstmt.setInt(1, tmId);
            pstmt.setString(2, targetMarket.getPlanDescription());
            String contactId = null;
            if (targetMarket.getContactId() == 0){
                pstmt.setNull(3, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(3, targetMarket.getContactId());
                contactId = Integer.toString(targetMarket.getContactId());
            }
            pstmt.setString(4, targetMarket.getCompetitorConvert());
            String compId = null;
            if (targetMarket.getCompetitorId() == 0){
                pstmt.setNull(5, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(5, targetMarket.getCompetitorId());
                compId =  Integer.toString(targetMarket.getCompetitorId());
            }
            pstmt.setInt(6, targetMarket.getCommitmentLevel());
            pstmt.setString(7, targetMarket.getBusinessObjective());
            pstmt.setDate(8,  new java.sql.Date(targetMarket.getStartDate().getTime()));
            pstmt.setDate(9, new java.sql.Date(targetMarket.getEndDate().getTime()));
            pstmt.setInt(10, targetMarket.getSalesObjective());
            pstmt.setInt(11, targetMarket.getIncrementalGrowth());
            pstmt.setInt(12, targetMarket.getSalesTrackingTypeId());
            pstmt.setDate(13, new java.sql.Date(targetMarket.getStartDate().getTime()));
            pstmt.setDate(14, new java.sql.Date(targetMarket.getEndDate().getTime()));
            pstmt.setString(15, targetMarket.getSalesObjectiveOtherNotes());
            pstmt.setString(16, targetMarket.getSalesPlanUsed());
            pstmt.setDouble(17, targetMarket.getPercentGrowth1());
            pstmt.setDouble(18, targetMarket.getPercentPayout1());
            pstmt.setDouble(19, targetMarket.getPercentGrowth2());
            pstmt.setDouble(20, targetMarket.getPercentPayout2());
            pstmt.setDouble(21, targetMarket.getPercentGrowth3());
            pstmt.setDouble(22, targetMarket.getPercentPayout3());
            pstmt.setDouble(23, targetMarket.getPercentGrowth4());
            pstmt.setDouble(24, targetMarket.getPercentPayout4());
            // Bidman may be a part of phase 2
//                        pstmt.setString(25, targetMarket.getIncludeBidmanRebate());
            pstmt.setString(25, "N");
            pstmt.setString(26, userid);
            pstmt.setString(27, userid);
            pstmt.setDate(28, new java.sql.Date(new java.util.Date().getTime()));
            pstmt.setDate(29, new java.sql.Date(new java.util.Date().getTime()));
            pstmt.setDouble(30, targetMarket.getMaximumPayout());
            ArrayList codeTypes = MiscDAO.getCodes("target_market_status", DBConn);
            int savedValue = 0;
            for (int i=0; i< codeTypes.size(); i++){
                CodeType codetype = (CodeType) codeTypes.get(i);
                if (codetype.getValue().equalsIgnoreCase(targetMarket.getStatus())){
                    savedValue = codetype.getId();
                }
            }
            pstmt.setInt(31, savedValue);
            pstmt.setString(32, targetMarket.getSegmentOtherNotes());
            if (SMRCLogger.isDebuggerEnabled()) {
            	SMRCLogger.debug("insertNewTargetMarketPlan : " + targetMarketInsert 
            			+ "\nArguments : " + tmId + ", " + targetMarket.getPlanDescription() + ", " + contactId
            			 + ", " + targetMarket.getCompetitorConvert() + ", " + compId + ", " + targetMarket.getCommitmentLevel()
            			 + ", " + targetMarket.getBusinessObjective() + ", A Date, A Date, " + targetMarket.getSalesObjective()
            			 + ", " + targetMarket.getIncrementalGrowth() + ", " + targetMarket.getSalesTrackingTypeId() + ", " 
            			 + "A Date, A Date, " + targetMarket.getSalesObjectiveOtherNotes() + ", " + targetMarket.getSalesPlanUsed() + "....");                	
            }
            pstmt.executeUpdate();
            
            insertTargetMarketSegment(tmId, targetMarket.getPlanSegments(), DBConn, userid);
            insertTargetMarketContacts(tmId, targetMarket.getPlanContacts(), DBConn, userid);
            insertTargetMarketAccounts(tmId, targetMarket.getPlanAccounts(), DBConn, userid);
            insertTargetMarketEndCustomers(tmId, targetMarket.getEndCustomers(), DBConn, userid);
            insertTargetMarketProducts(tmId, targetMarket.getPlanProducts(), DBConn);
            
            }catch (Exception e)	{
            	throw new SMRCException("TargetMarketDAO.insertNewTargetMarketPlan(): ", e);
             }
            finally {
            	SMRCConnectionPoolUtils.close(rs);
            	SMRCConnectionPoolUtils.close(pstmt);
            }

            
            return tmId;
	}
       
              
        //** Creates new Target Market Segments for the specified target marke plan
   public static void insertTargetMarketSegment(int tmId, ArrayList segmentList, Connection DBConn, String userid) throws Exception {
	    PreparedStatement pstmt = null;
	    for (int i=0; i< segmentList.size(); i++){
	        int segmentId = ((Integer) segmentList.get(i)).intValue();
	        
	        try {
	         //  Inserting new record
	            pstmt = DBConn.prepareStatement(tmSegmentInsert);
	            pstmt.setInt(1, tmId);
	            pstmt.setInt(2, segmentId);
	            pstmt.setString(3, userid);
	            pstmt.setDate(4, new java.sql.Date(new java.util.Date().getTime()));
	                             
	            if (SMRCLogger.isDebuggerEnabled()) {
	            	SMRCLogger.debug("insertTargetMarketSegment" + tmSegmentInsert + "\nArguments : " + tmId + ", " + segmentId + ", "
	            			+ userid + ", A Date");       	
	            }
	            pstmt.executeUpdate();
	
	        }catch (Exception e)	{
	        	throw new SMRCException("TargetMarketDAO.insertTargetMarketSegment(): ", e);
	        }
	        finally {
	        	SMRCConnectionPoolUtils.close(pstmt);
	        }
	    }

    }
       
        //** Deletes Target Market Segments for the specified target marke plan
   public static void deleteTargetMarketSegment(int tmId, ArrayList segmentList, Connection DBConn) throws Exception {
        PreparedStatement pstmt = null;
        for (int i=0; i< segmentList.size(); i++){
            int segmentId = ((Integer) segmentList.get(i)).intValue();
            
            try {
             //  Inserting new record
                pstmt = DBConn.prepareStatement(tmSegmentDelete);
                pstmt.setInt(1, tmId);
                pstmt.setInt(2, segmentId);
                                
	            if (SMRCLogger.isDebuggerEnabled()) {
	            	SMRCLogger.debug("deleteTargetMarketSegment" + tmSegmentDelete + "\nArguments : " + tmId + ", " + segmentId);       	
	            }
	            
                pstmt.executeUpdate();

            }catch (Exception e)	{
            	throw new SMRCException("TargetMarketDAO.deleteTargetMarketSegment(): ", e);
            }
            finally {
            	SMRCConnectionPoolUtils.close(pstmt);
            }
        }

    }
       
        //** Creates new contacts for the specified target market plan
   public static void insertTargetMarketContacts(int tmId,  ArrayList contactList, Connection DBConn, String userid) throws Exception {
            PreparedStatement pstmt = null;
            for (int i=0; i< contactList.size(); i++){
                Contact contact = (Contact) contactList.get(i);
                int contactId = contact.getId();
                String vcn = contact.getCustomer();
            
                try {
                     //  Inserting new record
                        pstmt = DBConn.prepareStatement(tmContactInsert);
                        pstmt.setInt(1, tmId);
                        pstmt.setInt(2, contactId);
                        pstmt.setString(3, vcn);
                        pstmt.setString(4, userid);
                        pstmt.setDate(5, new java.sql.Date(new java.util.Date().getTime()));

        	            if (SMRCLogger.isDebuggerEnabled()) {
        	            	SMRCLogger.debug("insertTargetMarketContacts" + tmContactInsert + "\nArguments : " + tmId + ", " + contactId
        	            			+ ", " + vcn  
        	            			+ ", " + userid  
        	            			+ ", A Date");       	
        	            }
                        pstmt.executeUpdate();

                }catch (Exception e)	{
                	throw new SMRCException("TargetMarketDAO.insertTargetMarketContacts(): ", e);
                }
                finally {
                	SMRCConnectionPoolUtils.close(pstmt);
                }
            }

    }
   
    //** Deletes contacts for the specified target market plan
   public static void deleteTargetMarketContacts(int tmId,  ArrayList contactList, Connection DBConn) throws Exception {
            PreparedStatement pstmt = null;
            for (int i=0; i< contactList.size(); i++){
                Contact contact = (Contact) contactList.get(i);
                int contactId = contact.getId();
            
                try {
                        pstmt = DBConn.prepareStatement(tmContactDelete);
                        pstmt.setInt(1, tmId);
                        pstmt.setInt(2, contactId);
                        
        	            if (SMRCLogger.isDebuggerEnabled()) {
        	            	SMRCLogger.debug("deleteTargetMarketContacts" + tmContactDelete 
        	            			+ "\nArguments : " 
        	            			+ ", " + tmId  
        	            			+ ", " + contactId);       	
        	            }
                        pstmt.executeUpdate();

                }catch (Exception e)	{
                	throw new SMRCException("TargetMarketDAO.deleteTargetMarketContacts(): ", e);
                }
                finally {
                	SMRCConnectionPoolUtils.close(pstmt);
                }
            }

    }
   
    //** Creates new accounts for the specified target market plan on target_market_plan_accts
   public static void insertTargetMarketAccounts(int tmId, ArrayList accountList, Connection DBConn, String userid) throws Exception {
            PreparedStatement pstmt = null;
            for (int i=0; i< accountList.size(); i++){
            	String vcn = (String) accountList.get(i);
                
                try {
                     //  Inserting new record
                        pstmt = DBConn.prepareStatement(tmPlanAccountsInsert);
                        pstmt.setInt(1, tmId);
                        pstmt.setString(2, vcn);
                        pstmt.setString(3, userid);
                        pstmt.setDate(4, new java.sql.Date(new java.util.Date().getTime()));

        	            if (SMRCLogger.isDebuggerEnabled()) {
        	            	SMRCLogger.debug("insertTargetMarketAccounts" + tmPlanAccountsInsert 
        	            			+ "\nArguments : " 
        	            			+ ", " + tmId  
           	            			+ ", " + vcn  
           	            			+ ", " + userid  
        	            			+ ", A Date");       	
        	            }
                        pstmt.executeUpdate();

                }catch (Exception e)	{
                	throw new SMRCException("TargetMarketDAO.insertTargetMarketAccounts(): ", e);
                 }
                finally {
                	SMRCConnectionPoolUtils.close(pstmt);
                }
            }

    }
   
   //** Deletes accounts for the specified target market plan on target_market_plan_accts
   public static void deleteTargetMarketAccounts(int tmId, ArrayList accountList, Connection DBConn) throws Exception {
            PreparedStatement pstmt = null;
            for (int i=0; i< accountList.size(); i++){
                String vcn = (String) accountList.get(i);
                
                try {
                     
                        pstmt = DBConn.prepareStatement(tmPlanAccountsDelete);
                        pstmt.setInt(1, tmId);
                        pstmt.setString(2, vcn);
                        
        	            if (SMRCLogger.isDebuggerEnabled()) {
        	            	SMRCLogger.debug("deleteTargetMarketAccounts" + tmPlanAccountsDelete 
        	            			+ "\nArguments : " 
        	            			+ ", " + tmId  
           	            			+ ", " + vcn);       	
        	            }
                       pstmt.executeUpdate();

                }catch (Exception e)	{
                	throw new SMRCException("TargetMarketDAO.deleteTargetMarketAccounts(): ", e);
                }
                finally {
                	SMRCConnectionPoolUtils.close(pstmt);
                }
            }

    }
   
    //** Creates new accounts for the specified target market plan on target_market_plan_accts
   public static void insertTargetMarketEndCustomers(int tmId, ArrayList accountList, Connection DBConn, String userid) throws Exception {
            PreparedStatement pstmt = null;
            for (int i=0; i< accountList.size(); i++){
                String vcn = (String) accountList.get(i);
                try {
                     //  Inserting new record
                        pstmt = DBConn.prepareStatement(tmEndCustomersInsert);
                        pstmt.setInt(1, tmId);
                        pstmt.setString(2, vcn);
                        pstmt.setString(3, userid);
                        pstmt.setDate(4, new java.sql.Date(new java.util.Date().getTime()));

        	            if (SMRCLogger.isDebuggerEnabled()) {
        	            	SMRCLogger.debug("insertTargetMarketEndCustomers" + tmEndCustomersInsert 
        	            			+ "\nArguments : " 
        	            			+ ", " + tmId  
        	            			+ ", " + vcn  
        	            			+ ", " + userid  
          	            			+ ", A Date");       	
        	            }
        	            
                       pstmt.executeUpdate();

                }catch (Exception e)	{
                	throw new SMRCException("TargetMarketDAO.insertTargetMarketEndCustomers(): ", e);
                }
                finally {
                	SMRCConnectionPoolUtils.close(pstmt);
                }
            }

    }
   
   //** Deletes accounts for the specified target market plan on target_market_plan_accts
   public static void deleteTargetMarketEndCustomers(int tmId, ArrayList accountList, Connection DBConn) throws Exception {
            PreparedStatement pstmt = null;
            for (int i=0; i< accountList.size(); i++){
            	String vcn = (String) accountList.get(i);
               
                try {
                     
                        pstmt = DBConn.prepareStatement(tmEndCustomersDelete);
                        pstmt.setInt(1, tmId);
                        pstmt.setString(2, vcn);
                        
        	            if (SMRCLogger.isDebuggerEnabled()) {
        	            	SMRCLogger.debug("deleteTargetMarketEndCustomers" + tmEndCustomersDelete 
        	            			+ "\nArguments : " 
        	            			+ ", " + tmId  
        	            			+ ", " + vcn);       	
        	            }
                        pstmt.executeUpdate();

                }catch (Exception e)	{
                	throw new SMRCException("TargetMarketDAO.deleteTargetMarketEndCustomers(): ", e);
                }
                finally {
                	SMRCConnectionPoolUtils.close(pstmt);
                }
            }

    }
   
    //** Creates new products for the specified target market plan on target_market_plan_products
   public static void insertTargetMarketProducts(int tmId, ArrayList productList, Connection DBConn) throws Exception {
            PreparedStatement pstmt = null;
            for (int i=0; i< productList.size(); i++){
                TargetMarketProduct tmProduct = (TargetMarketProduct) productList.get(i);
                String productId = tmProduct.getProduct().getId();
                int periodYYYY = tmProduct.getProduct().getPeriodYYYY();
                double salesObjective = tmProduct.getSalesObjective();
                
                try {
                     //  Inserting new record
                        pstmt = DBConn.prepareStatement(tmPlanProductsInsert);
                        pstmt.setInt(1, tmId);
                        pstmt.setString(2, productId);
                        pstmt.setInt(3, periodYYYY);
                        pstmt.setDouble(4, salesObjective);
                     //  Incremental Growth is calculated each time, don't need to save
                        pstmt.setDouble(5, 0);

        	            if (SMRCLogger.isDebuggerEnabled()) {
        	            	SMRCLogger.debug("deleteTargetMarketEndCustomers" + tmEndCustomersDelete 
        	            			+ "\nArguments : " 
        	            			+ ", " + tmId  
        	            			+ ", " + productId  
        	            			+ ", " + periodYYYY  
        	            			+ ", " + salesObjective  
        	            			+ ", 0");       	
        	            }
        	            
                        pstmt.executeUpdate();

                }catch (Exception e)	{
                	throw new SMRCException("TargetMarketDAO.insertTargetMarketProducts(): ", e);
                }
                finally {
                	SMRCConnectionPoolUtils.close(pstmt);
                }
            }

    }
   
     //** Updates products for the specified target market plan on target_market_plan_products
   public static void updateTargetMarketProducts(int tmId, ArrayList productList, Connection DBConn) throws Exception {
            PreparedStatement pstmt = null;
            for (int i=0; i< productList.size(); i++){
                TargetMarketProduct tmProduct = (TargetMarketProduct) productList.get(i);
                String productId = tmProduct.getProduct().getId();
                int periodYYYY = tmProduct.getProduct().getPeriodYYYY();
                double salesObjective = tmProduct.getSalesObjective();
                
                try {
                     //  Inserting new record
                        pstmt = DBConn.prepareStatement(tmPlanProductsUpdate);
                        pstmt.setInt(1, periodYYYY);
                        pstmt.setDouble(2, salesObjective);
                        pstmt.setInt(3, tmId);
                        pstmt.setString(4, productId);
                     
        	            if (SMRCLogger.isDebuggerEnabled()) {
        	            	SMRCLogger.debug("updateTargetMarketProducts" + tmPlanProductsUpdate 
        	            			+ "\nArguments : " 
        	            			+ ", " + periodYYYY  
        	            			+ ", " + salesObjective  
        	            			+ ", " + tmId  
        	            			+ ", " + productId);       	
        	            }
                        pstmt.executeUpdate();

                }catch (Exception e)	{
                	throw new SMRCException("TargetMarketDAO.updateTargetMarketProducts(): ", e);
                }
                finally {
                	SMRCConnectionPoolUtils.close(pstmt);
                }
            }

    }
   
   
   
   //** Deletes products for the specified target market plan on target_market_plan_products
   public static void deleteTargetMarketProducts(int tmId, ArrayList productList, Connection DBConn) throws Exception {
            PreparedStatement pstmt = null;
            for (int i=0; i< productList.size(); i++){
                TargetMarketProduct tmProduct = (TargetMarketProduct) productList.get(i);
                String productId = tmProduct.getProduct().getId();
                
                try {
                     //  Inserting new record
                        pstmt = DBConn.prepareStatement(tmPlanProductsDelete);
                        pstmt.setInt(1, tmId);
                        pstmt.setString(2, productId);
                        
        	            if (SMRCLogger.isDebuggerEnabled()) {
        	            	SMRCLogger.debug("deleteTargetMarketProducts" + tmPlanProductsDelete 
        	            			+ "\nArguments : " 
        	            			+ ", " + tmId  
        	            			+ ", " + productId);       	
        	            }
                        pstmt.executeUpdate();

                }catch (Exception e)	{
                	throw new SMRCException("TargetMarketDAO.deleteTargetMarketProducts(): ", e);
                }
                finally {
                	SMRCConnectionPoolUtils.close(pstmt);
                }
            }

    }

   //** Update existing Target Market plan in target_market_plans
   public static void updateTargetMarketPlan(TargetMarket targetMarket, Connection DBConn, String userid) throws Exception {
            PreparedStatement pstmt = null;
            ResultSet rs  = null;
            
            try {
                    pstmt = DBConn.prepareStatement(targetMarketUpdate);
                    pstmt.setString(1, targetMarket.getPlanDescription());
                    if (targetMarket.getContactId() == 0){
                        pstmt.setNull(2, java.sql.Types.INTEGER);
                    } else {
                        pstmt.setInt(2, targetMarket.getContactId());
                    }
                    pstmt.setString(3, targetMarket.getCompetitorConvert());
                    if (targetMarket.getCompetitorId() == 0){
                        pstmt.setNull(4, java.sql.Types.INTEGER);
                    } else {
                        pstmt.setInt(4, targetMarket.getCompetitorId());
                    }
                    pstmt.setInt(5, targetMarket.getCommitmentLevel());
                    pstmt.setString(6, targetMarket.getBusinessObjective());
                    pstmt.setDate(7,  new java.sql.Date(targetMarket.getStartDate().getTime()));
                    pstmt.setDate(8, new java.sql.Date(targetMarket.getEndDate().getTime()));
                    pstmt.setInt(9, targetMarket.getSalesObjective());
                    pstmt.setInt(10, targetMarket.getIncrementalGrowth());
                    pstmt.setInt(11, targetMarket.getSalesTrackingTypeId());
                    pstmt.setDate(12, new java.sql.Date(targetMarket.getStartDate().getTime()));
                    pstmt.setDate(13, new java.sql.Date(targetMarket.getEndDate().getTime()));
                    pstmt.setString(14, targetMarket.getSalesObjectiveOtherNotes());
                    pstmt.setString(15, targetMarket.getSalesPlanUsed());
                    pstmt.setDouble(16, targetMarket.getPercentGrowth1());
                    pstmt.setDouble(17, targetMarket.getPercentPayout1());
                    pstmt.setDouble(18, targetMarket.getPercentGrowth2());
                    pstmt.setDouble(19, targetMarket.getPercentPayout2());
                    pstmt.setDouble(20, targetMarket.getPercentGrowth3());
                    pstmt.setDouble(21, targetMarket.getPercentPayout3());
                    pstmt.setDouble(22, targetMarket.getPercentGrowth4());
                    pstmt.setDouble(23, targetMarket.getPercentPayout4());
                  	// keep for phase 2
                    //pstmt.setString(24, targetMarket.getIncludeBidmanRebate());
                    //pstmt.setString(25, "E0063829");
                    pstmt.setString(24, userid);
                    //pstmt.setDate(27, new java.sql.Date(new java.util.Date().getTime()));
                    pstmt.setDate(25, new java.sql.Date(new java.util.Date().getTime()));
                    pstmt.setDouble(26, targetMarket.getMaximumPayout());
                    pstmt.setString(27, targetMarket.getSegmentOtherNotes());
                    ArrayList codeTypes = MiscDAO.getCodes("target_market_status", DBConn);
                    int savedValue = 0;
                    for (int i=0; i< codeTypes.size(); i++){
                        CodeType codetype = (CodeType) codeTypes.get(i);
                        if (codetype.getValue().equalsIgnoreCase(targetMarket.getStatus())){
                            savedValue = codetype.getId();
                        }
                    }
                    pstmt.setInt(28, savedValue);
                    pstmt.setInt(29, targetMarket.getTargetMarketPlanId());
                                            
    	            if (SMRCLogger.isDebuggerEnabled()) {
    	            	SMRCLogger.debug("updateTargetMarketPlan" + targetMarketUpdate);       	
    	            }
                    pstmt.executeUpdate();
                    
                    updateTMSegments(targetMarket, DBConn, userid);
                    updateTMContacts(targetMarket, DBConn, userid);
                    updateTMPlanAccounts(targetMarket, DBConn, userid);
                    updateTMEndCustomers(targetMarket, DBConn, userid);
                    updateTMProducts(targetMarket, DBConn);
                    
            }catch (Exception e)	{
            	throw new SMRCException("TargetMarketDAO.insertNewTargetMarketPlan(): ", e);
            }
            finally {
            	SMRCConnectionPoolUtils.close(rs);
            	SMRCConnectionPoolUtils.close(pstmt);
            }

          
}
   
   
   private static void updateTMSegments(TargetMarket targetMarket, Connection DBConn, String userid) throws Exception {
       ArrayList tmList = new ArrayList();
       ArrayList dbContentList = new ArrayList();
       ArrayList insertList = new ArrayList();
       ArrayList deleteList = new ArrayList();
       try {
           tmList = targetMarket.getPlanSegments();
           dbContentList = getTMSegments(targetMarket.getTargetMarketPlanId(), DBConn);

           // Create insert list
           for (int tmIndex=0; tmIndex < tmList.size(); tmIndex++){
               boolean found = false;
               int segmentId = ((Integer) tmList.get(tmIndex)).intValue();
               for (int dbIndex = 0; dbIndex < dbContentList.size(); dbIndex++){
                   int dbSegId = ((Integer) dbContentList.get(dbIndex)).intValue();
                   if (segmentId == dbSegId){
                       found = true;
                   }
               }
               if (!found){
                    insertList.add(tmList.get(tmIndex));
               }
           }

           // Create delete list
           for (int dbIndex=0; dbIndex < dbContentList.size(); dbIndex++){
               boolean found = false;
               int dbSegId = ((Integer) dbContentList.get(dbIndex)).intValue();
               for (int tmIndex = 0; tmIndex < tmList.size(); tmIndex++){
                   int segmentId = ((Integer) tmList.get(tmIndex)).intValue();
                   if (segmentId == dbSegId){
                       found = true;
                   }
               }
               if (!found){
                    deleteList.add(dbContentList.get(dbIndex));
               }
           }

           if (insertList.size() > 0){
               insertTargetMarketSegment(targetMarket.getTargetMarketPlanId(), insertList, DBConn, userid);
           } 
           if (deleteList.size() > 0){
               deleteTargetMarketSegment(targetMarket.getTargetMarketPlanId(), deleteList, DBConn);
           }
       }catch (Exception e)	{
    	   throw new SMRCException("TargetMarketDAO.updateTMSegments(): ", e);
       }
        
   }
   
   private static void updateTMContacts(TargetMarket targetMarket, Connection DBConn, String userid) throws Exception {
       ArrayList tmList = new ArrayList();
       ArrayList dbContentList = new ArrayList();
       ArrayList insertList = new ArrayList();
       ArrayList deleteList = new ArrayList();
       
       try {
           tmList = targetMarket.getPlanContacts();
           dbContentList = getTMContacts(targetMarket.getTargetMarketPlanId(), DBConn);

           // Create insert list
           for (int tmIndex=0; tmIndex < tmList.size(); tmIndex++){
               boolean found = false;
               int contactId = ((Contact) tmList.get(tmIndex)).getId();
               for (int dbIndex = 0; dbIndex < dbContentList.size(); dbIndex++){
                   int dbContactId = ((Contact) dbContentList.get(dbIndex)).getId();
                   if (contactId == dbContactId){
                       found = true;
                   }
               }
               if (!found){
                    insertList.add(tmList.get(tmIndex));
               }
           }

           // Create delete list
           for (int dbIndex=0; dbIndex < dbContentList.size(); dbIndex++){
               boolean found = false;
               int dbContactId = ((Contact) dbContentList.get(dbIndex)).getId();
               for (int tmIndex = 0; tmIndex < tmList.size(); tmIndex++){
                   int contactId = ((Contact) tmList.get(tmIndex)).getId();
                   if (contactId == dbContactId){
                       found = true;
                   }
               }
               if (!found){
                    deleteList.add(dbContentList.get(dbIndex));
               }
           }

           if (insertList.size() > 0){
               insertTargetMarketContacts(targetMarket.getTargetMarketPlanId(), insertList, DBConn, userid);
           } 
           if (deleteList.size() > 0){
               deleteTargetMarketContacts(targetMarket.getTargetMarketPlanId(), deleteList, DBConn);
           }
       }catch (Exception e)	{
    	   throw new SMRCException("TargetMarketDAO.updateTMContacts(): ", e);
       }
       
   }
   
   private static void updateTMPlanAccounts(TargetMarket targetMarket, Connection DBConn, String userid) throws Exception {
       ArrayList tmList = new ArrayList();
       ArrayList dbContentList = new ArrayList();
       ArrayList insertList = new ArrayList();
       ArrayList deleteList = new ArrayList();
       
       try {
           tmList = targetMarket.getPlanAccounts();
           dbContentList = getTMOtherAccounts(targetMarket.getTargetMarketPlanId(), DBConn);

           // Create insert list
           for (int tmIndex=0; tmIndex < tmList.size(); tmIndex++){
               boolean found = false;
               String vcn = (String) tmList.get(tmIndex);
               for (int dbIndex = 0; dbIndex < dbContentList.size(); dbIndex++){
                   String dbVcn = (String) dbContentList.get(dbIndex);
                   if (vcn.equalsIgnoreCase(dbVcn)){
                       found = true;
                   }
               }
               if (!found){
                    insertList.add(tmList.get(tmIndex));
               }
           }

           // Create delete list
           for (int dbIndex=0; dbIndex < dbContentList.size(); dbIndex++){
               boolean found = false;
               String dbVcn = (String) dbContentList.get(dbIndex);
               for (int tmIndex = 0; tmIndex < tmList.size(); tmIndex++){
                   String vcn = (String) tmList.get(tmIndex);
                   if (dbVcn.equalsIgnoreCase(vcn)){
                       found = true;
                   }
               }
               if (!found){
                    deleteList.add(dbContentList.get(dbIndex));
               }
           }

           if (insertList.size() > 0){
               insertTargetMarketAccounts(targetMarket.getTargetMarketPlanId(), insertList, DBConn, userid);
           } 
           if (deleteList.size() > 0){
               deleteTargetMarketAccounts(targetMarket.getTargetMarketPlanId(), deleteList, DBConn);
           }
       }catch (Exception e)	{
    	   throw new SMRCException("TargetMarketDAO.updateTMPlanAccounts(): ", e);
       }
   }
   
    private static void updateTMEndCustomers(TargetMarket targetMarket, Connection DBConn, String userid) throws Exception {
       ArrayList tmList = new ArrayList();
       ArrayList dbContentList = new ArrayList();
       ArrayList insertList = new ArrayList();
       ArrayList deleteList = new ArrayList();
       
       try {
           tmList = targetMarket.getEndCustomers();
           dbContentList = getTMEndCustomers(targetMarket.getTargetMarketPlanId(), DBConn);

           // Create insert list
           for (int tmIndex=0; tmIndex < tmList.size(); tmIndex++){
               boolean found = false;
               String vcn = (String) tmList.get(tmIndex);
               for (int dbIndex = 0; dbIndex < dbContentList.size(); dbIndex++){
                   String dbVcn = (String) dbContentList.get(dbIndex);
                   if (vcn.equalsIgnoreCase(dbVcn)){
                       found = true;
                   }
               }
               if (!found){
                   insertList.add(tmList.get(tmIndex));
               }
           }

           // Create delete list
           for (int dbIndex=0; dbIndex < dbContentList.size(); dbIndex++){
               boolean found = false;
               String dbVcn = (String) dbContentList.get(dbIndex);
               for (int tmIndex = 0; tmIndex < tmList.size(); tmIndex++){
                   String vcn = (String) tmList.get(tmIndex);
                   if (dbVcn.equalsIgnoreCase(vcn)){
                       found = true;
                   }
               }
               if (!found){
                    deleteList.add(dbContentList.get(dbIndex));
               }
           }

           if (insertList.size() > 0){
               insertTargetMarketEndCustomers(targetMarket.getTargetMarketPlanId(), insertList, DBConn, userid);
           } 
           if (deleteList.size() > 0){
               deleteTargetMarketEndCustomers(targetMarket.getTargetMarketPlanId(), deleteList, DBConn);
           }
           
       }catch (Exception e)	{
    	   throw new SMRCException("TargetMarketDAO.updateTMEndCustomers(): ", e);
        }
   }
    
    private static void updateTMProducts(TargetMarket targetMarket, Connection DBConn) throws Exception {
       ArrayList tmList = new ArrayList();
       ArrayList dbContentList = new ArrayList();
       ArrayList insertList = new ArrayList();
       ArrayList deleteList = new ArrayList();
       ArrayList updateList = new ArrayList();
       
       try {
           tmList = targetMarket.getPlanProducts();
           dbContentList = getTargetMarketProducts(targetMarket.getTargetMarketPlanId(), DBConn);

           // Create insert list
           for (int tmIndex=0; tmIndex < tmList.size(); tmIndex++){
               boolean found = false;
               boolean update = false;
               TargetMarketProduct targetMarketProduct = (TargetMarketProduct) tmList.get(tmIndex);
                for (int dbIndex = 0; dbIndex < dbContentList.size(); dbIndex++){
                   TargetMarketProduct dbTMProduct = (TargetMarketProduct) dbContentList.get(dbIndex);
                   if ((targetMarketProduct.getProduct()).getId().equalsIgnoreCase((dbTMProduct.getProduct()).getId())){
                       found = true;
                       // See if PeriodYYYY or Sales Objectives have changed
                       if ((targetMarketProduct.getProduct().getPeriodYYYY() != dbTMProduct.getProduct().getPeriodYYYY()) ||
                           (targetMarketProduct.getSalesObjective() != dbTMProduct.getSalesObjective())){
                               update = true;
                       }
                   }
               }
               if (!found){
                    insertList.add(tmList.get(tmIndex));
               } 
               if (update){
                   updateList.add(tmList.get(tmIndex));
               }
           }

           // Create delete list
           for (int dbIndex=0; dbIndex < dbContentList.size(); dbIndex++){
               boolean found = false;
               TargetMarketProduct dbTMProduct = (TargetMarketProduct) dbContentList.get(dbIndex);
               for (int tmIndex = 0; tmIndex < tmList.size(); tmIndex++){
                   TargetMarketProduct targetMarketProduct = (TargetMarketProduct) tmList.get(tmIndex);
                   if ((targetMarketProduct.getProduct()).getId().equalsIgnoreCase((dbTMProduct.getProduct()).getId())){
                       found = true;
                   }
               }
               if (!found){
                    deleteList.add(dbContentList.get(dbIndex));
               }
           }

           if (insertList.size() > 0){
               insertTargetMarketProducts(targetMarket.getTargetMarketPlanId(), insertList, DBConn);
           } 
           if (deleteList.size() > 0){
               deleteTargetMarketProducts(targetMarket.getTargetMarketPlanId(), deleteList, DBConn);
           }
           if (updateList.size() > 0){
               updateTargetMarketProducts(targetMarket.getTargetMarketPlanId(), updateList, DBConn);
           }
           
       }catch (Exception e)	{
    	   throw new SMRCException("TargetMarketDAO.updateTMProducts(): ", e);
           }
       }
  
   
    private static int getMonthValue(java.util.Date convertDate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(convertDate);
        return (calendar.get(Calendar.MONTH) + 1);
    }
    
    public static HashMap getActiveTMsAndSalesTrackingId(String conversionFilter, String segmentFilter, Calendar startDate, Calendar endDate, Connection DBConn)throws Exception{
        StringBuffer theQuery = new StringBuffer();
        theQuery.append("select target_market_plans.sales_tracking_type_id, target_market_plans.target_market_plan_id ");
        theQuery.append("from target_market_plans, code_types ");
        if (segmentFilter.length() > 0){
            theQuery.append(", target_mrkt_plan_segments ");
        }
        theQuery.append("where target_market_plans.target_market_plan_status_id = code_types.code_type_id ");
        theQuery.append("and code_types.code_type = 'target_market_status' ");
        theQuery.append("and code_types.code_value = 'A' ");
        theQuery.append("and target_market_plans.end_date >= to_date('" + (startDate.get(Calendar.MONTH) + 1) + "-" + startDate.get(Calendar.YEAR) + "','MM-YYYY') ");   
        theQuery.append("and target_market_plans.start_date <= to_date('" + (endDate.get(Calendar.MONTH) + 1) + "-" + endDate.get(Calendar.YEAR) + "','MM-YYYY')  ");
        if (conversionFilter.length() > 0){
            theQuery.append("and target_market_plans.competitor_convert = 'Y' ");
            theQuery.append("and target_market_plans.competitor_id = " + conversionFilter + " ");
        }
        if (segmentFilter.length() > 0){
            theQuery.append("and target_mrkt_plan_segments.target_market_plan_id = target_market_plans.target_market_plan_id ");
            theQuery.append("and target_mrkt_plan_segments.segment_id = " + segmentFilter + " ");
        }
        
        theQuery.append("group by  target_market_plans.target_market_plan_id, target_market_plans.sales_tracking_type_id");
       
        HashMap results = new HashMap();
        Statement stmt = null;
        ResultSet rs  = null;
 		
        try {
                stmt = DBConn.createStatement();
	            if (SMRCLogger.isDebuggerEnabled()) {
	            	SMRCLogger.debug("getActiveTMsAndSalesTrackingId" + theQuery.toString());       	
	            }
                rs = stmt.executeQuery(theQuery.toString());
                while (rs.next())		
                {
                    Integer tmId = new Integer(rs.getInt("target_market_plan_id"));
                    Integer salesTrackingTypeId = new Integer(rs.getInt("sales_tracking_type_id"));
                    results.put(tmId, salesTrackingTypeId);
                        
                }
        }catch (Exception e)	{
        	throw new SMRCException("TargetMarketDAO.getActiveTMsAndSalesTrackingId(): ", e);
        }
        finally {
        	SMRCConnectionPoolUtils.close(rs);
        	SMRCConnectionPoolUtils.close(stmt);
        }

        return results;
    }
    
    
    public static ArrayList getTMTotals (ArrayList tapDollarIds, ArrayList endmarketIds, Calendar startDate, Calendar endDate, Connection DBConn) throws Exception{
        ArrayList tmBeans = new ArrayList();
        try {
            
            if (tapDollarIds.size() > 0){
                String theQuery = createTMTotalQuery(tapDollarIds, "TAPDollars");
                if (SMRCLogger.isDebuggerEnabled()) {
                	SMRCLogger.debug("getTMTotals : " + theQuery);               	
                }
                ArrayList stockBeans = executeTMTotalQuery(theQuery, DBConn);
                for (int i=0; i< stockBeans.size(); i++){
                    TMBean tmBean = (TMBean) stockBeans.get(i);
                    tmBeans.add(tmBean);
                }                 
            }
            if (endmarketIds.size() > 0){
                String theQuery = createTMTotalQuery(endmarketIds, "EndMarket");
                if (SMRCLogger.isDebuggerEnabled()) {
                	SMRCLogger.debug("getTMTotals : " + theQuery);               	
                }
                ArrayList endBeans = executeTMTotalQuery(theQuery, DBConn);
                for (int i=0; i< endBeans.size(); i++){
                    TMBean tmBean = (TMBean) endBeans.get(i);
                    tmBeans.add(tmBean);
                }                
            }    
             
        }catch (Exception e)	{
        	throw new SMRCException("TargetMarketDAO.getTMTotals(): ", e);
        }
                
        return tmBeans;
    }
    
    public static ArrayList getTMGroupByTotals (ArrayList TAPDollarsIds, ArrayList endmarketIds, String groupBy, 
        String divisionFilter, String zoneFilter, String districtFilter, Connection DBConn) throws Exception{
        ArrayList tmGroupBys = new ArrayList();
        try {
            
            if (TAPDollarsIds.size() > 0){
                String theQuery = "";
                ArrayList stockBeans = new ArrayList();
               if (groupBy.equalsIgnoreCase("division")){
                    theQuery = createTMDivisionQuery(TAPDollarsIds, "TAPDollars", divisionFilter, zoneFilter, districtFilter);
                } else if (groupBy.equalsIgnoreCase("district")){
                    theQuery = createTMDistrictQuery(TAPDollarsIds, "TAPDollars", divisionFilter, zoneFilter, districtFilter);
                } else if (groupBy.equalsIgnoreCase("plan")){
                	theQuery = createTMPlanQuery(TAPDollarsIds,"TAPDollars", divisionFilter,zoneFilter,districtFilter);
                } else {
                    theQuery = createTMAccountQuery(TAPDollarsIds, "TAPDollars", divisionFilter, zoneFilter, districtFilter);
                }
                if (SMRCLogger.isDebuggerEnabled()) {
                	SMRCLogger.debug("getTMGroupByTotals : " + theQuery);               	
                }
                stockBeans = executeTMGroupingTotalQuery(theQuery, groupBy, DBConn);
                for (int i=0; i< stockBeans.size(); i++){
                    TMGroupByBean tmGroupByBean = (TMGroupByBean) stockBeans.get(i);
                    tmGroupBys.add(tmGroupByBean);
                }                 
            }
            if (endmarketIds.size() > 0){
                String theQuery = "";
                ArrayList endBeans = new ArrayList();
                if (groupBy.equalsIgnoreCase("division")){
                    theQuery = createTMDivisionQuery(endmarketIds, "EndMarket", divisionFilter, zoneFilter, districtFilter);
                } else if (groupBy.equalsIgnoreCase("district")){
                    theQuery = createTMDistrictQuery(endmarketIds, "EndMarket", divisionFilter, zoneFilter, districtFilter);
                } else if (groupBy.equalsIgnoreCase("plan")){
                	theQuery = createTMPlanQuery(endmarketIds,"EndMarket", divisionFilter,zoneFilter,districtFilter);
                } else {
                    theQuery = createTMAccountQuery(endmarketIds, "EndMarket", divisionFilter, zoneFilter, districtFilter);
                }
                if (SMRCLogger.isDebuggerEnabled()) {
                	SMRCLogger.debug("getTMGroupByTotals : " + theQuery);               	
                }
                endBeans = executeTMGroupingTotalQuery(theQuery, groupBy, DBConn);
                for (int i=0; i< endBeans.size(); i++){
                    TMGroupByBean tmGroupByBean = (TMGroupByBean) endBeans.get(i);
                    tmGroupBys.add(tmGroupByBean);
                }                
            }    
             
        }catch (Exception e)	{
        	throw new SMRCException("TargetMarketDAO.getTMGroupByTotals(): ", e);
        }
                
        return tmGroupBys;
    }
    
    private static String createTMTotalQuery (ArrayList tmIds, String salesTrackingType){
        StringBuffer tmTotalsql = new StringBuffer();
        
        // Not sure if they want invoice or order, so I will define it as a variable here
        
 //       String dollarType = "order_tap_dollars";
        String dollarType = "invoice_tap_dollars";

// Braffet : Need to evaluate this...
        
// Changed to Target market dollars        
        
       
//      TODO May need to use Years table here       

        tmTotalsql.append("with tmtotals as ( ");
        if (salesTrackingType.trim().equalsIgnoreCase("TAPDollars")){
	        tmTotalsql.append(" select  max(target_market_plans.maximum_payout) maximum_payout, ");
	     //   tmTotalsql.append("nvl(sum(case when tap_dollars.year = (to_number(to_char(target_market_plans.start_date, 'YYYY')) - 1) and tap_dollars.month >= to_number(to_char(target_market_plans.start_date, 'MM')) and tap_dollars.month <= to_number(to_char(target_market_plans.end_date, 'MM')) then tap_dollars." + dollarType + " else 0 end),0) sales_baseline, ");
	        tmTotalsql.append("nvl(sum(case when tap_dollars.year = (to_number(to_char(target_market_plans.start_date, 'YYYY')) - 1) and tap_dollars.month >= to_number(to_char(target_market_plans.start_date, 'MM')) and tap_dollars.month <= (case when (to_number(to_char(target_market_plans.start_date, 'YYYY')) < years.year or to_number(to_char(target_market_plans.end_date, 'MM')) < years.month) then to_number(to_char(target_market_plans.end_date, 'MM')) else years.month end) then tap_dollars." + dollarType + " else 0 end),0) sales_baseline, ");
	        tmTotalsql.append("nvl(sum(case when tap_dollars.year = (to_number(to_char(target_market_plans.start_date, 'YYYY'))) and tap_dollars.month >= to_number(to_char(target_market_plans.start_date, 'MM')) and tap_dollars.month <= to_number(to_char(target_market_plans.end_date, 'MM')) then tap_dollars." + dollarType + " else 0 end),0) sales_plan, ");
	        tmTotalsql.append("nvl(sum(case when tap_dollars.year = (to_number(to_char(add_months(target_market_plans.start_date,-12), 'YYYY'))) and tap_dollars.month >= to_number(to_char(target_market_plans.start_date, 'MM')) and tap_dollars.month <= to_number(to_char(target_market_plans.end_date, 'MM')) then tap_dollars." + dollarType + " else 0 end),0) prev_yr_plan, ");
	        tmTotalsql.append("target_market_plans.target_market_plan_id from tap_dollars, target_mrkt_plan_accts, target_market_plans, target_mrkt_plan_products, years ");
	        tmTotalsql.append("where tap_dollars.product_id = target_mrkt_plan_products.product_id ");
	        tmTotalsql.append("and target_mrkt_plan_accts.target_market_plan_id = target_market_plans.target_market_plan_id ");
	        tmTotalsql.append("and tap_dollars.vista_customer_number = target_mrkt_plan_accts.vista_customer_number and target_mrkt_plan_products.target_market_plan_id = target_market_plans.target_market_plan_id ");
	        tmTotalsql.append("and target_market_plans.target_market_plan_id in (");
	        for (int i=0; i< tmIds.size(); i++){
	            int tmId = ((Integer) tmIds.get(i)).intValue();
	            if (i>0){
	                tmTotalsql.append(", ");
	            }
	            tmTotalsql.append("'" + tmId + "'");
	        }
	        tmTotalsql.append(") group by target_market_plans.target_market_plan_id ");
        } else {
        	tmTotalsql.append("	     select tmp.target_market_plan_id, tmp.maximum_payout, ");
  			tmTotalsql.append("      nvl(sum(case when sales.year = (to_number(to_char(tmp.start_date, 'YYYY'))) ");
  			tmTotalsql.append("            and sales.month >= to_number(to_char(tmp.start_date, 'MM')) ");
  			tmTotalsql.append("            and sales.month <= to_number(to_char(tmp.end_date, 'MM')) ");
  			tmTotalsql.append("           then sales.total_sales else 0 end),0) sales_plan, ");
  			tmTotalsql.append("      nvl(sum(case when sales.year = (to_number(to_char(add_months(tmp.start_date,-12), 'YYYY'))) ");
  			tmTotalsql.append("            and sales.month >= to_number(to_char(tmp.start_date, 'MM')) ");
  			tmTotalsql.append("            and sales.month <= to_number(to_char(tmp.end_date, 'MM')) ");
  			tmTotalsql.append("           then sales.total_sales else 0 end),0) prev_yr_plan, ");
  			tmTotalsql.append("      nvl(sum(case when sales.year = (to_number(to_char(tmp.start_date, 'YYYY')) - 1) ");
  			tmTotalsql.append("            and sales.month >= to_number(to_char(tmp.start_date, 'MM')) ");
  			tmTotalsql.append("            and sales.month <= (case when (to_number(to_char(tmp.start_date, 'YYYY')) < y.year) ");
  			tmTotalsql.append("           then to_number(to_char(tmp.end_date, 'MM')) else y.month end) ");
  			tmTotalsql.append("                then sales.total_sales else 0 end),0) sales_baseline ");
  			tmTotalsql.append("      from charge_end_market_sales sales, target_market_plans tmp, target_mrkt_ec_sales_plans ec, target_mrkt_plan_accts dist, ");
  			tmTotalsql.append("	           target_mrkt_plan_products prod, years y ");
  			tmTotalsql.append("      where tmp.target_market_plan_id in (");
  			for (int i=0; i< tmIds.size(); i++){
	            int tmId = ((Integer) tmIds.get(i)).intValue();
	            if (i>0){
	                tmTotalsql.append(", ");
	            }
	            tmTotalsql.append("'" + tmId + "'");
	        }
  			tmTotalsql.append(" )     and tmp.target_market_plan_id = dist.target_market_plan_id ");
  			tmTotalsql.append("      and sales.charge_to_customer_number = dist.vista_customer_number ");
  			tmTotalsql.append("      and tmp.target_market_plan_id = ec.target_market_plan_id ");
  			tmTotalsql.append("      and sales.end_market_customer_number = ec.vista_customer_number ");
  			tmTotalsql.append("      and tmp.target_market_plan_id = prod.target_market_plan_id ");
  			tmTotalsql.append("      and sales.product_id = prod.product_id ");
  			tmTotalsql.append("      group by tmp.target_market_plan_id, tmp.maximum_payout "); 
        }
        tmTotalsql.append(") select plantotals.*, ");
        tmTotalsql.append("nvl(case when ((target_market_plans.percent_growth1 <= plantotals.totalgrowthpercentage) and (plantotals.totalgrowthpercentage < target_market_plans.percent_growth2)) then (plantotals.totalgrowth * (target_market_plans.percent_payout1 / 100)) else ");
        tmTotalsql.append("(case when ((target_market_plans.percent_growth2 <= plantotals.totalgrowthpercentage) and (plantotals.totalgrowthpercentage < target_market_plans.percent_growth3)) then (plantotals.totalgrowth * (target_market_plans.percent_payout2 / 100)) else ");
        tmTotalsql.append("(case when ((target_market_plans.percent_growth3 <= plantotals.totalgrowthpercentage) and (plantotals.totalgrowthpercentage < target_market_plans.percent_growth4)) then (plantotals.totalgrowth * (target_market_plans.percent_payout3 / 100)) else ");
        tmTotalsql.append("(case when (plantotals.totalgrowthpercentage >= target_market_plans.percent_growth4) then (plantotals.totalgrowth * (target_market_plans.percent_payout4 / 100)) else 0 end ) ");
        tmTotalsql.append(" end) end ) end,0) growth_payout from target_market_plans, ( select tmtotals.*,  (nvl((((tmtotals.sales_plan - tmtotals.sales_baseline) / decode(tmtotals.sales_baseline,0,1,tmtotals.sales_baseline))),0) * tmtotals.prev_yr_plan) totalgrowth, ");
        tmTotalsql.append("nvl((((tmtotals.sales_plan - tmtotals.sales_baseline) / decode(tmtotals.sales_baseline,0,1,tmtotals.sales_baseline)) * 100),0) totalgrowthpercentage from tmtotals " );
        tmTotalsql.append(") plantotals where target_market_plans.target_market_plan_id = plantotals.target_market_plan_id ");
       
      
        return tmTotalsql.toString();
        
        
    }
    
    private static String createTMDivisionQuery (ArrayList tmIds, String salesTrackingType, String divisionFilter,
            String zoneFilter, String districtFilter){
        StringBuffer tmProductsql = new StringBuffer();
        // Not sure if they want invoice or order, so I will define it as a variable here
        
 //       String dollarType = "order_tap_dollars";
        String dollarType = "";
        if (salesTrackingType.trim().equalsIgnoreCase("TAPDollars")){
        	dollarType = "invoice_tap_dollars";
        } else {
        	dollarType = "total_sales";
        }

// Braffet : Need to evaluate this...
        
// Changed to Target market dollars        
        
//        if (salesTrackingType.equalsIgnoreCase("stock")){
//            dollarType = "stock_sales";
//        } 
  
//      TODO May need to use Years table here
        tmProductsql.append("select  nvl(sum(case when dollars.year = (to_number(to_char(target_market_plans.start_date, 'YYYY'))) and dollars.month >= to_number(to_char(target_market_plans.start_date, 'MM')) and dollars.month <= to_number(to_char(target_market_plans.end_date, 'MM')) then dollars." + dollarType + " else 0 end),0) sales_plan,  ");
        tmProductsql.append("target_mrkt_plan_products.product_id, nvl(sum(case when dollars.year = (to_number(to_char(target_market_plans.start_date, 'YYYY')) - 1) and dollars.month >= to_number(to_char(target_market_plans.start_date, 'MM')) and dollars.month <= (case when (to_number(to_char(target_market_plans.start_date, 'YYYY')) < years.year or to_number(to_char(target_market_plans.end_date, 'MM')) < years.month) then to_number(to_char(target_market_plans.end_date, 'MM')) else years.month end) then dollars." + dollarType + " else 0 end),0) sales_baseline, ");
        tmProductsql.append("target_market_plans.target_market_plan_id, product_line_division.division_id groupingId from");
        if (salesTrackingType.trim().equalsIgnoreCase("TAPDollars")){
   			tmProductsql.append(" tap_dollars dollars, ");
   		} else {
   			tmProductsql.append(" charge_end_market_sales dollars, ");
   		}
   		tmProductsql.append(" target_mrkt_plan_accts, target_market_plans, years, ");
        tmProductsql.append("target_mrkt_plan_products, product_line_division ");
        if ((zoneFilter.length() > 0) || (districtFilter.length() > 0)){
            tmProductsql.append(", customer ");
        }
        if (salesTrackingType.trim().equalsIgnoreCase("EndMarket")){
        	tmProductsql.append(", target_mrkt_ec_sales_plans ec ");
        }
        tmProductsql.append("where dollars.product_id = target_mrkt_plan_products.product_id ");
        tmProductsql.append("and target_mrkt_plan_accts.target_market_plan_id = target_market_plans.target_market_plan_id ");
        tmProductsql.append(" and dollars.");
        if (salesTrackingType.trim().equalsIgnoreCase("TAPDollars")){
   			tmProductsql.append("vista_customer_number");
   		} else {
   			tmProductsql.append("charge_to_customer_number");
   		}
         
        tmProductsql.append(" = target_mrkt_plan_accts.vista_customer_number ");
        tmProductsql.append("and target_mrkt_plan_products.target_market_plan_id = target_market_plans.target_market_plan_id ");
        tmProductsql.append("and target_market_plans.target_market_plan_id in (");
        for (int i=0; i< tmIds.size(); i++){
            int tmId = ((Integer) tmIds.get(i)).intValue();
            if (i>0){
                tmProductsql.append(", ");
            }
            tmProductsql.append("'" + tmId + "'");
        }
        tmProductsql.append(") and product_line_division.product_id = target_mrkt_plan_products.product_id " );
        if (salesTrackingType.trim().equalsIgnoreCase("EndMarket")){
        	tmProductsql.append(" and target_market_plans.target_market_plan_id = ec.target_market_plan_id ");
        	tmProductsql.append(" and dollars.end_market_customer_number = ec.vista_customer_number ");
        }
        if (divisionFilter.length() > 0){
            tmProductsql.append("and product_line_division.division_id = " + divisionFilter + " ");
        }
        if (zoneFilter.length() > 0){
            tmProductsql.append("and customer.vista_customer_number = target_mrkt_plan_accts.vista_customer_number ");
            tmProductsql.append("and substr(customer.sp_geog,1,4)||'0' = '" + zoneFilter + "' ");
        }
        if (districtFilter.length() > 0){
            tmProductsql.append("and customer.vista_customer_number = target_mrkt_plan_accts.vista_customer_number ");
            tmProductsql.append("and substr(customer.sp_geog,1,5) = '" + districtFilter + "' ");
        }
        
        tmProductsql.append("group by  target_mrkt_plan_products.product_id , target_market_plans.target_market_plan_id, product_line_division.division_id ");
                     
        
        
        return tmProductsql.toString();
        
        
    }
    
     private static String createTMDistrictQuery (ArrayList tmIds, String salesTrackingType, String divisionFilter,
            String zoneFilter, String districtFilter){
        StringBuffer tmDistrictsql = new StringBuffer();

        String dollarType = "";
        if (salesTrackingType.trim().equalsIgnoreCase("TAPDollars")){
        	dollarType = "invoice_tap_dollars";
        } else {
        	dollarType = "total_sales";
        }
        
//      TODO May need to use Years table here
        tmDistrictsql.append("select  nvl(sum(case when dollars.year = (to_number(to_char(target_market_plans.start_date, 'YYYY'))) and dollars.month >= to_number(to_char(target_market_plans.start_date, 'MM')) and dollars.month <= to_number(to_char(target_market_plans.end_date, 'MM')) then dollars." + dollarType + " else 0 end),0) sales_plan, ");
        tmDistrictsql.append("nvl(sum(case when dollars.year = (to_number(to_char(target_market_plans.start_date, 'YYYY')) - 1) and dollars.month >= to_number(to_char(target_market_plans.start_date, 'MM')) and dollars.month <= (case when (to_number(to_char(target_market_plans.start_date, 'YYYY')) < years.year or to_number(to_char(target_market_plans.end_date, 'MM')) < years.month) ");
        tmDistrictsql.append("then to_number(to_char(target_market_plans.end_date, 'MM')) else years.month end) ");
        tmDistrictsql.append("then dollars." + dollarType + " else 0 end),0) sales_baseline, ");
        tmDistrictsql.append("target_market_plans.target_market_plan_id, substr(customer.sp_geog,1,5) groupingId ");
        tmDistrictsql.append("from ");
        if (salesTrackingType.trim().equalsIgnoreCase("TAPDollars")){
        	tmDistrictsql.append(" tap_dollars dollars, ");
   		} else {
   			tmDistrictsql.append(" charge_end_market_sales dollars, ");
   		}
        tmDistrictsql.append("target_mrkt_plan_accts, target_market_plans, target_mrkt_plan_products, customer, years ");
        if (salesTrackingType.equalsIgnoreCase("endmarket")){
            tmDistrictsql.append(", target_mrkt_ec_sales_plans ");
        }
         if (divisionFilter.length() > 0){
            tmDistrictsql.append(", product_line_division ");
        }
        tmDistrictsql.append("where dollars.product_id = target_mrkt_plan_products.product_id  ");
        tmDistrictsql.append("and target_mrkt_plan_accts.target_market_plan_id = target_market_plans.target_market_plan_id ");
        tmDistrictsql.append("and dollars.");
        if (salesTrackingType.trim().equalsIgnoreCase("TAPDollars")){
        	tmDistrictsql.append("vista_customer_number");
   		} else {
   			tmDistrictsql.append("charge_to_customer_number");
   		}
        tmDistrictsql.append(" = target_mrkt_plan_accts.vista_customer_number ");
        tmDistrictsql.append("and target_mrkt_plan_products.target_market_plan_id = target_market_plans.target_market_plan_id ");
        tmDistrictsql.append("and target_market_plans.target_market_plan_id in (");
        for (int i=0; i< tmIds.size(); i++){
            int tmId = ((Integer) tmIds.get(i)).intValue();
            if (i>0){
                tmDistrictsql.append(", ");
            }
            tmDistrictsql.append("'" + tmId + "'");
        }
        tmDistrictsql.append(") and customer.vista_customer_number = target_mrkt_plan_accts.vista_customer_number ");
        if (salesTrackingType.equalsIgnoreCase("endmarket")){
            tmDistrictsql.append("and dollars.end_market_customer_number = target_mrkt_ec_sales_plans.vista_customer_number ");
            tmDistrictsql.append("and target_mrkt_ec_sales_plans.target_market_plan_id = target_market_plans.target_market_plan_id ");
        }
        if (divisionFilter.length() > 0){
            tmDistrictsql.append("and product_line_division.product_id = target_mrkt_plan_products.product_id ");
            tmDistrictsql.append("and product_line_division.division_id = " + divisionFilter + " ");
        }
        if (zoneFilter.length() > 0){
            tmDistrictsql.append("and substr(customer.sp_geog,1,4)||'0' = '" + zoneFilter + "' ");
        }
        if (districtFilter.length() > 0){
            tmDistrictsql.append("and substr(customer.sp_geog,1,5) = '" + districtFilter + "' ");
        }
        tmDistrictsql.append("group by  substr(customer.sp_geog,1,5), target_market_plans.target_market_plan_id ");
        
        return tmDistrictsql.toString();
         
    }
     
    private static String createTMAccountQuery (ArrayList tmIds, String salesTrackingType, String divisionFilter,
            String zoneFilter, String districtFilter){
        StringBuffer tmAccountsql = new StringBuffer();

        String dollarType = "";
        String customerField = "";
        if (salesTrackingType.trim().equalsIgnoreCase("TAPDollars")){
        	dollarType = "invoice_tap_dollars";
        	customerField = "vista_customer_number";
        } else {
        	dollarType = "total_sales";
        	customerField = "charge_to_customer_number";
        }
        
//      TODO May need to use Years table here
        tmAccountsql.append("select  nvl(sum(case when dollars.year = (to_number(to_char(target_market_plans.start_date, 'YYYY'))) and dollars.month >= to_number(to_char(target_market_plans.start_date, 'MM')) and dollars.month <= to_number(to_char(target_market_plans.end_date, 'MM')) then dollars." + dollarType + " else 0 end),0) sales_plan, ");
        tmAccountsql.append("nvl(sum(case when dollars.year = (to_number(to_char(target_market_plans.start_date, 'YYYY')) - 1) and dollars.month >= to_number(to_char(target_market_plans.start_date, 'MM')) and dollars.month <= (case when (to_number(to_char(target_market_plans.start_date, 'YYYY')) < years.year or to_number(to_char(target_market_plans.end_date, 'MM')) < years.month) ");
        tmAccountsql.append("then to_number(to_char(target_market_plans.end_date, 'MM')) else years.month end) ");
        tmAccountsql.append("then dollars." + dollarType + " else 0 end),0) sales_baseline, ");
        tmAccountsql.append("target_market_plans.target_market_plan_id, product_line_division.division_id subGroupingId, dollars." + customerField + " groupingId ");
        tmAccountsql.append("from ");
        if (salesTrackingType.trim().equalsIgnoreCase("TAPDollars")){
        	tmAccountsql.append(" tap_dollars dollars, ");
   		} else {
   			tmAccountsql.append(" charge_end_market_sales dollars, ");
   		}
        tmAccountsql.append("target_mrkt_plan_accts, target_market_plans, target_mrkt_plan_products, product_line_division, years ");
        if (salesTrackingType.equalsIgnoreCase("endmarket")){
            tmAccountsql.append(", target_mrkt_ec_sales_plans ");
        }
        if ((zoneFilter.length() > 0) || (districtFilter.length() > 0)){
            tmAccountsql.append(", customer ");
        }
        tmAccountsql.append("where dollars.product_id = target_mrkt_plan_products.product_id  and target_mrkt_plan_accts.target_market_plan_id = target_market_plans.target_market_plan_id ");
        tmAccountsql.append("and dollars." + customerField + " = target_mrkt_plan_accts.vista_customer_number and target_mrkt_plan_products.target_market_plan_id = target_market_plans.target_market_plan_id ");
        tmAccountsql.append("and target_market_plans.target_market_plan_id in (");
        for (int i=0; i< tmIds.size(); i++){
            int tmId = ((Integer) tmIds.get(i)).intValue();
            if (i>0){
                tmAccountsql.append(", ");
            }
            tmAccountsql.append("'" + tmId + "'");
        }
        tmAccountsql.append(") ");
        if (salesTrackingType.equalsIgnoreCase("endmarket")){
            tmAccountsql.append("and dollars.end_market_customer_number = target_mrkt_ec_sales_plans.vista_customer_number ");
            tmAccountsql.append("and target_mrkt_ec_sales_plans.target_market_plan_id = target_market_plans.target_market_plan_id ");
        }
        if (divisionFilter.length() > 0){
            tmAccountsql.append("and product_line_division.division_id = " + divisionFilter + " ");
        }
        if (zoneFilter.length() > 0){
            tmAccountsql.append("and customer.vista_customer_number = target_mrkt_plan_accts.vista_customer_number ");
            tmAccountsql.append("and substr(customer.sp_geog,1,4)||'0' = '" + zoneFilter + "' ");
        }
        if (districtFilter.length() > 0){
            tmAccountsql.append("and customer.vista_customer_number = target_mrkt_plan_accts.vista_customer_number ");
            tmAccountsql.append("and substr(customer.sp_geog,1,5) = '" + districtFilter + "' ");
        }
        tmAccountsql.append("and product_line_division.product_id = target_mrkt_plan_products.product_id group by dollars." + customerField + ", target_market_plans.target_market_plan_id, product_line_division.division_id ");
        
        
        
        return tmAccountsql.toString();
         
    }
    
    private static String createTMPlanQuery (ArrayList tmIds, String salesTrackingType, String divisionFilter,
            String zoneFilter, String districtFilter){
        StringBuffer tmpQuerysql = new StringBuffer();

        String dollarType = "";
        String customerField = "";
        if (salesTrackingType.trim().equalsIgnoreCase("TAPDollars")){
        	dollarType = "invoice_tap_dollars";
        	customerField = "vista_customer_number";
        } else {
        	dollarType = "total_sales";
        	customerField = "charge_to_customer_number";
        }
        
//      TODO May need to use Years table here
        tmpQuerysql.append("select  nvl(sum(case when dollars.year = (to_number(to_char(target_market_plans.start_date, 'YYYY'))) and dollars.month >= to_number(to_char(target_market_plans.start_date, 'MM')) and dollars.month <= to_number(to_char(target_market_plans.end_date, 'MM')) then dollars." + dollarType + " else 0 end),0) sales_plan, ");
        tmpQuerysql.append("nvl(sum(case when dollars.year = (to_number(to_char(target_market_plans.start_date, 'YYYY')) - 1) and dollars.month >= to_number(to_char(target_market_plans.start_date, 'MM')) and dollars.month <= (case when (to_number(to_char(target_market_plans.start_date, 'YYYY')) < years.year or to_number(to_char(target_market_plans.end_date, 'MM')) < years.month) ");
        tmpQuerysql.append("then to_number(to_char(target_market_plans.end_date, 'MM')) else years.month end) ");
        tmpQuerysql.append("then dollars." + dollarType + " else 0 end),0) sales_baseline, ");
        tmpQuerysql.append("target_market_plans.target_market_plan_id groupingId, target_market_plans.target_market_plan_id ");
        tmpQuerysql.append("from ");
        if (salesTrackingType.trim().equalsIgnoreCase("TAPDollars")){
        	tmpQuerysql.append(" tap_dollars dollars, ");
   		} else {
   			tmpQuerysql.append(" charge_end_market_sales dollars, ");
   		}
        tmpQuerysql.append("target_mrkt_plan_accts, target_market_plans, target_mrkt_plan_products, years ");
        if (salesTrackingType.equalsIgnoreCase("endmarket")){
        	tmpQuerysql.append(", target_mrkt_ec_sales_plans ");
        }
        if ((zoneFilter.length() > 0) || (districtFilter.length() > 0)){
        	tmpQuerysql.append(", customer ");
        }
        if (divisionFilter.length() > 0){
        	tmpQuerysql.append(", product_line_division  ");
        }
        tmpQuerysql.append("where dollars.product_id = target_mrkt_plan_products.product_id ");
        tmpQuerysql.append("and target_mrkt_plan_accts.target_market_plan_id = target_market_plans.target_market_plan_id ");
        tmpQuerysql.append("and dollars." + customerField + " = target_mrkt_plan_accts.vista_customer_number ");
        tmpQuerysql.append("and target_mrkt_plan_products.target_market_plan_id = target_market_plans.target_market_plan_id ");
        tmpQuerysql.append("and target_market_plans.target_market_plan_id in (");
        for (int i=0; i< tmIds.size(); i++){
            int tmId = ((Integer) tmIds.get(i)).intValue();
            if (i>0){
            	tmpQuerysql.append(", ");
            }
            tmpQuerysql.append("'" + tmId + "'");
        }
        tmpQuerysql.append(") ");
        if (salesTrackingType.equalsIgnoreCase("endmarket")){
        	tmpQuerysql.append("and dollars.end_market_customer_number = target_mrkt_ec_sales_plans.vista_customer_number ");
        	tmpQuerysql.append("and target_mrkt_ec_sales_plans.target_market_plan_id = target_market_plans.target_market_plan_id ");
        }
        if (divisionFilter.length() > 0){
        	tmpQuerysql.append("and product_line_division.division_id = " + divisionFilter + " ");
        	tmpQuerysql.append("and product_line_division.product_id = target_mrkt_plan_products.product_id ");
        }
        if (zoneFilter.length() > 0){
        	tmpQuerysql.append("and customer.vista_customer_number = target_mrkt_plan_accts.vista_customer_number ");
        	tmpQuerysql.append("and substr(customer.sp_geog,1,4)||'0' = '" + zoneFilter + "' ");
        }
        if (districtFilter.length() > 0){
        	tmpQuerysql.append("and customer.vista_customer_number = target_mrkt_plan_accts.vista_customer_number ");
        	tmpQuerysql.append("and substr(customer.sp_geog,1,5) = '" + districtFilter + "' ");
        }
        
        tmpQuerysql.append("group by target_market_plans.target_market_plan_id ");
        
        
        
        return tmpQuerysql.toString();
         
    }
    
    private static ArrayList executeTMTotalQuery (String theQuery, Connection DBConn) throws Exception{
        ArrayList tmBeans = new ArrayList();
        Statement stmt = null;
        ResultSet rs = null;
        try{
            stmt = DBConn.createStatement();
            SMRCLogger.debug("executeTMTotalQuery SQL: " + theQuery);
            rs = stmt.executeQuery(theQuery);
            while (rs.next()){
                TMBean tmBean = new TMBean();
                tmBean.setTmid(rs.getInt("target_market_plan_id"));
                tmBean.setBaselineSales(rs.getDouble("sales_baseline"));
                tmBean.setPlanSales(rs.getDouble("sales_plan"));
                tmBean.setGrowth(rs.getDouble("totalgrowth"));
                double growth = rs.getDouble("growth_payout");
                double max = rs.getDouble("maximum_payout");
                tmBean.setMaxPayout(max);
                if (growth > max){
                    tmBean.setGrowthPayout(max);
                } else {
                    tmBean.setGrowthPayout(growth);
                }
                tmBean.setGrowthPercentage(rs.getDouble("totalgrowthpercentage"));
                tmBeans.add(tmBean);
            }
            
        
        }catch (Exception e)	{
        	throw new SMRCException("TargetMarketDAO.executeTMTotalQuery(): ", e);
        }
        finally {
        	SMRCConnectionPoolUtils.close(rs);
        	SMRCConnectionPoolUtils.close(stmt);
        }
        return tmBeans;
    }
    
    private static ArrayList executeTMGroupingTotalQuery (String theQuery, String groupBy, Connection DBConn) throws Exception{
        ArrayList tmGroupByBeans = new ArrayList();
        Statement stmt = null;
        ResultSet rs = null;
        try{
            stmt = DBConn.createStatement();
            rs = stmt.executeQuery(theQuery);
            while (rs.next()){
                TMGroupByBean tmGroupByBean = new TMGroupByBean();
                tmGroupByBean.setTmid(rs.getInt("target_market_plan_id"));
                tmGroupByBean.setBaselineSales(rs.getDouble("sales_baseline"));
                tmGroupByBean.setPlanSales(rs.getDouble("sales_plan"));
                tmGroupByBean.setGroupingId(rs.getString("groupingId"));
                if (groupBy.equalsIgnoreCase("account")){
                    tmGroupByBean.setSubGroupingId(rs.getString("subGroupingId"));
                }
                if (groupBy.equalsIgnoreCase("division")){
                    tmGroupByBean.setProductId(rs.getString("product_id"));
                } 
                tmGroupByBeans.add(tmGroupByBean);
            }
            
        
        }catch (Exception e)	{
        	throw new SMRCException("TargetMarketDAO.executeTMProductTotalQuery(): ", e);
         }
        finally {
        	SMRCConnectionPoolUtils.close(rs);
        	SMRCConnectionPoolUtils.close(stmt);
        }
        return tmGroupByBeans;
    }
    
    // This method returns an ArrayList of TargetMarket objects that are pending this
    // users approval
    public static ArrayList getTargetMarketsPendingApproval(User user, Connection DBConn) throws Exception {
        ArrayList plans = new ArrayList();
        // District Managers and Channel Marketing Managers are the only users approving
        // Target Market plans + Also some selected Sales Engineers can also approve Target Market plans.
        
        if (!user.isDistrictManager() && !user.isChannelMarketingManager() && !user.isSalesEngineer()){
            return plans;
        }
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
        	boolean isSalesEngineer = false;
            if (user.isDistrictManager()){
                pstmt = DBConn.prepareStatement(getPendingDMApprovalsQuery);
                pstmt.setString(1,"B");
                pstmt.setString(2,user.getGeography() + "%");
            } else if (user.isChannelMarketingManager()){
                pstmt = DBConn.prepareStatement(getPendingCMMApprovalsQuery);
                pstmt.setString(1,"D");
            }
            else if(user.isSalesEngineer()){
            	isSalesEngineer = true;
                pstmt = DBConn.prepareStatement(getPendingSEApprovalsQuery);
                pstmt.setString(1,"S");//Initial Account
                pstmt.setString(2,user.getGeography() + "%");
            }
            rs = pstmt.executeQuery();
            
            if(isSalesEngineer){

            	whileLoopLabel:
                while (rs.next()){
                
                	TargetMarket tm = TargetMarketDAO.getTargetMarket(rs.getInt("target_market_plan_id"),DBConn);
                    ArrayList targetMarketPlanAccounts = (tm.getPlanAccounts() != null) ? tm.getPlanAccounts() : new ArrayList();

                    int targetMarketPlanAccountsSize = targetMarketPlanAccounts.size();
                    for(int targetMarketPlanAccountsIndex=0; targetMarketPlanAccountsIndex < targetMarketPlanAccountsSize; targetMarketPlanAccountsIndex++){
                    	
                    	String currentTargetMarketPlanAccount = (String)targetMarketPlanAccounts.get(targetMarketPlanAccountsIndex);
                    	Account currentAccount = AccountDAO.getAccount(currentTargetMarketPlanAccount, DBConn);
                    	if(user.ableToSee(currentAccount)){
                    		plans.add(tm);
                    		continue whileLoopLabel;
                    	}	
                    }
                }
            }else{
                while (rs.next()){
                    TargetMarket tm = TargetMarketDAO.getTargetMarket(rs.getInt("target_market_plan_id"),DBConn);
                    plans.add(tm);
                }
            }
        } catch (Exception e) {
        	throw new SMRCException("TargetMarketDAO.getTargetMarketsPendingApproval(): " + e);
        } finally {
        	SMRCConnectionPoolUtils.close(rs);
        	SMRCConnectionPoolUtils.close(pstmt);
        }
        
        return plans;
    }
    
    public static String getTargetMarketPlanDescription (int tmId, Connection DBConn) throws Exception {
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	String tmpDescription = "";
    	try {
    		pstmt = DBConn.prepareStatement(getTargetMarketPlanDescription);
    		pstmt.setInt(1,tmId);
    		rs = pstmt.executeQuery();
    		while (rs.next()){
    			tmpDescription = rs.getString("plan_description");
    		}
    	} finally {
    		SMRCConnectionPoolUtils.close(rs);
    		SMRCConnectionPoolUtils.close(pstmt);
    	}
    	
    	return tmpDescription;
    	
    }
    
    public static void updateTargetMarketStatus (int targetMarketPlanId, String targetMarketStatus, Connection DBConn) throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs  = null;
        
        try {
            pstmt = DBConn.prepareStatement(updateTargetMarketStatus);
            int codeTypeId = CodeTypeDAO.getCodeTypeId(targetMarketStatus, DBConn);
            
            pstmt.setInt(1, codeTypeId);
            pstmt.setInt(2, targetMarketPlanId);
            
            pstmt.executeUpdate();

        }catch (Exception e)	{
        	throw new SMRCException("TargetMarketDAO.updateTargetMarketStatus(): ", e);
        }
        finally {
        	SMRCConnectionPoolUtils.close(rs);
        	SMRCConnectionPoolUtils.close(pstmt);
        }
    }
    
}
