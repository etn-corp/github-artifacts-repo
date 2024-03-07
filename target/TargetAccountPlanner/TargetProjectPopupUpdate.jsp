<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ include file="analytics.jsp" %>
<html>

<jsp:useBean id="project" class="com.eaton.electrical.smrc.bo.TargetProject" scope="request"/>
<%
    TreeMap statusDesc = new TreeMap();
    statusDesc = (TreeMap) request.getAttribute("statusDescriptions");
    
    TreeMap reasonDesc = new TreeMap();
    reasonDesc = (TreeMap) request.getAttribute("reasonDescriptions");

    TreeMap prefDesc = new TreeMap();
    prefDesc = (TreeMap) request.getAttribute("preferenceDescriptions");

    TreeMap distMap = new TreeMap();
    distMap = (TreeMap) request.getAttribute("distributors");

    TreeMap contMap = new TreeMap();
    contMap = (TreeMap) request.getAttribute("contractors");

    TreeMap custMap = new TreeMap();
    custMap = (TreeMap) request.getAttribute("customers");

    ArrayList users = (ArrayList) request.getAttribute("users");
    ArrayList memberTypes = (ArrayList) request.getAttribute("memberTypes");
    ArrayList members = (ArrayList) request.getAttribute("members");
    ArrayList allProducts = (ArrayList) request.getAttribute("allProducts");
    ArrayList bom = (ArrayList) request.getAttribute("bom");
    ArrayList cops = (ArrayList) request.getAttribute("cops");
    ArrayList vendors = (ArrayList) request.getAttribute("vendors");
    ArrayList tpVens = (ArrayList) request.getAttribute("tpVens");

%>

<%@ include file="./TAPheader.jsp" %>

<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>
		<span class="crumbarrow">&gt;</span>        
		<span class="crumbcurrent">Target Project</span>
      </p> 
    </td>
  </tr>
</table>
<br>


<script language='javascript'>
	function approveDelete(ad) {
		document.UPDATEPROJ.apprDel.value = ad;
		document.UPDATEPROJ.submit();
	}
</script>
<%
if (!request.getAttribute("message").equals("")) {
%>
<blockquote><font class="crumbcurrent"><%= request.getAttribute("message") %></font></blockquote>
<% } %>
<form name="UPDATEPROJ" action="TargetProjectPopup" method="POST">

<%

                        Boolean canApprove = (Boolean) request.getAttribute("canApprove");
                        Boolean canDelete = (Boolean) request.getAttribute("canDelete");
                        if ((canApprove.booleanValue() || canDelete.booleanValue()) && (project.getId()!=0)) {
%>				<div class=centerOnly>
<%				if (canApprove.booleanValue()) {
%>					<a href="javascript:onClick=approveDelete('A')"><img src="<%= sImagesURL %>check_mark.jpg" border=0 alt='Approve Project' align="absmiddle"></a> <span class="textnormal">Click to approve</span>
<%				}

				if (canDelete.booleanValue()) {
%>					<a href="javascript:onClick=approveDelete('D')"><img src="<%= sImagesURL %>remove.gif" border=0 alt='Remove Target Account' align="absmiddle"></a> <span class="textnormal">Click to delete<span>
<%				}
%>				</div>
<%			}
%>

<input type=hidden name=apprDel value="N">
<input type=hidden name=page value="<%= header.getPage() %>" >
<input type=hidden name=groupId value="<%= header.getSalesGroup().getId() %>" >
<input type=hidden name=projId value="<%= project.getId() %>" >
<input type=hidden name=option value="save">

<%

			if (request.getAttribute("cust") != null) {
                                Customer cust = (Customer) request.getAttribute("cust");
%>				<input type="hidden" name="cust" value="<%= cust.getVistaCustNum() %>">
<%			}
%>

<table width="85%" border=0 align=center>
    <tr>
        <td valign=top><table width="100%" cellspacing=1 cellpadding=1 border=0 class=tableBorder>
        <tr>
            <td class=innerColor colspan=2 nowrap><div class=tocactive>Last Revision Date</div></td>
            <td class=cellColor width="50%"><div class=smallFontC><%= project.getDateChangedAsString() %></div></td>
        </tr>

