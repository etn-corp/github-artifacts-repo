package com.eaton.electrical.smrc.bo;

import java.sql.*;
import java.util.*;

import com.eaton.electrical.smrc.dao.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;


public class TargetProjectUpdateEmail implements java.io.Serializable  // extends Thread
{
	
	private String _projId = "";
	private User _changedBy = new User();
	private User _addedBy = new User();
	private TargetProject _oldProject = new TargetProject();
	private TargetProject _newProject = new TargetProject();
	private ArrayList _approvers = new ArrayList();
	private ArrayList _thisApprover = new ArrayList();
	private int srYear = 0;
	
	private final char newline = 13;
	
	private static final long serialVersionUID = 100;

	public void setProjectId(String id) {
		_projId = id;
	}
	
	public void setUpdateUser(User user) {
		_changedBy = user;
	}
	
	public void setOldProject(TargetProject tp) {
		_oldProject = tp;
	}
	
	public void setNewProject(TargetProject tp) {
		_newProject = tp;
	}
	
	/** This method retrieves the text to be included in the email.
	 *	@return String The message in the email.
	 */
	private String getText(boolean approverOnly, Connection DBConn) {
		StringBuffer text = new StringBuffer("");
		String changes = "";
		
		try {
			if (_oldProject.getId() != 0) {
				text.append(newline + "A change has been made to a Target Project in the Target Account Planner by ");
				text.append(_changedBy.getFirstName());
				text.append(" ");
				text.append(_changedBy.getLastName());
				text.append("." + newline + "You are receiving a copy of this changes because you have been identified as one of the team members for this project.");
				text.append(newline);
				text.append(newline + "Here is a list of changes that have been made");
				
				changes = getChanges(_oldProject, _newProject);
			}
			else {
				text.append(_changedBy.getFirstName());
				text.append(" ");
				text.append(_changedBy.getLastName());
				text.append(" has created a target project in the Target Account Planner. ");
				text.append("." + newline + "You are receiving a copy of this changes because you have been identified as one of the team members for this project.");
				text.append(newline + "This target must still be approved by the district manager, project sales manager, and CHAMPS manager.");
				text.append(newline + "Here is a description of the target project" + newline);
				text.append(newline);
			}
			
			if (!_oldProject.isActive() && _newProject.isActive()) {
				text.append(newline);
				text.append(newline + "This Target Project has now been approved. Please return to the Target Account Planner and fill in the Team Member list" + newline);
			}
			
			text.append(newline);
			text.append(newline + "To access the Account Planner, go to the Sales Resources channel on JOE." + newline);
			text.append(newline);
			
			text.append(changes);
			
			text.append(getProjectDescription(_newProject, DBConn));
			
			if (approverOnly && _thisApprover.size() > 0) {
				//User approver = (User)_thisApprover.get(0);
				text.append(newline);
				text.append(newline);
				text.append("Please log onto the Sales Resource Channel and approve or reject this target project");
				text.append(newline);
			}
			
		}
		catch (Exception e) {
		    SMRCLogger.error(this.getClass().getName() + ".getText(): ", e);
			text.append("An error occurred getting text for message. Please forward this to IT Support.");
		}
		
		return text.toString();
	}
	
