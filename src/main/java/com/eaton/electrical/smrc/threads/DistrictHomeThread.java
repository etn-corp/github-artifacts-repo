package com.eaton.electrical.smrc.threads;

import java.sql.*;
import java.util.*;

import com.eaton.electrical.smrc.dao.*;

/**
 * @author E0062708
 *
 */
public class DistrictHomeThread extends Thread {
    
    private String district = null;
    private String salesOrders = null;
    private int srYear;
    private int srMonth;
    private Connection DBConn = null;
    private ArrayList returnResults = null;
    private String query;
    
    public DistrictHomeThread(String district, String salesOrders, int srYear, int srMonth, Connection DBConn, String query) {
        this.district = district;
        this.salesOrders = salesOrders;
        this.srYear = srYear;
        this.srMonth = srMonth;
        this.DBConn = DBConn;
        this.query = query;
    }

    public void run(){
        try {
            if(query.equals("district")) {
                returnResults = DistrictDAO.getYTDandMTDSalesForDistrict(district, salesOrders, srYear, srMonth, DBConn);
            }else if(query.equals("target")) {
                returnResults = DistrictDAO.getYTDSalesForDistrictTarget(district, salesOrders, srYear, srMonth, DBConn);
            }else if(query.equals("division")) {
                returnResults = DistrictDAO.getYTDSalesForDistrictDivisionTargets(district, salesOrders, srYear, srMonth, DBConn);
            }else if(query.equals("segments")) {
                returnResults = DistrictDAO.getYTDSalesForDistrictSegments(district, salesOrders, srYear, srMonth, DBConn);
            }
        } catch (Exception e) {
            // TODO figure out what to do here.  run() doesnt throw an exception....
            e.printStackTrace();
        }

    }

    public ArrayList getResults() {
        return returnResults;
    }
    
}
