
package com.eaton.electrical.smrc.dao;

import java.sql.*;
import java.util.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

/**
 * @author E0062708
 *
 * TODO To change the template for this generated type comment go to
 */
public class ProductModuleDAO {
	
	public static final String MODULE_CHANGE_VISTA_STATUS_IN_VISTA = "In Vista";
	public static final String MODULE_CHANGE_VISTA_STATUS_APPROVED = "Approved";
	public static final String MODULE_CHANGE_VISTA_STATUS_IN_PROCESS = "In Process";
	public static final String MODULE_CHANGE_VISTA_STATUS_REJECTED = "Rejected";
	
	
	private static final String getProductModulesQuery = "select * from products"+
		" where product_id in (select distinct product_id from product_load_modules)"+
		" and period_yyyy=? order by seq_num";
	private static final String getLoadModulesQuery = "select * from product_load_modules a, load_modules b"+
		" where a.module_id=b.module_id"+
		" and a.product_id=?"+
		" order by a.module_id";
	private static final String deleteProductModules = "DELETE FROM DISTRIB_PRODUCT_MODULES WHERE DISTRIBUTOR_ID=?";
	private static final String insertProductModules = "INSERT INTO DISTRIB_PRODUCT_MODULES (DISTRIBUTOR_ID,MODULE_ID,DATE_CHANGED) VALUES (?,?,SYSDATE)";
	private static final String getSelectedModulesQuery="SELECT b.* FROM DISTRIB_PRODUCT_MODULES a, load_modules b WHERE a.module_id=b.module_id and DISTRIBUTOR_ID=?";
	
	private static final String getModuleChangeVistaStatusId = "select * from module_change_vista_status t where t.module_change_vista_status = ?";
	private static final String getModuleChangeVistaStatusName = "select * from module_change_vista_status t where t.module_change_vista_status_id = ?";
	private static final String insertModuleChangeRequest = "insert into module_change_requests (module_change_requests_id,distributor_id,user_added,date_added) values (?,?,?,sysdate)";
	private static final String getModuleChangeRequestNextval = "select module_change_requests_id_seq.nextval from dual";
	private static final String updateModuleChangeRequest = "update module_change_requests mcr set mcr.user_changed = ?, mcr.date_changed = sysdate " +
			" where mcr.module_change_requests_id = ?";
	private static final String getModuleChangeRequest = "select * from module_change_requests mcr where mcr.module_change_requests_id = ?";
	
	private static final String insertModuleChangeRequestProduct = "insert into module_change_request_prod (module_change_request_prod_id,module_change_requests_id,module_id,action," +
			"  module_change_vista_status_id,user_added,date_added,user_changed,date_changed) values (?,?,?,?,?,?,?,?,?)";
	private static final String getModuleChangeProductNextval = "select module_change_req_prod_id_seq.nextval from dual";
	private static final String deleteAllProductsForRequest = "delete module_change_request_prod mcrp where mcrp.module_change_requests_id = ?";
	private static final String getModuleChangeProduct = "select mcrp.*, lm.module_name, lm.module_code, lm.module_short_code from module_change_request_prod mcrp, load_modules lm  " +
			" where mcrp.module_change_requests_id = ? and lm.module_id = mcrp.module_id (+) ";
	private static final String updateAllProductsForModule = "update module_change_request_prod mcr " +
			" set mcr.module_change_vista_status_id = ?, mcr.date_changed = sysdate, mcr.user_changed = ? " +
			" where mcr.module_change_requests_id = ?";
	
	
	private static final String insertModuleChangeReasonNotes = "insert into module_change_reasons (module_change_reasons_id,module_change_requests_id,change_reason,user_added,date_added)" +
			" values (module_change_req_prod_id_seq.nextval,?,?,?,sysdate)";
	private static final String getModuleChangeReasonNote = "select * from module_change_reasons n where n.module_change_reasons_id = ?";
	private static final String getModuleChangeReasonNotesForRequest = "select n.module_change_reasons_id from module_change_reasons n where n.module_change_requests_id = ? order by n.date_added asc";
	
