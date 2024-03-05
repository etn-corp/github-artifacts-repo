<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.*" %>
<%@ page import="java.text.*" %>
<html>

<%@ include file="./SMRCHeader.jsp" %>
<%@ include file="analytics.jsp" %> 

<%
	User user = header.getUser();
    Account account = header.getAccount();//(Account) request.getAttribute("account");
    String seName = (String) request.getAttribute("seName");
    int tmId = ((Integer) request.getAttribute("tmId")).intValue();
    ArrayList combinedProductList = (ArrayList) request.getAttribute("combinedProductList");
    ArrayList competitors = (ArrayList) request.getAttribute("competitors");
    TargetMarket targetMarket = (TargetMarket) request.getAttribute("targetMarket");
    ArrayList otherTargetMarketList = (ArrayList) request.getAttribute("otherTargetMarketList");
    ArrayList allTMSegments = (ArrayList) request.getAttribute("allTMSegments");
    ArrayList salesObjectives = (ArrayList) request.getAttribute("salesObjectives");
    ArrayList allContacts = (ArrayList) request.getAttribute("allContacts");
    ArrayList salesTrackingDollars = (ArrayList) request.getAttribute("salesTrackingDollars");
    String globalDisable = "";
// Used in cases where disabling input will not work
    boolean disableEverything = false;
    if (targetMarket.isActive() || targetMarket.isRejected() || targetMarket.isDMApproved()){
            globalDisable = "disabled";
            disableEverything = true;
    }
//  These session attributes are destroyed in the TargetMarketSetup servlet
    session.setAttribute("targetMarket", targetMarket);
    session.setAttribute("combinedProductList", combinedProductList);
    String statusText = "";
    
	Date today = new Date();
	int year = Calendar.getInstance().get(Calendar.YEAR);
	int lastYear = year - 1;
	int nextYear = year + 1;
	String forecastActualTag = "";
	if (targetMarket.isActive()){
		if (today.after(targetMarket.getEndDate())){
			forecastActualTag = "Actual";
		} else {
			forecastActualTag = "Forecasted";
		}
	}

    if (targetMarket.isPendingResubmission()){
    	statusText = "Pending Resubmission";
    } else if (targetMarket.isDMApproved()){
        statusText = "DM Approved";
    } else if (targetMarket.isRejected()){
        statusText = "Rejected";
    } else if (targetMarket.isSaved()){
        statusText = "Saved";
    } else if (targetMarket.isActive()){
    	if (today.after(targetMarket.getEndDate())){
    		statusText = "Complete";
    	} else {
	        statusText = "Active";
	    }
    } else if (targetMarket.isSubmitted()){
        statusText = "Submitted to DM";
    }

    boolean canApprove = ((Boolean) request.getAttribute("canApprove")).booleanValue();
    TreeMap endCustMap = (TreeMap) request.getAttribute("endCustMap");
    TreeMap planAcctMap = (TreeMap) request.getAttribute("planAcctMap");

	boolean canPurge = false;
	if ((canApprove || (targetMarket.getUserAdded().trim().equalsIgnoreCase(user.getUserid())) || user.hasOverrideSecurity() || user.isChannelMarketingManager()) &&
		(targetMarket.isSaved() || targetMarket.isRejected())){
		canPurge = true;
	}
	
	ArrayList priorApprovers = new ArrayList();
	priorApprovers = (ArrayList)request.getAttribute("priorApprovers");
    
%>

<script language='javascript' src='<%= jsURL %>layerHandler.js'></script>

<Script>
function updateIncrementalGrowth(prodSalesObj, index){
    newValue = 0;
    enteredValue = removeCurrency(prodSalesObj.value);
    if (isNaN(enteredValue)){
        alert(prodSalesObj.value + " is not a valid number.");
    } else {
       newValue = parseFloat(enteredValue) - parseFloat(document.tmSetup.prevSales[index].value);
       document.tmSetup.incrGrowth[index].value = newValue;
       prodSalesObj.value = parseFloat(enteredValue);
    }
    
}

function checkExpirationDate(){
	startIndex = document.tmSetup.startDateYear.selectedIndex;
	endIndex = document.tmSetup.endDateYear.selectedIndex;
	if (document.tmSetup.startDateYear[startIndex].value != document.tmSetup.endDateYear[endIndex].value){
		return false;
	} else {
		return true;
	}
}

function submitForm(action){
    if (isEmpty(document.tmSetup.planDescription.value)) {
			alert('"Desciption of this plan" cannot be an empty value.\n');
		} else {
			if (!checkExpirationDate()){
				alert("Target market plans must end in the same year they begin. If a plan must extend into another year, create a separate plan for that year.");
			} else {
		    	document.tmSetup.action.value = action;
		   		document.tmSetup.submit();
	    	}
    	}
}

function hideShow(layerName, showExplanationLayer) {
	if(showExplanationLayer == true){
		hideAllLayers();
		showLayer(layerName);
	}else {
		hideAllLayers();
		//hideLayer(layerName);
	}
}
	
function hideAllLayers(){

	for(i=0;i<document.tmSetup.priorApproversSize.value;i++){
		hideLayer("reviseResubmitExplanationDiv");
	}
}

