<%@ page language="java" errorPage="SMRCErrorPage.jsp" %>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ include file="analytics.jsp" %>
<%
ArrayList segments = (ArrayList)request.getAttribute("segments");
Segment currentSegment = (Segment)request.getAttribute("currentSegment");
boolean canSeeApprovalLink = ((Boolean) request.getAttribute("canSeeApprovalLink")).booleanValue();
int approvalsPending = ((Integer) request.getAttribute("approvalsPending")).intValue();

%>
<html>
<%@ include file="./SMRCHeader.jsp" %>
<script language="Javascript">
function gotoCSF(){
var number=document.csfform.csf.selectedIndex;
if(number==0){ return false; }
location.href="CSF?page=Division&divisionId=" + document.csfform.csf.options[number].value;
}


function goMap(segmentId, geog) {
	location.href = "CustomerListing?page=listing&otherHomeSegment=" + segmentId + "&DISTRICT=" + geog;
}
</script>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750"> 
      <p><br>
        <a class="crumb" href="SMRCHome">Home Page</a>
    </td>
  </tr>

</table>
<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="10">&nbsp;</td>
    <td width="750" valign="top"> 
      <p>&nbsp;</p>
      <p class="heading2">Target Accounts
      <% if (canSeeApprovalLink) { %>
          &nbsp;&nbsp;&nbsp;&nbsp;<span><a href="PendingApprovals">You have <%= approvalsPending %> requests for approval</a></span>
      <% } %>
      </p>
	  <p>
	  Click a segment on the left or click the desired district on the map.<br>
	  <img src="<%= sImagesURL %>toolbox.gif" border="0" align="absmiddle"> indicates toolbox link
	  <%
	  String segmentId=request.getParameter("segmentId");
	  	if(segmentId != null){
	  %>
		<br><br><a href="CustomerListing?page=listing&otherHomeSegment=<%= segmentId %>">All <b><%= currentSegment.getName() %></b> Accounts</a>
	  <%
	  	}else{
			segmentId="0";
		}
	  %>
	  
	  
	  </p>
      <table width="750" border="0" cellspacing="0" cellpadding="0">

      	<tr>
      		<td width="420" valign="top">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
				 <td width="63%"><hr width="100%" noshade color="#CCCCCC"></td>
				 <td width="37%">&nbsp;</td>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">		
				<%
				int col=0;
				for (int i=0; i < segments.size(); i++) {
				Segment segment = (Segment)segments.get(i);
				ArrayList subSegments = segment.getSubSegments();

					if(subSegments.size()==0){
			 		  %>  
  				  <tr class="cellShade">
  					<td valign="top"><a href="CustomerListing?page=listing&otherHomeSegment=<%= segment.getSegmentId() %>"><strong><%= segment.getName() %></strong></a>&nbsp;&nbsp;<a href="AccountToolbox?segmentId=<%= segment.getSegmentId() %>"><img src="<%= sImagesURL %>toolbox.gif" border="0" align="absmiddle"></a></td>
		 		  </tr>
    				<!--<tr>
    					<td valign="top">&nbsp;&nbsp;<img src="<%= sImagesURL %>brown_bullet.gif" width="5" height="5" align="absmiddle">&nbsp;&nbsp;No Sub-Segments</td>
			 		  </tr>-->
    				<%							
					}else{
					%>
						 <tr class="cellShade">
    					<td valign="top"><a href="SMRCHome?page=Other&segmentId=<%= segment.getSegmentId() %>"><strong><%= segment.getName() %></strong></a>&nbsp;&nbsp;<a href="AccountToolbox?segmentId=<%= segment.getSegmentId() %>"><img src="<%= sImagesURL %>toolbox.gif" border="0" align="absmiddle"></a></td>
			 		  </tr>
					<%
						for (int j=0; j < subSegments.size(); j++) {
						Segment subSegment = (Segment)subSegments.get(j); 
			 		  %>  
    				<tr>
    					<td valign="top" nowrap="nowrap" >&nbsp;&nbsp;<img src="<%= sImagesURL %>brown_bullet.gif" width="5" height="5" align="absmiddle">&nbsp;&nbsp;<a href="SMRCHome?page=Other&segmentId=<%= subSegment.getSegmentId() %>"><%= subSegment.getName() %></a>&nbsp;&nbsp;<a href="AccountToolbox?segmentId=<%= subSegment.getSegmentId() %>"><img src="<%= sImagesURL %>toolbox.gif" border="0" align="absmiddle"></a></td>
			 		  </tr>
    				<%
    					}
    			}
    			%>
				  <tr class="">
  					<td valign="top">&nbsp;</td>
		 		  </tr>
				<%
	      }
	      %>
      				

			 </table>


   		 </td>
      		<td width="330" valign="top">
      		<form name="csfform">
      		<img src="<%= sImagesURL %>districts2010.gif" border="0" usemap="#Map">
      		<p align="center"><br>
      		View Critical Success Factors for Division &nbsp;
      		<select name="csf" onchange="javascript: return gotoCSF()">
      		<option selected value="-1">Select Division...</option>
      		<%
					ArrayList divisions = (ArrayList)request.getAttribute("divisions");
					for (int j=0; j < divisions.size(); j++) {
								Division div = (Division)divisions.get(j); 
					%>
						<option value="<%= div.getId() %>"><%= div.getName() %></option>
					<% } %>
      		</select>
					<a href="CSF?page=Division&divisionId=divid"></a>
      		</p>
      		</form>
      		</td>
	   </tr>

   	 </table>
      <p class="heading3">&nbsp;
   	 </p>
      <p align="center" class="heading2">
  	   <br>
	 </p>
	  </td>
  </tr>
