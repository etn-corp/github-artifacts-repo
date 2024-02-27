
package com.eaton.electrical.smrc.bo;


/**	This class contains all of the methods and attributes needed by the Target Account Planner
*	as needed for each detail item in the toolbox on the home page
*
*	@author Carl Abel
*/
public class ToolboxElement implements java.io.Serializable  {
	private String _desc = null;
	private String _def = null;
	private String _url = null;
	private String _image = null;
	private boolean _ieOnly = false;
	private boolean _netOnly = false;

	private static final long serialVersionUID = 100;

	public ToolboxElement(){
		_desc = "";
		_def = "";
		_url = "";
		_image = "";
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

	/** Lets the calling routine set the definition of this item
	*	@param String The definition of this item
	*/
	public void setDefinition(String def) {
		_def = def;
		
	}
	public String getDefinition() {
		if(_def==null) return "";	
		return _def;
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

	/** Lets the calling routine work with the URL
	*	@return String The url of this item
	*/
	public String getURL() {
		return _url;
	}

	/** Lets the calling routine work with the description of this item
	*	@return String The description of this item
	*/
	public String getDescription() {
		return _desc;
	}

	/** Lets the calling routine work with the image or logo of this item
	*	@return String the filename of the image or logo for this item
	*/
	public String getImage() {
		return _image;
	}

	/** Indicates this item should only be displayed when user is using Intenet Explorer
	*	@return boolean T/F indicating whether this item should only be displayed on
	*		an Internet Explorer browser.
	*/
	public boolean ieOnly() {
		return _ieOnly;
	}

	/** Indicates this item should only be displayed when user is using Netscape
	*	@return boolean T/F indicating whether this item should only be displayed on
	*		a Netscape browser.
	*/
	public boolean netOnly() {
		return _netOnly;
	}

	/** This method returns this particular item in HTML format to be displayed on a web browser
	*	@return String This item in HTML format
	*/
	public String printToHTML() {
		StringBuffer html = new StringBuffer("");

		if (!_ieOnly && !_netOnly) {
			html.append(" 				<tr><td align=left class=smallFont>\n");

			if (_url != null && _url.length() > 0) {
				html.append("					<a href=\"");
				html.append(_url);
				html.append("\" target=toolbox>");
			}

			if (_image != null && _image.length() > 0) {
				html.append("<img src=");
				html.append(_image);
				html.append(" alt='");
				html.append(_desc);
				html.append("'>");
			}
			else {
//				html.append("<div class=toolboxElement>");
				html.append(_desc);
//				html.append("</div>");
			}

			if (_url != null && _url.length() > 0) {
				html.append("</a>\n");
			}

			if (_def != null && _def.length() > 0) {
				html.append(" (");
				html.append(_def);
				html.append(")");
			}

			html.append("				</td></tr>\n");
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
				html.append("	document.write(\"				<tr><td class=smallFont><a href=");
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
//				html.append("<div class=toolboxElement>");
				html.append(_desc);
//				html.append("</div>");
			}

			if (_url != null && _url.length() > 0) {
				html.append("</a>");
			}

			if (_def != null && _def.length() > 0) {
				html.append(" (");
				html.append(_def);
				html.append(")");
			}

			html.append("</td></tr>\");\n");
			html.append("</script>\n");
		}

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

		returnString += "\tdefinition = " + this.getDefinition() + "\n";
		returnString += "\tuRL = " + this.getURL() + "\n";
		returnString += "\tdescription = " + this.getDescription() + "\n";
		returnString += "\timage = " + this.getImage() + "\n";

		return returnString;
	}
}
