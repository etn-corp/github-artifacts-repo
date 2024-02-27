function formValidation() {
	var errorMessage = '';
	
	if (isEmpty(document.theform.MAINTAIN_DOLLARS.value)) {
		errorMessage += '"A. Maintain" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.MAINTAIN_DOLLARS.value)) {
		errorMessage += '"A. Maintain" contains an invalid value.\n';
	}

	if (isEmpty(document.theform.GROW_DOLLARS.value)) {
		errorMessage += '"B. Grow" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.GROW_DOLLARS.value)) {
		errorMessage += '"B. Grow" contains an invalid value.\n';
	}

	if (isEmpty(document.theform.PENETRATE_DOLLARS.value)) {
		errorMessage += '"C. Penetrate" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.PENETRATE_DOLLARS.value)) {
		errorMessage += '"C. Penetrate" contains an invalid value.\n';
	}

	if (isEmpty(document.theform.ADD_DOLLARS.value)) {
		errorMessage += '"List distributor(s) to add" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.ADD_DOLLARS.value)) {
		errorMessage += '"List distributor(s) to add" contains an invalid value.\n';
	}

	if (isEmpty(document.theform.TERMINATE_DOLLARS.value)) {
		errorMessage += '"List distributor(s) to terminate" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.TERMINATE_DOLLARS.value)) {
		errorMessage += '"List distributor(s) to terminate" contains an invalid value.\n';
	}

	if (isEmpty(document.theform.RISK_DOLLARS.value)) {
		errorMessage += '"List distributor(s) at risk" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.RISK_DOLLARS.value)) {
		errorMessage += '"List distributor(s) at risk" contains an invalid value.\n';
	}

	if (isEmpty(document.theform.OTHER_CHAIN_IMPACT.value)) {
		errorMessage += '"Impact of chain(s) in other geographies" cannot be empty.\n';
	}
	else if (!isNumeric(document.theform.OTHER_CHAIN_IMPACT.value)) {
		errorMessage += '"Impact of chain(s) in other geographies" contains an invalid value.\n';
	}


	if (!isEmpty(errorMessage)) {
		alert(errorMessage);
		return false;
	}
	else {
		return true;
	}
}


