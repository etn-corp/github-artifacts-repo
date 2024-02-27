
package com.eaton.electrical.smrc.bo;

import java.io.*;
import java.util.*;

import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.util.*;

public class EmailReport implements java.io.Serializable  // extends Thread {
{
	private ArrayList _results = new ArrayList(50);
	private User _user = new User();
	private String _rptMonth = "";
	private boolean _showPot = false;
	private boolean _showFor = false;
	private boolean _showComp = false;
	private boolean _showCrSales = false;
	private boolean _showCrOrder = false;
	private boolean _showEMSales = false;
	private boolean _showEMOrder = false;
	private boolean _showDirSales = false;
	private boolean _showDirOrder = false;
	private boolean _showSSO = false;
	private boolean _showCrMon = false;
	private boolean _showCrYTD = false;
	private boolean _showCrPrevYTD = false;
	private boolean _showCrPrevTot = false;
	private boolean _showCrPrevMo = false;
	private boolean _showEMMon = false;
	private boolean _showEMYTD = false;
	private boolean _showEMPrevYTD = false;
	private boolean _showEMPrevTot = false;
	private boolean _showEMPrevMo = false;
	private boolean _showDirMon = false;
	private boolean _showDirYTD = false;
	private boolean _showDirPrevYTD = false;
	private boolean _showDirPrevTot = false;
	private boolean _showDirPrevMo = false;
	private boolean _showSSOMon = false;
	private boolean _showSSOYTD = false;
	private boolean _showSSOPrevYTD = false;
	private boolean _showSSOPrevTot = false;
	private boolean _showSSOPrevMo = false;
	
	char newline = 13;
	
	
//	private String _folder = "C:\\TargetAccountPlanner\\testemails\\";		// NT Folder
//	private String _errFolder = "C:\\TargetAccountPlanner\\testemails\\";		// NT Folder
	private String _folder = "/opt/Netscape/AcctPlan/reports/";		// Unix Folder
	private String _errFolder = "/opt/Netscape/AcctPlan/reports/errors/";		// Unix Folder
	
	private StringBuffer errors = new StringBuffer("");
	
	private static final long serialVersionUID = 100;

	public void setResults(ArrayList results) {
		_results = results;
	}
	
	public void setUser(User user) {
		_user = user;
	}
	
	public void setRptMonth(String rptMonth) {
		_rptMonth = rptMonth;
	}
	
	public void showPotential(boolean showPot) {
		_showPot = showPot;
	}
	
	public void showForecast(boolean showFor) {
		_showFor = showFor;
	}
	
	public void showCompetitor(boolean showComp) {
		_showComp = showComp;
	}
	
	public void showCRSales(boolean showCrSales) {
		_showCrSales = showCrSales;
	}
	
	public void showCROrder(boolean showCrOrder) {
		_showCrOrder = showCrOrder;
	}
	
	public void showEMSales(boolean showEMSales) {
		_showEMSales = showEMSales;
	}
	
	public void showEMOrder(boolean showEMOrder) {
		_showEMOrder = showEMOrder;
	}
	
	public void showSSO(boolean showSSO) {
		_showSSO = showSSO;
	}
	
	public void showDirSales(boolean showDirSales) {
		_showDirSales = showDirSales;
	}
	
	public void showDirOrder(boolean showDirOrder) {
		_showDirOrder = showDirOrder;
	}
	
	public void showCRMon(boolean showCrMon) {
		_showCrMon = showCrMon;
	}
	
	public void showCRYTD(boolean showCrYTD) {
		_showCrYTD = showCrYTD;
	}
	
	public void showCRPrevYTD(boolean showCrPrevYTD) {
		_showCrPrevYTD = showCrPrevYTD;
	}
	
	public void showCRPrevTot(boolean showCrPrevTot) {
		_showCrPrevTot = showCrPrevTot;
	}
	
	public void showCRPrevMo(boolean showCrPrevMo) {
		_showCrPrevMo = showCrPrevMo;
	}
	
	public void showEMMon(boolean showEMMon) {
		_showEMMon = showEMMon;
	}
	
	public void showEMYTD(boolean showEMYTD) {
		_showEMYTD = showEMYTD;
	}
	
	public void showEMPrevYTD(boolean showEMPrevYTD) {
		_showEMPrevYTD = showEMPrevYTD;
	}
	
	public void showEMPrevTot(boolean showEMPrevTot) {
		_showEMPrevTot = showEMPrevTot;
	}
	
