
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
public class DistributorDAO {
	private static final String doesDistExist = "SELECT COUNT(*) FROM DISTRIBUTORS WHERE VISTA_CUSTOMER_NUMBER=?";
	private static final String initializeDistributorInsert = "INSERT INTO DISTRIBUTORS (DISTRIBUTOR_ID,VISTA_CUSTOMER_NUMBER)"+ 
		" VALUES(distributors_seq.nextval,?)";
	private static final String saveDistributorUpdate1 = "UPDATE DISTRIBUTORS SET FEDERAL_TAX_ID=?,DUNN_BRADSTREET=?,CUSTOMER_CATEGORY=?,LOCATION_TYPE_ID=?,CHAIN_NAME=?,APPLYING_FOR_TYPE_ID=?,APPLYING_FOR_OTHER_NOTES=?,PREVIOUS_NAME=?,PREVIOUS_VISTA_CUSTOMER_NUMBER=?,OWNERSHIP_FORM_TYPE_ID=?,OWNERSHIP_FORM_NOTES=?,PRIMARY_BUS_ACTIVITY_TYPE_ID=?,FACILITY_AREA=?,COMMITMENT_PROGRAM=?,PROJECTED_EATON_SALES_YEAR1=?,PROJECTED_EATON_SALES_YEAR2=?,PROJECTED_SALES_VS_COMP_Y1=?,PROJECTED_SALES_VS_COMP_Y2=?,DISTRIBUTOR_NOTES=?,OTHER_CUSTOMER_SEGMENT=?,OTHER_CUSTOMER_SEGMENT_VALUE=?,COMMITMENT_REASON=? WHERE VISTA_CUSTOMER_NUMBER=?";
	private static final String saveDistributorUpdate2 = "UPDATE DISTRIBUTORS SET LOCATION_TYPE_ID=?,"+
		"CHAIN_NAME=?,NUM_OF_BRANCH_LOCATIONS=?,NUM_OF_YEARS_AT_LOCATION=?,FACILITY_AREA=?"+
		" WHERE VISTA_CUSTOMER_NUMBER=?";
	private static final String saveDistributorUpdate3 = "UPDATE DISTRIBUTORS SET INSIDE_SALES_CONSTR_PERSONNEL=?,"+
		"INSIDE_SALES_INDSTR_PERSONNEL=?,INSIDE_SALES_GEN_PERSONNEL=?,OUTSIDE_SALES_CONSTR_PERSONNEL=?,OUTSIDE_SALES_INDSTR_PERSONNEL=?,"+
		"OUTSIDE_SALES_GEN_PERSONNEL=?,MANAGEMENT_PERSONNEL=?,COUNTER_SALES_PERSONNEL=?,SPECIALIST_PERSONNEL=?,"+
		"ELECTRICAL_ENGINEER_PERSONNEL=?,WHSE_DRIVERS_PERSONNEL=?,ADMIN_PERSONNEL=?"+
		" WHERE VISTA_CUSTOMER_NUMBER=?";
	private static final String saveDistributorUpdate4 = "UPDATE DISTRIBUTORS SET OTHER_CUSTOMER_SEGMENT=?,"+
	"OTHER_CUSTOMER_SEGMENT_VALUE=? WHERE VISTA_CUSTOMER_NUMBER=?";	
	private static final String saveDistributorUpdate5="UPDATE DISTRIBUTORS SET CURRENT_YEAR_SALES_ESTIMATE=?, PRIOR_YEAR_ACTUAL_SALES=?,PRIOR_2_YEAR_ACTUAL_SALES=?,PRIOR_3_YEARS_TOTAL_SALES=?,APPROXIMATE_INVENTORY=?," +
		"PROJECTED_EATON_SALES_YEAR1=?,PROJECTED_EATON_SALES_YEAR2=?,NAED_PARTICIPATION=?,TRADE_ASSOCIATIONS=?,DISTRIBUTOR_NOTES=? WHERE VISTA_CUSTOMER_NUMBER=?";	
	private static final String getDistributorQuery = "SELECT * FROM DISTRIBUTORS WHERE VISTA_CUSTOMER_NUMBER=?";
	private static final String distributorSegmentsDelete = "DELETE FROM DISTRIB_SEGMENTATION WHERE DISTRIBUTOR_ID=?";
	private static final String distributorSegmentsInsert = "INSERT INTO DISTRIB_SEGMENTATION (DISTRIBUTOR_ID,SEGMENTATION_TYPE_ID,SALES_PERCENTAGE)"+
		" VALUES(?,?,?)";
	private static final String facilitiesOtherInsert = "INSERT INTO DISTRIB_FACILITIES (DISTRIBUTOR_ID,FACILITY_TYPE_ID,OTHER_NOTES) VALUES(?,23,?)";
	private static final String facilitiesOtherDelete = "DELETE FROM DISTRIB_FACILITIES WHERE DISTRIBUTOR_ID=? AND FACILITY_TYPE_ID = 23";
	private static final String getFacilitiesOtherQuery = "SELECT OTHER_NOTES FROM DISTRIB_FACILITIES WHERE DISTRIBUTOR_ID=? AND FACILITY_TYPE_ID=23";
	private static final String servicesOtherInsert = "INSERT INTO DISTRIB_SERVICES (DISTRIBUTOR_ID,SERVICE_TYPE_ID,NOTES) VALUES(?,34,?)";
	private static final String servicesOtherDelete = "DELETE FROM DISTRIB_SERVICES WHERE DISTRIBUTOR_ID=? AND SERVICE_TYPE_ID = 34";
	private static final String getServicesOtherQuery = "SELECT NOTES FROM DISTRIB_SERVICES WHERE DISTRIBUTOR_ID=? AND SERVICE_TYPE_ID=34";
	private static final String buyingGroupOtherInsert = "INSERT INTO DISTRIB_BUYING_GROUPS (DISTRIBUTOR_ID,BUYING_GROUP_TYPE_ID,NOTES) VALUES(?,54,?)";
	private static final String buyingGroupOtherDelete = "DELETE FROM DISTRIB_BUYING_GROUPS WHERE DISTRIBUTOR_ID=? AND BUYING_GROUP_TYPE_ID = 54";
	private static final String getBuyingGroupOtherQuery = "SELECT NOTES FROM DISTRIB_BUYING_GROUPS WHERE DISTRIBUTOR_ID=? AND BUYING_GROUP_TYPE_ID=54";
	private static final String getCountySalesQuery = "SELECT * FROM DISTRIB_COUNTY_SALES WHERE DISTRIBUTOR_ID=?";
	private static final String countySalesDelete = "DELETE FROM DISTRIB_COUNTY_SALES WHERE DISTRIBUTOR_ID=?";
	private static final String countySalesInsert = "INSERT INTO DISTRIB_COUNTY_SALES (DISTRIBUTOR_ID,COUNTY_ID,PERCENT_SOLD_THRU_STOCK)"+
		" VALUES(?,?,?)";
	private static final String getDistributorSegmentsQuery = "SELECT SALES_PERCENTAGE FROM DISTRIB_SEGMENTATION WHERE DISTRIBUTOR_ID=? AND SEGMENTATION_TYPE_ID=?";
	private static final String setDistributorProductsInsert = "INSERT INTO DISTRIB_PRD_COMPETITORS (DISTRIBUTOR_ID,PRODUCT_ID,PERIOD_YYYY,"+
		"TOTAL_SALES,PERCENT_STOCK_SALE,PRI_CURRENT_COMPETITOR_ID,SEC_CURRENT_COMPETITOR_ID,OTHER_CURRENT_COMPETITOR_ID,"+
		"PRI_PROPOSED_COMPETITOR_ID,SEC_PROPOSED_COMPETITOR_ID,OTHER_PROPOSED_COMPETITOR_ID)" +
		" VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	private static final String setDistributorProductsDelete = "DELETE FROM DISTRIB_PRD_COMPETITORS WHERE DISTRIBUTOR_ID=?";
	private static final String getDistributorProductsQuery = "SELECT * FROM DISTRIB_PRD_COMPETITORS WHERE DISTRIBUTOR_ID=?";
	private static final String saveKeyAccountInsert = "INSERT INTO DISTRIB_KEY_ACCOUNTS (DISTRIBUTOR_ID,VISTA_CUSTOMER_NUMBER,DATE_CHANGED) VALUES (?,?,SYSDATE)";
	private static final String saveDistributorsImpactedInsert = "INSERT INTO IMPACTED_DISTRIBUTORS (DISTRIBUTOR_ID,VISTA_CUSTOMER_NUMBER,DATE_CHANGED) VALUES (?,?,SYSDATE)";
	private static final String deleteKeyAccount = "DELETE FROM DISTRIB_PRODUCT_OPP WHERE DISTRIBUTOR_ID=? AND VISTA_CUSTOMER_NUMBER=?";
	private static final String deleteKeyAccount2 = "DELETE FROM DISTRIB_KEY_ACCOUNTS WHERE DISTRIBUTOR_ID=? AND VISTA_CUSTOMER_NUMBER=?";
	private static final String updateKeyAccount = "UPDATE DISTRIB_KEY_ACCOUNTS SET POTENTIAL_EATON_DOLLARS=?,DATE_CHANGED=SYSDATE WHERE DISTRIBUTOR_ID=? AND VISTA_CUSTOMER_NUMBER=?";
	private static final String deleteDistribProductOpp = "DELETE FROM DISTRIB_PRODUCT_OPP WHERE DISTRIBUTOR_ID=? AND VISTA_CUSTOMER_NUMBER=?";
	private static final String insertDistribProductOpp = "INSERT INTO DISTRIB_PRODUCT_OPP (DISTRIBUTOR_ID,VISTA_CUSTOMER_NUMBER,PRODUCT_ID,DATE_CHANGED) VALUES(?,?,?,SYSDATE)";
	private static final String getKeyAccountsQuery = "select a.VISTA_CUSTOMER_NUMBER,b.CUSTOMER_NAME,a.POTENTIAL_EATON_DOLLARS FROM DISTRIB_KEY_ACCOUNTS a,CUSTOMER b"+
		" WHERE DISTRIBUTOR_ID=? AND b.VISTA_CUSTOMER_NUMBER = a.VISTA_CUSTOMER_NUMBER ORDER BY b.CUSTOMER_NAME";
	private static final String getKeyAccountProductIdsQuery = "SELECT PRODUCT_ID FROM DISTRIB_PRODUCT_OPP WHERE DISTRIBUTOR_ID=? AND VISTA_CUSTOMER_NUMBER=?";
	private static final String getSupplierProductsQuery = "SELECT * FROM SUPPLIER_PRODUCTS ORDER BY PRODUCT_NAME";
	private static final String getSelectedSupplierProductsQuery = "SELECT * FROM DISTRIB_SUPPLIERS WHERE DISTRIBUTOR_ID=?";
	private static final String deleteBuyingGroups = "DELETE FROM DISTRIB_BUYING_GROUPS WHERE DISTRIBUTOR_ID=?";
	private static final String insertBuyingGroup = "INSERT INTO DISTRIB_BUYING_GROUPS (DISTRIBUTOR_ID,BUYING_GROUP_TYPE_ID) VALUES(?,?)";	
	private static final String deleteSupplierProducts = "DELETE FROM DISTRIB_SUPPLIERS WHERE DISTRIBUTOR_ID=?";
	private static final String insertSupplierProduct = "INSERT INTO DISTRIB_SUPPLIERS (DISTRIBUTOR_ID,SUPPLIER_PRODUCT_ID,SUPPLIER_ID) VALUES(?,?,?)";	
	private static final String updateComProgram = "UPDATE DISTRIBUTORS d SET d.COMMITMENT_PROGRAM=?, d.USER_CHANGED=?, DATE_CHANGED=SYSDATE WHERE d.vista_customer_number=?";
	
