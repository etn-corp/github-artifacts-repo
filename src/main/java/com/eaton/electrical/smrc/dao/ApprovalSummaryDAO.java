package com.eaton.electrical.smrc.dao;

import java.sql.*;
import java.util.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

/**
 * @author E0062708 - Josh Vender
 *
 * This class provides most database calls related to the DistributorApproval object.
 * It is mostly used in the "Approval Summary" page for distributor accounts.
 * 
 */
public class ApprovalSummaryDAO {
	//private static final String getApprovalSummaryQuery = "SELECT * FROM DISTRIB_APPROVAL_SUMMARY WHERE DISTRIBUTOR_ID=?";
	private static final String getApprovalSummaryQuery = "SELECT DAS.*, D.PROJECTED_EATON_SALES_YEAR1 EATON_SALES1, D.PROJECTED_EATON_SALES_YEAR2 EATON_SALES2" +
			" FROM DISTRIB_APPROVAL_SUMMARY DAS, DISTRIBUTORS D WHERE D.DISTRIBUTOR_ID= ? AND DAS.DISTRIBUTOR_ID = D.DISTRIBUTOR_ID";
	private static final String initializeApprovalSummaryInsert = "INSERT INTO DISTRIB_APPROVAL_SUMMARY (DISTRIBUTOR_ID) VALUES (?)";
	private static final String isApprovalSummaryInitializedQuery = "SELECT * FROM DISTRIB_APPROVAL_SUMMARY WHERE DISTRIBUTOR_ID=?";
	private static final String saveApprovalSummaryUpdate = "UPDATE DISTRIB_APPROVAL_SUMMARY SET COMMITMENT_LEVEL=?,EXCLUSIVE_EATON_COMMITMENT=?,"+
			"PROJECTED_EATON_SALES_YEAR1=?,PROJECTED_EATON_SALES_YEAR3=?,NET_AREA_IMPACT_YEAR1=?,NET_AREA_IMPACT_YEAR3=?,DISTRICT_STRATEGY=?"+
			" WHERE DISTRIBUTOR_ID=?"; 
	private static final String deleteImpactedDist="DELETE FROM IMPACTED_DISTRIBUTORS WHERE DISTRIBUTOR_ID=?";
	private static final String insertImpactedDist="INSERT INTO IMPACTED_DISTRIBUTORS (DISTRIBUTOR_ID,VISTA_CUSTOMER_NUMBER,EATON_SALES_AT_RISK,CONTACT_NOTIFIED,NOTES) VALUES (?,?,?,?,?)";
	
	private static final String getImpactedDistributorsQuery = "with cps as ( " +
			"select b.vista_customer_number, " +
            "sum(case when year = (? - 1) then a.total_sales else 0 end) prev_year_total " +
			"from credit_customer_sales a, impacted_distributors b, products " +
			"where b.vista_customer_number=a.credit_customer_number " +
			"and b.distributor_id=? " +
			"and a.product_id = products.product_id " +
			"and products.sp_load_total = 'T' " +
			"and products.period_yyyy = ? " +
			"group by b.vista_customer_number ) " +
			"select a.vista_customer_number, a.customer_name,c.city, c.state,d.eaton_sales_at_risk,d.contact_notified,d.notes,p.prev_year_total " +
			"from customer a, customer_address c, impacted_distributors d, cps p " +
			"where a.vista_customer_number = d.vista_customer_number " +
			"and c.vista_customer_number(+)=d.vista_customer_number " +
			"and d.distributor_id = ? " +
			"and c.address_type_id(+)=46 " +
			"and a.vista_customer_number=p.vista_customer_number(+) " +
			"order by a.customer_name";
	