	public void showEMPrevMo(boolean showEMPrevMo) {
		_showEMPrevMo = showEMPrevMo;
	}
	
	public void showDirMon(boolean showDirMon) {
		_showDirMon = showDirMon;
	}
	
	public void showDirYTD(boolean showDirYTD) {
		_showDirYTD = showDirYTD;
	}
	
	public void showDirPrevYTD(boolean showDirPrevYTD) {
		_showDirPrevYTD = showDirPrevYTD;
	}
	
	public void showDirPrevTot(boolean showDirPrevTot) {
		_showDirPrevTot = showDirPrevTot;
	}
	
	public void showDirPrevMo(boolean showDirPrevMo) {
		_showDirPrevMo = showDirPrevMo;
	}
	
	public void showSSOMon(boolean showDirMon) {
		_showDirMon = showDirMon;
	}
	
	public void showSSOYTD(boolean showDirYTD) {
		_showDirYTD = showDirYTD;
	}
	
	public void showSSOPrevYTD(boolean showDirPrevYTD) {
		_showDirPrevYTD = showDirPrevYTD;
	}
	
	public void showSSOPrevTot(boolean showDirPrevTot) {
		_showDirPrevTot = showDirPrevTot;
	}
	
	public void showSSOPrevMo(boolean showDirPrevMo) {
		_showDirPrevMo = showDirPrevMo;
	}
	
	private String getText() {
		StringBuffer ret = new StringBuffer("");
		
		ret.append(_user.getFirstName());
		ret.append("," + newline + "Attached is the report you requested through the Target Account Planner." + newline);
		ret.append("If you have any problems with this report, please contact IT support by replying to this message.");
		
		return ret.toString();
	}
	
