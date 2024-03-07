var changed = false;

//Vince took this from Josh V's code.  We should keep this here in one place.
function newWindow(url, windowName) {
        catWindow = window.open(url, windowName, config='width=650,height=475,location=no,scrollbars=yes,toolbar=no,resize=yes,status=1')
}

function openPopup(url, name, height, width)
{
	var isNav = (navigator.appName.indexOf("Netscape") != -1);

	if (isNav)
		open(url, name, "height=" + height + ",width=" + width + ",scrollbars=yes, resizable=yes, screenx=0, screeny=0");
	else
		open(url, name, "height=" + height + ",width=" + width + ",scrollbars=yes, resizable=yes, left=0, top=0");
}

function doPAPSubmit()
{
	var isNav = (navigator.appName.indexOf("Netscape") != -1);

	if (isNav) {
		window.setTimeout(window.close, 500);
		document.forms[0].submit();
	}
	else {	// is IE
		window.setTimeout(window.close(), 500);
	}
}

function closePopup(doSubmit)
{
	this.close();
}

function validateVCN() {
	var e = document.forms[0].cust;
	
	if (isInteger(e.value)) {
		if (e.value.length == 6) {
		}
		else {
			alert("The customer number is an improper length.\nIt should be 6 numbers long.\nTry adding leading zero's (0)");
			return false;
		}
	}
	else {
		alert("The customer number should be an integer.");
		return false;
	}
	
	return true;
}

function validateMix() {
	var error = false;
	var msg = "";
	var f = document.forms[0];

	for (p=0; p < f.elements.length; p++) {
		var e = f.elements[p];
		if (e.type == "checkbox") {
			if (e.checked) {
				var id = e.name.substring(2,e.name.length);

				var pot = noComma(f.elements[p+1].value);
				var act = noComma(f.elements[p+2].value);
				var compet = noComma(f.elements[p+3].value);

				if (isNumeric(pot)) {
					if (isInteger(pot) && pot.length <= 8) {
					}
					else {
						error = true
						msg = msg + "The Potential $ for " +
							id + " is too large. (< 100,000,000)\n"
					}
				}
				else {
					error = true;
					msg = msg + "The Potential $ for " +
						id + " is not a valid number\n"
				}

				if (isNumeric(act)) {
					if (isInteger(act) && act.length <= 8) {
					}
					else {
						error = true
						msg = msg + "The Cutler-Hammer $ for " +
							id + " is too large. (< 100,000,000)\n"
					}
				}
				else {
					error = true;
					msg = msg + "The Cutler-Hammer $ for " +
						id + " is not a valid number\n"
				}

				if (isNumeric(compet)) {
					if (isInteger(compet) && compet.length <= 8) {
					}
					else {
						error = true
						msg = msg + "The Competitor $ for " +
							id + " is too large. (< 100,000,000)\n"
					}
				}
				else {
					error = true;
					msg = msg + "The Competitor $ for " +
						id + " is not a valid number\n"
				}

			}
		}
	}

	if (error) {
		alert(msg);
		return false;
	}

	return true;
}

function noComma(input) {
	var ret = "";

    for (i = 0; i < input.length; i++)
    {
        var c = input.charAt(i);
        if (c != ',') {
        	ret = ret + c;
        }
    }
    
    return ret;
}

function isNumeric(test) {
	if (isInteger(test) || isSignedInteger(test) || 
		isFloat(test) || isSignedFloat(test)) {
		return true;
	}
	else {
		return false;
	}
}

function isEmpty(s)
{   return ((s == null) || (s.length == 0))
}

function isDigit (c)
{   return (((c >= "0") && (c <= "9")))
}
	
function isPercent (s){
   if(!isInteger(s) || s>100 || s<0){
   	return false;
   }

	return true;   
}
	
		
function isInteger (s)
{   
    var i;
    if (isEmpty(s))
       if (isInteger.arguments.length == 1) return true;
       else return (isInteger.arguments[1] == true);

    for (i = 0; i < s.length; i++)
    {
        var c = s.charAt(i);
        if (!isDigit(c)) return false;
    }
	
    return true;
}
		
function isSignedInteger (s)
{   if (isEmpty(s))
       if (isSignedInteger.arguments.length == 1) return true;
       else return (isSignedInteger.arguments[1] == true);

    else {
        var startPos = 0;
        var secondArg = true;
	
        if (isSignedInteger.arguments.length > 1)
            secondArg = isSignedInteger.arguments[1];
		
        if ( (s.charAt(0) == "-") || (s.charAt(0) == "+") )
           startPos = 1;
        return (isInteger(s.substring(startPos, s.length), secondArg))
    }
}
		
