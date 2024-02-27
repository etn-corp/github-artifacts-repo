package com.eaton.electrical.smrc.bo;

import java.io.*;
import java.sql.*;

public class TaskAttachment implements Serializable {
	
	private long id = 0;
	private String fileName = null;
	private Blob attachment = null;
	private String contentType = null;
	private long taskId = 0;
	
	private static final long serialVersionUID = 100;

	public TaskAttachment (){
		fileName = "";
		contentType = "";
	}
	
	public Blob getAttachment() {
		return attachment;
	}
	public void setAttachment(Blob attachment) {
		this.attachment = attachment;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
	

}
