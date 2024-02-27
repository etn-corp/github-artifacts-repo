<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.dao.*"%>
<%@ page import="com.eaton.electrical.smrc.*" %>
<%@ page import="java.text.NumberFormat" %>
<html>
	<%@ include file="./SMRCHeader.jsp" %>
	<%@ include file="analytics.jsp" %>
	<%
	     User user = (User) request.getAttribute("user");
	     boolean hasSegmentOverrides = false;
	     ArrayList segmentOverrideList = user.getSegmentOverrides();
	     if (segmentOverrideList.size() > 0){
	        hasSegmentOverrides = true;
	     }
	     ArrayList sortedHeaders = (ArrayList) request.getAttribute("sortedHeaders");
	     ArrayList results = (ArrayList) request.getAttribute("results");
	     String filterString = (String) request.getAttribute("filterString");
	     String securityString = (String) request.getAttribute("securityString");
	     StandardReportSQLbuilder sqlBuilder = (StandardReportSQLbuilder) request.getAttribute("sqlBuilder");
	     int beginWith = ((Integer) request.getAttribute("beginWith")).intValue();
	     int endWith = ((Integer) request.getAttribute("endWith")).intValue();
	     boolean lastPage = ((Boolean) request.getAttribute("lastPage")).booleanValue();
	     ArrayList recordLinkList = (ArrayList) request.getAttribute("recordLinkList");
	     String recordLink = (String) recordLinkList.get(0);
	     String recordLink2 = "";
	     String recordLink3 = "";
	     if (recordLinkList.size() > 1){
	         recordLink2 = (String) recordLinkList.get(1);
	         recordLink3 = (String) recordLinkList.get(2);
	     }
	    boolean multiLevel = ((Boolean) request.getAttribute("multiLevel")).booleanValue();
	     int idIndex = 0;
	     int descIndex = 0;
	     int id2Index = 0;
	     int desc2Index = 0;
	     String queryString = request.getQueryString();
	//   We don't want any sort or prev/next information here
	     int pageLoc = -1;
	     if (queryString.indexOf("page") > 0){
	         pageLoc = queryString.indexOf("page");
	     }
	     StringBuffer newQueryString = new StringBuffer(queryString);
	     if (pageLoc >= 0){
	        newQueryString.delete(0, pageLoc);
	     }
	//   We want the sorting information here, but not the prev/next information
	     StringBuffer newPrevNextQueryString = new StringBuffer(queryString);
	     int prevLoc = -1;
	     int nextLoc = -1;
	     if (queryString.indexOf("prevPage") >= 0){
	        prevLoc = queryString.indexOf("prev");
	     }
	     if (queryString.indexOf("nextPage") >= 0){
	        nextLoc = queryString.indexOf("next");
	     }
	     if (prevLoc >= 0){
	        newPrevNextQueryString.delete(0, prevLoc + 11);
	     }
	     if (nextLoc >= 0){
	        newPrevNextQueryString.delete(0, nextLoc + 11);
	     }
	%>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750">
				<p><br>
					<a class="crumb" href="SMRCHome">Home Page</a><span class="crumbarrow">&gt;</span>
					<a class="crumb" href="StandardReport?page=sr">Standard Report</a>
					<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Standard Report Results</span>
				</p>
			</td>
		</tr>
	</table>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10">&nbsp;</td>
			<td width="750" valign="top">
				<p>&nbsp;</p>
				<!-- Main Text	-->
				<p class="heading2">Standard Report Results</p>
				<%= filterString %><br>
				<%= securityString %>
				<br><center>
					<p align="center">
						<%
						if (beginWith > 0){
						%> <a href="StandardReport?beginWith=<%= beginWith %>&endWith=<%=endWith%>&prevPage=Y&<%= newPrevNextQueryString.toString() %>">&lt;&lt;Prev 50</a> |
						<%  } else { %>
						<font class="textgray">&lt;&lt;Prev 50 | </font>
						<% } %>
						<%  if (!lastPage){
						%> <a href="StandardReport?beginWith=<%= beginWith %>&endWith=<%=endWith%>&nextPage=Y&<%= newPrevNextQueryString.toString() %>">Next 50&gt;&gt;</a>
						<%  } else { %>
						<font class="textgray">Next 50&gt;&gt;</font>
						<% } %>
					</p>
					<table class=tableBorder width="100%" align=left border=0 cellpadding=1 cellspacing=1>
						Amounts represent <%= sqlBuilder.getMonth() %>/<%= sqlBuilder.getYear() %> 
						<a href="StandardReport?<%= queryString %>&emailExcel=Y" target="blank.html">
							<img align="absmiddle" src="<%= sImagesURL %>excel.gif" border=0 alt='Excel Spreadsheet Version'></a>
						<a href="javascript: openPopup('StandardReportChart','charts',300,600)">
							<img align="absmiddle" src="<%= sImagesURL %>graph.gif" border=0 alt='Show Graphically'></a>
						<a href="javascript: openPopup('AcctPlanAssocUsersPopup?<%= newQueryString.toString() %>','assocpopup',400,600)">
							<img align="absmiddle" src="<%= sImagesURL %>outlook.gif" alt='Contact Associated Users' border=0></a>
						<a href="StandardReport?<%= newQueryString.toString() %>&emailMailingList=Y" target="blank.html">
							<img align="absmiddle" src="<%= sImagesURL %>envelope.gif" alt='Mailing List Spreadsheet' border=0></a>
						<thead bgcolor="#F8F8FF">
							<%
							     if (sqlBuilder.isGroupByAccount() || sqlBuilder.isGroupByParent()){
							%>
							  		<td class=searchResHdrL nowrap><a href="StandardReport?sortby=id&sortdir=asc&<%=newQueryString%>">Vista Customer Number</a>
							  		</td>
							<%   }
							%>
							<td class=searchResHdrL nowrap><a href="StandardReport?sortby=description&sortdir=asc&<%=newQueryString%>"><%= sqlBuilder.returnDescriptionColumnHeading() %></a></td>
							<%
									for (int i=0; i < sortedHeaders.size(); i++){
							                    StandardReportHeader rptHdr = (StandardReportHeader) sortedHeaders.get(i);
							                    if (!rptHdr.isDescription() && !rptHdr.isId() && !rptHdr.isDescription2() && !rptHdr.isId2() && (rptHdr.getSequence() < 99)){
							                        if (multiLevel){
							%> <td><%= rptHdr.getReportHeader() %></td>
							<%                      } else {
							%> <td><a href="StandardReport?sortby=<%= rptHdr.getSQLColumnName() %>&sortdir=desc&<%=newQueryString%>"><%= rptHdr.getReportHeader() %></a></td>
							<%                      }
							                  } else{
							                        if (rptHdr.isDescription()){
							                            descIndex = rptHdr.getColumnIndex();
							                        } else if (rptHdr.isId()){
							                            idIndex = rptHdr.getColumnIndex();
							                        } else if (rptHdr.isDescription2()){
							                            desc2Index = rptHdr.getColumnIndex();
							                        } else if (rptHdr.isId2()){
							                            id2Index = rptHdr.getColumnIndex();
							                        }
							                    }
							                }
							%>
						</thead>
						<%
								if (request.getParameter("emailMailingList") != null) {
						%>
						<div class=smallFontL>Your mailing list will be emailed to you shortly. If you don't receive it within 1 hour, please contact IT Support at <a href="mailto:oemaccountplanner@eaton.com">oemaccountplanner@eaton.com</a></div>
						<%		}
								else if (request.getParameter("emailExcel") != null && request.getParameter("emailExcel").equals("Y")) {
						%>
						<div class=smallFontL>Your report will be emailed to you shortly. If you don't receive it within 1 hour, please contact IT Support at <a href="mailto:oemaccountplanner@eaton.com">oemaccountplanner@eaton.com</a></div>
						<%		}
				 // Start from 1; 0 is headers record
								for (int i=1; i < results.size(); i++) {
						                        StandardReportSearchResults thisRes = (StandardReportSearchResults) results.get(i);
						                        ArrayList resultRec = thisRes.getResultObjects();
						                        boolean showLink = true;
						//             This code determines if this record should be a link
						                      if ((multiLevel) && (resultRec.get(id2Index) != null)) {
						                           if (!hasSegmentOverrides){
							                            if (sqlBuilder.isGroupByZone() && sqlBuilder.isGroupByDistrict()
							                                && (!user.ableToSeeThisLevel((String) resultRec.get(id2Index), "TEAM"))){
							                                showLink = false;
							                            } else if (sqlBuilder.isGroupByDistrict() && (sqlBuilder.isGroupByProduct() || sqlBuilder.isGroupBySummaryProduct())
							                                && (!user.ableToSeeThisLevel((String) resultRec.get(idIndex), "ACCOUNT"))){
							                                showLink = false;
							                            } else if (sqlBuilder.isGroupByDistrict() && sqlBuilder.isGroupByTeam()
							                                && (!user.ableToSeeThisLevel((String) resultRec.get(id2Index), "ACCOUNT"))){
							                                showLink = false;
							                            } else if (sqlBuilder.isGroupByDistrict() && (sqlBuilder.isGroupByPrimarySegment()  || sqlBuilder.isGroupBySecondarySegment())
							                                && (!user.ableToSeeThisLevel((String) resultRec.get(id2Index), "TEAM"))){
							                                showLink = false;
							                            } 
						                           }
						                        } else if (sqlBuilder.isGroupBySalesOrg() && (!user.ableToSeeThisLevel((String) resultRec.get(idIndex), "GROUP")) && (!hasSegmentOverrides)){
						                            showLink = false;
						                        } else if (sqlBuilder.isGroupByGroupCode() && (!user.ableToSeeThisLevel((String) resultRec.get(idIndex), "ZONE")) && (!hasSegmentOverrides)){
						                            showLink = false;
						                        } else if (sqlBuilder.isGroupByZone() && (!user.ableToSeeThisLevel((String) resultRec.get(idIndex), "DISTRICT")) && (!hasSegmentOverrides)){
						                            showLink = false;
						// Making sure it is for grouping by District only
						                        } else if (sqlBuilder.isGroupByDistrict() && (!sqlBuilder.isGroupByPrimarySegment()) && (!sqlBuilder.isGroupBySecondarySegment()) 
						                              && (!user.ableToSeeThisLevel((String) resultRec.get(idIndex), "TEAM")) && (!hasSegmentOverrides)){
						                            showLink = false;
						// Have to make sure it's not the District/Team grouping combination here
						                        } else if ((sqlBuilder.isGroupByTeam()) && (!sqlBuilder.isGroupByDistrict()) && (!user.ableToSeeThisLevel((String) resultRec.get(idIndex), "ACCOUNT")) && (!hasSegmentOverrides)){
						                            showLink = false;
					  	                        }  else if ((sqlBuilder.isGroupByProduct() || sqlBuilder.isGroupBySummaryProduct() ||
							                                sqlBuilder.isGroupByPrimarySegment() || sqlBuilder.isGroupBySecondarySegment()) 
							                                && (!hasSegmentOverrides) && (!user.ableToViewEverything()) && (!user.hasOverrideSecurity())){
							                               showLink = false;
							                       
							                    }
							                   
						//
						%> <tr class=cellColor>
							<%
						        if (sqlBuilder.isGroupByAccount() || sqlBuilder.isGroupByParent()){
						     %>
						  		<td><div class="searchLeft">
						     <%         if (multiLevel){
						      			if (resultRec.get(id2Index) != null){
						      			    if (showLink){
						     %>
						     				<a href="<%= recordLink2 %><%= resultRec.get(id2Index) %>&<%= recordLink3 %><%= resultRec.get(idIndex)%>">
						     <%             } %>
						                    <%= resultRec.get(id2Index) %>
						     <%           } else {
						                    if (showLink){
						     %>                 <a href="<%= recordLink %><%= resultRec.get(idIndex) %>">
						     <%             }   %>
						                    <b>>>><%= resultRec.get(idIndex) %></b>
						     <%           }
						             } else {
						                 if (showLink){
						     %>              <a href="<%= recordLink %><%= resultRec.get(idIndex) %>">
						     <%          }  %>
						                <%= resultRec.get(idIndex)  %>
	                         <%         }
						             if (showLink){
						     %>          </a>
						     <%      }  %>
						   		</div></td>
						     <%   }
						     %>
							<%						
							                       if (multiLevel){
							                            if (resultRec.get(id2Index) != null){
							                                if (showLink){
							%> <td><div class="searchLeft"><a href="<%= recordLink2 %><%= resultRec.get(id2Index) %>&<%= recordLink3 %><%= resultRec.get(idIndex)%>">
										<%= resultRec.get(desc2Index) %></a></div></td>
							<%                              } else {
							%> <td><div class="searchLeft"><%= resultRec.get(desc2Index) %></div></td>
							<%                              }
							                            } else {
							                                if (showLink){
							%> <td><div class="searchLeft"><a href="<%= recordLink %><%= resultRec.get(idIndex) %>">
										<b>>>><%= resultRec.get(descIndex) %></b></a></div></td>
							<%                              } else {
							%> <td><div class="searchLeft"><b>>>><%= resultRec.get(descIndex) %></b></div></td>
							<%                              }
							                            }
							                       } else {
							                            if (showLink){
							%>
							<td><div class="searchLeft"><a href="<%= recordLink %><%= resultRec.get(idIndex) %>">
										<%= resultRec.get(descIndex) %></a></div></td>
							<%                          } else {
							%>
							<td><div class="searchLeft"><%= resultRec.get(descIndex) %></div></td>
							<%                          }
							                      }
							%>
							<% 
							                                       NumberFormat nf = NumberFormat.getCurrencyInstance();
							                                       nf.setMaximumFractionDigits(0);
							                                       for (int x=0; x < sortedHeaders.size(); x++){
							                                            StandardReportHeader rptHdr2 = (StandardReportHeader) sortedHeaders.get(x);
							                                            if (!rptHdr2.isDescription() && !rptHdr2.isId() && !rptHdr2.isDescription2() && !rptHdr2.isId2() && (rptHdr2.getSequence() < 99)){
							%>
							<td><div class="searchRight"><%= nf.format(((Double) resultRec.get(rptHdr2.getColumnIndex())).doubleValue()) %></div></td>
							<%                                          }
							                                       }
							%>
						</tr>
						<%	//	 		}
						                 }
						%>
					</table>
					<p align="center">
						<%
						if (beginWith > 0){
						%> <a href="StandardReport?beginWith=<%= beginWith %>&endWith=<%=endWith%>&prevPage=Y&<%= newPrevNextQueryString.toString() %>">&lt;&lt;Prev 50</a> |
						<%  } else { %>
						<font class="textgray">&lt;&lt;Prev 50 | </font>
						<% } %>
						<%  if (!lastPage){
						%> <a href="StandardReport?beginWith=<%= beginWith %>&endWith=<%=endWith%>&nextPage=Y&<%= newPrevNextQueryString.toString() %>">Next 50&gt;&gt;</a>
						<%  } else { %>
						<font class="textgray">Next 50&gt;&gt;</font>
						<% } %>
					</p>
				</center>
			</td>
		</tr>
	</table>
	<br><br><br>
		</body>
</html>