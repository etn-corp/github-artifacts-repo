package com.eaton.electrical.smrc.bo;

public class ModifySegmentBO {
	public String vcn;
	public int segmentID;
	public String isnewSegment;
	public String approved;
	
	
	public String getApproved() {
		return approved;
	}
	public void setApproved(String approved) {
		this.approved = approved;
	}
	public String getIsnewSegment() {
		return isnewSegment;
	}
	public void setIsnewSegment(String isnewSegment) {
		this.isnewSegment = isnewSegment;
	}
	public int getSegmentID() {
		return segmentID;
	}
	public void setSegmentID(int segmentID) {
		this.segmentID = segmentID;
	}
	public String getVcn() {
		return vcn;
	}
	public void setVcn(String vcn) {
		this.vcn = vcn;
	}
	
	
}
