package com.eaton.electrical.smrc.bo;

import java.util.*;
import java.text.*;
import com.eaton.electrical.smrc.util.*;
import com.eaton.electrical.smrc.service.*;
import com.eaton.electrical.smrc.dao.*;
import java.sql.Connection;

public class CommitmentChangeRequest {
	
	private long id;
	private String vcn;
	private String name;
	private String approverName;
	private String approvalFlag;
	private Date dateAdded;
	private int approvalOrder;
	private int comLevel;
	private int projSales1;
	private int projSales3;
	private int percSales1;
	private int percSales3;
	private String electricLines;
	
	
	public int getApprovalOrder() {
		return approvalOrder;
	}
	public void setApprovalOrder(int approvalOrder) {
		this.approvalOrder = approvalOrder;
	}
	public String getApproverName() {
		return approverName;
	}
	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}
	public String getApprovalFlag() {
		return approvalFlag;
	}
	public void setApprovalFlag(String aprovalFlag) {
		this.approvalFlag = aprovalFlag;
	}
	public int getComLevel() {
		return comLevel;
	}
	public void setComLevel(int comLevel) {
		this.comLevel = comLevel;
	}
	public Date getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	public String getElectricLines() {
		return electricLines;
	}
	public void setElectricLines(String electricLines) {
		this.electricLines = electricLines;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getPercSales1() {
		return percSales1;
	}
	public void setPercSales1(int percSales1) {
		this.percSales1 = percSales1;
	}
	public int getPercSales3() {
		return percSales3;
	}
	public void setPercSales3(int percSales3) {
		this.percSales3 = percSales3;
	}
	public int getProjSales1() {
		return projSales1;
	}
	public void setProjSales1(int projSales1) {
		this.projSales1 = projSales1;
	}
	public int getProjSales3() {
		return projSales3;
	}
	public void setProjSales3(int projSales3) {
		this.projSales3 = projSales3;
	}
	public String getVcn() {
		return vcn;
	}
	public void setVcn(String vcn) {
		this.vcn = vcn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	

}
