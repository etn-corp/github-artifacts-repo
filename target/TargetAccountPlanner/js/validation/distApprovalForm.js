function formValidation() {
	var errorMessage = '';
	
	if (isEmpty(document.theform.PROJECTED_EATON_SALES_YEAR1.value)) {
		errorMessage += '"Projected Total Eaton Sales (Year 1)" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.PROJECTED_EATON_SALES_YEAR1.value)) {
		errorMessage += '"Projected Total Eaton Sales (Year 1)" contains an invalid value.\n';
	}
	if (isEmpty(document.theform.PROJECTED_EATON_SALES_YEAR3.value)) {
		errorMessage += '"Projected Total Eaton Sales (Year 3)" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.PROJECTED_EATON_SALES_YEAR3.value)) {
		errorMessage += '"Projected Total Eaton Sales (Year 3)" contains an invalid value.\n';
	}
	/*
	if (isEmpty(document.theform.textfield8.value)) {
		errorMessage += '"Estimated % Breakdown of Sales (Eaton; Year 1)" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.textfield8.value)) {
		errorMessage += '"Estimated % Breakdown of Sales (Eaton; Year 1)" contains an invalid value.\n';
	}
	if (isEmpty(document.theform.textfield9.value)) {
		errorMessage += '"Estimated % Breakdown of Sales (Eaton; Year 3)" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.textfield9.value)) {
		errorMessage += '"Estimated % Breakdown of Sales (Eaton; Year 3)" contains an invalid value.\n';
	}

	if (isEmpty(document.theform.textfield10.value)) {
		errorMessage += '"Estimated % Breakdown of Sales (Compet 1; Year 1)" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.textfield10.value)) {
		errorMessage += '"Estimated % Breakdown of Sales (Compet 1; Year 1)" contains an invalid value.\n';
	}
	if (isEmpty(document.theform.textfield12.value)) {
		errorMessage += '"Estimated % Breakdown of Sales (Compet 1; Year 3)" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.textfield12.value)) {
		errorMessage += '"Estimated % Breakdown of Sales (Compet 1; Year 3)" contains an invalid value.\n';
	}

	if (isEmpty(document.theform.textfield11.value)) {
		errorMessage += '"Estimated % Breakdown of Sales (Compet 2; Year 1)" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.textfield11.value)) {
		errorMessage += '"Estimated % Breakdown of Sales (Compet 2; Year 1)" contains an invalid value.\n';
	}
	if (isEmpty(document.theform.textfield13.value)) {
		errorMessage += '"Estimated % Breakdown of Sales (Compet 2; Year 3)" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.textfield13.value)) {
		errorMessage += '"Estimated % Breakdown of Sales (Compet 2; Year 3)" contains an invalid value.\n';
	}
	*/
	if (isEmpty(document.theform.NET_AREA_IMPACT_YEAR1.value)) {
		errorMessage += '"Net Area Impact (Year 1)" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.NET_AREA_IMPACT_YEAR1.value)) {
		errorMessage += '"Net Area Impact (Year 1)" contains an invalid value.\n';
	}
	if (isEmpty(document.theform.NET_AREA_IMPACT_YEAR3.value)) {
		errorMessage += '"Net Area Impact (Year 3)" cannot be empty.\n';
	}
	else if (!isFloat(document.theform.NET_AREA_IMPACT_YEAR3.value)) {
		errorMessage += '"Net Area Impact (Year 3)" contains an invalid value.\n';
	}

	if (!isEmpty(errorMessage)) {
		alert(errorMessage);
		return false;
	}
	else {
		return true;
	}
}


