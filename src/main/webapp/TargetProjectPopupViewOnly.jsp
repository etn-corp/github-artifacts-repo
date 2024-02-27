<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ include file="analytics.jsp" %>
<html>

<jsp:useBean id="project" class="com.eaton.electrical.smrc.bo.TargetProject" scope="request"/>
<%
    TreeMap productDesc = new TreeMap();
    productDesc = (TreeMap) request.getAttribute("product_hashtable");

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
<%-- <jsp:getProperty name="beanInstanceName"  property="propertyName" /> --%>

<table width="85%" border=0 align=center>
    <tr>
        <td valign=top><table width="100%" cellspacing=1 cellpadding=1 border=0 class=tableBorder>
        <tr>
            <td class=innerColor colspan=2 nowrap><div class=tocactive>Last Revision Date</div></td>
            <td class=cellColor width="50%"><div class=smallFontC><%= project.getDateChangedAsString() %></div></td>
        </tr>
	<tr>
            <td class=innerColor><div class=tocactive>District</div></td>
            <td class=cellColor colspan=2><div class=smallFontL><%= request.getAttribute("geogName") %></div></td>
	</tr>
    <tr>
        <td class=innerColor><div class=tocactive>Job Name</div></td>
         <td class=cellColor colspan=2><div class=smallFontL><%= project.getJobName() %></div></td>
    </tr>
    <tr>
        <td class=innerColor><div class=tocactive>Consultant</div></td>
        <td class=cellColor colspan=2><div class=smallFontL><%= project.getConsultant() %></div></td>
    </tr>
    <tr>
        <td class=innerColor><div class=tocactive>Bill of Material</div></td>
        <td class=cellColor colspan=2><div class=smallFontL>

<%
	                Set set = productDesc.entrySet();
                        Iterator descIt = set.iterator();
                        while (descIt.hasNext()){
                            Map.Entry me = (Map.Entry) descIt.next();
%>                          <%= me.getValue() %> (<%= me.getKey() %>)<br>
<%                        }

%>

        </div></td>
    </tr>
    <tr>
        <td class=innerColor><div class=tocactive>Bidding G.C.'s</div></td>
        <td class=cellColor colspan=2><div class=smallFontL><%= project.getGenContractors() %></div></td>
    </tr>
    <tr>
        <td class=innerColor><div class=tocactive>EG Value</div></td>
        <td class=cellColor colspan=2><div class=smallFontL><%= project.getCHValue() %></div></td>
    </tr>
    <tr>
        <td class=innerColor><div class=tocactive>Total Value</div></td>
        <td class=cellColor colspan=2><div class=smallFontL><%= project.getTotalValue() %></div></td>
    </tr>
    <tr>
        <td class=innerColor><div class=tocactive>Bid Date</div></td>
        <td class=cellColor colspan=2><div class=smallFontL><%= project.getBidDateAsString() %></div></td>
    </tr>
    <tr>
        <td class=innerColor><div class=tocactive>Change Order Potential</div></td>
        <td class=cellColor colspan=2><div class=smallFontL><%= project.getChangeOrderPotential().getDescription() %></div></td>
    </tr>
    <tr>
        <td class=innerColor><div class=tocactive>EG Position w/ Contractor</div></td>
        <td class=cellColor colspan=2><div class=smallFontL><%= project.getCHPosition() %></div></td>
    </tr>
    <tr>
        <td class=innerColor><div class=tocactive>Specified Vendors</div></td>
        <td class=cellColor colspan=2><div class=smallFontL>

<%

			
                        TreeMap vendorDesc = new TreeMap();
                        vendorDesc = (TreeMap) request.getAttribute("vendorDescriptions");
                        Set vendorset = vendorDesc.entrySet();
                        Iterator vendorIt = vendorset.iterator();
                        while (vendorIt.hasNext()){
                            Map.Entry vendor_me = (Map.Entry) vendorIt.next();
%>                          <%= vendor_me.getValue() %><br>
<%                        }

%>

        </div></td>
    </tr>
    <tr>
        <td class=innerColor><div class=tocactive>Neg Number</div></td>
        <td class=cellColor colspan=2><div class=smallFontL><%= project.getNegNum() %></div></td>
    </tr>
</table></td>
<td valign=top>
<table width="100%" cellspacing=1 cellpadding=1 border=0 class=tableBorder>
    <thead class=innerColor>
        <td colspan="<%= statusDesc.size() %>" class="smallFontC">Project Status</td>
        <td colspan="<%= reasonDesc.size() %>" class="smallFontC">Strategic Reasons</td>
        <td colspan="<%= prefDesc.size() %>" class="smallFontC">Spec Preferences for EG</td>
    </thead>
    <tr class=cellColor>

