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
public class ProjectDAO {
	private static final String getTargetProjectsQry = "select target_project_id from target_projects where sp_geog = ? and internal_status = 'A'";
	private static final String getApprovalPendingProjectsQuery = "select target_project_id from target_projects where internal_status = ? and sp_geog like ?";
	private static final String getApprovalPendingProjectsMktMgrQuery = "select target_project_id from target_projects where internal_status = ?";
	private static final String getTargetProjectQuery = "select * from target_projects where target_project_id = ?";
	private static final String getProjectXrefQuery = "select * from customer_project_xref where target_project_id = ?";
	private static final String getProjectVendorsQuery = "select v.vendor_id, v.description from target_project_vendors tpv, vendors v " +
     	"where v.vendor_id = tpv.vendor_id and tpv.target_project_id = ?";
	private static final String getProjectBOMQuery = "select p.product_id, p.product_description from target_project_bom tpb, products p " +
        "where p.product_id = tpb.product_id and tpb.target_project_id = ? and p.period_yyyy = ?";
	private static final String deleteProjectUpdate = "update target_projects set internal_status = 'D', date_changed = sysdate where target_project_id = ?";
	private static final String updateProjectDMApproved = "update target_projects set internal_status = 'Z', dm_approval_id = ?, " +
        "dm_approval_date = sysdate, date_changed = sysdate where target_project_id = ?";
//    private static final String updateProjectZMApproved = "update target_projects set internal_status = 'M', zm_approval_id = ?, zm_approval_date = sysdate, " + 
//       "date_changed = sysdate where target_project_id = ?";
    private static final String updateProjectPSMApproved = "update target_projects set internal_status = 'M', proj_sales_mgr_approval_id = ?, proj_sales_mgr_approval_date = sysdate, " + 
    "date_changed = sysdate where target_project_id = ?";
//    private static final String updateProjectMMApproved = "update target_projects set internal_status = 'A', mkt_mgr_approval_id = ?, mkt_mgr_approval_date = sysdate, " + 
//        "date_changed = sysdate where target_project_id = ?";
    private static final String updateProjectCHAMPSMgrApproved = "update target_projects set internal_status = 'A', champs_mgr_approval_id = ?, champs_mgr_approval_date = sysdate, " + 
    "date_changed = sysdate where target_project_id = ?";
               
	
	public static ArrayList getTargetProjects(String geog, Connection DBConn) throws Exception {
		ArrayList projects = new ArrayList(20);

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DBConn.prepareStatement(getTargetProjectsQry);
			
			pstmt.setString(1, geog);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				TargetProject tp = new TargetProject();
				tp = getTargetProject(DBConn, rs.getString("target_project_id"));
				projects.add(tp);
			}
			
		}catch (Exception e)	{
			SMRCLogger.error("ProjectDAO.getTargetProjects(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return projects;
	}
	
	public static ArrayList getApprovalPendingProjects(User user, Connection DBConn) throws Exception {
	    ArrayList projects = new ArrayList();
	    // Only division, zone, and district managers approve target projects
	    if (!user.isProjectSalesManager() && !user.isDistrictManager() && !user.isCHAMPSManager()){
	        return projects;
	    }
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    try {
	        if (user.isDistrictManager()){
	            String district = user.getGeography();
	            pstmt = DBConn.prepareStatement(getApprovalPendingProjectsQuery);
	            pstmt.setString(1,"N");
	            pstmt.setString(2,district + "%");
	        } else if (user.isProjectSalesManager()){
	            String district = user.getUserGroupGeog();
	            pstmt = DBConn.prepareStatement(getApprovalPendingProjectsQuery);
	            pstmt.setString(1,"Z");
	            pstmt.setString(2,district.substring(0,4) + "%");
	        } else if (user.isCHAMPSManager()){
	            pstmt = DBConn.prepareStatement(getApprovalPendingProjectsMktMgrQuery);
	            pstmt.setString(1,"M");
	        }
	        rs = pstmt.executeQuery();
	        while (rs.next()){
	            TargetProject tp = getTargetProject(DBConn, rs.getString("target_project_id"));
	            projects.add(tp);
	        }
	        
	    } catch (Exception e) {
	        SMRCLogger.error("ProjectDAO.getApprovalPendingProjects(): ", e);
	        throw e;
	    } finally {
	        SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
	    }
	    
	  
	    return projects;
	}
	

    public static TargetProject getTargetProject(Connection aConnection, String id) throws Exception {

        TargetProject tp = new TargetProject();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = aConnection.prepareStatement(getTargetProjectQuery);
            pstmt.setInt(1,Globals.a2int(id));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                tp.setId(rs.getInt("target_project_id"));

                if (rs.getString("sp_geog") != null) {
                    tp.setSPGeog(rs.getString("sp_geog"));
                }

                if (rs.getString("job_name") != null) {
                    tp.setJobName(rs.getString("job_name"));
                }

                if (rs.getString("consultant") != null) {
                    tp.setConsultant(rs.getString("consultant"));
                }

                if (rs.getString("ch_value") != null) {
                    tp.setCHValue(rs.getDouble("ch_value"));
                }

                if (rs.getString("total_value") != null) {
                    tp.setTotalValue(rs.getDouble("total_value"));
                }

                if (rs.getString("bid_date") != null) {
                    tp.setBidDate(rs.getDate("bid_date"));
                }

                if (rs.getString("cop_id") != null) {
                    tp.setChangeOrderPotential(TAPcommon.getChangeOrderPotential(rs.getString("cop_id"), aConnection));
                }

                if (rs.getString("general_contractors") != null) {
                    tp.setGenContractors(rs.getString("general_contractors"));
                }

                if (rs.getString("electrical_contractors") != null) {
                    tp.setElecContractors(rs.getString("electrical_contractors"));
                }

                if (rs.getString("ch_position_with_contractor") != null) {
                    tp.setCHPosition(rs.getString("ch_position_with_contractor"));
                }

                if (rs.getString("status_id") != null) {
                    tp.setStatus(TAPcommon.getProjectStatus(rs.getInt("status_id")
                            + "", aConnection));
                }

                if (rs.getString("preference_id") != null) {
                    tp.setPreference(TAPcommon.getSpecPreference(rs.getInt("preference_id")
                            + "", aConnection));
                }

                if (rs.getString("strat_reason_id") != null) {
                    tp.setReason(TAPcommon.getStratReason(rs.getInt("strat_reason_id")
                            + "", aConnection));
                }

                if (rs.getString("status_notes") != null) {
                    tp.setStatusNotes(rs.getString("status_notes"));
                }

                if (rs.getString("preference_notes") != null) {
                    tp.setPreferenceNotes(rs
                            .getString("preference_notes"));
                }

                if (rs.getString("strat_reason_notes") != null) {
                    tp.setStratReasonNotes(rs
                            .getString("strat_reason_notes"));
                }

                if (rs.getString("neg_num") != null) {
                    tp.setNegNum(rs.getString("neg_num"));
                }

                if (rs.getString("notes") != null) {
                    tp.setNotes(rs.getString("notes"));
                }

                if (rs.getString("user_added") != null) {
                    tp.setUserAdded(rs.getString("user_added"));
                }

                if (rs.getString("date_added") != null) {
                    tp.setDateAdded(rs.getDate("date_added"));
                }

                if (rs.getString("date_changed") != null) {
                    tp.setDateChanged(rs.getDate("date_changed"));
                }

                if (rs.getString("internal_status") != null) {
                    tp.setInternalStatus(rs.getString("internal_status"));
                }

                if (rs.getString("dm_approval_id") != null) {
                    tp.setDMApprovalId(rs.getString("dm_approval_id"));
                }
                if (rs.getString("zm_approval_id") != null) {
                    tp.setZMApprovalId(rs.getString("zm_approval_id"));
                }
                if (rs.getString("mkt_mgr_approval_id") != null) {
                    tp.setMktMgrApprovalId(rs
                            .getString("mkt_mgr_approval_id"));
                }
                if (rs.getString("dm_approval_date") != null) {
                    tp.setDMApprovalDate(rs.getDate("dm_approval_date"));
                }
                if (rs.getString("zm_approval_date") != null) {
                    tp.setZMApprovalDate(rs.getDate("zm_approval_date"));
                }
                if (rs.getString("mkt_mgr_approval_date") != null) {
                    tp.setMktMgrApprovalDate(rs
                            .getDate("mkt_mgr_approval_date"));
                }
                if (rs.getString("proj_sales_mgr_approval_id") != null) {
                    tp.setProjSalesMgrApproveId(rs.getString("proj_sales_mgr_approval_id"));
                }
                if (rs.getString("champs_mgr_approval_id") != null) {
                    tp.setChampsMgrApproveId(rs.getString("champs_mgr_approval_id"));
                } 
                if (rs.getString("proj_sales_mgr_approval_date") != null) {
                    tp.setProjSalesMgrApproveDt(rs.getDate("proj_sales_mgr_approval_date"));
                }
                if (rs.getString("champs_mgr_approval_date") != null) {
                    tp.setChampsMgrApproveDt(rs.getDate("champs_mgr_approval_date"));
                }
            }
        } catch (Exception e) {
            SMRCLogger.error("ProjectDAO.getTargetProject() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(pstmt);
        }

        ArrayList vendors = getProjectVendors(aConnection, id);
        for (int i = 0; i < vendors.size(); i++) {
            Vendor v = (Vendor) vendors.get(i);
            tp.addVendor(v);
        }
        ArrayList bom = getProjectBOM(aConnection, id);
        for (int i = 0; i < bom.size(); i++) {
            Product p = (Product) bom.get(i);
            tp.addProduct(p);
        }

        
        try {
            pstmt = aConnection.prepareStatement(getProjectXrefQuery);
            pstmt.setInt(1,Globals.a2int(id));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String vcn = rs.getString("vista_customer_number");
                String name = AccountDAO.getAccountName(vcn, aConnection);
                String type = rs.getString("customer_type");

                Customer c = new Customer();
                c.setVistaCustNum(vcn);
                c.setName(name);

                tp.addCustomer(c, type);
            }
        } catch (Exception e) {
            SMRCLogger.error("ProjectDAO.getTargetProject() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(pstmt);
        }

        return tp;

    } //method
    
    private static ArrayList getProjectVendors(Connection aConnection, String id) throws Exception {

        ArrayList vendors = new ArrayList(5);

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = aConnection.prepareStatement(getProjectVendorsQuery);
            pstmt.setInt(1,Globals.a2int(id));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Vendor v = new Vendor();
                v.setId(rs.getInt("vendor_id"));
                v.setDescription(rs.getString("description"));
                vendors.add(v);
            }
        } catch (Exception e) {
            SMRCLogger.error("ProjectDAO.getProjectVendors() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(pstmt);
        }

        return vendors;

    } //method

    public static ArrayList getProjectBOM(Connection aConnection, String id) throws Exception {

        ArrayList bom = new ArrayList(5);
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // Just get srYear here instead of passing it around
            int srYear = Globals.a2int(MiscDAO.getSRYear(aConnection));
            pstmt = aConnection.prepareStatement(getProjectBOMQuery);
            pstmt.setInt(1,Globals.a2int(id));
            pstmt.setInt(2, srYear);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Product p = new Product();

           //     p.setId(rs.getString("product_id"));
           //     p.setDescription(rs.getString("product_description"));
                // Get complete Product object
                p = ProductDAO.productLookup(rs.getString("product_id"),srYear,aConnection);

                bom.add(p);
            }
        } catch (Exception e) {
            SMRCLogger.error("ProjectDAO.getProjectBOM() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(rs);
            SMRCConnectionPoolUtils.close(pstmt);
        }

        return bom;

    } //method

    public static void deleteProject(Connection aConnection, TargetProject tp) throws Exception {

        PreparedStatement pstmt = null;
        try {
            pstmt = aConnection.prepareStatement(deleteProjectUpdate);
            pstmt.setInt(1,tp.getId());
            pstmt.executeUpdate();

        } catch (Exception e) {
            SMRCLogger.error("ProjectDAO.deleteProject() ", e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(pstmt);
        }

    } //method

    public static void approveProject(Connection aConnection, TargetProject tp, User user) throws Exception {

        PreparedStatement pstmt = null;
        try {
     //       if (!tp.dmApproved()) {
     //           pstmt = aConnection.prepareStatement(updateProjectDMApproved);
     //       } else if (!tp.zmApproved()) {
     //           pstmt = aConnection.prepareStatement(updateProjectZMApproved);
     //       } else {
     //           pstmt = aConnection.prepareStatement(updateProjectMMApproved);
     //       }
            if (tp.waitingForDM()){
                pstmt = aConnection.prepareStatement(updateProjectDMApproved);
            } else if (tp.waitingForProjectSalesMgr()){
                pstmt = aConnection.prepareStatement(updateProjectPSMApproved);
            } else if (tp.waitingForChampsMgr()){
                pstmt = aConnection.prepareStatement(updateProjectCHAMPSMgrApproved);
            }
            pstmt.setString(1,user.getUserid());
            pstmt.setInt(2,tp.getId());
            pstmt.executeUpdate();

        } catch (Exception e) {
            SMRCLogger.error("ProjectDAO.approveProject() ",e);
            throw e;
        } finally {
            SMRCConnectionPoolUtils.close(pstmt);
        }

    } //method

	
}
