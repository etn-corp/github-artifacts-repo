package com.eaton.electrical.smrc.bo;

import java.util.*;

public class ModifySegmentApprovalsBO {

	private int id;
	private String vcn = "";
	private String dm = "";
	private String dmEMail = "";
	private String dmApproval = "";
	private Date dmDate;
	private String zm = "";
	private String zmEmail = "";
	private String zmApproval = "";
	private Date zmDate;
	private String channel = "";
	private String channelApproval = "";
	private Date channelDate;
	private String requester = "";
	private String requesterEmail = "";
	
	
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getChannelApproval() {
		return channelApproval;
	}
	public void setChannelApproval(String channelApproval) {
		this.channelApproval = channelApproval;
	}
	public String getDm() {
		return dm;
	}
	public void setDm(String dm) {
		this.dm = dm;
	}
	public String getDmApproval() {
		return dmApproval;
	}
	public void setDmApproval(String dmApproval) {
		this.dmApproval = dmApproval;
	}
	public String getDmEMail() {
		return dmEMail;
	}
	public void setDmEMail(String dmEMail) {
		this.dmEMail = dmEMail;
	}
	public String getRequester() {
		return requester;
	}
	public void setRequester(String requester) {
		this.requester = requester;
	}
	public String getRequesterEmail() {
		return requesterEmail;
	}
	public void setRequesterEmail(String requesterEmail) {
		this.requesterEmail = requesterEmail;
	}
	public String getVcn() {
		return vcn;
	}
	public void setVcn(String vcn) {
		this.vcn = vcn;
	}
	public String getZm() {
		return zm;
	}
	public void setZm(String zm) {
		this.zm = zm;
	}
	public String getZmApproval() {
		return zmApproval;
	}
	public void setZmApproval(String zmApproval) {
		this.zmApproval = zmApproval;
	}
	public String getZmEmail() {
		return zmEmail;
	}
	public void setZmEmail(String zmEmail) {
		this.zmEmail = zmEmail;
	}
	public Date getChannelDate() {
		return channelDate;
	}
	public void setChannelDate(Date channelDate) {
		this.channelDate = channelDate;
	}
	public Date getDmDate() {
		return dmDate;
	}
	public void setDmDate(Date dmDate) {
		this.dmDate = dmDate;
	}
	public Date getZmDate() {
		return zmDate;
	}
	public void setZmDate(Date zmDate) {
		this.zmDate = zmDate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