	public static ArrayList getProductModules(int srYear, Connection DBConn) throws Exception{

		ArrayList modules = new ArrayList();
		ArrayList outModules = new ArrayList();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		
		try {
			pstmt = DBConn.prepareStatement(getProductModulesQuery);
			pstmt.setInt(1,srYear);
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				ProductModule productModule = new ProductModule();
				productModule.setProductId(StringManipulation.noNull(rs.getString("PRODUCT_ID")));
				productModule.setProductDescription(StringManipulation.noNull(rs.getString("PRODUCT_DESCRIPTION")));
				if(StringManipulation.noNull(rs.getString("SP_LOAD_TOTAL")).equals("S")){
					productModule.setSummaryLine(true);
				}else{
					productModule.setSummaryLine(false);
				}
				modules.add(productModule);
			}
			
			for(int i=0;i<modules.size();i++){
				ProductModule productModule = (ProductModule)modules.get(i);
				pstmt1 = DBConn.prepareStatement(getLoadModulesQuery);
				pstmt1.setString(1,productModule.getProductId());
				rs1 = pstmt1.executeQuery();
				ArrayList loadModules = new ArrayList();
				while (rs1.next())
				{
					LoadModule loadModule = new LoadModule();
					loadModule.setModuleId(rs1.getInt("MODULE_ID"));
					loadModule.setProductModuleCode(StringManipulation.noNull(rs1.getString("PRODUCT_MODULE_CODE")));
					if(StringManipulation.noNull(rs1.getString("REQUIRED_MODULE")).equals("Y")){
						loadModule.setRequired(true);
					}else{
						loadModule.setRequired(false);
					}
					loadModule.setModuleName(StringManipulation.noNull(rs1.getString("MODULE_NAME")));
					loadModule.setModuleCode(StringManipulation.noNull(rs1.getString("MODULE_CODE")));
					loadModule.setModuleShortCode(StringManipulation.noNull(rs1.getString("MODULE_SHORT_CODE")));
					loadModule.setUrl(StringManipulation.noNull(rs1.getString("URL")));
					loadModules.add(loadModule);
				}
				SMRCConnectionPoolUtils.close(rs1);
				SMRCConnectionPoolUtils.close(pstmt1);
				
				productModule.setLoadModules(loadModules);
				outModules.add(productModule);
				
			}

		}catch (Exception e)	{
			SMRCLogger.error("ProductModuleDAO.getProductModules(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(rs1);
			SMRCConnectionPoolUtils.close(pstmt);
			SMRCConnectionPoolUtils.close(pstmt1);

		}
		
		return outModules;	
	}
	
