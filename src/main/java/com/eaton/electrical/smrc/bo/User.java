package com.eaton.electrical.smrc.bo;
import java.util.*;

public class User extends Object implements java.io.Serializable {
	private static final String ZONE_HOME_PAGE="zone";
	private static final String DISTRICT_HOME_PAGE="district";
	private static final String SE_HOME_PAGE="se";
	private static final String OTHER_HOME_PAGE="other";
	
	private static final String GROUP_ZM="Zone Manager";
	private static final String GROUP_DM="District Manager";
	private static final String GROUP_SE="Sales Engineer";
	private static final String GROUP_TL="Team Leader";
	private static final String GROUP_CHANNEL_MANAGER="Global Channel Manager";
	private static final String GROUP_CREDIT_MANAGER="Credit Manager";
	private static final String GROUP_OTHER="N/A - Other";
	private static final String GROUP_DIVISION_MANAGER = "Division Manager";
	private static final String GROUP_CHAMPS_MANAGER = "CHAMPS Manager";
	private static final String GROUP_PROJECT_SALES_MANAGER = "Project Sales Manager";
	
	private String _geog = null;
	private String _salesId = null;
	private List salesIds = null;
	private String _userid = null;
	private String _vistaId = null;
	private String _firstName = null;
	private String _lastName = null;
	private String _emailAddress = null;
	private boolean _setSecurity = false;
	private boolean _overrideSecurity = false;
	private boolean _viewAll = false;
	private boolean viewProfile = false;
	private ArrayList _ugs = null;
	private String titleTx = null;
	private int groupId = -1;
	private String userGroup = null;
	private String jobTitle = null;
	private String homePage = null;
	private boolean ableToSeeDivisionCSF = false;
	private boolean ableToSeeDistrictCSF = false;
	private String showSalesOrders = null;
	private boolean useCredit = true;
	private boolean useEndMarket = false;
	private boolean useDirect = false;
	private boolean useSSO = false;
//	private int dollarTypeCode = -1;
	private ArrayList segmentOverrides = null;
	// The userGroupxxxxx fields are used only if the user has records
	// on the user group xref table. For now, that should only be Division Managers
	// and Project Sales Managers
	private String userGroupDivisionId = null;
	private String userGroupGeog = null;
	
	private static final long serialVersionUID = 100;

	public User() {
		_geog = "";
		_salesId = "";
		_userid = "";
		_vistaId = "";
		_firstName = "";
		_lastName = "";
		_emailAddress = "";
		_ugs = new ArrayList();
		titleTx = "";
		userGroup = "";
		jobTitle = "";
		homePage = "";
		showSalesOrders = "s";
		segmentOverrides=new ArrayList();
		salesIds = new ArrayList();
	}
	public void setTitleTx(String titleTx) {
		this.titleTx = titleTx;
	}
	public String getTitleTx() {
		return titleTx;
	}
	/**
	 * Allows the calling routine to store the userid of this user
	 * 
	 * @param String
	 *            The userid of this user
	 */
	public void setUserid(String userid) {
		_userid = userid;
	}
	/**
	 * Allows the calling routine to store the Vista id of this user
	 * 
	 * @param String
	 *            the Vista id of this user.
	 */
	public void setVistaId(String vistaId) {
		_vistaId = vistaId;
	}
	/**
	 * Allows the calling routine to store the geography of this user
	 * 
	 * @param String
	 *            The geography of this user
	 */
	public void setGeography(String geog) {
		_geog = geog;
	}
	/**
	 * Allows the calling routine to store the first name of this user
	 * 
	 * @param String
	 *            The first name of this user
	 */
	public void setFirstName(String first) {
		_firstName = first;
	}
	/**
	 * Allows the calling routine to store the last name of this user
	 * 
	 * @param String
	 *            the last name of this user
	 */
	public void setLastName(String last) {
		_lastName = last;
	}
	/**
	 * Allows the calling routine to store the sales id of this user
	 * 
	 * @param String
	 *            the sales id of this user
	 */
	public void setSalesId(String salesId) {
		_salesId = salesId;
	}
	/**
	 * Allows the calling routine to store the email address of this user
	 * 
	 * @param String
	 *            the email address of this user
	 */
	public void setEmailAddress(String emailAddress) {
		_emailAddress = emailAddress;
	}
	/**
	 * Allows the calling routine to set whether this user can set security for
	 * other users
	 * 
	 * @param boolean
	 *            T/F indicating whether the user can set security for other
	 *            users
	 */
	public void setSetSecurity(boolean setSecurity) {
		_setSecurity = setSecurity;
	}

