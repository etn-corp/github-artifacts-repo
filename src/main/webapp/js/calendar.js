<!-- Hide script
var baseURL = '../../SalesResourceChannel'

function displayCalendar(theForm, fieldName)
{
	calendarWindow=window.open(baseURL + '/Calendar.html'
				 ,'CalendarWindow'
				 ,"toolbar=no"
				+ ",location=top"
				+ ",directories=no"
				+ ",status=no"
				+ ",menubar=no"
				+ ",scrollbars=no"
				+ ",resizeable=no"
				+ ",width=255"
				+ ",height=240")

	theForm.DATE_FIELD_NAME.value = fieldName;
}

function check_date_old (field)
{
	if (field.value.length == 0)
		return true

	var month = field.value.substring(0,2)
	var slash1 = field.value.substring(2,3)
	var day = field.value.substring(3,5)
	var slash2 = field.value.substring(5,6)
	var year = parseInt(field.value.substring(6,10))
	if (isNaN(month) ||
		slash1 != '/' ||
		isNaN(day) ||
		slash2 != '/' ||
		isNaN(year) ||
		(year < 1996))
	{
		alert('Please enter date in the form of mm/dd/yyyy')
		field.focus()
	}
}

function check_date(theField)
{
	theDate = new Date(theField.value)
	firstSlash = theField.value.indexOf('/', 0)
	lastSlash = theField.value.indexOf('/', firstSlash + 1)
	month = theField.value.substring(0, firstSlash)
	day = theField.value.substring(firstSlash + 1, lastSlash)
	year = theField.value.substring(lastSlash + 1, theField.value.length)
	rc = true

	// Step 1: check the format
	if (theDate == 'Invalid Date')
	{
		alert('Invalid Date.  Must be in mm/dd/yyyy format.')
		rc =  false
	}
	else
	{
		// Step 2: check the month
		if ((month < 1) || (month > 12))
		{
			alert('Invalid month. ' + month + '. Must be between 1 and 12.')
			rc = false
		}
		// Step 3: check the day
		else
		{
			if ((day < 1) || (day > 31))
			{
				alert('Invalid day. ' + day + '. Must be between 1 and 31.')
				rc = false
			}
			else
				switch (month)
				{
				case 4:
				case 6:
				case 9:
				case 11:
					if (day > 30)
					{
						alert('Month ' + month + ' cannot have ' + day + ' days.')
						rc = false
					}
					break;
				case 2:
					if ((isLeapYear(year) && (day > 29))
					|| ((! isLeapYear(year)) && (day > 28)))
					{
						alert('February cannot have ' + day + ' days in ' + year + '.')
						rc = false
					}
					break;
				}
			
		}
		// Step 4: check the year
		if ((year.length != 2) && (year.length != 4))
		{
			alert('Invalid year. ' + year + '. Must be 2 digits or 4 digits.')
			rc = false
		}
		// Step 5: check the century
		else 
			if (theDate.getYear(year) > 59)
			{
				theField.value = "".concat(month,'/', day, '/', theDate.getYear(year)); // + 1900)
				rc = false
			}
			else if (theDate.getYear(year) <= 60)
			{
				theField.value = "".concat(month,'/', day, '/', theDate.getYear(year) + 2000)
				rc = false
			}
	}
	if (rc)
		return true
	else
	{
		theField.focus()
		return false
	}
}

function isLeapYear (year)
{
    if (((year % 4) == 0) && ((year % 100) != 0) || ((year % 400) == 0))
        return true;
    else
        return false;
}


function isEmpty (field)
{
	if (field.value == 0)
	{
		alert('Field is empty');
		field.focus();
		return
	}
}

function replicate(month)
{
    for (i = month; i < ((2 * 12) + 2); i+=2)
        if (document.INPUT.elements[i].type == 'text')
            document.INPUT.elements[i].value = document.INPUT.elements[month].value;

    updateTotals(document.INPUT.elements[i - 2]);
}

// -->
