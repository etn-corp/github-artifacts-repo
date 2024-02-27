package com.eaton.electrical.smrc.util;

import java.text.*;

/**
 * @author E0062708
 *
 */
public class Money {
	public static String formatDoubleAsDollars(double value)
	{
	     //Round the value to two decimal places
	     NumberFormat myFormatter = NumberFormat.getInstance();
	     myFormatter.setMaximumFractionDigits(0);
	     myFormatter.setMinimumFractionDigits(0);
	     String output = myFormatter.format(value);
	     
	     return output;
	}


	public static String formatDoubleAsDollarsAndCents(double value)
	{
		DecimalFormat myFormatter = new DecimalFormat("###.##");
		myFormatter.setMaximumFractionDigits(2);
		myFormatter.setMinimumFractionDigits(2);
		String output = myFormatter.format(value);

	    return output;
	}

	public static String formatDoubleAsDollarsAndCents(String value)
	{
		double dblValue = Globals.a2double(value);
		DecimalFormat myFormatter = new DecimalFormat("###.##");
		myFormatter.setMaximumFractionDigits(2);
		myFormatter.setMinimumFractionDigits(2);
		String output = myFormatter.format(dblValue);

	    return output;
	}
	
}