	/**
	 * Allows the calling routine to set whether this user is allowed to view
	 * and update everything in the application - no matter what their profile
	 * indicates.
	 * 
	 * @param boolean
	 *            T/F indicating whether this user is allowed to view everything
	 *            in the application
	 */
	public void setOverrideSecurity(boolean overrideSecurity) {
		_overrideSecurity = overrideSecurity;
	}
	/**
	 * Allows the calling routine to set whether this user is allowed to view
	 * everything in the application - no matter what their profile indicates.
	 * 
	 * @param boolean
	 *            T/F indicating whether this user is allowed to view everything
	 *            in the application
	 */
	public void setViewEverything(boolean viewAll) {
		_viewAll = viewAll;
	}
	/**
	 * Allows the calling routine to set whether this user is allowed to view
	 * everything in the application - no matter what their profile indicates.
	 * 
	 * @param String
	 *            Y/N indicating whether this user is allowed to view everything
	 *            in the application
	 */
	public void setViewEverything(String viewAll) {
		_viewAll = (viewAll.equals("Y"));
	}
	/**
	 * Some users have override access at the geography level. This tracks these
	 * overrides
	 * 
	 * @param String
	 *            The sp_geog that the user has override access to
	 */
	public void addGeogSecurity(UserGeogSecurity ugs) {
		_ugs.add(ugs);
	}

	public String getGeography() {
		return _geog;
	}

	public String getSegment() {
		if (_geog.length() > 0) {
			return _geog.substring(2, 5);
		}
		return "";
		
	}

	public String getUserid() {
		return _userid;
	}

	public String getVistaId() {
		return _vistaId;
	}

	public String getFirstName() {
		return _firstName;
	}

	public String getLastName() {
		return _lastName;
	}

	public String getSalesId() {
		return _salesId;
	}

	public String getEmailAddress() {
		return _emailAddress;
	}

	public boolean ableToSetSecurity() {
		return _setSecurity;
	}

	public boolean hasOverrideSecurity() {
		return _overrideSecurity;
	}
	
	/*
	 * For setting and getting segment Overrides
	 */
	public void addSegmentOverride(int segmentId) {
		segmentOverrides.add(new Integer(segmentId));
	}
	public ArrayList getSegmentOverrides(){
		return segmentOverrides;
	}
	public boolean hasSegmentOverride(Account acct){		
		ArrayList overrideSegmentIds = getSegmentOverrides();
		ArrayList accountSegments = acct.getSegments();

		for(int i=0;i<overrideSegmentIds.size();i++){
			int id = ((Integer)overrideSegmentIds.get(i)).intValue();
			for(int j=0;j<accountSegments.size();j++){
				Segment seg = (Segment)accountSegments.get(j);
				if(id==seg.getSegmentId()) return true;
			}	
		}
		return false;
	}
	
	public boolean ableToUpdate(String geog) {
		//if (hasOverrideSecurity() || geog.trim().length()==0) {
	    if (hasOverrideSecurity()) {
		    return true;
		}
		
		boolean yn = false;

		char[] gChars = new char[6];
		char[] tmp = geog.toCharArray();
		for (int j = 0; j < tmp.length; j++) {
			gChars[j] = tmp[j];
		}
		for (int j = tmp.length; j < gChars.length; j++) {
			gChars[j] = ' ';
		}
		for (int i = 0; i < _ugs.size(); i++) {
			UserGeogSecurity ugs = (UserGeogSecurity) _ugs.get(i);
			if (ugs.getSPGeog().equals(geog) && ugs.ableToUpdate()) {
			    yn = true;
				break;
			} else if (ugs.ableToUpdate()) {
				char[] tChars = new char[6];
				tmp = ugs.getSPGeog().toCharArray();
				for (int j = 0; j < tmp.length; j++) {
					tChars[j] = tmp[j];
				}
				for (int j = tmp.length; j < tChars.length; j++) {
					tChars[j] = ' ';
				}
				boolean good = true;
				for (int j = gChars.length - 1; j >= 0; j--) {
					if (gChars[j] != tChars[j] && tChars[j] != ' '
							&& tChars[j] != '0') {
						good = false;
						break;
					}
				}
				yn = good;
				if (yn) {
					break;
				}
			}
		}

		return yn;
	}

