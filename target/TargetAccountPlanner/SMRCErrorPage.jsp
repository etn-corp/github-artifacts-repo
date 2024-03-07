<%@ page language="java" import="java.io.*,java.text.*,java.util.*,com.etn.cutlerhammer.VistaMessageService.Data.*,com.etn.cutlerhammer.VistaMessageService.Data.Format.*" %>
<%@ page import="com.eaton.electrical.smrc.bo.*"%>
<%@ page isErrorPage="true" %>
<%@ page import ="java.util.ResourceBundle" %>

<%!
    private String getStackTraceString( Throwable anException )   {
        try {
            StringWriter stringwriter = new StringWriter();
            PrintWriter printwriter = new PrintWriter( stringwriter );
            anException.printStackTrace( printwriter );
            return stringwriter.toString();
        } catch ( Exception e ) {
            return "Error converting Stack Trace!";
        }
    } //method

%>

<%
	String servletException = "";
	if ( session.getAttribute( "servletException" ) != null ) {
		// If this is displaying general Servlet errors, they will be passed in as a String
		servletException = (String)session.getAttribute( "servletException" );
		session.removeAttribute( "servletException" );
	} else {
		// If this is displaying general Servlet errors, they will be passed in as a String
		servletException = (String)request.getAttribute( "exception" );
	}

	ErrorBean errorBean = null;
	if ( session.getAttribute( "errorBean" ) != null ) {
		// If this is displaying TAPError errors, they will be passed as a ErrorBean
		errorBean = ( ErrorBean ) session.getAttribute( "errorBean" );
		session.removeAttribute( "errorBean" );
	} else {
		// If this is displaying TAPError errors, they will be passed as a ErrorBean
		errorBean = ( ErrorBean ) request.getAttribute( "error" );
	}

	Throwable myException = null;
	if ( session.getAttribute( "myException" ) != null ) {
		myException = ( Throwable ) session.getAttribute( "myException" );
		session.removeAttribute( "myException" );
	} else {
		myException = exception;
	}

	// If this is displaying Message Service errors, they will be passed in as a VMS errors
	Collection errorCollection = null;
	errorCollection = (Collection) request.getAttribute( "messageservice.level2.error" );
	// IS IT WORTHWHILE TO HAVE THESE HERE???
	// We would need to somehow get them into and out of session...
	String region = null;
	String subSystemThreeChar = null;
	String subSystemCaps = null;

	// General information for all errors
	String userName = null;
	String serverName = java.net.InetAddress.getLocalHost().getHostName();

	// Format the current time.
	Calendar rightNow = Calendar.getInstance();
	SimpleDateFormat formatter = new SimpleDateFormat( "EEEEEE, MMMMMMMMM d, yyyy" );
	formatter.setCalendar( rightNow );
	Date currentTime_1 = new Date();
	String theDate = formatter.format( currentTime_1 );
	formatter.applyPattern( "h:mm:ss a z" );
	String theTime = formatter.format( currentTime_1 );

	SMRCHeaderBean header =  new SMRCHeaderBean();
	String sImagesURL = (String)header.getImagesURL();
	String cssURL = (String)header.getCssURL();
	String jsURL = (String)header.getJsURL();
	
	ResourceBundle rb = ResourceBundle.getBundle("com.eaton.electrical.smrc.SMRC");
%>

<html>
<%
	// Clear the window if the response was written
	if ( response.isCommitted() ) {
		// Store the error information in session and refresh the page
		if ( servletException != null ) {
			session.setAttribute( "servletException", servletException );
		}

		if ( errorBean != null ) {
			session.setAttribute( "errorBean", errorBean );
		}

		if ( myException != null ) {
			session.setAttribute( "myException", myException );
		}
%>
<SCRIPT LANGUAGE="JavaScript">
    document.write("");
    document.close();
    window.location="SMRCErrorPage.jsp";
</script>
<%

	}
%>
<head><title>Target Account Planner</title>
<link rel=stylesheet type="text/css" href="<%= cssURL %>style_2_1.css">
<link rel=stylesheet type="text/css" href="<%= cssURL %>oem-styles.css">
<SCRIPT LANGUAGE="JavaScript" SRC="<%= jsURL %>prototypeScript.js"></script>
<script language='javascript' src='<%= jsURL %>scripts.js'></script>

<script language="javascript">
    function showStackTrace() {
        if (document.selecter1.hStackTrace) {
            alert(document.selecter1.hStackTrace.value);
        }
    }

    function doBackOrClose() {
        browserName = navigator.appName;
        //browserVersion = parseInt(navigator.appVersion);

        if ( browserName == "Netscape" ) {
            if (history.length <=1){
                window.close();
            } else {
                history.back();
            }
        } else {
            if (history.length == 0){
                window.close();
            } else {
                history.back();
            }
        }
    }
