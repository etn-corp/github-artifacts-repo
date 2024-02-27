package com.eaton.electrical.smrc.dao;

import java.sql.*;
import java.util.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

/**
 * @author E0062708
 *  
 */
public class ProductDAO {

    private static final String getSummaryProductLinesQuery = "SELECT * FROM PRODUCTS WHERE SP_LOAD_TOTAL='S' AND CURRENT_FLAG='Y' ORDER BY SEQ_NUM";
    private static final String getSummaryProductLinesQueryYYYY = "SELECT * FROM PRODUCTS WHERE PERIOD_YYYY=? AND SP_LOAD_TOTAL='S' ORDER BY SEQ_NUM";
//    private static final String getSublinesQuery = "SELECT * FROM PRODUCTS WHERE SUMMARY_PRODUCT_ID=? AND CURRENT_FLAG='Y' ORDER BY SEQ_NUM";
//    private static final String getSublinesQueryYYYY = "SELECT * FROM PRODUCTS WHERE SUMMARY_PRODUCT_ID=? AND PERIOD_YYYY=? ORDER BY SEQ_NUM";
    private static final String getProductInfo = "SELECT * FROM PRODUCTS WHERE PRODUCT_ID=? and PERIOD_YYYY=? ORDER BY SEQ_NUM";
    private static final String getProductsPeriodQuery = "SELECT DISTINCT PERIOD_YYYY FROM DISTRIB_PRD_COMPETITORS WHERE DISTRIBUTOR_ID=?";
    private static final String getProductsAlphabeticallyQuery = "SELECT * from products where period_yyyy = ? order by product_description";
    private static final String getProductsWithDivisionQuery = "SELECT p.product_id, p.product_description,(select d.division_description from divisions d where d.division_id = (select t.division_id from product_line_division t where t.period_yyyy = ? and p.product_id = t.product_id) )division from products p where p.period_yyyy = ? order by product_id";
    private static final String getProductsBySequenceQuery = "SELECT * from products where period_yyyy = ? order by seq_num";
    private static final String getDivisionForProductIdQuery = "select division_id from product_line_division where product_id = ? and period_yyyy = ?";

    //Vince: Junk not yet baked. TODO
    //private static final String getProductsQueryYYYY = "Vince: Not yet implemented properly.  See temp method below";