	/*
	 * TODO
	 * Customer should eventually be replaced by account, so this overloaded
	 * method will replace the ableToUpdate(Customer c)
	 * All this security code needs evaluated for re-coding
	 */
	public boolean ableToUpdate(Account account) {
		if (ableToUpdate(account.getSalesEngineer1()) || 
		        ableToUpdate(account.getSalesEngineer2()) ||
		        ableToUpdate(account.getSalesEngineer3()) ||
		        ableToUpdate(account.getSalesEngineer4()) ||
		        ableToUpdate(account.getDistrict()) ||
		        hasSegmentOverride(account)) {
			return true;
		}

		return false;

	}
	
	public boolean ableToUpdate(Customer c) {
		if (ableToUpdate(c.getSalesId()) || ableToUpdate(c.getSPGeog())) {
			return true;
		}
		return false;

	}
	
	public boolean ableToSee(String geog) {
		
		if (hasOverrideSecurity() || ableToViewEverything()) {
			return true;
		}

		boolean yn = false;
		
		char[] gChars = new char[6];
		char[] tmp = geog.toCharArray();
		for (int j = 0; j < tmp.length; j++) {
			gChars[j] = tmp[j];
		}
		for (int j = tmp.length; j < gChars.length; j++) {
			gChars[j] = ' ';
		}
		for (int i = 0; i < _ugs.size(); i++) {
			UserGeogSecurity ugs = (UserGeogSecurity) _ugs.get(i);
			if (ugs.getSPGeog().equals(geog)) {
				yn = true;
				break;
			}
			char[] tChars = new char[6];
			tmp = ugs.getSPGeog().toCharArray();
			for (int j = 0; j < tmp.length; j++) {
				tChars[j] = tmp[j];
			}
			for (int j = tmp.length; j < tChars.length; j++) {
				tChars[j] = ' ';
			}
			boolean good = true;
			for (int j = gChars.length - 1; j >= 0; j--) {
				if (gChars[j] != tChars[j] && tChars[j] != ' '
						&& tChars[j] != '0') {
					good = false;
					break;
				}
			}
			if (good) {
				if (!ugs.ableToSeeTeamLevel() && isTeam(geog)) {
					good = false;
				} else if (!ugs.ableToSeeDistrictLevel()
						&& isDistrict(geog)) {
					good = false;
				} else if (!ugs.ableToSeeZoneLevel() && isZone(geog)) {
					good = false;
				} else if (!ugs.ableToSeeGroupLevel() && isGroup(geog)) {
					good = false;
				} else if (!ugs.ableToSeeNationalLevel()
						&& isNational(geog)) {
					good = false;
				}
			}
			yn = good;
			if (yn) {
				break;
			}
		}
		
		return yn;
	}
	
	
	public boolean ableToSee(Customer c) {
		
		Account acct = new Account();
		acct.setDistrict(c.getSPGeog());
		acct.setSalesEngineer1(c.getSalesId());
		acct.setSalesEngineer2(c.getSalesEngineer2());
		acct.setSalesEngineer3(c.getSalesEngineer3());
		acct.setSalesEngineer4(c.getSalesEngineer4());
		return ableToSee(acct);
		
		/*
		 * TODO the following is commented out because ableToSee(Customer c) simply
		 * sets an account object and calls ableToSee(Account).  This method
		 * will eventually go away once the Customer object is removed
		 */ 
		/*
		if (hasOverrideSecurity() || ableToViewEverything()) {
			return true;
		}
		
		boolean yn = false;
		String geog = c.getSPGeog();
		String salesId = c.getSalesId();

		char[] gChars = new char[6];
		char[] tmp = geog.toCharArray();
		for (int j = 0; j < tmp.length; j++) {
			gChars[j] = tmp[j];
		}
		for (int j = tmp.length; j < gChars.length; j++) {
			gChars[j] = ' ';
		}
		for (int i = 0; i < _ugs.size(); i++) {
			UserGeogSecurity ugs = (UserGeogSecurity) _ugs.get(i);
			if ((ugs.getSPGeog().equals(geog) && ugs.ableToViewSalesman()) || ugs.getSPGeog().equals(salesId)) {
				yn = true;
				break;
			} else if (ugs.ableToViewSalesman()) {
				char[] tChars = new char[6];
				tmp = ugs.getSPGeog().toCharArray();
				for (int j = 0; j < tmp.length; j++) {
					tChars[j] = tmp[j];
				}
				for (int j = tmp.length; j < tChars.length; j++) {
					tChars[j] = ' ';
				}
				boolean good = true;
				for (int j = gChars.length - 1; j >= 0; j--) {
					if (gChars[j] != tChars[j] && tChars[j] != ' '
							&& tChars[j] != '0') {
						good = false;
						break;
					}
				}
				if (good) {
					if (!ugs.ableToSeeTeamLevel() && isTeam(geog)) {
						good = false;
					} else if (!ugs.ableToSeeDistrictLevel()
							&& isDistrict(geog)) {
						good = false;
					} else if (!ugs.ableToSeeZoneLevel() && isZone(geog)) {
						good = false;
					} else if (!ugs.ableToSeeGroupLevel() && isGroup(geog)) {
						good = false;
					} else if (!ugs.ableToSeeNationalLevel()
							&& isNational(geog)) {
						good = false;
					}
				}
				yn = good;
				if (yn) {
					break;
				}
			}
		}

		return yn;
		*/
	}
	