<%
        if (!project.dmApproved()) {
                ArrayList districts = (ArrayList) request.getAttribute("districts");
%>              <tr>
                    <td class="innerColor"><div class="tocactive">District</div></td>
                    <td class="cellColor" colspan=2><select class="smallFontL" name="spGeog">
                out.println("					<option value=''>Select from these ...</option>");
<%
                for (int i=0; i < districts.size(); i++) {
                       Region s = (Region)districts.get(i);
                       String checked = "";

                        if (s.getSPGeog().equals(project.getSPGeog())) {
                                checked = "selected";
                        }
%>
                        <option value='<%= s.getSPGeog() %>' <%= checked %>> <%= s.getDescription() %> (<%= s.getSPGeog() %>)</option>
<%                }
%>                </select>
<%        } else {

                String geogName = (String) request.getAttribute("geogName");
%>              <tr>
                    <td class="innerColor"><div class="tocactive">District</div></td>
                    <td class="cellColor" colspan="2"><div class="smallFontL"><%= geogName  %></div>
                            <input type="hidden" name="spGeog" value='<%=  project.getSPGeog() %>'></td>
                </tr>
<%        }

%>

	</td>
    </tr>
    <tr>
        <td class=innerColor><div class=tocactive>Job Name</div></td>
        <td class=cellColor colspan=2><input maxlength="150" class=smallFontL name=jobName value='<%= project.getJobName() %>'></td>
    </tr>
    <tr>
        <td class=innerColor><div class=tocactive>Consultant</div></td>
        <td class=cellColor colspan=2><input maxlength="150" class=smallFontL name=consultant value='<%= project.getConsultant() %>'></td>
    </tr>
    <tr>
        <td class=innerColor><div class=tocactive>Bill of Material</div></td>
        <td class=cellColor colspan=2><select class=smallFontL name=bom multiple>

<%

                for (int i=0; i < allProducts.size(); i++) {
                        Product p = (Product) allProducts.get(i);
                        String selected = "";

                        for (int j=0; j < bom.size(); j++) {
                                if (((String)bom.get(j)).equals(p.getId())) {
                                        selected = " selected";
                                }
                        }
%>
                        <option value='<%= p.getId() %>' <%= selected %>> <%= p.getDescription() %> (<%= p.getId() %>)</option>
<%                }

%>
        	</select>
	</div></td>
    </tr>
    <tr>
        <td class=innerColor><div class=tocactive>Bidding G.C.'s</div></td>
	<td class=cellColor colspan=2><input class=smallFontL name=gc value='<%= project.getGenContractors() %>'></td>
    </tr>
    <tr>
        <td class=innerColor><div class=tocactive>EG Value</div></td>
        <td class=cellColor colspan=2><input class=smallFontL name=chValue value='<%= project.getCHValue() %>' size=8> (Must be a number)</td>
    </tr>
    <tr>
        <td class=innerColor><div class=tocactive>Total Value</div></td>
        <td class=cellColor colspan=2><input class=smallFontL name=totalValue value='<%= project.getTotalValue() %>' size=8> (Must be a number)</td>
    </tr>
    <tr>
        <td class=innerColor><div class=tocactive>Bid Date</div></td>
        <td class=cellColor colspan=2><input class=smallFontL name=bidDate value='<%= project.getBidDateAsString() %>'></td>
    </tr>
    <tr>
	<td class=innerColor><div class=tocactive>Change Order Potential</div></td>
	<td class=cellColor colspan=2><select class=smallFontL name=copId>
		<option value=''>Select one ...</option>
<%
       for (int i=0; i < cops.size(); i++) {
                ChangeOrderPotential cop = (ChangeOrderPotential)cops.get(i);
                String checked = "";

                if (project.getChangeOrderPotential().getId() == cop.getId()) {
                        checked = " selected";
                }
%>
                <option value='<%= cop.getId() %>' <%= checked %>><%= cop.getDescription() %></option>
<%        }

%>

		</select>
        </td>
    <tr>
        <td class=innerColor><div class=tocactive>EG Position w/ Contractor</div></td>
        <td class=cellColor colspan=2><input class=smallFontL name=chPos value='<%= project.getCHPosition() %>'></td>
    </tr>
    <tr>
        <td class=innerColor><div class=tocactive>Specified Vendors</div></td>
        <td class=cellColor colspan=2><select class=smallFontL name=vendor multiple>

