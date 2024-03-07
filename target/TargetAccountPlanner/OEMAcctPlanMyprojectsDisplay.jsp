<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ include file="analytics.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

OEMAcctPlanBean OEMAPVars = (OEMAcctPlanBean)request.getAttribute("variables");
request.setAttribute("header", OEMAPVars.getHeader());
boolean found = false;
%>

<HTML>

<%@ include file="./TAPheader.jsp" %>
<%

ArrayList projects = OEMAPVars.getProjects();
User user = OEMAPVars.getUser();


%>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>
		<span class="crumbarrow">&gt;</span>        
		<span class="crumbcurrent">My Target Projects</span>
      </p> 
    </td>
  </tr>
</table>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="10">&nbsp;</td>
    <td width="750" valign="top"> 
    <p>&nbsp;</p>
		<div class=heading2>My Target Projects</div>
		<br><br>
		<table class=tableBorder cellspacing=1 cellpadding=1 border=0 width="100%">
			<caption class=heading3>Active Projects</caption>
			<thead class=innerColor>
				<td class=searchCenter2>Delete</td>
				<td class=searchCenter2>Name</td>
				<td class=searchCenter2>District</td>
				<td class=searchCenter2>Status</td>
				<td class=searchCenter2>Reason</td>
				<td class=searchCenter2>Spec Preference</td>
				<td class=searchCenter2>Bid Date</td>
				<td class=searchCenter2>EG Value</td>
				<td class=searchCenter2>Total Value</td>
				<td class=searchCenter2>Neg Number</td>
			</thead>

<%
		for (int i=0; i < projects.size(); i++) {
			TargetProject tp = (TargetProject)projects.get(i);

			if (tp.isActive()) {
				found=true;
				boolean canApprove = tp.getCanApprove(); //allowedToApproveTargetProject(user, tp);
				boolean canDelete = canApprove||user.getUserid().equals(tp.getUserAdded());
				%>
					<tr class=cellColor>
						<td class=smallFontL>
				<%
				if (canDelete) {
					%>
					<a href=OEMAcctPlan?page=myProjects&delete=<%= tp.getId() %>><img src=<%= sImagesURL %>remove.gif alt='Delete Project' border=0></a>
						<%
				}
				%>
						</td>
						<td class=smallFontL><a href=TargetProjectPopup?projId=<%= tp.getId() %>><%= tp.getJobName() %></a></td>
						<td class=smallFontL><%= tp.getGeogName() %></td>
						<td class=smallFontL><%= tp.getStatus().getDescription() %></td>
						<td class=smallFontL><%= tp.getReason().getDescription() %></td>
						<td class=smallFontL><%= tp.getPreference().getDescription() %></td>
						<td class=smallFontL><%= tp.getBidDateAsString() %></td>
						<td class=smallFontR><%= tp.getCHValueForDisplay() %></td>
						<td class=smallFontR><%= tp.getTotalValueForDisplay() %></td>
						<td class=smallFontL><%= tp.getNegNum() %></td>
					</tr>
				<%

			}
		}
		if(found==false){
		%>
					<tr class=cellColor>
						<td colspan="10" class="smallFontL">&nbsp;No Projects</td>
					</tr> 
		<%
		}
		found=false;
%>
		</table>
		<p>&nbsp;</p>
		<table class=tableBorder cellspacing=1 cellpadding=1 border=0 width="100%">
			<caption class=heading3>Awaiting CHAMPS Managers Approval</caption>
			<thead class=innerColor>
				<td class=searchCenter2>Approve / Delete</td>
				<td class=searchCenter2>Name</td>
				<td class=searchCenter2>District</td>
				<td class=searchCenter2>Status</td>
				<td class=searchCenter2>Reason</td>
				<td class=searchCenter2>Spec Preference</td>
				<td class=searchCenter2>Bid Date</td>
				<td class=searchCenter2>EG Value</td>
				<td class=searchCenter2>Total Value</td>
				<td class=searchCenter2>Neg Number</td>
			</thead>
