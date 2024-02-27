//Copyright (c) 2004 Eaton, Inc.
//
// $Log: not supported by cvs2svn $
// Revision 1.42  2006/06/05 17:14:27  e0073445
// 4.2.2 - CSF changes for tap dollars
//
// Revision 1.41  2006/03/30 22:21:17  e0073445
// Tap Dollars : Changes for 4.2.3 along with a lot of cleanup including sublines and removing old dollar types that are no longer used
//
// Revision 1.40  2006/03/22 18:13:26  e0073445
// *** empty log message ***
//
// Revision 1.39  2006/03/16 19:13:31  e0073445
// Removed code that was producing warnings.
//
// Revision 1.38  2005/05/24 15:00:55  lubbejd
// Removed System.out.println.
//
// Revision 1.37  2005/04/27 15:23:08  lubbejd
// Changes for creating CSFs at the district level instead of the division level.
// (CR29410)
//
// Revision 1.36  2005/04/25 19:08:28  lubbejd
// More changes to change the reported dollars on the CSF screens to
// user the default dollar type instead of credit dollars (CR29410).
//
// Revision 1.35  2005/04/22 17:45:35  lubbejd
// Continued changes for assigning csfs to districts, and assigning
// a status to the district.  (CR29410)
//
// Revision 1.34  2005/04/21 19:15:28  lubbejd
// Started changes for assigning csfs to districts. (CR29410)
//
// Revision 1.33  2005/04/20 19:20:22  lubbejd
// Changed CSF division queries to use charge/end market dollars instead
// of using credit dollars. Not done yet (CR30288).
//
// Revision 1.32  2005/02/21 19:39:09  lubbejd
// Changes to allow Division Managers to update CSF notes, and
// display names of users adding/changing CSFs (CR28942 - 4.1.1)
//
// Revision 1.31  2005/01/12 04:50:43  schweks
// Changed to use Globals.a2int instead.
//
// Revision 1.30  2005/01/10 03:00:24  schweks
// Set the default for sFwdUrl to SMRCErrorPage in case an error occurs in securedocs.
//
// Revision 1.29  2005/01/09 05:59:55  schweks
// Added check to see if user is null before trying to stick user info in the log file.
//
// Revision 1.28  2005/01/06 15:33:50  lubbejd
// Changes to use Years table instead of sysdate
//
// Revision 1.27  2005/01/05 22:40:25  vendejp
// declared the user object at the class level and changed the catches to include the ProfileException handling
//
// Revision 1.26  2004/12/23 18:12:51  vendejp
// Changes to connection objects and cleaned up reports look and feel
//
// Revision 1.25  2004/11/19 00:00:58  vendejp
// fixed bug  -  had submit button that wasnt needed
//
// Revision 1.24  2004/11/02 19:04:00  lubbejd
// Finished number crunching for division csf
//
// Revision 1.23  2004/11/02 14:25:08  lubbejd
// Changes for Division CSF
//
// Revision 1.22  2004/10/30 22:52:43  vendejp
// Changes to accomodate header requirements.  Made User and Account objects set in the header object where I could.
//
// Revision 1.21  2004/10/30 20:58:54  lubbejd
// add logic for retrieving sales/orders data
//
// Revision 1.20  2004/10/19 14:51:03  schweks
// Removing unused variables and code.
//
// Revision 1.19  2004/10/16 18:14:59  manupvj
// Added abstract servlet for common functions and common point of logging.
//
// Revision 1.18  2004/10/14 17:40:51  schweks
// Added Eaton header comment.
// Reformatted source code.
//

package com.eaton.electrical.smrc;

import java.io.*;
import java.sql.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.eaton.electrical.smrc.bo.*;
import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.exception.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

public class CSF extends SMRCBaseServlet {

	private static final long serialVersionUID = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	    String sFwdUrl="/SMRCErrorPage.jsp";
		boolean redirect=false;
		Connection DBConn = null;
		User user = null;
		HttpSession session;
		  
