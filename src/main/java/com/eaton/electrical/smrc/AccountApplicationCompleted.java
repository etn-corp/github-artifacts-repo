package com.eaton.electrical.smrc;

import java.sql.Connection;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.service.SMRCConnectionPoolUtils;
import com.eaton.electrical.smrc.service.SMRCLogger;


public class AccountApplicationCompleted {
	
	public String AccountCompleted(String acctId) {
		String returnVal = "F";
		Connection DBConn = null;
		
		try {
			DBConn = SMRCConnectionPoolUtils.getDatabaseConnection();

			Account a = AccountDAO.getAccount(acctId, DBConn);
			
			if(a.getFax() == null || a.getFax().equals("")) {
				return returnVal;
			}
			//System.out.println("PJHONE= " + a.getPhone());
			//Thread.sleep(2000);
			if(a.getPhone() == null || a.getPhone().equals("")) {
				return returnVal;
			}
			
			Distributor d = DistributorDAO.getDistributor(acctId, DBConn);
			
			if(d.getFederalTaxId() == null || d.getFederalTaxId().equals("")){
				return returnVal;
			}
			if(d.getDunnBradStreet() == null || d.getDunnBradStreet().equals("")) {
				return returnVal;
			}
			if(d.getLocationType() == 0) {
				return returnVal;
			}
			if(d.getCustomerCategory() == 0) {
				return returnVal;
			}
			if(d.getFormOfOwnership() == 0) {
				return returnVal;
			}
			if(d.getPrimaryBusinessActivity() == 0) {
				return returnVal;
			}			
			if(d.getFacilities() == null) {
				return returnVal;
			}
			if(d.getProjectedEatonSalesYr1()== 0) {
				return returnVal;
			}
			if(d.getProjectedEatonSalesYr2() == 0) {
				return returnVal;
			}
			if(d.getProjectedVScompYr1() == 0) {
				return returnVal;
			}
			if(d.getProjectedVScompYr2() == 0) {
				return returnVal;
			}
			if(d.getElectricalLines() == null) {
				return returnVal;
			}
			
		} catch(Exception e) {
			SMRCLogger.warn(this.getClass().getName() + "Distributor TRY Block " + e);
		} finally {
			SMRCConnectionPoolUtils.close(DBConn);
		}
		
		returnVal = "Y";
		return returnVal;
	}
	

}
