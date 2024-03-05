function formValidation() {
	var errorMessage = '';
	
	if (document.theform.LOCATION_TYPE_ID.selectedIndex == 0) {
		errorMessage += '"Location Type (Drop Down)" must choose an option.\n';
	}


	if (isEmpty(document.theform.CHAIN_NAME.value)) {
		errorMessage += '"Name of Chain" cannot be empty.\n';
	}

	if (isEmpty(document.theform.NUM_OF_BRANCH_LOCATIONS.value)) {
		errorMessage += '"Number of Branches" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.NUM_OF_BRANCH_LOCATIONS.value)) {
		errorMessage += '"Number of Branches" contains an invalid value.\n';
	}
	
	if (isEmpty(document.theform.NUM_OF_YEARS_AT_LOCATION.value)) {
		errorMessage += '"Years at this location" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.NUM_OF_YEARS_AT_LOCATION.value)) {
		errorMessage += '"Years at this location" contains an invalid value.\n';
	}
	
	if (isEmpty(document.theform.FACILITY_AREA.value)) {
		errorMessage += '"Approximate total square footage at this facility" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.FACILITY_AREA.value)) {
		errorMessage += '"Approximate total square footage at this facility" contains an invalid value.\n';
	}
	
	if (!isEmpty(errorMessage)) {
		alert(errorMessage);
		return false;
	}
	else {
		return true;
	}
}