</script>

</head>

<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="152"><img src="<%= sImagesURL %>header/r1_image.gif" width="152" height="40"></td>
    <td width="336"><img src="<%= sImagesURL %>header/r1_logo_title.gif" width="336" height="40"></td>
    <td width="99%" bgcolor="#CEBC87">&nbsp;
    </td>
    <td width="79"><img src="<%= sImagesURL %>header/r1_intranet_logo.gif" width="79" height="40"></td>
  </tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="152"><img src="<%= sImagesURL %>header/r2_image.gif" width="152" height="14"></td>
    <td width="341"><img src="<%= sImagesURL %>header/r2_spacer.gif" width="336" height="14"></td>
    <td width="99%" bgcolor="#CEBC87"><font size="1">&nbsp;</font></td>
    <td width="32"><a href="SMRCHome" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('home','','<%= sImagesURL %>header/r2_home_hot.gif',1)"><img src="<%= sImagesURL %>header/r2_home_off.gif" name="home" width="32" height="14" border="0"></a></td>
	<td width="32"><a href=<%=rb.getString("helpURL")%> target="_blank" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('help','','<%= sImagesURL %>header/r2_help_hot.gif',1)"><img src="<%= sImagesURL %>header/r2_help_off.gif" name="help" width="32" height="14" border="0"></a></td>
    <td width="69"><a href="javascript: openPopup('ConnectRequestIt','requestIt',700,800)" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('suggestions','','<%= sImagesURL %>header/r2_suggestions_hot.gif',1)"><img src="<%= sImagesURL %>header/r2_suggestions_off.gif" name="suggestions" width="69" height="14" border="0"></a></td>
    <td width="41"><a href="mailto:oemaccountplanner@eaton.com" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('email','','<%= sImagesURL %>header/r2_email_hot.gif',1)"><img src="<%= sImagesURL %>header/r2_email_off.gif" name="email" width="41" height="14" border="0"></a></td>
    <td width="44"><a href="javascript: close()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('logout','','<%= sImagesURL %>header/r2_log_out_hot.gif',1)"><img src="<%= sImagesURL %>header/r2_log_out_off.gif" name="logout" width="44" height="14" border="0"></a></td>
 </tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="152"><img src="<%= sImagesURL %>header/r3_image.gif" width="152" height="26"></td>
    <td width="1"><img src="<%= sImagesURL %>header/r3_tab_spacer.gif" width="1" height="26"></td>
	<td width="99%" bgcolor="#71674A">&nbsp;</td>
  </tr>
</table>

<table width="760" border="0" cellspacing="0" cellpadding="0">
	<tr> 
	    <td width="10">&nbsp;</td>
    	<td width="750"> 
      		<p><br>
        		<a class="crumb" href="SMRCHome">Home</a>
				<span class="crumbarrow">&gt;</span>
				<span class="crumbcurrent">Error</span>
      		</p> 
    	</td>
  	</tr>
</table>
<br>
<table width="760" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="10">&nbsp;</td>
		<td width="750">
		<table width="750" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td colspan="3"><span class="heading2">ERROR</span></td>
			</tr>
		</table>

		<br>

		<form name="selecter1"><!--Second Table Here--> 
<%
	// Message Service Errors display here
	if ( errorCollection != null ) {
%>
		<table width="760" border="0" cellspacing="0" cellpadding="1" bgcolor="#71674A">
			<tr>
				<td width="10">&nbsp;</td>
				<td width="750">
				<table width="750" border="0" cellspacing="0" cellpadding="0">
					<tr bgcolor=""#71674A"">
						<td width="200" align="right"><span class="tablewhitebold">Target Account Planner Error Catcher:&nbsp;</span></td>
						<td width="550"><font>&nbsp;</font></td>
					</tr>
					<tr>
						<td width="750" colspan="2">
						<table width="750" border="0" cellspacing="0" cellpadding="0"
							bgcolor="#FFFFFF">
							<tr>
								<td nowrap><font class="tbb">Errors found in processing</font></td>
								<td>&nbsp;</td>
							</tr>
<%
		Record record = null;
		Field field = null;
		Iterator errorCollectionIterator = null;
		Iterator fieldCollectionIterator = null;

		errorCollectionIterator = errorCollection.iterator();
		while (errorCollectionIterator.hasNext()) {
			record = (Record) errorCollectionIterator.next();
			fieldCollectionIterator = record.getFields().iterator();
			while (fieldCollectionIterator.hasNext()) {
				field = (Field) fieldCollectionIterator.next();
%>
							<tr>
								<td nowrap valign="top"><font class="tg">&nbsp;<%=field.getName()%></font>
								</td>
								<td><font class="tg">&nbsp;<%=field.getValue().trim()%></font></td>
							</tr>
<%
			} // while
		} // while
	} else {
%>
			An error has occurred.  Please check your Internet connection. The server may currently be restarting to correct an issue or may have stopped responding. Please try again.
			<p>If you continue to have problems, please contact Proprietary Application Support at 1-800-468-1705 Option 3, 
			or via e-mail at <a href="mailto:PASHelpDesk@Eaton.com?Subject=Target Account Planner Issue">HelpDesk, Proprietary Applications</a>.</p>  
			<p>Please include the following information in your correspondence to Proprietary Application Support.</p>
<%
	}