	private static final String insertSummaryCompetitor = "INSERT INTO DISTRIB_COMPETITORS (DISTRIBUTOR_ID,VENDOR_ID,YEAR1_SALES,YEAR3_SALES) VALUES(?,?,?,?)";
	private static final String deleteSummaryCompetitors = "DELETE FROM DISTRIB_COMPETITORS WHERE DISTRIBUTOR_ID=?";
	private static final String getSummaryCompetitorsSelect1 = "SELECT VENDOR_ID FROM VENDORS WHERE DESCRIPTION='Eaton'";
	private static final String getSummaryCompetitorsSelect2 = "SELECT * FROM DISTRIB_PRD_COMPETITORS WHERE DISTRIBUTOR_ID=?";
	private static final String getSummaryCompetitorSelect = "select * from vendors a, distrib_competitors b"+
	" where a.vendor_id=b.vendor_id(+)"+
	" and a.vendor_id=?" + 
	" and b.distributor_id(+)=?";
	
	
	/**
	 * Saves distributor approval information
	 * @param distApproval  the distributor approval information
	 * @param DBConn  the database connection
	 * @throws Exception
	 */
	public static void saveApprovalSummary(DistributorApproval distApproval, Connection DBConn) throws Exception
	{
		int distId= distApproval.getDistributorId();
		initializeApprovalSummary(distId,DBConn);

		PreparedStatement pstmt = null;
		
		try {

			pstmt = DBConn.prepareStatement(saveApprovalSummaryUpdate);

			int pIndex = 0 ;

			pstmt.setString( ++pIndex, distApproval.getCommitmentLevel());
			if(distApproval.getCommitmentDay().trim().length()==0 || distApproval.getCommitmentMonth().trim().length()==0 || distApproval.getCommitmentYear().trim().length()==0){
				pstmt.setDate( ++pIndex, null);
			}else{
				pstmt.setDate( ++pIndex, new java.sql.Date(distApproval.getExclusiveEatonCommitment().getTime()));
			}
			pstmt.setDouble( ++pIndex, distApproval.getProjEatonSalesYr1());
			pstmt.setDouble( ++pIndex, distApproval.getProjEatonSalesYr3());
			pstmt.setDouble( ++pIndex, distApproval.getNetAreaImpactYr1());
			pstmt.setDouble( ++pIndex, distApproval.getNetAreaImpactYr3());
			pstmt.setString( ++pIndex, distApproval.getDistrictStrategy());
			pstmt.setInt( ++pIndex, distApproval.getDistributorId());
			pstmt.executeUpdate();
			
			// update the impacted distributors table
			
			pstmt = DBConn.prepareStatement(deleteImpactedDist);
			pstmt.setInt(1, distApproval.getDistributorId());
			pstmt.executeUpdate();
			
			pstmt = DBConn.prepareStatement(insertImpactedDist);
			
			ArrayList impactedDists = distApproval.getImpactedDistributors();	
			for(int i=0;i<impactedDists.size();i++){
				ImpactedDistributor impDist = (ImpactedDistributor)impactedDists.get(i);
				pstmt.setInt(1, impDist.getDistId());
				pstmt.setString(2, impDist.getVcn());
				pstmt.setDouble(3, impDist.getSalesAtRisk());
				if(impDist.getContactId()==0){
					pstmt.setNull(4, java.sql.Types.INTEGER);
				}else{
					pstmt.setInt(4, impDist.getContactId());
				}
				pstmt.setString(5, impDist.getNotes());
				pstmt.executeUpdate();
			}
			
			pstmt = DBConn.prepareStatement(deleteSummaryCompetitors);
			pstmt.setInt(1, distApproval.getDistributorId());
			pstmt.executeUpdate();

			
			pstmt = DBConn.prepareStatement(insertSummaryCompetitor);
			ArrayList summaryCompetitors = distApproval.getSummaryCompetitors();	
			for(int i=0;i<summaryCompetitors.size();i++){				
				SummaryCompetitor summaryCompetitor = (SummaryCompetitor)summaryCompetitors.get(i);
				SMRCLogger.debug("*" + distApproval.getDistributorId() + " " + summaryCompetitor.getId());
				pstmt.setInt(1, distApproval.getDistributorId());
				pstmt.setInt(2, summaryCompetitor.getId());
				pstmt.setDouble(3, summaryCompetitor.getYear1Sales());
				pstmt.setDouble(4, summaryCompetitor.getYear3Sales());
				pstmt.executeUpdate();
			}
			
		}catch (Exception e){
			SMRCLogger.error("ApprovalSummaryDAO.insertNewApprovalSummary():" , e);
			throw e;
		}finally{
			SMRCConnectionPoolUtils.close(pstmt);
		}
			
		
		
	}
	
	/**
	 * Initializes the distributor approval in the database.  If the database is initialized
	 * 		it returns, otherwise it inserts a new record with appropriate distributor ID
	 * @param distId  the distributor ID
	 * @param DBConn  the database connection
	 * @throws Exception
	 */
	public static void initializeApprovalSummary(int distId, Connection DBConn) throws Exception
	{
		if(isApprovalSummaryInitialized(distId, DBConn)){
			return;
		}
		
		PreparedStatement pstmt = null;
		try {
			pstmt = DBConn.prepareStatement(initializeApprovalSummaryInsert);
			int pIndex = 0 ;
			pstmt.setInt( ++pIndex, distId);
			pstmt.executeUpdate();

		}catch (Exception e){
			SMRCLogger.error("ApprovalSummaryDAO.initializeApprovalSummary(): ", e);
			throw e;
		}finally{
			SMRCConnectionPoolUtils.close(pstmt);
		}
	}
	
