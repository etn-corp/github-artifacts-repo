<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page import="com.eaton.electrical.smrc.util.*" %>
<html>
<%@ include file="./SMRCHeaderNoNav.jsp" %>
<%@ include file="analytics.jsp" %>
<%
	int taskRow = ((Integer) request.getAttribute("taskRow")).intValue() ;
	ArrayList fileNames = (ArrayList) request.getAttribute("fileNames");
	ArrayList hiddenNames = (ArrayList) request.getAttribute("hiddenNames");
%>
<script>
	
	
	availableSlots = window.opener.getNumberAvailable(<%= taskRow %>);
	
	var setInnerHTML = function( id, str ){
	    if(!document.getElementById) return; // Not Supported
		if(document.getElementById){
			document.getElementById(id).innerHTML = str;
		}
	}
	
    docList = new Array();
        
	function changeListDisplay(){
		var displayString = "<font size=2 color=\"#71674A\">";
		displayString = "<input type=hidden name=availableSlots value=" + availableSlots + " >";
		for (i=0; i<docList.length; i++){
			displayString += "&#149;&nbsp;&nbsp;";
			displayString += docList[i].fileName;
			displayString += "&nbsp;&nbsp;&nbsp;&nbsp; <img src=\"<%= sImagesURL %>/remove.gif\" onClick=\"remove(" + i + ")\" alt=\"Remove from list\"> <br>";
			displayString += "<input type=hidden name=doc" + i + " value=" + docList[i].fileName + " >";
			displayString += "<input type=hidden name=doc" + i + "_hidden value=" + docList[i].hiddenName + " >";
		}
		
		
		displayString += "</font><br>";
		setInnerHTML("docListDisplay", displayString);
	}

	function remove(index){
		for (i=index; i < (docList.length - 1); i++){
			docList[i] = docList[i+1];
		}
		// remove the last item since it will now be duplicated
		docList.length = (docList.length - 1);
		changeListDisplay();
		
	}
	
	function updateParent(){
		
			startFrom = <%= Globals.getTaskAttachmentLimit() %> - availableSlots;
			for (i=0; i<docList.length; i++){
				window.opener.addAttachment(<%= taskRow %>,startFrom + i,docList[i].fileName,docList[i].hiddenName);
			}
		
			window.opener.showAttachedFiles();	
	}
	
	function submitAddDocumentForm(){
		    if (availableSlots > 0){
				document.documentlist.submit();
			} else {
				alert("No additional documents can be attached to this task. Please delete an existing attchment from this task first.");
			}	
	}
	
	function files (fileName, hiddenName){
		this.fileName = fileName;
		this.hiddenName = hiddenName;
	}
	
	
	<%
		for (int i=0; i < fileNames.size(); i++){
			String filename = (String) fileNames.get(i);
			String modFilename = StringManipulation.addExtraBackslash(filename);
			String hiddenName = (String) hiddenNames.get(i);
			String modHiddenName = StringManipulation.addExtraBackslash(hiddenName);
	%>
			file<%= i %> = new files('<%= modFilename %>', '<%= modHiddenName %>');
			docList[docList.length] = file<%= i %>;
			window.opener.addToTempFilesDeleted('<%= modHiddenName %>');
	<%	}
	%>
	
</script>
<style>
<!-- TAP color = #71674A -->

	.bupload { 
	BORDER-RIGHT: #FFFFFF 1px solid;
	BORDER-TOP: #FFFFFF 1px solid;
	FONT-SIZE: 15px;
	BORDER-LEFT: #FFFFFF 1px solid;
	WIDTH: 85px;
	BORDER-BOTTOM: #FFFFFF 1px solid;
	BACKGROUND-COLOR: #FFFFFF;
	
}
</style> 
<body>
<table border=0>
<tr>
	<td width=10>&nbsp;</td>
	<td>
	<table cellpadding=10>
		<form name="documentlist" action="TaskAttachments" method=post enctype="multipart/form-data">
		<input type=hidden name=taskRow value=<%= taskRow %>>
		<tr>
			<td colspan=2><div class="heading2">You can add up to <script> document.write(availableSlots) </script> documents to this Task<div></td>
		</tr>
		<tr>
			<td align=left colspan=2><span class="heading3">Select Document  </span>&nbsp;&nbsp;&nbsp;
				<input type=file name="uploader" class=bupload value="" size=-1 onChange="submitAddDocumentForm()">
			</td>
		</tr>
		<tr valign="top">
			<td><div class="heading3">Pending Attachments: </div></td>
			<td>
				<div id=docListDisplay>&nbsp;</div>
			</td>
		</tr>
		<tr>
			<td colspan=2><center><input type=button name=submitButton value="Add Documents to Task" onClick="updateParent(); self.close();"></center>
			</td>
		</tr>
		</form>
	</table>
	</td>
</tr>
</table>
<script>
	changeListDisplay();
</script>
</body>

</html>