<%

                        for (int i=0; i < vendors.size(); i++) {
				Vendor v = (Vendor) vendors.get(i);
				String selected = "";

				for (int j=0; j < tpVens.size(); j++) {
					if (Integer.parseInt((String)tpVens.get(j)) == v.getId()) {
						selected = " selected";
					}
				}
%>
				<option value='<%= v.getId() %>' <%= selected %>> <%= v.getDescription() %></option>
<%			}
%>

        </div></td>
    </tr>
    <tr>
        <td class=innerColor><div class=tocactive>Neg Number</div></td>
        <td class=cellColor colspan=2><input class=smallFontL name=negNum value='<%= project.getNegNum() %>'></td>
    </tr>
</table></td>
<td valign=top>
<table width="100%" cellspacing=1 cellpadding=1 border=0 class=tableBorder>
    <thead class=innerColor>
        <td colspan=" <%= statusDesc.size() %>" class=smallFontC>Project Status</td>
        <td colspan=" <%= reasonDesc.size() %>" class=smallFontC>Strategic Reasons</td>
        <td colspan=" <%= prefDesc.size() %>" class=smallFontC>Spec Preferences for EG</td>
    </thead>
    <tr class=cellColor>

<%

                        Set statset = statusDesc.entrySet();
                        Iterator statDescIt = statset.iterator();
                        while (statDescIt.hasNext()){
                            java.util.Map.Entry stat_me = (java.util.Map.Entry) statDescIt.next();
%>                          <td class="smallFontC"><%= stat_me.getValue() %></td>
<%                        }

                        
			Set reasonset = reasonDesc.entrySet();
                        Iterator reasonDescIt = reasonset.iterator();
                        while (reasonDescIt.hasNext()){
                            java.util.Map.Entry reason_me = (java.util.Map.Entry) reasonDescIt.next();
%>                          <td class="smallFontC"><%= reason_me.getValue() %></td>
<%                        }
                        
                        Set prefset = prefDesc.entrySet();
                        Iterator prefdescIt = prefset.iterator();
                        while (prefdescIt.hasNext()){
                            java.util.Map.Entry pref_me = (java.util.Map.Entry) prefdescIt.next();
%>                          <td class="smallFontC"><%= pref_me.getValue() %></td>
<%                        }
			
%>

    </tr>
    <tr class=cellColor>

<%

                        statset = statusDesc.entrySet();
                        statDescIt = statset.iterator();
                        String checked = "";
                        while (statDescIt.hasNext()){
                            checked = "";
                            java.util.Map.Entry stat_me = (java.util.Map.Entry) statDescIt.next();
                            Integer statInt = (Integer) stat_me.getKey();
                            if (statInt.intValue() == (project.getStatus().getId())){
                                checked = " checked";
                            } 
%>                          <td class="smallFontC"><input type="radio" name="status" value="<%= stat_me.getKey()%>" <%= checked %>></td>
<%                        }

                       
			reasonset = reasonDesc.entrySet();
                        reasonDescIt = reasonset.iterator();
                        while (reasonDescIt.hasNext()){
                            checked = "";
                            java.util.Map.Entry reason_me = (java.util.Map.Entry) reasonDescIt.next();
                            Integer reasonInt = (Integer) reason_me.getKey();
                            if (reasonInt.intValue() == (project.getReason().getId())){
                               checked = " checked";
                            } 
%>                          <td class="smallFontC"><input type="radio" name="reason" value="<%= reason_me.getKey()%>" <%= checked %>></td>
<%                        }

			
                        prefset = prefDesc.entrySet();
                        prefdescIt = prefset.iterator();
                        while (prefdescIt.hasNext()){
                            checked = "";
                            java.util.Map.Entry pref_me = (java.util.Map.Entry) prefdescIt.next();
                            Integer prefInt = (Integer) pref_me.getKey();
                            if (prefInt.intValue() == (project.getPreference().getId())){
                               checked = " checked";
                            } 
%>                          <td class="smallFontC"><input type="radio" name="preference" value="<%= pref_me.getKey()%>" <%= checked %>></td>
<%                        }

			