	public static Distributor getDistributor(String acctId, Connection DBConn) throws Exception{
		Distributor dist = new Distributor();		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DBConn.prepareStatement(getDistributorQuery);
			
			pstmt.setString(1, acctId);
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				dist.setId(rs.getInt("DISTRIBUTOR_ID"));
				dist.setVcn(rs.getString("VISTA_CUSTOMER_NUMBER"));

				dist.setFederalTaxId(StringManipulation.noNull(rs.getString("FEDERAL_TAX_ID")));
				dist.setDunnBradStreet(StringManipulation.noNull(rs.getString("DUNN_BRADSTREET")));
				dist.setCustomerCategory(rs.getInt("CUSTOMER_CATEGORY"));
				dist.setApplyingFor(rs.getInt("APPLYING_FOR_TYPE_ID"));
				dist.setApplyingForOtherNotes(StringManipulation.noNull(rs.getString("APPLYING_FOR_OTHER_NOTES")));
				dist.setPreviousName(StringManipulation.noNull(rs.getString("PREVIOUS_NAME")));
				dist.setPreviousVistaCustNumber(rs.getString("PREVIOUS_VISTA_CUSTOMER_NUMBER"));
				dist.setFormOfOwnership(rs.getInt("OWNERSHIP_FORM_TYPE_ID"));
				dist.setFormOfOwnershipNotes(StringManipulation.noNull(rs.getString("OWNERSHIP_FORM_NOTES")));
				dist.setParentCompany(StringManipulation.noNull(rs.getString("PARENT_COMPANY")));
				dist.setPrimaryBusinessActivity(rs.getInt("PRIMARY_BUS_ACTIVITY_TYPE_ID"));
				dist.setLocationType(rs.getInt("LOCATION_TYPE_ID"));
				dist.setChainName(StringManipulation.noNull(rs.getString("CHAIN_NAME")));
				dist.setNumOfBranches(rs.getInt("NUM_OF_BRANCH_LOCATIONS"));
				dist.setNumOfYrsAtLocation(rs.getInt("NUM_OF_YEARS_AT_LOCATION"));
				dist.setFacilityArea(StringManipulation.noNull(rs.getString("FACILITY_AREA")));
				dist.setCommitmentProgram(rs.getInt("COMMITMENT_PROGRAM"));
				dist.setCommitmentReason(StringManipulation.noNull(rs.getString("COMMITMENT_REASON")));
				dist.setFacilitiesOther(getFacilitiesOther(dist, DBConn));
				dist.setServicesOther(getServicesOther(dist, DBConn));
				dist.setFacilities(genericDistributorQuery(dist.getId(), "DISTRIB_FACILITIES", "FACILITY_TYPE_ID", DBConn));
				dist.setServices(genericDistributorQuery(dist.getId(), "DISTRIB_SERVICES", "SERVICE_TYPE_ID", DBConn));
				dist.setECommerce(genericDistributorQuery(dist.getId(), "DISTRIB_ECOMMERCE", "ECOMMERCE_TYPE_ID", DBConn));
				dist.setElectricalLines(genericDistributorQuery(dist.getId(),"DISTRIB_ELECTRIC_LINES","ELECTRICAL_LINE_ID", DBConn));
				
				dist.setInsideSalesConstrPersonnel(rs.getInt("INSIDE_SALES_CONSTR_PERSONNEL"));
				dist.setInsideSalesIndstrPersonnel(rs.getInt("INSIDE_SALES_INDSTR_PERSONNEL"));
				dist.setInsideSalesGenPersonnel(rs.getInt("INSIDE_SALES_GEN_PERSONNEL"));
				dist.setOutsideSalesConstrPersonnel(rs.getInt("OUTSIDE_SALES_CONSTR_PERSONNEL"));
				dist.setOutsideSalesIndstrPersonnel(rs.getInt("OUTSIDE_SALES_INDSTR_PERSONNEL"));
				dist.setOutsideSalesGenPersonnel(rs.getInt("OUTSIDE_SALES_GEN_PERSONNEL"));
				dist.setElectricalEngPersonnel(rs.getInt("ELECTRICAL_ENGINEER_PERSONNEL"));
				dist.setSpecialistPersonnel(rs.getInt("SPECIALIST_PERSONNEL"));
				dist.setCounterSalesPersonnel(rs.getInt("COUNTER_SALES_PERSONNEL"));
				dist.setWhseDriversPersonnel(rs.getInt("WHSE_DRIVERS_PERSONNEL"));
				dist.setManagementPersonnel(rs.getInt("MANAGEMENT_PERSONNEL"));
				dist.setAdminPersonnel(rs.getInt("ADMIN_PERSONNEL"));
				dist.setProducts(getDistributorProducts(dist, DBConn));
				dist.setCountySales(getCountySales(dist, DBConn));
				dist.setKeyCustomers(new ArrayList());
				dist.setSuppliersProducts(new ArrayList());
				dist.setPrior3YrsTotalSales(rs.getInt("PRIOR_3_YEARS_TOTAL_SALES"));
				dist.setCurrentYrSalesEstimate(rs.getInt("CURRENT_YEAR_SALES_ESTIMATE"));
				dist.setPriorYrActualSales(rs.getInt("PRIOR_YEAR_ACTUAL_SALES"));
				dist.setPrior2YrsActualSales(rs.getInt("PRIOR_2_YEAR_ACTUAL_SALES"));
				dist.setApproxInventory(rs.getInt("APPROXIMATE_INVENTORY"));
				dist.setProjectedEatonSalesYr1(rs.getInt("PROJECTED_EATON_SALES_YEAR1"));
				dist.setProjectedVScompYr1(rs.getInt("PROJECTED_SALES_VS_COMP_Y1"));
				dist.setProjectedEatonSalesYr2(rs.getInt("PROJECTED_EATON_SALES_YEAR2"));
				dist.setProjectedVScompYr2(rs.getInt("PROJECTED_SALES_VS_COMP_Y2"));
				dist.setNaedParticipation(StringManipulation.noNull(rs.getString("NAED_PARTICIPATION")));
				dist.setTradeAssociations(StringManipulation.noNull(rs.getString("TRADE_ASSOCIATIONS")));
				
				dist.setBuyingGroupAssn(genericDistributorQuery(dist.getId(), "DISTRIB_BUYING_GROUPS", "BUYING_GROUP_TYPE_ID", DBConn));
				dist.setBuyingGroupOther(getBuyingGroupOther(dist, DBConn));
				dist.setDistributorNotes(StringManipulation.noNull(rs.getString("DISTRIBUTOR_NOTES")));
				dist.setProductLoadingTypeId(rs.getInt("PRODUCT_LOADING_TYPE_ID"));
	 
				dist.setLoadModules(ProductModuleDAO.getSelectedModules(rs.getInt("DISTRIBUTOR_ID"),DBConn));
				dist.setOtherCustomerSegmentPercentage(rs.getInt("OTHER_CUSTOMER_SEGMENT"));
				dist.setOtherCustomerSegmentPercentageNote(StringManipulation.noNull(rs.getString("OTHER_CUSTOMER_SEGMENT_VALUE")));
				
			}
		}catch (Exception e)	{
			SMRCLogger.error("DistributorDAO.getDistributor(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
			
	return dist;
		
	}

	
	
	public static void saveDistributor(Distributor dist, int page, Connection DBConn) throws Exception{

		updateDistributor(dist,page,DBConn);
	}

	public static void initializeDistributor(String vcn, Connection DBConn) throws Exception{

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
		    pstmt = DBConn.prepareStatement(doesDistExist);
		    pstmt.setString(1,vcn);
		    rs = pstmt.executeQuery();
		    while(rs.next()) {
		        count = rs.getInt(1);
		        SMRCLogger.debug("DistributorDAO.initializeDistributor(): number of records: " + count);
		    }
			if(count==0) {
			    SMRCLogger.debug("DistributorDAO.initializeDistributor(): Distributor record doesnt exist, so creating it.");
				pstmt = DBConn.prepareStatement(initializeDistributorInsert);
				pstmt.setString(1,vcn);
				pstmt.executeUpdate();
			}

		}catch (Exception e)	{
			SMRCLogger.error("DistributorDAO.initializeDistributor(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
	}
	
	public static void updateCommtimentsProgram(int comID, String distID, String userID, ArrayList electricalLines, Connection DBConn) throws Exception{
		PreparedStatement pstmt = null;

		try {
			pstmt = DBConn.prepareStatement(updateComProgram);
			pstmt.setInt(1, comID);
			pstmt.setString(2, userID);
			pstmt.setString(3, distID);
			pstmt.executeUpdate();
			
			genericDistributorInsert(""+distID, electricalLines, "DISTRIB_ELECTRIC_LINES","ELECTRICAL_LINE_ID", DBConn);
			
		}catch(SQLException sqle) {
			throw sqle;			
		}catch (Exception e)	{
			SMRCLogger.error("DistributorDAO.updateComProgram((): ", e);
			throw e;
		}finally {
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
			SMRCConnectionPoolUtils.close(pstmt);
		}
	}
	
	private static void updateDistributor(Distributor dist,int page,Connection DBConn) throws Exception{

		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		PreparedStatement pstmt4 = null;
		PreparedStatement pstmt5 = null;

		try {
			int pIndex = 0 ;			

			if(page==1){
			    try {
					pstmt1 = DBConn.prepareStatement(saveDistributorUpdate1);
					
					pstmt1.setString( ++pIndex, dist.getFederalTaxId() ) ;
					pstmt1.setString(++pIndex, dist.getDunnBradStreet());
					pstmt1.setInt(++pIndex, dist.getCustomerCategory());
					pstmt1.setInt( ++pIndex, dist.getLocationType() ) ;
					pstmt1.setString( ++pIndex, dist.getChainName() ) ;
					pstmt1.setInt( ++pIndex, dist.getApplyingFor() ) ;
					pstmt1.setString( ++pIndex, dist.getApplyingForOtherNotes() ) ;
					pstmt1.setString( ++pIndex, dist.getPreviousName() ) ;
					pstmt1.setString( ++pIndex, dist.getPreviousVistaCustNumber() ) ;
					pstmt1.setInt( ++pIndex, dist.getFormOfOwnership() ) ;
					pstmt1.setString( ++pIndex, dist.getFormOfOwnershipNotes() ) ;
					pstmt1.setInt( ++pIndex, dist.getPrimaryBusinessActivity() ) ;
					pstmt1.setString( ++pIndex, dist.getFacilityArea() ) ;
					pstmt1.setInt(++pIndex, dist.getCommitmentProgram());
					pstmt1.setInt( ++pIndex, dist.getProjectedEatonSalesYr1());
					pstmt1.setInt( ++pIndex, dist.getProjectedEatonSalesYr2());
					pstmt1.setInt(++pIndex, dist.getProjectedVScompYr1());
					pstmt1.setInt(++pIndex, dist.getProjectedVScompYr2());
					pstmt1.setString( ++pIndex, dist.getDistributorNotes());
					pstmt1.setInt( ++pIndex, dist.getOtherCustomerSegmentPercentage());
					pstmt1.setString( ++pIndex, dist.getOtherCustomerSegmentPercentageNote());
					pstmt1.setString(++pIndex, dist.getCommitmentReason());
					pstmt1.setString( ++pIndex, dist.getVcn() ) ;
					pstmt1.executeUpdate();
					
					genericDistributorInsert(""+dist.getId(), dist.getFacilities(),"DISTRIB_FACILITIES","FACILITY_TYPE_ID", DBConn);
					
					if(!dist.getFacilitiesOther().equals("")){
						facilitiesOtherInsert(dist, DBConn);
					}	
					
					genericDistributorInsert(""+dist.getId(), dist.getElectricalLines(), "DISTRIB_ELECTRIC_LINES","ELECTRICAL_LINE_ID", DBConn);
					
					setCountySales(dist, DBConn);
					setDistributorSegments(dist, DBConn);
					setDistributorProducts(dist, DBConn);
					
					supplierProductUpdate(dist,DBConn);
					buyingGroupsUpdate(dist,DBConn);
					if(!dist.getBuyingGroupOther().equals("")){
						buyingGroupOtherInsert(dist, DBConn);
					}
			    }catch(SQLException sqle) {
			        if(sqle.getErrorCode()==2291) {
			            throw new Exception("The \"Previous Vista Number\" is invalid.<br><br>Please hit the back button and try again.", sqle);
			        }
			        throw sqle;
			    }
			}else if(page==2){
				
				
				pstmt2 = DBConn.prepareStatement(saveDistributorUpdate2);
				pstmt2.setInt( ++pIndex, dist.getLocationType() ) ;
				pstmt2.setString( ++pIndex, dist.getChainName() ) ;
				pstmt2.setInt( ++pIndex, dist.getNumOfBranches() ) ;
				pstmt2.setInt( ++pIndex, dist.getNumOfYrsAtLocation() ) ;
				pstmt2.setString( ++pIndex, dist.getFacilityArea() ) ;
				pstmt2.setString( ++pIndex, dist.getVcn() ) ;
				pstmt2.executeUpdate();
				
				genericDistributorInsert(""+dist.getId(), dist.getFacilities(),"DISTRIB_FACILITIES","FACILITY_TYPE_ID", DBConn);
				genericDistributorInsert(""+dist.getId(), dist.getServices(),"DISTRIB_SERVICES","SERVICE_TYPE_ID", DBConn);
				genericDistributorInsert(""+dist.getId(), dist.getECommerce(),"DISTRIB_ECOMMERCE","ECOMMERCE_TYPE_ID", DBConn);

				if(!dist.getFacilitiesOther().equals("")){
					facilitiesOtherInsert(dist, DBConn);
				}
				if(!dist.getServicesOther().equals("")){
					servicesOtherInsert(dist, DBConn);
				}

				
			}else if(page==3){
				pstmt3 = DBConn.prepareStatement(saveDistributorUpdate3);

				pstmt3.setInt( ++pIndex, dist.getInsideSalesConstrPersonnel());
				pstmt3.setInt( ++pIndex, dist.getInsideSalesGenPersonnel());
				pstmt3.setInt( ++pIndex, dist.getInsideSalesIndstrPersonnel());
				pstmt3.setInt( ++pIndex, dist.getOutsideSalesConstrPersonnel());
				pstmt3.setInt( ++pIndex, dist.getOutsideSalesGenPersonnel());
				pstmt3.setInt( ++pIndex, dist.getOutsideSalesIndstrPersonnel());
				pstmt3.setInt( ++pIndex, dist.getManagementPersonnel());
				pstmt3.setInt( ++pIndex, dist.getCounterSalesPersonnel());
				pstmt3.setInt( ++pIndex, dist.getSpecialistPersonnel());
				pstmt3.setInt( ++pIndex, dist.getElectricalEngPersonnel());
				pstmt3.setInt( ++pIndex, dist.getWhseDriversPersonnel());
				pstmt3.setInt( ++pIndex, dist.getAdminPersonnel());
				pstmt3.setString( ++pIndex, dist.getVcn());			
				pstmt3.executeUpdate();

			}else if(page==4){	

				pstmt4 = DBConn.prepareStatement(saveDistributorUpdate4);
				pstmt4.setInt( ++pIndex, dist.getOtherCustomerSegmentPercentage());
				pstmt4.setString( ++pIndex, dist.getOtherCustomerSegmentPercentageNote());
				pstmt4.setString( ++pIndex, dist.getVcn());
				pstmt4.executeUpdate();
				
				setCountySales(dist, DBConn);
				setDistributorSegments(dist, DBConn);
				setDistributorProducts(dist, DBConn);
				
				
				
			}else if(page==5){			
				
				pstmt5 = DBConn.prepareStatement(saveDistributorUpdate5);
				pstmt5.setInt( ++pIndex, dist.getCurrentYrSalesEstimate());
				pstmt5.setInt( ++pIndex, dist.getPriorYrActualSales());
				pstmt5.setInt( ++pIndex, dist.getPrior2YrsActualSales());
				pstmt5.setInt( ++pIndex, dist.getPrior3YrsTotalSales());
				pstmt5.setInt( ++pIndex, dist.getApproxInventory());
				pstmt5.setInt( ++pIndex, dist.getProjectedEatonSalesYr1());
				pstmt5.setInt( ++pIndex, dist.getProjectedEatonSalesYr2());
				pstmt5.setString( ++pIndex, dist.getNaedParticipation());
				pstmt5.setString( ++pIndex, dist.getTradeAssociations());
				pstmt5.setString( ++pIndex, dist.getDistributorNotes());
				pstmt5.setString( ++pIndex, dist.getVcn());
				pstmt5.executeUpdate();
				
				supplierProductUpdate(dist,DBConn);
				buyingGroupsUpdate(dist,DBConn);
				if(!dist.getBuyingGroupOther().equals("")){
					buyingGroupOtherInsert(dist, DBConn);
				}
			}

		}catch (Exception e)	{
			SMRCLogger.error("DistributorDAO.updateDistributor(): ", e);
			throw e;
		}finally {
			SMRCConnectionPoolUtils.close(pstmt1);
			SMRCConnectionPoolUtils.close(pstmt2);
			SMRCConnectionPoolUtils.close(pstmt3);
			SMRCConnectionPoolUtils.close(pstmt4);
			SMRCConnectionPoolUtils.close(pstmt5);
		}
	}

	private static void setDistributorSegments(Distributor dist, Connection DBConn) throws Exception{
	
		PreparedStatement pstmt = null;
		PreparedStatement pstmtI = null;

		ArrayList segments = dist.getSegments();
		try {
			pstmt = DBConn.prepareStatement(distributorSegmentsDelete);
			pstmt.setInt(1,dist.getId());
			pstmt.executeUpdate();
			
			pstmtI = DBConn.prepareStatement(distributorSegmentsInsert);
			
			for(int i=0;i<segments.size();i++){
				Segment segment = (Segment)segments.get(i);
				int pIndex = 0 ;
				pstmtI.setInt( ++pIndex,dist.getId());
				pstmtI.setInt( ++pIndex,segment.getSegmentId());
				pstmtI.setInt( ++pIndex,segment.getSalesPercentage());
				pstmtI.executeUpdate();
			}
			
	
		}catch (Exception e)	{
			SMRCLogger.error("DistributorDAO.setDistributorSegments(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
			SMRCConnectionPoolUtils.close(pstmtI);
		}
	
		
	
	}	
	

	private static void genericDistributorInsert(String distId,ArrayList things, String table, String column, Connection DBConn) throws Exception{
	
		PreparedStatement pstmt = null;
		PreparedStatement pstmtI = null;
		String genericDistributorInsert = "INSERT INTO " + table + "(DISTRIBUTOR_ID," + column+ ") VALUES(?,?)";
		String genericDistributorDelete = "DELETE FROM " + table + " WHERE DISTRIBUTOR_ID=?";
		

		try {
			pstmt = DBConn.prepareStatement(genericDistributorDelete);
			pstmt.setInt(1,Globals.a2int(distId));
			pstmt.executeUpdate();
			pstmtI = DBConn.prepareStatement(genericDistributorInsert);
			
			for(int i=0;i<things.size();i++){
				int pIndex = 0 ;
				pstmtI.setString( ++pIndex,distId);
				pstmtI.setInt( ++pIndex,Globals.a2int((String)things.get(i)));
				pstmtI.executeUpdate();
			}
			
	
		}catch (Exception e)	{
			SMRCLogger.error("DistributorDAO.genericDistributorInsert(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
			SMRCConnectionPoolUtils.close(pstmtI);
		}
	
		
	
	}	
	
	private static void facilitiesOtherInsert(Distributor dist, Connection DBConn) throws Exception{
			PreparedStatement pstmt = null;

			try {
				pstmt = DBConn.prepareStatement(facilitiesOtherDelete);
				pstmt.setInt(1,dist.getId());
				pstmt.executeUpdate();
				pstmt = DBConn.prepareStatement(facilitiesOtherInsert);
				int pIndex=0;
				pstmt.setInt( ++pIndex,dist.getId());
				pstmt.setString( ++pIndex,dist.getFacilitiesOther());
				pstmt.executeUpdate();
			}catch (Exception e)	{
				SMRCLogger.error("DistributorDAO.facilitiesOtherInsert(): ", e);
				throw e;
			}
			finally {
				SMRCConnectionPoolUtils.close(pstmt);
			}
		
		}		

	private static String getFacilitiesOther(Distributor dist, Connection DBConn) throws Exception{
		
			String note = "";
			PreparedStatement pstmt = null;
			
			ResultSet rs = null;
	
			try {

				pstmt = DBConn.prepareStatement(getFacilitiesOtherQuery);
				int pIndex=0;
				pstmt.setInt( ++pIndex,dist.getId());
				rs = pstmt.executeQuery();
				
				while(rs.next()){
					note = StringManipulation.noNull(rs.getString("OTHER_NOTES"));			
				}
	
		
			}catch (Exception e)	{
				SMRCLogger.error("DistributorDAO.getFacilitiesOther(): ", e);
				throw e;
			}
			finally {
				SMRCConnectionPoolUtils.close(rs);
				SMRCConnectionPoolUtils.close(pstmt);
			}
		
			return note;
		
		}	
	
	private static void servicesOtherInsert(Distributor dist, Connection DBConn) throws Exception{
			PreparedStatement pstmt = null;

			try {
				pstmt = DBConn.prepareStatement(servicesOtherDelete);
				pstmt.setInt(1,dist.getId());
				pstmt.executeUpdate();
				
				pstmt = DBConn.prepareStatement(servicesOtherInsert);
								
				int pIndex=0;
				pstmt.setInt( ++pIndex,dist.getId());
				pstmt.setString( ++pIndex,dist.getServicesOther());
				pstmt.executeUpdate();
		
			}catch (Exception e)	{
				SMRCLogger.error("DistributorDAO.servicesOtherInsert(): ", e);
				throw e;
			}
			finally {
				SMRCConnectionPoolUtils.close(pstmt);
			}
		
			
		
		}		

	private static String getServicesOther(Distributor dist, Connection DBConn) throws Exception{
		
			String note = "";
			PreparedStatement pstmt = null;
			
			ResultSet rs = null;
	
			try {

				pstmt = DBConn.prepareStatement(getServicesOtherQuery);
				int pIndex=0;
				pstmt.setInt( ++pIndex,dist.getId());
				rs = pstmt.executeQuery();
				
				while(rs.next()){
					note = StringManipulation.noNull(rs.getString("NOTES"));			
				}
	
		
			}catch (Exception e)	{
				SMRCLogger.error("DistributorDAO.getServicesOther(): ", e);
				throw e;
			}
			finally {
				SMRCConnectionPoolUtils.close(rs);
				SMRCConnectionPoolUtils.close(pstmt);
			}
		
			return note;
		
		}	
	
	private static void buyingGroupOtherInsert(Distributor dist, Connection DBConn) throws Exception{
		PreparedStatement pstmt = null;

		try {
			pstmt = DBConn.prepareStatement(buyingGroupOtherDelete);
			pstmt.setInt(1,dist.getId());
			pstmt.executeUpdate();
			
			pstmt = DBConn.prepareStatement(buyingGroupOtherInsert);
							
			int pIndex=0;
			pstmt.setInt( ++pIndex,dist.getId());
			pstmt.setString( ++pIndex,dist.getBuyingGroupOther());
			pstmt.executeUpdate();
		}catch (Exception e)	{
			SMRCLogger.error("DistributorDAO.buyingGroupOtherInsert(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
	
	}		

	private static String getBuyingGroupOther(Distributor dist, Connection DBConn) throws Exception{
	
		String note = "";
		PreparedStatement pstmt = null;
		
		ResultSet rs = null;

		try {

			pstmt = DBConn.prepareStatement(getBuyingGroupOtherQuery);
			int pIndex=0;
			pstmt.setInt( ++pIndex,dist.getId());
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				note = StringManipulation.noNull(rs.getString("NOTES"));			
			}
		}catch (Exception e)	{
			SMRCLogger.error("DistributorDAO.getBuyingGroupOther(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return note;
	
	}		
	
	
	private static String[] genericDistributorQuery(int distId, String table, String column, Connection DBConn) throws Exception{
		String[] returnThings=null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "SELECT " + column + " FROM " + table + " WHERE DISTRIBUTOR_ID=?";
		ArrayList things = new ArrayList();
		
		try {
			pstmt = DBConn.prepareStatement(query);
			pstmt.setInt(1,distId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				things.add(rs.getString(1));
			}
			
			returnThings = new String[things.size()]; 
				
			for(int i=0;i<things.size();i++){
				returnThings[i]=(String)things.get(i);
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("DistributorDAO.genericDistributorQuery(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return returnThings;
	}	

	public static ArrayList getCountySales(Distributor dist, Connection DBConn) throws Exception{

		ArrayList returnSegments = new ArrayList();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getCountySalesQuery);
			pstmt.setInt(1, dist.getId());
	
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				DistributorCountySalesRecord rec = new DistributorCountySalesRecord();
				rec.setCounty(StringManipulation.noNull(rs.getString("COUNTY_ID")));
				rec.setPercentage(StringManipulation.noNull(rs.getString("PERCENT_SOLD_THRU_STOCK")));
				returnSegments.add(rec);
			}
	
		}catch (Exception e)	{
			SMRCLogger.error("DistributorDAO.getCountySales(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return returnSegments;
		
	}	




	private static void setCountySales(Distributor dist, Connection DBConn) throws Exception{
	
		PreparedStatement pstmt = null;
		PreparedStatement pstmtI = null;

		ArrayList countySalesRecords = dist.getCountySales();
		try {
			pstmt = DBConn.prepareStatement(countySalesDelete);
			pstmt.setInt(1,dist.getId());
			pstmt.executeUpdate();
			
			pstmtI = DBConn.prepareStatement(countySalesInsert);
			
			for(int i=0;i<countySalesRecords.size();i++){
				DistributorCountySalesRecord rec = (DistributorCountySalesRecord)countySalesRecords.get(i);
				int pIndex = 0 ;
				if(rec.getCounty().equals("")){
					rec.setCounty("0");
				}
				if(rec.getPercentage().equals("")){
					rec.setPercentage("0");
				}
				
				pstmtI.setInt( ++pIndex,dist.getId());
				pstmtI.setInt( ++pIndex,Globals.a2int(rec.getCounty()));
				pstmtI.setInt( ++pIndex,Globals.a2int(rec.getPercentage()));
				pstmtI.executeUpdate();
			}
			
		}catch (SQLException se)	{
			/*
			 * If a user adds the same county multiple times, just ignore one.
			 */ 
		    if(se.getErrorCode()==1) {
		        // Error Code of 1 means unique constraint violation which is OK
		        // it means they added the same county more than once.  Ignore
		        SMRCLogger.debug("DistributorDAO.setCountySales(): Expected unique constraint violation");
            }else {
                SMRCLogger.error("DistributorDAO.setCountySales(): ", se);
                throw se;
            }
		}catch(Exception e) {
			SMRCLogger.error("DistributorDAO.setCountySales(): ", e);
			throw e;
		}finally {
			SMRCConnectionPoolUtils.close(pstmt);
			SMRCConnectionPoolUtils.close(pstmtI);
		}
	}	
	
	public static ArrayList getDistributorSegments(Distributor dist, Connection DBConn) throws Exception{
		
		ArrayList segments = SegmentsDAO.getDistCustSegments(DBConn);
		ArrayList returnSegments = new ArrayList();
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		if(segments.size()>0){
			try {
				
				
				pstmt = DBConn.prepareStatement(getDistributorSegmentsQuery);
				
				for(int i=0;i<segments.size();i++){
					rs = null;
					Segment seg = (Segment)segments.get(i);
					int pIndex = 0 ;
					pstmt.setInt(++pIndex, dist.getId());
					pstmt.setInt(++pIndex, seg.getSegmentId());
					rs = pstmt.executeQuery();
			
					while (rs.next())
					{
						seg.setSalesPercentage(Globals.a2int(rs.getString("SALES_PERCENTAGE")));
					}
					returnSegments.add(seg);
					SMRCConnectionPoolUtils.close(rs);
				}
				
	
			}catch (Exception e)	{
				SMRCLogger.error("DistributorDAO.getDistributorSegments(): ", e);
				throw e;
			}
			finally {
				SMRCConnectionPoolUtils.close(rs);
				SMRCConnectionPoolUtils.close(pstmt);
			}
		}
		return returnSegments;
		
	}	

	public static void setDistributorProducts(Distributor dist, Connection DBConn) throws Exception{
		
		ArrayList products = dist.getProducts();
		PreparedStatement pstmt = null;
	
		if(products.size()>0){
			try {
				
				pstmt = DBConn.prepareStatement(setDistributorProductsDelete);
				pstmt.setInt(1,dist.getId());
				pstmt.executeUpdate();
				
				pstmt = DBConn.prepareStatement(setDistributorProductsInsert);
				for(int i=0;i<products.size();i++){
					int pIndex=0;
					
					Product product = (Product)products.get(i);
					pstmt.setInt(++pIndex, dist.getId());
					pstmt.setString(++pIndex, product.getId());
					pstmt.setInt(++pIndex, product.getPeriodYYYY());
					pstmt.setDouble(++pIndex, product.getTotalSales());
					pstmt.setInt(++pIndex, Globals.a2int(product.getTotalSalesThruStock()));
					
					if(Globals.a2int(product.getCurrentPrimaryManufacturer())==0){
						pstmt.setNull(++pIndex, java.sql.Types.INTEGER);
					}else{
						pstmt.setInt(++pIndex, Globals.a2int(product.getCurrentPrimaryManufacturer()));
					}
					if(Globals.a2int(product.getCurrentSecondaryManufacturer())==0){
						pstmt.setNull(++pIndex, java.sql.Types.INTEGER);
					}else{
						pstmt.setInt(++pIndex, Globals.a2int(product.getCurrentSecondaryManufacturer()));
					}
					if(Globals.a2int(product.getCurrentOtherManufacturer())==0){
						pstmt.setNull(++pIndex, java.sql.Types.INTEGER);
					}else{
						pstmt.setInt(++pIndex, Globals.a2int(product.getCurrentOtherManufacturer()));
					}
					if(Globals.a2int(product.getProposedPrimaryManufacturer())==0){
						pstmt.setNull(++pIndex, java.sql.Types.INTEGER);
					}else{
						pstmt.setInt(++pIndex, Globals.a2int(product.getProposedPrimaryManufacturer()));
					}
					if(Globals.a2int(product.getProposedSecondaryManufacturer())==0){
						pstmt.setNull(++pIndex, java.sql.Types.INTEGER);
					}else{
						pstmt.setInt(++pIndex, Globals.a2int(product.getProposedSecondaryManufacturer()));
					}
					if(Globals.a2int(product.getProposedOtherManufacturer())==0){
						pstmt.setNull(++pIndex, java.sql.Types.INTEGER);
					}else{
						pstmt.setInt(++pIndex, Globals.a2int(product.getProposedOtherManufacturer()));
					}
					
					pstmt.executeUpdate();
				}
				
			
			}catch (Exception e)	{
				SMRCLogger.error("DistributorDAO.setDistributorProducts(): ", e);
				throw e;
			}
			finally {
				SMRCConnectionPoolUtils.close(pstmt);
			}
		}

	}	
	
	public static ArrayList getDistributorProducts(Distributor dist, Connection DBConn) throws Exception{
		
		ArrayList products = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		

			try {			
				pstmt = DBConn.prepareStatement(getDistributorProductsQuery);
				pstmt.setInt(1, dist.getId());
				rs=pstmt.executeQuery();
				while(rs.next()){
					Product product = new Product();
					product.setId(rs.getString("PRODUCT_ID"));
					product.setPeriodYYYY(Globals.a2int(rs.getString("PERIOD_YYYY")));
					product.setTotalSales(Globals.a2double(rs.getString("TOTAL_SALES")));
					product.setTotalSalesThruStock(rs.getString("PERCENT_STOCK_SALE"));
					product.setCurrentPrimaryManufacturer(rs.getString("PRI_CURRENT_COMPETITOR_ID"));
					product.setCurrentSecondaryManufacturer(rs.getString("SEC_CURRENT_COMPETITOR_ID"));
					product.setCurrentOtherManufacturer(rs.getString("OTHER_CURRENT_COMPETITOR_ID"));
					product.setProposedPrimaryManufacturer(rs.getString("PRI_PROPOSED_COMPETITOR_ID"));
					product.setProposedSecondaryManufacturer(rs.getString("SEC_PROPOSED_COMPETITOR_ID"));
					product.setProposedOtherManufacturer(rs.getString("OTHER_PROPOSED_COMPETITOR_ID"));	
					products.add(product);
				}
				
			}catch (Exception e)	{
				SMRCLogger.error("DistributorDAO.getDistributorProducts(): ", e);
				throw e;
			}
			finally {
				SMRCConnectionPoolUtils.close(rs);
				SMRCConnectionPoolUtils.close(pstmt);
			}

		return products;
	}	

	public static void saveKeyAccount(String distId, String keyAcctId, Connection DBConn) throws Exception{
		PreparedStatement pstmt = null;
		
		try {
			pstmt = DBConn.prepareStatement(saveKeyAccountInsert);
			pstmt.setInt(1,Globals.a2int(distId));
			pstmt.setString(2,keyAcctId);
			pstmt.executeUpdate();
		}catch (SQLException sqle){
		    // if Its an sql exception of code=1 - unique constraint, just ignore.
		    // the user added duplicate accounts
		    if(sqle.getErrorCode()==1) {
		        SMRCLogger.warn("DistributorDAO.saveKeyAccount(): Unique constraint - user tried to add account that already is listed.");
		    }else {
		        SMRCLogger.error("DistributorDAO.saveKeyAccount(): ", sqle);
		        throw sqle;
		    }
		}catch (Exception e)	{
			SMRCLogger.error("DistributorDAO.saveKeyAccounts(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
	}
	
	public static void saveDistributorsImpacted(String distId, String vcn, Connection DBConn) throws Exception{
		
		ApprovalSummaryDAO.initializeApprovalSummary(Globals.a2int(distId),DBConn);
		
		PreparedStatement pstmt = null;

		try {
			pstmt = DBConn.prepareStatement(saveDistributorsImpactedInsert);
			pstmt.setInt(1,Globals.a2int(distId));
			pstmt.setString(2,vcn);
			pstmt.executeUpdate();


		}catch (Exception e)	{
			SMRCLogger.error("DistributorDAO.saveDistributorsImpacted(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}	
	}
	
	public static void deleteKeyAccount(int distId, String keyAcctId, Connection DBConn) throws Exception{
		PreparedStatement pstmt = null;

		try {
			pstmt = DBConn.prepareStatement(deleteKeyAccount);
			pstmt.setInt(1,distId);
			pstmt.setString(2,keyAcctId);
			pstmt.executeUpdate();
			
			pstmt = DBConn.prepareStatement(deleteKeyAccount2);
			pstmt.setInt(1,distId);
			pstmt.setString(2,keyAcctId);
			pstmt.executeUpdate();
			
		}catch (Exception e)	{
			SMRCLogger.error("DistributorDAO.deleteKeyAccount(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
	}

	public static void updateKeyAccount(int distId, String keyAcctId, String[] productsIds, String potential, Connection DBConn) throws Exception{
		PreparedStatement pstmt = null;

		try {
			pstmt = DBConn.prepareStatement(updateKeyAccount);
			pstmt.setInt(1,Globals.a2int(potential));
			pstmt.setInt(2,distId);
			pstmt.setString(3,keyAcctId);
			pstmt.executeUpdate();

			pstmt = DBConn.prepareStatement(deleteDistribProductOpp);
			pstmt.setInt(1,distId);
			pstmt.setString(2,keyAcctId);
			pstmt.executeUpdate();
			
			pstmt = DBConn.prepareStatement(insertDistribProductOpp);
			
			if(productsIds!=null){
				for(int i=0;i<productsIds.length;i++){
					pstmt.setInt(1,distId);
					pstmt.setString(2,keyAcctId);
					pstmt.setInt(3,Globals.a2int(productsIds[i]));
					pstmt.executeUpdate();
				}
			}
		}catch (SQLException sqle){
		    // if Its an sql exception of code=1 - unique constraint, just ignore.
		    // the user added duplicate accounts
		    if(sqle.getErrorCode()==1) {
		        SMRCLogger.warn("DistributorDAO.updateKeyAccount(): Unique constraint - user tried to add account that already is listed.");
		    }else {
		        SMRCLogger.error("DistributorDAO.updateKeyAccount(): ", sqle);
		        throw sqle;
		    }
		}catch (Exception e){
		    SMRCLogger.error("DistributorDAO.updateKeyAccount(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
	}
	

	
	public static ArrayList getKeyAccounts(int distId, Connection DBConn) throws Exception{
		
		ArrayList accounts = new ArrayList();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
			
		try {			
			pstmt = DBConn.prepareStatement(getKeyAccountsQuery);
			pstmt.setInt(1, distId);
			rs=pstmt.executeQuery();
			while(rs.next()){
				DistributorKeyAccount acct = new DistributorKeyAccount();
				acct.setVcn(rs.getString("VISTA_CUSTOMER_NUMBER"));
				acct.setAccountName(rs.getString("CUSTOMER_NAME"));
				acct.setPotentialEatonDollars(rs.getInt("POTENTIAL_EATON_DOLLARS"));
				acct.setProductIds(getKeyAccountProductIds(distId,acct.getVcn(), DBConn));
				accounts.add(acct);
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("DistributorDAO.getKeyAccounts(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}

		return accounts;
	}	
	
	
	public static ArrayList getKeyAccountProductIds(int distId, String vcn, Connection DBConn) throws Exception{
		
		ArrayList productIds = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {			
			pstmt = DBConn.prepareStatement(getKeyAccountProductIdsQuery);
			pstmt.setInt(1, distId);
			pstmt.setString(2, vcn);
			rs=pstmt.executeQuery();
			
			while(rs.next()){		
				productIds.add(rs.getString("PRODUCT_ID"));
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("DistributorDAO.getKeyAccountProductIds(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}

		return productIds;
	}	
	
	public static ArrayList getSupplierProducts(Connection DBConn) throws Exception{
		
		ArrayList products = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {			
			pstmt = DBConn.prepareStatement(getSupplierProductsQuery);
			rs=pstmt.executeQuery();
			
			while(rs.next()){		
				Product product = new Product();
				product.setId(""+rs.getInt("SUPPLIER_PRODUCT_ID"));
				product.setDescription(rs.getString("PRODUCT_NAME"));
				products.add(product);
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("DistributorDAO.getSupplierProducts(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}

		return products;
	}	
	
	public static ArrayList getSelectedSupplierProducts(Distributor dist, Connection DBConn) throws Exception{
		
		ArrayList supplierProducts = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

			try {			
				pstmt = DBConn.prepareStatement(getSelectedSupplierProductsQuery);
				pstmt.setInt(1, dist.getId());
				rs=pstmt.executeQuery();
				
				while(rs.next()){		
					DistributorSupplierProduct supplierProduct = new DistributorSupplierProduct();
					supplierProduct.setVendorId(rs.getString("SUPPLIER_ID"));
					supplierProduct.setProductId(rs.getString("SUPPLIER_PRODUCT_ID"));
					supplierProducts.add(supplierProduct);
				}
				
			}catch (Exception e)	{
				SMRCLogger.error("DistributorDAO.getSelectedSupplierProducts(): ", e);
				throw e;
			}
			finally {
				SMRCConnectionPoolUtils.close(rs);
				SMRCConnectionPoolUtils.close(pstmt);
			}

		return supplierProducts;
	}		
	
	private static void buyingGroupsUpdate(Distributor dist,Connection DBConn) throws Exception{

		PreparedStatement pstmt = null;
		PreparedStatement pstmtI = null;
		ArrayList buyingGroups = dist.getBuyingGroupAssn();

		try {
			pstmt = DBConn.prepareStatement(deleteBuyingGroups);
			pstmt.setInt(1,dist.getId());
			pstmt.executeUpdate();

			pstmtI = DBConn.prepareStatement(insertBuyingGroup);
			
			for(int i=0;i<buyingGroups.size();i++){
				String buyingGroup = (String)buyingGroups.get(i);

				pstmtI.setInt(1,dist.getId());
				pstmtI.setInt(2,Globals.a2int(buyingGroup));
				
				pstmtI.executeUpdate();
			}

		}catch (Exception e)	{
			SMRCLogger.error("DistributorDAO.buyingGroupsUpdate(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
			SMRCConnectionPoolUtils.close(pstmtI);
		}
	}
	
	private static void supplierProductUpdate(Distributor dist,Connection DBConn) throws Exception{
		PreparedStatement pstmt = null;
		PreparedStatement pstmtI = null;
		ArrayList supplierProds = dist.getSuppliersProducts();
		
		try {
			pstmt = DBConn.prepareStatement(deleteSupplierProducts);
			pstmt.setInt(1,dist.getId());
			pstmt.executeUpdate();

			pstmtI = DBConn.prepareStatement(insertSupplierProduct);
			
			for(int i=0;i<supplierProds.size();i++){
				DistributorSupplierProduct distProduct = (DistributorSupplierProduct)supplierProds.get(i);

				pstmtI.setInt(1,dist.getId());
				pstmtI.setInt(2,Globals.a2int(distProduct.getProductId()));
				pstmtI.setInt(3,Globals.a2int(distProduct.getVendorId()));
				
				pstmtI.executeUpdate();
			}

		}catch (Exception e)	{
			SMRCLogger.error("DistributorDAO.supplierProductUpdate(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
			SMRCConnectionPoolUtils.close(pstmtI);
		}
	}
	
	public static int getDistributorId(String vcn, Connection DBConn) throws Exception{
		return AccountDAO.getDistributorId(vcn, DBConn);
	}
	
	


}
