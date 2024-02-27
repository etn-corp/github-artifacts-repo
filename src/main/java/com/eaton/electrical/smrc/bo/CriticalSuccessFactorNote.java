package com.eaton.electrical.smrc.bo;

import java.util.*;

/**
 * @author E0062708
 *
 */
public class CriticalSuccessFactorNote implements java.io.Serializable {
	
	int id = 0;
	String note = null;
	String geog = null;
	String geogDescription = null;
	Date dateAdded = null;
	Date dateChanged = null;
	boolean active = false;
	String userAdded = null;
	String userChanged = null;
	String userAddedName = null;
	String userChangedName = null;
	String color = null;
	
		
	private static final long serialVersionUID = 100;

	public CriticalSuccessFactorNote() {
		note = "";
		geog = "";
		geogDescription = "";
		dateAdded = new java.sql.Date( new java.util.Date().getTime());
		dateChanged = new java.sql.Date( new java.util.Date().getTime());
		userAdded = "";
		userChanged = "";
		userAddedName = "";
		userChangedName = "";
		color = "";
		
	}

	
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Date getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	public Date getDateChanged() {
		return dateChanged;
	}
	public void setDateChanged(Date dateChanged) {
		this.dateChanged = dateChanged;
	}
	public String getGeogDescription() {
		return geogDescription;
	}
	public void setGeogDescription(String geogDescription) {
		this.geogDescription = geogDescription;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getGeog() {
		return geog;
	}
	public void setGeog(String geog) {
		this.geog = geog;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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

		returnString += "\tdateAdded = " + this.getDateAdded() + "\n";
		returnString += "\tdateChanged = " + this.getDateChanged() + "\n";
		returnString += "\tgeogDescription = " + this.getGeogDescription() + "\n";
		returnString += "\tnote = " + this.getNote() + "\n";
		returnString += "\tgeog = " + this.getGeog() + "\n";
		returnString += "\tid = " + this.getId() + "\n";

		return returnString;
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
    /**
     * @return Returns the color.
     */
    public String getColor() {
        return color;
    }
    /**
     * @param color The color to set.
     */
    public void setColor(String color) {
        this.color = color;
    }
}