		try{    

			DBConn = SMRCConnectionPoolUtils.getDatabaseConnection() ;
			
			user = SMRCSession.getUser(request,DBConn);
			
			String page = request.getParameter("page");	
			
			SMRCHeaderBean hdr = new SMRCHeaderBean();
			hdr.setUser(user);
			request.setAttribute("header", hdr);
			session = request.getSession( false );
			int srYear = Globals.a2int((String) session.getAttribute("srYear"));
			int srMonth = Globals.a2int((String) session.getAttribute("srMonth"));
			
			if(page == null){
				page = "Division";
			}
			
			if(page.equalsIgnoreCase("District")){
				String salesOrders = (String)request.getParameter("salesOrders");
				if (salesOrders == null){
					salesOrders = "invoice";
				}
				Geography geog = MiscDAO.getGeography(request.getParameter("district"), DBConn);
				ArrayList divisions = DivisionDAO.getDivisions(DBConn);
				ArrayList returnDivisions = new ArrayList();
				ArrayList reportBeans = CSFDAO.getSalesForDistrictCSF(geog.getGeog(), salesOrders, srYear, srMonth, user, DBConn);
				
				for (int i=0; i < divisions.size(); i++) {
					Division division = (Division)divisions.get(i);
					ArrayList csfs = CSFDAO.getCSFsForDivisionAndDistrict(division.getId(),geog.getGeog(),DBConn);
					//ArrayList csfs = CSFDAO.getCSFsForDistrict(division.getId(), request.getParameter("district"), DBConn);
					division.setCSFs(csfs);
					returnDivisions.add(division);
				}
				
				request.setAttribute("geog", geog);
				request.setAttribute("divisions", returnDivisions);
				request.setAttribute("salesOrders", salesOrders);
				request.setAttribute("reportBeans", reportBeans);
				sFwdUrl="/CSFDistrictDisplay.jsp";
				
			}else if(page.equalsIgnoreCase("editDistrictCSF") || page.equalsIgnoreCase("saveNote")){
				String saveMsg="";
				if(page.equalsIgnoreCase("saveNote")){
					CSFDAO.saveNote(request.getParameter("district"),request.getParameter("csfid"),StringManipulation.noNull(request.getParameter("NOTE")), user.getUserid(), DBConn);
					saveMsg="New Critical Success Factor Note Added.";
				}
				
				Geography geog = MiscDAO.getGeography(request.getParameter("district"), DBConn);

// Braffet
				CriticalSuccessFactor csf = CSFDAO.getCSF(request.getParameter("csfid"),request.getParameter("district"), DBConn);
				String from = null;
				String divId = null;
				if (request.getParameter("from") != null){
				    from = request.getParameter("from");
				}
				if (request.getParameter("divId") != null){
				    divId = request.getParameter("divId");
				}
				request.setAttribute("csf", csf);
				request.setAttribute("geog", geog);
				request.setAttribute("saveMsg", saveMsg);
				request.setAttribute("from", from);
				request.setAttribute("divId", divId);
				
				sFwdUrl="/editDistrictCSF.jsp";			
				
			}else if(page.equalsIgnoreCase("Division") || page.equalsIgnoreCase("saveNew") || page.equalsIgnoreCase("deactivate") || page.equalsIgnoreCase("updateColor")){
				String salesOrders = request.getParameter("salesOrders");
				if (salesOrders == null){
					salesOrders = "invoice";
				}
				String divisionId = request.getParameter("divisionId");
				String saveMsg="";
				if(page.equalsIgnoreCase("saveNew")){

// Braffet					
					int divCSFId = CSFDAO.newCSF(divisionId, request.getParameter("CSF"), user.getUserid(), DBConn);
					
				    String[] districts = request.getParameterValues("csfDistricts");
					boolean allSelected = false;
					for (int i=0; i< districts.length; i++){
					    if (districts[i].equalsIgnoreCase("All")){
					        allSelected = true;
					//        break;
					    }
					}
					if (allSelected){
					    ArrayList districtList = new ArrayList();

// Braffet					    
					    districtList = CSFDAO.getAvailableDistrictsForDivisionCSF(Globals.a2int(divisionId),DBConn);
					    for (int i=0; i < districtList.size(); i++){
					        DropDownBean bean = (DropDownBean) districtList.get(i);
					        if (bean.getValue().equalsIgnoreCase("All")){
					            continue;
					        } else {
					            CSFDAO.addDistrictToCSFNotes(bean.getValue(),divCSFId,user.getUserid(),DBConn);
					        }
					    }
					} else {
					    for (int i=0; i< districts.length; i++){
					        CSFDAO.addDistrictToCSFNotes(districts[i],divCSFId,user.getUserid(),DBConn);
					    }
					}
					saveMsg="New Critical Success Factor Added.";
					
				}else if(page.equalsIgnoreCase("deactivate")){
					CSFDAO.changeCSFStatus(request.getParameter("csfid"), "N", user.getUserid(), DBConn);
					saveMsg="Critical Success Factor deactivated.";
					
				}else if(page.equalsIgnoreCase("updateColor")){
					CSFDAO.changeCSFColor(request.getParameter("CSF_ID"), request.getParameter("DISTRICT"), request.getParameter("COLOR"), user.getUserid(), DBConn);
					saveMsg="Critical Success Factor Updated.";
				}
				
				//ArrayList districts = DistrictDAO.getAllDistricts(DBConn);
				//request.setAttribute("districts", districts);
				// Get all districts and zones beginning with 145
				ArrayList geogs = DistrictDAO.getDistrictsAndZonesLike("145", DBConn);
				request.setAttribute("geogs", geogs);
				request.setAttribute("saveMsg", saveMsg);
// Braffet
				ArrayList csfs = CSFDAO.getCSFsForDivision(divisionId, DBConn);
				request.setAttribute("csfs", csfs);
				request.setAttribute("division", DivisionDAO.getDivision(divisionId, DBConn));
				ArrayList reportBeans = CSFDAO.getSalesForDivisionCSF(Globals.a2int(divisionId), salesOrders, srYear, srMonth, user, DBConn);
				request.setAttribute("reportBeans", reportBeans);
				request.setAttribute("salesOrders", salesOrders);
				sFwdUrl="/CSFDivisionDisplay.jsp";
				
			}else if(page.equalsIgnoreCase("noteStatus")){
				String saveMsg=null;
				CSFDAO.changeNoteStatus(request.getParameter("csfnoteid"), "N", user.getUserid(), DBConn);
				saveMsg="Critical Success Factor Note deactivated.";
				
				Geography geog = MiscDAO.getGeography(request.getParameter("district"), DBConn);

// Braffet				
				CriticalSuccessFactor csf = CSFDAO.getCSF(request.getParameter("csfid"),request.getParameter("district"), DBConn);
	
				request.setAttribute("csf", csf);
				request.setAttribute("geog", geog);
				request.setAttribute("saveMsg", saveMsg);
				
				sFwdUrl="/editDistrictCSF.jsp";	
				
				
			}else if(page.equalsIgnoreCase("add")){
			    int divisionId = Globals.a2int(request.getParameter("divisionId"));
			    ArrayList districtList = new ArrayList();
// Braffet
			    districtList = CSFDAO.getAvailableDistrictsForDivisionCSF(divisionId,DBConn);
			    request.setAttribute("districtList", districtList);
				sFwdUrl="/CSFNewDisplay.jsp";			
			}else{
				sFwdUrl="/CSFNewDisplay.jsp";
				
			}
			
			SMRCConnectionPoolUtils.commitTransaction(DBConn);
        } catch (ProfileException pe) {
        	// See if we can get the user info for the log message.
        	if ( user != null ) {
	        	// Put as much on the first line as possible for NetCool
	            SMRCLogger.warn(this.getClass().getName() + 
	            	".doGet(): USERID=" + user.getUserid() + 
					"; MESSAGE=" + pe.getMessage() + 
					"\nUSER:" + user, pe);
        	} else {
	        	// Put as much on the first line as possible for NetCool
	            SMRCLogger.warn(this.getClass().getName() + 
	            	".doGet(): MESSAGE=" + pe.getMessage(), pe);
        	}
        	
            SMRCConnectionPoolUtils.rollbackTransaction(DBConn);

            request.setAttribute("exception", pe.getMessage());
            sFwdUrl = "/SMRCErrorPage.jsp";
            redirect = false;

        } catch (Exception e) {
        	// See if we can get the user info for the log message.
        	if ( user != null ) {
	        	// Put as much on the first line as possible for NetCool
	            SMRCLogger.error(this.getClass().getName() + 
	                	".doGet(): USERID=" + user.getUserid() + 
	    				"; MESSAGE=" + e.getMessage() + 
	    				"\nUSER:" + user, e);
        	} else {
	        	// Put as much on the first line as possible for NetCool
	            SMRCLogger.error(this.getClass().getName() + 
	            	".doGet(): MESSAGE=" + e.getMessage(), e);
        	}

        	SMRCConnectionPoolUtils.rollbackTransaction(DBConn);

            request.setAttribute("exception", e.getMessage());
            sFwdUrl = "/SMRCErrorPage.jsp";
            redirect = false;

        } finally {
			SMRCConnectionPoolUtils.close(DBConn);
			gotoPage(sFwdUrl, request, response,redirect);
		}

	}

} //class