function submitReviseResubmit(radioButtonsCount){

	var selectedPriorApproverWorkflowApprovalId = "";
	var selectedPriorApproverTargetMarketStatus = "";
	
	//I can't iterate through radio buttons (as done in the else below) when there is only 1 radio button and hence the special case to check for radioButtonsCount == 1
       if(radioButtonsCount == 1) {
           selectedPriorApproverWorkflowApprovalId = document.tmSetup.reviseResubmitApprover.value;
           //selectedPriorApproverTargetMarketStatus = "S";//since there is only one radio button, it means that there's only Initial Account Information.
           selectedPriorApproverTargetMarketStatus = getTargetMarketStatus();
       } else {
		for(i = 0; i < radioButtonsCount; i++){
			if(eval('document.tmSetup.reviseResubmitApprover[' + i + ']').checked){
			
				selectedPriorApproverWorkflowApprovalId = eval('document.tmSetup.reviseResubmitApprover[' + i + ']').value;

				/*if(i == 0){//Initial Account Information
					selectedPriorApproverTargetMarketStatus = "S";
				}
				else if (i == 1){//District Manager
					selectedPriorApproverTargetMarketStatus = "B";
				}*/
				
				selectedPriorApproverTargetMarketStatus = getTargetMarketStatus();
			}
		}
    }
    
	var explanation = document.tmSetup.reviseResubmitExplanation.value;
	if(selectedPriorApproverWorkflowApprovalId != "" && explanation.length > 0 && explanation.length <= 2000){
		
		//location = location + "&reviseResubmitExplantion=" + explanation + "&selectedPriorApproverWorkflowApprovalId=" + selectedPriorApproverWorkflowApprovalId;
		//document.location = location;
		
		document.tmSetup.reviseResubmitExplantion.value = explanation;
		document.tmSetup.selectedPriorApproverWorkflowApprovalId.value = selectedPriorApproverWorkflowApprovalId;
		document.tmSetup.targetMarketStatus.value = selectedPriorApproverTargetMarketStatus;
		
		submitForm('revise');
	}
	else if (selectedPriorApproverWorkflowApprovalId == ""){
		alert("Please pick a prior approver if you want to use the 'Revise Resubmit' option.");
	}
	else if (explanation.length <= 0){
		alert("Please enter something in the Explanation Text before clicking Submit.")
	}
	else if (explanation.length > 2000) {
		alert("The explanation entered exceeds the maximum limit of 2000 characters.");
	}
}

function updateReviseResubmitApproverWorkflowStepName(workflowStepName){
	document.tmSetup.reviseResubmitApproverWorkflowStepName.value = workflowStepName;
}

function getTargetMarketStatus(){
	
	var workflowStepName = document.tmSetup.reviseResubmitApproverWorkflowStepName.value;
	
	if(workflowStepName == "<%=WorkflowStep.STEP_NAME_INITIAL_ACCOUNT_INFO%>"){
		return "S";
	}
	else if(workflowStepName == "<%=WorkflowStep.STEP_NAME_DISTRICT_MANAGER%>"){
		return "B";
	}
	else{
		alert("workflowStepName error. workflowStepName = " + workflowStepName);
	}
}


</Script>
<form name="tmSetup" action="TargetMarketSetup?acctId=<%= account.getVcn() %>" method="POST">

<input type=hidden name="priorApproversSize" value="<%=(priorApprovers != null) ? priorApprovers.size() : 0%>">
<input type=hidden name="reviseResubmitApproverWorkflowStepName" value="">

<% if (tmId > 0){
%>
    <input type=hidden name=tmId value="<%= tmId %>">
<% }
%>

<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="seHome.html">Home Page</a>
		<span class="crumbarrow">&gt;</span><span class="crumbcurrent">Target Market Setup</span>
      </p> 
    </td>
  </tr>
</table>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
  <td width="750" valign="top"> <br>
		<% if(request.getParameter("action")!=null){
			if(request.getParameter("action").equalsIgnoreCase("save")){
			%>
			<blockquote><font class="crumbcurrent">Save Successful</font></blockquote>
			<% }else if(request.getParameter("action").equalsIgnoreCase("silentsave")){ %>
			<blockquote><font class="crumbcurrent"><img align="absmiddle" src="<%= sImagesURL %>alert.gif">&nbsp;The page refreshed with your selection, but you must click the "Save" button to save your changes</font></blockquote>
			<%  } else if (request.getParameter("action").equalsIgnoreCase("send")){ %>
			<blockquote><font class="crumbcurrent"><img align="absmiddle" src="<%= sImagesURL %>alert.gif">&nbsp;Target Market Saved and Submitted for DM Approval</font></blockquote>
			<%  } else if (request.getParameter("action").equalsIgnoreCase("approve")){ %>
			<blockquote><font class="crumbcurrent"><img align="absmiddle" src="<%= sImagesURL %>alert.gif">&nbsp;Approve Successful</font></blockquote>
			<%  } else if (request.getParameter("action").equalsIgnoreCase("reject")){ %>
			<blockquote><font class="crumbcurrent"><img align="absmiddle" src="<%= sImagesURL %>alert.gif">&nbsp;Target Market Rejected</font></blockquote>
<%			}
		}
		%>     	
     <p class="heading2">Target Market Setup </p>
      <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
                <td width="69%" class="heading3">Account: <%= account.getCustomerName() %></td>
      		<td width="31%">&nbsp;</td>
        </tr>
      	<tr>
      		<td width="69%" class="heading3"><%= seName %></td>
      		<td width="31%">&nbsp;</td>
	  </tr>
  </table>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
      	<tr>
      		<td width="70%" valign="top">
      			<table width="100%" border="0" cellspacing="0" cellpadding="0">
      				<tr><td colspan="7">&nbsp;</td></tr>				
					<tr>
      					<td width="27%"><div align="right"><font class="crumbcurrent">*</font> Desciption of this plan:</div></td>
      					<td width="3%"></td>
      					<td colspan="4" width="60%">

      							<input type="text" name="planDescription" value='<%= targetMarket.getPlanDescription() %>' <%= globalDisable %>>

                         </td>
                                        <td width="">Status: <%= statusText %>
                                        </td>
   					</tr>
      				<tr><td colspan="10">&nbsp;</td></tr>				
      				<tr>
      					<td><div align="right">Contact at distributor:</div></td>
      					<td>&nbsp;</td>
				        <td>	
				        	<table width="100%" border="0" cellspacing="0" cellpadding="0">
				        		<tr>
				        			<td width="35%">
				        				<select name="primaryContact" <%= globalDisable %>>
				        				<option value="0">Select One...</option>
                                                                            <%
                                                                                 String contactSelected = "";
                                                                                 for (int i=0; i< allContacts.size(); i++){
                                                                                        Contact contact = (Contact) allContacts.get(i);
                                                                                        if (contact.getId() == targetMarket.getContactId()){
                                                                                                contactSelected = "selected";
                                                                                        } else {
                                                                                                contactSelected = "";
                                                                                        }
                                                                            %>
                                                                                        <option value="<%= contact.getId()%>" <%= contactSelected %>><%= contact.getLastName()%>, <%= contact.getFirstName() %></option>
                                                                            <%
                                                                                 }
                                                                            %>


			        					</select>
				        			</td>
                                                                <td width="65%">&nbsp;</td>
   			        			</tr>
			        		</table>
						</td>

      				</tr>					
      				<tr><td colspan="7">&nbsp;</td></tr>
					<tr>
						<td valign="top"><div align="right">Is plan designed to convert from competitor:</div></td>
						<td>&nbsp;</td>
						<td colspan="2" valign="top">


							<table cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td width="5%" valign="top"> 
										<div align="center">