%>
							<tr>
								<td nowrap>&nbsp;</td>
								<td>&nbsp;</td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>

		<br>

		<table width="760" border="0" cellspacing="0" cellpadding="1" bgcolor="#FFFFFF">
			<tr>
				<td width="10">&nbsp;</td>
				<td width="750">
					<table width="750" border="0" cellspacing="0" cellpadding="1" bgcolor="#71674A">
						<tr>
							<td width="750">
								<table width="750" border="0" cellspacing="0" cellpadding="0">
									<tr bgcolor="#71674A">
										<td width="200" align="left"><span class="textwhitebold">Error Details:&nbsp;</span></td>
										<td width="550"><font>&nbsp;</font></td>
									</tr>
									<tr>
										<td width="750" colspan="2">
											<table width="750" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
<%
	// If a stack trace was passed in, display it
	Object stackTrace = request.getAttribute( "messageservice.error.debugstacktrace" );
	if ( stackTrace != null ) {
%>
												<input type=hidden name=hStackTrace value="<%=HtmlFormatter.formatDisplayStringHtmlView((String)stackTrace)%>">
												<tr>
													<td nowrap width="100"><font class="tg">DEBUG:</font></td>
													<td nowrap><A href="#" onClick="showStackTrace(); return false">Stack Trace</A></td>
												</tr>
<%
	} 

	if (servletException!=null) {
%>
												<tr>
													<td nowrap width="100"><font class="tg">Error Text:</font></td>
													<td nowrap><%=servletException%></td>
												</tr>

<%
 	}

	if (myException != null) {
		stackTrace = getStackTraceString( myException );
%>
												<tr>
													<td nowrap width="100"><font class="tg">Exception Text:</font></td>
													<td nowrap><%=myException%></td>
												</tr>
												<input type=hidden name=hStackTrace value="<%=HtmlFormatter.formatDisplayStringHtmlView((String)stackTrace)%>">
												<tr>
													<td nowrap width="100"><font class="tg">Stack Trace:</font></td>
													<td nowrap><A href="#" onClick="showStackTrace(); return false">Stack Trace</A></td>
												</tr>
<%
	}

	if ( errorBean != null ) {
%>
												<tr>
													<td nowrap width="100"><font class="tg">Error Text:</font></td>
													<td nowrap><%= errorBean.getErrorMessage() %></td>
												</tr>
<%
	}
%>
												<tr>
													<td nowrap width="100" colspan="2">&nbsp;</td>
												</tr>
<%
	if (userName != null) {
%>
												<tr>
													<td nowrap width="100"><font class="tg">USER:</font></td>
													<td nowrap><%=userName%></td>
												</tr>
<%
	}

	if (region != null) {
%>
												<tr>
													<td nowrap width="100"><font class="tg">DB:</font></td>
													<td nowrap><%=region%></td>
												</tr>
<%
	}

	if (request.getAttribute("theRequestingPage") != null) {
%>
												<tr>
													<td nowrap width="100"><font class="tg">URL:</font></td>
													<td nowrap><%= (String) request.getAttribute("theRequestingPage")%></td>
												</tr>
<%
	} else if ( request.getHeader("referer") != null ) {
%>
												<tr>
													<td nowrap width="100"><font class="tg">URL:</font></td>
													<td nowrap><%= request.getHeader("referer") %></td>
												</tr>
<%
	} else {
%>
												<tr>
													<td nowrap width="100"><font class="tg">URL:</font></td>
													<td nowrap><%= request.getRequestURL().toString() %></td>
												</tr>
<%
	}
%>
												<tr>
													<td nowrap width="100" valign="top"><font class="tg">SERVER:</font></td>
													<td nowrap><%=serverName%></td>
												</tr>
												<tr>
													<td nowrap width="100"><font class="tg">DATE:</font></td>
													<td nowrap><%=theDate%></td>
												</tr>
												<tr>
													<td nowrap width="100"><font class="tg">TIME:</font></td>
													<td nowrap><%=theTime%></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>				
		</table>
		</form>
		</td>
	</tr>
</table>

</body>
</html>