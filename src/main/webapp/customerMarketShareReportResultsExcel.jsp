<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page contentType="application/msexcel"%>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.dao.*" %>
<%@ page import="com.eaton.electrical.smrc.*" %>
<%@ page import="java.text.NumberFormat" %>
<html>
	<%
	     response.setContentType("text/txt");
	     response.setHeader("Content-Disposition", "attachment; filename=\"tapreport.xls\"");
	     ArrayList sortedHeaders = (ArrayList) request.getAttribute("sortedHeaders");
	     ArrayList results = (ArrayList) request.getAttribute("results");
	     String filterString = (String) request.getAttribute("filterString");
	     String securityString = (String) request.getAttribute("securityString");
	     StandardReportSQLbuilder sqlBuilder = (StandardReportSQLbuilder) request.getAttribute("sqlBuilder");
	     boolean lastPage = ((Boolean) request.getAttribute("lastPage")).booleanValue();
	     boolean multiLevel = ((Boolean) request.getAttribute("multiLevel")).booleanValue();
	     int idIndex = 0;
	     int descIndex = 0;
	     int id2Index = 0;
	     int desc2Index = 0;
	
	%>
	<!-- Main Text	-->
	Standard Report Results<br>
	<%= filterString %>
	<br>
	<%= securityString %>
	<br>
		<center>
			<table width="90%" align=center border=1 cellpadding=1 cellspacing=1>
				<caption >Amounts represent <%= sqlBuilder.getMonth() %>/<%= sqlBuilder.getYear() %> (All dollars are in thousands)
				</caption>
				<thead>
				  <%
				     if (sqlBuilder.isGroupByAccount() || sqlBuilder.isGroupByParent()){
				  %>
				  		<td>Vista Customer Number
				  		</td>
				  <%   }
				  %>
					<td><%= sqlBuilder.returnDescriptionColumnHeading() %></td>
					<%
							for (int i=0; i < sortedHeaders.size(); i++){
					                    StandardReportHeader rptHdr = (StandardReportHeader) sortedHeaders.get(i);
					                    if (!rptHdr.isDescription() && !rptHdr.isId() && !rptHdr.isDescription2() && !rptHdr.isId2() && (rptHdr.getSequence() < 99)){
					                        if (multiLevel){
					%> <td><%= rptHdr.getReportHeader() %></td>
					<%                      } else {
					%> <td><%= rptHdr.getReportHeader() %></td>
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
				                // Start from 1; 0 is headers record
						for (int i=1; i < results.size(); i++) {
				                        StandardReportSearchResults thisRes = (StandardReportSearchResults) results.get(i);
				                        ArrayList resultRec = thisRes.getResultObjects();
				%> <tr>
					  <%
					     if (sqlBuilder.isGroupByAccount() || sqlBuilder.isGroupByParent()){
					  %>
					  		<td>&nbsp;
					  <%         if (multiLevel){
					      			if (resultRec.get(id2Index) != null){
					   %>
					                    <%= resultRec.get(id2Index) %>
					   <%           } else {
					   %>
					                    <b>>>><%= resultRec.get(idIndex) %></b>
					   <%           }
					             } else {
					   %>
					                <%= resultRec.get(idIndex)  %>
                      <%         } 
					  %>
					   		</td>
					  <%   }
					  %>
					<%
					                        if (multiLevel){
					                            if (resultRec.get(id2Index) != null){
					%> <td>
						<%= resultRec.get(desc2Index) %></td>
					<%                          } else {
					%> <td>
						<b>>>><%= resultRec.get(descIndex) %></b></td>
					<%                          }
					                       } else {
					%>
					<td>
						<%= resultRec.get(descIndex) %></td>
					<%                    }
					%>
					<%
					                                       NumberFormat nf = NumberFormat.getCurrencyInstance();
					                                       nf.setMaximumFractionDigits(0);
					                                       for (int x=0; x < sortedHeaders.size(); x++){
					                                            StandardReportHeader rptHdr2 = (StandardReportHeader) sortedHeaders.get(x);
					                                            if (!rptHdr2.isDescription() && !rptHdr2.isId() && !rptHdr2.isDescription2() && !rptHdr2.isId2() && (rptHdr2.getSequence() < 99)){
					%>
					<td><%= nf.format(((Double) resultRec.get(rptHdr2.getColumnIndex())).doubleValue()) %></td>
					<%                                          }
					                                       }
					%>
				</tr>
				<%	//	 		}
				                 }
				
				%>
			</table>
			<br>
		</center>
		</body>
</html>