	/*
	 * TODO
	 * Customer should eventually be replaced by account, so this overloaded
	 * method will replace the ableToSee(Customer c)
	 * All this security code needs evaluated for re-coding
	 */
	public boolean ableToSee(Account account) {
		if (hasOverrideSecurity() || ableToViewEverything() || hasSegmentOverride(account)) {
			return true;
		}
		
		boolean yn = false;
		String geog = account.getDistrict();
		String salesId = account.getSalesEngineer1();
		String salesId2 = account.getSalesEngineer2();
		String salesId3 = account.getSalesEngineer3();
		String salesId4 = account.getSalesEngineer4();

		char[] gChars = new char[6];
		char[] tmp = geog.toCharArray();
		for (int j = 0; j < tmp.length; j++) {
			gChars[j] = tmp[j];
		}
		for (int j = tmp.length; j < gChars.length; j++) {
			gChars[j] = ' ';
		}
		for (int i = 0; i < _ugs.size(); i++) {
			UserGeogSecurity ugs = (UserGeogSecurity) _ugs.get(i);
			if ((ugs.getSPGeog().equals(geog) && ugs.ableToViewSalesman()) || 
			        ugs.getSPGeog().equals(salesId) ||
			        ugs.getSPGeog().equals(salesId2) ||
			        ugs.getSPGeog().equals(salesId3) ||
			        ugs.getSPGeog().equals(salesId4)) {
				yn = true;
				break;
			} else if (ugs.ableToViewSalesman()) {
				char[] tChars = new char[6];
				tmp = ugs.getSPGeog().toCharArray();
				for (int j = 0; j < tmp.length; j++) {
					tChars[j] = tmp[j];
				}
				for (int j = tmp.length; j < tChars.length; j++) {
					tChars[j] = ' ';
				}
				boolean good = true;
				for (int j = gChars.length - 1; j >= 0; j--) {
					if (gChars[j] != tChars[j] && tChars[j] != ' '
							&& tChars[j] != '0') {
						good = false;
						break;
					}
				}
				if (good) {
					if (!ugs.ableToSeeTeamLevel() && isTeam(geog)) {
						good = false;
					} else if (!ugs.ableToSeeDistrictLevel()
							&& isDistrict(geog)) {
						good = false;
					} else if (!ugs.ableToSeeZoneLevel() && isZone(geog)) {
						good = false;
					} else if (!ugs.ableToSeeGroupLevel() && isGroup(geog)) {
						good = false;
					} else if (!ugs.ableToSeeNationalLevel()
							&& isNational(geog)) {
						good = false;
					}
				}
				yn = good;
				if (yn) {
					break;
				}
			}
		}

		return yn;
	}
	
