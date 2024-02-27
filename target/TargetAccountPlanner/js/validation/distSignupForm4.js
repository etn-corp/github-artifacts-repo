function formValidation() {
	var marketCoveragePercent=0;
	var errorMessage = '';
	

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
		return false;
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



