package com.eaton.electrical.smrc.bo;



public class DistRptSubsection implements java.io.Serializable {
	private String _groupId = "";
	private int _sectionId = 0;
	private int _subsectionId = 0;
	private String _desc = "";
	private int _seqNum = 0;
	
	private static final long serialVersionUID = 100;

	public void setGroupId(String group) {
		_groupId = group;
	}
	
	public void setSectionId(int sect) {
		_sectionId = sect;
	}
	
	public void setSubsectionId(int ssId) {
		_subsectionId = ssId;
	}
	
	public void setDescription(String desc) {
		_desc = desc;
	}
	
	public void setSeqNum(int seq) {
		_seqNum = seq;
	}
	
	public String getGroupId() {
		return _groupId;
	}
	
	public int getSectionId() {
		return _sectionId;
	}
	
	public int getSubsectionId() {
		return _subsectionId;
	}
	
	public String getDescription() {
		return _desc;
	}
	
	public int getSeqNum() {
		return _seqNum;
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

		returnString += "\tgroupId = " + this.getGroupId() + "\n";
		returnString += "\tsectionId = " + this.getSectionId() + "\n";
		returnString += "\tsubsectionId = " + this.getSubsectionId() + "\n";
		returnString += "\tdescription = " + this.getDescription() + "\n";
		returnString += "\tseqNum = " + this.getSeqNum() + "\n";

		return returnString;
	}
}
