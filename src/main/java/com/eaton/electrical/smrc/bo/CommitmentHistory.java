package com.eaton.electrical.smrc.bo;

import java.util.*;

public class CommitmentHistory {
	private String vcn;
	private String approver_name;
	private String action;
	private Date date;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getApprover_name() {
		return approver_name;
	}
	public void setApprover_name(String approver_name) {
		this.approver_name = approver_name;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getVcn() {
		return vcn;
	}
	public void setVcn(String vcn) {
		this.vcn = vcn;
	}
}