	private String getProjectDescription(TargetProject project, Connection DBConn) {
		StringBuffer text = new StringBuffer("");
		
		try {
			text.append(newline + "Geography: ");
			
			if (project.getSPGeog() != null) {
				text.append(MiscDAO.getGeography(project.getSPGeog(),DBConn).getDescription());
				text.append(" (");
				text.append(project.getSPGeog());
				text.append(")");
			}
			
			text.append(newline + "Job Name: ");
			if (project.getJobName() != null) {
				text.append(project.getJobName());
			}
			else {
				text.append(" ");
			}
			text.append(newline + "Consultant: ");
			if (project.getConsultant() != null) {
				text.append(project.getConsultant());
			}
			else {
				text.append(" ");
			}
			text.append(newline + "General Contractors: ");
			if (project.getGenContractors() != null) {
				text.append(project.getGenContractors());
			}
			else {
				text.append(" ");
			}
			text.append(newline + "CH Position with Contractors: ");
			if (project.getCHPosition() != null) {
				text.append(project.getCHPosition());
			}
			else {
				text.append(" ");
			}
			text.append(newline + "Status Notes: ");
			if (project.getStatusNotes() != null) {
				text.append(project.getStatusNotes());
			}
			else {
				text.append(" ");
			}
			text.append(newline + "Strategic Reason Notes: ");
			if (project.getStratReasonNotes() != null) {
				text.append(project.getStratReasonNotes());
			}
			else {
				text.append(" ");
			}
			text.append(newline + "Spec Preference Notes: ");
			if (project.getPreferenceNotes() != null) {
				text.append(project.getPreferenceNotes());
			}
			else {
				text.append(" ");
			}
			text.append(newline + "Neg Number: ");
			if (project.getNegNum() != null) {
				text.append(project.getNegNum());
			}
			else {
				text.append(" ");
			}
			text.append(newline + "Date Changed: ");
			text.append(project.getDateChangedAsString());
			text.append(newline + "Bid Date: ");
			text.append(project.getBidDateAsString());
			text.append(newline + "Change Order Potential: ");
			if (project.getChangeOrderPotential().getDescription() != null) {
				text.append(project.getChangeOrderPotential().getDescription());
			}
			else {
				text.append(" ");
			}
			text.append(newline + "Project Status: ");
			if (project.getStatus().getDescription() != null) {
				text.append(project.getStatus().getDescription());
			}
			else {
				text.append(" ");
			}
			text.append(newline + "Spec Preference: ");
			if (project.getPreference().getDescription() != null) {
				text.append(project.getPreference().getDescription());
			}
			else {
				text.append(" ");
			}
			text.append(newline + "Target Reason: ");
			if (project.getReason().getDescription() != null) {
				text.append(project.getReason().getDescription());
			}
			else {
				text.append(" ");
			}
			text.append(newline + "CH value: ");
			text.append(project.getCHValueForDisplay());
			text.append(newline + "Total Value: ");
			text.append(project.getTotalValueForDisplay());
			text.append(newline + "Internal Status: ");
			
			if (project.getInternalStatus() != null)  {
				if (project.deleted()) {
					text.append("Deleted");
				} else if (project.isActive()) {
					text.append("Active");
				} else if (project.waitingForChampsMgr()) {
				    text.append("Awaiting CHAMPS Manager Approval");
				} else if (project.waitingForProjectSalesMgr()) {
					text.append("Awaiting Project Sales Manager Approval");
				} else {
					text.append("Awaiting District Manager Approval");
				}
			}
			else {
				text.append("Unknown");
			}
			
			text.append(newline);
			text.append(newline + "Products in project: ");
			int pdadCnt = 0;
			int cifdCnt = 0;
			int totCnt = 0;
		//	boolean parseError = false;
			
			if (project.getProducts() != null) {
			    for (int i=0; i < project.getProducts().size(); i++) {
					Product p = (Product)project.getProducts().get(i);
					if (i > 0) {
						text.append(", ");
					}
					text.append(p.getDescription());
					text.append(" (");
					text.append(p.getId());
					text.append(")");
					totCnt++;
					Division division = p.getDivision();
					if (division.getName().equalsIgnoreCase("PDAD")){
					    pdadCnt++;
					} else if (division.getName().equalsIgnoreCase("CIFD")){
					    cifdCnt++;
					}
					
				}
			}
			
			if (totCnt == 0) {
				text.append("No products were selected for this project.");
			}
			else {
				java.text.NumberFormat nf = java.text.NumberFormat.getPercentInstance(Locale.US);
				nf.setMaximumFractionDigits(0);
				nf.setMinimumFractionDigits(0);
				text.append(newline + "% PDAD by product count: ");
				text.append(nf.format((double)pdadCnt/(double)totCnt));
				text.append(newline + "% CIFD by product count: ");
				text.append(nf.format((double)cifdCnt/(double)totCnt));
			}
			
		
			text.append(newline);
			text.append(newline + "Vendors bidding on project: ");
			if (project.getVendors() != null) {
				for (int i=0; i < project.getVendors().size(); i++) {
					Vendor v = (Vendor)project.getVendors().get(i);
					if (i > 0) {
						text.append(", ");
					}
					text.append(v.getDescription());
				}
			}
			else {
				text.append("none");
			}
			
			text.append(newline);
			text.append(newline + "Contractors working on project: ");
			if (project.getContractors() != null) {
				for (int i=0; i < project.getContractors().size(); i++) {
					Customer c = (Customer)project.getContractors().get(i);
					if (i > 0) {
						text.append(", ");
					}
					text.append(c.getName());
					text.append(" (");
					text.append(c.getVistaCustNum());
					text.append(")");
				}
			}
			else {
				text.append("none");
			}
			
			text.append(newline);
			text.append(newline + "Distributors working on project: ");
			if (project.getDistributors() != null) {
				for (int i=0; i < project.getDistributors().size(); i++) {
					Customer c = (Customer)project.getDistributors().get(i);
					if (i > 0) {
						text.append(", ");
					}
					text.append(c.getName());
					text.append(" (");
					text.append(c.getVistaCustNum());
					text.append(")");
				}
			}
			else {
				text.append("none");
			}
			
			text.append(newline);
			text.append(newline + "End Customer for project: ");
			
			if (project.getEndCustomer() != null &&
					project.getEndCustomer().getVistaCustNum() != null)
			{
				text.append(project.getEndCustomer().getName());
				text.append(" (");
				text.append(project.getEndCustomer().getVistaCustNum());
				text.append(")");
			}
			else {
				text.append(" None Specified");
			}
			
			text.append(newline);
			text.append(newline + "Approvers" + newline);
			if (_approvers != null) {
				for (int i=0; i < _approvers.size(); i++) {
					User u = (User)_approvers.get(i);
					
					text.append(u.getFirstName());
					text.append(" ");
					text.append(u.getLastName());
					text.append(" - ");
					text.append(u.getEmailAddress());
					text.append(newline);
				}
			}
			else {
				text.append("none");
			}
		} 
		catch (Exception e) {
		    SMRCLogger.error(this.getClass().getName() + ".getProjectDescription(): ", e);
			text.append("An exception occurred getting project description. Please forward this message to IT Support.");
		}
		
		return text.toString();
	}
	
