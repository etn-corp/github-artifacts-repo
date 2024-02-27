package com.eaton.electrical.smrc.bo;

/**
 * @author E0062708
 *
 */
public class CodeType implements java.io.Serializable {
	
	
	private int id = -1;
	private String name = null;
	private String value = null;
	private int seq = -1;
	private String description = null;
	
	private static final long serialVersionUID = 100;

	public CodeType(){
		name = "";
		value = "";
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
		
	}
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description = description;
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
		
		returnString += "\tname = " + this.getName() + "\n";
		returnString += "\tseq = " + this.getSeq() + "\n";
		returnString += "\tvalue = " + this.getValue() + "\n";
		returnString += "\tid = " + this.getId() + "\n";
		returnString += "\tdescription = " + this.getDescription() + "\n";
		
		return returnString;
	}
}