<%                                                                               String compChecked = "";
                                                                                 if (targetMarket.getCompetitorConvert().equalsIgnoreCase("Y")){
                                                                                         compChecked = "checked";
                                                                                 } 
%>                                                             
											<input type="checkbox" name="competitorConvert" value="competitorConvert" <%= compChecked %> <%= globalDisable %>>
										</div>
									</td>
									<td width="3%">&nbsp;</td>
									<td width="48%" valign="top">
										<select name="competitor" <%= globalDisable %>>
											<option selected value="0">Choose Competitor...</option>
<%                                                                                          String competitorSelected = ""; 
                                                                                            for (int i=0; i < competitors.size(); i++){
                                                                                            Vendor vendor = (Vendor) competitors.get(i);
                                                                                            if (vendor.getId() == targetMarket.getCompetitorId()){
                                                                                                competitorSelected = "selected";
                                                                                            } else {
                                                                                                competitorSelected = "";
                                                                                            }
%>
                                                                                                    <option value='<%= vendor.getId() %>' <%= competitorSelected %>><%= vendor.getDescription() %></option>
<%                                                                                      }
%>
										</select>
										<span class="textgray"> <a href="#"></a>&nbsp;&nbsp;&nbsp;<br>
										(if checked choose a competitor)</span> 
									</td>
									<td width="44%" valign="top">&nbsp;
									</td>
								</tr>
							</table>
						</td>
					</tr>
      				<tr><td colspan="7">&nbsp;</td></tr>
     				<tr>
      					<td><div align="right">Commitment Level:</div></td>
      					<td>&nbsp;</td>
<%                                      if (targetMarket.getPlanAccounts().size() > 1){
%>                  
                                        <td colspan="5">Multiple</td>
<%                                      } else {
%>
				        <td colspan="5"><%= account.getCommitmentLevel() %></td>
<%                                      }
%>
   					</tr>

      				<tr><td colspan="7">&nbsp;</td></tr>
					
     				<tr>
      					<td valign="top"><div align="right">Segment targeting with this plan::</div></td>
      					<td>&nbsp;</td>
				        <td colspan="5">
				        	<table width="100%" border="0" cellspacing="0" cellpadding="0">
                            	
                            	
                            	
<%
                                        int remainder = 1;
                                        ArrayList thisTMSegments = targetMarket.getPlanSegments();
                                        for (int i = 0; i< allTMSegments.size(); i++){
                                            Segment segment = (Segment) allTMSegments.get(i);
                                            String segmentChecked = "";
                                            for (int segIndex = 0; segIndex < thisTMSegments.size(); segIndex++){
                                                int segId = ((Integer) thisTMSegments.get(segIndex)).intValue();
                                                if (segId == segment.getSegmentId()){
                                                        segmentChecked = "checked";
                                                }
                                            }
                                            if (remainder == 1){
%>
                                            <tr>
<%                                          }
%>
                                                <td valign="top"><input type="checkbox" name="segments" value="<%= segment.getSegmentId() %>" <%= segmentChecked %> <%= globalDisable %>> <%= segment.getName() %></td>
<%                                          if (remainder == 2){
%>                                          </tr>
<%                                          }
                                            if (remainder == 2){
                                                remainder = 1;
                                            } else {
                                                remainder = 2;
                                            }
                                        }
                                        if (remainder == 1){
%>                                          <tr>
<%                                      }
                                        String segmentOtherChecked = "";
                                        if (targetMarket.getSegmentOtherNotes().length() > 0){
                                            segmentOtherChecked = "checked";
                                        }
