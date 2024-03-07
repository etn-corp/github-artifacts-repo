function formValidation() {
	var errorMessage = '';
	
	/* required fields */
	if (isEmpty(document.theform.PROPOSED_TERMINATION_MON.value) ||
		isEmpty(document.theform.PROPOSED_TERMINATION_DAY.value) ||
		isEmpty(document.theform.PROPOSED_TERMINATION_YEAR.value)) {
		errorMessage += '"Proposed effective date of termination" cannot contain any blank values.\n';
	}
	else if (!isDate(document.theform.PROPOSED_TERMINATION_YEAR.value,
						document.theform.PROPOSED_TERMINATION_MON.value,
						document.theform.PROPOSED_TERMINATION_DAY.value)) {
		errorMessage += '"Proposed effective date of termination" contains an invalid date.\n';
	}

	if (isEmpty(document.theform.EXPLANATION.value)) {
		errorMessage += '"Explain" cannot be empty.\n';
	}
	else if (document.theform.EXPLANATION.value.length > 4000) {
		errorMessage += '"Explain" cannot be longer than 4000 characters.\n';
	}

	if (isEmpty(document.theform.ACTION_NOTES.value)) {
		errorMessage += '"Describe actions taken by district manager to inform distributor of decision to terminate if it is an Eaton Electrical decision" cannot be empty.\n';
	}
	else if (document.theform.ACTION_NOTES.value.length > 4000) {
		errorMessage += '"Describe actions taken by district manager to inform distributor of decision to terminate if it is an Eaton Electrical decision" cannot be longer than 4000 characters.\n';
	}

	if (isEmpty(document.theform.EST_INV_STDDE.value)) {
		errorMessage += '"Estimated Inventory Std DE" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.EST_INV_STDDE.value)) {
		errorMessage += '"Estimated Inventory Std DE" contains an invalid value.\n';
	}
	if (isEmpty(document.theform.EST_INV_PDCD.value)) {
		errorMessage += '"Estimated Inventory PDCD" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.EST_INV_PDCD.value)) {
		errorMessage += '"Estimated Inventory PDCD" contains an invalid value.\n';
	}
	if (isEmpty(document.theform.EST_INV_STDCTL.value)) {
		errorMessage += '"Estimated Inventory Std Control" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.EST_INV_STDCTL.value)) {
		errorMessage += '"Estimated Inventory Std Control" contains an invalid value.\n';
	}

	if (isEmpty(document.theform.POT_RET_STDDE.value)) {
		errorMessage += '"Potential Return Std DE" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.POT_RET_STDDE.value)) {
		errorMessage += '"Potential Return Std DE" contains an invalid value.\n';
	}
	if (isEmpty(document.theform.POT_RET_PDCD.value)) {
		errorMessage += '"Potential Return PDCD" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.POT_RET_PDCD.value)) {
		errorMessage += '"Potential Return PDCD" contains an invalid value.\n';
	}
	if (isEmpty(document.theform.POT_RET_STDCTL.value)) {
		errorMessage += '"Potential Return Std Control" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.POT_RET_STDCTL.value)) {
		errorMessage += '"Potential Return Std Control" contains an invalid value.\n';
	}

	if (isEmpty(document.theform.TERMINATION_EFFECTIVE_MON.value) ||
		isEmpty(document.theform.TERMINATION_EFFECTIVE_DAY.value) ||
		isEmpty(document.theform.TERMINATION_EFFECTIVE_YEAR.value)) {
		errorMessage += '"Termination request approved as of" cannot contain any blank values.\n';
	}
	else if (!isDate(document.theform.TERMINATION_EFFECTIVE_YEAR.value,
						document.theform.TERMINATION_EFFECTIVE_MON.value,
						document.theform.TERMINATION_EFFECTIVE_DAY.value)) {
		errorMessage += '"Termination request approved as of" contains an invalid date.\n';
	}

	if (!isEmpty(errorMessage)) {
		alert(errorMessage);
		return false;
	}
	else {
		return true;
	}
}

