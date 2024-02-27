
package com.eaton.electrical.smrc.bo;

import java.util.*;

/**	This class contains all of the methods and attributes needed by the Target Account Planner
*	as needed to work with a grouping of toolbox elements (including the header)
*
*	@author Carl Abel
*/
public class ToolboxGroup implements java.io.Serializable {
	private String _desc = null;
	private String _url = null;
	private String _image = null;
	private boolean _ieOnly = false;
	private boolean _netOnly = false;
	private int _row = 0;
	private int _col = 0;
	private int _colspan = 1;
	private ArrayList _details = null;

	private static final long serialVersionUID = 100;

	public ToolboxGroup(){
		_desc = "";
		_url = "";
		_image = "";
		_details = new ArrayList();
	}
	
	/** Lets calling routine set the url for this item
	*	@param String The url for this item
	*/
	public void setURL (String url) {
		_url = url;
	}

	/** Lets the calling routine set the description to be displayed for this item
	*	@param String the description of this item
	*/
	public void setDescription(String desc) {
		_desc = desc;
	}

	/** Lets the calling routine set the image or logo of this item
	*	@param String The filename of this item's image or logo
	*/
	public void setImage (String image) {
		_image = image;
	}

	/** Lets the calling routine set a browser flag for this item.
	*	<ul><li>I = Internet Explorer only</li><li>N = Netscape only</li></ul>
	*	All other codes will be ignored and this item will be displayed in all browsers.
	*	@param String The browser flag
	*/
	public void setBrowserFlag(String flag) {
		if (flag.equals("I")) {
			_ieOnly = true;
		}
		else if (flag.equals("N")) {
			_netOnly = true;
		}
	}

	/** Lets the calling routine set the row number this grouping should be located
	*	@param int The row number this grouping should be located.
	*/
	public void setRow(int row) {
		_row = row;
	}

	/** Lets the calling routine set the column this grouping should be located
	*	@param int The column this grouping should be located
	*/
	public void setColumn(int col) {
		_col = col;
	}

	/** Lets the calling routine set the number of columns this grouping should span
	*	@param int The number of columns this grouping should span
	*/
	public void setColumnCount(int colspan) {
		_colspan = colspan;
	}

	/** Lets the calling routine add a detailed element to this grouping one at a time
	*	@param ToolboxElement The detail being added to this grouping
	*/
	public void addDetail(ToolboxElement te) {
		_details.add(te);
	}

	/** Lets the calling routine add all of the details to this grouping at one time
	*	@param ArrayList The details for this grouping
	*/
	public void addDetails(ArrayList details) {
		_details = details;
	}
	public ArrayList getDetails() {
		return _details;
	}
	/** Lets the calling routine retrieve the url for the header of this grouping
	*	@return String The url for the header
	*/
	public String getURL() {
		return _url;
	}

	/** Lets the calling routine retrieve the description of this grouping
	*	@return String The description of this grouping's header
	*/
	public String getDescription() {
		return _desc;
	}

	/** Lets the calling routine retrieve the image or logo for this grouping
	*	@return String The image or logo for this grouping
	*/
	public String getImage() {
		return _image;
	}

	/** Indicates this grouping should only be displayed when user is using Intenet Explorer
	*	@return boolean T/F indicating whether this grouping should only be displayed on
	*		an Internet Explorer browser.
	*/
	public boolean ieOnly() {
		return _ieOnly;
	}

	/** Indicates this grouping should only be displayed when user is using Netscape
	*	@return boolean T/F indicating whether this grouping should only be displayed on
	*		a Netscape browser.
	*/
	public boolean netOnly() {
		return _netOnly;
	}

	/** Lets the calling routine know which row this grouping is to be placed
	*	@return int The row this grouping should be placed
	*/
	public int getRow() {
		return _row;
	}

	/** Lets the calling routine know which column this grouping is to be placed
	*	@return int The column this grouping should be placed
	*/
	public int getColumn() {
		return _col;
	}

	/** Lets the calling routine know how many columns this grouping should span
	*	@return int The number of columns this grouping should span
	*/
	public int getColumnCount() {
		return _colspan;
	}

	/** This method prints all of the toolbox details attached to this grouping
	*	@return String All of the toolbox details attached to this grouping
	*/
	private String printDetails() {
//		StringBuffer output = new StringBuffer("<table>\n");
		StringBuffer output = new StringBuffer("");
		for (int i=0; i < _details.size(); i++) {
			ToolboxElement te = (ToolboxElement)_details.get(i);
			output.append(te.printToHTML());
		}
//		output.append("</table>\n");
		return output.toString();
	}

	/** This method returns this particular grouping in HTML format to be displayed on a web browser
	*	@return String This grouping in HTML format
	*/
	public String printToHTML() {
		StringBuffer html = new StringBuffer("<table>");

		if (!_ieOnly && !_netOnly) {
			html.append("	<tr><td>");

			if (_url != null && _url.length() > 0) {
				html.append("					<a href=\"");
				html.append(_url);
				html.append(" target=toolbox>");
			}

			if (_image != null && _image.length() > 0) {
				html.append("<img src=");
				html.append(_image);
				html.append(" alt='");
				html.append(_desc);
				html.append("'>");
			}
			else {
				html.append("<div class=toolboxHeader>");
				html.append(_desc);
				html.append("</div>");
			}

			if (_url != null && _url.length() > 0) {
				html.append("</a>\n");
			}

			html.append("	</td></tr>\n");
			html.append(printDetails());
		}
		else {
			html.append("<script language='javascript'>\n");
			html.append("var isNav = (navigator.appName.indexOf(\"Netscape\") != -1);\n");

			if (_netOnly) {
				html.append("if (isNav)\n");
			}
			else if (_ieOnly) {
				html.append("if (!isNav)\n");
			}

			if (_url != null && _url.length() > 0) {
				html.append("	document.write(\"<tr><td><a href=");
				html.append(_url);
				html.append(" target=docs>");
			}

			if (_image != null && _image.length() > 0) {
				html.append("<img src=");
				html.append(_image);
				html.append(" alt='");
				html.append(_desc);
				html.append("'>");
			}
			else {
				html.append("<div class=toolboxHeader>");
				html.append(_desc);
				html.append("</div>");
			}

			if (_url != null && _url.length() > 0) {
				html.append("</a>");
			}

			html.append(printDetails());
			html.append("	</td></tr>\");\n");
			html.append("</script>\n");
		}

		html.append("</table>");
		return html.toString();
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public boolean equals(Object obj) {
		if (this == null || obj == null) {
			return false;
		}

		return super.equals(obj);
	}

	public String toString() {
		String returnString = "";

		returnString += "\tdetails = " + this.getDetails() + "\n";
		returnString += "\tuRL = " + this.getURL() + "\n";
		returnString += "\tdescription = " + this.getDescription() + "\n";
		returnString += "\timage = " + this.getImage() + "\n";
		returnString += "\trow = " + this.getRow() + "\n";
		returnString += "\tcolumn = " + this.getColumn() + "\n";
		returnString += "\tcolumnCount = " + this.getColumnCount() + "\n";

		return returnString;
	}
}
