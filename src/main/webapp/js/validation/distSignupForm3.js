function formValidation() {
	var errorMessage = '';
	
	if (isEmpty(document.theform.INSIDE_SALES_CONSTR_PERSONNEL.value)) {
		errorMessage += '"Inside Sales (Construction)" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.INSIDE_SALES_CONSTR_PERSONNEL.value) || document.theform.INSIDE_SALES_CONSTR_PERSONNEL.value < 0) {
		errorMessage += '"Inside Sales (Construction)" contains an invalid value.\n';
	}
	
	if (isEmpty(document.theform.INSIDE_SALES_INDSTR_PERSONNEL.value)) {
		errorMessage += '"Inside Sales (Industrial)" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.INSIDE_SALES_INDSTR_PERSONNEL.value) || document.theform.INSIDE_SALES_INDSTR_PERSONNEL.value < 0) {
		errorMessage += '"Inside Sales (Industrial)" contains an invalid value.\n';
	}
	
	if (isEmpty(document.theform.INSIDE_SALES_GEN_PERSONNEL.value)) {
		errorMessage += '"Inside Sales (General)" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.INSIDE_SALES_GEN_PERSONNEL.value) || document.theform.INSIDE_SALES_GEN_PERSONNEL.value < 0) {
		errorMessage += '"Inside Sales (General)" contains an invalid value.\n';
	}
	
	if (isEmpty(document.theform.OUTSIDE_SALES_CONSTR_PERSONNEL.value)) {
		errorMessage += '"Outside Sales (Construction)" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.OUTSIDE_SALES_CONSTR_PERSONNEL.value) || document.theform.OUTSIDE_SALES_CONSTR_PERSONNEL.value < 0) {
		errorMessage += '"Outside Sales (Construction)" contains an invalid value.\n';
	}
	
	if (isEmpty(document.theform.OUTSIDE_SALES_INDSTR_PERSONNEL.value)) {
		errorMessage += '"Outside Sales (Industrial)" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.OUTSIDE_SALES_INDSTR_PERSONNEL.value) || document.theform.OUTSIDE_SALES_INDSTR_PERSONNEL.value < 0) {
		errorMessage += '"Outside Sales (Industrial)" contains an invalid value.\n';
	}
	
	if (isEmpty(document.theform.OUTSIDE_SALES_GEN_PERSONNEL.value)) {
		errorMessage += '"Outside Sales (General)" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.OUTSIDE_SALES_GEN_PERSONNEL.value) || document.theform.OUTSIDE_SALES_GEN_PERSONNEL.value < 0) {
		errorMessage += '"Outside Sales (General)" contains an invalid value.\n';
	}
	
	if (isEmpty(document.theform.MANAGEMENT_PERSONNEL.value)) {
		errorMessage += '"Non Sales (Management)" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.MANAGEMENT_PERSONNEL.value) || document.theform.MANAGEMENT_PERSONNEL.value < 0) {
		errorMessage += '"Non Sales (Management)" contains an invalid value.\n';
	}
	
	if (isEmpty(document.theform.COUNTER_SALES_PERSONNEL.value)) {
		errorMessage += '"Non Sales (Counter Sales)" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.COUNTER_SALES_PERSONNEL.value) || document.theform.COUNTER_SALES_PERSONNEL.value < 0) {
		errorMessage += '"Non Sales (Counter Sales)" contains an invalid value.\n';
	}
	
	if (isEmpty(document.theform.SPECIALIST_PERSONNEL.value)) {
		errorMessage += '"Non Sales (Specialists)" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.SPECIALIST_PERSONNEL.value) || document.theform.SPECIALIST_PERSONNEL.value < 0) {
		errorMessage += '"Non Sales (Specialists)" contains an invalid value.\n';
	}
	
	if (isEmpty(document.theform.ELECTRICAL_ENGINEER_PERSONNEL.value)) {
		errorMessage += '"Non Sales (Staff Electrical Engineers)" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.ELECTRICAL_ENGINEER_PERSONNEL.value) || document.theform.ELECTRICAL_ENGINEER_PERSONNEL.value < 0) {
		errorMessage += '"Non Sales (Staff Electrical Engineers)" contains an invalid value.\n';
	}
	
	if (isEmpty(document.theform.WHSE_DRIVERS_PERSONNEL.value)) {
		errorMessage += '"Non Sales (Warehouse / Drivers)" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.WHSE_DRIVERS_PERSONNEL.value) || document.theform.WHSE_DRIVERS_PERSONNEL.value < 0) {
		errorMessage += '"Non Sales (Warehouse / Drivers)" contains an invalid value.\n';
	}
	
	if (isEmpty(document.theform.ADMIN_PERSONNEL.value)) {
		errorMessage += '"Non Sales (Administrative / Other)" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.ADMIN_PERSONNEL.value) || document.theform.ADMIN_PERSONNEL.value < 0) {
		errorMessage += '"Non Sales (Administrative / Other)" contains an invalid value.\n';
	}
	
	if (!isEmpty(errorMessage)) {
		alert(errorMessage);
		return false;
	}
	else {
		return true;
	}
}


