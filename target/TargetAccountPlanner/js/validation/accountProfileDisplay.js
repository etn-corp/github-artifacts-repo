function formValidation(isProspect) {
	
	checkZip();
	
	var errorMessage = '';
	
	/* required fields */
	
	// dont allow the following "bad" characters
	var checkBad = "&*\"@#$%^{}[]\\|;<>?/~!+_=";
	var checkStr = document.theform.CUSTOMER_NAME.value;
	var allValid = true;
	var nonSpaceFound = false;
	var foundBad = "";
	// if (isProspect){
		for (i = 0;  i < checkStr.length;  i++){
			ch = checkStr.charAt(i);
			if(ch!=' '){
				nonSpaceFound=true;
			}
			for (j = 0;  j < checkBad.length;  j++){
				if (ch == checkBad.charAt(j)){
					foundBad += " " + ch;
					allValid = false;
					break;
				}
	
			}
		}
	
		if (!nonSpaceFound || isEmpty(document.theform.CUSTOMER_NAME.value)){
			errorMessage += '"Customer Name" cannot be an empty value.\n';
		}
		
		
		if(isNaN(document.theform.VISTA_CUSTOMER_NUMBER.value) || isEmpty(document.theform.VISTA_CUSTOMER_NUMBER.value)) {
		if(document.theform.DIRECT_FLAG.checked){
				if(isEmpty(document.theform.APCONTACT.value))
					errorMessage += 'AP Contact is missing.\n';

				if (isEmpty(document.theform.APCONTACT_PHONE_NUMBER.value))
					errorMessage += 'AP Phone Number is missing.\n';

				if(isEmpty(document.theform.APCONTACT_EMAIL_ADDRESS.value))
					errorMessage += 'AP Email Address is missing.\n';
			
			}
		else
			if(document.theform.DIST_FLAG.value=="Y"){
				
				if(isEmpty(document.theform.APCONTACT.value))
					errorMessage += 'AP Contact is missing.\n';

				if (isEmpty(document.theform.APCONTACT_PHONE_NUMBER.value))
					errorMessage += 'AP Phone Number is missing.\n';

				if(isEmpty(document.theform.APCONTACT_EMAIL_ADDRESS.value))
					errorMessage += 'AP Email Address is missing.\n';
			}
			}
				
		
		
		if (!allValid && isProspect){
			errorMessage += '"Customer Name" has invalid characters.\ninvalid: ' + foundBad;
		}
    // }
    
    if (isEmpty(document.theform.SALES_ENGINEER1.value)){
    	errorMessage += 'A Primary Sales Engineer must be selected.\n';
    }
    
    if ((!document.theform.SEGMENTS) || (document.theform.SEGMENTS.length < 2)){
    	errorMessage += 'A Primary and Secondary Segment must be selected.\n';
    }
    
    if (isEmpty(document.theform.ADDRESS_LINE1.value) && isProspect){
    	errorMessage += 'Business Address Line 1 is missing.\n';
    }
    
    if (isEmpty(document.theform.CITY.value) && isProspect){
    	errorMessage += 'Business Address City is missing.\n';
    }
    
	/* optional fields which require validation */
	if (!isEmpty(document.theform.NUM_OF_VALUE.value)) {
		if (!isInteger(document.theform.NUM_OF_VALUE.value)) {
			errorMessage += '"Number of Employees" must be a valid integer.\n';
		}
	}

	if (!isEmpty(document.theform.PHONE_NUMBER.value)) {
		if (!isValidUSPhoneNumber(document.theform.PHONE_NUMBER.value)) {
			errorMessage += '"Phone" must be a valid phone number.\n';
		}
	} else {
		if (isProspect){
		    errorMessage += 'A phone number must be entered.\n';
		}
	}
	
	if (!isEmpty(document.theform.FAX_NUMBER.value)) {
		if (!isValidUSPhoneNumber(document.theform.FAX_NUMBER.value)) {
			errorMessage += '"FAX NUMBER" must be a valid phone number.\n';
		}
	} else {
		if (isProspect){
		    errorMessage += 'A fax number must be entered.\n';
		}
	}
	
	if (!isEmpty(document.theform.APCONTACT_PHONE_NUMBER.value)) {
		if (!isValidUSPhoneNumber(document.theform.APCONTACT_PHONE_NUMBER.value)) {
			errorMessage += '"AP Contact NUMBER" must be a valid phone number.\n';
		}
	}
	
	if (!isEmpty(document.theform.APCONTACT_EMAIL_ADDRESS.value)) {
		if (!goodEmailFormat(document.theform.APCONTACT_EMAIL_ADDRESS.value)) {
			errorMessage += '"AP Email Address" is mal-formed. Please use the format name@eaton.com\n';
			error = true;
		}
	}
	
	document.theform.PHONE_NUMBER.value=stripCharsInBag(document.theform.PHONE_NUMBER.value, phoneNumberDelimiters);
	
	document.theform.APCONTACT_PHONE_NUMBER.value=stripCharsInBag(document.theform.APCONTACT_PHONE_NUMBER.value, phoneNumberDelimiters);
	
	
	if (!isEmpty(document.theform.FAX_NUMBER.value) && isProspect) {
		if (!isValidUSPhoneNumber(document.theform.FAX_NUMBER.value)) {
			errorMessage += '"Fax" must be a valid phone number.\n';
		}
	}
	document.theform.FAX_NUMBER.value=stripCharsInBag(document.theform.FAX_NUMBER.value, phoneNumberDelimiters)
	

	if (document.theform.BACKGROUND_INFORMATION.value.length > 4000) {
		errorMessage += '"Background Information" cannot be longer than 4000 characters.\n';
	}

	if (!isEmpty(document.theform.DPC_NUM.value) && isProspect) {
		if (document.theform.DPC_NUM.value.length!=9) {
			errorMessage += '"DPC-Num" must be exactly 9 characters.\n';
		}
	}

	if (!isEmpty(document.theform.STORE_NUM.value) && isProspect) {
		if (!isInteger(document.theform.STORE_NUM.value) || document.theform.STORE_NUM.value.length!=4) {
			errorMessage += '"Store Number" must be exactly 4 numbers.\n';
		}
	}
	
	if (!isEmpty(document.theform.GENESIS_NUMBER.value) && isProspect) {
		if (!isInteger(document.theform.GENESIS_NUMBER.value) || document.theform.GENESIS_NUMBER.value.length!=7) {
			errorMessage += '"Genesis Number" must be exactly 7 numbers.\n';
		}
	}

	if (!isEmpty(errorMessage)) {
		alert(errorMessage);
		return false;
	}
	else {
		return true;
	}
}