%>

    </tr>
    <tr class=cellColor>
        <td class="smallFontL" colspan="<%= statusDesc.size() %>"><textarea name="statusNotes" class="smallFontL"><%= project.getStatusNotes() %></textarea></td>
        <td class="smallFontL" colspan="<%= reasonDesc.size() %>"><textarea name="stratReasonNotes" class="smallFontL"><%= project.getStratReasonNotes() %></textarea></td>
        <td class="smallFontL" colspan="<%= prefDesc.size() %>"><textarea name="prefNotes" class="smallFontL"><%= project.getPreferenceNotes() %></textarea></td>
    </tr>
</table>

<br>
<table width="100%" cellspacing=1 cellpadding=1 border=0 class=tableBorder>
    <thead class=innerColor>
        <td class=smallFontC>Distributor(s)</td>
        <td class=smallFontC>Electric Contractor(s)</td>
        <td class=smallFontC>End Customer</td>
    </thead>
    <tr class=cellColor>
        <td class=smallFontC valign=top>

<%

                        Set distset = distMap.entrySet();
                        Iterator distIt = distset.iterator();
                        while (distIt.hasNext()){
                            java.util.Map.Entry dist_me = (java.util.Map.Entry) distIt.next();
%>                            <a href="TargetProjectPopup?projId=<%= project.getId() %>&groupId=<%= header.getSalesGroup().getId() %>&option=removeDist&dist=<%= dist_me.getKey() %>"><img src="<%= sImagesURL %>remove.gif" border="0"></a><%= dist_me.getValue() %> (<%= dist_me.getKey() %>)<br>
<%                        }
                        for (int i=0; i < 3; i++) {
				if (i>0) {
%>					<br>
<%				}
%>				Enter Vista #: <input class="smallFontL" size="6" maxlength="6" name="distVCN">
<%			}
%>

        		</td>
			<td class=smallFontC valign=top>
<%
                        Set contset = contMap.entrySet();
                        Iterator contIt = contset.iterator();
                        while (contIt.hasNext()){
                            java.util.Map.Entry cont_me = (java.util.Map.Entry) contIt.next();
%>                            <a href="TargetProjectPopup?projId=<%= project.getId() %>&groupId=><%= header.getSalesGroup().getId() %>&option=removeEC&ec=<%= cont_me.getKey() %>"><img src="<%= sImagesURL %>remove.gif" border="0"></a><%= cont_me.getValue() %>(<%= cont_me.getKey() %>)<br>
<%                        }
			for (int i=0; i < 3; i++) {
				if (i>0) {
%>					<br>
<%				}
%>				Enter Vista #: <input class="smallFontL" size="6" maxlength="6" name="ecVCN">
<%			}
%>			</td>
<% 
                        Set custset = custMap.entrySet();
                        Iterator custIt = custset.iterator();
                        if (custIt.hasNext()){
                            java.util.Map.Entry cust_me = (java.util.Map.Entry) custIt.next();
                            if (!((String)cust_me.getKey()).equals("")){
%>                                <td class="smallFontC" valign="top"><a href="TargetProjectPopup?projId=<%= project.getId() %>&groupId=<%= header.getSalesGroup().getId() %>&option=removeCust&cust=<%= cust_me.getKey() %>"><img src="<%= sImagesURL %>remove.gif" border="0"></a><%= cust_me.getValue()%> (<%= cust_me.getKey() %>)</td>
<%                            } else {
%>                                <td class="smallFontC" valign="top">Enter Vista #: <input class="smallFontL" size="6" maxlength="6" name="custVCN"></td>
<%                            }
                        } else {
%>                            <td class="smallFontC" valign="top">Enter Vista #: <input class="smallFontL" size="6" maxlength="6" name="custVCN"></td>
<%                        }
			
%>
				</tr>
			</table>

	<br>
	<table width="100%" cellspacing=1 cellpadding=1 border=0 class=tableBorder>
	<caption class=tocactive>Team Members for Project (To be filled in after approval)</caption>
		<thead class=innerColor>
<%
			for (int i=0; i < memberTypes.size(); i++) {
				ProjectMemberType pmt = (ProjectMemberType)memberTypes.get(i);
%>				<td class="smallFontC"><%= pmt.getDescription() %></td>
<%			}
%>
		</thead>
		<tr class=cellColor>