%>
                                                <td valign="top"><input type="checkbox" name="segmentOther" value="segmentOther" <%= segmentOtherChecked %> <%= globalDisable %>>
                                                    Other &nbsp;&nbsp; <input name="segmentOtherNotes" type="text" size="10" maxlength="100" value="<%= targetMarket.getSegmentOtherNotes() %>" <%= globalDisable %>>
                                                </td>
                                            </tr>
                            	<tr>
                            		<td colspan="5">&nbsp;</td>
                       		</tr>
                       	</table>
				        </td>
   					</tr>					

      				<tr><td colspan="7">&nbsp;</td></tr>

     				<tr>
      					<td valign="top"><div align="right">Business objective of target market program:</div></td>
      					<td>&nbsp;</td>
				        <td colspan="5">
				        	<textarea name="businessObjective" cols="40" rows="4" <%= globalDisable %>><%= targetMarket.getBusinessObjective() %></textarea>
				        </td>
   					</tr>					

      				<tr><td colspan="7">&nbsp;</td></tr>

     				<tr>
      					<td valign="top"><div align="right">Other distributors involved with this plan:</div></td>
      					<td>&nbsp;</td>
				        <td colspan="5">
                                          <!--  This hidden is used to add new accounts from popup page -->
                                           <input type=hidden name="addAccount" value="">
                                           <input type=hidden name="addAccountChildren" value="">
                                            <!--  This hidden is used to remove accounts from popup page -->
                                           <input type=hidden name="removeAccount" value="">
				        	<table width="100%" border="0" cellspacing="0" cellpadding="0">
<%                                                  Set tempSet = planAcctMap.keySet();
													Iterator tempIter = tempSet.iterator();
													while (tempIter.hasNext()){
                                                        String planAccountName = (String) tempIter.next();
                                                        String planAccountVcn = (String) planAcctMap.get(planAccountName);
                                                        if (!planAccountVcn.equals(account.getVcn())){

%>
                                                    <tr>
                                                            <input type=hidden name="distributors" value="<%= planAccountVcn %>">
                                                            <td width="66%"><%= planAccountName %> (<%= planAccountVcn %>)</td>
                                                            <td width="34%">&nbsp;</td>
                                                    </tr>
<%                                                      }
                                                    }
%>

                                                    <tr>
                                                            <td>&nbsp;</td>
<%                                                          if (!disableEverything){
%>
                                                            <td><a href="javascript:openPopup('TargetMarketCustomerBrowse?tmId=<%= tmId %>&type=dist','keyAccts',650,475)">
                                                                <img src="<%= sImagesURL %>button_add_new.gif" width="70" height="20" border="0"></a></td>
<%                                                          } else {
%>                                                          <td>&nbsp;</td>
<%                                                          }
%>
                                                    </tr>
                                                </table>
				        </td>
   					</tr>					

      				<tr><td colspan="7">&nbsp;</td></tr>
  					<tr>
      					<td valign="top"><div align="right">Plan Dates:</div></td>
      					<td>&nbsp;</td>
				        <td colspan="5">
				        	<table width="100%" border="0" cellspacing="0" cellpadding="0">

				        		<tr>
				        		   <td>
				        			<select name="startDateMo" <%= globalDisable %>>
<%										Calendar startCal = Calendar.getInstance();
                                        startCal.setTime(targetMarket.getStartDate());
										for (int i=1; i <= 12; i++){
											String startMoSelected = "";
											if ((startCal.get(Calendar.MONTH) + 1) == i){
												startMoSelected = "selected";
											}
%>
				        				<option value="<%= i %>" <%= startMoSelected %>><%= i %></option>
<%										}
%>				        			
									</select>
									<select name="startDateYear" <%= globalDisable %>>
<%										
										for (int i=lastYear; i <= nextYear; i++){
											String startYearSelected = "";
											if (startCal.get(Calendar.YEAR) == i){
												startYearSelected = "selected";
											}
%>
				        				<option value="<%= i %>" <%= startYearSelected %>><%= i %></option>
<%										}
%>				        			

									</select>
									</td>
                                   	
				        			<td>
				        			<select name="endDateMo" <%= globalDisable %>>
<%										Calendar endCal = Calendar.getInstance();
                                        endCal.setTime(targetMarket.getEndDate());
										for (int i=1; i <= 12; i++){
											String endMoSelected = "";
											if ((endCal.get(Calendar.MONTH) + 1) == i){
												endMoSelected = "selected";
											}
%>
				        				<option value="<%= i %>" <%= endMoSelected %>><%= i %></option>
<%										}
%>				        			
				        			
<%                                                      
%>
									</select>
									<select name="endDateYear" <%= globalDisable %>>
<%										for (int i=lastYear; i <= nextYear; i++){
											String endYearSelected = "";
											if (endCal.get(Calendar.YEAR) == i){
												endYearSelected = "selected";
											}
%>
				        				<option value="<%= i %>" <%= endYearSelected %>><%= i %></option>
<%										}
%>				        			

									</select>
									</td>
                                <tr>  
                                	<td>
                                		<span class="textgray">&nbsp; &nbsp; Start Date</span></td>
                                	<td>
                                    	<span class="textgray">&nbsp; &nbsp; End Date</span></td>
			        			    
				        		</tr>
			        		</table>
				        </td>
   					</tr>
	      	</table>
	 		</td>
      		<td width="31%" valign="top">   			
      			<table width="100%" border="0" cellspacing="0" cellpadding="2">
                	<tr>
                		<td class="cellShade">&nbsp;</td>
                		<td class="cellShade"><strong>Other TM plans for account</strong>:</td>
           			</tr>