	// Returns true if user can see the specified level for the specified
	// geography
	public boolean ableToSeeThisLevel(String geog, String level) {
		if (hasOverrideSecurity() || ableToViewEverything()) {
			return true;
		}
		boolean yn = false;

		char[] gChars = new char[6];
		char[] tmp = geog.toCharArray();
		for (int j = 0; j < tmp.length; j++) {
			gChars[j] = tmp[j];
		}
		for (int j = tmp.length; j < gChars.length; j++) {
			gChars[j] = ' ';
		}
		for (int i = 0; i < _ugs.size(); i++) {
			UserGeogSecurity ugs = (UserGeogSecurity) _ugs.get(i);
    //    I don't think we want to return a true just because the geography matches 
	//    a geography in the ugs record. We should still verify the level below.
	//		if ((ugs.getSPGeog().equals(geog)) && (!level.equalsIgnoreCase("ACCOUNT"))) {
	//			yn = true;
	//			break;
	//		}
			char[] tChars = new char[6];
			tmp = ugs.getSPGeog().toCharArray();
			for (int j = 0; j < tmp.length; j++) {
				tChars[j] = tmp[j];
			}
			for (int j = tmp.length; j < tChars.length; j++) {
				tChars[j] = ' ';
			}
			boolean good = true;
			for (int j = gChars.length - 1; j >= 0; j--) {
				if (gChars[j] != tChars[j] && tChars[j] != ' '
						&& tChars[j] != '0') {
					good = false;
					break;
				}
			}
			if (good) {
				if (!ugs.ableToSeeTeamLevel()
						&& level.equalsIgnoreCase("TEAM")) {
					good = false;
				} else if (!ugs.ableToSeeDistrictLevel()
						&& level.equalsIgnoreCase("DISTRICT")) {
					good = false;
				} else if (!ugs.ableToSeeZoneLevel()
						&& level.equalsIgnoreCase("ZONE")) {
					good = false;
				} else if (!ugs.ableToSeeGroupLevel()
						&& level.equalsIgnoreCase("GROUP")) {
					good = false;
				} else if (!ugs.ableToSeeNationalLevel()
						&& level.equalsIgnoreCase("NATIONAL")) {
					good = false;
				} else if (!ugs.ableToViewSalesman()
						&& level.equalsIgnoreCase("ACCOUNT")) {
					good = false;
				}
			}
			yn = good;
			if (yn) {
				break;
			}
		}
		return yn;
	}
	
	

	
	public boolean ableToViewEverything() {
		return _viewAll;
	}

	public boolean isSalesEngineer() {
		if (titleTx.equals("AE") || titleTx.equals("AE-C") || titleTx.equals("AE-I") || titleTx.equals("DSS") || titleTx.equals("ISE") || titleTx.equals("REP") || titleTx.equals("SE") || titleTx.equals("TRAIN")){
			return true;
		}
		return false;

	}

	public boolean isDistrictManager() {
		if (titleTx.equals("BM") || titleTx.equals("DM") || titleTx.equals("SEC")){
			return true;
		}
		return false;

	}
	
	public boolean isChannelMarketingManager() {
		if(getUserGroup().equals(GROUP_CHANNEL_MANAGER)){
			return true;
		}
		return false;

	}
	
	public boolean isCHAMPSManager() {
		if(getUserGroup().equals(GROUP_CHAMPS_MANAGER)){
			return true;
		}
		return false;

	}
	
	public boolean isProjectSalesManager() {
		if(getUserGroup().equals(GROUP_PROJECT_SALES_MANAGER)){
			return true;
		}
		return false;

	}
	
	public boolean isCreditManager() {
		if(getUserGroup().equals(GROUP_CREDIT_MANAGER)){
			return true;
		}
		return false;

	}
	
	public boolean isDivisionManager(){
	    if(getUserGroup().equals(GROUP_DIVISION_MANAGER)){
	        return true;
	    }
	    return false;
	}
	
	public boolean isZoneManager() {
		if (titleTx.equalsIgnoreCase("ZM")) {
			return true;
		}
		return false;

	}
	
