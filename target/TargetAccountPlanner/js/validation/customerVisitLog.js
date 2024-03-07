function formValidation() {
	var errorMessage = '';
	
	/* required fields */
	if (isEmpty(document.theform.VISIT_DESCRIPTION.value)) {
		errorMessage += '"General Description" cannot be an empty value.\n';
	}
	if (document.theform.VISIT_REASON_TYPE_ID.selectedIndex == 0) {
		errorMessage += '"Reason for Visit (Drop Down)" must be selected.\n';
	}
	if (document.theform.VISIT_OUTCOME_TYPE_ID.selectedIndex == 0) {
		errorMessage += '"Outcome of Visit (Drop Down)" must be selected.\n';
	}
	
	if (isEmpty(document.theform.VISIT_DATE_MON.value) || isEmpty(document.theform.VISIT_DATE_DAY.value) || isEmpty(document.theform.VISIT_DATE_YEAR.value)) {
		errorMessage += '"Date of Visit" cannot be an empty value.\n';
	}else{
		if (!isInteger(document.theform.VISIT_DATE_MON.value) || !isInteger(document.theform.VISIT_DATE_DAY.value)){
			errorMessage += '"Date of Visit" must be in the format MM/DD/YYYY.\n';
		}
		if (!isInteger(document.theform.VISIT_DATE_YEAR.value) || document.theform.VISIT_DATE_YEAR.value.length!=4){
			errorMessage += '"Date of Visit" Year must be in the format YYYY.\n';
		}
	}
	
	if (!isEmpty(document.theform.NEXT_VISIT_DATE_YEAR.value)) {
		if (!isInteger(document.theform.NEXT_VISIT_DATE_YEAR.value) || document.theform.NEXT_VISIT_DATE_YEAR.value.length!=4) {
			errorMessage += '"Date of Next Visit" Year must be in the format YYYY.\n';
		}
	}
	
	if (!isEmpty(document.theform.NEXT_VISIT_DATE_MON.value) || !isEmpty(document.theform.NEXT_VISIT_DATE_DAY.value) || !isEmpty(document.theform.NEXT_VISIT_DATE_YEAR.value)) {
		if (!isInteger(document.theform.NEXT_VISIT_DATE_MON.value) || !isInteger(document.theform.NEXT_VISIT_DATE_DAY.value)){
			errorMessage += '"Date of Next Visit" must be in the format MM/DD/YYYY.\n';
		}
	}
	
	
	if (document.theform.VISIT_NOTES.value.length>4000) {
		errorMessage += '"Notes" cannot be longer than 4000 characters.\n';
	}
	
	if (!isEmpty(errorMessage)) {
		alert(errorMessage);
		return false;
	}
	else {
		return true;
	}
}