	private String getData() {
		StringBuffer ret = new StringBuffer("");
		
		ret.append("Amounts represent,");
		ret.append(_rptMonth.substring(4,_rptMonth.length()));
		ret.append("/");
		ret.append(_rptMonth.substring(0,4));
		
		ret.append("\nId,Description");
		
		if (_showPot) {
			ret.append(",Potential Dollars");
		}
		
		if (_showFor) {
			ret.append(",Forecast Dollars");
		}
		
		if (_showComp) {
			ret.append(",Competitor Dollars");
		}
		
		if (_showCrSales) {
			if (_showCrMon) {
				ret.append(",Monthly Credit Sales");
			}
			
			if (_showCrYTD) {
				ret.append(",YTD Credit Sales");
			}
			
			if (_showCrPrevYTD) {
				ret.append(",Prev YTD Credit Sales");
			}
			
			if (_showCrPrevTot) {
				ret.append(",Prev Yr Total Credit Sales");
			}
			
			if (_showCrPrevMo) {
				ret.append(",Prev Year's Monthly Credit Sales");
			}
		}
		
		if (_showCrOrder) {
			if (_showCrMon) {
				ret.append(",Monthly Credit Orders");
			}
			
			if (_showCrYTD) {
				ret.append(",YTD Credit Orders");
			}
			
			if (_showCrPrevYTD) {
				ret.append(",Prev YTD Credit Orders");
			}
			
			if (_showCrPrevTot) {
				ret.append(",Prev Year Tot Credit Orders");
			}
			
			if (_showCrPrevMo) {
				ret.append(",Prev Year's Monthly Credit Orders");
			}
		}
		
		if (_showEMSales) {
			if (_showEMMon) {
				ret.append(",End Mkt Monthly Sales");
			}
			
			if (_showEMYTD) {
				ret.append(",End Mkt YTD Sales");
			}
			
			if (_showEMPrevYTD) {
				ret.append(",End Mkt Prev YTD Sales");
			}
			
			if (_showEMPrevTot) {
				ret.append(",End Mkt Prev Yr Tot Sales");
			}
			
			if (_showEMPrevMo) {
				ret.append(",End Mkt Prev Year's Monthly Sales");
			}
		}
		
		if (_showEMOrder) {
			if (_showEMMon) {
				ret.append(",End Mkt Monthly Orders");
			}
			
			if (_showEMYTD) {
				ret.append(",End Mkt YTD Orders");
			}
			
			if (_showEMPrevYTD) {
				ret.append(",End Mkt Prev YTD Orders");
			}
			
			if (_showEMPrevTot) {
				ret.append(",End Mkt Prev Yr Tot Orders");
			}
			
			if (_showEMPrevMo) {
				ret.append(",End Mkt Prev Year's Monthly Orders");
			}
		}
		
		if (_showDirSales) {
			if (_showDirMon) {
				ret.append(",Direct Monthly Sales");
			}
			
			if (_showDirYTD) {
				ret.append(",Direct YTD Sales");
			}
			
			if (_showDirPrevYTD) {
				ret.append(",Direct Prev YTD Sales");
			}
			
			if (_showDirPrevTot) {
				ret.append(",Direct Prev Yr Tot Sales");
			}
			
			if (_showDirPrevMo) {
				ret.append(",Direct Prev Year's Monthly Sales");
			}
		}
		
		if (_showDirOrder) {
			if (_showDirMon) {
				ret.append(",Direct Monthly Orders");
			}
			
			if (_showDirYTD) {
				ret.append(",Direct YTD Orders");
			}
			
			if (_showDirPrevYTD) {
				ret.append(",Direct Prev YTD Orders");
			}
			
			if (_showDirPrevTot) {
				ret.append(",Direct Prev Yr Tot Orders");
			}
			
			if (_showDirPrevMo) {
				ret.append(",Direct Prev Year's Monthly Sales");
			}
		}
		
		if (_showSSO) {
			if (_showSSOMon) {
				ret.append(",SSO Monthly Dollars");
			}
			
			if (_showSSOYTD) {
				ret.append(",SSO YTD Dollars");
			}
			
			if (_showSSOPrevYTD) {
				ret.append(",SSO Prev YTD Dollars");
			}
			
			if (_showSSOPrevTot) {
				ret.append(",SSO Prev Yr Tot Dollars");
			}
			
			if (_showSSOPrevMo) {
				ret.append(",SSO Prev Year's Monthly Dollars");
			}
		}
		
		for (int i=0; i < _results.size(); i++) {
			SearchResults res = (SearchResults)_results.get(i);
			ret.append("\n");
			ret.append(res.getId());
			ret.append(",");
			ret.append(res.getDescription());
			
			if (_showPot) {
				ret.append(",");
				ret.append(res.getPotentialDollars());
			}
			
			if (_showFor) {
				ret.append(",");
				ret.append(res.getForecastDollars());
			}
			
			if (_showComp) {
				ret.append(",");
				ret.append(res.getCompetitorDollars());
			}
			
			if (_showCrSales) {
				if (_showCrMon) {
					ret.append(",");
					ret.append(res.getCRCurMoSales());
				}
				
				if (_showCrYTD) {
					ret.append(",");
					ret.append(res.getCRYTDSales());
				}
				
				if (_showCrPrevYTD) {
					ret.append(",");
					ret.append(res.getCRPrevYTDSales());
				}
				
				if (_showCrPrevTot) {
					ret.append(",");
					ret.append(res.getCRPrevYrTotSales());
				}
				
				if (_showCrPrevMo) {
					ret.append(",");
					ret.append(res.getCRPrevYrMoSales());
				}
			}
			
			if (_showCrOrder) {
				if (_showCrMon) {
					ret.append(",");
					ret.append(res.getCRCurMoOrder());
				}
				
				if (_showCrYTD) {
					ret.append(",");
					ret.append(res.getCRYTDOrder());
				}
				
				if (_showCrPrevYTD) {
					ret.append(",");
					ret.append(res.getCRPrevYTDOrder());
				}
				
				if (_showCrPrevTot) {
					ret.append(",");
					ret.append(res.getCRPrevYrTotOrder());
				}
				
				if (_showCrPrevMo) {
					ret.append(",");
					ret.append(res.getCRPrevYrMoOrder());
				}
			}
			
			if (_showEMSales) {
				if (_showEMMon) {
					ret.append(",");
					ret.append(res.getEMCurMoSales());
				}
				
				if (_showEMYTD) {
					ret.append(",");
					ret.append(res.getEMYTDSales());
				}
				
				if (_showEMPrevYTD) {
					ret.append(",");
					ret.append(res.getEMPrevYTDSales());
				}
				
				if (_showEMPrevTot) {
					ret.append(",");
					ret.append(res.getEMPrevYrTotSales());
				}
				
				if (_showEMPrevMo) {
					ret.append(",");
					ret.append(res.getEMPrevYrMoSales());
				}
			}
			
			if (_showEMOrder) {
				if (_showEMMon) {
					ret.append(",");
					ret.append(res.getEMCurMoOrder());
				}
				
				if (_showEMYTD) {
					ret.append(",");
					ret.append(res.getEMYTDOrder());
				}
				
				if (_showEMPrevYTD) {
					ret.append(",");
					ret.append(res.getEMPrevYTDOrder());
				}
				
				if (_showEMPrevTot) {
					ret.append(",");
					ret.append(res.getEMPrevYrTotOrder());
				}
				
				if (_showEMPrevMo) {
					ret.append(",");
					ret.append(res.getEMPrevYrMoOrder());
				}
			}
			
			if (_showDirSales) {
				if (_showDirMon) {
					ret.append(",");
					ret.append(res.getDirCurMoSales());
				}
				
				if (_showDirYTD) {
					ret.append(",");
					ret.append(res.getDirYTDSales());
				}
				
				if (_showDirPrevYTD) {
					ret.append(",");
					ret.append(res.getDirPrevYTDSales());
				}
				
				if (_showDirPrevTot) {
					ret.append(",");
					ret.append(res.getDirPrevYrTotSales());
				}
				
				if (_showDirPrevMo) {
					ret.append(",");
					ret.append(res.getDirPrevYrMoSales());
				}
			}
			
			if (_showDirOrder) {
				if (_showDirMon) {
					ret.append(",");
					ret.append(res.getDirCurMoOrder());
				}
				
				if (_showDirYTD) {
					ret.append(",");
					ret.append(res.getDirYTDOrder());
				}
				
				if (_showDirPrevYTD) {
					ret.append(",");
					ret.append(res.getDirPrevYTDOrder());
				}
				
				if (_showDirPrevTot) {
					ret.append(",");
					ret.append(res.getDirPrevYrTotOrder());
				}
				
				if (_showDirPrevMo) {
					ret.append(",");
					ret.append(res.getDirPrevYrMoOrder());
				}
			}
			
			if (_showSSO) {
				if (_showSSOMon) {
					ret.append(",");
					ret.append(res.getSSOCurMoOrder());
				}
				
				if (_showSSOYTD) {
					ret.append(",");
					ret.append(res.getSSOYTD());
				}
				
				if (_showSSOPrevYTD) {
					ret.append(",");
					ret.append(res.getSSOPrevYTD());
				}
				
				if (_showSSOPrevTot) {
					ret.append(",");
					ret.append(res.getSSOPrevYrTot());
				}
				
				if (_showSSOPrevMo) {
					ret.append(",");
					ret.append(res.getSSOPrevYrMo());
				}
			}
		}
		
		return ret.toString();
	}
	