<% 
			for (int j=0; j < memberTypes.size(); j++) {
				ProjectMemberType pmt = (ProjectMemberType)memberTypes.get(j);
%> 
				<td class="smallFontC"><select name="user_<%=pmt.getId()%>" class="smallFontL" multiple>
<%                                

				for (int i=0; i < users.size(); i++) {
					User u = (User)users.get(i);
					String selected = "";

					for (int k=0; k < members.size(); k++) {
						ProjectMember pm = (ProjectMember)members.get(k);

						if (pm.getUserid().equals(u.getUserid()) &&
							pm.getMemberType().equals(pmt.getId()))
						{
%>							<%= u.getFirstName() %> <%= u.getLastName() %><br>
<%                                                        selected = " selected";
						}
					}
%>                                      <option value='<%= u.getUserid() %>' <%= selected %>> <%= u.getLastName() %>, <%= u.getFirstName() %> </option>
<%				}
                                
%>                                  </select>

                                </td>
<%			}
			
%>

    </tr>
</table>
</td>
</tr>
</table>

<%

			if (!project.getNegNum().equals("")) {
                                TreeMap bidMap = new TreeMap();
                                bidMap = (TreeMap) request.getAttribute("bid");
                                Set bidset = bidMap.entrySet();
                                Iterator bidIt = bidset.iterator();
                                while (bidIt.hasNext()){
                                    java.util.Map.Entry bid_me = (java.util.Map.Entry) bidIt.next();
                                    Bid bid = (Bid) bid_me.getKey();
%>                                  <table class="tableBorder" cellspacing="1" cellpadding="1" border="0" align="center">

                                    <caption class="heading3">Bid Tracker information</caption>
                                    	<thead class="innerColor">
                                        	<td class="smallFontL">Neg Number</td>
                                                <td class="smallFontL">Bid Date</td>
                                                <td class="smallFontL">Job Name</td>
                                                <td class="smallFontL">Status</td>
                                                <td class="smallFontL">Salesman</td>
                                                <td class="smallFontL">Bid Dollars</td>
                                                <td class="smallFontL">Job Type</td>
                                                <td class="smallFontL">Order Number</td>
                                        </thead>
                                        <tr class=cellColor>
                                                <td class="smallFontL"><%= bid.getNegNum() %></td>
                                    		<td class="smallFontL"><%= bid.getBidDateAsString() %></td>
                                                <td class="smallFontL"><%= bid.getJobName() %></td>
                                                <td class="smallFontL"><%= bid.getStatus() %></td>
                                                <td class="smallFontL"><%= bid_me.getValue() %></td>
                                    		<td class="smallFontR"><%= bid.getBidDollarsForDisplay() %></td>
                                    		<td class="smallFontL"><%= bid.getJobType().getDescription() %></td>
                                    		<td class="smallFontL"><%= bid.getGONum() %></td>
                                    	</tr>
                                    </table>
<%                                }
			}

%>

<div class=centerOnly><input type="image" src="<%= sImagesURL %>/button_save.gif" width="70" height="20"><!--<input type=submit name=sub value='Save Changes' class=smallFontC>--></div><br>

<%
// Cannot format new project for printing.
                        if (project.getId() != 0) {
				String custStr = "";

				if (request.getParameter("cust") != null) {
                                        Customer cust = header.getCustomer();
					custStr = "&cust=" + cust.getVistaCustNum();
				}
				
%>
				 <div class=centerOnly><a href="TargetProjectPopup?projId=<%= project.getId() %>&groupId=<%= header.getSalesGroup().getId() + custStr %>&printLayout=Y"><img src="<%= sImagesURL%>printer_friendly_format.gif" alt='Format Page for Printing' border="0"></a></div>
<%			}
%>			
            </form>

<div class=smallFontC>
<%
User addedBy = (User) request.getAttribute("addedBy");
if(addedBy!=null && !addedBy.getLastName().equals("") && !addedBy.getFirstName().equals("")){
%>
<span class="textnormal">Entered by <%= addedBy.getFirstName() %> <%= addedBy.getLastName() %> on <%= project.getDateAddedAsString() %></span>
<% } %>
</div>
</body>
</html>
