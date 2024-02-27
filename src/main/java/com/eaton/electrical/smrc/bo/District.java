package com.eaton.electrical.smrc.bo;


/**
 * @author E0062708
 *
 */
public class District implements java.io.Serializable {
	private String id = null;
	private String name = null;
	private String code = null;
	private String description = null;
	private String cSFNotes = null;

	private static final long serialVersionUID = 100;
	
	public District() {
		id = "";
		name = "";
		code = "";
		description = "";
		cSFNotes = "";

	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the cSFNotes.
	 */
	public String getCSFNotes() {
		return cSFNotes;
	}
	/**
	 * @param notes The cSFNotes to set.
	 */
	public void setCSFNotes(String notes) {
		cSFNotes = notes;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String toString(){
		return this.code;
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

}