	public boolean isMarketingMgr() {
		if (titleTx.equalsIgnoreCase("MM")) {
			return true;
		}
		return false;

	}
	
	public boolean isTeamLeader() {
		if (titleTx.equalsIgnoreCase("TL")) {
			return true;
		}
		return false;

	}
	
	public boolean isOtherUser() {
		if (isCreditManager() || isChannelMarketingManager() || (!isSalesEngineer() && !isZoneManager() && !isDistrictManager())){
			return true;
		}
		return false;

	}

	public String getUserGroup() {
		if(!userGroup.equals("")){
			return userGroup;
		}else if(isZoneManager()){
			return GROUP_ZM;
		}else if(isDistrictManager()){
			return GROUP_DM;
		}else if(isSalesEngineer()){
			return GROUP_SE;
		}else if(isTeamLeader()){
			return GROUP_TL;
		}else{
			return GROUP_OTHER;
		}
	}
	
	public String getHomePage() {
		if(!homePage.equals("")){
			return homePage;
		}else if(isZoneManager()){
			return ZONE_HOME_PAGE;
		}else if(isDistrictManager()){
			return DISTRICT_HOME_PAGE;
		}else if(isSalesEngineer()){
			return SE_HOME_PAGE;
		}else if(isTeamLeader()){
			return DISTRICT_HOME_PAGE;			
		}else{
			return OTHER_HOME_PAGE;
		}
	}
	