<%                      for (int i=0; i< otherTargetMarketList.size(); i++){
                            TargetMarketReportBean bean = (TargetMarketReportBean) otherTargetMarketList.get(i);
                            String asterisk = "&nbsp;";
                            if (Globals.a2int(bean.getId()) == tmId){
                            	asterisk = "*";
                            }
%>
                            <tr><td class="cellShade"><%= asterisk %></td>
                				<td class="cellShade"><div align="left"><a href="TargetMarketSetup?acctId=<%= account.getVcn() %>&tmId=<%= bean.getId() %>"><%= bean.getDescription() %> (<%= bean.getStatus() %>)</a></div></td>
               				</tr>

<%                         
                        }
%>
                	
          		</table>
      		</td>
	  </tr>
  </table>
<br>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
<%               for (int i=0; i < salesObjectives.size(); i++){
                    ArrayList salesObj = (ArrayList) salesObjectives.get(i);
                    String soHeader = "";
                    String soSelected = "";
                    if (((Integer) salesObj.get(0)).intValue() == targetMarket.getSalesObjective()){
                        soSelected = "checked";
                    }
                    if (i==0){
                        soHeader = "Sales Objectives:";
                    } else {
%>                      </td></tr>
<%                  }
%>
		<tr>
			<td><div align="right"><%= soHeader %></div></td>
			<td width="2%">&nbsp;</td>
			<td colspan="2"><input type="radio" name="salesObjectives" value="<%= ((Integer) salesObj.get(0)).intValue() %>" <%= soSelected %> <%= globalDisable %> ><%= (String) salesObj.get(1) %>
<%              }
%>		
		<input type="text" name="salesObjectiveOtherNotes" value="<%= StringManipulation.noNull(targetMarket.getSalesObjectiveOtherNotes()) %>" <%= globalDisable %>></td>
                </tr>

	</table><br><br>
	
	<table width="100%" border="0" cellspacing="1" cellpadding="0">
		<tr align="right"><td colspan="12"><a href="javascript:openPopup('targetMarketChartHelp.jsp?tag=<%= forecastActualTag %>', 'tmpChartHelp', 500, 600)">What do these columns mean?</a></td></tr>
		<tr>
			<th width="5%">Include</th>
			<th width="12%">Product Line</th>
			<th width="5%">YTD Sales</th>
			<th width="6%">Prev Yr Sales</th>
			<th width="5%">Planned Sales Objective</th>
			<th width="5%">Incremental Growth</th>
			<th width="7%">Prev Plan Total Sales</th>
			<th width="7%">Prev Plan Sales to Date</th>
			<th width="6%">Sales in Plan</th>
			<th width="5%"><%= forecastActualTag %> Total Growth</th>
			<th width="5%"><%= forecastActualTag %> % Growth</th>
			<th width="5%"><%= forecastActualTag %> Payout<br>
			</th>
		</tr>