<%

                        Set statset = statusDesc.entrySet();
                        Iterator statDescIt = statset.iterator();
                        while (statDescIt.hasNext()){
                            Map.Entry stat_me = (Map.Entry) statDescIt.next();
%>                          <td class="smallFontC"><%= stat_me.getValue() %></td>
<%                        }

                        
			Set reasonset = reasonDesc.entrySet();
                        Iterator reasonDescIt = reasonset.iterator();
                        while (reasonDescIt.hasNext()){
                            Map.Entry reason_me = (Map.Entry) reasonDescIt.next();
%>                          <td class="smallFontC"><%= reason_me.getValue() %></td>
<%                        }
                        
                        Set prefset = prefDesc.entrySet();
                        Iterator prefdescIt = prefset.iterator();
                        while (prefdescIt.hasNext()){
                            Map.Entry pref_me = (Map.Entry) prefdescIt.next();
%>                          <td class="smallFontC"><%= pref_me.getValue() %></td>
<%                        }
			

%>
    </tr>
    <tr class=cellColor>

<%

                        statset = statusDesc.entrySet();
                        statDescIt = statset.iterator();
                        while (statDescIt.hasNext()){
                            Map.Entry stat_me = (Map.Entry) statDescIt.next();
                            Integer statInt = (Integer) stat_me.getKey();
                            if (statInt.intValue() == (project.getStatus().getId())){
%>                               <td class="smallFontC"><img src="<%= sImagesURL %>check_mark.jpg"></td>
<%                            } else {
%>                               <td class="smallFontC">&nbsp;</td>
<%                            }
                        }

                       
			reasonset = reasonDesc.entrySet();
                        reasonDescIt = reasonset.iterator();
                        while (reasonDescIt.hasNext()){
                            Map.Entry reason_me = (Map.Entry) reasonDescIt.next();
                            Integer reasonInt = (Integer) reason_me.getKey();
                            if (reasonInt.intValue() == (project.getReason().getId())){
%>                               <td class="smallFontC"><img src="<%= sImagesURL %>check_mark.jpg"></td>
<%                            } else {
%>                               <td class="smallFontC">&nbsp;</td>
<%                            }
                        }

			
                        prefset = prefDesc.entrySet();
                        prefdescIt = prefset.iterator();
                        while (prefdescIt.hasNext()){
                            Map.Entry pref_me = (Map.Entry) prefdescIt.next();
                            Integer prefInt = (Integer) pref_me.getKey();
                            if (prefInt.intValue() == (project.getPreference().getId())){
%>                               <td class="smallFontC"><img src="<%= sImagesURL %>check_mark.jpg"></td>
<%                            } else {
%>                               <td class="smallFontC">&nbsp;</td>
<%                            }
                        }

			

%>

    </tr>
    <tr class=cellColor>
        <td class="smallFontL" colspan="<%= statusDesc.size() %>"><%=  project.getStatusNotes() %></td>
        <td class="smallFontL" colspan="<%= reasonDesc.size() %>"><%= project.getStratReasonNotes() %></td>
        <td class="smallFontL" colspan="<%= prefDesc.size() %>"><%= project.getPreferenceNotes() %></td>
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
                            Map.Entry dist_me = (Map.Entry) distIt.next();
%>                          <%= dist_me.getValue() %> (<%= dist_me.getKey() %>)
<%                        }

%>        		</td>
			<td class="smallFontC" valign="top">
<%
                        Set contset = contMap.entrySet();
                        Iterator contIt = contset.iterator();
                        while (contIt.hasNext()){
                            Map.Entry cont_me = (Map.Entry) contIt.next();
%>                          <%= cont_me.getValue() %> (<%= cont_me.getKey() %>)
<%                        }
			
%>			</td>
<%
                        Set custset = custMap.entrySet();
                        Iterator custIt = custset.iterator();
                        while (custIt.hasNext()){
                            Map.Entry cust_me = (Map.Entry) custIt.next();
%>                            <td class="smallFontC" valign="top"><%=	cust_me.getValue() %> (<%= cust_me.getKey() %>)</td>
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
				<td class=smallFontC>
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
<%						}
					}
				}
%>
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
                                    Map.Entry bid_me = (Map.Entry) bidIt.next();
                                    Bid bid = (Bid) bid_me.getKey();
%>                                  <table class="tableBorder" cellspacing=1 cellpadding=1 border=0 align=center>
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
                                    	<tr class="cellColor">
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

<div class=smallFontC>

<%
	User addedBy = (User) request.getAttribute("addedBy");
	if(addedBy!=null){
%>                  
<span class="textnormal">Entered by <%= addedBy.getFirstName() %> <%= addedBy.getLastName() %> on <%= project.getDateAddedAsString() %></span>
<% } %>
</div>

</body>
</html>
