package com.eaton.electrical.smrc.bo;

import java.util.*;

public class ModifySegmentsHistoryBO {
	private int id;
	private String vcn;
	private String requester;
	private String dm;
	private Date dmDate;
	private String dmApproval;
	private String zm;
	private Date zmDate;
	private String zmApproval;
	private String channel;
	private Date channelDate;
	private String channelApproval;
	private ArrayList segments;
	
	
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public Date getChannelDate() {
		return channelDate;
	}
	public void setChannelDate(Date channelDate) {
		this.channelDate = channelDate;
	}
	public String getDm() {
		return dm;
	}
	public void setDm(String dm) {
		this.dm = dm;
	}
	public Date getDmDate() {
		return dmDate;
	}
	public void setDmDate(Date dmDate) {
		this.dmDate = dmDate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRequester() {
		return requester;
	}
	public void setRequester(String requester) {
		this.requester = requester;
	}
	public ArrayList getSegments() {
		return segments;
	}
	public void setSegments(ArrayList segments) {
		this.segments = segments;
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
	public Date getZmDate() {
		return zmDate;
	}
	public void setZmDate(Date zmDate) {
		this.zmDate = zmDate;
	}
	public String getChannelApproval() {
		return channelApproval;
	}
	public void setChannelApproval(String channelApproval) {
		this.channelApproval = channelApproval;
	}
	public String getDmApproval() {
		return dmApproval;
	}
	public void setDmApproval(String dmApproval) {
		this.dmApproval = dmApproval;
	}
	public String getZmApproval() {
		return zmApproval;
	}
	public void setZmApproval(String zmApproval) {
		this.zmApproval = zmApproval;
	}
	
}