<%
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumFractionDigits(0);
                nf.setGroupingUsed(false);
                DecimalFormat df = new DecimalFormat();
                df.setMinimumIntegerDigits(1);
                df.setMinimumFractionDigits(2);
                df.setMaximumFractionDigits(2);
                df.setGroupingUsed(false);
                
                double ytdSalesTotal = 0;
                double prevSalesTotal = 0;
                double soTotal = 0;
                double incrGrowthTotal = 0;
                double baselineSalesTotal = 0;
                double prevPlanSalesTotalToDate = 0;
                double planSalesTotal = 0;
                double totalGrowthTotal = 0;
                double totalGrowthPercentage = 0;
         //       double payoutTotal = 0;
                for (int i=0; i < combinedProductList.size(); i++){
                    TargetMarketProduct targetMarketProduct = (TargetMarketProduct) combinedProductList.get(i);
                    ytdSalesTotal += targetMarketProduct.getYTDSales();
                    prevSalesTotal += targetMarketProduct.getPrevSales();
                    soTotal += targetMarketProduct.getSalesObjective();
                    incrGrowthTotal += targetMarketProduct.getIncrementalGrowth();
                    baselineSalesTotal += targetMarketProduct.getBaselineSales();
                    prevPlanSalesTotalToDate += targetMarketProduct.getPrevPlanToDateSales();
                    planSalesTotal += targetMarketProduct.getSalesInPlan();
                    totalGrowthTotal += targetMarketProduct.getTotalGrowth();
           //         payoutTotal += targetMarketProduct.getPayout();
                        
%>
		<tr class="cellShade">
			<td><input type="checkbox" name="productChecked<%= targetMarketProduct.getProduct().getId() %>" value="productChecked<%= targetMarketProduct.getProduct().getId() %>" <%= targetMarketProduct.getChecked() %> <%= globalDisable %>>
			</td>
			<td><%= targetMarketProduct.getProduct().getId() %> - <%= targetMarketProduct.getProduct().getDescription() %></td>
			<td><div align="center"><input name="ytdSales" type="text" size="10" disabled value="<%= nf.format(targetMarketProduct.getYTDSales()) %>"></td>
			<td><div align="center"><input name="prevSales" type="text" size="10" disabled value="<%= nf.format(targetMarketProduct.getPrevSales()) %>"></td>
			<td><div align="center"><input name="prodSalesObj<%= targetMarketProduct.getProduct().getId() %>" type="text" size="10" value="<%= nf.format(targetMarketProduct.getSalesObjective()) %>" onBlur="updateIncrementalGrowth(this, <%= i %>)" <%= globalDisable %>>
			</div></td>
			<td><div align="center"><input name="incrGrowth" type="text" size="10" disabled value="<%= nf.format(targetMarketProduct.getIncrementalGrowth()) %>">
			</div></td>
<%
                        if (targetMarket.isActive()){
%>
			<td><div align="center"><input name="baselineSales" type="text" size="10" disabled value="<%= nf.format(targetMarketProduct.getBaselineSales()) %>"></td>
			<td><div align="center"><input name="baselineSalesToDate" type="text" size="10" disabled value="<%= nf.format(targetMarketProduct.getPrevPlanToDateSales()) %>"></td>
			<td><div align="center"><input name="salesInPlan" type="text" size="10" disabled value="<%= nf.format(targetMarketProduct.getSalesInPlan()) %>"></td>
			<td><div align="center"><input name="totalGrowth" type="text" size="10" disabled value="<%= nf.format(targetMarketProduct.getTotalGrowth()) %>"></td>
			<td><div align="center"><input name="growthPercentage" type="text" size="10" disabled value="<%= df.format((targetMarketProduct.getGrowthPercentage() * 100)) %>"></td>
			<td><div align="center"><input name="payout" type="text" size="10" disabled value="<%= nf.format(targetMarketProduct.getPayout()) %>"></td>
<%                      } else {
%>
                        <td>&nbsp;</td>
			<td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
<%                      }  %>
			
		</tr>
<%              }
                if (targetMarket.isActive()){
                 //   if (baselineSalesTotal == 0){
                 //       totalGrowthPercentage = 100;
                 //   } else {
                 //       totalGrowthPercentage = (totalGrowthTotal / baselineSalesTotal) * 100;
                 //   }
                    totalGrowthPercentage = ((planSalesTotal - prevPlanSalesTotalToDate) / prevPlanSalesTotalToDate);
                    double payoutTotal = ((Double) request.getAttribute("planTotalPayout")).doubleValue();
%>
                <tr class="cellShade">
                    <td colspan="2" align=center><b>Totals</b></td>
                    <td><b><%= nf.format(ytdSalesTotal) %></b></td>
                    <td><b><%= nf.format(prevSalesTotal) %></b></td>
                    <td><b><%= nf.format(soTotal) %></b></td>
                    <td><b><%= nf.format(incrGrowthTotal) %></b></td>
                    <td><b><%= nf.format(baselineSalesTotal) %></b></td>
                    <td><b><%= nf.format(prevPlanSalesTotalToDate) %></b></td>
                    <td><b><%= nf.format(planSalesTotal) %></b></td>
         <%--           <td><b><%= nf.format(totalGrowthTotal) %></b></td>  --%>
                    <td><b><%= nf.format(baselineSalesTotal * totalGrowthPercentage) %></b></td>
                    <td><b><%= df.format((totalGrowthPercentage * 100)) %></b></td>
                    <td><b><%= nf.format(payoutTotal) %></b></td>
                </tr>
<%              double actualPayout = 0;
                if (payoutTotal > targetMarket.getMaximumPayout()){
                    actualPayout = targetMarket.getMaximumPayout();
                } else {
                    actualPayout = payoutTotal;
                }

%>
                <tr>
                    <td colspan="9">&nbsp;</td>
                    <td colspan="2" align=center><b><%= forecastActualTag %> Payout</b></td>
                    <td><b><%= nf.format(actualPayout) %></b></td>
                </tr>
<%              }
%>
	</table>
	<br>
	<br><hr align="center" width="100%" size="1" color="#999999" noshade>
	<p class="heading3">Sales Plan</p>
	
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="19%" valign="top"><div align="right">Sales Plan Used:</div></td>
					<td width="2%">&nbsp;</td>
					<td width="79%">
<%                                      String salesPlan1Checked = "";
                                        String salesPlan2Checked = "";
                                        if (targetMarket.getSalesPlanUsed().equalsIgnoreCase("Y")){
                                                salesPlan1Checked = "checked";
                                        } else {
                                                salesPlan2Checked = "checked";
                                        }
%>
						<table width="100%" border="0" cellspacing="0" cellpadding="1">
							<tr>
								<td colspan="3"><input type="radio" name="salesPlan" value="salesPlan1" <%= salesPlan1Checked %> <%= globalDisable %>>Sales Plan(s) from distributors on this target market plan</td>
							</tr>
                                                        <tr>
								<td width="35%" valign="top"> <input type="radio" name="salesPlan" value="salesPlan2" <%= salesPlan2Checked %> <%= globalDisable %>>End Customer's Sales Plan</td>
								<td width="10%">&nbsp;</td>
<%                                                          if (!disableEverything){
%>
                                                            <td width="65%" valign="top"><a href="javascript:openPopup('TargetMarketCustomerBrowse?tmId=<%= tmId %>&type=end','keyAccts',650,475)">
                                                                <img src="<%= sImagesURL %>button_add_new.gif" width="70" height="20" border="0"></a></td>
<%                                                          } else {
%>                                                          <td width="65%">&nbsp;</td>
<%                                                          }
%>
                                                           </tr>
                                            <!--  This hidden is used to add new end customers from popup page -->
                                           <input type=hidden name="addEndCustomer" value="">
                                           <input type=hidden name="addEndCustomerChildren" value="">
                                           <!--  This hidden is used to remove end customers from popup page -->
                                           <input type=hidden name="removeEndCustomer" value="">