	public ArrayList getAllUGS() {
		return _ugs;
	}
	/*
	private boolean isSalesId(String in) {
		return (in.length() == 4);
	}
	*/
	private boolean isTeam(String in) {
		return (in.length() == 6 && in.charAt(5) != ' ');
	}
	private boolean isDistrict(String in) {
		return (in.length() == 5 && in.charAt(4) != '0');
	}
	private boolean isZone(String in) {
		return (in.length() == 5 && in.charAt(4) == '0' && in.charAt(3) != '0' && in
				.charAt(2) != '0');
	}
	private boolean isGroup(String in) {
		return (in.length() == 5 && in.charAt(4) == '0' && in.charAt(3) == '0'
				&& in.charAt(2) == '0' && in.charAt(1) != '0');
	}
	private boolean isNational(String in) {
		return (in.length() == 5 && in.charAt(4) == '0' && in.charAt(3) == '0'
				&& in.charAt(2) == '0' && in.charAt(1) == '0' && in.charAt(0) != '0');
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public boolean isAbleToSeeDistrictCSF() {
		return ableToSeeDistrictCSF;
	}
	public void setAbleToSeeDistrictCSF(boolean ableToSeeDistrictCSF) {
		this.ableToSeeDistrictCSF = ableToSeeDistrictCSF;
	}
	public boolean isAbleToSeeDivisionCSF() {
		return ableToSeeDivisionCSF;
	}
	public void setAbleToSeeDivisionCSF(boolean ableToSeeDivisionCSF) {
		this.ableToSeeDivisionCSF = ableToSeeDivisionCSF;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	
	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}
	public String getShowSalesOrders() {
		return showSalesOrders;
	}
	public void setShowSalesOrders(String showSalesOrders) {
		this.showSalesOrders = showSalesOrders;
	}

	public boolean isUseCredit() {
		return useCredit;
	}
	public void setUseCredit(boolean useCredit) {
		this.useCredit = useCredit;
	}
	public boolean isUseDirect() {
		return useDirect;
	}
	public void setUseDirect(boolean useDirect) {
		this.useDirect = useDirect;
	}
	public boolean isUseEndMarket() {
		return useEndMarket;
	}
	public void setUseEndMarket(boolean useEndMarket) {
		this.useEndMarket = useEndMarket;
	}
	public boolean isUseSSO() {
		return useSSO;
	}
	public void setUseSSO(boolean useSSO) {
		this.useSSO = useSSO;
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
	
	/*
	 * Check to see if the userid passed in equals the userId of the User object
	 */
	public boolean equals(String id) {
		if (id == null || id.trim().length()==0) {
			return false;
		}else if(id.equalsIgnoreCase(this.getUserid())){
			return true;
		}
		return false;
	}
	
	public String toString() {
		StringBuffer returnBuffer= new StringBuffer();
		returnBuffer.append("\tuserid = " + this.getUserid() + "\n");
		returnBuffer.append("\tgeography = " + this.getGeography() + "\n");
		returnBuffer.append("\tvistaId = " + this.getVistaId() + "\n");
		returnBuffer.append("\tfirstName = " + this.getFirstName() + "\n");
		returnBuffer.append("\tlastName = " + this.getLastName() + "\n");
		returnBuffer.append("\tsalesId = " + this.getSalesId() + "\n");
		returnBuffer.append("\tsalesIds = " + this.salesIdsToString() + "\n");
		returnBuffer.append("\temailAddress = " + this.getEmailAddress() + "\n");
		returnBuffer.append("\tgroupId = " + this.getGroupId() + "\n");
		returnBuffer.append("\tuserGroup = " + this.getUserGroup() + "\n");
		returnBuffer.append("\thomePage = " + this.getHomePage() + "\n");
		returnBuffer.append("\tjobTitle = " + this.getJobTitle() + "\n");
		returnBuffer.append("\ttitleTx = " + this.getTitleTx() + "\n");
		returnBuffer.append("\tshowSalesOrders = " + this.getShowSalesOrders() + "\n");
		returnBuffer.append("\tableToSetSecurity? = " + this.ableToSetSecurity() + "\n");
		returnBuffer.append("\tableToViewEverything? = " + this.ableToViewEverything() + "\n");
		returnBuffer.append("\thasOverrideSecurity? = " + this.hasOverrideSecurity() + "\n");
		returnBuffer.append("\tisAbleToSeeDistrictCSF? = " + this.isAbleToSeeDistrictCSF() + "\n");
		returnBuffer.append("\tisAbleToSeeDivisionCSF? = " + this.isAbleToSeeDivisionCSF() + "\n");
		returnBuffer.append("\tisChannelMarketingManager? = " + this.isChannelMarketingManager() + "\n");
		returnBuffer.append("\tisCreditManager? = " + this.isCreditManager() + "\n");
		returnBuffer.append("\tisDistrictManager? = " + this.isDistrictManager() + "\n");
		returnBuffer.append("\tisMarketingMgr? = " + this.isMarketingMgr() + "\n");
		returnBuffer.append("\tisZoneManager? = " + this.isZoneManager() + "\n");
		returnBuffer.append("\tisOtherUser? = " + this.isOtherUser() + "\n");
		returnBuffer.append("\tisSalesEngineer? = " + this.isSalesEngineer() + "\n");
		returnBuffer.append("\tisUseCredit? = " + this.isUseCredit() + "\n");
		returnBuffer.append("\tisUseDirect? = " + this.isUseDirect() + "\n");
		returnBuffer.append("\tisUseEndMarket? = " + this.isUseEndMarket() + "\n");
		returnBuffer.append("\tisUseSSO? = " + this.isUseSSO() + "\n");
		returnBuffer.append("\tableToViewProfile? = " + this.ableToViewProfile() + "\n");
		
		return returnBuffer.toString();
	}
	
	
	public String toHelpDeskString() {
		StringBuffer returnBuffer= new StringBuffer();
		returnBuffer.append("User ID = " + this.getUserid() + "<br>\n");
		returnBuffer.append("Geography = " + this.getGeography() + "<br>\n");
		returnBuffer.append("Vista Id = " + this.getVistaId() + "<br>\n");
		returnBuffer.append("First Name = " + this.getFirstName() + "<br>\n");
		returnBuffer.append("Last Name = " + this.getLastName() + "<br>\n");
		returnBuffer.append("Sales Id(s) = " + this.salesIdsToString() + "<br>\n");
		returnBuffer.append("Email = " + this.getEmailAddress() + "<br>\n");
		returnBuffer.append("Group ID = " + this.getGroupId() + "<br>\n");
		returnBuffer.append("User Group = " + this.getUserGroup() + "<br>\n");
		returnBuffer.append("Home Page = " + this.getHomePage() + "<br>\n");
		returnBuffer.append("Vista Title = " + this.getTitleTx() + "<br>\n");
		returnBuffer.append("Sales/Orders = " + this.getShowSalesOrders() + "<br>\n");
		returnBuffer.append("Able To Set Security? = " + this.ableToSetSecurity() + "<br>\n");
		returnBuffer.append("Able To View Everything? = " + this.ableToViewEverything() + "<br>\n");
		returnBuffer.append("Has Override Security? = " + this.hasOverrideSecurity() + "<br>\n");
		returnBuffer.append("Able to View Profiles? = " + this.ableToViewProfile() + "<br>\n");		
		returnBuffer.append("Is Able To See District CSF? = " + this.isAbleToSeeDistrictCSF() + "<br>\n");
		returnBuffer.append("Is Able To See Division CSF? = " + this.isAbleToSeeDivisionCSF() + "<br>\n");
		returnBuffer.append("Is Channel MarketingM anager? = " + this.isChannelMarketingManager() + "<br>\n");
		returnBuffer.append("Is Credit Manager? = " + this.isCreditManager() + "<br>\n");
		returnBuffer.append("Is District Manager? = " + this.isDistrictManager() + "<br>\n");
		returnBuffer.append("Is Marketing Mgr? = " + this.isMarketingMgr() + "<br>\n");
		returnBuffer.append("Is Zone Manager? = " + this.isZoneManager() + "<br>\n");
		returnBuffer.append("Is Other User? = " + this.isOtherUser() + "<br>\n");
		returnBuffer.append("Is Sales Engineer? = " + this.isSalesEngineer() + "<br>\n");
		returnBuffer.append("Use Credit Dollars? = " + this.isUseCredit() + "<br>\n");
		returnBuffer.append("Use Direct Dollars? = " + this.isUseDirect() + "<br>\n");
		returnBuffer.append("Use End Market Dollars? = " + this.isUseEndMarket() + "<br>\n");
		returnBuffer.append("Use SSO Dollars? = " + this.isUseSSO() + "<br>\n");
		
		
		return returnBuffer.toString();
	}

	/* Braffet : Removed for Tap dollars change
	public int getDollarTypeCode() {
		return dollarTypeCode;
	}
	public void setDollarTypeCode(int dollarTypeCode) {
		this.dollarTypeCode = dollarTypeCode;
	}
	
	*/

    public List getSalesIds() {
        return salesIds;
    }
    public void setSalesIds(List salesIds) {
        this.salesIds = salesIds;
    }
    public String salesIdsToString(){
        StringBuffer returnBuffer = new StringBuffer();
        Iterator it = salesIds.iterator();
        boolean addComma=false;
        while(it.hasNext()) {
            if(addComma==true) returnBuffer.append(", ");
            returnBuffer.append(it.next());
            addComma=true;
        }
        return returnBuffer.toString();
        
    }
    public String salesIdsToQuotedString(){
        StringBuffer returnBuffer = new StringBuffer();
        Iterator it = salesIds.iterator();
        boolean addComma=false;
        while(it.hasNext()) {
            if(addComma==true) returnBuffer.append(",");
            returnBuffer.append("'").append(it.next()).append("'");
            addComma=true;
        }
        return returnBuffer.toString();
        
    }
    public boolean isSalesId(String id) {
        Iterator it = salesIds.iterator();
        while(it.hasNext()) {
            if(it.next().equals(id)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean ableToViewProfile() {
        return viewProfile;
    }
    public void setViewProfile(boolean viewProfile) {
        this.viewProfile = viewProfile;
    }
    /**
     * @return Returns the userGroupDivisionId. This is the division id
     * for the division which this user is the Division Manager.
     */
    public String getUserGroupDivisionId() {
        return userGroupDivisionId;
    }
    /**
     * @param userGroupDivisionId The userGroupDivisionId to set.
     */
    public void setUserGroupDivisionId(String userGroupDivisionId) {
        this.userGroupDivisionId = userGroupDivisionId;
    }
    /**
     * @return Returns the userGroupGeog. This is the geog code for the 
     *  geography which this user is the Project Sales Manager.
     */
    public String getUserGroupGeog() {
        return userGroupGeog;
    }
    /**
     * @param userGroupGeog The userGroupGeog to set.
     */
    public void setUserGroupGeog(String userGroupGeog) {
        this.userGroupGeog = userGroupGeog;
    }
    
    public String getFullName(){
    	return (this.getFirstName() + " " + this.getLastName());
    	
    }
}