function isFloat (s)
{   var i;
    var seenDecimalPoint = false;

    if (isEmpty(s))
       if (isFloat.arguments.length == 1) return true;
       else return (isFloat.arguments[1] == true);

    if (s == ".") return false;

    for (i = 0; i < s.length; i++)
    {
        // Check that current character is number.
        var c = s.charAt(i);

        if ((c == ".") && !seenDecimalPoint) seenDecimalPoint = true;
        else if (!isDigit(c)) return false;
    }

    return true;
}
		
function isSignedFloat (s)
{   if (isEmpty(s))
       if (isSignedFloat.arguments.length == 1) return true;
       else return (isSignedFloat.arguments[1] == true);

    else {
        var startPos = 0;
        var secondArg = true;

        if (isSignedFloat.arguments.length > 1)
            secondArg = isSignedFloat.arguments[1];

        // skip leading + or -
        if ( (s.charAt(0) == "-") || (s.charAt(0) == "+") )
           startPos = 1;
        return (isFloat(s.substring(startPos, s.length), secondArg))
    }
}

function clickAccountReport(f) {
	if (f.viewAcct.checked == true) {
		f.viewProd.checked = false;
		f.viewZone.checked = false;
		f.viewDistrict.checked = false;
		f.viewSE.checked = false;
                f.viewSummaryProductline.checked = false;
                f.viewPrimarySegment.checked = false;
                f.viewSecondarySegment.checked = false;
                f.viewTeam.checked = false;
                f.viewSalesOrg.checked = false;
                f.viewGroupCode.checked = false;
	}
}

function clickParentReport(f) {
        if (f.viewParents.checked == true){
		f.viewProd.checked = false;
		f.viewZone.checked = false;
		f.viewDistrict.checked = false;
		f.viewSE.checked = false;
                f.viewSummaryProductline.checked = false;
                f.viewPrimarySegment.checked = false;
                f.viewSecondarySegment.checked = false;
                f.viewTeam.checked = false;
                f.viewSalesOrg.checked = false;
                f.viewGroupCode.checked = false;
        }
}


function clickZoneReport(f) {
	if (f.viewZone.checked == true) {
		f.viewAcct.checked = false;
		f.viewParents.checked = false;
		f.viewSE.checked = false;
                f.viewSummaryProductline.checked = false;
                f.viewPrimarySegment.checked = false;
                f.viewSecondarySegment.checked = false;
                f.viewTeam.checked = false;
                f.viewSalesOrg.checked = false;
                f.viewGroupCode.checked = false;
              
	}
}

function clickDistrictReport(f) {
	if (f.viewDistrict.checked == true) {
		f.viewSE.checked = false;
		f.viewAcct.checked = false;
		f.viewParents.checked = false;
                f.viewSalesOrg.checked = false;
                f.viewGroupCode.checked = false;
	}
}

function clickProductReport(f) {
	if (f.viewProd.checked == true) {
		f.viewSE.checked = false;
		f.viewAcct.checked = false;
		f.viewParents.checked = false;
                f.viewSummaryProductline.checked = false;
                f.viewPrimarySegment.checked = false;
                f.viewSecondarySegment.checked = false;
                f.viewTeam.checked = false;
                f.viewSalesOrg.checked = false;
                f.viewGroupCode.checked = false;
                f.viewZone.checked = false;              
		
	}
}

function clickSEReport(f) {
	if (f.viewSE.checked == true) {
		f.viewParents.checked = false;
		f.viewProd.checked = false;
		f.viewZone.checked = false;
		f.viewDistrict.checked = false;
		f.viewAcct.checked = false;
                f.viewSummaryProductline.checked = false;
                f.viewPrimarySegment.checked = false;
                f.viewSecondarySegment.checked = false;
                f.viewTeam.checked = false;
                f.viewSalesOrg.checked = false;
                f.viewGroupCode.checked = false;
	}
}

function clickSummaryProductlineReport(f) {
	if (f.viewSummaryProductline.checked == true) {
		f.viewParents.checked = false;
		f.viewProd.checked = false;
		f.viewZone.checked = false;
		f.viewAcct.checked = false;
                f.viewPrimarySegment.checked = false;
                f.viewSecondarySegment.checked = false;
                f.viewTeam.checked = false;
                f.viewSalesOrg.checked = false;
                f.viewGroupCode.checked = false;
	}
}

