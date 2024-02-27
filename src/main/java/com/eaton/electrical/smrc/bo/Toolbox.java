
package com.eaton.electrical.smrc.bo;

import java.util.*;

/**	This class contains all of the methods and attributes needed by the Target Account Planner
*	as needed to work the toolbox
*
*	@author Carl Abel
*/
public class Toolbox implements java.io.Serializable {
	private ArrayList _groups = null;
	
	private static final long serialVersionUID = 100;

	public Toolbox(){
		_groups = new ArrayList();
		
	}

	/** Lets the calling routine add a group to the toolbox one at a time
	*	@param ToolboxGroup The group being added to the toolbox
	*/
	public void addGroup(ToolboxGroup tg) {
		_groups.add(tg);
	}

	/** Lets the calling routine add all of the groups to the toolbox at one time
	*	@param ArrayList The groups for this toolbox
	*/
	public void addGroups(ArrayList groups) {
		_groups = groups;
	}
	public ArrayList getGroups(){
		return _groups;
	}

	/** This method returns the toolbox in HTML format to be displayed on a web browser
	*	@return String The toolbox in HTML format
	*/
	public String printToHTML() {
		StringBuffer html = new StringBuffer("<tr><td><table>");

		int row = 0;

		for (int i=0; i < _groups.size(); i++) {
			ToolboxGroup tg = (ToolboxGroup)_groups.get(i);

			if (tg.getRow() > row) {
				if (row != 0) {
					html.append("</tr>\n");
				}

				html.append("<tr>\n");
				row = tg.getRow();
			}

			html.append("<td colspan=");
			html.append(tg.getColumnCount());
			html.append(" valign=top>");
			html.append(tg.printToHTML());
			html.append("</td>\n");
		}

		html.append("</tr></table></td></tr>");

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

		returnString += "\tgroups = " + this.getGroups() + "\n";

		return returnString;
	}
}
