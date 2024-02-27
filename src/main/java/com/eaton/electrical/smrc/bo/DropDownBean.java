/*
 * Created on Feb 4, 2005
 *
 */
package com.eaton.electrical.smrc.bo;

/**
 * @author Jason Lubbert
 *
  */
public class DropDownBean implements java.io.Serializable {
    String name = null;
    String value = null;
    
	private static final long serialVersionUID = 100;
    
    public DropDownBean(){
        name = "";
        value = "";
        
    }
    public DropDownBean(String name, String value){
        this.name = name;
        this.value = value;
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
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }
    /**
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }
}