<%                                                      tempSet = endCustMap.keySet();
														tempIter = tempSet.iterator();
														while (tempIter.hasNext()){
															String endCustName = (String) tempIter.next();
															String endCustVcn = (String) endCustMap.get(endCustName);
                                                            
%>                                                          <tr>
                                                                <input type=hidden name="endCustomers" value="<%= endCustVcn %>">
								<td width="75%" valign="top" colspan="2">&nbsp;&nbsp;&nbsp;<span class="textgray"><%= endCustName %> (<%= endCustVcn %>)</span></td>
								<td width="25%" valign="top"></td>
                                                            </tr>

<%                                                      }
%>
					</table>
					</td>
				</tr>
			</table>
              <br>
<!--   May be needed for phase 2
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                   <tr>
                        <td><span class="heading3">Bid Manager Rebate</span></td>
                        <td align="right"><a href="TargetMarketCIFD.doc">CIFD Target Market Program Info</a></td>
                   </tr>
                   <tr>
                        <td colspan="2">
                            3/4 % of total distributor generated Bid-manager orders of the CIFD products specified
                            above will be rebated back to xx Electric based on dollar 1 sales. An additional 3/4 %
                            bonus will be included if the xx Electric entered orders in 2003 grows 10% from 2002.
                            This program is capped at $12,000.
                       </td>
                   </tr>
                   <tr> <td colspan="2">
<%  //                        String bidManChecked = "";
      //                      if (targetMarket.getIncludeBidmanRebate().equalsIgnoreCase("Y")){
        //                        bidManChecked = "checked";
          //                  }
%>
                            <input type="checkbox" name="bidManRebate" value="bidManRebate" >
                             Include Bid Manager Rebate (CIFD)
                         </td>
                  </tr>
                </table>
-->
		    <br>
	
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="19%"><div align="right">Distributor Resources to support this plan:</div></td>
				<td width="2%">&nbsp;</td>
				<td width="79%">
<%                                      if (!disableEverything){
%>
					<select name="distributorContacts" size="4" multiple>
<%                                      }
                                             for (int allIndex = 0; allIndex < allContacts.size(); allIndex++){
                                                    Contact distContact = (Contact) allContacts.get(allIndex);
                                                    ArrayList planContacts = targetMarket.getPlanContacts();
                                                    String distContactSelected = "";
                                                    for (int planIndex = 0; planIndex < planContacts.size(); planIndex++){
                                                        Contact planContact = (Contact) planContacts.get(planIndex);
                                                        if (distContact.getId() == planContact.getId()){
                                                            distContactSelected = "selected";
                                                        }
                                                    }
                                                    if (disableEverything){
                                                        if (distContactSelected.equalsIgnoreCase("selected")){
%>
                                                            <%= distContact.getLastName() %>, <%= distContact.getFirstName() %> <br>
<%                                                      }
                                                    } else {
%>                                                      <option value="<%= distContact.getId() %>" <%= distContactSelected %>><%= distContact.getLastName() %>, <%= distContact.getFirstName() %></option>
<%                                                  }
                                                }
                                       if (!disableEverything){
%>
                        		        </select>
<%                                      }
%>
				</td>
			</tr>
  </table>
<br>
<br>		<table width="100%" border="1" cellspacing="0" cellpadding="0" rules="none">
                        <tr>
                        <td><span class="heading3">Incentive Payout</span></td>
                        </tr>
                        <tr> <td> &nbsp; </td></tr>
                        </tr>
			<tr>
				<td width="100%" valign="top" >What is the payout for incremental growth?</td>
                       </tr>
                        <tr>
                                <td width="100%" valign="top" > &nbsp;&nbsp;&nbsp;
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="15%" valign="top"><div align="right">% Growth:</div></td>
							<td width="3%">&nbsp;</td>
							<td width="6%"><input name="percentGrowth1" type="text" size="4" value="<%= targetMarket.getPercentGrowth1() %>" <%= globalDisable %>></td>
							<td width="6%"><input name="percentGrowth2" type="text" size="4" value="<%= targetMarket.getPercentGrowth2() %>" <%= globalDisable %>></td>
							<td width="6%"><input name="percentGrowth3" type="text" size="4" value="<%= targetMarket.getPercentGrowth3() %>" <%= globalDisable %>></td>
                                                        <td ><input name="percentGrowth4" type="text" size="4" value="<%= targetMarket.getPercentGrowth4() %>" <%= globalDisable %>></td>
						</tr>
						<tr>
							<td valign="top"><div align="right">% Payout:</div></td>
							<td>&nbsp;</td>
							<td><input name="percentPayout1" type="text" size="4" value="<%= targetMarket.getPercentPayout1() %>" <%= globalDisable %>>
							</td>
							<td><input name="percentPayout2" type="text" size="4" value="<%= targetMarket.getPercentPayout2() %>" <%= globalDisable %>>
							</td>
							<td><input name="percentPayout3" type="text" size="4" value="<%= targetMarket.getPercentPayout3() %>" <%= globalDisable %>>
							</td>
                                                        <td><input name="percentPayout4" type="text" size="4" value="<%= targetMarket.getPercentPayout4() %>" <%= globalDisable %>>
						    </td>
						</tr>
					</table>
				</td>
			</tr>
                        <tr> <td> &nbsp; </td></tr>
                        <tr>
				<td width="100%" valign="top" >How will sales be tracked?</td>
                       </tr>
