function formValidation() {
	var errorMessage = '';
	
	/* Assumes the first element in the drop down is "Select One..."
	   Assumes the first "Supplier Name" dropdown is named "VENDOR_0"
	   Assumes the first "Product" dropdown is named "PRODUCT_0" */
	if (document.theform.VENDOR_0.selectedIndex == 0 ||
		document.theform.PRODUCT_0.selectedIndex == 0) {
		errorMessage += 'At least one "Supplier Name" and "Product" must be chosen.\n';
	}

	if (isEmpty(document.theform.CURRENT_YR_SALES_EST.value)) {
		errorMessage += '"Current Year Estimate" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.CURRENT_YR_SALES_EST.value)) {
		errorMessage += '"Current Year Estimate" contains an invalid value.\n';
	}
	
	if (isEmpty(document.theform.PRIOR_YR_SALES_ACTUAL.value)) {
		errorMessage += '"Previous Year Actual" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.PRIOR_YR_SALES_ACTUAL.value)) {
		errorMessage += '"Previous Year Actual" contains an invalid value.\n';
	}
	
	if (isEmpty(document.theform.PRIOR_2YR_SALES_ACTUAL.value)) {
		errorMessage += '"Year Before Last Actual" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.PRIOR_2YR_SALES_ACTUAL.value)) {
		errorMessage += '"Year Before Last Actual" contains an invalid value.\n';
	}
	
	if (isEmpty(document.theform.PRIOR_3YR_SALES_ACTUAL.value)) {
		errorMessage += '"Total sales of all products from this location over the last three years" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.PRIOR_3YR_SALES_ACTUAL.value)) {
		errorMessage += '"Total sales of all products from this location over the last three years" contains an invalid value.\n';
	}
	
	if (isEmpty(document.theform.APROX_INVENTORY.value)) {
		errorMessage += '"Approximate Inventory at this location" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.APROX_INVENTORY.value)) {
		errorMessage += '"Approximate Inventory at this location" contains an invalid value.\n';
	}
	
	if (isEmpty(document.theform.PROJECTED_EATON_SALES_1.value)) {
		errorMessage += '"Anticipated Eaton Sales (Year 1)" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.PROJECTED_EATON_SALES_1.value)) {
		errorMessage += '"Anticipated Eaton Sales (Year 1)" contains an invalid value.\n';
	}
	
	if (isEmpty(document.theform.PROJECTED_EATON_SALES_2.value)) {
		errorMessage += '"Anticipated Eaton Sales (Year 2)" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.PROJECTED_EATON_SALES_2.value)) {
		errorMessage += '"Anticipated Eaton Sales (Year 2)" contains an invalid value.\n';
	}
	
	/* Assumes the first element in the drop down is "Select One..." */
	if (document.theform.NAED.selectedIndex == 0) {
		errorMessage += '"Participates in NAED (Drop Down)" must choose an option.\n';
	}

	/* optional fields which require validation */
	if (document.theform.TRADE_ASSNS.value.length > 100) {
		errorMessage += '"List other trade associations: (ie, AHTD, EASA, etc...)" cannot be longer than 100 characters.\n';
	}

	if (document.theform.DISTRIBUTOR_NOTES.value.length > 4000) {
		errorMessage += '"Other notes or comments" cannot be longer than 4000 characters.\n';
	}

	if (!isEmpty(errorMessage)) {
		alert(errorMessage);
		return false;
	}
	else {
		return true;
	}
}