function clickPrimarySegmentReport(f) {
	if (f.viewPrimarySegment.checked == true) {
		f.viewParents.checked = false;
		f.viewProd.checked = false;
		f.viewZone.checked = false;
		f.viewAcct.checked = false;
                f.viewSummaryProductline.checked = false;
                f.viewSecondarySegment.checked = false;
                f.viewTeam.checked = false;
                f.viewSalesOrg.checked = false;
                f.viewGroupCode.checked = false;
	}
}

function clickSecondarySegmentReport(f) {
	if (f.viewSecondarySegment.checked == true) {
		f.viewParents.checked = false;
		f.viewProd.checked = false;
		f.viewZone.checked = false;
		f.viewAcct.checked = false;
                f.viewSummaryProductline.checked = false;
                f.viewPrimarySegment.checked = false;
                f.viewTeam.checked = false;
                f.viewSalesOrg.checked = false;
                f.viewGroupCode.checked = false;
	}
}

function clickTeamReport(f) {
	if (f.viewTeam.checked == true) {
		f.viewParents.checked = false;
		f.viewProd.checked = false;
		f.viewZone.checked = false;
		f.viewAcct.checked = false;
                f.viewSummaryProductline.checked = false;
                f.viewPrimarySegment.checked = false;
                f.viewSecondarySegment.checked = false;
                f.viewSalesOrg.checked = false;
                f.viewGroupCode.checked = false;
	}
}

function clickSalesOrgReport(f) {
	if (f.viewSalesOrg.checked == true) {
		f.viewParents.checked = false;
		f.viewProd.checked = false;
		f.viewZone.checked = false;
		f.viewDistrict.checked = false;
		f.viewAcct.checked = false;
                f.viewSummaryProductline.checked = false;
                f.viewPrimarySegment.checked = false;
                f.viewSecondarySegment.checked = false;
                f.viewTeam.checked = false;
                f.viewGroupCode.checked = false;
	}
}

function clickGroupCodeReport(f) {
	if (f.viewGroupCode.checked == true) {
		f.viewParents.checked = false;
		f.viewProd.checked = false;
		f.viewZone.checked = false;
		f.viewDistrict.checked = false;
		f.viewAcct.checked = false;
                f.viewSummaryProductline.checked = false;
                f.viewPrimarySegment.checked = false;
                f.viewSecondarySegment.checked = false;
                f.viewTeam.checked = false;
                f.viewSalesOrg.checked = false;
         }
}

function clickCustomerMarketShareViewAcct(f) {

	if (f.viewAcct.checked == true) {
	
		f.viewParents.checked = false;
		f.viewDistrict.checked = false;
		f.viewTeam.checked = false;
		f.viewZone.checked = false;
		f.viewPrimarySegment.checked = false;
		f.viewSecondarySegment.checked = false;
		f.viewProd.checked = false;
		f.viewDivision.checked = false;
		f.viewSE.checked = false;

	}


}

function clickCustomerMarketShareViewParents(f) {

	if (f.viewParents.checked == true) {
	
		f.viewAcct.checked = false;
		f.viewDistrict.checked = false;
		f.viewTeam.checked = false;
		f.viewZone.checked = false;
		f.viewPrimarySegment.checked = false;
		f.viewSecondarySegment.checked = false;
		f.viewProd.checked = false;
		f.viewDivision.checked = false;
		f.viewSE.checked = false;

	}
}

function clickCustomerMarketShareViewDistrict(f) {

	if (f.viewDistrict.checked == true) {
	
		f.viewAcct.checked = false;
		f.viewParents.checked = false;
		f.viewTeam.checked = false;
		f.viewZone.checked = false;
		f.viewPrimarySegment.checked = false;
		f.viewSecondarySegment.checked = false;
		f.viewProd.checked = false;
		f.viewDivision.checked = false;
		f.viewSE.checked = false;

	}
}

function clickCustomerMarketShareViewTeam(f) {

	if (f.viewTeam.checked == true) {
	
		f.viewAcct.checked = false;
		f.viewParents.checked = false;
		f.viewDistrict.checked = false;
		f.viewZone.checked = false;
		f.viewPrimarySegment.checked = false;
		f.viewSecondarySegment.checked = false;
		f.viewProd.checked = false;
		f.viewDivision.checked = false;
		f.viewSE.checked = false;

	}
}

