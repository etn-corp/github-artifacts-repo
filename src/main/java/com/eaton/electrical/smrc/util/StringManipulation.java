package com.eaton.electrical.smrc.util;

/**
 * @author E0062708
 *
 */
public class StringManipulation {
	
	public static String displayQuotesInInput(String in) {
	    if(in==null) return "";
	    StringBuffer newString = new StringBuffer("");
		
		for (int i=0; i < in.length(); i++) {
			char c = in.charAt(i);
			
			if (c == '\'') {
				newString.append("&acute;");
			}
			else {
				newString.append(c);
			}
		}
		
		return newString.toString();
	}
	
	public static String addBackslashToQuotes(String in) {
	    if(in==null) return "";
	    StringBuffer newString = new StringBuffer("");
		
		for (int i=0; i < in.length(); i++) {
			char c = in.charAt(i);
			
			if ((c == '\'') || (c == '\"')) {
				newString.append("\\");
				newString.append(c);
			}
			else {
				newString.append(c);
			}
		}
		
		return newString.toString();
	}
	
	public static String newLineToBR(String in) {
	    if(in==null) return "";
		//StringBuffer tmp = new StringBuffer(displayQuotesInInput(r.getString("notes")));
		StringBuffer tmp = new StringBuffer(in);
		StringBuffer tmp2 = new StringBuffer("");
		
		for (int i=0; i < tmp.length(); i++) {
			char c = tmp.charAt(i);
			
			if (c == '\n') {
				tmp2.append("<br>");
			}
			else {
				tmp2.append(c);
			}
		}
		
		return tmp2.toString();
	}
	
	public static String fixQuotes(String in) {
		if(in==null) return "";
	    
	    StringBuffer newString = new StringBuffer("");
		
		for (int i=0; i < in.length(); i++) {
			char c = in.charAt(i);
			
			if (c == '\'') {
				newString.append("'");
			}
			
			newString.append(c);
		}
		
		return newString.toString();
	}
	
	public static String fixSQLQuotes(String in) {
	    if(in==null) return "";
	    
		StringBuffer newString = new StringBuffer("");
		
		for (int i=0; i < in.length(); i++) {
			char c = in.charAt(i);
			
			if (c == '\'') {
				newString.append("''");
			}else{
				newString.append(c);
			}
		}
		
		return newString.toString();
	}
	
	
	public static String noNull(String s)
	{
		if ( s == null ) return "" ;
		return s ;
	}
	public static String checkInt(String s)
	{
		if ( s == null || s.equals("")){
			return "0";
		}
		return s;
	}
	
	
	
	public static String createTextBox(String name, String value, boolean editable, String length){
		return createTextBox(name, value, editable, length, "200");
	}
	
	public static String createTextBox(String name, String value, boolean editable, String length, String maxLength){
		if(editable){
		    return "<input name=\"" + name + "\" type=\"text\" value=\"" + value + "\" size=\"" + length + "\" maxlength=\"" + maxLength + "\">";
		}
		return value+"<input name=\"" + name + "\" type=\"hidden\" value=\"" + value + "\">";
	}	
	public static String createTextBoxWithOnBlur(String name, String value, boolean editable, String length, String maxLength, String functionCalled){
		if(editable){
		    return "<input name=\"" + name + "\" type=\"text\" value=\"" + value + "\" size=\"" + length + "\" maxlength=\"" + maxLength + "\" onBlur=\"" + functionCalled + "\">";
		}
		return value+"<input name=\"" + name + "\" type=\"hidden\" value=\"" + value + "\">";
	}
	
	public static String removeCurrency(String inString){
	    StringBuffer returnValue = new StringBuffer();
        for (int i=0; i< inString.length(); i++){
            char thisChar = inString.charAt(i);
            if ((thisChar != ',') && (thisChar != '$')){
                returnValue.append(thisChar);
            }
        }
        return returnValue.toString();
	}
	
	// This method finds all backslashes and adds another one so they are not lost
	// as escape characters
	public static String addExtraBackslash(String inString){
		StringBuffer modString = new StringBuffer();
		for (int x=0; x < inString.length(); x++){
			if (inString.charAt(x) == '\\'){
				modString.append("\\");
			}
			modString.append(inString.charAt(x));
		}
		return modString.toString();
	}
	
	/**
	 * Returns a string of tabs based on the number inputted.  For instance, if numberOfIndents is 3 then the
	 * string returned is '\t\t\t'.  This is used for logging formatting.
	 * 
	 * @param numberOfIndents Number of tabs to build into return string
	 * @return String of tabs
	 */
	
	public static String indentTabs(int numberOfIndents) {
		
		StringBuffer returnBuffer = new StringBuffer();
		
		for (int i = 0; i < numberOfIndents; i++) {
			
			returnBuffer.append("\t");
			
		}
		
		return returnBuffer.toString();
		
	} // end indentTabs
	
}