</table>

<map name="Map">


<area shape="poly" alt="Carolinas" coords="400,233, 443,178, 443,168, 400,168, 373,191, 384,191, 393,227" href="javascript:goMap('<%= segmentId %>','14547')"">
<area shape="poly" alt="Florida" coords="367,250, 379,250, 389,259, 389,275, 413,300, 418,297, 418,277, 409,259, 400,241, 400,236, 396,233, 396,229, 389,227, 376,236, 369,241, 362,250" href="javascript:goMap('<%= segmentId %>','14545')">
<area shape="poly" alt="Atlanta" coords="331,250, 364,250, 370,255, 366,250, 371,237, 384,232, 393,224, 389,214, 384,207, 385,201, 380,191, 371,191, 364,200, 355,201, 349,191, 349,195, 340,197, 335,200, 336,207, 340,214, 335,217, 340,227, 340,232, 331,241" href="javascript:goMap('<%= segmentId %>','14544')">
<area shape="poly" alt="Mid South" coords="292,259, 328,266, 323,255, 328,250, 328,241, 340,229, 336,223, 333,220, 334,214, 334,205, 334,197, 349,191, 356,200, 366,197, 393,168, 366,174, 359,178, 354,168, 340,174, 328,184, 310,184, 302,184, 292,191, 279,191, 282,184, 270,184, 275,207, 286,207, 297,214, 302,218, 302,233,292,241, 292,255" href="javascript:goMap('<%= segmentId %>','14573')">
<area shape="poly" alt="Capitol" coords="400,168, 443,163, 432,131, 409,131, 409,153" href="javascript:goMap('<%= segmentId %>','14541')">
<area shape="poly" alt="Cincinnati" coords="323,178, 319,184, 328,184, 328,178, 333,178, 340,168, 357,168, 360,178, 366,174, 366,168, 378,168, 384,168, 398,168, 409,153, 406,149, 408,139, 393,131, 393,127, 386,131, 378,123, 366,127, 353,119, 349,127, 349,131, 340,131, 332,157, 323,174" href="javascript:goMap('<%= segmentId %>','14571')">
<area shape="poly" alt="New York" coords="451,123, 467,106, 451,115" href="javascript:goMap('<%= segmentId %>','14525')">
<area shape="poly" alt="Philadelphia" coords="443,110, 433,106, 422,110, 422,119, 413,131, 437,131, 443,149, 447,149, 447,139, 451,127" href="javascript:goMap('<%= segmentId %>','14524')">
<area shape="poly" alt="New England" coords="444,62, 459,62, 465,30, 474,30, 484,47, 492,54, 477,66, 468,83, 474,93, 472,98, 464,102, 451,115, 451,123, 443,117, 443,110, 451,106, 451,102" href="javascript:goMap('<%= segmentId %>','14521')">
<area shape="poly" alt="Pittsburgh" coords="393,110, 405,96, 425,87, 424,77, 441,66, 451,102, 447,106, 443,106, 436,102, 414,110, 419,118, 410,131, 397,131" href="javascript:goMap('<%= segmentId %>','14522')">
<area shape="poly" alt="Great Lakes" coords="349,73, 340,91, 344,110, 357,119, 368,127, 381,123, 388,131, 391,127, 393,110, 367,116, 373,102, 370,91, 363,87, 361,77" href="javascript:goMap('<%= segmentId %>','14514')">
<area shape="poly" alt="Chicago" coords="309,110, 315,123, 311,127, 302,127, 302,139, 297,139, 298,144, 308,144, 308,149, 321,149, 328,149, 332,153, 332,139, 334,139, 334,131, 336,131, 336,127, 345,131, 345,123, 349,119, 343,114, 328,114, 328,110" href="javascript:goMap('<%= segmentId %>','14514')">
<area shape="poly" alt="North Central" coords="180,37, 263,40, 263,37, 270,37, 270,44, 280,45, 285,49, 292,51, 297,49, 302,51, 288,67, 292,68, 307,67, 320,57, 317,62, 319,66, 328,66, 328,69, 346,62, 349,68, 328,80, 328,110, 323,110, 310,110, 302,110, 298,105, 292,105, 292,102, 270,102, 270,110, 263,110, 263,105, 252,104,248,102, 240,93, 227,93, 227,87, 212,87, 207,87, 207,82, 199,82, 199,73, 194,73, 194,55, 190,54, 184,48" href="javascript:goMap('<%= segmentId %>','14515')">
<area shape="poly" alt="Heartland" coords="217,87, 212,105, 222,110, 222,119, 218,118, 217,123, 204,123, 203,119, 199,123, 199,127, 205,127, 204,139, 212,140, 207,168, 199,168, 203,178, 270,178, 277,183, 286,183, 283,191, 292,191, 292,184, 302,183, 302,178, 318,178, 322,178, 316,168, 325,168, 327,161, 328,157, 325,157,320,149, 313,149, 307,149, 307,144, 302,144, 296,144, 296,138, 302,137, 302,127, 311,127, 314,123, 310,115, 302,110, 292,110, 292,104, 270,104, 270,110, 262,110, 248,106, 241,95, 227,93, 227,87" href="javascript:goMap('<%= segmentId %>','14516')">
<area shape="poly" alt="Dallas" coords="199,178, 222,182, 222,201, 212,207, 212,211, 212,221, 222,222, 222,227, 230,230, 228,241, 234,241, 242,231, 248,235, 252,235, 257,241, 270,241, 270,245, 270,247, 270,250, 277,249, 276,241, 288,241, 285,236, 292,232, 301,232, 302,225, 302,220, 299,219, 297,215, 283,214, 283,207, 277,207,274,211, 270,207, 270,189, 270,182, 270,178, 266,178" href="javascript:goMap('<%= segmentId %>','14561')">
<area shape="poly" alt="Houston" coords="252,270, 262,270, 269,262, 270,259, 292,261, 291,252, 285,252, 285,247, 282,247, 281,241, 278,241, 278,253, 270,253, 266,247, 268,241, 265,241, 261,241, 259,241, 252,241, 252,256, 248,259, 252,266" href="javascript:goMap('<%= segmentId %>','14562')">
<area shape="poly" alt="Austin" coords="199,257, 222,284, 222,295, 246,304, 243,288, 252,272, 252,266, 245,258, 252,253, 252,251, 252,247, 252,244, 252,240, 252,237, 246,237, 242,232, 233,241, 226,238, 227,229, 222,228, 222,222, 212,222, 212,213, 208,212, 208,207, 214,207, 214,205, 222,201, 222,182, 199,182, 199,178, 183,178,182,182, 184,190, 189,193, 173,205, 182,205, 183,207, 187,213, 184,213, 183,228, 186,229, 186,236, 199,246" href="javascript:goMap('<%= segmentId %>','14562')">
<area shape="poly" alt="Southwest" coords="74,207, 74,212, 107,230, 135,233, 136,228, 152,229, 169,249, 171,258, 184,266, 190,255, 198,255, 199,247, 186,237, 182,235, 184,231, 181,228, 181,213, 184,211, 180,207, 172,207, 172,201, 179,197, 181,194, 186,194, 180,186, 178,186, 177,183, 171,178, 171,174, 159,174, 157,168, 148,168,148,161, 139,161, 139,168, 101,164, 101,157, 107,149, 91,149, 91,144, 86,144, 86,149, 81,149, 74,168, 81,183" href="javascript:goMap('<%= segmentId %>','14567')">
<area shape="poly" alt="Denver" coords="86,141, 94,141, 93,149, 107,149, 107,153, 103,157, 102,162, 136,168, 136,157, 151,161, 151,168, 157,168, 158,168, 159,172, 202,174, 202,168, 206,168, 207,157, 208,141, 202,141, 203,127, 199,127, 199,118, 205,118, 206,121, 217,122, 216,117, 222,117, 222,110, 213,106, 212,99, 215,94,215,87, 208,91, 207,87, 201,83, 201,79, 201,74, 194,73, 194,83, 163,81, 163,83, 166,87, 166,91, 151,91, 142,92, 139,95, 135,98, 131,97, 129,110, 121,110, 81,101, 77,101, 76,110, 81,114, 74,131, 81,133" href="javascript:goMap('<%= segmentId %>','14534')">
<area shape="poly" alt="Los Angeles" coords="21,163, 53,197, 53,205, 77,207, 81,183, 57,150, 49,149, 54,157, 54,168" href="javascript:goMap('<%= segmentId %>','14533')">
<area shape="poly" alt="San Franicisco" coords="21,87, 12,103, 17,110, 12,114, 12,123, 21,134, 21,157, 25,161, 52,167, 51,157, 49,151, 49,147, 58,148, 71,168, 76,146, 81,146, 84,148, 86,141, 79,133, 74,133, 73,131, 73,125, 76,119, 79,114, 77,110, 74,110, 76,101, 55,95, 52,104, 25,97, 21,96" href="javascript:goMap('<%= segmentId %>','14532')">
<area shape="poly" alt="Northwest" coords="178,36, 60,15, 53,23, 45,18, 42,18, 42,23, 41,30, 41,37, 39,41, 38,45, 22,74, 21,83, 23,85, 26,87, 23,93, 26,98, 31,96, 32,99, 50,103, 54,94, 118,108, 121,103, 122,105, 124,108, 128,110, 129,95, 135,96, 137,95, 138,94, 140,88, 147,92, 154,88, 165,90, 164,86, 160,82, 161,79, 191,82,194,56, 191,56, 188,54, 184,52, 184,48, 181,48, 181,43" href="javascript:goMap('<%= segmentId %>','14531')">

</map>

  </body>
</html>