function clickCustomerMarketShareViewZone(f) {

	if (f.viewZone.checked == true) {
	
		f.viewAcct.checked = false;
		f.viewParents.checked = false;
		f.viewDistrict.checked = false;
		f.viewTeam.checked = false;
		f.viewPrimarySegment.checked = false;
		f.viewSecondarySegment.checked = false;
		f.viewProd.checked = false;
		f.viewDivision.checked = false;
		f.viewSE.checked = false;

	}
}

function clickCustomerMarketShareViewPrimarySegment(f) {

	if (f.viewPrimarySegment.checked == true) {
	
		f.viewAcct.checked = false;
		f.viewParents.checked = false;
		f.viewDistrict.checked = false;
		f.viewTeam.checked = false;
		f.viewZone.checked = false;
		f.viewSecondarySegment.checked = false;
		f.viewProd.checked = false;
		f.viewDivision.checked = false;
		f.viewSE.checked = false;

	}
}

function clickCustomerMarketShareViewSecondarySegment(f) {

	if (f.viewSecondarySegment.checked == true) {
	
		f.viewAcct.checked = false;
		f.viewParents.checked = false;
		f.viewDistrict.checked = false;
		f.viewTeam.checked = false;
		f.viewZone.checked = false;
		f.viewPrimarySegment.checked = false;
		f.viewProd.checked = false;
		f.viewDivision.checked = false;
		f.viewSE.checked = false;

	}
}

function clickCustomerMarketShareViewProd(f) {

	if (f.viewProd.checked == true) {
	
		f.viewAcct.checked = false;
		f.viewParents.checked = false;
		f.viewDistrict.checked = false;
		f.viewTeam.checked = false;
		f.viewZone.checked = false;
		f.viewPrimarySegment.checked = false;
		f.viewSecondarySegment.checked = false;
		f.viewDivision.checked = false;
		f.viewSE.checked = false;

	}
}

function clickCustomerMarketShareViewDivision(f) {

	if (f.viewDivision.checked == true) {
	
		f.viewAcct.checked = false;
		f.viewParents.checked = false;
		f.viewDistrict.checked = false;
		f.viewTeam.checked = false;
		f.viewZone.checked = false;
		f.viewPrimarySegment.checked = false;
		f.viewSecondarySegment.checked = false;
		f.viewProd.checked = false;
		f.viewSE.checked = false;

	}
}

function clickCustomerMarketShareViewSE(f) {

	if (f.viewSE.checked == true) {
	
		f.viewAcct.checked = false;
		f.viewParents.checked = false;
		f.viewDistrict.checked = false;
		f.viewTeam.checked = false;
		f.viewZone.checked = false;
		f.viewPrimarySegment.checked = false;
		f.viewSecondarySegment.checked = false;
		f.viewProd.checked = false;
		f.viewDivision.checked = false;

	}
}



function validateCMSReportForm(f) {

	if (f.viewAcct.checked == false &&
		f.viewParents.checked == false &&
		f.viewDistrict.checked == false &&
		f.viewTeam.checked == false &&
		f.viewZone.checked == false &&
		f.viewPrimarySegment.checked == false &&
		f.viewSecondarySegment.checked == false &&
		f.viewProd.checked == false && 
		f.viewSE.checked == false && 
		f.viewDivision.checked == false)
		
		{
			alert("You must select at least one grouping option");
			return false;		
		}

}

function validateReportForm(f) {
	if (f.viewAcct.checked == false &&
		f.viewProd.checked == false &&
		f.viewZone.checked == false &&
		f.viewDistrict.checked == false &&
		f.viewParents.checked == false &&
		f.viewSE.checked == false &&
        f.viewSummaryProductline.checked == false &&
        f.viewPrimarySegment.checked == false &&
        f.viewSecondarySegment.checked == false &&
        f.viewTeam.checked == false &&
        f.viewSalesOrg.checked == false &&
        f.viewGroupCode.checked == false) {
		
		alert("You must select at least one viewing option");
		return false;
	}
	/*else if (f.zone.selectedIndex != 0 && f.district.selectedIndex != 0) {
		if (f.zone.options[f.zone.selectedIndex].value !=
			f.district.options[f.segment.selectedIndex].value.substring(0,2)) {
			
			alert("The district " + f.district.options[f.district.selectedIndex].text + 
				" is not in zone " + f.zone.options[f.zone.selectedIndex].text);
			return false;
		}
	}*/
		
	return true;
}

function changeUserMaint () {
	document.forms[0].switchuser.value="true";
	document.forms[0].submit();
}

//function changeGroup() {
//	if (itemChanged()) {
//		document.forms[0].submit();
//	}
//}

