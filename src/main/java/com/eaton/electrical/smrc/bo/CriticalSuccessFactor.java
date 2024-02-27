package com.eaton.electrical.smrc.bo;

import java.util.*;

/**
 * @author E0062708
 *
 */
public class CriticalSuccessFactor implements java.io.Serializable {
	
	String id = null;
	String name = null;
	String notes = null;
	Date dateAdded = null;
	ArrayList multipleNotes = null;
	ArrayList districtsWithNotes = null;
	String color = null;
	String userAdded = null;
	String userChanged = null;
	String userAddedName = null;
	String userChangedName = null;
	Date dateChanged = null;
	
	
	
	private static final long serialVersionUID = 100;

	public CriticalSuccessFactor() {
		id="";
		name = "";
		notes = "";
		multipleNotes = new ArrayList();
		dateAdded = new java.sql.Date( new java.util.Date().getTime());
		districtsWithNotes = new ArrayList();
		color="";
//		Date dateChanged = new java.sql.Date( new java.util.Date().getTime());
		userAdded = "";
		userChanged = "";
		userAddedName = "";
		userChangedName = "";
	}
	
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the notes.
	 */
	public String getNotes() {
		return this.notes;
	}
	/**
	 * @param notes The notes to set.
	 */
	public void setNotes(String notes) {
		this.notes = notes;
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
	 * @return Returns the districtNotes.
	 */
	public ArrayList getDistrictsWithNotes() {
		return districtsWithNotes;
	}
	/**
	 * @param districtNotes The districtNotes to set.
	 */
	public void setDistrictsWithNotes(ArrayList districtsWithNotes) {
		this.districtsWithNotes = districtsWithNotes;
	}
	/**
	 * @return Returns the alNotes.
	 */
	public Date getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	public ArrayList getMultipleNotes() {
		return multipleNotes;
	}
	public void setMultipleNotes(ArrayList multipleNotes) {
		this.multipleNotes = multipleNotes;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
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
		returnString += "\tnotes = " + this.getNotes() + "\n";
		returnString += "\tid = " + this.getId() + "\n";
		returnString += "\tdistrictsWithNotes = " + this.getDistrictsWithNotes() + "\n";
		returnString += "\tdateAdded = " + this.getDateAdded() + "\n";
		returnString += "\tmultipleNotes = " + this.getMultipleNotes() + "\n";
		returnString += "\tcolor = " + this.getColor() + "\n";

		return returnString;
	}
    /**
     * @return Returns the dateChanged.
     */
    public Date getDateChanged() {
        return dateChanged;
    }
    /**
     * @param dateChanged The dateChanged to set.
     */
    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }
    
    /**
     * @return Returns the userAddedName.
     */
    public String getUserAddedName() {
        return userAddedName;
    }
    /**
     * @param userAddedName The userAddedName to set.
     */
    public void setUserAddedName(String userAddedName) {
        this.userAddedName = userAddedName;
    }
    /**
     * @return Returns the userChangedName.
     */
    public String getUserChangedName() {
        return userChangedName;
    }
    /**
     * @param userChangedName The userChangedName to set.
     */
    public void setUserChangedName(String userChangedName) {
        this.userChangedName = userChangedName;
    }
    /**
     * @param userAdded The userAdded to set.
     */
    public void setUserAdded(String userAdded) {
        this.userAdded = userAdded;
    }
    /**
     * @param userChanged The userChanged to set.
     */
    public void setUserChanged(String userChanged) {
        this.userChanged = userChanged;
    }
}
