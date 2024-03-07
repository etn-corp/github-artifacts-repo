function formValidation() {
	var errorMessage = '';

	if (!isEmpty(errorMessage)) {
		alert(errorMessage);
		return false;
	}
	else {
		return true;
	}
}