	private String getChanges(TargetProject oldP, TargetProject newP) {
		StringBuffer changes = new StringBuffer(newline + "--------------------------------------------" + newline + "This section contains a list of all of the changes made to this project." + newline);
		changes.append(newline);
		
		if (oldP == null) {
			return "New Project";
		}
		
		if (newP == null) {
			return "New Project is null. Contact IT Support at oemaccountplanner@eaton.com";
		}
		
		try {
                        if (!oldP.getSPGeog().equals(newP.getSPGeog())) {
				changes.append(newline + "Geography changed. OLD: ");
				changes.append(oldP.getSPGeog());
				changes.append(" NEW: ");
				changes.append(newP.getSPGeog());
			}
                    
			if (!oldP.getJobName().equals(newP.getJobName())) {
				changes.append(newline + "Job Name changed. OLD: ");
				changes.append(oldP.getJobName());
				changes.append(" NEW: ");
				changes.append(newP.getJobName());
			}
                    
			if (!oldP.getConsultant().equals(newP.getConsultant())) {
				changes.append(newline + "Consultant changed. OLD: ");
				changes.append(oldP.getConsultant());
				changes.append(" NEW: ");
				changes.append(newP.getConsultant());
			}
                    
			if (!oldP.getGenContractors().equals(newP.getGenContractors())) {
				changes.append(newline + "General Contractors changed. OLD: ");
				changes.append(oldP.getGenContractors());
				changes.append(" NEW: ");
				changes.append(newP.getGenContractors());
			}
			
			if (!oldP.getElecContractors().equals(newP.getElecContractors())) {
				changes.append(newline + "Electrical Contractors changed. OLD: ");
				changes.append(oldP.getElecContractors());
				changes.append(" NEW: ");
				changes.append(newP.getElecContractors());
			}
			
			if (!oldP.getCHPosition().equals(newP.getCHPosition())) {
				changes.append(newline + "CH Position with Contractor changed. OLD: ");
				changes.append(oldP.getCHPosition());
				changes.append(" NEW: ");
				changes.append(newP.getCHPosition());
			}
			
			if (oldP.getStatus().getId() != newP.getStatus().getId()) {
				changes.append(newline + "Status changed. OLD: ");
				changes.append(oldP.getStatus().getDescription());
				changes.append(" NEW: ");
				changes.append(newP.getStatus().getDescription());
			}
                    
			if (!oldP.getStatusNotes().equals(newP.getStatusNotes())) {
				changes.append(newline + "Status Notes changed." + newline + "OLD: ");
				changes.append(oldP.getStatusNotes());
				changes.append(" NEW: ");
				changes.append(newP.getStatusNotes());
			}
			
			if (oldP.getPreference().getId() != newP.getPreference().getId()) {
				changes.append(newline + "Preference changed. OLD: ");
				changes.append(oldP.getPreference().getDescription());
				changes.append(" NEW: ");
				changes.append(newP.getPreference().getDescription());
			}
			
			if (!oldP.getPreferenceNotes().equals(newP.getPreferenceNotes())) {
				changes.append(newline + "Preference Notes changed." + newline + "OLD: ");
				changes.append(oldP.getPreferenceNotes());
				changes.append(" NEW: ");
				changes.append(newP.getPreferenceNotes());
			}
			
			if (oldP.getReason().getId() != newP.getReason().getId()) {
				changes.append(newline + "Reason changed. OLD: ");
				changes.append(oldP.getReason().getDescription());
				changes.append(" NEW: ");
				changes.append(newP.getReason().getDescription());
			}
                    	
			if (!oldP.getStratReasonNotes().equals(newP.getStratReasonNotes())) {
				changes.append(newline + "Strategic Reason Notes changed." + newline + "OLD: ");
				changes.append(oldP.getStratReasonNotes());
				changes.append(" NEW: ");
				changes.append(newP.getStratReasonNotes());
			}
			
			if (!oldP.getNegNum().equals(newP.getNegNum())) {
				changes.append(newline + "Neg Number changed. OLD: ");
				changes.append(oldP.getNegNum());
				changes.append(" NEW: ");
				changes.append(newP.getNegNum());
			}
			
			if (!oldP.getNotes().equals(newP.getNotes())) {
				changes.append(newline + "Notes changed. OLD: ");
				changes.append(oldP.getNotes());
				changes.append(" NEW: ");
				changes.append(newP.getNotes());
			}
			
			if (oldP.getBidDate()!=null && !oldP.getBidDate().equals(newP.getBidDate())) {
				changes.append(newline + "Bid Date changed. OLD: ");
				changes.append(oldP.getBidDateAsString());
				changes.append(" NEW: ");
				changes.append(newP.getBidDateAsString());
			}
			
			if (oldP.getChangeOrderPotential().getId() != newP.getChangeOrderPotential().getId()) {
				changes.append(newline + "Notes changed. OLD: ");
				changes.append(oldP.getChangeOrderPotential().getDescription());
				changes.append(" NEW: ");
				changes.append(newP.getChangeOrderPotential().getDescription());
			}
                    	
			if (oldP.getCHValue() != newP.getCHValue()) {
				changes.append(newline + "Cutler-Hammer value of project changed. OLD: ");
				changes.append(oldP.getCHValueForDisplay());
				changes.append(" NEW: ");
				changes.append(newP.getCHValueForDisplay());
			}
			
			if (oldP.getTotalValue() != newP.getTotalValue()) {
				changes.append(newline + "Total Value of project changed. OLD: ");
				changes.append(oldP.getTotalValueForDisplay());
				changes.append(" NEW: ");
				changes.append(newP.getTotalValueForDisplay());
			}
			
			if (!oldP.getSalesmanId().equals(newP.getSalesmanId())) {
				changes.append(newline + "Salesman Id changed. OLD: ");
				changes.append(oldP.getSalesmanId());
				changes.append(" NEW: ");
				changes.append(newP.getSalesmanId());
			}
			
            if (!oldP.getInternalStatus().equals(newP.getInternalStatus())) {
				changes.append(newline + "Internal Status changed. OLD: ");
				
				if (oldP.waitingForDM()) {
					changes.append("Awaiting District Manager approval");
				}
				if (oldP.waitingForProjectSalesMgr()){
				    changes.append("Awaiting Project Sales Manager approval");
				}
	/*			else if (!oldP.zmApproved()) {
					changes.append("Awaiting Zone Manager Approval");
				}
				else if (!oldP.mktMgrApproved()) {
					changes.append("Awaiting Mkt Manager Approval");
				}
	*/
				else if (oldP.waitingForChampsMgr()){
				    changes.append("Awaiting CHAMPS Manager approval");
				}
				else if (oldP.isActive()) {
					changes.append("Active");
				}
				else if (oldP.deleted()) {
					changes.append("Deleted or Inactive");
				}
				
				changes.append(" NEW: ");
				
				if (newP.waitingForDM()) {
					changes.append("Awaiting District Manager approval");
				}
				if (newP.waitingForProjectSalesMgr()){
				    changes.append("Awaiting Project Sales Manager approval");
				}
	/*			else if (!newP.zmApproved()) {
					changes.append("Awaiting Zone Manager Approval");
				}
				else if (!newP.mktMgrApproved()) {
					changes.append("Awaiting Mkt Manager Approval");
				} 
	*/
				else if (newP.waitingForChampsMgr()){
				    changes.append("Awaiting CHAMPS Manager approval");
				}
				else if (newP.isActive()) {
					changes.append("Active");
				}
				else if (newP.deleted()) {
					changes.append("Deleted or Inactive");
				}
			}
			
			boolean venChange = false;
			
            ArrayList oldVendors = oldP.getVendors();
			ArrayList newVendors = newP.getVendors();
			
			if (oldVendors == null) {
				oldVendors = new ArrayList(1);
			}
			
			if (newVendors == null) {
				newVendors = new ArrayList(1);
			}
			
			if (oldVendors.size() != newVendors.size()) {
				venChange = true;
			}
			else {
				for (int i=0; i < oldVendors.size(); i++) {
					Vendor ov = (Vendor)oldVendors.get(i);
					
					boolean stillThere = false;
					for (int j=0; j < newVendors.size(); j++) {
						Vendor nv = (Vendor)newVendors.get(i);
						
						if (nv.getId() == ov.getId()) {
							stillThere = true;
							break;
						}
					}
					
					if (!stillThere) {
						venChange = true;
						break;
					}
				}
				
				if (!venChange) {
					for (int i=0; i < newVendors.size(); i++) {
						Vendor nv = (Vendor)newVendors.get(i);
						
						boolean wasThere = false;
						for (int j=0; j < oldVendors.size(); j++) {
							Vendor ov = (Vendor)oldVendors.get(i);
							
							if (nv.getId() == ov.getId()) {
								wasThere = true;
								break;
							}
						}
						
						if (!wasThere) {
							venChange = true;
							break;
						}
					}
				}
			}
			
            if (venChange) {
				changes.append(newline);
				changes.append(newline + "Vendors changed." + newline + "OLD: ");
				
				for (int i = 0; i < oldP.getVendors().size(); i++) {
					Vendor v = (Vendor)oldP.getVendors().get(i);
					
					changes.append(v.getDescription());
					
					if (i < oldP.getVendors().size()-1){
						changes.append(", ");
					}
				}
				
				changes.append(newline + "NEW: ");
				
				for (int i = 0; i < newP.getVendors().size(); i++) {
					Vendor v = (Vendor)newP.getVendors().get(i);
					
					changes.append(v.getDescription());
					
					if (i < newP.getVendors().size()-1){
						changes.append(", ");
					}
				}
			}
			
			boolean contractorsChanged = false;
			
                        if (oldP.getContractors().size() != newP.getContractors().size()) {
				contractorsChanged = true;
			}
			else {
				for (int i = 0; i < oldP.getContractors().size(); i++) {
					Customer c = (Customer)oldP.getContractors().get(i);
					boolean custDel = true;
					
					for (int j=0; j < newP.getContractors().size(); j++) {
						Customer c2 = (Customer)newP.getContractors().get(j);
						
						if (c2 == null && c == null) {}
						else if (c2 == null && c != null) {}
						else if (c2 != null && c == null) {}
						else if (c2.getVistaCustNum() == null && c.getVistaCustNum() == null) {}
						else if (c2.getVistaCustNum() == null && c.getVistaCustNum() != null) {}
						else if (c2.getVistaCustNum() != null && c.getVistaCustNum() == null) {}
						else if (c2.getVistaCustNum().equals(c.getVistaCustNum())) {
							custDel = false;
							break;
						}
					}
					
					if (custDel) {
						contractorsChanged = true;
						break;
					}
				}
				
				if (!contractorsChanged) {
					for (int i = 0; i < newP.getContractors().size(); i++) {
						Customer c = (Customer)newP.getContractors().get(i);
						boolean newCust = true;
						
						for (int j=0; j < oldP.getContractors().size(); j++) {
							Customer c2 = (Customer)oldP.getContractors().get(j);
							
							if (c2 == null && c == null) {}
							else if (c2 == null && c != null) {}
							else if (c2 != null && c == null) {}
							else if (c2.getVistaCustNum() == null && c.getVistaCustNum() == null) {}
							else if (c2.getVistaCustNum() == null && c.getVistaCustNum() != null) {}
							else if (c2.getVistaCustNum() != null && c.getVistaCustNum() == null) {}
							else if (c2.getVistaCustNum().equals(c.getVistaCustNum())) {
								newCust = false;
								break;
							}
						}
						
						if (newCust) {
							contractorsChanged = true;
							break;
						}
					}
				}
			}
			if (contractorsChanged) {
				changes.append(newline);
				changes.append(newline + "Contractors changed." + newline + "OLD: ");
				
				for (int i = 0; i < oldP.getContractors().size(); i++) {
					Customer c = (Customer)oldP.getContractors().get(i);
					
					if (c != null) {
						if (c.getDescription() != null) {
							changes.append(c.getDescription());
							
							if (i < oldP.getContractors().size()-1){
								changes.append(", ");
							}
						}
					}
				}
				
				changes.append(newline + "NEW: ");
				
				for (int i = 0; i < newP.getContractors().size(); i++) {
					Customer c = (Customer)newP.getContractors().get(i);
					
					if (c != null) {
						if (c.getDescription() != null) {
							changes.append(c.getDescription());
							
							if (i < newP.getContractors().size()-1){
								changes.append(", ");
							}
						}
					}
				}
			}
			
			if (oldP.getEndCustomer() != null || newP.getEndCustomer() != null) {
				if (oldP.getEndCustomer().getVistaCustNum() != null || newP.getEndCustomer().getVistaCustNum() != null)
				{
					if (oldP.getEndCustomer().getVistaCustNum() == null) {
						changes.append(newline);
						changes.append(newline + "End Customer changed." + newline + "OLD: ");
						changes.append("N/A" + newline + "NEW: ");
						changes.append(newP.getEndCustomer().getName());
					}
					else if (newP.getEndCustomer().getVistaCustNum() == null) {
						changes.append(newline);
						changes.append(newline + "End Customer changed." + newline + "OLD: ");
						changes.append(oldP.getEndCustomer().getName());
						changes.append(newline + "NEW: N/A");
					}
					else if (!oldP.getEndCustomer().getVistaCustNum().equals(newP.getEndCustomer().getVistaCustNum()))
					{
						changes.append(newline);
						changes.append(newline + "End Customer changed." + newline + "OLD: ");
						changes.append(oldP.getEndCustomer().getName());
						changes.append(newline + "NEW: ");
						changes.append(newP.getEndCustomer().getName());
					}
				}
			}
			
			boolean distChanged = false;
			if (oldP.getDistributors().size() != newP.getDistributors().size()) {
				distChanged = true;
			}
			else {
				for (int i = 0; i < oldP.getDistributors().size(); i++) {
					Customer c = (Customer)oldP.getDistributors().get(i);
					boolean custDel = true;
					
					for (int j=0; j < newP.getDistributors().size(); j++) {
						Customer c2 = (Customer)newP.getDistributors().get(j);
						
						if (c2 == null && c == null) {}
						else if (c2 == null && c != null) {}
						else if (c2 != null && c == null) {}
						else if (c2.getVistaCustNum() == null && c.getVistaCustNum() == null) {}
						else if (c2.getVistaCustNum() == null && c.getVistaCustNum() != null) {}
						else if (c2.getVistaCustNum() != null && c.getVistaCustNum() == null) {}
						else if (c2.getVistaCustNum().equals(c.getVistaCustNum())) {
							custDel = false;
							break;
						}
					}
					
					if (custDel) {
						distChanged = true;
						break;
					}
				}
				
				if (!distChanged) {
					for (int i = 0; i < newP.getDistributors().size(); i++) {
						Customer c = (Customer)newP.getDistributors().get(i);
						boolean newCust = true;
						
						for (int j=0; j < oldP.getDistributors().size(); j++) {
							Customer c2 = (Customer)oldP.getDistributors().get(j);
							
							if (c2 == null && c == null) {}
							else if (c2 == null && c != null) {}
							else if (c2 != null && c == null) {}
							else if (c2.getVistaCustNum() == null && c.getVistaCustNum() == null) {}
							else if (c2.getVistaCustNum() == null && c.getVistaCustNum() != null) {}
							else if (c2.getVistaCustNum() != null && c.getVistaCustNum() == null) {}
							else if (c2.getVistaCustNum().equals(c.getVistaCustNum())) {
								newCust = false;
								break;
							}
						}
						
						if (newCust) {
							distChanged = true;
							break;
						}
					}
				}
			}
			
			if (distChanged) {
				changes.append(newline);
				changes.append(newline + "Distributors changed." + newline + "OLD: ");
				
				for (int i = 0; i < oldP.getDistributors().size(); i++) {
					Customer c = (Customer)oldP.getDistributors().get(i);
					
					if (c != null) {
						if (c.getDescription() != null) {
							changes.append(c.getDescription());
							
							if (i < oldP.getDistributors().size()-1){
								changes.append(", ");
							}
						}
					}
				}
				
				changes.append(newline + "NEW: ");
				
				for (int i = 0; i < newP.getDistributors().size(); i++) {
					Customer c = (Customer)newP.getDistributors().get(i);
					
					if (c != null) {
						if (c.getDescription() != null) {
							changes.append(c.getDescription());
							
							if (i < newP.getDistributors().size()-1){
								changes.append(", ");
							}
						}
					}
				}
			}
			
			boolean prodChange = false;
			ArrayList oldProds = oldP.getProducts();
			ArrayList newProds = newP.getProducts();
			
			if (oldProds.size() != newProds.size()) {
				prodChange = true;
			}
			else {
				for (int i=0; i < oldProds.size(); i++) {
					Product op = (Product)oldProds.get(i);
					
					boolean stillThere = false;
					for (int j=0; j < newProds.size(); j++) {
						Product np = (Product)newProds.get(i);
						
						if (np.getId().equals(op.getId())) {
							stillThere = true;
							break;
						}
					}
					
					if (!stillThere) {
						prodChange = true;
						break;
					}
				}
				
				if (!prodChange) {
					for (int i=0; i < newProds.size(); i++) {
						Product np = (Product)newProds.get(i);
						
						boolean wasThere = false;
						for (int j=0; j < oldProds.size(); j++) {
							Product op = (Product)oldProds.get(i);
							
							if (np.getId().equals(op.getId())) {
								wasThere = true;
								break;
							}
						}
						
						if (!wasThere) {
							prodChange = true;
							break;
						}
					}
				}
			}
			
			//		if (!oldP.getProducts().equals(newP.getProducts())) {
			if (prodChange) {
				changes.append(newline);
				changes.append(newline + "Products changed." + newline + "OLD: ");
				
				for (int i = 0; i < oldP.getProducts().size(); i++) {
					Product p = (Product)oldP.getProducts().get(i);
					
					changes.append(p.getDescription());
					
					if (i < oldP.getProducts().size()-1){
						changes.append(", ");
					}
				}
				
				changes.append(newline + "NEW: ");
                                
				for (int i = 0; i < newP.getProducts().size(); i++) {
					Product p = (Product)newP.getProducts().get(i);
					
					changes.append(p.getDescription());
					
					if (i < newP.getProducts().size()-1){
						changes.append(", ");
					}
				}
			}
		}
		catch (Exception e) {
		    SMRCLogger.error(this.getClass().getName() + ".getChanges(): ", e);
			changes.append(newline + "An Error occurred while gathering the changes. Please forward this to oemaccountplanner@eaton.com" + newline);
			changes.append(e.getMessage());
		}
		
		changes.append(newline);
		changes.append(newline + "End of Changes" + newline + "--------------------------------------------" + newline);
		
		return changes.toString();
	}
	