    public static ArrayList getSummaryProductLines(int period, Connection DBConn)
            throws Exception {

        ArrayList products = new ArrayList();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            if (period == 0) {
                SMRCLogger.debug("SQL - ProductDAO.getSummaryProductLines():\n"
                        + getSummaryProductLinesQuery);
                pstmt = DBConn.prepareStatement(getSummaryProductLinesQuery);
            } else {
                SMRCLogger.debug("SQL - ProductDAO.getSummaryProductLines():\n"
                        + getSummaryProductLinesQueryYYYY + "\nPeriod="
                        + period);
                pstmt = DBConn.prepareStatement(getSummaryProductLinesQueryYYYY);
                pstmt.setInt(1, period);
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();

                product.setId(StringManipulation.noNull(rs.getString("PRODUCT_ID")));
                product.setDescription(StringManipulation.noNull(rs.getString("PRODUCT_DESCRIPTION")));
                product.setPeriodYYYY(rs.getInt("PERIOD_YYYY"));
                product.setSpLoadTotal(rs.getString("sp_load_total"));
                product.setSummaryProductId(rs.getString("summary_product_id"));
                product.setGrandTotalId1(rs.getString("grand_total_line1"));
                product.setGrandTotalId2(rs.getString("grand_total_line2"));
//                product = getSublines(product, period, DBConn);
                products.add(product);

            }
        } catch (Exception e) {
            SMRCLogger.error("ProductDAO.getSummaryProductLines(): ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(pstmt);
        }

        return products;
    }

/*    public static Product getSublines(Product product, int period,
            Connection DBConn) throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            if (period == 0) {
                pstmt = DBConn.prepareStatement(getSublinesQuery);
                pstmt.setString(1, product.getId());
            } else {
                pstmt = DBConn.prepareStatement(getSublinesQueryYYYY);
                pstmt.setString(1, product.getId());
                pstmt.setInt(2, period);
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                ProductSubline productSubline = new ProductSubline();

                productSubline.setId(StringManipulation.noNull(rs.getString("PRODUCT_ID")));
                productSubline.setDescription(StringManipulation.noNull(rs.getString("PRODUCT_DESCRIPTION")));

                product.addSubline(productSubline);

            }

        } catch (Exception e) {
            SMRCLogger.error("ProductDAO.getSublines(): ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(pstmt);
        }

        return product;
    }
*/
    public static Product productLookup(String productId, int year,
            Connection DBConn) throws Exception {
        Product product = new Product();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            pstmt = DBConn.prepareStatement(getProductInfo);
            pstmt.setString(1, productId);
            pstmt.setInt(2, year);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                product.setId(StringManipulation.noNull(rs.getString("PRODUCT_ID")));
                product.setDescription(StringManipulation.noNull(rs.getString("PRODUCT_DESCRIPTION")));
                product.setPeriodYYYY(rs.getInt("PERIOD_YYYY"));
                product.setSpLoadTotal(StringManipulation.noNull(rs.getString("sp_load_total")));
                product.setSummaryProductId(rs.getString("summary_product_id"));
                product.setGrandTotalId1(rs.getString("grand_total_line1"));
                product.setGrandTotalId2(rs.getString("grand_total_line2"));
                Division division = DivisionDAO.getDivision(getDivisionForProduct(rs.getString("PRODUCT_ID"),rs.getInt("PERIOD_YYYY"),DBConn),DBConn);
                product.setDivision(division);
            }

        } catch (Exception e) {
            SMRCLogger.error("ProductDAO.productLookup(): ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(pstmt);
        }
        return product;

    }

    public static int getProductsPeriod(int distId, Connection DBConn)
            throws Exception {
        int year = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            SMRCLogger.debug("SQL - ProductDAO.getProductsPeriod():\n"
                    + getProductsPeriodQuery);
            pstmt = DBConn.prepareStatement(getProductsPeriodQuery);
            pstmt.setInt(1, distId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                year = rs.getInt(1);
            }

        } catch (Exception e) {
            SMRCLogger.error("ProductDAO.getProductsPeriod(): ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(pstmt);
        }
        return year;

    }

    /**
     * This method returns all products in Product objects for the current year
     * 
     * @param DBConn the database connection
     * @return an ArrayList of products sorted by Product_Description
     * @throws Exception
     */
    public static ArrayList getProductsAlphabetically(int srYear, Connection DBConn)
            throws Exception {
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList productList = new ArrayList();
  //      Calendar cal = Calendar.getInstance();
  //      java.util.Date today = new java.util.Date();
  //      cal.setTime(today);
  //      int year = cal.get(Calendar.YEAR);

        try {
            pstmt = DBConn.prepareStatement(getProductsAlphabeticallyQuery);
            pstmt.setInt(1, srYear);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getString("product_id"));
                product.setDescription(rs.getString("product_description"));
                product.setSpLoadTotal(rs.getString("sp_load_total"));
                product.setSummaryProductId(rs.getString("summary_product_id"));
                product.setGrandTotalId1(rs.getString("grand_total_line1"));
                product.setGrandTotalId2(rs.getString("grand_total_line2"));
                productList.add(product);
            }

        } catch (Exception e) {
            SMRCLogger.error("ProductDAO.getProductsAlphabetically(): ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(pstmt);
        }

        return productList;
    }

    public static ArrayList getProductsWithDivision(int srYear, Connection DBConn)throws Exception {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		ArrayList productList = new ArrayList();
		//      Calendar cal = Calendar.getInstance();
		//      java.util.Date today = new java.util.Date();
		//      cal.setTime(today);
		//      int year = cal.get(Calendar.YEAR);
		//srYear = 2005;
		try {
		    pstmt = DBConn.prepareStatement(getProductsWithDivisionQuery);
		    pstmt.setInt(1, srYear);
		    pstmt.setInt(2, srYear);
		    rs = pstmt.executeQuery();
		
		    while (rs.next()) {
		        Product product = new Product();
		        Division d = new Division();
		        product.setId(rs.getString("product_id"));
		        product.setDescription(rs.getString("product_description"));
		        d.setName(rs.getString("division"));
		        product.setDivision(d);
		        productList.add(product);
		    }
		
		} catch (Exception e) {
		    SMRCLogger.error("ProductDAO.getProductsWithDivision(): ", e);
		    throw e;
		} finally {
		    SMRCConnectionPoolUtils.close(rs);
		    SMRCConnectionPoolUtils.close(pstmt);
		}

		return productList;
    }
    /**
     * This method returns all products in Product objects for the current year
     * 
     * @param DBConn the database connection
     * @return an ArrayList of products sorted by seq_num
     * @throws Exception
     */
    public static ArrayList getProductsBySequence(Connection DBConn)
            throws Exception {
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList productList = new ArrayList();
        Calendar cal = Calendar.getInstance();
        java.util.Date today = new java.util.Date();
        cal.setTime(today);
        int year = cal.get(Calendar.YEAR);

        try {
            pstmt = DBConn.prepareStatement(getProductsBySequenceQuery);
            pstmt.setInt(1, year);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getString("product_id"));
                product.setDescription(rs.getString("product_description"));
                product.setSpLoadTotal(rs.getString("sp_load_total"));
                product.setSummaryProductId(rs.getString("summary_product_id"));
                product.setGrandTotalId1(rs.getString("grand_total_line1"));
                product.setGrandTotalId2(rs.getString("grand_total_line2"));
                productList.add(product);
            }

        } catch (Exception e) {
            SMRCLogger.error("ProductDAO.getProductsAlphabetically(): ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(pstmt);
        }

        return productList;
    }

    //Vince: should use getProductsQueryYYYY in the future
    public static ArrayList getProductsCurrentYear(int srYear, Connection DBConn)
            throws Exception {

        Statement stmtProducts = null;
        ResultSet resProducts = null;


        StringBuffer querySB = null;
        ArrayList products = new ArrayList(30);
     //   int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        
        //top-down flow control. If error gets set to true, we leave the
        // method.
        boolean error = false;

        try {
            /*
             * SELECT product_id, product_description FROM products WHERE
             * current_flag='Y' AND period_yyyy = int1 ORDER BY seq_num ASC
             */
            querySB = new StringBuffer(250);
            querySB.append("SELECT * ");
            querySB.append("FROM products ");
    //        querySB.append("WHERE current_flag='Y' ");
            querySB.append("Where period_yyyy = ");
            querySB.append("'").append(srYear).append("' "); //int1
            querySB.append("ORDER BY seq_num ASC");
            
            
            SMRCLogger.debug("ProductDAO.getProductsCurrentYear() - SQL\n" + querySB);
            stmtProducts = DBConn.createStatement();
            resProducts = stmtProducts.executeQuery(querySB.toString());

            while (resProducts.next()) {
                Product product = new Product();

                product.setId(resProducts.getString("product_id"));
                product.setDescription(resProducts.getString("product_description"));
                product.setSpLoadTotal(resProducts.getString("sp_load_total"));
                product.setSummaryProductId(resProducts.getString("summary_product_id"));
                product.setGrandTotalId1(resProducts.getString("grand_total_line1"));
                product.setGrandTotalId2(resProducts.getString("grand_total_line2"));

/*                  Braffet : 20060326 - There have not been sublines for a while  
                *
                *                try {
                    /*
                     * SELECT * FROM product_subline WHERE product_id = 'str1'
                     * AND period_yyyy = int1 AND prod_subline_id = '12' ORDER
                     * BY seq_num ASC
                     *  
                     
                    querySB = new StringBuffer(250);
                    querySB.append("SELECT * FROM product_subline ");
                    querySB.append("WHERE product_id = ");
                    querySB.append("'").append(
                            resProducts.getString("product_id")).append("' "); //'str1'
                    querySB.append("AND period_yyyy = " + srYear + " ");
           //         querySB.append(currentYear).append(" "); //int1
                    querySB.append("AND prod_subline_id = '12' ");
                    querySB.append("ORDER BY seq_num ASC");

 
  					stmtProductSubline = DBConn.createStatement();
                    resProductSubline = stmtProductSubline.executeQuery(querySB.toString());

                    while (resProductSubline.next()) {
                        ProductSubline productSubline = new ProductSubline();

                        productSubline.setId(resProductSubline.getString("prod_subline_id"));
                        productSubline.setDescription(resProductSubline.getString("description"));
                        productSubline.setSeqNum(resProductSubline.getInt("seq_num"));

                        product.addSubline(productSubline);
                    
                } catch (Exception e) {
                    SMRCLogger.error("OEMAcctPlan Servlet.getProducts(): ", e);
                    throw e;
                } finally {
                    SMRCConnectionPoolUtils.close(resProductSubline);
 //                   SMRCConnectionPoolUtils.close(stmtProductSubline);
                }
}*/
                products.add(product);

            } //while (resProducts.next())

        } catch (Exception e) {
            SMRCLogger.error("ProductDAO.getProductsCurrentYear(): ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(resProducts);
            SMRCConnectionPoolUtils.close(stmtProducts);
        }
        return error ? null : products;

    } //method
    
    // This method returns the division name for a specified product id and sales reporting year
    public static String getDivisionForProduct(String productId, int srYear, Connection DBConn) throws Exception {
        String divId = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = DBConn.prepareStatement(getDivisionForProductIdQuery);
            pstmt.setInt(1,Globals.a2int(productId));
            pstmt.setInt(2,srYear);
            rs = pstmt.executeQuery();
            while (rs.next()){
                divId = rs.getString("division_id");
            }
            
        } catch (Exception e) {
            SMRCLogger.error("ProductDAO.getDivisionForProduct():: " + e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(pstmt);
        }
        
        return divId;
    }

}