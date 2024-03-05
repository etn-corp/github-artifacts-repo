<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ include file="analytics.jsp" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
OEMAcctPlanBean OEMAPVars = (OEMAcctPlanBean)request.getAttribute("variables");

String genMsg = OEMAPVars.getGenMsg();
User user=OEMAPVars.getUser();
User maintUser = OEMAPVars.getMaintUser();
boolean userSameAsLoggedIn=false;
if(user.getUserid().equals(maintUser.getUserid())){
    userSameAsLoggedIn=true;
}  
boolean canViewProfile=maintUser.ableToViewProfile();
ArrayList dollarTypeCodes = (ArrayList)request.getAttribute("dollarTypeCodes");
request.setAttribute("header", OEMAPVars.getHeader());
%>
<html>
	<%@ include file="./TAPheader.jsp" %>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750">
				<p><br>
					<a class="crumb" href="SMRCHome">Home Page</a>
					<span class="crumbarrow">&gt;</span>
					<span class="crumbcurrent">User Profile</span>
				</p>
			</td>
		</tr>
	</table>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750">
				<p>&nbsp;</p>
				<% if(genMsg!=null){ %>
				<blockquote><font class="crumbcurrent"><%= genMsg %></font></blockquote>
				<% } %>
				<p class="heading2">User Profile</p>
				<div class="heading3"><%= user.getFirstName() %> <%= user.getLastName() %></div><br>
				<br>
				<form action="OEMAcctPlan" name="profileMaint" method="POST">
					<table width="100%" class=tableBorder cellpadding=1 cellspacing=1>
						<%
						if (maintUser.ableToSetSecurity() || canViewProfile) {
							TreeMap allUsersTm = (TreeMap)request.getAttribute("allUsersTm");
						%>
						<tr>
							<td class=cellColor width="30%"> Please choose a user to maintain:</td>
							<td class=cellColor width="70%">
							<input type="hidden" name="switchuser" value="false">
							<select name="newuser" class="textnormal">
								<%
				                Iterator it = allUsersTm.entrySet().iterator();
				                while(it.hasNext()) {
				                    String selected = "";
				                    Map.Entry e = (Map.Entry)it.next();
				                    if (e != null) {
				                    String key = (String)e.getKey();
				                    	if (key != null && key.length() > 0) {
				                    	    String value = (String)e.getValue();
				                    	    if(user.getUserid().equals(value)){
				                    	        selected = " selected";
				                    	    }
											%>
											<option value="<%= value %>"<%= selected %> class="textnormal"><%= key %></option>
				                    	    <%

				                    	} // end if key not null
				                    } // end if entry not null
				                }
				                %>
				           </select>
				           &nbsp; <input type="button" value="Go" onclick="changeUserMaint()">
							</td>
						</tr>
						<%
						}
						%>
						<tr>
							<td class=cellColor width="30%"><div class=crumb>Employee Number:</div></td>
							<td class=cellColor width="70%"><div class=textnormal><%= user.getUserid() %></div></td>
						</tr>
						<tr>
							<td class=cellColor><div class=crumb>First Name:</div></td>
							<td class=cellColor><div class=textnormal><%= user.getFirstName() %></div></td>
						</tr>
						<tr>
							<td class=cellColor><div class=crumb>Last Name:</div></td>
							<td class=cellColor><div class=textnormal><%= user.getLastName()%></div></td>
						</tr>
						<tr>
							<td class=cellColor><div class=crumb>Email Address:</div></td>
							<td class=cellColor><div class=textnormal><%= user.getEmailAddress() %></div></td>
						</tr>
						<% if(user.getVistaId().length()>0){ %>
						<tr>
							<td class=cellColor><div class=crumb>Vista Id:</div></td>
							<td class=cellColor><div class=textnormal><%= user.getVistaId() %></div></td>
						</tr>
						<% } %>
						<% if(user.getSalesId().trim().length()!=0){ %>
						<tr>
							<td class=cellColor><div class=crumb>Sales Id:</div></td>
							<td class=cellColor>
								<div class=textnormal>
								<%= user.salesIdsToString() %>
								</div>
							</td>
						</tr>
						<% } %>
						<% if(user.getTitleTx().trim().length()!=0){ %>
							<tr>
								<td class=cellColor><div class=crumb>Sales Reporting Job Title Code:</div></td>
								<td class=cellColor><div class=textnormal><%= user.getTitleTx() %></div></td>
							</tr>
						<% } %>						
						<tr>
							<td class=cellColor><div class=crumb>User Group:</div></td>
							<td class=cellColor><div class=textnormal><%= user.getUserGroup() %></div></td>
						</tr>

						<%
						if (maintUser.ableToSetSecurity()) {
						    String overSel = "";
							String setSel = "";
							String vaSel = "";
							String viewProfileSel = "";
							if (user.hasOverrideSecurity()) {
								overSel = " selected";
							}
							if (user.ableToSetSecurity()) {
								setSel = " selected";
							}
							if (user.ableToViewEverything()) {
								vaSel = " selected";
							}
							if (user.ableToViewProfile()) {
							    viewProfileSel = " selected";
							}
							%>
						<tr>
							<td class=cellColor><div class=crumb>Administrator</div></td>
							<td class=cellColor>
								<select name=setSecurity class=textnormal>
										<option value='N' class=textnormal>No</option>
										<option value='Y'<%= setSel %> class=textnormal>Yes</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class=cellColor><div class=crumb>Full Access</div></td>
							<td class=cellColor>
								<select name=overrideSecurity class=textnormal>
									<option value='N' class=textnormal>No</option>
									<option value='Y'<%= overSel %> class=textnormal>Yes</option>
								</select>							
							</td>
						</tr>
						<tr>
							<td class=cellColor><div class=crumb>View Everything</div></td>
							<td class=cellColor>
							    <select name=viewAll class=textnormal>
									<option value='N' class=textnormal>No</option>
									<option value='Y'<%= vaSel %> class=textnormal>Yes</option>
								</select>
							</td>
						</tr>
						<tr>
						<td class=cellColor><div class=crumb>View Profiles (HelpDesk)</div></td>
						<td class=cellColor>
						    <select name=viewProfiles class=textnormal>
								<option value='N' class=textnormal>No</option>
								<option value='Y'<%= viewProfileSel %> class=textnormal>Yes</option>
							</select>
						</td>
					</tr>						
						<%
				    
						}else {
						    if(canViewProfile) {
							    %>
									<tr>
									<td class=cellColor><div class=crumb>Administrator</div></td>
									<td class=cellColor>
									<%
									if(user.ableToSetSecurity()) {
									    out.println("Yes");
									}else{
									    out.println("No");
									}
									 %>
									</td>
								</tr>
								<tr>
									<td class=cellColor><div class=crumb>Full Access</div></td>
									<td class=cellColor>
									<%
									if(user.hasOverrideSecurity()) {
									    out.println("Yes");
									}else{
									    out.println("No");
									}
									%>								
									</td>
								</tr>
								<tr>
									<td class=cellColor><div class=crumb>View Everything</div></td>
									<td class=cellColor>
									<%
									if(user.ableToViewEverything()) {
									    out.println("Yes");
									}else{
									    out.println("No");
									}
									%>	
									</td>
								</tr>
								<tr>
								<td class=cellColor><div class=crumb>View Profiles (HelpDesk)</div></td>
								<td class=cellColor>
								<%
								if(user.ableToViewProfile()) {
								    out.println("Yes");
								}else{
								    out.println("No");
								}
								 %>
								</td>
							</tr>								
								<%
							    }
						    
							String overYN = "N";
							String setYN = "N";
							String vaYN = "N";
							String vpYN = "N";
							if (user.hasOverrideSecurity()) {
							    overYN = "Y";
							}
							if (user.ableToSetSecurity()) {
							    setYN = "Y";
							}
							if (user.ableToViewEverything()) {
							    vaYN = "Y";
							}
							if (user.ableToViewProfile()) {
							    vpYN = "Y";
							}							
							%>
							<input type=hidden name=overrideSecurity value=<%= overYN %>>
							<input type=hidden name=setSecurity value=<%= setYN %>>
							<input type=hidden name=viewAll value=<%= vaYN %>>
							<input type=hidden name=viewProfiles value=<%= vpYN %>>
							<%
						}
						if(canViewProfile) {
						    %>
							<tr>
							<td class=cellColor><div class=crumb>Troubleshooting Details</div></td>
							<td class=cellColor><%= user.toHelpDeskString() %>
							</td>
							</tr>
						    <%
						}
						int breakPt = 4;
						int colSpan = breakPt * 2;
						%>
						<tr><td class=cellColor colspan=3>
							</td></tr>
						<%
						if (maintUser.ableToSetSecurity() || canViewProfile) {
						    ArrayList geogs = OEMAPVars.getUserGeogOverrides();
						%>
						<tr>
							<td class=cellColor colspan=3>
								<table cellspacing=1 cellpadding=1 class=innerBorder align=center width="75%">
									<caption class=searchCenter2>
										<br>
										Provide user with full access to a geography
									</caption>
									<tr class=innerColor>
										<td class=searchLeft2>Delete</td>
										<td class=searchLeft2>Geography</td>
										<td class=searchLeft2>Level</td>
										<td class=searchLeft2>View Salesman</td>
										<td class=searchLeft2>Update</td>
									</tr>
									<%
									for (int i=0; i < geogs.size(); i++) {
									UserGeogSecurity ugs = (UserGeogSecurity)geogs.get(i);
									String slsmnCheck = "";
									String updCheck = "";
									boolean slsmnYN = false;
									boolean updYN = false;
									String natlSel = "";
									String grpSel = "";
									String zoneSel = "";
									String distSel = "";
									String teamSel = "";
									if (ugs.ableToViewSalesman()) {
										slsmnCheck = " checked";
										slsmnYN = true;
									}
									if (ugs.ableToUpdate()) {
										updCheck = " checked";
										updYN = true;
									}
									if (ugs.getLevel().equals("NATIONAL")) {
										natlSel = " selected";
									}
									else if (ugs.getLevel().equals("GROUP")) {
										grpSel = " selected";
									}
									else if (ugs.getLevel().equals("ZONE")) {
										zoneSel = " selected";
									}
									else if (ugs.getLevel().equals("DISTRICT")) {
										distSel = " selected";
									}
									else if (ugs.getLevel().equals("TEAM")) {
										teamSel = " selected";
									}
									if (ugs.manualRecord() &&  maintUser.ableToSetSecurity()) {
											%>
									<tr class=innerColor>
										<td class=textnormal><input type=checkbox name=gDelete value='<%= ugs.getSPGeog() %>' ></td>
										<td class=textnormal>
											<%
											if (ugs.getSPGeog().length() == 4) {
											%>
											<%= ugs.getSalesmanName() %> (<%= ugs.getSPGeog() %>)
											<%
											}
											else {
											%>
											<%= ugs.getGeogName() %> (<%= ugs.getSPGeog() %>)
											<%
											}
											%>
											<input type=hidden name=geogOverride value='<%= ugs.getSPGeog() %>'></td>
										<td class=textnormal>
											<select name=gLevel class=textnormal>
												<option value='TEAM'<%= teamSel %>>TEAM</option>
												<option value='DISTRICT'<%= distSel %>>DISTRICT</option>
												<option value='ZONE'<%= zoneSel %>>ZONE</option>
												<option value='GROUP'<%= grpSel %>>GROUP</option>
												<option value='NATIONAL'<%= natlSel %>>NATIONAL</option>
											</select></td>
										<td class=textnormal><input type=checkbox name=gSalesman value='<%= ugs.getSPGeog() %>' <%= slsmnCheck %>></td>
										<td class=textnormal><input type=checkbox name=gUpdate value='<%= ugs.getSPGeog() %>' <%= updCheck %>></td>
									</tr>
									<%
									}
									else {
											%>
									<tr class=innerColor>
										<td class=textnormal>&nbsp;</td>
										<%
										if (ugs.getSPGeog().length() == 4) {
										%>
										<td class=textnormal><%= ugs.getSalesmanName() %> (<%= ugs.getSPGeog() %>)</td>
										<%
										}
										else {
											%>
										<td class=textnormal><%= ugs.getGeogName() %> (<%= ugs.getSPGeog() %>)</td>
										<%
										}
										%>
										<td class=textnormal><%= ugs.getLevel() %></td>
										<%
										if (slsmnYN) {
											%>
										<td class=textnormal><img src=<%= sImagesURL %>check_mark.jpg></td>
										<%
										}
										else {
											%>
										<td class=textnormal>&nbsp;</td>
										<%
										}
										if (updYN) {
											%>
										<td class=textnormal><img src=<%= sImagesURL %>check_mark.jpg></td>
										<%
										}
										else {
										%>
										<td class=textnormal>&nbsp;</td>
										<%
										}
										%>
									</tr>
									<%
									}
									}
									
									if(maintUser.ableToSetSecurity()) {
									%>
									<tr class=innerColor>
										<td class=textnormal>&nbsp;</td>
										<td class=textnormal>New: <input class=textnormal name=newGeogOverride size=6 maxlength=6></td>
										<td class=textnormal>
											<select name=newLevel class=textnormal>
												<option value='TEAM'>TEAM</option>
												<option value='DISTRICT'>DISTRICT</option>
												<option value='ZONE'>ZONE</option>
												<option value='GROUP'>GROUP</option>
												<option value='NATIONAL'>NATIONAL</option>
											</select></td>
										<td class=textnormal><input type=checkbox name=newSalesman value=new></td>
										<td class=textnormal><input type=checkbox name=newUpdate value=new></td>
									</tr>
									<% } %>
								</table>
								<p>&nbsp;</p>

							</td>
						</tr>
						<tr>
						<td colspan="2" class="cellColor">
						<br>
						&nbsp;<span class=searchCenter2>Segment overrides</span><br><br>
						&nbsp;<span class="crumb">User currently has overrides to the following segments (*):</span><br>
						<br>
						<table>
						<tr><td width="16" align="center">
						<% if(maintUser.ableToSetSecurity()) { %>
						    <img src="<%= sImagesURL %>deleteIcon.gif" border="0" align="absmiddle">
						<% } %>
						   </td><td><b>Segment</b></td></tr>
						<%	
						ArrayList segmentOverrides = (ArrayList)request.getAttribute("segmentOverrides");
						if(segmentOverrides.size()==0){
							%>
							<tr><td></td><td><i>None</i></td></tr>
							<%
						}
						%>
						
						<%
						for(int i=0;i<segmentOverrides.size();i++){
							Segment segment=(Segment)segmentOverrides.get(i);
							%>
							<tr>
								<td>
								<% if(maintUser.ableToSetSecurity()) { %>
								    <input type="checkbox" name="rem_segment_override" value="<%= segment.getSegmentId() %>">
								<% } %>
								</td>
								<td><%= segment.getName() %></td></tr>
							<%
						}
						%>
						</table>
						<br>
						&nbsp;* Note - Any sub-segments below a selected level segment will be overridden as well.<br>
						<br>
						<% if(maintUser.ableToSetSecurity()) { %>
						    <br>&nbsp;<span class="crumb">Add new segments</span><br>
							<% Collection segments = (Collection)request.getAttribute("segments"); %>
							<%@ include file="./segmentSelectTree.jsp" %>
							<br><br>
						<% } %>
							</td>
							</tr>						
						<%
						}
						%>
					</table>
				<br>
				<input type=hidden name=maintUser value=<%= user.getUserid() %>>
				<input type=hidden name=page value=userprofile>
				<input type=hidden name=save value=true>
				<% if(maintUser.ableToSetSecurity()  || userSameAsLoggedIn) { %>
				<input type="image" src="<%= sImagesURL %>button_save.gif" width="70" height="20">
				<% } %>
				</form>
				<p>&nbsp;</p>
			</td>
		</tr>
	</table>
	</body>
</html>