<%
for (int i=0; i < projects.size(); i++) {
			TargetProject tp = (TargetProject)projects.get(i);

			if (tp.waitingForChampsMgr()) {
				found=true;
				boolean canApprove = tp.getCanApprove();//allowedToApproveTargetProject(user, tp);
				boolean canDelete = canApprove||user.getUserid().equals(tp.getUserAdded());
					%>
					<tr class=cellColor>
						<td class=smallFontL>
						<%
				if (canApprove) {
					%>
					<a href=OEMAcctPlan?page=myProjects&approve=<%= tp.getId() %>><img src=<%= sImagesURL %>check_mark.jpg alt='Approve Project' border=0></a>
					<%
				}
				if (canDelete) {
					%>
					<a href=OEMAcctPlan?page=myProjects&delete=<%= tp.getId() %>><img src=<%= sImagesURL %>remove.gif alt='Delete Project' border=0></a>
						<%
				}
				%>
						</td>

						<td class=smallFontL><a href=TargetProjectPopup?projId=<%= tp.getId() %>><%= tp.getJobName() %></a></td>
						<td class=smallFontL><%= tp.getGeogName() %></td>
						<td class=smallFontL><%= tp.getStatus().getDescription() %></td>
						<td class=smallFontL><%= tp.getReason().getDescription() %></td>
						<td class=smallFontL><%= tp.getPreference().getDescription() %></td>
						<td class=smallFontL><%= tp.getBidDateAsString() %></td>
						<td class=smallFontR><%= tp.getCHValueForDisplay() %></td>
						<td class=smallFontR><%= tp.getTotalValueForDisplay() %></td>
						<td class=smallFontL><%= tp.getNegNum() %></td>
					</tr>
					<%
			}
		}
		if(found==false){
		%>
					<tr class=cellColor>
						<td colspan="10" class="smallFontL">&nbsp;No Projects</td>
					</tr> 
		<%
		}
		found=false;
%>
		</table>
		<p>&nbsp;</p>
		<table class=tableBorder cellspacing=1 cellpadding=1 border=0 width="100%">
			<caption class=heading3>Awaiting Project Sales Managers Approval</caption>
			<thead class=innerColor>
				<td class=searchCenter2>Approve / Delete</td>
				<td class=searchCenter2>Name</td>
				<td class=searchCenter2>District</td>
				<td class=searchCenter2>Status</td>
				<td class=searchCenter2>Reason</td>
				<td class=searchCenter2>Spec Preference</td>
				<td class=searchCenter2>Bid Date</td>
				<td class=searchCenter2>EG Value</td>
				<td class=searchCenter2>Total Value</td>
				<td class=searchCenter2>Neg Number</td>
			</thead>


<%
		for (int i=0; i < projects.size(); i++) {
			TargetProject tp = (TargetProject)projects.get(i);

			if (tp.waitingForProjectSalesMgr()) {
				found=true;
				boolean canApprove = tp.getCanApprove();//allowedToApproveTargetProject(user, tp);
				boolean canDelete = canApprove||user.getUserid().equals(tp.getUserAdded());
					%>
					<tr class=cellColor>
						<td class=smallFontL>
						<%
				if (canApprove) {
					%>
					<a href=OEMAcctPlan?page=myProjects&approve=<%= tp.getId() %>><img src=<%= sImagesURL %>check_mark.jpg alt='Approve Project' border=0></a>
						<%
				}
				if (canDelete) {
						%>
						<a href=OEMAcctPlan?page=myProjects&delete=<%= tp.getId() %>><img src=<%= sImagesURL %>remove.gif alt='Delete Project' border=0></a>
						<%
				}
				%>
						</td>
						<td class=smallFontL><a href=TargetProjectPopup?projId=<%= tp.getId() %>><%= tp.getJobName() %></a></td>
						<td class=smallFontL><%= tp.getGeogName() %></td>
						<td class=smallFontL><%= tp.getStatus().getDescription() %></td>
						<td class=smallFontL><%= tp.getReason().getDescription() %></td>
						<td class=smallFontL><%= tp.getPreference().getDescription() %></td>
						<td class=smallFontL><%= tp.getBidDateAsString() %></td>
						<td class=smallFontR><%= tp.getCHValueForDisplay() %></td>
						<td class=smallFontR><%= tp.getTotalValueForDisplay() %></td>
						<td class=smallFontL><%= tp.getNegNum() %></td>
					</tr>
					<%
			}
		}
		if(found==false){
		%>
					<tr class=cellColor>
						<td colspan="10" class="smallFontL">&nbsp;No Projects</td>
					</tr> 
		<%
		}
		found=false;
