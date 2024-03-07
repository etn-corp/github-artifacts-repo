<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.StringManipulation"%>
<%@ page import="com.eaton.electrical.smrc.util.Money"%>

<html>
<%@ include file="./SMRCHeader.jsp" %>
<%@ include file="analytics.jsp" %>

<script language="javascript" src="<%= jsURL %>validation/distTerminationForm.js"></script>
<%

Account acct = header.getAccount();
User usr = header.getUser();
DistributorTermination distTerm = (DistributorTermination)request.getAttribute("distTerm");
boolean ableToUpdate=true;
%>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>

		<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Distributor Termination</span></p> 
    </td>
  </tr>
</table>
<form action="DistributorSignup" name="theform" method="POST" onSubmit="javascript:return formValidation();">
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
					<% if(request.getParameter("save")!=null){ %>
					<blockquote><font class="crumbcurrent">Save Successful</font></blockquote>
					<% } %>		         
            <p class="heading2">
            Distributor Termination</p>
           <p class="heading3">Account: <%= acct.getCustomerName() %></p>

            <table width="100%" border="0" cellspacing="5" cellpadding="0">
            	<tr>
            		<td width="25%"><div align="right">Proposed effective date of termination:</div></td>
            		<td width="3%">&nbsp;</td>
            		<td colspan="2" width="72%">
            		<%= StringManipulation.createTextBox("PROPOSED_TERMINATION_MON",distTerm.getProposedDateMonth(),ableToUpdate,"2") %>/
								<%= StringManipulation.createTextBox("PROPOSED_TERMINATION_DAY",distTerm.getProposedDateDate(),ableToUpdate,"2") %>/
								<%= StringManipulation.createTextBox("PROPOSED_TERMINATION_YEAR",distTerm.getProposedDateYear(),ableToUpdate,"4") %>
            		</td>

       		 	</tr>
				<tr>
					<td width="25%"><div align="right">Reason for Termination:</div></td>
					<td with="3%">&nbsp;</td>
					<td width="22%"><div align="left">
						<% if(distTerm.getReasonTypeId()==55){ %>
							<input type="radio" checked="true" name="TERMINATION_REASON_TYPE_ID" value="55">Eaton Electrical</div></td>
				    <% }else{ %>
				    	<input type="radio" name="TERMINATION_REASON_TYPE_ID" value="55">Eaton Electrical</div></td>
				    <% } %>
				    
				    <td width="50%" colspan="2"><div align="left">
						<% if(distTerm.getReasonTypeId()==56){ %>
							<input type="radio" checked="true" name="TERMINATION_REASON_TYPE_ID" value="56">Distributor decision</div></td>
				    <% }else{ %>
					    <input type="radio" name="TERMINATION_REASON_TYPE_ID" value="56">Distributor decision</div></td>
				    <% } %>
						

				</tr>
				<tr>
					<td valign="top"><div align="right">Explain:</div></td>
					<td>&nbsp;</td>
					<td colspan="3">
					<%
					if(ableToUpdate){
						%>
						<textarea name="EXPLANATION" cols="40" rows="4"><%= distTerm.getExplanation() %></textarea>
						<%
					}else{ 
						out.println(distTerm.getExplanation());
					 } %>					
					</td>
				</tr>
				<tr>
					<td><div align="right">Describe actions taken by district manager to inform 
						distributor of decision to terminate if it	is an Eaton Electrical decision.</div></td>

					<td>&nbsp;</td>
					<td colspan="3">
					<%
					if(ableToUpdate){
						%>
						<textarea name="ACTION_NOTES" cols="40" rows="4"><%= distTerm.getActionNotes() %></textarea>
						<%
					}else{ 
						out.println(distTerm.getActionNotes());
					 } %>
					</td>
				</tr>
				</table>
	           <table width="100%" border="0" cellspacing="5" cellpadding="0">
				<tr>
					<td width="25%"><div align="right">Estimated Inventory:</div></td>
					<td width="3%">&nbsp;</td>

					<td width="24%">$
              <%= StringManipulation.createTextBox("EST_INV_STDDE",Money.formatDoubleAsDollarsAndCents(distTerm.getEstInventoryStdDE()),ableToUpdate,"6") %>
              <span class="textgray">Std DE </span></td>
				    <td width="24%">$
				    	<%= StringManipulation.createTextBox("EST_INV_PDCD",Money.formatDoubleAsDollarsAndCents(distTerm.getEstInventoryPDCD()),ableToUpdate,"6") %>
				    	<span class="textgray">PDCD</span>
				    </td>
				    <td width="24%">$
				    	<%= StringManipulation.createTextBox("EST_INV_STDCTL",Money.formatDoubleAsDollarsAndCents(distTerm.getEstInventoryStdControl()),ableToUpdate,"6") %>
				    	<span class="textgray">Std Control
                    </span> </td>

				</tr>
				<tr>
					<td><div align="right">Potential Return:</div></td>
					<td>&nbsp;</td>
					<td>$ 
						<%= StringManipulation.createTextBox("POT_RET_STDDE",Money.formatDoubleAsDollarsAndCents(distTerm.getPotReturnStdDE()),ableToUpdate,"6") %>
						<span class="textgray">Std DE	</span></td>
				    <td>$
				    	<%= StringManipulation.createTextBox("POT_RET_PDCD",Money.formatDoubleAsDollarsAndCents(distTerm.getPotReturnPDCD()),ableToUpdate,"6") %>
				    	<span class="textgray">PDCD</span>

				    </td>
				    <td>$
				    	<%= StringManipulation.createTextBox("POT_RET_STDCTL",Money.formatDoubleAsDollarsAndCents(distTerm.getPotReturnStdControl()),ableToUpdate,"6") %>
                        <span class="textgray">Std Control	</span> </td>
				</tr>				
				<tr>
			</table>	
		<table width="100%" border="0" cellspacing="5" cellpadding="0">
			<tr>
				<td><div align="right">Termination request approved as of:</div></td>
				<td>&nbsp;</td>

				<td valign="top">
						<%= StringManipulation.createTextBox("TERMINATION_EFFECTIVE_MON",distTerm.getEffectiveDateMonth(),ableToUpdate,"2") %>/
						<%= StringManipulation.createTextBox("TERMINATION_EFFECTIVE_DAY",distTerm.getEffectiveDateDate(),ableToUpdate,"2") %>/
						<%= StringManipulation.createTextBox("TERMINATION_EFFECTIVE_YEAR",distTerm.getEffectiveDateYear(),ableToUpdate,"4") %>
					<span class="textgray">Date</span></td>
			</tr>
	     </table>
			<br><br>
			<table width="100%" border="0" cellpadding="0" cellspacing="10">
				<tr>
					<td width="5%"><div align="right">
					<input type="image" src="<%= sImagesURL %>button_save.gif" width="70" height="20"></div></td>
					<td width="95%"></td>

				</tr>
			</table>
	      <p>&nbsp;</p>
         </td>
        </tr>
      </table>
    
    </td>
  </tr>
</table>
<input type="hidden" name="acctId" value="<%= acct.getVcn() %>">
<input type="hidden" name="page" value="saveTermination">
</form>
<p>&nbsp;</p>

  </body>
</html>
