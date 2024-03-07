<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*, java.text.* " %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>

<html>
<%@ include file="./SMRCHeader.jsp" %>
<%@ include file="analytics.jsp" %>
<%
Account acct = header.getAccount();

User usr = header.getUser();
DuplicateCustomerBean[] duplicates = (DuplicateCustomerBean[]) request.getAttribute("duplicates");
String returnToPage = (String) request.getAttribute("returnToPage");

%>
<script>
	radioObjectArray = new Array();
	
	function submitForm(){
	    allSelected = true;
	    yesCount = 0;
	    for (i=0; i < radioObjectArray.length; i++){
	       radioButtonObj = radioObjectArray[i];
	       if (!radioButtonObj[0].checked && !radioButtonObj[1].checked){
				allSelected = false;
		   } else {
		   	  if (radioButtonObj[0].checked && radioButtonObj[0].value == 'Y'){
		   	  	 yesCount++;
		   	  } else {
		   	  	if (radioButtonObj[1].checked && radioButtonObj[1].value == 'Y'){
		   	  	 yesCount++;
		   	  	}
		   	  }
		   }
		}
		if (!allSelected){
			alert("You must select Yes or No for each potential duplicate customer listed.");
		} else {
			if (yesCount > 1){
				alert("Please only select 'Yes' for one potential duplicate customer.");
			} else {
				document.duplicateForm.submit();
			}
		}
	}
	
</script>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>

		<span class="crumbarrow">&gt;</span>
		<span class="crumbcurrent">Duplicate Customers</span></p> 
    </td>
  </tr>
</table>
<form name="duplicateForm" action="AccountProfile">
<input type="hidden" name="page" value="workflowApprove" />
<input type="hidden" name="acctId" value="<%= acct.getVcn() %>" />
<input type="hidden" name="fromDuplicate" value="Y" />
<input type="hidden" name="approvalId" value="<%= request.getParameter("approvalId") %>" /> 
<input type="hidden" name="returnToPage" value="<%= returnToPage %>" />
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p>&nbsp;</p>
      <table width="750" border="0" cellspacing="0" cellpadding="0">

        <tr valign="top"> 
          <td width="140"> 
						<%@ include file="./accountLeftNav.jsp" %>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
          </td>
          <td width="10" align="left" background="<%= sImagesURL %>divider.gif">&nbsp;</td>

         <td width="600"> 
                
            <p class="heading2">
            Potential Duplicate Customers</p>
           <p class="heading3">Account: <%= acct.getCustomerName() %></p>            
            <br>
          	 <table border="0" width="100%" cellspacing="5" >
          	 	<tr><td colspan="3" align="right"><span class="heading3">Is this a duplicate of the account you are setting up?</span></td></tr>
          	 	<tr><td colspan="3"><hr width="100%"></td></tr>
          	 <% for (int i=0; i < duplicates.length; i++) { 
          	 		DuplicateCustomerBean bean = duplicates[i];
          	  %>
	          	 <tr>
	          	 	<td class="columnHeader-acctProfile" colspan="2"><%= bean.getVistaCustomerName() %> (<%= bean.getVistaCustomerNumber() %>)</td>
	          	 	<td align="right"><input type="radio" name="customer_<%= bean.getVistaCustomerNumber() %>" value="Y" > Yes &nbsp; <input type="radio" name="customer_<%= bean.getVistaCustomerNumber() %>" value="N" > No</td>
	          	 </tr>
	          	 <script>
	          	 	radioObjectArray[radioObjectArray.length] = document.duplicateForm.customer_<%= bean.getVistaCustomerNumber() %>;
	          	 </script>
	          	 <tr>
	          	 	<td><%= bean.getBusinessAddress1() %></td>
	          	 	<td><b>Category:</b> <%= bean.getCategoryDescription() %> (<%= bean.getCategoryCode() %>)</td>
	          	 	<td><b>SE:</b> <%= bean.getSalesmanFirstName() %> <%= bean.getSalesmanLastName() %> (<%= bean.getSalesmanId() %>)</td>
	          	 </tr>
	          	 <tr>
	          	 	<td><%= bean.getBusinessAddress2() %></td>
	          	 	<td><b>Status:</b> <%= bean.getStatusDescription() %> (<%= bean.getStatus() %>)</td>
	          	 	<td><%= bean.getSpGeog() %> - <%= bean.getSalesLocation() %></td>
	          	 </tr>
	          	 <tr>
	          	 	<td><%= bean.getBusinessAddress3() %></td>
	          	 	<td><b>Phone:</b> <%= bean.getPhoneNumber() %></td>
	          	 	<td>&nbsp;</td>
	          	 </tr>
	          	 <tr>
	          	 	<td><%= bean.getBusinessCity() %>, <%= bean.getBusinessState() %> <%= bean.getBusinessZip() %></td>
	          	 	<td><b>Fax:</b> <%= bean.getFaxNumber() %></td>
	          	 	<td>&nbsp;</td>
	          	 </tr>
	          	
	          	 <tr><td colspan="3"><hr width="100%"></td></tr>
	          	<%	}  %>
		 	 	<tr><td colspan="3" align="right"><a href="javascript:submitForm()"><img src="<%= sImagesURL %>button_save.gif" width="70" height="20"  border="0" ></a></td></tr>	
       	  	 </table>
       	  	 
         </td>
        </tr>
      </table>
    
    </td>

  </tr>
</table>
</form>
<p>&nbsp;</p>
  </body>
</html>