	/**
	 * Checks to see if the distributor approval is in the database.
	 * @param distId  the distributor ID
	 * @param DBConn  the database connection
	 * @return true if the table is initialized, false if it is not
	 * @throws Exception
	 */
	private static boolean isApprovalSummaryInitialized(int distId, Connection DBConn) throws Exception{
		int count=0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(isApprovalSummaryInitializedQuery);
			int pIndex = 0 ;
			pstmt.setInt( ++pIndex, distId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				count=rs.getInt(1);
			}
			if(count==0){
				return false;
			}
			return true;
			
		}catch (Exception e){
			SMRCLogger.error("ApprovalSummaryDAO.isApprovalSummaryInitialized(): ", e);
			throw e;
		}finally{
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
	}
	
	/**
	 * Gets distributor approval summary
	 * @param acctId  the VCN or prospect number of the account
	 * @param DBConn  the database connection
	 * @return  the distributor approval summary data
	 * @throws Exception
	 */
	public static DistributorApproval getApprovalSummary(String acctId, int srYear, Connection DBConn) throws Exception{
		DistributorApproval distApproval = new DistributorApproval();		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getApprovalSummaryQuery);
			
			pstmt.setInt(1, AccountDAO.getDistributorId(acctId, DBConn));
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				distApproval.setVcn(acctId);
				distApproval.setDistributorId(rs.getInt("DISTRIBUTOR_ID"));
				distApproval.setCommitmentLevel(StringManipulation.noNull(rs.getString("COMMITMENT_LEVEL")));
				distApproval.setExclusiveEatonCommitment(rs.getDate("EXCLUSIVE_EATON_COMMITMENT"));
				//distApproval.setProjEatonSalesYr1(rs.getDouble("PROJECTED_EATON_SALES_YEAR1"));
				//distApproval.setProjEatonSalesYr3(rs.getDouble("PROJECTED_EATON_SALES_YEAR3"));
				distApproval.setProjEatonSalesYr1(rs.getDouble("EATON_SALES1"));
				distApproval.setProjEatonSalesYr3(rs.getDouble("EATON_SALES2"));
				distApproval.setNetAreaImpactYr1(rs.getDouble("NET_AREA_IMPACT_YEAR1"));
				distApproval.setNetAreaImpactYr3(rs.getDouble("NET_AREA_IMPACT_YEAR3"));
				distApproval.setImpactedDistributors(getImpactedDistributors(rs.getInt("DISTRIBUTOR_ID"), srYear, DBConn));
				distApproval.setDistrictStrategy(rs.getString("DISTRICT_STRATEGY"));
				
			}

		}catch (Exception e){
			SMRCLogger.error("ApprovalSummaryDAO.getApprovalSummary(): ", e);
			throw e;
		}finally{
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
			
	return distApproval;
		
	}
	
	/**
	 * Gets the impacted distributors for the Approval Summary page
	 * @param distId  the Distributor ID
	 * @param DBConn  the database connection
	 * @return ImpactedDistributor objects for the specified distributor Id
	 * @throws Exception
	 */
	public static ArrayList getImpactedDistributors(int distId, int srYear, Connection DBConn) throws Exception{
		ArrayList impactedDistributors = new ArrayList();
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		try {
			pstmt = DBConn.prepareStatement(getImpactedDistributorsQuery);
			
			pstmt.setInt(1, srYear);
			pstmt.setInt(2, distId);
			pstmt.setInt(3, srYear);
			pstmt.setInt(4, distId);
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				ImpactedDistributor impactedDist = new ImpactedDistributor();
				impactedDist.setVcn(StringManipulation.noNull(rs.getString("VISTA_CUSTOMER_NUMBER")));
				impactedDist.setCustomerName(StringManipulation.noNull(rs.getString("CUSTOMER_NAME")));
				impactedDist.setState(StringManipulation.noNull(rs.getString("STATE")));
				impactedDist.setCity(StringManipulation.noNull(rs.getString("CITY")));
				impactedDist.setAnnualEatonSales(rs.getDouble("PREV_YEAR_TOTAL"));
				impactedDist.setSalesAtRisk(rs.getDouble("EATON_SALES_AT_RISK"));
				impactedDist.setContactId(rs.getInt("CONTACT_NOTIFIED"));
				impactedDist.setNotes(StringManipulation.noNull(rs.getString("NOTES")));
				
				// Get the associated contacts for this ImpactedDistributor				
				impactedDist.setContacts(ContactsDAO.getContacts(impactedDist.getVcn(), DBConn));
				
				impactedDistributors.add(impactedDist);
				
			}
		}catch (Exception e){
			SMRCLogger.error("ApprovalSummaryDAO.getImpactedDistributors(): ", e);
			throw e;
		}finally{
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
			
	return impactedDistributors;
		
	}
	
	/**
	 * Gets the summary competitors (Vendors) for the Approval Summary page
	 * @param distId  the Distributor ID
	 * @param DBConn  the database connection
	 * @return LinkedHashSet objects of vendor IDs
	 * @throws Exception
	 */
	public static ArrayList getSummaryCompetitors(int distId, Connection DBConn) throws Exception{
		
		ArrayList vendors = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		LinkedHashSet lhs = new LinkedHashSet();
			try {			
				
				
				pstmt = DBConn.prepareStatement(getSummaryCompetitorsSelect1);
				rs=pstmt.executeQuery();
				if(rs.next()){
					lhs.add(new Integer(rs.getInt("VENDOR_ID")));
				}
				
				SMRCLogger.debug("SQL: ApprovalSummaryDAO.getSummaryCompetitors:\n" + getSummaryCompetitorsSelect2 + "\n? = " + distId);
				pstmt = DBConn.prepareStatement(getSummaryCompetitorsSelect2);
				pstmt.setInt(1, distId);
				rs=pstmt.executeQuery();
				
				while(rs.next()){		
					if(rs.getInt("PRI_CURRENT_COMPETITOR_ID")!=0){
						lhs.add(new Integer(rs.getInt("PRI_CURRENT_COMPETITOR_ID")));
					}
					if(rs.getInt("PRI_PROPOSED_COMPETITOR_ID")!=0){
						lhs.add(new Integer(rs.getInt("PRI_PROPOSED_COMPETITOR_ID")));
					}
					if(rs.getInt("SEC_CURRENT_COMPETITOR_ID")!=0){
						lhs.add(new Integer(rs.getInt("SEC_CURRENT_COMPETITOR_ID")));
					}
					if(rs.getInt("SEC_PROPOSED_COMPETITOR_ID")!=0){
						lhs.add(new Integer(rs.getInt("SEC_PROPOSED_COMPETITOR_ID")));
					}
					if(rs.getInt("OTHER_CURRENT_COMPETITOR_ID")!=0){
						lhs.add(new Integer(rs.getInt("OTHER_CURRENT_COMPETITOR_ID")));
					}
					if(rs.getInt("OTHER_PROPOSED_COMPETITOR_ID")!=0){
						lhs.add(new Integer(rs.getInt("OTHER_PROPOSED_COMPETITOR_ID")));
					}
				}

				
                
                Iterator it = lhs.iterator();

                while(it.hasNext()){
                	vendors.add(getSummaryCompetitor(distId,((Integer)it.next()).intValue(),DBConn));
                }
				
				
			}catch (Exception e)	{
				SMRCLogger.error("ApprovalSummaryDAO.getSummaryCompetitors(): ", e);
				throw e;
			}
			finally {
				SMRCConnectionPoolUtils.close(rs);
				SMRCConnectionPoolUtils.close(pstmt);
			}

		return vendors;
	}		
	
	/**
	 * Gets one summary competitor (Vendor) which has the vendor name and year 1 and year 3 sales percentages
	 * @param distId  the Distributor ID
	 * @param vendorId  the vendor ID
	 * @param DBConn  the database connection
	 * @return SummaryCompetitor object
	 * @throws Exception
	 */
	public static SummaryCompetitor getSummaryCompetitor(int distId, int vendorId, Connection DBConn) throws Exception{
		
		SummaryCompetitor summaryCompetitor = new SummaryCompetitor();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

			try {			

				
				SMRCLogger.debug("SQL: ApprovalSummaryDAO.getSummaryCompetitor:\n" + getSummaryCompetitorSelect + "\n? = " + vendorId + "\n? = " + distId);
				pstmt = DBConn.prepareStatement(getSummaryCompetitorSelect);
				pstmt.setInt(1, vendorId);
				pstmt.setInt(2, distId);
				rs=pstmt.executeQuery();
				
				while(rs.next()){		
					summaryCompetitor.setId(rs.getInt("VENDOR_ID"));
					summaryCompetitor.setDescription(rs.getString("DESCRIPTION"));
					summaryCompetitor.setYear1Sales(rs.getDouble("YEAR1_SALES"));
					summaryCompetitor.setYear3Sales(rs.getDouble("YEAR3_SALES"));
				}
			}catch (Exception e)	{
				SMRCLogger.error("ApprovalSummaryDAO.getSummaryCompetitor(): ", e);
				throw e;
			}
			finally {
				SMRCConnectionPoolUtils.close(rs);
				SMRCConnectionPoolUtils.close(pstmt);
			}

			return summaryCompetitor;
	}		
	
}