	private ArrayList getRecipients(Connection DBConn) {
		ArrayList emails = new ArrayList();
		
		String sel = "select distinct email_address from users u, project_user_xref pux " +
		"where u.userid = pux.userid " +
		"and pux.target_project_id = " + _projId;
		
		Statement s = null;
		ResultSet r = null;
		
		try {
			s = DBConn.createStatement();
			r = s.executeQuery(sel);
			
			while (r.next()) {
				emails.add(r.getString(1));
			}
			
		}
		catch (Exception e) {
		    SMRCLogger.error(this.getClass().getName() + ".getRecipients(): ", e);
		}
		finally {
		    SMRCConnectionPoolUtils.close(r);
		    SMRCConnectionPoolUtils.close(s);
		}
		
		for (int i=0; i < _approvers.size(); i++) {
			User u = (User)_approvers.get(i);
			boolean haveEmail = false;
			
			for (int j=0; j < emails.size(); j++) {
				String e = (String)emails.get(j);
				if (e.equals(u.getEmailAddress())) {
					haveEmail = true;
				}
			}
			
			if (!haveEmail) {
				emails.add(u.getEmailAddress().trim());
			}
		}
		
		return emails;
	}
	
	private void getApprovers(Connection DBConn) {
		if (!_newProject.deleted()) {
			if (_newProject.waitingForDM()) {
				getDistrictManager(true, DBConn);
			}
			else if (_newProject.waitingForProjectSalesMgr()){
			    getDistrictManager(false, DBConn);
			    getProjectSalesManager(true, DBConn);
			}
	/*		else if (!_newProject.zmApproved()) {
				getDistrictManager(false, DBConn);
				getZoneManager(true, DBConn);
			}
			else if (!_newProject.mktMgrApproved()) {
				getDistrictManager(false, DBConn);
				getZoneManager(false, DBConn);
				getDivisionManagers(true, DBConn);
			}
	*/
			else if (_newProject.waitingForChampsMgr()) {
				getDistrictManager(false, DBConn);
				getProjectSalesManager(false, DBConn);
				getChampsManagers(true, DBConn);
			}
			else if (_newProject.isActive()) {
				getDistrictManager(false, DBConn);
	//			getZoneManager(false, DBConn);
	//			getDivisionManagers(false, DBConn);
				getProjectSalesManager(false, DBConn);
				getChampsManagers(false, DBConn);
			}
		}
	}
	