%>
		</table>
		<p>&nbsp;</p>
		<table class=tableBorder cellspacing=1 cellpadding=1 border=0 width="100%">
			<caption class=heading3>Awaiting District Managers Approval</caption>
			<thead class=innerColor>
				<td class=searchCenter2>Approve / Delete</td>
				<td class=searchCenter2>Name</td>
				<td class=searchCenter2>District</td>
				<td class=searchCenter2>Status</td>
				<td class=searchCenter2>Reason</td>
				<td class=searchCenter2>Spec Preference</td>
				<td class=searchCenter2>Bid Date</td>
				<td class=searchCenter2>EG Value</td>
				<td class=searchCenter2>Total Value</td>
				<td class=searchCenter2>Neg Number</td>
			</thead>

<%
		for (int i=0; i < projects.size(); i++) {
			TargetProject tp = (TargetProject)projects.get(i);

			if (tp.waitingForDM()) {
				found=true;
				boolean canApprove = tp.getCanApprove();
				boolean canDelete = canApprove||user.getUserid().equals(tp.getUserAdded());
					%>
					<tr class=cellColor>
						<td class=smallFontL>
						<%
				if (canApprove) {
				%>
					<a href=OEMAcctPlan?page=myProjects&approve=<%= tp.getId() %>><img src=<%= sImagesURL %>check_mark.jpg alt='Approve Project' border=0></a>
					<%
				}
				if (canDelete) {
					%>
					<a href=OEMAcctPlan?page=myProjects&delete=<%= tp.getId() %>><img src=<%= sImagesURL %>remove.gif alt='Delete Project' border=0></a>
					<%
				}
						%>
						</td>
						<td class=smallFontL><a href=TargetProjectPopup?projId=<%= tp.getId() %>><%= tp.getJobName() %></a></td>
						<td class=smallFontL><%= tp.getGeogName() %></td>
						<td class=smallFontL><%= tp.getStatus().getDescription() %></td>
						<td class=smallFontL><%= tp.getReason().getDescription() %></td>
						<td class=smallFontL><%= tp.getPreference().getDescription() %></td>
						<td class=smallFontL><%= tp.getBidDateAsString() %></td>
						<td class=smallFontR><%= tp.getCHValueForDisplay() %></td>
						<td class=smallFontR><%= tp.getTotalValueForDisplay() %></td>
						<td class=smallFontL><%= tp.getNegNum() %></td>
					</tr>
					<%
			}
		}
		if(found==false){
		%>
					<tr class=cellColor>
						<td colspan="10" class="smallFontL">&nbsp;No Projects</td>
					</tr> 
		<%
		}
		found=false;
%>

		</table>
		<p>&nbsp;</p>
		<table class=tableBorder cellspacing=1 cellpadding=1 border=0 width="100%">
			<caption class=heading3>Deleted Projects</caption>
			<thead class=innerColor>
				<td class=searchCenter2>Name</td>
				<td class=searchCenter2>District</td>
				<td class=searchCenter2>Status</td>
				<td class=searchCenter2>Reason</td>
				<td class=searchCenter2>Spec Preference</td>
				<td class=searchCenter2>Bid Date</td>
				<td class=searchCenter2>EG Value</td>
				<td class=searchCenter2>Total Value</td>
				<td class=searchCenter2>Neg Number</td>
			</thead>
			
			
<%
		for (int i=0; i < projects.size(); i++) {
			TargetProject tp = (TargetProject)projects.get(i);

			if (tp.deleted()) {
					found=true;
					%>
					<tr class=cellColor>
						<td class=smallFontL><a href=TargetProjectPopup?projId=<%= tp.getId() %>><%= tp.getJobName() %></a></td>
						<td class=smallFontL><%= tp.getGeogName() %></td>
						<td class=smallFontL><%= tp.getStatus().getDescription() %></td>
						<td class=smallFontL><%= tp.getReason().getDescription() %></td>
						<td class=smallFontL><%= tp.getPreference().getDescription() %></td>
						<td class=smallFontL><%= tp.getBidDateAsString() %></td>
						<td class=smallFontR><%= tp.getCHValueForDisplay() %></td>
						<td class=smallFontR><%= tp.getTotalValueForDisplay() %></td>
						<td class=smallFontL><%= tp.getNegNum() %></td>
					</tr>
					<%
			}
		}
		if(found==false){
		%>
					<tr class=cellColor>
						<td colspan="9" class="smallFontL">&nbsp;No Projects</td>
					</tr> 
		<%
		}
%>
		</table>
		<br><div class=smallFontC>Click <a href=TargetProjectPopup?page=newtargetproject>here</a> to add a new target project</div>
		<p>&nbsp;</p>		<p>&nbsp;</p>
</td>
</tr>
</table>
			
</BODY>
</HTML>			