function contactValidation() {
	var errorMessage = '';
	
	/* required fields */
	if (isEmpty(document.theform.FIRST_NAME.value)) {
		errorMessage += '"First Name" cannot be an empty value.\n';
	}

	if (isEmpty(document.theform.LAST_NAME.value)) {
		errorMessage += '"Last Name" cannot be an empty value.\n';
	}
	
	if(isEmpty(document.theform.JOB_TITLE.value)) {
		errorMessage += '"Job Title" cannot be an empty value.\n';
	}

	if (isEmpty(document.theform.EMAIL.value)) {
		errorMessage += '"Email Address" cannot be an empty value.\n';
	}else{
		if (!goodEmailFormat(document.theform.EMAIL.value)) {
			errorMessage += '"Email Address" is mal-formed. Please use the format name@eaton.com\n';
			error = true;
		}
	}
	
	
	if (isEmpty(document.theform.PHONE.value)) {
		errorMessage += '"Phone Number" cannot be an empty value.\n';
	}else{
		if (!isValidUSPhoneNumber(document.theform.PHONE.value)) {
			errorMessage += '"Phone Number" must be a valid phone number.\n';
		}
	}
	
	document.theform.PHONE.value=stripCharsInBag(document.theform.PHONE.value, phoneNumberDelimiters)
	
	if (!isEmpty(document.theform.FAX.value)) {
		if (!isValidUSPhoneNumber(document.theform.FAX.value)) {
			errorMessage += '"Fax Number" must be a valid phone number.\n';
		}
	}
	document.theform.FAX.value=stripCharsInBag(document.theform.FAX.value, phoneNumberDelimiters)
	


	if (!isEmpty(errorMessage)) {
		alert(errorMessage);
		return false;
	}
	else {
		return true;
	}
}

function checkZip() {
	if(document.theform.ZIP.value == "") {
		alert("This account's district is now going to be matched with the provided business address zip code."); 
	}
}

function newWindow(url, windowName) {
	catWindow = window.open(url, windowName, config='width=650,height=475,location=no,scrollbars=yes,toolbar=no,resizeable=yes,status=1')
}

var setInnerHTML = function( id, str ){
	if(!document.getElementById) return; // Not Supported
	if(document.getElementById){
		document.getElementById(id).innerHTML = str;
	}
}

function clickBillTo(){

	document.theform.BILLTO_ADDRESS_LINE1.value=document.theform.ADDRESS_LINE1.value;
	document.theform.BILLTO_ADDRESS_LINE2.value=document.theform.ADDRESS_LINE2.value;
	document.theform.BILLTO_ADDRESS_LINE3.value=document.theform.ADDRESS_LINE3.value;
	document.theform.BILLTO_ADDRESS_LINE4.value=document.theform.ADDRESS_LINE4.value;
	document.theform.BILLTO_CITY.value=document.theform.CITY.value;
	document.theform.BILLTO_STATE.value=document.theform.STATE.value;
	document.theform.BILLTO_ZIP.value=document.theform.ZIP.value;
	document.theform.BILLTO_COUNTRY.value=document.theform.COUNTRY.value;
	setInnerHTML('BILLTO_CITY_DIV',document.theform.CITY.value);
	setInnerHTML('BILLTO_STATE_DIV',document.theform.STATE.value);
	setInnerHTML('BILLTO_ZIP_DIV',document.theform.ZIP.value);
	changeBillToCountry("true");

}
function clickShip(){

	document.theform.SHIP_ADDRESS_LINE1.value=document.theform.ADDRESS_LINE1.value;
	document.theform.SHIP_ADDRESS_LINE2.value=document.theform.ADDRESS_LINE2.value;
	document.theform.SHIP_ADDRESS_LINE3.value=document.theform.ADDRESS_LINE3.value;
	document.theform.SHIP_ADDRESS_LINE4.value=document.theform.ADDRESS_LINE4.value;
	document.theform.SHIP_CITY.value=document.theform.CITY.value;
	document.theform.SHIP_STATE.value=document.theform.STATE.value;
	document.theform.SHIP_ZIP.value=document.theform.ZIP.value;
	document.theform.SHIP_COUNTRY.value=document.theform.COUNTRY.value;
	setInnerHTML('SHIP_CITY_DIV',document.theform.CITY.value);
	setInnerHTML('SHIP_STATE_DIV',document.theform.STATE.value);
	setInnerHTML('SHIP_ZIP_DIV',document.theform.ZIP.value);
	changeShipCountry("true");

}
function clearValues(element,newValue){
	document.getElementById(element).value="";
	setInnerHTML(element + "_DIV",newValue);
}		