	private void getDistrictManager(boolean isThisApprover, Connection DBConn) {
		String sel = "select first_nm, last_nm, user_id from current_salesman_v a," +
		" (select salesman_id, max(start_dt) st_dt from current_salesman_v group by salesman_id) b" +
		" where sp_geog_cd = '" + _newProject.getSPGeog() + "'" +
		" and a.salesman_id = b.salesman_id" +
		" and a.start_dt = b.st_dt" +
		" and inactive_dt is null" +
		" and a.start_dt >= to_date('01-01-'||to_char(sysdate,'YYYY'),'MM-DD-YYYY')" +
		" and function_cd = 'DS'";
		
		Statement s = null;
		ResultSet r = null;
		
		try {
			s = DBConn.createStatement();
			r = s.executeQuery(sel);
			
			while (r.next()) {
				if (r.getString("user_id") != null) {
					User _dm = new User();
					if (r.getString("first_nm") != null) {
						_dm.setFirstName(r.getString("first_nm"));
					}
					
					if (r.getString("last_nm") != null) {
						_dm.setLastName(r.getString("last_nm"));
					}
					
					_dm.setVistaId(r.getString("user_id"));
					_dm.setEmailAddress(getEmail(r.getString("user_id"),DBConn));
					_dm.setUserid(getUserid(r.getString("user_id"),DBConn));
					
					if (isThisApprover) {
						_thisApprover.add(_dm);
					}
					else {
						_approvers.add(_dm);
					}
				}
				else if (isThisApprover) {
					//forceDMApproval(DBConn);
			//		getZoneManager(true, DBConn);
				    getProjectSalesManager(true,DBConn);
				}
			}

		}
		catch (Exception e) {
		    SMRCLogger.error(this.getClass().getName() + ".getDistrictManager(): ", e);
		}
		finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}
	}
	/*
	private void getZoneManager(boolean isThisApprover, Connection DBConn) {
		
	    if (_newProject.getSPGeog().length() < 4) {
	        return;
	    }		    
		    
		String sel = "select first_nm, last_nm, user_id from current_salesman_v a," +
		" (select salesman_id, max(start_dt) st_dt from current_salesman_v group by salesman_id) b" +
		" where sp_geog_cd = '" + _newProject.getSPGeog().substring(0,4) + "0'" +
		" and a.salesman_id = b.salesman_id" +
		" and a.start_dt = b.st_dt" +
		" and inactive_dt is null" +
		" and a.start_dt >= to_date('01-01-'||to_char(sysdate,'YYYY'),'MM-DD-YYYY')" +
		" and function_cd = 'ZN'";
		
		Statement s = null;
		ResultSet r = null;
		
		try {
		    
		    s = DBConn.createStatement();
			r = s.executeQuery(sel);
			
			while (r.next()) {
				if (r.getString("user_id") != null) {
					User _zm = new User();
					if (r.getString("first_nm") != null) {
						_zm.setFirstName(r.getString("first_nm"));
					}
					if (r.getString("last_nm") != null) {
						_zm.setLastName(r.getString("last_nm"));
					}
					
					_zm.setVistaId(r.getString("user_id"));
					_zm.setEmailAddress(getEmail(r.getString("user_id"),DBConn));
					_zm.setUserid(getUserid(r.getString("user_id"), DBConn));
					
					if (isThisApprover) {
						_thisApprover.add(_zm);
					}
					else {
						_approvers.add(_zm);
					}
				}
				//else if (isThisApprover) {
				//	forceZMApproval(DBConn);
					//getMarketingManagers(true);
				//}
			}
			
		}
		catch (Exception e) {
		    SMRCLogger.error(this.getClass().getName() + ".getZoneManager(): ", e);
		}
		finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}		
	}
	*/
	
	// This method returns an ArrayList of User objects for the division managers 
	// of the specified divisions
	private ArrayList getDivisionManagers(String division, Connection DBConn) {
	   ArrayList divisionManagers = new ArrayList();
	   try {
		   	ArrayList userList = UserDAO.getDivisionManagersForDivision(division,DBConn);
		   	for (int i=0; i< userList.size(); i++){
		   	    User user = (User) userList.get(i);
		   	    divisionManagers.add(user);
			}
	    } catch (Exception e) {
		    SMRCLogger.error(this.getClass().getName() + ".getDivisionManagers(): ", e);
		}	
			
	    return divisionManagers;
	}
	
	
	private String getEmail(String vistaId, Connection DBConn) {
		String email = "";
		
		String sel = "select email_address from users where vistaline_id = '" + vistaId + "'";
		
		Statement s = null;
		ResultSet r = null;
		
		try {
			s = DBConn.createStatement();
			r = s.executeQuery(sel);
			
			while (r.next()) {
				email = r.getString(1);
			}

		}
		catch (Exception e) {
		    SMRCLogger.error(this.getClass().getName() + ".getEmail(): ", e);
		}
		finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}		
		return email;
	}
	
	private String getUserid(String vistaId, Connection DBConn) {
		String userid = "";
		
		String sel = "select userid from users where vistaline_id = '" + vistaId + "'";
		
		Statement s = null;
		ResultSet r = null;
		
		try {
			s = DBConn.createStatement();
			r = s.executeQuery(sel);
			
			while (r.next()) {
				userid = r.getString(1);
			}

		}
		catch (Exception e) {
		    SMRCLogger.error(this.getClass().getName() + ".getUserid(): ", e);
		}
		finally {
			SMRCConnectionPoolUtils.close(r);
			SMRCConnectionPoolUtils.close(s);
		}		
		return userid;
	}
	/*
	private void setProductPcnts() {
		int pdadCnt = 0;
		int cifdCnt = 0;
		int totCnt = 0;
		//boolean parseError = false;
		for (int i=0; i < _newProject.getProducts().size(); i++) {
			Product p = (Product)_newProject.getProducts().get(i);
			
			totCnt++;
			try {
				int id = Integer.parseInt(p.getId());
				if (id >= 45 && id <= 54) {
					pdadCnt++;
				}
				else {
					cifdCnt++;
				}
			}
			catch (Exception e) {
			    SMRCLogger.error(this.getClass().getName() + ".setProductPcnts(): ", e);
				//parseError = true;
			}
		}
		
		//if (!parseError && totCnt != 0) {
		//	_pdadPcnt = (double)pdadCnt/(double)totCnt;
		//	_cifdPcnt = (double)cifdCnt/(double)totCnt;
		//}
	}
     */   
        
        
    //  This method sets the appropriate ArrayList of all project sales managers to be copied
    //  on the approved emails
    private void getProjectSalesManager (boolean isThisApprover, Connection DBConn){
       ArrayList projectSalesManagers = new ArrayList();
	   String zoneDistrict = (_newProject.getSPGeog().substring(0,4) + "0");
	   try {
	        projectSalesManagers = UserDAO.getProjectSalesManagers(zoneDistrict,DBConn);
	        if (projectSalesManagers.size() == 0){
	            SMRCLogger.warn(this.getClass().getName() + "Target Project " + _newProject.getId() + " - No project sales manager found for " + zoneDistrict) ;
	        } else {
	            for (int i=0; i < projectSalesManagers.size(); i++){
	                User user = (User) projectSalesManagers.get(i);
			        if (isThisApprover) {
						_thisApprover.add(user);
					}
					else {
						_approvers.add(user);
					}
	            }
	        }
        
        } catch (Exception e) {
		    SMRCLogger.error(this.getClass().getName() + ".getProjectSalesManager(): ", e);
		} 
        
		
    }
	
    //  This method sets the appropriate ArrayList of all CHAMPS managers to be copied
    //  on the approved emails
    private void getChampsManagers (boolean isThisApprover, Connection DBConn){
       ArrayList champsManagers = new ArrayList();
	   try {
	       champsManagers = UserDAO.getAllCHAMPSManagers(DBConn);
	        if (champsManagers.size() == 0){
	            SMRCLogger.warn(this.getClass().getName() + "Target Project " + _newProject.getId() + " - No CHAMPS managers found ") ;
	        } else {
	            for (int i=0; i < champsManagers.size(); i++){
	                User user = (User) champsManagers.get(i);
			        if (isThisApprover) {
						_thisApprover.add(user);
					}
					else {
						_approvers.add(user);
					}
	            }
	        }
        
        } catch (Exception e) {
		    SMRCLogger.error(this.getClass().getName() + ".getChampsSalesManager(): ", e);
		} 
        
		
    }
    
	/** This is a required method in all objects that extend "Thread"
	 *	<br>This is run each time this email should be sent (each time the tread needs to be executed)
	 */
	public void run() {
	    // Do not send emails for changes to active projects. I put this check here
	    // because this method is called from a few places.
	    if (_oldProject.isActive()){
	        SMRCLogger.debug("TargetProjectUpdateEmail not sent for active project (" + _newProject.getId() + ") changes");
	    } else {
			Connection DBConn = null;
			try {
				
				StringBuffer errorTracker = new StringBuffer("");
		        
				DBConn = SMRCConnectionPoolUtils.getDatabaseConnection() ;
	
				_addedBy = UserDAO.getUser(_newProject.getUserAdded(), DBConn);
			//	setProductPcnts();
				getApprovers(DBConn);
				String falseText = getText(false,DBConn);
				String trueText = getText(true,DBConn);
				
				String from = _changedBy.getEmailAddress().trim();
				String cc = from;
				
				try {
					TAPMail false_mail = new TAPMail();
					String false_subject;
					
					errorTracker.append("from; " + from);
					false_mail.setSenderInfo(_changedBy.getFirstName() + " " + _changedBy.getLastName(), from);
					boolean fromIsTo = false;
					
					ArrayList recipients = getRecipients(DBConn);
					
					for (int i=0; i < recipients.size(); i++) {
						String recip = (String)recipients.get(i);
						
						if (recip.trim().length() > 0) {
							errorTracker.append(newline + "recip.trim().length() > 0; TO; " + recip.trim());
							false_mail.addRecipient(recip.trim());
							if (recip.trim().equalsIgnoreCase(from)) {
								fromIsTo = true;
							}
						}
					}
					
					if (!fromIsTo) {
						errorTracker.append(newline + "!fromIsTo; CC; " + cc.trim());
						false_mail.addCCRecipient(cc);
					}
					
					if (!_addedBy.getEmailAddress().trim().equalsIgnoreCase(from)) {
						boolean isRecip = false;
						
						for (int i=0; i < recipients.size(); i++) {
							String recip = (String)recipients.get(i);
							
							if (recip.equals(_addedBy.getEmailAddress().trim())) {
								isRecip = true;
							}
						}
						
						if (!isRecip) {
							errorTracker.append(newline + "!recip; CC; " + _addedBy.getEmailAddress().trim());
							false_mail.addCCRecipient(_addedBy.getEmailAddress().trim());
						}
					}
					if (_newProject.isActive()){
                        ArrayList divisionManagers = getDivisionManagers("PDAD",DBConn);
//                        boolean addMore = divisionManagers.addAll(getDivisionManagers("CIFD", DBConn));
                        for (int i=0; i < divisionManagers.size(); i++){
                            User user = (User) divisionManagers.get(i);
                            SMRCLogger.debug("Adding Division Manager to TargetProjectUpdateEmail - " + user.getUserid() + "  " + user.getFirstName() + " " + user.getLastName());
                            false_mail.addCCRecipient(user.getEmailAddress());  
                        }
                        
                    }
					
					if (_oldProject.getId() == 0) {
						false_subject = "New Target Project Created in Target Account Planner";
					}
					else {
						false_subject = "Target Project Update in Target Account Planner";
					}
					
					false_mail.sendMessage(falseText, false_subject);
				}
				catch(Exception e) {
				    SMRCLogger.error(this.getClass().getName() + ".run(): ", e);
				    
				}
				
				
				// This try block is meant to send the approval email to the approvers of this
				// particular Target Project only - not to the general public.
				if (!_newProject.isActive() && !_newProject.deleted()) {
					try {
						TAPMail approve_mail = new TAPMail();
						String approve_subject;
											
						if (_changedBy != null && from != null) {
							approve_mail.setSenderInfo(_changedBy.getFirstName() + " " + _changedBy.getLastName(), from);
						}
						else {
							approve_mail.setSenderInfo("System Admin", "oemaccountplanner@eaton.com");
						}
						
						if (_thisApprover.size() > 0) {
							for (int i=0; i < _thisApprover.size(); i++) {
								User recip = (User)_thisApprover.get(i);
								SMRCLogger.debug("Adding approver to TargetProjectUpdateEmail - " + recip.getUserid() + " " + recip.getFirstName() + " " + recip.getLastName());
								approve_mail.addRecipient(recip.getEmailAddress().trim());
							}
						}
						if (_oldProject.getId() == 0) {
							approve_subject = "New Target Project Created in Target Account Planner";
						}
						else {
							approve_subject = "Target Project Update in Target Account Planner";
						}
						approve_mail.sendMessage(trueText, approve_subject);
					}
					catch(Exception e) {
					    SMRCLogger.error(this.getClass().getName() + ".run(): ", e);
						
					}
				}
			
				SMRCConnectionPoolUtils.commitTransaction(DBConn);
			}catch (Exception e) {
			    SMRCLogger.error(this.getClass().getName() + ".run(): ", e);
			    SMRCConnectionPoolUtils.rollbackTransaction(DBConn);
			}
			finally {
				SMRCConnectionPoolUtils.close(DBConn) ;
			}
	    }
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public boolean equals(Object obj) {
		if (this == null || obj == null) {
			return false;
		}

		return super.equals(obj);
	}

	public String toString() {
		String returnString = "";


		return returnString;
	}
    /**
     * @return Returns the srYear.
     */
    public int getSrYear() {
        return srYear;
    }
    /**
     * @param srYear The srYear to set.
     */
    public void setSrYear(int srYear) {
        this.srYear = srYear;
    }
}
