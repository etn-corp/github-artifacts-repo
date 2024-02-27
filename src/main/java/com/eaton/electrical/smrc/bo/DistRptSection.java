
package com.eaton.electrical.smrc.bo;

import java.util.*;

public class DistRptSection implements java.io.Serializable {
	private ArrayList _subSections = new ArrayList(10);
	private int _ssCtr = -1;
	private int _sectionId = 0;
	private String _desc = "";
	private String _groupId = "";
	
	private static final long serialVersionUID = 100;

	public void setSectionId(int sectId) {
		_sectionId = sectId;
	}
	
	public void setDescription(String desc) {
		_desc = desc;
	}
	
	public void setGroupId(String group) {
		_groupId = group;
	}
	
	public void addSubSection(DistRptSubsection subSection) {
		_subSections.add(subSection);
	}
	
	public int getSectionId() {
		return _sectionId;
	}
	
	public String getDescription() {
		return _desc;
	}
	
	public String getGroupId() {
		return _groupId;
	}
	
	public boolean moreSubsections() {
		return (_ssCtr < (_subSections.size() - 1));
	}
	
	public DistRptSubsection nextSubsection() {
		if (!moreSubsections()) {
			return null;
		}
		
		_ssCtr++;
		return (DistRptSubsection)_subSections.get(_ssCtr);
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

		returnString += "\tsectionId = " + this.getSectionId() + "\n";
		returnString += "\tdescription = " + this.getDescription() + "\n";
		returnString += "\tgroupId = " + this.getGroupId() + "\n";

		return returnString;
	}
}
