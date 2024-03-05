function formValidation() {
	var errorMessage = '';
	
	if (isEmpty(document.theform.FEDERAL_TAX_ID.value)) {
		errorMessage += '"Federal Tax ID" cannot be empty.\n';
	}
	
	if(isEmpty(document.theform.DUNN_BRADSTREET.value)) {
		errorMessage += '"Dunn Bradstreet Number" cannot be empty.\n';
	}

	if(document.theform.LOCATION_TYPE_ID.selectedIndex == 0) {
		errorMessage += '"Location Type" cannot be empty.\n';
	}
	
	if(document.theform.CUSTOMER_CATEGORY.selectedIndex == 0) {
		errorMessage += '"Customer Category" cannot be empty.\n';
	}
	/* Assumes the first element in the drop down is "Select One..." */
	if (document.theform.APPLYING_FOR_TYPE_ID.selectedIndex == 0) {
		errorMessage += '"Applying For (Drop Down)" must choose an option.\n';
	}
	/* Assumes the last element in the drop down is "Other" */
	if (document.theform.APPLYING_FOR_TYPE_ID.selectedIndex == document.theform.APPLYING_FOR_TYPE_ID.length - 1) {
		if (isEmpty(document.theform.APPLYING_FOR_OTHER_NOTES.value)) {
			errorMessage += '"Applying For (if other please explain)" cannot be empty if "Applying For TYPE ID (Drop Down)" is "Other"\n'
		}
	}

	/* Assumes the first element in the drop down is "Select One..." */
	if (document.theform.OWNERSHIP_FORM_TYPE_ID.selectedIndex == 0) {
		errorMessage += '"Form of Ownership (Drop Down)" must choose an option.\n';
	}
	/* Assumes the last element in the drop down is "Other" */
	if (document.theform.OWNERSHIP_FORM_TYPE_ID.selectedIndex == document.theform.OWNERSHIP_FORM_TYPE_ID.length - 1) {
		if (isEmpty(document.theform.OWNERSHIP_FORM_NOTES.value)) {
			errorMessage += '"Form of Ownership (Subsidiary of Whom OR Explanation for other)" cannot be empty if "Form of Ownership (Drop Down)" is "Other"\n';
		}
	}
	/* Assumes the next to last element in the drop down is "Subsidiary" */
	if (document.theform.OWNERSHIP_FORM_TYPE_ID.selectedIndex == document.theform.OWNERSHIP_FORM_TYPE_ID.length - 2) {
		if (isEmpty(document.theform.OWNERSHIP_FORM_NOTES.value)) {
			errorMessage += '"Form of Ownership (Subsidiary of Whom OR Explanation for other)" cannot be empty if "Form of Ownership (Drop Down)" is "Subsidiary"\n';
		}
	}
	
	if(document.theform.PRIMARY_BUS_ACTIVITY_TYPE_ID.selectedIndex == 0) {
		errorMessage += '"Primary Business Activity" cannot be empty.\n';
	}
	
	if (isEmpty(document.theform.FACILITY_AREA.value)) {
		errorMessage += '"Approximate total square footage at this facility" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.FACILITY_AREA.value)) {
		errorMessage += '"Approximate total square footage at this facility" contains an invalid value.\n';
	}
	
	isChecked = false;
	for(f=0; f < document.theform.FACILITIES.length; f++) {
		if(document.theform.FACILITIES[f].checked) {
			isChecked = true;
		}
	}
	if(!isChecked) {
		errorMessage += '"Facilities" cannot be empty.\n';
	}

	
	if(document.theform.CONTACT_LAST_NAME == null) {
		errorMessage += 'There must be at least one contact provided.\n';
	} else if(document.theform.CONTACT_LAST_NAME.length <= 0) {
		errorMessage += 'There must be at least one contact provided.\n';
	}
	
	//isChecked = false;
	//for(c=0; c < document.theform.COMMITMENT_PROGRAM.length; c++) {
	//	if(document.theform.COMMITMENT_PROGRAM[c].checked) {
	//		isChecked = true;
	//	}
	//}
	//if(!isChecked) {
	//	errorMessage += 'A Commitment Program must be selected.\n';
	//}
	
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
	
	if (isEmpty(document.theform.PROJECTED_SALES_VS_COMP_1.value)) {
		errorMessage += '"Anticipated Eaton Sales (Year 1)" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.PROJECTED_SALES_VS_COMP_1.value)) {
		errorMessage += '"Anticipated Eaton Sales (Year 1)" contains an invalid value.\n';
	}
	
	if (isEmpty(document.theform.PROJECTED_SALES_VS_COMP_2.value)) {
		errorMessage += '"Anticipated Eaton Sales (Year 2)" cannot be empty.\n';
	}
	else if (!isInteger(document.theform.PROJECTED_SALES_VS_COMP_2.value)) {
		errorMessage += '"Anticipated Eaton Sales (Year 2)" contains an invalid value.\n';
	}
	
	isChecked = false;
	for(d=0; d < document.theform.ELECTRICAL_LINES.length; d++) {
		if(document.theform.ELECTRICAL_LINES[d].checked) {
			isChecked = true;
		}
	}
	if(!isChecked) {
		errorMessage += '"Competting Electrical Lines" cannont be empty.\n';
	}
	
	if (document.theform.DISTRIBUTOR_NOTES.value.length > 4000) {
		errorMessage += '"Other notes or comments" cannot be longer than 4000 characters.\n';
	}
	
	errorMessage+=checkPercent(document.theform.COUNTY_SALES1,document.theform.COUNTY_SALES1_VALUE);
	errorMessage+=checkPercent(document.theform.COUNTY_SALES2,document.theform.COUNTY_SALES2_VALUE);
	errorMessage+=checkPercent(document.theform.COUNTY_SALES3,document.theform.COUNTY_SALES3_VALUE);
	errorMessage+=checkPercent(document.theform.COUNTY_SALES4,document.theform.COUNTY_SALES4_VALUE);
	errorMessage+=checkPercent(document.theform.COUNTY_SALES5,document.theform.COUNTY_SALES5_VALUE);
	errorMessage+=checkPercent(document.theform.COUNTY_SALES6,document.theform.COUNTY_SALES6_VALUE);
	errorMessage+=checkPercent(document.theform.COUNTY_SALES7,document.theform.COUNTY_SALES7_VALUE);
	errorMessage+=checkPercent(document.theform.COUNTY_SALES8,document.theform.COUNTY_SALES8_VALUE);
	errorMessage+=checkPercent(document.theform.COUNTY_SALES9,document.theform.COUNTY_SALES9_VALUE);
	errorMessage+=checkPercent(document.theform.COUNTY_SALES10,document.theform.COUNTY_SALES10_VALUE);
	errorMessage+=checkPercent(document.theform.COUNTY_SALES11,document.theform.COUNTY_SALES11_VALUE);
	errorMessage+=checkPercent(document.theform.COUNTY_SALES12,document.theform.COUNTY_SALES12_VALUE);
	errorMessage+=validateSegmentPercentage();
//	errorMessage+=validateIntoStockPercentage(); - removed 3/23/05 - jdl
	errorMessage+=validateMarketPercentage();
	
	if (!isEmpty(errorMessage)) {
		alert(errorMessage);
		//return false;
	}
	else {
		return true;
	}
}

