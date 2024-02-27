package com.eaton.electrical.smrc.bo;



public class ProjectMember implements java.io.Serializable {
	private String _userid = "";
	private int _projId = 0;
	private String _memberType = "";

	private static final long serialVersionUID = 100;

	public void setUserid(String u) {
		_userid = u;
	}

	public void setProjectId(int id) {
		_projId = id;
	}

	public void setMemberType(String mt) {
		_memberType = mt;
	}

	public String getUserid() {
		return _userid;
	}

	public int getProjId() {
		return _projId;
	}

	public String getMemberType() {
		return _memberType;
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

		returnString += "\tuserid = " + this.getUserid() + "\n";
		returnString += "\tprojId = " + this.getProjId() + "\n";
		returnString += "\tmemberType = " + this.getMemberType() + "\n";

		return returnString;
	}
}