<%                      for (int i=0; i < salesTrackingDollars.size(); i++){
                            String salesDollarChecked = "";
                            ArrayList trackingDollar = (ArrayList) salesTrackingDollars.get(i);
                            if ((targetMarket.getSalesTrackingTypeId() == ((Integer) trackingDollar.get(0)).intValue()) ||
                                 (tmId == 0) && (i ==0)){
                                salesDollarChecked = "checked";
                            }
%>
                            <tr>
				<td width="100%" valign="top" ><input type="radio" name="salesTrackingDollar" value="<%= ((Integer) trackingDollar.get(0)).intValue() %>" <%= salesDollarChecked %>  <%= globalDisable %>> <%= (String) trackingDollar.get(1) %></td>
                            </tr>
<%
                        }
%>
                       
                       <tr> <td> &nbsp; </td></tr>
                       <tr>
                                 <td width="100%" valign="top" >What is the maximum payout? <input type=text name="maximumPayout" maxlength="8" size="8" value="<%= (int) targetMarket.getMaximumPayout() %>" <%= globalDisable %>>
                       
		</table><br><br>
		<table width="100%" border="0" cellspacing="10" cellpadding="0">
<%              if (!targetMarket.isActive() && !targetMarket.isRejected()){ 
	                 if (targetMarket.isSaved() || targetMarket.isUnsaved() || (tmId == 0)){
	                 	String buttonName = "button_save_send.gif";
	                 	if (targetMarket.isPendingResubmission()){
	                 		buttonName = "button_resubmit.gif";
	                 	}
%>
			<tr>
				<td><a href="javascript: submitForm('save')"><img src="<%= sImagesURL %>button_save.gif" width="70" height="20" border=0></a>&nbsp;&nbsp;
				<a href="javascript: submitForm('send')"><img src="<%= sImagesURL %><%= buttonName %>" width="70" height="20" border=0></a>&nbsp;&nbsp;
				
<%						if (canPurge){			%>
							<a href="javascript: submitForm('purge')"><img src="<%= sImagesURL %>button_delete.gif" width="70" height="20" border=0></a>
<%						} %>

				</td>
			</tr>
<%                  }

                    if (canApprove && (targetMarket.isSubmitted() || targetMarket.isDMApproved())){
                    	String buttonName = "button_approve.gif";
                    	if (targetMarket.isPendingResubmission()){
                    		buttonName = "button_resubmit.gif";
                    	}
%>
			<tr>
				<td colspan="3"><a href="javascript: submitForm('approve')"><img src="<%= sImagesURL %><%= buttonName %>" width="70" height="20" border=0></a>&nbsp;&nbsp;
				<a href="javascript: submitForm('reject')"><img src="<%= sImagesURL %>button_reject.gif" width="70" height="20" border="0"></a>&nbsp;&nbsp;
				<a href="#" onclick='hideShow("reviseResubmitExplanationDiv", true);return false;'><img src="<%= sImagesURL %>button_revise_resubmit.gif" width="70" height="20" border="0"></a></td>
				
				
					<td>
						<div id="reviseResubmitExplanationDiv" style="display: none">
							<table>
								<tr>
									<td>
										<u>Workflow Step, Name</u>
									</td>
								</tr>
	<%
							int priorApproversSize = priorApprovers.size();											
							if(priorApproversSize == 0){
	%>						
								<tr>
									<td>
										No prior approvals.
									</td>
								</tr>		
	<%						
							}
							else{
								for(int priorApproversIndex=0; priorApproversIndex < priorApproversSize; priorApproversIndex++){
									
									WorkflowPriorApprover currentWorkflowPriorApprover = (WorkflowPriorApprover)priorApprovers.get(priorApproversIndex);
		%>
									<tr>
										<td>
											<INPUT TYPE=radio NAME="reviseResubmitApprover" onclick='updateReviseResubmitApproverWorkflowStepName("<%=currentWorkflowPriorApprover.getWorkflowStepName()%>");' VALUE="<%=currentWorkflowPriorApprover.getWorkflowApprovalId()%>"><%=currentWorkflowPriorApprover.getWorkflowStepName()%>, <%=currentWorkflowPriorApprover.getPriorApproverFirstName()%>&nbsp;<%=currentWorkflowPriorApprover.getPriorApproverLastName()%>
										</td>
									</tr>											
		<%							
								}

		%>									
									<tr>
										<td>
											Explanation Text:<textarea rows="3" cols="20" name="reviseResubmitExplanation"></textarea>
											<br><a href="#" title="Explain what needs updated by prior approver in TAP." onclick="alert('Explain what needs updated by prior approver in TAP.');">Help</a>
										</td>
										<td>
											<a href="#" onclick='submitReviseResubmit("<%=priorApproversSize%>"); return false;'> Submit </a>
										</td>
									</tr>
		<%
							}
		%>							
							</table>
						</div>
				 </td>
				 
				 
			</tr>
<%                  }
%>
		
<%              } else if (canPurge){
%>
				<tr>
					<td width="75"></td>
					<td width="75"></td>
					<td align="left"><a href="javascript: submitForm('purge');"><img src="<%= sImagesURL %>button_purge.gif" width="70" height="20" border=0></a></td>
				</tr>		
<%				}   %>
		</table>

                <input type=hidden name="action" value="none">
                
                <input type=hidden name="reviseResubmitExplantion" value="">
                <input type=hidden name="selectedPriorApproverWorkflowApprovalId" value="">
                <input type=hidden name="targetMarketStatus" value="">
                
		<p class="heading3">&nbsp;</p>
  </td>
  </tr>
  
</table>
</form>
<p>&nbsp;</p>
</body>
</html>