function validateNewUser(f) {
	var msg = "The following fields have been left blank: ";
	var error = false;
	
	if (isEmpty(f.userid.value)) {
		msg = msg + "\nUser Id";
		error = true;
	}
	
	if (isEmpty(f.password.value)) {
		msg = msg + "\nPassword";
		error = true;
	}
	
	if (isEmpty(f.firstName.value)) {
		msg = msg + "\nFirst Name";
		error = true;
	}

	if (isEmpty(f.lastName.value)) {
		msg = msg + "\nLast Name";
		error = true;
	}

	if (isEmpty(f.vistaId.value)) {
		msg = msg + "\nVista Id";
		error = true;
	}

	if (isEmpty(f.email.value)) {
		msg = msg + "\nEmail Address";
		error = true;
	}
	else if (!goodEmailFormat(f.email.value)) {
		msg = msg + "\n\nYour email address is mal-formed. Please use the format name@eaton.com";
		error = true;
	}
	
	var primGroup = f.primary.options[f.primary.selectedIndex].value + "access";
	var groups = new Array();
	
	for (var i=0; i < f.primary.length; i++) {
		groups[i] = f.primary.options[i].value + "access";
	}
	
	var oneGroupSelected = false;

	for (var i=0; i < f.elements.length; i++) {
		var e = f.elements[i];
		
		if (e.name == primGroup) {
			if (!e.checked) {
				msg = msg + "\n\nYour default login planner must be one you have access to.";
				error = true;
			}
		}
		
		for (var j=0; j < groups.length; j++) {
			if (e.name == groups[j] && e.checked) {
				oneGroupSelected = true;
			}
		}
	}
	
	if (!oneGroupSelected) {
		msg = msg + "\n\nYou must select at least one planner to continue.";
		error = true;
	}

	if (error) {
		alert(msg);
		return false;
	}
	
	return true;
}
/*
function validateUserProfile(f) {
	var msg = "The following fields have been left blank: ";
	var error = false;
	
	if (isEmpty(f.firstName.value)) {
		msg = msg + "\nFirst Name";
		error = true;
	}
	
	if (isEmpty(f.lastName.value)) {
		msg = msg + "\nLast Name";
		error = true;
	}

	if (isEmpty(f.vistaId.value)) {
		msg = msg + "\nVista Id";
		error = true;
	}

	if (isEmpty(f.email.value)) {
		msg = msg + "\nEmail Address";
		error = true;
	}
	
	if (!goodEmailFormat(f.email.value)) {
		msg = msg + "\n\nYour email address is mal-formed. Please use the format name@eaton.com";
		error = true;
	}
	
	if (error) {
		alert(msg);
		return false;
	}
	
	return true;
}	
*/
function goodEmailFormat(s) {
	var haveAt = false;
	var haveDot = false;
	
    for (i = 0; i < s.length; i++)
    {
        var c = s.charAt(i);
        if (c == '@') {
        	haveAt = true;
        }
        
        if (haveAt && c == '.') {
			haveDot = true;
        }
    }
    
    if (haveAt && haveDot) {
    	return true;
    }
    
    return false
}

function validateEmail(e) {
	if (!goodEmailFormat(e.value)) {
    	alert("Your email address is mal-formed. Please use the format name@eaton.com\n" +
    		"Your email address will be used in case you lose your password so it is " +
    		"very important that you enter it correctly.");
    	e.focus();
    }
}

function updateProductValue(dolType,prod) {
	var f = document.forms[1];
	var sublines = new Array();

	for (p=0; p < f.elements.length; p++) {
		var e = f.elements[p];
		
		if (e.name.length > 7) {
			if (e.name.substr(0,5) == 'prod_') {
				var id = e.name.substr(5,2);
				
				if (id == prod) {
					var subline = e.value.substr(3,3);
					var tmp = sublines.push(subline);
				}
			}
		}
	}
	
	var newVal = 0;
	
	for (p=0; p < f.elements.length; p++) {
		var e = f.elements[p];
		var test = 'ps_' + dolType;
		var maxTest = test.length;

		if (e.name.length > maxTest) {
			if (e.name.substr(0,maxTest) ==  test) {
				for (q=0; q < sublines.length; q++) {
					var tst = test + '_' + sublines[q];
					
					if (e.name == tst) {
						if (isNumeric(noComma(e.value))) {
							var val = parseFloat(noComma(e.value));
							newVal = newVal+val;
						}
						else {
							alert("At least one subline contains non-numeric data. Please correct.");
						}
					}
				}
			}
		}
	}
	
	for (p=0; p < f.elements.length; p++) {
		var e = f.elements[p];
		
		if (e.name == dolType + "_" + prod) {
			e.value=newVal;
			break;
		}
	}
//	alert("newVal: " + newVal);
}