	public static void saveModules(String acctId, String[] moduleIds, Connection DBConn) throws Exception{
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		//DistributorDAO.initializeDistributor(acctId, DBConn);
		Distributor dist = DistributorDAO.getDistributor(acctId, DBConn);
		

		try {
			pstmt = DBConn.prepareStatement(deleteProductModules);
			pstmt.setInt(1,dist.getId());
			pstmt.executeUpdate();
			//SMRCLogger.debug(count + " records deleted");
			pstmt1 = DBConn.prepareStatement(insertProductModules);
			if(moduleIds!=null){
				for(int i=0;i<moduleIds.length;i++){
					//SMRCLogger.debug("dist-" + dist.getId() + ", moduleId-"+moduleIds[i]);
					pstmt1.setInt(1,dist.getId());
					pstmt1.setInt(2,Globals.a2int(moduleIds[i]));
					pstmt1.executeUpdate();
				}
			}
	
		}catch (Exception e)	{
			SMRCLogger.error("ProductModuleDAO.saveModules(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(pstmt);
			SMRCConnectionPoolUtils.close(pstmt1);
		}
		
	}

	
	public static ArrayList getSelectedModules(int distId, Connection DBConn) throws Exception{
		ArrayList modules = new ArrayList();

		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DBConn.prepareStatement(getSelectedModulesQuery);
			pstmt.setInt(1,distId);
			rs = pstmt.executeQuery();

			while (rs.next())
			{
				LoadModule mod = new LoadModule();
				mod.setModuleId(rs.getInt("MODULE_ID"));
				if(StringManipulation.noNull(rs.getString("REQUIRED_MODULE")).equals("Y")){
					mod.setRequired(true);
				}else{
					mod.setRequired(false);
				}
				mod.setModuleName(StringManipulation.noNull(rs.getString("MODULE_NAME")));
				mod.setModuleCode(StringManipulation.noNull(rs.getString("MODULE_CODE")));
				mod.setModuleShortCode(StringManipulation.noNull(rs.getString("MODULE_SHORT_CODE")));
				modules.add(mod);
			}
			

		}catch (Exception e)	{
			SMRCLogger.error("ProductModuleDAO.getSelectedModules(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return modules;

	}
	
	/**
	 * This method returns the primary key (MODULE_CHANGE_VISTA_STATUS_ID) for the vista status passed in.
	 * @param vistaStatus  - use the constants defined in this class
	 * @param DBConn
	 * @return
	 * @throws Exception
	 */
	public static long getModuleChangeVistaStatusId (String vistaStatus, Connection DBConn) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long id = 0;
		try {
			pstmt = DBConn.prepareStatement(getModuleChangeVistaStatusId);
			pstmt.setString(1,vistaStatus);
			rs = pstmt.executeQuery();
			while (rs.next()){
				id = rs.getLong("MODULE_CHANGE_VISTA_STATUS_ID");
			}
		}catch (Exception e)	{
			SMRCLogger.error("ProductModuleDAO.getModuleChangeVistaStatusId(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return id;
		
	}
	
	/**
	 * This method returns the vista status name (MODULE_CHANGE_VISTA_STATUS_ID) for the vista status id passed in.
	 * @param vistaStatusId 
	 * @param DBConn
	 * @return
	 * @throws Exception
	 */
	public static String getModuleChangeVistaStatusName (long vistaStatusId, Connection DBConn) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String name = null;
		try {
			pstmt = DBConn.prepareStatement(getModuleChangeVistaStatusName);
			pstmt.setLong(1,vistaStatusId);
			rs = pstmt.executeQuery();
			while (rs.next()){
				name = rs.getString("MODULE_CHANGE_VISTA_STATUS");
			}
		}catch (Exception e)	{
			SMRCLogger.error("ProductModuleDAO.getModuleChangeVistaStatusName(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		return name;
		
	}
	
	private static long[] getModuleChangeRequestIdsInStatus (long distributorId, String[] vistaStatuses, Connection DBConn) throws Exception {
		long[] requestIds = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		query.append("select distinct mcr.module_change_requests_id ");
		query.append(" from module_change_requests mcr, module_change_request_prod prod, module_change_vista_status stat ");
		query.append(" where mcr.distributor_id = ");
		query.append(distributorId);
		query.append(" and mcr.module_change_requests_id = prod.module_change_requests_id ");
		query.append(" and prod.module_change_vista_status_id = stat.module_change_vista_status_id ");
		query.append(" and stat.module_change_vista_status in (");
		for (int i=0; i < vistaStatuses.length; i++){
			if (i > 0){
				query.append(", ");
			}
			query.append("'" + vistaStatuses[i] + "'");
		}
		query.append(") ");
		query.append(" order by mcr.module_change_requests_id desc");
		SMRCLogger.debug("ProductModuleDAO.countModuleChangeRequestsInStatus() query: " + query.toString());
		
		try {
			stmt = DBConn.createStatement();
			rs = stmt.executeQuery(query.toString());
			ArrayList idList = new ArrayList();
			while (rs.next()){
				idList.add(new Long(rs.getLong(1)));
			}
			int count = idList.size();
			SMRCLogger.debug("count of idList: " + count);
			requestIds = new long[count];
			for (int i=0; i < count; i++){
				requestIds[i] = ((Long) idList.get(i)).longValue();
			}
		}catch (Exception e)	{
			SMRCLogger.error("ProductModuleDAO.countModuleChangeRequestsInStatus(): ", e);
			throw e;
		}
		finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(stmt);
		}
		SMRCLogger.debug("requestIds: " + requestIds.length);
		return requestIds;
	}
	
	public static boolean distributorHasCurrentModuleChangeRequest (long distributorId, Connection DBConn) throws Exception {
		boolean hasCurrentRequests = false;
		String[] vistaStatuses = new String[2];
		vistaStatuses[0] = ProductModuleDAO.MODULE_CHANGE_VISTA_STATUS_IN_PROCESS;
		vistaStatuses[1] = ProductModuleDAO.MODULE_CHANGE_VISTA_STATUS_APPROVED;
		long[] requestIds = ProductModuleDAO.getModuleChangeRequestIdsInStatus(distributorId,vistaStatuses,DBConn);
		if (requestIds.length > 0){
			hasCurrentRequests = true;
		}
		SMRCLogger.debug("ProductModuleDAO.distributorHasCurrentModuleChangeRequest() :: hasCurrentRequests = " + hasCurrentRequests);
		return hasCurrentRequests;
	}

	public static boolean canUpdateDistributorModuleChangeRequest (long distributorId, Connection DBConn) throws Exception {
		boolean canUpdateRequests = false;
		String[] vistaStatuses = new String[1];
		vistaStatuses[0] = ProductModuleDAO.MODULE_CHANGE_VISTA_STATUS_IN_PROCESS;
		long[] requestIds = ProductModuleDAO.getModuleChangeRequestIdsInStatus(distributorId,vistaStatuses,DBConn);
		if (requestIds.length > 0){
			canUpdateRequests = true;
		}
		SMRCLogger.debug("ProductModuleDAO.canUpdateDistributorModuleChangeRequest() :: canUpdateRequests = " + canUpdateRequests);
		return canUpdateRequests;
	}
	
	public static ModuleChangeRequest saveOrUpdateModuleChangeRequest(ModuleChangeRequest moduleChangeRequest ,Connection DBConn) throws Exception {
		SMRCLogger.debug("saveOrUpdateModuleChangeRequest");
		if (ProductModuleDAO.distributorHasCurrentModuleChangeRequest(moduleChangeRequest.getDistributorId(),DBConn)){
			ProductModuleDAO.updateModuleChangeRequest(moduleChangeRequest,DBConn);
		} else {
			long requestId = ProductModuleDAO.insertModuleChangeRequest(moduleChangeRequest,DBConn);
			moduleChangeRequest.setId(requestId);
		}
		return moduleChangeRequest;
	}
	
	private static long insertModuleChangeRequest (ModuleChangeRequest moduleChangeRequest, Connection DBConn) throws Exception {
		SMRCLogger.debug("insertModuleChangeRequest");
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		ResultSet rs = null;
		long requestId = 0;
		try {
			pstmt = DBConn.prepareStatement(getModuleChangeRequestNextval);
			rs = pstmt.executeQuery();
			if (rs.next()){
				requestId = rs.getLong(1);
			}
			if (requestId == 0){
				throw new Exception ("nextVal was not retrieved from module_change_requests_id_seq");
			}
			pstmt1 = DBConn.prepareStatement(insertModuleChangeRequest);
			pstmt1.setLong(1,requestId);
			pstmt1.setLong(2,moduleChangeRequest.getDistributorId());
			pstmt1.setString(3,moduleChangeRequest.getUserAdded());
			pstmt1.executeUpdate();
			
		} catch (Exception e) {
			SMRCLogger.error("Exception in ProductModuleDAO.insertModuleChangeRequest()" + e, e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
			SMRCConnectionPoolUtils.close(pstmt1);
		}
		return requestId;
		
		
	}
	
	private static void updateModuleChangeRequest (ModuleChangeRequest moduleChangeRequest, Connection DBConn) throws Exception {
		SMRCLogger.debug("updateModuleChangeRequest");		
		PreparedStatement pstmt = null;
		try {
			pstmt = DBConn.prepareStatement(updateModuleChangeRequest);
			pstmt.setString(1,moduleChangeRequest.getUserChanged());
			pstmt.setLong(2, moduleChangeRequest.getId());
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			SMRCLogger.error("Exception in ProductModuleDAO.updateModuleChangeRequest()" + e, e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
	}
	
	public static ModuleChangeRequest getModuleChangeRequest(long moduleChangeRequestId, Connection DBConn) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ModuleChangeRequest moduleChangeRequest = null;
		try {
			pstmt = DBConn.prepareStatement(getModuleChangeRequest);
			pstmt.setLong(1,moduleChangeRequestId);
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				moduleChangeRequest = new ModuleChangeRequest();
				moduleChangeRequest.setDateAdded(rs.getDate("date_added"));
				moduleChangeRequest.setDateChanged(rs.getDate("date_changed"));
				moduleChangeRequest.setDistributorId(rs.getLong("distributor_id"));
				moduleChangeRequest.setId(rs.getLong("module_change_requests_id"));
				moduleChangeRequest.setUserAdded(rs.getString("user_added"));
				moduleChangeRequest.setUserChanged(rs.getString("user_changed"));
				moduleChangeRequest.setModuleChangeReasonNotes(ProductModuleDAO.getModuleChangeReasonNotesForRequest(moduleChangeRequestId,DBConn));
			}
		} catch (Exception e) {
			SMRCLogger.error("Exception in ProductModuleDAO.getModuleChangeRequest()" + e, e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return moduleChangeRequest;
		
	}
	
	public static ModuleChangeRequest getCurrentModuleChangeRequest (long distributorId, Connection DBConn) throws Exception {
		String[] vistaStatuses = new String [2];
		vistaStatuses[0] = ProductModuleDAO.MODULE_CHANGE_VISTA_STATUS_IN_PROCESS;
		vistaStatuses[1] = ProductModuleDAO.MODULE_CHANGE_VISTA_STATUS_APPROVED;
		long[] requestIds = ProductModuleDAO.getModuleChangeRequestIdsInStatus(distributorId,vistaStatuses,DBConn);
		if (requestIds.length > 1){
			throw new Exception("Too many current module change requests returned : " + requestIds.length);
		} else if (requestIds.length == 0){
			return null;
		}
		
		ModuleChangeRequest moduleChangeRequest = ProductModuleDAO.getModuleChangeRequest(requestIds[0],DBConn);
		
		return moduleChangeRequest;
	}
	
	public static ModuleChangeRequest[] getModuleChangeRequestHistory (long distributorId, Connection DBConn) throws Exception {
		String[] vistaStatuses = new String [4];
		vistaStatuses[0] = ProductModuleDAO.MODULE_CHANGE_VISTA_STATUS_IN_VISTA;
		vistaStatuses[1] = ProductModuleDAO.MODULE_CHANGE_VISTA_STATUS_REJECTED;
		vistaStatuses[2] = ProductModuleDAO.MODULE_CHANGE_VISTA_STATUS_APPROVED;
		vistaStatuses[3] = ProductModuleDAO.MODULE_CHANGE_VISTA_STATUS_IN_PROCESS;
		long[] requestIds = ProductModuleDAO.getModuleChangeRequestIdsInStatus(distributorId,vistaStatuses,DBConn);
		
		if (requestIds == null || requestIds.length == 0){
			return null;
		}
		
		ModuleChangeRequest[] moduleChangeRequests = new ModuleChangeRequest[requestIds.length];
		
		for (int i=0; i < requestIds.length; i++){
			moduleChangeRequests[i] = ProductModuleDAO.getModuleChangeRequest(requestIds[i],DBConn);
		}
		
		return moduleChangeRequests;
	}
	
	public static long updateModuleChangeProduct (ModuleChangeProduct moduleChangeProduct, Connection DBConn) throws Exception {
		
		SMRCLogger.debug("insertModuleChangeProduct");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt1 = null;
		
		long nextvalId = 0;
		try {
			pstmt = DBConn.prepareStatement(getModuleChangeProductNextval);
			rs = pstmt.executeQuery();
			if (rs.next()){
				nextvalId = rs.getLong(1);
			}
			if (nextvalId == 0){
				throw new Exception ("nextVal was not retrieved from module_change_req_prod_id_seq");
			}
			pstmt1 = DBConn.prepareStatement(insertModuleChangeRequestProduct);
			pstmt1.setLong(1,nextvalId);
			pstmt1.setLong(2,moduleChangeProduct.getModuleChangeRequestId());
			pstmt1.setLong(3,moduleChangeProduct.getModuleId());
			pstmt1.setString(4,moduleChangeProduct.getAction());
			pstmt1.setLong(5,moduleChangeProduct.getVistaStatusId());
			// Even though we are inserting, we want to keep the user/date added of any previous
			// records since we are deleting all old records with each update
			pstmt1.setString(6,moduleChangeProduct.getUserAdded());
			pstmt1.setTimestamp(7,new java.sql.Timestamp(moduleChangeProduct.getDateAdded().getTime()));
			pstmt1.setString(8,moduleChangeProduct.getUserChanged());
			pstmt1.setTimestamp(9,new java.sql.Timestamp(moduleChangeProduct.getDateChanged().getTime()));
			pstmt1.executeUpdate();
			
		} catch (Exception e) {
			SMRCLogger.error("Exception in ProductModuleDAO.insertModuleChangeProduct()" + e, e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
			SMRCConnectionPoolUtils.close(pstmt1);
		}
		return nextvalId;
		
		
	}
	
	public static void deleteExistingProducts(long moduleChangeRequestId ,Connection DBConn) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = DBConn.prepareStatement(deleteAllProductsForRequest);
			pstmt.setLong(1,moduleChangeRequestId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			SMRCLogger.error("Exception in ProductModuleDAO.deleteExistingProducts()" + e, e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
	}
	
	public static void updateAllProductsForModule(long moduleChangeRequestId, long productStatusId, User user, Connection DBConn) throws Exception {
		
		PreparedStatement pstmt = null;
		try {
			pstmt = DBConn.prepareStatement(updateAllProductsForModule);
			pstmt.setLong(1,productStatusId);
			pstmt.setString(2,user.getUserid());
			pstmt.setLong(3,moduleChangeRequestId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			SMRCLogger.error("Exception in ProductModuleDAO.updateExistingProducts()" + e, e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
	}
	
	public static ModuleChangeProduct[] getModuleChangeProductsForRequest(long moduleChangeRequestId, Connection DBConn) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ModuleChangeProduct[] products = null;
		try {
			pstmt = DBConn.prepareStatement(getModuleChangeProduct);
			pstmt.setLong(1,moduleChangeRequestId);
			rs = pstmt.executeQuery();
			ArrayList productList = new ArrayList();
			while (rs.next()){
				ModuleChangeProduct product = new ModuleChangeProduct();
				product.setAction(rs.getString("action"));
				product.setDateAdded(rs.getTimestamp("date_added"));
				product.setDateChanged(rs.getTimestamp("date_changed"));
				product.setId(rs.getLong("module_change_request_prod_id"));
				product.setModuleChangeRequestId(rs.getLong("module_change_requests_id"));
				product.setModuleId(rs.getLong("module_id"));
				product.setUserAdded(rs.getString("user_added"));
				product.setUserChanged(rs.getString("user_changed"));
				product.setVistaStatusId(rs.getLong("module_change_vista_status_id"));
				product.setLoadingModuleCode(rs.getString("module_code"));
				product.setLoadingModuleName(rs.getString("module_name"));
				product.setLoadingModuleShortCode(rs.getString("module_short_code"));
				productList.add(product);				
			}
			int listSize = productList.size();
			products = new ModuleChangeProduct[listSize];
			for (int i=0; i < listSize; i++){
				products[i] = (ModuleChangeProduct) productList.get(i);
			}
		} catch (Exception e) {
			SMRCLogger.error("Exception in ProductModuleDAO.getModuleChangeProductsForRequest()" + e, e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return products;
		
	}
	
	public static void insertModuleChangeReasonNote (ModuleChangeReasonNotes reasonNote, Connection DBConn) throws Exception {
		
		PreparedStatement pstmt = null;
		
		try {
			pstmt = DBConn.prepareStatement(insertModuleChangeReasonNotes);
			pstmt.setLong(1,reasonNote.getModuleChangeRequestId());
			pstmt.setString(2,reasonNote.getReasonNotes());
			pstmt.setString(3,reasonNote.getUserAdded());
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			SMRCLogger.error("Exception in ProductModuleDAO.insertModuleChangeReasonNote()" + e, e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
	}
	
	public static ModuleChangeReasonNotes getModuleChangeReasonNotesById (long moduleChangeReasonNotesId, Connection DBConn) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ModuleChangeReasonNotes notes = null;
		try {
			pstmt = DBConn.prepareStatement(getModuleChangeReasonNote);
			pstmt.setLong(1,moduleChangeReasonNotesId);
			rs = pstmt.executeQuery();
			while (rs.next()){
				notes = new ModuleChangeReasonNotes();
				notes.setDateAdded(rs.getTimestamp("date_added"));
				notes.setDateChanged(rs.getTimestamp("date_changed"));
				notes.setId(rs.getLong("module_change_reasons_id"));
				notes.setModuleChangeRequestId(rs.getLong("module_change_requests_id"));
				notes.setReasonNotes(rs.getString("change_reason"));
				notes.setUserAdded(rs.getString("user_added"));
				notes.setUserChanged(rs.getString("user_changed"));	
				notes.setUserAddedName(UserDAO.getUserName(notes.getUserAdded(),DBConn));
			}
			
		} catch (Exception e) {
			SMRCLogger.error("Exception in ProductModuleDAO.getModuleChangeReasonNotesById()" + e, e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return notes;
	}
	
	public static ModuleChangeReasonNotes[] getModuleChangeReasonNotesForRequest (long moduleChangeRequestId, Connection DBConn) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ModuleChangeReasonNotes[] notes = null;
		try {
			pstmt = DBConn.prepareStatement(getModuleChangeReasonNotesForRequest);
			pstmt.setLong(1,moduleChangeRequestId);
			rs = pstmt.executeQuery();
			ArrayList noteList = new ArrayList();
			while (rs.next()){
				ModuleChangeReasonNotes note = ProductModuleDAO.getModuleChangeReasonNotesById(rs.getLong(1),DBConn);
				noteList.add(note);
			}
			int count = noteList.size();
			notes = new ModuleChangeReasonNotes[count];
			for (int i=0; i < count; i++){
				notes[i] = (ModuleChangeReasonNotes) noteList.get(i);
			}
			
		} catch (Exception e) {
			SMRCLogger.error("Exception in ProductModuleDAO.getModuleChangeReasonNotesForRequest()" + e, e);
			throw e;
		} finally {
			SMRCConnectionPoolUtils.close(rs);
			SMRCConnectionPoolUtils.close(pstmt);
		}
		
		return notes;
	}
	
	
	
}
