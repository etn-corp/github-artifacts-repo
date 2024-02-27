function formValidation() {
	var errorMessage = '';
	
	if (isEmpty(document.theform.FINANCIAL_INFORMATION.value)) {
		errorMessage += '"Distributors Bank Affiliation" cannot be empty.\n';
	}
	else if (document.theform.FINANCIAL_INFORMATION.value.length>4000) {
		errorMessage += '"Distributors Bank Affiliation" cannot be longer than 4000 characters.\n';
	}

	if (isEmpty(document.theform.TRADE_REFERENCE1.value) ||
		isEmpty(document.theform.TRADE_REFERENCE1_PHONE.value)) {
		errorMessage += 'At least One Trade Reference Name and Phone Number must be entered.\n';
	}

	if (!isEmpty(document.theform.TRADE_REFERENCE1_PHONE.value) &&
		!isValidUSPhoneNumber(document.theform.TRADE_REFERENCE1_PHONE.value)) {
		errorMessage += 'The first Trade Reference Phone Number is invalid.\n';
	}
	if (!isEmpty(document.theform.TRADE_REFERENCE2_PHONE.value) &&
		!isValidUSPhoneNumber(document.theform.TRADE_REFERENCE2_PHONE.value)) {
		errorMessage += 'The second Trade Reference Phone Number is invalid.\n';
	}
	if (!isEmpty(document.theform.TRADE_REFERENCE3_PHONE.value) &&
		!isValidUSPhoneNumber(document.theform.TRADE_REFERENCE3_PHONE.value)) {
		errorMessage += 'The third Trade Reference Phone Number is invalid.\n';
	}

	if (!isEmpty(document.theform.TRADE_REFERENCE1_FAX.value) &&
		!isValidUSPhoneNumber(document.theform.TRADE_REFERENCE1_FAX.value)) {
		errorMessage += 'The first Trade Reference Fax Number is invalid.\n';
	}
	if (!isEmpty(document.theform.TRADE_REFERENCE2_FAX.value) &&
		!isValidUSPhoneNumber(document.theform.TRADE_REFERENCE2_FAX.value)) {
		errorMessage += 'The second Trade Reference Fax Number is invalid.\n';
	}
	if (!isEmpty(document.theform.TRADE_REFERENCE3_FAX.value) &&
		!isValidUSPhoneNumber(document.theform.TRADE_REFERENCE3_FAX.value)) {
		errorMessage += 'The third Trade Reference Fax Number is invalid.\n';
	}

	/* optional fields which require validation */
	if (document.theform.ADDITIONAL_SALES_INFORMATION.value.length>4000) {
		errorMessage += '"Additional Sales Information (If Applicable)" cannot be longer than 4000 characters.\n';
	}

	if (!isEmpty(errorMessage)) {
		alert(errorMessage);
		return false;
	}
	else {
		return true;
	}
}