function modElement() {
	changed = true;
}

function itemChanged() {
	if (changed) {
		return (confirm("At least one item was changed on the screen.\nAre you sure you want to " +
				"continue without saving your changes?"));
	}
	
	return true;
}

/****************************************************************************/
/*
 * Added by Josh Davis - for client side validation
 */

var daysInMonth = new Array(0,31,29,31,30,31,30,31,31,30,31,30,31);

function isDate (year, month, day)
{

	if (! (isYear(year) && isMonth(month) && isDay(day))) {
		return false;
	}

	var intYear = parseInt(year, 10);
	var intMonth = parseInt(month, 10);
	var intDay = parseInt(day, 10);

	if (intDay > daysInMonth[intMonth]) {
		return false;
	}
	if ((intMonth == 2) && (intDay > daysInFebruary(intYear))) {
		return false;
	}

	return true;
}

function isYear(s)
{	
	if (!isInteger(s) || s <= 0) {
		return false;
	}
	
	return ((s.length == 2) || (s.length == 4));
}

function isMonth(s)
{	
	if (isInteger(s) && 1 <= s && s <= 12) {
		return true;
	}
	else {
		return false;
	}
}


function isDay(s)
{
	if (isInteger(s) && 1 <= s && s <= 31) {
		return true;
	}
	else {
		return false;
	}	
}


function daysInFebruary (year)
{
	return ( ((year % 4 == 0) && (!(year % 100 == 0) || (year % 400 == 0) ) ) ? 29 : 28 );
}

var phoneNumberDelimiters = "()-. ";
var minDigitsInPhoneNumber = 10;

function isValidUSPhoneNumber(s) {
	var normalizedPhone = stripCharsInBag(s, phoneNumberDelimiters)
	if (isInteger(normalizedPhone) && normalizedPhone.length >= minDigitsInPhoneNumber)  {
		return true;
	}
	else {
		return false;
	}
}

function stripCharsInBag(s, bag) {
	var i;
	var returnString = "";

	for (i = 0; i < s.length; i++) {
		var c = s.charAt(i);
		if (bag.indexOf(c) == -1) {
			returnString += c;
		}
	}

	return returnString;
}


function getLineCount(aTextArea, aLineLength) {

        //Note that we DO NOT get the "lineLength" from the "cols" attribute of the TextArea itself.
        //It turns out that this allows us to make the "lineLength" a multiple of the "cols" attribute.

	//gotta run through this twice for IE to work correctly.
        //It does not hurt other browsers to do this twice.
	for (i=0; i<2; i++) {
		// Trim trailing whitespace including newline chars
		RegExp.multiline = true;
		trimRe = new RegExp();
		trimRe.compile("\\s+$", "g");
		aTextArea.value = aTextArea.value.replace(trimRe, "");

		// Split the text into lines
		splitRe = /[\n\r]+/;
		fieldLines = aTextArea.value.split(splitRe);

		// Fix up lines with more than aLineLength characters.
		var tempValue = "";
		var fieldValue = "";

		for (line = 0; line < fieldLines.length; line++) {
			fieldLines[line] = trim(fieldLines[line]);
			tempValue = "";

			while(fieldLines[line].length > aLineLength) {
				tempValue = tempValue + fieldLines[line].substring(0,aLineLength) + '\n';
				fieldLines[line] = fieldLines[line].substring(aLineLength);
			}

			tempValue = tempValue +fieldLines[line];
			if (line<fieldLines.length-1) {
				tempValue = tempValue + "\n";
			}

			fieldValue = fieldValue + tempValue;

		} //for
		
		aTextArea.value = fieldValue;
	} //for

        return (fieldLines.length);

} //function

function trim(strText) {
	// this will get rid of leading spaces
	while (strText.substring(0,1) == ' ')
		strText = strText.substring(1, strText.length);

	// this will get rid of trailing spaces
	while (strText.substring(strText.length-1,strText.length) == ' ')
		strText = strText.substring(0, strText.length-1);

	return strText;
}

function removeCurrency(inString){
            var returnValue = 0;
            for (i=0; i< inString.length; i++){
                if ((inString.charAt(i) != ",") && (inString.charAt(i) != "$")){
                    returnValue += inString.charAt(i);
                }
            }
            return returnValue;
}

/****************************************************************************/

