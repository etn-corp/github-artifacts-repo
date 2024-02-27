function formValidation() {
	var errorMessage = '';
	
	if (isEmpty(document.theform.chartHeight.value)) {
		errorMessage += '"Height" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.chartHeight.value)) {
		errorMessage += '"Height" must contain an integer value.\n';
	}

	if (isEmpty(document.theform.chartWidth.value)) {
		errorMessage += '"Width" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.chartWidth.value)) {
		errorMessage += '"Width" must contain an integer value.\n';
	}
	
	if (isEmpty(document.theform.titleFontSize.value)) {
		errorMessage += '"Title Font Size" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.titleFontSize.value)) {
		errorMessage += '"Title Font Size" must contain an integer value.\n';
	}
	
	if (isEmpty(document.theform.legendFontSize.value)) {
		errorMessage += '"Legend Font Size" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.legendFontSize.value)) {
		errorMessage += '"Legend Font Size" must contain an integer value.\n';
	}

	if (!isEmpty(errorMessage)) {
		alert(errorMessage);
		return false;
	}
	else {
		return true;
	}
	

}

var setInnerHTML = function( id, str ){
    if(!document.getElementById) return; // Not Supported
	if(document.getElementById){
		document.getElementById(id).innerHTML = str;
	}
	
}


