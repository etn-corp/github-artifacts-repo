<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page contentType="application/msexcel"%>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.dao.*" %>
<%@ page import="com.eaton.electrical.smrc.*" %>
<%@ page import="com.eaton.electrical.smrc.util.*" %>
<html>
 
<%
     response.setContentType("text/txt");
     response.setHeader("Content-Disposition", "attachment; filename=\"mailinglist.xls\"");
     ArrayList mailingListBeans = (ArrayList) request.getAttribute("mailingListBeans");
     
%>

<!-- Main Text	-->
Contacts for Customers in Search Results
<br><center>


<table width="90%" align=center border=1 cellpadding=1 cellspacing=1>
    <thead>
        <td>Customer</td>
        <td>Customer Number</td>
        <td>Job Title</td>
        <td>First Name</td>
        <td>Last Name</td>
        <td>Contact's Phone</td>
        <td>Email Address</td>
        <td>Fax Number</td>
        <td>Address</td>
        <td>City</td>
        <td>State</td>
        <td>Zip</td>
        <td>Customer Phone</td>

 </thead>
<%

                for (int i=0; i < mailingListBeans.size(); i++) {

                       StandardReportMailingListBean bean = (StandardReportMailingListBean) mailingListBeans.get(i);
       

 %>                      
                        <tr>
                            <td><%= StringManipulation.noNull(bean.getCustomerName()) %></td>
                            <td><%= StringManipulation.noNull(bean.getVistaCustomerNumber())%></td>
                            <td><%= StringManipulation.noNull(bean.getTitle())%></td> 
                            <td><%= StringManipulation.noNull(bean.getFirstName())%></td>
                            <td><%= StringManipulation.noNull(bean.getLastName())%></td>
                            <td><%= StringManipulation.noNull(bean.getPhoneNumber())%></td>
                            <td><%= StringManipulation.noNull(bean.getEmailAddress())%></td>
                            <td><%= StringManipulation.noNull(bean.getFaxNumber())%></td>
                            <td><%= StringManipulation.noNull(bean.getAddressLine1()) %></td>
                            <td><%= StringManipulation.noNull(bean.getCity()) %></td>
                            <td><%= StringManipulation.noNull(bean.getState()) %></td>
                            <td><%= StringManipulation.noNull(bean.getZip()) %></td>
                            <td><%= StringManipulation.noNull(bean.getCompanyPhoneNumber()) %></td>

                        </tr>
	
<%
           
                }
%>
</table>
<br>
</center>

</body>
</html>