	private void saveFile(String table,String filename) {
		try {
			FileOutputStream out = new FileOutputStream (filename);
			
			out.write(table.getBytes());
			out.close();
		}
		catch (Exception e) {
		    SMRCLogger.error(this.getClass().getName() + ".saveFile: ",  e);
		}
	}
	
	public void run() {
		String table = getData();
		Calendar timeStamp = Calendar.getInstance();
		
		String filename = _folder + _user.getUserid() + "_report_extract_" +
		timeStamp.get(Calendar.MONTH) + "_" + timeStamp.get(Calendar.DATE) + "_" +
		timeStamp.get(Calendar.YEAR) + "_" + timeStamp.get(Calendar.HOUR) + "_" +
		timeStamp.get(Calendar.SECOND) + "_" + timeStamp.get(Calendar.MILLISECOND) + ".csv";
		saveFile(table,filename);
		
		try {
			String from = "oemaccountplanner@eaton.com";
			String to = _user.getEmailAddress().trim();
			
			try {
				TAPMail tapmail = new TAPMail();
				tapmail.addAttachment(filename);
				tapmail.setSenderInfo("Target Account Planner", from);
				tapmail.addRecipient(to);
				String msg_text = getText();
				tapmail.sendMessage(msg_text, "Target Account Planner Report for Excel");
				
			}
			catch(Exception e) {
			    SMRCLogger.error(this.getClass().getName() + ".run(): ", e);
				errors.append(e.getMessage());
				logErrors();
			}
		}
		catch (Exception e) {}
	}
	
	private void logErrors() {
		try {
			Calendar now = Calendar.getInstance();
			
			FileOutputStream out = new FileOutputStream (_errFolder + "status_email.errors.log." +
					now.get(Calendar.YEAR) + now.get(Calendar.MONTH) + now.get(Calendar.DATE) +
					now.get(Calendar.HOUR_OF_DAY) + now.get(Calendar.MINUTE) + now.get(Calendar.SECOND) +
					now.get(Calendar.MILLISECOND));
			
			out.write(errors.toString().getBytes());
			out.close();
		}
		catch (Exception e) {
		    SMRCLogger.error(this.getClass().getName() + ".logErrors(): ", e);
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
}