function checkPercent(county, percent){
	var errorMessage='';
	if(county.selectedIndex != 0){
		if (isEmpty(percent.value)) {
			errorMessage += 'A "Market Coverage" percentage is required.\n';
		}else if (!isPercent(percent.value)) {
			errorMessage += 'A "Market Coverage" percentage is not valid.  Invalid: ' + percent.value +'\n';
		}
	}
	return errorMessage;
}

var setInnerHTML = function( id, str ){
	if(!document.getElementById) return; // Not Supported
	if(document.getElementById){
		document.getElementById(id).innerHTML = str;
	}
}

function validateSegmentPercentage(){
    errorMessage = '';
	total = getSegmentPercentage();
	if (total != 100){
	    errorMessage = 'Customer Segments percentage must equal 100%. \n';
	} 
	return errorMessage;

}

function validateIntoStockPercentage(){
    errorMessage = '';
	total = getIntoStockPercentage();
	if (total != 100){
	    errorMessage = 'Product/Sales Percentage Sold Through Stock must equal 100%. \n';
	} 
	return errorMessage;

}

function validateMarketPercentage(){
    errorMessage = '';
	total = getMarketPercentage();
	if (total != 100){
	    errorMessage = 'Market Coverage Percentage Sold Into Each County must equal 100%. \n';
	} 
	return errorMessage;

}


